<template>
  <k-form ref="editWorkdayProgramForm" :data-col="1">
    <k-form-item label="方案编号">
      <k-field-text v-model="value.pgmno" data-disabled data-clearable="false" :data-max-length="32"/>
    </k-form-item>
    <k-form-item label="方案名称">
      <k-field-text v-model="value.pgmname" :data-allowblank="false" :data-max-length="250"/>
    </k-form-item>
    <k-form-item label="方案类型">
      <k-field-select v-model="value.pgmtype" data-dict="pgmtype" :data-allowblank="false" data-disabled/>
    </k-form-item>
    <k-form-item label="备注">
      <k-field-text v-model="value.remark" data-allowblank :data-max-length="250"/>
    </k-form-item>
    <k-form-footer data-align="center">
      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="WorkdayProgram.update"
             data-from="editWorkdayProgramForm" :data-model="value" data-target="workdayProgramGrid">
        <md-icon md-src="/static/svg/confirm.svg"/>确定
      </k-btn>
      <k-btn class="btn-custom-plain" data-functype="CLOSE">
        <md-icon md-src="/static/svg/cancel.svg"/>取消
      </k-btn>
    </k-form-footer>
  </k-form>
</template>

<script>
  import kayak from '@/frame/kayak.js'
  import ListWorkDay from "./ListWorkDay";

  export default {
    props: {
      updSuccess: Function
    },
    data() {
      return {
      };
    },
    methods: {
    },
    computed: {
      value() {
        return this.$attrs.value
      }
    }
  };
</script>
