<template>

  <div>
    <k-form ref="addT8ProdDeclaraModelForm" :data-col="2" dataLabelWidth="170px" dataInputWidth="180px;">
      <k-form-item label="模板名称" :data-col="2">
        <k-field-text v-model="formData.modelName" :data-disabled="true"/>
      </k-form-item>


      <k-form-item label="产品审批人姓名">
        <k-field-select v-model="formData.approverName" data-action='T8ProdCustomerInfo.find'
                        :data-params="{'custType':'0'}" :data-disabled="true"
                        data-display-field="custName" data-value-field="custName" @data-on-change="checkChange"/>
      </k-form-item>
      <k-form-item label="产品审批人证件号">
        <k-field-text v-model="formData.approverIdcardNoTm" :data-max-length="32"   :data-disabled="true"/>
      </k-form-item>



      <k-form-item label="产品设计人姓名">
        <k-field-select v-model="formData.designerName"  data-action='T8ProdCustomerInfo.find'
                        :data-params="{'custType':'1',}" data-display-field="custName"
                        data-value-field="custName" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品设计人证件号">
        <k-field-text v-model="formData.designerIdcardNoTm"  :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="投资经理名称">
        <k-field-select v-model="formData.investManageName" data-action='T8ProdCustomerInfo.find' :data-disabled="true"
                        :data-params="{'custType':'2'}" data-display-field="custName" data-value-field="custName"
                       />
      </k-form-item>
      <k-form-item label="投资经理证件号">
        <k-field-text v-model="formData.investManageIdcardNoTm"  :data-disabled="true"/>
      </k-form-item>

      <k-form-item label="业务联系人姓名">
        <k-field-select v-model="formData.businessContactName" data-action='T8ProdCustomerInfo.find' :data-disabled="true"
                        :data-params="{'custType':'3'}" data-display-field="custName" data-value-field="custName"/>
      </k-form-item>
      <k-form-item label="业务联系座机号">
        <k-field-text v-model="formData.businessContactLandline"  :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="业务联系人手机号">
        <k-field-text v-model="formData.businessContactPhoneTm"  :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="业务联络人邮箱">
        <k-field-text v-model="formData.businessContactEmail"  :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="资金投向地区">
        <k-field-select v-model="formData.investRegion" data-dict="invest_region" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品投资国家或地区"  v-show="formData.investRegion != '01'">
        <k-field-select v-model="formData.investRegionOutside" data-dict="t8_countries_regions" :data-disabled="true"  />
      </k-form-item>
      <k-form-item label="理财业务服务模式" v-show="formData.investRegion != '02'">
        <k-field-select v-model="formData.financialServiceMode" data-dict="t8_invest_region" :data-disabled="true"/>
      </k-form-item>


      <k-form-item label="产品资产配置方式">
        <k-field-select v-model="formData.prodAssetAllocation" data-dict="t8_prodAssetAllocation" :data-disabled="true" />
      </k-form-item>
      <k-form-item label="产品管理模式">
        <k-field-select v-model="formData.prodManageMode" data-dict="prod_manage_mode" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="实际管理人名称">
        <k-field-text v-model="formData.actualManagerName" :data-disabled="true" />
      </k-form-item>
      <k-form-item label="产品定价方式">
        <k-field-select v-model="formData.prodPriceWay"  data-dict="prod_price_way" :data-disabled="true"/>
      </k-form-item>

      <k-form-item label="产品销售区域" :data-col="2">
        <k-field-select v-model="formData.prodSalesArea" data-dict="prod_sale_area" data-multiple="true"
                         :data-max-length="2000" inputType="textarea" :rows="1" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="发行机构提前终止权标识">
        <k-field-select v-model="formData.earlyTerminationFlag" :data-disabled="true"  data-dict="early_termination_flag"/>
      </k-form-item>
      <k-form-item label="客户赎回权标识">
        <k-field-select v-model="formData.customerRedemptionFlag" :data-disabled="true"  data-dict="customer_redemption_flag"/>
      </k-form-item>
      <k-form-item label="合作模式">
        <k-field-select v-model="formData.cooperationMode" :data-disabled="true"  data-dict="cooperation_mode"/>
      </k-form-item>
      <k-form-item label="合作机构名称" v-show="formData.cooperationMode != '01'">
        <k-field-text v-model="formData.cooperateOrganizationName"  :data-max-length="120"  :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="投资本金到账日">
        <k-field-select v-model="formData.investPrincipalArriveDate" data-dict="invest_principal_arrive_date" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="投资金收益到账日">
        <k-field-select v-model="formData.investIncomeArriveDate" data-dict="invest_income_arrive_date" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品增信标识">
        <k-field-select v-model="formData.prodCreditLogo" data-dict="prod_credit_logo" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品增信机构类型" v-show="formData.prodCreditLogo == '01'">
        <k-field-select v-model="formData.prodCreditType" data-multiple="true" data-dict="prod_credit_type" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品增信形式" v-show="formData.prodCreditLogo == '01'">
        <k-field-select v-model="formData.prodCreditForm" data-dict="prod_credit_form" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="投资资产种类及比例">
        <k-field-text v-model="formData.investAssetTypeProportion" :data-disabled="true" />
      </k-form-item>
      <k-form-item label="备注" :data-col="2">
        <k-field-text v-model="formData.remarks"  :data-max-length="2000" inputType="textarea" :rows="1" :data-disabled="true"/>
      </k-form-item>
    </k-form>





  </div>

</template>

<script>


    export default {
      name: "T8ProdDeclaraModelParams",
      props: {
        formData:{},
      },
      data() {
        return {
        };
      },
      methods: {
        validateData() {
          return this.$refs.addT8ProdDeclaraModelForm.validate();
        },

        businessContactNameChange(value){
          this.httpUtil
            .comnQuery({
              action: "T8ProdCustomerInfo.find",
              params: {
                custName: value,
                custType:'3'
              },
            })
            .then((data3) => {
              this.$nextTick(() => {
                if (data3.rows.length > 0) {
                  this.formData.businessContactLandline = data3.rows[0].homeTel;
                  this.$set(this.formData,'businessContactPhone',data3.rows[0].mobileNo);
                  this.formData.businessContactPhoneTm = data3.rows[0].mobile;
                  this.formData.businessContactEmail = data3.rows[0].email;
                }
              });
            });
        },


        checkChange(value){
          this.httpUtil.comnQuery({
            action: 'T8ProdCustomerInfo.find',
            params: {
              custName: value,
              custType:'0'
            }
          }).then(data0 => {
            this.$nextTick(()=>{
              if (data0.rows.length>0){
                this.$set(this.formData,"approverIdcardNoTm",data0.rows[0].idCode);
                this.$set(this.formData,"approverIdcardNo",data0.rows[0].idCodeNo);
              }
            })
          });
        },

        designerNameChange(value){
          this.$set(this.formData,"designerIdcardNo",'');
          this.httpUtil
            .comnQuery({
              action: "T8ProdCustomerInfo.find",
              params: {
                custName: value,
                custType:'1'
              },
            })
            .then((data1) => {
              this.$nextTick(() => {
                if (data1.rows.length > 0) {
                  this.$set(this.formData,"designerIdcardNoTm",data1.rows[0].idCode);
                  this.$set(this.formData,"designerIdcardNo",data1.rows[0].idCodeNo);
                }
              });
            });
        },

        investManageNameChange(value){
          this.$set(this.formData,"investManageIdcardNo",'');
          this.httpUtil
            .comnQuery({
              action: "T8ProdCustomerInfo.find",
              params: {
                custName: value,
                custType:'2'
              },
            })
            .then((data2) => {
              this.$nextTick(() => {
                if (data2.rows.length > 0) {
                  this.$set(this.formData,"investManageIdcardNoTm",data2.rows[0].idCode);
                  this.$set(this.formData,"investManageIdcardNo",data2.rows[0].idCodeNo);
                }
              });
            });
        },



      },

    }
</script>

<style scoped>

</style>
