package cn.maodun.service;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * @author DELL
 * @date 2023/3/29
 */
public class EsService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    public  CreateIndexResponse createIndex(String index) throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(index);
        return restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
    }
}
