<template v-slot:formItem>
	<div
		v-if="uiType == 'element'"
		class="el-form-item"
		:class="[
			{
				'is-error': validateState === 'error',
				'is-required': field && !field.dataAllowblank,
			},
			'el-form-item--small',
		]"
		:style="itemStyle"
	>
		<label class="el-form-item__label" :style="labelStyle" v-if="label || $slots.label">
			<slot name="label">{{ label }}</slot>
		</label>
		<div class="el-form-item__content" :style="contentStyle">
			<slot></slot>
			<transition name="el-zoom-in-top">
				<slot v-if="validateState === 'error'" name="error">
					<div class="el-form-item__error">
						{{ validateMessage }}
					</div>
				</slot>
			</transition>
		</div>
	</div>

	<div v-else class="k-material-from-item" :style="itemStyle">
		<md-field
			v-if="kForm && kForm.dataLabelPosition == 'vertical'"
			:class="{ 'md-invalid': validateState === 'error' }"
		>
			<label>{{ label }}</label>
			<slot></slot>
			<span class="md-error">{{ validateMessage }}</span>
		</md-field>
		<div v-else class="md-layout">
			<label class="k-from-horizontal-label" :style="labelStyle" v-if="label || $slots.label">
				{{ label }}
			</label>
			<div class="redStar"><span v-show="!showRedBar">*</span></div>
			<div class="md-layout-item" :style="inputStyle">
				<md-field
					:mdClearable="clearable"
					@md-clear="clearInput"
					:class="{ 'md-invalid': validateState === 'error', 'md-hide-border': hideBorder }"
				>
					<slot></slot>
					<span class="md-error">{{ validateMessage }}</span>
				</md-field>
			</div>
		</div>
	</div>
</template>
<script>
import emitter from "@/components/k-element/common/k-emitter.js";
import data from "../../../pages/flow/G6Editor/test";
export default {
	name: "KFormItem",
	mixins: [emitter()],
	inject: ["kForm"],
	provide() {
		return {
			KFormItem: this,
		};
	},
	props: {
		label: String,
		// dataAllowblank:{
		//   type:Boolean,
		//   default:true
		// },
		dataLabelWidth: {
			type: String,
		},
		dataInputWidth: {
			type: String,
		},
		dataUi: {
			type: String,
		},
		dataCol: {
			type: Number,
			default: 1,
		},
		dataColor: {
			type: String,
		},
	},
	watch: {
		// 'field.dataAllowblank': (newVal)=>{
		//      this.showRedBar=newVal;
		//   this.$forceUpdate();
		//      console.log(this.showRedBar)
		// }
	},
	created() {
		this.$on("k.form.addField", (field) => {
			this.field = field;
			if (null != field) {
				if (undefined != field.dataAllowblank && null != field.dataAllowblank) {
					this.showRedBar = field.dataAllowblank;
					// 键路径
					this.$watch("field.dataAllowblank", function (newVal, oldVal) {
						this.showRedBar = newVal;
					});
				}
			}
			//设置k-field-text小眼睛是否左移
			if (this.field && this.field.$options.name == "KFieldText" && this.field.dataClearable) {
				this.field.passwordLeft = true;
			}

			if (
				this.field &&
				(this.field.$options.name == "KFieldRadio" || this.field.$options.name == "KFieldCheckbox")
			) {
				this.hideBorder = true;
			}
		});
		/* istanbul ignore next */
		this.$on("k.form.removeField", (field) => {
			this.field = null;
		});
	},
	computed: {
		labelStyle() {
			const ret = {};
			const dataLabelWidth = this.dataLabelWidth || this.kForm.dataLabelWidth;
			if (dataLabelWidth) {
				ret.width = dataLabelWidth;
			}
			return ret;
		},
		contentStyle() {
			const ret = {};
			const label = this.label;
			if (!label && !this.labelWidth) return ret;
			const labelWidth = this.dataLabelWidth || this.kForm.dataLabelWidth;
			ret.marginLeft = labelWidth;
			return ret;
		},
		itemStyle() {
			const ret = {};
			const dataLabelWidth = this.dataLabelWidth || this.kForm.dataLabelWidth;
			let dataLabelWidthInt = parseInt(dataLabelWidth.replace("px", ""));
			const dataInputWidth = this.dataInputWidth || this.kForm.dataInputWidth;
			let dataInputWidthInt = parseInt(dataInputWidth.replace("px", ""));
			if (this.kForm) {
				if (this.kForm.dataUi == "element" || this.kForm.dataLabelPosition == "horizontal") {
					dataInputWidthInt += dataLabelWidthInt;
				}
			}
			dataInputWidthInt = dataInputWidthInt * this.dataCol;
			ret.marginLeft = this.kForm.dataItemMargin;
			ret.marginRight = this.kForm.dataItemMargin;

			if (this.kForm.dataItemMargin) {
				dataInputWidthInt =
					parseInt(this.kForm.dataItemMargin.replace("px", "")) * 2 * (this.dataCol - 1) + dataInputWidthInt;
			}
			// ret.width = dataInputWidthInt + "px";
			if (this.dataColor) {
				//为操作日志页面的隐藏所建立
				ret.color = this.dataColor;
			}
			// if(this.label === ""){//为操作日志页面的隐藏所建立
			//   ret.visibility = 'hidden';
			// }
			return ret;
		},
		inputStyle() {
			const ret = {};
			const dataInputWidth = this.dataInputWidth || this.kForm.dataInputWidth;
			let dataInputWidthInt = parseInt(dataInputWidth.replace("px", ""));
			ret.width = dataInputWidthInt + "px";
			return ret;
		},
		uiType() {
			let ui = "material";

			if (this.kForm) {
				ui = this.kForm.dataUi;
			}

			if (this.dataUi) {
				ui = this.dataUi;
			}
			return ui;
		},
		clearable() {
			return (
				this.field &&
				(this.field.dataClearable === true || this.field.dataClearable === "true") &&
				this.field.$options.name != "KFieldRadio" &&
				this.field.$options.name != "KFieldDisplay"
			);
		},
	},
	data() {
		return {
			field: null, //当前输入框对象
			validateState: "validating",
			validateMessage: "",
			hideBorder: false,
			showRedBar: false,
		};
	},
	methods: {
		//校验函数
		validate() {
			if (this.field) {
				let re = this.field.validate();
				if (re === true) {
					this.validateState = "";
					this.validateMessage = "";
				} else {
					this.validateState = "error";
					this.validateMessage = re || "数据格式错误";
				}
				return re === true;
			}
			return true;
		},
		//重置函数
		reset() {
			this.validateState = "";
			this.validateMessage = "";
			if (this.field) {
				this.field.reset();
			}
		},
		clearInput() {
			if (this.field) {
				this.field.clear();
			}
		},
	},
	mounted() {
		this.dispatch("KForm", "k.form.addFormItem", this);
	},
	beforeDestroy() {
		this.dispatch("KForm", "k.form.removeFormItem", this);
	},
};
</script>

<style lang="scss">
@import "./k-form-item.scss";
.redStar {
	width: 20px;
	display: flex;
	justify-content: center;
	align-items: center;
	padding-top: 10px;
	padding-right: 6px;
	color: red;
}
.md-layout-item {
	padding-left: 0px;
}
.k-from-horizontal-label {
	padding-right: 0px;
}

.md-field.md-required label:after {
	position: absolute;
	top: 0px;
	right: 0;
	content: "" !important;
	line-height: 0em;
	vertical-align: top;
}

.md-field {
	::v-deep .vue-treeselect__control {
		border: none !important;
	}
	::v-deep .vue-treeselect__x-container {
		display: none !important;
	}
}
//设置
.md-field .md-input-action {
	margin: 0;
	position: absolute;
	top: 50%;
	/* top: 16px; */
	right: 0;
	transition: 0.4s cubic-bezier(0.4, 0, 0.2, 1);
	transform: translate(-0%, -80%);
}
.el-form-item__error {
	z-index: 1;
}
</style>
