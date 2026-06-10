<template>
	<div class="py-page">
		<div>
			<k-form-search-customize data-model-name="DwsZyConcentrationCust" v-model="searchParam" data-target="tableGrid">
				<k-form-item label="数据日期">
					<k-field-date v-model="searchParam.actDt" data-type="month" data-date-format="yyyy-MM" data-value-format="yyyyMM" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="报表名称">
					<k-field-text v-model="searchParam.stype" />
				</k-form-item>
				<k-form-item label="客户名称">
					<k-field-text v-model="searchParam.custName" />
				</k-form-item>
				<k-form-item label="行业名称">
					<k-field-text v-model="searchParam.induName" />
				</k-form-item>
				<k-form-item label="国家或地区名称">
					<k-field-text v-model="searchParam.regionName" />
				</k-form-item>
			</k-form-search-customize>
		</div>
		<div class="py-page-container">
			<div class="table-top-btns">
				<div class="left">
					<k-btn slot="button" ref="uploadBtnRef" :load-disabled="false" data-functype="POPUP" class="btn-custom-plain" data-target="uploadPopup"> <md-icon>cloud_upload</md-icon>导入 </k-btn>
					<k-btn
						slot="button"
						class="btn-custom-plain"
						data-functype="EXPORT"
						data-export-dict="false"
						data-target="tableGrid"
						data-export-name="客户集中度排序"
					>
						<md-icon>cloud_download</md-icon>导出
					</k-btn>
					<k-btn slot="button" ref="reloadBtnRef" class="btn-custom-plain" data-functype="POPUP" data-target="handleTaskPopup" loading-tip="正在重新生成报表，请稍后重试！">
                        <md-icon>cloud_download</md-icon>重新生成报表
				    </k-btn>
				</div>
				<ReReport ref="reReportRef" :formData="formData" :menuId="menuId" :buttonName="buttonName" />
			</div>
			<k-grid
				ref="tableGrid"
				@data-row-select="selectRow"
				data-action="DwsZyConcentrationCust.findDwsZyConcentrationCusts"
				:data-operate-column="false"
				:data-autoload="false"
			>
			    <k-grid-column data-header="数据日期" data-name="actDt" data-export="false"></k-grid-column>
				<k-grid-column data-header="序号" data-name="xh"></k-grid-column>
				<k-grid-column data-header="报表名称" data-name="stype"></k-grid-column>
				<k-grid-column data-header="客户名称" data-name="custName"></k-grid-column>
				<k-grid-column data-header="行业名称" data-name="induName"></k-grid-column>
				<k-grid-column data-header="国家或地区名称" data-name="regionName"></k-grid-column>
			</k-grid>
		</div>

		<k-popup ref="uploadPopup" data-title="导入">
			<k-form ref="addForm" data-ui="element">
				<k-form-item label="数据日期" data-ui="element">
					<k-field-date v-model="formData.dealDate" data-type="month" data-date-format="yyyy-MM" data-value-format="yyyy-MM" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="附件" data-ui="element" data-input-width="500px">
					<k-field-excel-upload
						data-type="file"
						ref="uploadRef"
						:data-multiple="false"
						:data-limit="1"
						data-accept=".xlsx,.xls"
						:data-error="onSubmitError"
						:data-success="onSubmitSuccess"
						:data-auto-upload="false"
						data-upload-url="upload/server/DpsApp/concentration/excelUpload.json"
					>
					</k-field-excel-upload>
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						ref="submitBtn"
						:data-auto-upload="false"
						data-from="addForm"
						:data-handler="submitUploadParam"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>

		<k-popup ref="handleTaskPopup" data-title="重新生成报表">
			<k-form ref="handleTaskAppForm" data-ui="element">
				<k-form-item label="数据日期" data-ui="element" data-input-width="500px">
					<k-field-date
						v-model="formData.reportDate"
						data-type="month"
						data-date-format="yyyy-MM"
						data-value-format="yyyy-MM"
						:data-allowblank="false"
					/>
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn class="btn-custom-primary" data-from="editForm" :data-handler="handleTaskApp">
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE">
						<md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
					</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>
	</div>
</template>

<script>
import Tools from "@/utils/tools";
import ProdMixin from "@/pages/pms/subject/mixins/prodMixin.js";
import moment from "moment";
import ReReport from "@/utils/ReReport.vue";
export default {
	name: "DwsZyConcentrationCust",
	components: {
      ReReport
    },
	mixins: [ProdMixin],
	data() {
		return {
			menuId: "M0620",
            buttonName: "重新生成报表",
		};
	},
	computed: {
		lastDay() {
        if (this.formData.reportDate) {
          return moment([this.formData.reportDate.split("-")[0], this.formData.reportDate.split("-")[1] - 1])
            .endOf("month")
            .format("YYYYMMDD");
        }
        return "";
      },
    },
    methods: {
		handleTaskApp() {
        this.$refs.reReportRef.handleReports(this.lastDay);
      },
	}
};
</script>
