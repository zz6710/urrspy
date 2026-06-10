import httpUtil from './httpUtil.js';
import tools from '@/utils/tools';
import global from './global.js';

import "../assets/scss/material-dashboard.scss";

import formInfoRegisterPlugin from "../pages/flow/formInfo/formInfoRegisterPlugin";
import flowableFormInfoRegisterPlugin from "../pages/flowable/components/FlowableFormInfoRegisterPlugin";
import reportRegisterPlugin from "../pages/report/develop/component/reportRegisterPlugin";

import KFieldSelect from '@/components/k-element/k-field-select/k-field-select.vue';
import KFieldGrid from '@/components/k-element/k-field-select/grid/k-field-grid.vue';
import kFieldDate from '@/components/k-element/k-field-date/k-field-date.vue';
import kFieldBswitch from '@/components/k-element/k-field-bswitch/k-field-bswitch.vue';
import KFieldRadio from '@/components/k-element/k-field-radio/k-field-radio.vue';
import KFieldCheckBox from '@/components/k-element/k-field-checkbox/k-field-checkbox.vue';
import KBtn from '@/components/k-element/k-btn/k-btn.vue';
import KFieldCode from '@/components/k-element/k-field-code/k-field-code.vue';
import KFieldTime from '@/components/k-element/k-field-time/k-field-time.vue';
import KPopup from '@/components/k-element/k-popup/k-popup.vue';
import KSteps from '@/components/k-element/k-steps/k-steps.vue';
import KStep from '@/components/k-element/k-steps/k-step.vue';
import KForm from '@/components/k-element/k-from/k-form.vue';
import KFormItem from '@/components/k-element/k-from/k-form-item.vue';
import KFormFooter from '@/components/k-element/k-from/k-form-footer.vue';
import KFieldText from '@/components/k-element/k-field-text/k-field-text.vue';
import KFieldCascader from '@/components/k-element/k-field-cascader/k-field-cascader.vue';
import KFieldRich from '@/components/k-element/k-field-rich/k-field-rich.vue';
import KGrid from '@/components/k-element/k-grid/k-grid1.vue';
import KGridColumn from '@/components/k-element/k-grid/k-grid-column.vue';
import KFormSearch from '@/components/k-element/k-form-search/k-form-search.vue'
import KFormCustomize from '@/components/k-element/k-form-search/k-form-customize.vue'
import kFormSearchCustomize from '@/components/k-element/k-form-search/k-form-search-customize.vue'
import KFieldUpload from '@/components/k-element/k-field-upload/k-field-upload.vue'
import KFieldExcelUpload from '@/components/k-element/k-field-excel-upload/k-field-excel-upload.vue'
import KSearchPanel from '@/components/k-element/k-search-panel/k-search-panel.vue'
import KTree from '@/components/k-element/k-tree/k-tree.vue'
import KFieldTree from '@/components/k-element/k-field-tree/k-field-tree.vue'
import KApiRpanel from '@/components/k-element/k-api-rpanel/k-api-rpanel.vue'
import KFlowUpload from '@/components/k-flow/k-flow-upload/k-flow-upload.vue'
import KFlowForm from '@/components/k-flow/k-flow-form/k-flow-form.vue'
import KFlowGrid from '@/components/k-flow/k-flow-grid/k-flow-grid.vue'
import KFlowFormInfo from '@/components/k-flow/k-flow-formInfo/k-flow-formInfo.vue'
import KFlowModifyInformation from '@/components/k-flow/k-flow-modify-information/k-flow-modify-information.vue'
import KIcon from '@/components/k-element/k-icon/k-icon.vue'
import KReport from '@/components/k-element/k-report/k-report'
import KTooltip from '@/components/k-element/k-tooltip/k-tooltip'

import Sidebar from '@/components/k-material/SidebarPlugin'
import Notifications from '@/components/k-material/NotificationPlugin'
import DropDown from '@/components/k-material/Dropdown.vue'

import {
  CollapseTransition,
  SlideYDownTransition,
  FadeTransition,
  ZoomCenterTransition
} from "../../node_modules/vue2-transitions";

import {
  directive as vClickOutside
} from "../../node_modules/vue-clickaway";

import KFieldDisplay from '@/components/k-element/k-field-display/k-field-display.vue'
import KGridDisplay from '@/components/k-element/k-grid/k-grid-display.vue'
import user from '@/pages/system/user.vue'
import dept from '@/pages/system/dept'
import dict from '@/pages/system/dict'
let kayakPlugin = {};

const components = [
  KFieldSelect,
  KFieldGrid,
  kFieldDate,
  kFieldBswitch,
  KBtn,
  KFieldCode,
  KFieldTime,
  KPopup,
  KSteps,
  KStep,
  KForm,
  KFormItem,
  KFormFooter,
  KFieldRadio,
  KFieldCheckBox,
  KFieldText,
  KFieldCascader,
  KFieldRich,
  KGrid,
  KGridColumn,
  KFieldUpload,
  KFieldExcelUpload,
  KSearchPanel,
  KFormCustomize,
  KFormSearch,
  kFormSearchCustomize,
  KSearchPanel,
  KTree,
  KFieldTree,
  KApiRpanel,
  KFieldDisplay,
  KGridDisplay,
  KFlowUpload,
  KFlowForm,
  KFlowGrid,
  KFlowFormInfo,
  KFlowModifyInformation,
  KIcon,
  user,
  dept,
  dict,
  KReport,
  KTooltip
];


kayakPlugin.install = function(Vue, options) {

  //注册全局组件
  components.forEach(component => {
    Vue.component(component.name, component);
  });

  //全局实例
  Vue.prototype.httpUtil = httpUtil;
  Vue.prototype.tools = tools;
  Vue.prototype.global = global;

  //安装实例
  Vue.use(flowableFormInfoRegisterPlugin);
  Vue.use(Sidebar);
  Vue.use(formInfoRegisterPlugin);
  Vue.use(reportRegisterPlugin);
  Vue.use(Notifications);
  Vue.component("drop-down", DropDown);
  Vue.component("CollapseTransition", CollapseTransition);
  Vue.component("SlideYDownTransition", SlideYDownTransition);
  Vue.component("FadeTransition", FadeTransition);
  Vue.component("ZoomCenterTransition", ZoomCenterTransition);

  //全局指令
  Vue.directive("click-outside", vClickOutside);
}


export default kayakPlugin;
