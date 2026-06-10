<template>
  <k-grid  ref="StepGrid" data-action="KbatchTaskStepExec.query"     data-operate-width="120" :data-params="{'taskExecid':value.taskExecid,'taskId':value.taskId}"
           :dataOperateColumn="false"  :data-card="false" >
    <k-grid-column data-name="taskExecid" data-header="执行ID" data-width="140"/>
    <k-grid-column data-name="taskDate" data-header="清算业务日期" data-width="120"/>
    <k-grid-column data-name="taskGroup" data-header="任务组"  data-width="120"/>
    <k-grid-column data-name="taskId" data-header="任务ID"/>
    <k-grid-column data-name="stepNo" data-header="执行步骤"/>
    <k-grid-column data-name="stepName" data-header="步骤名称" data-width="150"/>
    <k-grid-column data-name="threadUuid" data-header="任务执行ID" data-width="150"/>
    <k-grid-column data-name="execStatus" data-header="执行状态" data-dict="batch_task_status"/>
    <k-grid-column data-name="rtnCode" data-header="返回码"/>
    <k-grid-column data-name="rtnDesc" data-header="返回信息" data-width="250"/>
    <k-grid-column data-name="serverName" data-header="服务器主机名"  data-width="150"/>
    <k-grid-column data-name="serverIp" data-header="服务器IP" data-width="150"/>
    <!-- <k-grid-column data-name="moduleid" data-header="系统模块ID"/> -->
    <!-- <k-grid-column data-name="targetCode" data-header="目标代码"/>
    <k-grid-column data-name="prodCode" data-header="产品代码"/> -->
    <k-grid-column data-name="shouldExecDate" data-header="系统工作日"/>
    <k-grid-column data-name="execDate" data-header="执行日期"/>
    <k-grid-column data-name="startTime" data-header="执行开始时间"/>
    <k-grid-column data-name="endTime" data-header="执行结束时间"/>
    <k-grid-column data-name="isNeglect" data-header="是否忽略对账不平"/>
    <k-grid-column data-name="isSkip" data-header="是否跳过执行该步骤"/>
    <k-grid-column data-name="isReplay" data-header="是否重新执行该步骤"/>
    <!-- <k-grid-column data-name="isStop" data-header="是否断点"/> -->
    <k-grid-column data-name="isSlice" data-header="是否分片任务"/>
    <k-grid-column data-name="serverNode" data-header="服务器节点号"/>
    <!-- <k-grid-column data-name="distributorBatch" data-header="销售商文件批次号"/> -->
  </k-grid>
</template>

<script>
    export default {
        name: "Ta5003StepExec",
      computed: {
        value() {
          return this.$attrs.value;
        }
      },
    }
</script>

<style scoped>

</style>
