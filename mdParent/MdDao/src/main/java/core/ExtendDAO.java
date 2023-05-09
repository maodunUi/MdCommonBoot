package core;

import entity.PageResult;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 扩展DAO，扩展之前DAO的各类方法
 *
 * @param <R> 表对象
 * @param <P> POJO类型
 * @param <T> 主键类型
 * @author Diamond
 */
public interface ExtendDAO<R extends UpdatableRecord<R>, P, T> extends DAO<R, P, T> {

    /**
     * 获取 DSLContext
     *
     * @return DSLContext
     */
    DSLContext create();

    /**
     * 条件查询单条记录
     *
     * @param condition 约束条件
     * @return <p>
     */
    P fetchOne(Condition condition);

    /**
     * 根据条件查询总数
     * @param condition 条件
     * @return 总数
     */
    long fetchCount(Condition condition);

    /**
     * 根据条件查询是否存在
     * @param condition 条件
     * @return 是否存在
     */
    boolean exists(Condition condition);

    /**
     * 条件查询单条记录
     *
     * @param condition 约束条件
     * @return Optional<P>
     */
    Optional<P> fetchOneOptional(Condition condition);

    /**
     * 条件查询多条，并排序
     *
     * @param condition  约束条件
     * @param sortFields 排序字段
     * @return POJO 集合
     */
    List<P> fetch(Condition condition, SortField<?>... sortFields);

    /**
     * 通过指定的 主键集合 查找数据
     *
     * @param ids 主键集合
     * @return POJO 集合
     */
    List<P> fetchById(Collection<T> ids);

    /**
     * 读取分页数据
     *
     * @param page 分页参数
     * @return 分页结果集
     */
    PageResult<P> fetchPage(PageResult<P> page);

    /**
     * 读取分页数据
     *
     * @param page       分页参数
     * @param condition  约束条件
     * @param sortFields 排序字段
     * @return 分页结果集
     */
    PageResult<P> fetchPage(PageResult<P> page, Condition condition, SortField<?>... sortFields);

    /**
     * 读取分页数据
     *
     * @param page            分页参数
     * @param selectLimitStep 查询语句
     * @return 分页结果集
     */
    PageResult<P> fetchPage(PageResult<P> page, SelectLimitStep<?> selectLimitStep);

    /**
     * 任意类型读取分页数据
     *
     * @param page            分页参数
     * @param selectLimitStep 查询语句
     * @param mapper          结果映射方式
     * @param <O>             返回类型的泛型
     * @return 分页结果集
     */
    <O> PageResult<O> fetchPage(PageResult<O> page, SelectLimitStep<?> selectLimitStep,
                                RecordMapper<? super Record, O> mapper);

    /**
     * 任意类型读取分页数据
     *
     * @param page            分页参数
     * @param selectLimitStep 查询语句
     * @param pojoType        POJO类型
     * @param <O>             返回类型的泛型
     * @return 分页结果集
     */
    <O> PageResult<O> fetchPage(PageResult<O> page, SelectLimitStep<?> selectLimitStep, Class<O> pojoType);

    /**
     * 对指定的 POJO 执行 UPDATE 操作， 只更新非空的部分
     *
     * @param object 需要更新的 POJO
     */
    void updateSelective(P object);

    /**
     * 默认只更新非空的部分，includeFields 指定的字段就算为空也会被更新
     *
     * @param object        需要更新的 POJO
     * @param includeFields 指定字段必须被更新，不论是否为空
     */
    void updateSelective(P object, Field<?>... includeFields);

    /**
     * 对指定的 POJOs 执行 UPDATE 操作， 只更新非空的部分
     *
     * @param objects 需要更新的 POJOs
     */
    void updateSelective(P[] objects);

    /**
     * 对指定的 POJOs 执行 UPDATE 操作，includeFields 指定的字段就算为空也会被更新
     *
     * @param objects       需要更新的 POJOs
     * @param includeFields 包含字段必须被更新，不论是否为空
     */
    void updateSelective(Collection<P> objects, Field<?>... includeFields);

    /**
     * 对传入的 POJO 执行 INSERT 操作，只会插入非空的值
     * 如果有自增主键,传入的 POJO 主键会被赋值
     *
     * @param object 参数
     */
    void insertSelective(P object);

    /**
     * 对传入的 POJO 执行 INSERT 操作， 只会插入非空的值
     *
     * @param objects 参数
     */
    void insertSelective(P[] objects);

    /**
     * 对传入的 POJO 执行 INSERT 操作， 只会插入非空的值
     *
     * @param objects 参数
     */
    void insertSelective(Collection<P> objects);

    /**
     * 替换插入，当唯一索引或者主键重复时，替换
     * @param object 对象
     * @return 行数
     */
    int replaceInto(P object);

    /**
     * 替换插入，当唯一索引或者主键重复时，替换
     * @param object 对象
     * @return 影响行数(replace into的影响行数如果产生replace，会是2，因为有delete和insert操作）
     */
    int replaceInto(Collection<P> object);

    /**
     * 对传入的 object 执行 INSERT操作，当有重复key的时候，忽略
     * @param object 插入对象
     * @return 影响行数(replace into的影响行数如果产生replace，会x2，因为有delete和insert操作）
     */
    int insertOnDuplicateKeyIgnore(P object);

    /**
     * 对传入的 objects 执行 INSERT操作，当有重复key的时候，忽略
     * @param object 插入对象
     * @return 影响行数
     */
    int insertOnDuplicateKeyIgnore(Collection<P> object);

    /**
     * 通过条件删除
     *
     * @param condition 删除条件
     */
    void delete(Condition condition);

    /**
     * 时间格式化
     * @param dateTimeField 时间字段
     * @param format SQLFormat %y-%M-%d
     * @return 格式化字段
     */
    static Field<String> dateFormat(Field<?> dateTimeField, String format) {
        return DSL.field("date_format({0}, {1})", String.class, dateTimeField, DSL.inline(format));
    }
}
