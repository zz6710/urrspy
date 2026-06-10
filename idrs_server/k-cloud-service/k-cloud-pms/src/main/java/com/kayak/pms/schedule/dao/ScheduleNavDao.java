package com.kayak.pms.schedule.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.DateFormatEnum;
import com.kayak.core.util.DateUtil;
import com.kayak.pms.netValue.model.T8ProdNetValueNotice;
import com.kayak.pms.prod.model.T8ProdBaseNav;
import com.kayak.pms.schedule.model.ScheduleNav;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class ScheduleNavDao extends ComnDao {

    public void insertNavData(ScheduleNav nav) throws Exception {
            update("insert into t8_prod_nav (`ID`,`NUMBER`,`NAV_DATE`,`PROD_NAME`,`PROD_CODE`," +
                            "`TOTAL_NET`,`TOTAL_VOL`,`NAV`,`NAV_PROFIT`,`TEN_THOUSAND_INCOME_AMT`," +
                            "`SEVEN_DAYS_INCOME_RATE`,`SALE_SERVICE_FEE`,`TOTAL_NAV`,`NAV_GROWTH_RATE`," +
                            "`MANAGEMENT_FEES`,`QUARTER_INCOME_RATE`,`SEC_ASS_PROFIT`,`NON_DEPOSIT_DAY`," +
                            "`TOTAL_ASSET_NAV`,`IMPORT_DATE`,`CRT_DATE`,`CRT_TIME`,`CRT_USER`)  " +

                            "VALUES ($AUTOIDS{id},$S{number},$S{navDate},$S{prodName},$S{prodCode},$S{totalNet},$S{totalVol}," +
                            "$S{nav},$S{navProfit},$S{tenThousandIncomeAmt},$S{sevenDaysIncomeRate}," +
                            "$S{saleServiceFee},$S{totalNav},$S{navGrowthRate},$S{managementFees}," +
                            "$S{quarterIncomeRate},$S{secAssProfit},$S{nonDepositDay},$S{totalAssetNav}," +
                            "$S{importDate},$S{crtDate},$S{crtTime},$S{crtUser})",
                    nav);

    }

    public void insertLog(String StartDate,String StartTime,String log,String remark) throws Exception {
        Date now = new Date();
        String endDate = DateUtil.formatDate(now, DateFormatEnum.DATE_FORMAT);
        String endTime = DateUtil.formatDate(now, DateFormatEnum.TIME_FORMAT);
            update("insert into t8_ods_log(`id`,`datatype`,`logs`,`start_date`,`start_time`,`end_date`,`end_time`,`remark`) values ($AUTOIDI{id},'3','"+log+"','"+StartDate+"','"+StartTime+"','"+endDate+"','"+endTime+"','"+remark+"')");
    }

    /**
     * 定时任务 查询所有成立的产品信息
     * @return
     * @throws Exception
     */
    public List<SqlRow> findProdInfo() throws Exception {
            return super.findRows("SELECT\r\n" + 
            		"  tpi.id,\r\n" + 
            		"	tpi.prod_code AS prodCode,\r\n" +
                    "  tpi.is_share_sort AS isShareSort, \r\n" +
            		"	tpa.establish_date AS establishDate,\r\n" + 
            		"IF\r\n" + 
            		"	( tpp.base_type = '2', tpp.base_min_rate, tpp.base_rate ) AS baseRate  \r\n" + 
            		"FROM\r\n" + 
            		"	t8_prod_info tpi\r\n" + 
            		"	LEFT JOIN t8_prod_calendar tpa ON tpi.id = tpa.t8_prod_info_id\r\n" + 
            		"	LEFT JOIN t8_prod_performance tpp \r\n" + 
            		"	 ON tpi.id = tpp.t8_prod_Info_id \r\n" + 
            		"WHERE\r\n" + 
            		"	tpi.prod_status IN ('6','7') \r\n" + 
            		"	union all \r\n" + 
            		"	SELECT\r\n" + 
            		"			tpi.id,\r\n" + 
            		"			tpi.sales_code AS prodCode,\r\n" +
                    "           '2' AS isShareSort, \r\n" +
            		"			tpa.establish_date AS establishDate,\r\n" + 
            		"		IF\r\n" + 
            		"			( tpi.base_type = '2', tpi.base_min_rate, tpi.base_rate ) AS baseRate  \r\n" +
            		"		FROM\r\n" + 
            		"			t8_prod_share_sort tpi\r\n" + 
            		"			LEFT JOIN t8_prod_calendar tpa ON tpi.t8_prod_info_id = tpa.t8_prod_info_id\r\n" + 
            		"			LEFT JOIN t8_prod_performance tpp  ON tpi.t8_prod_info_id = tpp.t8_prod_Info_id  \r\n" + 
            		"			left join t8_prod_info p on tpi.t8_prod_info_id = p.id\r\n" + 
            		"		WHERE\r\n" + 
            		"			p.prod_status IN ('6','7')");
    }

    public UpdateResult insertProdBasenav(T8ProdBaseNav t8ProdBaseNavSqlParam) throws Exception {
        return super.update("insert into t8_prod_base_nav(`PROD_CODE`,`NAV_DATE`,`BASE_NAV`,`CRT_DATE`,`CRT_TIME`) values ($S{prodCode},$S{navDate},$S{baseNav},$S{crtDate},$S{crtTime})",t8ProdBaseNavSqlParam);
    }
    /**
     * 功能：查询净值数据信息
     * 作者：rennannan
     * 日期：20210628
     *
     * @param nav
     * @return
     */
    public List<ScheduleNav> findNavList(ScheduleNav nav) throws Exception {
        String sql = " select id,NUMBER,NAV_DATE,PROD_CODE,TOTAL_NET,TOTAL_VOL,NAV,NAV_PROFIT,TEN_THOUSAND_INCOME_AMT," +
                " SEVEN_DAYS_INCOME_RATE,SALE_SERVICE_FEE,TOTAL_NAV,NAV_GROWTH_RATE,MANAGEMENT_FEES,QUARTER_INCOME_RATE," +
                " MANAGEMENT_FEES,QUARTER_INCOME_RATE,SEC_ASS_PROFIT,NON_DEPOSIT_DAY,TOTAL_ASSET_NAV" +
                " from t8_prod_nav where 1=1 ";
        if (StringUtils.isNotEmpty(nav.getNavDate())) {
            sql += " and NAV_DATE=$S{navDate}";
        }
        if (StringUtils.isNotEmpty(nav.getProdCode())) {
            sql += " and PROD_CODE=$S{prodCode}";
        }
        return super.findRows(ScheduleNav.class, sql, 0, nav);
    }

    public int updProdBasenav(T8ProdBaseNav t8ProdBaseNavSqlParam) throws Exception {
        return super.update("update t8_prod_base_nav set BASE_NAV=$S{baseNav},CRT_DATE=$S{crtDate},CRT_TIME=$S{crtTime} where PROD_CODE=$S{prodCode} and NAV_DATE=$S{navDate}",t8ProdBaseNavSqlParam).getEffect();
    }

    /**
     * 根据prodCode和navDate查询数据
     */
    public SqlRow findByPcNd(String prodCode,String navDate) throws Exception {
        return super.findRow("select PROD_CODE from t8_prod_base_nav where PROD_CODE = '"+prodCode+"' and NAV_DATE = '"+navDate+"'",null);
    }
    /**
     * t8_prod_nav和t8_prod_net_value_notice表所有数据
     */
    public List<T8ProdNetValueNotice> findProdNetValueNoticeAll() throws Exception {
        String sql = " \n" +
                "SELECT\n" +
                "\tnav.TOTAL_NET,\n" +
                "\tnav.TOTAL_VOL,\n" +
                "\tnav.NAV_PROFIT,\n" +
                "\tnav.NAV,\n" +
                "\tnav.TOTAL_NAV,\n" +
                "\tnav.TEN_THOUSAND_INCOME_AMT,\n" +
                "\tnav.SEVEN_DAYS_INCOME_RATE,\n" +
                "\tpnvn.prod_code,\n" +
                "\tpnvn.netval_date\n" +
                "FROM\n" +
                " t8_prod_nav nav\n" +
                "\tinner JOIN t8_prod_net_value_notice pnvn ON pnvn.prod_code = nav.prod_code \n" +
                "\tAND  pnvn.netval_date = nav.NAV_DATE ";
        return super.findRows(T8ProdNetValueNotice.class, sql, 0, null);
    }

    public int updProdNetValueNoticeAll(T8ProdNetValueNotice t8ProdNetValueNotice) throws Exception {
        return super.update(" update t8_prod_net_value_notice set total_net=$D{totalNet},total_vol=$D{totalVol}," +
                " nav=$D{nav},nav_profit=$D{navProfit},total_nav=$D{totalNav},ten_thousand_income_amt=$D{tenThousandIncomeAmt}," +
                " seven_days_income_rate=$D{sevenDaysIncomeRate} where prod_code = $S{prodCode} " +
                " and netval_date = $S{netvalDate} ",t8ProdNetValueNotice).getEffect();
    }

    public List<SqlRow> findShareSortProd(String prodCode) throws Exception {
        String sql = "select sort.sales_code as salesCode from t8_prod_info info left join t8_prod_share_sort sort on info.id=sort.t8_prod_info_id " +
                " where info.prod_code = '" + prodCode + "'";
        return findRows(sql);
    }

    //查询定价已确认的业绩基准信息（只有非份额分类产品）
    public List<SqlRow> findT8ProdPriceByCode() throws Exception {
//        String sql = "select tpd.* from t8_prod_price tpd left join t8_prod_info tpi on  tpd.prod_code = tpi.prod_code\n" +
//                "\tleft join t8_prod_performance tpp on tpp.t8_prod_info_id = tpi.id\n" +
//                "\twhere tpd.confirm_status in ('1','2') and tpi.is_share_sort = '0' and tpi.prod_code = '" + prodCode +"'";
        String sql = "select tpd.* from t8_prod_price tpd left join t8_prod_info tpi on  tpd.prod_code = tpi.prod_code\n" +
                "\tleft join t8_prod_performance tpp on tpp.t8_prod_info_id = tpi.id\n" +
                "\twhere tpd.confirm_status in ('1','2') and tpi.is_share_sort = '0'";
        return super.findRows(sql,0);
    }
}
