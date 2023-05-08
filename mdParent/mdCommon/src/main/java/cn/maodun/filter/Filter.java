package cn.maodun.filter;

/**
 * @author DELL
 * @date 2023/5/8
 */
public interface Filter {
    void doFilter(FilterRequest filterRequest,FilterResponse filterResponse,Filter filterChain);
}
