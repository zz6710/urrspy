<template>
  <el-row class="widget-col widget-view"
    type="flex"
    :key="element.key"
    :class="{active: kFormDesign.widgetFormSelect.key == element.key}"
    :gutter="element.options.gutter ? element.options.gutter : 0"
    :justify="element.options.justify"
    :align="element.options.align"
    @click.native.stop="kFormDesign.selectComponentByKey(element.key)">
    <el-col  v-for="(col, colIndex) in element.columns" :key="colIndex" :span="col.span ? col.span : 0">
      <draggable
        v-model="col.list"
        :no-transition-on-drag="true"
        v-bind="{group:{name:'people', pull:'clone', put:()=>{return kFormDesign.draggableType!='k-grid-column';}}, ghostClass: 'ghost',animation: 200, handle: '.drag-widget'}"
        @add="addColComponent(kFormDesign.pageList, colIndex, $event)"
      >
        <transition-group name="fade" tag="div" class="widget-col-list">
          <template v-for="(el) in col.list">
            <k-form-item-component v-if="element.inForm == true" :element="el" :key="el.key"/>
            <basic-component v-else :element="el" :key="el.key"/>
          </template>
        </transition-group>
      </draggable>
    </el-col>
    <div class="widget-view-action widget-col-action" v-if="kFormDesign.widgetFormSelect.key == element.key">
      <i class="iconfont icon-trash" @click.stop="kFormDesign.deleteComponentByKey(element.key)"></i>
    </div>
    <div class="widget-view-drag widget-col-drag" v-if="kFormDesign.widgetFormSelect.key == element.key">
      <i class="iconfont icon-drag drag-widget"></i>
    </div>
  </el-row>
</template>

<script>
import Draggable from 'vuedraggable'
import BasicComponent from "./BasicComponent"
import KFormItemComponent from "./KFormItemComponent"
import { parseOptions } from './componentsConfig';

export default {
  components: {
    Draggable,
    BasicComponent,
    KFormItemComponent
  },
  props: ['element'],
  inject: ['kFormDesign'],
  data () {
    return {
    }
  },
  mounted () {
    // document.body.ondrop = function (event) {
    //   let isFirefox = navigator.userAgent.toLowerCase().indexOf('firefox') > -1
    //   if (isFirefox) {
    //     event.preventDefault()
    //     event.stopPropagation()
    //   }
    // }
  },
  methods: {
    addColComponent: function(data, colIndex, event){
      const newIndex = event.newIndex
      let parentKey = this.element.key;

      if(this.element.columns[newIndex].key){  // key存在 => 从别的容器中拖拉的组件，修改部分参数
        this.$set(this.element.columns[colIndex].list, newIndex, {
          ...this.element.columns[colIndex].list[newIndex],
          inForm: this.element.inForm,
          parentKey: parentKey,
        })
      } else {
        let key = Date.parse(new Date()) + '_' + Math.ceil(Math.random() * 99999);
        this.$set(this.element.columns[colIndex].list, newIndex, {
          ...this.element.columns[colIndex].list[newIndex],
          key: key,
          model: this.element.columns[colIndex].list[newIndex].type.replaceAll("-","_") + '_' + key,
          inForm: this.element.inForm,
          options: parseOptions(this.element.columns[colIndex].list[newIndex].type),
        })
      }

      // 选中元素
      this.kFormDesign.widgetFormSelect = this.element.columns[colIndex].list[newIndex]
    },
  },
  watch: {
  }
}
</script>

<style lang="scss" scoped>   //必须要加上“<style lang="scss" scoped>，否则引入失败（提示保存）
  @import '../styles/cover.scss';
  @import '../styles/index.scss';

</style>
