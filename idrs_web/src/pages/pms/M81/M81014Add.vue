<template>
  <k-form ref="updateProdRegistForm" :data-col="2">
    <k-form-item label="产品代码">
      <k-field-text v-model="value.prodCode" data-disabled="true" :data-allowblank="false"/>
    </k-form-item>
    <k-form-item label="产品名称">
      <k-field-text v-model="value.prodName" data-disabled="true" :data-allowblank="false"/>
    </k-form-item>
    <k-form-item label="申报结果">
      <k-field-select v-model="value.prodSonStatus" :data-data="options" :data-allowblank="false"/>
    </k-form-item>
     <k-form-item label="募集方式" v-show="false">
      <k-field-select v-model="value.raiseType" />
    </k-form-item>
    <k-form-item label="产品登记编码" v-if="value.prodSonStatus == 8">
      <k-field-text v-model="value.registCode"/>
    </k-form-item>
    <k-form-footer data-align="center">
      <k-btn class="btn-custom-primary" data-functype="SUBMIT"
             data-from="updateProdRegistForm" :data-model="value" data-target="prodInfoGrid" :data-handler="updateInfo" data-action="T8ProdRegist.updateT8ProdRegisCodeAndStatus">
        <md-icon md-src="/static/svg/confirm.svg"/>确定
      </k-btn>
      <k-btn class="btn-custom-plain" data-functype="CLOSE">
        <md-icon md-src="/static/svg/cancel.svg"/>取消
      </k-btn>
    </k-form-footer>
  </k-form>
</template>

<script>
import kayak from '@/frame/kayak.js'
//import ListWorkDay from "./ListWorkDay";
import Tools from '@/utils/tools';

export default {
  name:"M81014Add",
  props: {
    updSuccess: Function
  },
  data() {
    return {
      options:[
        {
          label:'申报成功',
          value:'8'
        },
        {
          label:'申报失败',
          value:'9'
        }
      ],
    };
  },
  methods: {
    onSubmitError() {
      this.$refs.uploadRef.doReset();
      this.$refs.submitBtn.setIconStyle(1, []);
    },
    onSubmitSuccess() {

      this.$refs.editTable.close();
      this.$refs.prodInfoGrid.load();
    },
    updateInfo(){
      if(this.value.prodSonStatus == 8 && this.value.registCode.length == 0){
        Tools.alert("产品登记编码不能为空!","danger");
        return false;
      }
        //公募 产品登记编码14
        if(this.value.prodSonStatus == 8 && this.value.raiseType=='01' && this.value.registCode.length!='14'){
          Tools.alert("公募产品登记编码长度必须为14位!","danger");
          return false;
        }
        //私募 产品登记编码15
        if(this.value.prodSonStatus == 8 && this.value.raiseType=='02' && this.value.registCode.length!='15'){
          Tools.alert("私募产品登记编码长度必须为15位!","danger");
          return false;
        }

      // this.httpUtil.comnUpdate({
      //     action: 'T8ProdInfo.updateT8ProdRegisCodeAndStatus',
      //     params: this.value,
      //     successAlert: true
      //   }).then(data => {
      //    // this.reloadGroupData();
      //    this.onSubmitSuccess();
      //   })
    },
    //  //查询
    //   reloadGroupData(){
    //     this.httpUtil.comnQuery({
    //       action: 'T8ProdTemplate.findProdTemplateInfo',
    //       params: {},
    //     }).then(data => {
    //       this.prodModeinfos = data.rows;

    //     });
    //   }

  },
  computed: {
    value() {

      let value = this.$attrs.value;

      if(value.prodSonStatus != '8' && value.prodSonStatus != '9'){
        value.prodSonStatus = '';
      }
      return value;
    }
  }
};
</script>
