package com.kayak.schedule.biz;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.kayak.schedule.dao.QuartzInfoDao;
import com.kayak.schedule.exception.MySchedulerException;
import com.kayak.schedule.model.QuartzInfo;
import com.kayak.schedule.utils.QuartzUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Scope("prototype")
@Service
public class QuartzBiz {
	/**
	 * 调度工厂Bean
	 */
	@Autowired
	private Scheduler scheduler;
	@Autowired
	private QuartzInfoDao quartzDao;

	public void startTask(QuartzInfo quartzInfo) throws Exception {
		CronTrigger cronTrigger = QuartzUtil.getCronTrigger(scheduler, quartzInfo.getJobName(), quartzInfo.getJobGroup());
		String id = String.valueOf(quartzInfo.getId());//id
		String preTaskId = quartzInfo.getPreTaskId();//前置任务id
		String[] pathArgs = quartzDao.getpreAllPathByPreId(preTaskId);
		if (cronTrigger == null) {
			//创建一个新的任务
			QuartzUtil.creatSchedulerJob(scheduler, quartzInfo, pathArgs);
			QuartzInfo info = new QuartzInfo();
			info.setStatus("1");
			info.setId(id);
			quartzDao.updateStatus(info);
		} else {
			// Trigger已存在，那么更新相应的定时设置
			QuartzUtil.deleteTask(scheduler, quartzInfo.getJobName(), quartzInfo.getJobGroup());
			QuartzUtil.creatSchedulerJob(scheduler, quartzInfo, pathArgs);
			QuartzInfo info = new QuartzInfo();
			info.setStatus("1");
			info.setId(id);
			quartzDao.updateStatus(info);
		}
	}

	public void pauseTask(String id, String jobName, String jobGroup) throws Exception {
		QuartzUtil.pauseSchedulerJob(scheduler, jobName, jobGroup);
		QuartzInfo info = new QuartzInfo();
		info.setStatus("0");
		info.setId(id);
		quartzDao.updateStatus(info);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void deleteTask(String id, String jobName, String jobGroup) throws MySchedulerException, Exception {
		QuartzUtil.deleteTask(scheduler, jobName, jobGroup);
		quartzDao.deleteQuartzInfo(id);
		//quartzDao.deleteTaskLog(id);
	}

	public void addTask(QuartzInfo quartzInfo) throws Exception {
		quartzDao.addQuartzInfo(quartzInfo);
	}

	public void modTask(QuartzInfo quartzInfo) throws Exception {
		quartzDao.modTask(quartzInfo);
	}

	public void jobInit() throws Exception {
		List<Map<String, String>> listMaps = quartzDao.getStartTask();
		if (CollectionUtils.isEmpty(listMaps)) {
			return;
		}
		for (Map<String, String> map : listMaps) {
			String jobName = map.get("jobName");
			String jobGroup = map.get("jobGroup");
			String jobClassPath = map.get("jobClassPath");
			String cronExpression = map.get("cronexpression");
			String preTaskId = map.get("pretaskid");

			Map<String, String> mapV = new HashMap<String, String>();
			String preJobClassPath = "";
			String preJobName = "";
			String preJobGroup = "";
			if (StringUtils.isNotEmpty(preTaskId)) {
				mapV = getpreInfoById(preTaskId);
				preJobClassPath = mapV.get("jobClassPath");
				preJobName = mapV.get("jobName");
				preJobGroup = mapV.get("jobGroup");
			}
			QuartzInfo quartzInfo = new QuartzInfo();
			quartzInfo.setJobName(jobName);
			quartzInfo.setJobClasspath(jobClassPath);
			quartzInfo.setJobGroup(jobGroup);
			quartzInfo.setCronExpression(cronExpression);
			quartzInfo.setPreJobName(preJobName);
			quartzInfo.setPreJobGroup(preJobGroup);
			quartzInfo.setPreJobClassPath(preJobClassPath);
			String[] pathArgs = quartzDao.getpreAllPathByPreId(preTaskId);
			//在Scheduler获取触发器对象
			CronTrigger cronTrigger = QuartzUtil.getCronTrigger(scheduler, jobName, jobGroup);
			if (cronTrigger == null) {
				//创建一个新任务
				QuartzUtil.creatSchedulerJob(scheduler, quartzInfo, pathArgs);

			} else {
				QuartzUtil.deleteTask(scheduler, jobName, jobGroup);
				QuartzUtil.creatSchedulerJob(scheduler, quartzInfo, pathArgs);
			}
		}
	}

	//	public void addTaskLog(String quartzid,String jobname,String jobgroup,String loginfo,String operatetime) throws Exception{
//		quartzDao.addTaskLog(quartzid, jobname, jobgroup, loginfo, operatetime);
//	}
	public Map<String, String> getpreInfoById(String preTaskId) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map = quartzDao.getPreInfoById(preTaskId);
		return map;
	}

}
