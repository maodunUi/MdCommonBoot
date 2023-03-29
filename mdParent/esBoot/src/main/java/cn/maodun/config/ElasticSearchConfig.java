package cn.maodun.config;

import cn.maodun.service.EsService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * spring-boot-autoconfigure自动配置es
 * ElasticsearchRestClientAutoConfiguration类根据你导入了什么jar包使用es客户端
 * 我们这里引入了 下面的RestHighLevelClient
 * <dependency>
 *             <groupId>org.elasticsearch.client</groupId>
 *             <artifactId>elasticsearch-rest-high-level-client</artifactId>
 *             <version>7.8.0</version>
 * </dependency>
 * 但是我们配置了@EnableAutoConfiguration(exclude = {ElasticsearchRestClientAutoConfiguration.class})
 * 不使用springboot帮我们来实例化RestHighLevelClient
 * 我们可以按照自己的配置类实例化RestHighLevelClient。
 */
@Configuration
@ConfigurationProperties(prefix = "es")
@Getter
@Setter
public class ElasticSearchConfig {

    private String host;

    private Integer port;

    private String xpackSecurityUser;

    private String nodeName;

    private String username;

    private String password;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(RestClient.builder(new HttpHost(getHost(), getPort(), "http")));
    }

    @Bean
    @ConditionalOnBean(RestHighLevelClient.class)
    public EsService esService(){
        return new EsService();
    }
}
