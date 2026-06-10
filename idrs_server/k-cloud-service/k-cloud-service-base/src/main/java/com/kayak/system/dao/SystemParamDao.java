package com.kayak.system.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.constants.SystemParamConstants;
import com.kayak.core.util.Tools;
import com.kayak.system.model.SystemParam;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Repository
public class SystemParamDao extends ComnDao {

    public SqlResult<SystemParam> find(SqlParam<SystemParam> params) throws Exception {
        return super.findRows("SELECT * FROM sys_param ", params);
    }

    public SqlResult<SystemParam> findImpinfo(SqlParam<SystemParam> params) throws Exception {
        SqlRow row =super.findRow("SELECT * FROM sys_param WHERE paraid ='80000083003' ORDER BY paraid DESC", params);
        String conditions = row.getString("paravalue");
        String conditions2 = conditions.replace(",","','");
        String sql = "SELECT biz_date paravalue FROM (SELECT * FROM a_p_prd_sellerholdingstatic_i where TA_CODE IN ('"+conditions2+"') GROUP BY TA_CODE,BIZ_DATE) TEMP GROUP BY TEMP.BIZ_DATE HAVING COUNT(BIZ_DATE)>=(SELECT LENGTH('"+conditions+"') - LENGTH(REPLACE('"+conditions+"', ',', '')) + 1 AS total FROM DUAL) ORDER BY temp.biz_date DESC LIMIT 1";
        return super.findRows(sql, params);
    }

    public SqlResult<SystemParam> findSystemTime(SqlParam<SystemParam> params) throws Exception {
        return super.findRows("SELECT * FROM sys_param WHERE paraid IN ( "+"10006" +","+"10004"+ ") ORDER BY paraid DESC", params);
    }

    public void update(List<SystemParam> params) throws Exception {
        if (CollectionUtils.isEmpty(params)) {
            return;
        }
        doTrans(() -> {
            for (SystemParam p : params) {
                super.update("UPDATE sys_param SET paravalue = $S{paravalue} WHERE paraid = $S{paraid}", p);
            }
        });
    }

    /**
     * 获取系统参数数据
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<SystemParam> findSysParams(SqlParam<SystemParam> params) throws Exception {
        StringBuilder sqlBuilder = new StringBuilder("SELECT " +
                " p.moduleid, " +
                " p.paraid, " +
                " p.paravalue, " +
                " p.paraname, " +
                " p.groupparaid, " +
                " p.dict, " +
                " p.functype, " +
                " p.confoption, " +
                " p.isdisplay, " +
                " i.itemval as dict_value " +
                " FROM SYS_PARAM p " +
                " LEFT JOIN sys_dict_item i ON i.dict = p.dict AND i.itemkey = p.paravalue " +
                " WHERE p.moduleid = '0' and p.isdisplay = '" + SystemParamConstants.SHOW + "' ");

        SystemParam model = params.getModel();
        if (Tools.isNotBlank(model.getParavalue())) {
            sqlBuilder.append(" AND ( p.dict IS NULL AND p.paravalue LIKE '%$U{paravalue}%' or i.itemval LIKE '%$U{paravalue}%') ");
        }

        if (Tools.isNotBlank(model.getParaname())) {
            sqlBuilder.append(" AND p.paraname LIKE '%$U{paraname}%' ");
        }

        sqlBuilder.append(" order by PARAID ");

        return super.findRows(sqlBuilder.toString(), params);
    }

    /**
     * 获取业务参数数据
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<SystemParam> findBusinessParams(SqlParam<SystemParam> params) throws Exception {
        StringBuffer strBuffer = new StringBuffer("SELECT " +
                " p.moduleid, " +
                " p.paraid, " +
                " p.paravalue, " +
                " p.paraname, " +
                " p.groupparaid, " +
                " p.dict, " +
                " p.functype, " +
                " p.confoption, " +
                " p.isdisplay, " +
                " i.itemval as dict_value " +
                " FROM SYS_PARAM p " +
                " LEFT JOIN sys_dict_item i ON i.dict = p.dict AND i.itemkey = p.paravalue " +
                " WHERE p.moduleid = '8' and p.isdisplay = '" + SystemParamConstants.SHOW + "' ");

        SystemParam model = params.getModel();
        if (Tools.isNotBlank(model.getParavalue())) {
            strBuffer.append(" AND ( p.dict IS NULL AND p.paravalue LIKE '%$U{paravalue}%' or i.itemval LIKE '%$U{paravalue}%') ");
        }

        if (Tools.isNotBlank(model.getParaname())) {
            strBuffer.append(" AND p.paraname LIKE '%$U{paraname}%' ");
        }

        strBuffer.append(" order by PARAID ");

        return super.findRows(strBuffer.toString(), params);
    }

    public SqlResult<SystemParam> findPortParams(SqlParam<SystemParam> params) throws Exception {
        StringBuffer sqlBuilder = new StringBuffer("SELECT " +
                " p.moduleid, " +
                " p.paraid, " +
                " p.paravalue, " +
                " p.paraname, " +
                " p.groupparaid, " +
                " p.dict, " +
                " p.functype, " +
                " p.confoption, " +
                " p.isdisplay, " +
                " i.itemval as dict_value " +
                " FROM SYS_PARAM p " +
                " LEFT JOIN sys_dict_item i ON i.dict = p.dict AND i.itemkey = p.paravalue " +
                " WHERE p.moduleid not in ('0', '8', 'R') and p.isdisplay = '" + SystemParamConstants.SHOW + "' ");

        SystemParam model = params.getModel();
        if (Tools.isNotBlank(model.getParavalue())) {
            sqlBuilder.append(" AND ( p.dict IS NULL AND p.paravalue LIKE '%$U{paravalue}%' or i.itemval LIKE '%$U{paravalue}%') ");
        }

        if (Tools.isNotBlank(model.getParaname())) {
            sqlBuilder.append(" AND p.paraname LIKE '%$U{paraname}%' ");
        }

        sqlBuilder.append(" order by PARAID ");

        return super.findRows(sqlBuilder.toString(), params);
    }


    public SqlResult<SystemParam> findRegisterParams(SqlParam<SystemParam> params) throws Exception {
        StringBuffer sqlBuilder = new StringBuffer("SELECT " +
                " p.moduleid, " +
                " p.paraid, " +
                " p.paravalue, " +
                " p.paraname, " +
                " p.groupparaid, " +
                " p.dict, " +
                " p.functype, " +
                " p.confoption, " +
                " p.isdisplay, " +
                " i.itemval as dict_value " +
                " FROM SYS_PARAM p " +
                " LEFT JOIN sys_dict_item i ON i.dict = p.dict AND i.itemkey = p.paravalue " +
                " WHERE p.moduleid = 'R' and p.isdisplay = '" + SystemParamConstants.SHOW + "' ");

        SystemParam model = params.getModel();
        if (Tools.isNotBlank(model.getParavalue())) {
            sqlBuilder.append(" AND ( p.dict IS NULL AND p.paravalue LIKE '%$U{paravalue}%' or i.itemval LIKE '%$U{paravalue}%') ");
        }

        if (Tools.isNotBlank(model.getParaname())) {
            sqlBuilder.append(" AND p.paraname LIKE '%$U{paraname}%' ");
        }

        sqlBuilder.append(" order by PARAID ");

        return super.findRows(sqlBuilder.toString(), params);
    }

    public SqlResult<SystemParam> findAllParamsByParaid(SqlParam<SystemParam> params) throws Exception {
        StringBuffer sqlBuilder = new StringBuffer("SELECT " +
                " p.moduleid, " +
                " p.paraid, " +
                " p.paravalue, " +
                " p.paraname, " +
                " p.groupparaid, " +
                " p.dict, " +
                " p.functype, " +
                " p.confoption, " +
                " p.isdisplay, " +
                " i.itemval as dict_value " +
                " FROM SYS_PARAM p " +
                " LEFT JOIN sys_dict_item i ON i.dict = p.dict AND i.itemkey = p.paravalue " +
                " WHERE 1=1 ");

        SystemParam model = params.getModel();

        if (Tools.isNotBlank(model.getParaid())) {
            sqlBuilder.append(" AND p.paraid = '$U{paraid}' ");
        }

        return super.findRows(sqlBuilder.toString(), params);
    }


    //添加
    public UpdateResult addSysParam (SqlParam<SystemParam> params) throws Exception {
        return super.update("INSERT INTO SYS_PARAM(moduleid,paraid,paravalue,paraname,groupparaid,dict,functype,confoption,isdisplay) VALUES($S{moduleid},$S{paraid},$S{paravalue},$S{paraname},$S{groupparaid},$S{dict},$S{functype},$S{confoption},$S{isdisplay})",params.getModel());
    }
    //修改
    public UpdateResult updateSysParam(SqlParam<SystemParam> params) throws Exception {
        return super.update("UPDATE SYS_PARAM SET paravalue=$S{paravalue} ,paraname=$S{paraname}   WHERE  moduleid=$S{moduleid} and paraid=$S{paraid}",
                params.getModel());
    }
    //删除
    public UpdateResult deleteSysParam(SqlParam<SystemParam> params) throws Exception {
        return super.update("DELETE FROM SYS_PARAM WHERE moduleid=$S{moduleid} and paraid=$S{paraid} ",
                params.getModel());
    }
}
