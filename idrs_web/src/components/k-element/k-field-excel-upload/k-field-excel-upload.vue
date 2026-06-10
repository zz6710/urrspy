<template>
  <el-upload
    class="upload-demo"
    ref="upload"
    :value="value"
    :headers="headers"
    :action="_dataUploadUrl"
    :disabled="dataDisabled === true || dataDisabled === 'true'"
    :limit="dataLimit"
    :drag="dataDrag === true || dataDrag === 'true'"
    :list-type="dataType"
    :accept="dataAccept"
    :multiple="dataMultiple === true || dataMultiple === 'true'"
    :data="extraData"
    :auto-upload="dataAutoUpload === true || dataAutoUpload === 'true'"
    :on-preview="dataPreview"
    :on-remove="dataRemove"
    :on-change="dataChange"
    :on-success="_dataSuccess"
    :on-error="dataError"
    :on-progress="dataProgress"
    :before-upload="dataBeforeUpload"
    :before-remove="dataBeforeRemove"
    :on-exceed="dataExceed"
    :http-request="dataHttpRequest"
  >
    <i class="el-icon-upload" v-if="dataDrag"></i>
    <div class="el-upload__text" v-if="dataDrag">将文件拖到此处，或<em>点击上传</em></div>
    <k-btn class="md-success" v-else>
      <md-icon md-src="/static/svg/upload.svg"/>
      上传附件
    </k-btn>
  </el-upload>
</template>

<script>
import props from "@/components/k-element/common/k-field-props.js";
import event from "@/components/k-element/common/k-field-event.js";
import emitter from "@/components/k-element/common/k-emitter.js";
import auth from "@/utils/auth.js";
import Tools from '@/utils/tools.js';
import kayak from "../../../frame/kayak";
import httpUtil from "../../../frame/httpUtil";


export default {
  name: 'KFieldExcelUpload',
  mixins: [props(), event(), emitter()],
  data() {
    return {
      uploadSuccessList: [],
      extraData: {},
      headers: {
        'Authorization': auth.getToken()
      }
    }
  },
  props: {
    dataUploadUrl: {
      type: String,
      default: 'base/comn-upload.json'
    },
    dataType: {
      type: String,
      default: 'text'
    },
    dataDrag: {
      type: [Boolean, String],
      default: true
    },
    dataAccept: String,
    dataDisabled: {
      type: [Boolean, String],
      default: false
    },
    dataLimit: {
      type: Number,
      default: 3
    },
    dataMultiple: {
      type: [Boolean, String],
      default: true
    },
    dataAutoUpload: {
      type: [Boolean, String],
      default: false
    },
    dataPreview: {
      type: Function
    },
    dataRemove: {
      type: Function
    },
    dataChange: {
      type: Function
    },
    dataSuccess: {
      type: Function
    },
    dataError: {
      type: Function
    },
    dataProgress: {
      type: Function
    },
    dataBeforeUpload: {
      type: Function
    },
    dataBeforeRemove: {
      type: Function
    },
    dataExceed: {
      type: Function
    },
    dataHttpRequest:{
      type:Function
    },
    dataTipSucc: String,
    dataTipFail: String,
  },
  computed: {
    _dataUploadUrl() {
      return this.httpUtil.basePath + this.dataUploadUrl;
    }
  },
  methods: {
    httpRequest(){

    },
    //外部可调用函数
    //调用方式: this.$refs.xx.method()
    upload(params) {
      this.extraData = params;
      //等待ElementUi渲染控件结束才能传参，否则后端获取不到前端传递的参数
      this.$nextTick(()=>{
        this.$refs.upload.submit()
      })
    },
    getSuccessUpload() {
      //获取成功上传的文件列表
      console.log(this.uploadSuccessList)
    },
    doReset() {
      //重置上传控件，清空上传列表
      this.uploadSuccessList = []
      this.extraData = {}
      this.$refs.upload.clearFiles()
    },
    abort(file) {
      //取消上传
      this.$refs.upload.abort(file)
    },
    _dataSuccess(response, file, fileList) {
      this.uploadSuccessList = fileList

      if (response.success === true) {
        Tools.alertTime(response.returnmsg || this.dataTipSucc || "批量导入成功", "success", 0)
        if (this.dataSuccess) {
          this.dataSuccess(file, fileList)
        }
        this.$emit("input", fileList)
      } else {
        if (response.login) {
          kayak.app.$router.push({
            path: '/login'
          });
          return;
        }
        if (response.token_freshen) {
          auth.setToken(response.token);
          let formData=new FormData()
          for(let a in this.extraData){
            formData.append(a,this.extraData[a])
          }
          formData.append("file",file.raw)
          this.httpUtil.upload({
            url:this.dataUploadUrl,
            formData:formData
          }).then(res=>{
            this._dataSuccess(res.data, file, fileList);
          })
          return;
        }
        Tools.alertTime(response.returnmsg || this.dataTipFail || "批量导入失败", "danger", 0)
        // this.doReset();
        this.dataError();
      }
    },
    _dataError(err, file, fileList){

    }
  },
}
</script>
