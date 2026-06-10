<template>
	<div class="widget-param" v-if="element && element.key" :key="element.key">
		<template v-if="element.inForm">
			<el-divider content-position="center">k-form-item</el-divider>
			<KFormItemParam :element="element.formItem"></KFormItemParam>
		</template>

		<el-divider content-position="center">组件属性</el-divider>
		<!-- <SelectOptions v-model="element.selectOptions" :type="element.type"></SelectOptions> -->
		<el-form ref="form" :model="element" label-position="top" label-width="80px">
			<el-form-item label="组件id">
				<el-input v-model="element.options.formId" placeholder="请输入表单Id">
					<el-button slot="append" @click="openInherit">获取</el-button>
					<el-button slot="append" @click="clearContent">清空</el-button>
				</el-input>
			</el-form-item>
			<el-form-item label="组件宽度">
				<el-input v-model="element.style.width" />
			</el-form-item>
			<el-form-item label="组件高度">
				<el-input v-model="element.style.height" />
			</el-form-item>

		</el-form>
	</div>
</template>

<script>
import Draggable from "vuedraggable";
import SelectOptions from "../base/SelectOptions.vue";
import FormCodeEditor from "../base/FormCodeEditor.vue";
import FormCustomAttr from "../base/FormCustomAttr.vue";
import KFormItemParam from "./KFormItemParam.vue";
import eventBus from '@/utils/eventBus';
import Tools from '@/utils/tools.js';
import { findConfigById } from "@/pages/design/utils/getForm"
export default {
	components: {
		Draggable,
		SelectOptions,
		FormCodeEditor,
		FormCustomAttr,
		KFormItemParam,
	},
	props: ["element", "selectWidget"],
	inject: ["kFormDesign"],
	data() {
		return {};
	},
	created() {},
	mounted() {
		// document.body.ondrop = function (event) {
		// 	let isFirefox = navigator.userAgent.toLowerCase().indexOf("firefox") > -1;
		// 	if (isFirefox) {
		// 		event.preventDefault();
		// 		event.stopPropagation();
		// 	}
		// };
	},
	methods: {
		openInherit() {
      // console.log(this.element.options.formId, '===');
			if (this.element.options.formId) {
        findConfigById(this.element.options.formId).then((res) => {
          // this.$emit('parmaFunc', {
          //   data: res,
          //   element: this.element
          // })
					eventBus.$emit('kCustomerParma', {
            data: res,
            element: this.element
          })
        })
			} else {
				Tools.alert("请输入表单Id", "danger");
				return;
			}
		},
    clearContent() {
      this.element.options.formId = ''
      // this.$emit('parmaFunc', {
      //   data: data.returndata,
      //   element: this.element
      // })

    },
	},
	watch: {},
};
</script>

<style lang="scss" scoped>
//必须要加上“<style lang="scss" scoped>，否则引入失败（提示保存）
@import "../../styles/cover.scss";
@import "../../styles/index.scss";
</style>
