<template>
  <div class="py-page">
    <k-form-search-customize data-target="grid" v-model="queryParam" data-label-width="70px">
      <k-form-item label="英文名">
        <k-field-text v-model="queryParam.name" :data-max-length="50"></k-field-text>
      </k-form-item>
      <k-form-item label="中文名">
        <k-field-text v-model="queryParam.displayName" :data-max-length="100"></k-field-text>
      </k-form-item>
    </k-form-search-customize>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn slot="button" data-functype="POPUP" class="btn-custom-primary" :data-handler="()=>this.formData={}" data-target="addPopup">
            <md-icon md-src="/static/svg/add.svg" />
            新增
          </k-btn>
        </div>
      </div>
      <k-grid ref="grid" dataContentType='json' @data-row-select="selectRow" data-url='wf/env/list.json'>
        <k-grid-column data-align="center" data-header="英文名" data-name="name"></k-grid-column>
        <k-grid-column data-align="center" data-header="中文名" data-name="displayName"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改流程参数" data-functype="POPUP" data-size="mini" data-target="editPopup">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-info md-just-icon md-simple" @click.native.stop="editItemBtn(scope.row.row)" data-descript="编辑流程参数项" data-size="mini">
            <md-icon>format_align_left</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" dataContentType='json' :data-url="'wf/env/delete/'+ scope.row.row.envId+'.json'" data-size="mini" data-type="danger" data-target="grid" :data-confirm="true" data-descript="删除">
            <md-icon>close</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </div>

    <k-popup ref="editItemPopup" class="edit_item" data-title="修改" data-width="1100px" >
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
          </k-descriptions>
        </el-card>
        <p></p>
        <el-card class="box-card">
          <div slot="header">
            <md-icon md-src="/static/svg/basic.svg" style="display:inline-block" />
            <span>流程参数</span>
          </div>
          <k-grid ref="envItemGrid" :data-data="envItemGridData" :dataPageSize="-1" :data-display="false" data-operate-width="100px">
            <k-grid-column data-header="参数名" data-width="150px">
              <template slot-scope="scope">
                <k-field-text v-model="scope.row.row.itemKey" :data-allowblank="false" :data-max-length="50"></k-field-text>
              </template>
            </k-grid-column>
            <k-grid-column data-header="参数类型" data-width="150px">
              <template slot-scope="scope">
                <k-field-select v-model="scope.row.row.itemType" :dataAllowblank='false' :data-data="wfParamStatusSelect" data-display-field="label" data-value-field="value" />
              </template>
            </k-grid-column>
            <k-grid-column data-header="参数值">
              <template slot-scope="scope">
                <k-field-text :data-disabled="scope.row.row.itemType == wfParamStatus.url.value" v-model="scope.row.row.itemValue" data-type="textarea" :data-allowblank="false"></k-field-text>
              </template>
            </k-grid-column>
            <template slot="expand" slot-scope="props" v-if="props.row.itemType == wfParamStatus.url.value">
              <el-form ref="form" :model="item" label-position="right" label-width="auto" v-for="(item,index) in props.row.urls" :key="index">
                <el-row :gutter="20">
                  <el-col :span="6">
                    <el-form-item label="请求类型">
                      <k-field-select v-model="item.requestType" :data-data="requestTypeOptions" :data-allowblank="false" data-display-field="label" data-value-field="value"></k-field-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="6">
                    <el-form-item label="返回报文别名">
                      <k-field-text v-model="item.urlKey" :data-allowblank="false" :data-max-length="50" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="10">
                    <el-form-item label="url" label-width="30px">
                      <k-field-text v-model="item.urlValue" data-type="textarea" :data-allowblank="false" :data-max-length="200" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="2" style="padding-top: 10px">
                    <k-btn class="md-info md-just-icon md-simple" data-descript="添加" :data-handler="() => addUrlBtn(props.row.urls,index)" style="top: -9px;">
                      <md-icon>add</md-icon>
                    </k-btn>
                    <k-btn class="md-danger md-just-icon md-simple" data-descript="删除" :data-handler="() => deleteUrlBtn(props.row.urls,index)" style="top: -9px;">
                      <md-icon md-src="/static/svg/delete.svg" />
                    </k-btn>
                  </el-col>
                </el-row>
              </el-form>
            </template>
            <template slot="operate" slot-scope="scope">
              <k-btn class="md-danger md-just-icon md-simple" :data-handler="()=>envItemGridData.rows.splice(scope.row.row.row_index-1,1)" data-size="mini" data-type="danger" :data-confirm="true" data-descript="删除">
                <md-icon>close</md-icon>
              </k-btn>
            </template>
          </k-grid>
          <div class="continue-select" @click="addItemBtn">
            <svg-icon icon-class="add"></svg-icon>添加一条数据
          </div>
        </el-card>
      </div>

      <div style="margin: 0 auto;width: 255px;">
        <k-btn class="btn-custom-primary" dataContentType='json' :data-handler="submitEditItemHandler" data-size="small">
          <md-icon md-src="/static/svg/confirm.svg" />
          确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE" data-size="small">
          <md-icon md-src="/static/svg/cancel.svg" />
          取消
        </k-btn>
      </div>
    </k-popup>
    <k-popup ref="editPopup" data-title="修改流程参数" >
      <k-form ref="editForm" :data-col="1" data-ui="element">
        <k-form-item label="中文名">
          <k-field-text v-model="formData.displayName" :data-allowblank="false" data-regx="^[\u4e00-\u9fa5a-zA-Z0-9_]+$" data-regx-text="请输入正确的中文名" :data-max-length="50" />
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" dataContentType='json' data-url="wf/env/update.json" data-target="grid" data-from="editForm" :data-model="formData" data-size="small">
            <md-icon md-src="/static/svg/confirm.svg" />
            确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE" data-size="small">
            <md-icon md-src="/static/svg/cancel.svg" />
            取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
    <k-popup ref="addPopup" data-title="新增">
      <k-form ref="addForm" :data-col="1" data-ui="element">
        <k-form-item label="英文名">
          <k-field-text v-model="formData.name" :data-allowblank="false" data-regx="^[a-zA-Z0-9_]+$" data-regx-text="请输入正确的英文名" :data-max-length="100" />
        </k-form-item>
        <k-form-item label="中文名">
          <k-field-text v-model="formData.displayName" :data-allowblank="false" data-regx="^[\u4e00-\u9fa5a-zA-Z0-9_]+$" data-regx-text="请输入正确的中文名" :data-max-length="50" />
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-url="wf/env/add.json" data-target="grid" dataContentType='json' data-from="addForm" :data-model="formData" data-size="small">
            <md-icon md-src="/static/svg/confirm.svg" />
            确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE" data-size="small">
            <md-icon md-src="/static/svg/cancel.svg" />
            取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

  </div>

</template>

<script>
import { assign, cloneDeep } from "lodash";
import wfStatus from "../enum/enum.js";
import Tools from "@/utils/tools.js";
import KDescriptions from "../components/k-descriptions/k-descriptions.vue";
import KDescriptionsItem from "../components/k-descriptions/k-descriptions-item.vue";
export default {
	name: "flowContext",
	components: { KDescriptions, KDescriptionsItem },
	data() {
		return {
			formData: {},
			envItems: [],
			selectRowData: {},
			queryParam: {},

			requestTypeOptions: [
				{ label: "POST", value: "POST" },
				{ label: "GET", value: "GET" },
			],
			wfParamStatus: wfStatus.param,
			envItemGridData: {},
			wfParamStatusSelect: Object.values(wfStatus.param),
			urlData: {
				requestType: "",
				urlKey: "",
				urlValue: "",
			},
		};
	},
	methods: {
		addUrlBtn(row, index) {
			let newUrlData = Object.assign({}, this.urlData);
			row.splice(index + 1, 0, newUrlData);
		},
		deleteUrlBtn(row, index) {
			//最后一个不删除，把属性置为空
			if (row.length === 1) {
				row[index].requestType = "";
				row[index].urlKey = "";
				row[index].urlValue = "";
			} else {
				row.splice(index, 1);
			}
		},
		submitEditItemHandler() {
			let flag = true;
			let sumbitData = cloneDeep(this.envItemGridData.rows);
			if (sumbitData && sumbitData.length > 0) {
				sumbitData.forEach((item) => {
					if (!item.itemKey || !item.itemType) {
						flag = false;
						return;
					}

					if (item.itemType == this.wfParamStatus.url.value) {
						item.urls.forEach((url) => {
							if (!url.requestType || !url.urlKey || !url.urlValue) {
								flag = false;
								return;
							}
						});
						item.itemValue = JSON.stringify(item.urls);
					} else {
						if (!item.itemValue) {
							flag = false;
							return;
						}
					}
				});
			}
			if (!flag) {
				Tools.alert("数据不能为空", "danger");
				return;
			}
			this.httpUtil
				.ajaxJson({
					url: "wf/envItem/saveAll.json",
					params: sumbitData,
				})
				.then((data) => {
					if (data.success) {
						this.$refs.editItemPopup.close();
						this.$refs.grid.load();
						Tools.alert("操作成功");
					}
				});

			return true;
		},
		editItemBtn(row) {
			this.selectRowData = assign({}, row);
			this.httpUtil
				.ajaxJson({
					url: "wf/envItem/list.json",
					params: { envId: row.envId },
				})
				.then((data) => {
					data.rows.forEach((item) => {
						if (item.itemType == this.wfParamStatus.url.value) {
							let urls = JSON.parse(item.itemValue);
							item.urls = urls;
							item.itemValue = "";
						} else {
							let urls = [];
							urls.push(assign({}, this.urlData));
							item.urls = urls;
						}
					});

					this.$set(this.envItemGridData, "rows", data.rows);
					this.$set(this.envItemGridData, "total", data.rows.length);
					console.log("this.envItemGridData", this.envItemGridData);
					this.$refs.editItemPopup.popup();
				});
		},
		addItemBtn() {
			let urls = [];
			urls.push(assign({}, this.urlData));
			this.envItemGridData.rows.push({ envId: this.selectRowData.envId, urls: urls });
		},
		selectRow(row, column, event) {
			const _this = this;
			_this.selectRowData = assign({}, row);
			_this.formData = assign({}, row);
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
