<template>
  <div>
    <k-form ref="t8ProdRiskConfigForm" :data-col="2">
      <k-form-item label="序号">
        <k-field-text v-model="formData.numberId" :data-allowblank="false" @data-on-blur="isProdRiskId"/>
      </k-form-item>
      <k-form-item label="风险名称">
        <k-field-text v-model="formData.prodRisk" :data-allowblank="false" @data-on-blur="isProdRiskName"/>
      </k-form-item>
      <k-form-item label="风险描述"  :data-col="2">
        <k-field-text v-model="formData.prodRiskDesc" :data-max-length="2000" inputType="textarea" :rows="5"
                      :data-allowblank="false"/>
      </k-form-item>
    </k-form>

  </div>
</template>

<script>
    export default {
        name: "T8ProdRiskConfigPopup",
      props: {
        formData:{},
      },
      data() {
        return {
        };
      },
      methods: {

        validateData() {
          return this.$refs.t8ProdRiskConfigForm.validate();
        },

        isProdRiskId(){


        },
        isProdRiskName(){

        }
      },

    }
</script>

<style scoped>

</style>
