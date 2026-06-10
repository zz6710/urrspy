<template>
  <div class="detailpannel" :style="detailpannelStyle">
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

            <template v-if="nodeModel.type =='task'||nodeModel.type=='gateway-join'">
              <el-form-item label="显示名称">
                <el-input
                  v-model="nodeModel.displayName"
                  size="mini"
                  :disabled="isApplicationTaskNode"
                  placeholder="请输入内容"
                ></el-input>
              </el-form-item>
              <el-form-item label="任务功能">
                <el-select filterable :clearable="true" v-model="nodeModel.funcId" @change="changeFunc">
                  <el-option
                    v-for="item in taskFuncInfos"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="角色名称">
                <el-select :clearable="true" multiple v-model="nodeModel.roleid" @change="">
                  <el-option
                    v-for="item in roleList"
                    :key="item.roleid"
                    :label="item.rolename"
                    :value="item.roleid">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="是否跳过">
                <el-select v-model="nodeModel.ignore">
                  <el-option
                    v-for="item in yesNoList"
                    :key="item.itemkey"
                    :label="item.itemval"
                    :value="item.itemkey">
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

<!--            <template v-if="nodeModel.type =='gateway-join'">-->
<!--              <el-form-item label="节点拦截器">-->
<!--                <el-input-->
<!--                  v-model="nodeModel.postInterceptors"-->
<!--                  size="mini"-->
<!--                  placeholder="请输入内容"-->
<!--                ></el-input>-->
<!--              </el-form-item>-->
<!--            </template>-->

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
      <!--点击其他区域时，隐藏配置面板，显示画布-->
      <div
        v-if="status==='canvas-selected'"
        class="pannel"
        id="canvas_detailpannel"
      >
        <div class="pannel-title">画布</div>
      </div>
    </div>
  </div>
</template>

<script>
  import eventBus from "@/utils/eventBus";
  import ElSelectDropdown from "../../../../components/k-element/k-field-select/grid/select-dropdown";

  export default {
    name: "detailPanel2",
    components: {ElSelectDropdown},
    data() {
      return {
        taskFuncInfos:[],
        roleList:[],
        yesNoList:[],
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
        ]
      };
    },
    props: {
      data: {
        type: Object,
        default: () => {}
      },
      height:{
        type:Number,
        default:400
      },
      width:{
        type:Number,
        default:300
      }
    },
    created() {
      this.canvasModel.name = this.data.name;
      this.canvasModel.displayName = this.data.displayName;
      this.canvasModel.type = this.data.type;
      this.getTaskFuncInfos();
      this.getRoleList();
      this.getYesNoList();
      this.bindEvent();
    },
    computed: {
      detailpannelStyle(){
        let style={
          height:this.height+"px",
          width: this.width+"px"
        }
        return style
      },
      isApplicationTaskNode() {
        return this.nodeModel.name === this.global.wf_application_en_task_name;
      },
      rejectNodes() {
        let nodes = this.graph.getNodes();
        let result = []
        for (let i = 0; i < nodes.length; i++) {
          let model = nodes[i].getModel().attrs;
          if (model.name == this.nodeModel.name) {
            continue;
          }
          if (model.type == 'task') {
            result.push(model)
          }
        }
        return result;
      },
    },
    methods: {
      changeFunc(id){
          this.taskFuncInfos.every((item)=>{
             if(id==item.id){
               this.nodeModel.displayName=item.name;
               return false
             }
          })
      },
      getTaskFuncInfos(){
        this.httpUtil.ajax({
          url:'wf/flowTemplate/findTaskFuncInfos.json'
        }).then(res=>{
          this.taskFuncInfos=res.data
        })
      },
      getRoleList(){
        this.httpUtil.ajax({
          url:'wf/flowTemplate/findRoleList.json'
        }).then(res=>{
          this.roleList=res.data;
        })
      },
      getYesNoList(){
        this.httpUtil.ajax({
          url:'wf/flowTemplate/getYesNoList.json'
        }).then(res=>{
          this.yesNoList=res.data;
        })
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
        if (model.attrs && model.attrs.type === "task") {
          item.target.getContainer().get("children").forEach(item2=>{
            if(item2.type=="text"){
            item2.attr("text", this.fittingString(model.attrs.displayName,6));
            }
          })
          // item.target.getContainer().get("children")[2].attr("text", this.fittingString(model.attrs.displayName,6));
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
        this.canvasModel.type = val.type;
      },
    }
  };
</script>

<style scoped>
  .detailpannel {
    height: 100%;
    position: absolute;
    top:0px;
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
