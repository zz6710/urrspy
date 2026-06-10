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
      <el-form-item label="类型" v-if="element.selectOptions.indexOf('dataType') > -1">
        <el-select v-model="element.options.dataType" placeholder="请选择">
          <el-option label="date" value="date"></el-option>
          <el-option label="year" value="year"></el-option>
          <el-option label="month" value="month"></el-option>
          <el-option label="dates" value="dates"></el-option>
          <el-option label="week" value="week"></el-option>
          <el-option label="datetime" value="datetime"></el-option>
          <el-option label="datetimerange" value="datetimerange"></el-option>
          <el-option label="daterange" value="daterange"></el-option>
          <el-option label="monthrange" value="monthrange"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="是否允许为空" v-if="element.selectOptions.indexOf('dataAllowblank') > -1">
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
      <el-form-item label="是否可以清空" v-if="element.selectOptions.indexOf('dataClearable') > -1">
        <el-select v-model="element.options.dataClearable" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="最小值" v-if="element.selectOptions.indexOf('dataMinValue') > -1">
        <el-input v-model="element.dataMinValue"></el-input>
      </el-form-item>
      <el-form-item label="最大值" v-if="element.selectOptions.indexOf('dataMaxValue') > -1">
        <el-input v-model="element.dataMaxValue"></el-input>
      </el-form-item>
      <el-form-item label="日期值格式" v-if="element.selectOptions.indexOf('dataValueFormat') > -1">
        <el-input v-model="element.dataValueFormat"></el-input>
      </el-form-item>
      <el-form-item label="日期显示格式" v-if="element.selectOptions.indexOf('dataDateFormat') > -1">
        <el-input v-model="element.dataDateFormat"></el-input>
      </el-form-item>
      <el-form-item label="工作日" v-if="element.selectOptions.indexOf('dataWorkday') > -1">
        <el-select v-model="element.options.dataWorkday" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="工作日方案编号" v-if="element.selectOptions.indexOf('dataWorkdayPgmno') > -1">
        <el-input v-model="element.dataWorkdayPgmno"></el-input>
      </el-form-item>

      <el-form-item label="验证函数" v-if="element.selectOptions.indexOf('dataValidate') > -1">
        <el-select v-model="element.options.dataValidate" placeholder="请选择">
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
