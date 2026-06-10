package com.kayak.rpt.zz.manage.service;

import cn.hutool.core.bean.BeanUtil;
import com.kayak.core.dao.DaoService;
import com.kayak.core.system.RequestSupport;
import com.kayak.graphql.model.FetcherData;
import com.kayak.rpt.zz.manage.enums.OperatorEnum;
import com.kayak.rpt.zz.manage.model.AssetRegistInfo;
import com.kayak.rpt.zz.manage.model.ProdTransRegistInfo;
import com.kayak.rpt.zz.manage.model.SubseqSubscrRegistInfo;
import com.kayak.rpt.zz.manage.util.CheckDataParams;
import com.kayak.rpt.zz.operate.service.UnderAssetRegistService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.manage.dao.UnderAssetRegistInfoDao;
import com.kayak.rpt.zz.manage.model.UnderAssetRegistInfo;

import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "底层资产持仓管理服务", model = UnderAssetRegistInfo.class)
public class UnderAssetRegistInfoService {

	@Autowired
	private UnderAssetRegistInfoDao underAssetRegistInfoDao;

	@Autowired
	private UnderAssetRegistService underAssetRegistService;
	@Autowired
	protected DaoService daoService;

	@Autowired
	CheckDataForVueService checkDataForVueService;

	CheckDataParams checkDataParams = new CheckDataParams();

	@API(desc = "查询底层资产持仓管理信息", auth = APIAuth.YES)
	public SqlResult<UnderAssetRegistInfo> findUnderAssetRegistInfos(SqlParam<UnderAssetRegistInfo> params) throws Exception {
//		params.setMakeSql(true);
		return underAssetRegistInfoDao.findUnderAssetRegistInfos(params);
	}

	@API(desc = "查询底层资产持仓管理信息（日报）", auth = APIAuth.YES)
	public SqlResult<UnderAssetRegistInfo> findUnderAssetRegistInfos_day(SqlParam<UnderAssetRegistInfo> params) throws Exception {
		params.setMakeSql(true);
		return underAssetRegistInfoDao.findUnderAssetRegistInfos_day(params);
	}

	@API(desc = "添加底层资产持仓管理", params = "bank_code,asset_manager_code,convert_sum_amt,asset_sum_number,non_invested_amt,under_asset_code,under_asset_sum,under_convert_sum_amt,report_date,register_serno,imp_date,register_date,register_status", auth = APIAuth.YES)
	public int addUnderAssetRegistInfo(SqlParam<UnderAssetRegistInfo> params) throws Exception {
		// 操作记录
//		underAssetRegistService.addUnderAssetRegist(params, OperatorEnum.CREATE.getVal());
		return underAssetRegistInfoDao.addUnderAssetRegistInfo(params).getEffect();
	}
	
	@API(desc = "修改底层资产持仓管理", params = "bank_code,asset_manager_code,convert_sum_amt,asset_sum_number,non_invested_amt,under_asset_code,under_asset_sum,under_convert_sum_amt,report_date,register_serno,imp_date,register_date,register_status", auth = APIAuth.YES)
	public String updateUnderAssetRegistInfo(SqlParam<UnderAssetRegistInfo> params) throws Exception {
		try {
			checkDataParams.initDataNoDict();
			String whiteregex = CheckDataParams.whiteregex;
			String whitereForCode = CheckDataParams.whitereForCode;
			String checkErr = checkDataForVueService.underAssetRegistInfoCheckForVue(whiteregex,whitereForCode,params.getModel());
			if (StringUtils.isNotBlank(checkErr)) {
				return RequestSupport.updateReturnJson(false,  "修改失败！错误信息为：\n"+checkErr, null).toString();
			}
			// 操作记录
			SqlResult<UnderAssetRegistInfo> originParams =  underAssetRegistInfoDao.findUnderAssetRegistInfos(params);
			Map paramMap = null;
			if(originParams.getRows().size()>0){
				UnderAssetRegistInfo param = 	originParams.getRows().get(0);
				paramMap = BeanUtil.beanToMap(param);
			}
			SqlParam<UnderAssetRegistInfo> oldParams =  new FetcherData<>(paramMap,UnderAssetRegistInfo.class);
			underAssetRegistService.addUnderAssetRegist(oldParams, OperatorEnum.UPDATE.getVal());
			underAssetRegistInfoDao.updateUnderAssetRegistInfo(params);
			return RequestSupport.updateReturnJson(true,  "修改成功！", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "修改失败，数据库错误信息为："+e.getMessage(), null).toString();
		}

	}
	
	@API(desc = "删除底层资产持仓管理", params = "bank_code,asset_manager_code,convert_sum_amt,asset_sum_number,non_invested_amt,under_asset_code,under_asset_sum,under_convert_sum_amt,report_date,register_serno,imp_date,register_date,register_status", auth = APIAuth.YES)
	public int deleteUnderAssetRegistInfo(SqlParam<UnderAssetRegistInfo> params) throws Exception {
		// 操作记录
		underAssetRegistService.addUnderAssetRegist(params, OperatorEnum.DELETE.getVal());
		return underAssetRegistInfoDao.deleteUnderAssetRegistInfo(params).getEffect();
	}

	public void importUnderAssetRegistInfo(List<UnderAssetRegistInfo> underAssetRegistInfos,Map<String, Object> params) throws Exception {
		// 添加至操作记录
		underAssetRegistInfoDao.deleteImportUnderAssetRegistInfo(params);
		for (UnderAssetRegistInfo underAssetRegistInfo : underAssetRegistInfos) {
			Map<String, Object> map = BeanUtil.beanToMap(underAssetRegistInfo);
//			underAssetRegistService.addImportUnderAssetRegist(underAssetRegistInfo,OperatorEnum.IMPORT.getVal());
			underAssetRegistInfoDao.addImportUnderAssetRegistInfo(map);
		}
	}

	@API(desc = "查询报送状态为0,1的数据", auth = APIAuth.NO)
	public String getAbnormalData(SqlParam<UnderAssetRegistInfo> params) throws Exception {
		try {
			int  recordCnt = underAssetRegistInfoDao.findUnderAssetRegistInfosCount(params);
			if (recordCnt == 0) {
				return RequestSupport.updateReturnJson(false,  "没有需要确认并导出的数据，请检查！", null).toString();
			}
			int unreadyCnt= underAssetRegistInfoDao.findUnderAssetRegistInfoFailStatus(params);
			if (unreadyCnt > 0) {
				return RequestSupport.updateReturnJson(false,  "存在报送状态异常(0 初始化 或 1 校验失败)的数据，请处理后导出！", null).toString();
			}
			return RequestSupport.updateReturnJson(true,  "", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "查询失败，请检查", null).toString();
		}
	}

	@API(desc = "确认并导出底层持仓登记",  auth = APIAuth.YES)
	public String updateUnderAssetRegistInfoStatus(SqlParam<UnderAssetRegistInfo> params) throws Exception {
		try {
			daoService.doTrans(() -> {
				underAssetRegistInfoDao.updateUnderAssetRegistInfoStatus(params);
				underAssetRegistInfoDao.updateBaseReportResultInfo(params);
			});
			return RequestSupport.updateReturnJson(true,  "操作成功！", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "操作失败，请检查!", null).toString();
		}
	}
}
