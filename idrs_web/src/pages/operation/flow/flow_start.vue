<template>
  <div>
    <k-form-search data-target="processNodeGrid" data-model-name="Process"></k-form-search>

    <k-grid ref="processNodeGrid" data-action="Process.listEffectiveProcess" :data-display="false" :data-after-load="afterLoad">
      <k-grid-column data-header="流程中文名" data-name="displayName"/>
      <k-grid-column data-header="流程英文名" data-name="name" />
      <template slot="operate" slot-scope="scope">
        <k-btn class="btn-custom-primary" data-size="mini" data-descript="流程发起"
            data-functype="PAGE" data-target="/main/operation/flow/flow_detail" :data-handler="handleFirstNode">
            流程发起
        </k-btn>
        <k-btn class="btn-custom-plain" data-size="mini" data-descript="查看" :data-handler="selectRow" >
          查看
        </k-btn>
      </template>
    </k-grid>

    <div ref="parentContent" class="page" style="background-color: white;">
      <div id="graph-container_1" class="graph-container_1"></div>
    </div>
  </div>

</template>

<script>
  import Tools from '@/utils/tools.js';
  import G6 from "@antv/g6";
  import customNode from '@/pages/operation/flow/G6Editor/Flow/customNode'
  import customEdge from '@/pages/operation/flow/G6Editor/Flow/customEdge'
  import startSvg from "@/assets/editor/start.svg";
  import funcSvg from "@/assets/editor/func.svg";
  import exclusiveGatewaySvg from "@/assets/editor/exclusiveGateway.svg";
  import parallelGatewaySvg from "@/assets/editor/parallelGateway.svg";
  import joinSvg from "@/assets/editor/join.svg";
  import userTaskSvg from "@/assets/editor/userTask.svg";
  import endSvg from "@/assets/editor/end.svg";
  import cloneDeep from "lodash/cloneDeep";

  export default {
    name: "flow_start",
    data() {
      return {
        graph: null,
      };
    },
    destroyed(){
      if(this.graph){
        this.graph.destroy();
      }
    },
    created() {
      customNode.init()
      customEdge.init()
    },
    methods: {
      selectRow(row) {
        if(this.graph){
          this.graph.destroy();
        }
        this.init(row.processData);
        // document.getElementById('graph-container_1').scrollIntoView({
        //   block: 'start',
        //   inline: 'nearest',
        //   behavior: 'smooth'
        // });
      },
      init(processData) {
        // 使用父级容器的高度
        const height = (processData.maxY || 300) + 100;
        const width = this.$refs.parentContent.clientWidth;

        this.graph = new G6.Graph({
          container: "graph-container_1",
          height: height,
          width: width,
          modes: {
            // 支持的 behavior
            default: [],
            mulitSelect: [],
            addEdge: [],
            moveNode: []
          }
        });

        this.readData(processData);
      },
      readData(data) {
        if (data&&data.nodes) {
          let copyData = cloneDeep(data);
          copyData.nodes.forEach(node => {
            if (node.attrs) {
              if (node.attrs.type === "startevent-none") {
                node.image = startSvg;
              } else if (node.attrs.type == "task") {
                node.image = userTaskSvg;
              } else if (node.attrs.type == "operation") {
                node.image = funcSvg;
              } else if (node.attrs.type == "gateway-exclusive") {
                node.image = exclusiveGatewaySvg;
              } else if (node.attrs.type == "gateway-fork") {
                node.image = parallelGatewaySvg;
              } else if (node.attrs.type == "gateway-join") {
                node.image = joinSvg;
              } else if (node.attrs.type == "endevent-none") {
                node.image = endSvg;
              }
            }
          });

          this.graph.read(copyData);

          // 更新 taskname
          this.graph.getNodes().forEach(node => {
            let model = node.getModel();
            if (model && model.attrs && (model.attrs.type === "task" || model.attrs.type === "operation")) {
              node.getContainer().get("children")[2].attr("text", model.attrs.displayName);
            }
          });
        }
      },
      afterLoad(data) {
        this.$refs.processNodeGrid.list.forEach(row => {
          if (row.json) {
            let jsonObj = JSON.parse(row.json);
            let connectionsInfo = jsonObj.connectionsInfo;
            // let firstNode = {};
            // 按顺序存放链路节点的名字
            let nodeOrder = [];
            if (connectionsInfo) {
              let firstEdge = {to:'end'};
              // 取出start节点，然后才能顺着start节点获取到链路上的节点
              for (const key in connectionsInfo) {
                if (connectionsInfo[key].from == 'start') {
                  firstEdge = connectionsInfo[key];
                  break;
                }
              }
              let to = firstEdge.to;
              // firstNode = jsonObj.nodesInfo[to].attrCache;
              let i = 0;
              // 防止没有end节点，导致一直while循环
              while(to != 'end' && i<Object.keys(connectionsInfo).length) {
                to!=nodeOrder[nodeOrder.length-1] && nodeOrder.push(to);
                for (const key in connectionsInfo) {
                  let connectionInfo = connectionsInfo[key];
                  if (connectionInfo.from == to) {
                    firstEdge = connectionInfo;
                    to = firstEdge.to;
                    break;
                  }
                }
                ++i;
              }
            }
            let nodesInfo = jsonObj.nodesInfo;
            let nodes = [];
            if (nodesInfo) {
              for (const nodeKey of nodeOrder) {
                let node = nodesInfo[nodeKey];
                nodes.push(node.attrCache);
              }
            }
            row.nodes = nodes || [firstNode];
            jsonObj.orignal.maxY = Math.max(...jsonObj.orignal.nodes.map(item => item.y));
            row.processData = jsonObj.orignal;
          }
          row.json = '';
        })
      },
      handleFirstNode(row) {
        console.log('row====',row)
        row.nodeInfo = row.nodes[0];
        return row;
      },
    }
  };
</script>

<style scoped>
/deep/ .el-step__head {
  line-height: 100%;
}
</style>
