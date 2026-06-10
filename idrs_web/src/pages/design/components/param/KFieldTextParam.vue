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
      <el-form-item label="数据字段(v-model)">
        <el-input v-model="element.model"></el-input>
      </el-form-item>
      <el-form-item label="占位文本" v-if="element.selectOptions.indexOf('dataPlaceholder') > -1">
        <el-input v-model="element.options.dataPlaceholder"></el-input>
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
      <el-form-item label="最小长度" v-if="element.selectOptions.indexOf('dataMinLength') > -1">
        <el-input-number v-model="element.options.dataMinLength" label=""></el-input-number>
      </el-form-item>
      <el-form-item label="最大长度" v-if="element.selectOptions.indexOf('dataMaxLength') > -1">
        <el-input-number v-model="element.options.dataMaxLength" label=""></el-input-number>
      </el-form-item>
      <el-form-item label="验证类型" v-if="element.selectOptions.indexOf('dataValidateType') > -1">
        <el-select v-model="element.options.dataValidateType" placeholder="请选择">
          <el-option label="电子邮箱" value="email"></el-option>
          <el-option label="数字文本" value="code"></el-option>
          <el-option label="整型数" value="int"></el-option>
          <el-option label="包含小数点的数字" value="number"></el-option>
          <el-option label="邮政编码" value="postcode"></el-option>
          <el-option label="手机号" value="telephone"></el-option>
          <el-option label="金额" value="money"></el-option>
          <el-option label="中文，数字和英文字母" value="text"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="字数统计" v-if="element.selectOptions.indexOf('dataShowWordLimit') > -1">
        <el-select v-model="element.options.dataShowWordLimit" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="显示为密码" v-if="element.selectOptions.indexOf('dataShowPassword') > -1">
        <el-select v-model="element.options.dataShowPassword" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="正则校验" v-if="element.selectOptions.indexOf('dataRegx') > -1">
        <el-input v-model="element.options.dataRegx"></el-input>
      </el-form-item>
      <el-form-item label="正则校验失败信息" v-if="element.selectOptions.indexOf('dataRegxText') > -1">
        <el-input v-model="element.options.dataRegxText"></el-input>
      </el-form-item>
      <el-form-item label="最大值" v-if="element.selectOptions.indexOf('dataMaxValue') > -1">
        <el-input v-model="element.options.dataMaxValue" label=""></el-input>
      </el-form-item>
      <el-form-item label="最小值" v-if="element.selectOptions.indexOf('dataMinValue') > -1">
        <el-input v-model="element.options.dataMinValue" label=""></el-input>
      </el-form-item>
      <el-form-item label="整数位数" v-if="element.selectOptions.indexOf('dataIntegerLength') > -1">
        <el-input-number v-model="element.options.dataIntegerLength" label=""></el-input-number>
      </el-form-item>
      <el-form-item label="小数位数" v-if="element.selectOptions.indexOf('dataDigits') > -1">
        <el-input-number v-model="element.options.dataDigits" label=""></el-input-number>
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
  },
  watch: {
  }
}
</script>

<style lang="scss" scoped>   //必须要加上“<style lang="scss" scoped>，否则引入失败（提示保存）
  @import '../../styles/cover.scss';
  @import '../../styles/index.scss';

</style>
