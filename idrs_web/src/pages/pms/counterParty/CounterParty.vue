<template>
	<div class="py-page">
		<div>
			<k-form-search-customize v-model="queryParam" data-target="counterPartyGrid">
				<k-form-item label="统一社会信用代码">
					<k-field-text v-model="queryParam.counterPartyCd"></k-field-text>
				</k-form-item>
				<k-form-item label="债券发行人">
					<k-field-text v-model="queryParam.counterPartyNm"></k-field-text>
				</k-form-item>
				<k-form-item label="企业规模">
					<k-field-select v-model="queryParam.enterpScale" data-dict="pbc_enterprise_scale" data-dict-type="1"></k-field-select>
				</k-form-item>
				<k-form-item label="发行人注册地">
					<k-field-select v-model="queryParam.registerArea" data-dict="bond_register_area" data-dict-type="1"></k-field-select>
				</k-form-item>
				<k-form-item label="">
					<el-checkbox v-model="queryParam.enterpScaleFu" style="margin: 0 20px 0 40px" true-label="1" false-label="0">筛选非金融企业债中企业规模为空的数据</el-checkbox>
				</k-form-item>
				<k-form-item label="">
					<el-checkbox v-model="queryParam.registerAreaFu" style="margin: 0 20px 0 40px" true-label="1" false-label="0">筛选发行人注册地为空的数据</el-checkbox>
				</k-form-item>
				<!--  <k-btn class="btn-custom-primary" data-functype="POPUP" slot="button" :data-handler="()=>this.formData={}" data-target="addCounterPartyPopup">
          <md-icon md-src="/static/svg/add.svg" />新增</k-btn>  -->
			</k-form-search-customize>
		</div>
		<div class="py-page-container">
			<div class="table-top-btns">
				<div class="left">
					<!-- <k-btn slot="button" style="width: 120px" class="btn-custom-plain" :data-download-name="'交易对手信息导入模板'+'.xlsx'"
              data-descript="下载模板" data-functype="DOWNLOAD" data-size="small"
              data-url="/download/server/DpsApp/counterPartyInfo/comn-download.json">
            <md-icon>cloud_download</md-icon>
            下载模板
          </k-btn> -->
					 <k-btn slot="button" ref="uploadBtnRef" class="btn-custom-plain"  data-functype="POPUP" data-target="initPopup"
                  v-if="global.isShowAuthorityButton('CounterPartyModel.CounterPartyImport')" :load-disabled="false">
            <md-icon>backup</md-icon>
            增量导入
          </k-btn>
					<k-btn
						slot="button"
						class="btn-custom-plain"
						data-functype="EXPORT"
						data-export-dict="true"
						data-target="counterPartyGrid"
						v-if="global.isShowAuthorityButton('CounterPartyModel.CounterPartyExport')"
						:data-export-name="'债券发行人信息'"
					>
						<md-icon>cloud_download</md-icon>
						导出
					</k-btn>
				</div>
			</div>
			<k-grid
				ref="counterPartyGrid"
				@data-row-select="selectRow"
				data-fixed="right"
				data-operate-width="120px"
				data-action="CounterPartyModel.findCounterPartyModel"
				data-dict-type="1"
			>
				<k-grid-column data-header="统一社会信用代码" data-name="counterPartyCd"></k-grid-column>
				<!--<k-grid-column data-header="交易对手简称" data-name="counterPartyShtNm"></k-grid-column>-->
				<k-grid-column data-header="债券发行人" data-name="counterPartyNm"></k-grid-column>
				 <!--<k-grid-column data-header="交易对手类型" data-name="counterPartyType" data-dict="counterparty_type_zg"></k-grid-column>-->
				 <!--<k-grid-column data-header="SPV人行登记产品代码" data-name="spvPbanEnc"></k-grid-column>-->
				 <!--<k-grid-column data-header="SPV人行登记机构代码" data-name="spvOrgEnc"></k-grid-column>-->
				<k-grid-column data-header="企业规模" data-name="enterpScale" data-dict="pbc_enterprise_scale"></k-grid-column>
				<!-- <k-grid-column data-header="机构编码" data-name="orgCd" data-width="120"></k-grid-column> -->
				<!-- <k-grid-column data-header="机构名称" data-name="orgNm"  data-width="250"></k-grid-column> -->
				<!-- <k-grid-column data-header="机构种类" data-name="orgTyp"  data-dict="org_type"  data-width="160"></k-grid-column> -->
				<!-- <k-grid-column data-header="SPV人行编码" data-name="spvPbanEnc"  data-width="150"></k-grid-column> -->
				<!-- <k-grid-column data-header="备注" data-name="remark"  data-width="200"></k-grid-column> -->
				<!-- <k-grid-column data-header="版本号" data-name="version"  data-width="60"></k-grid-column> -->
				<k-grid-column data-header="发行人注册地" data-name="registerArea" data-dict="bond_register_area"></k-grid-column>
				<template slot="operate" slot-scope="scope">
					<k-btn
						class="btn-custom-text"
						data-functype="POPUP"
						data-size="mini"
						v-if="global.isShowAuthorityButton('CounterPartyModel.updateCounterPartyModel')"
						data-target="editCounterPartyPopup"
					>
						修改
					</k-btn>
					<k-btn
						class="btn-custom-text"
						data-functype="SUBMIT"
						data-action="CounterPartyModel.deleteCounterPartyModel"
						data-target="counterPartyGrid"
						:data-confirm="true"
					>
						删除
					</k-btn>
				</template>
			</k-grid>
		</div>

		<!--    修改CounterPartyModel弹出框   -->
		<k-popup ref="editCounterPartyPopup" data-title="修改" @data-opened="checkColumn()">
			<k-form ref="editCounterPartyForm" :data-col="2" data-label-width="160px">
				<k-form-item label="统一社会信用代码" :class="[handleItemDiff('counterPartyCd')]">
					<k-field-text v-model="formData.counterPartyCd" :data-allowblank="false" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="交易对手简称" v-show="false">
					<k-field-text v-model="formData.counterPartyShtNm" />
				</k-form-item>
				<k-form-item label="债券发行人" :class="[handleItemDiff('counterPartyNm')]">
					<k-field-text v-model="formData.counterPartyNm" />
				</k-form-item>
				<k-form-item label="交易对手类型" v-show="false">
					<k-field-select v-model="formData.counterPartyType" data-dict="counterparty_type_zg" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="SPV人行登记产品代码" v-show="false">
					<k-field-text v-model="formData.spvPbanEnc" v-show="false" />
				</k-form-item>
				<k-form-item label="SPV人行登记机构代码" v-show="false">
					<k-field-text v-model="formData.spvOrgEnc" />
				</k-form-item>
				<k-form-item label="企业规模" :class="[handleItemDiff('enterpScale')]">
					<k-field-select v-model="formData.enterpScale" data-dict="pbc_enterprise_scale" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="发行人注册地" :class="[handleItemDiff('registerArea')]">
					<k-field-select v-model="formData.registerArea" data-dict="bond_register_area" data-dict-type="1" />
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="CounterPartyModel.updateCounterPartyModel"
						data-from="editCounterPartyForm"
						:data-model="formData"
						data-target="counterPartyGrid"
						:handle-before="handleBefore"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>

		<k-popup ref="initPopup" title="交易对手信息导入" @data-opened="uploadOpened()">
			<k-form ref="addForm" data-ui="element">
				<k-form-item label="交易对手信息导入" data-ui="element" data-input-width="500px">
					<k-field-excel-upload
						data-type="file"
						ref="uploadRef"
						:data-multiple="false"
						:data-limit="1"
						data-accept=".xlsx,.xls"
						:data-error="onSubmitErrorLegal"
						:data-success="onSubmitDocSuccessLegal"
						:data-auto-upload="false"
						data-upload-url="/upload/server/DpsApp/counterPartyUpload.json"
					>
					</k-field-excel-upload>
				</k-form-item>
				<k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" ref="submitBtn"
                   :data-auto-upload="false" data-from="addForm" :data-handler="submitUploadParamLegal">
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
import AssetMixin from "@/pages/pms/subject/mixins/assetMixin.js"
export default {
	name: "CounterPartyModel",
	mixins: [AssetMixin],
	data() {
		return {
			formData: {},
			formDataCopy: {},
			selectRowData: {},
			queryParam: {},
			OrgDict: {},
			showFlag: true,
		};
	},
	methods: {
		handleBefore() {
			if (this.formNoChangeCb()) {
				this.$refs.editCounterPartyPopup.close();
				return false
			}
			return true
		},
		findOrgNm() {
			this.httpUtil
				.comnQuery({
					action: "T8OrgSheet.findOrgNm",
					params: { orgNbr: this.formData.orgNbrExt },
				})
				.then((data) => {
					this.formData.orgNm = data.rows[0].orgNm;
				})
				.catch({});
		},
		checkColumn() {
			// 请求债券字段
			this.httpUtil
				.comnQuery({
					action: "AssetCollection.findColumns",
					params: { page: "7" },
				})
				.then((data) => {
					let formDatas = { ...this.formData };
					for (let f in formDatas) {
						formDatas[f + "Disabled"] = true;
					}
					if (data && data.rows.length > 0) {
						let labels = data.rows[0].label;
						let arr = labels.split(",");
						if (arr.length > 0) {
							arr.forEach((a) => {
								formDatas[a + "Disabled"] = false;
								if (a == "rEMARK") {
									formDatas["remarkDisabled"] = formDatas[a + "Disabled"];
								}
							});
						}
					}
					this.formData = { ...formDatas };
					this.formData.spvProdRegEncDisabled = !(
						this.formData.orgTyp === "07" ||
						this.formData.orgTyp === "08" ||
						this.formData.orgTyp === "09" ||
						this.formData.orgTyp === "10" ||
						this.formData.orgTyp === "11" ||
						this.formData.orgTyp === "12" ||
						this.formData.orgTyp === "13" ||
						this.formData.orgTyp === "14" ||
						this.formData.orgTyp === "15" ||
						this.formData.orgTyp === "16"
					);
					this.formData.spvPbanEncDisabled = !(this.formData.orgTyp === "07");
					this.formDataCopy = Object.assign({}, this.formData);
				})
				.catch({});
		},
		selectRow(row, column, event) {
			this.selectRowData = Object.assign({}, row);
			this.formData = Object.assign({}, row);
		},
		downLoadFile() {
			Tools.alert("正在生成文件，请稍等！");
			this.flag = true;
			this.httpUtil.download({
				url: "download/server/DpsApp/conterPartyExpoertExcel.json",
				params: {
					heads: "交易对手编号,债券发行人,SPV机构编码,SPV人行编码",
					fields: "counter_party_cd,counter_party_nm,spv_org_enc,spv_prod_reg_enc",
					counterPartyCd: this.queryParam.counterPartyCd,
					counterPartyNm: this.queryParam.counterPartyNm,
					orgNm: this.queryParam.orgNm,
				},
				callback: (response) => {
					Tools.alert("下载完成");
					this.$refs.batchDownloadButton.setIconStyle(1, []);
					this.flag = false;
				},
			});
		},
		uploadOpened() {},
		onSubmitErrorLegal() {
			this.$refs.uploadBtnRef.setIconStyle(1);
		},
		onSubmitDocSuccessLegal() {
			this.$refs.counterPartyGrid.load();
			this.$refs.uploadBtnRef.setIconStyle(1);
		},
		submitUploadParamLegal() {
			//文件上传校验
			let validate = this.$refs.addForm.validate();
			if (validate) {
				let formData = {};
				let temp = document.getElementsByClassName("upload-demo");
				let lis = temp[0].childNodes[1].childNodes.length;
				if (lis > 0) {
					this.$refs.uploadBtnRef.setIconStyle(0)
					this.$refs.uploadRef.upload(formData);
					setTimeout(()=>{
						this.$refs.initPopup.close();
					}, 300)
				} else {
					this.$message.error("上传文件不能为空!");
					return false;
				}
			}
		},
		changeFlag(counterPartyType) {
			this.showFlag = !(
				counterPartyType === "01" ||
				counterPartyType === "02" ||
				counterPartyType === "03" ||
				counterPartyType === "04" ||
				counterPartyType === "05" ||
				counterPartyType === "06" ||
				counterPartyType === "17" ||
				counterPartyType === "99"
			);
			this.formData.spvOrgEncDisabled =
				counterPartyType === "01" ||
				counterPartyType === "02" ||
				counterPartyType === "03" ||
				counterPartyType === "04" ||
				counterPartyType === "05" ||
				counterPartyType === "06" ||
				counterPartyType === "17" ||
				counterPartyType === "99";
			this.formData.spvProdRegEncDisabled =
				counterPartyType === "01" ||
				counterPartyType === "02" ||
				counterPartyType === "03" ||
				counterPartyType === "04" ||
				counterPartyType === "05" ||
				counterPartyType === "06" ||
				counterPartyType === "17" ||
				counterPartyType === "99";
		},
	},
};
</script>
