import Cookies from 'js-cookie'

const sessionStorage = window.sessionStorage;
export class Auth {
  static getEditableTabs() {
    return sessionStorage.getItem('user.editableTabs')
  }


  static setEditableTabs(value = []) {
    return sessionStorage.setItem('user.editableTabs', JSON.stringify(value))
  }

  static removeEditableTabs() {
    return sessionStorage.removeItem('user.editableTabs')
  }

  static getEditableTabsValue() {
    return sessionStorage.getItem('user.editableTabsValue')
  }

  static setEditableTabsValue(value) {
    return sessionStorage.setItem('user.editableTabsValue', value)
  }

  static removeEditableTabsValue() {
    return sessionStorage.removeItem('user.editableTabsValue')
  }
}

