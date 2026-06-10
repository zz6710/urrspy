<template>
  <div>
    <k-form ref="addChannelRuleForm" :data-col="2" data-input-width="160px" data-label-width="110px" :isFormBodyScreen="true">

      <div class ="tableLine" >
        <span class="leftText">规则信息</span>
        <div class="itemsCorn"></div>
      </div>


      <k-form-item label="渠道规则名称" :dataCol="2" key="channelRuleName">
        <k-field-text v-model="formData.channelRuleName" :data-disabled="true" :data-max-length="60"></k-field-text>
      </k-form-item>
      <k-form-item label="信披类型" >
        <k-field-select v-model="formData.disclosureType" data-dict="xp_doc_type" :data-disabled="true"   ></k-field-select>
      </k-form-item>
      <k-form-item label="信披子类型"  v-if="formData.disclosureType=='5'||formData.disclosureType=='6'||formData.disclosureType=='1'||formData.disclosureType=='9'">
        <k-field-select v-model="formData.disclosureSonType"  data-dict="xp_son_type" :data-disabled="true"></k-field-select>
      </k-form-item>
      <k-form-item label="渠道名称" :dataCol="2">
        <k-field-select v-model="formData.channelIds"   data-action="DisclosureChannel.findDisChannelDetail" :data-disabled="true" data-value-field="id" data-display-field="channelName"  :data-multiple="true"></k-field-select>
      </k-form-item>
      <k-form-item label="渠道文件类型"  :dataCol="1">
        <k-field-select v-model="formData.uploadFileType" :data-disabled="true" data-dict="xp_upload_file_type" :data-multiple="true" :rows="1"/>
      </k-form-item>
      <k-form-item label="确认文件后缀"  :dataCol="1">
        <k-field-text v-model="formData.suffixFileName" :data-disabled="true" />
      </k-form-item>
      <k-form-item label="渠道文件名称" :dataCol="2">
        <k-field-text v-model="formData.uploadFileNameType"  :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="备注" :data-col="2">
        <k-field-text v-model="formData.remark" :data-disabled="true"
                      :data-max-length="200" inputType="textarea" :rows="3"/>
      </k-form-item>


      <div class ="tableLine" >
        <span class="leftText">产品参数</span>
        <div class="itemsCorn"></div>
      </div>


      <k-form-item label="产品形态" key="prodForm" >
        <k-field-select v-model="formData.prodForm" data-dict="xp_prod_form" :data-disabled="true" v-show="!changeMultiple" :data-multiple="false"></k-field-select>
        <k-field-select v-model="formData.prodForm" data-dict="xp_prod_form" :data-disabled="true"  v-show="changeMultiple" :data-multiple="true"></k-field-select>
      </k-form-item>
      <k-form-item label="投资周期维度" key="invPrdDime" v-if="formData.prodForm == '06'">
        <k-field-select v-model="formData.invPrdDime" data-dict="xp_cycle_dimension" :data-disabled="true" v-show="!changeMultiple" :data-multiple="false"></k-field-select>
        <k-field-select v-model="formData.invPrdDime" data-dict="xp_cycle_dimension" :data-disabled="true"  v-show="changeMultiple" :data-multiple="true"></k-field-select>
      </k-form-item>
      <k-form-item label="投资周期长度" key="invPrdLen" v-if="formData.prodForm == '06'">
        <k-field-text v-model="formData.invPrdLen" :data-disabled="true"></k-field-text>
        <k-field-text v-model="formData.invPrdLen" :data-disabled="true"></k-field-text>
      </k-form-item>
      <k-form-item label="销售对象" key="prodObj">
        <k-field-select v-model="formData.prodObj" :data-disabled="true" data-dict="xp_target_customer" v-show="!changeMultiple" :data-multiple="false"></k-field-select>
        <k-field-select v-model="formData.prodObj" :data-disabled="true" data-dict="xp_target_customer"  v-show="changeMultiple" :data-multiple="true"></k-field-select>
      </k-form-item>
      <k-form-item label="募集方式"   key="prodClcMth">
        <k-field-select v-model="formData.prodClcMth" :data-disabled="true" data-dict="xp_raise_type" v-show="!changeMultiple" :data-multiple="false"></k-field-select>
        <k-field-select v-model="formData.prodClcMth" :data-disabled="true" data-dict="xp_raise_type"  v-show="changeMultiple" :data-multiple="true"></k-field-select>
      </k-form-item>
      <k-form-item label="产品投资性质" >
        <k-field-select
          v-model="formData.prodInvTyp" data-dict="xp_prod_invest_nature" v-show="!changeMultiple" :data-multiple="false"
          :data-disabled="true">
        </k-field-select>
        <k-field-select
          v-model="formData.prodInvTyp" data-dict="xp_prod_invest_nature" v-show="changeMultiple" :data-multiple="true"
          :data-disabled="true">
        </k-field-select>
      </k-form-item>
      <k-form-item label="产品系列"  :dataCol="2" key="prodSerCd">
        <k-field-select
          v-model="formData.prodSerCd"
          data-action="T8ProdInfo.getNewProdSeries" v-show="!changeMultiple" :data-multiple="false"
          data-value-field="seriesCode"
          data-display-field="seriesName"
          :data-disabled="true"
        ></k-field-select>
        <k-field-select
          v-model="formData.prodSerCd"
          data-action="T8ProdInfo.getNewProdSeries"  v-show="changeMultiple" :data-multiple="true"
          data-value-field="seriesCode"
          data-display-field="seriesName"
          :data-disabled="true"
        ></k-field-select>
      </k-form-item>

    </k-form>
  </div>
</template>

<script>
export default {
  name: "ChannelRuleOperate.vue",
  props: {
    formData: {},
  },
  data() {
    return {
      xpqd: [
        {label: '中国光大银行官网', value: '1'},
        {label: '光大理财官网', value: '2'},
        {label: '光大银行销售渠道', value: '3'},
        {label: '光大理财直销渠道', value: '4'},
        {label: '行外代销机构', value: '5'},
        {label: '专户机构邮箱', value: '6'},
      ],
    }
  },
  computed: {
    changeMultiple(){
      if(this.formData.disclosureSonType==='0903'||this.formData.disclosureType==='6') {
        return true;
      }
    },
  },
  watch: {
  },
  methods:{
  },
  created() {
  },
}
</script>

<style scoped>
.tableLine {
  margin: 5% 0 3% 10%;
  border-top: 1px dotted rgba(2, 2, 2, 0.34);
  width: 100%;
  position: relative;
  font-size: 14px;
}

.itemsCorn {
  position: absolute;
  background: #41A0EB;
  border-radius: 0px;
  left: -6.2%;
  width: 7px;
  height: 15px;
  transform: translateX(-50%) translateY(-50%);
}
.leftText {
  position: absolute;
  left: 0;
  background-color: #ffffff;
  font-weight: 300;
  padding: 0 15px;
  transform: translateX(-50%) translateY(-50%);
}
</style>
