<template>
	<div class="py-page">
		<div>
			<k-form-search-customize
				data-model-name="DwsAssetA1413DepStruc"
				data-label-width="80px"
				v-model="searchParam"
				data-target="dwsAssetA1413DepStrucGrid"
			>
				<k-form-item label="数据日期">
					<k-field-date v-model="searchParam.actDt" data-type="month" data-date-format="yyyy-MM" data-value-format="yyyyMM" />
				</k-form-item>
			</k-form-search-customize>
		</div>
		<div class="py-page-container">
			<div class="table-top-btns">
				<div class="left">
					<k-btn
						class="btn-custom-primary"
						data-functype="POPUP"
						:data-handler="() => (this.formData = {})"
						data-target="addDwsAssetA1413DepStrucPopup"
					>
						<md-icon md-src="/static/svg/add.svg" />新增</k-btn
					>
					<!-- <k-btn class="btn-custom-plain" data-functype="POPUP" data-target="handleTaskPopup" :data-handler="() => (this.formData = {})"
						>重新生成报表</k-btn
					> -->
					<k-btn
						slot="button"
						ref="reloadBtnRef"
						class="btn-custom-plain"
						data-functype="POPUP"
						data-target="handleTaskPopup"
						:load-disabled="false"
						data-action="DwsProdTTRDBef.updateTaskAppQuery"
						loading-tip="正在处理中，请稍后重试！"
					>
						<md-icon>cloud_download</md-icon>
						重新生成报表
					</k-btn>
				</div>
				<ReReport ref="reReportRef" :formData="formData" :menuId="menuId" :buttonName="buttonName" />
			</div>
			<k-grid
				ref="dwsAssetA1413DepStrucGrid"
				@data-row-select="selectRow"
				data-action="DwsAssetA1413DepStruc.findDwsAssetA1413DepStrucs"
				:data-autoload="false"
			>
				<k-grid-column data-header="数据日期" data-name="actDt"></k-grid-column>
				<k-grid-column data-header="单位和个人定期及其他存款_1年以内（含）-余额（元）" data-name="orgOne"></k-grid-column>
				<k-grid-column data-header="单位和个人定期及其他存款_1至2年（含）-余额（元）" data-name="orgOneTwo"></k-grid-column>
				<k-grid-column data-header="单位和个人定期及其他存款_2至3年（含）-余额（元）" data-name="orgTwoThr"></k-grid-column>
				<k-grid-column data-header="单位和个人定期及其他存款_3年以上-余额（元）" data-name="orgThr"></k-grid-column>
				<k-grid-column data-header="境内非存款类金融机构定期及其他存款_1年以内（含）-余额（元）" data-name="domOne"></k-grid-column>
				<k-grid-column data-header="境内非存款类金融机构定期及其他存款_1至2年（含）-余额（元）" data-name="domOneTwo"></k-grid-column>
				<k-grid-column data-header="境内非存款类金融机构定期及其他存款_2至3年（含）-余额（元）" data-name="domTwoThr"></k-grid-column>
				<k-grid-column data-header="境内非存款类金融机构定期及其他存款_3年以上-余额（元）" data-name="domThr"></k-grid-column>
				<k-grid-column data-header="现金管理类理财存款-余额（元）" data-name="craDep"></k-grid-column>
				<k-grid-column data-header="现金管理类理财份额投资-余额（元）" data-name="craInv"></k-grid-column>
				<template slot="operate" slot-scope="scope">
					<k-btn
						class="btn-custom-text"
						data-descript="修改A1413存款期限结构及相关业务情况补录表"
						data-functype="POPUP"
						data-size="mini"
						data-target="editDwsAssetA1413DepStrucPopup"
					>
						修改
					</k-btn>
					<k-btn
						class="btn-custom-text"
						data-functype="SUBMIT"
						data-action="DwsAssetA1413DepStruc.deleteDwsAssetA1413DepStruc"
						data-size="mini"
						data-type="danger"
						data-target="dwsAssetA1413DepStrucGrid"
						:data-confirm="true"
						data-descript="删除A1413存款期限结构及相关业务情况补录表"
					>
						删除
					</k-btn>
				</template>
			</k-grid>
		</div>

		<!--    添加A1413存款期限结构及相关业务情况补录表弹出框   -->
		<k-popup ref="addDwsAssetA1413DepStrucPopup" data-title="新增">
			<k-form ref="addDwsAssetA1413DepStrucForm" :data-col="2" data-label-width="280px">
				<k-form-item label="数据日期">
					<k-field-date v-model="formData.actDt" data-type="month" data-date-format="yyyy-MM" data-value-format="yyyy-MM" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="单位和个人定期及其他存款_1年以内（含）-余额（元）">
					<k-field-text v-model="formData.orgOne" :data-allowblank="false" data-validate-type="money" />
				</k-form-item>
				<k-form-item label="单位和个人定期及其他存款_1至2年（含）-余额（元）">
					<k-field-text v-model="formData.orgOneTwo" :data-allowblank="false" data-validate-type="money" />
				</k-form-item>
				<k-form-item label="单位和个人定期及其他存款_2至3年（含）-余额（元）">
					<k-field-text v-model="formData.orgTwoThr" :data-allowblank="false" data-validate-type="money" />
				</k-form-item>
				<k-form-item label="单位和个人定期及其他存款_3年以上-余额（元）">
					<k-field-text v-model="formData.orgThr" :data-allowblank="false" data-validate-type="money" />
				</k-form-item>
				<k-form-item label="境内非存款类金融机构定期及其他存款_1年以内（含）-余额（元）">
					<k-field-text v-model="formData.domOne" :data-allowblank="false" data-validate-type="money" />
				</k-form-item>
				<k-form-item label="境内非存款类金融机构定期及其他存款_1至2年（含）-余额（元）">
					<k-field-text v-model="formData.domOneTwo" :data-allowblank="false" data-validate-type="money" />
				</k-form-item>
				<k-form-item label="境内非存款类金融机构定期及其他存款_2至3年（含）-余额（元）">
					<k-field-text v-model="formData.domTwoThr" :data-allowblank="false" data-validate-type="money" />
				</k-form-item>
				<k-form-item label="境内非存款类金融机构定期及其他存款_3年以上-余额（元）">
					<k-field-text v-model="formData.domThr" :data-allowblank="false" data-validate-type="money" />
				</k-form-item>
				<k-form-item label="现金管理类理财存款-余额（元）">
					<k-field-text v-model="formData.craDep" :data-allowblank="false" data-validate-type="money" />
				</k-form-item>
				<k-form-item label="现金管理类理财份额投资-余额（元）">
					<k-field-text v-model="formData.craInv" :data-allowblank="false" data-validate-type="money" />
				</k-form-item>

				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="DwsAssetA1413DepStruc.addDwsAssetA1413DepStruc"
						data-from="addDwsAssetA1413DepStrucForm"
						:data-model="formDataTransfer"
						data-target="dwsAssetA1413DepStrucGrid"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>

		<!--    修改A1413存款期限结构及相关业务情况补录表弹出框   -->
		<k-popup ref="editDwsAssetA1413DepStrucPopup" data-title="修改">
			<k-form ref="editDwsAssetA1413DepStrucForm" :data-col="2" data-label-width="280px">
				<k-form-item label="数据日期" :class="[handleItemDiff('actDt')]">
					<k-field-date v-model="formData.actDt" :data-allowblank="false" data-disabled="true" />
				</k-form-item>
				<k-form-item label="单位和个人定期及其他存款_1年以内（含）-余额（元）" :class="[handleItemDiff('orgOne')]">
					<k-field-text v-model="formData.orgOne" :data-allowblank="false" data-validate-type="money" />
				</k-form-item>
				<k-form-item label="单位和个人定期及其他存款_1至2年（含）-余额（元）" :class="[handleItemDiff('orgOneTwo')]">
					<k-field-text v-model="formData.orgOneTwo" :data-allowblank="false" data-validate-type="money" />
				</k-form-item>
				<k-form-item label="单位和个人定期及其他存款_2至3年（含）-余额（元）" :class="[handleItemDiff('orgTwoThr')]">
					<k-field-text v-model="formData.orgTwoThr" :data-allowblank="false" data-validate-type="money" />
				</k-form-item>
				<k-form-item label="单位和个人定期及其他存款_3年以上-余额（元）" :class="[handleItemDiff('orgThr')]">
					<k-field-text v-model="formData.orgThr" :data-allowblank="false" data-validate-type="money" />
				</k-form-item>
				<k-form-item label="境内非存款类金融机构定期及其他存款_1年以内（含）-余额（元）" :class="[handleItemDiff('domOne')]">
					<k-field-text v-model="formData.domOne" :data-allowblank="false" data-validate-type="money" />
				</k-form-item>
				<k-form-item label="境内非存款类金融机构定期及其他存款_1至2年（含）-余额（元）" :class="[handleItemDiff('domOneTwo')]">
					<k-field-text v-model="formData.domOneTwo" :data-allowblank="false" data-validate-type="money" />
				</k-form-item>
				<k-form-item label="境内非存款类金融机构定期及其他存款_2至3年（含）-余额（元）" :class="[handleItemDiff('domTwoThr')]">
					<k-field-text v-model="formData.domTwoThr" :data-allowblank="false" data-validate-type="money" />
				</k-form-item>
				<k-form-item label="境内非存款类金融机构定期及其他存款_3年以上-余额（元）" :class="[handleItemDiff('domThr')]">
					<k-field-text v-model="formData.domThr" :data-allowblank="false" data-validate-type="money" />
				</k-form-item>
				<k-form-item label="现金管理类理财存款-余额（元）" :class="[handleItemDiff('craDep')]">
					<k-field-text v-model="formData.craDep" :data-allowblank="false" data-validate-type="money" />
				</k-form-item>
				<k-form-item label="现金管理类理财份额投资-余额（元）" :class="[handleItemDiff('craInv')]">
					<k-field-text v-model="formData.craInv" :data-allowblank="false" data-validate-type="money" />
				</k-form-item>

				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="DwsAssetA1413DepStruc.updateDwsAssetA1413DepStruc"
						data-from="editDwsAssetA1413DepStrucForm"
						:data-model="formData"
						data-target="dwsAssetA1413DepStrucGrid"
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
				<k-form-item label="数据日期" data-ui="element" data-input-height="500px">
					<k-field-date v-model="formData.actDt" data-type="month" data-date-format="yyyy-MM" data-value-format="yyyy-MM" :data-allowblank="false" />
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
import moment from "moment";
import Tools from "@/utils/tools.js";
import ReReport from "@/utils/ReReport.vue";
export default {
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
			menuId: "M061603",
            buttonName: "重新生成报表",
		};
	},
	computed: {
		lastDay() {
			if (this.formData.actDt) {
				return moment([this.formData.actDt.split("-")[0], this.formData.actDt.split("-")[1] - 1])
					.endOf("month")
					.format("YYYYMMDD");
			}
			return "";
		},
		formDataTransfer() {
			return {
				...this.formData,
				actDt: this.lastDay,
			};
		},
	},
	methods: {
		handleBefore() {
			if (this.formNoChangeCb()) {
				this.$refs.editDwsAssetA1413DepStrucPopup.close();
				return false;
			}
			return true;
		},
		selectRow(row, column, event) {
			this.selectRowData = Object.assign({}, row);
			this.formData = Object.assign({}, row);
			this.formDataCopy = Object.assign({}, row);
		},
		handleTaskApp() {
          this.$refs.reReportRef.handleReports(this.lastDay);
        },
		// handleTaskApp() {
		// 	if (this.$refs.handleTaskAppForm.validate()) {
		// 		this.$refs.reloadBtnRef.setIconStyle(0);
		// 		this.httpUtil
		// 			.comnUpdate({
		// 				action: "DwsProdTTRDBef.updateTaskAppQuery",
		// 				async: true,
		// 				params: { menuId: "M061603", buttonName: "重新生成报表", reportDate: this.lastDay },
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
