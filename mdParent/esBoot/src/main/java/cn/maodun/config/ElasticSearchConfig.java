package cn.maodun.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
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
@EnableAutoConfiguration(exclude = {ElasticsearchRestClientAutoConfiguration.class})
@Slf4j
public class ElasticSearchConfig {

    private String host;

    private Integer port;

    private String xpackSecurityUser;

    private String nodeName;

    private String username;

    private String password;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        String[] usernameAndPassword = xpackSecurityUser.split(":");
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(usernameAndPassword[0], usernameAndPassword[1]));
        return new RestHighLevelClient(RestClient.builder(new HttpHost(getHost(), 9200, "http"))
                .setHttpClientConfigCallback(httpAsyncClientBuilder -> {
                    httpAsyncClientBuilder.disableAuthCaching();
                    return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                }));
    }

}
