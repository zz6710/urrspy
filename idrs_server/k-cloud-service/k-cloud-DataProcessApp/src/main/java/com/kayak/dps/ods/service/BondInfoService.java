package com.kayak.dps.ods.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.dps.app.model.BondInfoModel;
import com.kayak.dps.check.util.NextVersionUtil;
import com.kayak.dps.ods.dao.BondInfoDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service
@APIDefine(desc = "债券信息服务", model = BondInfoModel.class)
public class BondInfoService {
    @Resource(name = "bondInfoDao")
    private BondInfoDao bondInfoDao;

    private static final int ONE = 1;
    private static final int ZERO = 0;
    /**
     * 新增债券信息
     * @param params
     * @throws Exception
     */
    @API(desc = "新增债券信息",operation = APIOperation.INSTER, auth = APIAuth.YES)
    public String addBondInfoModel(SqlParam<BondInfoModel> params) throws Exception {
        try {
            params.getModel().setCrtDate(SysUtil.getSystemParamsByParaid("10004"));
            params.getModel().setCrtTime(DateUtil.getNowTime());
            params.getModel().setCrtUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
            params.getModel().setDealDate(DateUtil.getNowDate());
            DaoUtil.doTrans(() -> {
                bondInfoDao.insertBondInfo(params);
                //删行权、还本、浮息信息
                bondInfoDao.deleteBondXQ(params.getModel());
                bondInfoDao.deleteBondHB(params.getModel());
                bondInfoDao.deleteBondFX(params.getModel());
                // 行权
                if (params.getModel().getEmbOptFGridData()!=null) {
                    for (BondInfoModel embOptFGridData : params.getModel().getEmbOptFGridData()) {
                        embOptFGridData.setScrId(params.getModel().getScrId());
                        embOptFGridData.setCrtDate(params.getModel().getCrtDate());
                        embOptFGridData.setCrtTime(params.getModel().getCrtTime());
                        embOptFGridData.setCrtUser(params.getModel().getCrtUser());
                        embOptFGridData.setDealDate(params.getModel().getDealDate());
                        bondInfoDao.insertBondXQ(embOptFGridData);
                    }
                }
                // 还本
                if (params.getModel().getIsRepaidGridData()!=null) {
                    for (BondInfoModel isRepaidGridData : params.getModel().getIsRepaidGridData()) {
                        isRepaidGridData.setScrId(params.getModel().getScrId());
                        isRepaidGridData.setCrtDate(params.getModel().getCrtDate());
                        isRepaidGridData.setCrtTime(params.getModel().getCrtTime());
                        isRepaidGridData.setCrtUser(params.getModel().getCrtUser());
                        isRepaidGridData.setDealDate(params.getModel().getDealDate());
                        bondInfoDao.insertBondHB(isRepaidGridData);
                    }
                }
                // 浮息
                if (params.getModel().getCouponTypeGridData()!=null) {
                    for (BondInfoModel couponTypeGridData : params.getModel().getCouponTypeGridData()) {
                        couponTypeGridData.setScrId(params.getModel().getScrId());
                        couponTypeGridData.setCrtDate(params.getModel().getCrtDate());
                        couponTypeGridData.setCrtTime(params.getModel().getCrtTime());
                        couponTypeGridData.setCrtUser(params.getModel().getCrtUser());
                        couponTypeGridData.setDealDate(params.getModel().getDealDate());
                        bondInfoDao.insertBondFX(couponTypeGridData);
                    }
                }
            });
            return RequestSupport.updateReturnJson(true,  "新增成功！", null).toString();
        } catch (Exception e) {
            log.error("新增失败！");
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,  "新增失败！", null).toString();
        }
    }

    /**
     * 查询债券信息
     * @param params
     * @throws Exception
     */
    @API(desc = "查询债券信息",operation = APIOperation.SELECT, auth = APIAuth.YES)
    public SqlResult<BondInfoModel> findBondInfoModels(SqlParam<BondInfoModel> params) throws Exception {

        SqlResult<BondInfoModel> pm = bondInfoDao.findBondInfo(params);
        for (BondInfoModel m :pm.getRows()) {
            Map<String,Object> map =new HashMap<>();
            map.put("scrId",m.getScrId());
            //浮息
            List<BondInfoModel> FXAll =bondInfoDao.findBondFX(map);
            //还本
            List<BondInfoModel> HBAll = bondInfoDao.findBondHB(map);
            //行权
            List<BondInfoModel> XQAll = bondInfoDao.findBondXQ(map);
            List<BondInfoModel> FXForId = new ArrayList<>();
            List<BondInfoModel> HBForId = new ArrayList<>();
            List<BondInfoModel> XQForId = new ArrayList<>();
            for (BondInfoModel ma :FXAll) {
                if (m.getScrId().equals(ma.getScrId())){
                    FXForId.add(ma);
                }
            }
            for (BondInfoModel mb :HBAll) {
                if (m.getScrId().equals(mb.getScrId())){
                    HBForId.add(mb);
                }
            }
            for (BondInfoModel mc :XQAll) {
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

    /**
     * 查询债券信息代码与名称 去重
     * @param params
     * @throws Exception
     */
    @API(desc = "查询债券信息代码与名称",operation = APIOperation.SELECT, auth = APIAuth.NO)
    public SqlResult<BondInfoModel> findBondInfoModelsCdAndNm(SqlParam<BondInfoModel> params) throws Exception {
        return bondInfoDao.findBondInfoCdAndNm(params);
    }

    /**
     * 市场查债券
     * @param params
     * @return
     * @throws Exception
     */
    @API(desc = "查询债券信息代码与名称根据市场代码",operation = APIOperation.SELECT, auth = APIAuth.NO)
    public SqlResult<BondInfoModel> findBondInfoModelsCdAndNmByTrxMkt(SqlParam<BondInfoModel> params) throws Exception {
        return bondInfoDao.findBondInfoCdAndNmByTrxMkt(params);
    }


    /**
     * 修改债券信息
     * @param params
     * @throws Exception
     */
    @API(desc = "修改债券信息",operation = APIOperation.UPDATE, auth = APIAuth.YES)
    public String updateBondInfoModel(SqlParam<BondInfoModel> params) throws Exception {
        try {
            params.getModel().setUpdDate(SysUtil.getSystemParamsByParaid("10004"));
            params.getModel().setUpdTime(DateUtil.getNowTime());
            params.getModel().setUpdUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
            params.getModel().setDealDate(DateUtil.getNowDate());
            params.getModel().setCrtDate(SysUtil.getSystemParamsByParaid("10004"));
            params.getModel().setCrtTime(DateUtil.getNowTime());
            params.getModel().setCrtUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
            DaoUtil.doTrans(() -> {
                bondInfoDao.updateBondInfo(params).getEffect();
                bondInfoDao.updateBondInfoBlField(params).getEffect();
                //删行权、还本、浮息信息
                bondInfoDao.deleteBondXQ(params.getModel());
                bondInfoDao.deleteBondHB(params.getModel());
                bondInfoDao.deleteBondFX(params.getModel());
                // 行权
                if (params.getModel().getEmbOptFGridData()!=null) {
                    for (BondInfoModel embOptFGridData : params.getModel().getEmbOptFGridData()) {
                        embOptFGridData.setScrId(params.getModel().getScrId());
                        embOptFGridData.setCrtDate(params.getModel().getUpdDate());
                        embOptFGridData.setCrtTime(params.getModel().getUpdTime());
                        embOptFGridData.setCrtUser(params.getModel().getUpdUser());
                        embOptFGridData.setDealDate(params.getModel().getDealDate());
                        bondInfoDao.insertBondXQ(embOptFGridData);
                    }
                }
                //
                if (params.getModel().getIsRepaidGridData()!=null) {
                    for (BondInfoModel isRepaidGridData : params.getModel().getIsRepaidGridData()) {
                        isRepaidGridData.setScrId(params.getModel().getScrId());
                        isRepaidGridData.setCrtDate(params.getModel().getCrtDate());
                        isRepaidGridData.setCrtTime(params.getModel().getCrtTime());
                        isRepaidGridData.setCrtUser(params.getModel().getCrtUser());
                        isRepaidGridData.setDealDate(params.getModel().getDealDate());
                        bondInfoDao.insertBondHB(isRepaidGridData);
                    }
                }
                // 浮息
                if (params.getModel().getCouponTypeGridData()!=null) {
                    for (BondInfoModel couponTypeGridData : params.getModel().getCouponTypeGridData()) {
                        couponTypeGridData.setScrId(params.getModel().getScrId());
                        couponTypeGridData.setCrtDate(params.getModel().getCrtDate());
                        couponTypeGridData.setCrtTime(params.getModel().getCrtTime());
                        couponTypeGridData.setCrtUser(params.getModel().getCrtUser());
                        couponTypeGridData.setDealDate(params.getModel().getDealDate());
                        couponTypeGridData.setScrCd(params.getModel().getScrCd());
                        bondInfoDao.insertBondFX(couponTypeGridData);
                    }
                }
            });
            return RequestSupport.updateReturnJson(true, "修改成功！", null).toString();
        } catch (Exception e) {
            log.error("修改失败！");
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,  "修改失败！", null).toString();
        }
    }



    /**
     * 补录债券信息
     * @param params
     * @throws Exception
     */
    @API(desc = "补录债券信息",operation = APIOperation.UPDATE, auth = APIAuth.YES)
    public String blBondInfoModel(SqlParam<BondInfoModel> params) throws Exception {
        try {
            params.getModel().setUpdDate(SysUtil.getSystemParamsByParaid("10004"));
            params.getModel().setUpdTime(DateUtil.getNowTime());
            params.getModel().setUpdUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
            params.getModel().setVersion(NextVersionUtil.getNextVersion(params.getModel().getVersion()));
            params.getModel().setDealDate(DateUtil.getNowDate());
            bondInfoDao.updateBondInfoBl(params);
            return RequestSupport.updateReturnJson(true,  "补录成功！", null).toString();
        } catch (Exception e) {
            log.error("补录失败！");
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,  "补录失败！", null).toString();
        }
    }



    /**
     * 删除债券信息
     * @param params
     * @throws Exception
     */
    @API(desc = "删除债券信息",operation = APIOperation.DELETE, auth = APIAuth.YES)
    public String deleteBondInfoModel(SqlParam<BondInfoModel> params) throws Exception {
        try {
            DaoUtil.doTrans(() -> {
                bondInfoDao.deleteBondInfo(params).getEffect();
                bondInfoDao.deleteBondXQ(params.getModel()).getEffect();
                bondInfoDao.deleteBondFX(params.getModel()).getEffect();
                bondInfoDao.deleteBondHB(params.getModel()).getEffect();
            });
            return RequestSupport.updateReturnJson(true,  "删除成功！", null).toString();
        }catch (Exception e){
            log.error("删除失败！");
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,  "删除失败！", null).toString();
        }
    }



    @API(desc = "根据已有文档类型获取模板子类型数据字典",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<SqlRow> getUPDTypeByDocType(SqlParam<BondInfoModel> param) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("bndFrsCtg",param.getModel().getCbndFrsCtg());
        params.put("bndScdCtg",param.getModel().getCbndScdCtg());
        List<SqlRow> tempTypeByDocType = bondInfoDao.getUPDTypeByDocType(params);
        SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
        sqlRowSqlResult.setResults(tempTypeByDocType.size());
        sqlRowSqlResult.setRows(tempTypeByDocType);
        sqlRowSqlResult.setDesensitized(false);;
        return sqlRowSqlResult;
    }

    @API(desc = "新增下拉根据代码回显产品名称", auth = APIAuth.NO ,operation = APIOperation.SELECT)
    public SqlResult<BondInfoModel> findBondInfoName(SqlParam<BondInfoModel> params) throws Exception {
        return bondInfoDao.findBondInfoName(params);
    }
    /**
     * 查询是否有同一个债券（同一个市场同一个代码）
     */
    private boolean existSameBond(SqlParam<BondInfoModel> params, int i) throws Exception {
        SqlRow sqlRow = bondInfoDao.existSameBond(params);
        if (i == ONE) {
            // 如果是更新 查询数量-1
            long con = (long) sqlRow.get("con");
            return con - ONE > ZERO;
        } else {
            long con = (long) sqlRow.get("con");
            return con > ZERO;
        }
    }

    @API(desc = "中债一级查询中债二级",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<SqlRow> getUPDTypeByDoc(SqlParam<BondInfoModel> param) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("cbndScdCtg",param.getModel().getCbndScdCtg());
        params.put("cbndFrsCtg",param.getModel().getCbndFrsCtg());
        List<SqlRow> tempTypeByDocType = bondInfoDao.getUPDTypeByDoc(params);
        SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
        sqlRowSqlResult.setResults(tempTypeByDocType.size());
        sqlRowSqlResult.setRows(tempTypeByDocType);
        sqlRowSqlResult.setDesensitized(false);;
        return sqlRowSqlResult;
    }

    @API(desc = "人行一级查询人行二级",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<SqlRow> getPbnkScdByPbnkFrs(SqlParam<BondInfoModel> param) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("pbnkFrsCtg",param.getModel().getPbnkFrsCtg());
        List<SqlRow> tempTypeByDocType = bondInfoDao.getPbnkScdByPbnkFrs(params);
        SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
        sqlRowSqlResult.setResults(tempTypeByDocType.size());
        sqlRowSqlResult.setRows(tempTypeByDocType);
        sqlRowSqlResult.setDesensitized(false);;
        return sqlRowSqlResult;
    }

    @API(desc = "人行二级查询人行三级",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<SqlRow> getPbnkTrdByPbnkScd(SqlParam<BondInfoModel> param) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("pbnkScdCtg",param.getModel().getPbnkScdCtg());
        List<SqlRow> tempTypeByDocType = bondInfoDao.getPbnkTrdByPbnkScd(params);
        SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
        sqlRowSqlResult.setResults(tempTypeByDocType.size());
        sqlRowSqlResult.setRows(tempTypeByDocType);
        sqlRowSqlResult.setDesensitized(false);;
        return sqlRowSqlResult;
    }

    @API(desc = "发行机构所属行业（一级分类）查询发行机构所属行业（二级分类）",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<SqlRow> findIsuOrgBlgIdtDict(SqlParam<BondInfoModel> param) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("ecoFrsTyp",param.getModel().getGgCbcType());
        List<SqlRow> tempTypeByDocType = bondInfoDao.findIsuOrgBlgIdtDict(params);
        SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
        sqlRowSqlResult.setResults(tempTypeByDocType.size());
        sqlRowSqlResult.setRows(tempTypeByDocType);
        sqlRowSqlResult.setDesensitized(false);;
        return sqlRowSqlResult;
    }

    @API(desc = "查询系统字典",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<SqlRow> findDict(SqlParam<BondInfoModel> param) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("dict",param.getModel().getDict());
        params.put("itemkey",param.getModel().getItemkey());
        params.put("itemval",param.getModel().getItemval());
        List<SqlRow> dictList = bondInfoDao.findDict(params);
        SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
        sqlRowSqlResult.setResults(dictList.size());
        sqlRowSqlResult.setRows(dictList);
        sqlRowSqlResult.setDesensitized(false);
        return sqlRowSqlResult;
    }

    @API(desc = "查询系统字典(模糊查询)",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<SqlRow> findDictLike(SqlParam<BondInfoModel> param) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("dict",param.getModel().getDict());
        params.put("itemkey",param.getModel().getItemkey());
        params.put("itemval",param.getModel().getItemval());
        List<SqlRow> dictList = bondInfoDao.findDictLike(params);
        SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
        sqlRowSqlResult.setResults(dictList.size());
        sqlRowSqlResult.setRows(dictList);
        sqlRowSqlResult.setDesensitized(false);
        return sqlRowSqlResult;
    }


    @API(desc = "回显产品名称", auth = APIAuth.NO ,operation = APIOperation.SELECT)
    public SqlResult<SqlRow> findProd(SqlParam<BondInfoModel> params) throws Exception {
        List<SqlRow> dictList = bondInfoDao.findProd();
        SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
        sqlRowSqlResult.setResults(dictList.size());
        sqlRowSqlResult.setRows(dictList);
        sqlRowSqlResult.setDesensitized(false);
        return sqlRowSqlResult;
    }

    @API(desc = "导出债券信息", auth = APIAuth.YES,operation = APIOperation.UPDATE)
    public void bondExcelDownloadAction(SqlParam<BondInfoModel> params) throws Exception {}
}
