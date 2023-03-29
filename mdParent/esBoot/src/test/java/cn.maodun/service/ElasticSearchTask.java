package cn.maodun.service;

import org.elasticsearch.client.RestHighLevelClient;

/**
 * @author DELL
 * @date 2022/9/14
 */
public interface ElasticSearchTask {
    void doSomething(RestHighLevelClient client) throws Exception;
}
