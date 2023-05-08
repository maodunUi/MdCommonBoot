package cn.maodun.filter;

/**
 * @author DELL
 * @date 2023/5/8
 */
public class FilterSample {

    public static void main(String[] args) {
        FilterRequest filterRequest = null;
        FilterResponse filterResponse = null;
        FilterChain filterChain = new FilterChain();
        filterChain.addFilter(new BusinessAFilter());
        filterChain.addFilter(new BusinessBFilter());
        filterChain.doFilter(filterRequest,filterResponse,filterChain);
    }
}
