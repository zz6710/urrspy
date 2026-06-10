<template>
  <div>
    <div>
		<k-form-search-customize data-target="disclosureAssetPlanGrid" v-model="queryParam">
			<k-form-item label="私募产品计划代码" data-label-width="150px">
				<k-field-text v-model="prodSearchParam.privateProdCode" data-validate-type="text"/>
			</k-form-item>
			<k-form-item label="私募产品计划名称"  data-label-width="150px">
				<k-field-text v-model="prodSearchParam.privateProdName" data-validate-type="text"/>
			</k-form-item>
			<k-form-item label="数据日期">
				<k-field-date v-model="queryParamDateRange" data-type="daterange"/>
			</k-form-item>
			<k-form-item label="产品代码"  data-label-width="150px">
				<k-field-select v-model="prodSearchParam.prodCode" data-action="T8Dict.findTaProdInfos"
								data-display-field="prodCode,prodName" data-value-field="prodCode"/>
			</k-form-item>
			<k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" data-target="addDisclosureAssetPlanPopup"
				   :data-handler="addDisclosureAssetPlan" v-if="global.isShowAuthorityButton('DisclosureAssetPlan.addDisclosureAssetPlan')">
				<md-icon md-src="/static/svg/add.svg" />
				新增
			</k-btn>
<!--			<k-btn slot="button" style="width: 120px" class="btn-custom-primary" :data-download-name="'底层资产数据导入模板'+'.xlsx'"-->
<!--				   data-descript="下载Excel模板" data-functype="DOWNLOAD" data-size="small"-->
<!--				   data-url="/download/server/PmsApp/prod/privateEquityDetail/comn-download.json">-->
<!--				<md-icon>cloud_download</md-icon>-->
<!--				下载Excel模板-->
<!--			</k-btn>-->
			<k-btn slot="button" style="width: 120px" data-functype="POPUP" class="btn-custom-primary"
				   data-target="addPopup">
				<md-icon>cloud_upload</md-icon>
				批量导入数据
			</k-btn>
			        <k-btn slot="button" style="width: 120px" class="btn-custom-primary" :data-download-name="'底层资产数据导入模板'+'.xlsx'"
			               data-descript="下载Excel模板" data-functype="DOWNLOAD" data-size="small"
			               data-url="/download/server/PmsApp/prod/privateEquityDetail/comn-download.json">
			          <md-icon>cloud_download</md-icon>
			          下载Excel模板
					</k-btn>
		</k-form-search-customize>
    </div>
    <div>
      <k-grid ref="disclosureAssetPlanGrid" @data-row-select="selectRow" data-action="DisclosureAssetPlan.findDisclosureAssetPlans1" >
		<k-grid-column data-header="id" data-name="id" data-hidden="true"></k-grid-column>
		<k-grid-column data-header="私募资管计划代码" data-name="privateProdCode" data-width="100"></k-grid-column>
		<k-grid-column data-header="私募资管计划名称" data-name="privateProdName" data-width="250"></k-grid-column>
		<k-grid-column data-header="数据日期" data-name="dataDate" data-type="date" data-width="100"></k-grid-column>
		<k-grid-column data-header="固定收益投资" data-name="fixedIncomeInvest" data-width="150"></k-grid-column>
		<k-grid-column data-header="权益投资" data-name="equityInvest" data-width="150"></k-grid-column>
		<k-grid-column data-header="金融衍生品投资" data-name="financialInvest" data-width="150"></k-grid-column>
		<k-grid-column data-header="商品及其他投资" data-name="otherInvest" data-width="150"></k-grid-column>
		<k-grid-column data-header="公募资管产品" data-name="publicProdInvest" data-width="150"></k-grid-column>
		<k-grid-column data-header="私募资管合计" data-name="privateTotal" data-width="150"></k-grid-column>
		<k-grid-column data-header="私募资管计划份额" data-name="planShare" data-width="150"></k-grid-column>
		<k-grid-column data-header="私募资管计划资产净值" data-name="planNet" data-width="150"></k-grid-column>
		<k-grid-column data-header="创建人id" data-name="createUserId" data-hidden="true"></k-grid-column>
		<k-grid-column data-header="创建人名称" data-name="createUserName" data-width="150"></k-grid-column>
		<k-grid-column data-header="创建日期" data-name="createDate" data-hidden="true"></k-grid-column>
		<k-grid-column data-header="创建时间" data-name="createTime"data-width="150" data-render="renderDateTimeCreate"></k-grid-column>
        <template slot="operate" slot-scope="scope">
			<k-btn class="md-info md-just-icon md-simple" :data-model="scope.row.row" @click="popupDetail(scope.row.row)" data-descript="查看私募资管产品"
				   data-functype="POPUP" data-size="mini">
				<md-icon>library_books</md-icon>
			</k-btn>
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改私募资管产品计划" data-functype="POPUP" data-size="mini"
            data-target="editDisclosureAssetPlanPopup">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="DisclosureAssetPlan.deleteDisclosureAssetPlan" data-size="mini"
               data-type="danger" data-target="disclosureAssetPlanGrid" :data-confirm="true" data-descript="删除私募资管产品计划">
          	<md-icon>close</md-icon>
    	  </k-btn>
        </template>
      </k-grid>
    </div>
    
	<!--    添加私募资管产品计划弹出框   -->
	<k-popup ref="addDisclosureAssetPlanPopup" data-title="新增">
    	<k-form ref="addDisclosureAssetPlanForm" :data-col="2">
			<k-form-item label="私募资管计划代码">
	        	<k-field-text v-model="formData.privateProdCode" :data-allowblank="false" :data-max-length="32"/>
	     	</k-form-item>
			<k-form-item label="私募资管计划名称">
	        	<k-field-text v-model="formData.privateProdName" :data-allowblank="false" :data-max-length="128"/>
	     	</k-form-item>
			<k-form-item label="数据日期">
	        	<k-field-date v-model="formData.dataDate" :data-allowblank="false" data-date-format="yyyy-MM-dd"/>
	     	</k-form-item>
			<k-form-item label="固定收益投资">
	        	<k-field-text v-model="formData.fixedIncomeInvest"
							  :data-allowblank="false"
							  data-regx-text="请输入正确的数字" :data-max-length="17"
							  data-digits="2" data-integer-length="14"
							  data-validate-type="number" data-type="number"/>
	     	</k-form-item>
			<k-form-item label="权益投资">
	        	<k-field-text v-model="formData.equityInvest"
							  :data-allowblank="false"
							  data-regx-text="请输入正确的数字" :data-max-length="17"
							  data-digits="2" data-integer-length="14"
							  data-validate-type="number" data-type="number"/>
	     	</k-form-item>
			<k-form-item label="金融衍生品投资">
	        	<k-field-text v-model="formData.financialInvest"
							  :data-allowblank="false"
							  data-regx-text="请输入正确的数字" :data-max-length="17"
							  data-digits="2" data-integer-length="14"
							  data-validate-type="number" data-type="number"/>
	     	</k-form-item>
			<k-form-item label="商品及其他投资">
	        	<k-field-text v-model="formData.otherInvest"
							  :data-allowblank="false"
							  data-regx-text="请输入正确的数字" :data-max-length="17"
							  data-digits="2" data-integer-length="14"
							  data-validate-type="number" data-type="number"/>
	     	</k-form-item>
			<k-form-item label="公募资管产品">
	        	<k-field-text v-model="formData.publicProdInvest"
							  :data-allowblank="false"
							  data-regx-text="请输入正确的数字" :data-max-length="17"
							  data-digits="2" data-integer-length="14"
							  data-validate-type="number" data-type="number"/>
	     	</k-form-item>
			<k-form-item label="私募资管合计">
	        	<k-field-text v-model="formData.privateTotal"
							  :data-allowblank="false"
							  data-regx-text="请输入正确的数字" :data-max-length="17"
							  data-digits="2" data-integer-length="14"
							  data-validate-type="number" data-type="number"
							  :data-disabled="true"/>
	     	</k-form-item>
			<k-form-item label="私募资管计划份额">
	        	<k-field-text v-model="formData.planShare"
							  :data-allowblank="false"
							  data-regx-text="请输入正确的数字" :data-max-length="17"
							  data-digits="2" data-integer-length="14"
							  data-validate-type="number" data-type="number"/>
	     	</k-form-item>
			<k-form-item label="私募资管计划资产净值">
	        	<k-field-text v-model="formData.planNet"
							  :data-allowblank="false"
							  data-regx-text="请输入正确的数字" :data-max-length="17"
							  data-digits="2" data-integer-length="14"
							  data-validate-type="number" data-type="number"/>
	     	</k-form-item>
	  	
	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="DisclosureAssetPlan.addDisclosureAssetPlan" data-from="addForm"
		               :data-model="formData" data-target="disclosureAssetPlanGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>
    
	<!--    修改私募资管产品计划弹出框   -->
	<k-popup ref="editDisclosureAssetPlanPopup" data-title="修改">
	  <k-form ref="editDisclosureAssetPlanForm" :data-col="2">
		<k-form-item label="私募资管计划代码">
        	<k-field-text v-model="formData.privateProdCode" :data-allowblank="false" :data-max-length="32" :data-disabled="true"/>
     	</k-form-item>
		<k-form-item label="私募资管计划名称">
        	<k-field-text v-model="formData.privateProdName" :data-allowblank="false" :data-max-length="128" :data-disabled="true"/>
     	</k-form-item>
		<k-form-item label="数据日期">
        	<k-field-text v-model="formData.dataDate" :data-allowblank="false" data-date-format="yyyy-MM-dd" :data-disabled="true"/>
     	</k-form-item>
		<k-form-item label="固定收益投资">
        	<k-field-text v-model="formData.fixedIncomeInvest"
						  :data-allowblank="false"
						  data-regx-text="请输入正确的数字" :data-max-length="17"
						  data-digits="2" data-integer-length="14"
						  data-validate-type="number" data-type="number"/>
     	</k-form-item>
		<k-form-item label="权益投资">
        	<k-field-text v-model="formData.equityInvest"
						  :data-allowblank="false"
						  data-regx-text="请输入正确的数字" :data-max-length="17"
						  data-digits="2" data-integer-length="14"
						  data-validate-type="number" data-type="number"/>
     	</k-form-item>
		<k-form-item label="金融衍生品投资">
        	<k-field-text v-model="formData.financialInvest"
						  :data-allowblank="false"
						  data-regx-text="请输入正确的数字" :data-max-length="17"
						  data-digits="2" data-integer-length="14"
						  data-validate-type="number" data-type="number"/>
     	</k-form-item>
		<k-form-item label="商品及其他投资">
        	<k-field-text v-model="formData.otherInvest"
						  :data-allowblank="false"
						  data-regx-text="请输入正确的数字" :data-max-length="17"
						  data-digits="2" data-integer-length="14"
						  data-validate-type="number" data-type="number"/>
     	</k-form-item>
		<k-form-item label="公募资管产品">
        	<k-field-text v-model="formData.publicProdInvest"
						  :data-allowblank="false"
						  data-regx-text="请输入正确的数字" :data-max-length="17"
						  data-digits="2" data-integer-length="14"
						  data-validate-type="number" data-type="number"/>
     	</k-form-item>
		<k-form-item label="私募资管合计">
        	<k-field-text v-model="formData.privateTotal"
						  :data-allowblank="false"
						  data-regx-text="请输入正确的数字" :data-max-length="17"
						  data-digits="2" data-integer-length="14"
						  data-validate-type="number" data-type="number"/>
     	</k-form-item>
		<k-form-item label="私募资管计划份额">
        	<k-field-text v-model="formData.planShare"
						  :data-allowblank="false"
						  data-regx-text="请输入正确的数字" :data-max-length="17"
						  data-digits="2" data-integer-length="14"
						  data-validate-type="number" data-type="number"
						  :data-disabled="true"/>
     	</k-form-item>
		<k-form-item label="私募资管计划资产净值">
        	<k-field-text v-model="formData.planNet"
						  :data-allowblank="false"
						  data-regx-text="请输入正确的数字" :data-max-length="17"
						  data-digits="2" data-integer-length="14"
						  data-validate-type="number" data-type="number"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="DisclosureAssetPlan.updateDisclosureAssetPlan" data-from="editDisclosureAssetPlanForm"
	        :data-model="formData" data-target="disclosureAssetPlanGrid">
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
										data-upload-url="/upload/server/PmsApp/prod/privateEquityDetail/comn-upload.json">
				  </k-field-excel-upload>
			  </k-form-item>
			  <k-form-footer data-align="center">
				  <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="disclosureAssetPlanGrid" ref="submitBtn"
						 :data-auto-upload="false" data-from="addForm" :data-handler="submitUploadParam">确定
				  </k-btn>
				  <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
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
			  privateProdCode:'',
			  privateProdName:'',
		  },
		  queryParamDateRange: [],
      };
    },
	  computed: {
		  queryParam() {
			  return {
				  'privateProdCode': this.prodSearchParam.privateProdCode,
				  'privateProdName': this.prodSearchParam.privateProdName,
				  'dataDateForQuery1': this.queryParamDateRange ? this.queryParamDateRange[0] : null,
				  'dataDateForQuery2': this.queryParamDateRange ? this.queryParamDateRange[1] : null,
				  'prodCode':this.prodSearchParam.prodCode,
			  }
		  }
	  },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      },
		addDisclosureAssetPlan(){
      		this.formData = {};
			this.$set(this.formData,'privateTotal','0');
			this.$set(this.formData,'fixedIncomeInvest','0');
			this.$set(this.formData,'equityInvest','0');
			this.$set(this.formData,'otherInvest','0');
			this.$set(this.formData,'publicProdInvest','0');
			this.$set(this.formData,'financialInvest','0');
		},
		renderDateTimeCreate(row) {
			return Tools.formatDateTime(row.createDate, row.createTime);
		},
		popupDetail(row){
      		this.$router.push({
				path:'/main/pms/disclosureFlow/privateEquityDetail',
				query:{
					'privateProdCode':row.privateProdCode,
					'privateProdName':row.privateProdName,
					'dataDate':row.dataDate,
				}

			})
		},
		onSubmitSuccess() {
			this.$refs.uploadRef.doReset();
			this.$refs.addForm.reset();
			this.$refs.addPopup.close();
			this.$refs.disclosureAssetPlanGrid.load();
		},
		onSubmitError() {
			this.$refs.uploadRef.doReset();
			this.$refs.submitBtn.setIconStyle(1, [])
		},
		submitUploadParam() {
			//文件上传校验
			var validate = this.$refs.addForm.validate();
			if (validate) {
				let formData = this.formData;
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
    },
	  watch: {
		  // 'formData.prodCode': function (newdata, oldVal) {
			//   //console.log("PordCode：", newdata)
			//   this.httpUtil.comnUpdate({
			// 	  action: "T8Dict.findAllTaProdInfosByCode",
			// 	  params: {
			// 		  prodCode:newdata
			// 	  },successAlert: false
		  //
			//   }).then(data => {
			// 	  //console.log("请求成功",data.returndata.prodeName)
			// 	  this.formData.prodName=data.returndata.prodeName
			//   });
		  // },
		  //这里是监听固定收益  做累加
		  'formData.fixedIncomeInvest': function (fixedIncomeInvest, oldVal) {
			  if (fixedIncomeInvest == '') {
				  this.formData.fixedIncomeInvest = 0
				  this.formData.privateTotal = (parseFloat(0) + parseFloat(this.formData.equityInvest) + parseFloat(this.formData.otherInvest) + parseFloat(this.formData.publicProdInvest) + parseFloat(this.formData.financialInvest)).toFixed(2)
			  } else {
				  this.formData.privateTotal = (parseFloat(fixedIncomeInvest) + parseFloat(this.formData.equityInvest) + parseFloat(this.formData.otherInvest) + parseFloat(this.formData.publicProdInvest) + parseFloat(this.formData.financialInvest)).toFixed(2)
			  }
		  },
		  'formData.equityInvest': function (equityInvest, oldVal) {
			  if (equityInvest == '') {
				  this.formData.equityInvest = 0
				  this.formData.privateTotal = (parseFloat(0) + parseFloat(this.formData.fixedIncomeInvest) + parseFloat(this.formData.otherInvest) + parseFloat(this.formData.publicProdInvest) + parseFloat(this.formData.financialInvest)).toFixed(2)
			  } else {
				  this.formData.privateTotal = (parseFloat(equityInvest) + parseFloat(this.formData.fixedIncomeInvest) + parseFloat(this.formData.otherInvest) + parseFloat(this.formData.publicProdInvest) + parseFloat(this.formData.financialInvest)).toFixed(2)
			  }
		  },
		  'formData.otherInvest': function (otherInvest, oldVal) {
			  if (otherInvest == '') {
				  this.formData.otherInvest = 0
				  this.formData.privateTotal = (parseFloat(0) + parseFloat(this.formData.fixedIncomeInvest) + parseFloat(this.formData.equityInvest) + parseFloat(this.formData.publicProdInvest) + parseFloat(this.formData.financialInvest)).toFixed(2)
			  } else {
				  this.formData.privateTotal = (parseFloat(otherInvest) + parseFloat(this.formData.fixedIncomeInvest) + parseFloat(this.formData.equityInvest) + parseFloat(this.formData.publicProdInvest) + parseFloat(this.formData.financialInvest)).toFixed(2)
			  }
		  },
		  'formData.publicProdInvest': function (publicProdInvest, oldVal) {
			  if (publicProdInvest == '') {
				  this.formData.publicProdInvest = 0
				  this.formData.privateTotal = (parseFloat(0) + parseFloat(this.formData.fixedIncomeInvest) + parseFloat(this.formData.equityInvest) + parseFloat(this.formData.otherInvest) + parseFloat(this.formData.financialInvest)).toFixed(2)
			  } else {
				  this.formData.privateTotal = (parseFloat(publicProdInvest) + parseFloat(this.formData.fixedIncomeInvest) + parseFloat(this.formData.equityInvest) + parseFloat(this.formData.otherInvest) + parseFloat(this.formData.financialInvest)).toFixed(2)
			  }
		  },
		  'formData.financialInvest': function (financialInvest, oldVal) {
			  if (financialInvest == '') {
				  this.formData.financialInvest = 0
				  this.formData.privateTotal = (parseFloat(0) + parseFloat(this.formData.fixedIncomeInvest) + parseFloat(this.formData.equityInvest) + parseFloat(this.formData.otherInvest) + parseFloat(this.formData.publicProdInvest)).toFixed(2)
			  } else {
				  this.formData.privateTotal = (parseFloat(financialInvest) + parseFloat(this.formData.fixedIncomeInvest) + parseFloat(this.formData.equityInvest) + parseFloat(this.formData.otherInvest) + parseFloat(this.formData.publicProdInvest)).toFixed(2)
			  }
		  },
		  'formData.privateTotal': function (privateTotal, oldVal) {
			  if (isNaN(privateTotal)) {
				  this.formData.privateTotal = 0
			  }
		  }
	  }
  };
</script>
