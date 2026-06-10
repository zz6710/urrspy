<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="NetValSPVInfoModel" data-target="NetValSPVInfoModel" v-model = "searchParam">
      <k-form-item label="资管计划代码">
             <k-field-select v-model="searchParam.scrCd"  data-action="NetValSPVInfoModel.findNetValSPVInfoModelsCdAndNm"  ref="scrCdId" :dataRemote="true"
                           data-display-field="scrCd,scrNm" data-value-field="scrCd"/>
       </k-form-item>
        <k-form-item label="资管计划名称">
          <k-field-select v-model="searchParam.scrNm"  data-action="NetValSPVInfoModel.findNetValSPVInfoModelsCdAndNm"  ref="scrCdId" :dataRemote="true"
                          data-display-field="scrCd,scrNm" data-value-field="scrNm"/>
        </k-form-item>
        <k-form-item label="投资方式" >
          <k-field-select v-model="searchParam.investWay" data-dict="investWay" />
        </k-form-item>
        <k-form-item label="起息日">
          <k-field-date v-model="dateRange1" data-type="daterange" data-date-format="yyyyMMdd"
                        data-value-format="yyyyMMdd"></k-field-date>
        </k-form-item>

        <k-form-item label="中债二级分类" >
          <k-field-select v-model="searchParam.cbndScdCtg" :data-data="cbndScdCtgDict" data-display-field="TEXT"
                          data-value-field="VALUE"/>
        </k-form-item>
        <k-form-item label="人行三级分类">
          <k-field-select v-model="searchParam.pbnkTrdCtg" data-dict="pbnkScdCtg"/>
        </k-form-item>
       
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addNetValSPVInfoModelPopup"
            slot="button"
                v-if="global.isShowAuthorityButton('NetValSPVInfoModel.addNetValSPVInfoModel')">
            <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
          <k-btn slot="button"  class="btn-custom-plain"  data-functype="EXPORT" data-target="NetValSPVInfoModel" :data-export-name="'资管计划导出'"
                v-if="global.isShowAuthorityButton('NetValSPVInfoModel.SPVExcelDownloadAction')">
            <md-icon>cloud_download</md-icon>
            导出
          </k-btn>
        </div>
      </div>
      <k-grid ref="NetValSPVInfoModel" @data-row-select="selectRow" data-operate-width="160px" data-action="NetValSPVInfoModel.findNetValSPVInfoModels"
              v-if="global.isShowAuthorityButton('NetValSPVInfoModel.findNetValSPVInfoModels')">
        <k-grid-column data-header="资管计划编号" data-name="scrId" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="资管计划代码" data-name="scrCd"></k-grid-column>
        <k-grid-column data-header="资管计划名称" data-name="scrNm"></k-grid-column>
        <k-grid-column data-header="投资方式" data-name="investWay" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="中债二级分类" data-name="cbndScdCtg" data-dict ="cbndScdCtg"></k-grid-column>
        <k-grid-column data-header="G06二级分类" data-name="ggCbcSubType" data-dict ="g06_scd_type"></k-grid-column>
        <k-grid-column data-header="人行四级分类" data-name="pbnkFurCtg" data-dict ="pbnkTrdCtg"></k-grid-column>
        <k-grid-column data-header="资管计划属性" data-name="astMngPlanPrpt" data-dict="astMngPlanPrpt"></k-grid-column>
        <k-grid-column data-header="购买结构" data-name="buyStrc" data-width="230" data-dict="buyStrc"></k-grid-column>
        <k-grid-column data-header="版本号" data-name="version"></k-grid-column>



        <k-grid-column data-header="中债一级分类" data-name="cbndFrsCtg" data-hidden="true" data-dict ="cbndFrsCtg"></k-grid-column>
        <k-grid-column data-header="币种" data-name="ccy" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="托管人" data-name="cstd" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="是否有预期收益率" data-name="expeRatF" data-hidden="true" data-dict="1yes2no"></k-grid-column>
        <k-grid-column data-header="资金实际投向" data-name="fndActlDir" data-hidden="true" data-dict="actual_invest_dir_fund"></k-grid-column>
        <k-grid-column data-header="资金运用行业" data-name="fndCrryIdt" data-hidden="true" data-dict="isuOrgBlgIdt"></k-grid-column>
        <k-grid-column data-header="管理人" data-name="mng" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="到期日期" data-name="mtuDt" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="成立日期" data-name="setUpDt" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="SPV机构编码" data-name="spvOrgEnc" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="SPV产品登记编码" data-name="spvProdRegEnc" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="市场" data-name="trxMkt" data-hidden="true" data-dict ="market_asset"></k-grid-column>
        <k-grid-column data-header="交易流通场所" data-name="trxPla" data-hidden="true" data-dict="tacdingPlace"></k-grid-column>

        <k-grid-column data-header="金额(元)" data-name="amt" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="是否为银行理财产品" data-name="bnkInvProdF" data-hidden="true" data-dict="1yes2no"></k-grid-column>
        <k-grid-column data-header="备注" data-name="cmt" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="预期最高收益率(%)" data-name="expeMaxRat" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="预期最低收益率(%)" data-name="expeMinRat" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="是否由金融资产投资公司发行" data-name="finAstInvCmpIsuF" data-hidden="true" data-dict="1yes2no"></k-grid-column>
        <k-grid-column data-header="资金运用方式" data-name="fndCrryMth" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="G06一级分类" data-name="ggCbcType" data-hidden="true" data-dict="g06_first_type"></k-grid-column>
        <k-grid-column data-header="是否投向金融公司私募产品" data-name="isFinIsuF" data-hidden="true" data-dict="1yes2no"></k-grid-column>
        <k-grid-column data-header="资管计划发起人机构编码" data-name="isuOrgEnc" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="中介服务机构合计费率(%)" data-name="medAgnSrvOrgSmrFeeRat" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="管理费率(%)" data-name="mngFeeTat" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="管理方式" data-name="mngMth" data-hidden="true" data-dict="mngMth"></k-grid-column>
        <k-grid-column data-header="其他合计费率(%)" data-name="othSmrFeeRat" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="人行一级分类" data-name="pbnkFrsCtg" data-hidden="true" data-dict="asseFrsCtg"></k-grid-column>
        <k-grid-column data-header="人行二级分类" data-name="pbnkScdCtg" data-hidden="true" data-dict ="pbnkFrsCtg"></k-grid-column>
        <k-grid-column data-header="人行三级分类" data-name="pbnkTrdCtg" data-hidden="true" data-dict ="pbnkScdCtg"></k-grid-column>
        <k-grid-column data-header="产品登记编码" data-name="prodRegEnc" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="托管费率(%)" data-name="trstFeeTat" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="交易相关合计费率(%)" data-name="trxRelSmrFeeRat" data-hidden="true"></k-grid-column>




        <k-grid-column data-header="创建日期" data-name="crtDate" data-hidden="true" data-export="false" />
        <k-grid-column data-header="创建时间" data-name="crtTime" data-hidden="true" data-export="false" />
        <k-grid-column data-header="创建人" data-name="crtUser" data-hidden="true" data-export="false" />
        <k-grid-column data-header="修改日期" data-name="updDate" data-hidden="true" data-export="false" />
        <k-grid-column data-header="修改时间" data-name="updTime" data-hidden="true" data-export="false" />
        <k-grid-column data-header="修改人" data-name="updUser" data-hidden="true" data-export="false" />
        <k-grid-column data-header="处理日期" data-name="dealDate" data-hidden="true" data-export="false"/>

        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info specialClass" data-descript="修改净值SPV信息" data-functype="POPUP" data-size="mini"
            data-target="editNetVaSPVInfoModelPopup"   v-if="global.isShowAuthorityButton('NetValSPVInfoModel.updateNetValSPVInfoModel')">
            修改
          </k-btn>
          <k-btn class="md-info specialClass" data-descript="补录净值SPV信息" data-functype="POPUP" data-size="mini" v-if="global.isShowAuthorityButton('NetValSPVInfoModel.updateNetValSPVInfoModelBl')"
            data-target="collectNetVaSPVInfoModelPopup">
            补录
          </k-btn>

          <k-btn class="md-danger specialClass" data-functype="SUBMIT" data-action="NetValSPVInfoModel.deleteNetValSPVInfoModel" data-size="mini" v-if="global.isShowAuthorityButton('NetValSPVInfoModel.deleteNetValSPVInfoModel')"
               data-type="danger" data-target="NetValSPVInfoModel" :data-confirm="true" data-descript="删除净值SPV信息">
          	删除
    	  </k-btn>
        </template>
      </k-grid>
    </div>


    <!--    添加弹出框   -->
    <k-popup ref="addNetValSPVInfoModelPopup" data-title="新增" :dataDialogDrag="true">
      <EditComp @loadGriding="loadGriding" ref="addComp" :info="{}" :disabledVal="false"/>
    </k-popup>

    <!--  修改弹出框   -->
    <k-popup ref="editNetVaSPVInfoModelPopup" data-title="修改" :dataDialogDrag="true">
      <EditComp  @loadGriding="loadGriding" ref="editComp" :info="formData" :disabledVal="true"/>
    </k-popup>

    <!--  补录弹出框   -->
    <k-popup ref="collectNetVaSPVInfoModelPopup" data-title="补录" :dataDialogDrag="false" data-width="55%">
      <div class="modal">
        <el-aside width="0%">
          <el-menu default-active="activeMenu" @select="handleMenuSelect" >
            <el-menu-item index="1">基础信息</el-menu-item>
            <el-menu-item index="2">补录信息</el-menu-item>
          </el-menu>
        </el-aside>
        <div class="modal-content">
          <EditComp  @loadGriding="loadGriding" ref="editComp" :info="formData" :disabledVal="true" :isDetailShow="true"/>
          <BlComp  @loadGriding="loadGriding" ref="blComp" :info="formData" :disabledVal="true"/>
        </div>
      </div>
    </k-popup>

  </div>
</template>

<script>
import EditComp from "@/pages/pms/asset/netvalSPV/NetValSPVInfoEdit";
import BlComp from "@/pages/pms/asset/netvalSPV/NetValSPVInfoModelCollection";
import Vue from "vue";
import AssetCommon from "@/pages/pms/asset/AssetComFunction";
const BlCompIns = new Vue(BlComp);
const EditCompIns = new Vue(EditComp);
  export default {
    name:"NetValSPVInfoModel",
    components: {BlComp,EditComp},
    props: {

    },
    data() {
      return {
        activeMenu: '1',
        dateRange1:[],
        dateRange2:[],
        formData: {},
        selectRowData: {},
        searchParam: {},//查询参数
        cbndScdCtgDict: {},
      };
    },
    created() {
      AssetCommon.areaDict(this,'cbndScdCtgDict','cbndScdCtg','17',true);
    },
    methods: {

      handleMenuSelect(index){
        switch (index) {
          case '1': EditCompIns.MenuSelect(index); break;
          case '2': BlCompIns.MenuSelect(index); break;
          default: break;
        }
      },
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      },

      loadGriding(val){
        this.$refs.addNetValSPVInfoModelPopup.close();
        this.$refs.editNetVaSPVInfoModelPopup.close();
        this.$refs.collectNetVaSPVInfoModelPopup.close();
        this.$refs.NetValSPVInfoModel.load(this.searchParam);
        this.$refs.scrCdId.load();
      },
    },
    watch: {
      dateRange1() {
        this.$set(this.searchParam, 'valDtStart', this.dateRange1 == null ? '' : this.dateRange1[0]);
        this.$set(this.searchParam, 'valDtEnd', this.dateRange1 == null ? '' : this.dateRange1[1]);
      },
    }
  };
</script>

<style scoped lang="scss">
@import "../AssetComFunction.css";
</style>
