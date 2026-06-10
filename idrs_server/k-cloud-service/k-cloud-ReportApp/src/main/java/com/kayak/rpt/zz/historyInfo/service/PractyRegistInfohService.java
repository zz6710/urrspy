package com.kayak.rpt.zz.historyInfo.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.historyInfo.dao.PractyRegistInfohDao;
import com.kayak.rpt.zz.historyInfo.model.PractyRegistInfoh;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@APIDefine(desc = "从业人员登记历史信息服务", model = PractyRegistInfoh.class)
@Slf4j
@RefreshScope
public class PractyRegistInfohService {

	@Autowired
	private PractyRegistInfohDao practyRegistInfohDao;




	@API(desc = "查询从业人员登记历史信息信息", auth = APIAuth.YES)
	public SqlResult<PractyRegistInfoh> findPractyRegistInfos(SqlParam<PractyRegistInfoh> params) throws Exception {
		params.setMakeSql(true);
		return practyRegistInfohDao.findPractyRegistInfohs(params);
	}

	@API(desc = "添加从业人员登记历史信息", params = "profession,name,sex,bank_code,id_code,iss_branch_type,region,firm_name,department,post,education,degree,career_start_date,wealth_start_date,profess_qualy_level,wealth_cer,regist_cer_no,reward,telphone,mobile,email,register_classify,regist_type,details,register_serno,imp_date,register_date,register_status", auth = APIAuth.NO)
	public int addPractyRegistInfo(SqlParam<PractyRegistInfoh> params) throws Exception {
		return practyRegistInfohDao.addPractyRegistInfoh(params).getEffect();
	}
	
	@API(desc = "修改从业人员登记历史信息", params = "profession,name,sex,bank_code,id_code,iss_branch_type,region,firm_name,department,post,education,degree,career_start_date,wealth_start_date,profess_qualy_level,wealth_cer,regist_cer_no,reward,telphone,mobile,email,register_classify,regist_type,details,register_serno,imp_date,register_date,register_status", auth = APIAuth.NO)
	public int updatePractyRegistInfo(SqlParam<PractyRegistInfoh> params) throws Exception {
		return practyRegistInfohDao.updatePractyRegistInfoh(params).getEffect();
	}
	
	@API(desc = "删除从业人员登记历史信息", params = "profession,name,sex,bank_code,id_code,iss_branch_type,region,firm_name,department,post,education,degree,career_start_date,wealth_start_date,profess_qualy_level,wealth_cer,regist_cer_no,reward,telphone,mobile,email,register_classify,regist_type,details,register_serno,imp_date,register_date,register_status", auth = APIAuth.NO)
	public int deletePractyRegistInfo(SqlParam<PractyRegistInfoh> params) throws Exception {
		return practyRegistInfohDao.deletePractyRegistInfoh(params).getEffect();
	}




}
