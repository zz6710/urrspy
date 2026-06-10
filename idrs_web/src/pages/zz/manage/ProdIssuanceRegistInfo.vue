<template>
  <div class="py-page">
    <div>
      <k-form-search-customize ref="searchFormRef" data-model-name="ProdIssuanceRegistInfo" data-target="prodIssuanceRegistInfoGrid" v-model="queryParam"  >
        <k-form-item label="募集起始日期">
          <k-field-date v-model="queryParamDateRange"  data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="理财产品代码">
          <k-field-text v-model="searchParam.prodIdentCode"/>
        </k-form-item>
        <k-form-item label="产品登记编码">
          <k-field-text v-model="searchParam.prodCode"/>
        </k-form-item>
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
          <k-btn slot="button"  data-functype="POPUP" class="btn-custom-plain" data-target="uploadProdIssuanceRegistInfoPopup">
            <md-icon>cloud_upload</md-icon>导入</k-btn>
          <k-btn slot="button" ref="exportRef" class="btn-custom-plain"  data-functype="EXPORT" data-target="prodIssuanceRegistInfoGrid" data-export-dict="true"
                  :data-export-name="'产品发行登记信息管理'" :handleBefore="handleBefore" @downSuccess="downSuccess" :data-handler="handleExport" data-export-form="searchFormRef">
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
      <k-grid ref="prodIssuanceRegistInfoGrid"
              @data-row-select="selectRow" data-fixed="right" data-operate-width="150px"
              @init="(grid)=>{this.$kgrid = grid}"
              :data-autoload="false"
              data-action="ProdIssuanceRegistInfo.findProdIssuanceRegistInfos">
        <k-grid-column data-align="left" data-header="报送日期" data-name="reportDate" data-export="false" data-width="80" ></k-grid-column>
        <k-grid-column data-align="left" data-header="版本号" data-name="sysDataVersion" data-export="false" data-width="60" ></k-grid-column>
        <k-grid-column data-align="left" data-header="产品登记编码" data-name="prodCode"
                       data-width="140"></k-grid-column>
        <k-grid-column data-align="left" data-header="理财产品代码" data-name="prodIdentCode"
                       data-width="140"></k-grid-column>
        <k-grid-column data-align="left" data-header="发行机构代码" data-name="bankCode"
                       data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="募集起始日期" data-name="subscriptionStartDate"
                       data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="募集结束日期" data-name="subscriptionEndDate"
                       data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品起始日期" data-name="prodValueDate"
                       data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品终止日期" data-name="prodMaturityDate"
                       data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="管理方式" data-name="managementMethod"
                       data-dict="subm_managementMethod" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="是否为结构化（分级）产品" data-name="structuredProd"
                       data-dict="subm_isTrue" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="分级比例" data-name="clsfSto" data-dict="subm_isTrue"
                       data-width="100"></k-grid-column>
        <k-grid-column data-align="right" data-header="业绩比较基准上限%" data-name="upLimitPerRate" data-digits="5"
                       data-width="100"></k-grid-column>
        <k-grid-column data-align="right" data-header="业绩比较基准下限%" data-name="lowLimitPerRate" data-digits="5"
                       data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="业绩比较基准说明" data-name="detailsPerRate"
                       data-width="320"></k-grid-column>
        <k-grid-column data-align="left" data-header="开放模式" data-name="openingMode" data-dict="subm_open_mod"
                       data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="规律开放周期" data-name="regularOpenPeriod"
                       data-dict="subm_t8_open_calendar" data-width="100"></k-grid-column>
        <k-grid-column data-align="right" data-header="其他规律开放周期 (天)" data-name="otherOpenPeriod"
                       data-width="100"></k-grid-column>
        <k-grid-column data-align="right" data-header="定期开放周期(天)" data-name="regularOpenPeriodDay"
                       data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="无规律开放说明" data-name="disorderOpenPeriod"
                       data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="首次开放周期起始日" data-name="firstOpenDay"
                       data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="节假日是否开放" data-name="holidayOpenType"
                       data-dict="subm_isTrue" data-width="100"></k-grid-column>
        <k-grid-column data-align="right" data-header="平均开放次数（年化）" data-name="averageOpenNo" data-digits="2"
                       data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="开放期业务" data-name="busiOpenPeriod"
                       data-dict="subm_t8_open_control" data-width="130"></k-grid-column>
        <k-grid-column data-align="left" data-header="开放期业务说明" data-name="detailsBusiOpPeriod"
                       data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="登记流水号" data-name="registerSerno" data-width="250"
                       data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="登记日期" data-name="registerDate"
                       data-width="100" data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="新增日期" data-name="createDate"
                       data-export="false" data-width="80" ></k-grid-column>
        <k-grid-column data-align="left" data-header="报送状态" data-name="registerStatus"
                       data-dict="report_status" data-width="100" data-export="false"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改产品发行登记信息管理" data-functype="POPUP" data-size="mini"
           v-show="scope.row.row.registerStatus != '5'"       data-target="editProdIssuanceRegistInfoPopup">
            修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT"
                 data-action="ProdIssuanceRegistInfo.deleteProdIssuanceRegistInfo" data-size="mini"
                 data-type="danger" data-target="prodIssuanceRegistInfoGrid" :data-confirm="true"
          v-show="scope.row.row.registerStatus != '5'"        data-descript="删除产品发行登记信息管理">
            删除
          </k-btn>
<!--          <k-btn data-functype="PAGE" data-size="mini" class="btn-custom-plain" :data-model="scope.row.row.id"
                 @click="popupEdit(scope.row.row)" data-descript="产品发行登记信息管理错误详情">
            错误详情
          </k-btn>-->
        </template>
      </k-grid>
    </div>

	<!--    添加产品发行登记信息管理弹出框   -->
	<k-popup ref="addProdIssuanceRegistInfoPopup" data-title="新增">
    	<k-form ref="addProdIssuanceRegistInfoForm" :data-col="2" isFormBodyScreen data-label-width="200px">
        <k-form-item label="理财产品代码">
          <k-field-text v-model="formData.prodIdentCode" :data-allowblank="false" :data-max-length="100"/>
        </k-form-item>
        <k-form-item label="产品登记编码">
	        	<k-field-text v-model="formData.prodCode" :data-allowblank="false" :data-max-length="15"/>
	     	</k-form-item>
        <k-form-item label="发行机构代码">
	        	<k-field-text v-model="formData.bankCode" :data-allowblank="false" :data-max-length="6"/>
	     	</k-form-item>
			<k-form-item label="募集起始日期">
	        	<k-field-date v-model="formData.subscriptionStartDate" :data-allowblank="false" dataDateFormat="yyyyMMdd"/>
	     	</k-form-item>
			<k-form-item label="募集结束日期">
	        	<k-field-date v-model="formData.subscriptionEndDate" :data-allowblank="false" dataDateFormat="yyyyMMdd"/>
	     	</k-form-item>
			<k-form-item label="产品起始日期">
	        	<k-field-date v-model="formData.prodValueDate" :data-allowblank="false" dataDateFormat="yyyyMMdd"/>
	     	</k-form-item>
			<k-form-item label="产品终止日期">
	        	<k-field-date v-model="formData.prodMaturityDate" :data-allowblank="false" dataDateFormat="yyyyMMdd"/>
	     	</k-form-item>
			<k-form-item label="管理方式">
	        	<k-field-select v-model="formData.managementMethod" :data-allowblank="false" data-dict="subm_managementMethod"/>
	     	</k-form-item>
			<k-form-item label="是否为结构化（分级）产品">
	        	<k-field-select v-model="formData.structuredProd" :data-allowblank="false" data-dict="subm_isTrue"  @data-on-change="dataOnChange"/>
	     	</k-form-item>
			<k-form-item label="分级比例">
	        	<k-field-text v-model="formData.clsfSto" data-digits="5" :data-allowblank="formData.clsfStoAllowblank"  :data-disabled="formData.clsfStoDisabled" data-integer-length="3" data-validate-type="number" data-type="number"/>
	     	</k-form-item>
        <k-form-item label="业绩比较基准上限%">
          <k-field-text v-model="formData.upLimitPerRate" data-digits="7" data-integer-length="1" :data-max-length="9"
                        data-validate-type="money" data-type="money"/>
        </k-form-item>
        <k-form-item label="业绩比较基准下限%">
          <k-field-text v-model="formData.lowLimitPerRate" data-digits="7" data-integer-length="1" :data-max-length="9"
                        data-validate-type="money" data-type="money"/>
        </k-form-item>
			<k-form-item label="业绩比较基准说明">
	        	<k-field-text v-model="formData.detailsPerRate" :data-max-length="400"/>
	     	</k-form-item>
			<k-form-item label="开放模式">
	        	<k-field-select v-model="formData.openingMode" data-dict="subm_open_mod"/>
	     	</k-form-item>
        <k-form-item label="规律开放周期">
          <k-field-select v-model="formData.regularOpenPeriod" data-dict="subm_t8_open_calendar"/>
        </k-form-item>
        <k-form-item label="其他规律开放周期(天)">
          <k-field-text v-model="formData.otherOpenPeriod" data-validate-type="number" :data-max-length="4"/>
        </k-form-item>
        <k-form-item label="定期开放周期（天）">
          <k-field-text v-model="formData.regularOpenPeriodDay" data-validate-type="number" data-type="number" :data-max-length="4"/>
        </k-form-item>
        <k-form-item label="无规律开放说明">
          <k-field-text v-model="formData.disorderOpenPeriod" :data-max-length="256"/>
        </k-form-item>
        <k-form-item label="首次开放周期起始日">
          <k-field-date v-model="formData.firstOpenDay"  dataDateFormat="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="节假日是否开放">
          <k-field-select v-model="formData.holidayOpenType" data-dict="subm_isTrue"/>
        </k-form-item>
        <k-form-item label="平均开放次数（年化）">
          <k-field-text v-model="formData.averageOpenNo" data-digits="2" data-integer-length="3"  data-validate-type="number" data-type="number"/>
        </k-form-item>
        <k-form-item label="开放期业务">
          <k-field-select v-model="formData.busiOpenPeriod" data-dict="subm_t8_open_control"/>
        </k-form-item>
        <k-form-item label="开放期业务说明">
          <k-field-text v-model="formData.detailsBusiOpPeriod"  :data-max-length="256"/>
        </k-form-item>

	      	<k-form-footer slot="footer" data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ProdIssuanceRegistInfo.addProdIssuanceRegistInfo" data-from="addProdIssuanceRegistInfoForm"
		               :data-model="formData" data-target="prodIssuanceRegistInfoGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改产品发行登记信息管理弹出框   -->
	<k-popup ref="editProdIssuanceRegistInfoPopup" data-title="修改" @data-opened="editOpened()">
	  <k-form ref="editProdIssuanceRegistInfoForm" :data-col="2" isFormBodyScreen data-label-width="200px">
      <k-form-item label="理财产品代码" :class="[handleItemDiff('prodIdentCode')]">
        <k-field-text v-model="formData.prodIdentCode" :data-disabled="true" :data-allowblank="false" :data-max-length="100"/>
      </k-form-item>
      <k-form-item label="产品登记编码" :class="[handleItemDiff('prodCode')]">
        <k-field-text v-model="formData.prodCode" :data-disabled="true" :data-allowblank="false" :data-max-length="15"/>
      </k-form-item>
      <k-form-item label="发行机构代码" :class="[handleItemDiff('bankCode')]">
        <k-field-text v-model="formData.bankCode" :data-disabled="true" :data-allowblank="false" :data-max-length="6"/>
      </k-form-item>
      <k-form-item label="募集起始日期" :class="[handleItemDiff('subscriptionStartDate')]">
        <k-field-date v-model="formData.subscriptionStartDate" :data-allowblank="false" dataDateFormat="yyyyMMdd"/>
      </k-form-item>
      <k-form-item label="募集结束日期" :class="[handleItemDiff('subscriptionEndDate')]">
        <k-field-date v-model="formData.subscriptionEndDate" :data-allowblank="false" dataDateFormat="yyyyMMdd"/>
      </k-form-item>
      <k-form-item label="产品起始日期" :class="[handleItemDiff('prodValueDate')]">
        <k-field-date v-model="formData.prodValueDate" :data-allowblank="false" dataDateFormat="yyyyMMdd"/>
      </k-form-item>
      <k-form-item label="产品终止日期" :class="[handleItemDiff('prodMaturityDate')]">
        <k-field-date v-model="formData.prodMaturityDate" :data-allowblank="false" dataDateFormat="yyyyMMdd"/>
      </k-form-item>
      <k-form-item label="管理方式" :class="[handleItemDiff('managementMethod')]">
        <k-field-select v-model="formData.managementMethod" :data-allowblank="false" data-dict="subm_managementMethod"/>
      </k-form-item>
      <k-form-item label="是否为结构化（分级）产品" :class="[handleItemDiff('structuredProd')]">
        <k-field-select v-model="formData.structuredProd" :data-allowblank="false" data-dict="subm_isTrue"   @data-on-change="dataOnChange"/>
      </k-form-item>
      	<k-form-item label="分级比例" :class="[handleItemDiff('clsfSto')]">
          <k-field-text v-model="formData.clsfSto" data-digits="5" :data-allowblank="formData.clsfStoAllowblank"  :data-disabled="formData.clsfStoDisabled" data-integer-length="3" data-validate-type="number" data-type="number"/>
      </k-form-item>
      <k-form-item label="业绩比较基准上限%" :class="[handleItemDiff('upLimitPerRate')]">
        <k-field-text v-model="formData.upLimitPerRate" data-digits="7" data-integer-length="1"
                      data-validate-type="money" data-type="money"/>
      </k-form-item>
      <k-form-item label="业绩比较基准下限%" :class="[handleItemDiff('lowLimitPerRate')]">
        <k-field-text v-model="formData.lowLimitPerRate" data-digits="7" data-integer-length="1"
                      data-validate-type="money" data-type="money"/>
      </k-form-item>
      <k-form-item label="业绩比较基准说明" :class="[handleItemDiff('detailsPerRate')]">
        <k-field-text v-model="formData.detailsPerRate" :data-max-length="400"/>
      </k-form-item>
      <k-form-item label="开放模式" :class="[handleItemDiff('openingMode')]">
        <k-field-select v-model="formData.openingMode" data-dict="subm_open_mod"/>
      </k-form-item>
      <k-form-item label="规律开放周期" :class="[handleItemDiff('regularOpenPeriod')]">
        <k-field-select v-model="formData.regularOpenPeriod" data-dict="subm_t8_open_calendar"/>
      </k-form-item>
      <k-form-item label="其他规律开放周期(天)" :class="[handleItemDiff('otherOpenPeriod')]">
        <k-field-text v-model="formData.otherOpenPeriod" data-validate-type="number"  :data-max-length="4"/>
      </k-form-item>
       <k-form-item label="定期开放周期（天）" :class="[handleItemDiff('regularOpenPeriodDay')]">
        <k-field-text v-model="formData.regularOpenPeriodDay" data-validate-type="number" data-type="number" :data-max-length="4"  data-regx-text="请输入大于等于0的数值" data-min-value="0" />
      </k-form-item>
      <k-form-item label="无规律开放说明" :class="[handleItemDiff('disorderOpenPeriod')]">
        <k-field-text v-model="formData.disorderOpenPeriod" :data-max-length="256"/>
      </k-form-item>
      <k-form-item label="首次开放周期起始日" :class="[handleItemDiff('firstOpenDay')]">
        <k-field-date v-model="formData.firstOpenDay"  dataDateFormat="yyyyMMdd"/>
      </k-form-item>
      <k-form-item label="节假日是否开放" :class="[handleItemDiff('holidayOpenType')]">
        <k-field-select v-model="formData.holidayOpenType" data-dict="subm_isTrue"/>
      </k-form-item>
      <k-form-item label="平均开放次数（年化）" :class="[handleItemDiff('averageOpenNo')]">
        <k-field-text v-model="formData.averageOpenNo" data-digits="2" data-integer-length="3"  data-validate-type="number" data-type="number"  data-regx-text="请输入大于0的数值" data-min-value="(0" data-max-value="366" />
      </k-form-item>
      <k-form-item label="开放期业务" :class="[handleItemDiff('busiOpenPeriod')]">
        <k-field-select v-model="formData.busiOpenPeriod" data-dict="subm_t8_open_control"/>
      </k-form-item>
      <k-form-item label="开放期业务说明" :class="[handleItemDiff('detailsBusiOpPeriod')]">
        <k-field-text v-model="formData.detailsBusiOpPeriod"  :data-max-length="256" />
      </k-form-item>
	    <k-form-footer slot="footer" data-align="center">
	      <k-btn class="btn-custom-primary"  ref="sumbitedit" :data-handler="sumbit_edit" data-from="editProdIssuanceRegistInfoForm"
	        :data-model="formData" data-target="prodIssuanceRegistInfoGrid"  :handle-before="handleBeforeUpdate">
	        <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
	      </k-btn>
	      <k-btn class="btn-custom-plain" data-functype="CLOSE">
	        <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	    </k-form-footer>
	  </k-form>
	</k-popup>

    <k-popup ref="uploadProdIssuanceRegistInfoPopup" title="报送数据导入" @data-opened="uploadOpened()">
        <k-form ref="addForm" data-ui="element">
        <k-form-item label="报送日期">
                       <k-field-date v-model="formData.reportDate" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd" :data-allowblank="false"/>
           </k-form-item>
          <k-form-item label="报送数据导入" data-ui="element" data-input-width="500px">

            <k-field-excel-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit='1'
                data-accept=".xlsx,.xls"
                :data-error="onSubmitError" :data-success="onSubmitSuccess"
                :data-auto-upload="false"
                data-upload-url="upload/server/RptApp/reportManage/prodIssuanceRegistImport.json">
            </k-field-excel-upload>
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="prodIssuanceRegistInfoGrid" ref="submitBtn"
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
import KFieldCheckboxParam from "@/pages/design/components/param/KFieldCheckboxParam.vue";
import Tools from "@/utils/tools";
import ProdMixin from "@/pages/zz/manage/mixins/prodMixin.js";

export default {
  name: "ProdIssuanceRegistInfo",
  components: {KFieldCheckboxParam},
  mixins: [ProdMixin],
  data() {
    return {
      formData: {
        prodName:'',
        prodIdentCode:'',
        registerStatus:'',
        reportDate:''
      },
      formDataCopy: {},
      selectRowData: {},
      searchParam: {},
      infoPop: {},
      auditInfoPopupData: {
        tableId: 'app_prod_issuance_regist_info',
        tableName: '产品发行登记信息管理'
      },
      queryParamDateRange: [],
      queryParamCreateDateRange: [],
      queryParamReportDateRange: [],
      abnormalAction: "ProdIssuanceRegistInfo.getAbnormalData",
      updateStatusAction: "ProdIssuanceRegistInfo.updateProdIssuanceRegistInfoStatus",
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
        'prodIdentCode': this.searchParam.prodIdentCode,
        'prodCode': this.searchParam.prodCode,
        'registerStatus': this.searchParam.registerStatus,
      }
    }
  },
  methods: {
    handleBeforeUpdate() {
      if (this.formNoChangeCb()) {
        this.$refs.editProdIssuanceRegistInfoPopup.close();
        return false
      }
      return true
    },
    sumbit_edit(){
                  this.$refs.sumbitedit.setIconStyle(0,[]);
                  if(this.$refs.editProdIssuanceRegistInfoForm.validate()){
                       this.httpUtil.query({
                               url: 'server/json/RptApp/audit/checkProdIssuanceRegistInfo.json',
                               params:  this.formData
                                        }).then(res => {
                                          if(res.success) {
                                           this.httpUtil.comnUpdate({
                                                    action: 'ProdIssuanceRegistInfo.updateProdIssuanceRegistInfo',
                                                    params:  this.formData
                                                     }).then(res => {
                                                      if(res.success) {
                                                      this.$refs.editProdIssuanceRegistInfoPopup.close();
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
                     this.$refs.prodIssuanceRegistInfoGrid.load(this.queryParam);
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
                 this.$refs.prodIssuanceRegistInfoGrid.load(this.queryParam);
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
        prodIdentCode: this.searchParam.prodIdentCode,
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
      let tableData = [{systemCode:11,planProductDate: null}];
      let testValidator = async (_rule, value) => {
        console.log(value, tabledata, "testValidator");
        if (tabledata[0].systemCode != null) {
          if(tabledata[0].planProductDate == null){
            return Promise.reject(new Error('计划时间不能为空'));
          } else {
            return Promise.resolve(true);
          }
        }
      }
      this.$refs.uploadRef.doReset();
      this.$refs.addForm.reset();
      this.$refs.uploadProdIssuanceRegistInfoPopup.close();
      this.$refs.prodIssuanceRegistInfoGrid.load(this.queryParam);
    },
    onSubmitError() {
      this.$refs.uploadRef.doReset();
      this.$refs.submitBtn.setIconStyle(1, [])
    },
    popupEdit(row) {
      let pathUrl = '/main/zz/errorInfo/ProdIssRgInfoErr';
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
      this.formData.reportDate = ''
    },
    editOpened(){
      this.dataOnChange();
      this.formDataCopy = Object.assign({}, this.formData)
      this.formData.oldData=Tools.json2str(this.formData);
    },
    dataOnChange(){
      if(this.formData.structuredProd == '01'){
        this.formData.clsfStoAllowblank=false;
        this.formData.clsfStoDisabled=false;
      }else{
        this.$set(this.formData, 'clsfSto', '');
        this.formData.clsfStoAllowblank=true;
        this.formData.clsfStoDisabled=true;
      }
    },

    // 复核弹窗
    auditPopup() {
      this.infoPop = this.auditInfoPopupData;
      this.$refs.auditInfoPopup.popup();
    },
    //检查查询条件框是否为空
    checkConditionParams: function () {
      let flag = false;
      if (this.prodSearchParam.prodName != null && this.prodSearchParam.prodName != '' && this.prodSearchParam.prodName != undefined) {
        flag = true;
      }
      if (this.prodSearchParam.disclosureType != null && this.prodSearchParam.disclosureType != '' && this.prodSearchParam.disclosureType != undefined) {
        flag = true;
      }
      if (this.prodSearchParam.disclosureSonType != null && this.prodSearchParam.disclosureSonType != '' && this.prodSearchParam.disclosureSonType != undefined) {
        flag = true;
      }
      if (this.prodSearchParam.prodBaseDate != null && this.prodSearchParam.prodBaseDate != '' && this.prodSearchParam.prodBaseDate != undefined) {
        flag = true;
      }
      if (this.prodSearchParam.prodCode != null && this.prodSearchParam.prodCode != '' && this.prodSearchParam.prodCode != undefined) {
        flag = true;
      }
      if (this.prodSearchParam.disclosureStatus != null && this.prodSearchParam.disclosureStatus != '' && this.prodSearchParam.disclosureStatus != undefined) {
        flag = true;
      }
      return flag;
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
};
</script>
