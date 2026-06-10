<template>
  <div class="py-page">
      <div>
           <k-form-search-customize data-model-name="CustTransInfoh" data-target="CustTransInfohGrid" v-model = "searchParam" :handleConfirm="handleConfirm">
             <k-form-item label="数据日期">
               <k-field-date v-model="searchParam.reportDate" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
             </k-form-item>
             <k-form-item label="客户统一编号">
               <k-field-text v-model="searchParam.hostCustNo"/>
             </k-form-item>
             <k-form-item label="识别标识">
               <k-field-text v-model="searchParam.custNo"/>
             </k-form-item>
             <k-form-item label="业务种类">
               <k-field-select v-model="searchParam.busiCode"  data-dict="subm_tr_busi_code"/>
             </k-form-item>
             <k-form-item label="产品登记编码">
               <k-field-text v-model="searchParam.prodCode"/>
             </k-form-item>
             <k-form-item label="销售机构代码">
               <k-field-text v-model="searchParam.agentBankCode"/>
             </k-form-item>
             <k-form-item label="销售机构名称">
               <k-field-text v-model="searchParam.agentBankName"/>
             </k-form-item>
           </k-form-search-customize>
      </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn slot="button" ref="uploadModifyBtnRef" :load-disabled="false" data-functype="POPUP" class="btn-custom-plain"
                data-target="uploadTrCustTransInfoModifyPopup">
            <md-icon>cloud_upload</md-icon>
            导入数据变更
          </k-btn>
          <k-btn slot="button" ref="exportBtnRef" class="btn-custom-plain"  data-functype="EXPORT" data-target="CustTransInfohGrid"
                 data-action="CustTransInfoh.historyDownload" :data-export-name="'投资者明细信息登记历史数据管理'"
                 :report-date="searchParam.reportDate" :cust-no="searchParam.custNo" :data-handler="handleConfirmExport">
            <md-icon>cloud_download</md-icon>
            导出
          </k-btn>
        </div>
      </div>

      <k-grid ref="CustTransInfohGrid" @data-row-select="selectRow"  data-fixed="right" data-operate-width="250px"
              data-action="CustTransInfoh.findCustTransInfohs" :data-autoload="false" :handleDataFun="handleData" data-operate-column="false">
        <k-grid-column data-align="left" data-header="ID" data-name="id" data-export="false" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="数据日期" data-name="reportDate" data-export="false" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="**登记机构代码" data-name="bankCode" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="**销售合同号" data-name="contractNo" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="**核心交易流水号" data-name="transSerno" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="**理财账号" data-name="fncTransAcctNo" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="**客户统一编号" data-name="hostCustNo" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="**识别标识" data-name="custNo" data-width="120"></k-grid-column>
        <!-- <k-grid-column data-align="left" data-header="**客户姓名" data-name="custName" data-width="120"></k-grid-column> -->
        <k-grid-column data-align="left" data-header="**交易序列号" data-name="dealNo" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="**关联活期存款账号" data-name="acctNo" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="**关联活期存款账号开户行代码" data-name="acctBankNo" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="**关联活期存款账号开户行名称" data-name="acctBankName" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="**关联账号开户所在地" data-name="acctLocCode" data-dict="subm_prod_sale_area" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="**是否代销" data-name="isAgent" data-dict="tr_is_belong" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="**销售机构代码" data-name="agentBankCode" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="**销售机构名称" data-name="agentBankName" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="*销售机构所属监管机构" data-name="agentReguCode" data-dict="subm_tr_agent_regu_code" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="**产品登记编码" data-name="prodCode" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="**子份额代码" data-name="sonShareCode" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="**业务种类" data-name="busiCode" data-dict="subm_tr_busi_code" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="**业务发生地所属监管" data-name="busiReguCode" data-dict="subm_tr_agent_regu_code" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="**业务确认日期" data-name="ackDate"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="**业务确认时间" data-name="ackTime"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="**币种" data-name="cur" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="特殊渠道" data-name="speChannelFlag" data-dict="subm_tr_spe_channel_flag_z" data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="**金额" data-name="ackAmt" data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="**折算人民币金额" data-name="convertRmb" data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="*确认净值" data-name="nav" data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="**份额" data-name="ackVol" data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="**费用" data-name="feeAmt" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="**渠道" data-name="channelFlag" data-dict="subm_tr_channel_flag_z" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="**交易柜员号" data-name="inputuser" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="备注" data-name="remark" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="登记流水号" data-name="registerSerno" data-export="false" data-width="250"></k-grid-column>
        <k-grid-column data-align="left" data-header="登记日期" data-name="registerDate" data-export="false" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="报送状态" data-name="registerStatus" data-dict="subm_report_status" data-export="false" data-width="120"></k-grid-column>
        <!-- 以下为处理脱敏字段处理 -->
        <!-- <k-grid-column data-align="left" data-header="客户姓名" data-name="custNameDisplay" data-hidden="true" data-export="false"></k-grid-column> -->
        <k-grid-column data-align="left" data-header="关联活期存款账号" data-name="acctNoDisplay" data-hidden="true" data-export="false"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改投资者明细信息登记历史表" data-functype="POPUP" data-size="mini"
                 data-target="editTrCustTransInfoHPopup">
            修改
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    修改投资者明细信息登记表弹出框   -->
    <k-popup ref="editTrCustTransInfoHPopup" data-title="修改">
      <k-form ref="editTrCustTransInfoHForm" :data-col="2" isFormBodyScreen>
        <k-form-item label="数据日期">
          <k-field-date v-model="formData.reportDate" :data-allowblank="false" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="登记机构代码">
          <k-field-text v-model="formData.bankCode" :data-allowblank="false"  :data-max-length="6"/>
        </k-form-item>
        <k-form-item label="核心交易流水号">
          <k-field-text v-model="formData.transSerno" :data-allowblank="false" :data-max-length="32"/>
        </k-form-item>
        <k-form-item label="销售合同号">
          <k-field-text v-model="formData.contractNo" :data-allowblank="false" :data-max-length="32"/>
        </k-form-item>
        <k-form-item label="理财账号">
          <k-field-text v-model="formData.fncTransAcctNo" :data-allowblank="false" :data-max-length="32"/>
        </k-form-item>
        <k-form-item label="客户统一编号">
          <k-field-text v-model="formData.hostCustNo" :data-allowblank="false" :data-max-length="32"/>
        </k-form-item>
        <k-form-item label="识别标识">
          <k-field-text v-model="formData.custNo" :data-allowblank="false" :data-max-length="32"/>
        </k-form-item>
        <!-- <k-form-item label="客户姓名">
          <k-field-text v-model="formData.custNameDisplay" :data-allowblank="false" :data-max-length="32"/>
        </k-form-item> -->
        <k-form-item label="交易序列号">
          <k-field-text v-model="formData.dealNo" :data-allowblank="false" :data-max-length="30"/>
        </k-form-item>
        <k-form-item label="关联活期存款账号">
          <k-field-text v-model="formData.acctNoDisplay" :data-allowblank="false" :data-max-length="60"/>
        </k-form-item>
        <k-form-item label="关联活期存款账号开户行代码">
          <k-field-text v-model="formData.acctBankNo" :data-allowblank="false" :data-max-length="30"/>
        </k-form-item>
        <k-form-item label="关联活期存款账号开户行名称">
          <k-field-text v-model="formData.acctBankName" :data-allowblank="false" :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="关联账号开户所在地">
          <k-field-select v-model="formData.acctLocCode" :data-allowblank="false" data-dict="subm_prod_sale_area"/>
        </k-form-item>
        <k-form-item label="是否代销">
          <k-field-select v-model="formData.isAgent" data-dict="tr_is_belong" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="销售机构代码">
          <k-field-text v-model="formData.agentBankCode" :data-max-length="32"/>
        </k-form-item>
        <k-form-item label="销售机构名称">
          <k-field-text v-model="formData.agentBankName" :data-max-length="32"/>
        </k-form-item>
        <k-form-item label="销售机构所属监管机构">
          <k-field-select v-model="formData.agentReguCode" data-dict="subm_tr_agent_regu_code"/>
        </k-form-item>
        <k-form-item label="产品登记编码">
          <k-field-text v-model="formData.prodCode" :data-allowblank="false" :data-max-length="32"/>
        </k-form-item>
        <k-form-item label="子份额代码">
	        <k-field-text v-model="formData.sonShareCode" :data-allowblank="false" :data-max-length="32"/>
	     	</k-form-item>
        <k-form-item label="业务种类">
          <k-field-select v-model="formData.busiCode" data-dict="subm_tr_busi_code" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="业务发生地所属监管">
          <k-field-select v-model="formData.busiReguCode" data-dict="subm_tr_agent_regu_code" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="业务确认日期">
          <k-field-date v-model="formData.ackDate" :data-allowblank="false"   data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="业务确认时间">
          <k-field-time v-model="formData.ackTime" :data-allowblank="false" :data-max-length="32"/>
        </k-form-item>
        <k-form-item label="币种">
          <k-field-select v-model="formData.cur" data-dict="tr_cur" :data-allowblank="false" :data-max-length="32"/>
        </k-form-item>
        <k-form-item label="特殊渠道">
	        <k-field-select v-model="formData.speChannelFlag" data-dict="subm_tr_spe_channel_flag_z" :data-allowblank="false"/>
	     	</k-form-item>
        <k-form-item label="金额(元)">
          <k-field-text v-model="formData.ackAmt" :data-allowblank="false" data-validate-type="money" data-digits="2" data-integer-length="13"/>
        </k-form-item>
        <k-form-item label="折算人民币金额">
          <k-field-text v-model="formData.convertRmb" :data-allowblank="false" data-validate-type="money" data-digits="2" data-integer-length="13"/>
        </k-form-item>
        <k-form-item label="确认净值">
          <k-field-text v-model="formData.nav" :data-allowblank="false" data-validate-type="money"  data-digits="5" data-integer-length="5"/>
        </k-form-item>
        <k-form-item label="份额">
          <k-field-text v-model="formData.ackVol" :data-allowblank="false" data-validate-type="money" data-digits="5" data-integer-length="13"/>
        </k-form-item>
        <k-form-item label="费用">
          <k-field-text v-model="formData.feeAmt" :data-allowblank="false" data-validate-type="money" data-digits="2" data-integer-length="13"/>
        </k-form-item>
        <k-form-item label="渠道">
          <k-field-select v-model="formData.channelFlag" data-dict="subm_tr_channel_flag_z" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="交易柜员号">
          <k-field-text v-model="formData.inputuser" :data-allowblank="false" :data-max-length="32"/>
        </k-form-item>
        <k-form-item label="备注">
          <k-field-text v-model="formData.remark" :data-max-length="32"/>
        </k-form-item>
        <k-form-item label="登记流水号">
          <k-field-text v-model="formData.registerSerno" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="登记日期">
          <k-field-date v-model="formData.registerDate" :data-allowblank="false" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-footer slot="footer" data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="CustTransInfoh.updateCustTransInfoh" data-from="editTrCustTransInfoHForm"
            :data-model="formData" data-target="trCustTransInfoGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="uploadTrCustTransInfoModifyPopup" data-title="投资者明细信息登记历史信息变更数据导入">
        <k-form ref="addModifyForm" data-ui="element">
          <k-form-item label="变更数据导入" data-ui="element" data-input-width="500px">
            <k-field-excel-upload data-type="file" ref="uploadModifyRef" :data-multiple="false" :data-limit='1'
                data-accept=".xlsx,.xls"
                :data-error="onModifySubmitError" :data-success="onModifySubmitSuccess"
                :data-auto-upload="false"
               data-upload-url="upload/server/RptApp/reportManage/custTransModifyImport.json">
            </k-field-excel-upload>
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="trCustTransInfoGrid" ref="submitBtn"
                  :data-auto-upload="false" data-from="addModifyForm" :data-handler="submitUploadModifyParam">
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
import Tools from "@/utils/tools";
export default {
  name: "CustTransInfoh",
  data() {
    return {
      formData: {},
      selectRowData: {},
      searchParam:{},
      RegisterDate:[]
    };
  },
  methods: {
    handleConfirmExport() {
			this.httpUtil
				.comnQuery({
					action: "BaseReportExportLog.fileStatusQuery",
					params: {reportName: this.$refs.exportBtnRef.dataExportName},
					successAlert: false,
				})
				.then((data) => {
					if (data.returndata.flag == '0') {
            if (!this.handleConfirm()) {
              return false;
            }
            this.$refs.exportBtnRef.handleExport(this.searchParam);
					} else if (data.returndata.flag == '1') {
            Tools.alertTime(data.returnmsg, "danger", 0);
          } else if (data.returndata.flag == '2') {
            Tools.alertTime(data.returnmsg, "success", 3000);
          }
				});
			return false;
		},
    handleData(row) {
      row.map(sr => {
        sr.custName = this.sensitiveNameHandle(sr.custName);//处理行内名称敏感数据规则
        sr.acctNo = this.sensitiveAccountHandle(sr.acctNo);//处理行内存款账号敏感数据规则
      })
      return row;
    },
    //行内规则-客户法定名称(姓名、拼音、英文名)：至多显示前1/3和后1/3（向下取整），其他用*号代替
    sensitiveNameHandle(name){
      if(name !== null) {
        const length = name.length;
        const limit = Math.floor(length/3);
        const left = name.slice(0,limit);
        const right = name.slice(length - limit, length);
        return left + '*'.repeat(length - limit * 2) + right;
      }
    },
    //行内规则-银行卡卡号:显示前6位+*(实际位数)+后4位
    sensitiveAccountHandle(card){
      if(card !== null) {
        const length = card.length;
        if(length <= 6) {
          return card;
        }
        const left = card.slice(0,6);
        const right = card.slice(length - 4, length);
        return left + '*'.repeat(length - 10) + right;
      }
    },
    handleConfirm() {
      if (!this.searchParam.reportDate && !this.searchParam.custNo) {
        this.$message.error('“数据日期”和“识别标识”不能同时为空!');
        return;
      }
      return true;
    },
    onModifySubmitSuccess() {
      this.$refs.uploadModifyBtnRef.setIconStyle(1);
    },
    onModifySubmitError() {
      this.$refs.uploadModifyBtnRef.setIconStyle(1);
    },
    submitUploadModifyParam() {
      //文件上传校验
      var validate = this.$refs.addModifyForm.validate();
      if (validate) {
        let formData = { reportDate: ''};
        let temp = document.getElementsByClassName('upload-demo');
        let lis = temp[0].childNodes[1].childNodes.length;
        if (lis > 0) {
          this.$refs.uploadModifyBtnRef.setIconStyle(0);
          this.$refs.uploadModifyRef.upload(formData);
          setTimeout(()=>{
            this.$refs.uploadTrCustTransInfoModifyPopup.close();
          }, 300)
        } else {
          Tools.alert("上传文件不能为空!", "danger");
        }
      }
      return false;
    },
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
    },
    loadGriding(val){
      this.$refs.validateInfoPopup.close();
    }
  },
  watch: {
    //查询起息日
    RegisterDate() {
      this.$set(this.searchParam, 'startDate', this.RegisterDate == null ? '' : this.RegisterDate[0]);
      this.$set(this.searchParam, 'endDate', this.RegisterDate == null ? '' : this.RegisterDate[1]);
    },
  }
};
</script>

