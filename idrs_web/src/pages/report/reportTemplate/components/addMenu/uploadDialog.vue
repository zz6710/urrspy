<template>
	<div>
		<div class="panel-body">
			<k-form
				id="M8430F010"
				name="M8430F010"
				class="k-form col2"
				action="uploadXML.action"
				method="post"
				enctype="multipart/form-data"
			>
				<k-form-item>
					<k-field-upload
						ref="uploadRef"
						label="附件信息"
						data-type="file"
						:data-limit="1"
						:data-error="onSubmitError"
						:dataChange="onUploadChange"
						:data-success="onSubmitSuccess"
						data-accept=".pdf"
						:data-auto-upload="false"
						data-upload-url="/upload/server/WorkflowServer/uploadFlow/upload.json"
					>
					</k-field-upload>
				</k-form-item>
			</k-form>
		</div>
		<k-form-footer data-align="center">
			<k-btn
				class="btn-custom-primary"
				:data-handler="upHandler"
				data-functype="SUBMIT"
				data-from="#M8430F010"
				data-descript="上传xml文件"
				id="uploadButton"
			>
				<md-icon md-src="/static/svg/confirm.svg" />上传
			</k-btn>
			<k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg" />取消</k-btn>
		</k-form-footer>
	</div>
</template>
<script>
export default {
	data() {
		return {
			fileList: [],
		};
	},
	methods: {
		onSubmitError() {
			this.$refs.uploadRef.doReset();
		},
		onSubmitSuccess() {
			this.$refs.uploadRef.doReset();
			this.$refs.M8606Grid.load();
		},
		onUploadChange(file, fileList) {
			this.fileList = fileList;
		},
		upHandler() {
			console.log(this.fileList, "fileList");
			if (!this.fileList.length) {
				this.$message({
					type: "warning",
					message: "请上传文件",
				});
			} else if (!this.fileList[0].name.endsWith(".xml")) {
				this.$message({
					type: "warning",
					message: "只能上传xml文件",
				});
			} else {
				this.fileData = new FormData();
				this.$refs.uploadRef.upload({
					menuid: this.formData.menuid,
				});
			}
		},
	},
};
</script>