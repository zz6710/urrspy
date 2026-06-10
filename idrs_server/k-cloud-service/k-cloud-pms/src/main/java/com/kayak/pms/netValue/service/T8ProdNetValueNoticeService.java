package com.kayak.pms.netValue.service;

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
import com.kayak.pms.disclosureControl.dao.DisclosureOperationDao;
import com.kayak.pms.disclosureControl.dao.DisclosureWorkdayDao;
import com.kayak.pms.disclosureControl.dao.ScheduleProdRuleDao;
import com.kayak.pms.disclosureControl.disclousreEnum.DisclosureTypeEnum;
import com.kayak.pms.disclosureControl.disclousreEnum.OperationTypeEnum;
import com.kayak.pms.disclosureControl.model.DisclosureOperation;
import com.kayak.pms.disclosureControl.model.DisclosureWorkday;
import com.kayak.pms.disclosureControl.model.ScheduleProdRule;
import com.kayak.pms.email.dao.T8DisChannelInfoDao;
import com.kayak.pms.netValue.dao.T8ProdNetValueNoticeDao;
import com.kayak.pms.netValue.dao.T8ProdNetValueTaskDao;
import com.kayak.pms.netValue.model.T8ProdNetValueNotice;
import com.kayak.pms.netValue.model.T8ProdNetValueTask;
import com.kayak.pms.netValue.util.NetValUtil;
import com.kayak.pms.schedule.dao.ScheduleNavDao;
import com.kayak.pms.schedule.model.ScheduleNav;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


@Service
@APIDefine(desc = "净值披露数据服务", model = T8ProdNetValueNotice.class)
public class T8ProdNetValueNoticeService {

	@Autowired
	private T8ProdNetValueNoticeDao t8ProdNetValueNoticeDao;
	@Autowired
	private T8ProdNetvalDaysService t8ProdNetvalDaysService;
	@Autowired
	private ScheduleProdRuleDao scheduleProdRuleDao;
	@Autowired
	private DisclosureOperationDao disOperationDao;
	@Autowired
	private DisclosureWorkdayDao disclosureWorkdayDao;
	@Autowired
	private T8ProdNetValueTaskDao t8ProdNetValueTaskDao;
	@Autowired
	private ScheduleNavDao scheduleNavDao;
	@Autowired
	private T8DisChannelInfoDao t8DisChannelInfoDao;

	@API(desc = "查询净值披露数据信息", auth = APIAuth.NO)
	public SqlResult<T8ProdNetValueNotice> findT8ProdNetValueNotices(SqlParam<T8ProdNetValueNotice> params) throws Exception {
		LinkedList<T8ProdNetValueNotice> result = new LinkedList<>();
		List<T8ProdNetValueNotice> t8ProdNetValueNotices = t8ProdNetValueNoticeDao.findT8ProdNetValueNotices(params.getModel());
		//遍历母产品，获取子份额数据
		for (T8ProdNetValueNotice  t8ProdNetValueNotice:t8ProdNetValueNotices){
			t8ProdNetValueNotice.setIsParentProd("1");
			result.add(t8ProdNetValueNotice);
			if("1".equals(t8ProdNetValueNotice.getIsShareSort())){
				//获取nav表里所有母产品子份额信息
				List<T8ProdNetValueNotice> t8ProdShareSort = t8ProdNetValueNoticeDao.findT8ProdShareSort(t8ProdNetValueNotice);
				result.addAll(t8ProdShareSort);
			}
		}
		LinkedList<T8ProdNetValueNotice> finalResult = SqlResult.page(params, result);
		return SqlResult.build(finalResult, result.size(),false);
	}

	@API(desc = "根据产品代码和净值日期查询公告", auth = APIAuth.NO)
	public SqlResult<T8ProdNetValueNotice> findT8ProdNetValueNoticeList(SqlParam<T8ProdNetValueNotice> params) throws Exception {
		return t8ProdNetValueNoticeDao.findT8ProdNetValueNoticeList(params);
	}

	@API(desc = "添加净值披露数据", auth = APIAuth.NO)
	public int addT8ProdNetValueNotice(SqlParam<T8ProdNetValueNotice> params) throws Exception {
		String date = DateUtil.getNowDate();
		String time = DateUtil.getNowTime();
		String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));//用户编号
		String username = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username"));//姓名
		params.getModel().setStatus("0");//状态默认为未确认
		params.getModel().setCrtDate(date);
		params.getModel().setCrtTime(time);
		params.getModel().setCrtUserId(userid);
		params.getModel().setCrtUserName(username);
		List<SqlRow> row = t8ProdNetValueNoticeDao.findNetValueNoticeList(params.getModel().getT8DisclosureTaskId());
		Integer count = 0;
		if(row!=null&&row.size()>0){
			count = row.size();
		}
		count = count+1;
		String des = "当前有"+count+"只产品需要披露";
		AtomicReference<Integer> in = new AtomicReference<>(0);
		DaoUtil.doTrans(() -> {
			t8ProdNetValueTaskDao.updateT8ProdNetValueTaskForDes(des,params.getModel().getT8DisclosureTaskId());
			in.set(t8ProdNetValueNoticeDao.addT8ProdNetValueNotice(params).getEffect());
		});
		return in.get();
	}

	@API(desc = "修改净值披露数据", auth = APIAuth.NO)
	public int updateT8ProdNetValueNotice(SqlParam<T8ProdNetValueNotice> params) throws Exception {
		String date = DateUtil.getNowDate();
		String time = DateUtil.getNowTime();
		String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));//用户编号
		String username = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username"));//姓名
		params.getModel().setUpdDate(date);
		params.getModel().setUpdTime(time);
		params.getModel().setUpdUserId(userid);
		params.getModel().setUpdUserName(username);
		return t8ProdNetValueNoticeDao.updateT8ProdNetValueNotice(params).getEffect();
	}

	@API(desc = "删除净值披露数据", auth = APIAuth.NO)
	public int deleteT8ProdNetValueNotice(SqlParam<T8ProdNetValueNotice> params) throws Exception {
		List<SqlRow> row = t8ProdNetValueNoticeDao.findNetValueNoticeList(params.getModel().getT8DisclosureTaskId());
		Integer count = 0;
		if(row!=null&&row.size()>0){
			count = row.size();
		}
		count = count-1;
		String des = "当前有"+count+"只产品需要披露";
		AtomicReference<Integer> in = new AtomicReference<>(0);
		DaoUtil.doTrans(() -> {
			t8ProdNetValueTaskDao.updateT8ProdNetValueTaskForDes(des,params.getModel().getT8DisclosureTaskId());
			in.set(t8ProdNetValueNoticeDao.deleteT8ProdNetValueNotice(params).getEffect());
		});
		return in.get();
	}

	/**
	 * 功能：更新今日需要披露的产品净值数据  供每天跑的批量调用
	 * 作者：rennannan
	 * 日期：20210626
	 *
	 * @param params
	 * @throws Exception
	 */
	@API(desc = "更新今日需要披露的产品净值数据", auth = APIAuth.NO)
	public void sysNoticeNetValue(SqlParam<T8ProdNetValueNotice> params) throws Exception {
		//获取当前日期
		String nowDate = DateUtil.getNowDate();

		//查询notice表中需要今日披露的数据
		T8ProdNetValueNotice notice = new T8ProdNetValueNotice();
		notice.setDisclosureDate(nowDate);
		List<T8ProdNetValueNotice> noticeList = t8ProdNetValueNoticeDao.findT8NetValueNoticeList(notice);

		//逐条更新
		for (T8ProdNetValueNotice netValueNotice : noticeList) {
			//根据产品代码和净值日期查询净值数据  t8_prod_nav
			ScheduleNav nav = new ScheduleNav();
			nav.setNavDate(netValueNotice.getNetvalDate());//净值日期
			nav.setProdCode(netValueNotice.getProdCode());//产品代码
			List<ScheduleNav> navList = scheduleNavDao.findNavList(nav);
			if (navList.size() > 0) {
				String date = DateUtil.getNowDate();
				String time = DateUtil.getNowTime();
				ScheduleNav navNew = navList.get(0);
				netValueNotice.setNav(navNew.getNav());//产品单位净值
				netValueNotice.setNavProfit(navNew.getNavProfit());//当日收益
				netValueNotice.setSevenDaysIncomeRate(navNew.getSevenDaysIncomeRate());//近七日年化收益率
				netValueNotice.setTenThousandIncomeAmt(navNew.getTenThousandIncomeAmt());//单位万份收益
				netValueNotice.setTotalNav(navNew.getTotalNav());//产品累计净值
				netValueNotice.setTotalNet(navNew.getTotalNet());//产品总净值
				netValueNotice.setTotalVol(navNew.getTotalVol());//产品总份额
				netValueNotice.setUpdDate(date);
				netValueNotice.setUpdTime(time);
				t8ProdNetValueNoticeDao.updateProdNetValueNotice(netValueNotice);
			}
		}

	}

	/**
	 * 功能：校验今日披露净值数据是否完整 完整的状态改为2 不完整的状态改为1
	 * 验证字段为 产品单位净值nav和单位万份收益ten_thousand_income_amt
	 * 作者：rennannan
	 * 日期：20210626
	 */
	@API(desc = "校验今日披露净值数据是否完整", auth = APIAuth.NO)
	public void autoCheckNetComplete(SqlParam<T8ProdNetValueNotice> params) throws Exception {
		//获取当前日期
		String nowDate = DateUtil.getNowDate();
		//查询notice表中需要今日披露的数据
		T8ProdNetValueNotice notice = new T8ProdNetValueNotice();
		notice.setDisclosureDate(nowDate);
		List<T8ProdNetValueNotice> noticeList = t8ProdNetValueNoticeDao.findT8NetValueNoticeList(notice);
		for (T8ProdNetValueNotice netValueNotice : noticeList) {
			//不为空
			if (StringUtils.isNotEmpty(netValueNotice.getNav()) || StringUtils.isNotEmpty(netValueNotice.getTenThousandIncomeAmt())) {
				netValueNotice.setStatus("2");//数据已更新
			} else {
				netValueNotice.setStatus("1");//数据未更新
			}
			t8ProdNetValueNoticeDao.updateNoticeStatus(netValueNotice);
		}
	}

	/**
	 * 功能：生成净值日期等于下一日的净值披露任务和数据
	 * 作者：rennannan
	 * 日期：20210624
	 */
	@API(desc = "跑批生成净值为下一日的净值披露任务和数据", auth = APIAuth.NO, operation = APIOperation.INSTER)
	public void autoGenerateNetByDay(SqlParam<T8ProdNetValueNotice> params) throws Exception {
		//获取当前日期
		String nowDate = DateUtil.getNowDate();

		//获取下一日日期
		String nextDate = DateUtil.add(nowDate, "yyyyMMdd", 1);

		List<T8ProdNetValueNotice> noticeList = getNextDayNetValDayList(nextDate);

		//生成notice表数据
		deleteAndAddNets(noticeList, nextDate);

		//生成task数据
		String des = "当前有0只产品需要披露";
		String taskId = checkAndAddTask(nextDate,des);

		//更新披露日期为nextDate 的净值数据的任务id
		T8ProdNetValueNotice notice = new T8ProdNetValueNotice();
		notice.setDisclosureDate(nextDate);
		notice.setT8DisclosureTaskId(taskId);
		t8ProdNetValueNoticeDao.updateNoticeTaskId(notice);

		//插入审批流代办暂定信披经理发起审批,注册登记岗,估值核算岗,交易支持岗,信息披露岗可以审批
		generateOperation(taskId);

		t8ProdNetValueTaskDao.updateNetValueTaskForDes(taskId);
	}

	/**
	 * 功能：生成净值披露代办
	 * 作者：zls
	 * 日期：20210804
	 *
	 * @return
	 */
	public void generateOperation(String taskId) throws Exception {
		//先删除待办
		DisclosureOperation operation = new DisclosureOperation();
		operation.setDealId(taskId);
		operation.setDisclosureType(DisclosureTypeEnum.NINE.getVal());//信披类型
		operation.setOperationType(OperationTypeEnum.SEVEN.getVal());//操作类型 2 代表数据补录
		disOperationDao.deleteDisclosureOperation(operation);
		String nowDate = DateUtil.getNowDate();
		String nowTime = DateUtil.getNowTime();
		String roleId = "";
		List<SqlRow> row = t8ProdNetValueNoticeDao.findRoleId(roleId);
		roleId = row.get(0).getString("roleid");
		//生成待办
		if(row!=null&&row.size()>0){
			for(int i=0;i<row.size();i++){
				DisclosureOperation ope = new DisclosureOperation();
				ope.setStatus("0");//状态 0 待办结
				ope.setDisclosureType(DisclosureTypeEnum.NINE.getVal());//信披类型
				ope.setRoleid(roleId);//角色编号
				ope.setOperationType(OperationTypeEnum.SEVEN.getVal());//操作类型 2 代表数据补录
				ope.setUserid(row.get(i).getString("userid"));//用户编号
				//ope.setDealId(processId);//业务流水号  process的id
				ope.setDealId(taskId);
				//ope.setDealTable("idb_disclosure_notice_process");//业务表
				ope.setDealTable("t8_prod_net_value_taks");
				ope.setCrtDate(nowDate);
				ope.setCrtTime(nowTime);
				disOperationDao.insertDisOperation(ope);
			}
		}
	}

	/**
	 * 功能：将当日需要披露的状态为2（已更新）的数据生成数据文件并发送指定邮箱
	 * 作者：zls
	 * 日期：20210729
	 * 测试接口
	 * @param params
	 * @throws Exception
	 */
	@API(desc = "测试发送净值披露文件至邮箱", auth = APIAuth.NO)
	public String testSendEmails(SqlParam<T8ProdNetValueTask> params) throws Exception {
		String path = "";//附件路径
		//获取当前日期
		String nowDate = DateUtil.getNowDate();
		//查询notice表中需要今日披露的数据
		T8ProdNetValueNotice notice = new T8ProdNetValueNotice();
		String taskDate = params.getModel().getTaskDate();

		notice.setDisclosureDate(taskDate);
		//List<T8ProdNetValueNotice> noticeList = t8ProdNetValueNoticeDao.findT8NetValueNoticeList(notice);

		//将数据放入到文件中     文件形式待定
		path = generateNetValueNotice(notice);


		/*//查询收件邮箱
		T8DisChannelInfo channelInfo = new T8DisChannelInfo();
		channelInfo.setChannelType("3");//类型 3净值披露接收邮箱
		List<T8DisChannelInfo> receiveEmail = t8DisChannelInfoDao.findChannelInfoList(channelInfo);
		//查询发件邮箱
		SqlRow accRow = emailDao.findSendEmailAccount();
		if (null == accRow) {

		} else {
			//调用发送邮箱接口
			Map<String, String> map = new HashMap<>();
			map.put("emails", accRow.get("emails").toString());//邮箱地址
			map.put("emailPasswd", accRow.get("email_passwd").toString());//邮箱密码
			map.put("emailAcctnoName", accRow.get("channel_name").toString());//邮箱名
			map.put("receiveMailAccount", receiveEmail.get(0).getEmails());//收件邮箱
			map.put("cc", "");//抄送邮箱
			map.put("subject", "信披公告");//邮件主题
			map.put("content", "信披公告");//文本内容
			map.put("filePath", path);
			emailService.sendEmail(map);
		}*/
		return RequestSupport.updateReturnJson(true, "净值披露文件生成成功!", null).toString();
	}

	/**
	 * 功能：生成净值披露文件
	 * 作者：zls
	 * 日期：20210729
	 *
	 * @return
	 */
	public String generateNetValueNotice2(T8ProdNetValueNotice notice) throws Exception {
		byte[] buffer = new byte[1024];
		FileInputStream fileInputStream = null;
		BufferedInputStream bufferedInputStream = null;
		BufferedOutputStream outputStream = null;
		FileOutputStream out = null;
		String path = "";
		/*List<T8ProdNetValueNotice> noticeList = t8ProdNetValueNoticeDao.findNetValueNoticeList(notice);
		int ch = 0;
		try {
			//获取净值披露静态模板
			String rootPath = PrintTempUtil.getRootPath();
			String fileName = rootPath + "/static/excelTemp/净值披露模板表.xlsx";
			fileInputStream = new FileInputStream(fileName);
			HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
			HSSFSheet sheet = workbook.getSheetAt(0);
			if(!CollectionUtils.isEmpty(noticeList)){
				for(int i=0;i<noticeList.size();i++){
					HSSFRow row = sheet.getRow(i);
					if (StringUtils.isNotEmpty(noticeList.get(i).getTotalVol())){
						row.createCell(0).setCellValue(noticeList.get(i).getTotalVol());
					}
					if (StringUtils.isNotEmpty(noticeList.get(i).getNavProfit())){
						row.createCell(1).setCellValue(noticeList.get(i).getNavProfit());
					}
					if (StringUtils.isNotEmpty(noticeList.get(i).getTenThousandIncomeAmt())){
						row.createCell(2).setCellValue(noticeList.get(i).getTenThousandIncomeAmt());
					}
					if (StringUtils.isNotEmpty(noticeList.get(i).getSevenDaysIncomeRate())){
						row.createCell(2).setCellValue(noticeList.get(i).getSevenDaysIncomeRate());
					}
					if (StringUtils.isNotEmpty(noticeList.get(i).getNetvalDate())){
						row.createCell(2).setCellValue(noticeList.get(i).getNetvalDate());
					}
				}

			}
			//bufferedInputStream = new BufferedInputStream(fileInputStream);
			//File file = new File("d:\\out.xls");
			File file=new File("d:\\poi\\");
			OutputStream stream=null;
			try {
				stream = new FileOutputStream(new File(file, "机构客户信息表_"+".xls"));
				//document.write(stream);
				workbook.write(stream);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(stream != null);
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			out = new FileOutputStream("d:\\out.xls");
			path="d:\\out.xls";
			outputStream = new BufferedOutputStream(out);
			while ((ch = bufferedInputStream.read()) != -1) {
				outputStream.write(ch);
			}
		}catch (Exception e){

		} finally {
			//关闭输入流等（略）
			if(outputStream!=null){
				outputStream.close();
			}
			if(fileInputStream!=null){
				fileInputStream.close();
			}
			if(bufferedInputStream!=null){
				bufferedInputStream.close();
			}
		}*/
		return path;
	}

	public String generateNetValueNotice(T8ProdNetValueNotice notice) throws Exception {
		Date date = new Date();
		OutputStream out = null;
		String path = "";
		// 创建一个excel文件
		HSSFWorkbook book = new HSSFWorkbook();
		// 创建Sheet对象
		/*HSSFSheet sheet = book.createSheet("机构客户信息表");
		List<T8ProdNetValueNotice> noticeList = t8ProdNetValueNoticeDao.findNetValueNoticeList(notice);
		if (noticeList != null && noticeList.size()>0) {
			HSSFRow  row = null;
			HSSFCell cell = null;
			int rowIndex = 2;
			row = sheet.createRow(1);
			for(int i=0;i<noticeList.size();i++){
				row = sheet.createRow(rowIndex);
				if (StringUtils.isNotEmpty(noticeList.get(i).getTotalVol())){
					row.createCell(0).setCellValue(noticeList.get(i).getTotalVol());
				}
				if (StringUtils.isNotEmpty(noticeList.get(i).getNavProfit())){
					row.createCell(1).setCellValue(noticeList.get(i).getNavProfit());
				}
				if (StringUtils.isNotEmpty(noticeList.get(i).getTenThousandIncomeAmt())){
					row.createCell(2).setCellValue(noticeList.get(i).getTenThousandIncomeAmt());
				}
				if (StringUtils.isNotEmpty(noticeList.get(i).getSevenDaysIncomeRate())){
					row.createCell(3).setCellValue(noticeList.get(i).getSevenDaysIncomeRate());
				}
				if (StringUtils.isNotEmpty(noticeList.get(i).getNetvalDate())){
					row.createCell(4).setCellValue(noticeList.get(i).getNetvalDate());
				}
				rowIndex++;
			}
		}
		path = "d:\\poi\\机构客户信息表.xls";
		File file=new File("d:\\poi\\");
		OutputStream stream=null;
		try {
			stream = new FileOutputStream(new File(file, "机构客户信息表"+".xls"));
			//document.write(stream);
			book.write(stream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(stream != null);
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}*/
		return path;
	}

	/**
	 * 功能：先检查当天日期是否已经存在任务，如果存在则查询出任务id，否则就插入
	 * 作者：rennannan
	 * 日期：20210626
	 *
	 * @return
	 */
	public String checkAndAddTask(String taskDate,String des) throws Exception {
		T8ProdNetValueTask valueTask = new T8ProdNetValueTask();
		valueTask.setTaskDate(taskDate);
		List<T8ProdNetValueTask> taskList = t8ProdNetValueTaskDao.findT8ProdNetValueTasksList(valueTask);
		String taskId = "";
		if (taskList.size() > 0) {
			taskId = taskList.get(0).getId();
		} else {
			taskId = addTask(taskDate,des);
		}
		return taskId;
	}

	/**
	 * 功能：插入净值披露任务
	 * 作者：rennannan
	 * 日期：20210626
	 *
	 * @param taskDate 任务日期
	 * @throws Exception
	 */
	public String addTask(String taskDate,String des) throws Exception {
		String date = DateUtil.getNowDate();
		String time = DateUtil.getNowTime();
		T8ProdNetValueTask task = new T8ProdNetValueTask();
		task.setCrtDate(date);
		task.setCrtTime(time);
		task.setTaskDesc(des);
		task.setTaskName("净值披露任务");
		task.setTaskDate(taskDate);
		task.setTaskStatus("0");//状态 默认未确认
		return t8ProdNetValueTaskDao.insertProdNetValueTask(task);
	}

	/**
	 * 功能：删除净值日期为当前的日期数据，再插入
	 */
	public void deleteAndAddNets(List<T8ProdNetValueNotice> noticeList, String date) throws Exception {
		DaoUtil.doTrans(() -> {
			//先删除，再插入
			t8ProdNetValueNoticeDao.deleteNoticeByNetDay(date);
			String nowDate = DateUtil.getNowDate();
			String time = DateUtil.getNowTime();
			for (T8ProdNetValueNotice notice : noticeList) {
				notice.setCrtDate(nowDate);
				notice.setCrtTime(time);
				t8ProdNetValueNoticeDao.addNetValueNotice(notice);
			}
		});
	}

	/**
	 * 功能：根据日期获取披露日期等于传入日期的净值披露数据
	 * 作者：rennannan
	 * 日期：20210624
	 *
	 * @param date
	 * @return
	 */
	public List<T8ProdNetValueNotice> getNextDayNetValDayList(String date) throws Exception {
		List<T8ProdNetValueNotice> t8ProdNetValNoticeList = new ArrayList<>();

		ScheduleProdRule prodRule = new ScheduleProdRule();
		prodRule.setDisclosureType("9");//信披类型  净值公告
		prodRule.setStartRule("2");//发起方式  时间规则发起
		List<ScheduleProdRule> prodRuleList = scheduleProdRuleDao.findProdRulesByType(prodRule);

		for (ScheduleProdRule scheduleProdRule : prodRuleList) {
			t8ProdNetValNoticeList.addAll(getNetValDayByRule(scheduleProdRule, date));
		}
		return t8ProdNetValNoticeList;
	}

	/**
	 * 功能：根据信披规则按日查询产品净值披露数据
	 * 作者：rennannan
	 * 日期：20210624
	 *
	 * @return
	 */
	public List<T8ProdNetValueNotice> getNetValDayByRule(ScheduleProdRule scheduleProdRule, String date) throws Exception {
		List<T8ProdNetValueNotice> netValNoticeList = new ArrayList<>();
		String prodInfoId = scheduleProdRule.getT8ProdInfoId();//产品id
		String prodCode = scheduleProdRule.getProdCode();//产品代码
		String netValueDateStr = scheduleProdRule.getNetValueDate();//基准日期 多个，以“,”分隔
		String netValueDateRule = scheduleProdRule.getNetValueDateRule();//披露日期规则
		String netValueDates[] = netValueDateStr.split(",");//基准日期数组
		String realCloseDate = scheduleProdRule.getProdRealCloseDate();//实际终止日期
		String establishDate = scheduleProdRule.getEstablishDate();//成立日

		//净值日期需要加的天数   加上以后才能得出披露日期
		int addDays = NetValUtil.getAddDays(netValueDateRule);

		//查询当天交易所工作日
		List<DisclosureWorkday> dealWorkDayList = t8ProdNetvalDaysService.getDealWorkDays("002", date, date);
		//查询下一个工作日
		Map<String, String> nextParam = new HashMap<>();
		nextParam.put("workday", date);
		nextParam.put("pgmno", "002");
		List<DisclosureWorkday> nextWorkDayList = disclosureWorkdayDao.findNextWorkDay(nextParam);
		boolean hasBankRule = Arrays.asList(netValueDates).contains("01"); //是否包含01 工作日披露工作日以及节假日的类型

		Set<String> set = new HashSet<String>();
		String firstNetVal = "";
		for (int i = 0; i < netValueDates.length; i++) {//基准日期
			String netValueDate = netValueDates[i];//单个基准日期
			switch (netValueDate) {
				case "01":  //银行间工作日（工作日披露工作日及相邻节假日）
					if (!set.contains(date)) {//不存在才插入
						//查询是否为银行间工作日，若是则放入list，然后判断是否为节假日前工作日，如果是，需要判断是否是产品的终止日，不是才需要披露节假日的净值，是则只披露当前的
						List<DisclosureWorkday> bankWorkDayList = t8ProdNetvalDaysService.getDealWorkDays("001", date, date);
						if (bankWorkDayList.size() > 0) {//存在
							netValNoticeList.add(setNetValDayPropForNet(date, prodCode, "", addDays, prodInfoId));
							set.add(date);
							//实际终止日期为空或者不等于实际终止日期都需要判断是否为节假日前的工作日
							if (StringUtils.isEmpty(realCloseDate) || !(date.equals(realCloseDate))) {
								//满足条件则将工作日相邻的节假日净值披露
								getAndAddHolidayForNet(nextWorkDayList, date, netValNoticeList, prodCode, addDays, prodInfoId);
							}
						}
					}
					break;
				case "02": //每个交易所工作日 直接取sys_workday_set中pgmno为002的
					//判断是否为交易所工作日
					if (!set.contains(date)) {//不存在才插入
						if (dealWorkDayList.size() > 0) {//存在
							netValNoticeList.add(setNetValDayProp(date, prodCode, "", addDays, prodInfoId));
							set.add(date);
						}
					}
					break;
				case "03": //交易所节假日前最后一个工作日 sys_workday_set表中取pgmno为002的
					if (!set.contains(date)) {//不存在才插入
						if (dealWorkDayList.size() > 0) {//存在
							//判断是否为节假日前的最后一个工作日
							String nextDate = "";
							if (nextWorkDayList.size() > 0) {
								nextDate = nextWorkDayList.get(0).getWorkday();
							}
							if (StringUtils.isNotEmpty(nextDate)) {//下一个工作日不为空
								//计算与下一个工作日相差的天数
								int days = DateUtil.getdifferentDays(date, nextDate);
								//如果相差天数大于1，说明是节假日前的工作日
								if (days > 1) {
									netValNoticeList.add(setNetValDayProp(date, prodCode, "", addDays, prodInfoId));
									set.add(date);
								}
							}
						}
					}
					break;
				case "04": //月末最后一个工作日 sys_workday_set表中取，日期区间本月的最后一日
					//获取对应日期所在月份并查询对应月份最后一个工作日，判断是否相等，相等时才插入
					if (!set.contains(date)) {//不存在才插入
						Map<String,String> map = new HashMap<>();
						map.put("prodId",prodInfoId);
						String month = date.substring(0,6);
						map.put("month",month);
						String lastDate = disclosureWorkdayDao.findLastWorkDayOfMonth(map);
						String lastDayOfMonth = DateUtil.getLastDayOfMonth(date);
						if (lastDate.equals(date)) {
							netValNoticeList.add(setNetValDayProp(date, prodCode, "", addDays, prodInfoId));
							set.add(date);
							if (hasBankRule) { //如果信披规则中包含01的，需要判断是否为节假日前的最后一个工作日，是则需要披露包括节假日的净值
								getAndAddHoliday(nextWorkDayList, date, netValNoticeList, prodCode, addDays, prodInfoId);
							}
						}
					}
					break;
				case "05": //开放日 从t8_prod_days表中取值
					//判断是否为产品的开放日，是才插入
					if (!set.contains(date)) {//不存在才插入
						Map<String, String> openParam = new HashMap<>();
						openParam.put("prodId", prodInfoId);
						openParam.put("startDate", date);
						openParam.put("endDate", date);
						List<DisclosureWorkday> openDaysList = disclosureWorkdayDao.findProdOpenDays(openParam);
						if (openDaysList.size() > 0) {
							netValNoticeList.add(setNetValDayProp(date, prodCode, "", addDays, prodInfoId));
							set.add(date);
							if (hasBankRule) { //如果信披规则中包含01的，需要判断是否为节假日前的最后一个工作日，是则需要披露包括节假日的净值
								getAndAddHoliday(nextWorkDayList, date, netValNoticeList, prodCode, addDays, prodInfoId);
							}
						}
					}
					break;
				case "06": //封闭投资日   从t8_prod_schedule表中取值  根据产品代码和日期区间查询
					if (!set.contains(date)) {//不存在才插入
						Map<String, String> closeParam = new HashMap<>();
						closeParam.put("prodCode", prodCode);
						closeParam.put("workday", date);
						int count = disclosureWorkdayDao.getCloseDayCount(closeParam);
						if (count > 0) {
							netValNoticeList.add(setNetValDayProp(date, prodCode, "", addDays, prodInfoId));
							set.add(date);
						}
					}
					break;
				case "07": //分红除权日-2  查询分红方案表t8_prod_dividend_plan中分红除权日
					//当前日期加2天等于产品的分红除权日，插入
					if (!set.contains(date)) {//不存在才插入
						String twoDaysAfter = DateUtil.add(date, "yyyyMMdd", 2);
						Map<String, String> divideTwoParam = new HashMap<>();
						divideTwoParam.put("prodCode", prodCode);
						divideTwoParam.put("workday", twoDaysAfter);
						int count = disclosureWorkdayDao.getDivideDayCount(divideTwoParam);
						if (count > 0) {
							netValNoticeList.add(setNetValDayProp(date, prodCode, "", addDays, prodInfoId));
							set.add(date);
						}
					}
					break;
				case "08": //分红除权日
					//当前日期等产品的分红除权日，插入
					if (!set.contains(date)) {//不存在才插入
						Map<String, String> divideParam = new HashMap<>();
						divideParam.put("prodCode", prodCode);
						divideParam.put("workday", date);
						int count = disclosureWorkdayDao.getDivideDayCount(divideParam);
						if (count > 0) {
							netValNoticeList.add(setNetValDayProp(date, prodCode, "", addDays, prodInfoId));
							set.add(date);
						}
					}
					break;
				case "09"://成立日
					//当前日期等于产品的成立日，插入
					if (!set.contains(date)) {//不存在才插入
						//判断是否等于成立日
						if (StringUtils.isNotEmpty(establishDate)) {
							if (date.equals(establishDate)) {
								//setNetValDayProp(date, prodCode, "", addDays, prodInfoId);
								netValNoticeList.add(setNetValDayProp(date, prodCode, "", addDays, prodInfoId));
								set.add(date);
								if (hasBankRule) { //如果信披规则中包含01的，需要判断是否为节假日前的最后一个工作日，是则需要披露包括节假日的净值
									getAndAddHoliday(nextWorkDayList, date, netValNoticeList, prodCode, addDays, prodInfoId);
								}
							}
						}
					}
					break;

				case "10"://终止日
					//当前日期等于产品的终止日，插入
					if (!set.contains(date)) {//不存在才插入
						//判断是否等于成立日
						if (StringUtils.isNotEmpty(realCloseDate)) {
							if (date.equals(realCloseDate)) {
								netValNoticeList.add(setNetValDayProp(date, prodCode, "", addDays, prodInfoId));
								set.add(date);
							}
							if (hasBankRule) { //如果信披规则中包含01的，需要判断是否为节假日前的最后一个工作日，是则需要披露包括节假日的净值
								getAndAddHoliday(nextWorkDayList, date, netValNoticeList, prodCode, addDays, prodInfoId);
							}
						}
					}
					break;
			}
		}
		return netValNoticeList;
	}

	/**
	 * 功能：查询当前传入的日期是否为传入的产品的分红除权日
	 * 作者:rennannan
	 * 日期：20210625
	 *
	 * @param prodCode
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public int getDivideDayCount(String prodCode, String date) throws Exception {
		Map<String, String> divideParam = new HashMap<>();
		divideParam.put("prodCode", prodCode);
		divideParam.put("workday", date);
		return disclosureWorkdayDao.getDivideDayCount(divideParam);
	}

	/**
	 * 功能：判断工作日是否为节假日前工作日，如果是，则将需要披露的节假日信息放入list中
	 * 作者：rennannan
	 * 日期：20210625
	 *
	 * @param nextWorkDayList  下一个工作日
	 * @param date             当前日期
	 * @param netValNoticeList 需要放入的list
	 * @param prodCode         产品代码
	 * @param addDays          需要加的天数
	 * @param prodInfoId       产品id
	 * @throws Exception
	 */
	public void getAndAddHoliday(List<DisclosureWorkday> nextWorkDayList, String date, List<T8ProdNetValueNotice> netValNoticeList, String prodCode, int addDays, String prodInfoId) throws Exception {
		String nextDate = "";
		if (nextWorkDayList.size() > 0) {
			nextDate = nextWorkDayList.get(0).getWorkday();
		}
		if (StringUtils.isNotEmpty(nextDate)) {//下一个工作日不为空
			//计算与下一个工作日相差的天数
			int days = DateUtil.getdifferentDays(date, nextDate);
			//如果相差天数大于1，说明是节假日前的工作日，那么需要披露相邻的节假日  date为披露日期，相邻的节假日日期为净值日期
			if (days > 1) {
				String tempDate = date;
				for (int j = 1; j < days; j++) {
					tempDate = DateUtil.add(tempDate, "yyyyMMdd", 1);//净值日期
					netValNoticeList.add(setNetValDayProp(tempDate, prodCode, date, addDays, prodInfoId));
				}
			}
		}
	}

	/**
	 * 功能：判断工作日是否为节假日前工作日，如果是，则将需要披露的节假日信息放入list中
	 * 作者：rennannan
	 * 日期：20210625
	 *
	 * @param nextWorkDayList  下一个工作日
	 * @param date             当前日期
	 * @param netValNoticeList 需要放入的list
	 * @param prodCode         产品代码
	 * @param addDays          需要加的天数
	 * @param prodInfoId       产品id
	 * @throws Exception
	 */
	public void getAndAddHolidayForNet(List<DisclosureWorkday> nextWorkDayList, String date, List<T8ProdNetValueNotice> netValNoticeList, String prodCode, int addDays, String prodInfoId) throws Exception {
		String nextDate = "";
		if (nextWorkDayList.size() > 0) {
			nextDate = nextWorkDayList.get(0).getWorkday();
		}
		if (StringUtils.isNotEmpty(nextDate)) {//下一个工作日不为空
			//计算与下一个工作日相差的天数
			int days = DateUtil.getdifferentDays(date, nextDate);
			//如果相差天数大于1，说明是节假日前的工作日，那么需要披露相邻的节假日  date为披露日期，相邻的节假日日期为净值日期
			if (days > 1) {
				String tempDate = date;
				for (int j = 1; j < days; j++) {
					tempDate = DateUtil.add(tempDate, "yyyyMMdd", 1);//净值日期
					netValNoticeList.add(setNetValDayPropForNet(tempDate, prodCode, date, addDays, prodInfoId));
				}
			}
		}
	}

	/**
	 * 功能：给notice赋值
	 * 作者：rennannan
	 * 日期：20210625
	 *
	 * @return
	 */
	public T8ProdNetValueNotice setNetValDayProp(String netValueDate, String prodCode, String disclosureDate, int addDays, String prodInfoId) throws Exception {
		T8ProdNetValueNotice notice = new T8ProdNetValueNotice();
		notice.setNetvalDate(netValueDate);//净值日期
		if (StringUtils.isEmpty(disclosureDate)) {//传入的披露日期不为空
			disclosureDate = t8ProdNetvalDaysService.getDisclosureDateByDays(netValueDate, addDays, prodInfoId);
		}
		notice.setDisclosureDate(disclosureDate);//披露日期
		notice.setStatus("0");//状态  未校验
		notice.setProdCode(prodCode);//产品代码
		return notice;
	}

	/**
	 * 功能：给notice赋值(银行间工作日)
	 * 作者：zls
	 * 日期：20210806
	 *
	 * @return
	 */
	public T8ProdNetValueNotice setNetValDayPropForNet(String netValueDate, String prodCode, String disclosureDate, int addDays, String prodInfoId) throws Exception {
		T8ProdNetValueNotice notice = new T8ProdNetValueNotice();
		notice.setNetvalDate(netValueDate);//净值日期
		if (StringUtils.isEmpty(disclosureDate)) {//传入的披露日期不为空
			disclosureDate = t8ProdNetvalDaysService.getDisclosureDateByDays(netValueDate, addDays, prodInfoId);
		}
		notice.setMasterNetvalDate(disclosureDate);
		notice.setDisclosureDate(disclosureDate);//披露日期
		notice.setStatus("0");//状态  未校验
		notice.setProdCode(prodCode);//产品代码
		return notice;
	}
}
