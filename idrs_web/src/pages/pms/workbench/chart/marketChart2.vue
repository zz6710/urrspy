<template>
	<div class="wrap-container sn-container" :style="{ height: height, width: width }">
		<div class="sn-content">
			<slot />
			<div class="sn-title"><span> 销售目标达成情况</span><img src="../../../../assets/img/board/img-label.png" /></div>
			<div class="sn-body">
				<div class="wrap-container">
					<div class="chartsdom" :id="id" :style="{ height: parseInt(height) - 30 + 'px', width: width }"></div>
					<div class="chartsdom_label1">
						<div class="chartsdom_line">
							<span class="label_text">年度目标: </span><span class="money_text">{{ yearValue.max }}</span>
							<span class="unit_text">亿元</span>
						</div>
						<div class="chartsdom_line">
							<span class="label_text">已完成量: </span><span class="money_text">{{ yearValue.value }}</span
							><span class="unit_text">亿元</span>
						</div>
					</div>
					<div class="chartsdom_label2">
						<div class="chartsdom_line">
							<span class="label_text">季度目标: </span><span class="money_text">{{ quarterValue.max }}</span
							><span class="unit_text">亿元</span>
						</div>
						<div class="chartsdom_line">
							<span class="label_text">已完成量: </span><span class="money_text">{{ quarterValue.value }}</span
							><span class="unit_text">亿元</span>
						</div>
					</div>
					<div class="chartsdom_label3">
						<div class="chartsdom_line">
							<span class="label_text">月度目标: </span><span class="money_text">{{ monthValue.max }}</span
							><span class="unit_text">亿元</span>
						</div>
						<div class="chartsdom_line">
							<span class="label_text">已完成量: </span><span class="money_text">{{ monthValue.value }}</span
							><span class="unit_text">亿元</span>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
import  * as echarts from "echarts";

export default {
	name: "marketChart1",
	props: {
		className: {
			type: String,
			default: "chart",
		},
		width: {
			type: String,
			default: "100%",
		},
		height: {
			type: String,
			default: "300px",
		},
		id: {
			type: String,
			required: true,
		},
	},
	data() {
		return {
			yearValue: {
				max: 0,
				value: 0,
			},
			monthValue: {
				max: 0,
				value: 0,
			},
			quarterValue: {
				max: 0,
				value: 0,
			},
		};
	},
	methods: {
		initChart(res) {
			let myChart = echarts.init(document.getElementById(this.id));

			let option = {
				//指定图表的配置项和数据
				tooltip: { formatter: "已达成:<br/>{c}" + "%" }, //配置提示框组件
				series: [
					//配置数据系列，共有4个仪表盘
					{
						//设置数据系列之1：速度
						name: "年度目标",
						type: "gauge",
						z: 3,
						min: 0, //设置速度仪表盘的最小值
						max: 130, //设置速度仪表盘的最大值
						splitNumber: 13, //设置速度仪表盘的分隔数目为22
						radius: "65%", //设置速度仪表盘的大小
						axisLine: {
							lineStyle: {
								width: 15,
								color: [
									[0.23, "#82E2E4"],
									[0.77, "#5893FF"],
									[1, "#EF606C"],
								],
							},
						},
						axisLabel: {
							fontSize: 8,
						},
						axisTick: {
							//设置坐标轴小标记
							distance: -15,
							length: 8, //设置属性length控制线长
							splitNumber: 5, //设置坐标轴小标记的分隔数目为5
							lineStyle: {
								color: "#031C51",
								width: 1,
							},
						},
						splitLine: {
							length: 15,
							distance: -15,
							lineStyle: {
								color: "#031C51",
								width: 2,
							},
						},
						pointer: { width: 3, length: 60 },

						title: {
							fontSize: 14,
							fontWeight: 500,
							color: "#0C99E2",
							offsetCenter: [0, "70%"],
						},
						detail: {
							// show: false,
							valueAnimation: true,
							formatter: "{value} %",
							color: "#0C99E2",
							fontSize: 13,
							offsetCenter: [0, "30%"],
						},
						data: [{ value: 2314.4 }],
					},
					{
						//设置数据系列之1：速度
						name: "季度目标",
						type: "gauge",
						min: 0, //设置速度仪表盘的最小值
						max: 130, //设置速度仪表盘的最大值
						splitNumber: 13, //设置速度仪表盘的分隔数目为22
						radius: "55%", //设置速度仪表盘的大小
						center: ["15%", "55%"], //设置转速仪表盘中心点的位置，默认全局居中
						axisLine: {
							lineStyle: {
								width: 12,
								color: [
									[0.23, "#82E2E4"],
									[0.77, "#5893FF"],
									[1, "#EF606C"],
								],
							},
						},
						axisTick: {
							//设置坐标轴小标记
							distance: -12,
							length: 4, //设置属性length控制线长
							splitNumber: 5, //设置坐标轴小标记的分隔数目为5
							lineStyle: {
								color: "#031C51",
								width: 1,
							},
						},
						splitLine: {
							length: 12,
							distance: 0,
							lineStyle: {
								color: "#031C51",
								width: 1,
							},
						},
						pointer: { width: 2, length: 40 },
						axisLabel: {
							fontSize: 8,
						},

						title: {
							fontSize: 12,
							fontWeight: 500,
							color: "#0C99E2",
							offsetCenter: [0, "70%"],
						},
						detail: {
							// show: false,
							valueAnimation: true,
							formatter: "{value} %",
							color: "#0C99E2",
							fontSize: 12,
							offsetCenter: [0, "30%"],
						},
						data: [{ value: 2314.4 }],
					},
					{
						//设置数据系列之1：速度
						name: "月度目标",
						type: "gauge",
						// z: 3,
						min: 0, //设置速度仪表盘的最小值
						max: 130, //设置速度仪表盘的最大值
						splitNumber: 13, //设置速度仪表盘的分隔数目为22
						radius: "55%", //设置速度仪表盘的大小
						center: ["85%", "55%"], //设置转速仪表盘中心点的位置，默认全局居中
						axisLine: {
							lineStyle: {
								width: 12,
								color: [
									[0.23, "#82E2E4"],
									[0.77, "#5893FF"],
									[1, "#EF606C"],
								],
							},
						},
						axisTick: {
							//设置坐标轴小标记
							distance: -12,
							length: 4, //设置属性length控制线长
							splitNumber: 5, //设置坐标轴小标记的分隔数目为5
							lineStyle: {
								color: "#031C51",
								width: 1,
							},
						},
						splitLine: {
							length: 12,
							distance: 0,
							lineStyle: {
								color: "#031C51",
								width: 1,
							},
						},
						pointer: { width: 2, length: 40 },
						axisLabel: {
							fontSize: 8,
						},

						title: {
							fontSize: 12,
							fontWeight: 500,
							color: "#0C99E2",
							offsetCenter: [0, "70%"],
						},
						detail: {
							valueAnimation: true,
							formatter: "{value} %",
							color: "#0C99E2",
							fontSize: 12,
							offsetCenter: [0, "30%"],
						},
						data: [{ value: 150 }],
					},
				],
			};

			let { data } = res;
			data.forEach((item) => {
				if (item.name == "年度目标") {
					this.yearValue.max = item.max;
					this.yearValue.value = item.value;
					if (item.value && item.max) {
						option.series[0].data[0].value = Number(((item.value / item.max) * 100).toFixed(2));
					} else {
						option.series[0].data[0].value = 0;
					}
				} else if (item.name == "季度目标") {
					this.quarterValue.max = item.max;
					this.quarterValue.value = item.value;
					if (item.value && item.max) {
						option.series[1].data[0].value = Number(((item.value / item.max) * 100).toFixed(2));
					} else {
						option.series[1].data[0].value = 0;
					}
				} else if (item.name == "月度目标") {
					this.monthValue.max = item.max;
					this.monthValue.value = item.value;
					if (item.value && item.max) {
						option.series[2].data[0].value = Number(((item.value / item.max) * 100).toFixed(2));
					} else {
						option.series[2].data[0].value = 0;
					}
				}
			});

			myChart.setOption(option, true);

			window.addEventListener("resize", () => {
				myChart.resize();
			});
		},
	},
};
</script>

<style lang="scss" scoped>
.sn-container {
	position: relative;
	background: rgba(12, 47, 99, 0.6);
	border: 1px solid #02144b;

	&::after,
	&::before {
		content: "";
		position: absolute;
		top: -2px;
		width: 8px;
		height: 8px;
		border-color: #20daec;
		border-style: solid;
		border-top-width: 2px;
		border-bottom-width: 0;
	}

	&::before {
		left: -2px;
		border-left-width: 2px;
		border-right-width: 0;
	}

	&::after {
		right: -2px;
		border-left-width: 0;
		border-right-width: 2px;
	}

	.sn-content {
		&::after,
		&::before {
			content: "";
			position: absolute;
			bottom: -2px;
			width: 8px;
			height: 8px;
			border-color: #20daec;
			border-style: solid;
			border-top-width: 0;
			border-bottom-width: 2px;
		}
		&::before {
			left: -2px;
			border-left-width: 2px;
			border-right-width: 0;
		}

		&::after {
			right: -2px;
			border-left-width: 0;
			border-right-width: 2px;
		}
	}

	.sn-title {
		font-size: 17px;
		color: #fff;
		font-weight: 600;
		margin: 8px 20px 0;
	}
	// .chartsdom {
	// 	width: 100%;
	// 	height: 300px;
	// }
	.chartsdom_label1 {
		position: absolute;
		bottom: 10px;
		left: 50%;
		transform: translateX(-50%);
		width: 170px;
		text-align: center;
		line-height: 25px;
	}
	.chartsdom_label2 {
		position: absolute;
		bottom: 6px;
		left: 2px;
		width: 170px;
		text-align: center;
		line-height: 25px;
	}
	.chartsdom_label3 {
		position: absolute;
		bottom: 6px;
		right: 0;
		// transform: translateX(-50%);
		width: 170px;
		text-align: center;
		line-height: 25px;
	}
	.chartsdom_line {
		font-size: 12px;
		color: #0c99e2;
		font-weight: 500;
	}
	.money_text {
		color: rgb(255, 151, 56);
		font-weight: 600;
		font-size: 16px;
	}
	.unit_text {
		display: inline-block;
		transform: scale(0.7);
		font-weight: 600;
	}
	.label_text {
		display: inline-block;
		transform: scale(0.95);
		font-weight: 600;
	}
}
</style>
