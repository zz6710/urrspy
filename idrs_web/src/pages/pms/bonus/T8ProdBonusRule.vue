<template>
  <div>
    <div>
      <k-form-search-customize data-model-name="T8ProdBonusRule" data-target="t8ProdBonusRuleGrid" v-model="prodSearchParam">
		  <k-form-item label="产品代码">
			  <k-field-select v-model="prodSearchParam.prodCode" data-action="T8Dict.findTaProdInfos"
							  data-display-field="prodCode,prodName" data-value-field="prodCode"/>
		  </k-form-item>
		  <k-form-item label="产品名称">
			  <k-field-text v-model="prodSearchParam.prodName" data-validate-type="text"/>
		  </k-form-item>
		  <k-form-item label="权益登记日">
			  <k-field-date v-model="prodSearchParam.dividendBaseDate" data-validate-type="date"/>
		  </k-form-item>

		  <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addT8ProdBonusRulePopup">
			  <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
<!--		  <k-btn slot="button" class="btn-custom-primary" @click="genarateNoticeTask">-->
<!--			  <md-icon md-src="/static/svg/add.svg" />生成</k-btn>-->
      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="t8ProdBonusRuleGrid" @data-row-select="selectRow" data-action="T8ProdBonusRule.findT8ProdBonusRules1" >
		<k-grid-column data-header="id" data-name="id" data-hidden="true"></k-grid-column>
		<k-grid-column data-header="产品代码" data-name="prodCode"></k-grid-column>
		<k-grid-column data-header="产品名称" data-name="prodName" data-width="150"></k-grid-column>
		<k-grid-column data-header="分红方式" data-name="dividendType" data-dict="div_method"></k-grid-column>
		<k-grid-column data-header="分红模式" data-name="dividendMode" data-dict="t8_dividend_mode"></k-grid-column>
		<k-grid-column data-header="权益登记日" data-name="dividendRegisterDate" data-type="date" data-width="100"></k-grid-column>
		<k-grid-column data-header="分红基准日" data-name="dividendBaseDate" data-type="date" data-width="100"></k-grid-column>
		<k-grid-column data-header="分红除权日" data-name="dividendExDate" data-type="date" data-width="100"></k-grid-column>
		<k-grid-column data-header="当前份额" data-name="share" data-width="120"></k-grid-column>
		<k-grid-column data-header="份额日期" data-name="shareDate" data-type="date" data-width="100"></k-grid-column>
		<k-grid-column data-header="分红总金额" data-name="totalAmount" data-width="120"></k-grid-column>
		<k-grid-column data-header="单位分红" data-name="dividendUnit" data-width="120"></k-grid-column>
		<k-grid-column data-header="红利发放日" data-name="dividendIssueDate" data-type="date" data-width="100"></k-grid-column>
		<k-grid-column data-header="红利再投日" data-name="handOutDate" data-type="date" data-width="100"></k-grid-column>
		<k-grid-column data-header="现金红利到账日" data-name="dividendArrivalDate" data-type="date" data-width="100"></k-grid-column>
		<k-grid-column data-header="产品经理" data-name="prodManager"></k-grid-column>
		<k-grid-column data-header="投资经理" data-name="investManager"></k-grid-column>
		<k-grid-column data-header="估值经理" data-name="valuationManager"></k-grid-column>
		<k-grid-column data-header="审批任务id" data-name="processInstanceId" data-hidden="true"></k-grid-column>
		<k-grid-column data-header="流程状态" data-name="processStatus" data-dict="bonus_process_status"></k-grid-column>
		<k-grid-column data-header="创建人id" data-name="crtUserId" data-hidden="true"></k-grid-column>
		<k-grid-column data-header="创建人名称" data-name="crtUserUsername" data-hidden="true"></k-grid-column>
		<k-grid-column data-header="创建日期" data-name="crtDate" data-hidden="true"></k-grid-column>
		<k-grid-column data-header="创建时间" data-name="crtTime" data-render="renderDateTimeCreate" data-width="150"></k-grid-column>
		<k-grid-column data-header="更新人id" data-name="updUserId" data-hidden="true"></k-grid-column>
		<k-grid-column data-header="更新人名称" data-name="updUserUsername" data-hidden="true"></k-grid-column>
		<k-grid-column data-header="更新日期" data-name="updDate" data-hidden="true"></k-grid-column>
		<k-grid-column data-header="更新时间" data-name="updTime" data-render="renderDateTimeUpdate" data-width="150"></k-grid-column>
        <k-grid-column data-header="备注" data-name="remark"></k-grid-column>
		  <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="查看分红任务" :data-model="scope.row.row" @click="openRegular(scope.row.row)">
			  <md-icon>library_books</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="T8ProdBonusRule.deleteT8ProdBonusRule" data-size="mini"
               data-type="danger" data-target="t8ProdBonusRuleGrid" :data-confirm="true" data-descript="删除分红任务">
          	<md-icon>close</md-icon>
    	  </k-btn>
        </template>
      </k-grid>
    </div>
    
	<!--    添加分红规则弹出框   -->
	<k-popup ref="addT8ProdBonusRulePopup" data-title="新增">
    	<k-form ref="addT8ProdBonusRuleForm" :data-col="2">
			<k-form-item label="产品代码">
	        	<k-field-select v-model="formData.prodCode" data-action="T8ProdInfo.getProdInfos" data-value-field="prodCode"
							  data-display-field="prodCode,prodName" @data-on-change="changeProdCode" :data-allowblank="false"/>
	     	</k-form-item>
			<k-form-item label="产品名称">
	        	<k-field-text v-model="formData.prodName" :data-allowblank="false"/>
	     	</k-form-item>
			<k-form-item label="权益登记日">
	        	<k-field-date v-model="formData.dividendRegisterDate" :data-allowblank="false"/>
	     	</k-form-item>
	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdBonusRule.addT8ProdBonusRule" data-from="addT8ProdBonusRuleForm"
		               :data-model="formData" data-target="t8ProdBonusRuleGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>
    
	<!--    修改分红规则弹出框   -->
	<k-popup ref="editT8ProdBonusRulePopup" data-title="修改">
	  <k-form ref="editT8ProdBonusRuleForm" :data-col="2">
		<k-form-item label="id">
        	<k-field-text v-model="formData.id"/>
     	</k-form-item>
		<k-form-item label="产品代码">
        	<k-field-text v-model="formData.prodCode"/>
     	</k-form-item>
		<k-form-item label="产品名称">
        	<k-field-text v-model="formData.prodName"/>
     	</k-form-item>
		<k-form-item label="分红方式">
        	<k-field-text v-model="formData.dividendType"/>
     	</k-form-item>
		<k-form-item label="分红模式">
        	<k-field-text v-model="formData.dividendMode"/>
     	</k-form-item>
		<k-form-item label="权益登记日">
        	<k-field-text v-model="formData.dividendRegisterDate"/>
     	</k-form-item>
		<k-form-item label="分红基准日">
        	<k-field-text v-model="formData.dividendBaseDate"/>
     	</k-form-item>
		<k-form-item label="分红除权日">
        	<k-field-text v-model="formData.dividendExDate"/>
     	</k-form-item>
		<k-form-item label="当前份额">
        	<k-field-text v-model="formData.share"/>
     	</k-form-item>
		<k-form-item label="份额日期">
        	<k-field-text v-model="formData.shareDate"/>
     	</k-form-item>
		<k-form-item label="分红总金额">
        	<k-field-text v-model="formData.totalAmount"/>
     	</k-form-item>
		<k-form-item label="单位分红">
        	<k-field-text v-model="formData.dividendUnit"/>
     	</k-form-item>
		<k-form-item label="红利发放日">
        	<k-field-text v-model="formData.dividendIssueDate"/>
     	</k-form-item>
		<k-form-item label="红利再投日">
        	<k-field-text v-model="formData.handOutDate"/>
     	</k-form-item>
		<k-form-item label="现金红利到账日">
        	<k-field-text v-model="formData.dividendArrivalDate"/>
     	</k-form-item>
		<k-form-item label="产品经理id">
        	<k-field-text v-model="formData.prodManagerId"/>
     	</k-form-item>
		<k-form-item label="投资经理id">
        	<k-field-text v-model="formData.investManagerId"/>
     	</k-form-item>
		<k-form-item label="估值经理id">
        	<k-field-text v-model="formData.valuationManagerId"/>
     	</k-form-item>
		<k-form-item label="审批任务id">
        	<k-field-text v-model="formData.processInstanceId"/>
     	</k-form-item>
		<k-form-item label="流程状态">
        	<k-field-text v-model="formData.processStatus"/>
     	</k-form-item>
		<k-form-item label="分红方案状态">
        	<k-field-text v-model="formData.dividendStatus"/>
     	</k-form-item>
		<k-form-item label="创建人id">
        	<k-field-text v-model="formData.crtUserId"/>
     	</k-form-item>
		<k-form-item label="创建人名称">
        	<k-field-text v-model="formData.crtUserUsername"/>
     	</k-form-item>
		<k-form-item label="创建日期">
        	<k-field-text v-model="formData.crtDate"/>
     	</k-form-item>
		<k-form-item label="创建时间">
        	<k-field-text v-model="formData.crtTime"/>
     	</k-form-item>
		<k-form-item label="更新人id">
        	<k-field-text v-model="formData.updUserId"/>
     	</k-form-item>
		<k-form-item label="更新人名称">
        	<k-field-text v-model="formData.updUserUsername"/>
     	</k-form-item>
		<k-form-item label="更新日期">
        	<k-field-text v-model="formData.updDate"/>
     	</k-form-item>
		<k-form-item label="更新时间">
        	<k-field-text v-model="formData.updTime"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdBonusRule.updateT8ProdBonusRule" data-from="editT8ProdBonusRuleForm"
	        :data-model="formData" data-target="t8ProdBonusRuleGrid">
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
	import Tools from "@/utils/tools";
	export default {
		data() {
      return {
        formData: {},
        selectRowData: {},
		  prodSearchParam: {
        	prodCode: '',
			  prodName:'',
			  dividendRegisterDate:'',
		  },
		  establishDate:'',
      };
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      },
		renderDateTimeUpdate(row) {
			return Tools.formatDateTime(row.updDate, row.updTime);
		},
		renderDateTimeCreate(row) {
			return Tools.formatDateTime(row.crtDate, row.crtTime);
		},
		changeProdCode(val) {
			this.httpUtil.comnQuery({
				action: "T8ProdInfo.findProdDividendInfo",
				params: {prodCode: val}
			}).then(data => {
				this.$set(this.formData,"prodName",data.rows[0].prodName);
				// this.$set(_this.formData,"dividendType",data.rows[0].bonusType);
				this.establishDate = data.rows[0].establishDate;
			}).catch({});
		},
		openRegular(row) {
			this.$router.push({
				path: "/main/pms/bonus/prodBonusRuleRegular",
				query: {"prodCode":row.prodCode,"id":row.id},
			});
		},
		genarateNoticeTask() {
			this.httpUtil.comnQuery({
				action: "T8ProdBonusRule.generateBonusNoticeTask",
			})
		},
    }
  };
</script>
