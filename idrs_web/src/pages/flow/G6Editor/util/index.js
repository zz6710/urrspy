import merge from 'lodash/merge'
import pick from 'lodash/pick'
import uniqueId from 'lodash/uniqueId'
import extend from 'lodash/extend'
import upperFirst from 'lodash/upperFirst'
import isArray from 'lodash/isArray'
import isObject from 'lodash/isObject'

const toQueryString = obj => Object.keys(obj).map(key => `${encodeURIComponent(key)}=${encodeURIComponent(obj[key])}`).join('&')

const addListener = (target, eventName, handler) => {
  if (typeof handler === 'function') target.on(eventName, handler)
}

const getBox = (x, y, width, height) => {
  const x1 = (x + width) < x ? (x + width) : x
  const x2 = (x + width) > x ? (x + width) : x
  const y1 = (y + height) < y ? (y + height) : y
  const y2 = (y + height) > y ? (y + height) : y
  return {
    x1, x2, y1, y2
  }
}

const each = function each (elements, func) {
  if (!elements) {
    return
  }
  var rst = void 0
  if (isArray(elements)) {
    for (var i = 0, len = elements.length; i < len; i++) {
      rst = func(elements[i], i)
      if (rst === false) {
        break
      }
    }
  } else if (isObject(elements)) {
    for (var k in elements) {
      if (elements.hasOwnProperty(k)) {
        rst = func(elements[k], k)
        if (rst === false) {
          break
        }
      }
    }
  }
}

export {
  merge,
  pick,
  toQueryString,
  uniqueId,
  upperFirst,
  addListener,
  getBox,
  each,
  extend
}
