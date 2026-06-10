<template>
	<div class="py-page">
		<k-form-search-customize data-target="userGrid" v-model="queryParam" data-label-width="70px">
			<k-form-item label="所属部门">
				<k-field-cascader
					v-model="queryParam.deptno"
					data-diffcondition="deptno,parentdeptno"
					:data-graphql="querydeptGraphql"
					data-display-child="children"
					data-check-strictly
					data-show-num
					:data-props="{ expandTrigger: 'hover' }"
					data-size="medium"
					data-placeholder="请选择"
					data-clearable
					data-fileterable
					data-display-field="deptname"
					data-value-field="deptno"
				>
				</k-field-cascader>
			</k-form-item>
			<k-form-item label="工号" data-label-width="40px">
				<k-field-text v-model="queryParam.jobno" data-validate-type="text"></k-field-text>
			</k-form-item>
			<k-form-item label="登录名称">
				<k-field-text v-model="queryParam.loginname" data-validate-type="text" />
			</k-form-item>
			<k-form-item label="用户名称">
				<k-field-text v-model="queryParam.username" data-validate-type="text"></k-field-text>
			</k-form-item>
			<k-form-item label="用户状态">
				<k-field-select v-model="queryParam.userstatus" data-dict="userstatus"></k-field-select>
			</k-form-item>

			<!-- <k-btn slot="button" class="btn-custom-default" data-functype="POPUP" :data-handler="()=>this.formData={}"
             data-target="addUserPopup" v-if="global.isShowAuthorityButton('User.addUser')">
        <md-icon md-src="/static/svg/add.svg"/>新增
      </k-btn> -->
		</k-form-search-customize>

		<div class="py-page-container">
			<!--    添加UserItem弹出框   -->
			<k-popup ref="addUserPopup" data-title="新增">
				<k-form ref="addUserForm" :data-col="2">
					<k-form-item label="登录名">
						<k-field-text
							v-model="formData.loginname"
							:data-allowblank="false"
							:data-max-length="32"
							:data-auto-validate="true"
							data-regx="^[A-Za-z_0-9]*$"
							data-regx-text="请输入字母、下划线与数字"
						/>
					</k-form-item>
					<k-form-item label="用户名">
						<k-field-text
							v-model="formData.username"
							:data-allowblank="false"
							:data-max-length="64"
							:data-auto-validate="true"
						/>
					</k-form-item>
					<k-form-item label="工号">
						<k-field-text v-model="formData.jobno" :data-allowblank="false" :data-max-length="64" />
					</k-form-item>
					<k-form-item label="密码">
						<k-field-text
							v-model="formData.passwd"
							:data-allowblank="false"
							:data-max-length="32"
							data-regx="^(?!([A-Z]*|[a-z]*|[0-9]*|[!-/:-@\[-`{-~]*|[A-Za-z]*|[A-Z0-9]*|[A-Z!-/:-@\[-`{-~]*|[a-z0-9]*|[a-z!-/:-@\[-`{-~]*|[0-9!-/:-@\[-`{-~]*)$)[A-Za-z0-9!-/:-@\[-`{-~]{8,15}$"
							data-regx-text="密码要求包含大写字母、小写字母、数字、特殊符号中至少三种类型，长度为8~15位"
							data-show-password="true"
						/>
					</k-form-item>

					<k-form-item label="证件类型">
						<k-field-select v-model="formData.idtype" data-dict="id_type" :data-allowblank="false" />
					</k-form-item>
					<k-form-item label="证件号码">
						<k-field-text v-model="formData.idno" :data-allowblank="false" />
					</k-form-item>

					<k-form-item label="所属部门">
						<k-field-cascader
							style="width: 100%"
							v-model="formData.deptno"
							data-diffcondition="deptno,parentdeptno"
							:data-graphql="querydeptGraphql"
							data-display-child="children"
							data-check-strictly
							data-show-num
							:data-props="{ expandTrigger: 'hover' }"
							data-size="medium"
							data-placeholder="请选择所属部门"
							data-clearable
							data-fileterable
							data-display-field="deptname"
							data-value-field="deptno"
							:data-allowblank="false"
						>
						</k-field-cascader>
					</k-form-item>
					<k-form-item label="所属机构">
						<k-field-cascader
							style="width: 100%"
							v-model="formData.orgno"
							data-diffcondition="orgno,parentorgno"
							:data-graphql="queryOrgGraphql"
							data-display-child="children"
							data-check-strictly
							data-show-num
							:data-props="{ expandTrigger: 'hover' }"
							data-size="medium"
							data-placeholder="请选择所属机构"
							data-clearable
							data-fileterable
							data-display-field="orgname"
							data-value-field="orgno"
						>
						</k-field-cascader>
					</k-form-item>
					<k-form-item label="移动电话">
						<k-field-text v-model="formData.mobileno" data-validate-type="number" :data-max-length="11" />
					</k-form-item>

					<k-form-item label="电子邮箱">
						<k-field-text
							v-model="formData.email"
							data-regx="^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$"
							data-regx-text="请输入正确的邮箱"
							:data-max-length="64"
						/>
					</k-form-item>
					<k-form-item label="性别">
						<k-field-select v-model="formData.sex" data-dict="sex" />
					</k-form-item>

					<k-form-footer data-align="center">
						<k-btn
							class="btn-custom-primary"
							data-functype="SUBMIT"
							data-from="addUserForm"
							ref="addBut"
							:data-model="formData"
							data-target="userGrid"
							:data-handler="md5PwdandAddUserStatus"
						>
							<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
						</k-btn>
						<k-btn class="btn-custom-plain" data-functype="CLOSE">
							<md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn
						>
					</k-form-footer>
				</k-form>
			</k-popup>
			<div class="table-top-btns">
				<div class="left">
					<k-btn
						class="btn-custom-primary"
						data-functype="POPUP"
						:data-handler="() => (this.formData = {})"
						data-target="addUserPopup"
						v-if="global.isShowAuthorityButton('User.addUser')"
					>
						<md-icon md-src="/static/svg/add.svg" />新增</k-btn
					>
				</div>
			</div>
			<k-grid
				ref="userGrid"
				@data-row-select="selectRow"
				data-action="User.findUsers"
				data-fixed="right"
				data-operate-width="300px"
				@init="
					(grid) => {
						this.$kgrid = grid;
					}
				"
			>
				<k-grid-column data-header="用户id" data-name="userid"></k-grid-column>
				<k-grid-column data-header="工号" data-name="jobno"></k-grid-column>
				<k-grid-column data-header="登录名" data-name="loginname"></k-grid-column>
				<k-grid-column data-header="用户名" data-name="username"></k-grid-column>
				<k-grid-column data-header="证件类型" data-name="idtype" data-dict="id_type"></k-grid-column>
				<k-grid-column data-header="证件号码" data-name="idno" data-width="140px"></k-grid-column>
				<k-grid-column data-header="所属部门" data-name="deptname"></k-grid-column>
				<k-grid-column data-header="电话" data-name="mobileno"></k-grid-column>
				<k-grid-column data-header="邮箱" data-name="email"></k-grid-column>
				<k-grid-column data-header="用户状态" data-name="userstatus" data-dict="userstatus"></k-grid-column>
				<template slot="operate" slot-scope="scope">
					<k-btn
						class="md-info md-just-icon md-simple"
						data-descript="修改用户"
						data-functype="POPUP"
						data-size="mini"
						data-target="editUserPopup"
						v-if="global.isShowAuthorityButton('User.updateUser')"
						:data-disabled="checkCanUpdate(scope.row.row.userid)"
					>
						<md-icon>edit</md-icon>
					</k-btn>
					<!-- <k-btn class="btn-custom-text" data-functype="POPUP" data-size="mini"
                 data-target="editUserPopup" v-if="global.isShowAuthorityButton('User.updateUser')"
                 :data-disabled="checkCanUpdate(scope.row.row.userid)">
            修改用户
          </k-btn> -->
					<k-btn
						class="md-success md-just-icon md-simple"
						data-descript="角色设置"
						data-functype="POPUP"
						data-size="mini"
						data-target="setRolePopup"
						v-if="global.isShowAuthorityButton('UserRole.update')"
					>
						<md-icon>person</md-icon>
					</k-btn>
					<!-- <k-btn class="btn-custom-text" data-functype="POPUP" data-size="mini"
            data-target="setRolePopup" v-if="global.isShowAuthorityButton('UserRole.update')">
            角色设置
          </k-btn> -->
					<k-btn
						class="md-success md-just-icon md-simple"
						data-descript="重置密码"
						data-functype="POPUP"
						data-target="resetPwdPopup"
						v-if="global.isShowAuthorityButton('User.resetPwd')"
					>
						<md-icon md-src="/static/svg/restpw.svg" />
					</k-btn>
					<!-- <k-btn class="btn-custom-text" data-functype="POPUP"
                 data-target="resetPwdPopup" v-if="global.isShowAuthorityButton('User.resetPwd')">
            重置密码
          </k-btn> -->
					<k-field-bswitch
						data-on-value="N"
						data-off-value="D"
						v-model="scope.row.row.userstatus"
						data-on-action="User.recoverUse"
						data-off-action="User.stopUse"
						:data-params="scope.row.row"
						:data-confirm="true"
						data-on-confirm-info="启用"
						data-off-confirm-info="停用"
						v-if="
							global.isShowAuthorityButton('User.recoverUse') ||
							global.isShowAuthorityButton('User.stopUse')
						"
					/>
					<!-- <k-btn class="btn-custom-text" :data-action="scope.row.row.userstatus=='N' ? 'User.stopUse' : 'User.recoverUse'"
            :data-params="scope.row.row" :data-confirm="true" data-functype="SUBMIT" data-target="userGrid" :data-descript="scope.row.row.userstatus=='N' ? '停用' : '启用'"
            v-if="global.isShowAuthorityButton('User.recoverUse') || global.isShowAuthorityButton('User.stopUse')">
            {{scope.row.row.userstatus=='N' ? '停用' : '启用'}}
          </k-btn> -->
				</template>
			</k-grid>
		</div>

		<!--    重置密码弹出框   -->
		<div>
			<k-popup ref="resetPwdPopup" data-title="重置密码">
				<k-form ref="resetPwdForm" :data-col="1">
					<k-form-item label="新密码">
						<k-field-text
							v-model="formData.newPwd"
							data-show-password
							:data-allowblank="false"
							:data-max-length="20"
						/>
					</k-form-item>
					<k-form-item label="确认密码">
						<k-field-text
							v-model="formData.confirmNewPwd"
							data-show-password
							:data-allowblank="false"
							:data-max-length="20"
						/>
					</k-form-item>
					<k-form-footer data-align="center">
						<k-btn
							class="btn-custom-primary"
							data-functype="SUBMIT"
							data-action="User.resetPwd"
							:data-model="formData"
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

		<!--    角色设置弹出框   -->
		<div>
			<k-popup ref="setRolePopup" data-title="角色设置" data-width="600px">
				<k-form ref="setRoleForm" :data-model="formData" :data-col="1" style="width: 550px">
					<k-form-item label="角色" data-input-width="400px">
						<k-field-checkbox
							v-model="formData.roleids"
							:data-allowblank="false"
							:data-params="{ roleids: formData.roleids, userid: formData.userid }"
							data-action="Role.findRoleByLoginUser"
							data-display-field="rolename"
							data-value-field="roleid"
						/>
					</k-form-item>
					<k-form-footer data-align="center">
						<k-btn
							class="btn-custom-primary"
							data-functype="SUBMIT"
							data-action="UserRole.update"
							data-target="userGrid"
							:data-model="formData"
							data-form="setRoleForm"
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

		<!--    修改用户弹出框   -->
		<k-popup ref="editUserPopup" data-title="修改">
			<k-form ref="editUserForm" :data-col="2">
				<k-form-item label="登录名">
					<k-field-text v-model="formData.loginname" data-disabled data-clearable="false" />
				</k-form-item>
				<k-form-item label="用户名">
					<k-field-text v-model="formData.username" :data-allowblank="false" :data-max-length="32" />
				</k-form-item>
				<k-form-item label="工号">
					<k-field-text
						v-model="formData.jobno"
						data-disabled
						:data-allowblank="false"
						data-clearable="false"
					/>
				</k-form-item>

				<k-form-item label="证件类型">
					<k-field-select v-model="formData.idtype" data-dict="id_type" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="证件号码">
					<k-field-text v-model="formData.idno" :data-allowblank="false" />
				</k-form-item>
				<!--        <k-form-item label="密码"  v-show="false">
                    <k-field-text v-model="formData.passwd" :data-max-length="32"/>
                  </k-form-item> -->
				<k-form-item label="所属部门">
					<k-field-cascader
						style="width: 100%"
						v-model="formData.deptno"
						data-diffcondition="deptno,parentdeptno"
						:data-graphql="querydeptGraphql"
						data-display-child="children"
						data-check-strictly
						data-show-num
						:data-props="{ expandTrigger: 'hover' }"
						data-size="medium"
						data-placeholder="请选择所属部门"
						data-clearable
						data-fileterable
						data-display-field="deptname"
						data-value-field="deptno"
						:data-allowblank="false"
					>
					</k-field-cascader>
				</k-form-item>

				<k-form-item label="所属机构">
					<k-field-cascader
						style="width: 100%"
						v-model="formData.orgno"
						data-diffcondition="orgno,parentorgno"
						:data-graphql="queryOrgGraphql"
						data-display-child="children"
						data-check-strictly
						data-show-num
						:data-props="{ expandTrigger: 'hover' }"
						data-size="medium"
						data-placeholder="请选择所属机构"
						data-clearable
						data-fileterable
						data-display-field="orgname"
						data-value-field="orgno"
					>
					</k-field-cascader>
				</k-form-item>
				<k-form-item label="移动电话">
					<k-field-text v-model="formData.mobileno" data-validate-type="number" :data-max-length="11" />
				</k-form-item>
				<k-form-item label="电子邮箱">
					<k-field-text
						v-model="formData.email"
						data-regx="^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$"
						data-regx-text="请输入正确的邮箱"
						:data-max-length="64"
					/>
				</k-form-item>
				<k-form-item label="性别">
					<k-field-select v-model="formData.sex" data-dict="sex" />
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-from="editUserForm"
						:data-model="formData"
						data-target="userGrid"
						ref="editBut"
						:data-handler="editCheckInfo"
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
import { assign } from "lodash";
import MD5 from "@/frame/md5.js";
import Tools from "@/utils/tools";

export default {
	name: "user",
	data() {
		return {
			userOrgno: "ROOT",
			userRole: "",
			formData: {},
			cascaderValue: [],
			selectRowData: {},
			$kgrid: null,
			queryParam: {},
		};
	},
	computed: {
		queryOrgGraphql() {
			return (
				'{queryOrg(action:"findChildren",orgno:"' +
				this.userOrgno +
				'") {rows{orgid, orgname, parentorgno, orgno},results}}'
			);
		},
		querydeptGraphql() {
			return '{queryDept(action:"find") {rows{deptno, deptname, parentdeptno, deptid},results}}';
		},
	},
	methods: {
		editCheckInfo(val) {
			//让按钮处于加载状态
			this.$refs.editBut.setIconStyle(0, []);
			let b = this.idCodeData(val);
			this.$nextTick(() => {
				if (!b) {
					//让按钮取消加载状态
					this.$refs.editBut.setIconStyle(1, []);
				}
				let a = this.$refs.editUserForm.validate();
				// //验证通过执行ajax
				if (a && b) {
					this.httpUtil
						.comnUpdate({
							action: "User.updateUser",
							params: val,
						})
						.then((data) => {
							this.$refs.editBut.setIconStyle(1, []);
							if (data.success) {
								this.$refs.editUserPopup.close();
								this.$refs.userGrid.load(this.queryParam);
							}
						});
				}
			});
			//让按钮取消加载状态
			this.$refs.editBut.setIconStyle(1, []);
		},

		addCheckInfo(val) {
			//让按钮处于加载状态
			this.$refs.addBut.setIconStyle(0, []);
			let b = this.idCodeData(val);
			this.$nextTick(() => {
				if (!b) {
					//让按钮取消加载状态
					this.$refs.addBut.setIconStyle(1, []);
				}
				let a = this.$refs.addUserForm.validate();
				// //验证通过执行ajax
				if (a && b) {
					this.httpUtil
						.comnUpdate({
							action: "User.addUser",
							params: val,
						})
						.then((data) => {
							this.$refs.addBut.setIconStyle(1, []);
							if (data.success) {
								this.$refs.addUserPopup.close();
								this.$refs.userGrid.load(this.queryParam);
							}
						});
				}
			});
			//让按钮取消加载状态
			this.$refs.addBut.setIconStyle(1, []);
		},

		//身份证验证器 axin
		idCodeData(val) {
			if (val.idtype === "1") {
				let StrNo = val.idno.toString();
				let errors = [];
				if (StrNo.length === 15) {
					if (!Tools.isValidDate("19" + StrNo.substr(6, 2) + StrNo.substr(8, 2) + StrNo.substr(10, 2))) {
						errors.push("身份证号码错误：出生日期不正确");
						//return '身份证号码错误：出生日期不正确';
					}
				} else if (StrNo.length === 18) {
					if (!Tools.isValidDate(StrNo.substr(6, 4) + StrNo.substr(10, 2) + StrNo.substr(12, 2))) {
						errors.push("身份证号码错误：出生日期不正确");
						//return '身份证号码错误：出生日期不正确';
					}
				} else {
					errors.push("身份证号码必须为15位或者18位");
					//return '身份证号码必须为15位或者18位';
				}

				if (StrNo.length === 18) {
					let a, b, c;
					if (!Tools.isNumber(StrNo.substr(0, 17))) {
						errors.push("身份证号码错误：前17位不能含有英文字母");
						//return '身份证号码错误：前17位不能含有英文字母';
					}
					a =
						parseInt(StrNo.substr(0, 1)) * 7 +
						parseInt(StrNo.substr(1, 1)) * 9 +
						parseInt(StrNo.substr(2, 1)) * 10;
					a =
						a +
						parseInt(StrNo.substr(3, 1)) * 5 +
						parseInt(StrNo.substr(4, 1)) * 8 +
						parseInt(StrNo.substr(5, 1)) * 4;
					a =
						a +
						parseInt(StrNo.substr(6, 1)) * 2 +
						parseInt(StrNo.substr(7, 1)) * 1 +
						parseInt(StrNo.substr(8, 1)) * 6;
					a =
						a +
						parseInt(StrNo.substr(9, 1)) * 3 +
						parseInt(StrNo.substr(10, 1)) * 7 +
						parseInt(StrNo.substr(11, 1)) * 9;
					a =
						a +
						parseInt(StrNo.substr(12, 1)) * 10 +
						parseInt(StrNo.substr(13, 1)) * 5 +
						parseInt(StrNo.substr(14, 1)) * 8;
					a = a + parseInt(StrNo.substr(15, 1)) * 4 + parseInt(StrNo.substr(16, 1)) * 2;
					b = a % 11;
					if (b == 2) {
						//最后一位为校验位
						c = StrNo.substr(17, 1).toUpperCase(); //转为大写X
					} else {
						c = parseInt(StrNo.substr(17, 1));
					}
					switch (b) {
						case 0:
							if (c != 1) {
								errors.push(
									"身份证号码校验位错"
								); /*return '身份证号码校验位错';/*：最后一位应该为：1';*/
							}
							break;
						case 1:
							if (c != 0) {
								errors.push(
									"身份证号码校验位错"
								); /*return '身份证号码校验位错';/*：最后一位应该为：0";*/
							}
							break;
						case 2:
							if (c != "X") {
								errors.push(
									"身份证号码校验位错"
								); /*return '身份证号码校验位错';/*：最后一位应该为：X";*/
							}
							break;
						case 3:
							if (c != 9) {
								errors.push(
									"身份证号码校验位错"
								); /*return '身份证号码校验位错';/*：最后一位应该为：9";*/
							}
							break;
						case 4:
							if (c != 8) {
								errors.push(
									"身份证号码校验位错"
								); /*return '身份证号码校验位错';/*：最后一位应该为：8";*/
							}
							break;
						case 5:
							if (c != 7) {
								errors.push(
									"身份证号码校验位错"
								); /*return '身份证号码校验位错';/*：最后一位应该为：7";*/
							}
							break;
						case 6:
							if (c != 6) {
								errors.push(
									"身份证号码校验位错"
								); /*return '身份证号码校验位错';/*：最后一位应该为：6";*/
							}
							break;
						case 7:
							if (c != 5) {
								errors.push("身份证号码校验位错"); /*return '身份证号码校验位错';/*最后一位应该为：5";*/
							}
							break;
						case 8:
							if (c != 4) {
								errors.push(
									"身份证号码校验位错"
								); /*return '身份证号码校验位错';/*：最后一位应该为：4";*/
							}
							break;
						case 9:
							if (c != 3) {
								errors.push(
									"身份证号码校验位错"
								); /*return '身份证号码校验位错';/*：最后一位应该为：3";*/
							}
							break;
						case 10:
							if (c != 2) {
								errors.push(
									"身份证号码校验位错"
								); /*return '身份证号码校验位错';/*：最后一位应该为：2";*/
							}
					}
				} else {
					//15位身份证号
					if (!Tools.isNumber(StrNo)) {
						errors.push("身份证号码错误：前15位不能含有英文字母");
					}
				}
				if (errors.length > 0) {
					Tools.alert(errors[0], "danger");
					return false;
				} else {
					return true;
				}
			} else {
				return true;
			}
		},

		//验证当前用户是否可以点击修改按钮
		checkCanUpdate(userId) {
			let result = true;
			let sessionUserId = localStorage.getItem("userid");
			let roleIds = localStorage.getItem("roleids");
			let roleIdsArr = [];
			if (roleIds != null && roleIds != undefined) {
				roleIdsArr = roleIds.split(",");
			}
			let roleContain = false;
			if (roleIdsArr.some((role) => role == "0" || role == "1")) {
				roleContain = true;
			}
			//自己的数据或者登录用户是超级管理员、业务管理员、admin都可以操作
			if (sessionUserId == userId || sessionUserId == "admin" || roleContain) {
				result = false;
			}
			return result;
		},
		selectRow(row, column, event) {
			const _this = this;
			_this.selectRowData = assign({}, row);
			_this.formData = assign({}, row);
		},
		dataBeforeLoad() {
			return {
				excOrgno: "ROOT",
			};
		},
		statusRender(row) {
			// console.log("statusRender=======>", row)
		},
		updSuccess(pop) {
			this.$refs.userGrid.load();
			pop.close();
		},
		md5PwdandAddUserStatus(params) {
			this.addCheckInfo(params);
			params.passwd = MD5.MD5(params.loginname + params.passwd);
			params.userstatus = "N";
			return true;
		},
		resetPwdHandler(params) {
			if (params.newPwd !== params.confirmNewPwd) {
				this.$message.error("两次输入不一致！请重新输入");
				return false;
			}
			params.passwd = MD5.MD5(params.loginname + params.newPwd);
			params.newPwd = {};
			params.confirmNewPwd = {};
			return true;
		},
		roleHandler(params) {
			params.roleids = params.roleids.join(",");
		},

		roleSubmitHandler(params) {
			params.roleid = params.roleids.join(",");
		},
		checkUserStatusOut(params) {
			if (params.userstatus == "D") {
				return true;
			}
		},
		checkUserStatusOn(params) {
			if (params.userstatus == "N") {
				return false;
			}
		},
	},
};
</script>

<style lang="scss" scoped>
.md-switch {
	position: relative;
	display: inline-block;
	margin: auto;
}
</style>
