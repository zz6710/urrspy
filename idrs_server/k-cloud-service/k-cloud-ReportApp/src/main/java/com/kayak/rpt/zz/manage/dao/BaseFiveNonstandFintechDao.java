package com.kayak.rpt.zz.manage.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.manage.model.BaseFiveNonstandFintech;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

@Repository
public class BaseFiveNonstandFintechDao extends ComnDao {

	public SqlResult<BaseFiveNonstandFintech> findBaseFiveNonstandFintechs(SqlParam<BaseFiveNonstandFintech> params) throws Exception {
		return super.findRows("SELECT id,windid,companycode,ssxyfield,ssxycode,ssxyname,nbstypecode,nbstypename,icneacode,icneaname,confidence,period FROM base_five_nonstand_fintech", params);
	}

	public UpdateResult addBaseFiveNonstandFintech(SqlParam<BaseFiveNonstandFintech> params) throws Exception {
		return super.update("INSERT INTO base_five_nonstand_fintech(id,windid,companycode,ssxyfield,ssxycode,ssxyname,nbstypecode,nbstypename,icneacode,icneaname,confidence,period) VALUES($AUTOIDI{id},$S{windid},$S{companycode},$S{ssxyfield},$S{ssxycode},$S{ssxyname},$S{nbstypecode},$S{nbstypename},$S{icneacode},$S{icneaname},$S{confidence},$S{period})",
				params.getModel());
	}
	
	public UpdateResult updateBaseFiveNonstandFintech(SqlParam<BaseFiveNonstandFintech> params) throws Exception {
		return super.update("UPDATE base_five_nonstand_fintech SET windid=$S{windid} ,companycode=$S{companycode} ,ssxyfield=$S{ssxyfield} ,ssxycode=$S{ssxycode} ,ssxyname=$S{ssxyname} ,nbstypecode=$S{nbstypecode} ,nbstypename=$S{nbstypename} ,icneacode=$S{icneacode} ,icneaname=$S{icneaname} ,confidence=$S{confidence} ,period=$S{period}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteBaseFiveNonstandFintech(SqlParam<BaseFiveNonstandFintech> params) throws Exception {
		return super.update("DELETE FROM base_five_nonstand_fintech WHERE  id=$I{id} ",
				params.getModel());
	}

	public void importBaseFiveNonstandFintech(List<BaseFiveNonstandFintech> baseFiveNonstandFinteches, Map<String, Object> params) throws Exception {
		//开启事务处理批处理
		doTrans( () ->{
			PreparedStatement preparedStatement = null;
			try {
				super.update("DELETE FROM base_five_nonstand_fintech WHERE period=$S{period} ", params);
				Connection connection = this.getConnection();
				preparedStatement = connection.prepareStatement("INSERT INTO base_five_nonstand_fintech(windid,companycode,ssxyfield,ssxycode,ssxyname,nbstypecode,nbstypename,icneacode,icneaname,confidence,period) VALUES(?,?,?,?,?,?,?,?,?,?,?)");
				for (BaseFiveNonstandFintech baseFiveNonstandFintech : baseFiveNonstandFinteches) {
					preparedStatement.setString(1, baseFiveNonstandFintech.getWindid());
					preparedStatement.setString(2, baseFiveNonstandFintech.getCompanycode());
					preparedStatement.setString(3, baseFiveNonstandFintech.getSsxyfield());
					preparedStatement.setString(4, baseFiveNonstandFintech.getSsxycode());
					preparedStatement.setString(5, baseFiveNonstandFintech.getSsxyname());
					preparedStatement.setString(6, baseFiveNonstandFintech.getNbstypecode());
					preparedStatement.setString(7, baseFiveNonstandFintech.getNbstypename());
					preparedStatement.setString(8, baseFiveNonstandFintech.getIcneacode());
					preparedStatement.setString(9, baseFiveNonstandFintech.getIcneaname());
					preparedStatement.setString(10, baseFiveNonstandFintech.getConfidence());
					preparedStatement.setString(11, baseFiveNonstandFintech.getPeriod());

					preparedStatement.addBatch();
				}
				preparedStatement.executeBatch();
			} catch (Exception e) {
				throw e;
			} finally {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
			}
		});
	}

}
