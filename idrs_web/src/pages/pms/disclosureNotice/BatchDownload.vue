<template>
  <div>
    <div>
      <k-grid ref="disclosureNoticeGrid" @data-row-select="selectRow"
               data-fixed="right"
              :dataData="infoData" data-operate-width="200px">
        <k-grid-column data-align="left" data-header="公告id" data-name="id" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="公告版本id" data-name="noticeVersionId"
                       :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="信披类型" data-name="disclosureType" data-dict="xp_doc_type"
                       data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="信披子类型" data-name="disclosureSonType" data-dict="xp_son_type"
                       data-width="130"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品代码" data-name="prodCode" data-width="110"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品名称" data-name="prodName" data-width="230"></k-grid-column>
        <k-grid-column data-align="left" data-header="模板名称" data-name="modName" data-width="100"
                       :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="公告标题" data-name="noticeTitle"
                       data-width="600"></k-grid-column>
        <k-grid-column data-align="left" data-header="模板版本号" data-name="modVersion" data-width="60"
                       :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="基准日期" data-name="prodBaseDate" data-type="date"
                       data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="公告版本号" data-name="disclosureVersion"
                       data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="公告状态" data-name="disclosureStatus"
                       data-dict="xp_disclosure_notice_status" data-width="80"></k-grid-column>
        <k-grid-column data-align="left" data-header="复核状态" data-name="reviewStatus"
                       data-dict="xp_disclosure_check_status" data-width="80"></k-grid-column>
        <k-grid-column data-align="left" data-header="计划发布日期" data-name="planFbDate" data-type="date"
                       data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="实际发布日期" data-name="realFbDate" data-type="date"
                       data-width="100"></k-grid-column>

        <!-- 下面为隐藏字段   -->
        <k-grid-column data-align="center" data-header="模板版本文件格式" data-name="suffix"
                       :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="信披规则id" data-name="t8DisclosureRuleId"
                       :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="信披模板id" data-name="t8DisclosureModId"
                       :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="信披模板版本id" data-name="t8DisclosureModVersionId"
                       :data-hidden="true"></k-grid-column>
        <!--        <k-grid-column data-align="center" data-header="报告类型" data-name="reportType" :data-hidden="true"></k-grid-column>-->
        <k-grid-column data-align="center" data-header="信披任务id" data-name="taskId"
                       :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="计划审批日期" data-name="planSpDate" data-type="date"
                       :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="计划补录日期" data-name="planBlDate" data-type="date"
                       :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="实际补录日期" data-name="realBlDate" data-type="date"
                       :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="实际审批日期" data-name="realSpDate" data-type="date"
                       :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="文件存放路径" data-name="filePath"
                       :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="文件名" data-name="fileName"
                       :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="文件发送路径" data-name="crtPath"
                       :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="创建时间" data-name="crtTime" data-type="time"
                       :data-hidden="true"></k-grid-column>
        <!--        <k-grid-column data-align="center" data-header="创建人" data-name="crtUserId" :data-hidden="true"></k-grid-column>-->
        <k-grid-column data-align="center" data-header="创建人名称" data-name="crtUserName"
                       :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="更新日期" data-name="updDate" data-type="date"
                       :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="更新时间" data-name="updTime" data-type="time"
                       :data-hidden="true"></k-grid-column>
        <!--        <k-grid-column data-align="center" data-header="更新人" data-name="updUserId" :data-hidden="true"></k-grid-column>-->
        <k-grid-column data-align="center" data-header="更新人名称" data-name="updUserName"
                       :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="公告id" data-name="t8DisclosureNoticeId"
                       :data-hidden="true"></k-grid-column>

        <template slot="operate" slot-scope="props">
          <k-btn class="btn-custom-text specialClass" :data-download-name="props.row.row.fileName"
                 data-functype="DOWNLOAD" data-size="small"
                 data-url="/download/server/PmsApp/M8DisclosureManual/download.json" v-model="props.row.row">
            <md-icon>cloud_download</md-icon>下载
          </k-btn>
          <k-btn class="btn-custom-text specialClass" :data-model="props.row.row" data-size="small"
                 @click="previewXPGGTempVersion(props.row.row)" data-functype="POPUP">
            <i class="icon-search"/>预览
          </k-btn>
        </template>

      </k-grid>
    </div>

  </div>
</template>

<script>
import KFieldCheckboxParam from "@/pages/design/components/param/KFieldCheckboxParam.vue";
// import Tools from "@/utils/tools";
// import {clone} from "lodash";
// import moment from "moment"

export default {
  name: "batchDownload",
  components: {KFieldCheckboxParam},
  props: {

    submitData: {},
    auditMethod: '',
  },
  data() {
    return {
      selectRowData: {},

      infoData:{
        rows:[],
      },
      formData: {
        prodCode: '',
        prodName: '',
        prodName1: '',
        feeJson: '',
        crtUser: '',
        startEstablishDate: '',
        sendEmail: '',
        note: '',
        fileList:'',
        t8DisclosureNoticeId:''
      },
    };
  },

  computed: {},
  created() {
    console.log("this.submitData=:>>>",this.submitData);
    let tempData = JSON.parse(this.submitData.list);
    console.log("tempData=:>>>",tempData);
     this.infoData.rows = tempData;
     console.log(this.infoData.rows);
  },
  methods: {
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row);
      this.formData = Object.assign({}, row);
    },
    download() {
      let selectedList = this.submitData.selectedList;
      console.log(selectedList);
      // let list = selectedList.map(item => ({
      //   t8DisclosureNoticeId: item.id,
      //   id: item.noticeVersionId,
      //   fileName: item.fileName
      // }))
      //
      // if (list.length < 1 && this.prodSearchParam.prodCode == '' && this.prodSearchParam.publishStatus == '' && this.prodSearchParam.stage == '' && this.prodSearchParam.disclosureType == '') {
      //
      //   Tools.alert("未输入查询条件或未选择公告", "danger");
      //   this.$refs.disclosureNoticeGrid.load(this.queryParam);
      //   this.$refs.batchDownloadButton.setIconStyle(1, []);
      //   return false;
      // }
      // var fileName = moment().format('YYYYMMDD');
      // this.httpUtil.download({
      //   url: "/download/server/PmsApp/notice/batchDownLoad.json",
      //   params: {
      //     list: JSON.stringify(list),
      //     "prodSearchParam": JSON.stringify(this.prodSearchParam)
      //   },
      //   callback: response => {
      //     Tools.alert("下载完成");
      //     this.$refs.disclosureNoticeGrid.load(this.queryParam);
      //     this.$refs.batchDownloadButton.setIconStyle(1, [])
      //   }
      // }, fileName).then(res=>{
      //   if (res.success){
      //     this.$emit('close')
      //   }
      // })
      // this.httpUtil.ajax({
      //   url: 'server/json/RptApp/audit/status.json',
      //   params: {
      //     tableId: tableId,
      //     startDate: startDate,
      //     endDate: endDate,
      //     auditStatus: auditStatus
      //   }
      // }).then(res => {
      //   if (res.success) {
      //     this.$emit('close');
      //   }
      // })

    },
    //预览定期报告
    previewXPGGTempVersion(rows) {

      let fileName = this.formData.title + '.pdf';
      //rows.noticeTitle=this.formData.title;
      if(rows.noticeTitle){
        console.log("preview_rows=:>>>>",rows);
      }else{
        console.log("preview_rows2222222222222=:>>>>",rows);
        rows.noticeTitle=this.formData.title;
      }
      rows.id=this.formData.t8DisclosureNoticeId;
      if(rows.t8DisclosureNoticeId){
        console.log("preview_rows=:>>>>",rows);
      }else{
        console.log("preview_rows2222222222222=:>>>>",rows);
        rows.t8DisclosureNoticeId=this.formData.t8DisclosureNoticeId;
      }
      //rows.t8DisclosureNoticeId=this.formData.t8DisclosureNoticeId;
      rows.disclosureType='11';
      console.log("rows=:>>",rows);
      this.httpUtil.comnQuery({
        action: "DisclosureNoticeProcess.findMaxVersionById",
        params: {t8DisclosureNoticeId: rows.t8DisclosureNoticeId}
      }).then(data => {
        rows.fileName = data.returndata.fileName;
        rows.filePath = data.returndata.filePath;
        rows.docType = data.returndata.docType;
        rows.version = data.returndata.maxVersion;
        rows.maxVersionId = data.returndata.maxVersionId;
        this.httpUtil.preview({
          url: "/download/server/PmsApp/print/previewXPGGByNoticeVersion.json",
          params: rows
        }, fileName);
      }).catch({})
    },



  }
};
</script>
