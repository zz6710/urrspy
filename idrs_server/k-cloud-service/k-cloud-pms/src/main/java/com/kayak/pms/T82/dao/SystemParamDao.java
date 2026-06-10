package com.kayak.pms.T82.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.pms.T82.model.T82009;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Repository
public class SystemParamDao extends ComnDao {

    public SqlResult<T82009> find(SqlParam<T82009> params) throws Exception {
        return super.findRows("SELECT * FROM sys_param order by PARAID", params);
    }


    public void update(List<T82009> params) throws Exception {
        if (CollectionUtils.isEmpty(params)) {
            return;
        }
        doTrans(() -> {
            for (T82009 p : params) {
                super.update("UPDATE sys_param SET paravalue = $S{paravalue} WHERE paraid = $S{paraid}", p);
            }
        });
    }



    public SqlResult<T82009> findSysParams(SqlParam<T82009> params) throws Exception {
        return super.findRows("SELECT a.moduleid," +
                "       a.paraid," +
                "(case a.functype when 'password'then '******' else a.paravalue end ) paravalue,\n" +
                        "\t(case t.itemval when NULL then ( case a.functype when 'password' then '******' else a.paravalue end) else t.itemval end) paravalue_text,"+
                "       a.paraname," +
                "       a.groupparaid," +
                "       a.dict," +
                "       a.functype," +
                "       a.confoption," +
                "       a.isdisplay" +
                "  FROM SYS_PARAM a" +
                "  left join sys_dict_item t" +
                "    on t.dict = a.dict" +
                "   and t.itemkey = a.paravalue" +
                " WHERE a.isdisplay = '1'" +
                "   and a.groupparaid = 'E0001'" +
                " order by a.PARAID ", params);
    }

    /**
     *  通过参数查询系统参数
     * @param params
     * @return
     * @throws Exception
     */
    public SqlRow findByParam(String params) throws Exception {
    return super.findRow(
        "SELECT a.moduleid,"
            + "       a.paraid,"
            + "(case a.functype when 'password'then '******' else a.paravalue end ) paravalue,\n"
            + "\t(case t.itemval when NULL then ( case a.functype when 'password' then '******' else a.paravalue end) else t.itemval end) paravalue_text,"
            + "       a.paraname,"
            + "       a.groupparaid,"
            + "       a.dict,"
            + "       a.functype,"
            + "       a.confoption,"
            + "       a.isdisplay"
            + "  FROM SYS_PARAM a"
            + "  left join sys_dict_item t"
            + "    on t.dict = a.dict"
            + "   and t.itemkey = a.paravalue"
            + " WHERE a.isdisplay = '1' AND paraid = '"
            + params
            + "'"
            + " order by a.PARAID ",
        null);
    }

    public UpdateResult addSysParam(SqlParam<T82009> params) throws Exception {
        return super.update("INSERT INTO SYS_PARAM(moduleid,paraid,paravalue,paraname,groupparaid,dict,functype,confoption,isdisplay,fieldtype) VALUES($S{moduleid},$S{paraid},$S{paravalue},$S{paraname},$S{groupparaid},$S{dict},$S{functype},$S{confoption},$S{isdisplay},$S{fieldtype})",
                params.getModel());
    }

    public UpdateResult updateSysParam(SqlParam<T82009> params) throws Exception {
        return super.update("UPDATE SYS_PARAM SET paravalue=$S{paravalue} ,paraname=$S{paraname}   WHERE  moduleid=$S{moduleid} and paraid=$S{paraid}",
                params.getModel());
    }

    public UpdateResult deleteSysParam(SqlParam<T82009> params) throws Exception {
        return super.update("DELETE FROM SYS_PARAM WHERE moduleid=$S{moduleid} and paraid=$S{paraid} ",
                params.getModel());
    }

    public SqlResult<T82009> findParamValueByParaId(SqlParam<T82009> params) throws Exception {

        return super.findRows("select * from  sys_param where  paraid = $S{paraid}",params);

    }
}
