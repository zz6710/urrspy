<template>
  <div>
    <k-form :data-col="4" :data-model="formData" data-input-width="200px" data-label-width="90px">
      <k-form-item label="步骤号" data-input-width="100px">
        <k-field-select data-placeholder="请选择筛选步骤号" ref="stepNo" v-model="formData.stepNo" @data-on-change="queryDistributor" data-action="KbatchSliceExec.queryStepNo" data-value-field="stepNo" data-display-field="stepNo" :data-params="{'taskExecid':value.taskExecid}"/>
      </k-form-item>
      <k-form-item label="分片任务状态" data-label-width="150px" data-input-width="120px">
        <k-field-select data-placeholder="请选择筛选分片任务状态" ref="sliceStatus" v-model="formData.sliceStatus" @data-on-change="queryDistributor" data-dict="batch_task_status"/>
      </k-form-item>
      <k-form-item label="数据源" data-input-width="100px" data-label-width="90px">
        <k-field-select data-placeholder="请选择筛选数据源" ref="datasource" v-model="formData.datasource" @data-on-change="queryDistributor" data-action="KbatchSliceExec.queryDatasource" data-value-field="datasource" data-display-field="datasource" :data-params="{'taskExecid':value.taskExecid}"/>
      </k-form-item>
      <k-form-item label="服务器IP" data-input-width="150px" data-label-width="90px">
        <k-field-select data-placeholder="请选择筛选服务器IP" ref="serverIp" v-model="formData.serverIp" @data-on-change="queryDistributor" data-action="KbatchSliceExec.queryServerIp" data-value-field="serverIp" data-display-field="serverIp" :data-params="{'taskExecid':value.taskExecid}"/>
      </k-form-item>
    </k-form>
    <k-grid  ref="SliceGrid" data-action="KbatchSliceExec.query" data-operate-width="120" :data-params="{'taskExecid':value.taskExecid}"
             :dataOperateColumn="false"   :data-card="false">
    <k-grid-column data-name="sliceExecid" data-header="分片任务ID" data-width="140"/>
    <k-grid-column data-name="taskExecid" data-header="批量任务执行ID" data-width="140"/>
    <k-grid-column data-name="stepNo" data-header="步骤号" data-width="70"/>
    <k-grid-column data-name="taskId" data-header="批量任务ID" data-width="100"/>
    <k-grid-column data-name="threadUuid" data-header="分片任务执行UUID"  data-width="150"/>
    <k-grid-column data-name="rtnDesc" data-header="返回信息" data-width="200"/>
    <k-grid-column data-name="datasource" data-header="数据源"/>
    <!-- <k-grid-column data-name="serverNode" data-header="节点号"/> -->
    <k-grid-column data-name="serverName" data-header="服务器主机名"  data-width="100"/>
    <k-grid-column data-name="serverIp" data-header="服务器IP" data-width="100"/>
    <!-- <k-grid-column data-name="moduleid" data-header="系统模块ID" data-dict="moduleid"/> -->
    <!-- <k-grid-column data-name="sliceType" data-header="分片任务类型"/> -->
    <k-grid-column data-name="targetCode" data-header="目标代码"/>
    <!-- <k-grid-column data-name="prodCode" data-header="产品代码"/> -->
    <!--<k-grid-column data-name="sliceNo" data-header="分片序列号"/> -->
    <!--<k-grid-column data-name="sliceLength" data-header="分片长度"/> -->
    <k-grid-column data-name="busiParams" data-header="业务参数"/>
     <k-grid-column data-name="sliceStatus" data-header="分片任务状态" data-dict="batch_task_status"/>
    <k-grid-column data-name="execOrder" data-header="执行优先级" data-width="70"/>
    <!--<k-grid-column data-name="inQueueTime" data-header="进入队列时间"/> -->
    <k-grid-column data-name="sliceStart" data-header="分片起始" />
    <k-grid-column data-name="sliceEnd" data-header="分片结束" />
    <k-grid-column data-name="execStartDate" data-header="执行起始日期"/>
    <k-grid-column data-name="execStartTime" data-header="执行起始时间"/>
    <k-grid-column data-name="execEndDate" data-header="执行结束日期"/>
    <k-grid-column data-name="execEndTime" data-header="执行结束时间"/>
    <k-grid-column data-name="appName" data-header="应用服务器名称"/>
    <k-grid-column data-name="threadId" data-header="线程号"/>

  </k-grid>
  </div>
</template>
<script>
    export default {
      data() {
        return {
          formData: {
            stepNo: "",
            sliceStatus:"",
            datasource:"",
            serverIp:"",
          },
        };
      },
      methods: {
        queryDistributor(){
          this.$refs.SliceGrid.load(this.formData);
        },
      },
      name: "Ta5003SliceExec",
      props:{
          taskExecId:{
            type:String
          }
      },
      computed: {
        value() {
          console.log(this.$attrs.value);
          return this.$attrs.value;
        }
      },
    }
</script>

<style scoped>

</style>
