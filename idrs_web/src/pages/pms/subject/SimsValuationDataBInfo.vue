<template>
  <div class="py-page">
    <div>
      <k-form-search-customize ref="searchFormRef" data-model-name="SimsValuationDataBInfoB" data-target="tableGrid" data-label-width="80px" v-model="queryParam">
        <k-form-item label="估值日期">
          <k-field-date v-model="searchParam.inputDate" data-type="date" data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd" :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="资产代码">
          <k-field-text v-model="searchParam.icode" data-validate-type="text"/>
        </k-form-item>
        <k-form-item label="底层代码">
          <k-field-text v-model="searchParam.bottomCode" data-validate-type="text"/>
        </k-form-item>
        <k-form-item label="资产分类">
          <k-field-select v-model="searchParam.assetType" data-dict="sims_asset_type"/>
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn slot="button" ref="exportRef" class="btn-custom-plain"  data-functype="EXPORT" data-target="tableGrid" data-export-dict="true"
                :data-export-name="'SIMS底层估值明细表（调整后）'" @downSuccess="downSuccess" :data-handler="handleExport" data-export-form="searchFormRef">
            <md-icon>cloud_download</md-icon>
            导出
          </k-btn>
          <k-btn slot="button" ref="uploadBtnRef" :load-disabled="false" loadingTip="正在导入并重新生成底层估值明细表（调整前），请稍后重试" data-functype="POPUP" class="btn-custom-plain"
                data-target="uploadSimsValuationDataBInfoPopup" :handle-before="uploadHandleBefore">
            <md-icon>cloud_upload</md-icon>
            导入
          </k-btn>
        </div>
      </div>

      <k-grid ref="tableGrid" @data-row-select="selectRow" data-fixed="right" data-operate-width="150px" data-operate-column="false" :data-autoload="false" data-action="SimsValuationDataBInfo.findSimsValuationDataBInfos" >
        <k-grid-column data-align="left" data-header="资产代码" data-name="icode" data-width="200" ></k-grid-column>
        <k-grid-column data-align="left" data-header="组合代码" data-name="comcode" data-width="200" ></k-grid-column>
        <k-grid-column data-align="left" data-header="底层代码" data-name="bottomCode" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="资产分类" data-name="assetType"  data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="净价金额" data-name="netValue" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="市值" data-name="amount" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="成本" data-name="cost" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="币种" data-name="currency" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="科目代码" data-name="itemId" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="科目名称" data-name="itemName" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="交易场所" data-name="tradePlace" data-width="100"></k-grid-column >
        <k-grid-column data-align="left" data-header="中债报送类别" data-name="zzReportType" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="估值日期" data-name="inputDate" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="导入日期" data-name="importDate" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="底层资产首次入库日期" data-name="dataInsrDt" data-width="180"></k-grid-column>
      </k-grid>
    </div>


    <k-popup ref="uploadSimsValuationDataBInfoPopup" title="SIMS估值数据导入" @data-opened="uploadOpened()" >
        <k-form ref="addForm" data-ui="element" data-label-width="140px">
          <k-form-item label="估值日期">
                  <k-field-date v-model="formData.inputDate" data-type="date" data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd"  :data-allowblank="false"></k-field-date>
          </k-form-item>
          <k-form-item label="SIMS估值数据导入" data-ui="element" data-input-width="500px">
            <k-field-excel-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit='1'
                data-accept=".xlsx,.xls"
                :data-error="onSubmitError" :data-success="onSubmitSuccess"
                :data-auto-upload="false"
                data-upload-url="upload/server/RptApp/simsValuationDataBInfoImport.json">
            </k-field-excel-upload>
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="tableGrid" ref="submitBtn"
                  :data-auto-upload="false" data-from="addForm" :data-handler="submitUploadParam">
                  <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
          </k-form-footer>
        </k-form>
        </k-popup>
  </div>
</template>

<script>
import ProdMixin from "@/pages/zz/manage/mixins/prodMixin.js"
import Tools from '@/utils/tools.js';
export default {
  name: "SimsValuationDataBInfo",
  mixins: [ProdMixin],
  data() {
    return {
      formData: {
              inputDate: "",
      },
      selectRowData: {},
      searchParam:{},
      infoPop: {},
      queryParamDateRange: [],
      comfirmExportParam: {}
    };
  },
  computed: {
    queryParam () {
      return {
          'beginDate': this.queryParamDateRange ? this.queryParamDateRange[0] : null,
          'queryDate': this.queryParamDateRange ? this.queryParamDateRange[1] : null,
          'inputDate': this.searchParam.inputDate,
          'icode': this.searchParam.icode,
          'bottomCode': this.searchParam.bottomCode,
          'assetType': this.searchParam.assetType,
      }
    }
  },
  watch: {
	},
  methods: {
    setConfirmExportParam() {
      this.comfirmExportParam = {
        beginDate: this.queryParamDateRange ? this.queryParamDateRange[0] : null,
        queryDate: this.queryParamDateRange ? this.queryParamDateRange[1] : null,
      };
    },
    submitUploadParam() {
      //文件上传校验
      let validate = this.$refs.addForm.validate();
      if (validate) {
        let formData = {inputDate: this.formData.inputDate};
        let temp = document.getElementsByClassName('upload-demo');
        let lis = temp[0].childNodes[1].childNodes.length;
        if (lis > 0) {
          this.$confirm("请确认是否包含所有存续产品/持有资管计划的估值数据，导入后该日期数据不会再用上游数据覆盖！！！", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          })
            .then(() => {
              // 判断下是否有正在执行的
              this.httpUtil
              .comnUpdate({
					      action: "SimsValuationDataBInfo.reloadTaskQuery",
					      params: { inputDate: this.formData.inputDate },
					      successAlert: false,
                dataAfterSuccess: (reData)=>{
                  this.$refs.uploadRef.upload(formData);
                  this.$refs.uploadBtnRef.setIconStyle(0);
                  setTimeout(()=>{
                    this.$refs.uploadSimsValuationDataBInfoPopup.close();
                  }, 500)
                }
				      });
            })
            .catch(() => {});
        } else {
          this.$message.error("上传文件不能为空!");
        }
      }
      return false
    },
    onSubmitSuccess() {
      // 导入成功后
      if (this.searchParam.inputDate) {
        this.$refs.tableGrid.load(this.queryParam);
      }
      this.httpUtil
				.comnUpdate({
					action: "SimsValuationDataBInfo.reloadTask",
					params: { inputDate: this.formData.inputDate },
					successAlert: false,
          dataAfterSuccess: (reData)=>{
            this.$refs.uploadBtnRef.setIconStyle(1);
            Tools.alertTime(reData.returnmsg, "success", 0);
          }
				})
        .then(data => {
					this.$refs.uploadBtnRef.setIconStyle(1);
        });
    },
    uploadOpened() {
      this.formData.inputDate = ''
    },
    uploadHandleBefore() {
      this.formData.inputDate = ''
      return true
    },
    onSubmitError() {
      this.$refs.uploadBtnRef.setIconStyle(1)
    },
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
    },
  }
};
</script>
