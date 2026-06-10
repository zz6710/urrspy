<template>
  <div>
    <div>
      <k-grid ref="formProdShareSortGrid" :dataPopupAppendToBody="true">
        <k-grid-column data-header="numId" data-name="numId" :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="t8ProdInfoId" data-name="t8ProdInfoId" :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="份额名称" data-name="shareName" data-dict="t8_share_name"></k-grid-column>
        <k-grid-column data-header="销售代码" data-name="salesCode"></k-grid-column>
        <k-grid-column data-header="销售名称" data-name="salesName"></k-grid-column>
        <k-grid-column data-header="销售客群" data-name="salesGroup"></k-grid-column>
        <k-grid-column data-header="销售份额状态" data-name="salesShareStatus" data-dict="t8_prod_status"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="详情" data-functype="POPUP" data-size="mini"
                 @click="detailHandler(scope.row.row)">
            <md-icon>library_books</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--业绩报酬及销售服务费率-->
    <k-grid ref="formProdSectionGrid" :data-operate-column="false">
      <k-grid-column data-header="numId" data-name="numId" :data-hidden="true"></k-grid-column>
      <k-grid-column data-header="t8ProdInfoId" data-name="t8ProdInfoId" :data-hidden="true"></k-grid-column>
      <k-grid-column data-header="份额名称" data-name="shareName" data-dict="t8_share_name"></k-grid-column>
      <k-grid-column data-header="基准类型" data-name="baseType" data-dict="t8_base_type"></k-grid-column>
      <k-grid-column data-header="基准利率%" data-name="baseRate"></k-grid-column>
      <k-grid-column data-header="基准利率下限%" data-name="baseMinRate"></k-grid-column>
      <k-grid-column data-header="基准利率上限%" data-name="baseMaxRate"></k-grid-column>
<!--      <k-grid-column data-header="业绩报酬提取比例%" data-name="performanceOut"></k-grid-column>-->
      <k-grid-column data-header="销售服务费率%" data-name="salesFeeRate"></k-grid-column>
    </k-grid>

    <!--业绩报酬及销售服务费率-->
    <k-grid ref="formProdMoneyGrid" :data-operate-column="false">
      <k-grid-column data-header="numId" data-name="numId" :data-hidden="true"></k-grid-column>
      <k-grid-column data-header="t8ProdInfoId" data-name="t8ProdInfoId" :data-hidden="true"></k-grid-column>
      <k-grid-column data-header="份额名称" data-name="shareName" data-dict="t8_share_name"></k-grid-column>
      <k-grid-column data-header="起点金额(元)" data-name="minAmount"></k-grid-column>
      <k-grid-column data-header="递增金额(元)" data-name="stepAmount"></k-grid-column>
      <k-grid-column data-header="认购追加金额" data-name="subAppendAmount"></k-grid-column>
      <k-grid-column data-header="申购追加金额" data-name="redeemAppendAmount" v-if="this.popShow"></k-grid-column>
      <k-grid-column data-header="单笔最小赎回份额" data-name="minRedeemVol"  v-if="this.popShow"></k-grid-column>
      <k-grid-column data-header="持有份额下限" data-name="minHoleVol"></k-grid-column>
      <k-grid-column data-header="持有份额上限" data-name="maxHoleVol"></k-grid-column>
    </k-grid>

    <k-popup ref="detailPopup" style="float: left;margin-right: 10%;" :dataDialogDrag="true"
             :dataPopupAppendToBody="true">
      <prod-share-sort-detail :formProdShareSort="formProdShareSort" :envItems="envItems"
                              :tailingCommisionList="tailingCommisionList"
                              :moneyList="moneyList"></prod-share-sort-detail>
    </k-popup>
  </div>
</template>

<script>
import ProdShareSortDetail from "@/pages/pms/shareSort/ProdShareSortDetail";

export default {
  name: "DisplayProdShareSort",
  props: {
    t8ProdInfoId: '',
    baseType: '',
    dataParams: {
      type: Array,
      default: [],
    },
  },
  components: {ProdShareSortDetail},
  data() {
    return {
      popShow:true,
      tableParams: [],
      updateParam: [],
      formProdShareSort: {},
      envItems: [{}],
      moneyList: [],
      tailingCommisionList: [],
    }
  },
  created() {
     console.log(this.t8ProdInfoId);
     this.httpUtil.comnQuery({
        action: 'T8ProdInfo.findT8ProdInfos',
        params: {
          id : this.t8ProdInfoId ,
        },
        successAlert: false,
      }).then(data => {
        if(data.rows.length > 0 ){
          let row = data.rows[0];
          if(row.prodMode=='1'){
            this.popShow=false;
          }

        }

      });
    let _this = this;
    _this.uptDataParams(this.dataParams);
  },
  watch: {
    dataParams(val) {
      let _this = this;
      _this.uptDataParams(val);
    },
    immediate: true
  },
    methods: {
    uptDataParams(dataParams) {
      this.$nextTick(() => {
        this.$set(this.$refs.formProdShareSortGrid, 'list', dataParams);
        this.$set(this.$refs.formProdSectionGrid, 'list', dataParams);
        this.$set(this.$refs.formProdMoneyGrid, 'list', dataParams);
      });
    },
    //打开详情弹出框
    detailHandler(val) {
      this.formProdShareSort = Object.assign({}, val);
      if (this.formProdShareSort.prodShareRatio) {
        this.envItems = this.formProdShareSort.prodShareRatio;
      } else {
        this.envItems = [{}];
      }
      if (this.formProdShareSort.prodShareSection) {
        let moneyList2 = [];
        let array = [];
        for (let j = 0; j < this.formProdShareSort.prodShareSection.length; j++) {
          array.push(this.formProdShareSort.prodShareSection[j]);
          if (j < this.formProdShareSort.prodShareSection.length - 1) {
            moneyList2.push(parseInt(this.formProdShareSort.prodShareSection[j].dimension1Max));
          }
        }

        this.moneyList = moneyList2;
        this.tailingCommisionList = array;
        this.tailingCommisionMoneyList = array;
      }
      this.$refs.detailPopup.popup();
    },
  }
}
</script>

<style scoped>
::v-deep .kk-mask {
  width: auto;
}

.add-btn-div {
  position: relative;
  z-index: 1;
}

.add-btn {
  background-color: #4caf50;
  border-radius: 20px;
  box-shadow: 0 4px 5px 0 rgba(76, 175, 80, 0.14), 0 1px 10px 0 rgba(76, 175, 80, 0.12), 0 2px 4px -1px rgba(76, 175, 80, 0.2);
  width: 20px;
  height: 20px;
  line-height: 20.5px;
  font-size: 23px;
  font-weight: 400;
  cursor: pointer;
  color: #FFF;
  text-align: center;
}

.tableLine {
  margin: 30px 8px 20px 6px;
  border-top: 1px dotted #C0C0C0;
  width: 900px;
  position: relative;
  text-align: center;
  font-size: 14px;
}

.midText {
  position: absolute;
  left: 50%;
  background-color: #ffffff;
  font-weight: 300;
  padding: 0 15px;
  transform: translateX(-50%) translateY(-50%);
}
</style>
