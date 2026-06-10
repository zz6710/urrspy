<template>
  <div>


    <!--    添加产品发行登记信息管理弹出框   -->
    <k-form ref="updateAuditStatusForm" :data-col="2" isFormBodyScreen>
      <k-form-item label="报表类型">
        <k-field-text v-model="infoPop.tableName" :data-allowblank="false" data-dict="subm_isTrue" data-disabled="true"/>
      </k-form-item>
      <k-form-item label="报送起始日期" data-label-width="100px">
        <k-field-date v-model="queryParamDateRange" data-type="daterange" data-date-format="yyyy-MM-dd"
                      data-value-format="yyyyMMdd" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="复核状态">
        <k-field-select v-model="infoPop.auditStatus" data-dict="xp_disclosure_check_status" data-default-value="0"/>
      </k-form-item>
      <!--        <k-form-item label="起始日期">-->
      <!--          <k-field-date v-model="infoPop.startDate" :data-allowblank="false"/>-->
      <!--        </k-form-item>-->
      <!--        <k-form-item label="结束日期">-->
      <!--          <k-field-date v-model="infoPop.endDate" :data-allowblank="false"/>-->
      <!--        </k-form-item>-->

      <k-form-footer slot="footer" data-align="center">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT"
               data-from="updateAuditStatusForm"
               data-action=""
               data-target="prodIssuanceRegistInfoGrid"
               @click="audit"
               :data-model="infoPop"
        >
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">
          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
        </k-btn>
      </k-form-footer>
    </k-form>

    <!--    修改产品发行登记信息管理弹出框   -->

  </div>
</template>

<script>
import KFieldCheckboxParam from "@/pages/design/components/param/KFieldCheckboxParam.vue";
import Tools from "@/utils/tools";
import {clone} from "lodash";

export default {
  name: "KAudit",
  components: {KFieldCheckboxParam},
  props: {
    infoPop: {},
    auditMethod: '',
  },
  data() {
    return {
      selectRowData: {},
      searchParam: {},
      queryParamDateRange: [],
    };
  },
  computed: {

  },
  methods: {
    findWorkFlowData() {
      this.httpUtil.ajaxJson({
        url: 'wf/process/todoList.json',
        params: {},
      }).then(data => {
        //console.log(data);
        if (data.rows.length > 0) {
          this.processTableData = data.rows;
          // 设置流程状态（待审批）
          for (let i = 0; i < this.processTableData.length; i++) {
            this.processTableData[i].status = "待审批";
          }
        }
      });
    },
    audit() {
      let tableName = this.infoPop.tableName;
      let tableId = this.infoPop.tableId;
      let auditStatus = this.infoPop.auditStatus;
      let startDate = this.queryParamDateRange ? this.queryParamDateRange[0] : null;
      let endDate = this.queryParamDateRange ? this.queryParamDateRange[1] : null;

      this.httpUtil.ajax({
        url: 'server/json/RptApp/audit/status.json',
        params: {
          tableId: tableId,
          startDate: startDate,
          endDate: endDate,
          auditStatus: auditStatus
        }
      }).then(res => {
        if(res.success) {
          this.$emit('auditFunc', this.infoPop);
        }
      })
      // console.log(tableName);
      console.log(tableId);
      console.log(startDate);
      console.log(endDate);
      console.log(auditStatus);
      // console.log(this.$parent.$parent.$parent.$refs);
      // this.$parent.$parent.$parent.$refs.prodIssuanceRegistInfoGrid.load();

    },


    //检查选中数据是否满足可发布状态

  }
};
</script>
