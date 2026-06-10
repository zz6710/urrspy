<template>
	<iframe :src="iframeUrl" frameborder="0"></iframe>
</template>

<script>
export default {
	data() {
		return {
			iframeUrl: "",
		};
	},
	methods: {
		getSrc(reportDate) {
			const { baseUrl } = getURL();
			this.iframeUrl = baseUrl + "jmreport/view/M07ZZS0030?reportDate=" + reportDate;
		},	
	},
};
</script>
