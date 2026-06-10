<template>
  <div class="toolbar">
    <link rel="stylesheet" type="text/css"  />
    <i
      class="command iconfont icon-undo"
      title="撤销"
      :class="undoList.length>0?'':'disable'"
      @click="handleUndo"
    >
      <md-icon>undo</md-icon>
    </i>
    <i
      class="command iconfont icon-redo"
      title="重做"
      :class="redoList.length>0?'':'disable'"
      @click="handleRedo"
    >
      <md-icon>redo</md-icon>
    </i>
    <span class="separator"></span>
    <!-- <i data-command="copy" class="command iconfont icon-copy-o disable" title="复制"></i>
    <i data-command="paste" class="command iconfont icon-paster-o disable" title="粘贴"></i>-->
    <i
      data-command="delete"
      class="command iconfont icon-delete-o"
      title="删除"
      :class="selectedItem && selectedItem.length > 0?'':'disable'"
      @click="handleDelete"
    >
      <md-icon>delete_sweep</md-icon>
    </i>
    <span class="separator"></span>
    <el-button
      @click="updateData"
      type="primary"
      size="mini"
      :loading="updateLoading"
    >{{btnText}}</el-button>
  </div>
</template>

<script>
import eventBus from "@/utils/eventBus";
import trans from "./trans";
import Tools from "@/utils/tools";
export default {
	data() {
		return {
			page: {},
			graph: {},
			redoList: [],
			undoList: [],
			editor: null,
			command: null,
			selectedItem: null,
			multiSelect: false,
			addGroup: false,
      updateLoading: false
		};
	},
	props: {
		data: {
			type: Object,
			default: () => {}
		}
	},
	created() {
		this.init();
		this.bindEvent();
	},
	watch: {
		selectedItem(val) {
			if (val && val.length > 1) {
				this.addGroup = true;
			} else {
				this.addGroup = false;
			}
		}
	},
	computed: {
		btnText() {
		  return "保存"
			// return this.data.update ? "更新" : "保存";
		}
	},
	methods: {
		init() {
			const { editor, command } = this.$parent;
			this.editor = editor;
			this.command = command;
		},
		bindEvent() {
			let self = this;
			eventBus.$on("afterAddPage", page => {
				self.page = page;
				self.graph = self.page.graph;
			});
			eventBus.$on("add", data => {
				self.redoList = data.redoList;
				self.undoList = data.undoList;
			});
			eventBus.$on("update", data => {
				self.redoList = data.redoList;
				self.undoList = data.undoList;
			});
			eventBus.$on("delete", data => {
				self.redoList = data.redoList;
				self.undoList = data.undoList;
			});
			eventBus.$on("updateItem", item => {
				self.command.executeCommand("update", [item]);
			});
			eventBus.$on("addItem", item => {
				self.command.executeCommand("add", [item]);
			});
			eventBus.$on("nodeselectchange", () => {
				self.selectedItem = self.graph.findAllByState("node", "selected");
				self.selectedItem = self.selectedItem.concat(...this.graph.findAllByState("edge", "selected"));
			});
			eventBus.$on("deleteItem", () => {
				self.handleDelete();
			});
			eventBus.$on("muliteSelectEnd", () => {
				self.multiSelect = false;
				self.selectedItem = this.graph.findAllByState("node", "selected");
			});
		},
		handleUndo() {
			if (this.undoList.length > 0) this.command.undo();
		},
		handleRedo() {
			if (this.redoList.length > 0) this.command.redo();
		},
		handleDelete() {
			if (this.selectedItem && this.selectedItem.length > 0) {
				this.command.executeCommand("delete", this.selectedItem);
				eventBus.$emit("deleteDetailPanel");
				this.selectedItem = null;
			}
		},
		updateData() {
		  this.updateLoading = true;
			let res = this.$parent.$children.filter(child => {
				return child.$options.name === "detailPanel";
			});

			let detail = res[0];
			// this.graph.findAll('edge', edge => {
			// 	let model = edge.getModel()
			// 	model.label = edge.getContainer().get("children")[1]._attrs.text
			// 	// model.attrs.displayName = edge.getContainer().get("children")[0]._attrs.displayName
			// 	// model.attrs.envTransitionExpr = edge.getContainer().get("children")[0]._attrs.envTransitionExpr
			// 	edge.update(model)
			// });
			let graphData = this.graph.save();
			graphData.nodes.forEach(node => {
				delete node["image"];
				delete node.dragItemConfig['image']
			});

			let transed = trans(graphData);

			let reqData = {};
			reqData.name = detail.canvasModel.name;
			reqData.displayName = detail.canvasModel.displayName;
			reqData.envId = detail.canvasModel.envId;
			reqData.type = detail.canvasModel.type;
			reqData.processType = 0;
			let processData = {};
			processData.name = detail.canvasModel.name;
			processData.displayName = detail.canvasModel.displayName;
			processData.nodesInfo = transed.nodesInfo;
			processData.connectionsInfo = transed.connectionsInfo;
			processData.orignal = graphData;
			reqData.processData = JSON.stringify(processData);
      //modify by zhangchangsi 流程图字段调整,每个节点添加是否产品用户组判断
			reqData.isJudgeProdUser = detail.canvasModel.isJudgeProdUser;
			console.log('是否判断产品用户组', detail.canvasModel.isJudgeProdUser)
			let action = "/wf/process/saveProcess.json";
			if (this.data.update) {
				action = "/wf/process/updateProcess.json";
				reqData.processId = this.data.processId;
			}

			if (!this.validate(reqData)) {
        this.updateLoading = false;
			  return;
      }
			this.httpUtil.ajax({ url: action, params: reqData , callback: this.closeLoading}).then(res => {
				this.data.processPopup.close();
				this.data.$grid.load();
			});
		},
    closeLoading() {
      this.updateLoading = false;
    },
    validate(reqData) {
		  console.log(reqData)
      if (!reqData.name) {
        Tools.alert("请填写流程英文名", "danger");
      } else if (!reqData.displayName) {
        Tools.alert("请填写流程中文名", "danger");
      } else {
        return true;
      }

      return false;
    }
	}

};
</script>

<style scoped>
.toolbar {
	text-align: center;
	box-sizing: border-box;
	padding: 8px 0px;
	width: 100%;
	border: 1px solid #e9e9e9;
	height: 42px;
	z-index: 3;
	box-shadow: 0px 8px 12px 0px rgba(0, 52, 107, 0.04);
	position: absolute;
}
.toolbar .command:nth-of-type(1) {
	margin-left: 24px;
}
.toolbar .command {
	box-sizing: border-box;
	width: 27px;
	height: 27px;
	margin: 0px 6px;
	border-radius: 2px;
	padding-left: 4px;
	display: inline-block;
	border: 1px solid rgba(2, 2, 2, 0);
}
.toolbar .command:hover {
	cursor: pointer;
	border: 1px solid #e9e9e9;
}
.toolbar .disable {
	color: rgba(0, 0, 0, 0.25);
}
.toolbar .separator {
	margin: 4px;
	border-left: 1px solid #e9e9e9;
}
</style>
