<template>
  <div style="height: 1500px;">
    <k-form ref="addForm" :data-col="2">
      <k-form-item label="公告ID">
        <k-field-text v-model="formData.id" :data-disabled="true"/>


      </k-form-item>
	  <k-form-item label="创建时间">
	    <k-field-text v-model="formData.crtDate" :data-disabled="true"/>
	  </k-form-item>
	  <k-form-item label="创建人">
	    <k-field-text v-model="formData.crtUser" :data-disabled="true"/>
    </k-form-item>
      <k-form-item label="公告标题" :data-col="2">
        <k-field-text v-model="formData.title" :data-max-length="100" :dataAllowblank="false"/>
      </k-form-item>
      <k-form-item label="公告文件" :data-col="2">
        <k-field-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit=1
                        :data-error="onSubmitError" :data-success="onSubmitSuccess" @data-before-upload="beforeUpload"
                        :data-auto-upload="false"
                        data-upload-url="/upload/server/PmsApp/M8DisclosureManual/fileUpload.json">
        </k-field-upload>
      </k-form-item>
      <k-form-item label="信披类型" :data-col="2">
        <k-field-select v-model="formData.type" data-dict="xp_doc_type" style="width: 60%;" :dataAllowblank="false"/>
      </k-form-item>
      <k-form-item label="信披子类型" :data-col="2">
        <k-field-select v-model="formData.sonType" data-dict="xp_son_type" style="width: 60%;"/>
      </k-form-item>
      <k-form-item label="选择产品" :data-col="2">

        <k-field-select v-model="formData.prodCode" data-action="T8Dict.findTaProdInfos" style="width: 60%;"
                        data-display-field="prodCode,prodName" data-value-field="prodCode" :dataAllowblank="false"/>
        (单选)
      </k-form-item>

      <k-form-item label="计划发布日期" :data-col="2">
        <k-field-date v-model="formData.startEstablishDate" data-type="date" style="width: 60%;"
                      :dataAllowblank="false"/>
      </k-form-item>
      <k-form-item label="发送邮箱" :data-col="2">
        <!--<k-field-select v-model="formData.sendEmail" style="width: 60%;" data-action="T8DisChannelInfo.findChannelName"
                        data-display-field="channel_name" data-value-field="channel_type"/>(单选)-->
        <k-field-select v-model="formData.sendEmail" data-default-value="0" data-dict="t8_disclosure_channel_type" style="width: 60%;"
                        :dataAllowblank="false" :data-disabled="true"/>(单选)
      </k-form-item>
      <!--<k-form-item label="信披渠道" :data-col="2" :dataAllowblank="false">
        <k-field-checkbox v-model="formData.channel" :data-data="options" :dataAllowblank="false"/>
      </k-form-item>-->
<!--      <k-form-item label="信披渠道" :data-col="2">-->
<!--        <k-field-checkbox v-model="formData.channel" :data-allowblank="false"-->
<!--                          data-action="T8DisChannelInfo.findDisChannel" :data-params="{channelType:1}"-->
<!--                          data-display-field="channelName" data-value-field="id"/>-->
<!--      </k-form-item>-->


        <k-form-item label="备注说明" :data-col="2">
          <k-field-text v-model="formData.note" input-type="textarea" :data-max-length="200"/>
        </k-form-item>

      <k-form-footer data-align="center" >
        <k-btn class="btn-custom-primary"
                :data-handler="saveRule"
               data-from="addForm" :data-model="formData"
               data-target="t8ObjectGrid">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>提交
        </k-btn>
      </k-form-footer>
    </k-form>
  </div>


</template>

<script>
import {assign} from "lodash";
import Tools from "@/utils/tools";

export default {
  name: "",
  data() {
    return {
      options:[
        /*{label:'中国光大银行官网',value:'1'},
        {label:'光大理财官网',value:'2'},
        {label:'光大银行销售渠道',value:'3'},
        {label:'光大理财直销渠道',value:'4'},
        {label:'行外代销机构',value:'5'},
        {label:'专户机构邮箱',value:'6'},*/
      ],
      prodSearchParam: {
        prodCode: '',
      },
      formData: {
        prodCode: '',
        prodName: '',
        feeJson:'',
        id:'系统自动生成',
        crtDate:new Date().toLocaleString(),//当前时间
        crtUser:'',
        startEstablishDate:'',
        sendEmail:'',
        note:'',

      },
      modelData:[
        {text:'1',value:"待提供数据"},
      ],
    }
  },
  watch:{
  },
  created() {
    Tools.getLoginUser().then(res => {
      this.formData.crtUser=res.username;
    })
  },
  methods: {
    beforeUpload(){
      return false;
    },
    onSubmitError() {
      this.$refs.uploadRef.doReset();
      this.$refs.submitBtn.setIconStyle(1, [])
    },

    onSubmitSuccess() {
      var aas =this.$refs.uploadRef.doReset();
      this.$refs.uploadRef.doReset();
      this.$refs.addForm.reset();
      Tools.getLoginUser().then(res => {
        this.formData.crtUser=res.username;
      })
      this.formData.id='系统自动生成';
      this.formData.crtDate=new Date().toLocaleString();//当前时间



    },
    saveRule(params){
      let validateResult = this.$refs.addForm.validate();
      if (validateResult) {
        let temp = document.getElementsByClassName('upload-demo');
        let lis = temp[0].childNodes[1].childNodes.length;
        let formData = this.formData;
        this.$refs.addForm.validate();
        if(lis==0){
          Tools.alert("请选择需要上传的文件","danger");
          return false;
        }
        this.$refs.uploadRef.upload(formData);
      }

    },
  }
}
</script>

<style scoped>

</style>
