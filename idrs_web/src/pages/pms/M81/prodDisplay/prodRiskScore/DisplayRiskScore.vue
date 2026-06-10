<template>
  <div>
    <k-form ref="updateProdRiskRatForm" :data-col="3">
      <k-form-item label="理财产品名称:">
        <k-field-select v-model="prodRiskRat.t8ProdInfoId" data-action="T8ProdInfo.findT8ProdInfos"
                        data-display-field="prodCode,prodName" data-value-field="id" data-disabled/>
      </k-form-item>
      <k-form-item label="模板:">
        <k-field-select v-model="prodRiskRat.t8RiskTemplateVersionId" data-action="ProdRiskTemplate.getTemplateList"
                        data-value-field="id" data-display-field="templateName" data-disabled/>
      </k-form-item>
    </k-form>


    <k-grid ref="updateGrid" :dataRowStyle="setBGcolor" data-page-size="0" data-height="500px"
            data-operate-column="false" data-display="false" style="height: 600px; overflow: auto;">
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
          <el-input v-model="scope.row.row.coefficientProd"  :disabled="true"/>
        </template>
      </k-grid-column>
      <k-grid-column data-header="权重" data-name="weight" data-width="150"></k-grid-column>
      <k-grid-column data-header="判断（0-1）" data-name="judge" data-width="150">
        <template slot-scope="scope" v-if="scope.row.row.coefficient != null && scope.row.row.coefficient != ''  && scope.row.row.isShowInput=='1'">
          <el-input v-model="scope.row.row.judge" :disabled="true"></el-input>
        </template>
      </k-grid-column>
      <k-grid-column data-header="积分" data-name="integral" data-width="150">
        <template slot-scope="scope">
          <el-input v-model="scope.row.row.integral"  :disabled="true"
                    v-if="scope.row.row.coefficient != null && scope.row.row.coefficient != '' && scope.row.row.isShowInput=='1'"></el-input>
          <font size="5" v-if="scope.row.row.riskProject == '产品评级得分'">{{scope.row.row.integral}}</font>
        </template>
      </k-grid-column>
    </k-grid>
    <k-form ref="addProdRiskRatForm" :data-col="3">
      <div style="margin-left: 10px">
        <k-form-item label="产品评分备注:" :data-col="2.8">
          <k-field-text v-model="riskRemark" input-type="textarea" :data-disabled="true"/>
        </k-form-item>
      </div>
    </k-form>
  </div>
</template>

<script>
  export default {
    name: "DisplayRiskScore",
    props:{
      prodRiskRat:{},
      riskRemark:{},
    },
    mounted() {
      this.$set(this.$refs.updateGrid,'list',this.prodRiskRat.RatGrid);
    },
    methods: {
      //根据参数判断本行是否设置背景颜色
      setBGcolor(row) {
        let stylejson = {};
        if (this.getTitle1(row.row.riskProject)) {
          stylejson.backgroundColor = "#CC99FF";
        } else if (this.getTitle2(row.row.riskProject)) {
          stylejson.backgroundColor = "#FFFF00";
        }
        return stylejson;
      },
      //紫色标题
      getTitle1(val) {
        let title = ['投资价值风险', '流动性风险', '操作风险', '汇率风险', '其他风险', '投资策略调减项**', '投资策略调增项**'];
        for (let i = 0; i <= title.length; i++) {
          if (title[i] == val) {
            return true;
          }
        }
        return false;
      },
      //黄色标题
      getTitle2(val) {
        let title = ['产品类型', '融资主体性质', '公开市场评级', '增信措施', '资产区域分布情况', '资产层级情况', '投资范围', '募集方式', '杠杆比例（总资产/净资产）', '分级比例（优先级/劣后级）', '开放式产品', '封闭式产品', '估值方式', '操作程序', '币种'];
        for (let i = 0; i <= title.length; i++) {
          if (title[i] == val) {
            return true;
          }
        }
        return false;
      },
    },
    watch:{
      'prodRiskRat.RatGrid':function(val){
        this.$set(this.$refs.updateGrid,'list',val);
      }

    }
  }
</script>

<style scoped>

</style>
