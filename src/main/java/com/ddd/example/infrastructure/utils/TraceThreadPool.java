package com.ddd.example.infrastructure.utils;

import lombok.Getter;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 实现一个基于newFixedThreadPool的自定义线程池
 *
 * @author maqidi
 * @version 1.0
 * @create 2024-07-01 09:57
 */
public class TraceThreadPool extends ThreadPoolExecutor {
    /**
     * 构造线程池
     *
     * @param corePoolSize    线程池大小
     * @param maximumPoolSize 线程池最大数量
     * @param keepAliveTime   线程池空闲时间
     * @param unit            时间单位
     * @param workQueue       任务队列
     * @param threadFactory   线程工厂
     * @param handler         拒绝策略
     */
    public TraceThreadPool(int corePoolSize,
                           int maximumPoolSize,
                           long keepAliveTime,
                           TimeUnit unit,
                           BlockingQueue<Runnable> workQueue,
                           ThreadFactory threadFactory,
                           RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    public void execute(Runnable command) {
        String traceId = TraceIdUtil.getTraceId();
        super.execute(new TraceRunnable(command, traceId));
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        String traceId = TraceIdUtil.getTraceId();
        return super.submit(new TraceCallable<>(task, traceId));
    }

    @Override
    public Future<?> submit(Runnable task) {
        String traceId = TraceIdUtil.getTraceId();
        return super.submit(new TraceRunnable(task, traceId));
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        String traceId = TraceIdUtil.getTraceId();
        return super.submit(new TraceRunnable(task, traceId), result);
    }

    /**
     * 自定义线程工厂，定义名称
     */
    public static class TraceThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        @Getter
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        @Getter
        private final String namePrefix;

        TraceThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "tracePool-" +
                    poolNumber.getAndIncrement() +
                    "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }

    }

    public static TraceThreadPool newCustomThreadPool(int corePoolSize,
                                                      int maximumPoolSize,
                                                      long keepAliveTime,
                                                      int queueCapacity) {
        //默认线程工厂
        ThreadFactory threadFactory = new TraceThreadFactory();
        //拒绝策略, 直接抛出异常，了解线程情况，后面拉机器抵抗问题，比如扩大队列数量
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
        //存活时间
        TimeUnit unit = TimeUnit.MILLISECONDS;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(queueCapacity);

        return new TraceThreadPool(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

}
