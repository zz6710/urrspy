<template>
  <k-form  ref="fileForm" data-ui="element">

    <k-form-item label="流程名">
      <k-field-text v-model="taskInfo.processDisplayName"  :data-max-length="100" data-disabled/>
    </k-form-item>
    <k-form-item label="发起人">
      <k-field-text v-model="taskInfo.applyUser"  :data-max-length="100" data-disabled/>
    </k-form-item>
    <k-form-item label="发起时间">
      <k-field-date v-model="taskInfo.createDate"  :data-max-length="100" data-tyoe="date" data-disabled/>
    </k-form-item>
    <k-form-item label="流程状态">
      <k-field-select v-model="taskInfo.result" data-dict="flow_status"  :data-allowblank="false"/>
    </k-form-item>

    <k-form-item   label="附件" data-ui="element" data-input-width="500px">
      <k-field-upload label="附件信息" data-type="file" ref="uploadFlowFile" :data-multiple="false" :data-limit=1
                      :data-error="onUploadFlowFile" :dataChange="onUploadChange" :data-success="onSubmitSuccess"
                      data-accept=".pdf" :data-auto-upload="false"
                      data-upload-url="/upload/server/WorkflowServer/uploadFlow/upload.json">
      </k-field-upload>
<!--      :dataHttpRequest="httpRequest"-->
    </k-form-item>

    <k-form-footer data-align="center">
      <k-btn class="btn-custom-primary" data-target="prodInfoGrid" ref="submitBtn"
             data-from="fileForm" :data-model="taskInfo" :data-handler="submitUploadParam">
        <span >确定</span>
<!--        <span v-show="showSubmitBtn">确定</span>-->
<!--        <i v-show="!showSubmitBtn" class="el-icon-loading"/>-->
      </k-btn>
      <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
    </k-form-footer>
  </k-form>
</template>

<script>
    import Tools from "@/utils/tools";
    import KFieldUpload from "../../components/k-element/k-field-upload/k-field-upload"
    export default {
        name: "FlowUploadFile",
      comments: {KFieldUpload},
      props: {
        taskInfo: {}
      },
      data() {
        return {
          // showSubmitBtn:true,
          fileData:{}
        }
      },
      methods: {
        onUploadChange(file){
          let fileName = file.name
          let suffix = fileName.substr(fileName.lastIndexOf('.') + 1);
          if ('pdf' != suffix) {
            Tools.alert("只能上传格式为pdf类型的文档!","danger");
            this.$refs.uploadFlowFile.doReset();
            return false;
          }

          if (file == null) {
            Tools.alert("请选择文件!","danger");
            this.$refs.uploadFlowFile.doReset();
            return;
          }
        },
        onUploadFlowFile() {
          this.$refs.uploadFlowFile.doReset();
          // this.showSubmitBtn = true;
        },
        httpRequest(file){
 //         this.taskInfo.append('file', file);
        },
        submitUploadParam() {
          let formData = this.taskInfo;
          this.$set(formData,'isSurrogate','true');
          this.$refs.uploadFlowFile.upload(formData);
        },
        onSubmitSuccess() {
          this.$emit('submitClose', '1')
        },
      }
    }
</script>

<style scoped>

</style>
