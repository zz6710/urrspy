<template>

  <div class="app-wrap">
    <div class="kk-top">
      <top-navbar class="top-navbar" :subMenus="subMenus"></top-navbar>
      <!-- 此处放置el-tabs代码 -->
      <div class="template-tabs">
        <div class="menu" id="menu1">
          <el-tabs
            id="menu2"
            :style="ulStyle"
            v-model="editableTabsValue"
            type="card"
            closable
            @tab-click="tabClick"
            @tab-remove="removeTab">
            <el-tab-pane
              v-for="(item, index) in editableTabs"
              :key="item.name"
              :label="item.title"
              :name="item.name"
            >
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
    </div>
    <div class="tab-content">
        <keep-alive :include="includeComponent">
          <router-view>

          </router-view>
        </keep-alive>
<!--      <keep-alive>-->
<!--        <router-view v-if="$route.meta.keepAlive&&$route.meta.needAlive">-->
<!--          &lt;!&ndash;这里是会被缓存的路由&ndash;&gt;-->
<!--        </router-view>-->
<!--      </keep-alive>-->
<!--      <router-view v-if="!$route.meta.keepAlive||!$route.meta.needAlive"/>-->
    </div>
  </div>
</template>

<script>
  import TopNavbar from "../main/TopNavbar"

  export default {
    name: "TabComponents",
    components: {
      TopNavbar
    },
    props: {
      subMenus: {
        type: Array,
        default: []
      }
    },
    data() {
      return {
        includeComponent: [],
        leftPx:"",
      }
    },
    methods: {
      setAlive(item){
          if(item.meta.keepAlive&&item.meta.a==true){
            return true
          }else{
            return false
          }
      },
      validateMove(){
        if(document.getElementById("menu1").clientWidth==document.getElementById("menu2").clientWidth){
          return 0
        }else{
          return document.getElementById("menu1").clientWidth-document.getElementById("menu2").clientWidth
        }
      },
      clickMenuRight(num){
        let diffWidth=this.validateMove()
        if(diffWidth==0){
          return
        }else{
          if(this.leftPx<diffWidth){
            return;
          }else{
            let b=this.leftPx-diffWidth
            if(b<num){
              this.leftPx=this.leftPx-b
            }else{
              this.leftPx=this.leftPx-num
            }
          }
        }
      },
      clickMenuLeft(num){
        let diffWidth=this.validateMove()
        if(diffWidth==0){
          return
        }else{
          let b=0-this.leftPx
          if((b-num)<0){
            this.leftPx=this.leftPx+b
          }else{
            this.leftPx=this.leftPx+num
          }
        }
      },
      //点击切换tab
      tabClick(tab) {
        let path = tab.name;
        this.$store.commit('system/setEditableTabsValue', path);
        this.$router.push({path: path});
      },
      //点击移除tab
      removeTab(targetName) {
        this.includeComponent = ["user"]
        if (targetName === "/main/desktop") {
          return;
        }
        let tabs;
        tabs = typeof this.editableTabs === 'string' ? JSON.parse(this.editableTabs) : this.editableTabs;

        let activeName = this.editableTabsValue;
        if (activeName === targetName) {
          // 设置当前激活的路由
          tabs.forEach((tab, index) => {
            if (tab.name === targetName) {
              let nextTab = tabs[index + 1];
              let preTab = tabs[index - 1];

              if (nextTab) {
                activeName = nextTab.name;
              } else if (preTab) {
                activeName = preTab.name;
              } else {
                activeName = "/main/desktop";
              }
            }
          });
        }

        let tab1;
        tab1 = tabs.filter((tab)=>{
          if(tab.name !== targetName){
            return true
          }else{
            if(tab.componentName){
               this.includeComponent=this.includeComponent.filter(item=>item!==tab.componentName)
            }
            return false
          }
        });

        this.$store.commit('system/setEditableTabs', tab1);
        this.$store.commit('system/setEditableTabsValue', activeName);

        this.$router.push({path: activeName});
      }
    },
    computed: {
      ulStyle(){
        let a={

        }
        a.marginLeft=this.leftPx+"px"
        return a
      },
      //存放所有tab的数组
      editableTabs() {
        let tabs;
        let data = this.$store.state.system.editableTabs;
        tabs = typeof data === 'string' ? JSON.parse(data) : data;
        return tabs;
      },
      //当前tab 初始默认为首页(/home)
      editableTabsValue: {
        get() {
          return this.$store.state.system.editableTabsValue;
        },
        set: function () {
        }
      }
    },
    watch: {
      //监听路由的变化来实现添加或切换tab
      '$route': function (to,oldTo) {
        if(to.meta.needAlive){
          this.includeComponent.push(to.meta.componentName)
        }
        let flag = false;
        let tabs = this.editableTabs;
        let route = this.editableTabsValue;
        for (let i = 0; i < tabs.length; i++) {
          if (tabs[i].name === to.path) {
            flag = true;
            //设置当前tab为当前路由
            this.$store.commit('system/setEditableTabsValue', to.path);
            break;
          }
        }


        if (!flag) {
          let data = {
            title: to.name,
            name: to.path,
            componentName:to.meta.componentName
          };
          tabs.push(data);
          route = to.path;
          //设置tab数组
          this.$store.commit('system/setEditableTabs', tabs);
          this.$store.commit('system/setEditableTabsValue', route);
        }
      }
    },
  }
</script>

<style scoped lang="scss">
  .kk-top {
    width: calc(100% - 260px);
    position: fixed;
    right: 0;
    top: 0;
    z-index: 3000;
  }

  @media (min-width: 0px) and (max-width: 2000px) {
    .sidebar-mini .main-panel {
      .kk-top {
        width: calc(100% - 80px);
      }
    }
  }

  ::v-deep .el-tabs {
    height: 40px;

    .el-tabs__header {
      margin: 0px;
    }
  }

  ::v-deep.template-tabs {
    position: relative;
    z-index: -1;
    background-color: white;
    display: flex;
    width: 100%;
    .menu-left{
      width: 25px;
      display: flex;
      align-items: center;
      cursor: pointer;
    }
    .menu-right{
      width: 25px;
      display: flex;
      align-items: center;
      cursor: pointer;
    }
    .menu{
      width: 50%;
      overflow: hidden;
      .el-tabs{
        overflow: hidden;
        .el-tabs__nav-prev{
        }
        .el-tabs__nav-next{
        }
      }
    }
  }

  .tab-content {
    margin-top: 120px;
    background-color: #EEEEEE;
  }
</style>
