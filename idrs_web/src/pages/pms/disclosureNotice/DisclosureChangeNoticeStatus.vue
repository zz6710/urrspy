<template>
  <div>
    <div>
      <k-form-search-customize data-model-name="NoticeVersion" data-target="disclosureChangeNoticeStatusGrid" v-model="queryParam"  data-label-width="150px">
        <k-form-item label="信披类型">
          <k-field-select v-model="prodSearchParam.disclosureType" :data-data="DocTypeDict"  data-value-field="value" data-display-field="text" @data-on-change="changeXpType"  data-label-width="150px"></k-field-select>
        </k-form-item>

        <k-form-item label="信披子类型" v-if="prodSearchParam.disclosureType==='5'||prodSearchParam.disclosureType==='6'||prodSearchParam.disclosureType==='1'||prodSearchParam.disclosureType==='9'">
          <k-field-select v-model="prodSearchParam.disclosureSonType" :data-data="addDocTypeDict" data-value-field="value" data-display-field="text"  data-label-width="150px"></k-field-select>
        </k-form-item>

        <k-form-item label="产品代码">
          <k-field-select v-model="prodSearchParam.prodCode" data-action="T8ProdInfo.getProdInfosZG" data-value-field="prodCode" data-display-field="prodCode,prodName"  data-label-width="150px">
          </k-field-select>
        </k-form-item>

        <k-form-item label="产品名称">
          <k-field-text v-model="prodSearchParam.prodName" data-validate-type="text" :data-max-length="100"/>
        </k-form-item>
        <k-form-item label="变更日期" data-label-width="150px">
         <k-field-date v-model="queryParamDateRange" data-type="daterange"  data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd"/>
       </k-form-item>
        <k-btn slot="button" class="btn-custom-plain" data-target="disclosureChangeNoticeStatusGrid" :data-export-name="'信披状态变更记录导出'"
                             data-descript="导出" data-functype="EXPORT" data-size="small" data-url="DisclosureChangeNoticeStatus.findDisclosureChangeNoticeStatus" v-if="global.isShowAuthorityButton('DisclosureChangeNoticeStatus.exportChangeNoticeStatusControl')">
                 <md-icon>cloud_download</md-icon>
                 导出
       </k-btn>
      </k-form-search-customize>
    </div>
    <div>
         <k-grid ref="disclosureChangeNoticeStatusGrid" @data-row-select="selectRow" data-action="DisclosureChangeNoticeStatus.findDisclosureChangeNoticeStatus" :data-operate-column="false">
        <k-grid-column data-align="left" data-header="id" data-name="id" data-export="false" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="信披公告id" data-name="t8DisclosureNoticeId" data-export="false" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="信披类型" data-name="disclosureType" data-dict="xp_doc_type" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="信披子类型" data-name="disclosureSonType" data-dict="xp_son_type" data-width="160"></k-grid-column>
        <k-grid-column data-align="left" data-header="公告标题" data-name="noticeTitle" data-width="450"></k-grid-column>
        <k-grid-column data-align="left" data-header="信披版本号" data-name="noticeVersion" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品代码" data-name="prodCode" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品名称" data-name="prodName" data-width="200"></k-grid-column>
        <k-grid-column data-align="left" data-header="公告状态（变更前）" data-name="disclosureStatusAhead" data-dict="xp_disclosure_notice_status" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="公告状态（变更后）" data-name="disclosureStatusAfter" data-dict="xp_disclosure_notice_status" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="变更日期" data-name="crtDate"  data-width="100" ></k-grid-column>
        <k-grid-column data-align="left" data-header="变更人" data-name="crtUserName"  data-width="100" ></k-grid-column>
        <k-grid-column data-align="left" data-header="变更原因" data-name="changeReason"  data-width="250" ></k-grid-column>
      </k-grid>
    </div>


  </div>
</template>

<script>
  import Tools from "@/utils/tools";
  import moment from "moment"
  export default {
    name:"disclosureChangeNoticeStatus",
    data() {
      return {
        DocTypeDict: {},
        $kgrid: null,
        formData: {},
        selectRowData: {},
        addDocTypeDict:{},
        prodSearchParam: {},
        queryParamDateRange: [],
      };
    },
   computed: {
        queryParam () {
          return {
              'beginDate': this.queryParamDateRange ? this.queryParamDateRange[0] : null,
              'queryDate': this.queryParamDateRange ? this.queryParamDateRange[1] : null,
              'disclosureType': this.prodSearchParam.disclosureType,
             'disclosureSonType': this.prodSearchParam.disclosureSonType,
             'prodCode':this.prodSearchParam.prodCode,
             'prodName':this.prodSearchParam.prodName,
          }
        }
      },
    created() {
      // this.prodSearchParam = {};
      this.xpType();
    },
    methods: {
      renderDateTimeCreate(row) {
        return Tools.formatDateTime(row.crtDate, row.crtTime);
      },
      changeName(row){
       let name =row.noticeTitle;
       if(name!=null && name.indexOf(".")>0){
         let arrName = name.split(".")
         name=arrName[0]+row.version+"."+arrName[1];
         return name;
        }else{
         return "";
       }
      },
      //批量下载公告
      batchDownLoad(){
         const _this = this
         const list = _this.$kgrid.getSelected();
         if(list.length < 1 &&this.prodSearchParam.prodCode=='' &&this.prodSearchParam.disclosureType=='' &&this.prodSearchParam.disclosureSonType==''
             &&this.prodSearchParam.prodName=='' &&this.prodSearchParam.crtDate==''){
           Tools.alert("未输入查询条件或未选择公告","danger");
            this.$refs.disclosureNoticeVersionGrid.load();
            return false;
         }

         var fileName =  moment().format('YYYYMMDD');
         this.httpUtil.download({
           url: "/download/server/PmsApp/notice/batchDownLoad.json",
           params: {list: JSON.stringify(list), "prodSearchParam":JSON.stringify(this.prodSearchParam)},
           callback: response => {
             Tools.alert("下载完成");
             //this.$refs.disclosureNoticeVersionGrid.load();
             this.$refs.batchDownloadButton.setIconStyle(1, [])
           }
        }, fileName);
      },
      //单独下载某版本公告
      downloadXPGGTempVersion(row){
        let fileName =row.noticeTitle+row.suffix;//发布渠道前存放文件名称
        //根据文件名称和路径下载文件
        this.httpUtil.download({
          url: "/download/server/PmsApp/print/downloadXPGGHandVersion.json",
          params: row,
          callback: response => {
            Tools.alert("下载完成");
            this.$refs.trusteeBtn.setIconStyle(1, []);
          }
        }, fileName);
      },
      changeXpType(disclosureType) {
        this.$set(this.prodSearchParam, 'disclosureSonType', '');
        this.httpUtil.comnQuery({
          action: "DisclosureMod.getXPTypeByDocType",
          params: {disclosureType: disclosureType}
        }).then(data => {
          this.addDocTypeDict = data.rows;
        }).catch({})
      },
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      },
      xpType() {
        this.httpUtil.comnQuery({
          action: "DisclosureMod.getXPTypeInProd2",
          params: null
        }).then(data => {
          this.DocTypeDict = data.rows;
        }).catch({})
      },
    }
  };
</script>
