let reportRegisterPlugin = {};

import ReportQueryForm from "@/pages/report/develop/component/reportQueryForm";

const components = [
  ReportQueryForm,
];

reportRegisterPlugin.install = function (Vue, options) {
  //注册全局组件
  components.forEach(component => {
    Vue.component(component.name, component);
  });

}

export default reportRegisterPlugin;
