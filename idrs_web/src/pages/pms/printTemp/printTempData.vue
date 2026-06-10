<template>
  <div>
    <k-form-search-customize data-target="printTempDataGrid" v-model="printTempData">

      <k-form-item label="是否信披">
        <k-field-select v-model="printTempData.isXpData" data-dict="t8_prod_isok"/>
      </k-form-item>
      <k-form-item label="信披模板类型" v-if="printTempData.isXpData==='1'">
        <k-field-select v-model="printTempData.xpDocType" data-dict="xp_doc_type"
                        />
      </k-form-item>
      <k-btn slot="button" data-functype="POPUP" style="width: 70px" class="btn-custom-primary" :data-handler="addHandler"
             data-target="addPopup" v-if="global.isShowAuthorityButton('PrintTempData.addPrintTempData')">
        <md-icon md-src="/static/svg/add.svg"/>
        添加
      </k-btn>
    </k-form-search-customize>


    <k-grid ref="printTempDataGrid" data-action="PrintTempData.getPrintTempDataList1" @data-row-select="selectRow">
      <k-grid-column data-align="center" data-header="数据源id" data-name="id"/>
      <!--<k-grid-column data-align="center" data-header="文档类型" data-name="docType" data-dict="t8_print_doc"/>
      <k-grid-column data-align="center" data-header="模板类型" data-dict="t8_temp_type" data-name="tempType"/>-->
      <k-grid-column data-align="center" data-header="数据类型" data-dict="t8_temp_data_type" data-name="dataType"/>
      <k-grid-column data-align="center" data-header="是否信披" data-dict="t8_prod_isok" data-name="isXpData"/>
      <k-grid-column data-align="center" data-header="信披模版类型" data-dict="xp_doc_type" data-name="xpDocType"/>
      <k-grid-column data-align="center" data-header="数据分组名称" data-name="dataGroupName"/>
      <k-grid-column data-align="center" data-header="上传日期" data-type="date" data-name="createDate"/>
      <k-grid-column data-align="center" data-header="上传时间" data-type="time" data-name="crtTime"/>
      <k-grid-column data-align="center" data-header="更新日期" data-type="date" data-name="updateDate"/>
      <k-grid-column data-align="center" data-header="更新时间" data-type="time" data-name="updTime"/>
      <template slot="operate">
        <k-btn class="md-info md-just-icon md-simple" data-descript="修改数据源sql" data-functype="POPUP" data-size="small"
               data-target="editPopup" :data-handler="editHandler"
               v-if="global.isShowAuthorityButton('updatePrintTempData')">
          <md-icon>edit</md-icon>
        </k-btn>
      </template>
    </k-grid>

    <k-popup ref="addPopup" title="新增文档数据源">
      <k-form ref="addForm" data-ui="element">
        <!--<k-form-item label="文档类型">
          <k-field-select v-model="formData.docType" :data-allowblank="false" data-dict="t8_print_doc"
                          @data-on-change="onAddDocTypeChange"/>
        </k-form-item>
        <k-form-item label="模板类型">
          <k-field-select v-model="formData.tempType" :data-allowblank="false" :data-data="addDocTypeDict"
                          data-value-field="value" data-display-field="value,text"/>
        </k-form-item>-->
        <k-form-item label="是否信披">
          <k-field-select v-model="formData.isXpData" data-dict="t8_prod_isok"
                          :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="信披模板类型" v-if="formData.isXpData==='1'">
          <k-field-select v-model="formData.xpDocType" data-dict="xp_doc_type"
                          :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="数据类型">
          <k-field-select v-model="formData.dataType" @data-on-change="onDataTypeChange" data-dict="t8_temp_data_type"
                          :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="数据分组名称" >
          <k-field-text v-model="formData.dataGroupName" :data-disabled="dataTypeFlag"/>
        </k-form-item>
        <k-form-item label="数据源Sql" :data-col="2">
          <k-field-text v-model="formData.sqlInfo" inputType="textarea" :rows="10"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="printTempDataGrid"
                 data-from="addForm" :data-model="formData" data-action="PrintTempData.addPrintTempData">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>


    <k-popup ref="editPopup" title="修改文档数据源">
      <k-form ref="editForm" data-ui="element">
        <k-form-item label="模板数据源id">
          <k-field-text v-model="editFormData.id" :data-disabled="true"/>
        </k-form-item>
       <!-- <k-form-item label="数据类型">
          <k-field-select v-model="editFormData.dataType" :data-disabled="true" data-dict="t8_temp_data_type"/>
        </k-form-item>-->
        <k-form-item label="是否信披">
          <k-field-select v-model="editFormData.isXpData" data-dict="t8_prod_isok"
                          :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="信披模板类型" v-if="editFormData.isXpData==='1'">
          <k-field-select v-model="editFormData.xpDocType" data-dict="xp_doc_type"
                          :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="数据分组名称">
          <k-field-text v-model="editFormData.dataGroupName" :data-disabled="editDataTypeFlag"/>
        </k-form-item>
        <k-form-item label="数据源Sql" :data-col="2">
          <k-field-text v-model="editFormData.sqlInfo" inputType="textarea" :rows="10"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="printTempDataGrid"
                 data-from="editForm" :data-model="editFormData" data-action="PrintTempData.updatePrintTempData">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
  </div>
</template>

<script>
  import {assign} from "lodash";

  export default {
    name: "printTempData",
    data() {
      return {
        printTempData: {
          docType: '',
          tempType: ''
        },
        docTypeDict: {},
        addDocTypeDict: {},
        dataTypeFlag: true,
        editDataTypeFlag: true,
        formData: {
          docType: '',
          tempType: '',
          dataType: '',
          dataGroupName: '',
          sqlInfo: ''
        },
        editFormData: {
          id: '',
          dataType: '',
          dataGroupName: '',
          sqlInfo: ''
        }
      }
    },
    methods: {
      onDocTypeChange() {
        this.printTempData.tempType = '';
        this.httpUtil.comnQuery({
          action: "PrintTemp.getTempTypeByDocType",
          params: {docType: this.printTempData.docType}
        }).then(data => {
          this.docTypeDict = data.rows;
        }).catch({})
      },
      onAddDocTypeChange() {
        this.formData.tempType = '';
        this.httpUtil.comnQuery({
          action: "PrintTemp.getTempTypeByDocType",
          params: {docType: this.formData.docType}
        }).then(data => {
          this.addDocTypeDict = data.rows;
        }).catch({})
      },
      addHandler() {
        this.formData.docType = '';
        this.formData.tempType = '';
        this.formData.dataGroupName = '';
        this.formData.sqlInfo = '';
      },
      onDataTypeChange(value) {
        this.formData.dataGroupName = '';
        this.dataTypeFlag = value == '1';
      },
      selectRow(row, column, event) {
        const _this = this;
        _this.selectRowData = assign({}, row);
        _this.formData = assign({}, row);
      },
      editHandler(value) {
        this.editFormData = value;
        console.log("this.editFormData=:>>>>",this.editFormData);
        if (this.editFormData.dataType == '1') {
          this.editDataTypeFlag = true;
        } else {
          this.editDataTypeFlag = false;
        }
      },
    }
  }
</script>

<style scoped>

</style>
