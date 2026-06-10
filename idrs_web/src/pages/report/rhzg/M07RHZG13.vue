
<template>
  <div class="py-page">

          <k-form-search-customize ref="searchRef" data-model-name="ZG13" data-target="ZG13Grid" v-model="queryParam">
            <k-form-item label="数据日期">
              <k-field-date v-model="prodSearchParam.reportDate" data-type="month" data-date-format="yyyy-MM" data-value-format="yyyyMM" :data-allowblank="false"/>
            </k-form-item>
            <k-form-item label="产品代码_资管">
              <k-field-text v-model="prodSearchParam.prodCd" data-validate-type="text"/>
            </k-form-item>
          </k-form-search-customize>
          <div class="py-page-container">
            <div class="table-top-btns">
              <div class="left">
                <k-btn ref="assetsZG13Send" slot="button" style="width: 120px" class="btn-custom-plain"
              data-descript="数据报送" data-size="small" @click="creatZipFile('ZG13')">
               <md-icon>cloud_download</md-icon>
              数据报送
            </k-btn>
                <k-btn slot="button" style="width: 120px" class="btn-custom-plain" data-target="ZG13Grid" :data-export-name="exportName('ZG13', '其他股权明细信息')"
              data-descript="报送数据导出" data-functype="EXPORT" data-size="small"
              data-url="ZG13.findZG13s" data-export-form="searchRef">
              <md-icon>cloud_download</md-icon>
              报送数据导出
            </k-btn>
                <k-btn slot="button" style="width: 120px" data-functype="POPUP" class="btn-custom-plain"
                data-target="addPopup">
              <md-icon>cloud_upload</md-icon>
              报送数据导入
            </k-btn>
          </div>
          </div>
            <k-grid ref="ZG13Grid" @data-row-select="selectRow" data-action="ZG13.findZG13s" data-fixed="right" data-operate-width="120px" :data-autoload="false" data-dict-type="1">
                <k-grid-column data-align="left" data-header="数据日期" data-name="reportDate" data-width="100" data-export="false"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品代码_资管" data-name="prodCd" data-width="130"></k-grid-column>
                <k-grid-column data-align="left" data-header="资产负债项目" data-name="assetDebtProject"  data-dict="pbc_sh_balance_proj" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="其他股权投资内部编码" data-name="scrCd" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="标的企业名称" data-name="scrOrgNm" data-width="180"></k-grid-column>
                <k-grid-column data-align="left" data-header="标的企业代码" data-name="scrOrgCd" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="地区代码_资管13表" data-name="orgBlgZon"  data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="行业信息" data-name="orgBlgIndustry" data-dict="isuOrgBlgIdt01" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="企业出资人经济成分" data-name="orgTypEcn" data-dict="pbc_eco_comp" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="企业规模_资管" data-name="orgTypScale" data-dict="pbc_enterprise_scale" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="股权投资方式" data-name="rightInvestWay" data-dict="right_invest_way" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="股权出让方代码" data-name="rightOrgCd" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="股权出让方名称" data-name="rightOrgNm" data-width="180"></k-grid-column>
                <k-grid-column data-align="left" data-header="合同币种" data-name="ccyCd" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="合同金额" data-name="amount" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="合同金额折人民币" data-name="amountCny" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="其他股权余额币种" data-name="rightCcyCd" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="其他股权余额" data-name="rightAmount" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="其他股权余额折人民币" data-name="rightAmountCny" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="持股比例" data-name="posRat" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="投资退出方式" data-name="investExtWay" data-dict='invest_ext_way' data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="合同起始日期_资管" data-name="bgnDt" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="合同预计终止日期" data-name="mtuDt" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="合同展期到期日期" data-name="deferMtuDt" data-width="150"></k-grid-column>
                <template slot="operate" slot-scope="scope">
                <k-btn class="btn-custom-text" data-descript="修改" data-functype="POPUP" data-size="mini"
                    data-target="editZG13Popup">
                    修改
                </k-btn>
                <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="ZG13.deleteZG13" data-size="mini"
                    data-type="danger" data-target="ZG13Grid" :data-confirm="true" data-descript="删除">
                    删除
                </k-btn>
                </template>
            </k-grid>
          </div>
          <!--    修改资产负债剩余期限信息弹出框   -->
          <k-popup ref="editZG13Popup" data-title="修改">
            <k-form ref="editZG13Form" :data-col="3" isFormBodyScreen data-label-width="170px">
               <k-form-item label="数据日期" :class="[handleItemDiff('reportDate')]"><k-field-date v-model="formData.reportDate" :data-allowblank="true" data-type="date" data-value-format="yyyyMMdd" :data-disabled="true"/></k-form-item>
               <k-form-item label="产品代码_资管" :class="[handleItemDiff('prodCd')]"><k-field-text v-model="formData.prodCd"  :data-allowblank="false" :data-disabled="true"/></k-form-item>
               <k-form-item label="股权资产负债项目" :class="[handleItemDiff('assetDebtProject')]"><k-field-select v-model="formData.assetDebtProject" :data-allowblank="false" data-dict="pbc_sh_balance_proj" data-dict-type="1"/></k-form-item>
               <k-form-item label="其他股权投资内部编码" :class="[handleItemDiff('scrCd')]"><k-field-text v-model="formData.scrCd" :data-allowblank="false"/></k-form-item>
               <k-form-item label="标的企业名称" :class="[handleItemDiff('scrOrgNm')]"><k-field-text v-model="formData.scrOrgNm" :data-allowblank="false"/></k-form-item>
               <k-form-item label="标的企业代码" :class="[handleItemDiff('scrOrgCd')]"><k-field-text v-model="formData.scrOrgCd" :data-allowblank="false"/></k-form-item>
               <k-form-item label="地区代码_资管" :class="[handleItemDiff('orgBlgZon')]"><k-field-text v-model="formData.orgBlgZon" :data-allowblank="false" /></k-form-item>
               <k-form-item label="行业信息" :class="[handleItemDiff('orgBlgIndustry')]"><k-field-select v-model="formData.orgBlgIndustry" :data-allowblank="false" data-dict="isuOrgBlgIdt01" data-dict-type="1"/></k-form-item>
               <k-form-item label="企业出资人经济成分" :class="[handleItemDiff('orgTypEcn')]"><k-field-select v-model="formData.orgTypEcn" :data-allowblank="false" data-dict="pbc_eco_comp" data-dict-type="1" /></k-form-item>
               <k-form-item label="企业规模_资管" :class="[handleItemDiff('orgTypScale')]"><k-field-select v-model="formData.orgTypScale" :data-allowblank="false" data-dict="pbc_enterprise_scale" data-dict-type="1"/></k-form-item>
               <k-form-item label="股权投资方式" :class="[handleItemDiff('rightInvestWay')]"><k-field-select v-model="formData.rightInvestWay" :data-allowblank="false" data-dict="right_invest_way" data-dict-type="1"/></k-form-item>
               <k-form-item label="股权出让方代码" :class="[handleItemDiff('rightOrgCd')]"><k-field-text v-model="formData.rightOrgCd" :data-allowblank="false"/></k-form-item>
               <k-form-item label="股权出让方名称" :class="[handleItemDiff('rightOrgNm')]"><k-field-text v-model="formData.rightOrgNm" :data-allowblank="false"/></k-form-item>
               <k-form-item label="合同币种" :class="[handleItemDiff('ccyCd')]"><k-field-text v-model="formData.ccyCd" :data-allowblank="false"/></k-form-item>
               <k-form-item label="合同金额" :class="[handleItemDiff('amount')]"><k-field-text v-model="formData.amount" :data-allowblank="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="合同金额折人民币" :class="[handleItemDiff('amountCny')]"><k-field-text v-model="formData.amountCny" :data-allowblank="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="其他股权余额币种" :class="[handleItemDiff('rightCcyCd')]"><k-field-text v-model="formData.rightCcyCd" :data-allowblank="false"/></k-form-item>
               <k-form-item label="其他股权余额" :class="[handleItemDiff('rightAmount')]"><k-field-text v-model="formData.rightAmount" :data-allowblank="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="其他股权余额折人民币" :class="[handleItemDiff('rightAmountCny')]"><k-field-text v-model="formData.rightAmountCny" :data-allowblank="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="持股比例" :class="[handleItemDiff('posRat')]"><k-field-text v-model="formData.posRat" data-validate-type="number" :data-max-length="12" data-digits="4"/></k-form-item>
               <k-form-item label="投资退出方式" :class="[handleItemDiff('investExtWay')]"><k-field-select v-model="formData.investExtWay" :data-allowblank="false" data-dict='invest_exit_way' data-dict-type="1"/></k-form-item>
               <k-form-item label="合同起始日期_资管" :class="[handleItemDiff('bgnDt')]"><k-field-text v-model="formData.bgnDt" :data-allowblank="false"/></k-form-item>
               <k-form-item label="合同预计终止日期" :class="[handleItemDiff('mtuDt')]"><k-field-text v-model="formData.mtuDt" /></k-form-item>
               <k-form-item label="合同展期到期日期" :class="[handleItemDiff('deferMtuDt')]"><k-field-text v-model="formData.deferMtuDt"/></k-form-item>
                <k-form-footer data-align="center" slot="footer">
                <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ZG13.updateZG13" data-from="editZG13Form"
                    :data-model="formData" data-target="ZG13Grid" :handle-before="handleBefore">
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
              <k-field-date v-model="beginDate" data-type="month" data-date-format="yyyy-MM" data-value-format="yyyy-MM" :data-allowblank="false"></k-field-date>
      </k-form-item>
      <k-form-item label="报送数据导入" data-ui="element" data-input-width="500px">
        <k-field-excel-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit='1'
            data-accept=".xlsx,.xls"
            :data-error="onSubmitError" :data-success="onSubmitSuccess"
            :data-auto-upload="false"
            data-upload-url="/upload/server/RptApp/rhzg/uploadZG13.json">
        </k-field-excel-upload>
      </k-form-item>
      <k-form-footer data-align="center">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="ZG13Grid" ref="submitBtn"
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
import Tools from '@/utils/tools.js';
import ZgMixin from "@/pages/report/rhzg/zgMixin.js";
export default {
    name: "M07RHZG13",
    mixins: [ZgMixin],
    data () {
        return {
            uploadBeginDate: '',
            uploadQueryDate: '',
            activeName: '1',
            prodSearchParam: {
                reportDate: Tools.getPreviousMonth(),
                prodCd: '',
                theoryReportStartDate: localStorage.getItem('currentWorkday'),
                id: '',
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
          this.$refs.editZG13Popup.close();
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
          this.$refs.ZG13Grid.load(this.queryParam);
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
          this.$refs.ZG13Grid.load(this.prodSearchParam)

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
