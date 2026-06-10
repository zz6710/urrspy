package com.kayak.pms.T85.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.T85.model.SysDictItem;
import org.springframework.stereotype.Repository;

/**
 * 文件名: SysDictItemDao.java
 * 描述:   字典数据
 * 创建人: zengzt
 * 创建时间:2020年5月16日下午2:29:03
 */
@Repository
public class SysDictItemDao extends ComnDao {

	public SqlResult<SysDictItem> findSysDictItem(SqlParam<SysDictItem> params) throws Exception {
		
		String sql = " SELECT dict,itemkey,itemval,itemorder,if_using,itemrender FROM sys_dict_item WHERE if_using='1' order by dict,ifnull(itemorder,itemkey)";
		
		return super.findRows(sql, params);
	}
	
	
	public SqlResult<SysDictItem> findSysDictItemWithMultipleKey(SqlParam<SysDictItem> params ,String[] itemKeys) throws Exception {
		
		String dict = params.getModel().getDict();
		
		String sql = " SELECT dict,itemkey,itemval,itemorder,if_using,itemrender FROM sys_dict_item WHERE dict='"+dict+"' AND if_using='1' ";
		
		if(itemKeys.length>0){
			sql = sql + " AND itemkey IN ( ";
			
			for (int i = 0; i < itemKeys.length; i++) {
				if(i==itemKeys.length-1){
					sql = sql + "'" + itemKeys[i] + "'";
				}else{
					sql = sql + "'" + itemKeys[i] + "',";
				}
			}
			sql = sql + ") ";
		}
		sql = sql + " order by dict,ifnull(itemorder,itemkey)";
		
		return super.findRows(sql, params);
	}
	
	public int insertSysDictItem(SqlParam<SysDictItem> params) throws Exception {
		
		String sql = " INSERT INTO sys_dict_item(dict,itemkey,itemval,itemorder,if_using,itemrender)VALUES("
				+ "	$S{dict},LPAD(seq_prod_mode_itemkey.nextVal,2,0),$S{itemval},$S{itemorder},$S{ifUsing},$S{itemrender}) ";
		
		return super.update(sql, params.getModel()).getEffect();
	}
	
	public int updateSysDictItem(SqlParam<SysDictItem> params) throws Exception {
		
		String sql = " UPDATE sys_dict_item SET "
				+ "	itemval=$S{itemval},itemorder=$S{itemorder},if_using=$S{ifUsing},itemrender=$S{itemrender} "
				+ " WHERE dict=$S{dict} AND itemkey=$S{itemkey}";
		
		return super.update(sql, params.getModel()).getEffect();
	}
	
	public int deleteSysDictItem(SqlParam<SysDictItem> params) throws Exception {
		
		String sql = " DELETE FROM  sys_dict_item  "
				+ " 	WHERE dict=$S{dict} AND itemkey=$S{itemkey}";
		
		return super.update(sql, params.getModel()).getEffect();
	}
	
	public int deleteSysDictItem(SysDictItem params) throws Exception {
		
		String sql = " DELETE FROM  sys_dict_item  "
				+ " 	WHERE dict=$S{dict} AND itemkey=$S{itemkey}";
		
		return super.update(sql, params).getEffect();
	}

    public String findDictValueByKey(String itemKey,String dist) throws Exception {
		return super.findRow(String.class,"select itemval from sys_dict_item where sys_dict_item.itemkey='"+itemKey+"' and sys_dict_item.dict='"+dist+"'",0,null);
    }
}
