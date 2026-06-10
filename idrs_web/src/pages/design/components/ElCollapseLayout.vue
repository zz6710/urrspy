<template>
  <Shape :element="element" :isAbsLayout="isAbsLayout">
    <el-collapse :value="element.options.value" :accordion="element.options.accordion">
      <el-collapse-item :title="item.options.title" :name="item.options.name" :disabled="item.options.disabled" :key="index" v-for="(item,index) in element.list">
        <template slot="title">
          <i v-if="item.options.icon.type == 'icon'" :class="item.options.icon.value" />
          <md-icon v-else-if="item.options.icon.type == 'md'" style="margin:0 !important" >{{item.options.icon.value}}</md-icon>
          <md-icon v-else-if="item.options.icon.type == 'svg'" :md-src="item.options.icon.value" />
          {{item.options.title}}
        </template >
        <base-container ref="widgetForm" :element="item"></base-container>
      </el-collapse-item>
    </el-collapse>
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
      let config = findComponent("el-collapse-item");
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
.form-empty {
	top: 60px !important;
	width: 240px !important;
}
@import "../styles/cover.scss";
@import "../styles/index.scss";
</style>
