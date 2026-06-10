<template>
  <div>
    <div>
      <k-form-search-customize v-model="searchParam" data-target="gridFbassetHoldAnalysisGrid">
        <k-form-item label="产品代码" data-label-width="150px">
                 <k-field-text v-model="searchParam.prodCd" />
               </k-form-item>
             <k-form-item label="品种代码" data-label-width="150px">
                 <k-field-select v-model="searchParam.bredCd"  data-dict ="bred_cd"/>
               </k-form-item>
             <k-form-item label="资产代码" data-label-width="150px">
               <k-field-select v-model="searchParam.scrId" data-action="GridFbassetHoldAnalysis.findScrIdAndscrNm" :dataRemote="true"
                               data-display-field="scrId,scrNm" data-value-field="scrId" />
               </k-form-item>
               <k-form-item label="项目名称" data-label-width="150px">
                <k-field-text v-model="searchParam.projectName" />
              </k-form-item>
              <k-form-item label="发布日期" data-label-width="150px">
                   <k-field-date v-model="searchParam.posDt" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
                 </k-form-item>
              <k-form-item label="数据日期" data-label-width="150px">
                   <k-field-date v-model="searchParam.dealDt" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
                 </k-form-item>
             </k-form-search-customize>
      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="gridFbassetHoldAnalysisGrid" @data-row-select="selectRow" data-fixed="right" data-operate-width="250px"
              data-action="GridFbassetHoldAnalysis.findGridFbassetHoldAnalysis">
        <k-grid-column data-header="id" data-name="id" data-hidden="true" data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品代码" data-name="prodCd" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="品种代码" data-name="bredCd"  data-dict ="bred_cd"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="资产代码" data-name="scrId"  data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="资产名称" data-name="scrNm" data-width="250"></k-grid-column>
        <k-grid-column data-align="left" data-header="融资客户" data-name="financeCustomer"  data-width="250"></k-grid-column>
        <k-grid-column data-align="left" data-header="项目名称" data-name="projectName"  data-width="250"></k-grid-column>
        <k-grid-column data-align="left" data-header="剩余融资期限" data-name="leftDays"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="到期收益分配" data-name="incomeAllocate"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="交易结构" data-name="dealStructure"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="风险状况" data-name="riskConditions"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="发布日期" data-name="posDt"  data-width="80"></k-grid-column>
        <k-grid-column data-align="left" data-header="数据日期" data-name="dealDt"  data-width="80"></k-grid-column>
        <k-grid-column data-align="left" data-header="创建日期" data-name="crtDt"  data-width="80"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-plain" data-descript="修改非标资产持仓情况" data-functype="POPUP" data-size="mini"
                 data-target="editGridFbassetHoldAnalysisPopup" :openType="'edit'" v-if="global.isShowAuthorityButton('GridFbassetHoldAnalysis.updateGridFbassetHoldAnalysis')">
            修改
          </k-btn>
           <k-btn class="md-danger" data-functype="SUBMIT" data-action="GridFbassetHoldAnalysis.deleteGridFbassetHoldAnalysis" data-size="mini"
              data-type="danger" data-target="gridFbassetHoldAnalysisGrid" :data-confirm="true" data-descript="删除" v-if="global.isShowAuthorityButton('GridFbassetHoldAnalysis.deleteGridFbassetHoldAnalysis')">
              删除
          </k-btn>
        </template>
      </k-grid>

        <k-popup ref="editGridFbassetHoldAnalysisPopup" data-title="修改">
                  <k-form ref="editGridFbassetHoldAnalysisForm" :data-col="2" isFormBodyScreen>
                      <k-form-item label="id" v-if="false">
                          <k-field-text v-model="formData.id" />
                      </k-form-item>
                      <k-form-item label="产品代码">
                          <k-field-text v-model="formData.prodCd"   :data-allowblank="true" :data-disabled="false"  :data-max-length="100"/>
                      </k-form-item>
                      <k-form-item label="品种代码">
                           <k-field-select  v-model="formData.bredCd"  data-dict ="bred_cd"  :data-allowblank="true" :data-disabled="false"/>
                      </k-form-item>
                      <k-form-item label="资产代码">
                          <k-field-text v-model="formData.scrId" :data-allowblank="true" :data-disabled="false"  :data-max-length="100"/>
                      </k-form-item>
                      <k-form-item label="资产名称">
                          <k-field-text v-model="formData.scrNm"  :data-allowblank="true" :data-disabled="false"  :data-max-length="200"/>
                      </k-form-item>
                      <k-form-item label="融资客户">
                          <k-field-text v-model="formData.financeCustomer" :data-allowblank="true" :data-disabled="false"  :data-max-length="200"/>
                      </k-form-item>
                      <k-form-item label="项目名称">
                          <k-field-text v-model="formData.projectName" :data-allowblank="true" :data-disabled="false"  :data-max-length="200"/>
                      </k-form-item>
                      <k-form-item label="剩余融资期限">
                          <k-field-text v-model="formData.leftDays" :data-allowblank="true" :data-disabled="false"  :data-max-length="6"/>
                      </k-form-item>
                      <k-form-item label="到期收益分配">
                          <k-field-text v-model="formData.incomeAllocate" :data-allowblank="true" :data-disabled="false"  :data-max-length="64"/>
                      </k-form-item>
                      <k-form-item label="交易结构">
                          <k-field-text v-model="formData.dealStructure" :data-allowblank="true" :data-disabled="false"  :data-max-length="32"/>
                      </k-form-item>
                      <k-form-item label="风险状况">
                          <k-field-text v-model="formData.riskConditions" :data-allowblank="true" :data-disabled="false"  :data-max-length="32"/>
                      </k-form-item>
                     <k-form-item label="发布日期">
                          <k-field-date v-model="formData.posDt" data-type="date"   data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"  :data-allowblank="true" :data-disabled="false"/>
                     </k-form-item>
                      <k-form-footer data-align="center" slot="footer">
                      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="GridFbassetHoldAnalysis.updateGridFbassetHoldAnalysis" data-from="editGridFbassetHoldAnalysisForm"
                          :data-model="formData" data-target="gridFbassetHoldAnalysisGrid">
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
  name:"GridFbassetHoldAnalysis",
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
