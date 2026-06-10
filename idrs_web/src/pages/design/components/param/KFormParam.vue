<template>
  <div class="widget-param"
    v-if="element && element.key"
    :key="element.key"
  >
    <SelectOptions v-model="element.selectOptions" :type="element.type"></SelectOptions>
    <el-form ref="form" :model="element" label-position="top" label-width="80px">
      <!-- <el-form-item label="表单域标签的位置" v-if="element.selectOptions.indexOf('labelPosition')">
        <el-input v-model="element.options.labelPosition"></el-input>
      </el-form-item> -->
      <el-form-item label="ref">
        <el-input v-model="element.ref"></el-input>
      </el-form-item>
      <el-form-item label="标签宽度" v-if="element.selectOptions.indexOf('dataLabelWidth') > -1">
        <el-input v-model="element.options.dataLabelWidth"></el-input>
      </el-form-item>
      <el-form-item label="标签定位" v-if="element.selectOptions.indexOf('dataLabelPosition') > -1">
        <el-input v-model="element.options.dataLabelPosition"></el-input>
      </el-form-item>
      <el-form-item label="组件宽度" v-if="element.selectOptions.indexOf('dataInputWidth') > -1">
        <el-input v-model="element.options.dataInputWidth"></el-input>
      </el-form-item>
      <el-form-item label="组件间距" v-if="element.selectOptions.indexOf('dataItemMargin') > -1">
        <el-input v-model="element.options.dataItemMargin"></el-input>
      </el-form-item>
      <el-form-item label="表单每行几列" v-if="element.selectOptions.indexOf('dataCol') > -1">
        <el-input-number v-model="element.options.dataCol"></el-input-number>
      </el-form-item>
      <el-form-item label="行内表单模式" v-if="element.selectOptions.indexOf('inline') > -1">
        <el-select v-model="element.options.inline" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="UI类型" v-if="element.selectOptions.indexOf('dataUi') > -1">
        <el-select v-model="element.options.dataUi" placeholder="请选择">
          <el-option label="material" value="material"></el-option>
          <el-option label="element" value="element"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="表单宽度" v-if="element.selectOptions.indexOf('dataTotalWidth') > -1">
        <el-input v-model="element.options.dataTotalWidth"></el-input>
      </el-form-item>

      <el-divider content-position="center">自定义属性</el-divider>
      <FormCustomAttr :customAttrs="element.customAttrs"></FormCustomAttr>

    </el-form>
  </div>
</template>

<script>
import Draggable from 'vuedraggable'
import KCodeEditor from '../KCodeEditor.vue';
import SelectOptions from '../base/SelectOptions.vue';
import FormCustomAttr from '../base/FormCustomAttr.vue';

export default {
  components: {
    Draggable,KCodeEditor,SelectOptions,FormCustomAttr
  },
  props: ['element', 'selectWidget'],
  inject: ['kFormDesign'],
  data () {
    return {
    }
  },
  created (){
  },
  mounted () {
    document.body.ondrop = function (event) {
      let isFirefox = navigator.userAgent.toLowerCase().indexOf('firefox') > -1
      if (isFirefox) {
        event.preventDefault()
        event.stopPropagation()
      }
    }
  },
  methods: {

  },
  watch: {
  }
}
</script>

<style lang="scss" scoped>   //必须要加上“<style lang="scss" scoped>，否则引入失败（提示保存）
  @import '../../styles/cover.scss';
  @import '../../styles/index.scss';

</style>
