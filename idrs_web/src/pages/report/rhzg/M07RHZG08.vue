
<template>
  <div class="py-page">

          <k-form-search-customize ref="searchRef" data-model-name="ZG08" data-target="ZG08Grid" v-model="queryParam" data-label-width="150px">
            <k-form-item label="数据日期">
              <k-field-date v-model="prodSearchParam.reportDate" data-type="month" data-date-format="yyyy-MM" data-value-format="yyyyMM" :data-allowblank="false"/>
            </k-form-item>
            <k-form-item label="产品代码_资管">
              <k-field-text v-model="prodSearchParam.prodCd" data-validate-type="text"/>
            </k-form-item>
            <k-form-item label="资产负债项目">
              <k-field-select v-model="prodSearchParam.assetDebtProject" data-dict="pbc_balance_proj_prt" data-dict-type="1"/>
            </k-form-item>
            <k-form-item label="交易对手产品种类">
              <k-field-select v-model="prodSearchParam.counterpartProdTyp" data-dict="pbc_prd_typ" data-dict-type="1"/>
            </k-form-item>
            <k-form-item label="交易对手机构编码">
              <k-field-text v-model="prodSearchParam.counterpartOrgCd" data-validate-type="text"/>
            </k-form-item>
            <k-form-item label="交易对手产品代码">
              <k-field-text v-model="prodSearchParam.counterpartProdCd" data-validate-type="text"/>
            </k-form-item>
          </k-form-search-customize>
          <div class="py-page-container">
            <div class="table-top-btns">
              <div class="left">
                <k-btn ref="assetsZG08Send" slot="button" class="btn-custom-plain"
              data-descript="数据报送" data-size="small" @click="creatZipFile('ZG08')">
               <md-icon>cloud_download</md-icon>
              数据报送
            </k-btn>
                <k-btn slot="button" class="btn-custom-plain" data-target="ZG08Grid" :data-export-name="exportName('ZG08', '特定目的载体交易对手明细信息')"
              data-descript="报送数据导出" data-functype="EXPORT" data-size="small"
              data-url="ZG08.findZG08s" data-export-form="searchRef">
              <md-icon>cloud_download</md-icon>
              报送数据导出
            </k-btn>
                <k-btn slot="button" data-functype="POPUP" class="btn-custom-plain"
                data-target="addPopup">
              <md-icon>cloud_upload</md-icon>
              报送数据导入
            </k-btn>
          </div>
          </div>
            <k-grid ref="ZG08Grid" @data-row-select="selectRow" data-action="ZG08.findZG08s" data-fixed="right" data-operate-width="120px" :data-autoload="false" data-dict-type="1">
                <k-grid-column data-header="数据日期" data-name="reportDate" data-width="100" data-export="false"></k-grid-column>
                <k-grid-column data-header="产品代码_资管" data-name="prodCd"></k-grid-column>
                <k-grid-column data-header="资产负债项目" data-name="assetDebtProject" data-dict="pbc_balance_proj_prt" data-width="150"></k-grid-column>
                <k-grid-column data-header="交易对手产品种类" data-name="counterpartProdTyp" data-dict="pbc_prd_typ" data-width="150"></k-grid-column>
                <k-grid-column data-header="交易对手机构编码" data-name="counterpartOrgCd"></k-grid-column>
                <k-grid-column data-header="交易对手产品代码" data-name="counterpartProdCd"></k-grid-column>
                <k-grid-column data-header="币种_资管表" data-name="ccy"></k-grid-column>
                <k-grid-column data-header="期末金额" data-name="endDtAmt"></k-grid-column>
                <k-grid-column data-header="期末金额折人民币" data-name="endDtAmtCny"></k-grid-column>
                <!-- <k-grid-column data-header="交易对手产品名称" data-name="counterpartProdNm"></k-grid-column> -->
                <template slot="operate" slot-scope="scope">
                <k-btn class="btn-custom-text" data-descript="修改" data-functype="POPUP" data-size="mini"
                    data-target="editZG08Popup">
                    修改
                </k-btn>
                <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="ZG08.deleteZG08" data-size="mini"
                    data-type="danger" data-target="ZG08Grid" :data-confirm="true" data-descript="删除">
                    删除
                </k-btn>
                </template>
            </k-grid>
          </div>
          <!--    修改特定目的载体交易对手明细信息弹出框   -->
          <k-popup ref="editZG08Popup" data-title="修改">
            <k-form ref="editZG08Form" :data-col="2" isFormBodyScreen data-label-width="140px">
                <k-form-item label="数据日期" :class="[handleItemDiff('reportDate')]">
                    <k-field-date v-model="formData.reportDate" :data-allowblank="true" data-type="date" data-value-format="yyyyMMdd"  :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="产品代码_资管" :class="[handleItemDiff('prodCd')]">
                    <k-field-text v-model="formData.prodCd"  :data-allowblank="false" :data-disabled="false"/>
                </k-form-item>

                <k-form-item label="资产负债项目" :class="[handleItemDiff('assetDebtProject')]">
                    <k-field-select v-model="formData.assetDebtProject" :data-allowblank="false" data-dict="pbc_balance_proj_prt" data-dict-type="1"  :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="交易对手产品种类" :class="[handleItemDiff('counterpartProdTyp')]">
                    <k-field-select v-model="formData.counterpartProdTyp" :data-allowblank="false" data-dict="pbc_prd_typ" data-dict-type="1"  :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="交易对手机构编码" :class="[handleItemDiff('counterpartOrgCd')]">
                    <k-field-text v-model="formData.counterpartOrgCd" :data-allowblank="false" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="交易对手产品代码" :class="[handleItemDiff('counterpartProdCd')]">
                    <k-field-text v-model="formData.counterpartProdCd" :data-allowblank="false" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="币种_资管08表" :class="[handleItemDiff('ccy')]">
                    <k-field-text v-model="formData.ccy" :data-allowblank="false" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="期末金额" :class="[handleItemDiff('endDtAmt')]">
                    <k-field-text v-model="formData.endDtAmt" :data-allowblank="false" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2" />
                </k-form-item>
                <k-form-item label="期末金额折人民币" :class="[handleItemDiff('endDtAmtCny')]">
                    <k-field-text v-model="formData.endDtAmtCny" :data-allowblank="false" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2" />
                </k-form-item>



                <k-form-footer data-align="center" slot="footer">
                <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ZG08.updateZG08" data-from="editZG08Form"
                    :data-model="formData" data-target="ZG08Grid" :handle-before="handleBefore">
                    <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
                </k-btn>
                <k-btn class="btn-custom-plain" data-functype="CLOSE">
                    <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
                </k-form-footer>
            </k-form>
          </k-popup>


     <k-popup ref="addPopup" title="报送数据导入" @data-opened="uploadOpened()">
    <k-form ref="addForm" data-ui="element">
      <k-form-item label="数据日期">
              <k-field-date v-model="beginDate" data-type="month" data-date-format="yyyy-MM" data-value-format="yyyy-MM"  :data-allowblank="false"></k-field-date>
      </k-form-item>
      <k-form-item label="报送数据导入" data-ui="element" data-input-width="500px">
        <k-field-excel-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit='1'
            data-accept=".xlsx,.xls"
            :data-error="onSubmitError" :data-success="onSubmitSuccess"
            :data-auto-upload="false"
            data-upload-url="/upload/server/RptApp/rhzg/uploadZG08.json">
        </k-field-excel-upload>
      </k-form-item>
      <k-form-footer data-align="center">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="ZG08Grid" ref="submitBtn"
              :data-auto-upload="false" data-from="addForm" :data-handler="submitUploadParam">确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
      </k-form-footer>
    </k-form>
    </k-popup>

  </div>
</template>
<script>
import Tools from '@/utils/tools.js';
import ZgMixin from "@/pages/report/rhzg/zgMixin.js";
export default {
    name: "M07RHZG08",
    mixins: [ZgMixin],
    data () {
        return {
            reportDate: '',
            uploadBeginDate: '',
            uploadQueryDate: '',
            activeName: '1',
            prodSearchParam: {
                reportDate: Tools.getPreviousMonth(),
                prodCd: '',
                theoryReportStartDate: localStorage.getItem('currentWorkday'),
                id: '',
                assetDebtProject: '',
                counterpartProdTyp: '',
            },
            formData: {},
            formDataCopy: {},
            beginDate:'',
            directedData:{},
            nowDate:'',
        }
    },
    created() {
      this.getNowDate();
    },
    computed: {
      queryParam () {
        return {
             ...this.prodSearchParam
            }
        }
    },
    methods: {
      handleBefore() {
        if (this.formNoChangeCb()) {
          this.$refs.editZG08Popup.close();
          return false
        }
        return true
      },
       submitUploadParam() {
        //文件上传校验
        let validate = this.$refs.addForm.validate();
        if (validate) {
          let formData = { beginDate: this.lastDayBeginDate };
          let temp = document.getElementsByClassName('upload-demo');
          let lis = temp[0].childNodes[1].childNodes.length;
          if (lis > 0) {
            this.$refs.uploadRef.upload(formData);
          } else {
            this.$message.error("上传文件不能为空!");
            return false;
          }
        }
      },
      onSubmitSuccess() {
        this.beginDate = '';
        this.$refs.uploadRef.doReset();
        this.$refs.addForm.reset();
        this.$refs.addPopup.close();
        if (this.prodSearchParam.reportDate) {
          this.$refs.ZG08Grid.load(this.queryParam);
        }
      },
      onSubmitError() {
        this.$refs.uploadRef.doReset();
        this.$refs.submitBtn.setIconStyle(1, [])
      },
      uploadOpened() {
       this.beginDate = '';
      },

      tabClick(tab, event) {
          this.$refs.ZG08Grid.load(this.prodSearchParam)

      },
      selectRow(row, column, event) {
        this.formData = Object.assign({}, row)
        this.formDataCopy = Object.assign({}, row)
      },
      getNowDate() {
        const timeOne = new Date();
        const year = timeOne.getFullYear();
        let month = timeOne.getMonth() + 1;
        let day = timeOne.getDate();
        month = month < 10 ? '0' + month : month;
        day = day < 10 ? '0' + day : day;
        this.nowDate=year+''+month+''+day;
      }
  }

}
</script>
