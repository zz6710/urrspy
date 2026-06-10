<template>
  <div class="dialogBody" id="dialogBody">
    <div><b>姓名:</b><el-input v-model="user.name" disabled></el-input><br/></div>
    <div><b>旧密码:</b><el-input v-model="user.oldPassword" placeholder="请填写您的旧密码"></el-input><br/></div>
    <div><b>新密码:</b><el-input type="password" v-model="user.newPassword" placeholder="请填写您的新密码"></el-input><br/></div>
    <div><el-input type="password" v-model="user.againPassword" placeholder="请再次填写您的新密码"></el-input><br/></div>
    </div>
</template>

<script>
import kayak from '@/frame/kayak.js'
import '@/styles/editExcel.css'

export default {
  name: 'EditPassword',
  props: ['param'],
  data: function () {
    return {
      kayak: kayak,
      user: {
        name: this.param.name,
        oldPassword: '',
        newPassword: '',
        againPassword: ''
      }
    }
  },
  methods: {},
  mounted: function () {},
  buttons: [
    {
      name: '确定',
      click: function () {
        if (this.user.newPassword == '' && this.user.againPassword == '') {
          this.$notify({
            title: '失败',
            message: '新密码不能为空哦！',
            type: 'error'
          })
        } else {
          if (this.user.againPassword == this.user.newPassword) {
            let action = {
              userId: this.param.id,
              oldPassword: this.user.oldPassword,
              newPassword: this.user.newPassword
            }
            this.$emit('ok', action)
          } else {
            this.$notify({
              title: '失败',
              message: '您两次输入的新密码不同',
              type: 'error'
            })
          }
        }
      }
    },
    {
      name: '取消',
      click: function () {
        this.$emit('cancel')
      }
    }
  ]
}
</script>

<style scoped>
.el-dialog__header {
  font-weight: 600;
  font-size: 18px;
  color: #409eff;
}

.el-dialog__body {
  padding: 20px;
}

.el-dialog__body .el-input {
  width: 60%;
  left: 12%;
}

.el-dialog__body b {
  position: absolute;
  left: 6%;
  margin-top: 2px;
}

.dialogBody div {
  margin-bottom: 7px;
  line-height: 35px;
}

.el-checkbox {
  margin-left: 30px;
}

#dialogBody div b {
  font-size: 14px;
  font-weight: 600;
  margin-top: 0;
  margin-right: 5px;
}

#dialogBody div .el-input {
  margin-left: 3%;
}
</style>
