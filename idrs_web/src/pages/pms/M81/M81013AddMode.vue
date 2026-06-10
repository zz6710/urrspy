<template>
  <div>
    <k-form ref="modeForm" :data-col="2">
      <k-form-item label="是否子系列" data-input-width="560px">
        <k-field-select v-model="formData.sonFlag"  :data-allowblank="false" data-dict="t8_prod_isok" :data-max-length="120"/>
      </k-form-item>
      <k-form-item label="母系列名称" data-input-width="560px" v-if="this.formData.sonFlag=='1'">
        <k-field-select v-model="formData.parentCode"  :data-allowblank="false" data-action="T8Dict.findSeriesInfos"
                      data-display-field="seriesName"
                      data-value-field="seriesCode" :data-max-length="120"/>
      </k-form-item>
      <k-form-item label="产品系列名称" data-input-width="560px">
        <k-field-text v-model="formData.seriesName"  :data-allowblank="false" :data-max-length="120"/>
      </k-form-item>
      <k-form-item label="产品品牌" data-input-width="560px">
        <k-field-select v-model="formData.prodBrand" data-dict="prod_brand" data-default-value="1" :data-allowblank="false" :data-max-length="120"/>
      </k-form-item>
      <k-form-item label="说明" data-input-width="560px">
        <k-field-text v-model="formData.market" :data-max-length="120" inputType="textarea" :rows="1"/>
      </k-form-item>

      <k-form-footer data-align="center">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdSeries.addT8ProdSeries" data-from="modeForm" :data-model="formData"
               :data-after-success="afterSuccess">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
        </k-btn>
        <k-btn class="btn-custom-plain"  data-functype="CLOSE">
          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
        </k-btn>
      </k-form-footer>
    </k-form>

  </div>
</template>

<script>

export default {
  props: {
    updSuccess: Function,
  },
  data() {
    return {
      formData :{
      },
    };
  },
  created() {
  },
  methods: {

    afterSuccess(){
      this.$emit('reload-data');
    }
  }

}
</script>

<style lang="scss" scoped>

.my-display{
  font-size: 17px;
}
.md-switch-label {
  font-size: 17px !important;
}
.md-switch{
  margin-top: 38px !important;
}
.my-item2-chips{
  margin: 15px 0 0px 0px;
  width: 356px !important;
}
.md-chips.md-field .md-chip{
  margin-top: 0px;
}
.my-table{
  margin-top: -50px;
}
</style>
