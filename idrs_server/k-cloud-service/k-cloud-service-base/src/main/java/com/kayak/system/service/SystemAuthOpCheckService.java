package com.kayak.system.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.cache.util.CacheUtil;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.system.dao.SystemAuthOpCheckDao;
import com.kayak.system.model.SystemAuthOpCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

;

@Service
@Slf4j
@APIDefine(desc = "操作授权审批服务", model = SystemAuthOpCheck.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SystemAuthOpCheckService {

    private final SystemAuthOpCheckDao systemAuthOpCheckDao;

    @API(desc = "新增操作授权审批条件",auth = APIAuth.YES)
    public String add(SqlParam<SystemAuthOpCheck> params) throws Exception {
        try {
            systemAuthOpCheckDao.add(params);
        }catch (Exception e){
            return RequestSupport.updateReturnJson(false, "新增失败", null).toString();
        }
        CacheUtil.freshenGateway();
        return RequestSupport.updateReturnJson(true, "新增成功", null).toString();
    }

    @API(desc = "删除操作授权审批条件",auth = APIAuth.YES)
    public String delete(SqlParam<SystemAuthOpCheck> params) throws Exception {
        try {
            systemAuthOpCheckDao.deleteById(params);
        }catch (Exception e){
            return RequestSupport.updateReturnJson(false, "删除失败", null).toString();
        }
        CacheUtil.freshenGateway();
        return RequestSupport.updateReturnJson(true, "删除成功", null).toString();
    }


    @API(desc = "查询操作授权审批条件")
    public SqlResult<SystemAuthOpCheck> find(SqlParam<SystemAuthOpCheck> params) throws Exception {
        params.setMakeSql(true);
        SqlResult<SystemAuthOpCheck> result = systemAuthOpCheckDao.find(params);
        return result;
    }

    @API(desc = "新增操作授权审批条件查询实体属性",auth = APIAuth.NO)
    public SqlResult<SqlRow> findModelField(SqlParam<SystemAuthOpCheck> params) throws Exception {
        SqlRow sqlRow= systemAuthOpCheckDao.findModelField(params);
        String model_field=sqlRow.getString("model_field");
        String modelFields[]=model_field.split(",");
        List<SqlRow> sqlRows=new LinkedList<>();
        for(String item:modelFields){
            SqlRow sqlRow1=new SqlRow();
            String arrs[]=item.split(":");
            for(int i=0;i<arrs.length;i++){
                if(i==0){
                    sqlRow1.put("field",arrs[i]);
                }
                if(i==1){
                    String type=arrs[i];
                    sqlRow1.put("fieldtype",type.substring(type.lastIndexOf(".")+1));
                }
                if(i==2){
                    sqlRow1.put("fieldname",arrs[i]);
                }
            }
            sqlRows.add(sqlRow1);
        }
        return SqlResult.build(sqlRows,sqlRow.size());
    }

}
