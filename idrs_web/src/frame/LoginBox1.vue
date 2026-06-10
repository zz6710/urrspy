<template>
  <div class="md-layout text-center">
    <div class="login-content md-layout-item">
      <login-card header-color="green" :loginConfig="loginConfig" :showHeader="false">
        <h2 slot="description" class="description">浦银理财{{loginConfig.systemName}}</h2>
        <md-button slot="footer" @click="doLogin" class="btn-custom-primary">
          统 一 认 证 登 录
        </md-button>
      </login-card>
    </div>
  </div>
</template>
<script>
  import kayak from '@/frame/kayak.js'
  import auth from "@/utils/auth.js"
  import MD5 from "@/frame/md5.js"
  import LoginCard from "./LoginCard";
  import global from "../frame/global.js";

  import Notifications from '@/components/k-material/NotificationPlugin'
  import Tools from "../utils/tools";
  export default {
    components: {
      LoginCard
    },
    props: {
      loginConfig: {
        type: Object,
        default: new Object()
      }
    },
    data() {
      return {
        remember: false, // 记住密码
        username: '',
        password: '',
        loading: false,
        showDialog: false,
        eiamState:'0'
      };
    },


  //   created() {
  //       this.httpUtil
  //         .ajax({
  //           url: "jwt/queryState.json",
  //           params: {}
  //         }).then(res => {
  //           console.log(res.returnmsg);
  //            this.eiamState=res.returnmsg;
  //       });
  //
  //
  // },
    methods: {
      setTab(){
        sessionStorage.removeItem("kk-tab")
        sessionStorage.removeItem("kk-tab2")
        this.$store.commit("system/setTab", [
          {
            name: "首页",
            path: "/main/desktop",
            query: {},
            active: true
          },]);
        this.$store.commit("system/setTab2", []);
      },
      doLogin() {
        location.href= getURL().baseUrl+"jwt/loginAuth.json"
      },
      convert_password(){
        document.getElementById('passwordInput').focus()
      },
      convert_login(){
        this.doLogin()
      }
    },
    computed: {
      loginBtn() {
        const ret = {};
        ret.color = '#003472 !important'; //this.loginConfig.loginColor + "!important";
        return ret;
      }
    }
  };
</script>

<style lang="scss" scoped>
  .login-content i {
    width: 23px !important;
    height: 23px !important;
  }
  .login-btn {
    width: 310px;
    height: 40px !important;
    .md-ripple {
      font-size: 16px;
    }
  }
  /deep/ .description {
    color: #003472;
    font-size: 28px;
    line-height: 46px;
    margin-bottom: 14px;
    font-weight: 400;
  }
  .btn-custom-primary {
    height: 47px !important;
    width: 100%;
    margin: 30px 0 24px !important;
    /deep/ .md-ripple {
      font-size: 18px;
      color: #fff;
    }
  }
</style>
