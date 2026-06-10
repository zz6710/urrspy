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
      <el-form-item label="允许为空">
        <el-select v-model="element.options.dataAllowblank" placeholder="请选择" v-if="element.selectOptions.indexOf('dataAllowblank') > -1">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="禁用" v-if="element.selectOptions.indexOf('dataDisabled') > -1">
        <el-select v-model="element.options.dataDisabled" placeholder="请选择" >
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="组件大小" v-if="element.selectOptions.indexOf('dataAllowblank') > -1">
        <el-select v-model="element.options.dataSize" placeholder="请选择">
          <el-option label="mini" value="mini"></el-option>
          <el-option label="small" value="small"></el-option>
          <el-option label="medium" value="medium"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="文本颜色" v-if="element.selectOptions.indexOf('dataTextColor') > -1">
        <el-color-picker v-model="element.options.dataTextColor"></el-color-picker>
      </el-form-item>
      <el-form-item label="填充色和边框色" v-if="element.selectOptions.indexOf('dataFillColor') > -1">
        <el-color-picker v-model="element.options.dataFillColor"></el-color-picker>
      </el-form-item>
      <el-form-item label="样式" v-if="element.selectOptions.indexOf('dataUiType') > -1">
        <el-select v-model="element.options.dataUiType" placeholder="请选择">
          <el-option label="radio" value="radio"></el-option>
          <el-option label="button" value="button"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="显示边框" v-if="element.selectOptions.indexOf('dataBorder') > -1">
        <el-select v-model="element.options.dataBorder" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="数据字典" v-if="element.selectOptions.indexOf('dataDict') > -1">
        <el-input v-model="element.options.dataDict"></el-input>
      </el-form-item>
      <el-form-item label="自定义数据" label-width="auto" v-if="element.selectOptions.indexOf('dataData') > -1">
        <el-radio-group v-model="element.options.dataData" disabled>
          <draggable tag="ul" :list="element.options.dataData"
            v-bind="{group:{ name:'options'}, ghostClass: 'ghost',handle: '.drag-item'}"
            handle=".drag-item"
            style="padding: 0;"
          >
            <li v-for="(item, index) in element.options.dataData" :key="index" >
              <el-radio
                :label="item.label"
                style="margin-right: 5px;"
              >
                <el-input style="width:90px" size="mini" v-model="item.key"></el-input>
                <el-input style="width:90px;" size="mini" v-model="item.label"></el-input>
              </el-radio>
              <i class="drag-item" style="font-size: 16px;margin: 0 5px;cursor: move;"><i class="iconfont icon-icon_bars"></i></i>
              <el-button @click="handleOptionsRemove(index)" circle plain type="danger" size="mini" icon="el-icon-minus" style="padding: 4px;margin-left: 5px;"></el-button>
            </li>
          </draggable>
        </el-radio-group>
        <div style="margin-left: 22px;">
          <el-button type="text" @click="handleAddOption">添加选项</el-button>
        </div>
      </el-form-item>
      <el-form-item label="查询参数" v-if="element.selectOptions.indexOf('dataParams') > -1">
        <el-input v-model="element.options.dataParams"></el-input>
      </el-form-item>
      <el-form-item label="数据来源Action" v-if="element.selectOptions.indexOf('dataAction') > -1">
        <el-input v-model="element.options.dataAction"></el-input>
      </el-form-item>
      <el-form-item label="数据来源graphql" v-if="element.selectOptions.indexOf('dataGraphql') > -1">
        <el-input v-model="element.options.dataGraphql"></el-input>
      </el-form-item>
      <el-form-item label="值字段名称" v-if="element.selectOptions.indexOf('dataValueField') > -1">
        <el-input v-model="element.options.dataValueField"></el-input>
      </el-form-item>
      <el-form-item label="显示字段名称" v-if="element.selectOptions.indexOf('dataDisplayField') > -1">
        <el-input v-model="element.options.dataDisplayField"></el-input>
      </el-form-item>
      <el-form-item label="字段分隔符" v-if="element.selectOptions.indexOf('dataDisplaySeparator') > -1">
        <el-input v-model="element.options.dataDisplaySeparator"></el-input>
      </el-form-item>
      <el-form-item label="返回对象" v-if="element.selectOptions.indexOf('dataOnObject') > -1">
        <el-input v-model="element.options.dataOnObject"></el-input>
      </el-form-item>
      <el-form-item label="行屏蔽" v-if="element.selectOptions.indexOf('dataValueDisabled') > -1">
        <el-select v-model="element.options.dataValueDisabled" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="行屏蔽条件" v-if="element.selectOptions.indexOf('dataValueMethod') > -1">
        <el-select v-model="element.options.dataValueMethod" placeholder="请选择">
          <el-option :label="item" :value="item" v-for="(item, index) in getMethodList(element.methods)" :key="item + '_' + index"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="加载数据前回调函数" v-if="element.selectOptions.indexOf('dataOnBeforeload') > -1">
        <el-select v-model="element.options.dataOnBeforeload" placeholder="请选择">
          <el-option :label="item" :value="item" v-for="(item, index) in getMethodList(element.methods)" :key="item + '_' + index"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="加载数据后回调函数" v-if="element.selectOptions.indexOf('dataOnAfterload') > -1">
        <el-select v-model="element.options.dataOnAfterload" placeholder="请选择">
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
    handleOptionsRemove (index) {
      this.element.options.dataData.splice(index, 1)
    },
    handleAddOption () {
      this.element.options.dataData.push({
        key:  'key_' + Date.parse(new Date()) + '_' + Math.ceil(Math.random() * 99999),
        label: "新选项"
      })
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
