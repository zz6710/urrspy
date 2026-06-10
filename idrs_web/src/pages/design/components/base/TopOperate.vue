<template>
  <div>
    <header class="header">
      <div class="logo">
       <img src="../../../../assets/img/img-logo.png" alt="">
        <span>Low Code Engine</span>
      </div>
      <!-- <div>
        预留
      </div> -->
      <div class="operate">
        <el-tooltip effect="dark" content="关闭" placement="bottom">
          <i class="el-icon-close operate_icon" @click="close"></i>
        </el-tooltip>
        <div class="operate_line"></div>
        <el-tooltip effect="dark" content="保存" placement="bottom">
          <i class="el-icon-check operate_icon" @click="save"></i>
        </el-tooltip>
        <el-tooltip effect="dark" content="清空" placement="bottom">
          <i class="el-icon-refresh-right operate_icon" @click="clear"></i>
        </el-tooltip>
        <div class="operate_line"></div>
        <el-tooltip effect="dark" content="预览" placement="bottom">
          <i class="el-icon-view operate_icon" @click="preview"></i>
        </el-tooltip>
        <el-tooltip effect="dark" content="生成代码" placement="bottom">
          <i class="el-icon-document operate_icon" @click="genCode"></i>
        </el-tooltip>
        <el-tooltip effect="dark" content="JSON配置" placement="bottom">
          <i class="el-icon-s-order operate_icon" @click="genJson"></i>
        </el-tooltip>
        <!-- <el-button type="text" size="medium" icon="el-icon-close" @click="close">关闭</el-button> -->
          <!-- <el-button type="text" size="medium" icon="el-icon-check" @click="save">保存</el-button> -->
          <!-- <el-button type="text" size="medium" icon="el-icon-refresh-right" @click="clear">清空</el-button>
        <el-button type="text" size="medium" icon="el-icon-view" @click="preview">预览</el-button>
        <el-button type="text" size="medium" icon="el-icon-document" @click="genCode">生成代码</el-button>
        <el-button type="text" size="medium" icon="el-icon-s-order" @click="genJson">JSON配置</el-button> -->
      </div>
    </header>

    <!-- 预览 -->
    <el-dialog
      title="预览"
      :visible.sync="displayVisible"
      :fullscreen="true"
      :show-close="true"
      :modal='false'
    >
      <CodePreview :source="code"></CodePreview>
    </el-dialog>

    <Code :rawCode="code" :codeDialogVisible.sync="codeDialogVisible" :codeType="codeType" :updatePageList="updatePageList">
    </Code>
  </div>
</template>

<script>
import generateCode from '../../utils/generateCode.js';
import CodePreview from '../../code-viewer/src/code-preview.vue'
import Code from './Code.vue';

export default {
  components: {
    CodePreview, Code
  },
  props: {
  },
  inject: ['kFormDesign'],
  data () {
    return {
      displayVisible: false,
      codeDialogVisible: false,

      codeType: "code",
      code: "",
    }
  },
  created() {
  },
  mounted () {
  },
  methods: {
    close(){
      this.$confirm('确定关闭吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 刷新表格
        localStorage.setItem('lowcodeConfig_refresh', Math.ceil(Math.random() * 99999))
        // 关闭浏览器窗口
        window.opener = null;
        window.open('', '_self');
        window.close();
      }).catch(() => {
      });
    },
    save: function(){
      // 数据初始化
      this.kFormDesign.config.json = JSON.stringify(this.kFormDesign.pageList);
      // 校验
      if(!this.kFormDesign.config.name){
        this.$message.error("请输入功能名称");
        return
      }
      let action = "LowCodeConfig.add";
      if(this.kFormDesign.config.id){   // 修改
        action = "LowCodeConfig.update";
      }

      this.httpUtil.comnUpdate({
        action: action,
        params: this.kFormDesign.config,
        mask: true,
      }).then(data => {
        console.log("  data  ", data);
        if(data.success){
          // 刷新表格
          localStorage.setItem('lowcodeConfig_refresh', Math.ceil(Math.random() * 99999))
          // 关闭浏览器窗口
          // window.opener = null;
          // window.open('', '_self');
          // window.close();
        }
      });
    },
    // 预览
    preview: function(){
      this.code = generateCode(this.kFormDesign.pageList);
      // console.log(this.kFormDesign.pageList);
      // console.log(this.code);
      this.displayVisible = true;
    },
    // 清空
    clear: function(){
      this.kFormDesign.initPageList();
      this.kFormDesign.currPage = this.kFormDesign.pageList[0];
      this.kFormDesign.widgetFormSelect = {};
    },
    // 生成代码
    genCode: function(){
      console.log("### 生成代码 ###", this.kFormDesign.pageList);
      // console.log(generateCode(this.pageList));
      this.code = generateCode(this.kFormDesign.pageList);
      this.codeType = "code";
      this.codeDialogVisible = true;
    },
    genJson: function(){
      const vueBeautify = require('vue-beautify');
      console.log("### 查看代码 ###", this.kFormDesign.pageList);
      this.code = vueBeautify("<script>" + JSON.stringify(this.kFormDesign.pageList) + "<\/script>");
      this.code = this.code.replace("<script>","").replace("<\/script>","");
      this.codeType = "json";
      this.codeDialogVisible = true;
    },
    updatePageList: function(rawCode){
      this.kFormDesign.pageList = JSON.parse(rawCode);
      this.kFormDesign.currPage = this.kFormDesign.pageList[0];
      this.$message.success("配置更新成功! ");
    },
    // closeWindow: function() {
    //   var userAgent = navigator.userAgent
    //   if (userAgent.indexOf('Firefox') !== -1 || userAgent.indexOf('Chrome') !== -1) {
    //     window.location.replace('about:blank')
    //   } else {
    //     window.opener = null
    //     window.open('', '_self')
    //   }
    //   window.close()
    // },
    // closeWebPage: function() {
    //   if (navigator.userAgent.indexOf("MSIE") > 0) { // IE
    //       if (navigator.userAgent.indexOf("MSIE 6.0") > 0) {// IE6
    //           window.opener = null;
    //           window.close();
    //       } else {// IE6+
    //           window.open('', '_top');
    //           window.top.close();
    //       }
    //   } else if (navigator.userAgent.indexOf("Firefox") > 0 || navigator.userAgent.indexOf("Presto") > 0) {// FF和Opera
    //       window.location.href = 'about:blank';
    //       window.close();// 火狐默认状态非window.open的页面window.close是无效的
    //   } else {
    //       window.opener = null;
    //       window.open('', '_self', '');
    //       window.close();
    //   }
    // }

  },
  watch: {

  }
}
</script>

<style lang="scss" scoped>
@import '../../styles/variable.scss';

.header {
  height: 50px;
  display: flex;
  padding: 5px;
  flex-direction: row;
  justify-content: space-between;
  background-color: $dark-header-bg;

  .logo {
    display: flex;
    justify-content: center;
    align-items: center;
    img {
      height: 100%;
      margin-right: 10px;
    }
    span {
      font-size: 20px;
      font-weight: 500;
      color: #fff;
    }
  }
  .operate {
    display: flex;
    align-items: center;
    margin-right: 20px;
    &_icon {
      font-size: 18px;
      color: $dark-color;
      font-weight: 600;
      padding: 8px;
      transition: .2s color;
      cursor: pointer;
      &:hover {
        color: #fff;
      }
    }
    &_line {
      width: 1px;
      height: 32px;
      line-height: 50px;
      background-color: $dark-border;
      margin: 0 5px;
    }
  }

}



</style>
