<template>
	<div class="wrap-container sn-container" :style="{ height: height, width: width }">
		<div class="sn-content">
			<slot />
			<div class="sn-title"><span> 净申购（亿元）</span><img src="../../../../assets/img/board/img-label.png" /></div>
			<div class="selectClass">
				<span style="color: #4bb6ff; font-size: 12px">展示维度</span>
				<el-select v-model="optionsValue" size="mini" style="width: 70px" @change="changeTime">
					<el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"> </el-option>
				</el-select>
				<span style="color: #4bb6ff; font-size: 12px">日期</span>
				<el-date-picker
					v-if="optionsValue == 0"
					v-model="valueDate"
					type="date"
					format="yyyy-MM-dd"
					value-format="yyyy-MM-dd"
					placeholder="选择日期"
					size="mini"
					style="width: 123px"
					@change="changeTime2"
				/>
				<el-date-picker
					v-else
					v-model="valueDate"
					type="month"
					placeholder="选择月"
					size="mini"
					style="width: 123px"
					@change="changeTime2"
				/>
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
import Tools from "@/utils/tools.js";

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
			valueDate: "", //"2020-01-01",
			optionsValue: 0,
			options: [
				{ label: "按日", value: 0 },
				{ label: "按月", value: 1 },
				// { label: "按年", value: 2 },
			],
		};
	},
	mounted() {
		// this.getEchart();
	},
	methods: {
		initData(res) {
			this.valueDate = res+'';
			this.getEchart();
		},
		async getEchart() {
			let resDate;
			let legendNmae = [];
			let xDate = [];
			let endDate = "";

			// await this.httpUtil.sysDate().then((res) => {
			// if (res) {
			// if(this.optionsValue == 0) {    // 按日

			// this.valueDate = Tools.formatDate(res);   // 2020-01-01
			// } else {        // 按月
			// this.valueDate = moment(new Date(Tools.formatDate(res))).format("YYYY-MM");
			// this.valueDate = moment(new Date(Tools.formatDate(res))).format("YYYYMM");
			//     }
			//     startTDate = res;     // 20200101
			//     startTMonth = res.slice(0, 6);   // 202001
			// 	}
			// });

			if (this.optionsValue == 0) {
				// 按日
				let startT = moment(new Date(this.valueDate).getTime() - 5 * 24 * 60 * 60 * 1000).format("YYYYMMDD"); // 起始日期
				let endT = moment(new Date(this.valueDate).getTime()).format("YYYYMMDD");
				await this.httpUtil
					.ajax({
						url: "server/form/PmsApp/direct/analyse_by_day.json",
						params: { startDate: startT, endDate: endT },
					})
					.then((res) => {
						if (res.success) {
							resDate = res;
							let { xAxisData } = resDate;
							xAxisData.forEach((item) => {
								xDate.push(item.replace(/^(\d{4})(\d{2})(\d{2})$/, "$1-$2-$3"));
							});
						}
					});
			} else {
				// 按月
				let startT = moment(new Date(this.valueDate).getTime() - 5 * 30 * 24 * 60 * 60 * 1000).format("YYYYMM"); // 起始日期
				let endT = moment(new Date(this.valueDate).getTime()).format("YYYYMM");
				await this.httpUtil
					.ajax({
						url: "server/form/PmsApp/direct/analyse_by_month.json",
						params: { startDate: startT, endDate: endT },
					})
					.then((res) => {
						if (res.success) {
							resDate = res;
							let { xAxisData } = resDate;
							xAxisData.forEach((item) => {
								xDate.push(item.replace(/^(\d{4})(\d{2})$/, "$1-$2"));
							});
						}
					});
			}

			let { series } = resDate;
			series.forEach((item, index) => {
				legendNmae.push(item.name);
				item.data.forEach((itemDate, indexDate) => {
					series[index].data[indexDate] = Number(itemDate);
				});
			});

			let option = {
				color: ["#4EC29B", "#49AFFA", "#F4A101"],
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
					data: legendNmae,
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
						// data: ["2020年4月", "2020年5月", "2020年6月", "2020年7月", "2020年8月", "2020年9月"],
						data: xDate,
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
				],
				series: series,
			};

			let myChart = echarts.init(document.getElementById(this.id));
			myChart.setOption(option, true);
			window.addEventListener("resize", () => {
				myChart.resize();
			});
		},
		changeTime() {
			this.getEchart();
			this.valueDate = 1577808000000;
		},
		changeTime2(val) {
			this.getEchart(val);
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
