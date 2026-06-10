<template>
	<k-popup ref="handleTaskPopup" data-title="重新生成报表">
		<k-form ref="handleTaskAppForm" data-ui="element">
			<k-form-item label="数据日期" data-ui="element" data-input-width="500px">
				<k-field-date v-model="formData.reportDate" data-type="month" data-date-format="yyyy-MM" data-value-format="yyyy-MM" :data-allowblank="false" />
			</k-form-item>
			<k-form-footer data-align="center">
				<k-btn ref="createFormBtnRef" class="btn-custom-primary" data-from="editForm" :data-handler="handleTaskApp">
					<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
				</k-btn>
				<k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
			</k-form-footer>
		</k-form>
	</k-popup>
</template>
<script>
import Tools from "@/utils/tools";
import moment from "moment";
export default {
	props: {
		paraid: String,
		menuId: String,
		buttonName: String,
		actionPath:{type:String, default:"DwsProdTTRDBef.updateTaskApp"}
	},
	data() {
		return {
			formData: { reportDate: "" },
			reloading: true,
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
		popup() {
			this.$refs.handleTaskPopup.popup();
			this.formData.reportDate = "";
		},
		handleTaskApp() {
			if (!this.$refs.handleTaskAppForm.validate()) {
				return false;
			}
			this.$refs.createFormBtnRef.setIconStyle(0);
			this.httpUtil
			.comnUpdate({
				action: "DwsProdTTRDBef.updateTaskAppQuery",
				params: { reportDate: this.lastDay },
				successAlert: false,
				dataAfterSuccess: (reData)=>{
					this.httpUtil
					.comnUpdate({
						action: this.actionPath,
						params: { 
							reportDate: this.lastDay, 
							dealDate: this.lastDay, 
							actDt: this.lastDay, 
							paraid: this.paraid, 
							menuId: this.menuId, 
							buttonName: this.buttonName 
						},
						successAlert: false,
						dataAfterSuccess: (dataSuccess)=>{
							Tools.alertTime(dataSuccess.returnmsg, "success", 0);
						}
					})
					.then((data) => {
						this.$refs.createFormBtnRef.setIconStyle(1);
						this.$refs.handleTaskPopup.close();
					})
					.catch(() => {
						this.$refs.createFormBtnRef.setIconStyle(1);
					});
				}
			})
			.then(data => {
			   this.$refs.createFormBtnRef.setIconStyle(1);
            });
			return false;
		},
	},
};
</script>
