package com.kayak.pms.netValue.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.pms.disclosureControl.service.ScheduleNoticeService;
import com.kayak.pms.email.dao.T8DisChannelInfoDao;
import com.kayak.pms.netValue.dao.T8ProdNetValueNoticeDao;
import com.kayak.pms.netValue.dao.T8ProdNetValueTaskDao;
import com.kayak.pms.netValue.model.T8ProdNetValueNotice;
import com.kayak.pms.netValue.model.T8ProdNetValueTask;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;


@Service
@APIDefine(desc = "净值披露任务服务", model = T8ProdNetValueTask.class)
public class T8ProdNetValueTaskService {

	private static final String separate = "/";

	@Autowired
	private T8ProdNetValueTaskDao t8ProdNetValueTaskDao;

	@Autowired
	private T8ProdNetValueNoticeDao t8ProdNetValueNoticeDao;

	@Autowired
	private T8DisChannelInfoDao t8DisChannelInfoDao;
	@Autowired
	private ScheduleNoticeService scheduleNoticeService;
	private String fileStorePath;

	@Value("${path.word}")
	private  String winPath;

	@API(desc = "查询净值披露任务信息", auth = APIAuth.YES)
	public SqlResult<T8ProdNetValueTask> findT8ProdNetValueTasks(SqlParam<T8ProdNetValueTask> params) throws Exception {
//		params.setMakeSql(true);
		return t8ProdNetValueTaskDao.findT8ProdNetValueTasks(params);
	}
	@API(desc = "导出净值披露任务", auth = APIAuth.YES)
	public SqlResult<T8ProdNetValueTask> findNetValueProdTasksList(SqlParam<T8ProdNetValueTask> params) throws Exception {

		// 查询披露产品
		return  t8ProdNetValueTaskDao.findNetValueProdTasksList(params);
	}

	@API(desc = "净值披露提交审批流", auth = APIAuth.NO, operation = APIOperation.UPDATE)
	public int confirmNetValueTask(SqlParam<T8ProdNetValueTask> params) throws Exception {
		Map notice = RequestSupport.getParameters();
		String date = DateUtil.getNowDate();
		String time = DateUtil.getNowTime();
		String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));//用户编号
		String username = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username"));//姓名
		params.getModel().setConfirmDate(date);
		params.getModel().setConfirmTime(time);
		params.getModel().setConfirmUserId(userid);
		params.getModel().setConfirmUserName(username);
		params.getModel().setTaskStatus("1");//状态 1代表已确认
		return t8ProdNetValueTaskDao.confirmNetValueTask(params);
	}

	/**
	 * 功能：获取数据不完整净值产品集合
	 * 作者：zls
	 * 日期：20210804
	 * 测试接口
	 * @param params
	 * @throws Exception
	 */
	@API(desc = "获取数据不完整净值产品集合", auth = APIAuth.NO)
	public String getNavProduct(SqlParam<T8ProdNetValueTask> params) throws Exception {
		Map<String,Object> map = new HashMap<>();
		T8ProdNetValueNotice notice = new T8ProdNetValueNotice();
		String taskDate = params.getModel().getTaskDate();
		notice.setDisclosureDate(taskDate);
		String prodCode = t8ProdNetValueNoticeDao.findNetValueNoticeListForNotComplete(notice);
		if(Tools.isNotEmpty(prodCode)){
			map.put("prodCode",prodCode);
		}
		return RequestSupport.updateReturnJson(true, "获取不完整净值产品成功", map).toString();
	}

	public String generateNetValueNotice(T8ProdNetValueNotice notice) throws Exception {
		Date date = new Date();
		OutputStream out = null;
		String path = "";
		String s = "";
		String emptyProdCodeForMoney ="";
		String emptyProdCodeForNoMoney ="";
		String os = System.getProperty("os.name");
		if(os.toLowerCase().startsWith("win")){
			fileStorePath = winPath;
		}else{
			s="80000080003";
			path = SysUtil.getSystemParamsByParaid(s);
			fileStorePath = path + separate;
		}
		String fileSavePath = fileStorePath +separate+ "netValue" + separate + notice.getDisclosureDate();
		File localPathFile = new File(fileSavePath);
		//文件夹不存在的话创建文件夹
		if (!localPathFile.exists() && !localPathFile.isDirectory()) {
			localPathFile.mkdirs();
		}

		//{YYYYMMDD}净值报告
		String fileName = notice.getDisclosureDate()+"净值公告"+".xls";
		// 创建一个excel文件
		HSSFWorkbook book = new HSSFWorkbook();
		// 创建Sheet1对象
		HSSFSheet sheet = book.createSheet(notice.getDisclosureDate()+"现金管理类产品净值报告");
		sheet.setColumnWidth(0,4500);
		sheet.setColumnWidth(1, 4500);
		sheet.setColumnWidth(2, 4500);
		sheet.setColumnWidth(3, 4500);
		sheet.setColumnWidth(4, 4500);
		sheet.setColumnWidth(5, 4500);
		sheet.setColumnWidth(6, 4500);

		// 创建Sheet2对象
		HSSFSheet sheet2 = book.createSheet(notice.getDisclosureDate()+"非现金管理类产品净值报告");
		sheet2.setColumnWidth(0,4500);
		sheet2.setColumnWidth(1, 4500);
		sheet2.setColumnWidth(2, 4500);
		sheet2.setColumnWidth(3, 4500);
		sheet2.setColumnWidth(4, 4500);
		sheet2.setColumnWidth(5, 4500);
		sheet2.setColumnWidth(6, 4500);
		//合并的单元格样式
		HSSFCellStyle borderStyle = book.createCellStyle();
		//垂直居中
		borderStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		borderStyle.setAlignment(HorizontalAlignment.CENTER);
		//borderStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		//设置一个边框
		borderStyle.setBorderTop(HSSFBorderFormatting.BORDER_THICK);

		HSSFFont font = book.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 12);
		HSSFCellStyle style = book.createCellStyle();
		style.setFont(font);
		HSSFFont font2 = book.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font2.setFontName("宋体");
		font2.setFontHeightInPoints((short) 14);
		HSSFFont font3 = book.createFont();
		font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font3.setFontName("宋体");
		font3.setFontHeightInPoints((short) 12);
		font3.setColor((short)0xa);
		HSSFCellStyle style2 = book.createCellStyle();
		style2.setFont(font2);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFCellStyle style3 = book.createCellStyle();
		style3.setFont(font3);
		style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		List<T8ProdNetValueNotice> moneyList = t8ProdNetValueNoticeDao.findNetValueNoticeListForMoney(notice);
		List<T8ProdNetValueNotice> noMoneyList = t8ProdNetValueNoticeDao.findNetValueNoticeListForNoMoney(notice);
		HSSFRow row = null;
		HSSFCell cell = null;
		int rowIndex = 0;
		if (moneyList != null && moneyList.size()>0) {

			row = sheet.createRow(0);
			for(int i=0;i<moneyList.size();i++){
				row = sheet.createRow(rowIndex);
				if("公告类型".equals(moneyList.get(i).getProdCode())){
					row.createCell(0).setCellValue("");
					row.createCell(1).setCellValue("");
					row.createCell(2).setCellValue("");
					row.createCell(3).setCellValue("");
					row.createCell(4).setCellValue("");
					row.createCell(5).setCellValue("");
					row.createCell(6).setCellValue("");
					row.getCell(0).setCellValue(moneyList.get(i).getTotalVol());
					row.getCell(0).setCellStyle(style);
					// 合并单元格：参数：起始行, 终止行, 起始列, 终止列
					CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, 0, 6);
					sheet.addMergedRegion(cra);
					row.getCell(0).setCellStyle(style2);
					//注意：边框样式需要重新设置一下
					//RegionUtil.setBorderTop(HSSFBorderFormatting.BORDER_THICK, cra, sheet, book);
				}else{
					if (StringUtils.isNotEmpty(moneyList.get(i).getProdCode())){
						if("产品代码".equals(moneyList.get(i).getProdCode())){
							row.createCell(0).setCellValue(moneyList.get(i).getProdCode());
							row.getCell(0).setCellStyle(style);
						}else{
							row.createCell(0).setCellValue(moneyList.get(i).getProdCode());
						}
					}else{
						row.createCell(0).setCellValue("");
						//emptyProdCodeForMoney = emptyProdCodeForMoney+moneyList.get(i).getProdCode()+",";
					}
					if (StringUtils.isNotEmpty(moneyList.get(i).getProdName())){
						if("产品名称".equals(moneyList.get(i).getProdName())){
							row.createCell(1).setCellValue(moneyList.get(i).getProdName());
							row.getCell(1).setCellStyle(style);
						}else{
							row.createCell(1).setCellValue(moneyList.get(i).getProdName());
						}
					}else{
						row.createCell(1).setCellValue("");
						//emptyProdCodeForMoney = emptyProdCodeForMoney+moneyList.get(i).getProdCode()+",";
					}
					if (StringUtils.isNotEmpty(moneyList.get(i).getTotalVol())){
						if("产品总份额".equals(moneyList.get(i).getTotalVol())){
							row.createCell(2).setCellValue(moneyList.get(i).getTotalVol());
							row.getCell(2).setCellStyle(style);
						}else{
							row.createCell(2).setCellValue(moneyList.get(i).getTotalVol());
						}
					}else{
						row.createCell(2).setCellValue("");
						//emptyProdCodeForMoney = emptyProdCodeForMoney+moneyList.get(i).getProdCode()+",";
					}
					if (StringUtils.isNotEmpty(moneyList.get(i).getNavProfit())){
						if("本日收益".equals(moneyList.get(i).getNavProfit())){
							row.createCell(3).setCellValue(moneyList.get(i).getNavProfit());
							row.getCell(3).setCellStyle(style);
						}else{
							row.createCell(3).setCellValue(moneyList.get(i).getNavProfit());
						}
					}else{
						row.createCell(3).setCellValue("");
						//emptyProdCodeForMoney = emptyProdCodeForMoney+moneyList.get(i).getProdCode()+",";
					}
					if (StringUtils.isNotEmpty(moneyList.get(i).getTenThousandIncomeAmt())){
						if("每万份收益".equals(moneyList.get(i).getTenThousandIncomeAmt())){
							row.createCell(4).setCellValue(moneyList.get(i).getTenThousandIncomeAmt());
							row.getCell(4).setCellStyle(style);
						}else{
							row.createCell(4).setCellValue(moneyList.get(i).getTenThousandIncomeAmt());
						}
					}else{
						row.createCell(4).setCellValue("");
						emptyProdCodeForMoney = emptyProdCodeForMoney+moneyList.get(i).getProdCode()+",";
					}
					if (StringUtils.isNotEmpty(moneyList.get(i).getSevenDaysIncomeRate())){
						if("七日年化收益率".equals(moneyList.get(i).getSevenDaysIncomeRate())){
							row.createCell(5).setCellValue(moneyList.get(i).getSevenDaysIncomeRate());
							row.getCell(5).setCellStyle(style);
						}else{
							row.createCell(5).setCellValue(moneyList.get(i).getSevenDaysIncomeRate());
						}
					}else{
						row.createCell(5).setCellValue("");
						//emptyProdCodeForMoney = emptyProdCodeForMoney+moneyList.get(i).getProdCode()+",";
					}
					if (StringUtils.isNotEmpty(moneyList.get(i).getNetvalDate())){
						if("净值日期".equals(moneyList.get(i).getNetvalDate())){
							row.createCell(6).setCellValue(moneyList.get(i).getNetvalDate());
							row.getCell(6).setCellStyle(style);
						}else{
							row.createCell(6).setCellValue(moneyList.get(i).getNetvalDate());
						}
					}else{
						row.createCell(6).setCellValue("");
						//emptyProdCodeForMoney = emptyProdCodeForMoney+moneyList.get(i).getProdCode()+",";
					}
				}
				rowIndex++;
			}
		}
		if (!Tools.isEmpty(emptyProdCodeForMoney)) {
			row = sheet.createRow(rowIndex);
			row.createCell(0).setCellValue("");
			row.createCell(1).setCellValue("");
			row.createCell(2).setCellValue("");
			row.createCell(3).setCellValue("");
			row.createCell(4).setCellValue("");
			row.createCell(5).setCellValue("");
			row.createCell(6).setCellValue("");
			row.getCell(0).setCellValue("以下产品净值数据不完整:"+emptyProdCodeForMoney.substring(0,emptyProdCodeForMoney.length()-1));
			CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, 0, 6);
			sheet.addMergedRegion(cra);
			row.getCell(0).setCellStyle(style3);
		}




		HSSFRow row2 = null;
		HSSFCell cell2 = null;
		int rowIndex2 = 0;
		if (noMoneyList != null && noMoneyList.size()>0) {
			row2 = sheet2.createRow(0);
			for(int i=0;i<noMoneyList.size();i++){
				row2 = sheet2.createRow(rowIndex2);
				if("公告类型".equals(noMoneyList.get(i).getProdCode())){
					row2.createCell(0).setCellValue("");
					row2.createCell(1).setCellValue("");
					row2.createCell(2).setCellValue("");
					row2.createCell(3).setCellValue("");
					row2.createCell(4).setCellValue("");
					row2.createCell(5).setCellValue("");
					row2.createCell(6).setCellValue("");
					row2.getCell(0).setCellValue(noMoneyList.get(i).getTotalVol());
					row2.getCell(0).setCellStyle(style);
					// 合并单元格：参数：起始行, 终止行, 起始列, 终止列
					CellRangeAddress cra2 = new CellRangeAddress(rowIndex2, rowIndex2, 0, 6);
					sheet2.addMergedRegion(cra2);
					row2.getCell(0).setCellStyle(style2);
					//注意：边框样式需要重新设置一下
					//RegionUtil.setBorderTop(HSSFBorderFormatting.BORDER_THICK, cra, sheet, book);
				}else{
					if (StringUtils.isNotEmpty(noMoneyList.get(i).getProdCode())){
						if("产品代码".equals(noMoneyList.get(i).getProdCode())){
							row2.createCell(0).setCellValue(noMoneyList.get(i).getProdCode());
							row2.getCell(0).setCellStyle(style);
						}else{
							row2.createCell(0).setCellValue(noMoneyList.get(i).getProdCode());
						}
					}else{
						row2.createCell(0).setCellValue("");
						//emptyProdCodeForNoMoney = emptyProdCodeForNoMoney+noMoneyList.get(i).getProdCode()+",";
					}
					if (StringUtils.isNotEmpty(noMoneyList.get(i).getProdName())){
						if("产品名称".equals(noMoneyList.get(i).getProdName())){
							row2.createCell(1).setCellValue(noMoneyList.get(i).getProdName());
							row2.getCell(1).setCellStyle(style);
						}else{
							row2.createCell(1).setCellValue(noMoneyList.get(i).getProdName());
						}
					}else{
						row2.createCell(1).setCellValue("");
						//emptyProdCodeForNoMoney = emptyProdCodeForNoMoney+noMoneyList.get(i).getProdCode()+",";
					}
					if (StringUtils.isNotEmpty(noMoneyList.get(i).getTotalVol())){
						if("产品总份额".equals(noMoneyList.get(i).getTotalVol())){
							row2.createCell(2).setCellValue(noMoneyList.get(i).getTotalVol());
							row2.getCell(2).setCellStyle(style);
						}else{
							row2.createCell(2).setCellValue(noMoneyList.get(i).getTotalVol());
						}
					}else{
						row2.createCell(2).setCellValue("");
						//emptyProdCodeForNoMoney = emptyProdCodeForNoMoney+noMoneyList.get(i).getProdCode()+",";
					}
					if (StringUtils.isNotEmpty(noMoneyList.get(i).getNavProfit())){
						if("总净值".equals(noMoneyList.get(i).getNavProfit())){
							row2.createCell(3).setCellValue(noMoneyList.get(i).getNavProfit());
							row2.getCell(3).setCellStyle(style);
						}else{
							row2.createCell(3).setCellValue(noMoneyList.get(i).getNavProfit());
						}
					}else{
						row2.createCell(3).setCellValue("");
						//emptyProdCodeForNoMoney = emptyProdCodeForNoMoney+noMoneyList.get(i).getProdCode()+",";
					}
					if (StringUtils.isNotEmpty(noMoneyList.get(i).getTenThousandIncomeAmt())){
						if("单位净值".equals(noMoneyList.get(i).getTenThousandIncomeAmt())){
							row2.createCell(4).setCellValue(noMoneyList.get(i).getTenThousandIncomeAmt());
							row2.getCell(4).setCellStyle(style);
						}else{
							row2.createCell(4).setCellValue(noMoneyList.get(i).getTenThousandIncomeAmt());
						}
					}else{
						row2.createCell(4).setCellValue("");
						emptyProdCodeForNoMoney = emptyProdCodeForNoMoney+noMoneyList.get(i).getProdCode()+",";
					}
					if (StringUtils.isNotEmpty(noMoneyList.get(i).getSevenDaysIncomeRate())){
						if("累计净值".equals(noMoneyList.get(i).getSevenDaysIncomeRate())){
							row2.createCell(5).setCellValue(noMoneyList.get(i).getSevenDaysIncomeRate());
							row2.getCell(5).setCellStyle(style);
						}else{
							row2.createCell(5).setCellValue(noMoneyList.get(i).getSevenDaysIncomeRate());
						}
					}else{
						row2.createCell(5).setCellValue("");
						//emptyProdCodeForNoMoney = emptyProdCodeForNoMoney+noMoneyList.get(i).getProdCode()+",";
					}
					if (StringUtils.isNotEmpty(noMoneyList.get(i).getNetvalDate())){
						if("净值日期".equals(noMoneyList.get(i).getNetvalDate())){
							row2.createCell(6).setCellValue(noMoneyList.get(i).getNetvalDate());
							row2.getCell(6).setCellStyle(style);
						}else{
							row2.createCell(6).setCellValue(noMoneyList.get(i).getNetvalDate());
						}
					}else{
						row2.createCell(6).setCellValue("");
						//emptyProdCodeForNoMoney = emptyProdCodeForNoMoney+noMoneyList.get(i).getProdCode()+",";
					}
				}
				rowIndex2++;
			}
			if (!Tools.isEmpty(emptyProdCodeForNoMoney)) {
				row2 = sheet2.createRow(rowIndex2);
				row2.createCell(0).setCellValue("");
				row2.createCell(1).setCellValue("");
				row2.createCell(2).setCellValue("");
				row2.createCell(3).setCellValue("");
				row2.createCell(4).setCellValue("");
				row2.createCell(5).setCellValue("");
				row2.createCell(6).setCellValue("");
				row2.getCell(0).setCellValue("以下产品净值数据不完整:"+emptyProdCodeForNoMoney.substring(0,emptyProdCodeForNoMoney.length()-1));
				CellRangeAddress cra2 = new CellRangeAddress(rowIndex2, rowIndex2, 0, 6);
				sheet2.addMergedRegion(cra2);
				row2.getCell(0).setCellStyle(style3);
			}

		}

		OutputStream stream=null;
		try {
			stream = new FileOutputStream(new File(localPathFile, fileName));
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
		}
		path = localPathFile+separate+fileName;
		return path;
	}
}
