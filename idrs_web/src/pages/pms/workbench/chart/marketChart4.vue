<template>
	<div class="wrap-container sn-container" :style="{ height: height, width: width }">
		<div class="sn-content">
			<slot />
			<div class="sn-title"><span>系列规模情况（万份）</span><img src="../../../../assets/img/board/img-label.png" /></div>
			<div class="selectClass">
				<span style="color: #4bb6ff; font-size: 12px">日期</span>
				<el-date-picker
					v-model="valueDate"
					type="date"
					placeholder="选择日期"
					size="mini"
					style="width: 123px"
					format="yyyy-MM-dd"
					value-format="yyyy-MM-dd"
					@change="changeTime2"
				>
				</el-date-picker>
			</div>
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
import moment from "moment";
import Tools from "@/utils/tools";

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
			// valueDate: "2021-10-10",
			valueDate: "", //"2020-01-01",
			optionsValue: 0,
			options: [
				{ label: "按月", value: 0 },
				{ label: "按季", value: 1 },
				{ label: "按年", value: 2 },
			],
		};
	},
	created() {
		// this.getTime();
	},
	mounted() {
		this.initDate();
	},
	methods: {
		async initDate() {
			await this.getTime();
			await this.getEchart();
		},
		async getTime() {
			await this.httpUtil.sysDate().then((res) => {
				if (res) {
					this.valueDate = Tools.formatDate(res);
					// nowYearFirstDay2 = Tools.formatDate(res);
				}
			});
		},
		async getEchart() {
			let resDate;

			let startT = moment(new Date(this.valueDate).getTime()).format("YYYYMMDD");

			await this.httpUtil
				.ajax({
					url: "server/form/PmsApp/direct/get_prod_series.json",
					params: { startDate: startT },
				})
				.then((res) => {
					if (res.success) {
						// console.log("res", res);
						resDate = res.data;
					}
				});

			let { series, legendData, xAxisData } = resDate;

			series &&
				series.forEach((item) => {
					if (item.data.length == 0) {
						item.data = [0, 0, 0, 0];
					}
					if (item.type == "bar") {
						item.barWidth = 15;
					}
				});

			// console.log("series: ----", series, legendData, xAxisData);

			let option = {
				color: ["#A168E2", "#F4A101", "#49AFFA"],
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
					x: "left",
					// data: ["规模", "同比", "环比"],
					data: legendData,
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
						// data: ["缤赢", "创赢", "稳赢", "安赢"],
						data: xAxisData,
					},
				],
				yAxis: [
					{
						type: "value",
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
					},
					{
						type: "value",
						// min: 0,
						// max: 25,
						// interval: 5,
						axisLabel: {
							show: true,
							textStyle: {
								color: "#ffffff",
							},
							formatter: "{value} %",
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
				// 		barWidth: 15,
				// 		data: [1000000, 2000000, 4000000],
				// 		name: "规模",
				// 		type: "bar",
				// 	},
				// 	{
				// 		barWidth: 15,
				// 		data: ["0.00", "100.00", "33.33"],
				// 		name: "同比",
				// 		type: "line",
				// 		yAxisIndex: 1,
				// 	},
				// 	{
				// 		barWidth: 15,
				// 		data: ['0.00', '22122.22', '300.00'],
				// 		name: "环比",
				// 		type: "line",
				// 		yAxisIndex: 1,
				// 	},
				// ],
				// series: [
				// 	{
				// 		name: "规模",
				// 		type: "bar",
				//     barWidth: 15,
				// 		data: [0, 0, 30, 50],
				// 	},
				// 	{
				// 		name: "同比",
				// 		type: "line",
				// 		yAxisIndex: 1,
				// 		data: [489.69, 543.51, 527.04, 539.4, 618.9, 599.13],
				// 	},
				// 	{
				// 		name: "环比",
				// 		type: "line",
				// 		yAxisIndex: 1,
				// 		data: [244.845, 271.76, 263.52, 269.7, 309.45, 309.45],
				// 	},
				// ],
			};

			let myChart = echarts.init(document.getElementById(this.id));
			myChart.setOption(option, true);
			window.addEventListener("resize", () => {
				myChart.resize();
			});
		},
		changeTime() {},
		changeTime2() {
			this.getEchart();
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
