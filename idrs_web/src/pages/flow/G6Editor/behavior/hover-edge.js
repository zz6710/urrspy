/* eslint-disable */

import * as Util from '@/utils'
import eventBus from '@/utils/eventBus'
export default {
  getEvents() {
    return {
      'edge:mouseover': 'onMouseover',
      'edge:mouseenter': 'onMouseEnter',
      'edge:mouseleave': 'onMouseleave',
      'edge:click': 'onClick'
    }
  },
  onMouseEnter(e) {
    const self = this
    const item = e.item
    const graph = self.graph
    item.getModel().style.lineWidth = 3
    const source = item.getSource();
    // 该边的结束点
    const target = item.getTarget();
    // 先将边提前，再将端点提前。这样该边两个端点还是在该边上层，较符合常规。
    item.toFront();
    source.toFront();
    target.toFront();
    graph.paint()
  },
  onMouseover(e) {
    const self = this
    const item = e.item
    const graph = self.graph
    if (item.hasState('selected')) {
      return
    } else {
      if (self.shouldUpdate.call(self, e)) {
        graph.setItemState(item, 'hover', true)
      }
    }
    graph.paint()
  },
  onMouseleave(e) {
    const self = this
    const item = e.item
    const graph = self.graph
    const edges = graph.getEdges();
    item.getModel().style.lineWidth = 2
    // 遍历边，将所有边的层级放置在后方，以恢复原样
    edges.forEach(edge => {
      edge.toBack();
    });
    const group = item.getContainer()
    group.find(g => {
      if (g._attrs.isInPoint || g._attrs.isOutPoint) {
        g.attr('fill', '#fff')
      }
    })
    if (self.shouldUpdate.call(self, e)) {
      if (!item.hasState('selected')) { graph.setItemState(item, 'hover', false) }
    }
    graph.paint()
  },
  onClick(e) {
    const self = this
    const item = e.item
    const graph = self.graph
    const autoPaint = graph.get('autoPaint')
    graph.setAutoPaint(false)
    const selectedNodes = graph.findAllByState('node', 'selected')
    Util.each(selectedNodes, node => {
      graph.setItemState(node, 'selected', false)
    })
    if (!self.keydown || !self.multiple) {
      const selected = graph.findAllByState('edge', 'selected')
      Util.each(selected, edge => {
        if (edge !== item) {
          graph.setItemState(edge, 'selected', false)
        }
      })
    }
    if (item.hasState('selected')) {
      if (self.shouldUpdate.call(self, e)) {
        graph.setItemState(item, 'selected', false)
      }
      eventBus.$emit('nodeselectchange', { target: item, select: false })
    } else {
      if (self.shouldUpdate.call(self, e)) {
        graph.setItemState(item, 'selected', true)
      }
      eventBus.$emit('nodeselectchange', { target: item, select: true })
    }
    graph.setAutoPaint(autoPaint)
    graph.paint()
  }

}
