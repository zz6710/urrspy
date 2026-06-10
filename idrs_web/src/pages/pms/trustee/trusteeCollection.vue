<template>
  <div>
    <k-form ref="collectTrusteeForm" :data-col="2" isFormBodyScreen>
      <k-form-item label="托管行机构代码">
        <k-field-text v-model="formData.trusteeCode" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="托管行名称">
        <k-field-text v-model="formData.trusteeName" :data-disabled="true"/>
      </k-form-item>

      <k-form-item label="组织机构代码">
        <k-field-text v-model="formData.trusteeAcctCode" :data-disabled="true"/>
      </k-form-item>

      <k-form-item label="所在地区">
        <k-field-select v-model="formData.trusteeCountry" data-dict="tr_in_out_sign" :data-disabled="true" />
      </k-form-item>
      <k-form-item label="托管行性质">
        <k-field-select v-model="formData.trusteeProperty" data-dict="trustee_property" :data-disabled="true" />
      </k-form-item>
    </k-form>
  </div>
</template>

<script>
import inputNumberController from "@/pages/pms/basePublish/DisclosureRule/input-number-controller";
import Tools from "@/utils/tools";
export default {
  components: {inputNumberController},
  props: {
    parentFormData: {
      type: Object,
      default: () => {
        return {
        }
      }
    }
  },
  data() {
    return {
      selectRowData: {},
      formData: {
      },
    }
  },

  watch: {

  },
  created() {
    this.formData = { ...this.parentFormData };
  },
  methods:{

    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
    },
  },
};
</script>
