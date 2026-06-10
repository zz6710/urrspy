<template>
  <div>
    <k-field-code>
      <textarea>
属性：
 -	data-upload-url：上传地址，默认：base/comnUpload.json
 -	data-type: 上传格式，img/file, 默认file。img会显示图片上传样式，文件会显示文件上传样式
 -	data-multiple: 是否支持多选，默认fasle
 -	data-params: 指定默认上传参数
 -	data-drag: 是否支持拖拽上传，默认true
 -	data-accept: 接受的文件格式
 -	data-disabled: 是否禁用，默认true
 -	data-limit: 最大上传数量限制

事件：
 -	data-preview: 点击文件列表中已上传的文件时的钩子
 -	data-remove: 文件列表移除文件时的钩子
 -	data-success: 文件上传成功时的钩子
 -	data-error: 文件上传失败时的钩子
 -	data-progress: 文件上传时的钩子
 -	data-before-upload: 上传文件之前的钩子，参数为上传的文件，若返回 false 或者返回 Promise 且被 reject，则停止上传
 -	data-before-remove: 删除文件之前的钩子，参数为上传的文件和文件列表，若返回 false 或者返回 Promise 且被 reject，则停止删除
 -	data-auto-upload: 是否在选取文件后立即进行上传，默认true
 -	data-exceed: 文件超出个数限制时的钩子

函数：
upload(params);上传函数
getSuccessUpload();获取成功上传的文件列表
getErrorUpload();获取失败上传的文件列表
reset();重置上传控件，清空上传列表
abort();取消上传
          </textarea>
    </k-field-code>
<template>
  <div>
       <k-field-upload
        ref="kfu"
        v-model = "sucData"
        data-type="picture"
        :data-multiple="false"
        :data-auto-upload="false"
        :data-accept="dataAccept"
        @data-preview="preview"
        @data-remove="remove"
        @data-success="success"
        @data-progress="progress"
        @data-error="error"
        @data-before-upload="beforeUpload"
        @data-before-remove="beforeRemove"
        @data-exceed="exceed"
        :data-limit=3
        >
        </k-field-upload>
        <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过10m</div>
        <el-button @click="submit">上传</el-button>
        <el-button @click="abort">取消上传</el-button>
        <el-button @click="reset">重置上传列表</el-button>
        <el-button @click="getSuccessList">获取上传成功列表</el-button>
        <!-- <button @click="getErrorList">获取上传失败列表</button> -->
  </div>
</template>
    <k-field-code>
      <textarea>
代码：
 <k-field-upload
        ref="kfu"
        v-model = "sucData"
        data-type="picture"
        :data-multiple="false"
        :data-auto-upload="false"
        :data-accept="dataAccept"
        @data-preview="preview"
        @data-remove="remove"
        @data-success="success"
        @data-progress="progress"
        @data-error="error"
        @data-before-upload="beforeUpload"
        @data-before-remove="beforeRemove"
        @data-exceed="exceed"
        :data-limit=2 />
      </textarea>
    </k-field-code>
  </div>
</template>
<script>
  import kayak from '@/frame/kayak.js'
  export default {
    data() {
      return {
        uploadSuccessList: [],
        extraData: {
          "upload_dir": "123"
        },
        sucData: [],
        dataAccept: "image/jpg,image/jpeg,image/gif,image/png"
      }
    },
    methods: {
      submit() {
        this.$refs.kfu.upload(this.extraData)
        console.log("开始上传")
      },
      reset() {
        //重置上传控件，清空上传列表
        this.$refs.kfu.doReset()
      },
      abort() {
        //取消上传
        this.$refs.kfu.abort()
      },
      preview(file) {
        console.log("当前点击的是列表中的文件"+file.name)
      },
      remove(file, fileList) {
        console.log("删除"+file.name)
      },
      success(file, fileList) {
        console.log(fileList[fileList.length - 1].name+"上传成功--->")
      },
      error(fileList) {
        console.log("上传失败--->")
      },
      progress(fileList) {
        console.log("文件正在上传...")
      },
      beforeUpload(file) {
                console.log(file.type)
                const filter = (file.type === 'image/jpeg' ||  file.type === 'image/png');

                const isLt10M = file.size / 1024 / 1024 < 10
                if(!filter) {
                    this.$message({
                        message: '上传文件只能是jpg/png格式!',
                        type: 'error'
          });
          return false;//必须加上return false; 才能阻止
                }
                if(!isLt10M) {
                    this.$message({
                        message: '上传文件大小不能超过 10MB!',
                        type: 'error'
          });
         return false;
                }
                return filter && isLt10M
      },
      beforeRemove(file, fileList) {
        console.log("删除文件"+file.name+"之前")
      },
      exceed(files, fileList) {
        console.log("超出文件最大上传数量")
      },
      getSuccessList() {
        if(this.sucData) {
          console.log(this.sucData[0].url)
        }
        this.$refs.kfu.getSuccessUpload()
      },
      // getErrorList() {
      //   this.$refs.kfu.getErrorUpload()
      // }
    }
  }
</script>
<style>
</style>
