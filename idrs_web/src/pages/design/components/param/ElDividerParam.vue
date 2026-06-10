<template>
  <div class="widget-param" v-if="element && element.key" :key="element.key">
    <template v-if="element.inForm">
      <el-divider content-position="center">k-form-item</el-divider>
      <KFormItemParam :element="element.formItem"></KFormItemParam>
    </template>
    <SelectOptions v-model="element.selectOptions" :type="element.type"></SelectOptions>
    <el-form ref="form" :model="element" label-position="top" label-width="80px">
      <el-form-item label="分割线方向" v-if="element.selectOptions.indexOf('direction') > -1">
        <el-select style="width:100%" v-model="element.options.direction" placeholder="请选择" clearable>
          <el-option label="horizontal" value="horizontal"></el-option>
          <el-option label="vertical" value="vertical"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="分割线文案的位置" v-if="element.selectOptions.indexOf('contentPosition') > -1">
        <el-select style="width:100%" v-model="element.options.contentPosition" placeholder="请选择" clearable>
          <el-option label="left" value="left"></el-option>
          <el-option label="right" value="right"></el-option>
          <el-option label="center" value="center"></el-option>
        </el-select>
      </el-form-item>
      <el-divider content-position="center">分割文字</el-divider>
      <el-form-item label="">
        <el-input placeholder="自定义图标" v-model="element.icon.value" class="input-with-select" clearable>
          <template slot="prepend">{{element.icon.type}}</template>
          <el-button slot="append" @click="openSelectIcon">选择</el-button>
        </el-input>
      </el-form-item>
      <el-form-item label="" label-width="auto">
        <el-input style="width: 100%" v-model="element.value" clearable placeholder="分割文字"></el-input>
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
    <el-dialog width="50%" title="选择图标" :visible.sync="visible">
      <select-icon @select="handleSelect"></select-icon>
    </el-dialog>
  </div>
</template>

<script>
import Draggable from "vuedraggable";
import KCodeEditor from "../KCodeEditor.vue";
import SelectOptions from "../base/SelectOptions.vue";
import FormCustomAttr from "../base/FormCustomAttr.vue";
import FormCodeEditor from "../base/FormCodeEditor.vue";
import KFormItemParam from "./KFormItemParam.vue";
import ElCollapseItemParam from './ElCollapseItemParam'
import SelectIcon from '../base/SelectIcon.vue';

export default {
  components: {
    Draggable,
    KCodeEditor,
    SelectOptions,
    FormCustomAttr,
    FormCodeEditor,
    KFormItemParam,
    ElCollapseItemParam,
    SelectIcon
  },
  props: ["element", "selectWidget"],
  inject: ["kFormDesign"],
  data() {
    return {
      visible: false,
    };
  },
  created() { },
  mounted() {
    document.body.ondrop = function (event) {
      let isFirefox = navigator.userAgent.toLowerCase().indexOf("firefox") > -1;
      if (isFirefox) {
        event.preventDefault();
        event.stopPropagation();
      }
    };
  },
  methods: {
    openSelectIcon: function () {
      this.visible = true;
    },
    handleSelect: function (icon) {
      this.element.icon = icon;
      this.visible = false;
    }
  },
  watch: {},
};
</script>

<style lang="scss" scoped>
//必须要加上“<style lang="scss" scoped>，否则引入失败（提示保存）
@import "../../styles/cover.scss";
@import "../../styles/index.scss";
.column_ul {
	overflow-x: auto;
	white-space: nowrap;
}
</style>