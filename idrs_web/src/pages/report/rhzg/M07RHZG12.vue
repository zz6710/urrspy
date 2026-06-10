
<template>
  <div class="py-page">
          <k-form-search-customize ref="searchRef" data-model-name="ZG12" data-target="ZG12Grid" v-model="queryParam">
            <k-form-item label="数据日期" data-label-width="150px">
              <k-field-date v-model="prodSearchParam.reportDate" data-type="month" data-date-format="yyyy-MM" data-value-format="yyyyMM" :data-allowblank="false"/>
            </k-form-item>
            <k-form-item label="产品代码_资管" data-label-width="150px">
              <k-field-text v-model="prodSearchParam.prodCd" data-validate-type="text"/>
            </k-form-item>
          </k-form-search-customize>
          <div class="py-page-container">
            <div class="table-top-btns">
              <div class="left">
                <k-btn ref="assetsZG12Send" slot="button" class="btn-custom-plain"
                  data-descript="数据报送" data-size="small"  @click="creatZipFile('ZG12')">
                          <md-icon>cloud_download</md-icon>
                         数据报送
                       </k-btn>
            <k-btn slot="button" class="btn-custom-plain" data-target="ZG12Grid" :data-export-name="exportName('ZG12', '除资产收益权外其他债权明细信息')"
              data-descript="报送数据导出" data-functype="EXPORT" data-size="small"
              data-url="ZG12.findZG12s" data-export-form="searchRef">
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
            <k-grid ref="ZG12Grid" @data-row-select="selectRow" data-action="ZG12.findZG12s" data-fixed="right" data-operate-width="120px" :data-autoload="false" data-dict-type="1">
                <k-grid-column data-header="数据日期" data-name="reportDate" data-width="120" data-export="false"></k-grid-column>
                                <k-grid-column data-align="left" data-header="产品代码_资管" data-name="prodCd"  data-width="150"></k-grid-column>
                                <k-grid-column data-align="left" data-header="借款人类型" data-name="borrowerTyp" data-dict="pbc_org_typ" data-width="130"></k-grid-column>
                                <k-grid-column data-align="left" data-header="地区代码_资管" data-name="zone"  data-width="170"></k-grid-column>
                                <k-grid-column data-align="left" data-header="借款人代码" data-name="borrowerCd" data-width="150"></k-grid-column>
                                <k-grid-column data-align="left" data-header="行业信息" data-name="industryMsg" data-dict="pbc_eco_inds_typ" data-width="100"></k-grid-column>
                                <k-grid-column data-align="left" data-header="企业出资人经济成分" data-name="enterSponsorEcoSector" data-dict="pbc_eco_comp" data-width="150"></k-grid-column>
                                <k-grid-column data-align="left" data-header="企业规模_资管" data-name="enterScale" data-dict="pbc_enterprise_scale" data-width="130"></k-grid-column>
                                <k-grid-column data-align="left" data-header="除资产收益权外其他债权内部编码" data-name="borrowerAssetCd" data-width="150"></k-grid-column>
                                <k-grid-column data-align="left" data-header="除资产收益权外其他债权实际投向" data-name="borrowerRealTrans" data-dict="pbc_eco_inds_typ_det" data-width="160"></k-grid-column>
                                <k-grid-column data-align="left" data-header="除资产收益权外其他债权起始日期" data-name="borrowerIssueDt" data-width="120"></k-grid-column>
                                <k-grid-column data-align="left" data-header="除资产收益权外其他债权预计到期日期" data-name="borrowerEndDt"  data-width="120"></k-grid-column>
                                <k-grid-column data-align="left" data-header="除资产收益权外其他债权展期到期日期" data-name="borrowerExtensionDt"  data-width="120"></k-grid-column>
                                <k-grid-column data-align="left" data-header="利率是否固定_资管12表" data-name="isFixedRate" data-dict="pbc_rate_typ" data-width="150"></k-grid-column>
                                <k-grid-column data-align="left" data-header="利率水平_资管" data-name="rateLevel" data-width="100"></k-grid-column>
                                <k-grid-column data-align="left" data-header="担保方式_资管12表" data-name="guaranteeMode" data-dict="pbc_loan_grt_typ" data-width="150"></k-grid-column>
                                <k-grid-column data-align="left" data-header="原始合同币种" data-name="oriContractCcy" data-width="100"></k-grid-column>
                                <k-grid-column data-align="left" data-header="原始合同金额" data-name="oriContractAmt" data-type="money" data-width="150"></k-grid-column>
                                <k-grid-column data-align="left" data-header="原始合同金额折人民币" data-name="oriContractAmtCny" data-type="money" data-width="150"></k-grid-column>
                                <k-grid-column data-align="left" data-header="除资产收益权外其他债权余额币种" data-name="borrowerBalanceCcy" data-width="100"></k-grid-column>
                                <k-grid-column data-align="left" data-header="除资产收益权外其他债权余额" data-name="borrowerBalance" data-type="money" data-width="150"></k-grid-column>
                                <k-grid-column data-align="left" data-header="除资产收益权外其他债权余额折人民币" data-name="borrowerBalanceCny" data-type="money" data-width="150"></k-grid-column>
                                <k-grid-column data-align="left" data-header="债权类型" data-name="typeOfDebt" data-dict="typeOfDebt" data-width="150"></k-grid-column>
                                <k-grid-column data-align="left" data-header="登记交易场所" data-name="registerTradingPlace" data-dict="registerTradingPlace" data-width="150"></k-grid-column>
                                <k-grid-column data-align="left" data-header="登记交易场所代码_资管产品" data-name="registerTradingCode" data-dict="registerTradingCode" data-width="150"></k-grid-column>
                                <k-grid-column data-align="left" data-header="科技相关产业标识" data-name="techFlag" data-dict="pbc_conf_flag" data-width="140"></k-grid-column>
                                <k-grid-column data-align="left" data-header="绿色领域标识" data-name="greenFlag" data-dict="pbc_conf_flag" data-width="140"></k-grid-column>
                                <k-grid-column data-align="left" data-header="普惠领域标识" data-name="specFlag" data-dict="pbc_conf_flag" data-width="140"></k-grid-column>
                                <k-grid-column data-align="left" data-header="养老产业标识" data-name="agedFlag" data-dict="pbc_conf_flag" data-width="140"></k-grid-column>
                                <k-grid-column data-align="left" data-header="数字经济核心产业标识" data-name="numCoreFlag" data-dict="pbc_conf_flag" data-width="140"></k-grid-column>			

              <template slot="operate" slot-scope="scope">
                <k-btn class="btn-custom-text" data-descript="修改" data-functype="POPUP" data-size="mini"
                       data-target="editZG12Popup">
                  修改
                </k-btn>
                <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="ZG12.deleteZG12" data-size="mini"
                       data-type="danger" data-target="ZG12Grid" :data-confirm="true" data-descript="删除">
                  删除
                </k-btn>
              </template>
            </k-grid>
          </div>
          <!--    修改特定目的载体交易对手明细信息弹出框   -->
          <k-popup ref="editZG12Popup" data-title="修改">
            <k-form ref="editZG12Form" :data-col="2" isFormBodyScreen>
                 <k-form-item label="数据日期" :class="[handleItemDiff('reportDate')]">
                     <k-field-date v-model="formData.reportDate" :data-allowblank="true" :data-disabled="true" data-type="date" data-value-format="yyyyMMdd"  />
                 </k-form-item>
                <k-form-item label="产品代码_资管" :class="[handleItemDiff('prodCd')]">
                    <k-field-text v-model="formData.prodCd"  :data-allowblank="false" :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="借款人类型" :class="[handleItemDiff('borrowerTyp')]">
                    <k-field-select v-model="formData.borrowerTyp" :data-allowblank="false" data-dict="pbc_org_typ" data-dict-type="1"/>
                </k-form-item>
                <k-form-item label="地区代码_资管" :class="[handleItemDiff('zoneText')]">
                    <k-field-select v-model="formData.zoneText" :data-allowblank="true" :data-disabled="false" data-action="ZG12.addclcSourceZonCdDict" :dataRemote="true" :dataRemotePaging="true" :data-params="{'clcSourceCustTyp':formData.borrowerTyp,'clcSourceZonCd':formData.zoneText}" data-value-field="VALUE" data-display-field="VALUE,TEXT" />
                </k-form-item>
                 <k-form-item label="借款人代码" :class="[handleItemDiff('borrowerCd')]">
                    <k-field-text v-model="formData.borrowerCd" data-max-length="100" :data-allowblank="false"/>
                </k-form-item>
                <k-form-item label="行业信息" :class="[handleItemDiff('industryMsg')]">
                    <k-field-select v-model="formData.industryMsg" data-dict="pbc_eco_inds_typ" data-dict-type="1" :data-allowblank="false"/>
                </k-form-item>
                <k-form-item label="企业出资人经济成分" :class="[handleItemDiff('enterSponsorEcoSector')]">
                    <k-field-select v-model="formData.enterSponsorEcoSector" data-dict="pbc_eco_comp" data-dict-type="1" :data-allowblank="true" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="企业规模_资管" :class="[handleItemDiff('enterScale')]">
                    <k-field-select v-model="formData.enterScale" data-dict="pbc_enterprise_scale" data-dict-type="1" :data-allowblank="true" :data-disabled="false"/>
                </k-form-item>
                 <k-form-item label="除资产收益权外其他债权内部编码" :class="[handleItemDiff('borrowerAssetCd')]">
                    <k-field-text v-model="formData.borrowerAssetCd"  data-max-length="100" :data-allowblank="true" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="除资产收益权外其他债权实际投向" :class="[handleItemDiff('borrowerRealTrans')]">
                       <k-field-select v-model="formData.borrowerRealTrans" data-dict="pbc_eco_inds_typ_det" data-dict-type="1" :data-allowblank="false"/>
                   </k-form-item>
                   <k-form-item label="除资产收益权外其他债权起始日期" :class="[handleItemDiff('borrowerIssueDt')]">
                       <k-field-date v-model="formData.borrowerIssueDt" data-type="date" data-value-format="yyyy-MM-dd" data-date-format="yyyy-MM-dd" :data-allowblank="false"/>
                   </k-form-item>
                   <k-form-item label="除资产收益权外其他债权预计到期日期" :class="[handleItemDiff('borrowerEndDt')]">
                        <k-field-date v-model="formData.borrowerEndDt" data-type="date" data-value-format="yyyy-MM-dd" data-date-format="yyyy-MM-dd" :data-allowblank="false"/>
                   </k-form-item>
                   <k-form-item label="除资产收益权外其他债权展期到期日期" :class="[handleItemDiff('borrowerExtensionDt')]">
                        <k-field-date v-model="formData.borrowerExtensionDt" data-type="date" data-value-format="yyyy-MM-dd" data-date-format="yyyy-MM-dd"  :data-allowblank="true"/>
                   </k-form-item>
                   <k-form-item label="利率是否固定_资管12表" :class="[handleItemDiff('isFixedRate')]">
                       <k-field-select v-model="formData.isFixedRate" data-dict="pbc_rate_typ" data-dict-type="1" :data-allowblank="false"/>
                   </k-form-item>
                   <k-form-item label="利率水平_资管" :class="[handleItemDiff('rateLevel')]">
                       <k-field-text v-model="formData.rateLevel"  data-validate-type="number" :data-max-length="10" data-digits="5" :data-allowblank="false"/>
                   </k-form-item>
                    <k-form-item label="担保方式_资管12表" :class="[handleItemDiff('guaranteeMode')]">
                       <k-field-select v-model="formData.guaranteeMode" data-dict="pbc_loan_grt_typ" data-dict-type="1" :data-allowblank="false"/>
                   </k-form-item>
                    <k-form-item label="原始合同币种" :class="[handleItemDiff('oriContractCcy')]">
                       <k-field-text v-model="formData.oriContractCcy"  data-max-length="3" :data-allowblank="false"/>
                   </k-form-item>
                   <k-form-item label="原始合同金额" :class="[handleItemDiff('oriContractAmt')]">
                       <k-field-text v-model="formData.oriContractAmt"  data-validate-type="number" :data-max-length="20" data-digits="2" :data-allowblank="false"/>
                   </k-form-item>
                    <k-form-item label="原始合同金额折人民币" :class="[handleItemDiff('oriContractAmtCny')]">
                        <k-field-text v-model="formData.oriContractAmtCny"  data-validate-type="number" :data-max-length="20" data-digits="2" :data-allowblank="false"/>
                    </k-form-item>
                    <k-form-item label="除资产收益权外其他债权余额币种" :class="[handleItemDiff('borrowerBalanceCcy')]">
                        <k-field-text v-model="formData.borrowerBalanceCcy"  data-max-length="3" :data-allowblank="false"/>
                    </k-form-item>
                     <k-form-item label="除资产收益权外其他债权余额" :class="[handleItemDiff('borrowerBalance')]">
                         <k-field-text v-model="formData.borrowerBalance"  data-validate-type="number" :data-max-length="20" data-digits="2" :data-allowblank="false"/>
                     </k-form-item>
                     <k-form-item label="除资产收益权外其他债权余额折人民币" :class="[handleItemDiff('borrowerBalanceCny')]">
                         <k-field-text v-model="formData.borrowerBalanceCny"  data-validate-type="number" :data-max-length="20" data-digits="2" :data-allowblank="false"/>
                     </k-form-item>
                    <k-form-item label="债权类型" :class="[handleItemDiff('typeOfDebt')]">
                      <k-field-select v-model="formData.typeOfDebt" data-dict="typeOfDebt" data-dict-type="1" :data-allowblank="false"/>
                    </k-form-item>
                    <k-form-item label="登记交易场所" :class="[handleItemDiff('registerTradingPlace')]">
                      <k-field-select v-model="formData.registerTradingPlace" data-dict="registerTradingPlace" data-dict-type="1" :data-allowblank="false"/>
                    </k-form-item>
                    <k-form-item label="登记交易场所代码_资管产品" :class="[handleItemDiff('registerTradingCode')]">
                      <k-field-select v-model="formData.registerTradingCode" data-dict="registerTradingCode" data-dict-type="1" :data-allowblank="false"/>
                    </k-form-item>
                    <!--追加金融“五篇大文章”数据标识 程晓鹏 20250304 modify-->
                    <k-form-item label="科技相关产业标识" :class="[handleItemDiff('techFlag')]">
                      <k-field-select v-model="formData.techFlag" data-dict="pbc_conf_flag" data-dict-type="1" />
                    </k-form-item>
                    <k-form-item label="绿色领域标识" :class="[handleItemDiff('greenFlag')]">
                      <k-field-select v-model="formData.greenFlag" data-dict="pbc_conf_flag" data-dict-type="1" />
                    </k-form-item>
                    <k-form-item label="普惠领域标识" :class="[handleItemDiff('specFlag')]">
                      <k-field-select v-model="formData.specFlag" data-dict="pbc_conf_flag" data-dict-type="1" />
                    </k-form-item>
                    <k-form-item label="养老产业标识" :class="[handleItemDiff('agedFlag')]">
                      <k-field-select v-model="formData.agedFlag" data-dict="pbc_conf_flag" data-dict-type="1" />
                    </k-form-item>
                    <k-form-item label="数字经济核心产业标识" :class="[handleItemDiff('numCoreFlag')]">
                      <k-field-select v-model="formData.numCoreFlag" data-dict="pbc_conf_flag" data-dict-type="1" />
                    </k-form-item>
                <k-form-footer data-align="center" slot="footer">
                <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ZG12.updateZG12" data-from="editZG12Form"
                    :data-model="formData" data-target="ZG12Grid" :handle-before="handleBefore">
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
            data-upload-url="/upload/server/RptApp/rhzg/uploadZG12.json">
        </k-field-excel-upload>
      </k-form-item>
      <k-form-footer data-align="center">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT"  ref="submitBtn"
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
    name: "M07RHZG12",
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
            dataId:'',
        }
    },
    created() {
      this.getNowDate();
      if(this.$route.query.directedData && this.$route.query.directedData.dataId){
           this.$set(this.prodSearchParam, 'theoryReportStartDate', '');
           this.directedData = this.$route.query.directedData
           this.prodSearchParam.id = this.$route.query.directedData.dataId;
           this.$nextTick(()=>{
              this.$refs.ZG12Grid.load(this.prodSearchParam);
           });
      }
    },
    activated() {
      if(this.$route.query.directedData && this.$route.query.directedData.dataId){
                 this.$set(this.prodSearchParam, 'theoryReportStartDate', '');
                 this.directedData = this.$route.query.directedData
                 this.prodSearchParam.id = this.$route.query.directedData.dataId;
                 this.$nextTick(()=>{
                    this.$refs.ZG12Grid.load(this.prodSearchParam);
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
          this.$refs.editZG12Popup.close();
          return false
        }
        return true
      },
       submitUploadParam() {
        //文件上传校验
        let validate = this.$refs.addForm.validate();
        if (validate) {
          let formData = { beginDate: this.lastDayBeginDate};
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
          this.$refs.ZG12Grid.load(this.queryParam);
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
          this.$refs.ZG12Grid.load(this.prodSearchParam)

      },
      selectRow(row, column, event) {
        this.formData = Object.assign({}, row, {zoneText: row.zone})
        this.formDataCopy = Object.assign({}, row, {zoneText: row.zone})
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
