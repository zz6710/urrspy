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
      <k-form-item label="产品到期日">
        <k-field-date v-model="prodSearchParam.endDate"></k-field-date>
      </k-form-item>
      <k-form-item label="产品状态">
        <k-field-select v-model="prodSearchParam.prodStatus" data-dict="t8_prod_status"></k-field-select>
      </k-form-item>
    </k-form-search-customize>

    <k-grid
      ref="taCustodianBankGrid"
      data-action='T8ProdExpiration.findT8ProdExpirations1'
      @data-row-select="selectRow"
      data-operate-column-position="end"
      data-operate-width="200px">
      <k-grid-column data-header="产品代码" data-name="prodCode" :dataMaxLength="32"></k-grid-column>
      <k-grid-column data-header="产品名称" data-name="prodName"></k-grid-column>
<!--      <k-grid-column data-header="会计版本" data-name="accountVersion" data-dict="accountversion"></k-grid-column>-->
      <k-grid-column data-header="产品形态" data-name="prodMode" data-dict="t8_prod_create_type"></k-grid-column>
      <k-grid-column data-header="产品状态" data-name="prodStatus"  data-dict="t8_prod_status" ></k-grid-column>
      <k-grid-column data-header="产品实际年化收益率" data-name="actualAnnualizedYield"></k-grid-column>
      <k-grid-column data-header="预设到期日" data-name="endDate" data-type="date"></k-grid-column>
      <k-grid-column data-header="清盘日期" data-name="liquidate" data-type="date" data-date-format="yyyy-MM-dd"></k-grid-column>
      <k-grid-column data-header="实际终止日期" data-name="prodRealCloseDate" data-type="date"></k-grid-column>
      <k-grid-column data-header="产品总资产" data-name="totalAssets"></k-grid-column>
      <k-grid-column data-header="审批状态" data-name="checkState" data-hidden="true"></k-grid-column>
      <template slot="operate" slot-scope="scope">
        <k-btn data-functype="POPUP" data-confirm data-size="mini" :data-disabled="scope.row.row.prodStatus==='12'"
               class="md-info md-just-icon md-simple" v-if="global.getProdIfUser(scope.row.row.prodId)&&
               global.isShowAuthorityButton('T8ProdExpiration.addT8ProdExpiration')"
               data-target="addLiquidationPopup" @click="setEndDate(scope.row.row)" data-descript="清盘方案" v-show="showLiquidation">
          <md-icon>add</md-icon>
        </k-btn>
        <k-btn data-functype="POPUP" data-confirm data-size="mini" :data-disabled="scope.row.row.prodStatus==='12'"
               class="md-info md-just-icon md-simple" v-if="global.getProdIfUser(scope.row.row.prodId)&&
               global.isShowAuthorityButton('T8ProdExpiration.addOrUpdateT8ProdExpiration')"
               data-target="editTaCustodianBankPopup" :data-handler="checkLiquidation" data-descript="产品到期" v-show="showDeadline">
          <md-icon>edit</md-icon>
        </k-btn>
        <k-btn data-functype="POPUP" data-confirm data-size="mini" class="md-info md-just-icon md-simple" data-descript="延期"
               @click="setDelyDate(scope.row.row)"  data-target="addDelyPopup"  v-if="global.getProdIfUser(scope.row.row.prodId)&&
               global.isShowAuthorityButton('T8ProdExpiration.t8ProdDelay')"
               v-show="showDelayDeadline">
          <md-icon>edit_attributes</md-icon>
        </k-btn>
        <k-btn data-functype="POPUP" data-confirm data-size="mini" class="md-info md-just-icon md-simple" data-descript="详情"
               data-target="taCustodianBankPopupInfo" v-if="global.getProdIfUser(scope.row.row.prodId)" >
          <md-icon>library_books</md-icon>
        </k-btn>

      </template>
    </k-grid>

    <!--    清盘方案弹出框   -->
    <k-popup ref="addLiquidationPopup" data-title="清盘方案">
      <k-form ref="addLiquidationForm" :data-col="2">
        <k-form-item label="产品代码">
          <k-field-text v-model="dateFormData.prodCode"  :data-disabled="true" /></k-form-item>
        <k-grid-column data-hidden="true" v-model="dateFormData.prodId" />
        <k-form-item label="产品名称">
          <k-field-text v-model="dateFormData.prodName" :data-disabled="true" /></k-form-item>
        <k-form-item label="产品清盘日">
          <k-field-date v-model="dateFormData.liquidate" :dataAllowblank="false" data-type="date" data-date-format="yyyy-MM-dd"/></k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdExpiration.addT8ProdExpiration"
                 data-from="addLiquidationForm" :data-model="dateFormData"
                 data-target="taCustodianBankGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>关闭</k-btn>
        </k-form-footer>

      </k-form>
    </k-popup>

      <!--    产品延期  -->
    <k-popup ref="addDelyPopup" data-title="产品延期">
      <k-form ref="addDelyForm" :data-col="2">
        <k-form-item label="产品代码">
          <k-field-text v-model="delyFormData.prodCode"  :data-disabled="true" />
        </k-form-item>
        <k-form-item label="产品id" v-show="false">
          <k-field-text v-model="delyFormData.prodId"  />
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="delyFormData.prodName" :data-disabled="true" />
        </k-form-item>
        <k-form-item label="产品原到期日">
          <k-field-date v-model="delyFormData.oldEndDate" :dataAllowblank="false" :data-disabled="true"
                        data-type="date" data-date-format="yyyy-MM-dd"/>
        </k-form-item>
        <k-form-item label="调整后到期日">
          <k-field-date v-model="delyFormData.endDate" :dataAllowblank="false"
                        data-type="date" data-date-format="yyyy-MM-dd"/>
        </k-form-item>
         <k-form-item label="产品调整日">
          <k-field-date v-model="delyFormData.adjustDate" :dataAllowblank="false" :data-disabled="true"
                        data-type="date" data-date-format="yyyy-MM-dd"/>
         </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdExpiration.t8ProdDelay"
                 data-from="addDelyForm" :data-model="delyFormData"
                 data-target="taCustodianBankGrid" :data-handler="validform">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>关闭</k-btn>
        </k-form-footer>

      </k-form>
    </k-popup>

    <!--    产品到期弹出框   -->
    <k-popup ref="editTaCustodianBankPopup" data-title="产品到期">
      <k-form ref="editTaCustodianBankForm" :data-col="2" dataLabelWidth="190px" >
        <k-form-item label="产品代码">
          <k-field-display v-model="formData.prodCode"  />
        </k-form-item>
        <k-form-item label="产品id" v-show="false">
          <k-field-text v-model="formData.prodId"  />
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-display v-model="formData.prodName" :dataAllowblank="false"  />
        </k-form-item>
        <k-form-item label="产品到期日">
          <k-field-display v-model="formData.endDate" :dataAllowblank="false"  data-type="date" />
        </k-form-item>
        <k-form-item label="产品清盘日">
          <k-field-display v-model="formData.liquidate" :dataAllowblank="false"  data-type="date" />
        </k-form-item>
        <k-form-item label="登记编码">
          <k-field-display v-model="formData.registrationCode"/>
        </k-form-item>

        <k-form-item label="发行机构代码">
          <k-field-display v-model="formData.organizationCode" :dataAllowblank="false"  />
        </k-form-item>
        <k-form-item label="产品总资产">
          <k-field-text v-model="formData.totalAssets" :dataAllowblank="false" data-digits="2"  data-integer-length="14"
                        data-min-value="0" data-validate-type="money" data-show-gbmoney="true"  data-type="money" data-placeholder="单位(元)"/>
        </k-form-item>
        <k-form-item label="实际终止日期">
           <k-field-date v-model="formData.prodRealCloseDate" :dataAllowblank="false" data-type="date" />
        </k-form-item>
        <k-form-item label="银行实际实现收入(元)">
          <k-field-text v-model="formData.bankActualIncome" :dataAllowblank="false" data-digits="2"  data-integer-length="14"
                        data-min-value="0" data-validate-type="money" data-show-gbmoney="true" data-type="money" data-placeholder="单位(元)"/>
        </k-form-item>
        <k-form-item label="兑付客户收益(元)">
          <k-field-text v-model="formData.cashCustomerIncome" :dataAllowblank="false" data-digits="2"  data-integer-length="14"
                        data-min-value="0" data-validate-type="money"  data-show-gbmoney="true" data-type="money" data-placeholder="单位(元)"/>
        </k-form-item>
        <k-form-item label="兑付客户总金额(元)">
          <k-field-text v-model="formData.cashCustomerTotalAmount" :dataAllowblank="false" data-digits="2"  data-integer-length="14"
                        data-min-value="0" data-validate-type="money"  data-show-gbmoney="true" data-type="money" data-placeholder="单位(元)"/>
        </k-form-item>
        <k-form-item label="兑付总份额(元)">
          <k-field-text v-model="formData.cashTotalAmount" :dataAllowblank="false" data-digits="5"  data-integer-length="13"
                        data-min-value="0" data-validate-type="money"  data-show-gbmoney="true" data-type="money" data-placeholder="单位(元)"/></k-form-item>
        <k-form-item label="本机构托管费(元)">
          <k-field-text v-model="formData.trusteeshipFee" :dataAllowblank="false" data-digits="2" data-integer-length="13"
                        data-min-value="0" data-validate-type="money"  data-show-gbmoney="true" data-type="money" data-placeholder="单位(元)"/>
        </k-form-item>
        <k-form-item label="本机构管理费(元)">
          <k-field-text v-model="formData.managementFee" :dataAllowblank="false" data-digits="2" data-integer-length="13"
                        data-min-value="0" data-validate-type="money"  data-show-gbmoney="true" data-type="money" data-placeholder="单位(元)"/>
        </k-form-item>
        <k-form-item label="本机构销售手续费(元)">
          <k-field-text v-model="formData.salesServiceCharge" :dataAllowblank="false" data-digits="2" data-integer-length="13"
                        data-min-value="0" data-validate-type="money"  data-show-gbmoney="true" data-type="money" data-placeholder="单位(元)"/>
        </k-form-item>
        <k-form-item label="本机构其他产品费用(元)">
          <k-field-text v-model="formData.organizeOhterFee" :dataAllowblank="false" data-digits="2" data-integer-length="13"
                        data-min-value="0" data-validate-type="money"  data-show-gbmoney="true" data-type="money" data-placeholder="单位(元)"/>
        </k-form-item>
        <k-form-item label="其他机构托管费(元)">
          <k-field-text v-model="formData.otherInstitutionsTrusteeshipFee" :dataAllowblank="false" data-digits="2" data-integer-length="13"
                        data-min-value="0" data-validate-type="money"  data-show-gbmoney="true" data-type="money" data-placeholder="单位(元)"/>
        </k-form-item>
        <k-form-item label="其他机构管理费(元)">
          <k-field-text v-model="formData.otherInstitutionsManagementFee" :dataAllowblank="false" data-digits="2" data-integer-length="13"
                        data-min-value="0" data-validate-type="money"  data-show-gbmoney="true" data-type="money" data-placeholder="单位(元)"/>
        </k-form-item>
        <k-form-item label="其他机构销售手续费(元)">
          <k-field-text v-model="formData.otherInstitutionsSalesServiceCharge" :dataAllowblank="false" data-digits="2" data-integer-length="13"
                        data-min-value="0" data-validate-type="money"  data-show-gbmoney="true" data-type="money" data-placeholder="单位(元)"/>
        </k-form-item>
        <k-form-item label="其他机构其他产品费(元)">
          <k-field-text v-model="formData.otherInstitutionOtherFee" :dataAllowblank="false" data-digits="2" data-integer-length="13"
                        data-min-value="0" data-validate-type="money"  data-show-gbmoney="true"  data-type="money" data-placeholder="单位(元)"/>
        </k-form-item>
        <k-form-item label="投资顾问费用(元)">
          <k-field-text v-model="formData.investmentConsultantFee" :dataAllowblank="false" data-digits="2" data-integer-length="13"
                        data-min-value="0" data-validate-type="money"  data-show-gbmoney="true" data-type="money" data-placeholder="单位(元)"/>
        </k-form-item>
        <k-form-item label="客户实际年化收益率(%)">
         <k-field-text v-model="formData.actualAnnualizedRate" :dataAllowblank="false" data-type="number"
                       data-digits="5" data-integer-length="3" data-validate-type="number"/>
        </k-form-item>
         <k-form-item label="产品实际年化收益率(%)">
         <k-field-text v-model="formData.actualAnnualizedYield" :dataAllowblank="false" data-type="number" data-validate-type="number"
                       data-digits="5" data-integer-length="3"/>
         </k-form-item>

        <k-form-footer data-align="center">
           <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdExpiration.addOrUpdateT8ProdExpiration"
                 data-from="editTaCustodianBankForm" :data-model="formData"
                 data-target="taCustodianBankGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>关闭</k-btn>
        </k-form-footer>

      </k-form>
    </k-popup>


    <k-popup ref="taCustodianBankPopupInfo" data-title="产品到期详情">
      <k-form ref="taCustodianBankFormInfo" :data-col="2" dataLabelWidth="190px">

        <k-form-item label="产品代码">
          <k-field-display v-model="formData.prodCode"   />
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-display v-model="formData.prodName"  />
        </k-form-item>

        <k-form-item label="产品模型">
          <k-field-display v-model="formData.prodMode" data-dict="t8_prod_mode" />
        </k-form-item>
        <k-form-item label="产品状态">
          <k-field-display v-model="formData.prodStatus" data-dict="t8_prod_status"  data-type="date"/>
        </k-form-item>
        <k-form-item label="产品到期日">
          <k-field-display v-model="formData.endDate" data-type="date" data-date-format="yyyy-MM-dd"/>
        </k-form-item>
        <k-form-item label="产品清盘日">
          <k-field-display v-model="formData.liquidate"  data-type="date" data-date-format="yyyy-MM-dd"/>
        </k-form-item>
        <k-form-item label="产品总资产">
          <k-field-display v-model="formData.totalAssets" data-digits="2" data-integer-length="16"
                           data-validate-type="money" data-type="money"/>
        </k-form-item>
        <k-form-item label="登记编码">
          <k-field-display v-model="formData.registrationCode"/>
        </k-form-item>

        <k-form-item label="机构代码">
          <k-field-display v-model="formData.organizationCode" />
        </k-form-item>
        <k-form-item label="实际终止日期">
          <k-field-display v-model="formData.prodRealCloseDate" data-type="date"/>
        </k-form-item>
        <k-form-item label="银行实际实现收入(元)">
          <k-field-display v-model="formData.bankActualIncome"  data-digits="2" data-integer-length="14"
                           data-validate-type="money" data-type="money"/>
        </k-form-item>
        <k-form-item label="兑付客户收益(元)">
          <k-field-display v-model="formData.cashCustomerIncome"  data-digits="2" data-integer-length="14"
                           data-validate-type="money" data-type="money"/>
        </k-form-item>
        <k-form-item label="兑付客户总金额(元)">
          <k-field-display v-model="formData.cashCustomerTotalAmount"  data-digits="5" data-integer-length="13"
                           data-validate-type="money" data-type="money"/>
        </k-form-item>
        <k-form-item label="兑付总份额(元)">
          <k-field-display v-model="formData.cashTotalAmount" data-digits="2" data-integer-length="13"
                           data-validate-type="money" data-type="money"/>
        </k-form-item>
        <k-form-item label="本机构托管费(元)">
          <k-field-display v-model="formData.trusteeshipFee" data-digits="2" data-integer-length="13"
                           data-validate-type="money" data-type="money"/>
        </k-form-item>
        <k-form-item label="本机构管理费(元)">
          <k-field-display v-model="formData.managementFee"  data-digits="2" data-integer-length="13"
                           data-validate-type="money" data-type="money"/>
        </k-form-item>
        <k-form-item label="本机构销售手续费(元)">
          <k-field-display v-model="formData.salesServiceCharge" data-digits="2" data-integer-length="13"
                           data-validate-type="money" data-type="money"/>
        </k-form-item>
        <k-form-item label="本机构其他产品费用(元)">
          <k-field-display v-model="formData.organizeOhterFee" data-digits="2" data-integer-length="13"
                           data-validate-type="money" data-type="money"/>
        </k-form-item>
        <k-form-item label="其他机构托管费(元)">
          <k-field-display v-model="formData.otherInstitutionsTrusteeshipFee" data-digits="2" data-integer-length="13"
                           data-validate-type="money" data-type="money"/>
        </k-form-item>

        <k-form-item label="其他机构管理费(元)">
          <k-field-display v-model="formData.otherInstitutionsManagementFee" data-digits="2" data-integer-length="13"
                           data-validate-type="money" data-type="money"/>
        </k-form-item>
        <k-form-item label="其他机构销售手续费(元)">
          <k-field-display v-model="formData.otherInstitutionsSalesServiceCharge"  data-digits="2" data-integer-length="13"
                           data-validate-type="money" data-type="money"/>
        </k-form-item>
        <k-form-item label="其他机构其他产品费(元)">
          <k-field-display v-model="formData.otherInstitutionOtherFee" data-digits="2" data-integer-length="13"
                        data-validate-type="money" data-type="money"/>
        </k-form-item>
        <k-form-item label="投资顾问费用(元)">
          <k-field-display v-model="formData.investmentConsultantFee" data-digits="2" data-integer-length="13"
                           data-validate-type="money" data-type="money"/>
        </k-form-item>

        <k-form-item label="客户实际年化收益率(%)">
         <k-field-display v-model="formData.actualAnnualizedRate" data-type="number"
                          data-digits="5" data-integer-length="3" data-validate-type="number"/>
        </k-form-item>

         <k-form-item label="产品实际年化收益率(%)">
         <k-field-display v-model="formData.actualAnnualizedYield" data-type="number"
                          data-digits="5" data-integer-length="3" data-validate-type="number"/>
         </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>关闭</k-btn>
        </k-form-footer>

      </k-form>
    </k-popup>


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
      prodEndDate:'',
      prodCode:'',
      showLiquidation:true,//是否显示清盘方案按钮
      showDeadline:true,//是否显示到期按钮
      showDelayDeadline:true,//是否显示延期按钮
    };
  },

  methods: {
    checkLiquidation(value){
      if(value.liquidate==''||value.liquidate==""||value.liquidate==null||value.liquidate==undefined){
        Tools.alert("请先添加清盘方案!","danger");
        return false;
      }
      return true;
    },
    setEndDate(row){
      if(row.liquidate==""||row.liquidate==null){
        this.dateFormData.liquidate = row.endDate;
      }else{
      this.dateFormData.liquidate = row.liquidate;
      }
      this.dateFormData.prodId = row.prodId;
       this.dateFormData.prodCode = row.prodCode;
       this.dateFormData.prodName = row.prodName;

    },
     setDelyDate(row){

       this.delyFormData.oldEndDate = row.endDate;
       this.delyFormData.prodId = row.prodId;
       this.delyFormData.prodCode = row.prodCode;
       this.delyFormData.prodName = row.prodName;
        this.delyFormData.adjustDate=  new Date().Format("yyyyMMdd");

    },
    changeNetCash(cashShare,cashAmount){
      this.formData.netCash = cashAmount/cashShare;
      this.formData.cashIncome =cashAmount-cashShare;
      //TODO
    },
    changeCashAmount(netCash,cashShare){
      this.formData.cashAmount = netCash*cashShare;
    },

    validform(value){
      if(value.oldEndDate>value.endDate){
        Tools.alert("调整后日期不能小于当前到期日");
        return false;
      }else{
        return true;
      }


    },
    selectRow(row, column, event) {
      this.selectRowData = assign({}, row)
      this.formData = assign({}, row)
    },
    dataBeforeLoad() {
      return {"excOrgno":"ROOT"}
    },
    statusRender(row) {
      console.log("statusRender=======>", row)
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
  },
  mounted(){
    if(this.prodCode!=''){
      this.$refs.taCustodianBankGrid.load({'prodCode':this.prodCode});
    }
  },
  created() {
    this.prodCode = this.$route.query.prodCode;
    this.$nextTick(()=>{
      //获取需要隐藏按钮属性  rennannan 20210322   将接收到的属性值置为false达到隐藏按钮的效果
      this.global.getHideButtons(this);
      let prodCode = this.$route.query.prodCode;
      if(prodCode){
        this.$refs.taCustodianBankGrid.load({prodCode:prodCode});
      }
    });
    //console.log(this.$route.query.prodCode);

  },
};
</script>
