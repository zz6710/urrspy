<template>
	<div class="py-page">
		<div>
			<k-form-search-customize data-model-name="ReportConvert" v-model="searchParam" data-target="tableGrid">
				<k-form-item label="报表名称">
					<k-field-select
						v-model="searchParam.reportId"
						data-action="ReportConvert.findReportTemplateList"
						style="width: 100%"
						data-display-field="reportId,reportName"
						data-value-field="reportId"
						:data-remote="true"
						:data-remote-paging="true"
					/>
				</k-form-item>
				<k-form-item label="操作日期">
					<k-field-date v-model="searchParam.crtDate" data-type="date" data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd" />
				</k-form-item>
                <k-form-item label="操作员名称">
					<k-field-text v-model="searchParam.operatername"/>
				</k-form-item>
			</k-form-search-customize>
		</div>
		<div class="py-page-container">
			<div class="table-top-btns">
				<div class="left">
					<k-btn slot="button" ref="uploadBtnRef" data-functype="POPUP" class="btn-custom-plain" data-target="addPopup" :load-disabled="false">
						<md-icon>cloud_upload</md-icon>
						模板转换
					</k-btn>				
				</div>
			</div>
			<k-grid
				ref="tableGrid"
				@data-row-select="selectRow"
				@data-select-change="handleChange"
				data-action="ReportConvert.findReportConvert"
			>
                <k-grid-column data-header="ID" data-name="id"></k-grid-column>
				<k-grid-column data-header="报表名称" data-name="reportName"></k-grid-column>
				<k-grid-column data-header="操作日期" data-name="crtDate"></k-grid-column>
				<k-grid-column data-header="操作时间" data-name="crtTime"></k-grid-column>
				<k-grid-column data-header="操作员名称" data-name="operatername"></k-grid-column>
				<k-grid-column data-header="操作员编号" data-name="operaterno"></k-grid-column>
                <k-grid-column data-header="上传文件名" data-name="upFilename"></k-grid-column>
                <k-grid-column data-header="转换后文件名" data-name="convertFilename"></k-grid-column>
                <template slot="operate" slot-scope="scope">
                    <k-btn class="btn-custom-text" data-descript="下载模板转换信息" data-functype="DOWNLOAD" data-size="mini"
                  data-url="/download/server/DpsApp/reportconvert/exportConvertFile.json" v-model="scope.row.row">
                        下载
                    </k-btn>
                </template>
			</k-grid>
		</div>

		<k-popup ref="addPopup" data-title="模板转换上传">
			<k-form ref="addForm" data-ui="element" v-loading="loading">
                <k-form-item label="报表名称">
					<k-field-select
						v-model="formData.reportId"
						data-action="ReportConvert.findReportTemplateList"
						style="width: 100%"
						data-display-field="reportId,reportName"
						data-value-field="reportId"
						:data-remote="true"
						:data-remote-paging="true"
                        :data-allowblank="false"
					/>
				</k-form-item>
                <k-form-item label="报表数据导入" data-ui="element" data-input-width="500px">
                    <k-field-excel-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit='1'
                        data-accept=".xlsx,.xls"
                        :data-error="onSubmitError" :data-success="onSubmitSuccess"
                        :data-auto-upload="false"
						:data-http-request="httpRequestUploadDownload"
                        data-upload-url="uploadDownload/server/DpsApp/reportconvert/reportConvertImportDownload.json">
                    </k-field-excel-upload>
                </k-form-item>
                <k-form-footer data-align="center">
                    <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="tableGrid" ref="submitBtn" 
                        :data-auto-upload="false" data-from="addForm" :data-handler="submitUploadParam">
												<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
                    </k-btn>
                    <k-btn class="btn-custom-plain" data-functype="CLOSE">
											<md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
										</k-btn>
                </k-form-footer>				
			</k-form>
		</k-popup>
	</div>
</template>

<script>
import httpUtil from "../../../frame/httpUtil";
import Tools from '@/utils/tools.js';
export default {
	name: "ReportConvert",
	data() {
		return {
			formData: {},
			selectRowData: {},
			searchParam: {
                reportId: "",
				operatername: "",
				crtDate: ""
			},
			loading: false
		};
	},
	methods: {
		onSubmitSuccess() {
			this.$refs.tableGrid.load(this.searchParam);
			this.$refs.uploadBtnRef.setIconStyle(1);
		},
		onSubmitError() {
			this.$refs.uploadBtnRef.setIconStyle(1);
		},
		handleChange(row) {
			this.selectRowData = row;
		},
		httpRequestUploadDownload(data) {
            let file = data.file;
			const form = new FormData();
			form.append("file", file);
			form.append("reportId", this.formData.reportId);
			let reqUrl = this.$refs.uploadRef.dataUploadUrl;
			this.httpUtil.uploadDownload(reqUrl, form).then(resp =>{
				let cd = resp.headers['content-disposition'];
				if (!cd) {
					Tools.alertTime("报表模板转换失败", "danger", 0);
					this.$refs.uploadBtnRef.setIconStyle(1);
					return
				}
				let filename = cd.substring(cd.indexOf('=') + 1);
				const blob = new Blob([resp.data]);
				//非IE浏览器下载
				const elink = document.createElement("a");
				elink.download = decodeURI(filename); //中文转码
				elink.style.display = "none";
				elink.href = URL.createObjectURL(blob);
				document.body.appendChild(elink);
				elink.click();
				URL.revokeObjectURL(elink.href); // 释放URL对象
				document.body.removeChild(elink); //移除定义的元素对象
				Tools.alertTime("报表模板转换成功", "success", 0);				
				this.$refs.tableGrid.load(this.searchParam);
				this.$refs.uploadBtnRef.setIconStyle(1);
			}).catch(err=>{
				console.log(err);
				Tools.alertTime("报表模板转换失败", "danger", 0);
				this.$refs.uploadBtnRef.setIconStyle(1);
			});
			
        },
		selectRow(row, column, event) {
			this.formData = Object.assign({}, row);
		},
		submitUploadParam() {
			//文件上传校验
            var validate = this.$refs.addForm.validate();
            if (validate) {
                let formData = {reportId: this.formData.reportId};
                let temp = document.getElementsByClassName('upload-demo');
                let lis = temp[0].childNodes[1].childNodes.length;
                if (lis > 0) {
                    this.$refs.uploadRef.upload(formData);
										this.$refs.uploadBtnRef.setIconStyle(0);
										setTimeout(()=>{
											this.$refs.addPopup.close();
										}, 300)
                } else {
                    Tools.alert("上传文件不能为空!", "danger");
                    return false;
                }
            }
		},
	},
};
</script>
