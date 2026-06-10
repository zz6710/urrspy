<template>
	<el-tooltip :placement="dataPlacement" :trigger="dataTrigger">
		<div class="content" slot="content">
			{{ dataContent }}
		</div>
		<slot>
			<i class="el-icon-info"></i>
		</slot>
	</el-tooltip>
</template>
<script>
export default {
	name: "KTooltip",
	props: {
		dataPlacement: {
			type: String,
			default: "top",
		},
		dataTitle: {
			type: String,
			default: "",
		},
		dataWidth: {
			type: String,
			default: "200",
		},
		dataTrigger: {
			type: String,
			default: "hover",
		},
		dataContent: {
			type: String,
			default: "",
		},
	},
};
</script>
<style lang="scss" scoped>
.el-icon-info {
	cursor: pointer;
}
</style>
