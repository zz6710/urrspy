<template>
  <div>
    <k-form ref="modeEditForm" :data-col="2">
      <k-form-item label="产品模型名称" data-input-width="560px">
        <k-field-text v-model="formData.prodModeName"  :data-allowblank="false"  :data-max-length="120"/>
      </k-form-item>
<!--      <k-form-item label="产品创建URL" data-input-width="560px">-->
<!--        <k-field-text v-model="formData.createUrl"  :data-allowblank="false"  :data-max-length="120"/>-->
<!--      </k-form-item>-->
      <k-form-item label="说明" data-input-width="560px">
        <k-field-text v-model="formData.remark" :data-max-length="120"/>
      </k-form-item>

      <k-form-footer data-align="center">
        <k-btn class="btn-custom-primary"  data-functype="SUBMIT" data-action="T8ProdModeInfo.updateT8ProdModeInfo" data-from="modeEditForm" :data-model="formData"
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
    modValue : {
      type:Object
    },
  },
  data() {
    return {
      formData :{
      },
    };
  },
  mounted() {
    this.formData = Object.assign({}, this.modValue);
  },
  methods: {
    afterSuccess(){
      this.$emit('reload-data');
    }
  },
  computed: {
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
