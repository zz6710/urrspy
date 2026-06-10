<template>
  <div class="md-layout text-center">
    <div class="login-content md-layout-item">
      <login-card header-color="green" :loginConfig="loginConfig" :showHeader="false">
        <h2 slot="description" class="description">开科唯识{{loginConfig.systemName}}</h2>
        <!-- <md-field class="md-form-group" slot="inputs" style="margin-top: 33px;">
          <md-icon>face</md-icon>
          <label>账号</label>
          <md-input v-model="username"  @keyup.enter="convert_password"></md-input>
        </md-field> -->
        <!-- <md-field class="md-form-group" slot="inputs" :md-toggle-password="false">
          <md-icon>lock_outline</md-icon>
          <label>密码</label>
          <md-input id="passwordInput" @keyup.enter="convert_login"  v-model="password" type="password"></md-input>
        </md-field> -->
        <el-input class="input-user" placeholder="工号" v-model="username" slot="inputs">
          <md-icon slot="suffix" class="form-icon" md-src="/static/svg/login/icon/username.svg"></md-icon>
        </el-input>
        <el-input class="input-pwd" id="passwordInput" placeholder="密码" v-model="password" slot="inputs" :type="canViewPwd ? 'text' : 'password'" @keyup.enter="convert_login">
          <md-icon slot="suffix" class="form-icon" md-src="/static/svg/login/icon/pwdView.svg" v-if="canViewPwd" @click.native="canViewPwd = !canViewPwd"></md-icon>
          <md-icon slot="suffix" class="form-icon" md-src="/static/svg/login/icon/pwdNoview.svg" v-else @click.native="canViewPwd = !canViewPwd"></md-icon>
        </el-input>

        <md-button slot="footer" @click="doLogin" class="btn-custom-primary">
          登 录
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
        eiamState:'0',
        canViewPwd: false,
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
        let pass = this.password.match("^(?!([A-Z]*|[a-z]*|[0-9]*|[!-/:-@\\[-`{-~]*|[A-Za-z]*|[A-Z0-9]*|[A-Z!-/:-@\\[-`{-~]*|[a-z0-9]*|[a-z!-/:-@\\[-`{-~]*|[0-9!-/:-@\\[-`{-~]*)$)[A-Za-z0-9!-/:-@\\[-`{-~]{8,15}$");
        if (pass == null || pass === "") {
          pass = false;
        } else {
          pass = true;
        }
        if (this.username === 'admin')
          pass = true;

        var pwd = MD5.MD5(this.username + this.password);
        if(this.eiamState=='1'){
           pwd = this.password;
        }
        this.httpUtil.query({
          url: "jwt/login.json",
          params: {
            username: this.username,
            password: pwd,
            pass: pass
          }
        })
        .then(data => {
          if(data.success){
            auth.setToken(data.returndata.token);
            localStorage.setItem("token", data.returndata.token);
            localStorage.setItem("username", data.returndata.username);
            localStorage.setItem("userid", data.returndata.userid);
            localStorage.setItem("servers", data.returndata.servers);
            localStorage.setItem("roleids", data.returndata.roleids);
            this.$store.commit("system/setGridMaxHeight", data.returndata.gridMaxHeight);
            //长沙报送系统去掉用户组权限  axin  20220718
            // this.global.getProdUser(data.returndata.userid);
            // this.global.getRoleAndProd(data.returndata.userid);
            this.setTab()
            this.$router.push({
              path: '/main'
            });
          }if(!data.success&& data.returndata.reset ==true){
           // Tools.alert(data.returnmsg);
            this.$router.push({
              path: '/resetPwd',
              query: {
                username: this.username
              }
            });
          }
        });
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
    /* max-width: ; */
  }
  /deep/ .description {
    color: #003472;
    font-size: 28px;
    line-height: 46px;
    margin-bottom: 14px;
    font-weight: 400;
  }

  /deep/ {
    .el-input  {
      margin-bottom: 24px;
      .el-input__inner {
        height: 38px;
        line-height: 38px;
        color: #000;
        &::placeholder {
          color: rgba(0,0,0,0.5);
        }
      }
      .el-input__suffix {
        display: flex;
        align-items: center;
      }
      &.input-user {
        path {
          stroke: rgba(0,0,0,0.9);
        }
      }
      svg {
        width: 18px;
      }
      &.input-pwd {
        svg {
          cursor: pointer;
        }
      }
    }
    .btn-custom-primary {
      height: 47px !important;
      width: 100%;
      margin: 0 0 24px !important;
      .md-ripple {
        font-size: 18px;
        color: #fff;
      }
    }
  }

</style>
