package com.kayak.dps.ods.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.dps.app.model.FundInfoModel;
import com.kayak.dps.check.util.NextVersionUtil;
import com.kayak.dps.ods.dao.FundInfoDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "基金信息服务", model = FundInfoModel.class)
public class FundInfoService {
    @Resource(name = "fundInfoDao")
    private FundInfoDao fundInfoDao;

    @Resource(name = "assetCollectionService")
    private AssetCollectionService assetCollectionService;

    private static final int ONE = 1;
    private static final int ZERO = 0;

    /**
     * 新增基金信息
     * @param params
     * @throws Exception
     */
    @API(desc = "新增基金信息",operation = APIOperation.INSTER, auth = APIAuth.YES)
    public String addFoudInfo(SqlParam<FundInfoModel> params) throws Exception {

        try {
            //校验唯一性
            Map<String, Object> param = new HashMap<>();
            param.put("checkTableName","ods_fnd_bas_inf");
            param.put("scrId",params.getModel().getScrId());
            if(assetCollectionService.isOnlyOne(param)>0){
                return RequestSupport.updateReturnJson(false,  "该公墓基金已存在！", null).toString();
            }
            params.getModel().setCrtDate(SysUtil.getSystemParamsByParaid("10004"));
            params.getModel().setCrtTime(DateUtil.getNowTime());
            params.getModel().setCrtUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
            params.getModel().setDealDate(DateUtil.getNowDate());
            fundInfoDao.insertFundInfo(params).getEffect();
            return RequestSupport.updateReturnJson(true, "新增成功！", null).toString();
        }catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,  "新增失败！", null).toString();
        }
    }

    @API(desc = "查询基金信息",operation = APIOperation.SELECT, auth = APIAuth.YES)
    public SqlResult<FundInfoModel> findFundInfoModels(SqlParam<FundInfoModel> params) throws Exception {
        return fundInfoDao.findFundInfo(params);
    }

    /**
     * 查询基金信息代码与名称 去重
     * @param params
     * @throws Exception
     */
    @API(desc = "查询基金信息代码与名称",operation = APIOperation.SELECT, auth = APIAuth.NO)
    public SqlResult<FundInfoModel> findFondInfoModelsCdAndNm(SqlParam<FundInfoModel> params) throws Exception {
        params.setMakeSql(false);
        return fundInfoDao.findFundInfoCdAndNm(params);
    }

    @API(desc = "基金信息批量导入", auth = APIAuth.NO, operation = APIOperation.UPDATE)
    public void importFundInfoAdditional(SqlParam<FundInfoModel> importFundInfoAdditional){
        //此方法只作用于权限控制
    }

    @API(desc = "修改基金信息",operation = APIOperation.UPDATE, auth = APIAuth.YES)
    public String updateFundInfo(SqlParam<FundInfoModel> params) throws Exception {
        try {
            params.getModel().setUpdDate(SysUtil.getSystemParamsByParaid("10004"));
            params.getModel().setUpdTime(DateUtil.getNowTime());
            params.getModel().setUpdUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
            params.getModel().setDealDate(DateUtil.getNowDate());
            fundInfoDao.updateFundInfo(params).getEffect();
            return RequestSupport.updateReturnJson(true,  "修改成功！", null).toString();
        }catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,  "修改失败！", null).toString();
        }
    }

    @API(desc = "补录基金信息",operation = APIOperation.UPDATE, auth = APIAuth.YES)
    public String updateFundInfoBl(SqlParam<FundInfoModel> params) throws Exception {
        try {
            params.getModel().setUpdDate(SysUtil.getSystemParamsByParaid("10004"));
            params.getModel().setUpdTime(DateUtil.getNowTime());
            params.getModel().setUpdUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
            params.getModel().setVersion(NextVersionUtil.getNextVersion(params.getModel().getVersion()));
            params.getModel().setDealDate(DateUtil.getNowDate());
            fundInfoDao.updateFundInfoBl(params).getEffect();
            return RequestSupport.updateReturnJson(true,  "补录成功！", null).toString();
        }catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,  "补录失败！", null).toString();
        }
    }


    @API(desc = "删除基金信息",operation = APIOperation.DELETE, auth = APIAuth.YES)
    public String deleteFundInfo(SqlParam<FundInfoModel> params) throws Exception {
        try {
            fundInfoDao.deleteFundInfo(params).getEffect();
            return RequestSupport.updateReturnJson(true,  "删除成功！", null).toString();
        }catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,  "删除失败！", null).toString();
        }
    }

    @API(desc = "根据文档类型获取模板类型数据字典",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<SqlRow> getXPTypeByDocType(SqlParam<FundInfoModel> param) throws Exception {

        String docType = param.getModel().getCbndFrsCtg();
        List<SqlRow> tempTypeByDocType = fundInfoDao.getXPTypeByDocType(docType);
        SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
        sqlRowSqlResult.setResults(tempTypeByDocType.size());
        sqlRowSqlResult.setRows(tempTypeByDocType);
        sqlRowSqlResult.setDesensitized(false);;
        return sqlRowSqlResult;
    }



    /**
     * 查询是否有同一个基金（同一个市场同一个代码）
     */
    private boolean existSameFund(SqlParam<FundInfoModel> params,int i) throws Exception {
        SqlRow sqlRow = fundInfoDao.existSameFund(params);
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
    public SqlResult<SqlRow> getUPDTypeByDoc(SqlParam<FundInfoModel> param) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("cbndScdCtg",param.getModel().getCbndScdCtg());
        params.put("cbndFrsCtg",param.getModel().getCbndFrsCtg());
        List<SqlRow> tempTypeByDocType = fundInfoDao.getUPDTypeByDoc(params);
        SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
        sqlRowSqlResult.setResults(tempTypeByDocType.size());
        sqlRowSqlResult.setRows(tempTypeByDocType);
        sqlRowSqlResult.setDesensitized(false);
        return sqlRowSqlResult;
    }

    @API(desc = "人行一级查询人行二级",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<SqlRow> getPbnkScdByPbnkFrs(SqlParam<FundInfoModel> param) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("pbnkFrsCtg",param.getModel().getPbnkFrsCtg());
        List<SqlRow> tempTypeByDocType = fundInfoDao.getPbnkScdByPbnkFrs(params);
        SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
        sqlRowSqlResult.setResults(tempTypeByDocType.size());
        sqlRowSqlResult.setRows(tempTypeByDocType);
        sqlRowSqlResult.setDesensitized(false);
        return sqlRowSqlResult;
    }

    @API(desc = "人行二级查询人行三级",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<SqlRow> getPbnkTrdByPbnkScd(SqlParam<FundInfoModel> param) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("pbnkScdCtg",param.getModel().getPbnkScdCtg());
        List<SqlRow> tempTypeByDocType = fundInfoDao.getPbnkTrdByPbnkScd(params);
        SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
        sqlRowSqlResult.setResults(tempTypeByDocType.size());
        sqlRowSqlResult.setRows(tempTypeByDocType);
        sqlRowSqlResult.setDesensitized(false);
        return sqlRowSqlResult;
    }


    @API(desc = "导出基金信息", auth = APIAuth.YES,operation = APIOperation.UPDATE)
    public void excelDownloadFund(SqlParam<FundInfoModel> params) throws Exception {}
}
