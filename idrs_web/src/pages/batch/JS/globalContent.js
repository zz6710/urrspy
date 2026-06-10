const BATCH_TASK_TYPE={'SYSTEM':'1','PROD':'2','DISTRIBUTOR_IMP':'3','DISTRIBUTOR_EXP':'4','ZG_IMP':'5','ZG_EXP':'6' };

//执行状态 0-未执行、1-占用中、2-执行中、3-分片任务执行中、4-待调用、5-执行成功 、6-执行失败、7-跳过执行、8-任务终止、9-分片任务执行失败 、
//R-任务回滚中、Z-任务执行预展示表，初始化状态
const BATCH_TASK_STATUS={'NON_EXECUTION':'0','LOOT':'1','EXECUTION':'2','SLICE_EXECUTION':'3','TO_EXEC':'4','SUCCESS':'5'
        ,'FAILED':'6','SKIP':'7','TERMINATION':'8','SLICE_FAILED':'9','ROLL_BACK':'R','DISPLAY_INIT':'Z','BLOCK': 'B'};

export default
{
    BATCH_TASK_TYPE,BATCH_TASK_STATUS,
}　
