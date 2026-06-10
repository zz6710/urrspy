<template>
	<div class="py-page">
		<div>
			<k-form-search-customize data-model-name="InvestorBaseInfo" v-model="searchParam" data-target="investorGrid" data-label-width="100px">
				<k-form-item label="识别标识">
					<k-field-text v-model="searchParam.custNo" />
				</k-form-item>
				<k-form-item label="TA_ID">
					<k-field-text v-model="searchParam.taId" />
				</k-form-item>
				<k-form-item label="投资者类别">
					<k-field-select v-model="searchParam.custType" data-dict="tr_cust_type" data-display-field="itemval" />
				</k-form-item>
				<k-form-item label="证件号码">
					<k-field-text v-model="searchParam.idCode" />
				</k-form-item>
				<k-form-item label="个人证件类别">
					<k-field-select v-model="searchParam.personalIdType" data-dict="tr_personal_id_type" data-display-field="itemval" />
				</k-form-item>
				<k-form-item label="机构证件类别">
					<k-field-select v-model="searchParam.organizationIdType" data-dict="tr_organization_id_type" data-display-field="itemval" />
				</k-form-item>
			</k-form-search-customize>
		</div>
		<div class="py-page-container">
			<div class="table-top-btns">
				<div class="left">
					<k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="handleAdd" data-target="addInvestorBaseInfoPopup">
						<md-icon md-src="/static/svg/add.svg" />新增
					</k-btn>
					<k-btn slot="button" class="btn-custom-plain" data-functype="EXPORT" data-target="investorGrid" data-export-name="全量投资者身份信息">
						<md-icon>cloud_download</md-icon>导出
					</k-btn>
				</div>
			</div>
			<k-grid ref="investorGrid" @data-row-select="selectRow" data-action="InvestorBaseInfo.queryInvestorBaseInfo" data-operate-width="110" data-fixed="right"
			        :data-autoload="false" :handleDataFun="handleData" >
				<k-grid-column data-header="id" data-name="主键" data-hidden="true" data-export="false" ></k-grid-column>
				<k-grid-column data-header="登记机构代码" data-name="bankCode" data-width="100"></k-grid-column>
				<k-grid-column data-header="识别标识" data-name="custNo" data-width="100"></k-grid-column>
				<k-grid-column data-header="内部识别标识" data-name="innerCustNo" data-width="100"></k-grid-column>
				<k-grid-column data-header="原识别标识" data-name="oriCustNo" data-width="100"></k-grid-column>
				<k-grid-column data-header="该投资者是否属于本行" data-name="isBelong" data-dict="tr_is_belong" data-width="150"></k-grid-column>
				<k-grid-column data-header="投资者所属银行名称" data-name="issBankName" data-width="150"></k-grid-column>
				<k-grid-column data-header="投资者所属银行代码" data-name="issBankCode" data-width="150"></k-grid-column>
				<k-grid-column data-header="投资者境内外标识" data-name="inOutSign" data-dict="tr_in_out_sign" data-width="150"></k-grid-column>
				<k-grid-column data-header="投资者所属国家或地区" data-name="issCountry" data-dict="tr_iss_country" data-width="160"></k-grid-column>
				<k-grid-column data-header="投资者类别" data-name="custType" data-dict="tr_cust_type" data-width="100"></k-grid-column>
				<k-grid-column data-header="个人证件类别" data-name="personalIdType" data-dict="tr_personal_id_type" data-width="100"></k-grid-column>
				<k-grid-column data-header="机构证件类别" data-name="organizationIdType" data-dict="tr_organization_id_type" data-width="100"></k-grid-column>
				<k-grid-column data-header="其他证件名称" data-name="otherIdName" data-width="100"></k-grid-column>
				<k-grid-column data-header="证件号码" data-name="idCode" data-width="100"></k-grid-column>
				<k-grid-column data-header="spv资金托管账户开户行" data-name="spvOpenBank" data-dict="subm_tr_spv_open_bank" data-width="160"></k-grid-column>
				<k-grid-column data-header="其他资金托管账户开户行" data-name="otherOpenBank" data-width="180"></k-grid-column>
				<k-grid-column data-header="投资者名称" data-name="custName" data-width="100"></k-grid-column>
				<k-grid-column data-header="性别" data-name="sex" data-dict="subm_tr_sex"></k-grid-column>
				<k-grid-column data-header="风险偏好" data-name="riskLevel" data-dict="subm_investor_risk_preference"></k-grid-column>
				<k-grid-column data-header="手机号码" data-name="moble" data-width="100"></k-grid-column>
				<k-grid-column data-header="固定电话" data-name="telPhone" data-width="100"></k-grid-column>
				<k-grid-column data-header="电子邮箱" data-name="email" data-width="100"></k-grid-column>
				<k-grid-column data-header="TA_ID" data-name="taId" data-width="100"></k-grid-column>
				<k-grid-column data-header="渠道号" data-name="channelCode"></k-grid-column>
				<k-grid-column data-header="合格投资者标识" data-name="custMark" data-dict="qualify_investor_type" data-width="120"></k-grid-column>
				<k-grid-column data-header="备注" data-name="remark" data-width="150"></k-grid-column>
				<!-- 以下为处理脱敏字段处理 -->
        <k-grid-column data-align="left" data-header="投资者名称" data-name="custNameDisplay" data-export="false" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="证件号码" data-name="idCodeDisplay" data-export="false" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="手机号码" data-name="mobleDisplay" data-export="false" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="固定电话" data-name="telPhoneDisplay" data-export="false" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="电子邮箱" data-name="emailDisplay" data-export="false" data-hidden="true"></k-grid-column>
				<template slot="operate" slot-scope="scope">
					<k-btn class="btn-custom-text"
					  data-functype="POPUP"
					  data-target="editInvestorBaseInfoPopup"> 修改
					</k-btn>
					<k-btn class="btn-custom-text"
					  data-descript="删除"
					  data-functype="SUBMIT"
					  data-action="InvestorBaseInfo.removeInvestorBaseInfo"
					  data-target="investorGrid"
					  :data-confirm="true"> 删除
					</k-btn>
				</template>
			</k-grid>
		</div>

		<!-- 新增投资者基本信息弹窗 -->
		<k-popup ref="addInvestorBaseInfoPopup" data-title="新增">
			<k-form ref="addInvestorBaseInfoForm" :data-col="2" data-label-width="170px">
				<k-form-item label="id" v-show="false">
					<k-field-text v-model="formData.id" />
				</k-form-item>
				<k-form-item label="登记机构代码">
					<k-field-text v-model="formData.bankCode" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="识别标识">
					<k-field-text v-model="formData.custNo" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="内部识别标识">
          <k-field-text v-model="formData.innerCustNo" :data-allowblank="false" />
        </k-form-item>
				<k-form-item label="原识别标识">
					<k-field-text v-model="formData.oriCustNo" />
				</k-form-item>
				<k-form-item label="该投资者是否属于本行">
					<k-field-select v-model="formData.isBelong" data-dict="tr_is_belong" data-display-field="itemval"/>
				</k-form-item>
				<k-form-item label="投资者所属银行名称">
					<k-field-text v-model="formData.issBankName" />
				</k-form-item>
				<k-form-item label="投资者所属银行代码">
					<k-field-text v-model="formData.issBankCode" />
				</k-form-item>
				<k-form-item label="投资者境内外标识">
					<k-field-select v-model="formData.inOutSign" data-dict="tr_in_out_sign" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="投资者所属国家或地区">
					<k-field-select v-model="formData.issCountry" data-dict="tr_iss_country" />
				</k-form-item>
				<k-form-item label="投资者类别">
					<k-field-select v-model="formData.custType" data-dict="tr_cust_type" :data-allowblank="false" @data-on-change="changeCustType" />
				</k-form-item>
				<k-form-item label="个人证件类别">
				  <k-field-select v-model="formData.personalIdType"  data-dict="tr_personal_id_type" />
				</k-form-item>
				<k-form-item label="机构证件类别">
					<k-field-select v-model="formData.organizationIdType" data-dict="tr_organization_id_type" />
				</k-form-item>
				<k-form-item label="其他证件名称">
				  <k-field-text v-model="formData.otherIdName" />
				</k-form-item>
				<k-form-item label="证件号码">
					<k-field-text v-model="formData.idCode" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="spv资金托管账户开户行">
					<k-field-select v-model="formData.spvOpenBank" data-dict="subm_tr_spv_open_bank" />
				</k-form-item>
				<k-form-item label="其他资金托管账户开户行">
				  <k-field-text v-model="formData.otherOpenBank" />
				</k-form-item>
				<k-form-item label="投资者名称">
					<k-field-text v-model="formData.custName" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="性别">
					<k-field-select v-model="formData.sex" data-dict="subm_tr_sex" :data-allowblank="!['01','02','03'].includes(formData.custType)"
						:data-disabled="!['01','02','03'].includes(formData.custType)" />
				</k-form-item>
				<k-form-item label="风险偏好">
					<k-field-select v-model="formData.riskLevel" data-dict="subm_investor_risk_preference" />
				</k-form-item>
				<k-form-item label="手机号码">
					<k-field-text v-model="formData.moble" data-validate-type="telephone1" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="固定电话">
					<k-field-text v-model="formData.telPhone" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="电子邮箱">
					<k-field-text v-model="formData.email" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="TA_ID">
					<k-field-text v-model="formData.taId" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="渠道号">
          <k-field-text v-model="formData.channelCode" :data-allowblank="false" />
        </k-form-item>
				<k-form-item label="合格投资者标识">
					<k-field-select v-model="formData.custMark" data-dict="qualify_investor_type" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="备注">
          <k-field-text v-model="formData.remark" />
        </k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="InvestorBaseInfo.putInvestorBaseInfo"
						data-from="addInvestorBaseInfoForm"
						:data-model="formData"
						data-target="investorGrid"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>

		<!--    修改资产负债分类配置表弹出框   -->
		<k-popup ref="editInvestorBaseInfoPopup" data-title="修改">
			<k-form ref="editInvestorBaseInfoForm" :data-col="2" data-label-width="170px">
				<k-form-item label="id" v-show="false">
					<k-field-text v-model="formData.id" />
				</k-form-item>
				<k-form-item label="登记机构代码" :class="[handleItemDiff('bankCode')]">
					<k-field-text v-model="formData.bankCode" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="识别标识" :class="[handleItemDiff('custNo')]">
					<k-field-text v-model="formData.custNo" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="原识别标识" :class="[handleItemDiff('oriCustNo')]">
					<k-field-text v-model="formData.oriCustNo" />
				</k-form-item>
				<k-form-item label="该投资者是否属于本行" :class="[handleItemDiff('isBelong')]">
					<k-field-select v-model="formData.isBelong" data-dict="tr_is_belong" />
				</k-form-item>
				<k-form-item label="投资者所属银行名称" :class="[handleItemDiff('issBankName')]">
					<k-field-text v-model="formData.issBankName" />
				</k-form-item>
				<k-form-item label="投资者所属银行代码" :class="[handleItemDiff('issBankCode')]">
					<k-field-text v-model="formData.issBankCode" />
				</k-form-item>
				<k-form-item label="投资者境内外标识" :class="[handleItemDiff('inOutSign')]">
					<k-field-select v-model="formData.inOutSign" data-dict="tr_in_out_sign" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="投资者所属国家或地区" :class="[handleItemDiff('issCountry')]">
					<k-field-select v-model="formData.issCountry" data-dict="tr_iss_country" />
				</k-form-item>
				<k-form-item label="投资者类别" :class="[handleItemDiff('custType')]">
					<k-field-select v-model="formData.custType" data-dict="tr_cust_type" :data-allowblank="false"  @data-on-change="changeCustType"/>
				</k-form-item>
				<k-form-item label="个人证件类别" :class="[handleItemDiff('personalIdType')]">
				  <k-field-select v-model="formData.personalIdType"  data-dict="tr_personal_id_type" />
				</k-form-item>
				<k-form-item label="机构证件类别" :class="[handleItemDiff('organizationIdType')]">
					<k-field-select v-model="formData.organizationIdType" data-dict="tr_organization_id_type" />
				</k-form-item>
				<k-form-item label="其他证件名称" :class="[handleItemDiff('otherIdName')]">
				  <k-field-text v-model="formData.otherIdName" />
				</k-form-item>
				<k-form-item label="证件号码" :class="[handleItemDiff('idCodeDisplay')]">
					<k-field-text v-model="formData.idCodeDisplay" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="spv资金托管账户开户行" :class="[handleItemDiff('spvOpenBank')]">
					<k-field-select v-model="formData.spvOpenBank" data-dict="subm_tr_spv_open_bank" />
				</k-form-item>
				<k-form-item label="其他资金托管账户开户行" :class="[handleItemDiff('otherOpenBank')]">
				  <k-field-text v-model="formData.otherOpenBank" />
				</k-form-item>
				<k-form-item label="投资者名称" :class="[handleItemDiff('custNameDisplay')]">
					<k-field-text v-model="formData.custNameDisplay" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="性别" :class="[handleItemDiff('sex')]">
					<k-field-select v-model="formData.sex" data-dict="subm_tr_sex" :data-allowblank="!['01','02','03'].includes(formData.custType)"
						:data-disabled="!['01','02','03'].includes(formData.custType)" />
				</k-form-item>
				<k-form-item label="风险偏好" :class="[handleItemDiff('riskLevel')]">
					<k-field-select v-model="formData.riskLevel" data-dict="subm_investor_risk_preference" />
				</k-form-item>
				<k-form-item label="渠道号" :class="[handleItemDiff('channelCode')]">
					<k-field-text v-model="formData.channelCode" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="手机号码" :class="[handleItemDiff('mobleDisplay')]">
					<k-field-text v-model="formData.mobleDisplay" data-validate-type="telephone1" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="固定电话" :class="[handleItemDiff('telPhoneDisplay')]">
					<k-field-text v-model="formData.telPhoneDisplay" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="电子邮箱" :class="[handleItemDiff('emailDisplay')]">
					<k-field-text v-model="formData.emailDisplay" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="备注" :class="[handleItemDiff('remark')]">
					<k-field-text v-model="formData.remark" />
				</k-form-item>
				<k-form-item label="TA_ID" :class="[handleItemDiff('taId')]">
					<k-field-text v-model="formData.taId" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="合格投资者标识" :class="[handleItemDiff('custMark')]">
					<k-field-select v-model="formData.custMark" data-dict="qualify_investor_type" :data-allowblank="false" />
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="InvestorBaseInfo.updateInvestorBaseInfo"
						data-from="editInvestorBaseInfoForm"
						:data-model="formData"
						data-target="investorGrid"
						:handle-before="handleBefore"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>
	</div>
</template>

<script>
export default {
name:"InvestorBaseInfo",
	data() {
		return {
			formData: {},
			formDataCopy: {},
			selectRowData: {},
			searchParam: {},
		};
	},
	methods: {
		handleBefore() {
			if (this.formNoChangeCb()) {
				this.$refs.editInvestorBaseInfoPopup.close();
				return false
			}
			return true
		},
		changeCustType() {
			if (!['01','02','03'].includes(this.formData.custType)) {
				this.formData.sex = ""
			}
		},
	  handleData (row) {
      row.map(sr => {
        sr.custName = this.sensitiveNameHandle(sr.custName);//处理行内名称敏感数据规则
        sr.idCode = this.sensitiveIdCodeHandle(sr.idCode);//处理行内证件号敏感数据规则
        sr.moble = this.sensitiveMobileHandle(sr.moble);//处理行内手机号敏感数据规则
        sr.telPhone = this.sensitiveFixedPhoneHandle(sr.telPhone);//处理行内固定电话敏感数据规则
        sr.email = this.sensitiveEmailHandle(sr.email);//处理行内电子邮箱敏感数据规则
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
		selectRow(row, column, event) {
			this.selectRowData = Object.assign({}, row);
			this.formData = Object.assign({}, row);
			this.formDataCopy = Object.assign({}, row);
		},
		handleAdd() {
			this.formData = {
				strtDt: this.getNowDate(),
				endDt: '20991231'
			}
		},
		getNowDate() {
			const timeOne = new Date();
			const year = timeOne.getFullYear();
			let month = timeOne.getMonth() + 1;
			let day = timeOne.getDate();
			month = month < 10 ? "0" + month : month;
			day = day < 10 ? "0" + day : day;
			const nowDate = year + "" + month + "" + day;
			return nowDate;
		},
		onSubmitSuccess() {
			this.$refs.uploadRef.doReset();
			this.$refs.addForm.reset();
			this.$refs.uploadInvestorBaseInfoPopup.close();
			this.$refs.investorGrid.load(this.searchParam);
		},
		onSubmitError() {
			this.$refs.uploadRef.doReset();
			this.$refs.submitBtn.setIconStyle(1, []);
		},
	},
};
</script>
