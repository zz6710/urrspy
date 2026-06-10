<template>
  <div class="md-layout text-center">
    <div class="login-content md-layout-item">
      <login-card header-color="green" :loginConfig="loginConfig" :showHeader="false">
        <h2 slot="description" class="description">开科唯识{{loginConfig.systemName}}</h2>
        <!-- <h4 slot="sub-title" class="title" style="font-size: 18px;font-weight: initial;">修改密码</h4> -->
        <!-- <md-field class="md-form-group" slot="inputs" style="margin-top: 33px;">
          <md-icon>face</md-icon>
          <label>账号</label>
          <md-input v-model="username"  @keyup.enter="convert_password" disabled="true"></md-input>
        </md-field> -->
        <el-input class="input-user" placeholder="工号" v-model="username" slot="inputs" :disabled="true">
          <md-icon slot="suffix" class="form-icon" md-src="/static/svg/login/icon/username.svg"></md-icon>
        </el-input>
        <!-- <md-field class="md-form-group" slot="inputs" :md-toggle-password="false">
          <md-icon>lock_outline</md-icon>
          <label>密码</label>
          <md-input id="passwordInput" @keyup.enter="convert_login"  v-model="password" type="password"></md-input>
        </md-field>
        <md-field class="md-form-group" slot="inputs" :md-toggle-password="false">
          <md-icon>lock_outline</md-icon>
          <label>新密码</label>
          <md-input id="passwordInput" @keyup.enter="convert_login"  v-model="newPassword" type="password"></md-input>
        </md-field> -->
        <el-input class="input-pwd" id="passwordInput" placeholder="密码" v-model="password" slot="inputs" type="password">
          <md-icon slot="suffix" class="form-icon" md-src="/static/svg/login/icon/pwd.svg"></md-icon>
        </el-input>
        <el-input class="input-pwd" id="passwordInput" placeholder="新密码" v-model="newPassword" slot="inputs" type="password">
          <md-icon slot="suffix" class="form-icon" md-src="/static/svg/login/icon/pwd.svg"></md-icon>
        </el-input>
        <el-input class="input-pwd" id="passwordInput" placeholder="确认新密码" v-model="confirmPassword" slot="inputs" type="password">
          <md-icon slot="suffix" class="form-icon" md-src="/static/svg/login/icon/pwd.svg"></md-icon>
        </el-input>

        <!-- <md-field class="md-form-group" slot="inputs" :md-toggle-password="false">
          <md-icon>lock_outline</md-icon>
          <label>确认新密码</label>
          <md-input id="passwordInput" @keyup.enter="convert_login"  v-model="confirmPassword" type="password"></md-input>
        </md-field> -->
        <md-button slot="footer" @click="doLogin" class="btn-custom-primary">
          确 认
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
  import Tools from "@/utils/tools";
  import Notifications from '@/components/k-material/NotificationPlugin'
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
        username: this.$route.query.username,
        password: '',
        newPassword: '',
        confirmPassword: '',
        loading: false,
        showDialog: false
      };
    },
    methods: {
      doLogin() {
        if(this.password===''||this.password===undefined||this.password===null){
          Tools.alert("原密码不能为空！","danger");
          return ;
        }
        if(this.newPassword==''||this.newPassword==undefined||this.newPassword==null){
          Tools.alert("新密码不能为空！","danger");
          return ;
        }
        let pass = this.newPassword.match("^(?!([A-Z]*|[a-z]*|[0-9]*|[!-/:-@\\[-`{-~]*|[A-Za-z]*|[A-Z0-9]*|[A-Z!-/:-@\\[-`{-~]*|[a-z0-9]*|[a-z!-/:-@\\[-`{-~]*|[0-9!-/:-@\\[-`{-~]*)$)[A-Za-z0-9!-/:-@\\[-`{-~]{8,15}$");
        if(pass==''||pass==undefined||pass==null){
          Tools.alert("密码要求包含大写字母、小写字母、数字、特殊符号中至少三种类型，长度为8~15位！","danger");
          return ;
        }
        if(this.newPassword!=this.confirmPassword){
          Tools.alert("新密码与确认密码不一致！","danger");
          return ;
        }
        this.httpUtil.query({
          url: "jwt/resetPwd.json",
          params: {
            username: this.username,
            password: MD5.MD5(this.username + this.password),
            newPassword: MD5.MD5(this.username + this.newPassword)
          }
        }).then(data => {
          if(data.success){
           Tools.alert(data.returnmsg);
            this.$router.push({
              path: '/login'
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
        ret.color = this.loginConfig.loginColor + "!important";
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
          path, rect {
            stroke: rgba(0,0,0,0.9);
          }
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
