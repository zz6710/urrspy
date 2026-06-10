import globalContents from "./globalContent.js";
import Tools from '@/utils/tools.js';

const BATCH_TASK_STATUS =  globalContents.BATCH_TASK_STATUS;
let ta5003js = {};

ta5003js.execDisableCondition = function(row){
    if(row.execStatus==BATCH_TASK_STATUS.NON_EXECUTION || row.execStatus==BATCH_TASK_STATUS.FAILED || row.execStatus==BATCH_TASK_STATUS.SLICE_FAILED){
        //未执行或执行失败的任务可以发起执行
        return false;
    }
    if((row.execStatus==BATCH_TASK_STATUS.SUCCESS || row.execStatus==BATCH_TASK_STATUS.SKIP || row.execStatus==BATCH_TASK_STATUS.TERMINATION) && row.canAgain=='1'){
        //可重复执行的任务，执行状态为：5成功/7跳过/8终止，也是可以执行的
        return false;
    }
    return true;
};

/**
 * 【跳过】按钮的是否展示条件
 */
ta5003js.skipShowCondition = function(row){
    //销售商任务允许跳过
    if((row.taskType==globalContents.BATCH_TASK_TYPE.DISTRIBUTOR_IMP || row.taskType==globalContents.BATCH_TASK_TYPE.DISTRIBUTOR_EXP) 
        && (row.execStatus==BATCH_TASK_STATUS.NON_EXECUTION || row.execStatus==BATCH_TASK_STATUS.FAILED || row.execStatus==BATCH_TASK_STATUS.SLICE_FAILED)){
        return true;
    }

    //“清算结果检查”允许跳过
    if(row.taskId == "C020" 
        && (row.execStatus==BATCH_TASK_STATUS.NON_EXECUTION || row.execStatus==BATCH_TASK_STATUS.FAILED || row.execStatus==BATCH_TASK_STATUS.SLICE_FAILED)){
        return true;
    }

    return false;
};

/**
 * 回滚 销售商文件推送任务
 */
ta5003js.rollbackShowCondition3 = function(row){
    if(row.taskId=='F007' && (row.execStatus==BATCH_TASK_STATUS.SUCCESS || row.execStatus==BATCH_TASK_STATUS.FAILED || row.execStatus==BATCH_TASK_STATUS.SKIP
        || row.execStatus==BATCH_TASK_STATUS.TERMINATION || row.execStatus==BATCH_TASK_STATUS.SLICE_FAILED)){
        return true;
    }
    if(row.taskId=='F012' && (row.execStatus==BATCH_TASK_STATUS.SUCCESS || row.execStatus==BATCH_TASK_STATUS.FAILED || row.execStatus==BATCH_TASK_STATUS.SKIP
        || row.execStatus==BATCH_TASK_STATUS.TERMINATION || row.execStatus==BATCH_TASK_STATUS.SLICE_FAILED)){
        return true;
    }
    return false;
};

/**
 * 【重跑】按钮的disable条件
 */
ta5003js.rollbackShowCondition = function(row){
    if(row.taskType== globalContents.BATCH_TASK_TYPE.PROD && (row.execStatus==BATCH_TASK_STATUS.SUCCESS|| row.execStatus==BATCH_TASK_STATUS.FAILED || row.execStatus==BATCH_TASK_STATUS.SKIP 
        || row.execStatus==BATCH_TASK_STATUS.TERMINATION || row.execStatus==BATCH_TASK_STATUS.SLICE_FAILED)){
        return true;
    }
    return false;
};
/**
 * 文件预处理重跑
 */
ta5003js.rollbackShowCondition2 = function(row){
    if(row.taskId=='F010' && (row.execStatus==BATCH_TASK_STATUS.SUCCESS || row.execStatus==BATCH_TASK_STATUS.FAILED || row.execStatus==BATCH_TASK_STATUS.SKIP
        || row.execStatus==BATCH_TASK_STATUS.TERMINATION || row.execStatus==BATCH_TASK_STATUS.SLICE_FAILED)){
        return true;
    }
    return false;
};

ta5003js.invokeClear = function(row){

    Tools.confirm(() => {

        this.httpUtil.ajax({
            url: "/server/json/ta-batch/C999", // 调用后台，采用格式为：/server/服务名/交易接口
            params: row
        }).then(data => {
            console.log(data);
            let rows = data.rows;
        })
    },
    "确定执行该清算任务?"
    )

    return false;
};

ta5003js.rollBackClear = function(row){
    Tools.confirm(() => {
        this.httpUtil.ajax({
            url: "/server/json/ta-batch/CS001", // 调用后台，采用格式为：/server/服务名/交易接口
            params: row
        }).then(data => {
            console.log(data);
            let rows = data.rows;
        })
    },
    "确定回滚该清算任务?"
    )

    return false;
};

ta5003js.rollBackClear2 = function(row){
    Tools.confirm(() => {
        this.httpUtil.ajax({
            url: "/server/json/ta-batch/CS002", // 调用后台，采用格式为：/server/服务名/交易接口
            params: row
        }).then(data => {
            console.log(data);
            let rows = data.rows;
        })
    },
    "确定回滚该清算任务?"
    )

    return false;
};

ta5003js.rollBackClear3 = function(row){
    Tools.confirm(() => {
        this.httpUtil.ajax({
            url: "/server/json/ta-batch/CS000", // 调用后台，采用格式为：/server/服务名/交易接口
            params: row
        }).then(data => {
            console.log(data);
            let rows = data.rows;
        })
    },
    "确定回滚该清算任务?"
    )

    return false;
};
ta5003js.checkTaskExecid=function(row){
  if(row.taskExecid && row.buttonIsDisplay != '0'){//暂时写1为了调试页面
    return true;
  }
  return false
};
ta5003js.checkTaskId=function(row){
  if(row.taskExecid && row.buttonIsDisplay != '0' &&row.taskId=='C017'){//暂时写1为了调试页面
    return true;
  }
  return false
};
export default ta5003js;
