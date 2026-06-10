package com.kayak.dps.app.service;

import com.kayak.aspect.annotations.APIOperation;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.dps.app.dao.MidDirectFusionDao;
import com.kayak.dps.app.model.AssetRightModel;
import com.kayak.dps.app.model.MidDirectFusion;
import com.kayak.dps.check.util.NextVersionUtil;
import com.kayak.dps.ods.service.AssetCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 直融工具
 * axin
 * 20230613
 */

@Service
@APIDefine(desc = "直融信息服务", model = MidDirectFusion.class)
public class MidDirectFusionService {

	@Autowired
	private MidDirectFusionDao midDirectFusionDao;

	@Resource(name = "assetCollectionService")
	private AssetCollectionService assetCollectionService;

	@API(desc = "查询城市下拉框", auth = APIAuth.NO)
	public SqlResult<MidDirectFusion> findCity(SqlParam<MidDirectFusion> params) throws Exception {
		params.setMakeSql(false);
		return midDirectFusionDao.findCity(params);
	}

	@API(desc = "查询资产下拉框", auth = APIAuth.NO)
	public SqlResult<MidDirectFusion> findScrCd(SqlParam<MidDirectFusion> params) throws Exception {
		params.setMakeSql(false);
		return midDirectFusionDao.findScrCd(params);
	}

	@API(desc = "查询直融信息", auth = APIAuth.YES, operation = APIOperation.SELECT)
	public SqlResult<MidDirectFusion> findMidDirectFusionInfos(SqlParam<MidDirectFusion> params) throws Exception {
		//浮息
		List<MidDirectFusion> FXAll =midDirectFusionDao.findBondFX(params).getRows();
		//还本
		List<MidDirectFusion> HBAll = midDirectFusionDao.findBondHB(params).getRows();
		//行权
		List<MidDirectFusion> XQAll = midDirectFusionDao.findBondXQ(params).getRows();
		SqlResult<MidDirectFusion> pm = midDirectFusionDao.findMidDirectFusions(params);
		for (MidDirectFusion m :pm.getRows()) {
			List<MidDirectFusion> FXForId = new ArrayList<>();
			List<MidDirectFusion> HBForId = new ArrayList<>();
			List<MidDirectFusion> XQForId = new ArrayList<>();
			for (MidDirectFusion ma :FXAll) {
				if (m.getScrId().equals(ma.getScrId())){
					FXForId.add(ma);
				}
			}
			for (MidDirectFusion mb :HBAll) {
				if (m.getScrId().equals(mb.getScrId())){
					HBForId.add(mb);
				}
			}
			for (MidDirectFusion mc :XQAll) {
				if (m.getScrId().equals(mc.getScrId())){
					XQForId.add(mc);
				}
			}
			m.setCouponTypeGridData(FXForId);
			m.setIsRepaidGridData(HBForId);
			m.setEmbOptFGridData(XQForId);
		}
		return pm;
	}

	//只作为页面权限控制
	@API(desc = "新增直融信息", auth = APIAuth.YES, operation = APIOperation.INSTER)
	public String addMidDirectFusion(SqlParam<MidDirectFusion> params){
		try {
			//校验唯一性
			Map<String, Object> param = new HashMap<>();
			param.put("checkTableName","ods_direct_bas_inf");
			param.put("scrId",params.getModel().getScrId());
			if(assetCollectionService.isOnlyOne(param)>0){
				return RequestSupport.updateReturnJson(false,  "存在有相同的理财直融信息，新增失败！", null).toString();
			}
			params.getModel().setCrtDate(SysUtil.getSystemParamsByParaid("10004"));
			params.getModel().setCrtTime(DateUtil.getNowTime());
			params.getModel().setCrtUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
			params.getModel().setDealDate(DateUtil.getNowDate());
//			int n =midDirectFusionDao.findAssetCount(params);
//			if(n>0){
//				return RequestSupport.updateReturnJson(false, "存在有相同的理财直融信息，新增失败", null).toString();
//			}
			midDirectFusionDao.addMidDirectFusion(params.getModel());
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,"新增失败！" + e.getMessage() ,null).toString();
		}
		return RequestSupport.updateReturnJson(true,"新增成功！",null).toString();
	}
	
	@API(desc = "修改直融信息", auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public String updateMidDirectFusion(SqlParam<MidDirectFusion> params) throws Exception {
		try {
			params.getModel().setUpdDate(SysUtil.getSystemParamsByParaid("10004"));
			params.getModel().setUpdTime(DateUtil.getNowTime());
			params.getModel().setUpdUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
			params.getModel().setDealDate(DateUtil.getNowDate());
			midDirectFusionDao.updateMidDirectFusion(params.getModel());
		} catch (Exception ex) {
			ex.printStackTrace();
			return RequestSupport.updateReturnJson(false,"修改失败！" + ex.getMessage() ,null).toString();
		}
		return RequestSupport.updateReturnJson(true,"修改成功！",null).toString();
	}

	@API(desc = "补录直融信息", auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public String updateMidDirectSupplyFusion(SqlParam<MidDirectFusion> params) throws Exception {
		try {
			params.getModel().setUpdDate(SysUtil.getSystemParamsByParaid("10004"));
			params.getModel().setUpdTime(DateUtil.getNowTime());
			params.getModel().setUpdUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
			params.getModel().setVersion(NextVersionUtil.getNextVersion(params.getModel().getVersion()));
			params.getModel().setDealDate(DateUtil.getNowDate());
			DaoUtil.doTrans(() -> {
				midDirectFusionDao.updateMidDirectFusionBl(params.getModel());
				midDirectFusionDao.deleteBondXQ(params.getModel());
				midDirectFusionDao.deleteBondHB(params.getModel());
				midDirectFusionDao.deleteBondFX(params.getModel());
				// 行权
				if(params.getModel().getEmbOptFGridData()!=null) {
					for (MidDirectFusion embOptFGridData : params.getModel().getEmbOptFGridData()) {
						embOptFGridData.setScrId(params.getModel().getScrId());
						embOptFGridData.setCrtDate(params.getModel().getUpdDate());
						embOptFGridData.setCrtTime(params.getModel().getUpdTime());
						embOptFGridData.setCrtUser(params.getModel().getUpdUser());
						embOptFGridData.setDealDate(params.getModel().getDealDate());
						midDirectFusionDao.insertBondXQ(embOptFGridData);
					}
				}
				// 还本
				if(params.getModel().getIsRepaidGridData()!=null) {
					for (MidDirectFusion isRepaidGridData : params.getModel().getIsRepaidGridData()) {
						isRepaidGridData.setScrId(params.getModel().getScrId());
						isRepaidGridData.setCrtDate(params.getModel().getUpdDate());
						isRepaidGridData.setCrtTime(params.getModel().getUpdTime());
						isRepaidGridData.setCrtUser(params.getModel().getUpdUser());
						isRepaidGridData.setDealDate(params.getModel().getDealDate());
						midDirectFusionDao.insertBondHB(isRepaidGridData);
					}
				}
				// 浮息
				if(params.getModel().getCouponTypeGridData()!=null) {
					for (MidDirectFusion couponTypeGridData : params.getModel().getCouponTypeGridData()) {
						couponTypeGridData.setScrId(params.getModel().getScrId());
						couponTypeGridData.setCrtDate(params.getModel().getUpdDate());
						couponTypeGridData.setCrtTime(params.getModel().getUpdTime());
						couponTypeGridData.setCrtUser(params.getModel().getUpdUser());
						couponTypeGridData.setDealDate(params.getModel().getDealDate());
						couponTypeGridData.setScrCd(params.getModel().getScrCd());
						midDirectFusionDao.insertBondFX(couponTypeGridData);
					}
				}

			});

			return RequestSupport.updateReturnJson(true,"补录成功！",null).toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			return RequestSupport.updateReturnJson(false,"补录失败！" + ex.getMessage() ,null).toString();
		}
	}

	@API(desc = "删除直融信息",  auth = APIAuth.YES, operation = APIOperation.DELETE)
	public String deleteMidDirectFusion(SqlParam<MidDirectFusion> params) throws Exception {
		try {
			DaoUtil.doTrans(() -> {
				midDirectFusionDao.deleteMidDirectFusion(params.getModel().getScrId());
				midDirectFusionDao.deleteBondXQ(params.getModel());
				midDirectFusionDao.deleteBondFX(params.getModel());
				midDirectFusionDao.deleteBondHB(params.getModel());
			});
			return RequestSupport.updateReturnJson(true,"删除成功",null).toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			return RequestSupport.updateReturnJson(false,"删除失败！" + ex.getMessage() ,null).toString();
		}

	}

	@API(desc = "导出直融信息", auth = APIAuth.YES,operation = APIOperation.UPDATE)
	public void dirExcelDownloadAction(SqlParam<MidDirectFusion> params) throws Exception {}

}
