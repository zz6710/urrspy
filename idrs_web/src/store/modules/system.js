import {Auth} from './auth';
const state = () => ({
  cardBackground: "#4caf50",
  level1Menu:[],
  gridMaxHeight:"500",
  /** 当前tab   '/home' 可以更改为自己默认的页面*/
  editableTabsValue:Auth.getEditableTabsValue() || '/main/desktop',
  /*tab数组*/
  editableTabs:Auth.getEditableTabs() || [{title:'首页',name:'/main/desktop'}],
  tab:[
    {
      name: "首页",
      path: "/main/desktop",
      query: {},
      active: true
    },],
  tab2:[],
  exincludeList:[],
})

// getters
const getters = {}

// actions
const actions = {}

// mutations
const mutations = {
  setBackground(state, color) {
    state.cardBackground = color
  },
  setLevel1Menu(state, level1Menu){
    state.level1Menu = level1Menu
  },
  setGridMaxHeight(state, height) {
    state.gridMaxHeight = height
  },
  //退出登录时注销tab
  ACCOUNT_LOGOUT_FAILURE(state) {
    state.editableTabsValue = '/home';
    state.editableTabs = [{title:'首页',name:'/home'}];
    //其他代码
    Auth.removeEditableTabs();
    Auth.removeEditableTabsValue()
  },
  //设置当前tab数组
  setEditableTabs(state,data){
    state.editableTabs = data;
    Auth.setEditableTabs(data);
  },
  //设置当前tab
  setEditableTabsValue(state,data){
    state.editableTabsValue = data;
    Auth.setEditableTabsValue(data);
  },
  setExincludeList(state, exincludeList) {
    state.exincludeList = exincludeList
  },
  setTab(state, tab) {
    state.tab = tab
  },
  setTabPush(state, tabitem) {
    // state.tab.unshift(tabitem)
    // let mid = state.tab[0];
    // state.tab[0] = state.tab[1];
    // state.tab[1] = mid;
    const index = state.tab.findIndex(item=>item.name == tabitem.name);
    if (index > -1) {
      state.tab[index] = tabitem;
    } else {
      state.tab.push(tabitem);
    }
  },
  setTabSplice(state, param){
    state.tab.splice(param.index,param.count)
  },
  setTabActive(state, param){
    state.tab[param.index]['active']=param.active
    if(param.query){
      state.tab[param.index]['query']=param.query
    }
  },
  setTab2Active(state, param){
    state.tab2[param.index]['active']=param.active
    if(param.query){
      state.tab2[param.index]['query']=param.query
    }
  },
  setTab2(state, tab2){
    state.tab2 = tab2
  },
  setTab2Push(state, tabitem) {
    state.tab2.push(tabitem)
  },
  setTab2Splice(state,param){
    state.tab2.splice(param.index,param.count)
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
