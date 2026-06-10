<template>
  <div class="design-engine">
    <div class="fm-style">
      <!-- 顶部操作按钮 -->
      <top-operate></top-operate>
      <el-container class="fm2-container">
        <!-- 左侧拖拉组件 -->
        <left-operate></left-operate>
        <!-- 画布区域 -->
        <main-operate ref="mainOperate"></main-operate>
        <!-- 组件属性配置区域 -->
        <right-operate></right-operate>
      </el-container>
    </div>
  </div>
</template>

<script>
  import RightOperate from '@/pages/design/components/base/RightOperate.vue';
  import LeftOperate from '@/pages/design/components/base/LeftOperate.vue';
  import TopOperate from '@/pages/design/components/base/TopOperate.vue';
  import MainOperate from '@/pages/design/components/base/Main.vue';
  import { findConfigById } from "@/pages/design/utils/getForm"

  // 公共方法
  import { getStyle } from '@/pages/design/utils/style'
  import { $ } from '@/pages/design/utils/utils'
  import { parseOptions, findComponent } from '@/pages/design/components/componentsConfig';
  import eventBus from '@/utils/eventBus';


  export default {
    components: {
      LeftOperate,
      TopOperate,
      RightOperate,
      MainOperate
    },
    provide() {
      return {
        kFormDesign: this,
      };
    },
    props: {

    },
    data() {
      return {
        currPage: {},
        widgetFormSelect: {},
        pageList: [],
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
        config: {
          name: '',
        },
        draggableType: "",  // 拖动组件类型
        sysVersion: "",
        moduleId: "",
      }
    },
    created(){
      let configStr = this.$route.query.data;
      let configName = this.$route.query.name;
      this.moduleId = this.$route.query.moduleId
      this.sysVersion = this.$route.query.sysVersion
      if(configName) {
        this.config.name = configName;
      }
      if(this.moduleId) {
        this.config.moduleId = this.moduleId;
      }
      if(configStr && Object.keys(JSON.parse(configStr)).length>0){
        this.config = JSON.parse(configStr);
        this.pageList = JSON.parse(this.config.json);

        // 自定义组件
        this.pageList[0].list.forEach(item => {
          if(item.type == 'k-customer') {
            item.options.formId && findConfigById(item.options.formId).then((res) => {
              this.parmaFunc({
                data: res,
                element: item
              })
            })
          }
        })

      } else {
        this.initPageList();
      }
      this.currPage = this.pageList[0];
    },
    mounted(){
      // 滚动条处理
      const body = window.document.getElementsByTagName('body')[0];
      body.style.overflow = 'hidden';
    },
    methods: {
      // 公共方法
      getStyle: getStyle,
      $: $,
      parseOptions: parseOptions,
      findComponent: findComponent,

      parmaFunc(val) {
        // k-customer function
        const { data, element } = val;
        let cunstomNum = -1;
        let jsonData = data.json && JSON.parse(data.json)

        try {
          // 异常处理 （处理 k-customer 嵌套问题）
          if(jsonData[0].list.length > 0) {
            jsonData[0].list.forEach(item => {
              if(item.type == 'k-customer') {
                setTimeout(() => {Tools.alert('渲染失败，暂不支持自定义控件嵌套 ', "danger");}, 500)
                throw new Error('渲染失败，暂不支持自定义控件嵌套 ')
              }
            })
          }

          // 正常获取数据
          this.pageList[0].list.forEach(item => {
            if(item.type == 'k-customer') cunstomNum ++     // 处理多个k-customer
            if(item.key == element.key) {
              item.list = jsonData
              // this.$refs.mainOperate.$refs.widgetForm.$refs.KCustomer[cunstomNum].setGenerateCode()
              eventBus.$emit('kCustomerGetData')
            }
          })

        } catch (error) {
        }
      },

      initPageList: function(){
        this.pageList = [
          {
            title: '主页',
            name: 'main',
            key: 'main',
            options: {...this.pageConfig, dataTitle: '主页'},
            type: "home",
            list: [],
            cycles: [],
            css: "",
            data: "",
            customClass: 'component_main'
          }
        ]
      },
      /**
       * 选中组件
       * @param {*} key 想要查找的目标
       * @param {*} tree 树形数据
       */
      selectComponentByKey: function(key, tree=this.pageList){
        let _component = this.findComponentByKey(key, tree);
        if(_component){
          this.widgetFormSelect = _component;
        } else {
          console.log(" 未找到组件, key: ", key);
        }
      },
      /**
       * 深度遍历查找
       * @param {*} key 想要查找的目标
       * @param {*} tree 树形数据
       */
      findComponentByKey: function (key, tree=this.pageList) {
        // 模拟栈，管理结点
        let stack = [...tree]
        while (stack && stack.length>0) {
          // 栈顶节点出栈
          let node = stack.pop()

          // 查找到目标，退出
          if (node.key == key) {
            // this.widgetFormSelect = node;
            // console.log(" [DFS查找] 匹配到KEY ", node.key, node, this.widgetFormSelect);
            return node;
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

      /**
       * 根据key删除组件
       * @param {*} key
       * @param {*} data
       */
      deleteComponentByKey: function (key, data=this.pageList){
        data.forEach((node, index) => {
          if(node.key == key){
            this.$nextTick(() => {
              data.splice(index, 1);
              console.log(" 匹配到KEY并删除 ", node, key);
            })
          }

          // 子节点
          if(node.list && node.list.length>0){
            this.deleteComponentByKey(key, node.list);
          }
          // 按钮组
          if(node.btns && node.btns.length>0){
            this.deleteComponentByKey(key, node.btns);
          }
          if(node.headerBtns && node.headerBtns.length>0){
            this.deleteComponentByKey(key, node.headerBtns);
          }
          // el-row布局子节点
          if (node.type == 'el-row') {
            node.columns.forEach(c => {
              if(c.list && c.list.length > 0){
                this.deleteComponentByKey(key, c.list);
              }
            });
          }
        });
      },
      /**
       * 根据key复制组件
       * @param {*} key
       * @param {*} data
       */
      cloneComponentByKey: function(key, data=this.pageList){
        console.log(" 复制组件KEY: ", key);
      },

    }
  }
</script>

<style lang="scss" scoped>   //必须要加上“<style lang="scss" scoped>，否则引入失败（提示保存）
  @import './styles/index.scss';

  .design-engine {
    background-color: #FFFFFF;
  }

</style>
