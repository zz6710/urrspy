<template>
  <k-form ref="editGroupInfoForm" :data-col="1">
    <k-form-item label="用户组名">
      <k-field-text v-model="value.groupName" :data-max-length="50"/>
    </k-form-item>
    <k-form-item label="分组类型">
      <k-field-select v-model="value.groupMod" data-dict="group_mod_type"/>
    </k-form-item>
    <k-form-item label="产品代码组">
      <k-field-select v-model="value.prodCodes" data-action="T8ProdInfo.findAll" data-display-field="prodName"
                      data-value-field="prodCode" :data-multiple="true" :dataAllowblank="value.groupMod=='2'?false:true"></k-field-select>
    </k-form-item>
    <k-form-item label="系列代码组">
      <k-field-select v-model="value.seriesCodes" data-action="T8ProdSeries.findT8ProdSeriesReturnCode" data-display-field="seriesName"
                      data-value-field="seriesCode"  :data-multiple="true" :dataAllowblank="value.groupMod=='1'?false:true"></k-field-select>
    </k-form-item>
    <!--    <k-form-item label="备注">
          <k-field-text v-model="value.remark" data-allowblank/>
        </k-form-item>-->
    <k-form-footer data-align="center">
      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8GroupInfo.updateT8GroupInfo"
             data-from="editGroupInfoForm" :data-model="value" data-target="groupInfoGrid">
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
//import ListWorkDay from "./ListWorkDay";

export default {
  name:"M81012edit",
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
