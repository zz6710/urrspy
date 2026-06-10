package com.kayak.rpt.zz.feedback.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "trClearLogService", table = "tr_clear_log")
public class TrClearLog {
    @GraphQLField(kkhtml = "KFieldText", label = "交易流水号", sql = "trans_serno = $S{transSerno}", field = "trans_serno")
    private String transSerno;
    @GraphQLField(kkhtml = "KFieldText", label = "流程序号", sql = "step_no = $S{stepNo}", field = "step_no")
    private String stepNo;
    @GraphQLField(kkhtml = "KFieldText", label = "子流程序号", sql = "step_sub_no = $S{stepSubNo}", field = "step_sub_no")
    private String stepSubNo;
    @GraphQLField(kkhtml = "KFieldText", label = "系统工作日", sql = "workdate = $S{workdate}", field = "workdate")
    private String workdate;
    @GraphQLField(kkhtml = "KFieldText", label = "业务代码", sql = "busi_code = $S{busiCode}", field = "busi_code")
    private String busiCode;
    @GraphQLField(kkhtml = "KFieldText", label = "执行日期", sql = "exec_date = $S{execDate}", field = "exec_date")
    private String execDate;
    @GraphQLField(kkhtml = "KFieldText", label = "开始时间", sql = "start_time = $S{startTime}", field = "start_time")
    private String startTime;
    @GraphQLField(kkhtml = "KFieldText", label = "结束时间", sql = "end_time = $S{endTime}", field = "end_time")
    private String endTime;
    @GraphQLField(kkhtml = "KFieldText", label = "执行状态( 0", sql = "exec_status = $S{execStatus}", field = "exec_status")
    private String execStatus;
    @GraphQLField(kkhtml = "KFieldText", label = "返回编码", sql = "rtn_code = $S{rtnCode}", field = "rtn_code")
    private String rtnCode;
    @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upd_date = $S{updDate}", field = "upd_date")
    private String updDate;
    @GraphQLField(kkhtml = "KFieldText", label = "更新时间", sql = "upd_time = $S{updTime}", field = "upd_time")
    private String updTime;
    @GraphQLField(kkhtml = "KFieldText", label = "批量文件名", sql = "file_name = $S{fileName}", field = "file_name")
    private String fileName;
    @GraphQLField(kkhtml = "KFieldText", label = "批量文件批次号", sql = "file_serno = $S{fileSerno}", field = "file_serno")
    private String fileSerno;
    @GraphQLField(kkhtml = "KFieldText", label = "文件记录数", sql = "recordnum = $S{recordnum}", field = "recordnum")
    private String recordnum;
    @GraphQLField(kkhtml = "KFieldText", label = "TA代码", sql = "tano = $S{tano}", field = "tano")
    private String tano;
    @GraphQLField(kkhtml = "KFieldText", label = "返回信息", sql = "rtn_desc = $S{rtnDesc}", field = "rtn_desc")
    private String rtnDesc;

}
