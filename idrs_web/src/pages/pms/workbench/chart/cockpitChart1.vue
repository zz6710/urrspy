<template>
	<div class="wrap-container sn-container" :style="{ height: height, width: width }">
		<div class="sn-content">
			<div class="sn-title" style="margin-bottom: 10px"><span> 中收/管理费</span><img src="../../../../assets/img/board/img-label.png" /></div>
			<div class="sn-body">
				<div class="wrap-container">
					<div class="chartsdom" :id="id" :style="{ height: parseInt(height) - 40 + 'px', width: width }"></div>
					<div class="pdt-info" :class="`info${index}`" v-for="(item, index) in arrData" :key="index">
						<span></span>
						<span>{{ item.name }}：</span>
						<span>{{ item.number }}万元</span>
						<span>（{{ item.percentage }}%）</span>
					</div>
					<div class="tagClass">
						<span style="color: #fff; font-size: 16px; font-weight: 600">目标金额：</span>
						<span style="color: #ff9738; font-size: 24px; font-weight: 600">{{ targetAmount }}</span>
						<span style="color: #ff9738; font-size: 16px; font-weight: 600">万元</span>
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
			option: null,
			targetAmount: "",
			arrData: [
				// {
				// 	name: "2020-9",
				// 	number: 112196.97,
				// 	percentage: 75.0,
				// },
				// {
				// 	name: "2020-8",
				// 	number: 100378.59,
				// 	percentage: 67.0,
				// },
				// {
				// 	name: "2020-7",
				// 	number: 88170.15,
				// 	percentage: 59.0,
				// },
				// {
				// 	name: "2020-6",
				// 	number: 77530.01,
				// 	percentage: 52.0,
				// },
				// {
				// 	name: "2020-5",
				// 	number: 67133.57,
				// 	percentage: 45.0,
				// },
			],
		};
	},
	mounted() {
		// this.getEchart();
	},
	methods: {
		setMoney(res) {
			this.targetAmount = res;
		},
		getEchart(res) {
			// 初始化echarts实例

      console.log(res, '中收管理');
      res.data.forEach(item => {
          // console.log(item.number, this.targetAmount);
        item.percentage = (Number((Number(item.number) / this.targetAmount).toFixed(4)) * 100).toFixed(2)
      })
      this.arrData = res.data


			let myChart = echarts.init(document.getElementById(this.id));
			let itemStyle = {
				normal: {
					color: "#091f45",
				},
			};

			this.option = {
				color: ["#559DA8", "#D77273", "#4B94DE", "#F4BC56", " #8E5DC6"],
				// title: {
				//   text: '目标中收：150000万元',
				//   left: 'center',
				//   top:300,
				//   textStyle: {
				//     color: '#ccc'
				//   }
				// },
				series: [
					{
						name: "2020年9月",
						type: "pie",
						clockWise: false,
						startAngle: 90,
						hoverAnimation: false,
						radius: ["96%", "90%"],
						center: ["50%", "50%"],
						label: {
							normal: {
								show: false,
							},
						},
						labelLine: {
							normal: {
								show: false,
							},
						},
						data: [
							{
								value: 5,
								name: "50%",
								itemStyle: {
									normal: {
										color: "#559DA8",
									},
								},
							},
							{
								value: 5,
								name: "50%",
								itemStyle: itemStyle,
							},
						],
					},
					{
						name: "2020年8月",
						type: "pie",
						clockWise: false,
						startAngle: 90,
						hoverAnimation: false,
						radius: ["76%", "70%"],
						center: ["50%", "50%"],
						label: {
							normal: {
								show: false,
							},
						},
						labelLine: {
							normal: {
								show: false,
							},
						},
						data: [
							{
								value: 3,
								name: "50%",
								itemStyle: {
									normal: {
										color: "#D77273",
									},
								},
							},
							{
								value: 7,
								name: "50%",
								itemStyle: itemStyle,
							},
						],
					},
					{
						name: "2020年7月",
						type: "pie",
						clockWise: false,
						startAngle: 90,
						hoverAnimation: false,
						radius: ["56%", "50%"],
						center: ["50%", "50%"],
						label: {
							normal: {
								show: false,
							},
						},
						labelLine: {
							normal: {
								show: false,
							},
						},
						data: [
							{
								value: 2,
								name: "50%",
								itemStyle: {
									normal: {
										color: "#4B94DE",
									},
								},
							},
							{
								value: 8,
								name: "50%",
								itemStyle: itemStyle,
							},
						],
					},
					{
						name: "2020年6月",
						type: "pie",
						clockWise: false,
						startAngle: 90,
						hoverAnimation: false,
						radius: ["36%", "30%"],
						center: ["50%", "50%"],
						label: {
							normal: {
								show: false,
							},
						},
						labelLine: {
							normal: {
								show: false,
							},
						},
						data: [
							{
								value: 10,
								name: "50%",
								itemStyle: {
									normal: {
										color: "#F4BC56",
									},
								},
							},
							{
								value: 22,
								name: "50%",
								itemStyle: itemStyle,
							},
						],
					},
					{
						name: "2020年5月",
						type: "pie",
						clockWise: false,
						startAngle: 90,
						hoverAnimation: false,
						radius: ["16%", "10%"],
						center: ["50%", "50%"],
						label: {
							normal: {
								show: false,
							},
						},
						labelLine: {
							normal: {
								show: false,
							},
						},
						data: [
							{
								value: 10,
								name: "50%",
								itemStyle: {
									normal: {
										color: "#8E5DC6",
									},
								},
							},
							{
								value: 22,
								name: "50%",
								itemStyle: itemStyle,
							},
						],
					},
				],
			};


      this.arrData.length > 0 && this.option.series.forEach((item, index) => {
        if(this.arrData[index]) {
          item.name = this.arrData[index].name;
          item.data[0].value = this.arrData[index].percentage
          item.data[1].value = 100 - this.arrData[index].percentage
        } else {
          // item.name = this.arrData[index].name;
          item.data[0].value = 0
          item.data[1].value = 1
        }

      })


			// 使用制定的配置项和数据显示图表
			myChart.setOption(this.option, true);

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

	.pdt-info {
		position: absolute;
		left: 50%;
		top: 50%;
		margin-left: -2px;
		transform: translate(0%, -50%);
		white-space: nowrap;
		&.info0 {
			margin-top: -136px;
			span {
				&:nth-child(1) {
					background-color: #559da8;
				}
			}
		}
		&.info1 {
			margin-top: -101px;
			span {
				&:nth-child(1) {
					background-color: #d77273;
				}
			}
		}
		&.info2 {
			margin-top: -67px;
			span {
				&:nth-child(1) {
					background-color: #4b94de;
				}
			}
		}
		&.info3 {
			margin-top: -32px;
			span {
				&:nth-child(1) {
					background-color: #f4bc56;
				}
			}
		}
		&.info4 {
			margin-top: 3px;
			span {
				&:nth-child(1) {
					background-color: #8e5dc6;
				}
			}
		}
		> span {
			font-size: 16px;
			color: #0072bc;
			vertical-align: middle;
			&:nth-child(1) {
				margin-right: 6px;
				display: inline-block;
				width: 12px;
				height: 12px;
				border-radius: 50%;
				box-shadow: 0 0 2em, 0 0 4em, 0 0 6em, 0 0 8em, 0 0 10em, 0 0 0 0.5em rgba(255, 255, 0, 0.1);
				text-decoration: underline;
				cursor: pointer;
				&:hover {
					color: #00aeef;
				}
			}
			&:nth-child(2) {
				font-family: Arial;
				color: #00aeef;
			}
		}
	}

	.tagClass {
		position: absolute;
		bottom: 28px;
		right: 18px;
	}
}
</style>
