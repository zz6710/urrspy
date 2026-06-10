<template>
	<div class="py-page">
		<div>
			<k-form-search-customize v-model="queryParam" data-target="counterPartyGrid">
				<k-form-item label="交易对手名称">
					<k-field-text v-model="queryParam.counterPartyName"></k-field-text>
				</k-form-item>
				<k-form-item label="产品登记编码">
					<k-field-text v-model="queryParam.regCode"></k-field-text>
				</k-form-item>
				<k-form-item label="关联交易情况">
					<k-field-select v-model="queryParam.counterType" data-dict="related_party_trans" data-dict-type="1"></k-field-select>
				</k-form-item>		
				<k-form-item label="托管行名称">
					<k-field-text v-model="queryParam.careName"></k-field-text>
				</k-form-item>	
				<k-form-item label="数据来源">
					<k-field-select v-model="queryParam.dataFrom" data-dict="data_from" data-dict-type="1"></k-field-select>
				</k-form-item>		
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
		   <k-btn class="btn-custom-primary" data-functype="POPUP" slot="button" :data-handler="()=>this.formData={}" data-target="addCounterPartyPopup">
          	<md-icon md-src="/static/svg/add.svg" />新增</k-btn> 
			<k-btn
				slot="button"
				class="btn-custom-plain"
				data-functype="EXPORT"
				data-export-dict="true"
				data-target="counterPartyGrid"
				:data-export-name="'关联交易映射表'"
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
				data-action="DwdLinkedTransMapping.findLinkedTransMappings"
				data-dict-type="1"
			>
				<k-grid-column data-header="交易对手名称" data-name="counterPartyName"></k-grid-column>
				<k-grid-column data-header="关联交易情况" data-name="counterType" data-dict="related_party_trans"></k-grid-column>
				<k-grid-column data-header="产品登记编码" data-name="regCode"></k-grid-column>
				<k-grid-column data-header="备注" data-name="remark"></k-grid-column>
				<k-grid-column data-header="托管行名称" data-name="careName"></k-grid-column>
				<k-grid-column data-header="数据来源" data-name="dataFrom" data-dict="data_from"></k-grid-column>
				<k-grid-column data-header="更新人" data-name="userId"></k-grid-column>
				<k-grid-column data-header="更新时间" data-name="updateTime"></k-grid-column>
				<template slot="operate" slot-scope="scope">
					<k-btn
						class="btn-custom-text"
						data-functype="POPUP"
						data-size="mini"
						data-target="editCounterPartyPopup"
					>
						修改
					</k-btn>
					<k-btn
						class="btn-custom-text"
						data-functype="SUBMIT"
						data-action="DwdLinkedTransMapping.delLinkedTransMapping"
						data-target="counterPartyGrid"
						:data-confirm="true"
					>
						删除
					</k-btn>
				</template>
			</k-grid>
		</div>

		<k-popup ref="addCounterPartyPopup" data-title="新增">
			<k-form ref="addCounterPartyForm" :data-col="2" data-label-width="160px">
				<k-form-item label="交易对手名称">
					<k-field-text v-model="formData.counterPartyName" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="关联交易情况">
					<k-field-select v-model="formData.counterType" :data-allowblank="false" data-dict="related_party_trans" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="产品登记编码">
					<k-field-text v-model="formData.regCode" :data-allowblank="false"/>
				</k-form-item>
				<k-form-item label="备注">
					<k-field-text v-model="formData.remark" />
				</k-form-item>
				<k-form-item label="托管行名称">
					<k-field-text v-model="formData.careName" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="数据来源">
					<k-field-select v-model="formData.dataFrom" :data-allowblank="false" data-dict="data_from" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="更新时间" v-show="false">
					<k-field-text v-model="formData.updateTime" />
				</k-form-item>				
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="DwdLinkedTransMapping.addLinkedTransMapping"
						data-from="addCounterPartyForm"
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

		<!--    修改CounterPartyModel弹出框   -->
		<k-popup ref="editCounterPartyPopup" data-title="修改">
			<k-form ref="editCounterPartyForm" :data-col="2" data-label-width="160px">
				<k-form-item label="交易对手名称">
					<k-field-text v-model="formData.counterPartyName" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="关联交易情况">
					<k-field-select v-model="formData.counterType" :data-allowblank="false" data-dict="related_party_trans" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="产品登记编码">
					<k-field-text v-model="formData.regCode" :data-allowblank="false"/>
				</k-form-item>
				<k-form-item label="备注">
					<k-field-text v-model="formData.remark" />
				</k-form-item>	
				<k-form-item label="托管行名称">
					<k-field-text v-model="formData.careName" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="数据来源">
					<k-field-select v-model="formData.dataFrom" :data-allowblank="false" data-dict="data_from" data-dict-type="1" />
				</k-form-item>			
				<k-form-item label="更新人" v-show="false">
					<k-field-text v-model="formData.userId" v-show="false" />
				</k-form-item>
				<k-form-item label="更新时间" v-show="false">
					<k-field-text v-model="formData.updateTime" />
				</k-form-item>				
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="DwdLinkedTransMapping.updateLinkedTransMapping"
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
	</div>
</template>

<script>
import Tools from "@/utils/tools";
import AssetMixin from "@/pages/pms/subject/mixins/assetMixin.js"
export default {
	name: "DwdLinkedTransMapping",
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
		selectRow(row, column, event) {
			this.selectRowData = Object.assign({}, row);
			this.formData = Object.assign({}, row);
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
	},
};
</script>
