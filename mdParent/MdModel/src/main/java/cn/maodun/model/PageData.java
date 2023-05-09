package cn.maodun.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageData<T> {
    /**
     * 总数据条数
     */
    private long total;

    /**
     * 当前页码
     */
    private int currentPage;

    /**
     * 每页数据条数
     */
    private int pageSize;

    /**
     * 数据
     */
    private List<T> data;
}
