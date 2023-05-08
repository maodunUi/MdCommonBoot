package cn.maodun.filter;

import lombok.extern.slf4j.Slf4j;

/**
 * @author DELL
 * @date 2023/5/8
 */
@Slf4j
public class BusinessBFilter implements Filter{
    @Override
    public void doFilter(FilterRequest filterRequest, FilterResponse filterResponse, Filter filterChain) {
        log.info("do business B ");
        filterChain.doFilter(filterRequest, filterResponse, filterChain);
    }
}
