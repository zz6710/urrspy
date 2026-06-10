<template>
  <div>
    <k-form ref="addForm" :data-col="2">
      <k-form-item label="公告ID">
        <k-field-text v-model="formData.id" :data-disabled="true"/>
      </k-form-item>
	  <k-form-item label="创建时间">
	    <k-field-text v-model="formData.crtDate" :data-disabled="true"/>
	  </k-form-item>
	  <k-form-item label="公告标题" :data-col="2">
	    <k-field-text v-model="formData.title" />
    </k-form-item>
      <k-form-item label="产品规则名称" :data-col="2">
        <k-field-text v-model="formData.ruleCode" style="width: 60%;"/>
        <k-btn class="btn-custom-plain" style="margin-top: -0px;">
          取数生成
        </k-btn>
      </k-form-item>
      <k-form-item label="公告文件" :data-col="2">
        <k-field-text v-model="formData.notice_file"/>
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
    <k-form-item label="计划发布日期" :data-col="2">
      <k-field-date v-model="formData.startEstablishDate" data-type="date" style="width: 60%;"/>
    </k-form-item>
    <k-form-item label="发送邮箱" :data-col="2">
      <k-field-select v-model="formData.sendEmail" style="width: 60%;" :data-data="modelData"
        data-display-field="text" data-value-field="value"/>(单选)
    </k-form-item>
      <k-form-item label="信披渠道" :data-col="2">
        <k-field-checkbox data-label-width="120px" v-model="formData.channel" :data-allowblank="false"
                          :data-data="xpqd" data-display-field="text" data-value-field="value"/>
      </k-form-item>
    <k-form-item label="其他接收邮箱" :data-col="2">
      <k-field-text v-model="formData.otherEmail" />
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
      xpqd:[
        {label:'中国光大银行官网',value:'1'},
        {label:'光大理财官网',value:'2'},
        {label:'光大银行销售渠道',value:'3'},
        {label:'光大理财直销渠道',value:'4'},
        {label:'行外代销机构',value:'5'},
        {label:'专户机构邮箱',value:'6'},
      ],
      prodSearchParam: {
        prodCode: '',
      },
      formData: {
        prodCode: '',
        prodName: '',
        feeJson:'',
        id:'系统自动生成',
        crtDate:new Date().toLocaleString(),//当前时间
      },
      modelData:[
        {text:'1',value:"待提供数据"},
      ],
    }
  },
  watch:{
  },
  created() {
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
