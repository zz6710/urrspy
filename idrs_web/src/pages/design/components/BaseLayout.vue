<template>
  <Shape :element="element" :isAbsLayout="isAbsLayout" class="container-layout baseOverflow" :style="kFormDesign.getStyle(element.style)">
    <base-container :element="element"></base-container>
  </Shape>
</template>

<script>
import { $ } from '@/pages/design/utils/utils'
import Shape from '@/pages/design/components/base/Shape.vue'
import Draggable from 'vuedraggable'
import GridLayout from "./GridLayout"
import KFormLayout from "./KFormLayout"
import KFormSearchLayout from "./KFormSearchLayout"
import KFormSearchCustomizeLayout from "./KFormSearchCustomizeLayout"
import KGridLayout from "./KGridLayout"
import BasicComponent from "./BasicComponent"
import KBtnComponent from "./KBtnComponent"
// import AbsoluteLayout from "./AbsoluteLayout"

import { parseOptions, findComponent } from './componentsConfig';
import { getStyle } from '@/pages/design/utils/style'

import KStepsLayout from "./KStepsLayout"
import ElTabsLayout from "./ElTabsLayout"
import ElCollapseLayout from "./ElCollapseLayout"
import ElDividerLayout from "./ElDividerLayout"


export default {
  name: "BaseLayout",
  components: {
    Shape,
    Draggable,
    GridLayout,
    KFormLayout,
    KFormSearchLayout,
    KFormSearchCustomizeLayout,
    KGridLayout,
    BasicComponent,
    KBtnComponent,
    AbsoluteLayout: () => import("./AbsoluteLayout"),
    KStepsLayout,
    ElTabsLayout,
    BaseContainer: () => import('@/pages/design/components/container/BaseContainer.vue'),
    ElCollapseLayout,
    ElDividerLayout
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
  data () {
    return {
      width: '',  // 空状态显示提示文字宽度
    }
  },
  created() {
  },
  mounted () {
  },
  computed: {
  },
  methods: {
    getStyle: getStyle,
    // 大容器添加元素
    handleWidgetAdd ($event) {
      const newIndex = $event.newIndex
      let parentKey = this.element.key;
      if(this.element.list[newIndex].key){    // key存在 => 从别的容器中拖拉的组件，修改部分参数
        this.$set(this.element.list, newIndex, {
          ...this.element.list[newIndex],
          inForm: false,
          parentKey: parentKey,
        })
      } else {
        let key = Date.parse(new Date()) + '_' + Math.ceil(Math.random() * 99999);
        const _class = 'component_' + key;
        this.$set(this.element.list, newIndex, {
          key: key,
          ...this.element.list[newIndex],
          model: this.element.list[newIndex].type.replaceAll("-","_") + '_' + key,
          options: parseOptions(this.element.list[newIndex].type),
          inForm: false,
          parentKey: parentKey,
          customClass: _class,
        })
      }

      // 容器组件初始化样式
      if(['absolute-layout'].includes(this.element.list[newIndex].type)){
        if(!this.element.list[newIndex].style.height){
          let parentHeight = $("." + this.element.customClass).clientHeight;
          let _height = Math.round(parentHeight/3) > 300 ? 300 : Math.round(parentHeight/3);
          this.$set(this.element.list[newIndex].style, 'height', _height < 60? 60: _height);
          // this.element.list[newIndex].style.height = _height;
        }
      }

      this.kFormDesign.widgetFormSelect = this.element.list[newIndex]
    },
  },
  watch: {
    'element.style': {
      handler(){
        let width = this.kFormDesign.$("." + this.element.customClass).clientWidth
        if(width > 100){
          this.width = "";
        } else {
          this.width = width + "px"
        }
      },
      deep: true
    }
  }
}
</script>

<style lang="scss" scoped>
  @import '../styles/cover.scss';
  @import '../styles/index.scss';

  .baseOverflow {
    overflow: hidden;
  }

  /deep/ .k-form-body > div {
    // display: none;
    flex: 1
  }

  .empty {
    background: #f1f1f1;
  }
  .empty::before{
    content:'请拖拽组件到此';
    position: absolute;
    top: 50%;
    left: 50%;
    width: var(--width, 100px);
    height: 14px;
    line-height: 14px;
    transform: translate(-50%, -50%);
    color: #a7b1bd;
  }

</style>
