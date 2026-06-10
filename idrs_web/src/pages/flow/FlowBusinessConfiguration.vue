<template>
  <div class="py-page">
    <k-form-search data-target="grid" data-model-name="WfBusinessConfig" data-label-width="80px">
    </k-form-search>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addPopup"
             v-if="global.isShowAuthorityButton('WfBusinessConfig.add')">
          <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
        </div>
      </div>
      <k-grid
        ref="grid"
        @data-row-select="selectRow"
        data-action='WfBusinessConfig.find1'>
        <k-grid-column data-align="center"
                      data-header="业务操作"
                      data-name="serverName"
        ></k-grid-column>
        <k-grid-column data-align="center"
                      data-header="流程名"
                      data-name="processDisplayName"
        ></k-grid-column>
        <k-grid-column data-align="center"
                      data-header="主键"
                      data-name="busKeys"
        ></k-grid-column>
        <k-grid-column data-align="center"
                      data-header="是否在移动端展示"
                      data-name="appDisplay"
                      data-dict="1yes0no"
        ></k-grid-column>
        <k-grid-column data-align="center"
                      data-header="状态"
                      data-name="status"
                      data-dict="flow_business_status"
        ></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="启用"  data-size="mini" data-functype="SUBMIT"
                data-target="grid" data-action="WfBusinessConfig.turnOn" :data-disabled="scope.row.row.status === '1'"
                v-if="global.isShowAuthorityButton('WfBusinessConfig.turnOn')">
            <md-icon>lock_open</md-icon>
          </k-btn>
          <k-btn class="md-info md-just-icon md-simple" data-descript="停用"  data-size="mini" data-functype="SUBMIT"
                data-target="grid" data-action="WfBusinessConfig.turnDown" :data-disabled="scope.row.row.status === '0'"
                v-if="global.isShowAuthorityButton('WfBusinessConfig.turnDown')">
            <md-icon>lock</md-icon>
          </k-btn>
          <k-btn class="md-info md-just-icon md-simple" data-descript="编辑" data-functype="POPUP" data-size="mini"
                data-target="editPopup"
                v-if="global.isShowAuthorityButton('WfBusinessConfig.edit')">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="WfBusinessConfig.delete" data-size="mini"
                data-type="danger" data-target="grid" :data-confirm="true" data-descript="删除"
                v-if="global.isShowAuthorityButton('WfBusinessConfig.delete')">
            <md-icon>close</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </div>

    <k-popup ref="addPopup" data-title="新增" >
      <k-form ref="addForm" :data-col="2" >
        <k-form-item label="业务" style="width:720px !important;">
          <k-field-tree  v-model="formData.server" data-diffcondition="id,parentId" :data-multiple="false"
                        data-action="WfBusinessConfig.findServerMethodTree" :data-flat="false" :data-allowblank="false"
                        data-display-child="children" data-placeholder="请选择业务" data-display-field="name"
                        data-value-field="id" :data-disable-branch-nodes="true" style="width:700px !important;" />
        </k-form-item>
        <k-form-item label="流程">
          <k-field-select ref="popupProcessSelect" v-model="formData.processName" :data-allowblank="false" data-url="/wf/process/listAllProcess.json"
          data-params="{start:0,limit: 10000000}"
          data-value-field="name"  @data-on-change="changeProcess"
          data-display-field="displayName"/>
        </k-form-item>
        <k-form-item label="主键名">
          <k-field-text v-model="formData.busKeys" :data-allowblank="false" data-placeholder="多个主键用英文逗号分割"/>
        </k-form-item>
        <k-form-item label="表名" v-if="processIsAggressive">
          <k-field-text v-model="formData.tableName" data-placeholder="入库需要填写表名" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="是否在移动端展示" data-input-width="164px" data-label-width="180px">
          <k-field-select v-model="formData.appDisplay"  data-dict="1yes0no"  :data-allowblank="false"/>
        </k-form-item>
        <k-form-footer data-align="center" style="margin-top:70px;">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="WfBusinessConfig.add" data-target="grid"
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

    <k-popup ref="editPopup" data-title="修改">
      <k-form ref="editForm" :data-col="1">
        <k-form-item label="业务">
          <k-field-text v-model="formData.serverName" :data-disabled="true"></k-field-text>
        </k-form-item>
        <k-form-item label="流程">
          <k-field-select ref="popupProcessSelect" v-model="formData.processName" :data-after-load="changeProcess"
                          :data-allowblank="false" data-url="/wf/process/listAllProcess.json" @data-on-change="changeProcess"
                        data-params="{start:0,limit: 10000000}" data-value-field="name" data-display-field="displayName"/>
        </k-form-item>
        <k-form-item label="主键名">
          <k-field-text v-model="formData.busKeys" :data-allowblank="false"
                        data-placeholder="多个主键用英文逗号分割"/>
        </k-form-item>
        <k-form-item label="表名" v-if="processIsAggressive">
          <k-field-text v-model="formData.tableName" data-placeholder="入库需要填写表名" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="是否在移动端展示" data-input-width="164px" data-label-width="180px">
          <k-field-select v-model="formData.appDisplay"  data-dict="1yes0no"  :data-allowblank="false"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="WfBusinessConfig.edit" data-target="grid"
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
  import Tools from "@/utils/tools.js";
  import FlowProcessInstanceListDialog from './FlowProcessInstanceListDialog'

  export default {
    name: "FlowUserLaunch",
    components: {FlowProcessInstanceListDialog},
    data() {
      return {
        formData: {},
        envItems: [],
        selectRowData: {},
        queryParamProcessName: '',
        queryParamStatus: '',
        queryParamCreateDateRange: [],
        queryParamFinishDateRange: [],
        processIsAggressive: false
      };
    },
    methods: {
      changeProcess() {
        this.$nextTick(()=>{
          if (!this.$refs.popupProcessSelect) {
            this.processIsAggressive =  false;
            return;
          }

          let selectObj = this.$refs.popupProcessSelect.getSelectObject();
          if (!selectObj) {
            this.processIsAggressive = false
            return;
          }

          if (selectObj.type === '1') {
            this.processIsAggressive = true;
          } else {
            this.processIsAggressive = false;
          }
        })
      },
      renderCreateDateTime(row) {
        return Tools.formatDateTime(row.createDate, row.createTime);
      },
      renderFinishDateTime(row) {
        let date = Tools.formatDateTime(row.finishDate, row.finishTime);
        if (date == "") {
          return '-'
        }
        return date;
      },
      renderApplyUser(row) {
       if (row.approvalUser) {
         return row.approvalUser
       } else {
         return row.applyUser
       }
      },
      renderResult(row) {
        if (!result) {
         return "提交流程申请"
        }
      },
      renderOpinion(row) {
        if (row.opinion) {
          return row.opinion
        } else {
          return "-"
        }
      },
      renderFinishDateTime(row) {
        if (row.finishDate && row.finishTime) {
         return Tools.formatDateTime(row.finishDate, row.finishTime);
        }
      },
      selectRow(row, column, event) {
        const _this = this
        _this.selectRowData = assign({}, row)
        _this.formData = assign({}, row)
      }
    }
  };
</script>

<style scoped>
  /deep/ .el-dialog {
    padding-top: 35px;
  }
</style>
