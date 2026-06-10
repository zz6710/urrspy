<template>
  <div style="width: 100%;">
    <k-grid ref="opBusiInfoGrid" :data-data="gridData">
      <k-grid-column data-header="功能id" data-name="busiId"></k-grid-column>
      <k-grid-column data-header="功能名称" data-name="busiName"></k-grid-column>
      <k-grid-column data-header="上级功能" data-name="upperId"></k-grid-column>
      <k-grid-column data-header="icon图标" data-name="iconClass"></k-grid-column>
      <template slot="operate" slot-scope="scope">
        <k-btn class="btn-custom-plain" data-size="mini">修改</k-btn>
        <k-btn class="md-danger" data-target="opBusiInfoGrid" :data-confirm="true" data-descript="删除">
          删除
        </k-btn>
      </template>
    </k-grid>
  </div>
</template>

<script>
  export default {
    name: 'flowTest',
    props: {},
    data() {
      return {
        gridData: {
          rows: [
            {"busiId":"00000003","busiName":"功能1","upperId":"","iconClass":"111","children":[],"row_index":0},
            {"busiId":"00000004","busiName":"功能2","upperId":"","iconClass":"bbb","children":[],"row_index":1},
            {"busiId":"00000005","busiName":"功能3","upperId":"00000004","iconClass":"","children":[],"row_index":2}
          ]
        }
      }
    },
    methods: {
      returnData() {
        return this.gridData.rows;
      },
      // data和returnData所返回的结构一致
      loadData(data) {
        this.gridData.rows = data;
      }
    }
  }
</script>
