<template>
  <div>

      <div class="fm-style">
        <el-container class="fm2-container">
          <el-main class="fm2-main">
            <el-container>
              <!-- 左侧拖拉组件 -->
              <el-aside width="200px">
                <div class="components-list">
                  <!-- 基础组件 -->
                  <template>
                    <div class="widget-cate">基础组件</div>
                    <draggable tag="ul" :list="basicComponents"
                      v-bind="{group:{ name:'people', pull:'clone',put:false},sort:false, ghostClass: 'ghost'}"
                      @end="handleMoveEnd"
                      @start="handleMoveStart"
                      @clone="clone"
                      :move="handleMove"
                    >
                      <li class="form-edit-widget-label" :class="{'no-put': item.type == 'divider'}" v-for="(item, index) in basicComponents" :key="index" :data-type="item.type">
                        <a>
                          <i class="icon iconfont" :class="item.icon"></i>
                          <span>{{item.name}}</span>
                        </a>
                      </li>
                    </draggable>
                  </template>

                  <!-- 高级组件 -->
                  <template v-if="advanceComponents && advanceComponents.length > 0">
                    <div class="widget-cate">高级组件</div>
                    <draggable tag="ul" :list="advanceComponents"
                      v-bind="{group:{ name:'people', pull:'clone',put:false},sort:false, ghostClass: 'ghost'}"
                      @end="handleMoveEnd"
                      @start="handleMoveStart"
                      @clone="clone"
                      :move="handleMove"
                    >
                      <li class="form-edit-widget-label" :class="{'no-put': item.type == 'table'}" v-for="(item, index) in advanceComponents" :key="index" :data-type="item.type">
                        <a>
                          <i class="icon iconfont" :class="item.icon"></i>
                          <span>{{item.name}}</span>
                        </a>
                      </li>
                    </draggable>
                  </template>

                  <!-- 布局组件 -->
                  <template>
                    <div class="widget-cate">布局组件</div>
                    <draggable tag="ul" :list="layoutComponents"
                      v-bind="{group:{ name:'people', pull:'clone',put:false},sort:false, ghostClass: 'ghost'}"
                      @end="handleMoveEnd"
                      @start="handleMoveStart"
                      @clone="clone"
                      :move="handleMove"
                    >
                      <li class="form-edit-widget-label no-put" v-for="(item, index) in layoutComponents" :key="index" :data-type="item.type">
                        <a>
                          <i class="icon iconfont" :class="item.icon"></i>
                          <span>{{item.name}}</span>
                        </a>
                      </li>
                    </draggable>
                  </template>
                </div>
              </el-aside>

              <!-- 主区域 -->
              <el-container class="center-container" direction="vertical">
                <!-- 操作按钮栏 -->
                <el-header class="btn-bar" style="height: 45px;">
                  <slot name="action">
                  </slot>
                  <el-button type="text" size="medium" icon="el-icon-close" @click="close">关闭</el-button>
                  <template v-if="!config.history">
                    <el-button type="text" size="medium" icon="el-icon-check" @click="save">保存</el-button>
                    <el-button type="text" size="medium" icon="el-icon-refresh-right" @click="clear">清空</el-button>
                  </template>
                  <el-button type="text" size="medium" icon="el-icon-view" @click="preview">预览</el-button>
                  <el-button type="text" size="medium" icon="el-icon-document" @click="genCode">生成代码</el-button>
                  <el-button type="text" size="medium" icon="el-icon-s-order" @click="genJson">JSON配置</el-button>
                </el-header>

                <!-- 画布区域 -->
                <el-main>
                  <el-tabs v-model="activeTabsValue" type="card" addable  @tab-click="tabClick" @tab-remove="tabRemove" @tab-add="tabAdd" >
                    <el-tab-pane
                      :key="item.name"
                      v-for="(item) in pageList"
                      :label="item.options.dataTitle"
                      :name="item.name"
                      :closable="item.name == 'main' ? false: true"
                    >
                      <custom-canvas ref="widgetForm" :data="item" :select.sync="widgetFormSelect"></custom-canvas>
                    </el-tab-pane>
                  </el-tabs>
                </el-main>
              </el-container>

              <!-- 组件属性配置区域 -->
              <el-aside class="widget-config-container">
                <el-tabs :stretch="true" v-model="activeName">
                  <el-tab-pane label="配置属性" name="config">
                    <config-param :element="config"></config-param>
                  </el-tab-pane>
                  <el-tab-pane label="页面属性" name="global">
                    <page-param :element="currPage"></page-param>
                  </el-tab-pane>
                  <el-tab-pane label="组件属性" name="widget">
                    <component v-if="widgetFormSelect.paramType" :is="widgetFormSelect.paramType" :element="widgetFormSelect"></component>z
                  </el-tab-pane>
                </el-tabs>
              </el-aside>
            </el-container>
          </el-main>
        </el-container>
      </div>

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

      <Code :rawCode="code" :config="tmpConfig" :codeDialogVisible.sync="codeDialogVisible" :codeType="codeType" :updatePageList="updatePageList">
      </Code>

  </div>
</template>

<script>
  import Draggable from 'vuedraggable'
  import {basicComponents, layoutComponents, advanceComponents} from './components/componentsConfig.js'
  import CustomCanvas from './components/CustomCanvas.vue'
  import KFieldTextParam from './components/param/KFieldTextParam';
  import KFieldBswitchParam from './components/param/KFieldBswitchParam';
  import KFieldRadioParam from './components/param/KFieldRadioParam';
  import KFieldCheckboxParam from './components/param/KFieldCheckboxParam';
  import KFieldTimeParam from './components/param/KFieldTimeParam';
  import KFieldDateParam from './components/param/KFieldDateParam';
  import KFieldSelectParam from './components/param/KFieldSelectParam';
  import KFieldCascaderParam from './components/param/KFieldCascaderParam';
  import KFieldRichParam from './components/param/KFieldRichParam';
  import KFieldUploadParam from './components/param/KFieldUploadParam';
  import KStepsParam from './components/param/KStepsParam';
  import KFieldTreeParam from './components/param/KFieldTreeParam';
  import ElRowParam from './components/param/ElRowParam';
  import KFormParam from './components/param/KFormParam';
  import KFormSearchParam from './components/param/KFormSearchParam';
  import KFormSearchCustomizeParam from './components/param/KFormSearchCustomizeParam';
  import KGridParam from './components/param/KGridParam';
  import KBtnParam from './components/param/KBtnParam';
  import PageParam from './components/param/PageParam';
  import ConfigParam from './components/param/ConfigParam';

  // test
  import preview from './preview'
  import display from './display/index'
  import CodePreview from './code-viewer/src/code-preview.vue'
  import generateCode from './utils/generateCode.js';
  import Code from './components/base/Code.vue';


  export default {
    components: {
      Draggable,
      CustomCanvas,
      KFieldTextParam,
      KFieldBswitchParam,
      KFieldRadioParam,
      KFieldCheckboxParam,
      KFieldTimeParam,
      KFieldDateParam,
      KFieldSelectParam,
      KFieldCascaderParam,
      KFieldRichParam,
      KFieldUploadParam,
      KStepsParam,
      KFieldTreeParam,
      ElRowParam,
      KFormParam,
      KFormSearchParam,
      KFormSearchCustomizeParam,
      KGridParam,
      KBtnParam,
      PageParam,
      preview,
      display,
      CodePreview,
      ConfigParam,
      Code
    },
    provide() {
      return {
        kFormDesign: this,
      };
    },
    props: {
      close: {
        type: Function,
        default: () => {}
      },
      config: {
        type: Object,
        default: () => {return {}}
      }
    },
    data() {
      return {
        activeTabsValue: 'main',  // 选中tab
        currPage: {},
        pageList: [
          {
            title: '主页',
            name: 'main',
            options: {...this.pageConfig, dataTitle: '主页'},
            type: "home",
            list: [],
            cycles: [],
            css: "",
            data: "",
          }
        ],
        widgetFormSelect: {},

        compoentOptions: "",
        basicComponents,
        layoutComponents,
        advanceComponents,
        dialogVisible: false,
        configTab: "widget",
        activeName: "config",
        draggableType: "",  // 拖动组件类型
        pageConfig: {
          dataTitle: "新页面",
          dataWidthPercent: "",
          dataWidth: "",
          dataFullscreen: null,
          dataDirection: "",
          dataConfirmDescribe: "",
          dataConfirmClose: null,
          dataMask: null,
          dataType: "dialog",
          dataTitleAlign: "",
          dataCloseOnClickModal: null
        },

        displayVisible: false,
        code: "",
        tmpConfig: "",
        codeType: "code",
        codeDialogVisible: false,
      }
    },
    created(){
      if(this.config && Object.keys(this.config).length>0){
        this.pageList = JSON.parse(this.config.json);
      } else {
      }
      this.currPage = this.pageList[0];
    },
    methods: {
      tabClick: function(tab){
        console.log(tab);
      },
      tabRemove: function(targetName){
        console.log(" tabRemove ", targetName);
        this.$confirm('删除后不可恢复, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let tabs = this.pageList;
          let activeName = this.activeTabsValue;
          if (activeName === targetName) {
            tabs.forEach((tab, index) => {
              if (tab.name === targetName) {
                let nextTab = tabs[index + 1] || tabs[index - 1];
                if (nextTab) {
                  activeName = nextTab.name;
                }
              }
            });
            this.activeTabsValue = activeName;
          }

          this.activeTabsValue = activeName;
          this.pageList = tabs.filter(tab => tab.name !== targetName);
        }).catch(() => {

        });
      },
      tabAdd: function(){
        let name = Date.parse(new Date()) + '_' + Math.ceil(Math.random() * 99999);
        this.pageList.push({
          title: '新页面',
          name: name,
          options: {
            ...this.pageConfig
          },
          list: [],
          events: [],
          type: "k-popup",
          data: "",
          css: "",
          customAttrs: [],
          events: []
        });
        this.activeTabsValue = name;
      },


      handleMoveEnd: function(e){
      },
      handleMoveStart: function(e){
        this.draggableType = e.clone.dataset.type;
      },
      handleMove: function(e){
      },
      handleConfigSelect (value) {
        this.configTab = value
      },
      save: function(){
        // 数据初始化
        this.config.json = JSON.stringify(this.pageList);


        // 校验
        if(!this.config.name){
          this.$message.error("请输入功能名称");
          return
        }


        let action = "LowcodeConfig.addConfig";
        if(this.config.id){   // 修改
          action = "LowcodeConfig.updateConfig";
        }

        this.httpUtil.comnUpdate({
          action: action,
          params: this.config,
          mask: true,
        }).then(data => {
          console.log("  data  ", data);
          if(data.success){
            this.close(false);
          }
        });
      },
      // 预览
      preview: function(){
        this.code = generateCode(this.pageList);
        this.displayVisible = true;
      },
      // 清空
      clear: function(){
        this.pageList = [
          {
            title: '主页',
            name: 'main',
            options: {...this.pageConfig, dataTitle: '主页'},
            type: "home",
            list: [],
            cycles: [],
            css: "",
            data: "",
          }
        ],
        this.currPage = this.pageList[0];
      },
      // 生成代码
      genCode: function(){
        console.log("### 生成代码 ###", this.pageList);
        // console.log(generateCode(this.pageList));
        this.code = generateCode(this.pageList);
        this.codeType = "code";
        this.codeDialogVisible = true;
      },
      genJson: function(){
        const vueBeautify = require('vue-beautify');
        console.log("### 查看代码 ###", this.pageList);
        this.code = vueBeautify("<script>" + JSON.stringify(this.pageList) + "<\/script>");
        this.code = this.code.replace("<script>","").replace("<\/script>","");
        this.codeType = "json";
        this.codeDialogVisible = true;
      },
      updatePageList: function(rawCode){
        console.log(" update rawCode ", rawCode);
        this.pageList = JSON.parse(rawCode);
        this.currPage = this.pageList[0];

        this.$message.success("配置更新成功! ");
      },

      clone: function(origin){
        // console.log(" origin ", origin);
        //这一步最关键，没处理好，后面会数据混乱
        //通过转成字符串，让他变成一个新对象，不然拖拽第二个组件将会和第一个组件一模一样，改变第一个组件第二个、第三个也会跟着变动。
        // const data = parse(stringify(origin))
        // data.id = parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16);
        return origin;
      },
      /**
       * 深度遍历查找
       * @param {*} tree 树形数据
       * @param {*} key 想要查找的目标
       */
      findComponentByKey: function (key, tree=this.pageList) {
        // 模拟栈，管理结点
        let stack = [...tree]
        while (stack && stack.length>0) {
          // 栈顶节点出栈
          let node = stack.pop()

          // 查找到目标，退出
          if (node.key == key) {
            this.widgetFormSelect = node;
            // console.log(" [DFS查找] 匹配到KEY ", node, node.key, this.widgetFormSelect);
            return;
          }
          // 子节点
          if(node.list && node.list.length>0){
            stack.push(...node.list);
          }
          // 按钮组
          if(node.btns && node.btns.length>0){
            stack.push(...node.btns);
          }
          if(node.headerBtns && node.headerBtns.length>0){
            stack.push(...node.headerBtns);
          }
          // el-row布局子节点
          if (node.type == 'el-row') {
            node.columns.forEach(c => {
              if(c.list && c.list.length > 0){
                stack.push(...c.list);
              }
            });
          }
        }
      },
      // 根据key删除组件
      deleteComponentByKey: function(key, data=this.pageList){
        data.forEach((v, index) => {
          if(v.key == key){
            this.$nextTick(() => {
              data.splice(index, 1);
              console.log(" 匹配到KEY并删除 ", v, v.key, key);
            })
            return false;
          } else if (v.type == 'home' || v.type == 'k-popup') {
              this.deleteComponentByKey(key, v.list);
          } else if (v.type == 'k-form' || v.type == 'k-form-search-customize') {
            this.deleteComponentByKey(key, v.list);
            this.deleteComponentByKey(key, v.btns);
          } else if (v.type == 'k-grid') {
            this.deleteComponentByKey(key, v.list);
            this.deleteComponentByKey(key, v.btns);
          } else if (v.type == 'el-row') {
            v.columns.forEach(c => {
              if(c.list.length > 0){
                this.deleteComponentByKey(key, c.list);
              }
            });
          }
        });
      },
      tmpUpdateOption: function(){
        let v = JSON.parse(this.compoentOptions);
        this.updateCompoentOptions(v.key);
      },
      updateCompoentOptions: function(key, data=this.pageList) {
        data.forEach((v, index) => {
          if(v.key == key){
            this.$nextTick(() => {
              this.$set(data, index, JSON.parse(this.compoentOptions))
            })
            return false;
          } else if (v.type == 'home' || v.type == 'k-popup') {
            this.updateCompoentOptions(key, v.list);
          } else if ((v.type == 'k-form' || v.type == 'k-form-search-customize') && v.list.length > 0) {
            this.updateCompoentOptions(key, v.list);
          } else if (v.type == 'el-row') {
            v.columns.forEach(c => {
              if(c.list.length > 0){
                this.updateCompoentOptions(key, c.list);
              }
            });
          }
        });
      },
    },
    watch: {
      widgetFormSelect: {
        handler(val){
          console.log(" widgetFormSelect ", val);
          this.compoentOptions = JSON.stringify(val);
        },
        deep: true
      },
      activeTabsValue: {
        handler(val){
          console.log("  currPage  ", val );
          let pageList = this.pageList.filter(tab => tab.name == val);
          if(pageList && pageList.length>0){
            this.currPage = pageList[0];
          }
        }
      }
    }
  }
</script>

<style lang="scss" scoped>   //必须要加上“<style lang="scss" scoped>，否则引入失败（提示保存）
  // @import './styles/cover.scss';
  @import './styles/index.scss';

  /deep/ .el-tabs__item {
    height: 45px;
  }

  /deep/ .kk-mask {
    display: none;
  }

</style>
