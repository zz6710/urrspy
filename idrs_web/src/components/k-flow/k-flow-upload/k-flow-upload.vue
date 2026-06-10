<template>
	<div v-if="enableAttachment">
		<el-row :gutter="10">
			<h4>附件列表</h4>
			<el-col :span="15">
				<k-grid
					ref="attachmentGrid"
					@data-row-select="fileSelect"
					data-url='/wf/wf/attachment/list.json'
					:data-display="false"
					:data-page-size="0"
					:data-params='{"processInstanceId": taskInfo.processInstanceId}'
				>
					<k-grid-column data-header="上传者" data-name="username"></k-grid-column>
					<k-grid-column data-header="文件名" data-name="originalFilename"></k-grid-column>
					<template slot="operate">
						<k-btn
							class="md-success md-just-icon md-simple"
							data-descript="下载"
							data-size="mini"
							:data-handler="download"
						>
							<md-icon>cloud_download</md-icon>
						</k-btn>
					</template>
				</k-grid>
			</el-col>
			<el-col :span="5" :push="1">
				<k-btn class="md-success" :data-handler="downloadAll">
					<md-icon md-src="/static/svg/download.svg" />全部下载
				</k-btn>
				<k-field-upload
					data-type="picture"
					:data-success="uploadFileSuccessHandler"
					:data-remove="removeFileSuccessHandler"
					:data-drag="false"
					:data-multiple="false"
					:data-auto-upload="true"
				/>
			</el-col>
		</el-row>
	</div>
</template>

<script>
  import Tools from "@/utils/tools.js";
  import Vue from "vue";
Vue.prototype.bus = new Vue()
export default {
	name: 'KFlowUpload',
	props: {
      taskInfo: {},
    },
	data() {
      return {
        fileSelectRow: {},
        attachmentList: [],
        enableAttachment: false,
        attachmentIds: '',
        taskFormComponentName: null,
        resultDict: {
          "1": "开始",
          "2": "驳回",
          "3": "拒绝",
          "4": "驳回到开始申请节点",
          "5": "通过",
          "6": "重新提交申请",
          "7": "审批中",
          "9": "完成"
        }
      };
    },
    created() {
      this.httpUtil
        .ajax({
          url: "/wf/wf/attachment/enableAttachment.json",
          params: {"processId": this.taskInfo.processId}
        })
        .then(res => {
          this.enableAttachment = res.data == 1 ? true : false;
        });
    },
    methods: {
      fileSelect(row) {
        this.fileSelectRow = row
      },
      download() {
        Tools.confirm(() => {
          Tools.download(this.fileSelectRow.url, this.fileSelectRow.originalFilename);
        }, '确定下载该文件吗？', null, 'info')
      },
      downloadAll() {
        let rows = this.$refs.attachmentGrid['list'];
        if (!rows || rows.length < 1) {
         return;
        }
        Tools.confirm(() => {
          let files = []
          rows.forEach(r=>{
            files.push(r.url);
            files.push(r.originalFilename)
          })
          Tools.batch_download(files.join(","), "全部附件.zip");
        }, '确定下载所有文件吗？', null, 'info')
      },
      removeFileSuccessHandler(file) {
        let index = this.attachmentList.indexOf(file);
        if (index > -1) {
          this.attachmentList.splice(index, 1);
        }
      },
      uploadFileSuccessHandler(file) {
        this.httpUtil
          .ajax({
            url: "/wf/wf/attachment/add.json",
            params: {
              "upload_name": file.response.returndata.upload_name,
              "upload_code": file.response.returndata.upload_code,
              "upload_path": file.response.returndata.upload_path
            }
          })
          .then(res => {
            this.attachmentList.push(file)
            this.attachmentIds += (res.data.id + ",")
            this.bus.$emit('change',this.attachmentIds)
            this.$refs.attachmentGrid._load(this.$refs.attachmentGrid.getCachedParams())
          });
      }
    }
}
</script>
