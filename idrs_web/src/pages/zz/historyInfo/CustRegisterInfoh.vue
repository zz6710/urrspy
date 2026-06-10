<template>
  <div class="py-page">
      <div>
           <k-form-search-customize data-model-name="CustRegisterInfoh" data-target="CustRegisterInfohGrid" data-label-width="100px" v-model = "searchParam">
              <k-form-item label="数据日期">
                <k-field-date v-model="searchParam.reportDate" :data-allowblank="false" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
              </k-form-item>

               <k-form-item label="识别标识">
                <k-field-text v-model="searchParam.custNo"/>
              </k-form-item>

              <k-form-item label="个人证件类别">
                <k-field-select v-model="searchParam.personalIdType" data-dict="tr_personal_id_type"/>
              </k-form-item>

              <k-form-item label="机构证件类别">
                <k-field-select v-model="searchParam.organizationIdType" data-dict="tr_organization_id_type"/>
              </k-form-item>

              <k-form-item label="数据类型">
                <k-field-select v-model="searchParam.dataType" data-dict="tr_data_type"/>
              </k-form-item>

              <k-form-item label="投资者类别">
                <k-field-select v-model="searchParam.custType" data-dict="tr_cust_type"/>
              </k-form-item>

              <k-form-item label="证件号码">
                <k-field-text v-model="searchParam.idCode" data-validate-type="text"/>
              </k-form-item>
           </k-form-search-customize>
      </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn slot="button"  class="btn-custom-plain"  data-functype="EXPORT" data-target="CustRegisterInfohGrid"
            data-action="CustRegisterInfoh.historyDownload" :data-export-name="'投资者身份信息登记历史数据管理'" 
            :report-date="searchParam.reportDate" :cust-no="searchParam.custNo">
            <md-icon>cloud_download</md-icon>
            导出
          </k-btn>
        </div>
      </div>

      <k-grid ref="CustRegisterInfohGrid" @data-row-select="selectRow"  data-operate-column="false" data-action="CustRegisterInfoh.findCustRegisterInfos"
              :data-autoload="false" :handleDataFun="handleData" >
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
    	    <k-grid-column data-align="left" data-header="登记日期" data-name="registerDate" data-export="false" data-width="120"></k-grid-column>
    	    <k-grid-column data-align="left" data-header="报送状态" data-name="registerStatus"  data-dict="subm_report_status" data-export="false"  data-width="120"></k-grid-column>
      </k-grid>
    </div>
  </div>
</template>

<script>
export default {
  name: "CustRegisterInfoh",
  data() {
    return {
      formData: {},
      selectRowData: {},
      searchParam:{},
      RegisterDate:[]
    };
  },
  methods: {
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
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
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

