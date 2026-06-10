<template>
  <div class="toolbar">
    <i
      class="command iconfont icon-undo"
      title="撤销"
      :class="undoList.length>0?'':'disable'"
      @click="handleUndo"
    ></i>
    <i
      class="command iconfont icon-redo"
      title="重做"
      :class="redoList.length>0?'':'disable'"
      @click="handleRedo"
    ></i>
    <span class="separator"></span>
    <!-- <i data-command="copy" class="command iconfont icon-copy-o disable" title="复制"></i>
    <i data-command="paste" class="command iconfont icon-paster-o disable" title="粘贴"></i>-->
    <i
      data-command="delete"
      class="command iconfont icon-delete-o"
      title="删除"
      :class="selectedItem && selectedItem.length > 0?'':'disable'"
      @click="handleDelete"
    ></i>
    <span class="separator"></span>
    <el-button
      @click="updateData1"
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
  import './inconf.css';
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
        return this.data.update ? "更新" : "保存";
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
      updateData1() {
        console.log('执行流程保存方法')
        this.updateLoading = true;
        let res = this.$parent.$children.filter(child => {
          return child.$options.name === "detailPanel";
        });

        let detail = res[0];
        let graphData = this.graph.save();
        graphData.nodes.forEach(node => {
          delete node["image"];
          delete node.dragItemConfig['image']
        });

        let transed = trans(graphData);
        console.log(graphData)

        let processData = {};
        processData.name = detail.canvasModel.name;
        processData.displayName = detail.canvasModel.displayName;
        processData.nodesInfo = transed.nodesInfo;
        processData.connectionsInfo = transed.connectionsInfo;
        processData.orignal = graphData;

        let reqData = {
          name: detail.canvasModel.name,
          displayName: detail.canvasModel.displayName,
          envId: detail.canvasModel.envId,
          type: detail.canvasModel.type,
          processType: 0,
          json: JSON.stringify(processData)
        };

        let action = "Process.saveProcess";
        if (this.data.update) {
          action = "Process.updateProcess.json";
          reqData.processId = this.data.processId;
          reqData.version = this.data.version;
        }

        if (!this.validate(reqData)) {
          this.updateLoading = false;
          return;
        }
        this.httpUtil.comnUpdate({
          action: action,
          params: reqData,
          callback: () => {
            this.updateLoading = false
          }
        }).then(res => {
        });
      },
      validate(reqData) {
        var spaceRe = new RegExp("^[ ]+$");  // 校验是否为空格
        var enNameRe = new RegExp("^[a-zA-Z0-9_]+$");  // 英文名称校验
        var zhNameRe = new RegExp("^[\u4e00-\u9fa5a-zA-Z0-9_]+$");  // 中文名称校验
        if (!reqData.name || spaceRe.test(reqData.name)) {
          Tools.alert("请填写流程英文名", "danger");
        } else if (!reqData.displayName || spaceRe.test(reqData.displayName)) {
          Tools.alert("请填写流程中文名", "danger");
        } else if(!enNameRe.test(reqData.name)){
          Tools.alert("请填写正确的流程英文名(字母、数字、下划线)", "danger");
        } else if(!zhNameRe.test(reqData.displayName)){
          Tools.alert("请填写正确的流程中文名(中文、字母、数字、下划线)", "danger");
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
    background-color: white;
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
