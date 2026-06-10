<template>
	<div class="py-page">
		<div>
			<k-form-search-customize
				data-model-name="DwdAsstBondComprat"
				data-label-width="80px"
				v-model="searchParam"
				data-target="dwdAsstBondCompratGrid"
			>
				<k-form-item label="主体名称">
					<k-field-text v-model="searchParam.comyName" />
				</k-form-item>
				<k-form-item label="评级机构">
					<k-field-text v-model="searchParam.ratingComp" />
				</k-form-item>
				<k-form-item label="评级日期">
					<k-field-date v-model="searchParam.anntDt" data-type="date" data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd" />
				</k-form-item>
				<k-form-item label="">
					<el-checkbox v-model="searchParam.multRating" style="margin: 0 20px 0 40px" true-label="1" false-label="0">筛选多主体评级数据</el-checkbox>
				</k-form-item>
			</k-form-search-customize>
		</div>
		<div class="py-page-container">
			<div class="table-top-btns">
				<div class="left">
					<k-btn
					slot="button"
					ref="reloadBtnRef"
					class="btn-custom-plain"
					data-functype="POPUP"
					data-target="handleTaskPopup"
					data-action="DwsProdTTRDBef.updateTaskAppQuery"
					loading-tip="正在重新生成报表，请稍后重试！"
				>
					<md-icon>cloud_download</md-icon>
					重新生成报表
				</k-btn>
				</div>
				<ReReport ref="reReportRef" :formData="formData" :menuId="menuId" :buttonName="buttonName" />
			</div>
			<k-grid ref="dwdAsstBondCompratGrid" @data-row-select="selectRow" data-action="DwdAsstBondComprat.findDwdAsstBondComprats">
				<k-grid-column data-header="主体名称" data-name="comyName" data-width="300px"></k-grid-column>
				<k-grid-column data-header="主体评级" data-name="rating"></k-grid-column>
				<k-grid-column data-header="评级机构" data-name="ratingComp" data-width="240px"></k-grid-column>
				<k-grid-column data-header="评级日期" data-name="anntDt"></k-grid-column>
				<k-grid-column data-header="更新人" data-name="summitUser"></k-grid-column>
				<k-grid-column data-header="更新日期" data-name="updateDate"></k-grid-column>
				<k-grid-column data-header="更新时间" data-name="updateTime"></k-grid-column>
				<template slot="operate" slot-scope="scope">
					<k-btn
						class="btn-custom-text"
						data-descript="修改主体评级"
						data-functype="POPUP"
						data-size="mini"
						data-target="editDwdAsstBondCompratPopup"
					>
						修改
					</k-btn>
					<k-btn
						class="btn-custom-text"
						data-functype="SUBMIT"
						data-action="DwdAsstBondComprat.deleteDwdAsstBondComprat"
						data-size="mini"
						data-type="danger"
						data-target="dwdAsstBondCompratGrid"
						:data-confirm="true"
					>
						删除
					</k-btn>
				</template>
			</k-grid>
		</div>
		<!--    修改主体评级弹出框   -->
		<k-popup ref="editDwdAsstBondCompratPopup" data-title="编辑">
			<k-form ref="editDwdAsstBondCompratForm" :data-col="2">
				<k-form-item label="主体名称" :class="[handleItemDiff('comyName')]">
					<k-field-text v-model="formData.comyName" :data-disabled="true" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="主体评级" :class="[handleItemDiff('rating')]">
					<k-field-text v-model="formData.rating" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="评级机构" :class="[handleItemDiff('ratingComp')]">
					<k-field-text v-model="formData.ratingComp" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="评级日期" :class="[handleItemDiff('anntDt')]">
					<k-field-date v-model="formData.anntDt" :data-allowblank="false" data-type="date" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="DwdAsstBondComprat.updateDwdAsstBondComprat"
						data-from="editDwdAsstBondCompratForm"
						:data-model="formData"
						data-target="dwdAsstBondCompratGrid"
						:handle-before="handleBefore"
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
						data-type="date"
						data-date-format="yyyy-MM-dd"
						data-value-format="yyyyMMdd"
						:data-allowblank="false"
					/>
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn class="btn-custom-primary" data-from="editForm" :data-handler="handleTaskApp">
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>
	</div>
</template>

<script>
import Tools from "@/utils/tools";
import ReReport from "@/utils/ReReport.vue";

export default {
	name: "DwdAsstBondComprat",
	components: {
      ReReport
   	},
	data() {
		return {
			formData: {},
			formDataCopy: {},
			selectRowData: {},
			searchParam: {},
			reloading: true,
			menuId: "M0623",
            buttonName: "重新生成报表",
		};
	},
	methods: {
		handleBefore() {
			if (this.formNoChangeCb()) {
				this.$refs.editDwdAsstBondCompratPopup.close();
				return false
			}
			return true
		},
		selectRow(row, column, event) {
			this.selectRowData = Object.assign({}, row);
			this.formData = Object.assign({}, row);
			this.formDataCopy = Object.assign({}, row);
		},
		handleTaskApp() {
			this.$refs.reReportRef.handleReports(this.formData.reportDate);
		},
		// handleTaskApp() {
		// 	if (this.$refs.handleTaskAppForm.validate()) {
		// 		this.$refs.reloadBtnRef.setIconStyle(0);
		// 		this.httpUtil
		// 			.comnUpdate({
		// 				action: "DwsProdTTRDBef.updateTaskAppQuery",
		// 				async: true,
		// 				params: {  reportDate: this.formData.dealDate,menuId: 'M0623',buttonName: '重新生成报表'},
		// 				successAlert: false,
		// 				dataAfterSuccess: (reData) => {
		// 					Tools.alertTime(reData.returnmsg, "success", 0);
		// 				},
		// 			})
		// 			.then((data) => {
		// 				this.$refs.reloadBtnRef.setIconStyle(1);
		// 			})
		// 			.catch((err) => {
		// 				console.log(err, "err");
		// 				this.$refs.reloadBtnRef.setIconStyle(1);
		// 			});
		// 		setTimeout(() => {
		// 			this.$refs.handleTaskPopup.close();
		// 		}, 300);
		// 	}
		// },
	},
};
</script>
