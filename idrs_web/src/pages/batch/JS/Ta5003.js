import globalContents from "./globalContent.js";
import Tools from '@/utils/tools.js';

const BATCH_TASK_STATUS =  globalContents.BATCH_TASK_STATUS;
let ta5003js = {};

ta5003js.execDisableCondition = function(row){
    if(row.execStatus===BATCH_TASK_STATUS.NON_EXECUTION || row.execStatus===BATCH_TASK_STATUS.FAILED || row.execStatus===BATCH_TASK_STATUS.SLICE_FAILED){
        //未执行或执行失败的任务可以发起执行
        return false;
    }
    if((row.execStatus===BATCH_TASK_STATUS.SUCCESS || row.execStatus===BATCH_TASK_STATUS.SKIP || row.execStatus===BATCH_TASK_STATUS.TERMINATION) && row.canAgain==='1'){
        //可重复执行的任务，执行状态为：5成功/7跳过/8终止，也是可以执行的
        return false;
    }
    return true;
};

/*
* 继续任务
* */
ta5003js.continueExcute = function (row) {
  Tools.confirm(() => {
    this.httpUtil.comnQuery({
      action: "/Ta5014Detail.updateStatusContinue.json", // 调用后台，采用格式为：/server/服务名/交易接口
      params: row
    }).then(data => {
      let rows = data.rows;
      Tools.alert("跳过清算任务卡批成功");
    })
  }, "确定跳过清算任务卡批?")
  return false;
}

/*
* [跳过] 跳过按钮点击事件
* */
ta5003js.updateStatusSkip = function (row) {
  Tools.confirm(() => {
    this.httpUtil.comnQuery({
      action: "/Ta5014Detail.updateStatusSkip.json", // 调用后台，采用格式为：/server/服务名/交易接口
      params: row
    }).then(data => {
      let rows = data.rows;
      Tools.alert("跳过任务成功");
    })
  }, "确定跳过该清算任务?")
  return false;
}

/**
 * 【跳过】按钮的是否展示条件
 */
ta5003js.skipShowCondition = function(row){
    //销售商任务允许跳过
    if((row.taskType===globalContents.BATCH_TASK_TYPE.DISTRIBUTOR_IMP || row.taskType===globalContents.BATCH_TASK_TYPE.DISTRIBUTOR_EXP)
        && (row.execStatus===BATCH_TASK_STATUS.NON_EXECUTION || row.execStatus===BATCH_TASK_STATUS.FAILED || row.execStatus===BATCH_TASK_STATUS.SLICE_FAILED)){
        return true;
    }

    // 业务批检允许跳过
    if((row.taskId === "C022" || row.taskId === "C500" || row.taskId === "C501")
            && (row.execStatus===BATCH_TASK_STATUS.NON_EXECUTION || row.execStatus===BATCH_TASK_STATUS.FAILED || row.execStatus===BATCH_TASK_STATUS.SLICE_FAILED)){
            return true;
    }

    return false;
};

/**
 * 回滚 产品后批次和系统预检,以及文件推送
 */
ta5003js.rollbackShowCondition4 = function(row){
    if(row.taskId==='C020' || row.taskId==='C155' || row.taskId==='C156' || row.taskId==='C157' || row.taskId==='C158'
    || row.taskId==='C022' || row.taskId==='C017' || row.taskId==='C215' || row.taskId==='C500' || row.taskId==='C501'
    || row.taskId==='C319' || row.taskId==='C007' || row.taskId==='C012' || row.taskId==='C222'){
        if(row.execStatus===BATCH_TASK_STATUS.SUCCESS || row.execStatus===BATCH_TASK_STATUS.FAILED || row.execStatus===BATCH_TASK_STATUS.SKIP
         || row.execStatus===BATCH_TASK_STATUS.TERMINATION || row.execStatus===BATCH_TASK_STATUS.SLICE_FAILED || row.execStatus===BATCH_TASK_STATUS.BLOCK){
            return true;
         }
    }
    return false;
};

/**
 * 回滚 销售商文件推送任务
 */
ta5003js.rollbackShowCondition3 = function(row){
    if(row.taskId==='F007' && (row.execStatus===BATCH_TASK_STATUS.SUCCESS || row.execStatus===BATCH_TASK_STATUS.FAILED || row.execStatus===BATCH_TASK_STATUS.SKIP
        || row.execStatus===BATCH_TASK_STATUS.TERMINATION || row.execStatus===BATCH_TASK_STATUS.SLICE_FAILED || row.execStatus===BATCH_TASK_STATUS.BLOCK)){
        return true;
    }
    if(row.taskId==='F012' && (row.execStatus===BATCH_TASK_STATUS.SUCCESS || row.execStatus===BATCH_TASK_STATUS.FAILED || row.execStatus===BATCH_TASK_STATUS.SKIP
        || row.execStatus===BATCH_TASK_STATUS.TERMINATION || row.execStatus===BATCH_TASK_STATUS.SLICE_FAILED || row.execStatus===BATCH_TASK_STATUS.BLOCK)){
        return true;
    }
    return false;
};

/**
 * 【重跑】按钮的disable条件
 */
ta5003js.rollbackShowCondition = function(row){
    if(row.taskType=== globalContents.BATCH_TASK_TYPE.PROD && (row.execStatus===BATCH_TASK_STATUS.SUCCESS|| row.execStatus===BATCH_TASK_STATUS.FAILED || row.execStatus===BATCH_TASK_STATUS.SKIP
        || row.execStatus===BATCH_TASK_STATUS.TERMINATION || row.execStatus===BATCH_TASK_STATUS.SLICE_FAILED || row.execStatus===BATCH_TASK_STATUS.BLOCK)){
        return true;
    }
    if ((row.taskId === 'C209'||row.taskId === 'C401') && (row.execStatus===BATCH_TASK_STATUS.SUCCESS || row.execStatus===BATCH_TASK_STATUS.FAILED || row.execStatus===BATCH_TASK_STATUS.SKIP
      || row.execStatus===BATCH_TASK_STATUS.TERMINATION || row.execStatus===BATCH_TASK_STATUS.SLICE_FAILED || row.execStatus===BATCH_TASK_STATUS.BLOCK)){
      return true;
    }

  return false;
};
/**
 * 文件预处理重跑
 */
ta5003js.rollbackShowCondition2 = function(row){
    if((row.taskId==='F010'||row.taskId==='F014'||row.taskId==='F025'||row.taskId==='F026' ) && (row.execStatus===BATCH_TASK_STATUS.SUCCESS || row.execStatus===BATCH_TASK_STATUS.FAILED || row.execStatus===BATCH_TASK_STATUS.SKIP
        || row.execStatus===BATCH_TASK_STATUS.TERMINATION || row.execStatus===BATCH_TASK_STATUS.SLICE_FAILED || row.execStatus===BATCH_TASK_STATUS.BLOCK)){
        return true;
    }
    return false;
};


ta5003js.invokeClear = function(row){

    Tools.confirm(() => {
        if (row.parentExecGridId){
          this.httpUtil.ajax({
            url: "/server/json/DpsApp/C999", // 调用后台，采用格式为：/server/服务名/交易接口
            params: row
          }).then(data => {
            let rows = data.rows;
          })
        }else {
          if ("6"===row.execStatus ){
            this.httpUtil.ajax({
              url: "/commQuery/Ta5014/queryErrorTaskByTaskGroup.json",
              params: {"queryTaskDate": row.taskDate, "taskGroup": row.taskGroup}
            }).then(res => {
              res.rows.forEach(taskId => {
                this.httpUtil.ajax({
                  url: "/server/json/DpsApp/C999", // 调用后台，采用格式为：/server/服务名/交易接口
                  params: {"taskGroup": row.taskGroup, "moduleid": "a", "taskDate": row.taskDate, "taskId": taskId}
                }).then(data => {
                  let rows = data.rows;
                })
              })
            })
          }else {
            this.httpUtil.ajax({
              url: "/server/json/ta-batch/C998", // 调用后台，采用格式为：/server/服务名/交易接口
              params: row
            }).then(data => {
              let rows = data.rows;
            })
          }
        }

    },
    "确定执行该清算任务?"
    )

    return false;
};

ta5003js.rollBackClearTask = function(row){
  Tools.confirm(() => {
    this.httpUtil.comnQuery({
      action: "/Ta5014Detail.rollBackClearTask.json", // 调用后台，采用格式为：/server/服务名/交易接口
      params: row
    }).then(data => {
      let rows = data.rows;
      Tools.alert("回滚任务成功");
    })
  }, "确定回滚该清算任务?")
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
  if(row.taskExecid && row.buttonIsDisplay != '0' &&row.taskId==='C017'){//暂时写1为了调试页面
    return true;
  }
  return false
};
export default ta5003js;
