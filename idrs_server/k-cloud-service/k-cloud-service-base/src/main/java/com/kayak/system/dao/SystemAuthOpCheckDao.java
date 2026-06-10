package com.kayak.system.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.system.model.SystemAuthOpCheck;
import com.kayak.system.model.SystemAuthSet;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SystemAuthOpCheckDao extends ComnDao {
    public SqlResult<SystemAuthOpCheck> find(SqlParam<SystemAuthOpCheck> params) throws Exception {
        return super.findRows("SELECT id,server,field,fieldtype,fieldname,logic,value,opjoin,descript FROM sys_auth_op_check", params);
    }
    public SqlRow findModelField(SqlParam<SystemAuthOpCheck> params) throws Exception {
        return super.findRow("select m2.model_field from sys_server_method m left join sys_server_model m2 on m.model_name=m2.model_name where m.server =$S{server}",params.getParams());
    }

    public int add(SqlParam<SystemAuthOpCheck> params) throws Exception {
        return super.update("INSERT INTO sys_auth_op_check(id,server,field,fieldtype,fieldname,logic,value,opjoin,descript) VALUES($AUTOIDS{id},$S{server},$S{field},$S{fieldtype},$S{fieldname},$S{logic},$S{value},$S{opjoin},$S{descript})",params.getModel()).getEffect();
    }

    public int deleteById(SqlParam<SystemAuthOpCheck> params) throws Exception {
        return super.update("delete from sys_auth_op_check where id=$S{id}",params.getModel()).getEffect();
    }
}
