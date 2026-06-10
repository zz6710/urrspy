
<template>
  <div class="py-page">
          <k-form-search-customize ref="searchRef" data-model-name="ZG06" data-target="ZG06Grid" v-model="queryParam" data-label-width="100px">
            <k-form-item label="数据日期">
               <k-field-date v-model="prodSearchParam.reportDate" data-type="month" data-date-format="yyyy-MM" data-value-format="yyyyMM" :data-allowblank="false"/>
            </k-form-item>
            <k-form-item label="产品代码_资管">
              <k-field-text v-model="prodSearchParam.prodCd" data-validate-type="text"/>
            </k-form-item>
            <k-form-item label="资产负债项目">
              <k-field-select v-model="prodSearchParam.assetDebtProject" data-dict="pbc_balance_proj_prt" data-dict-type="1"/>
            </k-form-item>
            <k-form-item label="资产收益权内部编码" data-label-width="150px">
              <k-field-text v-model="prodSearchParam.assetIncomeCode" data-validate-type="text"/>
            </k-form-item>
          </k-form-search-customize>
          <div class="py-page-container">
            <div class="table-top-btns">
              <div class="left">
                <k-btn ref="assetsZG06Send" slot="button" class="btn-custom-plain"
              data-descript="数据报送" data-size="small" @click="creatZipFile('ZG06')">
               <md-icon>cloud_download</md-icon>
              数据报送
            </k-btn>
                <k-btn slot="button" class="btn-custom-plain" data-target="ZG06Grid" :data-export-name="exportName('ZG06', '资产收益权明细信息')"
              data-descript="报送数据导出" data-functype="EXPORT" data-size="small"
              data-url="ZG06.findZG06s" data-export-form="searchRef">
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
            <k-grid ref="ZG06Grid" @data-row-select="selectRow" data-action="ZG06.findZG06s" data-fixed="right" data-operate-width="120px" :data-autoload="false" data-dict-type="1">
                <k-grid-column data-align="left" data-header="数据日期" data-name="reportDate" data-width="100" data-export="false"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品代码_资管" data-name="prodCd" data-width="130"></k-grid-column>
                <k-grid-column data-align="left" data-header="资产负债项目" data-name="assetDebtProject" data-width="150" data-dict="pbc_balance_proj_rev"></k-grid-column>
                <k-grid-column data-align="left" data-header="资产收益权内部编码" data-name="assetIncomeCode" data-width="130"></k-grid-column>
                <k-grid-column data-align="left" data-header="基础资产出让机构名称" data-name="baseAssetSaleOrgNm" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="基础资产出让机构代码" data-name="baseAssetSaleOrgCd" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="基础资产出让机构类型" data-name="baseAssetSaleOrgTyp"  data-dict="pbc_org_typ" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="基础资产出让机构行业" data-name="baseAssetSaleOrgIndustry" data-dict="pbc_eco_inds_typ" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="基础资产出让机构注册地区" data-name="baseAssetSaleOrgRegZone" data-width="170"></k-grid-column>
                <k-grid-column data-align="left" data-header="基础资产出让机构经济成分" data-name="baseAssetSaleOrgEconoSector" data-width="170" data-dict="pbc_eco_comp"></k-grid-column>
                <k-grid-column data-align="left" data-header="基础资产出让机构规模" data-name="baseAssetSaleOrgScale" data-width="150"  data-dict="pbc_enterprise_scale"></k-grid-column>
                <k-grid-column data-align="left" data-header="转让起始日期" data-name="transDt"  data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="转让预计终止日期" data-name="transScheduledEndDt" data-width="130"></k-grid-column>
                <k-grid-column data-align="left" data-header="转让展期到期日期" data-name="transExtensionDt"  data-width="130"></k-grid-column>
                <k-grid-column data-align="left" data-header="基础资产类型" data-name="baseAssetTyp" data-dict="pbc_asset_bas_typ" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="基础资产原始协议币种" data-name="baseAssetOriProtCcy" data-width="140"></k-grid-column>
                <k-grid-column data-align="left" data-header="基础资产原始协议金额" data-name="baseAssetOriProtAmt" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="基础资产原始协议金额折人民币" data-name="baseAssetOriProtAmtCny" data-width="190"></k-grid-column>
                <k-grid-column data-align="left" data-header="基础资产转让币种" data-name="baseAssetTransCcy" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="基础资产转让金额" data-name="baseAssetTransAmt" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="基础资产转让金额折人民币" data-name="baseAssetTransAmtCny" data-width="170"></k-grid-column>
                <k-grid-column data-align="left" data-header="出让机构出表标识" data-name="transOrgOutTableF" data-dict="pbc_conf_flag" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="出让机构回购标识" data-name="transOrgBuyBackF" data-dict="pbc_conf_flag" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="利率是否固定_资管06表" data-name="isFixedRate" data-width="120" data-dict="pbc_rate_typ"></k-grid-column>
                <k-grid-column data-align="left" data-header="利率水平_资管06表" data-name="rateLevel" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="担保方式_资管06表" data-name="guaranteeMode" data-dict="pbc_grt_typ" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="基础资产投向部门" data-name="baseAssetTransDep" data-dict="pbc_org_typ" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="基础资产期末币种" data-name="baseAssetEndDtCcy" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="基础资产期末余额" data-name="baseAssetEndDtBalance" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="基础资产期末余额折人民币" data-name="baseAssetEndDtBalanceCny" data-width="170"></k-grid-column>
                <k-grid-column data-align="left" data-header="登记交易场所" data-name="registerTradingPlace" data-dict="registerTradingPlace" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="登记交易场所代码_资管产品" data-name="registerTradingCode" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="科技相关产业标识" data-name="techFlag" data-dict="pbc_conf_flag" data-width="140"></k-grid-column>
                <k-grid-column data-align="left" data-header="绿色领域标识" data-name="greenFlag" data-dict="pbc_conf_flag" data-width="140"></k-grid-column>
                <k-grid-column data-align="left" data-header="普惠领域标识" data-name="specFlag" data-dict="pbc_conf_flag" data-width="140"></k-grid-column>
                <k-grid-column data-align="left" data-header="养老产业标识" data-name="agedFlag" data-dict="pbc_conf_flag" data-width="140"></k-grid-column>
                <k-grid-column data-align="left" data-header="数字经济核心产业标识" data-name="numCoreFlag" data-dict="pbc_conf_flag" data-width="140"></k-grid-column>			
                <template slot="operate" slot-scope="scope">
                <k-btn class="btn-custom-text" data-descript="修改" data-functype="POPUP" data-size="mini"
                    data-target="editZG06Popup">
                    修改
                </k-btn>
                <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="ZG06.deleteZG06" data-size="mini"
                    data-type="danger" data-target="ZG06Grid" :data-confirm="true" data-descript="删除">
                    删除
                </k-btn>
                </template>
            </k-grid>
          </div>
          <!--    修改资产收益权明细信息弹出框   -->
          <k-popup ref="editZG06Popup" data-title="修改">
            <k-form ref="editZG06Form" :data-col="3" isFormBodyScreen data-label-width="220px">
                <k-form-item label="数据日期" :class="[handleItemDiff('reportDate')]">
                    <k-field-date v-model="formData.reportDate" data-type="date"  data-value-format="yyyyMMdd" :data-allowblank="true" :data-disabled="true"/>
                </k-form-item>

                <k-form-item label="产品代码_资管" :class="[handleItemDiff('prodCd')]">
                    <k-field-text v-model="formData.prodCd"  :data-allowblank="false" :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="资产负债项目" :class="[handleItemDiff('assetDebtProject')]"><k-field-select v-model="formData.assetDebtProject" :data-allowblank="false" :data-disabled="false" data-dict="pbc_balance_proj_rev" data-dict-type="1"/></k-form-item>
                <k-form-item label="资产收益权内部编码" :class="[handleItemDiff('assetIncomeCode')]"><k-field-text v-model="formData.assetIncomeCode" :data-allowblank="true" :data-disabled="false"/></k-form-item>
                <k-form-item label="基础资产出让机构名称" :class="[handleItemDiff('baseAssetSaleOrgNm')]"><k-field-text v-model="formData.baseAssetSaleOrgNm" :data-allowblank="false" :data-disabled="false"/></k-form-item>
                <k-form-item label="基础资产出让机构代码" :class="[handleItemDiff('baseAssetSaleOrgCd')]"><k-field-text v-model="formData.baseAssetSaleOrgCd" :data-allowblank="false" :data-disabled="false"/></k-form-item>
                <k-form-item label="基础资产出让机构类型" :class="[handleItemDiff('baseAssetSaleOrgTyp')]"><k-field-select v-model="formData.baseAssetSaleOrgTyp" :data-allowblank="false" :data-disabled="false" data-dict="pbc_org_typ" data-dict-type="1"/></k-form-item>
                <k-form-item label="基础资产出让机构行业" :class="[handleItemDiff('baseAssetSaleOrgIndustry')]"><k-field-select v-model="formData.baseAssetSaleOrgIndustry" :data-allowblank="false" :data-disabled="false" data-dict="pbc_eco_inds_typ" data-dict-type="1"/></k-form-item>
                <k-form-item label="基础资产出让机构注册地区" :class="[handleItemDiff('zoneText')]"><k-field-select v-model="formData.zoneText" :data-allowblank="false" :data-disabled="false" data-action="ZG06.addclcSourceZonCdDict" :dataRemote="true" :dataRemotePaging="true" :data-params="{'clcSourceCustTyp':formData.baseAssetSaleOrgTyp,'clcSourceZonCd':formData.baseAssetSaleOrgRegZoneText}" data-value-field="VALUE" data-display-field="VALUE,TEXT" /></k-form-item>
                <k-form-item label="基础资产出让机构经济成分" :class="[handleItemDiff('baseAssetSaleOrgEconoSector')]"><k-field-select v-model="formData.baseAssetSaleOrgEconoSector" :data-allowblank="true" :data-disabled="false" data-dict="pbc_eco_comp" data-dict-type="1" :data-dict-filter="['A01', 'A02', 'B01', 'B02', 'B03']"/></k-form-item>
                <k-form-item label="基础资产出让机构规模" :class="[handleItemDiff('baseAssetSaleOrgScale')]"><k-field-select v-model="formData.baseAssetSaleOrgScale" :data-allowblank="true" :data-disabled="false" data-dict="pbc_enterprise_scale" data-dict-type="1"/></k-form-item>
                <k-form-item label="转让起始日期" :class="[handleItemDiff('transDt')]"><k-field-date v-model="formData.transDt" :data-allowblank="false" :data-disabled="false" data-type="date" data-value-format="yyyy-MM-dd" data-date-format="yyyy-MM-dd"/></k-form-item>
                <k-form-item label="转让预计终止日期" :class="[handleItemDiff('transScheduledEndDt')]"><k-field-date v-model="formData.transScheduledEndDt" :data-allowblank="false" :data-disabled="false" data-type="date" data-value-format="yyyy-MM-dd" data-date-format="yyyy-MM-dd"/></k-form-item>
                <k-form-item label="转让展期到期日期" :class="[handleItemDiff('transExtensionDt')]"><k-field-date v-model="formData.transExtensionDt" :data-allowblank="true" :data-disabled="false" data-type="date" data-value-format="yyyy-MM-dd" data-date-format="yyyy-MM-dd"/></k-form-item>
<!--                <k-form-item label="转让实际终止日期"><k-field-date v-model="formData.transRealEndDt" :data-allowblank="true" :data-disabled="false" data-type="date" data-value-format="yyyy-MM-dd" data-date-format="yyyy-MM-dd"/></k-form-item>-->
                <k-form-item label="基础资产类型" :class="[handleItemDiff('baseAssetTyp')]"><k-field-select v-model="formData.baseAssetTyp" :data-allowblank="false" :data-disabled="false" data-dict="pbc_asset_bas_typ" data-dict-type="1"/></k-form-item>
                <k-form-item label="基础资产原始协议币种" :class="[handleItemDiff('baseAssetOriProtCcy')]"><k-field-text v-model="formData.baseAssetOriProtCcy" :data-allowblank="false" :data-disabled="false"/></k-form-item>
                <k-form-item label="基础资产原始协议金额" :class="[handleItemDiff('baseAssetOriProtAmt')]"><k-field-text v-model="formData.baseAssetOriProtAmt" :data-allowblank="false" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="基础资产原始协议金额折人民币" :class="[handleItemDiff('baseAssetOriProtAmtCny')]"><k-field-text v-model="formData.baseAssetOriProtAmtCny" :data-allowblank="false" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="基础资产转让币种" :class="[handleItemDiff('baseAssetTransCcy')]"><k-field-text v-model="formData.baseAssetTransCcy" :data-allowblank="false" :data-disabled="false"/></k-form-item>
                <k-form-item label="基础资产转让金额" :class="[handleItemDiff('baseAssetTransAmt')]"><k-field-text v-model="formData.baseAssetTransAmt" :data-allowblank="false" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="基础资产转让金额折人民币" :class="[handleItemDiff('baseAssetTransAmtCny')]"><k-field-text v-model="formData.baseAssetTransAmtCny" :data-allowblank="false" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="出让机构出表标识" :class="[handleItemDiff('transOrgOutTableF')]"><k-field-select v-model="formData.transOrgOutTableF" :data-allowblank="false" :data-disabled="false" data-dict="pbc_conf_flag" data-dict-type="1"/></k-form-item>
                <k-form-item label="出让机构回购标识" :class="[handleItemDiff('transOrgBuyBackF')]"><k-field-select v-model="formData.transOrgBuyBackF" :data-allowblank="false" :data-disabled="false" data-dict="pbc_conf_flag" data-dict-type="1"/></k-form-item>
                <k-form-item label="利率是否固定_资管06表" :class="[handleItemDiff('isFixedRate')]"><k-field-select v-model="formData.isFixedRate" :data-allowblank="false" :data-disabled="false" data-dict="pbc_rate_typ" data-dict-type="1"/></k-form-item>
                <k-form-item label="利率水平_资管06表" :class="[handleItemDiff('rateLevel')]"><k-field-text v-model="formData.rateLevel" :data-allowblank="false" :data-disabled="false" data-validate-type="number" :data-max-length="15" data-digits="5"/></k-form-item>
                <k-form-item label="担保方式_资管06表" :class="[handleItemDiff('guaranteeMode')]"><k-field-select v-model="formData.guaranteeMode" :data-allowblank="true" :data-disabled="false" data-dict="pbc_grt_typ" data-dict-type="1"/></k-form-item>
                <k-form-item label="基础资产投向部门" :class="[handleItemDiff('baseAssetTransDep')]"><k-field-select v-model="formData.baseAssetTransDep" :data-allowblank="false" :data-disabled="false" data-dict="pbc_org_typ" data-dict-type="1"/></k-form-item>
                <k-form-item label="基础资产期末币种" :class="[handleItemDiff('baseAssetEndDtCcy')]"><k-field-text v-model="formData.baseAssetEndDtCcy" :data-allowblank="false" :data-disabled="false"/></k-form-item>
                <k-form-item label="基础资产期末余额" :class="[handleItemDiff('baseAssetEndDtBalance')]"><k-field-text v-model="formData.baseAssetEndDtBalance" :data-allowblank="false" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="基础资产期末余额折人民币" :class="[handleItemDiff('baseAssetEndDtBalanceCny')]"><k-field-text v-model="formData.baseAssetEndDtBalanceCny" :data-allowblank="false" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="登记交易场所" :class="[handleItemDiff('registerTradingPlace')]"><k-field-select v-model="formData.registerTradingPlace" :data-allowblank="false" :data-disabled="false" data-dict="registerTradingPlace" data-dict-type="1"/></k-form-item>
                <k-form-item label="登记交易场所代码_资管产品" :class="[handleItemDiff('registerTradingCode')]"><k-field-text v-model="formData.registerTradingCode" :data-allowblank="false" :data-disabled="false"/></k-form-item>
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
                <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ZG06.updateZG06" data-from="editZG06Form"
                    :data-model="formData" data-target="ZG06Grid" :handle-before="handleBefore">
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
            data-upload-url="/upload/server/RptApp/rhzg/uploadZG06.json">
        </k-field-excel-upload>
      </k-form-item>
      <k-form-footer data-align="center">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="ZG06Grid" ref="submitBtn"
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
    name: "M07RHZG06",
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
                assetDebtProject: '',
                assetIncomeCode: '',
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
              this.$refs.ZG06Grid.load(this.prodSearchParam);
           });
      }
    },
    activated() {
         if(this.$route.query.directedData && this.$route.query.directedData.dataId){
                    this.$set(this.prodSearchParam, 'theoryReportStartDate', '');
                    this.directedData = this.$route.query.directedData
                    this.prodSearchParam.id = this.$route.query.directedData.dataId;
                    this.$nextTick(()=>{
                       this.$refs.ZG06Grid.load(this.prodSearchParam);
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
            this.$refs.editZG06Popup.close();
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
            this.$refs.ZG06Grid.load(this.prodSearchParam);
          }
        },
        onSubmitError() {
          this.$refs.uploadRef.doReset();
          this.$refs.submitBtn.setIconStyle(1, [])
        },
        uploadOpened() {
          this.dataDete = '';
        },

      tabClick(tab, event) {
          this.$refs.ZG06Grid.load(this.prodSearchParam)

      },
      selectRow(row, column, event) {
        this.formData = Object.assign({}, row, {zoneText: row.baseAssetSaleOrgRegZone})
        this.formDataCopy = Object.assign({}, row, {zoneText: row.baseAssetSaleOrgRegZone})
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
  },
}
</script>
