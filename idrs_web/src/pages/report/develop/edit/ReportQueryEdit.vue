<template>
  <div>
    <k-form ref="editReportQueryForm" :data-col="2" isFormBodyScreen >
      <k-form-item label="ID" v-show="false">
        <k-field-text v-model="formData.id"/>
      </k-form-item>
      <k-form-item label="ID" v-show="false">
        <k-field-text v-model="formData.cssId" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="name">
        <k-field-text v-model="formData.cssName" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="type">
        <k-field-select v-model="formData.cssType" data-dict="input_type" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="class">
        <k-field-text v-model="formData.cssClass" :data-allowblank="false" :data-disabled="true"
                      :data-clearable="false"/>
      </k-form-item>
      <k-form-item label="label">
        <k-field-text v-model="formData.labelDiscrible" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="data-min-length">
        <k-field-text v-model="formData.dataMinLength"/>
      </k-form-item>
      <k-form-item label="data-max-length">
        <k-field-text v-model="formData.dataMaxLength"/>
      </k-form-item>
      <k-form-item label="data-data">
        <k-field-text v-model="formData.dataData"/>
      </k-form-item>
      <k-form-item label="data-exeid">
        <k-field-text v-model="formData.dataExeid"/>
      </k-form-item>
      <k-form-item label="data-dict">
        <k-field-text v-model="formData.dataDict"/>
      </k-form-item>
      <k-form-item label="data-action">
        <k-field-text v-model="formData.dataAction"/>
      </k-form-item>
      <k-form-item label="data-sqlinfo" :dataCol=2>
        <k-field-text v-model="formData.dataSqlinfo" inputType="textarea"/>
      </k-form-item>
      <k-form-item label="data-value-field">
        <k-field-text v-model="formData.dataValueField"/>
      </k-form-item>
      <k-form-item label="data-display-field">
        <k-field-text v-model="formData.dataDisplayField"/>
      </k-form-item>
      <k-form-item label="data-workday">
        <k-field-text v-model="formData.dataWorkday"/>
      </k-form-item>
      <k-form-item label="data-date-format">
        <k-field-text v-model="formData.dataDateFormat"/>
      </k-form-item>
      <k-form-item label="data-value">
        <k-field-text v-model="formData.dataValue"/>
      </k-form-item>
      <k-form-item label="data-allowblank">
        <k-field-text v-model="formData.dataAllowblank"/>
      </k-form-item>
      <k-form-item label="data-min-value">
        <k-field-text v-model="formData.dataMinValue"/>
      </k-form-item>
      <k-form-item label="data-max-value">
        <k-field-text v-model="formData.dataMaxValue"/>
      </k-form-item>
      <k-form-item label="ligroupid">
        <k-field-text v-model="formData.ligroupid"/>
      </k-form-item>
      <k-form-item label="data-validate-type">
        <k-field-text v-model="formData.validatetype"/>
      </k-form-item>
      <k-form-item label="data-on-change">
        <k-field-text v-model="formData.dataOnChange"/>
      </k-form-item>
      <k-form-item label="data-list">
        <k-field-text v-model="formData.datalist"/>
      </k-form-item>
      <k-form-footer data-align="center">
        <k-btn class="btn-custom-primary" @click.native="submitHandler" :data-model="formData">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">
          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
        </k-btn>
      </k-form-footer>
    </k-form>
  </div>
</template>

<script>
export default {
  name: "ReportQueryEdit",
  props: {
    info : {
      type:Object,
    },

    disabledVal: {
      type:Boolean,
    },
  },



  data() {
    return {
      formData : {},

    };
  },
  computed : {

  },

  methods: {

    submitHandler() {
      this.$emit('editGrid',this.formData);
    },


  },
  created() {
    this.formData = this.info;
  },

  watch: {
    "formData.cssType": {
      handler(val) {
        this.formData.cssClass = "k-field-" + val
      }
    }
  },


}
</script>

<style scoped>

</style>
