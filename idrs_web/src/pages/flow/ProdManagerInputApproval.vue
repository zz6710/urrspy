<template>
  <div>
    <k-form :data-col="3" data-input-width="560px" data-label-width="180px" data-total-width="1188px" style="width: 100%">
      <k-form-item label="产品代码" >
        <k-field-select v-model="formData.prod_code" :data-disabled="true" data-action="T8Dict.findTaProdInfos"
                        data-display-field="prodCode,prodName" data-value-field="prodCode"/>
      </k-form-item>
      <k-form-item label="资金到账天数">
        <k-field-text v-model="formData.account_day" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="分配日期描述">
        <k-field-text v-model="formData.assignment_date_desc" :data-disabled="true" input-type="textarea" :rows="3"/>
      </k-form-item>
      <k-form-item label="实际到期日">
        <k-field-date v-model="formData.prod_real_close_date" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="具体清算日描述">
        <k-field-text v-model="formData.settlement_date_desc" :data-disabled="true" input-type="textarea" :rows="3"/>
      </k-form-item>

    </k-form>
  </div>
</template>

<script>
export default {
  name: "ProdManagerInputApproval",
  props:{
    dataData:[]
  },
  data(){
    return{
      formData:{},
    }
  },
  created() {
    this.getUrlFormData();
  },
  methods:{
    getUrlFormData(){
      let processId = '';
      if (this.dataData.id===undefined){
        processId = this.dataData[1];
      }else {
        processId = this.dataData.id;
      }
      this.httpUtil.ajax({
        url: 'wf/businessProcess/querySubmitParams.json',
        params: {'processInstanceId':processId},
      }).then(data => {
        let jsonData = JSON.parse(data.data);
        console.log('获取产品经理录入审批-表单数据',jsonData)
        // this.formData = formData.submitFlowData;
        this.formData = JSON.parse(jsonData.submitFlowData);

        this.$set(this.formData,'prod_code',jsonData.prodCode)
        console.log('获取产品经理录入审批-表单数据',this.formData)
        console.log('获取产品经理录入审批-表单数据this',this.formData)
      });
    },
    validateData(){
      return true;
    },
  },
}
</script>

<style scoped>

</style>
