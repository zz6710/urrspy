<template>
  <div>
    <k-form ref="collectT8OrgSheetForm" :data-col="2" isFormBodyScreen>
      <k-form-item label="序号" v-show="false">
        <k-field-text v-model="formData.id"    :data-disabled="true" />
      </k-form-item>
      <k-form-item label="机构编码">
        <k-field-text v-model="formData.orgNbrExt"    :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="机构名称">
        <k-field-text v-model="formData.orgFullName"    :data-disabled="true" />
      </k-form-item>
      <k-form-item label="机构简称">
        <k-field-text v-model="formData.orgShtNm"    :data-disabled="true" />
      </k-form-item>
      <k-form-item label="统一社会信用编码">
        <k-field-text v-model="formData.csldSocCrdCd"    :data-disabled="true" />
      </k-form-item>
      <k-form-item label="机构种类">
        <k-field-select v-model="formData.orgTyp" data-dict="org_type"   :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="同业机构类型" v-if="formData.orgTyp === '04' || formData.orgTyp === '05' || formData.orgTyp === '06'">
        <k-field-select v-model="formData.samBusOrgTyp" data-dict="samBusOrgTyp":data-disabled="true"/>
      </k-form-item>
      <k-form-item label="机构所属地区">
        <k-field-select v-model="formData.orgBlgZon" data-dict="prod_sale_area"    :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="机构外部评级">
        <k-field-select v-model="formData.orgOutRat" data-dict="mainRating"    :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="机构内部评级">
        <k-field-select v-model="formData.orgInRat" data-dict="mainRating"    :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="是否政府融资平台(财汇)">
        <k-field-select v-model="formData.isPlatFormCh" data-dict="isTrue" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="是否政府融资平台(手工)">
        <k-field-select v-model="formData.isPlatFormSg" data-dict="isTrue" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="发行机构所属行业">
        <k-field-select v-model="formData.ccIndustryIssuer" data-dict="isuOrgBlgIdt"    :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="发行机构类型（按规模划分）">
        <k-field-select v-model="formData.isuOrgTypScaleSiz" data-dict="debtor_scale_type"    :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="发行机构类型（按技术领域划分）">
        <k-field-select v-model="formData.isuOrgTypTchno" data-dict="isuOrgTypTchno"    :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="发行机构类型（按经济类型划分）" >
        <k-field-select v-model="formData.isuOrgTypEcn" data-dict="isuOrgTypEcn"    :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="规模" >
        <k-field-select v-model="formData.ccInstituteTypeScale" data-dict="debtor_type" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="行业一级分类" >
        <k-field-select v-model="formData.orgFrsCtg" data-dict="isuOrgBlgIdt01" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="行业二级分类" >
        <k-field-select v-model="formData.orgSecCtg"  data-dict="isuOrgBlgIdt" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="版本" >
        <k-field-text v-model="formData.version" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="备注" :data-col="2">
        <k-field-text v-model="formData.remark" :data-max-length="120" inputType="textarea" :rows="3" :data-disabled="true"/>
      </k-form-item>
    </k-form>
  </div>
</template>

<script>
import inputNumberController from "@/pages/pms/basePublish/DisclosureRule/input-number-controller";
import Tools from "@/utils/tools";
export default {
  components: {inputNumberController},
  props: {
    info : {
      type:Object,
    },
  },
  data() {
    return {
      formData: {
      },
    }
  },
  created() {
    this.formData =this.info ;
  },
  methods:{
  },
};
</script>
