<template>
  <div class="py-page">
    <div>
      <k-form-search-customize ref="searchFormRef" data-label-width="100px" v-model="queryParam" data-target="trTerminationRegistInfoGrid">
         <k-form-item label="报送日期">
          <k-field-date v-model="queryParamDateRange" :data-allowblank="false" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="产品登记编码">
          <k-field-text v-model="searchParam.prodCode" data-validate-type="text"/>
        </k-form-item>
        <k-form-item label="理财产品实际终止日期" data-label-width="160px">
          <k-field-date v-model="ActualProdTerDate" data-type="daterange" data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd" ></k-field-date>
        </k-form-item>

        <k-form-item label="报送状态" data-label-width="80px">
          <k-field-select v-model="searchParam.registerStatus" data-dict="report_status"></k-field-select>
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn slot="button" ref="uploadBtnRef" data-functype="POPUP" class="btn-custom-plain"
                  data-target="uploadTrTerminationRegistInfoPopup">
            <md-icon>cloud_upload</md-icon>导入
          </k-btn>
          <k-btn slot="button" ref="exportRef" class="btn-custom-plain"  data-functype="EXPORT" data-target="trTerminationRegistInfoGrid" data-export-dict="true"
                  :data-export-name="'产品终止登记管理'" @downSuccess="downSuccess"  :handleBefore="handleBefore" :data-handler="dataHandler">
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
      <k-grid ref="trTerminationRegistInfoGrid" @data-row-select="selectRow" data-fixed="right" data-operate-width="150px" :data-autoload="false" data-action="TrTerminationRegistInfo.findTrTerminationRegistInfos" >
        <k-grid-column data-align="left" data-header="报送日期" data-name="reportDate" data-export="false" data-width="120" ></k-grid-column>
        <k-grid-column data-align="left" data-header="产品登记编码" data-name="prodCode"  data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="发行机构代码" data-name="bankCode"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="理财产品实际终止日期" data-name="actualProdTerDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="银行实际实现收入（元）" data-name="realizedBankIncome"  data-width="170"></k-grid-column>
        <k-grid-column data-align="right" data-header="兑付客户收益（元）" data-name="interestPayment"  data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="兑付客户总金额（元）" data-name="payment"  data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="兑付总份额" data-name="deliveredVol"  data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="本机构托管费（元）" data-name="inCustodianFee"  data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="本机构管理费（元）" data-name="inManageFee"  data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="本机构销售手续费（元）" data-name="inSalesCommision"  data-width="170"></k-grid-column>
        <k-grid-column data-align="right" data-header="本机构其他产品费用（元）" data-name="inOtherProdFee"  data-width="170"></k-grid-column>
        <k-grid-column data-align="right" data-header="其他机构托管费（元）" data-name="otherCustodianFee"  data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="其他机构管理费（元）" data-name="otherManageFee"  data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="其他机构销售手续费（元）" data-name="otherSalesComm"  data-width="170"></k-grid-column>
        <k-grid-column data-align="right" data-header="投资顾问费用（元）" data-name="consultFee"  data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="其他机构其他产品费用（元）" data-name="otherProdFee"  data-width="180"></k-grid-column>
        <k-grid-column data-align="right" data-header="客户实际年化收益率%" data-name="annualReturnClient"  data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="产品实际年化收益率%" data-name="annualReturnProd"  data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="登记流水号" data-name="registerSerno" data-export="false"  data-width="250"></k-grid-column>
        <k-grid-column data-align="left" data-header="登记日期" data-name="registerDate"  data-export="false" data-type="date" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="报送状态" data-name="registerStatus"  data-dict="report_status" data-export="false"  data-width="120"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改终止登记要素" data-functype="POPUP" data-size="mini"
              v-show="scope.row.row.registerStatus != '5'"    data-target="editTrTerminationRegistInfoPopup">
            修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="TrTerminationRegistInfo.deleteTrTerminationRegistInfo" data-size="mini"
             v-show="scope.row.row.registerStatus != '5'"     data-type="danger" data-target="trTerminationRegistInfoGrid" :data-confirm="true" data-descript="删除终止登记要素">
            删除
          </k-btn>
          <!--<k-btn data-functype="PAGE" data-size="mini" class="btn-custom-plain" :data-model="scope.row.row.id"
                 @click="popupEdit(scope.row.row)"  data-descript="终止登记要素登记管理错误详情">
            错误详情
          </k-btn>-->

        </template>
      </k-grid>
    </div>

	<!--    添加终止登记要素弹出框   -->
	<k-popup ref="addTrTerminationRegistInfoPopup" data-title="新增">
    	<k-form ref="addTrTerminationRegistInfoForm" :data-col="2" isFormBodyScreen data-label-width="200px">
			<k-form-item label="产品登记编码">
	        	<k-field-text v-model="formData.prodCode" :data-allowblank="false" :data-max-length="15"/>
	     	</k-form-item>
			<k-form-item label="发行机构代码">
	        	<k-field-text v-model="formData.bankCode" :data-allowblank="false" :data-max-length="6"/>
	     	</k-form-item>
			<k-form-item label="理财产品实际终止日期">
	        	<k-field-date v-model="formData.actualProdTerDate" :data-allowblank="false"   data-date-format="yyyy-MM-dd"  data-value-format="yyyyMMdd"/>
	     	</k-form-item>
        <k-form-item label="银行实际实现收入（元）">
          <k-field-text v-model="formData.realizedBankIncome" :data-allowblank="false" data-validate-type="money" data-integer-length="13" data-digits="2"/>
        </k-form-item>
			<k-form-item label="兑付客户收益（元）">
	        	<k-field-text v-model="formData.interestPayment" :data-allowblank="false" data-validate-type="money" data-integer-length="13" data-digits="2"/>
	     	</k-form-item>
        <k-form-item label="兑付客户总金额（元）">
          <k-field-text v-model="formData.payment" :data-allowblank="false" data-validate-type="money" data-integer-length="15" data-digits="5"  data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
        </k-form-item>
        <k-form-item label="兑付总份额">
          <k-field-text v-model="formData.deliveredVol" :data-allowblank="false" data-validate-type="number" data-integer-length="15" data-digits="5" data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
        </k-form-item>
			<k-form-item label="本机构托管费(元)">
	        	<k-field-text v-model="formData.inCustodianFee" :data-allowblank="false" data-validate-type="money" data-integer-length="13" data-digits="2" data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
	     	</k-form-item>
			<k-form-item label="本机构管理费（元）">
	        	<k-field-text v-model="formData.inManageFee" :data-allowblank="false" data-validate-type="money" data-integer-length="13" data-digits="2" data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
	     	</k-form-item>
			<k-form-item label="本机构销售手续费（元）">
	        	<k-field-text v-model="formData.inSalesCommision" :data-allowblank="false" data-validate-type="money" data-integer-length="13" data-digits="2" data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
	     	</k-form-item>
			<k-form-item label="本机构其他产品费用（元）">
	        	<k-field-text v-model="formData.inOtherProdFee" :data-allowblank="false" data-validate-type="money"  data-integer-length="13" data-digits="2"/>
	     	</k-form-item>
			<k-form-item label="其他机构托管费（元）">
	        	<k-field-text v-model="formData.otherCustodianFee" :data-allowblank="false" data-validate-type="money" data-integer-length="13" data-digits="2" data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
	     	</k-form-item>
			<k-form-item label="其他机构管理费（元）">
	        	<k-field-text v-model="formData.otherManageFee" :data-allowblank="false" data-validate-type="money" data-integer-length="13" data-digits="2" data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
	     	</k-form-item>
			<k-form-item label="其他机构销售手续费（元）">
	        	<k-field-text v-model="formData.otherSalesComm" :data-allowblank="false" data-validate-type="money" data-integer-length="13" data-digits="2" data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
	     	</k-form-item>
			<k-form-item label="投资顾问费用（元）">
	        	<k-field-text v-model="formData.consultFee" :data-allowblank="false" data-validate-type="money" data-integer-length="13" data-digits="2" data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
	     	</k-form-item>
			<k-form-item label="其他机构其他产品费用（元）">
	        	<k-field-text v-model="formData.otherProdFee" :data-allowblank="false" data-validate-type="money" data-integer-length="13" data-digits="2" data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
	     	</k-form-item>
			<k-form-item label="客户实际年化收益率%">
	        	<k-field-text v-model="formData.annualReturnClient" :data-allowblank="false" data-validate-type="money" data-integer-length="1" data-digits="7"/>
	     	</k-form-item>
			<k-form-item label="产品实际年化收益率%">
	        	<k-field-text v-model="formData.annualReturnProd" :data-allowblank="false" data-validate-type="money" data-integer-length="1" data-digits="7"/>
	     	</k-form-item>
	      	<k-form-footer slot="footer" data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="TrTerminationRegistInfo.addTrTerminationRegistInfo" data-from="addTrTerminationRegistInfoForm"
		               :data-model="formData" data-target="trTerminationRegistInfoGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改终止登记要素弹出框   -->
	<k-popup ref="editTrTerminationRegistInfoPopup" data-title="修改" @data-opened="editOpened()">
	  <k-form ref="editTrTerminationRegistInfoForm" :data-col="2" isFormBodyScreen data-label-width="200px">
				<k-form-item label="产品登记编码" :class="[handleItemDiff('prodCode')]">
  	        	<k-field-text v-model="formData.prodCode"  :data-disabled="true" :data-allowblank="false" :data-max-length="15"/>
  	     	</k-form-item>
  			<k-form-item label="发行机构代码" :class="[handleItemDiff('bankCode')]">
  	        	<k-field-text v-model="formData.bankCode"  :data-disabled="true" :data-allowblank="false" :data-max-length="6"/>
  	     	</k-form-item>
  			<k-form-item label="理财产品实际终止日期" :class="[handleItemDiff('actualProdTerDate')]">
  	        	<k-field-date v-model="formData.actualProdTerDate" :data-allowblank="false"   data-date-format="yyyy-MM-dd"  data-value-format="yyyyMMdd"/>
  	     	</k-form-item>
          <k-form-item label="银行实际实现收入（元）" :class="[handleItemDiff('realizedBankIncome')]">
            <k-field-text v-model="formData.realizedBankIncome" :data-allowblank="false" data-validate-type="money" data-integer-length="13" data-digits="2"/>
          </k-form-item>
  			<k-form-item label="兑付客户收益（元）" :class="[handleItemDiff('interestPayment')]">
  	        	<k-field-text v-model="formData.interestPayment" :data-allowblank="false" data-validate-type="money" data-integer-length="13" data-digits="2"/>
  	     	</k-form-item>
          <k-form-item label="兑付客户总金额（元）" :class="[handleItemDiff('payment')]">
            <k-field-text v-model="formData.payment" :data-allowblank="false" data-validate-type="money" data-integer-length="15" data-digits="5" data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
          </k-form-item>
          <k-form-item label="兑付总份额" :class="[handleItemDiff('deliveredVol')]">
            <k-field-text v-model="formData.deliveredVol" :data-allowblank="false" data-validate-type="number" data-integer-length="15" data-digits="5" data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
          </k-form-item>
  			<k-form-item label="本机构托管费(元)" :class="[handleItemDiff('inCustodianFee')]">
  	        	<k-field-text v-model="formData.inCustodianFee" :data-allowblank="false" data-validate-type="money" data-integer-length="13" data-digits="2" data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
  	     	</k-form-item>
  			<k-form-item label="本机构管理费（元）" :class="[handleItemDiff('inManageFee')]">
  	        	<k-field-text v-model="formData.inManageFee" :data-allowblank="false" data-validate-type="money" data-integer-length="13" data-digits="2" data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
  	     	</k-form-item>
  			<k-form-item label="本机构销售手续费（元）" :class="[handleItemDiff('inSalesCommision')]">
  	        	<k-field-text v-model="formData.inSalesCommision" :data-allowblank="false" data-validate-type="money" data-integer-length="13" data-digits="2" data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
  	     	</k-form-item>
  			<k-form-item label="本机构其他产品费用（元）" :class="[handleItemDiff('inOtherProdFee')]">
  	        	<k-field-text v-model="formData.inOtherProdFee" :data-allowblank="false" data-validate-type="money"  data-integer-length="13" data-digits="2"/>
  	     	</k-form-item>
  			<k-form-item label="其他机构托管费（元）" :class="[handleItemDiff('otherCustodianFee')]">
  	        	<k-field-text v-model="formData.otherCustodianFee" :data-allowblank="false" data-validate-type="money" data-integer-length="13" data-digits="2" data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
  	     	</k-form-item>
  			<k-form-item label="其他机构管理费（元）" :class="[handleItemDiff('otherManageFee')]">
  	        	<k-field-text v-model="formData.otherManageFee" :data-allowblank="false" data-validate-type="money" data-integer-length="13" data-digits="2" data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
  	     	</k-form-item>
  			<k-form-item label="其他机构销售手续费（元）" :class="[handleItemDiff('otherSalesComm')]">
  	        	<k-field-text v-model="formData.otherSalesComm" :data-allowblank="false" data-validate-type="money" data-integer-length="13" data-digits="2" data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
  	     	</k-form-item>
  			<k-form-item label="投资顾问费用（元）" :class="[handleItemDiff('consultFee')]">
  	        	<k-field-text v-model="formData.consultFee" :data-allowblank="false" data-validate-type="money" data-integer-length="13" data-digits="2" data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
  	     	</k-form-item>
  			<k-form-item label="其他机构其他产品费用（元）" :class="[handleItemDiff('otherProdFee')]">
  	        	<k-field-text v-model="formData.otherProdFee" :data-allowblank="false" data-validate-type="money" data-integer-length="13" data-digits="2" data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
  	     	</k-form-item>
  			<k-form-item label="客户实际年化收益率%" :class="[handleItemDiff('annualReturnClient')]">
  	        	<k-field-text v-model="formData.annualReturnClient" :data-allowblank="false" data-validate-type="money" data-integer-length="3" data-digits="5"/>
  	     	</k-form-item>
  			<k-form-item label="产品实际年化收益率%" :class="[handleItemDiff('annualReturnProd')]">
  	        	<k-field-text v-model="formData.annualReturnProd" :data-allowblank="false" data-validate-type="money" data-integer-length="3" data-digits="5"/>
  	     	</k-form-item>
	    <k-form-footer slot="footer" data-align="center">
	      <k-btn class="btn-custom-primary"  :data-handler="sumbit_edit" ref="sumbitedit" data-from="editTrTerminationRegistInfoForm"
	        :data-model="formData" data-target="trTerminationRegistInfoGrid" :handle-before="handleBeforeUpdate">
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
                                data-upload-url="/upload/server/PmsApp/chinaBondSubmit/TrTerminationRegistInfo/comn-upload.json">
          </k-field-excel-upload>
        </k-form-item>
        <k-form-footer slot="footer" data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="trTerminationRegistInfoGrid" ref="submitBtn"
                 :data-auto-upload="false" data-from="addForm" :data-handler="submitUploadParam">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

      <k-popup ref="uploadTrTerminationRegistInfoPopup" title="报送数据导入" @data-opened="uploadOpened()">
                 <k-form ref="addForm" data-ui="element">
                    <k-form-item label="报送日期">
                      <k-field-date v-model="formData.reportDate" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd" :data-allowblank="false"/>
                    </k-form-item>
                   <k-form-item label="报送数据导入" data-ui="element" data-input-width="500px">
                     <k-field-excel-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit='1'
                         data-accept=".xlsx,.xls"
                         :data-error="onSubmitError" :data-success="onSubmitSuccess"
                         :data-auto-upload="false"
                         data-upload-url="upload/server/RptApp/reportManage/terminationRegistImport.json">
                     </k-field-excel-upload>
                   </k-form-item>
                   <k-form-footer data-align="center">
                     <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="trTerminationRegistInfoGrid" ref="submitBtn"
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
  name: "TrTerminationRegistInfo",
  mixins: [ProdMixin],
  data() {
    return {
      formData: {},
      formDataCopy: {},
      searchParam: {},
      selectRowData: {},
      queryParamDateRange: [],
      ActualProdTerDate: [],
      infoPop: {},
      auditInfoPopupData: {
        tableId: 'app_termination_regist_info',
        tableName: '产品终止登记管理'
      },
      abnormalAction: "TrTerminationRegistInfo.getAbnormalData",
      updateStatusAction: "TrTerminationRegistInfo.updateTrTerminationRegistInfoStatus"
    };
  },
  computed: {
    queryParam() {
      return {
        'beginDate': this.queryParamDateRange ? this.queryParamDateRange[0] : null,
        'queryDate': this.queryParamDateRange ? this.queryParamDateRange[1] : null,
        'prodCode': this.searchParam.prodCode,
        'registerStatus': this.searchParam.registerStatus,
        'actualStartDate': this.ActualProdTerDate ? this.ActualProdTerDate[0] : null,
        'actualEndDate': this.ActualProdTerDate ? this.ActualProdTerDate[1] : null,
      }
    }
  },
    methods: {
      editOpened(){
            this.formData.oldData=Tools.json2str(this.formData);
      },
      handleBeforeUpdate() {
        if (this.formNoChangeCb()) {
          this.$refs.editTrTerminationRegistInfoPopup.close();
          return false
        }
        return true
      },
		dataHandler() {
			if (this.queryParamDateRange == null) {
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
      if(this.$refs.editTrTerminationRegistInfoForm.validate()){
           this.httpUtil.query({
                   url: 'server/json/RptApp/audit/checkTrTerminationRegistInfo.json',
                   params:  this.formData
                            }).then(res => {
                              if(res.success) {
                               this.httpUtil.comnUpdate({
                                        action: 'TrTerminationRegistInfo.updateTrTerminationRegistInfo',
                                        params:  this.formData
                                         }).then(res => {
                                          if(res.success) {
                                          this.$refs.editTrTerminationRegistInfoPopup.close();
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
      let flag = '0';
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
                     this.$refs.trTerminationRegistInfoGrid.load(this.queryParam);
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
                 this.$refs.trTerminationRegistInfoGrid.load(this.queryParam);
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
          prodCode: this.searchParam.prodCode,
          registerStatus: this.searchParam.registerStatus,
          actualStartDate: this.ActualProdTerDate ? this.ActualProdTerDate[0] : null,
          actualEndDate: this.ActualProdTerDate ? this.ActualProdTerDate[1] : null,
        };
      },
      popupEdit(row){
        let pathUrl = '/main/zz/errorInfo/TerminationRgInfoErr';
        this.$router.push({
          path: pathUrl,
          query: {
            registerSerno: row.registerSerno,
          },
        });
      },
      onSubmitSuccess() {
        this.$refs.uploadRef.doReset();
        this.$refs.addForm.reset();
        this.$refs.addPopup.close();
        this.$refs.uploadTrTerminationRegistInfoPopup.close();
        this.$refs.trTerminationRegistInfoGrid.load(this.queryParam);
        this.formData.reportDate='';
      },
      onSubmitError() {
        this.$refs.uploadRef.doReset();
        this.$refs.submitBtn.setIconStyle(1, [])
      },
      submitUploadParam() {
        //文件上传校验
        var validate = this.$refs.addForm.validate();
        if (validate) {
        let formData = {reportDate: this.formData.reportDate};
          let temp = document.getElementsByClassName('upload-demo');
          let lis = temp[0].childNodes[1].childNodes.length;
          if (lis > 0) {
            this.$refs.uploadRef.upload(formData);
            this.showSubmitBtn = true;
          } else {
            Tools.alert("上传文件不能为空!", "danger");
            this.showSubmitBtn = true;
            return false;
          }
        }
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
    // 复核弹窗关闭
    closeAuditFunc(val) {
      console.log(val);
      this.$nextTick(() => {
        this.$refs.trTerminationRegistInfoGrid.load();
      })
      this.$refs.auditInfoPopup.close();
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
      this.queryParamDateRange[0] = defaultDate;
      this.queryParamDateRange[1] = defaultDate;
      this.$set(this.queryParam, "queryParamDateRange", defaultDate);

    },
};
</script>
