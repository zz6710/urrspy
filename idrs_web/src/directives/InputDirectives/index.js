import numberOnly from './numberOnly'

const install = function (Vue) {
  Vue.directive('numberOnly')
}

if (window.Vue) {
  window.numberOnly = numberOnly
  Vue.use(install)
}

numberOnly.install = install
export default numberOnly
