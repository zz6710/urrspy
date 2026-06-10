package com.kayak.rpt.rhzj.biz;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import com.kayak.rpt.rhzj.service.ReportPPIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

//表示业务逻辑层的bean对象
@Service
public class FileGenerator {
    private static final Logger log = LoggerFactory.getLogger(FileGenerator.class);
    private final String SPLIT = "|";

    @Autowired
    private ReportPPIService reportPPIService;

    @Autowired
    private ComnDao comnDao;

    /**
     * 产品资金募集信息报送数据
     *
     * @throws Exception
     */
    public void generatePCD1(Map<String, Object> params) throws Exception {
        StringBuilder sb = new StringBuilder();
        String root = (String) params.get("root");
        PrintStream p = new PrintStream(new FileOutputStream(root + "/PCD1.dat"), false, "GBK");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            List<SqlRow> rows = comnDao.findRows("select id,report_date,prod_code,peoplebank_submitcode,area_code,cust_type,cny,current_buy_amount current_buy_amount,current_buy_amount_rmb current_buy_amount_rmb,current_buy_vol current_buy_vol,current_redemption_amount current_redemption_amount,current_redemption_amountrmb current_redemption_amountrmb,current_redemption_vol current_redemption_vol,termina_prod_amount termina_prod_amount,termina_prod_amount_rmb termina_prod_amount_rmb,termina_prod_vol termina_prod_vol,termina_prod_nav termina_prod_nav,termina_prod_nav_rmb termina_prod_nav_rmb,termina_prod_nav_add termina_prod_nav_add,termina_prod_nav_add_rmb termina_prod_nav_add_rmb,prod_max_rate prod_max_rate,prod_min_rate prod_min_rate from app_rpt_pcd where report_date like '$U{reportDate}%' order by prod_code,area_code,cny", params);
            Stream.iterate(0, i -> i + 1).limit(rows.size()).forEach(i -> {
                SqlRow sr = rows.get(i);
                sb.setLength(0);
                try {
                    sb.append(sdf.format(new SimpleDateFormat("yyyyMMdd").parse(sr.getString("report_date")))).append(SPLIT)
                            .append(sr.getString("peoplebank_submitcode")).append(SPLIT)
                            .append(sr.getString("area_code")).append(SPLIT)
                            .append(sr.getString("cust_type")).append(SPLIT)
                            .append(sr.getString("cny")).append(SPLIT)
                            .append(sr.getString(("current_buy_amount"))).append(SPLIT)
                            .append(sr.getString("current_buy_amount_rmb")).append(SPLIT)
                            .append(sr.getString("current_buy_vol")).append(SPLIT)
                            .append(sr.getString("current_redemption_amount")).append(SPLIT)
                            .append(sr.getString("current_redemption_amountrmb")).append(SPLIT)
                            .append(sr.getString("current_redemption_vol")).append(SPLIT)
                            .append(sr.getString("termina_prod_amount")).append(SPLIT)
                            .append(sr.getString("termina_prod_amount_rmb")).append(SPLIT)
                            .append(sr.getString("termina_prod_vol")).append(SPLIT)
                            .append(sr.getString("termina_prod_nav")).append(SPLIT)
                            .append(sr.getString("termina_prod_nav_rmb")).append(SPLIT)
                            .append(sr.getString("termina_prod_nav_add")).append(SPLIT)
                            .append(sr.getString("termina_prod_nav_add_rmb")).append(SPLIT)
                            .append(sr.getString("prod_max_rate")).append(SPLIT)
                            .append(sr.getString("prod_min_rate"));
                } catch (ParseException e) {
                    log.error(e.getMessage());
                }
                p.print(sb.toString());
                if (i != rows.size() - 1) {
                    p.print("\r\n");
                }
            });
            rows.forEach(sr -> {

            });
            p.close();
        } catch (Exception e) {
            log.error("查询失败", e);
            throw new Exception("查询失败");

        }
    }

    /**
     * 产品基本信息
     *
     * @throws UnsupportedEncodingException
     */
    public void generatePIB1(Map<String, Object> params) throws Exception {
        StringBuilder sb = new StringBuilder();
        String root = (String) params.get("root");
        PrintStream p = new PrintStream(new FileOutputStream(root + "/PIB1.dat"), false, "GBK");
        try {
            List<SqlRow> rows = comnDao.findRows(" select '1' data_type,0 t8_prod_base_id,peoplebank_submitcode,orgno,a.prod_variety,a.prod_name,a.PROD_BRAND,a.PROD_TIMES PROD_TIMES,a.prod_code,\n" +
                    "        a.coll_mod,a.oper_mod,a.run_mod,a.PROD_TYPE,a.busi_mod,a.SAFE_RATE,a.SAFE_CAPIT,a.MAX_RATE MAX_RATE,a.MIN_RATE MIN_RATE,a.subs_bdate subs_bdate,a.subs_edate subs_edate,a.TERM_FLAG,a.REDEEM_FLAG,a.PROD_CREDIT_FLAG,\n" +
                    "        a.bord_trusti_code,a.OVERS_TRUSTI_NATION,a.OVERS_TRUSTI_NAME,a.ESTABLISH_DATE ESTABLISH_DATE,a.END_DATE END_DATE,a.pbc_assetscode,entrested_obligation/*受托职责*/,cooperation_mode/*合作模式*/,grading_flag /*产品分级标识*/,\n" +
                    "         transfer_flag ,is_trust,\n" +
                    "         orgno_flag, cash_type ,cross_border_finan\n" +
                    "         FROM  app_rpt_ppi a where a.establish_date between $S{beginDate} and $S{queryDate} order by a.peoplebank_submitcode", params);
            rows.forEach(sr -> {
                sb.setLength(0);
                sb.append(sr.getString("data_type")).append(SPLIT)
                        .append(sr.getString("peoplebank_submitcode")).append(SPLIT)
                        .append(sr.getString("orgno")).append(SPLIT)
                        .append(sr.getString("prod_variety")).append(SPLIT)
                        .append(sr.getString("prod_name")).append(SPLIT)
                        .append(sr.getString("prod_brand")).append(SPLIT)
                        .append(sr.getString("prod_times")).append(SPLIT)
                        .append(sr.getString("prod_code")).append(SPLIT)
                        .append(sr.getString("coll_mod")).append(SPLIT)
                        .append(sr.getString("oper_mod")).append(SPLIT)
                        .append(sr.getString("run_mod")).append(SPLIT)
                        .append(sr.getString("prod_type")).append(SPLIT)
                        .append(sr.getString("busi_mod")).append(SPLIT)
                        .append(sr.getString("safe_rate")).append(SPLIT)
                        .append(sr.getString("safe_capit")).append(SPLIT)
                        .append(sr.getString("max_rate")).append(SPLIT)
                        .append(sr.getString("min_rate")).append(SPLIT)
                        .append(sr.getString("subs_bdate")).append(SPLIT)
                        .append(sr.getString("subs_edate")).append(SPLIT)
                        .append(sr.getString("term_flag")).append(SPLIT)
                        .append(sr.getString("redeem_flag")).append(SPLIT)
                        .append(sr.getString("prod_credit_flag")).append(SPLIT)
                        .append(sr.getString("bord_trusti_code")).append(SPLIT)
                        .append(sr.getString("overs_trusti_nation")).append(SPLIT)
                        .append(sr.getString("overs_trusti_name")).append(SPLIT)
                        .append(sr.getString("establish_date")).append(SPLIT)
                        .append(sr.getString("end_date")).append(SPLIT)
                        .append(sr.getString("pbc_assetscode")).append(SPLIT)
                        .append(sr.getString("entrested_obligation")).append(SPLIT)
                        .append(sr.getString("cooperation_mode")).append(SPLIT)
                        .append(sr.getString("grading_flag")).append(SPLIT)
                        .append(sr.getString("transfer_flag")).append(SPLIT)
                        .append(sr.getString("orgno_flag")).append(SPLIT)
                        .append(sr.getString("cash_type")).append(SPLIT)
                        .append(sr.getString("cross_border_finan")).append(SPLIT)
                        .append(sr.getString("is_trust"));
                p.print(sb.toString());
                p.print("\r\n");
            });
            List<SqlRow> rows1 = comnDao.findRows("select * from (\n" +
                    "\t\tselect '2' date_type, a.prod_code peoplebank_submitcode,'1' cry_type, a.issu_ccy cry_code FROM app_rpt_ppi a where a.establish_date  between $S{beginDate} and $S{queryDate}\n" +
                    "\t\tunion all\n" +
                    "\t\tselect '2' date_type, a.prod_code peoplebank_submitcode,'2' cry_type, a.issu_ccy FROM app_rpt_ppi a where a.establish_date between  $S{beginDate} and $S{queryDate}\n" +
                    "\t\tunion all\n" +
                    "\t\tselect '2' date_type, a.prod_code peoplebank_submitcode,'3' cry_type, a.issu_ccy FROM app_rpt_ppi a where a.establish_date between $S{beginDate} and $S{queryDate}\n" +
                    "\t\t) a order by peoplebank_submitcode asc,cry_type desc", params);
            rows1.forEach(sr1 -> {
                sb.setLength(0);
                sb.append(sr1.getString("date_type")).append(SPLIT)
                        .append(sr1.getString("peoplebank_submitcode")).append(SPLIT)
                        .append(sr1.getString("cry_type")).append(SPLIT)
                        .append(sr1.getString("cry_code"));
                p.print(sb.toString());
                p.print("\r\n");
            });
            List<SqlRow> rows2 = comnDao.findRows("select '3' date_type, peoplebank_submitcode,invest_object\n" +
                    "    FROM app_rpt_ppi a\n" +
                    "    where a.establish_date between $S{beginDate} and $S{queryDate}\n" +
                    "    union all\n" +
                    "    select '4' date_type,peoplebank_submitcode,\n" +
                    "    a.prod_credit_org invest_object FROM app_rpt_ppi a\n" +
                    "    where a.prod_credit_org is not null\n" +
                    "    and  a.establish_date between $S{beginDate} and $S{queryDate}\n" +
                    "    union all\n" +
                    "    select '5' date_type,peoplebank_submitcode,\n" +
                    "    a.prod_credit_mod invest_object\n" +
                    "    FROM app_rpt_ppi a\n" +
                    "    where a.prod_credit_mod is not null\n" +
                    "    and  a.establish_date between $S{beginDate} and $S{queryDate}", params);
            Stream.iterate(0, i -> i + 1).limit(rows2.size()).forEach(i -> {
                SqlRow sr2 = rows2.get(i);
                sb.setLength(0);
                sb.append(sr2.getString("date_type")).append(SPLIT)
                        .append(sr2.getString("peoplebank_submitcode")).append(SPLIT)
                        .append(sr2.getString("invest_object"));
                p.print(sb.toString());
                if (i != rows2.size() - 1) {
                    p.print("\r\n");
                }
            });
            p.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("查询失败");

        }
    }

    /**
     * 产品起始募集信息
     *
     * @param params
     */
    public void generatePIB2(Map<String, Object> params) throws Exception {
        StringBuilder sb = new StringBuilder();
        String root = (String) params.get("root");
        PrintStream p = new PrintStream(new FileOutputStream(root + "/PIB2.dat"), false, "GBK");
        try {
            List<SqlRow> rows = comnDao.findRows("select id,report_date,peoplebank_submitcode,prod_code,area_code,cust_type,cny,init_amount init_amount, init_amount_rmb init_amount_rmb,init_vol init_vol from app_rpt_pib where report_date between $S{beginDate} and $S{queryDate}", params);
            Stream.iterate(0, i -> i + 1).limit(rows.size()).forEach(i -> {
                SqlRow sr = rows.get(i);
                sb.setLength(0);
                sb.append(sr.getString("peoplebank_submitcode")).append(SPLIT)
                        .append(sr.getString("area_code")).append(SPLIT)
                        .append(sr.getString("cust_type")).append(SPLIT)
                        .append(sr.getString("cny")).append(SPLIT)
                        .append(sr.getString("init_amount")).append(SPLIT)
                        .append(sr.getString("init_amount_rmb")).append(SPLIT)
                        .append(sr.getString("init_vol"));
                p.print(sb.toString());
                if (i != rows.size() - 1) {
                    p.print("\r\n");
                }
            });
            p.close();
        } catch (Exception e) {
            log.error("查询失败", e);
            throw new Exception("查询失败");

        }
    }

    /**
     * 产品终止信息报送
     *
     * @param params
     * @throws Exception
     * @throws FileNotFoundException
     * @throws ParseException
     * @throws UnsupportedEncodingException
     */
    public void generatePIE1(Map<String, Object> params) throws Exception, FileNotFoundException, ParseException, UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        String root = (String) params.get("root");
        PrintStream p = new PrintStream(new FileOutputStream(root + "/PIE1.dat"), false, "GBK");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            List<SqlRow> rows = comnDao.findRows("select id,prod_code,pbc_assetscode,peoplebank_submitcode,end_date_real, cny, org_ern org_ern,org_ern_rmb org_ern_rmb, cust_ern cust_ern,cust_ern_rmb cust_ern_rmb,cust_ern_yld cust_ern_yld from app_rpt_pie where end_date_real between $S{beginDate} and $S{queryDate} order by prod_code,cny", params);
            Stream.iterate(0, i -> i + 1).limit(rows.size()).forEach(i -> {
                SqlRow sr = rows.get(i);
                sb.setLength(0);
                try {
                    sb.append(sr.getString("peoplebank_submitcode")).append(SPLIT)
                            .append(sdf.format(new SimpleDateFormat("yyyyMMdd").parse(sr.getString("end_date_real")))).append(SPLIT)
                            .append(sr.getString("cny")).append(SPLIT)
                            .append(sr.getString("org_ern")).append(SPLIT)
                            .append(sr.getString("org_ern_rmb")).append(SPLIT)
                            .append(sr.getString("cust_ern")).append(SPLIT)
                            .append(sr.getString("cust_ern_rmb")).append(SPLIT)
                            .append(sr.getString("cust_ern_yld"));
                } catch (ParseException e) {
                    log.error("", e);
                }
                p.print(sb.toString());
                if (i != rows.size() - 1) {
                    p.print("\r\n");
                }
            });
            p.close();
        } catch (Exception e) {
            log.error("查询失败", e);
            throw new Exception("查询失败");

        }
    }


    /**
     * 资产池基本信息报表文件PPB1
     *
     * @throws Exception
     **/
    public void generatePPB1(Map<String, Object> params) throws Exception {
        String exeid = "M84PPIEQ002";
        StringBuilder sb = new StringBuilder();
        String root = (String) params.get("root");
        PrintStream p = new PrintStream(new FileOutputStream(root + "/PPB1.dat"), false, "GBK");
        try {
            List<SqlRow> rows = comnDao.findRows("select a.pbc_assetscode,a.orgno,a.prod_name,a.establish_date establish_date FROM app_rpt_ppi a  \n" +
                    "     where a.establish_date  between $S{beginDate} and $S{queryDate}  and  a.pbc_assetscode is not null order by a.pbc_assetscode", params);
            Stream.iterate(0, i -> i + 1).limit(rows.size()).forEach(i -> {
                SqlRow sr = rows.get(i);
                sb.setLength(0);
                sb.append(sr.getString("pbc_assetscode")).append(SPLIT)
                        .append(sr.getString("orgno")).append(SPLIT)
                        .append(sr.getString("prod_name")).append(SPLIT)
                        .append(sr.getString("establish_date"));
                p.print(sb.toString());
                if (i != rows.size() - 1) {
                    p.print("\r\n");
                }
            });
            p.close();
        } catch (Exception e) {
            log.error("查询失败:", e);
            throw new Exception("查询失败");

        }
    }


    /**
     * 资产池终止信息
     *
     * @param params
     * @throws Exception
     * @throws FileNotFoundException
     * @throws ParseException
     * @throws UnsupportedEncodingException
     */
    public void generatePPE1(Map<String, Object> params) throws Exception, FileNotFoundException, ParseException, UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        String root = (String) params.get("root");
        PrintStream p = new PrintStream(new FileOutputStream(root + "/PPE1.dat"), false, "GBK");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            List<SqlRow> rows = comnDao.findRows("select distinct pbc_assetscode,end_date_real from app_rpt_pie where end_date_real between $S{beginDate} and $S{queryDate}", params);
            Stream.iterate(0, i -> i + 1).limit(rows.size()).forEach(i -> {
                SqlRow sr = rows.get(i);
                sb.setLength(0);
                try {
                    sb.append(sr.getString("pbc_assetscode")).append(SPLIT)
                            .append(sdf.format(new SimpleDateFormat("yyyyMMdd").parse(sr.getString("end_date_real"))));
                } catch (ParseException e) {
                    log.error(e.getMessage());
                }
                p.print(sb.toString());
                if (i != rows.size() - 1) {
                    p.print("\r\n");
                }
            });
            p.close();
        } catch (Exception e) {
            log.error("查询失败!", e);
            throw new Exception("查询失败");

        }
    }

    public void generatePVD1(Map<String, Object> params) throws Exception {
        StringBuilder sb = new StringBuilder();
        String root = (String) params.get("root");
        PrintStream p = new PrintStream(new FileOutputStream(root + "/PVD1.dat"), false, "GBK");
        try {
            List<SqlRow> rows = comnDao.findRows("select report_date report_date,prod_code,pbc_assetscode,data_type,cny,end_amount end_amount,\n" +
                    "\t\t  end_amount_rmb end_amount_rmb from app_rpt_pvd\n" +
                    "\t\t where report_date = $S{reportDate}\n" +
                    "\t\t\tand end_amount<>0\n" +
                    "\t\torder by prod_code,data_type", params);
            Stream.iterate(0, i -> i + 1).limit(rows.size()).forEach(i -> {
                SqlRow sr = rows.get(i);
                sb.setLength(0);
                sb.append(sr.getString("report_date")).append(SPLIT)
                        .append(sr.getString("pbc_assetscode")).append(SPLIT)
                        .append(sr.getString("data_type")).append(SPLIT)
                        .append(sr.getString("cny")).append(SPLIT)
                        .append(sr.getString("end_amount")).append(SPLIT)
                        .append(sr.getString(("end_amount_rmb")));
                p.print(sb.toString());
                if (i != rows.size() - 1) {
                    p.print("\r\n");
                }
            });
            p.close();
        } catch (Exception e) {
            log.error("查询失败,", e);
            throw new Exception("查询失败");

        }
    }

    public void generatePVD3(Map<String, Object> params) throws Exception {
        StringBuilder sb = new StringBuilder();
        String root = (String) params.get("root");
        PrintStream p = new PrintStream(new FileOutputStream(root + "/PVD3.dat"), false, "GBK");
        try {
            List<SqlRow> rows = comnDao.findRows("select id,report_date report_date,prod_code,stock_type,pbc_assetscode,cny,orgno,product_code,prod_amount prod_amount,prod_amount_rmb prod_amount_rmb from\n" +
                    "\t\tapp_rpt_pvd3 where report_date LIKE CONCAT(SUBSTR($S{reportDate},1,6),'%')\n" +
                    "\t\t and (prod_code like '%$U{prodCode}%') order by pbc_assetscode", params);
            Stream.iterate(0, i -> i + 1).limit(rows.size()).forEach(i -> {
                SqlRow sr = rows.get(i);
                sb.setLength(0);
                sb.append(sr.getString("report_date")).append(SPLIT)
                        .append(sr.getString("pbc_assetscode")).append(SPLIT)
                        .append(sr.getString("stock_type")).append(SPLIT)
                        .append(sr.getString("orgno")).append(SPLIT)
                        .append(sr.getString("product_code")).append(SPLIT)
                        .append(sr.getString("cny")).append(SPLIT)
                        .append(sr.getString("prod_amount")).append(SPLIT)
                        .append(sr.getString("prod_amount_rmb"));
                p.print(sb.toString());
                if (i != rows.size() - 1) {
                    p.print("\r\n");
                }
            });
            p.close();
        } catch (Exception e) {
            log.error("查询失败,", e);
            throw new Exception("查询失败");

        }
    }
}
