<!--
 * @Author: litao
 * @Date: 2022-07-13 10:18:46
 * @LastEditTime: 2022-07-19 16:09:49
 * @LastEditors: litao
 * @Description: 资金池及产品信息
 * @FilePath: \idrs_web\src\pages\report\rhzj\M07RHZJ01.vue
-->
<template>
  <div>
      <el-tabs v-model="activeName" @tab-click="tabClick">
        <el-tab-pane label="资金池及产品信息" name="1">
          <k-form-search-customize data-model-name="ReportPPI" data-target="reportPPIGrid" v-model="queryParam">
            <k-form-item label="内部产品代码" data-label-width="150px">
              <k-field-text v-model="prodSearchParam.prodCode" data-validate-type="text"/>
            </k-form-item>
            <k-form-item label="资产池代码"  data-label-width="150px">
              <k-field-text v-model="prodSearchParam.pbcAssetscode" data-validate-type="text"/>
            </k-form-item>
            <k-form-item label="人行产品代码"  data-label-width="150px">
              <k-field-text v-model="prodSearchParam.peoplebankSubmitcode" data-validate-type="text"/>
            </k-form-item>
            <k-form-item label="日期范围">
              <k-field-date v-model="queryParamDateRange" data-type="daterange"  data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd"/>
            </k-form-item>
            <k-btn slot="button" style="width: 120px" class="md-success"
              data-descript="产品基本信息报送" data-size="small" @click="generatePBFile('prodSend')">
              <!-- <md-icon>cloud_download</md-icon> -->
              产品基本信息报送
            </k-btn>
            <k-btn slot="button" style="width: 120px" class="md-success"
              data-descript="资金池基本信息报送" @click="generatePBFile('zjSend')">
              <!-- <md-icon>cloud_download</md-icon> -->
              资金池基本信息报送
            </k-btn>
            <k-btn slot="button" style="width: 120px" class="btn-custom-plain" data-target="reportPPIGrid" :data-export-name="'产品基本信息'"
              data-descript="报送数据导出" data-functype="EXPORT" data-size="small"
              data-url="ReportPPI.findReportPPIs">
              <md-icon>cloud_download</md-icon>
              报送数据导出
            </k-btn>
            <k-btn slot="button" style="width: 120px" data-functype="POPUP" class="btn-custom-plain"
                data-target="addPopup">
              <md-icon>cloud_upload</md-icon>
              报送数据导入
            </k-btn>
          </k-form-search-customize>
          <div>
            <k-grid ref="reportPPIGrid" @data-row-select="selectRow"  data-fixed="right" data-action="ReportPPI.findReportPPIs">
              <k-grid-column data-header="发起机构内部产品代码" data-name="prodCode" data-width="140"></k-grid-column>
              <k-grid-column data-header="产品对应资产池代码" data-name="pbcAssetscode" data-width="140"></k-grid-column>
              <k-grid-column data-header="报送人行产品代码" data-name="peoplebankSubmitcode" data-width="120"></k-grid-column>
              <k-grid-column data-header="产品名称" data-name="prodName"></k-grid-column>
              <k-grid-column data-header="发起机构代码" data-name="orgno" data-width="120"></k-grid-column>
              <k-grid-column data-header="产品品种" data-name="prodVariety" data-dict="t8_prod_variety_dat" data-width="80"></k-grid-column>
              <k-grid-column data-header="产品品牌" data-name="prodBrand"></k-grid-column>
              <k-grid-column data-header="产品期次" data-name="prodTimes"></k-grid-column>
              <k-grid-column data-header="募集方式" data-name="collMod" data-dict="t8_reserve4_dat" data-width="80"></k-grid-column>
              <k-grid-column data-header="管理方式" data-name="operMod" data-dict="t8_reserve6_dat" data-width="120"></k-grid-column>
              <k-grid-column data-header="运行方式" data-name="runMod" data-dict="t8_run_mod_dat" data-width="120"></k-grid-column>
              <k-grid-column data-header="产品类型" data-name="prodType" data-dict="t8_invest_prop_type_dat" data-width="120"></k-grid-column>
              <k-grid-column data-header="业务模式" data-name="busiMod" data-dict="t8_reserve7_dat" data-width="80"></k-grid-column>
              <k-grid-column data-header="收益保障标识" data-name="safeRate" data-width="100" data-dict="t8_1yes2no_dat"></k-grid-column>
              <k-grid-column data-header="本金保障标识" data-name="safeCapit" data-width="100" data-dict="t8_1yes2no_dat"></k-grid-column>
              <k-grid-column data-header="预计客户最高收益率" data-name="maxRate" data-width="130"></k-grid-column>
              <k-grid-column data-header="预计客户最低收益率" data-name="minRate" data-width="130"></k-grid-column>
              <k-grid-column data-header="募集起始日期" data-name="subsBdate" data-width="100" data-type="date"></k-grid-column>
              <k-grid-column data-header="募集结束日期" data-name="subsEdate" data-width="100" data-type="date"></k-grid-column>
              <k-grid-column data-header="发行机构提前终止权标识" data-name="termFlag" data-width="160" data-dict="t8_flag_dat"></k-grid-column>
              <k-grid-column data-header="客户赎回权标识" data-name="redeemFlag" data-width="110" data-dict="t8_flag_dat"></k-grid-column>
              <k-grid-column data-header="产品增信标识" data-name="prodCreditFlag" data-width="100" data-dict="t8_1yes2no_dat"></k-grid-column>
              <k-grid-column data-header="境内托管机构代码" data-name="bordTrustiCode" data-width="130"></k-grid-column>
              <k-grid-column data-header="境外托管机构国别" data-name="oversTrustiNation" data-width="130"></k-grid-column>
              <k-grid-column data-header="境外托管机构名称" data-name="oversTrustiName" data-width="130"></k-grid-column>
              <k-grid-column data-header="产品起始日期" data-name="establishDate" data-width="120" data-type="date"></k-grid-column>
              <k-grid-column data-header="产品预计终止日期" data-name="endDate" data-width="130" data-type="date"></k-grid-column>
              <k-grid-column data-header="募集资金币种" data-name="issuCcy" data-width="100"></k-grid-column>
              <k-grid-column data-header="兑付本金币种" data-name="returnCcy" data-width="100"></k-grid-column>
              <k-grid-column data-header="兑付收益币种" data-name="incomeCcy" data-width="100"></k-grid-column>
              <k-grid-column data-header="客户类型" data-name="investObject" data-dict="t8_custom_type_dat"></k-grid-column>
              <k-grid-column data-header="增信机构类型" data-name="prodCreditOrg" data-width="120" data-dict="t8_zhz_credit_org_dat" ></k-grid-column>
              <k-grid-column data-header="增信形式" data-name="prodCreditMod" data-dict="t8_zhz_up_credit_dat" data-width="80"></k-grid-column>
              <k-grid-column data-header="受托职责" data-name="entrestedObligation" data-dict="t8_entrested_obligation_dat" data-width="110"></k-grid-column>
              <k-grid-column data-header="合作模式" data-name="cooperationMode" data-dict="t8_cooperation_mode_dat" data-width="90"></k-grid-column>
              <k-grid-column data-header="分级产品标识" data-name="gradingFlag" data-width="110" data-dict="t8_1yes2no_dat"></k-grid-column>
              <k-grid-column data-header="收益权转让产品标识" data-name="transferFlag" data-width="140" data-dict="t8_1yes2no_dat"></k-grid-column>
              <k-grid-column data-header="发起机构标识" data-name="orgnoFlag" data-width="120" data-dict="t8_orgno_flag_dat"></k-grid-column>
              <k-grid-column data-header="现金管理类产品" data-name="cashType" data-width="120" data-dict="t8_1yes2no_dat"></k-grid-column>
              <k-grid-column data-header="跨境理财通标识" data-name="crossBorderFinan" data-width="120" data-dict="t8_cross_border_finan"></k-grid-column>
              <k-grid-column data-header="以财产权信托名义开展的资金信托标识" data-name="isTrust" data-width="250" data-dict="t8_cross_border_finan"></k-grid-column>
                  <template slot="operate" slot-scope="scope">
                    <k-btn class="md-info md-just-icon md-simple" data-descript="修改基本信息" data-functype="POPUP" data-size="mini"
                      data-target="editReportPPIPopup">
                      <md-icon>edit</md-icon>
                    </k-btn>
                    <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="ReportPPI.deleteReportPPI" data-size="mini"
                        data-type="danger" data-target="reportPPIGrid" :data-confirm="true" data-descript="删除">
                      <md-icon>close</md-icon>
                  </k-btn>
                  </template>
            </k-grid>
            <!--    修改人行资金池及产品信息弹出框   -->
            <k-popup ref="editReportPPIPopup" data-title="修改基本信息">
              <k-form ref="editReportPPIForm" :data-col="2">
              <k-form-item label="内部产品代码">
                    <k-field-text v-model="formProdData.prodCode" :data-allowblank="false" :data-disabled="true"/>
                </k-form-item>
              <k-form-item label="发起机构代码" :data-allowblank="false">
                    <k-field-text v-model="formProdData.orgno"/>
                </k-form-item>
              <k-form-item label="报送人行产品代码">
                    <k-field-text v-model="formProdData.peoplebankSubmitcode" :data-allowblank="false"/>
                </k-form-item>
              <k-form-item label="产品品种">
                    <k-field-select v-model="formProdData.prodVariety" data-dict="t8_prod_variety_dat"  :data-allowblank="false"/>
                </k-form-item>
              <k-form-item label="产品名称">
                    <k-field-text v-model="formProdData.prodName" :data-allowblank="false"/>
                </k-form-item>
              <k-form-item label="产品品牌">
                    <k-field-text v-model="formProdData.prodBrand" :data-allowblank="false"/>
                </k-form-item>
              <k-form-item label="产品期次">
                    <k-field-text v-model="formProdData.prodTimes" :data-allowblank="false" data-validate-type="number"/>
                </k-form-item>
              <k-form-item label="募集方式">
                    <k-field-select v-model="formProdData.collMod" data-dict="t8_reserve4_dat" :data-allowblank="false"/>
                </k-form-item>
              <k-form-item label="管理方式">
                    <k-field-select v-model="formProdData.operMod" data-dict="t8_reserve6_dat" :data-allowblank="false"/>
                </k-form-item>
              <k-form-item label="运行方式">
                    <k-field-select v-model="formProdData.runMod" data-dict="t8_run_mod_dat" :data-allowblank="false"/>
                </k-form-item>
              <k-form-item label="产品类型">
                    <k-field-select v-model="formProdData.prodType" data-dict="t8_invest_prop_type_dat" :data-allowblank="false"/>
                </k-form-item>
              <k-form-item label="业务模式">
                    <k-field-select v-model="formProdData.busiMod" data-dict="t8_1yes2no_dat" :data-allowblank="false"/>
                </k-form-item>
              <k-form-item label="收益保障标识">
                    <k-field-select v-model="formProdData.safeRate" data-dict="t8_1yes2no_dat" :data-allowblank="false"/>
                </k-form-item>
              <k-form-item label="本金保障标识">
                    <k-field-select v-model="formProdData.safeCapit" data-dict="t8_1yes2no_dat" :data-allowblank="false"/>
                </k-form-item>
              <k-form-item label="预计客户最高收益率">
                    <k-field-text v-model="formProdData.maxRate" :data-max-length="9"  data-validate-type="number" :data-allowblank="false"/>
                </k-form-item>
              <k-form-item label="预计客户最低收益率">
                    <k-field-text v-model="formProdData.minRate" :data-max-length="9" data-validate-type="number" :data-allowblank="false"/>
                </k-form-item>
              <k-form-item label="募集起始日期">
                    <k-field-date v-model="formProdData.subsBdate" data-type="date" :data-allowblank="false" data-value-format="yyyyMMdd"/>
                </k-form-item>
              <k-form-item label="募集结束日期">
                    <k-field-date v-model="formProdData.subsEdate" data-type="date" :data-allowblank="false" data-value-format="yyyyMMdd"/>
                </k-form-item>
              <k-form-item label="发行机构提前终止权标识">
                    <k-field-select v-model="formProdData.termFlag" data-dict="t8_flag_dat" :data-allowblank="false"/>
                </k-form-item>
              <k-form-item label="客户赎回权标识">
                    <k-field-select v-model="formProdData.redeemFlag"  data-dict="t8_flag_dat" :data-allowblank="false"/>
                </k-form-item>
              <k-form-item label="产品增信标识">
                    <k-field-select v-model="formProdData.prodCreditFlag"  data-dict="t8_1yes2no_dat" :data-allowblank="false"/>
                </k-form-item>
              <k-form-item label="境内托管机构代码">
                    <k-field-text v-model="formProdData.bordTrustiCode"/>
                </k-form-item>
              <k-form-item label="境外托管机构国别">
                    <k-field-text v-model="formProdData.oversTrustiNation"/>
                </k-form-item>
              <k-form-item label="境外托管机构名称">
                    <k-field-text v-model="formProdData.oversTrustiName"/>
                </k-form-item>
              <k-form-item label="产品起始日期">
                    <k-field-date v-model="formProdData.establishDate" data-type="date" :data-allowblank="false" data-value-format="yyyyMMdd"/>
                </k-form-item>
              <k-form-item label="产品预计终止日期">
                    <k-field-date v-model="formProdData.endDate" :data-allowblank="false" data-value-format="yyyyMMdd"/>
                </k-form-item>
              <k-form-item label="产品对应资产池代码">
                    <k-field-text v-model="formProdData.pbcAssetscode"/>
                </k-form-item>
              <k-form-item label="募集资金币种">
                    <k-field-select v-model="formProdData.issuCcy" data-dict="money_flag" :data-allowblank="false"/>
                </k-form-item>
              <k-form-item label="兑付本金币种">
                    <k-field-select v-model="formProdData.returnCcy" data-dict="money_flag" :data-allowblank="false"/>
                </k-form-item>
              <k-form-item label="兑付收益币种">
                    <k-field-select v-model="formProdData.incomeCcy" data-dict="money_flag" :data-allowblank="false"/>
                </k-form-item>
              <k-form-item label="客户类型">
                    <k-field-select v-model="formProdData.investObject" data-dict="t8_custom_type_dat" :data-allowblank="false"/>
                </k-form-item>
              <k-form-item label="增信机构类型">
                    <k-field-select v-model="formProdData.prodCreditOrg" data-dict="t8_zhz_credit_org_dat"/>
                </k-form-item>
              <k-form-item label="增信形式">
                    <k-field-select v-model="formProdData.prodCreditMod"  data-dict="t8_zhz_up_credit_dat"/>
                </k-form-item>
              <k-form-item label="受托职责">
                <k-field-select v-model="formProdData.entrestedObligation"  data-dict="t8_entrested_obligation_dat"/>
              </k-form-item>
              <k-form-item label="合作模式">
                  <k-field-select v-model="formProdData.cooperationMode" data-dict="t8_cooperation_mode_dat"/>
                </k-form-item>
              <k-form-item label="分级产品标识">
                    <k-field-select v-model="formProdData.gradingFlag" data-dict="t8_1yes2no_dat"/>
                </k-form-item>
              <k-form-item label="收益权转让产品标识">
                    <k-field-select v-model="formProdData.transferFlag" data-dict="t8_1yes2no_dat"/>
                </k-form-item>
              <k-form-item label="理财产品发起机构标识">
                    <k-field-select v-model="formProdData.orgnoFlag"  data-dict="t8_orgno_flag_dat"/>
                </k-form-item>
                <k-form-footer data-align="center">
                  <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ReportPPI.updateReportPPI" data-from="editReportPPIForm"
                    :data-model="formProdData" data-target="reportPPIGrid">
                    <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
                  </k-btn>
                  <k-btn class="btn-custom-plain" data-functype="CLOSE">
                    <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
                </k-form-footer>
              </k-form>
            </k-popup>
          </div>
        </el-tab-pane>
        <el-tab-pane label="终止信息" name="2">
          <k-form-search-customize data-model-name="ReportPIE" data-target="reportPIEGrid" v-model="queryStopParam">
            <k-form-item label="产品代码" data-label-width="150px">
              <k-field-text v-model="prodStopSearchParam.prodCode" data-validate-type="text"/>
            </k-form-item>
            <k-form-item label="日期范围">
              <k-field-date v-model="queryStopParamDateRange" data-type="daterange"  data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd"/>
            </k-form-item>
            <k-btn slot="button" style="width: 120px" class="md-success"
              data-descript="产品终止信息报送" data-size="small" @click="generatePBFile('prodStopSend')">
              <!-- <md-icon>cloud_download</md-icon> -->
              产品终止信息报送
            </k-btn>
            <k-btn slot="button" style="width: 120px" class="md-success"
              data-descript="资金池终止信息报送" @click="generatePBFile('zjStopSend')">
              <!-- <md-icon>cloud_download</md-icon> -->
              资金池终止信息报送
            </k-btn>
            <k-btn slot="button" style="width: 120px" class="btn-custom-plain" data-target="reportPIEGrid" :data-export-name="'产品终止信息'"
              data-descript="报送数据导出" data-functype="EXPORT" data-size="small"
              data-url="ReportPIE.findReportPIEs">
              <md-icon>cloud_download</md-icon>
              报送数据导出
            </k-btn>
            <k-btn slot="button" style="width: 120px" data-functype="POPUP" class="btn-custom-plain"
                data-target="addPopup">
              <md-icon>cloud_upload</md-icon>
              报送数据导入
            </k-btn>
          </k-form-search-customize>
          <div>
            <k-grid ref="reportPIEGrid" @data-row-select="selectRow" data-action="ReportPIE.findReportPIEs" data-fixed="right">
              <k-grid-column data-header="产品代码" data-name="prodCode"></k-grid-column>
              <k-grid-column data-header="报送人行产品代码" data-name="peoplebankSubmitcode"></k-grid-column>
              <k-grid-column data-header="资金池代码" data-name="pbcAssetscode"></k-grid-column>
              <k-grid-column data-header="产品实际终止日期" data-name="endDateReal" data-type="date"></k-grid-column>
              <k-grid-column data-header="币种代码" data-name="cny"></k-grid-column>
              <k-grid-column data-header="发行机构实现收入" data-name="orgErn" data-type="money"></k-grid-column>
              <k-grid-column data-header="发行机构实现收入折人民币(元)" data-name="orgErnRmb" data-width="200" data-type="money"></k-grid-column>
              <k-grid-column data-header="兑付客户收益" data-name="custErn"></k-grid-column>
              <k-grid-column data-header="兑付客户收益折人民币(元)" data-name="custErnRmb" data-width="180" data-type="money"></k-grid-column>
              <k-grid-column data-header="兑付客户收益率" data-name="custErnYld"></k-grid-column>
                  <template slot="operate" slot-scope="scope">
                    <k-btn class="md-info md-just-icon md-simple" data-descript="修改产品终止信息" data-functype="POPUP" data-size="mini"
                      data-target="editReportPIEPopup">
                      <md-icon>edit</md-icon>
                    </k-btn>
                    <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="ReportPIE.deleteReportPIE" data-size="mini"
                        data-type="danger" data-target="reportPIEGrid" :data-confirm="true" data-descript="删除产品终止信息">
                      <md-icon>close</md-icon>
                  </k-btn>
                  </template>
            </k-grid>
          </div>
          <!--    修改产品终止信息弹出框   -->
          <k-popup ref="editReportPIEPopup" data-title="修改产品终止信息">
            <k-form ref="editReportPIEForm" :data-col="2">
              <k-form-item label="产品代码">
                  <k-field-text v-model="formStopData.prodCode"  :data-allowblank="false" :data-disabled="true"/>
              </k-form-item>
              <k-form-item label="报送人行产品代码">
                  <k-field-text v-model="formStopData.peoplebankSubmitcode"  :data-allowblank="false" />
              </k-form-item>
              <k-form-item label="资金池代码">
        	      <k-field-text v-model="formStopData.pbcAssetscode" :data-allowblank="false"  />
     	        </k-form-item>
              <k-form-item label="产品实际终止日期">
                <k-field-date v-model="formStopData.endDateReal" data-type="date" data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd" :data-allowblank="false" :data-disabled="true"/>
              </k-form-item>
              <k-form-item label="币种代码">
                  <k-field-text v-model="formStopData.cny" :data-disabled="true"/>
              </k-form-item>
              <k-form-item label="发行机构实现收入">
                  <k-field-text v-model="formStopData.orgErn"/>
              </k-form-item>
              <k-form-item label="发行机构实现收入折人民币">
                  <k-field-text v-model="formStopData.orgErnRmb" data-validate-type="money"/>
              </k-form-item>
              <k-form-item label="兑付客户收益">
                  <k-field-text v-model="formStopData.custErn" data-validate-type="money"/>
              </k-form-item>
              <k-form-item label="兑付客户收益折人民币">
                  <k-field-text v-model="formStopData.custErnRmb" data-validate-type="money"/>
              </k-form-item>
              <k-form-item label="兑付客户收益率">
                  <k-field-text v-model="formStopData.custErnYld" data-validate-type="number"/>
              </k-form-item>
              <k-form-footer data-align="center">
                <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ReportPIE.updateReportPIE" data-from="editReportPIEForm"
                  :data-model="formStopData" data-target="reportPIEGrid">
                  <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
                </k-btn>
                <k-btn class="btn-custom-plain" data-functype="CLOSE">
                  <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
              </k-form-footer>
            </k-form>
          </k-popup>
        </el-tab-pane>
        <el-tab-pane label="起始募捐信息" name="3">
          <k-form-search-customize data-model-name="ReportPIB" data-target="reportPIBGrid" v-model="queryStartParam">
            <k-form-item label="行内产品代码" data-label-width="150px">
              <k-field-text v-model="prodStartSearchParam.prodCode" data-validate-type="text"/>
            </k-form-item>
            <k-form-item label="日期范围">
              <k-field-date v-model="queryStartParamDateRange" data-type="daterange"  data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd"/>
            </k-form-item>
            <k-btn slot="button" style="width: 120px" class="btn-custom-plain" data-target="reportPIBGrid" :data-export-name="'产品起始募集信息'"
              data-descript="报送数据导出" data-functype="EXPORT" data-size="small"
              data-url="ReportPIB.findReportPIBs">
              <md-icon>cloud_download</md-icon>
              报送数据导出
            </k-btn>
            <k-btn slot="button" style="width: 120px" data-functype="POPUP" class="btn-custom-plain"
                data-target="addPopup">
              <md-icon>cloud_upload</md-icon>
              报送数据导入
            </k-btn>
          </k-form-search-customize>
          <k-grid ref="reportPIBGrid" @data-row-select="selectRow" data-action="ReportPIB.findReportPIBs"  data-fixed="right">
            <k-grid-column data-header="报送日期" data-name="reportDate" data-type="date"></k-grid-column>
            <k-grid-column data-header="行内产品代码" data-name="prodCode"></k-grid-column>
            <k-grid-column data-header="报送人行产品代码" data-name="peoplebankSubmitcode" data-width="150"></k-grid-column>
            <k-grid-column data-header="地区代码" data-name="areaCode"></k-grid-column>
            <k-grid-column data-header="客户类型" data-name="custType" data-dict="t8_invest_object_rpt_dat"></k-grid-column>
            <k-grid-column data-header="币种代码" data-name="cny"></k-grid-column>
            <k-grid-column data-header="起始募集金额(元)" data-name="initAmount" data-type="money"></k-grid-column>
            <k-grid-column data-header="起始募集金额折人民币(元)" data-name="initAmountRmb" data-width="180" data-type="money"></k-grid-column>
            <k-grid-column data-header="起始募集份额(元)" data-name="initVol" data-type="money"></k-grid-column>
              <template slot="operate" slot-scope="scope">
                <k-btn class="md-info md-just-icon md-simple" data-descript="修改产品起始募集信息" data-functype="POPUP" data-size="mini"
                  data-target="editReportPIBPopup">
                  <md-icon>edit</md-icon>
                </k-btn>
                <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="ReportPIB.deleteReportPIB" data-size="mini"
                    data-type="danger" data-target="reportPIBGrid" :data-confirm="true" data-descript="删除产品起始募集信息">
                  <md-icon>close</md-icon>
              </k-btn>
              </template>
          </k-grid>
           <!--    修改产品起始募集信息弹出框   -->
          <k-popup ref="editReportPIBPopup" data-title="修改产品起始募集信息">
            <k-form ref="editReportPIBForm" :data-col="2">
              <k-form-item label="行内产品代码">
                <k-field-text v-model="formStartData.prodCode" :data-allowblank="false" :data-disabled ="true"/>
              </k-form-item>
              <k-form-item label="报送人行产品代码">
                  <k-field-text v-model="formStartData.peoplebankSubmitcode" :data-allowblank="false"/>
              </k-form-item>
              <k-form-item label="地区代码">
                  <k-field-text v-model="formStartData.areaCode" :data-allowblank="false" :data-max-length="6" />
              </k-form-item>
              <k-form-item label="客户类型">
                  <k-field-select v-model="formStartData.custType" data-dict="t8_invest_object_rpt_dat" :data-allowblank="false"/>
              </k-form-item>
              <k-form-item label="币种代码">
                  <k-field-text v-model="formStartData.cny" :data-disabled ="true"/>
              </k-form-item>
              <k-form-item label="起始募集金额">
                  <k-field-text v-model="formStartData.initAmount" data-validate-type="money"/>
              </k-form-item>
              <k-form-item label="起始募集金额折人民币">
                  <k-field-text v-model="formStartData.initAmountRmb" data-validate-type="money"/>
              </k-form-item>
              <k-form-item label="起始募集份额">
                  <k-field-text v-model="formStartData.initVol" data-validate-type="money"/>
              </k-form-item>
              <k-form-footer data-align="center">
                <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ReportPIB.updateReportPIB" data-from="editReportPIBForm"
                  :data-model="formStartData" data-target="reportPIBGrid">
                  <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
                </k-btn>
                <k-btn class="btn-custom-plain" data-functype="CLOSE">
                  <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
              </k-form-footer>
            </k-form>
          </k-popup>
        </el-tab-pane>
      </el-tabs>
      <k-popup ref="addPopup" title="报送数据导入" @data-opened="uploadOpened()">
        <k-form ref="addForm" data-ui="element">
          <k-form-item label="开始日期">
            <k-field-date v-model="uploadBeginDate" data-type="date"  data-date-format="yyyy-MM-dd"  data-value-format="yyyyMMdd" :data-allowblank="false"/>
          </k-form-item>
          <k-form-item label="截止日期">
            <k-field-date v-model="uploadQueryDate" data-type="date"  data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd" :data-allowblank="false"/>
          </k-form-item>
          <k-form-item label="报送数据导入" data-ui="element" data-input-width="500px">
            <k-field-excel-upload data-type="picture" ref="uploadRef" :data-multiple="false" :data-limit='1'
                data-accept=".xlsx,.xls"
                :data-error="onSubmitError" :data-success="onSubmitSuccess"
                :data-auto-upload="false"
                :data-upload-url="`/upload/server/RptApp/reportPPI/${this.activeName === '1' ? 'uploadPPI.json': this.activeName === '2' ? 'uploadPIE.json' : 'uploadPIB.json'}`">
            </k-field-excel-upload>
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-target="this.activeName === '1' ? 'reportPPIGrid': this.activeName === '2' ? 'reportPIEGrid' : 'reportPIBGrid'" ref="submitBtn"
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
  name: "M07RHZJ01",
    data () {
      return {
        uploadBeginDate: '',
        uploadQueryDate: '',
        formProdData: {},
        formStopData: {},
        formStartData: {},
        activeName: '1',
        queryParamDateRange: [],
        queryStartParamDateRange: [],
        queryStopParamDateRange: [],
        prodSearchParam: {
          pbcAssetscode: '',
          prodCode: '',
          peoplebankSubmitcode: ''
        },
        prodStartSearchParam: {
          prodCode: ''
        },
        prodStopSearchParam: {
           prodCode: ''
        }
      }
    },
    computed: {
      queryParam () {
			  return {
				  'pbcAssetscode': this.prodSearchParam.pbcAssetscode,
				  'peoplebankSubmitcode': this.prodSearchParam.peoplebankSubmitcode,
				  'beginDate': this.queryParamDateRange ? this.queryParamDateRange[0] : null,
				  'queryDate': this.queryParamDateRange ? this.queryParamDateRange[1] : null,
				  'prodCode': this.prodSearchParam.prodCode,
			  }
		  },
      queryStartParam() {
        return {
				  'beginDate': this.queryStartParamDateRange ? this.queryStartParamDateRange[0] : null,
				  'queryDate': this.queryStartParamDateRange ? this.queryStartParamDateRange[1] : null,
				  'prodCode': this.prodStartSearchParam.prodCode
			  }
      },
      queryStopParam() {
        return {
				  'beginDate': this.queryStopParamDateRange ? this.queryStopParamDateRange[0] : null,
				  'queryDate': this.queryStopParamDateRange ? this.queryStopParamDateRange[1] : null,
				  'prodCode': this.prodStopSearchParam.prodCode
			  }
      }
    },
    methods: {
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
        this.$refs[this.activeName === '1' ? 'reportPPIGrid': this.activeName === '2' ? 'reportPIEGrid' : 'reportPIBGrid'].load();
      },
      onSubmitError() {
        this.$refs.uploadRef.doReset();
        this.$refs.submitBtn.setIconStyle(1, [])
      },
      uploadOpened() {
        this.uploadBeginDate = ''
        this.uploadQueryDate = ''
      },
      tabClick(val, event) {

      },
      selectRow(row) {
        let formDataStr = this.activeName === '1' ? 'formProdData' : this.activeName === '2' ? 'formStopData' : 'formStartData'
        this[formDataStr] = Object.assign({}, row)
      },
      generatePBFile(type) {
        if (type === 'prodSend' || type === 'zjSend') {
          if (!this.queryParam.beginDate) {
            this.$message.error("请选择起始日期")
            return
          }
          if (!this.queryParam.queryDate) {
            this.$message.error("请选择结束日期")
            return
          }
        } else {
          if (!this.queryStopParam.beginDate) {
            this.$message.error("请选择起始日期")
            return
          }
          if (!this.queryStopParam.queryDate) {
            this.$message.error("请选择结束日期")
            return
          }
        }
        if (type === 'prodSend') {
          this.httpUtil.comnQuery({
              action: "ReportPIB.findReportPIBsByReportDate",
              params: { queryDate: this.queryParam.queryDate }
          }).then(data => {
            if (data.rows.length === 0) {
              this.$message.error(`没有采集数据：${this.queryParam.queryDate}的产品起始募集信息`)
            } else {
              this.httpUtil.download({
                url: '/download/server/RptApp/reportPPI/download.json',
                params: { beginDate: this.queryParam.beginDate, queryDate: this.queryParam.queryDate, sendType: type },
                callback: () => {

                }
              });
            }
          });
        } else {
           this.httpUtil.download({
              url: '/download/server/RptApp/reportPPI/download.json',
              params: { beginDate: this[type === 'zjSend' ? 'queryParam': 'queryStopParam'].beginDate, queryDate: this[type === 'zjSend' ? 'queryParam': 'queryStopParam'].queryDate, sendType: type },
              callback: () => {

              }
           });
        }
      }
    }
}
</script>
