import Vue from 'vue'
import Vuex from 'vuex'
import system from './modules/system.js'
import flowTemplate from './modules/flowTemplate.js'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    system,
    flowTemplate
  }
})
