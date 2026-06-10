<template>
  <div class="py-page">
    <k-form-search-customize data-target="valReportTabGrid1" data-model-name="valReportTab" v-model="searchParam">
      <k-form-item label="报送日期">
        <k-field-date v-model="searchParam.reportDate" data-type="date" data-date-format="yyyy-MM-dd" />
      </k-form-item>
    </k-form-search-customize>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}"
             data-target="addValReportTabPopup" >
            <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
        </div>
      </div>
    
      <k-grid ref="valReportTabGrid1" @data-row-select="selectRow" data-fixed="right"
              data-operate-width="380px" data-action="DataSupplementModel.findDataSupplements" :data-page-size="5">
        <k-grid-column data-header="报送日期" data-name="reportDate" data-type="date"></k-grid-column>
        <k-grid-column data-header="发行人表内资产余额（元）" data-name="balanceAssets" data-type="money"></k-grid-column>
        <k-grid-column data-header="发行人表内金融资产余额（元）" data-name="financialAssets" data-type="money"></k-grid-column>
        <k-grid-column data-header="备注" data-name="remark"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改报送数据" data-functype="POPUP" data-size="mini"
                data-target="editValReportTabPopup" >
            修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="DataSupplementModel.deleteDataSupplement" data-size="mini"
                data-type="danger" data-target="valReportTabGrid1" :data-confirm="true" data-descript="删除报送数据">
            删除
          </k-btn>
        </template>
      </k-grid>
    </div>
    <!--    添加估值表主体弹出框   -->
    <k-popup ref="addValReportTabPopup" data-title="新增">
      <k-form ref="addValReportTabForm" :data-col="2">
        <k-form-item label="报送日期">
          <k-field-date v-model="formData.reportDate" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="发行人表内资产余额（元）">
          <k-field-text v-model="formData.balanceAssets" :data-allowblank="false" :data-max-length="21" data-digits="2" data-min-value="(0"  data-integer-length="16" data-validate-type="money" data-regx-text="请输入正确小数位数的数值" />
        </k-form-item>
        <k-form-item label="发行人表内金融资产余额（元）">
          <k-field-text v-model="formData.financialAssets" :data-allowblank="false" :data-max-length="21" data-digits="2" data-min-value="(0"  data-integer-length="16" data-validate-type="money" data-regx-text="请输入正确小数位数的数值" />
        </k-form-item>
        <k-form-item label="备注">
          <k-field-text v-model="formData.remark"/>
        </k-form-item>
        <k-form-item label="创建日期" v-show="false">
          <k-field-text v-model="formData.crtDate"/>
        </k-form-item>
        <k-form-item label="创建时间" v-show="false">
          <k-field-text v-model="formData.crtTime"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"  data-from="addValReportTabForm" :data-handler="beforeSubmit" ref="addSubmitBtn"
                 :data-model="formData" data-target="valReportTabGrid1">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改估值表主体弹出框   -->
    <k-popup ref="editValReportTabPopup" data-title="修改">
      <k-form ref="editValReportTabForm" :data-col="2">
        <k-form-item label="数据ID"  v-show="false">
          <k-field-text v-model="formData.id" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="报送日期">
          <k-field-date v-model="formData.reportDate" :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="发行人表内资产余额（元）">
          <k-field-text v-model="formData.balanceAssets" :data-allowblank="false" :data-max-length="21" data-digits="2" data-min-value="(0"  data-integer-length="16" data-validate-type="money" data-regx-text="请输入正确小数位数的数值" />
        </k-form-item>
        <k-form-item label="发行人表内金融资产余额（元）">
          <k-field-text v-model="formData.financialAssets" :data-allowblank="false" :data-max-length="21" data-digits="2" data-min-value="(0"  data-integer-length="16" data-validate-type="money" data-regx-text="请输入正确小数位数的数值" />
        </k-form-item>
        <k-form-item label="备注">
          <k-field-text v-model="formData.remark"/>
        </k-form-item>
        <k-form-item label="录入柜员" v-show="false">
          <k-field-text v-model="formData.inputuser" />
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"  data-from="editValReportTabForm" :data-handler="beforeEditSubmit" ref="editSubmitBtn"
                 :data-model="formData" data-target="valReportTabGrid1">
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
import Tools from '@/utils/tools.js';
import httpUtil from "@/frame/httpUtil";

export default {
  name: "ImportConfig",
  data() {
    return {
      formData: {},
      selectRowData: {},
      findFormData :{},
      searchParam:{},
    };
  },
  methods: {
    setFormData(){
      this.formData = {};
    },
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
    },
    beforeSubmit(value){
      this.httpUtil.comnQuery({
        action: "DataSupplementModel.findOnlyDataSupplements",
        params: {reportDate: this.formData.reportDate},
        successAlert: false
      }).then(data => {
        if (this.formData.reportDate === '' || this.formData.reportDate === undefined
            || this.formData.balanceAssets === '' || this.formData.balanceAssets === undefined
            || this.formData.financialAssets === '' || this.formData.financialAssets === undefined){
          return false;
        }
        if(data.rows.length>0){
          Tools.alert("该报送日期数据已经存在！","danger");
          this.$refs.addSubmitBtn.loading = false;
        } else {
          this.httpUtil.comnUpdate({
            action: "DataSupplementModel.addDataSupplement",
            params: this.formData,
          }).then(data => {
            this.$refs.addValReportTabPopup.close();
            this.$refs.valReportTabGrid1.load();
          });
        }


      });
    },
    beforeEditSubmit(value){
      this.httpUtil.comnQuery({
        action: "DataSupplementModel.findOnlyDataSupplements",
        params: {reportDate: this.formData.reportDate,id:this.formData.id},
        successAlert: false
      }).then(data => {
        if (this.formData.reportDate === '' || this.formData.balanceAssets === '' || this.formData.financialAssets === ''){
          return false;
        }
        if(data.rows.length>0){
          Tools.alert("该报送日期数据已经存在！","danger");
          this.$refs.editSubmitBtn.loading = false;
        } else {
          this.httpUtil.comnUpdate({
            action: "DataSupplementModel.updateDataSupplement",
            params: this.formData,
          }).then(data => {
            this.$refs.editValReportTabPopup.close();
            this.$refs.valReportTabGrid1.load();
          });
        }


      });
    },

  }
};
</script>
