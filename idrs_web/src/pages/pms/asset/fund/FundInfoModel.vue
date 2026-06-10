<template>
  <div class="py-page">
    <div>
      <k-form-search-customize  data-model-name="FundInfoModel" data-target="fundInfoModelGrid" v-model = "searchParam">
      <k-form-item label="基金代码">
             <k-field-select v-model="searchParam.scrCd" data-action="FundInfoModel.findFondInfoModelsCdAndNm"  ref="scrCdId" :dataRemote="true"
                           data-display-field="scrCd,scrNm" data-value-field="scrCd"/>
      </k-form-item>
      <k-form-item label="中债二级分类" >
              <k-field-select v-model="searchParam.cbndScdCtg" :data-data="cbndScdCtgDict" data-display-field="TEXT"
                              data-value-field="VALUE"/>
      </k-form-item>
      <k-form-item label="市场">
              <k-field-select v-model="searchParam.trxMkt" data-dict="market_asset"/>
      </k-form-item>
        
<!--      <k-btn slot="button" class="btn-custom-plain" style="width: 120px"  data-functype="POPUP" data-target="initPopup"-->
<!--             v-if="global.isShowAuthorityButton('FundInfoModel.importFundInfoAdditional')">-->
<!--        <md-icon>backup</md-icon>-->
<!--        数据导入-->
<!--      </k-btn>-->
        
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addInfo" slot="button"  v-if="global.isShowAuthorityButton('FundInfoModel.addFoudInfo')">
            <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
          <k-btn slot="button"  class="btn-custom-plain"  data-functype="EXPORT" data-target="fundInfoModelGrid" :data-export-name="'公募基金导出'"
               v-if="global.isShowAuthorityButton('FundInfoModel.excelDownloadFund')">
            <md-icon>cloud_download</md-icon>
            导出
          </k-btn>
        </div>
      </div>
      <k-grid ref="fundInfoModelGrid" @data-row-select="selectRow" data-operate-width="160px" data-action="FundInfoModel.findFundInfoModels"
              v-if="global.isShowAuthorityButton('FundInfoModel.findFundInfoModels')">

        <k-grid-column data-header="基金编码" data-name="scrId" data-hidden=true></k-grid-column>
        <k-grid-column data-header="基金代码" data-name="scrCd"></k-grid-column>
        <k-grid-column data-header="基金名称" data-name="scrNm"></k-grid-column>
        <k-grid-column data-header="市场" data-name="trxMkt" data-dict ="market_asset"></k-grid-column>
        <k-grid-column data-header="外部资讯分类" data-name="assInfClass" data-dict="assInfClassFund" />
        <k-grid-column data-header="中债二级分类" data-name="cbndScdCtg" data-dict ="cbndScdCtg"></k-grid-column>
        <k-grid-column data-header="人行四级分类" data-name="pbnkFurCtg" data-dict ="pbnkTrdCtg"></k-grid-column>
        <k-grid-column data-header="发行份额（亿份）" data-name="isuLot"></k-grid-column>
        <k-grid-column data-header="成立日" data-name="setUpDt" data-type="date"></k-grid-column>
        <k-grid-column data-header="版本号" data-name="version"></k-grid-column>

        <k-grid-column data-header="中债一级分类" data-name="cbndFrsCtg" data-hidden=true data-dict="cbndFrsCtg"></k-grid-column>
        <k-grid-column data-header="固定收益类标识" data-name="fixErnF" data-hidden=true data-dict="isFixErnF"></k-grid-column>
        <k-grid-column data-header="基金发行公司" data-name="fndCmpNm" data-hidden=true></k-grid-column>
        <k-grid-column data-header="基金管理机构名称" data-name="fndMngOrgNm" data-hidden=true></k-grid-column>
        <k-grid-column data-header="基金托管机构名称" data-name="fndTrstOrgNm" data-hidden=true></k-grid-column>
        <k-grid-column data-header="发行机构所属行业" data-name="idt" data-hidden=true data-dict="isuOrgBlgIdt"></k-grid-column>
        <k-grid-column data-header="投资企业类型（按经济类型划分）" data-name="invEntpTypEcn" data-hidden=true data-dict="isuOrgTypEcn"></k-grid-column>
        <k-grid-column data-header="投资企业类型（按规模划分）" data-name="invEntpTypSiz" data-hidden=true data-dict="debtor_scale_type"></k-grid-column>
        <k-grid-column data-header="投资企业类型（按技术领域划分）" data-name="invEntpTypTchno" data-hidden=true data-dict="isuOrgTypTchno" ></k-grid-column>
        <k-grid-column data-header="登记备案机构" data-name="regRcdOrg" data-hidden=true data-dict="regRcdOrg"></k-grid-column>
        <k-grid-column data-header="交易流通场所" data-name="trxPla" data-hidden=true data-dict="tacdingPlace"></k-grid-column>


        <k-grid-column data-header="是否属于政府投资基金" data-name="blgGovInvFndF" data-hidden=true></k-grid-column>
        <k-grid-column data-header="备注" data-name="cmt" data-hidden=true></k-grid-column>
        <k-grid-column data-header="金融资产投资公司发行标识" data-name="finAstInvCmpIsuF" data-hidden=true></k-grid-column>
        <k-grid-column data-header="基金投资资产" data-name="fndInvAst" data-hidden=true></k-grid-column>
        <k-grid-column data-header="发行机构编码" data-name="fndOrgEnc" data-hidden=true></k-grid-column>
        <k-grid-column data-header="基金登记编码" data-name="fndProdRegEnc" data-hidden=true></k-grid-column>
        <k-grid-column data-header="G06二级分类" data-name="ggCbcSubType" data-hidden=true data-dict="g06_scd_type"></k-grid-column>
        <k-grid-column data-header="G06一级分类" data-name="ggCbcType" data-hidden=true data-dict="g06_first_type"></k-grid-column>
        <k-grid-column data-header="政府投资基金投向" data-name="govInvFndDir" data-hidden=true></k-grid-column>
        <k-grid-column data-header="投资阶段" data-name="invStg" data-hidden=true></k-grid-column>
        <k-grid-column data-header="人行一级分类" data-name="pbnkFrsCtg" data-hidden=true data-dict="asseFrsCtg"></k-grid-column>
        <k-grid-column data-header="人行二级分类" data-name="pbnkScdCtg" data-hidden=true data-dict="pbnkFrsCtg"></k-grid-column>
        <k-grid-column data-header="人行三级分类" data-name="pbnkTrdCtg" data-hidden=true data-dict="pbnkScdCtg"></k-grid-column>
        <k-grid-column data-header="运作方式" data-name="runMth" data-hidden=true data-dict="operation_mode"></k-grid-column>


        <k-grid-column data-header="创建日期" data-name="crtDate" data-hidden="true" data-export="false" />
        <k-grid-column data-header="创建时间" data-name="crtTime" data-hidden="true" data-export="false" />
        <k-grid-column data-header="创建人" data-name="crtUser" data-hidden="true" data-export="false" />
        <k-grid-column data-header="修改日期" data-name="updDate" data-hidden="true" data-export="false" />
        <k-grid-column data-header="修改时间" data-name="updTime" data-hidden="true" data-export="false" />
        <k-grid-column data-header="修改人" data-name="updUser" data-hidden="true" data-export="false" />
        <k-grid-column data-header="处理日期" data-name="dealDate" data-hidden="true" data-export="false"/>

        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text specialClass" data-descript="修改基金信息" data-functype="POPUP" data-size="mini"
                 v-if="global.isShowAuthorityButton('FundInfoModel.updateFundInfo')" data-target="editInfo" >
           修改
          </k-btn>
          <k-btn class="btn-custom-text specialClass" data-descript="补录基金信息" data-functype="POPUP" data-size="mini" v-if="global.isShowAuthorityButton('FundInfoModel.updateFundInfoBl')"
                       data-target="blInfo">
                       补录
                    </k-btn>
          <k-btn class="btn-custom-text specialClass" data-functype="SUBMIT" data-action="FundInfoModel.deleteFundInfo" data-size="mini" v-if="global.isShowAuthorityButton('FundInfoModel.deleteFundInfo')"
               data-type="danger" data-target="fundInfoModelGrid" :data-confirm="true" data-descript="删除基金信息">
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
    <k-popup ref="blInfo" data-title="补录" :dataDialogDrag="false">
      <EditComp  ref="editComp" @loadGriding="loadGriding"
                 :info="formData"
                 :disabledVal="true"
                 :isDetailShow="true"/>
      <BlComp  ref="blComp" @loadGriding="loadGriding"
               :info="formData"
               :disabledVal="true"/>
    </k-popup>



    <k-popup ref="initPopup" data-title="数据导入" >
      <k-form class="my-form" ref="addFundInfoForm"  >
        <k-form-item label="附件" data-ui="element" data-input-width="500px">

          <k-field-upload data-type="file" ref="uploadRef" :data-multiple="true" :data-limit=1
                          data-accept=".xlsx,.xls"
                          :data-error="onSubmitErrorLegal" :data-success="onSubmitDocSuccessLegal"
                          data-upload-url= "/upload/server/DpsApp/fundTempUpload.json" :data-auto-upload="false" >
          </k-field-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="prodInfoGrid" ref="submitBtn" :data-auto-upload="false"
                 :data-handler="submitUploadParamLegal" >
            <span v-show="showSubmitBtn">确定</span>
            <i v-show="!showSubmitBtn" class="el-icon-loading"/>
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
  </div>
</template>

<script>
import BlComp from "@/pages/pms/asset/fund/FundInfoModelCollection";
import EditComp from "@/pages/pms/asset/fund/FundInfoModelEdit";
import AssetCommon from "@/pages/pms/asset/AssetComFunction";

  export default {
  name:"FundInfoModel",
    components: {
      // KPopup,
      BlComp,
      EditComp
    },
    data() {
      return {
        formData: {},
        selectRowData: {},
        searchParam:{},
        cbndScdCtgDict:{},
        showSubmitBtn:true,
      };
    },
    created() {
      // this.fundIdAndNm();
       this.$set(this.searchParam, 'wdScdCtg', '');
      AssetCommon.areaDict(this,'cbndScdCtgDict','cbndScdCtg','1505,1106,1303,2401,2402,2403,2499',false);
    },
    methods: {

      loadGriding(val){
        this.$refs.addInfo.close();
        this.$refs.editInfo.close();
        this.$refs.blInfo.close();
        this.$refs.fundInfoModelGrid.load(this.searchParam);
      },
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      },

      submitUploadParamLegal(){
        //文件上传校验
        let validate = this.$refs.addFundInfoForm.validate();
        if (validate) {
          this.showSubmitBtn = false;
          let formData = { beginDate: this.beginDate};
          let temp = document.getElementsByClassName('upload-demo');
          let lis = temp[0].childNodes[1].childNodes.length;
          if (lis > 0) {
            this.$refs.uploadRef.upload(formData);
            this.showSubmitBtn = true;
          } else {
            this.$message.error("上传文件不能为空!");
            this.showSubmitBtn = true;
            return false;
          }
        }
      },
      onSubmitErrorLegal() {
        this.$refs.uploadRef.doReset();
      },

      onSubmitDocSuccessLegal() {
        this.$refs.uploadRef.doReset();
        this.$refs.initPopup.close();
        this.$refs.fundInfoModelGrid.load(this.searchParam);
        this.$refs.scrCdId.load();
      },
    },
  };
</script>

<style scoped lang="scss">
@import "../AssetComFunction.css";
</style>
