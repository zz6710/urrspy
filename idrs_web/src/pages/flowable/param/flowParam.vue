<!--流程参数-->
<template>
  <div>
    <k-form-search-customize data-target="grid" data-label-width="80px">
      <k-form-item label="参数名">
        <k-field-text v-model="queryParams.paramName" />
      </k-form-item>
      <k-form-item label="参数类型">
        <k-field-select v-model="queryParams.paramType" :data-data="wfParamStatusSelect" data-display-field="label" data-value-field="value" />
      </k-form-item>
      <k-btn slot="button" data-functype="POPUP" :data-handler="addDataHandler" class="btn-custom-primary" data-target="popup">
        <md-icon md-src="/static/svg/add.svg" />
        新增
      </k-btn>
    </k-form-search-customize>

    <k-grid ref="grid" @data-row-select="selectRow" dataContentType="json" :data-display="false" data-url='wf/param/list.json' :data-params="queryParams">
      <k-grid-column data-align="center" data-header="参数名" data-name="paramName"></k-grid-column>
      <k-grid-column data-align="center" data-header="参数类型" data-render="renderParamType"></k-grid-column>
      <k-grid-column data-align="center" data-header="备注" data-name="remark"></k-grid-column>
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" data-descript="修改" :data-handler="editDataHandler" data-functype="POPUP" data-size="mini" data-target="popup">
          <md-icon>edit</md-icon>
        </k-btn>
        <k-btn class="md-danger md-just-icon md-simple" data-size="mini" dataContentType='json' data-functype="SUBMIT" data-target="grid" data-type="danger" :data-url="'wf/param/delete/' + scope.row.row.paramId+'.json'" :data-confirm="true" data-descript="删除">
          <md-icon>close</md-icon>
        </k-btn>
      </template>
    </k-grid>

    <k-popup  ref="popup" :data-title="title" data-width="70%">

      <div class="form-body">
        <el-card class="box-card">
          <div slot="header">
            <md-icon md-src="/static/svg/basic.svg" style="display:inline-block" />
            <span>默认参数</span>
          </div>
          <k-descriptions :data-border="false" data-col="3" data-label-width="100px" data-value-width="300px">
            <k-descriptions-item data-label="流程发起人id" data-value="initiator" />
            <k-descriptions-item data-label="流程key" data-value="processKey" />
            <k-descriptions-item data-label="流程定义id" data-value="processDefinitionId" />
            <k-descriptions-item data-label="流程实例id" data-value="processInstanceId" />
            <k-descriptions-item data-label="流程状态" data-value="processStatus" />
            <k-descriptions-item data-label="表单字段" data-value="流程发起人提交的表单字段" />
            <k-descriptions-item data-label="扩展属性" data-value="流程节点的扩展属性" />
            <k-descriptions-item data-label="注入字段" data-value="监听器中的注入字段" />
          </k-descriptions>
        </el-card>

        <el-card class="box-card">
          <div slot="header">
            <md-icon md-src="/static/svg/basic.svg" style="display:inline-block" />
            <span>流程参数</span>
          </div>
          <k-form ref="form" :data-col="2" data-ui="element" data-label-width="150px">
            <k-form-item label="参数名">
              <k-field-text v-model="form.paramName" :dataAllowblank='false' :data-max-length="100" :data-disabled="title=='修改'?true:false" />
            </k-form-item>
            <k-form-item label="参数类型">
              <k-field-select v-model="form.paramType" :dataAllowblank='false' :data-data="wfParamStatusSelect" data-display-field="label" data-value-field="value" />
            </k-form-item>
            <k-form-item label="参数值" :data-col="2" v-if="form.paramType!==wfParamStatus.url.value">
              <k-field-text v-model="form.paramValue" :dataAllowblank='false' :data-max-length="500" data-type="textarea"/>
            </k-form-item>
            <k-form-item label="备注" :data-col="2">
              <k-field-text v-model="form.remark" data-type="textarea" :data-max-length="500" />
            </k-form-item>
          </k-form>
          <template v-if="form.paramType==wfParamStatus.url.value">
            <k-grid ref="fieldsGrid" :data-data="fieldsGridData" :dataPageSize="-1" :data-display="false" data-operate-width="50px">
              <k-grid-column data-header="请求类型" data-name="requestType" data-width="150px">
                <template slot-scope="scope">
                  <k-field-select v-model="scope.row.row.requestType" :data-data="requestTypeOptions" :data-allowblank="false" data-display-field="label" data-value-field="value"></k-field-select>
                </template>
              </k-grid-column>
              <k-grid-column data-header="返回报文别名" data-name="urlKey" data-width="150px">
                <template slot-scope="scope">
                  <k-field-text v-model="scope.row.row.urlKey" :data-allowblank="false" :data-max-length="50"></k-field-text>
                </template>
              </k-grid-column>
              <k-grid-column data-header="url" data-name="urlValue">
                <template slot-scope="scope">
                  <k-field-text v-model="scope.row.row.urlValue" data-type="textarea" :data-allowblank="false" :data-max-length="200"></k-field-text>
                </template>
              </k-grid-column>
              <template slot="operate" slot-scope="scope">
                <k-btn class="md-danger md-just-icon md-simple" :data-handler="()=>fieldsGridData.rows.splice(scope.row.row.row_index-1,1)" data-size="mini" data-type="danger" :data-confirm="true" data-descript="删除">
                  <md-icon>close</md-icon>
                </k-btn>
              </template>
            </k-grid>
            <div class="continue-select" @click="fieldsGridData.rows.push({})">
              <svg-icon icon-class="add"></svg-icon>添加一条数据
            </div>
          </template>
          <div class="k-form-footer-content k-form-footer-align-center">
            <k-btn class="btn-custom-primary" :data-handler="submitHandle" dataContentType="json" data-functype="SUBMIT" data-target="grid" :data-url="form.paramId != null ? 'wf/param/update.json' : 'wf/param/add.json'" :data-model="form">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
            </k-btn>
          </div>
        </el-card>
      </div>
    </k-popup>
  </div>
</template>

<script>
import Tools from "@/utils/tools.js";
import wfStatus from "../enum/enum.js";
import { assign } from "lodash";
import KDescriptions from "../components/k-descriptions/k-descriptions.vue";
import KDescriptionsItem from "../components/k-descriptions/k-descriptions-item.vue";
export default {
	name: "param",
	components: { KDescriptions, KDescriptionsItem },
	data() {
		return {
			// 弹出层标题
			title: "",
			// 查询参数
			queryParams: {
				categoryName: undefined,
				code: undefined,
			},
			// 表单参数
			form: {},
			wfParamStatusSelect: Object.values(wfStatus.param),
			wfParamStatus: wfStatus.param,
			fieldsGridData: {},
			requestTypeOptions: [
				{ label: "POST", value: "POST" },
				{ label: "GET", value: "GET" },
			],
		};
	},
	created() {},
	methods: {
		selectRow(row, column, event) {
			this.form = assign({}, row);
			this.fieldsGridData = {};
			if (this.form.paramType == this.wfParamStatus.url.value) {
				let fields = JSON.parse(this.form.paramValue);
				this.$set(this.fieldsGridData, "rows", fields);
				this.$set(this.fieldsGridData, "total", fields.length);
			} else {
				this.$set(this.fieldsGridData, "rows", []);
				this.$set(this.fieldsGridData, "total", 0);
			}
		},
		editDataHandler() {
			this.title = "修改";
		},
		addDataHandler() {
			this.form = {};
			this.$set(this.fieldsGridData, "rows", []);
			this.$set(this.fieldsGridData, "total", 0);
			this.title = "新增";
		},
		renderParamType(row) {
			if (row.paramType) {
				let arr = this.wfParamStatusSelect.filter((t) => t.value == row.paramType);
				if (arr && arr.length > 0) {
					return arr[0].label;
				}
			} else {
				return "-";
			}
		},
		submitHandle(value) {
			console.log("this.form", this.form);
			let result = true;
			result = this.$refs.form.validate();
			if (result === false) {
				return false;
			}
			if (value.paramType == this.wfParamStatus.url.value) {
				let a = true;

				if (this.fieldsGridData.rows && this.fieldsGridData.rows.length > 0) {
					this.fieldsGridData.rows.forEach((t) => {
						if (!t.requestType) {
							Tools.alert("请选择请求类型！", "warning");
							a = false;
							return;
						}
						if (!t.urlKey) {
							Tools.alert("请输入返回报文别名！", "warning");
							a = false;
							return;
						}
						if (!t.urlValue) {
							Tools.alert("请输入url！", "warning");
							a = false;
							return;
						}
					});
				} else {
					Tools.alert("请输入url！", "warning");
					a = false;
				}
				if (a) {
					value.paramValue = JSON.stringify(this.fieldsGridData.rows);
				}
				return a;
			}
		},
	},
};
</script>
<style scoped lang="scss">
.continue-select {
	width: 100%;
	margin: 0 0 30px 0;
	display: flex;
	align-items: center;
	justify-content: center;
	padding: 6px 0;
	border: 1px dashed;
	border-radius: 4px;
	color: #00a6ec;
	cursor: pointer;

	.svg-icon {
		width: 14px;
		height: 14px;
		margin-right: 10px;
	}
}
.form-body {
	padding: 0 20px 20px 20px;
}
.k-form-footer-content {
	width: 100%;
	display: flex;
	margin-top: 25px;
	margin-left: 0px;
	gap: 10px;
}
.k-form-footer-align-center {
	justify-content: center;
}
</style>
