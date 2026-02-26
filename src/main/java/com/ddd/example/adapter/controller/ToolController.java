package com.ddd.example.adapter.controller;

import com.ddd.example.infrastructure.utils.TraceThreadPool;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.concurrent.*;

/**
 * 工具类的接口，例如动态配置线程池
 *
 * @author maqidi
 * @version 1.0
 * @create 2024-10-29 18:50
 */
@RestController
@RequestMapping("/tools")
@Validated
public class ToolController {

    @Autowired
    TraceThreadPool traceThreadPool;

    //服务存活的检验方法
    @RequestMapping(value = "/health", method = RequestMethod.GET)
    public String health() {
        return "OK";
    }


    @RequestMapping(value = "/threadPoolInfo/get", method = RequestMethod.GET)
    public ResponseVO<ThreadPoolInfo> getThreadPoolInfo() {
        ThreadPoolInfo threadPoolInfo = new ThreadPoolInfo();
        threadPoolInfo.setCorePoolSize(traceThreadPool.getCorePoolSize());
        threadPoolInfo.setMaximumPoolSize(traceThreadPool.getMaximumPoolSize());
        threadPoolInfo.setKeepAliveTime(traceThreadPool.getKeepAliveTime(TimeUnit.MILLISECONDS));
        threadPoolInfo.setQueueCapacity(traceThreadPool.getQueue().size());
        TraceThreadPool.TraceThreadFactory threadFactory = (TraceThreadPool.TraceThreadFactory) traceThreadPool.getThreadFactory();
        threadPoolInfo.setThreadNamePrefix(threadFactory.getNamePrefix());
        return ResponseVO.successResponse(threadPoolInfo);
    }

    @RequestMapping(value = "/threadPoolInfo/update", method = RequestMethod.POST)
    public ResponseVO<String> updateThreadPool(@Valid @RequestBody ThreadPoolInfoCmd threadPoolInfoCmd) {
        //销毁原来的线程池，重新创建
        traceThreadPool.setCorePoolSize(threadPoolInfoCmd.getCorePoolSize());
        traceThreadPool.setMaximumPoolSize(threadPoolInfoCmd.getMaximumPoolSize());
        traceThreadPool.setKeepAliveTime(threadPoolInfoCmd.getKeepAliveTime(), TimeUnit.MILLISECONDS);
        //TODO 因为java.util.concurrent.ThreadPoolExecutor，不能修改队列长度，而springboot的org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor也不能
        //只能通过销毁再创建，不过可以看看别的实现重写代码的方式实现
        return ResponseVO.successResponse("修改成功");
    }


    /**
     * 数据库的配置
     */
    @Data
    public static class ThreadPoolInfo {
        /**
         * 核心线程数
         */
        private int corePoolSize;
        /**
         * 最大线程数
         */
        private int maximumPoolSize;
        /**
         * 线程存活时间,毫秒
         */
        private long keepAliveTime;
        /**
         * 队列容量
         */
        private int queueCapacity;
        /**
         * 线程的名字
         */
        private String threadNamePrefix;
    }

    @Data
    public static class ThreadPoolInfoCmd {
        /**
         * 核心线程数
         */
        @Min(value = 1, message = "核心线程数不能小于1")
        private int corePoolSize;
        /**
         * 最大线程数
         */
        @Min(value = 1, message = "最大线程数不能小于1")
        private int maximumPoolSize;
        /**
         * 线程存活时间,毫秒
         */
        @Min(value = 1, message = "线程存活时间不能小于1")
        private long keepAliveTime;
        /**
         * 队列容量
         */
        @Min(value = 100, message = "队列容量不能小于100")
        private int queueCapacity;
    }


}
