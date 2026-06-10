<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="UnderAssetRegistInfo" data-label-width="150px" data-target="underAssetRegistInfoGrid" v-model="searchParam" ref="searchRef">
        <k-form-item class="lh16" label="对应资管及委外资产行内资产/负债编码">
          <k-field-text v-model="searchParam.assetManagerCode"/>
        </k-form-item>
        <k-form-item class="lh16" label="底层资产行内资产/负债编码">
          <k-field-text v-model="searchParam.underAssetCode"/>
        </k-form-item>
        <k-form-item label="持仓日期(数据日期)">
          <k-field-date v-model="searchParam.startDate" data-type="date" data-date-format="yyyy-MM-dd"  data-value-format="yyyyMMdd" :data-allowblank="false"/>
        </k-form-item>
        <!-- <k-form-item label="理论报送起始日期">
          <k-field-date v-model="searchParam.theoryReportStartDate" data-type="date"  data-date-format="yyyy-MM-dd"  data-value-format="yyyyMMdd"/>
        </k-form-item> -->

      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
<!--           <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addUnderAssetRegistInfoPopup" slot="button">
            <md-icon md-src="/static/svg/add.svg" />新增</k-btn>-->
          <k-btn slot="button"  class="btn-custom-plain"  data-functype="EXPORT" data-target="underAssetRegistInfoGrid" data-export-dict="true" data-export-form="searchRef"
              :data-export-name="'底层资产持仓管理'">
            <md-icon>cloud_download</md-icon>
            导出
          </k-btn>
        </div>
      </div>
      <k-grid ref="underAssetRegistInfoGrid" @data-row-select="selectRow" data-fixed="right" data-operate-column="false" data-action="UnderAssetRegistInfo.findUnderAssetRegistInfos_day" :data-autoload="false" >
         <k-grid-column data-align="left" data-header="发行机构编码" data-name="id" :data-hidden="true"  data-export="false" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="发行机构代码" data-name="bankCode" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="对应资管及委外资产行内资产/负债编码" data-name="assetManagerCode" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="资管及委外资产当前总数量" data-name="assetSumNumber" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="资管及委外资产当前总折算人民币金额(元)" data-name="convertSumAmt" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="资管及委外资产未投资头寸(元)" data-name="nonInvestedAmt" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="底层资产行内资产/负债编码" data-name="underAssetCode" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="底层资产持仓数量" data-name="underAssetSum" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="底层资产折算人民币市值(元)" data-name="underConvertSumAmt" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="持仓日期" data-name="reportDate"  data-width="150"></k-grid-column>
       <k-grid-column data-align="left" data-header="新增日期" data-name="createDate" data-type="date"  data-export="false" data-width="150"></k-grid-column>

      </k-grid>
    </div>

	<!--    添加底层资产持仓管理弹出框   -->
	<k-popup ref="addUnderAssetRegistInfoPopup" data-title="新增">
    	<k-form ref="addUnderAssetRegistInfoForm" :data-col="2" isFormBodyScreen>
			<k-form-item label="发行机构代码">
	        	<k-field-text v-model="formData.bankCode"  :data-allowblank="false" :data-max-length="6"/>
	     	</k-form-item>
			<k-form-item label="对应资管及委外资产行内资产/负债编码">
	        	<k-field-text v-model="formData.assetManagerCode" :data-allowblank="false" :data-max-length="40"/>
	     	</k-form-item>
			<k-form-item label="资管及委外资产当前总折算人民币金额(元)">
	        	<k-field-text v-model="formData.convertSumAmt" :data-allowblank="false" data-integer-length="13" data-digits="2" data-validate-type="money" data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
	     	</k-form-item>
			<k-form-item label="资管及委外资产当前总数量">
	        	<k-field-text v-model="formData.assetSumNumber" :data-allowblank="false" data-integer-length="13" data-digits="5" data-validate-type="money" data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
	     	</k-form-item>
			<k-form-item label="资管及委外资产未投资头寸(元)">
	        	<k-field-text v-model="formData.nonInvestedAmt" :data-allowblank="false" data-integer-length="13" data-digits="2" data-validate-type="money" data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
	     	</k-form-item>
			<k-form-item label="底层资产行内资产/负债编码">
	        	<k-field-text v-model="formData.underAssetCode" :data-allowblank="false" :data-max-length="40"/>
	     	</k-form-item>
			<k-form-item label="底层资产持仓数量">
	        	<k-field-text v-model="formData.underAssetSum" :data-allowblank="false" data-integer-length="13" data-digits="5" data-validate-type="number" data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
	     	</k-form-item>
			<k-form-item label="底层资产折算人民币市值(元)">
	        	<k-field-text v-model="formData.underConvertSumAmt" :data-allowblank="false" data-integer-length="13" data-digits="2" data-validate-type="money" />
	     	</k-form-item>
			<k-form-item label="持仓日期">
	        	<k-field-date v-model="formData.reportDate" :data-allowblank="false"  data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd" />
	     	</k-form-item>
	      	<k-form-footer slot="footer" data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="UnderAssetRegistInfo.addUnderAssetRegistInfo" data-from="addUnderAssetRegistInfoForm"
		               :data-model="formData" data-target="underAssetRegistInfoGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改底层资产持仓管理弹出框   -->
	<k-popup ref="editUnderAssetRegistInfoPopup" data-title="修改">
	  <k-form ref="editUnderAssetRegistInfoForm" :data-col="2" isFormBodyScreen>
      <k-form-item label="发行机构代码" :class="[handleItemDiff('bankCode')]">
      	        	<k-field-text v-model="formData.bankCode"  :data-allowblank="false" :data-max-length="6"/>
      	     	</k-form-item>
      			<k-form-item label="对应资管及委外资产行内资产/负债编码" :class="[handleItemDiff('assetManagerCode')]">
      	        	<k-field-text v-model="formData.assetManagerCode" :data-allowblank="false" :data-max-length="40" :data-disabled="true"/>
      	     	</k-form-item>
      			<k-form-item label="资管及委外资产当前总折算人民币金额(元)" :class="[handleItemDiff('convertSumAmt')]">
      	        	<k-field-text v-model="formData.convertSumAmt" :data-allowblank="false" data-integer-length="13" data-digits="2" data-validate-type="money" data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
      	     	</k-form-item>
      			<k-form-item label="资管及委外资产当前总数量" :class="[handleItemDiff('assetSumNumber')]">
      	        	<k-field-text v-model="formData.assetSumNumber" :data-allowblank="false" data-integer-length="13" data-digits="5" data-validate-type="money" data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
      	     	</k-form-item>
      			<k-form-item label="资管及委外资产未投资头寸(元)" :class="[handleItemDiff('nonInvestedAmt')]">
      	        	<k-field-text v-model="formData.nonInvestedAmt" :data-allowblank="false" data-integer-length="13" data-digits="2" data-validate-type="money" data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
      	     	</k-form-item>
      			<k-form-item label="底层资产行内资产/负债编码" :class="[handleItemDiff('underAssetCode')]">
      	        	<k-field-text v-model="formData.underAssetCode" :data-allowblank="false" :data-max-length="40"/>
      	     	</k-form-item>
      			<k-form-item label="底层资产持仓数量" :class="[handleItemDiff('underAssetSum')]">
      	        	<k-field-text v-model="formData.underAssetSum" :data-allowblank="false" data-integer-length="13" data-digits="5" data-validate-type="number" data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
      	     	</k-form-item>
      			<k-form-item label="底层资产折算人民币市值(元)" :class="[handleItemDiff('underConvertSumAmt')]">
      	        	<k-field-text v-model="formData.underConvertSumAmt" :data-allowblank="false" data-integer-length="13" data-digits="2" data-validate-type="money" />
      	     	</k-form-item>
      			<k-form-item label="持仓日期" :class="[handleItemDiff('reportDate')]">
      	        	<k-field-date v-model="formData.reportDate" :data-allowblank="false"  data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd" />
      	     	</k-form-item>
	    <k-form-footer slot="footer" data-align="center">
	      <k-btn class="btn-custom-primary"  ref="sumbitedit" :data-handler="sumbit_edit"   data-from="editUnderAssetRegistInfoForm"
	        :data-model="formData" data-target="underAssetRegistInfoGrid" :handle-before="handleBeforeUpdate">
	        <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
	      </k-btn>
	      <k-btn class="btn-custom-plain" data-functype="CLOSE">
	        <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	    </k-form-footer>
	  </k-form>
	</k-popup>

    <k-popup ref="uploadUnderAssetRegistInfoPopup" title="报送数据导入" @data-opened="uploadOpened()">
            <k-form ref="addForm" data-ui="element">
              <k-form-item label="理论报送起始日期">
                <k-field-date v-model="uploadBeginDate" data-type="date"  data-date-format="yyyy-MM-dd"  data-value-format="yyyyMMdd" :data-allowblank="false"/>
              </k-form-item>
              <!--<k-form-item label="截止日期">
                <k-field-date v-model="uploadQueryDate" data-type="date"  data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd" :data-allowblank="false"/>
              </k-form-item>-->
              <k-form-item label="报送数据导入" data-ui="element" data-input-width="500px">
                <k-field-excel-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit='1'
                    data-accept=".xlsx,.xls"
                    :data-error="onSubmitError" :data-success="onSubmitSuccess"
                    :data-auto-upload="false"
                    data-upload-url="upload/server/RptApp/reportManage/underAssetRegistImport.json">
                </k-field-excel-upload>
              </k-form-item>
              <k-form-footer data-align="center">
                <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="underAssetRegistInfoGrid" ref="submitBtn"
                      :data-auto-upload="false" data-from="addForm" :data-handler="submitUploadParam">确定
                </k-btn>
                <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
              </k-form-footer>
            </k-form>
            </k-popup>
    <k-popup ref="auditInfoPopup">
      <k-form ref="updateAuditStatusForm" :data-col="2" isFormBodyScreen>
         <k-form-item label="报表类型">
           <k-field-text v-model="infoPop.tableName" :data-allowblank="false" data-disabled="true"/>
         </k-form-item>
         <k-form-item label="数据日期" data-label-width="100px">
           <k-field-date v-model="queryParamDateRange" data-type="daterange" data-date-format="yyyy-MM-dd"
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
export default {
  name: "UnderAssetRegistInfo",
  data() {
    return {
      formData: {},
      formDataCopy: {},
      selectRowData: {},
      searchParam: {},
      queryParamDateRange:[],
      uploadBeginDate: '',
      uploadQueryDate: '',

      infoPop: {},
      auditInfoPopupData: {
        tableId: 'app_under_asset_regist_info',
        tableName: '底层资产持仓管理'
      }
    };
  },
  methods: {
    handleBeforeUpdate() {
      if (this.formNoChangeCb()) {
        this.$refs.editUnderAssetRegistInfoPopup.close();
        return false
      }
      return true
    },
    sumbit_edit(){
        this.$refs.sumbitedit.setIconStyle(0,[]);
         if(this.$refs.editUnderAssetRegistInfoForm.validate()){
               this.httpUtil.query({
                       url: 'server/json/RptApp/audit/checkunderAssetRegistInfo.json',
                       params:  this.formData
                                }).then(res => {
                                  if(res.success) {
                                   this.httpUtil.comnUpdate({
                                            action: 'UnderAssetRegistInfo.updateUnderAssetRegistInfo',
                                            params:  this.formData
                                             }).then(res => {
                                              if(res.success) {
                                              this.$refs.editUnderAssetRegistInfoPopup.close();
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
               this.$confirm("日期区间存在未校验或校验未通过的数据,确认复核吗？", "操作提示", {
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
                     this.$refs.underAssetRegistInfoGrid.load(this.searchParam);
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
                 this.$refs.underAssetRegistInfoGrid.load(this.searchParam);
                 this.$refs.auditInfoPopup.close();
                }
              })
           }
         }
       })
    },
    submitUploadParam() {
      //文件上传校验
      let validate = this.$refs.addForm.validate();
      if (validate) {
        let formData = {beginDate: this.uploadBeginDate, queryDate: this.uploadBeginDate};
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
      this.$refs.uploadUnderAssetRegistInfoPopup.close();
      this.$refs.underAssetRegistInfoGrid.load(this.searchParam);
    },
    onSubmitError() {
      this.$refs.uploadRef.doReset();
      this.$refs.submitBtn.setIconStyle(1, [])
    },
    popupEdit(row) {
      let pathUrl = '/main/zz/errorInfo/UnderAssetRgInfoErr';
      this.$router.push({
        path: pathUrl,
        query: {
          registerSerno: row.registerSerno,
        },
      });
    },
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
      this.formDataCopy = Object.assign({}, row)
    },
    uploadOpened() {
      this.uploadBeginDate = ''
      this.uploadQueryDate = ''
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
  }
};
</script>
