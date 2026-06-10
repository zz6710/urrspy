<template>
  <div class="formPanel" ref="formPanel" style="margin-left: 15%;">
    <div class="form-item prod-panel" >
      <div class="title">
      </div>
      <DisplayFeeDealDiscount ref="baseInfoForm" v-model="T8FeeDealDiscount" :T8FeeDealDiscount="T8FeeDealDiscount"/>
    </div>
  </div>
</template>

<script>
  import DisplayFeeDealDiscount         from "../../M81/prodDisplay/DisplayFeeDealDiscount.vue"
  export default {
    name: "FeeDealDiscountFlow",
    components: {
      DisplayFeeDealDiscount
    },
    props:{
      taskInfo: {},
    },
    data() {
      return {
        T8FeeDealDiscount:{
          moneyList: [],
          timeList:[],
          feeTableHead: "费率",
          feeTableDiscount: "优惠比例",
          isShowDiscountFee:false,
          decisionType:"",
          prodCode:"",
        },
      }
    },
    created() {
      this.httpUtil
        .ajax({
          url: "/wf/formData/getSubmitParamsByProcessInstanceId.json",
          params: {processInstanceId: this.taskInfo.processInstanceId,_wfProcessInstanceId:this.taskInfo.processInstanceId}
        }).then(res => {
        if (res.data) {
          this.T8FeeDealDiscount = JSON.parse(res.data.submitParams);
          let SegmentBool = this.T8FeeDealDiscount.switchSegmentValue;
          let TimeBool = this.T8FeeDealDiscount.switchTimeValue;
          if(SegmentBool == 'true' || SegmentBool == true){
            this.T8FeeDealDiscount.switchSegmentValue = true;
          }else{
            this.T8FeeDealDiscount.switchSegmentValue = false;
          }
          if(TimeBool == 'true' || TimeBool == true){
            this.T8FeeDealDiscount.switchTimeValue = true;
          }else{
            this.T8FeeDealDiscount.switchTimeValue = false;
          }
          if (this.T8FeeDealDiscount.tailingCommisionList) {
            this.$set(this.T8FeeDealDiscount,'feeTableDiscount',"优惠比例");
            let tailingCommisionList = JSON.parse(this.T8FeeDealDiscount.tailingCommisionList);
            this.$set(this.T8FeeDealDiscount,'tailingCommisionList',tailingCommisionList);
          }
          if (this.T8FeeDealDiscount.ProdFeeDeal3) {
            let ProdFeeDeal3 = JSON.parse(this.T8FeeDealDiscount.ProdFeeDeal3);
            this.$set(this.T8FeeDealDiscount,'ProdFeeDeal3',ProdFeeDeal3);
          }
          if (this.T8FeeDealDiscount.moneyList) {
            this.T8FeeDealDiscount.moneyList = JSON.parse(this.T8FeeDealDiscount.moneyList);
          }
          if (this.T8FeeDealDiscount.timeList) {
            this.T8FeeDealDiscount.timeList = JSON.parse(this.T8FeeDealDiscount.timeList);
          }
        }
      });

    },
  }
</script>

<style lang="scss" scoped>
  @import "../../../../styles/T81001.scss";
</style>
