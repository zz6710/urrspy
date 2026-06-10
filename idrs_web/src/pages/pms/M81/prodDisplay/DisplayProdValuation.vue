<template>
  <div>
    <k-form class="my-form " ref="addT8ProdValuationForm" dataLabelWidth="170px" dataInputWidth="300px">
      <k-form-item label="id" v-show="false">
        <k-field-text v-model="ProdValuation.id" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品代码" v-show="false">
        <k-field-text v-model="ProdValuation.prodCode" :data-disabled="true" />
      </k-form-item>
      <k-form-item label="产品主表id" v-show="false">
        <k-field-text v-model="ProdValuation.t8ProdInfoId" :data-disabled="true" />
      </k-form-item>
      <k-form-item label="估值目标日">
        <k-field-select v-model="ProdValuation.valuationRate" data-dict="t8_valuation_rate" :data-disabled="true" />
      </k-form-item>
			<k-form-item label="估值频率" >
            <k-field-select v-model="ProdValuation.valuationFrequency" data-dict="valuation_frequency" :data-disabled="true" />
	     	</k-form-item>
      <k-form-item label="估值方法" >
        <k-field-radio style="width:400px" v-model="ProdValuation.valuationMethod" data-dict="valuation_method"  :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品净值披露类型" v-if="this.prodMode=='4'">
            <k-field-select v-model="ProdValuation.ntDisclosureType " data-dict="ntDisclosureType"   :data-disabled="true"/>
      </k-form-item>
			<k-form-item label="估值方式说明" :data-col="2">
	        	<k-field-text v-model="ProdValuation.valuationTypeDesc" :data-max-length="8000" inputType="textarea" :data-disabled="true" :rows="5"/>
	     	</k-form-item>
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
      assemblyMenuType :'',
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
       prodMode: {
        type: String,
        default: ''
      },
    },
    data() {
      return {
        //ProdFee: {},
        selectRowData: {}
      };
    },
    created() {  console.log(this.prodMode)},
    methods: {
      validateData() {
        return this.$refs.addT8ProdValuationForm.validate();
      },
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.ProdValuation = Object.assign({}, row)
      }
    }
  };
</script>
