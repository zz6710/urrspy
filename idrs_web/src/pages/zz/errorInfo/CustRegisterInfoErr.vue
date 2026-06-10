<template>
  <div class="py-page">
     <div>
         <k-form-search-customize data-model-name="CustRegisterInfoErr" data-target="CustRegisterInfoErrGrid" data-label-width="80px" v-model = "searchParam">
           <k-form-item label="导入日期">
             <k-field-date v-model="BreathDay" data-type="daterange" data-date-format="yyyyMM"
                           data-value-format="yyyyMMdd"/>
           </k-form-item>
           <k-form-item label="登记流水号">
             <k-field-text v-model="searchParam.registerSerno"/>
           </k-form-item>
         </k-form-search-customize>
       </div>
    <div class="py-page-container">
      <k-grid ref="CustRegisterInfoErrGrid" @data-row-select="selectRow" data-operate-column="false" data-action="CustRegisterInfoErr.findCustRegisterInfos" >
		<k-grid-column data-header="登记流水号" data-name="registerSerno"></k-grid-column>
		<k-grid-column data-header="登记银行代码错误描述" data-name="bankCodeDesc"></k-grid-column>
		<k-grid-column data-header="该权益人是否属于本行错误描述" data-name="isBelongDesc"></k-grid-column>
		<k-grid-column data-header="权益人所属银行名称错误描述" data-name="issBankNameDesc"></k-grid-column>
		<k-grid-column data-header="权益人所属银行代码错误描述" data-name="issBankCodeDesc"></k-grid-column>
		<k-grid-column data-header="权益人境内外标识错误描述" data-name="inOutSignDesc"></k-grid-column>
		<k-grid-column data-header="权益人所属国家或地区错误描述" data-name="issCountryDesc"></k-grid-column>
		<k-grid-column data-header="数据类型错误描述" data-name="dataTypeDesc"></k-grid-column>
		<k-grid-column data-header="原识别标识错误描述" data-name="oriCustNoDesc"></k-grid-column>
		<k-grid-column data-header="识别标识错误描述" data-name="custNoDesc"></k-grid-column>
		<k-grid-column data-header="权益人类别错误描述" data-name="custTypeDesc"></k-grid-column>
		<k-grid-column data-header="个人证件类别错误描述" data-name="personalIdTypeDesc"></k-grid-column>
		<k-grid-column data-header="机构证件类别错误描述" data-name="organizationIdTypeDesc"></k-grid-column>
		<k-grid-column data-header="其他证件名称错误描述" data-name="otherIdNameDesc"></k-grid-column>
		<k-grid-column data-header="证件号码错误描述" data-name="idCodeDesc"></k-grid-column>
		<k-grid-column data-header="SPV资金托管账户开户行错误描述" data-name="spvOpenBankDesc"></k-grid-column>
		<k-grid-column data-header="其他资金托管账户开户行错误描述" data-name="otherOpenBankDesc"></k-grid-column>
		<k-grid-column data-header="权益人名称错误描述" data-name="custNameDesc"></k-grid-column>
		<k-grid-column data-header="性别错误描述" data-name="sexDesc"></k-grid-column>
		<k-grid-column data-header="风险偏好错误描述" data-name="riskLevelDesc"></k-grid-column>
		<k-grid-column data-header="手机号码错误描述" data-name="mobleDesc"></k-grid-column>
		<k-grid-column data-header="固定电话错误描述" data-name="telPhoneDesc"></k-grid-column>
		<k-grid-column data-header="电子邮箱错误描述" data-name="emailDesc"></k-grid-column>
		<k-grid-column data-header="导入日期" data-name="impDate"></k-grid-column>
		<k-grid-column data-header="降序备注" data-name="remarkDesc"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改投资者登记错误信息" data-functype="POPUP" data-size="mini"
            data-target="editCustRegisterInfoErrPopup">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="CustRegisterInfoErr.deleteCustRegisterInfo" data-size="mini"
               data-type="danger" data-target="{lowHeadModel}Grid" :data-confirm="true" data-descript="删除投资者登记错误信息">
          	<md-icon>close</md-icon>
    	  </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加投资者登记错误信息弹出框   -->
	<k-popup ref="addCustRegisterInfoErrPopup" data-title="新增">
    	<k-form ref="addCustRegisterInfoErrForm" :data-col="2">
			<k-form-item label="登记流水号">
	        	<k-field-text v-model="formData.registerSerno"/>
	     	</k-form-item>
			<k-form-item label="登记银行代码错误描述">
	        	<k-field-text v-model="formData.bankCodeDesc"/>
	     	</k-form-item>
			<k-form-item label="该权益人是否属于本行错误描述">
	        	<k-field-text v-model="formData.isBelongDesc"/>
	     	</k-form-item>
			<k-form-item label="权益人所属银行名称错误描述">
	        	<k-field-text v-model="formData.issBankNameDesc"/>
	     	</k-form-item>
			<k-form-item label="权益人所属银行代码错误描述">
	        	<k-field-text v-model="formData.issBankCodeDesc"/>
	     	</k-form-item>
			<k-form-item label="权益人境内外标识错误描述">
	        	<k-field-text v-model="formData.inOutSignDesc"/>
	     	</k-form-item>
			<k-form-item label="权益人所属国家或地区错误描述">
	        	<k-field-text v-model="formData.issCountryDesc"/>
	     	</k-form-item>
			<k-form-item label="数据类型错误描述">
	        	<k-field-text v-model="formData.dataTypeDesc"/>
	     	</k-form-item>
			<k-form-item label="原识别标识错误描述">
	        	<k-field-text v-model="formData.oriCustNoDesc"/>
	     	</k-form-item>
			<k-form-item label="识别标识错误描述">
	        	<k-field-text v-model="formData.custNoDesc"/>
	     	</k-form-item>
			<k-form-item label="权益人类别错误描述">
	        	<k-field-text v-model="formData.custTypeDesc"/>
	     	</k-form-item>
			<k-form-item label="个人证件类别错误描述">
	        	<k-field-text v-model="formData.personalIdTypeDesc"/>
	     	</k-form-item>
			<k-form-item label="机构证件类别错误描述">
	        	<k-field-text v-model="formData.organizationIdTypeDesc"/>
	     	</k-form-item>
			<k-form-item label="其他证件名称错误描述">
	        	<k-field-text v-model="formData.otherIdNameDesc"/>
	     	</k-form-item>
			<k-form-item label="证件号码错误描述">
	        	<k-field-text v-model="formData.idCodeDesc"/>
	     	</k-form-item>
			<k-form-item label="SPV资金托管账户开户行错误描述">
	        	<k-field-text v-model="formData.spvOpenBankDesc"/>
	     	</k-form-item>
			<k-form-item label="其他资金托管账户开户行错误描述">
	        	<k-field-text v-model="formData.otherOpenBankDesc"/>
	     	</k-form-item>
			<k-form-item label="权益人名称错误描述">
	        	<k-field-text v-model="formData.custNameDesc"/>
	     	</k-form-item>
			<k-form-item label="性别错误描述">
	        	<k-field-text v-model="formData.sexDesc"/>
	     	</k-form-item>
			<k-form-item label="风险偏好错误描述">
	        	<k-field-text v-model="formData.riskLevelDesc"/>
	     	</k-form-item>
			<k-form-item label="手机号码错误描述">
	        	<k-field-text v-model="formData.mobleDesc"/>
	     	</k-form-item>
			<k-form-item label="固定电话错误描述">
	        	<k-field-text v-model="formData.telPhoneDesc"/>
	     	</k-form-item>
			<k-form-item label="电子邮箱错误描述">
	        	<k-field-text v-model="formData.emailDesc"/>
	     	</k-form-item>
			<k-form-item label="导入日期">
	        	<k-field-text v-model="formData.impDate"/>
	     	</k-form-item>
			<k-form-item label="">
	        	<k-field-text v-model="formData.remarkDesc"/>
	     	</k-form-item>

	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="CustRegisterInfoErr.addCustRegisterInfo" data-from="addCustRegisterInfoErrForm"
		               :data-model="formData" data-target="CustRegisterInfoErrGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改投资者登记错误信息弹出框   -->
	<k-popup ref="editCustRegisterInfoErrPopup" data-title="修改">
	  <k-form ref="editCustRegisterInfoErrForm" :data-col="2">
		<k-form-item label="登记流水号">
        	<k-field-text v-model="formData.registerSerno"/>
     	</k-form-item>
		<k-form-item label="登记银行代码错误描述">
        	<k-field-text v-model="formData.bankCodeDesc"/>
     	</k-form-item>
		<k-form-item label="该权益人是否属于本行错误描述">
        	<k-field-text v-model="formData.isBelongDesc"/>
     	</k-form-item>
		<k-form-item label="权益人所属银行名称错误描述">
        	<k-field-text v-model="formData.issBankNameDesc"/>
     	</k-form-item>
		<k-form-item label="权益人所属银行代码错误描述">
        	<k-field-text v-model="formData.issBankCodeDesc"/>
     	</k-form-item>
		<k-form-item label="权益人境内外标识错误描述">
        	<k-field-text v-model="formData.inOutSignDesc"/>
     	</k-form-item>
		<k-form-item label="权益人所属国家或地区错误描述">
        	<k-field-text v-model="formData.issCountryDesc"/>
     	</k-form-item>
		<k-form-item label="数据类型错误描述">
        	<k-field-text v-model="formData.dataTypeDesc"/>
     	</k-form-item>
		<k-form-item label="原识别标识错误描述">
        	<k-field-text v-model="formData.oriCustNoDesc"/>
     	</k-form-item>
		<k-form-item label="识别标识错误描述">
        	<k-field-text v-model="formData.custNoDesc"/>
     	</k-form-item>
		<k-form-item label="权益人类别错误描述">
        	<k-field-text v-model="formData.custTypeDesc"/>
     	</k-form-item>
		<k-form-item label="个人证件类别错误描述">
        	<k-field-text v-model="formData.personalIdTypeDesc"/>
     	</k-form-item>
		<k-form-item label="机构证件类别错误描述">
        	<k-field-text v-model="formData.organizationIdTypeDesc"/>
     	</k-form-item>
		<k-form-item label="其他证件名称错误描述">
        	<k-field-text v-model="formData.otherIdNameDesc"/>
     	</k-form-item>
		<k-form-item label="证件号码错误描述">
        	<k-field-text v-model="formData.idCodeDesc"/>
     	</k-form-item>
		<k-form-item label="SPV资金托管账户开户行错误描述">
        	<k-field-text v-model="formData.spvOpenBankDesc"/>
     	</k-form-item>
		<k-form-item label="其他资金托管账户开户行错误描述">
        	<k-field-text v-model="formData.otherOpenBankDesc"/>
     	</k-form-item>
		<k-form-item label="权益人名称错误描述">
        	<k-field-text v-model="formData.custNameDesc"/>
     	</k-form-item>
		<k-form-item label="性别错误描述">
        	<k-field-text v-model="formData.sexDesc"/>
     	</k-form-item>
		<k-form-item label="风险偏好错误描述">
        	<k-field-text v-model="formData.riskLevelDesc"/>
     	</k-form-item>
		<k-form-item label="手机号码错误描述">
        	<k-field-text v-model="formData.mobleDesc"/>
     	</k-form-item>
		<k-form-item label="固定电话错误描述">
        	<k-field-text v-model="formData.telPhoneDesc"/>
     	</k-form-item>
		<k-form-item label="电子邮箱错误描述">
        	<k-field-text v-model="formData.emailDesc"/>
     	</k-form-item>
		<k-form-item label="导入日期">
        	<k-field-text v-model="formData.impDate"/>
     	</k-form-item>
		<k-form-item label="">
        	<k-field-text v-model="formData.remarkDesc"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="CustRegisterInfoErr.updateCustRegisterInfo" data-from="editCustRegisterInfoErrForm"
	        :data-model="formData" data-target="CustRegisterInfoErrGrid">
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
  export default {
    name: "CustRegisterInfoErr",
    data() {
      return {
        formData: {},
        selectRowData: {},
        searchParam:{}, //查询条件
        BreathDay:[],
      };
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      }
    },
    watch: {
      // 查询导入日期
      BreathDay() {
        console.log(this.BreathDay);
        this.$set(this.searchParam, 'startDate', this.BreathDay == null ? '' : this.BreathDay[0]);
        this.$set(this.searchParam, 'endDate', this.BreathDay == null ? '' : this.BreathDay[1]);
      },
    }
  };
</script>
