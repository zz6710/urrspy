<template>
  <div>
    <div>
      <k-form-search-customize data-model-name="NoticeVersion" data-target="disclosureNoticeVersionGrid" v-model="prodSearchParam">
        <k-form-item label="信披类型">
          <k-field-select v-model="prodSearchParam.disclosureType" :data-data="DocTypeDict"  data-value-field="value" data-display-field="text" @data-on-change="changeXpType"></k-field-select>
        </k-form-item>

        <k-form-item label="信披子类型" v-if="prodSearchParam.disclosureType==='5'||prodSearchParam.disclosureType==='6'||prodSearchParam.disclosureType==='1'||prodSearchParam.disclosureType==='9'">
          <k-field-select v-model="prodSearchParam.disclosureSonType" :data-data="addDocTypeDict" data-value-field="value" data-display-field="text"></k-field-select>
        </k-form-item>

        <k-form-item label="产品代码">
          <k-field-select v-model="prodSearchParam.prodCode"
                          data-action="T8ProdInfo.getProdInfosZG"
                          data-value-field="prodCode"
                          data-display-field="prodCode,prodName">
          </k-field-select>
        </k-form-item>

        <k-form-item label="产品名称">
          <k-field-text v-model="prodSearchParam.prodName" data-validate-type="text" :data-max-length="100"/>
        </k-form-item>

        <k-form-item label="创建日期">
          <k-field-date v-model="prodSearchParam.crtDate" data-type="date" data-date-format="yyyy-MM-dd"/>
        </k-form-item>

        <k-btn slot="button" class="btn-custom-primary" data-functype="SUBMIT" ref="batchDownloadButton"  :data-handler="batchDownLoad"
            v-if="global.isShowAuthorityButton('DisclosureNoticeVersion.downLoadsRightControl')" style="width: 90px;"  v-show="true">
          <md-icon>cloud_download</md-icon>批量下载</k-btn>
        <k-btn slot="button" class="md-rose" data-functype="EXPORT" data-target="disclosureNoticeVersionGrid"
               :data-export-name="'产品公告版本管理'" v-if="global.isShowAuthorityButton('DisclosureNoticeVersion.exportProdRuleRightControl')">
          <md-icon>cloud_download</md-icon>
          导出
        </k-btn>
      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="disclosureNoticeVersionGrid" @data-row-select="selectRow" data-action="DisclosureNoticeVersion.findDisclosureNoticeVersion1"
              data-fixed="right" @init="(grid)=>{this.$kgrid = grid}" :data-checkbox="true" data-checkbox-id="id" >
        <k-grid-column data-align="left" data-header="公告版本id" data-name="id" data-export="false" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="公告id" data-name="t8DisclosureNoticeId" data-export="false" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="信披类型" data-name="disclosureType" data-dict="xp_doc_type" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="信披子类型" data-name="disclosureSonType" data-dict="xp_son_type" data-width="120"></k-grid-column>
<!--        <k-grid-column data-align="left" data-header="模板名称" data-name="modName" data-width="240" data-export="false" :data-sortable="true" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="模板版本文件格式" data-name="suffix" data-export="false" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="模板版本号" data-name="modVersion" data-width="120" data-export="false" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="文件名" data-name="fileName" data-width="240" data-export="false" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="文件路径" data-name="filePath" data-width="120" data-export="false" :data-hidden="true"></k-grid-column>-->
        <k-grid-column data-align="left" data-header="公告标题" data-name="noticeTitle" data-width="500"></k-grid-column>
        <k-grid-column data-align="left" data-header="信披版本号" data-name="noticeVersion" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品代码 " data-name="prodCode" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品名称 " data-name="prodName" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="创建日期" data-name="crtDate"></k-grid-column>
        <k-grid-column data-align="left" data-header="创建人" data-name="crtUserName"></k-grid-column>

<!--        <k-grid-column data-align="center" data-header="创建人id" data-name="crtUserId" :data-hidden="true"></k-grid-column>-->
<!--        <k-grid-column data-align="left" data-header="创建时间" data-name="crtTime" data-export="false" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="更新日期" data-name="updDate" data-export="false" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="更新时间" data-name="updTime" data-export="false" :data-hidden="true"></k-grid-column>-->
<!--        <k-grid-column data-align="center" data-header="更新人" data-name="updUserId" :data-hidden="true"></k-grid-column>-->
<!--        <k-grid-column data-align="left" data-header="更新人名称" data-name="updUserName" data-export="false" :data-hidden="true"></k-grid-column>-->
        <template slot="operate" slot-scope="scope">
          <k-btn ref="trusteeBtn" class="md-info specialClass" data-functype="submit" :data-download-name="changeName(scope.row.row)"  data-descript="下载公告" data-size="mini"
                 style="min-width:40px;"  @click="downloadXPGGTempVersion(scope.row.row)" :data-model="formData"  v-if="global.isShowAuthorityButton('DisclosureNoticeVersion.downLoadRightControl')">
            下载
          </k-btn>
        </template>
      </k-grid>
    </div>


  </div>
</template>

<script>
  import Tools from "@/utils/tools";
  import moment from "moment"
  export default {
    name:"NoticeVersion",
    data() {
      return {
        DocTypeDict: {},
        $kgrid: null,
        formData: {},
        selectRowData: {},
        addDocTypeDict:{},
        prodSearchParam: {},
      };
    },
    computed: {
      queryParam() {
        return {
          'disclosureType': this.prodSearchParam.disclosureType,
          'disclosureSonType': this.prodSearchParam.disclosureSonType,
           'prodCode':this.prodSearchParam.prodCode,
          'prodName':this.prodSearchParam.prodName,
          'crtDate':this.prodSearchParam.crtDate
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
<style scoped>
>>> .el-table__cell {
  padding: 0px 0 !important;
}

>>> .specialClass > .md-ripple{
  padding: 8px !important;
}
</style>
