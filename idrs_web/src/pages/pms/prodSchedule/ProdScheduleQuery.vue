<template>
  <div>
    <k-form-search-customize data-target="scheduleListGrid" v-model="queryParam">
      <k-form-item label="产品代码">
        <k-field-select v-model="prodSearchParam.prodCode" data-action="T8Dict.findTaProdInfos"
                        data-display-field="prodCode,prodName" data-value-field="prodCode"/>
      </k-form-item>
      <k-form-item label="产品名称">
        <k-field-text v-model="prodSearchParam.prodName" data-validate-type="text"/>
      </k-form-item>
      <k-form-item label="产品经理">
        <k-field-select v-model="prodSearchParam.prodManageId" :data-params="{roleId:'3'}"
                        data-action="User.getUserByRoleId2" data-display-field="username"
                        data-value-field="userid"/>
      </k-form-item>
      <k-form-item label="当前阶段">
        <k-field-select v-model="prodSearchParam.prodStatus"
                        data-dict="t8_prod_status"/>
      </k-form-item>
      <k-form-item label="申报登记时间">
        <k-field-date v-model="firstDateRange" data-type="daterange" :data-allowblank="true"/>
      </k-form-item>
      <k-form-item label="发行登记时间">
        <k-field-date v-model="secondDateRange" data-type="daterange" :data-allowblank="true"/>
      </k-form-item>
      <k-form-item label="代码回收">
        <k-field-select v-model="prodSearchParam.isRecycleCode" data-dict="1yes0no" ></k-field-select>
      </k-form-item>
      <k-btn slot="button" class="btn-custom-primary" data-functype="EXPORT" data-target="scheduleListGrid"
             :data-export-name="'产品生命周期进度表'">
        <md-icon>cloud_download</md-icon>
        导出
      </k-btn>
    </k-form-search-customize>
    <k-grid ref="scheduleListGrid" data-action="ProdScheduleQuery.findProdScheduleList1" :data-operate-column="false">
      <k-grid-column data-align="center" data-header="产品经理" data-name="prodManageName" data-width="140"></k-grid-column>
      <k-grid-column data-align="center" data-header="产品名称" data-name="prodName" data-width="160"></k-grid-column>
      <k-grid-column data-align="center" data-header="产品当前阶段" data-name="prodStatus" data-width="140"></k-grid-column>
      <k-grid-column data-align="center" data-header="过创设会" data-name="meetDate"> data-width="140"</k-grid-column>
      <k-grid-column data-align="center" data-header="会后参数确认" data-name="meetParamConfirmDate"
                     data-width="140"></k-grid-column>
      <k-grid-column data-align="center" data-header="申报参数确认" data-name="applyParamConfirmDate"
                     data-width="140"></k-grid-column>
      <k-grid-column data-align="center" data-header="说明书法审" data-name="manualLawExamineDate" data-width="140"></k-grid-column>
      <k-grid-column data-align="center" data-header="说明书定稿" data-name="manualFinalizeDate" data-width="140"></k-grid-column>
      <k-grid-column data-align="center" data-header="报备材料法审" data-name="applyRegistDocLaw"
                     data-width="140"></k-grid-column>
      <k-grid-column data-align="center" data-header="完成申报登记" data-name="applyConfirm" data-width="140"></k-grid-column>
      <k-grid-column data-align="center" data-header="发行参数确认" data-name="issueParamConfirmDate"
                     data-width="140"></k-grid-column>
      <k-grid-column data-align="center" data-header="完成发行登记" data-name="issueConfirm" data-width="140"></k-grid-column>
      <k-grid-column data-align="center" data-header="完成产品参数设置" data-name="paramFinalize"
                     data-width="160"></k-grid-column>
    </k-grid>
  </div>
</template>

<script>
export default {
  name: "ProdScheduleQuery",
  data() {
    return {
      prodSearchParam: {},//查询参数
      firstDateRange: [],//一次报备时间段
      secondDateRange: [],//二次报备时间段
    }
  },
  computed: {
    queryParam() {
      return {
        'prodName': this.prodSearchParam.prodName,//产品名称
        'prodCode': this.prodSearchParam.prodCode,//产品代码
        'isRecycleCode': this.prodSearchParam.isRecycleCode,//是否代码回收
        'prodStatus': this.prodSearchParam.prodStatus,//当前阶段
        'prodManageId': this.prodSearchParam.prodManageId,//产品经理id
        'firstStartDate': this.firstDateRange ? this.firstDateRange[0] : null,
        'firstEndDate': this.firstDateRange ? this.firstDateRange[1] : null,
        'secondStartDate': this.secondDateRange ? this.secondDateRange[0] : null,
        'secondEndDate': this.secondDateRange ? this.secondDateRange[1] : null,
      }
    }
  },

}
</script>
