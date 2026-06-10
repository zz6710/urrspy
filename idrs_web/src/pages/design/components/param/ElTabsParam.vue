<template>
  <div>
    <div style="margin-left: 22px;">
      <el-button type="text" @click="handleAdd">添加el-tab-pane</el-button>
    </div>
    <draggable tag="ul" :list="element.list" v-bind="{ group: { name: 'options' }, ghostClass: 'ghost', handle: '.drag-item' }" handle=".drag-item" class="column_ul">
      <li v-for="(item, index) in element.list" :key="index">
        <i class="drag-item" style="font-size: 16px; margin: 0 5px; cursor: move"><i class="iconfont icon-icon_bars"></i></i>
        <label style="margin-right: 5px">
          <el-input style="width: 90px" size="mini" placeholder="选项卡标题" v-model="item.options.label"></el-input>
          <el-input style="width: 90px" size="mini" placeholder="别名" v-model="item.options.name"></el-input>
        </label>
        <el-button type="primary" size="mini" icon="el-icon-edit" circle @click="handleOptionsEdit(item)"></el-button>
        <el-button type="danger" size="mini" icon="el-icon-delete" circle @click="handleOptionsRemove(index)"></el-button>
      </li>
    </draggable>
    <!-- 字段信息修改 -->
    <el-tab-pane-param v-if="showDialog" :showDialog.sync="showDialog" :element="current" />
  </div>
</template>

<script>
import Draggable from "vuedraggable";
import KCodeEditor from "../KCodeEditor.vue";
import SelectOptions from "../base/SelectOptions.vue";
import FormCustomAttr from "../base/FormCustomAttr.vue";
import FormCodeEditor from "../base/FormCodeEditor.vue";
import KFormItemParam from "./KFormItemParam.vue";
import ParamUtils from '../../utils/param.js';
import ElTabPaneParam from './ElTabPaneParam.vue'
import { parseOptions, findComponent } from '../componentsConfig';

export default {
  components: {
    Draggable,
    KCodeEditor,
    SelectOptions,
    FormCustomAttr,
    FormCodeEditor,
    KFormItemParam,
    ElTabPaneParam
  },
  props: ["element", "selectWidget"],
  inject: ["kFormDesign"],
  data() {
    return {
      showDialog: false,
      current: {}
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
    handleAdd(){
      let config = findComponent("el-tab-pane");
      this.element.list.push({
        ...config,
        key: Date.parse(new Date()) + '_' + Math.ceil(Math.random() * 99999),
        options: parseOptions(config.type)
      })
    },
    handleOptionsEdit: function (item) {
      this.current = item;
      this.showDialog = true;
    },
    handleOptionsRemove(index) {
      console.log(" remove index ", index);
      this.element.list.splice(index, 1)
    },
    getMethodList: ParamUtils.getMethodList

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