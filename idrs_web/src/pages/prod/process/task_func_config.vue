<template>
  <div>
    <div>
      <k-form-search data-target="taskFuncConfigGrid" data-model-name="TaskFuncConfig">
        <k-btn class="btn-custom-primary" data-functype="POPUP" data-target="taskFunctaskFuncConfigPopupConfigPopup" :data-handler="openProdRiskRat"
               v-if="global.isShowAuthorityButton('TaskFuncConfig.saveTaskFuncConfig')">
          <md-icon md-src="/static/svg/add.svg" />新增
        </k-btn>
      </k-form-search>
      <k-grid ref="taskFuncConfigGrid"  data-action="TaskFuncConfig.queryTaskFuncConfig1">
        <k-grid-column data-header="功能名称" data-name="name"></k-grid-column>
        <k-grid-column data-header="功能指向路径" data-name="url"></k-grid-column>
        <k-grid-column data-header="检查语句" data-name="checksql"></k-grid-column>
        <k-grid-column data-header="判断条件" data-name="conditions"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-plain" data-size="mini" data-functype="POPUP" data-target="taskFuncConfigPopup" :data-handler="updateTaskFuncConfig"
                 v-if="global.isShowAuthorityButton('TaskFuncConfig.updateTaskFuncConfig')"
          >修改</k-btn>
          <k-btn class="md-danger" data-functype="SUBMIT" data-action="TaskFuncConfig.deleteTaskFuncConfig" data-size="mini"
                 data-type="danger" data-target="taskFuncConfigGrid" :data-confirm="true" data-descript="删除"
                 v-if="global.isShowAuthorityButton('TaskFuncConfig.deleteTaskFuncConfig')">
            删除
          </k-btn>
        </template>
      </k-grid>
    </div>
    <!--    产品流程功能配置弹出框   -->
    <k-popup ref="taskFuncConfigPopup" data-title="产品流程功能配置" >
      <k-form ref="taskFuncConfigForm" :data-col="2">
        <k-form-item label="功能名称:">
          <k-field-text v-model="formData.name" :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="功能指向路径:">
          <k-field-text v-model="formData.url"/>
        </k-form-item>
        <k-form-item label="准入检查语句:">
          <k-field-text v-model="formData.entryChecksql" />
        </k-form-item>
        <k-form-item label="准入检查条件:">
          <k-field-text v-model="formData.entryConditions" />
        </k-form-item>
        <k-form-item label="检查语句:">
          <k-field-text v-model="formData.checksql" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="判断条件:">
          <k-field-text v-model="formData.conditions" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="需隐藏按钮id:">
          <k-field-text v-model="formData.hideButtonIds"/>
        </k-form-item>
      </k-form>
      <div style="text-align: center;">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-from="taskFuncConfigForm" data-target="taskFuncConfigGrid" v-show="saveType == 1"
               :data-model="formData" data-action="TaskFuncConfig.saveTaskFuncConfig"><md-icon md-src="/static/svg/confirm.svg"></md-icon>保存</k-btn>
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-from="taskFuncConfigForm" data-target="taskFuncConfigGrid" v-show="saveType == 2"
               :data-model="formData" data-action="TaskFuncConfig.updateTaskFuncConfig"><md-icon md-src="/static/svg/confirm.svg"></md-icon>保存</k-btn>
      </div>
    </k-popup>
  </div>
</template>

<script>
  import {
    assign
  } from "lodash";
  import Tools from "@/utils/tools";

  export default {
    data() {
      return {
        formData: {},
        saveType:1,//1：保存，2：修改
      };
    },
    created() {
    },
    computed: {
    },
    methods: {
      openProdRiskRat(){
        this.saveType=1;
      },
      updateTaskFuncConfig(params){
        this.formData=params;
        this.saveType=2;
      },
      selectRow(row, column, event) {
        const _this = this
        _this.selectRowData = assign({}, row)
        _this.formData = assign({}, row)
      }
    }
  };
</script>
