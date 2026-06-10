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
    <el-form ref="form" :model="element" label-position="top" label-width="80px">
      <el-form-item label="数据字段">
        <el-input v-model="element.model"></el-input>
      </el-form-item>
      <el-form-item label="是否允许为空" v-if="element.selectOptions.indexOf('dataAllowblank')> -1">
        <el-select v-model="element.options.dataAllowblank" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="禁用" v-if="element.selectOptions.indexOf('dataDisabled')> -1">
        <el-select v-model="element.options.dataDisabled" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="文件列表类型" v-if="element.selectOptions.indexOf('dataType')> -1">
        <el-select v-model="element.options.dataType" placeholder="请选择">
          <el-option label="text" value="text"></el-option>
          <el-option label="picture" value="picture"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="拖拽上传" v-if="element.selectOptions.indexOf('dataDrag')> -1">
        <el-select v-model="element.options.dataDrag" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="接受上传文件类型" v-if="element.selectOptions.indexOf('dataAccept')> -1">
        <el-input v-model="element.options.dataAccept"></el-input>
      </el-form-item>
      <el-form-item label="最大上传数量限制" v-if="element.selectOptions.indexOf('dataLimit')> -1">
        <el-input-number v-model="element.options.dataLimit"></el-input-number>
      </el-form-item>
      <el-form-item label="支持多选" v-if="element.selectOptions.indexOf('dataMultiple')> -1">
        <el-select v-model="element.options.dataMultiple" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="选取文件立即上传" v-if="element.selectOptions.indexOf('dataAutoUpload')> -1">
        <el-select v-model="element.options.dataAutoUpload" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="点击文件钩子函数" v-if="element.selectOptions.indexOf('dataPreview') > -1">
        <el-select v-model="element.options.dataPreview" placeholder="请选择">
          <el-option :label="item" :value="item" v-for="(item, index) in getMethodList(element.methods)" :key="item + '_' + index"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="移除文件钩子函数" v-if="element.selectOptions.indexOf('dataRemove') > -1">
        <el-select v-model="element.options.dataRemove" placeholder="请选择">
          <el-option :label="item" :value="item" v-for="(item, index) in getMethodList(element.methods)" :key="item + '_' + index"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="上传成功钩子函数" v-if="element.selectOptions.indexOf('dataSuccess') > -1">
        <el-select v-model="element.options.dataSuccess" placeholder="请选择">
          <el-option :label="item" :value="item" v-for="(item, index) in getMethodList(element.methods)" :key="item + '_' + index"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="上传失败钩子函数" v-if="element.selectOptions.indexOf('dataError') > -1">
        <el-select v-model="element.options.dataError" placeholder="请选择">
          <el-option :label="item" :value="item" v-for="(item, index) in getMethodList(element.methods)" :key="item + '_' + index"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="上传时钩子函数" v-if="element.selectOptions.indexOf('dataProgress') > -1">
        <el-select v-model="element.options.dataProgress" placeholder="请选择">
          <el-option :label="item" :value="item" v-for="(item, index) in getMethodList(element.methods)" :key="item + '_' + index"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="上传文件前钩子" v-if="element.selectOptions.indexOf('dataBeforeUpload') > -1">
        <el-select v-model="element.options.dataBeforeUpload" placeholder="请选择">
          <el-option :label="item" :value="item" v-for="(item, index) in getMethodList(element.methods)" :key="item + '_' + index"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="删除文件前钩子" v-if="element.selectOptions.indexOf('dataBeforeRemove') > -1">
        <el-select v-model="element.options.dataBeforeRemove" placeholder="请选择">
          <el-option :label="item" :value="item" v-for="(item, index) in getMethodList(element.methods)" :key="item + '_' + index"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="文件超出个数钩子" v-if="element.selectOptions.indexOf('dataExceed') > -1">
        <el-select v-model="element.options.dataExceed" placeholder="请选择">
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
import SelectOptions from '../base/SelectOptions.vue';
import FormCustomAttr from '../base/FormCustomAttr.vue';
import FormCodeEditor from '../base/FormCodeEditor.vue';
import ParamUtils from '../../utils/param.js';
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
