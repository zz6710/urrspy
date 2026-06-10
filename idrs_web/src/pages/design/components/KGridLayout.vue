<template>
  <Shape :element="element" :isAbsLayout="isAbsLayout">
    <k-grid :ref="element.ref" :dataData="element.dataData" :dataEntity="element.options.dataEntity"  :dataDisplay="false" data-operate-width="210px">
      <template v-for="(item) in element.list">
        <k-grid-column v-if="item.key" :key="item.key" :data-header="item.options.dataHeader" :data-name="item.options.dataName"></k-grid-column>
      </template>

      <template slot="operate" slot-scope="scope">
        <k-btn-component :key="index" :element="item" v-for="(item, index) in element.btns"/>
      </template>
    </k-grid>

    <el-row :gutter="20">
      <el-col :span="12">
        <div class="grid-content bg-purple">
          <div class="form-empty">
            拖拽到此处添加表格字段
          </div>
          <draggable class=""
            v-model="element.list"
            v-bind="{group:{ name:'people', pull:'clone',put: ()=>{return this.kFormDesign.draggableType=='k-grid-column';}}, ghostClass: 'ghost',animation: 200, handle: '.drag-widget'}"
            @add="handleKGridAdd"
          >
            <transition-group name="fade" tag="div" class="widget-form-list">

            </transition-group>
          </draggable>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="grid-content bg-purple">
          <div class="form-empty">
            拖拽到此处添加表格行按钮
          </div>
          <draggable class=""
            v-model="element.btns"
            v-bind="{group:{ name:'people', pull:'clone',put: ()=>{return this.kFormDesign.draggableType=='k-btn'}}, ghostClass: 'ghost',animation: 200, handle: '.drag-widget'}"
            @add="handleBtnAdd"
          >
            <transition-group name="fade" tag="div" class="widget-form-list">

            </transition-group>
          </draggable>
        </div>
      </el-col>
    </el-row>

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
    }
  },
  created(){
    // if(!this.element.list || this.element.list.length==0){
    //   let config = findComponent("k-grid-column");
    //   this.$set(this.element, 'list', [{
    //     ...config,
    //     key: Date.parse(new Date()) + '_' + Math.ceil(Math.random() * 99999),
    //     options: parseOptions(config.type),
    //   }])
    // }
  },
  mounted () {
  },
  methods: {
    checkComponentType: function($event){
      // TODO 修改判断方式，有可能是从其他容器的组件拖过来
      if(this.kFormDesign.draggableType != 'k-grid-column'){
        return false;
      }
      return true;
    },
    // k-grid表格添加元素
    handleKGridAdd: function($event){
      const newIndex = $event.newIndex

      //为拖拽到容器的元素添加唯一 key
      const key = Date.parse(new Date()) + '_' + Math.ceil(Math.random() * 99999)
      this.$set(this.element.list, newIndex, {
        ...this.element.list[newIndex],
        options: parseOptions(this.element.list[newIndex].type),
        key,
      })
    },

    handleBtnAdd: function($event){
      const newIndex = $event.newIndex
      let parentKey = this.element.key;

      if(this.element.btns[newIndex].key){  // key存在 => 从别的容器中拖拉的组件，修改部分参数
        this.$set(this.element.btns, newIndex, {
          ...this.element.btns[newIndex],
          inForm: false,
          parentKey: parentKey,
        })
      } else {
        let key = Date.parse(new Date()) + '_' + Math.ceil(Math.random() * 99999);
        this.$set(this.element.btns, newIndex, {
          ...this.element.btns[newIndex],
          key: key,
          options: parseOptions(this.element.btns[newIndex].type),
          inForm: false,
          parentKey: parentKey,
          customAttrs: [],
          methods: [],
          events: [],
        })
      }

      this.kFormDesign.widgetFormSelect = this.element.btns[newIndex]
    },
  },
  watch: {
  }
}
</script>

<style lang="scss" scoped>   //必须要加上“<style lang="scss" scoped>，否则引入失败（提示保存）
  .widget-form-list {
    min-height: 80px !important;
  }

  .form-empty {
    top: 60px !important;
  }

  @import '../styles/cover.scss';
  @import '../styles/index.scss';

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

</style>
