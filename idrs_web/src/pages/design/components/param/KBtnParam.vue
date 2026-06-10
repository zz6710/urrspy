<template>
  <div class="widget-param"
    v-if="element && element.key"
    :key="element.key"
  >
    <SelectOptions v-model="element.selectOptions" :type="element.type"></SelectOptions>
    <el-form ref="form" :model="element" label-position="top" label-width="80px">
      <el-form-item label="class">
        <el-select v-model="element.class" multiple filterable allow-create clearable placeholder="请选择">
          <el-option-group
            v-for="group in classGroupArr"
            :key="group.label"
            :label="group.label">
            <el-option
              v-for="item in group.childer"
              :key="item.value"
              :label="item.label"
              :value="item.value">
              <span style="float: left">{{ item.label }}</span>
              <span style="float: right; color: #8492a6; font-size: 12px">{{ item.value }}</span>
            </el-option>
          </el-option-group>
        </el-select>
      </el-form-item>

      <el-form-item label="按钮名称">
        <el-input v-model="element.options.btnName"></el-input>
      </el-form-item>

      <el-form-item label="选择图标">
        <el-input placeholder="" v-model="element.icon.value" class="input-with-select">
          <template slot="prepend">{{element.icon.type}}</template>
          <el-button slot="append" @click="openSelectIcon">选择</el-button>
        </el-input>
      </el-form-item>

      <el-form-item label="按钮描述" v-if="element.selectOptions.indexOf('dataDescript') > -1">
        <el-input v-model="element.options.dataDescript"></el-input>
      </el-form-item>
      <el-form-item label="目标名称" v-if="element.selectOptions.indexOf('dataTarget') > -1">
        <el-input v-model="element.options.dataTarget"></el-input>
      </el-form-item>
      <el-form-item label="默认参数" v-if="element.selectOptions.indexOf('dataParams') > -1">
        <el-input v-model="element.options.dataParams"></el-input>
      </el-form-item>
      <el-form-item label="禁用" v-if="element.selectOptions.indexOf('dataDisabled') > -1">
        <el-select v-model="element.options.dataDisabled" placeholder="请选择" clearable>
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="禁用时置灰" v-if="element.selectOptions.indexOf('dataDisabledGrey') > -1">
        <el-select v-model="element.options.dataDisabledGrey" placeholder="请选择" clearable>
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="大小" v-if="element.selectOptions.indexOf('dataSize') > -1">
        <el-select v-model="element.options.dataSize" placeholder="请选择" clearable>
          <el-option label="small" value="small"></el-option>
          <el-option label="primary" value="primary"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="按钮类型" v-if="element.selectOptions.indexOf('dataFunctype') > -1">
        <el-select v-model="element.options.dataFunctype" placeholder="请选择" clearable>
          <el-option label="重置表单" value="RESET"></el-option>
          <el-option label="提交" value="SUBMIT"></el-option>
          <el-option label="导出表格" value="EXPORT"></el-option>
          <el-option label="打开弹框" value="POPUP"></el-option>
          <el-option label="关闭弹框" value="CLOSE"></el-option>
          <el-option label="打开页面" value="PAGE"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="提交请求的地址(Action)" v-if="element.selectOptions.indexOf('dataAction') > -1">
        <el-input v-model="element.options.dataAction"></el-input>
      </el-form-item>
      <el-form-item label="提交请求的地址(Graphql)" v-if="element.selectOptions.indexOf('dataGraphql') > -1">
        <el-input v-model="element.options.dataGraphql"></el-input>
      </el-form-item>
      <el-form-item label="提交请求的地址(dataUrl)" v-if="element.selectOptions.indexOf('dataUrl') > -1">
        <el-input v-model="element.options.dataUrl"></el-input>
      </el-form-item>
      <el-form-item label="提交确认" v-if="element.selectOptions.indexOf('dataConfirm') > -1">
        <el-select v-model="element.options.dataConfirm" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="指定表单" v-if="element.selectOptions.indexOf('dataFrom') > -1">
        <el-input v-model="element.options.dataFrom"></el-input>
      </el-form-item>
      <el-form-item label="提交参数" v-if="element.selectOptions.indexOf('dataModel') > -1">
        <el-input v-model="element.options.dataModel"></el-input>
      </el-form-item>
      <el-form-item label="悬浮提示信息" v-if="element.selectOptions.indexOf('dataDescript') > -1">
        <el-input v-model="element.options.dataDescript"></el-input>
      </el-form-item>
      <el-form-item label="是否校验表单" v-if="element.selectOptions.indexOf('dataValidateForm') > -1">
        <el-select v-model="element.options.dataValidateForm" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="导出文件名" v-if="element.selectOptions.indexOf('dataExportName') > -1">
        <el-input v-model="element.options.dataExportName"></el-input>
      </el-form-item>
      <el-form-item label="下载文件名" v-if="element.selectOptions.indexOf('dataDownloadName') > -1">
        <el-input v-model="element.options.dataDownloadName"></el-input>
      </el-form-item>
      <el-form-item label="提交前回调函数" v-if="element.selectOptions.indexOf('dataHandler') > -1">
        <el-select v-model="element.options.dataHandler" placeholder="请选择" clearable>
          <el-option :label="item" :value="item" v-for="(item, index) in getMethodList(element.methods)" :key="item + '_' + index"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="提交成功回调函数" v-if="element.selectOptions.indexOf('dataAfterSuccess') > -1">
        <el-select v-model="element.options.dataAfterSuccess" placeholder="请选择" clearable>
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

    <el-dialog
      width="50%"
      title="选择图标"
      :visible.sync="visible">
      <select-icon @select="handleSelect"></select-icon>
    </el-dialog>
  </div>
</template>

<script>
import Draggable from 'vuedraggable'
import KCodeEditor from '../KCodeEditor.vue';
import SelectOptions from '../base/SelectOptions.vue';
import FormCodeEditor from '../base/FormCodeEditor.vue';
import ParamUtils from '../../utils/param.js';
import FormCustomAttr from '../base/FormCustomAttr.vue';
import SelectIcon from '../base/SelectIcon.vue';

export default {
  components: {
    Draggable,
    KCodeEditor,
    SelectOptions,
    FormCodeEditor,
    FormCustomAttr,
    SelectIcon
  },
  props: ['element', 'selectWidget'],
  inject: ['kFormDesign'],
  data () {
    return {
      visible: false,
      classGroupArr: [
        {
          label: "基础",
          childer: [{
            label: "图标",
            value: "md-just-icon"
          },{
            label: "透明",
            value: "md-simple"
          },{
            label: "md-button",
            value: "md-button"
          },{
            label: "边框",
            value: "btn-border"
          }]
        }, {
          label: "OA主题色",
          childer: [{
            label: "主要",
            value: "btn-primary"
          },{
            label: "信息",
            value: "btn-info"
          }]
        }, {
          label: "主题色",
          childer: [{
            label: "主要",
            value: "md-primary"
          },{
            label: "成功",
            value: "md-success"
          },{
            label: "信息",
            value: "md-info"
          },{
            label: "警告",
            value: "md-warning"
          },{
            label: "危险",
            value: "md-danger"
          },]
        }
      ]
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
    getMethodList: ParamUtils.getMethodList,
    openSelectIcon: function(){
      this.visible = true;
    },
    handleSelect: function(icon){
      this.element.icon = icon;
      this.visible = false;
      console.log(" handle select ", icon);
    }
  },
  watch: {
  }
}
</script>

<style lang="scss" scoped>   //必须要加上“<style lang="scss" scoped>，否则引入失败（提示保存）
  @import '../../styles/cover.scss';
  @import '../../styles/index.scss';

  

</style>
