package com.kayak.clear;

import com.kayak.clear.constants.LogTypeContents;
import com.kayak.clear.service.monitor.ClearTaskRegistService;
import com.kayakwise.kcloud.db.Dbop;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 任务注册定时器
 *
 * @author xiamh
 * @date 2022/6/17 10:34
 */
@Configuration
public class TaskRegistMonitor {

    private final Logger log = LoggerFactory.getLogger(TaskRegistMonitor.class);

    @Autowired
    private Dbop dbop;

    @Autowired
    private ApplicationContext ac;
    @Value("${redis.lock.waitTime:5}")
    private String waitTime;
    @Value("${redis.lock.leaseTime:300}")
    private String leaseTime;

    private String redissonKeyA = "{Dps}:TaskRegistMonitor:KeyA";
    private String redissonKeyB = "{Dps}:TaskRegistMonitor:KeyB";

    @Autowired
    private RedissonClient redissonClient;

    @Scheduled(cron = "${pms.scheduled.regist.dispatch}")
    public void doRegistProcess() {
        MDC.put("LogType", LogTypeContents.RegistMonitor);
        MDC.put("uuid", UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16));

        // 获取任务注册业务处理 service，内部成员变量多，service不通过@Autowired注入
        ClearTaskRegistService clearTaskRegistService = ac.getBean(ClearTaskRegistService.class);

        /*
         * 注意：
         *  日切将参数表下一系统工作日更新到当前系统工作日进行系统工作日切换，目前做法是：
         * 	  1. 日切必须是当天最后一个清算任务，日切的业务校验里面要校验当前系统工作日的清算是否已经全部执行完成，未执行完成不能日切
         * 	  2. 日切切到下一工作日后，任务注册扫描到展示表没有当前系统工作日的任务数据，就会注册下一日的任务，包括日切，执行日期为下一工作日，这样完成循环
         */

        // 多机部署的话需要增加分布式锁，否则会有并发问题
        RLock lock = redissonClient.getLock(redissonKeyA);
        try {
            if (lock.tryLock(Integer.parseInt(waitTime), Integer.parseInt(leaseTime), TimeUnit.SECONDS)) {
                log.info("---------- TaskRegistMonitor分布式加锁：" + redissonKeyA + " --------------");
                // 注册清算任务
                clearTaskRegistService.registProcess();

            }


        } catch (Exception e) {
            log.error("任务注册定时任务出错！错误信息:[{}]", e.getMessage(), e);
        } finally {
            // 加了分布式锁则需要释放锁
            if (lock.isHeldByCurrentThread()) {
                log.info("---------- TaskRegistMonitor分布式解锁：" + redissonKeyA + " --------------");
                Optional.of(lock).ifPresent(RLock::unlock);
            }
        }
    }

    @Scheduled(cron = "${pms.scheduled.regist.actdispatch}")
    public void doRegistProcessB() {
        MDC.put("LogType", LogTypeContents.RegistMonitor);
        MDC.put("uuid", UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16));

        // 获取任务注册业务处理 service，内部成员变量多，service不通过@Autowired注入
        ClearTaskRegistService clearTaskRegistService = ac.getBean(ClearTaskRegistService.class);

        /*
         * 注意：
         *  日切将参数表下一系统工作日更新到当前系统工作日进行系统工作日切换，目前做法是：
         * 	  1. 日切必须是当天最后一个清算任务，日切的业务校验里面要校验当前系统工作日的清算是否已经全部执行完成，未执行完成不能日切
         * 	  2. 日切切到下一工作日后，任务注册扫描到展示表没有当前系统工作日的任务数据，就会注册下一日的任务，包括日切，执行日期为下一工作日，这样完成循环
         */

        // 多机部署的话需要增加分布式锁，否则会有并发问题
        RLock lock = redissonClient.getLock(redissonKeyB);
        try {
            if (lock.tryLock(Integer.parseInt(waitTime), Integer.parseInt(leaseTime), TimeUnit.SECONDS)) {
                log.info("---------- TaskRegistMonitor分布式加锁：" + redissonKeyB + " --------------");
                //实时清算任务刷新
                clearTaskRegistService.registProcessB();

            }
        } catch (Exception e) {
            log.error("任务注册定时任务出错！错误信息:[{}]", e.getMessage(), e);
        } finally {
            // 加了分布式锁则需要释放锁
            if (lock.isHeldByCurrentThread()) {
                log.info("---------- TaskRegistMonitor分布式解锁：" + redissonKeyB + " --------------");
                Optional.of(lock).ifPresent(RLock::unlock);
            }
        }
    }

}