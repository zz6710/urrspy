<template>
  <div class="widget-form-container" @click="handlerContainerClick">
    <draggable
      class=""
      style="height:100%"
      v-model="element.list"
      v-bind="{
        group:{ name:'people', pull:true, put: ()=>{return kFormDesign.draggableType!='k-grid-column';}},
        ghostClass: 'ghost',
        animation: 200,
        handle: '.drag-widget',
      }"
      @add="handleWidgetAdd"
    >
      <transition-group name="" tag="div" :class="['widget-form-list', element.customClass]">
        <!-- warn处理：先渲染后赋值引起的warn -->
        <template v-for="(item) in element.list">
          <component
            v-if="item.key"
            :key="item.key"
            :is="item.renderType?item.renderType:'basic-component'"
            :element="item"
          />
        </template>
      </transition-group>
    </draggable>
   </div>
</template>

<script>
import _ from 'lodash'
import { $ } from '@/pages/design/utils/utils'
import Draggable from 'vuedraggable'
import GridLayout from "./GridLayout"
import KFormLayout from "./KFormLayout"
import KFormSearchLayout from "./KFormSearchLayout"
import KFormSearchCustomizeLayout from "./KFormSearchCustomizeLayout"
import KGridLayout from "./KGridLayout"
import BasicComponent from "./BasicComponent"
import KBtnComponent from "./KBtnComponent"
import AbsoluteLayout from "./AbsoluteLayout"
import BaseLayout from "@/pages/design/components/BaseLayout"
import KStepsLayout from "./KStepsLayout"
import ElTabsLayout from "./ElTabsLayout"
import ElCollapseLayout from "./ElCollapseLayout"
import ElDividerLayout from "./ElDividerLayout"
import KCustomer from "./KCustomer"


import { parseOptions, findComponent  } from './componentsConfig';
import { getStyle } from '@/pages/design/utils/style'

export default {
  components: {
    Draggable,
    GridLayout,
    KFormLayout,
    KFormSearchLayout,
    KFormSearchCustomizeLayout,
    KGridLayout,
    BasicComponent,
    KBtnComponent,
    AbsoluteLayout,
    BaseLayout,
    KStepsLayout,
    ElTabsLayout,
    ElCollapseLayout,
    ElDividerLayout,
    KCustomer,
  },
  props: ['element'],
  inject: ['kFormDesign'],
  data () {
    return {
    }
  },
  mounted () {
    document.body.ondrop = function (event) {
      let isFirefox = navigator.userAgent.toLowerCase().indexOf('firefox') > -1
      if (isFirefox) {
        event.preventDefault()
        event.stopPropagation()
      }
    }
  },
  methods: {
    onStart(evt){
      var e = evt || window.event;
      var chosen = document.querySelector('.sortable-fallback');
      chosen.style.top = e.item.offsetTop + 'px';
    },
    getStyle: getStyle,
    // 大容器添加元素
    handleWidgetAdd (evt) {
      const newIndex = evt.newIndex
      let parentKey = this.element.key;

      if(this.element.list[newIndex].key){            // key存在 => 从别的容器中拖拉的组件，修改部分参数
        this.$set(this.element.list, newIndex, {
          ...this.element.list[newIndex],
          inForm: false,
          parentKey: parentKey,
        })
      } else {
        let key = Date.parse(new Date()) + '_' + Math.ceil(Math.random() * 99999);
        const _class = 'component_' + key;
        let type = this.element.list[newIndex].type;
        let _component = findComponent(type);
        let _deepComponent = _.cloneDeep(_component);                                   // 深度复制，防止组件之间修改参数影响
        this.$set(this.element.list, newIndex, Object.assign({}, {..._deepComponent}, {
          key: key,
          model: type.replaceAll("-","_") + '_' + key,
          options: parseOptions(type),
          inForm: false,
          parentKey: parentKey,
          customClass: _class,
        }))
      }
      console.log('----------');
      console.log('new add data: ******',this.element.list[newIndex]);
      console.log('----------');

      this.$nextTick(()=> {
        // 初始化样式
        if(['absolute-layout'].includes(this.element.list[newIndex].type)){
          if(!this.element.list[newIndex].style.height){
            let _component = this.kFormDesign.findComponentByKey(this.element.list[newIndex].parentKey);
            let parentHeight = $("." + _component.customClass).clientHeight;
            // console.log($("." + _component.customClass), $("." + _component.customClass).clientHeight);
            let _height = Math.round(parentHeight/2.5) > 500 ? 500 : Math.round(parentHeight/2.5);
            this.$set(this.element.list[newIndex].style, 'height', _height);
            this.$set(this.element.list[newIndex].style, 'width', '100%');
          }
        }
      })

      this.kFormDesign.widgetFormSelect = this.element.list[newIndex]
    },
    handlerContainerClick: function(){
      this.kFormDesign.widgetFormSelect = {};
    }
  },
  watch: {
  }
}
</script>

<style lang="scss" scoped>   //必须要加上“<style lang="scss" scoped>，否则引入失败（提示保存）
  @import '../styles/cover.scss';
  @import '../styles/index.scss';

  /deep/ .k-form-body > div {
    // display: none;
    flex: 1
  }

  .widget-form-container {
    flex: 1;
    overflow: auto;
    padding: 10px;
    padding-top: 0;
  }

  .custom-canvas-main {
    position: absolute;
    width: 100%;
    // height: 100%;
    top: 0;
    left: 0;
  }


</style>
