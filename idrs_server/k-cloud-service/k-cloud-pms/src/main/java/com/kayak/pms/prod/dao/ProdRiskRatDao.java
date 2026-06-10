package com.kayak.pms.prod.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.Tools;
import com.kayak.pms.prod.model.ProdRiskRat;
import com.kayak.utils.ObjectToMapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ProdRiskRatDao extends ComnDao {

    /**
     * 查询理财产品评分项目
     * */
    public SqlResult<ProdRiskRat> queryRatItem(SqlParam<ProdRiskRat> params) throws Exception {
        /*查询该产品是否已经进行评分*/
        SqlResult<ProdRiskRat> rowsQuery = super.findRows("select distinct t.t8_risk_template_version_id,r.t8_prod_info_id from t8_risk_project t\n" +
                "                  left join t8_prod_risk_rat r on r.t8_risk_project_id = t.id\n" +
                "where t.t8_risk_template_version_id=$S{t8RiskTemplateVersionId} and r.t8_prod_info_id=$S{t8ProdInfoId}", params);

        String sql = "";
        if (rowsQuery.getRows().size() == 0){
             sql = "select t.id t8_risk_project_id," +
                    "       t.t8_risk_template_version_id," +
                    "       t.risk_project," +
                    "       t.coefficient," +
                    "       '' judge," +
                    "       '' integral " +
                    "from t8_risk_project t " +
                    "where t.t8_risk_template_version_id = $S{t8RiskTemplateVersionId} order by CONVERT(t.id,SIGNED) ";

        }else{
             sql = "select t.id t8_risk_project_id," +
                "       t.t8_risk_template_version_id," +
                "       t.risk_project," +
                "       t.coefficient," +
                "       t.weight," +
                "       r.judge," +
                "       r.integral, " +
                "       r.coefficient_prod " +
                "from t8_risk_project t " +
                "         left join t8_prod_risk_rat r on r.t8_risk_project_id = t.id " +
                "where  t.t8_risk_template_version_id = $S{t8RiskTemplateVersionId} and r.t8_prod_info_id=$S{t8ProdInfoId} order by CONVERT(t.id,SIGNED) ";

        }
        SqlResult<ProdRiskRat> rows = super.findRows(sql, params);
        List<ProdRiskRat> listRows = rows.getRows();
        for (ProdRiskRat riskRat : listRows) {
            String val = riskRat.getCoefficient();
            boolean creatable = NumberUtils.isCreatable(val);
            /*数字为0字符串为1*/
            riskRat.setIsDisabled(creatable? "0" : "1");
            /*如果风险系数是数字*/
            if (creatable) {
                /*将是否显示输入框设置为1*/
                riskRat.setIsShowInput("1");
            } else {
                String[] split = val.split("-");
                if (split.length > 0){
                    /*将是否显示输入框设置为1*/
                    riskRat.setIsShowInput("1");
                } else {
                    /*将是否显示输入框设置为0*/
                    riskRat.setIsShowInput("0");
                }
            }
        }
        return rows;

    }

    public SqlResult<ProdRiskRat> QueryRatItemByCode(SqlParam<ProdRiskRat> param) throws Exception {
        String sql = "select * from t8_prod_risk_rat where t8_prod_info_id=$S{t8ProdInfoId}";
        return super.findRows(sql, param);
    }

    public SqlResult<ProdRiskRat> addQueryRatItem(SqlParam<ProdRiskRat> param) throws Exception {
        String sql = "select t.id t8_risk_project_id," +
                "       t.t8_risk_template_version_id," +
                "       t.risk_project," +
                "       t.coefficient," +
                "       t.weight," +
                "       '' judge," +
                "       '' integral " +
                "from t8_risk_project t " +
                "where t.t8_risk_template_version_id = $S{t8RiskTemplateVersionId} order by CONVERT(t.id,SIGNED) ";
        SqlResult<ProdRiskRat> rows = super.findRows(sql, param);
        List<ProdRiskRat> listRows = rows.getRows();
        for (ProdRiskRat riskRat : listRows) {
            String val = riskRat.getCoefficient();
            riskRat.setCoefficientProd(val);
            boolean creatable = NumberUtils.isCreatable(val);
            /*数字为0字符串为1*/
            riskRat.setIsDisabled(creatable? "0" : "1");
            /*如果风险系数是数字*/
            if (creatable) {
                /*将是否显示输入框设置为1*/
                riskRat.setIsShowInput("1");
            } else {
                String[] split = val.split("-");
                if (split.length > 0){
                    /*将是否显示输入框设置为1*/
                    riskRat.setIsShowInput("1");
                    /*将输入框的值设置为空字符串*/
                    riskRat.setCoefficientProd("");
                } else {
                    /*将是否显示输入框设置为0*/
                    riskRat.setIsShowInput("0");
                }
            }
        }
        return rows;
    }

   

    /**
     * 保存理财产品评分项目
     *
     * @return*/
    public void saveRatItem(JSONArray jsonObj, String prodInfoId) throws Exception {
         //根据理财产品ID删除评分
        super.update("delete from t8_prod_risk_rat  where t8_prod_info_id=" + prodInfoId,null);

        //保存产品风险评分
        //super.update("update t8_prod_info set risk_score=" + score + ",prod_risk_level=" + riskRating + " where id=" + prodInfoId,null);
        //保存评分
        for (int i = 0; i < jsonObj.length(); i++) {
            //JSONObject转成Map对象
            Map<String, Object> params=Tools.json2map((JSONObject) jsonObj.get(i));
            params.put("t8ProdInfoId", prodInfoId);
            /*map转实体类*/
            ProdRiskRat prodRiskRat = ObjectToMapUtils.mapToEntity(params, ProdRiskRat.class);
            /*String judge ="".equals( String.valueOf(params.get("judge"))) ? "null":String.valueOf(params.get("judge"));
            String integral = "".equals( String.valueOf(params.get("integral"))) ? "null":String.valueOf(params.get("integral"));
            String coefficientProd = "".equals( String.valueOf(params.get("coefficientProd"))) ? "null":String.valueOf(params.get("coefficientProd"));
            super.update("INSERT INTO t8_prod_risk_rat (id, t8_risk_project_id, t8_prod_info_id, judge, integral,coefficient_prod) VALUES " +
                    "($AUTOIDS{t8_prod_risk_rat}, "+params.get("t8RiskProjectId")+", "+prodInfoId+", "+ judge+", "+integral +"," + coefficientProd +")");*/
            super.update("INSERT INTO t8_prod_risk_rat (id, t8_risk_project_id, t8_prod_info_id, judge, integral,coefficient_prod) VALUES ($AUTOIDS{t8_prod_risk_rat}," +
                    "$S{t8RiskProjectId},$S{t8ProdInfoId},$S{judge},$S{integral},$S{coefficientProd})", prodRiskRat);
        }
    }

    /**
     * 查询评分表中每个项目的值
     *
     * @return*/
    public SqlRow queryRatItemVal(String risk_project, String t8_risk_template_version_id, String prodId) throws Exception {
        return super.findRow("select r.coefficient_prod coefficientProd,r.judge,r.integral from t8_prod_risk_rat r left join t8_risk_project p on p.id=r.t8_risk_project_id where p.risk_project='"+risk_project+"' and p.t8_risk_template_version_id="+t8_risk_template_version_id + " and r.t8_prod_info_id = " + prodId,null);
    }
}
