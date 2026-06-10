<template>
  <div>
    <k-form class="my-form " ref="addT8ProdValuationForm" dataLabelWidth="170px" dataInputWidth="300px">
      <k-form-item label="id" v-show="false">
        <k-field-text v-model="ProdValuation.id"/>
      </k-form-item>
      <k-form-item label="产品代码" v-show="false">
        <k-field-text v-model="ProdValuation.prodCode" />
      </k-form-item>
      <k-form-item label="产品主表id" v-show="false">
        <k-field-text v-model="ProdValuation.t8ProdInfoId" />
      </k-form-item>
      <k-form-item label="估值目标日">
        <k-field-select v-model="ProdValuation.valuationRate" data-dict="t8_valuation_rate" />
      </k-form-item>

			<k-form-item label="估值频率" >
            <k-field-select v-model="ProdValuation.valuationFrequency"  :data-data="options"/>
	     	</k-form-item>
      <k-form-item label="估值方法" >
        <k-field-radio style="width:400px"  v-model="ProdValuation.valuationMethod" data-dict="valuation_method"  :data-default-value="10000" />
      </k-form-item>

      <k-form-item label="产品净值披露类型" v-if="this.prodMode=='4'">
            <k-field-select v-model="ProdValuation.ntDisclosureType " data-dict="ntDisclosureType"  data-multiple="true" :data-allowblank="false"/>
      </k-form-item>
			<k-form-item label="估值方法说明" :data-col="2">
	        	<k-field-text v-model="ProdValuation.valuationTypeDesc"  inputType="textarea" :rows="5" :data-max-length="8000"  @data-on-change="handleStrvaluationTypeDesc"/>
	     	</k-form-item>
	      	<k-form-footer data-align="center"  v-show="menuName == 'M81007'">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdInvest.addT8ProdValuation" data-from="addT8ProdValuationForm"
		               :data-model="ProdValuation" data-target="t8ProdInvestGrid" :data-handler="addHandler"  :data-after-success="passDataSuccess">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>保存
		        </k-btn>
	      	</k-form-footer>
    	</k-form>
  </div>
</template>

<script>
  export default {
    computed: {},
    model: {
      prop: 'ProdValuation',
      event: 'input'
    },
    props:{
      menuName :'',
      ProdValuation: {

      },
      prodCode: {
        type: String,
        default: ''
      },
      t8ProdInfoId: {
        type: String,
        default: ''
      },
      valuationFrequency: {
        type: String,
        default: ''
      },
      prodMode: {
        type: String,
        default: ''
      },
    },
    data() {
      return {
        //ProdFee: {},
        options: [],

      };
    },

    created() {

      this.httpUtil.comnQuery({
          action: 'DictItem.find',
          params: {
            dict: 'valuation_frequency'
          }
        }).then(data => {

          for (let model in  data.rows) {

              if( data.rows[model].itemkey=='6'){
                data.rows[model].itemval='每个（现金管理产品使用）'
              }

          }
          this.options=data.rows;

        });},
    methods: {
      passDataSuccess(){
        this.$emit('isShowButton', '1')
      },
      addHandler(val){
        this.$set(val,'assemblyMenuType','prodValuation');
      },
      validateData() {
        return this.$refs.addT8ProdValuationForm.validate();
      },
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.ProdValuation = Object.assign({}, row)
      },
       handleStrvaluationTypeDesc(val){

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
        this.ProdValuation.valuationTypeDesc = str;
       }
      }
    }
  };
</script>
