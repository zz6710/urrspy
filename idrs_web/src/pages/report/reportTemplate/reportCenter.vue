<template>
	<iframe :src="src" frameborder="0"></iframe>
</template>
<script>
export default {
	data() {
		return {
			src: getURL().rptUrl+"jmreport/list",
		};
	},
};
</script>
<style lang="scss" scoped>
iframe {
	width: 100%;
	height: 100%;
}
</style>
