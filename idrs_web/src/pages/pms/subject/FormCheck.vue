<template>
	<div class="py-page">
		<k-form-search-customize v-model="searchParam" data-target="formCheckGrid" data-label-width="90px" @dataLoadAfter="dataLoadAfter" :handleConfirm="handleConfirm">
			<k-form-item label="报表名称">
				<k-field-select v-model="searchParam.tableName" :data-data="tableNameDict" data-display-field="tableName" data-value-field="systemTableName" :data-allowblank="false" ></k-field-select>
			</k-form-item>
			<k-form-item label="数据日期">
				<k-field-date v-model="searchParam.dealDate" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd" />
			</k-form-item>
			<k-form-item label="报送日期">
				<k-field-date v-model="searchParam.reportDate" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd" />
			</k-form-item>
		</k-form-search-customize>

		<div class="py-page-container">
			<div class="table-top-btns">
				<div class="left">
					<k-btn slot="button" ref="uploadBtnRef" data-functype="POPUP" class="btn-custom-plain" data-target="uploadPopup" :data-handler="handleUpload" 
						:load-disabled="false" loadingTip="正在导入并比较，请稍后重试！">
						<md-icon>cloud_upload</md-icon>
						上传
					</k-btn>
					<k-btn slot="button"  class="btn-custom-plain"  data-functype="DOWNLOAD" data-from="RptCmp" :data-download-name="getExpFileName()"
                 data-url="/download/server/DpsApp/exportCompareFile.json"
                 :data-model="searchParam" data-descript="导出结果">
            <md-icon>cloud_download</md-icon>
            导出
          </k-btn>
					<k-btn class="btn-custom-primary" data-functype="POPUP" data-target="cpmPopup" :data-handler="checkLoading">
          						<i v-show="!loading"><md-icon md-src="/static/svg/confirm.svg" /></i>
          						<i v-show="loading" class="el-icon-loading" />
          						投资者比较</k-btn>
				</div>
				<div class="right">
					核对结果：上传数据<span class="count">{{ totalItem }}</span>条，
					系统数据<span class="count">{{ systemItem }}</span>条，
					匹配数据<span class="count">{{ pkMatchItem }}</span>条，
					匹配一致数据<span class="count">{{ allMatchItem }}</span>条，
					匹配不一致数据<span class="count diff">{{ notMatchItem }}</span>条
				</div>
			</div>


			<k-grid ref="formCheckGrid" data-action="RptCmp.find" @data-row-select="selectRow" :dataAutoload="false" >
				<k-grid-column data-header="上传数据" data-align="center">
					<k-grid-column data-header="主键" data-name="lfPk" data-hidden="true"/>
					<k-grid-column data-header="行代码/主键" data-name="lfRowCode" />
					<k-grid-column data-header="行名称" data-name="lfRowName" />
					<k-grid-column data-header="列代码" data-name="lfColumnCode" />
					<k-grid-column data-header="列名称" data-name="lfColumnName" />
					<k-grid-column data-header="指标值" data-name="lfData" />
				</k-grid-column>
				<k-grid-column data-header="系统数据" data-align="center">
					<k-grid-column data-header="主键" data-name="rtPk" data-hidden="true"/>
					<k-grid-column data-header="行代码/主键" data-name="rtRowCode" />
					<k-grid-column data-header="行名称" data-name="rtRowName" />
					<k-grid-column data-header="列代码" data-name="rtColumnCode" />
					<k-grid-column data-header="列名称" data-name="rtColumnName" />
					<k-grid-column data-header="指标值" data-name="rtData" />
				</k-grid-column>
				<template slot="operate">
					<k-btn data-functype="POPUP" data-target="formCheckDetail" class="btn-custom-text"> 详情 </k-btn>
				</template>
			</k-grid>
		</div>

		<k-popup ref="uploadPopup" data-title="上传" data-width="580px">
			<k-form ref="uploadForm" :data-col="0">
				<k-form-item label="报表名称">
					<k-field-select v-model="formData.tableName" :data-data="tableNameDict" data-display-field="tableName" data-value-field="id" :data-allowblank="false" @data-on-change="changeTableName" />
				</k-form-item>
				<k-form-item label="数据日期" :class="{'special-form-item': isSpecialForm}">
					<k-field-date v-model="formData.dealDate" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"  :data-disabled="formData.tableName && specialTable ? true : false" />
				</k-form-item>
				<div class="date-tip" v-if="isSpecialForm">
					<i class="el-icon-info"></i>
					<span class="tip">{{formData.tableName == '217' ? '只能核对月末报送数据，日表数据不支持核对' : '只能核对月末和周末报送数据，日表数据不支持核对'}}</span>
				</div>
				<k-form-item label="报送日期">
					<k-field-date v-model="formData.reportDate" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd" :data-disabled="formData.tableName && !specialTable ? true : false" />
					<k-tooltip data-content="报送/数据日期必填一项"></k-tooltip>
				</k-form-item>
				<k-form-item label="附件">
					<k-field-upload
						v-model="formData.fileList"
						:data-allowblank="true"
						label="附件"
						data-type="file"
						ref="uploadRef"
						data-accept=".xlsx,.xls,.zip"
						:data-limit="2"
						:data-error="onSubmitError"
						:dataChange="onUploadChange"
						:data-auto-upload="false"
						:data-success="onSubmitSuccess"
						:dataHttpRequest="httpRequest"
					>
					</k-field-upload>
				</k-form-item>

				<k-form-footer data-align="center">
					<k-btn class="btn-custom-primary" data-target="formCheckGrid" data-functype="SUBMIT"
					       data-from="uploadForm" :data-model="formData" :data-handler="submitUploadParam" >
					  <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE">
					  <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
					</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>
    <k-popup ref="cpmPopup" data-title="比较" data-width="580px">
    			<k-form ref="cpmForm" :data-col="0">
    				<k-form-item label="数据日期">
    					<k-field-date v-model="formData.reportDate" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd" :data-allowblank="false" />
    				</k-form-item>

    				<k-form-item label="报表名称">
    					<k-field-select v-model="formData.tableName" :data-data="investTableNameDict" data-display-field="tableNameZh" data-value-field="tableName" :data-allowblank="false" />
    				</k-form-item>

    				<k-form-footer data-align="center">
    					<k-btn class="btn-custom-primary" data-target="formCheckGrid" data-functype="SUBMIT"
    					       data-from="cpmForm" :data-model="formData"  @click="compare" >
    					  <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
    					</k-btn>
    					<k-btn class="btn-custom-plain" data-functype="CLOSE">
    					  <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
    					</k-btn>
    				</k-form-footer>
    			</k-form>
    		</k-popup>
		<k-popup ref="formCheckDetail" data-title="详情" data-width="60%">
			<el-row :gutter="15">
				<k-form style="width: 100%" :data-col="3" data-label-width="200px">
					<el-col :span="12">
						<h5>上传数据：</h5>
						<k-form-item v-for="(submitData, index) in oldDatas" :key="index" :label="submitData.label" :dataColor="submitData.color">
							<k-field-display :value="submitData.value" :data-clearable="false" />
						</k-form-item>
					</el-col>
					<el-col :span="12">
						<h5>系统数据：</h5>
						<k-form-item v-for="(submitData, index) in submitDatas" :key="index" :label="submitData.label" :dataColor="submitData.color">
							<k-field-display :value="submitData.value" :data-clearable="false" />
						</k-form-item>
					</el-col>
				</k-form>
			</el-row>
		</k-popup>
	</div>
</template>

<script>
import Tools from "@/utils/tools.js";
import { assign } from "lodash";
export default {
	name: "FormCheck",
	data() {
		return {
		  fileData: new FormData(),
			selectRowData: {},
			searchParam: {},
			formData: {
        trxMkt:'',
        scrNm:'',
        fundType:'',
        scrCd:'',
        scrId:''
      },
			hasOldData: false,
			oldDatas: [],
			submitDatas: [],
			totalItem: 0,
			systemItem:0,
			pkMatchItem:0,
			allMatchItem:0,
			notMatchItem:0,
			sameItem: 0,
			diffItem: 0,
			loading: false,
			tableNameDict:[],
			investTableNameDict:
			      [{tableName:'app_cust_register_info',tableNameZh:"投资者身份信息"},
			      {tableName:'app_cust_vol_register_info',tableNameZh:'投资者持有信息'},
			      {tableName:'app_cust_trans_info',tableNameZh:'投资者明细信息'}],
			uploadFlag: {
				isSuccess: false,
				isError: false,
				loading: false
			}
		};
	},
	computed: {
		specialTable() {
			const tableNameId = ["81", "82", "91", "116", "117", "187", "188", "189"]
			return tableNameId.includes(this.formData.tableName)
		},
		isSpecialForm() {
			return ['217', '203'].includes(this.formData.tableName) 
		}
	},
	mounted: function(){
    this.initTableDict();
  },
	methods: {
		changeTableName() {
			this.$nextTick(()=>{
				if (this.specialTable) {
					this.formData.dealDate = ""
				} else {
					this.formData.reportDate = ""
				}
			})
		},
		handleUpload() {
			// if (this.uploadFlag.loading) {
			// 	Tools.alert("正在导入并比较，请稍后重试!","success");
			// 	return false
			// } else {
				this.formData = {}
			// }
		},
    handleConfirm() {
      if(this.searchParam.dealDate == null && this.searchParam.reportDate == null) {
        Tools.alert("数据日期和报送日期不能同时为空！","danger");
        return false;
      } else {
        return true;
      }
    },
		dataLoadAfter() {
			this.httpUtil.comnQuery({
              action: "RptCmp.findCmpLog",
              params: this.searchParam
            }).then(data => {
               if (data.rows) {
                  if(data.rows[0]) {
                    this.totalItem=data.rows[0].uploadCount;
                    this.systemItem = data.rows[0].systemCount;
                    this.pkMatchItem = data.rows[0].pkMatchCount;
                    this.allMatchItem = data.rows[0].allMatchCount;
                    this.notMatchItem = data.rows[0].notMatchCount;
                  } else {
                    this.totalItem=0;
                    this.systemItem = 0;
                    this.pkMatchItem = 0;
                    this.allMatchItem = 0;
                    this.notMatchItem = 0;
                  }

               }
            }).catch({})
		},
		selectRow(row, column, event) {
			debugger
			this.hasOldData = false;
			let submitdata = Tools.str2Json(row.rtData);
			if (row.lfData) {
				let olddata = Tools.str2Json(row.lfData);
				let oldLfPk = Tools.str2Json(row.lfPk);
				let oldRtPk = Tools.str2Json(row.rtPk);
				if (!olddata && olddata.length === 0) {
					this.submitDatas = submitdata;
					return;
				}
				this.hasOldData = true;
				if (JSON.stringify(olddata).indexOf('行ID') > 0 && JSON.stringify(olddata).indexOf('列ID') > 0) {
					this.oldDatas = this.formateData1(olddata, submitdata, oldLfPk, oldRtPk);
				    this.submitDatas = this.formateData1(submitdata, olddata, oldRtPk, oldLfPk);
				} else {
					this.oldDatas = this.formateData(olddata, submitdata);
				    this.submitDatas = this.formateData(submitdata, olddata);
				}
				return;
			}
			this.submitDatas = submitdata;
		},
		formateData1(core, right, pk1, pk2) {
			let newArray = new Array();
			let rdatas = new Map();
			right.forEach((data, i = 0) => {
				rdatas.set(data.label, data);
			});
			let pkdatas1 = new Map(Object.entries(pk1));
			let pkdatas2 = new Map(Object.entries(pk2));
			newArray.push({ label: "行名称" + "：", value: pkdatas1.get("行名称"), color: pkdatas1.get("行名称") === pkdatas2.get("行名称") ? "#606266" : "red" });
			newArray.push({ label: "列名称" + "：", value: pkdatas1.get("列名称"), color: pkdatas1.get("行名称") === pkdatas2.get("行名称") ? "#606266" : "red" });
			for (let i = 0, j = 0; i < core.length && j < right.length; i++, j++) {
				if (core[i].label === right[j].label) {
					if (core[i].value === right[j].value) {
						// 相同数据只展示主键
					    newArray.push({ label: core[i].label + "：", value: core[i].value, color: "#606266" });
					} else {
						newArray.push({ label: core[i].label + "：", value: core[i].value, color: "red" });
					}
				} else {
					if (rdatas.get(core[i].label)) {
						newArray.push({ label: "", value: "", color: "#606266" });
						i--;
					} else {
						newArray.push({ label: core[i].label + "：", value: core[i].value, color: "#606266" });
						j--;
					}
				}
				if (j === right.length - 1) {
					for (i++; i < core.length; i++) {
						newArray.push({ label: core[i].label + ":", value: core[i].value, color: "#606266" });
					}
				}
			}
			if (right.length === 0) {
				core.forEach((data) => {
					newArray.push({ label: data.label + ":", value: data.value, color: "#606266" });
				});
			}
			return newArray;
		},
		formateData(core, right) {
			let newArray = new Array();
			let rdatas = new Map();
			right.forEach((data, i = 0) => {
				rdatas.set(data.label, data);
			});
			for (let i = 0, j = 0; i < core.length && j < right.length; i++, j++) {
				if (core[i].label === right[j].label) {
					if (core[i].value === right[j].value) {
					    // 相同数据只展示主键
					    newArray.push({ label: core[i].label + "：", value: core[i].value, color: "#606266" });
					} else {
						newArray.push({ label: core[i].label + "：", value: core[i].value, color: "red" });
					}
				} else {
					if (rdatas.get(core[i].label)) {
						newArray.push({ label: "", value: "", color: "#606266" });
						i--;
					} else {
						newArray.push({ label: core[i].label + "：", value: core[i].value, color: "#606266" });
						j--;
					}
				}
				if (j === right.length - 1) {
					for (i++; i < core.length; i++) {
						newArray.push({ label: core[i].label + ":", value: core[i].value, color: "#606266" });
					}
				}
			}
			if (right.length === 0) {
				core.forEach((data) => {
					newArray.push({ label: data.label + ":", value: data.value, color: "#606266" });
				});
			}
			return newArray;
		},
		onSubmitError() {
			// this.$refs.uploadRef.doReset();
		},
		onUploadChange(file, fileList) {
		  this.fileData.delete("files");
      this.fileData.append('files', file.file);
      console.log("this.fileList=:>>>",fileList);
      this.fileList = fileList;
      this.fileNameList = [];
      for(let i in this.fileList){
        this.fileNameList.push(this.fileList[i].name);
      }
      if (this.formData.modName ===''||this.formData.modName== null){
        this.$set(this.formData,'modName',this.fileNameList[0].substring(0,this.fileNameList[0].lastIndexOf(".")));
      }
      this.$set(this.formData, "fileList", fileList);
		},
		//上传成功操作
		onSubmitSuccess(response, file, fileList) {
			if ((this.searchParam.reportDate || this.searchParam.dealDate) && this.searchParam.tableName) {
				this.$refs.formCheckGrid.load(this.searchParam);
				this.dataLoadAfter();
			}
		},
		handleSubmit() {
			const data = {};
			data.reportDate = this.formData.reportDate;
			data.formName = this.formData.tableName;
			this.$refs.uploadRef.upload(data);
		},
		initTableDict(){
      this.httpUtil.comnQuery({
        action: "RptCmp.findTableName",
        params: {dictName: "tableNameDict"}
      }).then(data => {
        this.tableNameDict = data.rows;
      }).catch({})
    },

    submitUploadParam() {
      var validate = this.$refs.uploadForm.validate();
      if(validate==false){
        return false;
      }
      let urlPath = window.document.location.href;  //浏览器显示地址 http://10.1.20.88:8201/xxx/xxx
      let docPath = "8201"; //服务器相对地址 8201/xxx/xxx
      let index = urlPath.indexOf(docPath);
      let serverPath = urlPath.substring(0, index);
      let onlineUrl = this.httpUtil.onlineUrl;
      if(onlineUrl!="undefined"&&onlineUrl!=null&&onlineUrl!=""){
        this.formData.onlineUrl = onlineUrl;
      }else{
        this.formData.onlineUrl = serverPath+"8201";
      }

      this.fileNameList = [];
      if(this.fileList==null || this.fileList.length<=0){
        Tools.alert("上传附件不能为空！","danger");
        return false;
      }
      let uploadData = this.formData;
      //this.fileData = new FormData();
      this.$refs.uploadRef.upload();
      this.fileData.delete('params');
      this.fileData.append('params', JSON.stringify(uploadData));
			this.$refs.uploadBtnRef.setIconStyle(0);
			this.uploadFlag.isSuccess = false
			this.uploadFlag.isError = false
			this.uploadFlag.loading = true
      this.httpUtil.upload({
        // url:"/upload-files/server/PmsApp/xpdoc/uploadTemp.json",
        url:"/upload-files/server/DpsApp/importTemplate/implDataAndCompare.json",
        formData: this.fileData
      }).then(res=>{
				this.uploadFlag.isSuccess = true
				this.uploadFlag.isError = false
				this.uploadFlag.loading = false
        this.showSubmitBtn = true;
        if(res.data.success){
          Tools.alertTime(res.data.returnmsg, "success", 0);
          this.onSubmitSuccess()
        }else{
          this.showSubmitBtn = true;
          if (res.data.returnmsg!==''&&res.data.returnmsg!==null&&res.data.returnmsg!==undefined){
            Tools.alertTime(res.data.returnmsg,"danger", 0);
          }else {
            Tools.alertTime("上传文件失败！","danger", 0);
          }
        }
				this.$refs.uploadBtnRef.setIconStyle(1);
      }).catch(err=>{
				this.uploadFlag.isSuccess = false
				this.uploadFlag.isError = true
				this.uploadFlag.loading = false
				this.$refs.uploadBtnRef.setIconStyle(1);
			})
			// 关闭弹框
			setTimeout(()=>{
				this.$refs.uploadPopup.close();
			}, 500)
			return false
    },
    httpRequest(file){
      const _this = this
      _this.fileList=[];
      _this.fileData.delete('files');
      _this.fileData.append('files', file.file);
      _this.fileList.push(file.filename);
    },
    validateForm(){
      var validate = this.$refs.uploadForm.validate();
      if(validate==false){
        return false;
      }
      if (validate) {
        let formData = this.formData;
        let temp = document.getElementsByClassName('upload-demo');
        let lis = temp[0].childNodes[1].childNodes.length;
        if(lis>0){
          this.$refs.uploadRef.upload(formData);
        }else{
          // Tools.alert("上传文件不能为空!","danger");
          this.showSubmitBtn=true;
          return false;
        }
      }
    },
    checkLoading(){
          if (this.loading) {
              Tools.alert("正在进行报表比对，请稍后重试！", "danger");
               return false;
             }
    },
    compare(){
        const data = {};
		data.report_date = this.formData.reportDate;
		data.table_name = this.formData.tableName;

		if (this.formData.reportDate == '' || this.formData.reportDate == null ||
		this.formData.tableName == '' || this.formData.tableName == null) {
			return false;
		}

		this.loading = true;
		this.httpUtil.ajax({
						url: "/server/json/DpsApp/investorReportCompare",
						params: data
						}).then(data => {
						this.loading = false;
						console.log(data);
						this.$refs["cpmPopup"].close();
						let rows = data.rows;

						})
    },
    getExpFileName(){
        for(let i in this.tableNameDict){
            if(this.tableNameDict[i].systemTableName === this.searchParam.tableName){
              if(this.searchParam.dealDate){
                return this.tableNameDict[i].tableName + "-" + this.searchParam.dealDate + "-核对结果.xlsx";
              }
              if(this.searchParam.reportDate){
                return this.tableNameDict[i].tableName + "-" + this.searchParam.reportDate + "-核对结果.xlsx";
              }
              return this.tableNameDict[i].tableName + "-核对结果.xlsx";
            }
        }
        return this.searchParam.tableName + "-核对结果.xlsx";
    },
	},
};
</script>

<style lang="scss" scoped>
.right {
	font-size: 14px;
	.count {
		color: #407fff;
		margin: 0 2px;
		font-size: 16px;
		font-weight: bold;
		&.diff {
			color: red;
		}
	}
}
.date-tip {
	margin-left: 120px;
	color: #aaa;
	margin-bottom: 10px;
}
.special-form-item {
	margin-bottom: 2px;
}
</style>
