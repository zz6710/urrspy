<template>
  <draggable
    class=""
    style="height:100%"
    v-model="element.list"
    v-bind="{
      group:{ name:'people', pull:true, put: ()=>{return kFormDesign.draggableType!='k-grid-column';}},
      ghostClass: 'ghost',
      animation: 200,
      handle: '.drag-widget'
    }"
    @add="handleWidgetAdd"
  >
    <transition-group v-if="element.list && element.list.length" name="" tag="div" style="height:100%" :style="kFormDesign.getStyle(element.style, ['top','left'])">
      <template v-for="(item) in element.list">
        <component
          v-if="item.key"
          :key="item.key"
          :is="item.renderType?item.renderType:'basic-component'"
          :element="item"
        />
      </template>
    </transition-group>

    <!-- 没有组件时默认显示 -->
    <transition-group v-else name="" tag="div" class="empty" style="position:relative;min-height:60px;height:100%;" :style="{...kFormDesign.getStyle(element.style,['height','top','left']),'--width':width}">
    </transition-group>
  </draggable>
</template>

<script>
import { $ } from '@/pages/design/utils/utils'
import Draggable from 'vuedraggable'
import GridLayout from "@/pages/design/components/GridLayout"
import KFormLayout from "@/pages/design/components/KFormLayout"
import KFormSearchLayout from "@/pages/design/components/KFormSearchLayout"
import KFormSearchCustomizeLayout from "@/pages/design/components/KFormSearchCustomizeLayout"
import KGridLayout from "@/pages/design/components/KGridLayout"
import BasicComponent from "@/pages/design/components/BasicComponent"
import KBtnComponent from "@/pages/design/components/KBtnComponent"
import KStepsLayout from "@/pages/design/components/KStepsLayout"
import ElTabsLayout from "@/pages/design/components/ElTabsLayout"
import { parseOptions, findComponent } from '@/pages/design/components/componentsConfig';
import KCustomer from "../KCustomer"

export default {
  name: "BaseLayout",
  components: {
    Draggable,
    GridLayout,
    KFormLayout,
    KFormSearchLayout,
    KFormSearchCustomizeLayout,
    KGridLayout,
    BasicComponent,
    KBtnComponent,
    AbsoluteLayout: () => import("@/pages/design/components/AbsoluteLayout"),
    KStepsLayout,
    ElTabsLayout,
    KCustomer
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
        }
      }

        console.log(this.element.list, 'handleWidgetAdd');


      // 默认选中
      this.kFormDesign.widgetFormSelect = this.element.list[newIndex]
    },
  },
  watch: {
    'element.style': {
      handler(){
        console.log(11);
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
  @import '../../styles/cover.scss';
  @import '../../styles/index.scss';

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
