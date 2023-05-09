package service;


import core.ExtendDAO;
import entity.PageResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 抽象通用Service
 *
 * @param <P>
 * @param <T>
 * @author Diamond
 */
public abstract class GenericServiceImpl<P, T> implements GenericService<P, T> {
    protected ExtendDAO<?, P, T> dao;

    public GenericServiceImpl(ExtendDAO<?, P, T> dao) {
        this.dao = dao;
    }

    @Override
    public P get(T id) {
        return dao.findById(id);
    }

    @Override
    public Optional<P> getOptional(T id) {
        return Optional.ofNullable(get(id));
    }

    @Override
    public T save(P entity) {
        dao.insertSelective(entity);
        return dao.getId(entity);
    }

    @Override
    public void save(Collection<P> entities) {
        dao.insertSelective(entities);
    }

    @Override
    public void update(P entity) {
        dao.updateSelective(entity);
    }

    @Override
    public void update(Collection<P> entities) {
        dao.updateSelective(entities);
    }

    @Override
    public PageResult<P> page(PageResult<P> page) {
        return dao.fetchPage(page);
    }

    @Override
    public void delete(T id) {
        dao.deleteById(id);
    }

    @Override
    public void delete(Collection<T> ids) {
        dao.deleteById(ids);
    }

    @Override
    public List<P> listAll() {
        return dao.findAll();
    }

    @Override
    public List<P> listByIds(Collection<T> ids) {
        return dao.fetchById(ids);
    }

    @Override
    public T saveOrUpdate(P model) {
        T id = dao.getId(model);
        if (id == null) {
            return save(model);
        } else {
            update(model);
        }
        return id;
    }

    @Override
    public void saveOrUpdate(Collection<P> models) {
        List<P> insertList = new ArrayList<>();
        List<P> updateList = new ArrayList<>();
        models.forEach(item -> {
            Object id = dao.getId(item);
            if (id == null) {
                insertList.add(item);
            } else {
                updateList.add(item);
            }
        });
        if (insertList.size() > 0) {
            dao.insertSelective(insertList);
        }
        if (updateList.size() > 0) {
            dao.updateSelective(updateList);
        }
    }
}
