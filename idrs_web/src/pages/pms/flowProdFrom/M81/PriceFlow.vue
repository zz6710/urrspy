<template>
  <div class="md-card k-card md-theme-default parent-div">
    <div class="md-card-header md-card-header-text md-card-header-green" id="mainPanel">
      <div class="form-item prod-panel" id="quota">
        <div class="title">
          <div class="prod-items"></div>
          <k-field-display class="title-desc" value="额度信息"></k-field-display>
        </div>
        <quota :prodCode="prodCode" :prodDate="prodDate"></quota>
      </div>
      <div class="form-item prod-panel" id="price">
        <div class="title">
          <div class="prod-items"></div>
          <k-field-display class="title-desc" value="业绩基准信息"></k-field-display>
        </div>
        <price :prodCode="prodCode" :prodDate="prodDate"></price>
      </div>
      <div class="form-item prod-panel" id="fee">
        <div class="title">
          <div class="prod-items"></div>
          <k-field-display class="title-desc" value="费用优惠信息"></k-field-display>
        </div>
        <fee :prodCode="prodCode" :prodDate="prodDate"></fee>
      </div>
    </div>
  </div>
</template>

<script>
import Quota from '../../prodPrice/ProdQuotaDetail.vue'
import Price from '../../prodPrice/PriceDetail.vue'
import Fee from '../../prodPrice/FeeconcessionDetail.vue'
import Tools from "@/utils/tools";

export default {
  name: "PriceFlow",
  components: {Quota, Price, Fee},
  props: {
    taskInfo: {}
  },
  data() {
    return {
      prodCode: '',//产品代码
      prodDate: '',//成立/开放日
      saveLoading: false,

    }
  },
  created() {
    this.httpUtil
      .ajax({
        url: "/wf/formData/getSubmitParamsByProcessInstanceId.json",
        params: {
          processInstanceId: this.taskInfo.processInstanceId,
          _wfProcessInstanceId: this.taskInfo.processInstanceId
        }
      }).then(res => {
      if (res.data) {
        let returnData = JSON.parse(res.data.submitParams);
        this.prodCode = returnData.prodCode;
        this.prodDate = returnData.prodDate;
      }
    });

  },
  methods: {
    //查询条件产品代码改变事件
    changeProCode() {
      this.prodDate = '';
      this.$refs.prodDates.load({prodCode: this.prodCode});
    },
  }
}
</script>

<style lang="scss" scoped>

@import "../../../../styles/T81001.scss";

</style>
