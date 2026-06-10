<template>
	<div class="py-page">
		<div>
			<k-form-search-customize
				ref="searchRef"
				data-model-name="DwsAstEquInfo"
				v-model="searchParam"
				data-target="tableGrid"
			>
				<k-form-item label="数据日期">
					<k-field-date
						v-model="searchParam.actDt"
						data-type="month"
						data-date-format="yyyy-MM"
						data-value-format="yyyyMM"
						:data-allowblank="false"
					/>
				</k-form-item>
				<k-form-item label="产品代码">
					<k-field-text v-model="searchParam.prodCd" />
				</k-form-item>
				<k-form-item label="内部产品代码">
					<k-field-text v-model="searchParam.prodIntrCd" />
				</k-form-item>
				<k-form-item label="资产代码">
					<k-field-text v-model="searchParam.astCd" />
				</k-form-item>
				<k-form-item label="资产三类编码">
					<k-field-select
						v-model="searchParam.astThrCd"
						data-dict="asst_3_knd"
						data-dict-type="1"
						data-value-field="itemkey"
						data-display-field="itemval"
					/>
				</k-form-item>
				<k-form-item label="借款人代码">
					<k-field-text v-model="searchParam.dbtCd" />
				</k-form-item>
				<k-form-item label="借款人名称">
					<k-field-text v-model="searchParam.dbtNm" />
				</k-form-item>
			</k-form-search-customize>
		</div>
		<div class="py-page-container">
			<div class="table-top-btns">
				<div class="left">
					<k-btn
						class="btn-custom-primary"
						data-functype="POPUP"
						:data-handler="() => (this.formData = {})"
						data-target="addPopup"
					>
						<md-icon md-src="/static/svg/add.svg" />新增
					</k-btn>
					<k-btn
						slot="button"
						ref="uploadBtnRef"
						data-functype="POPUP"
						class="btn-custom-plain"
						data-target="uploadPopup"
						:load-disabled="false"
					>
						<md-icon>cloud_upload</md-icon>导入
					</k-btn>
					<k-btn
						slot="button"
						class="btn-custom-plain"
						data-functype="EXPORT"
						data-export-dict="true"
						data-target="tableGrid"
						data-export-name="月度贷款明细和收益权明细信息"
						data-export-form="searchRef"
					>
						<md-icon>cloud_download</md-icon>导出
					</k-btn>
					<!-- <k-btn slot="button" class="btn-custom-plain" @click="handleTaskApp">
					重新生成报表
					</k-btn>-->
					<k-btn
						slot="button"
						ref="reloadBtnRef"
						class="btn-custom-plain"
						data-functype="POPUP"
						data-target="handleTaskPopup"
						data-action="DwsProdTTRDBef.updateTaskAppQuery"
						loading-tip="正在重新生成报表，请稍后重试！"
					>
						<md-icon>cloud_download</md-icon>重新生成报表
					</k-btn>
				</div>
				<ReReport ref="reReportRef" :formData="formData" :menuId="menuId" :buttonName="buttonName" />
			</div>
			<k-grid
				ref="tableGrid"
				@data-row-select="selectRow"
				data-action="DwsAstEquInfo.findDwsAstEquInfos"
				data-operate-width="120px"
				data-autoload="false"
				data-fixed="right"
				data-dict-type="1"
			>
				<k-grid-column data-header="数据日期" data-name="actDt"></k-grid-column>
				<k-grid-column data-header="产品代码" data-name="prodCd" data-width="140"></k-grid-column>
				<k-grid-column data-header="内部产品代码" data-name="prodIntrCd" data-width="120"></k-grid-column>
				<k-grid-column data-header="资产代码" data-name="astCd" data-width="120"></k-grid-column>
				<k-grid-column data-header="资产三类编码" data-name="astThrCd" data-width="100"></k-grid-column>
				<k-grid-column data-header="资产三类名称" data-name="astThrNm" data-width="210"></k-grid-column>
				<k-grid-column data-header="债权类型/资产负债项目" data-name="astTypDbtPjt" data-width="180">
					<template slot-scope="scope">{{ getDictLabel(scope.row.row.astTypDbtPjt) }}</template>
				</k-grid-column>
				<k-grid-column
					data-header="地区代码"
					data-name="zonCd"
					data-dict="pbc_worldcountry_code"
					data-width="140"
				></k-grid-column>
				<k-grid-column data-header="借款人名称" data-name="dbtNm" data-width="100"></k-grid-column>
				<k-grid-column data-header="借款人类型" data-name="dbtTyp" data-dict="pbc_org_typ" data-width="120"></k-grid-column>
				<k-grid-column data-header="借款人代码" data-name="dbtCd" data-width="100"></k-grid-column>
				<k-grid-column
					data-header="行业类型"
					data-name="idtTyp"
					data-dict="pbc_eco_inds_typ_det_1"
					data-width="150"
				></k-grid-column>
				<k-grid-column
					data-header="企业出资人经济成分"
					data-name="invIcmPct"
					data-dict="enterSponsorEcoSector_dk"
					data-width="160"
				></k-grid-column>
				<k-grid-column data-header="企业规模" data-name="etpScl" data-dict="pbc_enterprise_scale"></k-grid-column>
				<k-grid-column data-header="成立日" data-name="opnDt" data-width="100"></k-grid-column>
				<k-grid-column data-header="到期日" data-name="endDt" data-width="100"></k-grid-column>
				<k-grid-column data-header="展期到期日" data-name="expEndDt" data-width="100"></k-grid-column>
				<k-grid-column
					data-header="利率是否固定"
					data-name="intrRtTyp"
					data-width="120"
					data-dict="pbc_rate_typ"
				></k-grid-column>
				<k-grid-column data-header="利率水平/持股比例" data-name="intrRt" data-width="140"></k-grid-column>
				<k-grid-column
					data-header="担保方式"
					data-name="gurTyp"
					data-dict="pbc_loan_grt_typ"
					data-width="140"
				></k-grid-column>
				<k-grid-column data-header="币种" data-name="ccyCd"></k-grid-column>
				<k-grid-column data-header="起息本金" data-name="intrAmtBal" data-width="160"></k-grid-column>
				<k-grid-column data-header="余额" data-name="amtBal" data-width="160"></k-grid-column>
				<k-grid-column
					data-header="登记交易场所"
					data-name="rgtTrdPlc"
					data-dict="registerTradingPlace"
					data-width="160"
				></k-grid-column>
				<k-grid-column data-header="登记交易场所代码" data-name="rgtTrdPlcCd" data-width="160"></k-grid-column>
				<k-grid-column
					data-header="股权投资方式"
					data-name="shrHldInvTyp"
					data-dict="right_invest_way"
					data-width="100"
				></k-grid-column>
				<k-grid-column data-header="股权出让方代码" data-name="shrHldTsfCd" data-width="160"></k-grid-column>
				<k-grid-column data-header="股权出让方名称" data-name="shrHldTsfNm" data-width="160"></k-grid-column>
				<k-grid-column
					data-header="投资退出方式"
					data-name="invOutTyp"
					data-dict="invest_exit_way"
					data-width="100"
				></k-grid-column>
				<k-grid-column
					data-align="left"
					data-header="出让机构出表标识"
					data-name="transOrgOutTableF"
					data-dict="pbc_conf_flag"
					data-width="120"
				></k-grid-column>
				<k-grid-column
					data-align="left"
					data-header="出让机构回购标识"
					data-name="transOrgBuyBackF"
					data-dict="pbc_conf_flag"
					data-width="120"
				></k-grid-column>
				<k-grid-column
					data-align="left"
					data-header="基础资产投向部门"
					data-name="baseAssetTransDep"
					data-dict="pbc_org_typ"
					data-width="120"
				></k-grid-column>
				<k-grid-column data-header="原始协议金额" data-name="baseAssetOriProtAmt" data-width="160"></k-grid-column>
				<k-grid-column
					data-header="科技相关产业标识"
					data-name="techFlag"
					data-dict="pbc_conf_flag"
					data-width="140"
				></k-grid-column>
				<k-grid-column
					data-header="绿色领域标识"
					data-name="greenFlag"
					data-dict="pbc_conf_flag"
					data-width="140"
				></k-grid-column>
				<k-grid-column
					data-header="普惠领域标识"
					data-name="specFlag"
					data-dict="pbc_conf_flag"
					data-width="140"
				></k-grid-column>
				<k-grid-column
					data-header="养老产业标识"
					data-name="agedFlag"
					data-dict="pbc_conf_flag"
					data-width="140"
				></k-grid-column>
				<k-grid-column
					data-header="数字经济核心产业标识"
					data-name="numCoreFlag"
					data-dict="pbc_conf_flag"
					data-width="140"
				></k-grid-column>
				<template slot="operate" slot-scope="scope">
					<k-btn
						class="btn-custom-text"
						data-descript="修改月度贷款明细和收益权明细信息"
						data-functype="POPUP"
						data-size="mini"
						data-target="editPopup"
					>修改</k-btn>
					<k-btn
						class="btn-custom-text"
						data-functype="SUBMIT"
						data-action="DwsAstEquInfo.deleteDwsAstEquInfo"
						data-size="mini"
						data-type="danger"
						data-target="tableGrid"
						:data-confirm="true"
						data-descript="删除月度贷款明细和收益权明细信息"
					>删除</k-btn>
				</template>
			</k-grid>
		</div>

		<!--    添加贷款明细和收益权明细中间表弹出框   -->
		<k-popup ref="addPopup" data-title="新增月度贷款明细和收益权明细信息">
			<k-form ref="addForm" :data-col="2" data-label-width="160px">
				<k-form-item label="数据日期">
					<k-field-date
						v-model="formData.actDt"
						data-type="month"
						data-date-format="yyyy-MM"
						data-value-format="yyyy-MM"
						:data-allowblank="false"
					/>
				</k-form-item>
				<k-form-item label="内部产品代码">
					<k-field-text v-model="formData.prodIntrCd" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="资产代码">
					<k-field-text v-model="formData.astCd" />
				</k-form-item>
				<k-form-item label="资产三类编码">
					<k-field-select v-model="formData.astThrCd" data-dict="asst_3_knd" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="债权类型/资产负债项目">
					<k-field-select
						v-model="formData.astTypDbtPjt"
						:data-data="astTypDbtPjtDict"
						data-dict-type="1"
					/>
				</k-form-item>
				<k-form-item label="地区代码">
					<k-field-select v-model="formData.zonCd" data-dict="pbc_worldcountry_code" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="借款人名称">
					<k-field-text v-model="formData.dbtNm" />
				</k-form-item>
				<k-form-item label="借款人类型">
					<k-field-select
						v-model="formData.dbtTyp"
						data-dict="pbc_org_typ"
						data-dict-type="1"
						@data-on-change="changeData"
					/>
				</k-form-item>
				<k-form-item label="借款人代码">
					<k-field-text v-model="formData.dbtCd" />
				</k-form-item>
				<k-form-item label="行业类型">
					<k-field-select
						v-model="formData.idtTyp"
						data-dict="pbc_eco_inds_typ_det_1"
						data-dict-type="1"
					/>
				</k-form-item>
				<k-form-item label="企业出资人经济成分">
					<k-field-select
						v-model="formData.invIcmPct"
						data-dict="enterSponsorEcoSector_dk"
						data-dict-type="1"
					/>
				</k-form-item>
				<k-form-item label="企业规模">
					<k-field-select v-model="formData.etpScl" data-dict="pbc_enterprise_scale" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="成立日">
					<k-field-date v-model="formData.opnDt" />
				</k-form-item>
				<k-form-item label="到期日">
					<k-field-date v-model="formData.endDt" />
				</k-form-item>
				<k-form-item label="展期到期日">
					<k-field-date v-model="formData.expEndDt" />
				</k-form-item>
				<k-form-item label="利率是否固定">
					<k-field-select v-model="formData.intrRtTyp" data-dict="pbc_rate_typ" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="利率水平/持股比例">
					<k-field-text v-model="formData.intrRt" data-dict-type="1" data-validate-type="number" />
				</k-form-item>
				<k-form-item label="担保方式">
					<k-field-select v-model="formData.gurTyp" data-dict="pbc_loan_grt_typ" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="币种">
					<k-field-text v-model="formData.ccyCd" />
				</k-form-item>
				<k-form-item label="起息本金">
					<k-field-text v-model="formData.intrAmtBal" data-validate-type="number" />
				</k-form-item>
				<k-form-item label="余额">
					<k-field-text v-model="formData.amtBal" data-validate-type="number" />
				</k-form-item>
				<k-form-item label="登记交易场所">
					<k-field-select
						v-model="formData.rgtTrdPlc"
						data-dict="registerTradingPlace"
						data-dict-type="1"
					/>
				</k-form-item>
				<k-form-item label="登记交易场所代码">
					<k-field-text v-model="formData.rgtTrdPlcCd" />
				</k-form-item>
				<k-form-item label="股权投资方式">
					<k-field-select
						v-model="formData.shrHldInvTyp"
						data-dict="right_invest_way"
						data-dict-type="1"
					/>
				</k-form-item>
				<k-form-item label="股权出让方代码">
					<k-field-text v-model="formData.shrHldTsfCd" />
				</k-form-item>
				<k-form-item label="股权出让方名称">
					<k-field-text v-model="formData.shrHldTsfNm" />
				</k-form-item>
				<k-form-item label="投资退出方式">
					<k-field-select v-model="formData.invOutTyp" data-dict="invest_exit_way" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="出让机构出表标识">
					<k-field-select
						v-model="formData.transOrgOutTableF"
						:data-disabled="false"
						data-dict="pbc_conf_flag"
						data-dict-type="1"
					/>
				</k-form-item>
				<k-form-item label="出让机构回购标识">
					<k-field-select
						v-model="formData.transOrgBuyBackF"
						:data-disabled="false"
						data-dict="pbc_conf_flag"
						data-dict-type="1"
					/>
				</k-form-item>
				<k-form-item label="基础资产投向部门">
					<k-field-select
						v-model="formData.baseAssetTransDep"
						:data-disabled="false"
						data-dict="pbc_org_typ"
						data-dict-type="1"
					/>
				</k-form-item>
				<k-form-item label="原始协议金额">
					<k-field-text v-model="formData.baseAssetOriProtAmt" />
				</k-form-item>
				<!--追加金融“五篇大文章”数据标识 程晓鹏 20250304 modify-->
				<k-form-item label="科技相关产业标识">
					<k-field-select v-model="formData.techFlag" data-dict="pbc_conf_flag" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="绿色领域标识">
					<k-field-select v-model="formData.greenFlag" data-dict="pbc_conf_flag" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="普惠领域标识">
					<k-field-select v-model="formData.specFlag" data-dict="pbc_conf_flag" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="养老产业标识">
					<k-field-select v-model="formData.agedFlag" data-dict="pbc_conf_flag" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="数字经济核心产业标识">
					<k-field-select v-model="formData.numCoreFlag" data-dict="pbc_conf_flag" data-dict-type="1" />
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="DwsAstEquInfo.addDwsAstEquInfo"
						data-from="addForm"
						:data-model="formDataTransfer"
						data-target="tableGrid"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE">
						<md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
					</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>

		<k-popup ref="uploadPopup" data-title="导入">
			<k-form ref="addForm" data-ui="element">
				<k-form-item label="数据日期" data-ui="element">
					<k-field-date
						v-model="formData.dealDate"
						data-type="month"
						data-date-format="yyyy-MM"
						data-value-format="yyyy-MM"
						:data-allowblank="false"
					/>
				</k-form-item>
				<k-form-item label="报表名称">
					<k-field-select
						v-model="formData.fileType"
						data-dict="excelImportType"
						:data-allowblank="false"
						data-dict-type="1"
					/>
				</k-form-item>
				<k-form-item label="附件" data-ui="element" data-input-width="500px">
					<k-field-excel-upload
						data-type="file"
						ref="uploadRef"
						:data-multiple="false"
						:data-limit="1"
						data-accept=".xlsx,.xls"
						:data-error="onSubmitError"
						:data-success="onSubmitSuccess"
						:data-auto-upload="false"
						data-upload-url="upload/server/RptApp/uploadDwsAstEquInfo.json"
					></k-field-excel-upload>
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						ref="submitBtn"
						:data-auto-upload="false"
						data-from="addForm"
						:data-handler="submitUploadParamCheck"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE">
						<md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
					</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>

		<!--    修改贷款明细和收益权明细中间表弹出框   -->
		<k-popup ref="editPopup" data-title="修改">
			<k-form ref="editForm" :data-col="2" data-label-width="160px">
				<k-form-item label="数据日期">
					<k-field-date v-model="formData.actDt" :data-allowblank="false" data-disabled="true" />
				</k-form-item>
				<k-form-item label="产品代码">
					<k-field-text v-model="formData.prodCd" :data-allowblank="false" data-disabled="true" />
				</k-form-item>
				<k-form-item label="内部产品代码">
					<k-field-text v-model="formData.prodIntrCd" :data-allowblank="false" data-disabled="true" />
				</k-form-item>
				<k-form-item label="资产代码" :class="[handleItemDiff('astCd')]">
					<k-field-text v-model="formData.astCd" />
				</k-form-item>
				<k-form-item label="资产三类编码" :class="[handleItemDiff('astThrCd')]">
					<k-field-select v-model="formData.astThrCd" data-dict="asst_3_knd" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="债权类型/资产负债项目" :class="[handleItemDiff('astTypDbtPjt')]">
					<k-field-select
						v-model="formData.astTypDbtPjt"
						:data-data="astTypDbtPjtDict"
						data-dict-type="1"
					/>
				</k-form-item>
				<k-form-item label="地区代码" :class="[handleItemDiff('zonCd')]">
					<k-field-select v-model="formData.zonCd" data-dict="pbc_worldcountry_code" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="借款人名称" :class="[handleItemDiff('dbtNm')]">
					<k-field-text v-model="formData.dbtNm" />
				</k-form-item>
				<k-form-item label="借款人类型" :class="[handleItemDiff('dbtTyp')]">
					<k-field-select
						v-model="formData.dbtTyp"
						data-dict="pbc_org_typ"
						data-dict-type="1"
						@data-on-change="changeData"
					/>
				</k-form-item>
				<k-form-item label="借款人代码" :class="[handleItemDiff('dbtCd')]">
					<k-field-text v-model="formData.dbtCd" />
				</k-form-item>
				<k-form-item label="行业类型" :class="[handleItemDiff('idtTyp')]">
					<k-field-select
						v-model="formData.idtTyp"
						data-dict="pbc_eco_inds_typ_det_1"
						data-dict-type="1"
					/>
				</k-form-item>
				<k-form-item label="企业出资人经济成分" :class="[handleItemDiff('invIcmPct')]">
					<k-field-select
						v-model="formData.invIcmPct"
						data-dict="enterSponsorEcoSector_dk"
						data-dict-type="1"
					/>
				</k-form-item>
				<k-form-item label="企业规模" :class="[handleItemDiff('etpScl')]">
					<k-field-select v-model="formData.etpScl" data-dict="pbc_enterprise_scale" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="成立日" :class="[handleItemDiff('opnDt')]">
					<k-field-date v-model="formData.opnDt" />
				</k-form-item>
				<k-form-item label="到期日" :class="[handleItemDiff('endDt')]">
					<k-field-date v-model="formData.endDt" />
				</k-form-item>
				<k-form-item label="展期到期日" :class="[handleItemDiff('expEndDt')]">
					<k-field-date v-model="formData.expEndDt" />
				</k-form-item>
				<k-form-item label="利率是否固定" :class="[handleItemDiff('intrRtTyp')]">
					<k-field-select v-model="formData.intrRtTyp" data-dict="pbc_rate_typ" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="利率水平/持股比例" :class="[handleItemDiff('intrRt')]">
					<k-field-text v-model="formData.intrRt" data-dict-type="1" data-validate-type="number" />
				</k-form-item>
				<k-form-item label="担保方式" :class="[handleItemDiff('gurTyp')]">
					<k-field-select v-model="formData.gurTyp" data-dict="pbc_loan_grt_typ" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="币种" :class="[handleItemDiff('ccyCd')]">
					<k-field-text v-model="formData.ccyCd" />
				</k-form-item>
				<k-form-item label="起息本金" :class="[handleItemDiff('intrAmtBal')]">
					<k-field-text v-model="formData.intrAmtBal" data-validate-type="number" />
				</k-form-item>
				<k-form-item label="余额" :class="[handleItemDiff('amtBal')]">
					<k-field-text v-model="formData.amtBal" data-validate-type="number" />
				</k-form-item>
				<k-form-item label="登记交易场所" :class="[handleItemDiff('rgtTrdPlc')]">
					<k-field-select
						v-model="formData.rgtTrdPlc"
						data-dict="registerTradingPlace"
						data-dict-type="1"
					/>
				</k-form-item>
				<k-form-item label="登记交易场所代码" :class="[handleItemDiff('rgtTrdPlcCd')]">
					<k-field-text v-model="formData.rgtTrdPlcCd" />
				</k-form-item>
				<k-form-item label="股权投资方式" :class="[handleItemDiff('shrHldInvTyp')]">
					<k-field-select
						v-model="formData.shrHldInvTyp"
						data-dict="right_invest_way"
						data-dict-type="1"
					/>
				</k-form-item>
				<k-form-item label="股权出让方代码" :class="[handleItemDiff('shrHldTsfCd')]">
					<k-field-text v-model="formData.shrHldTsfCd" />
				</k-form-item>
				<k-form-item label="股权出让方名称" :class="[handleItemDiff('shrHldTsfNm')]">
					<k-field-text v-model="formData.shrHldTsfNm" />
				</k-form-item>
				<k-form-item label="投资退出方式" :class="[handleItemDiff('invOutTyp')]">
					<k-field-select v-model="formData.invOutTyp" data-dict="invest_exit_way" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="出让机构出表标识" :class="[handleItemDiff('transOrgOutTableF')]">
					<k-field-select
						v-model="formData.transOrgOutTableF"
						:data-disabled="false"
						data-dict="pbc_conf_flag"
						data-dict-type="1"
					/>
				</k-form-item>
				<k-form-item label="出让机构回购标识" :class="[handleItemDiff('transOrgBuyBackF')]">
					<k-field-select
						v-model="formData.transOrgBuyBackF"
						:data-disabled="false"
						data-dict="pbc_conf_flag"
						data-dict-type="1"
					/>
				</k-form-item>
				<k-form-item label="基础资产投向部门" :class="[handleItemDiff('baseAssetTransDep')]">
					<k-field-select
						v-model="formData.baseAssetTransDep"
						:data-disabled="false"
						data-dict="pbc_org_typ"
						data-dict-type="1"
					/>
				</k-form-item>
				<k-form-item label="原始协议金额">
					<k-field-text v-model="formData.baseAssetOriProtAmt" />
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
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="DwsAstEquInfo.updateDwsAstEquInfo"
						data-from="editForm"
						:data-model="formData"
						data-target="tableGrid"
						:handle-before="handleBefore"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE">
						<md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
					</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>
		<!-- <GenerateFormAgainDialog ref="formAgainRef" paraid="90000052005" menuId="M061804" buttonName="重新生成报表" /> -->
		<k-popup ref="handleTaskPopup" data-title="重新生成报表">
			<k-form ref="handleTaskAppForm" data-ui="element">
				<k-form-item label="数据日期" data-ui="element" data-input-width="500px">
					<k-field-date
						v-model="formData.reportDate"
						data-type="month"
						data-date-format="yyyy-MM"
						data-value-format="yyyy-MM"
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
	</div>
</template>

<script>
import Tools from "@/utils/tools.js";
import AssetMixin from "@/pages/pms/subject/mixins/assetMixin.js";
import GenerateFormAgainMixin from "@/pages/pms/subject/mixins/generateFormAgainMixin.js";
import moment from "moment";
import ReReport from "@/utils/ReReport.vue";
export default {
	name: "DwsAstEquInfo",
	mixins: [AssetMixin, GenerateFormAgainMixin],
	components: {
      ReReport
   	},
	data() {
		return {
			searchParam: {
				actDt: Tools.getPreviousMonth(),
			},
			astTypDbtPjtDict: [],
			formData: { reportDate: "" },
			menuId: "M061804",
            buttonName: "重新生成报表",
		};
	},
	mounted() {
		this.getMultDict();
	},
	computed: {
		lastDay() {
			if (this.formData.reportDate) {
				return moment([this.formData.reportDate.split("-")[0], this.formData.reportDate.split("-")[1] - 1])
					.endOf("month")
					.format("YYYYMMDD");
			}
			return "";
		},
	},
	methods: {
		handleTaskApp() {
			this.$refs.reReportRef.handleReports(this.lastDay);
		},
		// handleTaskApp() {
		// 	if (this.$refs.handleTaskAppForm.validate()) {
		// 		this.$refs.reloadBtnRef.setIconStyle(0);
		// 		this.httpUtil
		// 			.comnUpdate({
		// 				action: "DwsProdTTRDBef.updateTaskAppQuery",
		// 				async: true,
		// 				params: { menuId: "M061804", buttonName: "重新生成报表", reportDate: this.lastDay, paraid: "90000052005" },
		// 				successAlert: false,
		// 				dataAfterSuccess: (reData) => {
		// 					Tools.alertTime(reData.returnmsg, "success", 0);
		// 				},
		// 			})
		// 			.then((data) => {
		// 				this.$refs.reloadBtnRef.setIconStyle(1);
		// 			})
		// 			.catch((err) => {
		// 				console.log(err, "err");
		// 				this.$refs.reloadBtnRef.setIconStyle(1);
		// 			});
		// 		setTimeout(() => {
		// 			this.$refs.handleTaskPopup.close();
		// 		}, 300);
		// 	}
		// },
		submitUploadParamCheck() {
			//文件上传校验
			let validate = this.$refs.addForm.validate();
			if (validate) {
				let temp = document.getElementsByClassName("upload-demo");
				let lis = temp[0].childNodes[1].childNodes.length;
				if (lis > 0) {
					let formData = { dealDate: this.lastDayDeal, fileType: this.formData.fileType }; //在原有功能基础上追加fileType  程晓鹏 2025.01.22 modify
					this.$refs.uploadRef.upload(formData);
					this.$refs.uploadBtnRef.setIconStyle(0);
					setTimeout(() => {
						this.$refs.uploadPopup.close();
					}, 300);
				} else {
					this.$message.error("上传文件不能为空!");
				}
			}
			return false;
		},
		handleBefore() {
			if (this.formNoChangeCb()) {
				this.$refs.editPopup.close();
				return false;
			}
			return true;
		},
		getMultDict() {
			Promise.all([this.httpUtil.dict("typeOfDebt"), this.httpUtil.dict("pbc_balance_proj_prt_dk"), this.httpUtil.dict("pbc_balance_proj_rev")]).then(
				(res) => {
					this.astTypDbtPjtDict = res[0].concat(res[1]).concat(res[2]);
				}
			);
		},
		getDictLabel(v) {
			const itemval = (this.astTypDbtPjtDict.find((item) => item.itemkey == v) || {}).itemval;
			return itemval ? v + " " + itemval : "";
		},
		changeData(dbtTyp) {
			if (dbtTyp == "") {
				this.$set(this.formData, "invIcmPct", "");
			}
		},
	},
};
</script>
