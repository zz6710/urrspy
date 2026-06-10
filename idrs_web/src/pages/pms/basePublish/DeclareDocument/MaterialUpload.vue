<template>
  <div>
    <k-form dataInputWidth="300px" ref="uploadFrom" :data-col="2">
      <k-form-item label="模板名称" >
        <k-field-text v-model="formData.templateName" :data-allowblank="false" :data-max-length="128" :data-disabled="disabledVal"/>
      </k-form-item>
      <k-form-item label="文档类型">
        <k-field-select v-model="formData.templateType" data-action="MaterialTemplate.getTemplateTypeDict" :data-allowblank="false"
                        data-display-field="templateType,templateLabel" data-value-field="templateType" :data-disabled="disabledVal"
                        @data-on-change="templateTypeChange"/>
      </k-form-item>

      <k-form-item label="文档子类型" v-if="templateTypeDict.length>1">
        <k-field-select v-model="formData.templateSonType"  :data-data="templateTypeDict" :data-allowblank="false"
                        data-display-field="templateSonType,templateSonLabel" data-value-field="templateSonType" :data-disabled="disabledVal"/>
      </k-form-item>

      <k-form-item label="产品模式" v-show="formData.templateType!=='04' && formData.templateType!=='05'" >
        <k-field-select v-model="formData.prodMod" :data-allowblank="false" data-dict="declare_prod_mod" :data-disabled="disabledVal"/>
      </k-form-item>
      <k-form-item label="托管行" v-show="formData.templateType==='04' || formData.templateType==='05'">
        <k-field-select v-model="formData.truteeBank"  data-dict="pids_trutee_bank" :data-allowblank="false"
                        data-display-field="itemkey,itemval" data-value-field="itemkey" :data-disabled="disabledVal"/>
      </k-form-item>

      <k-form-item label="备注" :dataCol=2>
        <k-field-text v-model="formData.remark"  inputType="textarea" :rows="2" :data-max-length="256"/>
      </k-form-item>

      <k-form-item label="附件" data-ui="element" data-input-width="500px">
        <k-field-upload data-type="file" ref="fileUploadRef" :data-multiple="false" :data-limit=1
                        :data-error="onFileSubmitError" :data-success="onFileSubmitSuccess"
                        :data-auto-upload="false" data-upload-url="upload/server/PmsApp/materialController/MaterialUploadAction.action">
        </k-field-upload>
      </k-form-item>

      <k-form-footer data-align="center">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT"  ref="fileSubmitBtn"
               data-from="uploadFrom" :data-model="formData" :data-handler="fileSubmitUploadParam">确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
      </k-form-footer>

    </k-form>

  </div>
</template>

<script>

  import Tools from "@/utils/tools";

  export default {
    name: "MaterialUpload",
    props: {
      info : {
        type:Object,
      },
      disabledVal: {
        type:Boolean,
      },
    },
    data() {
      return {
        formData : {},
        templateTypeDict:[],
      };
    },
    computed : {

    },
    methods: {

      onFileSubmitError(){
        this.$refs.fileUploadRef.doReset();
        this.$refs.fileSubmitBtn.setIconStyle(1, []);
      },
      onFileSubmitSuccess() {
        this.$refs.fileUploadRef.doReset();
        this.$refs.uploadFrom.reset();
        this.$emit('loadTemplateDoc',this.formData);
      },
      fileSubmitUploadParam(){
        let formData = this.formData;
        let validate = this.$refs.uploadFrom.validate();
        if(validate){
          let temp = document.getElementsByClassName('upload-demo');
          let lis = temp[0].childNodes[1].childNodes.length;
          if (lis > 0) {
            this.$refs.fileUploadRef.upload(formData);
          } else {
            Tools.alert("上传文件不能为空!", "danger")
            return false;
          }
        }
      },
      templateTypeChange(val) {
        this.httpUtil.comnQuery({
          action: "MaterialTemplate.getTemplateSonTypeDict",
          params: {templateType: val}
        }).then(data => {
          this.templateTypeDict = data.rows;
        })
        if(val === '04' || val === '05'){//尽职调查 或托管行协议
          this.$set(this.formData,'prodMod','99');
          this.$set(this.formData,'truteeBank','');
        }else{
          this.$set(this.formData,'prodMod','');
          this.$set(this.formData,'truteeBank','-1');
        }

      },

    },
    created() {
      this.formData = this.info;
    }


  }
</script>

<style scoped>

</style>
