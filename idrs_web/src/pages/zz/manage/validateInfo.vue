<template>
  <div>
    <k-form ref="validateInfoForm" isFormBodyScreen :data-col="4">
      <template>
        <k-grid ref="validateInfoDataGrid" :data-action="action" dataHeight="500" data-operate-column='false'
                :data-params="{'validateTable':this.formData.tableId,'reportDate':this.formData.reportDate,'dataId':this.formData.id}">
          <k-grid-column data-header="指标代码" data-name="indexCode" data-width="80"></k-grid-column>
          <k-grid-column data-header="校验错误信息" data-name="reason"></k-grid-column>
        </k-grid>
      </template>
      <k-form-footer slot="footer" data-align="center">
        <k-btn class="btn-custom-plain" data-functype="CLOSE">
          <md-icon md-src="/static/svg/cancel.svg"></md-icon>关闭
        </k-btn>
      </k-form-footer>
    </k-form>
  </div>
</template>

<script>
import Tools from "@/utils/tools";

export default {
  name: "ValidateInfo",
  props: {
    info : {
      type:Object,
    },
  },
  data() {
    return {
      formData: {},
      selectRows: [],
      action:""
    };
  },
  created() {
    this.formData = this.info;
    this.getAction();
  },
  methods: {
    getAction(){
    if(this.formData.tableId==="app_cust_register_info"){
        this.action = "TrCustRegisterInfo.findValidateInfos"
      }else if(this.formData.tableId==="app_cust_vol_register_info"){
        this.action = "TrCustVolRegisterInfo.findValidateInfos"
      }else{
        this.action = "TrCustTransInfo.findValidateInfos"
      }
    }
  }
}
</script>

<style scoped>

</style>
