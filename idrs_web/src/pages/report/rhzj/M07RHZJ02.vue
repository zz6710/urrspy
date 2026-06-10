<!--
 * @Author: litao
 * @Date: 2022-07-13 10:18:46
 * @LastEditTime: 2022-07-20 10:46:28
 * @LastEditors: litao
 * @Description: 产品资金募集信息
 * @FilePath: \idrs_web\src\pages\report\rhzj\M07RHZJ02.vue
-->
<template>
  <div>
    <div>
      <k-form-search-customize data-model-name="ReportPCD" data-target="reportPCDGrid" v-model="queryParam">
        <k-form-item label="行内产品代码" data-label-width="150px">
            <k-field-text v-model="prodSearchParam.prodCode" data-validate-type="text"/>
        </k-form-item>
        <k-form-item label="人行产品代码" data-label-width="150px">
            <k-field-text v-model="prodSearchParam.peoplebankSubmitcode" data-validate-type="text"/>
        </k-form-item>
        <k-form-item label="查询日期">
            <k-field-date v-model="prodSearchParam.reportDate" data-type="month"  data-date-format="yyyy-MM" data-value-format="yyyyMM" :data-allowblank="false"/>
        </k-form-item>
        <k-btn slot="button" style="width: 120px" class="md-success"
            data-descript="资金募集数据报送" data-size="small" @click="generatePBFile('mjDataSend')">
            <!-- <md-icon>cloud_download</md-icon> -->
            资金募集数据报送
        </k-btn>
        <k-btn slot="button" style="width: 120px" class="btn-custom-plain" data-target="reportPCDGrid" :data-export-name="'产品资金募集信息'"
            data-descript="报送数据导出" data-functype="EXPORT" data-size="small"
            data-url="ReportPCD.findReportPCDs">
            <md-icon>cloud_download</md-icon>
            报送数据导出
        </k-btn>
        <k-btn slot="button" style="width: 120px" data-functype="POPUP" class="btn-custom-plain"
            data-target="addPopup">
            <md-icon>cloud_upload</md-icon>
            报送数据导入
        </k-btn>
      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="reportPCDGrid" @data-row-select="selectRow" data-action="ReportPCD.findReportPCDs" data-fixed="right">
		<k-grid-column data-header="日期" data-name="reportDate" data-type="date"  data-width="100"></k-grid-column>
		<k-grid-column data-header="行内产品代码" data-name="prodCode" data-width="130"></k-grid-column>
		<k-grid-column data-header="报送人行产品代码" data-name="peoplebankSubmitcode" data-width="150"></k-grid-column>
		<k-grid-column data-header="地区代码" data-name="areaCode"></k-grid-column>
		<k-grid-column data-header="客户类型" data-name="custType" data-dict="t8_invest_object_rpt_dat" data-width="120"></k-grid-column>
		<k-grid-column data-header="币种代码" data-name="cny"></k-grid-column>
		<k-grid-column data-header="当期申购金额" data-name="currentBuyAmount" data-type="money" data-width="120"></k-grid-column>
		<k-grid-column data-header="当期申购金额折人民币" data-name="currentBuyAmountRmb" data-type="money" data-width="150"></k-grid-column>
		<k-grid-column data-header="当期申购份额" data-name="currentBuyVol" data-type="money"  data-width="120"></k-grid-column>
		<k-grid-column data-header="当期兑付/赎回金额" data-name="currentRedemptionAmount" data-type="money" data-width="120"></k-grid-column>
		<k-grid-column data-header="当期兑付/赎回金额折人民币" data-name="currentRedemptionAmountrmb" data-type="money" data-width="180"></k-grid-column>
		<k-grid-column data-header="当期兑付/赎回份额" data-name="currentRedemptionVol" data-type="money" data-width="150"></k-grid-column>
		<k-grid-column data-header="期末产品金额" data-name="terminaProdAmount" data-type="money"  data-width="120"></k-grid-column>
		<k-grid-column data-header="期末产品金额折人民币" data-name="terminaProdAmountRmb" data-type="money"  data-width="150"></k-grid-column>
		<k-grid-column data-header="期末产品份额" data-name="terminaProdVol" data-type="money"  data-width="120"></k-grid-column>
		<k-grid-column data-header="净值型产品期末净值" data-name="terminaProdNav" data-width="150"></k-grid-column>
		<k-grid-column data-header="净值型产品期末净值折人民币" data-name="terminaProdNavRmb"  data-width="180"></k-grid-column>
		<k-grid-column data-header="净值型产品期末累计净值" data-name="terminaProdNavAdd"  data-width="160"></k-grid-column>
		<k-grid-column data-header="净值型产品期末累计净值折人民币" data-name="terminaProdNavAddRmb"  data-width="200"></k-grid-column>
		<k-grid-column data-header="开放式非净值型产品预期最高收益率" data-name="prodMaxRate" data-width="220"></k-grid-column>
		<k-grid-column data-header="开放式非净值型产品预期最低收益率" data-name="prodMinRate" data-width="220"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改产品募集信息" data-functype="POPUP" data-size="mini"
            data-target="editReportPCDPopup">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="ReportPCD.deleteReportPCD" data-size="mini"
               data-type="danger" data-target="reportPCDGrid" :data-confirm="true" data-descript="删除产品募集信息">
          	<md-icon>close</md-icon>
    	  </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    修改产品募集信息弹出框   -->
	<k-popup ref="editReportPCDPopup" data-title="修改">
	  <k-form ref="editReportPCDForm" :data-col="2">
		<k-form-item label="日期">
        	<k-field-text v-model="formData.reportDate" :data-disabled="true" :data-allowblank="false"/>
     	</k-form-item>
		<k-form-item label="产品代码">
        	<k-field-text v-model="formData.prodCode" :data-disabled="true" :data-allowblank="false"/>
     	</k-form-item>
		<k-form-item label="报送人行产品代码">
        	<k-field-text v-model="formData.peoplebankSubmitcode" :data-disabled="true" :data-allowblank="false"/>
     	</k-form-item>
		<k-form-item label="地区代码">
        	<k-field-text v-model="formData.areaCode" :data-allowblank="false"/>
     	</k-form-item>
		<k-form-item label="客户类型">
        	<k-field-select v-model="formData.custType" data-dict="t8_invest_object_rpt_dat" :data-allowblank="false"/>
     	</k-form-item>
		<k-form-item label="币种代码">
        	<k-field-text v-model="formData.cny" :data-disabled="true" :data-allowblank="false"/>
     	</k-form-item>
		<k-form-item label="当期申购金额">
        	<k-field-text v-model="formData.currentBuyAmount" data-validate-type="number" :data-max-length="16" :data-allowblank="true"/>
     	</k-form-item>
		<k-form-item label="当期申购金额折人民币">
        	<k-field-text v-model="formData.currentBuyAmountRmb" data-validate-type="number" :data-max-length="16" />
     	</k-form-item>
		<k-form-item label="当期申购份额">
        	<k-field-text v-model="formData.currentBuyVol" data-validate-type="number" :data-max-length="16"  :data-allowblank="true"/>
     	</k-form-item>
		<k-form-item label="当期兑付/赎回金额">
        	<k-field-text v-model="formData.currentRedemptionAmount" data-validate-type="number" :data-max-length="16"/>
     	</k-form-item>
		<k-form-item label="当期兑付/赎回金额折人民币">
        	<k-field-text v-model="formData.currentRedemptionAmountrmb" data-validate-type="number" :data-max-length="16"  :data-allowblank="true"/>
     	</k-form-item>
		<k-form-item label="当期兑付/赎回份额">
        	<k-field-text v-model="formData.currentRedemptionVol" data-validate-type="number" :data-max-length="16"  :data-allowblank="true"/>
     	</k-form-item>
		<k-form-item label="期末产品金额">
        	<k-field-text v-model="formData.terminaProdAmount" data-validate-type="number" :data-max-length="16"  :data-allowblank="true"/>
     	</k-form-item>
		<k-form-item label="期末产品金额折人民币">
        	<k-field-text v-model="formData.terminaProdAmountRmb" data-validate-type="number" :data-max-length="16" />
     	</k-form-item>
		<k-form-item label="期末产品份额">
        	<k-field-text v-model="formData.terminaProdVol" data-validate-type="number" :data-max-length="16" :data-allowblank="true"/>
     	</k-form-item>
		<k-form-item label="净值型产品期末净值">
        	<k-field-text v-model="formData.terminaProdNav" data-validate-type="number" :data-max-length="9" />
     	</k-form-item>
		<k-form-item label="净值型产品期末净值折人民币">
        	<k-field-text v-model="formData.terminaProdNavRmb" data-validate-type="number" :data-max-length="9"/>
     	</k-form-item>
		<k-form-item label="净值型产品期末累计净值">
        	<k-field-text v-model="formData.terminaProdNavAdd" data-validate-type="number" :data-max-length="9"/>
     	</k-form-item>
		<k-form-item label="净值型产品期末累计净值折人民币">
        	<k-field-text v-model="formData.terminaProdNavAddRmb" data-validate-type="number" :data-max-length="9"/>
     	</k-form-item>
		<k-form-item label="开放式非净值型产品预期最高收益率">
        	<k-field-text v-model="formData.prodMaxRate" data-validate-type="number" :data-max-length="9"/>
     	</k-form-item>
		<k-form-item label="开放式非净值型产品预期最低收益率">
        	<k-field-text v-model="formData.prodMinRate" data-validate-type="number" :data-max-length="9" :data-max-value="formData.prodMaxRate"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ReportPCD.updateReportPCD" data-from="editReportPCDForm"
	        :data-model="formData" data-target="reportPCDGrid">
	        <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
	      </k-btn>
	      <k-btn class="btn-custom-plain" data-functype="CLOSE">
	        <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	    </k-form-footer>
	  </k-form>
	</k-popup>

	<k-popup ref="addPopup" title="报送数据导入">
        <k-form ref="addForm" data-ui="element">
          <k-form-item label="报送数据导入" data-ui="element" data-input-width="500px">
            <k-field-excel-upload data-type="picture" ref="uploadRef" :data-multiple="false" :data-limit='1'
                data-accept=".xlsx,.xls"
                :data-error="onSubmitError" :data-success="onSubmitSuccess"
                :data-auto-upload="false"
                data-upload-url="/upload/server/RptApp/reportPCD/uploadPCD.json">
            </k-field-excel-upload>
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="reportPCDGrid" ref="submitBtn"
                  :data-auto-upload="false" data-from="addForm" :data-handler="submitUploadParam">确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
          </k-form-footer>
        </k-form>
    </k-popup>
  </div>
</template>

<script>
export default {
  name: "M07RHZJ02",
	data() {
		return {
			formData: {},
			selectRowData: {},
			prodSearchParam: {
				peoplebankSubmitcode: "",
				prodCode: "",
				reportDate: localStorage.getItem("currentWorkday").substring(0, 6)
			}
		};
	},
	computed: {
		queryParam() {
			return {
				...this.prodSearchParam
			};
		}
	},
	mounted() {
		this.$refs.reportPCDGrid.load(this.prodSearchParam);
	},
	methods: {
		submitUploadParam() {
			//文件上传校验
			let validate = this.$refs.addForm.validate();
			if (validate) {
				let temp = document.getElementsByClassName("upload-demo");
				let lis = temp[0].childNodes[1].childNodes.length;
				if (lis > 0) {
					this.$refs.uploadRef.upload();
				} else {
					this.$message.error("上传文件不能为空!");
					return false;
				}
			}
		},
		onSubmitSuccess() {
			this.$refs.uploadRef.doReset();
			this.$refs.addForm.reset();
			this.$refs.addPopup.close();
			this.$refs.reportPCDGrid.load();
		},
		onSubmitError() {
			this.$refs.uploadRef.doReset();
			this.$refs.submitBtn.setIconStyle(1, []);
		},
		selectRow(row, column, event) {
			this.selectRowData = Object.assign({}, row);
			this.formData = Object.assign({}, row);
		},
		generatePBFile(type) {
			if (!this.queryParam.reportDate) {
				this.$message.error("请选择查询日期");
				return;
			}
			this.httpUtil.download({
				url: "/download/server/RptApp/reportPPI/download.json",
				params: { reportDate: this.queryParam.reportDate, sendType: type },
				callback: () => {}
			});
		}
	}
};
</script>
