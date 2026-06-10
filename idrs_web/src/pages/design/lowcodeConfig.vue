<template>
	<div class="modulePage">
		<div class="modulePage_header">
			<el-dropdown class="modulePage_header_select">
				<div class="modulePage_header_select_label">当前版本： V{{ system.version }}<i class="el-icon-arrow-down el-icon--right"></i></div>
				<el-dropdown-menu slot="dropdown">
					<div class="dropdown_inner">
						<el-dropdown-item v-for="sysItem in systemList" :key="sysItem.id">
							<div class="flex_sb sysItem">
								<span>V{{ sysItem.version }}</span>
								<div class="sysItem_icon">
									<i class="el-icon-share" @click.stop="changeSys(sysItem)"></i>
									<i class="el-icon-document-copy" @click.stop="copySys(sysItem)"></i>
									<i class="el-icon-delete" @click.stop="deleteSys(sysItem)"></i>
								</div>
								<div class="sysItemActive" v-if="sysItem.status == '1'">当前版本</div>
							</div>
						</el-dropdown-item>
						<div class="sys_btn_add" @click.stop="addSys">
							<i class="el-icon-plus"></i>
							新增本版
						</div>
					</div>
				</el-dropdown-menu>
			</el-dropdown>
		</div>
		<div class="modulePage_body">
			<div class="modulePage_body_item modulePage_body_add" @click="newMoudle">
				<i class="el-icon-plus"></i>
			</div>
			<div class="modulePage_body_item" :class="'bg_card' + (index % 5)" v-for="(item, index) in moudleList" :key="item.id">
				<div class="modulePage_body_item_con" @click="jupUrl(item)">
					<div>{{ item.name }}</div>
				</div>
				<div class="modulePage_body_item_bottom">
					<i class="el-icon-edit btn_icon" @click="editMoudle(item)"></i>
					<el-divider direction="vertical"></el-divider>
					<i class="el-icon-document-copy btn_icon" @click="copyMoudle(item)"></i>
					<el-divider direction="vertical"></el-divider>
					<i class="el-icon-delete btn_icon" @click="deleteMoudle(item)"></i>
				</div>
			</div>
		</div>

		<el-dialog title="模块" :visible.sync="dialogVisible" width="380px" destroy-on-close>
			<div v-if="moudleObj.id" style="margin-bottom: 10px">
				<i class="el-icon-info"></i>
				此操作将基于本模块复制一个新模块
			</div>
			<el-form label-width="80px">
				<el-form-item label="模块名称">
					<el-input v-model="moudleObj.name"></el-input>
				</el-form-item>
			</el-form>
			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogVisible = false">取 消</el-button>
				<el-button type="primary" @click="moudleSave">确 定</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
import { clone } from "lodash";

export default {
	data() {
		return {
			system: {
				id: "",
				version: "",
			},
			systemList: [],
			moudleList: [],
			dialogVisible: false,
			moudleObj: {
				id: "",
				name: "",
				sysId: "",
				flag: "0", // 0 新增   1 修改   2 复制
			},
      loading: null,
		};
	},

	computed: {},
	created() {
    // this.loading = this.$loading({
    //   lock: true,
    //   text: '努力加载中...',
    //   spinner: 'el-icon-loading',
    //   background: 'rgba(0, 0, 0, 0.3)'
    // });
		this.initData();
    // setTimeout(() => {
    //   if(this.loading) {
    //     this.loading.close();
    //   }
    // }, 2000)
	},
	methods: {
		async initData() {
			await this.getSys();
			await this.getSysList();
			await this.getMoudleList();
      // this.loading.close();
		},
		changeSys(sysData) {
			// 切换版本
			this.$confirm("此操作将系统版本切换为 V" + sysData.version + ", 是否继续?", "提示", {
				confirmButtonText: "切换",
				cancelButtonText: "取消",
				type: "warning",
			})
				.then(async () => {
					// 切换版本
					await this.switchSysVersion(sysData.id);
					// 再次获取当前版本和版本列表
					// 获取当前版本下的所有模块
					await this.initData();
					this.$message({
						type: "success",
						message: "切换成功 !",
					});
				})
				.catch(() => {});
		},
		copySys(sysData) {
			// 复制版本
			this.$confirm("此操作将基于 V" + sysData.version + "新增一个新版本, 是否继续?", "提示", {
				confirmButtonText: "确定",
				cancelButtonText: "取消",
				type: "warning",
			})
				.then(async () => {
					// 复制版本
					await this.addVersion(sysData.id);
					// 再次获取当前版本和版本列表
					// 获取当前版本下的所有模块
					await this.initData();
					this.$message({
						type: "success",
						message: "新增成功 !",
					});
				})
				.catch(() => {});
		},
		deleteSys(sysData) {
			// 删除版本
			this.$confirm("此操作将删除版本 V" + sysData.version + ", 是否继续?", "提示", {
				confirmButtonText: "确定",
				cancelButtonText: "取消",
				type: "warning",
			})
				.then(async () => {
					// 删除版本
					await this.deleteVersion(sysData.id);
					// 再次获取当前版本和版本列表
					// 获取当前版本下的所有模块
					await this.initData();
					this.$message({
						type: "success",
						message: "删除成功 !",
					});
				})
				.catch(() => {});
		},
		addSys() {
			// 新增版本
			this.$confirm("此操作将新增版本, 是否继续?", "提示", {
				confirmButtonText: "确定",
				cancelButtonText: "取消",
				type: "warning",
			})
				.then(async () => {
					// 删除版本
					await this.addVersion();
					// 再次获取当前版本和版本列表
					// 获取当前版本下的所有模块
					await this.initData();
					this.$message({
						type: "success",
						message: "新增成功 !",
					});
				})
				.catch(() => {});
		},
		async getSys() {
			let res = await this.httpUtil.comnQuery({
				action: "LowCodeSysVersion.getCurrentVersion",
			});
			if (res.success) {
				this.system.id = res.returndata.id;
				this.system.version = res.returndata.version;
			}
		},
		async getSysList() {
			let res = await this.httpUtil.comnQuery({
				action: "LowCodeSysVersion.page",
			});
			this.systemList = clone(res.rows);
		},
		async getMoudleList() {
			let res = await this.httpUtil.comnQuery({
				action: "LowCodeModule.page",
				params: { sysId: this.system.id },
			});
			this.moudleList = clone(res.rows);
		},
		async switchSysVersion(id) {
			await this.httpUtil.comnQuery({
				action: "LowCodeSysVersion.switchSysVersion",
				params: { id: id },
			});
		},
		async deleteVersion(id) {
			await this.httpUtil.comnQuery({
				action: "LowCodeSysVersion.delete",
				params: { id: id },
			});
		},
		async addVersion(id) {
			await this.httpUtil.comnQuery({
				action: "LowCodeSysVersion.add",
				params: { version: id },
			});
		},
		jupUrl(moudleData) {
			this.$router.push({
				path: "/main/design/funcConfig",
				query: { sysVersion: this.system.version, moduleId: moudleData.id },
			});
		},
		newMoudle() {
			this.dialogVisible = true;
			this.moudleObj.name = "";
			this.moudleObj.id = "";
			this.moudleObj.sysId = this.system.id;
			this.moudleObj.flag = "0";
		},
		async moudleSave() {
			const { name, sysId, flag, id } = this.moudleObj;
			if (flag == "0") {
				await this.httpUtil.comnQuery({
					action: "LowCodeModule.add",
					params: { name: name, sysId: sysId },
				});
			} else if (flag == "1") {
				await this.httpUtil.comnQuery({
					action: "LowCodeModule.update",
					params: { name: name, sysId: sysId, id: id },
				});
			} else if (flag == "2") {
				await this.httpUtil.comnQuery({
					action: "LowCodeModule.copy",
					params: { name: name, sysId: sysId, id: id },
				});
			}
			this.dialogVisible = false;
			this.$message.success("操作成功");
			this.getMoudleList();
		},
		editMoudle(moudleData) {
			this.dialogVisible = true;
			this.moudleObj.id = moudleData.id;
			this.moudleObj.name = moudleData.name;
			this.moudleObj.sysId = moudleData.sysId;
			this.moudleObj.flag = "1";
		},
		copyMoudle(moudleData) {
			this.dialogVisible = true;
			this.moudleObj.id = moudleData.id;
			this.moudleObj.name = "";
			this.moudleObj.sysId = moudleData.sysId;
			this.moudleObj.flag = "2";
		},
		deleteMoudle(moudleData) {
			// 删除模块
			this.$confirm("此操作将删除模块 V" + moudleData.name + ", 是否继续?", "提示", {
				confirmButtonText: "确定",
				cancelButtonText: "取消",
				type: "warning",
			})
				.then(async () => {
					await this.httpUtil.comnQuery({
						action: "LowCodeModule.delete",
						params: { id: moudleData.id },
					});
					this.getMoudleList();

					this.$message({
						type: "success",
						message: "删除成功 !",
					});
				})
				.catch(() => {});
		},
	},
};
</script>

<style lang="scss" scoped>
.modulePage {
	height: 100%;
	background-color: #f4f6fa;
	&_header {
		text-align: right;
		padding: 10px 50px;
		background: #fff;
		&_select {
			display: inline-block;
			&_label {
				cursor: pointer;
			}
		}
	}
	&_body {
		display: flex;
		justify-content: flex-start;
		align-items: center;
		flex-wrap: wrap;
    margin-right: 30px;
		&_item {
			width: 224px;
			height: 108px;
			border-radius: 4px;
			color: #fff;
			font-size: 16px;
			margin: 25px 0 0 30px;
			background-size: 102%;
			background-repeat: no-repeat;
			background-position: center;
			transition: 0.2s all;

			&:hover {
				box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
			}
			&_con {
				padding: 14px;
				font-size: 16px;
				height: 50px;
				box-sizing: content-box;
				border-bottom: 1px solid #ffffff30;
				cursor: pointer;
			}
			&_bottom {
				display: flex;
				height: 28px;
				justify-content: space-around;
				align-items: center;
				.el-divider {
					background-color: #ffffff50;
					margin: 0;
				}
				.btn_icon {
					padding: 5px 10px;
					color: #fff;
					cursor: pointer;
					opacity: 0.7;
					transition: 0.2s all;
					&:hover {
						opacity: 1;
					}
				}
			}
		}
		&_add {
			background: #fff;
			text-align: center;
			cursor: pointer;
			.el-icon-plus {
				line-height: 100px;
				color: #5c7ab0;
				font-size: 42px;
				font-weight: 600;
				text-align: center;
			}
		}
	}
	.el-icon-info {
		color: #e6a23c;
	}
}
.el-dropdown-menu__item {
	&:hover {
		.sysItem_icon {
			display: inline-block;
		}
		.sysItemActive {
			display: none;
		}
	}
}
.sysItem {
	width: 130px;
}
.sysItem_icon {
	display: none;
}
.flex_sb {
	display: flex;
	// align-items: ;
	justify-content: space-between;
}
.dropdown_inner {
	height: 200px;
	overflow-y: auto;
	overflow-x: hidden;
	margin-bottom: 25px;
	// margin-bottom: 25px;
	&::-webkit-scrollbar {
		display: none; /* Chrome Safari */
	}
}
.sys_btn_add {
	position: absolute;
	bottom: 0;
	border-top: 1px solid #eee;
	background: #fff;
	width: 100%;
	color: #409eff;
	text-align: center;
	font-size: 12px;
	padding: 5px;
	cursor: pointer;
	.el-icon-plus {
		font-weight: 600;
	}
}
.sysItemActive {
	font-size: 12px;
	color: #e6a23c;
}
.bg_card0 {
	background-image: url("../../assets/img/img-card0.png");
}
.bg_card1 {
	background-image: url("../../assets/img/img-card1.png");
}
.bg_card2 {
	background-image: url("../../assets/img/img-card2.png");
}
.bg_card3 {
	background-image: url("../../assets/img/img-card3.png");
}
.bg_card4 {
	background-image: url("../../assets/img/img-card4.png");
}
</style>
