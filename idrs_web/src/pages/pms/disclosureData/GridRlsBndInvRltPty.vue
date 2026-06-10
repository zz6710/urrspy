<template>
  <div>
    <div>
      <k-form-search-customize v-model="searchParam" data-target="GridRlsBndInvRltPtyGrid">
        <k-form-item label="数据日期" data-label-width="150px">
            <k-field-date v-model="searchParam.dealDt" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
          </k-form-item>
        <k-form-item label="关联方名称" data-label-width="150px">
          <k-field-text v-model="searchParam.affiliateName" :data-max-length="200" />
        </k-form-item>
        <k-form-item label="证券代码" data-label-width="150px">
          <k-field-text v-model="searchParam.securitiesCode" :data-max-length="128" />
        </k-form-item>
        <k-form-item label="证券简称" data-label-width="150px">
          <k-field-text v-model="searchParam.securitiesName" :data-max-length="200" />
        </k-form-item>
        <k-form-item label="发行方关联方式" data-label-width="150px">
          <k-field-select v-model="searchParam.partyRelation"  data-dict ='partyRelation' />
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="GridRlsBndInvRltPtyGrid" @data-row-select="selectRow" data-fixed="right" data-operate-width="250px"
              data-action="GridRlsBndInvRltPty.findGridRlsBndInvRltPty">
        <k-grid-column data-header="id" data-name="id" data-hidden="true" data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="公告版本id" data-name="noticeVersionId"></k-grid-column>
        <k-grid-column data-align="left" data-header="数据日期" data-name="dealDt"></k-grid-column>
        <k-grid-column data-align="left" data-header="序号" data-name="orderNo"></k-grid-column>
        <k-grid-column data-align="left" data-header="关联方名称" data-name="affiliateName" data-width="200"></k-grid-column>
        <k-grid-column data-align="left" data-header="证券代码" data-name="securitiesCode"></k-grid-column>
        <k-grid-column data-align="left" data-header="证券简称" data-name="securitiesName"></k-grid-column>
        <k-grid-column data-align="right" data-header="交易金额（单位：元）" data-name="dealAmount" ></k-grid-column>
        <k-grid-column data-align="left" data-header="发行方关联方式" data-name="partyRelation" data-dict ='partyRelation'  ></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-plain" data-descript="修改债券数据" data-functype="POPUP" data-size="mini"
                 data-target="editGridRlsBndInvRltPtyPopup" :openType="'edit'" v-if="global.isShowAuthorityButton('GridRlsBndInvRltPty.updateGridRlsBndInvRltPty')">
            修改
          </k-btn>
           <k-btn class="md-danger" data-functype="SUBMIT" data-action="GridRlsBndInvRltPty.deleteGridRlsBndInvRltPty" data-size="mini"
              data-type="danger" data-target="GridRlsBndInvRltPtyGrid" :data-confirm="true" data-descript="删除" v-if="global.isShowAuthorityButton('GridRlsBndInvRltPty.deleteGridRlsBndInvRltPty')">
              删除
          </k-btn>
        </template>
      </k-grid>

        <k-popup ref="editGridRlsBndInvRltPtyPopup" data-title="修改">
                  <k-form ref="editGridRlsBndInvRltPtyForm" :data-col="2" isFormBodyScreen>
                      <k-form-item label="id" v-if="false">
                          <k-field-text v-model="formData.id" />
                      </k-form-item>
                      <k-form-item label="公告版本id">
                          <k-field-text v-model="formData.noticeVersionId"   :data-allowblank="true" :data-disabled="false"  :data-max-length="20"/>
                      </k-form-item>
                      <k-form-item label="数据日期">
                           <k-field-date v-model="formData.dealDt" data-type="date"   data-date-format="yyyyMMdd" data-value-format="yyyyMMdd" :data-allowblank="true" :data-disabled="false"/>
                      </k-form-item>
                      <k-form-item label="序号">
                          <k-field-text v-model="formData.orderNo" :data-allowblank="true" :data-disabled="false"  :data-max-length="100"/>
                      </k-form-item>
                      <k-form-item label="关联方名称">
                          <k-field-text v-model="formData.affiliateName"  :data-allowblank="true" :data-disabled="false"  :data-max-length="200"/>
                      </k-form-item>
                      <k-form-item label="证券代码">
                          <k-field-text v-model="formData.securitiesCode" :data-allowblank="true" :data-disabled="false"  :data-max-length="128"/>
                      </k-form-item>
                      <k-form-item label="证券简称">
                          <k-field-text v-model="formData.securitiesName" :data-allowblank="true" :data-disabled="false"  :data-max-length="20"/>
                      </k-form-item>
                      <k-form-item label="交易金额（单位：元）">
                          <k-field-text v-model="formData.dealAmount" :data-allowblank="true" :data-disabled="false" data-validate-type="money" data-type="money" data-integer-length="16" data-digits="2"/>
                      </k-form-item>
                      <k-form-item label="发行方关联方式">
                          <k-field-select v-model="formData.partyRelation" :data-allowblank="true" :data-disabled="false"  data-dict ='partyRelation'/>
                      </k-form-item>
                      <k-form-footer data-align="center" slot="footer">
                      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="GridRlsBndInvRltPty.updateGridRlsBndInvRltPty" data-from="editGridRlsBndInvRltPtyForm"
                          :data-model="formData" data-target="GridRlsBndInvRltPtyGrid">
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
