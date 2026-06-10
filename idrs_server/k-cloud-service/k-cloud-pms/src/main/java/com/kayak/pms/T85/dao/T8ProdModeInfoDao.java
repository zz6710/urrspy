package com.kayak.pms.T85.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.Sql;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.Tools;
import com.kayak.pms.T85.model.T8ProdModeInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文件名: T8ProdModeInfoDao.java
 * 描述:   产品模型信息表
 * 创建人: zengzt
 * 创建时间:2020年6月4日下午1:42:48
 */
@Repository
public class T8ProdModeInfoDao extends ComnDao{


	public SqlResult<T8ProdModeInfo> findT8ProdModeInfos(SqlParam<T8ProdModeInfo> params) throws Exception {

		String sql = " SELECT prod_mode,prod_mode_name,create_url,remark FROM t8_prod_mode_info ORDER BY prod_mode";

		return super.findRows(sql, params);
	}

	public SqlResult<T8ProdModeInfo> findT8ProdModeInfosByCode(SqlParam<T8ProdModeInfo> params) throws Exception {

		String sql = " SELECT\n" +
				"\tprod_mode,\n" +
				"\tprod_mode_name,\n" +
				"\tcreate_url,\n" +
				"\tremark \n" +
				"FROM\n" +
				"\tt8_prod_mode_info \n" +
				"\twhere prod_mode_name not in(\"封闭净值\",\"天天净值\",\"定开净值\",\"货币净值\",\"自定义净值\")\n" +
				"ORDER BY\n" +
				"\tprod_mode";

		return super.findRows(sql, params);
	}

	public int insertT8ProdModeInfo(SqlParam<T8ProdModeInfo> params) throws Exception {

		String sql = " INSERT INTO t8_prod_mode_info(prod_mode,prod_mode_name,create_url,remark)VALUES("
				//+ "	LPAD(seq_prod_mode_itemkey.nextVal,8,0),$S{prodModeName},$S{createUrl},$S{remark}) ";
				+ "'"+Tools.getStringRandom(8)+"'" + ",$S{prodModeName},$S{createUrl},$S{remark}) ";

		return super.update(sql, params.getModel()).getEffect();
	}
	public boolean checkProdModeName(SqlParam<T8ProdModeInfo> params,boolean isUpdate) throws Exception {
		String sql="select count(1) c from t8_prod_mode_info c where c.PROD_MODE_NAME = $S{prodModeName} ";
		if(isUpdate){
			sql+=" and c.prod_mode <> $S{prodMode} ";
		}
		SqlRow row=super.findRow(sql,params.getModel());
		if(row.getInteger("c") == 0){
			return false;
		}
		return true;
	}

	public int updateT8ProdModeInfo(SqlParam<T8ProdModeInfo> params) throws Exception {

		String sql = " UPDATE t8_prod_mode_info SET  create_url=$S{createUrl},prod_mode_name=$S{prodModeName},remark=$S{remark}"
				+ "	 WHERE prod_mode=$S{prodMode}";

		return super.update(sql, params.getModel()).getEffect();
	}

	/**
	 *
	 * 方法描述:删除产品清算任务配置和产品模型
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public void deleteT8ProdModeInfo(SqlParam<T8ProdModeInfo> params) throws Exception {

		String sql = "DELETE FROM t8_prod_task_set WHERE prod_mode = $S{prodMode} ";

		String sql2 = " DELETE FROM  t8_prod_mode_info  WHERE prod_mode = $S{prodMode}  ";

		doTrans(() -> {
			//删除清算配置
			super.update(sql, params.getModel());
			//删除产品模型表数据
			super.update(sql2, params.getModel());
		});
	}


	public List<T8ProdModeInfo> checkTaProdWithMode(SqlParam<T8ProdModeInfo> params) throws Exception {
		String sql = "select group_concat(prod_code) prod_code from t8_prod_info where prod_mode_id = $S{prodMode}";
		SqlResult<T8ProdModeInfo> rows = super.findRows(sql, params);
		return rows.getRows();


	}



}
