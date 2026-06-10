package com.kayak.pms.netValue.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.pms.netValue.model.T8ProdNetValueNotice;
import com.spire.ms.System.Collections.ArrayList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class T8ProdNetValueNoticeDao extends ComnDao {

	public List<T8ProdNetValueNotice> findT8ProdNetValueNotices(T8ProdNetValueNotice params) throws Exception {
		StringBuffer sql = new StringBuffer("SELECT notice.id,notice.t8_disclosure_task_id,notice.prod_code,notice.netval_date," +
				" notice.disclosure_date,notice.total_net,notice.total_vol,notice.nav,notice.nav_profit," +
				" notice.ten_thousand_income_amt,notice.seven_days_income_rate,notice.total_nav," +
				" notice.crt_date,notice.crt_time,notice.crt_user_id,notice.crt_user_name," +
				" notice.upd_date,notice.upd_time,notice.upd_user_id,notice.upd_user_name," +
				" notice.remark,notice.status," +
				" prod.prod_name,prod.is_share_sort " +
				" FROM t8_prod_net_value_notice notice" +
				" left join t8_prod_info prod " +
				" on notice.prod_code = prod.prod_code where 1 = 1 ");
		if(StringUtils.isNotBlank(params.getProdCode())){
			sql = sql.append(" and notice.prod_code = '"+params.getProdCode()+"' ");
		}

		if(StringUtils.isNotBlank(params.getProdName())){
           sql = sql.append(" and prod.prod_name like '%" + params.getProdName() + "%' ");
		}

		if(StringUtils.isNotBlank(params.getT8DisclosureTaskId())){
			sql = sql.append(" and notice.t8_disclosure_task_id = '"+params.getT8DisclosureTaskId()+"' ");
		}

		if(StringUtils.isNotBlank(params.getNetvalDate())){
			sql = sql.append(" and notice.netval_date = '"+params.getNetvalDate()+"' ");
		}

		if(StringUtils.isNotBlank(params.getDisclosureDate())){
			sql = sql.append(" and notice.disclosure_date = '"+params.getDisclosureDate()+"' ");
		}
		if(StringUtils.isNotBlank(params.getIsShareSort())){
			sql = sql.append(" and prod.is_share_sort = '"+params.getIsShareSort()+"' ");
		}

		sql = sql.append(" order by notice.prod_code ");
		return super.findRows(T8ProdNetValueNotice.class,sql.toString(),0,null);
	}

	public List<T8ProdNetValueNotice> findT8ProdShareSort(T8ProdNetValueNotice params) throws Exception {
    String sql = "select tpn.PROD_CODE,tpn.PROD_NAME,'"+params.getDisclosureDate()+"' as disclosure_date," +
			" tpn.NAV_DATE netval_date,tpn.TOTAL_NET,tpn.TOTAL_VOL,tpn.NAV_PROFIT,tpn.NAV,tpn.TOTAL_NAV,tpn.TEN_THOUSAND_INCOME_AMT," +
			" tpn.SEVEN_DAYS_INCOME_RATE,'0' is_parent_prod from t8_prod_nav tpn where tpn.PROD_CODE in (select tpss.sales_code prod_code " +
			" from t8_prod_share_sort tpss left join t8_prod_info tpi on tpi.id = tpss.t8_prod_info_id " +
			" where tpi.prod_code = '"+params.getProdCode()+"' ) and tpn.NAV_DATE = '"+params.getNetvalDate()+"' ";
		return super.findRows(T8ProdNetValueNotice.class,sql,0,params);
	}


	public SqlResult<T8ProdNetValueNotice> findT8ProdNetValueNoticeList(SqlParam<T8ProdNetValueNotice> params) throws Exception {
		return super.findRows("SELECT notice.id,notice.t8_disclosure_task_id,notice.prod_code,notice.netval_date," +
				" notice.disclosure_date,notice.total_net,notice.total_vol,notice.nav,notice.nav_profit," +
				" notice.ten_thousand_income_amt,notice.seven_days_income_rate,notice.total_nav," +
				" notice.crt_date,notice.crt_time,notice.crt_user_id,notice.crt_user_name," +
				" notice.upd_date,notice.upd_time,notice.upd_user_id,notice.upd_user_name," +
				" notice.remark,notice.status," +
				" prod.prod_name " +
				" FROM t8_prod_net_value_notice notice" +
				" left join t8_prod_info prod " +
				"        on notice.prod_code = prod.prod_code where notice.prod_code=$S{prodCode} and notice.netval_date=$S{netvalDate}", params);
	}

	public List<SqlRow> findNetValueNoticeList(String taskId) throws Exception {
		return super.findRows("SELECT notice.id,notice.t8_disclosure_task_id,notice.prod_code,notice.netval_date," +
				" notice.disclosure_date,notice.total_net,notice.total_vol,notice.nav,notice.nav_profit," +
				" notice.ten_thousand_income_amt,notice.seven_days_income_rate,notice.total_nav," +
				" notice.crt_date,notice.crt_time,notice.crt_user_id,notice.crt_user_name," +
				" notice.upd_date,notice.upd_time,notice.upd_user_id,notice.upd_user_name," +
				" notice.remark,notice.status," +
				" prod.prod_name " +
				" FROM t8_prod_net_value_notice notice" +
				" left join t8_prod_info prod " +
				"        on notice.prod_code = prod.prod_code where t8_disclosure_task_id='"+taskId+"'", taskId);
	}

	/**
	 * 功能:查询净值数据list
	 * 作者：rennannan
	 * 日期：20210626
	 *
	 * @param notice
	 * @return
	 */
	public List<T8ProdNetValueNotice> findT8NetValueNoticeList(T8ProdNetValueNotice notice) throws Exception {
		String sql = "SELECT id,t8_disclosure_task_id,prod_code," +
				" netval_date,disclosure_date," +
				" total_net,total_vol,nav,nav_profit,ten_thousand_income_amt," +
				" seven_days_income_rate,total_nav," +
				" crt_date,crt_time,crt_user_id,crt_user_name," +
				" upd_date,upd_time,upd_user_id,upd_user_name," +
				" remark,status " +
				" FROM t8_prod_net_value_notice" +
				" where 1=1 ";
		if (StringUtils.isNotEmpty(notice.getDisclosureDate())) {
			sql += " and disclosure_date=$S{disclosureDate}";
		}
		return super.findRows(T8ProdNetValueNotice.class, sql, 0, notice);
	}

	/**
	 * 功能:查询净值数据list(现金管理类产品)
	 * 作者：rennannan
	 * 日期：20210626
	 *
	 * @param notice
	 * @return
	 */
	public List<T8ProdNetValueNotice> findNetValueNoticeListForMoney(T8ProdNetValueNotice notice) throws Exception {
		List<T8ProdNetValueNotice> list = new ArrayList();
		//查询现金管理类产品
		String sql = "SELECT tpnvn.prod_code,tpi.prod_name,tpnvn.id,tpnvn.t8_disclosure_task_id,tpnvn.prod_code," +
				" tpnvn.netval_date,tpnvn.disclosure_date," +
				" tpnvn.total_net,tpnvn.total_vol,tpnvn.nav,tpnvn.nav_profit,tpnvn.ten_thousand_income_amt," +
				" tpnvn.seven_days_income_rate,tpnvn.total_nav," +
				" tpnvn.crt_date,tpnvn.crt_time,tpnvn.crt_user_id,tpnvn.crt_user_name," +
				" tpnvn.upd_date,tpnvn.upd_time,tpnvn.upd_user_id,tpnvn.upd_user_name," +
				" tpnvn.remark,tpnvn.status,tpi.is_share_sort " +
				" FROM t8_prod_net_value_notice tpnvn left join t8_prod_info tpi on tpnvn.prod_code = tpi.prod_code " +
				" where 1=1 and tpi.prod_mode='4' ";
		if (StringUtils.isNotEmpty(notice.getDisclosureDate())) {
			sql += " and disclosure_date=$S{disclosureDate}";
		}

		List<T8ProdNetValueNotice> moneyList = super.findRows(T8ProdNetValueNotice.class, sql, 0, notice);
		//finalList用来处理数据顺序问题
		List<T8ProdNetValueNotice> finalList = new ArrayList();

		//用来接收获取的份额分类信息
		for(T8ProdNetValueNotice t8ProdNetValueNotice : moneyList){
			finalList.add(t8ProdNetValueNotice);
			//查询份额分类的信息
			if("1".equals(t8ProdNetValueNotice.getIsShareSort())){
				List<T8ProdNetValueNotice> t8ProdShareSort1 = findT8ProdShareSort1(t8ProdNetValueNotice);
				finalList.addAll(t8ProdShareSort1);
			}
		}

		T8ProdNetValueNotice temp1 = new T8ProdNetValueNotice();
		temp1.setProdCode("产品代码");
		temp1.setProdName("产品名称");
		temp1.setTotalVol("产品总份额");
		temp1.setNavProfit("本日收益");
		temp1.setTenThousandIncomeAmt("每万份收益");
		temp1.setSevenDaysIncomeRate("七日年化收益率");
		temp1.setNetvalDate("净值日期");
		finalList.add(0,temp1);

		T8ProdNetValueNotice temp4 = new T8ProdNetValueNotice();
		temp4.setProdCode("公告类型");
		temp4.setTotalVol("现金管理类净值");
		temp4.setNavProfit("");
		temp4.setTenThousandIncomeAmt("");
		temp4.setSevenDaysIncomeRate("");
		temp4.setNetvalDate("");
		finalList.add(0,temp4);
		list.addAll(finalList);
		return list;
	}

	public List<T8ProdNetValueNotice> findT8ProdShareSort1(T8ProdNetValueNotice notice) throws Exception {
		String sql = "select tpn.PROD_CODE,tpn.PROD_NAME," +
				" tpn.NAV_DATE netval_date,tpn.TOTAL_NET,tpn.TOTAL_VOL,tpn.NAV_PROFIT,tpn.NAV,tpn.TOTAL_NAV,tpn.TEN_THOUSAND_INCOME_AMT," +
				" tpn.SEVEN_DAYS_INCOME_RATE from t8_prod_nav tpn where tpn.PROD_CODE in (select tpss.sales_code prod_code " +
				" from t8_prod_share_sort tpss left join t8_prod_info tpi on tpi.id = tpss.t8_prod_info_id " +
				" where tpi.prod_code = '"+notice.getProdCode()+"' ) and tpn.NAV_DATE = '"+notice.getNetvalDate()+"' ";
		return super.findRows(T8ProdNetValueNotice.class,sql,0,notice);
	}


	/**
	 * 功能:查询净值数据list(非现金管理类产品)
	 * 作者：rennannan
	 * 日期：20210626
	 *
	 * @param notice
	 * @return
	 */
	public List<T8ProdNetValueNotice> findNetValueNoticeListForNoMoney(T8ProdNetValueNotice notice) throws Exception {
		List<T8ProdNetValueNotice> list = new ArrayList();
		//查询非现金管理类产品
		String sql2 = "SELECT tpnvn.prod_code,tpi.prod_name,tpnvn.id,tpnvn.t8_disclosure_task_id,tpnvn.prod_code," +
				" tpnvn.netval_date,tpnvn.disclosure_date," +
				" tpnvn.total_net,tpnvn.total_vol,tpnvn.nav,tpnvn.nav_profit,tpnvn.ten_thousand_income_amt," +
				" tpnvn.seven_days_income_rate,tpnvn.total_nav," +
				" tpnvn.crt_date,tpnvn.crt_time,tpnvn.crt_user_id,tpnvn.crt_user_name," +
				" tpnvn.upd_date,tpnvn.upd_time,tpnvn.upd_user_id,tpnvn.upd_user_name," +
				" tpnvn.remark,tpnvn.status,tpi.is_share_sort " +
				" FROM t8_prod_net_value_notice tpnvn left join t8_prod_info tpi on tpnvn.prod_code = tpi.prod_code " +
				" where 1=1 and tpi.prod_mode!='4' ";
		if (StringUtils.isNotEmpty(notice.getDisclosureDate())) {
			sql2 += " and tpnvn.disclosure_date=$S{disclosureDate}";
		}
		List<T8ProdNetValueNotice> netValueList = super.findRows(T8ProdNetValueNotice.class, sql2, 0, notice);

		//判断非现金管理类list是否为空,不为空,添加list表头
		//if(!CollectionUtils.isEmpty(netValueList)){
		List<T8ProdNetValueNotice> tempList = new ArrayList();
		//调整非现金管理类字段取值,方便excel导出
		for(int i=0;i<netValueList.size();i++){
			T8ProdNetValueNotice temp = new T8ProdNetValueNotice();
			temp.setProdName(netValueList.get(i).getProdName());
			temp.setProdCode(netValueList.get(i).getProdCode());
			temp.setTotalVol(netValueList.get(i).getTotalVol());
			temp.setNavProfit(netValueList.get(i).getTotalNet());
			temp.setTenThousandIncomeAmt(netValueList.get(i).getNav());
			temp.setSevenDaysIncomeRate(netValueList.get(i).getTotalNav());
			temp.setNetvalDate(netValueList.get(i).getNetvalDate());
            temp.setIsShareSort(netValueList.get(i).getIsShareSort());
			tempList.add(temp);

			//用来接收获取的份额分类信息
			List<T8ProdNetValueNotice> shareSort1 = new java.util.ArrayList<>();
			if("1".equals(netValueList.get(i).getIsShareSort())){
				//查询份额分类的信息
				List<T8ProdNetValueNotice> t8ProdShareSort1 = findT8ProdShareSort1(netValueList.get(i));
				//调整份额分类非现金管理类字段取值,方便excel导出
				for(T8ProdNetValueNotice t8ProdNetValueNotice : t8ProdShareSort1){
					t8ProdNetValueNotice.setTenThousandIncomeAmt(t8ProdNetValueNotice.getNav());
					t8ProdNetValueNotice.setSevenDaysIncomeRate(t8ProdNetValueNotice.getTotalNav());
				}
				shareSort1.addAll(t8ProdShareSort1);
			    }
			tempList.addAll(shareSort1);
		}
		T8ProdNetValueNotice temp2 = new T8ProdNetValueNotice();
		temp2.setProdCode("产品代码");
		temp2.setProdName("产品名称");
		temp2.setTotalVol("产品总份额");
		temp2.setNavProfit("总净值");
		temp2.setTenThousandIncomeAmt("单位净值");
		temp2.setSevenDaysIncomeRate("累计净值");
		temp2.setNetvalDate("净值日期");
		tempList.add(0,temp2);

		T8ProdNetValueNotice temp5 = new T8ProdNetValueNotice();
		temp5.setProdCode("公告类型");
		temp5.setTotalVol("非现金管理类");
		temp5.setNavProfit("");
		temp5.setTenThousandIncomeAmt("");
		temp5.setSevenDaysIncomeRate("");
		temp5.setNetvalDate("");
		tempList.add(0,temp5);

		list.addAll(tempList);

		return list;
	}

	/**
	 * 功能:查询净值数据不完整产品集合
	 * 作者：zls
	 * 日期：20210804
	 *
	 * @param notice
	 * @return
	 */
	public String findNetValueNoticeListForNotComplete(T8ProdNetValueNotice notice) throws Exception {
		String prodCode = "";
		//查询总份额为空的产品,标记为数据不完整
		String sql =
        "select GROUP_CONCAT(prod_code) prod_code from (SELECT tpnvn.prod_code FROM t8_prod_net_value_notice tpnvn LEFT JOIN t8_prod_info tpi ON tpnvn.prod_code=tpi.prod_code WHERE 1=1 AND tpi.prod_mode='4' AND tpnvn.total_vol IS NULL ";
		if (StringUtils.isNotEmpty(notice.getDisclosureDate())) {
			sql += " and disclosure_date=$S{disclosureDate}";
		}
		sql = sql +"UNION SELECT tpnvn.prod_code FROM t8_prod_net_value_notice tpnvn LEFT JOIN t8_prod_info tpi ON tpnvn.prod_code=tpi.prod_code WHERE 1=1 AND tpi.prod_mode !='4' AND tpnvn.total_vol IS NULL ";
		if (StringUtils.isNotEmpty(notice.getDisclosureDate())) {
			sql += " and disclosure_date=$S{disclosureDate}";
		}
		sql = sql+" )temp";
		List<T8ProdNetValueNotice> prodCodeList = super.findRows(T8ProdNetValueNotice.class, sql, 0, notice);
		if(prodCodeList!=null && prodCodeList.size()>0){
			prodCode = prodCodeList.get(0).getProdCode();
		}


		return prodCode;
	}

	public UpdateResult addT8ProdNetValueNotice(SqlParam<T8ProdNetValueNotice> params) throws Exception {
		return super.update("INSERT INTO t8_prod_net_value_notice(id,t8_disclosure_task_id,prod_code,netval_date,disclosure_date,total_net,total_vol,nav,nav_profit,ten_thousand_income_amt,seven_days_income_rate,total_nav,crt_date,crt_time,crt_user_id,crt_user_name,upd_date,upd_time,upd_user_id,upd_user_name,remark,status) VALUES($AUTOIDS{id},$S{t8DisclosureTaskId},$S{prodCode},$S{netvalDate},$S{disclosureDate},$D{totalNet},$D{totalVol},$D{nav},$D{navProfit},$D{tenThousandIncomeAmt},$D{sevenDaysIncomeRate},$D{totalNav},$S{crtDate},$S{crtTime},$S{crtUserId},$S{crtUserName},$S{updDate},$S{updTime},$S{updUserId},$S{updUserName},$S{remark},$S{status})",
				params.getModel());
	}

	public UpdateResult updateT8ProdNetValueNotice(SqlParam<T8ProdNetValueNotice> params) throws Exception {
		return super.update("UPDATE t8_prod_net_value_notice SET t8_disclosure_task_id=$S{t8DisclosureTaskId} ,prod_code=$S{prodCode} ,netval_date=$S{netvalDate} ,disclosure_date=$S{disclosureDate} ,total_net=$D{totalNet} ,total_vol=$D{totalVol} ,nav=$D{nav} ,nav_profit=$D{navProfit} ,ten_thousand_income_amt=$D{tenThousandIncomeAmt} ,seven_days_income_rate=$D{sevenDaysIncomeRate} ,total_nav=$D{totalNav} ,crt_date=$S{crtDate} ,crt_time=$S{crtTime} ,crt_user_id=$S{crtUserId} ,crt_user_name=$S{crtUserName} ,upd_date=$S{updDate} ,upd_time=$S{updTime} ,upd_user_id=$S{updUserId} ,upd_user_name=$S{updUserName} ,remark=$S{remark} ,status=$S{status}  WHERE  id=$S{id} ",
				params.getModel());
	}

	/**
	 * 功能：修改披露产品净值信息
	 * 作者：rennannan
	 * 日期：20210626
	 *
	 * @param notice
	 * @throws Exception
	 */
	public void updateProdNetValueNotice(T8ProdNetValueNotice notice) throws Exception {
		String sql = " UPDATE t8_prod_net_value_notice " +
				" SET t8_disclosure_task_id=$S{t8DisclosureTaskId} ," +
				" netval_date=$S{netvalDate} ," +
				" disclosure_date=$S{disclosureDate} ," +
				" total_net=$D{totalNet} ," +
				" total_vol=$D{totalVol} ," +
				" nav=$D{nav} ," +
				" nav_profit=$D{navProfit} ," +
				" ten_thousand_income_amt=$D{tenThousandIncomeAmt} ," +
				" seven_days_income_rate=$D{sevenDaysIncomeRate} ," +
				" total_nav=$D{totalNav} ," +
				" upd_date=$S{updDate} ," +
				" upd_time=$S{updTime} ," +
				" upd_user_id=$S{updUserId} ," +
				" upd_user_name=$S{updUserName} ," +
				" remark=$S{remark} ," +
				" status=$S{status}  " +
				" WHERE id=$S{id} ";
		super.update(sql, notice);

	}

	/**
	 * 功能：修改产品净值披露数据状态
	 * 作者：rennannan
	 * 日期：20210626
	 */
	public void updateNoticeStatus(T8ProdNetValueNotice notice) throws Exception {
		String sql = " update t8_prod_net_value_notice " +
				" set status=$S{status} " +
				" where id=$S{id}";
		super.update(sql, notice);
	}

	public UpdateResult deleteT8ProdNetValueNotice(SqlParam<T8ProdNetValueNotice> params) throws Exception {
		return super.update("DELETE FROM t8_prod_net_value_notice WHERE  id=$S{id} ",
				params.getModel());
	}

	/**
	 * 功能：根据净值日期删除净值数据
	 * 作者：rennannan
	 * 日期：20210626
	 */
	public void deleteNoticeByNetDay(String netValDate) throws Exception {
		String sql = " delete from t8_prod_net_value_notice where netval_date=$S{netValDate} or master_netval_date='"+netValDate+"'";
		super.update(sql, netValDate);
	}

	/**
	 * 功能：插入净值数据信息 传入参数为T8ProdNetValueNotice
	 * 作者：rennannan
	 * 日期：20210626
	 *
	 * @param params
	 * @throws Exception
	 */
	public void addNetValueNotice(T8ProdNetValueNotice params) throws Exception {
		String sql = "INSERT INTO t8_prod_net_value_notice(  id,t8_disclosure_task_id,prod_code,master_netval_date," +
				" netval_date,disclosure_date," +
				" total_net,total_vol,nav,nav_profit,ten_thousand_income_amt," +
				" seven_days_income_rate,total_nav," +
				" crt_date,crt_time,crt_user_id,crt_user_name," +
				" remark,status) " +
				" VALUES($AUTOIDS{id},$S{t8DisclosureTaskId},$S{prodCode},$S{masterNetvalDate}," +
				" $S{netvalDate},$S{disclosureDate}," +
				" $D{totalNet},$D{totalVol},$D{nav},$D{navProfit},$D{tenThousandIncomeAmt}," +
				" $D{sevenDaysIncomeRate},$D{totalNav}," +
				" $S{crtDate},$S{crtTime},$S{crtUserId},$S{crtUserName}," +
				" $S{remark},$S{status})";
		super.update(sql, params);
	}

	/**
	 * 功能：根据披露日期修改净值披露日期的id
	 * 作者：rennannan
	 * 日期：20210626
	 */
	public void updateNoticeTaskId(T8ProdNetValueNotice params) throws Exception {
		String sql = " update t8_prod_net_value_notice " +
				" set t8_disclosure_task_id = $S{t8DisclosureTaskId} " +
				" where disclosure_date=$S{disclosureDate}";
		super.update(sql, params);
	}

	/**
	 * 功能：查询净值披露发起审批角色id
	 * 作者：zls
	 * 日期：20210805
	 */
	public List<SqlRow> findRoleId(String id) throws Exception {
		String sql = "select sr.roleid,sur.userid from sys_role sr left join sys_user_role sur on sr.roleid=sur.roleid where sr.rolename='估值核算岗'";
		return super.findRows(sql, id);
	}
}
