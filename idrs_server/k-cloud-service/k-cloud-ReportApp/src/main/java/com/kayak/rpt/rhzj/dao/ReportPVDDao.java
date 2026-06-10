package com.kayak.rpt.rhzj.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.rhzj.model.ReportPVD;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReportPVDDao extends ComnDao {

	public SqlResult<ReportPVD> findReportPVDs(SqlParam<ReportPVD> params) throws Exception {
		return super.findRows("SELECT id,report_date,prod_code,pbc_assetscode,data_type,cny,end_amount,end_amount_rmb FROM app_rpt_pvd", params);
	}

	public UpdateResult addReportPVD(SqlParam<ReportPVD> params) throws Exception {
		return super.update("INSERT INTO app_rpt_pvd(id,report_date,prod_code,pbc_assetscode,data_type,cny,end_amount,end_amount_rmb) VALUES($AUTOIDI{id},$S{reportDate},$S{prodCode},$S{pbcAssetscode},$S{dataType},$S{cny},if($S{endAmount}='',null,$S{endAmount}),if($S{endAmountRmb}='',null,$S{endAmountRmb}))",
				params.getModel());
	}

	public UpdateResult addReportPVD(Object params) throws Exception {
		return super.update("INSERT INTO app_rpt_pvd(id,report_date,prod_code,pbc_assetscode,data_type,cny,end_amount,end_amount_rmb) VALUES($AUTOIDI{id},$S{reportDate},$S{prodCode},$S{pbcAssetscode},$S{dataType},$S{cny},if($S{endAmount}='',null,$S{endAmount}),if($S{endAmountRmb}='',null,$S{endAmountRmb}))",
				params);
	}
	
	public UpdateResult updateReportPVD(SqlParam<ReportPVD> params) throws Exception {
		return super.update("UPDATE app_rpt_pvd SET id=$I{id} ,report_date=$S{reportDate} ,prod_code=$S{prodCode} ,pbc_assetscode=$S{pbcAssetscode} ,data_type=$S{dataType} ,cny=$S{cny} ,end_amount=if($S{endAmount}='',null,$S{endAmount}) ,end_amount_rmb=if($S{endAmountRmb}='',null,$S{endAmountRmb}) WHERE id = $S{id}",
				params.getModel());
	}
	
	public UpdateResult deleteReportPVD(SqlParam<ReportPVD> params) throws Exception {
		return super.update("DELETE FROM app_rpt_pvd WHERE id = $S{id}",
				params.getModel());
	}
	public UpdateResult deleteReportPVDByReportDate(Object params) throws Exception {
		return super.update("DELETE FROM app_rpt_pvd where report_date = last_day($S{queryDate})",
				params);
	}

	public SqlResult<ReportPVD> validateReportPVDsAmount(SqlParam<ReportPVD> params) throws Exception {
		return super.findRows("select prod_code,pbc_assetscode,data_type,end_amount from app_rpt_pvd where report_date = $S{reportDate} and end_amount<0 and data_type not in ('A9000','B4000','C0000','C2000','C3000') order by prod_code,data_type", params);
	}

	public List<SqlRow> validateReportPVDsSum(Object params) throws Exception {
		return super.findRows("with tb as\n" +
				"    (\n" +
				"    SELECT  sum(end_amount) amount,PROD_CODE,data_type,pbc_assetscode fROM app_rpt_pvd where\n" +
				"\t\treport_date= $S{reportDate}\n" +
				"    group by PROD_CODE,data_type,pbc_assetscode order by prod_code\n" +
				"    )\n" +
				"    select t1.prod_code,t1.pbc_assetscode,'特定目的载体份额-母项不等于子项之和' data_type,if(t1.amount=t2.amount,1,0) isEqual from\n" +
				"    (select sum(amount) amount,prod_code,pbc_assetscode from tb  where data_type in ('A7200') group by prod_code,pbc_assetscode) t1\n" +
				"    left join\n" +
				"     (select sum(amount)amount,prod_code,pbc_assetscode from tb where data_type  in('A7210','A7220','A7230','A7240','A7250','A7260','A7270','A7280','A7290','A72a0') group by prod_code,pbc_assetscode  order by prod_code) t2 on t1.prod_code = t2.prod_code\n" +
				"   union all\n" +
				"       select t1.prod_code,t1.pbc_assetscode,'权益合计-母项不等于子项之和' data_type,if(t1.amount=t2.amount,1,0) isEqual from\n" +
				"    (select sum(amount) amount,prod_code,pbc_assetscode from tb  where data_type in ('C0000') group by prod_code,pbc_assetscode) t1\n" +
				"    left join\n" +
				"     (select sum(amount)amount,prod_code,pbc_assetscode from tb where data_type  in ('C1000','C3000','C4000') group by prod_code,pbc_assetscode  order by prod_code) t2 on t1.prod_code = t2.prod_code\n" +
				"      union all\n" +
				"    select t1.prod_code,t1.pbc_assetscode,'资产合计-母项不等于子项之和' data_type,if(ifnull(t1.amount,0)=ifnull(t2.amount,0)-ifnull(t3.amount,0),1,0) isEqual from\n" +
				"    (select sum(amount) amount,prod_code,pbc_assetscode from tb  where data_type in ('A0001') group by prod_code,pbc_assetscode) t1\n" +
				"    left join\n" +
				"     (select sum(amount)amount,prod_code,pbc_assetscode from tb where data_type  in('A1000','A2000','A3000','A4000','A5000','A6000','A7000','A8000','A9000','AA001','AB000') group by prod_code,pbc_assetscode  order by prod_code) t2 on t1.prod_code = t2.prod_code\n" +
				"    left join\n" +
				"     (select sum(amount) amount,prod_code,pbc_assetscode from tb  where data_type in ('AK000') group by prod_code,pbc_assetscode order by prod_code) t3 on t1.prod_code = t3.prod_code\n" +
				"\n" +
				"     union all\n" +
				"    select t1.prod_code,t1.pbc_assetscode,'资产合计不等于负债合计和权益合计之和' data_type,if(ifnull(t1.amount,0)=ifnull(t2.amount,0),1,0) isEqual from\n" +
				"    (select sum(amount) amount,prod_code,pbc_assetscode from tb  where data_type in ('A0001') group by prod_code,pbc_assetscode) t1\n" +
				"    left join\n" +
				"     (select sum(amount)amount,prod_code,pbc_assetscode from tb where data_type  in('B0000','C0000') group by prod_code,pbc_assetscode  order by prod_code) t2 on t1.prod_code = t2.prod_code", params);
	}
}
