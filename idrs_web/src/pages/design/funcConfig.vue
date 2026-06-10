<template>
	<div class="funcPage">
		<div class="funcPage_header">
			<el-breadcrumb separator-class="el-icon-arrow-right">
				<el-breadcrumb-item :to="{ path: '/main/design/lowcodeConfig' }">模块管理</el-breadcrumb-item>
				<el-breadcrumb-item>功能管理</el-breadcrumb-item>
			</el-breadcrumb>
			<el-tooltip effect="dark" content="刷新" placement="top">
				<el-button icon="el-icon-refresh" circle @click="refreshFunc"></el-button>
			</el-tooltip>
		</div>
		<div class="funcPage_body">
			<div class="funcPage_body_item funcPage_body_add" @click="dialogVisible = true">
				<i class="el-icon-plus"></i>
			</div>
			<div class="funcPage_body_item" v-for="(item, index) in funcLists" :key="item.id">
				<div class="funcPage_body_item_head">
					<div>
						<el-tag>
							{{ item.status | filterStatus }}
						</el-tag>
						<el-tag v-if="item.template == '1'" type="warning"> 模板 </el-tag>
					</div>

					<el-dropdown v-if="item.version" trigger="click" @command="historyVersion">
						<el-tooltip effect="dark" content="历史版本" placement="top">
							<i class="el-icon-more"></i>
						</el-tooltip>
						<el-dropdown-menu slot="dropdown">
							<el-dropdown-item :command="[item.id, item.version - index + 1]" v-for="index of item.version" :key="index">{{
								item.version - index + 1
							}}</el-dropdown-item>
						</el-dropdown-menu>
					</el-dropdown>
				</div>
				<div class="funcPage_body_item_con">
					<div class="con_text1">{{ item.name }}</div>
					<div class="con_text2" @click="copy(index)">
						ID:<span ref="idRefs">{{ item.id }}</span>
					</div>
				</div>
				<div class="funcPage_body_item_bottom">
					<el-tooltip effect="dark" content="编辑" placement="bottom">
					  <i class="el-icon-edit btn_icon" @click="editFunc(item)"></i>
					</el-tooltip>
					<el-divider direction="vertical"></el-divider>
					<el-tooltip effect="dark" content="发布" placement="bottom">
						<i class="el-icon-position btn_icon" @click="publishFunc(item)"></i>
					</el-tooltip>
					<el-divider direction="vertical"></el-divider>
					<el-tooltip effect="dark" content="设置为模板" placement="bottom">
						<i class="el-icon-open btn_icon" @click="moudleFunc(item)"></i>
					</el-tooltip>
					<el-divider direction="vertical"></el-divider>
					<el-tooltip effect="dark" content="删除" placement="bottom">
					  <i class="el-icon-delete btn_icon" @click="deleteFunc(item)"></i>
					</el-tooltip>

				</div>
			</div>
		</div>
		<el-dialog title="功能" :visible.sync="dialogVisible" width="380px" destroy-on-close>
			<el-form label-width="80px">
				<el-form-item label="功能名称">
					<el-input v-model="funcObj.name"></el-input>
				</el-form-item>
			</el-form>
			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogVisible = false">取 消</el-button>
				<el-button type="primary" @click="newFunc">确 定</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
import { clone, assign } from "lodash";
const lowcodeConfigStatus = ["新增未发布", "修改未发布", "已发布"];
export default {
	data() {
		return {
			sysVersion: "",
			moduleId: "",
			funcLists: [],
			dialogVisible: false,
			dialogVisibleMoudle: false,
			funcObj: {
				name: "",
			},
		};
	},
	filters: {
		filterStatus: (value) => lowcodeConfigStatus[Number(value)],
	},
	beforeRouteEnter(to, from, next) {
		next((vm) => {
			const { sysVersion, moduleId } = vm.$route.query;
			vm.sysVersion = sysVersion;
			vm.moduleId = moduleId;
			vm.initData();
		});
	},
	methods: {
		async initData() {
      // let loading = this.$loading({
      //   lock: true,
      //   text: '努力加载中...',
      //   spinner: 'el-icon-loading',
      //   background: 'rgba(0, 0, 0, 0.3)'
      // });
			await this.getFuncList();
      // loading.close();
		},
		async refreshFunc() {
			await this.getFuncList();
			this.$message.success("刷新成功");
		},
		async getFuncList() {
			let res = await this.httpUtil.comnQuery({
				action: "LowCodeConfig.page",
				params: {
					sysVersion: this.sysVersion,
					moduleId: this.moduleId,
				},
			});
			this.funcLists = clone(res.rows);
		},
		newFunc() {
			this.dialogVisible = false;
			let route = this.$router.resolve({
        path: "/lowCodeDesignEngine",
				query: {
          name: this.funcObj.name,
					moduleId: this.moduleId,
					sysVersion: this.sysVersion,
				},
			});
      this.funcObj.name = '';
			window.open(route.href, "_blank");
		},
		editFunc(funcData) {
			let route = this.$router.resolve({
				path: "/lowCodeDesignEngine",
				query: {
          moduleId: this.moduleId,
					sysVersion: this.sysVersion,
					data: JSON.stringify(funcData),
				},
			});
			window.open(route.href, "_blank");
		},
		publishFunc(funcData) {
			this.$confirm("确定发布吗?", "提示", {
				confirmButtonText: "确定",
				cancelButtonText: "取消",
				type: "warning",
			})
				.then(() => {
					this.httpUtil
						.comnUpdate({
							action: "LowCodeConfig.release",
							params: { id: funcData.id },
							mask: true,
						})
						.then((data) => {
							if (data.success) {
								this.$message.success("操作成功");
								this.getFuncList();
							}
						});
				})
				.catch(() => {});
		},
		moudleFunc(funcData) {
      let str = ''
      let flag = ''
      if(funcData.template == '1') {
        str = '此操作将取消模板设置，是否继续?'
        flag = '0'
      } else {
        str = '此操作将该功能设置为模板，是否继续?'
        flag = '1'
      }
      this.$confirm(str, "提示", {
				confirmButtonText: "确定",
				cancelButtonText: "取消",
				type: "warning",
			})
				.then(() => {
					this.httpUtil
						.comnUpdate({
							action: "LowCodeConfig.setTemplate",
							params: {
                id: funcData.id,
                template: flag,
              },
						})
						.then((data) => {
							if (data.success) {
								this.getFuncList();
							}
						});
				})
				.catch(() => {});
		},
		deleteFunc(funcData) {
			this.$confirm("确定删除吗?", "提示", {
				confirmButtonText: "确定",
				cancelButtonText: "取消",
				type: "warning",
			})
				.then(() => {
					this.httpUtil
						.comnUpdate({
							action: "LowCodeConfig.delete",
							params: { id: funcData.id },
							mask: true,
						})
						.then((data) => {
							if (data.success) {
								this.$message.success("操作成功");
								this.getFuncList();
							}
						});
				})
				.catch(() => {});
		},
		copy(index) {
      let random = this.$refs.idRefs[index].innerHTML;
			const input = document.createElement("input");
			document.body.appendChild(input);
			input.setAttribute("value", random);
			input.setAttribute("create", 1);
			input.select();
			document.execCommand("Copy");
			var list = document.getElementsByTagName("input");
			var inputList = Array.prototype.slice.call(list);
			inputList.forEach((item) => {
				if (item.getAttribute("create")) document.body.removeChild(item);
			});
			this.$message.success("ID复制成功");
		},
		historyVersion(info) {
			console.log(" historyVersion ", info);
			this.httpUtil
				.comnQuery({
					action: "LowCodeConfig.findConfigByVersion",
					params: { id: info[0], version: info[1] },
					mask: true,
				})
				.then((data) => {
					console.log(" ### 查询结果 ###", data);
					if (data.success) {
						let selectRowData = assign({ history: true }, data.returndata);
						this.editFunc(selectRowData);
					} else {
						this.$message.error("查询历史版本信息失败!");
					}
				});
		},
	},
};
</script>

<style lang="scss" scoped>
.funcPage {
	height: 100%;
	background-color: #f4f6fa;
	&_header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 10px 20px;
		background: #fff;
	}
	&_body {
		display: flex;
		justify-content: flex-start;
		// align-items: center;
		flex-wrap: wrap;
		margin-right: 30px;
		&_item {
			width: 270px;
			height: 160px;
			border-radius: 6px;
			font-size: 16px;
			margin: 25px 0 0 30px;
			background: #fff;
			transition: 0.2s all;

			&:hover {
				box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
			}
			&_head {
				display: flex;
				justify-content: space-between;
				align-items: center;
				padding: 5px 15px;
			}
			&_con {
				padding: 10px 15px;
				font-size: 16px;
				height: 70px;
				box-sizing: content-box;
				border-bottom: 1px solid #dcdfe6;
				cursor: pointer;
				.con_text1 {
					font-weight: 600;
					color: #303133;
				}
				.con_text2 {
					font-size: 12px;
					color: #909399;
				}
			}
			&_bottom {
				display: flex;
				height: 32px;
				justify-content: space-around;
				align-items: center;
				.el-divider {
					background-color: #dcdfe680;
					margin: 0;
				}
				.btn_icon {
					padding: 5px 10px;
					color: #909399;
					cursor: pointer;
					transition: 0.2s all;
					&:hover {
						color: #409eff;
					}
				}
			}
		}
		&_add {
			text-align: center;
			cursor: pointer;
			.el-icon-plus {
				line-height: 160px;
				color: #5c7ab0;
				font-size: 50px;
				font-weight: 600;
				text-align: center;
			}
		}
	}
	.el-icon-more {
		cursor: pointer;
	}
}
</style>
