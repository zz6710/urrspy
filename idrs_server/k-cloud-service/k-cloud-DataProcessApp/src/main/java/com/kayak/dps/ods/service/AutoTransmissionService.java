package com.kayak.dps.ods.service;

import com.alibaba.fastjson.JSONObject;
import com.kayak.base.dao.ComnDao;
import com.kayak.clear.req.PubReq;
import com.kayak.clear.resp.PubResp;
import com.kayak.clear.service.pub.CreateTaskService;
import com.kayak.config.utils.DbopChange;
import com.kayak.core.sql.SqlResult;
import com.kayak.dps.check.dao.T8portInfoDao;
import com.kayak.dps.check.model.T8PortInfoModel;
import com.kayak.graphql.model.FetcherData;
import com.kayakwise.kcloud.batch.annotation.StepNo;
import com.kayakwise.kcloud.batch.enums.BatchStepStatusControlEnum;
import com.kayakwise.kcloud.batch.model.bo.BatchTaskStepReuslt;
import com.kayakwise.kcloud.batch.model.bo.RegistClearTaskPojo;
import com.kayakwise.kcloud.batch.model.bo.SliceJobSplitBO;
import com.kayakwise.kcloud.batch.model.entity.KbatchTaskExec;
import com.kayakwise.kcloud.batch.model.entity.KbatchTaskStepExec;
import com.kayakwise.kcloud.batch.model.req.TaskRegeditReq;
import com.kayakwise.kcloud.batch.service.BaseTaskService;
import com.kayakwise.kcloud.batch.service.SliceJobCutter;
import com.kayakwise.kcloud.db.util.ParamMap;
import com.kayakwise.kcloud.sequence.base.service.SequenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;


@Slf4j
@Component
@Scope("prototype")
public class AutoTransmissionService extends BaseTaskService<PubReq, PubResp> {

    @Autowired
    public CreateTaskService createTaskService;
    @Autowired
    public ComnDao comnDao;
    @Autowired
    public SliceJobCutter sliceJobCutter;
    @Autowired
    private SequenceService sequenceService;
    @Autowired
    private T8portInfoDao t8portInfoDao;
    @Override
    protected void doCheckParams(PubReq request) throws Exception {
        log.info(" ###### 参数校验 ");
        DbopChange.setDataSourceName("default");
        sequenceService.getSequence("kcloud_batch_execid");
    }

    @Override
    protected void doCheckBusiness(PubReq request) throws Exception {
        log.info(" ###### 业务校验 ");
    }

    @Override
    protected RegistClearTaskPojo taskRegist(TaskRegeditReq taskRegeditReq) throws Exception {
        log.info(" ###### 任务注册");
        return createTaskService.createSystemTask(taskRegeditReq);
    }

    /**
     * 数据验证
     */
    protected void dataModeCheck(PubReq request) throws Exception{
        log.info("---------- 任务: " + request.getTaskId() +" 数据验证开始 Start -----------");


        log.info("---------- 任务: " + request.getTaskId() +" 数据验证结束 End-----------");
    }

    /**
    * @功能描述:自动生成产品信披规则入口
    * @params:[request]
    * @return:void
    * @Athor:ouyifan
    * @date:2022/8/26
    */
    @StepNo(stepNo = 1)
    protected BatchTaskStepReuslt autoTransmission(PubReq request) throws Exception{
        log.info(request.getTaskId() + " Request: {}", request);
        Map<String, Object> map = new HashMap<>();
        map.put("pid",request.getTaskId());
        FetcherData fetcherData = new FetcherData(map, T8PortInfoModel.class);
        SqlResult<T8PortInfoModel> res = t8portInfoDao.queryPortAndField(fetcherData);
        T8PortInfoModel t8PortInfoModel = res.getRows().get(0);
        SliceJobSplitBO bo = new SliceJobSplitBO(){{
            //分片表
            setTableName(t8PortInfoModel.getSliceTableName());

            //分配数据库
            setDatasource(t8PortInfoModel.getSliceDataSource());

            //分片键值 根据键值不同进行分片
            //PS: 20220101 20220102 20220103 20220104 20220105 20230501 20230502
            //上面的数据则分成7个片，分片键值需要合理分配
            setSplitKey(t8PortInfoModel.getSplitKey());

            //分片条件
            setSplitWhere(t8PortInfoModel.getSplitWhere());

            //每片最大数量
            setClearRecordCustNum(Integer.valueOf(t8PortInfoModel.getSliceCount()));

            ParamMap map = new ParamMap().on("deal_date",request.getTaskDate()).on("table_name",t8PortInfoModel.getSliceTableName());
            //分片条件赋值
            setSplitWhereParams(map);
            setSliceBusinessParams(JSONObject.toJSONString(map));

        }};
        KbatchTaskExec kbatchTaskExec = new KbatchTaskExec();
        kbatchTaskExec.setTaskExecid(request.getTaskExecid());
        kbatchTaskExec.setTaskId(request.getTaskId());
        KbatchTaskStepExec kbatchTaskStepExec = new KbatchTaskStepExec();
        kbatchTaskStepExec.setStepNo(1);
        kbatchTaskStepExec.setTaskExecid(request.getTaskExecid());
        kbatchTaskStepExec.setTaskId(request.getTaskId());
        List<String> list = new ArrayList();
        list.add(t8PortInfoModel.getSliceDataSource());
        Integer num = sliceJobCutter.dataBusiSplit(kbatchTaskExec,kbatchTaskStepExec,bo,list);

        BatchTaskStepReuslt batchTaskStepReuslt = new BatchTaskStepReuslt(BatchStepStatusControlEnum.NORMAL,"aaa",num);

        return batchTaskStepReuslt;
    }




}