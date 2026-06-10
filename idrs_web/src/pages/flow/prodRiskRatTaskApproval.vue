<template>
  <div>
    <k-form ref="addProdRiskRatForm" :data-col="4" data-input-width="320px" data-label-width="180px">
      <k-form-item label="理财产品名称:">
        <k-field-select v-model="formData.t8ProdInfoId" data-action="T8ProdInfo.findAllT8ProdCode"
                        data-display-field="prodCode,prodName" data-value-field="id" :data-allowblank="false" data-disabled="true"/>
      </k-form-item>
      <k-form-item label="模板:">
        <k-field-select v-model="formData.t8RiskTemplateVersionId" data-action="ProdRiskTemplate.getTemplateList"
                        data-value-field="id" data-display-field="templateName" data-disabled="true"/>
      </k-form-item>
    </k-form>
    <k-grid ref="addGrid" :dataRowStyle="setBGcolor" :data-data="{'rows':this.prodRiskRat.$RatGrid}"
            :data-page-size="0" data-height="500px" data-operate-column="false"
             data-display="false" style="height: 600px; overflow: auto;">
      <k-grid-column data-header="项目" data-name="riskProject" data-width="250">
        <template slot-scope="scope">
          <div style="text-align: center;" v-if="scope.row.row.coefficient != '-' && scope.row.row.coefficient != ''">{{scope.row.row.riskProject}}</div>
          <div style="margin-left: 40px;" v-if="scope.row.row.coefficient == ''">{{scope.row.row.riskProject}}</div>
          <div v-if="scope.row.row.coefficient == '-'">{{scope.row.row.riskProject}}</div>
        </template>
      </k-grid-column>
      <k-grid-column data-header="风险系数(模板)" data-name="coefficient" data-width="150"></k-grid-column>
      <k-grid-column data-header="风险系数(产品)" data-name="coefficientProd" data-width="150">
        <template slot-scope="scope" v-if="scope.row.row.coefficient != null && scope.row.row.coefficient != '' && scope.row.row.isShowInput=='1'">
          <el-input v-model="scope.row.row.coefficientProd"  @change="checkDateRange(scope.row.row)" :disabled="true"></el-input>
        </template>
      </k-grid-column>
      <k-grid-column data-header="权重" data-name="weight" data-width="150"></k-grid-column>
      <k-grid-column data-header="判断（0-1）" data-name="judge" data-width="150">
        <template slot-scope="scope" v-if="scope.row.row.coefficient != null && scope.row.row.coefficient != '' && scope.row.row.isShowInput=='1'">
          <el-input v-model="scope.row.row.judge" @input="sunIntegral(scope.row.row,'judge')" :disabled="true"></el-input>
        </template>
      </k-grid-column>
      <k-grid-column data-header="积分" data-name="integral" data-width="150">
        <template slot-scope="scope">
          <el-input v-model="scope.row.row.integral" :disabled="true"
                    v-if="scope.row.row.coefficient != null && scope.row.row.coefficient != '' && scope.row.row.isShowInput=='1'"></el-input>
          <font size="5" v-if="scope.row.row.riskProject == '产品评级得分'">{{scope.row.row.integral}}</font>
        </template>
      </k-grid-column>
    </k-grid>

    <k-form ref="addProdRiskRatForm" :data-col="3">
      <div style="margin-left: 10px">
        <k-form-item label="产品评分备注:" :data-col="2.8">
          <k-field-text v-model="formData.riskRemark" inputType="textarea" :data-max-length="4000" data-disabled="true"/>
        </k-form-item>
      </div>
    </k-form>
  </div>
</template>

<script>
import Tools from "@/utils/tools";

export default {
  name: "prodRiskRatTaskApproval",
  props:{
    dataData:{
      type:Array,
      required:true
    }
  },
  data(){
    return{
      formData:[],
      prodRiskRat: {
        prodName: '',
        t8ProdInfoId: '',
        t8RiskTemplateVersionId: '',
        $RatGrid: null,//风险评分表格对象
      },
    }
  },
  created() {
    this.getUrlFormData();
  },
  methods:{
    //空方法不做任何操作
    validateData(){
      return true;
    },
    getUrlFormData(){
      console.log('prodRiskRatTaskApproval:this.dataData',this.dataData)
      console.log('url形式:prodRiskRatTaskApproval',this.dataData.id)
      let processId = this.dataData.id;
      this.httpUtil.ajax({
        url: 'wf/businessProcess/querySubmitParams.json',
        params: {'processInstanceId':processId},
      }).then(data => {
        this.formData = JSON.parse(data.data);
        console.log('prodRiskRatTaskApproval解析后的值',this.formData)
        let datas = this.formData.datas;
        let dataList = JSON.parse(datas);
        this.prodRiskRat.$RatGrid = dataList;
        console.log('prodRiskRatTaskApproval->解析datas',dataList)
        console.log('this.prodRiskRat.$RatGrid',this.prodRiskRat.$RatGrid)
        this.$refs.addGrid.list = this.prodRiskRat.$RatGrid;
      });
    },
    setBGcolor(row){
      let stylejson={};
      if(this.getTitle1(row.row.riskProject)){
        stylejson.backgroundColor="#CC99FF";
      }else if(this.getTitle2(row.row.riskProject)){
        stylejson.backgroundColor="#FFFF00";
      }
      return stylejson;
    },
    //紫色标题
    getTitle1(val){
      let title=['投资价值风险','流动性风险','操作风险','汇率风险','其他风险','投资策略调减项**','投资策略调增项**'];
      for(let i=0;i<=title.length;i++){
        if(title[i] == val){
          return true;
        }
      }
      return false;
    },
    //黄色标题
    getTitle2(val){
      let title=['产品类型','融资主体性质','公开市场评级','增信措施','资产区域分布情况','资产层级情况','投资范围','募集方式','杠杆比例（总资产/净资产）','分级比例（优先级/劣后级）','开放式产品','封闭式产品','估值方式','操作程序','币种'];
      for(let i=0;i<=title.length;i++){
        if(title[i] == val){
          return true;
        }
      }
      return false;
    },
  }
}
</script>

<style scoped>

</style>
