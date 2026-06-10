<template>
    <div>
        <k-form ref="techInfoForm" :data-col="2"   dataLabelWidth="170px" dataInputWidth="300px">
            <k-form-item label="接口方式">
                <k-field-radio v-model="baseInfoData.interfaceType" ref="interfaceType" data-default-value="0" data-dict="interface_type" :dataAllowblank="false"  @data-on-change="findVersionByType"/>
            </k-form-item>
            <k-form-item label="接口配置版本">
                <k-field-select v-model="baseInfoData.interfaceVersion"
                ref="interfaceVersion" data-action="Ta2017.findVersionByType"
                 data-display-field="interfaceVersion" data-value-field="interfaceId"  />
            </k-form-item>

            <k-form-item label="是否导出参数文件(C1-C5)">
                <k-field-radio v-model="baseInfoData.isExportC1c5File" :dataAllowblank="false" data-dict="is_export_c1c5_file" data-default-value="1"/>
            </k-form-item>
            <k-form-item label="是否导出C6、26文件">
                <k-field-radio v-model="baseInfoData.isExportC626File" :dataAllowblank="false" data-dict="1yes0no" data-default-value="0"/>
            </k-form-item>

            <k-form-item label="是否导出销售服务费文件">
                <k-field-radio v-model="baseInfoData.isExportSaleFeeFile" :dataAllowblank="false" data-dict="is_export_sale_fee_file" data-default-value="1"/>
            </k-form-item>
            <k-form-item label="是否允许发起违约赎回">
                <k-field-radio v-model="baseInfoData.allowBreakRedeem" data-dict="1yes0no" :dataAllowblank="false" data-default-value="0"/>
            </k-form-item>
            <k-form-item label="是否支持多交易账号">
                <k-field-radio v-model="baseInfoData.isTransMuchAcct" data-dict="is_trans_much_acct" :dataAllowblank="false" data-default-value="0"/>
            </k-form-item>
            <k-form-item label="是否支持单步转托管">
                <k-field-radio v-model="baseInfoData.isSingleTrust" data-dict="is_single_trust" :dataAllowblank="false" data-default-value="1"/>
            </k-form-item>
            <k-form-item label="转换确认方式" v-show="false">
                <k-field-radio v-model="baseInfoData.convertAckMethod" data-dict="convert_ack_method" :dataAllowblank="false" data-disabled data-default-value="1"/>
            </k-form-item>
            <k-form-item label="是否有份额明细">
                <k-field-radio v-model="baseInfoData.isVolList" data-dict="is_vol_list" :dataAllowblank="false" data-default-value="0" :dataDisabled="true"/>
            </k-form-item>
            <k-form-item label="对账方式">
                <k-field-radio v-model="baseInfoData.checkType" data-dict="check_type" :dataAllowblank="false" data-default-value="0"/>
            </k-form-item>

          <k-form-item label="导入批次">
            <k-field-select id="impTaskGroup" v-model="baseInfoData.impTaskGroup" :data-max-length="32" data-action="T8ClearGroupInfo.findT8ClearGroupInfos" :dataParams="{'execTaskType':'3'}"
                            data-display-field="taskGroupName"  data-value-field="taskGroup" />
          </k-form-item>
          <k-form-item label="导出批次">
              <k-field-select id="expTaskGroup" v-model="baseInfoData.expTaskGroup" :data-max-length="32" data-action="T8ClearGroupInfo.findT8ClearGroupInfos" :dataParams="{'execTaskType':'4'}"
                              data-display-field="taskGroupName"  data-value-field="taskGroup" />
            </k-form-item>

            <k-form-item label="申请文件路径">
                <k-field-text v-model="baseInfoData.reqFilePath" :data-max-length="60"/>
            </k-form-item>
            <k-form-item label="确认文件路径">
                <k-field-text v-model="baseInfoData.cfmFilePath" :data-max-length="60"/>
            </k-form-item>
            <k-form-item label="行情文件路径">
                <k-field-text v-model="baseInfoData.funddayFilePath" :data-max-length="60"/>
            </k-form-item>
            <k-form-item label="是否节假日推送">
                <k-field-radio v-model="baseInfoData.isHolidaysSend" data-dict="1yes0no" :dataAllowblank="false" data-default-value="1"/>
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
        }
    },
    created(){
    },
    methods: {
      findVersionByType(value) {
           let interfaceType = value;
            this.$refs.interfaceVersion.load({interfaceType:interfaceType});
      },
      validateData() {
        return this.$refs.techInfoForm.validate();
      },
    },
}
</script>
