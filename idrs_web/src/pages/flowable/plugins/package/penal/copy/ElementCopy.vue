<template>
  <div class="panel-tab__content">
    <el-form size="mini" label-width="100px" @submit.native.prevent>
      <el-row>
        <h4><b>抄送表单配置</b></h4>
      </el-row>
      <el-form-item label="表单类型" prop="copyFormType">
        <el-select v-model="elementCopyInfo.copyFormType" placeholder="请选择表单类型" @change="updateCopyInfo('copyFormType')" clearable filterable style="width:100%">
          <el-option v-for="item in formTypes" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item v-if="elementCopyInfo.copyFormType" label="表单">
        <el-select v-if="elementCopyInfo.copyFormType=='formFieldId'" v-model="elementCopyInfo.copyFormKey" placeholder="请选择表单" @change="updateCopyInfo('copyFormKey')" clearable filterable style="width:100%">
          <el-option v-for="item in formOptions" :key="item.formFieldId" :label="item.displayName" :value="item.formFieldId" />
        </el-select>
        <el-select v-else-if="elementCopyInfo.copyFormType=='dynamicFormId'" v-model="elementCopyInfo.copyFormKey" placeholder="请选择表单" @change="updateCopyInfo('copyFormKey')" clearable filterable>
          <el-option v-for="item in dynamicForms" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-row>
        <h4><b>抄送人配置</b></h4>
      </el-row>
      <el-form-item label="用户">
        <el-select v-model="elementCopyInfo.copyUsers" multiple size="mini" placeholder="请搜索 用户" filterable remote :loading="userOptionsLoading" :remote-method="userOptionsRemoteMethod" @change="updateCopyInfo('copyUsers')">
          <el-option v-for="item in userOptionsCopy" :key="item.userid" :label="item.username" :value="item.userid">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="角色">
        <el-select v-model="elementCopyInfo.copyRoles" multiple size="mini" placeholder="请选择 角色" @change="updateCopyInfo('copyRoles')">
          <el-option v-for="item in roleOptions" :key="item.roleid" :label="item.rolename" :value="item.roleid" :disabled="item.status === 1">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="动态审批人">
        <el-select v-model="elementCopyInfo.copyUserParam" size="mini" clearable placeholder="请选择 流程参数" @change="updateCopyInfo('copyUserParam')" filterable>
          <el-option v-for="item in envItems" :key="item.envItemId" :label="item.itemKey" :value="item.envItemId">
          </el-option>
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="是否需要已阅">
          <el-radio-group v-model="elementCopyInfo.needRead" @change="updateCopyInfo('needRead')">
            <el-radio label="1">是</el-radio>
            <el-radio label="0">否</el-radio>
          </el-radio-group>
        </el-form-item> -->
    </el-form>
  </div>
</template>

  <script>
import { getEnvItems } from "../../utils";
import Tools from "@/utils/tools.js";
export default {
	name: "ElementCopy",
	props: {
		id: String,
		type: String,
	},
	inject: {
		prefix: "prefix",
		width: "width",
	},
	data() {
		return {
			elementCopyInfo: {},
			formOptions: [],
			envItems: [],
			dynamicForms: [],
			roleOptions: [],
			userOptions: [],
			userOptionsLoading: false,
			userOptionsCopy: [],
			formTypes: [
				{
					id: "formFieldId",
					name: "表单",
				},
				{
					id: "dynamicFormId",
					name: "动态表单",
				},
			],
		};
	},
	watch: {
		id: {
			immediate: true,
			handler(val) {
				val &&
					val.length &&
					this.$nextTick(() => {
						this.resetCopyInfo();
						this.getEnvItemList();
					});
			},
		},
	},
	created() {
		this.getFormList();
		//this.getDynamicForms();
		this.getRoleOptions();
	},
	methods: {
		resetCopyInfo() {
			this.bpmnElement = window?.bpmnInstances?.bpmnElement;
			this.elementCopyInfo = {
				copyFormType: this.bpmnElement.businessObject.copyFormType,
				copyFormKey: this.bpmnElement.businessObject.copyFormKey,
				copyUsers: this.bpmnElement.businessObject.copyUsers ? this.bpmnElement.businessObject.copyUsers.toString().split(",") : [],
				copyRoles: this.bpmnElement.businessObject.copyRoles ? this.bpmnElement.businessObject.copyRoles.toString().split(",") : [],
				copyUserParam: this.bpmnElement.businessObject.copyUserParam,
				needRead: this.bpmnElement.businessObject.needRead ?? "0",
			};

			//处理用户
			Promise.all([this.getUserOptions()]).then((data) => {
				this.userOptionsCopy = this.userOptions.filter((k) => this.elementCopyInfo.copyUsers.indexOf(k.userid) >= 0);
			});
		},
		userOptionsRemoteMethod(query) {
			if (query !== "") {
				this.userOptionsLoading = true;
				setTimeout(() => {
					this.userOptionsLoading = false;
					this.userOptionsCopy = this.userOptions.filter((item) => {
						if (item.username.toLowerCase().indexOf(query.toLowerCase()) > -1) {
							return true;
						} else if (this.elementCopyInfo.copyUsers.indexOf(item.userid) >= 0) {
							return true;
						} else {
							return false;
						}
					});
				}, 200);
			}
		},
		getEnvItemList() {
			getEnvItems().then((rows) => {
				this.envItems = rows;
			});
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
		/** 查询表单列表 */
		getFormList() {
			this.httpUtil
				.ajaxJson({
					url: "wf/formField/list.json",
				})
				.then((res) => {
					this.formOptions = res.rows;
				});
		},
		/**
		 * 查询角色
		 */
		getRoleOptions() {
			return this.httpUtil
				.ajaxJson({
					url: "wf/system/role/list.json",
				})
				.then((response) => {
					this.roleOptions = response.rows;
				});
		},
		getDynamicForms() {
			this.httpUtil
				.comnQuery({
					action: "LowCodeSysVersion.getCurrentVersion",
					errCallback: (reData) => {
						Tools.alert("低代码服务调用出错，无法使用动态表单", "warning");
					},
				})
				.then((res) => {
					if (res.success) {
						let version = res.returndata.version;
						this.httpUtil
							.comnQuery({
								action: "LowCodeConfig.page",
								params: {
									sysVersion: version,
								},
							})
							.then((res) => {
								this.dynamicForms = res.rows;
							});
					}
				});
		},
		updateCopyInfo(key) {
			const attrObj = Object.create(null);
			attrObj[key] = this.elementCopyInfo[key];
			if (key === "copyFormType") {
				window.bpmnInstances.modeling.updateProperties(this.bpmnElement, {
					copyFormType: this.elementCopyInfo[key],
					copyFormKey: null,
				});
			} else {
				window.bpmnInstances.modeling.updateProperties(this.bpmnElement, attrObj);
			}
		},
	},
	beforeDestroy() {
		this.bpmnElement = null;
	},
};
</script>
