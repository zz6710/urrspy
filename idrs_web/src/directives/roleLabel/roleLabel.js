import kayak from '@/frame/kayak.js'

export default{
  bind (el, binding, vnode) {
    var roleList = JSON.parse(window.localStorage.getItem('Roles'))
    roleList.forEach((item, index) => {
      if (item.id == binding.value) {
        el.innerHTML = item.name
      }
    })
    // el.innerHTML = binding.value + window.localStorage.getItem("key")
    //  window.localStorage.getItem("key")
  }
}
