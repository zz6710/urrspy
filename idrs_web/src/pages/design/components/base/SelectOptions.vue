<template>
  <div class="selectOptions">
    <p>{{ title }}</p>
    <el-select v-model="selectOptions" multiple filterable  placeholder="请选择组件属性" >
      <el-option
        v-for="item in options"
        :key="item.key"
        :label="item.label + '('+item.key+')'"
        :value="item.key">
      </el-option>
    </el-select>
  </div>
</template>

<script>
import { findComponentOptionsArr  } from '../componentsConfig';

export default {
  props: {
    title: {
      type: String,
      default: '选择需要修改的组件属性'
    },
    type: {
      type: String
    }, 
    value: {
      type: Array
    }
  },
  data () {
    return {
      options: [],
      selectOptions: [],
    }
  },
  created() {
    this. selectOptions = this.value
    let _options = findComponentOptionsArr(this.type);
    this.options = _options.filter(option => !option.extField)
    console.log(" options ", this.options);
  },
  mounted () {
  },
  methods: {
  },
  watch: {
    selectOptions: {
      handler(newVal, oldVal){
        this.$emit("input", newVal);
      },
      deep: true
    }
  }
}
</script>

<style lang="scss" scoped>
  .selectOptions {
    width: 100%;
    padding: 5px;
    background-color: #ecedee;

    p {
      margin-top: 5px;
      margin-bottom: 5px;
    }
  }
  .selectOptions .el-select {
    width: 100%;
  }
</style>
