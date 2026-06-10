
<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="NonStandInfoModel" data-target="InfoModelGrid" v-model = "searchParam">
        <k-form-item label="资产代码">
          <k-field-select v-model="searchParam.scrCd" data-action="NonStandInfoModel.findNonStandInfoIdAndNm" :dataRemote="true"  ref="scrCdId"
                          data-display-field="scrCd,scrNm" data-value-field="scrCd"/>
        </k-form-item>
        <k-form-item label="起息日">
          <k-field-date v-model="valDate" data-type="daterange" data-date-format="yyyyMMdd"
                        data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="到期日">
          <k-field-date v-model="mtuDate" data-type="daterange" data-date-format="yyyyMMdd"
                        data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="中债二级分类">
          <k-field-select v-model="searchParam.cbndScdCtg" data-dict ="cbndScdCtg" />
        </k-form-item>
        <k-form-item label="人行二级分类">
          <k-field-select v-model="searchParam.pbnkScdCtg" data-dict = "pbnkFrsCtg"/>
        </k-form-item>
        <k-form-item label="人行三级分类">
          <k-field-select v-model="searchParam.pbnkTrdCtg" data-dict = "pbnkScdCtg"/>
        </k-form-item>
        
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="addDataHandler" data-target="addInfo" slot="button"  v-if="global.isShowAuthorityButton('NonStandInfoModel.addNonStandInfoModel')">
            <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
          <k-btn slot="button"  class="btn-custom-plain"  data-functype="EXPORT" data-target="InfoModelGrid" :data-export-name="'非标信息导出'"
                v-if="global.isShowAuthorityButton('NonStandInfoModel.nonExcelDownloadAction')">
            <md-icon>cloud_download</md-icon>
            导出
          </k-btn>
        </div>
      </div>
      <k-grid ref="InfoModelGrid" @data-row-select="selectRow" data-operate-width="160px" data-action="NonStandInfoModel.findNonStandInfoModels"
              v-if="global.isShowAuthorityButton('NonStandInfoModel.findNonStandInfoModels')">
        <k-grid-column data-header="资产编号" data-name="scrId" data-hidden="true"></k-grid-column>
		    <k-grid-column data-header="资产代码" data-name="scrCd"></k-grid-column>
		    <k-grid-column data-header="资产名称" data-name="scrNm"></k-grid-column>
		    <k-grid-column data-header="是否通道投资" data-name="isChannel" data-dict="1yes2no"></k-grid-column>
		    <k-grid-column data-header="通道" data-name="channelNo"></k-grid-column>
		    <k-grid-column data-header="投向" data-name="mmActualDirect" data-dict="isuOrgBlgIdt"></k-grid-column>
        <k-grid-column data-header="起息日期" data-name="valDt" data-type="date"></k-grid-column>
        <k-grid-column data-header="到期日期" data-name="mtuDt" data-type="date"></k-grid-column>
        <k-grid-column data-header="项目收益率（%）" data-name="yld"></k-grid-column>
        <k-grid-column data-header="中债二级分类" data-name="cbndScdCtg" data-dict="cbndScdCtg"></k-grid-column>
        <k-grid-column data-header="G06二级分类" data-name="ggCbcSubType" data-dict ="g06_scd_type"></k-grid-column>
        <k-grid-column data-header="G06三级分类" data-name="ggCbcTrdType" data-dict ="g06_trd_type"></k-grid-column>
        <k-grid-column data-header="人行三级分类" data-name="pbnkTrdCtg" data-dict="pbnkScdCtg"></k-grid-column>
        <k-grid-column data-header="人行四级分类" data-name="pbnkFurCtg" data-dict="pbnkTrdCtg"></k-grid-column>
        <k-grid-column data-header="版本号" data-name="version"></k-grid-column>



        <k-grid-column data-header="金额(元)" data-name="amt" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="资产类型" data-name="assetType" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="基准利率种类" data-name="bchmRatTyp" data-hidden="true" data-dict="bchmRatTyp"></k-grid-column>
        <k-grid-column data-header="中债一级分类" data-name="cbndFrsCtg" data-hidden="true" data-dict="cbndFrsCtg"></k-grid-column>
        <k-grid-column data-header="币种" data-name="ccy" data-hidden="true" data-dict="cur_type"></k-grid-column>
        <k-grid-column data-header="含权类型" data-name="embOptTyp" data-hidden="true" data-dict="embOptTyp"></k-grid-column>
        <k-grid-column data-header="是否有预期收益率" data-name="expeRatF" data-hidden="true" data-dict="1yes2no"></k-grid-column>
        <k-grid-column data-header="固定行权日" data-name="fixXcsRitDt" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="首次付息日" data-name="frsPayIntrDt" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="担保性质" data-name="grntChr" data-hidden="true" data-dict="grntChr"></k-grid-column>
        <k-grid-column data-header="担保人与融资人关系" data-name="grntLvrgRel" data-hidden="true" data-dict="grntLvrgRel"></k-grid-column>
        <k-grid-column data-header="融资人主体评级" data-name="grntMainRat" data-hidden="true" data-dict="mainRating"></k-grid-column>
        <k-grid-column data-header="担保方式" data-name="grntMth" data-hidden="true" data-dict="grntWay"></k-grid-column>
        <k-grid-column data-header="增信机构代码" data-name="incCrdOrgCd" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="增信机构名称" data-name="incCrdOrgNm" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="分期还本条款标识" data-name="insPayPrcpF" data-hidden="true" data-dict="insPayPrcpF"></k-grid-column>
        <k-grid-column data-header="计息基础" data-name="intrBas" data-hidden="true" data-dict="nonIntrBas"></k-grid-column>
        <k-grid-column data-header="计息类型" data-name="intrTyp" data-hidden="true" data-dict="interest_type"></k-grid-column>
        <k-grid-column data-header="融资人" data-name="lvrg" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="融资人所属行业" data-name="lvrgBlgIdt" data-hidden="true" data-dict="isuOrgBlgIdt"></k-grid-column>
        <k-grid-column data-header="融资人所属地区" data-name="lvrgBlgZon" data-hidden="true" data-dict="prod_sale_area"></k-grid-column>
        <k-grid-column data-header="融资人内部信用评级" data-name="lvrgInCrdRat" data-hidden="true" data-dict="mainRating"></k-grid-column>
        <k-grid-column data-header="融资人组织机构（社会信用）代码" data-name="lvrgOrgOrgCd" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="融资人类型（按经济类型划分）" data-name="lvrgTypEcn" data-hidden="true" data-dict="isuOrgTypEcn"></k-grid-column>
        <k-grid-column data-header="融资人类型（按规模划分）" data-name="lvrgTypSiz" data-hidden="true" data-dict="instituteTypeTech"></k-grid-column>
        <k-grid-column data-header="融资人类型（按技术领域划分）" data-name="lvrgTypTchno" data-hidden="true" data-dict="isuOrgTypTchno"></k-grid-column>
        <k-grid-column data-header="外部评级机构及融资人评级" data-name="outRatOrgAndLvrgRat" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="付息频率（个月/次）" data-name="payIntrFrq" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="还本付息情况说明" data-name="payPrcpIntrStsCmt" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="付息频率" data-name="payinterestFreq" data-hidden="true" data-dict="payIntrFrqAll"></k-grid-column>
        <k-grid-column data-header="抵质押物类型" data-name="plgTyp" data-hidden="true" data-dict="plgTyp"></k-grid-column>
        <k-grid-column data-header="抵质押物价值(元)" data-name="plgVal" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="是否规则付息" data-name="rulPayIntrF" data-hidden="true" data-dict="1yes2no"></k-grid-column>
        <k-grid-column data-header="利差(%)" data-name="sprd" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="市场" data-name="trxMkt" data-hidden="true" data-dict ="market_bond"></k-grid-column>
        <k-grid-column data-header="交易流通场所" data-name="trxPla" data-hidden="true" data-dict="tacdingPlace"></k-grid-column>
        <k-grid-column data-header="行权方式" data-name="xcsRitMth" data-hidden="true" data-dict="xcsRitMth"></k-grid-column>
        <k-grid-column data-header="行权价格(元)" data-name="xcsRitPrc" data-hidden="true"></k-grid-column>


        <k-grid-column data-header="资产内部评级" data-name="astInRat" data-hidden="true" data-dict="mainRating"></k-grid-column>
        <k-grid-column data-header="资产外部评级" data-name="astOutRat" data-hidden="true" data-dict="mainRating"></k-grid-column>
        <k-grid-column data-header="基础资产类型" data-name="basAstTyp" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="是否买入返售" data-name="buyBackF" data-hidden="true" data-dict="1yes2no"></k-grid-column>
        <k-grid-column data-header="中债发行机构所属行业" data-name="ccIndustryIssuer" data-hidden="true" data-dict="isuOrgBlgIdt"></k-grid-column>
        <k-grid-column data-header="通道名称" data-name="channelName" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="选择权" data-name="chcRit" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="备注" data-name="cmt" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="超额收益分配比例(%)" data-name="exsErnAlcRto" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="浮动因子(%)" data-name="fltFct" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="是否浮动因子" data-name="fltFctF" data-hidden="true" data-dict="1yes2no"></k-grid-column>
        <k-grid-column data-header="首次行权日期" data-name="frsFixXcsDt" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="首次重定价日期" data-name="frsRprcDt" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="G06一级分类" data-name="ggCbcType" data-hidden="true" data-dict ="g06_first_type"></k-grid-column>
        <k-grid-column data-header="担保情况说明" data-name="grntStsCmt" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="收/受益权类型" data-name="incBenRitTyp" data-hidden="true" data-dict="incBenRitType"></k-grid-column>
        <k-grid-column data-header="利息分布方式" data-name="intrAlcMth" data-hidden="true" data-dict="intrAlcMth"></k-grid-column>
        <k-grid-column data-header="利息递延条款类型" data-name="intrPpnTyp" data-hidden="true" data-dict="intrPpnTyp"></k-grid-column>
        <k-grid-column data-header="中债发行机构类型（按规模划分）" data-name="isuOrgTypScaleSiz" data-hidden="true" data-dict="debtor_scale_type"></k-grid-column>
        <k-grid-column data-header="人行发行机构企业规模" data-name="isuOrgTypSiz" data-hidden="true" data-dict ="debtor_type" ></k-grid-column>
        <k-grid-column data-header="重点监控行业和领域类别" data-name="keyMntIdtTyp" data-hidden="true" data-dict="keyMntIdtTyp"></k-grid-column>
        <k-grid-column data-header="重点监控行业和领域类别说明" data-name="keyMntIdtTypCmt" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="份额面值" data-name="lotParVal" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="融资项目" data-name="lvrgPrj" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="融资项目所属行业" data-name="lvrgPrjBlgIdt" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="融资总费率(%)" data-name="lvrgTotFee" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="部分赎回标识" data-name="partRdmF" data-hidden="true" data-dict="1yes2no"></k-grid-column>
        <k-grid-column data-header="部分赎回比例" data-name="partRdmRto" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="付息计划" data-name="payPlan" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="还本方式" data-name="payPrcpMth" data-hidden="true" data-dict="payPrcpMth"></k-grid-column>
        <k-grid-column data-header="人行一级分类" data-name="pbnkFrsCtg" data-hidden="true" data-dict="asseFrsCtg"></k-grid-column>
        <k-grid-column data-header="人行发行机构所属行业" data-name="pbnkIndustryIssuer" data-hidden="true" data-dict="subm_isuOrgBlgIdt"></k-grid-column>
        <k-grid-column data-header="人行二级分类" data-name="pbnkScdCtg" data-hidden="true" data-dict="pbnkFrsCtg"></k-grid-column>
        <k-grid-column data-header="永续条款类型" data-name="perpTyp" data-hidden="true" data-dict="perpTyp"></k-grid-column>
        <k-grid-column data-header="递延利息是否计息" data-name="ppnIntrIntrF" data-hidden="true" data-dict="1yes2no"></k-grid-column>
        <k-grid-column data-header="项目归属重点监控行业和领域标识" data-name="prjBlgKeyMntIdt" data-hidden="true" data-dict="1yes2no"></k-grid-column>
        <k-grid-column data-header="还本计划" data-name="repayPlan" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="重定价周期" data-name="rprcPrd" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="法定到期日" data-name="staMtuDt" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="结构档次" data-name="strcGrd" data-hidden="true" data-dict="strcGrd"></k-grid-column>
        <k-grid-column data-header="版本号" data-name="version" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="行权条件说明" data-name="xcsRitCondCmt" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="行权周期" data-name="xcsRitPrd" data-hidden="true"></k-grid-column>


        <k-grid-column data-header="创建日期" data-name="crtDate" data-hidden="true" data-export="false" />
        <k-grid-column data-header="创建时间" data-name="crtTime" data-hidden="true" data-export="false" />
        <k-grid-column data-header="创建人" data-name="crtUser" data-hidden="true" data-export="false" />
        <k-grid-column data-header="修改日期" data-name="updDate" data-hidden="true" data-export="false" />
        <k-grid-column data-header="修改时间" data-name="updTime" data-hidden="true" data-export="false" />
        <k-grid-column data-header="修改人" data-name="updUser" data-hidden="true" data-export="false" />
        <k-grid-column data-header="处理日期" data-name="dealDate" data-hidden="true" data-export="false"/>




        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text specialClass" data-descript="修改非标信息" data-functype="POPUP" data-size="mini"  v-if="global.isShowAuthorityButton('NonStandInfoModel.updateNonStandInfoModel')"
            data-target="editInfo">
            修改
          </k-btn>
          <k-btn class="btn-custom-text specialClass" data-descript="补录非标信息" data-functype="POPUP" data-size="mini" v-if="global.isShowAuthorityButton('NonStandInfoModel.updateNonStandInfoModelBl')"
                 data-target="blInfo">
            补录
          </k-btn>
          <k-btn class="btn-custom-text specialClass" data-functype="SUBMIT" data-action="NonStandInfoModel.deleteNonStandInfoModel" data-size="mini" v-if="global.isShowAuthorityButton('NonStandInfoModel.deleteNonStandInfoModel')"
               data-type="danger" data-target="InfoModelGrid" :data-confirm="true" data-descript="删除非标信息">
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
                     :disabledVal="true" :isDetailShow="true"/>
          <BlComp  ref="blComp" @loadGriding="loadGriding"
                   :info="formData"
                   :disabledVal="true"/>
        </div>
      </div>
    </k-popup>

  </div>
</template>

<script>
  import BlComp from "@/pages/pms/asset/nonStand/NonStandInfoModelCollection";
  import EditComp from "@/pages/pms/asset/nonStand/NonStandInfoModelEdit";
  import Vue from "vue";
  const BlCompIns = new Vue(BlComp);
  const EditCompIns = new Vue(EditComp);

  export default {
    name:"NonStandInfoModel",
    components: {
      BlComp,
      EditComp
    },
    data() {
      return {
        activeMenu: '1',
        formData: {},
        selectRowData: {},
        searchParam:{},
        valDate:[],
        mtuDate:[]
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
      },
    },
    watch: {
      // 查询起息日
      valDate() {
        console.log(this.valDate);
        this.$set(this.searchParam, 'valDtStart', this.valDate == null ? '' : this.valDate[0]);
        this.$set(this.searchParam, 'valDtEnd', this.valDate == null ? '' : this.valDate[1]);
      },
      // 查询到期日
      mtuDate(){
        this.$set(this.searchParam, 'mtuDtStart', this.mtuDate == null ? '' : this.mtuDate[0]);
        this.$set(this.searchParam, 'mtuDtEnd', this.mtuDate == null ? '' : this.mtuDate[1]);
      }
    }
  };
</script>

<style scoped lang="scss">
@import "../AssetComFunction.css";
</style>
