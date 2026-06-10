<template>
	<div class="wrap-container sn-container" :style="{ height: height, width: width }">
		<div class="sn-content">
			<slot />
			<div class="sn-title"><span> 客户净申购规模变动（亿元）</span><img src="../../../../assets/img/board/img-label.png" /></div>
			<div class="sn-body">
				<div class="wrap-container">
					<div class="chartsdom" v-if="type == 'chart'" :id="id" :style="{ height: parseInt(height) - 30 + 'px', width: width }"></div>
					<MarketTable v-else :tableData="tableData" :height="parseInt(height) - 35" />
				</div>
			</div>
		</div>
	</div>
</template>

<script>
import  * as echarts from "echarts";
import MarketTable from '@/pages/pms/workbench/chart/marketTable.vue'
import marketMixins from '@/pages/pms/workbench/chart/marketMixins.js'

export default {
	name: "marketChart1",
	components: { MarketTable },
	mixins: [marketMixins],
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
			type: 'chart',
			tableData: {},
			chartData: {},
			actionParams: {}
		};
	},
	mounted() {},
	methods: {
		initChart(res) {
			this.chartData = JSON.parse(JSON.stringify(res))
			let myChart = echarts.init(document.getElementById(this.id));
			console.log(res, 'res');
			let { xAxisData, series } = res;
			let legendNmae = [];
			let xDate = [];

			series.forEach((item) => {
				if (item.name == "01") {
					item.name = "个人";
					legendNmae.push("个人");
				} else if (item.name == "02") {
					item.name = "机构";
					legendNmae.push("机构");
				} else {
					item.name = "内部";
					legendNmae.push("内部");
				}
			});

			xAxisData.forEach((item) => {
				xDate.push(item.replace(/^(\d{4})(\d{2})$/, "$1-$2"));
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
					// data: ["个人", "机构", "内部"],
					data: legendNmae,
				},
				grid: {
					left: "3%",
					right: "4%",
					top: "10%",
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
				// series: [
				// 	{
				// 		name: "个人",
				// 		type: "bar",
				// 		data: [979.38, 1087.02, 1054.08, 1078.79, 1237.8, 1198.25],
				// 	},
				// 	{
				// 		name: "机构",
				// 		type: "bar",
				// 		data: [489.69, 543.51, 527.04, 539.4, 618.9, 599.13],
				// 	},
				// 	{
				// 		name: "内部",
				// 		type: "bar",
				// 		data: [244.845, 271.76, -263.52, -269.7, 309.45, 309.45],
				// 	},
				// ],
			};
			myChart.setOption(option, true);

			window.addEventListener("resize", () => {
				myChart.resize();
			});
		},
		initTable(res, dateObj) {
			this.actionParams = dateObj
			const tableHeader = [
				{
					label: '日期',
					prop: 'date'
				},
				{
					label: '个人',
					prop: 'personal'
				},
				{
					label: '机构',
					prop: 'organization'
				},
				{
					label: '内部',
					prop: 'inside'
				}
			]
			this.tableData = {
				tableHeader,
				tableContent: res.rows.map(item => {
					item.date = item.date.replace(/^(\d{4})(\d{2})$/, "$1-$2")
					return item
				})
			}
		},
		setType(type) {
			this.type = type
			if (this.type == 'chart') {
				this.$nextTick(()=>{
					if (this.chartData.series) {
						this.initChart(this.chartData)
					}
				})
			}
		}
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
// .sn-container:after,
// .sn-container:before {
// }

// .sn-container {
// border: 2px solid rgba(92, 255, 255, 0.1);
// }

// .sn-container:before {
// 	left: -2px;
// 	border-left-width: 2px;
// 	border-right-width: 0;
// }

// .sn-container:after {
// 	right: -2px;
// 	border-left-width: 0;
// 	border-right-width: 2px;
// }

// .sn-container .sn-content:after {
// 	content: "";
// 	position: absolute;
// 	bottom: -2px;
// 	width: 8px;
// 	height: 8px;
// 	border-color: #20daec;
// 	border-style: solid;
// 	border-top-width: 0;
// 	border-bottom-width: 2px;
// 	left: -2px;
// 	border-left-width: 2px;
// 	border-right-width: 0;
// }
// .sn-container .sn-content:before {
// 	content: "";
// 	position: absolute;
// 	bottom: -2px;
// 	width: 8px;
// 	height: 8px;
// 	border-color: #20daec;
// 	border-style: solid;
// 	border-top-width: 0;
// 	border-bottom-width: 2px;
// 	right: -2px;
// 	border-left-width: 0;
// 	border-right-width: 2px;
// }
</style>
