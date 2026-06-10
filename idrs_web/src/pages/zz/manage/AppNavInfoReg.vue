<template>
	<div class="py-page">
		<div>
			<k-form-search-customize
				ref="searchFormRef"
				data-model-name="AppNavInfoReg"
				v-model="queryParam"
				data-target="appNavInfoRegGrid"
				:handleConfirm="handleConfirm"
			>
				<k-form-item label="报送日期">
				  <k-field-date v-model="queryParamDateRange" :data-allowblank="false" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd" />
				</k-form-item>
				<k-form-item label="净值日期">
				  <k-field-date v-model="queryParamNavDateRange" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd" />
				</k-form-item>
				<!-- <k-form-item label="数据日期">
					<k-field-date v-model="searchParam.dataDate" data-type="date" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd" />
				</k-form-item> -->
				<k-form-item label="产品登记编码">
					<k-field-text v-model="searchParam.prodRegEnc" />
				</k-form-item>
				<k-form-item label="产品子份额代码">
					<k-field-text v-model="searchParam.sonShareCode" />
				</k-form-item>
				<k-form-item label="净值登记类型">
					<k-field-select v-model="searchParam.navRegType" data-dict="navRegType" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="报送状态">
					<k-field-select v-model="searchParam.registerStatus" data-dict="subm_report_status" :dataDictExcludeFilter="['9']" />
				</k-form-item>
				<k-form-item label="母产品代码">
					<k-field-text v-model="searchParam.motherFundCode" />
				</k-form-item>
				<k-form-item label="产品类型">
					<k-field-select v-model="searchParam.openType" data-dict="openType" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="0份额标识">
					<k-field-select v-model="searchParam.volZeroFlag" data-dict="vol0Flag" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="业绩基准类型">
					<k-field-select v-model="searchParam.prfrBnchTyp" data-dict="prfrBnchTyp" data-dict-type="1" />
				</k-form-item>
			</k-form-search-customize>
		</div>
		<div class="py-page-container">
			<div class="table-top-btns">
				<div class="left">
				  <k-btn slot="button"  data-functype="POPUP" class="btn-custom-plain" data-target="uploadAppNavInfoRegPopup">
            <md-icon>cloud_upload</md-icon>导入
          </k-btn>
					<k-btn
						slot="button"
						ref="exportRef"
						class="btn-custom-plain"
						data-functype="EXPORT"
						data-target="appNavInfoRegGrid"
						data-export-dict="true"
						data-export-form="searchFormRef"
						:data-export-name="'净值信息登记'"
						:handleBefore="handleBefore"
						@downSuccess="downSuccess"
						:data-handler="dataHandler"
					>
						<md-icon>cloud_download</md-icon>
						导出
					</k-btn>
					<k-btn slot="button" class="btn-custom-plain" :handleBefore="handleBefore" :data-handler="handleConfirmExport1">
						<md-icon>cloud_download</md-icon>
						确认并导出
					</k-btn>
					<k-btn slot="button" ref="insertNavRef" class="btn-custom-plain" data-functype="POPUP" data-target="rangeNavPopup" :load-disabled="false"
						data-action="DwsAssetA1413DepStruc.rangeInsertNavInfo" loading-tip="正在处理中，请稍后重试！">
						<md-icon>cloud_download</md-icon>
						生成区间段净值信息
					</k-btn>
          <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="auditPopup">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>复核
          </k-btn>
				</div>
			</div>
			<k-grid
				ref="appNavInfoRegGrid"
				@data-row-select="selectRow"
				data-action="AppNavInfoReg.findAppNavInfoRegs"
				data-fixed="right"
				data-operate-width="120px"
				:data-autoload="false"
				data-dict-type="1"
			>
				<k-grid-column data-header="报送日期" data-name="reportDate" data-export="false"></k-grid-column>
				<k-grid-column data-header="*发行机构代码" data-name="bankCode" data-width="120"></k-grid-column>
				<k-grid-column data-header="*产品登记编码" data-name="prodRegEnc" data-width="130"></k-grid-column>
				<k-grid-column data-header="*净值登记类型" data-name="navRegType" data-dict="navRegType" data-width="120" data-dict-type="1"></k-grid-column>
				<k-grid-column data-header="产品子份额代码" data-name="sonShareCode" data-width="120"></k-grid-column>
				<k-grid-column data-header="*币种" data-name="cny"></k-grid-column>
				<k-grid-column data-header="*净值" data-name="nav" data-width="120"></k-grid-column>
				<k-grid-column data-header="*折算人民币净值" data-name="rmbNav" data-width="140"></k-grid-column>
				<k-grid-column data-header="达基净值" data-name="djNav" data-width="120"></k-grid-column>
				<k-grid-column data-header="*累计净值" data-name="totalNav" data-width="120"></k-grid-column>
				<k-grid-column data-header="*折算人民币累计净值" data-name="rmbTotalNav" data-width="140"></k-grid-column>
				<k-grid-column data-header="*复权净值" data-name="fqNav" data-width="120"></k-grid-column>
				<k-grid-column data-header="*折算人民币复权净值" data-name="rmbFqNav" data-width="140"></k-grid-column>
				<k-grid-column data-header="*估值依据" data-name="navCalType" data-dict="navCalType" data-dict-type="1"></k-grid-column>
				<k-grid-column data-header="*份额" data-name="share" data-width="120"></k-grid-column>
				<k-grid-column data-header="*净值日期" data-name="navDate"></k-grid-column>
				<k-grid-column data-header="披露日期" data-name="disclosureDate"></k-grid-column>
				<k-grid-column data-header="*存续余额（元）" data-name="remainBal" data-width="120"></k-grid-column>
				<k-grid-column data-header="*折算人民币存续余额（元）" data-name="rmbRemainBal" data-width="170"></k-grid-column>
				<k-grid-column data-header="备注" data-name="details"></k-grid-column>
				<k-grid-column data-header="母产品代码" data-name="motherFundCode" data-width="120" :data-export="isExportField"></k-grid-column>
				<k-grid-column data-header="产品类型" data-name="openType" data-dict="openType" data-width="120" :data-export="isExportField"></k-grid-column>
				<k-grid-column data-header="0份额标识" data-name="volZeroFlag" data-dict="vol0Flag" data-width="120" :data-export="isExportField"></k-grid-column>
				<k-grid-column data-header="子产品成立日" data-name="establishDate" data-width="120" :data-export="isExportField"></k-grid-column>
				<k-grid-column data-header="子产品到期日" data-name="endDate" data-width="120" :data-export="isExportField"></k-grid-column>
				<k-grid-column data-header="上一基准日" data-name="jzDate" data-width="120" :data-export="isExportField"></k-grid-column>
				<k-grid-column data-header="业绩比较基准" data-name="prfrBnch"  data-width="120" :data-export="isExportField"></k-grid-column>
				<k-grid-column data-header="业绩基准类型" data-name="prfrBnchTyp" data-width="120" data-dict="prfrBnchTyp" :data-export="isExportField"></k-grid-column>
				<k-grid-column data-header="业绩基准上限" data-name="intrRtUpp" data-width="120" :data-export="isExportField"></k-grid-column>
				<k-grid-column data-header="业绩基准下限" data-name="intrRtFlr" data-width="120" :data-export="isExportField"></k-grid-column>
				<k-grid-column data-header="业绩基准说明" data-name="prfrBnchTypDscr" data-width="180" :data-export="isExportField"></k-grid-column>

				<k-grid-column data-header="万份收益" data-name="enDwjjsy" data-width="180" :data-export="isExportField"></k-grid-column>
				<k-grid-column data-header="上一基准日净值" data-name="lstJzNav" data-width="180" :data-export="isExportField"></k-grid-column>
				<k-grid-column data-header="上一工作日单位净值" data-name="lstWkdNav" data-width="180" :data-export="isExportField"></k-grid-column>
				<k-grid-column data-header="是否迁移产品" data-name="isProdTransfer" data-dict="1yes2no" data-width="180" :data-export="isExportField"></k-grid-column>
				<k-grid-column data-header="迁移产品迁移净值" data-name="transferNav" data-width="180" :data-export="isExportField"></k-grid-column>
				<k-grid-column data-header="净值日指数" data-name="navDtIndex" data-width="180" :data-export="isExportField"></k-grid-column>
				<k-grid-column data-header="净值日上一工作日指数" data-name="lstWkdIndex" data-width="180" :data-export="isExportField"></k-grid-column>
				<k-grid-column
					data-header="报送状态"
					data-name="registerStatus"
					data-dict="subm_report_status"
					data-export="false"
					data-width="100"
					data-dict-type="0"
				></k-grid-column>
				<!-- <k-grid-column data-header="登记日期" data-name="registerDate" data-export="false"></k-grid-column>
				<k-grid-column data-header="登记流水号" data-name="registerSerno" data-export="false" data-width="160"></k-grid-column> -->
				<k-grid-column data-header="新增日期" data-name="crtDt" data-export="false"></k-grid-column>
				<template slot="operate" slot-scope="scope">
					<k-btn class="btn-custom-text"  v-show="scope.row.row.registerStatus != '5'" data-descript="修改净值信息登记" data-functype="POPUP" data-size="mini" data-target="editAppNavInfoRegPopup">
						修改
					</k-btn>
					<k-btn
						class="btn-custom-text"
						data-functype="SUBMIT"
						data-action="AppNavInfoReg.deleteAppNavInfoReg"
						data-size="mini"
						data-type="danger"
						data-target="appNavInfoRegGrid"
						:data-confirm="true"
					   v-show="scope.row.row.registerStatus != '5'"	data-descript="删除净值信息登记"
					>
						删除
					</k-btn>
				</template>
			</k-grid>
		</div>

		<!--    修改净值信息登记弹出框   -->
		<k-popup ref="editAppNavInfoRegPopup" data-title="编辑" @data-opened="editOpened()">
			<k-form ref="editAppNavInfoRegForm" :data-col="2" data-label-width="190px">
				<k-form-item label="*发行机构代码">
					<k-field-text v-model="formData.bankCode" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="*产品登记编码">
					<k-field-text v-model="formData.prodRegEnc" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="*净值登记类型" :class="[handleItemDiff('navRegType')]">
					<k-field-select v-model="formData.navRegType" data-dict="navRegType" :data-allowblank="false"  data-dict-type="1" />
				</k-form-item>
				<k-form-item label="产品子份额代码">
					<k-field-text v-model="formData.sonShareCode" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="*币种" :class="[handleItemDiff('cny')]">
					<k-field-select v-model="formData.cny" data-dict="subm_cur_type" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="*净值" :class="[handleItemDiff('nav')]">
					<k-field-text v-model="formData.nav" :data-allowblank="false"  data-validate-type="money" data-integer-length="5" data-digits="8"   data-regx-text="请输入大于等于0的数值" data-min-value="0" />
				</k-form-item>
				<k-form-item label="*折算人民币净值" :class="[handleItemDiff('rmbNav')]">
					<k-field-text v-model="formData.rmbNav" :data-allowblank="false"  data-validate-type="money" data-integer-length="5" data-digits="8"  data-regx-text="请输入大于等于0的数值" data-min-value="0" />
				</k-form-item>
				<k-form-item label="*达基净值" :class="[handleItemDiff('djNav')]">
					<k-field-text v-model="formData.djNav" data-validate-type="money" data-integer-length="5" data-digits="8"/>
				</k-form-item>
				<k-form-item label="*累计净值" :class="[handleItemDiff('totalNav')]">
					<k-field-text v-model="formData.totalNav" :data-allowblank="false"  data-validate-type="money" data-integer-length="5" data-digits="8"  data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
				</k-form-item>
				<k-form-item label="*折算人民币累计净值" :class="[handleItemDiff('rmbTotalNav')]">
					<k-field-text v-model="formData.rmbTotalNav" :data-allowblank="false"  data-validate-type="money" data-integer-length="5" data-digits="8"  data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
				</k-form-item>
				<k-form-item label="*复权净值" :class="[handleItemDiff('fqNav')]">
					<k-field-text v-model="formData.fqNav" :data-allowblank="false"  data-validate-type="money" data-integer-length="5" data-digits="8"  data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
				</k-form-item>
				<k-form-item label="*折算人民币复权净值" :class="[handleItemDiff('rmbFqNav')]">
					<k-field-text v-model="formData.rmbFqNav" :data-allowblank="false" data-validate-type="money" data-integer-length="5" data-digits="8"  data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
				</k-form-item>
				<k-form-item label="*估值依据" :class="[handleItemDiff('navCalType')]">
					<k-field-select v-model="formData.navCalType" data-dict="navCalType" data-dict-type="1" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="*份额" :class="[handleItemDiff('share')]">
					<k-field-text v-model="formData.share" :data-allowblank="false" data-validate-type="money" data-integer-length="15" data-digits="5"  data-regx-text="请输入大于等于0的数值" data-min-value="0" />
				</k-form-item>
				<k-form-item label="*净值日期" :class="[handleItemDiff('navDate')]">
					<k-field-text v-model="formData.navDate" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="披露日期" :class="[handleItemDiff('disclosureDate')]">
					<k-field-text v-model="formData.disclosureDate" />
				</k-form-item>
				<k-form-item label="*存续余额（元）" :class="[handleItemDiff('remainBal')]">
					<k-field-text v-model="formData.remainBal" :data-allowblank="false" data-validate-type="money" data-integer-length="15" data-digits="5"  data-regx-text="请输入大于等于0的数值" data-min-value="0" />
				</k-form-item>
				<k-form-item label="*折算人民币存续余额（元）" :class="[handleItemDiff('rmbRemainBal')]">
					<k-field-text v-model="formData.rmbRemainBal" :data-allowblank="false"   data-validate-type="money" data-integer-length="15" data-digits="5"  data-regx-text="请输入大于等于0的数值" data-min-value="0"/>
				</k-form-item>
				<k-form-item label="备注" :class="[handleItemDiff('details')]">
					<k-field-text v-model="formData.details" :data-max-length="256" />
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						ref="sumbitedit" :data-handler="sumbit_edit"
						data-from="editAppNavInfoRegForm"
						:data-model="formData"
						data-target="appNavInfoRegGrid"
						:handle-before="handleBeforeUpdate"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>

		<k-popup ref="uploadAppNavInfoRegPopup" title="报送数据导入" @data-opened="uploadOpened()">
            <k-form ref="addForm" data-ui="element">
              <k-form-item label="报送日期">
                <k-field-date v-model="uploadBeginDate" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd" :data-allowblank="false"/>
              </k-form-item>
              <k-form-item label="报送数据导入" data-ui="element" data-input-width="500px">
                <k-field-excel-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit='1'
                    data-accept=".xlsx,.xls"
                    :data-error="onSubmitError" :data-success="onSubmitSuccess"
                    :data-auto-upload="false"
                    data-upload-url="upload/server/RptApp/reportManage/appNavInfoRegImport.json">
                </k-field-excel-upload>
              </k-form-item>
              <k-form-footer data-align="center">
                <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="appNavInfoRegGrid" ref="submitBtn"
                      :data-auto-upload="false" data-from="addForm" :data-handler="submitUploadParam">确定
                </k-btn>
                <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
              </k-form-footer>
            </k-form>
        </k-popup>

		<k-popup ref="rangeNavPopup" data-title="生成区间段净值信息">
			<k-form ref="rangeNavForm" :data-col="1" data-label-width="100px" style="margin-top:20px">
				<k-form-item label="日期选择">
					<k-field-date v-model="formData.dateRange" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd" :data-allowblank="false" />
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						:data-handler="handleRangeNav"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>
    <k-popup ref="auditInfoPopup">
      <k-form ref="updateAuditStatusForm" :data-col="2" isFormBodyScreen>
         <k-form-item label="报表类型">
           <k-field-text v-model="infoPop.tableName" :data-allowblank="false" data-disabled="true"/>
         </k-form-item>
         <k-form-item label="报送日期" data-label-width="100px">
           <k-field-date v-model="queryParamDateRange" data-type="daterange" data-date-format="yyyy-MM-dd"
                         data-value-format="yyyyMMdd" :data-allowblank="false"/>
         </k-form-item>
         <k-form-item label="复核状态">
           <k-field-select v-model="infoPop.auditStatus" data-dict="xp_disclosure_check_status" data-default-value="1" data-disabled="true"/>
         </k-form-item>
         <k-form-footer slot="footer" data-align="center">
           <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-from="updateAuditStatusForm" data-target="prodIssuanceRegistInfoGrid"
                  @click="audit" :data-model="infoPop"><md-icon md-src="/static/svg/confirm.svg"></md-icon>确定</k-btn>
           <k-btn class="btn-custom-plain" data-functype="CLOSE"><md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
         </k-form-footer>
      </k-form>
    </k-popup>
	</div>
</template>

<script>
import ProdMixin from "@/pages/zz/manage/mixins/prodMixin.js";
import moment from "moment";
import Tools from '@/utils/tools.js';
export default {
	name: "AppNavInfoReg",
	mixins: [ProdMixin],
	data() {
		return {
			formData: {
				dateRange: []
			},
			formDataCopy: {},
			selectRowData: {},
			searchParam: {},
			queryParamDateRange: [],
			queryParamNavDateRange: [],
			abnormalAction: "AppNavInfoReg.getAbnormalData",
			updateStatusAction: "AppNavInfoReg.updateAppNavInfoRegStatus",
			isExportField: "true",
			uploadBeginDate: '',
      infoPop: {},
      auditInfoPopupData: {
        tableId: 'app_nav_info_reg',
        tableName: '净值信息登记'
      }
		};
	},
	computed: {
	  editOpened(){
	    this.formData.oldData=Tools.json2str(this.formData);
    },
		queryParam() {
			return {
				reportBeginDate: this.queryParamDateRange ? this.queryParamDateRange[0] : null,
        reportEndDate: this.queryParamDateRange ? this.queryParamDateRange[1] : null,
				navBeginDate: this.queryParamNavDateRange ? this.queryParamNavDateRange[0] : null,
        navEndDate: this.queryParamNavDateRange ? this.queryParamNavDateRange[1] : null,
				dataDate: this.searchParam.dataDate,
				prodRegEnc: this.searchParam.prodRegEnc,
				registerStatus: this.searchParam.registerStatus,
				sonShareCode: this.searchParam.sonShareCode,
				navRegType: this.searchParam.navRegType,
				motherFundCode: this.searchParam.motherFundCode,
				openType: this.searchParam.openType,
				volZeroFlag: this.searchParam.volZeroFlag,
				prfrBnchTyp: this.searchParam.prfrBnchTyp

			};
		}
	},
	methods: {
		handleBeforeUpdate() {
			if (this.formNoChangeCb()) {
				this.$refs.editAppNavInfoRegPopup.close();
				return false;
			}
			return true;
		},
    sumbit_edit(){
      this.$refs.sumbitedit.setIconStyle(0,[]);
      if(this.$refs.editAppNavInfoRegForm.validate()){
           this.httpUtil.query({
                   url: 'server/json/RptApp/audit/checkAppNavInfoReg.json',
                   params:  this.formData
                            }).then(res => {
                              if(res.success) {
                               this.httpUtil.comnUpdate({
                                        action: 'AppNavInfoReg.updateAppNavInfoReg',
                                        params:  this.formData
                                         }).then(res => {
                                          if(res.success) {
                                          this.$refs.editAppNavInfoRegPopup.close();
                                      }else{
                                        this.$refs.sumbitedit.setIconStyle(1,[]);
                                      }
                            })
                      }else{
                          this.$refs.sumbitedit.setIconStyle(1,[]);
                      }
           });
      }else{
        this.$refs.sumbitedit.setIconStyle(1,[]);
      }
    },
    audit() {
      let tableName = this.infoPop.tableName;
      let tableId = this.infoPop.tableId;
      let auditStatus = this.infoPop.auditStatus;
      let startDate = this.queryParamDateRange ? this.queryParamDateRange[0] : null;
      let endDate = this.queryParamDateRange ? this.queryParamDateRange[1] : null;
      this.httpUtil.ajax({
         url: 'server/json/RptApp/audit/indexstatus.json',
         params: {
           tableId: tableId,
           startDate: startDate,
           endDate: endDate,
           auditStatus: auditStatus
         }
       }).then(res => {
         if(res.success) {
           if(res.returnmsg=='存在指标校验未通过数据'){
               this.$confirm("日期区间存在未校验或校验未通过的数据,确认复核吗？", "操作提示", {
               confirmButtonText: "确定",
               cancelButtonText: "取消",
               type: "warning"
               }).then(() => {
                    this.httpUtil.ajax({
                    url: 'server/json/RptApp/audit/status.json',
                    params: {
                      tableId: tableId,
                      startDate: startDate,
                      endDate: endDate,
                      auditStatus: auditStatus
                    }
                  }).then(res => {
                    if(res.success) {
                     Tools.alert(res.returnmsg, "success");
                     this.$refs.appNavInfoRegGrid.load(this.queryParam);
                     this.$refs.auditInfoPopup.close();
                    }
                  })
              }).catch(() => {});
           }else{
              this.httpUtil.ajax({
                url: 'server/json/RptApp/audit/status.json',
                params: {
                  tableId: tableId,
                  startDate: startDate,
                  endDate: endDate,
                  auditStatus: auditStatus
                }
              }).then(res => {
                if(res.success) {
                 Tools.alert(res.returnmsg, "success");
                 this.$refs.appNavInfoRegGrid.load(this.queryParam);
                 this.$refs.auditInfoPopup.close();
                }
              })
           }
         }
       })
    },
    // 复核弹窗
    auditPopup() {
      this.infoPop = this.auditInfoPopupData;
      this.$refs.auditInfoPopup.popup();
    },
    uploadOpened() {
      this.uploadBeginDate = ''
    },
    submitUploadParam() {
      //文件上传校验
      let validate = this.$refs.addForm.validate();
      if (validate) {
        let formData = {reportDate: this.uploadBeginDate};
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
      this.$refs.uploadAppNavInfoRegPopup.close();
      this.$refs.appNavInfoRegGrid.load(this.queryParam);
    },
    onSubmitError() {
      this.$refs.uploadRef.doReset();
      this.$refs.submitBtn.setIconStyle(1, [])
    },
		selectRow(row, column, event) {
			this.selectRowData = Object.assign({}, row);
			this.formData = Object.assign({}, row);
			this.formDataCopy = Object.assign({}, row);
		},
		handleConfirm() {
			if (this.queryParamNavDateRange == null && this.queryParamDateRange == null && !this.searchParam.sonShareCode && !this.searchParam.motherFundCode) {
				this.$message.error("“报送日期”、“净值日期”和“产品代码”不能同时为空!");
				return false;
			}
			this.isExportField = "false";
			this.$nextTick(()=>{
				this.$refs.appNavInfoRegGrid.initMtd();
			})
			return true;
		},
		setConfirmExportParam() {
			this.comfirmExportParam = {
				...this.queryParam,
				filter0Vol: "01"
			};
		},
		handleBefore() {
			return this.handleConfirm();
		},
		handleRangeNav() {
			if (this.$refs.rangeNavForm.validate()) {
				const startDate = this.formData.dateRange[0];
				const endDate = this.formData.dateRange[1];
				this.$refs.insertNavRef.setIconStyle(0);

				this.httpUtil.comnUpdate({
					action: "DwsAssetA1413DepStruc.rangeInsertNavInfo",
					async:true,
					params: {dateRange: startDate+","+endDate},
					successAlert: true
				}).then(data => {
					this.$refs.insertNavRef.setIconStyle(1);
				}).catch((err)=>{
					console.log(err, 'err');
					this.$refs.insertNavRef.setIconStyle(1);
				})
				setTimeout(()=>{
					this.$refs.rangeNavPopup.close();
					this.formData.dateRange = [];
				}, 300)
			}
		},
		dataHandler() {
			if (this.queryParamNavDateRange == null && this.queryParamDateRange == null && !this.searchParam.sonShareCode && !this.searchParam.motherFundCode) {
				this.$message.error("“报送日期”、“净值日期”和“产品代码”不能同时为空!");
				return false;
			}
			this.isExportField = "true";
			this.$nextTick(()=>{
				this.$refs.appNavInfoRegGrid.initMtd();
			})
			setTimeout(()=>{
				this.$refs.exportRef.handleExport(this.queryParam);
			}, 500)
			return false
		},
		handleConfirmExport1() {
			this.setConfirmExportParam();
			setTimeout(()=>{
				this.handleConfirmExport(this.comfirmExportParam, "custom");
			}, 500)
			return false;
		}
	},
	created() {
    let now = new Date();
    let year = now.getFullYear(); //获取年
    let month = now.getMonth(); //获取月
    let date = now.getDate(); //得到日期
    month = month + 1;
    month = month.toString().padStart(2, "0");
    date = date.toString().padStart(2, "0");
    let  defaultDate = `${year}${month}${date}`;
    this.queryParamDateRange[0] = defaultDate;
    this.queryParamDateRange[1] = defaultDate;
    this.$set(this.queryParam, "queryParamDateRange", defaultDate);
  },
};
</script>
