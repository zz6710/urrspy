<template>
  <div class="py-page">
    <k-form-search-customize data-target="midAssAsharedescriptionGrid" v-model="searchParam" data-label-width="100px">
      <k-form-item label="股票代码">
        <k-field-select v-model="searchParam.scrCd" data-action="MidAssAsharedescription.findScrCd" ref="scrCdId" :dataRemote="true"
                        data-display-field="scrCd,scrNm" data-value-field="scrCd"/>
      </k-form-item>
      <k-form-item label="市场">
        <k-field-select v-model="searchParam.trxMkt" :data-data="trxMktDict" data-display-field="TEXT"
                        data-value-field="VALUE"/>
      </k-form-item>
      <k-form-item label="股票类型">
        <k-field-select v-model="searchParam.stockType" data-dict="stock_type"/>
      </k-form-item>
      <k-form-item label="中债二级分类">
        <k-field-select v-model="searchParam.cbndScdCtg" data-dict="cbndScdCtg"/>
      </k-form-item>
      <k-form-item label="人行三级分类">
        <k-field-select v-model="searchParam.pbnkTrdCtg" data-dict="pbnkScdCtg"/>
      </k-form-item>

      
    </k-form-search-customize>
  <div class="py-page-container">
    <div class="table-top-btns">
      <div class="left">
        <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}"
             data-target="addMidAssAsharedescriptionPopup" v-if="global.isShowAuthorityButton('MidAssAsharedescription.addMidAssAsharedescription')">
          <md-icon md-src="/static/svg/add.svg" />新增</k-btn>

        <k-btn slot="button"  class="btn-custom-plain"  data-functype="EXPORT" data-target="midAssAsharedescriptionGrid" :data-export-name="'股票信息导出'"
              v-if="global.isShowAuthorityButton('MidAssAsharedescription.ashExcelDownloadAction')">
          <md-icon>cloud_download</md-icon>
          导出
        </k-btn>
      </div>
    </div>

    <k-grid ref="midAssAsharedescriptionGrid" @data-row-select="selectRow" data-action="MidAssAsharedescription.findMidAssAsharedescriptions"
            v-if="global.isShowAuthorityButton('MidAssAsharedescription.findMidAssAsharedescriptions')"
            data-operate-width="160px">

      <k-grid-column data-header="证券编码" data-name="scrId" data-hidden="true"/>
      <k-grid-column data-header="股票代码" data-name="scrCd"/>
      <k-grid-column data-header="股票名称" data-name="scrNm" />
      <k-grid-column data-header="市场" data-name="trxMkt" data-dict="market_asset"/>
      <k-grid-column data-header="股票类型" data-name="stockType" data-dict="stock_type"/>
      <k-grid-column data-header="融资企业名称" data-name="companyName" />
      <k-grid-column data-header="外部资讯分类" data-name="assInfClass" data-dict="assInfClassWb" />
      <k-grid-column data-header="中债二级分类" data-name="cbndScdCtg" data-dict="cbndScdCtg" />
      <k-grid-column data-header="人行三级分类" data-name="pbnkTrdCtg" data-dict="pbnkScdCtg" />
      <k-grid-column data-header="版本号" data-name="version" />



      <k-grid-column data-header="币种" data-name="ccy" data-hidden="true" data-dict="cur_type"/>
      <k-grid-column data-header="板块类型" data-name="plateType" data-hidden="true" data-dict="plateType"/>
      <k-grid-column data-header="交易流通场所" data-name="trxPla" data-hidden="true" data-dict="tacdingPlace"/>


      <k-grid-column data-header="中债一级分类" data-name="cbndFrsCtg" data-hidden="true" data-dict="cbndFrsCtg"/>
      <k-grid-column data-header="备注" data-name="cmt" data-hidden="true"/>
      <k-grid-column data-header="是否为债转股" data-name="debtEquitySwap" data-hidden="true" data-dict="1yes2no"/>
      <k-grid-column data-header="G06二级分类" data-name="ggCbcSubType" data-hidden="true" data-dict="g06_scd_type"/>
      <k-grid-column data-header="G06一级分类" data-name="ggCbcType" data-hidden="true" data-dict="g06_first_type"/>
      <k-grid-column data-header="机构所属行业（中债）" data-name="industryIssuer" data-hidden="true" data-dict="isuOrgBlgIdt"/>
      <k-grid-column data-header="投资阶段" data-name="investmentType" data-hidden="true" data-dict="invest_stage"/>
      <k-grid-column data-header="机构类型（按经济类型划分）" data-name="isuOrgTypEcn" data-hidden="true" data-dict="isuOrgTypEcn"/>
      <k-grid-column data-header="机构类型（按规模划分）" data-name="isuOrgTypSiz" data-hidden="true" data-dict="debtor_scale_type"/>
      <k-grid-column data-header="机构类型（按技术领域划分）" data-name="isuOrgTypTchno" data-hidden="true" data-dict="isuOrgTypTchno"/>
      <k-grid-column data-header="人行一级分类" data-name="pbnkFrsCtg" data-hidden="true" data-dict="asseFrsCtg"/>
      <k-grid-column data-header="人行二级分类" data-name="pbnkScdCtg" data-hidden="true" data-dict="pbnkFrsCtg"/>
      <k-grid-column data-header="是否为质押融资" data-name="pledgedFinace" data-hidden="true" data-dict="1yes2no"/>
      <k-grid-column data-header="股权退出安排" data-name="sharehold" data-type="date" data-hidden="true"/>


      <k-grid-column data-header="创建日期" data-name="crtDate" data-hidden="true" data-export="false" />
      <k-grid-column data-header="创建时间" data-name="crtTime" data-hidden="true" data-export="false" />
      <k-grid-column data-header="创建人" data-name="crtUser" data-hidden="true" data-export="false" />
      <k-grid-column data-header="修改日期" data-name="updDate" data-hidden="true" data-export="false" />
      <k-grid-column data-header="修改时间" data-name="updTime" data-hidden="true" data-export="false" />
      <k-grid-column data-header="修改人" data-name="updUser" data-hidden="true" data-export="false" />
      <k-grid-column data-header="处理日期" data-name="dealDate" data-hidden="true" data-export="false"/>



      <template slot="operate" slot-scope="scope">
        <k-btn class="btn-custom-text specialClass" data-descript="修改股票信息" data-functype="POPUP" data-size="mini"
               data-target="editMidAssAsharedescriptionPopup" v-if="global.isShowAuthorityButton('MidAssAsharedescription.updateMidAssAsharedescription')">
          修改
        </k-btn><k-btn class="btn-custom-text specialClass" data-descript="补录股票信息" data-functype="POPUP" data-size="mini"
               data-target="blMidAssAsharedescriptionPopup" v-if="global.isShowAuthorityButton('MidAssAsharedescription.updateMidAssSupplyAsharedescription')">
          补录
        </k-btn>
        <k-btn class="btn-custom-text specialClass" data-functype="SUBMIT" data-action="MidAssAsharedescription.deleteMidAssAsharedescription" data-size="mini"
               data-type="danger" data-target="midAssAsharedescriptionGrid" :data-confirm="true" data-descript="删除股票信息"
               v-if="global.isShowAuthorityButton('MidAssAsharedescription.deleteMidAssAsharedescription')" >
          删除
        </k-btn>
      </template>
    </k-grid>
  </div>

	<!--    添加弹出框   -->
    <k-popup ref="addMidAssAsharedescriptionPopup" data-title="新增" :dataDialogDrag="true">
      <EditComp @loadGriding="loadGriding" ref="addComp" :info="{}" :disabledVal="false"/>
    </k-popup>

    <!--  修改弹出框   -->
    <k-popup ref="editMidAssAsharedescriptionPopup" data-title="修改" :dataDialogDrag="true">
      <EditComp  @loadGriding="loadGriding" ref="editComp" :info="formData" :disabledVal="true"/>
    </k-popup>

    <!--  补录弹出框   -->
    <k-popup ref="blMidAssAsharedescriptionPopup" data-title="补录" :dataDialogDrag="false">
      <EditComp  @loadGriding="loadGriding" ref="editComp" :info="formData" :disabledVal="true" :isDetailShow="true"/>
      <BlComp  @loadGriding="loadGriding" ref="blComp" :info="formData" :disabledVal="true"/>
    </k-popup>
  </div>
</template>


<script>
import EditComp from "./MidAssAsharedescriptionEdit";
import BlComp from "./MidAssAsharedescriptionCont";
import AssetCommon from "@/pages/pms/asset/AssetComFunction";
  export default {
    name: "MidAssAsharedescription",
    components: {
      EditComp,
      BlComp
    },
    data() {
      return {
        formData: {},
        selectRowData: {},
        searchParam: {},//查询参数
        trxMktDict:{},
      };
    },
    created() {
      AssetCommon.areaDict(this,'trxMktDict','market_asset','1,2,10',false);
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      },

      loadGriding(val){
        this.$refs.addMidAssAsharedescriptionPopup.close();
        this.$refs.editMidAssAsharedescriptionPopup.close();
        this.$refs.blMidAssAsharedescriptionPopup.close();
        this.$refs.midAssAsharedescriptionGrid.load(this.searchParam);
        this.$refs.scrCdId.load();
      },

    }
  };
</script>

<style scoped lang="scss">
@import "../AssetComFunction.css";
</style>
