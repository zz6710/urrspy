<template>
  <Shape :element="element" :isAbsLayout="isAbsLayout">
    <el-divider :direction="element.options.direction" :content-position="element.options.contentPosition">
      <template v-if="element.options.direction==='horizontal'">
        <template v-if="element.options.icon">
          <i v-if="element.options.icon.type=='icon'" v-bind:class="element.options.icon.value" />
          <md-icon v-else-if="element.options.icon.type=='md'">{{element.options.icon.value}}</md-icon>
          <md-icon v-else-if="element.options.icon.type=='svg'" :md-src="element.options.icon.value" />
        </template>
        {{element.options.value}}
      </template>
    </el-divider>
  </Shape>
</template>

<script>
import Shape from '@/pages/design/components/base/Shape.vue'

export default {
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
  data() {
    return {
    }
  },
  created() {

  },
  mounted() {
  },
  methods: {
  },
  watch: {
  }
}
</script>

<style lang="scss" scoped>
</style>
