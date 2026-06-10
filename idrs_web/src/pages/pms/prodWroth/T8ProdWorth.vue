<template>
	<div class="py-page">
		<div>
			<k-form-search-customize data-model-name="t8ProdWorth" v-model="searchParam" data-target="t8ProdWorthGrid">
				<k-form-item label="产品代码">
					<k-field-select
						v-model="searchParam.prodCd"
						data-action="T8ProdWorth.findProdWorthCdAndNm"
						data-display-field="prodCd,prodNm"
						data-value-field="prodCd"
					/>
				</k-form-item>
				<k-form-item label="估值日期">
					<k-field-date v-model="searchParam.navDt" :data-default-value="this.currentDate"></k-field-date>
				</k-form-item>
				<k-form-item label="披露日期" v-show="false">
					<k-field-date v-model="searchParam.isuDt"></k-field-date>
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
						slot="button"
						v-if="global.isShowAuthorityButton('T8ProdWorth.addT8ProdWorth')"
						data-target="addT8ProdWorthPopup"
					>
						<md-icon md-src="/static/svg/add.svg" />新增
					</k-btn>
					<k-btn
						slot="button"
						class="btn-custom-plain"
						data-functype="EXPORT"
						data-target="t8ProdWorthGrid"
						:data-export-name="'产品净值信息'"
					>
						<md-icon>cloud_download</md-icon>
						导出
					</k-btn>
				</div>
			</div>
			<k-grid
				ref="t8ProdWorthGrid"
				@data-row-select="selectRow"
				data-operate-width="250px"
				data-action="T8ProdWorth.findT8ProdWorths"
				:data-autoload="false"
			>
				<k-grid-column data-header="产品代码" data-name="prodCd" data-width="110"></k-grid-column>
				<k-grid-column data-header="产品名称" data-name="prodNm" data-width="200"></k-grid-column>
				<k-grid-column data-header="估值日期" data-name="navDt" data-type="date"></k-grid-column>
				<k-grid-column data-header="披露日期" data-name="isuDt" data-type="date"></k-grid-column>
				<k-grid-column data-header="单位净值" data-name="untNav"></k-grid-column>
				<k-grid-column data-header="累计单位净值" data-name="acmNav"></k-grid-column>
				<k-grid-column data-header="总资产" data-name="totAst"></k-grid-column>
				<k-grid-column data-header="总负债" data-name="totLbl"></k-grid-column>
				<k-grid-column data-header="产品总净值" data-name="totNav"></k-grid-column>
				<k-grid-column data-header="总份额" data-name="totLot"></k-grid-column>

				<template slot="operate" slot-scope="scope">
					<k-btn
						class="btn-custom-text"
						data-descript="产品净值信息详情"
						data-functype="POPUP"
						data-size="mini"
						:data-handler="beforeDate"
						data-target="detailT8ProdWorthPopup"
					>
						详情
					</k-btn>
					<k-btn
						class="btn-custom-text"
						data-descript="修改产品净值信息"
						data-functype="POPUP"
						data-size="mini"
						:data-handler="beforeDate"
						v-if="global.isShowAuthorityButton('T8ProdWorth.updateT8ProdWorth')"
						data-target="editT8ProdWorthPopup"
					>
						修改
					</k-btn>
					<k-btn
						class="btn-custom-text"
						data-functype="SUBMIT"
						data-action="T8ProdWorth.deleteT8ProdWorth"
						data-size="mini"
						v-if="global.isShowAuthorityButton('T8ProdWorth.deleteT8ProdWorth')"
						data-type="danger"
						data-target="t8ProdWorthGrid"
						:data-confirm="true"
						data-descript="删除产品净值信息"
					>
						删除
					</k-btn>
				</template>
			</k-grid>
		</div>

		<!--    添加T8ProdWorth弹出框   -->
		<k-popup ref="addT8ProdWorthPopup" data-title="新增">
			<k-form ref="addT8ProdWorthForm" :data-col="2" isFormBodyScreen>
				<!--        <k-form-item label="id" :hidden="true">-->
				<!--          <k-field-text v-model="formData.id"/>-->
				<!--        </k-form-item>-->
				<k-form-item label="产品代码">
					<k-field-select
					    ref="refProd"
						v-model="formData.prodCd"
						data-action="T8ProdWorth.findProdWorthCdAndNm"
						data-display-field="prodCd,prodNm"
						data-value-field="prodCd"
						:data-allowblank="false"
						:data-remote="true"
						:data-remote-paging="true"
						@data-on-change="changeProdCd"
					/>
				</k-form-item>
				<k-form-item label="产品名称">
					<k-field-text v-model="formData.prodNm" />
				</k-form-item>
				<k-form-item label="估值日期">
					<k-field-date
						v-model="formData.navDt"
						data-date-format="yyyy-MM-dd"
						:data-allowblank="false"
						:data-auto-validate="true"
						:data-default-value="currentDate"
					/>
				</k-form-item>
				<k-form-item label="披露日期" v-show="false">
					<k-field-date
						v-model="formData.isuDt"
						data-date-format="yyyy-MM-dd"
						:data-default-value="currentDate"
					/>
				</k-form-item>
				<k-form-item label="单位净值">
					<k-field-text
						v-model="formData.untNav"
						:data-allowblank="false"
						:data-auto-validate="true"
						:data-max-length="21"
						data-digits="8"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="累计单位净值">
					<k-field-text
						v-model="formData.acmNav"
						:data-allowblank="false"
						:data-auto-validate="true"
						:data-max-length="21"
						data-digits="8"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="总资产">
					<k-field-text
						v-model="formData.totAst"
						:data-allowblank="false"
						:data-auto-validate="true"
						:data-max-length="21"
						data-digits="2"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="总负债">
					<k-field-text
						v-model="formData.totLbl"
						:data-allowblank="false"
						:data-auto-validate="true"
						:data-max-length="21"
						data-digits="2"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="产品总净值">
					<k-field-text
						v-model="formData.totNav"
						:data-allowblank="false"
						:data-auto-validate="true"
						:data-max-length="21"
						data-digits="2"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="总份额">
					<k-field-text
						v-model="formData.totLot"
						:data-allowblank="false"
						:data-auto-validate="true"
						:data-max-length="21"
						data-digits="2"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="近七日年化收益率（%）">
					<k-field-text
						v-model="formData.rct7dAnlYld"
						:data-allowblank="false"
						:data-auto-validate="true"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="近一个月年化收益率（%）">
					<k-field-text
						v-model="formData.rct1mAnlYld"
						:data-allowblank="false"
						:data-auto-validate="true"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="近三个月年化收益率（%）">
					<k-field-text
						v-model="formData.rct3mAnlYld"
						:data-allowblank="false"
						:data-auto-validate="true"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="近半年年化收益率（%）">
					<k-field-text
						v-model="formData.rct6mAnlYld"
						:data-allowblank="false"
						:data-auto-validate="true"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="近一年年化收益率（%）">
					<k-field-text
						v-model="formData.rct1yAnlYld"
						:data-allowblank="false"
						:data-auto-validate="true"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="近二年年化收益率（%）">
					<k-field-text
						v-model="formData.rct2yAnlYld"
						:data-allowblank="false"
						:data-auto-validate="true"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="近三年年化收益率（%）">
					<k-field-text
						v-model="formData.rct3yAnlYld"
						:data-allowblank="false"
						:data-auto-validate="true"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="近五年年化收益率（%）">
					<k-field-text
						v-model="formData.rct5yAnlYld"
						:data-allowblank="false"
						:data-auto-validate="true"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="成立至今收益率（%）">
					<k-field-text
						v-model="formData.setUpTilNowYld"
						:data-allowblank="false"
						:data-auto-validate="true"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="上一开放日至今收益率（%）">
					<k-field-text
						v-model="formData.lastOpnDayTilNowYld"
						:data-allowblank="false"
						:data-auto-validate="true"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="今年年化收益率（%）">
					<k-field-text
						v-model="formData.nowAnlYld"
						:data-allowblank="false"
						:data-auto-validate="true"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="近1日净值增长率（%）">
					<k-field-text
						v-model="formData.rct1dGrwRat"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="近7日净值增长率（%）">
					<k-field-text
						v-model="formData.rct7dGrwRat"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="近一个月净值增长率（%）">
					<k-field-text
						v-model="formData.rct1mGrwRat"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="近三个月净值增长率（%）">
					<k-field-text
						v-model="formData.rct3mGrwRat"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="近半年净值增长率（%）">
					<k-field-text
						v-model="formData.rct6mGrwRat"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="近一年净值增长率（%）">
					<k-field-text
						v-model="formData.rct1yGrwRat"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="近两年净值增长率（%）">
					<k-field-text
						v-model="formData.rct2yGrwRat"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="近三年净值增长率（%）">
					<k-field-text
						v-model="formData.rct3yGrwRat"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="近五年净值增长率（%）">
					<k-field-text
						v-model="formData.rct5yGrwRat"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="成立至今净值增长率（%）">
					<k-field-text
						v-model="formData.setUpTilNowGrwRat"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="上一开放日至今净值增长率（%）">
					<k-field-text
						v-model="formData.lastOpnDayTilNowGrwRat"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="今年净值增长率（%）">
					<k-field-text
						v-model="formData.nowGrwRat"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="折算单位净值">
					<k-field-text
						v-model="formData.cnvUntNav"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="成立至今最大回撤">
					<k-field-text
						v-model="formData.setUpTilNowMaxWdw"
						:data-max-length="17"
						data-digits="8"
						data-integer-length="8"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="成立至今波动率（%）">
					<k-field-text
						v-model="formData.setUpTilNowFlctRat"
						:data-max-length="17"
						data-digits="8"
						data-integer-length="8"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="T8ProdWorth.addT8ProdWorth"
						data-from="addT8ProdWorthForm"
						:data-model="formData"
						data-target="t8ProdWorthGrid"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE">
						<md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn
					>
				</k-form-footer>
			</k-form>
		</k-popup>

		<!--    修改T8ProdWorth弹出框   -->
		<k-popup ref="editT8ProdWorthPopup" data-title="修改">
			<k-form ref="editT8ProdWorthForm" :data-col="2" isFormBodyScreen>
				<k-form-item label="产品代码" :class="[handleItemDiff('prodCd')]">
					<k-field-select
						v-model="formData.prodCd"
						data-action="T8ProdWorth.findProdWorthCdAndNm"
						data-display-field="prodCd,prodNm"
						data-value-field="prodCd"
						:data-disabled="true"
						:data-allowblank="false"
						:data-remote="true"
						:data-remote-paging="true"
					/>
				</k-form-item>
				<k-form-item label="产品名称" :class="[handleItemDiff('prodNm')]">
					<k-field-text v-model="formData.prodNm" :data-disabled="true" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="估值日期" :class="[handleItemDiff('navDt')]">
					<k-field-date v-model="formData.navDt" :data-allowblank="false" data-date-format="yyyy-MM-dd" />
				</k-form-item>
				<k-form-item label="披露日期" v-show="false" :class="[handleItemDiff('isuDt')]">
					<k-field-date v-model="formData.isuDt" data-date-format="yyyy-MM-dd" />
				</k-form-item>
				<k-form-item label="单位净值" :class="[handleItemDiff('untNav')]">
					<k-field-text
						v-model="formData.untNav"
						:data-allowblank="false"
						:data-max-length="21"
						data-digits="8"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="累计单位净值" :class="[handleItemDiff('acmNav')]">
					<k-field-text
						v-model="formData.acmNav"
						:data-allowblank="false"
						:data-max-length="21"
						data-digits="8"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="总资产" :class="[handleItemDiff('totAst')]">
					<k-field-text
						v-model="formData.totAst"
						:data-allowblank="false"
						:data-max-length="21"
						data-digits="2"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="总负债" :class="[handleItemDiff('totLbl')]">
					<k-field-text
						v-model="formData.totLbl"
						:data-allowblank="false"
						:data-max-length="21"
						data-digits="2"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="产品总净值" :class="[handleItemDiff('totNav')]">
					<k-field-text
						v-model="formData.totNav"
						:data-allowblank="false"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="总份额" :class="[handleItemDiff('totLot')]">
					<k-field-text
						v-model="formData.totLot"
						:data-allowblank="false"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="近七日年化收益率（%）" :class="[handleItemDiff('rct7dAnlYld')]">
					<k-field-text
						v-model="formData.rct7dAnlYld"
						:data-allowblank="false"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="近一个月年化收益率（%）" :class="[handleItemDiff('rct1mAnlYld')]">
					<k-field-text
						v-model="formData.rct1mAnlYld"
						:data-allowblank="false"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="近三个月年化收益率（%）" :class="[handleItemDiff('rct3mAnlYld')]">
					<k-field-text
						v-model="formData.rct3mAnlYld"
						:data-allowblank="false"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="近半年年化收益率（%）" :class="[handleItemDiff('rct6mAnlYld')]">
					<k-field-text
						v-model="formData.rct6mAnlYld"
						:data-allowblank="false"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="近一年年化收益率（%）" :class="[handleItemDiff('rct1yAnlYld')]">
					<k-field-text
						v-model="formData.rct1yAnlYld"
						:data-allowblank="false"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="近二年年化收益率（%）" :class="[handleItemDiff('rct2yAnlYld')]">
					<k-field-text
						v-model="formData.rct2yAnlYld"
						:data-allowblank="false"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="近三年年化收益率（%）" :class="[handleItemDiff('rct3yAnlYld')]">
					<k-field-text
						v-model="formData.rct3yAnlYld"
						:data-allowblank="false"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="近五年年化收益率（%）" :class="[handleItemDiff('rct5yAnlYld')]">
					<k-field-text
						v-model="formData.rct5yAnlYld"
						:data-allowblank="false"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="成立至今收益率（%）" :class="[handleItemDiff('setUpTilNowYld')]">
					<k-field-text
						v-model="formData.setUpTilNowYld"
						:data-allowblank="false"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="上一开放日至今收益率（%）" :class="[handleItemDiff('lastOpnDayTilNowYld')]">
					<k-field-text
						v-model="formData.lastOpnDayTilNowYld"
						:data-allowblank="false"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="今年年化收益率（%）" :class="[handleItemDiff('nowAnlYld')]">
					<k-field-text
						v-model="formData.nowAnlYld"
						:data-allowblank="false"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="近1日净值增长率（%）" :class="[handleItemDiff('rct1dGrwRat')]">
					<k-field-text
						v-model="formData.rct1dGrwRat"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="近7日净值增长率（%）" :class="[handleItemDiff('rct7dGrwRat')]">
					<k-field-text
						v-model="formData.rct7dGrwRat"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="近一个月净值增长率（%）" :class="[handleItemDiff('rct1mGrwRat')]">
					<k-field-text
						v-model="formData.rct1mGrwRat"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="近三个月净值增长率（%）" :class="[handleItemDiff('rct3mGrwRat')]">
					<k-field-text
						v-model="formData.rct3mGrwRat"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="近半年净值增长率（%）" :class="[handleItemDiff('rct6mGrwRat')]">
					<k-field-text
						v-model="formData.rct6mGrwRat"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="近一年净值增长率（%）" :class="[handleItemDiff('rct1yGrwRat')]">
					<k-field-text
						v-model="formData.rct1yGrwRat"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="近两年净值增长率（%）" :class="[handleItemDiff('rct2yGrwRat')]">
					<k-field-text
						v-model="formData.rct2yGrwRat"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="近三年净值增长率（%）" :class="[handleItemDiff('rct3yGrwRat')]">
					<k-field-text
						v-model="formData.rct3yGrwRat"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="近五年净值增长率（%）" :class="[handleItemDiff('rct5yGrwRat')]">
					<k-field-text
						v-model="formData.rct5yGrwRat"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="成立至今净值增长率（%）" :class="[handleItemDiff('setUpTilNowGrwRat')]">
					<k-field-text
						v-model="formData.setUpTilNowGrwRat"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="上一开放日至今净值增长率（%）" :class="[handleItemDiff('lastOpnDayTilNowGrwRat')]">
					<k-field-text
						v-model="formData.lastOpnDayTilNowGrwRat"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="今年净值增长率（%）" :class="[handleItemDiff('nowGrwRat')]">
					<k-field-text
						v-model="formData.nowGrwRat"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="折算单位净值" :class="[handleItemDiff('cnvUntNav')]">
					<k-field-text
						v-model="formData.cnvUntNav"
						:data-max-length="21"
						data-digits="4"
						data-integer-length="16"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="成立至今最大回撤" :class="[handleItemDiff('setUpTilNowMaxWdw')]">
					<k-field-text
						v-model="formData.setUpTilNowMaxWdw"
						:data-max-length="17"
						data-digits="8"
						data-integer-length="8"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-item label="成立至今波动率（%）" :class="[handleItemDiff('setUpTilNowFlctRat')]">
					<k-field-text
						v-model="formData.setUpTilNowFlctRat"
						:data-max-length="17"
						data-digits="8"
						data-integer-length="8"
						data-validate-type="number"
						data-regx-text="请输入正确小数位数的数值"
					/>
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="T8ProdWorth.updateT8ProdWorth"
						data-from="editT8ProdWorthForm"
						:data-model="formData"
						data-target="t8ProdWorthGrid"
						:handle-before="handleBefore"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE">
						<md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn
					>
				</k-form-footer>
			</k-form>
		</k-popup>

		<!--    详情   -->
		<k-popup ref="detailT8ProdWorthPopup" data-title="详情">
			<k-form ref="editT8ProdWorthForm" :data-col="2" isFormBodyScreen>
				<k-form-item label="产品代码">
					<k-field-select
						v-model="formData.prodCd"
						data-action="T8ProdWorth.findProdWorthCdAndNm"
						data-display-field="prodCd,prodNm"
						data-value-field="prodCd"
						:data-disabled="true"
						:data-allowblank="false"
					    :data-remote="true"
						:data-remote-paging="true"
					/>
				</k-form-item>
				<k-form-item label="产品名称">
					<k-field-text v-model="formData.prodNm" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="估值日期">
					<k-field-date v-model="formData.navDt" :data-disabled="true" data-date-format="yyyy-MM-dd" />
				</k-form-item>
				<k-form-item label="披露日期" v-show="false">
					<k-field-date v-model="formData.isuDt" :data-disabled="true" data-date-format="yyyy-MM-dd" />
				</k-form-item>
				<k-form-item label="单位净值">
					<k-field-text v-model="formData.untNav" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="累计单位净值">
					<k-field-text v-model="formData.acmNav" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="总资产">
					<k-field-text v-model="formData.totAst" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="总负债">
					<k-field-text v-model="formData.totLbl" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="产品总净值">
					<k-field-text v-model="formData.totNav" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="总份额">
					<k-field-text v-model="formData.totLot" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="近七日年化收益率（%）">
					<k-field-text v-model="formData.rct7dAnlYld" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="近一个月年化收益率（%）">
					<k-field-text v-model="formData.rct1mAnlYld" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="近三个月年化收益率（%）">
					<k-field-text v-model="formData.rct3mAnlYld" :data-allowblank="false" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="近半年年化收益率（%）">
					<k-field-text v-model="formData.rct6mAnlYld" :data-allowblank="false" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="近一年年化收益率（%）">
					<k-field-text v-model="formData.rct1yAnlYld" :data-allowblank="false" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="近二年年化收益率（%）">
					<k-field-text v-model="formData.rct2yAnlYld" :data-allowblank="false" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="近三年年化收益率（%）">
					<k-field-text v-model="formData.rct3yAnlYld" :data-allowblank="false" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="近五年年化收益率（%）">
					<k-field-text v-model="formData.rct5yAnlYld" :data-allowblank="false" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="成立至今收益率（%）">
					<k-field-text v-model="formData.setUpTilNowYld" :data-allowblank="false" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="上一开放日至今收益率（%）">
					<k-field-text
						v-model="formData.lastOpnDayTilNowYld"
						:data-allowblank="false"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="今年年化收益率（%）">
					<k-field-text v-model="formData.nowAnlYld" :data-allowblank="false" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="近1日净值增长率（%）">
					<k-field-text v-model="formData.rct1dGrwRat" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="近7日净值增长率（%）">
					<k-field-text v-model="formData.rct7dGrwRat" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="近一个月净值增长率（%）">
					<k-field-text v-model="formData.rct1mGrwRat" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="近三个月净值增长率（%）">
					<k-field-text v-model="formData.rct3mGrwRat" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="近半年净值增长率（%）">
					<k-field-text v-model="formData.rct6mGrwRat" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="近一年净值增长率（%）">
					<k-field-text v-model="formData.rct1yGrwRat" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="近两年净值增长率（%）">
					<k-field-text v-model="formData.rct2yGrwRat" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="近三年净值增长率（%）">
					<k-field-text v-model="formData.rct3yGrwRat" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="近五年净值增长率（%）">
					<k-field-text v-model="formData.rct5yGrwRat" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="成立至今净值增长率（%）">
					<k-field-text v-model="formData.setUpTilNowGrwRat" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="上一开放日至今净值增长率（%）">
					<k-field-text v-model="formData.lastOpnDayTilNowGrwRat" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="今年净值增长率（%）">
					<k-field-text v-model="formData.nowGrwRat" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="折算单位净值">
					<k-field-text v-model="formData.cnvUntNav" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="成立至今最大回撤">
					<k-field-text v-model="formData.setUpTilNowMaxWdw" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="成立至今波动率（%）">
					<k-field-text v-model="formData.setUpTilNowFlctRat" :data-disabled="true" />
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn class="btn-custom-plain" data-functype="CLOSE">
						<md-icon md-src="/static/svg/cancel.svg"></md-icon>关闭</k-btn
					>
				</k-form-footer>
			</k-form>
		</k-popup>
	</div>
</template>

<script>
import moment from "moment";
import Tools from "@/utils/tools";
import httpUtil from "@/frame/httpUtil";
export default {
	name: "T8ProdWorth",
	data() {
		return {
			formData: {
				addDocTypeDict: "",
			},
			formDataCopy: {},
			selectRowData: {
				prodCd: "",
				prodNm: "",
				navDt: "",
				isuDt: "",
				prodCodes: this.prodCodes, //净值披露任务产品代码集
			},
			prodCodes: "", //净值披露任务产品代码集
			searchParam: {},
			currentDate: {}, //定义当前日期回显使用
		};
	},
	methods: {
		handleBefore() {
			if (this.formNoChangeCb()) {
				this.$refs.editT8ProdWorthPopup.close();
				return false
			}
			return true
		},
		changeProdCd() {
			let label = (this.$refs.refProd.options.find(item=>item.value==this.formData.prodCd) || {}).label;
			if (label) {
				this.formData.prodNm = label.split('-')[1] || '';
			}
		},
		selectRow(row, column, event) {
			this.selectRowData = Object.assign({}, row);
			this.formData = Object.assign({}, row);
		},
		changeProdCode(val) {
			if (val == "") {
				this.formData.prodNm = "";
			}
			this.httpUtil
				.comnQuery({
					action: "T8ProdWorth.findT8ProdProdName",
					params: { prodCode: val },
				})
				.then((data) => {
					this.$set(this.formData, "prodNm", data.rows[0].prodNm);
				})
				.catch({});
		},
		beforeDate(param) {
			this.httpUtil
				.comnQuery({
					action: "T8ProdWorth.findDataByProdCode",
					params: param,
				})
				.then((data) => {
					this.$set(this.formData, "rct7dAnlYld", data.rows[0].rct7dAnlYld);
					this.$set(this.formData, "rct1mAnlYld", data.rows[0].rct1mAnlYld);
					this.$set(this.formData, "rct3mAnlYld", data.rows[0].rct3mAnlYld);
					this.$set(this.formData, "rct6mAnlYld", data.rows[0].rct6mAnlYld);
					this.$set(this.formData, "rct1yAnlYld", data.rows[0].rct1yAnlYld);
					this.$set(this.formData, "rct2yAnlYld", data.rows[0].rct2yAnlYld);
					this.$set(this.formData, "rct3yAnlYld", data.rows[0].rct3yAnlYld);
					this.$set(this.formData, "rct5yAnlYld", data.rows[0].rct5yAnlYld);
					this.$set(this.formData, "setUpTilNowYld", data.rows[0].setUpTilNowYld);
					this.$set(this.formData, "lastOpnDayTilNowYld", data.rows[0].lastOpnDayTilNowYld);
					this.$set(this.formData, "nowAnlYld", data.rows[0].nowAnlYld);
					this.$set(this.formData, "rct1dGrwRat", data.rows[0].rct1dGrwRat);
					this.$set(this.formData, "rct7dGrwRat", data.rows[0].rct7dGrwRat);
					this.$set(this.formData, "rct1mGrwRat", data.rows[0].rct1mGrwRat);
					this.$set(this.formData, "rct3mGrwRat", data.rows[0].rct3mGrwRat);
					this.$set(this.formData, "rct6mGrwRat", data.rows[0].rct6mGrwRat);
					this.$set(this.formData, "rct1yGrwRat", data.rows[0].rct1yGrwRat);
					this.$set(this.formData, "rct2yGrwRat", data.rows[0].rct2yGrwRat);
					this.$set(this.formData, "rct3yGrwRat", data.rows[0].rct3yGrwRat);
					this.$set(this.formData, "rct5yGrwRat", data.rows[0].rct5yGrwRat);
					this.$set(this.formData, "setUpTilNowGrwRat", data.rows[0].setUpTilNowGrwRat);
					this.$set(this.formData, "lastOpnDayTilNowGrwRat", data.rows[0].lastOpnDayTilNowGrwRat);
					this.$set(this.formData, "nowGrwRat", data.rows[0].nowGrwRat);
					this.$set(this.formData, "cnvUntNav", data.rows[0].cnvUntNav);
					this.$set(this.formData, "setUpTilNowMaxWdw", data.rows[0].setUpTilNowMaxWdw);
					this.$set(this.formData, "setUpTilNowFlctRat", data.rows[0].setUpTilNowFlctRat);
					this.formDataCopy = Object.assign({}, this.formData);
				})
				.catch({});
		},
	},
	created() {
		const sessionStorage = window.sessionStorage;
		this.prodCodes = sessionStorage.getItem("prodCodes");
		this.prodBaseDate = this.$route.query.prodBaseDate;
		if (this.prodCodes) {
			// this.currentDate = this.prodBaseDate;
			this.$set(this.searchParam, "isuDt", this.prodBaseDate.toString());
			this.$nextTick(() => {
				this.$refs.t8ProdWorthGrid.load({ isuDt: this.searchParam.isuDt, prodCodes: this.prodCodes });
			});
			this.isRouter = false;
		} else {
			/*let now = new Date();
        let year = now.getFullYear(); //获取年
        let month = now.getMonth(); //获取月
        let date = now.getDate(); //得到日期
        month = month + 1;
        month = month.toString().padStart(2, "0");
        date = date.toString().padStart(2, "0");
        let  defaultDate = `${year}${month}${date}`;
        this.currentDate = defaultDate;*/
			this.currentDate = "";
			this.httpUtil.sysDate().then((res) => {
				if (res) {
					this.$set(this.searchParam, "navDt", res.toString());
					httpUtil.sysparam("10006", "0").then((data) => {
						if (data === 1) {
							this.$nextTick(() => {
								this.$refs.t8ProdWorthGrid.load({ navDt: "sysDate" });
							});
						} else {
							this.$nextTick(() => {
								this.$refs.t8ProdWorthGrid.load({ navDt: res.toString() });
							});
						}
					});
				}
			});
		}
	},
	mounted() {
		console.log("mounted-m1");
	},
	activated() {
		console.log("activated-m1");
	},
	// 净值披露任务路由跳转动态查询
	/*   activated() {
      this.prodCodes = this.$route.query.prodCodes;
      this.prodBaseDate = this.$route.query.prodBaseDate;
      this.routerAdd = this.$route.query.routerAdd;
      if (this.prodCodes) {
        // this.$refs.t8ProdWorthGrid.load({prodCodes: this.prodCodes,navDt: this.prodBaseDate});
        this.$refs.t8ProdWorthGrid.load({isuDt: this.prodBaseDate,navDt:"sysDate"});
        this.prodCodes = '';
        this.prodBaseDate = '';
      }else if (this.routerAdd){
        this.$refs.addT8ProdWorthPopup.popup();
      }
    },*/
};
</script>
