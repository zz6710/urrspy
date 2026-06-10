<template>
  <div class="formPanel" ref="formPanel" style="margin-left: 15%;">
    <div class="form-item prod-panel" >
      <div class="title" style="display: none">
      </div>
      <DisplayT8OnlineWordValue ref="addProdForm"  v-if = "isReady" v-model="formData" :formData="formData"/>
    </div>

  </div>
</template>

<script>
  import DisplayT8OnlineWordValue         from "../../../M81/prodDisplay/prodDocument/DisplayT8OnlineWordValue.vue"
  export default {
    name: "T8OnlineWordValueFlow",
    components: {
      DisplayT8OnlineWordValue
    },
    props:{
      taskInfo: {},
    },
    data() {
      return {
        formData:{},
        isReady:false,
      }
    },
    method:{
      isReady(){
        return this.isReady;
      },
    },
    created() {
      this.httpUtil
        .ajax({
          url: "/wf/formData/getSubmitParamsByProcessInstanceId.json",
          params: {processInstanceId: this.taskInfo.processInstanceId,_wfProcessInstanceId:this.taskInfo.processInstanceId}
        }).then(res => {
        if (res.data) {
          this.formData = JSON.parse(res.data.submitParams);
          this.$set(this.formData,'onlineEditDataParams',this.formData.onlineEditData);
          this.$nextTick(()=>{
            //查询修改前的值，做对比
            this.httpUtil.comnQuery({
              action: 'T8OnlineWordValue.getT8OnlineWordValueList',
              params: {
                t8ProdDocumentVersionId: this.formData.id,
              }
            }).then(data => {
              if (data.rows.length > 0) {
                this.viewUrl = data.rows[0].viewUrl;
                this.onlineEditData = data.rows;
                this.$set(this.formData,'oldOnlineEditDataParams',this.onlineEditData);
                this.isReady=true;
              }
            });
          });
        }
      });
    },
  }
</script>

<style lang="scss" scoped>
  @import "../../../../../styles/T81001.scss";
</style>
