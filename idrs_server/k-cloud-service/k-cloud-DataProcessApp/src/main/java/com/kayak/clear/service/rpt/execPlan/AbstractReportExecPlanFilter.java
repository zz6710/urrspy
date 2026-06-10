package com.kayak.clear.service.rpt.execPlan;

import com.kayak.base.dao.ComnDao;
import com.kayak.clear.chain.Filter;
import com.kayak.clear.chain.FilterChain;
import com.kayak.clear.req.ReportTimeExecPlanInput;
import com.kayak.clear.req.ReportTimeExecPlanOutput;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 抽象报表执行计划过滤器
 */
public abstract class AbstractReportExecPlanFilter implements Filter<ReportTimeExecPlanInput, List<ReportTimeExecPlanOutput>> {

    @Override
    public void doFilter(ReportTimeExecPlanInput request, List<ReportTimeExecPlanOutput> response, FilterChain<ReportTimeExecPlanInput, List<ReportTimeExecPlanOutput>> chain) throws Exception{
        if(baseType() != null && baseType().equals(request.getBaseType())){
            String baseLineDate = "";
            baseLineDate = getBaseLineDate(request);
            if(baseLineDate != null && baseLineDate.length() == 8){
                ReportTimeExecPlanOutput output = getOutput(request, baseLineDate);
                if(output != null){
                    response.add(output);
                }
            }
        }
    }

    /**
     * 基准日期类型
     * @return
     */
    public abstract String baseType();

    /**
     * 获取基准日期（格式为：yyyyMMdd）
     * @param req 请求对象
     * @return
     */
    public abstract String getBaseLineDate(ReportTimeExecPlanInput req) throws Exception;

    /**
     * 获取响应数据
     * @param req 日期类型  ReportTimeExecPlanInput 输入对象
     * @return
     */
    public ReportTimeExecPlanOutput getOutput(ReportTimeExecPlanInput req, String baseLineDate) throws Exception{
        ReportTimeExecPlanOutput result = null;
        String strCompareDate = ""; //进行比较的日期
        String strStartDate = "";
        String strEndDate = "";
        int offset = req.getDataGenerTimeRequire(); //报送数据生成日期，偏移量
        int subOffset = req.getSuperviseSubmissionTime(); //监管要求的报送日期，偏移量
        if("01".equals(req.getDataType())){ //01 工作日
            strCompareDate = getWorkDayAfterDay(baseLineDate, offset);
            strStartDate = getAfterDay(baseLineDate,req.getInnerSubmissionTimeRequire());
            strEndDate = getWorkDayAfterDay(req.getWorkDate(), subOffset);
        }else if( "02".equals(req.getDataType())) { //02 自然日
            strCompareDate = getAfterDay(baseLineDate, offset);
            strStartDate = getAfterDay(baseLineDate,req.getInnerSubmissionTimeRequire());
            strEndDate = getAfterDay(baseLineDate, subOffset);
        }else{
            throw new Exception("无法处理日期类型 "  + offset);
        }

        boolean checkResult = false; //校验结果
        if("10".equals(req.getBaseType()) || "99".equals(req.getBaseType())){
            checkResult = true;
        }else{
            checkResult = strCompareDate.equals(req.getWorkDate()); //均跟工作日进行比对
        }

        if(checkResult && "1".equals(req.getTimeType())){ //当日期校验通过，并且为非规则配置时，使用配置的结束日期
            strEndDate = getTimeType1EndDate(req);
        }

        if(checkResult){ //当校验通过时，包装响应对象
            result = new ReportTimeExecPlanOutput();
            result.setReportType(req.getReportType());
            result.setReportTable(req.getReportTable());
            result.setReportTableName(req.getReportTableName());
            result.setExecStatus("0");
            result.setBaseLineDate(baseLineDate);
            result.setWorkDate(req.getWorkDate());
            result.setStartDate(strStartDate);
            result.setEndDate(strEndDate);
        }
        return result;
    }

    /**
     * 获取非规则配置的结束日期
     * @param req
     * @return
     */
    private String getTimeType1EndDate(ReportTimeExecPlanInput req) throws Exception{
        String sql = "select end_date from report_time_type_info where report_table = $S{reportTable} and end_date > $S{workDate} order by end_date limit 1";
        SqlRow row = comnDao.findRow(sql, req);
        return row.getString("end_date");
    }

    @Autowired
    protected ComnDao comnDao;

    /**
     * 获取工作日后的offset的工作日
     * @param date
     * @param offset
     * @return
     */
    public String getWorkDayAfterDay(String date, int offset) throws Exception{
        String strSql = "";
        if(offset == 0){
            return date;
        }else if(offset > 0){
            strSql = "select max(workday) workday from (select workday from sys_workday_set t where pgmno = '001' and workday  > '"+date+"'  limit "+String.valueOf(offset)+") tb1";
        }else{
            strSql = "select min(workday) workday from (select workday from sys_workday_set t where pgmno = '001' and workday  < '"+date+"'  order by workday desc limit "+String.valueOf(-offset)+") tb1";
        }
        SqlRow row = comnDao.findRow(strSql, null);
        return row.getString("workday");
    }

    /**
     * 自然日期偏移量后的日期
     * @param date
     * @param offset
     * @return
     * @throws ParseException
     */
    public String getAfterDay(String date,int offset) throws ParseException {
        if(offset == 0){
            return date;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date d = dateFormat.parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.DAY_OF_MONTH,offset);
        return dateFormat.format(cal.getTime());
    }

    /**
     * 获取上一个月最后一个自然日
     * @param date
     * @return
     * @throws Exception
     */
    public String getLastMonthEndDay(String date) throws Exception{
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date d = dateFormat.parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.MONTH, -1);
        String strLastMonthDay = dateFormat.format(cal.getTime()); //上一个月的这一天
        return DateUtil.getLastDayOfMonth(strLastMonthDay); //获取上一个月的最后一天
    }

    /**
     * 向前推日期
     * @param req
     * @return
     */
    public String getPreDate(ReportTimeExecPlanInput req) throws Exception{
        int idx = req.getDataGenerTimeRequire(); //数据生成日期的偏移量
        if("01".equals(req.getDataType())){ //01 工作日
            return getWorkDayAfterDay(req.getWorkDate(), -idx);
        }else if("02".equals(req.getDataType())){ // 02 自然日
            return getAfterDay(req.getWorkDate(), -idx);
        }else{
            throw new Exception("无法处理日期类型 "  + req.getDataType());
        }
    }

    @Override
    public void reset() {

    }
}
