<template>
  <div>
    <k-grid ref="t8FeeConcessionDetailsGrid" :data-autoload="false" data-action="T8FeeConcession.find"
            :dataPopupAppendToBody="true">
      <k-grid-column data-align="center" data-header="成立/开放日" data-name="prodDate"/>
      <k-grid-column data-align="center" data-header="费用类型" data-name="feeType" data-dict="fee_type"/>
      <k-grid-column data-align="center" data-header="是否有优惠" data-name="isFeeConcession" data-dict="1yes0no"/>
      <k-grid-column data-align="center" data-header="费用优惠到%" data-type="number" data-digits="2"
                     data-name="feeConcessionRate"/>
      <k-grid-column data-align="center" data-header="优惠生效日期" data-type="date" data-name="concessionStartDate"/>
      <k-grid-column data-align="center" data-header="优惠失效日期" data-type="date" data-name="concessionEndDate"/>
      <k-grid-column data-align="center" data-header="确认状态" data-dict="confirm_status" data-name="confirmStatus"/>
      <k-grid-column data-align="center" data-header="决策类型" data-name="decisionType" data-dict="decision_type"/>
      <k-grid-column data-align="center" data-header="决策名称" data-name="meetingName"/>
      <template slot="operate" slot-scope="scope">
        <k-btn data-functype="POPUP" data-confirm data-size="mini" class="md-info md-just-icon md-simple"
               data-target="meetAttach" :data-handler="toParams" data-descript="管理会议附件信息">
          <md-icon>cloud_download</md-icon>
        </k-btn>
      </template>
    </k-grid>
    <k-popup ref="meetAttach" :dataAppendToBody="true">
      <meet :meetId="meetingId"></meet>
    </k-popup>
  </div>
</template>

<script>
import KPopup from "@/components/k-element/k-popup/k-popup";
import Meet from "./MeetDetail.vue"

export default {
  name: "FeeconcessionDetail.vue",
  components: {KPopup, Meet},
  props: {
    prodCode: '',
    prodDate: '',
  },
  data() {
    return {
      meetingId: '',//会议id
    }
  },
  watch: {
    prodCode(value) {
      if (value != '' && this.prodDate != '') {
        this.$refs.t8FeeConcessionDetailsGrid.load({
          prodCode: this.prodCode,
          prodDate: this.prodDate,
          status: 1
        });
      } else {
        this.$refs.t8FeeConcessionDetailsGrid.data = [];
      }
    },
    prodDate(value) {
      if (value != '' && this.prodCode != '') {
        this.$refs.t8FeeConcessionDetailsGrid.load({
          prodCode: this.prodCode,
          prodDate: this.prodDate,
          status: 1
        });
      } else {
        this.$refs.t8FeeConcessionDetailsGrid.$data.list = [];
        console.log(this.$refs.t8FeeConcessionDetailsGrid);
      }
    }
  },
  created() {

  },
  methods: {
    toParams: function (row) {
      this.meetingId = row.meetingId;
    },
  },
}
</script>

<style scoped>

</style>
