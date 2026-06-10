package com.kayak.rpt.zz.operate.service;

import cn.hutool.core.bean.BeanUtil;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.rpt.zz.manage.model.TrPractyRegistInfo;
import com.kayak.rpt.zz.operate.model.InitialSubRegist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.operate.dao.PractyRegistDao;
import com.kayak.rpt.zz.operate.model.PractyRegist;

@Service
@APIDefine(desc = "从业人员登记信息操作记录服务", model = PractyRegist.class)
public class PractyRegistService {

	@Autowired
	private PractyRegistDao practyRegistDao;

	@API(desc = "查询从业人员登记信息操作记录信息", auth = APIAuth.YES)
	public SqlResult<PractyRegist> findPractyRegists(SqlParam<PractyRegist> params) throws Exception {
		params.setMakeSql(true);
		return practyRegistDao.findPractyRegists(params);
	}

	@API(desc = "添加从业人员登记信息操作记录", params = "profession,name,sex,bank_code,id_code,iss_branch_type,region,firm_name,department,post,education,degree,career_start_date,wealth_start_date,profess_qualy_level,wealth_cer,regist_cer_no,reward,telphone,mobile,email,register_classify,regist_type,details,register_serno,imp_date,register_date,register_status,summit_user,create_date,create_time,op_type", auth = APIAuth.NO)
	public int addPractyRegist(SqlParam<TrPractyRegistInfo> params, String opType) throws Exception {
		PractyRegist practyRegist = BeanUtil.copyProperties(params.getModel(), PractyRegist.class);
		practyRegist.setOpType(opType);
		practyRegist.setSummitUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username")));
		return practyRegistDao.addPractyRegist(practyRegist).getEffect();
	}
	
	@API(desc = "修改从业人员登记信息操作记录", params = "profession,name,sex,bank_code,id_code,iss_branch_type,region,firm_name,department,post,education,degree,career_start_date,wealth_start_date,profess_qualy_level,wealth_cer,regist_cer_no,reward,telphone,mobile,email,register_classify,regist_type,details,register_serno,imp_date,register_date,register_status,summit_user,create_date,create_time,op_type", auth = APIAuth.NO)
	public int updatePractyRegist(SqlParam<PractyRegist> params) throws Exception {
		return practyRegistDao.updatePractyRegist(params).getEffect();
	}
	
	@API(desc = "删除从业人员登记信息操作记录", params = "profession,name,sex,bank_code,id_code,iss_branch_type,region,firm_name,department,post,education,degree,career_start_date,wealth_start_date,profess_qualy_level,wealth_cer,regist_cer_no,reward,telphone,mobile,email,register_classify,regist_type,details,register_serno,imp_date,register_date,register_status,summit_user,create_date,create_time,op_type", auth = APIAuth.NO)
	public int deletePractyRegist(SqlParam<PractyRegist> params) throws Exception {
		return practyRegistDao.deletePractyRegist(params).getEffect();
	}

}
