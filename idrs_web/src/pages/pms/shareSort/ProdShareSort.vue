<template>
  <div>
    <div>
      <k-form-search-customize data-target="prodShareSortGrid" v-model="queryParam">
        <k-form-item label="产品代码">
          <k-field-select v-model="queryParam.prodCode" data-action="T8ProdInfo.findT8ProdInfos"
                          :data-params="{isShareSort:1}"
                          data-display-field="prodCode,prodName" data-value-field="prodCode"></k-field-select>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="queryParam.prodName"></k-field-text>
        </k-form-item>
        <k-form-item label="产品状态">
          <k-field-select v-model="queryParam.prodStatus" data-dict="t8_prod_status"></k-field-select>
        </k-form-item>
        <k-form-item label="募集方式">
          <k-field-select v-model="queryParam.raiseType" data-dict="t8_raise_type"></k-field-select>
        </k-form-item>
        <k-form-item label="基准类型">
          <k-field-select v-model="queryParam.baseType" data-dict="t8_base_type"></k-field-select>
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="prodShareSortGrid" data-action="T8ProdInfo.findT8ProdInfos"
              :data-params="{isShareSort:1}">
        <k-grid-column data-header="产品代码" data-name="prodCode"></k-grid-column>
        <k-grid-column data-header="产品名称" data-name="prodName" data-width="250"></k-grid-column>
        <k-grid-column data-header="产品状态" data-name="prodStatus" data-dict="t8_prod_status"></k-grid-column>
        <k-grid-column data-header="产品子状态" data-name="prodSonStatus" data-dict="t8_prod_son_status"></k-grid-column>
        <k-grid-column data-header="成立日期" data-name="establishDate"></k-grid-column>
        <k-grid-column data-header="到期日" data-name="endDate"></k-grid-column>
        <k-grid-column data-header="募集方式" data-name="raiseType" data-dict="t8_raise_type"></k-grid-column>
        <k-grid-column data-header="基准类型" data-name="baseType" data-dict="t8_base_type"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="维护份额分类信息" data-size="mini"
                 @click="toOpePage(scope.row.row)">
            <md-icon>edit</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </div>


  </div>
</template>

<script>
export default {
  data() {
    return {
      formData: {},
      selectRowData: {},
      queryParam: {},//查询参数
    };
  },
  methods: {
    //行被选中
    selectRow(row, column, event) {
      this.formData = Object.assign({}, row)
    },
    //跳转到份额分类维护页面
    toOpePage(row) {
      this.selectRowData = Object.assign({}, row)
      this.$router.push({
        path: '/main/pms/shareSort/ProdShareSort/ProdShareSortOpe',
        query: {
          t8ProdInfoId: this.selectRowData.id,
          baseType: this.selectRowData.baseType,
          prodStatus: this.selectRowData.prodStatus,
          prodCode:row.prodCode,
          prodName:row.prodName
        },
      });
    }
  }
};
</script>
