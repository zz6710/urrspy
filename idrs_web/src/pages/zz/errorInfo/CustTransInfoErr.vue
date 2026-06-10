<template>
  <div class="py-page">
    <div>
        <k-form-search-customize data-model-name="CustTransInfoErr" data-target="CustTransInfoErrGrid" data-label-width="80px" v-model = "searchParam">
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
      <k-grid ref="CustTransInfoErrGrid" @data-row-select="selectRow"  data-operate-column="false" data-action="CustTransInfoErr.findCustTransInfos" >
		<k-grid-column data-header="导入时间" data-name="impDate"></k-grid-column>
		<k-grid-column data-header="登记流水号" data-name="registerSerno"></k-grid-column>
		<k-grid-column data-header="发行机构代码错误描述" data-name="bankCodeDesc"></k-grid-column>
		<k-grid-column data-header="核心交易流水号错误描述" data-name="transSernoDesc"></k-grid-column>
		<k-grid-column data-header="销售合同号错误描述" data-name="contractNoDesc"></k-grid-column>
		<k-grid-column data-header="理财账号错误描述" data-name="fncTransAcctNoDesc"></k-grid-column>
		<k-grid-column data-header="客户统一编号错误描述" data-name="hostCustNoDesc"></k-grid-column>
		<k-grid-column data-header="识别标识错误描述" data-name="custNoDesc"></k-grid-column>
		<k-grid-column data-header="客户姓名错误描述" data-name="custNameDesc"></k-grid-column>
		<k-grid-column data-header="交易序列号错误描述" data-name="dealNoDesc"></k-grid-column>
		<k-grid-column data-header="关联活期存款账号错误描述" data-name="acctNoDesc"></k-grid-column>
		<k-grid-column data-header="关联账号开户所在地错误描述" data-name="acctLocCodeDesc"></k-grid-column>
		<k-grid-column data-header="关联活期存款账号开户行代码错误描述" data-name="acctBankNoDesc"></k-grid-column>
		<k-grid-column data-header="关联活期存款账号开户行名称错误描述" data-name="acctBankNameDesc"></k-grid-column>
		<k-grid-column data-header="是否有其他机构代销错误描述" data-name="isAgentDesc"></k-grid-column>
		<k-grid-column data-header="代销机构代码错误描述" data-name="agentBankCodeDesc"></k-grid-column>
		<k-grid-column data-header="代销机构名称错误描述" data-name="agentBankNameDesc"></k-grid-column>
		<k-grid-column data-header="代销机构所属监管机构错误描述" data-name="agentReguCodeDesc"></k-grid-column>
		<k-grid-column data-header="产品登记编码错误描述" data-name="prodCodeDesc"></k-grid-column>
		<k-grid-column data-header="业务种类错误描述" data-name="busiCodeDesc"></k-grid-column>
		<k-grid-column data-header="业务发生地所属监管错误描述" data-name="busiReguCodeDesc"></k-grid-column>
		<k-grid-column data-header="业务确认日期错误描述" data-name="ackDateDesc"></k-grid-column>
		<k-grid-column data-header="业务确认时间错误描述" data-name="ackTimeDesc"></k-grid-column>
		<k-grid-column data-header="币种错误描述" data-name="curDesc"></k-grid-column>
		<k-grid-column data-header="金额错误描述" data-name="ackAmtDesc"></k-grid-column>
		<k-grid-column data-header="折算人民币金额错误描述" data-name="convertRmbDesc"></k-grid-column>
		<k-grid-column data-header="确认净值错误描述" data-name="navDesc"></k-grid-column>
		<k-grid-column data-header="份额错误描述" data-name="ackVolDesc"></k-grid-column>
		<k-grid-column data-header="费用错误描述" data-name="feeAmtDesc"></k-grid-column>
		<k-grid-column data-header="渠道错误描述" data-name="channelFlagDesc"></k-grid-column>
		<k-grid-column data-header="交易柜员号错误描述" data-name="inputuserDesc"></k-grid-column>
		<k-grid-column data-header="备注错误描述" data-name="remarkDesc"></k-grid-column>
		<k-grid-column data-header="登记流水号错误描述" data-name="registerSernoDesc"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改投资者明细错误信息" data-functype="POPUP" data-size="mini"
            data-target="editCustTransInfoErrPopup">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="CustTransInfoErr.deleteCustTransInfo" data-size="mini"
               data-type="danger" data-target="CustTransInfoErrGrid" :data-confirm="true" data-descript="删除投资者明细错误信息">
          	<md-icon>close</md-icon>
    	  </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加投资者明细错误信息弹出框   -->
	<k-popup ref="addCustTransInfoErrPopup" data-title="新增">
    	<k-form ref="addCustTransInfoErrForm" :data-col="2">
			<k-form-item label="导入时间">
	        	<k-field-text v-model="formData.impDate"/>
	     	</k-form-item>
			<k-form-item label="登记流水号">
	        	<k-field-text v-model="formData.registerSerno"/>
	     	</k-form-item>
			<k-form-item label="发行机构代码错误描述">
	        	<k-field-text v-model="formData.bankCodeDesc"/>
	     	</k-form-item>
			<k-form-item label="核心交易流水号错误描述">
	        	<k-field-text v-model="formData.transSernoDesc"/>
	     	</k-form-item>
			<k-form-item label="销售合同号错误描述">
	        	<k-field-text v-model="formData.contractNoDesc"/>
	     	</k-form-item>
			<k-form-item label="理财账号错误描述">
	        	<k-field-text v-model="formData.fncTransAcctNoDesc"/>
	     	</k-form-item>
			<k-form-item label="客户统一编号错误描述">
	        	<k-field-text v-model="formData.hostCustNoDesc"/>
	     	</k-form-item>
			<k-form-item label="识别标识错误描述">
	        	<k-field-text v-model="formData.custNoDesc"/>
	     	</k-form-item>
			<k-form-item label="客户姓名错误描述">
	        	<k-field-text v-model="formData.custNameDesc"/>
	     	</k-form-item>
			<k-form-item label="关联活期存款账号错误描述">
	        	<k-field-text v-model="formData.acctNoDesc"/>
	     	</k-form-item>
			<k-form-item label="关联账号开户所在地错误描述">
	        	<k-field-text v-model="formData.acctLocCodeDesc"/>
	     	</k-form-item>
			<k-form-item label="是否有其他机构代销错误描述">
	        	<k-field-text v-model="formData.isAgentDesc"/>
	     	</k-form-item>
			<k-form-item label="代销机构代码错误描述">
	        	<k-field-text v-model="formData.agentBankCodeDesc"/>
	     	</k-form-item>
			<k-form-item label="代销机构名称错误描述">
	        	<k-field-text v-model="formData.agentBankNameDesc"/>
	     	</k-form-item>
			<k-form-item label="代销机构所属监管机构错误描述">
	        	<k-field-text v-model="formData.agentReguCodeDesc"/>
	     	</k-form-item>
			<k-form-item label="产品登记编码错误描述">
	        	<k-field-text v-model="formData.prodCodeDesc"/>
	     	</k-form-item>
			<k-form-item label="业务种类错误描述">
	        	<k-field-text v-model="formData.busiCodeDesc"/>
	     	</k-form-item>
			<k-form-item label="业务发生地所属监管错误描述">
	        	<k-field-text v-model="formData.busiReguCodeDesc"/>
	     	</k-form-item>
			<k-form-item label="业务确认日期错误描述">
	        	<k-field-text v-model="formData.ackDateDesc"/>
	     	</k-form-item>
			<k-form-item label="业务确认时间错误描述">
	        	<k-field-text v-model="formData.ackTimeDesc"/>
	     	</k-form-item>
			<k-form-item label="币种错误描述">
	        	<k-field-text v-model="formData.curDesc"/>
	     	</k-form-item>
			<k-form-item label="金额错误描述">
	        	<k-field-text v-model="formData.ackAmtDesc"/>
	     	</k-form-item>
			<k-form-item label="折算人民币金额错误描述">
	        	<k-field-text v-model="formData.convertRmbDesc"/>
	     	</k-form-item>
			<k-form-item label="确认净值错误描述">
	        	<k-field-text v-model="formData.navDesc"/>
	     	</k-form-item>
			<k-form-item label="份额错误描述">
	        	<k-field-text v-model="formData.ackVolDesc"/>
	     	</k-form-item>
			<k-form-item label="费用错误描述">
	        	<k-field-text v-model="formData.feeAmtDesc"/>
	     	</k-form-item>
			<k-form-item label="渠道错误描述">
	        	<k-field-text v-model="formData.channelFlagDesc"/>
	     	</k-form-item>
			<k-form-item label="交易柜员号错误描述">
	        	<k-field-text v-model="formData.inputuserDesc"/>
	     	</k-form-item>
			<k-form-item label="备注错误描述">
	        	<k-field-text v-model="formData.remarkDesc"/>
	     	</k-form-item>
			<k-form-item label="登记流水号错误描述">
	        	<k-field-text v-model="formData.registerSernoDesc"/>
	     	</k-form-item>

	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="CustTransInfoErr.addCustTransInfo" data-from="addCustTransInfoErrForm"
		               :data-model="formData" data-target="CustTransInfoErrGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改投资者明细错误信息弹出框   -->
	<k-popup ref="editCustTransInfoErrPopup" data-title="修改">
	  <k-form ref="editCustTransInfoErrForm" :data-col="2">
		<k-form-item label="导入时间">
        	<k-field-text v-model="formData.impDate"/>
     	</k-form-item>
		<k-form-item label="登记流水号">
        	<k-field-text v-model="formData.registerSerno"/>
     	</k-form-item>
		<k-form-item label="发行机构代码错误描述">
        	<k-field-text v-model="formData.bankCodeDesc"/>
     	</k-form-item>
		<k-form-item label="核心交易流水号错误描述">
        	<k-field-text v-model="formData.transSernoDesc"/>
     	</k-form-item>
		<k-form-item label="销售合同号错误描述">
        	<k-field-text v-model="formData.contractNoDesc"/>
     	</k-form-item>
		<k-form-item label="理财账号错误描述">
        	<k-field-text v-model="formData.fncTransAcctNoDesc"/>
     	</k-form-item>
		<k-form-item label="客户统一编号错误描述">
        	<k-field-text v-model="formData.hostCustNoDesc"/>
     	</k-form-item>
		<k-form-item label="识别标识错误描述">
        	<k-field-text v-model="formData.custNoDesc"/>
     	</k-form-item>
		<k-form-item label="客户姓名错误描述">
        	<k-field-text v-model="formData.custNameDesc"/>
     	</k-form-item>
		<k-form-item label="关联活期存款账号错误描述">
        	<k-field-text v-model="formData.acctNoDesc"/>
     	</k-form-item>
		<k-form-item label="关联账号开户所在地错误描述">
        	<k-field-text v-model="formData.acctLocCodeDesc"/>
     	</k-form-item>
		<k-form-item label="是否有其他机构代销错误描述">
        	<k-field-text v-model="formData.isAgentDesc"/>
     	</k-form-item>
		<k-form-item label="代销机构代码错误描述">
        	<k-field-text v-model="formData.agentBankCodeDesc"/>
     	</k-form-item>
		<k-form-item label="代销机构名称错误描述">
        	<k-field-text v-model="formData.agentBankNameDesc"/>
     	</k-form-item>
		<k-form-item label="代销机构所属监管机构错误描述">
        	<k-field-text v-model="formData.agentReguCodeDesc"/>
     	</k-form-item>
		<k-form-item label="产品登记编码错误描述">
        	<k-field-text v-model="formData.prodCodeDesc"/>
     	</k-form-item>
		<k-form-item label="业务种类错误描述">
        	<k-field-text v-model="formData.busiCodeDesc"/>
     	</k-form-item>
		<k-form-item label="业务发生地所属监管错误描述">
        	<k-field-text v-model="formData.busiReguCodeDesc"/>
     	</k-form-item>
		<k-form-item label="业务确认日期错误描述">
        	<k-field-text v-model="formData.ackDateDesc"/>
     	</k-form-item>
		<k-form-item label="业务确认时间错误描述">
        	<k-field-text v-model="formData.ackTimeDesc"/>
     	</k-form-item>
		<k-form-item label="币种错误描述">
        	<k-field-text v-model="formData.curDesc"/>
     	</k-form-item>
		<k-form-item label="金额错误描述">
        	<k-field-text v-model="formData.ackAmtDesc"/>
     	</k-form-item>
		<k-form-item label="折算人民币金额错误描述">
        	<k-field-text v-model="formData.convertRmbDesc"/>
     	</k-form-item>
		<k-form-item label="确认净值错误描述">
        	<k-field-text v-model="formData.navDesc"/>
     	</k-form-item>
		<k-form-item label="份额错误描述">
        	<k-field-text v-model="formData.ackVolDesc"/>
     	</k-form-item>
		<k-form-item label="费用错误描述">
        	<k-field-text v-model="formData.feeAmtDesc"/>
     	</k-form-item>
		<k-form-item label="渠道错误描述">
        	<k-field-text v-model="formData.channelFlagDesc"/>
     	</k-form-item>
		<k-form-item label="交易柜员号错误描述">
        	<k-field-text v-model="formData.inputuserDesc"/>
     	</k-form-item>
		<k-form-item label="备注错误描述">
        	<k-field-text v-model="formData.remarkDesc"/>
     	</k-form-item>
		<k-form-item label="登记流水号错误描述">
        	<k-field-text v-model="formData.registerSernoDesc"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="CustTransInfoErr.updateCustTransInfo" data-from="editCustTransInfoErrForm"
	        :data-model="formData" data-target="CustTransInfoErrGrid">
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
    name: "CustTransInfoErr",
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
