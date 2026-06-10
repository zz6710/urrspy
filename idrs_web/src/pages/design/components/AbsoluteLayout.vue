<template>
  <Shape :element="element" :isAbsLayout="isAbsLayout" :style="kFormDesign.getStyle(element.style)">
    <draggable
      class=""
      style=""
      v-model="element.list"
      v-bind="{
        group:{ name:'people', pull:true, put: ()=>{return this.kFormDesign.draggableType!='k-grid-column';}},
        ghostClass: 'ghost',
        animation: 200,
        handle: '.drag-widget'
      }"
      @add="handleWidgetAdd"
    >
      <transition-group name="" tag="div" style="height:100%" :style="getStyle(element.style)">
        <template v-for="(item) in element.list">
          <component
            v-if="item.key"
            :key="item.key"
            :is="item.renderType?item.renderType:'basic-component'"
            :element="item"
            :isAbsLayout="true"
          />
        </template>
      </transition-group>
    </draggable>

    <!-- 标线 -->
    <MarkLine :element="element"/>

  </Shape>
</template>

<script>
import MarkLine from '@/pages/design/components/base/MarkLine.vue'
import Shape from '@/pages/design/components/base/Shape.vue'
import Draggable from 'vuedraggable'
import { parseOptions, findComponent } from './componentsConfig';
import { getStyle } from '@/pages/design/utils/style'

import { $ } from '@/pages/design/utils/utils'
import KFormLayout from "@/pages/design/components/KFormLayout"
import GridLayout from "@/pages/design/components/GridLayout"
import KFormSearchLayout from "@/pages/design/components/KFormSearchLayout"
import KFormSearchCustomizeLayout from "@/pages/design/components/KFormSearchCustomizeLayout"
import KGridLayout from "@/pages/design/components/KGridLayout"
import BasicComponent from "@/pages/design/components/BasicComponent"
import KBtnComponent from "@/pages/design/components/KBtnComponent"
import AbsoluteLayout from "@/pages/design/components/AbsoluteLayout"
import BaseLayout from "@/pages/design/components/BaseLayout"
import KCustomer from "./KCustomer"

export default {
  name: "AbsoluteLayout",
  components: {
    MarkLine,
    Shape,
    Draggable,
    BasicComponent,
    KFormLayout,
    GridLayout,
    KFormSearchLayout,
    KFormSearchCustomizeLayout,
    KGridLayout,
    KBtnComponent,
    AbsoluteLayout,
    BaseLayout,
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
  provide() {
    return {
      canvasElement: this.element,
    };
  },
  data () {
    return {
    }
  },
  created (){
    if(!this.element.list){
      this.$set(this.element, 'list', [])
    }
  },
  mounted () {
  },
  methods: {
    getStyle: getStyle,
    // 大容器添加元素
    handleWidgetAdd: function ($event) {
      const newIndex = $event.newIndex
      const to = $event.to

      // 获取节点偏移位置
      let top = $event.originalEvent.offsetY < 0 ? 0 :$event.originalEvent.offsetY;
      let left = $event.originalEvent.offsetX < 0 ? 0:$event.originalEvent.offsetX;

      let parentKey = this.element.key;

      //为拖拽到容器的元素添加唯一 key
      if(this.element.list[newIndex].key){  // key存在 => 从别的容器中拖拉的组件，修改部分参数
        // const _class = 'component_' + key;
        this.$set(this.element.list, newIndex, {
          ...this.element.list[newIndex],
          inForm: false,
          parentKey: parentKey,
          style: {
            ...this.element.list[newIndex].style,
            position: 'absolute',
            top: top,
            left: left,
          },
        })
      } else {  // key不存在 => 左边菜单新拖拉的组件，需要初始化参数
        const key = Date.parse(new Date()) + '_' + Math.ceil(Math.random() * 99999)
        const _class = 'component_' + key;
        let type = this.element.list[newIndex].type;
        let _component = findComponent(type);
        let _deepComponent = _.cloneDeep(_component);
        console.log('_component: ', _component);
        this.$set(this.element.list, newIndex, Object.assign({}, {..._deepComponent}, {
          key,
          // 绑定键值
          model: this.element.list[newIndex].type.replaceAll("-","_") + '_' + key,
          inForm: false,
          options: parseOptions(this.element.list[newIndex].type),
          parentKey: parentKey,
          style: {
            position: 'absolute',
            top: top,
            left: left,
          },
          customClass: _class
        }))
      }

      // 初始化样式
      if(['absolute-layout','base-layout'].includes(this.element.list[newIndex].type)){
        let _component = this.kFormDesign.findComponentByKey(this.element.list[newIndex].parentKey);
        let parentDom = $("." + _component.customClass);
        if(!this.element.list[newIndex].style.height){
          let parentHeight = parentDom.clientHeight;
          let _height = Math.round(parentHeight/3) > 300 ? 300 : Math.round(parentHeight/3);
          this.$set(this.element.list[newIndex].style, 'height', _height < 60? 60: _height);
        }
        if(!this.element.list[newIndex].style.width) {
          let parentWidth = parentDom.clientWidth;
          let _width = Math.round(parentWidth/3) > 300 ? 300 : Math.round(parentWidth/3);
          this.$set(this.element.list[newIndex].style, 'width', _width < 60? 60: _width);
        }
      } else if(this.element.list[newIndex].type === 'k-customer') {
        this.$set(this.element.list[newIndex].style, 'height', 250);
        this.$set(this.element.list[newIndex].style, 'width', '100%');
      }

      // 新增元素挪动到最后
      let spliceArr = this.element.list.splice(newIndex, 1);
      this.element.list.push(...spliceArr);

      // 选中当前元素
      this.kFormDesign.widgetFormSelect = this.element.list[newIndex]
    },
    isNearly(dragValue, targetValue) {
        return dragValue < 0 || Math.abs(dragValue - targetValue) <= this.diff
    }
  },
  watch: {
  },
}
</script>

<style lang="scss" scoped>   //必须要加上“<style lang="scss" scoped>，否则引入失败（提示保存）
  @import '../styles/cover.scss';
  @import '../styles/index.scss';

</style>
