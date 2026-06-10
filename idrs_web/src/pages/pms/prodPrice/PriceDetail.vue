<template>
  <div>
    <k-form class="my-form" :data-col="2" dataLabelWidth="170px" dataInputWidth="300px" ref="submitForm">
      <k-form-item label="成立/开放日">
        <k-field-select
          v-model="priceData.prodDate"
          :data-params="{prodCode:prodCode}"
          data-action="T8ProdWorkdays.findProdOpenDays"
          data-display-field="changeDate"
          data-value-field="changeDate"
          :data-disabled="true"
        />
      </k-form-item>
      <k-form-item label="市场端业绩基准">
        <k-field-text v-model="priceData.marketPerformanceOut" :data-max-length="255"
                      :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="综合成本%">
        <k-field-text v-model="priceData.compositeCost" :data-max-length="8" data-digits="4" data-type="number"
                      data-validate-type="number"
                      data-max-value="100" data-min-value="0" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="生效日期">
        <k-field-date v-model="priceData.validateDate"
                      :data-max-value="priceData.invalidateDate==''?'29991230':priceData.invalidateDate"
                      :data-disabled="true">

        </k-field-date>
      </k-form-item>
      <k-form-item label="失效日期">
        <k-field-date v-model="priceData.invalidateDate"
                      :data-min-value="'('+(this.priceData.validateDate==''?this.priceData.prodDate:this.priceData.validateDate)"
                      :data-disabled="true">

        </k-field-date>
      </k-form-item>
      <k-form-item label="决策类型">
        <k-field-select v-model="priceData.decisionType" data-dict="decision_type"
                        :data-disabled="true"></k-field-select>
      </k-form-item>
      <k-form-item label="会议/审批单">
        <k-field-select data-action="QuotaMeeting.findQuotaMeetings"
                        v-model="priceData.meetId"
                        data-display-field="meetingName"
                        data-value-field="id" :data-disabled="true"
                        ref="meetings"></k-field-select>
      </k-form-item>
      <k-form-item label="状态">
        <k-field-select v-model="priceData.confirmStatus" data-dict="confirm_status" :data-disabled="true">

        </k-field-select>
      </k-form-item>
    </k-form>
    <meet :meetId="priceData.meetId"></meet>
  </div>
</template>

<script>
import Meet from "./MeetDetail.vue"
export default {
  name: "PriceDetail.vue",
  components: {Meet},
  props: {
    prodCode: '',
    prodDate: '',
  },
  data() {
    return {
      priceData: {},
    }
  },
  created() {
    //如果产品代码与成立/开放日不为空  查询业绩基准信息
    if (this.prodCode != '' && this.prodDate != '') {
      this.findPriceByCodeAndDate();
    }
  },
  watch: {
    prodCode(value) {
      if (value != '' && this.prodDate != '') {
        this.findPriceByCodeAndDate();
      } else {
        this.priceData = {}
      }
    },
    prodDate(value) {
      if (value != '' && this.prodCode != '') {
        this.findPriceByCodeAndDate();
      } else {
        this.priceData = {}
      }
    }
  },
  methods: {
    //根据产品代码与日期查询业绩基准信息
    findPriceByCodeAndDate() {
      this.httpUtil.comnQuery({
        action: 'T8ProdPrice.findProdPriceInfoByProdCode',
        params: {
          prodCode: this.prodCode,
          prodDate: this.prodDate,
          status: 1
        },
      }).then(data => {
        if (data.rows.length > 0) {
          this.priceData = data.rows[0];
        }
      });
    }
  }
}
</script>

<style scoped>

</style>
