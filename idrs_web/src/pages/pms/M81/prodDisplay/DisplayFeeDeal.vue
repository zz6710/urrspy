<template>
  <div style="min-height:225px;">
        <k-grid ref="t8FeeDealGrid" :data-operate-column="true"  @data-row-select="selectRow" :dataPopupAppendToBody="true">
          <k-grid-column data-header="行数" data-name="numId" :data-hidden="true"></k-grid-column>
          <k-grid-column data-header="序号" data-name="id" :data-hidden="true"></k-grid-column>
          <k-grid-column data-header="产品代码" data-name="prodCode" :data-hidden="true"></k-grid-column>
          <k-grid-column data-header="交易费用类型" data-name="feeTypeDeal" data-dict="t8_fee_type_deal"></k-grid-column>
          <!-- <k-grid-column data-header="交易客户类型" data-name="custTypeDeal"  data-dict="t8_cust_type_deal"></k-grid-column> -->
<!--          <k-grid-column data-header="交易计费基数" data-name="chargingIndexDeal" data-dict="t8_charging_index_deal"></k-grid-column>-->
<!--          <k-grid-column data-header="交易计算方式" data-name="chargingMethod" data-dict="t8_charging_method_deal"></k-grid-column>-->
          <k-grid-column data-header="基本费率%" data-name="baseFeeRate"></k-grid-column>
          <k-grid-column data-header="最高费用" data-name="maxCost"></k-grid-column>
          <k-grid-column data-header="最低费用" data-name="minCost"></k-grid-column>
          <k-grid-column data-header="是否按金额分段" data-name="isAmtSegment" data-dict="t8_prod_isok"></k-grid-column>
          <k-grid-column data-header="是否按时间分段" data-name="isTimeSegment" data-dict="t8_prod_isok"></k-grid-column>
          <k-grid-column data-header="费用说明" data-name="costDesc" :data-hidden="true"/>
          <k-grid-column data-header="审批状态" data-name="approvalStatus" :data-hidden="true"></k-grid-column>
          <k-grid-column data-header="生命周期状态" data-name="prodStatus" :data-hidden="true"></k-grid-column>
          <k-grid-column data-header="创建日期" data-name="crtDate" :data-hidden="true"></k-grid-column>
          <k-grid-column data-header="创建时间" data-name="crtTime" :data-hidden="true"></k-grid-column>
          <k-grid-column data-header="创建人" data-name="crtUser" :data-hidden="true"></k-grid-column>
          <k-grid-column data-header="更新日期" data-name="updDate" :data-hidden="true"></k-grid-column>
          <k-grid-column data-header="更新时间" data-name="updTime" :data-hidden="true"></k-grid-column>
          <k-grid-column data-header="更新人" data-name="updUser" :data-hidden="true"></k-grid-column>
          <template slot="operate" slot-scope="scope">
            <k-btn  data-functype="POPUP" data-size="mini" data-target="selctComp" class="md-info md-just-icon md-simple"
                    :data-handler="editHandler" data-descript="查看交易费用">
              <md-icon>search</md-icon>
            </k-btn>
          </template>
        </k-grid>

    <div >
      <k-popup ref="selctComp" data-title="查看"  style="float: left;margin-right: 18%" :dataDialogDrag="true">
        <selctComp  v-model="ProdFeeDeal2" :info="ProdFeeDeal2" :prodMode="prodMode" :dataParams="dataParams" />
      </k-popup>
    </div>


</div>



</template>

<script>
  import {assign} from "lodash";
  import Tools from "@/utils/tools";
  import selctComp from "../prodInfoGD/M81001-FeeDealSelect";
  export default {
    components: {selctComp},
    computed: {},
    model: {
      prop: 'ProdFeeDeal',
      event: 'input'
    },
    props:{
      prodMode: {
        type: String,
        default: ''
      },
      ProdFeeDeal: {
        //t8PrjFeeLists:'',
        dataParams: [],
      },
    },
    data() {
      return {
        selectRowData:{},
        ProdFeeDeal2: {},
        dataParams:[],
      };
    },
    mounted(){


    },
    methods: {
      editHandler(params){
        let numId = params.numId;
        this.ProdFeeDeal2 = this.dataParams[numId];
        if(this.ProdFeeDeal2.t8PrjFeeLists==null){
          Tools.alert("未获取到信息，稍后重试 !","danger");
          return  false;
        }
        return params;
      },
      selectRow(row, column, event) {
        this.selectRowData = assign({}, row)
        this.ProdFeeDeal2 = assign({}, row)
      },
      uptDataParams(dataParams) {
        for (let i = 0; i < dataParams.length; i++) {
          this.$set(dataParams[i], 'numId', i)
        }
        this.dataParams = dataParams;
        //先加载页面，再查询子模块
        this.$set(this.$refs.t8FeeDealGrid, 'list', this.dataParams);

      },

    },
    watch:{
      'ProdFeeDeal.dataParams' : function (value) {
        console.log(2)
        this.uptDataParams(value);
      },
    },


    created() {
      this.ProdFeeDeal2.chargeType = "1";
      this.$nextTick(() => {
        this.uptDataParams(this.ProdFeeDeal.dataParams);
      })

    }




  };
</script>
<style lang="scss" scoped>
::v-deep .kk-mask{
  width: auto;
}
.add-btn-div{
  position: relative;
  z-index: 1;
}
.add-btn{
  background-color: #4caf50;
  border-radius: 20px;
  box-shadow: 0 4px 5px 0 rgba(76,175,80,0.14), 0 1px 10px 0 rgba(76,175,80,0.12), 0 2px 4px -1px rgba(76,175,80,0.2);
  width: 20px;
  height: 20px;
  line-height: 20.5px;
  font-size: 23px;
  font-weight: 400;
  cursor: pointer;
  color: #FFF;
  text-align: center;
}
</style>
