<template>
  <div>
    <k-grid ref="t8ProdNetValueTaskGrid" data-action="T8ProdNetValueTask.findT8ProdNetValueTasks"
             data-operate-column="false" :data-autoload="false">
      <k-grid-column data-header="任务日期" data-name="taskDate"></k-grid-column>
      <k-grid-column data-header="任务名称" data-name="taskName"></k-grid-column>
      <k-grid-column data-header="任务描述" data-name="taskDesc" data-width="160"></k-grid-column>
      <k-grid-column data-header="任务状态" data-name="taskStatus" data-dict="t8_net_task_status"></k-grid-column>
      <k-grid-column data-header="创建日期" data-name="crtDate" data-type="date"></k-grid-column>
      <k-grid-column data-header="创建时间" data-name="crtTime" data-type="time"></k-grid-column>
      <k-grid-column data-header="确认人" data-name="confirmUserName"></k-grid-column>
      <k-grid-column data-header="确认日期" data-name="confirmDate"></k-grid-column>
      <k-grid-column data-header="确认时间" data-name="confirmTime"></k-grid-column>
    </k-grid>
    <k-grid ref="t8ProdNoticeGrid" data-action="T8ProdNetValueNotice.findT8ProdNetValueNotices"
            data-operate-column="false" :data-autoload="false">
      <k-grid-column data-header="产品代码" data-name="prodCode"></k-grid-column>
      <k-grid-column data-header="产品名称" data-name="prodName"></k-grid-column>
      <k-grid-column data-header="披露日期" data-name="disclosureDate"></k-grid-column>
      <k-grid-column data-header="净值日期" data-name="netvalDate"></k-grid-column>
      <k-grid-column data-header="产品总净值" data-name="totalNet"></k-grid-column>
      <k-grid-column data-header="产品总份额" data-name="totalVol"></k-grid-column>
      <k-grid-column data-header="当日收益" data-name="navProfit"></k-grid-column>
      <k-grid-column data-header="单位净值" data-name="nav"></k-grid-column>
      <k-grid-column data-header="累计净值" data-name="totalNav"></k-grid-column>
      <k-grid-column data-header="单位万份收益" data-name="tenThousandIncomeAmt"></k-grid-column>
      <k-grid-column data-header="近七日年化收益率" data-name="sevenDaysIncomeRate"></k-grid-column>
<!--      <k-grid-column data-header="状态" data-name="status" data-dict="t8_net_notice_status"></k-grid-column>-->
    </k-grid>
  </div>
</template>

<script>
import Tools from "@/utils/tools";

export default {
  name: "DisplayDisclosureNetValueNotice",
  props:{
    formData:{

    },
    /*truteeApproval:{},*/
  },data() {
    return {
      id:"",
    }
  },methods:{
    //下载定期报告
    downloadXPGGTempVersion(params){
      //var filieName = params.ruleDocName;
      var filieName = params.noticeTitle+".docx";

      this.httpUtil.download({
        url: "/download/server/PmsApp/print/downloadXPGGTempVersion.json",
        params: params,
        callback: response => {
          console.log(response)
          Tools.alert("下载完成");
        }
      }, filieName);
      /*Tools.confirm(() => {
        },
        "该模板为业务人员手动上传,内容可能与补录界面信息有差别,是否下载?"
      )*/
    },
  },
  watch:{
    'formData.id' (val){
      if(val!=undefined&&val!=''){
        this.$refs.t8ProdNetValueTaskGrid.load({'id':val});
        this.$refs.t8ProdNoticeGrid.load({'t8DisclosureTaskId':val});
      }
    }
  },
  created() {
    /*this.$nextTick(()=>{
      console.log("formdata22222=:>>",this.formData);
      this.id=this.formData.id;
      console.log("this.id=:>>",this.id);

    })*/

  }
}
</script>

<style scoped>

</style>
