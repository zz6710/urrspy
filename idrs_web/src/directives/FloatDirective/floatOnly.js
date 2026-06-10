export default {
  inserted: function (el, binding, vnode) {
    el.addEventListener('keypress', function (e) {
      e = e || window.event
      let charcode = typeof e.charCode === 'number' ? e.charCode : e.keyCode
      let reg = /\d/
      if (charcode === 46 || charcode === 45) {
        if (vnode.elm.children[0].value.includes('.') || vnode.elm.children[0].value.includes('-')) {
          e.preventDefault()
        }
      } else if (!reg.test(String.fromCharCode(charcode)) && charcode > 9 && !e.ctrlKey && String.fromCharCode(charcode) != '-') {
        if (e.preventDefault) {
          e.preventDefault()
        } else {
          e.returnValue = false
        }
      }
    })
  }
}
