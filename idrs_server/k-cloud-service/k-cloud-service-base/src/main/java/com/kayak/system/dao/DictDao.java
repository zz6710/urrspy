package com.kayak.system.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.system.model.Dict;
import com.kayak.system.model.DictItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DictDao extends ComnDao {

    public SqlResult<Dict> findDict(SqlParam<Dict> params) throws Exception {
        return super.findRows("SELECT * FROM sys_dict", params);
    }

    public int editDict(SqlParam<Dict> params) throws Exception {
        return super.update(
                "UPDATE sys_dict SET dictname = $S{dictname} WHERE dict = $S{dict}", params.getModel()).getEffect();
    }

    public int addDict(SqlParam<Dict> params) throws Exception {
        return super.update(
                "INSERT INTO sys_dict (dict, dictname) " +
                        " VALUES ($S{dict},$S{dictname})", params.getModel()).getEffect();
    }

    public void deleteDict(SqlParam<Dict> params) throws Exception {
        doTrans(() -> {
            super.update(
                    "DELETE FROM sys_dict_item WHERE dict = $S{dict}", params.getModel()).getEffect();
            super.update(
                    "DELETE FROM sys_dict WHERE dict = $S{dict}", params.getModel()).getEffect();
        });

    }
    public SqlResult<Dict> findDictOnly(SqlParam<Dict> params) throws Exception {
        return super.findRows("SELECT * FROM sys_dict where dict = $S{dict} ", params);
    }

    public SqlResult<DictItem> findDictItem(SqlParam<DictItem> params) throws Exception {
        return super.findRows("SELECT * FROM sys_dict_item WHERE dict = $S{dict}  order by ifnull(itemorder,itemkey)+0", params);
    }
    public SqlResult<DictItem> findDictItemOnly(SqlParam<DictItem> params) throws Exception {
        return super.findRows("SELECT * FROM sys_dict_item WHERE dict = $S{dict} and itemkey= $S{itemkey}", params);
    }

    public int addDictItem(SqlParam<DictItem> params) throws Exception {
        return super.update(
                "INSERT INTO sys_dict_item (dict, itemkey, itemval, itemrender, itemorder) " +
                        "VALUES ($S{dict},$S{itemkey},$S{itemval},$S{itemrender},$S{itemorder})",
                params.getModel()).getEffect();
    }

    public int editDictItem(SqlParam<DictItem> params) throws Exception {
        return super.update(
                "UPDATE sys_dict_item SET itemval = $S{itemval}, itemrender = $S{itemrender}, itemorder = $S{itemorder} " +
                        "WHERE dict = $S{dict} AND itemkey = $S{itemkey}", params.getModel()).getEffect();
    }

    public int deleteDictItem(SqlParam<DictItem> params) throws Exception {
        return super.update(
                "DELETE FROM sys_dict_item WHERE dict = $S{dict} AND itemkey = $S{itemkey}",
                params.getModel()).getEffect();
    }

    //查询所有的省份
    public List<SqlRow> fidAllArea(SqlParam<DictItem> params) throws  Exception{
        return super.findRows("select itemval,itemKey from sys_dict_item where dict = 'prod_sale_area' and itemKey not in ('710000','810000','820000','900000')");
    }

    /**
     * 查询所有的数据字典
     * @return
     * @throws Exception
     */
    public List<SqlRow> findAllDict() throws Exception {
        return super.findRows("SELECT dict,itemval,itemKey FROM sys_dict_item");
    }
}
