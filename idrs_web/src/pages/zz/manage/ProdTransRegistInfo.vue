<template>
  <div class="py-page">
     <k-form-search-customize data-model-name="prodTransRegistGrid" data-target="prodTransRegistInfoGrid" data-label-width="130px" v-model="queryParam" ref="searchFormRef">
          <k-form-item label="产品登记编码">
            <k-field-text v-model="searchParam.prodCode"/>
          </k-form-item>
            <k-form-item label="交易日(数据日期)" data-label-width="150px">
              <k-field-date v-model="queryRegisterDate" data-type="daterange"  data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"  :data-allowblank="false"/>
            </k-form-item>
          <k-form-item label="行内资产/负债编码">
            <k-field-text v-model="searchParam.assetCode"/>
          </k-form-item>
          <k-form-item label="报送状态">
            <k-field-select v-model="searchParam.registerStatus" data-dict="subm_report_status"/>
          </k-form-item>
          <!--
        <k-form-item label="登记日期">
              <k-field-date v-model="queryRegisterDate" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
            </k-form-item>
            -->
          <k-form-item label="资金流动类型">
            <k-field-select v-model="searchParam.cashType" data-dict="subm_cash_type"/>
          </k-form-item>
          </k-form-search-customize>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <!-- <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addProdTransRegistInfoPopup" slot="button">
              <md-icon md-src="/static/svg/add.svg" />新增</k-btn> -->
          <k-btn slot="button" ref="uploadBtnRef" data-functype="POPUP" class="btn-custom-plain"
                  data-target="ImportAssetRegistInfoPopup">
            <md-icon>cloud_upload</md-icon>导入
          </k-btn>
          <k-btn slot="button" ref="exportRef"  class="btn-custom-plain"  data-functype="EXPORT" data-target="prodTransRegistInfoGrid" data-export-dict="true"
                :data-export-name="'交易信息登记管理'" data-export-form="searchFormRef" :handleBefore="handleBefore"  @downSuccess="downSuccess"   :data-handler="handleExport">
            <md-icon>cloud_download</md-icon>
            导出
          </k-btn>
          <k-btn slot="button" class="btn-custom-plain" :handleBefore="handleBefore" :data-handler="handleConfirmExport">
            <md-icon>cloud_download</md-icon>确认并导出
          </k-btn>
          <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="auditPopup">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>复核
          </k-btn>
          <k-btn slot="button" ref="uploadBtnRef" data-functype="POPUP" class="btn-custom-plain"
                  data-target="uploadProdTransRegistInfoPopup">
            <md-icon>cloud_upload</md-icon>批量修改导入
          </k-btn>
        </div>
      </div>
       <k-grid ref="prodTransRegistInfoGrid" @data-row-select="selectRow" :data-autoload="false" data-fixed="right" data-operate-width="250px" data-action="ProdTransRegistInfo.findProdTransRegistInfos" >
        <k-grid-column data-align="left" data-header="报送状态" data-name="registerStatus" data-dict="report_status" data-export="false" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="id" data-name="id" :data-hidden="true" data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品登记编码" data-name="prodCode"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="发行机构代码" data-name="bankCode"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="行内资产/负债编码" data-name="assetCode"  data-width="160"></k-grid-column>
        <k-grid-column data-align="left" data-header="行内交易编码" data-name="transCode"  data-width="160"></k-grid-column>
        <k-grid-column data-align="left" data-header="资金流动类型" data-name="cashType" data-dict="subm_cash_type"  data-width="160"></k-grid-column>
        <k-grid-column data-align="left" data-header="资金流动类型说明" data-name="detailCashType"  data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="数量" data-name="quantity" data-width="120" ></k-grid-column>
        <k-grid-column data-align="left" data-header="资产计量方式" data-name="methodAssetMeasure" data-dict="subm_asset_measure"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="币种" data-name="cur"  data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="折算人民币金额" data-name="convertRmb" data-type="money"  data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="发生金额" data-name="amt" data-type="money"  data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="单位成交价格(净价)" data-name="unitPriceNet"  data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="单位成交价格(全价)" data-name="unitPriceFull"  data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="到期收益率%" data-name="rateAnnualReturn"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="交易对手方" data-name="tradeCounter"  data-width="250"></k-grid-column>
        <k-grid-column data-align="left" data-header="关联交易情况" data-name="relatedPartyTrans" data-dict="subm_related_party_trans"  data-width="200"></k-grid-column>
        <k-grid-column data-align="left" data-header="交易对手方类型" data-name="counterType" data-dict="subm_counterparty_type"  data-width="160"></k-grid-column>
        <k-grid-column data-align="left" data-header="成交编号/合同号" data-name="transIdentCode"  data-width="160"></k-grid-column>
        <k-grid-column data-align="left" data-header="交易审批人身份证号" data-name="transApproveId"  data-width="160"></k-grid-column>
        <k-grid-column data-align="left" data-header="交易审批人姓名" data-name="transApproveName"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="交易员身份证号" data-name="traderId"  data-width="160"></k-grid-column>
        <k-grid-column data-align="left" data-header="交易员姓名" data-name="traderName"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="交易日" data-name="tradeDate"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="交易发起时间" data-name="trxTm"  data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="备注" data-name="details"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="登记流水号" data-name="registerSerno" data-width="250" data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="登记日期" data-name="registerDate" data-width="100" data-export="false"  ></k-grid-column>
        <!--
        <k-grid-column data-align="left" data-header="理论报送起始日期" data-name="theoryReportStartDate"  data-width="100" data-export="false"  ></k-grid-column>
        <k-grid-column data-align="left" data-header="理论报送截止日期" data-name="theoryReportEndDate" data-width="100" data-export="false"  ></k-grid-column>
        -->
        <k-grid-column data-align="left" data-header="新增日期" data-name="createDate" data-width="100" data-export="false"  ></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改交易信息登记管理" data-functype="POPUP" data-size="mini"
               v-show="scope.row.row.registerStatus != '5'"   data-target="editProdTransRegistInfoPopup">
            修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="ProdTransRegistInfo.deleteProdTransRegistInfo" data-size="mini"
               v-show="scope.row.row.registerStatus != '5'"   data-type="danger" data-target="prodTransRegistInfoGrid" :data-confirm="true" data-descript="删除交易信息登记管理">
            删除
          </k-btn>
          <!--<k-btn data-functype="PAGE" data-size="mini" class="btn-custom-plain" :data-model="scope.row.row.id"
                 @click="popupEdit(scope.row.row)"  data-descript="交易信息登记管理错误详情">
            错误详情
          </k-btn>-->
        </template>
      </k-grid>
    </div>

    <!--    添加交易信息登记管理弹出框   -->
    <k-popup ref="addProdTransRegistInfoPopup" data-title="新增">
      <k-form ref="addProdTransRegistInfoForm" :data-col="2" isFormBodyScreen>
        <k-form-item label="产品登记编码">
          <k-field-text v-model="formData.prodCode" :data-allowblank="false" :data-max-length="15"/>
        </k-form-item>
        <k-form-item label="资金流动类型">
          <k-field-select v-model="formData.cashType" :data-allowblank="false" data-dict="subm_cash_type"  @data-on-change="dataOnChange"/>
        </k-form-item>
        <k-form-item label="发行机构代码">
          <k-field-text v-model="formData.bankCode" :data-allowblank="false" :data-max-length="6"/>
        </k-form-item>
        <k-form-item label="行内资产/负债编码">
          <k-field-text v-model="formData.assetCode" :data-allowblank="formData.assetCodeAllowblank" :data-disabled="formData.assetCodeDisabled" :data-max-length="15"/>
        </k-form-item>
        <k-form-item label="行内交易编码">
          <k-field-text v-model="formData.transCode" :data-allowblank="false" :data-max-length="32"/>
        </k-form-item>
        <k-form-item label="资金流动类型说明">
          <k-field-text v-model="formData.detailCashType" :data-allowblank="false"  :data-max-length="60"/>
        </k-form-item>
        <k-form-item label="数量">
          <k-field-text v-model="formData.quantity" :data-allowblank="false" data-min-value="0" data-digits="5"  data-integer-length="13"
                        data-validate-type="number" data-regx-text="请输入大于等于0的数字" />
        </k-form-item>
        <k-form-item label="资产计量方式">
          <k-field-select v-model="formData.methodAssetMeasure" :data-allowblank="formData.methodAssetMeasureAllowblank" :data-disabled="formData.methodAssetMeasureDisabled" data-dict="subm_asset_measure"/>
        </k-form-item>
        <k-form-item label="币种">
          <k-field-select  v-model="formData.cur" :data-allowblank="false" data-dict="subm_t8_prod_currtype_more" data-default-value="CNY"/>
        </k-form-item>
        <k-form-item label="折算人民币金额">
          <k-field-text v-model="formData.convertRmb" :data-allowblank="false" data-digits="2"  data-integer-length="13" data-min-value="0"
                       data-validate-type="money" data-regx-text="请输入大于等于0的数字" />
        </k-form-item>
        <k-form-item label="发生金额">
          <k-field-text v-model="formData.amt" :data-allowblank="false" data-digits="2"  data-integer-length="13" data-min-value="0"
                        data-validate-type="money" data-regx-text="请输入大于等于0的数字"/>
        </k-form-item>

        <k-form-item label="单位成交价格(净价)">
          <k-field-text v-model="formData.unitPriceNet" :data-allowblank="formData.unitPriceNetAllowblank" data-digits="4"  data-integer-length="13"
                        data-validate-type="money"/>
        </k-form-item>

        <k-form-item label="单位成交价格(全价)">
          <k-field-text v-model="formData.unitPriceFull" :data-allowblank="formData.unitPriceFullAllowblank"  data-digits="4"  data-integer-length="13"
                        data-validate-type="money" :data-min-value="formData.unitPriceNet"/>
        </k-form-item>
        <k-form-item label="到期收益率%">
          <k-field-text v-model="formData.rateAnnualReturn"  data-digits="7"  data-integer-length="1"
                        data-validate-type="money"/>
        </k-form-item>
        <k-form-item label="交易对手方">
          <k-field-text v-model="formData.tradeCounter" :data-allowblank="false"  :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="关联交易情况">
          <k-field-select v-model="formData.relatedPartyTrans" :data-allowblank="false" data-dict="subm_related_party_trans" @data-on-change="dataOnChange"/>
        </k-form-item>
        <k-form-item label="交易对手方类型">
          <k-field-select v-model="formData.counterType" :data-allowblank="false" data-dict="subm_counterparty_type" @data-on-change="dataOnChange"/>
        </k-form-item>
        <k-form-item label="成交编号/合同号">
          <k-field-text v-model="formData.transIdentCode" :data-allowblank="false" :data-max-length="100"/>
        </k-form-item>
        <k-form-item label="交易审批人身份证号">
          <k-field-text v-model="formData.transApproveId"  :data-allowblank="formData.transApproveIdAllowblank" :data-max-length="30"/>
        </k-form-item>
        <k-form-item label="交易审批人姓名">
          <k-field-text v-model="formData.transApproveName" :data-allowblank="formData.transApproveNameAllowblank" :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="交易员身份证号">
          <k-field-text v-model="formData.traderId" :data-allowblank="formData.traderIdAllowblank" :data-max-length="30"/>
        </k-form-item>
        <k-form-item label="交易员姓名">
          <k-field-text v-model="formData.traderName" :data-allowblank="formData.traderNameAllowblank" :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="交易日">
          <k-field-date v-model="formData.tradeDate" :data-allowblank="false" data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="交易发起时间">
          <k-field-text v-model="formData.trxTm" :data-allowblank="false" :data-max-length="14" data-validate-type="int"/>
        </k-form-item>
        <k-form-item label="备注">
          <k-field-text v-model="formData.details" :data-allowblank="formData.detailsAllowblank" :data-max-length="256"/>
        </k-form-item>
        <k-form-footer slot="footer" data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ProdTransRegistInfo.addProdTransRegistInfo" data-from="addProdTransRegistInfoForm"
                 :data-model="formData" data-target="prodTransRegistInfoGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改交易信息登记管理弹出框   -->
    <k-popup ref="editProdTransRegistInfoPopup" data-title="修改" @data-opened="editOpened()">
      <k-form ref="editProdTransRegistInfoForm" :data-col="2" isFormBodyScreen>
        <k-form-item label="产品登记编码" :class="[handleItemDiff('prodCode')]">
          <k-field-text v-model="formData.prodCode" :data-allowblank="false"  :data-max-length="15" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="资金流动类型" :class="[handleItemDiff('cashType')]">
          <k-field-select v-model="formData.cashType" :data-allowblank="false" :data-disabled="true"  data-dict="subm_cash_type"  @data-on-change="dataOnChange"/>
        </k-form-item>
        <k-form-item label="发行机构代码" :class="[handleItemDiff('bankCode')]">
          <k-field-text v-model="formData.bankCode" :data-allowblank="false" :data-disabled="true" :data-max-length="6"/>
        </k-form-item>
        <k-form-item label="行内资产/负债编码" :class="[handleItemDiff('assetCode')]">
          <k-field-text v-model="formData.assetCode" :data-allowblank="formData.assetCodeAllowblank" :data-disabled="true" :data-max-length="15"/>
        </k-form-item>
        <k-form-item label="行内交易编码" :class="[handleItemDiff('transCode')]">
          <k-field-text v-model="formData.transCode" :data-allowblank="false" :data-max-length="32" :data-disabled="true" />
        </k-form-item>
        <k-form-item label="资金流动类型说明" :class="[handleItemDiff('detailCashType')]">
          <k-field-text v-model="formData.detailCashType" :data-allowblank="false"  :data-max-length="60"/>
        </k-form-item>
        <k-form-item label="数量" :class="[handleItemDiff('quantity')]">
          <k-field-text v-model="formData.quantity" :data-allowblank="false" data-min-value="0" data-digits="5"  data-integer-length="13"
                        data-validate-type="number" data-regx-text="请输入大于等于0的数字" />
        </k-form-item>
        <k-form-item label="资产计量方式" :class="[handleItemDiff('methodAssetMeasure')]">
          <k-field-select v-model="formData.methodAssetMeasure" :data-allowblank="formData.methodAssetMeasureAllowblank" :data-disabled="formData.methodAssetMeasureDisabled" data-dict="subm_asset_measure"/>
        </k-form-item>
        <k-form-item label="币种" :class="[handleItemDiff('cur')]">
          <k-field-select  v-model="formData.cur" :data-allowblank="false" data-dict="subm_t8_prod_currtype_more" data-default-value="CNY"/>
        </k-form-item>
        <k-form-item label="折算人民币金额" :class="[handleItemDiff('convertRmb')]">
          <k-field-text v-model="formData.convertRmb" :data-allowblank="false" data-digits="2"  data-integer-length="13" data-min-value="0"
                       data-validate-type="money" data-regx-text="请输入大于等于0的数字" />
        </k-form-item>
        <k-form-item label="发生金额" :class="[handleItemDiff('amt')]">
          <k-field-text v-model="formData.amt" :data-allowblank="false" data-digits="2"  data-integer-length="13" data-min-value="0"
                        data-validate-type="money" data-regx-text="请输入大于等于0的数字"/>
        </k-form-item>

        <k-form-item label="单位成交价格(净价)" :class="[handleItemDiff('unitPriceNet')]">
          <k-field-text v-model="formData.unitPriceNet" :data-allowblank="formData.unitPriceNetAllowblank" data-digits="4"  data-integer-length="13"
                        data-validate-type="money"/>
        </k-form-item>

        <k-form-item label="单位成交价格(全价)" :class="[handleItemDiff('unitPriceFull')]">
          <k-field-text v-model="formData.unitPriceFull" :data-allowblank="formData.unitPriceFullAllowblank"  data-digits="4"  data-integer-length="13"
                        data-validate-type="money" :data-min-value="formData.unitPriceNet"/>
        </k-form-item>
        <k-form-item label="到期收益率%" :class="[handleItemDiff('rateAnnualReturn')]">
          <k-field-text v-model="formData.rateAnnualReturn"  data-digits="7"  data-integer-length="1"
                        data-validate-type="money"/>
        </k-form-item>
        <k-form-item label="交易对手方" :class="[handleItemDiff('tradeCounter')]">
          <k-field-text v-model="formData.tradeCounter" :data-allowblank="false"  :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="关联交易情况" :class="[handleItemDiff('relatedPartyTrans')]">
          <k-field-select v-model="formData.relatedPartyTrans" :data-allowblank="false" data-dict="subm_related_party_trans" @data-on-change="dataOnChange"/>
        </k-form-item>
        <k-form-item label="交易对手方类型" :class="[handleItemDiff('counterType')]">
          <k-field-select v-model="formData.counterType" :data-allowblank="false" data-dict="subm_counterparty_type" @data-on-change="dataOnChange"/>
        </k-form-item>
        <k-form-item label="成交编号/合同号" :class="[handleItemDiff('transIdentCode')]">
          <k-field-text v-model="formData.transIdentCode" :data-allowblank="false" :data-max-length="100"/>
        </k-form-item>
        <k-form-item label="交易审批人身份证号" :class="[handleItemDiff('transApproveId')]">
          <k-field-text v-model="formData.transApproveId"  :data-allowblank="formData.transApproveIdAllowblank" :data-max-length="30"/>
        </k-form-item>
        <k-form-item label="交易审批人身份证号" v-if="false">
          <k-field-text v-model="formData.initTransApproveId"  :data-allowblank="formData.transApproveIdAllowblank" :data-max-length="30"/>
        </k-form-item>
        <k-form-item label="交易审批人姓名" :class="[handleItemDiff('transApproveName')]">
          <k-field-text v-model="formData.transApproveName" :data-allowblank="formData.transApproveNameAllowblank" :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="交易员身份证号" :class="[handleItemDiff('traderId')]">
          <k-field-text v-model="formData.traderId" :data-allowblank="formData.traderIdAllowblank" :data-max-length="30"/>
        </k-form-item>
        <k-form-item label="交易员身份证号" v-if="false">
          <k-field-text v-model="formData.initTraderId" :data-allowblank="formData.traderIdAllowblank" :data-max-length="30"/>
         </k-form-item>
        <k-form-item label="交易员姓名" :class="[handleItemDiff('traderName')]">
          <k-field-text v-model="formData.traderName" :data-allowblank="formData.traderNameAllowblank" :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="交易日" :class="[handleItemDiff('tradeDate')]">
          <k-field-date v-model="formData.tradeDate" :data-allowblank="false" data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="交易发起时间" :class="[handleItemDiff('trxTm')]">
          <k-field-text v-model="formData.trxTm" :data-allowblank="false" :data-max-length="14" data-validate-type="int"/>
        </k-form-item>
        <k-form-item label="备注" :class="[handleItemDiff('details')]">
          <k-field-text v-model="formData.details" :data-allowblank="formData.detailsAllowblank" :data-max-length="256"/>
        </k-form-item>
        <k-form-item label="是否覆盖数据" :class="[handleItemDiff('isCover')]">
          <k-field-select v-model="formData.isCover" :data-allowblank="false" data-dict="1yes2no" data-dict-type="1"/>
        </k-form-item>
        <k-form-footer slot="footer" data-align="center">
          <k-btn class="btn-custom-primary" ref="sumbitedit"    data-from="editProdTransRegistInfoForm"
                 :data-model="formData" data-target="prodTransRegistInfoGrid"   :data-handler="sumbit_edit" :handle-before="handleBeforeUpdate">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>


    <k-popup ref="uploadProdTransRegistInfoPopup" title="批量修改导入" @data-opened="uploadOpened()">
        <k-form ref="addForm" data-ui="element">
          <k-form-item label="是否覆盖数据">
            <k-field-select v-model="isCover" :data-allowblank="false" data-dict="1yes2no" data-dict-type="1"/>
          </k-form-item>
          <k-form-item label="修改数据导入" data-ui="element" data-input-width="500px">
            <k-field-excel-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit='1'
                data-accept=".xlsx,.xls"
                :data-error="onSubmitError" :data-success="onSubmitSuccess"
                :data-auto-upload="false"
                :dataHttpRequest="httpRequest"
                >
            </k-field-excel-upload>
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="prodTransRegistInfoGrid" ref="submitBtn"
                data-auto-upload="false" data-from="addForm" :data-handler="submitUploadParam">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
            </k-btn>
          </k-form-footer>
        </k-form>
    </k-popup>

   <k-popup ref="ImportAssetRegistInfoPopup" title="数据导入">
         <k-form ref="addFormAdd" data-ui="element">
          <k-form-item label="数据日期">
              <k-field-date v-model="importData" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd" :data-allowblank="false"/>
           </k-form-item>
           <k-form-item label="报送数据导入" data-ui="element" data-input-width="500px">
             <k-field-excel-upload data-type="file"   ref="uploadAddRef" :data-multiple="false" :data-limit='1'
                                   data-accept=".xlsx,.xls"
                                   :data-error="onAddSubmitError" :data-success="onAddSubmitSuccess"
                                   :data-auto-upload="false"
                                   data-upload-url="upload/server/RptApp/reportManage/prodTransRegistInfoImport.json">
             </k-field-excel-upload>
           </k-form-item>
           <k-form-footer data-align="center">
             <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="AssetRegistInfoGrid" ref="submitBtn"
                    :data-auto-upload="false" data-from="uploadRefAdd" :data-handler="submitAddUploadParam" >确定
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
import ProdMixin from "@/pages/zz/manage/mixins/prodMixin.js";

export default {
  name: "ProdTransRegistInfo",
  mixins: [ProdMixin],
  data() {
    return {
      formData: {},
      formDataCopy: {},
      selectRowData: {},
      searchParam:{},
      queryRegisterDate:[],
      queryParamDateRange:[],
      importData:'',
      fileData: [],
      isCover: '01',
      ischeck :false,
      infoPop: {},
      auditInfoPopupData: {
        tableId: 'app_prod_trans_regist_info',
        tableName: '交易信息登记管理'
      },
       abnormalAction: "ProdTransRegistInfo.getAbnormalData",
       updateStatusAction: "ProdTransRegistInfo.updateProdTransRegistInfoStatus",
       comfirmExportParam:{}
    };
  },
   created() {
      },
    computed: {
      queryParam () {
          return {
              'startDate': this.queryRegisterDate ? this.queryRegisterDate[0] : null,
              'endDate': this.queryRegisterDate ? this.queryRegisterDate[1] : null,
              'queryStartDate': this.queryRegisterDate ? this.queryRegisterDate[0] : null,
              'queryEndDate': this.queryRegisterDate ? this.queryRegisterDate[1] : null,
              'prodCode': this.searchParam.prodCode,
              'assetCode': this.searchParam.assetCode,
              'registerStatus': this.searchParam.registerStatus,
              'cashType': this.searchParam.cashType
          }
        }
    },
  methods: {
    handleBeforeUpdate() {
      if (this.formNoChangeCb()) {
          this.$refs.editProdTransRegistInfoPopup.close();
          return false;
      }
     return true;
    },
   onSubmitSuccess() {
      this.$refs.addForm.reset();
      this.$refs.uploadRef.doReset();
      this.$refs.submitBtn.setIconStyle(1);
      this.$refs.uploadBtnRef.setIconStyle(1);
      this.$refs.uploadProdTransRegistInfoPopup.close();
      this.$refs.prodTransRegistInfoGrid.load(this.queryParam);
    },
    onSubmitError() {
      this.$refs.uploadRef.doReset();
      this.$refs.submitBtn.setIconStyle(1);
      this.$refs.uploadBtnRef.setIconStyle(1);
    },
   onAddSubmitSuccess() {
      this.$refs.addFormAdd.reset();
      this.$refs.uploadAddRef.doReset();
      this.$refs.submitBtn.setIconStyle(1);
      this.$refs.uploadBtnRef.setIconStyle(1);
      this.$refs.ImportAssetRegistInfoPopup.close();
      this.$refs.prodTransRegistInfoGrid.load(this.queryParam);
    },
    onAddSubmitError() {
      this.$refs.uploadAddRef.doReset();
      this.$refs.submitBtn.setIconStyle(1);
      this.$refs.uploadBtnRef.setIconStyle(1);
    },
    sumbit_edit(){
    this.$refs.sumbitedit.setIconStyle(0,[]);
     if(this.$refs.editProdTransRegistInfoForm.validate()){
           this.httpUtil.query({
                   url: 'server/json/RptApp/audit/checkprodTransRegistInfo.json',
                   params:  this.formData
                            }).then(res => {
                              if(res.success) {
                               this.httpUtil.comnUpdate({
                                        action: 'ProdTransRegistInfo.updateProdTransRegistInfo',
                                        params:  this.formData
                                         }).then(res => {
                                          if(res.success) {
                                          this.$refs.editProdTransRegistInfoPopup.close();
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
   setConfirmExportParam() {
            this.comfirmExportParam = {
              'startDate': this.queryRegisterDate ? this.queryRegisterDate[0] : null,
              'endDate': this.queryRegisterDate ? this.queryRegisterDate[1] : null,
               'queryStartDate': this.queryRegisterDate ? this.queryRegisterDate[0] : null,
               'queryEndDate': this.queryRegisterDate ? this.queryRegisterDate[1] : null,
               'prodCode': this.searchParam.prodCode,
               'assetCode': this.searchParam.assetCode,
               'registerStatus': this.searchParam.registerStatus,
               'cashType': this.searchParam.cashType
            };
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
                     this.$refs.prodTransRegistInfoGrid.load(this.queryParam);
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
                 this.$refs.prodTransRegistInfoGrid.load(this.queryParam);
                 this.$refs.auditInfoPopup.close();
                }
              })
           }
         }
       })
    },
    httpRequest(file) {
      this.fileData.append("files", file.file);
      this.fileData.append("isCover", this.isCover);
    },
    submitUploadParam() {
      //文件上传校验
      let validate = this.$refs.addForm.validate();
      if (validate) {
        let temp = document.getElementsByClassName('upload-demo');
        let lis = temp[0].childNodes[1].childNodes.length;
        if (lis > 0) {
          // 检查下是否已经有正在审批的文件，如果有则提示，不允许上传
          this.httpUtil
            .comnQuery({
              action: "ProdTransRegistInfo.fileStatusQuery",
              params: {},
              successAlert: false,
            })
            .then((data) => {
              if (data.returndata.flag == '0') {
                this.$refs.submitBtn.setIconStyle(0);
                this.$refs.uploadBtnRef.setIconStyle(0);
                this.fileData = new FormData();
				        this.$refs.uploadRef.upload();
                this.httpUtil.upload({
                  url: "/upload-files/server/RptApp/reportManage/prodTransRegistImport.json",
                  formData: this.fileData,
                }).then((res) => {
                  debugger
                  if (res.data.success) {
                    // 文件上传后，解析文件数据并入库
                    this.httpUtil
                      .comnUpdate({
                        action: "ProdTransRegistInfo.prodTransRegistImport",
                        params: {
                          id: res.data.returndata.id,
                          isCover: this.isCover
                        },
                        mask: false,
                      })
                      .then(result => {
                        this.onSubmitSuccess();
                      });
                  } else {
                    this.onSubmitError();
                  }
                }).catch(res => {
                  this.onSubmitError();
                });
              } else if (data.returndata.flag == '1') {
                Tools.alertTime(data.returnmsg, "danger", 0);
                this.$refs.uploadBtnRef.setIconStyle(1);
                this.$refs.uploadProdTransRegistInfoPopup.close();
              } else if (data.returndata.flag == '2') {
                Tools.alertTime(data.returnmsg, "success", 3000);
                this.$refs.uploadBtnRef.setIconStyle(1);
                this.$refs.uploadProdTransRegistInfoPopup.close();
              }
            });
        } else {
          this.$message.error("上传文件不能为空!");
          return false;
        }
      }
    },
    submitAddUploadParam() {
          //文件上传校验
          let validate = this.$refs.addFormAdd.validate();
          if (validate) {
            let formData = {reportDate: this.importData};
            let temp = document.getElementsByClassName('upload-demo');
            let lis = temp[0].childNodes[1].childNodes.length;
            if (lis > 0) {
              this.$refs.uploadAddRef.upload(formData);
            } else {
              this.$message.error("上传文件不能为空!");
              return false;
            }
          }
    },

    editOpened(){
      this.$set(this.formData, 'initTransApproveId', this.formData.transApproveId);
      this.$set(this.formData, 'initTraderId', this.formData.traderId);
      this.dataOnChange();
      this.formDataCopy = Object.assign({}, this.formData);
      this.formData.oldData=Tools.json2str(this.formData);
    },
    popupEdit(row){
      let pathUrl = '/main/zz/errorInfo/ProdTransRgInfoErr';
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
      this.isCover = '01'
    },
    dataOnChange(){
            //行内资产/负债编码  当“资金流动类型”选择 01-10 项时必填，选择 11-12 项时 可不填；
            if(this.formData.cashType == '01' || this.formData.cashType == '02' || this.formData.cashType == '03' || this.formData.cashType == '04' || this.formData.cashType == '05'
                 || this.formData.cashType == '06' || this.formData.cashType == '07' || this.formData.cashType == '08' || this.formData.cashType == '09' || this.formData.cashType == '10'){
                this.formData.assetCodeAllowblank = false;
                 this.formData.assetCodeDisabled = false;
            }else{
                 this.formData.assetCodeAllowblank=true;
                this.formData.assetCodeDisabled = true;
                this.$set(this.formData, 'assetCode', '');
            }
            //“资金流动类型”选择 01-08 项时必填，当“资金流动类型”选择 01-08 项以外时，不可填。
            if(this.formData.cashType == '01' || this.formData.cashType == '02' || this.formData.cashType == '03' || this.formData.cashType == '04' || this.formData.cashType == '05'
                 || this.formData.cashType == '06' || this.formData.cashType == '07' || this.formData.cashType == '08'){
                this.formData.methodAssetMeasureAllowblank = false;
                 this.formData.methodAssetMeasureDisabled = false;
            }else{
                 this.formData.methodAssetMeasureAllowblank=true;
                this.formData.methodAssetMeasureDisabled = true;
                this.$set(this.formData, 'methodAssetMeasure', '');
            }
            //单位成交价格（全价）/ 单位成交价格（净价）

             if(this.formData.cashType == '01' || this.formData.cashType == '02'){
               this.formData.unitPriceNetAllowblank = false;
               this.formData.unitPriceFullAllowblank = false;
             }else{
                this.formData.unitPriceNetAllowblank = true;
                this.formData.unitPriceFullAllowblank = true;
             }
             //备注
             if(this.formData.relatedPartyTrans == '99' || this.formData.counterType == '99'){
                this.formData.detailsAllowblank = false;
              }else{
                 this.formData.detailsAllowblank = true;
              }

               if(this.formData.cashType == '01' || this.formData.cashType == '02' || this.formData.cashType == '03' || this.formData.cashType == '04' || this.formData.cashType == '05'
                     || this.formData.cashType == '06'){
                     this.formData.transApproveIdAllowblank = false;
                     this.formData.transApproveNameAllowblank = false;
                     this.formData.traderIdAllowblank = false;
                     this.formData.traderNameAllowblank = false;
                }else{
                     this.formData.transApproveIdAllowblank = true;
                     this.formData.transApproveNameAllowblank = true;
                     this.formData.traderIdAllowblank = true;
                     this.formData.traderNameAllowblank = true;
                }
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


};
</script>
