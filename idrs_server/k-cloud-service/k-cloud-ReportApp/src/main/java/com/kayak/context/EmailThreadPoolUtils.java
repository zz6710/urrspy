package com.kayak.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * @author
 * @description
 * @create
 **/
@Slf4j
@Component
public class EmailThreadPoolUtils {
    @Autowired
    private EmailThreadPoolProperties emailThreadPoolProperties;
    public static ThreadPoolExecutor pool = null;

    private static EmailThreadPoolUtils emailThreadPoolUtils;

    @PostConstruct
    public void init() {
        emailThreadPoolUtils = this;
    }

    /**
     * 无响应执行
     *
     * @param runnable
     */
    public static void execute(Runnable runnable) {
        getThreadPool().execute(runnable);
    }

    /**
     * 有响应执行
     *
     * @param callable
     * @param <T>
     * @return
     */
    public static <T> Future<T> submit(Callable<T> callable) {
        return getThreadPool().submit(callable);
    }

    /**
     * 获取单例的线程池对象  属性以及含义。
     * corePoolSize:指定了线程池中的线程数量，它的数量决定了添加的任务是开辟新的线程去执行，还是放到workQueue任务队列中去；
     * maximumPoolSize:指定了线程池中的最大线程数量，这个参数会根据你使用的workQueue任务队列的类型，决定线程池会开辟的最大线程数量；
     * keepAliveTime:当线程池中空闲线程数量超过corePoolSize时，多余的线程会在多长时间内被销毁；
     * unit:keepAliveTime的单位
     * workQueue:任务队列，被添加到线程池中，但尚未被执行的任务  放的可执行线程。
     * new LinkedBlockingDeque<Runnable>(队列的容量));
     *
     * @return
     */
    public static ThreadPoolExecutor getThreadPool() {
        if (pool == null) {
            synchronized (EmailThreadPoolUtils.class) {
                if (pool == null) {
                    log.info("开辟程池！！！！！");
                    EmailThreadPoolProperties properties = emailThreadPoolUtils.emailThreadPoolProperties;
                    pool = new ThreadPoolExecutor(
                            properties.getMinCount(),
                            properties.getMaxCount(),
                            0L,
                            TimeUnit.MILLISECONDS,
                            new ArrayBlockingQueue<>(properties.getBlockingQueueCount()));
                }
            }
        }
        return pool;
    }
}
