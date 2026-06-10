<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="MidDirectFusion" data-target="InfoModelGrid" v-model = "searchParam" data-label-width="70px">

        <k-form-item label="资产名称">
          <k-field-select v-model="searchParam.scrCd" data-action="MidDirectFusion.findScrCd" ref="scrCdId" :dataRemote="true"
                          data-display-field="scrCd,scrNm" data-value-field="scrCd" />
        </k-form-item>

        <k-form-item label="起息日">
          <k-field-date v-model="dateRange1" data-type="daterange" data-date-format="yyyyMMdd"
                        data-value-format="yyyyMMdd"></k-field-date>
        </k-form-item>

        <k-form-item label="到期日">
          <k-field-date v-model="dateRange2" data-type="daterange" data-date-format="yyyyMMdd"
                        data-value-format="yyyyMMdd"></k-field-date>
        </k-form-item>
       
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="addDataHandler" data-target="addInfo" slot="button"
                v-if="global.isShowAuthorityButton('MidDirectFusion.addMidDirectFusion')">
            <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
          <k-btn slot="button"  class="btn-custom-plain"  data-functype="EXPORT" data-target="InfoModelGrid" :data-export-name="'直融信息导出'"
                v-if="global.isShowAuthorityButton('MidDirectFusion.dirExcelDownloadAction')">
            <md-icon>cloud_download</md-icon>
            导出
          </k-btn>
        </div>
      </div>
      <k-grid ref="InfoModelGrid" @data-row-select="selectRow" data-operate-width="160px" data-action="MidDirectFusion.findMidDirectFusionInfos"
              v-if="global.isShowAuthorityButton('MidDirectFusion.findMidDirectFusionInfos')">
        <k-grid-column data-header="证券编码" data-name="scrId" data-hidden="true"/>
        <k-grid-column data-header="资产代码" data-name="scrCd"/>
        <k-grid-column data-header="资产名称" data-name="scrNm"/>
        <k-grid-column data-header="起息日" data-name="beginDate" data-type="date"/>
        <k-grid-column data-header="到期日" data-name="endDate" data-type="date"/>
        <k-grid-column data-header="票面利率" data-name="couponRate"/>
        <k-grid-column data-header="发行机构所属行业" data-name="publisherTradePb" data-dict="isuOrgBlgIdt" />
        <k-grid-column data-header="发行机构企业规模" data-name="publisherScalePb" data-dict="debtor_type" data-width="120"/>
        <k-grid-column data-header="版本号" data-name="version"></k-grid-column>


        <k-grid-column data-header="发行方式" data-name="issMod" data-hidden="true" data-dict="iss_mode_bond"/>
        <k-grid-column data-header="发行主体" data-name="issuer" data-hidden="true"/>
        <k-grid-column data-header="发行机构类型（按经济类型划分）" data-name="isuOrgTypEcn" data-hidden="true" data-dict="isuOrgTypEcn"/>
        <k-grid-column data-header="发行机构类型（按规模划分）" data-name="isuOrgTypScaleSiz" data-hidden="true" data-dict="debtor_scale_type"/>
        <k-grid-column data-header="发行机构类型（按技术领域划分）" data-name="isuOrgTypTchno" data-hidden="true" data-dict="isuOrgTypTchno"/>
        <k-grid-column data-header="中债发行机构所属行业" data-name="publisherTrade" data-hidden="true" data-dict="isuOrgBlgIdt"/>
        <k-grid-column data-header="登记托管机构" data-name="regTrstOrg" data-hidden="true" data-dict="regTrstOrg"/>
        <k-grid-column data-header="登记托管机构说明" data-name="regTrstOrgCmt" data-hidden="true"/>
        <k-grid-column data-header="资产全称" data-name="scrFullNm" data-hidden="true"/>
        <k-grid-column data-header="具体类别" data-name="spcType" data-hidden="true" data-dict ="spcType"/>
        <k-grid-column data-header="主体评级（外部）" data-name="subLevel" data-hidden="true" data-dict ="mainRating"/>
        <k-grid-column data-header="市场" data-name="trxMkt" data-hidden="true" data-dict ="market_asset"/>
        <k-grid-column data-header="交易流通场所" data-name="trxPla" data-hidden="true" data-dict="tacdingPlace"/>
        <k-grid-column data-header="人行二级分类" data-name="pbnkScdCtg" data-hidden="true" data-dict ="pbnkScdCtg"/>
        <k-grid-column data-header="债项当前评级（外部）" data-name="bondCredit" data-hidden="true" data-dict ="mainRating"/>
        <k-grid-column data-header="利差(%)" data-name="bondSpread" data-hidden="true"/>
        <k-grid-column data-header="中债一级分类" data-name="cbndFrsCtg" data-hidden="true" data-dict ="cbndFrsCtg"/>
        <k-grid-column data-header="备注" data-name="cmt" data-hidden="true"/>
        <k-grid-column data-header="G06二级分类" data-name="ggCbcSubType" data-hidden="true" data-dict ="g06_scd_type"/>
        <k-grid-column data-header="G06一级分类" data-name="ggCbcType" data-hidden="true" data-dict ="g06_first_type"/>
        <k-grid-column data-header="担保人评级（外部）" data-name="grntRat" data-hidden="true" data-dict ="mainRating"/>
        <k-grid-column data-header="担保方式" data-name="guarType" data-hidden="true" data-dict="grntWay"/>
        <k-grid-column data-header="担保机构" data-name="guaranteer" data-hidden="true"/>
        <k-grid-column data-header="计息基础" data-name="interestBase" data-hidden="true" data-dict="intrBas"/>
        <k-grid-column data-header="计息方式" data-name="interestMode" data-hidden="true" data-dict="intrMth"/>
        <k-grid-column data-header="息票品种" data-name="interestType" data-hidden="true" data-dict="couponType"/>
        <k-grid-column data-header="是否含权" data-name="isExercise" data-hidden="true" data-dict="1yes2no"/>
        <k-grid-column data-header="是否提前还本" data-name="isRepaid" data-hidden="true" data-dict="1yes2no"/>
        <k-grid-column data-header="发行价(元)" data-name="issuePrice" data-hidden="true"/>
        <k-grid-column data-header="发行量(亿元)" data-name="issueVolume" data-hidden="true"/>
        <k-grid-column data-header="债项发行评级" data-name="isuBndRat" data-hidden="true" data-dict ="mainRating"/>
        <k-grid-column data-header="付息频率" data-name="payFreq" data-hidden="true" data-dict="payIntrFrq"/>
        <k-grid-column data-header="人行一级分类" data-name="pbnkFrsCtg" data-hidden="true" data-dict="asseFrsCtg"/>
        <k-grid-column data-header="人行三级分类" data-name="pbnkTrdCtg" data-hidden="true" data-dict ="pbnkTrdCtg"/>
        <k-grid-column data-header="中债二级分类" data-name="cbndScdCtg" data-hidden="true" data-dict ="cbndScdCtg"/>

        <k-grid-column data-header="创建日期" data-name="crtDate" data-hidden="true" data-export="false" />
        <k-grid-column data-header="创建时间" data-name="crtTime" data-hidden="true" data-export="false" />
        <k-grid-column data-header="创建人" data-name="crtUser" data-hidden="true" data-export="false" />
        <k-grid-column data-header="修改日期" data-name="updDate" data-hidden="true" data-export="false" />
        <k-grid-column data-header="修改时间" data-name="updTime" data-hidden="true" data-export="false" />
        <k-grid-column data-header="修改人" data-name="updUser" data-hidden="true" data-export="false" />
        <k-grid-column data-header="处理日期" data-name="dealDate" data-hidden="true" data-export="false"/>


        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text specialClass" data-descript="修改直融信息" data-functype="POPUP" data-size="mini"  v-if="global.isShowAuthorityButton('MidDirectFusion.updateMidDirectFusion')"
                 data-target="editInfo">
            修改
          </k-btn>
          <k-btn class="btn-custom-text specialClass" data-descript="补录直融信息" data-functype="POPUP" data-size="mini" v-if="global.isShowAuthorityButton('MidDirectFusion.updateMidDirectSupplyFusion')"
                 data-target="blInfo">
            补录
          </k-btn>
          <k-btn class="btn-custom-text specialClass" data-functype="SUBMIT" data-action="MidDirectFusion.deleteMidDirectFusion" data-size="mini" v-if="global.isShowAuthorityButton('MidDirectFusion.deleteMidDirectFusion')"
                 data-type="danger" data-target="InfoModelGrid" :data-confirm="true" data-descript="删除直融信息">
            删除
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    添加弹出框   -->
    <k-popup ref="addInfo" data-title="新增" :dataDialogDrag="true">
      <EditComp ref="addComp" @loadGriding="loadGriding"
                :info="{}"
                :disabledVal="false"/>
    </k-popup>

    <!--  修改弹出框   -->
    <k-popup ref="editInfo" data-title="修改" :dataDialogDrag="true">
      <EditComp  ref="editComp" @loadGriding="loadGriding"
                 :info="formData"
                 :disabledVal="true"/>
    </k-popup>

    <!--  补录弹出框   -->
    <k-popup ref="blInfo" data-title="补录" :dataDialogDrag="false" data-width="55%">
      <div class="modal">
        <el-aside width="0%">
          <el-menu default-active="activeMenu" @select="handleMenuSelect" >
            <el-menu-item index="1">基础信息</el-menu-item>
            <el-menu-item index="2">补录信息</el-menu-item>
          </el-menu>
        </el-aside>
        <div class="modal-content">
          <EditComp  ref="editComp" @loadGriding="loadGriding"
                     :info="formData"
                     :disabledVal="true"
                     :isDetailShow="true"/>
          <BlComp  ref="blComp" @loadGriding="loadGriding"
                   :info="formData"
                   :embOptFGrid="embOptFGridData"
                   :isRepaidGrid="isRepaidGridData"
                   :couponTypeGrid="couponTypeGridData"
                   :disabledVal="true"/>
        </div>
      </div>
    </k-popup>

  </div>
</template>

<script>
import EditComp from "@/pages/pms/asset/directFusionTool/MidDirectFusionEdit";
import BlComp from "@/pages/pms/asset/directFusionTool/MidDirectFusionCollection";
import Vue from "vue";
const BlCompIns = new Vue(BlComp);
const EditCompIns = new Vue(EditComp);

export default {
  name:"MidDirectFusion",
  components: {
    BlComp,
    EditComp
  },
  data() {
    return {
      activeMenu: '1',
      dateRange1:[],
      dateRange2:[],
      formData: {},
      selectRowData: {},
      searchParam:{},
      embOptFGridData: {},
      isRepaidGridData: {},
      couponTypeGridData: {},
    };
  },
  created() {

  },
  methods: {
    handleMenuSelect(index){
      
      switch (index) {
        case '1': EditCompIns.MenuSelect(index); break;
        case '2': BlCompIns.MenuSelect(index); break;
        default: break;
      }
    },
    addDataHandler() {
      this.formData = {};
    },
    loadGriding(val){
      this.$refs.addInfo.close();
      this.$refs.editInfo.close();
      this.$refs.blInfo.close();
      this.$refs.InfoModelGrid.load(this.searchParam);
      this.$refs.scrCdId.load();
    },
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
      if (this.formData.isExercise == "01") {
        let fields = this.formData.embOptFGridData;
        this.$set(this.embOptFGridData, "rows", fields);
        this.$set(this.embOptFGridData, "total", fields.length);
      } else {
        this.$set(this.embOptFGridData, "rows", []);
        this.$set(this.embOptFGridData, "total", 0);
      }
      if (this.formData.isRepaid == "01") {
        let fields = this.formData.isRepaidGridData;
        this.$set(this.isRepaidGridData, "rows", fields);
        this.$set(this.isRepaidGridData, "total", fields.length);
      } else {
        this.$set(this.isRepaidGridData, "rows", []);
        this.$set(this.isRepaidGridData, "total", 0);
      }
      if (this.formData.interestType == "2") {
        let fields = this.formData.couponTypeGridData;
        this.$set(this.couponTypeGridData, "rows", fields);
        this.$set(this.couponTypeGridData, "total", fields.length);
      } else {
        this.$set(this.couponTypeGridData, "rows", []);
        this.$set(this.couponTypeGridData, "total", 0);
      }
    },
  },
  watch: {
    dateRange1() {
      this.$set(this.searchParam, 'valDtStart', this.dateRange1 == null ? '' : this.dateRange1[0]);
      this.$set(this.searchParam, 'valDtEnd', this.dateRange1 == null ? '' : this.dateRange1[1]);
    },
    dateRange2() {
      this.$set(this.searchParam, 'mtuDtStart', this.dateRange2 == null ? '' : this.dateRange2[0]);
      this.$set(this.searchParam, 'mtuDtEnd', this.dateRange2 == null ? '' : this.dateRange2[1]);
    },
  }
};
</script>
<style scoped lang="scss">
@import "../AssetComFunction.css";
</style>

