<template>
  <div>
    <div>
      <k-form-search-customize data-target="t8ProdDeclaraGrid" v-model="queryParam">

        <k-form-item label="产品代码">
          <k-field-select v-model="prodSearchParam.t8ProdInfoId" data-action="T8Dict.findTaProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="t8ProdInfoId"/>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="prodSearchParam.prodName" data-validate-type="text"/>
        </k-form-item>
        <k-form-item label="募集起始日期">
          <k-field-date v-model="queryParamDateRange" data-type="daterange" />
        </k-form-item>
        <k-btn slot="button" class="md-rose" data-functype="EXPORT" data-target="t8ProdDeclaraGrid"
               :data-export-name="'产品申报登记'">
          <md-icon>cloud_download</md-icon>导出
        </k-btn>
      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="t8ProdDeclaraGrid" @data-row-select="selectRow" data-action="T8ProdDeclara.findT8ProdDec1" >
        <k-grid-column data-header="产品名称" data-name="prodName" data-width="120"></k-grid-column>
        <k-grid-column data-header="行内标识码" data-name="internalIdentCode" data-width="100"></k-grid-column>
        <k-grid-column data-header="发行机构代码" data-name="issuerCode" data-width="100"></k-grid-column>
        <k-grid-column data-header="产品审批人姓名" data-name="approverName" data-width="120"></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="产品审批人身份证号" data-name="approverIdcardNo" data-width="200"></k-grid-column>
        <k-grid-column data-header="产品设计人姓名" data-name="designerName" data-width="120"></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="产品设计人身份证号" data-name="designerIdcardNo"></k-grid-column>
        <k-grid-column data-header="投资经理名称" data-name="investManageName" data-width="120"></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="投资经理身份证号" data-name="investManageIdcardNo"></k-grid-column>
        <k-grid-column data-header="业务联系人姓名" data-name="businessContactName" data-width="120"></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="业务联系座机号" data-name="businessContactLandline"></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="业务联系人手机号" data-name="businessContactPhone"></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="业务联络人邮箱" data-name="businessContactEmail"></k-grid-column>
        <k-grid-column data-header="产品收益类型" data-name="revenueType"  data-width="100" ></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="新老产品标记" data-name="isOldProd" ></k-grid-column>
        <k-grid-column data-header="产品期限" data-name="prodTerm"  data-width="120"></k-grid-column>
        <k-grid-column data-header="是否金融同业专属" data-name="isFinancialIndustry"  data-width="150"></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="资金投向地区" data-name="investRegion" ></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="产品投资国家或地区（境外）" data-name="investRegionOutside"></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="理财业务服务模式" data-name="financialServiceMode" ></k-grid-column>
        <k-grid-column data-header="产品运作模式" data-name="productOperationMode" data-width="120"></k-grid-column>
        <k-grid-column data-header="产品募集方式" data-name="prodRaiseMethod"  data-width="100"></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="产品资产配置方式" data-name="prodAssetAllocation"  data-width="150"></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="产品管理模式" data-name="prodManageMode" data-width="100"></k-grid-column>
        <k-grid-column data-header="实际管理人名称" data-name="actualManagerName" data-width="120"></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="产品定价方式" data-name="prodPriceWay" data-width="100"></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="产品投资性质" data-name="prodInvestNature"  data-width="100"></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="业绩比较基准" data-name="performanceBenchmark"></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="是否设置最短持有期限" data-name="isMinHoldPeriod"></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="最短持有期限（天）" data-name="minHoldPeriod"></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="最短持有期后是否自由赎回" data-name="isFreeRedemption"  ></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="是否现金管理类" data-name="isCashManagement"  ></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="产品销售区域" data-name="prodSalesArea"  ></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="起点销售金额" data-name="startSalesAmount"></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="募集币种" data-name="raisedCurrency" ></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="兑付本金币种" data-name="cashGoldCoin" ></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="兑付收益币种" data-name="currencyCashIncome" ></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="销售手续费率%" data-name="salesServiceRate"></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="托管费率%" data-name="escrowRate"></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="募集起始日期（从）" data-name="raiseDateStart"  data-width="100"></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="募集结束日期（到）" data-name="raiseDateEnd"  data-width="100" ></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="计划募集金额（元）" data-name="planFundRaiseAmount"></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="境内托管机构名称" data-name="domesticTrusteeName" ></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="境内托管机构代码" data-name="domesticTrusteeCode"></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="境外托管机构国别" data-name="countryOverseasTrusteeshipInstitution"></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="境外托管机构名称" data-name="overseasTrusteeshipInstitutionName"></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="投资者风险偏好" data-name="investorRiskPreference" ></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="产品风险等级" data-name="prodRiskLevel" ></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="发行机构提前终止权标识" data-name="earlyTerminationFlag" ></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="客户赎回权标识" data-name="customerRedemptionFlag" ></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="产品品牌" data-name="prodBrand"></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="产品期次" data-name="prodPeriod"></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="投资管理费率%" data-name="investManageRate"></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="合作模式" data-name="cooperationMode" ></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="合作机构名称" data-name="cooperateOrganizationName"></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="投资本金到账日" data-name="investPrincipalArriveDate" ></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="投资金收益到账日" data-name="investIncomeArriveDate" ></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="产品增信标识" data-name="prodCreditLogo" ></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="产品增信机构类型" data-name="prodCreditType" ></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="产品增信形式" data-name="prodCreditForm" ></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="投资资产种类及比例" data-name="investAssetTypeProportion"></k-grid-column>
        <k-grid-column :data-hidden="true" data-header="备注" data-name="remarks"></k-grid-column>
        <template slot="operate" slot-scope="scope">
        <!--    去除用户组权限      -->
        <!--     v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)"     -->
          <k-btn class="md-info md-just-icon md-simple" data-descript="产品申报要素详情" data-functype="POPUP" data-size="mini"
                 data-target="editT8ProdDeclaraPopup"  >
            <md-icon>library_books</md-icon>
          </k-btn>
          <!--          <k-btn class="md-info md-just-icon md-simple" :data-disabled="scope.row.row.status=='0'" :data-download-name="'产品申报登记.xls'"-->
          <!--               data-descript="导出产品申报" data-functype="DOWNLOAD" data-size="small"-->
          <!--               data-url="/download/server/PmsApp/declaration/downloadDeclarationInfo.json" v-model="scope.row.row">-->
          <!--          <md-icon>cloud_download</md-icon>-->
          <!--        </k-btn>-->
        </template>
      </k-grid>

    </div>


    <k-popup ref="editT8ProdDeclaraPopup" data-title="详情">
      <T8DeclarationInfo ref="declarationInfo" v-model="formData" :T8DeclarationInfo="formData"
                         :prodCode="formData.prodCode" />
    </k-popup>
  </div>
</template>

<script>
  import T8DeclarationInfo      from "../../M81/prodDisplay/DisplayDeclarationInfo.vue"
  export default {
    name: "declaration",
    components: {
      T8DeclarationInfo,
    },
    data() {
      return {
        formData: {},
        selectRowData: {},
        queryParamDateRange:[],
        prodSearchParam: {
          prodCode: ''
        },
      };
    },
    created() {
      this.$nextTick(()=>{
        this.global.getProdUser('');
        if (this.$route.query.t8_prod_info_id) {
          this.$refs.t8ProdDeclaraGrid.load({t8ProdInfoId: this.$route.query.t8_prod_info_id});
        }
      })

    },
    computed: {
      queryParam() {
        return {
          'prodName': this.prodSearchParam.prodName,
          't8ProdInfoId': this.prodSearchParam.t8ProdInfoId,
          'raiseDateStart': this.queryParamDateRange ? this.queryParamDateRange[0] : null,
          'raiseDateEnd': this.queryParamDateRange ? this.queryParamDateRange[1] : null,
        }
      }
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row);
        this.httpUtil.comnQuery({
          action: 'T8ProdDeclara.findT8ProdDeclaras',
          params: {
            t8ProdInfoId : this.selectRowData.t8ProdInfoId,
          }
        }).then(data => {
          if(data.rows.length> 0 ){
            this.formData = data.rows[0]?data.rows[0]:{};
          }
        });
      }
    }
  };
</script>
