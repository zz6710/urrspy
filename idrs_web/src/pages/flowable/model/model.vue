<!--流程模型-->
<template>
  <div class="py-page">
    <k-form-search-customize data-target="grid" data-label-width="80px">
      <k-form-item label="模型名称">
        <k-field-text v-model="queryParams.modelName" />
      </k-form-item>
    </k-form-search-customize>
		<div class="py-page-container">
			<div class="table-top-btns">
				<div class="left">
					<k-btn slot="button" @click.native.stop="handleAdd" class="btn-custom-primary">
						<md-icon md-src="/static/svg/add.svg" />
						新增
					</k-btn>
				</div>
			</div>
			<k-grid ref="grid" @data-row-select="selectRow" dataContentType="json" :data-display="false" data-url='wf/model/list.json' :data-params="queryParams" data-operate-width="230px">
				<k-grid-column data-align="center" data-header="模型标识" data-name="modelKey"></k-grid-column>
				<k-grid-column data-align="center" data-header="模型名称" data-name="modelName">
					<template slot-scope="scope">
						<el-button type="text" @click="handleProcessView(scope.row.row)">
							<span>{{ scope.row.row.modelName }}</span>
						</el-button>
					</template>
				</k-grid-column>
				<k-grid-column data-align="center" data-header="模型版本">
					<template slot-scope="scope">
						<el-tag size="medium">v{{ scope.row.row.version }}</el-tag>
					</template>
				</k-grid-column>
				<k-grid-column data-align="center" data-header="描述" data-name="description"></k-grid-column>
				<k-grid-column data-align="center" data-header="创建人" data-name="updateUserName"></k-grid-column>
				<k-grid-column data-align="center" data-header="创建时间" data-name="updateTime"></k-grid-column> <template slot="operate" slot-scope="scope">
					<el-button :loading="btnLoading" size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row.row)">编辑</el-button>
					<el-button :loading="btnLoading" type="text" size="mini" icon="el-icon-brush" @click="handleDesigner(scope.row.row)">设计</el-button>
					<el-button :loading="btnLoading" type="text" size="mini" icon="el-icon-video-play" @click.native="handleDeploy(scope.row.row)">部署</el-button>
					<el-dropdown>
						<el-button size="mini" type="text">
							<i class="el-icon-arrow-down el-icon--right"></i>更多
						</el-button>
						<el-dropdown-menu slot="dropdown">
							<el-dropdown-item icon="el-icon-view" @click.native="handleProcessView(scope.row.row)">流程图</el-dropdown-item>
							<el-dropdown-item icon="el-icon-price-tag" @click.native="handleHistory(scope.row.row)">历史</el-dropdown-item>
							<el-dropdown-item icon="el-icon-delete" @click.native="handleDelete(scope.row.row)">删除</el-dropdown-item>
						</el-dropdown-menu>
					</el-dropdown>
				</template>
			</k-grid>
		</div>

    <k-popup ref="popup" :data-title="title" :dataAppendToBody="true">
      <k-form ref="form" :data-col="1" data-ui="element">
        <k-form-item label="模型标识">
          <k-field-text v-model="form.modelKey" :data-disabled='(form.modelId !== undefined)' :dataAllowblank='false' />
        </k-form-item>
        <k-form-item label="模型名称">
          <k-field-text v-model="form.modelName" :dataAllowblank='false' />
        </k-form-item>
        <k-form-item label="描述">
          <k-field-text v-model="form.description" data-type="textarea" :data-max-length="200" />
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" :data-after-success="addModelAfterSuccess" dataContentType="json" data-functype="SUBMIT" data-target="grid" :data-url="form.modelId !== undefined ? 'wf/model/update.json' : 'wf/model/add.json'" :data-model="form">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!-- 流程图 -->
    <el-drawer :title="processView.title" :visible.sync="processView.open" size="70%" append-to-body>
      <process-viewer :key="`designer-${processView.index}`" :xml="processView.xmlData" :style="{height: '100%'}" />
    </el-drawer>

    <k-popup  ref="historyPopup" data-title="模型历史" data-width="70%">
      <k-grid ref="historyGrid" dataContentType="json" :data-display="false" data-url='wf/model/historyList.json' :data-params="queryHistoryParams">
        <k-grid-column data-align="center" data-header="模型标识" data-name="modelKey"></k-grid-column>
        <k-grid-column data-align="center" data-header="模型名称" data-name="modelName">
          <template slot-scope="scope">
            <el-button type="text" @click="handleProcessView(scope.row.row)">
              <span>{{ scope.row.row.modelName }}</span>
            </el-button>
          </template>
        </k-grid-column>
        <k-grid-column data-align="center" data-header="模型版本">
          <template slot-scope="scope">
            <el-tag size="medium">v{{ scope.row.row.version }}</el-tag>
          </template>
        </k-grid-column>
        <k-grid-column data-align="center" data-header="描述" data-name="description"></k-grid-column>
        <k-grid-column data-align="center" data-header="创建人" data-name="updateUserName"></k-grid-column>
        <k-grid-column data-align="center" data-header="创建时间" data-name="updateTime"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <el-button type="text" size="mini" icon="el-icon-video-play" @click.native="handleDeploy(scope.row)">部署</el-button>
          <el-button type="text" size="mini" icon="el-icon-star-off" @click.native="handleLatest(scope.row)">设为最新</el-button>
        </template>
      </k-grid>

    </k-popup>

    <k-popup  ref="processDesignerPopup" :data-close-on-click-modal="false" :data-title="designerData.title" data-width="1300px" :dataAppendToBody="true">
      <process-designer :key="`process-designer-${designerData.index}`" style="border:1px solid rgba(0, 0, 0, 0.1);" ref="modelDesigner" v-loading="designerData.loading" :bpmnXml="designerData.bpmnXml" :designerForm="designerData.form" @save="onSaveDesigner" />
    </k-popup>
  </div>
</template>

<script>
import ProcessDesigner from "../components/ProcessDesigner";
import ProcessViewer from "../components/ProcessViewer";
import { MessageBox } from "element-ui";
import { assign } from "lodash";
import Tools from "@/utils/tools.js";

export default {
	name: "Model",
	components: {
		ProcessDesigner,
		ProcessViewer,
	},
	data() {
		return {
			title: "",
			form: {},
			designerData: {
				title: "",
				index: "",
				loading: false,
				bpmnXml: "",
				modelId: null,
				form: {
					processName: null,
					processKey: null,
				},
			},
			processView: {
				title: "",
				index: undefined,
				xmlData: "",
				open: false,
			},
			// 查询参数
			queryParams: {
				modelKey: null,
				modelName: null,
				category: null,
			},
			queryHistoryParams: {
				modelKey: null,
			},
			selectRowData: {},
			btnLoading: false,
		};
	},
	created() {},
	methods: {
		addModelAfterSuccess(res) {
			if (res.data) {
				//新增后弹出设计弹窗
				this.handleDesigner(res.data);
			}
		},
		selectRow(row, column, event) {
			const _this = this;
			_this.selectRowData = assign({}, row);
			_this.form = assign({}, row);
		},
		/** 部署流程 */
		handleDeploy(row) {
			this.btnLoading = true;
			this.httpUtil
				.ajaxJson({
					url: "wf/model/deploy/" + row.modelId + ".json",
					successAlert: true,
					errCallback: (reData) => {
						Tools.alert(reData.returnmsg, "danger");
						this.btnLoading = false;
					},
				})
				.then((response) => {
					this.btnLoading = false;
				});
		},
		/** 查看流程图 */
		handleProcessView(row) {
			let modelId = row.modelId;
			this.processView.title = "流程图";
			this.processView.index = modelId;
			// 发送请求，获取xml
			this.httpUtil
				.ajaxJson({
					url: "wf/model/bpmnXml/get/" + modelId + ".json",
				})
				.then((response) => {
					this.processView.xmlData = response.data;
				});
			this.processView.open = true;
		},
		handleHistory(row) {
			this.queryHistoryParams.modelKey = row.modelKey;
			this.$refs.historyPopup.popup();
		},
		/** 设为最新版 */
		handleLatest(row) {
			Tools.confirm(
				() => {
					this.httpUtil
						.ajaxJson({
							url: "wf/model/latest/" + row.row.modelId + ".json",
						})
						.then((response) => {
							this.$refs.historyPopup.close();
							this.$refs.grid.load();
							Tools.alert("操作成功");
						});
				},
				"确定设为最新版吗？",
				null,
				"warning"
			);
		},
		handleAdd() {
			this.title = "新增";
			const dateTime = new Date().getTime();
			this.form = {
				modelKey: `Process_${dateTime}`,
				modelName: `业务流程_${dateTime}`,
			};
			this.$refs.popup.popup();
		},
		/** 编辑按钮操作 */
		handleUpdate(row) {
			this.title = "编辑";
			this.form = {
				modelId: row.modelId,
				modelKey: row.modelKey,
				modelName: row.modelName,
				category: row.category,
				description: row.description,
			};
			this.$refs.popup.popup();
		},
		/** 设计按钮操作 */
		handleDesigner(row) {
			this.btnLoading = true;
			this.designerData.title = "流程设计 - " + row.modelName;
			this.designerData.modelId = row.modelId;
			this.designerData.index = row.modelId;
			this.designerData.form = {
				processName: row.modelName,
				processKey: row.modelKey,
			};
			if (row.modelId) {
				this.designerData.loading = true;
				this.httpUtil
					.ajaxJson({
						url: "wf/model/bpmnXml/get/" + row.modelId + ".json",
					})
					.then((response) => {
						this.designerData.bpmnXml = response.data || "";
						this.$refs.processDesignerPopup.popup();
						this.designerData.loading = false;
						this.btnLoading = false;
					});
			}
		},
		onSaveDesigner(bpmnXml) {
			this.bpmnXml = bpmnXml;
			let dataBody = {
				modelId: this.designerData.modelId,
				bpmnXml: this.bpmnXml,
			};
			MessageBox.confirm("是否将此模型保存为新版本？", "操作提示", {
				confirmButtonText: "确定",
				cancelButtonText: "取消",
				cancelButtonClass: "el-button--info",
				type: "warning",
				beforeClose: (action, instance, done) => {
					instance.confirmButtonLoading = true;
					instance.confirmButtonText = "执行中...";
					if (action === "confirm") {
						this.confirmSave(dataBody, true);
					} else {
						this.confirmSave(dataBody, false);
					}
					instance.confirmButtonLoading = false;
					done();
				},
			}).catch(() => {});
		},
		confirmSave(body, newVersion) {
			this.designerData.loading = true;
			Object.assign(body, {
				newVersion: newVersion,
			});
			this.httpUtil
				.ajaxJson({
					url: "wf/model/save.json",
					params: body,
					successAlert: true,
				})
				.then((response) => {
					if (response.success) {
						this.$refs.grid.load();
						this.$refs.processDesignerPopup.close();
					}
				});
			this.designerData.loading = false;
		},
		/** 删除按钮操作 */
		handleDelete(row) {
			Tools.confirm(
				() => {
					this.httpUtil
						.ajaxJson({
							url: "wf/model/delete/" + row.modelId + ".json",
						})
						.then((response) => {
							this.$refs.grid.load();
							Tools.alert("操作成功");
						});
				},
				"确定删除吗？",
				null,
				"warning"
			);
		},
	},
};
</script>
