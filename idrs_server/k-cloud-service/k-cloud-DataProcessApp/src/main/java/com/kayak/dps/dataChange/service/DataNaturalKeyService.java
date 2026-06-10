package com.kayak.dps.dataChange.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.action.BaseController;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.dps.dataChange.dao.DataNarutalKeyDao;
import com.kayak.dps.dataChange.model.DataNaturalKeyModel;
import com.kayak.dps.dataChange.model.SourceDataConfigModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@APIDefine(desc = "各层级业务主键服务", model = DataNaturalKeyModel.class)
public class DataNaturalKeyService extends BaseController {

    @Autowired
    private DataNarutalKeyDao dataNarutalKeyDao;

    @API(desc = "添加各层级业务主键", auth = APIAuth.YES)
    public String addDataNarutalKeyModel(SqlParam<DataNaturalKeyModel> params)  {
        try {
            List<DataNaturalKeyModel> naturalKeyList = params.getModel().getNaturalKeyGridData();
            dataNarutalKeyDao.deleteDataNarutalKeyModel(params);
            for (DataNaturalKeyModel model : naturalKeyList){
                model.setTableName(params.getModel().getTableName());
                model.setHierarchy(params.getModel().getHierarchy());
                dataNarutalKeyDao.addDataNarutalKeyModel(model);
            }
            return RequestSupport.updateReturnJson(true,  "新增成功！", null).toString();
        } catch (Exception e){
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,  e.getMessage(), null).toString();
        }
    }

    @API(desc = "查询各层级业务主键", auth = APIAuth.NO)
    public SqlResult<DataNaturalKeyModel> findDataNarutalKeyModel(SqlParam<DataNaturalKeyModel> params) throws Exception {
        params.setMakeSql(false);
        SqlResult<DataNaturalKeyModel> flagModel =dataNarutalKeyDao.findDataNarutalKeyModel(params);
        for (int i = 0; i < flagModel.getRows().size(); i++) {
            List<DataNaturalKeyModel> modelList = new ArrayList<>();
            List<SqlRow> flagModel2 = dataNarutalKeyDao.findNaturalKeys(flagModel.getRows().get(i).getTableName());
            for (SqlRow row : flagModel2){
                DataNaturalKeyModel modelRow = new DataNaturalKeyModel();
                modelRow.setNaturalKey(row.getString("natural_key"));
                modelRow.setStandardKey(row.getString("standard_key"));
                modelRow.setOutDict(row.getString("out_dict"));
                modelList.add(modelRow);
            }
            flagModel.getRows().get(i).setNaturalKeyGridData(modelList);
        }
        return flagModel;
    }

    @API(desc = "删除各层级业务主键", auth = APIAuth.YES)
    public String deleteDataNarutalKeyModel(SqlParam<DataNaturalKeyModel> params){
        try {
            dataNarutalKeyDao.deleteDataNarutalKeyModel(params);
            return RequestSupport.updateReturnJson(true,  "删除成功！", null).toString();
        } catch (Exception e){
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,  e.getMessage(), null).toString();
        }
    }

    @API(desc = "查询数据表", auth = APIAuth.NO)
    public SqlResult<DataNaturalKeyModel> findTables(SqlParam<DataNaturalKeyModel> params) throws Exception {
        return dataNarutalKeyDao.findTables(params);
    }

    @API(desc = "查询业务主键", auth = APIAuth.NO)
    public SqlResult<DataNaturalKeyModel> findNaturalKey(SqlParam<DataNaturalKeyModel> params) throws Exception {
        return dataNarutalKeyDao.findNaturalKey(params);
    }
}
