import G6 from '@antv/g6'
import uniqueId from 'lodash/uniqueId'
import editorStyle from './defaultStyle'
import store from '../../../../store/index'

/**
 * x y 相对于 canvas 的偏移量
 */
const startDefaultOptions = {
  icon: null,
  iconStyle: {
    width: 18,
    height: 18,
    left: 6,
    top: 6
  },
  style: {
    ...editorStyle.nodeStyle,
    fill: '#FEF7E8',
    stroke: '#FA8C16',
    cursor: 'default'
  },
  stateStyles: {
    selected: {
      fill: '#FCD49A'
    },
    hover: {
      cursor: editorStyle.cursor.hoverNode
    }
  }
}

const taskDefaultOptions = {
  icon: null,
  iconStyle: {
    width: 12,
    height: 12,
    left: 2,
    top: 2
  },
  style: {
    ...editorStyle.nodeStyle,
    fill: '#E7F7FE',
    stroke: '#1890FF',
    cursor: 'default'
  },
  stateStyles: {
    selected: {
      fill: '#95D6FB'
    },
    hover: {
      cursor: editorStyle.cursor.hoverNode
    }
  }
}

const endDefaultOptions = {
  icon: null,
  iconStyle: {
    width: 18,
    height: 18,
    left: 6,
    top: 6
  },
  style: {
    ...editorStyle.nodeStyle,
    fill: '#EFF7E8',
    stroke: '#F5222D',
    cursor: 'default'
  },
  stateStyles: {
    selected: {
      fill: '#CFD49A'
    },
    hover: {
      cursor: editorStyle.cursor.hoverNode
    }
  }
}

const setColor=function (state) {
    if(store.state.flowTemplate.nodeStatus=="edit"){
      return '#ffffff'
    }
    if(state=="0"){
      // return '#36a0d9'
      return '#d4d9e2'
    }else if(state=="1"){
      return '#d6d92c'
    }else if(state=="2"){
      return '#ffce72'
    }else if(state=="3"){
      return '#54c587'
    }
    else if(state=="4"){
      return '#ff1722'
    }
}

const customNode = {
  init () {
    this.initStartNode()
    this.initEndNode()
    this.initTaskNode()
    this.initCustomNode()
    this.initCustomCircle()
    this.initKRect()
    this.initdd()
  },
  initStartNode () {
    G6.registerNode('start-node', {
      shapeType: 'circle',
      labelPosition: 'bottom',
      options: {
        ...startDefaultOptions
      },
      getShapeStyle (cfg) {
        cfg.size = [30, 30]
        const width = cfg.size[0]
        const style = {
          x: 0,
          y: 0,
          r: width / 2,
          ...this.options.style
        }
        return style
      },
      afterDraw (cfg, group) {
        group.icon = group.addShape('path', {
          attrs: {
            path: [
              ['M', -4, -6],
              ['L', 6, 0],
              ['L', -4, 6],
              ['Z'] // close
            ],
            fill: this.options.style.stroke,
            stroke: this.options.style.stroke
          }
        })
      },
      getAnchorPoints () {
        return [
          [0.5, 0], // top
          [1, 0.5], // right
          [0.5, 1] // bottom
        ]
      }
    }, 'base-node')
  },
  initEndNode () {
    G6.registerNode('end-node', {
      shapeType: 'circle',
      labelPosition: 'bottom',
      options: {
        ...endDefaultOptions
      },
      getShapeStyle (cfg) {
        cfg.size = [30, 30]
        const width = cfg.size[0]
        const style = {
          x: 0,
          y: 0,
          r: width / 2,
          ...this.options.style
        }
        return style
      },
      afterDraw (cfg, group) {
        group.icon = group.addShape('path', {
          attrs: {
            path: [
              ['M', -4, -4],
              ['L', 4, -4],
              ['L', 4, 4],
              ['L', -4, 4],
              ['Z'] // close
            ],
            fill: this.options.style.stroke,
            stroke: this.options.style.stroke
          }
        })
      },
      getAnchorPoints () {
        return [
          [0.5, 0], // top
          [0.5, 1], // bottom
          [0, 0.5] // left
        ]
      }
    }, 'base-node')
  },
  initTaskNode () {
    G6.registerNode('task-node', {
      shapeType: 'rect',
      options: {
        ...taskDefaultOptions
      },
      getShapeStyle (cfg) {
        cfg.size = [80, 44]
        const width = cfg.size[0]
        const height = cfg.size[1]
        const style = {
          x: 0 - width / 2,
          y: 0 - height / 2,
          width,
          height,
          ...this.options.style
        }
        return style
      }
    }, 'base-node')

    G6.registerNode('user-task-node', {
    }, 'rect')
  },
  initCustomNode () {
    G6.registerNode('customNode', {
      getAnchorPoints() {
        return [
          [0, 0.5], // 左侧中间
          [1, 0.5], // 右侧中间
          [0.5, 0], // 上侧中间
          [0.5, 1], // 下侧中间
        ];
      },
      draw (cfg, group) {
        let size = cfg.size
        if (!size) {
          size = [60, 34]
        }
        // 此处必须是NUMBER 不然bbox不正常
        const width = parseInt(size[0])
        const height = parseInt(size[1])
        // 此处必须有偏移 不然drag-node错位
        const offsetX = -width / 2
        const offsetY = -height / 2
        const mainId = 'rect' + uniqueId()
        // 最外层的节点
        const shape = group.addShape('rect', {
          attrs: {
            id: mainId,
            x: offsetX,
            y: offsetY,
            width: width,
            height: height,
            stroke: '#ced4d9',
            fill: '#fff', // 此处必须有fill 不然不能触发事件
            radius: 4,
          }
        })

        // 左边的那条竖线
        /*         group.addShape('rect', {
                  attrs: {
                    x: offsetX,
                    y: offsetY,
                    width: 4,
                    height: height,
                    fill: color,
                    parent: mainId,
                    radius: [4, 0, 0, 4]
                  }
                }) */

        // 矩形框中加一个图片并规定其大小
        let imageWidth = cfg.imageWidth || 20
        let imageHeight = cfg.imageWidth || 16
        group.addShape('image', {
          attrs: {
            x: offsetX + imageWidth / 2.5,
            y: offsetY + imageHeight / 5,
            width: imageWidth,
            height: imageHeight,
            img: cfg.image,
            parent: mainId
          }
        })

        // 如果有 label 属性, 则设置 label 文件内容
        if (cfg.label) {
          group.addShape('text', {
            attrs: {
              id: 'label' + uniqueId(),
              x: offsetX + width / 1.8,
              y: offsetY + height / 2,
              textAlign: 'center',
              textBaseline: 'middle',
              text: '',
              parent: mainId,
              fill: '#565758'
            }
          })
        }

        // 绘制进入小圆圈
        if (cfg.inPoints) {
          for (let i = 0; i < cfg.inPoints.length; i++) {
            let x
            let y = 0
            // 0为顶 1为底 0.5为侧边
            if (cfg.inPoints[i][0] === 0) {
              y = 0
            } else if (cfg.inPoints[i][0] === 0.5) {
              y = height * 0.5
            } else {
              y = height
            }
            x = width * cfg.inPoints[i][1]
            const id = 'circle' + uniqueId()
            group.addShape('circle', {
              attrs: {
                id: 'circle' + uniqueId(),
                parent: id,
                x: x + offsetX/1.05,
                y: y + offsetY/1.05,
                r: 15,
                isInPointOut: true,
                fill: '#1890ff',
                opacity: 0
              }
            })
            group.addShape('circle', {
              attrs: {
                id: id,
                x: x + offsetX,
                y: y + offsetY,
                r: 3,
                isInPoint: true,
                fill: '#fff',
                stroke: '#1890ff',
                opacity: 0
              }
            })
          }
        }

        // 绘制出的小圆圈
        if (cfg.outPoints) {
          for (let i = 0; i < cfg.outPoints.length; i++) {
            let x
            let y = 0
            // 0为顶 1为底 0.5为侧边
            if (cfg.outPoints[i][0] === null) {
              break;
            }
            if (cfg.outPoints[i][0] === 0) {
              y = 0
            } else if (cfg.inPoints[i][0] === 0.5) {
              y = height * 0.5
            } else {
              y = height
            }
            x = width * cfg.outPoints[i][1]
            const id = 'circle' + uniqueId()
            group.addShape('circle', {
              attrs: {
                id: 'circle' + uniqueId(),
                parent: id,
                x: x + offsetX,
                y: y + offsetY,
                r: 10,
                isOutPointOut: true,
                fill: '#1890ff',
                opacity: 0
              }
            })

            group.addShape('circle', {
              attrs: {
                id: id,
                x: x + offsetX,
                y: y + offsetY,
                r: 3,
                isOutPoint: true,
                fill: '#fff',
                stroke: '#1890ff',
                opacity: 0
              }
            })
          }
        }
        // group.sort()
        // 添加文本、更多图形
        return shape
      },
      // 设置状态
      setState (name, value, item) {
        const group = item.getContainer()
        const shape = group.get('children')[0] // 顺序根据 draw 时确定

        const children = group.findAll(g => {
          return g._attrs.parent === shape._attrs.id
        })
        const circles = group.findAll(circle => {
          return circle._attrs.isInPoint || circle._attrs.isOutPoint
        })
        const selectStyles = () => {
          shape.attr('fill', '#f3f9ff')
          shape.attr('stroke', '#6ab7ff')
          shape.attr('cursor', 'move')
          children.forEach(child => {
            child.attr('cursor', 'move')
          })
          circles.forEach(circle => {
            circle.attr('opacity', 1)
          })
        }
        const unSelectStyles = () => {
          shape.attr('fill', '#fff')
          shape.attr('stroke', '#ced4d9')
          circles.forEach(circle => {
            circle.attr('opacity', 0)
          })
        }
        switch (name) {
          case 'selected':
          case 'hover':
            if (value) {
              selectStyles()
            } else {
              unSelectStyles()
            }
            break
        }
      }
    })
  },
  initCustomCircle () {
    G6.registerNode('customCircleNode', {
      draw (cfg, group) {
        let size = cfg.size
        if (!size) {
          size = [170, 34]
        }
        let r = 20

        // 此处必须是NUMBER 不然bbox不正常
        const width = parseInt(size[0])
        const height = parseInt(size[0])
        // 此处必须有偏移 不然drag-node错位
        const offsetX = -width / 4
        const offsetY = -width / 4
        const mainId = 'circle' + uniqueId()
        // 最外层的节点
        const shape = group.addShape('circle', {
          attrs: {
            id: mainId,
            x: 0,
            y: 0,
            r: r,
            stroke: '#ced4d9',
            fill: 'blue', // 此处必须有fill 不然不能触发事件
          }
        })

        // 圆形框中加一个图片并规定其大小
        group.addShape('image', {
          attrs: {
            x: -15,
            y: -13,
            width: 30,
            height: 30,
            img: cfg.image,
            parent: mainId
          }
        })

        // 绘制进入小圆圈
        if (cfg.inPoints) {
          for (let i = 0; i < cfg.inPoints.length; i++) {
            let x
            let y = 0
            // 0为顶 1为底
            if (cfg.inPoints[i][0] === 0) {
              y = 0
            } else {
              y = height
            }
            x = width * cfg.inPoints[i][1]
            const id = 'circle' + uniqueId()
            group.addShape('circle', {
              attrs: {
                id: 'circle' + uniqueId(),
                parent: id,
                x: offsetX,
                y:  offsetY,
                r: 10,
                isInPointOut: true,
                fill: '#1890ff',
                opacity: 0
              }
            })
            group.addShape('circle', {
              attrs: {
                id: id,
                x: offsetX,
                y: offsetY,
                r: 3,
                isInPoint: true,
                fill: '#fff',
                stroke: '#1890ff',
                opacity: 0
              }
            })
          }
        }

        // 绘制出的小圆圈
        if (cfg.outPoints) {
          for (let i = 0; i < cfg.outPoints.length; i++) {
            let x
            let y = 0
            // 0为顶 1为底
            if (cfg.outPoints[i][0] === 0) {
              y = 0
            } else {
              y = height
            }
            x = width * cfg.outPoints[i][1]
            const id = 'circle' + uniqueId()
            group.addShape('circle', {
              attrs: {
                id: 'circle' + uniqueId(),
                parent: id,
                x: x + offsetX,
                y: y + offsetY,
                r: 10,
                isOutPointOut: true,
                fill: '#1890ff',
                opacity: 0
              }
            })

            group.addShape('circle', {
              attrs: {
                id: id,
                x: x + offsetX,
                y: y + offsetY,
                r: 3,
                isOutPoint: true,
                fill: '#fff',
                stroke: '#1890ff',
                opacity: 0
              }
            })
          }
        }
        // group.sort()
        // 添加文本、更多图形
        return shape
      },
      // 设置状态
      setState (name, value, item) {
        const group = item.getContainer()
        const shape = group.get('children')[0] // 顺序根据 draw 时确定

        const children = group.findAll(g => {
          return g._attrs.parent === shape._attrs.id
        })
        const circles = group.findAll(circle => {
          return circle._attrs.isInPoint || circle._attrs.isOutPoint
        })
        const selectStyles = () => {
          shape.attr('fill', '#f3f9ff')
          shape.attr('stroke', '#6ab7ff')
          shape.attr('cursor', 'move')
          children.forEach(child => {
            child.attr('cursor', 'move')
          })
          circles.forEach(circle => {
            circle.attr('opacity', 1)
          })
        }
        const unSelectStyles = () => {
          shape.attr('fill', '#fff')
          shape.attr('stroke', '#ced4d9')
          circles.forEach(circle => {
            circle.attr('opacity', 0)
          })
        }
        switch (name) {
          case 'selected':
          case 'hover':
            if (value) {
              selectStyles()
            } else {
              unSelectStyles()
            }
            break
        }
      }
    }, 'circle')
  },
  initKRect(){
    G6.registerNode('k-Rect', {
      getAnchorPoints() {
        return [
          [0, 0.5], // 左侧中间
          [1, 0.5], // 右侧中间
          [0.5, 0], // 上侧中间
          [0.5, 1], // 下侧中间
        ];
      },
      draw (cfg, group) {
        let size = cfg.size
        if (!size) {
          size = [60, 34]
        }
        // 此处必须是NUMBER 不然bbox不正常
        const width = parseInt(size[0])
        const height = parseInt(size[1])
        // 此处必须有偏移 不然drag-node错位
        const offsetX = -width / 2
        const offsetY = -height / 2
        const mainId = 'rect' + uniqueId()
        // 最外层的节点
        const shape = group.addShape('rect', {
          attrs: {
            id: mainId,
            x: offsetX,
            y: offsetY,
            width: width,
            height: height,
            stroke: '#ced4d9',
            fill:store.state.flowTemplate.nodeStatus=="edit"?"#ffffff":"#5c92ea", // 此处必须有fill 不然不能触发事件
            radius: 4,
          }
        })

        // 如果有 label 属性, 则设置 label 文件内容
        if (cfg.label) {
          group.addShape('text', {
            attrs: {
              id: 'label' + uniqueId(),
              x: 0,
              y: 0,
              textAlign: 'center',
              textBaseline: 'middle',
              text: cfg.attrs.displayName,
              parent: mainId,
              fill: '#565758'
            }
          })
        }

        // 绘制进入小圆圈
        if (cfg.inPoints) {
          for (let i = 0; i < cfg.inPoints.length; i++) {
            let x
            let y = 0
            // 0为顶 1为底 0.5为侧边
            if (cfg.inPoints[i][0] === 0) {
              y = 0
            } else if (cfg.inPoints[i][0] === 0.5) {
              y = height * 0.5
            } else {
              y = height
            }
            x = width * cfg.inPoints[i][1]
            const id = 'circle' + uniqueId()
            group.addShape('circle', {
              attrs: {
                id: 'circle' + uniqueId(),
                parent: id,
                x: x + offsetX/1.05,
                y: y + offsetY/1.05,
                r: 15,
                isInPointOut: true,
                fill: '#1890ff',
                opacity: 0
              }
            })
            group.addShape('circle', {
              attrs: {
                id: id,
                x: x + offsetX,
                y: y + offsetY,
                r: 3,
                isInPoint: true,
                fill: '#fff',
                stroke: '#1890ff',
                opacity: 0
              }
            })
          }
        }

        // 绘制出的小圆圈
        if (cfg.outPoints) {
          for (let i = 0; i < cfg.outPoints.length; i++) {
            let x
            let y = 0
            // 0为顶 1为底 0.5为侧边
            if (cfg.outPoints[i][0] === null) {
              break;
            }
            if (cfg.outPoints[i][0] === 0) {
              y = 0
            } else if (cfg.inPoints[i][0] === 0.5) {
              y = height * 0.5
            } else {
              y = height
            }
            x = width * cfg.outPoints[i][1]
            const id = 'circle' + uniqueId()
            group.addShape('circle', {
              attrs: {
                id: 'circle' + uniqueId(),
                parent: id,
                x: x + offsetX,
                y: y + offsetY,
                r: 10,
                isOutPointOut: true,
                fill: '#1890ff',
                opacity: 0
              }
            })

            group.addShape('circle', {
              attrs: {
                id: id,
                x: x + offsetX,
                y: y + offsetY,
                r: 3,
                isOutPoint: true,
                fill: '#fff',
                stroke: '#1890ff',
                opacity: 0
              }
            })
          }
        }
        // group.sort()
        // 添加文本、更多图形
        return shape
      },
      // 设置状态
      setState (name, value, item) {
        const group = item.getContainer()
        const shape = group.get('children')[0] // 顺序根据 draw 时确定

        const children = group.findAll(g => {
          return g._attrs.parent === shape._attrs.id
        })
        const circles = group.findAll(circle => {
          return circle._attrs.isInPoint || circle._attrs.isOutPoint
        })
        const selectStyles = () => {
          shape.attr('fill', '#f3f9ff')
          shape.attr('stroke', '#6ab7ff')
          shape.attr('cursor', 'move')
          children.forEach(child => {
            child.attr('cursor', 'move')
          })
          circles.forEach(circle => {
            circle.attr('opacity', 1)
          })
        }
        const unSelectStyles = () => {
          shape.attr('fill', '#fff')
          shape.attr('stroke', '#ced4d9')
          circles.forEach(circle => {
            circle.attr('opacity', 0)
          })
        }
        if(store.state.flowTemplate.nodeStatus=="edit"){
          switch (name) {
            case 'selected':
            case 'hover':
              if (value) {
                selectStyles()
              } else {
                unSelectStyles()
              }
              break
          }
        }
      }
    })
  },
  initdd(){
    G6.registerNode('dd', {
      getAnchorPoints() {
        return [
          [0, 0.5], // 左侧中间
          [1, 0.5], // 右侧中间
          [0.5, 0], // 上侧中间
          [0.5, 1], // 下侧中间
        ];
      },
      draw (cfg, group) {
        let size = cfg.size
        size=[60,25]
        if (!size) {
          size = [60, 34]
        }
        // 此处必须是NUMBER 不然bbox不正常
        const width = parseInt(size[0])
        const height = parseInt(size[1])
        // 此处必须有偏移 不然drag-node错位
        const offsetX = -width / 2
        const offsetY = -height / 2
        const mainId = 'rect' + uniqueId()
        // 最外层的节点
        const shape = group.addShape('rect', {
          attrs: {
            id: mainId,
            x: offsetX,
            y: offsetY,
            width: width,
            height: height,
            // stroke: '#ced4d9',
            fill:setColor(cfg.state?cfg.state:"0"), // 此处必须有fill 不然不能触发事件
            radius: 4,
          }
        })


        // 如果有 label 属性, 则设置 label 文件内容
        if (cfg.label) {
          group.addShape('text', {
            attrs: {
              id: 'label' + uniqueId(),
              x: 0,
              y: 0,
              textAlign: 'center',
              textBaseline: 'middle',
              text: cfg.attrs.displayName,
              parent: mainId,
              fill: '#565758'
            }
          })
        }

        // 绘制进入小圆圈
        if (cfg.inPoints) {
          for (let i = 0; i < cfg.inPoints.length; i++) {
            let x
            let y = 0
            // 0为顶 1为底 0.5为侧边
            if (cfg.inPoints[i][0] === 0) {
              y = 0
            } else if (cfg.inPoints[i][0] === 0.5) {
              y = height * 0.5
            } else {
              y = height
            }
            x = width * cfg.inPoints[i][1]
            const id = 'circle' + uniqueId()
            group.addShape('circle', {
              attrs: {
                id: 'circle' + uniqueId(),
                parent: id,
                x: x + offsetX/1.05,
                y: y + offsetY/1.05,
                r: 15,
                isInPointOut: true,
                fill: '#1890ff',
                opacity: 0
              }
            })
            group.addShape('circle', {
              attrs: {
                id: id,
                x: x + offsetX,
                y: y + offsetY,
                r: 3,
                isInPoint: true,
                fill: '#fff',
                stroke: '#1890ff',
                opacity: 0
              }
            })
          }
        }

        // 绘制出的小圆圈
        if (cfg.outPoints) {
          for (let i = 0; i < cfg.outPoints.length; i++) {
            let x
            let y = 0
            // 0为顶 1为底 0.5为侧边
            if (cfg.outPoints[i][0] === null) {
              break;
            }
            if (cfg.outPoints[i][0] === 0) {
              y = 0
            } else if (cfg.inPoints[i][0] === 0.5) {
              y = height * 0.5
            } else {
              y = height
            }
            x = width * cfg.outPoints[i][1]
            const id = 'circle' + uniqueId()
            group.addShape('circle', {
              attrs: {
                id: 'circle' + uniqueId(),
                parent: id,
                x: x + offsetX,
                y: y + offsetY,
                r: 10,
                isOutPointOut: true,
                fill: '#1890ff',
                opacity: 0
              }
            })

            group.addShape('circle', {
              attrs: {
                id: id,
                x: x + offsetX,
                y: y + offsetY,
                r: 3,
                isOutPoint: true,
                fill: '#fff',
                stroke: '#1890ff',
                opacity: 0
              }
            })
          }
        }
        // group.sort()
        // 添加文本、更多图形
        return shape
      },
      // 设置状态
      setState (name, value, item) {
        const group = item.getContainer()
        const shape = group.get('children')[0] // 顺序根据 draw 时确定

        const children = group.findAll(g => {
          return g._attrs.parent === shape._attrs.id
        })
        const circles = group.findAll(circle => {
          return circle._attrs.isInPoint || circle._attrs.isOutPoint
        })
        const selectStyles = () => {
          shape.attr('fill', '#f3f9ff')
          shape.attr('stroke', '#6ab7ff')
          shape.attr('cursor', 'move')
          children.forEach(child => {
            child.attr('cursor', 'move')
          })
          circles.forEach(circle => {
            circle.attr('opacity', 1)
          })
        }
        const unSelectStyles = () => {
          shape.attr('fill', '#fff')
          shape.attr('stroke', '#ced4d9')
          circles.forEach(circle => {
            circle.attr('opacity', 0)
          })
        }
        if(store.state.flowTemplate.nodeStatus=="edit"){
          switch (name) {
            case 'selected':
            case 'hover':
              if (value) {
                selectStyles()
              } else {
                unSelectStyles()
              }
              break
          }
        }
      }
    })
  }
}

export default customNode
