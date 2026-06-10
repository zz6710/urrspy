export default function() {
  return {
    props: {
      value: {},
      dataDict: {
        type: String
      },
      dataSqlinfo: {
        type: String
      },
      id: {
        type: String
      },
      dataAction: {
        type: String
      },
      dataGraphql: {
        type: String
      },
      dataUrl: {
        type: String
      },
      dataData: {},
      dataAutoLoad: {
        type: [Boolean, String],
        default: true
      },
      dataReadonly: {
        type: [Boolean, String],
        default: false
      },
      dataDisabled: {
        type: [Boolean, String],
        default: false
      },
      dataClearable: {
        type: [Boolean, String],
        default: true
      },
      dataAllowblank: {
        type: [Boolean, String],
        default: true
      },
      dataDefaultValue: {},
      dataPlaceholder: {
        type: String,
        default: ''
      },
      dataValidate: {
        type: Function
      },
      dataParams: {
        type: [String,Object],
        default: undefined
      },
      dataShowSubscript: {
        type: Boolean,
        default: null
      },
      // 开启单个表单自动校验
      dataAutoValidate: {
        type: [Boolean, String],
        default: false
      },
      dataLabelWidth: {
        type: String,
        default: '120px'
      }
    }
  };
}
