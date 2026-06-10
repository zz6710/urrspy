package com.kayak.dps.dataChange.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.action.BaseController;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.dps.dataChange.dao.SourceDataChgInfoDao;
import com.kayak.dps.dataChange.model.SourceDataChgInfoModel;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "源数据变化", model = SourceDataChgInfoModel.class)
public class SourceDataChgInfoService extends BaseController {

    @Autowired
    private SourceDataChgInfoDao sourceDataChgInfoDao;

    @API(desc = "查询单个源数据变化", auth = APIAuth.NO)
    public SqlResult<SourceDataChgInfoModel> findSourceDataChgInfoModelForOne(SqlParam<SourceDataChgInfoModel> params) throws Exception {
        params.setMakeSql(false);
        SqlResult<SourceDataChgInfoModel> flagModel = sourceDataChgInfoDao.findSourceDataChgInfoModelForOne(params);
        flagModel = dealModel(flagModel,"0");
        return flagModel;
    }
    @API(desc = "查询单个源数据变化(未确认统计)", auth = APIAuth.NO)
    public SqlResult<SourceDataChgInfoModel> findSourceDataChgInfoModelForUnconfirmed(SqlParam<SourceDataChgInfoModel> params) throws Exception {
        params.setMakeSql(false);
        SqlResult<SourceDataChgInfoModel> flagModel = sourceDataChgInfoDao.findSourceDataChgInfoModelForUnconfirmed(params);
        flagModel = dealModel(flagModel,"0");
        return flagModel;
    }
    @API(desc = "查询资产单个源数据变化", auth = APIAuth.NO)
    public SqlResult<SourceDataChgInfoModel> findAssetSourceDataChgInfoModelForOne(SqlParam<SourceDataChgInfoModel> params) throws Exception {
        params.setMakeSql(false);
        SqlResult<SourceDataChgInfoModel> flagModel = sourceDataChgInfoDao.findAssetSourceDataChgInfoModelForOne(params);
        flagModel = dealModel(flagModel,"0");
        return flagModel;
    }

    @API(desc = "查询资产单个源数据变化(未确认统计)", auth = APIAuth.NO)
    public SqlResult<SourceDataChgInfoModel> findAssetSourceDataChgInfoModelForUnconfirmed(SqlParam<SourceDataChgInfoModel> params) throws Exception {
        params.setMakeSql(false);
        SqlResult<SourceDataChgInfoModel> flagModel = sourceDataChgInfoDao.findAssetSourceDataChgInfoModelForUnconfirmed(params);
        flagModel = dealModel(flagModel,"0");
        return flagModel;
    }


    @API(desc = "确认源数据变化", auth = APIAuth.YES,operation = APIOperation.UPDATE)
    public String confirmSourceDataChgInfoModel(SqlParam<SourceDataChgInfoModel> params) {
        try {
            List<SourceDataChgInfoModel> sourceData = params.getModel().getSourceData();
            //判断是否拼接对比
            String  table = sourceData.get(0).getTables();
            Map<String, Object> qryMap = new HashMap<>();
            qryMap.put("port_table",table);
            List<SqlRow> list = sourceDataChgInfoDao.findComType(qryMap);
            if( null!=list && list.size()>0 && !StringUtil.isBlank(list.get(0).getString("comparison_rules")) && list.get(0).getString("comparison_rules").equals("02")){
                //拼接对比逻辑
                for (int i = 0; i < sourceData.size(); i++) {
                    //更新确认表
                    confirmSourceDataChgInfoModel2(sourceData.get(i));
                    if(i==sourceData.size()-1){

                        if("0".equals(sourceData.get(i).getEditstatus())){
                            //不修改该字段，新数据置为失效 维持旧数据不变
                            changeEffectiveContrastUnUpdate(sourceData.get(i));
                        }else {
                            //修改字段 新数据置生效 旧数据置为失效
                            changeEffectiveContrast(sourceData.get(i));
                        }

                    }
                }
            }else {
                // 字段对比逻辑
                for (int i = 0; i < sourceData.size(); i++) {
                    if("0".equals(sourceData.get(i).getEditstatus())){
                        //不修改该字段，新数据要修改为旧数据
                        submitN(sourceData.get(i));

                    }
                    //更新确认表
                    confirmSourceDataChgInfoModel2(sourceData.get(i));
                    if(i==sourceData.size()-1){
                        changeEffective(sourceData.get(i));
                    }
                }

            }



            return RequestSupport.updateReturnJson(true, "确认成功！", null).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,  e.getMessage(), null).toString();
        }
    }

    @API(desc = "修改源数据变化状态", auth = APIAuth.NO)
    public void confirmSourceDataChgInfoModel2(SourceDataChgInfoModel params) throws Exception {
        sourceDataChgInfoDao.confirmSourceDataChgInfoModel(params);
    }

    @API(desc = "查询所有源数据变化", auth = APIAuth.NO)
    public SqlResult<SourceDataChgInfoModel> findSourceDataChgInfoModelAll(SqlParam<SourceDataChgInfoModel> params) throws Exception {
        params.setMakeSql(false);


        SqlResult<SourceDataChgInfoModel> flagModel = sourceDataChgInfoDao.findSourceDataChgInfoModelAll(params);


        //新增 拼接对比判断逻辑
        Map<String, Object> qryMap =  new HashMap<>();
        qryMap.put("port_table",params.getModel().getTables());
        List<SqlRow> list = sourceDataChgInfoDao.findComType(qryMap);
        if( null!=list && list.size()>0 && !StringUtil.isBlank(list.get(0).getString("comparison_rules")) && list.get(0).getString("comparison_rules").equals("02")){

            flagModel = dealModelContrast(flagModel,"1");
        }else {
            flagModel = dealModel(flagModel,"1");
        }

        return flagModel;
    }

    @API(desc = "查询所有源数据变化", auth = APIAuth.NO)
    public SqlResult<SourceDataChgInfoModel> findSourceDataChgInfoModelAll2(SqlParam<SourceDataChgInfoModel> params) throws Exception {
        params.setMakeSql(false);
        SqlResult<SourceDataChgInfoModel> flagModel = sourceDataChgInfoDao.findSourceDataChgInfoModelAll2(params);

            //新增 拼接对比判断逻辑
            Map<String, Object> qryMap =  new HashMap<>();
            qryMap.put("port_table",params.getModel().getTables());
            List<SqlRow> list = sourceDataChgInfoDao.findComType(qryMap);
            if( null!=list && list.size()>0 && !StringUtil.isBlank(list.get(0).getString("comparison_rules")) && list.get(0).getString("comparison_rules").equals("02")){
                flagModel = dealModelContrast(flagModel,"1");
            }else {
                flagModel = dealModel(flagModel,"1");
            }


        return flagModel;
    }

    @API(desc = "修改新源数据变化", auth = APIAuth.NO ,operation = APIOperation.UPDATE)
    public void submitN(SourceDataChgInfoModel params) throws Exception {
        //新数据需要修改
        sourceDataChgInfoDao.updateField(params);
    }

    @API(desc = "修改数据", auth = APIAuth.NO ,operation = APIOperation.UPDATE)
    public void changeEffective(SourceDataChgInfoModel params) throws Exception {
        sourceDataChgInfoDao.newChg(params,"1");
        sourceDataChgInfoDao.oldChg(params,"0");

    }


    @API(desc = "修改数据", auth = APIAuth.NO ,operation = APIOperation.UPDATE)
    public void changeEffectiveContrast(SourceDataChgInfoModel params) throws Exception {

        String[] newIds = params.getNewid().split(",");
        for(String newId :  newIds){
            params.setNewid(newId);
            sourceDataChgInfoDao.newChg(params,"1");
        }

        String[] oldIds = params.getOldid().split(",");
        for(String oldId :  oldIds){
            params.setOldid(oldId);
            sourceDataChgInfoDao.oldChg(params,"0");
        }
    }


    @API(desc = "不修改数据，拼接对比逻辑", auth = APIAuth.NO ,operation = APIOperation.UPDATE)
    public void changeEffectiveContrastUnUpdate(SourceDataChgInfoModel params) throws Exception {
        String[] newIds = params.getNewid().split(",");
        for(String newId :  newIds){
            params.setNewid(newId);
            sourceDataChgInfoDao.newChg(params,"0");
        }
    }



    private SqlResult<SourceDataChgInfoModel> dealModelContrast(SqlResult<SourceDataChgInfoModel> flagModel,String stopflag) throws Exception {
        String addNaturaKeyL = "";
        String reportsnameL = "";
        for (int i = 0; i < flagModel.getRows().size(); i++) {
            String addNaturaKey = "";
            String[] naturaKeys = flagModel.getRows().get(i).getNaturalKeys().split(",");
            String whererelatedReport = "";
            String tables = flagModel.getRows().get(i).getTables();
            String[] relatedReports = null;
            if(flagModel.getRows().get(i).getReportName()!=null&&flagModel.getRows().get(i).getReportName()!=""){
                relatedReports = flagModel.getRows().get(i).getReportName().split(",");
            }

            //非首次循环 后续参数取第一次循环得到的数据
            if("1".equals(stopflag)&&i!=0){
                flagModel.getRows().get(i).setNaturalKeysName(addNaturaKeyL);
                flagModel.getRows().get(i).setReportsName(reportsnameL);
            }else{
                for (String naturaKey : naturaKeys){
                    String key = naturaKey.split(":")[0];
                    String value = naturaKey.split(":")[1];
                    SqlRow flagModel2 = sourceDataChgInfoDao.findKeyName(key,tables);
                    SqlRow flagModel3 = sourceDataChgInfoDao.findValueName(key,value,tables);
                    addNaturaKey += flagModel2.getString("FIELD_NAME")+":"+flagModel3.getString("itemval")+",";
                }
                addNaturaKey = addNaturaKey.substring(0,addNaturaKey.length()-1);
                addNaturaKeyL = addNaturaKey;
                flagModel.getRows().get(i).setNaturalKeysName(addNaturaKey);
                if (relatedReports!=null){
                    for (String relatedReport : relatedReports){
                        whererelatedReport += "'"+relatedReport+"',";
                    }
                    whererelatedReport = whererelatedReport.substring(0,whererelatedReport.length()-1);
                    SqlRow flagModel3 = sourceDataChgInfoDao.findReportName(whererelatedReport);
                    reportsnameL = flagModel3.getString("reports_name");
                    flagModel.getRows().get(i).setReportsName(flagModel3.getString("reports_name"));
                }
            }

            // 旧数据中文名
            String oldDataName= "";
            // 新数据中文名
            String newDataName= "";
            // 字段中文名
            String fieldName= "";

            // 拼接对比字段/字段值 获取中文名

            // 拆分字段  多个字段
            String[] changeFields = flagModel.getRows().get(i).getChangeField().split(",");


            // 拆分值  新数据  多条数据
            String[] newDatas = flagModel.getRows().get(i).getFieldNew().split(";");


            // 拆分值  旧数据 多条数据
            String[] oldDatas = flagModel.getRows().get(i).getFieldOld().split(";");





            //转义旧数据中文名
            for(String oldData : oldDatas){
                 //拆分值  旧数据  单条 多个值
                String[]  oldDatastr=  oldData.split(",");
                for (int j = 0; j <changeFields.length ; j++) {
                    oldDataName =  oldDataName  +","+  sourceDataChgInfoDao.findValueNameContrast(changeFields[j],oldDatastr[j],tables).getString("itemval");
                }
                oldDataName = oldDataName + ";";
            }
            // 返回旧数据中文名
            oldDataName = oldDataName.substring(1,oldDataName.length()-1);
            flagModel.getRows().get(i).setDictOld(oldDataName);

            //转义新数据中文名
            for(String newData : newDatas){
                //拆分值  新数据  单条 多个值
                String[]  newDatastr=  newData.split(",");
                for (int j = 0; j <changeFields.length ; j++) {
                    newDataName  =  newDataName +","+  sourceDataChgInfoDao.findValueNameContrast(changeFields[j],newDatastr[j],tables).getString("itemval");
                }
                newDataName = newDataName + ";";
            }

            // 返回新数据中文名
            newDataName = newDataName.substring(1,newDataName.length()-1);
            flagModel.getRows().get(i).setDictNew(newDataName);

            //转义字段名
            for (int j = 0; j <changeFields.length ; j++) {
                fieldName  =  fieldName +","+  sourceDataChgInfoDao.findKeyName(changeFields[j],tables).getString("FIELD_NAME");
            }
            // 返回字段中文名
            fieldName = fieldName.substring(1,fieldName.length());
            flagModel.getRows().get(i).setChangeFieldName(fieldName);


        }
        return flagModel;
    }




    private SqlResult<SourceDataChgInfoModel> dealModel(SqlResult<SourceDataChgInfoModel> flagModel,String stopflag) throws Exception {
        String addNaturaKeyL = "";
        String reportsnameL = "";
        for (int i = 0; i < flagModel.getRows().size(); i++) {
            String addNaturaKey = "";
            String[] naturaKeys = flagModel.getRows().get(i).getNaturalKeys().split(",");
            String whererelatedReport = "";
            String tables = flagModel.getRows().get(i).getTables();
            String[] relatedReports = null;
            if(flagModel.getRows().get(i).getReportName()!=null&&flagModel.getRows().get(i).getReportName()!=""){
                relatedReports = flagModel.getRows().get(i).getReportName().split(",");
            }
            if("1".equals(stopflag)&&i!=0){
                flagModel.getRows().get(i).setNaturalKeysName(addNaturaKeyL);
                flagModel.getRows().get(i).setReportsName(reportsnameL);
            }else{
                for (String naturaKey : naturaKeys){
                    String key = naturaKey.split(":")[0];
                    String value = naturaKey.split(":")[1];
                    SqlRow flagModel2 = sourceDataChgInfoDao.findKeyName(key,tables);
                    SqlRow flagModel3 = sourceDataChgInfoDao.findValueName(key,value,tables);
                    addNaturaKey += flagModel2.getString("FIELD_NAME")+":"+flagModel3.getString("itemval")+",";
                }
                addNaturaKey = addNaturaKey.substring(0,addNaturaKey.length()-1);
                addNaturaKeyL = addNaturaKey;
                flagModel.getRows().get(i).setNaturalKeysName(addNaturaKey);
                if (relatedReports!=null){
                    for (String relatedReport : relatedReports){
                        whererelatedReport += "'"+relatedReport+"',";
                    }
                    whererelatedReport = whererelatedReport.substring(0,whererelatedReport.length()-1);
                    SqlRow flagModel3 = sourceDataChgInfoDao.findReportName(whererelatedReport);
                    reportsnameL = flagModel3.getString("reports_name");
                    flagModel.getRows().get(i).setReportsName(flagModel3.getString("reports_name"));
                }
            }
        }
        return flagModel;
    }




}
