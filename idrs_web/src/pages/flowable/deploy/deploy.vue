<!--部署管理-->
<template>
  <div class="py-page">
    <k-form-search-customize data-target="grid" data-label-width="80px">
      <k-form-item label="流程名称">
        <k-field-text v-model="queryParams.processName" />
      </k-form-item>
      <k-form-item label="状态">
        <k-field-select :data-data="stateOptions" v-model="queryParams.state" data-display-field="label" data-value-field="value"></k-field-select>
      </k-form-item>
    </k-form-search-customize>
		<div class="py-page-container">
			<k-grid ref="grid" dataContentType="json" :data-display="false" data-url='wf/deploy/list.json' :data-params="queryParams">
				<k-grid-column data-align="center" data-header="流程标识" data-name="processKey"></k-grid-column>
				<k-grid-column data-align="center" data-header="流程名称">
					<template slot-scope="scope">
						<el-button type="text" @click="handleProcessView(scope.row.row)">
							<span>{{ scope.row.row.processName }}</span>
						</el-button>
					</template>
				</k-grid-column>
				<k-grid-column data-align="center" data-header="流程版本">
					<template slot-scope="scope">
						<el-tag size="medium">v{{ scope.row.row.version }}</el-tag>
					</template>
				</k-grid-column>
				<k-grid-column data-align="center" data-header="状态">
					<template slot-scope="scope">
						<el-tag type="success" v-if="!scope.row.row.suspended">激活</el-tag>
						<el-tag type="warning" v-if="scope.row.row.suspended">挂起</el-tag>
					</template>
				</k-grid-column>
				<k-grid-column data-align="center" data-header="部署时间" data-name="deploymentTime"></k-grid-column>
				<template slot="operate" slot-scope="scope">
					<el-button type="text" size="mini" icon="el-icon-price-tag" @click.native="handlePublish(scope.row.row)">版本管理</el-button>
					<el-button type="text" size="mini" icon="el-icon-delete" :disabled="scope.row.row.deleteFlag==='0'" @click="handleDelete(scope.row.row)">删除</el-button>
				</template>
			</k-grid>
		</div>

    <!-- 流程图 -->
    <el-drawer :title="processView.title" :visible.sync="processView.open" size="70%" append-to-body>
      <process-viewer :key="`designer-${processView.index}`" :xml="processView.xmlData" :style="{height: '100%'}" />
    </el-drawer>
    <!-- 版本覆盖 -->
    <k-popup  ref="overridePopup" data-title="版本覆盖" :dataAppendToBody="true">
      <k-grid ref="overrideGrid" dataContentType="json" :data-display="false" data-url='wf/deploy/publishList.json' :data-params="publishQueryParams" data-height="423px">
        <k-grid-column data-align="center" data-header="流程标识" data-name="processKey"></k-grid-column>
        <k-grid-column data-align="center" data-header="流程名称">
          <template slot-scope="scope">
            <el-button type="text" @click="handleProcessView(scope.row.row)">
              <span>{{ scope.row.row.processName }}</span>
            </el-button>
          </template>
        </k-grid-column>
        <k-grid-column data-align="center" data-header="流程版本">
          <template slot-scope="scope">
            <el-tag size="medium">v{{ scope.row.row.version }}</el-tag>
          </template>
        </k-grid-column>
        <k-grid-column data-align="center" data-header="状态">
          <template slot-scope="scope">
            <el-tag type="success" v-if="!scope.row.row.suspended">激活</el-tag>
            <el-tag type="warning" v-if="scope.row.row.suspended">挂起</el-tag>
          </template>
        </k-grid-column>
        <template slot="operate" slot-scope="scope">
          <el-button type="text" size="mini" icon="el-icon-help" @click.native="submitOverride(scope.row.row)">覆盖</el-button>
        </template>
      </k-grid>
    </k-popup>
    <!-- 版本管理 -->
    <k-popup  ref="publishPopup" data-title="版本管理">
      <k-grid ref="publishGrid" dataContentType="json" :data-display="false" data-url='wf/deploy/publishList.json' :data-params="publishQueryParams" data-operate-width="240px" data-height="423px">
        <k-grid-column data-align="center" data-header="流程标识" data-name="processKey"></k-grid-column>
        <k-grid-column data-align="center" data-header="流程名称">
          <template slot-scope="scope">
            <el-button type="text" @click="handleProcessView(scope.row.row)">
              <span>{{ scope.row.row.processName }}</span>
            </el-button>
          </template>
        </k-grid-column>
        <k-grid-column data-align="center" data-header="流程版本">
          <template slot-scope="scope">
            <el-tag size="medium">v{{ scope.row.row.version }}</el-tag>
          </template>
        </k-grid-column>
        <k-grid-column data-align="center" data-header="状态">
          <template slot-scope="scope">
            <el-tag type="success" v-if="!scope.row.row.suspended">激活</el-tag>
            <el-tag type="warning" v-if="scope.row.row.suspended">挂起</el-tag>
          </template>
        </k-grid-column>
        <template slot="operate" slot-scope="scope">
          <el-button type="text" size="mini" icon="el-icon-help" @click.native="handleOverride(scope.row.row)">版本覆盖</el-button>
          <el-button type="text" size="mini" icon="el-icon-video-pause" v-if="!scope.row.row.suspended" @click.native="handleChangeState(scope.row.row, 'suspended')">挂起</el-button>
          <el-button type="text" size="mini" icon="el-icon-video-play" v-if="scope.row.row.suspended" @click.native="handleChangeState(scope.row.row, 'active')">激活</el-button>
          <el-button type="text" size="mini" icon="el-icon-delete" :disabled="scope.row.row.deleteFlag==='0'" @click="handleDelete(scope.row.row)">删除</el-button>
        </template>
      </k-grid>
    </k-popup>
  </div>
</template>

<script>
import ProcessViewer from "../components/ProcessViewer";
import Tools from "@/utils/tools.js";

export default {
	name: "Deploy",
	components: {
		ProcessViewer,
	},
	data() {
		return {
			// 查询参数
			queryParams: {
				processKey: null,
				processName: null,
				category: null,
				state: null,
			},
			processView: {
				title: "",
				open: false,
				index: undefined,
				xmlData: "",
			},
			publishQueryParams: {
				processKey: "",
			},
			stateOptions: [
				{ label: "激活", value: "active" },
				{ label: "挂起", value: "suspended" },
			],
			selectDefinition: {},
		};
	},
	created() {},
	methods: {
		/** 查看流程图 */
		handleProcessView(row) {
			let definitionId = row.definitionId;
			this.processView.title = "流程图";
			this.processView.index = definitionId;
			// 发送请求，获取xml
			this.httpUtil
				.ajaxJson({
					url: "wf/deploy/bpmnXml/get/" + definitionId + ".json",
				})
				.then((response) => {
					this.processView.xmlData = response.data;
				});
			this.processView.open = true;
		},
		handlePublish(row) {
			this.publishQueryParams.processKey = row.processKey;
			this.$refs.publishPopup.popup();
		},
		handleOverride(row) {
			this.selectDefinition = row;
			this.publishQueryParams.processKey = row.processKey;
			this.$refs.overridePopup.popup();
		},
		submitOverride(row) {
			let toDefinitionId = row.definitionId;
			let fromDefinitionId = this.selectDefinition.definitionId;
			//version
			Tools.confirm(
				() => {
					this.httpUtil
						.ajaxJson({
							url: "wf/deploy/override/" + fromDefinitionId + "/" + toDefinitionId + ".json",
							successAlert: true,
						})
						.then(() => {});
				},
				"是否将 v" + this.selectDefinition.version + "覆盖为 v" + row.version,
				null,
				"info"
			);
		},
		/** 挂起/激活流程 */
		handleChangeState(row, state) {
			const params = {
				definitionId: row.definitionId,
				state: state,
			};
			this.httpUtil
				.ajaxJson({
					url: "wf/deploy/changeState.json",
					params: params,
					successAlert: true,
				})
				.then(() => {
					this.$refs.publishGrid.load();
					this.$refs.grid?.load();
				});
		},
		handleDelete(row) {
			Tools.confirm(
				() => {
					this.httpUtil
						.ajaxJson({
							url: "wf/deploy/delete/" + row.deploymentId + ".json",
							successAlert: true,
						})
						.then(() => {
							this.$refs.publishGrid?.load();
							this.$refs.grid?.load();
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

<style scoped>
</style>
