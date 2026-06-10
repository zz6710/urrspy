<template>
  <k-form ref="addForm" :data-col="2">
    <k-form-item label="产品代码" v-show="false">
      <k-field-text v-model="value.prodCode" :data-allowblank="false"/>
    </k-form-item>
    <k-form-item label="服务费类型" >
      <k-field-select v-model="value.feeType" data-dict="sale_manager_fee_type"  :data-allowblank="false"/>
    </k-form-item>

    <k-form-item label="计提开始日">
      <k-field-date v-model="value.startDate" :data-allowblank="false" :data-max-value="value.endDate" :dataMinValue="this.currentWorkday"/>
    </k-form-item>
    <k-form-item label="计提截止日">
      <k-field-date v-model="value.endDate" :data-allowblank="false" :data-min-value="value.startDate"  :dataMinValue="this.currentWorkday"/>
    </k-form-item>
    <k-form-item label="计划支付日">
      <k-field-date v-model="value.theoryPayDate" :data-allowblank="false" :data-min-value="'('+value.endDate"/>
    </k-form-item>
    <k-form-item label="实际支付日">
      <k-field-date v-model="value.factPayDate" :data-min-value="'('+value.endDate"/>
    </k-form-item>
    <k-form-item label="创建人" v-show="false">
      <k-field-text v-model="value.crtUser"/>
    </k-form-item>
    <k-form-item label="备注">
      <k-field-text v-model="value.remark" :data-max-length="150"/>
    </k-form-item>
    <k-form-footer data-align="center">
      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T83002.addT83002"
                 data-from="addForm" :data-model="value" :data-handler="checkParam"
                 data-target="T81001Grid">
         <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
      </k-btn>
      <k-btn class="btn-custom-plain" data-functype="CLOSE">
       <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>

    </k-form-footer>
  </k-form>
</template>

<script>
  import kayak from '@/frame/kayak.js'
  import Tools from '@/utils/tools.js';
  export default {
    props: {
      updSuccess: Function,
      info:{
        type:Object,
      }
    },
    data() {
      return {
        userid: localStorage.getItem("userid"),
        value : {},
        currentWorkday: null,
      };
    },
    created() {
      this.value = this.info;
      this.httpUtil.sysDate().then(res=>{
        if (res) {
          this.currentWorkday = res;
        }
      });
    },
    methods: {
      checkParam(){

        if(this.value.prodCode==null || this.value.prodCode==''){
            Tools.alert("未录入产品代码","danger");
            return false;
        }

       if(this.value.startDate == null){
         Tools.alert("计提开始日不能为空", "danger");
         return false;
       }
       if(this.value.endDate == null){
         Tools.alert("计提截止日不能为空", "danger");
         return false;
       }

       if(this.value.theoryPayDate == null){
         Tools.alert("计划支付日不能为空", "danger");
         return false;
       }

       if(this.value.theoryPayDate <= this.value.endDate){
         Tools.alert("计划支付日不能小于等于计提截止日", "danger");
         return false;
       }

       if(this.value.factPayDate <= this.value.endDate){
         Tools.alert("实际支付日不能小于等于计提截止日", "danger");
         return false;
       }


       return true;
      },

    },
    computed: {
    },

  };
</script>
