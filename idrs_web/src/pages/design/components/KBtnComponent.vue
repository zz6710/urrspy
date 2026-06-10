<template>
  <Shape class="btn_outside" :element="element" :isAbsLayout="isAbsLayout" :style="kFormDesign.getStyle(element.style, ['width','height'], [])">
    <k-btn v-if="element.key" v-bind:class="element.options.class" v-bind="element.options" :style="kFormDesign.getStyle(element.style, ['position','top','left'])">
      <template v-if="element.options.icon">
        <i v-if="element.options.icon.type=='icon'" v-bind:class="element.options.icon.value" />
        <md-icon v-else-if="element.options.icon.type=='md'">{{element.options.icon.value}}</md-icon>
        <md-icon v-else-if="element.options.icon.type=='svg'" :md-src="element.options.icon.value" />
      </template>
      {{element.options.btnName}}
    </k-btn>
  </Shape>
</template>

<script>
import Shape from '@/pages/design/components/base/Shape.vue'
import Draggable from 'vuedraggable'
import eventBus from '@/utils/eventBus'

export default {
  components: {
    Draggable, Shape
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
      btnClass: "",
    }
  },
  created (){
    if(!this.element.icon){
      this.element.icon = {
        type: "",
        value: "",
      };
    }
  },
  mounted () {
  },
  methods: {
  },
  watch: {
    element: {
      handler(val){
      },
      deep: true
    }
  },
}
</script>

<style lang="scss" scoped>   //必须要加上“<style lang="scss" scoped>，否则引入失败（提示保存）
  @import '../styles/cover.scss';
  @import '../styles/index.scss';

</style>
