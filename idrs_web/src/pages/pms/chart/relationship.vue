<template>
	<div class="app-container">
		<!--    左边的按钮组-->
		<!-- <div class="button-wrapper">
			<div v-for="button in buttonGroup" :key="button.label">
				<div class="button" @click="click(button.type)">
					<div class="icon"><img class="btnIcon" :src="button.icon" alt="button.label" /></div>
					{{ button.label }}
				</div>
			</div>
		</div> -->
		<!--    右边的绘制区域-->
		<div id="flowWrap" ref="flowWrap" class="flow-wrapper">
			<div id="table-flow">
				<!--        对齐辅助线-->
				<div
					v-show="auxiliaryLine.isShowXLine"
					class="auxiliary-line-x"
					:style="{ width: auxiliaryLinePos.width, top: auxiliaryLinePos.y + 'px', left: auxiliaryLinePos.offsetX + 'px' }"
				></div>
				<div
					v-show="auxiliaryLine.isShowYLine"
					class="auxiliary-line-y"
					:style="{ height: auxiliaryLinePos.height, left: auxiliaryLinePos.x + 'px', top: auxiliaryLinePos.offsetY + 'px' }"
				></div>
				<TableNode
					v-for="node in json.nodes"
					:id="node.name"
					:key="node.name"
					:node="node"
					@mouseoverNode="mouseoverNode"
					@mouseoutNode="mouseoutNode"
					@handleClick="handleClick"
				/>
			</div>
		</div>
	</div>
</template>

<script>
import jsplumbModule from "jsplumb";
import commConfig from "./config/jsplumbConfig";
import buttonGroup from "./config/buttonGroup";
import buttonMethods from "./methods/buttonMethods";
import colorFields from "./config/tableTypeMappingColor";
import comm from "./methods/comm";
import $ from "jquery";
import TableNode from "./components/TableNode";
import sampleData from "./config/sampleData.json";

const jsplumb = jsplumbModule.jsPlumb;
export default {
	name: "Index",
	components: {
		TableNode,
	},
	data() {
		return {
			jsplumbInstance: null,
			json: {
				nodes: [],
				edges: [],
			},
			buttonGroup: buttonGroup,
			commConfig: commConfig,
			auxiliaryLine: { isShowXLine: false, isShowYLine: false }, //对齐辅助线是否显示
			auxiliaryLinePos: { width: "100%", height: "100%", offsetX: 0, offsetY: 0, x: 20, y: 20 },
			minus: "-", //表名和列名的分割符号
			anchorArr: ["Left", "Right"], //specified anchor 锚点
			commGrid: [5, 5], //节点移动最小距离
			activeId: "",
		};
	},

  created() {
    //设置jsplumb实例,设置jsplumb默认配置，仅初始化一次
    this.jsplumbInstance = jsplumb.getInstance().importDefaults(commConfig);
  },

  mounted() {
    this.renderDefaultLineage();
  },

  activated() {
    this.renderDefaultLineage();
  },

	beforeDestroy() {
		this.jsplumbInstance.reset();
	},
	methods: {
		renderDefaultLineage() {
      this.json = this.$route.params.data;
			// this.json.nodes = sampleData.nodes;
			// this.json.edges = sampleData.edges;
			this.init();
		},
		//初始化
		init() {
			this.fixNodesPosition();
			//nextTick 立即更新DOM
			this.$nextTick(() => {
				this.initialize();
			});
		},
		//真正的初始化
		initialize() {
			jsplumb.ready(() => {
        //设置jsplumb容器
        this.jsplumbInstance.setContainer("table-flow");
				// 先清除一下画布,防止缓存
				this.jsplumbInstance.reset();

				this.drawing(this.anchorArr);

				// 会使整个jsPlumb立即重绘。
				this.jsplumbInstance.setSuspendDrawing(false, true);
				this.initPanZoom();
			});
		},
		// 绘制
		drawing(anchorArr) {
			if (this.json.nodes.length !== 0 && this.json.edges.length !== 0) {
				//1 绘制节点信息
				this.json.nodes.forEach((node) => {
					//使节点可拖动
					// this.draggableNode(node.name);
					node.fields.forEach((field) => {
						//表字段添加端点
						this.addEndpoint(node.name.concat(this.minus, field.name), anchorArr);
						//表头添加端点
						this.addEndpoint(node.name.concat(this.minus), anchorArr);
					});
				});
				//2 绘制节点间连线
				this.json.edges.forEach((edge) => {
					let from = edge.from.name.concat(this.minus, edge.from.field, this.minus, "Right"),
						to = edge.to.name.concat(this.minus, edge.to.field, this.minus, "Left");
					this.connectEndpoint(from, to);
				});
			}
		},
		mouseoverNode(v, e) {
			this.handleHighlight(v, e, colorFields[3].color, true);
		},
		mouseoutNode(v, e) {
			if (e.target.id == this.activeId) {
				return;
			}
			this.handleHighlight(v, e, colorFields[5].color);
		},
		handleHighlight(v, e, color, isActive = false) {
			const div = document.getElementById(e.target.id);
			if (div) {
				div.style.backgroundColor = color;
				const obj = { tbName: v.name, column: v.field };
				const nodePrev = this.getPrevNode(obj);
				const nodeNext = this.getNextNode(obj);
				const arr = nodePrev.concat(nodeNext);
				arr.forEach((item) => {
					this.setLineHighlight(item.start, item.end, isActive);
				});
				const nodePrev1 = nodePrev.map(item=>item.start);
				const nodeNext1 = nodeNext.map(item=>item.end);
				const arr1 = nodePrev1.concat(nodeNext1);
				arr1.forEach((item)=> {
					this.setNodeHighlight(item, isActive);
				})
			}
		},
		handleClick(v, e) {
			// this.activeId = e.target.id;
			// this.clearStyle();
			// this.mouseoverNode(v, e);
		},
		clearStyle() {
      this.json.nodes.forEach((item) => {
				item.fields.forEach((cItem) => {
					const div = document.getElementById(item.name + this.minus + cItem.name);
					if (div) {
						const obj = { tbName: item.name, column: cItem.name };
						this.setNodeHighlight(obj);
						this.setLineHighlight(obj);
					}
				});
			});
		},
		getPrevNode(an, arr = []) {
			const prevItem = this.json.edges.filter((item) => {
				return item.to.name == an.tbName && item.to.field == an.column;
			});
			if (prevItem.length) {
				prevItem.forEach((item) => {
					const { from } = item;
					const obj = {
						tbName: from.name,
						column: from.field,
					};
					arr.push({start: obj, end: an});
					this.getPrevNode(obj, arr);
				});
			}
			return arr;
		},
		getNextNode(an, arr = []) {
			const nextItem = this.json.edges.filter((item) => {
				return item.from.name == an.tbName && item.from.field == an.column;
			});
			if (nextItem.length) {
				nextItem.forEach((item) => {
					const { to } = item;
					const obj = {
						tbName: to.name,
						column: to.field,
					};
					arr.push({start: an, end: obj});
					this.getNextNode(obj, arr);
				});
			}
			return arr;
		},
		setLineHighlight(an, target, isActive = false) {
			const full = an.tbName + this.minus + an.column;
			const tg = target.tbName + this.minus + target.column;
			this.jsplumbInstance
				.getConnections({
					source: full,
					target: tg
				})
				.map((c) => {
					c.setPaintStyle({
						stroke: colorFields[isActive ? 3 : 2].color,
					});
				});
		},
		setNodeHighlight(an, isActive = false) {
			const full = an.tbName + this.minus + an.column;
			$("#" + an.tbName + "-fields")
				.find("#" + full)
				.css("background-color", colorFields[isActive ? 4 : 5].color);
		},
		...comm,
		...buttonMethods,
	},
};
</script>

<style lang="scss" scoped>
.app-container {
	display: flex;
	width: 100%;
	height: 100%;
	border: 1px solid #ccc;
	background: #fff;

	.button-wrapper {
		width: 150px;
		height: 100%;
		border-right: 1px solid #ccc;

		.icon {
			width: 40px;
			height: 40px;
		}

		.button {
			display: flex;
			height: 40px;
			width: 80%;
			margin: 15px auto;
			border: 1px solid #ccc;
			border-radius: 5px;
			line-height: 40px;
			font-size: 11px;

			&:hover {
				cursor: pointer;
			}

			.log {
				width: 40px;
				height: 40px;
			}

			.name {
				width: 0;
				flex-grow: 1;
			}
		}

		.btnIcon {
			width: 40px;
			height: 40px;
		}
	}

	.flow-wrapper {
		height: 100%;
		position: relative;
		overflow: hidden;
		outline: none !important;
		flex-grow: 1;
		background-image: url("../../../assets/point.png");

		#table-flow {
			position: relative;
			// 调大width目的是暂时解决节点拖动到table-flow区域外时(如flow-wrapper)节点宽度自动变窄的问题
			width: 1000%;
			height: 100%;

			.auxiliary-line-x {
				position: absolute;
				border: 0.5px dashed #2ab1e8;
				z-index: 9999;
			}

			.auxiliary-line-y {
				position: absolute;
				border: 0.5px dashed #2ab1e8;
				z-index: 9999;
			}

			/deep/ svg {
				z-index: 9999;
			}
		}
	}
}
</style>

<style lang="scss">
// 下面是鼠标移动到连线上时激活的样式
.jtk-connector.jtk-hover {
	z-index: 9999;

	path {
		// cursor: pointer !important;
	}
}
</style>
