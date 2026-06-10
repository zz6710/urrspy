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
import com.kayak.dps.app.model.FundPrivateInfoModel;
import com.kayak.dps.app.model.T8OrgSheet;
import com.kayak.dps.check.util.NextVersionUtil;
import com.kayak.dps.ods.dao.FundPrivateInfoDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "私募基金信息", model = FundPrivateInfoModel.class)
public class FundPrivateInfoService {
    @Resource(name = "fundPrivateInfoDao")
    private FundPrivateInfoDao fundPrivateInfoDao;

    private static final int ONE = 1;
    private static final int ZERO = 0;

    /**
     * 新增私募基金信息
     * @param params
     * @throws Exception
     */
    @API(desc = "新增私募基金信息",operation = APIOperation.INSTER, auth = APIAuth.YES)
    public String addFundPrivateInfo(SqlParam<FundPrivateInfoModel> params) throws Exception {

        try {
            params.getModel().setCrtDate(DateUtil.getNowDate());
            params.getModel().setCrtTime(DateUtil.getNowTime());
            params.getModel().setCrtUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
            params.getModel().setDealDate(DateUtil.getNowDate());
            params.getModel().setDealDate(DateUtil.getNowDate());
            params.getModel().setScrId(params.getModel().getScrCd()+".3.12");
            if(existSameFund(params,0)){
                return RequestSupport.updateReturnJson(true,  "基金代码："+params.getModel().getScrCd()+" 已经存在,请勿重复新增", null).toString();
            }
            DaoUtil.doTrans(() -> {
                fundPrivateInfoDao.insertFundPrivateInfo(params).getEffect();
            });
            return RequestSupport.updateReturnJson(true, "新增成功！", null).toString();
        }catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,  "新增失败！", null).toString();
        }
    }

    @API(desc = "查询私募基金信息",operation = APIOperation.SELECT, auth = APIAuth.NO)
    public SqlResult<FundPrivateInfoModel> findFundPrivateInfoModels(SqlParam<FundPrivateInfoModel> params) throws Exception {
        return fundPrivateInfoDao.findFundPrivateInfo(params);
    }

    /**
     * 查询私募基金信息代码与名称 去重
     * @param params
     * @throws Exception
     */
    @API(desc = "查询私募基金信息代码与名称",operation = APIOperation.SELECT, auth = APIAuth.NO)
    public SqlResult<FundPrivateInfoModel> findFundPrivateInfoModelsCdAndNm(SqlParam<FundPrivateInfoModel> params) throws Exception {
        params.setMakeSql(false);
        return fundPrivateInfoDao.findFundPrivateInfoCdAndNm(params);
    }

    /**
     * 查询机构名称 去重
     * @param params
     * @throws Exception
     */
    @API(desc = "查询机构名称",operation = APIOperation.SELECT, auth = APIAuth.NO)
    public SqlResult<FundPrivateInfoModel> findOrgNmAll(SqlParam<FundPrivateInfoModel> params) throws Exception {
        params.setMakeSql(false);
        return fundPrivateInfoDao.findOrgNmAll(params);
    }

    @API(desc = "私募基金信息批量导入", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public void importFundPrivateInfoAdditional(SqlParam<FundPrivateInfoModel> importFundPrivateInfoAdditional){
        //此方法只作用于权限控制
    }

    @API(desc = "修改私募基金信息",operation = APIOperation.UPDATE, auth = APIAuth.YES)
    public String updateFundPrivateInfo(SqlParam<FundPrivateInfoModel> params) throws Exception {
        try {
            params.getModel().setUpdDate(DateUtil.getNowDate());
            params.getModel().setUpdTime(DateUtil.getNowTime());
            params.getModel().setUpdUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
            params.getModel().setDealDate(DateUtil.getNowDate());
            fundPrivateInfoDao.updateFundPrivateInfo(params).getEffect();
            return RequestSupport.updateReturnJson(true,  "修改成功！", null).toString();
        }catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,  "修改失败！", null).toString();
        }
    }

    @API(desc = "补录私募基金信息",operation = APIOperation.UPDATE, auth = APIAuth.YES)
    public String updatePrivateInfoBl(SqlParam<FundPrivateInfoModel> params) throws Exception {
        try {
            params.getModel().setUpdDate(DateUtil.getNowDate());
            params.getModel().setUpdTime(DateUtil.getNowTime());
            params.getModel().setUpdUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
            params.getModel().setVersion(NextVersionUtil.getNextVersion(params.getModel().getVersion()));
            params.getModel().setDealDate(DateUtil.getNowDate());
            fundPrivateInfoDao.updatePrivateInfoBl(params).getEffect();
            return RequestSupport.updateReturnJson(true,  "修改成功！", null).toString();
        }catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,  "修改失败！", null).toString();
        }
    }


    @API(desc = "删除私募基金信息",operation = APIOperation.DELETE, auth = APIAuth.YES)
    public String deleteFundPrivateInfo(SqlParam<FundPrivateInfoModel> params) throws Exception {
        try {
            DaoUtil.doTrans(() -> {
                fundPrivateInfoDao.deleteFundPrivateInfo(params).getEffect();
            });
            return RequestSupport.updateReturnJson(true,  "删除成功！", null).toString();
        }catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,  "删除失败！", null).toString();
        }
    }

    /**
     * 查询是否有同一个基金（同一个市场同一个代码）
     */
    private boolean existSameFund(SqlParam<FundPrivateInfoModel> params,int i) throws Exception {
        SqlRow sqlRow = fundPrivateInfoDao.existSameFund(params);
        long con = (long) sqlRow.get("con");
        if (i == ONE) {
            // 如果是更新 查询数量-1
            return con - ONE > ZERO;
        } else {
            return con > ZERO;
        }

    }

    @API(desc = "中债一级查询中债二级",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<SqlRow> getUPDTypeByDoc(SqlParam<FundPrivateInfoModel> param) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("cbndScdCtg",param.getModel().getCbndScdCtg());
        params.put("cbndFrsCtg",param.getModel().getCbndFrsCtg());
        List<SqlRow> tempTypeByDocType = fundPrivateInfoDao.getUPDTypeByDoc(params);
        SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
        sqlRowSqlResult.setResults(tempTypeByDocType.size());
        sqlRowSqlResult.setRows(tempTypeByDocType);
        sqlRowSqlResult.setDesensitized(false);
        return sqlRowSqlResult;
    }

    @API(desc = "人行二级查询人行三级",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<SqlRow> getPbnkScdByPbnkFrs(SqlParam<FundPrivateInfoModel> param) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("pbnkFrsCtg",param.getModel().getPbnkFrsCtg());
        List<SqlRow> tempTypeByDocType = fundPrivateInfoDao.getPbnkScdByPbnkFrs(params);
        SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
        sqlRowSqlResult.setResults(tempTypeByDocType.size());
        sqlRowSqlResult.setRows(tempTypeByDocType);
        sqlRowSqlResult.setDesensitized(false);
        return sqlRowSqlResult;
    }

    @API(desc = "人行三级查询人行四级",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<SqlRow> getPbnkTrdByPbnkScd(SqlParam<FundPrivateInfoModel> param) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("pbnkScdCtg",param.getModel().getPbnkScdCtg());
        List<SqlRow> tempTypeByDocType = fundPrivateInfoDao.getPbnkTrdByPbnkScd(params);
        SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
        sqlRowSqlResult.setResults(tempTypeByDocType.size());
        sqlRowSqlResult.setRows(tempTypeByDocType);
        sqlRowSqlResult.setDesensitized(false);
        return sqlRowSqlResult;
    }
}
