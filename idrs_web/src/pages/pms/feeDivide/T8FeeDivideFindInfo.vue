<template>
  <div>
    <k-form-search-customize data-target="t8FeeDivideGrid" v-model="T8ProdInfo">

      <k-form-item label="产品代码">
        <k-field-select v-model="T8ProdInfo.prodCode" data-action="T8ProdInfo.findT8ProdInfos" data-display-field="prodCode,prodName"
                        data-value-field="prodCode"  :data-allowblank="this.isMast==true?true:false">

        </k-field-select>
      </k-form-item>

      <k-form-item label="销售商名称">
        <k-field-select v-model="T8ProdInfo.distributorName" data-action="T82001.findTaDistributorInfos"
                        data-display-field="distributorCode,distributorName"
                        data-value-field="distributorCode" >

        </k-field-select>
      </k-form-item>
      <k-form-item label="分成方式">
        <k-field-select v-model="T8ProdInfo.divideType" data-dict="divide_type" >
        </k-field-select>
      </k-form-item>
      <k-form-item label="代码回收">
        <k-field-select v-model="T8ProdInfo.isRecycleCode" data-dict="1yes0no" @data-on-change="changedStatus(T8ProdInfo.isRecycleCode)"></k-field-select>
      </k-form-item>
    </k-form-search-customize>
    <k-grid ref="t8FeeDivideGrid"
            @data-row-select="selectRow" :data-operate-column="false" :data-autoload="false" data-action="T8FeeDivideInfo.findInfoByProdCode1">
      <k-grid-column data-align="center" data-hidden="true" data-header="销售商代码" data-name="distributorCode"/>
      <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode"/>
      <k-grid-column data-align="center" data-header="产品名称" data-name="prodName" data-value-field="prodName" />
      <k-grid-column data-align="center" data-header="销售商名称" data-name="distributorName"/>
      <k-grid-column data-align="center" data-header="费用类型" data-name="feeType" data-dict="t8_fee_divide_fee_type"/>
      <k-grid-column data-align="center" data-header="启用日期" data-type="date" data-name="enableDate"/>
      <k-grid-column data-align="center" data-header="分成方式" data-dict="divide_type" data-name="divideType"/>
      <k-grid-column data-align="center" data-header="归销售商比例（%）" data-type="number" data-name="distributorDivideRate"/>
      <k-grid-column data-align="center" data-header="归管理人比例（%）" data-type="number" data-name="managerDivideRate"/>
      <k-grid-column data-align="center" data-header="归销售商固定BP" data-type="number" data-name="distributorDivideBp"/>
      <k-grid-column data-align="center" data-header="创建人" data-name="createUserName"/>
      <k-grid-column data-align="center" data-header="创建日期" data-type="date" data-name="createDate"/>
      <k-grid-column data-align="center" data-header="创建时间" data-type="time" data-name="createTime"/>
      <k-grid-column data-align="center" data-header="数据状态" data-dict="t8_fee_divide_status" data-name="status"/>
<!--      <template slot="operate" slot-scope="scope">
        <k-btn data-functype="POPUP" data-size="mini"
               class="md-info md-just-icon md-simple" data-target="detailPopup"
               :data-handler="detailHandler" data-descript="查看费用分成详情">
          <md-icon>library_books</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple"
               data-descript="修改费用分成信息" data-functype="POPUP" data-size="small"
               :data-handler="editHandler" data-target="editPopup">
          <md-icon>edit</md-icon>
        </k-btn>

        <k-btn data-functype="POPUP" data-size="mini" data-target="detailStatus"
               class="md-info md-just-icon md-simple" :data-disabled="scope.row.row.status == 1"
               :data-handler="detailHandler" data-descript="确认费用分成信息">
          <md-icon>done</md-icon>
        </k-btn>

        <k-btn class="md-info md-just-icon md-simple"
               data-descript="关联产品" data-functype="POPUP" data-size="small"
               :data-handler="associaProdHandler" data-target="editProdPopup">
          <md-icon>add</md-icon>
        </k-btn>
      </template>-->
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
      selectRowData: {},
      isMast:false,
    };
  },
  methods: {
    changedStatus(value){
      //console.log("value=:>",value);
      if('1'===value){
        this.isMast=true;
      }else if('0'===value){
        this.isMast=true;
      }else{
        this.isMast=false;
      }
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
