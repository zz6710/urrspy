<template>
    <div class="dialogBody">
        <el-upload
                class="avatar-uploader"
                action="/upload/form/image.UploadImage"
                :show-file-list="false"
                :on-success="handleAvatarSuccess">
            <img v-if="imageUrl" :src="imageUrl" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
    </div>
</template>

<script>
import kayak from '@/frame/kayak.js'
import '@/styles/editExcel.css'
import fadeIn from '@/styles/fadeIn.css'

export default {
  name: 'UploadImg',
  data () {
    return {
      file: {},
      imageUrl: ''
    }
  },
  methods: {
    handleAvatarSuccess (response) {
      this.imageUrl = '/data/ShowImgAction/' + response.uploadAction.file.token
      this.file.imageUrl = response.uploadAction.file.token
    }
  },
  buttons: [
    {
      name: '确定',
      click: function () {
        this.$emit('ok', this.file)
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

<style>
    .avatar-uploader .el-upload {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
    }
    .avatar-uploader .el-upload:hover {
        border-color: #409EFF;
    }
    .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 178px;
        height: 178px;
        line-height: 178px;
        text-align: center;
    }
    .avatar {
        width: 178px;
        height: 178px;
        display: block;
    }

</style>
