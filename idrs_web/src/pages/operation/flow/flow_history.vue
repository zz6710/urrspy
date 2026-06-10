<template>
  <div>
    <el-tabs v-model="activeName" type="border-card">
      <el-tab-pane label="我发起的" name="found">
        <k-form-search data-target="opFormInfoGrid" data-model-name="ProcessInstance">
          <template slot="otherFormItem">
            <k-form-item label="发起时间">
              <k-field-date v-model="createDate" data-type="daterange"/>
            </k-form-item>
          </template>
        </k-form-search>
        <k-grid ref="opFormInfoGrid" data-action="ProcessInstance.findByCreator" :data-params="createDateStr">
          <k-grid-column data-header="流程名" data-name="processDisplayName"/>
          <k-grid-column data-header="产品代码" data-name="prodCode"/>
          <k-grid-column data-header="产品名称" data-name="prodName"/>
          <k-grid-column data-header="发起人" data-name="creator"/>
          <k-grid-column data-header="发起时间" data-name="createDate" data-type="datetime"/>
          <k-grid-column data-header="最后操作节点" data-name="lastNode"/>
          <k-grid-column data-header="最后操作人" data-name="lastOperator"/>
          <k-grid-column data-header="最后操作时间" data-name="updateDate" data-type="datetime"/>
          <k-grid-column data-header="待操作流程" data-name="currentNode"/>
          <k-grid-column data-header="当前状态" data-name="currentStatus" data-dict="process_instance_status"/>
          <!-- <k-grid-column data-header="备注" data-name="remark"/> -->
          <template slot="operate" slot-scope="scope">
            <k-btn class="btn-custom-plain" data-size="mini" data-functype="PAGE" data-target="/main/operation/flow/flow_history_detail"
            >查看</k-btn>
          </template>
        </k-grid>
      </el-tab-pane>
      <el-tab-pane label="我参与的" name="participant">
        <k-form-search data-target="opFormInfoGrid1" data-model-name="ProcessInstance">
          <template slot="otherFormItem">
            <k-form-item label="发起时间">
              <k-field-date v-model="createDate1" data-type="daterange"/>
            </k-form-item>
          </template>
        </k-form-search>
        <k-grid ref="opFormInfoGrid1" data-action="ProcessInstance.findByJoin" :data-params="createDateStr1">
          <k-grid-column data-header="流程名" data-name="processDisplayName"/>
          <k-grid-column data-header="产品代码" data-name="prodCode"/>
          <k-grid-column data-header="产品名称" data-name="prodName"/>
          <k-grid-column data-header="发起人" data-name="creator"/>
          <k-grid-column data-header="发起时间" data-name="createDate" data-type="datetime"/>
          <k-grid-column data-header="最后操作节点" data-name="lastNode"/>
          <k-grid-column data-header="最后操作人" data-name="lastOperator"/>
          <k-grid-column data-header="最后操作时间" data-name="updateDate" data-type="datetime"/>
          <k-grid-column data-header="待操作流程" data-name="currentNode"/>
          <k-grid-column data-header="当前状态" data-name="currentStatus" data-dict="process_instance_status"/>
          <template slot="operate" slot-scope="scope">
            <k-btn class="btn-custom-text" data-size="mini" data-functype="PAGE" data-target="/main/operation/flow/flow_history_detail"
            >查看</k-btn>
          </template>
        </k-grid>
      </el-tab-pane>
    </el-tabs>
  </div>

</template>

<script>
  import Tools from '@/utils/tools.js';

  export default {
    name: "flow_history",
    data() {
      return {
        activeName: 'found',
        createDate: [],
        createDate1: []
      };
    },
    computed: {
      createDateStr() {
        if (this.createDate && this.createDate.length>0) {
          return {createDateStart:this.createDate[0], createDateEnd: this.createDate[1]};
        }
        return {};
      },
      createDateStr1() {
        if (this.createDate1 && this.createDate1.length>0) {
          return {createDateStart:this.createDate1[0], createDateEnd: this.createDate1[1]};
        }
        return {};
      }
    },
    methods: {
      afterload(rows) {
        Object.keys(rows).forEach(key => rows[key].createDate+=rows[key].createTime);
        console.log(rows);
      }
    }
  };
</script>

<style>

</style>
