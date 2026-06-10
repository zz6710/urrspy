<template>
  <div>
    <k-form-search-customize data-target="prodDealInfoGrid" v-model="prodSearchParam">

      <k-form-item label="产品代码">
        <k-field-select v-model="prodSearchParam.prodCode" data-action="T8ProdInfo.findT8ProdInfos"
                        :data-params="{isShareSort:0}"
                        data-display-field="prodCode,prodName" data-value-field="prodCode"/>
      </k-form-item>
      <k-form-item label="产品名称">
        <k-field-text v-model="prodSearchParam.prodName" data-validate-type="text"/>
      </k-form-item>
      <k-form-item label="查询日期">
        <k-field-date v-model="prodSearchParam.changeDate" data-type="date" data-date-format="yyyy-MM-dd"/>
      </k-form-item>
      <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addT8ProdSalesInfoPopup"
             v-if="global.isShowAuthorityButton('T8ProdSalesInfo.addT8ProdSalesInfo')">
        <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
    </k-form-search-customize>
    <k-grid ref="prodDealInfoGrid" :data-operate-column="true" data-action="T8ProdSalesInfo.findT8ProdSalesInfos1"
            @data-row-select="selectRow">
      <k-grid-column data-header="id" data-name="id" :data-hidden="true"></k-grid-column>
      <k-grid-column data-header="产品代码" data-name="prodCode"></k-grid-column>
      <k-grid-column data-header="产品名称" data-name="prodName"></k-grid-column>
      <k-grid-column data-header="发生事件" data-name="prodDealType" data-dict="prod_deal_type"></k-grid-column>
      <k-grid-column data-header="发生日期" data-name="changeDate"></k-grid-column>
      <k-grid-column data-header="认申购份额" data-name="subsVol"></k-grid-column>
      <k-grid-column data-header="认申购金额" data-name="subsAmt"></k-grid-column>
      <k-grid-column data-header="赎回份额" data-name="redeemVol"></k-grid-column>
      <k-grid-column data-header="赎回金额" data-name="redeemAmt"></k-grid-column>
      <k-grid-column data-header="费用类型" data-name="feeType" data-dict="t8_fee_type_deal"></k-grid-column>
      <k-grid-column data-header="费用金额" data-name="feeMoney"></k-grid-column>
      <k-grid-column data-header="代销总额" data-name="proxySumMoney"></k-grid-column>
      <k-grid-column data-header="备注" data-name="remark"></k-grid-column>
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" data-descript="修改产品份额" data-functype="POPUP" data-size="mini"
               data-target="editT8ProdSalesInfoPopup"
               v-if="global.isShowAuthorityButton('T8ProdSalesInfo.updateT8ProdSalesInfo')">
          <md-icon>edit</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple" data-descript="添加销售商份额" data-functype="POPUP" data-size="mini"
               data-target="addT8ProdSalesInfoDisPopup"
               v-if="global.isShowAuthorityButton('T8ProdSalesInfo.addT8ProdSalesInfoDistributor')">
          <md-icon>add_circle</md-icon>
        </k-btn>
        <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT"
               data-action="T8ProdSalesInfo.deleteT8ProdSalesInfo" data-size="mini"
               data-type="danger" data-target="prodDealInfoGrid" :data-confirm="true" data-descript="删除产品份额"
               v-if="global.isShowAuthorityButton('T8ProdSalesInfo.deleteT8ProdSalesInfo')">
          <md-icon>close</md-icon>
        </k-btn>
      </template>
    </k-grid>

    <k-grid ref="prodSalesInfoGrid" :data-operate-column="true" :data-autoload="false"
            data-action="T8ProdSalesInfoDistributor.findT8ProdSalesInfoDistributors" @data-row-select="selectRowDis">
      <k-grid-column data-header="产品代码" data-name="prodCode"></k-grid-column>
      <k-grid-column data-header="销售商" data-name="distributorCode"></k-grid-column>
      <k-grid-column data-header="发生事件" data-name="prodDealType" data-dict="prod_deal_type"></k-grid-column>
      <k-grid-column data-header="发生日期" data-name="changeDate"></k-grid-column>
      <k-grid-column data-header="认申购份额" data-name="subsVol"></k-grid-column>
      <k-grid-column data-header="认申购金额" data-name="subsAmt"></k-grid-column>
      <k-grid-column data-header="赎回份额" data-name="redeemVol"></k-grid-column>
      <k-grid-column data-header="赎回金额" data-name="redeemAmt"></k-grid-column>
      <k-grid-column data-header="费用类型" data-name="feeType" data-dict="t8_fee_type_deal"></k-grid-column>
      <k-grid-column data-header="费用金额" data-name="feeMoney"></k-grid-column>
      <k-grid-column data-header="代销总额" data-name="proxySumMoney" data-hidden="true"></k-grid-column>
      <k-grid-column data-header="备注" data-name="remark"></k-grid-column>
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" data-descript="修改销售商份额" data-functype="POPUP" data-size="mini"
               data-target="editT8ProdSalesInfoDisPopup"
               v-if="global.isShowAuthorityButton('T8ProdSalesInfo.updateT8ProdSalesInfoDistributor')">
          <md-icon>edit</md-icon>
        </k-btn>
        <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT"
               data-action="T8ProdSalesInfoDistributor.deleteT8ProdSalesInfoDistributor" data-size="mini"
               data-type="danger" data-target="prodSalesInfoGrid" :data-confirm="true" data-descript="删除销售商份额"
               v-if="global.isShowAuthorityButton('T8ProdSalesInfo.deleteT8ProdSalesInfoDistributor')">
          <md-icon>close</md-icon>
        </k-btn>
      </template>
    </k-grid>


    <k-popup ref="addT8ProdSalesInfoDisPopup" data-title="添加销售商份额">
      <ProdSaleInfoDistributorPopup v-model="fromDataDistributor" :fromDataDistributor="fromDataDistributor" ref="addT8ProdSalesInfoDisData" :openType="'add'" />
      <k-form>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdSalesInfo.addT8ProdSalesInfoDistributor"
                 data-from="addT8ProdSalesInfoDisForm" :data-handler="addValidateDataDis"
                 :data-model="fromDataDistributor" data-target="prodSalesInfoGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="editT8ProdSalesInfoDisPopup" data-title="修改销售商份额">
      <ProdSaleInfoDistributorPopup v-model="fromDataDistributor" :fromDataDistributor="fromDataDistributor" ref="editT8ProdSalesInfoDisData" openType="edit"/>
      <k-form>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdSalesInfoDistributor.updateT8ProdSalesInfoDistributor"
                 data-from="editT8ProdSalesInfoDisForm" :data-handler="editValidateDataDis"
                 :data-model="fromDataDistributor" data-target="prodSalesInfoGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>





    <k-popup ref="addT8ProdSalesInfoPopup" data-title="添加份额">
      <ProdSaleInfoPopup v-model="formData" :formData="formData" ref="addT8ProdSalesInfoData" />
      <k-form>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdSalesInfo.addT8ProdSalesInfo"
                 data-from="addT8ProdSalesInfoForm" :data-handler="addValidateData"
                 :data-model="formData" data-target="prodDealInfoGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>


    <k-popup ref="editT8ProdSalesInfoPopup" data-title="修改份额">
      <ProdSaleInfoPopup v-model="formData" :formData="formData" ref="editT8ProdSalesInfoData" />
      <k-form>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdSalesInfo.updateT8ProdSalesInfo"
                 data-from="editT8ProdSalesInfoForm" :data-handler="editValidateData"
                 :data-model="formData" data-target="prodDealInfoGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

  </div>
</template>

<script>
  import {assign} from "lodash";
  import ProdSaleInfoPopup from "./prodSaleInfoPopup";
  import ProdSaleInfoDistributorPopup from "./prodSaleInfoDistributorPopup";
  export default {
    name: "",
    components: {ProdSaleInfoPopup,ProdSaleInfoDistributorPopup},
    data() {
      return {
        prodSearchParam: {
          prodCode:'',
          changeDate:''
        },
        selectRowData:{},
        formData :{},

        selectRowDataDistributor:{},
        fromDataDistributor:{},
      }
    },
    methods:{
      addValidateDataDis() {
        return this.$refs.addT8ProdSalesInfoDisData.validateData();
      },
      editValidateDataDis() {
        return this.$refs.editT8ProdSalesInfoDisData.validateData();
      },
      addValidateData() {
        return this.$refs.addT8ProdSalesInfoData.validateData();
      },
      editValidateData() {
        return this.$refs.editT8ProdSalesInfoData.validateData();
      },

      selectRowDis(row, column, event){
        this.selectRowDataDistributor = assign({}, row);
        this.fromDataDistributor = assign({}, row);
      },

      selectRow(row, column, event){
        this.selectRowData = assign({}, row);
        this.formData = assign({}, row);
        this.fromDataDistributor = assign({}, row);
        this.$set(this.fromDataDistributor,'t8ProdImpinfoId',this.formData.id);
        this.$set(this.fromDataDistributor,'id','');
        this.$refs.prodSalesInfoGrid.load(
          {
            t8ProdImpinfoId:this.formData.id,
          });
      }
    }
  }
</script>

<style scoped>

</style>
