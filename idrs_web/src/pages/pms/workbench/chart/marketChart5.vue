<template>
	<div class="wrap-container sn-container" :style="{ height: height, width: width }">
		<div class="sn-content">
			<slot />
			<div class="sn-title"><span> 渠道类型规模（亿元）</span><img src="../../../../assets/img/board/img-label.png" /></div>
			<div class="sn-body">
				<div class="wrap-container">
					<div class="chartsdom" :id="id" :style="{ height: parseInt(height) - 30 + 'px', width: width }"></div>
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
		return {};
	},
	mounted() {
		this.getEchart();
	},
	methods: {
		async getEchart() {
			let resDate;
			let encode;
			let seriesDate = [];
			let endDate;

			await this.httpUtil.sysDate().then((res) => {
				if (res) {
					endDate = res;
				}
			});

			await this.httpUtil
				.ajax({
					url: "server/form/PmsApp/direct/get_channel_scale.json",
					params: { endDate: endDate.toString().slice(0, 6) },
				})
				.then((res) => {
					if (res.success) {
						resDate = res.data;
					}
				});

			for (let index = 0; index < resDate[0].length; index++) {
				if (index > 0) {
					resDate[0][index] = resDate[0][index].replace(/^(\d{4})(\d{2})$/, "$1-$2");
				}
			}

			for (let index = 0; index < resDate.length; index++) {
				if (index > 0) {
					resDate[index].forEach((item, itemIndex) => {
						if (!item) {
							resDate[index][itemIndex] = 0;
						}
						if (itemIndex > 1) {
							resDate[index][itemIndex] = Number(resDate[index][itemIndex]);
						}
					});
				}

				if (index == resDate.length - 1) {
					seriesDate.push({
						type: "pie",
						id: "pie",
						radius: "50%",
						center: ["85%", "50%"],
						label: {
							// formatter: "{b}: {@2012} ({d}%)",
							formatter: "{b}: {@2012} ({d}%)",
						},
						encode: {
							itemName: resDate[0][0],
							value: resDate[0][1],
							tooltip: resDate[0][1],
						},
					});
				} else {
          seriesDate.push({ type: "line", smooth: true, seriesLayoutBy: "row" });
        }
			}

			// encode = {
			// 	itemName: resDate[0][0],
			// 	value: resDate[0][1],
			// 	tooltip: resDate[0][1],
			// };

			// seriesDate[resDate.length - 1].encode = encode;

			// for (let index = 0; index < resDate.length; index++) {
			//   const element = array[index];

			// }

			let option = {
				color: ["#F2BA55", "#4B95DF", "#70D0D9", "#EC7D68"],
				legend: {
					textStyle: {
						fontSize: 18, //字体大小
						color: "#a9e1f0", //字体颜色
					},
				},
				tooltip: {
					trigger: "axis",
					showContent: true,
				},
				dataset: {
					source: resDate,
					// source: [
					// 	["product", "2020-04", "2020-05", "2020-06", "2020-07", "2020-08", "2020-09"],
					// 	["广发银行", 1199.256, 1181.82, 980.44, 1494.19, 1400.43, 1401.49],
					// 	["直销系统", 599.63, 666.12, 666.12, 689.63, 700.22, 885.15],
					// 	["支付宝", 99.94, 128.93, 217.88, 91.95, 135.53, 98.35],
					// 	["银河证券", 99.94, 171.9, 261.45, 22.99, 22.59, 73.76],
					// ],
				},
				xAxis: {
					type: "category",
					axisLabel: {
						show: true,
						textStyle: {
							color: "#fff",
						},
					},
				},
				yAxis: {
					gridIndex: 0,
					axisLine: {
						show: false, // 不显示坐标轴线
					},
					axisLabel: {
						show: true,
						textStyle: {
							color: "#ffffff",
						},
					},
					splitLine: {
						show: true,
						lineStyle: {
							color: ["#2b406b"],
							type: "solid",
						},
					},
				},
				grid: { right: "30%" },
				series: seriesDate,
				// series: [
				// 	// { type: "line", smooth: true, seriesLayoutBy: "row" },
				// 	// { type: "line", smooth: true, seriesLayoutBy: "row" },
				// 	{ type: "line", smooth: true, seriesLayoutBy: "row" },
				// 	{ type: "line", smooth: true, seriesLayoutBy: "row" },
				// 	{
				// 		type: "pie",
				// 		id: "pie",
				// 		radius: "50%",
				// 		center: ["85%", "50%"],
				// 		label: {
				// 			// formatter: function (data) {},
				// 			formatter: "{b}: {@2012} ({d}%)",
				// 		},
				// 		// encode: {
				// 		// 	itemName: "product",
				// 		// 	value: "202201",
				// 		// 	tooltip: "202201",
				// 		// },
				// 		encode: encode,
				// 	},
				// ],
			};


			let myChart = echarts.init(document.getElementById(this.id));

			myChart.on("updateAxisPointer", function (event) {
				const xAxisInfo = event.axesInfo[0];
				if (xAxisInfo) {
					const dimension = xAxisInfo.value + 1;
					myChart.setOption({
						series: {
							id: "pie",
							label: {
								formatter: "{b}: {@[" + dimension + "]} ({d}%)",
							},
							encode: {
								value: dimension,
								tooltip: dimension,
							},
						},
					});
				}
			});

			myChart.setOption(option, true);
			window.addEventListener("resize", () => {
				myChart.resize();
			});
		},
		changeTime() {},
		changeTime2() {},
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
}
.selectClass {
	position: absolute;
	right: 10px;
	z-index: 2;
}
</style>
