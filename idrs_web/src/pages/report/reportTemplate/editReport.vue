<template>
	<iframe :src="src" frameborder="0"></iframe>
</template>
<script>
export default {
	data() {
		return {
			//urlBase: process.env.NODE_ENV == "development" ? "http://127.0.0.1:38890" : getURL().baseUrl,
			src: "",
		};
	},
	watch: {
		"$route.query.id": {
			handler(v) {
				if (v) {
					this.src = getURL().rptUrl + "jmreport/index/" + v;
				}
			},
			immediate: true,
		},
	},
};
</script>
<style lang="scss" scoped>
iframe {
	width: 100%;
	height: 100%;
}
</style>
