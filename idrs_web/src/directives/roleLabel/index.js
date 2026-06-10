import roleLabel from './roleLabel'

const install = function (Vue) {
  Vue.directive('roleLabel', roleLabel)
}

if (window.Vue) {
  window.roleLabel = roleLabel
  Vue.use(install); // eslint-disable-line
}

roleLabel.install = install
export default roleLabel
