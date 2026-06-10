package com.kayak.pms.opFlow.engine.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.helper.StringHelper;
import com.kayak.pms.opFlow.engine.cache.Cache;
import com.kayak.pms.opFlow.engine.cache.CacheManager;
import com.kayak.pms.opFlow.engine.dao.ProcessDao;
import com.kayak.pms.opFlow.engine.dao.ProcessInstanceDao;
import com.kayak.pms.opFlow.engine.entity.Process;
import com.kayak.pms.opFlow.engine.entity.vo.FormConfVO;
import com.kayak.pms.opFlow.engine.exception.WorkflowException;
import com.kayak.pms.opFlow.engine.model.NodeModel;
import com.kayak.pms.opFlow.engine.model.ProcessModel;
import com.kayak.pms.opFlow.engine.model.TaskModel;
import com.kayak.pms.opFlow.engine.parser.ModelParser;
import com.kayak.xsql.autoid.DefaultAutoId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by daniel on 20/03/2017.
 */
@Service
@APIDefine(desc = "操作流流程图", model = Process.class)
public class ProcessService {

    private static final Logger logger = LoggerFactory.getLogger(ProcessService.class);
    private static final String CACHE_NAME = "process.name";
    private static final String CACHE_ENTITY = "process.entity";
    private static final String DEFAULT_SEPARATOR = ".";
    private final boolean cacheEnable;

    //缓存管理器, 用于获取缓存
    private CacheManager cacheManager;

    /**
     * 实体cache(key=流程name,value=entity对象(entity))
     */
    private Cache<String, Process> entityCache;

    /**
     * 名称cache(key=id,value=name对象)
     */
    private Cache<String, String> nameCache;

    @Autowired
    ProcessDao processDao;
    @Autowired
    ProcessInstanceDao processInstanceDao;

    @API(desc = "保存流程图", auth = APIAuth.NO)
    public String saveProcess(SqlParam<Process> params) throws Exception {
        Process process = this.getProcess(params);
        if (processDao.countProcess(params) != 0) {
            return RequestSupport.updateReturnJson(false, "流程英文名和中文名必须唯一", null).toString();
        }
        try {
            // 解析并校验任务节点
            ModelParser.validate(process.getProcessModel());
            // 生成自增序列
            process.setProcessId(new DefaultAutoId().newId("opf_process", 8));
            // 保存入库
            processDao.saveProcess(params);
            // 缓存流程图对象
            cache(process);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RequestSupport.updateReturnJson(false, "添加流程失败" + e.getMessage(), null).toString();
        }
        return RequestSupport.updateReturnJson(true, "新增流程成功", null).toString();
    }

    @API(desc = "更新流程图", auth = APIAuth.NO)
    public String updateProcess(SqlParam<Process> params) throws Exception {
        // 解析并校验任务节点
        Process parseProcess = this.getProcess(params);
        ModelParser.validate(parseProcess.getProcessModel());

        if (processDao.countProcess(params) != 0) {
            return RequestSupport.updateReturnJson(false, "流程中文名必须唯一", null).toString();
        }
        Process process = processDao.getByMaxVersion(params);
        if (process == null) {
            return RequestSupport.updateReturnJson(false, "该流程不存在或已被删除", null).toString();
        } else if (process.getVersion().equals(params.getModel().getVersion())) {
            // 发布后的版本，首次修改时，新开一个流程
            process.setUpdateUser(SysUtil.getLoginUserid());
            process.setVersion(process.getVersion()+1);
            process.setStatus("0");
            process.setJson(params.getModel().getJson());
            processDao.insertNew(process);
        } else {
            // 已经修改过，则拿修改过的更新
            params.getModel().setUpdateUser(SysUtil.getLoginUserid());
            params.getModel().setVersion(process.getVersion());
            processDao.updateProcess(params);
        }
        return RequestSupport.updateReturnJson(true, "更新流程成功", null).toString();
    }


    @API(desc = "查询最新流程图", auth = APIAuth.NO)
    public SqlResult<Process> listAllProcess(SqlParam<Process> params) throws Exception {
        params.setMakeSql(true);
        return processDao.listAllProcess(params);
    }

    @API(desc = "查询生效的流程图", auth = APIAuth.NO)
    public SqlResult<Process> listEffectiveProcess(SqlParam<Process> params) throws Exception {
        params.setMakeSql(true);
        return processDao.listEffectiveProcess(params);
    }

    @API(desc = "发布流程图", auth = APIAuth.NO)
    public String deploy(SqlParam<Process> params) throws Exception {
        Process process = processDao.getByMaxVersion(params);
        if (process == null) {
            return RequestSupport.updateReturnJson(false, "流程不存在或已被删除", null).toString();
        } else if ("1".equals(process.getStatus())) {
            return RequestSupport.updateReturnJson(false, "流程已发布，请勿重复发布", null).toString();
        }
        process.setCreateUser(SysUtil.getLoginUserid());
        process.setStatus("1");
        processDao.deployProcess(process);
        return RequestSupport.updateReturnJson(true, "流程发布成功", null).toString();
    }

    @API(desc = "删除流程图", auth = APIAuth.NO)
    public String removeProcess(SqlParam<Process> params) throws Exception {
        processDao.removeProcess(params);
        // 清除缓存
        this.clear(params.getModel());
        return RequestSupport.updateReturnJson(true, "流程删除成功", null).toString();
    }

    @API(desc = "查询流程下一个审批节点", auth = APIAuth.NO)
    public String findNextNode(SqlParam<Process> params) throws Exception {
        Process process = params.getModel();
        Process processByVersion = processDao.getProcessByVersion(process.getProcessId(), process.getVersion().toString());
        ProcessModel processModel = ModelParser.parse(processByVersion.getJson());
        NodeModel node = processModel.getNode(process.getName());
        if (node != null && !node.getOutputs().isEmpty()) {
            List<TaskModel> taskModels = node.getNextTaskModels();
            if (taskModels.size() > 0) {
                TaskModel taskModel = taskModels.get(0);
                HashMap<String, Object> map = new HashMap<>();
                taskModel.setInputs(null);
                taskModel.setOutputs(null);
                map.put("nextNode", taskModel);
                return RequestSupport.updateReturnJson(true, "", map).toString();
            }
        }
        return RequestSupport.updateReturnJson(true, "", null).toString();
    }

    private Process getProcess(SqlParam<Process> param) {
        Process process = param.getModel();
        String processData = param.getModel().getJson();
        ProcessModel processModel = ModelParser.parse(processData);
        process.setProcessModel(processModel);
        process.setJson(processData);
        process.setName(processModel.getName());
        process.setCreator(SysUtil.getLoginUserid());
        process.setVersion(0);
        process.setStatus("1");
        return process;
    }

    public Process getProcessById(String processId) throws Exception {
        Process entity = null;
        String processName;
        Cache<String, String> nameCache = ensureAvailableNameCache();
        Cache<String, Process> entityCache = ensureAvailableEntityCache();
        if (nameCache != null && entityCache != null) {
            processName = nameCache.get(processId);
            if (StringHelper.isNotEmpty(processName)) {
                entity = entityCache.get(processName);
            }
        }
        if (entity != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("obtain process[id={}] from cache.", processId);
            }
            return entity;
        }
        //从数据库获取
        entity = processDao.getProcessById(processId);
        if (entity != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("obtain process[id={}] from database.", processId);
            }
            entity.setProcessModel(ModelParser.parse(entity.getJson()));
            cache(entity);
        }
        return entity;
    }

    public ProcessService() {
        cacheEnable = true;
    }

    public void deployProcess(String processId) throws Exception {
        Process process = getProcessById(processId);
        Process oldProcess = new Process();
        //获取最大版本号的工作流
        Process maxVersionProcess = getProcessByMaxVersionProcess(process.getName());
        //修改工作流的id以及版本号后保存
        process.setId(StringHelper.getPrimaryKey());
        if (maxVersionProcess == null) {//默认版本号为1，其它的最大版本号加1
            process.setVersion(1);
        } else {
            process.setVersion(maxVersionProcess.getVersion() + 1);

            //用于清楚久有缓存
            oldProcess.setId(maxVersionProcess.getId());
            oldProcess.setVersion(maxVersionProcess.getVersion());
        }
        processDao.insertNew(process);
        //清除旧有对象，保存新对象
        clear(oldProcess);
        cache(process);
    }

    public Process getProcessByMaxVersionProcess(String processId) throws Exception {
        Process maxVersionProcess = processDao.getProcessByMaxVersion(processId);
        if (maxVersionProcess == null) {
            return null;
        }
        return getProcessById(maxVersionProcess.getId());//这里再获取一次是为了缓存并解析ProcessModel
    }

    public List<FormConfVO> getFormConf(String processName) throws Exception {
        Process process = getProcessByMaxVersionProcess(processName);
        if (process == null) {
            throw new WorkflowException("工作流[" + processName + "]不存在");
        }

        List<TaskModel> models = process.getProcessModel().getModels(TaskModel.class);
        List<FormConfVO> result = new ArrayList<FormConfVO>();
        for (TaskModel model : models) {
            FormConfVO formConfVO = new FormConfVO(model.getFormId(), model.getFormUrl(), model.getBtns());
            formConfVO.setTaskName(model.getName());
            result.add(formConfVO);
        }
        return result;
    }

    private void cache(Process entity) {
        if (!cacheEnable) {
            return;
        }
        Cache<String, String> nameCache = ensureAvailableNameCache();
        Cache<String, Process> entityCache = ensureAvailableEntityCache();

        Integer version = entity.getVersion();
        if (version == null) {//只有版本号存在的时候才缓存
            return;
        }
        String processName = entity.getName() + DEFAULT_SEPARATOR + entity.getVersion();
        if (nameCache != null && entityCache != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("cache process id is[{}],name is[{}]", entity.getId(), processName);
            }
            entityCache.put(processName, entity);
            nameCache.put(entity.getId(), processName);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("no cache implementation class");
            }
        }
    }

    private void clearAll() {
        ensureAvailableNameCache();
        ensureAvailableEntityCache();
        if (nameCache != null) {
            nameCache.clear();
        }
        if (entityCache != null) {
            entityCache.clear();
        }
    }

    private void clear(Process entity) {
        if (!cacheEnable) {
            return;
        }
        Cache<String, String> nameCache = ensureAvailableNameCache();
        Cache<String, Process> entityCache = ensureAvailableEntityCache();
        if (nameCache != null && entityCache != null) {
            if (entity.getId() != null) {
                String name = nameCache.remove(entity.getId());
                if (StringHelper.isNotEmpty(name)) {
                    entityCache.remove(name);
                }
            }
        }
    }

    private Cache<String, String> ensureAvailableNameCache() {
        Cache<String, String> nameCache = ensureNameCache();
        if (nameCache == null && this.cacheManager != null) {
            nameCache = this.cacheManager.getCache(CACHE_NAME);
        }
        return nameCache;
    }

    private Cache<String, Process> ensureAvailableEntityCache() {
        Cache<String, Process> entityCache = ensureEntityCache();
        if (entityCache == null && this.cacheManager != null) {
            entityCache = this.cacheManager.getCache(CACHE_ENTITY);
        }
        return entityCache;
    }

    public Cache<String, String> ensureNameCache() {
        return nameCache;
    }

    public Cache<String, Process> ensureEntityCache() {
        return entityCache;
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
}
