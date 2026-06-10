<template>
	<div class="boardPage">
		<div class="wrap2" ref="editor2">
			<div class="boardPage_header">
				<div class="boardPage_header_back" @click="goBack"><i class="el-icon-arrow-left"></i> <span>返回</span></div>
			</div>
			<div class="boardPage_main">
				<div class="boardPage_main_row">
					<div class="sn-containerCard" style="width: 280px; height: 140px">
						<div class="sn-contentCard">
							<div class="sn-titleCard"><span>理财规模</span><img style="height: 22px" src="../../../assets/img/board/img-label.png" /></div>
							<div class="sn-bodyCard">
								<div>
									<span class="text1">{{ valueObj.num3 }}</span>
									<span class="text2">亿元</span>
									<div style="display: flex; align-items: center">
										<div v-if="valuePercentage.value3 > 0" class="icon-arrowUp"></div>
										<div v-else class="icon-arrowDown"></div>
										<span class="text5" style="margin: 10px">{{ valuePercentage.value3 }}%</span>
										<span class="text4">同比去年</span>
									</div>
								</div>
								<div>
									<img src="../../../assets/img/board/icon-b3.png" />
								</div>
							</div>
						</div>
					</div>
					<div class="sn-containerCard" style="width: 280px; height: 140px">
						<div class="sn-contentCard">
							<div class="sn-titleCard"><span>理财产品</span><img style="height: 22px" src="../../../assets/img/board/img-label.png" /></div>
							<div class="sn-bodyCard">
								<div>
									<span class="text3">{{ valueObj.num4 }}</span>
									<span class="text4">只</span>
									<div style="display: flex; align-items: center">
										<div v-if="valuePercentage.value4 > 0" class="icon-arrowUp"></div>
										<div v-else class="icon-arrowDown"></div>
										<span class="text5" style="margin: 10px">{{ valuePercentage.value4 }}只</span>
										<span class="text4">同比去年</span>
									</div>
								</div>
								<div>
									<img src="../../../assets/img/board/icon-b4.png" />
								</div>
							</div>
						</div>
					</div>
					<div class="sn-containerCard" style="width: 280px; height: 140px">
						<div class="sn-contentCard">
							<div class="sn-titleCard">
								<!-- <span>本年新增产品数</span><img style="height: 22px; margin-right: -18px" src="../../../assets/img/board/img-label.png" /> -->
								<span>本年成立产品</span><img style="height: 22px; margin-right: -18px" src="../../../assets/img/board/img-label.png" />
							</div>
							<div class="sn-bodyCard">
								<div>
									<span class="text1">{{ valueObj.num5 }}</span>
									<span class="text2">只</span>
									<div style="display: flex; align-items: center">
										<div v-if="valuePercentage.value5 > 0" class="icon-arrowUp"></div>
										<div v-else class="icon-arrowDown"></div>
										<span class="text5" style="margin: 10px">{{ valuePercentage.value5 }}只</span>
										<span class="text4">同比去年</span>
									</div>
								</div>
								<div>
									<img src="../../../assets/img/board/icon-b5.png" />
								</div>
							</div>
						</div>
					</div>

					<div class="sn-containerCard" style="width: 280px; height: 140px">
						<div class="sn-contentCard">
							<div class="sn-titleCard">
								<span>本年发行失败产品</span><img style="height: 22px; margin-right: -33px" src="../../../assets/img/board/img-label.png" />
							</div>
							<div class="sn-bodyCard">
								<div>
									<span class="text1">{{ valueObj.num1 }}</span>
									<span class="text2">只</span>
									<div style="display: flex; align-items: center">
										<div v-if="valuePercentage.value1 > 0" class="icon-arrowUp"></div>
										<div v-else class="icon-arrowDown"></div>
										<span class="text5" style="margin: 10px">{{ valuePercentage.value1 }}只</span>
										<span class="text4">同比去年</span>
									</div>
								</div>
								<div>
									<img src="../../../assets/img/board/icon-b1.png" />
								</div>
							</div>
						</div>
					</div>
					<div class="sn-containerCard" style="width: 280px; height: 140px">
						<div class="sn-contentCard">
							<div class="sn-titleCard"><span>报备失败产品</span><img style="height: 22px" src="../../../assets/img/board/img-label.png" /></div>
							<div class="sn-bodyCard">
								<div>
									<span class="text3">{{ valueObj.num2 }}</span>
									<span class="text4">只</span>
									<div style="display: flex; align-items: center">
										<div v-if="valuePercentage.value2 > 0" class="icon-arrowUp"></div>
										<div v-else class="icon-arrowDown"></div>
										<span class="text5" style="margin: 10px">{{ valuePercentage.value2 }}只</span>
										<span class="text4">同比去年</span>
									</div>
								</div>
								<div>
									<img src="../../../assets/img/board/icon-b2.png" />
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="boardPage_main_row">
					<cockpit-chart1 ref="cockpitChart1" id="cChart1" width="630px" height="385px"></cockpit-chart1>
					<cockpit-chart2 ref="cockpitChart2" id="cChart2" width="1000px" height="385px"></cockpit-chart2>
					<!-- <market-chart1 ref="marketChart1" id="mChart1" width="580px" height="300px"></market-chart1> -->

					<!-- <div style="width: 780px; height: 385px; background: red">1</div> -->
					<!-- <div style="width: 1000px; height: 385px; background: red">1</div> -->
				</div>
				<div class="boardPage_main_row">
					<cockpit-chart3 ref="cockpitChart3" id="cChart3" width="1000px" height="385px"></cockpit-chart3>
					<div class="sn-containerCard" style="width: 630px; height: 385px">
						<div class="sn-contentCard">
							<div class="sn-titleCard"><span>规模排名</span><img src="../../../assets/img/board/img-label.png" /></div>
							<div class="sn-table">
								<div class="table">
									<table border="0" cellpadding="0" cellspacing="0" class="table-header">
										<tbody>
											<tr>
												<td width="15%">
													<span>排名</span>
												</td>
												<td width="25%">
													<span>地区</span>
												</td>
												<td width="40%">
													<span>AUM(规模)</span>
												</td>
												<td width="20%">
													<span>环比（月）</span>
												</td>
											</tr>
										</tbody>
									</table>

									<table border="0" cellpadding="0" cellspacing="0" class="item" style="width: 100%">
										<tbody>
											<tr v-for="(item, index) in listData" :key="index">
												<td width="15%">
													<span v-if="index > 2">{{ index }}</span>
													<img v-else :src="require('../../../assets/img/board/icon-ranking' + index + '.png')" />
												</td>
												<td width="25%">
													<span>{{ item.area }}</span>
												</td>
												<td width="40%">
													<span>{{ item.aum }}亿元</span>
												</td>

												<td width="20%">
													<span>{{ item.ratio }}%</span>
												</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
import { screenSize } from "./chart/utils";
import cockpitChart1 from "./chart/cockpitChart1.vue";
import cockpitChart2 from "./chart/cockpitChart2.vue";
import cockpitChart3 from "./chart/cockpitChart3.vue";
import moment from "moment";
import Tools from "@/utils/tools";

export default {
	data() {
		return {
			valueObj: {
				num1: 0,
				num2: 0,
				num3: 0,
				num4: 0,
				num5: 0,
			},
			valuePercentage: {
				value1: 0,
				value2: 0,
				value3: 0,
				value4: 0,
				value5: 0,
			},

			listData: [],
		};
	},
	components: {
		cockpitChart1,
		cockpitChart2,
		cockpitChart3,
	},
	created() {
		this.initDate();
	},
	mounted() {
		screenSize(this.$refs.editor2);
	},
	methods: {
		goBack() {
			this.$router.push("main/desktop");
		},
		async initDate() {
			// 获取时间
			let nowYearFirstDay2;
			let nowDate;
			await this.httpUtil.sysDate().then((res) => {
				if (res) {
					nowDate = res;
					nowYearFirstDay2 = Tools.formatDate(res);
				}
			});
			nowYearFirstDay2 = moment(nowYearFirstDay2).format("YYYYMMDD"); // YYYYMMDD
			let nowYearFirstDay3 = moment(nowYearFirstDay2).format("YYYYMM"); // YYYYMM

			let nowYearFirstDay = nowDate.toString().slice(0, 4) + "0101"; // YYYY0101
			let lastYear = nowYearFirstDay2.slice(0, 4) - 1 + "0101"; // 去年0101
			// 本年成立产品
			await this.httpUtil
				.ajax({
					url: "server/form/PmsApp/direct/get_t8_prod_info_count.json",
					params: {
						startDate: nowYearFirstDay,
						// startDate: nowDate,
						endDate: nowDate.toString().slice(0, 4) + "1231",
					},
				})
				.then((res) => {
					if (res.success) {
						this.valueObj.num5 = res.rows[0].count;
					}
				});
			// 本年成立产品（同比去年）
			await this.httpUtil
				.ajax({
					url: "server/form/PmsApp/direct/get_t8_prod_info_count.json",
					params: {
						startDate: lastYear,
						// startDate: nowDate,
						endDate: lastYear.toString().slice(0, 4) + "1231",
					},
				})
				.then((res) => {
					if (res.success) {
						this.valuePercentage.value5 = res.rows[0].count == 0 ? 0 : this.valueObj.num5 - res.rows[0].count;
					}
				});

			// 理财产品
			await this.httpUtil
				.ajax({
					url: "server/form/PmsApp/direct/get_t8_prod_info_prod_code.json",
					params: {
						nowDate: nowDate,
						// endDate: nowDate.toString().slice(0, 4) + "1231",
					},
				})
				.then((res) => {
					if (res.success) {
						this.valueObj.num4 = res.rows[0].count;
					}
				});
			// 理财产品（同比去年）
			await this.httpUtil
				.ajax({
					url: "server/form/PmsApp/direct/get_t8_prod_info_prod_code.json",
					params: {
						nowDate: lastYear,
					},
				})
				.then((res) => {
					if (res.success) {
						this.valuePercentage.value4 = res.rows[0].count == 0 ? 0 : this.valueObj.num4 - res.rows[0].count;
					}
				});

			// 理财规模
			await this.httpUtil
				.ajax({
					url: "server/form/PmsApp/direct/get_sa_prd_valuation.json",
					params: {
						nowDate: nowDate,
					},
				})
				.then((res) => {
					if (res.success) {
						this.valueObj.num3 = res.rows[0].valuation;
					}
				});

			// 同比去年
			await this.httpUtil
				.ajax({
					url: "server/form/PmsApp/direct/get_sa_prd_vlt.json",
					params: {
						nowDate: nowDate,
					},
				})
				.then((res) => {
					if (res.success) {
						this.valuePercentage.value3 = res.rows[0].valuation ? res.rows[0].valuation : 0;
					}
				});

			// console.log(nowYearFirstDay2);
			// 本年发行失败产品   13
			await this.httpUtil
				.ajax({
					url: "server/form/PmsApp/direct/get_prod_son_status_count.json",
					params: {
						prodSonStatus: "13",
						startDate: nowYearFirstDay2,
						endDate: nowYearFirstDay2.toString().slice(0, 4) + "1231",
					},
				})
				.then((res) => {
					if (res.success) {
						this.valueObj.num1 = res.rows[0].count;
					}
				});
			// 本年发行失败产品(去年，算同比)   13

			// console.log("nowYearFirstDay2", nowYearFirstDay2, lastYear, nowYearFirstDay2.slice(0, 4) - 1);
			await this.httpUtil
				.ajax({
					url: "server/form/PmsApp/direct/get_prod_son_status_count.json",
					params: {
						prodSonStatus: "13",
						startDate: lastYear,
						endDate: lastYear.toString().slice(0, 4) + "1231",
					},
				})
				.then((res) => {
					if (res.success) {
						this.valuePercentage.value1 = res.rows[0].count == 0 ? 0 : this.valueObj.num1 - res.rows[0].count;
						// （今年 - 去年）/ 去年
						console.log("本年发行失败产品--同比", this.valuePercentage.value1);
					}
				});

			// 报备失败产品   9
			await this.httpUtil
				.ajax({
					url: "server/form/PmsApp/direct/get_prod_son_status_count.json",
					params: {
						prodSonStatus: "9",
						startDate: nowYearFirstDay2,
						endDate: nowYearFirstDay2.toString().slice(0, 4) + "1231",
					},
				})
				.then((res) => {
					if (res.success) {
						this.valueObj.num2 = res.rows[0].count;
					}
				});

			// 报备失败产品 (去年，算同比)  9
			await this.httpUtil
				.ajax({
					url: "server/form/PmsApp/direct/get_prod_son_status_count.json",
					params: {
						prodSonStatus: "9",
						startDate: lastYear,
						endDate: lastYear.toString().slice(0, 4) + "1231",
					},
				})
				.then((res) => {
					if (res.success) {
						this.valuePercentage.value2 = res.rows[0].count == 0 ? 0 : this.valueObj.num2 - res.rows[0].count; // （今年 - 去年）/ 去年
						// console.log("报备失败产品--同比", this.valuePercentage.value2);
					}
				});

			// 规模排名
			await this.httpUtil
				.ajax({
					url: "server/form/PmsApp/direct/get_area_list.json",
					params: {
						prodSonStatus: "9",
						startDate: nowYearFirstDay2,
						nowDate: nowDate,
					},
				})
				.then((res) => {
					if (res.success) {
						this.listData = res.rows;
					}
				});
			// 目标金额
			await this.httpUtil
				.ajax({
					url: "server/form/PmsApp/direct/get_t8_kpi_amount.json",
					params: {
						nowDate: nowDate,
					},
				})
				.then((res) => {
					if (res.success) {
						// this.listData = res.rows;
						console.log("目标金额: ", res);
						this.$nextTick(() => {
							// 需提供数据格式
							this.$refs.cockpitChart1.setMoney(res.rows[0].targetAmount);
						});
					}
				});
			// 中收/管理费
			await this.httpUtil
				.ajax({
					url: "server/form/PmsApp/direct/get_accrual.json",
					params: {
						endDate: nowDate.toString().slice(0, 6),
						// endDate: '202005'
					},
				})
				.then((res) => {
					if (res.success) {
						// this.listData = res.rows;
						console.log("中收管理费: ", res);
						this.$nextTick(() => {
							// 需提供数据格式
							this.$refs.cockpitChart1.getEchart(res);
						});
					}
				});

			// 保有量前五增量规模变化
			let preNowDate = Tools.getPreDate(Tools.formatDate(nowDate), 5).toString(); // 前6个月
			console.log("前6个月", preNowDate, Tools.formatDate(nowDate));
			await this.httpUtil
				.ajax({
					url: "server/form/PmsApp/direct/get_area_analyse_city.json",
					params: {
						// startDate: nowYearFirstDay3
						startDate: preNowDate.toString().slice(0, 6),
					},
				})
				.then((res) => {
					if (res.success) {
						// this.listData = res.rows;
						console.log("保有量前五增量规模变化: ", res);
						this.$nextTick(() => {
							// 需提供数据格式
							this.$refs.cockpitChart3.getEchart(res);
						});
					}
				});

			// 投资收益
			await this.httpUtil
				.ajax({
					url: "server/form/PmsApp/direct/get_profitloss.json",
					params: {
						startDate: preNowDate.toString().slice(0, 6),
						endDate: nowYearFirstDay3,
					},
				})
				.then((res) => {
					if (res.success) {
						// this.listData = res.rows;
						this.$nextTick(() => {
							// 需提供数据格式
							this.$refs.cockpitChart2.getEchart(res);
						});
					}
				});
		},
	},
};
</script>

<style lang="scss" scoped>
.boardPage {
	position: absolute;
	left: 0;
	top: 0;
	width: 100%;
	height: 100%;
	overflow: hidden;

	.wrap2 {
		transform-origin: 0px 0px 0px;
		background-image: url("../../../assets/img/board/img-bgCenter.png"), url("../../../assets/img/board/img-bgBottom.png");
		background-repeat: no-repeat no-repeat;
		background-size: 80%, 100%;
		background-position-x: center;
		background-position-y: 40%, 100%;
		background-color: #00013b;
		min-width: auto;
		width: 1920px;
		min-height: auto;
		height: 1080px;
		overflow: auto;
	}

	&_header {
		width: 100%;
		height: 100px;
		background: url("../../../assets/img/board/img-header1.png") no-repeat;
		background-color: transparent;
		background-position: 65% 0;
		border: none;
		overflow: hidden;

		&_back {
			display: inline-block;
			color: #3c7bff;
			font-size: 16px;
			font-weight: 600;
			margin-left: 44px;
			margin-top: 46px;
			cursor: pointer;

			.el-icon-arrow-left {
				font-size: 18px;
				font-weight: 600;
			}
		}
	}

	&_main {
		margin-top: 60px;
		margin-bottom: 60px;

		&_row {
			display: flex;
			justify-content: space-around;
			align-items: center;
			flex-wrap: nowrap;
			margin-top: 40px;
		}
	}
}

.sn-containerCard {
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

	.sn-contentCard {
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

	.sn-titleCard {
		font-size: 17px;
		color: #fff;
		font-weight: 600;
		margin: 8px 0 0 16px;
		display: flex;
		justify-content: flex-start;
		align-items: center;
	}

	.sn-bodyCard {
		display: flex;
		justify-content: space-around;
		align-items: center;
		margin-top: 20px;

		.text1 {
			color: #ff9738;
			font-size: 24px;
			font-weight: 600;
		}

		.text2 {
			color: #ff9738;
			font-size: 14px;
			font-weight: 600;
		}

		.text3 {
			color: #00a2ff;
			font-size: 24px;
			font-weight: 600;
		}

		.text4 {
			color: #00a2ff;
			font-size: 14px;
			font-weight: 600;
		}

		.text5 {
			color: #fe0000;
			font-size: 14px;
		}

		.text6 {
			color: #00ff24;
			font-size: 14px;
		}

		.icon-arrowUp {
			width: 0;
			height: 0;
			border: 5px solid transparent;
			border-bottom-color: #fe0000;
			margin-bottom: 6px;
		}

		.icon-arrowDown {
			width: 0;
			height: 0;
			border: 5px solid transparent;
			border-top-color: #00ff24;
			margin-top: 6px;
		}
	}

	%table-style {
		width: 100%;
		height: 35px;
		table-layout: fixed;

		tr {
			td {
				padding: 10px 5px;
				text-align: center;
				background-color: transparent;
				white-space: nowrap;
				overflow: hidden;
				color: #e2e5ff;
				font-size: 14px;
			}
		}
	}

	.table {
		width: 100%;
		text-align: center;
		line-height: 52px;
		color: #dddddd;
		font-size: 16px;
		font-weight: 600;

		.table-header {
			@extend %table-style;
		}

		.seamless-warp {
			height: 400px;
			overflow: hidden;
			visibility: visible;

			.colorRed {
				color: #ff4669;
			}

			.colorOrange {
				color: #ffc956;
			}

			.item {
				height: auto;
				@extend %table-style;

				tr {
					td {
						&.title {
							text-overflow: ellipsis;
							display: inline-block;
						}
					}
				}
			}
		}
	}
}
</style>
