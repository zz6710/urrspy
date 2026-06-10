<template>
  <div class="detailpannel">
    <div>
      <div
        v-if="status=='node-selected' || status=='label-selected'"
        class="pannel"
        id="node_detailpannel"
      >
        <div class="pannel-title">配置面板</div>
        <div class="block-container">
          <k-form ref="form" :data-col="1">
            <k-form-item label="名称">
              <k-field-text v-model="nodeModel.name" :data-disabled="true" data-placeholder="请输入内容"/>
            </k-form-item>
            <template v-if="formConfig[nodeModel.type]">
              <template v-for="item in formConfig[nodeModel.type]">
                <k-form-item :key="item.field" :label="item.label">
                  <k-field-select v-if="item.inputType=='select'" v-model="nodeModel[item.field]" :data-multiple="item.inputConfig.multiple"
                   :data-data="item.field=='rejectTaskNames'?rejectNodes:dictMap[item.field]" :data-display-field="item.inputConfig.displayField" :data-value-field="item.inputConfig.valueField"/>
                  <k-field-text v-else v-model="nodeModel[item.field]" :data-disabled="item.isApplicationDisabled==isApplicationTaskNode" />
                </k-form-item>
              </template>
            </template>
          </k-form>
        </div>
      </div>
      <div v-if="status==='canvas-selected'"
        class="pannel"
        id="canvas_detailpannel"
      >
        <div class="pannel-title">画布</div>
        <div class="block-container">
          <k-form ref="outerForm" :data-col="1">
            <k-form-item label="英文名">
              <k-field-text v-model="canvasModel.name" :data-disabled="data.update" :data-allowblank="false" :data-max-length="32"></k-field-text>
            </k-form-item>

            <k-form-item label="中文名">
              <k-field-text v-model="canvasModel.displayName" :data-allowblank="false" :data-max-length="32"></k-field-text>
            </k-form-item>
          </k-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import eventBus from "@/utils/eventBus";

  export default {
    name: "detailPanel",
    data() {
      return {
        canvasModel: {
          type: "",
          name: "",
          displayName: "",
          envId: ""
        },
        status: "canvas-selected",
        showGrid: false,
        page: {},
        graph: {},
        item: {},
        nodeModel: {},
        grid: null,
        value: "",
        dictMap: {
          'workflow':[],
          'actors-checkedKeys': [],
          'roles-checkedKeys': [],
          'btns-checkedKeys': [{
            id: "1",
            name: "拒绝"
          }, {
            id: "2",
            name: "驳回"
          }, {
            id: "3",
            name: "通过"
          }],
          'envTasks': [],
          'updateEnvTask': [],
          'formId': [],
          'busiId': [],
          'rejectTaskNames': [],
          'enableAttachment': [{
            id: "0",
            name: "否"
          }, {
            id: "1",
            name: "是"
          }]
        },
        actors: [],
        roles: [],
        envTasks: [],
        ctxs: [],
        types: [
          {
            "key": "1",
            "value": "入库"
          },
          {
            "key": "2",
            "value": "不入库"
          },
        ],
        updateEnvTasks: [],
        formConfig: {
          task: [{
            inputType: 'text',
            label: '显示名称',
            field: 'displayName',
            isApplicationDisabled: true
          }, {
            inputType: 'select',
            label: '审批流',
            field: 'workflow',
            inputConfig: {
              data: [],
              displayField: 'label',
              valueField: 'value',
              multiple: false,
            }
          }, {
            inputType: 'select',
            field: 'actors-checkedKeys',
            label: '参与者',
            inputConfig: {
              data: this.actors,
              displayField: 'name',
              valueField: 'id',
              multiple: true
            }
          }, {
            inputType: 'select',
            field: 'roles-checkedKeys',
            label: '参与角色',
            inputConfig: {
              data: this.roles,
              displayField: 'name',
              valueField: 'id',
              multiple: true
            }
          }],
          operation: [{
            inputType: 'text',
            field: 'displayName',
            label: '显示名称'
          }, {
            inputType: 'select',
            field: 'busiId',
            label: '功能',
            inputConfig: {
              displayField: 'busiName',
              valueField: 'busiId',
              multiple: false
            }
          }, {
            inputType: 'select',
            field: 'actors-checkedKeys',
            label: '参与者',
            inputConfig: {
              data: this.actors,
              displayField: 'name',
              valueField: 'id',
              multiple: true
            }
          }, {
            inputType: 'select',
            field: 'roles-checkedKeys',
            label: '参与角色',
            inputConfig: {
              data: this.roles,
              displayField: 'name',
              valueField: 'id',
              multiple: true
            }
          }],
          'gateway-exclusive': [{
            inputType: 'text',
            field: 'decisionHandler',
            label: '决策处理器'
          }, {
            inputType: 'text',
            field: 'postInterceptors',
            label: '节点拦截器'
          }],
          'gateway-fork': [{
            inputType: 'text',
            field: 'postInterceptors',
            label: '节点拦截器'
          }],
          'gateway-join': [{
            inputType: 'text',
            field: 'postInterceptors',
            label: '节点拦截器'
          }],
          edge: [{
            inputType: 'text',
            field: 'displayName',
            label: '显示名称'
          }, {
            inputType: 'text',
            field: 'envTransitionExpr',
            label: '表达式'
          }]
        }
      };
    },
    props: {
      data: {
        type: Object,
        default: () => {}
      }
    },
    created() {
      this.canvasModel.name = this.data.name;
      this.canvasModel.displayName = this.data.displayName;
      this.canvasModel.envId = this.data.envId;
      this.canvasModel.type = this.data.type;

      this.init();
      this.bindEvent();
      this.httpUtil.comnQuery({
        action: "SelectEntity.listActors",
        params: {}
      })
      .then(data => {
        this.actors = data.rows;
        this.dictMap['actors-checkedKeys'] = this.actors;
      });

      this.httpUtil.comnQuery({
        action: "SelectEntity.listRoles",
        params: {}
      })
      .then(data => {
        this.roles = data.rows;
        this.dictMap['roles-checkedKeys'] = this.roles;
      });

      this.httpUtil.comnQuery({
        action: "SelectEntity.listProcess",
        params: {}
      }).then(data => {
        this.ctxs = data.rows;
        this.dictMap['formId'] = this.ctxs;
      });

      this.httpUtil.comnQuery({
        action: "OpBusiInfo.findAll",
        params: {}
      }).then(data => {
        this.dictMap['busiId'] = data.rows;
      });
      this.httpUtil.comnQuery({
        action: "SelectEntity.listProcessConfig",
        params: {}
      }).then(data => {
        this.dictMap['workflow'] = data.rows;
      });
    },
    computed: {
      isApplicationTaskNode() {
        return this.nodeModel.name === this.global.wf_application_en_task_name;
      },
      rejectNodes() {
        let nodes = this.graph.getNodes();
        let edges = this.graph.getEdges();
        this.dictMap['rejectTaskNames'] = []
        this.getSourceModel(edges, this.nodeModel.name, this.dictMap['rejectTaskNames']);
        return this.dictMap['rejectTaskNames'];
      },
    },
    methods: {
      getSourceModel(edges, nodeName, result){
        if(edges){
          for(let i = 0;i < edges.length; i++){
            let edge = edges[i];
            let targetNode = edge._cfg.targetNode;
            let sourceNode = edge._cfg.sourceNode;
            if(targetNode && targetNode.getModel().attrs.name == nodeName){
              let model = sourceNode.getModel().attrs;
              if (model.type == 'task' && model.name != 'applicationTask') {
                let rstItem = result.filter(item => {return item.name == model.name});
                if(!rstItem || rstItem.length == 0) {
                  model.newValue = model.name + '&' + model.displayName;
                  result.push(model)
                }
              }
              if(sourceNode.getModel().attrs.name != nodeName){
                this.getSourceModel(edges, model.name, result);
              }
            }
          }
          return result;
        }else {
          return result;
        }
      },
      fittingString(str, maxWidth) {
        let currentWidth = str.length;
        let res = str;
        if (currentWidth > maxWidth) {
          res = `${str.substr(0, maxWidth)}\n${this.fittingString(str.substr(maxWidth),maxWidth)}`;
        }
        return res;
      },
      init() {},
      bindEvent() {
        let self = this;
        eventBus.$on("afterAddPage", page => {
          self.page = page;
          self.graph = self.page.graph;
          // modify model auto
          eventBus.$on("nodeselectchange", item => {
            if (item.target.getType() === "node") {
              if (item.select === true) {
                self.status = "node-selected";
                self.item = item.target;
                self.nodeModel = item.target.getModel().attrs;
                // 更新 task label
                this.updateTaskLabel(item);
              } else {
                self.status = "canvas-selected";
                self.item = null;
                self.nodeModel = null;
                // 更新 task label
                this.updateTaskLabel(item);
              }
            }

            if (item.target.getType() === "edge") {
              if (item.select === true) {
                self.status = "label-selected";
                // self.nodeModel = null;
                self.nodeModel = item.target.getModel().attrs;
                this.updateEdgeLabel(item);
              } else {
                self.status = "canvas-selected";
                self.nodeModel = null;
                self.item = null;
                this.updateEdgeLabel(item);
              }

            }
          });

          eventBus.$on("deleteDetailPanel", () => {
            this.nodeModel = "";
            this.item = {};
            this.value = "";
            this.status = "canvas-selected";
          });
        });
      },
      updateTaskLabel(item) {
        let model = item.target.getModel();
        if (model.attrs && (model.attrs.type === "task"||model.attrs.type === "operation")) {
          item.target.getContainer()
            .get("children")[2]
            .attr("text", this.fittingString(model.attrs.displayName,6));
        }
      },
      updateEdgeLabel(item) {
        let model = item.target.getModel();
        if (model.attrs && model.attrs.type === "edge") {
          model.label = model.attrs.displayName
          item.target.update(model);
        }
      },
    },
    watch: {
      data: function(val) {
        this.canvasModel.name = val.name;
        this.canvasModel.displayName = val.displayName;
        this.canvasModel.envId = val.envId;
        this.canvasModel.type = val.type;
      }
    }
  };
</script>

<style scoped>
  .detailpannel {
    height: 100%;
    position: absolute;
    right: 0px;
    z-index: 2;
    background: #f7f9fb;
    border-left: 1px solid #e6e9ed;
    overflow: auto;
  }
  .detailpannel .block-container {
    padding: 16px 8px;
  }
  .block-container .el-col {
    height: 28px;
    display: flex;
    align-items: center;
    margin-bottom: 10px;
  }
  .pannel-title {
    height: 32px;
    border-top: 1px solid #dce3e8;
    border-bottom: 1px solid #dce3e8;
    background: #ebeef2;
    color: #000;
    line-height: 28px;
    padding-left: 12px;
  }
</style>

