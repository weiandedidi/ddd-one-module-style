package com.ddd.example.infrastructure.utils;

import com.ddd.example.infrastructure.utils.httpdto.JsonStringHttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 提供http的请求工具类
 *
 * @author maqidi
 * @version 1.0
 * @create 2024-07-09 10:15
 */
@Slf4j
public class HttpClientUtil {

    // 设置连接超时时间，单位毫秒。
    private static final int CONNECT_TIMEOUT = 5000;

    // 请求获取数据的超时时间(即响应时间)，单位毫秒。
    private static final int SOCKET_TIMEOUT = 5000;


    /**
     * 默认超时时间的get请求,超时时间5s
     *
     * @param url
     * @param paramsMap
     * @return
     */
    public static JsonStringHttpResponse doGetJsonRequest(String url, Map<String, String> paramsMap) {
        return doGetJsonRequest(url, paramsMap, SOCKET_TIMEOUT);
    }

    public static JsonStringHttpResponse doGetJsonRequest(String url, Map<String, String> paramsMap, int timeout) {
        return doGetJsonRequest(url, paramsMap, timeout, null, null);
    }

    public static JsonStringHttpResponse doGetJsonRequest(String url, Map<String, String> paramsMap, int timeout, String userName, String password) {
        //创建client
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建配置
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout).setSocketTimeout(timeout).build();
        // 创建GET请求
        HttpGet request = null;
        try {
            // 构建参数字符串
            URIBuilder uriBuilder = new URIBuilder(url);
            Optional.ofNullable(paramsMap).orElse(new HashMap<>()).forEach(uriBuilder::addParameter);
            request = new HttpGet(uriBuilder.build());
        } catch (URISyntaxException e) {
            log.error("doGetJsonRequest URISyntaxException traceId {}, e ", TraceIdUtil.getTraceId(), e);
            return null;
        }
        request.setConfig(requestConfig);
        request.setHeader("x-trace-id", TraceIdUtil.getTraceId());
        if (StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(password)) {
            request.setHeader("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString((userName + ":" + password).getBytes()));
        }
        // 执行请求
        JsonStringHttpResponse jsonResponse = new JsonStringHttpResponse();
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(request);
            jsonResponse = handleResponse(response);
            log.info("doGetJsonRequest traceId {}, request {}, response {}", TraceIdUtil.getTraceId(), request, jsonResponse.getBody());
        } catch (IOException e) {
            log.error("doGetJsonRequest error, traceId ={}, e", TraceIdUtil.getTraceId(), e);
        } finally {
            //优雅关闭
            closeClient(response, httpClient);
        }
        return jsonResponse;
    }

    /**
     * 默认超时5秒的请求
     *
     * @param url
     * @param jsonString
     * @return
     */
    public static JsonStringHttpResponse sendPostRequest(String url, String jsonString) {
        return sendPostRequest(url, jsonString, SOCKET_TIMEOUT);
    }

    /**
     * @param url
     * @param jsonString
     * @param timeout    超时时间毫秒
     * @return
     */
    public static JsonStringHttpResponse sendPostRequest(String url, String jsonString, int timeout) {
        //创建client
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建配置
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout).setSocketTimeout(timeout).build();

        // 创建POST请求
        HttpPost request = new HttpPost(url);
        request.setConfig(requestConfig);
        //设置使用utf-8传输
        request.setHeader("Content-Type", "application/json");
        request.setHeader("x-trace-id", TraceIdUtil.getTraceId());
        StringEntity entity = new StringEntity(jsonString, StandardCharsets.UTF_8);
        request.setEntity(entity);

        // 执行请求
        CloseableHttpResponse response = null;
        JsonStringHttpResponse jsonResponse = null;
        try {
            response = httpClient.execute(request);
            jsonResponse = handleResponse(response);
            log.info("sendPostRequest traceId {}, url {}, request {}, response {}", TraceIdUtil.getTraceId(), url, jsonString, jsonResponse.getBody());
        } catch (IOException e) {
            log.error("sendPostRequest error, traceId ={}, e", TraceIdUtil.getTraceId(), e);
        } finally {
            closeClient(response, httpClient);
        }
        return jsonResponse;
    }

    /**
     * //账号
     *
     * @param url
     * @param timeout 超时时间毫秒
     * @return
     */
    public static boolean send204TextRequest(String url, String textString, int timeout, String userName, String password) {
        //创建client
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建配置
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout).setSocketTimeout(timeout).build();

        // 创建POST请求
        HttpPost request = new HttpPost(url);
        request.setConfig(requestConfig);
        //设置使用utf-8传输
        request.setHeader("Content-Type", "text/plain");
        StringEntity entity = new StringEntity(textString, StandardCharsets.UTF_8);
        request.setEntity(entity);
        if (StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(password)) {
            request.setHeader("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString((userName + ":" + password).getBytes()));
        }

        // 执行请求
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(request);
            //非2xx 开头就是异常，204是vm的正常返回
            return Optional.ofNullable(response).map(r -> 2 == r.getStatusLine().getStatusCode() / 100).orElse(false);
        } catch (IOException e) {
            log.error("sendTextRequest error, traceId ={}, e", TraceIdUtil.getTraceId(), e);
        } finally {
            closeClient(response, httpClient);
        }
        return false;
    }

    /**
     * 关闭客户端
     *
     * @param response
     * @param httpClient
     */
    private static void closeClient(CloseableHttpResponse response, CloseableHttpClient httpClient) {
        //关闭
        if (null != response) {
            try {
                response.close();
            } catch (IOException e) {
                log.error("close http response error, traceId {}, e", TraceIdUtil.getTraceId(), e);
            }
        }
        try {
            httpClient.close();
        } catch (IOException e) {
            log.error("close http client error, traceId {}, e", TraceIdUtil.getTraceId(), e);
        }
    }

    private static JsonStringHttpResponse handleResponse(CloseableHttpResponse response) throws IOException {
        JsonStringHttpResponse responseWrapper = new JsonStringHttpResponse();
        // 获取响应实体
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            // 将响应体转换为字符串
            String result = EntityUtils.toString(entity);
            responseWrapper.setBody(result);
        }

        // 获取响应头部信息
        Map<String, String> responseHeaders = new HashMap<>();
        for (org.apache.http.Header header : response.getAllHeaders()) {
            responseHeaders.put(header.getName(), header.getValue());
        }
        responseWrapper.setHeaders(responseHeaders);
        //TODO cookie的获取
        // 确保响应实体完全消费掉
        EntityUtils.consume(entity);
        return responseWrapper;
    }


}
