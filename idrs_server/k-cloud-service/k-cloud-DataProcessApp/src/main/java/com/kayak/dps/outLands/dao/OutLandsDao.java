package com.kayak.dps.outLands.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.Tools;
import com.kayak.dps.app.model.T8AffiliatedPerson;
import com.kayak.dps.outLands.model.OutLandsCash;
import com.kayak.dps.outLands.model.OutLandsRaise;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class OutLandsDao extends ComnDao {

    public SqlResult<OutLandsCash> findOutLandsCash(SqlParam<OutLandsCash> params) throws Exception {
        String sql=" SELECT PROD_CD, PROD_NM, CLC_AMT,CALL_AMT, DATA_DATE " +
                " FROM mid_ast_outlands_cash " +
                " where 1=1 " ;
        if (StringUtils.isNotBlank(params.getModel().getProdNm())) {
            sql += " and PROD_NM like '%$U{prodNm}%' ";
        }
        if(StringUtils.isNotBlank(params.getModel().getProdCd())){
            sql = sql + " and PROD_CD =  $S{prodCd} ";
        }
        if(StringUtils.isNotBlank(params.getModel().getDataDateStart())&&StringUtils.isNotBlank(params.getModel().getDataDateEnd())){
            sql = sql + " and DATA_DATE  between '" + params.getModel().getDataDateStart() + "'  and '" + params.getModel().getDataDateEnd() + "' ";
        }
        sql += "  order by  DATA_DATE desc  ";
        return super.findRows(sql,DataSourceProperty.PUB, params);
    }
    public SqlResult<OutLandsRaise> findOutLandsRaise(SqlParam<OutLandsRaise> params) throws Exception {
        String sql="SELECT PROD_CD, PROD_NM,CLC_BAL, DATA_DATE " +
                " FROM mid_ast_outlands_raise " +
                " where 1=1 " ;
       if (StringUtils.isNotBlank(params.getModel().getProdNm())) {
            sql += " and PROD_NM like '%$U{prodNm}%' ";
        }
        if(StringUtils.isNotBlank(params.getModel().getProdCd())){
            sql = sql + " and PROD_CD =  $S{prodCd} ";
        }
        if(StringUtils.isNotBlank(params.getModel().getDataDateStart())&&StringUtils.isNotBlank(params.getModel().getDataDateEnd())){
            sql = sql + " and DATA_DATE  between '" + params.getModel().getDataDateStart() + "'  and '" + params.getModel().getDataDateEnd() + "' ";
        }
        sql += "  order by  DATA_DATE desc  ";
        return super.findRows(sql,DataSourceProperty.PUB, params);
    }



    /**
     * 境外募集余额数据入库
     * @throws Exception
     */
    //@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addOutLandsRaise(String dataDate, List<Map<String, Object>> outLandsRaiseList) throws Exception {
        super.update("DELETE FROM  mid_ast_outlands_raise where DATA_DATE=$S{dataDate}",
                Tools.makeParams().put("dataDate", dataDate).build());
        for (Map<String, Object> datum : outLandsRaiseList) {
            if(StringUtils.equals(datum.get("id").toString(),"汇总")){
                break;
            }
            datum.put("dataDate",dataDate);
            super.update("insert into mid_ast_outlands_raise (PROD_CD, PROD_NM, CLC_BAL, DATA_DATE,CRT_DATE,CRT_TIME) values ($S{prodCd}, $S{prodNm}, $S{clcBal},$S{dataDate},DATE_FORMAT(NOW(), '%Y%m%d'),DATE_FORMAT(NOW(), '%H%i%s'))", datum);
        }

    }



    /**
     * 境外募集及兑付发生额数据入库
     * @throws Exception
     */
   // @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addOutLandsCash(String dataDate, List<Map<String, Object>> outLandsCashList) throws Exception {
        super.update("DELETE FROM  mid_ast_outlands_cash where DATA_DATE=$S{dataDate}",
                Tools.makeParams().put("dataDate", dataDate).build());
        for (Map<String, Object> datum : outLandsCashList) {
            if(StringUtils.equals(datum.get("id").toString(),"汇总")){
                break;
            }
            datum.put("dataDate",dataDate);
            super.update("insert into mid_ast_outlands_cash (PROD_CD, PROD_NM, CLC_AMT,CALL_AMT, DATA_DATE,CRT_DATE,CRT_TIME) values ($S{prodCd}, $S{prodNm}, $S{clcAmt},$S{callAmt}, $S{dataDate},DATE_FORMAT(NOW(), '%Y%m%d'),DATE_FORMAT(NOW(), '%H%i%s'))", datum);
        }


    }
}
