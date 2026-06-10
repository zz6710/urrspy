let wfStatus = {};

//流程状态
wfStatus.process = {
    running: {
        label: "进行中",
        value: "1"
    },
    finish: {
        label: "已完成",
        value: "2"
    },
    refuse: {
        label: "拒绝",
        value: "3"
    },
    backToApply: {
        label: "驳回到申请节点",
        value: "4"
    },
    reApply: {
        label: "重新提交申请",
        value: "5"
    }
}
//业务状态
wfStatus.business = {
    ready: {
        label: "就绪",
        value: "0"
    },
    processing: {
        label: "处理中",
        value: "1"
    },
    finish: {
        label: "完成",
        value: "2"
    },
    error: {
        label: "处理异常",
        value: "3"
    },
    errorConfirmed: {
        label: "处理异常已确认",
        value: "4"
    }
}
//任务状态
wfStatus.task = [
    {
        label: "申请",
        value: "0",
        tagType: "success"
    }, {
        label: "通过",
        value: "1",
        tagType: "success"
    }, {
        label: "退回",
        value: "2",
        tagType: "warning"
    }, {
        label: "驳回",
        value: "3",
        tagType: "danger"
    }, {
        label: "委派",
        value: "4",
        tagType: "primary"
    }, {
        label: "转办",
        value: "5",
        tagType: "primary"
    }, {
        label: "拒绝",
        value: "6",
        tagType: "danger"
    }]
//流程参数类型
wfStatus.param = {
    formField: {
        label: "表单字段",
        value: "1"
    },
    sql: {
        label: "SQL",
        value: "2"
    },
    url: {
        label: "URL",
        value: "3"
    }
}

//附件类型
wfStatus.attachmentType = {
    business: {
        label: "业务附件",
        value: "business"
    },
    process: {
        label: "流程附件",
        value: "process"
    }
}

//流程参数类型
wfStatus.taskBtns = {
    complete: {
        value: "complete",
        label: "通过",
    },
    delegate: {
        value: "delegate",
        label: "委派",
    },
    transfer:{
        value: "transfer",
        label: "转办",
    },
    refuse:{
        value: "refuse",
        label: "拒绝",
    },
    reject:{
        value: "reject",
        label: "驳回",
    }
}
export default wfStatus;
