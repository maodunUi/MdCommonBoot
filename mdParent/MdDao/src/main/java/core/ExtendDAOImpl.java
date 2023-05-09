package core;

import entity.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.jooq.*;
import org.jooq.impl.DAOImpl;
import org.jooq.impl.DSL;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.jooq.impl.DSL.row;


/**
 * @author Diamond
 */
@Slf4j
public abstract class ExtendDAOImpl<R extends UpdatableRecord<R>, P, T> extends DAOImpl<R, P, T>
        implements ExtendDAO<R, P, T> {
    static final String SELECT = "select";
    /**
     * 强制使用Slave，连接配置的会话级别为 全局一致性，超时策略为发送到主节点，触发超时后，FOUND_ROWS 结果不准确
     * 分页查询通常不属于对应事务，所以可以直接使用子节点, 通过Mysql Hint来强制sql走从库
     */
    static final String FORCE_SLAVE = "/*FORCE_SLAVE*/ ";

    protected ExtendDAOImpl(Table<R> table, Class<P> type) {
        super(table, type);
    }

    protected ExtendDAOImpl(Table<R> table, Class<P> type, Configuration configuration) {
        super(table, type, configuration);
    }

    @Override
    public DSLContext create() {
        return DSL.using(configuration());
    }

    @Override
    public boolean exists(Condition condition) {
        return fetchCount(condition) > 0;
    }

    @Override
    public P fetchOne(Condition condition) {
        return create().selectFrom(getTable())
                .where(condition)
                .orderBy(getTable().getPrimaryKey().getFields())
                .fetchOne(mapper());
    }

    @Override
    public long fetchCount(Condition condition) {
        return create().selectCount().from(getTable())
                .where(condition)
                .fetchOne(0, Integer.class);
    }

    @Override
    public Optional<P> fetchOneOptional(Condition condition) {
        return Optional.ofNullable(fetchOne(condition));
    }

    @Override
    public List<P> fetch(Condition condition, SortField<?>... sortFields) {
        return create().selectFrom(getTable())
                .where(condition)
                .orderBy(sortFields)
                .fetch(mapper());
    }

    @Override
    public List<P> fetchById(Collection<T> ids) {
        return fetch(equalPrimary(ids));
    }

    private Condition equalPrimary(Collection<T> ids) {
        Condition condition;
        TableField<R, ?>[] pk = getTable().getPrimaryKey().getFieldsArray();
        if (pk.length == 1) {
            if (ids.size() == 1) {
                condition = ((Field<Object>) pk[0]).equal(ids.iterator().next());
            } else {
                condition = pk[0].in(ids);
            }
        } else {
            condition = row(pk).in(ids.toArray(new Record[0]));
        }
        return condition;
    }

    @Override
    public PageResult<P> fetchPage(PageResult<P> page) {
        return fetchPage(page, DSL.noCondition());
    }

    @Override
    public PageResult<P> fetchPage(PageResult<P> page, Condition condition, SortField<?>... sortFields) {
        return fetchPage(page, create().selectFrom(getTable())
                .where(condition)
                .orderBy(sortFields));
    }

    @Override
    public PageResult<P> fetchPage(PageResult<P> page, SelectLimitStep<?> selectLimitStep) {
        return fetchPage(page, selectLimitStep, r -> r.into(getType()));
    }

    @Override
    public <O> PageResult<O> fetchPage(PageResult<O> page, SelectLimitStep<?> selectLimitStep,
                                       RecordMapper<? super Record, O> mapper) {
        PageResult<O> result = page.into(new PageResult<>(true));
        DSLContext dslContext = create();
        if (Boolean.TRUE.equals(page.getQueryAll())) {
            List<O> resultList = dslContext.fetch(selectLimitStep).map(mapper);
            result.setData(resultList);
            result.setTotal((long) resultList.size());
            return result;
        }
        int size = page.getPageSize();
        int start = (page.getCurrentPage() - 1) * size;
        // 在页数为零的情况下 小优化，不查询数据库直接返回数据为空集合的分页包装类
        if (size == 0) {
            return new PageResult<>(Collections.emptyList(), start, 0, 0);
        }

        List<O> resultList;
        long total;
        if (Boolean.TRUE.equals(page.getNotFetchCount())) {
            resultList = dslContext.fetch(selectLimitStep.limit(start, size)).map(mapper);
            total = start + resultList.size();
            if (resultList.size() >= size) {
                total++;
            }
        } else {
            total = dslContext.fetchCount(selectLimitStep);
            resultList = dslContext.fetch(selectLimitStep.limit(start, size)).map(mapper);
        }
        result.setData(resultList);
        result.setTotal(total);
        return result;
    }

    @Override
    public <O> PageResult<O> fetchPage(PageResult<O> page, SelectLimitStep<?> selectLimitStep, Class<O> pojoType) {
        return fetchPage(page, selectLimitStep, r -> r.into(pojoType));
    }

    @Override
    public void updateSelective(P object) {
        this.updateSelective(Collections.singletonList(object));
    }

    @Override
    public void updateSelective(P object, Field<?>... includeFields) {
        this.updateSelective(Collections.singleton(object), includeFields);
    }

    @Override
    public void updateSelective(P[] objects) {
        this.updateSelective(Arrays.asList(objects));
    }

    @Override
    public void updateSelective(Collection<P> objects, Field<?>... includeFields) {
        if (objects.size() > 1) {
            create().batchUpdate(recordsSelective(objects, true, Arrays.asList(includeFields))).execute();
        } else if (objects.size() == 1) {
            recordsSelective(objects, true, Arrays.asList(includeFields)).get(0).update();
        }
    }

    @Override
    public void update(Collection<P> objects) {
        updateSelective(objects);
    }

    @Override
    public void insertSelective(P object) {
        if (object == null) {
            return;
        }
        R record = recordsSelective(Collections.singletonList(object), false).get(0);
        record.insert();
        record.into(object);
    }

    /**
     * 重写 DAOImpl.insert 方法的原因是因为
     * 在默认配置下父级的方法不会进行批量插入操作，而是便利每个对象
     * 进行Insert操作，会产生N条SQL语句，影响性能
     *
     * @param objects
     */
    @Override
    public void insert(Collection<P> objects) {
        if (objects.size() == 1) {
            insertSelective(objects.iterator().next());
        } else {
            insertSelective(objects);
        }
    }

    @Override
    public void insertSelective(P[] objects) {
        insertSelective(Arrays.asList(objects));
    }

    @Override
    public void insertSelective(Collection<P> objects) {
        if (objects.size() > 0) {
            create().batchInsert(recordsSelective(objects, false)).execute();
        }
    }

    @Override
    public int replaceInto(P object) {
        if (object == null) {
            return 0;
        }
        return this.replaceInto(Collections.singletonList(object));
    }

    @Override
    public int replaceInto(Collection<P> object) {
        if (object == null || object.isEmpty()) {
            return 0;
        }
        Map<List<Field<?>>, List<R>> recordMap = createRecordMap(object);
        return recordMap.keySet().stream()
                .mapToInt(fields -> {
                    List<QueryPart> valueParams = getFiledAllValues(recordMap, fields).stream()
                            .map(values -> DSL.list(values.stream().map(DSL::val).collect(toList())))
                            .collect(toList());

                    List<Object> finalList = new ArrayList<>(Arrays.asList(getTable(), DSL.list(fields)));
                    finalList.addAll(valueParams);

                    int beginIndex = 2;
                    String joinStr = "({%s}),";
                    StringBuilder joinSql = new StringBuilder();
                    for (int i = 0; i < valueParams.size(); i++) {
                        joinSql.append(String.format(joinStr, i + beginIndex));
                    }
                    String finalString = joinSql.substring(0, joinSql.length() - 1);
                    return create().execute("replace into {0} ({1}) values " + finalString,
                            finalList.toArray()
                    );
                }).sum();
    }

    @Override
    public int insertOnDuplicateKeyIgnore(P object) {
        return insertOnDuplicateKeyIgnore(Collections.singletonList(object));
    }

    @Override
    public int insertOnDuplicateKeyIgnore(Collection<P> objects) {
        if (objects.size() > 0) {
            Map<List<Field<?>>, List<R>> recordMap = createRecordMap(objects);
            return recordMap.keySet().stream()
                    .mapToInt(fields -> {
                        List<? extends List<?>> allValues = getFiledAllValues(recordMap, fields);
                        InsertValuesStepN<R> insertStep = create().insertInto(getTable())
                                .columns(fields);
                        allValues.forEach(insertStep::values);
                        return insertStep
                                .onDuplicateKeyIgnore()
                                .execute();
                    }).sum();
        }
        return 0;
    }

    private List<? extends List<?>> getFiledAllValues(Map<List<Field<?>>, List<R>> recordMap, List<Field<?>> fields) {
        List<R> recordValues = recordMap.get(fields);
        List<? extends List<?>> allValues = recordValues.stream().map(r -> fields.stream().map(r::get)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
        return allValues;
    }

    private Map<List<Field<?>>, List<R>> createRecordMap(Collection<P> objects) {
        List<R> records = recordsSelective(objects, false);
        return records.stream().collect(Collectors.groupingBy(r ->
                Arrays.stream(r.fields()).filter(f -> f.changed(r))
                        .collect(Collectors.toList())));
    }

    private List<R> recordsSelective(Collection<P> objects, boolean forUpdate) {
        return this.recordsSelective(objects, forUpdate, null);
    }

    private List<R> recordsSelective(Collection<P> objects, boolean forUpdate, Collection<Field<?>> includeFields) {
        List<R> result = new ArrayList<>(objects.size());
        Field<?>[] pk = getTable().getPrimaryKey().getFieldsArray();
        DSLContext ctx = create();

        Set<Field<?>> includeFieldSet = includeFields == null ? Collections.emptySet() : new HashSet<>(includeFields);

        for (P object : objects) {
            R record = ctx.newRecord(getTable(), object);

            if (forUpdate && pk != null) {
                for (Field<?> field : pk) {
                    record.changed(field, false);
                }
            }

            for (int i = 0; i < record.size(); i++) {
                Object data = record.get(i);
                record.changed(i, data != null || includeFieldSet.contains(record.field(i)));
            }
            result.add(record);
        }

        return result;
    }

    @Override
    public void delete(Condition condition) {
        create().delete(getTable())
                .where(condition)
                .execute();
    }
}
