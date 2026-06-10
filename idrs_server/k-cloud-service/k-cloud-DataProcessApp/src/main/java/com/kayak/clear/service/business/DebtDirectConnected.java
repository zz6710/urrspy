package com.kayak.clear.service.business;

import com.kayak.base.dao.ComnDao;
import com.kayak.clear.req.PubReq;
import com.kayak.clear.resp.PubResp;
import com.kayak.clear.service.pub.CreateTaskService;
import com.kayak.core.sql.SqlRow;
import com.kayak.dps.direct.model.dto.IndexCodeDTO;
import com.kayak.dps.direct.service.ReportClearService;
import com.kayak.dps.direct.util.DirectParams;
import com.kayakwise.kcloud.batch.model.bo.RegistClearTaskPojo;
import com.kayakwise.kcloud.batch.model.req.TaskRegeditReq;
import com.kayakwise.kcloud.batch.service.BaseTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 批处理-中债直连
 * axin
 * 20220708
 * @author lll
 */

@Slf4j
@Component
@Scope("prototype")
public class DebtDirectConnected extends BaseTaskService<PubReq, PubResp> {

    @Autowired
    public CreateTaskService createTaskService;

    @Autowired
    public ReportClearService reportClearService;

    @Autowired
    public ComnDao comnDao;

    @Override
    protected void doCheckParams(PubReq request) throws Exception {
        log.info(" ###### 参数校验开始 ");
        beforeClear(request.getTaskDate());
    }

    @Override
    protected void doCheckBusiness(PubReq request) throws Exception {
        log.info(" ###### 业务校验开始 ");
        checkBusiness(request.getTaskId());
    }

    @Override
    protected RegistClearTaskPojo taskRegist(TaskRegeditReq taskRegeditReq) throws Exception {
        log.info(" ###### 任务注册");
        return createTaskService.createSystemTask(taskRegeditReq);
    }

    //数据合法性检查
    protected void dataValidityCheck() throws Exception {
        log.info(" ###### 数据合法性检查开始");
        reportClearService.checkRegisterData();

    }

    //生成中债三期报送文件
    protected void exportRegisterFile(String isRegisterFile) throws Exception {
        log.info(" ###### 生成中债三期报送文件开始");
        reportClearService.exportRegisterThreeFile(isRegisterFile);
    }

    // 中债一二期数据合法性检查
    protected void dataValidityCheckA(String isRegisterFile) throws Exception {
        log.info("###### 中债一二期数据合法性检查开始");
        // 查询所有待检查的报表
        List<SqlRow> reportSqlRow = comnDao.findRows("SELECT extab report_table FROM base_ex_seat WHERE extpid IN (" + isRegisterFile + ")");
        IndexCodeDTO.dto().setDealDate(DirectParams.workDate);
        // 校验当前清算任务对应表数据

        String report_table="";
        for (SqlRow sqlRow : reportSqlRow) {
            report_table=sqlRow.getString("report_table");
        }
        if(!"".equals(report_table)){
            List<SqlRow> dateList = comnDao.findRows("select report_date from base_report_data_audit_results where table_id='"+report_table+"' and audit_date='"+DirectParams.workDate+"' order by report_date ");
            for (SqlRow date : dateList) {
                reportClearService.checkRegisterDataA(report_table,date.getString("report_date"));
            }
        }
    }

    //生成中债一二期报送文件
    protected void exportRegisterFileBRXL(String isRegisterFile) throws Exception {
        log.info(" ###### 生成中债一二期报送文件开始");
        reportClearService.exportRegisterFile(isRegisterFile);

    }


    //发送报送文件
    protected void sendRegisterFile(String isRegisterFile) throws Exception {
        log.info(" ###### 发送报送文件开始");
        reportClearService.sendRegisterFile(isRegisterFile);

    }
    //获取结果文件
    protected void getResultFile(String isRegisterFile) throws Exception {
        //log.info(" ###### 获取结果文件开始");
        //reportClearService.getResultFile(isRegisterFile);

    }

    //数据归档
    protected void dataArchiving(String isRegisterFile) throws Exception {
        log.info(" ###### 执行数据归档开始");
        reportClearService.dataArchiving(isRegisterFile);

    }





    /**
     * 清算流程启动之前 调用
     * 验证清算检查前 的 准备工作是否完成
     * 包含数据初始化工作 （axin）
     * @throws Exception
     */
    public void beforeClear(String taskDate) throws Exception{

        //参数初始化
        reportClearService.initParams(taskDate);

        if("".equals(DirectParams.workDate)||DirectParams.workDate==null){
            throw new Exception("系统工作日不能为空。");
        }

        //当前时间
        String cur_datetime = DirectParams.sysDate + DirectParams.sysTime;
        System.err.println(cur_datetime + "  ---  " + DirectParams.workDate + DirectParams.workTime);
        if(Long.parseLong(cur_datetime) < Long.parseLong(DirectParams.workDate + DirectParams.workTime)){
            throw new Exception("理财中心切日后才能进行数据报备。");
        }
        if (Long.parseLong(DirectParams.sysDate) < Long.parseLong(DirectParams.workDate)) {
            throw new Exception("未到报备日期");
        }
        //验证路径
        if(DirectParams.localfilePath==null || "".equals(DirectParams.localfilePath)){
            throw new Exception("本地文件路径不能为空。");
        }

    }

    public void checkBusiness (String taskId) throws Exception {
        Map<String, Object> params = new HashMap<>();
        //检查上一日报送是否成功(根据清算最后一步的日期及状态进行清算控制)
        String checkstatus = "select t.task_date workdate,t.exec_status from kbatch_task_exec t " +
                " where t.task_id = $S{taskId} and task_date = $S{preWorkDate} and exec_status not in ('5','7') ";
        params.put("taskId",taskId);
        params.put("preWorkDate",DirectParams.preWorkDate);
        List<SqlRow> check_sr = comnDao.findRows(checkstatus, params);
        if (check_sr != null && check_sr.size() > 0){
            throw new Exception("请先完成[" + DirectParams.preWorkDate + "]的日终清算");
        }
    }

}
