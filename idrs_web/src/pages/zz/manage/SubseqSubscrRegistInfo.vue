<template>
  <div class="py-page">
    <div>
      <k-form-search-customize ref="searchFormRef" data-model-name="SubseqSubscrRegistInfo" data-target="subseqSubscrRegistInfoGrid" v-model="queryParam">
        <k-form-item label="数据日期">
          <k-field-date v-model="BusinessReportDate" :data-allowblank="false" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="产品登记编码">
          <k-field-text v-model="searchParam.prodCode"/>
        </k-form-item>
        <k-form-item label="业务起始日">
          <k-field-date v-model="BusinessStartDate" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="业务结束日">
          <k-field-date v-model="BusinessEndDate" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="报送状态">
          <k-field-select v-model="searchParam.registerStatus" data-dict="report_status"  />
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn slot="button" ref="uploadBtnRef" data-functype="POPUP" class="btn-custom-plain"
                  data-target="uploadSubseqSubscrRegistInfoPopup">
            <md-icon>cloud_upload</md-icon>导入
          </k-btn>
          <k-btn slot="button" ref="exportRef" class="btn-custom-plain" data-functype="EXPORT" data-target="subseqSubscrRegistInfoGrid" data-export-dict="true"
                :data-export-name="'产品存续期登记管理'" @downSuccess="downSuccess" :handleBefore="handleBefore" :data-handler="dataHandler">
            <md-icon>cloud_download</md-icon>
            导出
          </k-btn>
          <k-btn slot="button" class="btn-custom-plain"
            :handleBefore="handleBefore" :data-handler="handleConfirmExport">
            <md-icon>cloud_download</md-icon>
            确认并导出
          </k-btn>
          <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="auditPopup">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>复核
          </k-btn>
        </div>
      </div>
      <k-grid ref="subseqSubscrRegistInfoGrid" @data-row-select="selectRow" data-fixed="right" data-operate-width="150px" :data-autoload="false" data-action="SubseqSubscrRegistInfo.findSubseqSubscrRegistInfos" >
        <k-grid-column data-align="left" data-header="数据日期" data-name="theoryReportStartDate" data-export="false" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="*产品登记编码" data-name="prodCode" data-width="130"></k-grid-column>
         <k-grid-column data-align="left" data-header="*发行机构代码" data-name="bankCode" data-width="100"></k-grid-column>
        <k-grid-column data-align="right" data-header="初始净值" data-name="initialNav" data-width="100"></k-grid-column>
        <k-grid-column data-align="right" data-header="产品净值" data-name="nav" data-width="100"></k-grid-column>
        <k-grid-column data-align="right" data-header="累计净值" data-name="aggregateNav" data-width="100"></k-grid-column>
        <k-grid-column data-align="right" data-header="净值币种" data-name="navCur" data-width="100"></k-grid-column>
        <k-grid-column data-align="right" data-header="折算人民币初始净值" data-name="convertInitialNav" data-width="100"></k-grid-column>
        <k-grid-column data-align="right" data-header="折算人民币净值" data-name="convertRmbNav" data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="折算人民币累计净值" data-name="convertRmbAggNav" data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="净值日期" data-name="navDt" data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="实现收益率%" data-name="realizedAnnualReturn" data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="最新预期收益率%" data-name="expectedAnnualReturn" data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="*银行实现收益(元)" data-name="inconmeBank" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="*业务起始日" data-name="businessStartDate"   data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="*业务结束日" data-name="businessEndDate"   data-width="100"></k-grid-column>
        <k-grid-column data-align="right" data-header="*累计申购份额" data-name="subscribedLatestVol" data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="*累计赎回份额" data-name="redeemedLatestVol" data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="每万份份额分红" data-name="unitsBonus" data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="每万份现金分红" data-name="cashBonus" data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="产品余额(元)" data-name="prodAmt" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="*产品份额" data-name="prodVol" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*币种和申购兑付信息" data-name="ccyAndPchRdm" data-width="250"></k-grid-column>
        <k-grid-column data-align="left" data-header="备注" data-name="details" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="登记流水号" data-name="registerSerno" data-width="250" data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="登记日期" data-name="registerDate" data-width="100" data-type="date" data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="报送状态" data-name="registerStatus" data-dict="report_status"  data-width="100" data-export="false"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改产品存续期登记管理" data-functype="POPUP" data-size="mini"
             v-show="scope.row.row.registerStatus != '5'"     data-target="editSubseqSubscrRegistInfoPopup">
            修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="SubseqSubscrRegistInfo.deleteSubseqSubscrRegistInfo" data-size="mini"
             v-show="scope.row.row.registerStatus != '5'"     data-type="danger" data-target="subseqSubscrRegistInfoGrid" :data-confirm="true" data-descript="删除产品存续期登记管理">
            删除
          </k-btn>
          <!--<k-btn data-functype="PAGE" data-size="mini" class="btn-custom-plain" :data-model="scope.row.row.id"
                 @click="popupEdit(scope.row.row)"  data-descript="产品存续期登记管理错误详情">
            错误详情
          </k-btn>-->
        </template>
      </k-grid>
    </div>

    <!--    添加产品存续期登记管理弹出框   -->
    <k-popup ref="addSubseqSubscrRegistInfoPopup" data-title="新增">
      <k-form ref="addSubseqSubscrRegistInfoForm" :data-col="2" isFormBodyScreen data-label-width="150px">
        <k-form-item label="产品登记编码">
          <k-field-text v-model="formData.prodCode" :data-allowblank="false" :data-max-length="15" />
        </k-form-item>
        <k-form-item label="发行机构代码">
          <k-field-text v-model="formData.bankCode" :data-allowblank="false"  :data-max-length="6"/>
        </k-form-item>
        <k-form-item label="初始净值">
          <k-field-text v-model="formData.initialNav"  data-validate-type="money" data-type="money" data-digits="5"  data-integer-length="5"/>
        </k-form-item>
        <k-form-item label="产品净值">
          <k-field-text v-model="formData.nav"  data-validate-type="money" data-type="money" data-digits="5"  data-integer-length="5"/>
        </k-form-item>
        <k-form-item label="累计净值">
          <k-field-text v-model="formData.aggregateNav"  data-validate-type="money" data-type="money" data-digits="5"  data-integer-length="5"/>
        </k-form-item>
        <k-form-item label="净值币种">
          <k-field-select v-model="formData.navCur" :data-allowblank="false" data-dict="tr_cur" />
        </k-form-item>
        <k-form-item label="折算人民币初始净值">
          <k-field-text v-model="formData.convertInitialNav" data-validate-type="money" data-type="money" data-digits="5"  data-integer-length="5"/>
        </k-form-item>
        <k-form-item label="折算人民币净值">
          <k-field-text v-model="formData.convertRmbNav" data-validate-type="money" data-type="money" data-digits="5"  data-integer-length="5"/>
        </k-form-item>
        <k-form-item label="折算人民币累计净值">
          <k-field-text v-model="formData.convertRmbAggNav" data-validate-type="money" data-type="money" data-digits="5"  data-integer-length="5"/>
        </k-form-item>
        <k-form-item label="净值日期">
          <k-field-date v-model="formData.navDt"  data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd" />
        </k-form-item>
        <k-form-item label="实现收益率%">
          <k-field-text v-model="formData.realizedAnnualReturn" data-validate-type="money" data-type="money" data-digits="7"  data-integer-length="1"/>
        </k-form-item>
        <k-form-item label="最新预期收益率%">
          <k-field-text v-model="formData.expectedAnnualReturn"  data-validate-type="money" data-type="money" data-digits="7"  data-integer-length="1"/>
        </k-form-item>
        <k-form-item label="银行实现收益（元）">
          <k-field-text v-model="formData.inconmeBank" :data-allowblank="false"  data-validate-type="money" data-type="money"  data-digits="2"  data-integer-length="13"/>
        </k-form-item>
        <k-form-item label="业务起始日">
          <k-field-date v-model="formData.businessStartDate" :data-allowblank="false"   data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="业务结束日">
          <k-field-date v-model="formData.businessEndDate" :data-allowblank="false"   data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="累计申购份额">
          <k-field-text v-model="formData.subscribedLatestVol" :data-allowblank="false" data-validate-type="money" data-type="money"  data-digits="5"  data-integer-length="13"/>
        </k-form-item>
        <k-form-item label="累计赎回份额">
          <k-field-text v-model="formData.redeemedLatestVol" :data-allowblank="false"  data-validate-type="money" data-type="money"  data-digits="5"  data-integer-length="13"/>
        </k-form-item>
        <k-form-item label="每万份份额分红">
          <k-field-text v-model="formData.unitsBonus" data-validate-type="money" data-type="money"  data-digits="5"  data-integer-length="13"/>
        </k-form-item>
        <k-form-item label="每万份现金分红">
          <k-field-text v-model="formData.cashBonus" data-validate-type="money" data-type="money"  data-digits="5"  data-integer-length="13"/>
        </k-form-item>
        <k-form-item label="产品余额（元）">
          <k-field-text v-model="formData.prodAmt"  data-validate-type="money" data-type="money"  data-digits="2"  data-integer-length="13"/>
        </k-form-item>
        <k-form-item label="产品份额">
          <k-field-text v-model="formData.prodVol"  data-validate-type="money" data-type="money"  data-digits="5"  data-integer-length="13" :data-allowblank="false"/>
        </k-form-item>
         <k-form-item label="币种和申购兑付信息">
           <k-field-text v-model="formData.ccyAndPchRdm" :data-max-length="32" :data-allowblank="false" />
         </k-form-item>
        <k-form-item label="备注">
          <k-field-text v-model="formData.details"/>
        </k-form-item>

        <k-form-footer slot="footer" data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="SubseqSubscrRegistInfo.addSubseqSubscrRegistInfo" data-from="addSubseqSubscrRegistInfoForm"
                 :data-model="formData" data-target="subseqSubscrRegistInfoGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改产品存续期登记管理弹出框   -->
    <k-popup ref="editSubseqSubscrRegistInfoPopup" data-title="修改" @data-opened="editOpened()">
      <k-form ref="editSubseqSubscrRegistInfoForm" :data-col="2" isFormBodyScreen data-label-width="150px">
        <k-form-item label="产品登记编码" :class="[handleItemDiff('prodCode')]">
          <k-field-text v-model="formData.prodCode" :data-disabled="true" :data-allowblank="false" :data-max-length="32"/>
        </k-form-item>
        <k-form-item label="发行机构代码" :class="[handleItemDiff('bankCode')]">
          <k-field-text v-model="formData.bankCode" :data-disabled="true" :data-allowblank="false" :data-max-length="32"/>
        </k-form-item>
        <k-form-item label="初始净值" :class="[handleItemDiff('initialNav')]">
          <k-field-text v-model="formData.initialNav"  data-validate-type="money" data-type="money" data-digits="5"  data-integer-length="5"/>
        </k-form-item>
        <k-form-item label="产品净值" :class="[handleItemDiff('nav')]">
          <k-field-text v-model="formData.nav"  data-validate-type="money" data-type="money" data-digits="5"  data-integer-length="5"/>
        </k-form-item>
        <k-form-item label="累计净值" :class="[handleItemDiff('aggregateNav')]">
          <k-field-text v-model="formData.aggregateNav"  data-validate-type="money" data-type="money" data-digits="5"  data-integer-length="5"/>
        </k-form-item>
        <k-form-item label="净值币种" :class="[handleItemDiff('navCur')]">
          <k-field-select v-model="formData.navCur" :data-allowblank="false" data-dict="tr_cur" />
        </k-form-item>
        <k-form-item label="折算人民币初始净值" :class="[handleItemDiff('convertInitialNav')]">
          <k-field-text v-model="formData.convertInitialNav"  data-validate-type="money" data-type="money" data-digits="5"  data-integer-length="5"/>
        </k-form-item>
        <k-form-item label="折算人民币净值" :class="[handleItemDiff('convertRmbNav')]">
          <k-field-text v-model="formData.convertRmbNav"  data-validate-type="money" data-type="money" data-digits="5"  data-integer-length="5"/>
        </k-form-item>
        <k-form-item label="折算人民币累计净值" :class="[handleItemDiff('convertRmbAggNav')]">
          <k-field-text v-model="formData.convertRmbAggNav"  data-validate-type="money" data-type="money" data-digits="5"  data-integer-length="5"/>
        </k-form-item>
        <k-form-item label="净值日期" :class="[handleItemDiff('navDt')]">
          <k-field-date v-model="formData.navDt"  data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd" />
        </k-form-item>
        <k-form-item label="实现收益率%" :class="[handleItemDiff('realizedAnnualReturn')]">
          <k-field-text v-model="formData.realizedAnnualReturn"  data-validate-type="money" data-type="money" data-digits="7"  data-integer-length="1"/>
        </k-form-item>
        <k-form-item label="最新预期收益率%" :class="[handleItemDiff('expectedAnnualReturn')]">
          <k-field-text v-model="formData.expectedAnnualReturn"  data-validate-type="money" data-type="money" data-digits="7"  data-integer-length="1"/>
        </k-form-item>
        <k-form-item label="银行实现收益（元）" :class="[handleItemDiff('inconmeBank')]">
          <k-field-text v-model="formData.inconmeBank" :data-allowblank="false"  data-validate-type="money" data-type="money" data-digits="2"  data-integer-length="13"/>
        </k-form-item>
        <k-form-item label="业务起始日" :class="[handleItemDiff('businessStartDate')]">
          <k-field-date v-model="formData.businessStartDate" :data-allowblank="false"   data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="业务结束日" :class="[handleItemDiff('businessEndDate')]">
          <k-field-date v-model="formData.businessEndDate" :data-allowblank="false"   data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="累计申购份额" :class="[handleItemDiff('subscribedLatestVol')]">
          <k-field-text v-model="formData.subscribedLatestVol" :data-allowblank="false"  data-validate-type="money" data-type="money" data-digits="5"  data-integer-length="13"/>
        </k-form-item>
        <k-form-item label="累计赎回份额" :class="[handleItemDiff('redeemedLatestVol')]">
          <k-field-text v-model="formData.redeemedLatestVol" :data-allowblank="false"  data-validate-type="money" data-type="money"  data-digits="5"  data-integer-length="13"/>
        </k-form-item>
        <k-form-item label="每万份份额分红" :class="[handleItemDiff('unitsBonus')]">
          <k-field-text v-model="formData.unitsBonus" data-validate-type="money" data-type="money"  data-digits="5"  data-integer-length="13"/>
        </k-form-item>
        <k-form-item label="每万份现金分红" :class="[handleItemDiff('cashBonus')]">
          <k-field-text v-model="formData.cashBonus" data-validate-type="money" data-type="money"  data-digits="5"  data-integer-length="13"/>
        </k-form-item>
        <k-form-item label="产品余额（元）" :class="[handleItemDiff('prodAmt')]">
          <k-field-text v-model="formData.prodAmt"  data-validate-type="money" data-type="money"  data-digits="2"  data-integer-length="13"/>
        </k-form-item>
        <k-form-item label="产品份额" :class="[handleItemDiff('prodVol')]">
          <k-field-text v-model="formData.prodVol"   data-validate-type="money" data-type="money"  data-digits="5"  data-integer-length="13" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="币种和申购兑付信息" :class="[handleItemDiff('ccyAndPchRdm')]">
         <k-field-text v-model="formData.ccyAndPchRdm" :data-max-length="32" :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="备注" :class="[handleItemDiff('details')]">
          <k-field-text v-model="formData.details"/>
        </k-form-item>
        <k-form-footer slot="footer" data-align="center">
          <k-btn class="btn-custom-primary"  ref="sumbitedit" :data-handler="sumbit_edit" data-from="editSubseqSubscrRegistInfoForm"
                 :data-model="formData" data-target="subseqSubscrRegistInfoGrid" :handle-before="handleBeforeUpdate">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>


    <k-popup ref="uploadSubseqSubscrRegistInfoPopup" title="报送数据导入" @data-opened="uploadOpened()">
        <k-form ref="addForm" data-ui="element">
          <k-form-item label="数据日期">
            <k-field-date v-model="formData.reportDate" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd" :data-allowblank="false"/>
          </k-form-item>
          <k-form-item label="报送数据导入" data-ui="element" data-input-width="500px">
            <k-field-excel-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit='1'
                data-accept=".xlsx,.xls"
                :data-error="onSubmitError" :data-success="onSubmitSuccess"
                :data-auto-upload="false"
                data-upload-url="upload/server/RptApp/reportManage/subseqSubscrRegistImport.json">
            </k-field-excel-upload>
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="subseqSubscrRegistInfoGrid" ref="submitBtn"
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
import ProdMixin from "@/pages/zz/manage/mixins/prodMixin.js";
import Tools from "@/utils/tools";
export default {
  name: "SubseqSubscrRegistInfo",
  mixins: [ProdMixin],
  data() {
    return {
      formData: {},
      formDataCopy: {},
      selectRowData: {},
      searchParam:{
        registerSerno: '',//登记流水号
      },
      BusinessReportDate:[],
      BusinessStartDate:[],
      BusinessEndDate:[],
      queryParamDateRange:[],
      infoPop: {},
      auditInfoPopupData: {
        tableId: 'app_subseq_subscr_regist_info',
        tableName: '产品存续期登记管理'
      },
      abnormalAction: "SubseqSubscrRegistInfo.getAbnormalData",
      updateStatusAction: "SubseqSubscrRegistInfo.updateSubseqSubscrRegistInfoStatus",
      comfirmExportParam: {}
    };
  },
  computed: {
    queryParam() {
      return {
        'startDate1': this.BusinessStartDate ? this.BusinessStartDate[0] : null,
        'endDate1': this.BusinessStartDate ? this.BusinessStartDate[1] : null,
        'startDate2': this.BusinessEndDate ? this.BusinessEndDate[0] : null,
        'endDate2': this.BusinessEndDate ? this.BusinessEndDate[1] : null,
        'reportDateStart': this.BusinessReportDate ? this.BusinessReportDate[0] : null,
        'reportDateEnd': this.BusinessReportDate ? this.BusinessReportDate[1] : null,
        'prodCode': this.searchParam.prodCode,
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
        this.$refs.editSubseqSubscrRegistInfoPopup.close();
        return false
      }
      return true
    },
		dataHandler() {
			if (this.BusinessReportDate == null) {
				this.$message.error("数据日期不能为空!");
				return false;
			}
			setTimeout(()=>{
				this.$refs.exportRef.handleExport(this.queryParam);
			}, 500)
			return false
		},
    sumbit_edit(){
          this.$refs.sumbitedit.setIconStyle(0,[]);
          if(this.$refs.editSubseqSubscrRegistInfoForm.validate()){
               this.httpUtil.query({
                       url: 'server/json/RptApp/audit/checkSubseqSubscrRegistInfo.json',
                       params:  this.formData
                                }).then(res => {
                                  if(res.success) {
                                   this.httpUtil.comnUpdate({
                                            action: 'SubseqSubscrRegistInfo.updateSubseqSubscrRegistInfo',
                                            params:  this.formData
                                             }).then(res => {
                                              if(res.success) {
                                              this.$refs.editSubseqSubscrRegistInfoPopup.close();
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
                     this.$refs.subseqSubscrRegistInfoGrid.load(this.searchParam);
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
                 this.$refs.subseqSubscrRegistInfoGrid.load(this.searchParam);
                 this.$refs.auditInfoPopup.close();
                }
              })
           }
         }
       })
    },
    setConfirmExportParam() {
      this.comfirmExportParam = {
        startDate1: this.BusinessStartDate ? this.BusinessStartDate[0] : null,
        endDate1: this.BusinessStartDate ? this.BusinessStartDate[1] : null,
        startDate2: this.BusinessEndDate ? this.BusinessEndDate[0] : null,
        endDate2: this.BusinessEndDate ? this.BusinessEndDate[1] : null,
        reportDateStart: this.BusinessReportDate ? this.BusinessReportDate[0] : null,
        reportDateEnd: this.BusinessReportDate ? this.BusinessReportDate[1] : null,
        prodCode: this.searchParam.prodCode,
        registerStatus: this.searchParam.registerStatus,
      };
    },
    submitUploadParam() {
      //文件上传校验
      let validate = this.$refs.addForm.validate();
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
      }
    },
    onSubmitSuccess() {
      this.$refs.uploadRef.doReset();
      this.$refs.addForm.reset();
      this.$refs.uploadSubseqSubscrRegistInfoPopup.close();
      this.$refs.subseqSubscrRegistInfoGrid.load(this.queryParam);
      this.formData.reportDate = '';
    },
    onSubmitError() {
      this.$refs.uploadRef.doReset();
      this.$refs.submitBtn.setIconStyle(1, []);
      this.formData.reportDate = ''
    },
    popupEdit(row){
      let pathUrl = '/main/zz/errorInfo/SeqScrRgInfoErr';
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
     // this.formData.reportDate = ''
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
    this.BusinessReportDate = [defaultDate, defaultDate];
  },
  watch: {
    BusinessReportDate() {
      this.$set(this.searchParam, 'reportDateStart', this.BusinessReportDate == null ? null : this.BusinessReportDate[0]);
      this.$set(this.searchParam, 'reportDateEnd', this.BusinessReportDate == null ? '' : this.BusinessReportDate[1]);
    },
    //查询起息日
    BusinessStartDate() {
      this.$set(this.searchParam, 'startDate1', this.BusinessStartDate == null ? null : this.BusinessStartDate[0]);
      this.$set(this.searchParam, 'endDate1', this.BusinessStartDate == null ? null : this.BusinessStartDate[1]);
    },
    BusinessEndDate() {
      this.$set(this.searchParam, 'startDate2', this.BusinessEndDate == null ? null : this.BusinessEndDate[0]);
      this.$set(this.searchParam, 'endDate2', this.BusinessEndDate == null ? '' : this.BusinessEndDate[1]);
    },
  }
};
</script>
