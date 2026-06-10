<template>
  <k-form ref="editForm" :data-col="2">
    <k-form-item label="产品代码">
      <k-field-text v-model="formData.prodCode" :data-disabled="true"
                    :dataAllowblank="false"/>
    </k-form-item>
    <k-form-item label="产品名称">
      <k-field-text v-model="formData.prodName" :data-disabled="true"
                    :dataAllowblank="false"/>
    </k-form-item>
    <k-form-item label="决策类型" v-if="formData.feeMeetingType">
      <k-field-select v-model="formData.feeMeetingType" data-dict="decision_type" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="会议/审批单" v-if="formData.feeMeetingName">
      <k-field-text v-model="formData.feeMeetingName"  :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="决策类型" v-if="formData.decisionType">
      <k-field-select v-model="formData.decisionType" :dataAllowblank="false" :data-disabled="true"
                      data-dict="decision_type"/>
    </k-form-item>
    <k-form-item label="会议/审批单" v-if="formData.meetingId">
      <k-field-select v-model="formData.meetingId" :dataAllowblank="false" :data-disabled="true"
                      data-action="MeetCreate.findMeetDict" data-display-field="meetName" data-value-field="id"/>
    </k-form-item>
    <hr align=center width="500" color=#987cb9 SIZE=3/>
    <k-form ref="feeForm" v-for="(item,index) in formData.feeData" :key="index" :data-col="2">
      <k-form-item label="费用类型">
        <k-field-select v-model="item.feeType" data-dict="fee_type" :data-disabled="true" :dataAllowblank="false"/>
      </k-form-item>
      <k-form-item label="费率">
        <k-field-text v-model="item.rate" data-type="number" data-digits="2" :data-disabled="true"
                      :dataAllowblank="false"/>
      </k-form-item>
      <k-form-item label="付费规则">
        <k-field-select v-model="item.paymentRules" data-dict="t8_payment_rules" :data-disabled="true"
                        :dataAllowblank="false"/>
      </k-form-item>
      <k-form-item label="计提基数">
        <k-field-select v-model="item.chargingIndex" data-dict="t8_charging_index_deal" :data-disabled="true"
                        :dataAllowblank="false"/>
      </k-form-item>
      <k-form-item label="顺延规则">
        <k-field-select v-model="item.postponeRule" data-dict="t8_postpone_rule" :data-disabled="true"
                        :dataAllowblank="false"/>
      </k-form-item>
      <!-- <k-form-item label="费用说明">
        <k-field-text v-model="item.feeDesc" :data-disabled="true" :dataAllowblank="false"/>
      </k-form-item> -->
      <k-form-item label="是否优惠">
        <k-field-radio :data-data="options" :data-disabled="true"
                       @data-on-change="changedIsFeeConcession(item)" v-model="item.isFeeConcession" :dataAllowblank="false"/>
      </k-form-item>
      <k-form-item label="费率优惠到%">
        <k-field-text v-model="item.feeConcessionRate" data-validate-type="number" data-digits="2" :data-disabled="true"
                      :dataAllowblank="item.allowblank" data-min-value="(0" data-max-value="99.99"/>
      </k-form-item>
      <k-form-item label="优惠生效日期">
        <k-field-date v-model="item.concessionStartDate" data-type="date" ref="startDate" :dataAllowblank="item.allowblank":data-disabled="true"/>
      </k-form-item>
      <k-form-item label="优惠失效日期">
        <k-field-date v-model="item.concessionEndDate" data-type="date" ref="endDate" :data-disabled="true"
                      :dataAllowblank="item.allowblank"/>
      </k-form-item>
      <hr align=center width="500" color=#987cb9 SIZE=3/>
    </k-form>



  </k-form>
</template>

<script>
    export default {
        name: "DisplayFeeConcessionConfirm",
      props:{
        formData:{},
      },
      data() {
        return {
          options:[
            {
              value: '1',
              label: '是',
            }, {
              value: '0',
              label: '否'
            }
          ],
        }
      }
    }
</script>

<style scoped>

</style>
