package com.ddd.example.infrastructure.config;

import com.ddd.example.infrastructure.utils.TraceIdUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * traceId的过滤器，用于servlet中，使用过滤器设置traceID，
 * 1. 最后放入到threadlocal中，作为traceID传递
 * 2. 在servlet中，获取traceID，并设置到header中
 *
 * @author maqidi
 * @version 1.0
 * @create 2024-07-01 09:42
 */
@Component
public class TraceIdFilter implements Filter {

    //这个是一个traceId在header中的名字
    public static final String TRACE_ID_NAME = "X-Trace-Id";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No initialization required
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String traceId = httpRequest.getHeader("X-Trace-Id");
        if (StringUtils.isEmpty(traceId)) {
            traceId = TraceIdUtil.generateTraceId();
        }
        TraceIdUtil.setTraceId(traceId);
        httpResponse.setHeader(TRACE_ID_NAME, traceId);

        try {
            chain.doFilter(request, response);
        } finally {
            TraceIdUtil.clear();
        }
    }

    @Override
    public void destroy() {
        // No cleanup required
    }
}
