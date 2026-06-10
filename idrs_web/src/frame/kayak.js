import axios from "axios";
import Vue from "vue";
import Dialog from "./Dialog.vue";

let sn = 1;
const names = {}; // path --> name
const pages = {}; // path --> page

let user = null; // 当前登录用户，如果没有登录，则为null
let roles = new Set(); // 拥有的角色  (角色名称构成的集合，方便进行检查)

const kayak = {
  app: null, // App.vue 用户弹出信息框

  /** 远程过程调用 */
  rpc(name, action) {
    return new Promise((resolve, reject) => {
      // 将对象转换为json串，跳过以$开始的字段
      const json = JSON.stringify(action, (key, value) => {
        if (key.startsWith("$")) {
          return undefined;
        } else {
          return value;
        }
      });
      let url = "/rpc/" + name;
      if (process.env.NODE_ENV === "production") url = "/ucp/rpc/" + name;
      axios({
          method: "POST",
          url: url,
          data: json,
          xsrfCookieName: "JSESSIONID",
          xsrfHeaderName: "X-XSRF-TOKEN"
        })
        .then(resp => {
          const result = resp.data;

          if (result.error) {
            reject(result.error);
          } else {
            resolve(result.action);
          }
        })
        .catch(error => {
          reject(error);
        });
    }).catch(error => {
      if (this.app) {
        this.app.$message(error, "调用后台失败");
      }
      throw error;
    });
  },

  /** 下载 */
  download(name, action) {},

  // ============================================================


  /** 打开页面 */
  open(title, path, param, onOk) {
    console.log(`打开页面: ${title}: ${path}`);
  },

  // ============================================

  // 检查用户登录状态
  getLoginStatus() {
    return kayak.rpc("pub.GetLoginStatus", {}).then(result => {
      kayak.login(result.user);
      return result;
    });
  },

  /** 记录用户登录信息 */
  login(user0) {
    user = user0;
    roles = new Set();
    if (user && user.roles) {
      for (let role of user.roles) {
        roles.add(role.name);
      }
      Vue.prototype.privilege = {};
      // 挂载登录用户权限,菜单,按钮显隐用
      user.privileges.forEach(item => {
        let index = item.lastIndexOf(".");
        let key = item.substring(index + 1, item.length);
        Vue.prototype.privilege[key] = item;
      });
    }
  },

  /** 去掉用户登录信息 */
  logout() {
    user = null;
    roles = new Set();
  },

  /** 检查该用户是否拥有指定的角色。用户界面上的权限控制 */
  check(roles1) {
    if (roles1 == null) {
      return true;
    }

    for (let role of roles1) {
      if (roles.has(role)) {
        return true;
      }
    }
    return false;
  },

  /** 获取登录用户信息 */
  getLoginUser() {
    return user;
  },

  /** 日期格式化 */
  formatDate(date) {
    if (date != null && date != undefined && date != "") {
      return date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
    } else {
      return "";
    }
  },

  // /** 加载页面 */
  // load(path) {
  //   return new Promise((resolve, reject) => {
  //     let name = names[path];
  //     if (name != null) {
  //       let page = pages[path];
  //       resolve([name, page]);
  //       return;
  //     }
  //
  //     console.log(`加载页面: ${path}`);
  //     let a=import("@/pages/" + path + ".vue");
  //     Vue.component(name, page);
  //     // import("@/pages/" + path + ".vue")
  //     //   .then(module => {
  //     //     console.log(module,"@222")
  //     //     let page = module;
  //     //
  //     //     name = "page" + sn++;
  //     //     names[path] = name;
  //     //     pages[path] = page;
  //     //     Vue.component(name, page);
  //     //
  //     //     resolve([name, page]);
  //     //   })
  //     //   .catch(e => {
  //     //     console.log(`加载页面失败: ${path}`);
  //     //
  //     //     reject(e);
  //     //   });
  //   });
  // }

};

/** 显示对话框 */
function showDialog(dialog) {}

export default kayak;
