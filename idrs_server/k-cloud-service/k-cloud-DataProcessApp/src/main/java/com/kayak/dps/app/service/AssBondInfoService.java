package com.kayak.dps.app.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.dps.app.dao.AssBondInfoModelDao;
import com.kayak.dps.app.model.AssBondInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "债券补录服务", model = AssBondInfoModel.class)
public class AssBondInfoService {

    @Autowired
    private AssBondInfoModelDao assBondInfoModelDao;

    @API(desc = "债券补录信息", auth = APIAuth.NO)
    public String addAssBondInfoModel(SqlParam<AssBondInfoModel> params) throws Exception {
        try {
            //assBondInfoModelDao.deleteAssBondInfoModel(params);
            assBondInfoModelDao.addAssBondInfoModel(params);
            return RequestSupport.updateReturnJson(true,  "补录成功！", null).toString();
        } catch (Exception e){
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,  e.getMessage(), null).toString();
        }
    }

    @API(desc = "发行省查询发行市",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<SqlRow> findIssuCityDict(SqlParam<AssBondInfoModel> param) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("issuProvince",param.getModel().getIssuProvince());
        List<SqlRow> tempTypeByDocType = assBondInfoModelDao.findIssuCityDict(params);
        SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
        sqlRowSqlResult.setResults(tempTypeByDocType.size());
        sqlRowSqlResult.setRows(tempTypeByDocType);
        sqlRowSqlResult.setDesensitized(false);;
        return sqlRowSqlResult;
    }

}
