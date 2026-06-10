<template>
  <div class="py-page">
    <div>
      <k-form-search-customize ref="searchFormRef" v-model="queryParam" data-target="trCustTransInfoGrid">
        <k-form-item label="数据日期">
          <k-field-date v-model="queryParam.reportDate" :data-allowblank="false" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="客户统一编号">
          <k-field-text v-model="queryParam.hostCustNo"/>
        </k-form-item>
        <k-form-item label="识别标识">
          <k-field-text v-model="queryParam.custNo"/>
        </k-form-item>
        <k-form-item label="业务种类">
          <k-field-select v-model="queryParam.busiCode" data-dict="subm_tr_busi_code"/>
        </k-form-item>
        <k-form-item label="产品登记编码">
          <k-field-text v-model="queryParam.prodCode"/>
        </k-form-item>
        <k-form-item label="销售机构代码">
          <k-field-text v-model="queryParam.agentBankCode"/>
        </k-form-item>
        <k-form-item label="销售机构名称">
          <k-field-text v-model="queryParam.agentBankName"/>
        </k-form-item>
        <k-form-item label="报送状态">
          <k-field-select v-model="queryParam.registerStatus" data-dict="subm_report_status"></k-field-select>
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addTrCustTransInfoPopup" v-if="global.isShowAuthorityButton('TrCustTransInfo.addTrCustTransInfo')">
            <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
          <k-btn slot="button" ref="uploadBtnRefA" :load-disabled="false" data-functype="POPUP" class="btn-custom-plain"
                data-target="uploadImportTrCustTransInfoPopup" v-if="global.isShowAuthorityButton('TrCustTransInfo.download')">
            <md-icon>cloud_upload</md-icon>
            导入
          </k-btn>
          <!-- <k-btn slot="button" class="btn-custom-plain" :data-download-name="'投资者明细信息导入模板'+'.xlsx'"
                data-descript="下载Excel模板" data-functype="DOWNLOAD" data-size="small"
                data-url="/download/server/RptApp/chinaBondSubmit/TrCustTransInfo/comn-download.json">
            <md-icon>cloud_download</md-icon>
            下载Excel模板
          </k-btn> -->
          <k-btn slot="button" ref="exportBtn" data-functype="POPUP" :handleBefore="handleBefore" class="btn-custom-plain" data-target="exportTrCustTransInfoPopup" v-if="global.isShowAuthorityButton('TrCustTransInfo.download')">
            <md-icon>cloud_download</md-icon>
            导出
          </k-btn>
           <k-btn slot="button" :handleBefore="handleBefore"  class="btn-custom-plain"  data-functype="SUBMIT" :data-model="queryParam" data-action="TrCustTransInfo.updateCustTransInfoRegistStatusSuccess">
             <md-icon md-src="/static/svg/confirm.svg"></md-icon>
            确认报送状态
          </k-btn>
          <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="auditPopup" v-if="global.isShowAuthorityButton('TrCustTransInfo.download')">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>复核
          </k-btn>
          <k-btn slot="button" ref="uploadBtnRefB" data-functype="POPUP" class="btn-custom-plain"
                  data-target="uploadUpdateTrCustTransInfoPopup" v-if="global.isShowAuthorityButton('TrCustTransInfo.batchImport')">
            <md-icon>cloud_upload</md-icon>批量修改导入
          </k-btn>
        </div>
      </div>
      <k-grid ref="trCustTransInfoGrid" @data-row-select="selectRow" data-fixed="right" data-operate-width="250px" data-action="TrCustTransInfo.findTrCustTransInfos"
              :data-autoload="false" :handleDataFun="handleData">
        <k-grid-column data-align="left" data-header="ID" data-name="id" data-hidden="true" data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="数据日期" data-name="reportDate" data-width="120" data-export="false"></k-grid-column>
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
        <k-grid-column data-align="left" data-header="**业务确认时间" data-name="ackTime" data-width="120"></k-grid-column>
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
          <k-btn class="btn-custom-text" data-descript="修改投资者明细信息登记表" data-functype="POPUP" data-size="mini"
              v-show="scope.row.row.registerStatus != '5'"    data-target="editTrCustTransInfoPopup" v-if="global.isShowAuthorityButton('TrCustTransInfo.updateTrCustTransInfo')">
            修改
          </k-btn>
          <!--<k-btn data-functype="PAGE" data-size="mini" class="btn-custom-plain" :data-model="scope.row.row.id"
                 @click="popupEdit(scope.row.row)"  data-descript="投资者明细信息登记管理错误详情">
            错误详情
          </k-btn>-->
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="TrCustTransInfo.deleteTrCustTransInfo" data-size="mini"
              v-show="scope.row.row.registerStatus != '5'"    data-type="danger" data-target="trCustTransInfoGrid" :data-confirm="true" data-descript="删除投资者明细信息登记管理">
            删除
          </k-btn>
          <k-btn class="btn-custom-text" data-descript="校验失败详情" data-functype="POPUP"
                 data-target="validateInfoPopup" v-if="scope.row.row.registerStatus === '1' ">
            校验失败详情
          </k-btn>
        </template>
      </k-grid>
    </div>
  <!--    校验失败详情弹出框   -->
  <k-popup ref="validateInfoPopup" data-title="详情">
    <ErroComp ref="ErroComp" @loadGriding="loadGriding"
              :info="{...formData,...auditInfoPopupData}"
              :disabledVal="false"/>
  </k-popup>
	<!--    添加投资者明细信息登记表弹出框   -->
	<k-popup ref="addTrCustTransInfoPopup" data-title="新增">
    	<k-form ref="addTrCustTransInfoForm" :data-col="2" isFormBodyScreen>
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
	        	<k-field-text v-model="formData.custName" :data-allowblank="false" :data-max-length="32"/>
	     	</k-form-item> -->
        <k-form-item label="交易序列号">
          <k-field-text v-model="formData.dealNo" :data-allowblank="false" :data-max-length="30"/>
        </k-form-item>
			  <k-form-item label="关联活期存款账号">
	        	<k-field-text v-model="formData.acctNo" :data-allowblank="false" :data-max-length="60"/>
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
	        	<k-field-text v-model="formData.prodCode" :data-allowblank="false" :data-max-length="15"/>
	     	</k-form-item>
        <k-form-item label="子份额代码">
	        	<k-field-text v-model="formData.sonShareCode" :data-max-length="60"/>
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
	        	<k-field-select v-model="formData.speChannelFlag" data-dict="subm_tr_spe_channel_flag_z"/>
	     	</k-form-item>
			  <k-form-item label="金额(元)">
	        	<k-field-text v-model="formData.ackAmt" :data-allowblank="false" data-validate-type="money" data-digits="5" data-integer-length="13"/>
	     	</k-form-item>
			  <k-form-item label="折算人民币金额">
	        	<k-field-text v-model="formData.convertRmb" :data-allowblank="false" data-validate-type="money" data-digits="5" data-integer-length="13"/>
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
	        	<k-field-text v-model="formData.inputuser" :data-allowblank="false" :data-max-length="30"/>
	     	</k-form-item>
			  <k-form-item label="备注">
	        	<k-field-text v-model="formData.remark" :data-max-length="256"/>
	     	</k-form-item>
	   <!--    	<k-form-item label="登记日期">
          <k-field-date v-model="formData.registerDate" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>-->
        <k-form-footer slot="footer" data-align="center">
          <k-btn class="btn-custom-primary"  ref="sumbitadd" :data-handler="sumbit_add"   data-from="addTrCustTransInfoForm"
                 :data-model="formData" data-target="trCustTransInfoGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改投资者明细信息登记表弹出框   -->
	<k-popup ref="editTrCustTransInfoPopup" data-title="修改">
	  <k-form ref="editTrCustTransInfoForm" :data-col="2" isFormBodyScreen>
	    <k-form-item label="ID" v-if="false">
        <k-field-text v-model="formData.id" />
      </k-form-item>
	    <k-form-item label="数据日期" :class="[handleItemDiff('reportDate')]">
        <k-field-date v-model="formData.reportDate" :data-allowblank="false"  :data-disabled="true" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
      </k-form-item>
  		<k-form-item label="登记机构代码" :class="[handleItemDiff('bankCode')]">
        <k-field-text v-model="formData.bankCode" :data-allowblank="false"  :data-disabled="true" :data-max-length="6"/>
      </k-form-item>
      <k-form-item label="核心交易流水号" :class="[handleItemDiff('transSerno')]">
        <k-field-text v-model="formData.transSerno" :data-allowblank="false"  :data-disabled="true" :data-max-length="32"/>
      </k-form-item>
      <k-form-item label="销售合同号" :class="[handleItemDiff('contractNo')]">
        <k-field-text v-model="formData.contractNo" :data-allowblank="false"   :data-disabled="true" :data-max-length="32"/>
      </k-form-item>
      <k-form-item label="理财账号" :class="[handleItemDiff('fncTransAcctNo')]">
        <k-field-text v-model="formData.fncTransAcctNo" :data-allowblank="false"  :data-disabled="true" :data-max-length="32"/>
      </k-form-item>
      <k-form-item label="客户统一编号" :class="[handleItemDiff('hostCustNo')]">
        <k-field-text v-model="formData.hostCustNo" :data-allowblank="false"  :data-disabled="true" :data-max-length="32"/>
      </k-form-item>
      <k-form-item label="识别标识" :class="[handleItemDiff('custNo')]">
        <k-field-text v-model="formData.custNo" :data-allowblank="false"    :data-disabled="true" :data-max-length="32"/>
      </k-form-item>
      <!-- <k-form-item label="客户姓名" :class="[handleItemDiff('custNameDisplay')]">
        <k-field-text v-model="formData.custNameDisplay" :data-allowblank="false" :data-max-length="32"/>
      </k-form-item> -->
      <k-form-item label="交易序列号" :class="[handleItemDiff('dealNo')]">
        <k-field-text v-model="formData.dealNo" :data-allowblank="false" :data-max-length="30"/>
      </k-form-item>
      <k-form-item label="关联活期存款账号" :class="[handleItemDiff('acctNoDisplay')]">
        <k-field-text v-model="formData.acctNoDisplay" :data-allowblank="false" :data-max-length="60"/>
      </k-form-item>
      <k-form-item label="关联活期存款账号开户行代码" :class="[handleItemDiff('acctBankNo')]">
        <k-field-text v-model="formData.acctBankNo" :data-allowblank="false" :data-max-length="30"/>
      </k-form-item>
      <k-form-item label="关联活期存款账号开户行名称" :class="[handleItemDiff('acctBankName')]">
        <k-field-text v-model="formData.acctBankName" :data-allowblank="false" :data-max-length="200"/>
      </k-form-item>
      <k-form-item label="关联账号开户所在地" :class="[handleItemDiff('acctLocCode')]">
        <k-field-select v-model="formData.acctLocCode" :data-allowblank="false" data-dict="subm_prod_sale_area"/>
      </k-form-item>
      <k-form-item label="是否代销" :class="[handleItemDiff('isAgent')]">
        <k-field-select v-model="formData.isAgent" data-dict="tr_is_belong" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="销售机构代码" :class="[handleItemDiff('agentBankCode')]">
        <k-field-text v-model="formData.agentBankCode" :data-max-length="32"/>
      </k-form-item>
      <k-form-item label="销售机构名称" :class="[handleItemDiff('agentBankName')]">
        <k-field-text v-model="formData.agentBankName" :data-max-length="32"/>
      </k-form-item>
      <k-form-item label="销售机构所属监管机构" :class="[handleItemDiff('agentReguCode')]">
        <k-field-select v-model="formData.agentReguCode" data-dict="subm_tr_agent_regu_code"/>
      </k-form-item>
      <k-form-item label="产品登记编码" :class="[handleItemDiff('prodCode')]">
        <k-field-text v-model="formData.prodCode" :data-allowblank="false"  :data-disabled="true" :data-max-length="15"/>
      </k-form-item>
      <k-form-item label="子份额代码" :class="[handleItemDiff('sonShareCode')]">
	      <k-field-text v-model="formData.sonShareCode"   :data-disabled="true" :data-max-length="60"/>
	    </k-form-item>
      <k-form-item label="业务种类" :class="[handleItemDiff('busiCode')]">
        <k-field-select v-model="formData.busiCode" data-dict="subm_tr_busi_code" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="业务发生地所属监管" :class="[handleItemDiff('busiReguCode')]">
        <k-field-select v-model="formData.busiReguCode" data-dict="subm_tr_agent_regu_code" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="业务确认日期" :class="[handleItemDiff('ackDate')]">
        <k-field-date v-model="formData.ackDate" :data-allowblank="false"   data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd"/>
      </k-form-item>
      <k-form-item label="业务确认时间" :class="[handleItemDiff('ackTime')]">
        <k-field-time v-model="formData.ackTime" :data-allowblank="false" :data-max-length="32"/>
      </k-form-item>
      <k-form-item label="币种" :class="[handleItemDiff('cur')]">
        <k-field-select v-model="formData.cur" data-dict="tr_cur" :data-allowblank="false" :data-max-length="32"/>
      </k-form-item>
      <k-form-item label="特殊渠道" :class="[handleItemDiff('speChannelFlag')]">
	      <k-field-select v-model="formData.speChannelFlag" data-dict="subm_tr_spe_channel_flag_z"/>
	    </k-form-item>
      <k-form-item label="金额(元)" :class="[handleItemDiff('ackAmt')]">
        <k-field-text v-model="formData.ackAmt" :data-allowblank="false" data-validate-type="money" data-digits="5" data-integer-length="13"/>
      </k-form-item>
      <k-form-item label="折算人民币金额" :class="[handleItemDiff('convertRmb')]">
        <k-field-text v-model="formData.convertRmb" :data-allowblank="false" data-validate-type="money" data-digits="5" data-integer-length="13"/>
      </k-form-item>
      <k-form-item label="确认净值" :class="[handleItemDiff('nav')]">
        <k-field-text v-model="formData.nav" :data-allowblank="false" data-validate-type="money"  data-digits="5" data-integer-length="5"/>
      </k-form-item>
      <k-form-item label="份额" :class="[handleItemDiff('ackVol')]">
        <k-field-text v-model="formData.ackVol" :data-allowblank="false" data-validate-type="money" data-digits="5" data-integer-length="13"/>
      </k-form-item>
      <k-form-item label="费用" :class="[handleItemDiff('feeAmt')]">
        <k-field-text v-model="formData.feeAmt" :data-allowblank="false" data-validate-type="money" data-digits="2" data-integer-length="13"/>
      </k-form-item>
      <k-form-item label="渠道" :class="[handleItemDiff('channelFlag')]">
        <k-field-select v-model="formData.channelFlag" data-dict="subm_tr_channel_flag_z" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="交易柜员号" :class="[handleItemDiff('inputuser')]">
        <k-field-text v-model="formData.inputuser" :data-allowblank="false" :data-max-length="30"/>
      </k-form-item>
      <k-form-item label="备注" :class="[handleItemDiff('remark')]">
        <k-field-text v-model="formData.remark" :data-max-length="256"/>
      </k-form-item>
     <!-- <k-form-item label="登记流水号" :class="[handleItemDiff('registerSerno')]">
        <k-field-text v-model="formData.registerSerno" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="登记日期" :class="[handleItemDiff('registerDate')]">
        <k-field-date v-model="formData.registerDate" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
      </k-form-item>-->
	    <k-form-footer slot="footer" data-align="center">
	      <k-btn class="btn-custom-primary"  ref="sumbitedit" :data-handler="sumbit_edit"  data-from="editTrCustTransInfoForm"
	        :data-model="formData" data-target="trCustTransInfoGrid" :handle-before="handleBeforeUpdate">
	        <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
	      </k-btn>
	      <k-btn class="btn-custom-plain" data-functype="CLOSE">
	        <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	    </k-form-footer>
	  </k-form>
	</k-popup>


    <!-- 模板上传 -->
    <!-- <k-popup ref="addPopup" title="上传Excels">
      <k-form ref="addForm" data-ui="element">
        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-excel-upload data-type="picture" ref="uploadRef" :data-multiple="false" :data-limit=1
                                :data-error="onSubmitError" :data-success="onSubmitSuccess" data-accept=".xlsx,.xls"
                                :data-auto-upload="false"
                                data-upload-url="/upload/server/PmsApp/chinaBondSubmit/TrCustTransInfo/comn-upload.json">
          </k-field-excel-upload>
        </k-form-item>
        <k-form-footer slot="footer" data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="trCustTransInfoGrid" ref="submitBtn"
                 :data-auto-upload="false" data-from="addForm" :data-handler="submitUploadParam">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup> -->
    <k-popup ref="uploadImportTrCustTransInfoPopup" data-title="导入" @data-opened="uploadOpened()">
        <k-form ref="addForm" data-ui="element">
           <!-- <k-form-item label="数据日期">
            <k-field-date v-model="formData.reportDate"  :data-allowblank="false" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
          </k-form-item> -->
          <k-form-item label="导入" data-ui="element" data-input-width="500px">
            <k-field-excel-upload data-type="file" ref="uploadRefA" :data-multiple="false" :data-limit='1'
                data-accept=".xlsx,.xls"
                :data-error="onSubmitErrorA" :data-success="onSubmitSuccessA"
                :data-auto-upload="false"
               data-upload-url="upload/server/RptApp/reportManage/custTransImport.json">
            </k-field-excel-upload>
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="trCustTransInfoGrid" ref="submitBtn"
                  :data-auto-upload="false" data-from="addForm" :data-handler="submitImportUploadParam">
                  <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
          </k-form-footer>
        </k-form>
        </k-popup>
    <k-popup ref="uploadUpdateTrCustTransInfoPopup" data-title="批量修改导入" @data-opened="uploadOpened()">
        <k-form ref="addForm" data-ui="element">
           <k-form-item label="数据日期">
            <k-field-date v-model="formData.reportDate"  :data-allowblank="false" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
          </k-form-item>
          <k-form-item label="批量修改导入" data-ui="element" data-input-width="500px">
            <k-field-excel-upload data-type="file" ref="uploadRefB" :data-multiple="false" :data-limit='1'
                data-accept=".xlsx,.xls"
                :data-error="onSubmitErrorB" :data-success="onSubmitSuccessB"
                :data-auto-upload="false"
               data-upload-url="upload/server/RptApp/reportManage/custTransUpdateImport.json">
            </k-field-excel-upload>
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="trCustTransInfoGrid" ref="submitBtn"
                  :data-auto-upload="false" data-from="addForm" :data-handler="submitUpdateUploadParam">
                  <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
          </k-form-footer>
        </k-form>
        </k-popup>

    <k-popup ref="exportTrCustTransInfoPopup" data-title="报送数据导出" @data-opened="exportOpened()">
        <k-form data-ui="element">
          <k-form-item label="数据日期">
				    <k-field-date v-model="reportDateRange" :data-allowblank="false"
                 data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
			    </k-form-item>
          <k-form-item label="客户统一编号">
            <k-field-text v-model="exportParam.hostCustNo"/>
          </k-form-item>
          <k-form-item label="识别标识">
            <k-field-text v-model="exportParam.custNo"/>
          </k-form-item>
          <k-form-item label="业务种类">
            <k-field-select v-model="exportParam.busiCode" data-dict="subm_tr_busi_code"/>
          </k-form-item>
          <k-form-item label="产品登记编码">
            <k-field-text v-model="exportParam.prodCode"/>
          </k-form-item>
          <k-form-item label="销售机构代码">
            <k-field-text v-model="exportParam.agentBankCode"/>
          </k-form-item>
          <k-form-item label="销售机构名称">
            <k-field-text v-model="exportParam.agentBankName"/>
          </k-form-item>
          <k-form-item label="报送状态">
            <k-field-select v-model="exportParam.registerStatus" data-dict="subm_report_status"/>
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn ref="exportBtnRef" class="btn-custom-primary"  data-functype="EXPORT" data-target="trCustTransInfoGrid"
                  data-action="TrCustTransInfo.download" :data-export-name="'投资者明细信息登记管理'" :report-date="exportParam.reportDates" :data-handler="handleConfirmExport">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
              <k-btn class="btn-custom-plain" data-functype="CLOSE">
                <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
          </k-form-footer>
        </k-form>
        </k-popup>

    <k-popup ref="auditInfoPopup">
      <k-form ref="updateAuditStatusForm" :data-col="2" isFormBodyScreen>
         <k-form-item label="报表类型">
           <k-field-text v-model="infoPop.tableName" :data-allowblank="false" data-disabled="true"/>
         </k-form-item>
         <k-form-item label="数据日期" data-label-width="100px">
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
import Tools from "@/utils/tools";
import ErroComp from "@/pages/zz/manage/validateInfo";

export default {
  name: "TrCustTransInfo",
  components: {ErroComp},
  data() {
    return {
      formData: {},
      formDataCopy: {},
      queryParam: {},
      selectRowData: {},
      BreathDay: [],
      queryParamDateRange: [],
      beforeDate: '',
      exportParam: {},
      reportDateRange: [],
      uploadBeginDate: '',
      uploadQueryDate: '',

      infoPop: {},
      auditInfoPopupData: {
        tableId: 'app_cust_trans_info',
        tableName: '投资者明细信息登记管理'
      }
    };
  },
  methods: {
    handleBefore() {
			return this.$refs.searchFormRef.$refs.searchForm.validate();
		},
    handleConfirmExport() {
      if ((this.exportParam.reportStartDate == null || this.exportParam.reportStartDate == '') &&
          (this.exportParam.reportEndDate == null || this.exportParam.reportEndDate == '')) {
        this.$message.error('“数据日期”不能为空!');
        return false;
      }

			this.httpUtil
				.comnQuery({
					action: "BaseReportExportLog.fileStatusQuery",
					params: {
            reportName: this.$refs.exportBtnRef.dataExportName,
            reportStartDate: this.exportParam.reportStartDate,
            reportEndDate: this.exportParam.reportEndDate
          },
					successAlert: false,
				})
				.then((data) => {
					if (data.returndata.flag == '0') {
            this.exportParam.reportDate = data.returndata.reportDate;
            this.$refs.exportBtnRef.handleExport(this.exportParam);
					} else if (data.returndata.flag == '1') {
            Tools.alertTime(data.returnmsg, "danger", 0);
          } else if (data.returndata.flag == '2') {
            Tools.alertTime(data.returnmsg, "success", 3000);
          }
				});
			return false;
		},
   sumbit_edit(){
           this.$refs.sumbitedit.setIconStyle(0,[]);
            if(this.$refs.editTrCustTransInfoForm.validate()){
                  this.httpUtil.query({
                          url: 'server/json/RptApp/audit/checkTrCustTransInfo.json',
                          params:  this.formData
                                   }).then(res => {
                                     if(res.success) {
                                      this.httpUtil.comnUpdate({
                                               action: 'TrCustTransInfo.updateTrCustTransInfo',
                                               params:  this.formData
                                                }).then(res => {
                                                 if(res.success) {
                                                 this.$refs.editTrCustTransInfoPopup.close();
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
   sumbit_add(){
       this.$refs.sumbitadd.setIconStyle(0,[]);
        if(this.$refs.addTrCustTransInfoForm.validate()){
              this.httpUtil.query({
                      url: 'server/json/RptApp/audit/checkTrCustTransInfo.json',
                      params:  this.formData
                               }).then(res => {
                                 if(res.success) {
                                  this.httpUtil.comnUpdate({
                                           action: 'TrCustTransInfo.addTrCustTransInfo',
                                           params:  this.formData
                                            }).then(res => {
                                             if(res.success) {
                                             this.$refs.addTrCustTransInfoPopup.close();
                                         }else{
                                           this.$refs.sumbitadd.setIconStyle(1,[]);
                                         }
                               })
                         }else{
                             this.$refs.sumbitadd.setIconStyle(1,[]);
                         }
              });
         }else{
           this.$refs.sumbitadd.setIconStyle(1,[]);
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
               this.$confirm("日期区间存在指标校验未通过数据,确认复核吗？", "操作提示", {
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
                     this.$refs.trCustTransInfoGrid.load(this.queryParam);
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
                 this.$refs.trCustTransInfoGrid.load(this.queryParam);
                 this.$refs.auditInfoPopup.close();
                }
              })
           }
         }
       })
    },
    handleBeforeUpdate() {
      if (this.formNoChangeCb()) {
        this.$refs.editTrCustTransInfoPopup.close();
        return false
      }
      return true
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
    popupEdit(row) {
      let pathUrl = '/main/zz/errorInfo/CustTransInfoErr';
      this.$router.push({
        path: pathUrl,
        query: {
          registerSerno: row.registerSerno,
        },
      });
    },
     onSubmitSuccessA() {
      this.$refs.trCustTransInfoGrid.load(this.queryParam);
      this.$refs.uploadBtnRefA.setIconStyle(1);
    },
    onSubmitErrorA() {
      this.$refs.uploadBtnRefA.setIconStyle(1);
    },
    onSubmitSuccessB() {
      this.$refs.trCustTransInfoGrid.load(this.queryParam);
      this.$refs.uploadBtnRefB.setIconStyle(1);
    },
    onSubmitErrorB() {
      this.$refs.uploadBtnRefB.setIconStyle(1);
    },
    submitImportUploadParam() {
      //文件上传校验
      var validate = this.$refs.addForm.validate();
      if (validate) {
        let formData = { reportDate: this.formData.reportDate};
        let temp = document.getElementsByClassName('upload-demo');
        let lis = temp[0].childNodes[1].childNodes.length;
        if (lis > 0) {
          this.$refs.uploadBtnRefA.setIconStyle(0);
          this.$refs.uploadRefA.upload(formData);
          setTimeout(()=>{
            this.$refs.uploadImportTrCustTransInfoPopup.close();
          }, 300)
        } else {
          Tools.alert("上传文件不能为空!", "danger");
        }
      }
      return false;
    },
    submitUpdateUploadParam() {
      //文件上传校验
      var validate = this.$refs.addForm.validate();
      if (validate) {
        let formData = { reportDate: this.formData.reportDate};
        let temp = document.getElementsByClassName('upload-demo');
        let lis = temp[0].childNodes[1].childNodes.length;
        if (lis > 0) {
          this.$refs.uploadBtnRefB.setIconStyle(0);
          this.$refs.uploadRefB.upload(formData);
          setTimeout(()=>{
            this.$refs.uploadUpdateTrCustTransInfoPopup.close();
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
      this.formDataCopy = Object.assign({}, row)
    },
    uploadOpened() {
     this.$set(this.formData, 'reportDate', this.queryParam.reportDate);
    },
    exportOpened() {
      if (this.queryParam.reportDate) {
        this.reportDateRange = [this.queryParam.reportDate, this.queryParam.reportDate];
        this.$set(this.exportParam, 'reportStartDate', this.queryParam.reportDate);
        this.$set(this.exportParam, 'reportEndDate', this.queryParam.reportDate);
        this.$set(this.exportParam, 'reportDates', this.queryParam.reportDate);
      } else {
        this.reportDateRange = [this.beforeDate, this.beforeDate];
        this.$set(this.exportParam, 'reportStartDate', this.beforeDate);
        this.$set(this.exportParam, 'reportEndDate', this.beforeDate);
        this.$set(this.exportParam, 'reportDates', this.beforeDate);
      }
      this.$set(this.exportParam, 'hostCustNo', this.queryParam.hostCustNo);
      this.$set(this.exportParam, 'custNo', this.queryParam.custNo);
      this.$set(this.exportParam, 'busiCode', this.queryParam.busiCode);
      this.$set(this.exportParam, 'prodCode', this.queryParam.prodCode);
      this.$set(this.exportParam, 'agentBankCode', this.queryParam.agentBankCode);
      this.$set(this.exportParam, 'agentBankName', this.queryParam.agentBankName);
      this.$set(this.exportParam, 'registerStatus', this.queryParam.registerStatus);
    },
    // 复核弹窗
    auditPopup() {
      this.infoPop = this.auditInfoPopupData;
      this.httpUtil.sysDate().then(res=>{
        if (res) {
          this.queryParamDateRange = [res,res];
        }
      });
      this.$refs.auditInfoPopup.popup();
    },
    loadGriding(val){
      this.$refs.validateInfoPopup.close();
      this.$refs.trCustTransInfoGrid.load(this.queryParam);
    }
  },
  created() {
    let yesterday = new Date();
    yesterday.setDate(yesterday.getDate() -1);
    let year = yesterday.getFullYear(); //获取年
    let month = yesterday.getMonth(); //获取月
    let date = yesterday.getDate(); //得到日期
    month = month + 1;
    month = month.toString().padStart(2, "0");
    date = date.toString().padStart(2, "0");
    let  defaultDate = `${year}${month}${date}`;
    this.beforeDate = defaultDate;
    this.$set(this.queryParam, "reportDate", defaultDate);
  },
  watch: {
      BreathDay() {
        this.$set(this.queryParam, 'startDate', this.BreathDay == null ? null : this.BreathDay[0]);
        this.$set(this.queryParam, 'endDate', this.BreathDay == null ? null : this.BreathDay[1]);
      },
      queryParamDateRange() {
        this.$set(this.queryParam, 'queryStartDate', this.queryParamDateRange == null ? null : this.queryParamDateRange[0]);
        this.$set(this.queryParam, 'queryEndDate', this.queryParamDateRange == null ? null : this.queryParamDateRange[1]);
      },
      reportDateRange() {
        this.$set(this.exportParam, 'reportStartDate', this.reportDateRange == null ? null : this.reportDateRange[0]);
        this.$set(this.exportParam, 'reportEndDate', this.reportDateRange == null ? null : this.reportDateRange[1]);
        this.$set(this.exportParam, 'reportDates', this.reportDateRange[0]+'-'+this.reportDateRange[1]);
      }
    }
  };
</script>
