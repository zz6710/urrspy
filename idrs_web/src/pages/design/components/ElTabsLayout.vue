<template>
  <Shape :element="element" :isAbsLayout="isAbsLayout">
    <el-tabs :value="element.options.value" :type="element.options.type" :closable="element.options.closable" :addable="element.options.addable" :editable="element.options.editable" :tab-position="element.options.tabPosition" :stretch="element.options.stretch" :before-leave="element.options.beforeLeave">
      <el-tab-pane style="height: 100%" :label="item.options.label" :disabled="item.options.disabled" :name="item.options.name" :closable="item.options.closable" :lazy="item.options.lazy" :key="index" v-for="(item,index) in element.list">
        <span slot="label">
          <i v-if="item.options.icon.type == 'icon'" :class="item.options.icon.value" />
          <md-icon v-else-if="item.options.icon.type == 'md'"  >{{item.options.icon.value}}</md-icon>
          <md-icon v-else-if="item.options.icon.type == 'svg'" :md-src="item.options.icon.value" />
          {{item.options.label}}
        </span>
        <base-container class="base-layout"  ref="widgetForm" :element="item"></base-container>
      </el-tab-pane>
    </el-tabs>
  </Shape>
</template>

<script>
import Shape from '@/pages/design/components/base/Shape.vue'
import Draggable from 'vuedraggable'
import GridLayout from "./GridLayout"
import KFormItemComponent from "./KFormItemComponent"
import BasicComponent from "./BasicComponent"
import KBtnComponent from "./KBtnComponent"
import { parseOptions, findComponent } from './componentsConfig';


export default {
  components: {
    Shape,
    Draggable,
    GridLayout,
    KFormItemComponent,
    BasicComponent,
    KBtnComponent,
    // 解决组件直接循环引用的问题
    // BaseLayout: () => import('@/pages/design/components/BaseLayout.vue')
    // 解决组件直接循环引用的问题
    BaseContainer: () => import('@/pages/design/components/container/BaseContainer.vue')
  },
  props: {
    element: {
      type: Object
    },
    isAbsLayout: {
      type: Boolean,
      default: false
    }
  },
  inject: ['kFormDesign'],
  data() {
    return {
    }
  },
  created() {
    if (!this.element.list || this.element.list.length == 0) {
      let config = findComponent("el-tab-pane");
      this.$set(this.element, 'list', [{
        ...config,
        key: Date.parse(new Date()) + '_' + Math.ceil(Math.random() * 99999),
        options: parseOptions(config.type),
      }])
    }
  },
  mounted() {
  },
  methods: {
  },
  watch: {
  }
}
</script>

<style lang="scss" scoped>
//必须要加上“<style lang="scss" scoped>，否则引入失败（提示保存）
.widget-form-list {
	min-height: 80px !important;
}
.base-layout  /deep/ .widget-form-list {
	min-height: 400px !important;
}
@import "../styles/cover.scss";
@import "../styles/index.scss";
</style>
