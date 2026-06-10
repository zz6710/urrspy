<template>
  <div ref="formCodeEditor" :class="{ formCodeEditor:border }">
    <el-form-item>
      <div slot="label">
        <el-input placeholder="请输入内容" v-model="item.propertyName" v-if="event">
          <template slot="prepend">事件名：</template>
        </el-input>
        <label>函数名：{{ item.functionName }}</label>
        <el-button size="mini" @click="() => { $refs[refId].open() }">编辑</el-button>
        <el-button size="mini" type="danger" @click="remove" v-if="remove">删除</el-button>
      </div>
      <CodeEditor :ref="refId" v-model="item.body"/>
    </el-form-item>
  </div>
</template>

<script>
import CodeEditor from './CodeEditor.vue';

export default {
  name: "formCodeEditor",
  components: {
    CodeEditor
  },
  props: {
    item: {
      type: Object,
      default: {}
    },
    event: {
      type: Boolean,
      default: false
    },
    border: {
      type: Boolean,
      default: false
    },
    remove: {
      type: Function,
    }
  },
  data () {
    return {
      refId: "ref_0"
    }
  },
  created (){
    if(!this.item.functionName){
      this.item.functionName = this.parseFunctionName(this.item.body);
    }
    this.refId = 'ref_' + Math.ceil(Math.random() * 99999);
  },
  mounted () {

  },
  methods: {
    parseFunctionName(content){ 
      if(!content){
        return "";
      } else if(content.indexOf(":") > -1){
        return content.substring(0, content.indexOf(":")).trim();
      } else if(content.indexOf("(") > -1){
        return content.substring(0, content.indexOf("(")).trim();
      } else {
        console.log(" 函数格式有误! ");
        return "";
      }
    },
  },
  watch: {
    'item.body': {
      handler(newVal, oldVal){
        this.item.functionName = this.parseFunctionName(newVal);
      },
      deep: true
    }
  }
}
</script>

<style lang="scss" scoped>   //必须要加上“<style lang="scss" scoped>，否则引入失败（提示保存）
  .formCodeEditor {
    border: 2px solid #dcdfe6;
    // margin-top: 10px;
    margin-bottom: 10px; 
    padding: 5px;
    border-radius: 10px;
  }

</style>
