package com.kayak.pms.disclosureControl.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.pms.disclosureControl.dao.DisclosureNoticeVersionDao;
import com.kayak.pms.disclosureControl.model.DisclosureNoticeVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "信披公告版本信息表服务", model = DisclosureNoticeVersion.class)
public class DisclosureNoticeVersionService {

	@Autowired
	private DisclosureNoticeVersionDao disclosureNoticeVersionDao;

	@API(desc = "查询信披公告版本信息表信息", auth = APIAuth.NO)
	public SqlResult<DisclosureNoticeVersion> findDisclosureNoticeVersions(SqlParam<DisclosureNoticeVersion> params) throws Exception {
		return disclosureNoticeVersionDao.findDisclosureNoticeVersions(params);
	}

	/**
	 * 查询信披公告版本信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@API(desc = "查询", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public SqlResult<DisclosureNoticeVersion> findDisclosureNoticeVersion1(SqlParam<DisclosureNoticeVersion> params) throws Exception {
		return findDisclosureNoticeVersion(params);
	}

	/**
	 * 查询信披公告版本信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@API(desc = "查询", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public SqlResult<DisclosureNoticeVersion> findDisclosureNoticeVersion(SqlParam<DisclosureNoticeVersion> params) throws Exception {
		return disclosureNoticeVersionDao.findDisclosureNoticeVersion(params);
	}

	@API(desc = "添加信披公告版本信息表", params = "id,prod_code,t8_disclosure_notice_id,version,doc_type,crt_date,crt_time,crt_user_id,crt_user_name,upd_date,upd_time,upd_user_id,upd_user_name,file_path,file_name,remark", auth = APIAuth.NO)
	public int addDisclosureNoticeVersion(SqlParam<DisclosureNoticeVersion> params) throws Exception {
		return disclosureNoticeVersionDao.addDisclosureNoticeVersion(params).getEffect();
	}
	
	@API(desc = "修改信披公告版本信息表", params = "id,prod_code,t8_disclosure_notice_id,version,doc_type,crt_date,crt_time,crt_user_id,crt_user_name,upd_date,upd_time,upd_user_id,upd_user_name,file_path,file_name,remark", auth = APIAuth.NO)
	public int updateDisclosureNoticeVersion(SqlParam<DisclosureNoticeVersion> params) throws Exception {
		return disclosureNoticeVersionDao.updateDisclosureNoticeVersion(params).getEffect();
	}
	
	@API(desc = "删除信披公告版本信息表", params = "id,prod_code,t8_disclosure_notice_id,version,doc_type,crt_date,crt_time,crt_user_id,crt_user_name,upd_date,upd_time,upd_user_id,upd_user_name,file_path,file_name,remark", auth = APIAuth.NO)
	public int deleteDisclosureNoticeVersion(SqlParam<DisclosureNoticeVersion> params) throws Exception {
		return disclosureNoticeVersionDao.deleteDisclosureNoticeVersion(params).getEffect();
	}
	
	
	 @API(desc = "查询产品信披最新公告信息", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public DisclosureNoticeVersion findDisclosureNewNotice(String  noticeId) throws Exception {
	
        return disclosureNoticeVersionDao.findDisclosureNewNotice(noticeId);
    }
	 
	
	 public int updateNoticeVersion(DisclosureNoticeVersion noticeVersion)  throws Exception {
		 
		 return disclosureNoticeVersionDao.updateNoticeVersion(noticeVersion);
	 }
	/**
	 * 信披公告版本管理-批量下载权限控制
	 * @return
	 */
	@API(desc = "批量下载",auth = APIAuth.YES)
	public String downLoadsRightControl() {
		return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
	}

	/**
	 * 信披公告版本管理-下载权限控制
	 * @return
	 */
	@API(desc = "下载",auth = APIAuth.YES)
	public String downLoadRightControl() {
		return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
	}

	@API(desc = "导出",auth = APIAuth.YES)
	public String exportProdRuleRightControl() {
		return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
	}

	@API(desc = "查询手工公告版本信息", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public SqlResult<DisclosureNoticeVersion> findManualVersionsInfoById(SqlParam<DisclosureNoticeVersion> params) throws Exception {
		return disclosureNoticeVersionDao.findManualVersionsInfoById(params);
	}
}
