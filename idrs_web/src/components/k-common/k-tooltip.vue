<template>
  <el-tooltip
    class="item"
    effect="dark"
    :content="name +  ''"
    placement="top"
    :disabled="disabled"
  >
    <div class="ellipsis" ref="name">
      <span>{{name}}</span>
    </div>
  </el-tooltip>
</template>
<script>
export default {
  props: {
    name: String
  },
  data() {
    return {
      disabled: false
    }
  },
  watch: {
    name: {
      handler(v) {
        if (!v) return
        this.$nextTick(()=>{
          this.disabled = this.$refs.name.scrollWidth <= this.$refs.name.clientWidth
        })
      },
      immediate: true
    }
  }
}
</script>
<style scoped lang="scss">
.ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
