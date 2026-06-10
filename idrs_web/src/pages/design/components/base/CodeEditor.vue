<template>
  <div ref="codeEditor">
    <div v-bind:style="{width: width, height: height}">
        <editor
            v-model="myValue"
            @init="editorInit"
            @input='codeChange'
            lang="javascript"
            :options="editorOptions"
            theme="chrome"
        ></editor>
    </div>

    <el-dialog
      :title="title"
      :visible.sync="isFull"
      :fullscreen="false"
      :show-close="false"
      :destroy-on-close="true"
      :modal='true'
    >
      <div v-bind:style="{width: dialogWidth, height: dialogHeight}" v-if="isFull">
        <editor
          v-model="myValue"
          @init="editorInit"
          @input='codeChange'
          lang="javascript"
          :options="editorOptions"
          theme="chrome"
        ></editor>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Editor from 'vue2-ace-editor';

export default {
  name: "KCodeEditor",
  components: {
    Editor
  },
  props: {
    value: {
      type: String
    },
    title: {
        type: String,
        default: "编辑"
    },
    width: {
        type: String,
        default: "100%"
    },
    height: {
        type: String,
        default: "200px"
    },
    dialogWidth: {
        type: String,
        default: "100%"
    },
    dialogHeight: {
        type: String,
        default: "500px"
    }
  },
  data () {
    return {
      editorOptions: {
        // 设置代码编辑器的样式
        enableBasicAutocompletion: true,    //启用基本自动完成
        enableSnippets: true,               // 启用代码段
        enableLiveAutocompletion: true,     //启用实时自动完成
        tabSize: 2,                         //标签大小
        fontSize: 18,                       //设置字号
        showPrintMargin: false,             //去除编辑器里的竖线
      },
      dialogVisible: false,
      isFull: false,
      myValue: this.value,

    }
  },
  created (){
  },
  mounted () {

  },
  methods: {
    codeChange: function(val){
    },
    editorInit: function() {
      require('brace/theme/chrome');
      require('brace/ext/language_tools'); //language extension prerequsite...
      require('brace/mode/yaml');
      require('brace/mode/json');
      require('brace/mode/less');
      require('brace/snippets/json');
      require('brace/mode/lua');
      require('brace/snippets/lua');
      require('brace/mode/javascript');
      require('brace/snippets/javascript');
    },
    open: function(){
        this.isFull = true;
    },
    close: function(){
        this.isFull = false;
    }
  },
  watch: {
      //监听prop传的value，如果父级有变化了，将子组件的myValue也跟着变，达到父变子变的效果
    value(newVal) {
      this.myValue = newVal
    },
    //监听myValue，如果子组件中的内容变化了，通知父级组件，将新的值告诉父级组件，我更新了，父级组件接受到值后页就跟着变了
    //参考官网：https://cn.vuejs.org/v2/guide/components-custom-events.html#%E8%87%AA%E5%AE%9A%E4%B9%89%E7%BB%84%E4%BB%B6%E7%9A%84-v-model
    myValue(newVal) {
      this.$emit('input', newVal)
    }
  }
}
</script>

<style lang="scss" scoped>   //必须要加上“<style lang="scss" scoped>，否则引入失败（提示保存）
  .codeEditBox{
    width:100%;
    height:200px;
    border:1px solid #dcdee2;
  }

  .dialogCodeEdit {
    width:100%;
    height:500px;
  }

</style>
