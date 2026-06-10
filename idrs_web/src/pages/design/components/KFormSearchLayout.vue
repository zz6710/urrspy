<template>
  <Shape :element="element" :isAbsLayout="isAbsLayout">
    <k-form-search
      :data-target="element.options.dataTarget"
      :data-model-name="element.options.dataModelName"
      :key="formKey"
    >
      <div>
        <draggable class=""
          v-model="element.btns"
          v-bind="{group:{ name:'people', pull:'clone',put: ()=>{return this.kFormDesign.draggableType=='k-btn';}}, ghostClass: 'ghost',animation: 200, handle: '.drag-widget'}"
          @add="handleBtnAdd"
        >
          <transition-group name="fade" tag="div" class="widget-form-button">
            <k-btn-component :key="item.key" :element="item" v-for="item in element.btns"/>
          </transition-group>
        </draggable>
      </div>
    </k-form-search>
  </Shape>
</template>

<script>
import Shape from '@/pages/design/components/base/Shape.vue'
import Draggable from 'vuedraggable'
import GridLayout from "./GridLayout"
import KFormItemComponent from "./KFormItemComponent"
import KBtnComponent from "./KBtnComponent"
import { parseOptions } from './componentsConfig';
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
  data () {
    return {
      formKey: uniqueId('k_form_search_'),
    }
  },
  created(){
    if(!this.element.btns){
      this.$set(this.element, 'btns', [])
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
        this.$set(this.element.btns, newIndex, {
          ...this.element.btns[newIndex],
          key: key,
          model: this.element.btns[newIndex].type.replaceAll("-","_") + '_' + key,
          options: parseOptions(this.element.btns[newIndex].type),
          inForm: true,
          parentKey: parentKey,
          customAttrs: [],
          methods: [],
          events: [],
        })
      }

      this.kFormDesign.widgetFormSelect = this.element.btns[newIndex]
    }
  },
  watch: {
    'element.options': {
      handler(newVal, oldVal){
        this.formKey = uniqueId('k_form_search_');
      },
      deep: true
    }
  }
}
</script>

<style lang="scss" scoped>   //必须要加上“<style lang="scss" scoped>，否则引入失败（提示保存）
  @import '../styles/cover.scss';
  @import '../styles/index.scss';

</style>
