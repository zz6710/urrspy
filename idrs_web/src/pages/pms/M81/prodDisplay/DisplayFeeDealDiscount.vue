<template>
  <div>
    <k-form ref="editForm" :data-col="2">
      <k-form-item label="交易费用类型">
        <k-field-select v-model="T8FeeDealDiscount.feeTypeDeal" data-dict="t8_fee_type_deal" :data-disabled="true"/>
      </k-form-item>
      <!-- <k-form-item label="交易客户类型">
        <k-field-select v-model="T8FeeDealDiscount.custTypeDeal" data-dict="t8_cust_type_deal"/>
      </k-form-item> -->
      <k-form-item label="优惠比例（%）" v-show="T8FeeDealDiscount.isShowDiscountFee">
        <k-field-text v-model="T8FeeDealDiscount.discountProportion" data-validate-type="number" data-digits="2"
                         data-min-value="0" data-max-value="100" :dataAllowblank="!T8FeeDealDiscount.isShowDiscountFee" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="优惠生效日期">
        <k-field-date v-model="T8FeeDealDiscount.validateDate"
                         data-type="date"
                         ref="startDate" :data-disabled="true"
        />
      </k-form-item>
      <k-form-item label="优惠失效日期">
        <k-field-date v-model="T8FeeDealDiscount.invalidateDate"
                         data-type="date" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="基本费率(%)" v-show="!(T8FeeDealDiscount.switchSegmentValue||T8FeeDealDiscount.switchTimeValue)">
        <k-field-text v-model="T8FeeDealDiscount.baseFeeRate" :data-max-length="8" data-integer-length="3" data-max-value="100"
                         data-digits="4"
                         data-min-value="0" data-validate-type="money" data-type="money" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="优惠后费率(%)" v-show="!(T8FeeDealDiscount.switchSegmentValue||T8FeeDealDiscount.switchTimeValue)">
        <k-field-text v-model="T8FeeDealDiscount.feeRateAfterDiscount" :data-max-length="8" data-integer-length="3" data-max-value="100"
                         data-digits="4"
                         data-min-value="0" data-validate-type="money" data-type="money"  :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="决策类型">
        <k-field-select v-model="T8FeeDealDiscount.decisionType" data-dict="decision_type" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="会议/审批单">
        <k-field-select v-model="T8FeeDealDiscount.meetingId" data-action="QuotaMeeting.findQuotaMeetings"
                         data-display-field="meetingName" data-value-field="id" :data-disabled="true"/>
      </k-form-item>
    </k-form>
    <div label="金额分段展示"  class="my-table" v-show="T8FeeDealDiscount.switchSegmentValue || T8FeeDealDiscount.switchTimeValue"
         style="margin-top: 20px;margin-right: 35%" >
      <md-table>
        <md-table-row >
          <md-table-head v-if="T8FeeDealDiscount.moneyList.length > 0 " style="text-align:center;">金额段</md-table-head>
          <md-table-head v-if="T8FeeDealDiscount.timeList.length > 0"  style="text-align:center;">持有时间</md-table-head>
          <md-table-head style="text-align:center;">{{T8FeeDealDiscount.feeTableHead}}</md-table-head>
          <md-table-head style="text-align:center;">{{T8FeeDealDiscount.feeTableDiscount}}</md-table-head>
          <md-table-head style="text-align:center;">优惠后费率</md-table-head>
        </md-table-row>
        <md-table-row v-show="T8FeeDealDiscount.timeList.length > 0 || T8FeeDealDiscount.moneyList.length > 0"
                      v-for="(item,index) in T8FeeDealDiscount.tailingCommisionList" :key="index">
          <md-table-cell v-if="item.showMoneyTd" :rowspan="item.moneyRowspan" style="text-align: center">{{ item.moneyDesc }}</md-table-cell>
          <md-table-cell v-if="item.showTimeTd" style="text-align: center">{{ item.timeDesc }}</md-table-cell>
          <md-table-cell>
            <md-field style="width:70%;">
              <span class="md-suffix" v-if="T8FeeDealDiscount.ProdFeeDeal3.chargeType=='0'||T8FeeDealDiscount.ProdFeeDeal3.chargeType==''">￥</span>
              <md-input class="md-input" style="text-align: center;width:60px;" v-model="item.amtRate" maxlength="3" md-input-type="number" :disabled="true"
                        v-show="T8FeeDealDiscount.ProdFeeDeal3.chargeType=='1'"></md-input>
              <md-input class="md-input" style="text-align: center;width:60px" v-model="item.constantFee" md-input-type="number" :disabled="true"
                        v-show="T8FeeDealDiscount.ProdFeeDeal3.chargeType=='0'||T8FeeDealDiscount.ProdFeeDeal3.chargeType==''"></md-input>
              <span class="md-suffix" v-if="T8FeeDealDiscount.ProdFeeDeal3.chargeType=='1'">%</span>
            </md-field>
          </md-table-cell>
          <md-table-cell>
            <md-field style="width:70%;text-align:center;" >
              <span class="md-suffix" v-if="T8FeeDealDiscount.ProdFeeDeal3.chargeType=='0'||T8FeeDealDiscount.ProdFeeDeal3.chargeType==''">￥</span>
              <md-input class="md-input text-align-center" style="text-align: center;width:60px;padding-right:0px !important;" v-model="item.discountRate"  data-max-value="100"
                            v-show="T8FeeDealDiscount.ProdFeeDeal3.chargeType=='1'" data-min-value="0" data-integer-length="3" data-validate-type="number"
                            data-type="number" data-digits="4" :data-disabled="true"/>
              <!--              <md-input class="md-input" style="text-align: center;width:60px;" v-model="item.discountRate"  md-input-type="number" @input="" :disabled="false" v-show="ProdFeeDeal3.chargeType=='1'"></md-input>-->
              <span class="md-suffix" v-if="T8FeeDealDiscount.ProdFeeDeal3.chargeType=='1'">%</span>
            </md-field>
          </md-table-cell>
          <md-table-cell>
            <md-field style="width:70%">
              <span class="md-suffix" v-if="T8FeeDealDiscount.ProdFeeDeal3.chargeType=='0'||T8FeeDealDiscount.ProdFeeDeal3.chargeType==''">￥</span>
              <md-input v-model="item.amtRateAfterDiscount"  class="md-input" style="text-align: center;width:60px;" :disabled="true"></md-input>
              <span class="md-suffix" v-if="T8FeeDealDiscount.ProdFeeDeal3.chargeType=='1'">%</span>
            </md-field>
          </md-table-cell>
        </md-table-row>
      </md-table>
    </div>
  </div>
</template>

<script>
  export default {
    name: "DisplayFeeDealDiscount",
    props:{
      T8FeeDealDiscount:{
        moneyList: [],
        timeList:[],
        feeTableHead: "费率",
        feeTableDiscount: "优惠比例",
        isShowDiscountFee:false,
        decisionType:"",
        prodCode:"",
      },
    },
    data(){
      return{
      }
    },
    created() {
    }
  }
</script>

<style scoped>

</style>
