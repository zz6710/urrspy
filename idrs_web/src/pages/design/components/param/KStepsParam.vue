<template>
  <div >
    <div style="margin-left: 22px;">
      <el-button type="text" @click="handleAdd">添加k-step</el-button>
    </div>
    <draggable tag="ul" :list="element.list" v-bind="{ group: { name: 'options' }, ghostClass: 'ghost', handle: '.drag-item' }" handle=".drag-item" class="column_ul">
      <li v-for="(item, index) in element.list" :key="index">
        <i class="drag-item" style="font-size: 16px; margin: 0 5px; cursor: move"><i class="iconfont icon-icon_bars"></i></i>
        <label style="margin-right: 5px">
          <el-input style="width: 90px" size="mini" placeholder="标题" v-model="item.options.dataTitle"></el-input>
          <el-input style="width: 90px" size="mini" placeholder="描述" v-model="item.options.dataDescription"></el-input>
          <el-input style="width: 90px" size="mini" placeholder="图标" v-model="item.options.dataIcon"></el-input>
        </label>
        <el-button type="danger" size="mini" icon="el-icon-delete" circle @click="handleOptionsRemove(index)"></el-button>
      </li>
    </draggable>
  </div>
</template>

<script>
import Draggable from "vuedraggable";

import { parseOptions, findComponent } from '../componentsConfig';

export default {
  components: {
    Draggable
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
    handleOptionsRemove(index) {
      console.log(" remove index ", index);
      this.element.list.splice(index, 1)
    },
    handleAdd() {
      let config = findComponent("k-step");
      this.element.list.push({
        ...config,
        key: Date.parse(new Date()) + '_' + Math.ceil(Math.random() * 99999),
        options: parseOptions(config.type)
      })
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