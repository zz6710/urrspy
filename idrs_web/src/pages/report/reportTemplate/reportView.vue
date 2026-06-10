<template>
	<iframe :src="src" frameborder="0"></iframe>
</template>
<script>
export default {
	data() {
		return {
			//urlBase: process.env.NODE_ENV == "development" ? "http://127.0.0.1:38890" : location.host,
			src: "",
		};
	},
	watch: {
		"$route.params.id": {
			handler(v) {
				if (v) {
					this.getJmreportId(v);
				}
			},
			immediate: true,
		},
	},
	methods: {
		getJmreportId(id) {
			this.httpUtil
				.comnQuery({
					action: "JmSqlDictInfo.findJmSqlDictInfo",
					params: { menuid: id },
				})
				.then((data) => {
					this.src = getURL().rptUrl + "jmreport/view/" + data.rows[0].jimuReportId;
				});
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
