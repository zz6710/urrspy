<template>
  <div>
    <div>
      <k-form-search-customize v-model="searchParam" data-target="GridAffiliateFeePayGrid">
      <k-form-item label="产品代码" data-label-width="150px">
          <k-field-text v-model="searchParam.prodCd" />
        </k-form-item>
        <k-form-item label="关联方名称" data-label-width="150px">
          <k-field-text v-model="searchParam.affiliateName" />
        </k-form-item>
       <k-form-item label="发布日期" data-label-width="150px">
            <k-field-date v-model="searchParam.posDt" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
          </k-form-item>
       <k-form-item label="数据日期" data-label-width="150px">
            <k-field-date v-model="searchParam.dealDt" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
          </k-form-item>
      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="GridAffiliateFeePayGrid" @data-row-select="selectRow" data-fixed="right" data-operate-width="250px"
              data-action="GridAffiliateFeePay.findGridAffiliateFeePay">
        <k-grid-column data-header="id" data-name="id" data-hidden="true" data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品代码" data-name="prodCd"></k-grid-column>
        <k-grid-column data-align="left" data-header="关联方名称" data-name="affiliateName"></k-grid-column>
        <k-grid-column data-align="left" data-header="费用类型" data-name="feeType"></k-grid-column>
        <k-grid-column data-align="left" data-header="发生金额（单位：元）" data-name="dealAmount"></k-grid-column>
        <k-grid-column data-align="left" data-header="发布日期" data-name="posDt"></k-grid-column>
        <k-grid-column data-align="right" data-header="数据日期" data-name="dealDt" ></k-grid-column>
        <k-grid-column data-align="right" data-header="创建日期" data-name="crtDt" ></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-plain" data-descript="修改支付关联方费用数据" data-functype="POPUP" data-size="mini"
                 data-target="editGridAffiliateFeePayPopup" :openType="'edit'" v-if="global.isShowAuthorityButton('GridAffiliateFeePay.updateGridAffiliateFeePay')">
            修改
          </k-btn>
           <k-btn class="md-danger" data-functype="SUBMIT" data-action="GridAffiliateFeePay.deleteGridAffiliateFeePay" data-size="mini"
              data-type="danger" data-target="GridAffiliateFeePayGrid" :data-confirm="true" data-descript="删除">
              删除
          </k-btn>
        </template>
      </k-grid>

        <k-popup ref="editGridAffiliateFeePayPopup" data-title="修改">
                  <k-form ref="editGridAffiliateFeePayForm" :data-col="2" isFormBodyScreen>
                      <k-form-item label="id" v-if="false">
                          <k-field-text v-model="formData.id" />
                      </k-form-item>
                      <k-form-item label="产品代码">
                          <k-field-text v-model="formData.prodCd"   :data-allowblank="true" :data-disabled="false"  :data-max-length="100"/>
                      </k-form-item>
                      <k-form-item label="关联方名称">
                          <k-field-text v-model="formData.affiliateName"  :data-allowblank="true" :data-disabled="false"  :data-max-length="200"/>
                      </k-form-item>
                      <k-form-item label="费用类型">
                          <k-field-text v-model="formData.feeType" :data-allowblank="true" :data-disabled="false" data-max-length="10"/>
                      </k-form-item>
                      <k-form-item label="发生金额（单位：元）">
                          <k-field-text v-model="formData.dealAmount" :data-allowblank="true" :data-disabled="false"   data-validate-type="money" data-type="money" data-digits="2"  data-integer-length="18"/>
                      </k-form-item>
                      <k-form-item label="发布日期">
                           <k-field-date v-model="formData.posDt" data-type="date"   data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"  :data-allowblank="true" :data-disabled="false"/>
                      </k-form-item>
                      <k-form-footer data-align="center" slot="footer">
                      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="GridAffiliateFeePay.updateGridAffiliateFeePay" data-from="editGridAffiliateFeePayForm"
                          :data-model="formData" data-target="GridAffiliateFeePayGrid">
                          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
                      </k-btn>
                      <k-btn class="btn-custom-plain" data-functype="CLOSE">
                          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
                      </k-form-footer>
                  </k-form>
                </k-popup>
    </div>
  </div>
</template>

<script>
import Tools from "@/utils/tools";

export default {
  name:"GridAffiliateFeePay",
  data() {
    return {
      addDocTypeDict:{},
      formData: {},
      selectRowData: {},
      DocTypeDict: {},
      searchParam: {},
    };
  },
  created() {
  },
  methods: {
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row);
      this.formData = Object.assign({}, row);
    },

  }
};
</script>
