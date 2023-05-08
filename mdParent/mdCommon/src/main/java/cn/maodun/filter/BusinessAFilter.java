package cn.maodun.filter;

import lombok.extern.slf4j.Slf4j;

/**
 * @author DELL
 * @date 2023/5/8
 */
@Slf4j
public class BusinessAFilter implements Filter{
    @Override
    public void doFilter(FilterRequest filterRequest, FilterResponse filterResponse, Filter filterChain) {
      log.info("do business A ");
        filterChain.doFilter(filterRequest, filterResponse, filterChain);
    }
}
