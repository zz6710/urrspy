<template>
  <div>
    组件属性:
    <k-field-code>
      <textarea>
        data-allowblank：true/false，是否允许为空，必须用,只针对于cascader有效
        data-empty-text: 内容为空的时候展示的文本
        data-node-key: 每个树节点用来作为唯一标识的属性，整棵树应该是唯一的
        data-render-after-expand: 是否在第一次展开某个树节点后才渲染其子节点
        data-load: 加载子树数据的方法，仅当 lazy 属性为true 时生效 function(node, resolve)
        data-render-content:树节点的内容区的渲染 Function(h, { node, data, store }
        data-highlight-current:是否高亮当前选中节点，默认值是 false。
        data-default-expand-all: 是否默认展开所有节点
        data-expand-on-click-node: 是否在点击节点的时候展开或者收缩节点， 默认值为 true，如果为 false，则只有点箭头图标的时候才会展开或者收缩节点。
        data-check-on-click-node: 是否在点击节点的时候选中节点，默认值为 false，即只有在点击复选框时才会选中节点。
        data-auto-expand-parent: 展开子节点的时候是否自动展开父节点
        data-default-expanded-keys: 默认展开的节点的 key 的数组
        data-show-checkbox: 节点是否可被选择
        data-check-strictly: 在显示复选框的情况下，是否严格的遵循父子不互相关联的做法，默认为 false
        data-default-checked-keys	: 默认勾选的节点的 key 的数组
        data-current-node-key: 当前选中的节点
        data-filter-node-method: 对树节点进行筛选时执行的方法，返回 true 表示这个节点可以显示，返回 false 则表示这个节点会被隐藏Function(value, data, node)
        data-accordion: 是否每次只打开一个同级树节点展开
        data-indent: 相邻级节点间的水平缩进，单位为像素
        data-icon-class: 自定义树节点的图标
        data-lazy: 是否懒加载子节点，需与 load 方法结合使用
        data-draggable:是否开启拖拽节点功能 Function(node)
        data-allow-drop:拖拽时判定目标节点能否被放置。type 参数有三种情况：'prev'、'inner' 和 'next'，分别表示放置在目标节点前、插入至目标节点和放置在目标节点后 Function(draggingNode, dropNode, type)


        data-data: 内置自定义数据,默认取label,children,如果设置data-value-field，data-display-field等则取自定义的数据格式,如果定义了则会按照规则
        data-params：指定固定的查询参数值
        data-action：指定数据来源action
        data-graphql：指定数据来源graphql


        data-value-field：指定查询数据中作为值的字段名称,tree全局唯一的属性
        data-display-field：指定查询数据中作为文本显示的字段名称，可以指定多个字段名（以半角逗号分隔），那么显示的时候就以data-display-separator分隔符串起来
        data-display-separator：指定显示多个字段值的时候字段值之间的分隔符,，默认为“-”
        data-value-separator: 多层时候的显示分隔符
        data-display-child: 指定子节点定义的字段名称,用于解析数据
        data-diffcondition:一次性加载时候的上下级加载关系
        data-value-disabled: 指定结果中是否可以选择,当为true,'true','disabled',则当前项不能进行选择,当checkbox时候有效
        data-on-object: 如果配置则data-on-change触发返回的是对象,否则返回当前选中的vulue值

        ps:
        配置不出现任何描述标签，则使用默认属性解析数据
        配置出现data-value-field,data-display-field,data-display-child用于自定义数据
        配置出现data-display-field，data-value-field，data-parent-field + data-lazy,用于懒加载
        配置出现data-value-field,data-display-child,data-diffcondition，用于一次性加载
        ------事件------
        data-on-beforeload：指定加载数据之前触发调用的JS函数，切记初始化data-params
        data-on-afterload：指定加载数据之后触发调用的JS函数
        data-value-method: 自定义指定结果选择条件，当满足条件时则屏蔽选择条件,传入一个是row对象，必须返回true才能条件成立
        data-node-click: 节点被点击时的回调
        data-node-contextmenu:当某一节点被鼠标右键点击时会触发该事件
        data-check-change:节点选中状态发生变化时的回调
        data-check:当复选框被点击的时候触发
        data-current-change:当前选中节点变化时触发的事件
        data-node-expand:节点被展开时触发的事件
        data-node-collapse:节点被关闭时触发的事件
        data-node-drag-start:节点开始拖拽时触发的事件
        data-node-drag-enter:拖拽进入其他节点时触发的事件
        data-node-drag-leave:拖拽离开某个节点时触发的事件
        data-node-drag-over:在拖拽节点时触发的事件（类似浏览器的 mouseover 事件）
        data-node-drag-end:拖拽结束时（可能未成功）触发的事件
        data-node-drop:拖拽成功完成时触发的事件

      </textarea>
    </k-field-code>
    <br>

    <k-field-tree data-show-checkbox :data-data="options" @data-on-check="handlerDataCheck"></k-field-tree>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        options: [{
          id:'1',
          label: '一级 1',
          children: [{
            id:'2',
            label: '二级 1-1',
            children: [{
              id:'3',
              label: '三级 1-1-1'
            }]
          }]
        }, {
          id:'4',
          label: '一级 2',
          children: [{
            id:'5',
            label: '二级 2-1',
            children: [{
              id:'6',
              label: '三级 2-1-1'
            }]
          }, {
            id:'7',
            label: '二级 2-2',
            children: [{
              label: '三级 2-2-1'
            }]
          }]
        }, {
          id:'8',
          label: '一级 3',
          children: [{
            id:'9',
            label: '二级 3-1',
            children: [{
              label: '三级 3-1-1'
            }]
          }, {
            id:'10',
            label: '二级 3-2',
            children: [{
              id:'11',
              label: '三级 3-2-1'
            }]
          }]
        }],
      }
    },
    methods: {
      handlerDataCheck(data,node){
        console.log(data)
      }
    }
  }
</script>

<style scoped>

</style>
