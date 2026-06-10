<template>
<div>
  <div>
    <k-form-search-customize data-target="feeProdGrid" v-model="prodSearchParam">

      <k-form-item label="产品代码">
        <k-field-select v-model="prodSearchParam.prodCode"
                        data-action="T8Dict.findOpenInfos"
                        data-value-field="prodCode"
                        data-display-field="prodCode,prodName">
        </k-field-select>
      </k-form-item>
      <k-form-item label="产品名称">
        <k-field-text v-model="prodSearchParam.prodName" data-validate-type="text"/>
      </k-form-item>
    </k-form-search-customize>
  </div>
  <div>
  <k-grid ref="feeProdGrid" data-action="T8FeeDealDiscount.findOpenProds1" @data-row-select="selectRow">
    <k-grid-column data-header="产品代码" data-name="prodCode"></k-grid-column>
    <k-grid-column data-header="产品名称" data-name="prodName"></k-grid-column>
    <k-grid-column data-header="成立日" data-name="establishDate" data-type="date"></k-grid-column>
    <k-grid-column data-header="募集方式" data-name="raiseType" data-dict="t8_raise_type"></k-grid-column>
    <k-grid-column data-header="产品分类" data-name="prodClassify" data-dict="t8_prod_classify"></k-grid-column>
    <k-grid-column data-header="产品状态" data-name="prodStatus" data-dict="t8_prod_status"></k-grid-column>
    <k-grid-column data-header="子状态" data-name="prodSonStatus" data-dict="t8_prod_son_status"></k-grid-column>
    <template slot="operate" slot-scope="scope">
      <k-btn class="md-info md-just-icon md-simple"
             data-descript="新增交易费用优惠信息"
             data-functype="POPUP" v-if="global.getProdIfUser(scope.row.row.id)&&
             global.isShowAuthorityButton('T8FeeDealDiscount.insertDealDiscount')"
             data-target="addDealDiscount"
             :data-handler="beforePopupAdd" v-show="showCreate">
        <md-icon>add</md-icon>
      </k-btn>
    </template>
  </k-grid>
  </div>
  <k-grid ref="feeDealDiscountGrid" data-action="T8FeeDealDiscount.findDealDisCount" :data-autoload="false">
    <k-grid-column data-header="费用类型" data-name="feeTypeDeal" data-dict="t8_fee_type_deal"></k-grid-column>
    <!-- <k-grid-column data-header="交易客户类型" data-name="custTypeDeal" data-dict="t8_cust_type_deal"></k-grid-column> -->
    <k-grid-column data-header="产品销售商" data-name="distributorName" ></k-grid-column>
    <k-grid-column data-header="优惠比例" data-name="discountProportion"></k-grid-column>
    <k-grid-column data-header="优惠生效日" data-name="validateDate" data-type="date"></k-grid-column>
    <k-grid-column data-header="优惠失效日" data-name="invalidateDate" data-type="date"></k-grid-column>
    <k-grid-column data-header="状态" data-name="confirmStatus" data-dict="confirm_status"></k-grid-column>
    <k-grid-column data-header="创建人" data-name="crtUser"></k-grid-column>
    <k-grid-column data-header="创建日期" data-name="crtDate" data-type="date"></k-grid-column>
    <k-grid-column data-header="创建时间" data-name="crtTime" data-type="time"></k-grid-column>
    <template slot="operate" slot-scope="scope">
      <k-btn class="md-info md-just-icon md-simple"
             data-descript="修改交易费用优惠信息"
             data-functype="POPUP" v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)&&
             global.isShowAuthorityButton('T8FeeDealDiscount.updateDealDiscount')"
             data-target="editDealDiscount"
             :data-handler="getSelectData"
             :data-disabled="scope.row.row.confirmStatus=='1'"
             v-show=" showUpdate"
       >

        <md-icon>edit</md-icon>
      </k-btn>
      <k-btn class="md-danger md-just-icon md-simple"
             data-descript="删除交易费用优惠信息"
             data-functype="SUBMIT"
             data-type="danger" v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)&&
             global.isShowAuthorityButton('T8FeeDealDiscount.deleteDealDiscount')"
             data-target="feeDealDiscountGrid"
             :data-confirm="true"
             data-action="T8FeeDealDiscount.deleteDealDiscount"
             :data-disabled="scope.row.row.confirmStatus=='1'"
             v-show="showDelete"
      >
        <md-icon>delete</md-icon>
      </k-btn>
      <k-btn class="md-info md-just-icon md-simple"
             data-descript="确认交易费用优惠信息"
             data-functype="POPUP" v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)&&
             global.isShowAuthorityButton('T8FeeDealDiscount.confirmDiscount')"
             data-target="confirmDealDiscount"
             :data-handler="getSelectData"
             v-show="showConfirm"
             :data-disabled="scope.row.row.confirmStatus=='1'"
      >
        <md-icon>done</md-icon>
      </k-btn>
    </template>
  </k-grid>
  <k-popup ref="addDealDiscount" data-title="新增产品交易费用优惠信息" :dataDialogDrag="true">
    <T8FeeDealDiscountAdd :info="formData"></T8FeeDealDiscountAdd>
  </k-popup>
  <k-popup ref="editDealDiscount" data-title="修改产品交易费用优惠信息" :dataDialogDrag="true">
    <T8FeeDealDiscountEdit :info="this.feeDealDiscount"></T8FeeDealDiscountEdit>
  </k-popup>
  <k-popup ref="confirmDealDiscount" data-title="确认产品交易费用优惠信息" :dataDialogDrag="true">
    <T8FeeDealDiscountConfirm :info="this.feeDealDiscount"></T8FeeDealDiscountConfirm>
  </k-popup>
</div>
</template>

<script>
import T8FeeDealDiscountAdd from "@/pages/pms/feeConcession/T8FeeDealDiscountAdd";
import T8FeeDealDiscountEdit from "@/pages/pms/feeConcession/T8FeeDealDiscountEdit";
import T8FeeDealDiscountConfirm from "@/pages/pms/feeConcession/T8FeeDealDiscountConfirm";
import {assign} from "lodash";
export default {
  name: "T8FeeDealDiscount.vue",
  components: {
    T8FeeDealDiscountAdd,T8FeeDealDiscountEdit,T8FeeDealDiscountConfirm},
  data(){
    return{
      prodSearchParam: {
        prodCode: '',
      },
      formData:{},//产品信息
      feeDealDiscount:{},//交易费用信息
      showCreate:true,//是否显示新增按钮
      showUpdate:true,//是否显示修改按钮
      showDelete:true,//是否显示删除按钮
      showConfirm:true,//是否显示确认按钮
    }
  },
  created() {
    this.global.getProdUser('');
    this.$nextTick(()=>{
      //获取需要隐藏按钮属性  rennannan 20210322   将接收到的属性值置为false达到隐藏按钮的效果
      this.global.getHideButtons(this);
    });
  },
  methods:{
    //弹出新增框之前给变量赋值
    beforePopupAdd(value){
      this.formData = value;
    },
    //一级查询记录被选中
    selectRow(row, column, event) {
      const _this = this
      _this.selectRowData = assign({}, row)
      //刷新二级查询
      this.$refs.feeDealDiscountGrid.load({t8ProdInfoId:_this.selectRowData.id});
    },
    //点击二级查询中的修改按钮事件
    getSelectData(value){
      this.feeDealDiscount = value;
    }
  },
}
</script>

<style scoped>

</style>
