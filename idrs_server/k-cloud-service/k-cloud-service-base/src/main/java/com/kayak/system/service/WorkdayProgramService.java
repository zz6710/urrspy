package com.kayak.system.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.graphql.model.FetcherData;
import com.kayak.system.dao.WorkdayDao;
import com.kayak.system.model.WorkdayProgram;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kayak.core.system.constants.WorkdayConstants.SYSTEM_WORKDAY;

@Service
@Slf4j
@APIDefine(desc = "工作日方案服务", model = WorkdayProgram.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WorkdayProgramService {

    private final WorkdayDao workdayDao;


    @API(desc = "查询工作日方案列表", auth = APIAuth.YES, operation = APIOperation.SELECT)
    public SqlResult<WorkdayProgram> find1(SqlParam<WorkdayProgram> params) throws Exception {
        return find(params);
    }

    @API(desc = "查询工作日方案列表", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<WorkdayProgram> find(SqlParam<WorkdayProgram> params) throws Exception {
        params.setMakeSql(true);
        return workdayDao.findProgram(params);
    }

//    @API(desc = "新增工作日方案", operation = APIOperation.INSTER)
//    public String add(SqlParam<WorkdayProgram> params) throws Exception {
//        checkUniquenessSystemWorkdayProgram(params.getModel());
//        boolean result = workdayDao.addProgram(params) > 0;
//        if (!result) {
//            throw new PromptException("新增失败");
//        }
//        return RequestSupport.updateReturnJson(true, "新增成功", null).toString();
//    }

    @API(desc = "删除工作日方案", auth = APIAuth.YES, operation = APIOperation.DELETE)
    public String delete(SqlParam<WorkdayProgram> params) throws Exception {
        workdayDao.delProgram(params);
        return RequestSupport.updateReturnJson(true, "删除成功", null).toString();
    }

    @API(desc = "修改工作日方案", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public String update(SqlParam<WorkdayProgram> params) throws Exception {
        checkUniquenessSystemWorkdayProgram(params.getModel());
        workdayDao.updateProgram(params);
        return RequestSupport.updateReturnJson(true, "修改成功", null).toString();
    }

    /**
     * //检查是否只有一个系统工作日方案，不是抛出异常
     * 检查是否存在该工作其方案
     * @param program
     * @throws Exception
     */
    private void checkUniquenessSystemWorkdayProgram(WorkdayProgram program) throws Exception {
       // if (SYSTEM_WORKDAY.equals(program.getPgmtype())) {
            List<WorkdayProgram> systemWorkdayPrograms = this.getProgramByType(program.getPgmtype());

            if (CollectionUtils.isEmpty(systemWorkdayPrograms)) {
                return;
            }
            if (systemWorkdayPrograms.size() > 1) {
                throw new PromptException("只能存在一个类型工作日方案。");
            }
            WorkdayProgram systemWorkdayProgram = systemWorkdayPrograms.get(0);
            if (!program.getPgmno().equals(systemWorkdayProgram.getPgmno())) {
                throw new PromptException("只能存在一个类型工作日方案。");
            }
     //   }
    }

    private List<WorkdayProgram> getProgramByType(String pgmtype) throws Exception {
        Map<String, Object> param = new HashMap<>(1);
        param.put("pgmtype", pgmtype);
        FetcherData<WorkdayProgram> findProgram = new FetcherData<>(param, WorkdayProgram.class);
        findProgram.setMakeSql(true);
        SqlResult<WorkdayProgram> program = workdayDao.findProgram(findProgram);
        return program.getRows();
    }
}
