import Tools from "@/utils/tools";
import httpUtil from "../frame/httpUtil";
import auth from "../utils/auth"
/**
 * 全局常量、方法封装模块
 * 通过原型挂载到Vue属性
 * 通过 this.global 调用
 */

let global = {};
global.notify_type = {
  success: "success",
  warning: "warning",
  info: "info",
  error: "error"
}

global.wf_process_default_type = "2";

global.wf_process_status = {
  running: "1",
  reject: "2",
  refuse: "3",
  reject_to_apply: "4",
  pass: "5",
  re_apply: "6",
  approvaling: "7",
  revoke: "8",
  finish: "9"
}

global.wf_business_status = {
  ready: "0",
  processing: "1",
  finish: "2",
  error: "3",
  error_confirmed: "4"
}


//用户组权限设置，axin 2020
global.getProdUser = function (userid) {
  //長沙不涉及产品权限，去掉用户组权限 20220716  axin
  // if (!userid){
  //   userid = localStorage.getItem("userid");
  // };
  // httpUtil.comnQuery({
  //   action: 'T8ProdUser.findT8ProdUserAll',
  //   params: {userid : userid}
  // }).then(data => {
  //  if(data.rows.length > 0){
  //    let T8ProdList = '';
  //    for(let i = 0 ; i < data.rows.length ; i++){
  //      T8ProdList += data.rows[i].t8ProdInfoId;
  //      T8ProdList += ','
  //      localStorage.setItem("T8ProdList", T8ProdList);
  //    }
  //  }else{
  //    localStorage.setItem("T8ProdList", '');
  //  }
  // });
}
//判断用户组权限，axin
global.getProdIfUser = function (prodInfoId){
  if (localStorage.getItem("userid") == 'admin') {
    return true;
  } else {
    let T8ProdList = localStorage.getItem('T8ProdList');
    T8ProdList = T8ProdList.slice(0, T8ProdList.length - 1);
    let T8ProdArr = T8ProdList.split(',');
    return T8ProdArr.indexOf(prodInfoId) > -1;
  }
}

//判断是否是admin用户，axin
global.getUswerIfAdmin = function (prodInfoId){
  if (localStorage.getItem("userid") == 'admin') {
    return true;
  } else {
   return false;
  }

}

// 根据用户id查询所属产品用户组产品id与角色id rennannan 20210615
global.getRoleAndProd = function (userid) {
  if (!userid) {
    userid = localStorage.getItem("userid");
  }
  ;
  httpUtil.comnQuery({
    action: 'T8ProdUser.findProdByUserRoleId',
    params: {userid: userid}
  }).then(data => {
    if (data.rows.length > 0) {
      localStorage.setItem("userRoleProds", JSON.stringify(data.rows));
    } else {
      localStorage.setItem("userRoleProds", '');
    }
  });
}
//根据角色id与产品id判断是否有操作权限  rennannan 20210615
global.enableOpe = function (prodInfoId, roleId) {
  if (localStorage.getItem("userid") == 'admin') {
    return true;
  } else {
    var userRoleProds = localStorage.getItem('userRoleProds')
    if (userRoleProds) {
    let T8ProdList = JSON.parse(userRoleProds);
      for (let i = 0; i < T8ProdList.length; i++) {
        if (T8ProdList[i].t8ProdInfoId == prodInfoId && T8ProdList[i].roleId == roleId) {
          return true
        }
      }
    }
  }
  return false;
}
//公共隐藏按钮方法  用于控制不同节点控制不同按钮隐藏  rennannan 20210322
global.getHideButtons = function (item) {
  let hideButtonIds = item.$route.query.hideButtonids;
  if (hideButtonIds != '' && hideButtonIds != undefined) {//不为空才做操作
    //如果有多个值时是以逗号隔开，需要用逗号进行拆分成为数组
    let hideButtons = hideButtonIds.split(',');
    for (let i = 0; i < hideButtons.length; i++) {
      let propValue = hideButtons[i];
      item.$set(item.$data, propValue, false); //将是否展示按钮属性设置为false
    }
  }
}
//通过action判断当前用户是否需要隐藏该按钮
global.isShowAuthorityButton = function(action) {
  //判断权限是否显示或者隐藏
  if (action) {
    return auth.check(action);
  } else {
    return false;
  }
}


global.wf_application_en_task_name = "applicationTask"



export default global;
