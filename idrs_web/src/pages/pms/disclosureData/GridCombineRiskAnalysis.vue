<template>
  <div>
    <div>
      <k-form-search-customize v-model="searchParam" data-target="GridCombineRiskAnalysisGrid">
      <k-form-item label="产品代码" data-label-width="150px">
          <k-field-text v-model="searchParam.prodCd" />
        </k-form-item>
      <k-form-item label="品种代码" data-label-width="150px">
          <k-field-select v-model="searchParam.bredCd"  data-dict ="bred_cd"/>
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
      <k-grid ref="GridCombineRiskAnalysisGrid" @data-row-select="selectRow" data-fixed="right" data-operate-width="250px"
              data-action="GridCombineRiskAnalysis.findGridCombineRiskAnalysis">
        <k-grid-column data-header="id" data-name="id" data-hidden="true" data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品代码" data-name="prodCd"></k-grid-column>
        <k-grid-column data-align="left" data-header="品种代码" data-name="bredCd"  data-dict ="bred_cd"></k-grid-column>
        <k-grid-column data-align="left" data-header="投资方式" data-name="investWay"></k-grid-column>
        <k-grid-column data-align="left" data-header="资产种类" data-name="investType"></k-grid-column>
        <k-grid-column data-align="right" data-header="余额(元)" data-name="balanceAmt"></k-grid-column>
        <k-grid-column data-align="left" data-header="发布日期" data-name="posDt"></k-grid-column>
        <k-grid-column data-align="left" data-header="数据日期" data-name="dealDt"></k-grid-column>
        <k-grid-column data-align="left" data-header="创建日期" data-name="crtDt"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-plain" data-descript="修改投资组合资产配置情况及流动性风险分析数据" data-functype="POPUP" data-size="mini"
                 data-target="editGridCombineRiskAnalysisPopup" :openType="'edit'" v-if="global.isShowAuthorityButton('GridCombineRiskAnalysis.updateGridCombineRiskAnalysis')">
            修改
          </k-btn>
           <k-btn class="md-danger" data-functype="SUBMIT" data-action="GridCombineRiskAnalysis.deleteGridCombineRiskAnalysis" data-size="mini"
              data-type="danger" data-target="GridCombineRiskAnalysisGrid" :data-confirm="true" data-descript="删除"  v-if="global.isShowAuthorityButton('GridCombineRiskAnalysis.deleteGridCombineRiskAnalysis')">
              删除
          </k-btn>
        </template>
      </k-grid>

        <k-popup ref="editGridCombineRiskAnalysisPopup" data-title="修改">
                  <k-form ref="editGridCombineRiskAnalysisForm" :data-col="2" isFormBodyScreen>
                      <k-form-item label="id" v-if="false">
                          <k-field-text v-model="formData.id" />
                      </k-form-item>
                      <k-form-item label="产品代码">
                          <k-field-text v-model="formData.prodCd"   :data-allowblank="true" :data-disabled="false"  :data-max-length="200"/>
                      </k-form-item>
                      <k-form-item label="品种代码">
                          <k-field-select v-model="formData.bredCd"   data-dict ="bred_cd"  :data-allowblank="true" :data-disabled="false"/>
                      </k-form-item>
                      <k-form-item label="投资方式">
                          <k-field-text v-model="formData.investWay"  :data-allowblank="true" :data-disabled="false"  :data-max-length="64"/>
                      </k-form-item>
                      <k-form-item label="资产种类">
                          <k-field-text v-model="formData.investType" :data-allowblank="true" :data-disabled="false"  :data-max-length="200"/>
                      </k-form-item>
                      <k-form-item label="余额(元)">
                          <k-field-text v-model="formData.balanceAmt" :data-allowblank="true" :data-disabled="false" data-validate-type="money" data-type="money" data-integer-length="16" data-digits="2"/>
                      </k-form-item>
                        <k-form-item label="发布日期">
                             <k-field-date v-model="formData.posDt" data-type="date"   data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"  :data-allowblank="true" :data-disabled="false"/>
                        </k-form-item>
                      <k-form-footer data-align="center" slot="footer">
                      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="GridCombineRiskAnalysis.updateGridCombineRiskAnalysis" data-from="editGridCombineRiskAnalysisForm"
                          :data-model="formData" data-target="GridCombineRiskAnalysisGrid">
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
  name:"GridCombineRiskAnalysis",
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
