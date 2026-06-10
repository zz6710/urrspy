<template>
  <div>
    <k-grid ref="sourceDataGrid" :data-data="{'rows':this.sourceData.$RatGrid}"
            :dataPageSize="0" :data-display="false" :data-operate-column="false" class="continue-ele" isFormBodyScreen>
      <k-grid-column data-header="ID" data-name="id" data-hidden="true"></k-grid-column>
      <k-grid-column data-header="数据库表名" data-name="tableName" data-width="100"></k-grid-column>
      <k-grid-column data-header="主键信息" data-name="naturalKeys" data-hidden="true"></k-grid-column>
      <k-grid-column data-header="主键信息名" data-name="naturalKeysName" data-width="180"></k-grid-column>
      <k-grid-column data-header="涉及字段" data-name="changeField" data-width="100" data-hidden="true"></k-grid-column>
      <k-grid-column data-header="涉及字段名" data-name="changeFieldName" data-width="100"></k-grid-column>
      <k-grid-column data-header="旧数据ID" data-name="oldid" data-hidden="true"></k-grid-column>
      <k-grid-column data-header="新数据ID" data-name="newid" data-hidden="true"></k-grid-column>
      <k-grid-column data-header="数据库表" data-name="tables" data-hidden="true"></k-grid-column>
      <k-grid-column data-header="原数据" data-name="fieldOld" data-hidden="true"></k-grid-column>
      <k-grid-column data-header="新数据" data-name="fieldNew" data-hidden="true"></k-grid-column>
      <k-grid-column data-header="原数据" data-name="dictOld" data-width="160"></k-grid-column>
      <k-grid-column data-header="新数据" data-name="dictNew" data-width="160"></k-grid-column>
      <k-grid-column data-header="数据涉及报表" data-name="reportName" data-hidden="true"></k-grid-column>
      <k-grid-column data-header="数据涉及报表名" data-name="reportsName" data-width="120"></k-grid-column>
      <k-grid-column data-header="修改状态" data-name="editstatus" data-dict="editStatus" data-width="70"></k-grid-column>
    </k-grid>
  </div>
</template>

<script>
import Tools from "@/utils/tools";
export default {
  name: "ConfirmSourceDataFlow",
  props: ["submitData"],
  data() {
    return {
      formData: {},
      sourceData: {
        $RatGrid: null,
      }
    };
  },
  created() {
    this.formData = this.info;
    this.$nextTick(()=>{
      this.sourceData.$RatGrid = JSON.parse(this.submitData.sourceData);
    });
  },
  methods: {
    freshPage(){
      this.$emit('loadGriding',this.formData);
    },
  }
}
</script>

<style scoped>

</style>
