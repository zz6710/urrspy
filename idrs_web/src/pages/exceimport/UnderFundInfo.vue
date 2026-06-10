<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="UnderFundInfo"  data-target="underFondInfoGrid" v-model="searchParam" data-label-width="190px">
        <k-form-item label="对应资管及委外资产行内资产/负债编码" class="lh16">
          <k-field-select v-model="searchParam.assetManagerCode"
                          data-action="UnderFundInfo.findassetManagerCode" :dataRemote="true"
                          data-display-field="assetManagerCode" data-value-field="assetManagerCode"/>
        </k-form-item>
        <k-form-item label="底层资产行内资产负债/编码">
          <k-field-text v-model="searchParam.underAssetCode"/>
        </k-form-item>
        <k-form-item label="持仓日期" data-label-width="80px">
          <k-field-date v-model="searchParam.reportDate" data-date-format="yyyyMMdd"/>
        </k-form-item>
        <!--
        <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}"
               data-target="addUnderAssetRegistInfoPopup" slot="button">
          <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
        -->
        
        <!--
        <k-btn slot="button"  class="btn-custom-plain"  data-functype="EXPORT" data-target="underAssetRegistInfoGrid"
               :data-export-name="'底层资产持仓管理'">
          <md-icon>cloud_download</md-icon>
          导出
        </k-btn>
        -->
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn slot="button" class="btn-custom-plain" :data-download-name="'私募基金底层信息导入'+'.xlsx'"
               data-descript="下载Excel模板" data-functype="DOWNLOAD" data-size="small"
               data-url="/download/server/DpsApp/UnderFundInfo/comn-download.json">
            <md-icon>cloud_download</md-icon>
            下载Excel模板
          </k-btn>
          <k-btn slot="button" data-functype="POPUP" class="btn-custom-plain"
                data-target="uploadUnderFondInfoPopup">
            <md-icon>cloud_upload</md-icon>
            导入
          </k-btn>
        </div>
      </div>
      <k-grid ref="underFondInfoGrid" @data-row-select="selectRow" data-fixed="right"
              data-operate-width="180px" data-action="UnderFundInfo.findFundInfos" >
        <k-grid-column data-align="left" data-header="ID" data-name="id" :data-hidden="true"  data-export="false" data-width="150"/>
        <k-grid-column data-align="left" data-header="发行机构代码" data-name="bankCode" data-width="120"/>
        <k-grid-column data-align="left" data-header="对应资管及委外资产行内资产/负债编码" data-name="assetManagerCode" data-width="150"/>
        <k-grid-column data-align="right" data-header="资管及委外资产当前总数量" data-name="assetSumNumber" data-width="150"/>
        <k-grid-column data-align="right" data-header="资管及委外资产当前总折算人民币金额(元)" data-name="convertSumAmt" data-width="150"/>
        <k-grid-column data-align="right" data-header="资管及委外资产未投资头寸(元)" data-name="nonInvestedAmt" data-width="150"/>
        <k-grid-column data-align="left" data-header="底层资产行内资产负债/编码" data-name="underAssetCode" data-width="150"/>
        <k-grid-column data-align="right" data-header="底层资产持仓数量" data-name="underAssetSum" data-width="150"/>
        <k-grid-column data-align="right" data-header="底层资产折算人民币市值(元)" data-name="underConvertSumAmt" data-width="150"/>
        <k-grid-column data-align="left" data-header="持仓日期" data-name="reportDate" data-type="date" data-width="100"/>
        <template slot="operate" slot-scope="scope">

          <k-btn class="btn-custom-text specialClass" data-descript="修改底层资产持仓管理" data-functype="POPUP" data-size="mini"
                 data-target="editUnderFondInfoPopup">
            修改
          </k-btn>

          <k-btn class="btn-custom-text specialClass" data-functype="SUBMIT" data-action="UnderFundInfo.deleteFundInfo" data-size="mini"
                 data-type="danger" data-target="underFondInfoGrid" :data-confirm="true" data-descript="删除底层资产持仓管理">
            删除
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    添加底层资产持仓管理弹出框   -->
    <k-popup ref="editUnderFondInfoPopup" data-title="修改">
      <k-form ref="editUnderFondInfoForm" :data-col="2" isFormBodyScreen>
        <k-form-item label="发行机构代码">
          <k-field-text v-model="formData.bankCode"  :data-allowblank="false" :data-max-length="6"/>
        </k-form-item>
        <k-form-item label="对应资管及委外资产行内资产/负债编码">
          <k-field-text v-model="formData.assetManagerCode" :data-allowblank="false" :data-max-length="40"/>
        </k-form-item>
        <k-form-item label="资管及委外资产当前总折算人民币金额(元)">
          <k-field-text v-model="formData.convertSumAmt" :data-allowblank="false" data-integer-length="13" data-digits="2" data-validate-type="money" data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
        </k-form-item>
        <k-form-item label="资管及委外资产当前总数量">
          <k-field-text v-model="formData.assetSumNumber" :data-allowblank="false" data-integer-length="13" data-digits="2" data-validate-type="money" data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
        </k-form-item>
        <k-form-item label="资管及委外资产未投资头寸(元)">
          <k-field-text v-model="formData.nonInvestedAmt" :data-allowblank="false" data-integer-length="13" data-digits="2" data-validate-type="money" data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
        </k-form-item>
        <k-form-item label="底层资产行内资产/负债编码">
          <k-field-text v-model="formData.underAssetCode" :data-allowblank="false" :data-max-length="40"/>
        </k-form-item>
        <k-form-item label="底层资产持仓数量">
          <k-field-text v-model="formData.underAssetSum" :data-allowblank="false" data-integer-length="13" data-digits="5" data-validate-type="number" data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
        </k-form-item>
        <k-form-item label="底层资产折算人民币市值(元)">
          <k-field-text v-model="formData.underConvertSumAmt" :data-allowblank="false" data-integer-length="13" data-digits="2" data-validate-type="money" />
        </k-form-item>
        <k-form-item label="持仓日期">
          <k-field-date v-model="formData.reportDate" :data-allowblank="false"  data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd" />
        </k-form-item>
        <k-form-footer slot="footer" data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="UnderFundInfo.updateFundInfo" data-from="editUnderFondInfoForm"
                 :data-model="formData" data-target="editUnderFondInfoGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>


    <k-popup ref="uploadUnderFondInfoPopup" title="报送数据导入">
      <k-form ref="addForm" data-ui="element">

        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-excel-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit='1'
                                data-accept=".xlsx,.xls" :data-error="onSubmitError" :data-success="onSubmitSuccess"
                                :data-auto-upload="false"
                                data-upload-url="upload/server/DpsApp/excelUploadAction/underFundUploadAction.json">
          </k-field-excel-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="underFondInfoGrid" ref="submitBtn"
                 :data-auto-upload="false" data-from="addForm" :data-handler="submitUploadParam">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

  </div>
</template>

<script>
export default {
  name: "UnderFundInfo",
  data() {
    return {
      formData: {},
      selectRowData: {},
      searchParam: {},
    };
  },
  methods: {
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
    },

    onSubmitSuccess() {
      this.$refs.uploadRef.doReset();
      this.$refs.addForm.reset();
      this.$refs.uploadUnderFondInfoPopup.close();
      this.$refs.underFondInfoGrid.load(this.searchParam);
    },
    onSubmitError() {
      this.$refs.uploadRef.doReset();
      this.$refs.submitBtn.setIconStyle(1, [])
    },
    submitUploadParam(){
      //文件上传校验
        let temp = document.getElementsByClassName('upload-demo');
        let lis = temp[0].childNodes[1].childNodes.length;
        if (lis > 0) {
          let formData = this.formData;
          this.$refs.uploadRef.upload(formData);
        } else {
          this.$message.error("上传文件不能为空!");
          return false;
        }
      },

  },
  watch: {

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
