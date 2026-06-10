import Cookies from 'js-cookie'
import Tools from "@/utils/tools";

const TokenKey = 'Authorization'

let auth = {};

auth.getToken = function() {
  // return Cookies.get(TokenKey)
  return localStorage.getItem("token")
};

auth.setToken = function(token) {
  localStorage.setItem("token", token)
  // return Cookies.set(TokenKey, token)
};

auth.removeToken = function() {
  Tools.loginUser = undefined
  return Cookies.remove(TokenKey)
};

auth.check = function(server) {
  let userid = localStorage.getItem("userid");

  if (userid == 'admin') { //超级管理员不控制权限
    return true;
  }
  let serversArr = [];
  let servers = localStorage.getItem("servers");
  if (servers != '' && servers != undefined) {
    serversArr = servers.split(",");
  }
  let result = false;
  if (serversArr.some(ser => ser == server)) {
    result = true;
  }
  return result;
}

export default auth;
