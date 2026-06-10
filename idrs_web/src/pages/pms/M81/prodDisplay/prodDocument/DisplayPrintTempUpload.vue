<template>
  <k-form ref="addForm" data-ui="element">
    <k-form-item label="文档类型">
      <k-field-select v-model="formData.docType" :data-allowblank="false" data-action="T8Dict.t8PrintDoc"
                      data-display-field="itemval"  data-value-field="itemkey" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="模板类型">
      <k-field-select v-model="formData.tempType" :data-allowblank="false" :data-data="formData.addDocTypeDict"
                      data-value-field="value" data-display-field="value,text" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="销售商信息" key="distributor"
                 v-if="formData.tempType=='10003' || formData.tempType=='20003' || formData.tempType=='30003' ||
                      formData.tempType=='40003' || formData.tempType=='50003' || formData.tempType=='60003' || formData.tempType=='70003'">
      <k-field-select v-model="formData.distributorCode" :data-allowblank="true"  data-action="T8Dict.findTaDistributorInfos"
                      data-display-field="distributorName"  data-value-field="distributorCode"  :data-disabled="true" />
    </k-form-item>
    <k-form-item label="托管行信息" key="t8TruteeIn"
                 v-if="formData.tempType=='10002' || formData.tempType=='20002' || formData.tempType=='30002' ||
                      formData.tempType=='40002' || formData.tempType=='50002' || formData.tempType=='60002' || formData.tempType=='70002'">
      <k-field-select v-model="formData.t8TruteeInfoId" :data-allowblank="true" data-action="T82006.findTaCustodianBanks3"
                      data-display-field="truteeName"  data-value-field="id"  :data-disabled="true" />
    </k-form-item>
    <k-form-item label="所属会议" key="t8MeetCreate"
                 v-if="formData.tempType=='10001' || formData.tempType=='20001' || formData.tempType=='30001' || formData.tempType=='40001' ||
                      formData.tempType=='50001' || formData.tempType=='60001' || formData.tempType=='70001'">
      <k-field-select v-model="formData.t8MeetCreateId" :data-allowblank="true" data-action="MeetCreate.findMeetCreate"
                      data-display-field="meetName"  data-value-field="id"  :data-disabled="true" />
    </k-form-item>
    <k-form-item label="已存在风险数目" key="riskNum"
                     v-if="formData.tempType=='10001' ||

                      formData.tempType=='20001' ||
                      formData.tempType=='30001' ||
                      formData.tempType=='40001' ||
                      formData.tempType=='50001' ||
                      formData.tempType=='60001' ||
                      formData.tempType=='70001'||
                      formData.tempType=='10007' ||
                      formData.tempType=='20007' ||
                      formData.tempType=='30007' ||
                      formData.tempType=='40007' ||
                      formData.tempType=='50007' ||
                      formData.tempType=='60007' ||
                      formData.tempType=='70007'">
          <k-field-text v-model="formData.riskNum" :data-allowblank="false"  data-value-field="id"  :data-disabled="true" />
        </k-form-item>

    <k-form-item label="文档版本">
      <k-field-text v-model="formData.version" :data-allowblank="false" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="文档名称" data-input-width="590px">
      <k-field-text v-model="formData.tempName" :data-allowblank="false" :data-disabled="true" inputType="textarea" :rows="1"/>
    </k-form-item>
    <k-form-item label="备注" data-input-width="590px">
      <k-field-text v-model="formData.remark" :data-allowblank="false" :data-disabled="true" inputType="textarea" :rows="1"/>
    </k-form-item>


    <k-form-footer data-align="center">
      <k-btn class="btn-custom-primary" style="width: 110px" :data-download-name="formData.tempName"
             data-functype="DOWNLOAD" data-size="small" data-from="addForm"
             data-url="/download/server/PmsApp/print/downloadPrintTempVersion.json" :data-model="formData">
        <md-icon>cloud_download</md-icon>下载文档模板
      </k-btn>
      <k-btn class="btn-custom-primary" ref="previewRef" data-size="small" style="width: 130px"
             :data-handler="previewPrintTempVersion" v-model="formData">
        <md-icon>zoom_in</md-icon>预览文档模板信息
      </k-btn>
    </k-form-footer>
  </k-form>
</template>

<script>
    export default {
        name: "DisplayPrintTempUpload",
        props:{
          formData:{},
        },
      data() {
        return {
        }
       },

      methods:{
        previewPrintTempVersion(){
          this.httpUtil.comnQuery({
            action:'T8OnlineWordValue.getMaxT8OnlineWordValueByProcessInstanceId',
            params: {processInstanceId:this.formData.processInstanceId}
          }).then(data => {
            this.$nextTick(()=>{
              if (data != null && data.rows.length > 0){
                let url = data.rows[0].viewUrl;
                window.open(url,'_blank','width=1000,height=800,toolbars=yes,resizable=yes,scrollbars=yes,left=20,top=30');
              }
            })
          }).catch({

          })
        },
      }
    }
</script>

<style scoped>

</style>
