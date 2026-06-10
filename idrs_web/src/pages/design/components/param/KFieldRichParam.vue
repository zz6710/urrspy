<template>
  <div class="widget-param"
    v-if="element && element.key"
    :key="element.key"
  >
    <template v-if="element.inForm">
      <el-divider content-position="center">k-form-item</el-divider>
      <KFormItemParam :element="element.formItem"></KFormItemParam>
    </template>
    <SelectOptions v-model="element.selectOptions" :type="element.type"></SelectOptions>
    <el-form ref="form" :model="element"  label-position="top" label-width="80px">
      <el-form-item label="数据字段">
        <el-input v-model="element.model"></el-input>
      </el-form-item>
      <el-form-item label="自定义菜单" v-if="element.selectOptions.indexOf('dataMenus') > -1">
        <el-select v-model="element.options.dataMenus" multiple placeholder="请选择">
          <el-option label="标题" value="head"></el-option>
          <el-option label="粗体" value="bold"></el-option>
          <el-option label="字号" value="fontSize"></el-option>
          <el-option label="字体" value="fontName"></el-option>
          <el-option label="斜体" value="italic"></el-option>
          <el-option label="下划线" value="underline"></el-option>
          <el-option label="删除线" value="strikeThrough"></el-option>
          <el-option label="文字颜色" value="foreColor"></el-option>
          <el-option label="背景颜色" value="backColor"></el-option>
          <el-option label="插入链接" value="link"></el-option>
          <el-option label="列表" value="list"></el-option>
          <el-option label="对齐方式" value="justify"></el-option>
          <el-option label="引用" value="quote"></el-option>
          <el-option label="表情" value="emoticon"></el-option>
          <el-option label="插入图片" value="image"></el-option>
          <el-option label="表格" value="table"></el-option>
          <el-option label="插入视频" value="video"></el-option>
          <el-option label="插入代码" value="code"></el-option>
          <el-option label="撤销" value="undo"></el-option>
          <el-option label="重复" value="redo"></el-option>
          <el-option label="全屏" value="fullscreen"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="插入文字和链接的校验" v-if="element.selectOptions.indexOf('dataLinkCheck') > -1">
        <el-select v-model="element.options.dataLinkCheck" placeholder="请选择">
          <el-option :label="item" :value="item" v-for="(item, index) in getMethodList(element.methods)" :key="item + '_' + index"></el-option>
        </el-select>
      </el-form-item>

      <el-divider content-position="center">自定义属性</el-divider>
      <FormCustomAttr :customAttrs="element.customAttrs"></FormCustomAttr>

      <el-divider content-position="center">函数</el-divider>
      <FormCodeEditor v-for="(item, index) in element.methods" :key="'func_'+index" :item="item" :remove="() => { element.methods.splice(index, 1); }"></FormCodeEditor>
      <el-button type="primary" icon="el-icon-add" style="width:100%" @click="() => { element.methods.push({functionName: '', body: '方法名: function(){\n}', propertyName: ''}) }">添加函数</el-button>

      <el-divider content-position="center">事件</el-divider>
      <FormCodeEditor v-for="(item, index) in element.events" :key="'event_'+index" :item="item" event :remove="() => { element.events.splice(index, 1); }"></FormCodeEditor>
      <el-button type="primary" icon="el-icon-add" style="width:100%" @click="() => { element.events.push({functionName: '', body: '方法名: function(){\n}', propertyName: ''}) }">添加事件</el-button>
    </el-form>
  </div>
</template>

<script>
import Draggable from 'vuedraggable'
import KCodeEditor from '../KCodeEditor.vue';
import ParamUtils from '../../utils/param.js';
import SelectOptions from '../base/SelectOptions.vue';
import FormCustomAttr from '../base/FormCustomAttr.vue';
import FormCodeEditor from '../base/FormCodeEditor.vue';
import KFormItemParam from './KFormItemParam.vue';

export default {
  components: {
    Draggable,
    KCodeEditor,
    SelectOptions,
    FormCustomAttr,
    FormCodeEditor,
    KFormItemParam
  },
  props: ['element', 'selectWidget'],
  inject: ['kFormDesign'],
  data () {
    return {
    }
  },
  created (){
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
    handleWidgetClone (element) {
      console.log(" clone component ", element);
    },
    getMethodList: ParamUtils.getMethodList
  },
  watch: {
  }
}
</script>

<style lang="scss" scoped>   //必须要加上“<style lang="scss" scoped>，否则引入失败（提示保存）
  @import '../../styles/cover.scss';
  @import '../../styles/index.scss';

</style>
