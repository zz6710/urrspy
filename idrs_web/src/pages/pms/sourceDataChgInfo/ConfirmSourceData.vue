<template>
  <div>
    <k-form ref="sourceDataChgInfoForm" data-label-width="266px" isFormBodyScreen>
      <el-button @click="changeMeth('1')" style="margin-left:750px" type="success" icon="el-icon-check" size="small">批量修改</el-button>
      <el-button @click="changeMeth('0')" style="margin-right:10px" type="danger" icon="el-icon-close" size="small">批量不修改</el-button>
      <template>
        <k-grid ref="sourceDataGrid" data-action="SourceDataChgInfoModel.findSourceDataChgInfoModelAll"
                :data-params="{'tables':this.formData.tables,'naturalKeys':this.formData.naturalKeys,'newid':this.formData.newid}"
                :dataPageSize="0" :data-display="false" data-operate-width="100px" class="continue-ele" data-checkbox="true" data-checkbox-id="id">
          <k-grid-column data-header="ID" data-name="id" data-hidden="true"></k-grid-column>
          <k-grid-column data-header="数据库表名" data-name="tableName" data-width="100"></k-grid-column>
          <k-grid-column data-header="主键信息" data-name="naturalKeys" data-hidden="true"></k-grid-column>
          <k-grid-column data-header="主键信息名" data-name="naturalKeysName" data-width="180"></k-grid-column>
          <k-grid-column data-header="涉及字段" data-name="changeField" data-width="100" data-hidden="true"></k-grid-column>
          <k-grid-column data-header="涉及字段名" data-name="changeFieldName" data-width="100"></k-grid-column>
          <k-grid-column data-header="旧数据ID" data-name="oldid" data-hidden="true"></k-grid-column>
          <k-grid-column data-header="新数据ID" data-name="newid" data-hidden="true"></k-grid-column>
          <k-grid-column data-header="数据库表" data-name="tables" data-hidden="true"></k-grid-column>
          <k-grid-column data-header="原数据" data-name="fieldOld" data-hidden="true"></k-grid-column>
          <k-grid-column data-header="新数据" data-name="fieldNew" data-hidden="true"></k-grid-column>
          <k-grid-column data-header="原数据" data-name="dictOld" data-width="160"></k-grid-column>
          <k-grid-column data-header="新数据" data-name="dictNew" data-width="160"></k-grid-column>
          <k-grid-column data-header="数据涉及报表" data-name="reportName" data-hidden="true"></k-grid-column>
          <k-grid-column data-header="数据涉及报表名" data-name="reportsName" data-width="120"></k-grid-column>
          <k-grid-column data-header="修改状态" data-name="editstatus" data-dict="editStatus" data-width="70"></k-grid-column>
          <template slot="operate" slot-scope="scope">
            <k-btn class="md-info md-just-icon md-simple" :data-handler="()=>scope.row.row.editstatus='1'" data-size="mini" data-descript="修改">
              <md-icon>done</md-icon>
            </k-btn>
            <k-btn class="md-danger md-just-icon md-simple" :data-handler="()=>scope.row.row.editstatus='0'" data-size="mini" data-type="danger" data-descript="不修改">
              <md-icon>close</md-icon>
            </k-btn>
          </template>
        </k-grid>
      </template>
      <k-form-footer slot="footer" data-align="center">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-handler="submitHandle" data-from="sourceDataChgInfoForm" :data-model="formData" >
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">
          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
        </k-btn>
      </k-form-footer>
    </k-form>
  </div>
</template>

<script>
import Tools from "@/utils/tools";

export default {
  name: "ConfirmSourceData",
  props: {
    info : {
      type:Object,
    },
  },
  data() {
    return {
      sourceDataChgs: [],
      formData: {},
      selectRows: []
    };
  },
  created() {
    this.formData = this.info;
    let listDict = this.$refs.sourceDataGrid['list'];
    for (let i = 0; i < sourceData.length; i++) {
      for (const row of listDict){
        listDict.fieldOld = "";
      }
    }
  },
  methods: {
    submitHandle() {
      let sourceData = this.$refs.sourceDataGrid['list'];
      let sucflag = "0";
      for(let i=0;i<sourceData.length;i++){
        if(sourceData[i].editstatus===null||sourceData[i].editstatus===""){
          Tools.alert("有数据未确认修改状态！", "danger");
          return;
        }
      }
      let flowParams=[];
      flowParams["sourceData"] = JSON.stringify(sourceData);
      flowParams.tables= this.formData.tables;
      flowParams.naturalKeys=this.formData.naturalKeys;
      flowParams.taskGroup=this.formData.taskGroup;
      this.httpUtil.comnUpdate({
        action: "SourceDataChgInfoModel.confirmSourceDataChgInfoModel",
        params: flowParams,
        successAlert: true
      }).then(data => {
        this.$emit('loadGriding',this.formData);
      });
    },
    changeMeth(value){
      const list = this.$refs.sourceDataGrid.getSelected();
      for (const row of list){
        row.editstatus = value;
      }
      this.$refs.sourceDataGrid.setSelected([]);
    }
  }
}
</script>

<style scoped>

</style>
