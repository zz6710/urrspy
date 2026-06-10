<template>
  <div class="py-page">
    <k-form-search-customize data-target="reportConditionGrid" v-model="queryParam" data-label-width="80px">
      <k-form-item label="报表ID">
        <k-field-text v-model="queryParam.menuid"/>
      </k-form-item>
      <k-form-item label="报表名称">
        <k-field-text v-model="queryParam.menuname"/>
      </k-form-item>
    </k-form-search-customize>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn slot="button" data-functype="POPUP" class="btn-custom-primary" data-target="addReportConditionPopup"
              :data-handler="()=>this.formData={}">
            <md-icon md-src="/static/svg/add.svg"/>
            新增
          </k-btn>
        </div>
      </div>
      <k-grid ref="reportConditionGrid" @data-row-select="selectRow" data-action="ReportCondition.findReportMenu"
              :data-params='{"menuid": queryParam.menuid, "menuname": queryParam.menuname}' data-operate-width="300px">
        <k-grid-column data-header="报表菜单ID" data-name="menuid"></k-grid-column>
        <k-grid-column data-header="报表菜单名" data-name="menuname"></k-grid-column>
        <k-grid-column data-header="上级菜单ID" data-name="upperid"></k-grid-column>
        <k-grid-column data-header="页面URL" data-name="url"></k-grid-column>
        <k-grid-column data-header="加载顺序" data-name="loadorder"></k-grid-column>
        <k-grid-column data-header="积木报表ID" data-name="jimuReportId"></k-grid-column>
<!--        <k-grid-column data-header="页面配置ID" data-name="pageid"></k-grid-column>-->
        <k-grid-column data-header="菜单状态" data-name="status" data-dict="menu_status" :data-hidden="true"></k-grid-column>
        <template slot="operate" slot-scope="scope" >
          <div class="templateDiv">
            <k-btn class="btn-custom-text specialClass" data-descript="修改报表菜单" data-functype="POPUP" data-size="mini" style="min-width:60px;"
                           data-target="editReportConditionPopup" >
                      修改
             </k-btn>

            <k-btn class="btn-custom-text specialClass" data-descript="编辑报表" data-size="mini" data-functype="PAGE" style="min-width:60px;"
                   :data-handler="editReport"
                   >
              编辑报表
            </k-btn>

          </div>
        </template>
      </k-grid>
    </div>

    <k-popup ref="addReportConditionPopup" data-title="新增">
      <k-form ref="addReportConditionForm" :data-col="2">
        <k-form-item label="菜单ID">
          <k-field-text v-model="formData.menuid" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="菜单名称">
          <k-field-text v-model="formData.menuname" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="上级菜单ID">
          <k-field-text v-model="formData.upperid" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="页面URL">
          <k-field-text v-model="formData.url" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="图标">
          <k-field-text v-model="formData.icon"/>
        </k-form-item>
        <k-form-item label="加载顺序">
          <k-field-text v-model="formData.loadorder" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="页面配置">
          <k-field-text v-model="formData.pageid"/>
        </k-form-item>
        <k-form-item label="菜单状态">
          <k-field-select v-model="formData.status" data-dict="menu_status" :data-allowblank="false"/>
        </k-form-item>
        <!-- <k-form-item label="报表控件类型" v-show="false">
          <k-field-select v-model="formData.objType" data-dict="obj_type" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="报送模板路径" v-show="false">
          <k-field-text v-model="formData.reporturl"/>
        </k-form-item> -->
        <k-form-item label="积木报表ID">
          <k-field-text v-model="formData.jimuReportId" :data-allowblank="false"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ReportCondition.addReportMenu"
                 data-from="addReportConditionForm"
                 :data-model="formData" data-target="reportConditionGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="editReportConditionPopup" data-title="修改">
      <k-form ref="editReportConditionForm" :data-col="2">
        <k-form-item label="菜单ID">
          <k-field-text v-model="formData.menuid" :data-allowblank="false" :data-disabled="true"
                        :data-clearable="false"/>
        </k-form-item>
        <k-form-item label="菜单名称">
          <k-field-text v-model="formData.menuname" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="上级菜单ID">
          <k-field-text v-model="formData.upperid" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="页面URL">
          <k-field-text v-model="formData.url" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="图标">
          <k-field-text v-model="formData.icon"></k-field-text>
        </k-form-item>
        <k-form-item label="加载顺序">
          <k-field-text v-model="formData.loadorder" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="页面配置">
          <k-field-text v-model="formData.pageid"/>
        </k-form-item>
        <k-form-item label="菜单状态">
          <k-field-select v-model="formData.status" data-dict="menu_status" :data-allowblank="false"/>
        </k-form-item>
        <!-- <k-form-item label="报表控件类型" v-show="false">
          <k-field-select v-model="formData.objType" data-dict="obj_type" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="报送模板路径" v-show="false">
          <k-field-text v-model="formData.reporturl"/>
        </k-form-item> -->
        <k-form-item label="积木报表ID">
          <k-field-text v-model="formData.jimuReportId" :data-allowblank="false"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ReportCondition.updateReportMenu"
                 data-from="editReportConditionForm"
                 :data-model="formData" data-target="reportConditionGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="singleUploadPopup" data-title="上传xml文件">
      <k-form ref="singleUploadForm" :data-col="2">
        <k-form-item label="上传附件" data-ui="element">
          <k-field-upload ref="fileUpload" :data-limit="10" :data-auto-upload="false" data-accept=".xml"
                          :dataChange="uploadXml" :data-remove="uploadRemove">
            <div slot="tip" class="el-upload__tip">只能上传xml文件</div>
          </k-field-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn ref="upload" class="btn-custom-primary" @click.native="uploadHandler">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
  </div>
</template>

<script>
  import Tools from '@/utils/tools.js';
  import httpUtil from "@/frame/httpUtil";

  export default {
    name: "reportCondition",
    data() {
      return {
        queryParam: {},
        formData: {},
        uploadData: []
      };
    },
    methods: {
      selectRow(row, column, event) {
        this.formData = Object.assign({}, row)
      },
      uploadXml(file, fileList) {
        if (file.name.substring(file.name.lastIndexOf(".") + 1) !== "xml") {
          Tools.alert("文档类型不为xml！", "warning");
          this.$refs.fileUpload.doReset()
        } else {
          this.uploadData = fileList;
        }
      },
      uploadRemove(file, fileList) {
        this.uploadData = fileList;
      },
      uploadHandler($event) {
        if (this.uploadData.length === 0) {
          Tools.alert("请选择文件，然后提交！", "warning");
        } else {
          let params = [];
          this.uploadData.forEach(file => {
            params.push(file.raw)
          });
          let children = $event.currentTarget.childNodes[0].childNodes[0].childNodes;
          let iconList = [];
          for (let i = 0; i < children.length; i++) {
            if (children[i].nodeName === 'I') {
              iconList.push(children[i])
            }
          }
          this.$refs.upload.setIconStyle(0, iconList);
          httpUtil.uploadForReport({
            url: 'upload-files/server/RptApp/upload.json',
            files: params,
            successAlert: true
          }).then(res => {
            if (res.success) {
              this.$refs.upload.setIconStyle(1, iconList)
              this.$refs.singleUploadPopup.close()
              this.$refs.reportConditionGrid._load()
            }
          })
        }
      },
      allExportHandler(){
        this.$confirm('是否导出全部报表?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.exportHandler()
        }).catch(() => {
        });
      },
      exportHandler(row) {
        httpUtil.download({
          url: "download/server/RptApp/download.json",
          params: row
        })
      },

      toEdit(row) {
        let pathUrl = '/main/report/develop/ReportQuery';
        this.$router.push({
          path: pathUrl,
          query: {
            forTable:row.forTable,
          },
        });

      },
      reportDownloadExe(){
        httpUtil.download({
                     url: "download/server/RptApp/reportDownloadExe.json",
                     params: {fileName:"Supcan-Setup113.8.exe",osPath:"80000080001",winPath:"80000080002",msg:"报表控件"},
                     callback: response => {
                       Tools.alert("下载完成");
                     }
        });
      },
      editReport(row) {
        this.$router.push({
          path: "/main/report/reportTemplate/editReport",
          query: {
            id: row.jimuReportId
          }
        });
      },
    }
  };

</script>
<style scoped>
>>> .el-table__cell {
  padding: 1px 0 !important;
}
>>> .specialClass > .md-ripple{
  padding: 5px !important;
}
</style>
