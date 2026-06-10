<template>
  <div>
    <div>
      <k-form-search-customize v-model="searchParam" data-target="GridFbassetHoldFronttenGrid">
      <k-form-item label="产品代码" data-label-width="150px">
          <k-field-text v-model="searchParam.prodCd" />
        </k-form-item>
      <k-form-item label="品种代码" data-label-width="150px">
          <k-field-select v-model="searchParam.bredCd"  data-dict ="bred_cd"/>
        </k-form-item>
      <k-form-item label="资产代码" data-label-width="150px">
        <k-field-select v-model="searchParam.scrId" data-action="GridFbassetHoldFrontten.findScrIdAndscrNm" :dataRemote="true"
                        data-display-field="scrId,scrNm" data-value-field="scrId" />
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
      <k-grid ref="GridFbassetHoldFronttenGrid" @data-row-select="selectRow" data-fixed="right" data-operate-width="250px"
              data-action="GridFbassetHoldFrontten.findGridFbassetHoldFrontten">
        <k-grid-column data-header="id" data-name="id" data-hidden="true" data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品代码" data-name="prodCd"></k-grid-column>
        <k-grid-column data-align="left" data-header="品种代码" data-name="bredCd"  data-dict ="bred_cd"></k-grid-column>
        <k-grid-column data-align="left" data-header="资产代码" data-name="scrId"></k-grid-column>
        <k-grid-column data-align="left" data-header="资产名称" data-name="scrNm"></k-grid-column>
        <k-grid-column data-align="right" data-header="余额(万元)" data-name="balAmt"></k-grid-column>
        <k-grid-column data-align="right" data-header="发布日期" data-name="posDt" ></k-grid-column>
        <k-grid-column data-align="right" data-header="数据日期" data-name="dealDt" ></k-grid-column>
        <k-grid-column data-align="right" data-header="创建日期" data-name="crtDt" ></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-plain" data-descript="修改前十项资产数据" data-functype="POPUP" data-size="mini"
                 data-target="editGridFbassetHoldFronttenPopup" :openType="'edit'" v-if="global.isShowAuthorityButton('GridFbassetHoldFrontten.updateGridFbassetHoldFrontten')">
            修改
          </k-btn>
           <k-btn class="md-danger" data-functype="SUBMIT" data-action="GridFbassetHoldFrontten.deleteGridFbassetHoldFrontten" data-size="mini"
              data-type="danger" data-target="GridFbassetHoldFronttenGrid" :data-confirm="true" data-descript="删除" v-if="global.isShowAuthorityButton('GridFbassetHoldFrontten.deleteGridFbassetHoldFrontten')">
              删除
          </k-btn>
        </template>
      </k-grid>

        <k-popup ref="editGridFbassetHoldFronttenPopup" data-title="修改">
                  <k-form ref="editGridFbassetHoldFronttenForm" :data-col="2" isFormBodyScreen>
                        <k-form-item label="id" v-if="false">
                            <k-field-text v-model="formData.id" />
                        </k-form-item>
                        <k-form-item label="产品代码">
                            <k-field-text v-model="formData.prodCd"   :data-allowblank="true" :data-disabled="false"  :data-max-length="100"/>
                        </k-form-item>
                        <k-form-item label="品种代码">
                            <k-field-select v-model="formData.bredCd"  data-dict ="bred_cd" :data-allowblank="true" :data-disabled="false"  :data-max-length="10"/>
                        </k-form-item>
                        <k-form-item label="资产代码">
                            <k-field-text v-model="formData.scrId"  :data-allowblank="true" :data-disabled="false"  :data-max-length="100"/>
                        </k-form-item>
                        <k-form-item label="资产名称">
                            <k-field-text v-model="formData.scrNm" :data-allowblank="true" :data-disabled="false"  :data-max-length="200"/>
                        </k-form-item>
                        <k-form-item label="余额(万元)">
                            <k-field-text v-model="formData.balAmt" :data-allowblank="true" :data-disabled="false" data-validate-type="money" data-type="money" data-integer-length="16" data-digits="2"/>
                        </k-form-item>
                        <k-form-item label="发布日期">
                             <k-field-date v-model="formData.posDt" data-type="date"   data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"  :data-allowblank="true" :data-disabled="false"/>
                        </k-form-item>
                      <k-form-footer data-align="center" slot="footer">
                      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="GridFbassetHoldFrontten.updateGridFbassetHoldFrontten" data-from="editGridFbassetHoldFronttenForm"
                          :data-model="formData" data-target="GridFbassetHoldFronttenGrid">
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
  name:"GridFbassetHoldFrontten",
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
