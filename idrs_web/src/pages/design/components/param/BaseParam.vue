<template>
  <div class="widget-param" v-if="element && element.key" :key="element.key">
    <template v-if="element.inForm && element.formItem">
      <el-card header="k-form-item属性">
        <KFormItemParam :element="element.formItem"></KFormItemParam>
      </el-card>
    </template>

    <el-form ref="form" :model="element" label-position="top" label-width="80px">
      <el-card header="基础属性">
        <SelectOptions v-model="element.selectOptions" :type="element.type"></SelectOptions>
        <!-- 动态组装参数 -->
        <DynamicParam :element="element"></DynamicParam>
      </el-card>
      <el-card header="自定义属性">
        <FormCustomAttr :customAttrs="element.customAttrs"></FormCustomAttr>
      </el-card>
      <el-card header="函数">
        <FormCodeEditor v-for="(item, index) in element.methods" :key="'func_'+index" :item="item" :remove="() => { element.methods.splice(index, 1); }"></FormCodeEditor>
        <el-button type="primary" icon="el-icon-add" style="width:100%" @click="() => { element.methods.push({functionName: '', body: '方法名: function(){\n}', propertyName: ''}) }">添加函数</el-button>
      </el-card>
      <el-card header="事件">
        <FormCodeEditor v-for="(item, index) in element.events" :key="'event_'+index" :item="item" event :remove="() => { element.events.splice(index, 1); }"></FormCodeEditor>
        <el-button type="primary" icon="el-icon-add" style="width:100%" @click="() => { element.events.push({functionName: '', body: '方法名: function(){\n}', propertyName: ''}) }">添加事件</el-button>
      </el-card>
      <el-card header="样式">
        <style-setting :element="element"></style-setting>
      </el-card>
    </el-form>
  </div>
</template>

<script>
import Draggable from "vuedraggable";
import KCodeEditor from "../KCodeEditor.vue";
import SelectOptions from "../base/SelectOptions.vue";
import FormCustomAttr from "../base/FormCustomAttr.vue";
import FormCodeEditor from "../base/FormCodeEditor.vue";
import KFormItemParam from "./KFormItemParam.vue";

import StyleSetting from "@/pages/design/components/base/StyleSetting.vue";
import DynamicParam from "@/pages/design/components/base/DynamicParam.vue";

// import borderTop from "@/assets/icons/border_top.svg";

export default {
  components: {
    Draggable,
    KCodeEditor,
    SelectOptions,
    FormCustomAttr,
    FormCodeEditor,
    KFormItemParam,
    StyleSetting,
    DynamicParam
  },
  props: ["element", "selectWidget"],
  inject: ["kFormDesign"],
  data() {
    return {
      activeBorder: '',
      backgroundPositionSuggest: [
        {value:"top left"},
        {value:"top center"},
        {value:"top right"},
        {value:"center left"},
        {value:"center center"},
        {value:"center right"},
        {value:"bottom left"},
        {value:"bottom center"},
        {value:"bottom right"},
        {value:"x% y%"}
      ]
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
    querySearch(queryString, cb) {
      var results = queryString ? this.backgroundPositionSuggest.filter(this.createFilter(queryString)) : this.backgroundPositionSuggest;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },
    createFilter(queryString) {
      return (row) => {
        return (row.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
      };
    },
    selectBorder: function(border){
      // console.log(" border ");
      this.activeBorder = border;
    }
  },
  watch: {},
};
</script>

<style lang="scss" scoped>
//必须要加上“<style lang="scss" scoped>，否则引入失败（提示保存）
@import "../../styles/cover.scss";
@import "../../styles/index.scss";

.widget-param {
  /deep/ .el-input-group__append .el-color-picker__trigger{
    height: 23px;
    width: 23px;
  }
}
</style>
