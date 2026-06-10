<template>
  <k-popup ref="addApprovalPopup" data-title="提交审批任务" data-width="60%" >
    <flowProcessConfigurationJump ref="flowProcessConfigurationJump" :fromData="formList" @submitClose="popupClose" />
  </k-popup>
</template>

<script>
import flowProcessConfigurationJump from "@/pages/flow/flowProcessConfigurationJump";
export default {
  name: "flowJumpPopup",
  components: {
    flowProcessConfigurationJump
  },
  provide() {
    return {
      context: this.context
    }
  },
  data() {
    return {
      formList: {}
    }
  },
  mounted() {
    this.$refs.addApprovalPopup.popup()
  },
  methods: {
    popupClose() {
      this.$refs.addApprovalPopup.close()
      this.ok && this.ok()
    }
  }
}
</script>

<style scoped>

</style>
