package com.kayak.dps.direct.service;

import com.kayak.core.sql.SqlRow;
import com.kayak.dps.direct.dao.CheckDataDao;
import com.kayak.dps.direct.model.ExFmt;
import com.kayak.dps.direct.util.DirectParams;
import com.kayak.dps.direct.util.DirectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CheckDataService {


	protected final static String REGISTERDATA_EXFMT_PRE = "ZZ_"; // base_ex_fmt 表主键前缀

	@Autowired
	public CheckDataDao checkDataDao;

	/**
	 * 合法性校验 201--投资者身份信息登记，
	 * @throws Exception
	 */
	public int custInfoChecking(String datatype , String workDate , String whiteregex) throws Exception{

		Pattern whiterpattern = null;
		if(!"".equals(whiteregex)){
			whiterpattern = Pattern.compile(whiteregex);
		}

		//白名单字符
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("workdate", workDate);

		/**
		 *得到文件字段配置信息 base_ex_fmt  ZZ_201
		 */
		List<SqlRow> listrst = checkDataDao.getExFmt(datatype);
		//删除当日错误信息描述表数据
		checkDataDao.deleteRegisterInfo(workDate);

		//重复的数据条数
		//理财证件类型、证件号码重复的客户
		List<SqlRow> register = checkDataDao.findRegisterInfoIdCode();
		String idCodes = "";
		for (SqlRow sqlRow : register) {
			idCodes += sqlRow.getString("id_code");
			idCodes += "," ;
		}
		if (register != null && register.size() > 0) {
			throw new Exception("数据检查不通过,共有" + register.size() + "位客户证件重复,证件号码为"
					+ idCodes + "的客户存在重复数据，请进行删除");
		}

		//存在证件类型、证件号码为已报送过的数据
		idCodes = checkDataDao.findRegisterInfoIdCodeByType();
		if (!"".equals(idCodes)) {
			throw new Exception("数据检查不通过,证件号码为" + idCodes + "的客户已报送过，请进行核查");
		}

		// 判断数据里面有没有重复的数据
		register = checkDataDao.findRegisterDataType();
		if (register != null && register.size() > 0) {
			throw new Exception("数据检查不通过,识别标识为"+register.get(0).getString("cust_no")+",数据类型为" +
					register.get(0).getString("data_type")+"存在重复数据，请进行删除");
		}


		int read_limit = 10000; //每次查询1000条
		int cnt = 0;
		int error_cnt = 0;

		while(true){
			params.put("start", cnt);
			params.put("limit", read_limit);
			List<SqlRow> findSql = checkDataDao.findRegisterInfo(workDate);
			int count = 0;
			String desc_flag = "0"; //是否有误
			Map<String,Object> param = new HashMap<>();
			for (SqlRow sqlRow: findSql) {
				count++;
				SqlRow correctRow = null;
				String cust_no = sqlRow.getString("cust_no");	//识别标识
				String cust_no_desc = "";
				//登记银行代码
				String bank_code = sqlRow.getString("bank_code");
				String bank_code_desc = "";
				String bank_code_s = DirectParams.bankCode;
//				Pattern p=Pattern.compile("^([Z]{1}\\d{5})");
//				Matcher m=p.matcher(bank_code.trim());
//				boolean flagBankCode = m.matches();
				if ("".equals(bank_code)){
					bank_code_desc = "登记银行代码要素不可为空。";
				}else if (!bank_code_s.equals(bank_code)){
					bank_code_desc = "登记银行代码必须与银行代码相同。";
				}
//				else if (!flagBankCode){
//					bank_code_desc = "登记银行代码要素格式不对。正确的格式：ZXXXXX（XXXXX为数字）。";
//				}

				//数据类型
				String data_type = (String) DirectUtils.getCorrectValue(correctRow, "data_type", sqlRow.get("data_type"));
				String data_type_desc = DirectUtils.checkDict("tr_data_type", data_type);
				if("".equals(data_type)){
					data_type_desc = "数据类型要素不可为空。";
				}else if (DirectUtils.conUtil(data_type,"01,02,03,04")<=-1){
					data_type_desc = "数据类型不在值域范围内。";
				}

				//该投资者是否属于本机构
				String is_belong = (String) DirectUtils.getCorrectValue(correctRow, "is_belong", sqlRow.get("is_belong"));
				String is_belong_desc = DirectUtils.checkDict("tr_is_belong", is_belong);
				/*modify qink 20210426 验证识别标识是否存在*/
				if(DirectUtils.conUtil(data_type,"02,03,04")>-1){
					is_belong_desc=checkDataDao.checkCustInfo(cust_no);
				}
				if(DirectUtils.conUtil(data_type,"02,04")>-1&&!"".equals(is_belong)){
					is_belong_desc = "当数据类型为识别标识变更或其他信息变更时，该投资者是否属于本机构要素必须为空。";
				}else if (DirectUtils.conUtil(is_belong,"01,02")<=-1&&!"".equals(is_belong)){
					is_belong_desc = "该投资者是否属于本机构不在值域范围内。";
				}
				//投资者所属银行名称
				String iss_bank_name = (String) DirectUtils.getCorrectValue(correctRow, "iss_bank_name", sqlRow.get("iss_bank_name"));
				String iss_bank_name_desc = DirectUtils.checkFMT(listrst,"iss_bank_name",iss_bank_name);
				if(DirectUtils.conUtil(data_type,"02,04")>-1&&!"".equals(iss_bank_name)){
					iss_bank_name_desc = "当数据类型为识别标识变更或其他信息变更时，投资者所属银行名称要素必须为空。";
				}else if(iss_bank_name.getBytes("GBK").length > 60){
					iss_bank_name_desc = "投资者所属银行名称过长。";
				}
				//投资者所属银行代码
				String iss_bank_code = (String) DirectUtils.getCorrectValue(correctRow, "iss_bank_code", sqlRow.get("iss_bank_code"));
				String iss_bank_code_desc = DirectUtils.checkFMT(listrst,"iss_bank_code",iss_bank_code);
				Pattern pissBankCode=Pattern.compile("^([CZ]{1}\\d{5})");
				Matcher missBankCode=pissBankCode.matcher(iss_bank_code.trim());
				boolean flagissBankCode = missBankCode.matches();
				if(DirectUtils.conUtil(data_type,"02,04")>-1&&!"".equals(iss_bank_code)){
					iss_bank_code_desc = "当数据类型为识别标识变更或其他信息变更时，投资者所属银行代码要素必须为空。";
				}
				if (!flagissBankCode && !"".equalsIgnoreCase(iss_bank_code)){
					iss_bank_code_desc = iss_bank_code_desc + "投资者所属银行代码要素格式不对,正确的格式：C/ZXXXXX（XXXXX为数字）。";
				}
				//投资者境内外标识
				String in_out_sign = (String) DirectUtils.getCorrectValue(correctRow, "in_out_sign", sqlRow.get("in_out_sign"));
				String in_out_sign_desc = DirectUtils.checkDict("tr_in_out_sign", in_out_sign);
				if(DirectUtils.conUtil(data_type,"01,03")>-1 && "".equals(in_out_sign)){
					in_out_sign_desc = "当数据类型为新增或重要信息变更时, 投资者境内外标识要素不可为空。";
				}else if(DirectUtils.conUtil(data_type,"02,04")>-1 && !"".equals(in_out_sign)){
					in_out_sign_desc = "当数据类型为识别标识变更或其他信息变更时, 投资者境内外标识要素必须为空。";
				}else if (DirectUtils.conUtil(in_out_sign,"01,02")<=-1 && !"".equals(in_out_sign)){
					in_out_sign_desc = "投资者境内外标识不在值域范围内。";
				}
				System.err.println(in_out_sign + " : " + in_out_sign_desc + " == " + DirectUtils.conUtil(in_out_sign,"01,02"));
				//投资者所属国家或地区
				String iss_country = (String) DirectUtils.getCorrectValue(correctRow, "iss_country", sqlRow.get("iss_country"));
				String iss_country_desc = DirectUtils.checkDict("tr_iss_country", iss_country);
				if(DirectUtils.conUtil(data_type,"01,03")>-1 && DirectUtils.conUtil(in_out_sign,"01")>-1 && !"".equals(iss_country)){
					iss_country_desc = "当数据类型为新增或重要信息变更时, 投资者境内外标识为境内时，投资者所属国家或地区要素必须为空。";
				}else if(DirectUtils.conUtil(data_type,"02,04")>-1 && !"".equals(iss_country)){
					iss_country_desc = "当数据类型为识别标识变更或其他信息变更时,投资者所属国家或地区要素必须为空。";
				}

				//原识别标识   识别标识
				String ori_cust_no = (String) DirectUtils.getCorrectValue(correctRow, "ori_cust_no", sqlRow.get("ori_cust_no"));
				String ori_cust_no_desc = DirectUtils.checkFMT(listrst,"ori_cust_no",ori_cust_no);
				Pattern pCustNo=Pattern.compile(whiteregex);
				Matcher mCustNo=pCustNo.matcher(cust_no+"");
				boolean flagCustNo = mCustNo.matches();
				if ("".equals(cust_no)){
					cust_no_desc = "识别标识要素不可为空。";
				}else if (cust_no.getBytes().length > 30){
					cust_no_desc = "识别标识过长。";
				}else if(!whiterpattern.matcher(cust_no).matches()){
					cust_no_desc = "识别标识只能含有白名单内的阿拉伯数字，英文字母，半角符号，全角符号。";
				}

				if(DirectUtils.conUtil(data_type,"02")>-1 && cust_no.equals(ori_cust_no)){
					ori_cust_no_desc = "原识别标识和识别标识不能相同。";
				}else if (DirectUtils.conUtil(data_type,"01")>-1 && !"".equals(ori_cust_no)){
					ori_cust_no_desc = "当数据类型为新增时，原识别标识要素必须为空。";
				}else if (DirectUtils.conUtil(data_type,"02")>-1 && "".equals(ori_cust_no)){
					ori_cust_no_desc = "当数据类型为识别标识变更时，原识别标识要素不可为空。";
				}else if (DirectUtils.conUtil(data_type,"03,04")>-1 && !"".equals(ori_cust_no)){
					ori_cust_no_desc = "当数据类型为重要信息变更或其他信息变更时，原识别标识要素必须为空。";
				}else if (ori_cust_no.getBytes().length > 30){
					ori_cust_no_desc = "原识别标识过长。";
				}

				//投资者类别
				boolean isCustType = true;
				String cust_type = (String) DirectUtils.getCorrectValue(correctRow, "cust_type", sqlRow.get("cust_type"));
				String cust_type_desc = "";
//						DirectUtils.checkDict("tr_cust_type", cust_type);
				if(DirectUtils.conUtil(data_type,"01,03")>-1 && "".equals(cust_type)){
					cust_type_desc = "当数据类型为新增或重要信息变更时，投资者类别要素不可为空。";
				}else if(DirectUtils.conUtil(data_type,"02,04")>-1 && !"".equals(cust_type)){
					cust_type_desc = "当数据类型为识别标识变更或其他信息变更时，投资者类别要素必须为空。";
				}else if (!"".equals(cust_type) && DirectUtils.conUtil(cust_type,"01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26")<=-1){
					cust_type_desc = "投资者类别不在值域范围内。";
					isCustType= false;
				}
				//个人证件类别
				String personal_id_type = (String) DirectUtils.getCorrectValue(correctRow, "personal_id_type", sqlRow.get("personal_id_type"));
				String personal_id_type_desc = DirectUtils.checkDict("tr_personal_id_type", personal_id_type);
				if(DirectUtils.conUtil(data_type,"01")>-1  && isCustType){//新增
					if(DirectUtils.conUtil(cust_type,"01,02,03") >-1 && "".equals(personal_id_type)){
						personal_id_type_desc = "当数据类型为新增、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) + "时，个人证件类别要素不可为空。";
					}else if(DirectUtils.conUtil(cust_type,"04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26")>-1
							&& !"".equals(personal_id_type)){
						personal_id_type_desc = "当数据类型为新增、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) + "时，个人证件类别要素必须为空。";
					}else if(DirectUtils.conUtil(in_out_sign,"02")>-1  && DirectUtils.conUtil(cust_type,"01,02,03") >-1 && DirectUtils.conUtil(personal_id_type,"21,22,23,24,99")<=-1){
						personal_id_type_desc = "当数据类型为新增、投资者境内外标识为境外、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) + "时，个人证件类别只能填写外国护照、港澳往来内地通行证、台湾往来内地通行证、外国人永久居留证、其他";
					}else if(DirectUtils.conUtil(in_out_sign,"01")>-1  && DirectUtils.conUtil(cust_type,"01,02,03") >-1 && DirectUtils.conUtil(personal_id_type,"00,01,02,10,11,12,13,14,15,16,17,20,99")<=-1){
						personal_id_type_desc = "当数据类型为新增、投资者境内外标识为境内、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) + "时，个人证件类别只能填写居民身份证、临时居民身份证、户口簿、军官证、警官证、文职干部证、士兵证、军事院校学员证、离休干部荣誉证、军官退休证、文职干部退休证、中华人民共和国护照、其他。";
					}
				}else if(DirectUtils.conUtil(data_type,"02")>-1){//识别标识变更
					if(!"".equals(personal_id_type)){
						personal_id_type_desc = "当数据类型为识别标识变更时，个人证件类别要素必须为空。";
					}
				}else if(DirectUtils.conUtil(data_type,"03")>-1  && isCustType){//重要信息变更
					if(DirectUtils.conUtil(cust_type,"01,02,03") >-1 && "".equals(personal_id_type)){
						personal_id_type_desc = "当数据类型为重要信息变更、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) + "时，个人证件类别要素不可为空。";
					}else if(DirectUtils.conUtil(cust_type,"04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26")>-1
							&& !"".equals(personal_id_type)){
						personal_id_type_desc = "当数据类型为重要信息变更、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) + "时，个人证件类别要素必须为空。";
					}else if(DirectUtils.conUtil(in_out_sign,"02")>-1 && DirectUtils.conUtil(cust_type,"01,02,03") >-1 && DirectUtils.conUtil(personal_id_type,"21,22,23,24,99")<=-1){
						personal_id_type_desc = "当数据类型为重要信息变更、投资者境内外标识为境外、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) + "时，个人证件类别只能填写外国护照、港澳往来内地通行证、台湾往来内地通行证、外国人永久居留证、其他。";
					}else if(DirectUtils.conUtil(in_out_sign,"01")>-1 && DirectUtils.conUtil(cust_type,"01,02,03") >-1 && DirectUtils.conUtil(personal_id_type,"00,01,02,10,11,12,13,14,15,16,17,20,99")<=-1){
						personal_id_type_desc = "当数据类型为重要信息变更、投资者境内外标识为境内、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) + "时，个人证件类别只能填写居民身份证、临时居民身份证、户口簿、军官证、警官证、文职干部证、士兵证、军事院校学员证、离休干部荣誉证、军官退休证、文职干部退休证、中华人民共和国护照、其他。";
					}
				}else if(DirectUtils.conUtil(data_type,"04")>-1){//其他信息变更
					if(!"".equals(personal_id_type)){
						personal_id_type_desc = "当数据类型为其他信息变更时，个人证件类别要素必须为空。";
					}
				}else if (!"".equals(personal_id_type) && DirectUtils.conUtil(personal_id_type,"00,01,02,10,11,12,13,14,15,16,17,20,21,22,23,24,99")<=-1){
					personal_id_type_desc = "个人证件类别不在值域范围内。";
				}

				//机构证件类别
				String organization_id_type = (String) DirectUtils.getCorrectValue(correctRow, "organization_id_type", sqlRow.get("organization_id_type"));
				String organization_id_type_desc = DirectUtils.checkDict("tr_organization_id_type", organization_id_type);
				if(DirectUtils.conUtil(data_type,"01")>-1 && isCustType){//新增
					if(DirectUtils.conUtil(cust_type,"01,02,03") >-1 && !"".equals(organization_id_type)){
						organization_id_type_desc = "当数据类型为新增、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) + "时，机构证件类别要素必须为空。";
					}else if(DirectUtils.conUtil(cust_type,"04,05,06,07,08,09,10,11,12,13,14,15,16,24,25,26")>-1  && "".equals(organization_id_type)){
						organization_id_type_desc = "当数据类型为新增、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) + "时，机构证件类别要素不可为空。";
					}else if(DirectUtils.conUtil(cust_type,"17,18,19,20,21,22,23")>-1 && DirectUtils.conUtil(organization_id_type,"34")< 0){
						organization_id_type_desc = "当数据类型为新增、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) + "时，机构证件类别只能填写SPV登记编码。";
					}else if(DirectUtils.conUtil(cust_type,"04,05,06,07,08,09,10,11,12,13,14,15,16,24,25,26")>-1 && DirectUtils.conUtil(organization_id_type,"34")>-1){
						organization_id_type_desc = "当数据类型为新增、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) + "时，机构证件类别不能填写SPV登记编码。";
					}
				}else if(DirectUtils.conUtil(data_type,"02")>-1){//识别标识变更
					if(!"".equals(organization_id_type)){
						organization_id_type_desc = "当数据类型为识别标识变更时，机构证件类别要素必须为空。";
					}
				}else if(DirectUtils.conUtil(data_type,"03")>-1 && isCustType){//重要信息变更
					if(DirectUtils.conUtil(cust_type,"01,02,03") >-1 && !"".equals(organization_id_type)){
						organization_id_type_desc = "当数据类型为重要信息变更、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) + "时，机构证件类别要素必须为空。";
					}else if(DirectUtils.conUtil(cust_type,"04,05,06,07,08,09,10,11,12,13,14,15,16,24,25,26")>-1 && "".equals(organization_id_type)){
						organization_id_type_desc = "当数据类型为重要信息变更、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) + "时，机构证件类别要素不可为空。";
					}else if(DirectUtils.conUtil(cust_type,"17,18,19,20,21,22,23")>-1 && DirectUtils.conUtil(organization_id_type,"34")<0){
						organization_id_type_desc = "当数据类型为重要信息变更、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) + "时，机构证件类别只能填写SPV登记编码。";
					}else if(DirectUtils.conUtil(cust_type,"04,05,06,07,08,09,10,11,12,13,14,15,16,24,25,26")>-1 && DirectUtils.conUtil(organization_id_type,"34")>-1){
						organization_id_type_desc = "当数据类型为重要信息变更、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) + "时，机构证件类别不能填写SPV登记编码。";
					}
				}else if(DirectUtils.conUtil(data_type,"04")>-1){//其他信息变更
					if(!"".equals(organization_id_type)){
						organization_id_type_desc = "当数据类型为其他信息变更时，机构证件类别要素必须为空。";
					}
				}else if (!"".equals(organization_id_type) && DirectUtils.conUtil(organization_id_type,"01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,99")<=-1){
					organization_id_type_desc = "机构证件类别不在值域范围内。";
				}
				//其他证件名称
				String other_id_name = (String) DirectUtils.getCorrectValue(correctRow, "other_id_name", sqlRow.get("other_id_name"));
				String other_id_name_desc = DirectUtils.checkFMT(listrst,"other_id_name",other_id_name);
				if(DirectUtils.conUtil(data_type,"01")>-1 && isCustType){//新增
					if(DirectUtils.conUtil(cust_type,"01,02,03") >-1 && "99".equals(personal_id_type) && "".equals(other_id_name)){
						other_id_name_desc = "当数据类型为新增、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) + "时、个人证件类别为其他时，其他证件名称要素不可为空。";
					}else if(DirectUtils.conUtil(cust_type,"04,05,06,07,08,09,10,11,12,13,14,15,16,24,25,26")>-1 && "99".equals(organization_id_type) && "".equals(other_id_name)){
						other_id_name_desc = "当数据类型为新增、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) + "时、机构证件类别为其他时，其他证件名称要素不可为空。";
					}else if(DirectUtils.conUtil(cust_type,"01,02,03") >-1 && !"99".equals(personal_id_type) && !"".equals(other_id_name)){
						other_id_name_desc = "当数据类型为新增、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) + "时，个人证件类别为其他以外的选项时，其他证件名称要素必须为空。";
					}else if(DirectUtils.conUtil(cust_type,"04,05,06,07,08,09,10,11,12,13,14,15,16,24,25,26")>-1 && !"99".equals(organization_id_type) && !"".equals(other_id_name)){
						other_id_name_desc = "当数据类型为新增、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) + "时，机构证件类别为其他以外的选项时，其他证件名称要素必须为空。";
					}else if(DirectUtils.conUtil(cust_type,"17,18,19,20,21,22,23")>-1 && !"".equals(other_id_name)){
						other_id_name_desc = "当数据类型为新增、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) + "时，其他证件名称要素必须为空。";
					}
				}else if(DirectUtils.conUtil(data_type,"02")>-1){//识别标识变更
					if(!"".equals(other_id_name)){
						other_id_name_desc = "当数据类型为识别标识变更时，其他证件名称要素必须为空。";
					}
				}else if(DirectUtils.conUtil(data_type,"03")>-1  && isCustType){//重要信息变更
					if(DirectUtils.conUtil(cust_type,"01,02,03") >-1 && "99".equals(personal_id_type) && "".equals(other_id_name)){
						other_id_name_desc = "当数据类型为重要信息变更、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) + "时、个人证件类别为其他时，其他证件名称要素不可为空。";
					}else if(DirectUtils.conUtil(cust_type,"04,05,06,07,08,09,10,11,12,13,14,15,16,24,25,26")>-1 && "99".equals(organization_id_type) && "".equals(other_id_name)){
						other_id_name_desc = "当数据类型为重要信息变更、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) + "时、机构证件类别为其他时，其他证件名称要素不可为空。";
					}else if(DirectUtils.conUtil(cust_type,"01,02,03") >-1 && !"99".equals(personal_id_type) && !"".equals(other_id_name)){
						other_id_name_desc = "当数据类型为重要信息变更、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) + "时，个人证件类别为其他以外的选项时，其他证件名称要素必须为空。";
					}else if(DirectUtils.conUtil(cust_type,"04,05,06,07,08,09,10,11,12,13,14,15,16,24,25,26")>-1 && !"99".equals(organization_id_type) && !"".equals(other_id_name)){
						other_id_name_desc = "当数据类型为重要信息变更、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) + "时，机构证件类别为其他以外的选项时，其他证件名称要素必须为空。";
					}else if(DirectUtils.conUtil(cust_type,"17,18,19,20,21,22,23")>-1 && !"".equals(other_id_name)){
						other_id_name_desc = "当数据类型为重要信息变更、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) + "时，其他证件名称要素必须为空。";
					}
				}else if(DirectUtils.conUtil(data_type,"04")>-1){//其他信息变更
					if(!"".equals(other_id_name)){
						other_id_name_desc = "当数据类型为其他信息变更时，其他证件名称要素必须为空。";
					}
				}else if (other_id_name.getBytes("GBK").length > 60){
					other_id_name_desc = "其他证件名称过长。";
				}
				//证件号码
				String id_code = (String) DirectUtils.getCorrectValue(correctRow, "id_code", sqlRow.get("id_code"));
				String id_code_desc = DirectUtils.checkFMT(listrst,"id_code",id_code);
				if(DirectUtils.conUtil(data_type,"01")>-1){//新增
					if("".equals(id_code)){
						id_code_desc = "当数据类型为新增时，证件号码要素不可为空。";
					}else if(DirectUtils.conUtil(cust_type,"04,05,06,07,08,09,10,11,12,13,14,15,16,24,25,26")>-1 && "01".equals(organization_id_type) && id_code.length() != 18){
						id_code_desc = "当数据类型为新增、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) +"、机构证件类别为法人和其他组织统一社会信用代码时，证件号码必须为固定长18位英文数字。";
					}else if(DirectUtils.conUtil(cust_type,"01,02,03") >-1 && "01".equals(personal_id_type)&& (id_code.length() != 15 && id_code.length() != 18)){
						id_code_desc = "当数据类型为新增、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) + "、个人证件类别为临时居民身份证时，证件号码必须为固定长15位或者18位英文数字。";
					}else if(DirectUtils.conUtil(cust_type,"01,02,03") >-1 && "00".equals(personal_id_type)&& (id_code.length() != 15 && id_code.length() != 18)){
						id_code_desc = "当数据类型为新增、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) + "、个人证件类别为居民身份证时，证件号码必须为固定长15位或者18位英文数字。";
					}
				}else if(DirectUtils.conUtil(data_type,"02")>-1){//识别标识变更
					if(!"".equals(id_code)){
						id_code_desc = "当数据类型为识别标识变更时，证件号码要素必须为空。";
					}
				}else if(DirectUtils.conUtil(data_type,"03")>-1){//重要信息变更
					if("".equals(id_code)){
						id_code_desc = "当数据类型为重要信息变更时，证件号码要素不可为空。";
					}else if(DirectUtils.conUtil(cust_type,"04,05,06,07,08,09,10,11,12,13,14,15,16,24,25,26")>-1 && DirectUtils.conUtil(personal_id_type,"01") >-1 && id_code.length() != 18){
						id_code_desc = "当数据类型为重要信息变更、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) + "、机构证件类别为法人和其他组织统一社会信用代码时，证件号码必须为固定长18位英文数字。";
					}else if(DirectUtils.conUtil(cust_type,"01,02,03") >-1 && DirectUtils.conUtil(personal_id_type,"00") >-1 && (id_code.length() != 15 && id_code.length() != 18)){
						id_code_desc = "当数据类型为重要信息变更、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) + "、个人证件类别为居民身份证时，证件号码必须为固定长15位或者18位英文数字。";
					}else if(DirectUtils.conUtil(cust_type,"01,02,03") >-1 && DirectUtils.conUtil(personal_id_type,"01") >-1 && ((id_code.length() != 15 && id_code.length() != 18))){
						id_code_desc = "当数据类型为重要信息变更、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) + "、个人证件类别为临时居民身份证时，证件号码必须为固定长15位或者18位英文数字。";
					}
				}else if(DirectUtils.conUtil(data_type,"04")>-1){//其他信息变更
					if(!"".equals(id_code)){
						id_code_desc = "当数据类型为其他信息变更时，证件号码要素必须为空。";
					}
				}else if (id_code.length() > 30){
					id_code_desc = "证件号码过长。";
				}

				//SPV资金托管账户开户行
				String spv_open_bank = (String) DirectUtils.getCorrectValue(correctRow, "spv_open_bank", sqlRow.get("spv_open_bank"));
				String spv_open_bank_desc = DirectUtils.checkFMT(listrst,"spv_open_bank",spv_open_bank);
				if(DirectUtils.conUtil(data_type,"01")>-1 && DirectUtils.conUtil(cust_type,"01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,24,25,26") >-1 && !"".equals(spv_open_bank)){//新增
					spv_open_bank_desc = "当数据类型为新增、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) + "时，SPV资金托管账户开户行要素必须为空。";
				}else if(DirectUtils.conUtil(data_type,"02")>-1){//识别标识变更
					if(!"".equals(spv_open_bank)){
						spv_open_bank_desc = "当数据类型为识别标识变更时，SPV资金托管账户开户行要素必须为空。";
					}
				}else if(DirectUtils.conUtil(data_type,"03")>-1 && DirectUtils.conUtil(cust_type,"01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,24,25,26") >-1 && !"".equals(spv_open_bank)){//重要信息变更
					spv_open_bank_desc = "当数据类型为重要信息变更、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) + "时，SPV资金托管账户开户行要素必须为空。";
				}else if(DirectUtils.conUtil(data_type,"04")>-1){//其他信息变更
					if(!"".equals(spv_open_bank)){
						spv_open_bank_desc = "当数据类型为其他信息变更时，SPV资金托管账户开户行要素必须为空。";
					}
				}else if ("".indexOf(spv_open_bank)<=-1){

				}

				//其他资金托管账户开户行
				String other_open_bank = (String) DirectUtils.getCorrectValue(correctRow, "other_open_bank", sqlRow.get("other_open_bank"));
				String other_open_bank_desc = DirectUtils.checkFMT(listrst,"other_open_bank",other_open_bank);
			if(DirectUtils.conUtil(data_type,"01")>-1){//新增
				if("01".equals(cust_type) && !"".equals(other_open_bank)){
					other_open_bank_desc = "当数据类型为新增、投资者类别为普通个人时，其他资金托管账户开户行要素必须为空。";
				}else if("17".equals(cust_type) && "C10102".equals(spv_open_bank) && !"".equals(other_open_bank)){
					other_open_bank_desc = "当数据类型为新增、投资者类别为信托产品、SPV资金托管账户开户行为中国工商银行股份有限公司时，其他资金托管账户开户行要素必须为空。";
				}
			}else if(DirectUtils.conUtil(data_type,"02")>-1){//识别标识变更
				if(!"".equals(other_open_bank)){
					other_open_bank_desc = "当数据类型为识别标识变更时，其他资金托管账户开户行要素必须为空。";
				}
			}else if(DirectUtils.conUtil(data_type,"03")>-1){//重要信息变更
				if("01".equals(cust_type) && !"".equals(other_open_bank)){
					other_open_bank_desc = "当数据类型为重要信息变更、投资者类别为普通个人时，其他资金托管账户开户行要素必须为空。";
				}else if("17".equals(cust_type) && "C10102".equals(spv_open_bank) && !"".equals(other_open_bank)){
					other_open_bank_desc = "当数据类型为重要信息变更、投资者类别为信托产品、SPV资金托管账户开户行为中国工商银行股份有限公司时，其他资金托管账户开户行要素必须为空。";
				}
			}else if(DirectUtils.conUtil(data_type,"04")>-1){//其他信息变更
				if(!"".equals(other_open_bank)){
					other_open_bank_desc = "当数据类型为其他信息变更时，其他资金托管账户开户行要素必须为空。";
				}
			}
				if (!(DirectUtils.conUtil(data_type,"01,03") > -1
						&& DirectUtils.conUtil(cust_type,"17,18,19,20,21,22,23") > -1
						&& "999999".equals(spv_open_bank))
						&& !"".equals(other_open_bank)) {
					other_open_bank_desc = "非特定条件，其他资金托管账户开户行要素必须为空.";
				}
				if (other_open_bank.getBytes("GBK").length > 60){
					other_open_bank_desc = other_open_bank_desc + "其他资金托管账户开户行过长。";
				}
				//投资者名称
				String cust_name =  (String) DirectUtils.getCorrectValue(correctRow, "cust_name", sqlRow.get("cust_name"));
				String cust_name_desc = DirectUtils.checkFMT(listrst,"cust_name",cust_name);
				if(DirectUtils.conUtil(data_type,"01,03")>-1 ){
					if("".equals(cust_name)){
						cust_name_desc = "当数据类型为新增或重要信息变更时，投资者名称要素不可为空。";
					}else{
						if(DirectUtils.conUtil(in_out_sign,"01")>-1&&cust_name.indexOf(" ")>-1){ //修复境外投资者名称含有空格的bug 程晓鹏 2018.08.20 moidfy
							cust_name_desc = "境内投资者名称中不得含有空格";
						}
					}
				}else if(DirectUtils.conUtil(data_type,"02,04")>-1 && !"".equals(cust_name)){
					cust_name_desc = "当数据类型为识别标识变更或其他信息变更时，投资者名称要素必须为空。";
				}else if (cust_name.getBytes("GBK").length > 200){
					cust_name_desc = "投资者名称过长。";
				}

				//性别
				String sex = (String) DirectUtils.getCorrectValue(correctRow, "sex", sqlRow.get("sex"));
				String sex_desc =  DirectUtils.checkDict("tr_sex", sex);
				if(DirectUtils.conUtil(data_type,"01,03")>-1 && DirectUtils.conUtil(cust_type,"01,02,03") >-1 && "".equals(sex)){
					sex_desc = "当数据类型为新增或重要信息变更、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) + "时，性别要素不可为空。";
				}else if(DirectUtils.conUtil(data_type,"01,03")>-1 &&
						DirectUtils.conUtil(cust_type,"04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26") >-1 && !"".equals(sex)){
					sex_desc = "当数据类型为新增或重要信息变更、投资者类别为"+ DirectUtils.getDictName("tr_cust_type",cust_type) + "时，性别要素必须为空。";
				}else if(DirectUtils.conUtil(data_type,"02,04")>-1 && !"".equals(sex)){
					sex_desc = "当数据类型为识别标识变更或其他信息变更时，性别要素必须为空。";
				}else if(!"".equals(sex) && DirectUtils.conUtil(sex,"01,02") <= -1){
					sex_desc = "性别不在值域范围内。";
				}

				//风险偏好
				String risk_level = (String) DirectUtils.getCorrectValue(correctRow, "risk_level", sqlRow.get("risk_level"));
				String risk_level_desc = DirectUtils.checkDict("tr_risk_level", risk_level);
				if(DirectUtils.conUtil(data_type,"02")>-1 && !"".equals(risk_level) ){
					risk_level_desc = "当数据类型为识别标识变更时，风险偏好要素必须为空。";
				}else if(!"".equals(risk_level) && DirectUtils.conUtil(risk_level,"01,02,03,04,05") <= -1){
					risk_level_desc = "风险偏好不在值域范围内。";
				}

				//手机号码
				String moble = (String) DirectUtils.getCorrectValue(correctRow, "moble", sqlRow.get("moble"));
				String moble_desc =  DirectUtils.checkFMT(listrst,"moble",moble);
				Pattern pMobile=Pattern.compile("^[\u0030-\u0039]{11}$");
				Matcher mMobile=pMobile.matcher(moble+"");
				boolean flagMobile = mMobile.find();
				if(DirectUtils.conUtil(data_type,"01,03")>-1 && "".equals(moble)){
					moble_desc = "当数据类型为新增或重要信息变更时，手机号码要素不可为空。";
				}else if(DirectUtils.conUtil(data_type,"02")>-1 && !"".equals(moble)){
					moble_desc = "当数据类型为识别标识变更时，手机号码要素必须为空。";
				}else if(!flagMobile && !"".equals(moble)){
					moble_desc = "手机号码不合法,合法的模式：13488996666（11位）";
				}

				//固定电话
				String tel_phone = (String) DirectUtils.getCorrectValue(correctRow, "tel_phone", sqlRow.get("tel_phone"));
				String tel_phone_desc =  DirectUtils.checkFMT(listrst,"tel_phone",tel_phone);
				String reg="(?:(\\(\\+?86\\))(0[0-9]{2,3}\\-?)?([0-9][0-9]{6,7})+(\\-[0-9]{1,4})?)|"
						+ "(?:(86-?)?(0[0-9]{2,3}\\-?)?([0-9][0-9]{6,7})+(\\-[0-9]{1,8})?)";
				boolean flagTel = Pattern.matches(reg, tel_phone);
				if(DirectUtils.conUtil(data_type,"01,03")>-1 && "".equals(tel_phone)){
					tel_phone_desc = "当数据类型为新增或重要信息变更时，固定电话要素不可为空。";
				}else if(DirectUtils.conUtil(data_type,"02")>-1 && !"".equals(tel_phone) ){
					tel_phone_desc = "当数据类型为识别标识变更时，固定电话要素必须为空。";
				}else if(tel_phone.length() > 30){
					tel_phone_desc = "固定电话过长。";
				}else if(!flagTel && !"".equals(tel_phone)){
					tel_phone_desc = "固定电话格式有误。";
				}

				//电子邮箱
				String email = (String) DirectUtils.getCorrectValue(correctRow, "email", sqlRow.get("email"));
				String email_desc =  DirectUtils.checkFMT(listrst,"email",email);
				Pattern pEamil=Pattern.compile("^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$");
				Matcher mEmail=pEamil.matcher(email+"");
				boolean flagEmail = mEmail.find();
				if(DirectUtils.conUtil(data_type,"01,03")>-1 && "".equals(email)){
					email_desc = "当数据类型为新增或重要信息变更时，电子邮箱要素不可为空。";
				}else if(DirectUtils.conUtil(data_type,"02")>-1 && !"".equals(email) ){
					email_desc = "当数据类型为识别标识变更时，电子邮箱要素必须为空。";
				}else if(email.length() > 50){
					email_desc = "电子邮箱过长。";
				}else if(!flagEmail && !"".equals(email)){
					email_desc = "电子邮箱格式有误。";
				}

				//同空校验
				if(DirectUtils.conUtil(data_type,"04")>-1 ){
					if("".equals(moble) && "".equals(tel_phone) && "".equals(email)&&"".equals(risk_level)){
						email_desc = "当数据类型为其他信息变更时，风险偏好、手机号码、固定电话、电子邮箱不可同时为空";
					}
				}

				//备注
				String remark = (String) DirectUtils.getCorrectValue(correctRow, "remark", sqlRow.get("remark"));
				String remark_desc =  DirectUtils.checkFMT(listrst,"remark",remark);
				if(DirectUtils.conUtil(data_type,"02")>-1 && !"".equals(remark)){
					remark_desc =  "当数据类型为识别标识变更时，投资者信息备注要素必须为空。";
				}else if(!"".equals(remark) && remark.getBytes("GBK").length > 256){
					remark_desc =  "投资者信息备注过长。";
				}
				if(!"".equals(whiteregex)){
					if(!bank_code.equals("")&&!whiterpattern.matcher(bank_code).matches()) bank_code_desc=bank_code_desc+"登记银行代码必须填写白名单中的文字";
					if(!is_belong.equals("")&&!whiterpattern.matcher(is_belong).matches()) is_belong_desc=is_belong_desc+"投资者是否属于本行必须填写白名单中的文字";
					if(!iss_bank_name.equals("")&&!whiterpattern.matcher(iss_bank_name).matches()) iss_bank_name_desc=iss_bank_name_desc+"投资者所属银行名称必须填写白名单中的文字";
					if(!iss_bank_code.equals("")&&!whiterpattern.matcher(iss_bank_code).matches()) iss_bank_code_desc=iss_bank_code_desc+"投资者所属银行代码必须填写白名单中的文字";
					if(!in_out_sign.equals("")&&!whiterpattern.matcher(in_out_sign).matches()) in_out_sign_desc=in_out_sign_desc+"投资者境内外标识必须填写白名单中的文字";
					if(!iss_country.equals("")&&!whiterpattern.matcher(iss_country).matches()) iss_country_desc=iss_country_desc+"投资者所属国家或地区必须填写白名单中的文字";
					if(!data_type.equals("")&&!whiterpattern.matcher(data_type).matches()) data_type_desc=data_type_desc+"数据类型必须填写白名单中的文字";
					if(!ori_cust_no.equals("")&&!whiterpattern.matcher(ori_cust_no).matches()) ori_cust_no_desc=ori_cust_no_desc+"原识别标识必须填写白名单中的文字";
					if(!cust_no.equals("")&&!whiterpattern.matcher(cust_no).matches()) cust_no_desc=cust_no_desc+"识别标识必须填写白名单中的文字";
					if(!cust_type.equals("")&&!whiterpattern.matcher(cust_type).matches()) cust_type_desc=cust_type_desc+"投资者类别必须填写白名单中的文字";
					if(!personal_id_type.equals("")&&!whiterpattern.matcher(personal_id_type).matches()) personal_id_type_desc=personal_id_type_desc+"个人证件类别必须填写白名单中的文字";
					if(!organization_id_type.equals("")&&!whiterpattern.matcher(organization_id_type).matches()) organization_id_type_desc=organization_id_type_desc+"机构证件类别必须填写白名单中的文字";
					if(!other_id_name.equals("")&&!whiterpattern.matcher(other_id_name).matches()) other_id_name_desc=other_id_name_desc+"其他证件名称必须填写白名单中的文字";
					if(!id_code.equals("")&&!whiterpattern.matcher(id_code).matches()) id_code_desc=id_code_desc+"证件号码必须填写白名单中的文字";
					if(!spv_open_bank.equals("")&&!whiterpattern.matcher(spv_open_bank).matches()) spv_open_bank_desc=spv_open_bank_desc+"SPV资金托管账户开户行必须填写白名单中的文字";
					if(!other_open_bank.equals("")&&!whiterpattern.matcher(other_open_bank).matches()) other_open_bank_desc=other_open_bank_desc+"其他资金托管账户开户行必须填写白名单中的文字";
					if(!cust_name.equals("")&&!whiterpattern.matcher(cust_name).matches()) cust_name_desc=cust_name_desc+"投资者名称必须填写白名单中的文字";
					if(!sex.equals("")&&!whiterpattern.matcher(sex).matches()) sex_desc=sex_desc+"性别必须填写白名单中的文字";
					if(!risk_level.equals("")&&!whiterpattern.matcher(risk_level).matches()) risk_level_desc=risk_level_desc+"风险偏好必须填写白名单中的文字";
					if(!moble.equals("")&&!whiterpattern.matcher(moble).matches()) moble_desc=moble_desc+"手机号码必须填写白名单中的文字";
					if(!tel_phone.equals("")&&!whiterpattern.matcher(tel_phone).matches()) tel_phone_desc=tel_phone_desc+"固定电话必须填写白名单中的文字";
					if(!email.equals("")&&!whiterpattern.matcher(email).matches()) email_desc=email_desc+"电子邮箱必须填写白名单中的文字";
					if(!remark.equals("")&&!whiterpattern.matcher(remark).matches()) remark_desc=remark_desc+"备注必须填写白名单中的文字";
				}
				desc_flag = ("".equals(bank_code_desc))?desc_flag:"1";
				desc_flag = ("".equals(data_type_desc))?desc_flag:"1";
				desc_flag = ("".equals(is_belong_desc))?desc_flag:"1";
				desc_flag = ("".equals(iss_bank_name_desc))?desc_flag:"1";
				desc_flag = ("".equals(iss_bank_code_desc))?desc_flag:"1";
				desc_flag = ("".equals(in_out_sign_desc))?desc_flag:"1";
				desc_flag = ("".equals(iss_country_desc))?desc_flag:"1";
				desc_flag = ("".equals(cust_no_desc))?desc_flag:"1";
				desc_flag = ("".equals(ori_cust_no_desc))?desc_flag:"1";
				desc_flag = ("".equals(cust_type_desc))?desc_flag:"1";
				desc_flag = ("".equals(personal_id_type_desc))?desc_flag:"1";
				desc_flag = ("".equals(organization_id_type_desc))?desc_flag:"1";
				desc_flag = ("".equals(other_id_name_desc))?desc_flag:"1";
				desc_flag = ("".equals(id_code_desc))?desc_flag:"1";
				desc_flag = ("".equals(spv_open_bank_desc))?desc_flag:"1";
				desc_flag = ("".equals(other_open_bank_desc))?desc_flag:"1";
				desc_flag = ("".equals(cust_name_desc))?desc_flag:"1";
				desc_flag = ("".equals(sex_desc))?desc_flag:"1";
				desc_flag = ("".equals(risk_level_desc))?desc_flag:"1";
				desc_flag = ("".equals(moble_desc))?desc_flag:"1";
				desc_flag = ("".equals(tel_phone_desc))?desc_flag:"1";
				desc_flag = ("".equals(email_desc))?desc_flag:"1";
				desc_flag = ("".equals(remark_desc))?desc_flag:"1";

				if(desc_flag.equals("1")){//有错误描述,插入差错描述表
					error_cnt ++;

					param.put("bank_code_desc",bank_code_desc);
					param.put("is_belong_desc",is_belong_desc);
					param.put("iss_bank_name_desc",iss_bank_name_desc);
					param.put("iss_bank_code_desc",iss_bank_code_desc);
					param.put("in_out_sign_desc",in_out_sign_desc);
					param.put("iss_country_desc",iss_country_desc);
					param.put("data_type_desc",data_type_desc);
					param.put("cust_no_desc",cust_no_desc);
					param.put("ori_cust_no_desc",ori_cust_no_desc);
					param.put("cust_type_desc",cust_type_desc);
					param.put("personal_id_type_desc",personal_id_type_desc);
					param.put("organization_id_type_desc",organization_id_type_desc);
					param.put("other_id_name_desc",other_id_name_desc);
					param.put("id_code_desc",id_code_desc);
					param.put("spv_open_bank_desc",spv_open_bank_desc);
					param.put("other_open_bank_desc",other_open_bank_desc);
					param.put("cust_name_desc",cust_name_desc);
					param.put("sex_desc",sex_desc);
					param.put("risk_level_desc",risk_level_desc);
					param.put("moble_desc",moble_desc);
					param.put("tel_phone_desc",tel_phone_desc);
					param.put("email_desc",email_desc);
					param.put("remark_desc",remark_desc);
					param.put("register_serno",sqlRow.get("register_serno"));
					param.put("theory_report_start_date",workDate);
					param.put("workDate",workDate);

				}
				desc_flag = "0";
			}
			if(param != null && param.size() > 0){
				//插入投资者信息错误描述表
				checkDataDao.insertInfoErrorDesc(param);
			}
			cnt += read_limit;
			if(count<read_limit){//读取完毕，退出循环
				break;
			}
		}
		return error_cnt;
	}


	/**
	 * 合法性校验  202--权益人持有信息登记
	 * @throws Exception
	 */
	public int transRegisterChecking(String datatype, String workDate , String whiteregex) throws Exception{

		Pattern whiterpattern = null;
		if(!"".equals(whiteregex)){
			whiterpattern = Pattern.compile(whiteregex);
		}

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("workdate", workDate);

		List<SqlRow> listrst = checkDataDao.getExFmt(datatype);

		checkDataDao.deleteVolRgInfo(workDate);

		int read_limit = 10000; //每次查询1000条
		int cnt = 0;
		int error_cnt = 0;


		while(true){
			params.put("start", cnt);
			params.put("limit", read_limit);
			List<SqlRow> findSql = checkDataDao.findVolRegisterInfo(workDate);

			Map<String,Object> param = new HashMap<>();

			String desc_flag = "0"; //是否有误
			int count = 0;
			for (SqlRow sqlRow: findSql) {
				count ++;
				SqlRow correctRow = null;
				String id = sqlRow.getString("id");	// 记录ID
				String register_serno = sqlRow.getString("register_serno");	// 登记流水号

				//登记银行代码
				String bank_code = sqlRow.getString("bank_code");
				String bank_code_desc = "";
				String bank_code_s = DirectParams.bankCode;
//				Pattern p=Pattern.compile("^([Z]{1}\\d{5})");
//				Matcher m=p.matcher(bank_code);
//				boolean flagBankCode = m.matches();
				if ("".equals(bank_code)){
					bank_code_desc = "登记银行代码要素不可为空。";
				}else if (!bank_code_s.equals(bank_code)){
					bank_code_desc = "登记银行代码必须与银行代码相同。";
				}
//				else if (!flagBankCode && !"".equals(bank_code)){
//					bank_code_desc = "登记银行代码要素格式不对。正确的格式：ZXXXXX（XXXXX为数字）。";
//				}



				//产品登记编码
				String prod_code = (String) DirectUtils.getCorrectValue(correctRow, "prod_code", sqlRow.get("prod_code"));
				String prod_code_desc = DirectUtils.checkFMT(listrst,"prod_code",prod_code);
				Pattern pProdCode=Pattern.compile("^[Z]\\d{7}[ABC]{0,1}\\d{6}$");
				Matcher mProdCode=pProdCode.matcher(prod_code+"");
				boolean flagProdCode = mProdCode.matches();
				if("".equals(prod_code) ){
					prod_code_desc = "产品登记编码要素不可为空。";
				}else if(prod_code.length() < 14){
					prod_code_desc = "产品登记编码格式不正确";
				}else if(!flagProdCode && !"".equals(prod_code)){
					prod_code_desc = "产品登记编码要素格式不对。正确的格式：字母Z+7位数字+字母A，B，C（三个中的1个或0个）+6位数字。";
				}else if(!checkDataDao.checkZZProdCode(prod_code)){
					prod_code_desc +=DirectUtils.getErrInt()+ "产品登记编码为"+prod_code+"的产品信息在系统中尚未登记。";
				}

				// 持有日期
				String hold_date = (String) DirectUtils.getCorrectValue(correctRow, "hold_date", sqlRow.get("hold_date"));
				hold_date= DirectUtils.getS_d(hold_date);
				String hold_date_desc = DirectUtils.checkFMT(listrst,"hold_date",hold_date);
				String date_format = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)" ;
				if("".equals(hold_date) ){
					hold_date_desc = "持有日期要素不可为空。";
				}else if(!hold_date.matches(date_format)){
					hold_date_desc = "持有日期必须为日期格式（YYYY-MM-DD）";
					//生产要放开
				}else if(Integer.parseInt(hold_date.replace("-", "")) > Integer.parseInt(DirectParams.sysDate)){
					hold_date_desc = "持有日期必须小于等于系统处理当天日期。";
				}

				// 识别标识
				String cust_no = (String) DirectUtils.getCorrectValue(correctRow, "cust_no", sqlRow.get("cust_no"));
				String cust_no_desc = DirectUtils.checkFMT(listrst,"cust_no",cust_no);
				/*modify qink 20210426 验证识别标识是否存在*/
				cust_no_desc=checkDataDao.checkCustno(cust_no);
				Pattern pCustNo=Pattern.compile(whiteregex);
				Matcher mCustNo=pCustNo.matcher(cust_no+"");
				boolean flagCustNo = mCustNo.matches();
				if("".equals(cust_no) ){
					cust_no_desc = "识别标识要素不可为空。";
				}else if(cust_no.getBytes().length > 30){
					cust_no_desc = "识别标识过长。";
				}

				// 币种
				String cur = (String) DirectUtils.getCorrectValue(correctRow, "cur", sqlRow.get("cur"));
				String cur_desc = DirectUtils.checkDict("tr_cur", cur);
				//log.info("cur" + cur + " , cur_desc : " + cur_desc);
				if("".equals(cur) ){
					cur_desc = "币种不可为空。";
				}

				//持有份额
				String vol_tmp = (String) DirectUtils.getCorrectValue(correctRow, "hold_vol", sqlRow.get("hold_vol")) ;
				/*modify qink 20210426 空字符串不能转换数字*/
				String hold_vol_desc="";
				if("".equals(vol_tmp)) {
					hold_vol_desc = "持有份额要素不可为空。";
				}else{
					double vol = Double.parseDouble(vol_tmp);
					hold_vol_desc = DirectUtils.checkFMT(listrst,"hold_vol",vol);
					Pattern pHoldVol=Pattern.compile("^^(([1-9]{1}\\d{0,12})|([0]{1}))(\\.(\\d){1,5})?$");
					Matcher mHoldVol=pHoldVol.matcher(vol_tmp+"");
					boolean flagVol = mHoldVol.matches();
					if(vol<=0){
						hold_vol_desc = "持有份额必须大于0。";
					}else if("".equals(vol_tmp)){
						hold_vol_desc = "持有份额要素不可为空。";
					}else if(!flagVol){
						hold_vol_desc = "持有份额必须为n（18,5）格式。";
					}
				}

				//持有金额
				String amt_tmp =  (String) DirectUtils.getCorrectValue(correctRow, "hold_amt", sqlRow.get("hold_amt")) ;
				/*modify qink 20210426*/
				double amt=0.0;
				String hold_amt_desc="";
				if("".equals(amt_tmp)){
					hold_amt_desc = "持有金额要素不可为空。";
				}else{
					 amt = Double.parseDouble(amt_tmp);
					hold_amt_desc = DirectUtils.checkFMT(listrst,"hold_amt",amt);
					Pattern pHoldAmt=Pattern.compile("^^(([1-9]{1}\\d{0,12})|([0]{1}))(\\.(\\d){1,2})?$");
					Matcher mHoldAmt=pHoldAmt.matcher(amt_tmp+"");
					boolean flagAmt = mHoldAmt.matches();
					if(amt<=0){
						hold_amt_desc = "持有金额必须大于0。";
					}else if("".equals(amt_tmp)){
						hold_amt_desc = "持有金额要素不可为空。";
					}else if(!flagAmt){
						hold_amt_desc = "持有金额必须为n（15,2）格式。";
					}
				}


				// 折算人民币金额（元）
				String convert_rmb_tmp = (String) DirectUtils.getCorrectValue(correctRow, "convert_rmb", sqlRow.get("convert_rmb")) ;
				/*modify qink 20210426*/
				double convert_rmb=0.0;
				String convert_rmb_desc="";
				if("".equals(convert_rmb_tmp)){
					convert_rmb_desc = "折算人民币金额（元）要素不可为空。";
				}else{
					convert_rmb = Double.parseDouble(convert_rmb_tmp);
					convert_rmb_desc = DirectUtils.checkFMT(listrst,"convert_rmb",convert_rmb);
					Pattern pConvertAmt=Pattern.compile("^^(([1-9]{1}\\d{0,12})|([0]{1}))(\\.(\\d){1,2})?$");
					Matcher mConvertAmt=pConvertAmt.matcher(convert_rmb_tmp+"");
					boolean flagRmb = mConvertAmt.matches();
					if(convert_rmb<=0){
						convert_rmb_desc = "折算人民币金额（元）必须大于0。";
					}else if("".equals(convert_rmb_tmp)){
						convert_rmb_desc = "折算人民币金额（元）要素不可为空。";
					}
					if(DirectUtils.conUtil(cur,"CNY") > -1 && (Double)amt!=convert_rmb){
						convert_rmb_desc = "当持有金额的币种为人民币(CNY)时，折算人民币金额（元）与持有金额必须相等。";
					}else if(DirectUtils.conUtil(cur,"CNY") < 0 && !"".equals(convert_rmb_desc)){
						convert_rmb_desc = "折算人民币金额（元）的币种必须选择“人民币(CNY)。";
					}else if(!flagRmb){
						convert_rmb_desc = "折算人民币金额（元）必须为n（15,2）格式。";
					}
				}

				if(!"".equals(whiteregex)){
					if(!bank_code.equals("")&&!whiterpattern.matcher(bank_code).matches()) bank_code_desc=bank_code_desc+"登记银行代码必须填写白名单中的文字";
					if(!prod_code.equals("")&&!whiterpattern.matcher(prod_code).matches()) prod_code_desc=prod_code_desc+"产品登记编码必须填写白名单中的文字";
					if(!cust_no.equals("")&&!whiterpattern.matcher(cust_no).matches()) cust_no_desc=cust_no_desc+"识别标识必须填写白名单中的文字";
					if(!hold_date.equals("")&&!whiterpattern.matcher(hold_date).matches()) hold_date_desc=hold_date_desc+"持有日期必须填写白名单中的文字";
					if(!cur.equals("")&&!whiterpattern.matcher(cur).matches()) cur_desc=cur_desc+"币种必须填写白名单中的文字";
					if(!vol_tmp.equals("")&&!whiterpattern.matcher(vol_tmp).matches()) hold_vol_desc=hold_vol_desc+"持有份额必须填写白名单中的文字";
					if(!amt_tmp.equals("")&&!whiterpattern.matcher(amt_tmp).matches()) hold_amt_desc=hold_amt_desc+"持有金额必须填写白名单中的文字";
					if(!String.valueOf(convert_rmb).equals("")&&!whiterpattern.matcher(String.valueOf(convert_rmb)).matches()) convert_rmb_desc=convert_rmb_desc+"折算人民币金额必须填写白名单中的文字";
				}
				desc_flag = ("".equals(bank_code_desc))?desc_flag:"1";
				desc_flag = ("".equals(prod_code_desc))?desc_flag:"1";
				desc_flag = ("".equals(hold_date_desc))?desc_flag:"1";
				desc_flag = ("".equals(cust_no_desc))?desc_flag:"1";
				desc_flag = ("".equals(cur_desc))?desc_flag:"1";
				desc_flag = ("".equals(hold_amt_desc))?desc_flag:"1";
				desc_flag = ("".equals(convert_rmb_desc))?desc_flag:"1";
				desc_flag = ("".equals(hold_vol_desc))?desc_flag:"1";

				if(desc_flag.equals("1")){//有错误描述,插入差错描述表
					error_cnt ++;

					param.put("bank_code_desc",bank_code_desc);
					param.put("prod_code_desc",prod_code_desc);
					param.put("cust_no_desc",cust_no_desc);
					param.put("hold_date_desc",hold_date_desc);
					param.put("hold_vol_desc",hold_vol_desc);
					param.put("cur_desc",cur_desc);
					param.put("hold_amt_desc",hold_amt_desc);
					param.put("convert_rmb_desc",convert_rmb_desc);
					param.put("theory_report_start_date",workDate);
					param.put("register_serno",register_serno);
					param.put("id",id);
					param.put("workDate",workDate);

				}
				desc_flag = "0";
			}
			if(param != null  && param.size() > 0 ){
				checkDataDao.insertVolErrorDesc(param);
			}
			cnt += read_limit;
			if(count<read_limit){//读取完毕，退出循环
				break;
			}
		}
		return error_cnt;
	}

	/**
	 * 合法性校验  203--投资者明细信息登记
	 * @throws Exception
	 */
	public int transInfoChecking(String datatype, String workDate , String whiteregex) throws Exception{

		Pattern whiterpattern = null;
		if(!"".equals(whiteregex)){
			whiterpattern = Pattern.compile(whiteregex);
		}

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("workdate", workDate);

		List<SqlRow> listrst = checkDataDao.getExFmt(datatype);
		checkDataDao.deleteTransInfo(workDate);

		//登记流水号报文内不可重复。
		List<SqlRow> checkRep_sr = checkDataDao.findTransInfoSerno();
		if (checkRep_sr != null && checkRep_sr.size() > 0) {
			throw new Exception("投资者明细中，流水号为["+checkRep_sr.get(0).getString("register_serno") +
					"]的数据有"+checkRep_sr.get(0).getString("cnt")+"条重复数据。请删除或修正后重新校验！");
		}

		//同一个报文内，“发行机构代码+产品登记编码+核心交易流水号”不得同时重复。
		checkRep_sr = checkDataDao.findTransInfoBankCode();
		if (checkRep_sr != null && checkRep_sr.size() > 0) {
			throw new Exception("投资者明细中，发行机构代码["+checkRep_sr.get(0).getString("bank_code")+"]产品登记编码["+checkRep_sr.get(0).getString("prod_code")+"]" +
					"核心交易流水号["+checkRep_sr.get(0).getString("trans_serno")+"]的数据有："+checkRep_sr.get(0).getString("cnt")+"条重复数据。" +
					"请删除或修正后重新校验！");
		}
		//同一个发行机构内，“产品登记编码+核心交易流水号”不得与已登记信息重复。
		checkRep_sr = checkDataDao.findTransInfoProdCode();
		if (checkRep_sr != null && checkRep_sr.size() > 0) {
			throw new Exception("投资者明细中，产品登记编码["+checkRep_sr.get(0).getString("prod_code")+"]和核心交易流水号["+checkRep_sr.get(0).getString("trans_serno")+
					"]存在已登记的业务明细信息！请删除或修正后重新校验！");
		}


		int read_limit = 10000; //每次查询1000条
		int cnt = 0;
		int error_cnt = 0;

		while(true){
			params.put("start", cnt);
			params.put("limit", read_limit);
			List<SqlRow> findSql = checkDataDao.findTransInfo(workDate);
			Map<String,Object> param = new HashMap<>();
			String desc_flag = "0"; //是否有误
			int count = 0;
			for (SqlRow sqlRow: findSql) {
				count ++;
				SqlRow correctRow = null;
				//发行机构代码
				String bank_code = (String) DirectUtils.getCorrectValue(correctRow, "bank_code", sqlRow.get("bank_code"));
				String bank_code_desc = "";
				String bank_code_s = DirectParams.bankCode;
				boolean flagBankCode = bank_code.matches("^[a-zA-Z]{1}\\d{5}$");
				if ("".equals(bank_code)){
					bank_code_desc = "发行机构代码要素不可为空。";
				}else if (!bank_code_s.equals(bank_code)){
					bank_code_desc = "发行机构代码必须与银行代码相同。";
				}else if (!flagBankCode && !"".equals(bank_code)){
					bank_code_desc = "发行机构代码要素格式不对。正确的格式：6位英文或数字。";
				}

				//销售合同号
				String contract_no = (String) DirectUtils.getCorrectValue(correctRow, "contract_no", sqlRow.get("contract_no"));
				String contract_no_desc="";
				if ("".equals(contract_no)){
					contract_no_desc = "销售合同号要素不可为空。";
				}else if (contract_no.getBytes().length > 100){
					contract_no_desc = "销售合同号过长。";
				}

				//核心交易流水号
				String trans_serno = (String) DirectUtils.getCorrectValue(correctRow, "trans_serno", sqlRow.get("trans_serno"));
				String trans_serno_desc="";
				if ("".equals(trans_serno)){
					trans_serno_desc = "核心交易流水号要素不可为空。";
				}else if (trans_serno.length() > 60){
					trans_serno_desc = "核心交易流水号过长。";
				}

				//理财账号
				String fnc_trans_acct_no = (String) DirectUtils.getCorrectValue(correctRow, "fnc_trans_acct_no", sqlRow.get("fnc_trans_acct_no"));
				String fnc_trans_acct_no_desc="";
				if ("".equals(fnc_trans_acct_no)){
					fnc_trans_acct_no_desc = "理财账号要素不可为空。";
				}else if (fnc_trans_acct_no.getBytes().length > 60){
					fnc_trans_acct_no_desc = "理财账号过长。";
				}

				//客户统一编号
				String host_cust_no = (String) DirectUtils.getCorrectValue(correctRow, "host_cust_no", sqlRow.get("host_cust_no"));
				String host_cust_no_desc="";
				if ("".equals(host_cust_no)){
					host_cust_no_desc = "客户统一编号要素不可为空。";
				}else if (host_cust_no.getBytes().length > 60){
					host_cust_no_desc = "客户统一编号过长。";
				}

				// 识别标识
				String cust_no = (String) DirectUtils.getCorrectValue(correctRow, "cust_no", sqlRow.get("cust_no"));
				String cust_no_desc = "";
				/*modify qink 20210426 验证识别标识是否存在*/
				cust_no_desc=checkDataDao.checkCustno(cust_no);

				Pattern pCustNo=Pattern.compile("[\u4e00-\u9fa5]");
				Matcher mCustNo=pCustNo.matcher(cust_no);
				boolean flagCustNo = mCustNo.find();
				if("".equals(cust_no) ){
					cust_no_desc = "识别标识要素不可为空。";
				}else if(cust_no.getBytes().length > 30){
					cust_no_desc = "识别标识过长。";
				}

				//客户姓名
				String cust_name = (String) DirectUtils.getCorrectValue(correctRow, "cust_name", sqlRow.get("cust_name"));
				String cust_name_desc = "";
				if("".equals(cust_name) ){
					cust_name_desc = "客户姓名要素不可为空。";
				}else if(cust_name.getBytes("GBK").length>100){
					cust_name_desc = "客户姓名过长。";
				}else if(cust_name.matches("^[\u4e00-\u9fa5]+$") &&( cust_name.length()==2 ||  cust_name.length()==3)){
					cust_name_desc="客户姓名不符合变形处理要求。";
				}

				//关联活期存款账号
				String acct_no = (String) DirectUtils.getCorrectValue(correctRow, "acct_no", sqlRow.get("acct_no"));
				String acct_no_desc="";
				if ("".equals(acct_no)){
					acct_no_desc = "关联活期存款账号要素不可为空。";
				}else if (acct_no.getBytes().length > 60){
					acct_no_desc = "关联活期存款账号过长。";
				}

				//关联账号开户所在地
				String code="110000,310000,120000,370000,370200,230000,220000,210000,210200,320000,330000,330200,360000,340000,350000,350200,140000,410000,130000,430000,420000,440000,440300,450000,460000,500000,510000,530000,520000,610000,620000,630000,150000,640000,650000,540000,810000,820000,710000,900000";
				String acct_loc_code = (String) DirectUtils.getCorrectValue(correctRow, "acct_loc_code", sqlRow.get("acct_loc_code"));
				String acct_loc_code_desc="";
				if ("".equals(acct_loc_code)){
					acct_loc_code_desc = "关联账号开户所在地要素不可为空。";
				}else if (!"".equals(acct_loc_code) && DirectUtils.conUtil(acct_loc_code,code) < 0){
					acct_loc_code_desc = "关联账号开户所在地不在值域范围内。";
				}

				//是否有其他机构代销
				code="01,02";
				String is_agent = (String) DirectUtils.getCorrectValue(correctRow, "is_agent", sqlRow.get("is_agent"));
				String is_agent_desc="";
				if ("".equals(is_agent)){
					is_agent_desc = "是否代销要素不可为空。";
				}else if (!"".equals(acct_loc_code) && DirectUtils.conUtil(is_agent,code) < 0){
					is_agent_desc = "是否代销不在值域范围内。";
				}

				//代销机构代码
				String agent_bank_code = (String) DirectUtils.getCorrectValue(correctRow, "agent_bank_code", sqlRow.get("agent_bank_code"));
				String agent_bank_code_desc="";
				/*if("01".equals(is_agent)&&"".equals(agent_bank_code)){
					agent_bank_code_desc="当是否代销为是时，销售机构代码要素不可为空。";
				}else if ("02".equals(is_agent)&&!"".equals(agent_bank_code)){
					agent_bank_code_desc="当是否代销为否时，销售机构代码要素必须为空。";
				}else if (agent_bank_code.getBytes().length > 30){
					agent_bank_code_desc = "销售机构代码过长。";
				}*/
				if(agent_bank_code==null || "".equals(agent_bank_code)){
					agent_bank_code_desc = "销售机构代码要素不可为空。";
				}else {
					if ("02".equals(is_agent) && !agent_bank_code.equals(bank_code)) {
						agent_bank_code_desc = "当是否代销为否时，销售机构代码必须与登记机构代码保持一致。";
					}
					if (agent_bank_code.length() > 30) {
						agent_bank_code_desc = "销售机构代码过长。";
					}
				}

				//代销机构名称
				String agent_bank_name = (String) DirectUtils.getCorrectValue(correctRow, "agent_bank_name", sqlRow.get("agent_bank_name"));
				String agent_bank_name_desc="";
				/*if("01".equals(is_agent)&&"".equals(agent_bank_name)){
					agent_bank_name_desc="当是否代销为是时，销售机构名称要素不可为空。";
				}else if ("02".equals(is_agent)&&!"".equals(agent_bank_name)){
					agent_bank_name_desc="当是否代销为否时，销售机构代名称要素必须为空。";
				}else if (agent_bank_name.getBytes("GBK").length > 200){
					agent_bank_name_desc = "销售机构名称过长。";
				}*/
				if(agent_bank_name == null || "".equals(agent_bank_name)){
					agent_bank_name_desc = "销售机构名称要素不可为空。";
				}else{
					if (agent_bank_name.getBytes("GBK").length > 200){
						agent_bank_name_desc = "销售机构名称过长。";
					}
				}
				//代销机构所属监管机构
				code="01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37";
				String agent_regu_code = (String) DirectUtils.getCorrectValue(correctRow, "agent_regu_code", sqlRow.get("agent_regu_code"));
				String agent_regu_code_desc="";
				if("01".equals(is_agent)&&"".equals(agent_regu_code)){
					agent_regu_code_desc="当是否代销为是时，销售机构所属监管机构要素不可为空。";
				}else if ("02".equals(is_agent)&&!"".equals(agent_regu_code)){
					agent_regu_code_desc="当是否代销为否时，销售机构所属监管机构要素必须为空。";
				}else if (!"".equals(agent_regu_code) && DirectUtils.conUtil(agent_regu_code,code) < 0){
					agent_regu_code_desc = "销售机构所属监管机构不在值域范围内。";
				}
				//产品登记编码
				String prod_code = (String) DirectUtils.getCorrectValue(correctRow, "prod_code", sqlRow.get("prod_code"));
				String reg_code = (String) DirectUtils.getCorrectValue(correctRow, "reg_code", sqlRow.get("reg_code"));
				//System.out.println("woshizp1"+prod_code);
				String prod_code_desc = "";
				boolean flagProdCode = prod_code.matches("^[a-zA-Z0-9]{14,15}$");

				if("".equals(prod_code) ){
					prod_code_desc = "产品登记编码要素不可为空。";
				}else if(!flagProdCode){
					prod_code_desc = "产品登记编码要素格式不对。正确的格式：14或15位英文或数字。";
				}else if(!bank_code_s.equals(prod_code.substring(0,6))){
					prod_code_desc = "产品登记编码为"+prod_code+"的产品与登记银行不匹配。";
				}else if (reg_code==null ||"".equals(reg_code)){
					prod_code_desc="产品登记编码为"+prod_code+"的产品信息在系统中尚未登记。";
				}
				//System.out.println("woshizp"+prod_code_desc);
				//业务种类
				code="01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16";
				String busi_code = (String) DirectUtils.getCorrectValue(correctRow, "busi_code", sqlRow.get("busi_code"));
				String busi_code_desc = "";
				if("".equals(busi_code) ){
					busi_code_desc = "业务种类要素不可为空。";
				}else if(!"".equals(busi_code) && DirectUtils.conUtil(busi_code,code) < 0){
					busi_code_desc = "业务种类不在值域范围内。";
				}
				//业务发生地所属监管
				code="02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37";
				String busi_regu_code = (String) DirectUtils.getCorrectValue(correctRow, "busi_regu_code", sqlRow.get("busi_regu_code"));
				String busi_regu_code_desc = "";
				if("".equals(busi_regu_code) ){
					busi_regu_code_desc = "业务发生地所属监管要素不可为空。";
				}else if(!"".equals(busi_regu_code) && DirectUtils.conUtil(busi_regu_code,code) < 0){
					busi_regu_code_desc = "业务发生地所属监管不在值域范围内。";
				}

				//业务确认日期
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				format.setLenient(false);
				String ack_date = (String) DirectUtils.getCorrectValue(correctRow, "ack_date", sqlRow.get("ack_date"));
				String ack_date_desc = "";
				ack_date= DirectUtils.getS_d(ack_date);
				if("".equals(ack_date) ){
					ack_date_desc = "业务确认日期要素不可为空。";
				}else if(!ack_date.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")){
					ack_date_desc = "业务确认日期必须为日期格式（YYYY-MM-DD）。";
				}else if(Integer.parseInt(ack_date.replace("-", "")) > Integer.parseInt(DirectParams.sysDate)){
					ack_date_desc = "业务确认日期为"+ack_date+"的登记要素不可大于明细登记日期。";
				}else{
					try {
						format.parse(ack_date);
					} catch (ParseException e) {
						ack_date_desc = "业务确认日期必须为正确日期。";
					}
				}

				//业务确认时间
				SimpleDateFormat format1 = new SimpleDateFormat("HHmmss");
				format1.setLenient(false);
				String ack_time = (String) DirectUtils.getCorrectValue(correctRow, "ack_time", sqlRow.get("ack_time"));
				String ack_time_desc="";
				String matche="([0-9][0-9])([0-9][0-9])([0-9][0-9])$";
				if("".equals(ack_time) ){
					ack_time_desc = "业务确认时间要素不可为空。";
				}else if(!ack_time.matches(matche)){
					ack_time_desc = "业务确认时间要素格式不对。正确的格式：HHMMSS。";
				}else{
					try {
						format1.parse(ack_time);
					} catch (ParseException e) {
						ack_time_desc = "业务确认时间必须为正确时间。";
					}
				}

				//币种
				String cur_desc = "";
				String cur = (String) DirectUtils.getCorrectValue(correctRow, "cur", sqlRow.get("cur"));
				String scur_desc = DirectUtils.checkDict("tr_cur", cur);
				if("".equals(cur) ){
					cur_desc = "币种不可为空。";
				}else if (!"".equalsIgnoreCase(scur_desc)){
					cur_desc ="币种" + scur_desc;
				}

				//金额
				String ack_amt=DirectUtils.getCorrectValue(correctRow, "ack_amt", sqlRow.get("ack_amt")).toString();
				String ack_amt_desc = "";
				if("".equals(ack_amt)){
					ack_amt_desc = "持有金额要素不可为空。";
				}else{
					try {
						if(Double.parseDouble(ack_amt)<0){
							ack_amt_desc = "持有金额必须大于等于0。";
						}else if(!ack_amt.matches("^^(([1-9]{1}\\d{0,12})|([0]{1}))(\\.(\\d){0,2})?$")){
							ack_amt_desc = "持有金额必须为n（15,2）格式。";
						}
					} catch (Exception e) {
						ack_amt_desc = "持有金额必须为n（15,2）格式。";
					}
				}

				//折算人民币金额
				//System.out.println("12345"+DirectUtils.getCorrectValue(correctRow, "convert_rmb", sqlRow.get("convert_rmb")).getClass().getName());
				String convert_rmb = DirectUtils.getCorrectValue(correctRow, "convert_rmb", sqlRow.get("convert_rmb")).toString() ;
				String convert_rmb_desc="";
				if("".equals(convert_rmb)){
					convert_rmb_desc = "折算人民币金额（元）要素不可为空。";
				}else{
					try {
						if(Double.parseDouble(convert_rmb)<0){
							convert_rmb_desc = "折算人民币金额（元）必须大于等于0。";
						}else if("CNY".equals(cur) && Double.parseDouble(ack_amt)!=Double.parseDouble(convert_rmb)){
							convert_rmb_desc = "当币种为人民币(CNY)时，折算人民币金额与金额必须相等。";
						}else if(!convert_rmb.matches("^^(([1-9]{1}\\d{0,12})|([0]{1}))(\\.(\\d){1,2})?$")){
							convert_rmb_desc = "折算人民币金额（元）必须为n（15,2）格式。";
						}
					} catch (Exception e) {
						convert_rmb_desc = "折算人民币金额（元）必须为n（15,2）格式。";
					}
				}

				//确认净值
				String Nav=DirectUtils.getCorrectValue(correctRow, "Nav", sqlRow.get("Nav")).toString();
				//String proc_mode = sqlRow.getString("proc_mode");
				String nav_desc = "";
				try {
					//净值产品和非净值产品净值都不能为空
					if("".equals(Nav)){
						nav_desc="对于开放式和封闭式净值型产品,确认净值必填。";
					}else if(Double.parseDouble(Nav)<0){
						nav_desc="确认净值必须大于等于0。";
					}else if(!Nav.matches("^^(([1-9]{1}\\d{0,4})|([0]{1}))(\\.(\\d){1,5})?$")){
						nav_desc="确认净值必须为n（10,5）格式。";
					}
				} catch (Exception e) {
					nav_desc="确认净值必须为n（10,5）格式。";
				}

				//份额
				String ack_vol = DirectUtils.getCorrectValue(correctRow, "ack_vol", sqlRow.get("ack_vol")).toString() ;
				String ack_vol_desc="";
				try {
					if("".equals(ack_vol)){
						ack_vol_desc = "持有份额要素不可为空。";
					}else if(Double.parseDouble(ack_vol)<0){
						ack_vol_desc = "持有份额必须大于等于0。";
					}else if(!ack_vol.matches("^^(([1-9]{1}\\d{0,12})|([0]{1}))(\\.(\\d){1,5})?$")){
						ack_vol_desc = "持有份额必须为n（18,5）格式。";
					}
				} catch (Exception e) {
					ack_vol_desc = "持有份额必须为n（18,5）格式。";
				}

				//费用
				String fee_amt=DirectUtils.getCorrectValue(correctRow, "fee_amt", sqlRow.get("fee_amt")).toString();
				String fee_amt_desc = "";
				try {
					if("".equals(fee_amt)){
						fee_amt_desc="费用要素不可为空。";
					}else if(Double.parseDouble(fee_amt) <0){
						fee_amt_desc="费用必须大于等于0。";
					}else if(!fee_amt.matches("^^(([1-9]{1}\\d{0,12})|([0]{1}))(\\.(\\d){1,2})?$")){
						fee_amt_desc="费用必须为n（15,2）格式。";
					}
				} catch (Exception e) {
					fee_amt_desc="费用必须为n（15,2）格式。";
				}

				//渠道
				String channel_flag_desc = "";
				String channel_flag = (String) DirectUtils.getCorrectValue(correctRow, "channel_flag", sqlRow.get("channel_flag"));
//				code="01,02,03,04,05,06,99";
				code = "01 柜面,02 ATM,03 VTM,04 POS,05 网银,06 手机银行,07 银联交易,08 第三方支付,99 其他";
				if("".equals(channel_flag) ){
					channel_flag_desc = "渠道要素不可为空。";
				}else if (!"".equals(channel_flag)&&DirectUtils.conUtil(channel_flag,code)<0){
					channel_flag_desc ="渠道不在值域范围内。";
				}

				//交易柜员号
				String inputuser_desc = "";
				String inputuser = (String) DirectUtils.getCorrectValue(correctRow, "inputuser", sqlRow.get("inputuser"));
				if("".equals(inputuser) ){
					inputuser_desc = "交易柜员号要素不可为空。";
				}else if (inputuser.getBytes("GBK").length >30){
					inputuser_desc ="交易柜员号过长。";
				}

				//备注
				String remark_desc = "";
				String remark = (String) DirectUtils.getCorrectValue(correctRow, "remark", sqlRow.get("remark"));
				if (!"".equals(remark)&&inputuser.getBytes("GBK").length>256){
					remark_desc ="备注过长。";
				}

				//登记流水号
				String register_serno = (String) DirectUtils.getCorrectValue(correctRow, "register_serno", sqlRow.get("register_serno"));
				String register_serno_desc = "";
				boolean flagmpregister_serno = register_serno.matches("[\\x00-\\xff]+");
				if("".equals(register_serno) ){
					register_serno_desc = "登记流水号要素不可为空。";
				}else if (register_serno.getBytes("GBK").length >32){
					register_serno_desc ="登记流水号过长。";
				}

				if(!"".equals(whiteregex)){
					if(!contract_no.equals("")&&!whiterpattern.matcher(contract_no).matches()) contract_no_desc=contract_no_desc+"销售合同号必须填写白名单中的文字。";
					if(!trans_serno.equals("")&&!whiterpattern.matcher(trans_serno).matches()) trans_serno_desc=trans_serno_desc+"核心交易流水号必须填写白名单中的文字。";
					if(!fnc_trans_acct_no.equals("")&&!whiterpattern.matcher(fnc_trans_acct_no).matches()) fnc_trans_acct_no_desc=fnc_trans_acct_no_desc+"理财账号必须填写白名单中的文字。";
					if(!host_cust_no.equals("")&&!whiterpattern.matcher(host_cust_no).matches()) host_cust_no_desc=host_cust_no_desc+"客户统一编号必须填写白名单中的文字。";
					if(!cust_no.equals("")&&(!whiterpattern.matcher(cust_no).matches()||flagCustNo)) cust_no_desc=cust_no_desc+"识别标识只能含有白名单内的阿拉伯数字，英文字母，半角符号，全角符号。";
					if(!cust_name.equals("")&&!whiterpattern.matcher(cust_name).matches()) cust_name_desc=cust_name_desc+"客户姓名必须填写白名单中的文字。";
					if(!acct_no.equals("")&&!whiterpattern.matcher(acct_no).matches()) acct_no_desc=acct_no_desc+"关联活期存款账号必须填写白名单中的文字。";
					if(!agent_bank_code.equals("")&&!whiterpattern.matcher(agent_bank_code).matches()) agent_bank_code_desc=agent_bank_code_desc+"代销机构代码必须填写白名单中的文字。";
					if(!agent_bank_name.equals("")&&!whiterpattern.matcher(agent_bank_name).matches()) agent_bank_name_desc=agent_bank_name_desc+"代销机构名称必须填写白名单中的文字。";
					if(!inputuser.equals("")&&!whiterpattern.matcher(inputuser).matches()) inputuser_desc=inputuser_desc+"交易柜员号必须填写白名单中的文字。";
					if(!remark.equals("")&&!whiterpattern.matcher(remark).matches()) remark_desc=remark_desc+"备注必须填写白名单中的文字。";
					if(!register_serno.equals("")&&(!whiterpattern.matcher(register_serno).matches()||!flagmpregister_serno)) register_serno_desc=register_serno_desc+"登记流水号只能含有白名单内的阿拉伯数字，英文字母，半角符号。";
				}
				desc_flag = ("".equals(bank_code_desc))?desc_flag:"1";
				desc_flag = ("".equals(trans_serno_desc))?desc_flag:"1";
				desc_flag = ("".equals(contract_no_desc))?desc_flag:"1";
				desc_flag = ("".equals(fnc_trans_acct_no_desc))?desc_flag:"1";
				desc_flag = ("".equals(host_cust_no_desc))?desc_flag:"1";
				desc_flag = ("".equals(cust_no_desc))?desc_flag:"1";
				desc_flag = ("".equals(cust_name_desc))?desc_flag:"1";
				desc_flag = ("".equals(acct_no_desc))?desc_flag:"1";
				desc_flag = ("".equals(acct_loc_code_desc))?desc_flag:"1";
				desc_flag = ("".equals(is_agent_desc))?desc_flag:"1";
				desc_flag = ("".equals(agent_bank_code_desc))?desc_flag:"1";
				desc_flag = ("".equals(agent_bank_name_desc))?desc_flag:"1";
				desc_flag = ("".equals(agent_regu_code_desc))?desc_flag:"1";
				desc_flag = ("".equals(prod_code_desc))?desc_flag:"1";
				desc_flag = ("".equals(busi_code_desc))?desc_flag:"1";
				desc_flag = ("".equals(busi_regu_code_desc))?desc_flag:"1";
				desc_flag = ("".equals(ack_date_desc))?desc_flag:"1";
				desc_flag = ("".equals(ack_time_desc))?desc_flag:"1";
				desc_flag = ("".equals(cur_desc))?desc_flag:"1";
				desc_flag = ("".equals(ack_amt_desc))?desc_flag:"1";
				desc_flag = ("".equals(convert_rmb_desc))?desc_flag:"1";
				desc_flag = ("".equals(nav_desc))?desc_flag:"1";
				desc_flag = ("".equals(ack_vol_desc))?desc_flag:"1";
				desc_flag = ("".equals(fee_amt_desc))?desc_flag:"1";
				desc_flag = ("".equals(channel_flag_desc))?desc_flag:"1";
				desc_flag = ("".equals(inputuser_desc))?desc_flag:"1";
				desc_flag = ("".equals(remark_desc))?desc_flag:"1";
				desc_flag = ("".equals(register_serno_desc))?desc_flag:"1";

				if(desc_flag.equals("1")){//有错误描述,插入差错描述表
					error_cnt ++;
					param.put("workDate",workDate);
					param.put("theory_report_start_date",workDate);
					param.put("register_serno",register_serno);
					param.put("bank_code_desc",bank_code_desc);
					param.put("trans_serno_desc",trans_serno_desc);
					param.put("contract_no_desc",contract_no_desc);
					param.put("fnc_trans_acct_no_desc",fnc_trans_acct_no_desc);
					param.put("host_cust_no_desc",host_cust_no_desc);
					param.put("cust_no_desc",cust_no_desc);
					param.put("cust_name_desc",cust_name_desc);
					param.put("acct_no_desc",acct_no_desc);
					param.put("acct_loc_code_desc",acct_loc_code_desc);
					param.put("is_agent_desc",is_agent_desc);
					param.put("agent_bank_code_desc",agent_bank_code_desc);
					param.put("agent_bank_name_desc",agent_bank_name_desc);
					param.put("agent_regu_code_desc",agent_regu_code_desc);
					param.put("prod_code_desc",prod_code_desc);
					param.put("busi_code_desc",busi_code_desc);
					param.put("busi_regu_code_desc",busi_regu_code_desc);
					param.put("ack_date_desc",ack_date_desc);
					param.put("ack_time_desc",ack_time_desc);
					param.put("cur_desc",cur_desc);
					param.put("ack_amt_desc",ack_amt_desc);
					param.put("convert_rmb_desc",convert_rmb_desc);
					param.put("nav_desc",nav_desc);
					param.put("ack_vol_desc",ack_vol_desc);
					param.put("fee_amt_desc",fee_amt_desc);
					param.put("channel_flag_desc",channel_flag_desc);
					param.put("inputuser_desc",inputuser_desc);
					param.put("remark_desc",remark_desc);
					param.put("register_serno_desc",register_serno_desc);

				}
				desc_flag = "0";
			}
			if(param != null  && param.size() > 0 ){
				//插入投资者明细信息错误描述表
				checkDataDao.insertDetailErrorDesc(param);
			}
			cnt += read_limit;
			if(count<read_limit){//读取完毕，退出循环
				break;
			}
		}
		return error_cnt;
	}

}
