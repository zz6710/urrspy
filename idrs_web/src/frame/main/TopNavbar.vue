<template>
  <div>
    <k-popup ref="announcePop" :data-title="announce.title">
      <k-form ref="announceForm" :data-col="1" data-ui="element">
        <k-form-item label="内容" data-ui="element">
          <k-field-display v-model="announce.content" data-type="html" />
        </k-form-item>
        <k-form-item label="附件" data-ui="element">
          <a href="javascript:void(0)" @click="download(announce.annfilepath, announce.annfilename)">{{announce.annfilename}}</a>
        </k-form-item>
      </k-form>
    </k-popup>
    <md-toolbar md-elevation="0" class="md-transparent" :class="{
        'md-toolbar-absolute md-white md-fixed-top': $route.meta.navbarAbsolute
      }">

      <div class="md-toolbar-row">
<!--        <div class="md-toolbar-section-start">-->
<!--          <h3 class="md-title">{{ $route.name }}</h3>-->
<!--        </div>-->
        <div class="md-toolbar-section-end">
          <div class="md-collapse">
            <span class="TopNav__CurrentWorkday" v-show="currentWorkday">系统日期：{{currentWorkday}}</span>

            <k-form :data-col="1" data-input-width="177px" data-label-width="0px">
              <k-form-item dataUi="material">
                <k-field-select v-model="menuData" @data-on-change="menuChange" :data-data="subMenus" data-placeholder="搜索菜单..."
                                data-display-field="menuname" data-value-field="url"/>
              </k-form-item>
            </k-form>
            <md-list class="menu-list">
              <div class="menu-com-list">
                <li class="md-list-item" v-for="(menu,index) in comMenus" :key="index">
                  <a :href="'#/main/' + menu.url" class="md-list-item-router md-list-item-container md-button-clean dropdown">
                    <div class="md-list-item-content">
                      <drop-down :ref="'dropDown'+ index" direction="down" toggle-type="contextmenu">
                        <k-btn :data-descript="menu.menuname" dataTooltipClass="highest-priority" slot="title" class="md-button md-just-icon md-simple"
                               data-toggle="dropdown">
                          <md-icon class="menu-icon" v-if="menu.icon" :md-src="'/static/svg/'+menu.icon+'.svg'"></md-icon>
                          <md-icon v-if="!menu.icon" :md-src="'/static/svg/'+menu.icon+'.svg'"></md-icon>
                        </k-btn>
                        <ul class="dropdown-menu dropdown-menu-right">
                          <li><a @click="deleteComMenu(menu,index)" href="javascript:void(0)">取消置顶</a></li>
                        </ul>
                      </drop-down>
                    </div>
                  </a>
                </li>
              </div>
              <md-list-item v-if="hasMenuMore && !isOpenMenuMore" @click="openMenuMore" href="javascript:void(0)">
                <md-icon md-src="/static/svg/menu-more-left.svg"></md-icon>
              </md-list-item>
              <md-list-item v-if="hasMenuMore && isOpenMenuMore" @click="hideMenuMore" href="javascript:void(0)">
                <md-icon md-src="/static/svg/menu-more-right.svg"></md-icon>
              </md-list-item>

              <md-list-item>
                |
              </md-list-item>
              <div class="menu-com-list">
                <li class="md-list-item">
                  <a href="#/main/desktop" class="md-list-item-router md-list-item-container md-button-clean dropdown">
                    <div class="md-list-item-content">
                      <k-btn data-descript="首页" dataTooltipClass="highest-priority" slot="title" class="md-button md-just-icon md-simple" data-toggle="dropdown">
                        <md-icon md-src="/static/svg/home.svg"></md-icon>
                      </k-btn>
                    </div>
                  </a>
                </li>
              </div>

              <li class="md-list-item">
                <a  class="md-list-item-router md-list-item-container md-button-clean dropdown">
                  <div class="md-list-item-content">
                    <drop-down direction="down">
                      <k-btn data-descript="用户信息" dataTooltipClass="highest-priority" slot="title" class="md-button md-button md-just-icon md-simple md-theme-default" data-toggle="dropdown">
                        <md-icon md-src="/static/svg/user.svg"></md-icon>
                      </k-btn>
                      <ul class="dropdown-menu dropdown-menu-left" style=" margin-left: -80px;">

                        <li  @click='popEditUserInfor()'>
                          <a> <span class="el-icon-s-custom" style="margin-right: 8px;"></span> 个人信息</a>
                        </li>
                        <li  @click='popChangePwd()'>
                          <a> <span class="el-icon-unlock" style="margin-right: 8px;"></span> 修改密码</a>
                        </li>

                      </ul>
                    </drop-down>
                  </div>
                </a>
              </li>

<!--              <li class="md-list-item">-->
<!--                <a href="javascript:void(0)" @click="loadAnnounce" class="md-list-item-router md-list-item-container md-button-clean dropdown">-->
<!--                  <div class="md-list-item-content">-->
<!--                    <drop-down direction="down">-->
<!--                      <k-btn slot="title" class="md-button md-just-icon md-simple" data-toggle="dropdown">-->
<!--                        <md-icon md-src="/static/svg/notice.svg"></md-icon>-->
<!--                      </k-btn>-->
<!--                      <ul class="dropdown-menu dropdown-menu-right">-->
<!--                        <li v-for="(item,index) in announces" :key="item.annid" @click='popAnnounce(item)'>-->
<!--                          <a href="javascript:void(0)" v-html="item.title"></a>-->
<!--                        </li>-->
<!--                      </ul>-->
<!--                    </drop-down>-->
<!--                  </div>-->
<!--                </a>-->
<!--              </li>-->

              <div class="menu-com-list">
                <li class="md-list-item">
                  <a href="#" @click="loginOut" class="md-list-item-router md-list-item-container md-button-clean dropdown">
                    <div class="md-list-item-content">
                      <k-btn data-descript="安全退出" dataTooltipClass="highest-priority" slot="title" class="md-button md-just-icon md-simple" data-toggle="dropdown">
                        <md-icon md-src="/static/svg/loginout.svg"></md-icon>
                      </k-btn>
                    </div>
                  </a>
                </li>
              </div>

<!--              <li class="md-list-item">-->
<!--                <a href="javascript:void(0)" class="md-list-item-router md-list-item-container md-button-clean dropdown">-->
<!--                  <div class="md-list-item-content">-->
<!--                    <drop-down direction="down">-->
<!--                      <md-button slot="title" class="md-button md-just-icon md-simple" data-toggle="dropdown">-->
<!--                        <md-icon md-src="/static/svg/more.svg"></md-icon>-->
<!--                      </md-button>-->
<!--                      <ul class="dropdown-menu dropdown-menu-right" style="margin-left: -150px;">-->
<!--                        <li><a @click="addComMenu" href="javascript:void(0)">+常用功能</a></li>-->
<!--                      </ul>-->
<!--                    </drop-down>-->
<!--                  </div>-->
<!--                </a>-->
<!--              </li>-->
            </md-list>
          </div>
        </div>
      </div>
    </md-toolbar>


    <!--    修改用户弹出框   -->
    <k-popup ref="editUserPopup" data-title="编辑用户信息">
      <k-form ref="editUserForm" :data-col="1" >
        <k-form-item label="登录名" >
          <k-field-text v-model="formData.loginname" data-disabled />
        </k-form-item>
        <k-form-item label="所属部门">
          <k-field-text v-model="formData.deptname" data-disabled />
        </k-form-item>
        <k-form-item label="用户名称">
          <k-field-text v-model="formData.username" @input="forceUpdate" :data-max-length="32" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="移动电话">
          <k-field-text v-model="formData.mobileno" @input="forceUpdate" data-validate-type="number" :data-max-length="11"/>
        </k-form-item>
        <k-form-item label="电子邮箱">
          <k-field-text v-model="formData.email" data-validate-type="email" @input="forceUpdate" :data-max-length="64"/>
        </k-form-item>
        <k-form-item label="性别">
          <k-field-select v-model="formData.sex" data-dict="sex" @input="forceUpdate"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="User.updateUserNoAuth" data-from="editUserForm"
                 :data-model="formData">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>


    <k-popup ref="resetPwdPopup" data-title="修改密码">
      <k-form ref="resetPwdForm" :data-col="1">
        <k-form-item label="原密码">
          <k-field-text v-model="pwdData.oldPwd" data-show-password :dataAllowblank="false" />
        </k-form-item>
        <k-form-item label="新密码" style="margin-top: 20px">
          <k-field-text v-model="pwdData.newPwd" data-regx="^(?!([A-Z]*|[a-z]*|[0-9]*|[!-/:-@\[-`{-~]*|[A-Za-z]*|[A-Z0-9]*|[A-Z!-/:-@\[-`{-~]*|[a-z0-9]*|[a-z!-/:-@\[-`{-~]*|[0-9!-/:-@\[-`{-~]*)$)[A-Za-z0-9!-/:-@\[-`{-~]{8,15}$"
                        data-regx-text="密码要求包含大写字母、小写字母、数字、特殊符号中至少三种类型，长度为8~15位"  data-show-password :dataAllowblank="false"/>
        </k-form-item>
        <k-form-item label="确认密码" style="margin-top: 20px">
          <k-field-text v-model="pwdData.confirmNewPwd"data-show-password :dataAllowblank="false"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="User.resetPwdCheckOldPwd" :data-model="pwdData"
                 :data-handler="resetPwdHandler" data-form="resetPwdForm" data-target="userGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

  </div>
</template>

<script>
  import httpUtil from "../httpUtil";
  import {
    merge
  } from "lodash";
  import Tools from "@/utils/tools";
  import MD5 from "@/frame/md5.js"
  import auth from "../../utils/auth";

  export default {
    props: {
      subMenus: {
        type: Array
      }
    },
    data() {
      return {
        menuData: '',
        comMenus: [],
        minMenus: [],
        allMenus: [],
        hasMenuMore: false,
        isOpenMenuMore: false,
        announces: [],
        announce: {},
        userRole: '',
        formData: {
          loginname: ""
        },
        pwdData : {},
        currentWorkday: null
      };
    },
    created() {
      Tools.getLoginUser().then(res => {
        this.userRole = res.roleids;
      })
      this.loadComMenu();
      this.httpUtil.sysDate().then(res=>{
        if (res) {
          this.currentWorkday = Tools.formatDate(res)
          localStorage.setItem('currentWorkday', res)
        }
      })
    },
    methods: {
      loadAnnounce() {
        let graphql =
          '{queryAnnounceInfo(action:"show") {rows{title, content, createdate, createtime, annfilepath, annfilename},results}}'
        this.httpUtil
          .graphqlQurey({
            graphql: graphql,
            params: {
              "roleid": this.userRole
            }
          })
          .then(data => {
            this.announces = data["queryAnnounceInfo"].rows;
          });
      },
      download(path, filename) {
        this.httpUtil
          .download({
            url: "/base/comn-download.json",
            params: {
              "path": path
            },
            filename: filename
          })
      },
      loginOut(){
        auth.removeToken()
        this.$router.push({
          path: "/login"
        });
      },
      popAnnounce(announce) {
        this.announce = announce
        this.$refs.announcePop.popup();
      },
      popEditUserInfor() {
        this.$refs.editUserPopup.popup();
        this.getUserInfor();
      },
      popChangePwd() {
        this.$refs.resetPwdPopup.popup();
        this.getUserPwdInfor();
      },
      toggleSidebar() {
        this.$sidebar.displaySidebar(!this.$sidebar.showSidebar);
      },
      minimizeSidebar() {
        if (this.$sidebar) {
          this.$sidebar.toggleMinimize();
        }
      },
      menuChange(url) {
        //判断当前打开的地址是不是已经打开了
        let currentUrl = this.$router.history.current.path;
        let openUrl = "/main/" + url;

        if (currentUrl != openUrl) {
          this.$router.push({
            path: openUrl
          });
        }
      },
      loadComMenu() {
        this.httpUtil.query({
          url: "sys/findComMenus.json"
        }).then(data => {
          if (data.rows.length <= 5) {
            this.comMenus = data.rows;
            this.hasMenuMore = false;
          } else {
            this.hasMenuMore = true;
            this.minMenus = [];
            this.allMenus = data.rows;
            for (let i = 0; i < 5; i++) {
              this.minMenus.push(data.rows[i]);
            }
            if (this.isOpenMenuMore) {
              this.comMenus = this.allMenus;
            } else {
              this.comMenus = this.minMenus;
            }
          }

        });
      },
      addComMenu() {
        let currentUrl = this.$router.history.current.path;
        this.httpUtil.update({
          url: "sys/addComMenu.json",
          params: {
            url: currentUrl
          }
        }).then(data => {
          this.loadComMenu();
        });
      },
      deleteComMenu(menu, index) {
        this.httpUtil.update({
          url: "sys/deleteComMenu.json",
          params: menu
        }).then(data => {
          this.loadComMenu();
        });
        if (this.$refs['dropDown' + index][0]) {
          this.$refs['dropDown' + index][0].closeDropDown();
        }
      },
      openMenuMore() {
        this.isOpenMenuMore = true;
        this.comMenus = this.allMenus;
      },
      hideMenuMore() {
        this.isOpenMenuMore = false;
        this.comMenus = this.minMenus;
      },
      getUserInfor(){
        Tools.getLoginUser().then(res =>{
          this.formData.userid = res.userid;
          this.formData.loginname = res.loginname;
          this.formData.username = res.username;
          this.formData.mobileno = res.mobileno;
          this.formData.deptname = res.deptname;
          this.formData.passwd = res.passwd;
          this.formData.email = res.email;
          this.formData.sex = res.sex;
        });

      },
      getUserPwdInfor(){
        this.pwdData = {};
        Tools.getLoginUser().then(res =>{
          console.log(res)
          this.pwdData.loginname = res.loginname;
          this.pwdData.userid = res.userid;
        });
      },
      resetPwdHandler(params) {
        console.log(params)
        if (params.newPwd !== params.confirmNewPwd) {
          this.$message.error("两次输入不一致！请重新输入")
          return false
        }
        params.passwd = MD5.MD5(params.loginname + params.newPwd)
        params.oldPwd = MD5.MD5(params.loginname + params.oldPwd)
        params.newPwd = {}
        params.confirmNewPwd = {}
        return true
      },
      forceUpdate(){
        this.$forceUpdate();
      },
    }
  };
</script>

<style lang="scss" scoped>
  .menu-list svg {
    width: 20px;
    height: 20px;
  }
  ::v-deep .dropdown-menu{
    margin-top: 10px;
    right: auto;
  }

  @media (max-width: 991px){
    .md-toolbar .md-collapse {
      display: flex !important;
      width: 100%;
      -webkit-box-pack: end;
      -ms-flex-pack: end;
      justify-content: flex-end;
    }
  }

  ::v-deep .md-field .md-input-action {
    top: 65%;
  }

  .k-form {
    width: 197px;
    display: flex;
    align-items: center;
  }

  .TopNav__CurrentWorkday {
    display: block;
    line-height: 50px;
  }


</style>
