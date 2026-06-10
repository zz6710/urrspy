<template>
  <el-main class="main-operate">
    <el-tabs v-model="activeTabsValue" type="card" addable  @tab-click="tabClick" @tab-remove="tabRemove" @tab-add="tabAdd" >
      <el-tab-pane
        class="main-el-tab-pane"
        :key="item.name"
        v-for="(item) in kFormDesign.pageList"
        :label="item.options.dataTitle"
        :name="item.name"
        :closable="item.name == 'main' ? false: true"
      >
        <!-- 刻度 -->
        <!-- <div class="rulersWrapper">
          <ul class="rulers">
            <li>
              <span class="ruler-value">1000</span>
            </li>
            <li>
              <span class="ruler-value">1000</span>
            </li>
            <li>
              <span class="ruler-value">1000</span>
            </li>
          </ul>
        </div> -->
        <custom-canvas ref="widgetForm" :element="item"></custom-canvas>
      </el-tab-pane>
    </el-tabs>
  </el-main>
</template>

<script>
import CustomCanvas from '@/pages/design/components/CustomCanvas.vue'

export default {
  components: {
    CustomCanvas
  },
  props: {
  },
  inject: ['kFormDesign'],
  data () {
    return {
      activeTabsValue: 'main',  // 选中tab
    }
  },
  created() {
  },
  mounted () {
  },
  methods: {
    tabClick: function(tab){
      // console.log(tab);
    },
    tabRemove: function(targetName){
      this.$confirm('删除后不可恢复, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let tabs = this.kFormDesign.pageList;
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
        this.kFormDesign.pageList = tabs.filter(tab => tab.name !== targetName);
      }).catch(() => {

      });
    },
    tabAdd: function(){
      let name = Date.parse(new Date()) + '_' + Math.ceil(Math.random() * 99999);
      let _class = 'component_' + name;
      this.kFormDesign.pageList.push({
        title: '新页面',
        name: name,
        key: name,
        options: {
          ...this.kFormDesign.pageConfig
        },
        list: [],
        events: [],
        type: "k-popup",
        data: "",
        css: "",
        customAttrs: [],
        events: [],
        customClass: _class
      });
      this.activeTabsValue = name;
    },
  },
  watch: {
    activeTabsValue: {
      handler(val){
        let pageList = this.kFormDesign.pageList.filter(tab => tab.name == val);
        let pageIndex = this.kFormDesign.pageList.findIndex(tab => tab.name == val);
        if(pageList && pageList.length>0){
          this.kFormDesign.currPage = pageList[0];
        }
        this.$store.commit('setTabIndex', pageIndex);     // 页面index存储
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/pages/design/styles/variable.scss';

.main-operate{
  // border-left: 1px solid $dark-border;
  // border-right: 1px solid $dark-border;

  padding: 0;
  position: relative;
  background: #fafafa;

  .main-el-tab-pane {
    display: flex;
    flex-direction: column;
    height: calc(100vh - 106px);
    // background-color: $dark-body-bg;
  }
}
li, ul {
  list-style-type: none;
}
.rulersWrapper {
  position: absolute;
    left: 0;
    top: -12px;
    overflow: hidden;
  .rulers {
    width: 1920px;
    display: flex;
    flex-direction: row;
    flex-wrap: nowrap;
    top: 0;
    right: 0;
    left: 1px;
    background-size: 5px 4px!important;
    background: linear-gradient(to right,transparent 4px,rgba(255,255,255,.5) 1px) repeat-x;
    background-color: $dark-body-bg;
    height: 23px;

    li {
      position: relative;
      flex-basis: 49px;
      margin-left: 49px;
      flex-grow: 0;
      flex-shrink: 0;
      margin: 4px 0 0 49px;
      border-right: 1px solid transparent;
      border-right-color: rgba(255,255,255,.5);
      &::before {
        content: '';
        position: absolute;
        width: 1px;
        height: 6px;
        top: 0;
        left: -1px;
        box-sizing: border-box;
        background: rgba(255,255,255,.5);
      }
      .ruler-value {
        position: absolute;
        font-size: 12px;
        width: 40px;
        bottom: -4px;
        right: -46px;
        line-height: 10px;
        color: rgba(255,255,255,.5);
      }
    }
  }
}
</style>
