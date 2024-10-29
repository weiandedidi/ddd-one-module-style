package com.ddd.example.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * 创建ES的配置信息，使用的使用开启注释
 *
 * @author maqidi
 * @version 1.0
 * @create 2024-07-26 14:48
 */
//@Configuration
@Slf4j
public class ElasticsearchConfig {


    //    @Value("${elasticsearch.host}")
    private String hosts;

    //    @Value("${elasticsearch.port}")
    private int port;

    //    @Value("${elasticsearch.username}")
    private String username;

    //    @Value("${elasticsearch.password}")
    private String password;

    //    @Value("${elasticsearch.max-connect-num-per-route}")
    private int maxRoutes;
    //    @Value("${elasticsearch.max-connect-num}")
    private int maxConnectNum;


    //    @Bean
    public RestHighLevelClient restHighLevelClient() {
        final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));

        //目前只配置1个es集群
        HttpHost[] httpHosts = Arrays.stream(hosts.split(",")).map(host -> new HttpHost(host, port, "http")).toArray(HttpHost[]::new);
        RestClientBuilder builder = RestClient.builder(httpHosts)
                .setHttpClientConfigCallback(httpClientBuilder -> {
                    httpClientBuilder.disableAuthCaching()
                            .setDefaultCredentialsProvider(credentialsProvider)
                            .setMaxConnTotal(maxConnectNum) // 总的最大连接数
                            .setMaxConnPerRoute(maxRoutes) // 每路由的最大连接数
                            .setDefaultIOReactorConfig(IOReactorConfig.custom().setIoThreadCount(Runtime.getRuntime().availableProcessors() * 2).build());
                    return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                });
        return new RestHighLevelClient(builder);
    }
}
