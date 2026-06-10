<template>
  <k-form ref="addForm" :data-col="2">
    <k-form-item label="产品代码"  v-show="false">
      <k-field-text v-model="value.prodCode" :data-allowblank="false" data-disabled/>
    </k-form-item>
    <k-form-item label="启用日期">
      <k-field-date v-model="value.enableDate" :data-allowblank="false" :dataMinValue="this.currentWorkday"	/>
    </k-form-item>
    <k-form-item label="管理费计算方式">
      <k-field-select v-model="value.calcType" data-dict="calSalesFee" :data-allowblank="false"/>
    </k-form-item>
    <k-form-item label="年天数">
      <k-field-select v-model="value.managefeeYeardays" data-dict="yeardays" :data-allowblank="false"/>
    </k-form-item>
    <k-form-item label="净值取值规则">
      <k-field-select v-model="value.navRule" data-dict="nav_rule" :data-allowblank="false"/>
    </k-form-item>
    <k-form-item label="管理费率(%)">
      <k-field-text data-validate-type="number" v-model="value.rate" data-min-value="0" data-max-value="100" data-digits="5" :data-allowblank="false"/>
    </k-form-item>
    <k-form-item label="创建人" v-show="false">
      <k-field-text v-model="value.crtUser"/>
    </k-form-item>
    <k-form-item label="备注">
      <k-field-text v-model="value.remark" :data-max-length="256"/>
    </k-form-item>
    <k-form-footer data-align="center">
      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T83007.addT83007"
                 data-from="addForm" :data-model="value" :data-handler="confirmHandler"
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
        value:{},
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
      confirmHandler(params){
        if(this.value.prodCode==null || this.value.prodCode==''){
            Tools.alert("未录入产品代码","danger");
            return false;
        }
        return params;
      }
    },
    computed: {
    }
  };
</script>
