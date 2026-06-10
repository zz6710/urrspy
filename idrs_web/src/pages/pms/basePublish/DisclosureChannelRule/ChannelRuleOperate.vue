<template>
  <div>
    <k-form ref="addChannelRuleForm" :data-col="2" data-input-width="160px" data-label-width="110px" :isFormBodyScreen="true">

      <div class ="tableLine" >
        <span class="leftText">规则信息</span>
        <div class="itemsCorn"></div>
      </div>

      <k-form-item label="渠道规则名称" :dataCol="2" key="channelRuleName">
        <k-field-text v-model="formData.channelRuleName" :data-allowblank="false" :data-max-length="100"></k-field-text>
      </k-form-item>
      <k-form-item label="信披类型" >
        <k-field-select v-model="formData.disclosureType"  :data-allowblank="false"   :data-disabled="change"
                        :data-data="DocTypeDict"   data-value-field="value" data-display-field="text"></k-field-select>
      </k-form-item>
      <k-form-item label="信披子类型"  v-if="formData.disclosureType==='5'||formData.disclosureType==='6'||formData.disclosureType==='1'||formData.disclosureType==='9'">
        <k-field-select v-model="formData.disclosureSonType"  :data-allowblank="false" :data-data="addDocTypeDict"   data-value-field="value" data-display-field="text" :data-disabled="change"
        ></k-field-select>
      </k-form-item>

      <k-form-item label="渠道名称" :dataCol="2">
        <k-field-select v-model="formData.channelIds"  data-action="DisclosureChannel.findDisChannel" :data-allowblank="false" data-value-field="id" data-display-field="channelName"  ></k-field-select>
      </k-form-item>
      <k-form-item label="渠道文件类型" :dataCol="1" >
        <k-field-select v-model="formData.uploadFileType" :data-allowblank="false" data-dict="xp_upload_file_type" :data-multiple="true" :rows="1"/>
      </k-form-item>
      <k-form-item label="确认文件后缀" :dataCol="1" >
        <k-field-text v-model="formData.suffixFileName" :data-allowblank="true"
                      :rows="1" data-placeholder="例如：.ok"/>
      </k-form-item>
      <k-form-item label="渠道文件名称" :dataCol="2" >
        <k-field-text v-model="formData.uploadFileNameType" :data-allowblank="true"
                      inputType="textarea" :rows="2" :data-max-length="128"/>
      </k-form-item>
      <k-form-item label="备注" :data-col="2">
        <k-field-text v-model="formData.remark" :data-allowblank="true"
                      :data-max-length="256" inputType="textarea" :rows="3"/>
      </k-form-item>

      <div class ="tableLine" >
        <span class="leftText">产品参数</span>
        <div class="itemsCorn"></div>
      </div>


      <k-form-item label="产品形态" key="prodForm" >
        <k-field-select v-model="formData.prodForm" data-dict="xp_prod_form" :data-allowblank="true"  v-show="!changeMultiple" :data-multiple="false"></k-field-select>
        <k-field-select v-model="formData.prodForm" data-dict="xp_prod_form" :data-allowblank="true"  v-show="changeMultiple" :data-multiple="true"></k-field-select>
      </k-form-item>
      <k-form-item label="投资周期维度" key="invPrdDime" v-if="formData.prodForm == '06'">
        <k-field-select v-model="formData.invPrdDime" data-dict="xp_cycle_dimension" :data-allowblank="true"  v-show="!changeMultiple" :data-multiple="false"></k-field-select>
        <k-field-select v-model="formData.invPrdDime" data-dict="xp_cycle_dimension" :data-allowblank="true"  v-show="changeMultiple" :data-multiple="true"></k-field-select>
      </k-form-item>
      <k-form-item label="投资周期长度" key="invPrdLen" v-if="formData.prodForm == '06'">
        <k-field-text v-model="formData.invPrdLen" :data-allowblank="true"  v-show="!changeMultiple" :data-multiple="false" :data-max-length="3" data-regx="^\+?[1-9][0-9]*$" data-regx-text="请输入大于0的整数"></k-field-text>
        <k-field-text v-model="formData.invPrdLen" :data-allowblank="true"  v-show="changeMultiple" :data-multiple="true" :data-max-length="3" data-regx="^\+?[1-9][0-9]*$" data-regx-text="请输入大于0的整数"></k-field-text>
      </k-form-item>
      <k-form-item label="销售对象" key="prodObj">
        <k-field-select v-model="formData.prodObj" data-dict="xp_target_customer"  v-show="!changeMultiple" :data-multiple="false"></k-field-select>
        <k-field-select v-model="formData.prodObj" data-dict="xp_target_customer"  v-show="changeMultiple" :data-multiple="true"></k-field-select>
      </k-form-item>
      <k-form-item label="募集方式"   key="prodClcMth">
        <k-field-select v-model="formData.prodClcMth" data-dict="xp_raise_type"  v-show="!changeMultiple" :data-multiple="false"></k-field-select>
        <k-field-select v-model="formData.prodClcMth" data-dict="xp_raise_type"  v-show="changeMultiple" :data-multiple="true"></k-field-select>
      </k-form-item>
      <k-form-item label="产品投资性质" >
        <k-field-select
          v-model="formData.prodInvTyp" data-dict="xp_prod_invest_nature" v-show="!changeMultiple" :data-multiple="false"
          :data-allowblank="true">
        </k-field-select>
        <k-field-select
          v-model="formData.prodInvTyp" data-dict="xp_prod_invest_nature" v-show="changeMultiple" :data-multiple="true"
          :data-allowblank="true">
        </k-field-select>
      </k-form-item>
      <k-form-item label="产品系列" :dataCol="2" key="prodSerCd">
        <k-field-select
          v-model="formData.prodSerCd"
          data-action="T8ProdInfo.getNewProdSeries"  v-show="!changeMultiple" :data-multiple="false"
          data-value-field="seriesCode"
          data-display-field="seriesName"
          :data-allowblank="true"
          ref="prodsRef"
        ></k-field-select>
        <k-field-select
          v-model="formData.prodSerCd"
          data-action="T8ProdInfo.getNewProdSeries"  v-show="changeMultiple" :data-multiple="true"
          data-value-field="seriesCode"
          data-display-field="seriesName"
          :data-allowblank="true"
          ref="prodsRef"
        ></k-field-select>
      </k-form-item>


    </k-form>
  </div>
</template>

<script>
import Tools from "@/utils/tools";

export default {
  name: "ChannelRuleOperate.vue",
  props: {
    formData: {},
    oldSelectProds: '',
    oldSelectChannels: '',
    DocTypeDict: {},
    change: false,
  },
  data() {
    return {
      oldSelectProdArr: [],//原来选中的产品数组
      oldSelectChannelArr: [],//原来被选中的渠道数组
      addDocTypeDict:{},
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
    'formData.disclosureType'() {
      this.$set(this.formData, 'disclosureSonType', '');
      this.queryProd();
      this.changeMul();
    },
    'formData.disclosureSonType'() {
      this.changeMul();
    },
    'formData.prodForm'(value) {
      this.$set(this.formData, 'invPrdDime', '');
      this.$set(this.formData, 'invPrdLen', '');
    },
    'this.oldSelectProds'(value) {
      this.oldSelectProdArr = this.oldSelectProds.split(',');
    }
  },
  created() {
    // this.xpType();
    this.oldSelectProdArr = this.oldSelectProds.split(',');
    this.oldSelectChannelArr = this.oldSelectChannels.split(',');
    this.queryProd();
  },
  methods: {
    changeMul() {
      //切换选框模式时滞空数据
      if(!(this.formData.disclosureSonType==='0903'||this.formData.disclosureType==='6')) {
        this.$set(this.formData, 'prodSerCd', '');
        this.$set(this.formData, 'prodInvTyp', '');
        this.$set(this.formData, 'prodClcMth', '');
        this.$set(this.formData, 'prodObj', '');
        this.$set(this.formData, 'invPrdLen', '');
        this.$set(this.formData, 'invPrdDime', '');
        this.$set(this.formData, 'prodForm', '');
      }
    },
    //筛选需要展示的类型字典
    xpType() {
      this.httpUtil.comnQuery({
        action: "DisclosureMod.getXPType",
        params: null
      }).then(data => {
        this.DocTypeDict = data.rows;
      }).catch({})
    },
    queryProd(){
      this.httpUtil.comnQuery({
        action: "DisclosureMod.getXPTypeByDocType",
        params: {disclosureType: this.formData.disclosureType}
      }).then(data => {
        this.addDocTypeDict = data.rows;
      }).catch({})
    },
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
