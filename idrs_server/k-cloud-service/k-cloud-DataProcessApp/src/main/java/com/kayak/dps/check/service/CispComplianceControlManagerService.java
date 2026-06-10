package com.kayak.dps.check.service;

import com.kayak.base.dao.ComnDao;
import com.kayak.clear.req.CispComplianceInfo;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class CispComplianceControlManagerService {

    private static Logger log = LoggerFactory.getLogger(CispComplianceControlManagerService.class);

    @Autowired
    private ComnDao comnDao;

    /**
     * 获取发给Cisp的合规内控监测实体对象
     * @param workDate 工作日
     * @return
     * @throws Exception
     */
    public CispComplianceInfo getCispComplianceInfo(String workDate) throws Exception{
        CispComplianceInfo result = null;
        String lastMonthEndDay = getLastMonthDayEnd(workDate); //获取上个月最后一个自然日
        String minStartDate = getWorkDayAfterDay(lastMonthEndDay, 1); //获取本月查询数据的最小工作日【因为月初第一个工作日出报表，只有大于该日期才进行查询，否则返回0】
        if(Integer.parseInt(workDate) > Integer.parseInt(minStartDate)){
            result = queryCispComplianceInfo(workDate);
        }else{
            result = new CispComplianceInfo();
            result.setBaseLineDate(lastMonthEndDay);
            result.setWorkDate(workDate);
            result.setColumn01("0");
            result.setColumn02("0");
            result.setColumn03("0");
            result.setColumn04("0");
            result.setColumn05("0");
            result.setColumn06("0");
            result.setColumn07("0");
        }
        return result;
    }

    /**
     * 查询数据库获取合规内控监测数据
     * @param workDate 工作日
     * @return
     * @throws Exception
     */
    protected CispComplianceInfo queryCispComplianceInfo(String workDate) throws Exception{
        CispComplianceInfo result = new CispComplianceInfo();
        result.setBaseLineDate(getLastMonthDayEnd(workDate));
        result.setWorkDate(workDate);
        result.setColumn01(getColumn1(workDate));
        result.setColumn02(getColumn2(workDate));
        result.setColumn03(getColumn3(workDate));
        result.setColumn04(getColumn4(workDate));
        result.setColumn05(getColumn5(workDate));
        result.setColumn06(getColumn6(workDate));
        result.setColumn07(getColumn7(workDate));
        return result;
    }

    /**
     * 删除合规内控监测数据
     * @param cisp
     * @return
     * @throws Exception
     */
    public int delCispComplianceInfo(CispComplianceInfo cisp) throws Exception{
        String strSql = "delete from app_send_cisp_compliance_control_info where work_date = $S{workDate}";
        UpdateResult res = comnDao.update(strSql, cisp);
        return res.getEffect();
    }

    /**
     * 添加合规内控监测数据
     * @param cisp cisp对象
     * @return
     * @throws Exception
     */
    public int addCispComplianceInfo(CispComplianceInfo cisp) throws Exception{
        String strSql = "insert into app_send_cisp_compliance_control_info(column01,column02,column03,column04,column05,column06,column07,base_line_date,work_date) values($S{column01}, $S{column02}, $S{column03}, $S{column04}, $S{column05}, $S{column06}, $S{column07}, $S{baseLineDate}, $S{workDate})";
        UpdateResult res = comnDao.update(strSql, cisp);
        return res.getEffect();
    }

    /**
     * 指标编码：SYDF001
     * 分子：G06a01所有理财产品本期客户端实现收益总额期末余额
     * 汇总区间：统计月份（含）向前三年
     * 数据时点：每月末
     * @param workDate
     * @return
     * @throws Exception
     */
    public String getColumn1(String workDate) throws Exception{
        String result = "0";
        String strInSql = getPreviousMonthLastDaySqlInList(workDate, -36);
        String strSql = "select replace(format(sum(ifnull(data_value,0)),2),',','') data_value from app_asset_nkhg_data where data_key = 'G06a01-1' and report_date " + strInSql;
        SqlRow row = comnDao.findRow(strSql,null);
        if(row != null){
            result = row.getString("data_value");
        }
        return result;
    }

    /**
     * 指标：SYDF002
     * 分子：G06a01固定收益类理财产品本期客户端实现收益总额期末余额
     * 汇总区间：统计月份（含）向前三年
     * 数据时点：每月末
     * @param workDate
     * @return
     * @throws Exception
     */
    public String getColumn2(String workDate) throws Exception{
        String result = "0";
        String strInSql = getPreviousMonthLastDaySqlInList(workDate, -36);
        String strSql = "select replace(format(sum(ifnull(data_value,0)),2),',','') data_value from app_asset_nkhg_data where data_key = 'G06a01-2' and report_date " + strInSql;
        SqlRow row = comnDao.findRow(strSql,null);
        if(row != null){
            result = row.getString("data_value");
        }
        return result;
    }

    /**
     * 指标：SYDF003
     * 分子：G06a01权益类和混合类理财产品本期客户端实现收益总额期末余额
     * 汇总区间：统计月份（含）向前三年
     * 数据时点：每月末
     * @param workDate
     * @return
     * @throws Exception
     */
    public String getColumn3(String workDate) throws Exception{
        String result = "0";
        String strInSql = getPreviousMonthLastDaySqlInList(workDate, -36);
        String strSql = "select replace(format(sum(ifnull(data_value,0)),2),',','') data_value from app_asset_nkhg_data where data_key in ('G06a01-3', 'G06a01-4') and report_date " + strInSql;
        SqlRow row = comnDao.findRow(strSql,null);
        if(row != null){
            result = row.getString("data_value");
        }
        return result;
    }

    /**
     * 指标：CPJG005
     * 分子： G06a01所有理财产品期末余额
     * 汇总区间：统计月份（含）向前12个月
     * 数据时点：每月末
     * @param workDate
     * @return
     * @throws Exception
     */
    public String getColumn4(String workDate) throws Exception{
        String result = "0";
        BigDecimal sumValue = new BigDecimal("0");
        List<String> list = getPreviousMonthLastDayList(workDate, -12); //向前12个月的日期列表
        for(String date: list){
            BigDecimal g06a01 = getG06a01(date);
            BigDecimal g06a02 = getG06a02_1_13_1_14(date);
            int compare1 = g06a01.compareTo(BigDecimal.ZERO);
            int compare2 = g06a02.compareTo(BigDecimal.ZERO);
            if(compare1 != 0  && compare2 != 0){
                BigDecimal div = g06a02.divide(g06a01,2,RoundingMode.HALF_UP); //四舍五入，保留2位小数
                sumValue = sumValue.add(div);
            }
        }
        return sumValue.toString();
    }

    /**
     * G06a02  “1.15 合计”-“期末余额（穿透后）
     * @param workDate
     * @return
     */
    private BigDecimal getG06a01(String workDate) throws Exception{
        BigDecimal result = null;
        String strSql = "select replace(ifnull(data_value,0),',','') data_value from app_asset_nkhg_data where data_key = 'G06a02-2' and report_date ='" + workDate+"'";
        SqlRow row = comnDao.findRow(strSql,null);
        if(row != null){
            result = new BigDecimal(row.getString("data_value"));
        }else{
            result = new BigDecimal("0");
        }
        return result;
    }

    /**
     * G06a02 “1.13.7 合计”-“期末余额（穿透前）
     * 穿透前期末余额
     * @param workDate
     * @return
     * @throws Exception
     */
    private BigDecimal getG06a02_1_13_1_14(String workDate) throws Exception{
        BigDecimal result = null;
        String strSql = "select replace(ifnull(data_value,0),',','') data_value from app_asset_nkhg_data where data_key = 'G06a02-1' and report_date = '"+workDate+"' ";
        SqlRow row = comnDao.findRow(strSql,null);
        if(row != null){
            result = new BigDecimal(row.getString("data_value"));
        }else{
            result = new BigDecimal("0");
        }
        return result;
    }

    /**
     * 指标：FXZB001
     * 分子 年末净资本：G06b_I中“8.净资本”-“应计算金额”期末数
     * 风险资本：G06b_II“风险资本期末余额（列）”-“各项风险资本合计”数
     * 数据时点：每季度末
     * 供数频率：每月
     * @param workDate
     * @return
     * @throws Exception
     */
    public String getColumn5(String workDate) throws Exception{
        String result = "0";
        String strLastMonthEndDay = getLastMonthDayEnd(workDate); //获取上月最后一个自然日
        String month = strLastMonthEndDay.substring(4,6);
        if("03".equals(month) || "06".equals(month) || "09".equals(month) || "12".equals(month)){
            BigDecimal g06b1 = getG06b01_row18_column05(strLastMonthEndDay);
            BigDecimal g06b2 = getG06b02_row48_column05(strLastMonthEndDay);
            result = g06b1.subtract(g06b2).toString(); //取差值
        }
        return result;
    }

    /**
     * 指标：XYFXGL001
     * 分母：G06a02  “1.15 合计”-“期末余额（穿透后）
     * 数据时点：每月末
     * @param workDate
     * @return
     * @throws Exception
     */
   public String getColumn6(String workDate) throws Exception{
       String result = "0";
       String strLastMonthEndDay = getLastMonthDayEnd(workDate); //获取上月最后一个自然日
       String strSql = "select replace(ifnull(data_value,0),',','') data_value from app_asset_nkhg_data where data_key = 'G06a02-2' and report_date = '"+strLastMonthEndDay+"'";
       SqlRow row = comnDao.findRow(strSql,null);
       if(row != null){
           String tmpValue = row.getString("data_value");
           if(tmpValue != null && tmpValue.length() > 0){
               result= tmpValue;
           }
       }
       return result;
   }

    /**
     * 指标：LDXFXGL001
     * 分母：G06a02  “1.15 合计”-“期末余额（穿透后）
     * 数据时点：每月末
     * @param workDate
     * @return
     * @throws Exception
     */
    public String getColumn7(String workDate) throws Exception{
        String result = "0";
        String strLastMonthEndDay = getLastMonthDayEnd(workDate); //获取上月最后一个自然日
        String strSql = "select replace(ifnull(data_value,0),',','') data_value from app_asset_nkhg_data where data_key = 'G06a02-2' and report_date = '"+strLastMonthEndDay+"'";
        SqlRow row = comnDao.findRow(strSql,null);
        if(row != null){
            result = row.getString("data_value");
        }
        return result;
    }

    /**
     * 获取年末净资本：G06b_I中“8.净资本”-“应计算金额”期末数 报表存的是元
     * @param lastMonthEndDay
     * @return
     * @throws Exception
     */
    private BigDecimal getG06b01_row18_column05(String lastMonthEndDay) throws Exception{
        BigDecimal result = null;
        String strSql = "select replace(ifnull(round(data_value/10000,2),0),',','') data_value from app_zy_g06b_I where row_id ='18' and column_id = '5' and sys_data_status = '1' and report_date = '"+lastMonthEndDay+"'";
        SqlRow row = comnDao.findRow(strSql,null);
        if(row != null){
            String strValue = row.getString("data_value");
            if(strValue != null && strValue.length()>0){
                result = new BigDecimal(strValue);
            }
        }

        if(result == null){
            result= new BigDecimal("0");
        }
        return result;
    }

    /**
     *风险资本：G06b_II“风险资本期末余额（列）”-“各项风险资本合计”数 报表存的是万元
     * @param lastMonthEndDay
     * @return
     * @throws Exception
     */
    private BigDecimal getG06b02_row48_column05(String lastMonthEndDay) throws Exception{
        BigDecimal result = null;
        String strSql = "select replace(ifnull(round(data_value,2),0),',','') data_value from app_rpt_g06b_01 where row_id = '48' and column_id = '5' and sys_data_status = '1' and report_date = '"+lastMonthEndDay+"'";
        SqlRow row = comnDao.findRow(strSql,null);
        if(row != null){
            String strValue = row.getString("data_value");
            if(strValue != null && strValue.length()>0){
                result = new BigDecimal(strValue);
            }
        }

        if(result == null){
            result= new BigDecimal("0");
        }
        return result;
    }


    /**
     * 获取上一个月的最后一个自然日
     * @param date
     * @return
     */
    public String getLastMonthDayEnd(String date){
        return getPreviousMonthLastDay(date, 0);
    }

    /**
     * 获取月份偏移量后的日期（月末最后一个自然日的日期）
     * @param date 日期
     * @param index 偏移量
     * @return
     */
    public String getPreviousMonthLastDay(String date, int index){
        SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");
        Calendar cal=Calendar.getInstance();
        Date d;
        try {
            d = df.parse(date);
            cal.setTime(d);
            cal.add(Calendar.MONTH, index);
            cal.set(Calendar.DAY_OF_MONTH,0);
        } catch (ParseException e) {

        }
        return df.format(cal.getTime());
    }

    /**
     * 获取日期前N个月，组成in('','') sql语句
     * @param date 日期
     * @param index 向前推的月数
     * @return
     */
    private String getPreviousMonthLastDaySqlInList(String date, int index){
        StringBuilder sb = new StringBuilder();
        List<String> list = getPreviousMonthLastDayList(date, index);
        if(list != null && list.size() >0){
            sb.append(" in (");
            for(int i=0;i<list.size(); i++){
                sb.append("'");
                sb.append(list.get(i));
                sb.append("'");

                if(i+1 != list.size()){
                    sb.append(", ");
                }
            }
            sb.append(") ");
        }
        return sb.toString();
    }

    /**
     * 获取日期前的月末最后一个自然日列表
     * @param date 日期
     * @param index 前推多少个月
     * @return
     */
    public List<String> getPreviousMonthLastDayList(String date, int index){
        List<String> result = new ArrayList<>();
        while(index < 0){
            String preDate = getPreviousMonthLastDay(date, ++index);
            result.add(preDate);
        }
        return result;
    }

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
}
