<template>
  <div v-if="dataUiType == 'radio'">
    <md-radio v-for="item in options" :key="item[key]" :model="value" @change="_handleInput" :value="item[key]"
              :disabled="(dataDisabled === true || dataDisabled === 'true') || dataValueDisabled && dataValueDisabled in item && item[dataValueDisabled] === 'disabled' || 'disabled' in item  ? true : false">{{ item[label] }}</md-radio>
  </div>
  <el-radio-group v-else :value="value" :size="dataSize" :disabled="dataDisabled === true || dataDisabled === 'true'"
    :text-color="dataTextColor" :fill="dataFillColor" @input="_handleInput" @change="handleClickChange">
    <el-radio-button v-for="item in options" :key="item[key]" :label="item[key]" :disabled="dataValueDisabled && dataValueDisabled in item && item[dataValueDisabled] === 'disabled' || 'disabled' in item  ? true : false">{{ item[label] }}</el-radio-button>
  </el-radio-group>
</template>

<script>
  import props from '@/components/k-element/common/k-field-props.js'
  import event from '@/components/k-element/common/k-field-event.js'
  import emitter from "@/components/k-element/common/k-emitter.js";
  import Tools from '@/utils/tools.js'
  export default {
    name: 'KFieldRadio',
    mixins: [props(), event(), emitter()],
    props: {
      value: {
        type: [Object, String, Boolean, Number],
        default: undefined
      },
      dataSize: {
        type: String,
        default: 'mini'
      },
      dataTextColor: {
        type: String,
        default: '#ffffff'
      },
      dataFillColor: {
        type: String,
        default: '#409EFF'
      },
      dataUiType: {
        type: String,
        default: 'radio'
      },
      dataBorder: {
        type: [Boolean, String],
        default: undefined
      },
      // 显示字段，可同时多个
      dataDisplayField: {
        type: String,
        default: undefined
      },
      // 值字段
      dataValueField: {
        type: String,
        default: undefined
      },
      // 显示字段分隔符
      dataDisplaySeparator: {
        type: String,
        default: '-'
      },
      dataOnBeforeload: {
        type: Function,
        default: undefined
      },
      dataOnAfterload: {
        type: Function,
        default: undefined
      },
      dataOnChange:{
        type:Function,
        default:undefined
      },
      dataOnObject: {
        type: [Boolean, String],
        default: undefined
      },
      dataValueDisabled: {
        type: String,
        default: undefined
      },
      dataValueMethod: {
        type: Function,
        default: undefined
      }
    },
    data() {
      return {
        options: [],
        params: {},
        key: String,
        label: String
      }
    },
    created() {
      if (this.dataDict) {
        this.key = !this.dataValueField ? 'itemkey' : this.dataValueField
        this.label = !this.dataDisplayField ? 'itemval' : this.dataDisplayField
      } else {
        this.key = !this.dataValueField ? 'value' : this.dataValueField
        this.label = !this.dataDisplayField ? 'label' : this.dataDisplayField
      }
      if (this.dataParams) {
        this.params = typeof this.dataParams === 'string' ? Tools.str2Json(this.dataParams) : this.dataParams
      }
      if (this.dataAutoLoad === true || this.dataAutoLoad === 'true') {
        this.loadData()
      }
    },
    methods: {
      loadData() {
        if (this.dataOnBeforeload) {
          this.dataOnBeforeload(this.params)
        }
        if (this.dataDict) { // 加载数字字典
          this.loadDictData()
        } else if (this.dataAction) { // 通过Action加载
          this.loadActionData()
        } else if (this.dataGraphql) { // 通过Graphql加载
          this.loadGraphqlData()
        } else if (this.dataData) { // 直接指定源数据
          if (this.dataValueField && this.dataDisplayField) {
            this.handleData(this.dataData)
          } else {
            this.options = this.dataData
          }
        } else {
          console.error('未指定数据获取方式，请检查radio元素属性配置')
        }
        if (this.dataOnAfterload) {
          this.dataOnAfterload(this.options)
        }
      },
      _handleInput(newVal){
        this.handleChange(newVal)
        this.handleInput(newVal)
      },
      // 处理数据
      handleData(rows) {
        if (this.dataDict) {
          rows.map(row => {
            // eslint-disable-next-line no-prototype-builtins
            if (!row.hasOwnProperty(this.key)) {
              console.warn('data-key-name定义的值与数据返回值不相符')
            }
            // eslint-disable-next-line no-prototype-builtins
            if (!row.hasOwnProperty(this.label)) {
              console.warn('data-lable-name定义的值与数据返回值不相符')
            }
            this.options.push(row)
          })
        } else {
          rows.map(row => {
            // 获取值
            const valueField = row[this.dataValueField]
            // 获取显示值
            let displayField
            const dataDisplayFields = this.dataDisplayField.split(',')
            for (let i = 0; i < dataDisplayFields.length; i++) {
              if (i == 0) {
                displayField = row[dataDisplayFields[i]]
              } else {
                displayField += this.dataDisplaySeparator + row[dataDisplayFields[i]]
              }
            }
            if (this.dataValueDisabled && row.hasOwnProperty(this.dataValueDisabled)) {
              this.setRowDisabld(row)
            }
            if (!row.hasOwnProperty(this.key)) {
              row[this.key] = valueField
            }
            if (!row.hasOwnProperty(this.label)) {
              row[this.label] = displayField
            } else {
              if (dataDisplayFields.length > 0) {
                row[this.label] = displayField
              }
            }
            this.options.push(row)
          })
        }
      },
      setRowDisabld(row) {
        if (row[this.dataValueDisabled] === true || row[this.dataValueDisabled] === 'true' || row[this.dataValueDisabled] ===
          'disabled' || (this.dataValueMethod !== undefined ? this.dataValueMethod(row) : false)) {
          row[this.dataValueDisabled] = 'disabled'
        }
      },
      loadGraphqlData(params) {
        this.httpUtil.graphqlQurey({
          graphql: this.dataGraphql,
          params: this.params
        }).then(data => {
          // 获取请求头
          const graphqlFirst = this.dataGraphql.substring(this.dataGraphql.indexOf('{') + 1, this.dataGraphql.indexOf(
            '('))
          this.handleData(data[graphqlFirst].rows)
        })
      },
      loadActionData() {
        this.httpUtil.comnQuery({
          action: this.dataAction,
          params: this.params
        }).then(data => {
          this.handleData(data.rows)
        })
      },
      loadDictData() {
        this.httpUtil.dict(this.dataDict).then(rows => {
          this.handleData(rows)
        })
      },
      handleClickChange(value) {
        if (this.dataOnObject === true || this.dataOnObject === 'true') {
          this.handleChange(this.options.filter(row => row[this.key] === value))
        } else {
          this.handleChange(value)
        }
      }
    }
  }
</script>

<style lang="scss">
  @import './k-field-radio.scss'
</style>
