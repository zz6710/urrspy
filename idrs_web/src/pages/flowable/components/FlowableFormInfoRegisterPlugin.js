let flowableFormInfoRegisterPlugin = {};

import batchDownload from "@/pages/pms/disclosureNotice/BatchDownload.vue";
import confirmSourceDataFlow from "@/pages/pms/sourceDataChgInfo/ConfirmSourceDataFlow.vue";

const components = [
  batchDownload,
  confirmSourceDataFlow,
];


flowableFormInfoRegisterPlugin.install = function (Vue, options) {
  //注册全局组件
  components.forEach(component => {
    Vue.component(component.name, component);
  });

}

export default flowableFormInfoRegisterPlugin;
