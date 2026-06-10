package com.kayak.rpt.zz.manage.service;

import cn.hutool.core.bean.BeanUtil;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.rpt.zz.manage.dao.SysDictDao;
import com.kayak.rpt.zz.manage.dao.TrPractyRegistInfoDao;
import com.kayak.rpt.zz.manage.enums.ExcelEnum;
import com.kayak.rpt.zz.manage.enums.OperatorEnum;
import com.kayak.rpt.zz.manage.model.ExcelToMapInfo;
import com.kayak.rpt.zz.manage.model.TrPractyRegistInfo;
import com.kayak.rpt.zz.operate.model.PractyRegist;
import com.kayak.rpt.zz.operate.service.PractyRegistService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "从业人员登记信息服务", model = TrPractyRegistInfo.class)
public class TrPractyRegistInfoService {

	@Autowired
	private TrPractyRegistInfoDao trPractyRegistInfoDao;
	@Autowired
	private SysDictDao sysDictDao;
	@Autowired
	private ExcelToMapService excelToMapService;
	@Autowired
	private PractyRegistService practyRegistService;


	@API(desc = "查询从业人员登记信息信息", auth = APIAuth.YES, operation = APIOperation.SELECT)
	public SqlResult<TrPractyRegistInfo> findTrPractyRegistInfos(SqlParam<TrPractyRegistInfo> params) throws Exception {
		params.setMakeSql(true);
		return trPractyRegistInfoDao.findTrPractyRegistInfos(params);
	}

	@API(desc = "查询从业人员登记信息数据", auth = APIAuth.YES, operation = APIOperation.SELECT)
	public SqlResult<TrPractyRegistInfo> findTrPractyRegistInfosAndIsError(SqlParam<TrPractyRegistInfo> params) throws Exception {
		params.setMakeSql(true);
		SqlResult<TrPractyRegistInfo> trPractyRegistInfosAndIsError = trPractyRegistInfoDao.findTrPractyRegistInfosAndIsError(params);
		return trPractyRegistInfosAndIsError;
	}

	@API(desc = "添加从业人员登记信息", params = "profession,name,sex,bank_code,id_code,iss_branch_type,region,firm_name,department,post,education,degree,career_start_date,wealth_start_date,profess_qualy_level,wealth_cer,regist_cer_no,reward,telphone,mobile,email,register_classify,regist_type,details,register_serno,imp_date,register_date,register_status", auth = APIAuth.YES)
	public int addTrPractyRegistInfo(SqlParam<TrPractyRegistInfo> params) throws Exception {
		// 操作记录
		practyRegistService.addPractyRegist(params, OperatorEnum.CREATE.getVal());
		return trPractyRegistInfoDao.addTrPractyRegistInfo(params).getEffect();
	}

	@API(desc = "修改从业人员登记信息", params = "profession,name,sex,bank_code,id_code,iss_branch_type,region,firm_name,department,post,education,degree,career_start_date,wealth_start_date,profess_qualy_level,wealth_cer,regist_cer_no,reward,telphone,mobile,email,register_classify,regist_type,details,register_serno,imp_date,register_date,register_status", auth = APIAuth.YES)
	public int updateTrPractyRegistInfo(SqlParam<TrPractyRegistInfo> params) throws Exception {
		// 操作记录
		practyRegistService.addPractyRegist(params, OperatorEnum.UPDATE.getVal());
		return trPractyRegistInfoDao.updateTrPractyRegistInfo(params).getEffect();
	}

	@API(desc = "删除从业人员登记信息", params = "profession,name,sex,bank_code,id_code,iss_branch_type,region,firm_name,department,post,education,degree,career_start_date,wealth_start_date,profess_qualy_level,wealth_cer,regist_cer_no,reward,telphone,mobile,email,register_classify,regist_type,details,register_serno,imp_date,register_date,register_status", auth = APIAuth.YES)
	public int deleteTrPractyRegistInfo(SqlParam<TrPractyRegistInfo> params) throws Exception {
		// 操作记录
		practyRegistService.addPractyRegist(params, OperatorEnum.DELETE.getVal());
		return trPractyRegistInfoDao.deleteTrPractyRegistInfo(params).getEffect();
	}




	public static void exportFile(HttpServletResponse response, String fileName) {
		// 第一种获取静态资源
		ClassPathResource classPathResource = new ClassPathResource("static/" + fileName);// "static/excleTemplate/ImportModel.xlsx"
		// 第二种获取静态资源
		// InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("static/excleTemplate/" + fileName);
		// 第三种获取静态资源
		// InputStream inputStream = this.getClass().getResourceAsStream("static/excleTemplate/" + fileName);
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = classPathResource.getInputStream();
			outputStream = response.getOutputStream();
			int BUFFER_SIZE = 1024 * 4;
			byte[] buffer = new byte[BUFFER_SIZE];
			int reader = 0;
			while ((reader = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, reader);
			}
			response.setContentType("application/octet-stream");
			response.setCharacterEncoding("utf-8");
			String newFileName = URLEncoder.encode(classPathResource.getFilename(), "UTF-8");
			response.setHeader("Content-disposition", "attachment;filename=" + newFileName);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (outputStream != null) {
					/**flush():仅仅刷新缓冲区(一般写字符时要用,因为字符时先进入缓冲区),然后将内存中的数据立刻写出(因为缓冲区是装满之后才会写出
					 ,用flush()就不必等到缓冲区满,立刻写出,流对象还可以继续使用) */
					outputStream.flush();
					/**close():关闭流对象. 也会先刷新一次缓冲区,再关闭. 关闭之后,流对象不可以继续使用 */
					outputStream.close();
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}



	@API(desc = "批量导入数据", auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public String batchImport(String fileName, MultipartFile file) throws Exception {

		// 返回提示
		String resResult = "";
		Boolean lastResult = false;

		// 数据流转表格
		Workbook wb =  new XSSFWorkbook(file.getInputStream());

		// 获取第一页签
		Sheet sheet = wb.getSheetAt(0);


		List<ExcelToMapInfo> list =  new ArrayList<ExcelToMapInfo>();



		ExcelToMapInfo profession =  new ExcelToMapInfo();
		profession.setDict("tr_profession");  //字典值
		profession.setFieldIndex(0); // EXCEL列位置
		profession.setFieldName("从业人员类型");
		profession.setField("profession"); //字段
		profession.setFieldType(ExcelEnum.ENUM); //数据类型
		profession.setLength(0); //长度校验 0 不校验
		profession.setNotNULL(true); // 是否非空
		list.add(profession);

		ExcelToMapInfo name =  new ExcelToMapInfo();
		name.setFieldIndex(1);// EXCEL列位置
		name.setFieldName("姓名");
		name.setField("name"); //字段
		name.setFieldType(ExcelEnum.TEXT);//数据类型
		name.setLength(0); //长度校验 0 不校验
		name.setNotNULL(true);// 是否非空
		list.add(name);

		ExcelToMapInfo sex =  new ExcelToMapInfo();
		sex.setDict("tr_sex");  //字典值
		sex.setFieldIndex(2); // EXCEL列位置
		sex.setFieldName("性别");
		sex.setField("sex"); //字段
		sex.setFieldType(ExcelEnum.ENUM); //数据类型
		sex.setLength(0); //长度校验 0 不校验
		sex.setNotNULL(true); // 是否非空
		list.add(sex);

		ExcelToMapInfo bankCode =  new ExcelToMapInfo();
		bankCode.setFieldIndex(3); // EXCEL列位置
		bankCode.setFieldName("发行机构代码");
		bankCode.setField("bankCode"); //字段
		bankCode.setFieldType(ExcelEnum.TEXT); //数据类型
		bankCode.setLength(0); //长度校验 0 不校验
		bankCode.setNotNULL(false); // 是否非空
		list.add(bankCode);

		ExcelToMapInfo idCode =  new ExcelToMapInfo();
		idCode.setFieldIndex(4); // EXCEL列位置
		idCode.setFieldName("身份证号");
		idCode.setField("idCode"); //字段
		idCode.setFieldType(ExcelEnum.NUM); //数据类型
		idCode.setLength(0); //长度校验 0 不校验
		idCode.setNotNULL(true); // 是否非空
		list.add(idCode);


		ExcelToMapInfo issBranchType =  new ExcelToMapInfo();
		issBranchType.setDict("sys_orglevel");  //字典值
		issBranchType.setFieldIndex(5); // EXCEL列位置
		issBranchType.setFieldName("所属总行或分行");
		issBranchType.setField("issBranchType"); //字段
		issBranchType.setFieldType(ExcelEnum.ENUM); //数据类型
		issBranchType.setLength(0); //长度校验 0 不校验
		issBranchType.setNotNULL(true); // 是否非空
		list.add(issBranchType);

		ExcelToMapInfo region =  new ExcelToMapInfo();
		region.setDict("t8_stock_people_area");  //字典值
		region.setFieldIndex(6); // EXCEL列位置
		region.setFieldName("所属区域");
		region.setField("region"); //字段
		region.setFieldType(ExcelEnum.ENUM); //数据类型
		region.setLength(0); //长度校验 0 不校验
		region.setNotNULL(true); // 是否非空
		list.add(region);

		ExcelToMapInfo firmName =  new ExcelToMapInfo();
		firmName.setFieldIndex(7); // EXCEL列位置
		firmName.setFieldName("具体单位名称");
		firmName.setField("firmName"); //字段
		firmName.setFieldType(ExcelEnum.TEXT); //数据类型
		firmName.setLength(0); //长度校验 0 不校验
		firmName.setNotNULL(false); // 是否非空
		list.add(firmName);

		ExcelToMapInfo department =  new ExcelToMapInfo();
		department.setFieldIndex(8); // EXCEL列位置
		department.setFieldName("所属部门");
		department.setField("department"); //字段
		department.setFieldType(ExcelEnum.TEXT); //数据类型
		department.setLength(0); //长度校验 0 不校验
		department.setNotNULL(false); // 是否非空
		list.add(department);

		ExcelToMapInfo post =  new ExcelToMapInfo();
		post.setFieldIndex(9); // EXCEL列位置
		post.setFieldName("职位");
		post.setField("post"); //字段
		post.setFieldType(ExcelEnum.TEXT); //数据类型
		post.setLength(0); //长度校验 0 不校验
		post.setNotNULL(false); // 是否非空
		list.add(post);


		ExcelToMapInfo education =  new ExcelToMapInfo();
		education.setDict("t8_education");  //字典值
		education.setFieldIndex(10); // EXCEL列位置
		education.setFieldName("学历");
		education.setField("education"); //字段
		education.setFieldType(ExcelEnum.ENUM); //数据类型
		education.setLength(0); //长度校验 0 不校验
		education.setNotNULL(true); // 是否非空
		list.add(education);


		ExcelToMapInfo degree =  new ExcelToMapInfo();
		degree.setDict("t8_degree");  //字典值
		degree.setFieldIndex(11); // EXCEL列位置
		degree.setFieldName("学位");
		degree.setField("degree"); //字段
		degree.setFieldType(ExcelEnum.ENUM); //数据类型
		degree.setLength(0); //长度校验 0 不校验
		degree.setNotNULL(true); // 是否非空
		list.add(degree);

		ExcelToMapInfo careerStartDate =  new ExcelToMapInfo();
		careerStartDate.setFieldIndex(12); // EXCEL列位置
		careerStartDate.setFieldName("首次参加工作时间");
		careerStartDate.setField("careerStartDate"); //字段
		careerStartDate.setFieldType(ExcelEnum.DATE); //数据类型
		careerStartDate.setLength(6); //长度校验 0 不校验
		careerStartDate.setNotNULL(true); // 是否非空
		list.add(careerStartDate);


		ExcelToMapInfo wealthStartDate =  new ExcelToMapInfo();
		wealthStartDate.setFieldIndex(13); // EXCEL列位置
		wealthStartDate.setFieldName("首次从事理财业务时间");
		wealthStartDate.setField("wealthStartDate"); //字段
		wealthStartDate.setFieldType(ExcelEnum.DATE); //数据类型
		wealthStartDate.setLength(6); //长度校验 0 不校验
		wealthStartDate.setNotNULL(true); // 是否非空
		list.add(wealthStartDate);

		ExcelToMapInfo professQualyLevel =  new ExcelToMapInfo();
		professQualyLevel.setDict("t8_professional_titles");  //字典值
		professQualyLevel.setFieldIndex(14); // EXCEL列位置
		professQualyLevel.setFieldName("专业技术职称");
		professQualyLevel.setField("professQualyLevel"); //字段
		professQualyLevel.setFieldType(ExcelEnum.ENUM); //数据类型
		professQualyLevel.setLength(0); //长度校验 0 不校验
		professQualyLevel.setNotNULL(true); // 是否非空
		list.add(professQualyLevel);

		ExcelToMapInfo wealthCer =  new ExcelToMapInfo();
		wealthCer.setDict("t8_financial_certificate");  //字典值
		wealthCer.setFieldIndex(15); // EXCEL列位置
		wealthCer.setFieldName("理财专业证书");
		wealthCer.setField("wealthCer"); //字段
		wealthCer.setFieldType(ExcelEnum.ENUM); //数据类型
		wealthCer.setLength(0); //长度校验 0 不校验
		wealthCer.setNotNULL(true); // 是否非空
		list.add(wealthCer);

		ExcelToMapInfo registCerNo =  new ExcelToMapInfo();
		registCerNo.setFieldIndex(16); // EXCEL列位置
		registCerNo.setFieldName("理财登记培训证书编号");
		registCerNo.setField("registCerNo"); //字段
		registCerNo.setFieldType(ExcelEnum.TEXT); //数据类型
		registCerNo.setLength(0); //长度校验 0 不校验
		registCerNo.setNotNULL(false); // 是否非空
		list.add(registCerNo);

		ExcelToMapInfo reward =  new ExcelToMapInfo();
		reward.setFieldIndex(17); // EXCEL列位置
		reward.setFieldName("所获奖励");
		reward.setField("reward"); //字段
		reward.setFieldType(ExcelEnum.TEXT); //数据类型
		reward.setLength(0); //长度校验 0 不校验
		reward.setNotNULL(false); // 是否非空
		list.add(reward);

		ExcelToMapInfo telphone =  new ExcelToMapInfo();
		telphone.setFieldIndex(18); // EXCEL列位置
		telphone.setFieldName("办公电话");
		telphone.setField("telphone"); //字段
		telphone.setFieldType(ExcelEnum.TEXT); //数据类型
		telphone.setLength(0); //长度校验 0 不校验
		telphone.setNotNULL(false); // 是否非空
		list.add(telphone);

		ExcelToMapInfo mobile =  new ExcelToMapInfo();
		mobile.setFieldIndex(19); // EXCEL列位置
		mobile.setFieldName("移动电话");
		mobile.setField("mobile"); //字段
		mobile.setFieldType(ExcelEnum.TEXT); //数据类型
		mobile.setLength(0); //长度校验 0 不校验
		mobile.setNotNULL(false); // 是否非空
		list.add(mobile);

		ExcelToMapInfo email =  new ExcelToMapInfo();
		email.setFieldIndex(20); // EXCEL列位置
		email.setFieldName("电子邮箱");
		email.setField("email"); //字段
		email.setFieldType(ExcelEnum.TEXT); //数据类型
		email.setLength(0); //长度校验 0 不校验
		email.setNotNULL(false); // 是否非空
		list.add(email);

		ExcelToMapInfo registerClassify =  new ExcelToMapInfo();
		registerClassify.setDict("t8_register_type");  //字典值
		registerClassify.setFieldIndex(21); // EXCEL列位置
		registerClassify.setFieldName("登记业务分类");
		registerClassify.setField("registerClassify"); //字段
		registerClassify.setFieldType(ExcelEnum.ENUM); //数据类型
		registerClassify.setLength(0); //长度校验 0 不校验
		registerClassify.setNotNULL(true); // 是否非空
		list.add(registerClassify);

		ExcelToMapInfo registType =  new ExcelToMapInfo();
		registType.setDict("t8_register_person");  //字典值
		registType.setFieldIndex(22); // EXCEL列位置
		registType.setFieldName("登记人员类别");
		registType.setField("registType"); //字段
		registType.setFieldType(ExcelEnum.ENUM); //数据类型
		registType.setLength(0); //长度校验 0 不校验
		registType.setNotNULL(true); // 是否非空
		list.add(registType);

		ExcelToMapInfo details =  new ExcelToMapInfo();
		details.setFieldIndex(23); // EXCEL列位置
		details.setFieldName("备注");
		details.setField("details"); //字段
		details.setFieldType(ExcelEnum.TEXT); //数据类型
		details.setLength(0); //长度校验 0 不校验
		details.setNotNULL(false); // 是否非空
		list.add(details);


		Map<String,Object>   map  = excelToMapService.toMapAndCheck(list,sheet);

		boolean isError = (boolean) map.get("isError");

		if(isError){

			return RequestSupport.updateReturnJson(false, map.get("msg").toString(), null).toString();

		}
		List<Map<String,Object>> resList = (List<Map<String, Object>>) map.get("list");


		trPractyRegistInfoDao.addTrPractyRegistInfoBatch(resList);

		return RequestSupport.updateReturnJson(true, map.get("msg").toString(), null).toString();

	}


	// 判断 当前行是否为空
	public static boolean isEmptyRow(Row row) {
		if (row == null || row.toString().isEmpty()) {
			return true;
		} else {
			Iterator<Cell> it = row.iterator();
			boolean isEmpty = true;
			while (it.hasNext()) {
				Cell cell = it.next();
				if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
					isEmpty = false;
					break;
				}
			}
			return isEmpty;
		}
	}

	@API(desc = "导入从业人员登记信息管理", auth = APIAuth.YES)
	public void importtrPractyRegistInfo(List<TrPractyRegistInfo> trPractyRegistInfos,Map<String, Object> params) throws Exception {
		 trPractyRegistInfoDao.deleteImportTrPractyRegistInfo(params);
         for (TrPractyRegistInfo trPractyRegistInfo : trPractyRegistInfos) {
         	Map<String, Object> map = BeanUtil.beanToMap(trPractyRegistInfo);
			 addImporTrPractyRegistInfo(trPractyRegistInfo,OperatorEnum.IMPORT.getVal());
	 			trPractyRegistInfoDao.addImportTrPractyRegistInfo(map);
		 }
	}

	private int addImporTrPractyRegistInfo(TrPractyRegistInfo trPractyRegistInfo, String opType) throws Exception {
		TrPractyRegistInfo trPractyRegist = BeanUtil.copyProperties(trPractyRegistInfo, TrPractyRegistInfo.class);
		trPractyRegist.setOpType(opType);
		trPractyRegist.setSummitUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username")));
		return trPractyRegistInfoDao.addImporTrPractyRegistInfo(trPractyRegist).getEffect();
	}

}
