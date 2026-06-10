<template>
  <div class="py-page">
     <div>
       <k-form-search-customize data-model-name="CustVolRegisterInfoh" data-target="CustVolRegisterInfohGrid" v-model = "searchParam" :handleConfirm="handleConfirm">
         <k-form-item label="数据日期">
           <k-field-date v-model="searchParam.reportDate" data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd"/>
         </k-form-item>
         <k-form-item label="识别标识">
           <k-field-text v-model="searchParam.custNo" />
         </k-form-item>
         <k-form-item label="产品登记编码" >
           <k-field-text v-model="searchParam.prodCode"/>
         </k-form-item>
         <k-form-item label="币种">
           <k-field-text v-model="searchParam.cur" data-dict="tr_cur"/>
         </k-form-item>
        </k-form-search-customize>
      </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn slot="button" ref="uploadModifyBtnRef" :load-disabled="false" data-functype="POPUP" class="btn-custom-plain"
                data-target="uploadTrCustVolRegisterInfoModifyPopup">
            <md-icon>cloud_upload</md-icon>
            导入数据变更
          </k-btn>
          <k-btn slot="button" ref="exportBtnRef" class="btn-custom-plain"  data-functype="EXPORT" data-target="CustVolRegisterInfohGrid"
                 data-action="CustVolRegisterInfoh.historyDownload" :data-export-name="'投资者持有信息登记历史数据管理'"
                 :report-date="searchParam.reportDate" :cust-no="searchParam.custNo" :data-handler="handleConfirmExport">
            <md-icon>cloud_download</md-icon>
            导出
          </k-btn>
        </div>
      </div>

      <k-grid ref="CustVolRegisterInfohGrid" @data-row-select="selectRow" data-fixed="right" data-operate-column="false" data-operate-width="250px" data-action="CustVolRegisterInfoh.findCustVolRegisterInfohs" :data-autoload="false">
        <k-grid-column data-align="left" data-header="ID" data-name="id" data-export="false" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="数据日期" data-name="reportDate" data-export="false" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="**登记机构代码" data-name="bankCode"></k-grid-column>
        <k-grid-column data-align="left" data-header="**产品登记编码" data-name="prodCode"></k-grid-column>
        <k-grid-column data-align="left" data-header="**识别标识" data-name="custNo"></k-grid-column>
        <k-grid-column data-align="left" data-header="**持有日期" data-name="holdDate"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="**币种" data-name="cur" data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="**持有份额" data-name="holdVol" data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="**持有金额" data-name="holdAmt" data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="**折算人民币金额（元）" data-name="convertRmb"></k-grid-column>
		    <k-grid-column data-align="left" data-header="登记流水号" data-name="registerSerno" data-export="false" data-width="250"></k-grid-column>
        <k-grid-column data-align="left" data-header="登记日期" data-name="registerDate"  data-export="false" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="报送状态" data-name="registerStatus" data-dict="subm_report_status" data-export="false" data-width="120"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改投资者持有信息历史表" data-functype="POPUP" data-size="mini"
                 data-target="editTrCustVolRegisterInfoHPopup">
            修改
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    修改投资者持有历史信息弹出框   -->
    <k-popup ref="editTrCustVolRegisterInfoHPopup" data-title="修改">
      <k-form ref="editTrCustVolRegisterInfoHForm" :data-col="2" isFormBodyScreen>
        <k-form-item label="数据日期">
          <k-field-date v-model="formData.reportDate" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="登记机构代码">
              <k-field-text v-model="formData.bankCode" :data-allowblank="false"  :data-max-length="6"/>
          </k-form-item>
        <k-form-item label="产品登记编码">
              <k-field-text v-model="formData.prodCode" :data-allowblank="false"  :data-max-length="15"/>
          </k-form-item>
        <k-form-item label="识别标识">
              <k-field-text v-model="formData.custNo" :data-allowblank="false"  :data-max-length="30"/>
          </k-form-item>
        <k-form-item label="持有日期">
              <k-field-date v-model="formData.holdDate" :data-allowblank="false"   data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd"/>
          </k-form-item>
        <k-form-item label="币种">
              <k-field-select v-model="formData.cur" data-dict="tr_cur" :data-allowblank="false" :data-default-value="'CNY'"/>
          </k-form-item>
        <k-form-item label="持有份额">
              <k-field-text v-model="formData.holdVol" :data-allowblank="false" data-validate-type="money"  data-digits="5" data-integer-length="13"/>
          </k-form-item>
        <k-form-item label="持有金额">
              <k-field-text v-model="formData.holdAmt" :data-allowblank="false" data-validate-type="money"  data-digits="2" data-integer-length="13"/>
          </k-form-item>
        <k-form-item label="折算人民币金额">
              <k-field-text v-model="formData.convertRmb" :data-allowblank="false" data-validate-type="money"  data-digits="2" data-integer-length="13"/>
        </k-form-item>
        <k-form-item label="登记日期">
          <k-field-date v-model="formData.registerDate" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-footer slot="footer" data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="CustVolRegisterInfoh.updateCustVolRegisterInfoh" data-from="editTrCustVolRegisterInfoHForm"
            :data-model="formData" data-target="CustVolRegisterInfohGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="uploadTrCustVolRegisterInfoModifyPopup" data-title="投资者持有信息登记历史信息变更数据导入" @data-opened="uploadOpened()">
        <k-form ref="addModifyForm" data-ui="element">
          <k-form-item label="变更数据导入" data-ui="element" data-input-width="500px">
            <k-field-excel-upload data-type="file" ref="uploadModifyRef" :data-multiple="false" :data-limit='1'
                data-accept=".xlsx,.xls"
                :data-error="onModifySubmitError" :data-success="onModifySubmitSuccess"
                :data-auto-upload="false"
                data-upload-url="upload/server/RptApp/reportManage/custVolRegistModifyImport.json">
            </k-field-excel-upload>
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="prodRegistFilingInfoGrid" ref="submitBtn"
                  :data-auto-upload="false" data-from="addModifyForm" :data-handler="submitUploadModifyParam">
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
import Tools from "@/utils/tools";
export default {
  name: "CustVolRegisterInfoh",
  data() {
    return {
      formData: {},
      selectRowData: {},
      searchParam:{},
      RegisterDate:[],
      holdDate:[]
    };
  },
  methods: {
    handleConfirmExport() {
			this.httpUtil
				.comnQuery({
					action: "BaseReportExportLog.fileStatusQuery",
					params: {reportName: this.$refs.exportBtnRef.dataExportName},
					successAlert: false,
				})
				.then((data) => {
					if (data.returndata.flag == '0') {
            if (!this.handleConfirm()) {
              return false;
            }
            this.$refs.exportBtnRef.handleExport(this.searchParam);
					} else if (data.returndata.flag == '1') {
            Tools.alertTime(data.returnmsg, "danger", 0);
          } else if (data.returndata.flag == '2') {
            Tools.alertTime(data.returnmsg, "success", 3000);
          }
				});
			return false;
		},
    handleConfirm() {
      if (!this.searchParam.reportDate && !this.searchParam.custNo) {
        this.$message.error('“数据日期”和“识别标识”不能同时为空!');
        return;
      }
      return true;
    },
    onModifySubmitSuccess() {
      this.$refs.uploadModifyBtnRef.setIconStyle(1);
    },
    onModifySubmitError() {
      this.$refs.uploadModifyBtnRef.setIconStyle(1);
    },
    submitUploadModifyParam() {
      //文件上传校验
      var validate = this.$refs.addModifyForm.validate();
      if (validate) {
        let formData = { reportDate: ''};
        let temp = document.getElementsByClassName('upload-demo');
        let lis = temp[0].childNodes[1].childNodes.length;
        if (lis > 0) {
          this.$refs.uploadModifyBtnRef.setIconStyle(0);
          this.$refs.uploadModifyRef.upload(formData);
          setTimeout(()=>{
            this.$refs.uploadTrCustVolRegisterInfoModifyPopup.close();
          }, 300)
        } else {
          Tools.alert("上传文件不能为空!", "danger");
        }
      }
      return false;
    },
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
    },
    loadGriding(val){
      this.$refs.validateInfoPopup.close();
    },
  },
  watch: {
    //查询起息日
    RegisterDate() {
      this.$set(this.searchParam, 'startDate', this.RegisterDate == null ? '' : this.RegisterDate[0]);
      this.$set(this.searchParam, 'endDate', this.RegisterDate == null ? '' : this.RegisterDate[1]);
    },
    holdDate() {
      this.$set(this.searchParam, 'holdStartDate', this.holdDate == null ? null : this.holdDate[0]);
      this.$set(this.searchParam, 'holdEndDate', this.holdDate == null ? null : this.holdDate[1]);
    },
  }
};
</script>
