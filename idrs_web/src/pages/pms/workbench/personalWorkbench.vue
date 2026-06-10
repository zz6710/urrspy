<template>
	<div class="workbench py-page">
		<div class="py-page-container">
			<el-tabs v-model="activeName" @tab-click="handleClick">
				<el-tab-pane name="1" v-if="global.isShowAuthorityButton('DeskTopModel.findReportResultInfo')">
					<span slot="label"
						><i class="el-icon-alarm-clock"></i>
						报送提醒
					</span>
					<span slot="label">
						<el-badge class="mark" v-if="myInfoNum" :value="myInfoNum" />
					</span>
					<transition name="el-fade-in-linear">
						<my-info :tabAuthorityInfo="tabAuthorityInfo" ref="myInfoRef" @refreshDesktop="refreshMyDesktop" @handleUpdateCount="handleUpdateCount" @changeTab="changeTab"></my-info>
					</transition>
				</el-tab-pane>
				<!-- <el-tab-pane name="2" v-if="global.isShowAuthorityButton('DataCheckModel.findIndicatorCheckRemind')">
					<span slot="label"><i class="el-icon-menu"></i>
						指标校验
					</span>
					<span slot="label">
							<el-badge class="mark" v-if="myIndicatorCheckNum" :value="myIndicatorCheckNum" />
					</span>
					<transition name="el-fade-in-linear">
						<my-indicator-check v-if="activeName == '2'"></my-indicator-check>
					</transition>
				</el-tab-pane>
				<el-tab-pane name="8" v-if="global.isShowAuthorityButton('DataCheckModel.findSubmitRemind')">
					<span slot="label"><i class="el-icon-menu"></i>
						数据补录
					</span>
					<span slot="label">
							<el-badge class="mark" v-if="myDataNum" :value="myDataNum" />
						</span>
					<transition name="el-fade-in-linear">
						<my-data-record v-if="activeName == '8'"></my-data-record>
					</transition>
				</el-tab-pane>
				<el-tab-pane name="3" v-if="global.isShowAuthorityButton('SourceDataChgInfoModel.findSourceDataChgInfoModelForOne')">
					<span slot="label"><i class="el-icon-date"></i>
						产品源数据变化
					</span>
					<span slot="label">
							<el-badge class="mark" v-if="mySourceNum" :value="mySourceNum" />
					</span>
					<transition name="el-fade-in-linear">
						<my-source v-if="activeName == '3'"></my-source>
					</transition>
				</el-tab-pane>
				<el-tab-pane name="4" v-if="global.isShowAuthorityButton('SourceDataChgInfoModel.findAssetSourceDataChgInfoModelForOne')">
					<span slot="label"><i class="el-icon-alarm-clock"></i>
						资产源数据变化
					</span>
					<span slot="label">
							<el-badge class="mark" v-if="myAssetSourceNum" :value="myAssetSourceNum" />
					</span>
					<transition name="el-fade-in-linear">
						<my-asset-source v-if="activeName == '4'"></my-asset-source>
					</transition>
				</el-tab-pane> -->
				<el-tab-pane name="7">
					<span slot="label"
						><i class="el-icon-menu"></i>
						流程审批
					</span>
					<span slot="label">
						<el-badge class="mark" v-if="myTodoTaskNum" :value="myTodoTaskNum" />
					</span>
					<transition name="el-fade-in-linear">
						<my-to-do-task v-if="activeName == 7" @refreshNum="refresh"></my-to-do-task>
					</transition>
				</el-tab-pane>
				<!-- <el-tab-pane name="0" v-if="global.isShowAuthorityButton('DeskTopModel.findPortDeskTopInformation')">
					<span slot="label">
						<i class="el-icon-date"></i>
						接口接入情况
					</span>
					<span slot="label">
							<el-badge class="mark" v-if="myFlowNum" :value="myFlowNum" />
					</span>
					<transition name="el-fade-in-linear">
						<my-flow v-if="activeName == '0'" @refreshDesktop="refreshMyDesktop"></my-flow>
					</transition>
				</el-tab-pane> -->

				<!-- <el-tab-pane name="4" disabled v-if="isShow4">
					<span slot="label" @click="openDialog('/marketBoard')"><i class="el-icon-data-line"></i> 市场看板</span>
					<transition name="el-fade-in-linear">
						<market-board v-if="activeName == '4'"></market-board>
					</transition>
				</el-tab-pane> -->
				<!--						<el-tab-pane name="5" disabled>-->
				<!--							<span slot="label" @click="openDialog('/main/cockpit')"><i class="el-icon-odometer"></i> 高管驾驶舱</span>-->
				<!--							<transition name="el-fade-in-linear">-->
				<!--								&lt;!&ndash; <executive-cockpit v-if="activeName == '5'"></executive-cockpit> &ndash;&gt;-->
				<!--							</transition>-->
				<!--						</el-tab-pane>-->
			</el-tabs>
		</div>
	</div>
</template>

<script>
import myFlow from "./components/myFlow";
import myProduct from "./components/myProduct";
import myRemind from "./components/myRemind";
import myInfo from "./components/myInfo";
import mySource from "./components/mySource";
import newValue from "./components/newValue";
// import marketBoard from "./components/marketBoard";
// import executiveCockpit from "./components/executiveCockpit";
import todoReminder from "./components/todoReminder";
import { assign } from "lodash";
import Bus from "@/frame/bus";
import global from "@/frame/global";

import auth from "@/utils/auth.js";
import { re } from "semver";
import myToDoTask from "@/pages/pms/workbench/components/myToDoTask.vue";
import MyAssetSource from "@/pages/pms/workbench/components/myAssetSource";
import myDataRecord from "@/pages/pms/workbench/components/myDataRecord.vue";
import myIndicatorCheck from "@/pages/pms/workbench/components/myIndicatorCheck.vue";

export default {
	data() {
		return {
			myInfoNum: 0,
			myDataNum: 0,
			myFlowNum: 0,
			myTodoTaskNum: 0,
			surrogateTaskNum: 0,
			mySourceNum: 0,
			myAssetSourceNum: 0,
			myIndicatorCheckNum: 0,
			activeName: "1",
			searchValue: "",
			homeTaskTableData: [],
			expireTaskTableData: {},
			isShow: false,
			menuArr: [],
			menuArrLen: 0,
			tabAuthorityInfo: {
				myProcess: {},
				myMessage: {},
				myAgent: {},
				disAgent: {},
				prodAgent: {},
				myLiquidation: {}, //清盘待办
			},
			pagekey: 0,
			tipDate: ''
		};
	},
	components: {
		MyAssetSource,
		myDataRecord,
		myIndicatorCheck,
		myToDoTask,
		myFlow,
		myProduct,
		myRemind,
		myInfo,
		newValue,
		mySource,
		// marketBoard,
		// executiveCockpit,
		todoReminder,
	},
	created() {
		this.setLocalData(this.$route.query);
		this.activeName = this.$route.query.activeName;
		//未来重大提醒事件
		/*this.httpUtil
      .comnQuery({
        action: "DesktopIndex.findDesktopIndexList",
        params: {},
      })
      .then((data) => {
        if (data.results > 0) {
          data.rows.length > 0 &&
            data.rows.forEach((item) => {
              if (item.taskDesc.includes("到期")) {
                this.expireTaskTableData = item;
                this.expireTaskTableData.len = this.expireTaskTableData.prodCode.split(",").length;
              } else if (item.taskDesc.includes("登记")) {
                item.type = "0";
                item.typeName = "登记";
                this.homeTaskTableData.push(item);
              } else if (item.taskDesc.includes("定价")) {
                item.type = "1";
                item.typeName = "定价";
                this.homeTaskTableData.push(item);
              } else if (item.taskDesc.includes("协议")) {
                item.type = "2";
                item.typeName = "协议";
                this.homeTaskTableData.push(item);
              }
            });
          // this.homeTaskTableData = data.rows;
          console.log(this.homeTaskTableData);
        }
      });*/

		this.init();
		if (global.isShowAuthorityButton("DeskTopModel.findReportResultInfo")) {
			this.activeName = "1";
		} else {
			this.activeName = "7";
		}
		// if(global.isShowAuthorityButton('DeskTopModel.findPortDeskTopInformation')){
		//   this.activeName ="0";
		// }else if (!global.isShowAuthorityButton('DeskTopModel.findPortDeskTopInformation')) {
		//   if (global.isShowAuthorityButton('DeskTopModel.findDisclosureNDetails')) {
		//     this.activeName = "6";
		//   } else if (!global.isShowAuthorityButton('DeskTopModel.findDisclosureNDetails')) {
		//     if (global.isShowAuthorityButton('DeskTopModel.findReportResultInfo')) {
		//       this.activeName = "1";
		//     } else {
		//       this.activeName = "2";
		//     }
		//   }
		// }else if(global.isShowAuthorityButton('SourceDataChgInfoModel.findSourceDataChgInfoModelForOne')){
		//   this.activeName = "3";
		// }
	},
	filters: {
		filtersData: (val) => {
			if (typeof val == "undefined") {
				return 0;
			}
			return val ? val : 0;
		},
		filterAuth: (val) => {
			return val > 99 ? "99+" : val;
		},
	},
	// created() {
	// 	this.activeName = this.$route.query.activeName;
	//未来重大提醒事件
	/*this.httpUtil
			.comnQuery({
				action: "DesktopIndex.findDesktopIndexList",
				params: {},
			})
			.then((data) => {
				if (data.results > 0) {
					data.rows.length > 0 &&
						data.rows.forEach((item) => {
							if (item.taskDesc.includes("到期")) {
								this.expireTaskTableData = item;
								this.expireTaskTableData.len = this.expireTaskTableData.prodCode.split(",").length;
							} else if (item.taskDesc.includes("登记")) {
								item.type = "0";
								item.typeName = "登记";
								this.homeTaskTableData.push(item);
							} else if (item.taskDesc.includes("定价")) {
								item.type = "1";
								item.typeName = "定价";
								this.homeTaskTableData.push(item);
							} else if (item.taskDesc.includes("协议")) {
								item.type = "2";
								item.typeName = "协议";
								this.homeTaskTableData.push(item);
							}
						});
					// this.homeTaskTableData = data.rows;
					console.log(this.homeTaskTableData);
				}
			});*/

	// 	this.init();
	// },
	activated() {
		// this.activeName = '1';
		//未来重大提醒事件
		/*this.httpUtil
      .comnQuery({
        action: "DesktopIndex.findDesktopIndexList",
        params: {},
      })
      .then((data) => {
        if (data.results > 0) {
          data.rows.length > 0 &&
          data.rows.forEach((item) => {
            if (item.taskDesc.includes("到期")) {
              this.expireTaskTableData = item;
              this.expireTaskTableData.len = this.expireTaskTableData.prodCode.split(",").length;
            } else if (item.taskDesc.includes("登记")) {
              item.type = "0";
              item.typeName = "登记";
              this.homeTaskTableData.push(item);
            } else if (item.taskDesc.includes("定价")) {
              item.type = "1";
              item.typeName = "定价";
              this.homeTaskTableData.push(item);
            } else if (item.taskDesc.includes("协议")) {
              item.type = "2";
              item.typeName = "协议";
              this.homeTaskTableData.push(item);
            }
          });
          console.log(this.homeTaskTableData);
        }
      });*/

		this.init();
	},
	mounted() {
		Bus.$on("updateMenuList", () => {
			this.getCommonMenu();
		});
	},
	activated() {
		this.$nextTick(() => {
			 this.getCount()
		});
	},
	methods: {
	  changeTab() {
	    this.getCount()
	  },
	  getCount() {
	    if (this.$refs.myInfoRef.searchParam.theoryReportStartDate) {
        this.handleUpdateCount(this.$refs.myInfoRef.searchParam.theoryReportStartDate);
      }
	  },
		setLocalData(query) {
			console.log(this.$route, query, "query");
			if (query.username) {
				localStorage.setItem("username", query.username);
			}
			if (query.userid) {
				localStorage.setItem("userid", query.userid);
			}
			if (query.token) {
				localStorage.setItem("token", query.token);
				auth.setToken(query.token);
			}
			if (query.roleids) {
				localStorage.setItem("roleids", query.roleids);
			}
		},
		refresh() {
			this.init();
		},
		handleUpdateCount(v) {
			this.tipDate = v
			this.httpUtil
				.comnQuery({
					action: "DeskTopModel.findReportResultInfoNum",
					params: {
						theoryReportStartDate: v,
						checkType: 2,
					},
				})
				.then((data) => {
					this.myInfoNum = data.rows.length;
				});
		},
		init() {
			//查询-常用菜单配置
			//this.getCommonMenu();
			//this.getTabAuthority()

			// 【报送提醒】统计今日待报送，延期待报送的数据汇总
			// this.httpUtil.sysDate().then((res) => {
			// 	if (res) {
			// 		this.httpUtil
			// 			.comnQuery({
			// 				action: "DeskTopModel.findReportResultInfoNum",
			// 				params: {
			// 					theoryReportStartDate: res.toString(),
			// 					checkType: 2,
			// 				},
			// 			})
			// 			.then((data) => {
			// 				this.myInfoNum = data.rows.length;
			// 			});
			// 	}
			// }),
				// 【接口接入情况】统计今日待报送，延期待报送的数据汇总-----无日期
				// this.httpUtil.sysDate().then((res) => {
				// 	if (res) {
				// 		this.httpUtil
				// 			.comnQuery({
				// 				action: "DeskTopModel.findPortDeskTopErrInformation",
				// 				params: {
				// 					// dealDate: res.toString()
				// 				},
				// 			})
				// 			.then((data) => {
				// 				this.myFlowNum = data.rows.length;
				// 			});
				// 	}
				// }),
				// 【产品类型源数据变化情况】统计今日待报送，延期待报送的数据汇总----无日期
				// this.httpUtil.sysDate().then((res) => {
				// 	if (res) {
				// 		this.httpUtil
				// 			.comnQuery({
				// 				action: "SourceDataChgInfoModel.findSourceDataChgInfoModelForUnconfirmed",
				// 				params: {
				// 					// dealDate: res.toString()
				// 				},
				// 			})
				// 			.then((data) => {
				// 				this.mySourceNum = data.rows.length;
				// 			});
				// 	}
				// }),
				// 【资产类型源数据变化情况】统计今日待报送，延期待报送的数据汇总----无日期
				// this.httpUtil.sysDate().then((res) => {
				// 	if (res) {
				// 		this.httpUtil
				// 			.comnQuery({
				// 				action: "SourceDataChgInfoModel.findAssetSourceDataChgInfoModelForUnconfirmed",
				// 				params: {
				// 					dealDate: res.toString(),
				// 				},
				// 			})
				// 			.then((data) => {
				// 				this.myAssetSourceNum = data.rows.length;
				// 			});
				// 	}
				// }),
				// 【数据补录】统计（当前系统工作日）=（持仓日期）待补录的所有记录
				// this.httpUtil.sysDate().then((res) => {
				// 	if (res) {
				// 		this.httpUtil
				// 			.comnQuery({
				// 				action: "DeskTopModel.findReportResultInfoNum",
				// 				params: {
				// 					dealDate: res.toString(),
				// 					checkType: 2,
				// 				},
				// 			})
				// 			.then((data) => {
				// 				this.myDataNum = data.rows.length;
				// 			});
				// 	}
				// }),
				// 【指标校验】统计所有校验结果为：【不通过、校验预期】的指标记录----无日期
				// this.httpUtil.sysDate().then((res) => {
				// 	if (res) {
				// 		this.httpUtil
				// 			.comnQuery({
				// 				action: "DataCheckModel.findIndicatorCheck",
				// 				params: {
				// 					createDate: res.toString(),
				// 				},
				// 			})
				// 			.then((data) => {
				// 				this.myIndicatorCheckNum = data.rows.length;
				// 			});
				// 	}
				// }),
				this.findWorkFlowData();
		},
		//查询待审批数据
		findWorkFlowData() {
			this.httpUtil
				.ajaxJson({
					url: "wf/process/todoList.json",
					params: {},
				})
				.then((data) => {
					//console.log(data);
					if (data.rows.length > 0) {
						// this.processTableData = data.rows;
						for (let i = 0; i < data.rows.length; i++) {
							data.rows[i].status = "待审批";
						}
						this.myTodoTaskNum = data.rows.length;
					} else {
						this.myTodoTaskNum = data.rows.length;
					}
				});
		},
		getTabAuthority() {
			this.httpUtil
				.comnQuery({
					action: "DirectMode.findIndexMessage",
				})
				.then((data) => {
					this.tabAuthorityInfo = data.rows[0].messageInfo[0];
				});
		},
		getCommonMenu() {
			this.httpUtil
				.comnQuery({
					action: "CommonMenu.page",
				})
				.then((data) => {
					// console.log(data);
					this.menuArr = data.rows;
					this.menuArrLen = data.results;
				});
		},
		openDelete() {
			this.isShow = this.isShow ? false : true;
		},
		closeDelete() {
			this.isShow = false;
		},
		deleteMenu(menuid) {
			this.httpUtil
				.comnQuery({
					action: "CommonMenu.delete",
					params: { menuid: menuid },
				})
				.then((data) => {
					// console.log(data);
					this.$message.success("操作成功");
					this.getCommonMenu();
					this.isShow = false;

					// let rows = data.rows;
					// this.handleData(rows);
				});
		},
		jupUrl(url) {
			this.$router.push({ path: url });
		},
		openDialog(url) {
			// let editUrl = this.$router.resolve({ path: url });
			// window.open(editUrl.href, "_blank");
			this.$router.push({
				path: url,
			});
		},
		handleClick(tab, event) {},
		homeTaskCheckPage(data) {
			this.$router.push({
				path: data.url,
				query: { prodCode: data.prodCode },
			});
		},
		//首页刷新
		refreshMyDesktop() {
			this.getTabAuthority();
		},
	},
};
</script>

<style lang="scss" scoped>
.workbench .el-tabs .el-tabs__item.is-disabled {
	color: #303133;
	cursor: pointer;
}
.workbench .el-tabs .el-tabs__item.is-active {
	color: #00bcd4;
}
.workbench .el-tabs .el-tabs__active-bar {
	background: #00bcd4;
}
.workbench .el-tabs .el-tabs__item:hover {
	color: #00bcd4;
}
.workbench {
	.el-main {
		padding: 0;
		margin-right: 40px;
	}

	.card_block {
		display: flex;
		align-items: center;
		justify-content: center;
		padding: 14px 35px 14px 15px;
		box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
		background: #fff;
		border-radius: 4px;
		.el-input {
			.el-input__inner {
				border: none;
				font-size: 16px;
			}
		}
	}
	.el-icon-search {
		color: #00bcd4;
		font-size: 23px;
		// font-weight: 600;
		cursor: pointer;
	}
	.card_block2 {
		margin-bottom: 30px;
		&_header {
			display: flex;
			align-items: center;
			font-size: 16px;
			color: #303133;
			&::before {
				content: "";
				display: inline-block;
				width: 6px;
				height: 20px;
				background-color: #00bcd4;
				margin-right: 8px;
				border-radius: 8px;
			}
			&_flag {
				background: #f48824;
				height: 18px;
				line-height: 18px;
				padding: 0 5px;
				margin-left: 20px;
				position: relative;
				color: #fff;
				font-size: 12px;

				&::before {
					content: "";
					display: inline-block;
					border: 9px solid transparent;
					border-right-color: #f48824;
					position: absolute;
					left: -18px;
				}
			}
		}
		&_body1 {
			display: flex;
			// justify-content: space-between;
			// align-items: center;
			flex-wrap: wrap;
			height: 260px;
			overflow: auto;
			.el-icon-circle-close {
				position: absolute;
				top: -4px;
				right: 12px;
				font-size: 16px;
			}
			.blockItem {
				position: relative;
				font-size: 14px;
				color: #00bcd4;
				// margin-right: 20px;
				margin-top: 20px;
				cursor: pointer;
				.blockItem_warp {
					width: 76px;
					border: 1px solid #00bcd4;
					border-radius: 50%;
					background: #f1fbfd;
					padding: 22px;
					// margin-bottom: 20px;
					text-align: center;
					position: relative;
					margin: 0 auto 10px;
				}
				.blockItem_warp2 {
					width: 76px;
					border: 1px solid #999;
					border-radius: 50%;
					background: #f9f9f9;
					padding: 22px;
					margin: 0 auto 20px;
					text-align: center;
					.el-icon-more {
						color: #999;
						font-size: 30px;
					}
				}
				.md-icon {
					font-size: 30px !important;
					width: 30px !important;
					height: 30px !important;
					svg {
						fill: #00bcd4;
					}
					path {
						fill: unset !important;
					}
				}
			}
			.blockItem:nth-child(3n) {
				margin-right: 0;
			}
		}
		.hoverColor {
			cursor: pointer;
			&:hover {
				color: #409eff;
			}
		}
		&_list {
			max-height: 452px;
			margin-top: 20px;
			overflow: auto;
			&_item {
				border-bottom: 1px solid #eee;
				padding: 15px 0;
				cursor: pointer;
			}
			.item_line1 {
				display: flex;
				justify-content: flex-start;
				align-items: center;
				font-size: 16px;
			}
			.item_line2 {
				font-size: 14px;
				margin-left: 10px;
				margin-top: 10px;
				transition: 0.2s color;
				&:hover {
					color: #409eff;
				}
			}
			.item_2 {
				display: inline-block;
				line-height: 20px;
				color: #fc6d75;
				background: #f9ecf4;
				padding: 3px 10px;
				font-size: 12px;
				border-radius: 2px;
				margin-right: 10px;
			}
			.item_1 {
				display: inline-block;
				line-height: 20px;
				color: #7592f7;
				background: #e7ebff;
				padding: 3px 10px;
				font-size: 12px;
				border-radius: 2px;
				margin-right: 10px;
			}
			.item_0 {
				display: inline-block;
				line-height: 20px;
				color: #e1b61f;
				background: #fdf8df;
				padding: 3px 10px;
				font-size: 12px;
				border-radius: 2px;
				margin-right: 10px;
			}
		}
	}

	.el-tabs__nav {
		.el-tabs__item {
			padding: 0 !important;
			width: 140px;
			text-align: center;
			height: 55px;
			line-height: 55px;
			&.is-active {
				background: rgba(0, 0, 0, 0.03);
			}
			span {
				position: relative;
			}
		}
		.el-tabs__active-bar {
			width: 140px !important;
		}
	}

	.count {
		position: absolute;
		top: -5px;
		left: 99%;
		background: #ff9e00;
		border-radius: 10px;
		color: #fff;
		padding: 0 4px;
		height: 14px;
		line-height: 14px;
		text-align: center;
		font-weight: normal;
		font-size: 12px;
	}

	.el-card__body {
		padding-top: 0 !important;
		position: relative;
	}
	.refresh-img {
		width: 20px;
		position: absolute;
		top: 17px;
		right: 20px;
		z-index: 10;
		cursor: pointer;
	}
}
</style>
