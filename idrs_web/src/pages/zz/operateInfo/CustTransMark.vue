<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="CustTransMark" data-target="custTransMarkGrid"  v-model = "searchParam">
        <k-form-item label="操作日期">
          <k-field-date v-model="BreathDay" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="操作人员">
          <k-field-text v-model="searchParam.summitUser"/>
        </k-form-item>
        <k-form-item label="操作类型">
          <k-field-select v-model="searchParam.opType"  data-dict="op_type"/>
        </k-form-item>
        <k-form-item label="产品登记编码">
          <k-field-text v-model="searchParam.prodCode"/>
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <k-grid ref="custTransMarkGrid" @data-row-select="selectRow" data-operate-column="false" data-action="CustTransMark.findCustTransMarks" >
        <k-grid-column data-align="left" data-header="操作用户" data-name="summitUser" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="操作日期" data-name="createDate" data-type="date" data-sortable="true" data-default-sort="DESC"  data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="操作时间" data-name="createTime" data-type="time"  data-sortable="true" data-default-sort="DESC" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="操作类型" data-name="opType" data-dict="op_type" data-width="80"></k-grid-column>
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
      </k-grid>
    </div>




  </div>
</template>

<script>
  export default {
    name: "custTransMark",
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
