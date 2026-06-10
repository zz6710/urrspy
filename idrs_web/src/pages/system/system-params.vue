<template>
	<div class="py-page">
		<div class="py-page-container">
			<el-tabs v-model="selected.title" @tab-click="handleClick">
				<el-tab-pane v-for="tab in tabs" :label="tab.title" :name="tab.title" :key="tab.title">
					<component :is="tab.name" :param="tab.param"></component>
				</el-tab-pane>
			</el-tabs>
		</div>
	</div>
</template>
<script>
import sysParamTab from "./tabSysParam";
import businessParamTab from "./tabBusinessParam";
import portParamTab from "./tabPortParam";
import registerParam from "./tabRegisterParam";

export default {
	name: "business-params",
	components: {
		sysParamTab,
		businessParamTab,
		portParamTab,
		registerParam,
	},
	data() {
		return {
			tabs: [
				{
					title: "系统参数",
					name: "sysParamTab",
					param: {},
					grid: "SystemParamGrid",
				},
				{
					title: "业务参数",
					name: "businessParamTab",
					param: {},
					grid: "BusinessParamGrid",
				},
				{
					title: "技术参数",
					name: "portParamTab",
					param: {},
					grid: "PortParamGrid",
				},
				{
					title: "中债直联参数",
					name: "registerParam",
					param: {},
					grid: "RegisterParamGrid",
				},
			],
			selected: {
				title: "系统参数",
			},
			title: "",
		};
	},
	methods: {
		handleClick(a, b) {
			this.$nextTick(() => {
				console.log(this.selected.title, "vvv", a, b);
				const ref = this.tabs.find((item) => item.title == this.selected.title);
				console.log(ref, a.$children[0], a.$children[0].$refs[ref.grid], "====");
				if (ref) {
					const refGrid = a.$children[0].$refs[ref.grid];
					console.log(refGrid, "refGrid");
					if (refGrid) {
						refGrid.onPageSize();
					}
				}
			});
		},
	},
	created() {
		//接收路由中的title，决定激活哪个tab页
		this.title = this.$route.query.title;
		if (this.title != "" && this.title != undefined) {
			this.$set(this.selected, "title", this.title);
		}
	},
};
</script>

<style scoped lang="scss" scoped>
::v-deep .el-tabs__item {
	background-color: white;
}
</style>
