<template>
  <div>
    <div>
      <k-form-search-customize data-model-name="FieldsDispose" v-model="source" data-target="disclosureSourceGrid">
        <k-form-item label="字段描述">
          <k-field-text v-model="source.columnLabel" :data-max-length="100"/>
        </k-form-item>
        <k-form-item label="字段key" >
          <k-field-text v-model="source.columnKey" :data-max-length="50"/>
        </k-form-item>
        <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addDisclosureSourcePopup"
               v-if="global.isShowAuthorityButton('DisclosureSource.addDisclosureSource')">
          <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="disclosureSourceGrid" @data-row-select="selectRow" data-action="DisclosureSource.findDisclosureSourcesAuth"
              data-operate-width="150px" data-fixed="right">
        <k-grid-column data-align="center" data-header="id" data-name="id" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="字段描述" data-name="columnLabel"></k-grid-column>
        <k-grid-column data-align="center" data-header="字段key" data-name="columnKey"></k-grid-column>
        <k-grid-column data-align="center" data-header="默认值" data-name="columnValue" ></k-grid-column>
        <k-grid-column data-align="center" data-header="数据字典" data-name="dict"></k-grid-column>
        <k-grid-column data-align="center" data-header="取值sql" data-name="valueSql" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="sql参数名" data-name="sqlParameter" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="数据格式" data-name="functype"></k-grid-column>
        <k-grid-column data-align="center" data-header="数据长度" data-name="dataLength" ></k-grid-column>
        <k-grid-column data-align="center" data-header="金额格式" data-name="moneyFormat" data-dict="xp_money_format" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="计算表达式" data-name="computedExpression"   data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="数据库数据源" data-name="dataSource" data-dict="xp_sys_data_source" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="备注" data-name="remark" data-hidden="true"></k-grid-column>
        <template slot="operate" slot-scope="scope" data-align="center" >
          <k-btn  class="md-info specialClass" data-descript="修改信披字段" data-functype="POPUP" data-size="mini" style="min-width:40px;"
                  data-target="editDisclosureSourcePopup" v-if="global.isShowAuthorityButton('DisclosureSource.updateDisclosureSource')">
            修改
          </k-btn>
          <k-btn class="md-danger specialClass" data-functype="SUBMIT"
                 data-action="DisclosureSource.deleteDisclosureSource" data-size="mini" style="min-width:40px;"
                 data-type="danger" data-target="disclosureSourceGrid" :data-confirm="true" data-descript="删除信披字段"
                 v-if="global.isShowAuthorityButton('DisclosureSource.deleteDisclosureSource')">
            删除
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    添加信批字段弹出框   -->
    <k-popup ref="addDisclosureSourcePopup" data-title="新增" >
      <k-form ref="addDisclosureSourceForm" :data-col="2" :isFormBodyScreen="true">
        <k-form-item label="字段描述">
          <k-field-text v-model="formData.columnLabel " :dataAllowblank="false" :data-max-length="100"/>
        </k-form-item>
        <k-form-item label="字段key">
          <k-field-text v-model="formData.columnKey" :dataAllowblank="false" :data-max-length="50" data-regx="^\S*$" data-regx-text="不允许输入为空或存在空格"/>
        </k-form-item>
        <k-form-item label="默认值">
          <k-field-text v-model="formData.columnValue" :dataAllowblank="true" :data-max-length="50"/>
        </k-form-item>
        <k-form-item label="数据字典">
          <k-field-text v-model="formData.dict" :dataAllowblank="true" :data-max-length="32"/>
        </k-form-item>
        <k-form-item label="取值SQL" :dataCol=2>
          <k-field-text v-model="formData.valueSql" :dataAllowblank="true" inputType="textarea" :rows="5"/>
        </k-form-item>
        <k-form-item label="sql参数名">
          <k-field-text v-model="formData.sqlParameter" :dataAllowblank="true" :data-max-length="256"/>
        </k-form-item>
        <k-form-item label="数据格式">
          <k-field-text v-model="formData.functype" :dataAllowblank="true" :data-max-length="50"/>
        </k-form-item>
        <k-form-item label="数据长度">
          <k-field-text v-model="formData.dataLength" :dataAllowblank="true" :data-max-length="50" data-regx="^\+?[1-9][0-9]*$" data-regx-text="请输入非零正整数"/>
        </k-form-item>
        <k-form-item label="金额格式">
          <k-field-select v-model="formData.moneyFormat" :dataAllowblank="true" data-dict="xp_money_format" />
        </k-form-item>
        <k-form-item label="计算表达式">
          <k-field-text v-model="formData.computedExpression" :dataAllowblank="true" :data-max-length="256"/>
        </k-form-item>
        <k-form-item label="数据库数据源">
          <k-field-select v-model="formData.dataSource" data-dict="xp_sys_data_source" :dataAllowblank="false"/>
        </k-form-item>
        <k-form-item label="备注" :data-max-length="256" :dataCol=2>
          <k-field-text v-model="formData.remark" :dataAllowblank="true" :data-max-length="500" inputType="textarea" :rows="3"/>
        </k-form-item>
      </k-form>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="DisclosureSource.addDisclosureSource" data-from="addDisclosureSourceForm"
                 :data-model="formData" data-target="disclosureSourceGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
    </k-popup>

    <!--    修改信披字段弹出框   -->
    <k-popup ref="editDisclosureSourcePopup" data-title="修改"  >
      <k-form ref="editDisclosureSourceForm" :data-col="2" :isFormBodyScreen="true">
        <k-form-item label="id"  :hidden="true">
          <k-field-text v-model="formData.id"/>
        </k-form-item>
        <k-form-item label="字段描述">
          <k-field-text v-model="formData.columnLabel" :dataAllowblank="false" :data-max-length="100"/>
        </k-form-item>
        <k-form-item label="字段key">
          <k-field-text v-model="formData.columnKey" :dataAllowblank="false" :data-disabled="false" :data-max-length="50"/>
        </k-form-item>
        <k-form-item label="默认值">
          <k-field-text v-model="formData.columnValue" :data-max-length="50" />
        </k-form-item>
        <k-form-item label="数据字典">
          <k-field-text v-model="formData.dict" :data-max-length="32"/>
        </k-form-item>
        <k-form-item label="取值sql" :dataCol=2>
          <k-field-text v-model="formData.valueSql"  inputType="textarea" :rows="5"/>
        </k-form-item>
        <k-form-item label="sql参数名">
          <k-field-text v-model="formData.sqlParameter" :data-max-length="256"/>
        </k-form-item>
        <k-form-item label="数据格式">
          <k-field-text v-model="formData.functype" :dataAllowblank="true" :data-max-length="50"/>
        </k-form-item>
        <k-form-item label="数据长度">
          <k-field-text v-model="formData.dataLength" :data-max-length="50" data-regx="^\+?[1-9][0-9]*$" data-regx-text="请输入非零正整数"/>
        </k-form-item>
        <k-form-item label="金额格式">
          <k-field-select v-model="formData.moneyFormat"  data-dict="xp_money_format"/>
        </k-form-item>
        <k-form-item label="计算表达式">
          <k-field-text v-model="formData.computedExpression" :data-max-length="256" />
        </k-form-item>
        <k-form-item label="数据库数据源">
          <k-field-select v-model="formData.dataSource" data-dict="xp_sys_data_source" :dataAllowblank="false"/>
        </k-form-item>
        <k-form-item label="备注" ::data-max-length="256" :dataCol=2>
          <k-field-text v-model="formData.remark" :data-max-length="500" inputType="textarea" :rows="3"/>
        </k-form-item>
      </k-form>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="DisclosureSource.updateDisclosureSource" data-from="editDisclosureSourceForm"
                 :data-model="formData" data-target="disclosureSourceGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
    </k-popup>
  </div>
</template>

<script>
export default {
  name:"FieldsDispose",
  data() {
    return {
      source:{},
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
<style scoped>
>>> .el-table__cell {
  padding: 0px 0 !important;
}
>>> .specialClass > .md-ripple{
  padding: 8px !important;
}
</style>
