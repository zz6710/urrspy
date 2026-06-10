<script>
import CodeViewer from '@/pages/design/code-viewer'
export default {
  name: 'FlowOnlineForm',
  components: { CodeViewer },
  props: ['code'],
  data() {
    return {
      className: ['page-container'],
      formCode:`
<template>
  <div>
    <k-form ref="form" :data-col="2" data-ui="element">
      <k-form-item label="角色名称">
        <k-field-text v-model="submitData.rolename" :data-allowblank="false" :data-max-length="30" />
      </k-form-item>
      <k-form-item label="角色描述">
        <k-field-text v-model="submitData.descript" :data-max-length="128" />
      </k-form-item>
    </k-form>
  </div>
</template>

<script>
export default {
  name: "roleForm",
  props: ["submitData"],
  data() {
    return {};
  },
  created() {},
  methods: {},
};
<\/script>`
    }
  },
  mounted() {},
  methods: {},

  render() {
    const { className, style } = this
    if (this.code) {
      this.formCode = this.code
    }
    return (
      <div class={className} style={style}>
        <CodeViewer source={this.formCode} showCode></CodeViewer>
      </div>
    )
  }
}
</script>

<style lang="scss" scoped>
.page-container {
  padding: 16px;
}
</style>
