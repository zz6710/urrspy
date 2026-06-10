<template>
  <div >
    <k-form ref="dividendsForm" :data-col="4" data-input-width="220px" data-label-width="180px"
            data-total-width="988px" class="component_height" :style="{height:scrollerHeight}">
      <k-form-item :data-row="2" label="主题" data-input-width="560px" >
        <k-field-text data-placeholder="请输入" input-type="textarea" v-model="formData.title" data-max-length="200" :data-allowblank="false" :data-disabled="dataData.disabled"></k-field-text>
      </k-form-item>
      <k-form-item label="审批事项" data-input-width="560px">
        <k-field-text data-placeholder="请输入" input-type="textarea" v-model="formData.desc" data-max-length="500" :data-allowblank="false" :data-disabled="dataData.disabled"></k-field-text>
      </k-form-item>
      <k-form-item label="产品名称" data-input-width="560px">
        <k-field-text data-placeholder="请输入" input-type="text" v-model="formData.prodName" :data-allowblank="false" data-disabled></k-field-text>
      </k-form-item>
      <k-form-item label="产品代码" data-input-width="560px">
        <k-field-text data-placeholder="请输入" input-type="text" v-model="formData.prodCode" :data-allowblank="false" data-disabled></k-field-text>
      </k-form-item>
      <k-form-item label="分红方式" data-input-width="560px">
        <k-field-select data-placeholder="请选择" input-type="text" data-dict="div_method" v-model="formData.dividendType" :data-allowblank="false" data-disabled/>
      </k-form-item>
      <k-form-item label="当前份额" data-input-width="560px">
        <k-field-text data-placeholder="请输入"  input-type="text" v-model="formData.share" :data-allowblank="false" data-disabled></k-field-text>
      </k-form-item>
      <k-form-item label="分红金额" data-input-width="560px">
        <k-field-text data-placeholder="请输入" v-model="formData.totalAmount"
                      input-type="text" :data-allowblank="false" data-disabled></k-field-text>
      </k-form-item>
      <k-form-item label="分红基准日" data-input-width="560px">
        <k-field-date data-placeholder="请输入" v-model="formData.dividendBaseDate"
                      input-type="text" :data-allowblank="false" data-disabled></k-field-date>
      </k-form-item>
      <k-form-item label="分红除权日" data-input-width="560px">
        <k-field-date v-model="formData.dividendExDate"
                      input-type="text" :data-allowblank="false" data-disabled></k-field-date>
      </k-form-item>
      <k-form-item label="分红权益登记日" data-input-width="560px">
        <k-field-date v-model="formData.dividendRegisterDate"
                      input-type="text" :data-allowblank="false" data-disabled></k-field-date>
      </k-form-item>
      <k-form-item label="红利下发日" data-input-width="560px">
        <k-field-date v-model="formData.dividendIssueDate"
                      input-type="text" :data-allowblank="false" data-disabled></k-field-date>
      </k-form-item>
      <k-form-item label="红利再投日" data-input-width="560px">
        <k-field-date data-placeholder="请输入" v-model="formData.handOutDate"
                      input-type="text" :data-allowblank="false" data-disabled></k-field-date>
      </k-form-item>
      <k-form-item label="分红清算日" data-input-width="560px">
        <k-field-date  v-model="formData.dividendExEndDate"
                       :data-allowblank="false" :data-disabled="dataData.disabled"></k-field-date>
      </k-form-item>
      <k-form-item label="备注" data-input-width="560px">
        <k-field-text data-placeholder="请输入" input-type="textarea" :data-allowblank="true"
                      v-model="formData.remark" :data-disabled="dataData.disabled" data-max-length="500" :rows="3"></k-field-text>
      </k-form-item>
    </k-form>
  </div>
</template>

<script>
  export default {
    name: "dividendsTaskApproval",
    props:{
      dataData:{
        type:Array,
        required:true
      }
    },
    methods: {
      validateData(){
        return this.$refs.dividendsForm.validate();
      },
      getUrlFormData(){
        console.log('this.dataData',this.dataData)
        console.log('url形式:dividendsTaskApproval',this.dataData.id)
        let processId = this.dataData.id;
        this.httpUtil.ajax({
          url: 'wf/businessProcess/querySubmitParams.json',
          params: {'processInstanceId':processId},
        }).then(data => {
          this.formData = JSON.parse(data.data);
          console.log('解析后的值',this.formData)
        });
      },
      saveSubmitParams(){
        let processId = this.dataData.processId;
        console.log('this.formData',this.formData)
        this.httpUtil.ajax({
          url: 'wf/businessProcess/updateSubmitParams.json',
          params: this.formData,
        }).then(data => {

        });
      }
    },
    data() {
      return {
        formData:{},
        clientHeight:1000,
      }
    },
    mounted() {
      const that = this;
      window.screenHeight = document.body.clientHeight
      let element = this.$refs.dividendsForm.$children;
      let sign=0;
      let double =0;
      //获取元素的个数
      for(let i=0;i<element.length;i++){
        if("text"==element[i].$children[0].$options.propsData.inputType || element[i].$children[0].$options.propsData.inputType==undefined){
          sign=sign+1;
        }
        if("textarea"==element[i].$children[0].$options.propsData.inputType){
          double=double+1;
        }
      }
      //根据实际情况 text高度为50 textarea高度为100左右
      that.clientHeight = sign*50+double*100;
    },
    computed: {
      // 滚动区高度
      scrollerHeight: function() {
        return (this.clientHeight + 60) + 'px'; //自定义高度需求
      }
    },
    created() {
      this.getUrlFormData();
    },
    watch: {
      'formData.title':{
        handler: function (newValue,oldValue){
          this.saveSubmitParams();
        }
      },
      'formData.desc':{
        handler: function (newValue,oldValue){
          this.saveSubmitParams();
        }
      },
      'formData.remark':{
        handler: function (newValue,oldValue){
          this.saveSubmitParams();
        }
      },
      //formData.dividendExEndDate
      'formData.dividendExEndDate':{
        handler: function (newValue,oldValue){
          this.saveSubmitParams();
        }
      }
    }
  }
</script>

<style scoped>

  .component_heightdiv{
    display: block;
  }

  /deep/ .component_height .k-form-body {
    overflow-y: visible;
  }
</style>
