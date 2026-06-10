<template>
  <div class="widget-param"
    v-if="element && element.key"
    :key="element.key"
  >
    <template v-if="element.inForm">
      <el-divider content-position="center">k-form-item</el-divider>
      <KFormItemParam :element="element.formItem"></KFormItemParam>
    </template>

    <el-divider content-position="center">组件属性</el-divider>
    <SelectOptions v-model="element.selectOptions" :type="element.type"></SelectOptions>
    <el-form ref="form" :model="element" label-position="top" label-width="80px">
      <el-form-item label="数据字段">
        <el-input v-model="element.model"></el-input>
      </el-form-item>
      <el-form-item label="允许为空" v-if="element.selectOptions.indexOf('dataAllowblank') > -1">
        <el-select v-model="element.options.dataAllowblank" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="禁用" v-if="element.selectOptions.indexOf('dataDisabled') > -1">
        <el-select v-model="element.options.dataDisabled" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="开启的值" v-if="element.selectOptions.indexOf('dataOnValue') > -1">
        <el-input v-model="element.options.dataOnValue"></el-input>
      </el-form-item>
      <el-form-item label="关闭的值" v-if="element.selectOptions.indexOf('dataOffValue') > -1">
        <el-input v-model="element.options.dataOffValue"></el-input>
      </el-form-item>
      <el-form-item label="确认框" v-if="element.selectOptions.indexOf('dataConfirm') > -1">
        <el-select v-model="element.options.dataConfirm" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="打开调用接口" v-if="element.selectOptions.indexOf('dataOnAction') > -1">
        <el-input v-model="element.options.dataOnAction"></el-input>
      </el-form-item>
      <el-form-item label="关闭调用接口" v-if="element.selectOptions.indexOf('dataOffAction') > -1">
        <el-input v-model="element.options.dataOffAction"></el-input>
      </el-form-item>
      <el-form-item label="提交参数" v-if="element.selectOptions.indexOf('dataParams') > -1">
        <el-input v-model="element.options.dataParams"></el-input>
      </el-form-item>
      <el-form-item label="打开时确认框提示信息" v-if="element.selectOptions.indexOf('dataOnConfirmInfo') > -1">
        <el-input v-model="element.options.dataOnConfirmInfo"></el-input>
      </el-form-item>
      <el-form-item label="关闭时确认框提示信息" v-if="element.selectOptions.indexOf('dataOffConfirmInfo') > -1">
        <el-input v-model="element.options.dataOffConfirmInfo"></el-input>
      </el-form-item>

      <el-form-item label="执行前回调函数" v-if="element.selectOptions.indexOf('dataBeforeHandler') > -1">
        <el-select v-model="element.options.dataBeforeHandler" placeholder="请选择">
          <el-option :label="item" :value="item" v-for="(item, index) in getMethodList(element.methods)" :key="item + '_' + index"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="执行后回调函数" v-if="element.selectOptions.indexOf('dataAfterHandler') > -1">
        <el-select v-model="element.options.dataAfterHandler" placeholder="请选择">
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
import SelectOptions from '../base/SelectOptions.vue';
import FormCodeEditor from '../base/FormCodeEditor.vue';
import ParamUtils from '../../utils/param.js';
import FormCustomAttr from '../base/FormCustomAttr.vue';
import KFormItemParam from './KFormItemParam.vue';

export default {
  components: {
    Draggable,
    SelectOptions,
    FormCodeEditor,
    FormCustomAttr,
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
