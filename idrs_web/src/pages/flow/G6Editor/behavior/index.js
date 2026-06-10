import G6 from '@antv/g6';
import addLine from './add-edge';
import addMenu from './add-menu';
import dragItem from './drag-item';
import hoverEdge from './hover-edge';
import hoverNode from './hover-node';
import keyboard from './keyboard';
import mulitSelect from './mulit-select';
import selectNode from './select-node';

const behavors = {
  'hover-node': hoverNode,
  'add-edge': addLine,
  'drag-item': dragItem,
  'select-node': selectNode,
  'hover-edge': hoverEdge,
  'keyboard': keyboard,
  'mulit-select': mulitSelect,
  'add-menu': addMenu
}

export function initBehavors () {
  for (let key in behavors) {
    G6.registerBehavior(key, behavors[key])
  }
}
