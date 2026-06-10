<template>
	<!-- <div
		class="widget-view kCustomer"
		:key="element.key"
		@click.stop="kFormDesign.findComponentByKey(element.key)"
		:class="{ active: selectWidget.key == element.key }"
		:style="{ width: element.options.width, height: element.options.height }"
	> -->
	<Shape class="kCustomerWarp" :element="element" :isAbsLayout="isAbsLayout" :style="kFormDesign.getStyle(element.style)">
		<!-- <custom-canvas-abs v-if="element.list[0].position && element.list[0].position == 'absolute'" ref="widgetForm2" :data="element.list[0]" ></custom-canvas-abs> -->
		<!-- <custom-canvas nvas v-if="element.list[0]" ref="widgetForm" :data="element.list[0]" ></custom-canvas> -->
		<div class="kCustomer" >
      <CodePreview v-if="code" :source="code"></CodePreview>
    </div>

		<!-- <div class="widget-view-action" v-if="selectWidget.key == element.key">
			<i class="iconfont icon-trash" @click.stop="kFormDesign.deleteComponentByKey(element.key)"></i>
		</div>
		<div class="widget-view-drag" v-if="selectWidget.key == element.key && !absType">
			<i class="iconfont icon-drag drag-widget"></i>
		</div>
		<div class="widget-view-drag" v-if="selectWidget.key == element.key && absType" @mousedown="mousedownHandler">
			<i class="iconfont icon-drag drag-widget"></i>
		</div> -->
	</Shape>
	<!-- </div> -->
</template>

<script>
import Shape from "@/pages/design/components/base/Shape.vue";
import { parseOptions } from "./componentsConfig";
import optionMinix from "./minix";
import Tools from "@/utils/tools";
import CodePreview from "@/pages/design/code-viewer/src/code-preview.vue";
import generateCode from "@/pages/design/utils/generateCode.js";
import eventBus from '@/utils/eventBus';

export default {
	components: {
		// Draggable,
		// GridLayout,
		// KFormItemComponent,
		// KBtnComponent,
		CodePreview,
		Shape,
	},
	props: {
		element: {
			type: Object,
		},
		isAbsLayout: {
			type: Boolean,
			default: false,
		},
	},
	inject: ["kFormDesign"],
	mixins: [optionMinix],
	data() {
		return {
			// options: {}
			moveObj: {
				startMove: false,
				offset: {
					x: 0,
					y: 0,
				},
			},
			code: null,
		};
	},
	created () {
    console.log('kcustomer create...',this.element);
		eventBus.$on('kCustomerParma', (obj) => {
			let { data, element } = obj;
			if(element.key == this.element.key) {
				// console.log(data);
				let cunstomNum = -1;
        let jsonData = data.json && JSON.parse(data.json)

        try {
          // 异常处理 （处理 k-customer 嵌套问题）
          if(jsonData[0].list.length > 0) {
            jsonData[0].list.forEach(item => {
              if(item.type == 'k-customer') {
                setTimeout(() => {Tools.alert('渲染失败，暂不支持自定义控件嵌套 ', "danger");}, 500)
                throw new Error('渲染失败，暂不支持自定义控件嵌套 ')
              }
            })
          }

          // 正常获取数据
					// this.$set(this.element, 'list', {...jsonData})
					this.element.list = jsonData
					// console.log(this.element, jsonData);
					this.setGenerateCode();
          // this.pageList[0].list.forEach(item => {
          //   if(item.type == 'k-customer') cunstomNum ++     // 处理多个k-customer
          //   if(item.key == element.key) {
          //     item.list = jsonData
          //     this.$refs.widgetForm.$refs.KCustomer[cunstomNum].setGenerateCode()
          //   }
          // })

        } catch (error) {
        }
			}



		})
    eventBus.$on('kCustomerGetData',()=> {
      this.setGenerateCode()
    })
  },
	mounted() {},
	methods: {
		async setGenerateCode() {
			this.code = "";
			if (this.element.list && this.element.list[0] && this.element.list[0].list.length > 0) {
				this.code = await generateCode(this.element.list);
				console.log(this.code);
			}
		},
		// 表单添加按钮
		handleBtnAdd: function ($event) {
			const newIndex = $event.newIndex;
			const to = $event.to;

			//为拖拽到容器的元素添加唯一 key
			const key = Date.parse(new Date()) + "_" + Math.ceil(Math.random() * 99999);
			this.$set(this.element.btns, newIndex, {
				...this.element.btns[newIndex],
				options: parseOptions(this.element.btns[newIndex].type),
				key,
				inForm: true,
				selectOptions: [],
				events: [],
				methods: [],
				customAttrs: [],
			});
			this.kFormDesign.widgetFormSelect = this.element.btns[newIndex];
		},
	},
};
</script>

<style lang="scss" scoped>
//必须要加上“<style lang="scss" scoped>，否则引入失败（提示保存）
// @import "../styles/cover.scss";
// @import "../styles/index.scss";
.kCustomerWarp {
  overflow: hidden;
}
.kCustomer {
	background-color: #eee;
	&::after {
		content: "";
		display: block;
		position: relative;
		width: 100%;
		height: 100%;
		z-index: 4;
		background: transparent;
	}
}
.kCustomer /deep/ .code-view-wrapper {
  margin: 0;
}
.kCustomer /deep/ .k-form-body > div {
	flex: unset;
}
</style>
