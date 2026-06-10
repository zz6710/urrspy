<template>
  <div class="widget-param"
  >
    <el-dialog title="修改表单字段" 
      :visible.sync="showDialog"
      width="50%"
      :modal='false'
    >
      <SelectOptions v-model="element.selectOptions" title="" :type="element.type"></SelectOptions>
      <el-divider content-position="left"></el-divider>
      <el-form ref="form" :model="element"  label-position="right" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="表头名称">
              <el-input v-model="element.options.dataHeader"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="字段名称">
              <el-input v-model="element.options.dataName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="列宽(px)">
              <el-input v-model="element.options.dataWidth"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="开启排序">
              <el-select v-model="element.options.dataSortable" placeholder="请选择">
                <el-option label="是" :value="true"></el-option>
                <el-option label="否" :value="false"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="	是否隐藏">
              <el-select v-model="element.options.dataHidden" placeholder="请选择">
                <el-option label="是" :value="true"></el-option>
                <el-option label="否" :value="false"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="固定位置">
              <el-select v-model="element.options.dataFixed" placeholder="请选择">
                <el-option label="左侧" value="left"></el-option>
                <el-option label="右侧" value="right"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="对齐方式">
              <el-select v-model="element.options.dataAlign" placeholder="请选择">
                <el-option label="靠左" value="left"></el-option>
                <el-option label="居中" value="center"></el-option>
                <el-option label="靠右" value="right"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="内容过长显示提示">
              <el-select v-model="element.options.dataOverflow" placeholder="请选择">
                <el-option label="是" :value="true"></el-option>
                <el-option label="否" :value="false"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据字典">
              <el-input v-model="element.options.dataDict"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="格式化展示数据">
              <el-select v-model="element.options.dataType" placeholder="请选择">
                <el-option label="date" value="date"></el-option>
                <el-option label="time" value="time"></el-option>
                <el-option label="money" value="money"></el-option>
                <el-option label="timestamp" value="timestamp"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单元格渲染回调">
              <el-select v-model="element.options.dataRender" placeholder="请选择">
                <el-option :label="item" :value="item" v-for="(item, index) in getMethodList(element.methods)" :key="item + '_' + index"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="排序函数">
              <el-select v-model="element.options.dataSortMethod" placeholder="请选择">
                <el-option :label="item" :value="item" v-for="(item, index) in getMethodList(element.methods)" :key="item + '_' + index"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="center">函数</el-divider>
        <FormCodeEditor v-for="(item, index) in element.methods" :key="'func_'+index" :item="item" :remove="() => { element.methods.splice(index, 1); }"></FormCodeEditor>
        <el-button type="primary" icon="el-icon-add" style="width:100%" @click="() => { element.methods.push({functionName: '', body: '方法名: function(){\n}', propertyName: ''}) }">添加函数</el-button>
      
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="showDialog = false">取 消</el-button>
        <el-button type="primary" @click="handleEditData()">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import Draggable from 'vuedraggable'
import KCodeEditor from '../KCodeEditor.vue';
import SelectOptions from '../base/SelectOptions.vue';
import FormCodeEditor from '../base/FormCodeEditor.vue';
import ParamUtils from '../../utils/param.js';

export default {
  components: {
    Draggable, KCodeEditor, SelectOptions, FormCodeEditor
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
  data () {
    return {
      demoData: "",
      showDialog: true,

    }
  },
  created (){
    console.log(" element ", this.element);
  },
  mounted () {
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
    getParentRef: function(ref) {
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
    handleEditData: function(){
      this.showDialog = false;
    },
    getMethodList: ParamUtils.getMethodList
  },
  watch: {
    showDialog(value) {
      this.$emit("update:showDialog", value);
    },
  }
}
</script>

<style lang="scss" scoped>   //必须要加上“<style lang="scss" scoped>，否则引入失败（提示保存）
  @import '../../styles/cover.scss';
  @import '../../styles/index.scss';

  .column_ul {
    overflow-x: auto;
		white-space:nowrap;
  }

</style>
