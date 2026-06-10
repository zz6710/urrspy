<template>
  <div>
    <div>
      <k-form-search data-target="opFormInfoGrid" data-model-name="OpFormInfo">
        <k-btn class="btn-custom-primary" data-functype="POPUP" data-target="opFormInfoPopup" :data-handler="resetFormData"
               v-if="global.isShowAuthorityButton('OpFormInfo.add')">
          <md-icon md-src="/static/svg/add.svg" />新增
        </k-btn>
      </k-form-search>
      <k-grid ref="opFormInfoGrid" data-action="OpFormInfo.find" @data-row-select="selectRow">
        <k-grid-column data-header="表单id" data-name="formId"></k-grid-column>
        <k-grid-column data-header="表单名称" data-name="formName"></k-grid-column>
        <k-grid-column data-header="表单类型" data-name="formType" data-dict="op_form_type"></k-grid-column>
        <k-grid-column data-header="引用组件路径" data-name="compPath"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-plain" data-size="mini" data-functype="POPUP" data-target="opFormInfoPopup" :data-handler="()=>saveType='2'"
                 v-if="global.isShowAuthorityButton('OpFormInfo.update')"
          >修改</k-btn>
          <k-btn class="md-danger" data-functype="SUBMIT" data-action="OpFormInfo.delete" data-size="mini"
                 data-type="danger" data-target="opFormInfoGrid" :data-confirm="true" data-descript="删除"
                 v-if="global.isShowAuthorityButton('OpFormInfo.delete')">
            删除
          </k-btn>
          <k-btn v-if="scope.row.row.formType=='0'" class="btn-custom-primary" data-functype="PAGE" data-target="/main/operation/params" data-size="mini" data-descript="参数配置">
            参数配置
          </k-btn>
          <k-btn v-if="scope.row.row.formType=='1'" class="btn-custom-primary" data-functype="PAGE" data-target="/main/operation/params" data-size="mini" data-descript="查看组件">
            查看组件
          </k-btn>
          <k-btn class="btn-custom-primary" data-functype="PAGE" data-target="/main/operation/form_sql" data-size="mini">
            sql配置
          </k-btn>
        </template>
      </k-grid>
    </div>
    <!--    功能表单配置弹出框   -->
    <k-popup ref="opFormInfoPopup" :data-title="saveType==1?'新增功能表单':'修改功能表单'" >
      <k-form ref="opFormInfoForm" :data-col="2">
        <k-form-item label="表单名称:">
          <k-field-text v-model="formData.formName" :data-allowblank="false" :data-max-length="32" />
        </k-form-item>

        <k-form-item label="表单类型:">
          <k-field-select v-model="formData.formType" :data-allowblank="false" data-dict="op_form_type" />
        </k-form-item>

        <k-form-item v-if="formData.formType=='1'" label="引用组件路径:">
          <k-field-text v-model="formData.compPath" :data-allowblank="false" :data-max-length="256" />
        </k-form-item>
      </k-form>

      <div style="text-align: center;">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-from="opFormInfoForm" data-target="opFormInfoGrid" v-show="saveType == 1"
               :data-model="formData" data-action="OpFormInfo.add"><md-icon md-src="/static/svg/confirm.svg"></md-icon>保存</k-btn>
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-from="opFormInfoForm" data-target="opFormInfoGrid" v-show="saveType == 2"
               :data-model="formData" data-action="OpFormInfo.update"><md-icon md-src="/static/svg/confirm.svg"></md-icon>保存</k-btn>
        <k-btn data-functype="CLOSE"><md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
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
    name: "form",
    data() {
      return {
        formData: {},
        saveType: 1,//1：保存，2：修改
      };
    },
    created() {
    },
    computed: {
    },
    methods: {
      resetFormData(){
        this.formData = {};
        this.saveType=1;
      },
      selectRow(row, column, event) {
        this.selectRowData = assign({}, row)
        this.formData = assign({}, row)
      }
    }
  };
</script>
