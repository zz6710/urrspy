const state = () => ({
  nodeStatus: "no-edit"
})

// getters
const getters = {}

// actions
const actions = {}

// mutations
const mutations = {
  setNodeStatus(state, status) {
    state.nodeStatus = status
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
