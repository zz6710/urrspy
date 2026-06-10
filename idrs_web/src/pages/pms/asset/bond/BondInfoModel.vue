<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="BondInfoModel" data-target="bondInfoModelGrid" v-model = "searchParam">
      <k-form-item label="债券代码">
            <k-field-select v-model="searchParam.scrCd" data-action="BondInfoModel.findBondInfoModelsCdAndNm"  ref="scrCdId" :dataRemote="true"
                            data-display-field="scrCd,scrShtNm" data-value-field="scrCd" />
      </k-form-item>
      <k-form-item label="中债二级分类">
              <k-field-select v-model="searchParam.cbndScdCtg" data-dict="cbndScdCtg" />
      </k-form-item>
      <k-form-item label="市场">
              <k-field-select v-model="searchParam.trxMkt" data-dict="market_asset"/>
      </k-form-item>
       
       </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="addDataHandler" data-target="addBondInfo" slot="button" v-if="global.isShowAuthorityButton('BondInfoModel.addBondInfoModel')">
              <md-icon md-src="/static/svg/add.svg" />
            新增
          </k-btn>
            <k-btn slot="button"  class="btn-custom-plain"  data-functype="EXPORT" data-target="bondInfoModelGrid" :data-export-name="'债券信息导出'"
                  v-if="global.isShowAuthorityButton('BondInfoModel.bondExcelDownloadAction')">
              <md-icon>cloud_download</md-icon>
              导出
            </k-btn>
        </div>
      </div>
      <k-grid ref="bondInfoModelGrid" @data-row-select="selectRow" data-operate-width="160px"
              data-action="BondInfoModel.findBondInfoModels" v-if="global.isShowAuthorityButton('BondInfoModel.findBondInfoModels')">

        <k-grid-column data-header="编号" data-name="scrId" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="市场" data-name="trxMkt" data-dict="market_asset"></k-grid-column>
        <k-grid-column data-header="债券代码" data-name="scrCd"></k-grid-column>
        <k-grid-column data-header="债券名称" data-name="scrShtNm"></k-grid-column>
        <k-grid-column data-header="起息日" data-name="valDt" data-type="date"></k-grid-column>
        <k-grid-column data-header="到期日" data-name="mtuDt" data-type="date"></k-grid-column>
        <k-grid-column data-header="票面利率" data-name="parRat"></k-grid-column>
        <k-grid-column data-header="中债二级分类" data-name="cbndScdCtg" data-dict ="cbndScdCtg"></k-grid-column>
        <k-grid-column data-header="具体类别" data-name="spcType" data-dict ="spcType"></k-grid-column>
        <k-grid-column data-header="G06二级分类" data-name="ggCbcSubType" data-dict ="g06_scd_type"></k-grid-column>
        <k-grid-column data-header="人行三级分类" data-name="pbnkTrdCtg" data-dict ="pbnkScdCtg"></k-grid-column>
        <k-grid-column data-header="版本号" data-name="version"></k-grid-column>

        <!--隐藏字段-->
        <k-grid-column data-header="发行量(元)" data-name="actlIsuTotAmt" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="资讯分类" data-name="assInfClass" data-hidden="true" data-dict ="assInfClass"></k-grid-column>
        <k-grid-column data-header="债券当前评级（外部）" data-name="bondFrsRat" data-hidden="true" data-dict ="mainRating"></k-grid-column>
        <k-grid-column data-header="中债发行机构所属行业" data-name="ccIndustryIssuer" data-hidden="true" data-dict="isuOrgBlgIdt"></k-grid-column>
        <k-grid-column data-header="息票类型" data-name="couponType" data-hidden="true" data-dict="couponType"></k-grid-column>
        <k-grid-column data-header="债股类别" data-name="debtEquityClass" data-hidden="true" data-dict ="debtEquityClass"></k-grid-column>
        <k-grid-column data-header="是否含权" data-name="embOptF" data-hidden="true" data-dict="1yes2no"></k-grid-column>
        <k-grid-column data-header="担保机构" data-name="grnt" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="担保方式" data-name="grntMth" data-hidden="true" data-dict="grntMth"></k-grid-column>
        <k-grid-column data-header="担保人评级（外部）" data-name="grntRat" data-hidden="true" data-dict ="mainRating"></k-grid-column>
        <k-grid-column data-header="计息基础" data-name="intrBas" data-hidden="true" data-dict="intrBas"></k-grid-column>
        <k-grid-column data-header="计息方式" data-name="intrMth" data-hidden="true" data-dict="intrMth"></k-grid-column>
        <k-grid-column data-header="是否信用债" data-name="isCredit" data-hidden="true" data-dict="1yes0no"></k-grid-column>
        <k-grid-column data-header="是否提前还本" data-name="isRepaid" data-hidden="true" data-dict="1yes2no"></k-grid-column>
        <k-grid-column data-header="是否永续" data-name="isSustain" data-hidden="true" data-dict="1yes2no"></k-grid-column>
        <k-grid-column data-header="发行主体" data-name="isu" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="债项发行评级" data-name="isuBndRat" data-hidden="true" data-dict ="mainRating"></k-grid-column>
        <k-grid-column data-header="发行方式" data-name="isuMth" data-hidden="true" data-dict="iss_mode_bond"></k-grid-column>
        <k-grid-column data-header="中债发行机构类型（按经济类型划分）" data-name="isuOrgTypEcn" data-hidden="true" data-dict="isuOrgTypEcn"></k-grid-column>
        <k-grid-column data-header="中债发行机构类型（按规模划分）" data-name="isuOrgTypScaleSiz" data-hidden="true" data-dict="debtor_scale_type"></k-grid-column>
        <k-grid-column data-header="中债发行机构类型（按技术领域划分）" data-name="isuOrgTypTchno" data-hidden="true" data-dict="isuOrgTypTchno"></k-grid-column>
        <k-grid-column data-header="发行价(元)" data-name="isuPrc" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="主体评级（外部）" data-name="mainRat" data-hidden="true" data-dict ="mainRating"></k-grid-column>
        <k-grid-column data-header="原始权益人" data-name="orignInterestObject" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="付息频率" data-name="payIntrFrq" data-hidden="true" data-dict="payIntrFrq"></k-grid-column>
        <k-grid-column data-header="登记托管机构" data-name="regTrstOrg" data-hidden="true" data-dict="regTrstOrg"></k-grid-column>
        <k-grid-column data-header="债券全称" data-name="scrNm" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="利差(%)" data-name="sprd" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="交易流通场所" data-name="trxPla" data-hidden="true" data-dict="tacdingPlace"></k-grid-column>

        <k-grid-column data-header="中债一级分类" data-name="cbndFrsCtg" data-hidden="true" data-dict ="cbndFrsCtg"></k-grid-column>
        <k-grid-column data-header="备注" data-name="cmt" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="G06一级分类" data-name="ggCbcType" data-hidden="true" data-dict ="g06_first_type"></k-grid-column>
        <k-grid-column data-header="人行发行机构企业规模" data-name="isuOrgTypSiz" data-hidden="true" data-dict ="debtor_type"></k-grid-column>
        <k-grid-column data-header="人行一级分类" data-name="pbnkFrsCtg" data-hidden="true" data-dict="asseFrsCtg"></k-grid-column>
        <k-grid-column data-header="人行四级分类" data-name="pbnkFurCtg" data-hidden="true" data-dict ="pbnkTrdCtg"></k-grid-column>
        <k-grid-column data-header="人行发行机构所属行业" data-name="pbnkIndustryIssuer" data-hidden="true" data-dict="isuOrgBlgIdt"></k-grid-column>
        <k-grid-column data-header="人行二级分类" data-name="pbnkScdCtg" data-hidden="true" data-dict ="pbnkFrsCtg"></k-grid-column>



        <k-grid-column data-header="创建日期" data-name="crtDate" data-hidden="true" data-export="false" />
        <k-grid-column data-header="创建时间" data-name="crtTime" data-hidden="true" data-export="false" />
        <k-grid-column data-header="创建人" data-name="crtUser" data-hidden="true" data-export="false" />
        <k-grid-column data-header="修改日期" data-name="updDate" data-hidden="true" data-export="false" />
        <k-grid-column data-header="修改时间" data-name="updTime" data-hidden="true" data-export="false" />
        <k-grid-column data-header="修改人" data-name="updUser" data-hidden="true" data-export="false" />
        <k-grid-column data-header="处理日期" data-name="dealDate" data-hidden="true" data-export="false"/>


        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text specialClass" data-descript="修改债券信息" data-functype="POPUP" data-size="mini"
            data-target="editBondInfo"  v-if="global.isShowAuthorityButton('BondInfoModel.updateBondInfoModel')">
            修改
          </k-btn>
          <k-btn class="btn-custom-text specialClass" data-descript="补录债券信息" data-functype="POPUP" data-size="mini"
                 v-if="global.isShowAuthorityButton('BondInfoModel.blBondInfoModel')"
             data-target="blBondInfo">
             补录
          </k-btn>
          <k-btn class="btn-custom-text specialClass" data-functype="SUBMIT" data-action="BondInfoModel.deleteBondInfoModel" data-size="mini"  v-if="global.isShowAuthorityButton('BondInfoModel.deleteBondInfoModel')"
               data-type="danger" data-target="bondInfoModelGrid" :data-confirm="true" data-descript="删除债券信息">
          	删除
    	  </k-btn>
        </template>
      </k-grid>

      <!--    添加弹出框   -->
      <k-popup ref="addBondInfo" data-title="新增" :dataDialogDrag="true">
        <EditComp ref="addComp" @loadGriding="loadGriding"
                  :info="{}"
                  :embOptFGrid="embOptFGridData"
                  :isRepaidGrid="isRepaidGridData"
                  :couponTypeGrid="couponTypeGridData"
                  :disabledVal="false"/>
      </k-popup>

      <!--  修改弹出框   -->
      <k-popup ref="editBondInfo" data-title="修改" :dataDialogDrag="true">
        <EditComp  ref="editComp" @loadGriding="loadGriding"
                   :info="formData"
                   :embOptFGrid="embOptFGridData"
                   :isRepaidGrid="isRepaidGridData"
                   :couponTypeGrid="couponTypeGridData"
                   :disabledVal="true"/>
      </k-popup>

      <!--  补录弹出框   -->
      <k-popup ref="blBondInfo" data-title="补录" :dataDialogDrag="false" data-width="55%">
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
                       :embOptFGrid="embOptFGridData"
                       :isRepaidGrid="isRepaidGridData"
                       :couponTypeGrid="couponTypeGridData"
                       :disabledVal="true"
                       :isDetailShow="true"/>
            <BlComp  ref="blComp" @loadGriding="loadGriding"
                     :info="formData"
                     :disabledVal="true"/>
          </div>
        </div>
      </k-popup>
    </div>
  </div>
</template>

<script>
  import BlComp from "@/pages/pms/asset/bond/BondInfoModelCollection";
  import EditComp from "@/pages/pms/asset/bond/BondInfoModelEdit";
  import Tools from "@/utils/tools.js";
  import Vue from "vue";
  const BlCompIns = new Vue(BlComp);
  const EditCompIns = new Vue(EditComp);
  export default {
    name:"BondInfoModel",
    components: {
      BlComp,
      EditComp,
      Tools,
    },
    data() {
      return {
        activeMenu: '1',
        formData: {},
        selectRowData: {},
        embOptFGridData: {},
        isRepaidGridData: {},
        couponTypeGridData: {},
        searchParam:{}, //查询条件
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
      loadGriding(val){
        this.$refs.addBondInfo.close();
        this.$refs.editBondInfo.close();
        this.$refs.blBondInfo.close();
        this.$refs.bondInfoModelGrid.load(this.searchParam);
        this.$refs.scrCdId.load();
      },
      addDataHandler() {
        this.formData = {};
        this.$set(this.embOptFGridData, "rows", []);
        this.$set(this.embOptFGridData, "total", 0);
        this.$set(this.isRepaidGridData, "rows", []);
        this.$set(this.isRepaidGridData, "total", 0);
        this.$set(this.couponTypeGridData, "rows", []);
        this.$set(this.couponTypeGridData, "total", 0);
      },

      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row);
        this.formData = Object.assign({}, row);
        if (this.formData.embOptF == "01") {
          let fields = this.formData.embOptFGridData;
          this.$set(this.embOptFGridData, "rows", fields);
          this.$set(this.embOptFGridData, "total", fields.length);
          console.log("json",this.embOptFGridData.rows);
          console.log("json",this.embOptFGridData.total);
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
        if (this.formData.couponType == "2") {
          let fields = this.formData.couponTypeGridData;
          this.$set(this.couponTypeGridData, "rows", fields);
          this.$set(this.couponTypeGridData, "total", fields.length);
        } else {
          this.$set(this.couponTypeGridData, "rows", []);
          this.$set(this.couponTypeGridData, "total", 0);
        }
      },
    },
  };
</script>
<style scoped lang="scss">
@import "../AssetComFunction.css";
</style>
