<template>
	<div class="py-page">
		<div>
			<k-form-search-customize data-model-name="InvestorSubHoldInfo" v-model="searchParam" data-target="investorSubHoldGrid" data-label-width="100px" :handleConfirm="handleConfirm">
				<k-form-item label="持有日期">
					<k-field-date v-model="searchParam.holdDate" />
				</k-form-item>
				<k-form-item label="子产品代码">
					<k-field-text v-model="searchParam.prodCodeS" />
				</k-form-item>
				<k-form-item label="产品登记编码">
					<k-field-text v-model="searchParam.prodCode" />
				</k-form-item>
				<k-form-item label="识别标识">
					<k-field-text v-model="searchParam.custNo" />
				</k-form-item>
				<k-form-item label="TA_ID">
					<k-field-text v-model="searchParam.taId" />
				</k-form-item>
				<k-form-item label="投资者类别">
          <k-field-select v-model="searchParam.custType" data-dict="tr_cust_type" />
        </k-form-item>
        <k-form-item label="币种">
          <k-field-select v-model="searchParam.cur" data-dict="tr_cur" />
        </k-form-item>
        <k-form-item label="渠道号">
          <k-field-text v-model="searchParam.channelCode"  />
        </k-form-item>
			</k-form-search-customize>
		</div>
		<div class="py-page-container">
			<div class="table-top-btns">
				<div class="left">
					<k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="() => (this.formData = {})" data-target="addInvestorSubHoldPopup">
						<md-icon md-src="/static/svg/add.svg" />新增
					</k-btn>
					<k-btn slot="button" ref="uploadModifyBtnRef" :load-disabled="false" data-functype="POPUP" class="btn-custom-plain"
                		   data-target="uploadInvestorSubHoldInfoModifyPopup">
                        <md-icon>cloud_upload</md-icon>导入数据变更
                    </k-btn>
					<k-btn slot="button" ref="exportBtnRef" class="btn-custom-plain" data-functype="EXPORT" data-target="investorSubHoldGrid"
					       data-action="InvestorSubHoldInfo.historyDownload" data-export-name="投资者持有信息(子产品)历史数据"
						   :report-date="searchParam.holdDate" :cust-no="searchParam.custNo" :data-handler="handleConfirmExport">
						<md-icon>cloud_download</md-icon>导出
					</k-btn>
				</div>
			</div>
			<k-grid ref="investorSubHoldGrid" @data-row-select="selectRow" data-action="InvestorSubHoldInfo.queryInvestorSubHoldInfo"
			        data-operate-width="110" data-fixed="right" :data-autoload="false" :handleDataFun="handleData" >
				<k-grid-column data-header="登记机构代码" data-name="bankCode" data-width="120"></k-grid-column>
				<k-grid-column data-header="子产品代码" data-name="prodCodeS" data-width="120"></k-grid-column>
				<k-grid-column data-header="产品代码" data-name="prodCodeM" data-width="120"></k-grid-column>
				<k-grid-column data-header="产品登记编码" data-name="prodCode" data-width="120"></k-grid-column>
				<k-grid-column data-header="识别标识" data-name="custNo" data-width="120"></k-grid-column>
				<k-grid-column data-header="持有日期" data-name="holdDate" data-width="120"></k-grid-column>
				<k-grid-column data-header="币种" data-name="cur" data-dict="tr_cur" data-width="80"></k-grid-column>
				<k-grid-column data-align="right" data-header="持有份额" data-name="holdVol" data-width="120"></k-grid-column>
				<k-grid-column data-align="right" data-header="持有金额" data-name="holdAmt" data-width="120"></k-grid-column>
				<k-grid-column data-align="right" data-header="折算人民币金额(元)" data-name="convertRmb" data-width="160"></k-grid-column>
				<k-grid-column data-header="TA_ID" data-name="taId" data-width="120"></k-grid-column>
				<k-grid-column data-header="渠道号" data-name="channelCode" data-width="120"></k-grid-column>
				<k-grid-column data-header="投资者类别" data-name="custType" data-dict="tr_cust_type" data-width="120"></k-grid-column>
				<k-grid-column data-header="个人证件类别" data-name="personalIdType" data-dict="tr_personal_id_type" data-width="100"></k-grid-column>
				<k-grid-column data-header="机构证件类别" data-name="organizationIdType" data-dict="tr_organization_id_type" data-width="100"></k-grid-column>
				<k-grid-column data-header="其他证件名称" data-name="otherIdName" data-width="100"></k-grid-column>
				<k-grid-column data-header="证件号码" data-name="idCode" data-width="100"></k-grid-column>
				<k-grid-column data-header="业务登记日期" data-name="registerDate" data-width="100" data-hidden="true" data-export="false"></k-grid-column>
				<k-grid-column data-header="报表日期" data-name="reportDate" data-width="100" data-hidden="true" data-export="false"></k-grid-column>
				<k-grid-column data-header="登记状态" data-name="registerStatus" data-width="100" data-hidden="true" data-export="false"></k-grid-column>
				<k-grid-column data-header="登记流水号" data-name="registerSerno" data-width="100" data-hidden="true" data-export="false"></k-grid-column>
				<k-grid-column data-header="理论报送起始日期" data-name="theoryReportStartDate" data-width="100" data-hidden="true" data-export="false"></k-grid-column>
				<k-grid-column data-header="理论报送截止日期" data-name="theoryReportEndDate" data-width="100" data-hidden="true" data-export="false"></k-grid-column>
				<k-grid-column data-header="新增日期" data-name="createDate" data-width="100" data-hidden="true" data-export="false"></k-grid-column>
				<template slot="operate" slot-scope="scope">
					<k-btn class="btn-custom-text" data-functype="POPUP" data-target="editInvestorSubHoldPopup" v-if="searchParam.holdDate === workDate"> 修改 </k-btn>
					<k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="InvestorSubHoldInfo.removeInvestorSubHoldInfo" data-target="investorSubHoldGrid" :data-confirm="true" v-if="searchParam.holdDate === workDate"> 删除 </k-btn>
				</template>
			</k-grid>
		</div>

		<!--    新增客户持有(子产品信息)表弹出框   -->
		<k-popup ref="addInvestorSubHoldPopup" data-title="新增">
			<k-form ref="addInvestorSubHoldForm" :data-col="2" data-label-width="170px">
				<k-form-item label="登记机构代码">
					<k-field-text v-model="formData.bankCode" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="子产品代码">
          <k-field-text v-model="formData.prodCodeS" :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="产品代码">
          <k-field-text v-model="formData.prodCodeM" :data-allowblank="false" />
        </k-form-item>
				<k-form-item label="产品登记编码">
					<k-field-text v-model="formData.prodCode" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="识别标识">
					<k-field-text v-model="formData.custNo" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="持有日期">
					<k-field-date v-model="formData.holdDate" />
				</k-form-item>
				<k-form-item label="币种">
					<k-field-select v-model="formData.cur" data-dict="tr_cur" />
				</k-form-item>
				<k-form-item label="持有份额">
					<k-field-text v-model="formData.holdVol" :data-allowblank="false"/>
				</k-form-item>
				<k-form-item label="持有金额">
					<k-field-text v-model="formData.holdAmt" :data-allowblank="false"/>
				</k-form-item>
				<k-form-item label="折算人民币金额(元)">
					<k-field-text v-model="formData.convertRmb" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="TA_ID">
					<k-field-text v-model="formData.taId" :data-allowblank="false" />
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="InvestorSubHoldInfo.putInvestorSubHoldInfo"
						data-from="addInvestorSubHoldForm"
						:data-model="formData"
						data-target="investorSubHoldGrid"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>

		<!--    更新投资者持有(子产品)表弹出框   -->
		<k-popup ref="editInvestorSubHoldPopup" data-title="修改">
			<k-form ref="editInvestorSubHoldForm" :data-col="2" data-label-width="170px">
				<k-form-item label="id" v-show="false">
					<k-field-text v-model="formData.id" />
				</k-form-item>
				<k-form-item label="登记机构代码" :class="[handleItemDiff('bankCode')]">
					<k-field-text v-model="formData.bankCode" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="子产品代码" :class="[handleItemDiff('prodCodeS')]">
          <k-field-text v-model="formData.prodCodeS" :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="产品代码" :class="[handleItemDiff('prodCodeM')]">
          <k-field-text v-model="formData.prodCodeM" :data-allowblank="false" />
        </k-form-item>
				<k-form-item label="产品登记编码" :class="[handleItemDiff('prodCode')]">
					<k-field-text v-model="formData.prodCode" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="识别标识" :class="[handleItemDiff('custNo')]">
					<k-field-text v-model="formData.custNo" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="持有日期" :class="[handleItemDiff('holdDate')]">
					<k-field-date v-model="formData.holdDate" :data-allowblank="false"/>
				</k-form-item>
				<k-form-item label="币种" :class="[handleItemDiff('cur')]">
					<k-field-select v-model="formData.cur" data-dict="tr_cur" :data-allowblank="false"/>
				</k-form-item>
				<k-form-item label="持有份额" :class="[handleItemDiff('holdVol')]">
					<k-field-text v-model="formData.holdVol" :data-allowblank="false"/>
				</k-form-item>
				<k-form-item label="持有金额" :class="[handleItemDiff('holdAmt')]">
					<k-field-text v-model="formData.holdAmt" :data-allowblank="false"/>
				</k-form-item>
				<k-form-item label="折算人民币金额(元)" :class="[handleItemDiff('convertRmb')]">
					<k-field-text v-model="formData.convertRmb" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="TA_ID" :class="[handleItemDiff('taId')]">
					<k-field-text v-model="formData.taId" :data-allowblank="false" />
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="InvestorSubHoldInfo.updateInvestorSubHoldInfo"
						data-from="editInvestorSubHoldForm"
						:data-model="formData"
						data-target="investorSubHoldGrid"
						:handle-before="handleBeforeUpdate"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>

		<k-popup ref="uploadInvestorSubHoldInfoModifyPopup" data-title="投资者持有信息(子产品)变更数据导入">
        <k-form ref="addModifyForm" data-ui="element">
          <k-form-item label="变更数据导入" data-ui="element" data-input-width="500px">
            <k-field-excel-upload data-type="file" ref="uploadModifyRef" :data-multiple="false" :data-limit='1'
                data-accept=".xlsx,.xls"
                :data-error="onSubmitModifyError" :data-success="onModifySubmitSuccess"
                :data-auto-upload="false"
                data-upload-url="upload/server/RptApp/reportManage/invSubHoldModifyImport.json">
            </k-field-excel-upload>
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="investorSubHoldGrid" ref="submitBtn"
                  :data-auto-upload="false" data-from="addModifyForm" :data-handler="submitUploadModifyParam">
                  <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
          </k-form-footer>
        </k-form>
        </k-popup>
	</div>
</template>

<script>
import Tools from "@/utils/tools";
import httpUtil from "@/frame/httpUtil";
export default {
  name:"InvestorSubHoldInfo",
	data() {
		return {
			formData: {},
			formDataCopy: {},
			updFormData: {},
			selectRowData: {},
			searchParam: {},
			workDate: "" //当前工作日
		};
	},
	methods: {
		handleConfirmExport() {
			this.httpUtil
				.comnQuery({
					action: "BaseReportExportLog.fileStatusQuery",
					params: {reportName: this.$refs.exportBtnRef.dataExportName},
					successAlert: false,
				})
				.then((data) => {
					if (data.returndata.flag == '0') {
					  if (!this.handleConfirm()) {
							return false;
						}
					  this.$refs.exportBtnRef.handleExport(this.searchParam);
					} else if (data.returndata.flag == '1') {
					  Tools.alertTime(data.returnmsg, "danger", 0);
					} else if (data.returndata.flag == '2') {
					  Tools.alertTime(data.returnmsg, "success", 3000);
					}
				});
			return false;
		},
		handleBeforeUpdate() {
			if (this.formNoChangeCb()) {
				this.$refs.editInvestorSubHoldPopup.close();
				return false
			}
			return true
		},
		handleData (row) {
			row.map(sr => {
				sr.idCode = this.sensitiveIdCodeHandle(sr.idCode);//处理行内证件号敏感数据规则
			})
			return row;
		},
    //行内规则-身份证号码：屏蔽后6位
    sensitiveIdCodeHandle(idCode){
      if(idCode !== null) {
        const length = idCode.length;
        if(length <= 6) {
          return '*'.repeat(length);
        }
        const left = idCode.slice(0, length-6);
        return left + '*'.repeat(6);
      }
    },
		handleConfirm() {
      if (!this.searchParam.holdDate && !this.searchParam.custNo) {
        this.$message.error('“持有日期”和“识别标识”不能同时为空!');
        return;
      }
      return true;
		},
		selectRow(row, column, event) {
			this.selectRowData = Object.assign({}, row);
			this.formData = Object.assign({}, row);
			this.formDataCopy = Object.assign({}, row);
			// this.updFormData = Object.assign({}, row);
		},

		onSubmitSuccess() {
			this.$refs.uploadRef.doReset();
			this.$refs.addForm.reset();
			this.$refs.investorSubHoldGrid.load(this.searchParam);
		},

		onSubmitError() {
			this.$refs.uploadRef.doReset();
			this.$refs.submitBtn.setIconStyle(1, []);
		},
		onModifySubmitSuccess() {
			this.$refs.uploadModifyBtnRef.setIconStyle(1);
		},

		onModifySubmitError() {
			this.$refs.uploadModifyBtnRef.setIconStyle(1);
		},

		submitUploadParam() {
			//文件上传校验
			let temp = document.getElementsByClassName("upload-demo");
			let lis = temp[0].childNodes[1].childNodes.length;
			if (lis > 0) {
				this.$refs.uploadRef.upload();
			} else {
				this.$message.error("上传文件不能为空!");
				return false;
			}
		},
		submitUploadModifyParam() {
			//文件上传校验
			var validate = this.$refs.addModifyForm.validate();
			if (validate) {
				let formData = { reportDate: ''};
				let temp = document.getElementsByClassName('upload-demo');
				let lis = temp[0].childNodes[1].childNodes.length;
				if (lis > 0) {
				this.$refs.uploadModifyBtnRef.setIconStyle(0);
				this.$refs.uploadModifyRef.upload(formData);
				setTimeout(()=>{
					this.$refs.uploadInvestorSubHoldInfoModifyPopup.close();
				}, 300)
				} else {
				Tools.alert("上传文件不能为空!", "danger");
				}
			}
			return false;
		},
		loadGriding(val){
            this.$refs.validateInfoPopup.close();
        }
	},
	created() {
    let yesterday = new Date();
    yesterday.setDate(yesterday.getDate() -1);
    let year = yesterday.getFullYear(); //获取年
    let month = yesterday.getMonth(); //获取月
    let date = yesterday.getDate(); //得到日期
    month = month + 1;
    month = month.toString().padStart(2, "0");
    date = date.toString().padStart(2, "0");
    let  defaultDate = `${year}${month}${date}`;
    this.$set(this.searchParam, "holdDate", defaultDate);
	this.httpUtil.sysDate().then((res) => {
		this.workDate = res;
	});
  },

};
</script>
