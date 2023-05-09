package entity;

import java.util.Collections;
import java.util.List;

/**
 * 分页包装类
 *
 * @param <T> 数据类型
 * @author Diamond
 */
public class PageResult<T> {
    /**
     * 总数据条数
     */
    private long total;

    /**
     * 当前页码
     */
    private int currentPage = 1;

    /**
     * 每页数据条数
     */
    private int pageSize = 10;

    private Boolean queryAll;

    /**
     * 不读取总数
     */
    private Boolean notFetchCount;

    /**
     * 具体的数据，默认为空集合(防止NEP)
     */
    private List<T> data = Collections.emptyList();

    public PageResult() {
    }

    public PageResult(boolean queryAll) {
        this.queryAll = queryAll;
    }

    public PageResult(int currentPage, int pageSize) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    public PageResult(List<T> data, int currentPage, int pageSize, long total) {
        this.data = data;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.total = total;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Boolean getQueryAll() {
        return queryAll;
    }

    public void setQueryAll(Boolean queryAll) {
        this.queryAll = queryAll;
    }

    public Boolean getNotFetchCount() {
        return notFetchCount;
    }

    public void setNotFetchCount(Boolean notFetchCount) {
        this.notFetchCount = notFetchCount;
    }

    public void from(PageResult<T> pageResult) {
        setPageSize(pageResult.pageSize);
        setCurrentPage(pageResult.currentPage);
        setTotal(pageResult.total);
        setData(pageResult.data);
        setQueryAll(pageResult.queryAll);
    }

    public PageResult<T> into(PageResult<T> into) {
        into.from(this);
        return into;
    }

    public static <T> PageResult<T> pageQueryAll() {
        return new PageResult<>(true);
    }
}
