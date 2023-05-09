package service;


import entity.PageResult;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 通用 Service 接口
 *
 * @param <P> POJO类型
 * @param <T> 主键泛型
 * @author Diamond
 */
public interface GenericService<P, T> {

    /**
     * 获取当前时间戳
     *
     * @return 时间戳对象
     */
    static Timestamp currentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 通过主键获取数据
     *
     * @param id 主键
     * @return 实体对象
     */
    P get(T id);

    /**
     * 通过主键获取数据
     *
     * @param id 主键
     * @return 实体对象
     */
    Optional<P> getOptional(T id);

    /**
     * 插入数据（字段值为空忽略）, 如果主键是自动生成的，会返回
     *
     * @param entity 实体对象
     * @return 影响行数
     */
    T save(P entity);

    /**
     * 批量插入数据（字段值为空忽略）
     *
     * @param entities 实体对象集合
     */
    void save(Collection<P> entities);

    /**
     * 根据主键更新数据（默认不更新参数为空的字段）
     *
     * @param entity 实体对象
     */
    void update(P entity);

    /**
     * 根据主键批量更新
     *
     * @param entities 实体对象集合
     */
    void update(Collection<P> entities);

    /**
     * 获取分页数据
     *
     * @param page 分页对象
     * @return 分页数据包装
     */
    PageResult<P> page(PageResult<P> page);

    /**
     * 根据主键删除数据
     *
     * @param id 主键
     */
    void delete(T id);

    /**
     * 根据主键批量删除数据
     *
     * @param ids 主键集合
     */
    void delete(Collection<T> ids);

    /**
     * 获取全部数据
     *
     * @return 实体集合
     */
    List<P> listAll();

    /**
     * 通过主键获取批量数据
     *
     * @param ids 主键集合
     * @return 实体集合
     */
    List<P> listByIds(Collection<T> ids);

    /**
     * 保存或者更新，会根据model的ID是否为空进行判断
     *
     * @param model model对象
     */
    T saveOrUpdate(P model);

    /**
     * 批量保存或者更新（根据主键）
     *
     * @param model 数据集合
     */
    void saveOrUpdate(Collection<P> model);
}
