<template>
  <div class="py-page">
    <k-form-search-customize data-target="ta5004Grid" v-model="queryParam" data-label-width="100px">
      <k-form-item label="所属模块">
          <k-field-select v-model="queryParam.taskModel" data-dict="task_model"></k-field-select>
      </k-form-item>
      <k-form-item label="任务ID">
        <k-field-text v-model="queryParam.taskId" data-validate-type="text"/>
      </k-form-item>
      <k-form-item label="清算业务日期">
        <k-field-date v-model="queryParam.taskDate" ></k-field-date>
      </k-form-item>
      <k-form-item label="执行状态">
        <k-field-select v-model="queryParam.execStatus" data-dict="batch_task_stat"></k-field-select>
      </k-form-item>
    </k-form-search-customize>

    <div class="py-page-container">
      <k-grid ref="ta5004Grid" data-action="KbatchLog.findBatchLog" @data-row-select="selectRow"
              data-operate-width="300px" data-operate-column="false" :data-autoload="false">
        <k-grid-column data-header="模块ID" data-name="taskGroup"  ></k-grid-column>
        <k-grid-column data-header="所属模块" data-name="taskModel"  data-dict="task_model"></k-grid-column>
        <k-grid-column data-header="任务ID" data-name="taskId" ></k-grid-column>
        <k-grid-column data-header="任务名称" data-name="taskName" ></k-grid-column>
        <k-grid-column data-header="步骤号" data-name="stepNo" ></k-grid-column>
        <k-grid-column data-header="执行状态" data-name="execStatus" data-dict="batch_task_status"></k-grid-column>
        <k-grid-column data-header="执行结果" data-name="rtnDesc" width="90"></k-grid-column>
        <k-grid-column data-header="清算业务日期" data-name="taskDate"  data-type="date" data-width="100"></k-grid-column>
        <k-grid-column data-header="日志号" data-name="logSerno" ></k-grid-column>
        <k-grid-column data-header="任务执行ID" data-name="taskExecid" ></k-grid-column>
        <k-grid-column data-header="日志线程号" data-name="threadUuid" width="90" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="主机名" data-name="serverName" data-hidden="true" ></k-grid-column>
        <k-grid-column data-header="主机IP" data-name="serverIp" data-hidden="true"></k-grid-column>
      </k-grid>
   </div>
  </div>
</template>
<script>
  import {assign} from "lodash";
  import httpUtil from "@/frame/httpUtil";
  export default {
    name: "TA5004",
    data() {
      return {
        queryParam:{},
        formData: {},
        cascaderValue: [],
        selectRowData: {}
      };
    },
    methods: {
      selectRow(row, column, event) {
        const _this = this
        _this.selectRowData = assign({}, row)
        _this.formData = assign({}, row)
      }
    },
    created() {
        this.httpUtil.sysDate().then(res => {
          if (res) {
            this.$set(this.queryParam, 'taskDate', res.toString());
            httpUtil.sysparam('10006', '0').then(data=>{
              if(data===1){
                this.$nextTick(()=>{this.$refs.ta5004Grid.load({taskDate:"sysDate"})});
              }else{
                this.$nextTick(()=>{this.$refs.ta5004Grid.load({taskDate:res.toString()})});
              }
            })
          }
        })
    },
  }

</script>
