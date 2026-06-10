<template>
  <div class="formItemParam">
    <el-form ref="form" :model="element" label-position="top" label-width="80px">
      <el-form-item label="标签名">
        <el-input v-model="element.label"></el-input>
      </el-form-item>
      <el-form-item label="标签宽度">
        <el-input v-model="element.dataLabelWidth"></el-input>
      </el-form-item>
      <el-form-item label="组件宽度">
        <el-input v-model="element.dataInputWidth"></el-input>
      </el-form-item>
      <el-form-item label="列数">
        <el-input-number v-model="element.dataCol"></el-input-number>
      </el-form-item>
      <el-form-item label="UI类型">
        <el-select v-model="element.dataUi" placeholder="请选择">
          <el-option label="material" value="material"></el-option>
          <el-option label="element" value="element"></el-option>
        </el-select>
      </el-form-item>
      <el-divider content-position="center">自定义属性</el-divider>
      <FormCustomAttr :customAttrs="element.customAttrs"></FormCustomAttr>
    </el-form>
  </div>
</template>

<script>
import FormCustomAttr from '../base/FormCustomAttr.vue';

export default {
  props: {
    element: {
      type: Object,
    },
  },
  components: {
    FormCustomAttr
  },
  data () {
    return {
    }
  },
  created() {
    if(!this.element.customAttrs){
      this.$set(this.element, 'customAttrs', [])
    }
  },
  mounted () {
  },
  methods: {
  },
  watch: {

  }
}
</script>

<style lang="scss" scoped>
.formItemParam {
    // width: 100%;
    padding: 10px;
    // background-color: #ecedee;
    border: 2px solid #ecedee;
    border-radius: 5px;
}
</style>
