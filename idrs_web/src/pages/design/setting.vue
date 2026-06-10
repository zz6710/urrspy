<template>
	<div>
		<k-form-search data-target="menuGrid" data-model-name="Menu">
			<k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="resetData" data-target="addMenuPopup"> <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
		</k-form-search>
		<k-grid
			ref="menuGrid"
			@data-row-select="selectRow"
			data-action="Menu.find"
			data-diffcondition="menuid,upperid"
			:data-show-tree="true"
			dataTreeId="menuid"
			data-operate-width="210px"
		>
			<k-grid-column data-header="菜单ID" data-name="menuid"></k-grid-column>
			<k-grid-column data-header="菜单名称" data-name="menuname"></k-grid-column>
			<k-grid-column data-header="菜单短名称" data-name="shortname"></k-grid-column>
			<k-grid-column data-header="页面URL" data-name="url"></k-grid-column>
			<k-grid-column data-header="加载顺序" data-name="loadorder"></k-grid-column>
			<k-grid-column data-header="菜单状态" data-name="status" data-dict="menu_status"></k-grid-column>

			<template slot="operate">
				<k-btn
					class="md-info md-just-icon md-simple"
					data-descript="修改菜单"
					data-functype="POPUP"
					data-size="mini"
					data-target="editMenuPopup"
					:data-handler="editInitData"
				>
					<md-icon>edit</md-icon>
				</k-btn>
				<k-btn
					class="md-danger md-just-icon md-simple"
					data-functype="SUBMIT"
					data-action="Menu.delete"
					data-size="mini"
					data-type="danger"
					data-target="menuGrid"
					:data-confirm="true"
					data-descript="删除菜单"
				>
					<md-icon>close</md-icon>
				</k-btn>
			</template>
		</k-grid>

		<!--    添加机构弹出框   -->
		<k-popup ref="addMenuPopup" data-title="新增">
			<el-radio-group class="formRadio" v-model="formData.type">
				<el-radio-button label="0">低代码菜单</el-radio-button>
				<el-radio-button label="1">普通菜单</el-radio-button>
			</el-radio-group>
			<k-form ref="addMenuForm" :data-col="2">
				<k-form-item label="菜单ID">
					<k-field-text v-model="formData.menuid" :dataAllowblank="false" />
				</k-form-item>
				<k-form-item label="菜单名称">
					<k-field-text v-model="formData.menuname" :dataAllowblank="false" />
				</k-form-item>
				<k-form-item label="菜单短名称">
					<k-field-text v-model="formData.shortname" />
				</k-form-item>
				<k-form-item label="父级菜单ID">
					<k-field-cascader
						style="width: 100%"
						ref="parentAdd"
						v-model="formData.upperid"
						data-diffcondition="menuid,upperid"
						data-action="Menu.find"
						data-display-child="children"
						data-check-strictly
						data-show-num
						:data-props="{ expandTrigger: 'hover' }"
						data-size="medium"
						data-placeholder="请选择父级菜单"
						data-clearable
						data-fileterable
						data-display-field="menuname"
						data-value-field="menuid"
						:data-max-length="32"
					>
					</k-field-cascader>
				</k-form-item>
				<k-form-item v-if="formData.type == '1'" label="菜单url">
					<k-field-text v-model="formData.url" :dataAllowblank="false"></k-field-text>
				</k-form-item>
				<k-form-item v-else label="所属模块">
					<k-field-select
						v-model="currentMoudle"
						:data-data="moudleList"
						data-display-field="name"
						data-value-field="id"
						@data-on-change="changeMoudle"
						:dataAllowblank="false"
					></k-field-select>
				</k-form-item>
				<k-form-item v-if="currentMoudle" label="所属功能">
					<k-field-select
						v-model="currentFunc"
						:data-data="funcLists"
						data-display-field="name"
						data-value-field="id"
						:dataAllowblank="false"
						@data-on-change="changeFunc"
					></k-field-select>
				</k-form-item>
				<k-form-item label="图标">
					<k-field-text v-model="formData.icon"></k-field-text>
				</k-form-item>
				<k-form-item label="加载顺序">
					<k-field-text v-model="formData.loadorder" :dataAllowblank="false"></k-field-text>
				</k-form-item>
				<k-form-item label="菜单状态">
					<k-field-select v-model="formData.status" data-dict="menu_status" data-default-value="N"></k-field-select>
				</k-form-item>
				<k-form-item label="备注">
					<k-field-text v-model="formData.remark"></k-field-text>
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="Menu.add"
						data-target="menuGrid"
						data-from="addMenuForm"
						:data-model="formData"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>

		<!--    修改机构弹出框   -->
		<k-popup ref="editMenuPopup" data-title="修改">
			<el-radio-group class="formRadio" v-model="formData.type">
				<el-radio-button label="0">低代码菜单</el-radio-button>
				<el-radio-button label="1">普通菜单</el-radio-button>
			</el-radio-group>
			<k-form ref="editMenuForm" :data-col="2">
				<k-form-item label="菜单ID">
					<k-field-text v-model="formData.menuid" :dataAllowblank="false" :data-display="true" />
				</k-form-item>
				<k-form-item label="菜单名称">
					<k-field-text v-model="formData.menuname" :dataAllowblank="false" />
				</k-form-item>
				<k-form-item label="菜单短名称">
					<k-field-text v-model="formData.shortname" />
				</k-form-item>
				<k-form-item label="父级菜单ID">
					<k-field-cascader
						style="width: 100%"
						ref="parentAdd"
						v-model="formData.upperid"
						data-diffcondition="menuid,upperid"
						data-action="Menu.find"
						data-display-child="children"
						data-check-strictly
						data-show-num
						:data-props="{ expandTrigger: 'hover' }"
						data-size="medium"
						data-placeholder="请选择父级菜单"
						data-clearable
						data-fileterable
						data-display-field="menuname"
						data-value-field="menuid"
						:data-max-length="32"
					>
					</k-field-cascader>
				</k-form-item>
				<k-form-item v-if="formData.type == '1'" label="菜单url">
					<k-field-text v-model="formData.url"></k-field-text>
				</k-form-item>
				<k-form-item v-else label="所属模块">
					<k-field-select
						v-model="currentMoudle"
						:data-data="moudleList"
						data-display-field="name"
						data-value-field="id"
						@data-on-change="changeMoudle"
						:dataAllowblank="false"
					></k-field-select>
				</k-form-item>
				<k-form-item v-if="currentMoudle" label="所属功能">
					<k-field-select
						v-model="currentFunc"
						:data-data="funcLists"
						data-display-field="name"
						data-value-field="id"
						:dataAllowblank="false"
						@data-on-change="changeFunc"
					></k-field-select>
				</k-form-item>
				<k-form-item label="图标">
					<k-field-text v-model="formData.icon"></k-field-text>
				</k-form-item>
				<k-form-item label="加载顺序">
					<k-field-text v-model="formData.loadorder" :dataAllowblank="false"></k-field-text>
				</k-form-item>
				<k-form-item label="菜单状态">
					<k-field-select v-model="formData.status" data-dict="menu_status" :dataAllowblank="false"></k-field-select>
				</k-form-item>
				<k-form-item label="备注">
					<k-field-text v-model="formData.remark"></k-field-text>
				</k-form-item>

				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="Menu.update"
						data-target="menuGrid"
						data-from="editMenuForm"
						:data-model="formData"
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
import Tools from "@/utils/tools.js";
import { assign, cloneDeep } from "lodash";

export default {
	data() {
		return {
			formData: {
				type: "0",
			},
			cascaderValue: "",
			selectRowData: {},
			system: {
				id: "",
				version: "",
			},
			moudleList: [],
			funcLists: [],
			currentFunc: "",
			currentMoudle: "",
		};
	},
	created() {
		this.initData();
	},

	methods: {
		resetData() {
			this.formData = {
				type: "0",
			};
			this.currentFunc = "";
			this.currentMoudle = "";
		},
		async initData() {
			await this.getSys();
			await this.getMoudleList();
		},
		async editInitData(val) {
			console.log(val);
			if (val.url && val.url.includes("dynamic/")) {
				let index = val.url.indexOf("/");
				let id = val.url.substring(index + 1);

				let res = await this.httpUtil.comnQuery({
					action: "LowCodeConfig.findConfigById",
					params: { id: id },
				});
				if (res.success) {
					this.currentFunc = res.returndata.id;
					this.currentMoudle = res.returndata.moduleId;
					this.getFuncList({
						sysVersion: this.system.version,
						moduleId: this.currentMoudle,
					});
				}
			}
		},
		async getSys() {
			let res = await this.httpUtil.comnQuery({
				action: "LowCodeSysVersion.getCurrentVersion",
			});
			if (res.success) {
				this.system.id = res.returndata.id;
				this.system.version = res.returndata.version;
			}
		},
		async getMoudleList() {
			let res = await this.httpUtil.comnQuery({
				action: "LowCodeModule.page",
				params: { sysId: this.system.id },
			});
			this.moudleList = cloneDeep(res.rows);
		},
		changeMoudle(val) {
			this.getFuncList({
				sysVersion: this.system.version,
				moduleId: val,
			});
		},
		changeFunc(val) {
			this.formData.url = "dynamic/" + val;
		},
		async getFuncList(params) {
			let res = await this.httpUtil.comnQuery({
				action: "LowCodeConfig.page",
				params: params,
			});
			this.funcLists = cloneDeep(res.rows);
		},
		selectRow(row, column, event) {
			const _this = this;
			_this.selectRowData = assign({}, row);
			_this.formData = assign({}, row);
			console.log("_this.formData: ", _this.formData);
			if (_this.formData.url && _this.formData.url.includes("dynamic/")) {
				_this.formData.type = "0";
			} else {
				_this.formData.type = "1";
				console.log(2, _this.formData);
			}
		},
	},
};
</script>
<style scoped>
.formRadio {
	margin: 10px 50px;
}
</style>
