<template>
	<div class="boardPage">
		<div class="wrap" ref="editor">
			<div class="boardPage_header">
				<div class="boardPage_header_back" @click="goBack"><i class="el-icon-arrow-left"></i> <span>返回</span></div>
			</div>
			<div class="boardPage_main">
				<div class="boardPage_main_row">
					<market-chart1 ref="marketChart1" id="mChart1" width="580px" height="300px">
						<SwitchMark
							data-action="DirectMode.findDirectModes"
							data-export-name="客户净申购规则变动"
						/>
					</market-chart1>
					<market-chart2 ref="marketChart2" id="mChart2" width="580px" height="300px">
						<SwitchMark />
					</market-chart2>
					<market-chart3 ref="marketChart3" id="mChart3" width="580px" height="300px">
						<SwitchMark />
					</market-chart3>
				</div>
				<div class="boardPage_main_row">
					<market-chart4 ref="marketChart4" id="mChart4" width="780px" height="385px">
						<SwitchMark />
					</market-chart4>
					<market-chart5 ref="marketChart5" id="mChart5" width="1000px" height="385px">
						<SwitchMark />
					</market-chart5>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
import marketChart1 from "./chart/marketChart1.vue";
import marketChart2 from "./chart/marketChart2.vue";
import marketChart3 from "./chart/marketChart3.vue";
import marketChart4 from "./chart/marketChart4.vue";
import marketChart5 from "./chart/marketChart5.vue";
import { screenSize } from "./chart/utils";
import moment from "moment";
import Tools from "@/utils/tools.js";
import SwitchMark from '@/pages/pms/workbench/chart/switchMark.vue';

export default {
	data() {
		return {};
	},
	components: {
		marketChart1,
		marketChart2,
		marketChart3,
		marketChart4,
		marketChart5,
		SwitchMark
	},
	created() {
		this.initDate();
	},
	mounted() {
		screenSize(this.$refs.editor);
	},
	methods: {
		goBack() {
			this.$router.push("main/desktop");
		},
		async initDate() {
			// 系统日期
			let currentWorkday;
			let startT;
			let endT;
      let nowDate;
			await this.httpUtil.sysDate().then((res) => {
				if (res) {
					endT = res.slice(0, 6);
          nowDate = res
					currentWorkday = Tools.formatDate(res);
				}
			});
			startT = moment(new Date(currentWorkday).getTime() - 5 * 30 * 24 * 60 * 60 * 1000).format("YYYYMM"); // 起始日期
			// 客户净申购规模变动
			await this.httpUtil
				.ajax({
					url: "server/form/PmsApp/direct/analyse_by_cust_type.json",
					params: {
						startDate: startT,
						endDate: endT,
					},
				})
				.then((res) => {
					this.$nextTick(() => {
						this.$refs.marketChart1.initChart(res);
					});
				});

			await this.httpUtil
				.ajax({
					url: "server/form/PmsApp/direct/analyse_by_cust_type_list.json",
					params: {
						startDate: startT,
						endDate: endT,
					},
				})
				.then((res) => {
					this.$nextTick(() => {
						this.$refs.marketChart1.initTable(res, {startDate: startT, endDate: endT});
					});
				});

			// 销售目标达成情况
			await this.httpUtil
				.ajax({
					url: "server/form/PmsApp/direct/get_kpi.json",
          params: {endDate: nowDate},
				})
				.then((res) => {
					this.$nextTick(() => {
						this.$refs.marketChart2.initChart(res);
					});
				});

			this.$nextTick(() => {
				this.$refs.marketChart3.initData(currentWorkday); // 净申购（亿元）
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
	.wrap {
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
		background: url("../../../assets/img/board/img-header2.png") no-repeat;
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
		margin-top: 70px;
		&_row {
			display: flex;
			justify-content: space-around;
			align-items: center;
			flex-wrap: nowrap;
			margin-top: 40px;
		}
	}
}
</style>
