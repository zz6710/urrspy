package com.kayak.clear.service.monitor;

import cn.hutool.core.util.StrUtil;
import com.kayak.clear.constants.*;
import com.kayak.clear.dto.KbatchClearTaskInfoDto;
import com.kayak.clear.utils.Tools;
import com.kayak.config.dao.SysParamDao;
import com.kayak.config.dao.Ta5014DetailDao;
import com.kayak.config.model.Ta5014Detail;
import com.kayak.config.service.DpsSysParamService;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.util.DateUtil;
import com.kayakwise.kcloud.batch.exception.TransException;
import com.kayakwise.kcloud.batch.extend.service.BatchDateService;
import com.kayakwise.kcloud.batch.model.bo.BatchPreTaskConfBO;
import com.kayakwise.kcloud.batch.model.bo.BatchPubChkResult;
import com.kayakwise.kcloud.batch.model.bo.RegistClearTaskPojo;
import com.kayakwise.kcloud.batch.model.bo.TaClearTaskExecDisplay;
import com.kayakwise.kcloud.batch.model.entity.KbatchGroupInfo;
import com.kayakwise.kcloud.batch.model.entity.KbatchTaskExec;
import com.kayakwise.kcloud.batch.model.entity.KbatchTaskStepExec;
import com.kayakwise.kcloud.batch.model.req.BatchTaskRequest;
import com.kayakwise.kcloud.batch.model.req.TaskRegeditReq;
import com.kayakwise.kcloud.batch.model.resp.BatchTaskResponse;
import com.kayakwise.kcloud.batch.service.BaseTaskService;
import com.kayakwise.kcloud.batch.service.BatchSequenceService;
import com.kayakwise.kcloud.batch.service.pub.KbatchPubService;
import com.kayakwise.kcloud.db.Dbop;
import com.kayakwise.kcloud.db.Dbtrans;
import com.kayakwise.kcloud.db.SqlResult;
import com.kayakwise.kcloud.db.SqlRow;
import com.kayakwise.kcloud.db.util.ParamMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 任务注册服务
 * @author xiamh
 * @date 2022/6/16 15:38
 */
@Service
@Scope("prototype")
public class ClearTaskRegistService {

    private final Logger log = LoggerFactory.getLogger(ClearTaskRegistService.class);

    // 当日清算任务信息
    private List<KbatchClearTaskInfoDto> sysClearTaskInfos = new ArrayList<>();
    // 当日已经生成的清算任务
    private Map<String, KbatchTaskExec> existsExecMap = new HashMap<>();
    //当日未执行完成的清算任务
    private final Set<String> unFinishTaskSet = new HashSet<>();
    //当日未全部执行完成的清算组代码
    private final Set<String> unFinishGroupSet = new HashSet<>();
    // 当日未全部执行完成的清算组代码
    private Map<String, KbatchGroupInfo> groupInfoMap = new HashMap<>();
    // 批量任务信息
    private Map<String, KbatchClearTaskInfoDto> clearTaskInfoMap = new HashMap<>();

    // 当日已生成的展示数据
    private Map<String, TaClearTaskExecDisplay> existsTaskExecDisplay = new HashMap<>();

    //批量清算任务service对象列表，需要反复调用注册方法
    private final Map<String, BaseTaskService<BatchTaskRequest, BatchTaskResponse>> serviceCollection = new HashMap<>();

    // 要插入的展示表集合
    private final List<ParamMap> insertExecDisplayParamsList = new ArrayList<>();
    // 要更新的展示表集合
    private final List<ParamMap> updateExecDisplayParamsList = new ArrayList<>();
    // 要插入的执行表对象集合
    private final List<ParamMap> taskExecParamsList = new ArrayList<>();
    // 要插入的执行子步骤表对象集合
    private final List<ParamMap> taskExecStepParamsList = new ArrayList<>();

    private final List<ParamMap> refreshGroupParamsList = new ArrayList<>();

    @Autowired
    private Dbop dbop;
    @Autowired
    private ApplicationContext applicationContext;

    private TaskRegeditReq regeditRequest = null;

    @Autowired
    public BatchSequenceService batchSequenceService;

    @Autowired
    public BatchDateService batchDateService;

    @Autowired
    public SysParamDao sysParamDao;

    @Autowired
    private Ta5014DetailDao ta5014DetailDao;


    /**
     * 注册清算执行任务数据
     */
    public void registProcess() {
        try {
            log.info(" ###### 清算任务注册开始");
            //初始化成员变量
            doInit();
            //任务注册 - 系统清算任务
            createSysTask();
            // 任务批量入库
            taskBatchSave();

            log.info(" ###### 清算任务注册完成: 新增展示任务:[{}], 更新展示任务:[{}], 新增清算任务:[{}], 新增清算子任务:[{}]", insertExecDisplayParamsList.size(), updateExecDisplayParamsList.size(), taskExecParamsList.size(), taskExecStepParamsList.size());

        } catch (Exception e) {
            log.error("任务注册定时任务出错！错误信息:[{}]", e.getMessage(), e);
        }
    }

    /**
     * 注册清算执行任务数据(实时)
     */
    public void registProcessB() {
        try {
            log.info(" ###### 清算任务注册开始");
            //实时清算任务刷新
            actualTaskRefresh();
            //初始化成员变量
            doInitB();
            //任务注册 - 系统清算任务
            createSysTask();
            // 任务批量入库
            taskBatchSave();

            log.info(" ###### 清算任务注册完成: 新增展示任务:[{}], 更新展示任务:[{}], 新增清算任务:[{}], 新增清算子任务:[{}]", insertExecDisplayParamsList.size(), updateExecDisplayParamsList.size(), taskExecParamsList.size(), taskExecStepParamsList.size());

        } catch (Exception e) {
            log.error("任务注册定时任务出错！错误信息:[{}]", e.getMessage(), e);
        }
    }


    /**
     * 方法描述:初始化需要使用到的成员变量
     *
     * @throws Exception
     */
    private void doInit() throws Exception {
        TaskRegeditReq regeditReq = getRegeditReq();

        SqlResult sResult = null;

        /** 初始化系统清算任务：需要生成清算任务的信息 */
        sResult = dbop.select("C018Q196", new ParamMap().on("moduleid", ModuleIdContents.PMS).on("task_type", BatchTaskType.SYSTEM));
        if (sResult.getRowSize() > 0) {
            sysClearTaskInfos = this.getTaskInfoList(sResult);
        }

        /** 初始化已经注册的任务数据 */
        // 系统模块ID、这里按业务日期查询，因为按业务日期注册，而查询当日未执行任务应该要用应执行日期、任务类型：1-系统清算
        sResult = dbop.select("C018Q204", new ParamMap().on("moduleid", ModuleIdContents.PMS).on("task_date", regeditReq.getCurrWorkdate()).on("task_type", BatchTaskType.SYSTEM));
        if (sResult.getRowSize() > 0) {
            existsExecMap = sResult.getRows().stream().collect(Collectors.toMap(row -> ClearTaskRegistUtil.getSysExecKey(row.getString("task_group"), row.getString("task_id")), row -> converTaskExecObj(row)));
        }

        /** 初始化已经生成的展示数据 */
        sResult = dbop.select("C018Q203", new ParamMap().on("task_date", regeditReq.getCurrWorkdate()).on("moduleid", ModuleIdContents.PMS));
        if(sResult.getRowSize() > 0){
            existsTaskExecDisplay = sResult.getRows().stream().collect(Collectors.toMap(row -> ClearTaskRegistUtil.getSysExecKey(row.getString("task_group"), row.getString("task_id")), row -> converTaskExecDisplayObj(row)));
        }

        /** 查询当天未完成的任务，key=taskGroup_taskId */
        // 查询除了执行完成、跳过、终止状态的任务，这些都是当日未完成的任务，用应执行日期查询而不是业务日期，因为这是用来做当日前置任务校验的
        SqlResult sqlResult = dbop.select("C018Q206", new ParamMap()
                .on("moduleid", ModuleIdContents.PMS)                           // 模块ID
                .on("task_date", regeditReq.getCurrWorkdate())                 // 清算业务日期
                .on("exec_status_success", BatchTaskStatus.SUCCESS)            // 任务执行状态 执行成功
                .on("exec_status_skip", BatchTaskStatus.SKIP)                  // 任务执行状态 跳过
                .on("exec_status_termination", BatchTaskStatus.TERMINATION)    // 任务执行状态 终止
                .on("is_exec", GlobalContents.YES)                             //
        );
        if (sqlResult.getRowSize() > 0) {
            //将未完成的清算任务存入列表，后续判断前置任务是否完成时使用
            List<KbatchTaskExec> unFinishTaskList = this.getTaskExecs(sqlResult);
            unFinishTaskList.forEach(m -> ClearTaskRegistUtil.initTaskSet(m, unFinishTaskSet));
        }

        /** 查询未全部完成的清算组 */
        //查询当日组内仍有未完成任务的组代码；判断规则：如果展示表内有任务是需要执行的，但是在执行表中未查询到状态为完成的数据，就认为该任务所属组的任务未全部完成
        //用应执行日期查询而不是业务日期，因为这是用来做当日前置任务校验的
        sqlResult = dbop.select("C018Q208", new ParamMap()
                .on("moduleid", ModuleIdContents.PMS)
                .on("task_date", regeditReq.getCurrWorkdate())
                .on("is_exec_yes", GlobalContents.YES)
                .on("exec_status_success", BatchTaskStatus.SUCCESS)
                .on("exec_status_skip", BatchTaskStatus.SKIP)
                .on("exec_status_termination", BatchTaskStatus.TERMINATION)
        );
        if (sqlResult.getRowSize() > 0) {
            //记录还有任务未完成的清算组
            List<KbatchTaskExec> unFinishGroupList = this.getTaskExecs(sqlResult);
            unFinishGroupList.forEach(m -> unFinishGroupSet.add(m.getTaskGroup()));
        }

        /** 查询所有清算任务信息 */
        sqlResult = dbop.select("C018Q209", new ParamMap().on("moduleid", ModuleIdContents.PMS));
        if (sqlResult.getRowSize() > 0) {
            List<KbatchClearTaskInfoDto> taskInfoList = this.getTaskInfoList(sqlResult);
            clearTaskInfoMap = taskInfoList.stream().collect(Collectors.toMap(KbatchClearTaskInfoDto::getTaskId, m -> m));
        } else {
            throw new TransException("C118003", "未查询到清算任务信息");
        }

        /** 查询所有清算组信息 */
        sqlResult = dbop.select("C018Q210", new ParamMap());
        if (sqlResult.getRowSize() > 0) {
            //缓存清算组信息
            List<KbatchGroupInfo> groupInfoList = this.getGroupInfoList(sqlResult);
            groupInfoMap = groupInfoList.stream().collect(Collectors.toMap(KbatchGroupInfo::getTaskGroup, m -> m));
        } else {
            throw new TransException("C118003", "未查询到清算任务组信息");
        }
    }


    /**
     * 方法描述:初始化需要使用到的成员变量(实时)
     *
     * @throws Exception
     */
    private void doInitB() throws Exception {
        TaskRegeditReq regeditReq = getRegeditReq();

        SqlResult sResult = null;

        /** 初始化实时清算任务：需要生成清算任务的信息 */
        sResult = dbop.select("C018Q195", new ParamMap().on("moduleid", ModuleIdContents.PMS).on("task_type", BatchTaskType.ACTUAL_TIME));
        if (sResult.getRowSize() > 0) {
            sysClearTaskInfos = this.getTaskInfoList(sResult);
        }
        /** 初始化已经注册的任务数据 */
        // 系统模块ID、这里按业务日期查询，因为按业务日期注册，而查询当日未执行任务应该要用应执行日期、任务类型：9-实时清算
        sResult = dbop.select("C018Q204", new ParamMap().on("moduleid", ModuleIdContents.PMS).on("task_date", regeditReq.getCurrWorkdate()).on("task_type", BatchTaskType.ACTUAL_TIME));
        if (sResult.getRowSize() > 0) {
            existsExecMap = sResult.getRows().stream().collect(Collectors.toMap(row -> ClearTaskRegistUtil.getSysExecKey(row.getString("task_group"), row.getString("task_id")), row -> converTaskExecObj(row)));
        }

        /** 初始化已经生成的展示数据 */
        sResult = dbop.select("C018Q203", new ParamMap().on("task_date", regeditReq.getCurrWorkdate()).on("moduleid", ModuleIdContents.PMS));
        if(sResult.getRowSize() > 0){
            existsTaskExecDisplay = sResult.getRows().stream().collect(Collectors.toMap(row -> ClearTaskRegistUtil.getSysExecKey(row.getString("task_group"), row.getString("task_id")), row -> converTaskExecDisplayObj(row)));
        }

        /** 查询当天未完成的任务，key=taskGroup_taskId */
        // 查询除了执行完成、跳过、终止状态的任务，这些都是当日未完成的任务，用应执行日期查询而不是业务日期，因为这是用来做当日前置任务校验的
        SqlResult sqlResult = dbop.select("C018Q206", new ParamMap()
                .on("moduleid", ModuleIdContents.PMS)                           // 模块ID
                .on("task_date", regeditReq.getCurrWorkdate())                 // 清算业务日期
                .on("exec_status_success", BatchTaskStatus.SUCCESS)            // 任务执行状态 执行成功
                .on("exec_status_skip", BatchTaskStatus.SKIP)                  // 任务执行状态 跳过
                .on("exec_status_termination", BatchTaskStatus.TERMINATION)    // 任务执行状态 终止
                .on("is_exec", GlobalContents.YES)                             //
        );
        if (sqlResult.getRowSize() > 0) {
            //将未完成的清算任务存入列表，后续判断前置任务是否完成时使用
            List<KbatchTaskExec> unFinishTaskList = this.getTaskExecs(sqlResult);
            unFinishTaskList.forEach(m -> ClearTaskRegistUtil.initTaskSet(m, unFinishTaskSet));
        }

        /** 查询未全部完成的清算组 */
        //查询当日组内仍有未完成任务的组代码；判断规则：如果展示表内有任务是需要执行的，但是在执行表中未查询到状态为完成的数据，就认为该任务所属组的任务未全部完成
        //用应执行日期查询而不是业务日期，因为这是用来做当日前置任务校验的
        sqlResult = dbop.select("C018Q208", new ParamMap()
                .on("moduleid", ModuleIdContents.PMS)
                .on("task_date", regeditReq.getCurrWorkdate())
                .on("is_exec_yes", GlobalContents.YES)
                .on("exec_status_success", BatchTaskStatus.SUCCESS)
                .on("exec_status_skip", BatchTaskStatus.SKIP)
                .on("exec_status_termination", BatchTaskStatus.TERMINATION)
        );
        if (sqlResult.getRowSize() > 0) {
            //记录还有任务未完成的清算组
            List<KbatchTaskExec> unFinishGroupList = this.getTaskExecs(sqlResult);
            unFinishGroupList.forEach(m -> unFinishGroupSet.add(m.getTaskGroup()));
        }

        /** 查询所有清算任务信息 */
        sqlResult = dbop.select("C018Q209", new ParamMap().on("moduleid", ModuleIdContents.PMS));
        if (sqlResult.getRowSize() > 0) {
            List<KbatchClearTaskInfoDto> taskInfoList = this.getTaskInfoList(sqlResult);
            clearTaskInfoMap = taskInfoList.stream().collect(Collectors.toMap(KbatchClearTaskInfoDto::getTaskId, m -> m));
        } else {
            throw new TransException("C118003", "未查询到清算任务信息");
        }

        /** 查询所有清算组信息 */
        sqlResult = dbop.select("C018Q210", new ParamMap());
        if (sqlResult.getRowSize() > 0) {
            //缓存清算组信息
            List<KbatchGroupInfo> groupInfoList = this.getGroupInfoList(sqlResult);
            groupInfoMap = groupInfoList.stream().collect(Collectors.toMap(KbatchGroupInfo::getTaskGroup, m -> m));
        } else {
            throw new TransException("C118003", "未查询到清算任务组信息");
        }
    }

    /**
     * 方法描述:注册系统清算任务
     */
    private void createSysTask() throws Exception {

        /** 构建注册调用入参对象 (机器时间,工作日,是否自动执行) */
        TaskRegeditReq taskRegeditReq = getRegeditReq();

        // 循环系统清算任务，调用服务类的任务注册方法
        for (KbatchClearTaskInfoDto taskInfo : sysClearTaskInfos) {
            RegistClearTaskPojo registClearTaskPojo;
            /**
             * 如果任务执行表已经有数据，不用再注册。。如果注册后需要修改状态，另提供交易给管理台，进行预展示表、执行表的状态同时更新，任务注册扫描程序不做更新
             * 如果任务已经注册到执行表之后，再 修改参数信息，删减了清算任务，则该条任务任务在展示表、执行表的状态和前置任务等信息都不会去更新，需要人工干预
             */
            if (!existsExecMap.containsKey(ClearTaskRegistUtil.getSysExecKey(taskInfo.getTaskGroup(), taskInfo.getTaskId()))) {
                // 根据清算任务信息获取业务处理类
                BaseTaskService<BatchTaskRequest, BatchTaskResponse> clearService = this.getClearTaskService(taskInfo.getTaskId(), taskInfo.getServiceClass());
                // 调用注册方法，返回需要注册的任务对象
                taskRegeditReq.setTaClearTaskInfo(taskInfo);
                registClearTaskPojo = clearService.checkAndTaskRegist(taskRegeditReq);

                // 数据处理
                this.insertExecAndDisplay(registClearTaskPojo, taskRegeditReq, taskInfo, null, null);
            }
        }
    }

    /**
     * 清算数据批量入库、批量更新
     */
    private void taskBatchSave(){
        if((updateExecDisplayParamsList == null || updateExecDisplayParamsList.size()==0)
            && (insertExecDisplayParamsList == null || insertExecDisplayParamsList.size()==0)
            && (taskExecParamsList==null || taskExecParamsList.size()==0)
            && (taskExecStepParamsList==null || taskExecStepParamsList.size()==0)){
            return;
        }

        //插入执行表、子步骤执行表数据
        try (Dbtrans ds = dbop.starttrans()) {

            // 更新展示表
            if(updateExecDisplayParamsList != null && updateExecDisplayParamsList.size()>0){
                dbop.updateBatch("C018U024", updateExecDisplayParamsList);
            }

            // 插入展示表
            if(insertExecDisplayParamsList != null && insertExecDisplayParamsList.size()>0){
                dbop.updateBatch("C018U023", insertExecDisplayParamsList);
            }

            // 插入任务执行表
            if (taskExecParamsList.size() > 0) {
                dbop.updateBatch("C018U01", taskExecParamsList);
            }
            // 插入任务子步骤执行表
            if(taskExecStepParamsList!=null && taskExecStepParamsList.size()>0){
                dbop.updateBatch("C018U02", taskExecStepParamsList);
            }

            //事务提交
            dbop.commit();
        } catch (Exception e) {
            log.error("任务注册定时任务插入执行表出错！错误信息:[{}]", e.getMessage(), e);
            try {
                dbop.rollback();
            } catch (SQLException ex) {
                log.error(" 批量入库回滚失败: " + ex.getMessage(), ex);
            }
        }
    }

    /**
     * 实时清算任务刷新
     */
    private void actualTaskRefresh() throws Exception{
        TaskRegeditReq regeditReq = getRegeditReq();
        //查询实时任务执行失败的组及全部成功或跳过状态的组
        SqlResult sResult = dbop.select("C018Q215", new ParamMap()
                .on("moduleid", ModuleIdContents.PMS)
                .on("task_date", regeditReq.getCurrWorkdate())
                .on("task_type", BatchTaskType.ACTUAL_TIME)
                .on("exec_status_failed",BatchTaskStatus.FAILED)
                .on("exec_status_slice_failed",BatchTaskStatus.SLICE_FAILED)
                .on("exec_status_success",BatchTaskStatus.SUCCESS)
                .on("exec_status_skip",BatchTaskStatus.SKIP)
                .on("exec_status_termination",BatchTaskStatus.TERMINATION));
        while(sResult.next()){
            refreshGroupParamsList.add(new ParamMap().on("task_group",sResult.getString("task_group")).on("task_date", regeditReq.getCurrWorkdate()));
        }
        try (Dbtrans ds = dbop.starttrans()) {
            if(refreshGroupParamsList.size()>0){
                //删除任务子步骤执行表
                dbop.updateBatch("C018U025", refreshGroupParamsList);
                //删除任务执行表
                dbop.updateBatch("C018U026", refreshGroupParamsList);
                //删除展示表
                dbop.updateBatch("C018U027", refreshGroupParamsList);
            }
            //事务提交
            dbop.commit();
        } catch (Exception e) {
            log.error("实时清算任务刷新出错！错误信息:[{}]", e.getMessage(), e);
            try {
                dbop.rollback();
            } catch (SQLException ex) {
                log.error("回滚失败: " + ex.getMessage(), ex);
            }
        }

    }

    /**
     * 方法描述：业务校验未返回执行任务对象时，构建一个执行任务对象
     *
     * @param registClearTaskPojo
     * @return
     * @throws Exception
     */
    private KbatchTaskExec checkReturnTaskExec(RegistClearTaskPojo registClearTaskPojo, KbatchClearTaskInfoDto taskInfo, TaskRegeditReq taskRegeditReq, String prodCode, String distributorCode) throws Exception {
        //业务返回需要执行任务，则执行表对象不能为空
        if (registClearTaskPojo.getIsExec() && registClearTaskPojo.getTaClearTaskExec() == null) {
            throw new TransException("C118008", String.format("业务 taskid[%s]当日需要执行任务，但未构建执行任务对象，注册失败", taskInfo.getTaskId()));
        }
        return registClearTaskPojo.getTaClearTaskExec();
    }


    /**
     * 方法描述:插入记录到任务执行表、子步骤执行表和任务预展示表，方法内有事务
     *
     * @param regBatchTaskPojo 任务注册检查结果
     * @param prodCode         产品代码
     * @param targetCode       目标代码
     * @throws Exception 添加任务异常
     */
    private void insertExecAndDisplay(RegistClearTaskPojo regBatchTaskPojo, TaskRegeditReq taskRegtReq, KbatchClearTaskInfoDto taBatchTaskInfo, String prodCode, String targetCode) throws Exception {
        // 未返回注册对象或者 不需要注册，直接返回
        if (regBatchTaskPojo == null) {
            return;
        }

        // KbatchTaskExec: 校验任务执行表对象，如果清算处理类未返回，自行构造一个，否则用清算处理类返回的对象
        KbatchTaskExec taskExec = this.checkReturnTaskExec(regBatchTaskPojo, taBatchTaskInfo, taskRegtReq, prodCode, targetCode);

        // 任务执行ID
        String taskExecId;
        //运 营人员手工设置的任务状态，如果预展示表这个状态不是初始化状态，则按展示表中设置的状态插入到执行表
        String setExecStatus = null;
        // 是否执行
        String isExec = regBatchTaskPojo.getIsExec() ? GlobalContents.YES : GlobalContents.NO;

        // 判断当前展示任务是否已经注册
        TaClearTaskExecDisplay taskExecDisplay = this.existsTaskExecDisplay.get(ClearTaskRegistUtil.getSysExecKey(taBatchTaskInfo.getTaskGroup(), taBatchTaskInfo.getTaskId()));
        // 展示表已经有数据，判断是否需要更新
        if (taskExecDisplay != null) {

            taskExecId = taskExecDisplay.getTask_execid();
            // “是否需要执行”或者“前置任务”不一致，表示有参数发生了改变，现在该任务需要执行，需要更新预展示表
            if (!isExec.equals(taskExecDisplay.getIs_exec()) || (Tools.strIsNotEmpty(taskExec.getPreTaskId()) && !String.format("%s", taskExec.getPreTaskId()).equals(String.format("%s", taskExecDisplay.getPre_task_id())))) {
                // 初始化更新对象
                updateExecDisplayParamsList.add(new ParamMap()
                        .on("task_date", taskRegtReq.getCurrWorkdate())
                        .on("task_group", taBatchTaskInfo.getTaskGroup())
                        .on("task_id", taBatchTaskInfo.getTaskId())
                        .on("moduleid", taBatchTaskInfo.getModuleid())
                        .on("is_exec", isExec)
                        .on("pre_task_id", taskExecDisplay.getPre_task_id())
                    );

                //任务从不需要执行变成了需要执行，则可能会影响前置任务判断，需要调整未完成列表。如果是从需要执行到不需要执行，本次注册先不处理，下次注册会按新的is_exec来处理
                ClearTaskRegistUtil.initTaskSet(taskExec, this.unFinishTaskSet);
            }

            //有修改状态，按这个状态插入执行表
            if (!BatchTaskStatus.DISPLAY_INIT.equals(taskExecDisplay.getExec_status())) {
                setExecStatus = taskExecDisplay.getExec_status();
            }

            //判断是否是管理台发起的自动执行,如果是,则插入执行表中时,优先使用管理台发起的自动执行
            if (AutoExec.WEB_INVOKE.equals(taskExecDisplay.getAuto_exec())) {
                taskExec.setAutoExec(AutoExec.WEB_INVOKE);
            }
        // 展示表没有数据，初始化插入对象
        } else {
            taskExecId = taskExec.getTaskExecid();
            if (Tools.isBlank(taskExecId)) {
                // 预展示表无数据，插入数据执行ID新生成
                taskExecId = batchSequenceService.genExecId();
            }
            //没有数据插入展示表时,才开始执行任务注册.如果还有数据插入,则该次任务调度以后循环到的任务先不任务注册
            insertExecDisplayParamsList.add(initTaskExecDisplayParam(taskExec, taskRegtReq, isExec, taskExecId));
        }

        /** 初始化清算任务和子任务信息 */
        if (!regBatchTaskPojo.getIsExec()) {        // 如果当日不需要执行，则不插入
            return;
        }
        // 这里需要使用清算业务类返回的数据，因为taClearTaskExec 也可能是自己构造的，没有前置任务等数据
        if (regBatchTaskPojo.getTaClearTaskExec() == null) {
            log.debug("业务返回任务执行信息数据为空，该任务未达到执行条件，不注册");
            return;
        }
        if (regBatchTaskPojo.getTaClearTaskStepExecList() == null || regBatchTaskPojo.getTaClearTaskStepExecList().size() < 1) {
            log.debug("业务返回任务子步骤执行信息数据为空，该任务未达到执行条件，不注册");
            return;
        }

        // 前置任务校验，如果前置任务没有做完，就不插入数据到任务执行表；前置任务校验使用的前置任务信息都是ta_clear_task_info中查出来的，而不是在display中已经记录的
        boolean preTaskIsFinish = checkPreTask(taskExec, unFinishGroupSet);
        if (!preTaskIsFinish) {
            //前置任务没有全部完成，不注册
            log.info("前置任务未全部完成:[{}]", taskExec);
            return;
        }

        // 清算任务执行表和执行子步骤表，任务执行ID与展示表一致
        this.createClearTaskParams(taskExec, regBatchTaskPojo.getTaClearTaskStepExecList(), taskExecId, setExecStatus);
    }

    /**
     * 校验前置任务是否完成
     *
     * @param taskExec
     * @return
     */
    private boolean checkPreTask(KbatchTaskExec taskExec, Set<String> unFinishGroupSet) {

        // 先校验前置任务组,产品清算配置 ta_clear_task_set 表 只有本组内各任务的前置顺序关系，而组之间的前置任务要通过组前置来控制
        String taskGroup = taskExec.getTaskGroup();

        KbatchGroupInfo groupInfo = groupInfoMap.get(taskGroup);
        if (groupInfo == null) {
            throw new TransException("C118007", String.format("未查询到任务id[%s]的组信息，组代码[%s]，请核对清算任务配置", taskExec.getTaskId(), taskGroup));
        }

        // 分割前置组任务
        String[] groupPerTasks = KbatchPubService.dealGroupPerTaskStr(groupInfo.getPreTaskGroup());
        for (String preTask : groupPerTasks) {
            if (unFinishGroupSet.contains(preTask)) {
                //前置任务组有未完成的任务，直接返回不通过
                log.debug("前置任务组校验不通过 清算业务日期:[{}] 本任务组:[{}] 前置任务组:[{}]", taskExec.getTaskDate(), taskGroup, groupInfo.getPreTaskGroup());
                return false;
            }
        }

        //前置任务
        String preTaskId = taskExec.getPreTaskId();
        if (StringUtils.isBlank(preTaskId)) {
            //没有前置任务，直接返回成功
            return true;
        }

        // 校验前置任务配置格式
        BatchPubChkResult<String> chkResult = KbatchPubService.chkPreTaskConf(preTaskId, taskExec.getTaskGroup(), taskExec.getTaskId());
        if (!chkResult.isPass()) {
            // 前置任务格式不正确
            throw new TransException("C118002", String.format("前置任务格式有误，任务id[%s]", taskExec.getTaskId()));
        }

        // 解析前置任务
        List<BatchPreTaskConfBO> preTaskConfList = KbatchPubService.dealPerTaskStr(preTaskId);
        String taskDate = taskExec.getTaskDate();

        for (BatchPreTaskConfBO preTaskIdItem : preTaskConfList) {
            // 如果前置任务组为 # 则表示校验当前任务组（界面没提供配置前置，所以只会当前任务组，就是只会是#）
            String perTaskGroup = StringUtils.equals("#", preTaskIdItem.getTaskGroup()) ? taskExec.getTaskGroup() : preTaskIdItem.getTaskGroup();
            String perTaskId = preTaskIdItem.getTaskId();    // 前置任务 任务ID
            if (log.isDebugEnabled()) {
                log.debug("前置任务信息 清算业务日期:[{}] 前置任务组:[{}] 前置任务:[{}]", taskDate, perTaskGroup, perTaskId);
            }

            // 前置任务 * 代表校验该任务组所有任务
            if ("*".equals(perTaskId)) {
                if (unFinishGroupSet.contains(perTaskGroup)) {
                    log.debug("存在未执行前置任务  清算业务日期:[{}] 任务组:[{}] 前置任务ID:[*]", taskDate, perTaskGroup);
                    return false;
                } else {
                    //前置任务组的任务已经全部完成
                }
            } else {
                // 前置是单个任务而不是组内所有任务
                // 缓存中不存在前置任务信息，抛出异常
                if (!clearTaskInfoMap.containsKey(perTaskId)) {
                    throw new TransException("C118004", String.format("清算任务信息错误：未查询到前置任务信息，任务id[%s]，前置任务id[%s]", taskExec.getTaskId(), perTaskId));
                }

                // 从缓存中取出前置任务信息，根据前置任务的类型从缓存map中判断前置任务执行情况
                KbatchClearTaskInfoDto preTaskInfo = clearTaskInfoMap.get(perTaskId);
                // 任务拼接字符串 Key  = 任务组_任务ID
                String mapKey = ClearTaskRegistUtil.getSysExecKey(perTaskGroup, perTaskId);

                if (unFinishTaskSet.contains(mapKey)) {
                    //如果前置任务在执行表状态不是完成、且任务是要执行的，但还没有写入执行表都认为是未完成
                    log.debug("前置任务未完成，任务id[{}]，前置任务id[{}]，mapKey:[{}]", taskExec.getTaskId(), perTaskId, mapKey);
                    return false;
                }
            }
        }

        return true;
    }

    private ParamMap initTaskExecDisplayParam(KbatchTaskExec taskExec, TaskRegeditReq taskRegeditReq, String isExec, String execId) {
        return new ParamMap()
                .on("task_execid", execId)
                .on("is_exec", isExec)
                .on("moduleid", ModuleIdContents.PMS)
                .on("pre_task_id", taskExec.getPreTaskId())
                .on("prod_code", taskExec.getProdCode())
                .on("exec_status", BatchTaskStatus.DISPLAY_INIT)
                .on("should_exec_date", taskExec.getShouldExecDate())
                .on("should_exec_time", taskExec.getShouldExecTime())
                .on("target_code", taskExec.getTargetCode())
                .on("task_date", taskRegeditReq.getCurrWorkdate())
                .on("task_id",taskExec.getTaskId())
                .on("task_group", taskExec.getTaskGroup())
                .on("distributor_batch",taskExec.getDistributorBatch());
    }

    private TaClearTaskExecDisplay converTaskExecDisplayObj(SqlRow row) {
        TaClearTaskExecDisplay taskExecDisplay = new TaClearTaskExecDisplay();
        taskExecDisplay.setTask_execid(row.getString("task_execid"));
        taskExecDisplay.setIs_exec(row.getString("is_exec"));
        taskExecDisplay.setModuleid(ModuleIdContents.PMS);
        taskExecDisplay.setPre_task_id(row.getString("pre_task_id"));
        taskExecDisplay.setProd_code(row.getString("prod_code"));
        taskExecDisplay.setExec_status(BatchTaskStatus.DISPLAY_INIT);
        taskExecDisplay.setShould_exec_date(row.getString("should_exec_date"));
        taskExecDisplay.setShould_exec_time(row.getString("should_exec_time"));
        taskExecDisplay.setTarget_code(row.getString("target_code"));
        taskExecDisplay.setTask_date(this.regeditRequest.getCurrWorkdate());
        taskExecDisplay.setTask_group(row.getString("task_group"));
        taskExecDisplay.setTask_id(row.getString("task_id"));
        taskExecDisplay.setDistributor_batch(row.getString("distributor_batch"));
        return taskExecDisplay;
    }

    /**
     * 方法描述:创建调用注册方法入参对象
     */
    private TaskRegeditReq getRegeditReq() throws Exception {
        if (this.regeditRequest == null) {
            // 注册方法公共入参对象未初始化则初始化该对象
            // 查询参数表，系统是否要自动执行批量，该字段在任务调度时有用
//            String autoexec = systemService.getSysParam(TaSystemParameterContents.TA_BATCH_AUTO_EXEC, ModuleIdContents.TA).getParavalue();
//            String autoexec = "1";
            String autoexec = sysParamDao.queryParamById("8", "80000072");

                    // 当前物理日期
//            String curSystemDate = systemService.getCurrDate();
            String curSystemDate = batchDateService.getCurrDate();

            // 获取下一工作日，和下两个工作日 T1，T2产品

            // 当前工作日
            String currWorkDate = batchDateService.getCurrentWorkdate("0");
            // 上一工作日
            String preWorkDate = DateUtil.add(currWorkDate, "yyyyMMdd", -1);
            // 下一工作日
            String t1WorkDate = DateUtil.add(currWorkDate, "yyyyMMdd", 1);

            if (StringUtils.isBlank(currWorkDate)) {
                throw new TransException("9999", "获取工作日失败");
            }

            this.regeditRequest = new TaskRegeditReq();
            this.regeditRequest.setT1WorkDate(t1WorkDate);      // T1工作日
            this.regeditRequest.setAutoexec(autoexec);          // 是否自动执行参数
            this.regeditRequest.setCurrDate(curSystemDate);     // 当前机器时间
            this.regeditRequest.setCurrWorkdate(currWorkDate);  // 当前工作日
            this.regeditRequest.setPreWorkdate(preWorkDate);    // 上一工作日
        }

        return (TaskRegeditReq) this.regeditRequest.clone();
    }


    /**
     * 方法描述:获取清算任务的执行service
     *
     * @param taskId
     * @param serviceClass
     * @return
     * @throws Exception
     */
    private BaseTaskService<BatchTaskRequest, BatchTaskResponse> getClearTaskService(String taskId, String serviceClass) throws Exception {
        // 如果缓存map中已经有清算service，从缓存中获取，否则反射并放入缓存map
        if (serviceCollection.containsKey(taskId)) {
            return serviceCollection.get(taskId);
        } else {
            BaseTaskService<BatchTaskRequest, BatchTaskResponse> clearService = (BaseTaskService<BatchTaskRequest, BatchTaskResponse>) applicationContext.getBean(Class.forName(serviceClass));
            serviceCollection.put(taskId, clearService);
            return clearService;
        }
    }

    /**
     * 方法描述：插入数据到批量任务执行表
     *
     * @throws Exception
     */
    public void createClearTaskParams(KbatchTaskExec taskExec, List<KbatchTaskStepExec> taskStepExecList, String execId, String setExecStatus) {
        // 步骤信息
        ParamMap _execParams = new ParamMap();
        new ParamMap().on(taskExec).forEach((k,v) -> { _execParams.put(StrUtil.toUnderlineCase(k), v); });  // map中key由驼峰转为下划线
        if (Tools.strIsNotEmpty(setExecStatus)) {
            _execParams.on("exec_status", setExecStatus);   //预展示表有预设置状态
        }
        _execParams.on("task_execid", execId);              //执行ID预展示表保持一致
        taskExecParamsList.add(_execParams);

        // 子步骤信息
        List<ParamMap> stepExecParamsList = taskStepExecList.stream().map(item -> {
            ParamMap _stepExecParams = new ParamMap();
            new ParamMap().on(item).forEach((k,v) -> _stepExecParams.on(StrUtil.toUnderlineCase(k), v));    // map中key由驼峰转为下划线
            if (Tools.strIsNotEmpty(setExecStatus)) {
                //预展示表有预设置状态
                _stepExecParams.on("exec_status", setExecStatus);
            }
            _stepExecParams.on("task_execid", execId);
            return _stepExecParams;
        }).collect(Collectors.toList());
        taskExecStepParamsList.addAll(stepExecParamsList);
    }

    private List<KbatchTaskExec> getTaskExecs(SqlResult sResult) {
        List<KbatchTaskExec> taskExecs = new ArrayList<>();
        while (sResult.next()) {
            KbatchTaskExec pojo = new KbatchTaskExec();
            pojo.setProdCode(sResult.getString("prod_code"));
            pojo.setTaskGroup(sResult.getString("task_group"));
            pojo.setTaskId(sResult.getString("task_id"));
            pojo.setTargetCode(sResult.getString("target_code"));
            pojo.setDistributorBatch(sResult.getString("distributor_batch"));
            pojo.setTaskType(sResult.getString("task_type"));
            taskExecs.add(pojo);
        }
        return taskExecs;
    }

    private KbatchTaskExec converTaskExecObj(SqlRow row) {
        KbatchTaskExec taskExec = new KbatchTaskExec();
        taskExec.setProdCode(row.getString("prod_code"));
        taskExec.setTaskGroup(row.getString("task_group"));
        taskExec.setTaskId(row.getString("task_id"));
        taskExec.setTargetCode(row.getString("target_code"));
        taskExec.setDistributorBatch(row.getString("distributor_batch"));
        taskExec.setTaskType(row.getString("task_type"));
        return taskExec;
    }

    private List<KbatchClearTaskInfoDto> getTaskInfoList(SqlResult sResult) {
        List<KbatchClearTaskInfoDto> taskInfoList = new ArrayList<>();
        while (sResult.next()) {
            KbatchClearTaskInfoDto pojo = new KbatchClearTaskInfoDto();
            pojo.setTaskGroup(sResult.getString("task_group"));
            pojo.setRunningType(sResult.getString("running_type"));
            pojo.setShouldExecTime(sResult.getString("should_exec_time"));
            pojo.setAlarmTime(sResult.getString("alarm_time"));
            pojo.setTaskId(sResult.getString("task_id"));
            pojo.setTaskName(sResult.getString("task_name"));
            pojo.setModuleid(sResult.getString("moduleid"));
            pojo.setTaskType(sResult.getString("task_type"));
            pojo.setCanAgain(sResult.getString("can_again"));
            pojo.setPreTaskId(sResult.getString("pre_task_id"));
            pojo.setExecOrder(sResult.getString("exec_order"));
            pojo.setTaskParams(sResult.getString("task_params"));
            pojo.setSimpleFlow(sResult.getString("simple_flow"));
            pojo.setServiceClass(sResult.getString("service_class"));
            pojo.setReqClass(sResult.getString("req_class"));
            pojo.setInClass(sResult.getString("in_class"));
            pojo.setTaskModel(sResult.getString("task_model"));
            taskInfoList.add(pojo);
        }
        return taskInfoList;
    }

    private List<KbatchGroupInfo> getGroupInfoList(SqlResult sqlResult) {
        List<KbatchGroupInfo> groupInfoList = new ArrayList<>();
        while (sqlResult.next()) {
            KbatchGroupInfo pojo = new KbatchGroupInfo();
            pojo.setTaskGroup(sqlResult.getString("task_group"));
            pojo.setExecTaskType(sqlResult.getString("exec_task_type"));
            pojo.setPreTaskGroup(sqlResult.getString("pre_task_group"));
            groupInfoList.add(pojo);
        }
        return groupInfoList;
    }

}
