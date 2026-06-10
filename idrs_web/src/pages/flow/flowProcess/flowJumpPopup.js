import Vue from 'vue'
import flowJumpPopup from '@/pages/flow/flowProcess/flowJumpPopup.vue'
const FlowJumpPopupConstructor = Vue.extend(flowJumpPopup)
flowJumpPopup.install = (data) => {
  const instance = new FlowJumpPopupConstructor({
    data
  }).$mount()
  document.body.appendChild(instance.$el)
}
export default flowJumpPopup
