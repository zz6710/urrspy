<template>
  <div>
    组件属性:
    <k-field-code>
      <textarea>
        data-allowblank：true/false，是否允许为空，必须用,只针对于cascader有效
        data-placeholder: 用于显示基本文本内容框,只针对于cascader有效
        data-ui-type：选择样式，cascader或者panle，默认值是cascader
        data-size: 组件大小,默认是mini，可选 medium / small / mini ,只针对于cascader有效
        data-disabled: true/false，输入控件是否不可用,只针对于cascader有效
        data-clearable: 是否有清除按钮,只针对于cascader有效
        data-show-all-levels : 默认为true，如果设置为false则只显示最后一级,只针对于cascader有效
        data-collapse-tags: 多选模式下是否折叠Tag,只针对于cascader有效

        data-fileterable: 是否可搜索选项，默认为false,只针对于cascader有效
        data-filter-method: 自定义搜索逻辑，第一个参数是节点node，第二个参数是搜索关键词keyword，通过返回布尔值表示是否命中function(node, keyword),只针对于cascader有效
        data-debounce: 搜索关键词输入的去抖延迟，毫秒，默认300,只针对于cascader有效
        data-before-filter: 筛选之前的钩子，参数为输入的值，若返回 false 或者返回 Promise 且被 reject，则停止筛选 function(value)
        data-popper-class : 自定义浮层类名,只针对于cascader有效
        data-show-num: 是否显示下标记编号
        data-expand-trigger: 次级菜单的展开方式，默认是click，可选hover鼠标停留
        data-multiple: 是否允许多选,如果是多选，则v-model是数组
        data-check-strictly: 是否严格的遵守父子节点不互相关联,默认是false
        data-emit-path: 在选中节点改变时，是否返回由该节点所在的各级菜单的值所组成的数组，若设置 false，则只返回该节点的值，默认为true
        data-lazy: 是否动态加载子节点，需与 data-lazy-load 方法结合使用,默认为false,动态加载以为无法
        data-lazy-load: 动态加载方式,当data-lazy为true有效,系统已内置,重写将覆盖内置调用方式

        data-data: 内置自定义数据,默认取value,label,如果设置data-value-field，data-display-field则取自定义的数据格式
        data-params：指定固定的查询参数值
        data-action：指定数据来源action
        data-graphql：指定数据来源graphql


        data-value-field：指定查询数据中作为值的字段名称
        data-display-field：指定查询数据中作为文本显示的字段名称，可以指定多个字段名（以半角逗号分隔），那么显示的时候就以data-display-separator分隔符串起来
        data-display-separator：指定显示多个字段值的时候字段值之间的分隔符，默认为“-”
        data-value-disabled: 指定结果中是否可以选择,当为true,'true','disabled',则当前项不能进行选择,data-data原数据方式不参与数据处理
        data-display-child: 指定子节点定义的字段名称,用于解析数据,一次行加载时候有效
        data-parent-field: 父节点的字段名称,用于解析数据,用于异步加载时候有效
        data-diffcondition: 一次行加载时指定上戏级关系
        : 如果配置则data-on-change触发返回的是对象,否则返回当前选中的vulue值

        ------事件------
        data-on-change: 定义修改事件
        data-on-beforeload: 指定加载数据之前触发调用的JS函数，切记初始化data-params
        data-on-afterload: 指定加载数据之后触发调用的JS函数
        data-value-method: 自定义指定结果选择条件，当满足条件时则屏蔽选择条件,传入一个是数据row对象，必须返回true才能条件成立function(row)，与data-value-disabled配套使用
        data-on-expand-change: 当展开节点发生变化时触发
        data-on-remove-tag: 在多选模式下，移除Tag时触发
        data-on-visible-change:下拉框出现/隐藏时触发
        data-on-blur: 当失去焦点时触发
        data-on-focus: 当获得焦点时触发

      </textarea>
    </k-field-code>
    <br>

    基础用法:
    <k-field-cascader v-model="value" :data-data="options" data-show-num data-on-object data-expand-trigger="hover" data-multiple data-size="medium" :data-placeholder="'请选择信息'" data-fileterable data-clearable @data-on-change="handleChange"/>  {{ value }}
    <k-field-code>
      <textarea>
        <k-field-cascader v-model="value" :data-data="options" data-show-num  data-expand-trigger="hover"  data-multiple  data-size="medium"  :data-placeholder="'请选择信息'" data-fileterable data-clearable />
        默认数据格式:options：[{value: '',label: '',children[{value: '',label: '',children[{……}]}]}]
      </textarea>
    </k-field-code>
    <br>

    基础用法panle:
    <k-field-cascader v-model="value9" :data-data="options" data-ui-type="panle" data-show-num data-expand-trigger="hover" data-multiple data-size="medium" :data-placeholder="'请选择信息'" data-fileterable data-clearable @data-on-change="handleChange"/>  {{ value9 }}
    <k-field-code>
      <textarea>
        <k-field-cascader v-model="value9" :data-data="options" data-ui-type="panle" data-show-num data-expand-trigger="hover" data-multiple data-size="medium" :data-placeholder="'请选择信息'" data-fileterable data-clearable @data-on-change="handleChange"/>
      </textarea>
    </k-field-code>
    <br>

    自定义数据格式(单选):
    <k-field-cascader v-model="value1" :data-data="options1" data-placeholder="请选择信息" data-size="small" data-value-field="code" data-display-field="code,name" data-display-child="next" /> {{ value1 }}
    <k-field-code>
      <textarea>
        <k-field-cascader v-model="value1" :data-data="options1"  data-placeholder="请选择信息" data-size="small" data-value-field="code" data-display-field="code,name" data-display-child="next" />
      </textarea>
    </k-field-code>
    <br>

    自定义数据格式(任意一级单一选项):
    <k-field-cascader v-model="value7" :data-data="options1" data-expand-trigger="hover" data-check-strictly data-show-num data-placeholder="请选择信息" data-size="small" data-value-field="code" data-display-field="code,name" data-display-child="next" /> {{ value7 }}
    <k-field-code>
      <textarea>
        <k-field-cascader v-model="value7" :data-data="options1" data-expand-trigger="hover" data-check-strictly  data-show-num data-placeholder="请选择信息" data-size="small" data-value-field="code" data-display-field="code,name" data-display-child="next" />
      </textarea>
    </k-field-code>
    <br>

    自定义数据格式(多选并折叠):
    <k-field-cascader
      v-model="value8"
      :data-data="options1"
      data-expand-trigger="hover"
      data-collapse-tags
      data-check-strictly
      data-multiple
      data-placeholder="请选择信息"
      data-size="small"
      data-value-field="code"
      data-display-field="code,name"
      data-display-child="next"
    /> {{ value8 }}
    <k-field-code>
      <textarea>
        <k-field-cascader v-model="value8" :data-data="options1" data-expand-trigger="hover" data-collapse-tags data-check-strictly data-multiple data-placeholder="请选择信息" data-size="small" data-value-field="code" data-display-field="code,name" data-display-child="next" />
      </textarea>
    </k-field-code>
    <br>


    不可输入非绑定变量:
    <k-field-cascader v-model="value2" :data-data="options" data-disabled data-placeholder="请选择信息" />
    <k-field-code>
      <textarea>
        <k-field-cascader v-model="value2" :data-data="options"  data-disabled data-placeholder="请选择信息"></k-field-cascader>
      </textarea>
    </k-field-code>
    <br>

    动态加载data-graphql用法:
    <k-field-cascader v-model="value3" :data-value-method="checkRow" data-graphql="{queryTest(action:&quot;findTest&quot;) {rows{testid,testname,parentid,tdisabled,leaf},results}}" data-value-disabled="tdisabled" data-lazy data-display-field="testid,testname" data-params="{'parentid':'ROOT'}" data-value-field="testid" data-parent-field="parentid" />
    <k-field-code>
      <textarea>
        <k-field-cascader v-model="value3" :data-value-method="checkRow" data-graphql="{queryTest(action:&quot;findTest&quot;) {rows{testid,testname,parentid,tdisabled,leaf},results}}" data-value-disabled="tdisabled" data-lazy data-display-field="testid,testname" data-params="{'parentid':'ROOT'}" data-value-field="testid" data-parent-field="parentid" />
      </textarea>
    </k-field-code>

    动态加载data-action用法:
    <k-field-cascader v-model="value4" data-action="Test.findTest" :data-props="dataProps1" data-display-field="testid,testname" data-lazy :data-params="{'parentid':'ROOT'}" data-value-field="testid" data-parent-field="parentid" />
    <k-field-code>
      <textarea>
        <k-field-cascader v-model="value4" data-action="Test.findTest" :data-props="dataProps1" data-display-field="testid,testname" data-lazy :data-params="{'parentid':'ROOT'}" data-value-field="testid" data-parent-field="parentid" />
      </textarea>
    </k-field-code>

    一次性加载data-action用法
    <k-field-cascader v-model="value5" data-action="Test.findTests" data-display-field="testid,testname" :data-on-beforeload="beforeload" :data-on-afterload="afterload" data-value-field="testid" data-display-child="children" data-diffcondition="testid,parentid" />
    <k-field-code>
      <textarea>
        <k-field-cascader v-model="value5" data-action="Test.findTests"  data-display-field="testid,testname"  :data-on-beforeload="beforeload" :data-on-afterload="afterload"  data-value-field="testid" data-display-child="children" data-diffcondition="testid,parentid"></k-field-cascader>
      </textarea>
    </k-field-code>

    一次性加载data-graphql用法
    <k-field-cascader v-model="value6" data-graphql="{queryTest(action:&quot;findTests&quot;) {rows{testid,testname,parentid},results}}" data-display-field="testid,testname" data-value-field="testid" data-display-child="children" data-diffcondition="testid,parentid" />
    <k-field-code>
      <textarea>
        <k-field-cascader v-model="value6" data-graphql='{queryTest(action:"findTests") {rows{testid,testname,parentid},results}}'  data-display-field="testid,testname"   data-value-field="testid" data-display-child="children" data-diffcondition="testid,parentid"></k-field-cascader>
      </textarea>
    </k-field-code>

  </div>

</template>

<script>

export default {
  data() {
    return {
      value: [],
      value1: [],
      value2: [],
      value3: [],
      value4: [],
      value5: [],
      value6: [],
      value7: [],
      value8: [],
      value9: [],
      dataProps: {
        expandTrigger: 'hover',
        multiple: true,
        lazy: true
      },
      dataProps1: {
        lazy: true
      },
      options: [{
        value: 'zhinan',
        label: '指南',
        children: [{
          value: 'shejiyuanze',
          label: '设计原则',
          children: [{
            value: 'yizhi',
            label: '一致'
          }, {
            value: 'fankui',
            label: '反馈'
          }, {
            value: 'xiaolv',
            label: '效率'
          }, {
            value: 'kekong',
            label: '可控'
          }]
        }, {
          value: 'daohang',
          label: '导航',
          children: [{
            value: 'cexiangdaohang',
            label: '侧向导航'
          }, {
            value: 'dingbudaohang',
            label: '顶部导航'
          }]
        }]
      }, {
        value: 'zujian',
        label: '组件',
        children: [{
          value: 'basic',
          label: 'Basic',
          children: [{
            value: 'layout',
            label: 'Layout 布局'
          }, {
            value: 'color',
            label: 'Color 色彩'
          }, {
            value: 'typography',
            label: 'Typography 字体'
          }, {
            value: 'icon',
            label: 'Icon 图标'
          }, {
            value: 'button',
            label: 'Button 按钮'
          }]
        }, {
          value: 'form',
          label: 'Form',
          children: [{
            value: 'radio',
            label: 'Radio 单选框'
          }, {
            value: 'checkbox',
            label: 'Checkbox 多选框'
          }, {
            value: 'input',
            label: 'Input 输入框'
          }, {
            value: 'input-number',
            label: 'InputNumber 计数器'
          }, {
            value: 'select',
            label: 'Select 选择器'
          }, {
            value: 'cascader',
            label: 'Cascader 级联选择器'
          }, {
            value: 'switch',
            label: 'Switch 开关'
          }, {
            value: 'slider',
            label: 'Slider 滑块'
          }, {
            value: 'time-picker',
            label: 'TimePicker 时间选择器'
          }, {
            value: 'date-picker',
            label: 'DatePicker 日期选择器'
          }, {
            value: 'datetime-picker',
            label: 'DateTimePicker 日期时间选择器'
          }, {
            value: 'upload',
            label: 'Upload 上传'
          }, {
            value: 'rate',
            label: 'Rate 评分'
          }, {
            value: 'form',
            label: 'Form 表单'
          }]
        }, {
          value: 'data',
          label: 'Data',
          children: [{
            value: 'table',
            label: 'Table 表格'
          }, {
            value: 'tag',
            label: 'Tag 标签'
          }, {
            value: 'progress',
            label: 'Progress 进度条'
          }, {
            value: 'tree',
            label: 'Tree 树形控件'
          }, {
            value: 'pagination',
            label: 'Pagination 分页'
          }, {
            value: 'badge',
            label: 'Badge 标记'
          }]
        }, {
          value: 'notice',
          label: 'Notice',
          children: [{
            value: 'alert',
            label: 'Alert 警告'
          }, {
            value: 'loading',
            label: 'Loading 加载'
          }, {
            value: 'message',
            label: 'Message 消息提示'
          }, {
            value: 'message-box',
            label: 'MessageBox 弹框'
          }, {
            value: 'notification',
            label: 'Notification 通知'
          }]
        }, {
          value: 'navigation',
          label: 'Navigation',
          children: [{
            value: 'menu',
            label: 'NavMenu 导航菜单'
          }, {
            value: 'tabs',
            label: 'Tabs 标签页'
          }, {
            value: 'breadcrumb',
            label: 'Breadcrumb 面包屑'
          }, {
            value: 'dropdown',
            label: 'Dropdown 下拉菜单'
          }, {
            value: 'steps',
            label: 'Steps 步骤条'
          }]
        }, {
          value: 'others',
          label: 'Others',
          children: [{
            value: 'dialog',
            label: 'Dialog 对话框'
          }, {
            value: 'tooltip',
            label: 'Tooltip 文字提示'
          }, {
            value: 'popover',
            label: 'Popover 弹出框'
          }, {
            value: 'card',
            label: 'Card 卡片'
          }, {
            value: 'carousel',
            label: 'Carousel 走马灯'
          }, {
            value: 'collapse',
            label: 'Collapse 折叠面板'
          }]
        }]
      }, {
        value: 'ziyuan',
        label: '资源',
        children: [{
          value: 'axure',
          label: 'Axure Components'
        }, {
          value: 'sketch',
          label: 'Sketch Templates'
        }, {
          value: 'jiaohu',
          label: '组件交互文档'
        }]
      }],
      options1: [{
        code: '10000',
        name: '北京',
        next: [{
          code: '10001',
          name: '海淀'
        },
        {
          code: '10002',
          name: '丰台'
        },
        {
          code: '10003',
          name: '大兴'
        }]
      }, {
        code: '20000',
        name: '辽宁省',
        next: [{
          code: '20001',
          name: '沈阳'
        }, {
          code: '20002',
          name: '辽阳'
        }]
      }]
    }
  },
  methods: {
    handleChange(value) {
      console.log('事件测试:')
      console.log(value)
    },
    beforeload(params) {
      console.log('事件测试:我绑定啦！！！')
      if (params != undefined) {
        this.$set(params, 'userid', '10258')
        console.log(params)
      }
    },
    afterload(params) {
      console.log('事件测试:我执行完啦！！！！')
      console.log(params)
    },
    handleRemoveTag(value) {
      const nodes = this.$refs.cascader.getCheckedNodes(false)
      if (nodes.length - 1 > 0) {
        if (nodes.length - 1 === 0) {
          this.$refs.cascader.handleClear()
        }
      }
      this.$emit('data-on-remove-tag', value)
    },
    checkRow(row) {
      return !!(row.tdisabled === '1')
    }
  }
}
</script>

<style scoped>

</style>
