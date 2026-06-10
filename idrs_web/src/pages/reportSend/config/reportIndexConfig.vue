<template>
  <div class="py-page">
    <div>
      <k-form-search-customize v-model="searchParam" data-target="indexConfigGrid" data-label-width="100px" @handleReset="reportOnChangeSearch">

        <k-form-item label="报表大类">
          <k-field-select v-model="searchParam.reportType" data-dict="report_type" @data-on-change="reportOnChangeSearch"/>
        </k-form-item>

        <k-form-item label="报送报表名称">
					<k-field-select
						v-model="searchParam.reportTable"
						data-action="ReportTimeConfig.getReportTable"
						:data-params="{reportType: searchParam.reportType}"
						data-value-field="reportTable"
						data-display-field="tableName"
						:key="formKey"
					/>
				</k-form-item>

        <k-form-item label="指标代码">
          <k-field-text v-model="searchParam.indexCode"/>
        </k-form-item>

        <k-form-item label="校验指标类型">
          <k-field-select v-model="searchParam.indexType" data-dict="rpt_validate_type"/>
        </k-form-item>

        <k-form-item label="报表行名称">
          <k-field-text v-model="searchParam.rowName"/>
        </k-form-item>

        <k-form-item label="报表列名称">
          <k-field-text v-model="searchParam.listName"/>
        </k-form-item>

        <k-form-item label="规则标识">
          <k-field-select v-model="searchParam.indexRule" data-dict="index_rule"/>
        </k-form-item>

        <k-form-item label="状态">
          <k-field-select v-model="searchParam.isEffect" data-dict="index_status"/>
        </k-form-item>

      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.indexConfigAdd={}" slot="button"
                data-target="addIndexConfigPopup" >
            <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
          <k-btn slot="button" class="btn-custom-plain" data-target="indexConfigGrid" :data-export-name="'报表校验指标配置'"
                                data-descript="数据导出" data-functype="EXPORT" data-size="small" data-url="ReportValidationIndexModel.findReportValidationIndexConfigInformation">
                    <md-icon>cloud_download</md-icon>
                    导出
          </k-btn>
        </div>
      </div>
      <k-grid ref="indexConfigGrid" @data-row-select="selectRow" data-operate-width="300px" data-action="ReportValidationIndexModel.findReportValidationIndexConfigInformation"
              @init="(id)=>{this.$kgrid = id}" :data-checkbox="false" data-fixed="right" data-checkbox-id="indexCode">
        <k-grid-column data-align="left" data-header="指标代码" data-name="indexCode"></k-grid-column>
        <k-grid-column data-align="left" data-header="指标名称" data-name="indexName"></k-grid-column>
        <k-grid-column data-align="left" data-header="规则标识" data-name="indexRule" data-dict="index_rule" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="状态" data-name="isEffect" data-dict="index_status"></k-grid-column>
        <k-grid-column data-align="left" data-header="校验报表名称" data-name="tableName" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="报表列名称" data-name="listName" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="校验指标类型" data-name="indexType" data-dict="rpt_validate_type" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="校验表达式" data-name="express" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="校验指标详述" data-name="indexDetail" data-width="250"></k-grid-column>
        <k-grid-column data-align="left" data-header="校验字段行" data-name="rowNum" data-width="180"></k-grid-column>
                <k-grid-column data-align="left" data-header="校验字段列" data-name="columnNum" data-width="180"></k-grid-column>
                <k-grid-column data-align="left" data-header="报表行名称" data-name="rowName" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="备注" data-name="remark" data-width="150"></k-grid-column>
        <template slot="operate" slot-scope="scope" system-aligen>
          <k-btn data-functype="POPUP" data-confirm data-size="mini" class="btn-custom-text" :data-handler="selectRow"
                 data-target="editIndexConfigPopup" data-descript="修改校验指标参数">
            修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="ReportValidationIndexModel.deleteReportDataValidationIndex"
                 :data-confirm="true" data-size="mini" data-type="danger" data-target="indexConfigGrid" data-descript="删除">
            删除
          </k-btn>
          <k-btn class="btn-custom-text"  data-functype="SUBMIT" data-size="mini" data-action="ReportValidationIndexModel.recoverIndexStatus"
                 data-target="indexConfigGrid" :data-confirm="true" v-if="scope.row.row.isEffect == '02'">
            启用
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="ReportValidationIndexModel.stopIndexStatus" v-if="scope.row.row.isEffect == '01'"
                 :data-confirm="true" data-size="mini" data-type="danger" data-target="indexConfigGrid" >
            停用
          </k-btn>
          <k-btn class="btn-custom-text" data-descript="详情" data-functype="POPUP" data-size="mini"
                 :data-handler="selectRow" data-target="ReportIndexConfigPopup">
            详情
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    校验指标新增弹出框   -->
    <k-popup ref="addIndexConfigPopup" data-title="新增">
      <k-form ref="addIndexConfigForm" :data-col="2">

        <k-form-item label="校验指标代码">
          <k-field-text v-model="indexConfigAdd.indexCode" :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="关联报表名称">
          <k-field-select v-model="indexConfigAdd.reportTable" :data-data="reportTableDict" data-value-field="value" data-display-field="value,label" :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="校验字段所在行" >
          <k-field-text v-model="indexConfigAdd.rowNum" />
        </k-form-item>

        <k-form-item label="校验字段所在列" >
          <k-field-text v-model="indexConfigAdd.columnNum"/>
        </k-form-item>

        <k-form-item label="报表行名称">
          <k-field-text v-model="indexConfigAdd.rowName"/>
        </k-form-item>

        <k-form-item label="报表列名称">
          <k-field-text v-model="indexConfigAdd.listName"/>
        </k-form-item>

        <k-form-item label="校验指标类型">
          <k-field-select v-model="indexConfigAdd.indexType" data-dict="rpt_validate_type"/>
        </k-form-item>

        <k-form-item label="允许差值">
          <k-field-text v-model="indexConfigAdd.allowDeviation" data-digits="6" dataValidateType="money"/>
        </k-form-item>

        <k-form-item label="指标名称" :data-col="2" :data-row="2">
          <k-field-text v-model="indexConfigAdd.indexName"/>
        </k-form-item>

        <k-form-item label="校验表达式" :data-col="2" :data-row="2">
          <k-field-text v-model="indexConfigAdd.express"/>
        </k-form-item>

        <k-form-item label="表数据来源语句" :data-col="2" :data-row="2">
          <k-field-text v-model="indexConfigAdd.relationTables"/>
        </k-form-item>

        <k-form-item label="校验指标详述" :data-col="2" :data-row="2">
          <k-field-text v-model="indexConfigAdd.indexDetail"/>
        </k-form-item>

        <k-form-item label="正常日志模板" :data-col="2" :data-row="2">
          <k-field-text v-model="indexConfigAdd.correctPrompt"/>
        </k-form-item>

        <k-form-item label="异常日志模板" :data-col="2" :data-row="2">
          <k-field-text v-model="indexConfigAdd.errorPrompt"/>
        </k-form-item>

        <k-form-item label="备注" :data-col="2" :data-row="2">
          <k-field-text v-model="indexConfigAdd.remark"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" ref="confirmBtn" data-functype="SUBMIT"  data-from="addIndexConfigPopup" :data-handler="addReportIndexConfig"
                 :data-model="indexConfigAdd" data-target="indexConfigGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定</k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>

      </k-form>
    </k-popup>

    <!--   校验指标修改弹出框   -->
    <k-popup ref="editIndexConfigPopup" data-title="校验指标修改">
      <k-form ref="editIndexConfigForm" :data-col="2">

        <k-form-item label="校验指标代码">
          <k-field-text v-model="indexConfig.indexCode" :data-disabled="true" :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="关联报表名称">
          <k-field-select v-model="indexConfig.reportTable" :data-data="reportTableDict" data-value-field="value" data-display-field="value,label" :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="校验字段所在行" >
          <k-field-text v-model="indexConfig.rowNum"/>
        </k-form-item>

        <k-form-item label="校验字段所在列" >
          <k-field-text v-model="indexConfig.columnNum"/>
        </k-form-item>

        <k-form-item label="报表行名称">
          <k-field-text v-model="indexConfig.rowName"/>
        </k-form-item>

        <k-form-item label="报表列名称">
          <k-field-text v-model="indexConfig.listName"/>
        </k-form-item>

        <k-form-item label="校验指标类型">
          <k-field-select v-model="indexConfig.indexType" data-dict="rpt_validate_type"/>
        </k-form-item>

        <k-form-item label="允许差值">
          <k-field-text v-model="indexConfig.allowDeviation" data-digits="6" dataValidateType="money"/>
        </k-form-item>

        <k-form-item label="指标名称" :data-col="2" :data-row="2">
          <k-field-text v-model="indexConfig.indexName"/>
        </k-form-item>

        <k-form-item label="校验表达式" :data-col="2" :data-row="2">
          <k-field-text v-model="indexConfig.express"/>
        </k-form-item>

        <k-form-item label="表数据来源语句" :data-col="2" :data-row="2">
          <k-field-text v-model="indexConfig.relationTables"/>
        </k-form-item>

        <k-form-item label="校验指标详述" :data-col="2" :data-row="2">
          <k-field-text v-model="indexConfig.indexDetail"/>
        </k-form-item>

        <k-form-item label="正常日志模板" :data-col="2" :data-row="2">
          <k-field-text v-model="indexConfig.correctPrompt"/>
        </k-form-item>

        <k-form-item label="异常日志模板" :data-col="2" :data-row="2">
          <k-field-text v-model="indexConfig.errorPrompt"/>
        </k-form-item>

        <k-form-item label="备注" :data-col="2" :data-row="2">
          <k-field-text v-model="indexConfig.remark"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-from="editIndexConfigPopup" @click="updateReportIndexConfig"
                 :data-model="indexConfig" data-target="indexConfigGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定</k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>

      </k-form>
    </k-popup>

    <!--   校验指标详情弹出框   -->
    <k-popup ref="ReportIndexConfigPopup" data-title="校验指标详情">
      <k-form :data-col="3" class="form-detail">

        <k-form-item label="指标代码">
          <k-field-display v-model="indexConfig.indexCode"/>
        </k-form-item>

        <k-form-item label="关联报表名称" v-show="false">
          <k-field-display v-model="indexConfig.reportTable" />
        </k-form-item>

        <k-form-item label="校验报表名称">
          <k-field-display v-model="indexConfig.reportTable" />
        </k-form-item>

        <k-form-item label="校验字段所在行" >
          <k-field-display v-model="indexConfig.rowNum"/>
        </k-form-item>

        <k-form-item label="校验字段所在列" >
          <k-field-display v-model="indexConfig.columnNum"/>
        </k-form-item>

        <k-form-item label="报表行名称">
          <k-field-display v-model="indexConfig.rowName"/>
        </k-form-item>

        <k-form-item label="报表列名称">
          <k-field-display v-model="indexConfig.listName"/>
        </k-form-item>

        <k-form-item label="校验指标类型">
          <k-field-display v-model="indexConfig.indexType" data-dict="rpt_validate_type"/>
        </k-form-item>

        <k-form-item label="允许差值">
          <k-field-display v-model="indexConfig.allowDeviation"/>
        </k-form-item>

        <k-form-item label="指标名称" :data-col="2" :data-row="2">
          <k-field-display v-model="indexConfig.indexName"/>
        </k-form-item>

        <k-form-item label="校验表达式" :data-col="2" :data-row="2">
          <k-field-display v-model="indexConfig.express"/>
        </k-form-item>

<!--        <k-form-item label="表数据来源语句" :data-col="2" :data-row="2">-->
<!--          <k-field-display v-model="indexConfig.relationTables"/>-->
<!--        </k-form-item>-->

        <k-form-item label="校验指标详述" :data-col="2" :data-row="2">
          <k-field-display v-model="indexConfig.indexDetail"/>
        </k-form-item>

        <k-form-item label="正常日志模板" :data-col="2" :data-row="2">
          <k-field-display v-model="indexConfig.correctPrompt"/>
        </k-form-item>

        <k-form-item label="异常日志模板" :data-col="2" :data-row="2">
          <k-field-display v-model="indexConfig.errorPrompt"/>
        </k-form-item>

        <k-form-item label="备注" :data-col="2" :data-row="2">
          <k-field-display v-model="indexConfig.remark"/>
        </k-form-item>

      </k-form>
    </k-popup>

  </div>
</template>

<script>
  import Tools from "@/utils/tools";

  export default {
    name:"reportIndexConfig",
    data() {
      return {
        selectRowData: {},
        searchParam:{
          reportTableDict:'',
          reportTableShowDict:'',
          reportTable: '',
        },
        indexConfig:{},
        reportTableDict:{},
        indexConfigAdd:{},
        formKey: 1
      };
    },
    created() {
      this.getReportTableDict();
      this.getReportNameSearch();
    },
    methods: {
      getReportTableDict() {
        this.httpUtil.comnQuery({
          action: "ReportValidationIndexModel.getReportTableDict",
          params: null
        }).then(data => {
          this.reportTableDict = data.rows;
        }).catch({})
      },
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row);
        this.indexConfig = Object.assign({}, row);
      },
      getReportNameSearch() {
        this.$set(this.searchParam, 'reportTableShowDict', '');
        this.httpUtil.comnQuery({
          action: "ReportTimeConfig.getReportTable",
          params: null
        }).then(data => {
          this.searchParam.reportTableShowDict = data.rows;
        }).catch({})
      },
      reportOnChangeSearch(){
        this.searchParam.reportTable = "";
			  this.formKey += 1;
      },
      updateReportIndexConfig(){//修改指标配置信息
        this.httpUtil.ajax({
          url:"/server/form/RptApp/reportIndexConfig/updateReportIndex.action",
          params: this.indexConfig
        }).then(res=>{
          this.$refs.editIndexConfigPopup.close();
          this.$refs.indexConfigGrid.load(this.searchParam);
          Tools.alert(res.returnmsg);
        })
      },
      addReportIndexConfig(){//新增指标配置信息
        const valid = this.$refs.addIndexConfigForm.validate()
        if (!valid) {
          return false
        }
        this.$refs.confirmBtn.setIconStyle(0)
        this.httpUtil.ajax({
          url:"/server/form/RptApp/reportIndexConfig/addReportIndex.action",
          params: this.indexConfigAdd
        }).then(res=>{
          if (res.success) {
            this.$refs.addIndexConfigPopup.close();
            this.$refs.indexConfigGrid.load(this.searchParam);
            Tools.alert(res.returnmsg);
          }
          this.$refs.confirmBtn.setIconStyle(1)
        }).catch(err=>{
          this.$refs.confirmBtn.setIconStyle(1)
        })
        return false
      },
     }
  };
</script>
<style lang="scss" scoped>
>>> .el-table__cell {
  padding: 1px 0 !important;
}
>>> .specialClass > .md-ripple{
  padding: 5px !important;
}
.form-detail {
  /deep/ {
    .el-form-item {
      width: calc(50% - 10px);
    }
  }
}
</style>
