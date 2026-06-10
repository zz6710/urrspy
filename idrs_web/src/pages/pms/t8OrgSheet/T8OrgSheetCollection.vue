<template>
  <div>
    <k-form ref="collectT8OrgSheetForm" :data-col="2" isFormBodyScreen>
      <k-form-item label="机构编码" >
        <k-field-text v-model="formData.orgNbrExt" id="orgNbrExt" :data-allowblank="false"
                      :data-disabled="formData.orgNbrExtDisabled" />
      </k-form-item>
      <k-form-item label="机构名称" >
        <k-field-text v-model="formData.orgFullName" id="orgFullName" :data-allowblank="false"
                      :data-disabled="formData.orgFullNameDisabled"/>
      </k-form-item>
      <k-form-item label="统一社会信用编码">
        <k-field-text v-model="formData.csldSocCrdCd" id="csldSocCrdCd" :data-allowblank="false"
                      :data-disabled="formData.csldSocCrdCdDisabled" />
      </k-form-item>
      <k-form-item label="机构种类" >
        <k-field-select v-model="formData.orgTyp" id="orgTyp" data-dict="org_type"
                        :data-disabled="formData.orgTypDisabled" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="同业机构类型" v-if="formData.orgTyp === '04' || formData.orgTyp === '05' || formData.orgTyp === '06'">
        <k-field-select v-model="formData.samBusOrgTyp" id="samBusOrgTyp" data-dict="samBusOrgTyp"
                        :data-disabled="formData.samBusOrgTypDisabled" :data-allowblank="false"  />
      </k-form-item>
      <k-form-item label="机构所属地区">
        <k-field-select v-model="formData.orgBlgZon" id="orgBlgZon" data-dict="prod_sale_area"
                        :data-disabled="formData.orgBlgZonDisabled" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="机构外部评级">
        <k-field-select v-model="formData.orgOutRat" id="orgOutRat" data-dict="mainRating"
                        :data-disabled="formData.orgOutRatDisabled" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="机构内部评级">
        <k-field-select v-model="formData.orgInRat" id="orgInRat" data-dict="mainRating"
                        :data-disabled="formData.orgInRatDisabled" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="是否政府融资平台(财汇)">
        <k-field-select v-model="formData.isPlatFormCh" id="isPlatFormCh" data-dict="isTrue"
                        :data-disabled="formData.isPlatFormChDisabled"/>
      </k-form-item>
      <k-form-item label="是否政府融资平台(手工)">
        <k-field-select v-model="formData.isPlatFormSg" id="isPlatFormSg" data-dict="isTrue"
                        :data-disabled="formData.isPlatFormSgDisabled"/>
      </k-form-item>
      <k-form-item label="发行机构所属行业">
        <k-field-select v-model="formData.ccIndustryIssuer" id="ccIndustryIssuer" data-dict="isuOrgBlgIdt"
                        :data-allowblank="false" :data-disabled="formData.ccIndustryIssuerDisabled"/>
      </k-form-item>
      <k-form-item label="发行机构类型（按规模划分）">
        <k-field-select v-model="formData.isuOrgTypScaleSiz" id="isuOrgTypScaleSiz" data-dict="debtor_scale_type"
                        :data-allowblank="false" :data-disabled="formData.isuOrgTypScaleSizDisabled"/>
      </k-form-item>
      <k-form-item label="发行机构类型（按技术领域划分）">
        <k-field-select v-model="formData.isuOrgTypTchno" id="isuOrgTypTchno" data-dict="isuOrgTypTchno"
                        :data-allowblank="false" :data-disabled="formData.isuOrgTypTchnoDisabled"/>
      </k-form-item>
      <k-form-item label="发行机构类型（按经济类型划分）" >
        <k-field-select v-model="formData.isuOrgTypEcn" id="isuOrgTypEcn" data-dict="isuOrgTypEcn"
                        :data-allowblank="false" :data-disabled="formData.isuOrgTypEcnDisabled"/>
      </k-form-item>
      <k-form-item label="规模" >
        <k-field-select v-model="formData.ccInstituteTypeScale" id="ccInstituteTypeScale"
                        data-dict="debtor_type" :data-disabled="formData.ccInstituteTypeScaleDisabled"/>
      </k-form-item>
      <k-form-item label="行业一级分类" >
        <k-field-select v-model="formData.orgFrsCtg" id="orgFrsCtg" data-dict="isuOrgBlgIdt01"
                        :data-disabled="formData.orgFrsCtgDisabled"/>
      </k-form-item>
      <k-form-item label="行业二级分类" >
        <k-field-select v-model="formData.orgSecCtg" id="orgSecCtg" data-dict="isuOrgBlgIdt"
                        :data-disabled="formData.orgSecCtgDisabled"/>
      </k-form-item>
      <k-form-item label="版本" >
        <k-field-text v-model="formData.version" id="version" :data-disabled="formData.versionDisabled"/>
      </k-form-item>
      <k-form-item label="备注" :data-col="2">
        <k-field-text v-model="formData.remark" id="remark" :data-max-length="120" inputType="textarea" :rows="3" :data-disabled="formData.remarkDisabled"/>
      </k-form-item>


      <k-form-footer slot="footer" data-align="center">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8OrgSheet.updateT8OrgSheet" data-from="collectT8OrgSheetForm"
               :data-model="formData" data-target="t8OrgSheetGrid"  :data-handler="removeVifRubbish">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">
          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
      </k-form-footer>
    </k-form>
  </div>
</template>

<script>
import inputNumberController from "@/pages/pms/basePublish/DisclosureRule/input-number-controller";
import Tools from "@/utils/tools";
import AssetCommon from "@/pages/pms/asset/AssetComFunction";
export default {
  components: {inputNumberController},
  props: {
    //是否是不可修改
    disabledVal: {
      type:Boolean,
    },
    info : {
      type:Object,
    },
    //是否只作为详情展示
    isDetailShow: {
      type:Boolean,
      default:false
    },
  },
  data() {
    return {
      formData: {
      },
    }
  },
  created() {
    this.formData = this.info;
    if(this.disabledVal){
      AssetCommon.checkColumn(this,'formData','Disabled','23','01',this.isDetailShow);
    }
  },
  methods:{
    removeVifRubbish(val){
      AssetCommon.removeVifRubbish(this,'formData','collectT8OrgSheetForm',val);
      return val;
    },
  },
};
</script>
