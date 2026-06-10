<template>
	<div class="py-page">
		<div>
			<k-form-search-customize
				data-model-name="T8SQLConfigModel"
				data-label-width="80px"
				v-model="searchParam"
				data-target="T8SQLConfigModelGrid"
			>
				<k-form-item label="exeid">
					<k-field-text v-model="searchParam.exeid"></k-field-text>
				</k-form-item>
				<k-form-item label="sqlid">
					<k-field-text v-model="searchParam.sqlid"></k-field-text>
				</k-form-item>
				<k-form-item label="任务名称">
					<k-field-select
						v-model="searchParam.taskId"
						data-action="KbatchTaskInfo.findTaskInfos"
						data-display-field="taskId,taskName"
						data-value-field="taskId"
					/>
				</k-form-item>
			</k-form-search-customize>
		</div>
		<div class="py-page-container">
			<div class="table-top-btns">
				<k-btn
					class="btn-custom-primary"
					data-functype="POPUP"
					:data-handler="() => (this.formData = {})"
					data-target="addT8SQLConfigModelPopup"
					slot="button"
				>
					<md-icon md-src="/static/svg/add.svg" />新增</k-btn
				>
			</div>
			<k-grid
				ref="T8SQLConfigModelGrid"
				@data-row-select="selectRow"
				data-action="T8SQLConfigModel.findT8SQLConfigModels"
				:data-page-size="10"
				data-operate-width="250px"
			>
				<k-grid-column data-header="exeid" data-name="exeid"></k-grid-column>
				<k-grid-column data-header="sqlid" data-name="sqlid"></k-grid-column>
				<k-grid-column data-header="执行优先级" data-name="exeOrder"></k-grid-column>
				<k-grid-column data-header="sql内容描述" data-name="desc"></k-grid-column>
				<k-grid-column data-header="sql语句" data-name="sqlstr" :data-hidden="true"></k-grid-column>
				<k-grid-column data-header="任务id" data-name="taskId"></k-grid-column>
				<k-grid-column data-header="任务名称" data-name="taskName"></k-grid-column>
				<k-grid-column data-header="版本号" data-name="version"></k-grid-column>
				<k-grid-column data-header="更新日期" data-name="operationDate"></k-grid-column>
				<k-grid-column data-header="更新时间" data-name="operationTime"></k-grid-column>
				<k-grid-column data-header="更新人" data-name="username"></k-grid-column>
				<template slot="operate" slot-scope="scope">
					<k-btn
						class="btn-custom-text"
						data-descript="修改接口sql配置"
						data-functype="POPUP"
						data-size="mini"
						:data-handler="setExeidBool"
						data-target="editT8SQLConfigModelPopup"
					>
						修改
					</k-btn>
					<k-btn
						class="btn-custom-text"
						data-functype="SUBMIT"
						data-action="T8SQLConfigModel.deleteT8SQLConfigModel"
						:data-confirm="true"
						data-size="mini"
						data-type="danger"
						data-target="T8SQLConfigModelGrid"
						data-descript="删除接口sql配置"
					>
						删除
					</k-btn>
				</template>
			</k-grid>
		</div>

		<!--    添加接口sql配置弹出框   -->
		<k-popup ref="addT8SQLConfigModelPopup" data-title="新增">
			<k-form ref="addT8SQLConfigModelForm" :data-col="2">
				<k-form-item label="任务名称">
					<k-field-select
						v-model="formData.taskId"
						data-action="KbatchTaskInfo.findTaskInfos"
						data-display-field="taskId,taskName"
						data-value-field="taskId"
						@data-on-change="setExeid"
					/>
				</k-form-item>
				<k-form-item label="执行优先级">
					<k-field-text
						v-model="formData.exeOrder"
						:data-allowblank="false"
						@data-on-change="setExeid"
						data-validate-type="int"
					/>
				</k-form-item>

				<k-form-item label="exeid">
					<k-field-text v-model="formData.exeid" :data-allowblank="false" :data-disabled="bool" />
				</k-form-item>
				<k-form-item label="sqlid">
					<k-field-text v-model="formData.sqlid" :data-disabled="bool" />
				</k-form-item>
				<k-form-item label="sql内容描述">
					<k-field-text v-model="formData.desc" />
				</k-form-item>
				<k-form-item label="sql语句" :data-col="2" class="form-item-100">
          <k-tooltip data-content="使用$LIST{}可以引用 接口SQL参数配置 的SQL"></k-tooltip>
					<k-field-text v-model="formData.sqlstr" :data-allowblank="false" inputType="textarea" :rows="8" />
				</k-form-item>

				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="T8SQLConfigModel.addT8SQLConfigModel"
						data-from="addT8SQLConfigModelForm"
						:data-model="formData"
						data-target="T8SQLConfigModelGrid"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE">
						<md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn
					>
				</k-form-footer>
			</k-form>
		</k-popup>

		<!--    修改接口sql配置弹出框   -->
		<k-popup ref="editT8SQLConfigModelPopup" data-title="修改">
			<k-form ref="editT8SQLConfigModelForm" :data-col="2">
				<k-form-item label="任务名称">
					<k-field-select
						v-model="formData.taskId"
						data-action="KbatchTaskInfo.findTaskInfos"
						data-display-field="taskId,taskName"
						data-value-field="taskId"
						@data-on-change="setExeid"
					/>
				</k-form-item>
				<k-form-item label="执行优先级">
					<k-field-text v-model="formData.exeOrder" :data-allowblank="false" />
				</k-form-item>

				<k-form-item label="exeid">
					<k-field-text v-model="formData.exeid" :data-allowblank="false" :data-disabled="bool" />
				</k-form-item>
				<k-form-item label="sqlid">
					<k-field-text v-model="formData.sqlid" :data-disabled="bool" />
				</k-form-item>
				<k-form-item label="version">
					<k-field-text v-model="formData.version" :data-disabled="bool" />
				</k-form-item>
				<k-form-item label="sql内容描述">
					<k-field-text v-model="formData.desc" />
				</k-form-item>
				<k-form-item label="sql语句" :data-col="2" class="form-item-100">
          <k-tooltip data-content="使用$LIST{}可以引用 接口SQL参数配置 的SQL"></k-tooltip>
					<k-field-text v-model="formData.sqlstr" inputType="textarea" :rows="8" :data-allowblank="false" />
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="T8SQLConfigModel.updateT8SQLConfigModel"
						data-from="editT8SQLConfigModelForm"
						:data-model="formData"
						data-target="T8SQLConfigModelGrid"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE">
						<md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn
					>
				</k-form-footer>
			</k-form>
		</k-popup>
	</div>
</template>

<script>
export default {
	name: "T8SQLConfigModel",
	data() {
		return {
			formData: {},
			selectRowData: {},
			searchParam: {},
			bool: false,
		};
	},
	methods: {
		setExeidBool(val) {
			if (!val.taskId || !val.exeOrder) {
				this.bool = false;
			} else {
				this.bool = true;
			}
			this.httpUtil
				.comnQuery({
					action: "T8SQLConfigModel.getMaxVersion",
					params: { taskId: val.taskId, exeid: val.exeid },
				})
				.then((data) => {
					this.nextVersion = data.rows;
					this.$set(this.formData, "version", data.rows[0].version);
				})
				.catch({});
		},

		setExeid() {
			let taskId = this.formData.taskId;
			let exeOrder = this.formData.exeOrder;
			if (!taskId) {
				this.formData.exeid = "BUSINESSEU";
				this.formData.sqlid = "BUSINESSU";
				this.bool = false;
			} else {
				let a = "";
				if (exeOrder) {
					a = exeOrder.length === 1 ? "0" + exeOrder : exeOrder;
				}
				this.$set(this.formData, "exeid", taskId + "EU" + a);
				this.$set(this.formData, "sqlid", taskId + "U" + a);
				this.bool = true;
			}
		},

		selectRow(row, column, event) {
			this.selectRowData = Object.assign({}, row);
			this.formData = Object.assign({}, row);
		},
	},
};
</script>
<style lang="scss" scoped>
.form-item-100 {
	width: 100%;
}
</style>
