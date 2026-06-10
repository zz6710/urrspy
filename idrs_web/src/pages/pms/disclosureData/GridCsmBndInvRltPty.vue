<template>
  <div>
    <div>
      <k-form-search-customize v-model="searchParam" data-target="GridCsmBndInvRltPtyGrid">
      <k-form-item label="产品代码" data-label-width="150px">
          <k-field-text v-model="searchParam.prodCd" />
        </k-form-item>
      <k-form-item label="品种代码" data-label-width="150px">
          <k-field-select v-model="searchParam.bredCd"  data-dict ="bred_cd"/>
        </k-form-item>
        <k-form-item label="关联方名称" data-label-width="150px">
          <k-field-text v-model="searchParam.affiliateName" :data-max-length="200" />
        </k-form-item>
         <k-form-item label="证券代码" data-label-width="150px">
           <k-field-select v-model="searchParam.securitiesCode" data-action="GridCsmBndInvRltPty.findSecuritiesCodeAndSecuritiesName" :dataRemote="true"
                           data-display-field="securitiesCode,securitiesName" data-value-field="securitiesCode" />
           </k-form-item>
        <k-form-item label="发行方关联方式" data-label-width="150px">
          <k-field-select v-model="searchParam.partyRelation"  data-dict ='partyRelation' />
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
      <k-grid ref="GridCsmBndInvRltPtyGrid" @data-row-select="selectRow" data-fixed="right" data-operate-width="250px"
              data-action="GridCsmBndInvRltPty.findGridCsmBndInvRltPty">
        <k-grid-column data-header="id" data-name="id" data-hidden="true" data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品代码" data-name="prodCd"  data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="品种代码" data-name="bredCd"  data-dict ="bred_cd" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="关联方名称" data-name="affiliateName" data-width="250"></k-grid-column>
        <k-grid-column data-align="left" data-header="证券代码" data-name="securitiesCode" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="证券简称" data-name="securitiesName" data-width="250"></k-grid-column>
        <k-grid-column data-align="right" data-header="交易金额（单位：元）" data-name="dealAmount"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="发行方关联方式" data-name="partyRelation" data-dict ='partyRelation'   data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="发布日期" data-name="posDt"  data-width="80"></k-grid-column>
        <k-grid-column data-align="right" data-header="数据日期" data-name="dealDt" data-width="80"></k-grid-column>
        <k-grid-column data-align="right" data-header="创建日期" data-name="crtDt" data-width="80"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-plain" data-descript="修改债券数据" data-functype="POPUP" data-size="mini"
                 data-target="editGridCsmBndInvRltPtyPopup" :openType="'edit'" v-if="global.isShowAuthorityButton('GridCsmBndInvRltPty.updateGridCsmBndInvRltPty')">
            修改
          </k-btn>
           <k-btn class="md-danger" data-functype="SUBMIT" data-action="GridCsmBndInvRltPty.deleteGridCsmBndInvRltPty" data-size="mini"
              data-type="danger" data-target="GridCsmBndInvRltPtyGrid" :data-confirm="true" data-descript="删除"  v-if="global.isShowAuthorityButton('GridCsmBndInvRltPty.deleteGridCsmBndInvRltPty')">
              删除
          </k-btn>
        </template>
      </k-grid>

        <k-popup ref="editGridCsmBndInvRltPtyPopup" data-title="修改">
                  <k-form ref="editGridCsmBndInvRltPtyForm" :data-col="2" isFormBodyScreen>
                      <k-form-item label="id" v-if="false">
                          <k-field-text v-model="formData.id" />
                      </k-form-item>
                      <k-form-item label="产品代码">
                          <k-field-text v-model="formData.prodCd"   :data-allowblank="true" :data-disabled="false"  :data-max-length="100"/>
                      </k-form-item>
                      <k-form-item label="品种代码">
                          <k-field-select v-model="formData.bredCd" :data-allowblank="true" :data-disabled="false"  data-dict ="bred_cd"/>
                      </k-form-item>
                      <k-form-item label="关联方名称">
                          <k-field-text v-model="formData.affiliateName"  :data-allowblank="true" :data-disabled="false"  :data-max-length="200"/>
                      </k-form-item>
                      <k-form-item label="资产代码">
                          <k-field-text v-model="formData.securitiesCode"  :data-allowblank="true" :data-disabled="false"  :data-max-length="128"/>
                      </k-form-item>
                      <k-form-item label="证券简称">
                          <k-field-text v-model="formData.securitiesName"  :data-allowblank="true" :data-disabled="false"  :data-max-length="200"/>
                      </k-form-item>
                      <k-form-item label="交易金额（单位：元）">
                          <k-field-text v-model="formData.dealAmount" :data-allowblank="true" :data-disabled="false" data-validate-type="money" data-type="money" data-digits="2"  data-integer-length="18"/>
                      </k-form-item>
                      <k-form-item label="发行方关联方式">
                          <k-field-select v-model="formData.partyRelation" :data-allowblank="true" :data-disabled="false"  data-dict ='partyRelation'/>
                      </k-form-item>
                        <k-form-item label="发布日期">
                             <k-field-date v-model="formData.posDt" data-type="date"   data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"  :data-allowblank="true" :data-disabled="false"/>
                        </k-form-item>
                      <k-form-footer data-align="center" slot="footer">
                      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="GridCsmBndInvRltPty.updateGridCsmBndInvRltPty" data-from="editGridCsmBndInvRltPtyForm"
                          :data-model="formData" data-target="GridCsmBndInvRltPtyGrid">
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
  name:"GridRlsBndInvRltPty",
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
