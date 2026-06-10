<template>
  <div class="py-page">
    <div>
      <k-form-search data-model-name="T8ParamConfigModel" data-target="T8ParamConfigModelGrid" data-label-width="70px">
      </k-form-search>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addT8ParamConfigModelPopup">
            <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
        </div>
      </div>
      <k-grid ref="T8ParamConfigModelGrid" @data-row-select="selectRow" data-action="T8ParamConfigModel.findT8ParamConfigModels" :data-display ="false">
        <k-grid-column data-header="系统参数表ID" data-name="id" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="参数描述" data-name="configDescribe"></k-grid-column>
        <k-grid-column data-header="参数名称" data-name="configName"></k-grid-column>
        <k-grid-column data-header="参数值" data-name="configCode" ></k-grid-column>
        <k-grid-column data-header="真实参数值" data-name="realConfigCode" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="参数类型" data-name="configType"></k-grid-column>
        <k-grid-column data-header="状态" data-name="status" data-dict="statusForInter"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改接口参数配置" data-functype="POPUP" data-size="mini"
                 data-target="editT8ParamConfigModelPopup">
            修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="T8ParamConfigModel.deleteT8ParamConfigModel"
                 :data-confirm="true" data-size="mini" data-type="danger" data-target="T8ParamConfigModelGrid" data-descript="删除接口参数配置">
            删除
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    添加接口参数配置弹出框   -->
    <k-popup ref="addT8ParamConfigModelPopup" data-title="新增">
      <k-form ref="addT8ParamConfigModelForm" :data-col="2">
        <k-form-item label="系统参数表ID" v-show="false">
          <k-field-text v-model="formData.id"/>
        </k-form-item>
        <k-form-item label="参数描述">
          <k-field-text v-model="formData.configDescribe"/>
        </k-form-item>
        <k-form-item label="参数名称">
          <k-field-text v-model="formData.configName"/>
        </k-form-item>
        <k-form-item label="参数值">
          <k-field-text v-model="formData.realConfigCode" :data-show-password="formData.configName==='USERNAMES'||formData.configName==='PASSWORD'"  />
        </k-form-item>
        <k-form-item label="参数类型">
          <k-field-text v-model="formData.configType"/>
        </k-form-item>
        <k-form-item label="状态">
          <k-field-select v-model="formData.status" data-dict="statusForInter"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ParamConfigModel.addT8ParamConfigModel" data-from="addT8ParamConfigModelForm"
                 :data-model="formData" data-target="T8ParamConfigModelGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改接口参数配置弹出框   -->
    <k-popup ref="editT8ParamConfigModelPopup" data-title="修改">
      <k-form ref="editT8ParamConfigModelForm" :data-col="2">
        <k-form-item label="系统参数表ID" v-show="false">
          <k-field-text v-model="formData.id"/>
        </k-form-item>
        <k-form-item label="参数描述">
          <k-field-text v-model="formData.configDescribe"/>
        </k-form-item>
        <k-form-item label="参数名称">
          <k-field-text v-model="formData.configName"/>
        </k-form-item>
        <k-form-item label="参数值">
          <k-field-text v-model="formData.realConfigCode" :data-show-password="formData.configName==='USERNAMES'||formData.configName==='PASSWORD'"/>
        </k-form-item>
        <k-form-item label="参数类型">
          <k-field-text v-model="formData.configType"/>
        </k-form-item>
        <k-form-item label="状态">
          <k-field-select v-model="formData.status" data-dict="statusForInter"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ParamConfigModel.updateT8ParamConfigModel" data-from="editT8ParamConfigModelForm"
                 :data-model="formData" data-target="T8ParamConfigModelGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
  </div>
</template>

<script>
export default {
  name:"T8ParamConfigModel",
  data() {
    return {
      formData: {},
      selectRowData: {}
    };
  },
  methods: {
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
    }
  }
};
</script>
