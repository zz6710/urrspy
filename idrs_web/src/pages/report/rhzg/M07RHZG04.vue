
<template>
  <div class="py-page">

          <k-form-search-customize ref="searchRef" data-model-name="ZG04" data-target="ZG04Grid" v-model="prodSearchParam">
            <k-form-item label="数据日期">
              <k-field-date v-model="prodSearchParam.reportDate" data-type="month" data-date-format="yyyy-MM" data-value-format="yyyyMM" :data-allowblank="false"/>
            </k-form-item>
            <k-form-item label="产品代码_资管">
              <k-field-text v-model="prodSearchParam.prodCd" data-validate-type="text"/>
            </k-form-item>
            <k-form-item label="地区">
              <k-field-select v-model="prodSearchParam.clcSourceZonCd" data-action="ZG04.addclcSourceZonCdDict" :dataRemote="true"
              :data-params="{'clcSourceCustTyp':'','clcSourceZonCd':''}"
              data-value-field="VALUE" data-display-field="TEXT" data-dict-type="1" />
            </k-form-item>
            <k-form-item label="客户类型_资管">
              <k-field-select v-model="prodSearchParam.clcSourceCustTyp" data-dict="pbc_org_typ" data-dict-type="1"/>
            </k-form-item>
          </k-form-search-customize>
          <div class="py-page-container">
            <div class="table-top-btns">
              <div class="left">
                <k-btn ref="assetsZG04Send" slot="button" class="btn-custom-plain"
                         data-descript="数据报送" data-size="small" @click="creatZipFile('ZG04')">
                          <md-icon>cloud_download</md-icon>
                         数据报送
                       </k-btn>
                <k-btn slot="button" class="btn-custom-plain" data-target="ZG04Grid" :data-export-name="exportName('ZG04', '资管产品存续期募集信息')"
              data-descript="报送数据导出" data-functype="EXPORT" data-size="small"
              data-url="ZG04.findZG04s" data-export-form="searchRef">
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
            <k-grid ref="ZG04Grid" @data-row-select="selectRow" data-action="ZG04.findZG04s" data-fixed="right" data-operate-width="120px" :data-autoload="false" data-dict-type="1">
               <k-grid-column data-align="left" data-header="数据日期" data-name="reportDate" data-width="120" data-export="false"></k-grid-column>
               <k-grid-column data-align="left" data-header="产品代码_资管" data-name="prodCd" data-width="150"></k-grid-column>
               <k-grid-column data-align="left"  data-header="地区" data-name="clcSourceZonCd" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="客户类型_资管" data-name="clcSourceCustTyp" data-dict="pbc_org_typ" data-width="130"></k-grid-column>
               <k-grid-column data-align="left" data-header="币种_资管0304表" data-name="transCcy" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="当期申购金额" data-name="curPchAmt" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="当期申购金额折人民币" data-name="curPchAmtCny" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="当期申购份额" data-name="curPchLot" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="当期兑付/赎回金额" data-name="curCallAmt" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="当期兑付/赎回金额折人民币" data-name="curCallAmtCny" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="当期兑付/赎回份额" data-name="curCallLot" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="期末产品金额" data-name="endProdAmt" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="期末产品金额折人民币" data-name="endProdAmtCny" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="期末产品份额" data-name="endProdLot" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="净值型产品期末净值" data-name="netvalProdEndNav" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="净值型产品期末净值折人民币" data-name="netvalProdEndNavCny" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="净值型产品期末累计净值" data-name="netvalProdEndAcmNav" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="净值型产品期末累计净值折人民币" data-name="netvalProdEndAcmNavCny" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="产品期末业绩表现" data-name="prodEndAnlYld" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="当月年化收益率" data-name="monthEndAnlYld" data-width="150"></k-grid-column>
                <template slot="operate" slot-scope="scope">
                <k-btn class="btn-custom-text" data-descript="修改" data-functype="POPUP" data-size="mini"
                    data-target="editZG04Popup">
                    修改
                </k-btn>
                <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="ZG04.deleteZG04" data-size="mini"
                    data-type="danger" data-target="ZG04Grid" :data-confirm="true" data-descript="删除">
                    删除
                </k-btn>
                </template>
            </k-grid>
          </div>
          <!--    修改信息弹出框   -->
          <k-popup ref="editZG04Popup" data-title="修改">
            <k-form ref="editZG04Form" :data-col="2" isFormBodyScreen>
                <k-form-item label="数据日期" :class="[handleItemDiff('reportDate')]">
                                <k-field-date v-model="formData.reportDate" :data-allowblank="true" :data-disabled="true" data-type="date" data-value-format="yyyyMMdd"  />
                                 </k-form-item>
                                <k-form-item label="产品代码_资管" :class="[handleItemDiff('prodCd')]">
                                    <k-field-text v-model="formData.prodCd"  :data-allowblank="false" :data-disabled="true"/>
                                </k-form-item>
                                <k-form-item label="地区" :class="[handleItemDiff('clcSourceZonCdText')]"><k-field-select v-model="formData.clcSourceZonCdText" data-action="ZG04.addclcSourceZonCdDict" :dataRemote="true"  :data-params="{'clcSourceCustTyp':formData.clcSourceCustTyp,'clcSourceZonCd':formData.clcSourceZonCdText}" data-value-field="VALUE" data-display-field="VALUE,TEXT"   :data-allowblank="true" :data-disabled="false" /></k-form-item>
                               <k-form-item label="客户类型_资管" :class="[handleItemDiff('clcSourceCustTyp')]"><k-field-select v-model="formData.clcSourceCustTyp" :data-allowblank="true" :data-disabled="false" data-dict="pbc_org_typ" data-dict-type="1"/></k-form-item>
                                <k-form-item label="币种_资管0304表" :class="[handleItemDiff('transCcy')]"><k-field-text v-model="formData.transCcy" :data-allowblank="true" :data-disabled="false" :data-max-length="3"/></k-form-item>
                                <k-form-item label="当期申购金额" :class="[handleItemDiff('curPchAmt')]"><k-field-text v-model="formData.curPchAmt" :data-allowblank="true" :data-disabled="false" data-validate-type="number"  :data-max-length="20"  data-digits="2"/></k-form-item>
                               <k-form-item label="当期申购金额折人民币" :class="[handleItemDiff('curPchAmtCny')]"><k-field-text v-model="formData.curPchAmtCny" :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="20"   data-digits="2"/></k-form-item>
                                <k-form-item label="当期申购份额" :class="[handleItemDiff('curPchLot')]"><k-field-text v-model="formData.curPchLot" :data-allowblank="true" :data-disabled="false" data-validate-type="number"  :data-max-length="20"   data-digits="2"/></k-form-item>
                                <k-form-item label="当期兑付/赎回金额" :class="[handleItemDiff('curCallAmt')]"><k-field-text v-model="formData.curCallAmt" :data-allowblank="true" :data-disabled="false" data-validate-type="number"  :data-max-length="20"   data-digits="2"/></k-form-item>
                               <k-form-item label="当期兑付/赎回金额折人民币" :class="[handleItemDiff('curCallAmtCny')]"><k-field-text v-model="formData.curCallAmtCny" :data-allowblank="true" :data-disabled="false" data-validate-type="number"  :data-max-length="20"   data-digits="2"/></k-form-item>
                                <k-form-item label="当期兑付/赎回份额" :class="[handleItemDiff('curCallLot')]"><k-field-text v-model="formData.curCallLot" :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="20"   data-digits="2"/></k-form-item>
                                <k-form-item label="期末产品金额" :class="[handleItemDiff('endProdAmt')]"><k-field-text v-model="formData.endProdAmt" :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="20"   data-digits="2"/></k-form-item>
                                <k-form-item label="期末产品金额折人民币" :class="[handleItemDiff('endProdAmtCny')]"><k-field-text v-model="formData.endProdAmtCny" :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="20"   data-digits="2"/></k-form-item>
                                <k-form-item label="期末产品份额" :class="[handleItemDiff('endProdLot')]"><k-field-text v-model="formData.endProdLot" :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="20"  data-digits="2"/></k-form-item>
                                <k-form-item label="净值型产品期末净值" :class="[handleItemDiff('netvalProdEndNav')]"><k-field-text v-model="formData.netvalProdEndNav" :data-allowblank="true" :data-disabled="false" data-validate-type="number"  :data-max-length="20"  data-digits="8"/></k-form-item>
                                <k-form-item label="净值型产品期末净值折人民币" :class="[handleItemDiff('netvalProdEndNavCny')]"><k-field-text v-model="formData.netvalProdEndNavCny" :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="20"   data-digits="8"/></k-form-item>
                                <k-form-item label="净值型产品期末累计净值" :class="[handleItemDiff('netvalProdEndAcmNav')]"><k-field-text v-model="formData.netvalProdEndAcmNav" :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="20"   data-digits="8"/></k-form-item>
                                <k-form-item label="净值型产品期末累计净值折人民币" :class="[handleItemDiff('netvalProdEndAcmNavCny')]"><k-field-text v-model="formData.netvalProdEndAcmNavCny" :data-allowblank="true" :data-disabled="false" data-validate-type="number"  :data-max-length="20"   data-digits="8"/></k-form-item>
                                <k-form-item label="产品期末业绩表现" :class="[handleItemDiff('prodEndAnlYld')]"><k-field-text v-model="formData.prodEndAnlYld" :data-allowblank="true" :data-disabled="false" data-validate-type="number"  :data-max-length="10"   data-digits="5"/></k-form-item>
                                <k-form-item label="当月年化收益率" :class="[handleItemDiff('monthEndAnlYld')]"><k-field-text v-model="formData.monthEndAnlYld" :data-allowblank="true" :data-disabled="false" data-validate-type="number"  :data-max-length="10"   data-digits="5"/></k-form-item>


                <k-form-footer data-align="center" slot="footer">
                <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ZG04.updateZG04" data-from="editZG04Form"
                    :data-model="formData" data-target="ZG04Grid" :handle-before="handleBefore">
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
        <k-field-date v-model="reportDate" data-type="month" data-date-format="yyyy-MM" data-value-format="yyyy-MM" :data-allowblank="false"></k-field-date>
      </k-form-item>
      <k-form-item label="报送数据导入" data-ui="element" data-input-width="500px">
        <k-field-excel-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit='1'
            data-accept=".xlsx,.xls"
            :data-error="onSubmitError" :data-success="onSubmitSuccess"
            :data-auto-upload="false"
            data-upload-url="/upload/server/RptApp/rhzg/uploadZG04.json">
        </k-field-excel-upload>
      </k-form-item>
      <k-form-footer data-align="center">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="ZG04Grid" ref="submitBtn"
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
    name: "M07RHZG04",
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
                theoryReportStartDate:'',
                id:'',
                clcSourceZonCd:'',
                clcSourceCustTyp:'',
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
      if(this.$route.query.directedData && this.$route.query.directedData.dataId){
           this.$set(this.prodSearchParam, 'theoryReportStartDate', '');
          this.directedData = this.$route.query.directedData
          this.prodSearchParam.id = this.$route.query.directedData.dataId;
           this.$nextTick(()=>{
               this.$refs.ZG04Grid.load(this.prodSearchParam);
            });
      }
    },
    activated() {
        if(this.$route.query.directedData && this.$route.query.directedData.dataId){
             this.$set(this.prodSearchParam, 'theoryReportStartDate', '');
            this.directedData = this.$route.query.directedData
            this.prodSearchParam.id = this.$route.query.directedData.dataId;
             this.$nextTick(()=>{
                 this.$refs.ZG04Grid.load(this.prodSearchParam);
              });
        }
     },
    mounted() {
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
          this.$refs.editZG04Popup.close();
          return false
        }
        return true
      },
       submitUploadParam() {
        //文件上传校验
        let validate = this.$refs.addForm.validate();
        if (validate) {
          let formData = { reportDate: this.lastDayReportDate };
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
          this.$refs.ZG04Grid.load(this.prodSearchParam);
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
          this.$refs.ZG04Grid.load(this.prodSearchParam)

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
      },
  }

}
</script>
