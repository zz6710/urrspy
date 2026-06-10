import floatOnly from './floatOnly'

const install = function (Vue) {
  Vue.directive('floatOnly')
}

if (window.vue) {
  window.floatOnly = floatOnly
  Vue.use(install)
}

floatOnly.install = install
export default floatOnly
