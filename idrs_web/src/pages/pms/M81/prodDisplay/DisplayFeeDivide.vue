<template>
  <k-form ref="baseInfoForm" :data-col="0" dataLabelWidth="170px" dataInputWidth="300px" data-total-width="1100">
    <k-form-item label="销售商代码" >
      <k-field-select v-model="detailData.distributorCode" data-action="T8Dict.findTaDistributorInfos"
                      :dataAllowblank='false' :data-disabled="true"
                      data-display-field="distributorName"  data-value-field="distributorCode"  />
    </k-form-item>
    <k-form-item label="费用类型">
      <k-field-select data-dict="t8_fee_divide_fee_type" :value="detailData.feeType" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="分成方式">
      <k-field-select data-dict="divide_type" :value="detailData.divideType" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="启用日期">
      <k-field-text data-type="date" :value="detailData.enableDate" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="归管理人比例(%)" v-show="!detailData.switchDetailSegmentValue && detailData.divideType == '1'">
      <k-field-text :value="detailData.managerDivideRate" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="归销售商比例(%)" v-show="!detailData.switchDetailSegmentValue && detailData.divideType == '1'">
      <k-field-text :value="detailData.distributorDivideRate" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="归销售商固定BP" v-show="!detailData.switchDetailSegmentValue && detailData.divideType == '2'">
      <k-field-text :value="detailData.distributorDivideBp" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="产品代码">
      <k-field-select v-model="detailData.prodCode" data-action="T8Dict.findNotEstablishProdInfos" :data-disabled="true"
                      data-display-field="prodCode,prodName" data-value-field="prodCode" data-multiple="true"/>
    </k-form-item>
    <k-form ref="t8ProdPerformanceForm" v-show="detailData.switchDetailSegmentValue" :data-col="0" dataLabelWidth="170px"
            dataInputWidth="300px" data-total-width="1100">
      <div label="份额分段展示"  class="my-table" v-show="detailData.switchDetailSegmentValue">
        <md-table style="width: 900px;margin-left: 60px;text-align: center">
          <md-table-row>
            <md-table-head style="text-align: center">份额段</md-table-head>
            <md-table-head style="text-align: center" v-show="detailData.divideType=='1'">归管理人比例(%)</md-table-head>
            <md-table-head style="text-align: center" v-show="detailData.divideType=='1'">归销售商比例(%)</md-table-head>
            <md-table-head style="text-align: center" v-show="detailData.divideType=='2'">归销售商固定BP</md-table-head>
          </md-table-row>
          <md-table-row v-for="(item,index) in detailData.tailingCommisionMoneyList" :key="index">
            <md-table-cell>
              {{item.moneyDesc}}
            </md-table-cell>
            <md-table-cell  v-show="detailData.divideType=='1' ">
              {{item.managerDivideRate}}
              <span class="md-suffix">%</span>
            </md-table-cell>
            <md-table-cell  v-show="detailData.divideType=='1' ">
              {{item.distributorDivideRate}}
              <span class="md-suffix">%</span>
            </md-table-cell>
            <md-table-cell v-show="detailData.divideType=='2'">
              {{item.distributorDivideBp}}
            </md-table-cell>
          </md-table-row>
        </md-table>

      </div>
    </k-form>
  </k-form>
</template>

<script>
    export default {
        name: "DisplayFeeDivide",
      props:{
        detailData:{},
      },
      data(){
          return{

          }
      }
    }
</script>

<style scoped>

</style>
