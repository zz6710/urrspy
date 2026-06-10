<template>
  <div class="py-page">
    <k-form-search-customize data-target="grid" v-model="queryParam" data-label-width="60px">
      <k-form-item label="流程名">
        <k-field-select v-model="queryParamProcessKey" dataContentType="json" data-url="wf/deploy/list.json" data-params="{start:0,limit: 10000000}" data-value-field="processKey" data-display-field="processName" />
      </k-form-item>
      <k-form-item label="状态">
        <k-field-select v-model="queryParamStatus" data-dict="surrogate_status" />
      </k-form-item>
    </k-form-search-customize>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn slot="button" data-functype="POPUP" class="btn-custom-primary" :data-handler="()=>this.formData={'status': '1'}" data-target="addPopup">
            <md-icon md-src="/static/svg/add.svg" />
            新增
          </k-btn>
        </div>
      </div>
    <k-grid ref="grid" @data-row-select="selectRow" data-url='wf/surrogate/list.json' dataContentType="json">
      <k-grid-column data-header="流程名" data-name="processName"></k-grid-column>
      <k-grid-column data-header="开始时间" data-name="startDate" data-type="date"></k-grid-column>
      <k-grid-column data-header="结束时间" data-name="endDate" data-type="date"></k-grid-column>
      <k-grid-column data-header="授权人" data-name="createName"></k-grid-column>
      <k-grid-column data-header="代理人" data-name="surrogateName"></k-grid-column>
      <k-grid-column data-header="状态" data-name="status" data-dict="surrogate_status"></k-grid-column>
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" data-descript="编辑" data-functype="POPUP" data-size="mini" data-target="editPopup">
          <md-icon>edit</md-icon>
        </k-btn>
        <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" dataContentType="json" :data-url='"wf/surrogate/delete/"+scope.row.row.id+".json"' data-size="mini" data-type="danger" data-target="grid" :data-confirm="true" data-descript="删除">
          <md-icon>close</md-icon>
        </k-btn>
      </template>
    </k-grid>
    </div>

    <k-popup ref="editPopup" data-title="修改" >
      <k-form ref="editForm" :data-col="1" data-ui="element">
        <k-form-item label="流程名">
          <k-field-select v-model="formData.processKey" ref="processSelect" dataContentType="json" data-url="wf/deploy/list.json" data-params="{start:0,limit: 10000000}" data-value-field="processKey" data-display-field="processName" :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="代理人">
          <k-field-select :data-allowblank="false" data-placeholder="请搜索" ref="surrogateSelect" v-model="formData.surrogate"  :data-remote="true" dataContentType="json" data-url="wf/system/user/list.json" data-display-field="username" data-value-field="userid" />
        </k-form-item>
        <k-form-item label="开始时间">
          <k-field-date v-model="formData.startDate" :data-allowblank="false" data-date-format="yyyy-MM-dd" :data-max-value="formData.endDate" />
        </k-form-item>
        <k-form-item label="结束时间">
          <k-field-date v-model="formData.endDate" :data-allowblank="false" data-date-format="yyyy-MM-dd" :data-min-value="formData.startDate" />
        </k-form-item>
        <k-form-item label="状态">
          <k-field-select data-dict="surrogate_status" v-model="formData.status" />
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" dataContentType="json" data-url="wf/surrogate/update.json" data-target="grid" data-from="addForm" :data-model="formData" :data-handler="submitDataHandler">
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

    <k-popup ref="addPopup" data-title="新增" >
      <k-form ref="addForm" :data-col="1" data-ui="element">
        <k-form-item label="流程名">
          <k-field-select v-model="formData.processKey" dataContentType="json" ref="processSelect" data-url="wf/deploy/list.json" data-params="{start:0,limit: 10000000}" data-value-field="processKey" data-display-field="processName" :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="代理人">
          <k-field-select :data-allowblank="false" data-placeholder="请搜索" ref="surrogateSelect" v-model="formData.surrogate"  :data-remote="true" dataContentType="json" data-url="wf/system/user/list.json" data-display-field="username" data-value-field="userid" />
        </k-form-item>
        <k-form-item label="开始时间">
          <k-field-date v-model="formData.startDate" :data-allowblank="false" :data-max-value="formData.endDate" />
        </k-form-item>
        <k-form-item label="结束时间">
          <k-field-date v-model="formData.endDate" :data-allowblank="false" :data-min-value="formData.startDate" />
        </k-form-item>
        <k-form-item label="状态">
          <k-field-select data-dict="surrogate_status" v-model="formData.status" :data-disabled="true" :data-allowblank="false" />
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" dataContentType="json" data-url="wf/surrogate/add.json" data-target="grid" data-from="addForm" :data-model="formData" :data-handler="submitDataHandler">
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

export default {
	name: "surrogate",
	data() {
		return {
			formData: {},
			selectRowData: {},
			queryParamProcessKey: "",
			queryParamStatus: "",
		};
	},
	computed: {
		queryParam() {
			return {
				processKey: this.queryParamProcessKey,
				status: this.queryParamStatus,
			};
		},
	},
	methods: {
		submitDataHandler(value) {
			let processRows = this.$refs.processSelect.rows;
			if (processRows && processRows.length > 0) {
				for (let index in processRows) {
					if (processRows[index].processKey == value.processKey) {
						value.processName = processRows[index].processName;
						break;
					}
				}
			}
			let surrogateRows = this.$refs.surrogateSelect.rows;
			if (surrogateRows && surrogateRows.length > 0) {
				for (let index in surrogateRows) {
					if (surrogateRows[index].userid == value.surrogate) {
						value.surrogateName = surrogateRows[index].username;
						break;
					}
				}
			}
		},
		selectRow(row, column, event) {
			const _this = this;
			_this.selectRowData = assign({}, row);
			_this.formData = assign({}, row);
		},
	},
};
</script>
