<template>
  <div>
    <iframe class="iframe" frameborder="no" border="0" :src="pdfUrl"></iframe>
  </div>
</template>
  
<script>
export default {
	name: "FlowFileView",
	data() {
		return {
			pdfUrl: "",
		};
	},
	props: {
		file: {
			type: Object,
		},
	},
	methods: {
		viewPdf(file) {
			if (!file) {
				return;
			}
			let pdfFileName = file.name.substr(0, file.name.lastIndexOf(".")) + ".pdf";
			this.httpUtil.download(
				{
					url: "/download/server/WorkflowServer/fileUpload/preview/pdf/" + file.id + ".json",
					callback: (res) => {
						const binaryData = [];
						binaryData.push(res.data);
						const url = window.URL.createObjectURL(new Blob(binaryData, { type: "application/pdf" }));
						this.pdfUrl = "/static/pdfjs/web/viewer.html?file=" + encodeURIComponent(url);
					},
				},
				pdfFileName
			);
		},
	},
	watch: {},
	mounted() {
		this.viewPdf(this.file);
	},
};
</script>
  
<style scoped>
.iframe {
	display: block; 
	background: #000;
	border: none; 
	height: 81vh; 
	width: 100%;
}
</style>
  