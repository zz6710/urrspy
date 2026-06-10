package com.kayak.dps.app.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.dps.app.model.DwdLinkedTransMapping;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

@Repository
public class LinkedTransMappingDao extends ComnDao {

    public SqlResult<DwdLinkedTransMapping> findLinkedTransMappings(SqlParam<DwdLinkedTransMapping> params) throws Exception{
        String sql = "select a.id, a.counter_party_name, a.counter_type, a.reg_code, a.remark, a.care_name, a.data_from, a.user_id, a.update_time  from dwd_linked_trans_mapping_info a where 1=1 ";

        if (Strings.isNotBlank(params.getModel().getCounterPartyName())) {
            sql += " and a.counter_party_name like  '%" + params.getModel().getCounterPartyName() + "%' ";
        }

        if (Strings.isNotBlank(params.getModel().getCareName())) {
            sql += " and a.care_name like  '%" + params.getModel().getCareName() + "%' ";
        }

        if (Strings.isNotBlank(params.getModel().getCounterType())) {
            sql += " and a.counter_type  = '"+params.getModel().getCounterType() +"' ";
        }

        if (Strings.isNotBlank(params.getModel().getDataFrom())) {
            sql += " and a.data_from  = '"+params.getModel().getDataFrom() +"' ";
        }

        if (Strings.isNotBlank(params.getModel().getRegCode())) {
            sql += " and a.reg_code  = '"+params.getModel().getRegCode() +"' ";
        }

        return super.findRows(sql,
                DataSourceProperty.PUB, params);
    }

    public UpdateResult addLinkedTransMapping(SqlParam<DwdLinkedTransMapping> params) throws Exception {
        Object userName = SysUtil.getUserInfo().get(SysUtil.USERNAME);
        params.getModel().setUserId(userName.toString());
        params.getModel().setUpdateTime(DateUtil.getTimestamp19());
        return super.update("INSERT INTO dwd_linked_trans_mapping_info \n" +
                        "(counter_party_name, counter_type, reg_code, remark, care_name, data_from, user_id, update_time) \n" +
                        "VALUES($S{counterPartyName}, $S{counterType}, $S{regCode}, $S{remark}, $S{careName}, $S{dataFrom},$S{userId}, $S{updateTime})",
                DataSourceProperty.PUB,params.getModel());
    }

    public UpdateResult updateLinkedTransMapping(SqlParam<DwdLinkedTransMapping> params) throws Exception {
        Object userName = SysUtil.getUserInfo().get(SysUtil.USERNAME);
        params.getModel().setUserId(userName.toString());
        params.getModel().setUpdateTime(DateUtil.getTimestamp19());
        return super.update("update dwd_linked_trans_mapping_info " +
                        " set counter_party_name = $S{counterPartyName}, counter_type = $S{counterType}, reg_code = $S{regCode}, remark = $S{remark}, care_name =$S{careName}, data_from = $S{dataFrom}, user_id = $S{userId}, update_time = $S{updateTime} where id = $S{id}",
                DataSourceProperty.PUB,params.getModel());
    }

    public UpdateResult delLinkedTransMapping(SqlParam<DwdLinkedTransMapping> params) throws Exception {
        return super.update("delete from  dwd_linked_trans_mapping_info where id = $S{id}",
        DataSourceProperty.PUB,params.getModel());
    }
}
