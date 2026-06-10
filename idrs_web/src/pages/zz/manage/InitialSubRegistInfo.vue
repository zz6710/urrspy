<template>
  <div class="py-page">
    <div>
      <k-form-search-customize ref="searchFormRef" data-model-name="InitialSubRegistInfo" data-target="tableGrid" v-model="queryParam">
        <k-form-item label="产品起始日期">
          <k-field-date v-model="queryParamDateRange"  data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="产品登记编码">
          <k-field-text v-model="searchParam.prodCode"/>
        </k-form-item>
        <!-- <k-form-item label="业务登记日期">
          <k-field-date v-model="searchParam.registerDate" data-type="daterange" data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd" />
        </k-form-item> -->
        <k-form-item label="报送状态">
          <k-field-select v-model="searchParam.registerStatus" data-dict="report_status"/>
        </k-form-item>
        <k-form-item label="报送日期">
          <k-field-date v-model="queryParamReportDateRange" :data-allowblank="false" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="新增日期">
          <k-field-date v-model="queryParamCreateDateRange"  data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn slot="button"  data-functype="POPUP" class="btn-custom-plain" data-target="uploadInitialSubRegistInfoPopup">
            <md-icon>cloud_upload</md-icon>导入
          </k-btn>
          <k-btn slot="button" ref="exportRef" class="btn-custom-plain" data-functype="EXPORT" data-target="tableGrid" data-export-dict="true"
                :data-export-name="'募集总量登记管理'" :handleBefore="handleBefore" @downSuccess="downSuccess" :data-handler="dataHandler">
            <md-icon>cloud_download</md-icon>
            导出
          </k-btn>
          <k-btn slot="button" class="btn-custom-plain" :handleBefore="handleBefore" :data-handler="handleConfirmExport">
            <md-icon>cloud_download</md-icon>
            确认并导出
          </k-btn>
          <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="auditPopup">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>复核
          </k-btn>
        </div>
      </div>
      <k-grid ref="tableGrid" @data-row-select="selectRow"  data-fixed="right" data-operate-width="150px" :data-autoload="false" data-action="InitialSubRegistInfo.findInitialSubRegistInfos" >
        <k-grid-column data-align="left" data-header="报送日期" data-name="reportDate" data-export="false" data-width="80" ></k-grid-column>
        <k-grid-column data-align="left" data-header="版本号" data-name="sysDataVersion" data-export="false" data-width="60" ></k-grid-column>
        <k-grid-column data-align="left" data-header="产品起始日期" data-name="foundDt" data-export="false" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="*产品登记编码" data-name="prodCode" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="*发行机构代码" data-name="bankCode" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="*资金托管账号" data-name="fndTrstActNbr" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="*资金托管账户" data-name="fndTrstAct" data-width="260"></k-grid-column>
        <k-grid-column data-align="right" data-header="*个人投资者总数" data-name="numberIndivInvest" data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="*法人投资者总数" data-name="numberCorporInvest" data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="*非法人投资者总数" data-name="numberUcorInvest" data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="*实际募集金额(元)" data-name="actualSubscribedAmt" data-width="130"></k-grid-column>
        <k-grid-column data-align="right" data-header="*募集总份额" data-name="subscribedVol" data-width="130"></k-grid-column>
        <k-grid-column data-align="left" data-header="*是否有其他机构代销" data-name="otherDistributAgents" data-dict="subm_isTrue" data-width="100"></k-grid-column>
        <k-grid-column data-align="right" data-header="代销总金额" data-name="amtOtherDbAgents" data-width="100"></k-grid-column>
        <k-grid-column data-align="right" data-header="产品销售区域及募集金额" data-name="zonClcAmt" data-width="300"></k-grid-column>
        <k-grid-column data-align="right" data-header="认购币种" data-name="prodCcy" data-width="80"></k-grid-column>
        <k-grid-column data-align="left" data-header="备注" data-name="details" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="登记流水号" data-name="registerSerno" data-width="270" data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="登记日期" data-name="registerDate" data-width="100" data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="新增日期" data-name="createDate" data-export="false" data-width="80" ></k-grid-column>
        <k-grid-column data-align="left" data-header="报送状态" data-name="registerStatus" data-dict="report_status" data-width="100" data-export="false"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改募集总量登记管理" data-functype="POPUP" data-size="mini"
             v-show="scope.row.row.registerStatus != '5'"     data-target="editInitialSubRegistInfoPopup">
            修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="InitialSubRegistInfo.deleteInitialSubRegistInfo" data-size="mini"
             v-show="scope.row.row.registerStatus != '5'"     data-type="danger" data-target="tableGrid" :data-confirm="true" data-descript="删除募集总量登记管理">
            删除
          </k-btn>
          <!--<k-btn data-functype="PAGE" data-size="mini" class="btn-custom-plain" :data-model="scope.row.row.id"
                 @click="popupEdit(scope.row.row)"  data-descript="募集总量登记管理错误详情">
            错误详情
          </k-btn>-->
        </template>
      </k-grid>
    </div>
    <!--    添加募集总量登记管理弹出框   -->
    <k-popup ref="addInitialSubRegistInfoPopup" data-title="新增">
      <k-form ref="addInitialSubRegistInfoForm" :data-col="2" isFormBodyScreen data-label-width="170px">
        <k-form-item label="产品登记编码">
          <k-field-text v-model="formData.prodCode" :data-allowblank="false" :data-max-length="15"/>
        </k-form-item>
        <k-form-item label="发行机构代码">
          <k-field-text v-model="formData.bankCode" :data-allowblank="false" :data-max-length="6"/>
        </k-form-item>
        <k-form-item label="资金托管账号">
          <k-field-text v-model="formData.fndTrstActNbr" :data-allowblank="false" :data-max-length="60"/>
        </k-form-item>
        <k-form-item label="资金托管账户">
          <k-field-text v-model="formData.fndTrstAct" :data-allowblank="false" :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="个人投资者总数">
          <k-field-text v-model="formData.numberIndivInvest" :data-allowblank="false"  data-type="number" data-validate-type="number"   data-min-value="0" :data-max-length="9"/>
        </k-form-item>
        <k-form-item label="法人投资者总数">
          <k-field-text v-model="formData.numberCorporInvest" :data-allowblank="false"  data-type="number" data-validate-type="number"  data-min-value="0" :data-max-length="9"/>
        </k-form-item>
        <k-form-item label="非法人投资者总数">
          <k-field-text v-model="formData.numberUcorInvest" :data-allowblank="false" data-type="number" data-validate-type="number"  data-min-value="0" :data-max-length="9"/>
        </k-form-item>
        <k-form-item label="实际募集金额(元)">
          <k-field-text v-model="formData.actualSubscribedAmt" :data-allowblank="false" data-type="money" data-validate-type="money" data-min-value="0"  data-digits="2" data-integer-length="13"/>
        </k-form-item>
        <k-form-item label="募集总份额">
          <k-field-text v-model="formData.subscribedVol" :data-allowblank="false" data-validate-type="money" data-digits="5" data-min-value="0"   data-integer-length="13"/>
        </k-form-item>
        <k-form-item label="是否有其他机构代销">
          <k-field-select v-model="formData.otherDistributAgents" data-dict="subm_isTrue" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="产品销售区域及募集金额">
          <k-field-text v-model="formData.zonClcAmt" :data-max-length="1000"/>
        </k-form-item>
        <k-form-item label="认购币种">
          <k-field-select v-model="formData.prodCcy" data-dict="tr_cur" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="代销总金额">
          <k-field-text v-model="formData.amtOtherDbAgents" data-validate-type="money" data-digits="2"  data-min-value="0" data-integer-length="13"/>
        </k-form-item>
        <k-form-item label="备注">
          <k-field-text v-model="formData.details" :data-max-length="256"/>
        </k-form-item>

        <k-form-footer slot="footer" data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="InitialSubRegistInfo.addInitialSubRegistInfo" data-from="addInitialSubRegistInfoForm"
                 :data-model="formData" data-target="tableGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改募集总量登记管理弹出框   -->
    <k-popup ref="editInitialSubRegistInfoPopup" data-title="修改" @data-opened="editOpened()">
      <k-form ref="editInitialSubRegistInfoForm" :data-col="2" isFormBodyScreen data-label-width="170px">
        <k-form-item label="产品登记编码" :class="[handleItemDiff('prodCode')]">
          <k-field-text v-model="formData.prodCode" :data-disabled="true" :data-allowblank="false" :data-max-length="15"/>
        </k-form-item>
         <k-form-item label="发行机构代码" :class="[handleItemDiff('bankCode')]">
          <k-field-text v-model="formData.bankCode" :data-disabled="true" :data-allowblank="false" :data-max-length="6"/>
        </k-form-item>
        <k-form-item label="资金托管账号" :class="[handleItemDiff('fndTrstActNbr')]">
          <k-field-text v-model="formData.fndTrstActNbr" :data-allowblank="false" :data-max-length="60"/>
        </k-form-item>
        <k-form-item label="资金托管账户" :class="[handleItemDiff('fndTrstAct')]">
          <k-field-text v-model="formData.fndTrstAct" :data-allowblank="false" :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="个人投资者总数" :class="[handleItemDiff('numberIndivInvest')]">
          <k-field-text v-model="formData.numberIndivInvest" :data-allowblank="false" data-validate-type="number"   data-type="number"   data-min-value="0" :data-max-length="9"/>
        </k-form-item>
        <k-form-item label="法人投资者总数" :class="[handleItemDiff('numberCorporInvest')]">
          <k-field-text v-model="formData.numberCorporInvest" :data-allowblank="false" data-validate-type="number"  data-type="number"   data-min-value="0" :data-max-length="9"/>
        </k-form-item>
        <k-form-item label="非法人投资者总数" :class="[handleItemDiff('numberUcorInvest')]">
          <k-field-text v-model="formData.numberUcorInvest" :data-allowblank="false" data-validate-type="number"  data-type="number"  data-min-value="0" :data-max-length="9"/>
        </k-form-item>
        <k-form-item label="实际募集金额(元)" :class="[handleItemDiff('actualSubscribedAmt')]">
          <k-field-text v-model="formData.actualSubscribedAmt" :data-allowblank="false" data-validate-type="money" data-type="money"  data-min-value="0" data-digits="2"  data-integer-length="13"/>
        </k-form-item>
        <k-form-item label="募集总份额" :class="[handleItemDiff('subscribedVol')]">
          <k-field-text v-model="formData.subscribedVol" :data-allowblank="false" data-validate-type="money" data-type="money" data-min-value="0"  data-digits="5"   data-integer-length="13"/>
        </k-form-item>
        <k-form-item label="是否有其他机构代销" :class="[handleItemDiff('otherDistributAgents')]">
          <k-field-select v-model="formData.otherDistributAgents" data-dict="subm_isTrue" :data-allowblank="false"/>
        </k-form-item>
         <k-form-item label="产品销售区域及募集金额" :class="[handleItemDiff('zonClcAmt')]">
          <k-field-text v-model="formData.zonClcAmt" :data-max-length="1000"/>
        </k-form-item>
        <k-form-item label="认购币种" :class="[handleItemDiff('prodCcy')]">
          <k-field-text v-model="formData.prodCcy" />
        </k-form-item>
        <k-form-item label="代销总金额" :class="[handleItemDiff('amtOtherDbAgents')]">
          <k-field-text v-model="formData.amtOtherDbAgents" data-validate-type="money" data-type="money"  data-min-value="0" data-digits="2" data-integer-length="13"/>
        </k-form-item>
        <k-form-item label="备注" :class="[handleItemDiff('details')]">
          <k-field-text v-model="formData.details" :data-max-length="256"/>
        </k-form-item>
        <k-form-footer slot="footer" data-align="center">
          <k-btn class="btn-custom-primary"   ref="sumbitedit" :data-handler="sumbit_edit"  data-from="editInitialSubRegistInfoForm"
                 :data-model="formData" data-target="tableGrid" :handle-before="handleBeforeUpdate">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="uploadInitialSubRegistInfoPopup" title="报送数据导入" @data-opened="uploadOpened()">
        <k-form ref="addForm" data-ui="element">
          <k-form-item label="报送日期">
            <k-field-date v-model="formData.reportDate" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd" :data-allowblank="false"/>
          </k-form-item>
          <k-form-item label="报送数据导入" data-ui="element" data-input-width="500px">
            <k-field-excel-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit='1'
                data-accept=".xlsx,.xls"
                :data-error="onSubmitError" :data-success="onSubmitSuccess"
                :data-auto-upload="false"
                data-upload-url="upload/server/RptApp/reportManage/initialSubRegistImport.json">
            </k-field-excel-upload>
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="tableGrid" ref="submitBtn"
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
         <k-form-item label="报送日期" data-label-width="100px">
           <k-field-date v-model="queryParamReportDateRange" data-type="daterange" data-date-format="yyyy-MM-dd"
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
import ProdMixin from "@/pages/zz/manage/mixins/prodMixin.js";

export default {
  name: "InitialSubRegistInfo",
  mixins: [ProdMixin],
  data() {
    return {
      formData: {},
      formDataCopy: {},
      selectRowData: {},
      searchParam: {},
      registerDate: [],
      infoPop: {},
      auditInfoPopupData: {
        tableId: 'app_initial_sub_regist_info',
        tableName: '募集总量登记管理'
      },
      queryParamDateRange: [],
      queryParamCreateDateRange:[],
      queryParamReportDateRange:[],
      abnormalAction: "InitialSubRegistInfo.getAbnormalData",
      updateStatusAction: "InitialSubRegistInfo.updateInitialSubRegistInfoStatus",
      comfirmExportParam: {}
    };
  },
  computed: {
    queryParam() {
      return {
        'beginDate': this.queryParamDateRange ? this.queryParamDateRange[0] : null,
        'queryDate': this.queryParamDateRange ? this.queryParamDateRange[1] : null,
        'beginCrtDate': this.queryParamCreateDateRange ? this.queryParamCreateDateRange[0] : null,
        'endCrtDate': this.queryParamCreateDateRange ? this.queryParamCreateDateRange[1] : null,
        'reportBeginDate': this.queryParamReportDateRange ? this.queryParamReportDateRange[0] : null,
        'reportEndDate': this.queryParamReportDateRange ? this.queryParamReportDateRange[1] : null,
        'prodCode': this.searchParam.prodCode,
        'registerDate': this.searchParam.registerDate,
        'registerStatus': this.searchParam.registerStatus,
      }
    }
  },
  methods: {
    editOpened(){
        this.formData.oldData=Tools.json2str(this.formData);
      },
    handleBeforeUpdate() {
      if (this.formNoChangeCb()) {
        this.$refs.editInitialSubRegistInfoPopup.close();
        return false
      }
      return true
    },
		dataHandler() {
			if (this.queryParamReportDateRange == null) {
				this.$message.error("报送日期不能为空!");
				return false;
			}
			setTimeout(()=>{
				this.$refs.exportRef.handleExport(this.queryParam);
			}, 500)
			return false
		},
    sumbit_edit(){
              this.$refs.sumbitedit.setIconStyle(0,[]);
              if(this.$refs.editInitialSubRegistInfoForm.validate()){
                   this.httpUtil.query({
                           url: 'server/json/RptApp/audit/checkInitialSubRegistInfo.json',
                           params:  this.formData
                                    }).then(res => {
                                      if(res.success) {
                                       this.httpUtil.comnUpdate({
                                                action: 'InitialSubRegistInfo.updateInitialSubRegistInfo',
                                                params:  this.formData
                                                 }).then(res => {
                                                  if(res.success) {
                                                  this.$refs.editInitialSubRegistInfoPopup.close();
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
      let startDate = this.queryParamReportDateRange ? this.queryParamReportDateRange[0] : null;
      let endDate = this.queryParamReportDateRange ? this.queryParamReportDateRange[1] : null;
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
                     this.$refs.tableGrid.load(this.queryParam);
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
                 this.$refs.tableGrid.load(this.queryParam);
                 this.$refs.auditInfoPopup.close();
                }
              })
           }
         }
       })
    },
    setConfirmExportParam() {
      this.comfirmExportParam = {
        beginDate: this.queryParamDateRange ? this.queryParamDateRange[0] : null,
        queryDate: this.queryParamDateRange ? this.queryParamDateRange[1] : null,
        beginCrtDate: this.queryParamCreateDateRange ? this.queryParamCreateDateRange[0] : null,
        endCrtDate: this.queryParamCreateDateRange ? this.queryParamCreateDateRange[1] : null,
        reportBeginDate: this.queryParamReportDateRange ? this.queryParamReportDateRange[0] : null,
        reportEndDate: this.queryParamReportDateRange ? this.queryParamReportDateRange[1] : null,
        prodCode: this.searchParam.prodCode,
        registerDate: this.searchParam.registerDate,
        registerStatus: this.searchParam.registerStatus,
      };
    },
    submitUploadParam() {
      //文件上传校验
      let validate = this.$refs.addForm.validate();
      this.$refs.submitBtn.setIconStyle(0, []);
      if (validate) {
        let formData = {reportDate: this.formData.reportDate};
        let temp = document.getElementsByClassName('upload-demo');
        let lis = temp[0].childNodes[1].childNodes.length;
        if (lis > 0) {
          this.$refs.uploadRef.upload(formData);
        } else {
          this.$message.error("上传文件不能为空!");
          return false;
        }
       this.$refs.submitBtn.setIconStyle(1, []);
      }
    },
    onSubmitSuccess() {
      this.$refs.uploadRef.doReset();
      this.$refs.addForm.reset();
      this.$refs.uploadInitialSubRegistInfoPopup.close();
      this.$refs.tableGrid.load(this.queryParam);
      this.$refs.zonClcInfoGrid.load(this.queryParam);
      this.formData.reportDate = ''
    },
    onSubmitError() {
      this.$refs.uploadRef.doReset();
      this.$refs.submitBtn.setIconStyle(1, []);
    },
    popupEdit(row) {
      let pathUrl = '/main/zz/errorInfo/InitialSubRgInfoErr';
      this.$router.push({
        path: pathUrl,
        query: {
          registerSerno: row.registerSerno,
        },
      });
    },
    setExeidBool(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
    },
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
      this.formDataCopy = Object.assign({}, row)
    },
    uploadOpened() {
     // this.formData.reportDate = ''
    },
    // 复核弹窗
    auditPopup() {
      this.infoPop = this.auditInfoPopupData;
      this.$refs.auditInfoPopup.popup();
    },
  },
  created() {
    let now = new Date();
    let year = now.getFullYear(); //获取年
    let month = now.getMonth(); //获取月
    let date = now.getDate(); //得到日期
    month = month + 1;
    month = month.toString().padStart(2, "0");
    date = date.toString().padStart(2, "0");
    let  defaultDate = `${year}${month}${date}`;
    this.queryParamReportDateRange[0] = defaultDate;
    this.queryParamReportDateRange[1] = defaultDate;
    this.$set(this.queryParam, "queryParamReportDateRange", defaultDate);
  },
  watch: {
    registerDate() {
      this.$set(this.searchParam, 'startDate', this.registerDate == null ? '' : this.registerDate[0]);
      this.$set(this.searchParam, 'endDate', this.registerDate == null ? '' : this.registerDate[1]);
    },
  }
};
</script>
