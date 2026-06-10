<template>
    <k-grid  ref="bbGrid" data-action="Ta5014Detail.queryRevocation"   data-operate-width="120" data-operate-column="false" :data-params="{'taskExecid':value.taskExecid}"
             :dataOperateColumn="false"  :data-card="false">
      <k-grid-column data-name="taskExecid" data-header="任务执行ID"/>
      <k-grid-column data-name="stepNo"  data-header="步骤号"/>
      <k-grid-column data-name="execStatus" data-dict="batch_task_status" data-header="任务状态"/>
      <k-grid-column data-name="targetCode" data-header="目标代码"/>
      <k-grid-column data-name="execDate" data-header="执行日期"/>
      <k-grid-column data-name="serverIp" data-header="服务器执ip"/>
      <k-grid-column data-name="startTime" data-header="执行时间"/>
      <k-grid-column data-name="rtnDesc" data-header="返回信息"/>
      <template slot="operate" slot-scope="scope" >
        <!-- todo 执行回滚任务 -->
        <k-btn data-functype="SUBMIT" data-size="mini"  data-target="ta5003Grid" data-confirm  class="md-info md-just-icon md-simple"
            data-descript="执行回滚任务" :data-disable-condition ="ta5003js.execDisableCondition"
            :data-after-success="whenAfterSuccess"  :data-handler="ta5003js.invokeClear">
          <md-icon  md-src='/static/svg/run1.svg'>play_arrow</md-icon>
        </k-btn>
       </template>

    </k-grid>


</template>

<script>
    import ta5003js from "./JS/Ta5003.js";
    import { assign } from "lodash";

    export default {
      data() {
        return {
          formData: {},
          selectRowData: {},
          ta5003js:{},
          $kgrid : null,
        };
      },

      computed: {
        value() {
          return this.$attrs.value;
        }
      },

       methods: {
         whenAfterSuccess(){
           Tools.alert("执行成功");
         },
       },

    }
</script>

<style scoped>

</style>
