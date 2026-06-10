<template>
  <div class="widget-param"
    v-if="element && element.key"
    :key="element.key"
  >
    <el-form ref="form" :model="element" label-position="top" label-width="80px">
      <el-form-item label="数据字段(v-model)">
        <el-input v-model="element.model"></el-input>
      </el-form-item>
      <el-form-item label="刷新数据的表格ref">
        <el-input v-model="element.options.dataTarget"></el-input>
      </el-form-item>

      <el-divider content-position="center">自定义属性</el-divider>
      <FormCustomAttr :customAttrs="element.customAttrs"></FormCustomAttr>

    </el-form>
  </div>
</template>

<script>
import Draggable from 'vuedraggable'
import KCodeEditor from '../KCodeEditor.vue';
import FormCustomAttr from '../base/FormCustomAttr.vue';

export default {
  components: {
    Draggable,KCodeEditor,FormCustomAttr
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
