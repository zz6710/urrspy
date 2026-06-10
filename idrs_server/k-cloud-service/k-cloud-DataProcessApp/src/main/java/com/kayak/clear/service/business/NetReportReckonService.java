package com.kayak.clear.service.business;

import com.kayak.base.dao.util.DaoUtil;
import com.kayak.clear.req.PubReq;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.DateUtil;
import com.kayakwise.kcloud.batch.annotation.StepNo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@Scope("prototype")
public class NetReportReckonService extends BusinessBaseTaskService{

    private String friDay ; //上周五
    private String lastWorkDay ; //上个工作日
    private String latestTwoWorkDay ; //上两个工作日
    private Map<String , Object> configDays ; //上个确认日
    Map<String , Object> params = new HashMap<>();
    private List<Map<String,Object>> data = new ArrayList<>();;//组装数据

    @Override
    protected void doCheckBusiness(PubReq request) throws Exception {
        log.info(" ###### 初始化业务数据，并业务校验开始");
        try {
            // 此任务是零点线之后触发，所以workDate不在等于任务日期，
            // 而是要取下一个工作日。从而生成以当前任务日期为净值日期的净值规则
            workDate = DateUtil.getNextSysWordDay(workDate);//报表报送的工作日期

            //初始化报送估值日（上个工作日(T日估值)、上两个工作日(T+1日估值)、确认日（上一个））
            //friDay = DateUtil.getLastWeekMonday(workDate , Calendar.FRIDAY);//上周五
            lastWorkDay = DateUtil.getLastSysWordDay(workDate);//上个工作日：报送的业务数据工作日期
            latestTwoWorkDay = DateUtil.getLastSysWordDay(lastWorkDay);//上两个工作日，数据业务日期的前一个工作日
            configDays = getNextConfirmDay(workDate);//根据产品日历获取产品下一个最近的申赎确认日
        }catch (Exception e){
            log.error("初始化参数失败！" + e.getMessage());
        }
        log.info(" ###### 初始化业务数据，并业务校验结束");

    }


    //每日报送
    @StepNo(stepNo = 1)
    protected void stepProcess1(PubReq request) throws Exception{
        log.info(" ###### 每日报送推算开始 ");
        params.put("reportRules","1");
        params.put("workDate",workDate);
        params.put("lastWorkDay",lastWorkDay);
        List<SqlRow> dailyReport = findProdReport(params);
        if(dailyReport.size()==0){
            log.info(" ###### 未查询到相关规则信息 ");
            return;
        }
        for (SqlRow s : dailyReport){
            //判断当前工作日是否为确认日,即上一个工作日是否为基准日
            params.put("prod_cd",s.getString("prod_cd"));

            String is_cnf_date = checkIsBaseDay(params);
            //如果是否基准日不满足当前产品在报送日期的状态则跳过
            if(!"01".equals(s.getString("mode_cycle")) && !s.getString("is_base_day").equals(is_cnf_date)){
                continue;
            }

            //如果产品在前一个工作日(即数据业务日期)存在申赎数据确认时,则跳过
            Integer delaCount = checkExistDeal(params).get(0).getInteger("count");
            if(delaCount>0){
                continue;
            }

            addListData(s);
        }
        log.info(" ###### 每日报送推算结束 ");
    }

    //保存数据
    @StepNo(stepNo = 2)
    protected void stepProcess4(PubReq request) throws Exception{
        log.info(" ######  数据存储开始 ");
       if(data.size()==0){
           log.info(" ######  未查询到相关数据 ");
           return;
       }
        String date = request.getTaskDate();
        String time = DateUtil.getNowTime();
        //非工作日顺延
        String nextWorkDay = DateUtil.getNextSysWordDay(date);
        String delSql = "delete from dws_prd_prd_nav_isu_inf where isu_dt = $S{nextWorkDay} ";
        String addSql = "replace into dws_prd_prd_nav_isu_inf (prod_cd,nav_dt,isu_dt,crt_dt,crt_tm,upd_dt,upd_tm) " +
                "values ($S{prod_cd},$S{nav_dt},$S{isu_dt},$S{crt_dt},$S{crt_tm},$S{upd_dt},$S{upd_tm})";
        DaoUtil.doTrans(() -> {
            comnDao.update(delSql , nextWorkDay);
            for (Map<String ,Object> m : data) {
                m.put("crt_dt",date);
                m.put("crt_tm",time);
                comnDao.update(addSql , m);
            }
        });
        log.info(" ######  数据存储结束 ");

    }

    //同确认日报送(无此情况)
    /*@StepNo(stepNo = 2)
    protected void stepProcess2(PubReq request) throws Exception{
        log.info(" ###### 同确认日报送推算开始 ");
        params.put("reportRules","2");
        params.put("workDate",workDate);
        List<SqlRow> confirmReport = findProdReport(params);
        if(confirmReport.size()==0){
            log.info(" ###### 未查询到相关规则信息 ");
            return;
        }
        //查找当日为确认日的产品
        for (SqlRow s : confirmReport) {
            //判断确认日是否有申购赎回数据(正常情况下会一直存在，所以此方法直接会跳出)
            params.put("date",navDate(s.getString("report_date") , s.getString("prod_cd")));
            params.put("prod_cd",s.getString("prod_cd"));
            Integer delaCount = checkExistDeal(params).get(0).getInteger("count");
            if(delaCount>0){
                continue;
            }
            //月末是否报送(判断当前工作日是否为该月的第一个工作日，如果是则生成上个月末的报送信息)
            if("01".equals(s.getString("report_month")) && DateUtil.isFirstWordDay(workDate)){
                //月末是否有申购赎回数据
                params.put("date",DateUtil.geMonthEndDay(workDate));
                Integer lastDelaCount = checkExistDeal(params).get(0).getInteger("count");
                if(lastDelaCount>0){
                    continue;
                }
                addListDataMonth(s);
            }
            //如果确认日等于月末则跳出
            if((DateUtil.geMonthEndDay(workDate).equals(navDate(s.getString("report_date") , s.getString("prod_cd")))&&"01".equals(s.getString("report_month")))){
                continue;
            }
            //如果当前系统工作日等于确认日的下一个工作日
            if (workDate.equals(DateUtil.getNextSysWordDay(configDays.get(s.getString("prod_cd")).toString())))
                addListData(s);
        }
        log.info(" ###### 同确认日报送推算结束 ");

    }*/

    //固定频率报送
    /*@StepNo(stepNo = 3)
    protected void stepProcess3(PubReq request) throws Exception{
        log.info(" ###### 固定频率报送推算开始 ");
        params.put("reportRules","3");
        params.put("workDate",workDate);
        List<SqlRow> frequencyReport = findProdReport(params);
        if(frequencyReport.size()==0){
            log.info(" ###### 未查询到相关规则信息 ");
            return;
        }
        for (SqlRow s : frequencyReport) {
            //根据不同报送估值日查询是否存在申购赎回数据
            params.put("date",navDate(s.getString("report_date") , s.getString("prod_cd")));
            params.put("prod_cd",s.getString("prod_cd"));
            Integer delaCount = checkExistDeal(params).get(0).getInteger("count");
            if(delaCount>0){
                continue;
            }
            //月末是否报送(判断当前工作日是否为该月的第一个工作日，如果是则生成上个月末的报送信息)
            if("01".equals(s.getString("report_month")) && DateUtil.isFirstWordDay(workDate)){
                //月末是否有申购赎回数据
                params.put("date",DateUtil.geMonthEndDay(workDate));
                Integer lastDelaCount = checkExistDeal(params).get(0).getInteger("count");
                if(lastDelaCount>0){
                    continue;
                }
                addListDataMonth(s);
            }
            if (!DateUtil.isWordDay(workDate))
                continue;
            //如果报送估值日为周五,要是上周五等于月末则跳出
            //如果报送估值日为上一工作日,要是上一工作日等于月末则跳出
            //如果报送估值日为确认日,要是确认日等于月末则跳出
            if((DateUtil.geMonthEndDay(workDate).equals(navDate(s.getString("report_date") , s.getString("prod_cd")))&&"01".equals(s.getString("report_month")))){
                continue;
            }
            String lastReportDate = s.getString("found_dt");//成立日默认为初始推算日

            String lengthFreq = s.getString("length_freq");//频率
            String reportFreq = s.getString("report_freq");//月还是周
            String specificDate = s.getString("specific_date");//具体日期

            if ("1".equals(reportFreq)){//周报送
                List<String> dates = DateUtil.getDateListForWeek(lastReportDate,workDate,lengthFreq,specificDate);
                calculateDate(s, dates);
                continue;
            }

            if ("2".equals(reportFreq)){//月报送
                List<String> dates = DateUtil.getDateListForMonth(lastReportDate,workDate,lengthFreq,specificDate);
                calculateDate(s, dates);
            }

        }

        log.info(" ###### 固定频率报送推算结束 ");
    }*/

    private void calculateDate(SqlRow s, List<String> dates) throws Exception {

        if (dates.size() == 0) return;

        Collections.sort(dates);
        String maxDate = dates.get(dates.size()-1);
        if (workDate.equals(maxDate) /*|| workDate.equals(DateUtil.getNextSysWordDay(maxDate))*/)//如果推算出来的最大日期为今日或者下个工作日为今日
            addListData(s);
    }

    /**
     * 获取需要报送产品净值日期数据
     * @param s
     * @throws Exception
     */
    void addListData(SqlRow s) throws Exception {
        Map<String,Object> m = new HashMap<>();
        m.put("prod_cd" , s.getString("prod_cd"));
        m.put("isu_dt" , DateUtil.getNextWordDay(workDate));//发布日,即报送日期
        m.put("nav_dt" , navDate(s.getString("report_date") , s.getString("prod_cd")));//净值日期
        data.add(m);
    }

    /*void addListDataMonth(SqlRow s) throws Exception {
        Map<String,Object> m = new HashMap<>();
        String prodCode = s.getString("prod_cd");
        m.put("prod_cd" , prodCode);
        m.put("isu_dt" , DateUtil.getNextWordDay(workDate));
        m.put("nav_dt" , DateUtil.geMonthEndDay(workDate));
        data.add(m);
    }*/

    //推断净值日期
    String navDate(String reportDate , String prodCode) throws Exception {

        if("1".equals(reportDate))//上周五
            return friDay;

        if("2".equals(reportDate))//上一个工作日(T-1)
            return lastWorkDay;

        if("3".equals(reportDate))//确认日
            return (String)configDays.get(prodCode);

        if("4".equals(reportDate))//上两个工作日(T-2)
            return latestTwoWorkDay;

        throw new Exception("不支持该报送估值日选择");
    }

    /**
     * 获取需要报送的产品列表相关信息
     * @param params
     * @return
     * @throws Exception
     */
    public List<SqlRow> findProdReport(Map<String,Object> params) throws Exception {
        String sql =
                "select t1.prod_cd,t1.found_dt, CONCAT(t2.operation_mode,t2.regular_open_cycle) mode_cycle," +
                "      t2.report_rules,t2.report_freq,t2.length_freq,t2.specific_date,t2.report_date,t2.report_month,t2.report_confirm_date,t2.is_base_day  " +
                " from dwd_prd_prd_bas_inf t1 " +
                "inner join dwd_prd_prd_spvs_inf t3 on t1.prod_cd = t3.prod_cd " +
                "inner join idb_net_report_rules t2 " +
                "   on t1.prod_mod = t2.operation_mode and t2.report_rules = $S{reportRules} and ifnull(t3.RUL_OPN_PRD,'') = ifnull(t2.regular_open_cycle,'')" +
                "where t1.FOUND_DT < $S{workDate} " +
                "  and t1.MTU_DT >= $S{workDate} " +
                "  and (t1.PROD_STS NOT IN ('0','2','3','4','7') or (t1.PROD_STS  = '7' and t1.MTU_DT = $S{workDate}))" ;
        return comnDao.findRows(sql,params);
    }

    /**
     * 根据产品日历获取产品下一个最近的申赎确认日
     * @param date
     * @return
     * @throws Exception
     */
    public Map<String , Object> getNextConfirmDay(String date) throws Exception {
        String sql = "select min(DT_STR) as cfm_dt, prd_cd from DWD_PTY_PROD_CAL where DT_STR > $S{date} and (IS_BUT_CNF_DT = '1' or IS_RDM_CNF_DT = '1') group by prd_cd ";
        List<SqlRow> l = comnDao.findRows(sql , date);
        Map<String ,Object> m = new HashMap<>();
        for (SqlRow s : l) {
            m.put(s.getString("prod_cd") , s.getString("cfm_dt"));
        }
        return m;
    }

    /**
     * 检查产品在当前工作日期是否为确认日
     * @param params
     * @return
     * @throws Exception
     */
    public String checkIsBaseDay(Map<String,Object> params) throws Exception {
        String sql = "select (case when count(1) > 0 then '01' else '02' end) as is_base_day " +
                "       from DWD_PTY_PROD_CAL where (IS_BUT_CNF_DT = '1' or IS_RDM_CNF_DT = '1') and PRD_CD = $S{prod_cd} and DT_STR = $S{workDate} ";
        return comnDao.findRow(sql, params).getString("is_base_day");
    }

    /**
     * 查询产品在数据业务日期lastWorkDay 是否存在申赎确认数据
     * @param params
     * @return
     * @throws Exception
     */
    public List<SqlRow> checkExistDeal(Map<String,Object> params) throws Exception {
        String sql = "select count(1) count from DWD_EVT_PRD_SAL_DTL_SMR where REAL_CFM_DT = $S{lastWorkDay} and PROD_CD = $S{prod_cd}";
        return comnDao.findRows(sql, params);
    }

}
