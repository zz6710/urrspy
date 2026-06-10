<template>
    <div class="dialogBody">
        <el-upload
                class="upload-demo"
                action="/upload/form/excel.UploadExcelAction"
                :auto-upload="false"
                :limit="1"
                ref="upload"
                >
            <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
            <!--<el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>-->
            <div slot="tip" class="el-upload__tip">只能上传xls/xlsx文件</div>
        </el-upload>
    </div>
</template>

<script>
import '@/styles/editExcel.css'
import fadeIn from '@/styles/fadeIn.css'

export default {
  name: 'UploadExcel',
  methods: {
    submitUpload () {
      this.$confirm('确定要上传该文件吗?', '提示', {}).then(confirm => {
        if (confirm) {
          this.$refs.upload.submit()
          setTimeout(() => {
            this.$emit('ok')
          }, 500)
        }
      }).catch(error => {
        console.log('取消提交')
      })
    }
  },
  buttons: [
    {
      name: '确定',
      click: function () {
        this.$confirm('确定要上传该文件吗?', '提示', {}).then(confirm => {
          if (confirm) {
            this.$refs.upload.submit()
            setTimeout(() => {
              this.$emit('ok')
            }, 500)
          }
        }).catch(error => {
          console.log('取消提交')
        })
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

</style>
