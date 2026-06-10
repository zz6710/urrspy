<template>
  <div>
    <k-form ref="detailDisclosureRuleForm" :data-col="2" data-label-width="130px" data-input-width="180px" :isFormBodyScreen="true">

      <div class ="tableLine" >
        <span class="leftText">规则信息</span>
        <div class="itemsCorn"></div>
      </div>

      <k-form-item label="信披规则名称" :data-col="2">
        <k-field-text v-model="formData.ruleName" :data-disabled="true" :data-max-length="60" />
      </k-form-item>
      <k-form-item label="信披类型">
        <k-field-select v-model="formData.disclosureType" data-dict="xp_doc_type" :data-disabled="true" />
      </k-form-item>
      <k-form-item label="信披子类型" v-if="formData.disclosureType=='5'||formData.disclosureType=='6'||formData.disclosureType=='1'||formData.disclosureType=='9'">
        <k-field-select v-model="formData.disclosureSonType" :data-disabled="true" data-dict="xp_son_type"/>
      </k-form-item>
      <k-form-item label="公告标题" :data-col="2">
        <k-field-text v-model="formData.noticeTitle" :data-disabled="true"
                      :data-max-length="200"/>
      </k-form-item>
      <k-form-item label="模板名称"  :data-col="2">
        <k-field-select v-model="formData.disclosureModId" :data-disabled="true" style="width:100%"
                        :data-data="modList"
                        data-display-field="t8DisclosureModName"
                        data-value-field="disclosureModId" />

        <!--        <k-btn class="md-info md-just-icon md-simple" data-descript="预览文档模板信息" data-size="small"-->
        <!--               :data-handler="previewPrintTempVersion">-->
        <!--          <md-icon>zoom_in</md-icon>-->
        <!--        </k-btn>-->
      </k-form-item>
      <k-form-item label="版本号">
        <k-field-select v-model="formData.disclosureModVersionId"  :data-disabled="true"
                        :data-data="formData.modVList"
                        data-display-field="versionNumber"
                        data-value-field="disclosureModVersionId"
                        @data-on-change="queryIsClearing"
        />
      </k-form-item>
      <k-form-item label="是否需要补录">
        <k-field-select v-model="formData.ifClearing" data-dict="xp_if_ok" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="是否复核">
        <k-field-select v-model="formData.ifCondition" data-dict="if_ok" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="任务发起方式" >
        <k-field-select v-model="formData.startRule" data-dict="xp_disclosure_start_rule" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="公告负责角色" key="noticeRoleid">
        <k-field-select v-model="formData.noticeRoleid" :data-disabled="true"
                        data-action="Role.findParents" data-display-field="rolename" data-value-field="roleid"/>
      </k-form-item>
      <k-form-item label="备注" :data-col="2">
        <k-field-text v-model="formData.remark" :data-allowblank="true" :data-disabled="true" :data-max-length="60"
                      inputType="textarea" :rows="3"/>
      </k-form-item>

      <div class ="tableLine" >
        <span class="leftText">产品参数</span>
        <div class="itemsCorn"></div>
      </div>


      <k-form-item label="产品形态" >
        <k-field-select v-model="formData.prodForm"  data-dict="xp_prod_form" :data-disabled="true" v-show="!changeMultiple" :data-multiple="false"/>
        <k-field-select v-model="formData.prodForm"  data-dict="xp_prod_form" :data-disabled="true" v-show="changeMultiple" :data-multiple="true"/>
      </k-form-item>
      <k-form-item label="投资周期维度" v-if="formData.prodForm == '06'">
        <k-field-select v-model="formData.invPrdDime"  data-dict="xp_cycle_dimension" :data-disabled="true" v-show="!changeMultiple" :data-multiple="false"/>
        <k-field-select v-model="formData.invPrdDime"  data-dict="xp_cycle_dimension" :data-disabled="true" v-show="changeMultiple" :data-multiple="true"/>
      </k-form-item>
      <k-form-item label="投资周期长度" v-if="formData.prodForm == '06'">
        <k-field-text v-model="formData.invPrdLen"  :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="销售对象" >
        <k-field-select v-model="formData.prodObj"  :data-disabled="true" data-dict="xp_target_customer" v-show="!changeMultiple" :data-multiple="false"/>
        <k-field-select v-model="formData.prodObj"  :data-disabled="true" data-dict="xp_target_customer" v-show="changeMultiple" :data-multiple="true"/>
      </k-form-item>
      <k-form-item label="募集方式" >
        <k-field-select
          v-model="formData.prodClcMth" data-dict="xp_raise_type" v-show="!changeMultiple" :data-multiple="false"
          :data-disabled="true">
        </k-field-select>
        <k-field-select
          v-model="formData.prodClcMth" data-dict="xp_raise_type" v-show="changeMultiple" :data-multiple="true"
          :data-disabled="true">
        </k-field-select>
      </k-form-item>
      <k-form-item label="产品投资性质" >
        <k-field-select
          v-model="formData.prodInvTyp" data-dict="xp_prod_invest_nature" v-show="!changeMultiple" :data-multiple="false"
          :data-disabled="true">
        </k-field-select>
        <k-field-select
          v-model="formData.prodInvTyp" data-dict="xp_prod_invest_nature" v-show="changeMultiple" :data-multiple="true"
          :data-disabled="true">
        </k-field-select>
      </k-form-item>
      <k-form-item label="产品系列"  :dataCol="2" key="prodSerCd">
        <k-field-select
          v-model="formData.prodSerCd"
          data-action="T8ProdInfo.getNewProdSeries" v-show="!changeMultiple" :data-multiple="false"
          data-value-field="seriesCode"
          data-display-field="seriesName"
          :data-disabled="true">
        </k-field-select>
        <k-field-select
          v-model="formData.prodSerCd"
          data-action="T8ProdInfo.getNewProdSeries" v-show="changeMultiple" :data-multiple="true"
          data-value-field="seriesCode"
          data-display-field="seriesName"
          :data-disabled="true">
        </k-field-select>
      </k-form-item>
      <k-form-item label="分级产品标志">
        <k-field-select  v-show="!changeMultiple"
                         v-model="formData.motherFundFlag" data-dict="rule_mother_fund_flag" :data-multiple="false"
                         :data-disabled="true">
        </k-field-select>
        <k-field-select  v-show="changeMultiple"
                         v-model="formData.motherFundFlag" data-dict="rule_mother_fund_flag" :data-multiple="true"
                         :data-disabled="true" >
        </k-field-select>
      </k-form-item>
      <div class ="tableLine" >
        <span class="leftText">日期节点</span>
        <div class="itemsCorn"></div>
      </div>

      <!--下拉单选字典展示切换框-->
      <k-form-item label="基准日期" v-show="showTimePlan">
        <k-field-select v-model="formData.baseDate"
                        data-dict = "xp_disclosure_base_date" :data-disabled="true"
                        :data-allowblank="true"/>
      </k-form-item>
      <!--下拉单选字典展示切换框2-->
      <k-form-item label="基准日期" v-show="showTimePlanTwo">
        <k-field-select v-model="formData.baseDate" :data-data="baseDateForDict"
                        data-display-field="itemval" data-value-field="itemkey"
                        :data-allowblank="true" :data-disabled="true"/>
      </k-form-item>
      <!--复选字典展示切换框-->
      <k-form-item label="基准日期" v-show="showNetValueBaseDate" :data-col="2">
        <k-field-checkbox  v-model="formData.netValueDate" data-dict="xp_disclosure_net_value_date"
                           :data-allowblank="true" :data-disabled="true" />
      </k-form-item>
      <k-form-item label="计划时间设置" :data-col="2" v-show="showDateConfigTable" >
        <div>
          <div>
            <span style="color: #F56C6C" ></span>计划生成日期 T
            <k-field-select v-model="formData.expCreateRule" data-dict="xp_disclosure_compute_date" style="width: 60px;"
                            data-placeholder="" :data-allowblank="expCreateRuleBlank"  :data-disabled="true"/>
            <el-input-number v-model="formData.expCreateDays" controls-position="right" :min="0"
                             :max="90" :precision="0" :step="1" disabled="disabled"></el-input-number>
            工作日
          </div>
          <div  v-show="showDateClear">
            <span style="color: #F56C6C"></span>补录完成日期 T
            <k-field-select v-model="formData.expSupplementRule" data-dict="xp_disclosure_compute_date"
                            style="width: 60px;" :data-disabled="true"
                            data-placeholder="" :data-allowblank="expSupplementRuleBlank"/>
            <el-input-number v-model="formData.expSupplementDays" controls-position="right" :min="0"
                             :max="90" :precision="0" :step="1" disabled="disabled"></el-input-number>
            工作日
          </div>
          <div v-show="showDateCondition">
            <span style="color: #F56C6C"></span>计划复核日期 T
            <k-field-select v-model="formData.expApprovalRule" data-dict="xp_disclosure_compute_date"
                            style="width: 60px;" :data-disabled="true"
                            data-placeholder="" :data-allowblank="expApprovalRuleBlank" />
            <el-input-number v-model="formData.expApprovalDays" controls-position="right" :min="0"
                             :max="90" :precision="0" :step="1" disabled="disabled"></el-input-number>
            工作日
          </div>
          <div >
          <span style="color: #F56C6C"></span>计划发布日期 T
            <k-field-select v-model="formData.expPublishRule" data-dict="xp_disclosure_compute_date"
                            style="width: 60px;" :data-allowblank="expPublishRuleBlank"
                            data-placeholder="" :data-disabled="true"/>
            <el-input-number v-model="formData.expPublishDays" controls-position="right" :min="0"
                             :max="90" :precision="0" :step="1" disabled="disabled"></el-input-number>
            工作日
          </div>
        </div>
      </k-form-item>

    </k-form>
  </div>
</template>

<script>
export default {
  props: {
    formData: {},
    disclosureType:'',
  },
  data() {
    return {
      formData: {
        modVList:{},
        isOrNot:'',
      },
      ifClearing:'',
      addDocTypeDict:{},
      showDateCondition: true,
      showDateClear: true,
      modList:{},
      itemkey:'',
      baseDate:{},
      showDateConfigTable: true,
      baseDateForDict:{},
      trusteeExamineFlag: true,
      showProd: true,//是否展示选择产品
      showDiscloSonType: false,//是否展示信披子类型
      disclosureSonTypes: {},//信披子类型
      noticeTitles: {},//公告标题
      showBusType: true,//是否显示变更业务类型
      showTimePlan: true,//是否显示基准时间与计划时间设置
      showTimePlanTwo: false,//是否显示基准时间与计划时间设置
      busSonTypes: {},//业务变更子类型
      prodAllowBlank: false,//产品是否可以为空
      busTypeBlank: false,//业务变更类型是否可以为空
      busSonTypeBlank: false,//子业务类型是否可以为空
      baseDateBlank: false,//基准日期是否可以为空
      baseDateBlank2: true,//基准日期是否可以为空
      expCreateRuleBlank: false,
      expSupplementRuleBlank: false,
      expApprovalRuleBlank: false,
      expPublishRuleBlank: false,
      showNetValueBaseDate: false,//是否展示复选基准日期  默认不显示，选择净值公告时，才展示
      netBaseDateBlank: true,//净值披露基准日期与规则是否可以为空   默认可以为空，选择净值公告时，不可为空
      showTrusteeExamine: true,//是否显示托管行审核
    };
  },
  computed: {
    changeMultiple(){
      if(this.formData.disclosureSonType==='0903'||this.formData.disclosureType==='6') {
        return true;
      }
    },
  },
  watch: {

    //监听产品形态
    'formData.prodForm'(value) {
      this.$set(this.formData, 'invPrdDime', '');
      this.$set(this.formData, 'invPrdLen', '');
    },
    //监听版本
    'formData.disclosureModVersionId'(value) {
      if (this.formData.disclosureModVersionId === ''||this.formData.disclosureModVersionId==null){
        this.$set(this.formData, 'ifClearing', '');
      }else{
        this.queryIsClearing()
      }
    },
    //监听模板
    'formData.disclosureModId'(value) {
      if (this.formData.disclosureModId == ''||this.formData.disclosureModId==null){
        this.disclosureModVersionId(value);
      }else{
        //切换下拉选择时滞空前数据
        this.$set(this.formData, 'disclosureModVersionId', '');
        this.$set(this.formData, 'modVList', '');
        this.queryVersion()
      }
    },
    //监听信披类型
    'formData.disclosureType'(value) {
      this.$set(this.formData, 'disclosureSonType', '');
      this.$set(this.formData, 'disclosureModId', '');
      this.$set(this.formData, 'netValueDate', '');
      this.selectDisclosureType();
      //change之前还原状态
      this.netBaseDateBlank = true;
      this.baseDateBlank = false;
      this.baseDateBlank2 = true;
      this.showDateConfigTable = true;
      this.showNetValueBaseDate = false;
      this.baseDateForDict=[];
      this.$set(this.formData, 'baseDate', '');
      this.showTimePlan = true;
      this.showTimePlanTwo = false;
      if (this.formData.disclosureType==='7'||this.formData.disclosureType==='8'||this.formData.disclosureType==='11'||this.formData.disclosureType==='9'){
        this.baseDateBlank = true;
        this.baseDateBlank2 = true;
        this.showDateConfigTable = false;
        this.showTimePlan = false;
        this.$set(this.formData, 'expCreateRule', '2');
        this.$set(this.formData, 'expCreateDays', '0');
        this.$set(this.formData, 'expSupplementRule', '2');
        this.$set(this.formData, 'expSupplementDays', '0');
        this.$set(this.formData, 'expApprovalRule', '2');
        this.$set(this.formData, 'expApprovalDays', '0');
        this.$set(this.formData, 'expPublishRule', '2');
        this.$set(this.formData, 'expPublishDays', '0');
      }else if (this.formData.disclosureType==='1'){
        this.$set(this.formData, 'baseDate', '01');
        this.baseDateBlank = false;
        this.baseDateBlank2 = true;
      }else if (this.formData.disclosureType==='12'){
        this.$set(this.formData, 'baseDate', '13');
        this.baseDateBlank = false;
        this.baseDateBlank2 = true;
      }else if (this.formData.disclosureType==='2'){
        this.$set(this.formData, 'baseDate', '02');
        this.baseDateBlank2 = false;
        this.baseDateBlank = true;
      }else if (this.formData.disclosureType==='3'){
        this.$set(this.formData, 'baseDate', this.formData.baseDate);
        this.baseDateBlank2 = false;
        this.baseDateBlank = true;
      }else if (this.formData.disclosureType==='13'){
        this.$set(this.formData, 'baseDate', '11');
        this.baseDateBlank2 = false;
        this.baseDateBlank = true;
      }else if (this.formData.disclosureType==='5'){
        this.showTimePlan =false;
        this.showTimePlanTwo =true;
        this.baseDateBlank2 =true;
        this.baseDateBlank = false;
        this.itemkey='06,07,08,09'
        this.httpUtil.comnQuery({
          action: "T8Dict.findBaseDate",
          params: {dict:'xp_disclosure_base_date',itemkey:this.itemkey}
        }).then(data => {
          this.baseDateForDict=data.rows
        }).catch({})
      }else if (this.formData.disclosureType==='6'){
        this.showTimePlan = false;
        this.showTimePlanTwo = true;
        this.baseDateBlank2 =true;
        this.baseDateBlank = false;
        this.itemkey='08,09'
        this.httpUtil.comnQuery({
          action: "T8Dict.findBaseDate",
          params: {dict:'xp_disclosure_base_date',itemkey:this.itemkey}
        }).then(data => {
          this.baseDateForDict=data.rows
        }).catch({})
      }else{
        this.showTimePlan = true;
        this.showTimePlanTwo = false;
      }
      this.setNetValueDateShow();
    },
    //监听信披子类型
    'formData.disclosureSonType'(value) {
      this.$set(this.formData, 'disclosureModId', '');
    }
  },
  created() {
    this.queryIsClearing();
    this.$set(this.formData, 'modVList', '');
    this.queryVersion();
    this.selectDisclosureType();
    this.prodChange();
    this.previewPrintTempVersion();
    if (this.formData.disclosureType==='7'||this.formData.disclosureType==='8'||this.formData.disclosureType==='11') {
      this.baseDateBlank = true;
      this.baseDateBlank2 = true;
      this.showDateConfigTable = false;
      this.showTimePlan = false;
    }
    if(this.formData.disclosureType==='9'){
      this.baseDateBlank = true;
      this.baseDateBlank2 = true;
      this.showDateConfigTable = true;
      this.showTimePlan = false;
    }
    this.changeIfClearing();
    this.changeIfCondition();
    this.setNetValueDateShow();
  },
  methods: {
    //补录T+日期联动
    changeIfClearing() {
      this.showDateClear= true;
      if (this.formData.ifClearing==='0'){
        this.showDateClear= false;
      }
    },
    //复核T+日期联动
    changeIfCondition() {
      this.showDateCondition=true;
      if (this.formData.ifCondition==='0'){
        this.showDateCondition=false;
      }
    },
    queryIsClearing() {
      //是否补录
      this.httpUtil.comnQuery({
        action: "DisclosureRule.clearingOrNot",
        params: {disclosureModVersionId: this.formData.disclosureModVersionId}
      }).then(data => {
        let rows = data.rows;
        if (rows && rows.length === 1) {
          this.$set(this.formData, 'ifClearing', rows[0].ifClearing);
        }
      }).catch({})
    },
    queryVersion() {
      //获取版本号
      this.httpUtil.comnQuery({
        action: "DisclosureRule.findDisclosureModsVWithRule",
        params: {disclosureModId: this.formData.disclosureModId}
      }).then(data => {
        this.formData.modVList = data.rows;
      }).catch({})
    },
    selectDisclosureType() {
      //获取模板信息
      this.httpUtil.comnQuery({
        action: "DisclosureRule.findDisclosureModsWithRule",
        params: {disclosureType: this.formData.disclosureType,
          disclosureSonType: this.formData.disclosureSonType
        }
      }).then(data => {
        this.modList = data.rows;
      }).catch({})
      //请求信披字典
      this.httpUtil.comnQuery({
        action: "DisclosureMod.getXPTypeByDocType",
        params: {disclosureType: this.formData.disclosureType}
      }).then(data => {
        this.addDocTypeDict = data.rows;
      }).catch({})
      //基准日期渲染
      this.httpUtil.comnQuery({
        action: "T8Dict.findBaseDate",
        params: {dict:'xp_disclosure_base_date',itemkey:this.itemkey}
      }).then(data => {
        this.baseDate=data.rows
      }).catch({})
    },
    //清空版本框
    disclosureModVersionId(value) {
      this.$set(this.formData, 'disclosureModVersionId', '');
      this.$set(this.formData, 'modVList', '');
    },
    prodChange(params) {
      //获取模板信息
      this.httpUtil.comnQuery({
        action: "DisclosureRule.findDisclosureModsWithRule",
        params: {disclosureType: this.formData.disclosureType,
          disclosureSonType: this.formData.disclosureSonType
        }
      }).then(data => {
        this.modList = data.rows;
      }).catch({})
    },
    //展示净值披露基准日期
    setNetValueDateShow(value) {
      if(this.formData.disclosureType==='9'){
        this.showNetValueBaseDate = true;
        this.netBaseDateBlank = false;
      }
    },
    previewPrintTempVersion() {
      var versionId = this.formData.disclosureModId;
      this.httpUtil.comnQuery({
        action: 'DisclosureModColumn.getMaxXPVersionId',
        params: {disclosureModId: versionId}
      }).then(data => {
        this.$nextTick(() => {
          if (data != null && data.rows.length > 0) {

            let url = data.rows[0].viewUrl;
            window.open(url, '_blank', 'width=1000,height=800,toolbars=yes,resizable=yes,scrollbars=yes,left=20,top=30');
          }
        })
      }).catch({})
    }
  }
};
</script>



<style scoped>

.tableLine {
  margin: 5% 0 3% 10%;
  border-top: 1px dotted rgba(2, 2, 2, 0.34);
  width: 100%;
  position: relative;
  font-size: 14px;
}

.itemsCorn {
  position: absolute;
  background: #41A0EB;
  border-radius: 0px;
  left: -5.5%;
  width: 7px;
  height: 15px;
  transform: translateX(-50%) translateY(-50%);
}
.leftText {
  position: absolute;
  left: 0;
  background-color: #ffffff;
  font-weight: 300;
  padding: 0 15px;
  transform: translateX(-50%) translateY(-50%);
}
</style>
