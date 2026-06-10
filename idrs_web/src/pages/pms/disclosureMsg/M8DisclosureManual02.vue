<template>
  <div>
    <k-form ref="addForm" :data-col="2">
      <k-form-item label="公告ID">
        <k-field-text v-model="formData.id" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="创建时间">
        <k-field-text v-model="formData.crtDate" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="创建人">
        <k-field-text v-model="formData.crtUser" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="公告标题" :data-col="2">
        <k-field-text v-model="formData.title"/>
      </k-form-item>
      <k-form-item label="信披规则名称" :data-col="2">
        <k-field-select v-model="formData.ruleCode" style="width: 60%;" :data-data="modelData"
                        data-display-field="text" data-value-field="value"/>
      </k-form-item>
      <k-form-item label="信披类型" :data-col="2">
        <k-field-select v-model="formData.type" data-dict="t8_disclosure_type" style="width: 60%;"/>
      </k-form-item>
      <!-- <k-form-item label="信披子类型" :data-col="2">
        <k-field-select v-model="formData.itemType" data-action="T8Dict.findNotEstablishProdInfos" style="width: 60%;"
                        data-display-field="prodCode,prodName" data-value-field="prodCode" :data-multiple="true"/>
      </k-form-item> -->
      <k-form-item label="选择产品" :data-col="2">
        <k-field-select v-model="formData.prodCode" data-action="T8Dict.findNotEstablishProdInfos" style="width: 60%;"
                        data-display-field="prodCode,prodName" data-value-field="prodCode"/>
        (单选)
      </k-form-item>
	  <k-form-item label="备注说明" :data-col="2">
	    <k-field-text v-model="formData.note" input-type="textarea" rows="3"/>
	  </k-form-item>
      <k-form-footer data-align="center">
        <k-btn class="btn-custom-primary"
                :data-handler="saveRule"
               data-from="addForm" :data-model="formData"
               data-target="t8ObjectGrid">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>提交
        </k-btn>
      </k-form-footer>
    </k-form>
  </div>


</template>

<script>
import {assign} from "lodash";
import Tools from "@/utils/tools";

export default {
  name: "",
  data() {
    return {
      prodSearchParam: {
        prodCode: '',
      },
      formData: {
        prodCode: '',
        prodName: '',
        feeJson:'',
        id:'系统自动生成',
        crtDate:new Date().toLocaleString(),//当前时间
        crtUser:'',
      },
      modelData:[
        {text:'1',value:"待提供数据"},
      ],
    }
  },
  watch:{
  },
  created() {
    Tools.getLoginUser().then(res => {
      this.formData.crtUser=res.username;
    })
  },
  methods: {
    saveRule(params){
      Tools.alert("保存成功","success");
      return false;
    }
  }
}
</script>

<style scoped>

</style>
