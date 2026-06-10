package com.kayak.clear.service.rpt.execPlan;

import com.kayak.clear.req.ReportTimeExecPlanInput;
import com.kayak.clear.req.ReportTimeExecPlanOutput;
import com.kayak.core.sql.SqlRow;
import org.springframework.stereotype.Component;

/**
 * base_type = 01 募集起始日期
 */
@Component
public class ReportExecPlanFilter_BaseType01 extends AbstractReportExecPlanFilter {

    @Override
    public String baseType() {
        return "01";
    }

    /**
     * 获取基准日期
     * @param req 请求对象
     * @return
     */
    @Override
    public String getBaseLineDate(ReportTimeExecPlanInput req) throws Exception {
        String sql = "select count(*) cnt from DWD_PRD_PRD_SPVS_INF where CLC_BGN_DT = '" + getPreClcBgnDt(req)+ "'";
        SqlRow row = comnDao.findRow(sql, req);
        int size = row.getInteger("cnt");
        if (size > 0) {
            return getPreClcBgnDt(req);
        } else {
            return null;
        }
    }

    @Override
    public ReportTimeExecPlanOutput getOutput(ReportTimeExecPlanInput req, String baseLineDate) throws Exception {
        if(!baseLineDate.equals(getPreClcBgnDt(req))){
            return null;
        }

        String strStartDate = ""; //开始日期
        String strEndDate  =""; //结束日期
        int subOffset = req.getSuperviseSubmissionTime(); //监管要求的报送日期，偏移量
        if("01".equals(req.getDataType())){ //01 工作日
            strStartDate = getAfterDay(baseLineDate,req.getInnerSubmissionTimeRequire());
            strEndDate = getWorkDayAfterDay(baseLineDate, subOffset);
        }else if( "02".equals(req.getDataType())) { //02 自然日
            strStartDate = getAfterDay(baseLineDate,req.getInnerSubmissionTimeRequire());
            strEndDate = getAfterDay(baseLineDate, subOffset);
        }else{
            ;
        }

        ReportTimeExecPlanOutput result = new ReportTimeExecPlanOutput();
        result.setReportType(req.getReportType());
        result.setReportTable(req.getReportTable());
        result.setReportTableName(req.getReportTableName());
        result.setExecStatus("0");
        result.setBaseLineDate(baseLineDate);
        result.setWorkDate(req.getWorkDate());
        result.setStartDate(strStartDate);
        result.setEndDate(strEndDate);
        return result;
    }

    /**
     * 获取募集募集开始日期的前面，由于一般生成日期是提前报因此计算前日期
     * @param req
     * @return
     * @throws Exception
     */
    private String getPreClcBgnDt(ReportTimeExecPlanInput req) throws Exception{
        int idx = req.getDataGenerTimeRequire(); //数据生成日期的偏移量
        if("01".equals(req.getDataType())){ //01 工作日
            return getWorkDayAfterDay(req.getWorkDate(), idx);
        }else if("02".equals(req.getDataType())){ // 02 自然日
            return getAfterDay(req.getWorkDate(), idx);
        }else{
            throw new Exception("无法处理日期类型 "  + req.getDataType());
        }
    }
}
