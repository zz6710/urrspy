package com.kayak.config.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.clear.constants.BatchTaskStatus;
import com.kayak.config.dao.Ta5014DetailDao;
import com.kayak.config.model.Ta5014Detail;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;

/**
 * 文件名: Ta5003Service.java
 * 描述:  清算列表
 * 创建人: xiaofu
 * 创建时间:2020年4月26日下午3:18:56
 */
@Service
@APIDefine(desc = "清算详情列表", model = Ta5014Detail.class)
public class Ta5014DetailService {

    @Autowired
    private Ta5014DetailDao ta5014DetailDao;

    @API(desc = "查询清算任务信息", auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<Ta5014Detail> findTa5014Detail(SqlParam<Ta5014Detail> params) throws Exception {

        //是否自动追加参数,这里SQL比较复杂，自己拼接好SQL，不需要追加参数了
        params.setMakeSql(false);
        //如果是清算组类型为产品或者销售商类,有叶子节点.先查第一层
        String                  execGridId = "(";
        Ta5014Detail            model      = params.getModel();
        String                  type       = model.getExecTaskType();
        SqlResult<Ta5014Detail> rootData   = null;
//        if (BatchTaskType.isProdGroup(type) || BatchTaskType.isDistrGroup(type)) {
//            Map<String, Object> map = new HashMap<>();
//            rootData = ta5014DetailDao.queryRootData(params);
//            map.put("queryTaskDate", model.getQueryTaskDate());
//            map.put("taskGroup", model.getTaskGroup());
//            //存下页子所查到execGridId
//            for (Ta5014Detail item : rootData.getRows()) {
//                if ("(".equals(execGridId)) {
//                    execGridId = execGridId + "'" + item.getExecGridId() + "'";
//                } else {
//                    execGridId = execGridId + ",'" + item.getExecGridId() + "'";
//                }
//            }
//            execGridId = execGridId + ")";
//            FetcherData<Ta5014DetailRootStatus> paramsTemp   = new FetcherData<Ta5014DetailRootStatus>(map, Ta5014DetailRootStatus.class);
//            SqlResult<Ta5014DetailRootStatus>   statusResult = ta5014DetailDao.queryRootStatus(paramsTemp);
//            Map<String, Ta5014DetailRootStatus> rootStatus   = new HashMap<>();
//            statusResult.getRows().forEach(row -> {
//                rootStatus.put(row.getMapKey(), row);
//            });
//            rootData.getRows().forEach(row -> {
//                if (StringUtils.isBlank(row.getParentExecGridId())) {
//                    Ta5014DetailRootStatus item = rootStatus.get(row.getExecGridId());
//                    if (item != null) {
//                        String rtnDesc = "失败:" + item.getFailure() + ";成功:"
//                                + item.getSuccess() + ";未执行:" + item.getNoExcute() + ";未注册:" + item.getNoRegistry();
//                        row.setRtnDesc(rtnDesc);
//                        if (Integer.parseInt(item.getFailure()) > 0) {
//                            row.setExecStatus("6");
//                        } else if (Integer.parseInt(item.getExcuting()) > 0) {
//                            row.setExecStatus("2");
//                        } else if (Integer.parseInt(item.getNoExcute()) > 0) {
//                            row.setExecStatus("0");
//                        } else if (Integer.parseInt(item.getSuccess()) > 0) {
//                            row.setExecStatus("5");
//                        }
//                    }
//                }
//            });
//        }
        SqlResult<Ta5014Detail> result = ta5014DetailDao.queryClearTaskExecInfo(params, execGridId);
        //将任务pre_task为null的置为"",并排序
        result.getRows().forEach(res -> {
            if (res.getPreTaskId() == null){
                res.setPreTaskId("");
            }
        });
        result.getRows().sort(new Comparator<Ta5014Detail>() {
            @Override
            public int compare(Ta5014Detail o1, Ta5014Detail o2) {
                return o1.getPreTaskId().split("|").length - o2.getPreTaskId().split("|").length;
            }
        });
//        if (rootData != null && (BatchTaskType.isProdGroup(type) || BatchTaskType.isDistrGroup(type))) {
//            result.getRows().addAll(rootData.getRows());
//            result.setResults(rootData.getResults());
//        }
        return result;
    }

    @API(desc = "更新清算执行状态为跳过", auth = APIAuth.NO)
    public String updateStatusSkip(SqlParam<Ta5014Detail> params) throws Exception {
        params.setMakeSql(false);
        params.getModel().setExecStatus(BatchTaskStatus.SKIP);
        ta5014DetailDao.updateExecStatus(params.getModel(), null);
        return RequestSupport.updateReturnJson(true, "修改成功", null).toString();
    }

    @API(desc = "查询回滚任务", auth = APIAuth.NO)
    public SqlResult<Ta5014Detail> queryRevocation(SqlParam<Ta5014Detail> params) throws Exception {
        params.setMakeSql(false);
        return ta5014DetailDao.queryRevocation(params);
    }

    @API(desc = "回滚任务", auth = APIAuth.NO)
    public String rollBackClearTask(SqlParam<Ta5014Detail> params) throws Exception {
        params.setMakeSql(false);
        params.getModel().setExecStatus(BatchTaskStatus.NON_EXECUTION);
        ta5014DetailDao.rollBackClearTask(params);
        return RequestSupport.updateReturnJson(true, "回滚成功", null).toString();
    }

    @API(desc = "跳过卡批任务", auth = APIAuth.NO)
    public String updateStatusContinue(SqlParam<Ta5014Detail> params) throws Exception {
        params.setMakeSql(false);
        params.getModel().setExecStatus(BatchTaskStatus.SUCCESS);
        ta5014DetailDao.updateExecStatusOfBlockTask(params.getModel(), null);
        return RequestSupport.updateReturnJson(true, "修改成功", null).toString();
    }


}
