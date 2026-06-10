package com.kayak.rpt.zz.manage.service;

import cn.hutool.core.bean.BeanUtil;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.dao.DaoService;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.graphql.model.FetcherData;
import com.kayak.rpt.zz.manage.dao.AssetRegistInfoDao;
import com.kayak.rpt.zz.manage.enums.OperatorEnum;
import com.kayak.rpt.zz.manage.model.AssetRegistInfo;
import com.kayak.rpt.zz.manage.util.CheckDataParams;
import com.kayak.rpt.zz.operate.service.AssetRegistService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "资产持仓管理服务", model = AssetRegistInfo.class)
public class AssetRegistInfoService {

	@Autowired
	private AssetRegistInfoDao assetRegistInfoDao;

	@Autowired
	private AssetRegistService assetRegistService;
	@Autowired
	CheckDataForVueService checkDataForVueService;

	@Autowired
	protected DaoService daoService;

	CheckDataParams checkDataParams = new CheckDataParams();
	@API(desc = "查询资产持仓管理信息", auth = APIAuth.YES)
	public SqlResult<AssetRegistInfo> findAssetRegistInfos(SqlParam<AssetRegistInfo> params) throws Exception {
//		params.setMakeSql(true);
		return assetRegistInfoDao.findAssetRegistInfos(params);
	}

	@API(desc = "查询资产持仓管理信息（日表）", auth = APIAuth.YES)
	public SqlResult<AssetRegistInfo> findAssetRegistInfos_day(SqlParam<AssetRegistInfo> params) throws Exception {
		params.setMakeSql(true);
		return assetRegistInfoDao.findAssetRegistInfos_day(params);
	}

	@API(desc = "添加资产持仓管理", params = "account_code,asset_code,bank_code,cny,create_date,details,fair_value,fair_value_cny,fl_valuation,holding_date,holding_type,imp_date,invested_amount,invested_amount_cny,invested_asset,mezzanine_asset_code,mezzanine_number,net_valuation,prod_reg_enc,quantity,register_date,register_serno,register_status,theory_report_end_date,theory_report_start_date", auth = APIAuth.YES)
	public int addAssetRegistInfo(SqlParam<AssetRegistInfo> params) throws Exception {
		// 操作记录
//		assetRegistService.addAssetRegist(params, OperatorEnum.CREATE.getVal());
		return assetRegistInfoDao.addAssetRegistInfo(params).getEffect();
	}
	
	@API(desc = "修改资产持仓管理", params = "account_code,asset_code,bank_code,cny,create_date,details,fair_value,fair_value_cny,fl_valuation,holding_date,holding_type,imp_date,invested_amount,invested_amount_cny,invested_asset,mezzanine_asset_code,mezzanine_number,net_valuation,prod_reg_enc,quantity,register_date,register_serno,register_status,theory_report_end_date,theory_report_start_date", auth = APIAuth.YES)
	public String updateAssetRegistInfo(SqlParam<AssetRegistInfo> params) throws Exception {
		// 操作记录
		try {
		checkDataParams.initDataNoDict();
		String whiteregex = CheckDataParams.whiteregex;
		String whitereForCode = CheckDataParams.whitereForCode;
		String checkErr = checkDataForVueService.assetRegistInfoCheckForVue(whiteregex,whitereForCode,params.getModel());
		if (StringUtils.isNotBlank(checkErr)) {
			return RequestSupport.updateReturnJson(false,  "修改失败！错误信息为：\n"+checkErr, null).toString();
		}

		// 操作记录
		Map paramMap = new HashMap<>();
		paramMap.put("registerSerno",params.getModel().getRegisterSerno());
		SqlParam<AssetRegistInfo> oldParams =  new FetcherData<>(paramMap,AssetRegistInfo.class);
		SqlResult<AssetRegistInfo> originParams =  assetRegistInfoDao.findAssetRegistInfos(oldParams);
		if(originParams.getRows().size()>0){
			AssetRegistInfo param = 	originParams.getRows().get(0);
			paramMap = BeanUtil.beanToMap(param);
			oldParams =  new FetcherData<>(paramMap,AssetRegistInfo.class);
		}
		assetRegistService.addAssetRegist(oldParams, OperatorEnum.UPDATE.getVal());
		assetRegistInfoDao.updateAssetRegistInfo(params);
		return RequestSupport.updateReturnJson(true,  "修改成功！", null).toString();
	} catch (Exception e) {
		e.printStackTrace();
		return RequestSupport.updateReturnJson(false,  "修改失败，数据库错误信息为："+e.getMessage(), null).toString();
	}
	}
	
	@API(desc = "删除资产持仓管理", params = "account_code,asset_code,bank_code,cny,create_date,details,fair_value,fair_value_cny,fl_valuation,holding_date,holding_type,imp_date,invested_amount,invested_amount_cny,invested_asset,mezzanine_asset_code,mezzanine_number,net_valuation,prod_reg_enc,quantity,register_date,register_serno,register_status,theory_report_end_date,theory_report_start_date", auth = APIAuth.YES)
	public int deleteAssetRegistInfo(SqlParam<AssetRegistInfo> params) throws Exception {
		// 操作记录
		assetRegistService.addAssetRegist(params, OperatorEnum.DELETE.getVal());
		return assetRegistInfoDao.deleteAssetRegistInfo(params).getEffect();
	}

	@API(desc = "查询报送状态为0,1的数据", auth = APIAuth.NO)
	public String getAbnormalData(SqlParam<AssetRegistInfo> params) throws Exception {
		try {
			int  recordCnt = assetRegistInfoDao.findAssetRegistInfosCount(params);
			if (recordCnt == 0) {
				return RequestSupport.updateReturnJson(false,  "没有需要确认并导出的数据，请检查！", null).toString();
			}
			int unreadyCnt= assetRegistInfoDao.findAssetRegistInfosFailStatus(params);
			if (unreadyCnt > 0) {
				return RequestSupport.updateReturnJson(false,  "存在报送状态异常(0 初始化 或 1 校验失败)的数据，请处理后导出！", null).toString();
			}
			return RequestSupport.updateReturnJson(true,  "", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "查询失败，请检查", null).toString();
		}
	}
    /**手动更新对应条件数据的报送状态为  报送成功**/
	@API(desc = "确认并导出持仓登记",  auth = APIAuth.YES)
	public String updateAssetRegistInfoStatus(SqlParam<AssetRegistInfo> params) throws Exception {
		try {
			//查询 0 初始化 或 1 校验失败的数据。存在需提前处理
			int  recordCnt = assetRegistInfoDao.findAssetRegistInfosCount(params);
			if (recordCnt == 0) {
				return RequestSupport.updateReturnJson(false,  "没有需要确认报送状态的数据，请检查！", null).toString();
			}
			int unreadyCnt= assetRegistInfoDao.findAssetRegistInfosFailStatus(params);
			if (unreadyCnt > 0) {
				return RequestSupport.updateReturnJson(false,  "存在报送状态异常(0 初始化 或 1 校验失败)的数据，请处理后确认报送状态！", null).toString();
			}

			daoService.doTrans(() -> {
				assetRegistInfoDao.updateAssetRegistInfoStatus(params);
				assetRegistInfoDao.updateBaseReportResultInfo(params);
			});
			return RequestSupport.updateReturnJson(true,  "操作成功！", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "操作失败，请检查!", null).toString();
		}
	}
	/**
	 * 全量导入，先删除后插入
	 * @param AssetRegistInfos
	 * @param params
	 * @throws Exception
	 */
	public void importAssetRegistInfo(List<AssetRegistInfo> AssetRegistInfos,Map<String, Object> params) throws Exception {
		// 添加至操作记录
		assetRegistInfoDao.deleteImportAssetRegistInfo(params);
		for (AssetRegistInfo AssetRegistInfo : AssetRegistInfos) {
			Map<String, Object> map = BeanUtil.beanToMap(AssetRegistInfo);
//			assetRegistService.addImportAssetRegist(AssetRegistInfo,OperatorEnum.IMPORT.getVal());
			assetRegistInfoDao.addImportAssetRegistInfo(map);
		}
	}

	/**
	 * 增量导入，有相同的数据-新数据覆盖老数据，无相同的-插入
	 * @param AssetRegistInfos
	 * @param params
	 * @throws Exception
	 */
	public void addCoverImportAssetRegistInfo(List<AssetRegistInfo> AssetRegistInfos,Map<String, Object> params) throws Exception {
		// 需要按主键查询是否已存在，已存在的　走更新操作。不存在的直接插入。 (导入无id,表中无业务性主键，目前是按指标中的重复性校验判断重复)
		for (AssetRegistInfo assetRegistInfo : AssetRegistInfos) {
			Map<String, Object> map = BeanUtil.beanToMap(assetRegistInfo);
			String checkId = assetRegistInfoDao.countAssetRegistInfo(assetRegistInfo);
			System.out.println("********************************** checkId **************:"+checkId);
			if(checkId == null || "".equals(checkId)){
				assetRegistInfoDao.addImportAssetRegistInfo(map);
			}else{
				assetRegistInfo.setId(checkId);
				assetRegistInfoDao.updateRepeatAssetRegistInfo(assetRegistInfo);
			}
		}
	}
}
