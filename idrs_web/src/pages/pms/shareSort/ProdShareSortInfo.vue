<template>
  <div>
    <div>
      <k-form-search-customize data-target="prodShareSortInfoGrid" v-model="queryParam">

        <k-form-item label="产品代码">
          <k-field-select v-model="param.t8ProdInfoId" data-action="T8ProdInfo.findT8ProdInfos"
                          :data-params="{isShareSort:1}"
                          data-display-field="prodCode,prodName" data-value-field="id"/>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="param.prodName" data-validate-type="text"/>
        </k-form-item>
        <k-form-item label="查询日期">
          <k-field-date v-model="queryDate" data-type="daterange" :data-allowblank="true"/>
        </k-form-item>


        <k-form-item label="份额名称">
          <k-field-select v-model="param.shareName" data-dict="t8_share_name"/>
        </k-form-item>

        <k-form-item label="发生事件">
          <k-field-select v-model="param.occurEvent" data-dict="t8_share_sort_event"/>
        </k-form-item>


        <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP"
               v-if="global.isShowAuthorityButton('ProdShareSortInfo.addProdShareSortInfo')"
               :data-handler="()=>this.formData={}" data-target="addProdShareSortInfoPopup">
          <md-icon md-src="/static/svg/add.svg" />新增
        </k-btn>
      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="prodShareSortInfoGrid" @data-row-select="selectRow" data-action="ProdShareSortInfo.findProdShareSortInfos" >
     <k-grid-column data-header="产品代码" data-name="prodCode"></k-grid-column>
     <k-grid-column data-header="产品名称" data-name="prodName"></k-grid-column>
     <k-grid-column data-header="份额名称" data-name="realShareName"></k-grid-column>
		<k-grid-column data-header="发生事件" data-name="occurEvent" data-dict="t8_share_sort_event"></k-grid-column>
		<k-grid-column data-header="发生日期" data-name="occurDate" data-type="date"></k-grid-column>
		<k-grid-column data-header="认申购份额" data-name="subsVol"></k-grid-column>
		<k-grid-column data-header="认申购金额" data-name="subsAmt"></k-grid-column>
		<k-grid-column data-header="赎回份额" data-name="redeemVol"></k-grid-column>
		<k-grid-column data-header="赎回金额" data-name="redeemAmt"></k-grid-column>
		<k-grid-column data-header="费用类型" data-name="feeType" data-dict="t8_fee_type_deal"></k-grid-column>
		<k-grid-column data-header="费用金额" data-name="feeMoney"></k-grid-column>
		<k-grid-column data-header="代销总额" data-name="proxySumMoney"></k-grid-column>
		<k-grid-column data-header="备注" data-name="remark"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改" data-functype="POPUP" data-size="mini"
                 data-target="editProdShareSortInfoPopup"
                 v-if="global.isShowAuthorityButton('ProdShareSortInfo.updateProdShareSortInfo')">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-info md-just-icon md-simple" data-descript="新增份额详情信息" :data-handler="addDetailHandler"
                 data-functype="POPUP" data-size="mini"
                 data-target="addProdShareSortDetailPopup"
                 v-if="global.isShowAuthorityButton('ProdShareSortDetail.addProdShareSortDetail')">
            <md-icon>add_circle</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT"
                 data-action="ProdShareSortInfo.deleteProdShareSortInfo" data-size="mini"
                 data-type="danger" data-target="prodShareSortInfoGrid" :data-confirm="true" data-descript="删除"
                 v-if="global.isShowAuthorityButton('ProdShareSortInfo.deleteProdShareSortInfo')">
            <md-icon>close</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </div>


    <div>
      <k-grid ref="prodShareSortDetailGrid" @data-row-select="selectDetailRow" data-action="ProdShareSortDetail.findProdShareSortDetails" :data-autoload="false">
        <k-grid-column data-header="产品代码" data-name="prodCode"></k-grid-column>
        <k-grid-column data-header="份额名称" data-name="realShareName"></k-grid-column>
        <k-grid-column data-header="销售商代码" data-name="distributorCode"></k-grid-column>
        <k-grid-column data-header="发生事件" data-name="occurEvent" data-dict="t8_share_sort_event"></k-grid-column>
        <k-grid-column data-header="发生日期" data-name="occurDate" data-type="date"></k-grid-column>
        <k-grid-column data-header="认申购份额" data-name="subsVol"></k-grid-column>
        <k-grid-column data-header="认申购金额" data-name="subsAmt"></k-grid-column>
        <k-grid-column data-header="赎回份额" data-name="redeemVol"></k-grid-column>
        <k-grid-column data-header="赎回金额" data-name="redeemAmt"></k-grid-column>
        <k-grid-column data-header="费用类型" data-name="feeType" data-dict="t8_fee_type_deal"></k-grid-column>
        <k-grid-column data-header="费用金额" data-name="feeMoney"></k-grid-column>
        <k-grid-column data-header="备注" data-name="remark"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改产品份额分类详情" data-functype="POPUP"
                 data-size="mini"
                 data-target="editProdShareSortDetailPopup"
                 v-if="global.isShowAuthorityButton('ProdShareSortDetail.updateProdShareSortDetail')">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT"
                 data-action="ProdShareSortDetail.deleteProdShareSortDetail" data-size="mini"
                 data-type="danger" data-target="prodShareSortDetailGrid" :data-confirm="true"
                 data-descript="删除产品份额分类详情"
                 v-if="global.isShowAuthorityButton('ProdShareSortDetail.deleteProdShareSortDetail')">
            <md-icon>close</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加弹出框   -->
	<k-popup ref="addProdShareSortInfoPopup" data-title="新增">
    	<k-form ref="addProdShareSortInfoForm" :data-col="2">
        <k-form-item label="产品代码">
          <k-field-select v-model="formData.t8ProdInfoId" data-action="T8ProdInfo.findT8ProdInfos"
                          :data-params="{isShareSort:1}"
                          data-display-field="prodCode,prodName" @data-on-change="prodIdChange" data-value-field="id"
                          :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="份额分类名称" :key="formData.t8ProdInfoId">
          <k-field-select ref="t8ProdShareSortIdRef" v-model="formData.t8ProdShareSortId"
                          data-action="ProdShareSort.findProdShareSortsByProdId"
                          data-display-field="realShareName" :data-params="{'t8ProdInfoId': formData.t8ProdInfoId}"
                          data-value-field="id" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="发生事件">
          <k-field-select v-model="formData.occurEvent" data-dict="t8_share_sort_event" :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="发生日期">
          <k-field-date v-model="formData.occurDate" :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="认申购份额">
          <k-field-text v-model="formData.subsVol" data-type="money" data-digits="2"
                        data-validate-type="money" data-integer-length="14"/>
        </k-form-item>
        <k-form-item label="认申购金额">
          <k-field-text v-model="formData.subsAmt" data-type="money" data-digits="2"
                        data-validate-type="money" data-integer-length="14"/>
        </k-form-item>
        <k-form-item label="赎回份额">
          <k-field-text v-model="formData.redeemVol" data-type="money" data-digits="2"
                        data-validate-type="money" data-integer-length="14"/>
        </k-form-item>
        <k-form-item label="赎回金额">
          <k-field-text v-model="formData.redeemAmt" data-type="money" data-digits="2"
                        data-validate-type="money" data-integer-length="14"/>
        </k-form-item>
			<k-form-item label="费用类型">
        <k-field-select v-model="formData.feeType" data-dict="t8_fee_type_deal"/>
      </k-form-item>
        <k-form-item label="费用金额">
          <k-field-text v-model="formData.feeMoney" data-type="money" data-digits="2"
                        data-validate-type="money" data-integer-length="14"/>
        </k-form-item>
        <k-form-item label="代销总额">
          <k-field-text v-model="formData.proxySumMoney" data-type="money" data-digits="2"
                        data-validate-type="money" data-integer-length="14"/>
        </k-form-item>
        <k-form-item label="备注" :data-col="2">
          <k-field-text v-model="formData.remark" :data-max-length="500" inputType="textarea" :rows="3"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ProdShareSortInfo.addProdShareSortInfo"
                 data-from="addProdShareSortInfoForm"
                 :data-model="formData" data-target="prodShareSortInfoGrid"
                 v-if="global.isShowAuthorityButton('ProdShareSortInfo.addProdShareSortInfo')">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改无弹出框   -->
	<k-popup ref="editProdShareSortInfoPopup" data-title="修改">
	  <k-form ref="editProdShareSortInfoForm" :data-col="2">
      <k-form-item label="id" v-show="false">
        <k-field-text v-model="formData.id"/>
      </k-form-item>
      <k-form-item label="产品代码">
        <k-field-select v-model="formData.t8ProdInfoId" data-action="T8ProdInfo.findT8ProdInfos"
                        :data-params="{isShareSort:1}"
                        data-display-field="prodCode,prodName" @data-on-change="prodIdChange" data-value-field="id"
                        :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="份额分类名称" >
        <k-field-select ref="t8ProdShareSortIdRef" v-model="formData.t8ProdShareSortId"
                        data-action="ProdShareSort.findProdShareSortsByProdId"
                        data-display-field="realShareName" :data-params="{'t8ProdInfoId': formData.t8ProdInfoId}"
                        data-value-field="id" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="发生事件">
        <k-field-select v-model="formData.occurEvent" data-dict="t8_share_sort_event" :data-allowblank="false"/>
      </k-form-item>
		<k-form-item label="发生日期">
        	<k-field-date v-model="formData.occurDate"/>
     	</k-form-item>
		<k-form-item label="认申购份额">
      <k-field-text v-model="formData.subsVol" data-type="money" data-digits="2"
                    data-validate-type="money" data-integer-length="14"/>
     	</k-form-item>
		<k-form-item label="认申购金额">
      <k-field-text v-model="formData.subsAmt" data-type="money" data-digits="2"
                    data-validate-type="money" data-integer-length="14"/>
     	</k-form-item>
		<k-form-item label="赎回份额">
      <k-field-text v-model="formData.redeemVol" data-type="money" data-digits="2"
                    data-validate-type="money" data-integer-length="14"/>
     	</k-form-item>
		<k-form-item label="赎回金额">
      <k-field-text v-model="formData.redeemAmt" data-type="money" data-digits="2"
                    data-validate-type="money" data-integer-length="14"/>
     	</k-form-item>
		<k-form-item label="费用类型">
      <k-field-select v-model="formData.feeType" data-dict="t8_fee_type_deal"/>
    </k-form-item>
      <k-form-item label="费用金额">
        <k-field-text v-model="formData.feeMoney" data-type="money" data-digits="2"
                      data-validate-type="money" data-integer-length="14"/>
      </k-form-item>
      <k-form-item label="代销总额">
        <k-field-text v-model="formData.proxySumMoney" data-type="money" data-digits="2"
                      data-validate-type="money" data-integer-length="14"/>
      </k-form-item>
      <k-form-item label="备注" :data-col="2">
        <k-field-text v-model="formData.remark" :data-max-length="1000" inputType="textarea" :rows="3"/>
      </k-form-item>

      <k-form-footer data-align="center">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ProdShareSortInfo.updateProdShareSortInfo"
               data-from="editProdShareSortInfoForm"
               :data-model="formData" data-target="prodShareSortInfoGrid"
               v-if="global.isShowAuthorityButton('ProdShareSortInfo.updateProdShareSortInfo')">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">
          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
        </k-btn>
	    </k-form-footer>
	  </k-form>
	</k-popup>

    <!--    添加产品份额分类详情弹出框   -->
    <k-popup ref="addProdShareSortDetailPopup" data-title="新增份额详情信息">
      <k-form ref="addProdShareSortDetailForm" :data-col="2">
        <k-form-item label="产品份额分类信息id" v-show="false">
          <k-field-text v-model="datailFormData.t8ProdShareSortInfoId"/>
        </k-form-item>
        <k-form-item label="销售商代码">
          <k-field-select v-model="datailFormData.distributorCode" data-action="T8Dict.findTaDistributorInfos" :dataAllowblank='false'
                        data-display-field="distributorName"  data-value-field="distributorCode"/>
        </k-form-item>
        <k-form-item label="发生事件">
          <k-field-select v-model="datailFormData.occurEvent" data-dict="t8_share_sort_event" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="发生日期">
          <k-field-date v-model="datailFormData.occurDate" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="认申购份额">
          <k-field-text v-model="datailFormData.subsVol" data-type="money" data-digits="2"
                        data-validate-type="money" data-integer-length="14"/>
        </k-form-item>
        <k-form-item label="认申购金额">
          <k-field-text v-model="datailFormData.subsAmt" data-type="money" data-digits="2"
                        data-validate-type="money" data-integer-length="14"/>
        </k-form-item>
        <k-form-item label="赎回份额">
          <k-field-text v-model="datailFormData.redeemVol" data-type="money" data-digits="2"
                        data-validate-type="money" data-integer-length="14"/>
        </k-form-item>
        <k-form-item label="赎回金额">
          <k-field-text v-model="datailFormData.redeemAmt" data-type="money" data-digits="2"
                        data-validate-type="money" data-integer-length="14"/>
        </k-form-item>
        <k-form-item label="费用类型">
          <k-field-select v-model="datailFormData.feeType" data-dict="t8_fee_type_deal"/>
        </k-form-item>
        <k-form-item label="费用金额">
          <k-field-text v-model="datailFormData.feeMoney" data-type="money" data-digits="2"
                        data-validate-type="money" data-integer-length="14"/>
        </k-form-item>
        <k-form-item label="币种">
          <k-field-select v-model="datailFormData.cur" data-dict="t8_prod_currtype_more" :data-default-value="'CNY'"
                          :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="备注" :data-col="2">
          <k-field-text v-model="datailFormData.remark" :data-max-length="500" inputType="textarea" :rows="3"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ProdShareSortDetail.addProdShareSortDetail"
                 data-from="addProdShareSortDetailForm"
                 :data-model="datailFormData" data-target="prodShareSortDetailGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改产品份额分类详情弹出框   -->
    <k-popup ref="editProdShareSortDetailPopup" data-title="修改产品份额分类详情">
      <k-form ref="editProdShareSortDetailForm" :data-col="2">
        <k-form-item label="产品份额分类信息id" v-show="false">
          <k-field-text v-model="datailFormData.t8ProdShareSortInfoId"/>
        </k-form-item>
        <k-form-item label="id" v-show="false">
          <k-field-text v-model="datailFormData.id"/>
        </k-form-item>
        <k-form-item label="销售商代码">
          <k-field-select v-model="datailFormData.distributorCode" data-action="T8Dict.findTaDistributorInfos" :dataAllowblank='false'
                          data-display-field="distributorName"  data-value-field="distributorCode" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="发生事件">
          <k-field-select v-model="datailFormData.occurEvent" data-dict="t8_share_sort_event" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="发生日期">
          <k-field-date v-model="datailFormData.occurDate" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="认申购份额">
          <k-field-text v-model="datailFormData.subsVol" data-type="money" data-digits="2"
                        data-validate-type="money" data-integer-length="14"/>
        </k-form-item>
        <k-form-item label="认申购金额">
          <k-field-text v-model="datailFormData.subsAmt" data-type="money" data-digits="2"
                        data-validate-type="money" data-integer-length="14"/>
        </k-form-item>
        <k-form-item label="赎回份额">
          <k-field-text v-model="datailFormData.redeemVol" data-type="money" data-digits="2"
                        data-validate-type="money" data-integer-length="14"/>
        </k-form-item>
        <k-form-item label="赎回金额">
          <k-field-text v-model="datailFormData.redeemAmt" data-type="money" data-digits="2"
                        data-validate-type="money" data-integer-length="14"/>
        </k-form-item>
        <k-form-item label="费用类型">
          <k-field-select v-model="datailFormData.feeType" data-dict="t8_fee_type_deal"/>
        </k-form-item>
        <k-form-item label="费用金额">
          <k-field-text v-model="datailFormData.feeMoney" data-type="money" data-digits="2"
                        data-validate-type="money" data-integer-length="14"/>
        </k-form-item>
        <k-form-item label="币种">
          <k-field-select v-model="datailFormData.cur" data-dict="t8_prod_currtype_more"
                          :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="备注" :data-col="2">
          <k-field-text v-model="datailFormData.remark" :data-max-length="500" inputType="textarea" :rows="3"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ProdShareSortDetail.updateProdShareSortDetail"
                 data-from="editProdShareSortDetailForm"
                 :data-model="datailFormData" data-target="prodShareSortDetailGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        formData: {},
        selectRowData: {},
        param: {},
        queryDate: [],
        datailFormData: {}
      };
    },
    computed: {
      queryParam() {
        return {
          'prodName': this.param.prodName,
          't8ProdInfoId': this.param.t8ProdInfoId,
          't8ProdShareSortId': this.param.t8ProdShareSortId,
          'occurEvent': this.param.occurEvent,
          'querStartDate': this.queryDate ? this.queryDate[0] : null,
          'querEndDate': this.queryDate ? this.queryDate[1] : null,
          'shareName': this.param.shareName
        }
      }
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
        this.$refs.prodShareSortDetailGrid.load({'t8ProdShareSortInfoId': row.id})
      },
      addDetailHandler(val) {
        this.datailFormData = {};
        this.datailFormData.t8ProdShareSortInfoId = val.id
      },
      prodIdChange(val) {
        this.$set(this.formData,'t8ProdShareSortId','')
        //this.formData.t8ProdShareSortId = '';
      },
      selectDetailRow(row, column, event) {
        this.datailFormData = Object.assign({}, row)
      }
    }
  };
</script>
