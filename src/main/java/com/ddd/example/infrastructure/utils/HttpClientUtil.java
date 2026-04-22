package com.ddd.example.infrastructure.utils;

import com.ddd.example.infrastructure.utils.httpdto.JsonStringHttpResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.Buffer;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 使用了configuration的方式注入了配置，并实例化了bean
 */
@Slf4j
public class HttpClientUtil {
    // 当前工具类 bean实例
    private static volatile HttpClientUtil INSTANCE;
    // 请求追踪 ID header
    private static final String REQUEST_TRACE_ID = "x-request-id";
    // OkHttpClient 容器 （ key: 超时时间 ms  value: OkHttpClient）
    private static final Map<Long, OkHttpClient> clientMap = new ConcurrentHashMap<>();

    // 全局的超时时间，链接池设置
    private final Long timeoutMillis;
    private final Integer maxIdleConnections;
    private final Integer keepAliveDuration;

    public HttpClientUtil(Long timeoutMillis, Integer maxIdleConnections, Integer keepAliveDuration) {
        this.timeoutMillis = timeoutMillis;
        this.maxIdleConnections = maxIdleConnections;
        this.keepAliveDuration = keepAliveDuration;
    }

    /**
     * 获取当前bean 实例, 可以使其像正常的静态类使用
     * 注：因为自定义埋点是基于AOP实现的，所以不能直接使用静态工具类来实现，所以使用该方法模拟正常的静态工具类
     * 其实调用方式使用依赖注入也可以
     *
     * @return Spring容器中的 HttpClientUtil bean实例
     */
    public static HttpClientUtil getInstance() {
        if (INSTANCE == null) {
            synchronized (HttpClientUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = SpringUtils.getBean(HttpClientUtil.class);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 请求带有auth认证的http请求
     *
     * @param url
     * @param paramsMap
     * @param userName
     * @param password
     * @return
     */
    public JsonStringHttpResponse doGetJsonRequestWithBasicAuth(String url, Map<String, String> paramsMap, String userName, String password) {
        // 构建 URL 和请求参数
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
        Optional.ofNullable(paramsMap).orElse(new HashMap<>()).forEach(urlBuilder::addQueryParameter);
        HttpUrl finalUrl = urlBuilder.build();

        // 创建 GET 请求
        Request.Builder builder = new Request.Builder();
        if (StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(password)) {
            builder.addHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((userName + ":" + password).getBytes()));
        }
        if (StringUtils.isNotBlank(TraceIdUtil.getTraceId())) {
            builder.addHeader(REQUEST_TRACE_ID, TraceIdUtil.getTraceId());
        }

        Request request = builder.url(finalUrl).build();
        return executeRequest(request, timeoutMillis);
    }

    /**
     * 发送 GET 请求, 使用自定义超时时间
     *
     * @param url       请求地址
     * @param paramsMap 请求参数
     * @return JsonStringHttpResponse
     */
    public JsonStringHttpResponse doGetJsonRequest(@NotBlank String url, Map<String, String> paramsMap, Map<String, String> headerMap) {
        return doGetJsonRequest(url, paramsMap, headerMap, timeoutMillis);
    }

    /**
     * 添加带有header的请求方法
     *
     * @param url
     * @param paramsMap
     * @param headerMap
     * @return
     */
    public JsonStringHttpResponse doGetJsonRequest(@NotBlank String url, Map<String, String> paramsMap, Map<String, String> headerMap, long timeoutMillis) {
        // 构建 URL 和请求参数
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
        Optional.ofNullable(paramsMap).orElse(new HashMap<>()).forEach(urlBuilder::addQueryParameter);
        HttpUrl finalUrl = urlBuilder.build();

        // 创建 GET 请求
        Request.Builder builder = new Request.Builder();
        Optional.ofNullable(headerMap).orElse(new HashMap<>()).forEach(builder::addHeader);
        if (StringUtils.isNotBlank(TraceIdUtil.getTraceId())) {
            builder.addHeader(REQUEST_TRACE_ID, TraceIdUtil.getTraceId());
        }
        Request request = builder.url(finalUrl).build();
        return executeRequest(request, timeoutMillis);
    }

    /**
     * 发送 GET 请求, 使用自定义超时时间
     *
     * @param url      请求地址
     * @param data     请求的拼接数据
     * @param userName 请求认证的name
     * @param password 请求认证的password
     * @return boolean
     */
    public boolean post204RequestWithAuth(@NotBlank String url, String data, String userName, String password) {
        // 创建请求体 (使用 text/plain 格式)
        RequestBody body = RequestBody.create(data, MediaType.parse("text/plain"));

        Request.Builder builder = new Request.Builder();
        if (StringUtils.isNotBlank(TraceIdUtil.getTraceId())) {
            builder.header(REQUEST_TRACE_ID, TraceIdUtil.getTraceId());
        }
        // 创建 post 请求
        Request request = builder.url(url)
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((userName + ":" + password).getBytes()))
                .post(body)
                .build();
        // 执行请求并处理响应, 使用全局超时时间
        OkHttpClient client = getOkHttpClientByTimeoutConfig(timeoutMillis);
        try (Response response = client.newCall(request).execute()) {
            // 返回2xx的消息，发送成功，兼容以后的200和204状态码
            return Optional.of(response).map(r -> 2 == r.code() / 100).orElse(false);
        } catch (IOException e) {
            log.error("HTTP request failed: traceId={}, requestUrl={}, error=", TraceIdUtil.getTraceId(), request.url(), e);
        }
        return false;
    }

    /**
     * 发送 POST 请求, 使用默认超时时间
     *
     * @param url        请求地址
     * @param jsonString 请求参数
     * @return JsonStringHttpResponse
     */
    public JsonStringHttpResponse sendPostRequest(String url, String jsonString) {
        return sendPostRequest(url, jsonString, timeoutMillis);
    }


    public JsonStringHttpResponse sendPostRequest(String url, String jsonString, long timeoutMillis) {
        return sendPostRequest(url, jsonString, null, timeoutMillis);
    }

    /**
     * 发送 POST 请求, 使用自定义超时时间
     *
     * @param url           请求地址
     * @param jsonString    请求参数
     * @param headersMap    请求头
     * @param timeoutMillis 超时时间 单位: 毫秒
     * @return JsonStringHttpResponse
     */
    public JsonStringHttpResponse sendPostRequest(String url, String jsonString, Map<String, String> headersMap, long timeoutMillis) {
        // 创建请求体
        RequestBody body = RequestBody.create(jsonString, MediaType.parse("application/json; charset=utf-8"));
        // 创建 POST 请求
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .header(REQUEST_TRACE_ID, Optional.ofNullable(TraceIdUtil.getTraceId()).orElse(TraceIdUtil.generateTraceId()))  // 请求追踪 ID
                .post(body);
        // 设置请求头
        if (headersMap != null && !headersMap.isEmpty()) {
            requestBuilder.headers(Headers.of(headersMap));
        }

        return executeRequest(requestBuilder.build(), timeoutMillis);
    }

    /**
     * 执行请求并返回响应
     *
     * @param request 请求
     * @return JsonStringHttpResponse
     */
    private JsonStringHttpResponse executeRequest(Request request, long timeoutMillis) {
        JsonStringHttpResponse jsonResponse = null;
        long start = System.currentTimeMillis();
        OkHttpClient okHttpClient = getOkHttpClientByTimeoutConfig(timeoutMillis);
        String requestBodycontent = null;
        // 执行请求并处理响应
        try (Response response = okHttpClient.newCall(request).execute()) {
            jsonResponse = handleResponse(response);

            if (request.body() != null) {
                Buffer buffer = new Buffer();
                request.body().writeTo(buffer);
                requestBodycontent = buffer.readUtf8();
            }
            long executionTime = System.currentTimeMillis() - start;
            // 使用gson转换一下requestBodycontent, 确保日志输出在同一行,response放在前面的目的是因为request如果是图片base64可能是截取
            log.info("sendRequest traceId {}, url {},Execution time {}, response {},request {}", TraceIdUtil.getTraceId(), request.url(), executionTime, jsonResponse.getBody(), requestBodycontent);

        } catch (IOException e) {
            log.error("HTTP request failed: traceId={}, requestUrl={}, request {}, error=", TraceIdUtil.getTraceId(), request.url(), requestBodycontent, e);
        }

        return jsonResponse;
    }

    /**
     * 通过超市时间获取指定超时时间的OkHttpClient
     *
     * @param timeoutMillis
     * @return
     */
    private @NotNull OkHttpClient getOkHttpClientByTimeoutConfig(long timeoutMillis) {
        // 根据不同的超时时间 获取相对应的OkHttpClient，如果不存在则创建，并添加到容器中
        return Optional.ofNullable(clientMap.get(timeoutMillis)).orElseGet(() -> {
            OkHttpClient newClient = buildOkHttpClient(timeoutMillis);
            clientMap.put(timeoutMillis, newClient);
            return newClient;
        });
    }


    private JsonStringHttpResponse handleResponse(Response response) throws IOException {
        JsonStringHttpResponse jsonResponse = new JsonStringHttpResponse();
        if (response.body() != null) {
            jsonResponse.setBody(response.body().string());
        }

        // 设置响应头
        Map<String, String> responseHeaders = new HashMap<>();
        for (String headerName : response.headers().names()) {
            responseHeaders.put(headerName, response.header(headerName));
        }
        jsonResponse.setHeaders(responseHeaders);

        // 设置Http状态码
        jsonResponse.setStatusCode(response.code());

        return jsonResponse;
    }

    /**
     * 构建OkHttpClient
     *
     * @param timeoutMillis 超时时间
     * @return okhttpClient
     */
    private OkHttpClient buildOkHttpClient(long timeoutMillis) {
        return new OkHttpClient.Builder()
                .connectTimeout(timeoutMillis, TimeUnit.MILLISECONDS)   // 连接超时
                .readTimeout(timeoutMillis, TimeUnit.MILLISECONDS)      // 读取超时
                .writeTimeout(timeoutMillis, TimeUnit.MILLISECONDS)     // 写入超时
                .retryOnConnectionFailure(false)        // 自动重试
                .connectionPool(new ConnectionPool(maxIdleConnections, keepAliveDuration, TimeUnit.MINUTES)) // 连接池配置
                .build();
    }
}
