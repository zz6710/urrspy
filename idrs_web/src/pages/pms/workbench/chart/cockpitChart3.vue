<template>
	<div class="wrap-container sn-container" :style="{ height: height, width: width }">
		<div class="sn-content">
			<div class="sn-title"><span> 保有量前五增量规模变化</span><img src="../../../../assets/img/board/img-label.png" /></div>
			<!-- <div class="selectClass">
				<span style="color: #4bb6ff; font-size: 12px">日期</span>
				<el-date-picker v-model="valueDate" type="date" placeholder="选择日期" size="mini" style="width: 123px" @change="changeTime2"> </el-date-picker>
			</div> -->
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
		// this.getEchart();
	},
	methods: {
		getEchart(res) {
      let {series, xAxis} = res
      let xDate = []
      let legendName = []
			xAxis.forEach((item) => {
				xDate.push(item.replace(/^(\d{4})(\d{2})$/, "$1-$2"));
			});
			series.forEach((item) => {
				legendName.push(item.name);
			});
			let myChart = echarts.init(document.getElementById(this.id));
			let option = {
				color: ["#905EC9", "#F4BC56", "#4B94DE", "#D77273", "#64B8C2"],
				tooltip: {
					trigger: "axis",
					axisPointer: {
						// 坐标轴指示器，坐标轴触发有效
						type: "shadow", // 默认为直线，可选为：'line' | 'shadow'
					},
				},
				legend: {
					textStyle: {
						color: "#ffffff", //字体颜色
					},
					// x: "left",
					// data: ["上海市", "北京市", "广州市", "青岛市", "深圳市"],
          data: legendName,
				},
				grid: {
					left: "3%",
					right: "4%",
					top: "14%",
					bottom: "6%",
					containLabel: true,
				},
				xAxis: [
					{
						type: "category",
						axisLabel: {
							show: true,
							textStyle: {
								color: "#ffffff",
							},
						},
						// data: ["1月", "2月", "3月", "4月", "5月", "6月"],
            data: xDate,
					},
				],
				yAxis: [
					{
						type: "value",
						// name: '',
						axisLabel: {
							show: true,
							textStyle: {
								color: "#ffffff",
							},
							formatter: "{value} 万元",
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
					},
				],
        series: series,
				// series: [
				// 	{
				// 		name: "上海市",
				// 		type: "line",
				// 		barWidth: 15,
				// 		// stack: "zc",
				// 		data: [489.69, 543.51, 527.04, 539.4, 618.9, 599.13],
				// 	},
				// 	{
				// 		name: "北京市",
				// 		type: "line",
				// 		// stack: "zc",
				// 		data: [39.4, 618.9, 599.13, 269.7, 489.69, 543.51],
				// 	},
				// 	{
				// 		name: "广州市",
				// 		type: "line",
				// 		// stack: "zc",
				// 		data: [244.845, 269.7, 309.45, 309.45, 271.76, 263.52],
				// 	},
				// 	{
				// 		name: "青岛市",
				// 		type: "line",
				// 		// stack: "zc",
				// 		data: [100.69, 150.51, 527.04, 539.4, 300.9, 323.13],
				// 	},

				// 	{
				// 		name: "深圳市",
				// 		type: "line",
				// 		// stack: "zc",
				// 		data: [23.845, 122.76, 321.52, 123.7, 231.45, 123.45],
				// 	},
				// ],
			};
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
}
.selectClass {
	position: absolute;
	right: 10px;
	z-index: 2;
}
</style>
