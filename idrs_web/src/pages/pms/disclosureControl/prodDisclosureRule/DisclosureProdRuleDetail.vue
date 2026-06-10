<template>
  <div>
    <k-form ref="addDisclosureProdRuleForm" :data-col="2" data-label-width="130px" data-input-width="180px" :isFormBodyScreen="true">


      <k-form-item>
      </k-form-item>

      <div class ="tableLine" >
        <span class="leftText">产品信息</span>
        <div class="itemsCorn"></div>
      </div>

      <k-form-item label="产品名称" data-col="2">
        <k-field-select v-model="formData.prodCode" data-action="T8ProdInfo.getProdInfosAndCode" data-value-field="prodCode"
                        data-display-field="prodCode,prodName" :dataAllowblank="true" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品全称" data-col="2">
        <k-field-text v-model="formData.prodFullName" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品形态">
        <k-field-select v-model="formData.prodForm" data-dict="xp_prod_form" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="投资周期维度">
        <k-field-select v-model="formData.invProDime" data-dict="t8_inv_prd_dime" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="投资周期长度">
        <k-field-text v-model="formData.invPrdLed" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="销售对象">
        <k-field-select v-model="formData.prodObj" data-dict="xp_target_customer" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="募集方式">
        <k-field-select v-model="formData.prodClcMth" data-dict="xp_raise_type" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品投资性质">
        <k-field-select v-model="formData.prodInvTyp" data-dict="xp_prod_invest_nature" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品系列">
        <k-field-text v-model="formData.prodSerNm"  :data-disabled="true"/>
      </k-form-item>

      <div class ="tableLine" >
        <span class="leftText">规则信息</span>
        <div class="itemsCorn"></div>
      </div>

      <k-form-item label="信披类型">
        <k-field-select v-model="formData.disclosureType" data-dict="xp_doc_type" :data-disabled="true" :data-allowblank="true"
        />
      </k-form-item>
      <k-form-item label="信披子类型" v-if="formData.disclosureType=='5'||formData.disclosureType=='6'||formData.disclosureType=='1'||formData.disclosureType=='9'">
        <k-field-select v-model="formData.disclosureSonType" :data-allowblank="true" :data-data="formData.addDocTypeDict"
                        data-display-field="text" data-value-field="value" :data-disabled="true"/>
      </k-form-item>

      <k-form-item label="公告标题" :data-col="2">
        <k-field-text v-model="formData.noticeTitle" data-display-field="label" data-value-field="value"
                      :data-data="noticeTitles" :data-max-length="200" :data-allowblank="true" :data-disabled="true"/>
      </k-form-item>
      <!--      <template v-if="pageSource=='add'||pageSource=='update'">-->
      <k-form-item label="模板名称" :data-col="2">
        <k-field-select v-model="formData.disclosureModId" :data-allowblank="true" style="width:100%"
                        :data-data="formData.modList"
                        data-display-field="t8DisclosureModName" :data-disabled="true"
                        data-value-field="disclosureModId" ref="versionSelect" />
      </k-form-item>
      <k-form-item label="版本号">
        <k-field-select v-model="formData.disclosureModVersionId"  :data-allowblank="true"
                        :data-data="formData.modVList" :data-disabled="true"
                        data-display-field="versionNumber"
                        data-value-field="disclosureModVersionId" ref="versionSelect"/>
      </k-form-item>
      <k-form-item label="是否需要补录">
        <k-field-select v-model="formData.ifClearing" data-dict="xp_if_ok" :data-disabled="true" />
      </k-form-item>
      <k-form-item label="是否复核">
        <k-field-select v-model="formData.ifCondition" data-dict="if_ok"  :data-allowblank="true" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="任务发起方式" >
        <k-field-select v-model="formData.startRule" data-dict="xp_disclosure_start_rule" :data-disabled="true"  :data-allowblank="true"/>
      </k-form-item>
      <k-form-item label="公告负责角色" key="noticeRoleid">
        <k-field-select v-model="formData.noticeRoleid" :data-allowblank="true"
                        data-action="Role.findParents" data-display-field="rolename" data-value-field="roleid" :data-disabled="true"/>
      </k-form-item>
      <!--      </template>-->
      <k-form-item label="渠道名称" :dataCol="2">
        <k-field-select v-model="formData.channelIds"  data-action="DisclosureChannel.findDisChannel" :data-disabled="true" :data-allowblank="true" data-value-field="id" data-display-field="channelName"  :data-multiple="true"></k-field-select>
      </k-form-item>
      <k-form-item label="备注" :data-col="2">
        <k-field-text v-model="formData.remark" :data-allowblank="true" :data-max-length="60" :data-disabled="true"
                      inputType="textarea" :rows="3"/>
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

                        data-display-field="itemval" :data-disabled="true" data-value-field="itemkey"
                        :data-allowblank="true"/>
      </k-form-item>
      <!--复选字典展示切换框-->
      <k-form-item label="基准日期" v-show="showNetValueBaseDate" :data-col="2">
        <k-field-checkbox v-model="formData.netValueDate" data-dict="disclosure_net_value_date"
                          :data-allowblank="true" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="计划时间设置" :data-col="2" v-show="showDateConfigTable">
        <div>
          <div>
            <span style="color: #F56C6C">*</span>计划生成日期 T
            <k-field-select v-model="formData.expCreateRule" data-dict="xp_disclosure_compute_date" style="width: 60px;"
                            data-placeholder="" :data-allowblank="true"  :data-disabled="true"/>
            <el-input-number v-model="formData.expCreateDays" controls-position="right" :min="0"
                             :max="90" :precision="0" :step="1" disabled="disabled"></el-input-number>
            工作日
          </div>
          <div v-show="showDateClear">
            <span style="color: #F56C6C">*</span>补录完成日期 T
            <k-field-select v-model="formData.expSupplementRule" data-dict="xp_disclosure_compute_date"
                            style="width: 60px;" :data-disabled="true"
                            data-placeholder="" :data-allowblank="true" />
            <el-input-number v-model="formData.expSupplementDays" controls-position="right" :min="0"
                             :max="90" :precision="0" :step="1" disabled="disabled"></el-input-number>
            工作日
          </div>
          <div v-show="showDateCondition">
            <span style="color: #F56C6C">*</span>计划审批日期 T
            <k-field-select v-model="formData.expApprovalRule" data-dict="xp_disclosure_compute_date"
                            style="width: 60px;" :data-disabled="true"
                            data-placeholder="" :data-allowblank="true" />
            <el-input-number v-model="formData.expApprovalDays" controls-position="right" :min="0"
                             :max="90" :precision="0" :step="1" disabled="disabled"></el-input-number>
            工作日
          </div>
          <div>
          <span style="color: #F56C6C">*</span>计划发布日期 T
            <k-field-select v-model="formData.expPublishRule" data-dict="xp_disclosure_compute_date"
                            style="width: 60px;" :data-allowblank="true" :data-disabled="true"
                            data-placeholder="" />
            <el-input-number v-model="formData.expPublishDays" controls-position="right" :min="0"
                             :max="90" :precision="0" :step="1" disabled="disabled" ></el-input-number>
            工作日
          </div>
        </div>
      </k-form-item>

      <k-form-footer data-align="center">

      </k-form-footer>
    </k-form>
  </div>
</template>

<script>
import Tools from "@/utils/tools";
export default {
  // components: {
  //   DisclosureProdRuleDetail
  // },
  props: {
    disclosureType: '',
    disclosureSonType: '',
    disclosureModVersionId: '',
    formData: {},
  },
  data() {
    return {
      formData: {
        addDocTypeDict: {},
        modList: {},
        modVList: {},
        disclosureType: '',
      },
      baseDateBlank: false,//基准日期是否可以为空
      showTimePlan: true,//是否显示基准时间与计划时间设置
      showTimePlanTwo: false,//是否显示基准时间与计划时间设置
      baseDateForDict:{},
      baseDate:{},
      showDateConfigTable: true,
      ruleData: {},
      flag: "",
      noticeTitles: {},//公告标题
      docVersion: {},//模板子版本
      sonType: {},//子类型
      disclosureSonTypes: {},//信披子类型
      // queryRoleGraphql: "{queryRole(action:\"findChildren\",roleids:\"1\") {rows{roleid, rolename, parentroleid, descript},results}}",
    };
  },
  watch: {
    //监听信披类型
    'formData.disclosureType'(value) {
      //发起时间选框变更
      this.showTimePlan = true;
      this.showTimePlanTwo = false;
      this.showDateConfigTable = true;
      this.baseDateForDict=[];
      this.showNetValueBaseDate=false;
      //change前滞空选框
      this.$set(this.formData, 'baseDate', '');
      this.$set(this.formData, 'disclosureModId', '');
      this.$set(this.formData, 'disclosureSonType', '');
      this.$set(this.formData, 'addDocTypeDict', '');
      this.$set(this.formData, 'modList', '');
      this.$set(this.formData, 'disclosureModVersionId', '');
      this.$set(this.formData, 'modVList', '');
      this.$set(this.formData, 'ifClearing', '');

      if (this.formData.disclosureType!==undefined&&this.formData.disclosureType !== ''&&this.formData.disclosureType!==null) {
        this.findSonTypeByType(value);
        this.changeDisRule();
        this.findSonVersion(value);
      }
      if (this.formData.disclosureType==7||this.formData.disclosureType==8||this.formData.disclosureType==11||this.formData.disclosureType==9){
        this.showDateConfigTable = false;
        this.showTimePlan = false;
      }else if (this.formData.disclosureType==1){
        this.$set(this.formData, 'baseDate', '01');
        this.baseDateDisabled = true;
      }else if (this.formData.disclosureType==12){
        this.$set(this.formData, 'baseDate', '13');
        this.baseDateDisabled = true;
      }else if (this.formData.disclosureType==2){
        this.$set(this.formData, 'baseDate', '02');
        this.baseDateDisabled = true;
      }else if (this.formData.disclosureType==3){
        this.$set(this.formData, 'baseDate', this.formData.baseDate);
        this.baseDateDisabled = true;
      }else if (this.formData.disclosureType==13){
        this.$set(this.formData, 'baseDate', '11');
        this.baseDateDisabled = true;
      }else if (this.formData.disclosureType==5){
        this.showTimePlan =false;
        this.showTimePlanTwo =true;
        this.baseDateDisabled = false;
        this.itemkey='06,07,08,09'
        this.httpUtil.comnQuery({
          action: "T8Dict.findBaseDate",
          params: {dict:'xp_disclosure_base_date',itemkey:this.itemkey}
        }).then(data => {
          this.baseDateForDict=data.rows
        }).catch({})
      }else if (this.formData.disclosureType==6){
        this.showTimePlan = false;
        this.showTimePlanTwo = true;
        this.baseDateDisabled = false;
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
        this.baseDateDisabled = false;
      }
    },
    //监听子信披类型
    'formData.disclosureSonType'(value) {
      //change前滞空选框
      this.$set(this.formData, 'disclosureModId', '');
      this.$set(this.formData, 'modList', '');
      this.$set(this.formData, 'disclosureModVersionId', '');
      this.$set(this.formData, 'modVList', '');
      this.$set(this.formData, 'ifClearing', '');
      if (this.formData.disclosureSonType!==undefined&&this.formData.disclosureSonType !== ''&&this.formData.disclosureSonType!==null){
        this.findSonVersion(value);
        this.changeDisRule()
      }
    },
    //监听模板名称
    'formData.disclosureModId'(value) {
      //change前滞空选框
      this.$set(this.formData, 'disclosureModVersionId', '');
      this.$set(this.formData, 'modVList', '');
      this.$set(this.formData, 'ifClearing', '');
      if (value!==undefined&&value !== ''&&value!==null) {
        this.queryVersion(value);
      }
    },
    //监听版本号
    'formData.disclosureModVersionId'(value) {
      //change前滞空选框
      this.$set(this.formData, 'ifClearing', '');
      if (value!==undefined&&value !== ''&&value!==null) {
        this.queryIsClearing(value);
      }
    },
    //监听产品名称
    'formData.prodCode'(value) {
      if (value!==undefined&&value !== ''&&value!==null){
        //监听产品名称获取对应产品信息
        this.findBassInfoByProdCode();
        this.changeDisRule()
      }
    },
  },
  created() {
    this.findBassInfoByProdCode();
    this.queryVersion();
    this.findSonVersion();
    this.findSonTypeByType();
    if (this.formData.disclosureType==7||this.formData.disclosureType==8||this.formData.disclosureType==11){
      this.baseDateBlank = true;
      this.expCreateRuleBlank = true;
      this.showDateConfigTable = false;
      this.showTimePlan = false;
    }
    if(this.formData.disclosureType==9){
      this.baseDateBlank = true;
      this.expCreateRuleBlank = true;
      this.showDateConfigTable = true;
      this.showTimePlan = false;
    }
    this.changeIfClearing();
    this.changeIfCondition();
    this.setNetValueDateShow();
  },
  methods: {
    //展示净值披露基准日期
    setNetValueDateShow() {
      if(this.formData.disclosureType==='9'){
        this.showNetValueBaseDate = true;
        this.netBaseDateBlank = false;
      }
    },
    //复核
    changeIfCondition() {
      this.showDateCondition=true;
      if (this.formData.ifCondition==='0'){
        this.showDateCondition=false;
      }

    },
    //补录
    changeIfClearing() {
      this.showDateClear= true;
      if (this.formData.ifClearing==='0'){
        this.showDateClear= false;
      }
    },
    //请求版本号
    queryVersion(value) {
      this.httpUtil.comnQuery({
        action: "DisclosureRule.findDisclosureModsVWithRule",
        params: {disclosureModId: this.formData.disclosureModId}
      }).then(data => {
        // this.formData.modVList = data.rows;
        this.$set(this.formData, 'modVList', data.rows);
      }).catch({})

    },
    //请求是否补录
    queryIsClearing(value) {
      this.httpUtil.comnQuery({
        action: "DisclosureRule.clearingOrNot",
        params: {disclosureModVersionId: this.formData.disclosureModVersionId}
      }).then(data => {
        let rows = data.rows;
        if (rows && rows.length == 1) {
          this.$set(this.formData, 'ifClearing', rows[0].ifClearing);
        }
      }).catch({})
    },
    //根据信披类型查询信披模板子版本
    findSonVersion(value) {
      this.httpUtil.comnQuery({
        action: "DisclosureRule.findDisclosureModsWithRule",
        params: {disclosureType: this.formData.disclosureType,
          disclosureSonType: this.formData.disclosureSonType
        }
      }).then(data => {
        // this.formData.modList = data.rows;
        this.$set(this.formData, 'modList', data.rows);
      }).catch({})
    },
    //根据信披类型查询信披子类型
    findSonTypeByType(value) {
      //请求信披字典
      this.httpUtil.comnQuery({
        action: "DisclosureMod.getXPTypeByDocType",
        params: {disclosureType: this.formData.disclosureType}
      }).then(data => {
        // this.formData.addDocTypeDict = data.rows;
        this.$set(this.formData, 'addDocTypeDict', data.rows);
      }).catch({})
    },
    //监听产品名称获取对应产品信息
    findBassInfoByProdCode() {
      this.httpUtil.comnQuery({
        action: "DisclosureProdRule.findBassInfoByProdCode",
        params: {prodCode: this.formData.prodCode}
      }).then(data => {
        if (data.success){
          this.formData.prodForm =data.prodForm;
          this.formData.invProDime =data.invProDime;
          this.formData.invPrdLed =data.invPrdLed;
          this.formData.prodObj =data.prodObj;
          this.formData.prodClcMth =data.prodClcMth;
          this.formData.prodInvTyp =data.prodInvTyp;
          this.formData.prodSerNm =data.prodSerNm;
        }
      })
    },
    changeDisRule(value) {
      this.httpUtil.comnQuery({
        action:'DisclosureProdRule.findRulesByDisclosureForExist',
        params: {
          disclosureType: this.formData.disclosureType,
          disclosureSonType: this.formData.disclosureSonType,
          prodCode: this.formData.prodCode,
        }
      }).then(data => {
        if (data.success) {
        }else{
          Tools.alert(data.returnmsg,"danger");
        }
      });
    },

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
