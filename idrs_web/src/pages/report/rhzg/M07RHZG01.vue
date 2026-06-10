
<template>
  <div class="py-page">

          <k-form-search-customize ref="searchRef" data-model-name="ZG01" data-target="ZG01Grid" v-model="queryParam">
            <k-form-item label="新增日期">
              <k-field-date v-model="queryParamDateRange" data-type="daterange"  data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd" />
            </k-form-item>
            <k-form-item label="产品代码_资管">
              <k-field-text v-model="prodSearchParam.prodCd" data-validate-type="text"/>
            </k-form-item>
            <k-form-item label="报送日期">
              <k-field-date v-model="queryParamReportDateRange"  data-type="daterange" data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd"  :data-allowblank="false" />
            </k-form-item>
          </k-form-search-customize>
          <div class="py-page-container">
            <div class="table-top-btns">
              <div class="left">
                <k-btn ref="assetsZG01Send" slot="button" class="btn-custom-plain"
              data-descript="数据报送" data-size="small" @click="creatZipFile('ZG01')">
               <md-icon>cloud_download</md-icon>
              数据报送
            </k-btn>
                <k-btn slot="button" class="btn-custom-plain" data-target="ZG01Grid" :data-export-name="exportName('ZG01', '资管产品基本信息')"
              data-descript="报送数据导出" data-functype="EXPORT" data-size="small"
              data-url="ZG01.findZG01s" data-export-form="searchRef">
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
            <k-grid ref="ZG01Grid" @data-row-select="selectRow" data-action="ZG01.findZG01s" data-fixed="right" data-operate-width="120px" :data-autoload="false" data-dict-type="1">
                <k-grid-column data-align="left" data-header="新增日期" data-name="createDate" data-width="100" data-export="false"></k-grid-column>
                <k-grid-column data-align="left" data-header="报送日期" data-name="reportDate" data-export="false" data-width="100" ></k-grid-column>
                <k-grid-column data-align="left" data-header="版本号" data-name="sysDataVersion" data-export="false" data-width="100" ></k-grid-column>
                <k-grid-column data-align="left" data-header="信息类型" data-name="msgTyp" data-dict="pbc_prd_inf_typ" data-width="160"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品代码_资管" data-name="prodCd" data-width="140"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品名称" data-name="prodNm" data-width="160"></k-grid-column>
                <k-grid-column data-align="left" data-header="发行机构代码" data-name="isuOrgCd" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="发行机构名称" data-name="isuOrgNm" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品品种_资管" data-name="prodCate" data-dict="pbc_prd_typ" data-width="140"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品类型" data-name="prodInvTyp" data-dict="pbc_prod_classify" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品品牌" data-name="prodBrnd"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品期次" data-name="prodTms"></k-grid-column>
                <k-grid-column data-align="left" data-header="发行机构内部产品代码" data-name="isuOrgProdCd" data-width="140"></k-grid-column>
                <k-grid-column data-align="left" data-header="募集资金币种" data-name="clcCcy"></k-grid-column>
                <k-grid-column data-align="left" data-header="兑付本金币种" data-name="callPrcpCcy"></k-grid-column>
                <k-grid-column data-align="left" data-header="兑付收益币种" data-name="callErnCcy"></k-grid-column>
                <k-grid-column data-align="left" data-header="募集方式" data-name="prodClcMth" data-dict="pbc_raise_typ" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="管理方式" data-name="mngMth" data-dict="pbc_oper_mod" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="运行方式_资管" data-name="prodMod" data-dict="pbc_prd_mod" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="募集起始日期" data-name="clcBgnDt" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="募集结束日期" data-name="clcEndDt" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="发行机构提前终止权标识" data-name="isuOrgEarlyTermF" data-dict="pbc_term_flag" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="客户赎回权标识" data-name="custRedemptionF" data-dict="pbc_term_flag" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品增信标识" data-name="prodIncCrdF" data-dict="pbc_conf_flag" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="增信机构类型" data-name="prodIncCrdOrgTyp" data-dict="pbc_org_typ" data-width="140"></k-grid-column>
                <k-grid-column data-align="left" data-header="增信形式" data-name="prodIncCrdForm" data-dict="pbc_crd_incre_form" data-width="140"></k-grid-column>
                <k-grid-column data-align="left" data-header="境内托管机构代码" data-name="dmsTrstOrgCd" data-width="150px"></k-grid-column>
                <k-grid-column data-align="left" data-header="境外托管机构国别代码" data-name="ovsTrstOrgCnr" data-dict="pbc_country_code" data-width="150px"></k-grid-column>
                <k-grid-column data-align="left" data-header="托管机构名称" data-name="ovsTrstOrgNm" data-width="220px"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品起始日期" data-name="foundDt"  data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品变更日期" data-name="changeDt"  data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品预计终止日期" data-name="prodScheduledEndDt"  data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="受托机构管理职责" data-name="entrustedDuty" data-dict="pbc_mng_mod" data-width="80"></k-grid-column>
                <k-grid-column data-align="left" data-header="分级产品标识" data-name="clsfProdF" data-dict="pbc_conf_flag" data-width="80"></k-grid-column>
                <k-grid-column data-align="left" data-header="收益权转让产品标识" data-name="usufructChangeProdF" data-dict="pbc_conf_flag" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="货基或现金管理类产品标识" data-name="cashMngProdF" data-dict="pbc_conf_flag" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="跨境理财通" data-name="cbWMngF" data-dict="pbc_conf_flag" data-width="80"></k-grid-column>
                <k-grid-column data-align="left" data-header="信托产品类型" data-name="trustProdType" data-dict="trust_prod_type" data-width="80"></k-grid-column>
                <k-grid-column data-align="left" data-header="基本信息公开标识" data-name="baseOpenInfoF" data-dict="pbc_conf_flag" data-width="80"></k-grid-column>
                <k-grid-column data-align="left" data-header="变更原因" data-name="changeReason"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品展期标识" data-name="back1" data-dict="pbc_conf_flag"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品登记注册编码" data-name="back2" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="是否处于清算中" data-name="back3" data-dict="pbc_conf_flag"></k-grid-column>
                <k-grid-column data-align="left" data-header="最短开放周期" data-name="back4"></k-grid-column>
                <k-grid-column data-align="left" data-header="备用字段5" data-name="back5"></k-grid-column>
                <template slot="operate" slot-scope="scope">
                <k-btn class="btn-custom-text" data-descript="修改" data-functype="POPUP" data-size="mini"
                    data-target="editZG01Popup">
                    修改
                </k-btn>
                <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="ZG01.deleteZG01" data-size="mini"
                    data-type="danger" data-target="ZG01Grid" :data-confirm="true" data-descript="删除">
                    删除
                </k-btn>
                </template>
            </k-grid>
          </div>
          <!--    修改资管产品基本信息弹出框   -->
          <k-popup ref="editZG01Popup" data-title="修改"  @data-opened="editOpened()">
            <k-form ref="editZG01Form" :data-col="3" isFormBodyScreen data-label-width="190px">
                <k-form-item label="数据日期" :class="[handleItemDiff('theoryReportStartDate')]">
                    <k-field-date v-model="formData.theoryReportStartDate" data-type="date"  data-value-format="yyyyMMdd" :data-allowblank="true" :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="信息类型" :class="[handleItemDiff('msgTyp')]">
                    <k-field-select v-model="formData.msgTyp" :data-allowblank="false" data-dict="pbc_prd_inf_typ" data-dict-type="1"  :data-disabled="false"  @data-on-change="dataOnChange"/>
                </k-form-item>

                <k-form-item label="产品代码_资管" :class="[handleItemDiff('prodCd')]">
                    <k-field-text v-model="formData.prodCd" :data-allowblank="false" :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="产品名称" :class="[handleItemDiff('prodNm')]">
                    <k-field-text v-model="formData.prodNm" :data-allowblank="false" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="发行机构代码" :class="[handleItemDiff('isuOrgCd')]">
                    <k-field-text v-model="formData.isuOrgCd"  :data-allowblank="false" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="发行机构名称" :class="[handleItemDiff('isuOrgNm')]">
                    <k-field-text v-model="formData.isuOrgNm" :data-allowblank="false" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="产品品种_资管" :class="[handleItemDiff('prodCate')]">
                    <k-field-select v-model="formData.prodCate" :data-allowblank="false" data-dict="pbc_prd_typ" data-dict-type="1" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="产品类型" :class="[handleItemDiff('prodInvTyp')]">
                    <k-field-select v-model="formData.prodInvTyp"  data-dict="pbc_prod_classify" data-dict-type="1" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="产品品牌" :class="[handleItemDiff('prodBrnd')]">
                    <k-field-text v-model="formData.prodBrnd" :data-allowblank="true" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="产品期次" :class="[handleItemDiff('prodTms')]">
                    <k-field-text v-model="formData.prodTms" :data-allowblank="true" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="发行机构内部产品代码" :class="[handleItemDiff('isuOrgProdCd')]">
                    <k-field-text v-model="formData.isuOrgProdCd"  :data-allowblank="true" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="募集资金币种" :class="[handleItemDiff('clcCcy')]">
                    <k-field-text v-model="formData.clcCcy" :data-allowblank="false" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="兑付本金币种" :class="[handleItemDiff('callPrcpCcy')]">
                    <k-field-text v-model="formData.callPrcpCcy" :data-allowblank="false" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="兑付收益币种" :class="[handleItemDiff('callErnCcy')]">
                    <k-field-text v-model="formData.callErnCcy"  :data-allowblank="false" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="募集方式" :class="[handleItemDiff('prodClcMth')]">
                    <k-field-select v-model="formData.prodClcMth" :data-allowblank="false" data-dict="pbc_raise_typ" data-dict-type="1" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="管理方式" :class="[handleItemDiff('mngMth')]">
                    <k-field-select v-model="formData.mngMth" :data-allowblank="false" data-dict="pbc_oper_mod" data-dict-type="1" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="运行方式_资管" :class="[handleItemDiff('prodMod')]">
                    <k-field-select v-model="formData.prodMod"  :data-allowblank="false" data-dict="pbc_prd_mod" data-dict-type="1" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="募集起始日期" :class="[handleItemDiff('clcBgnDt')]">
                    <k-field-date v-model="formData.clcBgnDt" :data-allowblank="false" data-type="date" data-value-format="yyyy-MM-dd" data-date-format="yyyy-MM-dd"  :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="募集结束日期" :class="[handleItemDiff('clcEndDt')]">
                    <k-field-date v-model="formData.clcEndDt" :data-allowblank="false" data-type="date" data-value-format="yyyy-MM-dd" data-date-format="yyyy-MM-dd"  :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="发行机构提前终止权标识" :class="[handleItemDiff('isuOrgEarlyTermF')]">
                    <k-field-select v-model="formData.isuOrgEarlyTermF"  :data-allowblank="false" data-dict="pbc_term_flag" data-dict-type="1" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="客户赎回权标识" :class="[handleItemDiff('custRedemptionF')]">
                    <k-field-select v-model="formData.custRedemptionF" :data-allowblank="false" data-dict="pbc_term_flag" data-dict-type="1" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="产品增信标识" :class="[handleItemDiff('prodIncCrdF')]">
                    <k-field-select v-model="formData.prodIncCrdF" :data-allowblank="false" data-dict="pbc_conf_flag" data-dict-type="1" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="增信机构类型" :class="[handleItemDiff('prodIncCrdOrgTyp')]">
                    <k-field-select v-model="formData.prodIncCrdOrgTyp"  :data-allowblank="true" data-dict="pbc_org_typ" data-dict-type="1" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="增信形式" :class="[handleItemDiff('prodIncCrdForm')]">
                    <k-field-select v-model="formData.prodIncCrdForm" :data-allowblank="true" data-dict="pbc_crd_incre_form" data-dict-type="1" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="境内托管机构代码" :class="[handleItemDiff('dmsTrstOrgCd')]">
                    <k-field-text v-model="formData.dmsTrstOrgCd" :data-allowblank="true" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="境外托管机构国别代码" :class="[handleItemDiff('ovsTrstOrgCnr')]">
                    <k-field-text v-model="formData.ovsTrstOrgCnr"  :data-allowblank="true" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="托管机构名称" :class="[handleItemDiff('ovsTrstOrgNm')]">
                    <k-field-text v-model="formData.ovsTrstOrgNm" :data-allowblank="true" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="产品起始日期" :class="[handleItemDiff('foundDt')]">
                    <k-field-date v-model="formData.foundDt" data-type="date" data-value-format="yyyy-MM-dd" data-date-format="yyyy-MM-dd" :data-allowblank="false" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="产品变更日期" :class="[handleItemDiff('changeDt')]">
                    <k-field-date v-model="formData.changeDt" data-type="date" data-value-format="yyyy-MM-dd" data-date-format="yyyy-MM-dd" :data-allowblank="formData.changeDtAllowblank" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="产品预计终止日期" :class="[handleItemDiff('prodScheduledEndDt')]">
                    <k-field-date v-model="formData.prodScheduledEndDt" data-type="date" data-value-format="yyyy-MM-dd" data-date-format="yyyy-MM-dd" :data-allowblank="true" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="受托机构管理职责" :class="[handleItemDiff('entrustedDuty')]">
                    <k-field-select v-model="formData.entrustedDuty" :data-allowblank="false" data-dict="pbc_mng_mod" data-dict-type="1" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="分级产品标识" :class="[handleItemDiff('clsfProdF')]">
                    <k-field-select v-model="formData.clsfProdF"  :data-allowblank="false" data-dict="pbc_conf_flag" data-dict-type="1" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="收益权转让产品标识" :class="[handleItemDiff('usufructChangeProdF')]">
                    <k-field-select v-model="formData.usufructChangeProdF"  :data-allowblank="false" data-dict="pbc_conf_flag" data-dict-type="1" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="货基或现金管理类产品标识" :class="[handleItemDiff('cashMngProdF')]">
                    <k-field-select v-model="formData.cashMngProdF" :data-allowblank="false" data-dict="pbc_conf_flag" data-dict-type="1" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="跨境理财通" :class="[handleItemDiff('cbWMngF')]">
                    <k-field-select v-model="formData.cbWMngF"  :data-allowblank="false" data-dict="pbc_conf_flag" data-dict-type="1" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="信托产品类型" :class="[handleItemDiff('trustProdType')]">
                    <k-field-select v-model="formData.trustProdType" data-dict="trust_prod_type" data-dict-type="1" />
                </k-form-item>
                <k-form-item label="基本信息公开标识" :class="[handleItemDiff('baseOpenInfoF')]">
                    <k-field-select v-model="formData.baseOpenInfoF"  :data-allowblank="false" data-dict="pbc_conf_flag" data-dict-type="1" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="变更原因" :class="[handleItemDiff('changeReason')]">
                    <k-field-text v-model="formData.changeReason" :data-allowblank="formData.changeReasonAllowblank" :data-disabled="false"/>
                </k-form-item>
              <k-form-item label="产品展期标识" :class="[handleItemDiff('back1')]">
                <k-field-select v-model="formData.back1" :data-allowblank="false"  data-dict="pbc_conf_flag" data-dict-type="1" />
              </k-form-item>
              <k-form-item label="产品登记注册编码" :class="[handleItemDiff('back2')]">
                <k-field-text v-model="formData.back2" :data-allowblank="false"/>
              </k-form-item>
              <k-form-item label="是否处于清算中" :class="[handleItemDiff('back3')]">
                <k-field-select v-model="formData.back3" :data-allowblank="false"  data-dict="pbc_conf_flag" data-dict-type="1" />
              </k-form-item>
              <k-form-item label="最短开放周期" :class="[handleItemDiff('back4')]">
                <k-field-text v-model="formData.back4"/>
              </k-form-item>
              <k-form-item label="备用字段5" :class="[handleItemDiff('back5')]">
                <k-field-text v-model="formData.back5"/>
              </k-form-item>

                <k-form-footer data-align="center" slot="footer">
                <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ZG01.updateZG01" data-from="editZG01Form"
                    :data-model="formData" data-target="ZG01Grid" :handle-before="handleBefore">
                    <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
                </k-btn>
                <k-btn class="btn-custom-plain" data-functype="CLOSE">
                    <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
                </k-form-footer>
            </k-form>
          </k-popup>


    <k-popup ref="addPopup" title="报送数据导入" @data-opened="uploadOpened()">
    <k-form ref="addForm" data-ui="element">
      <!--<k-form-item label="开始日期">
        <k-field-date v-model="uploadBeginDate" data-type="date"  data-date-format="yyyy-MM-dd"  data-value-format="yyyyMMdd" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="截止日期">
        <k-field-date v-model="uploadQueryDate" data-type="date"  data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd" :data-allowblank="false"/>
      </k-form-item>-->
      <k-form-item label="报送数据导入" data-ui="element" data-input-width="500px">
        <k-field-excel-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit='1'
            data-accept=".xlsx,.xls"
            :data-error="onSubmitError" :data-success="onSubmitSuccess"
            :data-auto-upload="false"
            data-upload-url="/upload/server/RptApp/rhzg/uploadZG01.json">
        </k-field-excel-upload>
      </k-form-item>
      <k-form-footer data-align="center">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="ZG01Grid" ref="submitBtn"
              :data-auto-upload="false" data-from="addForm" :data-handler="submitUploadParam">确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
      </k-form-footer>
    </k-form>
    </k-popup>




  </div>
</template>

<script>
import ZgMixin from "@/pages/report/rhzg/zgMixin.js";

export default {
    name: "M07RHZG01",
    mixins: [ZgMixin],
    data () {
        return {
            uploadBeginDate: '',
            uploadQueryDate: '',
            activeName: '1',
            prodSearchParam: {
                prodCd: '',
                id: '',
            },
            formData: {},
            formDataCopy: {},
            queryParamDateRange: [],
            queryParamReportDateRange: [],
            directedData:{},
            nowDate:'',
        }
    },
    created() {
           this.getNowDate();
          if(this.$route.query.directedData && this.$route.query.directedData.dataId){
               this.prodManualData = this.$route.query.prodManualData
               this.prodSearchParam.id = this.$route.query.directedData.dataId;
               this.$nextTick(()=>{
                  this.$refs.ZG01Grid.load(this.prodSearchParam);
               });
          }
    },
    activated() {
          if(this.$route.query.directedData && this.$route.query.directedData.dataId){
                        this.prodManualData = this.$route.query.prodManualData
                        this.prodSearchParam.id = this.$route.query.directedData.dataId;
                        this.$nextTick(()=>{
                           this.$refs.ZG01Grid.load(this.prodSearchParam);
                        });
                   }
        },
    mounted() {
    },
    computed: {
      queryParam () {
        return {
            'beginDate': this.queryParamDateRange ? this.queryParamDateRange[0] : null,
            'queryDate': this.queryParamDateRange ? this.queryParamDateRange[1] : null,
            'reportBeginDate': this.queryParamReportDateRange ? this.queryParamReportDateRange[0] : null,
            'reportEndDate': this.queryParamReportDateRange ? this.queryParamReportDateRange[1] : null,
            'prodCd': this.prodSearchParam.prodCd,
            'id': this.prodSearchParam.id,
        }
      }
    },
    methods: {
        handleBefore() {
          if (this.formNoChangeCb()) {
            this.$refs.editZG01Popup.close();
            return false
          }
          return true
        },
        submitUploadParam() {
        //文件上传校验
        let validate = this.$refs.addForm.validate();
        if (validate) {
          let formData = { beginDate: this.uploadBeginDate, queryDate: this.uploadQueryDate }
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
        this.$refs.uploadRef.doReset();
        this.$refs.addForm.reset();
        this.$refs.addPopup.close();
        this.$refs.ZG01Grid.load(this.queryParam);
      },
      onSubmitError() {
        this.$refs.uploadRef.doReset();
        this.$refs.submitBtn.setIconStyle(1, [])
      },
      uploadOpened() {
        this.uploadBeginDate = ''
        this.uploadQueryDate = ''
      },
      editOpened() {
        if (this.formData.msgTyp == '2') {
           this.$set(this.formData, 'changeDtAllowblank', false);
           this.$set(this.formData, 'changeReasonAllowblank', false);
         } else {
           this.$set(this.formData, 'changeDtAllowblank', true);
           this.$set(this.formData, 'changeReasonAllowblank', true);
         }
        this.formDataCopy = Object.assign({}, this.formData)
      },

      tabClick(tab, event) {
          this.$refs.ZG01Grid.load(this.prodSearchParam)

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
        this.queryParamReportDateRange[0] = this.nowDate;
        this.queryParamReportDateRange[1] = this.nowDate;
        this.$set(this.queryParam, "queryParamReportDateRange", this.nowDate);
      },
      dataOnChange() {
         if (this.formData.msgTyp == '2') {
           this.formData.changeDtAllowblank = false;
           this.formData.changeReasonAllowblank = false;
         } else {
           this.formData.changeDtAllowblank = true;
           this.formData.changeReasonAllowblank = true;
         }
      }


  }

}
</script>
