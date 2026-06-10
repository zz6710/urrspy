
<template>
  <div class="py-page">

          <k-form-search-customize ref="searchRef" data-model-name="ZG07" data-target="ZG07Grid" v-model="queryParam">
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
                <k-btn ref="assetsZG07Send" slot="button" class="btn-custom-plain"
                         data-descript="数据报送" data-size="small" @click="creatZipFile('ZG07')">
                          <md-icon>cloud_download</md-icon>
                         数据报送
                       </k-btn>
                <k-btn slot="button" class="btn-custom-plain" data-target="ZG07Grid" :data-export-name="exportName('ZG07', '除回购和拆借外贷款明细信息')"
              data-descript="报送数据导出" data-functype="EXPORT" data-size="small"
              data-url="ZG07.findZG07s" data-export-form="searchRef">
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
            <k-grid ref="ZG07Grid" @data-row-select="selectRow" data-action="ZG07.findZG07s" data-fixed="right" data-operate-width="120px" :data-autoload="false" data-dict-type="1">
                <k-grid-column data-header="数据日期" data-name="reportDate" data-width="120" data-export="false"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品代码_资管" data-name="prodCd"  data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="贷款种类" data-name="loanTyp" data-dict="pbc_loan_typ" data-width="130"></k-grid-column>
                <k-grid-column data-align="left" data-header="贷款转让方机构代码" data-name="loanTransOrgCd" data-width="130"></k-grid-column>
                <k-grid-column data-align="left" data-header="贷款合同原始发放机构代码" data-name="loanContractOriCd" data-width="130"></k-grid-column>
                <k-grid-column data-align="left" data-header="贷款合同原始发放机构所在地代码" data-name="loanContractOriZone" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="借款人类型" data-name="borrowerTyp" data-dict="pbc_org_typ" data-width="130"></k-grid-column>
                <k-grid-column data-align="left" data-header="地区代码_资管" data-name="zone"  data-width="170"></k-grid-column>
                <k-grid-column data-align="left" data-header="借款人代码" data-name="borrowerCd" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="行业信息" data-name="industryMsg" data-dict="pbc_eco_inds_typ" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="企业出资人经济成分" data-name="enterSponsorEcoSector" data-dict="pbc_eco_comp" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="企业规模_资管" data-name="enterScale" data-dict="pbc_enterprise_scale" data-width="130"></k-grid-column>
                <k-grid-column data-align="left" data-header="贷款借据编码_资管" data-name="loanReceiptCd" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="贷款产品类别_资管" data-name="loanProdTyp" data-dict="pbc_loan_prd_typ" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="贷款实际投向_资管" data-name="loanRealTrans" data-dict="pbc_eco_inds_typ_det" data-width="160"></k-grid-column>
                <k-grid-column data-align="left" data-header="贷款发放日期_资管" data-name="loanIssueDt" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="贷款到期日期_资管" data-name="loanEndDt"  data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="贷款展期到期日期_资管" data-name="loanExtensionDt"  data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="利率是否固定_资管07表" data-name="isFixedRate"  data-dict="pbc_rate_typ" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="利率水平_资管" data-name="rateLevel" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="贷款担保方式_资管" data-name="guaranteeMode" data-dict="pbc_loan_grt_typ" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="贷款质量_资管" data-name="loanQuality" data-dict="pbc_loan_quality" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="贷款状态_资管" data-name="loanStatus" data-dict="pbc_loan_status" data-width="100" ></k-grid-column>
                <k-grid-column data-align="left" data-header="贷款转让折扣率" data-name="loanTransDiscountRate" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="原始合同币种" data-name="oriContractCcy" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="原始合同金额" data-name="oriContractAmt" data-type="money" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="原始合同金额折人民币" data-name="oriContractAmtCny" data-type="money" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="贷款余额币种" data-name="loanBalanceCcy" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="贷款余额_资管" data-name="loanBalance" data-type="money" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="贷款余额折人民币_资管" data-name="loanBalanceCny" data-type="money" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="科技相关产业标识" data-name="techFlag" data-dict="1yes0no" data-width="140"></k-grid-column>
                <k-grid-column data-align="left" data-header="绿色领域标识" data-name="greenFlag" data-dict="1yes0no" data-width="140"></k-grid-column>
                <k-grid-column data-align="left" data-header="普惠领域标识" data-name="specFlag" data-dict="1yes0no" data-width="140"></k-grid-column>
                <k-grid-column data-align="left" data-header="养老产业标识" data-name="agedFlag" data-dict="1yes0no" data-width="140"></k-grid-column>
                <k-grid-column data-align="left" data-header="数字经济核心产业标识" data-name="numCoreFlag" data-dict="1yes0no" data-width="140"></k-grid-column>		
                <template slot="operate" slot-scope="scope">
                <k-btn class="btn-custom-text" data-descript="修改" data-functype="POPUP" data-size="mini"
                    data-target="editZG07Popup">
                    修改
                </k-btn>
                <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="ZG07.deleteZG07" data-size="mini"
                    data-type="danger" data-target="ZG07Grid" :data-confirm="true" data-descript="删除">
                    删除
                </k-btn>
                </template>
            </k-grid>
          </div>
          <!--    修改特定目的载体交易对手明细信息弹出框   -->
          <k-popup ref="editZG07Popup" data-title="修改">
            <k-form ref="editZG07Form" :data-col="2" isFormBodyScreen data-label-width="230px">
                 <k-form-item label="数据日期" :class="[handleItemDiff('reportDate')]">
                     <k-field-date v-model="formData.reportDate" :data-allowblank="true" :data-disabled="true" data-type="date" data-value-format="yyyyMMdd"  />
                 </k-form-item>
                <k-form-item label="产品代码_资管" :class="[handleItemDiff('prodCd')]">
                    <k-field-text v-model="formData.prodCd"  :data-allowblank="false" :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="贷款种类" :class="[handleItemDiff('loanTyp')]">
                    <k-field-select v-model="formData.loanTyp" :data-allowblank="false"  data-dict="pbc_loan_typ" data-dict-type="1"/>
                </k-form-item>
                <k-form-item label="贷款转让方机构代码" :class="[handleItemDiff('loanTransOrgCd')]">
                    <k-field-text v-model="formData.loanTransOrgCd" :data-max-length="14" :data-allowblank="true" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="贷款合同原始发放机构代码" :class="[handleItemDiff('loanContractOriCd')]">
                    <k-field-text v-model="formData.loanContractOriCd" :data-max-length="14" :data-allowblank="true" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="贷款合同原始发放机构所在地代码" :class="[handleItemDiff('loanContractOriZone')]">
                    <k-field-text v-model="formData.loanContractOriZone" :data-max-length="6" :data-allowblank="true" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="借款人类型" :class="[handleItemDiff('borrowerTyp')]">
                    <k-field-select v-model="formData.borrowerTyp" :data-allowblank="false" data-dict="pbc_org_typ" data-dict-type="1"/>
                </k-form-item>
                <k-form-item label="地区代码_资管" :class="[handleItemDiff('zoneText')]">
                    <k-field-select v-model="formData.zoneText" :data-allowblank="true" :data-disabled="false" data-action="ZG07.addclcSourceZonCdDict" :dataRemote="true" :dataRemotePaging="true" :data-params="{'clcSourceCustTyp':formData.borrowerTyp,'clcSourceZonCd':formData.zoneText}" data-value-field="VALUE" data-display-field="VALUE,TEXT" />
                </k-form-item>
                 <k-form-item label="借款人代码" :class="[handleItemDiff('borrowerCd')]">
                    <k-field-text v-model="formData.borrowerCd" :data-max-length="100" :data-allowblank="false"/>
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
                 <k-form-item label="贷款借据编码_资管" :class="[handleItemDiff('loanReceiptCd')]">
                    <k-field-text v-model="formData.loanReceiptCd"  :data-max-length="100" :data-allowblank="false" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="贷款产品类别_资管" :class="[handleItemDiff('loanProdTyp')]">
                    <k-field-select v-model="formData.loanProdTyp" data-dict="pbc_loan_prd_typ" data-dict-type="1" :data-allowblank="false"/>
                </k-form-item>
                <k-form-item label="贷款实际投向_资管" :class="[handleItemDiff('loanRealTrans')]">
                       <k-field-select v-model="formData.loanRealTrans" data-dict="pbc_eco_inds_typ_det" data-dict-type="1" :data-allowblank="false"/>
                   </k-form-item>
                   <k-form-item label="贷款发放日期_资管" :class="[handleItemDiff('loanIssueDt')]">
                       <k-field-date v-model="formData.loanIssueDt" data-type="date" data-value-format="yyyy-MM-dd" data-date-format="yyyy-MM-dd" :data-allowblank="false"/>
                   </k-form-item>
                   <k-form-item label="贷款到期日期_资管" :class="[handleItemDiff('loanEndDt')]">
                        <k-field-date v-model="formData.loanEndDt" data-type="date" data-value-format="yyyy-MM-dd" data-date-format="yyyy-MM-dd" :data-allowblank="false"/>
                   </k-form-item>
                   <k-form-item label="贷款展期到期日期_资管" :class="[handleItemDiff('loanExtensionDt')]">
                        <k-field-date v-model="formData.loanExtensionDt" data-type="date" data-value-format="yyyy-MM-dd" data-date-format="yyyy-MM-dd"  :data-allowblank="true"/>
                   </k-form-item>
                   <k-form-item label="利率是否固定_资管07表" :class="[handleItemDiff('isFixedRate')]">
                       <k-field-select v-model="formData.isFixedRate" data-dict="pbc_rate_typ" data-dict-type="1" :data-allowblank="false"/>
                   </k-form-item>
                   <k-form-item label="利率水平_资管" :class="[handleItemDiff('rateLevel')]">
                       <k-field-text v-model="formData.rateLevel"  data-validate-type="number" :data-max-length="10" data-digits="5" :data-allowblank="false"/>
                   </k-form-item>
                    <k-form-item label="贷款担保方式_资管" :class="[handleItemDiff('guaranteeMode')]">
                       <k-field-select v-model="formData.guaranteeMode" data-dict="pbc_loan_grt_typ" data-dict-type="1" :data-allowblank="false"/>
                   </k-form-item>
                   <k-form-item label="贷款质量_资管" :class="[handleItemDiff('loanQuality')]">
                       <k-field-select v-model="formData.loanQuality" data-dict="pbc_loan_quality" data-dict-type="1" :data-allowblank="false" :data-disabled="false"/>
                   </k-form-item>
                    <k-form-item label="贷款状态_资管" :class="[handleItemDiff('loanStatus')]">
                       <k-field-select v-model="formData.loanStatus"  data-dict="pbc_loan_status" data-dict-type="1"  :data-allowblank="false" :data-disabled="false"/>
                   </k-form-item>
                   <k-form-item label="贷款转让折扣率" :class="[handleItemDiff('loanTransDiscountRate')]">
                       <k-field-text v-model="formData.loanTransDiscountRate" :data-allowblank="true" :data-disabled="false"  data-validate-type="number" :data-max-length="10" data-digits="5"/>
                   </k-form-item>
                    <k-form-item label="原始合同币种" :class="[handleItemDiff('oriContractCcy')]">
                       <k-field-text v-model="formData.oriContractCcy"  :data-max-length="3" :data-allowblank="false"/>
                   </k-form-item>
                   <k-form-item label="原始合同金额" :class="[handleItemDiff('oriContractAmt')]">
                       <k-field-text v-model="formData.oriContractAmt"  data-validate-type="number" :data-max-length="20" data-digits="2" :data-allowblank="false"/>
                   </k-form-item>
                    <k-form-item label="原始合同金额折人民币" :class="[handleItemDiff('oriContractAmtCny')]">
                        <k-field-text v-model="formData.oriContractAmtCny"  data-validate-type="number" :data-max-length="20" data-digits="2" :data-allowblank="false"/>
                    </k-form-item>
                    <k-form-item label="贷款余额币种" :class="[handleItemDiff('loanBalanceCcy')]">
                        <k-field-text v-model="formData.loanBalanceCcy"  :data-max-length="3" :data-allowblank="false"/>
                    </k-form-item>
                     <k-form-item label="贷款余额_资管" :class="[handleItemDiff('loanBalance')]">
                         <k-field-text v-model="formData.loanBalance"  data-validate-type="number" :data-max-length="20" data-digits="2" :data-allowblank="false"/>
                     </k-form-item>
                     <k-form-item label="贷款余额折人民币_资管" :class="[handleItemDiff('loanBalanceCny')]">
                         <k-field-text v-model="formData.loanBalanceCny"  data-validate-type="number" :data-max-length="20" data-digits="2" :data-allowblank="false"/>
                     </k-form-item>
                      <!--追加金融“五篇大文章”数据标识 程晓鹏 20250304 modify-->
                      <k-form-item label="科技相关产业标识" :class="[handleItemDiff('techFlag')]">
                        <k-field-select v-model="formData.techFlag" data-dict="1yes0no" data-dict-type="1" />
                      </k-form-item>
                      <k-form-item label="绿色领域标识" :class="[handleItemDiff('greenFlag')]">
                        <k-field-select v-model="formData.greenFlag" data-dict="1yes0no" data-dict-type="1" />
                      </k-form-item>
                      <k-form-item label="普惠领域标识" :class="[handleItemDiff('specFlag')]">
                        <k-field-select v-model="formData.specFlag" data-dict="1yes0no" data-dict-type="1" />
                      </k-form-item>
                      <k-form-item label="养老产业标识" :class="[handleItemDiff('agedFlag')]">
                        <k-field-select v-model="formData.agedFlag" data-dict="1yes0no" data-dict-type="1" />
                      </k-form-item>
                      <k-form-item label="数字经济核心产业标识" :class="[handleItemDiff('numCoreFlag')]">
                        <k-field-select v-model="formData.numCoreFlag" data-dict="1yes0no" data-dict-type="1" />
                      </k-form-item>
                <k-form-footer data-align="center" slot="footer">
                <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ZG07.updateZG07" data-from="editZG07Form"
                    :data-model="formData" data-target="ZG07Grid" :handle-before="handleBefore">
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
            data-upload-url="/upload/server/RptApp/rhzg/uploadZG07.json">
        </k-field-excel-upload>
      </k-form-item>
      <k-form-footer data-align="center">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="ZG07Grid" ref="submitBtn"
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
    name: "M07RHZG07",
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
      if(this.$route.query.directedData && this.$route.query.directedData.dataId){
           this.$set(this.prodSearchParam, 'theoryReportStartDate', '');
           this.directedData = this.$route.query.directedData
           this.prodSearchParam.id = this.$route.query.directedData.dataId;
           this.$nextTick(()=>{
              this.$refs.ZG07Grid.load(this.prodSearchParam);
           });
      }
    },
    activated() {
      if(this.$route.query.directedData && this.$route.query.directedData.dataId){
                 this.$set(this.prodSearchParam, 'theoryReportStartDate', '');
                 this.directedData = this.$route.query.directedData
                 this.prodSearchParam.id = this.$route.query.directedData.dataId;
                 this.$nextTick(()=>{
                    this.$refs.ZG07Grid.load(this.prodSearchParam);
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
          this.$refs.editZG07Popup.close();
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
          this.$refs.ZG07Grid.load(this.prodSearchParam);
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
          this.$refs.ZG07Grid.load(this.prodSearchParam)

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
