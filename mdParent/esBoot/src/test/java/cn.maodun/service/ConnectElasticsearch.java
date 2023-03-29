package cn.maodun.service;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * @author DELL
 * @date 2022/9/14
 */
public class ConnectElasticsearch {
    public static void connect(ElasticSearchTask task) {
        RestHighLevelClient esClient = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
        try {
            task.doSomething(esClient);
            esClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
