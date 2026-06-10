package com.kayak.rpt.zz.manage.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.manage.model.BaseFiveNonstandEntity;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

@Repository
public class BaseFiveNonstandEntityDao extends ComnDao {

	public SqlResult<BaseFiveNonstandEntity> findBaseFiveNonstandEntitys(SqlParam<BaseFiveNonstandEntity> params) throws Exception {
		return super.findRows("SELECT id,windid,companycode,companyname,usci,regioncode,region,isfintech,isgreen,isinclusive,ispension,isdigital,ishightech,issrdi,istechsme,isothertech,ishightechm,ishightechs,isstraemer,isintellectual,issmme,companyscale,isprivate,businessnature,isagriculture,isdigiindustry,isdigitalization,ispollution,period FROM base_five_nonstand_entity", params);
	}

	public UpdateResult addBaseFiveNonstandEntity(SqlParam<BaseFiveNonstandEntity> params) throws Exception {
		return super.update("INSERT INTO base_five_nonstand_entity(id,windid,companycode,companyname,usci,regioncode,region,isfintech,isgreen,isinclusive,ispension,isdigital,ishightech,issrdi,istechsme,isothertech,ishightechm,ishightechs,isstraemer,isintellectual,issmme,companyscale,isprivate,businessnature,isagriculture,isdigiindustry,isdigitalization,ispollution,period) VALUES($AUTOIDI{id},$S{windid},$S{companycode},$S{companyname},$S{usci},$S{regioncode},$S{region},$S{isfintech},$S{isgreen},$S{isinclusive},$S{ispension},$S{isdigital},$S{ishightech},$S{issrdi},$S{istechsme},$S{isothertech},$S{ishightechm},$S{ishightechs},$S{isstraemer},$S{isintellectual},$S{issmme},$S{companyscale},$S{isprivate},$S{businessnature},$S{isagriculture},$S{isdigiindustry},$S{isdigitalization},$S{ispollution},$S{period})",
				params.getModel());
	}
	
	public UpdateResult updateBaseFiveNonstandEntity(SqlParam<BaseFiveNonstandEntity> params) throws Exception {
		return super.update("UPDATE base_five_nonstand_entity SET windid=$S{windid} ,companycode=$S{companycode} ,companyname=$S{companyname} ,usci=$S{usci} ,regioncode=$S{regioncode} ,region=$S{region} ,isfintech=$S{isfintech} ,isgreen=$S{isgreen} ,isinclusive=$S{isinclusive} ,ispension=$S{ispension} ,isdigital=$S{isdigital} ,ishightech=$S{ishightech} ,issrdi=$S{issrdi} ,istechsme=$S{istechsme} ,isothertech=$S{isothertech} ,ishightechm=$S{ishightechm} ,ishightechs=$S{ishightechs} ,isstraemer=$S{isstraemer} ,isintellectual=$S{isintellectual} ,issmme=$S{issmme} ,companyscale=$S{companyscale} ,isprivate=$S{isprivate} ,businessnature=$S{businessnature} ,isagriculture=$S{isagriculture} ,isdigiindustry=$S{isdigiindustry} ,isdigitalization=$S{isdigitalization} ,ispollution=$S{ispollution} ,period=$S{period}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteBaseFiveNonstandEntity(SqlParam<BaseFiveNonstandEntity> params) throws Exception {
		return super.update("DELETE FROM base_five_nonstand_entity WHERE  id=$I{id} ",
				params.getModel());
	}

	public void importBaseFiveNonstandEntity(List<BaseFiveNonstandEntity> baseFiveNonstandEntities, Map<String, Object> params) throws Exception {
		//开启事务处理批处理
		doTrans( () ->{
			PreparedStatement preparedStatement = null;
			try {
				super.update("DELETE FROM base_five_nonstand_entity WHERE period=$S{period} ", params);
				Connection connection = this.getConnection();
				preparedStatement = connection.prepareStatement("INSERT INTO base_five_nonstand_entity(windid,companycode,companyname,usci,regioncode,region,isfintech,isgreen,isinclusive,ispension,isdigital,ishightech,issrdi,istechsme,isothertech,ishightechm,ishightechs,isstraemer,isintellectual,issmme,companyscale,isprivate,businessnature,isagriculture,isdigiindustry,isdigitalization,ispollution,period) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				for (BaseFiveNonstandEntity baseFiveNonstandEntity : baseFiveNonstandEntities) {
					preparedStatement.setString(1,  baseFiveNonstandEntity.getWindid());
					preparedStatement.setString(2,  baseFiveNonstandEntity.getCompanycode());
					preparedStatement.setString(3,  baseFiveNonstandEntity.getCompanyname());
					preparedStatement.setString(4,  baseFiveNonstandEntity.getUsci());
					preparedStatement.setString(5,  baseFiveNonstandEntity.getRegioncode());
					preparedStatement.setString(6,  baseFiveNonstandEntity.getRegion());
					preparedStatement.setString(7,  baseFiveNonstandEntity.getIsfintech());
					preparedStatement.setString(8,  baseFiveNonstandEntity.getIsgreen());
					preparedStatement.setString(9,  baseFiveNonstandEntity.getIsinclusive());
					preparedStatement.setString(10, baseFiveNonstandEntity.getIspension());
					preparedStatement.setString(11, baseFiveNonstandEntity.getIsdigital());
					preparedStatement.setString(12, baseFiveNonstandEntity.getIshightech());
					preparedStatement.setString(13, baseFiveNonstandEntity.getIssrdi());
					preparedStatement.setString(14, baseFiveNonstandEntity.getIstechsme());
					preparedStatement.setString(15, baseFiveNonstandEntity.getIsothertech());
					preparedStatement.setString(16, baseFiveNonstandEntity.getIshightechm());
					preparedStatement.setString(17, baseFiveNonstandEntity.getIshightechs());
					preparedStatement.setString(18, baseFiveNonstandEntity.getIsstraemer());
					preparedStatement.setString(19, baseFiveNonstandEntity.getIsintellectual());
					preparedStatement.setString(20, baseFiveNonstandEntity.getIssmme());
					preparedStatement.setString(21, baseFiveNonstandEntity.getCompanyscale());
					preparedStatement.setString(22, baseFiveNonstandEntity.getIsprivate());
					preparedStatement.setString(23, baseFiveNonstandEntity.getBusinessnature());
					preparedStatement.setString(24, baseFiveNonstandEntity.getIsagriculture());
					preparedStatement.setString(25, baseFiveNonstandEntity.getIsdigiindustry());
					preparedStatement.setString(26, baseFiveNonstandEntity.getIsdigitalization());
					preparedStatement.setString(27, baseFiveNonstandEntity.getIspollution());
					preparedStatement.setString(28, baseFiveNonstandEntity.getPeriod());

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
