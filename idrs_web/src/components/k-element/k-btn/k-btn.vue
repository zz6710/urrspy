<template>
	<el-tooltip
		v-if="dataDescript && isShow"
		:disabled="dataTooltipDisabled"
		class="item"
		effect="dark"
		:content="dataDescript"
		placement="bottom"
	>
		<md-button
			@click="handle($event)"
			:disabled="(loading && loadDisabled) || dataDisabled === true || dataDisabled === 'true'"
			:class="[disabledGrey ? 'md-disabled' : '']"
		>
			<!-- 'md-sm', -->
			<i v-show="loading" class="el-icon-loading"></i>
			<slot> </slot>
		</md-button>
	</el-tooltip>
	<md-button
		v-else-if="isShow"
		@click="handle($event)"
		:disabled="(loading && loadDisabled) || dataDisabled === true || dataDisabled === 'true'"
		:class="[disabledGrey ? 'md-disabled' : '', smallSize ? 'md-small-size' : '']"
	>
		<i v-show="loading" class="el-icon-loading"></i>
		<slot> </slot>
		<k-popup
			v-if="authOpCheck"
			data-title="授权操作"
			ref="authOpCheckPopup"
			:dataAppendToBody="true"
			@data-closed="authOpCheckClosed"
		>
			<k-form :data-col="1">
				<k-form-item label="登录名">
					<k-field-text v-model="authOpCheckForm.loginname"></k-field-text>
				</k-form-item>
				<k-form-item label="密码">
					<k-field-text data-type="password" v-model="authOpCheckForm.password"></k-field-text>
				</k-form-item>
				<k-form-footer>
					<k-btn class="btn-custom-primary" :data-model="authOpCheckForm" :data-handler="submitAuthRoleCheck">
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确认
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE">
						<md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
					</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>
	</md-button>
</template>

<script>
import emitter from "@/components/k-element/common/k-emitter.js";
import MD5 from "@/frame/md5.js";
import Tools from "@/utils/tools.js";
import auth from "@/utils/auth.js";
import { Notification } from "element-ui";

export default {
	name: "KBtn",
	mixins: [emitter()],
	props: {
		dataTarget: {
			type: String,
		},
		dataParams: {
			type: [Object, String],
		},
		dataExportParams: {
			type: Object,
			default: () => {}
		},
		dataExportForm: {
			type: String,
			default: ''
		},
		dataDisabled: {
			type: [Boolean, String],
			default: false,
		},
		dataDisabledGrey: {
			type: Boolean,
			default: true,
		},
		dataFunctype: {
			type: String,
		},
		dataAction: {
			type: String,
		},
		dataGraphql: {
			type: String,
		},
		dataUrl: {
			type: String,
		},
		dataAfterSuccess: {
			type: Function,
		},
		dataHandler: {
			type: Function,
		},
		dataConfirm: {
			type: [Boolean, String],
			default: false,
		},
		dataFrom: {
			type: String,
		},
		dataModel: {},
		dataDescript: {
			type: String,
		},
		dataValidateForm: {
			type: Boolean,
			default: true,
		},
		dataExportName: {
			type: String,
			default: undefined,
		},
		reportDate: {
			type: String,
			default: undefined,
		},
		custNo: {
			type: String,
			default: undefined,
		},
		dataDownloadName: {
			type: String,
		},
		dataClosePopup: {
			type: Boolean,
			default: true,
		},
		smallSize: {
			type: [Boolean, String],
			default: false,
		},
		dataContentType: {
			type: String,
			default: "form",
		},
		dataTooltipDisabled: {
			type: Boolean,
			default: false,
		},
		dataExportDict: {
			type: String,
			default: "",
		},
		dataExcelTemplate: {
			type: String,
			default: "",
		},
		dataExcelStartLine: {
			type: String,
			default: "",
		},
		dataExcelStartCol: {
			type: String,
			default: "",
		},
		dataTemplateName: {
			type: String,
			default: "",
		},
		dataExportType: {
			type: String,
			default: "",
		},
		handleBefore: {
			type: Function
		},
		loadDisabled: {
			type: Boolean,
			default: true
		},
		loadingTip: {
			type: String,
			default: "正在导入中，请稍后重试！"
		}
	},
	data() {
		return {
			form: null, //表单对象
			loading: false,
			oldData: "",
			isShow: true,
			mdSVGLoader: null,
			authOpCheck: false,
			authOpCheckForm: {},
			iconList: [],
			lastSubmitParams: {},
			notifyBtn: null
		};
	},
	computed: {
		disabledGrey: function () {
			return this.dataDisabledGrey && (this.dataDisabled === true || this.dataDisabled === "true");
		},
		justIcon: function () {
			return this.$el.className.indexOf("md-simple") > -1 && this.$el.className.indexOf("md-just-icon") > -1;
		},
	},
	created() {
		this.$on("k.form.addForm", (form) => {
			if (form) {
				this.form = form;
			}
		});
		if (this.dataModel) {
			this.oldData = Tools.json2str(this.dataModel);
		}

		//判断权限是否显示或者隐藏
		if (this.dataAction) {
			this.isShow = auth.check(this.dataAction);
		}
	},
	mounted() {
		if (this.disabledGrey) {
			let mdSVGLoader = this.getChildrens("MdSVGLoader", this.$children);
			if (mdSVGLoader && mdSVGLoader.length === 1) {
				this.mdSVGLoader = mdSVGLoader[0];

				this.$watch("mdSVGLoader.html", function (newVal, oldVal) {
					if (newVal && newVal != oldVal) {
						let fill;
						if (this.justIcon) {
							// 置灰
							fill = 'fill="#6c6868"';
						} else {
							// 转为白色
							fill = 'fill="#FFFFFF"';
						}
						this.mdSVGLoader.html = newVal.replace(/fill=[\"\'].*[\"\']/, fill);
					}
				});
			}
		}
	},
	methods: {
		handle($event) {
			if (this.loading && !this.loadDisabled) {
				Tools.alertTime(this.loadingTip || "正在导入中，请稍后重试！","success", 0);
				return
			}
			if (this.handleBefore) {
				if (!this.handleBefore()) {
					return
				}
			}
			this.$emit("click");
			let params = {
				btnop: true,
			};
			if (this.dataParams && typeof this.dataParams === "string") {
				params = Object.assign(params, Tools.str2Json(this.dataParams));
			}

			if (this.dataParams && Object.prototype.toString.call(this.dataParams) === "[object Object]") {
				params = Object.assign(params, this.dataParams);
			}

			//如果默认表单对象不存在，尝试获取指定表单
			if (!this.form) {
				if (this.dataFrom) {
					this.form = this.getParentRef(this.dataFrom);
				}
			}

			if (this.dataModel) {
				//如果定义data-model，则获取引用对象
				params = Object.assign(params, this.dataModel);
			}

			// 从表格行获取对象
			let kGrid = this.getParent("KGrid");
			if (kGrid) {
				let formerData = kGrid.getRowData($event);
				this.oldData = Tools.json2str(formerData);
				params = Object.assign(params, formerData);
			}

			var children = $event.currentTarget.childNodes[0].childNodes[0].childNodes;
			this.iconList = [];
			for (var i = 0; i < children.length; i++) {
				if (children[i].nodeName == "I") {
					this.iconList.push(children[i]);
				}
			}

			//执行data-hanlder事件
			if (this.dataHandler) {
				let re = this.dataHandler(params);
				if (re === false) {
					//不做任何处理
					return;
				}
			}

			if (this.oldData && this.dataContentType !== "json") {
				params.oldData = this.oldData;
			}

			if (this.dataFunctype == "RESET") {
				//重置表单
				if (this.form) {
					this.form.reset();
				}
			} else if (this.dataFunctype == "PAGE") {
				//打开新页面
				if (!this.dataTarget) {
					console.error("为定义data-target属性");
					return;
				}
				this.$router.push({
					path: this.dataTarget,
					query: params,
				});
			} else if (this.dataFunctype == "EXPORT") {
				let formParams = {};
				if (this.dataExportForm) {
					let target = this.getParentRef(this.dataExportForm);
					const valid = target.$refs.searchForm.validate();
					if (!valid) {
						return
					}
					Object.keys(target.value).forEach(item=>{
						if (target.value[item]) {
							formParams[item] = target.value[item]
						}
					})
				}
				//导出表格
				if (this.dataTarget) {
					let target = this.getParentRef(this.dataTarget);
					//判断dataTarget是不是表格，如果是表格，则刷新表格
					if (target && target.$options.name == "KGrid") {
						let queryParams = target.buildQueryParams();
						if (queryParams.hasOwnProperty("start")) {
							delete queryParams["start"];
							delete queryParams["limit"];
						}
						Object.assign(queryParams, formParams)
						let headers = "";
						let gridColumnOptions = target.gridColumnOptions;
						let unToDict = [];
						let dataExportDict = this.dataExportDict;
						gridColumnOptions.forEach((o) => {
							if (!o.dataHeader || !o.dataName) {
								return;
							}
							//为什么要删掉不可导出限制，此处处理字段是否需要导出----modify by ouyifan
							if (o.dataExport && (o.dataExport === "false" || o.dataExport === false)) {
								return;
							}
							headers = headers + o.dataHeader + ":";
							headers = headers + o.dataName + ":";
							headers = headers + (o.dataType ? o.dataType : "") + ":";
							headers = headers + (o.dataDict ? o.dataDict : "") + ":";
							headers = headers + ",";
							if (o["dataExportDict"] == "false" || o["dataExportDict"] == false) {
								unToDict.push(o["dataName"]);
							}
						});
						if (this.dataParams) {
							queryParams = Object.assign(queryParams, this.dataParams)
						}
						if (this.dataModel) {
							//如果定义data-model，则获取引用对象
							queryParams = Object.assign(queryParams, this.dataModel);
						}
						let params = {
							headers: headers,
							action_params: JSON.stringify(queryParams),
							action: target.dataAction || target.dataUrl,
							dataExportDict: this.dataExportDict,
							dataExcelTemplate: this.dataExcelTemplate,
							dataExcelStartLine:  this.dataExcelStartLine,
							dataExcelStartCol:this.dataExcelStartCol,
							dataTemplateName: this.dataTemplateName,
							dataExportType:this.dataExportType,
							unToDict: unToDict.join(","),
							type: target.dataAction ? 0 : 1,
							dataExportName: this.dataExportName,
							reportDate: this.reportDate,
							custNo: this.custNo
						};
						this.setIconStyle(0);
						// 如果存在data-action则走action接口
						if (this.dataAction) {
							this.httpUtil
								.comnUpdate({
									action: this.dataAction,
									params: params,
									mask: false,
								})
								.then((data) => {
									this.afterSuccess(data);
								});
						} else {
							this.httpUtil.download(
								{
									url: "excel/download.json",
									params: params,
									callback: (a) => {
										this.setIconStyle(1);
										this.$emit("downSuccess", a)
										//关闭弹窗
										let kPopup = this.getParent("KPopup");
										if (kPopup) {
											kPopup.close();
										}
									},
								},
								this.dataExportName
							);
						}
					} else {
						console.error("表格导出需配置操作的表格引用");
					}
				}
			} else if (this.dataFunctype == "DOWNLOAD") {
				//下载文件
				//校验表单
				if ((this.dataValidateForm == true || this.dataValidateForm == "true") && this.form) {
					let re = this.form.validate();
					if (re === false) {
						return;
					}
				}
				this.setIconStyle(0);
				this.httpUtil.download(
					{
						url: this.dataUrl,
						params: params,
						callback: () => {
							this.setIconStyle(1);
						},
					},
					this.dataDownloadName
				);
			} else if (this.dataFunctype == "POPUP") {
				//弹窗
				setTimeout(() => {
					let target = this.getParentRef(this.dataTarget);
					//判断dataTarget是不是弹窗，如果是弹窗，则关闭弹窗
					if (target && target.$options.name == "KPopup") {
						target.popup();
					}
				}, 50);
			} else if (this.dataFunctype == "CLOSE") {
				//关闭弹窗
				let kPopup = this.getParent("KPopup");
				if (kPopup) {
					kPopup.close();
				}
			} else if (this.dataFunctype == "SUBMIT") {
				//提交

				//校验表单
				if ((this.dataValidateForm == true || this.dataValidateForm == "true") && this.form) {
					let re = this.form.validate();
					if (re === false) {
						return;
					}
				}

				if (this.dataConfirm === "true" || this.dataConfirm === true) {
					let _dataDescript = this.dataDescript ? this.dataDescript : "执行该操作";
					this.$confirm("确认" + _dataDescript + "吗？", "操作提示", {
						confirmButtonText: "确定",
						cancelButtonText: "取消",
						type: "warning",
						beforeClose: (action, instance, done) => {
							if (action === "confirm") {
								instance.confirmButtonLoading = true;
								instance.confirmButtonText = "执行中...";
								this.submitHandler(params, () => {
									done();
									instance.confirmButtonLoading = false;
								});
							} else {
								done();
							}
						},
					}).catch(() => {});
				} else {
					this.submitHandler(params);
				}
			}
		},
		handleExport(dateParams) {
		  	const formParams = {}
			Object.keys(dateParams).forEach(item=>{
				if (dateParams[item]) {
				formParams[item] = dateParams[item]
				}
			})
			//导出表格
				if (this.dataTarget) {
					let target = this.getParentRef(this.dataTarget);
					//判断dataTarget是不是表格，如果是表格，则刷新表格
					if (target && target.$options.name == "KGrid") {
						let queryParams = target.buildQueryParams();
						if (queryParams.hasOwnProperty("start")) {
							delete queryParams["start"];
							delete queryParams["limit"];
						}
						let headers = "";
						let gridColumnOptions = target.gridColumnOptions;
						let unToDict = [];
						let dataExportDict = this.dataExportDict;
						gridColumnOptions.forEach((o) => {
							if (!o.dataHeader || !o.dataName) {
								return;
							}
							//为什么要删掉不可导出限制，此处处理字段是否需要导出----modify by ouyifan
							if (o.dataExport && (o.dataExport === "false" || o.dataExport === false)) {
								return;
							}
							headers = headers + o.dataHeader + ":";
							headers = headers + o.dataName + ":";
							headers = headers + (o.dataType ? o.dataType : "") + ":";
							headers = headers + (o.dataDict ? o.dataDict : "") + ":";
							headers = headers + ",";
							if (o["dataExportDict"] == "false" || o["dataExportDict"] == false) {
								unToDict.push(o["dataName"]);
							}
						});
						let params = {
							headers: headers,
							action_params: JSON.stringify(formParams),
							action: target.dataAction || target.dataUrl,
							dataExportDict: this.dataExportDict,
							unToDict: unToDict.join(","),
							type: target.dataAction ? 0 : 1,
							dataExportName: this.dataExportName,	
                            dataExcelTemplate: this.dataExcelTemplate,
                            dataExcelStartLine:  this.dataExcelStartLine,
                            dataExcelStartCol:this.dataExcelStartCol,
                            dataTemplateName: this.dataTemplateName,
                            dataExportType:this.dataExportType,	
							reportDate: this.reportDate,
							custNo: this.custNo
						};
						this.setIconStyle(0);
						// 如果存在data-action则走action接口
						if (this.dataAction) {
							this.httpUtil
								.comnUpdate({
									action: this.dataAction,
									params: params,
									mask: false,
								})
								.then((data) => {
									this.afterSuccess(data);
								});
						} else {
							this.httpUtil.download(
								{
									url: "excel/download.json",
									params: params,
									callback: (a) => {
										this.setIconStyle(1);
										this.$emit("downSuccess", a)
									},
								},
								this.dataExportName
							);
						}
					} else {
						console.error("表格导出需配置操作的表格引用");
					}
				}
		},
		afterSuccess() {
			this.setIconStyle(1);
			if (this.dataTarget) {
				let target = this.getParentRef(this.dataTarget);
				if (target && target.$options.name == "KGrid") {
					target._load(target.getCachedParams());
				}
			}
			if (this.dataClosePopup) {
				let kPopup = this.getParent("KPopup");
				if (kPopup) {
					kPopup.close();
				}
			}
		},
		callback(data) {
			this.setIconStyle(1);
			if (data.authOpcheck && data.authOpcheck) {
				this.authOpCheck = true;
				this.$nextTick(() => {
					this.$refs.authOpCheckPopup.popup();
				});
			}
		},
		//按钮提交操作
		submitHandler(params, callback) {
			this.setIconStyle(0);
			this.lastSubmitParams = params;
			if (this.dataAction) {
				//通过Action提交
				this.submitAction(params, callback);
			} else if (this.dataGraphql) {
				//通过Graphql提交
				this.submitGraphql(params, callback);
			} else if (this.dataUrl) {
				//通过url提交
				this.submitUrl(params, callback);
			} else {
				this.setIconStyle(1);
			}
		},
		submitAction(params, callback) {
			this.httpUtil
				.comnUpdate({
					action: this.dataAction,
					params: params,
					mask: false,
					callback: () => {
						if (callback) {
							callback();
						}
						this.callback(params);
					},
					dataAfterSuccess: this.dataAfterSuccess,
				})
				.then((data) => {
					this.afterSuccess(data);
				});
		},
		submitGraphql(params, callback) {
			this.httpUtil
				.graphqlUpdate({
					action: this.dataGraphql,
					params: params,
					mask: false,
					callback: () => {
						if (callback) {
							callback();
						}
						this.callback(params);
					},
					dataAfterSuccess: this.dataAfterSuccess,
				})
				.then((data) => {
					this.afterSuccess(data);
				});
		},
		submitUrl(params, callback) {
			if (this.dataContentType === "json") {
				this.httpUtil
					.ajaxJson({
						url: this.dataUrl,
						params: params,
						mask: false,
						successAlert: true,
						callback: () => {
							if (callback) {
								callback();
							}
							this.callback(params);
						},
						dataAfterSuccess: this.dataAfterSuccess,
					})
					.then((data) => {
						this.afterSuccess(data);
					});
			} else {
				this.httpUtil
					.update({
						url: this.dataUrl,
						params: params,
						mask: false,
						callback: () => {
							if (callback) {
								callback();
							}
							this.callback(params);
						},
						dataAfterSuccess: this.dataAfterSuccess,
					})
					.then((data) => {
						this.afterSuccess(data);
					});
			}
		},
		submitAuthRoleCheck(params) {
			let loginname = params.loginname;
			let password = params.password;
			this.lastSubmitParams["auth-role-check-loginname"] = loginname;
			this.lastSubmitParams["auth-role-check-passwd"] = MD5.MD5(loginname + password);
			this.submitHandler(this.lastSubmitParams);
		},
		setIconStyle(type) {
			if (type == 0) {
				this.loading = true;
				if (this.iconList.length > 1) {
					this.iconList[1].setAttribute("style", "display:none;");
					if(!this.loadDisabled) {
						this.notifyBtn = Notification({
							title: "提示",
							message: this.$route.name + "：" + this.loadingTip,
							duration: 0,
							position: "bottom-right",
							type: "info"
						});
					}
				}
			} else {
				this.loading = false;
				if (this.iconList.length > 1) {
					this.iconList[1].setAttribute("style", "display:inline;");
					if(!this.loadDisabled) {
						this.notifyBtn.close();
					}
				}
			}
		},
		authOpCheckClosed() {
			this.authOpCheck = false;
			this.authOpCheckForm = {};
		},
		setLoading(flag) {
			this.loading = flag;
		},
	},
};
</script>
