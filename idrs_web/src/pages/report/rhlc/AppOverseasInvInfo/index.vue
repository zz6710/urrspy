<template>
	<div class="py-page">
		<el-tabs v-model="selected.name" @tab-click="handleClick">
			<el-tab-pane v-for="tab in tabs" :label="tab.title" :name="tab.name" :key="tab.name">
			</el-tab-pane>
		</el-tabs>
		
			<div class="comn-form-search">
				<k-form-search-customize
					data-model-name="AppOverseasInvInfo2"
					data-label-width="80px"
					v-model="searchParam"
					data-target="appOverseasInvInfo2Grid"
					:handleConfirm="handleConfirm"
					ref="searchFormRef"
				>
					<k-form-item label="数据日期">
						<k-field-date
							v-model="searchParam.reportDate"
							data-type="date"
							data-date-format="yyyy-MM-dd"
							data-value-format="yyyyMMdd"
							:data-allowblank="false"
						/>
					</k-form-item>
				</k-form-search-customize>
				<div class="right">
					<k-btn slot="button" ref="downloadRef" class="btn-custom-plain" :data-handler="downloadFile">
						<md-icon>cloud_download</md-icon>
						导出
					</k-btn>
				</div>
			</div>
			<div class="py-page-container">
			<investInfo ref="investInfo" v-show="selected.name == 'investInfo'" />
			<investInfoIncome1 ref="investInfoIncome1" v-show="selected.name == 'investInfoIncome1'" />
			<investInfoIncome2 ref="investInfoIncome2" v-show="selected.name == 'investInfoIncome2'" />
			<investInfoIncome3 ref="investInfoIncome3" v-show="selected.name == 'investInfoIncome3'" />
		</div>
	</div>
</template>
<script>
import investInfo from "./investInfo";
import investInfoIncome1 from "./investInfoIncome1";
import investInfoIncome2 from "./investInfoIncome2";
import investInfoIncome3 from "./investInfoIncome3";

export default {
	name: "AppOverseasInvInfo",
	components: {
		investInfo,
		investInfoIncome1,
		investInfoIncome2,
		investInfoIncome3,
	},
	data() {
		return {
			searchParam: {},
			tabs: [
				{
					title: "资管公司境外投资情况表",
					name: "investInfo",
					param: {},
					grid: "appOverseasInvInfoGrid",
				},
				{
					title: "总体收益情况1",
					name: "investInfoIncome1",
					param: {},
					grid: "appOverseasInvInfo1Grid",
				},
				{
					title: "总体收益情况2",
					name: "investInfoIncome2",
					param: {},
					grid: "appOverseasInvInfo2Grid",
				},
				{
					title: "境外机构和人员情况",
					name: "investInfoIncome3",
					param: {},
					grid: "appOverseasInvInfo3Grid",
				},
			],
			selected: {
				name: "investInfo",
			},
			title: "",
		};
	},
	methods: {
		handleConfirm() {
			if (!this.formValid()) {
				return false;
			}
			this.tabs.forEach(item=>{
				if (item.name == "investInfoIncome3") {
					this.$refs[item.name].getSrc(this.searchParam.reportDate);
				} else {
					this.$refs[item.name].$refs[item.grid].load(this.searchParam);
				}
			})
			return false;
		},
		handleClick(a, b) {
			// if (this.searchParam.reportDate) {
			// 	const index = this.tabs.findIndex((item) => item.name == this.selected.name);
			// 	this.$nextTick(()=>{
			// 		this.$refs[this.selected.name].$refs[this.tabs[index].grid].load(this.searchParam);
			// 	})
			// }
		},
		formValid() {
			const valid = this.$refs.searchFormRef.$refs.searchForm.validate();
			if (!valid) {
				return false;
			}
			return true;
		},
		downloadFile() {
			if (!this.formValid()) {
				return false;
			}
			const index = this.tabs.findIndex((item) => item.name == this.selected.name);
			this.$refs.downloadRef.setIconStyle(0);
			this.$refs.downloadRef.setLoading(true);
			this.httpUtil.download({
				url: "/download/server/RptApp/JmReport/importTemplate/downloadInvestinfo.json",
				params: {
					reportDate: this.searchParam.reportDate,
					year: this.searchParam.reportDate.substring(0, 4),
					month: this.searchParam.reportDate.substring(4, 6),
					day: this.searchParam.reportDate.substring(6, 8),
					fileName: "资产管理机构境外投资情况表.xlsx",
				},
				callback: () => {
					this.$refs.downloadRef.setIconStyle(1);
					this.$refs.downloadRef.setLoading(false);
				},
			});
		},
	},
	mounted() {},
	created() {
		//接收路由中的title，决定激活哪个tab页
		this.title = this.$route.query.title;
		if (this.title) {
			this.$set(this.selected, "title", this.title);
		}
	},
};
</script>

<style scoped lang="scss" scoped>
.el-tabs {
	margin: 0 10px;
}
::v-deep {
	.el-tabs__header {
		margin-bottom: 10px;
	}
	.el-tabs__item {
		background-color: white;
	}
}
.comn-form-search {
	display: flex;
	.md-card {
		width: auto;
		box-shadow: none;
		margin-bottom: 0;
	}
	.right {
		.md-button {
			margin: 10px 0 0;
		}
	}
}
.py-page {
	background: #fff;
	.py-page-container {
		padding: 0;
	}
}
</style>
