package com.kayak.schedule.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.kayak.core.util.DateUtil;
import com.kayak.schedule.exception.MySchedulerException;
import com.kayak.schedule.model.QuartzInfo;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title:  ast_web
 * Description:  定时任务帮助类
 * Company:	kayak
 * Makedate: 2015-12-18 下午4:24:32
 *
 * @author lixiao
 */
public class QuartzUtil {
	
	static Logger log = LoggerFactory.getLogger(QuartzUtil.class);
    /**
     * 根据jobName,jobGroup获取触发器
     * 描述 : <描述函数实现的功能>. <br>
     * <p>
     *
     * @param scheduler 调度程序对象
     * @param jobName   job名
     * @param jobGroup  job分组
     * @return
     * @author lixiao
     */
    public static CronTrigger getCronTrigger(Scheduler scheduler, String jobName, String jobGroup) {
        //初始化触发器对象
        CronTrigger cronTrigger = null;
        try {
            //所有类型的trigger都有TriggerKey这个属性，表示trigger的身份
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        } catch (SchedulerException e) {

            e.printStackTrace();
        }
        return cronTrigger;

    }

    /**
     * 创建任务
     * 描述 : <描述函数实现的功能>. <br>
     * <p>
     *
     * @param scheduler
     * @param quartzInfo
     * @throws
     * @author lixiao
     */

    public static void creatSchedulerJob(Scheduler scheduler, QuartzInfo quartzInfo, String[] pathArgs) throws MySchedulerException {
        creatSchedulerJob(scheduler, quartzInfo.getJobName(), quartzInfo.getJobGroup(), quartzInfo.getCronExpression(), quartzInfo.getJobClasspath(), quartzInfo, pathArgs);
    }

    /**
     * 重写创建任务方法
     * 描述 : <描述函数实现的功能>. <br>
     * <p>
     *
     * @param scheduler
     * @param jobName
     * @param jobGroup
     * @param cronExpression
     * @param jobClassPath
     * @param param
     * @throws MySchedulerException
     * @author lixiao
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void creatSchedulerJob(Scheduler scheduler, String jobName, String jobGroup, String cronExpression, String jobClassPath, Object param, String[] pathArgs) throws MySchedulerException {
        try {
            //通过反射获取job类
          
            Class jobClass = Class.forName(jobClassPath);
            //创建JobDetail对象
           
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroup).build();
            //在jobdetail中存入数据，调用job时可使用
            jobDetail.getJobDataMap().put("quartzInfo", param);
            jobDetail.getJobDataMap().put("pathArgs", pathArgs);
            //表达式调度构建器
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            //创建一个新的触发器
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup).withSchedule(cronScheduleBuilder).build();
            //启动任务
            try {
                Date date = scheduler.scheduleJob(jobDetail, cronTrigger);
                log.info("定时任务【{}】,创建成功，执行时间【{}】",jobName,DateUtil.dateFormate(date, "yyyy-MM-dd HH:mm:ss"));
            } catch (SchedulerException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw new MySchedulerException("创建任务失败!");
            }
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 暂停定时任务
     * 描述 : <描述函数实现的功能>. <br>
     * <p>
     *
     * @param scheduler
     * @param jobName
     * @param jobGroup
     * @throws MySchedulerException
     * @author lixiao
     */
    public static void pauseSchedulerJob(Scheduler scheduler, String jobName, String jobGroup) throws MySchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        try {
            scheduler.pauseJob(jobKey);

        } catch (SchedulerException e) {
            throw new MySchedulerException("定时任务暂停失败！！");
        }
    }

    /**
     * 删除定时任务
     * 描述 : <描述函数实现的功能>. <br>
     * <p>
     *
     * @param scheduler
     * @param jobName
     * @param jobGroup
     * @throws MySchedulerException
     * @author lixiao
     */
    public static void deleteTask(Scheduler scheduler, String jobName, String jobGroup) throws MySchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        try {
            if(scheduler.deleteJob(jobKey))
            	log.info("定时任务删除【{}】",jobName);
            	
        } catch (SchedulerException e) {
            // TODO Auto-generated catch block
            throw new MySchedulerException("定时任务删除失败！！！");
        }
    }

    /**
     * 更新定时任务
     * 描述 : <描述函数实现的功能>. <br>
     * <p>
     *
     * @param scheduler
     * @param quartzInfo
     * @throws MySchedulerException
     * @author lixiao
     */
    public static void updateTask(Scheduler scheduler, QuartzInfo quartzInfo) throws MySchedulerException {
        updateTask(scheduler, quartzInfo.getJobName(), quartzInfo.getJobGroup(), quartzInfo.getCronExpression(), quartzInfo.getJobClasspath(), quartzInfo);
    }

    /**
     * 重写更新定时任务
     * 描述 : <描述函数实现的功能>. <br>
     * <p>
     *
     * @param scheduler
     * @param jobName
     * @param jobGroup
     * @param cronExpression
     * @param jobClassPath
     * @param param
     * @throws MySchedulerException
     * @author lixiao
     */
    public static void updateTask(Scheduler scheduler, String jobName, String jobGroup, String cronExpression, String jobClassPath, Object param) throws MySchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        //表达式调度构建器
        CronScheduleBuilder schedBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
        try {
            CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            //在jobdetail中存入数据，调用job时可使用
            jobDetail.getJobDataMap().put("quartzInfo", param);
            //重新构建触发器
            cronTrigger = cronTrigger.getTriggerBuilder().withIdentity(triggerKey)
                    .withSchedule(schedBuilder).build();
            scheduler.rescheduleJob(triggerKey, cronTrigger);

        } catch (SchedulerException e) {
            throw new MySchedulerException("更新定时任务出错！！");
        }
    }

    /**
     * 创建任务运行规则，任务运行表达式
     * 描述 : <描述函数实现的功能>. <br>
     * <p>
     *
     * @param week     --星期  1,2,3,4
     * @param taskTime --时间  122334
     * @return cronExpression  quartzRule
     * @author lixiao
     */
    public static Map<String, String> createCronExpressionByWeek(String week, String taskTime) {
        StringBuilder cron = new StringBuilder();
        StringBuilder rule = new StringBuilder();
        Map<String, String> mapWeek = new LinkedHashMap<String, String>();
        mapWeek.put("1", "MON");
        mapWeek.put("2", "TUE");
        mapWeek.put("3", "WED");
        mapWeek.put("4", "THU");
        mapWeek.put("5", "FRI");
        mapWeek.put("6", "SAT");
        mapWeek.put("7", "SUN");
        for (Map.Entry<String, String> entry : mapWeek.entrySet()) {
            week = week.replace(entry.getKey(), entry.getValue());
        }
        Map<String, String> mapresult = new HashMap<String, String>();
        cron.append((String) taskTime.subSequence(4, 6)).append(" ")
                .append((String) taskTime.subSequence(2, 4)).append(" ")
                .append((String) taskTime.subSequence(0, 2)).append(" ? * ")
                .append(week);
        Map<String, String> mapR = new LinkedHashMap<String, String>();
        mapR.put("MON", "一");
        mapR.put("TUE", "二");
        mapR.put("WED", "三");
        mapR.put("THU", "四");
        mapR.put("FRI", "五");
        mapR.put("SAT", "六");
        mapR.put("SUN", "日");
        for (Map.Entry<String, String> entry : mapR.entrySet()) {
            week = week.replace(entry.getKey(), entry.getValue());
        }
        rule.append("每周").append(week).append("的").append((String) taskTime.subSequence(0, 2))
                .append(":").append((String) taskTime.subSequence(2, 4)).append(":")
                .append((String) taskTime.subSequence(4, 6));
        String cronExpression = cron.toString();
        String quartzRule = rule.toString();
        mapresult.put("cronExpression", cronExpression);
        mapresult.put("quartzRule", quartzRule);
        return mapresult;
    }

    /**
     * 创建任务运行规则，任务运行表达式
     * 描述 : <描述函数实现的功能>. <br>
     * <p>
     *
     * @param day      --日期
     * @param taskTime --时间
     * @return cronExpression  quartzRule
     * @author lixiao
     */
    public static Map<String, String> createCronExpressionByDay(String day, String taskTime) {
        Map<String, String> mapresult = new HashMap<String, String>();
        StringBuilder cron = new StringBuilder();
        StringBuilder rule = new StringBuilder();
        cron.append((String) taskTime.subSequence(4, 6)).append(" ")
                .append((String) taskTime.subSequence(2, 4)).append(" ")
                .append((String) taskTime.subSequence(0, 2)).append(" ")
                .append(day).append(" * ?");
        rule.append("每月的").append(day.replace(",", "日,")).append("日").append("的").append((String) taskTime.subSequence(0, 2))
                .append(":").append((String) taskTime.subSequence(2, 4)).append(":")
                .append((String) taskTime.subSequence(4, 6));
        String cronExpression = cron.toString();
        String quartzRule = rule.toString();
        mapresult.put("cronExpression", cronExpression);
        mapresult.put("quartzRule", quartzRule);
        return mapresult;

    }

    /**
     * 创建任务运行规则，任务运行表达式
     * 描述 : <描述函数实现的功能>. <br>
     * <p>
     *
     * @param taskTime --时间
     * @return
     * @author lixiao
     */
    public static Map<String, String> createCronExpression(String taskTime) {
        Map<String, String> mapresult = new HashMap<String, String>();
        StringBuilder cron = new StringBuilder();
        StringBuilder rule = new StringBuilder();
        cron.append((String) taskTime.subSequence(4, 6)).append(" ")
                .append((String) taskTime.subSequence(2, 4)).append(" ")
                .append((String) taskTime.subSequence(0, 2))
                .append(" * * ?");
        rule.append("每天的").append((String) taskTime.subSequence(0, 2))
                .append(":").append((String) taskTime.subSequence(2, 4)).append(":")
                .append((String) taskTime.subSequence(4, 6)).append("执行");
        String cronExpression = cron.toString();
        String quartzRule = rule.toString();
        mapresult.put("cronExpression", cronExpression);
        mapresult.put("quartzRule", quartzRule);

        return mapresult;
    }


    public static Map<String, String> createCronExpression(String quarter, String taskTime) {
        Map<String, String> mapresult = new HashMap<String, String>();
        StringBuilder cron = new StringBuilder();
        StringBuilder rule = new StringBuilder();
        cron.append((String) taskTime.subSequence(4, 6)).append(" ")
                .append((String) taskTime.subSequence(2, 4)).append(" ")
                .append((String) taskTime.subSequence(0, 2)).append(" 20 ").append(quarter).append(" ? *");
        //"34 23 11 20 2 ? *"
        rule.append("每年的").append(quarter).append("月20的").append((String) taskTime.subSequence(0, 2))
                .append(":").append((String) taskTime.subSequence(2, 4)).append(":")
                .append((String) taskTime.subSequence(4, 6)).append("执行");
        mapresult.put("cronExpression", cron.toString());
        mapresult.put("quartzRule", rule.toString());
        return mapresult;

    }

    public static Map<String, String> createCronExpression(String freq, int flag) {
        Map<String, String> mapresult = new HashMap<String, String>();
        StringBuilder cron = new StringBuilder(); //表达式
        StringBuilder rule = new StringBuilder();    //运行规则
        switch (flag) {
            //按秒
            case 1:
                cron.append("0/").append(freq).append(" * * * * ?");
                rule.append("每隔").append(freq).append("秒执行一次");
                break;
            //按分钟
            case 2:
                cron.append("0 0/").append(freq).append(" * * * ?");
                rule.append("每隔").append(freq).append("分钟执行一次");
                break;
            //按小时
            case 3:
                cron.append("0 0 0/").append(freq).append(" * * ?");
                rule.append("每隔").append(freq).append("小时执行一次");
                break;
            default:
                break;
        }
        mapresult.put("cronExpression", cron.toString());
        mapresult.put("quartzRule", rule.toString());
        return mapresult;

    }

    /**
     * 获取当前时间字符串
     * 描述 : <描述函数实现的功能>. <br>
     * <p>
     *
     * @return
     * @author lixiao
     */
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }


    /**
     * 获取容器中的JobDetail对象
     * 描述 : <描述函数实现的功能>. <br>
     * <p>
     *
     * @param scheduler
     * @param jobName
     * @param jobGroup
     * @return
     * @author lixiao
     */
    public static JobDetail getJobDetail(Scheduler scheduler, String jobName, String jobGroup) {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        JobDetail jobDetail = null;
        try {
            jobDetail = scheduler.getJobDetail(jobKey);
        } catch (SchedulerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jobDetail;
    }

}
