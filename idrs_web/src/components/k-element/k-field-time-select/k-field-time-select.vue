<template>
  <el-time-select
    v-model="valueTime"
    :picker-options="{
      start: dataStart,
      step: dataStep,
      end: dataEnd
    }"
    :placeholder="dataPlaceholder"
    :disabled="dataDisabled"
    :editable="dataEditable"
    :clearable="dataClearable"
    :size="dataSize"
    :default-value="dataDefaultValue"
    :prefixIcon="dataPrefixIcon"
    :clearIcon="dataClearIcon"
    :start="dataStart"
    :end="dataEnd"
    :step="dataStep"
    :minTime="dataMinTime"
    :maxTime="dataMaxTime"
    @change="change"
    @blur="blur"
    @focus="focus"
  >
  </el-time-select>
</template>
<script>
export default {
  name: 'KFieldTimeSelect',
  props: {
    value: {
      type: String,
      default: ''
    },
    dataDisabled: {
      type: Boolean,
      default: false
    },
    dataEditable: {
      type: Boolean,
      default: true
    },
    dataClearable: {
      type: Boolean,
      default: true
    },
    dataSize: {
      type: String,
      default: ''
    },
    dataPlaceholder: {
      type: String,
      default: '选择时间'
    },
    dataDefaultValue: {
      type: String,
      default: ''
    },
    dataPrefixIcon: {
      type: String,
      default: ''
    },
    dataClearIcon: {
      type: String,
      default: ''
    },
    dataStart: {
      type: String,
      default: '00:00'
    },
    dataEnd: {
      type: String,
      default: '23:59'
    },
    dataStep: {
      type: String,
      default: '00:01'
    },
    dataMinTime: {
      type: String,
      default: ''
    },
    dataMaxTime: {
      type: String,
      default: ''
    }
  },
  watch: {
    value: {
      handler (v) {
        if (v) {
          this.valueTime = v
        } else {
          this.valueTime = this.dataDefaultValue
        }
      },
      immediate: true
    }
  },
  data () {
    return {
      valueTime: ''
    }
  },
  methods: {
    change () {
      this.$emit('input', this.valueTime)
      this.$emit('data-on-change')

    },
    focus () {
      this.$emit('data-on-focus')
    },
    blur () {
      this.$emit('data-on-blur')
    }
  }
}
</script>
