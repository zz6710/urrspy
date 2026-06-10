<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="AppraiseRegistInfo" data-target="appraiseRegistInfoGrid"
                               v-model="searchParam" data-label-width="130px">
        <k-form-item label="资产/负债编码" data-label-width="100px">
          <k-field-text v-model="searchParam.assetCode"/>
        </k-form-item>
        <k-form-item label="估值日期" data-label-width="80px">
          <k-field-date v-model="BreathDay" data-type="daterange" data-date-format="yyyyMMdd"
                        data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="报送状态" data-label-width="80px">
          <k-field-select v-model="searchParam.registerStatus" data-dict="subm_report_status"/>
        </k-form-item>
        <!-- <k-form-item label="理论报送起始日期">
          <k-field-date v-model="queryParamDateRange" data-type="daterange" data-date-format="yyyy-MM-dd"
                        data-value-format="yyyyMMdd"/>
        </k-form-item> -->

      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
<!--          <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}"
               data-target="addAppraiseRegistInfoPopup" slot="button">
            <md-icon md-src="/static/svg/add.svg" />新增
          </k-btn>
          <k-btn slot="button" data-functype="POPUP" class="btn-custom-plain" data-target="uploadAssetDebtRegisterInfoPopup"
                v-if="global.isShowAuthorityButton('AppraiseRegistInfo.importAppraiseRegistInfo')"
          >
            <md-icon>cloud_upload</md-icon>
            导入
          </k-btn>-->
          <k-btn slot="button" class="btn-custom-plain" data-functype="EXPORT" data-target="appraiseRegistInfoGrid" data-export-dict="true"
                :data-export-name="'估值信息登记管理'">
            <md-icon>cloud_download</md-icon>
            导出
          </k-btn>
<!--          <k-btn slot="button" class="btn-custom-plain" data-functype="POPUP"-->
<!--                :data-handler="auditPopup"-->
<!--                v-if="global.isShowAuthorityButton('DisclosureNotice.batchPublishChannel')">-->
<!--            <md-icon md-src="/static/svg/confirm.svg"></md-icon>复核-->
<!--          </k-btn>-->
        </div>
      </div>
      <k-grid ref="appraiseRegistInfoGrid" @data-row-select="selectRow" data-fixed="right"
              data-operate-width="250px" data-action="AppraiseRegistInfo.findAppraiseRegistInfos">
<!--        <k-grid-column data-align="left" data-header="复核状态" data-name="auditStatus" data-export="false" data-dict="xp_disclosure_check_status"></k-grid-column>-->
        <k-grid-column data-align="left" data-header="报送状态" data-name="registerStatus"
                       data-dict="subm_report_status" data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="行内资产/负债编码" data-name="assetCode"></k-grid-column>
        <k-grid-column data-align="left" data-header="发行机构代码" data-name="bankCode"></k-grid-column>
        <k-grid-column data-align="left" data-header="id" data-name="id" :data-hidden="true"
                       data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="估值日期" data-name="valuationDate"
                       data-type="date"></k-grid-column>
        <k-grid-column data-align="right" data-header="单位估值（净价）" data-name="unitDebtNet"></k-grid-column>
        <k-grid-column data-align="right" data-header="单位估值（全价）" data-name="unitDebtFull"></k-grid-column>
        <k-grid-column data-align="left" data-header="备注" data-name="details"></k-grid-column>
        <k-grid-column data-align="left" data-header="登记流水号" data-name="registerSerno" data-export="false"
                       data-width="250"></k-grid-column>
        <k-grid-column data-align="left" data-header="导入日期" data-name="impDate" data-type="date"
                       data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="登记日期" data-name="registerDate" data-type="date"
                       data-export="false"></k-grid-column>
        <!-- <k-grid-column data-align="left" data-header="理论报送起始日期" data-name="theoryReportStartDate"
                       data-type="date" data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="理论报送截止日期" data-name="theoryReportEndDate" data-type="date"
                       data-export="false"></k-grid-column> -->
        <k-grid-column data-align="left" data-header="新增日期" data-name="createDate" data-type="date"
                       data-export="false"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改估值信息登记管理" data-functype="POPUP" data-size="mini"
                 data-target="editAppraiseRegistInfoPopup">
            修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="AppraiseRegistInfo.deleteAppraiseRegistInfo"
                 data-size="mini"
                 data-type="danger" data-target="appraiseRegistInfoGrid" :data-confirm="true"
                 data-descript="删除估值信息登记管理">
            删除
          </k-btn>
          <k-btn class="btn-custom-text" data-size="mini"
                 data-functype="POPUP" data-target="editAssetDebtRegisterInfoPopup"
                 data-descript="估值信息登记管理错误详情">
            错误详情
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    添加估值信息登记管理弹出框   -->
    <k-popup ref="addAppraiseRegistInfoPopup" data-title="新增">
      <k-form ref="addAppraiseRegistInfoForm" :data-col="2" isFormBodyScreen>
        <k-form-item label="发行机构代码">
          <k-field-text v-model="formData.bankCode" :data-allowblank="false" :data-max-length="6"/>
        </k-form-item>
        <k-form-item label="行内资产/负债编码">
          <k-field-text v-model="formData.assetCode" :data-allowblank="false" :data-max-length="40"/>
        </k-form-item>
        <k-form-item label="估值日期">
          <k-field-date v-model="formData.valuationDate" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="单位估值（净价）">
          <k-field-text v-model="formData.unitDebtNet" data-integer-length="13" data-digits="4"
                        data-validate-type="money" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="单位估值（全价）">
          <k-field-text v-model="formData.unitDebtFull" data-integer-length="13" data-digits="4"
                        data-validate-type="money" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="备注">
          <k-field-text v-model="formData.details" :data-max-length="256"/>
        </k-form-item>
        <k-form-footer slot="footer" data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="AppraiseRegistInfo.addAppraiseRegistInfo"
                 data-from="addAppraiseRegistInfoForm"
                 :data-model="formData" data-target="appraiseRegistInfoGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改估值信息登记管理弹出框   -->
    <k-popup ref="editAppraiseRegistInfoPopup" data-title="修改">
      <k-form ref="editAppraiseRegistInfoForm" :data-col="2" isFormBodyScreen>
        <k-form-item label="发行机构代码">
          <k-field-text v-model="formData.bankCode" :data-allowblank="false" :data-max-length="6"/>
        </k-form-item>
        <k-form-item label="行内资产/负债编码">
          <k-field-text v-model="formData.assetCode" :data-allowblank="false" :data-max-length="40"/>
        </k-form-item>
        <k-form-item label="估值日期">
          <k-field-date v-model="formData.valuationDate" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="单位估值（净价）">
          <k-field-text v-model="formData.unitDebtNet" data-integer-length="13" data-digits="4"
                        data-validate-type="money" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="单位估值（全价）">
          <k-field-text v-model="formData.unitDebtFull" data-integer-length="13" data-digits="4"
                        data-validate-type="money" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="备注">
          <k-field-text v-model="formData.details" :data-max-length="256"/>
        </k-form-item>
        <k-form-footer slot="footer" data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="AppraiseRegistInfo.updateAppraiseRegistInfo"
                 data-from="editAppraiseRegistInfoForm"
                 :data-model="formData" data-target="appraiseRegistInfoGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="uploadAssetDebtRegisterInfoPopup" title="报送数据导入" @data-opened="uploadOpened()">
      <k-form ref="addForm" data-ui="element">
        <k-form-item label="理论报送起始日期">
          <k-field-date v-model="uploadBeginDate" data-type="date" data-date-format="yyyy-MM-dd"
                        data-value-format="yyyyMMdd" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="至">
          <k-field-date v-model="uploadQueryDate" data-type="date" data-date-format="yyyy-MM-dd"
                        data-value-format="yyyyMMdd" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="报送数据导入" data-ui="element" data-input-width="500px">
          <k-field-excel-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit='1'
                                data-accept=".xlsx,.xls"
                                :data-error="onSubmitError" :data-success="onSubmitSuccess"
                                :data-auto-upload="false"
                                data-upload-url="upload/server/RptApp/reportManage/appraiseImport.json">
          </k-field-excel-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="appraiseRegistInfoGrid" ref="submitBtn"
                 :data-auto-upload="false" data-from="addForm" :data-handler="submitUploadParam">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
    <k-popup ref="auditInfoPopup">
      <KAudit v-model="infoPop" :infoPop="infoPop" @auditFunc="closeAuditFunc">
      </KAudit>
    </k-popup>
  </div>
</template>

<script>
import KFieldExcelUpload from '@/components/k-element/k-field-excel-upload/k-field-excel-upload.vue'
import KAudit from "@/pages/zz/manage/Audit.vue";

export default {
  name: "AppraiseRegistInfo",
  components: {KAudit, KFieldExcelUpload},
  data() {
    return {
      formData: {},
      selectRowData: {},
      searchParam: {},
      BreathDay: [],
      uploadBeginDate: '',
      uploadQueryDate: '',
      queryParamDateRange: [],

      infoPop: {},
      auditInfoPopupData: {
        tableId: 'app_appraise_regist_info',
        tableName: '估值信息登记管理'
      }
    };
  },
  methods: {
    submitUploadParam() {
      //文件上传校验
      let validate = this.$refs.addForm.validate();
      if (validate) {
        let formData = {beginDate: this.uploadBeginDate, queryDate: this.uploadQueryDate};
        let temp = document.getElementsByClassName('upload-demo');
        let lis = temp[0].childNodes[1].childNodes.length;
        if (lis > 0) {
          this.$refs.uploadRef.upload(formData);
        } else {
          this.$message.error("上传文件不能为空!");
          return false;
        }
      }
    },
    onSubmitSuccess() {
      this.$refs.uploadRef.doReset();
      this.$refs.addForm.reset();
      this.$refs.uploadAssetDebtRegisterInfoPopup.close();
      this.$refs.appraiseRegistInfoGrid.load(this.searchParam);
    },
    onSubmitError() {
      this.$refs.uploadRef.doReset();
      this.$refs.submitBtn.setIconStyle(1, [])
    },
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
    },
    uploadOpened() {
      this.uploadBeginDate = ''
      this.uploadQueryDate = ''
    },
    // 复核弹窗
    auditPopup() {
      this.infoPop = this.auditInfoPopupData;
      this.$refs.auditInfoPopup.popup();
    },
    // 复核弹窗关闭
    closeAuditFunc(val) {
      console.log(val);
      this.$nextTick(() => {
        this.$refs.appraiseRegistInfoGrid.load();
      })
      this.$refs.auditInfoPopup.close();
    },
  },
  watch: {
    //查询起息日
    BreathDay() {
      this.$set(this.searchParam, 'startDate', this.BreathDay == null ? '' : this.BreathDay[0]);
      this.$set(this.searchParam, 'endDate', this.BreathDay == null ? '' : this.BreathDay[1]);
    },
    queryParamDateRange() {
      this.$set(this.searchParam, 'queryStartDate', this.queryParamDateRange == null ? '' : this.queryParamDateRange[0]);
      this.$set(this.searchParam, 'queryEndDate', this.queryParamDateRange == null ? '' : this.queryParamDateRange[1]);
    },
  }
};
</script>
