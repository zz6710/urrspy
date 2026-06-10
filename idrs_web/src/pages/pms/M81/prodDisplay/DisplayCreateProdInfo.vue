<template>
  <k-form ref="prodCreateInfoFrom" :data-col="2">
    <k-form-item label="产品管理人">
      <k-field-text v-model="prodCreateInfo.managerCode" :data-default-value="'光大理财有限责任公司'" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="产品系列">
      <k-field-select v-model="prodCreateInfo.prodSeries" data-action="T8ProdInfo.getProdSeries"
                      data-display-field="seriesName" data-value-field="seriesCode" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="产品子系列">
      <k-field-select v-model="prodCreateInfo.prodSonSeries" :data-data="prodSeriesSon"
                      data-display-field="seriesName" data-value-field="seriesCode" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="产品形态">
      <k-field-select id="prod_mode" v-model="prodCreateInfo.prodMode" data-dict="t8_prod_create_type"
                      :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="组件模型名称">
      <k-field-select id="originality" v-model="prodCreateInfo.prodModeId"
                      data-action="T8ProdModeInfo.findT8ProdModeInfos"
                      data-display-field="prodModeName" data-value-field="prodMode" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="产品品牌">
      <k-field-select v-model="prodCreateInfo.prodBrand" data-dict="t8_prod_brand" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="产品代码">
      <k-field-text v-model="prodCreateInfo.prodCode" data-validate-type="codeLetterLine" :data-disabled="true"
                    :data-max-length="20"/>
    </k-form-item>
    <k-form-item label="产品名称">
      <k-field-text v-model="prodCreateInfo.prodName" :data-max-length="128" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="募集方式">
      <k-field-select v-model="prodCreateInfo.raiseType" data-dict="t8_raise_type" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="是否关联创意">
      <k-field-select v-model="prodCreateInfo.isOriginality" data-dict="t8_prod_isok" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="创意名称" v-if="prodCreateInfo.isOriginality == '1'">
      <k-field-select id="originality" v-model="prodCreateInfo.originalityId" data-action="T8ProdInfo.getOriginality"
                      data-display-field="originalityName" data-value-field="originalityId" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="是否份额分类">
      <k-field-radio v-model="prodCreateInfo.isShareSort" data-dict="1yes0no" :data-disabled="true"/>
    </k-form-item>

  </k-form>
</template>

<script>
    export default {
        name: "DisplayCreateProdInfo",
        props: {
            prodCreateInfo: {},
        },
        data() {
            return {
                prodSeriesSon: [],
            };
        },
        methods: {

        },
        watch: {
            'prodCreateInfo.prodSeries': {
                handler(newVal, oldVal) {
                    this.httpUtil.comnQuery({
                        action:"T8Dict.getProdSonSeries",
                        params:{
                            id: this.prodCreateInfo.prodSeries,
                        }
                    }).then(data => {
                        console.log(">>>>>>",data.rows)
                        this.prodSeriesSon = data.rows
                    });
                },
                immediate: true
            }
        }

    }
</script>

<style scoped>

</style>
