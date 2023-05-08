package cn.maodun.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DELL
 * @date 2023/5/8
 */
public class FilterChain implements Filter{

    public List<Filter> mFilters = new ArrayList<>();

    public int index = 0;


    public FilterChain addFilter(Filter filter){
        mFilters.add(filter);
        return this;
    }

    @Override
    public void doFilter(FilterRequest filterRequest, FilterResponse filterResponse, Filter filterChain) {
        if(index == mFilters.size()) {
            return;
        }
        Filter filter = mFilters.get(index);
        index ++;
        filter.doFilter(filterRequest, filterResponse, this);
    }
}
