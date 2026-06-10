<template>
  <div class="py-page">
    <div>
		<k-form-search-customize ref="searchRef" data-model-name="DwsProdTTRDBefOri" data-label-width="80px" v-model="searchParam" data-target="dwsProdTTRDBefOriGrid">
    <k-form-item label="数据日期">
       <k-field-date v-model="searchParam.reportDate" data-type="date" data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd" :data-allowblank="false"/>
    </k-form-item>
    <k-form-item label="产品代码">
			<k-field-select
				v-model="searchParam.productCode"
				data-action="ProdInfoOds.findProdInfoOds"
				ref="prodCodeId"
				data-display-field="prodCode,prodName"
				data-value-field="prodCode"
				:data-remote="true"
				:data-remote-paging="true"
			/>
		</k-form-item>
    <k-form-item label="资产代码">
      <k-field-text v-model="searchParam.icode"/>
    </k-form-item>
    <k-form-item label="穿透前分类">
      <k-field-select v-model="searchParam.orgClassific" data-dict="g06_type"/>
    </k-form-item>
    <k-form-item label="穿透后分类">
      <k-field-select v-model="searchParam.newClassific" data-dict="g06_type"/>
    </k-form-item>
    <k-form-item label="持仓类型">
      <k-field-select v-model="searchParam.assettype" data-dict="position_type"/>
    </k-form-item>
		</k-form-search-customize>
    </div>
    <div class="py-page-container">
		<div class="table-top-btns">
			<div class="left">
				<k-btn slot="button" class="btn-custom-plain"  data-functype="EXPORT" data-export-dict="true" data-target="dwsProdTTRDBefOriGrid"
					data-export-name="G06穿透前报表（调整前）" data-export-form="searchRef">
                   <md-icon>cloud_download</md-icon>导出
                </k-btn>
				<k-btn
					slot="button"
					ref="reloadBtnRef"
					class="btn-custom-plain"
					data-functype="POPUP"
					data-target="handleTaskPopup"
					loading-tip="正在导入产品估值表，请稍后重试！">
					<md-icon>cloud_download</md-icon>
					导入产品估值表
				</k-btn>	
		    </div>
			<div class="right" v-show="vShow">
			&nbsp;&nbsp;导入开始时间：<span class="detail">{{ startTime }}</span>
			&nbsp;&nbsp;导入结束时间：<span class="detail">{{ endTime }}</span>
			&nbsp;&nbsp;导入日期：<span class="detail">{{ reportDate }}</span>
			&nbsp;&nbsp;导入结果：<a href="javascript:void(0)" class="detail1" @click="handleClick">{{ resultStatus }}</a>
			<k-btn class="btn-custom-icon" @click="queryRelust"> <md-icon md-src="/static/svg/reset.svg"></md-icon></k-btn>
			</div>
		</div>
      <k-grid ref="dwsProdTTRDBefOriGrid" @data-row-select="selectRow" data-action="DwsProdTTRDBefOri.findDwsProdTTRDBefOris" data-operate-column="false" :data-autoload="false" >
		    <k-grid-column data-header="id" data-name="id" data-hidden="true" data-export="false"></k-grid-column>
		    <k-grid-column data-header="数据日期" data-name="reportDate"></k-grid-column>
    		<k-grid-column data-header="产品代码" data-name="productCode"></k-grid-column>
    		<k-grid-column data-header="是否现管产品" data-name="cshMngF" data-dict="g06_yes_no"></k-grid-column>
    		<k-grid-column data-header="资产代码" data-name="icode"></k-grid-column>
    		<k-grid-column data-header="资产三类" data-name="assetThirdType"></k-grid-column>
    		<k-grid-column data-header="资产名称" data-name="iname"></k-grid-column>
    		<k-grid-column data-header="金额" data-name="amount"></k-grid-column>
    		<k-grid-column data-header="汇率" data-name="changerate"></k-grid-column>
    		<k-grid-column data-header="折算人民币金额" data-name="investedamountcny"></k-grid-column>
    		<k-grid-column data-header="穿透前分类" data-name="orgClassific" data-dict="g06_type"></k-grid-column>
    		<k-grid-column data-header="委托/自主管理" data-name="orderfreemanage" data-dict="g06_manager_type"></k-grid-column>
    		<k-grid-column data-header="穿透后分类" data-name="newClassific" data-dict="g06_type"></k-grid-column>
    		<k-grid-column data-header="是否逾期" data-name="isoverdue" data-dict="g06_yes_no"></k-grid-column>
    		<k-grid-column data-header="是否投向房地产业" data-name="moneyofproperty" data-dict="g06_yes_no"></k-grid-column>
    		<k-grid-column data-header="资金投向具体领域" data-name="cashtodomain" data-dict="g06_fields"></k-grid-column>
    		<k-grid-column data-header="创业投资基金" data-name="vcintfund" data-dict="g06_yes_no"></k-grid-column>
    		<k-grid-column data-header="政府出资产业投资基金" data-name="govintfund" data-dict="g06_yes_no"></k-grid-column>
    		<k-grid-column data-header="非标行业" data-name="isnostandard"  data-dict="g06_no_standard"></k-grid-column>
    		<k-grid-column data-header="如填列1.4.6-11债券，请分信用登记" data-name="bondrating" data-dict="g06_bond_credit"></k-grid-column>
    		<k-grid-column data-header="如填列1.4.2，请补充是否“1.4.2.a 专项债券”" data-name="specialbond" data-dict="g06_yes_no"></k-grid-column>
    		<k-grid-column data-header="如填列1.5.16 非标，对股质类业务区分“1.5.16.a 场内”和“1.5.16.b 场外”" data-name="inmarketornot" data-dict="g06_on_off_site"></k-grid-column>
    		<k-grid-column data-header="如填列1.5 非标，需补充是否“1.5.a 投向地方政府融资平台的部分”" data-name="cashtogovernment" data-dict="g06_yes_no"></k-grid-column>
    		<k-grid-column data-header="1.15.a 投向“公共私营合作项目”（PPP）的部分（金额）" data-name="cashtopublic"></k-grid-column>
    		<k-grid-column data-header="1.15.b 投向市场化债转股相关产品的部分（金额）" data-name="cashtorelateproduct"></k-grid-column>
    		<k-grid-column data-header="1.15.c 逾期资产（金额）" data-name="moneyofoverdueasset"></k-grid-column>
    		<k-grid-column data-header="3.2.3 二级资本债" data-name="secondlevelcaptialbond" data-dict="g06_yes_no" ></k-grid-column>
    		<k-grid-column data-header="3.2.2 永续债" data-name="continuebondforever" data-dict="g06_yes_no" ></k-grid-column>
    		<k-grid-column data-header="3.2.1 优先股" data-name="seniorbond" data-dict="g06_yes_no" ></k-grid-column>
    		<k-grid-column data-header="3.2.4 可转债" data-name="convertbond" data-dict="g06_yes_no" ></k-grid-column>
    		<k-grid-column data-header="3.2.5 其他银行资本补充工具" data-name="otherbanksupplementtools" data-dict="g06_yes_no" ></k-grid-column>
    		<k-grid-column data-header="持仓类型" data-name="assettype" data-dict="position_type"></k-grid-column>
    		<k-grid-column data-header="持仓数量" data-name="hldnQntt" data-type="money"></k-grid-column>
    		<k-grid-column data-header="处理日期" data-name="dealDate" data-hidden="true" data-export="false"></k-grid-column>
      </k-grid>
    </div>

	<!--    添加资产负载情况穿透前起源弹出框   -->
	<k-popup ref="addDwsProdTTRDBefOriPopup" data-title="新增">
    	<k-form ref="addDwsProdTTRDBefOriForm" :data-col="2">
			<k-form-item label="id">
	        	<k-field-text v-model="formData.id"/>
	     	</k-form-item>
			<k-form-item label="产品代码">
	        	<k-field-text v-model="formData.productCode"/>
	     	</k-form-item>
			<k-form-item label="资产代码">
	        	<k-field-text v-model="formData.icode"/>
	     	</k-form-item>
			<k-form-item label="资产三类">
	        	<k-field-text v-model="formData.assetThirdType"/>
	     	</k-form-item>
			<k-form-item label="资产名称">
	        	<k-field-text v-model="formData.iname"/>
	     	</k-form-item>
			<k-form-item label="金额">
	        	<k-field-text v-model="formData.amount"/>
	     	</k-form-item>
			<k-form-item label="汇率">
	        	<k-field-text v-model="formData.changerate"/>
	     	</k-form-item>
			<k-form-item label="折算人民币金额">
	        	<k-field-text v-model="formData.investedamountcny"/>
	     	</k-form-item>
			<k-form-item label="穿透前分类">
	        	<k-field-text v-model="formData.orgClassific"/>
	     	</k-form-item>
			<k-form-item label="委托/自主管理">
	        	<k-field-text v-model="formData.orderfreemanage"/>
	     	</k-form-item>
			<k-form-item label="穿透后分类">
	        	<k-field-text v-model="formData.newClassific"/>
	     	</k-form-item>
			<k-form-item label="是否逾期">
	        	<k-field-text v-model="formData.isoverdue"/>
	     	</k-form-item>
			<k-form-item label="资金投向具体领域">
	        	<k-field-text v-model="formData.cashtodomain"/>
	     	</k-form-item>
			<k-form-item label="创业投资基金">
	        	<k-field-text v-model="formData.vcintfund"/>
	     	</k-form-item>
			<k-form-item label="政府出资产业投资基金">
	        	<k-field-text v-model="formData.govintfund"/>
	     	</k-form-item>
			<k-form-item label="非标行业">
	        	<k-field-text v-model="formData.isnostandard"/>
	     	</k-form-item>
			<k-form-item label="如填列1.4.6-11债券，请分信用登记">
	        	<k-field-text v-model="formData.bondrating"/>
	     	</k-form-item>
			<k-form-item label="如填列1.4.2，请补充是否“1.4.2.a 专项债券”">
	        	<k-field-text v-model="formData.specialbond"/>
	     	</k-form-item>
			<k-form-item label="如填列1.5.16 非标，对股质类业务区分“1.5.16.a 场内”和“1.5.16.b 场外”">
	        	<k-field-text v-model="formData.inmarketornot"/>
	     	</k-form-item>
			<k-form-item label="如填列1.5 非标，需补充是否“1.5.a 投向地方政府融资平台的部分”">
	        	<k-field-text v-model="formData.cashtogovernment"/>
	     	</k-form-item>
			<k-form-item label="1.15.a 投向“公共私营合作项目”（PPP）的部分（金额）">
	        	<k-field-text v-model="formData.cashtopublic"/>
	     	</k-form-item>
			<k-form-item label="1.15.b 投向市场化债转股相关产品的部分（金额）">
	        	<k-field-text v-model="formData.cashtorelateproduct"/>
	     	</k-form-item>
			<k-form-item label="1.15.c 逾期资产（金额）">
	        	<k-field-text v-model="formData.moneyofoverdueasset"/>
	     	</k-form-item>
			<k-form-item label="1.15.d 投向房地产业的部分">
	        	<k-field-text v-model="formData.moneyofproperty"/>
	     	</k-form-item>
			<k-form-item label="3.2.3 二级资本债">
	        	<k-field-text v-model="formData.secondlevelcaptialbond"/>
	     	</k-form-item>
			<k-form-item label="3.2.2 永续债">
	        	<k-field-text v-model="formData.continuebondforever"/>
	     	</k-form-item>
			<k-form-item label="3.2.1 优先股">
	        	<k-field-text v-model="formData.seniorbond"/>
	     	</k-form-item>
			<k-form-item label="3.2.4 可转债">
	        	<k-field-text v-model="formData.convertbond"/>
	     	</k-form-item>
			<k-form-item label="3.2.5 其他银行资本补充工具">
	        	<k-field-text v-model="formData.otherbanksupplementtools"/>
	     	</k-form-item>
			<k-form-item label="持仓类型">
	        	<k-field-text v-model="formData.assettype"/>
	     	</k-form-item>
			<k-form-item label="处理日期">
	        	<k-field-text v-model="formData.dealDate"/>
	     	</k-form-item>
			<k-form-item label="报告日期">
	        	<k-field-text v-model="formData.reportDate"/>
	     	</k-form-item>

	      	<k-form-footer data-align="center">
              <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="DwsProdTTRDBefOri.addDwsProdTTRDBefOri" data-from="addDwsProdTTRDBefOriForm"
                     :data-model="formData" data-target="dwsProdTTRDBefOriGrid">
                <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
              </k-btn>
              <k-btn class="btn-custom-plain" data-functype="CLOSE">
                <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
            </k-form-footer>
    	</k-form>
	</k-popup>

	<k-popup ref="handleTaskPopup" data-title="导入产品估值表">
        <k-form ref="handleTaskAppForm" data-ui="element">
          <k-form-item label="估值日期" data-ui="element" data-input-width="500px">
            <k-field-date v-model="formData.dealDate" data-type="date" data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd" :data-default-value="currentDate" :data-allowblank="false"/>
          </k-form-item>
          <k-form-footer data-align="center">
           <k-btn ref="createFormBtnRef" class="btn-custom-primary" data-from="editForm" :data-handler="handleTaskApp">
							<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
          </k-form-footer>
        </k-form>
    </k-popup>

	<!--    修改资产负载情况穿透前起源弹出框   -->
	<k-popup ref="editDwsProdTTRDBefOriPopup" data-title="修改">
	  <k-form ref="editDwsProdTTRDBefOriForm" :data-col="2">
		<k-form-item label="id">
        	<k-field-text v-model="formData.id"/>
     	</k-form-item>
		<k-form-item label="产品代码">
        	<k-field-text v-model="formData.productCode"/>
     	</k-form-item>
		<k-form-item label="资产代码">
        	<k-field-text v-model="formData.icode"/>
     	</k-form-item>
		<k-form-item label="资产三类">
        	<k-field-text v-model="formData.assetThirdType"/>
     	</k-form-item>
		<k-form-item label="资产名称">
        	<k-field-text v-model="formData.iname"/>
     	</k-form-item>
		<k-form-item label="金额">
        	<k-field-text v-model="formData.amount"/>
     	</k-form-item>
		<k-form-item label="汇率">
        	<k-field-text v-model="formData.changerate"/>
     	</k-form-item>
		<k-form-item label="折算人民币金额">
        	<k-field-text v-model="formData.investedamountcny"/>
     	</k-form-item>
		<k-form-item label="穿透前分类">
        	<k-field-text v-model="formData.orgClassific"/>
     	</k-form-item>
		<k-form-item label="委托/自主管理">
        	<k-field-text v-model="formData.orderfreemanage"/>
     	</k-form-item>
		<k-form-item label="穿透后分类">
        	<k-field-text v-model="formData.newClassific"/>
     	</k-form-item>
		<k-form-item label="是否逾期">
        	<k-field-text v-model="formData.isoverdue"/>
     	</k-form-item>
		<k-form-item label="资金投向具体领域">
        	<k-field-text v-model="formData.cashtodomain"/>
     	</k-form-item>
		<k-form-item label="创业投资基金">
        	<k-field-text v-model="formData.vcintfund"/>
     	</k-form-item>
		<k-form-item label="政府出资产业投资基金">
        	<k-field-text v-model="formData.govintfund"/>
     	</k-form-item>
		<k-form-item label="非标行业">
        	<k-field-text v-model="formData.isnostandard"/>
     	</k-form-item>
		<k-form-item label="如填列1.4.6-11债券，请分信用登记">
        	<k-field-text v-model="formData.bondrating"/>
     	</k-form-item>
		<k-form-item label="如填列1.4.2，请补充是否“1.4.2.a 专项债券”">
        	<k-field-text v-model="formData.specialbond"/>
     	</k-form-item>
		<k-form-item label="如填列1.5.16 非标，对股质类业务区分“1.5.16.a 场内”和“1.5.16.b 场外”">
        	<k-field-text v-model="formData.inmarketornot"/>
     	</k-form-item>
		<k-form-item label="如填列1.5 非标，需补充是否“1.5.a 投向地方政府融资平台的部分”">
        	<k-field-text v-model="formData.cashtogovernment"/>
     	</k-form-item>
		<k-form-item label="1.15.a 投向“公共私营合作项目”（PPP）的部分（金额）">
        	<k-field-text v-model="formData.cashtopublic"/>
     	</k-form-item>
		<k-form-item label="1.15.b 投向市场化债转股相关产品的部分（金额）">
        	<k-field-text v-model="formData.cashtorelateproduct"/>
     	</k-form-item>
		<k-form-item label="1.15.c 逾期资产（金额）">
        	<k-field-text v-model="formData.moneyofoverdueasset"/>
     	</k-form-item>
		<k-form-item label="1.15.d 投向房地产业的部分">
        	<k-field-text v-model="formData.moneyofproperty"/>
     	</k-form-item>
		<k-form-item label="3.2.3 二级资本债">
        	<k-field-text v-model="formData.secondlevelcaptialbond"/>
     	</k-form-item>
		<k-form-item label="3.2.2 永续债">
        	<k-field-text v-model="formData.continuebondforever"/>
     	</k-form-item>
		<k-form-item label="3.2.1 优先股">
        	<k-field-text v-model="formData.seniorbond"/>
     	</k-form-item>
		<k-form-item label="3.2.4 可转债">
        	<k-field-text v-model="formData.convertbond"/>
     	</k-form-item>
		<k-form-item label="3.2.5 其他银行资本补充工具">
        	<k-field-text v-model="formData.otherbanksupplementtools"/>
     	</k-form-item>
		<k-form-item label="持仓类型">
        	<k-field-text v-model="formData.assettype"/>
     	</k-form-item>
		<k-form-item label="处理日期">
        	<k-field-text v-model="formData.dealDate"/>
     	</k-form-item>
		<k-form-item label="报告日期">
        	<k-field-text v-model="formData.reportDate"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="DwsProdTTRDBefOri.updateDwsProdTTRDBefOri" data-from="editDwsProdTTRDBefOriForm"
	        :data-model="formData" data-target="dwsProdTTRDBefOriGrid">
			  <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
	      </k-btn>
	      <k-btn class="btn-custom-plain" data-functype="CLOSE">
			  <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	    </k-form-footer>
	  </k-form>
	</k-popup>
  </div>
</template>

<style lang="scss" scoped>
.right {
  font-size: 12px;
  font-weight: bold;
  .detail {
		margin: 0 2px;
		font-size: 12px;
		font-weight: normal;
	}
  .detail1 {
		margin: 0 2px;
		font-size: 12px;
		font-weight: normal;
    color: #417fffff;
	}
  .btn-custom-icon {
    background: #fff !important;
    border: 1px solid #fff !important;
    color: #417fffff !important;
    box-shadow: none;
    /deep/ path {
      fill: #417fffff !important;
    }
  }
}
</style>

<script>
  import Tools from "@/utils/tools";
  export default {
    data() {
      return {
        formData: {},
        selectRowData: {},
        searchParam: {},
		currentDate:'',//定义上一天日期回显使用
		menuId: "M061811",
        buttonName: "导入产品估值表",
		vShow: false,
		startTime: "",
		endTime: "",
		reportDate: "",
		resultStatus: "",
		resultInfo: "",
      };
    },
	created() {
		this.getCurrentDate();
		this.queryRelust();
	},
    methods: {
        selectRow(row, column, event) {
            this.selectRowData = Object.assign({}, row)
            this.formData = Object.assign({}, row)
        },
		handleTaskApp() {
			if (this.$refs.handleTaskAppForm.validate()) {
				this.startTime = Tools.getCurrentTime("/", ":");
				this.endTime = "";
				this.reportDate = this.formData.dealDate;
				this.resultStatus = "正在"+this.buttonName+"中";
				this.resultInfo = "";

				this.$refs.reloadBtnRef.setIconStyle(0);
				this.httpUtil
					.comnUpdate({
						action: "DwsProdTTRDBefOri.impProductGzb",
						async: true,
						params: { 
							menuId: this.menuId,
							buttonName: this.buttonName,
							dealDate: this.formData.dealDate 
						},
						successAlert: false
					})
					.then((data) => {
						this.queryRelust();
						this.$refs.reloadBtnRef.setIconStyle(1);
					})
					.catch((err) => {
						console.log(err, "err");
						this.queryRelust();
						this.$refs.reloadBtnRef.setIconStyle(1);
					});
					setTimeout(() => {
						this.$refs.handleTaskPopup.close();
					}, 300);
			}
	    },
		queryRelust() {
			this.startTime = "";
			this.endTime = "";
			this.reportDate = "";
			this.resultStatus = "";
			this.resultInfo = "";

			this.httpUtil.comnQuery({
			action: "BaseReportReloadLog.findBaseReportReloadLogs",
			params: {
				menuId: this.menuId
			}
			}).then(data => {
			var rows = data.rows;
			if(rows.length>0) {
				this.vShow = true;
				this.startTime = rows[0].startTime;
				this.endTime = rows[0].endTime;
				this.reportDate = rows[0].reportDate;
				this.resultStatus = rows[0].resultStatus;
				this.resultInfo = rows[0].resultInfo;
			} else {
				this.vShow = false;
			}
			}).catch({});
		},
		getCurrentDate() {
			const now = new Date();
			const timeOne = new Date(now.getTime() - 86400000);
			const year = timeOne.getFullYear();
			let month = timeOne.getMonth() + 1;
			let day = timeOne.getDate();
			month = month < 10 ? '0' + month : month;
			day = day < 10 ? '0' + day : day;
			this.currentDate = year + '' + month + '' + day;
		},
		handleClick() {
			if (this.resultInfo) {
				if (this.resultStatus.indexOf("成功") > 0) {
					Tools.alertTime(this.resultStatus + "，具体报表如下：<br>" + this.resultInfo, "success", 5000);
				} else if (this.resultStatus.indexOf("失败") > 0) {
					Tools.alertTime(this.resultStatus + "，具体原因如下：<br>" + this.resultInfo, "danger", 5000);
				}
			} else {
				if (this.resultStatus.indexOf("中") > 0) {
					Tools.alertTime(this.resultStatus + "，请稍后", "warning", 5000);
				}
			}
		},
    }
  };
</script>
