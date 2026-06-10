<template>
	<div class="py-page">
		<div>
			<k-form-search-customize data-model-name="BaseReportFileManage" v-model="searchParam" data-target="tableGrid" @handleReset="handleReset">
				<k-form-item label="产品代码/名称">
					<k-field-select
						v-model="searchParam.prodCd"
						data-action="BaseReportFileManage.findOdsPrdPrdBasInfs"
						style="width: 100%"
						data-display-field="prodCd,prodNmFu"
						data-value-field="prodCd"
						:data-remote="true"
						:data-remote-paging="true" />
				</k-form-item>
				
				<k-form-item label="文件名称">
					<k-field-text v-model="searchParam.fileName"   />
				</k-form-item>
				
				<k-form-item label="上传压缩文件名称">
					<k-field-text v-model="searchParam.zipfilename"   />
				</k-form-item>

				<k-form-item label="上传日期">
					<k-field-date v-model="searchParam.crtDate" data-type="date" data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd" />
				</k-form-item>
				<k-form-item label="">
					<el-checkbox v-model="searchParam.prodNmFu" style="margin: 0 20px 0 40px" true-label="1" false-label="0">筛选产品名称为空的数据</el-checkbox>
				</k-form-item>
			</k-form-search-customize>
		</div>
		<div class="py-page-container">
			<div class="table-top-btns">
				<div class="left">
					<k-btn slot="button" ref="uploadBtnRef" data-functype="POPUP" class="btn-custom-plain" data-target="addPopup" :load-disabled="false">
						<md-icon>cloud_upload</md-icon>
						上传
					</k-btn>
					<k-btn
						ref="refDownload"
						slot="button"
						class="btn-custom-plain"
						data-size="small"
						@click="creatZipFile('/download/server/DpsApp/importTemplate/downloadBaseReportFileMange.json', 'refDownload')"
					>
						<md-icon>cloud_download</md-icon>
						下载文件
					</k-btn>
					<k-btn
						ref="refDownload1"
						slot="button"
						class="btn-custom-plain"
						data-size="small"
						@click="creatZipFile('/download/server/DpsApp/importTemplate/downloadBSBaseReportFileMange.json', 'refDownload1')"
					>
						<md-icon>cloud_download</md-icon>
						下载报送文件
					</k-btn>
					 <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="handleUpload" data-target="addPopupProd" slot="button" ref="uploadBtnRefProd" :load-disabled="false">
                      <md-icon md-src="/static/svg/add.svg" />九大附件导入</k-btn>
				</div>
			</div>
			<k-grid
				ref="tableGrid"
				@data-row-select="selectRow"
				@data-select-change="handleChange"
				data-action="BaseReportFileManage.findBaseReportFileManages"
				data-checkbox="true"
			>
				<k-grid-column data-header="文件名称" data-name="fileName"></k-grid-column>
				<k-grid-column data-header="文件类型" data-name="fileType" data-dict="prod_file_type"></k-grid-column>
				<k-grid-column data-header="产品代码" data-name="prodCd"></k-grid-column>
				<k-grid-column data-header="产品名称" data-name="prodNmFu"></k-grid-column>
				<k-grid-column data-header="上传压缩文件名称" data-name="zipfilename"></k-grid-column>
				<k-grid-column data-header="上传日期" data-name="crtDate"></k-grid-column>
				<k-grid-column data-header="上传时间" data-name="crtTime"></k-grid-column>
				<k-grid-column data-header="操作员编号" data-name="operaterno"></k-grid-column>
				<k-grid-column data-header="操作员名称" data-name="operatername"></k-grid-column>
				<k-grid-column data-header="更新日期" data-name="updDate"></k-grid-column>
				<k-grid-column data-header="更新时间" data-name="updTime"></k-grid-column>
				<template slot="operate" slot-scope="scope">
					<k-btn class="btn-custom-text" data-descript="修改产品报告文件管理表" data-functype="POPUP" data-size="mini" data-target="editPopup">
						修改
					</k-btn>
					<k-btn
						class="btn-custom-text"
						data-functype="SUBMIT"
						data-action="BaseReportFileManage.deleteBaseReportFileManage"
						data-size="mini"
						data-type="danger"
						data-target="tableGrid"
						:data-confirm="true"
						data-descript="删除产品报告文件管理表"
					>
						删除
					</k-btn>
				</template>
			</k-grid>
		</div>

		<k-popup ref="addPopup" data-title="上传">
			<k-form ref="addForm" data-ui="element">
				<k-form-item label="上传文件" data-ui="element" data-input-width="500px">
					<k-field-upload
						:data-allowblank="true"
						label="附件"
						data-type="file"
						ref="uploadRef"
						data-accept=".zip"
						:data-limit="1"
						:data-error="onSubmitError"
						:data-auto-upload="false"
						:data-success="onSubmitSuccess"
						:dataHttpRequest="httpRequest"
					>
					</k-field-upload>
				</k-form-item>

				<k-form-footer data-align="center">
					<k-btn class="btn-custom-primary" data-target="tableGrid" data-functype="SUBMIT"
					       data-from="uploadForm" :data-model="formData" :data-handler="submitUploadParam">
					  <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE">
					  <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
					</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>

		    <k-popup ref="addPopupProd" data-title="九大附件导入" >
          <k-form ref="addFormProd" :data-col="2">
            <k-form-item label="导入模式">
              <k-field-select v-model="formData.importmodel" data-dict="prod_import_model"  :data-allowblank="false"/>
            </k-form-item>
            <k-form-item label="附件" data-ui="element" data-input-width="500px">
              <k-field-upload data-type="file" ref="uploadRefProd" :data-multiple="false" :data-limit=1 data-accept=".zip"
                              :data-error="onSubmitErrorProd" :data-success="onSubmitSuccessProd"
                              :dataHttpRequest="httpRequest"
                              :data-auto-upload="false">
              </k-field-upload>
            </k-form-item>
            <k-form-footer data-align="center">
              <k-btn class="btn-custom-primary" ref="submitBtnProd" data-target="tableGrid" data-functype="SUBMIT"  data-from="addFormProd" :data-model="formData" :data-handler="submitUploadParamProd">
                <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
              </k-btn>
              <k-btn class="btn-custom-plain" data-functype="CLOSE">
                <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
              </k-btn>
            </k-form-footer>
          </k-form>
        </k-popup>

		<!--    修改产品报告文件管理表弹出框   -->
		<k-popup ref="editPopup" data-title="修改">
			<k-form ref="editForm" :data-col="2" data-label-width="140px">
				<k-form-item label="文件名称">
					<k-field-text v-model="formData.fileName" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="上传压缩文件名称">
					<k-field-text v-model="formData.zipfilename" :data-disabled="true" />
				</k-form-item>
					<k-form-item label="产品代码/名称">
					<k-field-select
						ref="refProd"
						v-model="formData.prodCd"
						data-action="BaseReportFileManage.findDwdPrdPrdBasInfs"
						style="width: 100%"
						data-display-field="prodCd,prodNmFu"
						data-value-field="prodCd"
						:data-remote="true"
						:data-remote-paging="true"
						@data-on-change="handleProdChange"
					/>
				</k-form-item>
				<k-form-item label="上传日期">
					<k-field-text v-model="formData.crtDate" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="上传时间">
					<k-field-text v-model="formData.crtTime" :data-disabled="true" />
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="BaseReportFileManage.updateBaseReportFileManage"
						data-from="editForm"
						:data-model="formData"
						data-target="tableGrid"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>
	</div>
</template>

<script>
import Tools from "@/utils/tools";
export default {
	name: "BaseReportFileManage",
	data() {
		return {
			formData: {},
			selectRowData: {},
			searchParam: {
				prodCd: "",
				prodNmFu: ""
			},
			nowDate: "",
			fileData: new FormData(),
			loading: false
		};
	},
	created() {
		this.getNowDate();
	},
	watch: {
		'searchParam.prodNmFu': {
			handler(v) {
				if (v == 1) {
					this.searchParam.prodCd = "";
				}
			},
			immediate: true
		},
		'searchParam.prodCd': {
			handler(v) {
				if (v) {
					this.searchParam.prodNmFu = "0";
				}
			},
			immediate: true
		}
	},
	methods: {
		handleProdChange() {
			let label = (this.$refs.refProd.options.find(item=>item.value==this.formData.prodCd) || {}).label;
			if (label) {
				this.formData.prodNmFu = label.split('-')[1] || '';
			}
		},
	  handleUpload() {
            this.formData = {}
     },
		handleReset() {
			this.searchParam.prodNmFu = "0";
		},
		handleBlur() {
			if (this.formData.prodCd) {
				this.httpUtil.comnQuery({
          action: 'BaseReportFileManage.findDwdPrdPrdBasInfs',
          params: {
						prodCd: this.formData.prodCd
					}
        }).then(data => {
					this.$set(this.formData, "prodNmFu", data.rows[0].prodNmFu);
        });
			}
		},
		handleChange(row) {
			this.selectRowData = row;
		},
		creatZipFile(url, ref) {
			if (!this.selectRowData.length) {
				this.$message.error("请选择数据，至少选择一条");
				return;
			}
			if(ref == 'refDownload1') {
				if (this.selectRowData.find(item=>!item.prodNmFu)) {
					return this.$message.error("下载报送文件存在产品名称为空的数据，请修改!");
				}
			}
			this.$refs[ref].setLoading(true);
			this.httpUtil.download({
				url,
				params: {
					id: this.selectRowData.map((item) => item.id).join(","),
				},
				callback: () => {
					this.$refs[ref].setLoading(false);
				},
			});
		},
		selectRow(row, column, event) {
			this.formData = Object.assign({}, row);
		},
		onSubmitSuccess() {
			this.$refs.uploadRef.doReset();
			this.$refs.addForm.reset();
			this.$refs.addPopup.close();
			this.$refs.tableGrid.load(this.searchParam);
		},
		onSubmitSuccessProd() {
			this.$refs.uploadRefProd.doReset();
			this.$refs.addFormProd.reset();
			this.$refs.addPopupProd.close();
			this.$refs.tableGrid.load(this.searchParam);
		},
		onSubmitError() {
			this.$refs.uploadRef.doReset();
		},
		onSubmitErrorProd() {
			this.$refs.uploadRefProd.doReset();
		},
		handleSubmit() {
			const data = {};
			data.reportDate = this.formData.reportDate;
			data.formName = this.formData.tableName;
			this.$refs.uploadRef.upload(data);
		},
		httpRequest(file) {
		  this.fileData.delete('files');
      this.fileData.append("files", file.file);
    },
	submitUploadParam() {
		if (this.$refs.uploadBtnRef.loading) {
			return
		}
		//文件上传校验
		let temp = document.getElementsByClassName("upload-demo");
		let lis = temp[0].childNodes[1].childNodes.length;
		if (lis > 0) {
			this.$refs.uploadBtnRef.setIconStyle(0)
			this.fileData = new FormData();
			this.$refs.uploadRef.upload();
			this.httpUtil.upload({
				url: "/upload-files/server/DpsApp/importTemplate/importBaseReportFileMange.json",
				formData: this.fileData
			}).then(res => {
				if (res.data.success) {
					Tools.alertTime(res.data.returnmsg, "success", 1000);
					this.$refs.tableGrid.load(this.searchParam);
				} else {
					Tools.alertTime(res.data.returnmsg || "上传文件失败！", "danger", 0);
				}
				this.$refs.uploadBtnRef.setIconStyle(1);
			}).catch(err => {
				console.log(err);
				Tools.alertTime("上传文件失败！", "danger", 0);
				this.$refs.uploadBtnRef.setIconStyle(1);
			})
			setTimeout(()=>{
				this.$refs.addPopup.close();
			}, 300)
		} else {
			this.$message.warning("上传文件不能为空!");
		}
		return false;
	},
	submitUploadParamProd() {
		var validate = this.$refs.addFormProd.validate();
		if (validate == false) {
			return false;
		}
		let temp = document.getElementsByClassName("upload-demo");
		let lis = temp[0].childNodes[1].childNodes.length;
		if (lis <= 0) {
			Tools.alert("上传附件不能为空!", "danger");
			this.$refs.submitBtnProd.setIconStyle(1);
			return false;
		}
      	this.$refs.uploadBtnRefProd.setIconStyle(0);
		let uploadData = this.formData;
		this.fileData.delete('params');
      	this.fileData.append('params', JSON.stringify(uploadData));
		this.$refs.uploadRefProd.upload();
		this.httpUtil
			.upload({
				url: "/upload-files/server/DpsApp/importTemplate/importProdReportFileMange.json",
				formData: this.fileData,
			})
			.then((res) => {
				if (res.data.success) {
					Tools.alertTime(res.data.returnmsg, "success", 1000);
					this.$refs.tableGrid.load(this.searchParam);
				} else {
            		Tools.alertTime(res.data.returnmsg || "上传文件失败！", "danger", 0);
				}
          		this.$refs.uploadBtnRefProd.setIconStyle(1);
			}).catch(err=>{
				console.log(err);
				Tools.alertTime("上传文件失败！", "danger", 0);
				this.$refs.uploadBtnRefProd.setIconStyle(1);
        });
		setTimeout(()=>{
			this.$refs.addPopupProd.close();
		}, 300)
		return false;
	},
	getNowDate() {
		const timeOne = new Date();
		const year = timeOne.getFullYear();
		let month = timeOne.getMonth() + 1;
		let day = timeOne.getDate();
		month = month < 10 ? "0" + month : month;
		day = day < 10 ? "0" + day : day;
		this.nowDate = year + "" + month + "" + day;
	},
	},
};
</script>
