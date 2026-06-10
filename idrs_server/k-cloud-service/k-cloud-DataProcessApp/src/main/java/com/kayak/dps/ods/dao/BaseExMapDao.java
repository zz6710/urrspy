package com.kayak.dps.ods.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.clear.utils.Tools;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.app.model.BaseExMapModel;
import com.kayak.dps.app.model.BondInfoModel;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class BaseExMapDao extends ComnDao {

    @Resource(name = "comnDao")
    private ComnDao comnDao;

    public SqlResult<BaseExMapModel> findBaseExMapModels(SqlParam<BaseExMapModel> params) throws Exception {
        BaseExMapModel baseExMapModel = params.getModel();
        String sql = " select bem.id, bem.MODULEID, sd.dictname, sdi.dict,  sdi.itemkey, sdi.itemval, bem.out_value, bem.remark from base_ex_map bem inner join sys_dict_item sdi on bem.DICT = sdi.dict and bem.sys_value = sdi.itemkey left join sys_dict sd on sdi.dict = sd.dict  where 1 = 1  ";
        if(!Tools.strIsEmpty(baseExMapModel.getModuleid())){
            sql += "  and bem.MODULEID = $S{moduleid} " ;
        }
        if(!Tools.strIsEmpty(baseExMapModel.getDict())){
            sql += " and sdi.dict like '%$U{dict}%' " ;
        }
        if(!Tools.strIsEmpty(baseExMapModel.getDictname())){
            sql += "  and sd.DICTNAME like '%$U{dictname}%' " ;
        }
        sql += " order by DICT,SYS_VALUE asc ";
        return super.findRows(sql, params);
    }

    public UpdateResult addBaseExMapModel(SqlParam<BaseExMapModel> params) throws Exception {
        return super.update("INSERT INTO base_ex_map(moduleid,dict,dictname,sys_value , out_value,remark ) " +
                        "VALUES($S{moduleid},$S{dict},(select dictname from sys_dict  where dict = $S{dict} limit 1),$S{itemkey},$S{outValue},$S{remark} )",
                params.getModel());
    }


    public UpdateResult updateBaseExMapModel(SqlParam<BaseExMapModel> params) throws Exception {
        return super.update("UPDATE base_ex_map SET  moduleid=$S{moduleid}, " +
                        " dict=$S{dict} ,dictname=$S{dictname} ,sys_value=$S{itemkey}, " +
                        " out_value=$S{outValue} , remark=$S{remark} " +
                        " WHERE  id=$S{id} ",
                params.getModel());
    }

    public UpdateResult deleteBaseExMapModel(SqlParam<BaseExMapModel> params) throws Exception {
        return super.update("DELETE FROM base_ex_map WHERE  id=$S{id} ",
                params.getModel());
    }

    public SqlResult<BaseExMapModel> findDictByNm(SqlParam<BaseExMapModel> params) throws Exception {
        String sql = "select dict,dictname  from sys_dict sdi where sdi.dictname like '%$U{dictname}%' or sdi.dict like '%$U{dictname}%' ";
        return super.findRows(sql, DataSourceProperty.PUB, params);

    }

    public SqlResult<BaseExMapModel> findSysDictItemInfo(SqlParam<BaseExMapModel> params) throws Exception {
        String sql = " select itemkey,itemval  from sys_dict_item sdi where sdi.dict = $S{dict} order by itemkey ";
        return super.findRows(sql, DataSourceProperty.PUB, params);

    }

    public SqlResult<BaseExMapModel> findSysDictName(SqlParam<BaseExMapModel> params) throws Exception {
        String sql = " select sdi.itemval ,sd.dictname  from sys_dict_item sdi left join sys_dict sd on sdi.dict = sd.dict where sdi.dict =$S{dict} and sdi.itemkey =$S{itemkey}  limit 1 ";
        return super.findRows(sql, DataSourceProperty.PUB, params);

    }
}
