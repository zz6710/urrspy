<template>
    <div>
        <k-form ref="baseInfoForm" :data-col="2"  dataLabelWidth="170px" dataInputWidth="300px">
            <k-form-item label="销售商代码">
                <k-field-text v-model="baseInfoData.distributorCode"  :data-max-length="32" :dataAllowblank="false" :data-disabled="disabledFlag" data-validate-type="codeLetter"/>
            </k-form-item>
            <k-form-item label="销售商名称">
                <k-field-text v-model="baseInfoData.distributorName"  :data-max-length="64" :dataAllowblank="false"/>
            </k-form-item>
            <k-form-item label="销售商状态">
              <k-field-select v-model="baseInfoData.status" data-dict="distributor_status" :dataAllowblank="false"  data-default-value="1"/>
<!--                <k-field-radio v-model="baseInfoData.status" data-dict="distributor_status" :dataAllowblank="false"  data-default-value="1"/>-->
            </k-form-item>
            <k-form-item label="销售商类型">
                <k-field-radio v-model="baseInfoData.distributorType" data-dict="distributor_type" :dataAllowblank="false" data-default-value="1"/>
            </k-form-item>
          <k-form-item label="管理部门">
            <k-field-select v-model="baseInfoData.managerDept" data-dict="manager_dept" :dataAllowblank="false" data-default-value="1"/>
<!--            <k-field-radio v-model="baseInfoData.managerDept" data-dict="manager_dept" :dataAllowblank="false" data-default-value="1"/>-->
          </k-form-item>
<!--            <k-form-item label="销售商工作日方案">
              <k-field-select v-model="baseInfoData.pgmno" data-action="WorkdayProgram.find"  :data-params="{'pgmtype':'3'}"
                             data-display-field="pgmno,pgmname" data-value-field="pgmno" :data-allowblank="false"/>
            </k-form-item>-->
            <k-form-item label="统一社会信用代码">
                <k-field-text v-model="baseInfoData.nLegalCode"  data-validate-type="codeNumber" :data-max-length="18" :data-min-length="18" :dataAllowblank="false"/>
            </k-form-item>
            <k-form-item label="法人代表证件类型">
                <k-field-select v-model="baseInfoData.nLegalType" data-dict="id_type_p" />
            </k-form-item>
            <k-form-item label="法人代表证件号码">
                <k-field-text v-model="baseInfoData.nLegalIdCode" :data-max-length="32" data-validate-type="codeLetter"/>
            </k-form-item>

            <k-form-item label="业务联系人姓名">
                <k-field-text v-model="baseInfoData.busiConnector" :data-max-length="64"/>
            </k-form-item>
            <k-form-item label="业务联系人电话">
                <k-field-text v-model="baseInfoData.busiConnectorMobile" :data-max-length="11" data-validate-type="telephone"/>
            </k-form-item>
            <k-form-item label="技术联系人姓名">
                <k-field-text v-model="baseInfoData.techConnector" :data-max-length="64"/>
            </k-form-item>
            <k-form-item label="技术联系人电话">
            <k-field-text v-model="baseInfoData.techConnectorMobile" :data-max-length="11" data-validate-type="telephone"/>
            </k-form-item>
            <k-form-item label="销售商地址">
                <k-field-text v-model="baseInfoData.address" :data-max-length="128"/>
            </k-form-item>
            <k-form-item label="邮件地址">
                <k-field-text v-model="baseInfoData.email" data-validate-type="email" :data-max-length="64"/>
            </k-form-item>
            <k-form-item label="邮编">
            <k-field-text v-model="baseInfoData.postcode" :data-max-length="6"  data-validate-type="postcode"/>
            </k-form-item>
            <k-form-item label="传真号码">
                <k-field-text v-model="baseInfoData.fax" :data-max-length="20"  data-validate-type="code"/>
            </k-form-item>
            <k-form-item label="备注">
                <k-field-text v-model="baseInfoData.remark" :data-max-length="128"/>
            </k-form-item>
      </k-form>
    </div>
</template>
<script>
export default {
  model: {
      prop: 'baseInfoData',
      event: 'input'
    },
    props: {
    baseInfoData:{},
    addFlag:{
        type:Boolean,
        default:true,
      },
    },
    data(){
        return {
            disabledFlag:false,
        }
    },
    created(){
        //是修改按钮进来，不允许修改销售商代码
        this.disabledFlag = !this.addFlag;
    },
    methods: {
      validateData() {
        return this.$refs.baseInfoForm.validate();
      },
    },
    watch:{
    }


}
</script>
