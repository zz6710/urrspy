<template>
	<div class="py-page">
		<div>
			<k-form-search-customize data-model-name="DwdSpvInvPrdRft" data-label-width="130px" v-model="searchParam" data-target="tableGrid">
				<k-form-item label="特定目的载体代码">
					<k-field-text v-model="searchParam.spvCode" />
				</k-form-item>
				<k-form-item label="特定目的载体类型">
					<k-field-select v-model="searchParam.spvType" data-dict="spv_type" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="资管产品统计编码">
					<k-field-text v-model="searchParam.ampsCode" />
				</k-form-item>
			</k-form-search-customize>
		</div>
		<div class="py-page-container">
			<div class="table-top-btns">
				<div class="left">
					<k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="() => (this.formData = {})" data-target="addDwdSpvInvPrdRftPopup">
						<md-icon md-src="/static/svg/add.svg" />新增</k-btn
					>
				</div>
			</div>
			<k-grid
				ref="tableGrid"
				@data-row-select="selectRow"
				data-action="DwdSpvInvPrdRft.findDwdSpvInvPrdRfts"
				data-operate-width="140px"
				data-dict-type="1"
			>
				<k-grid-column data-header="特定目的载体代码" data-name="spvCode"></k-grid-column>
				<k-grid-column data-header="特定目的载体类型" data-name="spvType" data-dict="spv_type"></k-grid-column>
				<k-grid-column data-header="资管产品统计编码" data-name="ampsCode"></k-grid-column>
				<template slot="operate" slot-scope="scope">
					<k-btn
						class="btn-custom-text"
						data-descript="修改人行特定目的载体产品代码映射表"
						data-functype="POPUP"
						data-size="mini"
						data-target="editDwdSpvInvPrdRftPopup"
					>
						修改
					</k-btn>
					<k-btn
						class="btn-custom-text"
						data-functype="SUBMIT"
						data-action="DwdSpvInvPrdRft.deleteDwdSpvInvPrdRft"
						data-size="mini"
						data-type="danger"
						data-target="tableGrid"
						:data-confirm="true"
					>
						删除
					</k-btn>
				</template>
			</k-grid>
		</div>

		<!--    添加人行特定目的载体产品代码映射表弹出框   -->
		<k-popup ref="addDwdSpvInvPrdRftPopup" data-title="添加">
			<k-form ref="addDwdSpvInvPrdRftForm" :data-col="2" data-label-width="140px">
				<k-form-item label="特定目的载体代码">
					<k-field-text v-model="formData.spvCode" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="特定目的载体类型">
					<k-field-select v-model="formData.spvType" data-dict="spv_type" data-dict-type="1" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="资管产品统计编码">
					<k-field-text v-model="formData.ampsCode" :data-allowblank="false" />
				</k-form-item>

				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="DwdSpvInvPrdRft.addDwdSpvInvPrdRft"
						data-from="addDwdSpvInvPrdRftForm"
						:data-model="formData"
						data-target="tableGrid"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>

		<!--    修改人行特定目的载体产品代码映射表弹出框   -->
		<k-popup ref="editDwdSpvInvPrdRftPopup" data-title="编辑">
			<k-form ref="editDwdSpvInvPrdRftForm" :data-col="2" data-label-width="140px">
				<k-form-item label="特定目的载体代码" :class="[handleItemDiff('spvCode')]">
					<k-field-text v-model="formData.spvCode" :data-allowblank="false" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="特定目的载体类型" :class="[handleItemDiff('spvType')]">
					<k-field-select v-model="formData.spvType" data-dict="spv_type" data-dict-type="1" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="资管产品统计编码" :class="[handleItemDiff('ampsCode')]">
					<k-field-text v-model="formData.ampsCode" :data-allowblank="false" />
				</k-form-item>

				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="DwdSpvInvPrdRft.updateDwdSpvInvPrdRft"
						data-from="editDwdSpvInvPrdRftForm"
						:data-model="formData"
						data-target="tableGrid"
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
export default {
	name: "DwdSpvInvPrdRft",
	data() {
		return {
			formData: {},
			formDataCopy: {},
			selectRowData: {},
			searchParam: {},
		};
	},
	methods: {
		handleBefore() {
			if (this.formNoChangeCb()) {
				this.$refs.editDwdSpvInvPrdRftPopup.close();
				return false
			}
			return true
		},
		selectRow(row, column, event) {
			this.selectRowData = Object.assign({}, row);
			this.formData = Object.assign({}, row);
			this.formDataCopy = Object.assign({}, row);
		},
	},
};
</script>
