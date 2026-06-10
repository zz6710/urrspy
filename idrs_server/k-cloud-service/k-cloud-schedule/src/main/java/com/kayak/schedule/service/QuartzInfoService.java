package com.kayak.schedule.service;

import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.util.DateUtil;
import com.kayak.schedule.biz.QuartzBiz;
import com.kayak.schedule.dao.QuartzInfoDao;
import com.kayak.schedule.model.QuartzInfo;
import com.kayak.schedule.utils.QuartzUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;

import java.util.HashMap;
import java.util.Map;


@Service
@APIDefine(desc = "任务调度配置表服务", model = QuartzInfo.class)
public class QuartzInfoService {
	@Autowired
	private QuartzBiz quartzBiz;
	@Autowired
	private QuartzInfoDao quartzinfoDao;

	@API(desc = "查询任务调度配置表信息", auth = APIAuth.YES, operation = APIOperation.SELECT)
	public SqlResult<QuartzInfo> findQuartzInfos(SqlParam<QuartzInfo> params) throws Exception {
		params.setMakeSql(true);
		return quartzinfoDao.findQuartzInfos(params);
	}

	@API(desc = "添加任务调度配置表", auth = APIAuth.YES, operation = APIOperation.INSTER)
	public int addQuartzInfo(SqlParam<QuartzInfo> params) throws Exception {
		String date = DateUtil.getTimestamp19();
		params.getModel().setCreateTime(date);
		String rule = params.getModel().getQuartzRule();
		String week = params.getModel().getWeek();
		String taskTime = params.getModel().getTaskTime();
		String day = params.getModel().getDay();
		String quarter = params.getModel().getQuarter();
		String freqSecond = params.getModel().getFreqSecond();
		String freqMinute = params.getModel().getFreqMinute();
		String freqHour = params.getModel().getFreqHour();
		Map<String, String> mapcron = new HashMap<String, String>();
		if (rule.equals("0")) {//按每月
			mapcron = QuartzUtil.createCronExpressionByDay(day, taskTime);
		} else if (rule.equals("1")) {//按每周
			mapcron = QuartzUtil.createCronExpressionByWeek(week, taskTime);
		} else if (rule.equals("2")) {//按每天
			mapcron = QuartzUtil.createCronExpression(taskTime);
		} else if (rule.equals("3")) {//按季度
			mapcron = QuartzUtil.createCronExpression(quarter, taskTime);
		} else if (rule.equals("4")) {//按频率(秒)
			mapcron = QuartzUtil.createCronExpression(freqSecond, 1);
		} else if (rule.equals("5")) {//按频率(分)
			mapcron = QuartzUtil.createCronExpression(freqMinute, 2);
		} else if (rule.equals("6")) {//按频率(小时)
			mapcron = QuartzUtil.createCronExpression(freqHour, 3);
		}
		String cronExpression = mapcron.get("cronExpression");
		//String quartzRule=mapcron.get("quartzRule");
		params.getModel().setCronExpression(cronExpression);
		params.getModel().setStatus("0");
		return quartzinfoDao.addQuartzInfo(params.getModel()).getEffect();
	}

	@API(desc = "修改任务调度配置表", auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public int updateQuartzInfo(SqlParam<QuartzInfo> params) throws Exception {
		String date = DateUtil.getTimestamp19();
		params.getModel().setModifyTime(date);
		String rule = params.getModel().getQuartzRule();
		String week = params.getModel().getWeek();
		String taskTime = params.getModel().getTaskTime();
		String day = params.getModel().getDay();
		String quarter = params.getModel().getQuarter();
		String freqSecond = params.getModel().getFreqSecond();
		String freqMinute = params.getModel().getFreqMinute();
		String freqHour = params.getModel().getFreqHour();
		Map<String, String> mapcron = new HashMap<String, String>();
		if (rule.equals("0")) {//按每月
			mapcron = QuartzUtil.createCronExpressionByDay(day, taskTime);
		} else if (rule.equals("1")) {//按每周
			mapcron = QuartzUtil.createCronExpressionByWeek(week, taskTime);
		} else if (rule.equals("2")) {//按每天
			mapcron = QuartzUtil.createCronExpression(taskTime);
		} else if (rule.equals("3")) {//按季度
			mapcron = QuartzUtil.createCronExpression(quarter, taskTime);
		} else if (rule.equals("4")) {//按频率(秒)
			mapcron = QuartzUtil.createCronExpression(freqSecond, 1);
		} else if (rule.equals("5")) {//按频率(分)
			mapcron = QuartzUtil.createCronExpression(freqMinute, 2);
		} else if (rule.equals("6")) {//按频率(小时)
			mapcron = QuartzUtil.createCronExpression(freqHour, 3);
		}
		String cronExpression = mapcron.get("cronExpression");
		//String quartzRule=mapcron.get("quartzRule");
		params.getModel().setCronExpression(cronExpression);
		return quartzinfoDao.updateQuartzInfo(params).getEffect();
	}

	/**
	 * 功能：启动一个任务
	 * 作者：rennannan
	 * 日期：20210601
	 */
	@API(desc = "启动任务", auth = APIAuth.YES, operation = APIOperation.DELETE)
	public int updateStatusOnEnable(SqlParam<QuartzInfo> params) throws Exception {
		String preTaskId = params.getModel().getPreTaskId();
		Map<String, String> mapV = new HashMap<String, String>();
		mapV = getpreInfoById(preTaskId);
		String preJobClassPath = mapV.get("jobClassPath");
		String preJobName = mapV.get("jobName");
		String preJobGroup = mapV.get("jobGroup");
		params.getModel().setPreJobClassPath(preJobClassPath);
		params.getModel().setPreJobName(preJobName);
		params.getModel().setPreJobGroup(preJobGroup);
		quartzBiz.startTask(params.getModel());
		return 1;
	}

	/**
	 * 功能：停用一个任务
	 * 作者：rennannan
	 * 日期：20210601
	 */
	@API(desc = "停用任务", auth = APIAuth.YES, operation = APIOperation.DELETE)
	public int updateStatusOnStop(SqlParam<QuartzInfo> params) throws Exception {
		String id = params.getModel().getId();
		String jobName = params.getModel().getJobName();
		String jobGroup = params.getModel().getJobGroup();
		quartzBiz.pauseTask(id, jobName, jobGroup);
		return 1;
	}

	/**
	 * 根据前置任务id获取前置任务bean,和前置任务方法
	 * 描述 : <描述函数实现的功能>. <br>
	 * <p>
	 *
	 * @param preTaskId --前置任务id
	 * @return
	 * @author lixiao
	 */
	private Map<String, String> getpreInfoById(String preTaskId) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map = quartzBiz.getpreInfoById(preTaskId);
		return map;

	}

	@API(desc = "删除任务调度配置表", auth = APIAuth.YES, operation = APIOperation.DELETE)
	public int deleteQuartzInfo(SqlParam<QuartzInfo> params) throws Exception {
		return quartzinfoDao.deleteQuartzInfo(params.getModel().getId()).getEffect();
	}

}
