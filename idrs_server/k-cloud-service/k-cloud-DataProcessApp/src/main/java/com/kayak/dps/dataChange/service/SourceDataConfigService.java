package com.kayak.dps.dataChange.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.action.BaseController;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.dps.dataChange.dao.SourceDataConfigDao;
import com.kayak.dps.dataChange.model.SourceDataConfigModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "源数据配置", model = SourceDataConfigModel.class)
public class SourceDataConfigService  extends BaseController {

    @Autowired
    private SourceDataConfigDao sourceDataConfigDao;

    @API(desc = "添加源数据配置", auth = APIAuth.YES)
    public String addSourceDataConfigModel(SqlParam<SourceDataConfigModel> params)  {
        try {
            //查询是否有配置
            SqlResult<SourceDataConfigModel> addflag = sourceDataConfigDao.findSourceDataConfigModel(params);
            if(addflag.getRows().size()>0&&"false".equals(params.getModel().getIsEdit())){
                return RequestSupport.updateReturnJson(false,  "该数据库表已存在配置信息！", null).toString();
            }
            sourceDataConfigDao.deleteSourceDataConfigModel(params);
            List<SourceDataConfigModel> fields = params.getModel().getFieldCodeGridData();
            for (SourceDataConfigModel model : fields){
                model.setTableName(params.getModel().getTableName());
                model.setRemindType(params.getModel().getRemindType());
                model.setComparisonRules(params.getModel().getComparisonRules());
                model.setRelatedReport(params.getModel().getRelatedReport());
                model.setTaskGroup(params.getModel().getTaskGroup());
                sourceDataConfigDao.addSourceDataConfigModel(model);
            }
            return RequestSupport.updateReturnJson(true,  "新增成功！", null).toString();
        } catch (Exception e){
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,  e.getMessage(), null).toString();
        }
    }

    @API(desc = "修改源数据配置", auth = APIAuth.YES)
    public String editSourceDataConfigModel(SqlParam<SourceDataConfigModel> params)  {
        try {
            sourceDataConfigDao.editSourceDataConfigModel(params);
            return RequestSupport.updateReturnJson(true,  "修改成功！", null).toString();
        } catch (Exception e){
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,  e.getMessage(), null).toString();
        }
    }

    @API(desc = "删除源数据配置", auth = APIAuth.YES)
    public String deleteSourceDataConfigModel(SqlParam<SourceDataConfigModel> params)  {
        try {
            sourceDataConfigDao.deleteSourceDataConfigModel(params);
            return RequestSupport.updateReturnJson(true,  "删除成功！", null).toString();
        } catch (Exception e){
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,  e.getMessage(), null).toString();
        }
    }

    @API(desc = "查询源数据配置", auth = APIAuth.NO)
    public SqlResult<SourceDataConfigModel> findSourceDataConfigModel(SqlParam<SourceDataConfigModel> params) throws Exception {
        params.setMakeSql(false);
        SqlResult<SourceDataConfigModel> flagModel = sourceDataConfigDao.findSourceDataConfigModel(params);
        for (int i = 0; i < flagModel.getRows().size(); i++) {
            List<SourceDataConfigModel> modelList = new ArrayList<>();
            params.getModel().setTableName(flagModel.getRows().get(i).getTableName());
            String whererelatedReport = "";
            String[] relatedReports = flagModel.getRows().get(i).getRelatedReport().split(",");
            params.setLimit(500);
            List<SqlRow> flagModel2 = sourceDataConfigDao.findReminName(params.getModel().getTableName());
            for (SqlRow row : flagModel2){
                SourceDataConfigModel modelRow = new SourceDataConfigModel();
                modelRow.setRemindField(row.getString("remind_field"));
                modelRow.setFieldType(row.getString("field_type"));
                modelRow.setOutDict(row.getString("out_dict"));
                modelList.add(modelRow);
            }
            flagModel.getRows().get(i).setFieldCodeGridData(modelList);
            for (String relatedReport : relatedReports){
                whererelatedReport += "'"+relatedReport+"',";
            }
            whererelatedReport = whererelatedReport.substring(0,whererelatedReport.length()-1);
            SqlRow flagModel3 = sourceDataConfigDao.findReportName(whererelatedReport);
            flagModel.getRows().get(i).setReportName(flagModel3.getString("report_name"));
        }
        return flagModel;
    }

    @API(desc = "查询数据库表", auth = APIAuth.NO)
    public SqlResult<SourceDataConfigModel> findTables(SqlParam<SourceDataConfigModel> params) throws Exception {
        return sourceDataConfigDao.findTables(params);
    }

    @API(desc = "查询表字段", auth = APIAuth.NO)
    public SqlResult<SourceDataConfigModel> findRemindField(SqlParam<SourceDataConfigModel> params) throws Exception {
        //查找主键信息
        String tablename = params.getModel().getTableName();
        SqlRow natureKeys = sourceDataConfigDao.findNatrueKey(tablename);
        String whereNK = "";
        if(natureKeys!=null){
            String[] natureKey = natureKeys.getString("natural_key")==null?null:natureKeys.getString("natural_key").split(",");
            if(natureKey.length>0){
                for (int i = 0; i < natureKey.length; i++) {
                    whereNK+="'"+natureKey[i].toUpperCase()+"',";
                }
            }
        }
        return sourceDataConfigDao.findRemindField(params,whereNK);
    }

    @API(desc = "查询所有字段", auth = APIAuth.NO)
    public SqlResult<SourceDataConfigModel> findRemindFieldAll(SqlParam<SourceDataConfigModel> params) throws Exception {
        //查找主键信息
        String tablename = params.getModel().getTableName();
        SqlRow natureKeys = sourceDataConfigDao.findNatrueKey(tablename);
        String whereNK = "";
        if(natureKeys!=null){
            String[] natureKey = natureKeys.getString("natural_key")==null?null:natureKeys.getString("natural_key").split(",");
            if(natureKey.length>0){
                for (int i = 0; i < natureKey.length; i++) {
                    whereNK+="'"+natureKey[i].toUpperCase()+"',";
                }
            }
        }
        return sourceDataConfigDao.findRemindFieldAll(params,whereNK);
    }

    @API(desc = "查询app数据库表", auth = APIAuth.NO)
    public SqlResult<SourceDataConfigModel> findTablesForApp(SqlParam<SourceDataConfigModel> params) throws Exception {
        return sourceDataConfigDao.findTablesForApp(params);
    }

    @API(desc = "查询字段信息", auth = APIAuth.NO)
    public SqlResult<SourceDataConfigModel> findRemindFieldMsg(SqlParam<SourceDataConfigModel> params) throws Exception {
        return sourceDataConfigDao.findRemindFieldMsg(params);
    }

    @API(desc = "查询任务组", auth = APIAuth.NO)
    public SqlResult<SourceDataConfigModel> findTaskGroup(SqlParam<SourceDataConfigModel> params) throws Exception {
        return sourceDataConfigDao.findTaskGroup(params);
    }
}
