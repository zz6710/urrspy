<template>
  <div>
    <k-form ref="editT8ProdQuotaForm" :data-col="2" data-input-width="160px" data-label-width="120px">

      <k-form-item label="产品代码">
        <k-field-select
          v-model="quota.prodCode"
          data-multiple="false"
          data-action="T8ProdInfo.getProdInfos"
          data-value-field="prodCode"
          data-display-field="prodName"
          data-disabled="true"
        ></k-field-select>
      </k-form-item>
      <k-form-item label="成立/开放日">
        <k-field-select
          v-model="quota.quotaDate"
          :data-params="{prodCode:quota.prodCode}"
          data-action="T8ProdWorkdays.findProdOpenDays"
          ref="openDays"
          data-display-field="changeDate"
          data-value-field="changeDate"
          data-disabled="true"
        />
      </k-form-item>
      <k-form-item label="销售总额度(亿)">
        <k-field-text v-model="quota.totalSaleQuota"
                      data-validate-type="money" data-min-value="(0" data-max-value="999999.99"
                      data-disabled="true"
        />
      </k-form-item>
      <k-form-item label="状态">
        <k-field-text v-model="quota.confirmStatusName" data-disabled="true">

        </k-field-text>
      </k-form-item>
      <k-form-item label="决策类型">
        <k-field-select v-model="quota.decisionType" data-dict="decision_type"
                        data-disabled="true"
        ></k-field-select>
      </k-form-item>
      <k-form-item label="会议/审批单">
        <k-field-select data-action="QuotaMeeting.findQuotaMeetings"
                        v-model="quota.meetingId"
                        data-display-field="meetingName"
                        data-value-field="id"
                        ref="meetings"
                        data-disabled="true"
        ></k-field-select>
      </k-form-item>
    </k-form>
    <meet :meetId="quota.meetingId"></meet>
    <k-grid ref="t8DistributorQuotaManageGrid" data-action="T8DistributorQuotaManage.findTotalDeptQuota"
            :data-autoload="false" @data-row-select="selectRow" :dataPopupAppendToBody="true">
      <k-grid-column data-header="销售部门" data-name="managerDept" data-dict="manager_dept"></k-grid-column>
      <k-grid-column data-header="申请额度(亿)" data-name="totalDeptQuota" data-type="money"></k-grid-column>
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" data-descript="查看销售商额度详情" data-functype="POPUP"
               data-size="mini" data-target="salesQuotaDetailPopup">
          <md-icon>zoom_in</md-icon>
        </k-btn>
      </template>
    </k-grid>
    <k-popup ref="salesQuotaDetailPopup" data-title="部门销售商额度需求信息" @data-opened="refreshSalesGrid"
             :data-dialog-drag="true" :dataAppendToBody="true">
      <k-grid ref="t8SalesQuotaDetailGrid" data-action="T8DistributorQuotaManage.findQuotaListByDeptNo"
              data-operate-column="false" :data-autoload="false" :dataPopupAppendToBody="true">
        <k-grid-column data-header="销售商名称" data-name="distributorName"></k-grid-column>
        <k-grid-column data-header="所属部门" data-name="managerDept" data-dict="manager_dept"></k-grid-column>
        <k-grid-column data-header="申请额度(亿)" data-name="quota" data-type="money"></k-grid-column>
        <k-grid-column data-header="确认状态" data-name="confirmStatus" data-dict="confirm_status"></k-grid-column>
        <k-grid-column data-header="创建人" data-name="crtUserName"></k-grid-column>
        <k-grid-column data-header="创建日期" data-name="crtDate"></k-grid-column>
        <k-grid-column data-header="创建时间" data-name="crtTime"></k-grid-column>
      </k-grid>
    </k-popup>
  </div>
</template>

<script>
import Meet from "./MeetDetail"
export default {
  name: "ProdQuotaDetail.vue",
  components: {Meet},
  props: {
    prodCode: '',
    prodDate: '',
  },
  data() {
    return {
      quota: {},
    }
  },
  created() {
    //如果产品代码与成立/开放日不为空  查询对应的总额度信息
    if (this.prodCode != '' && this.prodDate != '') {
      this.findQuotaByCodeAndDate();
    }
  },
  watch: {
    prodCode(value) {
      if (value != '' && this.prodDate != '') {
        this.findQuotaByCodeAndDate(value, this.prodDate);
      } else {
        this.quota = {}
        this.$refs.t8DistributorQuotaManageGrid.data = [];
      }
    },
    prodDate(value) {
      if (value != '' && this.prodCode != '') {
        this.findQuotaByCodeAndDate(this.prodCode, value);
      } else {
        this.quota = {}
        this.$refs.t8DistributorQuotaManageGrid.data = [];
      }
    }
  },
  methods: {
    //根据产品代码与日期查询总额度信息
    findQuotaByCodeAndDate(prodCode, prodDate) {

      this.httpUtil.comnQuery({
        action: 'T8ProdQuota.findT8ProdQuotas',
        params: {
          prodCode: prodCode,
          quotaDate: prodDate,
          status: 1
        }
      }).then(data => {
        if (data.rows.length > 0) {
          this.quota = data.rows[0];
          this.$refs.t8DistributorQuotaManageGrid.load({totalQuotaId: this.quota.id, status: 1});
        } else {
          this.$refs.t8DistributorQuotaManageGrid.data = [];
        }
      });
    },
    //部门销售额度被选中事件
    selectRow(row) {
      const _this = this;
      _this.selectRowData = Object.assign({}, row);
      this.managerDept = _this.selectRowData.managerDept;
      this.totalQuotaId = _this.selectRowData.totalQuotaId;
    },
    //刷新部门销售商详情列表
    refreshSalesGrid() {
      this.$refs.t8SalesQuotaDetailGrid.load({
        managerDept: this.managerDept,
        totalQuotaId: this.totalQuotaId,
        status: 1
      });
    },
  },
}
</script>

<style scoped>

</style>
