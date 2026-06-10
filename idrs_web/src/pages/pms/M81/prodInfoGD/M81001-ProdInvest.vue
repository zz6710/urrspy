<template>
  <div>
    <k-form class="my-form " ref="addT8ProdInvestForm" :data-col="2" dataLabelWidth="170px" dataInputWidth="300px">
      <k-form-item label="产品代码" v-show="false">
        <k-field-text v-model="ProdInvest.prodCode"/>
      </k-form-item>
      <k-form-item label="产品主表id" v-show="false">
        <k-field-text v-model="ProdInvest.t8ProdInfoId"/>
      </k-form-item>
      <k-form-item label="建仓期单位">
        <k-field-select v-model="ProdInvest.accumCompany" data-dict="t8_prod_open_type"/>
      </k-form-item>
      <k-form-item label="建仓期">
        <k-field-text v-model="ProdInvest.accumTerm" :data-max-length="3" data-digits="0" data-integer-length="3"
                      data-validate-type="number" data-type="number"/>
      </k-form-item>

      <k-form-item label="投资渠道">
        <k-field-checkbox v-model="ProdInvest.investmentType" data-dict="t8_investment_type"
                          @data-on-change="showProdInvest"/>
      </k-form-item>
      <k-form-item label="是否需新增证券公司" v-if="investment.bond">
        <k-field-select v-model="ProdInvest.isAddBond" data-dict="t8_prod_isok" :data-allowblank="!investment.bond"/>
      </k-form-item>
      <k-form-item label="是否需新增委外机构" v-if="investment.outsource">
        <k-field-select v-model="ProdInvest.isAddOutsource" data-dict="t8_prod_isok"
                        :data-allowblank="!investment.outsource"/>
      </k-form-item>
      <k-form-item label="是否需新增期货公司" v-if="investment.future">
        <k-field-select v-model="ProdInvest.idAddFuture" data-dict="t8_prod_isok"
                        :data-allowblank="!investment.future"/>
      </k-form-item>
      <k-form-item label="委外专户估值频率">
        <k-field-select v-model="ProdInvest.outsourceFreq" data-dict="t8_valuation_freq" data-default-value="1"/>
      </k-form-item>
      <k-form-item label="非标产品估值频率">
        <k-field-select v-model="ProdInvest.nonstandardFreq" data-dict="t8_valuation_freq" data-default-value="1"/>
      </k-form-item>
      <k-form-item label="委外投资的业绩报酬计提时点匹配">
        <k-field-select v-model="ProdInvest.isMatchPointtime" data-dict="t8_prod_isok"/>
      </k-form-item>
      <k-form-item label="投资目标说明" :data-col="2">
        <k-field-text v-model="ProdInvest.investTargetDesc" inputType="textarea" :rows="5" :data-max-length="8000" @data-on-change="handleStrInvestTargetDesc"/>
      </k-form-item>
      <k-form-item label="投资范围说明" :data-col="2">
        <k-field-text v-model="ProdInvest.investRangeDesc" inputType="textarea" :rows="5" :data-max-length="8000" @data-on-change="handleStrInvestRangeDesc"/>
      </k-form-item>
      <k-form-item label="投资比例说明" :data-col="2">
        <k-field-text v-model="ProdInvest.investScaleDesc" inputType="textarea" :rows="5" :data-max-length="8000"  @data-on-change="handleStrInvestScaleDesc"/>
      </k-form-item>
      <k-form-item label="投资策略说明" :data-col="2">
        <k-field-text v-model="ProdInvest.investPolicyDesc" inputType="textarea" :rows="5" :data-max-length="8000"  @data-on-change="handleStrInvestPolicyDesc"/>
      </k-form-item>
      <k-form-item label="投资限制说明" :data-col="2">
        <k-field-text v-model="ProdInvest.investLimitDesc" inputType="textarea" :rows="5" :data-max-length="8000"  @data-on-change="handleStrInvestLimitDesc"/>
      </k-form-item>
      <k-form-item label="挂钩标的说明" :data-col="2">
        <k-field-text v-model="ProdInvest.hookTargetDesc" inputType="textarea" :rows="5" :data-max-length="8000" />
      </k-form-item>
      <k-form-item label="产品期权结构说明" :data-col="2">
        <k-field-text v-model="ProdInvest.optionStructDesc" inputType="textarea" :rows="5" :data-max-length="8000"/>
      </k-form-item>
      <k-form-item label="资金用途" :data-col="2">
        <k-field-text v-model="ProdInvest.purposeUse" inputType="textarea" :rows="5" :data-max-length="8000"/>
      </k-form-item>
      <k-form-item label="交易安排" :data-col="2">
        <k-field-text v-model="ProdInvest.transactionArrangement" inputType="textarea" :rows="5" :data-max-length="8000"/>
      </k-form-item>
      <k-form-item label="产品风险" :data-col="2">
        <k-field-text v-model="ProdInvest.productRisk" inputType="textarea" :rows="5" :data-max-length="8000"/>
      </k-form-item>
      <k-form-item label="相关当事人介绍" :data-col="2">
        <k-field-text v-model="ProdInvest.partyIntroduction" inputType="textarea" :rows="5" :data-max-length="8000"/>
      </k-form-item>
      <k-form-item label="产品其他风险" :data-col="2">
        <k-field-select v-model="ProdInvest.prodRisk" data-action="T8ProdRiskConfig.findProdRiskConfig"
                        data-multiple="true" inputType="textarea" :rows="1" @data-on-change="setRiskItems"
                        data-display-field="prodRisk" data-value-field="prodRisk" />
      </k-form-item>
    </k-form>
<!--    //20210410 axin 风险-->
    <k-form  ref="addForm2" v-for="(item,index) in riskItems" :key="index"
             :data-col="6" data-input-width="300px" data-label-width="170px" data-total-width="1118px">

      <k-form-item label="序号" v-show="false">
        <k-field-text v-model="item.numberId" :data-allowblank="false" @data-on-blur="isProdRiskId(item.numberId,index)" />
      </k-form-item>
      <k-form-item label="风险名称">
        <k-field-text v-model="item.prodRisk" :data-allowblank="false" @data-on-blur="isProdRiskName(item.prodRisk,index)" :data-disabled="true" />
      </k-form-item>
      <k-form-item label="风险描述"  :data-col="2">
        <k-field-text v-model="item.prodRiskDesc"  :data-max-length="2000" inputType="textarea" :rows="1" />
      </k-form-item>
    </k-form>


    <k-form dataLabelWidth="170px" dataInputWidth="300px">
      <k-form-footer data-align="center" v-show="menuName == 'M81007'">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdInvest.addT8ProdInvest"
               data-from="addT8ProdInvestForm"
               :data-model="ProdInvest" data-target="t8ProdInvestGrid" :data-handler="addHandler"
               :data-after-success="passDataSuccess">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>保存
        </k-btn>
      </k-form-footer>
    </k-form>

    <!--    修改产品投资信息表弹出框   -->
  </div>
</template>

<script>
  import Tools from "@/utils/tools";
  export default {
    computed: {},
    model: {
      prop: 'T8ProdCalendar',
      event: 'input'
    },
    props: {
      menuName: '',
      ProdInvest: {},
      prodCode: {
        type: String,
        default: ''
      },
      t8ProdInfoId: {
        type: String,
        default: ''
      },
    },
    data() {
      return {
        investment: {
          bond: false,
          outsource: false,
          future: false,
        },
        //ProdInvest: {},
        selectRowData: {},
        riskItems:[],
        riskItemsStr:'',
      };
    },
    methods: {
      //20210410 axin 产品风险
      isProdRiskId(val,index){
        let riskItemsItem = JSON.parse(this.riskItemsStr);
        let flags = true;
        riskItemsItem.forEach(function(riskItem,i){
          if(riskItem.numberId == val && i != index){
            flags = false;
          }
        });
        if(!flags){
          this.riskItems[index].numberId = riskItemsItem[index].numberId;
          Tools.alert("序号已存在!","danger");
          return false;
        }else{
          this.riskItemsStr = JSON.stringify(this.riskItems);
        }
      },
      isProdRiskName(val,index){
        let riskItemsItem = JSON.parse(this.riskItemsStr);
        let flags = true;
        riskItemsItem.forEach(function(riskItem,i){
          if(riskItem.prodRisk == val && i != index){
            flags = false;
          }
        });
        if(!flags){
          this.riskItems[index].prodRisk = riskItemsItem[index].prodRisk;
          Tools.alert("风险名称已存在!","danger");
          return false;
        }else{
          this.riskItemsStr = JSON.stringify(this.riskItems);
        }
      },
      //20210410 axin 风险
      setRiskItems(val){
        this.httpUtil.comnQuery({
          action: 'T8ProdRiskConfig.findProdRiskConfig',
          params: {
            prodRisk : val,
          }
        }).then(data => {
          let prodRisks1 = [];
          for(let a in data.rows){
            prodRisks1.push(data.rows[a].prodRisk);
          }
          let prodRisks2 = [];
          for(let a in this.riskItems){
            prodRisks2.push(this.riskItems[a].prodRisk);
          }
          let s1 = new Set(prodRisks1);
          let s2 = new Set(prodRisks2);
          let prodRisksDif1 = prodRisks1.filter( x => !s2.has(x) );//新增
          let prodRisksDif2 = prodRisks2.filter( x => !s1.has(x) );//删除
          for(let a in prodRisksDif1){
            let prodRisk = prodRisksDif1[a];
            const index =  data.rows.findIndex((role) => role.prodRisk === prodRisk);
            this.$set(data.rows[index],'t8ProdInfoId',this.ProdInvest.t8ProdInfoId);
            this.riskItems.push(data.rows[index]);
          }
          for(let a in prodRisksDif2){
            let prodRisk = prodRisksDif2[a];
            const index =  this.riskItems.findIndex((role) => role.prodRisk === prodRisk);
            this.$delete(this.riskItems,index);
          }
          this.riskItemsStr = JSON.stringify(this.riskItems);
        });

      },
      showProdInvest(value) {
        if (value.indexOf("1") != -1) {
          this.investment.bond = true;
        } else {
          this.investment.bond = false;
        }

        if (value.indexOf("2") != -1) {
          this.investment.outsource = true;
        } else {
          this.investment.outsource = false;
        }

        if (value.indexOf("3") != -1) {
          this.investment.future = true;
        } else {
          this.investment.future = false;
        }
      },
      passDataSuccess() {
        this.$emit('isShowButton', '1')
      },
      addHandler(val) {
        this.$set(val, 'assemblyMenuType', 'prodInvest');
        val['t8ProdRiskConfig'] = JSON.stringify(this.riskItems);
      },
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.ProdInvest = Object.assign({}, row)
      },

      validateData() {
        return this.$refs.addT8ProdInvestForm.validate();
      },
      handleStrInvestTargetDesc (val){

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
      this.ProdInvest.investTargetDesc = str;
       }
      }
      ,
      handleStrInvestRangeDesc (val){

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
      this.ProdInvest.investRangeDesc = str;
       }
      },
      handleStrInvestScaleDesc (val){

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
      this.ProdInvest.investScaleDesc = str;
       }
      },
      handleStrInvestPolicyDesc (val){

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
      this.ProdInvest.investPolicyDesc = str;
       }
      },
      handleStrInvestLimitDesc (val){

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
      this.ProdInvest.investLimitDesc = str;
       }
      },
      handleStrHookTargetDesc (val){

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
      this.ProdInvest.hookTargetDesc = str;
       }
      }
    },

    created() {
      if (this.ProdInvest.investmentType != null) {
        this.showProdInvest(this.ProdInvest.investmentType);
      }
    },

    watch: {
      'ProdInvest.investmentType': function (val) {
        this.showProdInvest(val);
      },
    },
  };
</script>
