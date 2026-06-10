import  * as TEMP_CODE from './componentTempleCode'
const vueBeautify = require('vue-beautify');

let template = "";          // 前端代码
let data = {
  template: [],             // 组件
  props: "",                // props
  params: {},               // data参数
  cycles: "",               // 生命周期函数
  methods: [],              // 方法(:)
  style: "",                // 页面样式
  events: [],               // 事件(@)
  _props: null              // props对象(校验用)
}

/**
 * 单个组件解析入口
 * @param {*} config
 */
function parseComponent(config){
  let type = config.type.toUpperCase().replaceAll("-", "_");
  template += TEMP_CODE[`${type}_CODE`](config, data)
}

/**
 * 初始化
 * @param {*} data
 */
function initTemplate(pageList){
  console.log('pageList: ', pageList);
  if(pageList && pageList.length > 0){
    pageList.forEach(v => {
      // 解析页面
      if(v.type == "home"){
        // props
        if(v.props){
          data.props = "props: " + v.props + ",";
          data._props = eval("(" + v.props + ")");
        }

        // 参数
        if(v.data){
          data.params = eval("(" + v.data + ")");
        }

        // 生命周期函数
        if(v.cycles && v.cycles.length > 0){
          let cycles = [];
          v.cycles.forEach(cycle => {
            cycles.push(cycle.body);
          });
          if(cycles && cycles.length > 0){
            data.cycles = cycles.join(",") + ",";
          }
        }

        // 样式
        if(v.css){
          data.style = v.css;
        }

        // 解析页面组件
        if(v.list && v.list.length > 0){
          renderComponent(v)
          // v.list.forEach(l => {
          //   parseComponent(l);
          // });
        }

        console.log("  data  ", data);
      } else {
        parseComponent(v);
      }
    })
  }
}

/**
 * 递归渲染组件
 * @param data
 */
function renderComponent(data) {
  data.list.forEach(component => {
    if(component.type == 'absolute-layout' || component.type == 'base-layout') {
      template += TEMP_CODE.LAYOUT_CODE(component, data);
      renderComponent(component);
      template += `</div>`;
    }
    else {
      parseComponent(component);
    }
  });

}

/**
 * 代码格式化
 * @param {*} rawCode
 * @returns
 */
function prettyCode(rawCode) {
  let options = {};
  let _rawCode = vueBeautify(rawCode, options);
  return _rawCode;
}

/**
 * 初始化参数（每次生成代码前都重置一下）
 */
function initParam(){
  template = "";              // 前端代码
  data = {
    template: [],             // 组件
    props: "",                // props
    params: {},               // data参数
    cycles: "",               // 生命周期函数
    methods: [],              // 方法(:)
    style: "",                // 页面样式
    events: [],               // 事件(@)
    _props: null              // props对象(校验用)
  }
}

/**
 * data参数格式化
 * @param {*} obj
 * @returns
 */
function paramsFormat(obj){
  let _params = "";
  Object.keys(obj).forEach(key => {
    _params += key + ":"
    if(!obj[key]){
      _params += null;
    }else if (typeof obj[key] == "string"){
      _params += ("'" + obj[key] +"'")
    } else if (typeof obj[key] == "number"){
      _params += obj[key]
    } else if (typeof obj[key] == "object"){
      if(Array.isArray(obj[key])){
        _params += (JSON.stringify(obj[key]))
      } else {
        _params += ("{" + paramsFormat(obj[key]) + "}")
      }
    }
    _params += ","
  })
  return _params;
}


export default function (pageList) {
  initParam();
  initTemplate(pageList);

  console.log(" data._props ", data._props);

  let code =
    `<template>
      <div>
        ${template}
      </div>
    </template>
    <script>
    export default {
      ${data.props}
      data () {
        return {
          ${paramsFormat(data.params)}
        }
      },
      ${data.cycles}
      methods: {
        ${data.methods.join(",")}
      }
    }
    </script>
    <style>
      ${data.style}
    </style>`
  return prettyCode(code);
}
