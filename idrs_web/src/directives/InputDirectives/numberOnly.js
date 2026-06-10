export default {
  inserted: function (el, binding, vnode) {
    el.addEventListener('keypress', function (e) {
      e = e || window.event
      let charCode = typeof e.charCode === 'number' ? e.charCode : e.keyCode
      let reg = /\d/
      if (!reg.test(String.fromCharCode(charCode)) && charCode > 9 && !e.ctrlKey) {
        if (e.preventDefault) {
          e.preventDefault()
        } else {
          e.returnValue = false
        }
      }
    })
  }
}
