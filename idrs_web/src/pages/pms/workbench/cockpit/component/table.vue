<template>
  <el-table
    :data="tableData.tableContent"
    height="100%"
  >
    <el-table-column
      v-for="item in tableData.tableHeader"
      :key="item.prop"
      :label="item.label"
      :prop="item.prop"
      :align="item.align || 'center'"
    >
    </el-table-column>
  </el-table>
</template>
<script>
export default {
  props: {
    tableData: Object,
    height: Number,
  },
  methods: {}
}
</script>
<style scoped lang="scss">
.el-table {
  margin: 0 auto;
  background: transparent;
  color: #fff;
  &::before {
    height: 0;
  }
  /deep/ thead {
    color: #fff;
  }
  /deep/ tr {
    background: transparent;
    &:hover {
      td {
        background: rgba(32, 218, 236, 0.1) !important;
      }
    }
  }
  /deep/ th.el-table__cell {
    background: transparent;
  }
  /deep/ th.el-table__cell.is-leaf {
    border-bottom: 1px solid rgba(32, 218, 236, 0.1);
  }
  /deep/ td.el-table__cell {
    border-bottom: 1px solid rgba(32, 218, 236, 0.1);
  }
  &.el-table--scrollable-y {
    /deep/ .el-table__body-wrapper {
      &::-webkit-scrollbar {
        width: 4px;
        height: 4px;
      }
      &::-webkit-scrollbar-thumb {
        border-radius: 10px !important;
        background: rgba(125, 187, 254, 0.2) !important;
      }
      &::-webkit-scrollbar-track {
        border-radius: 10px !important;
        background: rgba(32, 218, 236, 0.1) !important;
      }
    }
  }
}

</style>
