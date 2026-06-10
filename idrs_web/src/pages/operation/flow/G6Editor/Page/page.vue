<template>
	<div class="page" ref="parentContent">
		<div :id="pageId" class="graph-container" style="position: relative;"></div>
	</div>
</template>

<script>
import G6 from "@antv/g6";
import { initBehavors } from "../behavior";
import testData from "../test";
import startSvg from "@/assets/editor/start.svg";
import funcSvg from "@/assets/editor/func.svg";
import exclusiveGatewaySvg from "@/assets/editor/exclusiveGateway.svg";
import parallelGatewaySvg from "@/assets/editor/parallelGateway.svg";
import joinSvg from "@/assets/editor/join.svg";
import userTaskSvg from "@/assets/editor/userTask.svg";
import endSvg from "@/assets/editor/end.svg";
export default {
	name: "page",
	data() {
		return {
			pageId: "graph-container",
			graph: null,
			timer: null
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
		}
	},
	created() {
		initBehavors();
	},
	mounted() {
		this.$nextTick(() => {
			this.init();
			this.initSize();
		});
	},
	destroyed(){
		if(this.graph){
			this.graph.destroy();
		}
	},
	methods: {
		init() {
			// const height = this.height - 42;
			// const width = this.width - 400;
			// 使用父级容器的高度
			const height = this.$refs.parentContent.clientHeight;
			const width = this.$refs.parentContent.clientWidth;

			this.graph = new G6.Graph({
				container: "graph-container",
				height: height,
				width: width,
				modes: {
					// 支持的 behavior
					default: ["drag-canvas", "hover-node", "select-node", "hover-edge", "keyboard", "customer-events", "add-menu"],
					mulitSelect: ["mulit-select"],
					addEdge: ["add-edge"],
					moveNode: ["drag-item"]
				}
			});
			const { editor, command } = this.$parent;
			editor.emit("afterAddPage", { graph: this.graph, command });

			this.readData(this.data.processData);
		},
		initSize () {
      const self = this
      this.timer = setTimeout(() => {
        // 浏览器窗口发生变化时
        window.onresize = function () {
          // todo 获取div parentContent 的宽度和高度
          // this.canvasWidth = self.$refs.parentContent.clientWidth
          // this.canvasHeight = self.$refs.parentContent.clientHeight
          // todo 修改画布的大小
					if (self.$refs.parentContent) {
          	self.graph.changeSize(self.$refs.parentContent.clientWidth, self.$refs.parentContent.clientHeight)
					}
          // todo 将图移动到画布中心位置
          // self.graph.fitCenter()
        }
      }, 20)
		},

		readData(data) {
			if (data&&data.nodes) {
				data.nodes.forEach(node => {
					if (node.attrs) {
						if (node.attrs.type === "startevent-none") {
							node.image = startSvg;
						} else if (node.attrs.type === "task") {
							node.image = userTaskSvg;
						} else if (node.attrs.type === "operation") {
							node.image = funcSvg;
						} else if (node.attrs.type === "gateway-exclusive") {
							node.image = exclusiveGatewaySvg;
						} else if (node.attrs.type === "gateway-fork") {
							node.image = parallelGatewaySvg;
						} else if (node.attrs.type === "gateway-join") {
							node.image = joinSvg;
						} else if (node.attrs.type === "endevent-none") {
							node.image = endSvg;
						}
					}
				});

				this.graph.read(data);
				// 更新 taskname
				this.graph.getNodes().forEach(node => {
					let model = node.getModel();
					if (model.attrs && (model.attrs.type === "task" || model.attrs.type === "operation")) {
						console.log(node.getContainer(), node.getContainer().get("children"))
						node.getContainer().get("children")[2].attr("text", model.attrs.displayName);
					}
				});
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
	/* 左侧栏宽度200 + 右侧栏宽度382 */
	width: calc(100% - 582px);
	height: 100%;
	position: absolute;
	/* 左侧栏的宽度 */
	left: 200px;
}
/deep/ canvas {
	background-color: #fbf6e9;
}
</style>
