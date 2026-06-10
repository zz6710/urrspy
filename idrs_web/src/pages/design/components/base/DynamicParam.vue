<template>
  <div>
    <template v-for="item in options">
      <el-form-item :label="item.label + '(' + item.key + ')'" :key="item.key" v-if="item.extField || element.selectOptions.indexOf(item.key) > -1">
        <!-- 文本输入框 -->
        <el-input v-model="element.options[item.key]" size="mini" v-if="item.paramType=='text'" :placeholder="item.placeholder" clearable></el-input>
        <!-- 数组 -->
        <el-input-number v-model="element.options[item.key]" v-if="item.paramType=='number'" :placeholder="item.placeholder" clearable size="mini"  controls-position="right"></el-input-number>
        <!-- 下拉选择 -->
        <el-select v-model="element.options[item.key]" size="mini" v-if="item.paramType=='select'" placeholder="请选择" clearable>
          <el-option :label="_item.key" :value="_item.value" v-for="(_item, index) in item.dict" :key="_item.key + '_' + index"></el-option>
        </el-select>
        <!-- 下拉分组选择 -->
        <el-select v-model="element.options[item.key]" v-if="item.paramType=='group-select'" multiple filterable allow-create clearable placeholder="请选择">
          <el-option-group
            v-for="group in item.dict"
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
        <!-- 函数选择 -->
        <el-select v-model="element.options[item.key]" size="mini" v-if="item.paramType=='function'" placeholder="请选择" clearable>
          <el-option :label="_item" :value="_item" v-for="(_item, index) in getMethodList(element.methods)" :key="_item + '_' + index"></el-option>
        </el-select>
        <!-- 图标选择 -->
        <el-input placeholder="" v-model="element.options[item.key].value" v-if="item.paramType=='icon'" class="input-with-select">
          <template slot="prepend">{{element.options[item.key].type}}</template>
          <el-button slot="append" @click="openSelectIcon(item.key)">选择</el-button>
        </el-input>
        <!-- 自定义组件 -->
        <component v-if="item.paramType=='component'" :is="item.component" :element="element"></component>
      </el-form-item>
    </template>

    <el-dialog
      append-to-body
      width="50%"
      title="选择图标"
      :visible.sync="visible">
      <select-icon @select="handleSelect"></select-icon>
    </el-dialog>
  </div>
</template>

<script>
import { findComponentOptionsArr  } from '../componentsConfig';
import ParamUtils from '../../utils/param.js';
import SelectIcon from '@/pages/design/components/base/SelectIcon.vue';
import KStepsParam from '@/pages/design/components/param/KStepsParam';
import ElTabsParam from '@/pages/design/components/param/ElTabsParam';
import ElCollapseParam from '@/pages/design/components/param/ElCollapseParam';


export default {
  name: "DynamicParam",
  components: {
    SelectIcon,
    KStepsParam,
    ElTabsParam,
    ElCollapseParam
  },
  props: {
    element: {
      type: Object,
      default: {}
    }
  },
  inject: ["kFormDesign"],
  data () {
    return {
      options: [],
      visible: false,
    }
  },
  created (){
    let _options = findComponentOptionsArr(this.element.type);
    this.options = _options.filter(option => true)
  },
  mounted () {

  },
  methods: {
    getMethodList: ParamUtils.getMethodList,
    openSelectIcon: function(key){
      this.visible = true;
      this.key = key;
    },
    handleSelect: function(icon){
      console.log(" handle select ", this.key, icon);
      this.element.options[this.key] = icon
      this.visible = false;
    }
  },
  watch: {
  }
}
</script>

<style lang="scss" scoped>   //必须要加上“<style lang="scss" scoped>，否则引入失败（提示保存）

</style>
