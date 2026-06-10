<template>
  <Shape :element="element" :isAbsLayout="isAbsLayout" :style="kFormDesign.getStyle(element.style, ['width','height'], [])">
    <component ref="cmpt" :key="unkey" :is="element.type" v-bind="element.options" :style="kFormDesign.getStyle(element.style, ['position','top','left'])"></component>
  </Shape>
</template>

<script>
import Shape from '@/pages/design/components/base/Shape.vue'
import { getStyle } from '@/pages/design/utils/style'

export default {
  name: 'BasicComponent',
  components: {
    Shape
  },
  props: {
    element: {
      type: Object
    },
    isAbsLayout: {
      type: Boolean,
      default: false
    }
  },
  inject: ['kFormDesign'],
  data () {
    return {
      unkey: ''
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
    getStyle: getStyle
  },
  watch: {
    element: {
      handler: function (newVal, oldVal) {
        if (this.element.refresh) {
          if (this.element.type.indexOf('chart') > -1) {
            //修改echarts组件后重新渲染
            this.$nextTick(() => {
              this.$refs.cmpt.loadData()
            })
          } else {
            this.unkey = Date.parse(new Date()) + '_' + Math.ceil(Math.random() * 99999);
          }
        }
      },
      deep: true
    },
  }
}
</script>

<style lang="scss" scoped>   //必须要加上“<style lang="scss" scoped>，否则引入失败（提示保存）
  @import '../styles/cover.scss';
  @import '../styles/index.scss';

</style>
