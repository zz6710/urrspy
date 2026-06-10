<template>
  <Shape :element="element" :isAbsLayout="isAbsLayout" :style="kFormDesign.getStyle(element.style, ['width','height'], [])">
    <k-form
      :key="element.key"
      v-bind="options"
    >
      <draggable class=""
        v-model="element.list"
        v-bind="{group:{ name:'people', pull:true, put: ()=>{return this.kFormDesign.draggableType!='k-grid-column';}}, ghostClass: 'ghost',animation: 200, handle: '.drag-widget'}"
        @add="handleFormAdd"
      >
        <transition-group name="fade" tag="div" class="widget-form-list formLayoutDraggable">
          <template v-for="(item) in element.list">
            <template v-if="item.type=='el-row'">
              <div style="width:100%" :key="item.key">
                <grid-layout :element="item" :key="item.key"/>
              </div>
            </template>
            <template v-else>
              <!-- warn处理：先渲染后赋值引起的warn -->
              <k-form-item-component v-if="item.key" :element="item" :key="item.key"/>
            </template>
          </template>
          <k-form-footer data-align="center" key="11">
            <draggable
              :class="{ formfooterDrag: element.btns.length == 0 }"
              v-model="element.btns"
              v-bind="{group:{ name:'people', pull:true, put: ()=>{return this.kFormDesign.draggableType=='k-btn';}}, ghostClass: 'ghost',animation: 200, handle: '.drag-widget'}"
              @add="handleBtnAdd"
            >
              <transition-group name="fade" tag="div" class="widget-form-button">
                <template v-for="(item) in element.btns">
                  <k-btn-component v-if="item.key" :key="item.key" :element="item"/>
                </template>
              </transition-group>
            </draggable>
          </k-form-footer>
        </transition-group>
      </draggable>
    </k-form>
  </Shape>
</template>

<script>
import _ from 'lodash'
import Shape from '@/pages/design/components/base/Shape.vue'
import Draggable from 'vuedraggable'
import GridLayout from "./GridLayout"
import KFormItemComponent from "./KFormItemComponent"
import KBtnComponent from "./KBtnComponent"
import { parseOptions, findComponent } from './componentsConfig';
import optionMinix from "./minix"

import uniqueId from 'lodash/uniqueId'



export default {
  components: {
    Draggable,
    GridLayout,
    KFormItemComponent,
    KBtnComponent,
    Shape
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
  mixins: [optionMinix],
  data () {
    return {
    }
  },
  mounted () {
  },
  methods: {
    // 表单添加按钮
    handleBtnAdd: function($event){
      const newIndex = $event.newIndex
      let parentKey = this.element.key;

      if(this.element.btns[newIndex].key){  // key存在 => 从别的容器中拖拉的组件，修改部分参数
        this.$set(this.element.btns, newIndex, {
          ...this.element.btns[newIndex],
          inForm: true,
          parentKey: parentKey,
        })
      } else {
        let key = Date.parse(new Date()) + '_' + Math.ceil(Math.random() * 99999);
        let type = this.element.btns[newIndex].type;
        let _component = findComponent(type);
        let _deepComponent = _.cloneDeep(_component);                                   // 深度复制，防止组件之间修改参数影响

        this.$set(this.element.btns, newIndex, Object.assign(
          {}, {..._deepComponent}, {
            ...this.element.btns[newIndex],
            key: key,
            model: type.replaceAll("-","_") + '_' + key,
            options: parseOptions(type),
            inForm: true,
            parentKey: parentKey,
            customAttrs: [],
            methods: [],
            events: [],
          }
        ))
      }

      console.log('findComponent(type)', findComponent(this.element.btns[newIndex].type));

      this.kFormDesign.widgetFormSelect = this.element.btns[newIndex]
    },
    // form表单添加元素
    handleFormAdd: function($event){
      let newIndex = $event.newIndex;
      // 兼容拖拽报错
      if ($event.newIndex + 1 > this.element.list.length) {
				newIndex = $event.newIndex - 1;
			}
      let parentKey = this.element.key;

      if(this.element.list[newIndex].key){  // key存在 => 从别的容器中拖拉的组件，修改部分参数
        let inForm = this.element.list[newIndex].inForm;
        this.$set(this.element.list, newIndex, {
          ...this.element.list[newIndex],
          inForm: true,
          parentKey: parentKey,
        })
        // 从非表单容器拖入，需要初始化formItem参数
        if(!inForm){
          let formItem = { label: this.element.list[newIndex].name }
          this.$set(this.element.list[newIndex], 'formItem', formItem)
        }
      } else {
        const key = Date.parse(new Date()) + '_' + Math.ceil(Math.random() * 99999)
        const _class = 'component_' + key;
        this.$set(this.element.list, newIndex, {
          ...this.element.list[newIndex],
          key: key,
          model: this.element.list[newIndex].type.replaceAll("-","_") + '_' + key,
          options: parseOptions(this.element.list[newIndex].type),
          inForm: true,
          parentKey: parentKey,
          formItem: { label: this.element.list[newIndex].name },
          customClass: _class
        })
      }

      this.kFormDesign.widgetFormSelect = this.element.list[newIndex]
    },
    uniqueId: uniqueId
  },
}
</script>

<style lang="scss" scoped>   //必须要加上“<style lang="scss" scoped>，否则引入失败（提示保存）
  @import '../styles/cover.scss';
  @import '../styles/index.scss';

  .widget-form-button {
    width: 400px;
    bottom: 30px;
  }

  .form-empty {
    width: 400px !important;
    top: 15px !important;
    // left: 0 !important;
  }

  .formfooterDrag {
    position: relative;

    &::after {
      content: "按 钮 插 槽";
      position: absolute;
      top: 50%;
      left: 50%;
      width: 70px;
      height: 14px;
      line-height: 14px;
      transform: translate(-50%, -50%);
      color: #C0C4CC;
    }
  }
  .formLayoutDraggable {
    min-height: 200px;
  }

</style>
