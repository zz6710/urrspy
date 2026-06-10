<template>
  <div>
    <div style="margin-left: 22px;">
      <el-button type="text" @click="handleAdd">添加el-collapse-item</el-button>
    </div>
    <draggable tag="ul" :list="element.list" v-bind="{ group: { name: 'options' }, ghostClass: 'ghost', handle: '.drag-item' }" handle=".drag-item" class="column_ul">
      <li v-for="(item, index) in element.list" :key="index">
        <i class="drag-item" style="font-size: 16px; margin: 0 5px; cursor: move"><i class="iconfont icon-icon_bars"></i></i>
        <label style="margin-right: 5px">
          <el-input style="width: 100px" size="mini" placeholder="面板标题" v-model="item.options.title" clearable></el-input>
          <el-input style="width: 100px" size="mini" placeholder="唯一标志符" v-model="item.options.name" clearable></el-input>
          <el-select style="width: 100px" size="mini" placeholder="是否禁用" v-model="item.options.disabled" clearable>
            <el-option label="是" :value="true"></el-option>
            <el-option label="否" :value="false"></el-option>
          </el-select>
        </label>
        <el-button type="primary" size="mini" icon="el-icon-edit" circle @click="handleOptionsEdit(item)"></el-button>
        <el-button type="danger" size="mini" icon="el-icon-delete" circle @click="handleOptionsRemove(index)"></el-button>
      </li>
    </draggable>
    <!-- 字段信息修改 -->
    <el-collapse-item-param v-if="showDialog" :showDialog.sync="showDialog" :element="current" />
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
import ElCollapseItemParam from './ElCollapseItemParam'
import { parseOptions, findComponent } from '../componentsConfig';

export default {
  components: {
    Draggable,
    KCodeEditor,
    SelectOptions,
    FormCustomAttr,
    FormCodeEditor,
    KFormItemParam,
    ElCollapseItemParam
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
    handleAdd() {
      let config = findComponent("el-collapse-item");
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
.divider /deep/ .el-divider__text {
	width: 203px;
}
</style>