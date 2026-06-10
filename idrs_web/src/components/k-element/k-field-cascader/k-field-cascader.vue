<template>
	<el-cascader
		v-if="dataUiType !== 'panel'"
		ref="cascader"
		:value="value"
		:options="options"
		:props="props"
		:placeholder="dataPlaceholder"
		:disabled="dataDisabled === true || dataDisabled === 'true'"
		:show-all-levels="dataShowAllLevels === true || dataShowAllLevels === 'true'"
		:collapse-tags="dataCollapseTags === true || dataCollapseTags === 'true'"
		:separator="dataValueSeparator"
		:filterable="dataFileterable === true || dataFileterable === 'true'"
		:filter-method="dataFilterMethod"
		:debounce="dataDebounce"
		:before-filter="dataBeforeFilter"
		:popper-class="dataPopperClass"
		clearable
		@input="handleInput"
		@change="handleClickChange"
		@expand-change="handleExpandChange"
		@focus="handleFocus"
		@visible-change="handleVisibleChange"
		@blur="handleBlur"
		@remove-tag="handleRemoveTag"
	>
		<template v-if="dataShowNum === true || dataShowNum === 'true'" slot-scope="{ node, data }">
			<span>{{ data[props.label] }}</span>
			<span v-if="!node.isLeaf"> ({{ data[props.children].length }}) </span>
		</template>
	</el-cascader>
	<el-cascader-panel
		v-else
		:value="value"
		:options="options"
		:props="props"
		@change="handleChange"
		@expand-change="handleExpandChange"
	>
		<template v-if="dataShowNum === true || dataShowNum === 'true'" slot-scope="{ node, data }">
			<span>{{ data[props.label] }}</span>
			<span v-if="!node.isLeaf"> ({{ data[props.children].length }}) </span>
		</template>
	</el-cascader-panel>
</template>

<script>
import props from "@/components/k-element/common/k-field-props.js";
import event from "@/components/k-element/common/k-field-event.js";
import treent from "@/components/k-element/common/k-field-treent.js";
import emitter from "@/components/k-element/common/k-emitter.js";
import Tools from "@/utils/tools.js";
export default {
	name: "KFieldCascader",
	componentName: "KFieldCascader",
	mixins: [props(), event(), emitter(), treent],
	props: {
		dataUiType: {
			type: String,
			default: "cascader",
		},
		value: {
			type: [Array, String, Number],
			default: () => [],
		},
		dataSize: {
			type: String,
			default: "mini",
		},
		dataShowAllLevels: {
			type: [Boolean, String],
			default: undefined,
		},
		dataCollapseTags: {
			type: [Boolean, String],
			default: undefined,
		},
		dataValueSeparator: {
			type: String,
			default: "/",
		},
		dataFileterable: {
			type: [Boolean, String],
			default: undefined,
		},
		dataFilterMethod: {
			type: Function,
			default: undefined,
		},
		dataDebounce: {
			type: Number,
			default: undefined,
		},
		dataBeforeFilter: {
			type: Function,
			default: undefined,
		},
		dataPopperClass: {
			type: String,
			default: undefined,
		},
		dataShowNum: {
			type: [Boolean, String],
			default: undefined,
		},
		dataDisplaySeparator: {
			type: String,
			default: "-",
		},
		// 显示字段，可同时多个
		dataDisplayField: {
			type: String,
			default: undefined,
		},
		// 值字段
		dataValueField: {
			type: String,
			default: undefined,
		},
		// 字段下标
		dataDisplayChild: {
			type: String,
			default: undefined,
		},
		// 父节点
		dataParentField: {
			type: String,
			default: undefined,
		},
		//上级级别节点
		dataDiffcondition: {
			type: String,
			default: undefined,
		},
		dataOnBeforeload: {
			type: Function,
			default: undefined,
		},
		dataOnAfterload: {
			type: Function,
			default: undefined,
		},
		dataExpandTrigger: {
			type: String,
			default: "click",
		},
		dataMultiple: {
			type: [Boolean, String],
			default: undefined,
		},
		dataCheckStrictly: {
			type: [Boolean, String],
			default: false,
		},
		dataEmitPath: {
			type: [Boolean, String],
			default: undefined,
		},
		dataLazy: {
			type: [Boolean, String],
			default: undefined,
		},
		dataLazyLoad: {
			type: Function,
			default: undefined,
		},
		dataOnObject: {
			type: [Boolean, String],
			default: undefined,
		},
	},
	data() {
		return {
			options: [],
			params: {},
			graphql: String,
			props: {},
			dataType: false,
		};
	},
	created() {
		// 配置验证
		if (
			(this.dataAction || this.dataGraphql) &&
			(this.dataLazy === true || this.dataLazy === "true") &&
			!this.dataParentField
		) {
			console.error("没有定义data-parent-field,数据加载失败!");
			return;
		}
		if (this.dataParams) {
			this.params =
				typeof this.dataParams === "string"
					? Tools.str2Json(this.dataParams)
					: this.dataParams === undefined
					? {}
					: this.dataParams;
		}

		if (
			(this.dataAction || this.dataGraphql) &&
			(this.dataLazy === true || this.dataLazy === "true") &&
			!this.params.hasOwnProperty(this.dataParentField)
		) {
			console.error("data-params中没有定义data-parent-field属性，数据加载失败!");
			return;
		}

		if (
			(this.dataAction || this.dataGraphql) &&
			!(this.dataLazy === true || this.dataLazy === "true") &&
			!this.dataDiffcondition
		) {
			console.error("没有定义data-diffcondition参数,加载失败!");
			return;
		}

		if (this.dataAction && !(this.dataLazy === true || this.dataLazy === "true") && this.dataDiffcondition) {
			this.params.diffcondition = this.dataDiffcondition;
		}

		this.props.expandTrigger = this.dataExpandTrigger;
		this.props.multiple = this.dataMultiple === true || this.dataMultiple === "true";
		this.props.checkStrictly = this.dataCheckStrictly === true || this.dataCheckStrictly === "true";
		this.props.emitPath = this.dataEmitPath === true || this.dataEmitPath === "true";
		this.props.lazy = this.dataLazy === true || this.dataLazy === "true";
		this.props.lazyLoad = !this.dataLazyLoad ? this.showNode : this.dataLazyLoad;
		this.props.value = !this.dataValueField ? "value" : this.dataValueField;
		this.props.label = !this.dataDisplayField ? "label" : this.dataDisplayField;
		this.props.children = !this.dataDisplayChild ? "children" : this.dataDisplayChild;
		this.props.disabled = "disabled";

		this.dataType = this.dataData && this.dataValueField && this.dataDisplayField && this.dataDisplayChild;
		if (this.dataAutoLoad === true || this.dataAutoLoad === "true") {
			this.loadData();
		}
	},
	methods: {
		loadData() {
			if (this.dataOnBeforeload) {
				this.dataOnBeforeload(this.params);
			}
			this.options = [];
			if (this.dataAction) {
				// 通过Action加载
				this.props.lazy ? this.loadActionData() : this.loadActionDataTree();
			} else if (this.dataGraphql) {
				// 通过Graphql加载
				this.graphql = this.props.lazy
					? this.dataGraphql
					: this.getGraphqlUrl(this.dataGraphql, this.dataDiffcondition);
				this.loadGraphqlData();
			} else if (this.dataData) {
				// 直接指定源数据
				this.options = this.dataType ? this.handleData(this.dataData) : this.dataData;
			} else {
				console.error("不支持的数据获取方式，请检查k-field-cascader的元素属性配置");
			}
			if (this.dataOnAfterload) {
				this.dataOnAfterload(this.options);
			}
		},
		handleVisibleChange(value) {
			this.$emit("data-on-visible-change", value);
		},
		handleExpandChange(value) {
			this.$emit("data-on-expand-change", value);
		},
		handleRemoveTag(value) {
			this.$refs.cascader.computePresentContent();
			this.$emit("data-on-remove-tag", value);
		},
		handleClickChange(value) {
			if (this.dataOnObject === true || this.dataOnObject === "true") {
				this.handleChange(this.$refs.cascader.getCheckedNodes(true));
			} else {
				this.handleChange(value);
			}
		},
	},
};
</script>

<style lang="scss">
.el-cascader-panel .el-radio {
	width: 100%;
	height: 100%;
	z-index: 10;
	position: absolute;
	top: 10px;
	right: -10px;
}
</style>
