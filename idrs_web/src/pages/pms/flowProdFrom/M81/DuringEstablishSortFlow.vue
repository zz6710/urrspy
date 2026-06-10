<template>
  <div class="formPanel" ref="formPanel" style="margin-left: 5%;">
    <div class="form-item prod-panel">
      <div class="title">
      </div>
      <k-form>
        <k-form-item label="产品代码">
          <k-field-text v-model="prodCode" :data-disabled="true"></k-field-text>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="prodName" :data-disabled="true"></k-field-text>
        </k-form-item>
      </k-form>
      <DisplayProdShareSort :dataParams="dataParams"/>
    </div>
  </div>
</template>

<script>
import DisplayProdShareSort from "@/pages/pms/M81/prodDisplay/DisplayProdShareSort";

export default {
  name: "DuringEstablishSortFlow",
  components: {
    DisplayProdShareSort
  },
  props: {
    taskInfo: {},
  },
  data() {
    return {
      formData: {},
      dataParams: [],
      prodCode: '',
      prodName: '',
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
        this.formData = JSON.parse(res.data.submitParams);
        this.dataParams = JSON.parse(this.formData.prodShareSorts);
        this.prodCode = this.dataParams[0].prodCode;
        this.prodName = this.dataParams[0].prodName;
      }
    });

  },
}
</script>

<style lang="scss" scoped>
@import "../../../../styles/T81001.scss";
</style>
