<template>
  <div>
    <div v-if="dataUiType == 'checkbox'">
      <md-checkbox v-for="item in options"  :model="dataValue" @change="selectChange" :key="item[key]" :value="item[key]"
                   :disabled="dataDisabled === true || dataDisabled === 'true'
                        || dataValueDisabled && dataValueDisabled in item
                        && item[dataValueDisabled] === 'disabled' || 'disabled' in item  ? true : false"
                   :dataMultiple="dataMultiple">
        <div :style="labelStyle">{{ item[label] }}</div>
      </md-checkbox>
    </div>
    <el-checkbox-group v-else :value="value" :size="dataSize" :disabled="dataDisabled === true || dataDisabled === 'true'"
      :min="dataMinNum" :max="dataMaxNum" :text-color="dataTextColor" :fill="dataFillColor" @input="handleInput"
      @change="handleClickChange">
      <el-checkbox-button v-for="item in options" :key="item[key]" :label="item[key]" :true-label="dataTrueLabel"
        :false-label="dataFalseLabel" :disabled="dataValueDisabled && dataValueDisabled in item && item[dataValueDisabled] === 'disabled' || 'disabled' in item  ? true : false">{{ item[label] }}</el-checkbox-button>
    </el-checkbox-group>
  </div>
</template>
<script>
  import props from '@/components/k-element/common/k-field-props.js'
  import event from '@/components/k-element/common/k-field-event.js'
  import emitter from "@/components/k-element/common/k-emitter.js";
  import Tools from '@/utils/tools.js'
  export default {
    name: 'KFieldCheckbox',
    mixins: [props(), event(), emitter()],
    props: {
      dataMultiple:{
        type: Boolean,
        default: true
      },
      value: {
        type: [String,Array],
        default: ""
      },
      dataSize: {
        type: String,
        default: 'mini'
      },
      dataMinNum: {
        type: Number,
        default: undefined
      },
      dataMaxNum: {
        type: Number,
        default: undefined
      },
      dataTextColor: {
        type: String,
        default: '#ffffff'
      },
      dataFillColor: {
        type: String,
        default: '#409EFF'
      },
      dataLabelWidth: {
        type: String,
        default: undefined
      },
      dataUiType: {
        type: String,
        default: 'checkbox'
      },
      dataTrueLabel: {
        type: [String, Number],
        default: undefined
      },
      dataFalseLabel: {
        type: [String, Number],
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
        label: String,
        datas: []
      }
    },
    computed: {
      dataValue(){
        if(this.value && this.value.length > 0){
          return this.value.split(",");
        }else{
          return [];
        }
      },
      labelStyle(){
        let labelStyle={}
        if(undefined!=this.dataLabelWidth){
          labelStyle.width =this.dataLabelWidth
          labelStyle.overflow="hidden"
          labelStyle.whiteSpace="nowrap"
          labelStyle.textOverflow="ellipsis"
        }
        return labelStyle
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
        this.params = (typeof this.dataParams === 'string') ? Tools.str2Json(this.dataParams) : this.dataParams
      }
      if (this.dataAutoLoad === true || this.dataAutoLoad === 'true') {
        this.loadData(this.dataParams)
      }
      if (this.MdField) {
        this.MdField.value = "true";
      }
      this.datas = this.value
    },

    methods: {
      loadData() {
        if (this.before) {
          this.before(this.params)
        }
        this.options = []
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
          console.error('未指定数据获取方式，请检查checkbox元素属性配置')
        }
        if (this.dataOnAfterload) {
          this.dataOnAfterload(this.options)
        }
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
      loadGraphqlData() {
        this.httpUtil.graphqlQurey({
          graphql: this.dataGraphql,
          params: this.params
        }).then(data => {
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
          this.handleChange(this.options.filter(x => [...value].some(y => y === x[this.key])))
        } else {
          this.handleChange(value)
        }
      },
      selectChange(value) {
        let _value  = "";

        if(this.dataMultiple === false && this.value){
          let index = value.indexOf(this.value);
          this.$delete(value,index);
        }

        if(value && value.length >0){
          _value = value.join(",");
        }
        this.handleChange(_value);
        this.handleInput(_value);
      }
    }
  }
</script>

<style lang="scss">
  @import './k-field-checkbox.scss';
  .label-limit{
    width: 100px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }
</style>
