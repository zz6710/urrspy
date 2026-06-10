<template>
  <div>
    <k-form class="my-form " ref="prodInfo" :data-col="2" dataLabelWidth="170px" dataInputWidth="300px">

      <k-form-item label="产品主表id " v-show="false">
        <k-field-text v-model="T8ProdInfo.id" />
      </k-form-item>
      <k-form-item label="产品形态">
        <k-field-select id="prod_mode" v-model="T8ProdInfo.prodMode"
                        data-dict="t8_prod_create_type" :data-disabled="true" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="产品系列">
        <k-field-select v-model="T8ProdInfo.prodSeries" data-action="T8ProdInfo.getProdSeries" data-display-field="seriesName"
                        data-value-field="seriesCode" :data-allowblank="false" :data-disabled="T8ProdInfo.flowTemplate=='1'"/>
      </k-form-item>
      <k-form-item label="产品子系列" id="prodSonSeries" v-show="(T8ProdInfo.prodSonSeries !=null&&T8ProdInfo.prodSonSeries !=''&&T8ProdInfo.prodSonSeries !=undefined) || menuName == 'M81007Copy'">
        <k-field-select v-model="T8ProdInfo.prodSonSeries" data-action="T8Dict.findSonSeriesInfos" data-display-field="seriesName"
                        data-value-field="seriesCode" :data-allowblank="menuName == 'M81007Copy'" :data-disabled="T8ProdInfo.flowTemplate=='1'"/>
      </k-form-item>
      <k-form-item label="产品代码 ">
        <k-field-text v-model="T8ProdInfo.prodCode" data-validate-type="codeLetterLine" :data-allowblank="false"
                      :data-max-length="20" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品名称 ">
        <k-field-text v-model="T8ProdInfo.prodName" :data-allowblank="false"
                      @data-on-change="isExistsProd" :data-max-length="128"/>
      </k-form-item>
      <k-form-item label="产品品牌">
        <k-field-select v-model="T8ProdInfo.prodBrand" :data-allowblank="false" data-dict="t8_prod_brand" data-default-value="1"/>
      </k-form-item>
      <k-form-item label="产品登记编码">
        <k-field-text v-model="T8ProdInfo.registCode" data-disabled="true" :data-max-length="15"/>
      </k-form-item>
      <k-form-item label="是否关联创意">
        <k-field-select id="isOriginality" v-model="T8ProdInfo.isOriginality" :data-allowblank="false" data-dict="t8_prod_isok"/>
      </k-form-item>
      <k-form-item label="创意名称" v-if="T8ProdInfo.isOriginality == '1'">
        <k-field-select id="originality" v-model="T8ProdInfo.originalityId"  data-action="T8ProdInfo.getOriginality"
                        data-display-field="originalityName"  data-value-field="originalityId"
                        :data-allowblank="T8ProdInfo.isOriginality == '0'"/>
      </k-form-item>
      <k-form-item label="风险等级">
        <k-field-select v-model="T8ProdInfo.prodRiskLevel" :data-disabled="true"  data-dict="risklevel"/>
      </k-form-item>
      <k-form-item label="产品币种">
        <k-field-select v-model="T8ProdInfo.prodCur" :data-allowblank="false" data-dict="t8_prod_currtype_more"
                        :data-default-value="'CNY'" @data-on-change="setProdCompany"/>
      </k-form-item>

      <k-form-item label="货币单位(文档用)">
        <k-field-text v-model="T8ProdInfo.prodCompany" :data-allowblank="false" :data-default-value="'元'"/>
      </k-form-item>

      <k-form-item label="发行价格">
        <k-field-select v-model="T8ProdInfo.netprice" :data-allowblank="false" data-dict="t8_faceprice"
                        data-display-field="itemval" data-value-field="itemkey"
                        :data-default-value="'1'"/>
      </k-form-item>

      <k-form-item label="募集方式">
        <k-field-select v-model="T8ProdInfo.raiseType" :data-allowblank="false" data-dict="t8_raise_type"/>
      </k-form-item>
      <k-form-item label="收益特点">
        <k-field-select v-model="T8ProdInfo.incomeType"   data-dict="t8_income_type" data-default-value="3" />
      </k-form-item>
      <k-form-item label="钞汇标识" v-if="T8ProdInfo.prodCur != 'CNY'">
        <k-field-select v-model="T8ProdInfo.bnoteRemitFlag" data-dict="bnote_remit_flag"
                        :data-multiple="true" :data-allowblank="T8ProdInfo.prodCur == 'CNY'"/>
      </k-form-item>
      <k-form-item label="产品分类">
        <k-field-select v-model="T8ProdInfo.prodClassify"  :data-allowblank="false" data-dict="prod_invest_nature"/>
      </k-form-item>
      <k-form-item label="产品管理人">
        <k-field-text v-model="T8ProdInfo.managerCode" :data-allowblank="false" data-disabled
                      :data-default-value="'光大理财有限责任公司'"/>
      </k-form-item>
      <k-form-item label="产品特点">
        <k-field-text v-model="T8ProdInfo.prodTrait"   :data-max-length="128"/>
      </k-form-item>
      <k-form-item label="主要投向/融资主体">
        <k-field-text v-model="T8ProdInfo.investDirection"   :data-max-length="128"/>
      </k-form-item>

      <k-form-item label="是否份额分类">
        <k-field-radio v-model="T8ProdInfo.isShareSort" data-dict="1yes0no" :dataAllowblank="false" />
      </k-form-item>

      <k-form-item label="净值披露说明" :data-col="2">
        <k-field-text v-model="T8ProdInfo.publishExplain" :data-max-length="4000" inputType="textarea" :rows="5"/>
      </k-form-item>
<!--      <k-form-item label="产品简介" :data-col="2">-->
<!--        <k-field-text v-model="T8ProdInfo.prodDesc" :data-max-length="4000" inputType="textarea" :rows="1"/>-->
<!--      </k-form-item>-->
      <k-form-item label="期限（文档使用）" :data-col="2">
        <k-field-text v-model="T8ProdInfo.productTerm" :data-max-length="4000" inputType="textarea" :rows="5" :data-allowblank="false"/>
      </k-form-item>
      <!-- <k-form-item label="其他风险二" :data-col="2">
        <k-field-text v-model="T8ProdInfo.otherRisk"  inputType="textarea" :rows="1" :data-max-length="2000"/>
      </k-form-item> -->

      <k-form-item label="创设背景(创设方案使用)" :data-col="2">
        <k-field-text v-model="T8ProdInfo.t8SpareColumnOne" :data-max-length="2000" inputType="textarea" :rows="5"  @data-on-change="handleStrT8SpareColumnOne"/>
      </k-form-item>
      <k-form-item label="备用字段二" :data-col="2" v-if="T8ProdInfo.t8SpareColumnTwo != ''">
        <k-field-text v-model="T8ProdInfo.t8SpareColumnTwo" :data-max-length="2000" inputType="textarea" :rows="1"/>
      </k-form-item>
      <k-form-item label="备用字段三" :data-col="2" v-if="T8ProdInfo.t8SpareColumnThree != ''">
        <k-field-text v-model="T8ProdInfo.t8SpareColumnThree" :data-max-length="2000" inputType="textarea" :rows="1"/>
      </k-form-item>
      <k-form-item label="备用字段四" :data-col="2" v-if="T8ProdInfo.t8SpareColumnFour != ''">
        <k-field-text v-model="T8ProdInfo.t8SpareColumnFour" :data-max-length="2000" inputType="textarea" :rows="1"/>
      </k-form-item>
      <k-form-item label="备用字段五" :data-col="2" v-if="T8ProdInfo.t8SpareColumnFive != ''">
        <k-field-text v-model="T8ProdInfo.t8SpareColumnFive" :data-max-length="2000" inputType="textarea" :rows="1"/>
      </k-form-item>

      <k-form-footer data-align="center" v-show="menuName == 'M81007'">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdInfo.addT8ProdInfoByButton" data-from="prodInfo"
               :data-model="T8ProdInfo" :data-handler="addHandler" :data-after-success="passDataSuccess">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>保存
        </k-btn>
      </k-form-footer>


    </k-form>
  </div>

</template>

<script>
  import eventBus from '@/utils/eventBus'
  import Tools from '@/utils/tools.js';
  import moment from 'moment';
  import newAggressiveTest from "@/pages/flow/newAggressiveTest";

  export default {
    computed: {},
    model: {
      prop: 'T8ProdInfo',
      event: 'input'
    },
    props: {
      T8ProdInfo: {},
      menuName:'',
    },
    data() {
      return {


      }
    },

    methods: {
      setProdCompany(val){
        if(val == 'USD'){
          this.T8ProdInfo.prodCompany = '美元';
        }else if (val == 'CNY'){
          this.T8ProdInfo.prodCompany = '元';
        }else{
          this.T8ProdInfo.prodCompany = '';
        }
      },
      passDataSuccess(){
        this.$emit('isShowButton', '1')
      },
      addHandler(val){
        this.$set(val,'assemblyMenuType','prodInfo');
      },
      isExistsProd(code,name){
        this.httpUtil.comnQuery({
          action: "T8ProdInfo.isExistsProdCountUpt",
          params: this.T8ProdInfo,
        }).then(data => {
          if(data.rows.length > 0){
            Tools.alert("产品名称已存在，请重新输入!","danger");
            this.T8ProdInfo.prodName = '';
            return false;
          }
        });
      },
      validateData() {
        return this.$refs.prodInfo.validate();
      },
      handleStrT8SpareColumnOne(val){

       if(val.indexOf("\\n")){
         let arrys=[];
         arrys = val.split("\n");
         var str ="";
         for(var i = 0; i<arrys.length;i++){
           if(i<arrys.length-1){
             str = str+"\u3000\u3000"+arrys[i].toString().trim()+"\n"
           }else{
             str = str+"\u3000\u3000"+arrys[i].toString().trim()
           }

         }
        this.T8ProdInfo.t8SpareColumnOne = str;
       }
      }
    },
    created() {
    },
    mounted() {
    },
    watch: {
      'T8ProdInfo.isShareSort':{
        handler(newVal, oldVal){
          eventBus.$emit('shareSortChange', {'shareSort': newVal})
        },
        immediate:true
      },


    },

  }
</script>

<style>

</style>
