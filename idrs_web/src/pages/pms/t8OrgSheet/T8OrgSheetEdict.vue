<template>
  <div>
    <k-form ref="editT8OrgSheetForm" :data-col="2">
      <k-form-item label="机构编码">
        <k-field-text v-model="formData.orgNbrExt" id="orgNbrExt" :data-allowblank="false" :data-disabled="formData.orgNbrExtDisabled"/>
      </k-form-item>
      <k-form-item label="机构名称">
        <k-field-text v-model="formData.orgFullName" id="orgFullName" :data-allowblank="false" :data-disabled="formData.orgFullNameDisabled"/>
      </k-form-item>
      <k-form-item label="机构简称" >
        <k-field-text v-model="formData.orgShtNm"  id="orgShtNm" :data-allowblank="false" :data-disabled="formData.orgShtNmDisabled"/>
      </k-form-item>
      <k-form-item label="统一社会信用编码">
        <k-field-text v-model="formData.csldSocCrdCd" id="csldSocCrdCd" :data-allowblank="false" :data-disabled="formData.csldSocCrdCdDisabled"/>
      </k-form-item>

      <k-form-footer data-align="center">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-action="action" data-from="editT8OrgSheetForm" :data-handler="removeVifRubbish"
               :data-model="formData" data-target="t8OrgSheetGrid">
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
import AssetCommon from "@/pages/pms/asset/AssetComFunction";
export default {
  components: {inputNumberController},
  props: {
    action: {
      type: String,
      default: ""
    },
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
      formData: {},
    }
  },
  created() {
    this.formData =  this.info ;
    if(this.disabledVal){
      AssetCommon.checkColumn(this,'formData','Disabled','23','02',this.isDetailShow);
    }
  },
  methods:{
    removeVifRubbish(val){
      AssetCommon.removeVifRubbish(this, 'formData', 'editT8OrgSheetForm', val);
      return val;
    },
  },
};
</script>
