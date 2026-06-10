<template>
  <div>
    <k-form :data-col="3" data-input-width="560px" data-label-width="180px" data-total-width="1188px" style="width: 100%">
      <k-form-item label="产品代码" >
        <k-field-text v-model="formData.prod_code" :data-disabled="true"
                      data-action="T8Dict.findTaProdInfos"
                      data-display-field="prodCode,prodName" data-value-field="prodCode"/>
      </k-form-item>
      <!--产品经理录入的数据-->
      <k-form-item label="资金到账天数">
        <k-field-text v-model="formData.accountDay" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="分配日期描述">
        <k-field-text v-model="formData.assignmentDateDesc" :data-disabled="true" input-type="textarea" :rows="3"/>
      </k-form-item>
      <k-form-item label="实际到期日">
        <k-field-date v-model="formData.prodRealCloseDate" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="具体清算日描述">
        <k-field-text v-model="formData.settlementDateDesc" :data-disabled="true" input-type="textarea" :rows="3"/>
      </k-form-item>
      <!--投资经理录入的数据-->
      <k-form-item label="资金是否已全部变现并到账">
        <k-field-radio v-model="formData.isAccount" data-dict="1yes0no" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品总资产">
        <k-field-text v-model="formData.totalAssets" :data-disabled="true"/>
      </k-form-item>
      <!--运营支持岗录入的数据-->
      <k-form-item label="清盘日">
        <k-field-date v-model="formData.liquidate" :data-disabled="true"/>
      </k-form-item>
      <!--运营估值岗录入的数据-->
      <k-form-item label="清盘分配本金">
        <k-field-text v-model="formData.corpus" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="清盘分配收益">
        <k-field-text v-model="formData.income" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="清盘分配总金额">
        <k-field-text v-model="formData.total_amount" :data-disabled="true"/>
      </k-form-item>

    </k-form>
  </div>
</template>

<script>
export default {
  name: "BusinessManagerInputApproval",
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
    //businessManagerInputApproval
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
        this.formData = JSON.parse(jsonData.submitFlowData);
        this.httpUtil.comnQuery({
          action:'ProdLiquidation.findProdLiquidationInfo',
          params:{
            prodCode:jsonData.prodCode
          }
        }).then(res=>{
          console.log('获取产品经理录入的清盘数据',res.returndata.data)
          let data = res.returndata.data;
          this.$set(this.formData,'accountDay',data.accountDay);
          this.$set(this.formData,'assignmentDateDesc',data.assignmentDateDesc);
          this.$set(this.formData,'prodRealCloseDate',data.prodRealCloseDate);
          this.$set(this.formData,'settlementDateDesc',data.settlementDateDesc);
          this.$set(this.formData,'isAccount',data.isAccount);
          this.$set(this.formData,'totalAssets',data.totalAssets);
          this.$set(this.formData,'liquidate',data.liquidate);
        })
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
