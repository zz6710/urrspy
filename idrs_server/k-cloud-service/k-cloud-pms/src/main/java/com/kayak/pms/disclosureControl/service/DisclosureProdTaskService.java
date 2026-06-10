package com.kayak.pms.disclosureControl.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.graphql.model.FetcherData;
import com.kayak.pms.T85.dao.SysDictItemDao;
import com.kayak.pms.disclosureControl.dao.*;
import com.kayak.pms.disclosureControl.model.*;
import com.kayak.pms.global.constants.*;
import com.kayak.pms.netValue.dao.T8ProdNetValueTaskDao;
import com.kayak.pms.netValue.model.T8ProdNetValueTask;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
@APIDefine(desc = "信披任务表服务", model = DisclosureProdTask.class)
public class DisclosureProdTaskService {
	ArrayBlockingQueue<Runnable> queues = new ArrayBlockingQueue<Runnable>(1000);

	private final ExecutorService es = new ThreadPoolExecutor(2, 10, 2000,
			TimeUnit.MILLISECONDS, queues);

	@Resource
	private DisclosureProdTaskService disclosureProdTaskService;
	@Autowired
	private ScheduleNoticeService scheduleNoticeService;
	@Autowired
	private DisclosureProdTaskDao disclosureProdTaskDao;
	@Autowired
	private DisclosureProdTaskDao t8DisclosureProdTaskDao;
	@Autowired
	private ScheduleProdRuleDao disclosureProdRuleDao;
	@Autowired
	private DisclosureWorkdayDao disclosureWorkdayDao;
	@Autowired
	private ScheduleNoticeDao t8DisclosureNoticeDao;
	@Autowired
	private DisclosureOperationDao disOperationDao;
	@Autowired
	private DisclosureNoticeProcessDao disNoticeProcessDao;
	@Autowired
	private ScheduleWorkdayDao scheduleWorkdayDao;

	@Autowired
	private SysDictItemDao sysDictItemDao;

	@Autowired
	private T8ProdNetValueTaskDao t8ProdNetValueTaskDao;

	private Logger logger = LoggerFactory.getLogger(DisclosureProdTaskService.class);

		@API(desc = "查询信披任务", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public SqlResult<DisclosureProdTask> findT8DisclosureProdTasksAuth(SqlParam<DisclosureProdTask> params)
			throws Exception {
		return findT8DisclosureProdTasks(params);
	}

	@API(desc = "查询信披任务", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public SqlResult<DisclosureProdTask> findT8DisclosureProdTasks(SqlParam<DisclosureProdTask> params)
			throws Exception {
//		params.setMakeSql(false);
		return t8DisclosureProdTaskDao.findT8DisclosureProdTasks(params);
	}
	/**
	* @功能描述:页面手动操作生成报告披露任务
	* @params:[params]
	* @return:java.lang.String
	* @Athor:ouyifan
	* @date:2022/6/20
	*/
	@API(desc = "生成信披任务", auth = APIAuth.NO, operation = APIOperation.INSTER)
	public String generateTaskManual(SqlParam<DisclosureProdTask> params) throws Exception {
		try {
			// 获取信披类型
			String disclosureType = params.getModel().getDisclosureType();
			// 获取信披子类型
			String disclosureSonType = params.getModel().getDisclosureSonType();
			// 验证是否存在配置对应信披类型的产品信披规则，如果不存在，进行提示
			ScheduleProdRule prodRule = new ScheduleProdRule();
			prodRule.setDisclosureType(disclosureType);
			prodRule.setDisclosureSonType(disclosureSonType);
			List<ScheduleProdRule> prodRuleList;
			// 整体公告需校验信披规则是否存在且启用
			if (disclosureType.equals(DisclosureType.ensemble.getItemKey())||disclosureSonType.equals(DisclosureSonType.netValueEntity.getItemKey())){
				prodRuleList= disclosureProdRuleDao.findRuleExists(prodRule);
			}else {
				prodRuleList= disclosureProdRuleDao.findRuleExistsByType(prodRule);
			}
			if (prodRuleList.size() == 0) {
				return RequestSupport.updateReturnJson(false, "未配置或启动对应信披类型的信披规则！", null).toString();
			}
			if (disclosureType.equals(DisclosureType.regular.getItemKey())) {// 定期报告
				String startMonth = params.getModel().getStartMonth();// 开始月份 yyyyMM
				String endMonth = params.getModel().getEndMonth();// 结束月份 yyyyMM
				// 获取两日期之间所有月份
				List<String> monthList = DateUtil.getMonthBetween(startMonth, endMonth);
				DaoUtil.doTrans(() -> {
					// for循环逐个月处理
					for (int i = 0; i < monthList.size(); i++) {
						String month = monthList.get(i);
						String firstDate = month + "01"; // 当前月第一天
						String lastDate = DateUtil.getLastDayOfMonth(firstDate);// 当前月最后一天
						List<DisclosureProdTask> tasks = queryTask(firstDate, lastDate, TaskStart.manual.getItemKey(), disclosureSonType,disclosureType,"");
						addProdTask(tasks,"");
						logger.info("定期报告任务生成完成");
					}
				});
				return RequestSupport.updateReturnJson(true, "定期报告任务生成完成！", null).toString();
			} else if (disclosureType.equals(DisclosureType.issEst.getItemKey())) {// 发行报告
				// 基准日期
				String baseDate = params.getModel().getProdBaseDate();
				issueReportComnMethod(baseDate, TaskStart.manual.getItemKey(),"");
				return RequestSupport.updateReturnJson(true, "发行成立公告任务生成完成！", null).toString();
			} else if (disclosureType.equals(DisclosureType.expire.getItemKey())) {// 到期报告
				// 基准日期
				String baseDate = params.getModel().getProdBaseDate();
				endReportComnMethod(baseDate, TaskStart.manual.getItemKey(),"");
				return RequestSupport.updateReturnJson(true, "到期公告任务生成完成！", null).toString();
			}else if (disclosureType.equals(DisclosureType.preSale.getItemKey())){//售前信息登记
				String baseDate = params.getModel().getProdBaseDate();
				beforeSaleDocReportComnMethod(baseDate,TaskStart.manual.getItemKey(),"",disclosureSonType);
				return RequestSupport.updateReturnJson(true, "售前信息登记任务生成完成！", null).toString();
			}else if (disclosureType.equals(DisclosureType.ensemble.getItemKey())){//整体公告
				//整体公告单独方式生成任务，取信披生成规则生成，不取产品信披规则
				String baseDate = params.getModel().getProdBaseDate();// 开始年份 yyyy
				DaoUtil.doTrans(() -> {
					List<DisclosureProdTask> tasks = queryTaskZT(baseDate.substring(0,4), TaskStart.manual.getItemKey(), disclosureSonType,disclosureType,"");
					addProdTask(tasks,"");
					logger.info("整体公告任务生成完成");
				});
				return RequestSupport.updateReturnJson(true, "整体公告任务生成完成！", null).toString();
			}else if (disclosureType.equals(DisclosureType.net.getItemKey())){//净值公告
					String startDate = params.getModel().getStartDate();// 开始日期
					String endDate = params.getModel().getEndDate();// 结束日期
					//TODO 遍历选择的基准日期区间—>每一天作为基准日,目前只允许选择一天
					String baseDate = params.getModel().getProdBaseDate();
					List<DisclosureProdTask> tasks =netDocReportComnMethod(baseDate,TaskStart.manual.getItemKey(),"",disclosureSonType,disclosureType);
					addProdTask(tasks,"");
					logger.info("净值单产品公告任务生成完成");
				return RequestSupport.updateReturnJson(true, "净值单产品公告任务生成完成！", null).toString();
			}else if (disclosureType.equals(DisclosureType.bonus.getItemKey())) {// 分红公告
				// 基准日期
				String baseDate = params.getModel().getProdBaseDate();
				issueReportBonusMethod(baseDate,TaskStart.manual.getItemKey(),"");
				return RequestSupport.updateReturnJson(true, "分红公告任务生成完成！", null).toString();
			}else if (disclosureType.equals(DisclosureType.sale.getItemKey())){//销售文档
				String baseDate = params.getModel().getProdBaseDate();
				saleDocReportComnMethod(baseDate,TaskStart.manual.getItemKey(),"");
				return RequestSupport.updateReturnJson(true, "销售文档任务生成完成！", null).toString();
			}else if (disclosureType.equals(DisclosureType.purchase.getItemKey())){//申购赎回公告
				String startDate = params.getModel().getStartDate();// 开始日期
				String endDate = params.getModel().getEndDate();// 结束日期
				//申购赎回公告是周一公布上周的数据，上周非工作日就公布上上周的数据
				//计算日期区间内所有的基准日期，一般为周一，如果周一为非工作日，一直往下找最近的工作日
				//首先获取日期区间内的所有周一，公告发布日，基准日
				List<String> weekMonday = DateUtil.getDayOfWeekWithinDateInterval(startDate, endDate,1);
				//判断当天是否为工作日，如果不是顺延找最近工作日，
				Set<String> workSet = new HashSet<>();
				for (String w:weekMonday) {
					List<DisclosureWorkday> disclosureWorkdays = scheduleNoticeService.WorkdayCheck(w, 1);
					if(null !=disclosureWorkdays && disclosureWorkdays.size()>0){
						String workday = disclosureWorkdays.get(0).getWorkday();
						workSet.add(workday);
					}
				}
				//遍历发布日set,如果上周有数据就生成任务
				DaoUtil.doTrans(() -> {
					// for循环逐个周处理
					for (String day:workSet) {
						List<DisclosureProdTask> tasks = queryTaskPeriod(day, TaskStart.manual.getItemKey(), disclosureSonType, disclosureType, "");
						addProdTask(tasks, "");
					}
					logger.info("申购赎回报告任务生成完成");
				});

//				// 获取两日期之间所有周一
//				List<String> weekMonday = DateUtil.getDayOfWeekWithinDateInterval(startDate, endDate,1);
//				DaoUtil.doTrans(() -> {
//					// for循环逐个周处理
//					for (int i = 0; i < weekMonday.size(); i++) {
//						String Monday = weekMonday.get(i);
//						boolean flag = false;
//						//取后五内日期，校验是否存在工作日
//						List<DisclosureWorkday>workList =scheduleNoticeService.WorkdayCheck(Monday,5);
//						for (DisclosureWorkday day :workList) {
//							for (int j = 0; j < 5; j++) {
//								if (day.getWorkday().equals(DateUtil.add(Monday,"yyyyMMdd",i)));
//								flag = true;
//							}
//						}
//						if (flag) {
//							List<DisclosureProdTask> tasks = queryTaskPeriod(Monday, TaskStart.manual.getItemKey(), disclosureSonType, disclosureType, "");
//							addProdTask(tasks,"");
//						}
//						logger.info("申购赎回报告任务生成完成");
//					}
//				});
				return RequestSupport.updateReturnJson(true, "申购赎回报告任务生成完成！", null).toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false, "任务生成失败", null).toString();
		}

		return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
	}
	/**
	 * @功能描述:生成报告，手动生成报告数据，复选后提交
	 * @params:[params]
	 * @return:java.lang.String
	 * @Athor:ouyifan
	 * @date:2022/7/1
	 */
	@API(desc = "生成（更新）报告数据", auth = APIAuth.YES, operation = APIOperation.INSTER)
	public String batchGenerateData(SqlParam<DisclosureProdTask> params) throws Exception {

		Map<String, Object> paramsDirect = RequestSupport.getParameters();
		//判断是数据更新还是生成公告按钮触发
		boolean update = paramsDirect.get("update").toString().trim().equalsIgnoreCase("true".trim());
		//前端复选表单
		String obj = (String) paramsDirect.get("list");
		List<DisclosureProdTask> list = JSONObject.parseArray(obj, DisclosureProdTask.class);
		List<DisclosureProdTask> taskList = new ArrayList<>();
		AtomicReference<String> checkProdInfo = new AtomicReference<>("");
		String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));//用户编号
		String username = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username"));//用户姓名
		if (list != null && list.size() > 0) {// 勾选
			taskList.addAll(list);
		}else {
			return RequestSupport.updateReturnJson(false, "不存在对应的信披任务，无法生成报告数据！", null).toString();
		}
		new Thread(() -> {
			try {
				DaoUtil.doTrans(() -> {
					for (DisclosureProdTask task : taskList) {
							try {
								//校验任务是否有关联的且启用的规则id，没有则不支持生成或更新数据
								if (task.getDisclosureType().equals(DisclosureType.ensemble.getItemKey())||(task.getDisclosureType().equals(DisclosureType.net.getItemKey())&&task.getDisclosureSonType().equals(DisclosureSonType.netValueEntity.getItemKey()))){
									if (t8DisclosureProdTaskDao.checkTaskRuleId(task)<=0) {
										logger.info("未配置信披生成规则的任务 {}", JSONObject.toJSONString(task));
										return;
									}
								}else {
									if (t8DisclosureProdTaskDao.checkTaskProdRuleId(task)<=0){
										logger.info("未配置产品信披规则的任务 {}",JSONObject.toJSONString(task));
										return;
									}
								}
								if (update){
									if (task.getStatus().equals(TaskStatus.forGenerate.getItemKey())||
											//已经生成且公告状态为发布成功的公告不能更新
											t8DisclosureProdTaskDao.checkNoticeStatus(task).equals(DisclosureStatus.overSend.getItemKey())){
										logger.info("该任务还未生成公告或对应公告已成功发布不支持更新数据 {}",JSONObject.toJSONString(task));
										return;
									}
								}else {
									if (task.getStatus().equals(TaskStatus.alreadyGenerate.getItemKey())){
										logger.info("该任务已生成公告不支持再次生成公告 {}",JSONObject.toJSONString(task));
										return;
									}
								}

								/**
								 * 报告数据生成或更新公共方法，包括生成或重新生成版本，字段数据、发布渠道等
								 */
								comnGenerateNotice(task,update,userid,username);


							} catch (Exception e) {
								logger.error("生成报告失败{}", e);
								try{
									task.setStatus(TaskStatus.generateFailed.getItemKey());
									task.setCreateStatus(DisclosureStatus.creatFailed.getItemKey());
									t8DisclosureProdTaskDao.updateTaskStatus(task);
									t8DisclosureProdTaskDao.updateNoticeStatus(task);
								}catch (Exception ex) {
									logger.error("更新状态失败,{}", ex);
								}

							}
					}
				});
			} catch (Exception e) {
				logger.error("生成报告失败{}", e);
			}
		}).start();
			return RequestSupport.updateReturnJson(true, "操作成功,报告生成中,请稍后查看信披公告数据", null).toString();
	}
	/**
	 * @功能描述:生成报告，自动生成报告数据
	 * @params:[params]
	 * @return:java.lang.String
	 * @Athor:ouyifan
	 * @date:2022/7/1
	 */
	@API(desc = "自动批量生成报告数据", auth = APIAuth.NO, operation = APIOperation.INSTER)
	public String autoBatchGenerateData(SqlParam<DisclosureProdTask> params) throws Exception {
		StringBuilder str = new StringBuilder("任务未执行成功");
		Boolean reMgs = true;
		String nowDate = DateUtil.getSysWordDay();
		String nowTime = DateUtil.getNowTime();
		List<DisclosureProdTask> taskList = t8DisclosureProdTaskDao.findAutoGenTask(nowDate,params.getModel().getDisclosureType());
		String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));//用户编号
		String username = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username"));//用户姓名
		new Thread(() -> {
			try {
				DaoUtil.doTrans(() -> {
					for (DisclosureProdTask task : taskList) {
							try {
								//校验任务是否有关联的且启用的规则id，没有则不支持生成或更新数据
								if (task.getDisclosureType().equals(DisclosureType.ensemble.getItemKey())||(task.getDisclosureType().equals(DisclosureType.net.getItemKey())&&task.getDisclosureSonType().equals(DisclosureSonType.netValueEntity.getItemKey()))){
									if (t8DisclosureProdTaskDao.checkTaskRuleId(task)<=0) {
										logger.info("未配置信披生成规则的任务 {}", JSONObject.toJSONString(task));
										return;
									}
								}else {
									if (t8DisclosureProdTaskDao.checkTaskProdRuleId(task)<=0){
										logger.info("未配置产品信披规则的任务 {}",JSONObject.toJSONString(task));
										return;
									}
								}
								if (task.getStatus().equals(TaskStatus.alreadyGenerate.getItemKey())){
									logger.info("该任务已生成公告不支持再次生成公告 {}",JSONObject.toJSONString(task));
									return;
								}

								/**
								 * 报告数据生成或更新公共方法，包括生成或重新生成版本，字段数据、发布渠道等
								 */
								comnGenerateNotice(task,false,userid,username);


							} catch (Exception e) {
								logger.error("生成报告失败{}", e);
							}
					}
				});
			} catch (Exception e) {
				logger.error("生成报告失败{}", e);
			}
		}).start();
		List<DisclosureProdTask> checkTaskList = t8DisclosureProdTaskDao.findAutoGenTask(nowDate,params.getModel().getDisclosureType());
		for (DisclosureProdTask tasks: checkTaskList ) {
			if (tasks.getStatus().equals(TaskStatus.forGenerate)){
				str.append("[").append(tasks.getProdCode()).append("|").append(tasks.getDisclosureType()).append("|").append(tasks.getDisclosureSonType()).append("]");
				reMgs = false;
			}
		}
		if (reMgs){
			return RequestSupport.updateReturnJson(true, "", null).toString();
		}else {
			return RequestSupport.updateReturnJson(false, str.toString(), null).toString();
		}


	}

	/**
	* @功能描述:任务数据转换公告任务对象，生成公告数据后更新任务状态
	* @params:[task, forUpdate]
	* @return:void
	* @Athor:ouyifan
	* @date:2022/8/4
	*/
//	@Transactional(rollbackFor = Exception.class)
	public void comnGenerateNotice(DisclosureProdTask task,Boolean forUpdate,String userid,String username) throws Exception {
		ScheduleNotice scheduleNotice = new ScheduleNotice();
		/**
		 * 获取任务基本信息，转换公告任务对象（公告标题、公告状态、公告基准日期、公告预计审批、发布时间等），返回公告任务对象用于执行公告生成
		 */
		scheduleNoticeService.setProperties(scheduleNotice,task);
		/**
		 * 公告数据处理，公告基本信息、公告版本信息、公告待发布渠道信息、公告取值信息
		 */
		logger.info(">>>>>>>>>>>>>>>>公告（任务id：" + task.getId() + "）数据生成替换start>>>>>>>>>>>>>>>>");
		scheduleNoticeService.comnGeneNotice(scheduleNotice, forUpdate,userid,username);
		logger.info(">>>>>>>>>>>>>>>>公告（任务id：" + task.getId() + "）数据生成替换end>>>>>>>>>>>>>>>>");
		//任务状态更新
		task.setStatus(TaskStatus.alreadyGenerate.getItemKey());
		task.setCreateStatus(scheduleNotice.getDisclosureStatus());
		t8DisclosureProdTaskDao.updateTaskStatus(task);
		t8DisclosureProdTaskDao.updateNoticeStatus(task);
		//更新任务表最新公告标题
		t8DisclosureProdTaskDao.updDisclosureTaskTitle(scheduleNotice);
		//入公告模板表格配置数据表
		t8DisclosureProdTaskDao.addIdbNoticeGridConfig(task,scheduleNotice);
		t8DisclosureProdTaskDao.genGridBaseData(scheduleNotice);
	}
	/**
	* @功能描述:定期报告判断子类型，生成对应时间段内任务数据
	* @params:[startDate, endDate, dataSource, disclosureSonType, disclosureType]
	* @return:java.util.List<com.kayak.pms.disclosureControl.model.DisclosureProdTask>
	* @Athor:ouyifan
	* @date:2022/6/20
	*/
	public List<DisclosureProdTask> queryTask(String startDate, String endDate, String taskStart,
			String disclosureSonType, String disclosureType,String forOnlyRule) throws Exception {
		// 获取产品信披规则(发起方式为时间规则发起的)
		ScheduleProdRule scheduleProdRule = new ScheduleProdRule();
		scheduleProdRule.setDisclosureType(disclosureType);// 信披类型 定期报告
		scheduleProdRule.setStartRule(taskStart);// 发起方式 1-自动发起（定时任务规定时间）、2-手动发起、0-业务变更日期(预留字段)->任务发起方式即数据来源
		scheduleProdRule.setDisclosureSonType(disclosureSonType);
		scheduleProdRule.setId(forOnlyRule);
		List<ScheduleProdRule> proRules = disclosureProdRuleDao.findProdRules(scheduleProdRule);
		List<DisclosureProdTask> tasks = new ArrayList<DisclosureProdTask>();
		try {
			proRules.forEach(prodRule -> {
				DisclosureProdTask prodTask = new DisclosureProdTask();
				String establishDate = prodRule.getEstablishDate();// 成立日
				String expireDate = prodRule.getEndDate();// 到期日
				if (Strings.isBlank(prodRule.getProdCode()) || Strings.isBlank(prodRule.getEstablishDate()) || Strings.isBlank(prodRule.getEndDate())) {
					throw new RuntimeException("产品信息为空，产品规则id为" + prodRule.getId());
				}
				String dataSource = querySource(taskStart);
				switch (prodRule.getBaseDate()) {
					case "06":// 每月最后一个自然日
						String lastMonthDate = endDate;
						try {
							tasks.add(this.copyEntityAndSetDate(prodTask, prodRule, lastMonthDate, dataSource));
						} catch (Exception e) {
							e.printStackTrace();
							throw new RuntimeException(e);
						}
						break;
					case "07":// 每季度最后一个自然日
						String maxDayOfQuart = DateUtil.getMaxOrMinDateOfQuarter(startDate, "max");// 所在季度最后一天
							try {/** 报告截至日-成立日+1>= 90个工作日 或 到期日-报告截止日+1 > 90个工作日 */
								// 季报 判断成立是否已经满90天 距离到期大于90天 才可以生成（受90自然日约束为“是”时，才需要验证，否则不需要）
								int establishDays = disclosureProdRuleDao.computeTwoWorkDays(establishDate, maxDayOfQuart);
								int expireDays = disclosureProdRuleDao.computeTwoWorkDays(maxDayOfQuart, expireDate);
								if (establishDays >= 90 && expireDays > 90) {
									tasks.add(this.copyEntityAndSetDate(prodTask, prodRule, maxDayOfQuart, dataSource));
								}
							} catch (Exception e) {
								e.printStackTrace();
								throw new RuntimeException(e);
							}
						// 根据下个月日期获取当前季度，并获取当前季度最后一日
						break;
					case "08":// 每上半年最后一个自然日 yyyy0630
						String halfYearDate = endDate.substring(0, 4) + "0630";
							try {/** 报告截至日-成立日+1>= 90个工作日 或 到期日-报告截止日+1 > 90个工作日 */
								int establishDays = disclosureProdRuleDao.computeTwoWorkDays(establishDate, halfYearDate);
								int expireDays = disclosureProdRuleDao.computeTwoWorkDays(halfYearDate, expireDate);
								if (establishDays >= 90 && expireDays > 90) {
									tasks.add(this.copyEntityAndSetDate(prodTask, prodRule, halfYearDate, dataSource));
								}
							} catch (Exception e) {
								e.printStackTrace();
								throw new RuntimeException(e);
							}
						break;
					case "09":// 每年最后1个自然日
						String yearLastDate = endDate.substring(0, 4) + "1231";
							try {/** 报告截至日-成立日+1>= 90个工作日 或 到期日-报告截止日+1 > 90个工作日 */
								int establishDays = disclosureProdRuleDao.computeTwoWorkDays(establishDate, yearLastDate);
								int expireDays = disclosureProdRuleDao.computeTwoWorkDays(yearLastDate, expireDate);
								if (establishDays >= 90 && expireDays > 90) {
									tasks.add(this.copyEntityAndSetDate(prodTask, prodRule, yearLastDate, dataSource));
								}
							} catch (Exception e) {
								e.printStackTrace();
								throw new RuntimeException(e);
							}
						break;
				}

			});
		}catch (Exception e){
			e.printStackTrace();
		}
		return tasks;
	}
/**
* @功能描述:获取数据来源
* @params:[taskStart]
* @return:java.lang.String
* @Athor:ouyifan
* @date:2022/6/30
*/
	private String querySource(String taskStart){
		String dataSource = "";
		if (taskStart.equals(TaskStart.manual.getItemKey())){//手动发起的任务
			dataSource = TaskDataSource.manual.getItemKey();//数据来源是手动发起
		}else if (taskStart.equals(TaskStart.auto.getItemKey())){//自动发起的任务
			dataSource = TaskDataSource.auto.getItemKey();//数据来源是自动发起
		}else {
			dataSource = taskStart;
		}
		return dataSource;
	}
	/**
	* @功能描述:整体报告生成任务
	* @params:[year, dataSource, disclosureSonType, disclosureType]
	* @return:java.util.List<com.kayak.pms.disclosureControl.model.DisclosureProdTask>
	* @Athor:ouyifan
	* @date:2022/6/21
	*/
	public List<DisclosureProdTask> queryTaskZT(String year,String taskStart,
			String disclosureSonType, String disclosureType,String forOnlyRule) throws Exception {
		// 获取产品信披规则(发起方式为时间规则发起的)
		ScheduleProdRule scheduleProdRule = new ScheduleProdRule();
		scheduleProdRule.setDisclosureType(disclosureType);// 信披类型 整体报告
		scheduleProdRule.setStartRule(taskStart);// 发起方式 1-自动发起（定时任务规定时间）、2-手动发起、0-业务变更日期(预留字段)->任务发起方式即数据来源
		scheduleProdRule.setDisclosureSonType(disclosureSonType);
		scheduleProdRule.setId(forOnlyRule);
		List<ScheduleProdRule> Rules = disclosureProdRuleDao.findProdRulesZT(scheduleProdRule);
		List<DisclosureProdTask> tasks = new ArrayList<DisclosureProdTask>();
		Rules.forEach(Rule -> {
			String dataSource = querySource(taskStart);
			DisclosureProdTask Task = new DisclosureProdTask();
			switch (Rule.getBaseDate()) {
			case "08":// 每上半年最后一个自然日
				String halfYearDate = year+ "0630";
					try {
						tasks.add(this.copyEntityAndSetDate(Task, Rule, halfYearDate, dataSource));
					} catch (Exception e) {
						e.printStackTrace();
						throw new RuntimeException(e);
					}
				break;
			case "09":// 每年最后1个自然日
				String yearLastDate = year + "1231";
					try {
						tasks.add(this.copyEntityAndSetDate(Task, Rule, yearLastDate, dataSource));
					} catch (Exception e) {
						e.printStackTrace();
						throw new RuntimeException(e);
					}
				break;
			}
		});
		return tasks;
	}
	/**
	* @功能描述:每周申购赎回任务数据生成
	* @params:[monday, dataSource, disclosureSonType, disclosureType] dataSource 数据来源 1自动生成 2手工生成
	* @return:java.util.List<com.kayak.pms.disclosureControl.model.DisclosureProdTask>
	* @Athor:ouyifan
	* @date:2022/6/20
	*/
	public List<DisclosureProdTask> queryTaskPeriod(String monday, String taskStart,
			String disclosureSonType, String disclosureType, String forOnlyRule) throws Exception {
		// 获取产品信披规则(发起方式为时间规则发起的)

		ScheduleProdRule scheduleProdRule = new ScheduleProdRule();
		scheduleProdRule.setDisclosureType(disclosureType);// 信披类型 申购赎回
		scheduleProdRule.setStartRule(taskStart);// 发起方式 1-自动发起（定时任务规定时间）、2-手动发起、0-业务变更日期(预留字段)->任务发起方式即数据来源
		scheduleProdRule.setDisclosureSonType(disclosureSonType);
		scheduleProdRule.setId(forOnlyRule);
		List<ScheduleProdRule> proRules = disclosureProdRuleDao.findProdRules(scheduleProdRule);
		List<DisclosureProdTask> tasks = new ArrayList<DisclosureProdTask>();
		proRules.forEach(prodRule -> {
			String dataSource = querySource(taskStart);
			boolean flag = false;
			DisclosureProdTask prodTask = new DisclosureProdTask();
			try {
				tasks.add(this.copyEntityAndSetDate(prodTask, prodRule, monday, dataSource));
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		});
		return tasks;
	}

	/**
	 * 功能：给任务对象属性复制，包括报告日期、基准日期、生成日期、生成时间、数据来源、预计生成日期
	 *
	 * @param prodTask
	 * @param prodRule
	 * @param baseDate
	 * @return
	 */
	public DisclosureProdTask copyEntityAndSetDate(DisclosureProdTask prodTask, ScheduleProdRule prodRule, String baseDate, String dataSource)
			throws Exception {
		BeanUtil.copyProperties(prodRule, prodTask, true);
		if (!prodRule.getDisclosureType().equals(DisclosureType.ensemble.getItemKey())
				&&!prodRule.getDisclosureSonType().equals(DisclosureSonType.netValueEntity.getItemKey())){
			prodTask.setT8DisclosureProdRuleId(prodTask.getId());// 信披规则id存入产品信披规则的id
			prodTask.setId("");
			prodTask.setT8DisclosureRuleId("");//非整体性质公告的任务不存储生成规则id
		}
		String now = DateUtil.getSysWordDay();
		prodTask.setCrtTaskDate(now);
		prodTask.setCrtDate(now);// 生成日期
		prodTask.setCrtTime(DateUtil.getNowTime());// 生成时间
		prodTask.setUpdDate(now);
		prodTask.setUpdTime(DateUtil.getNowTime());
		prodTask.setTaskMonth(baseDate.substring(0, 6));// 所属月份 存储形式为yyyyMM
		prodTask.setDataSource(dataSource);// 数据来源 1系统生成 2手工生成
		prodTask.setStatus(TaskStatus.forGenerate.getItemKey());// 状态 未生成

		String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));// 用户编号
		String username = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username"));// 姓名
		prodTask.setCrtUserId(userid);// 创建人id
		prodTask.setCrtUserName(username);// 创建人姓名
		prodTask.setUpdUserId(userid);
		prodTask.setUpdUserName(username);
		prodTask.setReportDate(baseDate);
		prodTask.setProdBaseDate(baseDate);
		String sysCrtDate="";
		if (!prodRule.getDisclosureType().equals(DisclosureType.net.getItemKey())){
			String rule = prodRule.getExpCreateRule();
			String days = prodRule.getExpCreateDays();
			prodTask.setProdBaseDate(baseDate);
			prodTask.setReportDate(baseDate);
			sysCrtDate= querySysCrtDate(rule,days,baseDate);
			prodTask.setSysCrtDate(sysCrtDate);// 计划生成日期
		}else {
			//净值公告也要配置日期加减
			String rule = prodRule.getExpCreateRule();
			String days = prodRule.getExpCreateDays();
			sysCrtDate= querySysCrtDate(rule,days,baseDate);
			prodTask.setSysCrtDate(sysCrtDate);
//			prodTask.setSysCrtDate(baseDate);// 计划生成日期
		}
		String title = prodRule.getNoticeTitle();// 未替换的公告标题
		String disclosureType = prodRule.getDisclosureType();// 信披类型
		String disclosureSonType = prodRule.getDisclosureSonType();// 信披子类型
		String prodCode = prodTask.getProdCode();// 产品代码
		prodTask.setNoticeTitle(queryTitle(title,baseDate,prodCode,disclosureType,disclosureSonType));// 公告标题
		return prodTask;
	}
	/**
	* @功能描述:生成日期公共方法
	* @params:[expCreateRule, expCreateDays, baseDate] 计划生成规则+/-,计划生成规则日期,基准日期
	* @return:java.lang.String
	* @Athor:ouyifan
	* @date:2022/7/14
	*/
	public String querySysCrtDate(String expCreateRule,String expCreateDays,String baseDate) throws Exception {
		String sysCrtDate ="";
		// 如果计划生成日期为空或者为0，生成日期都等于基准日期，否则，需要去库中查询
		if (StringUtils.isNotEmpty(expCreateDays)) {
			// 计划生成日期T+0 不需要查询，取基准日期
			if (Integer.parseInt(expCreateDays) == 0) {
				sysCrtDate = baseDate;
			} else {
				// 判断生成规则是+还是- 1代表- 2代表+
				Map<String, String> param = new HashMap<String, String>();
				param.put("pgmno", "001");
				param.put("days", expCreateDays);
				param.put("workday", baseDate);
				param.put("rule", expCreateRule);
				List<DisclosureWorkday> workdayList = disclosureWorkdayDao.findPlanCrtDate(param);
				if (workdayList.size() > 0) {
					sysCrtDate = workdayList.get(0).getWorkday();
				}else{//如果超出工作日方案的时间范围，则直接计算
					if (expCreateRule.equals("2")){
						sysCrtDate = DateUtil.add(baseDate, "yyyyMMdd", Integer.parseInt(expCreateDays));
					}else{
						sysCrtDate = DateUtil.add(baseDate, "yyyyMMdd", Integer.parseInt("-"+expCreateDays));
					}
				}
			}
		} else {
			sysCrtDate = baseDate;
		}
		return sysCrtDate;
	}
	/**
	* @功能描述:公告标题替换占位符公共方法
	* @params:[title, sysCrtDate, prodCode, disclosureType, disclosureSonType]
	* @return:java.lang.String
	* @Athor:ouyifan
	* @date:2022/8/4
	*/
	public String queryTitle(String title,String prodBaseDate,String prodCode, String disclosureType ,String disclosureSonType) throws Exception {
		if (StringUtils.isNotEmpty(title)) {// 公告标题
			Date cumDate = DateUtil.parseDate(prodBaseDate,"yyyyMMdd");
			String d = prodBaseDate.substring(6,8);
			String M = prodBaseDate.substring(4,6);
			String y = prodBaseDate.substring(0,4);
			if (Strings.isNotBlank(prodCode)){//没有产品不需要查询产品参数->整体报告都不对接产品
                Map<String, Object> prodInfo = prodInfo(prodCode,disclosureType,disclosureSonType);
                title = title.replace("{产品代码}", prodInfo.get("PROD_CD").toString());
                title = title.replace("{产品名称}", prodInfo.get("PROD_NM").toString());
				title = title.replace("{产品全称}", prodInfo.get("PROD_FULL_NAME").toString());
                title = title.replace("{产品品牌}", prodInfo.get("PROD_BRND").toString());
                //获取字典值
                String prodMod = sysDictItemDao.findDictValueByKey(prodInfo.get("PROD_MOD").toString(), "prod_mod");
                String prodCur = sysDictItemDao.findDictValueByKey(prodInfo.get("PROD_CCY").toString(), "cur_type");
                title = title.replace("{产品模式}", prodMod==null ? prodInfo.get("PROD_MOD").toString() :prodMod);
                title = title.replace("{募集币种}", prodCur==null ? prodInfo.get("PROD_CCY").toString():prodCur);
			}
			title = title.replace("{yyyy}", y);
			title = title.replace("{MM}", M);
			title = title.replace("{dd}", d);
			int month = Integer.parseInt(M);
			int monthDate = Integer.parseInt(M+d);
			if (disclosureType.equals(DisclosureType.regular.getItemKey())) {// 定期报告
				if (disclosureSonType.equals(DisclosureSonType.quarter.getItemKey())) {// 季报
					title = title.replace("{U}", DateUtil.getQuarterNumber(prodBaseDate, 1));// 将公告中的标题含有季度的替换为一二三四季度
				} else if (disclosureSonType.equals(DisclosureSonType.seAnnual.getItemKey())) {// 半年报
					if (month > 6) {
						title = title.replace("{U}", "下");
					} else {
						title = title.replace("{U}", "上");
					}
				} else if (disclosureSonType.equals(DisclosureSonType.month.getItemKey())) {// 月报
					title = title.replace("{U}", M);
				}
			} else if (disclosureType.equals(DisclosureType.purchase.getItemKey())) {// 申购赎回报告
				Calendar calendar = Calendar.getInstance();
				calendar.setFirstDayOfWeek(Calendar.MONDAY);
				Calendar cal = Calendar.getInstance();
				cal.setTime(cumDate);
				//判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周了
				int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
				//获得当前日期是一个星期的第几天
				if (1 == dayWeek) {
					cal.add(Calendar.DAY_OF_MONTH, -1);
				}
				//设置一个星期的第一天
				cal.setFirstDayOfWeek(Calendar.MONDAY);
				//获得当前日期是一个星期的第几天
				int day = cal.get(Calendar.DAY_OF_WEEK);
				//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
				cal.add(Calendar.DATE, (cal.getFirstDayOfWeek() - day -7));
				String dateMonday = DateUtil.dateFormate(cal.getTime(),"yyyyMMdd");
				cal.add(Calendar.DATE, 6);
				String dateSunday = DateUtil.dateFormate(cal.getTime(),"yyyyMMdd");
				//替换公告标题上周日日期
				String dF = dateSunday.substring(6,8);//{ddF}
				String MF = dateSunday.substring(4,6);//{MMF}
				String yF = dateSunday.substring(0,4);//{yyyyF}
				title = title.replace("{yyyyF}", yF);
				title = title.replace("{MMF}", MF);
				title = title.replace("{ddF}", dF);
				//替换公告标题上周一日期
				String dM = dateMonday.substring(6,8);//{ddM}
				String MM = dateMonday.substring(4,6);//{MMM}
				String yM = dateMonday.substring(0,4);//{yyyyM}
				title = title.replace("{yyyyM}", yM);
				title = title.replace("{MMM}", MM);
				title = title.replace("{ddM}", dM);
			}
			return title;
		}
		return  title;
	}

	public Map<String,Object> prodInfo(String prodCode, String disclosureType ,String disclosureSonType) throws Exception {
		List<SqlRow> prodInfo = disclosureProdRuleDao.ProdParamsByCode(prodCode,disclosureType,disclosureSonType);
		Map<String,Object> map = new HashMap<>();
		map.put("PROD_MOD",prodInfo.get(0).get("PROD_MOD") != null ? prodInfo.get(0).get("PROD_MOD").toString() : ""); //模式
		map.put("PROD_NM",prodInfo.get(0).get("PROD_NM") != null ? prodInfo.get(0).get("PROD_NM").toString() : "");//名称
		map.put("PROD_FULL_NAME",prodInfo.get(0).get("PROD_FULL_NAME") != null ? prodInfo.get(0).get("PROD_FULL_NAME").toString() : "");//产品全名称
		map.put("PROD_CCY",prodInfo.get(0).get("PROD_CCY") != null ? prodInfo.get(0).get("PROD_CCY").toString() : "");//币种
		map.put("PROD_CD",prodInfo.get(0).get("PROD_CD") != null ? prodInfo.get(0).get("PROD_CD").toString() : "");//代码
		map.put("PROD_BRND",prodInfo.get(0).get("PROD_BRND") != null ? prodInfo.get(0).get("PROD_BRND").toString() : "");//品牌
		return map;
	}

	/**
	 * 功能：自动跑批插入定期报告信披任务 先删除所属月份的再插入 定期报告的定时 生成基准日期落在当月的数据 作者：rennannan 日期：20210607
	 *
	 * @param
	 * @param
	 * @throws Exception
	 */
	@API(desc = "生成定期报告信披任务", auth = APIAuth.NO, operation = APIOperation.UPDATE)
	public String autoAddDisclosureTasks(SqlParam<DisclosureProdTask> params) throws Exception {

		try {
			String forOnlyRule = params.getModel().getT8DisclosureProdRuleId()==null?"":params.getModel().getT8DisclosureProdRuleId();
			// 获取当前日期
			String nowDate = DateUtil.getSysWordDay();
			/**自动生成报告前先删没有生成公告的任务*/
			if (Strings.isBlank(forOnlyRule)){
				DisclosureProdTask dis = new DisclosureProdTask();
				dis.setStartDate(nowDate);
				dis.setDisclosureType(DisclosureType.regular.getItemKey());
				dis.setProdBaseDate(params.getModel().getProdBaseDate());
				disclosureProdTaskDao.deleteBeforeAdd(dis);
			}
			// 获取本月第一天与最后一天
			String nextMonthFirstDay = DateUtil.getFirstDayDateOfMonth(nowDate);
			String nextMonthEndDate = DateUtil.getLastDayOfMonth(nowDate);
			ScheduleProdRule prodRule = new ScheduleProdRule();
			prodRule.setDisclosureType(DisclosureType.regular.getItemKey());
			prodRule.setDisclosureSonType("");
			List<ScheduleProdRule> prodRuleList = disclosureProdRuleDao.findRuleExistsByType(prodRule);

			if (prodRuleList.size() == 0) {
				logger.info("未配置或启用定期报告的信披规则");
				return RequestSupport.updateReturnJson(false, "未配置或启用定期报告的信披规则：", null).toString();
			}else{
				List<DisclosureProdTask> tasks = queryTask(nextMonthFirstDay, nextMonthEndDate, TaskStart.auto.getItemKey(), "",DisclosureType.regular.getItemKey(),forOnlyRule);
				// 添加到任务表
				addProdTask(tasks,forOnlyRule);
				logger.info("定期报告任务生成完成");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false, "生成任务失败："+e.getMessage(), null).toString();
		}
		return RequestSupport.updateReturnJson(true, "", null).toString();
	}
	/**
	 * 功能：自动跑批插入定期报告信披任务 先删除所属月份的再插入 定期报告的定时 生成基准日期落在当月的数据 作者：rennannan 日期：20210607
	 *
	 * @param
	 * @param
	 * @throws Exception
	 */
	@API(desc = "手动修改产品信披规则自动更新任务", auth = APIAuth.NO, operation = APIOperation.UPDATE)
	public void autoUpdateDisclosureTasks(Map<String, Object> params) throws Exception {
		//获取当前类型
		String disclosureType =params.get("disclosureType").toString();
		String disclosureSonType =params.get("disclosureSonType").toString();
		//转换对应泛型
		SqlParam<DisclosureProdTask> taskParams = new FetcherData<>(params,DisclosureProdTask.class);
		if (DisclosureType.purchase.getItemKey().equals(disclosureType)){
			autoAddDisclosurePeriodTasks(taskParams);
		}
		if (DisclosureType.issEst.getItemKey().equals(disclosureType)){
			autoGenerateIssueTask(taskParams);
		}
		if (DisclosureType.regular.getItemKey().equals(disclosureType)){
			autoAddDisclosureTasks(taskParams);
		}
		if (DisclosureType.preSale.getItemKey().equals(disclosureType)){
			autoGenerateBeforeSaleTask(taskParams);
		}
		if (DisclosureType.expire.getItemKey().equals(disclosureType)){
			autoGenerateEndTask(taskParams);
		}
		//整体公告应该通过修改信披规则可以实时修改未完成的任务,其他类型报告均可通过产品信披规则
		if (DisclosureType.ensemble.getItemKey().equals(disclosureType)){
			autoAddDisclosureZTTasks(taskParams);
		}
		if (DisclosureType.net.getItemKey().equals(disclosureType)){
			autoUpdNetTask(taskParams);
		}
		if (DisclosureType.sale.getItemKey().equals(disclosureType)){
			autoGenerateSaleTask(taskParams);
		}
		if (DisclosureType.bonus.getItemKey().equals(disclosureType)){
			autoGenerateBonusTask(taskParams);
		}
	}
	/**
	 * 功能：自动跑批插入申购赎回信披任务
	 *
	 * @param
	 * @param
	 * @throws Exception
	 */
	@API(desc = "生成申购赎回报告信披任务", auth = APIAuth.NO, operation = APIOperation.UPDATE)
	public String autoAddDisclosurePeriodTasks(SqlParam<DisclosureProdTask> params) throws Exception {
		try {
			//存在forOnlyRule则执行因规则修改而更新任务
			String forOnlyRule = params.getModel().getT8DisclosureProdRuleId()==null?"":params.getModel().getT8DisclosureProdRuleId();
			// 获取当前日期
			String nowDate = DateUtil.getSysWordDay();
			if (Strings.isBlank(forOnlyRule)){
				DisclosureProdTask dis = new DisclosureProdTask();
				dis.setStartDate(nowDate);
				dis.setDisclosureType(DisclosureType.purchase.getItemKey());
				dis.setProdBaseDate(params.getModel().getProdBaseDate());
				disclosureProdTaskDao.deleteBeforeAdd(dis);
			}

			ScheduleProdRule prodRule = new ScheduleProdRule();
			prodRule.setDisclosureType(DisclosureType.purchase.getItemKey());
			prodRule.setDisclosureSonType("");
			List<ScheduleProdRule> prodRuleList = disclosureProdRuleDao.findRuleExistsByType(prodRule);
			if (prodRuleList.size() == 0) {
				logger.info("未配置或启用申购赎回报告的信披规则");
				return RequestSupport.updateReturnJson(false, "未配置或启用申购赎回报告的信披规则", null).toString();
			}else{
				String prodBaseDate=params.getModel().getProdBaseDate();
				//获取下周一的日期进行任务生成
				Date date = DateUtil.parseDate(prodBaseDate,"yyyyMMdd");
				Calendar cr = new GregorianCalendar();
				cr.setTime(date);
				cr.add(Calendar.DATE,7);//如当天为周一顺延到下周周一的日期
				// 转换时间为String
				String nextWeekDay = DateUtil.dateFormate(cr.getTime(),"yyyyMMdd");
				// 获取nextWeekDay 的周一日期
				String nWeekDate =DateUtil.getThisWeekMonday(nextWeekDay);
				//String nDate =DateUtil.getThisWeekMonday(nowDate);
				//如果周一不是工作日需要找最近的工作日
				List<DisclosureWorkday> disclosureWorkdays = scheduleNoticeService.WorkdayCheck(nWeekDate, 1);
				if(null !=disclosureWorkdays && disclosureWorkdays.size()>0){
					nWeekDate = disclosureWorkdays.get(0).getWorkday();
				}
//				if (nDate.equals(nowDate)){//相等则说明当天为本周周一,则周一还需要生成上周还没生成披露公告的任务，上周该生成任务的报告基准日是本周周一
//					List<DisclosureProdTask> tasks = queryTaskPeriod(nDate, TaskStart.auto.getItemKey(), "",DisclosureType.purchase.getItemKey(),forOnlyRule);
//					addProdTask(tasks,forOnlyRule);
//					logger.info("上周申购赎回报告任务生成完成");
//				}
				//本周生成的任务，报告基准日期为下周周一
				List<DisclosureProdTask> tasks = queryTaskPeriod(nWeekDate, TaskStart.auto.getItemKey(), "",DisclosureType.purchase.getItemKey(),forOnlyRule);
				addProdTask(tasks,forOnlyRule);
				logger.info("申购赎回报告任务生成完成");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false, "生成任务失败："+e.getMessage(), null).toString();
		}
		return RequestSupport.updateReturnJson(true, "", null).toString();
	}
	/**
	 * 功能：自动跑批插入整体公告信披任务
	 *
	 * @param
	 * @param
	 * @throws Exception
	 * @return
	 */
	@API(desc = "生成整体公告信披任务", auth = APIAuth.NO, operation = APIOperation.UPDATE)
	public String autoAddDisclosureZTTasks(SqlParam<DisclosureProdTask> params) throws Exception {

		try {
			String forOnlyRule = params.getModel().getT8DisclosureRuleId()==null?"":params.getModel().getT8DisclosureRuleId();
			// 获取当前日期
			String nowDate = DateUtil.getSysWordDay();
			//存在forOnlyRule则执行因规则修改而更新任务
			if (Strings.isBlank(forOnlyRule)) {
				DisclosureProdTask dis = new DisclosureProdTask();
				dis.setStartDate(nowDate);
				dis.setDisclosureType(DisclosureType.ensemble.getItemKey());
				dis.setProdBaseDate(params.getModel().getProdBaseDate());
				disclosureProdTaskDao.deleteBeforeAdd(dis);
			}
			ScheduleProdRule prodRule = new ScheduleProdRule();
			prodRule.setDisclosureType(DisclosureType.ensemble.getItemKey());
			prodRule.setDisclosureSonType("");
			//查询信披生成规则，且状态为启用
			List<ScheduleProdRule> RuleList = disclosureProdRuleDao.findRuleExists(prodRule);

			if (RuleList.size() == 0) {
				logger.info("未配置或启用整体公告的信披规则");
				return RequestSupport.updateReturnJson(false, "未配置或启用整体公告的信披规则", null).toString();
			}else{
				String baseDate = nowDate.substring(0,4);// 开始年份 yyyy
				DaoUtil.doTrans(() -> {
					List<DisclosureProdTask> tasks = queryTaskZT(baseDate, TaskStart.auto.getItemKey(), "",DisclosureType.ensemble.getItemKey(),forOnlyRule);
					addProdTask(tasks,forOnlyRule);
					logger.info("整体公告任务生成完成");
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false, "任务生成失败："+e.getMessage(), null).toString();
		}
		return RequestSupport.updateReturnJson(true, "", null).toString();
	}

	private void addProdTask(List<DisclosureProdTask> tasks,String forOnlyRule) throws Exception {
		String date = DateUtil. getNowDate();
		if (tasks.size() > 0) {
			for (DisclosureProdTask task:tasks) {
				task.setCrtDate(date);
				//判断是否指定某一个规则的任务进行更新
				addForIsOnlyOrNot(forOnlyRule, task);
			}
		}
	}
	@API(desc = "自动生成发行公告任务", auth = APIAuth.NO)
	public String autoGenerateIssueTask(SqlParam<DisclosureProdTask> params) throws Exception {
		try {
			//存在forOnlyRule则执行因规则修改而更新任务
			String forOnlyRule = params.getModel().getT8DisclosureProdRuleId()==null?"":params.getModel().getT8DisclosureProdRuleId();
			// 获取当前日期
			String nowDate = DateUtil.getSysWordDay();
			/**自动生成报告前先删没有生成公告的任务*/
			if (Strings.isBlank(forOnlyRule)) {
				DisclosureProdTask dis = new DisclosureProdTask();
				dis.setStartDate(nowDate);
				dis.setDisclosureType(DisclosureType.issEst.getItemKey());
				dis.setProdBaseDate(params.getModel().getProdBaseDate());
				disclosureProdTaskDao.deleteBeforeAdd(dis);
			}
			ScheduleProdRule prodRule = new ScheduleProdRule();
			prodRule.setDisclosureType(DisclosureType.issEst.getItemKey());
			prodRule.setDisclosureSonType("");

			//TODO 校验通过直接传参
			List<ScheduleProdRule> prodRuleList = disclosureProdRuleDao.findRuleExistsByType(prodRule);
			if (prodRuleList.size() == 0) {
				logger.info("未配置或启用发行成立公告的信披规则");
				return RequestSupport.updateReturnJson(false, "未配置或启用发行成立公告的信披规则", null).toString();
			}
			//自动发行成立日期不指定，不传值
			issueReportComnMethod("", TaskStart.auto.getItemKey(),forOnlyRule);
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false, "任务生成失败"+e.getMessage(), null).toString();
		}
		return RequestSupport.updateReturnJson(true, "", null).toString();
	}

	@API(desc = "自动生成到期公告任务", auth = APIAuth.NO)
	public String autoGenerateEndTask(SqlParam<DisclosureProdTask> params) throws Exception {
		try {
			//存在forOnlyRule则执行因规则修改而更新任务
			String forOnlyRule = params.getModel().getT8DisclosureProdRuleId()==null?"":params.getModel().getT8DisclosureProdRuleId();
			// 获取当前日期
			String nowDate = DateUtil.getSysWordDay();
			/**自动生成报告前先删没有生成公告的任务*/
			if (Strings.isBlank(forOnlyRule)) {
				DisclosureProdTask dis = new DisclosureProdTask();
				dis.setStartDate(nowDate);
				dis.setDisclosureType(DisclosureType.expire.getItemKey());
				dis.setProdBaseDate(params.getModel().getProdBaseDate());
				disclosureProdTaskDao.deleteBeforeAdd(dis);
			}
			ScheduleProdRule prodRule = new ScheduleProdRule();
			prodRule.setDisclosureType(DisclosureType.expire.getItemKey());
			prodRule.setDisclosureSonType("");
			List<ScheduleProdRule> prodRuleList = disclosureProdRuleDao.findRuleExistsByType(prodRule);

			if (prodRuleList.size() == 0) {
				logger.info("未配置或启用到期公告的信披规则");
				return RequestSupport.updateReturnJson(false, "未配置或启用到期公告的信披规则", null).toString();
			}else{
				//自动到期日期不指定，不传值
				endReportComnMethod("", TaskStart.auto.getItemKey(), forOnlyRule);
				logger.info("到期公告任务生成完成");

			}
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false, "任务生成失败："+e.getMessage(), null).toString();
		}
		return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
	}

	@API(desc = "自动生成销售文档任务", auth = APIAuth.NO)
	public String autoGenerateSaleTask(SqlParam<DisclosureProdTask> params) throws Exception {

		try {
			//存在forOnlyRule则执行因规则修改而更新任务
			String forOnlyRule = params.getModel().getT8DisclosureProdRuleId()==null?"":params.getModel().getT8DisclosureProdRuleId();
			// 获取当前日期
			String nowDate = DateUtil.getSysWordDay();
			/**自动生成报告前先删没有生成公告的任务*/
			if (Strings.isBlank(forOnlyRule)) {
				DisclosureProdTask dis = new DisclosureProdTask();
				dis.setStartDate(nowDate);
				dis.setDisclosureType(DisclosureType.sale.getItemKey());
				dis.setProdBaseDate(params.getModel().getProdBaseDate());
				disclosureProdTaskDao.deleteBeforeAdd(dis);
			}

			ScheduleProdRule prodRule = new ScheduleProdRule();
			prodRule.setDisclosureType(DisclosureType.sale.getItemKey());
			prodRule.setDisclosureSonType("");
			List<ScheduleProdRule> prodRuleList = disclosureProdRuleDao.findRuleExistsByType(prodRule);

			if (prodRuleList.size() == 0) {
				logger.info("未配置或启用销售文档的信披规则");
				return RequestSupport.updateReturnJson(false, "未配置或启用销售文档的信披规则", null).toString();
			}else{
				saleDocReportComnMethod("", TaskStart.auto.getItemKey(), forOnlyRule);
				logger.info("销售文档任务生成完成");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false, "生成任务失败："+e.getMessage(), null).toString();
		}
		return RequestSupport.updateReturnJson(true, "", null).toString();
	}

	@API(desc = "自动生成净值公告任务", auth = APIAuth.NO)
	public String autoGenerateNetTask(SqlParam<DisclosureProdTask> params) throws Exception {


		try {
			//存在forOnlyRule则执行因规则修改而更新任务
			String forOnlyRule = params.getModel().getT8DisclosureProdRuleId()==null?"":params.getModel().getT8DisclosureProdRuleId();
			// 获取当前日期
			String nowDate = DateUtil.getSysWordDay();
			if (Strings.isBlank(forOnlyRule)) {
				DisclosureProdTask dis = new DisclosureProdTask();
				dis.setStartDate(nowDate);
				dis.setDisclosureType(DisclosureType.net.getItemKey());
				dis.setProdBaseDate(params.getModel().getProdBaseDate());
				disclosureProdTaskDao.deleteBeforeAdd(dis);
			}

			String prodBaseDate=params.getModel().getProdBaseDate();
			//String nowDate = DateUtil.getSysWordDay();
			ScheduleProdRule prodRule = new ScheduleProdRule();
			prodRule.setDisclosureType(DisclosureType.net.getItemKey());
			prodRule.setDisclosureSonType("");
			//TODO 按照净值公告子类型校验信披规则和产品规则
			List<ScheduleProdRule> prodRuleList = disclosureProdRuleDao.findRuleExistsByNet(prodRule);
			if (prodRuleList.size() == 0) {
				logger.info("未配置或启用净值公告的信披规则或产品信披规则");
				return RequestSupport.updateReturnJson(false, "未配置或启用净值公告的信披规则或产品信披规则", null).toString();
			}else{
				DaoUtil.doTrans(() -> {
					List<DisclosureProdTask> tasks ;
					//自动生成任务时，逐个执行每种子类型
					tasks =netDocReportComnMethod(prodBaseDate, TaskStart.auto.getItemKey(), "",DisclosureSonType.netValueEntity.getItemKey(),DisclosureType.net.getItemKey());
					tasks.addAll(netDocReportComnMethod(prodBaseDate, TaskStart.auto.getItemKey(),"","",DisclosureType.net.getItemKey())) ;
					addProdTask(tasks,"");
					logger.info("净值公告任务生成完成");
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false, "生成任务失败："+e.getMessage(), null).toString();
		}
		return RequestSupport.updateReturnJson(true, "", null).toString();
	}
	@API(desc = "修改净值公告整体报告时及时更新任务", auth = APIAuth.NO)
	public void autoUpdNetTask(SqlParam<DisclosureProdTask> params) throws Exception {
		ScheduleProdRule prodRule = new ScheduleProdRule();
		prodRule.setDisclosureType(DisclosureType.net.getItemKey());
		prodRule.setDisclosureSonType("");
		//TODO 按照净值公告子类型校验信披规则和产品规则
		List<ScheduleProdRule> prodRuleList = disclosureProdRuleDao.findRuleExistsByNet(prodRule);
		if (prodRuleList.size()<=0)
			return;
		//TODO 修改某一条规则时，查询任务表里这一条净值公告规则选择过的日期作为日期条件再次执行任务生成，有则更新，没有不执行新增
		prodRule.setProdForm(params.getModel().getProdForm());
		prodRule.setProdObj(params.getModel().getProdObj());
		prodRule.setProdClcMth(params.getModel().getProdClcMth());
		prodRule.setInvPrdDime(params.getModel().getInvPrdDime());
		prodRule.setInvPrdLen(params.getModel().getInvPrdLen());
		prodRule.setProdInvTyp(params.getModel().getProdInvTyp());
		prodRule.setProdSerCd(params.getModel().getProdSerCd());
		List<SqlRow> dateList = disclosureProdRuleDao.findExistsNetDate(prodRule);
		if (dateList.size()<=0)
			return;
		for (SqlRow dateObj :dateList) {
			DaoUtil.doTrans(() -> {
				List<DisclosureProdTask> tasks;
				if (params.getModel().getDisclosureSonType().equals(DisclosureSonType.netValueEntity.getItemKey())) {
					String forOnlyRule = params.getModel().getT8DisclosureRuleId()==null?"":params.getModel().getT8DisclosureRuleId();
					//手动修改生成规则时自动更新任务，整体公告传整体子类型
					tasks = netDocReportComnMethod(dateObj.getString("prod_base_date"), TaskStart.auto.getItemKey(), forOnlyRule, DisclosureSonType.netValueEntity.getItemKey(), DisclosureType.net.getItemKey());
					addProdTask(tasks, forOnlyRule);
				} else {
					String forOnlyRule = params.getModel().getT8DisclosureProdRuleId()==null?"":params.getModel().getT8DisclosureProdRuleId();
					//手动修改产品信披规则时自动更新任务，非整体公告不传子类型
					tasks = netDocReportComnMethod(dateObj.getString("prod_base_date"), TaskStart.auto.getItemKey(), forOnlyRule, "", DisclosureType.net.getItemKey());
					addProdTask(tasks, forOnlyRule);
				}
				logger.info("净值公告任务因规则变更自动更新完成");
			});
		}
	}

	@API(desc = "自动生成售前登记公告任务", auth = APIAuth.NO)
	public String autoGenerateBeforeSaleTask(SqlParam<DisclosureProdTask> params) throws Exception {
		try {
			//存在forOnlyRule则执行因规则修改而更新任务
			String forOnlyRule = params.getModel().getT8DisclosureProdRuleId()==null?"":params.getModel().getT8DisclosureProdRuleId();
			// 获取当前日期
			String nowDate = DateUtil.getSysWordDay();
			/**自动生成报告前先删没有生成公告的任务*/
			if (Strings.isBlank(forOnlyRule)) {
				DisclosureProdTask dis = new DisclosureProdTask();
				dis.setStartDate(nowDate);
				dis.setDisclosureType(DisclosureType.preSale.getItemKey());
				dis.setProdBaseDate(params.getModel().getProdBaseDate());
				disclosureProdTaskDao.deleteBeforeAdd(dis);
			}
			ScheduleProdRule prodRule = new ScheduleProdRule();
			prodRule.setDisclosureType(DisclosureType.preSale.getItemKey());
			prodRule.setDisclosureSonType("");
			List<ScheduleProdRule> prodRuleList = disclosureProdRuleDao.findRuleExistsByTypeBefSale(prodRule);

			if (prodRuleList.size() == 0) {
				logger.info("未配置或启用售前登记公告的信披规则");
				return RequestSupport.updateReturnJson(false, "未配置或启用售前登记公告的信披规则", null).toString();
			}else{
				for (ScheduleProdRule pRule:prodRuleList) {
					beforeSaleDocReportComnMethod("", TaskStart.auto.getItemKey(), forOnlyRule,pRule.getDisclosureSonType());
				}
				logger.info("售前登记公告任务生成完成");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false, "生成任务失败："+e.getMessage(), null).toString();
		}
		return RequestSupport.updateReturnJson(true, "", null).toString();
	}

	@API(desc = "自动生成分红公告任务", auth = APIAuth.NO)
	public String autoGenerateBonusTask(SqlParam<DisclosureProdTask> params) throws Exception {
		try {
			//存在forOnlyRule则执行因规则修改而更新任务
			String forOnlyRule = params.getModel().getT8DisclosureProdRuleId()==null?"":params.getModel().getT8DisclosureProdRuleId();
			//任务处理日期
			String nowDate = DateUtil.getSysWordDay();
			/**自动生成报告前先删没有生成公告的任务*/
			if (Strings.isBlank(forOnlyRule)) {
				DisclosureProdTask dis = new DisclosureProdTask();
				dis.setStartDate(nowDate);
				dis.setProdBaseDate(params.getModel().getProdBaseDate());
				dis.setDisclosureType(DisclosureType.bonus.getItemKey());
				dis.setProdBaseDate(params.getModel().getProdBaseDate());
				disclosureProdTaskDao.deleteBeforeAdd(dis);
			}

			ScheduleProdRule prodRule = new ScheduleProdRule();
			prodRule.setDisclosureType(DisclosureType.bonus.getItemKey());
			prodRule.setDisclosureSonType("");
			prodRule.setBaseDate(params.getModel().getProdBaseDate());
			//TODO 按照分红公告类型校验信披规则和产品规则
			List<ScheduleProdRule> prodRuleList = disclosureProdRuleDao.findRuleExistsByBonusIssueDate(prodRule);
			if (prodRuleList.size() == 0) {
				logger.info("未配置或启用分红公告的信披规则或产品信披规则");
				return RequestSupport.updateReturnJson(false, "未配置或启用分红公告的信披规则或产品信披规则", null).toString();
			}
			issueReportBonusMethod(params.getModel().getProdBaseDate(), TaskStart.auto.getItemKey(),forOnlyRule);
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false, "任务生成失败"+e.getMessage(), null).toString();
		}
		return RequestSupport.updateReturnJson(true, "", null).toString();
	}

	/**
	 * 功能：生成发行公告任务公共方法 作者：rennannan 日期：20211028
	 *
	 * @param establishDate 发行成立日
	 * @throws Exception
	 */
	public void issueReportComnMethod(String establishDate, String taskStart,String forOnlyRule) throws Exception {
		// 查询信披类型为发行公告的信披规则
		Map<String, Object> params = new HashMap<>();
		params.put("taskStart",taskStart);
		params.put("establishDate",establishDate);
		params.put("id",forOnlyRule);
		List<ScheduleProdRule> prodRuleList = disclosureProdRuleDao.findProdByTypeAndRule(params);
		if (CollectionUtil.isNotEmpty(prodRuleList)) {
			DaoUtil.doTrans(() -> {
				String dataSource = querySource(taskStart);
				for (ScheduleProdRule prodRule : prodRuleList) {
					String baseDate = "";
					if ("".equals(establishDate)||establishDate==null){
						baseDate = prodRule.getEstablishDate();
					}else{
						baseDate = establishDate;
					}
					insertReportInfo(baseDate,prodRule, dataSource,forOnlyRule);
				}
			});
		}
	}
	/**
	 * 功能：生成销售文档任务公共方法 作者：ouyifan 日期：20220609
	 *
	 * @param applyStartDate 产品发布日
	 * @throws Exception
	 */
	public void saleDocReportComnMethod(String applyStartDate, String taskStart,String forOnlyRule) throws Exception {
		// 查询信披类型为销售文档的信披规则
		Map<String, Object> params = new HashMap<>();
		params.put("taskStart",taskStart);
		params.put("applyStartDate",applyStartDate);
		params.put("id",forOnlyRule);
		List<ScheduleProdRule> prodRuleList = disclosureProdRuleDao.findProdByTypeAndRuleSale(params);
		if (CollectionUtil.isNotEmpty(prodRuleList)) {
			DaoUtil.doTrans(() -> {
				String dataSource = querySource(taskStart);
				for (ScheduleProdRule prodRule : prodRuleList) {
					String baseDate = "";
					if ("".equals(applyStartDate)||applyStartDate==null){
						baseDate = prodRule.getStartRaise();
					}else{
						baseDate = applyStartDate;
					}
					insertReportInfo(baseDate,prodRule, dataSource,forOnlyRule);
				}
			});
		}
	}
	/**
	 * 功能：生成售前信息登记任务公共方法 作者：ouyifan 日期：20220609
	 *
	 * @param applyStartDate 募集起始日
	 * @throws Exception
	 */
	public void beforeSaleDocReportComnMethod(String applyStartDate, String taskStart,String forOnlyRule,String disclosureSonType) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("taskStart",taskStart);
		params.put("applyStartDate",applyStartDate);
		params.put("disclosureSonType",disclosureSonType);
		params.put("id",forOnlyRule);
		List<ScheduleProdRule> prodRuleL = disclosureProdRuleDao.findProdByTypeAndRuleBeforeSale(params);
		if (CollectionUtil.isNotEmpty(prodRuleL)) {
			DaoUtil.doTrans(() -> {
				String dataSource = querySource(taskStart);
				for (ScheduleProdRule prodRule : prodRuleL) {
					String baseDate = "";
					if ("".equals(applyStartDate)||applyStartDate==null){
						baseDate = prodRule.getStartRaise();
					}else{
						baseDate = applyStartDate;
					}
					insertReportInfo(baseDate,prodRule, dataSource,forOnlyRule);
				}
			});
		}
	}
	/**
	 * 功能：生成净值公告任务公共方法 作者：ouyifan 日期：20220609
	 *
	 * @param netDate 净值基准日
	 * @throws Exception
	 */
	public List<DisclosureProdTask> netDocReportComnMethod(String netDate, String taskStart,String forOnlyRule,
	String disclosureSonType, String disclosureType) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("taskStart",taskStart);
		params.put("disclosureSonType",disclosureSonType);
		params.put("disclosureType",disclosureType);
		params.put("id",forOnlyRule);
		params.put("netDate",netDate);//基准日期
		List<ScheduleProdRule> RuleList;
		List<DisclosureProdTask> tasks = new ArrayList<DisclosureProdTask>();
		if (DisclosureSonType.netValueEntity.getItemKey().equals(disclosureSonType)){//净值整体公告
			ScheduleProdRule scheduleProdRule = new ScheduleProdRule();
			scheduleProdRule.setDisclosureType(disclosureType);// 信披类型 整体报告
			scheduleProdRule.setStartRule(taskStart);// 发起方式 1-自动发起（定时任务规定时间）、2-手动发起、0-业务变更日期(预留字段)->任务发起方式即数据来源
			scheduleProdRule.setDisclosureSonType(disclosureSonType);
			scheduleProdRule.setId(forOnlyRule);
			RuleList = disclosureProdRuleDao.findProdRulesZT(scheduleProdRule);
		}else {
			//查询所有信披类型为净值公告的产品信披规则
			RuleList = disclosureProdRuleDao.findProdByTypeAndRuleNet(params);
		}
		if (CollectionUtil.isNotEmpty(RuleList)) {
			DaoUtil.doTrans(() -> {
				String dataSource = querySource(taskStart);
				for (ScheduleProdRule prodRule : RuleList) {
					DisclosureProdTask Task = new DisclosureProdTask();
					if (!prodRule.getDisclosureSonType().equals(DisclosureSonType.netValueEntity.getItemKey())){
						params.put("prodCode",prodRule.getProdCode());
					}
					String[] baseList = new String[0];
					if(StringUtils.isNotBlank(prodRule.getNetValueDate())){
						 baseList= prodRule.getNetValueDate().split(",");
					}
					//是否已添加该任务
					boolean isAdd =false;
					for (String baseDate:baseList){
						switch (baseDate){
							case "1":// 净值发布日（且不为到期、确认日）
								try {
									if (!isAdd){//同一规则下的净值公告的任务，针对不同基准日，有且仅有一条
										if (DisclosureSonType.netValueEntity.getItemKey().equals(prodRule.getDisclosureSonType())){
											tasks.add(this.copyEntityAndSetDate(Task, prodRule, netDate, dataSource));//传入基准日期netDate
											isAdd=true;
										}else{
											if(params.get("prodCode")==null){
												continue;
											}
											List<SqlRow> netValueList =disclosureProdRuleDao.findNetValueForIsuDate(params);
											//以选择的日期查询当天是否有该产品的净值需要推送,不为到期、确认日，当天还有需要披露的数据则生成任务;
											if (netValueList.size()>0&&!netValueList.get(0).getString("nav_dt").equals(prodRule.getEndDate())){
												tasks.add(this.copyEntityAndSetDate(Task, prodRule, netDate, dataSource));//传入基准日期netDate
												isAdd=true;
											}
										}
									}
								} catch (Exception e) {
									throw new Exception(e);

								}
								break;
							case "2":// 到期日
								try {
									if (!isAdd) {
										if (DisclosureSonType.netValueEntity.getItemKey().equals(prodRule.getDisclosureSonType())) {
											tasks.add(this.copyEntityAndSetDate(Task, prodRule, netDate, dataSource));//传入基准日期netDate
											isAdd=true;
										} else {
											if(params.get("prodCode")==null){
												continue;
											}
											List<SqlRow> netValueList =disclosureProdRuleDao.findNetValueForEndDate(params);
											if (netValueList.size() > 0 && netValueList.get(0).getString("nav_dt").equals(prodRule.getEndDate())) {
												tasks.add(this.copyEntityAndSetDate(Task, prodRule, netDate, dataSource));//传入基准日期netDate
												isAdd=true;
											}
										}
									}
								} catch (Exception e) {
									throw new Exception(e);
								}
								break;
							case "3":// 确认日
								try {
									if (!isAdd) {
										if (DisclosureSonType.netValueEntity.getItemKey().equals(prodRule.getDisclosureSonType())) {
											tasks.add(this.copyEntityAndSetDate(Task, prodRule, netDate, dataSource));
											isAdd=true;
										} else {
											if(params.get("prodCode")==null){
												continue;
											}
											//查询是否申赎确认日
											List<SqlRow> netValueList =disclosureProdRuleDao.findNetValueForCnfDate(params);
											if (netValueList.size() > 0 && !netValueList.get(0).getString("nav_dt").equals(prodRule.getEndDate())) {
												tasks.add(this.copyEntityAndSetDate(Task, prodRule, netDate, dataSource));
												isAdd=true;
											}
										}
									}
								} catch (Exception e) {
									throw new Exception(e);
								}
								break;
							}
						}
					}
			});
		}
		return tasks;
	}

	/**
	 * 功能：生成到期公告任务公共方法
	 *
	 * @param realEndDate 实际到期日
	 * @throws Exception
	 */
	public void endReportComnMethod(String realEndDate, String taskStart,String forOnlyRule) throws Exception {
		// 查询信披类型为到期公告的信披规则
		Map<String, Object> params = new HashMap<>();
		params.put("taskStart",taskStart);
		params.put("realEndDate",realEndDate);
		params.put("id",forOnlyRule);
		List<ScheduleProdRule> prodRuleList = disclosureProdRuleDao.findEndProdRule(params);
		if (CollectionUtil.isNotEmpty(prodRuleList)) {
			DaoUtil.doTrans(() -> {
				String dataSource = querySource(taskStart);
				for (ScheduleProdRule prodRule : prodRuleList) {
					String baseDate = "";
					if ("".equals(realEndDate)||realEndDate==null){
						baseDate = prodRule.getEndDate();
					}else{
						baseDate = realEndDate;
					}
					insertReportInfo(baseDate,prodRule, dataSource,forOnlyRule);
				}
			});
		}
	}
	/**
	 * 功能：生成分红公告任务公共方法
	 *
	 * @param bonusIssueDate 红利发放日
	 * @throws Exception
	 */
	public void issueReportBonusMethod(String bonusIssueDate, String taskStart,String forOnlyRule) throws Exception {
		// 查询信披类型为发行公告的信披规则
		Map<String, Object> params = new HashMap<>();
		params.put("taskStart",taskStart);
		params.put("bonusIssueDate",bonusIssueDate);
		params.put("id",forOnlyRule);
		List<ScheduleProdRule> prodRuleList = disclosureProdRuleDao.findProdByBonusIssueDateAndRule(params);
		if (CollectionUtil.isNotEmpty(prodRuleList)) {
			DaoUtil.doTrans(() -> {
				String dataSource = querySource(taskStart);
				for (ScheduleProdRule prodRule : prodRuleList) {
					insertReportInfo(bonusIssueDate,prodRule, dataSource,forOnlyRule);
				}
			});
		}
	}
	/**
	 * 功能：组装好的任务list循环判断，如果存在，则更新，否则插入 作者：rennannan 日期：20211101
	 *
	 * @param prodRule
	 * @throws Exception
	 */
	private void insertReportInfo(String baseDate, ScheduleProdRule prodRule, String dataSource,String forOnlyRule) throws Exception {
		DisclosureProdTask task = getDisclosureProdTask(baseDate, prodRule);
		task.setDataSource(dataSource);
		//TODO 判断是否是净值公告，插入任务表不同
		//判断是否指定某一个规则的任务进行更新 [forOnlyRule]
		addForIsOnlyOrNot(forOnlyRule, task);
	}

	private void addForIsOnlyOrNot(String forOnlyRule, DisclosureProdTask task) throws Exception {
		Boolean flag ;
		// 检查任务表中是否已经存在信披任务，如果已经存在，更新，否则插入
		if (task.getDisclosureType().equals(DisclosureType.ensemble.getItemKey())||task.getDisclosureSonType().equals(DisclosureSonType.netValueEntity.getItemKey())){
			flag = t8DisclosureProdTaskDao.checkTaskZT(task);
		}else if(task.getDisclosureType().equals(DisclosureType.issEst.getItemKey())||task.getDisclosureType().equals(DisclosureType.sale.getItemKey())||
			task.getDisclosureType().equals(DisclosureType.preSale.getItemKey())||task.getDisclosureType().equals(DisclosureType.expire.getItemKey())){
			/**产品到期、发行成立、认购等产品日历挂钩的公告都不以基准日期做校验*/
			flag = t8DisclosureProdTaskDao.checkTaskCP(task);
		}else {
			flag = t8DisclosureProdTaskDao.checkTask(task);
		}
		if (!StringUtils.isNotEmpty(forOnlyRule)) {//规则修改导致的任务更新，仅更新不做新增，其他方式生成任务并校验后都新增
			if (flag) {
				/**添加到task表*/
				if (task.getDisclosureType().equals(DisclosureType.ensemble.getItemKey()) || task.getDisclosureSonType().equals(DisclosureSonType.netValueEntity.getItemKey())) {
					t8DisclosureProdTaskDao.addT8DisclosureProdTaskZT(task);
					//生成净值整体公告任务时候如果没有产品净值信息，也会有任务生成，需要把这个任务删除掉
					Map<String, Object> objectObjectHashMap = new HashMap<>();
					SqlParam<T8ProdNetValueTask> params = new FetcherData(objectObjectHashMap,T8ProdNetValueTask.class);
					SqlResult<T8ProdNetValueTask> t8ProdNetValueTasks = t8ProdNetValueTaskDao.findT8ProdNetValueTasks(params);
					//删除产品数量为0的任务
					List<T8ProdNetValueTask> rows = t8ProdNetValueTasks.getRows();
					for (T8ProdNetValueTask nTask:rows) {
						if("0".equals(nTask.getCount())){
							t8DisclosureProdTaskDao.deleteById(nTask.getId());
						}
					}
				} else {
					//挂钩产品的信披类型都不插入生成规则id
					task.setT8DisclosureRuleId("");
					t8DisclosureProdTaskDao.addT8DisclosureProdTask(task);
				}
			}
		}
	}

	/**
	 * 功能：根据信披规则组装任务信息 作者：rennannan 日期：20211101
	 *
	 * @param baseDate
	 * @param prodRule
	 * @return
	 * @throws Exception
	 */
	private DisclosureProdTask getDisclosureProdTask(String baseDate, ScheduleProdRule prodRule) throws Exception {
		//创建任务时间改成机器时间
//		String date = DateUtil.getSysWordDay();
		String date = DateUtil. getNowDate();
		String time = DateUtil.getNowTime();
		String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));// 用户编号
		String username = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username"));// 姓名
		String disclosureType = prodRule.getDisclosureType();
		String disclosureSonType = prodRule.getDisclosureSonType();
		DisclosureProdTask task = new DisclosureProdTask();
		String sysCrtDate = "";
		// 产品代码
		task.setProdCode(prodRule.getProdCode());
		//创建任务日期
		task.setCrtTaskDate(date);
		// 产品信披规则id
		if (!prodRule.getDisclosureType().equals(DisclosureType.ensemble.getItemKey())
				&&!prodRule.getDisclosureSonType().equals(DisclosureSonType.netValueEntity.getItemKey())){
			task.setT8DisclosureProdRuleId(prodRule.getId());// 信披规则id存入产品信披规则的id
		}
		// 状态 未生成报告
		task.setStatus(TaskStatus.forGenerate.getItemKey());
		// 计划生成日期，除净值报告外都需要通过计算规则T+/-
		if (!disclosureType.equals(DisclosureType.net.getItemKey())){
			String days = prodRule.getExpCreateDays();
			String rule = prodRule.getExpCreateRule();
			sysCrtDate = querySysCrtDate(rule,days,baseDate);
			task.setSysCrtDate(sysCrtDate);
		}else {
			//净值报告，当天为报告生成日
			task.setSysCrtDate(baseDate);
		}
		// 报告日期
		task.setReportDate(baseDate);
		task.setProdBaseDate(baseDate);
		// task_month 任务所属月份
		task.setTaskMonth(baseDate.substring(0, 6));
		// 信披类型
		task.setDisclosureType(disclosureType);
		task.setDisclosureSonType(disclosureSonType);
		// 创建时间 创建人
		task.setCrtUserId(userid);
		task.setCrtUserName(username);
		task.setCrtDate(date);
		task.setCrtTime(time);
		task.setUpdDate(date);
		task.setUpdTime(time);
		task.setUpdUserId(userid);
		task.setUpdUserName(username);
		// 未替换的公告标题
		String title = prodRule.getNoticeTitle();
		String prodCode = prodRule.getProdCode();
		task.setNoticeTitle(queryTitle(title,baseDate,prodCode,disclosureType,disclosureSonType));
		return task;
	}

	/**
	 * 信披任务管理-生成任务权限控制
	 * @return
	 */
	@API(desc = "生成任务",auth = APIAuth.YES)
	public String addTaskRightControl() {
		return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
	}
}
