<template>
	<div>
		<img class="icon-user" src="/static/svg/asideMenu/user.svg" alt="" />
		<el-dropdown @command="handleClick" placement="bottom">
			<span class="el-dropdown-link">
				{{ formData.loginname }}<i class="el-icon-arrow-down el-icon--right"></i>
			</span>
			<el-dropdown-menu slot="dropdown">
				<el-dropdown-item command="a">个人信息</el-dropdown-item>
				<el-dropdown-item command="b">修改密码</el-dropdown-item>
				<el-dropdown-item command="c">安全退出</el-dropdown-item>
			</el-dropdown-menu>
		</el-dropdown>

		<!--    修改用户弹出框   -->
		<k-popup ref="editUserPopup" data-title="编辑用户信息">
			<k-form ref="editUserForm" :data-col="1">
				<k-form-item label="登录名">
					<k-field-text v-model="formData.loginname" data-disabled />
				</k-form-item>
				<k-form-item label="所属部门">
					<k-field-text v-model="formData.deptname" data-disabled />
				</k-form-item>
				<k-form-item label="用户名称">
					<k-field-text
						v-model="formData.username"
						@input="forceUpdate"
						:data-max-length="32"
						:data-allowblank="false"
					/>
				</k-form-item>
				<k-form-item label="移动电话">
					<k-field-text
						v-model="formData.mobileno"
						@input="forceUpdate"
						data-validate-type="number"
						:data-max-length="11"
					/>
				</k-form-item>
				<k-form-item label="电子邮箱">
					<k-field-text
						v-model="formData.email"
						data-validate-type="email"
						@input="forceUpdate"
						:data-max-length="64"
					/>
				</k-form-item>
				<k-form-item label="性别">
					<k-field-select v-model="formData.sex" data-dict="sex" @input="forceUpdate" />
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="User.updateUserNoAuth"
						data-from="editUserForm"
						:data-model="formData"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE">
						<md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn
					>
				</k-form-footer>
			</k-form>
		</k-popup>

		<k-popup ref="resetPwdPopup" data-title="修改密码">
			<k-form ref="resetPwdForm" :data-col="1">
				<k-form-item label="原密码">
					<k-field-text v-model="pwdData.oldPwd" data-show-password :dataAllowblank="false" />
				</k-form-item>
				<k-form-item label="新密码" style="margin-top: 20px">
					<k-field-text
						v-model="pwdData.newPwd"
						data-regx="^(?!([A-Z]*|[a-z]*|[0-9]*|[!-/:-@\[-`{-~]*|[A-Za-z]*|[A-Z0-9]*|[A-Z!-/:-@\[-`{-~]*|[a-z0-9]*|[a-z!-/:-@\[-`{-~]*|[0-9!-/:-@\[-`{-~]*)$)[A-Za-z0-9!-/:-@\[-`{-~]{8,15}$"
						data-regx-text="密码要求包含大写字母、小写字母、数字、特殊符号中至少三种类型，长度为8~15位"
						data-show-password
						:dataAllowblank="false"
					/>
				</k-form-item>
				<k-form-item label="确认密码" style="margin-top: 20px">
					<k-field-text v-model="pwdData.confirmNewPwd" data-show-password :dataAllowblank="false" />
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="User.resetPwdCheckOldPwd"
						:data-model="pwdData"
						:data-handler="resetPwdHandler"
						data-form="resetPwdForm"
						data-target="userGrid"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE">
						<md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn
					>
				</k-form-footer>
			</k-form>
		</k-popup>
	</div>
</template>

<script>
import httpUtil from "../httpUtil";
import { merge } from "lodash";
import Tools from "@/utils/tools";
import MD5 from "@/frame/md5.js";
import auth from "../../utils/auth";

export default {
	components: {},
	props: {
		subMenus: {
			type: Array,
		},
	},
	data() {
		return {
			menuData: "",
			comMenus: [],
			minMenus: [],
			allMenus: [],
			hasMenuMore: false,
			isOpenMenuMore: false,
			announces: [],
			announce: {},
			userRole: "",
			formData: {
				loginname: "",
			},
			pwdData: {},
			currentWorkday: null,
		};
	},
	created() {
		Tools.getLoginUser().then((res) => {
			this.userRole = res.roleids;
		});
		this.httpUtil.sysDate().then((res) => {
			if (res) {
				this.currentWorkday = Tools.formatDate(res);
				localStorage.setItem("currentWorkday", res);
			}
		});
		this.getUserInfor();
	},
	methods: {
		handleClick(v) {
			if (v == "a") {
				this.popEditUserInfor();
			} else if (v == "b") {
				this.popChangePwd();
			} else if (v == "c") {
				this.loginOut();
			}
		},
		loadAnnounce() {
			let graphql =
				'{queryAnnounceInfo(action:"show") {rows{title, content, createdate, createtime, annfilepath, annfilename},results}}';
			this.httpUtil
				.graphqlQurey({
					graphql: graphql,
					params: {
						roleid: this.userRole,
					},
				})
				.then((data) => {
					this.announces = data["queryAnnounceInfo"].rows;
				});
		},
		download(path, filename) {
			this.httpUtil.download({
				url: "/base/comn-download.json",
				params: {
					path: path,
				},
				filename: filename,
			});
		},
		loginOut() {
			auth.removeToken();
			this.$router.push({
				path: "/login",
			});
		},
		popEditUserInfor() {
			this.$refs.editUserPopup.popup();
			this.getUserInfor();
		},
		popChangePwd() {
			this.$refs.resetPwdPopup.popup();
			this.getUserPwdInfor();
		},
		toggleSidebar() {
			this.$sidebar.displaySidebar(!this.$sidebar.showSidebar);
		},
		getUserInfor() {
			Tools.getLoginUser().then((res) => {
				this.formData.userid = res.userid;
				this.formData.loginname = res.loginname;
				this.formData.username = res.username;
				this.formData.mobileno = res.mobileno;
				this.formData.deptname = res.deptname;
				this.formData.passwd = res.passwd;
				this.formData.email = res.email;
				this.formData.sex = res.sex;
			});
		},
		getUserPwdInfor() {
			this.pwdData = {};
			Tools.getLoginUser().then((res) => {
				console.log(res);
				this.pwdData.loginname = res.loginname;
				this.pwdData.userid = res.userid;
			});
		},
		resetPwdHandler(params) {
			console.log(params);
			if (params.newPwd !== params.confirmNewPwd) {
				this.$message.error("两次输入不一致！请重新输入");
				return false;
			}
			params.passwd = MD5.MD5(params.loginname + params.newPwd);
			params.oldPwd = MD5.MD5(params.loginname + params.oldPwd);
			params.newPwd = {};
			params.confirmNewPwd = {};
			return true;
		},
		forceUpdate() {
			this.$forceUpdate();
		},
	},
};
</script>

<style lang="scss" scoped>
.menu-list svg {
	width: 20px;
	height: 20px;
}
::v-deep .dropdown-menu {
	margin-top: 10px;
	right: auto;
}

@media (max-width: 991px) {
	.md-toolbar .md-collapse {
		display: flex !important;
		width: 100%;
		-webkit-box-pack: end;
		-ms-flex-pack: end;
		justify-content: flex-end;
	}
}

::v-deep .md-field .md-input-action {
	top: 65%;
}

.k-form {
	width: 197px;
	display: flex;
	align-items: center;
}

.TopNav__CurrentWorkday {
	display: block;
	line-height: 50px;
}
.el-menu--horizontal {
	border-color: transparent;
	& > .el-submenu {
		/deep/ {
			.el-submenu__title {
				height: 45px;
				line-height: 45px;
			}
			&.is-active {
				color: green;
				.el-submenu__title {
					border-color: transparent;
				}
			}
		}
	}
}
/deep/ .el-dropdown-link {
	color: #003472;
}
.icon-user {
	width: 25px;
	margin-right: 5px;
}
</style>
