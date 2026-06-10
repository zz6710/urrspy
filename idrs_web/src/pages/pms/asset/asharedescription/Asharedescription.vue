
<template>
  <div>
    <div>
      <k-form-search-customize data-model-name="AsharedescriptionModel" data-target="AsharedescriptionModelGrid" v-model = "searchParam">
      <k-form-item label="市场"  data-label-width="150px">
                <k-field-select v-model="searchParam.trxMkt" data-dict ="market_stock" />
              </k-form-item>
        <k-form-item label="股票代码"  data-label-width="150px">
          <k-field-select v-model="searchParam.scrCd" data-action="AsharedescriptionModel.findAshareInfoIdAndNm" :dataRemote="true"
                          data-display-field="scrCd,scrNm" data-value-field="scrCd"/>
        </k-form-item>
        <k-form-item label="股票名称"  data-label-width="150px">
          <k-field-text v-model="searchParam.scrNm"/>
        </k-form-item>
        <k-form-item label="股票类型"  data-label-width="150px">
            <k-field-select v-model="searchParam.stockType" data-dict ="stock_type" />
          </k-form-item>
        <k-form-item label="中债二级分类"  data-label-width="150px">
          <k-field-select v-model="searchParam.cbndScdCtg" data-dict ="ashareCbndScdCtg" />
        </k-form-item>
        <k-form-item label="人行三级分类"  data-label-width="150px">
          <k-field-select v-model="searchParam.pbnkTrdCtg" data-dict = "pbnkTrdCtg"/>
        </k-form-item>
      <k-form-item label="版本号">
          <k-field-text v-model="searchParam.version"/>
      </k-form-item>
      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="AsharedescriptionModelGrid" @data-row-select="selectRow" data-fixed="right" data-operate-width="120px" data-action="AsharedescriptionModel.findAsharedesriptionInfo" >
         <k-grid-column  data-align="left" data-header="资产编号" data-name="scrId" data-hidden="true"  data-width="120"></k-grid-column>
          <k-grid-column  data-align="left" data-header="市场" data-name="trxMkt" data-dict="market_stock"  data-width="80"></k-grid-column>
          <k-grid-column  data-align="left" data-header="板块类型" data-name="plateType" data-dict="plateType"  data-width="80"></k-grid-column>
          <k-grid-column  data-align="left" data-header="股票代码" data-name="scrCd"  data-width="120"></k-grid-column>
          <k-grid-column  data-align="left" data-header="股票名称" data-name="scrNm"  data-width="180"></k-grid-column>
          <k-grid-column  data-align="left" data-header="公司名称" data-name="companyName"  data-width="180"></k-grid-column>
          <k-grid-column  data-align="left" data-header="币种" data-name="ccy" data-dict="cur_type"  data-width="120"></k-grid-column>
          <k-grid-column  data-align="left" data-header="中债一级分类" data-name="cbndFrsCtg" data-dict="cbndFrsCtg"  data-width="120"></k-grid-column>
          <k-grid-column  data-align="left" data-header="中债二级分类" data-name="cbndScdCtg" data-dict="ashareCbndScdCtg"  data-width="160"></k-grid-column>
          <k-grid-column  data-align="left" data-header="人行一级分类" data-name="pbnkFrsCtg" data-dict="pbnkFrsCtg"  data-width="160"></k-grid-column>
          <k-grid-column  data-align="left" data-header="人行二级分类" data-name="pbnkScdCtg" data-dict="pbnkScdCtg"  data-width="120"></k-grid-column>
          <k-grid-column  data-align="left" data-header="人行三级分类" data-name="pbnkTrdCtg" data-dict="pbnkTrdCtg"  data-width="160"></k-grid-column>
          <k-grid-column  data-align="left" data-header="股票类型" data-name="stockType" data-dict="stock_type"  data-width="80"></k-grid-column>
          <k-grid-column  data-align="left" data-header="投资阶段" data-name="investmentType" data-dict="invest_stage"  data-width="80"></k-grid-column>
          <k-grid-column  data-align="left" data-header="股权退出安排" data-name="sharehold"  data-width="80"></k-grid-column>
          <k-grid-column  data-align="left" data-header="机构类型（按规模划分）" data-name="isuOrgTypSiz" data-dict="instituteTypeTech"  data-width="120"></k-grid-column>
          <k-grid-column  data-align="left" data-header="机构类型（按技术领域划分）" data-name="isuOrgTypTchno" data-dict="isuOrgTypTchno"  data-width="120"></k-grid-column>
          <k-grid-column  data-align="left" data-header="机构类型（按经济类型划分）" data-name="isuOrgTypEcn" data-dict="isuOrgTypEcn"  data-width="120"></k-grid-column>
          <k-grid-column  data-align="left" data-header="机构所属行业" data-name="industryIssuer" data-dict="isuOrgBlgIdt"  data-width="120"></k-grid-column>
          <k-grid-column  data-align="left" data-header="是否为质押融资" data-name="pledgedFinace" data-dict="isTrue"  data-width="120"></k-grid-column>
          <k-grid-column  data-align="left" data-header="是否为债转股" data-name="debtEquitySwap" data-dict="isTrue"  data-width="120"></k-grid-column>
          <k-grid-column  data-align="left" data-header="备注" data-name="remark"   data-width="150"></k-grid-column>
          <k-grid-column  data-align="left" data-header="版本号" data-name="version"  data-width="60"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="补录股票信息" data-functype="POPUP" data-size="mini" v-if="global.isShowAuthorityButton('AsharedescriptionModel.addAsharedescriptionInfo')"
                 data-target="AsharedescriptionModelPopup">
            补录
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    补录股票信息字段   -->
    <k-popup ref="AsharedescriptionModelPopup" data-title="补录" :data-dialog-drag="true">
      <asharedescription-collection  :parentFormData="formData"
      ></asharedescription-collection>
    </k-popup>
  </div>
</template>

<script>
  import AsharedescriptionCollection from "@/pages/pms/asset/asharedescription/AsharedescriptionCollection";

  export default {
    name:"Asharedescription",
    components: {
      AsharedescriptionCollection,
    },
    data() {
      return {
        formData: {},
       selectRowData: {},
       searchParam:{},
       addDocTypeDict: {},
       valDate:[],
       isEdit:false,
       mtuDate:[],
       addDocTypeChangeDict:'',
      };
    },
created() {
  },

    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      },

    },

  };
</script>
