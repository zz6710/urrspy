<template>
  <div class="py-page">
    <div>

      <k-form-search-customize data-label-width="100px" ref="searchFormRef"  v-model="queryParam" data-target="trCustRegisterInfoGrid">
        <k-form-item label="数据日期">
          <k-field-date v-model="queryParamDateRange" :data-allowblank="false" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>

        <k-form-item label="个人证件类别">
          <k-field-select v-model="queryParam.personalIdType" data-dict="tr_personal_id_type"/>
        </k-form-item>

        <k-form-item label="机构证件类别">
          <k-field-select v-model="queryParam.organizationIdType" data-dict="tr_organization_id_type"/>
        </k-form-item>

        <k-form-item label="数据类型">
          <k-field-select v-model="queryParam.dataType" data-dict="tr_data_type"/>
        </k-form-item>

        <k-form-item label="识别标识">
          <k-field-text v-model="queryParam.custNo"/>
        </k-form-item>

        <k-form-item label="TA_ID">
          <k-field-text v-model="queryParam.taId"/>
        </k-form-item>

        <k-form-item label="投资者类别">
          <k-field-select v-model="queryParam.custType" data-dict="tr_cust_type"/>
        </k-form-item>

        <k-form-item label="证件号码">
          <k-field-text v-model="queryParam.idCode" data-validate-type="text"/>
        </k-form-item>

        <k-form-item label="报送状态">
          <k-field-select v-model="queryParam.registerStatus" data-dict="subm_report_status"></k-field-select>
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="handleAddBtn" data-target="addTrCustRegisterInfoPopup" v-if="global.isShowAuthorityButton('TrCustRegisterInfo.addTrCustRegisterInfo')">
            <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
          <k-btn slot="button" ref="uploadBtnRefA" :load-disabled="false" data-functype="POPUP" class="btn-custom-plain"
                data-target="uploadImportTrCustRegisterInfoPopup" v-if="global.isShowAuthorityButton('TrCustRegisterInfo.download')">
            <md-icon>cloud_upload</md-icon>
            导入
          </k-btn>
          <!-- <k-btn slot="button" class="btn-custom-plain" :data-download-name="'投资者身份信息导入模板'+'.xlsx'"
                data-descript="下载Excel模板" data-functype="DOWNLOAD" data-size="small"
                data-url="/download/server/RptApp/chinaBondSubmit/TrCustRegisterInfo/comn-download.json">
            <md-icon>cloud_download</md-icon>
            下载Excel模板
          </k-btn> -->
          <k-btn slot="button" ref="exportBtn" data-functype="POPUP" :handleBefore="handleBefore" class="btn-custom-plain" data-target="exportTrCustRegisterInfoPopup" v-if="global.isShowAuthorityButton('TrCustRegisterInfo.download')">
            <md-icon>cloud_download</md-icon>
            导出
          </k-btn>
          <k-btn slot="button" :handleBefore="handleBefore"  class="btn-custom-plain"  data-functype="SUBMIT" :data-model="queryParam" data-action="TrCustRegisterInfo.updateTrCustRegisterInfoRegistStatusSuccess">
            <!-- <md-icon>cloud_download</md-icon> -->
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>
            确认报送状态
          </k-btn>
					<!-- <k-btn slot="button" class="btn-custom-plain" data-functype="POPUP" data-target="handleTaskPopup" ref="reloadBtnRef"
						data-action="DwsProdTTRDBef.updateTaskAppQuery" loading-tip="正在重新生成报表，请稍后重试！">
						<md-icon>cloud_download</md-icon>重新生成报表
					</k-btn> -->
          <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="auditPopup" v-if="global.isShowAuthorityButton('TrCustRegisterInfo.download')">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>复核
          </k-btn>
          <k-btn slot="button" ref="uploadBtnRefB" data-functype="POPUP" class="btn-custom-plain"
                  data-target="uploadUpdateTrCustRegisterInfoPopup" v-if="global.isShowAuthorityButton('TrCustRegisterInfo.batchImport')">
            <md-icon>cloud_upload</md-icon>批量修改导入
          </k-btn>
        </div>
      </div>
      <k-grid ref="trCustRegisterInfoGrid" @data-row-select="selectRow" data-fixed="right" data-operate-width="250px"
              data-action="TrCustRegisterInfo.findTrCustRegisterInfos" :data-autoload="false" :handleDataFun="handleData">
        <k-grid-column data-align="left" data-header="ID" data-name="id" data-export="false" data-hidden="true" ></k-grid-column>
        <k-grid-column data-align="left" data-header="数据日期" data-name="reportDate" data-export="false" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="**登记机构代码" data-name="bankCode" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="该投资者是否属于本机构" data-name="isBelong" data-dict="subm_isTrue" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="投资者所属机构名称" data-name="issBankName" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="投资者所属机构代码" data-name="issBankCode" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="*投资者境内外标识" data-name="inOutSign" data-dict="tr_in_out_sign" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="投资者所属国家或地区" data-name="issCountry" data-dict="tr_iss_country" data-width="120" ></k-grid-column>
        <k-grid-column data-align="left" data-header="**数据类型" data-name="dataType" data-dict="tr_data_type" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="*原识别标识" data-name="oriCustNo" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="**识别标识" data-name="custNo" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="*投资者类别" data-name="custType" data-dict="tr_cust_type" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*个人证件类别" data-name="personalIdType" data-dict="tr_personal_id_type" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="*机构证件类别" data-name="organizationIdType" data-dict="tr_organization_id_type" data-width="220"></k-grid-column>
        <k-grid-column data-align="left" data-header="*其他证件名称" data-name="otherIdName" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="*证件号码" data-name="idCode" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="SPV资金托管账户开户行" data-name="spvOpenBank" data-dict="subm_tr_spv_open_bank" data-width="250"></k-grid-column>
        <k-grid-column data-align="left" data-header="其他资金托管账户开户行" data-name="otherOpenBank" data-width="250"></k-grid-column>
        <k-grid-column data-align="left" data-header="*投资者名称" data-name="custName" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="*性别" data-name="sex" data-dict="subm_tr_sex" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="风险偏好" data-name="riskLevel" data-dict="subm_investor_risk_preference" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="*手机号码" data-name="moble" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="*固定电话" data-name="telPhone" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="*电子邮箱" data-name="email" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="备注" data-name="remark" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="登记流水号" data-name="registerSerno" data-export="false" data-width="250"></k-grid-column>
        <k-grid-column data-align="left" data-header="TA_ID" data-name="taId" data-export="false" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="登记日期" data-name="registerDate" data-export="false" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="报送状态" data-name="registerStatus" data-dict="subm_report_status" data-export="false" data-width="120"></k-grid-column>
        <!-- 以下为处理脱敏字段处理 -->
        <k-grid-column data-align="left" data-header="投资者名称" data-name="custNameDisplay" data-export="false" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="证件号码" data-name="idCodeDisplay" data-export="false" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="手机号码" data-name="mobleDisplay" data-export="false" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="固定电话" data-name="telPhoneDisplay" data-export="false" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="电子邮箱" data-name="emailDisplay" data-export="false" data-hidden="true"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-functype="POPUP" data-size="mini" data-descript="修改投资者信息登记" data-target="editTrCustRegisterInfoPopup"
          v-show="scope.row.row.registerStatus != '5'"  :data-handler="handleUpdateBtn" v-if="global.isShowAuthorityButton('TrCustRegisterInfo.updateTrCustRegisterInfo')">
            修改
          </k-btn>
          <!--<k-btn data-functype="PAGE" data-size="mini" class="btn-custom-plain" :data-model="scope.row.row.id"
                 @click="popupEdit(scope.row.row)"  data-descript="投资者信息登记错误详情">
            错误详情
          </k-btn>-->
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="TrCustRegisterInfo.deleteTrCustRegisterInfo" data-size="mini"
           v-show="scope.row.row.registerStatus != '5'"       data-type="danger" data-target="trCustRegisterInfoGrid" :data-confirm="true"  data-descript="删除投资者信息登记">
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
	<!--    添加投资者信息登记弹出框   -->
	<k-popup ref="addTrCustRegisterInfoPopup" data-title="新增"  @data-opened="addOpened()">
    	<k-form ref="addTrCustRegisterInfoForm" :data-col="2" isFormBodyScreen>
        <k-form-item label="数据日期">
          <k-field-date v-model="formData.reportDate" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd" :data-allowblank="false"/>
        </k-form-item>
			  <k-form-item label="登记机构代码">
	        	<k-field-text v-model="formData.bankCode" :data-allowblank="false" :data-max-length="6"/>
	     	</k-form-item>
			  <k-form-item label="该投资者是否属于本机构">
	        	<k-field-select v-model="formData.isBelong" data-dict="isTrue" :data-disabled="formData.isBelongDisabled"/>
	     	</k-form-item>
			  <k-form-item label="投资者所属机构名称">
	        	<k-field-text v-model="formData.issBankName" :data-max-length="60" :data-disabled="formData.issBankNameDisabled"/>
	     	</k-form-item>
			  <k-form-item label="投资者所属机构代码">
	        	<k-field-text v-model="formData.issBankCode" :data-max-length="60" :data-disabled="formData.issBankCodeDisabled"/>
	     	</k-form-item>
			  <k-form-item label="投资者境内外标识">
	        	<k-field-select v-model="formData.inOutSign" data-dict="tr_in_out_sign" @data-on-change="dataOnChange" :data-disabled="formData.inOutSignDisabled"/>
	     	</k-form-item>
			  <k-form-item label="投资者所属国家或地区">
	        	<k-field-select v-model="formData.issCountry" data-dict="tr_iss_country"/>
	     	</k-form-item>
        <k-form-item label="数据类型">
          <k-field-select v-model="formData.dataType" :data-allowblank="false" data-dict="tr_data_type" @data-on-change="dataOnChange"/>
        </k-form-item>
        <k-form-item label="原识别标识" v-if="this.formData.dataType == 2">
          <k-field-text v-model="formData.oriCustNo" :data-allowblank="false" :data-max-length="30"/>
        </k-form-item>
        <k-form-item label="识别标识">
          <k-field-text v-model="formData.custNo" :data-allowblank="false" :data-max-length="30"/>
        </k-form-item>
			  <k-form-item label="投资者类别">
	        	<k-field-select v-model="formData.custType" data-dict="tr_cust_type" :data-allowblank="formData.custTypeAllowblank" @data-on-change="dataOnChange"  :data-disabled="formData.custTypeDisabled"/>
	     	</k-form-item>
			  <k-form-item label="个人证件类别">
	        	<k-field-select v-model="formData.personalIdType" :data-data="formData.addPersonalIdTypeDict" :data-allowblank="formData.personalIdTypeAllowblank" :data-disabled="formData.personalIdTypeDisabled"   data-value-field="VALUE" data-display-field="TEXT"  @data-on-change="dataOnChange"/>
	     	</k-form-item>
			  <k-form-item label="机构证件类别">
	        	<k-field-select v-model="formData.organizationIdType" data-dict="tr_organization_id_type" :data-allowblank="formData.organizationIdTypeAllowblank" :data-disabled="formData.organizationIdTypeDisabled"   @data-on-change="dataOnChange"/>
	     	</k-form-item>
			  <k-form-item label="其他证件名称">
	        	<k-field-text v-model="formData.otherIdName" :data-allowblank="formData.otherIdNameAllowblank" :data-disabled="formData.otherIdNameDisabled" :data-max-length="60"/>
	     	</k-form-item>
			  <k-form-item label="证件号码">
	        	<k-field-text v-model="formData.idCode" :data-max-length="32"/>
	     	</k-form-item>
			  <k-form-item label="SPV资金托管账户开户行" >
	        	<k-field-select v-model="formData.spvOpenBank" data-dict="subm_tr_spv_open_bank" />
	     	</k-form-item>
			  <k-form-item label="其他资金托管账户开户行">
	        	<k-field-text v-model="formData.otherOpenBank" :data-max-length="60"/>
	     	</k-form-item>
			  <k-form-item label="投资者名称">
	        	<k-field-text v-model="formData.custName" :data-max-length="32"/>
	     	</k-form-item>
			  <k-form-item label="性别">
	        	<k-field-select v-model="formData.sex" data-dict="subm_tr_sex"/>
	     	</k-form-item>
			  <k-form-item label="风险偏好">
	        	<k-field-select v-model="formData.riskLevel" data-dict="subm_investor_risk_preference"/>
	     	</k-form-item>
			  <k-form-item label="手机号码">
	        	<k-field-text v-model="formData.moble" data-validate-type="telephone1" :data-max-length="11"/>
	     	</k-form-item>
        <k-form-item label="固定电话">
          <k-field-text v-model="formData.telPhone" :data-max-length="30"/>
        </k-form-item>
        <k-form-item label="电子邮箱">
          <k-field-text v-model="formData.email" data-validate-type="email" :data-max-length="50"/>
        </k-form-item>
        <k-form-item label="登记日期">
          <k-field-date v-model="formData.registerDate" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="TA_ID">
          <k-field-text v-model="formData.taId"/>
        </k-form-item>
        <k-form-item label="备注">
          <k-field-text v-model="formData.remark" :data-max-length="256"/>
        </k-form-item>
	      	<k-form-footer slot="footer" data-align="center">
		        <k-btn class="btn-custom-primary" ref="sumbitadd" :data-handler="sumbit_add"  data-from="addTrCustRegisterInfoForm"
		               :data-model="formData" data-target="trCustRegisterInfoGrid"  >
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改投资者信息登记弹出框   -->
	<k-popup ref="editTrCustRegisterInfoPopup" data-title="修改"  @data-opened="editOpened()">
	  <k-form ref="editTrCustRegisterInfoForm" :data-col="2" isFormBodyScreen>
	    <k-form-item label="Id" v-if="false">
        <k-field-text v-model="formData.id" />
      </k-form-item>
      <k-form-item label="数据日期" :class="[handleItemDiff('reportDate')]">
        <k-field-date v-model="formData.reportDate" data-date-format="yyyyMMdd" :data-disabled="true" data-value-format="yyyyMMdd" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="登记机构代码" :class="[handleItemDiff('bankCode'),getDiffClass('bankCode')]">
	      	<k-field-text v-model="formData.bankCode" :data-allowblank="false" :data-disabled="true" :data-max-length="6"/>
	    </k-form-item>
			<k-form-item label="该投资者是否属于本机构" :class="[handleItemDiff('isBelong'),getDiffClass('isBelong')]">
	      	<k-field-select v-model="formData.isBelong"  data-dict="isTrue" :data-disabled="formData.isBelongDisabled"/>
	    </k-form-item>
			<k-form-item label="投资者所属机构名称" :class="[handleItemDiff('issBankName'),getDiffClass('issBankName')]">
	      	<k-field-text v-model="formData.issBankName" :data-max-length="60" :data-disabled="formData.issBankNameDisabled"/>
	    </k-form-item>
			<k-form-item label="投资者所属机构代码" :class="[handleItemDiff('issBankCode'),getDiffClass('issBankCode')]">
	      	<k-field-text v-model="formData.issBankCode" :data-max-length="6"  :data-disabled="formData.issBankCodeDisabled"/>
	    </k-form-item>
			<k-form-item label="投资者境内外标识" :class="[handleItemDiff('inOutSign'),getDiffClass('inOutSign')]">
	      	<k-field-select v-model="formData.inOutSign" data-dict="tr_in_out_sign"   @data-on-change="dataOnChange" :data-disabled="formData.inOutSignDisabled"/>
	    </k-form-item>
			<k-form-item label="投资者所属国家或地区" :class="[handleItemDiff('issCountry'),getDiffClass('issCountry')]">
	      	<k-field-select v-model="formData.issCountry" data-dict="tr_iss_country"/>
	    </k-form-item>
      <k-form-item label="数据类型" :class="[handleItemDiff('dataType'),getDiffClass('dataType')]">
        <k-field-select v-model="formData.dataType" :data-allowblank="false" :data-disabled="true" data-dict="tr_data_type"  @data-on-change="dataOnChange"/>
      </k-form-item>
      <k-form-item label="原识别标识" v-if="this.formData.dataType == 2" :class="[handleItemDiff('oriCustNo'),getDiffClass('oriCustNo')]">
        <k-field-text v-model="formData.oriCustNo" :data-allowblank="false"  :data-max-length="30"/>
      </k-form-item>
      <k-form-item label="识别标识" :class="[handleItemDiff('custNo'),getDiffClass('custNo')]">
        <k-field-text v-model="formData.custNo" :data-allowblank="false" :data-disabled="true" :data-max-length="30"/>
      </k-form-item>
			<k-form-item label="投资者类别" :class="[handleItemDiff('custType'),getDiffClass('custType')]">
	      	<k-field-select v-model="formData.custType" data-dict="tr_cust_type"  :data-allowblank="formData.custTypeAllowblank"   @data-on-change="dataOnChange"  :data-disabled="formData.custTypeDisabled"/>
	    </k-form-item>
			<k-form-item label="个人证件类别" :class="[handleItemDiff('personalIdType'),getDiffClass('personalIdType')]">
	      	<k-field-select v-model="formData.personalIdType" :data-data="formData.addPersonalIdTypeDict" :data-allowblank="formData.personalIdTypeAllowblank" :data-disabled="formData.personalIdTypeDisabled"   data-value-field="VALUE" data-display-field="TEXT"  @data-on-change="dataOnChange"/>
	    </k-form-item>
			<k-form-item label="机构证件类别" :class="[handleItemDiff('organizationIdType'),getDiffClass('organizationIdType')]">
	      	<k-field-select v-model="formData.organizationIdType" data-dict="tr_organization_id_type" :data-allowblank="formData.organizationIdTypeAllowblank" :data-disabled="formData.organizationIdTypeDisabled"   @data-on-change="dataOnChange"/>
	    </k-form-item>
			<k-form-item label="其他证件名称" :class="[handleItemDiff('otherIdName'),getDiffClass('otherIdName')]">
	      	<k-field-text v-model="formData.otherIdName" :data-allowblank="formData.otherIdNameAllowblank" :data-disabled="formData.otherIdNameDisabled" :data-max-length="60"/>
	    </k-form-item>
			<k-form-item label="证件号码" :class="[handleItemDiff('idCodeDisplay'),getDiffClass('idCodeDisplay')]">
	      	<k-field-text v-model="formData.idCodeDisplay" :data-max-length="32"/>
	    </k-form-item>
			<k-form-item label="SPV资金托管账户开户行" :class="[handleItemDiff('spvOpenBank'),getDiffClass('spvOpenBank')]">
	      	<k-field-select v-model="formData.spvOpenBank" data-dict="subm_tr_spv_open_bank" />
	    </k-form-item>
			<k-form-item label="其他资金托管账户开户行" :class="[handleItemDiff('otherOpenBank'),getDiffClass('otherOpenBank')]">
	      	<k-field-text v-model="formData.otherOpenBank" :data-max-length="60"/>
	    </k-form-item>
			<k-form-item label="投资者名称" :class="[handleItemDiff('custNameDisplay'),getDiffClass('custNameDisplay')]">
	      	<k-field-text v-model="formData.custNameDisplay" :data-max-length="32"/>
	    </k-form-item>
			<k-form-item label="性别" :class="[handleItemDiff('sex'),getDiffClass('sex')]">
	      	<k-field-select v-model="formData.sex" data-dict="subm_tr_sex"/>
	    </k-form-item>
			<k-form-item label="风险偏好" :class="[handleItemDiff('riskLevel'),getDiffClass('riskLevel')]">
	      	<k-field-select v-model="formData.riskLevel" data-dict="subm_investor_risk_preference"/>
	    </k-form-item>
			<k-form-item label="手机号码" :class="[handleItemDiff('mobleDisplay'),getDiffClass('mobleDisplay')]">
	      	<k-field-text v-model="formData.mobleDisplay" data-validate-type="telephone1" :data-max-length="11"/>
	    </k-form-item>
			<k-form-item label="固定电话" :class="[handleItemDiff('telPhoneDisplay'),getDiffClass('telPhoneDisplay')]">
        <k-field-text v-model="formData.telPhoneDisplay" :data-max-length="30"/>
      </k-form-item>
			<k-form-item label="电子邮箱" :class="[handleItemDiff('emailDisplay'),getDiffClass('emailDisplay')]">
        <k-field-text v-model="formData.emailDisplay" data-validate-type="email" :data-max-length="50"/>
      </k-form-item>
    <!--  <k-form-item label="登记日期" :class="[handleItemDiff('registerDate')]">
        <k-field-date v-model="formData.registerDate" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
      </k-form-item> -->
      <k-form-item label="TA_ID" :class="[handleItemDiff('taId'),getDiffClass('taId')]">
        <k-field-text :data-disabled="true" v-model="formData.taId"/>
      </k-form-item>
			<k-form-item label="备注" :class="[handleItemDiff('remark')]">
        <k-field-text v-model="formData.remark" :data-max-length="256"/>
      </k-form-item>

	    <k-form-footer slot="footer" data-align="center">
	      <k-btn class="btn-custom-primary" ref="sumbitedit" :data-handler="sumbit_edit"   data-from="editTrCustRegisterInfoForm"
	        :data-model="formData" data-target="trCustRegisterInfoGrid"   :handle-before="handleBeforeUpdate">
	        <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
	      </k-btn>
	      <k-btn class="btn-custom-plain" data-functype="CLOSE">
        <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
        </k-btn>
	    </k-form-footer>
	  </k-form>
	</k-popup>

    <k-popup ref="uploadImportTrCustRegisterInfoPopup" title="报送数据导入" @data-opened="uploadOpened()">
        <k-form ref="addForm" data-ui="element">
          <k-form-item label="数据日期">
            <k-field-date v-model="formData.reportDate" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd" :data-allowblank="false"/>
          </k-form-item>
<!--          <k-form-item label="理论报送起始日期">-->
<!--            <k-field-date v-model="uploadBeginDate"  data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd" :data-allowblank="false"/>-->
<!--          </k-form-item>-->
          <!--<k-form-item label="至">
            <k-field-date v-model="uploadQueryDate"  data-date-format="yyyyMMdd" data-value-format="yyyyMMdd" :data-allowblank="false"/>
          </k-form-item>-->
          <k-form-item label="报送数据导入" data-ui="element" data-input-width="500px">
            <k-field-excel-upload data-type="file" ref="uploadRefA" :data-multiple="false" :data-limit='1'
                data-accept=".xlsx,.xls"
                :data-error="onSubmitErrorA" :data-success="onSubmitSuccessA"
                :data-auto-upload="false"
                data-upload-url="upload/server/RptApp/reportManage/custRegistImport.json">
            </k-field-excel-upload>
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="trCustRegisterInfoGrid" ref="submitBtn"
                  :data-auto-upload="false" data-from="addForm" :data-handler="submitImportUploadParam">确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
          </k-form-footer>
        </k-form>
        </k-popup>

     <k-popup ref="uploadUpdateTrCustRegisterInfoPopup" title="批量修改导入" @data-opened="uploadOpened()">
        <k-form ref="addForm" data-ui="element">
          <k-form-item label="数据日期">
            <k-field-date v-model="formData.reportDate" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd" :data-allowblank="false"/>
          </k-form-item>
<!--          <k-form-item label="理论报送起始日期">-->
<!--            <k-field-date v-model="uploadBeginDate"  data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd" :data-allowblank="false"/>-->
<!--          </k-form-item>-->
          <!--<k-form-item label="至">
            <k-field-date v-model="uploadQueryDate"  data-date-format="yyyyMMdd" data-value-format="yyyyMMdd" :data-allowblank="false"/>
          </k-form-item>-->
          <k-form-item label="批量修改导入" data-ui="element" data-input-width="500px">
            <k-field-excel-upload data-type="file" ref="uploadRefB" :data-multiple="false" :data-limit='1'
                data-accept=".xlsx,.xls"
                :data-error="onSubmitErrorB" :data-success="onSubmitSuccessB"
                :data-auto-upload="false"
                data-upload-url="upload/server/RptApp/reportManage/custRegistUpdateImport.json">
            </k-field-excel-upload>
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="trCustRegisterInfoGrid" ref="submitBtn"
                  :data-auto-upload="false" data-from="addForm" :data-handler="submitUpdateUploadParam">确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
          </k-form-footer>
        </k-form>
        </k-popup>

    <k-popup ref="exportTrCustRegisterInfoPopup" data-title="报送数据导出" @data-opened="exportOpened()">
        <k-form data-ui="element">
          <k-form-item label="数据日期">
				    <k-field-date v-model="reportDateRange" :data-allowblank="false"
                 data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
			    </k-form-item>

          <k-form-item label="个人证件类别">
          <k-field-select v-model="exportParam.personalIdType" data-dict="tr_personal_id_type"/>
        </k-form-item>

        <k-form-item label="机构证件类别">
          <k-field-select v-model="exportParam.organizationIdType" data-dict="tr_organization_id_type"/>
        </k-form-item>

        <k-form-item label="数据类型">
          <k-field-select v-model="exportParam.dataType" data-dict="tr_data_type"/>
        </k-form-item>

        <k-form-item label="识别标识">
          <k-field-text v-model="exportParam.custNo"/>
        </k-form-item>

        <k-form-item label="TA_ID">
          <k-field-text v-model="exportParam.taId"/>
        </k-form-item>

        <k-form-item label="投资者类别">
          <k-field-select v-model="exportParam.custType" data-dict="tr_cust_type"/>
        </k-form-item>

        <k-form-item label="证件号码">
          <k-field-text v-model="exportParam.idCode" data-validate-type="text"/>
        </k-form-item>

        <k-form-item label="报送状态">
          <k-field-select v-model="exportParam.registerStatus" data-dict="subm_report_status"/>
        </k-form-item>

          <k-form-footer data-align="center">
            <k-btn ref="exportBtnRef" class="btn-custom-primary"  data-functype="EXPORT" data-target="trCustRegisterInfoGrid"
                  data-action="TrCustRegisterInfo.download" :data-export-name="'投资者身份信息登记管理'" :report-date="exportParam.reportDates" :data-handler="handleConfirmExport">
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
import Tools from "@/utils/tools";
import ErroComp from "@/pages/zz/manage/validateInfo";
import { validIdCard } from "@/utils/index.js"
export default {
  name: "TrCustRegisterInfo",
  components: {ErroComp},
  data() {
    return {
      formData: {
        addPersonalIdTypeDict: ''
      },
      formDataCopy: {},
      oldData: {},
      queryParam: {},
      selectRowData: {},
      addPersonalIdTypeDict: {},
      queryParamDateRange: [],
      beforeDate: '',
      exportParam: {},
      reportDateRange: [],
      uploadBeginDate: '',
      uploadQueryDate: '',

      infoPop: {},
      auditInfoPopupData: {
        tableId: 'app_cust_register_info',
        tableName: '投资者身份信息登记管理'
      },
      dialogType: ''
    };
  },
  watch: {
    queryParamDateRange() {
      this.$set(this.queryParam, 'queryStartDate', this.queryParamDateRange == null ? '' : this.queryParamDateRange[0]);
      this.$set(this.queryParam, 'queryEndDate', this.queryParamDateRange == null ? '' : this.queryParamDateRange[1]);
    },
    reportDateRange() {
      this.$set(this.exportParam, 'reportStartDate', this.reportDateRange == null ? null : this.reportDateRange[0]);
      this.$set(this.exportParam, 'reportEndDate', this.reportDateRange == null ? null : this.reportDateRange[1]);
      this.$set(this.exportParam, 'reportDates', this.reportDateRange[0]+'-'+this.reportDateRange[1]);
    }
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
    handleBeforeUpdate() {
      if (this.formNoChangeCb()) {
        this.$refs.editTrCustRegisterInfoPopup.close();
        return false
      }
      return true
    },
    sumbit_edit(){
        if(this.formData.dataType=='02'){
            if (this.formData.oriCustNo != null && this.formData.oriCustNo != "" && this.formData.oriCustNo != undefined && this.formData.custNo != null && this.formData.custNo != "" && this.formData.custNo != undefined) {
              if(this.formData.oriCustNo == this.formData.custNo){
                  Tools.alert("识别标识和原识别标识不能重复！","danger");
                  return false
              }
            }
        }
        if (this.dialogType == 'add') {
          if (['00', '01'].includes(this.formData.personalIdType) && this.formData.idCode && !validIdCard(this.formData.idCode)) {
            Tools.alertTime( "“居民身份证”或“临时居民身份证”的证件号码输入有误，请修改！", "danger", 5000);
            return false
          }
        } else if (this.dialogType == 'edit') {
           if (['00', '01'].includes(this.formData.personalIdType) && this.formData.idCodeDisplay && !validIdCard(this.formData.idCodeDisplay)) {
            Tools.alertTime( "“居民身份证”或“临时居民身份证”的证件号码输入有误，请修改！", "danger", 5000);
            return false
          }
        }
        this.$refs.sumbitedit.setIconStyle(0,[]);
        if(this.$refs.editTrCustRegisterInfoForm.validate()){
              this.httpUtil.query({
                      url: 'server/json/RptApp/audit/checkTrCustRegisterInfo.json',
                      params:  this.formData
                               }).then(res => {
                                 if(res.success) {
                                  this.httpUtil.comnUpdate({
                                           action: 'TrCustRegisterInfo.updateTrCustRegisterInfo',
                                           params:  this.formData
                                            }).then(res => {
                                             if(res.success) {
                                             this.$refs.editTrCustRegisterInfoPopup.close();
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
       if(this.formData.dataType=='02'){
                  if (this.formData.oriCustNo != null && this.formData.oriCustNo != "" && this.formData.oriCustNo != undefined && this.formData.custNo != null && this.formData.custNo != "" && this.formData.custNo != undefined) {
                    if(this.formData.oriCustNo == this.formData.custNo){
                        Tools.alert("识别标识和原识别标识不能重复！","danger");
                        return false
                    }
                  }
              }
      if (this.dialogType == 'add') {
        if (['00', '01'].includes(this.formData.personalIdType) && this.formData.idCode && !validIdCard(this.formData.idCode)) {
          Tools.alertTime( "“居民身份证”或“临时居民身份证”的证件号码输入有误，请修改！", "danger", 5000);
          return false
        }
      } else if (this.dialogType == 'edit') {
         if (['00', '01'].includes(this.formData.personalIdType) && this.formData.idCodeDisplay && !validIdCard(this.formData.idCodeDisplay)) {
          Tools.alertTime( "“居民身份证”或“临时居民身份证”的证件号码输入有误，请修改！", "danger", 5000);
          return false
        }
      }
       this.$refs.sumbitadd.setIconStyle(0,[]);
        if(this.$refs.addTrCustRegisterInfoForm.validate()){
              this.httpUtil.query({
                      url: 'server/json/RptApp/audit/checkTrCustRegisterInfo.json',
                      params:  this.formData
                               }).then(res => {
                                 if(res.success) {
                                  this.httpUtil.comnUpdate({
                                           action: 'TrCustRegisterInfo.addTrCustRegisterInfo',
                                           params:  this.formData
                                            }).then(res => {
                                             if(res.success) {
                                             this.$refs.addTrCustRegisterInfoPopup.close();
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
                     this.$refs.trCustRegisterInfoGrid.load(this.queryParam);
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
                 this.$refs.trCustRegisterInfoGrid.load(this.queryParam);
                 this.$refs.auditInfoPopup.close();
                }
              })
           }
         }
       })
    },
    handleAddBtn() {
      this.formData = {}
      this.dialogType = 'add'
    },
    handleUpdateBtn() {
      this.dialogType = 'edit'
    },
    handleData(row) {
      row.map(sr => {
        sr.custName = this.sensitiveNameHandle(sr.custName);//处理行内名称敏感数据规则
        sr.idCode = this.sensitiveIdCodeHandle(sr.idCode);//处理行内证件号敏感数据规则
        sr.moble = this.sensitiveMobileHandle(sr.moble);//处理行内手机号敏感数据规则
        sr.telPhone = this.sensitiveFixedPhoneHandle(sr.telPhone);//处理行内固定电话敏感数据规则
        sr.email = this.sensitiveEmailHandle(sr.email);//处理行内电子邮箱敏感数据规则
      })
      return row;
    },
		handleTaskApp() {
			this.$refs.reReportRef.handleReports(this.formData.reportDate);
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
    //行内规则-身份证号码：屏蔽后6位
    sensitiveIdCodeHandle(idCode){
      if(idCode !== null) {
        const length = idCode.length;
        if(length <= 6) {
          return '*'.repeat(length);
        }
        const left = idCode.slice(0, length-6);
        return left + '*'.repeat(6);
      }
    },
    //行内规则-手机号：隐藏中间4位
    sensitiveMobileHandle(mobile){
      if(mobile !== null){
        const length = mobile.length;
        if(length < 4){
          return mobile;
        }
        const left = mobile.slice(0, 3);
        const right = mobile.slice(7, length);
        return left + '*'.repeat(4) + right;
      }
    },
    //行内规则-固定电话：显示前3位
    sensitiveFixedPhoneHandle(telPhone){
      if(telPhone !== null){
        const length = telPhone.length;
        if(length < 3){
          return telPhone;
        }
        const left = telPhone.slice(0, 3);
        return left + '*'.repeat(length - 3);
      }
    },
    //行内规则-电子邮箱：@前面的字符显示前3位(因监管报送系统对上游系统送空的电子邮箱置11@11.com,可只显示前1位),后面显示3个*,@后面完整显示
    sensitiveEmailHandle(email){
      if(email !== null){
        if(email == '11@11.com'){//特殊处理空邮箱11@11.com
          return '1***@11.com';
        }
        const parts = email.split('@');//邮箱拆分前后两部分
        const left = parts[0].slice(0, 3);
        return left + '*'.repeat(3) + parts[1];
      }
    },
    popupEdit(row) {
      let pathUrl = '/main/zz/errorInfo/CustRegisterInfoErr';
      this.$router.push({
        path: pathUrl,
        query: {
          registerSerno: row.registerSerno,
        },
      });
    },
     onSubmitSuccessA() {
      this.$refs.trCustRegisterInfoGrid.load(this.queryParam);
      this.$refs.uploadBtnRefA.setIconStyle(1);
    },
    onSubmitErrorA() {
      this.$refs.uploadRefA.doReset();
      this.$refs.uploadBtnRefA.setIconStyle(1);
    },
    onSubmitSuccessB() {
      this.$refs.trCustRegisterInfoGrid.load(this.queryParam);
      this.$refs.uploadBtnRefB.setIconStyle(1);
    },
    onSubmitErrorB() {
      this.$refs.uploadRefB.doReset();
      this.$refs.uploadBtnRefB.setIconStyle(1);
    },
    submitImportUploadParam() {
      //文件上传校验
      var validate = this.$refs.addForm.validate();
      if (validate) {
        let formData = {reportDate: this.formData.reportDate};
        let temp = document.getElementsByClassName('upload-demo');
        let lis = temp[0].childNodes[1].childNodes.length;
        if (lis > 0) {
          //this.$refs.uploadBtnRefA.setIconStyle(0);
          this.$refs.uploadRefA.upload(formData);
          setTimeout(()=>{
            this.$refs.uploadImportTrCustRegisterInfoPopup.close();
          }, 300)
        } else {
          Tools.alert("上传文件不能为空!", "danger");
          return false;
        }
      }
    },
    submitUpdateUploadParam() {
      //文件上传校验
      var validate = this.$refs.addForm.validate();
      if (validate) {
        let formData = {reportDate: this.formData.reportDate};
        let temp = document.getElementsByClassName('upload-demo');
        let lis = temp[0].childNodes[1].childNodes.length;
        if (lis > 0) {
          //this.$refs.uploadBtnRefB.setIconStyle(0);
          this.$refs.uploadRefB.upload(formData);
          setTimeout(()=>{
            this.$refs.uploadUpdateTrCustRegisterInfoPopup.close();
          }, 300)
        } else {
          Tools.alert("上传文件不能为空!", "danger");
          return false;
        }
      }
    },

      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
        this.formDataCopy = Object.assign({}, row)
      },
      dataOnChange(){
          //个人证件类别
          if(this.formData.inOutSign == '01' || this.formData.inOutSign == '02'){
             if(this.formData.custType == '01' || this.formData.custType == '02' || this.formData.custType == '03'){
                 this.formData.personalIdTypeAllowblank=false;
                 this.formData.personalIdTypeDisabled = false;
                 this.getPersonalIdTypeDict();
             }else{
                 this.formData.personalIdTypeAllowblank=true;
                 this.formData.personalIdTypeDisabled = true;
                 this.$set(this.formData, 'personalIdType', '');
             }
             //机构证件类别
              if(this.formData.dataType == '01' || this.formData.dataType == '03'){
                 if(this.formData.custType == '04' || this.formData.custType == '05' || this.formData.custType == '06' || this.formData.custType == '07' || this.formData.custType == '08' || this.formData.custType == '09'
                     || this.formData.custType == '10'  || this.formData.custType == '11' || this.formData.custType == '12' || this.formData.custType == '13' || this.formData.custType == '14' || this.formData.custType == '15'
                     || this.formData.custType == '16' || this.formData.custType == '17' || this.formData.custType == '18' || this.formData.custType == '19' || this.formData.custType == '20' || this.formData.custType == '21'
                     || this.formData.custType == '22' || this.formData.custType == '23' || this.formData.custType == '24' || this.formData.custType == '25' || this.formData.custType == '26'){
                        this.formData.organizationIdTypeAllowblank=false;
                        this.formData.organizationIdTypeDisabled = false;
                 }else{
                        this.formData.organizationIdTypeAllowblank=true;
                        this.formData.organizationIdTypeDisabled = true;
                        this.$set(this.formData, 'organizationIdType', '');
                 }
              }else{
                    this.formData.organizationIdTypeAllowblank=true;
                    this.formData.organizationIdTypeDisabled = true;
                    this.$set(this.formData, 'organizationIdType', '');
              }

             //其他证件名称
             if(this.formData.custType == '01' || this.formData.custType == '02' || this.formData.custType == '03'){
                 if(this.formData.personalIdType == '99'){
                    this.formData.otherIdNameAllowblank=false;
                    this.formData.otherIdNameDisabled = false;
                 }else{
                      this.formData.otherIdNameAllowblank=true;
                      this.formData.otherIdNameDisabled = true;
                      this.$set(this.formData, 'otherIdName', '');
                 }

             }
             if(this.formData.custType == '04' || this.formData.custType == '05' || this.formData.custType == '06' || this.formData.custType == '07' || this.formData.custType == '08' || this.formData.custType == '09'
                                   || this.formData.custType == '10'  || this.formData.custType == '11' || this.formData.custType == '12' || this.formData.custType == '13' || this.formData.custType == '14' || this.formData.custType == '15'
                                   || this.formData.custType == '16'  || this.formData.custType == '24' || this.formData.custType == '25' || this.formData.custType == '26'){
                   if(this.formData.organizationIdType == '99'){
                       this.formData.otherIdNameAllowblank=false;
                       this.formData.otherIdNameDisabled = false;
                   }else{
                        this.formData.otherIdNameAllowblank=true;
                        this.formData.otherIdNameDisabled = true;
                        this.$set(this.formData, 'otherIdName', '');
                   }
               }
              if(this.formData.custType == '17' || this.formData.custType == '18' || this.formData.custType == '19' || this.formData.custType == '20' || this.formData.custType == '21'
                 || this.formData.custType == '22' || this.formData.custType == '23'){
                    this.formData.otherIdNameAllowblank=true;
                    this.formData.otherIdNameDisabled = true;
                    this.$set(this.formData, 'otherIdName', '');
               }
         }
    },
    uploadOpened() {
      //this.formData.reportDate = ''
    },
    exportOpened() {
      if (this.queryParam.queryStartDate && this.queryParam.queryEndDate) {
        this.reportDateRange = [this.queryParam.queryStartDate, this.queryParam.queryEndDate];
        this.$set(this.exportParam, 'reportStartDate', this.queryParam.queryStartDate);
        this.$set(this.exportParam, 'reportEndDate', this.queryParam.queryEndDate);
        this.$set(this.exportParam, 'reportDates', this.queryParam.queryStartDate+'-'+this.queryParam.queryEndDate);
      } else {
        this.reportDateRange = [this.beforeDate, this.beforeDate];
        this.$set(this.exportParam, 'reportStartDate', this.beforeDate);
        this.$set(this.exportParam, 'reportEndDate', this.beforeDate);
        this.$set(this.exportParam, 'reportDates', this.beforeDate);
      }
      this.$set(this.exportParam, 'personalIdType', this.queryParam.personalIdType);
      this.$set(this.exportParam, 'organizationIdType', this.queryParam.organizationIdType);
      this.$set(this.exportParam, 'dataType', this.queryParam.dataType);
      this.$set(this.exportParam, 'custNo', this.queryParam.custNo);
      this.$set(this.exportParam, 'taId', this.queryParam.taId);
      this.$set(this.exportParam, 'custType', this.queryParam.custType);
      this.$set(this.exportParam, 'idCode', this.queryParam.idCode);
      this.$set(this.exportParam, 'registerStatus', this.queryParam.registerStatus);
    },
    editOpened(){
      this.httpUtil.comnQuery({
        action: "InvestorBaseInfo.queryInvestorBaseInfo",
        params: {
          taId: this.formData.taId,
          dealDate: this.formData.reportDate
        }
      }).then(data => {
        this.oldData = data;
      }).catch({})
      this.dataOnChange();
      this.getPersonalIdTypeDict();
      this.formDataCopy = Object.assign({}, this.formData)
    },
    addOpened(){
      this.getPersonalIdTypeDict();
    },
     getPersonalIdTypeDict(){
                 this.$set(this.formData, 'addPersonalIdTypeDict', '');
                 this.httpUtil.comnQuery({
                    action: "TrCustRegisterInfo.getPersonalIdTypeDict",
                    params: {inOutSign: this.formData.inOutSign}
                  }).then(data => {
                    this.formData.addPersonalIdTypeDict = data.rows;
                    this.formDataCopy.addPersonalIdTypeDict = data.rows;
                  }).catch({})
              },
      submitParam(){
        if(this.formData.dataType=='02'){
            if (this.formData.oriCustNo != null && this.formData.oriCustNo != "" && this.formData.oriCustNo != undefined && this.formData.custNo != null && this.formData.custNo != "" && this.formData.custNo != undefined) {
              if(this.formData.oriCustNo == this.formData.custNo){
                  Tools.alert("识别标识和原识别标识不能重复！","danger");
                  return false
              }
            }
        }
        if (this.dialogType == 'add') {
          if (['00', '01'].includes(this.formData.personalIdType) && this.formData.idCode && !validIdCard(this.formData.idCode)) {
            Tools.alertTime( "“居民身份证”或“临时居民身份证”的证件号码输入有误，请修改！", "danger", 5000);
            return false
          }
        } else if (this.dialogType == 'edit') {
           if (['00', '01'].includes(this.formData.personalIdType) && this.formData.idCodeDisplay && !validIdCard(this.formData.idCodeDisplay)) {
            Tools.alertTime( "“居民身份证”或“临时居民身份证”的证件号码输入有误，请修改！", "danger", 5000);
            return false
          }
        }
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
        this.$refs.trCustRegisterInfoGrid.load(this.queryParam);
      },
      getDiffClass(key) {
        var result = "";
        var old = this.oldData.rows;

        if (this.formData.dataType == '03' && old != null && old.length>0) {
          var a = this.formData[key] != null ? this.formData[key] : "";
          var b = old[0][key] != null ? old[0][key] : "";
          result = a != b ? "value-diff" : "";
      }

		  return result;
	    },
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
      this.queryParamDateRange = [defaultDate, defaultDate];
      this.$set(this.queryParam, 'queryStartDate', defaultDate);
      this.$set(this.queryParam, 'queryEndDate', defaultDate);
    },

  };
</script>
