<template>
  <Shape :element="element" :isAbsLayout="isAbsLayout">
    <k-steps :key="unKey" :data-direction="element.options.dataDirection" :data-active.sync="element.options.dataActive" :data-next-show="element.options.dataNextShow" :data-submit-show="element.options.dataSubmitShow" :data-button-align="element.options.dataButtonAlign" :data-col="element.options.dataCol" :data-action="element.options.dataAction" :data-graphql="element.options.dataGraphql" :data-confirm="element.options.dataConfirm" :data-submit-description="element.options.dataSubmitDescription" :data-model="element.options.dataModel">
      <k-step :data-title="item.options.dataTitle" :data-description="item.options.dataDescription" :data-icon="item.options.dataIcon" :key="index" v-for="(item,index) in element.list">
        <draggable class="" v-model="item.list" v-bind="{group:{ name:'people', 
                         pull:true, 
                         put: ()=>{return kFormDesign.draggableType!='k-grid-column' && kFormDesign.draggableType!='k-step' ;}}, 
                ghostClass: 'ghost',
                animation: 200, 
                handle: '.drag-widget'}" @add="handleFormAdd" style="width:100%">
          <transition-group name="fade" tag="div" class="widget-form-list">
            <template v-for="(t) in item.list">
              <template v-if="t.type=='el-row'">
                <div style="width:100%" :key="t.key">
                  <grid-layout :element="t" :key="t.key" />
                </div>
              </template>
              <template v-else>
                <k-form-item-component :element="t" :key="t.key" />
              </template>
            </template>
          </transition-group>
        </draggable>
      </k-step>
    </k-steps>
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
  data() {
    return {
      unKey: '',
      ls: [],
      oldActive: 1,
      loadCount: 0
    }
  },
  created() {
    this.loadCount++
    this.unKey = Date.parse(new Date()) + '_' + Math.ceil(Math.random() * 99999);
    if (!this.element.list || this.element.list.length == 0) {
      let config = findComponent("k-step");
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
    // form表单添加元素
    handleFormAdd: function ($event, index) {
      // const newIndex = $event.newIndex
      // let parentKey = this.element.list[index].key;

      // if (this.element.list[index].list[newIndex].key) {  // key存在 => 从别的容器中拖拉的组件，修改部分参数
      //   this.$set(this.element.list[index].list, newIndex, {
      //     ...this.element.list[index].list[newIndex],
      //     inForm: true,
      //     parentKey: parentKey,
      //   })
      // } else {
      //   let key = Date.parse(new Date()) + '_' + Math.ceil(Math.random() * 99999);


      //   this.$set(this.element.list[index].list, newIndex, {
      //     ...this.element.list[index].list[newIndex],
      //     key: key,
      //     model: this.element.list[index].list[newIndex].type.replaceAll("-", "_") + '_' + key,
      //     options: parseOptions(this.element.list[index].list[newIndex].type),
      //     inForm: true,
      //     parentKey: parentKey,
      //     formItem: { label: this.element.list[index].list[newIndex].name }
      //   })
      // }
      // console.log("ls", this.element.list[index])
      // this.kFormDesign.widgetFormSelect = this.element.list[index].list[newIndex]
    },
  },
  watch: {
    element: {
      handler: function (newVal, oldVal) {
        //更改唯一id强制刷新组件
        this.unKey = Date.parse(new Date()) + '_' + Math.ceil(Math.random() * 99999);
        this.element.list.map(item => {
          let parentKey = item.key;
          for (var index in item.list) {
            let t = item.list[index]
            if (!t.key) {
              const key = Date.parse(new Date()) + '_' + Math.ceil(Math.random() * 99999)
              this.$set(item.list, index, {
                ...t,
                key: key,
                model: t.type.replaceAll("-", "_") + '_' + key,
                options: parseOptions(t.type),
                inForm: true,
                parentKey: parentKey,
                formItem: { label: t.name }
              })
            }
          }
        })
        if (this.loadCount > 1) {
          if(this.oldActive>this.element.list.length){
            this.element.options.dataActive = 1
          }else{
            this.element.options.dataActive = this.oldActive
          }
        } 
      },
      deep: true
    },
    //解决拖曳组件后，回到第一个step的问题
    'element.options.dataActive': {
      handler: function (newVal, oldVal) {
        this.oldActive = oldVal
      }
    }
  }
}
</script>

<style lang="scss" scoped>
//必须要加上“<style lang="scss" scoped>，否则引入失败（提示保存）
.widget-form-list {
	min-height: 80px !important;
}
@import "../styles/cover.scss";
@import "../styles/index.scss";
</style>
