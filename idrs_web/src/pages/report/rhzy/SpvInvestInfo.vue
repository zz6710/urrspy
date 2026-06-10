<template>
	<div class="py-page">
		<div>
			<k-form-search-customize ref="searchRef" data-model-name="SpvInvestInfo" data-label-width="130px" v-model="searchParam" data-target="tableGrid">
				<k-form-item label="数据日期">
					<k-field-date
						v-model="searchParam.reportDate"
						data-type="month"
						data-date-format="yyyy-MM"
						data-value-format="yyyyMM"
						:data-allowblank="false"
					/>
				</k-form-item>
				<k-form-item label="特定目的载体类型">
					<k-field-select v-model="searchParam.spvType" data-dict-type="1" data-dict="spv_type" />
				</k-form-item>
				<k-form-item label="特定目的载体代码">
					<k-field-text v-model="searchParam.spvCode" />
				</k-form-item>
			</k-form-search-customize>
		</div>
		<div class="py-page-container">
			<div class="table-top-btns">
				<div class="left">
					<k-btn ref="assetSend" slot="button" class="btn-custom-plain" data-size="small" @click="creatZipFile('SPVTZX')">
						<md-icon>cloud_download</md-icon>
						数据报送
					</k-btn>
					<k-btn
						slot="button"
						class="btn-custom-plain"
						data-target="tableGrid"
						:data-export-name="getFileName('SPVTZX')"
						data-functype="EXPORT"
						data-export-form="searchRef"
					>
						<md-icon>cloud_download</md-icon>
						报送数据导出
					</k-btn>
					<k-btn slot="button" data-functype="POPUP" class="btn-custom-plain" data-target="addPopup">
						<md-icon>cloud_upload</md-icon>
						报送数据导入
					</k-btn>
				</div>
			</div>
			<k-grid
				ref="tableGrid"
				@data-row-select="selectRow"
				data-action="SpvInvestInfo.findSpvInvestInfos"
				:data-autoload="false"
				data-operate-width="120px"
				data-fixed="right"
				data-dict-type="1"
			>
				<k-grid-column data-align="left" data-header="数据日期" data-name="reportDate" data-width="100" data-export="false"></k-grid-column>
				<k-grid-column data-header="金融机构代码" data-name="orgCode" data-width="130"></k-grid-column>
				<k-grid-column data-header="内部机构号" data-name="innerOrgCode" data-width="150"></k-grid-column>
				<k-grid-column data-header="特定目的载体类型" data-name="spvType" data-width="220" data-dict="spv_type"></k-grid-column>
				<k-grid-column data-header="资管产品统计编码" data-name="ampsCode" data-width="220"></k-grid-column>
				<k-grid-column data-header="特定目的载体代码" data-name="spvCode" data-width="220"></k-grid-column>
				<k-grid-column data-header="发行人代码" data-name="issuerCode" data-width="150"></k-grid-column>
				<k-grid-column data-header="发行人地区代码" data-name="issuerRegionCode" data-width="150">
					<template slot-scope="scope">
						{{ getRegionText(scope.row.row.issuerRegionCode) }}
					</template>
				</k-grid-column>
				<k-grid-column data-header="运行方式" data-name="runMode" data-width="130" data-dict="spv_run_mode"></k-grid-column>
				<k-grid-column data-header="认购日期" data-name="subscripDate" data-width="100"></k-grid-column>
				<k-grid-column data-header="到期日期" data-name="expireDate" data-width="100"></k-grid-column>
				<k-grid-column data-header="币种" data-name="cur" data-dict="cur_type" data-width="100"></k-grid-column>
				<k-grid-column data-header="投资余额" data-name="investBalance" data-width="150"></k-grid-column>
				<k-grid-column data-header="投资余额折人民币" data-name="investBalanceRmb" data-width="150"></k-grid-column>
				<template slot="operate" slot-scope="scope">
					<k-btn class="btn-custom-text" data-descript="修改存量特定目的载体投资信息" data-functype="POPUP" data-size="mini" data-target="editPopup">
						修改
					</k-btn>
					<k-btn
						class="btn-custom-text"
						data-functype="SUBMIT"
						data-action="SpvInvestInfo.deleteSpvInvestInfo"
						data-size="mini"
						data-type="danger"
						data-target="tableGrid"
						:data-confirm="true"
						data-descript="删除存量特定目的载体投资信息"
					>
						删除
					</k-btn>
				</template>
			</k-grid>
		</div>

		<k-popup ref="addPopup" title="报送数据导入" @data-opened="uploadOpened()">
			<k-form ref="addForm" data-ui="element">
				<k-form-item label="数据日期">
					<k-field-date v-model="reportDate" data-type="month" data-date-format="yyyy-MM" data-value-format="yyyy-MM" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="报送数据导入" data-ui="element" data-input-width="500px">
					<k-field-excel-upload
						data-type="file"
						ref="uploadRef"
						:data-multiple="false"
						:data-limit="1"
						data-accept=".xlsx,.xls"
						:data-error="onSubmitError"
						:data-success="onSubmitSuccess"
						:data-auto-upload="false"
						data-upload-url="/upload/server/RptApp/rhzy/uploadSpvInvestInfo.json"
					>
					</k-field-excel-upload>
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-target="tableGrid"
						ref="submitBtn"
						:data-auto-upload="false"
						data-from="addForm"
						:data-handler="submitUploadParam"
						>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>

		<!--    修改存量特定目的载体投资信息弹出框   -->
		<k-popup ref="editPopup" data-title="修改">
			<k-form ref="editForm" :data-col="2" data-label-width="150px">
				<k-form-item label="金融机构代码">
					<k-field-text v-model="formData.orgCode" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="内部机构号">
					<k-field-text v-model="formData.innerOrgCode" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="特定目的载体类型">
					<k-field-select v-model="formData.spvType" :data-allowblank="false" data-dict="spv_type" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="资管产品统计编码">
					<k-field-text v-model="formData.ampsCode" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="特定目的载体代码">
					<k-field-text v-model="formData.spvCode" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="发行人代码">
					<k-field-text v-model="formData.issuerCode" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="发行人地区代码">
					<k-field-select
						v-model="formData.issuerRegionCode"
						:data-allowblank="false"
						:data-data="regionList"
						data-value-field="VALUE"
						data-display-field="TEXT"
						data-dict-type="1"
					/>
				</k-form-item>
				<k-form-item label="运行方式">
					<k-field-select v-model="formData.runMode" :data-allowblank="false" data-dict="spv_run_mode" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="认购日期">
					<k-field-date v-model="formData.subscripDate" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="到期日期">
					<k-field-date v-model="formData.expireDate" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="币种">
					<k-field-select v-model="formData.cur" data-dict="cur_type" :data-allowblank="false" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="投资余额">
					<k-field-text v-model="formData.investBalance" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="投资余额折人民币">
					<k-field-text v-model="formData.investBalanceRmb" :data-allowblank="false" />
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="SpvInvestInfo.updateSpvInvestInfo"
						data-from="editForm"
						:data-model="formData"
						data-target="tableGrid"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>
	</div>
</template>
<script>
import ZyMixins from "@/pages/report/rhzy/zyMixin.js";
export default {
	name: "SpvInvestInfo",
	mixins: [ZyMixins],
	created() {
		this.getRegionList()
	},
};
</script>
