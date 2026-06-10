// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

import VueMaterial from "vue-material";

import App from './App'
import Icon from 'vue2-svg-icon/Icon.vue'
import kayak from '@/frame/kayak.js'
import global from '@/frame/global';
import '@/frame/utils.js';

import Chartist from "chartist";

import VueQuillEditor from 'vue-quill-editor'
// require styles 引入样式
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'
import '@/directives/dialog'
import store from '@/store'


import  * as echarts from 'echarts'

import "vue-material/dist/vue-material.min.css";

import kayakPlugin from "@/frame/kayakPlugin.js"

import router from './router'
import FormItemDiffMixin from "@/mixins/formItemDiffMixin.js"


Vue.use(VueQuillEditor);
Vue.use(ElementUI, {size: 'small'});
Vue.use(kayakPlugin);
Vue.use(router);

Vue.use(VueMaterial);
Vue.mixin(FormItemDiffMixin)



Vue.config.productionTip = false;
Vue.component('icon', Icon);
Vue.prototype.global=global;

Vue.prototype.$echarts = echarts;
Vue.prototype.$Chartist = Chartist;

Vue.directive('click-outside', {
  bind: function (el, binding, vnode) {
    el.clickOutsideEvent = function (event) {
      if (!(el == event.target || el.contains(event.target))) {
        if(vnode.context.hasOwnProperty("doBlur")){
          vnode.context.doBlur()
        }
      }
    }
    document.body.addEventListener('click', el.clickOutsideEvent)
  },
  unbind: function (el) {
    document.body.removeEventListener('click', el.clickOutsideEvent)
  }
})

router.beforeEach((to, from, next) => {
  if (to.meta.requireLogin) {
    // next()
    kayak.getLoginStatus().then((result) => {
      if (result.user !== null) {
        window.localStorage.setItem('User', JSON.stringify(result.user));
        next()
      } else {
        next({path: '/login'})
      }
    })
  } else {
    next()
  }
});

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: {App},
  template: '<App/>'
});
import Router from 'vue-router'

const originalPush = Router.prototype.push
Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

Number.prototype.toFixed = function (n) {
  if (n > 20 || n < 0) {
    throw new RangeError('toFixed() digits argument must be between 0 and 20');
  }
  const number = this;
  if (isNaN(number) || number >= Math.pow(10, 21)) {
    return number.toString();
  }
  if (typeof (n) == 'undefined' || n == 0) {
    return (Math.round(number)).toString();
  }

  let result = number.toString();
  const arr = result.split('.');

  // 整数的情况
  if (arr.length < 2) {
    result += '.';
    for (let i = 0; i < n; i += 1) {
      result += '0';
    }
    return result;
  }

  const integer = arr[0];
  const decimal = arr[1];
  if (decimal.length == n) {
    return result;
  }
  if (decimal.length < n) {
    for (let i = 0; i < n - decimal.length; i += 1) {
      result += '0';
    }
    return result;
  }
  result = integer + '.' + decimal.substr(0, n);
  const last = decimal.substr(n, 1);

  // 四舍五入，转换为整数再处理，避免浮点数精度的损失
  if (parseInt(last, 10) >= 5) {
    const x = Math.pow(10, n);
    result = (Math.round((parseFloat(result) * x)) + 1) / x;
    result = result.toFixed(n);
  }

  return result;
};
