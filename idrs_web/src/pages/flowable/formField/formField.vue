<!--表单配置-->
<template>
  <div class="py-page">
    <k-form-search-customize data-target="grid" v-model="queryParam" data-label-width="80px">
      <k-form-item label="表单名称">
        <k-field-text v-model="queryParam.displayName" :data-max-length="100"></k-field-text>
      </k-form-item>
    </k-form-search-customize>
		<div class="py-page-container">
			<div class="table-top-btns">
				<div class="left">
					<k-btn slot="button" data-functype="POPUP" class="btn-custom-primary" :data-handler="addDataHandler" data-target="addPopup">
						<md-icon md-src="/static/svg/add.svg" />
						新增
					</k-btn>
				</div>
			</div>
			<k-grid ref="grid" dataContentType='json' @data-row-select="selectRow" data-url='wf/formField/list.json'>
				<k-grid-column data-align="center" data-header="表单名称" data-name="displayName"></k-grid-column>
				<k-grid-column data-align="center" data-header="表单类型" data-render="renderFormType"></k-grid-column>
				<template slot="operate" slot-scope="scope">
					<k-btn class="md-info md-just-icon md-simple" data-descript="编辑" data-functype="POPUP" data-size="mini" data-target="addPopup" :data-handler="editDataHandler">
						<md-icon>edit</md-icon>
					</k-btn>
					<k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" dataContentType='json' :data-url="'wf/formField/delete/'+ scope.row.row.formFieldId+'.json'" data-size="mini" data-type="danger" data-target="grid" :data-confirm="true" data-descript="删除">
						<md-icon>close</md-icon>
					</k-btn>
					<k-btn class="md-warning md-just-icon md-simple" @click.native.stop="previewDataHandler(scope.row.row)" data-descript="预览">
						<md-icon md-src="/static/svg/log.svg" />
					</k-btn>
				</template>
			</k-grid>
		</div>
    <k-popup ref="previewPopup" data-title="预览" dataWidth="60%">
      <!-- 在线表单 -->
      <CodePreview v-if="formType==='onlineForm'" :source="formKey" :submit-data="{}"></CodePreview>
      <!-- 表单组件 -->
      <component v-else-if="formType==='mircoFormComponent'" :is="formKey" :submit-data="{}" />
      <!-- 表单 -->
      <k-form v-else-if="formType==='form'">
        <k-form-item v-for="(item, index) in formFieldList" :key="index" :label="item.displayName">
          <component :is="item.fieldType" v-model="item.value" v-bind="item.extendAttr" :data-allowblank="false" :data-dict="item['data-dict']" :data-disabled="true"></component>
        </k-form-item>
        <k-form-footer></k-form-footer>
      </k-form>
    </k-popup>
    <k-popup ref="addPopup" :data-title="title" >
      <div class="form-body">
        <k-form ref="form1" data-total-width="988px" data-ui="element" data-label-width="150px" :data-col="3">
          <k-form-item label="表单名称">
            <k-field-text v-model="formData.displayName" :data-allowblank="false" data-regx="^[\u4e00-\u9fa5a-zA-Z0-9_]+$" data-regx-text="请输入正确的中文名" :data-max-length="100"></k-field-text>
          </k-form-item>
          <k-form-item label="表单类型">
            <k-field-select v-model="formData.formType"  @data-on-change="formTypeChange"  :data-data="formTypeOptions" :data-clearable="true" dataDisplayField="label" dataValueField="value" :data-allowblank="false"></k-field-select>
          </k-form-item>
          <template v-if="formData.formType=='mircoFormComponent'">
            <k-form-item label="表单组件名称">
              <k-field-text v-model="formData.json" :data-allowblank="false"></k-field-text>
            </k-form-item>
          </template>
        </k-form>
        <template v-if="formData.formType=='form'">
          <k-grid ref="fieldsGrid" :data-data="fieldsGridData" :dataPageSize="-1" :data-display="false" data-operate-width="50px">
            <k-grid-column data-header="name" data-name="name" data-width="150px">
              <template slot-scope="scope">
                <k-field-text v-model="scope.row.row.name" :data-allowblank="false" :data-max-length="50" :data-clearable="true"></k-field-text>
              </template>
            </k-grid-column>
            <k-grid-column data-header="显示值" data-name="displayName" data-width="150px">
              <template slot-scope="scope">
                <k-field-text v-model="scope.row.row.displayName" :data-allowblank="false" :data-max-length="100" :data-clearable="true"></k-field-text>
              </template>
            </k-grid-column>
            <k-grid-column data-header="类型" data-name="fieldType" data-width="120px">
              <template slot-scope="scope">
                <k-field-select v-model="scope.row.row.fieldType" :data-data="filedTypeOptions" :data-clearable="true" dataDisplayField="label" dataValueField="value" :data-allowblank="false"></k-field-select>
              </template>
            </k-grid-column>
            <k-grid-column data-header="数据字典" data-name="data-dict" data-width="150px">
              <template slot-scope="scope">
                <k-field-text v-model="scope.row.row['data-dict']" :data-max-length="32" :data-clearable="true"></k-field-text>
              </template>
            </k-grid-column>
            <k-grid-column data-header="扩展字段" data-name="extendAttr">
              <template slot-scope="scope">
                <el-tooltip class="item" effect="dark" content="填写字段属性的json字符串" placement="bottom">
                  <k-field-text v-model="scope.row.row.extendAttr" data-type="textarea" :data-clearable="true"></k-field-text>
                </el-tooltip>
              </template>
            </k-grid-column>
            <template slot="operate" slot-scope="scope">
              <k-btn class="md-danger md-just-icon md-simple" :data-handler="()=>fieldsGridData.rows.splice(scope.row.row.index,1)" data-size="mini" data-type="danger" :data-confirm="true" data-descript="删除">
                <md-icon>close</md-icon>
              </k-btn>
            </template>
          </k-grid>
          <div class="continue-select" @click="fieldsGridData.rows.push({})">
            <svg-icon icon-class="add"></svg-icon>添加一条数据
          </div>
        </template>
        <template v-if="formData.formType=='onlineForm'">
          <FlowOnlineForm ref="flowOnlineForm" :code="formData.json"></FlowOnlineForm>
        </template>
        <div style="margin: 0 auto;width: 255px;">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" dataContentType='json' :data-model="formData" :data-url="title=='新增'?'wf/formField/add.json':'wf/formField/update.json'" :data-handler="submitHandle" data-target="grid">
            <md-icon md-src="/static/svg/confirm.svg" />
            保存
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg" />
            取消
          </k-btn>
        </div>

      </div>

    </k-popup>
  </div>

</template>

<script>
import { assign } from "lodash";
import Tools from "@/utils/tools.js";
import FlowOnlineForm from "../components/FlowOnlineForm.vue";
import CodePreview from "@/pages/design/code-viewer/src/code-preview.vue";
import validateSchema from 'webpack/lib/validateSchema';
export default {
	name: "FormField",
	components: { FlowOnlineForm, CodePreview },
	data() {
		return {
			queryParam: {
				name: "",
				displayName: "",
			},
			formData: {},
			title: "",
			selectRowData: {},
			fields: [],
			fieldsGridData: {},
			formTypeOptions: [
				{
					label: "表单",
					value: "form",
				},
				{
					label: "表单组件",
					value: "mircoFormComponent",
				},
				{
					label: "在线表单",
					value: "onlineForm",
				},
			],
			filedTypeOptions: [
				{
					label: "text",
					value: "k-field-text",
				},
				{
					label: "select",
					value: "k-field-select",
				},
				{
					label: "time",
					value: "k-field-time",
				},
				{
					label: "date",
					value: "k-field-date",
				},
			],
			formKey: "",
			formType: "",
			formFieldList: "",
		};
	},
	computed: {},
	methods: {
		editDataHandler() {
			this.title = "编辑";
		},
		previewDataHandler(data) {
			if (data.formType == "form") {
				let formFieldList = [];
				data = JSON.parse(data.json);
				if (data.fieldsConf && data.fieldsConf.length > 0) {
					for (let index in data.fieldsConf) {
						let field = data.fieldsConf[index];
						let attr = field.extendAttr ? field.extendAttr : "{}";
						field.extendAttr = JSON.parse(attr);
						formFieldList.push(field);
					}
					this.formFieldList = formFieldList;
				}
				this.formType = "form";
			} else if (data.formType == "mircoFormComponent") {
				this.formType = "mircoFormComponent";
				this.formKey = data.json;
			} else if (data.formType == "onlineForm") {
				this.formType = "onlineForm";
				this.formKey = data.json;
			}
			this.$refs.previewPopup.popup();
		},
		formTypeChange(val){
			this.formData.json=""
		},
		addDataHandler() {
			this.formData = {};
			this.title = "新增";
			this.$set(this.fieldsGridData, "rows", []);
			this.$set(this.fieldsGridData, "total", 0);
		},
		submitHandle(value) {
			let result = true;
			result = this.$refs.form1.validate();

			// if (result === false) {
			// 	return false;
			// }
			if (value.formType == "form" && this.fieldsGridData.rows && this.fieldsGridData.rows.length > 0) {
				let a = true;
				this.fieldsGridData.rows.forEach((t) => {
					if (!t.name) {
						this.Tools.alert("请输入name！", "warning");
						a = false;
						return;
					}
					if (!t.displayName) {
						this.Tools.alert("请输入显示值！", "warning");
						a = false;
						return;
					}
					if (!t.fieldType) {
						this.Tools.alert("请选择类型！", "warning");
						a = false;
						return;
					}
				});
				if (a) {
					value.json = JSON.stringify({ fieldsConf: this.fieldsGridData.rows });
				}
				return a;
			}
			if (value.formType == "onlineForm") {
				value.json = this.$refs.flowOnlineForm.$children[0].code;
			}
		},
		selectRow(row, column, event) {
			this.selectRowData = assign({}, row);
			this.formData = assign({}, row);
			if (this.formData.json && this.formData.formType == "form") {
				let fields = JSON.parse(this.formData.json).fieldsConf;
				this.$set(this.fieldsGridData, "rows", fields);
				this.$set(this.fieldsGridData, "total", fields.length);
			} else if (this.formData.json && this.formData.formType == "mircoFormComponent") {
				this.$set(this.fieldsGridData, "rows", []);
				this.$set(this.fieldsGridData, "total", 0);
			}
		},
		renderFormType(row) {
			let arr = this.formTypeOptions.filter((t) => t.value == row.formType);
			if (arr && arr.length > 0) {
				return arr[0].label;
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
</style>
