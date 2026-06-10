<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="DwsMonthPrdDtl" v-model="searchParam" data-target="tableGrid">
        <k-form-item label="数据日期">
          <k-field-date v-model="searchParam.dealDate" data-type="month" data-date-format="yyyy-MM" data-value-format="yyyyMM" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="产品代码">
          <k-field-text v-model="searchParam.prdcCd"/>
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
        <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addPopup">
          <md-icon md-src="/static/svg/add.svg" />新增
        </k-btn>
        <k-btn v-if="false" slot="button" ref="uploadBtnRef" data-functype="POPUP" class="btn-custom-plain" data-target="uploadPopup" :load-disabled="false">
          <md-icon>cloud_upload</md-icon>导入
        </k-btn>
        <k-btn slot="button" class="btn-custom-plain" data-functype="EXPORT" data-export-dict="true" data-target="tableGrid" data-export-name="产品明细月表">
          <md-icon>cloud_download</md-icon>导出
        </k-btn>
        <k-btn
						slot="button"
						ref="reloadBtnRef"
						class="btn-custom-plain"
						data-functype="POPUP"
						data-target="handleTaskPopup"
						data-action="DwsProdTTRDBef.updateTaskAppQuery"
						loading-tip="正在重新生成报表，请稍后重试！">
						<md-icon>cloud_download</md-icon>重新生成报表
					</k-btn>
        </div>
        <ReReport ref="reReportRef" :formData="formData" :menuId="menuId" :buttonName="buttonName" />
      </div>
      <k-grid ref="tableGrid" @data-row-select="selectRow" data-action="DwsMonthPrdDtl.findDwsMonthPrdDtls" data-autoload="false"
        data-operate-width="120px"
        data-fixed="right">
        <k-grid-column data-header="ID" data-name="id" data-hidden="true" data-export="false"></k-grid-column>
        <k-grid-column data-header="数据日期" data-name="dealDate" data-width="120" data-export="false"></k-grid-column>
        <k-grid-column data-header="产品代码" data-name="prdcCd"></k-grid-column>
        <k-grid-column data-header="本期总募集金额" data-name="totRaiseTt"></k-grid-column>
        <k-grid-column data-header="本期净募集金额" data-name="netRaiseTt"></k-grid-column>
        <k-grid-column data-header="期末余额" data-name="balEt"></k-grid-column>
        <k-grid-column data-header="本期兑付金额" data-name="curPayTt"></k-grid-column>
        <k-grid-column data-header="本期客户端实现收益总额" data-name="invAcvYieldTt" data-width="120"></k-grid-column>
        <k-grid-column data-header="本期银行端实现收益总额" data-name="bnkAcvYieldTt" data-width="120"></k-grid-column>
        <k-grid-column data-header="募集方式" data-name="raiseType" data-dict="t8_raise_type" data-dict-type="1"></k-grid-column>
        <k-grid-column data-header="投资性质" data-name="invProp" data-dict="t8_prod_classify" data-dict-type="1"></k-grid-column>
        <k-grid-column data-header="运作模式" data-name="optMod" data-dict="prod_mod" data-dict-type="1"></k-grid-column>
        <k-grid-column data-header="产品期限" data-name="prdTrm" data-dict="t8_prod_new_term" data-dict-type="1"></k-grid-column>
        <k-grid-column data-header="投资者持有情况" dataAlign="center">
          <k-grid-column data-header="自然人" data-name="invHldNtr"></k-grid-column>
          <k-grid-column data-header="法人或其他组织" data-name="invHldLgOrg" data-width="140"></k-grid-column>
          <k-grid-column data-header="非金融机构" data-name="invHldNonFncOrg" data-width="180"></k-grid-column>
          <k-grid-column data-header="银行类金融机构" data-name="invHldBnkFncOrg" data-width="140"></k-grid-column>
          <k-grid-column data-header="保险业金融机构" data-name="invHldIsrFncOrg" data-width="140"></k-grid-column>
          <k-grid-column data-header="信托公司" data-name="invHldTstCpn" data-width="100"></k-grid-column>
          <k-grid-column data-header="证券公司" data-name="invHldSctCpn" data-width="100"></k-grid-column>
          <k-grid-column data-header="基金公司" data-name="invHldFndCpn" data-width="100"></k-grid-column>
          <k-grid-column data-header="其他金融机构" data-name="invHldOtrFncOrg" data-width="120"></k-grid-column>
          <k-grid-column data-header="金融机构资产管理产品" data-name="invHldFncOrgPrd" data-width="180"></k-grid-column>
        </k-grid-column>
        <k-grid-column data-header="按币种划分" dataAlign="center">
          <k-grid-column data-header="人民币" data-name="curCny"></k-grid-column>
          <k-grid-column data-header="美元" data-name="curUsd"></k-grid-column>
          <k-grid-column data-header="欧元" data-name="curEur"></k-grid-column>
          <k-grid-column data-header="其他币种" data-name="curOtr"></k-grid-column>
        </k-grid-column>
        <k-grid-column data-header="是否现金管理类" data-name="cshMngF" data-dict="t8_csh_mng_f" data-dict-type="1" data-width="100"></k-grid-column>
        <k-grid-column data-header="平均剩余期限" data-name="avgRmnTrm"></k-grid-column>
        <k-grid-column data-header="是否从母行划转" data-name="isProdTsf" data-dict="1yes2no" data-dict-type="1"></k-grid-column>
        <k-grid-column data-header="划转日的成立金额" data-name="tsfFndAmt"></k-grid-column>
        <k-grid-column data-header="是否为养老产品" data-name="penInvPrdF" data-dict="1yes2no" data-dict-type="1"></k-grid-column>
        <k-grid-column data-header="单月年化收益率" data-name="mthAnlYield"></k-grid-column>
        <k-grid-column data-header="加权价格" data-name="wgtPrice"></k-grid-column>
        <k-grid-column data-header="风险等级" data-name="rskLev" data-dict="risk_rate" data-dict-type="1"></k-grid-column>
        <k-grid-column data-header="是否封闭期在半年以上" data-name="isSealPrdPast" data-dict="1yes2no" data-dict-type="1"></k-grid-column>
        <k-grid-column data-header="起息日" data-name="foundDt" data-type="date" data-date-format="yyyy-MM-dd"></k-grid-column>
        <k-grid-column data-header="到期日" data-name="mtuDt" data-type="date" data-date-format="yyyy-MM-dd"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改产品明细月中间表" data-functype="POPUP" data-size="mini"
                 data-target="editPopup">
            修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="DwsMonthPrdDtl.deleteDwsMonthPrdDtl" data-size="mini"
                 data-type="danger" data-target="tableGrid" :data-confirm="true" data-descript="删除产品明细月中间表">
            删除
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    添加产品明细月中间表弹出框   -->
    <k-popup ref="addPopup" data-title="新增" data-width="800px">
      <k-form ref="addForm" :data-col="2" isFormBodyScreen>
        <k-form-item label="ID" v-show="false">
          <k-field-text v-model="formData.id"/>
        </k-form-item>
        <k-form-item label="数据日期">
          <k-field-date v-model="formData.dealDate" data-type="month" data-date-format="yyyy-MM" data-value-format="yyyy-MM" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="产品代码">
          <k-field-text v-model="formData.prdcCd" :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="本期总募集金额">
          <k-field-text v-model="formData.totRaiseTt"/>
        </k-form-item>
        <k-form-item label="本期净募集金额">
          <k-field-text v-model="formData.netRaiseTt"/>
        </k-form-item>
        <k-form-item label="期末余额">
          <k-field-text v-model="formData.balEt"/>
        </k-form-item>
        <k-form-item label="本期兑付金额">
          <k-field-text v-model="formData.curPayTt"/>
        </k-form-item>
        <k-form-item label="本期客户端实现收益总额">
          <k-field-text v-model="formData.invAcvYieldTt"/>
        </k-form-item>
        <k-form-item label="本期银行端实现收益总额">
          <k-field-text v-model="formData.bnkAcvYieldTt"/>
        </k-form-item>
        <k-form-item label="募集方式">
          <k-field-select v-model="formData.raiseType" data-dict="t8_raise_type"/>
        </k-form-item>
        <k-form-item label="投资性质">
          <k-field-select v-model="formData.invProp" data-dict="t8_prod_classify"/>
        </k-form-item>
        <k-form-item label="运作模式">
          <k-field-select v-model="formData.optMod" data-dict="prod_mod"/>
        </k-form-item>
        <k-form-item label="产品期限">
          <k-field-select v-model="formData.prdTrm" data-dict="t8_prod_new_term"/>
        </k-form-item>
        <k-form-item label="自然人">
          <k-field-text v-model="formData.invHldNtr"/>
        </k-form-item>
        <k-form-item label="法人或其他组织">
          <k-field-text v-model="formData.invHldLgOrg"/>
        </k-form-item>
        <k-form-item label="非金融机构">
          <k-field-text v-model="formData.invHldNonFncOrg"/>
        </k-form-item>
        <k-form-item label="银行类金融机构">
          <k-field-text v-model="formData.invHldBnkFncOrg"/>
        </k-form-item>
        <k-form-item label="保险业金融机构">
          <k-field-text v-model="formData.invHldIsrFncOrg"/>
        </k-form-item>
        <k-form-item label="信托公司">
          <k-field-text v-model="formData.invHldTstCpn"/>
        </k-form-item>
        <k-form-item label="证券公司">
          <k-field-text v-model="formData.invHldSctCpn"/>
        </k-form-item>
        <k-form-item label="基金公司">
          <k-field-text v-model="formData.invHldFndCpn"/>
        </k-form-item>
        <k-form-item label="其他金融机构">
          <k-field-text v-model="formData.invHldOtrFncOrg"/>
        </k-form-item>
        <k-form-item label="金融机构资产管理产品">
          <k-field-text v-model="formData.invHldFncOrgPrd"/>
        </k-form-item>
        <k-form-item label="人民币">
          <k-field-text v-model="formData.curCny"/>
        </k-form-item>
        <k-form-item label="美元">
          <k-field-text v-model="formData.curUsd"/>
        </k-form-item>
        <k-form-item label="欧元">
          <k-field-text v-model="formData.curEur"/>
        </k-form-item>
        <k-form-item label="其他币种">
          <k-field-text v-model="formData.curOtr"/>
        </k-form-item>
        <k-form-item label="是否现金管理类">
          <k-field-select v-model="formData.cshMngF" data-dict="t8_csh_mng_f"/>
        </k-form-item>
        <k-form-item label="平均剩余期限">
          <k-field-text v-model="formData.avgRmnTrm" data-digits="4" data-min-value="0" data-validate-type="number" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="是否从母行划转">
          <k-field-select v-model="formData.isProdTsf" data-dict="1yes2no"/>
        </k-form-item>
        <k-form-item label="划转日的成立金额">
          <k-field-text v-model="formData.tsfFndAmt" data-min-value="0" data-validate-type="money" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="是否为养老产品">
          <k-field-select v-model="formData.penInvPrdF" data-dict="1yes2no"/>
        </k-form-item>
        <k-form-item label="单月年化收益率">
          <k-field-text v-model="formData.mthAnlYield"/>
        </k-form-item>
        <k-form-item label="加权价格">
          <k-field-text v-model="formData.wgtPrice"/>
        </k-form-item>
        <k-form-item label="风险等级">
          <k-field-select v-model="formData.rskLev" data-dict="risk_rate"/>
        </k-form-item>
        <k-form-item label="是否封闭期在半年以上">
          <k-field-select v-model="formData.isSealPrdPast" data-dict="1yes2no"/>
        </k-form-item>
        <k-form-item label="起息日">
          <k-field-date v-model="formData.foundDt"/>
        </k-form-item>
        <k-form-item label="到期日">
          <k-field-date v-model="formData.mtuDt"/>
        </k-form-item>
        <k-form-footer slot="footer" data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="DwsMonthPrdDtl.addDwsMonthPrdDtl" data-from="addForm"
                 :data-model="formDataTransfer">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="uploadPopup" data-title="导入">
      <k-form ref="addForm" data-ui="element">
        <k-form-item label="数据日期" data-ui="element">
          <k-field-date v-model="formData.dealDate" data-type="month" data-date-format="yyyy-MM" data-value-format="yyyy-MM" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-excel-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit='1'
                                data-accept=".xlsx,.xls" :data-error="onSubmitError" :data-success="onSubmitSuccess"
                                :data-auto-upload="false"
                                data-upload-url="upload/server/RptApp/uploadDwsMonthPrdDtl.json">
          </k-field-excel-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" ref="submitBtn"
                 :data-auto-upload="false" data-from="addForm" :data-handler="submitUploadParam">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="handleTaskPopup" data-title="重新生成报表">
			<k-form ref="handleTaskAppForm" data-ui="element">
				<k-form-item label="数据日期" data-ui="element" data-input-width="500px">
					<k-field-date
						v-model="formData.reportDate"
						data-type="date"
						data-date-format="yyyy-MM-dd"
						data-value-format="yyyyMMdd"
						:data-allowblank="false"
					/>
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn class="btn-custom-primary" data-from="editForm" :data-handler="handleTaskApp">
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE">
						<md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
					</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>

    <!--    修改产品明细月中间表弹出框   -->
    <k-popup ref="editPopup" data-title="修改" data-width="800px">
      <k-form ref="editForm" :data-col="2" isFormBodyScreen>
        <k-form-item label="ID" v-show="false">
          <k-field-text v-model="formData.id"/>
        </k-form-item>
        <k-form-item label="数据日期">
          <k-field-text v-model="formData.dealDate" :data-allowblank="false" data-disabled="true"/>
        </k-form-item>
        <k-form-item label="产品代码">
          <k-field-text v-model="formData.prdcCd" :data-allowblank="false" data-disabled="true"/>
        </k-form-item>
        <k-form-item label="本期总募集金额" :class="[handleItemDiff('totRaiseTt')]">
          <k-field-text v-model="formData.totRaiseTt"/>
        </k-form-item>
        <k-form-item label="本期净募集金额" :class="[handleItemDiff('netRaiseTt')]">
          <k-field-text v-model="formData.netRaiseTt"/>
        </k-form-item>
        <k-form-item label="期末余额" :class="[handleItemDiff('balEt')]">
          <k-field-text v-model="formData.balEt"/>
        </k-form-item>
        <k-form-item label="本期兑付金额" :class="[handleItemDiff('curPayTt')]">
          <k-field-text v-model="formData.curPayTt"/>
        </k-form-item>
        <k-form-item label="本期客户端实现收益总额" :class="[handleItemDiff('invAcvYieldTt')]">
          <k-field-text v-model="formData.invAcvYieldTt"/>
        </k-form-item>
        <k-form-item label="本期银行端实现收益总额" :class="[handleItemDiff('bnkAcvYieldTt')]">
          <k-field-text v-model="formData.bnkAcvYieldTt"/>
        </k-form-item>
        <k-form-item label="募集方式" :class="[handleItemDiff('raiseType')]">
          <k-field-select v-model="formData.raiseType" data-dict="t8_raise_type"/>
        </k-form-item>
        <k-form-item label="投资性质" :class="[handleItemDiff('invProp')]">
          <k-field-select v-model="formData.invProp" data-dict="t8_prod_classify"/>
        </k-form-item>
        <k-form-item label="运作模式" :class="[handleItemDiff('optMod')]">
          <k-field-select v-model="formData.optMod" data-dict="prod_mod"/>
        </k-form-item>
        <k-form-item label="产品期限" :class="[handleItemDiff('prdTrm')]">
          <k-field-select v-model="formData.prdTrm" data-dict="t8_prod_new_term"/>
        </k-form-item>
        <k-form-item label="自然人" :class="[handleItemDiff('invHldNtr')]">
          <k-field-text v-model="formData.invHldNtr"/>
        </k-form-item>
        <k-form-item label="法人或其他组织" :class="[handleItemDiff('invHldLgOrg')]">
          <k-field-text v-model="formData.invHldLgOrg"/>
        </k-form-item>
        <k-form-item label="非金融机构" :class="[handleItemDiff('invHldNonFncOrg')]">
          <k-field-text v-model="formData.invHldNonFncOrg"/>
        </k-form-item>
        <k-form-item label="银行类金融机构" :class="[handleItemDiff('invHldBnkFncOrg')]">
          <k-field-text v-model="formData.invHldBnkFncOrg"/>
        </k-form-item>
        <k-form-item label="保险业金融机构" :class="[handleItemDiff('invHldIsrFncOrg')]">
          <k-field-text v-model="formData.invHldIsrFncOrg"/>
        </k-form-item>
        <k-form-item label="信托公司" :class="[handleItemDiff('invHldTstCpn')]">
          <k-field-text v-model="formData.invHldTstCpn"/>
        </k-form-item>
        <k-form-item label="证券公司" :class="[handleItemDiff('invHldSctCpn')]">
          <k-field-text v-model="formData.invHldSctCpn"/>
        </k-form-item>
        <k-form-item label="基金公司" :class="[handleItemDiff('invHldFndCpn')]">
          <k-field-text v-model="formData.invHldFndCpn"/>
        </k-form-item>
        <k-form-item label="其他金融机构" :class="[handleItemDiff('invHldOtrFncOrg')]">
          <k-field-text v-model="formData.invHldOtrFncOrg"/>
        </k-form-item>
        <k-form-item label="金融机构资产管理产品" :class="[handleItemDiff('invHldFncOrgPrd')]">
          <k-field-text v-model="formData.invHldFncOrgPrd"/>
        </k-form-item>
        <k-form-item label="人民币" :class="[handleItemDiff('curCny')]">
          <k-field-text v-model="formData.curCny"/>
        </k-form-item>
        <k-form-item label="美元" :class="[handleItemDiff('curUsd')]">
          <k-field-text v-model="formData.curUsd"/>
        </k-form-item>
        <k-form-item label="欧元" :class="[handleItemDiff('curEur')]">
          <k-field-text v-model="formData.curEur"/>
        </k-form-item>
        <k-form-item label="其他币种" :class="[handleItemDiff('curOtr')]">
          <k-field-text v-model="formData.curOtr"/>
        </k-form-item>
        <k-form-item label="是否现金管理类" :class="[handleItemDiff('cshMngF')]">
          <k-field-select v-model="formData.cshMngF" data-dict="t8_csh_mng_f"/>
        </k-form-item>
        <k-form-item label="平均剩余期限" :class="[handleItemDiff('avgRmnTrm')]">
          <k-field-text v-model="formData.avgRmnTrm" data-digits="4" data-min-value="0" data-validate-type="number" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="是否从母行划转" :class="[handleItemDiff('isProdTsf')]">
          <k-field-select v-model="formData.isProdTsf" data-dict="1yes2no"/>
        </k-form-item>
        <k-form-item label="划转日的成立金额" :class="[handleItemDiff('tsfFndAmt')]">
          <k-field-text v-model="formData.tsfFndAmt" data-min-value="0" data-validate-type="money" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="是否为养老产品" :class="[handleItemDiff('penInvPrdF')]">
          <k-field-select v-model="formData.penInvPrdF" data-dict="1yes2no"/>
        </k-form-item>
        <k-form-item label="单月年化收益率" :class="[handleItemDiff('mthAnlYield')]">
          <k-field-text v-model="formData.mthAnlYield"/>
        </k-form-item>
        <k-form-item label="加权价格" :class="[handleItemDiff('wgtPrice')]">
          <k-field-text v-model="formData.wgtPrice"/>
        </k-form-item>
        <k-form-item label="风险等级" :class="[handleItemDiff('rskLev')]">
          <k-field-select v-model="formData.rskLev" data-dict="risk_rate"/>
        </k-form-item>
        <k-form-item label="是否封闭期在半年以上" :class="[handleItemDiff('isSealPrdPast')]">
          <k-field-select v-model="formData.isSealPrdPast" data-dict="1yes2no"/>
        </k-form-item>
        <k-form-item label="起息日" :class="[handleItemDiff('foundDt')]">
          <k-field-date v-model="formData.foundDt"/>
        </k-form-item>
        <k-form-item label="到期日" :class="[handleItemDiff('mtuDt')]">
          <k-field-date v-model="formData.mtuDt"/>
        </k-form-item>
        <k-form-footer slot="footer" data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="DwsMonthPrdDtl.updateDwsMonthPrdDtl" data-from="editForm"
                 :data-model="formData" data-target="tableGrid" :handle-before="handleBefore">
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
import ProdMixin from "@/pages/pms/subject/mixins/prodMixin.js"
import ReReport from "@/utils/ReReport.vue";
  export default {
    name: "DwsMonthPrdDtl",
    mixins: [ProdMixin],
    components: {
      ReReport
   	},
    data() {
        return {
            searchParam: {
                dealDate: Tools.getPreviousMonth()
            },
            menuId: "M061702",
            buttonName: "重新生成报表",
        };
	  },
    methods: {
      handleBefore() {
        if (this.formNoChangeCb()) {
          this.$refs.editPopup.close();
          return false
        }
        return true
      },
      handleTaskApp() {
        this.$refs.reReportRef.handleReports(this.formData.reportDate);
		  },
    }
  };
</script>
