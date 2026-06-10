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
export default {
	name: "page",
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
	destroyed(){
		if(this.graph){
			this.graph.destroy();
		}
	},
	methods: {
		init() {
			const height = this.height - 42;
			const width = this.width - 400;

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

			if (this.data.isTrans) {
				this.readOAData(this.data.processData);
			} else {
				this.readData(this.data.processData);
			}
		},
		readOAData(data) {
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
