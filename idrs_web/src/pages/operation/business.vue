<template>
  <div>
    <div>
      <k-form-search data-target="opBusiInfoGrid" data-model-name="OpBusiInfo">
        <k-btn class="btn-custom-primary" data-functype="POPUP" data-target="opBusiInfoPopup" :data-handler="resetFormData"
              v-if="global.isShowAuthorityButton('OpBusiInfo.add')">
          <md-icon md-src="/static/svg/add.svg" />新增
        </k-btn>
      </k-form-search>
      <k-grid ref="opBusiInfoGrid" data-action="OpBusiInfo.find" @data-row-select="selectRow"  data-show-tree dataLazy dataExpandAll data-tree-id="busiId" data-diffcondition="busiId,upperId">
        <k-grid-column data-header="功能id" data-name="busiId"></k-grid-column>
        <k-grid-column data-header="功能名称" data-name="busiName"></k-grid-column>
        <!-- <k-grid-column data-header="上级功能" data-name="upperId"></k-grid-column> -->
        <k-grid-column data-header="icon图标" data-name="iconClass"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-plain" data-size="mini" data-functype="POPUP" data-target="opBusiInfoPopup" :data-handler="()=>saveType='2'"
                v-if="global.isShowAuthorityButton('OpBusiInfo.update')"
          >修改</k-btn>
          <k-btn class="md-danger" data-functype="SUBMIT" data-action="OpBusiInfo.delete" data-size="mini"
                data-type="danger" data-target="opBusiInfoGrid" :data-confirm="true" data-descript="删除"
                v-if="global.isShowAuthorityButton('OpBusiInfo.delete')">
            删除
          </k-btn>
          <k-btn class="btn-custom-primary" data-functype="PAGE" data-target="/main/operation/busi_form" data-size="mini">
            表单配置
          </k-btn>
        </template>
      </k-grid>
    </div>

    <k-popup ref="opBusiInfoPopup" :data-title="saveType==1?'新增功能':'修改功能'" >
      <k-form ref="opBusiInfoForm" :data-col="2">
        <k-form-item label="功能名称:">
          <k-field-text v-model="formData.busiName" :data-allowblank="false" :data-max-length="32" />
        </k-form-item>

        <k-form-item label="上级功能:">
          <k-field-select v-model="formData.upperId" data-action="OpBusiInfo.findAll" :data-params="saveType==1?null:JSON.stringify({busiId:formData.busiId})" data-display-field="busiName" data-value-field="busiId" />
        </k-form-item>

        <k-form-item label="icon图标:">
          <k-field-text v-model="formData.iconClass" :data-max-length="256" />
        </k-form-item>
      </k-form>

      <div style="text-align: center;">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-from="opBusiInfoForm" data-target="opBusiInfoGrid" v-show="saveType == 1"
               :data-model="formData" data-action="OpBusiInfo.add"><md-icon md-src="/static/svg/confirm.svg"></md-icon>保存</k-btn>
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-from="opBusiInfoForm" data-target="opBusiInfoGrid" v-show="saveType == 2"
               :data-model="formData" data-action="OpBusiInfo.update"><md-icon md-src="/static/svg/confirm.svg"></md-icon>保存</k-btn>
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
    name: "business",
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
  }
</script>
