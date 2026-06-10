<template>
  <div>
    <el-row :gutter="10">
      <el-col :span="15">
        <k-grid ref="fileUploadGrid" @data-row-select="fileSelect" :data-url="'wf/fileUpload/list/'+taskInfo.procInsId+'.json'" :data-display="false" :data-page-size="0" :data-params='{"processInstanceId": taskInfo.processInstanceId}'>
          <k-grid-column data-header="附件类型" >
            <template slot-scope="scope">
					{{ renderType(scope.row.row) }}
            </template>
          </k-grid-column>
          <k-grid-column data-header="上传者" data-name="uploadUserName"></k-grid-column>
          <k-grid-column data-header="附件名" data-name="name"></k-grid-column>
          <k-grid-column data-header="上传时间" data-name="time"></k-grid-column>
          <template slot="operate" slot-scope="scope">
            <k-btn class="md-success md-just-icon md-simple" data-descript="下载" data-size="mini" :data-handler="download">
              <md-icon>cloud_download</md-icon>
            </k-btn>
            <k-btn class="md-success md-just-icon md-simple" data-descript="支持预览docx、txt、pdf文件" :data-disabled="previewDisabled(scope.row.row)" data-size="mini" @click.native.stop="preview(scope.row.row)">
              <md-icon md-src="/static/svg/log.svg" />
            </k-btn>
          </template>
        </k-grid>
      </el-col>
      <el-col :span="5" :push="1">
        <div style="padding-top:28px">
          <k-btn class="md-success" :data-handler="downloadAll">
            <md-icon md-src="/static/svg/download.svg" />全部下载
          </k-btn>
          <k-field-upload data-type="file" :data-success="uploadFileSuccessHandler" :data-remove="removeFileSuccessHandler" :data-drag="false" :data-multiple="false" :data-auto-upload="true" />
        </div>
      </el-col>
    </el-row>

    <k-popup data-title="文件预览" ref="viewPopup" :data-fullscreen="true">
      <FlowFileView :file="fileSelectRow"></FlowFileView>
    </k-popup>
  </div>
</template>

<script>
import Tools from "@/utils/tools.js";
import eventBus from "@/utils/eventBus";
import FlowFileView from "./FlowFileView.vue";
import wfStatus from "../enum/enum.js";

export default {
	name: "FlowFileUpload",
	components: { FlowFileView },
	props: {
		taskInfo: {},
	},
	data() {
		return {
			id: "",
			fileSelectRow: {},
			fileUploadList: [],
			previewFileType: [".docx", ".pdf", ".txt"],
			wfTaskStatus: Object.values(wfStatus.attachmentType),
		};
	},
	created() {},
	methods: {
		fileSelect(row) {
			this.fileSelectRow = row;
		},
		previewDisabled(row) {
			let suffix = row.name.substr(row.name.lastIndexOf("."));
			let index = this.previewFileType.find((k) => k == suffix);
			return !index;
		},
		preview(row) {
			this.fileSelectRow = row;
			this.$refs.viewPopup.popup();
		},
		download() {
			Tools.confirm(
				() => {
					Tools.download(this.fileSelectRow.url, this.fileSelectRow.name);
				},
				"确定下载该文件吗？",
				null,
				"info"
			);
		},
		downloadAll() {
			let rows = this.$refs.fileUploadGrid["list"];
			if (!rows || rows.length < 1) {
				return;
			}
			Tools.confirm(
				() => {
					let files = [];
					rows.forEach((r) => {
						files.push(r.url);
						files.push(r.name);
					});
					Tools.batch_download(files.join(","), "全部附件.zip");
				},
				"确定下载所有文件吗？",
				null,
				"info"
			);
		},
		removeFileSuccessHandler(file) {
			let uploadCode = file.response.returndata.upload_code;
			this.fileUploadList = this.fileUploadList.filter((t) => t.uploadCode != uploadCode);
			eventBus.$emit("wfFileChange", this.fileUploadList);
		},
		uploadFileSuccessHandler(file) {
			let uploadFile = {
				uploadName: file.response.returndata.upload_name,
				uploadCode: file.response.returndata.upload_code,
				uploadPath: file.response.returndata.upload_path,
			};
			this.fileUploadList.push(uploadFile);
			eventBus.$emit("wfFileChange", this.fileUploadList);
		},
		renderType(row) {
			let arr = this.wfTaskStatus.filter((t) => t.value == row.type);
			if (arr && arr.length > 0) {
				return arr[0].label;
			}
		},
	},
};
</script>
