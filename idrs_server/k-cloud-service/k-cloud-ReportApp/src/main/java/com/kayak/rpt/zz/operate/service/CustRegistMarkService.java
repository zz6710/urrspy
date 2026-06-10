package com.kayak.rpt.zz.operate.service;

import cn.hutool.core.bean.BeanUtil;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.rpt.zz.manage.model.TrCustRegisterInfo;
import com.kayak.rpt.zz.operate.model.CustTransMark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.operate.dao.CustRegistMarkDao;
import com.kayak.rpt.zz.operate.model.CustRegistMark;

@Service
@APIDefine(desc = "投资者身份登记操作记录服务", model = CustRegistMark.class)
public class CustRegistMarkService {

	@Autowired
	private CustRegistMarkDao custRegistMarkDao;

	@API(desc = "查询投资者身份登记操作记录信息", auth = APIAuth.YES)
	public SqlResult<CustRegistMark> findCustRegistMarks(SqlParam<CustRegistMark> params) throws Exception {
		params.setMakeSql(true);
		return custRegistMarkDao.findCustRegistMarks(params);
	}

	@API(desc = "添加投资者身份登记操作记录", params = "summit_user,create_date,create_time,bank_code,data_type,cust_no,ori_cust_no,is_belong,iss_bank_name,iss_bank_code,in_out_sign,iss_country,cust_type,personal_id_type,organization_id_type,other_id_name,id_code,spv_open_bank,other_open_bank,cust_name,sex,risk_level,moble,tel_phone,email,register_date,register_serno,remark,imp_date,register_status,op_type,report_date,ta_id", auth = APIAuth.NO)
	public int addCustRegistMark(SqlParam<TrCustRegisterInfo> params,String opType) throws Exception {
		CustRegistMark custRegistMark = BeanUtil.copyProperties(params.getModel(), CustRegistMark.class);
		custRegistMark.setOpType(opType);
		custRegistMark.setSummitUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username")));
		return custRegistMarkDao.addCustRegistMark(custRegistMark).getEffect();
	}
	
	@API(desc = "修改投资者身份登记操作记录", params = "summit_user,create_date,create_time,bank_code,data_type,cust_no,ori_cust_no,is_belong,iss_bank_name,iss_bank_code,in_out_sign,iss_country,cust_type,personal_id_type,organization_id_type,other_id_name,id_code,spv_open_bank,other_open_bank,cust_name,sex,risk_level,moble,tel_phone,email,register_date,register_serno,remark,imp_date,register_status,op_type,report_date,ta_id", auth = APIAuth.NO)
	public int updateCustRegistMark(SqlParam<CustRegistMark> params) throws Exception {
		return custRegistMarkDao.updateCustRegistMark(params).getEffect();
	}
	
	@API(desc = "删除投资者身份登记操作记录", params = "summit_user,create_date,create_time,bank_code,data_type,cust_no,ori_cust_no,is_belong,iss_bank_name,iss_bank_code,in_out_sign,iss_country,cust_type,personal_id_type,organization_id_type,other_id_name,id_code,spv_open_bank,other_open_bank,cust_name,sex,risk_level,moble,tel_phone,email,register_date,register_serno,remark,imp_date,register_status,op_type,report_date,ta_id", auth = APIAuth.NO)
	public int deleteCustRegistMark(SqlParam<CustRegistMark> params) throws Exception {
		return custRegistMarkDao.deleteCustRegistMark(params).getEffect();
	}

	public void addImportSubseqSubscrRegist(TrCustRegisterInfo custRegisterInfo, String opType) throws Exception {
		CustRegistMark custRegistMark = BeanUtil.copyProperties(custRegisterInfo, CustRegistMark.class);
		custRegistMark.setOpType(opType);
		custRegistMark.setSummitUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username")));
		custRegistMarkDao.addImportCustRegistMark(custRegistMark).getEffect();
	}
}
