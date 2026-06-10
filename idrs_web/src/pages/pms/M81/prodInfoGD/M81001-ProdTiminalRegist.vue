<template>
  <div>
    <k-form-search-customize data-target="taCustodianBankGrid" v-model="prodSearchParam">

     <k-form-item label="产品代码">
          <k-field-select v-model="prodSearchParam.prodCode" data-action="T8Dict.findTaProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode"/>
        </k-form-item>
      <k-form-item label="产品名称">
        <k-field-text v-model="prodSearchParam.prodName" data-validate-type="text"/>
      </k-form-item>

        <k-btn slot="button" class="md-rose" data-functype="EXPORT" data-target="taCustodianBankGrid"
               :data-export-name="'产品终止登记'">
          <md-icon>cloud_download</md-icon>导出
        </k-btn>
    </k-form-search-customize>

    <k-grid
      ref="taCustodianBankGrid"
      data-action='T8ProdTerminal.findT8ProdTerminalRegist1'
      @data-row-select="selectRow"
      data-operate-column-position="end"
      data-operate-width="200px" data-operate-column=false>
      <k-grid-column data-header="产品代码" data-name="prodCode"></k-grid-column>
      <k-grid-column data-header="产品名称" data-name="prodName"></k-grid-column>
      <!-- <k-grid-column data-header="清盘日期" data-name="liquidate"></k-grid-column>
      <k-grid-column data-header="产品总资产" data-name="totalAssets"></k-grid-column> -->
      <k-grid-column data-header="登记编码" data-name="registrationCode"></k-grid-column>
      <k-grid-column data-header="机构代码" data-name="organizationCode"></k-grid-column>
      <k-grid-column data-header="理财产品实际终止日期" data-name="prodRealCloseDate"></k-grid-column>
      <k-grid-column data-header="银行实际实现收(元)" data-name="bankActualIncome"></k-grid-column>
      <k-grid-column data-header="兑付客户收益(元)" data-name="cashCustomerIncome"></k-grid-column>
      <k-grid-column data-header="兑付客户总金额(元)" data-name="cashCustomerTotalAmount"></k-grid-column>
      <k-grid-column data-header="兑付总份额(元)" data-name="cashTotalAmount"></k-grid-column>
      <k-grid-column data-header="本机构托管费(元)" data-name="trusteeshipFee"></k-grid-column>
      <k-grid-column data-header="本机构管理费(元)" data-name="managementFee"></k-grid-column>
      <k-grid-column data-header="本机构销售手续费(元)" data-name="salesServiceCharge"></k-grid-column>
      <k-grid-column data-header="本机构其他产品费用(元)" data-name="organizeOhterFee"></k-grid-column>
      <k-grid-column data-header="其他机构托管费(元)" data-name="otherInstitutionsTrusteeshipFee"></k-grid-column>
      <k-grid-column data-header="其他机构管理费(元)" data-name="otherInstitutionsManagementFee"></k-grid-column>
      <k-grid-column data-header="其他机构销售手续费(元)" data-name="otherInstitutionsSalesServiceCharge"></k-grid-column>
      <k-grid-column data-header="其他机构其他管理费(元)" data-name="otherInstitutionOtherFee"></k-grid-column>
      <k-grid-column data-header="投资顾问费用(元)" data-name="investmentConsultantFee"></k-grid-column>
      <k-grid-column data-header="客户实际年化收益率(%)" data-name="actualAnnualizedRate"></k-grid-column>
      <k-grid-column data-header="产品实际年化收益率(%)" data-name="actualAnnualizedYield"></k-grid-column>
      <!-- <template slot="operate" slot-scope="scope">
             <k-btn class="md-info md-just-icon md-simple" :data-disabled="scope.row.row.status=='0'" :data-download-name="'产品终止登记.xls'"
               data-descript="导出产品终止登记信息" data-functype="DOWNLOAD" data-size="small"
               data-url="/download/server/PmsApp/terminalRegist/download.json" v-model="scope.row.row">
               <md-icon>cloud_download</md-icon>
       </k-btn>


      </template> -->
    </k-grid>



  </div>
</template>

<script>
import { assign } from "lodash";
import Tools from "@/utils/tools";


export default {
  data() {
    return {
      prodSearchParam: {
        prodCode: '',//产品代码
         //到期日
         //产品状态
      },
      formData: {
      },
      dateFormData: {
        liquidate:''
      },
      delyFormData: {

      },
      cascaderValue: [],
      selectRowData: {},
      prodEndDate:''
    };
  },

  methods: {

    selectRow(row, column, event) {
      this.selectRowData = assign({}, row)
      this.formData = assign({}, row)
    },
    dataBeforeLoad() {
      return {"excOrgno":"ROOT"}
    },
    updSuccess(pop) {
      this.$refs.taCustodianBankGrid.load()
      pop.close()
    }
  },
  computed: {
    value() {
      return this.$attrs.value
    }
  }
};
</script>
