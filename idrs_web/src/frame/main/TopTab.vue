<template>
  <div class="top-tab" id="top-tab" :data-color="activeColor">
    <div class="tab" id="tab">
      <div class="item" :class="{'item-selected':item.active}" v-for="(item,index) in tab"
           @click="clickTab(item)" :title="item.query.menuName" :key="index">
        <span class="item-name"> {{item.query.menuName || item.name }}</span>
        <div class="close" v-if="index!=0" @click.stop="removeTab(item,index)">×</div>
      </div>
    </div>
    <div class="down" id="down">
      <img src="../../assets/svg/k-menu/down2.svg"  :class="{'down-active':showTab2}"  @click="showTab2=!showTab2"/>
      <ul class="more" v-show="showTab2">
        <li v-for="(item,index) in tab2" @click="clickTab(item)" :key="index" :class="{'route-active':isActive(item)}">
          <span class="gou">√</span>
          <span class="name">{{ item.query.menuName || item.name }}</span>
          <span class="close" @click.stop="removeTab2(item,index)">×</span>
        </li>
        <li class="fg" v-show="this.tab2.length>0"></li>
        <li @click="closeCurrentTab">关闭当前选项</li>
        <li @click="closeAllTab">关闭所有选项</li>
        <li @click="closeOtherTab">关闭其他选项</li>
      </ul>
    </div>
  </div>
</template>

<script>
  export default {
    name: "TopTab",
    props:{
      activeColor:{
        type:String,
        default: "green",
      }
    },
    data() {
      return {
        currentIndex: 0,
        // tab: [
        //   {
        //     name: "驾驶舱",
        //     path: "/main/desktop",
        //     query: {},
        //     active: true
        //   },
        // ],
        // tab2: [],
        showTab2: false,
        showClose:false,
        currentTab: {},
        oldTab: {}
      }
    },
    computed:{
      tab(){
        let tabs;
        let data = this.$store.state.system.tab;
        tabs = typeof data === 'string' ? JSON.parse(data) : data;
        return tabs;
      },
      tab2(){
        let tabs;
        let data = this.$store.state.system.tab2;
        tabs = typeof data === 'string' ? JSON.parse(data) : data;
        return tabs;
      }
    },
    watch: {
      tab2(newVal){
          if(newVal.length==0){
            // this.showTab2=false
          }
      },
      //监听路由的变化来实现添加或切换tab
      '$route': function (to, oldTo) {
        let flag = false;
        for (let i = 0; i < this.tab.length; i++) {
          if (this.tab[i].path === to.path) {
            flag = true;
            this.$store.commit("system/setTabActive", {index: i, active: true,query:to.query});
          } else {
            this.$store.commit("system/setTabActive", {index: i, active: false});
          }
        }

        for (let i = 0; i < this.tab2.length; i++) {
          if (this.tab2[i].path === to.path) {
            flag = true;
            this.$store.commit("system/setTab2Active", {index: i, active: true,query:to.query});
          } else {
            this.$store.commit("system/setTab2Active", {index: i, active: false});
          }
        }


        if (!flag||(to.path.indexOf("/main/desktop")>-1)) {
          let data = {
            path: to.path,
            name: to.name,
            query: to.query,
            active: true,
            meta:to.meta,
            reportId: to.params.id
          };
          if(to.meta.notKeepAlive&&to.meta.notKeepAlive==true){
            this.setExInclude(data,1)
          }else{
            this.setExInclude(data,2)
          }
          to.path.indexOf("/main/desktop")<0?this.$store.commit("system/setTabPush", data):""
          this.$nextTick(()=>{
            this.setTopTab()
          })
        }
      },
    },
    mounted() {
      if(null!=sessionStorage.getItem("kk-tab")&&undefined!=sessionStorage.getItem("kk-tab")){
        this.$store.commit("system/setTab", JSON.parse(sessionStorage.getItem("kk-tab")));
      }
      if(null!=sessionStorage.getItem("kk-tab2")&&undefined!=sessionStorage.getItem("kk-tab2")){
        this.$store.commit("system/setTab2", JSON.parse(sessionStorage.getItem("kk-tab2")));
      }
      window.onresize =()=>{
        this.setTopTab()
      }
      window.addEventListener("beforeunload",()=>{
          sessionStorage.setItem("kk-tab",JSON.stringify(this.tab))
          sessionStorage.setItem("kk-tab2",JSON.stringify(this.tab2))
      })
    },
    methods: {
      isActive(item) {
        if (item.path == this.$route.path && JSON.stringify(item.query) == JSON.stringify(this.$route.query)) {
          return true
        } else {
          return false
        }
      },
      clickTab(tab) {
        this.$router.push(
          {
            path: tab.path,
            query: tab.query
          }
        );
      },
      closeCurrentTab(){
        if(this.tab.length==1){
          return
        }

        this.tab.forEach((item,index)=>{
          if(item.active){
            this.removeTab(item,index)
          }
        })

        this.tab2.forEach((item,index)=>{
          if(item.active){
            this.removeTab2(item,index)
          }
        })
      },
      closeAllTab(){
        if(this.tab.length==1){
          return
        }

         this.tab.forEach((item,index)=>{
            if(index!=0){
              this.setExInclude(item,1)
            }
         })

        this.tab2.forEach((item,index)=>{
            this.setExInclude(item,1)
        })

        let newArr=[]
        newArr.push(this.tab[0])
        this.$store.commit("system/setTab",newArr);
        this.$store.commit("system/setTab2",[]);
        this.$router.push(
          {
            path: this.tab[0].path,
            query: this.tab[0].query
          }
        );
        this.showTab2=false
      },
      closeOtherTab(){
        if(this.tab.length==1){
          return
        }

        let activeIndex=-1
        let istabIndex=true
        this.tab.forEach((item,index)=>{
          if(item.active){
            activeIndex=index
            istabIndex=true
          }

          if(index!=0||item.active!=true){
            this.setExInclude(item,1)
          }
        })

        this.tab2.forEach((item,index)=>{
          if(item.active){
            activeIndex=index
            istabIndex=false
          }

          if(item.active!=true){
            this.setExInclude(item,1)
          }

        })

        let newArr=[]
        newArr.push(this.tab[0])
        if(istabIndex){
          newArr.push(this.tab[activeIndex])
        }else{
          newArr.push(this.tab2[activeIndex])
        }

        this.$store.commit("system/setTab",newArr);
        this.$store.commit("system/setTab2",[]);
        this.showTab2=false
      },
      removeTab(tab, index) {
        let reportArr = JSON.parse(window.sessionStorage.getItem("reportArr"));
        let newReportArr = [];
        if (reportArr != null) {
          reportArr.forEach((v, i) => {
            if (v != tab.reportId) {
              newReportArr.push(v);
            }
          })
        }
        window.sessionStorage.setItem("reportArr", JSON.stringify(newReportArr));
        this.setExInclude(tab,1)
        this.$store.commit("system/setTabSplice",{index:index,count:1});
        if (tab.active) {
          this.$router.push(
            {
              path: this.tab[index - 1].path,
              query: this.tab[index - 1].query
            }
          );
        }

        if(this.tab2.length>0){
          this.$store.commit("system/setTabPush", this.tab2[0]);
          this.$store.commit("system/setTab2Splice", {index:0,count:1});
        }
      },
      removeTab2(tab, index) {
        this.setExInclude(tab,1)
        this.$store.commit("system/setTab2Splice", {index:index,count:1});
        if (tab.active) {
          if (index != 0) {
            this.$router.push(
              {
                path: this.tab2[index - 1].path,
                query: this.tab2[index - 1].query
              }
            )
          } else {
            this.$router.push(
              {
                path: this.tab[this.tab.length - 1].path,
                query: this.tab[this.tab.length - 1].query
              }
            )
          }
        }
      },
      setExInclude(tab,type){
        if(tab.meta&&tab.meta.componentName){
          //设置为不缓存
          if(type==1){
            let e=this.$store.state.system.exincludeList
            let a=false
            e.every((item)=>{
              if(item==tab.meta.componentName){
                a=true
                return false
              }else{
                return true
              }
            })
            if(!a){
              e.push(tab.meta.componentName)
              this.$store.commit("system/setExincludeList",e);
            }
          }else{
            //设置为缓存
            let e=this.$store.state.system.exincludeList
            let newArray=e.filter((item)=>{
                return item!=tab.meta.componentName
            })
            this.$store.commit("system/setExincludeList",newArray);
          }
        }
      },
      setTopTab() {
        let w = document.getElementById("tab").offsetWidth + document.getElementById("down").offsetWidth+20
        let w2 = document.getElementById("top-tab").offsetWidth
        if (w<= w2) {
           if(this.tab2.length>0){
                if((w2-w)>174){
                  this.$store.commit("system/setTabPush", this.tab2[0]);
                  this.$store.commit("system/setTab2Splice", {index:0,count:1});
                  // this.tab.push(this.tab2[0])
                  // this.tab2.splice(0,1)
                  this.$nextTick(()=>{
                    this.setTopTab()
                  })
                }
           }
        } else {
          // this.tab2.push(this.tab[this.tab.length - 1])
          // this.tab.splice(this.tab.length - 1, 1)
          let index=this.tab.length-1
          this.$store.commit("system/setTab2Push", this.tab[this.tab.length - 1]);
          this.$store.commit("system/setTabSplice",{index:index,count:1});
          this.$nextTick(()=>{
             this.setTopTab()
          })
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
 @import "TopTab";
</style>
