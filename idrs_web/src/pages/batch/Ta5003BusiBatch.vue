<template>
    <k-grid   ref="bbGrid" data-action="TaBusiBatchCheckModel.query"   data-operate-width="120" data-operate-column="false" :data-params="{'taskId':value.taskId,'busiDate':value.taskDate,'taskGroup':value.taskGroup}"
             :dataOperateColumn="false" :data-card="false">
      <k-grid-column data-name="busiDate" data-header="业务日期"/>
      <k-grid-column data-name="checkFlag" data-header="批检标识"/>
      <k-grid-column data-name="prodCode" data-header="产品代码"/>
      <k-grid-column data-name="distributorCode" data-header="销售商代码"/>
      <k-grid-column data-name="checkName" data-header="批检功能"/>
      <k-grid-column data-name="checkResult" data-header="批检结果"/>
      <k-grid-column data-name="crtTime" data-header="创建日期"/>
      <k-grid-column data-name="updTime" data-header="更新日期"/>
      <k-grid-column data-name="taskGroup" data-header="批次号"/>
      <k-grid-column data-name="moduleid" data-header="系统模块id"/>
      <k-grid-column data-name="remark" data-header="备注"/>
    </k-grid>
</template>

<script>
    import { assign } from "lodash";
    export default {
        name: "Ta5003BusiBatch",
      computed: {
        value() {
          return this.$attrs.value;
        }
      },
    }
</script>

<style scoped>

</style>
