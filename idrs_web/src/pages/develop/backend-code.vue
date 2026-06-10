<template>
  <div>
    <k-form :data-col="3" ref="codeForm">
      <k-form-item label="数据库表">
        <k-field-select v-model="data.table" :data-allowblank="false" data-action="DbTable.findTables"
          data-display-field="name,comment" data-value-field="name"></k-field-select>
      </k-form-item>
      <k-form-item label="实体类">
        <k-field-text v-model="data.model" :data-allowblank="false"></k-field-text>
      </k-form-item>
      <k-form-item label="实体类描述">
        <k-field-text v-model="data.tableComment" :data-allowblank="false"></k-field-text>
      </k-form-item>
      <k-form-item label="包名">
        <k-field-text v-model="data.package" :data-allowblank="false"></k-field-text>
      </k-form-item>
      <k-form-footer>
        <k-btn data-functype="DOWNLOAD" data-from="codeForm" data-download-name="代码.zip" data-url="download/server/ApiServer/code/server/download.json"
          :data-model="data" data-descript="导出代码">导出代码</k-btn>
      </k-form-footer>
    </k-form>
  </div>
</template>

<script>
  export default {
    name:"backend-code",
    data() {

      return {
        data: {
          table: "",
          model: "",
          tableComment: "",
          package: ""
        },
      }
    },
    methods: {

    }
  }
</script>

<style>
</style>
