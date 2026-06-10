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
          <el-form
            ref="form"
            :model="nodeModel"
            label-width="100px"
            size="mini"
          >
            <el-form-item label="名称">
              <el-input
                v-model="nodeModel.name"
                size="mini"
                disabled
                placeholder="请输入内容"
              ></el-input>
            </el-form-item>
            <template v-if="nodeModel.type =='task'">
              <el-form-item label="显示名称">
                <el-input
                  v-model="nodeModel.displayName"
                  size="mini"
                  :disabled="isApplicationTaskNode"
                  placeholder="请输入内容"
                ></el-input>
              </el-form-item>
              <el-form-item label="表单" v-if="!isApplicationTaskNode">
                <el-select
                  v-model='nodeModel.formId'
                  size="mini"
                  clearable
                  placeholder="请选择内容"
                  filterable
                >
                  <el-option
                    v-for="item in ctxs"
                    :key="item.id"
                    :label="item.displayName"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="全局表单" v-if="isApplicationTaskNode" key="formId">
                <el-select
                  v-model='nodeModel.formId'
                  size="mini"
                  clearable
                  placeholder="请选择内容"
                  filterable
                >
                  <el-option
                    v-for="item in ctxs"
                    :key="item.id"
                    :label="item.displayName"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="表单信息组件" v-if="!isApplicationTaskNode" key="formUrl_1">
                <el-input
                  v-model="nodeModel.formUrl"
                  size="mini"
                  placeholder="请输入内容"
                  filterable
                ></el-input>
              </el-form-item>
              <el-form-item label="全局表单信息组件" v-if="isApplicationTaskNode" key="formUrl_2">
                <el-input
                  v-model="nodeModel.formUrl"
                  size="mini"
                  placeholder="请输入内容"
                  filterable
                ></el-input>
              </el-form-item>
              <el-form-item label="按钮" v-if="!isApplicationTaskNode">
                <el-select
                  multiple
                  v-model='nodeModel["btns-checkedKeys"]'
                  size="mini"
                  placeholder="请选择内容"
                >
                  <el-option
                    v-for="item in btns"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="驳回节点" v-if="!isApplicationTaskNode" key="rejectTaskNames">
                <el-select
                  v-model='nodeModel["rejectTaskNames"]'
                  multiple
                  size="mini"
                  placeholder="请选择内容"
                >
                  <el-option
                    v-for="item in rejectNodes"
                    v-if="item.displayName && item.name != 'applicationTask'"
                    :key="item.name"
                    :label="item.displayName"
                    :value="item.name + '&' + item.displayName"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="参与者" v-if="!isApplicationTaskNode" key="participate">
                <el-select
                  multiple
                  v-model='nodeModel["actors-checkedKeys"]'
                  size="mini"
                  placeholder="请输入内容"
                  filterable
                >
                  <el-option
                    v-for="item in actors"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="参与角色" v-if="!isApplicationTaskNode" key="roles-checkedKeys">
                <el-select
                  multiple
                  v-model='nodeModel["roles-checkedKeys"]'
                  size="mini"
                  placeholder="请输入内容"
                  filterable
                >
                  <el-option
                    v-for="item in roles"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="节点拦截器" key="postInterceptors">
                <el-input
                  v-model="nodeModel.postInterceptors"
                  size="mini"
                  placeholder="请输入内容"
                ></el-input>
              </el-form-item>
              <el-form-item label="附件上传" v-if="isApplicationTaskNode">
                <el-select
                  v-model='nodeModel.enableAttachment'
                  size="mini"
                  placeholder="请选择内容"
                >
                  <el-option
                    v-for="item in yesNo"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="是否判断产品用户组"  key="isJudgeProdUser">
                <el-select
                  v-model='nodeModel.isJudgeProdUser'
                  size="mini"
                  clearable
                  placeholder="请选择内容"
                  filterable
                >
                  <el-option
                    v-for="item in flag"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="拒绝回调" v-if="isApplicationTaskNode" key="applyRefuseHandler">
                <el-input
                  v-model="nodeModel.applyRefuseHandler"
                  size="mini"
                  placeholder="请输入url"
                ></el-input>
              </el-form-item>
              <el-form-item label="上下文" v-if="!isApplicationTaskNode" key="envTask">
                <el-select
                  v-model='nodeModel.envTask'
                  size="mini"
                  clearable
                  placeholder="请选择内容"
                >
                  <el-option
                    v-for="item in envTasks"
                    :key="item.key"
                    :label="item.key"
                    :value="item.key"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="更新上下文" v-if="!isApplicationTaskNode" key="updateEnvTask">
                <el-select
                  v-model='nodeModel.updateEnvTask'
                  size="mini"
                  clearable
                  multiple
                  placeholder="请选择内容"
                >
                  <el-option
                    v-for="item in envTasks"
                    :key="item.key"
                    :label="item.key"
                    :value="item.key"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </template>

            <template v-if="nodeModel.type =='gateway-exclusive'">
              <el-form-item label="决策处理器">
                <el-input
                  v-model="nodeModel.decisionHandler"
                  size="mini"
                  placeholder="请输入内容"
                ></el-input>
              </el-form-item>
              <el-form-item label="节点拦截器">
                <el-input
                  v-model="nodeModel.postInterceptors"
                  size="mini"
                  placeholder="请输入内容"
                ></el-input>
              </el-form-item>
            </template>

            <template v-if="nodeModel.type =='gateway-fork'">
              <el-form-item label="节点拦截器">
                <el-input
                  v-model="nodeModel.postInterceptors"
                  size="mini"
                  placeholder="请输入内容"
                ></el-input>
              </el-form-item>
            </template>

            <template v-if="nodeModel.type =='gateway-join'">
              <el-form-item label="节点拦截器">
                <el-input
                  v-model="nodeModel.postInterceptors"
                  size="mini"
                  placeholder="请输入内容"
                ></el-input>
              </el-form-item>
            </template>
            <template v-if="nodeModel.type =='edge'">
              <el-form-item label="显示名称">
                <el-input
                  v-model="nodeModel.displayName"
                  size="mini"
                  placeholder="请输入内容"
                ></el-input>
              </el-form-item>
              <el-form-item label="表达式">
                <el-input
                  v-model="nodeModel.envTransitionExpr"
                  size="mini"
                  placeholder="请输入内容"
                ></el-input>
              </el-form-item>
            </template>
          </el-form>
        </div>
      </div>
      <div
        v-if="status==='canvas-selected'"
        class="pannel"
        id="canvas_detailpannel"
      >
        <div class="pannel-title">画布</div>
        <div class="block-container">
          <el-form
            ref="form"
            :model="canvasModel"
            label-width="100px"
            size="mini"
          >
            <el-form-item label="英文名" required>
              <el-input
                v-model="canvasModel.name"
                size="mini"
                placeholder="请输入内容"
                maxlength="100"
                :disabled="data.update"
              ></el-input>
            </el-form-item>
            <el-form-item label="中文名" required>
              <el-input
                v-model="canvasModel.displayName"
                size="mini"
                maxlength="50"
                placeholder="请输入内容"
              ></el-input>
            </el-form-item>
            <el-form-item label="上下文">
              <el-select v-model="canvasModel.envId" filterable clearable placeholder="请选择">
                <el-option
                  v-for="item in envs"
                  :key="item.id"
                  :label="item.text"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="是否判断产品用户组"  key="isJudgeProdUser1">
              <el-select
                v-model='canvasModel.isJudgeProdUser'
                size="mini"
                clearable
                placeholder="请选择内容"
                filterable
              >
                <el-option
                  v-for="item in flag"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="类型">
              <el-select v-model="canvasModel.type" filterable placeholder="请选择">
                <el-option
                  v-for="item in types"
                  :key="item.key"
                  :label="item.value"
                  :value="item.key">
                </el-option>
              </el-select>
            </el-form-item>
          </el-form>
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
          envId: "",
          isJudgeProdUser: ""
        },
        status: "canvas-selected",
        showGrid: false,
        page: {},
        graph: {},
        item: {},
        nodeModel: {},
        grid: null,
        value: "",
        actors: [],
        roles: [],
        envTasks: [],
        envs: [],
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
        btns: [
          {
            id: "1",
            name: "拒绝"
          },
          {
            id: "2",
            name: "驳回"
          },
          {
            id: "3",
            name: "通过"
          }
        ],
        yesNo: [
          {
            id: "0",
            name: "否"
          },
          {
            id: "1",
            name: "是"
          }
        ],
        flag: [
          {
            id: "0",
            name: "否"
          },
          {
            id: "1",
            name: "是"
          }
        ]
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
      this.canvasModel.isJudgeProdUser = this.data.isJudgeProdUser;
      console.log(" this.canvasModel.isJudgeProdUser",  this.canvasModel.isJudgeProdUser )

      this.init();
      this.bindEvent();
      this.httpUtil
        .ajax({
          url: "wf/web3api/actors.json",
          params: {}
        })
        .then(data => {
          this.actors = data.rows;
        });

      this.httpUtil
        .ajax({
          url: "wf/web3api/roles.json",
          params: {}
        })
        .then(data => {
          this.roles = data.rows;
        });

      this.httpUtil
        .ajax({
          url: "wf/env/select/listEnv.json",
          params: {}
        })
        .then(data => {
          this.envs = data.rows;
        });

      this.httpUtil
        .ajax({
          url: "/wf/ctx/list/select2.json",
          params: {}
        })
        .then(data => {
          this.ctxs = data.rows;
        });

    },
    computed: {
      isApplicationTaskNode() {
        return this.nodeModel.name === this.global.wf_application_en_task_name;
      },
      rejectNodes() {
        let nodes = this.graph.getNodes();
        let edges = this.graph.getEdges();
        let result = []
        this.getSourceModel(edges, this.nodeModel.name, result);
        return result;
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
              if (model.type == 'task') {
                let rstItem = result.filter(item => {return item.name == model.name});
                if(!rstItem || rstItem.length == 0){
                  result.push(model)
                }else{
                  // console.log(" ### 存在重复节点 ::: ", rstItem);
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
      refreshEnvTasks(envId) {
        this.httpUtil
          .ajax({
            url: "/wf/env/listEnvItemById.json",
            params: {"id": envId}
          })
          .then(data => {
            this.envTasks = data.data;
          });
      },
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
        if (model.attrs && model.attrs.type === "task") {
          item.target
            .getContainer()
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
      handleChangeName(e) {
        const model = {
          label: e
        };

        this.graph.update(this.item, model);
      }
    },
    watch: {
      data: function(val) {
        this.canvasModel.name = val.name;
        this.canvasModel.displayName = val.displayName;
        this.canvasModel.envId = val.envId;
        this.canvasModel.type = val.type;
      },
      'canvasModel.envId': function (val, oldVal) {
        if (val != oldVal) {
          this.refreshEnvTasks(val);
        }
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
    width: 300px;
    border-left: 1px solid #e6e9ed;
    overflow: scroll;
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
