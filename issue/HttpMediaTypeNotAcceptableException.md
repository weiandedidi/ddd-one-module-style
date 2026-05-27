# higress转了异常

# 1 问题现象

base原请求的返回

```bas
[root@ctyun-open-platform-base-848cb49859-5tfw2 work]# curl -i -k --location 'http://127.0.0.1:8080/coding/v1/messages' \
> --header 'Content-Type: application/json' \
> --header 'Authorization: Bearer cp_5fbb854994964730beca5d147d554xxx' \
> --data '{
>     "model": "6a357f49e5824148bfb5a619afda185e",
>     "messages": [{"role":"user","content":"你是什么模型"}],
>     "max_tokens": 100,
>     "stream": true
> }'
HTTP/1.1 400 
x-request-id: c60fc06b-6e96-426f-bf38-f2eb02f8b3dd
X-Logid: 9518249f9d40c0761f04425bf7aebf11
X-Content-Type-Options: nosniff
X-XSS-Protection: 0
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 26 May 2026 02:46:14 GMT
Connection: close

{"code":610003,"detail":"当前套餐AppKey不存在或已失效","message":"CODING_PLAN_APP_KEY_NOT_EXIST","error":{"code":"610003","message":"当前套餐AppKey不存在或已失效","type":"CODING_PLAN_APP_KEY_NOT_EXIST"}}
```

经过higress的问题

```bash
curl -i -k --location 'https://maas-pre.ctyun.cn:30443/coding/v1/messages' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer cp_5fbb854994964730beca5d147d554xxx' \
--data '{
    "model": "6a357f49e5824148bfb5a619afda185e",
    "messages": [{"role":"user","content":"你是什么模型"}],
    "max_tokens": 100,
    "stream": true
}'
HTTP/2 500
date: Tue, 26 May 2026 02:49:06 GMT
content-length: 0
x-request-id: 3967b081-e941-4e9b-aeea-d50c2f0df130
x-logid: dc2cc98b1b0d759b3b87d9c7abe85bb0
x-content-type-options: nosniff
x-xss-protection: 0
cache-control: no-cache, no-store, max-age=0, must-revalidate
pragma: no-cache
expires: 0
x-frame-options: DENY
req-cost-time: 163
req-arrive-time: 1779763746809
resp-start-time: 1779763746973
x-envoy-upstream-service-time: 132
strict-transport-security: max-age=31536000; includeSubDomains
```

# 2 分析

## 2.1 springboot报错日志

higress服务路由挂mock服务模拟错误码返回，不会吞掉异常。

问题在springboot的sse框架的流式返回，造成

```bash
org.springframework.web.HttpMediaTypeNotAcceptableException: No acceptable representation
```



```bash
[2026-05-26 16:09:21.427] [ERROR] [traceId=d92020284f4b8cdcffe14eb75ba73946] [spanId=4395f6918028e24e] [http-nio-8080-exec-74] com.ctyun.vertx.gateway2.openapi.controller.ApiExceptionResponseHandler:   53 -  - 当前套餐AppKey不存在或已失效
[2026-05-26 16:09:21.428] [WARN ] [traceId=d92020284f4b8cdcffe14eb75ba73946] [spanId=4395f6918028e24e] [http-nio-8080-exec-74] org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver:  480 -  - Failure in @ExceptionHandler com.ctyun.vertx.gateway2.controller.inner.CodingPlanHigressControllerV1#apiException(ApiException)
org.springframework.web.HttpMediaTypeNotAcceptableException: No acceptable representation
	at org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor.writeWithMessageConverters(AbstractMessageConverterMethodProcessor.java:291)
	at org.springframework.web.servlet.mvc.method.annotation.HttpEntityMethodProcessor.handleReturnValue(HttpEntityMethodProcessor.java:263)
	at org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite.handleReturnValue(HandlerMethodReturnValueHandlerComposite.java:78)
	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:136)
	at org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver.doResolveHandlerMethodException(ExceptionHandlerExceptionResolver.java:471)
	at org.springframework.web.servlet.handler.AbstractHandlerMethodExceptionResolver.doResolveException(AbstractHandlerMethodExceptionResolver.java:73)
	at org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver.resolveException(AbstractHandlerExceptionResolver.java:182)
	at org.springframework.web.servlet.handler.HandlerExceptionResolverComposite.resolveException(HandlerExceptionResolverComposite.java:80)
	at org.springframework.web.servlet.DispatcherServlet.processHandlerException(DispatcherServlet.java:1360)
	at org.springframework.web.servlet.DispatcherServlet.processDispatchResult(DispatcherServlet.java:1161)
	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1106)
	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:979)
	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1014)
	at org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java:914)
	at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:590)
	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:885)
	at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:138)

```

## 2.1 pod中请求强制header加Accept: text/event-stream

```bash
curl -i -k -v --location 'http://127.0.0.1:8080/inner/api/coding/v1/chat/completions' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer cp_5fbb854994964730beca5d147d554xxx' \
--header 'SafeToken: 0EylNUz4jhB5v1WH8VvY' \
--header 'Accept: text/event-stream' \
--data '{
    "model": "6a357f49e5824148bfb5a619afda185e",
    "messages": [
        {
            "role": "user",
            "content": "你是什么模型"
        }
    ],
    "stream": true
}'
```

一样报错

## 2.3 报错原因

### 2.3.1 路由服务转发的请求`Accept: */*`

服务路由转发mock服务的header头打印

```bash
2026/05/26 16:47:53 === 所有请求头 (Request Headers) START ===
2026/05/26 16:47:53 [Header] Content-Type: application/json
2026/05/26 16:47:53 [Header] Req-Start-Time: 1779785273413
2026/05/26 16:47:53 [Header] X-Real-Ip: 10.244.0.0
2026/05/26 16:47:53 [Header] X-Forwarded-Port: 443
2026/05/26 16:47:53 [Header] User-Agent: curl/8.4.0
2026/05/26 16:47:53 [Header] Accept: */*
2026/05/26 16:47:53 [Header] X-Error-Code: 610001
2026/05/26 16:47:53 [Header] X-Envoy-Original-Host: wishub-test.ctyun.cn:30443
2026/05/26 16:47:53 [Header] X-Higress-Llm-Model: GLM-5.1
2026/05/26 16:47:53 [Header] X-Api-Key: cp_8bcad483b58840a184a4470f7d2f6d97
2026/05/26 16:47:53 [Header] From_anthropic: higress
2026/05/26 16:47:53 [Header] X-Env:
2026/05/26 16:47:53 [Header] Routing-Strategy: least-request
2026/05/26 16:47:53 [Header] Authorization: Bearer cp_8bcad483b58840a184a4470f7d2f6d97
2026/05/26 16:47:53 [Header] X-Ai-Covert: no
2026/05/26 16:47:53 [Header] X-Envoy-Attempt-Count: 1
2026/05/26 16:47:53 [Header] Traceparent: 00-df4c51321a0f9ba63f01c4503768b81b-26dc8a4f4da989d2-01
2026/05/26 16:47:53 [Header] Tracestate:
2026/05/26 16:47:53 [Header] X-Envoy-External-Address: 10.244.6.22
2026/05/26 16:47:53 [Header] X-Forwarded-Host: wishub-test.ctyun.cn:30443
2026/05/26 16:47:53 [Header] Safetoken: bQAPFpwrw4YCyT7UekfN
2026/05/26 16:47:53 [Header] X-Forwarded-Proto: http
2026/05/26 16:47:53 [Header] X-Scheme: https
2026/05/26 16:47:53 [Header] X-Request-Id: e6ad077c-1f4c-94b4-b2f7-16603618aa3b
2026/05/26 16:47:53 [Header] X-Forwarded-For: 10.244.0.0,10.244.6.22
2026/05/26 16:47:53 [Header] X-Forwarded-Scheme: https
2026/05/26 16:47:53 [Header] X-Envoy-Decorator-Operation: mock-base.maas-xuntui-common-dev.svc.cluster.local:80/coding/*
2026/05/26 16:47:53 === 所有请求头 (Request Headers) END ===
```

### 2.3.1 ai代理转发增加了header项Accept: text/event-stream

**原因：**

higress的ai-proxy的代码位置：plugins/wasm-go/extensions/ai-proxy/provider/provider.go:923

parseRequestAndMapModel方法的`proxywasm.ReplaceHttpRequestHeader("Accept", "text/event-stream")`

==mock服务复现header加Accept: text/event-stream 原因，而springWeb的返回的严格按照客户端的接受方式来，text/event-stream不接受json，所以报错`No acceptable representation`==



Springboot请求如果

header头中有Accept: text/event-stream就会报错

```
--header 'Accept: text/event-stream' \
```

修改方式：

返回结果，手写输出即可

```bash
 /**
     * 统一写出JSON，内部自动获取Response，无NPE，兼容Accept:text/event-stream
     */
    public static void writeJsonResponse(ResponseEntity<?> entity) {
        try {
            // 1. 直接在当前方法内部 获取 RequestAttributes
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                log.error("[SpringHttpWriteUtils] 非Web上下文，无法获取Response");
                return;
            }

            // 2. 直接在当前方法内部 获取 Response
            HttpServletResponse response = attributes.getResponse();
            if (response == null) {
                log.error("[SpringHttpWriteUtils] Response为空");
                return;
            }

            // 3. 输出JSON（强制覆盖Accept头）
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(entity.getStatusCode().value());
            response.getWriter().write(JSONUtils.toJsonString(entity.getBody()));
            response.getWriter().flush();

        } catch (IOException e) {
            log.warn("[SpringHttpWriteUtils] IO异常（客户端断开）", e);
        } catch (Exception e) {
            log.error("[SpringHttpWriteUtils] 输出响应失败", e);
        }
    }
```

