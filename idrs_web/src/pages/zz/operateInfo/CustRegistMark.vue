<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="CustRegistMark" data-label-width="90px" data-target="custRegistMarkGrid" v-model = "searchParam">
        <k-form-item label="操作日期">
          <k-field-date v-model="BreathDay" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="操作人员">
          <k-field-text v-model="searchParam.summitUser"/>
        </k-form-item>
        <k-form-item label="操作类型">
          <k-field-select v-model="searchParam.opType"  data-dict="op_type"/>
        </k-form-item>
        <k-form-item label="投资者名称">
          <k-field-text v-model="searchParam.custName"/>
        </k-form-item>
        <k-form-item label="证件号码">
          <k-field-text v-model="searchParam.idCode"/>
        </k-form-item>
        <k-form-item label="识别标识">
          <k-field-text v-model="searchParam.custNo"/>
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <k-grid ref="custRegistMarkGrid" @data-row-select="selectRow" data-operate-column="false" data-action="CustRegistMark.findCustRegistMarks" >
        <k-grid-column data-align="left" data-header="操作用户" data-name="summitUser" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="操作日期" data-name="createDate" data-type="date" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="操作时间" data-name="createTime" data-type="time" data-width="80"></k-grid-column>
        <k-grid-column data-align="left" data-header="操作类型" data-name="opType" data-dict="op_type" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="登记银行代码" data-name="bankCode"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="该投资者是否属于本机构" data-name="isBelong" data-dict="subm_isTrue" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="投资者所属机构名称" data-name="issBankName" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="投资者所属机构代码" data-name="issBankCode" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="投资者境内外标识" data-name="inOutSign" data-dict="tr_in_out_sign" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="投资者所属国家或地区" data-name="issCountry" data-dict="tr_iss_country" data-width="120" ></k-grid-column>
        <k-grid-column data-align="left" data-header="数据类型" data-name="dataType" data-dict="tr_data_type" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="原识别标识" data-name="oriCustNo" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="识别标识" data-name="custNo" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="投资者类别" data-name="custType" data-dict="tr_cust_type" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="个人证件类别" data-name="personalIdType" data-dict="tr_personal_id_type" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="机构证件类别" data-name="organizationIdType" data-dict="tr_organization_id_type" data-width="220"></k-grid-column>
        <k-grid-column data-align="left" data-header="其他证件名称" data-name="otherIdName" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="证件号码" data-name="idCode" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="SPV资金托管账户开户行" data-name="spvOpenBank" data-dict="subm_tr_spv_open_bank" data-width="250"></k-grid-column>
        <k-grid-column data-align="left" data-header="其他资金托管账户开户行" data-name="otherOpenBank" data-width="250"></k-grid-column>
        <k-grid-column data-align="left" data-header="投资者名称" data-name="custName" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="性别" data-name="sex" data-dict="subm_tr_sex" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="风险偏好" data-name="riskLevel" data-dict="subm_investor_risk_preference" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="手机号码" data-name="moble" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="固定电话" data-name="telPhone" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="电子邮箱" data-name="email" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="备注" data-name="remark" data-width="120"></k-grid-column>
      </k-grid>
    </div>


  </div>
</template>

<script>
  export default {
    name: "custRegistMark",
    data() {
      return {
        formData: {},
        selectRowData: {},
        searchParam:{},
        BreathDay:[]
      };
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      }
    },
    watch: {
      //查询起息日
      BreathDay() {
        console.log(this.BreathDay);
        this.$set(this.searchParam, 'startDate', this.BreathDay == null ? '' : this.BreathDay[0]);
        this.$set(this.searchParam, 'endDate', this.BreathDay == null ? '' : this.BreathDay[1]);
      },
    }

  };
</script>
