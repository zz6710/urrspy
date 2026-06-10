<template>
  <div>
    <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addPopup">
      <md-icon md-src="/static/svg/add.svg"/>新增</k-btn>
    <k-grid
      ref="grid"
      :data-page-size="0"
      data-url='wf/test/find.json'>
      <k-grid-column data-header="id" data-name="id"></k-grid-column>
      <k-grid-column data-header="名称" data-name="name"></k-grid-column>
      <k-grid-column data-header="流程状态" data-dict="process_status" data-name="processStatus"></k-grid-column>
       <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" data-descript="发起流程" data-size="mini"
               :data-handler="handleSubmit"
               >
          <md-icon>edit</md-icon>
        </k-btn>
         <k-btn class="md-info md-just-icon md-simple" data-descript="审核" data-functype="POPUP" data-size="mini"
               :data-handler="handleApproval"
               >
         <md-icon>save</md-icon>
        </k-btn>
      </template>
    </k-grid>

    <k-popup ref="addPopup" data-title="新增测试数据">
      <k-form ref="addForm" :data-col="1">
        <k-form-item label="id">
          <k-field-text v-model="formData.id" :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="名称">
          <k-field-text v-model="formData.name" :data-allowblank="false" />
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-url="/wf/test/add.json" data-target="grid"
                 data-from="addForm" :data-model="formData">
            <md-icon md-src="/static/svg/confirm.svg"/>
            确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"/>
            取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

  </div>

</template>

<script>

  import {assign} from "lodash";

  export default {
    name: "test",
    data() {
      return {
        formData: {},
        selectRowData: {}
      };
    },
    methods: {
      handleSubmit(row) {
        let submitParams = {
          "_wfBusiTableName": 'wf_test',
          "_wfBusiTablePrimaryKey": 'id,id2',
          "id": row.id,
          "id2": row.id2,
          "_wfProcessName": 'rk2',
          "_wfOriginalData": JSON.stringify(row),
          "_wfLabelJsonInfo":
            JSON.stringify(
              {
                        "id":{"feild":"id","label":""},
                        "name":{"feild":"name","label":"名称"},
                        "processStatus":{"feild":"processStatus","label":"流程状态"}
              })
        };
         this.httpUtil
          .ajax({
            url: "/wf/processInstance/startAndExecute.json",
            params: submitParams
          })
          .then(data => {
            this.$refs.grid._load(this.$refs.grid.getCachedParams());
          });
        return false
      },
      handleApproval(row){
        let params = {
            opinion: 'test',
            result: 3,
            taskId: '01b2c83dfef94f38817415baa8db0d97',
            isSurrogate: false
        }
       this.httpUtil
          .ajax({
            url: "/wf/approval/doApproval.json",
            params: params
          })
          .then(data => {
          });
        return false
      }
    }
  };
</script>

<style>
</style>
