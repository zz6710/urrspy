<template>
  <div class="widget-param">
    <el-dialog title="修改collapseItem字段" :visible.sync="showDialog" width="50%" :modal='false'>
      <SelectOptions v-model="element.selectOptions" title="" :type="element.type"></SelectOptions>
      <el-divider content-position="left"></el-divider>
      <el-form ref="form" :model="element" label-position="right" label-width="auto">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="自定义面板标题">
              <el-input placeholder="" v-model="element.options.icon.value" class="input-with-select" clearable>
                <template slot="prepend">{{element.options.icon.type}}</template>
                <el-button slot="append" @click="openSelectIcon">选择</el-button>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="element.selectOptions.indexOf('name') > -1">
            <el-form-item label="唯一标志符">
              <el-input v-model="element.options.name" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="element.selectOptions.indexOf('title') > -1">
            <el-form-item label="面板标题">
              <el-select v-model="element.options.title" placeholder="请选择" style="width:100%" clearable>
                <el-option label="是" :value="true"></el-option>
                <el-option label="否" :value="false"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="element.selectOptions.indexOf('disabled') > -1">
            <el-form-item label="是否禁用">
              <el-select v-model="element.options.disabled" placeholder="请选择" style="width:100%">
                <el-option label="是" :value="true"></el-option>
                <el-option label="否" :value="false"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="showDialog = false">取 消</el-button>
        <el-button type="primary" @click="handleEditData()">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog width="50%" title="选择图标" :visible.sync="visible">
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
import SelectIcon from '../base/SelectIcon.vue';

export default {
  components: {
    Draggable, KCodeEditor, SelectOptions, FormCodeEditor, SelectIcon
  },
  props: {
    element: {
      type: Object
    },
    selectWidget: {
      type: Object
    },
    current: {
      type: Object
    },
    callback: {
      type: Function,
    }
  },
  inject: ['kFormDesign'],
  data() {
    return {
      demoData: "",
      showDialog: true,
      visible: false,

    }
  },
  created() {
    console.log(" element ", this.element);
  },
  mounted() {
    // document.body.ondrop = function (event) {
    //   let isFirefox = navigator.userAgent.toLowerCase().indexOf('firefox') > -1
    //   if (isFirefox) {
    //     event.preventDefault()
    //     event.stopPropagation()
    //   }
    // }
  },
  methods: {

    //根据引用名获取父引用对象
    getParentRef: function (ref) {
      var parent = this.$parent || this.$root;
      var $ref = parent.$refs[ref];

      while (parent && !$ref) {
        parent = parent.$parent;

        if (parent) {
          $ref = parent.$refs[ref];
        }
      }
      return $ref;
    },

    handleOptionsRemove: function (index) {
      console.log(" remove index ", index);
      this.element.list.splice(index, 1)
    },
    handleEditData: function () {
      this.showDialog = false;
    },
    getMethodList: ParamUtils.getMethodList,
    openSelectIcon: function () {
      this.visible = true;
    },
    handleSelect: function (icon) {
      this.element.options.icon = icon;
      this.visible = false;
    }
  },
  watch: {
    showDialog(value) {
      this.$emit("update:showDialog", value);
    },
  }
}
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
