<template>
  <div class="item-wrapper">
    <div v-for="(item, index) in list" :key="index" class="item">
      <img
        :src="item.dragItemConfig.image"
        :data-shape="item.shape"
        :data-type="item.type"
        :data-size="item.size"
        :style="{width: item.dragItemConfig.width +'px',height: item.dragItemConfig.height +'px' }"
        draggable
        @dragstart="handleDragstart"
        @dragend="handleDragEnd($event,item)"
      />
      <div class="node-label">{{item.name}}</div>
    </div>
  </div>
</template>

<script>
  import eventBus from "@/utils/eventBus";
  import rectSvg from "@/assets/editor/rect.svg";
  import downSvg from "@/assets/editor/down.svg";
  import startSvg from "@/assets/editor/start.svg";
  import lbxImage from "@/assets/editor/lbx.png";
  import exclusiveGatewaySvg from "@/assets/editor/exclusiveGateway.svg";
  import parallelGatewaySvg from "@/assets/editor/parallelGateway.svg";
  import joinSvg from "@/assets/editor/join.svg";
  import userTaskSvg from "@/assets/editor/userTask.svg";
  import endSvg from "@/assets/editor/end.svg";
  import cloneDeep from "lodash/cloneDeep";
  import uniqueId from "lodash/uniqueId";

  export default {
    data() {
      return {
        page: null,
        command: null,
        offsetX: 0,
        offsetY: 0,
        list: [
          {
            name: "开始",
            size: "50*35",
            type: "cicle",
            x: 0,
            y: 0,
            style:{
              fill:'blue',
            },
            shape: "customNode",
            color: "#1890ff",
            image: startSvg,
            imageWidth: 25,
            imageHeight: 20,
            inPoints: [
              [0, 0.5],
              [0.5, 0]
            ],
            outPoints: [
              [1, 0.5],
              [0.5, 1]
            ],
            // anchorPoints: [[0, 0.5],[0.5, 0],[1, 0.5],[0.5, 1]],
            dragItemConfig: {
              image: startSvg,
              width: 42,
              height: 42
            },
            attrs: {
              type: "startevent-none",
              name: "start"
            }
          },
          {
            name: "任务节点",
            label: "",
            size: "85*35",
            type: "node",
            x: 0,
            y: 0,
            shape: "k-Rect",
            color: "#1890ff",
            inPoints: [
              [0, 0.5],
              [0.5, 0]
            ],
            outPoints: [
              [1, 0.5],
              [0.5, 1]
            ],
            image: rectSvg,
            dragItemConfig: {
              image: rectSvg,
              width: 80,
              height: 44
            },
            attrs: {
              type: "task",
              name: "",
              displayName: "",
            },
            state:"0",
          },
          // {
          //   name: "排他网关",
          //   size: "70*50",
          //   type: "node",
          //   x: 0,
          //   y: 0,
          //   shape: "customNode",
          //   color: "#1890ff",
          //   inPoints: [
          //     [0, 0.5],
          //     [0.5, 0]
          //   ],
          //   outPoints: [
          //     [1, 0.5],
          //     [0.5, 1]
          //   ],
          //   image: exclusiveGatewaySvg,
          //   imageWidth: 40,
          //   imageHeight: 40,
          //   dragItemConfig: {
          //     image: exclusiveGatewaySvg,
          //     width: 48,
          //     height: 48
          //   },
          //   attrs: {
          //     type: "gateway-exclusive",
          //     name: "",
          //     displayName: "",
          //     enableAttachment: "0",
          //     useRole: "1"
          //   }
          // },
          {
            name: "合并网关",
            size: "70*50",
            type: "node",
            x: 0,
            y: 0,
            shape: "customNode",
            color: "#1890ff",
            inPoints: [
              [0, 0.5],
              [0.5, 0]
            ],
            outPoints: [
              [1, 0.5],
              [0.5, 1]
            ],
            image: joinSvg,
            imageWidth: 40,
            imageHeight: 40,
            dragItemConfig: {
              image: joinSvg,
              width: 48,
              height: 48
            },
            attrs: {
              type: "gateway-join",
              name: ""
            },
            state: "0",
          },
          {
            name: "结束",
            size: "50 * 35",
            type: "node",
            x: 0,
            y: 0,
            shape: "customNode",
            color: "#1890ff",
            inPoints: [
              [0, 0.5],
              [0.5, 0]
            ],
            outPoints: [
              [1, 0.5],
              [0.5, 1]
            ],
            image: endSvg,
            imageWidth: 25,
            imageHeight: 20,
            dragItemConfig: {
              image: endSvg,
              width: 42,
              height: 42
            },
            attrs: {
              type: "endevent-none",
              name: "end"
            }
          },
          {
            name: "任务",
            size: "75*30",
            type: "node",
            x: 0,
            y: 0,
            shape: "dd",
            color: "#1890ff",
            label:"",
            inPoints: [
              [0, 0.5],
              [0.5, 0]
            ],
            outPoints: [
              [1, 0.5],
              [0.5, 1]
            ],
            image: rectSvg,
            imageWidth: 20,
            imageHeight: 20,
            dragItemConfig: {
              image: rectSvg,
              width: 42,
              height: 42
            },
            attrs: {
              type: "task",
              name: "",
              displayName: "",
            },
            state:"0",
          },
        ]
      };
    },
    created() {
      this.bindEvent();
    },
    methods: {
      handleDragstart(e) {
        this.offsetX = e.offsetX;
        this.offsetY = e.offsetY;
      },
      handleDragEnd(e, item) {
        let data = {};
        Object.assign(data, item);
        data.offsetX = this.offsetX;
        data.offsetY = this.offsetY;
        if (this.page) {
          const graph = this.page.graph;
          // const size = e.target.dataset.size.split("*");
          const xy = graph.getPointByClient(e.x, e.y);
          data.x = xy.x;
          data.y = xy.y;
          data.size = item.size.split("*");
          data.type = "node";
          let res = cloneDeep(data);
          if (res.attrs.type === "task") {
            let taskName = "task" + uniqueId();
            res.attrs.name = taskName;
            res.label = taskName;
            res.displayName = taskName;
          }
          if (res.attrs.type === "gateway-exclusive") {
            res.attrs.name = "decision" + uniqueId();
          }
          if (res.attrs.type === "gateway-fork") {
            res.attrs.name = "fork" + uniqueId();
          }
          if (res.attrs.type === "gateway-join") {
            res.attrs.name = "join" + uniqueId();
          }
          this.command.executeCommand("add", [res]);
        }
      },
      bindEvent() {
        eventBus.$on("afterAddPage", page => {
          this.page = page;
          this.command = page.command;
        });
      }
    }
  };
</script>

<style scoped>
  .item-wrapper {
    padding: 16px;
    text-align: center;
  }

  .item img {
    padding: 4px;
    border: 1px solid rgba(0, 0, 0, 0);
    border-radius: 2px;
  }

  .item img:hover {
    border: 1px solid #ccc;
    cursor: move;
  }

  .item .node-label {
    font-size: 14px;
    line-height: 1.5;
    color: rgba(0, 0, 0, 0.65);
  }

  .itempannel {
    height: 100%;
    position: absolute;
    left: 0px;
    z-index: 2;
    background: #f7f9fb;
    width: 200px;
    padding-top: 8px;
    border-right: 1px solid #e6e9ed;
  }
  .itempannel ul {
    padding: 0px;
    padding-left: 16px;
  }
  .itempannel li {
    color: rgba(0, 0, 0, 0.65);
    border-radius: 4px;
    width: 160px;
    height: 28px;
    line-height: 26px;
    padding-left: 8px;
    border: 1px solid rgba(0, 0, 0, 0);
    list-style-type: none;
  }
  .itempannel li:hover {
    background: white;
    border: 1px solid #ced4d9;
    cursor: move;
  }

  .itempannel .pannel-type-icon {
    width: 16px;
    height: 16px;
    display: inline-block;
    vertical-align: middle;
    margin-right: 8px;
  }
</style>
