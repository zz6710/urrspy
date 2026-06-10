<template>
  <k-form ref="addForm" :data-col="2">
    <k-form-item label="产品代码"  v-show="false">
      <k-field-text v-model="value.prodCode" :data-allowblank="false" data-disabled/>
    </k-form-item>
<!--    <k-form-item label="启用日期">
      <k-field-date v-model="value.enableDate" :data-allowblank="false" :dataMinValue="this.currentWorkday"/>
    </k-form-item>-->
    <k-form-item label="创建人" v-show="false">
      <k-field-text v-model="value.crtUser"/>
    </k-form-item>
    <k-form-item label="销售商代码" v-show="true">
      <k-field-select v-model="value.distributorCode" data-action="T8Dict.findTaDistributorInfos"  :data-multiple="false"
                    data-display-field="distributorCode,distributorName"  data-value-field="distributorCode"/>
    </k-form-item>
    <k-form-item label="可销售标志">
      <k-field-select v-model="value.saleFlag" data-dict="t8_sale_flag" :data-allowblank="false"/>
    </k-form-item>

    <k-form-item label="认购到账天数(工作日) ">
      <k-field-text v-model="value.raiseCapitalDays" :data-max-length="150" :data-allowblank="false" />
    </k-form-item>
    <k-form-item label="申购到账天数(工作日)">
      <k-field-text v-model="value.subsCapitalDays" :data-max-length="150" :data-allowblank="false" />
    </k-form-item>
    <k-form-item label="赎回到账天数(工作日) ">
      <k-field-text v-model="value.redeemCapitalDays" :data-max-length="150" :data-allowblank="false" />
    </k-form-item>
    <k-form-item label="分红到账天数(工作日) ">
      <k-field-text v-model="value.divCapitalDays" :data-max-length="150" :data-allowblank="false" />
    </k-form-item>
    <k-form-item label="认购账务模式">
      <k-field-text v-model="value.raiseAccount" :data-max-length="150"/>
    </k-form-item>
    <k-form-item label="申购账务模式">
      <k-field-text v-model="value.subsAccount" :data-max-length="150"/>
    </k-form-item>
    <k-form-item label="光大银行募集期开始时间">
      <k-field-date v-model="value.gdRaiseStartDate" @data-on-change="change" data-type="date" data-date-format="yyyy-MM-dd"></k-field-date>
    </k-form-item>
    <k-form-item label="光大银行募集期结束时间">
      <k-field-date v-model="value.gdRaiseEndDate" @data-on-change="change" data-type="date" data-date-format="yyyy-MM-dd"/>
    </k-form-item>
    <k-form-item label="非母行代销商名称" v-show="true">
      <k-field-select v-model="value.otherDistributorName" data-action="T8Dict.findTaDistributorInfos"  :data-multiple="false"
                      data-display-field="distributorCode,distributorName"  data-value-field="distributorCode" :data-allowblank="false"/>
    </k-form-item>
    <k-form-item label="非母行代销商募集期开始时间（如有）">
      <k-field-date v-model="value.otherRaiseStartDate" @data-on-change="change" data-type="date" data-date-format="yyyy-MM-dd"/>
    </k-form-item>
    <k-form-item label="非母行代销商募集期结束时间（如有）">
      <k-field-date v-model="value.otherRaiseEndDate" @data-on-change="change" data-type="date" data-date-format="yyyy-MM-dd"/>
    </k-form-item>
    <k-form-item label="申购费优惠期开始时间">
      <k-field-date v-model="value.discountSubsStartDate" @data-on-change="change" data-type="date" data-date-format="yyyy-MM-dd"/>
    </k-form-item>
    <k-form-item label="申购费优惠期结束时间">
      <k-field-date v-model="value.discountSubsEndDate" @data-on-change="change" data-type="date" data-date-format="yyyy-MM-dd"/>
    </k-form-item>
    <k-form-item label="申购费折扣率">
      <k-field-text v-model="value.discountSubsScale" :data-max-length="9" data-min-value="0" data-digits="5"  data-integer-length="3" :data-allowblank="false"/>
    </k-form-item>
    <k-form-item label="赎回费优惠期开始时间">
      <k-field-date v-model="value.discountRedeemStartDate" @data-on-change="change" data-type="date" data-date-format="yyyy-MM-dd"/>
    </k-form-item>
    <k-form-item label="赎回费优惠期结束时间">
      <k-field-date v-model="value.discountRedeemEndDate" @data-on-change="change" data-type="date" data-date-format="yyyy-MM-dd"/>
    </k-form-item>
    <k-form-item label="赎回费折扣率">
      <k-field-text data-validate-type="number" v-model="value.discountRedeemScale" data-min-value="0" data-max-value="100" data-digits="5" :data-allowblank="false"/>
    </k-form-item>
    <k-form-item label="封闭期投资期（暂停赎回）">
      <k-field-text v-model="value.closePeriod" :data-max-length="120"/>
    </k-form-item>
    <k-form-item label="募集期交易状态">
      <k-field-text v-model="value.raiseStatus" :data-max-length="120"/>
    </k-form-item>
    <k-form-item label="净值披露频率">
      <k-field-text v-model="value.valRevealFreq" :data-max-length="120"/>
    </k-form-item>
    <k-form-item label="申购费优惠期开始时间（如有）">
      <k-field-date v-model="value.discountSubsStartDate" @data-on-change="change" data-type="date" data-date-format="yyyy-MM-dd"/>
    </k-form-item>
    <k-form-item label="申购费优惠期结束时间（如有）">
      <k-field-date v-model="value.discountSubsEndDate" @data-on-change="change" data-type="date" data-date-format="yyyy-MM-dd"/>
    </k-form-item>

    <k-form-item label="光大银行柜台渠道开放时间">
      <k-field-time v-model="value.openCounterTime" @data-on-change="change" @data-on-focus="focus"
                    @data-on-blur="blur" data-value-format="HHmmss"/>
    </k-form-item>
    <k-form-item label="光大银行其他渠道开放时间">
      <k-field-time v-model="value.otherCounterTime" @data-on-change="change" @data-on-focus="focus"
                    @data-on-blur="blur" data-value-format="HHmmss"/>
      </k-form-item>

    <k-form-footer data-align="center">
      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdDistributor.addT8ProdDistributor"
                 data-from="addForm" :data-model="value" :data-handler="confirmHandler"
                 data-target="T81001Grid">
         <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
      </k-btn>
      <k-btn class="btn-custom-plain" data-functype="CLOSE">
       <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
    </k-form-footer>
  </k-form>
</template>

<script>
  import kayak from '@/frame/kayak.js'
  import Tools from '@/utils/tools.js';

  export default {
    props: {
      updSuccess: Function,
      info:{
        type:Object,
      }
    },
    data() {
      return {
        userid: localStorage.getItem("userid"),
        value:{},
        currentWorkday: null,
      };
    },
    created() {
      this.value = this.info;
      this.httpUtil.sysDate().then(res=>{
        if (res) {
          this.currentWorkday = res;
        }
      });
    },
    methods: {
      confirmHandler(params){
        this.value.prodCode="0001";
        if(this.value.prodCode==null || this.value.prodCode==''){
            Tools.alert("未录入产品代码","danger");
            return false;
        }
        return params;
      },
      change(val) {
        console.log(val);
      },
      check(link) {
        if(link.length < 8) {
          return '插入失败,链接最小长度为8'
        } else {
          return true
        }
      },
      blur() {
        console.log("失焦啦")
      },
      focus() {
        console.log("聚焦啦")
      }
    },
    computed: {
    },
    mounted() {
      console.log(">>>>>>>>>>",this.value.prodCode);
    },
  };
</script>
