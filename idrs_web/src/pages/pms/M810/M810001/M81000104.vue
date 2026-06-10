<template>
  <k-form  ref="M81000104"  :data-col="2">
    <k-form-item label="产品代码" >
      <k-field-text  v-model="value.prodCode"  :data-allowblank="false" data-disabled></k-field-text>
    </k-form-item>
    <k-form-item label="产品名称" >
      <k-field-text  v-model="value.prodName"  :data-allowblank="false" data-disabled></k-field-text>
    </k-form-item>
    <k-form-item label="开户行名称" >
      <k-field-text  v-model="value.openAccountName"  :data-allowblank="false"  ></k-field-text>
    </k-form-item>
    <k-form-item label="资金账户名称" >
      <k-field-text  v-model="value.accountName"  :data-allowblank="false"  ></k-field-text>
    </k-form-item>
    <k-form-item label="资金账号" >
      <k-field-text  v-model="value.accountCode"  :data-allowblank="false"  ></k-field-text>
    </k-form-item>
    <k-form-item label="账号开户行行号" >
      <k-field-text  v-model="value.openAccountCode"  :data-allowblank="false"  ></k-field-text>
    </k-form-item>
    <k-form-item label="开户行所在省" >
      <k-field-select  v-model="value.openAccountProvince" data-action="District.findAllProvince" data-display-field="districtName"
                       data-value-field="id" @data-on-change="provinceChange"  :data-allowblank="false"></k-field-select>
    </k-form-item>
    <k-form-item label="开户行所在城市" >
      <k-field-select  v-model="value.openAccountCity" :data-data="citys" data-display-field="districtName" data-value-field="id" :data-allowblank="false"  ></k-field-select>
    </k-form-item>
    <k-form-item label="券商名称" >
      <k-field-select  v-model="value.brokerName"  :data-allowblank="true"></k-field-select>
    </k-form-item>
    <k-form-item label="深交所证券账户" >
      <k-field-text  v-model="value.szmarketCode"  :data-allowblank="false"  ></k-field-text>
    </k-form-item>
    <k-form-item label="上交所证券账户" >
      <k-field-text  v-model="value.shmarketCode"  :data-allowblank="false"  ></k-field-text>
    </k-form-item>
    <k-form-item label="备注" >
      <k-field-text  v-model="value.market"  :data-allowblank="true"  ></k-field-text>
    </k-form-item>

    <k-form-footer data-align="center">
      <k-btn data-target="queryTable" data-functype="SUBMIT" class="btn-custom-primary" :data-model="value" data-action="T810001.addAccountInfo" data-from="M81000104">
        <i class="icon-confirm"></i>确定
      </k-btn>
      <k-btn  data-functype="CLOSE" class="btn-custom-plain">
        <i class="icon-cancel"></i>取消
      </k-btn>
    </k-form-footer>
  </k-form>
</template>

<script>
  import Tools from '@/utils/tools.js';
  export default {
    props:{
      updSuccess:Function
    },
    data(){
      return {
        citys: {}
      }
    },
    computed: {
      value() {
        return this.$attrs.value;
      }
    },
    methods:{
      provinceChange(value){
        //this.$attrs.value.openAccountCity = "";
        //通过销售商代码查询销售商信息，并赋值
        this.httpUtil.comnQuery({
          action:"District.findCityByPId",
          params:{pid:value}
        }).then(data => {
          this.citys = data.rows;
        }).catch({
        })
      }
    }
  }
</script>

<style scoped>

</style>
