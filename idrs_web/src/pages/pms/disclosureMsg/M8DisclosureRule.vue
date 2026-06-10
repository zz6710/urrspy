<template>
  <div>
    <div>
      <k-form-search-customize data-target="t8ObjectGrid" v-model="prodSearchParam">
        <k-form-item label="产品代码">
          <k-field-select v-model="prodSearchParam.prodCode" data-action="T8Dict.findNotEstablishProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode"/>
        </k-form-item>
        <k-btn slot="button" data-functype="POPUP" class="btn-custom-primary" style="width: 120px;"
               data-target="addPopup">
          <!-- <md-icon>copy</md-icon> -->
          复制信披规则
        </k-btn>
      </k-form-search-customize>
      <k-grid ref="t8ObjectGrid" data-action="" :data-checkbox="true" data-checkbox-id="prodCode"
              :dataData="dataList" :dataAutoload="false" data-operate-width="200">
        <k-grid-column data-header="产品代码" data-name="prodCode"/>
        <k-grid-column data-header="产品名称" data-name="prodName"/>
        <k-grid-column data-header="适用类型" data-name="appropriateType"/>
        <k-grid-column data-header="信披规则名称" data-name="ruleName"/>
        <k-grid-column data-header="模板名称" data-name="modelName"/>
        <k-grid-column data-header="信披类型" data-dict="t8_disclosure_type" data-name="type"/>
        <!-- <k-grid-column data-header="信披子类型" data-name="itemType" /> -->
        <template slot="operate" slot-scope="scope">
          <k-btn data-functype="POPUP" data-size="mini" data-target="editPopup"
                 class="btn-custom-plain" data-descript="启用" :data-disabled="scope.row.row.isOpen != 'true'">
            启用
          </k-btn>
          <k-btn data-functype="POPUP" data-size="mini" data-target="editPopup"
                 class="md-danger" data-descript="停用" :data-disabled="scope.row.row.isOpen">
            停用
          </k-btn>
        </template>
      </k-grid>


      <k-popup ref="addPopup" data-title="复制信披规则">
        <k-form ref="addForm" :data-col="1">
          <k-form-item label="选择产品(多选)">
            <k-field-select v-model="formData.prodCode" data-action="T8Dict.findNotEstablishProdInfos"
                            data-display-field="prodCode,prodName" data-value-field="prodCode" :data-multiple="true"/>
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary"
                   :data-handler="saveRule"
                   data-from="addForm" :data-model="formData"
                   data-target="t8ObjectGrid">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
            </k-btn>
          </k-form-footer>
        </k-form>
      </k-popup>

    </div>
  </div>


</template>

<script>
import {assign} from "lodash";
import Tools from "@/utils/tools";

export default {
  name: "M8DisclosureRule",
  data() {
    return {
      prodSearchParam: {
        prodCode: '',
      },
      formData: {
        prodCode: '',
      },
      dataList:{
        rows:[
          {prodCode:'',prodName:'',appropriateType:'所有产品',ruleName:'发行公告模板（定期开放类)',modelName:'发行公告模板（定期开放类).docx',type:'2',isOpen:"true"},
          {prodCode:'EB1606',prodName:'阳光橙优选配置',appropriateType:'产品',ruleName:'阳光橙按季定开定期报告',modelName:'定期报告-季报.docx',type:'6',isOpen:"false"},
          {prodCode:'',prodName:'',appropriateType:'产品组',ruleName:'丰利系列净值公告',modelName:'丰利系列净值报告.doc',type:'4',isOpen:"true"},
          {prodCode:'',prodName:'',appropriateType:'与产品无直接关联',ruleName:'公司年报',modelName:'公司年报.docx',type:'10',isOpen:"true"},
        ]
      }
    }
  },
  watch:{
  },
  created() {
  },
  methods: {
    saveRule(params){
      Tools.alert("保存成功","success");
      this.$refs.addPopup.close();
      return false;
    }
  }
}
</script>

<style scoped>

</style>
