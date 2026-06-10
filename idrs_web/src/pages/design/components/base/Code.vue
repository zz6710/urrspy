<template>
  <el-dialog :title="title" :visible.sync="codeDialogVisible" :modal='false' width="80%" top="4vh" :before-close="handleClose">
    <div v-bind:style="{width: '100%'}">
      <editor
          v-model="tmpRawCode"
          @init="editorInit"
          @input='codeChange'
          lang="javascript"
          :options="editorOptions"
          theme="chrome"
          height='70vh'
      ></editor>
    </div>
    <span slot="footer">
      <el-tooltip effect="dark" content="拷贝" placement="top">
        <el-button type="success" icon="el-icon-document-copy" circle @click="copyCheck"></el-button>
      </el-tooltip>
      <el-tooltip effect="dark" content="下载" placement="top">
        <el-button type="success" icon="el-icon-download" circle @click="download"></el-button>
      </el-tooltip>
      <el-tooltip effect="dark" content="更新" placement="top" v-if="codeType == 'json'">
        <el-button type="success" icon="el-icon-refresh" circle @click="updatePageList(tmpRawCode)"></el-button>
      </el-tooltip>
    </span>
  </el-dialog>

</template>

<script>
import copy from 'copy-to-clipboard';
import { saveAs } from "file-saver";
import Editor from 'vue2-ace-editor';

export default {
  props: {
    rawCode: {
      type: String
    },
    codeDialogVisible: {
      type: Boolean
    },
    title: {
      type: String,
      default: "代码预览"
    },
    codeType: {
      type: String,
      default: "code"
    },
    updatePageList: {
      type: Function
    }

  },
  components: {
    Editor
  },

  data() {
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

      tmpRawCode: "",
      activeName: "code"
    };
  },
  beforeCreate() { },
  created() {
    this.tmpRawCode = this.rawCode;
  },
  beforeMount() { },
  mounted() { },
  beforeUpdate() { },
  updated() { },
  destoryed() { },
  methods: {
    // 在此自动生成
    request() {
      // 网络请求，可选
    },
    handleClose() {
      this.$emit("update:codeDialogVisible", false);
    },
    copyCheck() {
      this.copy();
    },
    codeChange: function(val){
    },
    editorInit: function() {
      require('brace/theme/chrome');
      require('brace/ext/language_tools'); //language extension prerequsite...
      // require('brace/mode/yaml');
      // require('brace/mode/json');
      // require('brace/mode/less');
      // require('brace/snippets/json');
      // require('brace/mode/lua');
      // require('brace/snippets/lua');
      require('brace/mode/javascript');
      require('brace/snippets/javascript');
      require('brace/mode/html');
      require('brace/snippets/html');
      require('brace/mode/css');
      require('brace/snippets/css');
    },
    copy() {
      if (copy(this.tmpRawCode)) {
        this.$message.success("代码已复制到剪贴板");
      } else {
        this.$message.error("代码复制有点问题?");
      }
    },
    download() {
      let blob = new Blob([this.tmpRawCode], {
        type: "text/plain;charset=utf-8",
      });
      saveAs(blob, "VueComponent.vue");
    },

  },
  watch: {
    codeDialogVisible(newValue) {
      if (newValue) {

      } else {

      }
    },
    rawCode(newVal){
      console.log(" newVal ", newVal);
      this.tmpRawCode = newVal;
    }
  },
  computed: {
  },
  fillter: {},
};
</script>

<style scoped>
/*  在此自动生成 */

::v-deep .el-dialog__body {
  padding: 0 30px !important;
}

.round-icon {
  background: #4dba87;
  width: 40px;
  height: 40px;
  border-radius: 20px;
  padding: 10px;
  margin-left: 10px;
  box-sizing: border-box;
}
</style>
