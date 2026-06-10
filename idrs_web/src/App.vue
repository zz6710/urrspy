<template>
  <div id="app">
    <notifications ref="notifications"></notifications>
    <router-view v-if="isRouterAlive"></router-view>
  </div>
</template>

<script>
  import kayak from '@/frame/kayak.js'
  import auth from '@/utils/auth.js'

  export default {
    name: 'App',
    provide (){
      return{
        reload: this.reload
      }
    },
    data() {
      return {
        isRouterAlive: true
      }
    },
    created() {
      kayak.app = this;
    },
    methods:{
      setLocalData(query) {
        debugger
        console.log(this.$route, query, 'query');
        if (query.username) {
          localStorage.setItem("username", query.username);
        }
        if (query.userid) {
          localStorage.setItem("userid", query.userid);
        }
        if (query.token) {
          localStorage.setItem("token", query.token);
          auth.setToken(query.token);
        }
        if (query.roleids) {
          localStorage.setItem("roleids", query.roleids);
        }
        this.getServer(query.userid);
      },
      getServer(userid) {
        this.httpUtil
        .query({
          url: "getAuthServers.json",
          params: {
            userid:userid
          },
        }).then((data)=>{
         // console.log(data,'----------')
           localStorage.setItem("servers", data.returndata.servers);
           this.isRouterAlive = true
        })
      },
      reload() {
        this.isRouterAlive = false
        this.$nextTick(function () {
          this.isRouterAlive = true
        })
      }
    },
    watch: {
      '$route': {
        handler(v,o) {
          if(v.name && !o.name && o.path == '/') {
            console.log(v,o,'-------')
            if (v.path == '/main/desktop') {
              debugger
              console.log("v::"+v);
              this.isRouterAlive = false
              this.setLocalData(v.query)
            }
          }
        },
        immediate: true
      }
    }
  }
</script>

<style lang="scss">
  @import "./assets/scss/k-menu";

  body {
    padding: 0;
    margin: 0;

    .highest-priority {
      z-index: 3005 !important;
    }
  }

  #app {
    font-family: "Avenir", Helvetica, Arial, sans-serif;
    /* -webkit-font-smoothing: antialiased; */
    -moz-osx-font-smoothing: grayscale;
    color: #2c3e50;
    height: 100%;
    width: 100%;
    /* background-color: #3fd5be; */
    position: absolute;
    top: 0;
    left: 0;
    bottom: 0;
    right: 0;
  }


  .v-modal {
    display: none;
  }

  .el-dialog__wrapper {
    z-index: 10 !important;
  }

  .kk-mask {
    position: fixed;
    right: 0;
    bottom: 0;
    width: 100%;
    height: calc(100vh - 0px);
    background-color: black;
    opacity: 0.5;
    z-index: -1;
    display: block;
  }

  .v-modal {
    display: none;
    width: 0 !important;
    height: 0 !important;
    z-index: -200 !important;
    position: absolute !important;
  }
  .el-table th.gutter {
    display: table-cell !important;
  }
</style>
