import kayak from './kayak'
import Vue from 'vue'

String.prototype.trim = function () {
  return this.replace(/(^\s*)|(\s*$)/g, '')
};

String.prototype.toDateTimeString = function () {
  if (this != null) {
    return (
      this.substring(0, 4) + '-' + this.substring(4, 6) + '-' + this.substring(6, 8) + ' ' + this.substring(8, 10) + ':' + this.substring(10, 12) + ':' + this.substring(12, 14)
    )
  } else { return '' }
};

String.prototype.toDateString = function () {
  if (this != null) { return this.substring(0, 4) + '-' + this.substring(4, 6) + '-' + this.substring(6, 8) } else { return '' }
};

String.prototype.toTimeString = function () {
  if (this != null) { return this.substring(0, 2) + ':' + this.substring(2, 4) + ':' + this.substring(4, 6) } else { return '' }
};

String.prototype.toMoneyString = function () {
  if (this != null) { return parseFloat(this.toString()).toFixed(2).toString() } else return 0.00
};

// 数字每三位隔开
String.prototype.toNumberString = function () {
  if (this != null) { return this.replace(/\s/g, '').replace(/[^\d]/g, '').replace(/(\d{3})(?=\d)/g, '$1 ') } else { return 0 }
};

// 数字每三位不隔开（入库）
String.prototype.backToNumberString = function () {
  return this.replace(/\s+/g, '')
};

Date.prototype.Format = function (fmt) {
  if (fmt == undefined || fmt == null || fmt == '') { return '' }
  var o = {
    'M+': this.getMonth() + 1, // 月份
    'd+': this.getDate(), // 日
    'h+': this.getHours(), // 小时
    'm+': this.getMinutes(), // 分
    's+': this.getSeconds(), // 秒
    'q+': Math.floor((this.getMonth() + 3) / 3), // 季度
    'S': this.getMilliseconds() // 毫秒
  };
  if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
  for (var k in o) { if (new RegExp('(' + k + ')').test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length))) }
  return fmt
};

function getCookie (name) {
  name = name + '=';
  let ca = document.cookie.split(/[ ]*;[ ]*/);
  for (let i = 0; i < ca.length; i++) {
    let c = ca[i];
    if (c.indexOf(name) != -1) return c.substring(name.length, c.length)
  }
  return ''
}

export function getDicts (type) {
  let action = {
    dict: {
      itemKey: '',
      itemValue: '',
      parent: ''
    },
    offset: 0,
    limit: 10000
  };
  action.dict.itemKey = type;
  return new Promise((resolve) => {
    kayak.rpc('pub.dict.ListSubDicts', action).then(result => {
      resolve(result.list.data)
    })
  })
}

export default {
  getCookie
}
