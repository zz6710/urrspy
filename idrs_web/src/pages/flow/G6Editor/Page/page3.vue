<template>
  <div class="page">
    <div :id="pageId" class="graph-container" style="position: relative;"></div>
  </div>
</template>

<script>
  import G6 from "@antv/g6";
  import { initBehavors } from "../behavior";
  import testData from "../test";
  import startSvg from "@/assets/editor/start.svg";
  import exclusiveGatewaySvg from "@/assets/editor/exclusiveGateway.svg";
  import parallelGatewaySvg from "@/assets/editor/parallelGateway.svg";
  import joinSvg from "@/assets/editor/join.svg";
  import userTaskSvg from "@/assets/editor/userTask.svg";
  import endSvg from "@/assets/editor/end.svg";
  import Tools from "@/utils/tools";
  export default {
    name: "page3",
    data() {
      return {
        pageId: "graph-container",
        graph: null
      };
    },
    props: {
      height: {
        type: Number,
        default: 0
      },
      width: {
        type: Number,
        default: 0
      },
      data: {
        type: Object,
        default: () => {}
      },
      isSkip:{
        type:Boolean,
        default:false
      },
      t8_prod_info_id:{
        type:String,
      },
      prod_code:{
        type:String,
      }
    },
    created() {
      initBehavors();
    },
    mounted() {
      this.$nextTick(() => {
        this.init();
      });
    },
    methods: {
      init() {
        const height = this.height + 900;
        const width = this.width - 250;

        this.graph = new G6.Graph({
          container: "graph-container",
          height: height,
          width: document.getElementsByClassName("tool").length>0?document.getElementsByClassName("tool")[0].offsetWidth:850,
          modes: {
            // 支持的 behavior
            default: ["drag-canvas", "hover-node", "select-node", "hover-edge", "keyboard", "customer-events", "add-menu"],
            mulitSelect: ["mulit-select"],
            addNode: ['click-add-node', 'click-select'],
            addEdge: ["add-edge"],
            // moveNode: ["drag-item"]
          }
        });
        const { editor, command } = this.$parent;
        editor.emit("afterAddPage", { graph: this.graph, command });
        // this.data.processData.nodes.forEach(item=>{
        //   item.x=item.x-(1142-document.getElementById("graph-container").offsetWidth)
        // })
        console.log(this.data.processData)
        this.readData(this.data.processData);
        // this.data.processData.nodes.forEach(item=>{
        //
        //   this.graph.update(item.id, {
        //     x: item.x-(1142-document.getElementById("graph-container").offsetWidth)
        //   })
        // })
      },

      readData(data) {
        if (data) {
          data.nodes &&
          data.nodes.forEach(node => {
            if (node.attrs) {
              if (node.attrs.type === "startevent-none") {
                node.image = startSvg;
              }

              if (node.attrs.type === "task") {
                node.image = userTaskSvg;
              }

              if (node.attrs.type === "gateway-exclusive") {
                node.image = exclusiveGatewaySvg;
              }

              if (node.attrs.type === "gateway-fork") {
                node.image = parallelGatewaySvg;
              }

              if (node.attrs.type === "gateway-join") {
                node.image = joinSvg;
              }

              if (node.attrs.type === "endevent-none") {
                node.image = endSvg;
              }
            }
          });

          this.graph.read(data);
          // 更新 taskname
          this.graph.getNodes().forEach(node => {
            let model = node.getModel();
            if (model.attrs && model.attrs.type === "task") {
              node.getContainer()
                .get("children")[2]
                .attr("text", model.attrs.displayName);
            }
          });
          this.graph.on('node:click', ev => {
            if(this.isSkip){
              if(ev.item.getModel().attrs.roleid != undefined && ev.item.getModel().attrs.roleid != ""){
                if(ev.item.getModel().state=="2"){
                  if(ev.item.getModel().attrs.funcId){
                    //检查当前用户权限是否满足
                    let roleidStr="";
                    for (var i = 0; i < ev.item.getModel().attrs.roleid.length; i++) {
                      roleidStr+=ev.item.getModel().attrs.roleid[i]+",";
                    }
                    roleidStr=roleidStr.substr(0,roleidStr.length-1);
                    this.httpUtil.ajax({
                      url: 'wf/flowTemplate/checkRoleID.json',
                      params: {
                        roleid:roleidStr
                      },
                    }).then(result => {
                      if(parseInt(result.data.resultCount) > 0){
                        this.httpUtil.ajax({
                          url: 'wf/flowTemplate/findTaskFuncInfos.json',
                          params: {
                            id:ev.item.getModel().attrs.funcId
                          },
                        }).then(res => {
                          let hideButtonIds = res.data[0].hide_button_ids;
                            this.$router.push({
                              path: "/main/" + res.data[0].url.split('?')[0],
                              query: {
                                is_process: true,
                                t8_prod_info_id: this.t8_prod_info_id,
                                prod_code: this.prod_code,
                                hideButtonids: hideButtonIds,
                                title: res.data[0].url.split('?')[1]
                              }
                            })
                        });
                      }else{
                        Tools.alert("非本节点配置的角色人员不可进行操作！","danger");
                      }
                    });
                  }
                }
              }
            }else{

            }
          });
          // this.graph.getEdges().forEach(edge => {
          // 	let model = edge.getModel();
          // 	if (model.attrs && model.attrs.type === "edge") {
          // 		edge.getContainer()
          // 			.get("children")[1]
          // 			.attr("text", model.attrs.displayName);
          // 		edge.getContainer()
          // 			.get("children")[0]
          // 			.attr("displayName", model.attrs.displayName)
          // 			.attr("envTransitionExpr", model.attrs.envTransitionExpr);
          // 	}
          // });
        }
      }
    },
    watch: {
      data: function(val) {
        if (val && val.processData) {
          if (val.isTrans) {
            this.readOAData(val.processData);
          }
          this.readData(val.processData);
        }
      }
    }
  };
</script>

<style scoped>
  .page {
    /* margin-left: 200px; */
  }
</style>
