<template>
  <div>
    <k-form-search-customize data-target="T8ProdAccountFindInfo" v-model="T8ProdInfo">

      <k-form-item label="产品代码">
        <k-field-select v-model="T8ProdInfo.prodCode" data-action="T8ProdInfo.findT8ProdInfos" data-display-field="prodCode,prodName"
                        data-value-field="prodCode" >

        </k-field-select>
      </k-form-item>
      <k-form-item label="产品名称">
        <k-field-text v-model="T8ProdInfo.prodName" data-validate-type="text"/>
      </k-form-item>
      <k-form-item label="产品状态">
        <k-field-select v-model="T8ProdInfo.prodStatus"  data-dict="t8_prod_status"></k-field-select>
      </k-form-item>
      <k-form-item label="是否维护账户">
        <k-field-select v-model="T8ProdInfo.isMaintainAccount"  data-dict="is_default"></k-field-select>
      </k-form-item>
      <k-form-item label="账户类型">
        <k-field-select v-model="T8ProdInfo.accountType" data-dict="t8_account_type"
                        data-multiple="true"/>
      </k-form-item>
      <k-form-item label="代码回收">
        <k-field-select v-model="T8ProdInfo.isRecycleCode" data-dict="1yes0no"></k-field-select>
      </k-form-item>
    </k-form-search-customize>
    <k-grid ref="T8ProdAccountFindInfo" data-action='T8ProdAccountFindInfo.findT8ProdInfos1' @data-row-select="selectRow"
            :data-operate-column="false" data-operate-width="200px">
      <k-grid-column data-header="产品id" data-name="id" :data-hidden="true"/>
      <k-grid-column data-header="产品代码" data-name="prodCode" />
      <k-grid-column data-header="产品名称" data-name="prodName" />
      <k-grid-column data-header="产品形态" data-name="prodMode" data-dict="t8_prod_create_type" />
      <k-grid-column data-header="募集方式" data-name="raiseType" data-dict="t8_raise_type" />
      <k-grid-column data-header="产品状态" data-name="prodStatus" data-dict="t8_prod_status" />
      <k-grid-column data-header="产品子状态" data-name="prodSonStatus" data-dict="t8_prod_son_status" />
      <template slot="operate" slot-scope="props">
        <k-btn data-functype="PAGE" data-size="mini"  class="md-info md-just-icon md-simple"
               :data-model="props.row.row" @click="toEditProdInfo(props.row.row)"
               :prodInfoId="props.row.row.id" data-descript="产品信息详情">
          <md-icon>library_books</md-icon>
        </k-btn>

      </template>
    </k-grid>


    <k-grid ref="t8prodAccountInfoGrid" :data-operate-column="false" data-action="T8ProdAccountInfo.findByProdCode" :data-autoload="false">
      <k-grid-column data-hidden="true" data-header="id" data-name="id" />
      <k-grid-column data-header="账户类型" data-name="accountType"  data-dict="t8_account_type"/>
      <k-grid-column data-header="开户行名称" data-name="openAccountName" />
      <k-grid-column data-header="资金账户名称" data-name="accountName" />
      <k-grid-column data-header="资金账号" data-name="accountCode" />
      <k-grid-column data-header="备注" data-name="remark" />
      <k-grid-column data-header="创建时间" data-name="crtDate" data-type="date" />
      <k-grid-column data-header="创建人名称" data-name="createUserName"/>
      <k-grid-column data-header="账户状态" data-name="accountStatus" data-dict="t8_account_status"/>
      <template slot="operate"  slot-scope="scope">

      </template>
    </k-grid>




  </div>
</template>

<script>
  import {assign} from "lodash";
  export default {
    data() {
      return {
        formData: {prodCode:'',},
        T8ProdInfo:{},
        selectRowData: {}
      };
    },

    methods: {

      toEditProdInfo(row){
        let pathUrl = '/main/pms/M81/prodDisplay/M81001display';
        this.$router.push({
          path: pathUrl,
          query: {prodMode: row.prodMode,prodInfoId:row.id,prodCode: row.prodCode,assemblyMenuType:'1',menuName:'T8ProdAccountFindInfo'},
        });

      },
      selectRow(row, column, event) {
        this.selectRowData = assign({}, row)
        this.formData = assign({}, row);
        this.$refs.t8prodAccountInfoGrid.load(
          {
            prodCodes:this.formData.prodCode,
          });
      },
    },
    computed: {

    }
  };
</script>
