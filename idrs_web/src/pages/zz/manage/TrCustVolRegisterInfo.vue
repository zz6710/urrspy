<template>
  <div class="py-page">
    <div>

      <k-form-search-customize ref="searchFormRef" v-model="queryParam" data-target="trCustVolRegisterInfoGrid" data-label-width="150px">
        <k-form-item label="数据日期">
          <k-field-date v-model="queryParam.reportDate" :data-allowblank="false" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="识别标识">
          <k-field-text v-model="queryParam.custNo" data-validate-type="text"/>
        </k-form-item>
        <k-form-item label="产品登记编码">
          <k-field-text v-model="queryParam.prodCode" data-validate-type="text"/>
        </k-form-item>
        <k-form-item label="币种">
          <k-field-select v-model="queryParam.cur" data-dict="tr_cur"></k-field-select>
        </k-form-item>
        <k-form-item label="报送状态">
          <k-field-select v-model="queryParam.registerStatus" data-dict="subm_report_status"></k-field-select>
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addTrCustVolRegisterInfoPopup" v-if="global.isShowAuthorityButton('TrCustVolRegisterInfo.addTrCustVolRegisterInfo')">
            <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
          <k-btn slot="button" ref="uploadBtnRef" :load-disabled="false" data-functype="POPUP" class="btn-custom-plain"
                data-target="importTrCustVolRegisterInfoPopup" v-if="global.isShowAuthorityButton('TrCustVolRegisterInfo.download')">
            <md-icon>cloud_upload</md-icon>
            导入
          </k-btn>
          <!-- <k-btn slot="button" class="btn-custom-plain" :data-download-name="'投资者持有信息导入模板'+'.xlsx'"
                data-descript="下载Excel模板" data-functype="DOWNLOAD" data-size="small"
                data-url="/download/server/RptApp/chinaBondSubmit/TrCustVolRegisterInfo/comn-download.json">
            <md-icon>cloud_download</md-icon>
            下载Excel模板
          </k-btn> -->
          <k-btn slot="button" ref="exportBtn" data-functype="POPUP" :handleBefore="handleBefore" class="btn-custom-plain" data-target="exportTrCustVolRegisterInfoPopup" v-if="global.isShowAuthorityButton('TrCustVolRegisterInfo.download')">
            <md-icon>cloud_download</md-icon>
            导出
          </k-btn>
           <k-btn slot="button" :handleBefore="handleBefore"  class="btn-custom-plain"  data-functype="SUBMIT" :data-model="queryParam" data-action="TrCustVolRegisterInfo.updateCustVolRegisterInfoRegistStatusSuccess">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>
            确认报送状态
          </k-btn>
          <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="auditPopup" v-if="global.isShowAuthorityButton('TrCustVolRegisterInfo.download')">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>复核
          </k-btn>
          <k-btn slot="button" ref="uploadBtnRefB" data-functype="POPUP" class="btn-custom-plain"
                  data-target="uploadTrCustVolRegisterInfoPopup" v-if="global.isShowAuthorityButton('TrCustVolRegisterInfo.batchImport')">
            <md-icon>cloud_upload</md-icon>批量修改导入
          </k-btn>
        </div>
      </div>
      <k-grid ref="trCustVolRegisterInfoGrid" @data-row-select="selectRow" data-fixed="right" data-operate-width="250px"
              data-action="TrCustVolRegisterInfo.findTrCustVolRegisterInfos" :data-autoload="false">
        <k-grid-column data-align="left" data-header="ID" data-name="id" data-export="false" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="数据日期" data-name="reportDate" data-export="false" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="**登记机构代码" data-name="bankCode" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="**产品登记编码" data-name="prodCode" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="**识别标识" data-name="custNo" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="**持有日期" data-name="holdDate"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="**币种" data-name="cur" data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="**持有份额" data-name="holdVol" data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="**持有金额" data-name="holdAmt" data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="**折算人民币金额（元）" data-name="convertRmb" data-width="120"></k-grid-column>
		    <k-grid-column data-align="left" data-header="登记流水号" data-name="registerSerno" data-export="false" data-width="250"></k-grid-column>
        <k-grid-column data-align="left" data-header="登记日期" data-name="registerDate"  data-export="false" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="报送状态" data-name="registerStatus" data-dict="subm_report_status" data-export="false" data-width="120"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改投资者持有信息" data-functype="POPUP" data-size="mini"
             v-show="scope.row.row.registerStatus != '5'"     data-target="editTrCustVolRegisterInfoPopup" v-if="global.isShowAuthorityButton('TrCustVolRegisterInfo.updateTrCustVolRegisterInfo')">
            修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="TrCustVolRegisterInfo.deleteTrCustVolRegisterInfo" data-size="mini"
              v-show="scope.row.row.registerStatus != '5'"    data-type="danger" data-target="trCustVolRegisterInfoGrid" :data-confirm="true" data-descript="删除投资者持有信息登记管理" >
            删除
          </k-btn>
          <!--<k-btn data-functype="PAGE" data-size="mini" class="btn-custom-plain" :data-model="scope.row.row.id"
                 @click="popupEdit(scope.row.row)"  data-descript="投资者持有信息错误详情">
            错误详情
          </k-btn>-->
          <k-btn class="btn-custom-text" data-descript="校验失败详情" data-functype="POPUP"
                 data-target="validateInfoPopup" v-if="scope.row.row.registerStatus === '1' ">
            校验失败详情
          </k-btn>
        </template>
      </k-grid>
    </div>
  <!--    校验失败详情弹出框   -->
  <k-popup ref="validateInfoPopup" data-title="详情">
    <ErroComp ref="ErroComp" @loadGriding="loadGriding"
              :info="{...formData,...auditInfoPopupData}"
              :disabledVal="false"/>
  </k-popup>
	<!--    添加投资者持有信息弹出框   -->
	<k-popup ref="addTrCustVolRegisterInfoPopup" data-title="新增">
    	<k-form ref="addTrCustVolRegisterInfoForm" :data-col="2" isFormBodyScreen>
        <k-form-item label="数据日期">
          <k-field-date v-model="formData.reportDate" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
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
              <k-field-text v-model="formData.holdAmt" :data-allowblank="false" data-validate-type="money"  data-digits="5" data-integer-length="13"/>
          </k-form-item>
        <k-form-item label="折算人民币金额">
             <k-field-text v-model="formData.convertRmb" :data-allowblank="false" data-validate-type="money"  data-digits="5" data-integer-length="13"/>
        </k-form-item>
        <k-form-footer slot="footer" data-align="center">
          <k-btn class="btn-custom-primary"   ref="sumbitadd" :data-handler="sumbit_add"   data-from="addTrCustVolRegisterInfoForm"
                 :data-model="formData" data-target="trCustVolRegisterInfoGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改投资者持有信息弹出框   -->
	<k-popup ref="editTrCustVolRegisterInfoPopup" data-title="修改">
	  <k-form ref="editTrCustVolRegisterInfoForm" :data-col="2" isFormBodyScreen>
	    <k-form-item label="数据日期" :class="[handleItemDiff('reportDate')]">
        <k-field-date v-model="formData.reportDate" data-date-format="yyyyMMdd"  :data-disabled="true" data-value-format="yyyyMMdd"/>
      </k-form-item>
      <k-form-item label="登记机构代码" :class="[handleItemDiff('bankCode')]">
        <k-field-text v-model="formData.bankCode" :data-allowblank="false"  :data-disabled="true"  :data-max-length="6"/>
      </k-form-item>
      <k-form-item label="产品登记编码" :class="[handleItemDiff('prodCode')]">
        <k-field-text v-model="formData.prodCode" :data-allowblank="false"  :data-disabled="true"  :data-max-length="15"/>
      </k-form-item>
      <k-form-item label="识别标识" :class="[handleItemDiff('custNo')]">
        <k-field-text v-model="formData.custNo" :data-allowblank="false"  :data-disabled="true"  :data-max-length="30"/>
      </k-form-item>
      <k-form-item label="持有日期" :class="[handleItemDiff('holdDate')]">
        <k-field-date v-model="formData.holdDate" :data-allowblank="false"  :data-disabled="true"   data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd"/>
      </k-form-item>
      <k-form-item label="币种" :class="[handleItemDiff('cur')]">
        <k-field-select v-model="formData.cur" data-dict="tr_cur" :data-allowblank="false" :data-default-value="'CNY'"/>
      </k-form-item>
      <k-form-item label="持有份额" :class="[handleItemDiff('holdVol')]">
        <k-field-text v-model="formData.holdVol" :data-allowblank="false" data-validate-type="money"  data-digits="5" data-integer-length="13"/>
      </k-form-item>
      <k-form-item label="持有金额" :class="[handleItemDiff('holdAmt')]">
        <k-field-text v-model="formData.holdAmt" :data-allowblank="false" data-validate-type="money"  data-digits="5" data-integer-length="13"/>
      </k-form-item>
      <k-form-item label="折算人民币金额" :class="[handleItemDiff('convertRmb')]">
        <k-field-text v-model="formData.convertRmb" :data-allowblank="false" data-validate-type="money"  data-digits="5" data-integer-length="13"/>
      </k-form-item>

	    <k-form-footer slot="footer" data-align="center">
	      <k-btn class="btn-custom-primary"  ref="sumbitedit" :data-handler="sumbit_edit"  data-from="editTrCustVolRegisterInfoForm"
	        :data-model="formData" data-target="trCustVolRegisterInfoGrid" :handle-before="handleBeforeUpdate">
	        <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
	      </k-btn>
	      <k-btn class="btn-custom-plain" data-functype="CLOSE">
	        <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	    </k-form-footer>
	  </k-form>
	</k-popup>



    <!-- 模板上传 -->
    <k-popup ref="addPopup" title="上传Excels">
      <k-form ref="addForm" data-ui="element">
        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-excel-upload data-type="picture" ref="uploadRef" :data-multiple="false" :data-limit=1
                                :data-error="onSubmitError" :data-success="onSubmitSuccess" data-accept=".xlsx,.xls"
                                :data-auto-upload="false"
                                data-upload-url="/upload/server/PmsApp/chinaBondSubmit/TrCustVolRegisterInfo/comn-upload.json">
          </k-field-excel-upload>
        </k-form-item>
        <k-form-footer slot="footer" data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="trCustVolRegisterInfoGrid" ref="submitBtn"
                 :data-auto-upload="false" data-from="addForm" :data-handler="submitUploadParam">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>


    <k-popup ref="uploadTrCustVolRegisterInfoPopup" data-title="批量修改导入" @data-opened="uploadOpened()">
        <k-form ref="addForm" data-ui="element">
          <k-form-item label="报送日期">
            <k-field-date v-model="formData.reportDate" :data-allowblank="false" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
          </k-form-item>
          <k-form-item label="批量修改导入" data-ui="element" data-input-width="500px">
            <k-field-excel-upload data-type="file" ref="uploadRefB" :data-multiple="false" :data-limit='1'
                data-accept=".xlsx,.xls"
                :data-error="onSubmitErrorB" :data-success="onSubmitSuccessB"
                :data-auto-upload="false"
                data-upload-url="upload/server/RptApp/reportManage/custVolRegistUpdateImport.json">
            </k-field-excel-upload>
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="trCustVolRegisterInfoGrid" ref="submitBtn"
                  :data-auto-upload="false" data-from="addForm" :data-handler="submitUpdateUploadParam">
                  <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
          </k-form-footer>
        </k-form>
        </k-popup>
    <k-popup ref="importTrCustVolRegisterInfoPopup" data-title="导入" @data-opened="uploadOpened()">
        <k-form ref="addForm" data-ui="element">
          <!-- <k-form-item label="数据日期">
            <k-field-date v-model="formData.reportDate" data-action="TrCustVolRegisterInfo.findSysDate" :data-allowblank="false"  :data-disabled="true" data-value-field="reportDate" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
          </k-form-item> -->
          <k-form-item label="导入" data-ui="element" data-input-width="500px">
            <k-field-excel-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit='1'
                data-accept=".xlsx,.xls"
                :data-error="onSubmitError" :data-success="onSubmitSuccess"
                :data-auto-upload="false"
                data-upload-url="upload/server/RptApp/reportManage/custVolRegistImport.json">
            </k-field-excel-upload>
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="trCustVolRegisterInfoGrid" ref="submitBtn"
                  :data-auto-upload="false" data-from="addForm" :data-handler="submitImportUploadParam">
                  <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
          </k-form-footer>
        </k-form>
        </k-popup>

    <k-popup ref="exportTrCustVolRegisterInfoPopup" data-title="报送数据导出" @data-opened="exportOpened()">
        <k-form data-ui="element">
          <k-form-item label="数据日期">
				    <k-field-date v-model="reportDateRange" :data-allowblank="false"
                 data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
			    </k-form-item>
          <k-form-item label="识别标识">
            <k-field-text v-model="exportParam.custNo" data-validate-type="text"/>
          </k-form-item>
          <k-form-item label="产品登记编码">
            <k-field-text v-model="exportParam.prodCode" data-validate-type="text"/>
          </k-form-item>
          <k-form-item label="币种">
            <k-field-select v-model="exportParam.cur" data-dict="tr_cur"/>
          </k-form-item>
          <k-form-item label="报送状态">
            <k-field-select v-model="exportParam.registerStatus" data-dict="subm_report_status"/>
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn ref="exportBtnRef" class="btn-custom-primary" data-functype="EXPORT" data-target="trCustVolRegisterInfoGrid"
                 data-action="TrCustVolRegisterInfo.download" :data-export-name="'投资者持有信息登记管理'" :report-date="exportParam.reportDates" :data-handler="handleConfirmExport">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
          </k-form-footer>
        </k-form>
        </k-popup>

    <k-popup ref="auditInfoPopup">
      <k-form ref="updateAuditStatusForm" :data-col="2" isFormBodyScreen>
         <k-form-item label="报表类型">
           <k-field-text v-model="infoPop.tableName" :data-allowblank="false" data-disabled="true"/>
         </k-form-item>
         <k-form-item label="数据日期" data-label-width="100px">
           <k-field-date v-model="queryParamDateRange" data-type="daterange" data-date-format="yyyyMMdd"
                         data-value-format="yyyyMMdd" :data-allowblank="false"/>
         </k-form-item>
         <k-form-item label="复核状态">
           <k-field-select v-model="infoPop.auditStatus" data-dict="xp_disclosure_check_status" data-default-value="1" data-disabled="true"/>
         </k-form-item>
         <k-form-footer slot="footer" data-align="center">
           <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-from="updateAuditStatusForm" data-target="prodIssuanceRegistInfoGrid"
                  @click="audit" :data-model="infoPop"><md-icon md-src="/static/svg/confirm.svg"></md-icon>确定</k-btn>
           <k-btn class="btn-custom-plain" data-functype="CLOSE"><md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
         </k-form-footer>
      </k-form>
    </k-popup>
  </div>
</template>

<script>
import Tools from "@/utils/tools";
import ErroComp from "@/pages/zz/manage/validateInfo";

export default {
  name: "TrCustVolRegisterInfo",
  components: {ErroComp},
  data() {
    return {
      formData: {},
      formDataCopy: {},
      queryParam: {},
      selectRowData: {},
      holdDate: [],
      queryParamDateRange: [],
      beforeDate: '',
      exportParam: {},
      reportDateRange: [],
      uploadBeginDate: '',
      uploadQueryDate: '',

      infoPop: {},
      auditInfoPopupData: {
        tableId: 'app_cust_vol_register_info',
        tableName: '投资者持有信息登记管理'
      }
    };
  },
  watch: {
    holdDate() {
      this.$set(this.queryParam, 'holdStartDate', this.holdDate == null ? null : this.holdDate[0]);
      this.$set(this.queryParam, 'holdEndDate', this.holdDate == null ? null : this.holdDate[1]);
    },
    queryParamDateRange() {
      this.$set(this.queryParam, 'queryStartDate', this.queryParamDateRange == null ? null : this.queryParamDateRange[0]);
      this.$set(this.queryParam, 'queryEndDate', this.queryParamDateRange == null ? null : this.queryParamDateRange[1]);
    },
    reportDateRange() {
      this.$set(this.exportParam, 'reportStartDate', this.reportDateRange == null ? null : this.reportDateRange[0]);
      this.$set(this.exportParam, 'reportEndDate', this.reportDateRange == null ? null : this.reportDateRange[1]);
      this.$set(this.exportParam, 'reportDates', this.reportDateRange[0]+'-'+this.reportDateRange[1]);
    }
  },
  methods: {
    handleBefore() {
			return this.$refs.searchFormRef.$refs.searchForm.validate();
		},
    handleConfirmExport() {
      if ((this.exportParam.reportStartDate == null || this.exportParam.reportStartDate == '') &&
          (this.exportParam.reportEndDate == null || this.exportParam.reportEndDate == '')) {
        this.$message.error('“数据日期”不能为空!');
        return false;
      }

			this.httpUtil
				.comnQuery({
					action: "BaseReportExportLog.fileStatusQuery",
					params: {
            reportName: this.$refs.exportBtnRef.dataExportName,
            reportStartDate: this.exportParam.reportStartDate,
            reportEndDate: this.exportParam.reportEndDate
          },
					successAlert: false,
				})
				.then((data) => {
					if (data.returndata.flag == '0') {
            this.exportParam.reportDate = data.returndata.reportDate;
            this.$refs.exportBtnRef.handleExport(this.exportParam);
					} else if (data.returndata.flag == '1') {
            Tools.alertTime(data.returnmsg, "danger", 0);
          } else if (data.returndata.flag == '2') {
            Tools.alertTime(data.returnmsg, "success", 3000);
          }
				});
			return false;
		},
    handleBeforeUpdate() {
      if (this.formNoChangeCb()) {
        this.$refs.editTrCustVolRegisterInfoPopup.close();
        return false
      }
      return true
    },
    sumbit_edit(){
            this.$refs.sumbitedit.setIconStyle(0,[]);
             if(this.$refs.editTrCustVolRegisterInfoForm.validate()){
                   this.httpUtil.query({
                           url: 'server/json/RptApp/audit/checkTrCustVolRegisterInfo.json',
                           params:  this.formData
                                    }).then(res => {
                                      if(res.success) {
                                       this.httpUtil.comnUpdate({
                                                action: 'TrCustVolRegisterInfo.updateTrCustVolRegisterInfo',
                                                params:  this.formData
                                                 }).then(res => {
                                                  if(res.success) {
                                                  this.$refs.editTrCustVolRegisterInfoPopup.close();
                                              }else{
                                                this.$refs.sumbitedit.setIconStyle(1,[]);
                                              }
                                    })
                              }else{
                                  this.$refs.sumbitedit.setIconStyle(1,[]);
                              }
                   });
              }else{
                this.$refs.sumbitedit.setIconStyle(1,[]);
              }
         },
    sumbit_add(){
        this.$refs.sumbitadd.setIconStyle(0,[]);
         if(this.$refs.addTrCustVolRegisterInfoForm.validate()){
               this.httpUtil.query({
                       url: 'server/json/RptApp/audit/checkTrCustVolRegisterInfo.json',
                       params:  this.formData
                                }).then(res => {
                                  if(res.success) {
                                   this.httpUtil.comnUpdate({
                                            action: 'TrCustVolRegisterInfo.addTrCustVolRegisterInfo',
                                            params:  this.formData
                                             }).then(res => {
                                              if(res.success) {
                                              this.$refs.addTrCustVolRegisterInfoPopup.close();
                                          }else{
                                            this.$refs.sumbitadd.setIconStyle(1,[]);
                                          }
                                })
                          }else{
                              this.$refs.sumbitadd.setIconStyle(1,[]);
                          }
               });
          }else{
            this.$refs.sumbitadd.setIconStyle(1,[]);
          }
     },
    audit() {
      let tableName = this.infoPop.tableName;
      let tableId = this.infoPop.tableId;
      let auditStatus = this.infoPop.auditStatus;
      let startDate = this.queryParamDateRange ? this.queryParamDateRange[0] : null;
      let endDate = this.queryParamDateRange ? this.queryParamDateRange[1] : null;
      this.httpUtil.ajax({
         url: 'server/json/RptApp/audit/indexstatus.json',
         params: {
           tableId: tableId,
           startDate: startDate,
           endDate: endDate,
           auditStatus: auditStatus
         }
       }).then(res => {
         if(res.success) {
           if(res.returnmsg=='存在指标校验未通过数据'){
               this.$confirm("日期区间存在指标校验未通过数据,确认复核吗？", "操作提示", {
               confirmButtonText: "确定",
               cancelButtonText: "取消",
               type: "warning"
               }).then(() => {
                    this.httpUtil.ajax({
                    url: 'server/json/RptApp/audit/status.json',
                    params: {
                      tableId: tableId,
                      startDate: startDate,
                      endDate: endDate,
                      auditStatus: auditStatus
                    }
                  }).then(res => {
                    if(res.success) {
                     Tools.alert(res.returnmsg, "success");
                     this.$refs.trCustVolRegisterInfoGrid.load(this.queryParam);
                     this.$refs.auditInfoPopup.close();
                    }
                  })
              }).catch(() => {});
           }else{
              this.httpUtil.ajax({
                url: 'server/json/RptApp/audit/status.json',
                params: {
                  tableId: tableId,
                  startDate: startDate,
                  endDate: endDate,
                  auditStatus: auditStatus
                }
              }).then(res => {
                if(res.success) {
                 Tools.alert(res.returnmsg, "success");
                 this.$refs.trCustVolRegisterInfoGrid.load(this.queryParam);
                 this.$refs.auditInfoPopup.close();
                }
              })
           }
         }
       })
    },
    popupEdit(row) {
      let pathUrl = '/main/zz/errorInfo/CustVolRgInfoErr';
      this.$router.push({
        path: pathUrl,
        query: {
          registerSerno: row.registerSerno,
        },
      });
    },
    onSubmitSuccess() {
      this.$refs.trCustVolRegisterInfoGrid.load(this.queryParam);
      this.$refs.uploadBtnRef.setIconStyle(1);
    },
    onSubmitError() {
      this.$refs.uploadBtnRef.setIconStyle(1);
    },
    onSubmitSuccessB() {
      this.$refs.trCustVolRegisterInfoGrid.load(this.queryParam);
      this.$refs.uploadBtnRefB.setIconStyle(1);
    },
    onSubmitErrorB() {
      this.$refs.uploadBtnRefB.setIconStyle(1);
    },
    submitImportUploadParam() {
      //文件上传校验
      var validate = this.$refs.addForm.validate();
      if (validate) {
        let formData = {reportDate: this.formData.reportDate};
        let temp = document.getElementsByClassName('upload-demo');
        let lis = temp[0].childNodes[1].childNodes.length;
        if (lis > 0) {
          this.$refs.uploadBtnRef.setIconStyle(0);
          this.$refs.uploadRef.upload(formData);
          setTimeout(()=>{
            this.$refs.importTrCustVolRegisterInfoPopup.close();
          }, 300)
        } else {
          Tools.alert("上传文件不能为空!", "danger");
        }
      }
      return false;
    },
    submitUpdateUploadParam() {
      //文件上传校验
      var validate = this.$refs.addForm.validate();
      if (validate) {
        let formData = {reportDate: this.formData.reportDate};
        let temp = document.getElementsByClassName('upload-demo');
        let lis = temp[0].childNodes[1].childNodes.length;
        if (lis > 0) {
          this.$refs.uploadBtnRefB.setIconStyle(0);
          this.$refs.uploadRefB.upload(formData);
          setTimeout(()=>{
            this.$refs.uploadTrCustVolRegisterInfoPopup.close();
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
        this.formDataCopy = Object.assign({}, row)
      },
    uploadOpened() {
      this.$set(this.formData, 'reportDate', this.queryParam.reportDate);
    },
    exportOpened() {
      if (this.queryParam.reportDate) {
        this.reportDateRange = [this.queryParam.reportDate, this.queryParam.reportDate];
        this.$set(this.exportParam, 'reportStartDate', this.queryParam.reportDate);
        this.$set(this.exportParam, 'reportEndDate', this.queryParam.reportDate);
        this.$set(this.exportParam, 'reportDates', this.queryParam.reportDate);
      } else {
        this.reportDateRange = [this.beforeDate, this.beforeDate];
        this.$set(this.exportParam, 'reportStartDate', this.beforeDate);
        this.$set(this.exportParam, 'reportEndDate', this.beforeDate);
        this.$set(this.exportParam, 'reportDates', this.beforeDate);
      }
      this.$set(this.exportParam, 'custNo', this.queryParam.custNo);
      this.$set(this.exportParam, 'prodCode', this.queryParam.prodCode);
      this.$set(this.exportParam, 'cur', this.queryParam.cur);
      this.$set(this.exportParam, 'registerStatus', this.queryParam.registerStatus);
    },
    // 复核弹窗
    auditPopup() {
      this.infoPop = this.auditInfoPopupData;
      this.httpUtil.sysDate().then(res=>{
        if (res) {
          this.queryParamDateRange = [res,res];
        }
      });
      this.$refs.auditInfoPopup.popup();
    },
    loadGriding(val){
      this.$refs.validateInfoPopup.close();
      this.$refs.trCustVolRegisterInfoGrid.load(this.queryParam);
    },
  },
  created() {
    let yesterday = new Date();
    yesterday.setDate(yesterday.getDate() -1);
    let year = yesterday.getFullYear(); //获取年
    let month = yesterday.getMonth(); //获取月
    let date = yesterday.getDate(); //得到日期
    month = month + 1;
    month = month.toString().padStart(2, "0");
    date = date.toString().padStart(2, "0");
    let  defaultDate = `${year}${month}${date}`;
    this.beforeDate = defaultDate;
    this.$set(this.queryParam, "reportDate", defaultDate);
  },

};
</script>
