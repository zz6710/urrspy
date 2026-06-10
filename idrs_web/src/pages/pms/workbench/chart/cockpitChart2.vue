<template>
	<div class="wrap-container sn-container" :style="{ height: height, width: width }">
		<div class="sn-content">
			<div class="sn-title"><span> 投资收益</span><img src="../../../../assets/img/board/img-label.png" /></div>
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
			option: null,
			dataMap: {},
		};
	},
	mounted() {
		// this.getEchart();
	},
	methods: {
		dataFormatter(obj) {
			let pList = ["债券", "基金", "股票", "货币市场"];
			let temp;
			for (let y = 2016; y <= 2020; y++) {
				let max = 0;
				let sum = 0;
				temp = obj[y];
				for (let i = 0, l = temp.length; i < l; i++) {
					max = Math.max(max, temp[i]);
					sum += temp[i];
					obj[y][i] = {
						name: pList[i],
						value: temp[i],
					};
				}
				obj[y + "max"] = Math.floor(max / 100) * 100;
				obj[y + "sum"] = sum;
			}
			return obj;
		},
		getEchart(res) {
			let myChart = echarts.init(document.getElementById(this.id));
      console.log(res);
      let { dates, options } = res.data;
      let xAxisData = []
      // data.forEach(item => {
      //   item.series[0].forEach(itemData => {
      //     xAxisData.push(itemData)
      //   })
      // });
      options[0] && options[0].series[0].data.forEach(item => {
        xAxisData.push(item.name)
      })
      options.forEach((item) => {
        item.series.forEach((itemSeries) => {
          itemSeries.data.forEach((itemData) => {
            itemData.value = Number(itemData.value)
          })
        })
      })
			let itemStyleJR = {
				color: "#F1BA55",
			};
			let itemStyleZW = {
				color: "#327EAE",
			};
			let itemStyleYL = {
				color: "#8EBBBB",
			};
			let itemStyleIT = {
				color: "#EC7D69",
			};
			let itemStyleHB = {
				color: "#8064A2",
			};

			this.option = {
				baseOption: {
					timeline: {
						axisType: "category",
						autoPlay: true,
						playInterval: 5000,
						// data: ["2020-04", "2020-05", "2020-06", "2020-07", "2020-08", "2020-09"],
            data: dates,
						lineStyle: {
							color: "#179bf1",
						},
						left: 30,
						right: 30,
						label: {
							color: "#2867a8",
							// formatter(s) {
							//   return (new Date(s)).getFullYear();
							// }
						},
						checkpointStyle: {
							color: "#01d8ff",
							symbolSize: 10,
							borderColor: "rgba(1, 216, 255, 0.5)",
							borderWidth: 5,
						},
						controlStyle: {
							showPlayBtn: false,
							borderColor: "#01bde6",
							itemGap: 20,
						},
						itemStyle: {
							// color: '#004b85',
							borderColor: "#004b85",
							borderWidth: 1,
							shadowColor: "rgba(1, 216, 225, 0.5)",
							shadowBlur: 5,
						},
						emphasis: {
							label: {
								color: "#01d8ff",
								show: false,
							},
							checkpointStyle: {
								color: "#01d8ff",
								borderColor: "rgba(1, 216, 255, 0.5)",
								borderWidth: 5,
							},
							controlStyle: {
								borderColor: "rgba(1, 216, 255, 0.5)",
							},
							itemStyle: {
								color: "#01d8ff",
								borderColor: "rgba(1, 216, 225, 0.5)",
								borderWidth: 5,
							},
						},
					},
					// tooltip: {
					//   trigger: 'item'
					// },
					grid: {
						bottom: "20%",
						right: "42%",
					},
					tooltip: {
						trigger: "item",
						// formatter: function (data) {
						// 	if (data.seriesIndex == 0) {
						// 		return data.seriesName + "<br/>" + data.data.name + ":" + data.data.value;
						// 	} else {
						// 		return data.seriesName + "<br/>" + data.data.name + ":" + data.data.value + "(" + data.percent + "%" + ")";
						// 	}
						// },
						// formatter: '{a} <br/>{b} : {c} ({d}%)'
					},
					calculable: true,
					xAxis: [
						{
							type: "category",
							// data: ["债券", "基金", "股票", "货币市场", "非标"],
              data: xAxisData,
							axisLabel: {
								show: true,
								textStyle: {
									color: "#ffffff",
								},
							},
						},
					],
					yAxis: [
						{
							type: "value",
							name: "收益(万元)",
							axisLabel: {
								show: true,
								textStyle: {
									color: "#ffffff",
								},
							},
							axisLine: {
								show: false, // 不显示坐标轴线
							},
							splitLine: {
								show: true,
								lineStyle: {
									color: ["#2b406b"],
									type: "solid",
								},
							},
							axisTick: {
								show: false,
							},
						},
					],
					series: [
						{
							name: "投资收益",
							type: "bar",
							barMaxWidth: 15,
							center: ["20%", "65%"],
							seriesLayoutBy: "row",
						},
						{
							name: "投资收益",
							type: "pie",
							center: ["76%", "50%"],
							radius: "60%",
							z: 100,
						},
					],
				},
        options: options,
				// options: [
				// 	{
				// 		series: [
				// 			{
				// 				data: [
				// 					{
				// 						name: "债券",
				// 						value: 34050.15,
				// 						itemStyle: itemStyleJR,
				// 					},
				// 					{
				// 						name: "基金",
				// 						value: 13081.03,
				// 						itemStyle: itemStyleZW,
				// 					},
				// 					{
				// 						name: "股票",
				// 						value: 6741.48,
				// 						itemStyle: itemStyleYL,
				// 					},
				// 					{
				// 						name: "货币市场",
				// 						value: 8250.94,
				// 						itemStyle: itemStyleHB,
				// 					},
				// 					{
				// 						name: "非标",
				// 						value: 34211.22,
				// 						itemStyle: itemStyleIT,
				// 					},
				// 				],
				// 			},
				// 			{
				// 				data: [
				// 					{
				// 						name: "债券",
				// 						value: 34050.15,
				// 						itemStyle: itemStyleJR,
				// 					},
				// 					{
				// 						name: "基金",
				// 						value: 13081.03,
				// 						itemStyle: itemStyleZW,
				// 					},
				// 					{
				// 						name: "股票",
				// 						value: 6741.48,
				// 						itemStyle: itemStyleYL,
				// 					},
				// 					{
				// 						name: "货币市场",
				// 						value: 8250.94,
				// 						itemStyle: itemStyleHB,
				// 					},
				// 					{
				// 						name: "非标",
				// 						value: 34211.22,
				// 						itemStyle: itemStyleIT,
				// 					},
				// 				],
				// 			},
				// 		],
				// 	},
				// 	{
				// 		series: [
				// 			{
				// 				data: [
				// 					{
				// 						name: "债券",
				// 						value: 33593.39,
				// 						itemStyle: itemStyleJR,
				// 					},
				// 					{
				// 						name: "基金",
				// 						value: 17422.32,
				// 						itemStyle: itemStyleZW,
				// 					},
				// 					{
				// 						name: "股票",
				// 						value: 13915.45,
				// 						itemStyle: itemStyleYL,
				// 					},
				// 					{
				// 						name: "货币市场",
				// 						value: 10051.21,
				// 						itemStyle: itemStyleHB,
				// 					},
				// 					{
				// 						name: "非标",
				// 						value: 38462.94,
				// 						itemStyle: itemStyleIT,
				// 					},
				// 				],
				// 			},
				// 			{
				// 				data: [
				// 					{
				// 						name: "债券",
				// 						value: 33593.39,
				// 						itemStyle: itemStyleJR,
				// 					},
				// 					{
				// 						name: "基金",
				// 						value: 17422.32,
				// 						itemStyle: itemStyleZW,
				// 					},
				// 					{
				// 						name: "股票",
				// 						value: 13915.45,
				// 						itemStyle: itemStyleYL,
				// 					},
				// 					{
				// 						name: "货币市场",
				// 						value: 10051.21,
				// 						itemStyle: itemStyleHB,
				// 					},
				// 					{
				// 						name: "非标",
				// 						value: 38462.94,
				// 						itemStyle: itemStyleIT,
				// 					},
				// 				],
				// 			},
				// 		],
				// 	},
				// 	{
				// 		series: [
				// 			{
				// 				data: [
				// 					{
				// 						name: "债券",
				// 						value: 33390.04,
				// 						itemStyle: itemStyleJR,
				// 					},
				// 					{
				// 						name: "基金",
				// 						value: 15486.21,
				// 						itemStyle: itemStyleZW,
				// 					},
				// 					{
				// 						name: "股票",
				// 						value: 14099.82,
				// 						itemStyle: itemStyleYL,
				// 					},
				// 					{
				// 						name: "itemStyleHB",
				// 						value: 7602.44,
				// 						itemStyle: itemStyleIT,
				// 					},
				// 					{
				// 						name: "非标",
				// 						value: 40849.48,
				// 						itemStyle: itemStyleIT,
				// 					},
				// 				],
				// 			},
				// 			{
				// 				data: [
				// 					{
				// 						name: "债券",
				// 						value: 33390.04,
				// 						itemStyle: itemStyleJR,
				// 					},
				// 					{
				// 						name: "基金",
				// 						value: 15486.21,
				// 						itemStyle: itemStyleZW,
				// 					},
				// 					{
				// 						name: "股票",
				// 						value: 14099.82,
				// 						itemStyle: itemStyleYL,
				// 					},
				// 					{
				// 						name: "货币市场",
				// 						value: 7602.44,
				// 						itemStyle: itemStyleHB,
				// 					},
				// 					{
				// 						name: "非标",
				// 						value: 40849.48,
				// 						itemStyle: itemStyleIT,
				// 					},
				// 				],
				// 			},
				// 		],
				// 	},
				// 	{
				// 		series: [
				// 			{
				// 				data: [
				// 					{
				// 						name: "债券",
				// 						value: 32505.56,
				// 						itemStyle: itemStyleJR,
				// 					},
				// 					{
				// 						name: "基金",
				// 						value: 21612.95,
				// 						itemStyle: itemStyleZW,
				// 					},
				// 					{
				// 						name: "股票",
				// 						value: 21502.11,
				// 						itemStyle: itemStyleYL,
				// 					},
				// 					{
				// 						name: "货币市场",
				// 						value: 7714.16,
				// 						itemStyle: itemStyleHB,
				// 					},
				// 					{
				// 						name: "非标",
				// 						value: 43092.89,
				// 						itemStyle: itemStyleIT,
				// 					},
				// 				],
				// 			},
				// 			{
				// 				data: [
				// 					{
				// 						name: "债券",
				// 						value: 32505.56,
				// 						itemStyle: itemStyleJR,
				// 					},
				// 					{
				// 						name: "基金",
				// 						value: 21612.95,
				// 						itemStyle: itemStyleZW,
				// 					},
				// 					{
				// 						name: "股票",
				// 						value: 21502.11,
				// 						itemStyle: itemStyleYL,
				// 					},
				// 					{
				// 						name: "货币市场",
				// 						value: 7714.16,
				// 						itemStyle: itemStyleHB,
				// 					},
				// 					{
				// 						name: "非标",
				// 						value: 43092.89,
				// 						itemStyle: itemStyleIT,
				// 					},
				// 				],
				// 			},
				// 		],
				// 	},
				// 	{
				// 		series: [
				// 			{
				// 				data: [
				// 					{
				// 						name: "债券",
				// 						value: 35383.97,
				// 						itemStyle: itemStyleJR,
				// 					},
				// 					{
				// 						name: "基金",
				// 						value: 23145.16,
				// 						itemStyle: itemStyleZW,
				// 					},
				// 					{
				// 						name: "股票",
				// 						value: 14268.82,
				// 						itemStyle: itemStyleYL,
				// 					},
				// 					{
				// 						name: "货币市场",
				// 						value: 9156.33,
				// 						itemStyle: itemStyleHB,
				// 					},
				// 					{
				// 						name: "非标",
				// 						value: 55548.39,
				// 						itemStyle: itemStyleIT,
				// 					},
				// 				],
				// 			},
				// 			{
				// 				data: [
				// 					{
				// 						name: "债券",
				// 						value: 35383.97,
				// 						itemStyle: itemStyleJR,
				// 					},
				// 					{
				// 						name: "基金",
				// 						value: 23145.16,
				// 						itemStyle: itemStyleZW,
				// 					},
				// 					{
				// 						name: "股票",
				// 						value: 14268.82,
				// 						itemStyle: itemStyleYL,
				// 					},
				// 					{
				// 						name: "货币市场",
				// 						value: 9156.33,
				// 						itemStyle: itemStyleHB,
				// 					},
				// 					{
				// 						name: "非标",
				// 						value: 55548.39,
				// 						itemStyle: itemStyleIT,
				// 					},
				// 				],
				// 			},
				// 		],
				// 	},
				// 	{
				// 		series: [
				// 			{
				// 				data: [
				// 					{
				// 						name: "债券",
				// 						value: 36999.95,
				// 						itemStyle: itemStyleJR,
				// 					},
				// 					{
				// 						name: "基金",
				// 						value: 32061.74,
				// 						itemStyle: itemStyleZW,
				// 					},
				// 					{
				// 						name: "股票",
				// 						value: 5084.19,
				// 						itemStyle: itemStyleYL,
				// 					},
				// 					{
				// 						name: "货币市场",
				// 						value: 6861.29,
				// 						itemStyle: itemStyleHB,
				// 					},
				// 					{
				// 						name: "非标",
				// 						value: 38893.97,
				// 						itemStyle: itemStyleIT,
				// 					},
				// 				],
				// 			},
				// 			{
				// 				data: [
				// 					{
				// 						name: "债券",
				// 						value: 36999.95,
				// 						itemStyle: itemStyleJR,
				// 					},
				// 					{
				// 						name: "基金",
				// 						value: 32061.74,
				// 						itemStyle: itemStyleZW,
				// 					},
				// 					{
				// 						name: "股票",
				// 						value: 5084.19,
				// 						itemStyle: itemStyleYL,
				// 					},
				// 					{
				// 						name: "货币市场",
				// 						value: 6861.29,
				// 						itemStyle: itemStyleHB,
				// 					},
				// 					{
				// 						name: "非标",
				// 						value: 38893.97,
				// 						itemStyle: itemStyleIT,
				// 					},
				// 				],
				// 			},
				// 		],
				// 	},
				// ],
			};

			myChart.setOption(this.option, true);

			window.addEventListener("resize", () => {
				myChart.resize();
			});
		},
	},
	beforeDestroy() {},
};
</script>

<style lang="scss" scoped>
// .sn-container {
// 	left: 1000px;
// 	top: 2030px;
// 	width: 870px;
// 	height: 400px;
// }
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
</style>
