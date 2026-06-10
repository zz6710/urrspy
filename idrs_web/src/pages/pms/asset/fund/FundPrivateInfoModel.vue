<template>
  <div>
    <div>
      <k-form-search-customize  data-model-name="FundPrivateInfoModel" data-target="FundPrivateInfoModelGrid" v-model = "searchParam">
        <k-form-item label="基金代码">
          <k-field-select v-model="searchParam.scrCd" data-action="FundPrivateInfoModel.findFundPrivateInfoModelsCdAndNm"  ref="scrCdId" :dataRemote="true"
                          data-display-field="scrCd,scrNm" data-value-field="scrCd"/>
        </k-form-item>
        <k-form-item label="行业">
          <k-field-select v-model="searchParam.investmentIndustry" data-dict="pbc_eco_inds_typ_det"/>
        </k-form-item>
        <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addInfo" slot="button"  v-if="global.isShowAuthorityButton('FundPrivateInfoModel.addFundPrivateInfo')">
          <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
        <k-btn slot="button"  class="btn-custom-plain"  data-functype="EXPORT" data-target="FundPrivateInfoModelGrid" :data-export-name="'私募基金导出'">
          <md-icon>cloud_download</md-icon>
          导出
        </k-btn>
      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="FundPrivateInfoModelGrid" @data-row-select="selectRow" data-fixed="right" data-operate-width="160px" data-action="FundPrivateInfoModel.findFundPrivateInfoModels" >

        <k-grid-column data-header="证券编号" data-name="scrId" data-hidden=true></k-grid-column>
        <k-grid-column data-header="基金代码" data-name="scrCd"></k-grid-column>
        <k-grid-column data-header="基金名称" data-name="scrNm"></k-grid-column>
        <k-grid-column data-header="金融咨询系统资产代码" data-name="fisCd" data-hidden=true />
        <k-grid-column data-header="是否由金融资产投资公司发行" data-name="isIssueFaic" data-hidden=true data-dict ="1yes2no"/>
        <k-grid-column data-header="投资阶段" data-name="fndIs" data-dict ="Investment_stage"></k-grid-column>
        <k-grid-column data-header="登记备案机构" data-name="fndRfa" data-hidden=true data-dict="regRcdOrg"  />
        <k-grid-column data-header="是否为固定收益类" data-name="isFic" data-dict ="1yes2no"data-hidden=true />
        <k-grid-column data-header="政府投资基金投向" data-name="fndIdg" data-dict ="government_investment_fund" data-hidden=true />
        <k-grid-column data-header="基金公司名称" data-name="fndMi" data-hidden=true />
        <k-grid-column data-header="基金管理机构名称" data-name="fndMiName"  data-hidden=true />
        <k-grid-column data-header="基金托管机构名称" data-name="fndCiName"  data-hidden=true />
        <k-grid-column data-header="基金管理机构名称" data-name="orgFullName3"   />
        <k-grid-column data-header="基金托管机构名称" data-name="orgFullName4"   />
        <k-grid-column data-header="投资行业" data-name="investmentIndustry" data-dict ="pbc_eco_inds_typ_det"  />
        <k-grid-column data-header="投资企业类型（按规模划分）" data-name="lvrgTypSiz" data-hidden=true data-dict="debtor_scale_type"></k-grid-column>
        <k-grid-column data-header="投资企业类型（按经济类型划分）" data-name="lvrgTypTchno" data-hidden=true data-dict="isuOrgTypEcn"></k-grid-column>
        <k-grid-column data-header="投资企业类型（按技术领域划分）" data-name="lvrgTypEcn" data-hidden=true data-dict="isuOrgTypTchno" ></k-grid-column>
        <k-grid-column data-header="是否投向金融资产投资公司或其附属机构发行的私募股权投资基金" data-name="isInvPeif" data-dict ="1yes0no" data-hidden=true />
        <k-grid-column data-header="资产分类" data-name="fndType" data-dict ="asset_classification" data-hidden=true />
        <k-grid-column data-header="中债一级分类" data-name="cbndFrsCtg" data-hidden=true ></k-grid-column>
        <k-grid-column data-header="中债二级分类" data-name="cbndScdCtg" data-dict ="cbndScdCtg" ></k-grid-column>
        <k-grid-column data-header="人行一级分类" data-name="pbnkFrsCtg" data-hidden=true data-dict="asset_frs_ctg"></k-grid-column>
        <k-grid-column data-header="人行二级分类" data-name="pbnkScdCtg" data-hidden=true data-dict="pbnkFrsCtg"></k-grid-column>
        <k-grid-column data-header="人行三级分类" data-name="pbnkTrdCtg" data-hidden=true data-dict="pbnkScdCtg"></k-grid-column>
        <k-grid-column data-header="人行四级分类" data-name="pbnkFouCtg" data-dict ="pbnkTrdCtg" data-hidden=true></k-grid-column>
        <k-grid-column data-header="G06一级分类" data-name="g06ScdCtg" data-hidden=true data-dict="g06_first_type"></k-grid-column>
        <k-grid-column data-header="G06二级分类" data-name="g06FrsCtg" data-hidden=true data-dict="g06_scd_type"></k-grid-column>
        <k-grid-column data-header="G06三级分类" data-name="g06TrdCtg" data-hidden=true data-dict="g06_trd_type"></k-grid-column>
        <k-grid-column data-header="基金投资资产" data-name="invAsset" data-hidden=true></k-grid-column>
        <k-grid-column data-header="备注" data-name="cmt" data-hidden=true></k-grid-column>
        <k-grid-column data-header="创建日期" data-name="crtDate" data-hidden="true" data-export="false" />
        <k-grid-column data-header="创建时间" data-name="crtTime" data-hidden="true" data-export="false" />
        <k-grid-column data-header="创建人" data-name="crtUser" data-hidden="true" data-export="false" />
        <k-grid-column data-header="修改日期" data-name="updDate" data-hidden="true" data-export="false" />
        <k-grid-column data-header="修改时间" data-name="updTime" data-hidden="true" data-export="false" />
        <k-grid-column data-header="修改人" data-name="updUser" data-hidden="true" data-export="false" />
        <k-grid-column data-header="处理日期" data-name="dealDate" data-hidden="true" data-export="false"/>

        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info specialClass" data-descript="详情" data-functype="POPUP" data-size="mini"
                 data-target="FundPrivateInfoPopupDetail">
            详情
          </k-btn>
          <k-btn class="md-info specialClass" data-descript="修改私募基金信息" data-functype="POPUP" data-size="mini"
                 v-if="global.isShowAuthorityButton('FundPrivateInfoModel.updateFundPrivateInfo')" data-target="editInfo" >
            修改
          </k-btn>
          <k-btn class="md-danger specialClass" data-functype="SUBMIT" data-action="FundPrivateInfoModel.deleteFundPrivateInfo" data-size="mini" v-if="global.isShowAuthorityButton('FundPrivateInfoModel.deleteFundPrivateInfo')"
                 data-type="danger" data-target="FundPrivateInfoModelGrid" :data-confirm="true" data-descript="删除私募基金信息">
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

    <k-popup ref="FundPrivateInfoPopupDetail" data-title="详情" :dataDialogDrag="true">
      <k-form ref="detailInfoModelForm" :data-col="2" isFormBodyScreen>
        <div class ="tableLine2" ><span class="leftText2">基本信息</span></div>
        <k-form-item label="资产编码" v-show="false">
          <k-field-text v-model="formData.scrId"  id="scrId" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="基金代码">
          <k-field-text v-model="formData.scrCd" id="scrCd"  :data-max-length="40"  :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="基金名称">
          <k-field-text v-model="formData.scrNm" id="scrNm"  :data-max-length="256" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="是否由金融资产投资公司发行">
          <k-field-select v-model="formData.isIssueFaic"   :data-disabled="true" data-dict ="1yes2no"/>
        </k-form-item>
        <k-form-item label="投资行业">
          <k-field-select v-model="formData.investmentIndustry"   :data-disabled="true" data-dict="pbc_eco_inds_typ_det"/>
        </k-form-item>
        <k-form-item label="登记备案机构">
          <k-field-select v-model="formData.fndRfa" id="fndRfa"  :data-disabled="true" data-dict="regRcdOrg" :data-default-value="'04'"/>
        </k-form-item>
        <k-form-item label="是否为固定收益类">
          <k-field-select v-model="formData.isFic"   :data-disabled="true" data-dict ="1yes2no"/>
        </k-form-item>
        <k-form-item label="是否属于政府投资基金">
          <k-field-select v-model="formData.isGifnd"   :data-disabled="true" data-dict ="1yes2no"/>
        </k-form-item>
        <k-form-item label="政府投资基金投向">
          <k-field-select v-model="formData.fndIdg"   :data-disabled="true" data-dict ="government_investment_fund"/>
        </k-form-item>
        <k-form-item label="基金公司名称">
          <k-field-select v-model="formData.fndMi" data-action="T8OrgSheet.findOrgNmAll"   :dataRemote="true" :data-disabled="true"
                          data-value-field="orgNbrExt" data-display-field="orgFullName" />
        </k-form-item>
        <k-form-item label="基金管理机构名称">
          <k-field-select v-model="formData.fndMiName" data-action="T8OrgSheet.findOrgNmAll"   :dataRemote="true" :data-disabled="true"
                          data-value-field="orgNbrExt" data-display-field="orgFullName" />
        </k-form-item>
        <k-form-item label="基金托管机构名称">
          <k-field-select v-model="formData.fndCiName" data-action="T8OrgSheet.findOrgNmAll"   :dataRemote="true" :data-disabled="true"
                          data-value-field="orgNbrExt" data-display-field="orgFullName" />
        </k-form-item>
        <k-form-item label="投资阶段" >
          <k-field-select v-model="formData.fndIs"  :data-disabled="true" data-dict="Investment_stage"/>
        </k-form-item>
        <k-form-item label="是否投向金融资产投资公司或其附属机构发行的私募股权投资基金">
          <k-field-select v-model="formData.isInvPeif"   :data-disabled="true" data-dict ="1yes0no"/>
        </k-form-item>
        <k-form-item label="资产分类">
          <k-field-select v-model="formData.fndType"   :data-disabled="true" data-dict ="asset_classification"/>
        </k-form-item>
        <k-form-item label="备注">
          <k-field-text v-model="formData.cmt" :data-max-length="256" :data-disabled="true"/>
        </k-form-item>
        <div class ="tableLine2" ><span class="leftText2">中债分类信息</span></div>
        <k-form-item label="中债一级分类">
          <k-field-select  v-model="formData.cbndFrsCtg" :data-disabled="true"  data-dict="cbndFrsCtg"/>
        </k-form-item>
        <k-form-item label="中债二级分类">
          <k-field-select v-model="formData.cbndScdCtg" data-dict="cbndScdCtg" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="投资企业类型（按规模划分）" >
          <k-field-select v-model="formData.lvrgTypSiz"  :data-disabled="true" data-dict="debtor_scale_type"/>
        </k-form-item>
        <k-form-item label="投资企业类型（按技术领域划分）" >
          <k-field-select v-model="formData.lvrgTypTchno"  :data-disabled="true" data-dict="isuOrgTypTchno" />
        </k-form-item>
        <k-form-item label="投资企业类型（按经济类型划分）" >
          <k-field-select v-model="formData.lvrgTypEcn"   :data-disabled="true" data-dict="isuOrgTypEcn"/>
        </k-form-item>
        <k-form-item label="基金投资资产">
          <k-field-text v-model="formData.invAsset" :data-max-length="400" :data-disabled="true"/>
        </k-form-item>
        <div class ="tableLine2" ><span class="leftText2">G06分类信息</span></div>
        <k-form-item label="G06一级分类">
          <k-field-select  v-model="formData.g06FrsCtg" :data-disabled="true" data-dict="g06_first_type"/>
        </k-form-item>
        <k-form-item label="G06二级分类">
          <k-field-select  v-model="formData.g06ScdCtg" :data-disabled="true" data-dict="g06_scd_type"/>
        </k-form-item>
        <k-form-item label="G06三级分类">
          <k-field-select  v-model="formData.g06TrdCtg" :data-disabled="true" data-dict="g06_trd_type"/>
        </k-form-item>
        <div class ="tableLine2" ><span class="leftText2">人行分类信息</span></div>
        <k-form-item label="人行一级分类">
          <k-field-select  v-model="formData.pbnkFrsCtg" :data-disabled="true" data-dict="asset_frs_ctg"/>
        </k-form-item>
        <k-form-item label="人行二级分类">
          <k-field-select  v-model="formData.pbnkScdCtg" :data-disabled="true" data-dict="pbnkFrsCtg"/>
        </k-form-item>
        <k-form-item label="人行三级分类">
          <k-field-select  v-model="formData.pbnkTrdCtg" :data-disabled="true" data-dict="pbnkScdCtg"/>
        </k-form-item>
        <k-form-item label="人行四级分类">
          <k-field-select  v-model="formData.pbnkFouCtg" :data-disabled="true" data-dict ="pbnkTrdCtg"
          />
        </k-form-item>
      </k-form>
      <k-form-footer data-align="center">
        <k-btn class="btn-custom-plain" data-functype="CLOSE">关闭</k-btn>
      </k-form-footer>
      </k-form>
    </k-popup>
  </div>
</template>

<script>
import EditComp from "@/pages/pms/asset/fund/FundPrivateInfoModelEdit";

export default {
  name:"FundPrivateInfoModel",
  components: {
    EditComp
  },
  data() {
    return {
      formData: {},
      selectRowData: {},
      searchParam:{},
    };
  },
  methods: {
    loadGriding(val){
      this.$refs.addInfo.close();
      this.$refs.editInfo.close();
      this.$refs.FundPrivateInfoModelGrid.load(this.searchParam);
    },
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
    },

    submitUploadParamLegal(){
      //文件上传校验
      let validate = this.$refs.addFundPrivateInfoForm.validate();
      if (validate) {
        let formData = { beginDate: this.beginDate};
        let temp = document.getElementsByClassName('upload-demo');
        let lis = temp[0].childNodes[1].childNodes.length;
        if (lis > 0) {
          this.$refs.uploadRef.upload(formData);
        } else {
          this.$message.error("上传文件不能为空!");
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
      this.$refs.FundPrivateInfoModelGrid.load(this.searchParam);
      this.$refs.scrCdId.load();
    },
  },
};
</script>

<style scoped lang="scss">
@import "../AssetComFunction.css";
</style>
