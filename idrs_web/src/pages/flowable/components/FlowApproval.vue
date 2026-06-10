<template>
  <div>
    <k-form ref="taskForm" data-ui="element" :data-col="1" data-input-width="500px">
      <k-form-item label="意见示例">
        <k-field-select v-model="commentExample" :data-data="commentExampleData" data-display-field="label" data-value-field="label" @data-on-change="exampleChange" />
      </k-form-item>
      <k-form-item label="审批意见">
        <k-field-text data-type="textarea" :data-col="1" :data-min-row="2" v-model="taskForm.comment" data-placeholder="请输入 审批意见" />
      </k-form-item>
      <!-- <k-form-item label="抄送人">
        <el-tag v-if="userData.copyUser.length===0">无</el-tag>
        <el-tag :key="index" v-for="(item, index) in userData.copyUser" closable :disable-transitions="false" @close="userTagClose(item)">
          {{ item.username }}
        </el-tag>
        <el-button class="button-new-tag" type="primary" icon="el-icon-plus" size="mini" circle @click="onSelectUsers" />
      </k-form-item> -->
      <k-form-footer>
        <el-button :loading="approvalBtnLoading" v-if="btns.indexOf(allBtns.complete.value)>-1" icon="el-icon-circle-check" type="success" @click="handleByType('complete')">通过</el-button>
        <el-button :loading="approvalBtnLoading" v-if="btns.indexOf(allBtns.delegate.value)>-1" icon="el-icon-chat-line-square" type="primary" @click="handleDelegate">委派</el-button>
        <el-button :loading="approvalBtnLoading" v-if="btns.indexOf(allBtns.transfer.value)>-1" icon="el-icon-refresh-left" type="success" @click="handleTransfer">转办</el-button>
        <el-button :loading="approvalBtnLoading" v-if="btns.indexOf(allBtns.refuse.value)>-1" icon="el-icon-circle-close" type="danger" @click="handleByType('refuse')">拒绝</el-button>
        <!-- <el-button :loading="approvalBtnLoading" v-if="btns.indexOf(allBtns.reject.value)>-1" icon="el-icon-circle-close" type="warning" @click="handleReject">驳回</el-button> -->
      </k-form-footer>
    </k-form>
    <k-popup ref="userPopup" :data-title="userData.title"  data-width="400px">
      <k-form ref="userForm" data-ui="element" :data-col="1">
        <k-form-item label="用户">
          <k-field-select v-model="queryUserName" data-placeholder="请搜索 用户" :data-auto-load="false" :data-remote="true" dataContentType="json" data-url="wf/system/user/list.json" data-display-field="username" data-value-field="userid" @data-on-change="userChange" />
        </k-form-item>
        <!-- <k-form-item label="动态审批人">
          <k-field-cascader v-model="dynamicUserId" data-placeholder="请选择 审批人" :data-clearable="true" :data-data="wfParams" data-display-field="paramName" data-value-field="paramId" @data-on-change="dynamicUserChange" :dataLazy="true" :dataLazyLoad="userLoad" />
        </k-form-item> -->
        <!-- <k-form-footer></k-form-footer>
        <k-form-item label="抄送用户" v-if="userData.type=='copy'">
          <el-tag v-if="userData.copyUser.length===0">无</el-tag>
          <el-tag :key="index" v-for="(item, index) in userData.copyUser" closable :disable-transitions="false" @close="userTagClose(item)">
            {{ item.username }}
          </el-tag>
        </k-form-item> -->
        <k-form-footer>
          <k-btn class="btn-custom-primary" @click.native.stop="submitUserData" ref="submitBtn">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确认
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="rejectTaskPopup" data-title="驳回节点"  data-width="400px">
      <k-form ref="rejectTaskForm" data-ui="element">
        <k-form-item label="驳回节点">
          <k-field-select v-model="taskForm.targetKey" :data-allowblank="false" :data-params="taskForm" dataContentType="json" data-url="wf/task/rejectList.json" data-display-field="taskName" data-value-field="taskDefKey" />
        </k-form-item>
        <k-form-footer>
          <k-btn class="btn-custom-primary" @click.native.stop="submitReject" ref="submitRejectBtn">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确认
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
  </div>
</template>

<script>
import Tools from "@/utils/tools.js";
import { uniqBy } from "lodash";
import eventBus from "@/utils/eventBus";
import wfStatus from "../enum/enum.js";

export default {
	name: "FlowApproval",
	props: {
		taskInfo: {},
	},
	data() {
		return {
			taskForm: {
				comment: "", // 意见内容
				procInsId: "", // 流程实例编号
				deployId: "", // 流程定义编号
				taskId: "", // 流程任务编号
				definitionId: "", // 流程编号
				copyUserIds: "", // 抄送人Id
				vars: "",
				targetKey: "",
				userId: "", //转办或者委派人
				uploadFiles: [], //上传文件
				variables: {},
			},
			userData: {
				title: "",
				type: "copy",
				copyUser: [],
				userId: "",
			},
			queryUserName: "",
			dynamicUserId: "",
			approvalBtnLoading: false,
			btns: [],
			allBtns: wfStatus.taskBtns,
			commentExampleData: [
				{
					label: "通过",
				},
				{
					label: "拒绝",
				},
			],
			commentExample: "",
			flowFormInfoRef: null,
		};
	},
	created() {
		this.taskForm.deployId = this.taskInfo.deployId;
		this.taskForm.definitionId = this.taskInfo.definitionId;
		this.taskForm.taskId = this.taskInfo.taskId;
		this.taskForm.procInsId = this.taskInfo.procInsId;
		eventBus.$on("wfFileChange", (fileList) => {
			this.handleFileChange(fileList);
		});
		this.getBtns();
	},
	mounted() {
		this.$nextTick(() => {
			setTimeout(() => {
				this.getFormRef();
			}, 1500);
		});
	},
	computed: {},
	beforeDestroy() {
		eventBus.$off("wfFileChange");
	},
	methods: {
		getFormRef() {
			this.flowFormInfoRef = this.$parent.$children[0]?.$children[1]?.$children[0];
		},
		//获取业务表单校验的结果
		getValidForm() {
			this.getFormRef();
			if (this.flowFormInfoRef?.formType == "formFieldId") {
				if (this.flowFormInfoRef.validForm) {
					this.formResult = this.flowFormInfoRef.validForm();
					if (this.formResult) {
						console.log("业务表单校验通过");
					} else {
						console.log("业务表单校验未通过");
					}
				}
			} else if (this.flowFormInfoRef?.formType == "formComponentName") {
				let formRef = this.flowFormInfoRef.$children[0];
				if (formRef?.validForm) {
					this.formResult = this.flowFormInfoRef.$children[0].validForm();
					if (this.formResult) {
						console.log("业务表单校验通过");
					} else {
						console.log("业务表单校验未通过");
					}
				}
			} else if (this.flowFormInfoRef?.formType == "dynamicFormId") {
				let formRef = this.flowFormInfoRef.$children[0]?.$children[0];
				if (formRef?.validForm) {
					this.formResult = formRef.validForm();
					if (this.formResult) {
						console.log("业务表单校验通过");
					} else {
						console.log("业务表单校验未通过");
					}
				}
			}
		},
		getFormData() {
			if (this.flowFormInfoRef?.formType == "formFieldId") {
				let formFieldList = this.flowFormInfoRef.formFieldList ?? [];
				formFieldList.forEach((formField) => {
					this.taskForm.variables[formField.name] = formField.value;
				});
			} else if (this.flowFormInfoRef?.formType == "formComponentName") {
				this.taskForm.variables = this.flowFormInfoRef.$children[0].formData;
			} else if (this.flowFormInfoRef?.formType == "dynamicFormId") {
				this.taskForm.variables = this.flowFormInfoRef.$children[0]?.$children[0].formData;
			}
		},
		userLoad(node, resolve) {
			this.httpUtil
				.ajaxJson({
					url: "wf/param/parseWfParam/" + this.taskInfo.procInsId + "/" + node.data.paramId + ".json",
				})
				.then((res) => {
					// 通过调用resolve将子节点数据返回，通知组件数据加载完成
					if (res.data instanceof Array) {
						this.httpUtil
							.ajaxJson({
								url: "wf/system/user/" + res.data + ".json",
							})
							.then((res) => {
								const users = res.rows.map((t) => {
									t.leaf = true;
									t.paramId = t.userid;
									t.paramName = t.username;
									return t;
								});
								resolve(users);
							});
					} else {
						resolve([]);
					}
				});
		},
		exampleChange(val) {
			//this.taskForm.comment = this.taskForm.comment + val;
			this.taskForm.comment = val;
		},
		/** 按钮配置 */
		getBtns() {
			return this.httpUtil
				.ajaxJson({
					url: "wf/task/btns/" + this.taskInfo.procDefId + "/" + this.taskInfo.taskDefKey + ".json",
				})
				.then((response) => {
					this.btns = response.rows;
				});
		},
		handleFileChange(fileList) {
			this.taskForm.uploadFiles = fileList;
		},
		onSelectUsers() {
			this.userData.title = "添加抄送人";
			this.userData.type = "copy";
			this.$refs.userPopup.popup();
		},
		/** 查询用户列表 */
		getUserOptions() {
			return this.httpUtil
				.ajaxJson({
					url: "wf/system/user/list.json",
					params: { start: 0, limit: 10000000 },
				})
				.then((response) => {
					this.userOptions = response.rows;
				});
		},
		userChange(userid, user) {
			// if (this.userData.type == "copy") {
			// 	this.queryUserName = "";
			// 	this.userData.copyUser.push(user);
			// 	this.userData.copyUser = uniqBy(this.userData.copyUser, "userid");
			// } else {

			// }
			if (userid) {
				this.userData.userId = userid;
				this.dynamicUserId = "";
			} else {
				this.userData.userId = "";
			}
		},
		dynamicUserChange(userid) {
			if (userid) {
				this.userData.userId = userid;
				this.queryUserName = "";
			} else {
				this.userData.userId = "";
			}
		},
		userTagClose(tag) {
			this.userData.copyUser = this.userData.copyUser.filter((t) => {
				return t.userid != tag.userid;
			});
		},
		/** 委派任务 */
		handleDelegate() {
			this.userData.type = "delegate";
			this.userData.title = "委派任务";
			this.queryUserName = "";
			this.$refs.userPopup.popup();
		},
		/** 转办任务 */
		handleTransfer() {
			this.userData.type = "transfer";
			this.userData.title = "转办任务";
			this.queryUserName = "";
			this.$refs.userPopup.popup();
		},
		handleByType(handleType) {
			//只有通过需要获取表单结果
			if (handleType == "complete") {
				this.getValidForm();
				if (this.formResult === false) {
					return;
				}
			}
			this.approvalBtnLoading = true;
			this.getFormData();
			this.taskForm.copyUserIds = this.userData.copyUser.map((t) => t.userid).join(",");
			this.httpUtil
				.ajaxJson({
					url: "wf/task/" + handleType + ".json",
					params: this.taskForm,
					successAlert: true,
					errCallback: (reData) => {
						Tools.alert(reData.returnmsg, "danger");
						this.approvalBtnLoading = false;
					},
				})
				.then((res) => {
					this.approvalBtnLoading = false;
					this.goBack();
				});
		},
		/** 驳回任务 */
		handleReject() {
			this.$refs.rejectTaskPopup.popup();
		},
		submitReject() {
			let re = this.$refs.rejectTaskForm.validate();
			if (re === false) {
				return;
			}
			this.$refs.submitRejectBtn.setIconStyle(0);
			this.httpUtil
				.ajaxJson({
					url: "wf/task/reject.json",
					params: this.taskForm,
					successAlert: true,
					errCallback: (reData) => {
						Tools.alert(reData.returnmsg, "danger");
						this.$refs.submitRejectBtn.setIconStyle(1);
					},
				})
				.then((res) => {
					this.$refs.submitRejectBtn.setIconStyle(1);
					this.goBack();
				});
		},
		/**提交数据 */
		submitUserData() {
			let type = this.userData.type;
			if (type === "copy") {
				this.$refs.userPopup.close();
			} else {
				if (!this.userData.userId) {
					Tools.alert("请选择用户", "danger");
					return false;
				}
				this.$refs.submitBtn.setIconStyle(0);
				this.taskForm.userId = this.userData.userId;
				this.httpUtil
					.ajaxJson({
						url: "wf/task/" + type + ".json",
						params: this.taskForm,
						successAlert: true,
						errCallback: (reData) => {
							Tools.alert(reData.returnmsg, "danger");
							this.$refs.submitBtn.setIconStyle(1);
						},
					})
					.then((res) => {
						this.$refs.submitBtn.setIconStyle(1);
						this.goBack();
					});
			}
		},
		/**关闭弹窗,加载表格 */
		goBack() {
			this.$emit("closeDetailPopup");
			this.$emit("loadTaskGrid", true);
		},
	},
};
</script>
