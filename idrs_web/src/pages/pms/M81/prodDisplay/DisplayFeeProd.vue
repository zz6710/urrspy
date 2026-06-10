<template>
    <div style="min-height:225px;">
      <k-grid ref="t8FeeProdGrid" @data-row-select="selectRow" :data-operate-column="false" :dataPopupAppendToBody="true">
        <k-grid-column data-header="行数" data-name="numId" :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="产品代码" data-name="prodCode" :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="费用类型" data-name="feeType" data-dict="fee_type"></k-grid-column>
        <k-grid-column data-header="境内托管行费率%" data-name="domesticRate"></k-grid-column>
        <k-grid-column data-header="费率%" data-name="rate"></k-grid-column>
        <!--<k-grid-column data-header="首次计提日期" data-name="firstProvisionDate"></k-grid-column>-->
        <k-grid-column data-header="付费规则" data-name="paymentRules" data-dict="t8_payment_rules"></k-grid-column>
        <!--<k-grid-column data-header="计提结束日期" data-name="provisionEndDate"></k-grid-column>-->
        <k-grid-column data-header="计费基数" data-name="chargingIndex" data-dict="t8_charging_index"></k-grid-column>
        <k-grid-column data-header="顺延规则" data-name="postponeRule" data-dict="t8_postpone_rule"></k-grid-column>
        <k-grid-column data-header="创建日期" data-name="crtDate" :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="创建人" data-name="crtUser" :data-hidden="true"></k-grid-column>
      </k-grid>
    </div>
</template>

<script>
import Tools from "@/utils/tools";
  export default {
    computed: {},
    model: {
      prop: 'ProdFee',
      event: 'input'
    },
    props:{
      ProdFee: {
        dataParams: [],
      },
      prodCode: {
        type: String,
        default: ''
      },
      t8ProdInfoId: {
        type: String,
        default: ''
      },
    },
    data() {
      return {
        ProdFeeUpt: {},
        ProdFeeAdd: {},
        selectRowData: {},
        dataParams:[],
      };
    },
    mounted(){
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.ProdFeeUpt = Object.assign({}, row)
      },
      uptDataParams(dataParams){
        for(let i = 0;i < dataParams.length; i++){
          this.$set(dataParams[i],'numId',i)
        }
        this.dataParams = dataParams;
        this.$set(this.$refs.t8FeeProdGrid, 'list', this.dataParams);
      },
    },
    watch: {
      'ProdFee.dataParams': function (value) {
        this.uptDataParams(value);
      },
    },
    created() {
      this.$nextTick(() => {
        this.uptDataParams(this.ProdFee.dataParams);
      })
    }
  };
</script>
<style lang="scss" scoped>
.add-btn-div{
  position: relative;
  z-index: 1;
}
.add-btn{
  background-color: #4caf50;
  border-radius: 20px;
  box-shadow: 0 4px 5px 0 rgba(76,175,80,0.14), 0 1px 10px 0 rgba(76,175,80,0.12), 0 2px 4px -1px rgba(76,175,80,0.2);
  width: 20px;
  height: 20px;
  line-height: 20.5px;
  font-size: 23px;
  font-weight: 400;
  cursor: pointer;
  color: #FFF;
  text-align: center;
}
</style>
