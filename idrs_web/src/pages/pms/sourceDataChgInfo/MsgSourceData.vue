<template>
  <div>
    <k-form data-label-width="215px" isFormBodyScreen>
        <k-grid data-action="SourceDataChgInfoModel.findSourceDataChgInfoModelAll2" :dataPageSize="0" :data-display="false"
                :data-params="{'tables':this.info.tables,'naturalKeys':this.info.naturalKeys,'status':this.info.status,'dealDate':this.info.dealDate,'newid':this.info.newid}"
                data-operate-width="100px" class="continue-ele" data-operate-column="false">
          <k-grid-column data-header="ID" data-name="id" data-hidden="true"></k-grid-column>
          <k-grid-column data-header="数据库表名" data-name="tableName" data-width="100"></k-grid-column>
          <k-grid-column data-header="主键信息" data-name="naturalKeys" data-hidden="true" data-width="5"></k-grid-column>
          <k-grid-column data-header="主键信息名" data-name="naturalKeysName" data-width="180"></k-grid-column>
          <k-grid-column data-header="涉及字段" data-name="changeField" data-width="100" data-hidden="true"></k-grid-column>
          <k-grid-column data-header="涉及字段名" data-name="changeFieldName" data-width="100"></k-grid-column>
          <k-grid-column data-header="旧数据ID" data-name="oldid" data-hidden="true" data-width="5"></k-grid-column>
          <k-grid-column data-header="新数据ID" data-name="newid" data-hidden="true" data-width="5"></k-grid-column>
          <k-grid-column data-header="数据库表" data-name="tables" data-hidden="true" data-width="5"></k-grid-column>
          <k-grid-column data-header="外部数据字典" data-name="outDict" data-hidden="true"></k-grid-column>
          <k-grid-column data-header="原数据" data-name="fieldOld" data-hidden="true"></k-grid-column>
          <k-grid-column data-header="新数据" data-name="fieldNew" data-hidden="true"></k-grid-column>
          <k-grid-column data-header="原数据" data-name="dictOld" data-width="160"></k-grid-column>
          <k-grid-column data-header="新数据" data-name="dictNew" data-width="160"></k-grid-column>
          <k-grid-column data-header="数据涉及报表" data-name="reportName" data-width="200" data-hidden="true"></k-grid-column>
          <k-grid-column data-header="数据涉及报表名" data-name="reportsName" data-width="120"></k-grid-column>
          <k-grid-column data-header="数据状态" data-name="editstatus" data-dict="editStatus" data-width="70"></k-grid-column>
        </k-grid>
    </k-form>
  </div>
</template>

<script>

export default {
  name: "MsgSourceData",
  props: {
    info : {
      type:Object,
    },
  },
  data() {
    return {
      sourceDataChgs: [],
      formData: {},
    };
  },
  watch:{
    info: {
      handler(){
        this.formData = this.info;
      },
    }
  },
  created() {
    this.formData = this.info;
  },
  methods: {

  }
}
</script>

<style scoped>

</style>
