<template>
  <div>
    <k-form ref="editNetReportRulesForm" :data-col="2" isFormBodyScreen >
      <k-form-item label="ID" v-show="false">
        <k-field-text v-model="formData.id" />
      </k-form-item>
      <k-form-item label="运作模式">
        <k-field-select v-model="formData.operationMode" data-dict="operation_mode" :data-allowblank="false" :data-disabled="disabledVal"
                        data-display-field="itemkey,itemval" data-value-field="itemkey" @data-on-change="operationModeChange"/>
      </k-form-item>

      <k-form-item label="规律开放周期" v-show="formData.operationMode==='03'||formData.operationMode===undefined">
        <k-field-select v-model="formData.regularOpenCycle" data-dict="regular_open_cycle" :data-allowblank="formData.operationMode!=='03'"
                        data-display-field="itemkey,itemval" data-value-field="itemkey" @data-on-change="operationCycleChange" :data-disabled="disabledVal"/>
      </k-form-item>

    </k-form>


    <k-form ref="reportRulesForm1" :data-col="2">

      <k-form-item label="报送规则" v-show="formData.operationMode!=='01'">
        <k-field-select v-model="formData.reportRules" data-dict="report_rules" :data-allowblank="false"
                        data-display-field="itemkey,itemval" data-value-field="itemkey" @data-on-change="reportRulesChange"/>
      </k-form-item>
      <k-form-item label="报送规则" v-show="formData.operationMode==='01'">
        <k-field-select v-model="formData.reportRules" :data-data="reportRulesDict" :data-allowblank="false"
                        data-display-field="itemkey,itemval" data-value-field="itemkey" @data-on-change="reportRulesChange"/>
      </k-form-item>
      <k-form-item label="报送估值日" v-show="formData.operationMode!=='01'">
        <k-field-select v-model="formData.reportDate" data-dict="report_date" :data-allowblank="false"
                        data-display-field="itemkey,itemval" data-value-field="itemkey" :data-disabled="formData.reportRules==='1'||formData.reportRules==='2'"/>
      </k-form-item>
      <k-form-item label="报送估值日" v-show="formData.operationMode ==='01'">
        <k-field-select v-model="formData.reportDate" :data-data="reportDatedict" :data-allowblank="false"
                        data-display-field="itemkey,itemval" data-value-field="itemkey" :data-disabled="formData.reportRules==='1'||formData.reportRules==='2'"/>
      </k-form-item>
      <k-form-item label="月末是否报送" >
        <k-field-checkbox v-model="formData.reportMonth" data-dict="1yes2no" :data-allowblank="false" :data-default-value="formData.operationMode===undefined?'01':''"
                          data-display-field="itemval" data-value-field="itemkey" :data-multiple="false"/>
      </k-form-item>
      <k-form-item v-show="formData.reportRules==='3'">
        <span style="color: #F56C6C;margin-top: 7px;margin-left: 23px;">*</span>
        <span style="padding-top: 7px;margin-left: 1px; " >每</span>
        <k-field-text v-model="formData.lengthFreq" :data-allowblank="false"
                      class="md-padding-left-10" style="width: 57px; margin-left: 10px" />
        <span style="color: #F56C6C;margin-top: 3px;">*</span>
        <k-field-select v-model="formData.reportFreq" data-dict="report_freq" @data-on-change="reportFreqChange"
                        class="md-padding-left-10" style="width: 90px; margin-left: 10px" />
        <span style="color: #F56C6C;margin-top: 3px;">*</span>
        <k-field-select v-model="formData.specificDate" data-dict="week" v-show="formData.reportFreq==='1'"
                        class="md-padding-left-10" style="width: 90px; margin-left: 10px"/>
        <k-field-select v-model="formData.specificDate" data-dict="month" v-show="formData.reportFreq!=='1'"
                        class="md-padding-left-10" style="width: 90px; margin-left: 10px"/>
      </k-form-item>
      <k-form-item label="创建日期" v-show="false">
        <k-field-text v-model="formData.crtDate"/>
      </k-form-item>
      <k-form-item label="创建时间" v-show="false">
        <k-field-text v-model="formData.crtTime"/>
      </k-form-item>
      <k-form-item label="创建人" v-show="false">
        <k-field-text v-model="formData.crtUser"/>
      </k-form-item>
      <k-form-item label="修改日期" v-show="false">
        <k-field-text v-model="formData.updDate"/>
      </k-form-item>
      <k-form-item label="修改时间" v-show="false">
        <k-field-text v-model="formData.updTime"/>
      </k-form-item>
      <k-form-item label="修改人" v-show="false">
        <k-field-text v-model="formData.updUser"/>
      </k-form-item>
    </k-form>

<!--    <div class ="tableLine" v-show = "formData.reportRules==='3'"><span class="midText">固定频率报送规则</span></div>-->

    <k-form ref="reportRulesForm2" :data-col="2" v-if="formData.reportRules==='3'">
      <k-form-item label="确认日是否报送" style="margin-top: 20px" v-show ="false">
        <k-field-checkbox v-model="formData.reportConfirmDate" data-dict="1yes2no" :data-allowblank="false"
                          data-display-field="itemval" data-value-field="itemkey" :data-multiple="false"/>
      </k-form-item>
    </k-form>


    <k-form ref="btnForm" :data-col="2">
      <k-form-footer data-align="center">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-from="editNetReportRulesForm"
               :data-model="formData" :data-handler="submitHandler">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">
          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
      </k-form-footer>
    </k-form>






  </div>
</template>

<script>
import Tools from "@/utils/tools";

export default {
  name: "NetReportRulesEdit",
  props: {
    info : {
      type:Object,
    },

    disabledVal: {
      type:Boolean,
    },
  },
  data() {
    return {
      formData : {},
      reportRulesDict:[
        {itemkey:'1',itemval:'每日报送'},
        {itemkey:'3',itemval:'按固定频率报送'}
      ],
      regularOpenCycle1:[
        {itemkey:'1',itemval:'每天'},
      ],
      reportDatedict:[
        {itemkey:'1',itemval:'上周五'},
        {itemkey:'2',itemval:'上一工作日'}
      ],
      regularOpenCycle0:[
        {itemkey:'2',itemval:'每周'},
        {itemkey:'3',itemval:'双周开'},
        {itemkey:'4',itemval:'每月'},
        {itemkey:'5',itemval:'每季度'},
        {itemkey:'6',itemval:'半年'},
        {itemkey:'7',itemval:'一年'},
        {itemkey:'8',itemval:'两年'},
        {itemkey:'9',itemval:'三年'},
      ],

    };
  },
  computed : {

  },

  methods: {

    submitHandler(val){
      let fromBool1 = this.$refs.editNetReportRulesForm.validate();
      let fromBool2 = this.$refs.reportRulesForm1.validate();

      if(!fromBool1 || !fromBool2)
        return false;

      if (val.reportRules === '3'){//固定频率报送
        if(!val.lengthFreq){
          Tools.alert('未填写报送周期长度', "danger");
          return false;
        }
        if(!val.specificDate){
          Tools.alert('未选择报送报送日期', "danger");
          return false;
        }
        if(!val.reportFreq){
          Tools.alert('未选择周期单位', "danger");
          return false;
        }
      }

      let url = "NetReportRules.addNetReportRules";
      if (this.disabledVal)
        url = "NetReportRules.updateNetReportRules";

      this.httpUtil.comnUpdate({
        action: url,
        params: val,
      }).then(data => {
        if (data.success === true) {
          this.$emit('loadGrid',val);
        }
      });
    },
    operationModeChange(val){
      this.$set(this.formData,'reportRules','');
      this.$set(this.formData,'reportDate','');
      this.$set(this.formData,'lengthFreq','');
      this.$set(this.formData,'reportFreq','');
      this.$set(this.formData,'specificDate','');
    },
  operationCycleChange(val){
    if(val === '01'){
      this.$set(this.formData,'reportRules','1');
      this.$set(this.formData,'reportDate','2');
    }else{
      this.$set(this.formData,'reportRules','');
      this.$set(this.formData,'reportDate','');
    }
  },

    reportRulesChange(val){
        this.$set(this.formData,'reportDate','');
        this.$set(this.formData,'lengthFreq','');
        this.$set(this.formData,'reportFreq','');
        this.$set(this.formData,'specificDate','');
        // this.$set(this.formData,'reportConfirmDate','');
      if (val === '2'){//同确认日报送
        this.$set(this.formData,'reportDate','3');
      }
      if (val === '1'){//每日报送
        this.$set(this.formData,'reportDate','2');
      }
    },
    reportFreqChange(val){
      let reportDate = val === '1' ? '1' : '2';
      this.$set(this.formData,'reportDate',reportDate);
    }


  },
  created() {
    this.formData = this.info;
  },

  watch:{
    'formData.reportRules'(val){

    }
  }


}
</script>

<style scoped>
.tableLine {
  margin: 30px 8px 20px 6px;border-top:1px dotted #C0C0C0;
  width: 750px;
  position: relative;
  text-align: center;
  font-size: 14px;
}

.midText {
  position: absolute;
  left: 50%;
  background-color: #ffffff;
  font-weight: 300;
  padding: 0 15px;
  transform: translateX(-50%) translateY(-50%);
}

</style>
