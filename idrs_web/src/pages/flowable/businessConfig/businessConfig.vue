<!--业务配置-->
<template>
  <div class="py-page">
    <k-form-search-customize data-target="grid" data-label-width="80px">
      <k-form-item label="流程" data-label-width="50px">
        <k-field-select v-model="queryParams.processKey" dataContentType="json" data-url="wf/deploy/list.json" data-params="{start:0,limit: 10000000}" data-value-field="processKey" data-display-field="processName" />
      </k-form-item>
      <k-form-item label="业务操作">
        <k-field-select v-model="queryParams.server" data-action="ServerMethod.find" data-params="{type:2}" data-value-field="server" data-display-field="name" />
      </k-form-item>
    </k-form-search-customize>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" dataContentType="json" :data-handler="()=>{this.formData={};this.title='新增'}" data-target="addPopup">
            <md-icon md-src="/static/svg/add.svg" />新增
          </k-btn>
        </div>
      </div>
      <k-grid ref="grid" dataContentType="json" @data-row-select="selectRow" data-url='wf/busConfig/list.json' :data-params="queryParams">
        <k-grid-column data-align="center" data-header="业务操作" data-name="serverDesc"></k-grid-column>
        <k-grid-column data-align="center" data-header="流程名" data-name="processName"></k-grid-column>
        <k-grid-column data-align="center" data-header="主键" data-name="busKeys"></k-grid-column>
        <k-grid-column data-align="center" data-header="主键名称" data-name="busName"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="编辑" :data-handler="editDataHandler" data-functype="POPUP" data-size="mini" data-target="addPopup">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" dataContentType="json" :data-url="'wf/busConfig/delete/'+scope.row.row.server+'.json'" data-size="mini" data-type="danger" data-target="grid" :data-confirm="true" data-descript="删除">
            <md-icon>close</md-icon>
          </k-btn>
          <el-switch v-model="scope.row.row.status" active-color="#00a6ec" :active-value="1" :inactive-value="0" @change="statusChange(scope.row.row)" />
        </template>
      </k-grid>
    </div>
    <k-popup ref="addPopup" :data-title="title" >
      <k-form ref="addForm" :data-col="1" data-ui="element">
        <k-form-item label="业务">
          <k-field-text v-if="title==='编辑'" v-model="formData.serverDesc" :data-disabled="true" :data-allowblank="false"></k-field-text>
          <k-field-tree v-else v-model="formData.server" data-diffcondition="id,parentId" :data-multiple="false" data-action="WfBusinessConfig.findServerMethodTree" :data-flat="false" :data-allowblank="false" data-display-child="children" data-placeholder="请选择业务" data-display-field="name" data-value-field="id" :data-disable-branch-nodes="true" />
        </k-form-item>
        <k-form-item label="流程">
          <k-field-select v-model="formData.processKey" dataContentType="json" data-url="wf/deploy/list.json" data-params="{start:0,limit: 10000000}" data-value-field="processKey" data-display-field="processName" :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="主键">
          <k-field-text v-model="formData.busKeys" :data-allowblank="false" data-placeholder="多个主键用英文逗号分割" data-regx="^[a-zA-Z0-9_,]+$" data-regx-text="请输入正确的主键" :data-max-length="50" />
        </k-form-item>
        <k-form-item label="主键名称">
          <k-field-text v-model="formData.busName" :data-allowblank="false" data-placeholder="多个主键名称用英文逗号分割"  data-regx-text="请输入正确的主键名称" :data-max-length="50" />
        </k-form-item>
        <k-form-item label="状态" v-show="false">
          <k-field-select v-model="formData.status" :data-allowblank="true" :data-data="stateOptions" data-display-field="label" data-value-field="value" />
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" dataContentType="json" :data-url="title==='新增'?'wf/busConfig/add.json':'wf/busConfig/update.json'" data-target="grid" data-from="addForm" :data-model="formData">
            <md-icon md-src="/static/svg/confirm.svg" />
            确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg" />
            取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

  </div>

</template>

<script>
import { assign } from "lodash";
import Tools from "@/utils/tools.js";
export default {
	name: "BusinessConfig",
	data() {
		return {
			formData: {},
			queryParams: {
				processKey: null,
				server: null,
			},
			selectRowData: {},
			title: "",
			stateOptions: [
				{ label: "启用", value: 1 },
				{ label: "停用", value: 0 },
			],
		};
	},
	methods: {
		editDataHandler() {
			this.title = "编辑";
		},
		selectRow(row, column, event) {
			const _this = this;
			_this.selectRowData = assign({}, row);
			_this.formData = assign({}, row);
		},
		statusChange(row) {
			this.httpUtil
				.ajaxJson({
					url: "wf/busConfig/update.json",
					params: row,
				})
				.then((response) => {
					this.$refs.grid.load();
					Tools.alert(`${row.status == 1 ? "启用" : "停用"}成功`);
				});
		},
	},
};
</script>

<style lang="scss" scoped>
</style>
