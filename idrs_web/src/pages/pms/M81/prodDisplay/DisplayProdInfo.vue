<template>
  <div>
    <k-form class="my-form " ref="prodInfo" :data-col="2" dataLabelWidth="170px" dataInputWidth="300px">

      <k-form-item label="产品主表id " v-show="false">
        <k-field-text v-model="T8ProdInfo.id"/>
      </k-form-item>
      <k-form-item label="产品形态">
        <k-field-select id="prod_mode" v-model="T8ProdInfo.prodMode"
                        data-dict="t8_prod_create_type" :data-disabled="true" :dataAllowblank="false"/>
      </k-form-item>

      <k-form-item label="产品系列">
        <k-field-select v-model="T8ProdInfo.prodSeries" data-action="T8ProdInfo.getProdSeries"
                        data-display-field="seriesName"
                        data-value-field="seriesCode" :data-disabled="true" :dataAllowblank="false"/>
      </k-form-item>
      <k-form-item label="产品子系列" v-show="T8ProdInfo.prodSonSeries !=null && T8ProdInfo.prodSonSeries !='' && T8ProdInfo.prodSonSeries !=undefined">
        <k-field-select v-model="T8ProdInfo.prodSonSeries" data-action="T8Dict.findSonSeriesInfos" data-display-field="seriesName"
                        data-value-field="seriesCode" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品代码 ">
        <k-field-text v-model="T8ProdInfo.prodCode" :data-max-length="32" :data-disabled="true"
                      :dataAllowblank="false"/>
      </k-form-item>
      <k-form-item label="产品名称 ">
        <k-field-text v-model="T8ProdInfo.prodName" :data-max-length="128" :data-disabled="true"
                      :dataAllowblank="false"/>
      </k-form-item>
      <k-form-item label="产品品牌">
        <k-field-select v-model="T8ProdInfo.prodBrand" data-dict="t8_prod_brand" :data-disabled="true"
                        data-default-value="1" :dataAllowblank="false"/>
      </k-form-item>
      <k-form-item label="产品登记编码">
        <k-field-text v-model="T8ProdInfo.registCode" :data-max-length="15" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="是否关联创意">
        <k-field-select v-model="T8ProdInfo.isOriginality" data-dict="t8_prod_isok" :data-disabled="true"
                        :dataAllowblank="false"/>
      </k-form-item>
      <k-form-item label="创意名称" v-if="T8ProdInfo.isOriginality == '1'">
        <k-field-select id="originality" v-model="T8ProdInfo.originalityId" data-action="T8ProdInfo.getOriginality"
                        data-display-field="originalityName" data-value-field="originalityId" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="风险等级">
        <k-field-select v-model="T8ProdInfo.prodRiskLevel" data-dict="risklevel" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品币种">
        <k-field-select v-model="T8ProdInfo.prodCur" data-dict="t8_prod_currtype_more" :data-disabled="true"
                        :dataAllowblank="false"/>
      </k-form-item>
      <k-form-item label="货币单位(文档用)">
        <k-field-text v-model="T8ProdInfo.prodCompany" :data-disabled="true" :dataAllowblank="false"/>
      </k-form-item>
      <k-form-item label="发行价格">
        <k-field-select v-model="T8ProdInfo.netprice" data-dict="t8_faceprice" :data-disabled="true"
                        :dataAllowblank="false"/>
      </k-form-item>
      <k-form-item label="募集方式">
        <k-field-select v-model="T8ProdInfo.raiseType" data-dict="t8_raise_type" :data-disabled="true"
                        :dataAllowblank="false"/>
      </k-form-item>
      <k-form-item label="收益特点">
        <k-field-select v-model="T8ProdInfo.incomeType" data-dict="t8_income_type" :data-disabled="true"
                        data-default-value="3"/>
      </k-form-item>
      <!--      <k-form-item label="钞汇标识">-->
      <!--        <k-field-select v-model="T8ProdInfo.bnoteRemitFlag" data-dict="bnote_remit_flag" :data-disabled="true" data-default-value="0" />-->
      <!--      </k-form-item>-->
      <k-form-item label="产品分类">
        <k-field-select v-model="T8ProdInfo.prodClassify" data-dict="prod_invest_nature" :data-disabled="true"
                        :dataAllowblank="false"/>
      </k-form-item>
      <k-form-item label="产品管理人">
        <k-field-text v-model="T8ProdInfo.managerCode"
                      :data-default-value="'光大理财有限责任公司'" :data-disabled="true" :dataAllowblank="false"/>
      </k-form-item>
      <k-form-item label="产品特点">
        <k-field-text v-model="T8ProdInfo.prodTrait" :data-max-length="128" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="主要投向/融资主体">
        <k-field-text v-model="T8ProdInfo.investDirection" :data-max-length="128" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="是否份额分类">
        <k-field-radio v-model="T8ProdInfo.isShareSort" data-dict="1yes0no" data-disabled="true"/>
      </k-form-item>
      <k-form-item label="净值披露说明" :data-col="2">
        <k-field-text v-model="T8ProdInfo.publishExplain" :data-max-length="4000" inputType="textarea"
                      :data-disabled="true" :rows="5"/>
      </k-form-item>
      <!--      <k-form-item label="产品简介" :data-col="2">-->
      <!--        <k-field-text v-model="T8ProdInfo.prodDesc" :data-max-length="4000" inputType="textarea" :rows="1" :data-disabled="true"/>-->
      <!--      </k-form-item>-->

      <k-form-item label="期限（文档使用）" :data-col="2">
        <k-field-text v-model="T8ProdInfo.productTerm" :data-max-length="4000" inputType="textarea" :rows="5"
                      :data-disabled="true" :dataAllowblank="false"/>
      </k-form-item>
      <!-- <k-form-item label="其他风险二"  :data-col="2">
        <k-field-text v-model="T8ProdInfo.otherRisk" :data-max-length="2000" inputType="textarea" :data-disabled="true" :rows="1" />
      </k-form-item> -->

      <k-form-item label="创设背景(创设方案使用)" :data-col="2">
        <k-field-text v-model="T8ProdInfo.t8SpareColumnOne" :data-max-length="2000" inputType="textarea"
                      :data-disabled="true" :rows="5"/>
      </k-form-item>
      <k-form-item label="备用字段二" :data-col="2" v-if="T8ProdInfo.t8SpareColumnTwo != ''">
        <k-field-text v-model="T8ProdInfo.t8SpareColumnTwo" :data-max-length="2000" inputType="textarea"
                      :data-disabled="true" :rows="1"/>
      </k-form-item>
      <k-form-item label="备用字段三" :data-col="2" v-if="T8ProdInfo.t8SpareColumnThree != ''">
        <k-field-text v-model="T8ProdInfo.t8SpareColumnThree" :data-max-length="2000" inputType="textarea"
                      :data-disabled="true" :rows="1"/>
      </k-form-item>
      <k-form-item label="备用字段四" :data-col="2" v-if="T8ProdInfo.t8SpareColumnFour != ''">
        <k-field-text v-model="T8ProdInfo.t8SpareColumnFour" :data-max-length="2000" inputType="textarea"
                      :data-disabled="true" :rows="1"/>
      </k-form-item>
      <k-form-item label="备用字段五" :data-col="2" v-if="T8ProdInfo.t8SpareColumnFive != ''">
        <k-field-text v-model="T8ProdInfo.t8SpareColumnFive" :data-max-length="2000" inputType="textarea"
                      :data-disabled="true" :rows="1"/>
      </k-form-item>

    </k-form>
  </div>

</template>

<script>
    import Tools from '@/utils/tools.js';
    import moment from 'moment';
    import eventBus from "@/utils/eventBus";

    export default {
        computed: {},
        model: {
            prop: 'T8ProdInfo',
            event: 'input'
        },
        props: {
            T8ProdInfo: {},
            assemblyMenuType: '',
        },
        data() {
            return {

            }
        },

        methods: {
            validateData() {
                return this.$refs.prodInfo.validate();
            },

        },
        created() {

        },
        mounted() {
        },
        watch: {
            'T8ProdInfo.isShareSort': {
                handler(newVal, oldVal) {
                    eventBus.$emit('shareSortChange', {'shareSort': newVal})
                },
                immediate: true
            }
        }
    }
</script>

<style>

</style>
