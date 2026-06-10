package com.kayak.bak.business.service;

import cn.hutool.core.util.ObjectUtil;
import com.kayak.bak.business.dao.BakPhaseDao;
import com.kayak.bak.core.bak.CollectionManage;
import com.kayak.bak.core.bak.TableBak;
import com.kayak.bak.core.bak.TableDeleteService;
import com.kayak.bak.model.dto.BakTaskDTO;
import com.kayak.bak.model.po.SysBakCollectionPO;
import com.kayak.bak.model.po.SysBakConfigPO;
import com.kayak.base.dao.ComnDao;
import com.kayak.clear.req.PubReq;
import com.kayak.clear.resp.PubResp;
import com.kayak.clear.service.pub.CreateTaskService;
import com.kayak.clear.utils.TaskUtil;
import com.kayakwise.kcloud.batch.annotation.StepNo;
import com.kayakwise.kcloud.batch.model.bo.RegistClearTaskPojo;
import com.kayakwise.kcloud.batch.model.req.TaskRegeditReq;
import com.kayakwise.kcloud.batch.service.BaseTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class BakPhaseService extends BaseTaskService<PubReq, PubResp> {

    @Autowired
    public CreateTaskService createTaskService;
    @Autowired
    public ComnDao comnDao;

    @Resource
    private BakPhaseDao bakPhaseDao;
    @Resource
    private CollectionManage tableSub;
    @Resource
    private TableBak tableBak;
    @Resource
    private TableDeleteService tableDeleteService;

    @Override
    protected void doCheckParams(PubReq request) throws Exception {
        log.info(" ###### 参数校验 ");
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
     * 备份数据库建表、数据量检查
     * @param request
     * @throws Exception
     */
    @StepNo(stepNo = 1)
    protected void bakStepA(PubReq request) throws Exception{
        log.info("----------归档备份任务开始, 任务id："+ request.getTaskId() +"-----------");
        //获取当前系统工作日
        String taskDate = request.getTaskDate();
        //获取配置备份列表
        List<BakTaskDTO> bakTaskDTOList = getBakTaskDTOList(taskDate);
        ExecutorService taskPools = Executors.newFixedThreadPool(3);
        //TODO: 2023/5/31 暂定为线性执行备份任务
        for (BakTaskDTO dto : bakTaskDTOList) {
            //取本次备份用仓库
            dto = tableSub.subTable(dto);
            BakTaskDTO finalDto = dto;
            taskPools.execute(() -> {
                //数据备份
                try {
                    tableBak.subTable(finalDto);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
        taskPools.shutdown();
        taskPools.awaitTermination(14400, TimeUnit.SECONDS);
        log.info("----------归档备份任务结束, 任务id："+ request.getTaskId() +"-----------");
    }

//    @StepNo(stepNo = 2)
    protected void bakStepB(PubReq request) throws Exception{
        log.info("----------删除任务开始, 任务id："+ request.getTaskId() +"-----------");
        //获取当前系统工作日
        String taskDate = request.getTaskDate();
        // 执行删除任务,暂定为单线程
        tableDeleteService.execute(taskDate);
        log.info("----------删除任务结束, 任务id："+ request.getTaskId() +"-----------");
    }

    /**
     * 获取归档任务配置列表
     * @param workDate
     * @throws Exception
     */
    private List<BakTaskDTO> getBakTaskDTOList(String workDate) throws Exception {
        //查询需要备份的表配置
        List<SysBakConfigPO> sysBakConfigPOS = bakPhaseDao.getSysBakConfig(workDate);
        if (ObjectUtil.isEmpty(sysBakConfigPOS)) {
            return Arrays.asList();
        }
        //查询备份仓库集合表信息
        List<SysBakCollectionPO> sysBakCollectionPOS = bakPhaseDao.getSysBakCollection(sysBakConfigPOS);
        //生成备份任务列表
        return BakTaskDTO.dto().initList(sysBakConfigPOS, sysBakCollectionPOS);
    }
}
