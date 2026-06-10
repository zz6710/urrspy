import _ from 'lodash'

let dict = {
  Y_N: [{
      key: '是',
      value: true
    },
    {
      key: '否',
      value: false
    }
  ]
}

let commonOptions = {
  dataDict: {
    label: "数据字典",
    type: String
  },
  dataAction: {
    label: "数据来源dataAction",
    type: String
  },
  dataGraphql: {
    label: "数据来源dataGraphql",
    type: String
  },
  dataUrl: {
    label: "数据来源dataUrl",
    type: String
  },
  dataData: {
    label: "自定义数据",
    type: String
  },
  dataAutoLoad: {
    label: "自动加载",
    type: Boolean,
    default: true
  },
  dataDisabled: {
    label: "禁用",
    type: Boolean,
    default: false
  },
  dataClearable: {
    label: "是否可以清空",
    type: Boolean,
    default: true
  },
  dataAllowblank: {
    label: "是否允许为空",
    type: Boolean,
    default: true
  },
  dataDefaultValue: {
    label: "默认值",
    type: Object,
  },
  dataPlaceholder: {
    label: "占位文本",
    type: String,
  },
  dataValidate: {
    label: "自定义验证函数",
    type: Function
  },
  dataParams: {
    label: "默认参数",
    type: [String, Object],
    default: undefined
  },
  dataShowSubscript: {
    label: "显示标题",
    type: Boolean,
    default: null
  }
}


// 基础组件
export const basicComponents = [{
    name: '单行文本',
    type: 'k-field-text',
    renderType: '',
    paramType: 'k-field-text-param',
    icon: 'icon-input',
    events: [],
    methods: [],
    customAttrs: [],
    selectOptions: [],
    options: {
      dataAllowblank: commonOptions.dataAllowblank,
      dataDisabled: commonOptions.dataDisabled,
      dataClearable: commonOptions.dataClearable,
      dataMaxLength: {
        label: "最大长度",
        type: Number
      },
      dataMinLength: {
        label: "最小长度",
        type: Number,
      },
      dataShowWordLimit: {
        label: "字数统计",
        type: Boolean,
        default: false
      },
      dataShowPassword: {
        label: "显示为密码",
        type: Boolean,
        default: false
      },
      dataValidateType: {
        label: "验证类型",
        type: String,
      },
      dataRegx: {
        label: "正则校验",
        type: String,
        default: null
      },
      dataPlaceholder: {
        label: "占位文本",
        type: String,
      },
      dataRegxText: {
        label: "正则校验失败信息",
        type: String,
      },
      dataMaxValue: {
        label: "最大值",
        type: String
      },
      dataMinValue: {
        label: "最小值",
        type: String
      },
      dataDigits: {
        label: "小数位数",
        type: Number,
      },
      dataIntegerLength: {
        label: "整数位数",
        type: Number,
      }
    },
    style: {
      display: 'inline-block',
      'vertical-align': 'top'
    }
  },
  {
    name: '下拉选择框',
    type: 'k-field-select',
    renderType: '',
    paramType: 'k-field-select-param',
    icon: 'icon-select',
    events: [],
    methods: [],
    customAttrs: [],
    selectOptions: [],
    options: {
      dataAllowblank: commonOptions.dataAllowblank,
      dataDict: commonOptions.dataDict,
      dataParams: commonOptions.dataParams,
      dataAction: commonOptions.dataAction,
      dataGraphql: commonOptions.dataGraphql,
      dataUrl: commonOptions.dataUrl,
      dataDisabled: commonOptions.dataDisabled,
      dataClearable: commonOptions.dataClearable,
      dataData: {
        label: "自定义数据",
        type: Array,
        default: []
      },
      dataMultiple: {
        label: "多选",
        type: Boolean,
        default: false
      },
      dataFilterable: {
        label: "搜索",
        type: Boolean,
        default: true
      },
      //显示字段，可同时多个
      dataDisplayField: {
        label: "显示字段名称",
        type: String,
        default: "itemval"
      },
      //值字段
      dataValueField: {
        label: "值字段名称",
        type: String,
        default: "itemkey"
      },
      //显示字段分隔符
      dataValueSeparator: {
        label: "字段分隔符",
        type: String,
        default: "-"
      },
      dataPlaceholder: {
        label: "占位文本",
        type: String,
        default: "请选择"
      },
      dataAfterLoad: {
        label: "加载数据后回调函数",
        type: Function
      },
      dataRemote: {
        label: "远程搜索",
        type: Boolean,
        default: false
      },
      dataRemoteField: {
        label: "远程搜索字段",
        type: String
      },
      dataRemoteDataSize: {
        label: "远程搜索分页大小",
        type: Number,
        default: 10
      }
    },
    style: {
      display: 'inline-block',
      'vertical-align': 'top'
    }
  },
  {
    name: '单选框组',
    type: 'k-field-radio',
    renderType: '',
    paramType: 'k-field-radio-param',
    icon: 'icon-radio-active',
    events: [],
    methods: [],
    customAttrs: [],
    selectOptions: [],
    options: {
      dataAllowblank: commonOptions.dataAllowblank,
      dataDict: commonOptions.dataDict,
      dataParams: commonOptions.dataParams,
      dataAction: commonOptions.dataAction,
      dataGraphql: commonOptions.dataGraphql,
      dataUrl: commonOptions.dataUrl,
      dataDisabled: commonOptions.dataDisabled,
      dataData: {
        label: "自定义数据",
        type: Array,
        default: []
      },
      dataSize: {
        label: "组件大小",
        type: String,
        default: 'mini'
      },
      dataTextColor: {
        label: "文本颜色",
        type: String,
        default: '#ffffff'
      },
      dataFillColor: {
        label: "填充色和边框色",
        type: String,
        default: '#409EFF'
      },
      dataUiType: {
        label: "样式",
        type: String,
        default: 'radio'
      },
      dataBorder: {
        label: "显示边框",
        type: Boolean,
        default: false
      },
      // 显示字段，可同时多个
      dataDisplayField: {
        label: "显示字段名称",
        type: String,
      },
      // 值字段
      dataValueField: {
        label: "值字段名称",
        type: String,
      },
      // 显示字段分隔符
      dataDisplaySeparator: {
        label: "字段分隔符",
        type: String,
        default: '-'
      },
      dataOnBeforeload: {
        label: "加载数据前回调函数",
        type: Function,
      },
      dataOnAfterload: {
        label: "加载数据后回调函数",
        type: Function,
      },
      dataValueDisabled: {
        label: "行禁止选择",
        type: Boolean,
      },
      dataValueMethod: {
        label: "行禁止选择条件",
        type: Function,
      },
      dataOnObject: {
        label: "返回对象",
        type: Boolean,
      }
    },
  },
  {
    name: '多选框组',
    type: 'k-field-checkbox',
    renderType: '',
    paramType: 'k-field-checkbox-param',
    icon: 'icon-check-box',
    events: [],
    methods: [],
    customAttrs: [],
    selectOptions: [],
    options: {
      dataAllowblank: commonOptions.dataAllowblank,
      dataDict: commonOptions.dataDict,
      dataParams: commonOptions.dataParams,
      dataAction: commonOptions.dataAction,
      dataGraphql: commonOptions.dataGraphql,
      dataUrl: commonOptions.dataUrl,
      dataDisabled: commonOptions.dataDisabled,
      dataClearable: commonOptions.dataClearable,
      dataData: {
        label: "自定义数据",
        type: Array,
        default: []
      },
      dataSize: {
        label: '组件大小',
        type: String,
        default: 'mini'
      },
      dataMinNum: {
        label: '最小选中数量',
        type: Number,
      },
      dataMaxNum: {
        label: '最大选中数量',
        type: Number,
      },

      dataTextColor: {
        label: "文本颜色",
        type: String,
        default: '#ffffff'
      },
      dataFillColor: {
        label: "填充色和边框色",
        type: String,
        default: '#409EFF'
      },
      dataUiType: {
        label: "样式",
        type: String,
        default: 'checkbox'
      },
      dataLabelWidth: {
        label: '标签宽度',
        type: String,
      },
      dataTrueLabel: {
        label: '选中时的值',
        type: [String, Number],
      },
      dataFalseLabel: {
        label: '未选中时的值',
        type: [String, Number],
      },
      // 显示字段，可同时多个
      dataDisplayField: {
        label: "显示字段名称",
        type: String,
      },
      // 值字段
      dataValueField: {
        label: "值字段名称",
        type: String,
      },
      // 显示字段分隔符
      dataDisplaySeparator: {
        label: "字段分隔符",
        type: String,
        default: '-'
      },
      dataOnBeforeload: {
        label: "加载数据前回调函数",
        type: Function,
      },
      dataOnAfterload: {
        label: "加载数据后回调函数",
        type: Function,
      },
      dataValueDisabled: {
        label: "行禁止选择",
        type: Boolean,
      },
      dataValueMethod: {
        label: "行禁止选择条件",
        type: Function,
      },
      dataOnObject: {
        label: "返回对象",
        type: Boolean,
      }
    }
  },
  {
    name: '日期选择器',
    type: 'k-field-date',
    renderType: '',
    paramType: 'k-field-date-param',
    icon: 'icon-date',
    events: [],
    methods: [],
    customAttrs: [],
    selectOptions: [],
    options: {
      dataAllowblank: commonOptions.dataAllowblank,
      dataDisabled: commonOptions.dataDisabled,
      dataClearable: commonOptions.dataClearable,
      dataType: {
        label: '类型',
        type: String,
        default: 'date'
      },
      dataDateFormat: {
        label: '日期显示格式',
        type: String,
        default: 'yyyy-MM-dd'
      },
      dataValueFormat: {
        label: '日期值格式',
        type: String,
        default: 'yyyyMMdd'
      },
      dataMaxValue: {
        label: '最大值',
        type: String,
        default: '20991231'
      },
      dataMinValue: {
        label: '最小值',
        type: String,
        default: '19700101'
      },
      dataWorkday: {
        label: '工作日',
        type: Boolean
      },
      dataWorkdayPgmno: {
        label: '工作日方案编号',
        type: String
      },
      dataValidate: {
        label: '验证函数',
        type: Function
      }
    }
  },
  {
    name: '时间选择器',
    type: 'k-field-time',
    renderType: '',
    paramType: 'k-field-time-param',
    icon: 'icon-time',
    events: [],
    methods: [],
    customAttrs: [],
    selectOptions: [],
    options: {
      dataAllowblank: commonOptions.dataAllowblank,
      dataClearable: commonOptions.dataClearable,
      dataMinValue: {
        label: '最小值',
        type: String,
      },
      dataMaxValue: {
        label: '最大值',
        type: String,
      },
      dataValueFormat: {
        label: '日期值格式',
        type: String,
        default: 'HHmmss'
      },
      dataValidate: {
        label: '验证函数',
        type: Function
      }
    }
  },
  {
    name: '级联选择器',
    type: 'k-field-cascader',
    renderType: '',
    paramType: 'k-field-cascader-param',
    icon: 'icon-jilianxuanze',
    events: [],
    methods: [],
    customAttrs: [],
    selectOptions: [],
    options: {
      dataAllowblank: commonOptions.dataAllowblank,
      dataParams: commonOptions.dataParams,
      dataAction: commonOptions.dataAction,
      dataGraphql: commonOptions.dataGraphql,
      dataUrl: commonOptions.dataUrl,
      dataDisabled: commonOptions.dataDisabled,
      dataClearable: commonOptions.dataClearable,
      dataData: {
        label: "自定义数据",
        type: Array,
        default: []
      },
      dataUiType: {
        label: "样式",
        type: String,
        default: 'cascader'
      },
      dataSize: {
        label: "组件大小",
        type: String,
        default: 'mini'
      },
      dataShowAllLevels: {
        label: "显示全部等级",
        type: [Boolean, String],
      },
      dataCollapseTags: {
        label: "多选模式折叠tag",
        type: [Boolean, String],
      },
      dataValueSeparator: {
        label: "文本分隔符",
        type: String,
        default: '/'
      },
      dataFileterable: {
        label: "搜索",
        type: [Boolean, String],
      },
      dataFilterMethod: {
        label: "自定义搜索逻辑",
        type: Function,
      },
      dataDebounce: {
        label: "搜索防抖延迟(ms)",
        type: Number,
      },
      dataBeforeFilter: {
        label: "筛选之前的钩子",
        type: Function,
      },
      dataPopperClass: {
        label: "浮层类名",
        type: String,
      },
      dataShowNum: {
        label: "显示数量",
        type: [Boolean, String],
      },
      dataDisplaySeparator: {
        label: "字段分隔符",
        type: String,
        default: '-'
      },
      // 显示字段，可同时多个
      dataDisplayField: {
        label: "显示字段名称",
        type: String,
      },
      // 值字段
      dataValueField: {
        label: "值字段名称",
        type: String,
      },
      // 字段下标
      dataDisplayChild: {
        label: "子节点字段名称",
        type: String,
      },
      // 父节点
      dataParentField: {
        label: "父节点字段名称",
        type: String,
      },
      //上级级别节点
      dataDiffcondition: {
        label: "上级级别节点",
        type: String,
      },
      dataOnBeforeload: {
        label: "加载数据前回调函数",
        type: Function,
      },
      dataOnAfterload: {
        label: "加载数据后回调函数",
        type: Function,
      },
      dataExpandTrigger: {
        label: "菜单展开方式",
        type: String,
        default: 'click'
      },
      dataMultiple: {
        label: "多选",
        type: [Boolean, String],
      },
      // dataCheckStrictly: {
      //   label: "",
      //   type: [Boolean, String],
      //   default: false
      // },
      dataEmitPath: {
        label: "是否返回数组",
        type: [Boolean, String],
      },
      dataLazy: {
        label: "动态加载子节点",
        type: [Boolean, String],
      },
      dataLazyLoad: {
        label: "加载动态数据的方法",
        type: Function,
      },
      dataOnObject: {
        label: "返回对象",
        type: [Boolean, String],
      }
    }
  },
  {
    name: '树',
    type: 'k-field-tree',
    renderType: '',
    paramType: 'k-field-tree-param',
    icon: 'icon-jilianxuanze',
    events: [],
    methods: [],
    customAttrs: [],
    selectOptions: [],
    options: {
      dataAllowblank: commonOptions.dataAllowblank,
      dataParams: commonOptions.dataParams,
      dataAction: commonOptions.dataAction,
      dataGraphql: commonOptions.dataGraphql,
      dataUrl: commonOptions.dataUrl,
      dataDisabled: commonOptions.dataDisabled,
      dataClearable: commonOptions.dataClearable,
      dataData: {
        label: "自定义数据",
        type: Array,
        default: []
      },
      dataDisableBranchNodes: {
        label: "阻止选择分支节点",
        type: [Boolean, String],
        default: false
      },
      dataFilterMethod: {
        label: "自定义搜索逻辑",
        type: Function,
      },
      dataSearchAble: {
        label: "启用搜索功能",
        type: Boolean,
        default: true
      },
      dataBeforeFilter: {
        label: "筛选之前的钩子",
        type: Function,
      },
      // 显示字段，可同时多个
      dataDisplayField: {
        label: "显示字段",
        type: String,
      },
      // 值字段
      dataValueField: {
        label: "值字段名称",
        type: String,
      },
      dataPlaceholder: {
        label: "占位文本",
        type: String,
        default: "Select..."
      },
      // 字段下标
      dataDisplayChild: {
        label: "字段下标",
        type: String,
      },
      // 父节点
      dataParentField: {
        label: "父节点字段名称",
        type: String,
      },
      // 父节点
      dataChildField: {
        label: "子节点字段名称",
        type: String,
      },
      //上级级别节点
      dataDiffcondition: {
        label: "上级级别节点",
        type: String,
      },
      dataOnBeforeload: {
        label: "加载数据前回调函数",
        type: Function,
      },
      dataOnAfterload: {
        label: "加载数据后回调函数",
        type: Function,
      },
      dataMultiple: {
        label: "多选",
        type: [Boolean, String],
        default: true
      },
      dataLazy: {
        label: "动态加载子节点",
        type: [Boolean, String],
        default: undefined
      },
      dataFlat: {
        label: "平面模式",
        type: Boolean,
        default: true,
      },
      dataAutoSelectDescendants: {
        label: "自动选择后代",
        type: Boolean,
        default: false,
      },
      dataAutoDeselectDescendants: {
        label: "自动取消选择后代",
        type: Boolean,
        default: false,
      },
      dataAutoDeselectAncestors: {
        label: "自动选择祖先",
        type: Boolean,
        default: false,
      },
      dataAutoSelectAncestors: {
        label: "自动取消选择祖先",
        type: Boolean,
        default: false
      },
      dataDefaultExpandLevel: {
        label: "分支节点展开级数",
        type: Number,
        default: 0
      },
      dataNoResultsText: {
        label: "未匹配到结果文本",
        type: String,
        default: "没有匹配的结果"
      },
      dataNoOptionsText: {
        label: "无选项时显示文本",
        type: String,
        default: "没有内容"
      },
      dataShowCount: {
        label: "显示数量",
        type: Boolean,
        default: false
      },
      dataShowCountOnSearch: {
        label: "搜索时显示数量",
        type: Boolean,
        default: false
      },
      //只显示搜索结果
      dataOnlyShowResults: {
        label: "只显示搜索结果",
        type: Boolean,
        default: false
      },
      dataAlwaysOpen: {
        label: "菜单始终打开",
        type: Boolean,
        default: false
      },
      dataClearOnSelect: {
        label: "选择后清除搜索输入",
        type: Boolean,
        default: true
      }
    }
  },
  {
    name: '开关',
    type: 'k-field-bswitch',
    renderType: '',
    paramType: 'k-field-bswitch-param',
    icon: 'icon-switch',
    events: [],
    methods: [],
    customAttrs: [],
    selectOptions: [],
    options: {
      dataOnValue: {
        label: '开启的值',
        type: [Boolean, String, Number],
        default: true
      },
      dataOffValue: {
        label: '关闭的值',
        type: [Boolean, String, Number],
        default: false
      },
      dataConfirm: {
        label: '确认框',
        type: [Boolean, String],
        default: false
      },
      dataOnAction: {
        label: '打开调用接口',
        type: String
      },
      dataOffAction: {
        label: '关闭调用接口',
        type: String
      },
      dataOnConfirmInfo: {
        label: '打开时确认框提示信息',
        type: String,
        default: "开启"
      },
      dataOffConfirmInfo: {
        label: '关闭时确认框提示信息',
        type: String,
        default: "关闭"
      },
      dataBeforeHandler: {
        label: '执行前回调函数',
        type: Function
      },
      dataAfterHandler: {
        label: '执行后回调函数',
        type: Function
      }
    }
  },
  {
    name: '按钮',
    type: 'k-btn',
    renderType: 'k-btn-component',
    paramType: 'base-param',
    icon: 'icon-tabs',
    btnType: 'svg',
    events: [],
    methods: [],
    customAttrs: [],
    selectOptions: [],
    options: {
      // 扩展属性
      btnName: {
        label: "按钮名称",
        type: String,
        default: "新按钮",
        extField: true,
        paramType: 'text'
      },
      class: {
        label: "按钮类型",
          type: Array,
          default: [],
          extField: true,
          paramType: 'group-select',
          dict: [{
            label: "基础",
            childer: [{
              label: "图标",
              value: "md-just-icon"
            }, {
              label: "透明",
              value: "md-simple"
            }, {
              label: "md-button",
              value: "md-button"
            }, {
              label: "边框",
              value: "btn-border"
            }]
          }, {
            label: "OA主题色",
            childer: [{
              label: "主要",
              value: "btn-primary"
            }, {
              label: "信息",
              value: "btn-info"
            }]
          }, {
            label: "主题色",
            childer: [{
              label: "主要",
              value: "md-primary"
            }, {
              label: "成功",
              value: "md-success"
            }, {
              label: "信息",
              value: "md-info"
            }, {
              label: "警告",
              value: "md-warning"
            }, {
              label: "危险",
              value: "md-danger"
            }, ]
          }]
      },
      icon: {
        label: "按钮图标",
        type: Object,
        default: {},
        extField: true,
        paramType: 'icon'
      },
      // type: {
      //   label: "按钮颜色",
      //   type: String,
      //   default: "md-primary",
      //   extField: true
      // },
      // svgIcon: {
      //   label: "按钮图标",
      //   type: String,
      //   default: "icon-add",
      //   extField: true
      // },
      // mdIcon: {
      //   label: "按钮图标",
      //   type: String,
      //   default: "edit",
      //   extField: true
      // },
      // mdSrc: {
      //   label: "按钮图标",
      //   type: String,
      //   default: "/static/svg/restpw.svg",
      //   extField: true
      // },

      dataTarget: {
        label: "目标名称",
        type: String,
        default: "dataTarget",
        paramType: 'text'
      },
      dataParams: {
        label: "默认参数",
        type: String,
        paramType: 'text',
        placeholder: '',
      },
      dataDisabled: {
        label: "禁用",
        type: Boolean,
        default: false,
        paramType: 'select',
        dict: dict.Y_N,
      },
      dataDisabledGrey: {
        label: "禁用时置灰",
        type: Boolean,
        default: true,
        paramType: 'select',
        dict: dict.Y_N,
      },
      dataFunctype: {
        label: "按钮类型",
        type: String,
        default: "POPUP",
        paramType: 'select',
        dict: [{
            key: '重置表单',
            value: 'RESET'
          },
          {
            key: '提交',
            value: 'SUBMIT'
          },
          {
            key: '导出表格',
            value: 'EXPORT'
          },
          {
            key: '打开弹框',
            value: 'POPUP'
          },
          {
            key: '关闭弹框',
            value: 'CLOSE'
          },
          {
            key: '打开页面',
            value: 'PAGE'
          }
        ]
      },
      dataAction: {
        label: "提交请求的地址",
        type: String,
        paramType: 'text',
      },
      dataGraphql: {
        label: "提交请求的地址",
        type: String,
        paramType: 'text',
      },
      dataUrl: {
        label: "提交请求的地址",
        type: String,
        paramType: 'text',
      },
      dataConfirm: {
        label: "提交确认",
        type: Boolean,
        default: false,
        paramType: 'select',
        dict: dict.Y_N,
      },
      dataFrom: {
        label: "指定表单",
        type: String,
        paramType: 'text',
      },
      dataModel: {
        label: "提交参数",
        type: Object,
        paramType: 'text',
      },
      dataDescript: {
        label: "悬浮提示信息",
        type: String,
        paramType: 'text',
      },
      dataValidateForm: {
        label: "是否校验表单",
        type: Boolean,
        default: true,
        paramType: 'select',
        dict: dict.Y_N,
      },
      dataExportName: {
        label: "导出文件名",
        type: String,
        paramType: 'text',
      },
      dataDownloadName: {
        label: "下载文件名",
        type: String,
        paramType: 'text',
      },
      dataSize: {
        label: "大小",
        type: String,
        default: "",
        paramType: 'text',
      },
      dataAfterSuccess: {
        label: "提交成功回调函数",
        type: Function,
        paramType: 'function',
      },
      dataHandler: {
        label: "提交前回调函数",
        type: Function,
        paramType: 'function',
      },
    },
    style: {
      display: 'inline-block'
    }
  },
  {
    name: '表格字段',
    type: 'k-grid-column',
    renderType: '',
    icon: 'icon-zidingyishuju',
    events: [],
    methods: [],
    customAttrs: [],
    selectOptions: ["dataHeader", "dataName", "dataWidth"],
    options: {
      dataHeader: {
        label: '表头名称',
        type: String,
        default: "示例字段"
      },
      dataName: {
        label: '字段名称',
        type: String,
        default: "example_name"
      },
      dataSortable: {
        label: '开启排序',
        type: [Boolean, String],
        default: false
      },
      dataHidden: {
        label: '是否隐藏',
        type: [Boolean, String],
        default: false
      },
      dataFixed: {
        label: '固定位置',
        type: [Boolean, String],
        default: false
      },
      dataWidth: {
        label: '列宽(px)',
        type: String,
        default: ""
      },
      dataAlign: {
        label: '对齐方式',
        type: String,
        default: "left"
      },
      dataOverflow: {
        label: '内容过长显示提示',
        type: [Boolean, String],
        default: true
      },
      dataDict: {
        label: '数据字典',
        type: String
      },
      dataType: {
        label: '数据类型',
        type: String
      },
      dataRender: {
        label: '单元格渲染函数',
        type: String
      },
      dataSortMethod: {
        label: '排序函数',
        type: Function
      },
    }
  }
]

// 高级组件
export const advanceComponents = [{
    name: '表格',
    type: 'k-grid',
    renderType: 'k-grid-layout',
    paramType: 'k-grid-param',
    icon: 'icon-table',
    ref: 'kGrid',
    list: [],
    btns: [],
    headerBtns: [],
    events: [],
    methods: [],
    customAttrs: [],
    selectOptions: [],
    dataData: {
      rows: [{}],
      total: 20,
    },
    options: {
      dataTitle: {
        label: '表格名称',
        type: String,
      },
      dataShowSubscript: commonOptions.dataShowSubscript,
      dataParams: commonOptions.dataParams,
      dataAction: commonOptions.dataAction,
      dataGraphql: commonOptions.dataGraphql,
      dataUrl: commonOptions.dataUrl,
      dataEntity: {
        label: '动态表头模块加载',
        type: String,
        default: ''
      },
      dataServer: {
        label: '动态表头服务',
        type: String,
        default: ''
      },
      dataBorder: {
        label: '纵向边框',
        type: [Boolean, String],
        default: false
      },
      dataRender: {
        label: '标签文本宽度',
        type: String,
        default: ""
      },
      dataAlign: {
        label: '对齐方式',
        type: String,
        default: "left"
      },
      dataPageSize: {
        label: '每页记录数',
        type: Number,
        default: 10
      },
      dataAutoload: {
        label: '自动加载数据',
        type: [Boolean, String],
        default: true
      },
      dataOperateColumn: {
        label: '操作列',
        type: [Boolean, String],
        default: true
      },
      dataOperateColumnPosition: {
        label: '操作列位置',
        type: String,
        default: 'end'
      },
      dataDisplay: {
        label: '行详情',
        type: [Boolean, String],
        default: true
      },
      dataFixed: {
        label: '操作列固定',
        type: [Boolean, String],
        default: false
      },
      dataShowTree: {
        label: '树表格',
        type: Boolean,
        default: false,
      },
      dataTreeId: {
        label: '树ID',
        type: String,
      },
      dataReserveSelection: {
        label: '',
        type: Boolean,
        default: false
      },
      dataCheckboxMultiple: {
        label: '多选',
        type: Boolean,
        default: true
      },
      dataPaginationLayout: {
        label: '分页功能菜单',
        type: Array,
        default: ['total', 'sizes', 'prev', 'pager', 'next', 'jumper']
      },
      dataHeight: {
        label: '表格高度',
        type: String,
      },
      dataCheckbox: {
        label: '显示复选框',
        type: Boolean,
      },
      dataCheckboxWidth: {
        label: '复选框宽度',
        type: String,
      },
      dataStripe: {
        label: '斑马纹',
        type: Boolean,
      },
      dataExpandAll: {
        label: '展开所有行',
        type: Boolean,
      },
    },
    style: {}
  },
  {
    name: '普通表单',
    type: 'k-form',
    renderType: 'k-form-layout',
    paramType: 'k-form-param',
    icon: 'icon-icon_bars',
    list: [],
    btns: [],
    events: [],
    methods: [],
    customAttrs: [],
    selectOptions: [],
    options: {
      labelPosition: {
        label: "表单域标签的位置",
        type: String,
      },
      dataLabelWidth: {
        label: "标签宽度",
        type: String,
        default: "120px",
      },
      dataLabelPosition: {
        label: "标签定位",
        type: String,
        default: "horizontal",
      },
      dataInputWidth: {
        label: "组件宽度",
        type: String,
        default: "225px",
      },
      dataItemMargin: {
        label: "组件间距",
        type: String,
        default: "10px",
      },
      dataCol: {
        label: "表单每行几列",
        type: Number,
        default: 2,
      },
      inline: {
        label: "行内表单模式",
        type: Boolean,
      },
      dataUi: {
        label: "UI类型",
        type: String,
        default: "material",
      },
      dataTotalWidth: {
        label: "表单宽度",
        type: String,
      }
    },
    style: {}
  },
  {
    name: '查询表单',
    type: 'k-form-search',
    renderType: 'k-form-search-layout',
    paramType: 'k-form-search-param',
    icon: 'icon-icon_bars',
    list: [],
    btns: [],
    events: [],
    methods: [],
    customAttrs: [],
    selectOptions: [],
    options: {
      dataModelName: {
        label: "查询条件Model",
        type: String,
      },
      dataTarget: {
        label: "表格ref",
        type: String
      }
    },
    style: {}
  },
  {
    name: '自定义查询表单',
    type: 'k-form-search-customize',
    renderType: 'k-form-search-customize-layout',
    paramType: 'k-form-search-customize-param',
    icon: 'icon-icon_bars',
    list: [],
    btns: [],
    events: [],
    methods: [],
    customAttrs: [],
    selectOptions: [],
    options: {
      dataTarget: {
        label: "表格ref",
        type: String
      }
    }
  },
  {
    name: '文件上传',
    type: 'k-field-upload',
    renderType: '',
    paramType: 'k-field-upload-param',
    icon: 'icon-wenjianshangchuan',
    events: [],
    methods: [],
    customAttrs: [],
    selectOptions: [],
    options: {
      dataAllowblank: {
        label: "是否允许为空",
        type: Boolean,
        default: false

      },
      dataDisabled: {
        label: "禁用",
        type: Boolean,
        default: false
      },
      dataType: {
        label: "文件列表类型",
        type: String,
        default: "text"
      },
      dataDrag: {
        label: "拖拽上传",
        type: Boolean,
        default: true
      },
      dataAccept: {
        label: "接受上传文件类型",
        type: String
      },
      dataLimit: {
        label: "最大上传数量限制",
        type: Number
      },
      dataMultiple: {
        label: "支持多选",
        type: Boolean
      },
      dataAutoUpload: {
        label: "选取文件立即上传",
        type: Boolean
      },
      dataPreview: {
        label: "点击文件钩子函数",
        type: Function
      },
      dataRemove: {
        label: "移除文件钩子函数",
        type: Function
      },
      dataSuccess: {
        label: "上传成功钩子函数",
        type: Function
      },
      dataError: {
        label: "上传失败钩子函数",
        type: Function
      },
      dataProgress: {
        label: "上传时钩子函数",
        type: Function
      },
      dataBeforeUpload: {
        label: "上传文件前钩子",
        type: Function
      },
      dataBeforeRemove: {
        label: "删除文件前钩子",
        type: Function
      },
      dataExceed: {
        label: "文件超出个数钩子",
        type: Function
      }
    }
  },
  {
    name: '编辑器',
    type: 'k-field-rich',
    renderType: '',
    paramType: 'k-field-rich-param',
    icon: 'icon-fuwenbenkuang',
    events: [],
    methods: [],
    customAttrs: [],
    selectOptions: [],
    options: {
      dataMenus: {
        label: "自定义菜单",
        type: Array,
        default: [
          "head", // 标题
          "bold", // 粗体
          "fontSize", // 字号
          "fontName", // 字体
          "italic", // 斜体
          "underline", // 下划线
          "strikeThrough", // 删除线
          "foreColor", // 文字颜色
          "backColor", // 背景颜色
          "link", // 插入链接
          "list", // 列表
          "justify", // 对齐方式
          "quote", // 引用
          "emoticon", // 表情
          "image", // 插入图片
          "table", // 表格
          "video", // 插入视频
          "code", // 插入代码
          "undo", // 撤销
          "redo", // 重复
          "fullscreen" // 全屏
        ]
      },
      dataLinkCheck: {
        label: "插入文字和链接的校验",
        type: Function
      }
    }
  },
  {
    name: '步骤条',
    type: 'k-steps',
    renderType: 'KStepsLayout',
    paramType: 'base-param',
    icon: 'icon-number',
    list: [],
    events: [],
    methods: [],
    customAttrs: [],
    selectOptions: ['dataActive'],
    options: {
      dataDirection: {
        label: "显示方向",
        type: String,
        default: "horizontal",
        paramType: 'select',
        dict: [{
            key: '水平',
            value: 'horizontal'
          },
          {
            key: '垂直',
            value: 'vertical'
          }
        ]
      },
      dataActive: {
        label: "当前激活步骤",
        type: Number,
        default: 1,
        paramType: 'number',
      },
      dataNextShow: {
        label: "是否显示下一步按钮",
        type: Boolean,
        default: true,
        paramType: 'select',
        dict: dict.Y_N,
      },
      dataSubmitShow: {
        label: "是否显示提交按钮",
        type: Boolean,
        default: true,
        paramType: 'select',
        dict: dict.Y_N,
      },
      dataButtonAlign: {
        label: "按钮对齐方式",
        type: String,
        default: "center",
        paramType: 'select',
        dict: [{
            key: 'left',
            value: 'left'
          },
          {
            key: 'right',
            value: 'right'
          },
          {
            key: 'center',
            value: 'center'
          }
        ]
      },
      dataCol: {
        label: "每一行展示的k-form-item数量",
        type: Number,
        paramType: 'number',
      },
      dataAction: {
        label: "提交请求的地址",
        type: String,
        paramType: "text"
      },
      dataGraphql: {
        label: "提交请求的地址",
        type: String,
        paramType: "text"
      },
      dataConfirm: {
        label: "是否弹出确认提示框",
        type: Boolean,
        paramType: 'select',
        dict: dict.Y_N,
      },
      dataSubmitDescription: {
        label: "确认提示框内容",
        type: String,
        paramType: "text"
      },
      dataModel: {
        label: "提交的model",
        type: Object,
        paramType: "text"
      },
      kStepConfig: {
        label: "kStep配置",
        paramType: "component",
        component: "kStepsParam",
        extField: true,
      }
    },
    style: {}
  },
  {
    name: '步骤条(k-step)',
    type: 'k-step',
    renderType: '',
    icon: 'icon-number',
    show: false,
    list: [],
    events: [],
    methods: [],
    customAttrs: [],
    selectOptions: ["dataTitle", "dataDescription", "dataIcon"],
    options: {
      dataTitle: {
        label: '标题',
        type: String,
        default: "标题"
      },
      dataDescription: {
        label: '描述性文字',
        type: String,
        default: "描述性文字"
      },
      dataIcon: {
        label: '图标',
        type: String,
      }
    }
  },
  {
    name: '自定义控件',
    type: 'k-customer',
    renderType: 'k-customer',
    paramType: 'k-customer-param',
    icon: 'icon-icon_bars',
    list: [],
    options: {
      formId: {
        label: '控件id',
        type: String,
        default: ''
      },
    },
    style: {
      width: '100%',
      height: 250,
    },
  },
]

// 布局组件
export const layoutComponents = [

  {
    name: '栅格布局',
    type: 'el-row',
    renderType: 'grid-layout',
    paramType: 'el-row-param',
    icon: 'icon-zhage',
    columns: [{
        span: 12,
        list: []
      },
      {
        span: 12,
        list: []
      }
    ],
    options: {
      gutter: 0,
      justify: 'start',
      align: 'top'
    },
    style: {}
  },
  {
    name: '自由布局',
    type: 'absolute-layout',
    renderType: 'absolute-layout',
    paramType: 'base-param',
    icon: 'icon-zhage',
    list: [],
    options: {},
    style: {},
    events: [],
    methods: [],
    customAttrs: [],
    selectOptions: [],
  },
  {
    name: '固定布局',
    type: 'base-layout',
    renderType: 'base-layout',
    paramType: 'base-param',
    icon: 'icon-zhage',
    list: [],
    options: {},
    style: {},
    events: [],
    methods: [],
    customAttrs: [],
    selectOptions: [],
  },
  {
    name: '分割线',
    type: 'el-divider',
    renderType: 'el-divider-layout',
    paramType: 'base-param',
    icon: 'icon-tabs',
    events: [],
    methods: [],
    customAttrs: [],
    selectOptions: [],
    options: {
      value: {
        label: "分割文字",
        type: String,
        default: "文字",
        extField: true,
        paramType: 'text',
      },
      icon: {
        label: "图标",
        type: Object,
        default: {},
        extField: true,
        paramType: 'icon'
      },
      direction: {
        label: "分割线方向",
        type: String,
        default: "horizontal",
        paramType: 'select',
        dict: [{
            key: 'horizontal',
            value: 'horizontal'
          },
          {
            key: 'vertical',
            value: 'vertical'
          },
        ],
      },
      contentPosition: {
        label: "分割线文案的位置",
        type: String,
        default: "center",
        paramType: 'select',
        dict: [{
            key: 'left',
            value: 'left'
          },
          {
            key: 'center',
            value: 'center'
          },
          {
            key: 'right',
            value: 'right'
          },
        ]
      }
    },
    style: {
      display: 'inline-block',
      'vertical-align': 'top'
    }
  },
  {
    name: '折叠面板(el-collapse-item)',
    type: 'el-collapse-item',
    renderType: '',
    icon: 'icon-tabs',
    show: false,
    paramType: '',
    list: [],
    events: [],
    methods: [],
    customAttrs: [],
    selectOptions: ['name', 'title', 'disabled'],
    options: {
      name: {
        label: '唯一标志符',
        type: String,
        paramType: "text"
      },
      title: {
        label: '面板标题',
        type: String,
        default: "标题",
        paramType: "text"
      },
      disabled: {
        label: '是否禁用',
        type: Boolean,
        paramType: 'select',
        dict: dict.Y_N,
      },
      icon: {
        label: "图标",
        type: Object,
        default: {},
        extField: true,
        paramType: 'icon'
      },
    }
  },
  {
    name: '折叠面板',
    type: 'el-collapse',
    renderType: 'elCollapseLayout',
    paramType: 'base-param',
    icon: 'icon-tabs',
    list: [],
    events: [],
    methods: [],
    customAttrs: [],
    selectOptions: ['value', 'accordion'],
    options: {
      value: {
        label: "绑当前激活的面板",
        type: [String, Array],
        paramType: "text"
      },
      accordion: {
        label: "是否手风琴模式",
        type: Boolean,
        default: false,
        paramType: 'select',
        dict: dict.Y_N,
      },
      elCollapseItemConfig: {
        label: "elTabPane配置",
        paramType: "component",
        component: "el-collapse-param",
        extField: true,
      }
    },
    style: {

    }
  },
  {
    name: '标签页',
    type: 'el-tabs',
    renderType: 'el-tabs-layout',
    paramType: 'base-param',
    icon: 'icon-tabs',
    list: [],
    events: [],
    methods: [],
    customAttrs: [],
    selectOptions: [],
    options: {
      value: {
        label: "绑定值，选中选项卡的name",
        type: String,
        paramType: "text"
      },
      type: {
        label: "风格类型",
        type: String,
        default: "card",
        paramType: 'select',
        dict: [{
            key: 'card',
            value: 'card'
          },
          {
            key: 'border-card',
            value: 'border-card'
          }
        ]
      },
      closable: {
        label: "标签是否可关闭",
        type: Boolean,
        paramType: 'select',
        dict: dict.Y_N,
      },
      addable: {
        label: "标签是否可增加",
        type: Boolean,
        paramType: 'select',
        dict: dict.Y_N,
      },
      editable: {
        label: "标签是否同时可增加和关闭",
        type: Boolean,
        paramType: 'select',
        dict: dict.Y_N,
      },
      tabPosition: {
        label: "选项卡所在位置",
        type: String,
        paramType: 'select',
        dict: [{
            key: 'top',
            value: 'top'
          },
          {
            key: 'right',
            value: 'right'
          },
          {
            key: 'bottom',
            value: 'bottom'
          },
          {
            key: 'left',
            value: 'left'
          }
        ]
      },
      stretch: {
        label: "标签的宽度是否自撑开",
        type: Boolean,
        paramType: 'select',
        dict: dict.Y_N,
      },
      beforeLeave: {
        label: "切换标签之前的钩子",
        type: Function,
        paramType: 'function',
      },
      elTabPaneConfig: {
        label: "elTabPane配置",
        paramType: "component",
        component: "el-tabs-param",
        extField: true,
      }
    },
    style: {}
  },
  {
    name: '标签页(el-tab-pane)',
    type: 'el-tab-pane',
    show: false,
    renderType: '',
    icon: 'icon-tabs',
    paramType: '',
    list: [],
    events: [],
    methods: [],
    customAttrs: [],
    selectOptions: ["label", "disabled", "name", "closable", "lazy"],
    options: {
      label: {
        label: '选项卡标题',
        type: String,
        default: "标题",
        paramType: "text"
      },
      disabled: {
        label: '是否禁用',
        type: Boolean,
        paramType: 'select',
        dict: dict.Y_N,
      },
      name: {
        label: '表示选项卡别名',
        type: String,
        paramType: "text"
      },
      closable: {
        label: '标签是否可关闭',
        type: Boolean,
        paramType: 'select',
        dict: dict.Y_N,
      },
      lazy: {
        label: '标签是否延迟渲染',
        type: Boolean,
        paramType: 'select',
        dict: dict.Y_N,
      },
      icon: {
        label: "图标",
        type: Object,
        default: {},
        extField: true,
        paramType: 'icon'
      },
    }
  },
]

let chartsCommonOptions = {
  dataDarkModel: {
    label: "是否开启深色模式",
    type: Boolean,
    paramType: 'select',
    dict: dict.Y_N,
    extField: true,
  },
  dataTitleShow: {
    label: "是否显示标题",
    type: Boolean,
    paramType: 'select',
    dict: dict.Y_N,
  },
  dataTitleText: {
    label: "标题",
    type: String,
    paramType: 'text',
    default: '标题'
  },
  dataTitleSubtext: {
    label: "子标题",
    type: String,
    paramType: 'text',
    default: '子标题'
  },
  dataTitleLeft: {
    label: "标题左右位置",
    type: String,
    paramType: 'text',
    placeholder: "数字、百分比、left、center、right"
  },
  dataTitleTop: {
    label: "标题上下位置",
    type: String,
    paramType: 'text',
    placeholder: "数字、百分比、top、middle、bottom"
  },
  dataLegendShow: {
    label: "是否显示图例",
    type: Boolean,
    paramType: 'select',
    dict: dict.Y_N,
  },
  dataLegendLeft: {
    label: "图例左右位置",
    type: String,
    paramType: 'text',
    placeholder: "数字、百分比、left、center、right"
  },
  dataLegendTop: {
    label: "图例上下位置",
    type: String,
    paramType: 'text',
    placeholder: "数字、百分比、top、middle、bottom"
  },
  dataLegendOrient: {
    label: "图例的布局朝向",
    type: String,
    paramType: 'select',
    dict: [{
        key: '水平',
        value: 'horizontal'
      },
      {
        key: '垂直',
        value: 'vertical'
      },
    ]
  },
  dataTooltipShow: {
    label: "是否显示提示框",
    type: Boolean,
    paramType: 'select',
    dict: dict.Y_N,
  },
  dataSeriesLabelShow: {
    label: "是否显示标签",
    type: Boolean,
    paramType: 'select',
    dict: dict.Y_N,
  },

  dataOtherOptions: {
    label: "其他配置",
    type: String,
    paramType: 'text',
  },
  dataParams: {
    label: "默认参数",
    type: String,
    paramType: 'text',
    placeholder: '',
  },
  dataModel: {
    label: "提交参数",
    type: Object,
    paramType: 'text',
  },
  dataAction: {
    label: "提交请求的地址",
    type: String,
    paramType: 'text'
  },
  dataGraphql: {
    label: "提交请求的地址",
    type: String,
    paramType: 'text',
  },
  dataUrl: {
    label: "提交请求的地址",
    type: String,
    paramType: 'text',
  },
}

// 图表组件
export const chartComponents = [{
    name: '折线图',
    type: 'k-line-chart',
    renderType: '',
    paramType: 'base-param',
    icon: 'icon-icon_bars',
    events: [],
    methods: [],
    customAttrs: [],
    refresh: true,
    selectOptions: [],
    options: Object.assign({}, chartsCommonOptions, {
      dataXAxisName: {
        label: "x坐标轴名称",
        type: String,
        paramType: 'text',
      },
      dataXAxisType: {
        label: "x坐标轴类型",
        type: String,
        paramType: 'select',
        default: 'category',
        dict: [{
            key: '数值轴',
            value: 'value'
          },
          {
            key: '类目轴',
            value: 'category'
          },
          {
            key: '时间轴',
            value: 'time'
          },
          {
            key: '对数轴',
            value: 'log'
          },
        ]
      },
      dataXAxisLabelRotate: {
        label: "x坐标轴标签旋转角度",
        type: Number,
        paramType: 'number',
      },
      dataYAxisName: {
        label: "y坐标轴名称",
        type: String,
        paramType: 'text',
      },
      dataYAxisType: {
        label: "y坐标轴类型",
        type: String,
        paramType: 'select',
        default: 'value',
        dict: [{
            key: '数值轴',
            value: 'value'
          },
          {
            key: '类目轴',
            value: 'category'
          },
          {
            key: '时间轴',
            value: 'time'
          },
          {
            key: '对数轴',
            value: 'log'
          },
        ]
      },
      dataZoomInsideDisabled: {
        label: "鼠标控制缩放",
        type: String,
        paramType: 'select',
        dict: [{
            key: '控制x轴缩放',
            value: 'x'
          },
          {
            key: '控制y轴缩放',
            value: 'y'
          },
          {
            key: '不控制缩放',
            value: 'n'
          },
        ]
      },
      dataZoomSliderDisabled: {
        label: "滑块控制缩放",
        type: String,
        paramType: 'select',
        dict: [{
            key: '控制x轴缩放',
            value: 'x'
          },
          {
            key: '控制y轴缩放',
            value: 'y'
          },
          {
            key: '控制x、y轴缩放',
            value: 'xy'
          },
          {
            key: '不控制缩放',
            value: 'n'
          },
        ]
      },
      dataSeriesSmooth: {
        label: "是否平滑",
        type: Boolean,
        paramType: 'select',
        dict: dict.Y_N,
      },
      dataXAxisField: {
        label: "x轴字段名",
        type: String,
        paramType: 'text',
      },
      dataYAxisField: {
        label: "y轴字段名",
        type: String,
        paramType: 'text',
      },
      dataTypeField: {
        label: "type字段名",
        type: String,
        paramType: 'text',
      },
    }),
    style: {}
  },
  {
    name: '柱状图',
    type: 'k-bar-chart',
    renderType: '',
    paramType: 'base-param',
    icon: 'icon-icon_bars',
    events: [],
    methods: [],
    customAttrs: [],
    refresh: true,
    selectOptions: [],
    options: Object.assign({}, chartsCommonOptions, {
      dataXAxisName: {
        label: "x坐标轴名称",
        type: String,
        paramType: 'text',
      },
      dataXAxisType: {
        label: "x坐标轴类型",
        type: String,
        paramType: 'select',
        dict: [{
            key: '数值轴',
            value: 'value'
          },
          {
            key: '类目轴',
            value: 'category'
          },
          {
            key: '时间轴',
            value: 'time'
          },
          {
            key: '对数轴',
            value: 'log'
          },
        ]
      },
      dataXAxisLabelRotate: {
        label: "x坐标轴标签旋转角度",
        type: Number,
        paramType: 'number',
      },
      dataYAxisName: {
        label: "y坐标轴名称",
        type: String,
        paramType: 'text',
      },
      dataYAxisType: {
        label: "y坐标轴类型",
        type: String,
        paramType: 'select',
        dict: [{
            key: '数值轴',
            value: 'value'
          },
          {
            key: '类目轴',
            value: 'category'
          },
          {
            key: '时间轴',
            value: 'time'
          },
          {
            key: '对数轴',
            value: 'log'
          },
        ]
      },
      dataZoomInsideDisabled: {
        label: "鼠标控制缩放",
        type: String,
        paramType: 'select',
        dict: [{
            key: '控制x轴缩放',
            value: 'x'
          },
          {
            key: '控制y轴缩放',
            value: 'y'
          },
          {
            key: '不控制缩放',
            value: 'n'
          },
        ]
      },
      dataZoomSliderDisabled: {
        label: "滑块控制缩放",
        type: String,
        paramType: 'select',
        dict: [{
            key: '控制x轴缩放',
            value: 'x'
          },
          {
            key: '控制y轴缩放',
            value: 'y'
          },
          {
            key: '控制x、y轴缩放',
            value: 'xy'
          },
          {
            key: '不控制缩放',
            value: 'n'
          },
        ]
      },
      dataSeriesStack: {
        label: "是否堆叠",
        type: Boolean,
        paramType: 'select',
        dict: dict.Y_N,
      },
      dataTransverse: {
        label: "是否横向柱状图",
        type: Boolean,
        paramType: 'select',
        dict: dict.Y_N,
      },
      dataSort: {
        label: "排序",
        type: String,
        paramType: 'select',
        dict: [{
            key: '升序',
            value: 'asc'
          },
          {
            key: '降序',
            value: 'desc'
          }
        ]
      },
      dataXAxisField: {
        label: "x轴字段名",
        type: String,
        paramType: 'text',
      },
      dataYAxisField: {
        label: "y轴字段名",
        type: String,
        paramType: 'text',
      },
      dataTypeField: {
        label: "type字段名",
        type: String,
        paramType: 'text',
      },
    }),
    style: {}
  },
  {
    name: '饼图',
    type: 'k-pie-chart',
    renderType: '',
    paramType: 'base-param',
    icon: 'icon-icon_bars',
    events: [],
    methods: [],
    customAttrs: [],
    refresh: true,
    selectOptions: [],
    options: Object.assign({}, chartsCommonOptions, {
      dataSeriesRadius: {
        label: "半径大小",
        type: String,
        paramType: 'text',
        placeholder: '50%或40%,70%'
      },
      dataSeriesItemRadius: {
        label: "圆角",
        type: String,
        paramType: 'number'
      },
      dataSeriesRoseType: {
        label: "南丁格尔图",
        type: String,
        paramType: 'select',
        dict: dict.Y_N,
      },
      dataValueField: {
        label: "值字段名",
        type: String,
        paramType: 'text',
      },
      dataNameField: {
        label: "分类字段名",
        type: String,
        paramType: 'text',
      },
    }),
    style: {}
  }
]

/**
 * 根据type查找组件
 * @param {*} type
 */
export function findComponent(type) {
  let basicComponent = basicComponents.filter(v => v.type == type);
  if (basicComponent && basicComponent.length > 0) {
    return basicComponent[0]
  }

  let advanceComponent = advanceComponents.filter(v => v.type == type);
  if (advanceComponent && advanceComponent.length > 0) {
    return advanceComponent[0]
  }

  let layoutComponent = layoutComponents.filter(v => v.type == type);
  if (layoutComponent && layoutComponent.length > 0) {
    return layoutComponent[0]
  }

  let chartComponent = chartComponents.filter(v => v.type == type);
  if (chartComponent && chartComponent.length > 0) {
    return chartComponent[0]
  }
  return null;
}

/**
 * 查询组件属性信息
 * @param {*} type
 * @returns
 */
export function findComponentOptionsArr(type) {
  let optionsArr = [];
  let config = findComponent(type);
  if (config && config.options) {
    Object.keys(config.options).forEach(key => {
      // optionsArr.push({ key: key, label: config.options[key].label, extField: config.options[key].extField });
      optionsArr.push({
        key: key,
        ...config.options[key]
      });
    });
    return optionsArr;
  }
}

/**
 * 解析组件options
 * @param {*} type
 * @returns
 */
export function parseOptions(type) {
  let options = {};
  let config = findComponent(type);
  if (config && config.options) {
    Object.keys(config.options).forEach(key => {
      let value = config.options[key];
      let _value = value.default || null;
      // if(value.type == String){
      //   _value = value.default || '';
      // } else if(value.type == Boolean){
      //   _value = new Boolean(value.default || null);
      // } else if(value.type == Number){
      //   _value = value.default || null;
      // } else {
      //   console.log(" 暂不支持的数据类型 ", value.type);
      // }
      options[key] = _value;
    });
  }
  return _.cloneDeep(options);
}
