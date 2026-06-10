<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="AssetRegistInfo"  data-target="AssetRegistInfoGrid" data-label-width="130px" v-model="queryParam" ref="searchFormRef">

        <k-form-item label="持仓日期(数据日期)" data-label-width="150px">
          <k-field-date v-model="searchParam.startDate" data-type="date" data-date-format="yyyy-MM-dd"  data-value-format="yyyyMMdd" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="产品登记编码">
          <k-field-text v-model="searchParam.prodRegEnc"/>
        </k-form-item>
        <k-form-item label="持仓类别" data-label-width="80px">
          <k-field-select v-model="searchParam.holdingType" data-dict="subm_holding_type"/>
        </k-form-item>
        <k-form-item label="行内资产/负债编码">
          <k-field-text v-model="searchParam.assetCode"/>
        </k-form-item>
        <k-form-item label="中间层行内资产/负债编码">
          <k-field-text v-model="searchParam.mezzanineAssetCode"/>
        </k-form-item>
        <k-form-item label="报送状态" data-label-width="80px">
          <k-field-select v-model="searchParam.registerStatus" data-dict="report_status"/>
        </k-form-item>
        <!-- <k-form-item label="理论报送起始日期">
          <k-field-date v-model="searchParam.theoryReportStartDate"   data-date-format="yyyy-MM-dd"  data-value-format="yyyyMMdd"/>
        </k-form-item> -->

      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
            <!-- <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addAssetRegistInfoPopup" slot="button">
            <md-icon md-src="/static/svg/add.svg" />新增</k-btn>-->
          <k-btn slot="button"  data-functype="POPUP" class="btn-custom-plain" data-target="uploadAssetRegistInfoPopup">
            <md-icon>cloud_upload</md-icon>导入
          </k-btn>
          <k-btn slot="button" ref="exportRef"  class="btn-custom-plain"  :handleBefore="handleBefore" data-functype="EXPORT" data-target="AssetRegistInfoGrid"  data-excel-template ="资产持仓登记管理.xlsx"  data-template-name="资产持仓登记"  data-excel-start-line ="2" data-export-dict="true" @downSuccess="downSuccess" :data-handler="handleExport"
             data-export-name="资产持仓登记">
            <md-icon>cloud_download</md-icon>
            导出
          </k-btn>
          <k-btn slot="button" class="btn-custom-plain" :handleBefore="handleBefore" data-functype="SUBMIT" :data-model="queryParam" data-action="AssetRegistInfo.updateAssetRegistInfoStatus">
            <!-- <md-icon>cloud_download</md-icon> -->
            确认报送状态
          </k-btn>
          <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="auditPopup">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>复核
          </k-btn>
        </div>
      </div>
      <k-grid ref="AssetRegistInfoGrid" @data-row-select="selectRow" data-fixed="right" data-operate-width="250px" data-action="AssetRegistInfo.findAssetRegistInfos" :data-autoload="false" >
        <k-grid-column  data-align="left" data-header="报送状态" data-name="registerStatus" data-dict="report_status" data-export="false"  data-width="100"></k-grid-column>
        <k-grid-column  data-align="left" data-header="发行机构代码" data-name="bankCode" data-width="120"></k-grid-column>
        <k-grid-column  data-align="left" data-header="产品登记编码" data-name="prodRegEnc" data-width="120"></k-grid-column>
        <k-grid-column  data-align="left" data-header="持仓类别" data-name="holdingType" data-dict ="subm_holding_type" data-width="150"></k-grid-column>
        <k-grid-column  data-align="left" data-header="行内资产/负债编码" data-name="assetCode" data-width="120"></k-grid-column>
        <k-grid-column  data-align="left" data-header="资产穿透情况" data-name="investedAsset" data-dict ="subm_invested_asset_type" data-width="150"></k-grid-column>
        <k-grid-column  data-align="left" data-header="中间层数" data-name="mezzanineNumber" data-width="120"></k-grid-column>
        <k-grid-column  data-align="left"  data-header="中间层行内资产/负债编码" data-name="mezzanineAssetCode" data-width="120"></k-grid-column>
        <k-grid-column  data-align="left"  data-header="会计科目名称" data-name="accountCode" data-width="120"></k-grid-column>
        <k-grid-column  data-align="right"  data-header="金额" data-name="investedAmount" data-width="120"></k-grid-column>
        <k-grid-column  data-align="right"  data-header="折算人民币金额" data-name="investedAmountCny"  data-width="120"></k-grid-column>
        <k-grid-column  data-align="right"  data-header="公允价值" data-name="fairValue"  data-width="120"></k-grid-column>
        <k-grid-column  data-align="right"  data-header="折算人民币公允价值" data-name="fairValueCny"  data-width="120"></k-grid-column>
        <k-grid-column  data-align="right"  data-header="单位估值(净价)" data-name="netValuation"  data-width="120"></k-grid-column>
        <k-grid-column  data-align="right"  data-header="单位估值(全价)" data-name="flValuation"  data-width="120"></k-grid-column>
        <k-grid-column  data-align="right"  data-header="数量" data-name="quantity"  data-width="120"></k-grid-column>
        <k-grid-column  data-align="left"   data-header="币种" data-name="cny"  data-width="100"></k-grid-column>
        <k-grid-column  data-align="left"   data-header="持仓日期" data-name="holdingDate"   data-width="100"></k-grid-column>
        <k-grid-column  data-align="left"   data-header="备注" data-name="details" data-width="150"></k-grid-column>
        <k-grid-column  data-align="left"   data-header="登记流水号" data-name="registerSerno" data-export="false" data-width="250"></k-grid-column>
        <k-grid-column  data-align="left"   data-header="登记日期" data-name="registerDate"  data-export="false" data-width="100"></k-grid-column>
        <!-- <k-grid-column  data-align="left"   data-header="理论报送起始日期" data-name="theoryReportStartDate"  data-export="false" data-width="100"></k-grid-column>
        <k-grid-column  data-align="left"   data-header="理论报送截止日期" data-name="theoryReportEndDate"  data-export="false" data-width="100"></k-grid-column> -->
        <k-grid-column  data-align="left"   data-header="新增日期" data-name="createDate"  data-export="false" data-width="100"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改资产持仓管理" data-functype="POPUP" data-size="mini"
              v-show="scope.row.row.registerStatus != '5'"    data-target="editAssetRegistInfoPopup">
            修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="AssetRegistInfo.deleteAssetRegistInfo" data-size="mini"
             v-show="scope.row.row.registerStatus != '5'"     data-type="danger" data-target="AssetRegistInfoGrid" :data-confirm="true" data-descript="删除资产持仓管理">
            删除
          </k-btn>
          <!--<k-btn data-functype="PAGE" data-size="mini" class="btn-custom-plain" :data-model="scope.row.row.id"
                 @click="popupEdit(scope.row.row)"  data-descript="资产持仓管理错误详情">
            错误详情
          </k-btn>-->
        </template>
      </k-grid>
    </div>

    <!--    添加资产持仓管理弹出框   -->
    <k-popup ref="addAssetRegistInfoPopup" data-title="新增">
      <k-form ref="addAssetRegistInfoForm" :data-col="2" isFormBodyScreen>
        <k-form-item label="发行机构代码">
          <k-field-text v-model="formData.bankCode" :data-allowblank="false" :data-max-length="6"/>
        </k-form-item>
        <k-form-item label="产品登记编码">
          <k-field-text v-model="formData.prodRegEnc" :data-allowblank="false" :data-max-length="40"/>
        </k-form-item>
        <k-form-item label="持仓类别">
          <k-field-select v-model="formData.holdingType" :data-allowblank="false" data-dict="subm_holding_type"/>
        </k-form-item>
        <k-form-item label="行内资产/负债编码">
          <k-field-text v-model="formData.assetCode" :data-allowblank="false" :data-max-length="40"/>
        </k-form-item>
        <k-form-item label="资产穿透情况">
          <k-field-select v-model="formData.investedAsset" :data-allowblank="false" data-dict="subm_invested_asset_type"/>
        </k-form-item>
        <k-form-item label="中间层数">
          <k-field-text v-model="formData.mezzanineNumber" :data-allowblank="false" data-integer-length="1" data-validate-type="number" data-type="number"/>
        </k-form-item>
        <k-form-item label="中间层行内资产/负债编码">
          <k-field-text v-model="formData.mezzanineAssetCode"  :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="会计科目名称">
          <k-field-text v-model="formData.accountCode"  :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="金额">
          <k-field-text v-model="formData.investedAmount" :data-allowblank="false" data-integer-length="13" data-digits="2" data-validate-type="money"/>
        </k-form-item>
        <k-form-item label="折算人民币金额">
          <k-field-text v-model="formData.investedAmountCny" :data-allowblank="false" data-integer-length="13" data-digits="2" data-validate-type="money"/>
        </k-form-item>
        <k-form-item label="公允价值">
          <k-field-text v-model="formData.fairValue"  data-integer-length="13" data-digits="2" data-validate-type="money"/>
        </k-form-item>
        <k-form-item label="折算人民币公允价值">
          <k-field-text v-model="formData.fairValueCny"  data-integer-length="13" data-digits="2" data-validate-type="money"/>
        </k-form-item>
        <k-form-item label="单位估值(净价)">
          <k-field-text v-model="formData.netValuation" :data-allowblank="false" data-integer-length="13" data-digits="4" data-validate-type="money"/>
        </k-form-item>
        <k-form-item label="单位估值(全价)">
          <k-field-text v-model="formData.flValuation" :data-allowblank="false" data-integer-length="13" data-digits="4" data-validate-type="money"/>
        </k-form-item>
        <k-form-item label="数量">
          <k-field-text v-model="formData.quantity" :data-allowblank="false" data-integer-length="13" data-digits="5" data-validate-type="money"/>
        </k-form-item>
        <k-form-item label="币种">
          <k-field-select v-model="formData.cny" :data-allowblank="false" data-dict="cur_type"/>
        </k-form-item>
        <k-form-item label="持仓日期">
          <k-field-date v-model="formData.holdingDate" :data-allowblank="false" data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd" />
        </k-form-item>
        <k-form-item label="理论报送起始日期">
          <k-field-date v-model="formData.theoryReportStartDate"   data-date-format="yyyy-MM-dd"  data-value-format="yyyyMMdd" />
        </k-form-item>
        <k-form-item label="备注">
          <k-field-text v-model="formData.details" :data-allowblank="false" :data-max-length="256"/>
        </k-form-item>
        <k-form-footer slot="footer" data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="AssetRegistInfo.addAssetRegistInfo"
                 data-from="addAssetRegistInfoForm"
                 :data-model="formData" data-target="AssetRegistInfoGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改资产持仓管理弹出框   -->
    <k-popup ref="editAssetRegistInfoPopup" data-title="修改" @data-opened="editOpened()">
      <k-form ref="editAssetRegistInfoForm" :data-col="2" isFormBodyScreen>
        <k-form-item label="发行机构代码" :class="[handleItemDiff('bankCode')]">
          <k-field-text v-model="formData.bankCode" :data-disabled="true" :data-allowblank="false" :data-max-length="6"/>
        </k-form-item>
        <k-form-item label="产品登记编码" :class="[handleItemDiff('prodRegEnc')]">
          <k-field-text v-model="formData.prodRegEnc" :data-allowblank="false" :data-disabled="true" :data-max-length="40"/>
        </k-form-item>
        <k-form-item label="持仓类别" :class="[handleItemDiff('holdingType')]">
          <k-field-select v-model="formData.holdingType" :data-allowblank="false" :data-disabled="true" data-dict="subm_holding_type"/>
        </k-form-item>
        <k-form-item label="行内资产/负债编码" :class="[handleItemDiff('assetCode')]">
          <k-field-text v-model="formData.assetCode" :data-allowblank="formDataMust.assetCode" :data-disabled="true" :data-max-length="40"/>
        </k-form-item>
        <k-form-item label="资产穿透情况" :class="[handleItemDiff('investedAsset')]">
          <k-field-select v-model="formData.investedAsset" :data-allowblank="false" :data-disabled="true" data-dict="subm_invested_asset_type"/>
        </k-form-item>
        <k-form-item label="中间层数" :class="[handleItemDiff('mezzanineNumber')]">
          <k-field-text v-model="formData.mezzanineNumber" :data-allowblank="false" :data-disabled="true" data-integer-length="1" data-validate-type="number" data-type="number"/>
        </k-form-item>
        <k-form-item label="中间层行内资产/负债编码" :class="[handleItemDiff('mezzanineAssetCode')]">
          <k-field-text v-model="formData.mezzanineAssetCode" :data-allowblank="formDataMust.mezzanineAssetCode"  :data-disabled="true" :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="会计科目名称" :class="[handleItemDiff('accountCode')]">
          <k-field-text v-model="formData.accountCode" :data-allowblank="formDataMust.accountCode"  :data-disabled="true" :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="金额" :class="[handleItemDiff('investedAmount')]">
          <k-field-text v-model="formData.investedAmount" :data-allowblank="false" data-integer-length="13" data-digits="2" data-validate-type="money"/>
        </k-form-item>
        <k-form-item label="折算人民币金额" :class="[handleItemDiff('investedAmountCny')]">
          <k-field-text v-model="formData.investedAmountCny" :data-allowblank="false" data-integer-length="13" data-digits="2" data-validate-type="money"/>
        </k-form-item>
        <k-form-item label="公允价值" :class="[handleItemDiff('fairValue')]">
          <k-field-text v-model="formData.fairValue" data-integer-length="13" data-digits="2" data-validate-type="money"/>
        </k-form-item>
        <k-form-item label="折算人民币公允价值" :class="[handleItemDiff('fairValueCny')]">
          <k-field-text v-model="formData.fairValueCny" data-integer-length="13" data-digits="2" data-validate-type="money"/>
        </k-form-item>
        <k-form-item label="单位估值(净价)" :class="[handleItemDiff('netValuation')]">
          <k-field-text v-model="formData.netValuation" :data-allowblank="formDataMust.netValuation" data-integer-length="13" data-digits="4" data-validate-type="money"/>
        </k-form-item>
        <k-form-item label="单位估值(全价)" :class="[handleItemDiff('flValuation')]">
          <k-field-text v-model="formData.flValuation" :data-allowblank="formDataMust.flValuation" data-integer-length="13" data-digits="4" data-validate-type="money"/>
        </k-form-item>
        <k-form-item label="数量" :class="[handleItemDiff('quantity')]">
          <k-field-text v-model="formData.quantity" :data-allowblank="formDataMust.quantity" data-integer-length="13" data-digits="5" data-validate-type="money" data-min-value="0"/>
        </k-form-item>
        <k-form-item label="币种" :class="[handleItemDiff('cny')]">
          <k-field-select v-model="formData.cny" :data-allowblank="false" data-dict="cur_type"/>
        </k-form-item>
        <k-form-item label="持仓日期" :class="[handleItemDiff('holdingDate')]">
          <k-field-date v-model="formData.holdingDate" :data-allowblank="false" data-date-format="yyyyMMdd" :data-disabled="true"  data-value-format="yyyyMMdd" />
        </k-form-item>
        <k-form-item label="备注" :class="[handleItemDiff('details')]">
          <k-field-text v-model="formData.details" :data-max-length="256"/>
        </k-form-item>
        <k-form-footer slot="footer" data-align="center">
          <k-btn class="btn-custom-primary"
                 data-from="editAssetRegistInfoForm"
                 :data-model="formData" data-target="AssetRegistInfoGrid" ref="sumbitedit" :data-handler="sumbit_edit" :handle-before="handleBeforeUpdate">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="uploadAssetRegistInfoPopup" title="数据导入">
      <k-form ref="addForm" data-ui="element">
        <k-form-item label="报送数据导入" data-ui="element" data-input-width="500px">
          <k-field-excel-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit='1'
                                data-accept=".xlsx,.xls"
                                :data-error="onSubmitError" :data-success="onSubmitSuccess"
                                :data-auto-upload="false"
                                data-upload-url="upload/server/RptApp/reportManage/assetRegistImport.json">
          </k-field-excel-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="AssetRegistInfoGrid" ref="submitBtn"
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
           <k-field-date v-model="infoPop.auditDate" data-type="date" data-date-format="yyyy-MM-dd"
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
import ProdMixin from "@/pages/zz/manage/mixins/prodMixin.js";
import Tools from "@/utils/tools";
export default {
  name: "AssetRegistInfo",
  mixins: [ProdMixin],
  data() {
    return {
      formData: {},
      formDataCopy: {},
      selectRowData: {},
      searchParam:{},
      uploadBeginDate: '',
      uploadQueryDate: '',
      formDataMust: {
        assetCode: false,
        mezzanineAssetCode: false,
        accountCode: false,
        netValuation: false,
        flValuation: false,
        quantity: false,
      },
      infoPop: {},
      auditInfoPopupData: {
        tableId: 'app_asset_regist_info',
        tableName: '资产持仓登记管理'
      },
      abnormalAction: "AssetRegistInfo.getAbnormalData",
      updateStatusAction:"AssetRegistInfo.updateAssetRegistInfoStatus",
      comfirmExportParam:{}
    };
  },
  computed: {
    queryParam() {
      return {
        'startDate': this.searchParam.startDate,
        'prodRegEnc': this.searchParam.prodRegEnc,
        'holdingType': this.searchParam.holdingType,
        'assetCode': this.searchParam.assetCode,
        'mezzanineAssetCode': this.searchParam.mezzanineAssetCode,
        'registerStatus': this.searchParam.registerStatus
      }
    }
  },
  methods: {
  editOpened(){
        this.formData.oldData=Tools.json2str(this.formData);
      },
    handleBeforeUpdate() {
      if (this.formNoChangeCb()) {
        this.$refs.editAssetRegistInfoPopup.close();
        return false
      }
      return true
    },
    setConfirmExportParam() {
      this.comfirmExportParam = {
        startDate: this.searchParam.startDate,
        prodRegEnc: this.searchParam.prodRegEnc,
        holdingType: this.searchParam.holdingType,
        assetCode: this.searchParam.assetCode,
        mezzanineAssetCode: this.searchParam.mezzanineAssetCode,
        registerStatus: this.searchParam.registerStatus
      };
    },
     sumbit_edit(){
              this.$refs.sumbitedit.setIconStyle(0,[]);
              if(this.$refs.editAssetRegistInfoForm.validate()){
                   this.httpUtil.query({
                           url: 'server/json/RptApp/audit/checkassetRegistInfo.json',
                           params:  this.formData
                                    }).then(res => {
                                      if(res.success) {
                                       this.httpUtil.comnUpdate({
                                                action: 'AssetRegistInfo.updateAssetRegistInfo',
                                                params:  this.formData
                                                 }).then(res => {
                                                  if(res.success) {
                                                  this.$refs.editAssetRegistInfoPopup.close();
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
      let startDate = this.infoPop.auditDate;
      let endDate = this.infoPop.auditDate;
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
                     this.$refs.AssetRegistInfoGrid.load(this.searchParam);
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
                 this.$refs.AssetRegistInfoGrid.load(this.searchParam);
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
        let formData = { beginDate: this.uploadBeginDate, queryDate: this.uploadBeginDate};
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
      this.$refs.uploadAssetRegistInfoPopup.close();
      this.$refs.AssetRegistInfoGrid.load(this.searchParam);
    },
    onSubmitError() {
      this.$refs.uploadRef.doReset();
      this.$refs.submitBtn.setIconStyle(1, [])
    },
    popupEdit(row){
      let pathUrl = '/main/zz/errorInfo/AssetRgInfoErr';
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
      this.$set(this.infoPop, 'auditDate', this.searchParam.startDate);
      this.$refs.auditInfoPopup.popup();
    },
  },
  watch: {
		"formData.holdingType": {
			handler(v, o) {
        if (v == "02" || v == "03") {
          this.$set(this.formDataMust, "assetCode", false);
          this.$set(this.formDataMust, "accountCode", true);
          this.$set(this.formDataMust, "netValuation", false);
          this.$set(this.formDataMust, "flValuation", false);
          this.$set(this.formDataMust, "quantity", false);
        } else {
          this.$set(this.formDataMust, "assetCode", true);
          this.$set(this.formDataMust, "accountCode", false);
          this.$set(this.formDataMust, "netValuation", true);
          this.$set(this.formDataMust, "flValuation", true);
          this.$set(this.formDataMust, "quantity", true);
        }
			},
      deep: true,
      immediate: true
		},
    "formData.mezzanineNumber": {
			handler(v, o) {
				if (v == "0") {
					this.$set(this.formDataMust, "mezzanineAssetCode", true);
				} else {
          this.$set(this.formDataMust, "mezzanineAssetCode", false);
        }
			},
      deep: true,
      immediate: true
		},
	}
};
</script>
