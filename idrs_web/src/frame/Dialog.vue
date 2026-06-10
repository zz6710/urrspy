<template>
  <el-dialog :width="width" :modal-append-to-body="false" :close-on-click-modal="false" :before-close="handleClose" :visible.sync="visible">
    <template slot="title">{{ title }}</template>
    <template>
      <component ref="page" :is="page" :param="param" @ok="onPageOk" @cancel="onPageCancel"></component>
    </template>
    <span slot="footer" class="dialog-footer" v-if="buttons && buttons.length > 0">
      <el-button v-for="button of buttons" @click="buttonClicked(button)" :key="button.name" type="primary">{{ button.name }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  data: function () {
    return {
      title: 'Title',
      page: '',
      width: '40%',
      visible: true,
      param: '',
      buttons: [] // 每个元素是一个按钮，{ name: "确定", click: function() {} }, 从包含的page的buttons属性复制过来
    }
  },
  methods: {
    buttonClicked (button) {
      button.click.bind(this.$refs.page).call() // 将按钮的事件绑定到page对象上
    },
    onPageOk (result) {
      this.$emit('ok', result)
      this.visible = false
    },
    onPageCancel () {
      this.$emit('cancel')
      this.visible = false
    },
    handleClose (done) {
      this.$emit('cancel')
      done()
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
