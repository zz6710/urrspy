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
        <k-field-select v-model="formData.prodCode" data-action="T8ProdInfo.getProdInfosAndCode" data-value-field="prodCode"  @data-on-change="findBassInfoByProdCode"changeDisRule
                        data-display-field="prodCode,prodName" :dataAllowblank="false" :data-max-length="100"/>
      </k-form-item>
      <k-form-item label="产品全称" data-col="2">
        <k-field-text v-model="formData.prodFullName" :dataAllowblank="false" :data-max-length="125"/>
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
        <k-field-select v-model="formData.disclosureType" :data-data="DocTypeDict"   data-value-field="value" data-display-field="text" :data-disabled="false" :data-allowblank="false"
        />
      </k-form-item>
      <k-form-item label="信披子类型" v-if="formData.disclosureType=='5'||formData.disclosureType=='6'||formData.disclosureType=='1'||formData.disclosureType=='9'">
        <k-field-select v-model="formData.disclosureSonType" :data-allowblank="false" :data-data="formData.addDocTypeDict"
                        data-display-field="text" data-value-field="value"/>
      </k-form-item>

      <k-form-item label="公告标题" :data-col="2">
        <k-field-text v-model="formData.noticeTitle" :data-allowblank="false" :data-disabled="false" :data-max-length="100" />
      </k-form-item>
      <!--      <template v-if="pageSource=='add'||pageSource=='update'">-->
      <k-form-item label="模板名称" :data-col="2">
        <k-field-select v-model="formData.disclosureModId" :data-allowblank="false" style="width:100%"
                        :data-data="formData.modList"
                        data-display-field="t8DisclosureModName"
                        data-value-field="disclosureModId" ref="versionSelect"/>
      </k-form-item>
      <k-form-item label="版本号">
        <k-field-select v-model="formData.disclosureModVersionId"  :data-allowblank="false"
                        :data-data="formData.modVList"
                        data-display-field="versionNumber"
                        data-value-field="disclosureModVersionId" ref="versionSelect"/>
      </k-form-item>
      <k-form-item label="是否需要补录">
        <k-field-select v-model="formData.ifClearing" data-dict="xp_if_ok" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="是否复核">
        <k-field-select v-model="formData.ifCondition" data-dict="if_ok" :data-disabled="false" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="任务发起方式" >
        <k-field-select v-model="formData.startRule" data-dict="xp_disclosure_start_rule" data-default-value="1"
                        :data-disabled="true" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="公告负责角色" key="noticeRoleid">
        <k-field-select v-model="formData.noticeRoleid" :data-allowblank="false"
                        data-action="Role.findParents" data-display-field="rolename" data-value-field="roleid"/>
      </k-form-item>
      <!--      </template>-->
      <k-form-item label="渠道名称" :data-col="2" >
        <k-field-select v-model="formData.channelIds"  data-action="DisclosureChannel.findDisChannel" :data-allowblank="false" data-value-field="id" data-display-field="channelName"  :data-multiple="true"></k-field-select>
      </k-form-item>
      <k-form-item label="备注" :data-col="2">
        <k-field-text v-model="formData.remark" :data-allowblank="true" :data-max-length="256"
                      inputType="textarea" :rows="3"/>
      </k-form-item>

      <div class ="tableLine" >
        <span class="leftText">日期节点</span>
        <div class="itemsCorn"></div>
      </div>


      <!--下拉单选字典展示切换框-->
      <k-form-item label="基准日期" v-show="showTimePlan">
        <k-field-select v-model="formData.baseDate"
                        data-dict = "xp_disclosure_base_date" :data-disabled="baseDateDisabled"
                        :data-allowblank="baseDateBlank"/>
      </k-form-item>
      <!--下拉单选字典展示切换框2-->
      <k-form-item label="基准日期" v-show="showTimePlanTwo">
        <k-field-select v-model="formData.baseDate" :data-data="baseDateForDict"
                        :data-disabled="baseDateDisabled"
                        data-display-field="itemval" data-value-field="itemkey"
                        :data-allowblank="baseDateBlank2"/>
      </k-form-item>
      <!--复选字典展示切换框-->
      <k-form-item label="基准日期" v-show="showNetValueBaseDate" :data-col="2">
        <k-field-checkbox v-model="formData.netValueDate" data-dict="xp_disclosure_net_value_date"
                          :data-allowblank="netBaseDateBlank"/>
      </k-form-item><k-form-item label="计划时间设置" :data-col="2" v-show="showDateConfigTable">
      <div>
        <div>
          <span style="color: #F56C6C">*</span>计划生成日期 T
          <k-field-select v-model="formData.expCreateRule" data-dict="xp_disclosure_compute_date" style="width: 60px;" :data-allowblank="blankExpCreate"
                          data-placeholder=""  data-default-value="2"/>
          <input-number-controller v-model="formData.expCreateDays" controls-position="right" :min="0" :data-allowblank="blankExpCreate"
                                   :max="90" :precision="0" :step="1" ref="expCreateDays"></input-number-controller>
          工作日
        </div>
        <div v-show="showDateClear">
          <span style="color: #F56C6C">*</span>补录完成日期 T
          <k-field-select v-model="formData.expSupplementRule" data-dict="xp_disclosure_compute_date"
                          style="width: 60px;" :data-allowblank="blankExpSupplement" :data-disabled="expSupplementDisabled"
                          data-placeholder=""  data-default-value="2"/>
          <input-number-controller v-model="formData.expSupplementDays" controls-position="right" :data-allowblank="blankExpSupplement"
                                   :min="showDateClear?(formData.expSupplementRule==='2'&&formData.expCreateRule==='2'?formData.expCreateDays:(formData.expSupplementRule==='1'&&formData.expCreateRule==='1'?0:0)):0"
                                   :setDisplayValue="showDateClear?(formData.expSupplementRule==='2'&&formData.expCreateRule==='2'?(formData.expCreateDays > formData.expSupplementDays):(formData.expSupplementRule==='1'&&formData.expCreateRule==='1'?(formData.expCreateDays < formData.expSupplementDays):false)):false"
                                   :toValue="showDateClear?(formData.expSupplementRule==='2'&&formData.expCreateRule==='2'?(formData.expCreateDays > formData.expSupplementDays?formData.expCreateDays:null):(formData.expSupplementRule==='1'&&formData.expCreateRule==='1'?(formData.expCreateDays < formData.expSupplementDays?formData.expCreateDays:null):null)):null"
                                   :max="showDateClear?(formData.expSupplementRule==='2'&&formData.expCreateRule==='2'?90:(formData.expSupplementRule==='1'&&formData.expCreateRule==='1'?formData.expCreateDays:90)):90"
                                   :precision="0" :step="1" ref="expSupplementDays"></input-number-controller>
          工作日
        </div>
        <div v-show="showDateCondition">
          <span style="color: #F56C6C">*</span>计划复核日期 T
          <k-field-select v-model="formData.expApprovalRule" data-dict="xp_disclosure_compute_date"
                          style="width: 60px;" :data-allowblank="blankExpApproval" :data-disabled="expApprovalDisabled"
                          data-placeholder=""  data-default-value="2"/>
          <input-number-controller v-model="formData.expApprovalDays" controls-position="right" :data-allowblank="blankExpApproval"
                                   :min="showDateCondition?(showDateClear?(formData.expApprovalRule==='2'&&formData.expSupplementRule==='2'?formData.expSupplementDays
                                        :(formData.expApprovalRule==='1'&&formData.expSupplementRule==='1'?0:0)):(formData.expApprovalRule==='2'&&formData.expCreateRule==='2'?formData.expCreateDays:(formData.expApprovalRule==='1'&&formData.expCreateRule==='1'?0:0))):0"
                                   :setDisplayValue="showDateCondition?(showDateClear?(formData.expApprovalRule==='2'&&formData.expSupplementRule==='2'?(formData.expSupplementDays > formData.expApprovalDays)
                                        :(formData.expApprovalRule==='1'&&formData.expSupplementRule==='1'?(formData.expSupplementDays < formData.expApprovalDays):false)):(formData.expApprovalRule==='2'&&formData.expCreateRule==='2'?(formData.expCreateDays > formData.expApprovalDays)
                                        :(formData.expApprovalRule==='1'&&formData.expCreateRule==='1'?(formData.expCreateDays < formData.expApprovalDays):false))):false"
                                   :toValue="showDateCondition?(showDateClear?(formData.expApprovalRule==='2'&&formData.expSupplementRule==='2'?(formData.expSupplementDays > formData.expApprovalDays?formData.expSupplementDays:null)
                                        :(formData.expApprovalRule==='1'&&formData.expSupplementRule==='1'?(formData.expSupplementDays < formData.expApprovalDays?formData.expSupplementDays:null):null))
                                        :(formData.expApprovalRule==='2'&&formData.expCreateRule==='2'?(formData.expCreateDays > formData.expApprovalDays?formData.expCreateDays:null):(formData.expApprovalRule==='1'&&formData.expCreateRule==='1'?(formData.expCreateDays < formData.expApprovalDays?formData.expCreateDays:null):null))):null"
                                   :max="showDateCondition?(showDateClear?(formData.expApprovalRule==='2'&&formData.expSupplementRule==='2'?90:(formData.expApprovalRule==='1'&&formData.expSupplementRule==='1'?formData.expSupplementDays:90))
                                        :(formData.expApprovalRule==='2'&&formData.expCreateRule==='2'?90:(formData.expApprovalRule==='1'&&formData.expCreateRule==='1'?formData.expCreateDays:90))):90"
                                   :precision="0" :step="1"  ref="expApprovalDays"></input-number-controller>
          工作日
        </div>
        <div >
          <span style="color: #F56C6C">*</span>计划发布日期 T
          <k-field-select v-model="formData.expPublishRule" data-dict="xp_disclosure_compute_date"
                          style="width: 60px;" :data-allowblank="blankExpPublish" :data-disabled="expPublishDisabled"
                          data-placeholder="" data-default-value="2"/>
          <input-number-controller v-model="formData.expPublishDays" controls-position="right" :data-allowblank="blankExpPublish"
                                   :min="showDateCondition?(formData.expPublishRule==='2'&&formData.expApprovalRule==='2'?formData.expApprovalDays:(formData.expPublishRule==='1'&&formData.expApprovalRule==='1'?0:0))
                                        :(showDateClear?(formData.expPublishRule==='2'&&formData.expSupplementRule==='2'?formData.expSupplementDays:(formData.expPublishRule==='1'&&formData.expSupplementRule==='1'?0:0))
                                        :(formData.expPublishRule==='2'&&formData.expCreateRule==='2'?formData.expCreateDays:(formData.expPublishRule==='1'&&formData.expCreateRule==='1'?0:0)))"
                                   :setDisplayValue="showDateCondition?(formData.expPublishRule==='2'&&formData.expApprovalRule==='2'?(formData.expApprovalDays > formData.expPublishDays):(formData.expPublishRule==='1'&&formData.expApprovalRule==='1'?(formData.expApprovalDays < formData.expPublishDays):false))
                                        :(showDateClear?(formData.expPublishRule==='2'&&formData.expSupplementRule==='2'?(formData.expSupplementDays > formData.expPublishDays):(formData.expPublishRule==='1'&&formData.expSupplementRule==='1'?(formData.expSupplementDays < formData.expPublishDays):false))
                                        :(formData.expPublishRule==='2'&&formData.expCreateRule==='2'?(formData.expCreateDays > formData.expPublishDays):(formData.expPublishRule==='1'&&formData.expCreateRule==='1'?(formData.expCreateDays < formData.expPublishDays):false)))"
                                   :toValue="showDateCondition?(formData.expPublishRule==='2'&&formData.expApprovalRule==='2'?(formData.expApprovalDays > formData.expPublishDays?formData.expApprovalDays:null):(formData.expPublishRule==='1'&&formData.expApprovalRule==='1'?(formData.expApprovalDays < formData.expPublishDays?formData.expApprovalDays:null):null))
                                        :(showDateClear?(formData.expPublishRule==='2'&&formData.expSupplementRule==='2'?(formData.expSupplementDays > formData.expPublishDays?formData.expSupplementDays:null):(formData.expPublishRule==='1'&&formData.expSupplementRule==='1'?(formData.expSupplementDays < formData.expPublishDays?formData.expSupplementDays:null):null))
                                        :(formData.expPublishRule==='2'&&formData.expCreateRule==='2'?(formData.expCreateDays > formData.expPublishDays?formData.expCreateDays:null):(formData.expPublishRule==='1'&&formData.expCreateRule==='1'?(formData.expCreateDays < formData.expPublishDays?formData.expCreateDays:null):null)))"
                                   :max="showDateCondition?(formData.expPublishRule==='2'&&formData.expApprovalRule==='2'?90:(formData.expPublishRule==='1'&&formData.expApprovalRule==='1'?formData.expApprovalDays:90))
                                        :(showDateClear?(formData.expPublishRule==='2'&&formData.expSupplementRule==='2'?90:(formData.expPublishRule==='1'&&formData.expSupplementRule==='1'?formData.expSupplementDays:90))
                                        :(formData.expPublishRule==='2'&&formData.expCreateRule==='2'?90:(formData.expPublishRule==='1'&&formData.expCreateRule==='1'?formData.expCreateDays:90)))"

                                   :precision="0" :step="1" ref="expPublishDays"></input-number-controller>
          工作日
        </div>
      </div>
    </k-form-item>
    </k-form>

    <k-form-footer data-align="center">
      <k-btn class="btn-custom-primary" data-functype="SUBMIT"  data-action="DisclosureProdRule.addDisclosureProdRule"
             data-from="addDisclosureProdRuleForm"
             :data-model="formData" data-target="disclosureProdRuleGrid">
        <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
      </k-btn>
      <k-btn class="btn-custom-plain" data-functype="CLOSE">
        <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
      </k-btn>
    </k-form-footer>
  </div>
</template>

<script>
import inputNumberController from "@/pages/pms/basePublish/DisclosureRule/input-number-controller";
import Tools from "@/utils/tools";
export default {
  components: {inputNumberController},
  props: {
    disclosureType: '',
    formData: {},
/*    prodForm_:'',
    invProDime_ :'',
    invPrdLed_:'',
    prodObj_:'',
    prodClcMth_:'',
    prodInvTyp_:'',
    prodSerNm_:'',*/
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
      baseDateBlank2: true,//基准日期是否可以为空
      showTimePlan: true,//是否显示基准时间与计划时间设置
      showTimePlanTwo: false,//是否显示基准时间与计划时间设置
      baseDateForDict:{},
      baseDate:{},
      DocTypeDict:{},
      showDateConfigTable: true,
      netBaseDateBlank: true,//基准日期是否可以为空
      baseDateDisabled: false,//基准日期是否置灰
      showNetValueBaseDate: false,//是否展示复选基准日期  默认不显示，选择净值公告时，才展示
      flag:true,//是否匹配规则 true 为没有匹配的规则
      flagTow:true,//是否匹配规则 true 为没有匹配的规则

      showDateCondition: true,
      showDateClear: true,

      blankExpCreate:false,
      blankExpSupplement:false,
      blankExpApproval:false,
      blankExpPublish:false,

      expSupplementDisabled:false,
      expApprovalDisabled:false,
      expPublishDisabled:false,
    };
  },
  watch: {
    //监听信披类型
    'formData.disclosureType'() {
      //发起时间选框变更
      this.baseDateBlank = false;
      this.netBaseDateBlank = true;
      this.baseDateBlank2 = true;
      this.showTimePlan = true;
      this.showTimePlanTwo = false;
      this.showDateConfigTable = true;
      this.baseDateDisabled=true;
      this.showNetValueBaseDate=false;
      this.baseDateForDict=[];
      //change前滞空选框
      this.$set(this.formData, 'baseDate', '');
      this.$set(this.formData, 'channelIds', '');
      this.$set(this.formData, 'disclosureModId', '');
      this.$set(this.formData, 'disclosureSonType', '');
      this.$set(this.formData, 'addDocTypeDict', '');
      this.$set(this.formData, 'modList', '');
      this.$set(this.formData, 'noticeTitle', '');
      this.$set(this.formData, 'disclosureModVersionId', '');
      this.$set(this.formData, 'modVList', '');
      this.$set(this.formData, 'ifClearing', '');
      if (!this.flag){
        this.timeController();
        this.$set(this.formData, 'ifCondition', '');
        this.$set(this.formData, 'startRule', '1');
        this.$set(this.formData, 'channelIds', '');
        this.$set(this.formData, 'remark', '');
        this.flag = true;
      }
      if (this.formData.disclosureType!==undefined&&this.formData.disclosureType !== ''&&this.formData.disclosureType!==null) {
        this.findSonTypeByType();
        this.queryNotice();
        this.findSonVersion();
        this.expNotBlank();
        this.changeDisRule();
      }
      if (this.formData.disclosureType=='7'||this.formData.disclosureType=='8'||this.formData.disclosureType=='11'){
        this.baseDateBlank = true;
        this.baseDateBlank2 = true;
        this.showDateConfigTable = false;
        this.showTimePlan = false;
        this.timeController();
      }else if (this.formData.disclosureType=='1'){
        this.$set(this.formData, 'baseDate', '01');
        this.baseDateBlank = false;
        this.baseDateBlank2 = true;
      }else if (this.formData.disclosureType=='12'){
        this.$set(this.formData, 'baseDate', '13');
        this.baseDateBlank = false;
        this.baseDateBlank2 = true;
      }else if (this.formData.disclosureType=='2'){
        this.$set(this.formData, 'baseDate', '02');
        this.baseDateBlank = false;
        this.baseDateBlank2 = true;
      }else if (this.formData.disclosureType=='3'){
        this.$set(this.formData, 'baseDate', '03');
        this.baseDateBlank = false;
        this.baseDateBlank2 = false;
      }else if (this.formData.disclosureType=='13'){
        this.$set(this.formData, 'baseDate', '11');
        this.baseDateBlank = false;
        this.baseDateBlank2 = true;
      }else if (this.formData.disclosureType=='10'){
        this.$set(this.formData, 'baseDate', '12');
        this.baseDateBlank = false;
        this.baseDateBlank2 = true;
      }else if (this.formData.disclosureType=='5'){
        this.showTimePlan =false;
        this.showTimePlanTwo =true;
        this.baseDateDisabled = false;
        this.baseDateBlank =true;
        this.baseDateBlank2 = false;
        this.itemkey='07,08,09';
        this.httpUtil.comnQuery({
          action: "T8Dict.findBaseDate",
          params: {dict:'xp_disclosure_base_date',itemkey:this.itemkey}
        }).then(data => {
          this.baseDateForDict=data.rows
        }).catch({})
      }else if (this.formData.disclosureType=='6'){
        this.baseDateBlank =true;
        this.baseDateBlank2 = false;
        this.showTimePlan = false;
        this.showTimePlanTwo = true;
        this.baseDateDisabled = false;
        this.itemkey='08,09';
        this.httpUtil.comnQuery({
          action: "T8Dict.findBaseDate",
          params: {dict:'xp_disclosure_base_date',itemkey:this.itemkey}
        }).then(data => {
          this.baseDateForDict=data.rows
        }).catch({})
      }else if(this.formData.disclosureType=='9'){
        this.baseDateBlank = true;
        this.baseDateBlank2 = true;
        this.showDateConfigTable = true;
        this.showTimePlan = false;
        this.timeController();
      }
      else{
        this.showTimePlan = true;
        this.showTimePlanTwo = false;
        this.baseDateDisabled = false;
      }
      this.setNetValueDateShow();
    },
    //监听子信披类型
    'formData.disclosureSonType'() {
      //change前滞空选框
      this.$set(this.formData, 'noticeTitle', '');
      this.queryNotice();
      this.$set(this.formData, 'disclosureModId', '');
      this.$set(this.formData, 'modList', '');
      this.$set(this.formData, 'disclosureModVersionId', '');
      this.$set(this.formData, 'modVList', '');
      this.$set(this.formData, 'ifClearing', '');
      this.baseDateDisabled = true;
      if (!this.flag){
        this.timeController();
        this.$set(this.formData, 'ifCondition', '');
        this.$set(this.formData, 'startRule', '1');
        this.$set(this.formData, 'channelIds', '');
        this.$set(this.formData, 'remark', '');
        this.flag = true;
      }
      if (this.formData.disclosureSonType!==undefined&&this.formData.disclosureSonType !== ''&&this.formData.disclosureSonType!==null){
        this.findSonVersion();
        this.changeDisRule();
      }
      let key = '';
      if (this.formData.disclosureType==='5'){
        if(this.formData.disclosureSonType==='0501'){
          key='07'
        }
        if(this.formData.disclosureSonType==='0502'){
          key='08'
        }
        if(this.formData.disclosureSonType==='0503'){
          key='09'
        }
        this.httpUtil.comnQuery({
          action: "T8Dict.findBaseDate",
          params: {dict:'xp_disclosure_base_date',itemkey:key}
        }).then(data => {
          this.baseDateForDict=data.rows
          this.$set(this.formData, 'baseDate', key);
        }).catch({})
      }else if (this.formData.disclosureType==='6'){
        if(this.formData.disclosureSonType==='0601'){
          key='08'
        }
        if(this.formData.disclosureSonType==='0602'){
          key='09'
        }
        this.httpUtil.comnQuery({
          action: "T8Dict.findBaseDate",
          params: {dict:'xp_disclosure_base_date',itemkey:key}
        }).then(data => {
          this.baseDateForDict=data.rows
          this.$set(this.formData, 'baseDate', key);
        }).catch({})
      }
    },
    //监听模板名称
    'formData.disclosureModId'() {
      if (!this.flag){
        this.flagTow=false;
      }else {
        this.flagTow=true;
      }
      if (this.flag) {
        //change前滞空选框
        this.$set(this.formData, 'disclosureModVersionId', '');
        this.$set(this.formData, 'modVList', '');
        if (this.formData.disclosureModId) {
          this.queryVersion();
        }
      }
      this.flag = true;
    },
    //监听版本号
    'formData.disclosureModVersionId'() {
      if (this.flagTow) {
        //change前滞空选框
        this.$set(this.formData, 'ifClearing', '');
        if (this.formData.disclosureModVersionId){
          this.queryIsClearing();
        }
      }
      this.flagTow = true;
    },
    //监听产品名称
    'formData.prodCode'() {
      //change前滞空选框
      if (!this.flag){
        this.$set(this.formData, 'noticeTitle', '');
        this.queryNotice();
        this.$set(this.formData, 'disclosureModId', '');
        this.$set(this.formData, 'disclosureModVersionId', '');
        this.$set(this.formData, 'ifClearing', '');
        this.$set(this.formData, 'ifCondition', '');
        this.$set(this.formData, 'startRule', '1');
        this.$set(this.formData, 'channelIds', '');
        this.$set(this.formData, 'remark', '');
        this.timeController();
        this.flag = true;
      }
      if (this.formData.prodCode!==undefined&&this.formData.prodCode !== ''&&this.formData.prodCode!==null){
        this.changeDisRule()
      }
    },

    //监听复核
    'formData.ifCondition'() {
      this.showDateCondition=true;
      this.blankExpSupplement=false;
      this.$set(this.formData, 'expApprovalRule', '2');
      this.$set(this.formData, 'expApprovalDays', '0');
      if (this.formData.ifCondition==='0'){
        this.showDateCondition=false;
        this.blankExpSupplement=true;
      }
      if (this.showDateCondition){
        this.changeExpApprovalRule();
      }else if (this.showDateClear){
        this.changeExpSupplementRule();
      }else {
        this.changeExpCreateRule();
      }

    },
    //监听补录
    'formData.ifClearing'() {
      this.showDateClear= true;
      this.blankExpApproval=false;
      this.$set(this.formData, 'expSupplementRule', '2');
      this.$set(this.formData, 'expSupplementDays', '0');
      if (this.formData.ifClearing==='0'){
        this.showDateClear= false;
        this.blankExpApproval=true;
      }
      if(this.showDateClear){
        this.changeExpSupplementRule();
      }else {
        this.changeExpCreateRule();
      }
    },
    //监听T+/T-时间变化----start----
    'formData.expCreateDays'(){
      if (this.formData.expCreateDays ===undefined||this.formData.expCreateDays ===null||this.formData.expCreateDays ===''){
        this.$set(this.formData,'expCreateDays',0);
      }
      this.changeExpCreateDays();
    },
    'formData.expSupplementDays'(){
      if (this.formData.expSupplementDays ===undefined||this.formData.expSupplementDays ===null||this.formData.expSupplementDays ===''){
        this.$set(this.formData,'expSupplementDays',0);
      }
      this.changeExpSupplementDays();
    },
    'formData.expApprovalDays'(){
      if (this.formData.expApprovalDays ===undefined||this.formData.expApprovalDays ===null||this.formData.expApprovalDays ===''){
        this.$set(this.formData,'expApprovalDays',0);
      }
      this.changeExpApprovalDays();
    },
    'formData.expPublishDays'(){
      if (this.formData.expPublishDays ===undefined||this.formData.expPublishDays ===null||this.formData.expPublishDays ===''){
        this.$set(this.formData,'expPublishDays',0);
      }
      this.changeExpPublishDays();
    },
    'formData.expCreateRule'(){
      if (this.formData.expCreateRule ===undefined||this.formData.expCreateRule ===null||this.formData.expCreateRule ===''){
        this.$set(this.formData,'expCreateRule','2');
      }
      this.changeExpCreateRule();
    },
    'formData.expSupplementRule'(){
      if (this.formData.expSupplementRule ===undefined||this.formData.expSupplementRule ===null||this.formData.expSupplementRule ===''){
        this.$set(this.formData,'expSupplementRule','2');
      }
      this.changeExpSupplementRule();
    },
    'formData.expApprovalRule'(){
      if (this.formData.expApprovalRule ===undefined||this.formData.expApprovalRule ===null||this.formData.expApprovalRule ===''){
        this.$set(this.formData,'expApprovalRule','2');
      }
      this.changeExpApprovalRule();
    },
    'formData.expPublishRule'(){
      if (this.formData.expPublishRule ===undefined||this.formData.expPublishRule ===null||this.formData.expPublishRule ===''){
        this.$set(this.formData,'expPublishRule','2');
      }
      this.changeExpPublishRule();
    },
    //监听T+/T-时间变化----end----
  },
  created() {
    this.xpType();
    this.timeController();
  },
  methods: {
    findBassInfoByProdCode() {
      this.httpUtil.comnQuery({
        action: "DisclosureProdRule.findBassInfoByProdCode",
        params: {prodCode: this.formData.prodCode}
      }).then(data => {
        this.$set(this.formData,'prodForm',data.prodForm);
        this.$set(this.formData,'invProDime',data.invProDime);
        this.$set(this.formData,'invPrdLed',data.invPrdLed);
        this.$set(this.formData,'prodObj',data.prodObj);
        this.$set(this.formData,'prodClcMth',data.prodClcMth);
        this.$set(this.formData,'prodInvTyp',data.prodInvTyp);
        this.$set(this.formData,'prodSerNm',data.prodSerNm);
      });

    },
    timeController() {
      this.$set(this.formData, 'expCreateRule', '2');
      this.$set(this.formData, 'expCreateDays', '0');
      this.$set(this.formData, 'expSupplementRule', '2');
      this.$set(this.formData, 'expSupplementDays', '0');
      this.$set(this.formData, 'expApprovalRule', '2');
      this.$set(this.formData, 'expApprovalDays', '0');
      this.$set(this.formData, 'expPublishRule', '2');
      this.$set(this.formData, 'expPublishDays', '0');
    },
    expNotBlank(){
      this.blankExpCreate = false;
      this.blankExpSupplement = false;
      this.blankExpApproval = false;
      this.blankExpPublish = false;
    },
    expAllowBlank(){
      this.blankExpCreate = true;
      this.blankExpSupplement = true;
      this.blankExpApproval = true;
      this.blankExpPublish = true;
    },
    queryNotice(){
      let title = '';
      if (this.formData.disclosureType=='1'){//售前公告标题
        if (this.formData.disclosureSonType=='0101'){
          title='附件一：杭州联合农村商业银行关于发行{yyyy}年{产品名称}{产品模式}{募集币种}理财产品的报告';
        }else if(this.formData.disclosureSonType=='0102'){
          title = '附件二：理财产品可行性评估报告';
        }else if(this.formData.disclosureSonType=='0106'){
          title = '附件六：理财产品协议书及客户权益须知';
        }else if(this.formData.disclosureSonType=='0107'){
          title = '附件七：理财产品说明书及风险揭示书';
        }else if(this.formData.disclosureSonType=='0108'){
          title = '附件八：理财产品宣传资料';
        }
      }else if(this.formData.disclosureType=='2'){//发行成立公告标题
        title = '杭州联合农村商业银行{产品品牌}{产品名称}（{产品代码}）{募集币种}理财产品发行公告';
      }else if(this.formData.disclosureType=='3'){//到期公告标题
        title = '杭州联合农村商业银行{产品品牌}{产品名称}（{产品代码}）{募集币种}理财产品到期公告';
      }else if(this.formData.disclosureType=='5'){//定期公告标题
        if (this.formData.disclosureSonType=='0501'){
          title = '杭州联合农村商业银行{产品品牌}{产品名称}（{产品代码}）{yyyy}年第{U}季度报告';
        }else if(this.formData.disclosureSonType=='0502'){
          title = '杭州联合农村商业银行{产品品牌}{产品名称}（{产品代码}）{yyyy}年半年度报告';
        }else if(this.formData.disclosureSonType=='0503'){
          title = '杭州联合农村商业银行{产品品牌}{产品名称}（{产品代码}）{yyyy}年年度报告';
        }else if(this.formData.disclosureSonType=='0504'){
          title = ''
        }else{
          title = '';
        }
      }else if(this.formData.disclosureType=='6'){//整体公告标题
        if (this.formData.disclosureSonType=='0601'){
          title = '杭州联合农村商业银行理财业务{yyyy}年半年度报告';
        }else if(this.formData.disclosureSonType=='0602'){
          title = '杭州联合农村商业银行理财业务{yyyy}年年度报告';
        }
      }else if(this.formData.disclosureType=='9'){//净值公告标题
        if (this.formData.disclosureSonType=='0901'||this.formData.disclosureSonType=='0902') {
          title = '杭州联合农村商业银行{产品品牌}{产品名称}（{产品代码}）{yyyy}年{MM}月{dd}日净值公告';
        }else if(this.formData.disclosureSonType=='0903'){
          title = '杭州联合农村商业银行封闭式及定开式净值型产品{yyyy}年{MM}月{dd}日产品净值公告';
        }
      }else if(this.formData.disclosureType=='10'){//分红公告标题
        title = '杭州联合农村商业银行{产品品牌}{产品名称}净值型人民币理财产品分红公告';
      }else if(this.formData.disclosureType=='12'){//销售文档标题
        title = '杭州联合农村商业银行{产品名称}（{产品代码}）销售文档';
      }else if(this.formData.disclosureType=='13'){//申购赎回标题
        title = '杭州联合农村商业银行{产品品牌}{产品名称}（{产品代码}）{yyyyM}年{MMM}月{ddM}日-{yyyyF}年{MMF}月{ddF}日申购赎回公告';
      }
      this.$set(this.formData,'noticeTitle',title);

    },
    xpType() {
      this.httpUtil.comnQuery({
        action: "DisclosureMod.getXPTypeInProd",
        params: null
      }).then(data => {
        this.DocTypeDict = data.rows;
      }).catch({})
    },
    //展示净值披露基准日期
    setNetValueDateShow() {
      if(this.formData.disclosureType==9){
        this.showNetValueBaseDate = true;
        this.netBaseDateBlank = false;
      }
    },
    //请求版本号
    queryVersion() {
      this.httpUtil.comnQuery({
        action: "DisclosureRule.findDisclosureModsVWithRule",
        params: {disclosureModId: this.formData.disclosureModId}
      }).then(data => {
        this.formData.modVList = data.rows;
      }).catch({})
    },
    //请求是否补录
    queryIsClearing() {
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
    findSonVersion() {
      this.httpUtil.comnQuery({
        action: "DisclosureRule.findDisclosureModsWithRule",
        params: {disclosureType: this.formData.disclosureType,
          disclosureSonType: this.formData.disclosureSonType
        }
      }).then(data => {
        this.formData.modList = data.rows;
      }).catch({})
    },
    //根据信披类型查询信披子类型
    findSonTypeByType() {
      //请求信披字典
      this.httpUtil.comnQuery({
        action: "DisclosureMod.getXPTypeByDocType",
        params: {disclosureType: this.formData.disclosureType}
      }).then(data => {
        if (this.formData.disclosureType==='9'){
          delete data.rows[1];//排除净值整体报告
          delete data.rows[2];//排除净值整体报告
        }
        this.formData.addDocTypeDict = data.rows;
      }).catch({})
    },
    changeDisRule() {
      let prodForm_ = this.formData.prodForm;
      let invProDime_ = this.formData.invProDime;
      let invPrdLed_ = this.formData.invPrdLed;
      let prodObj_ = this.formData.prodObj;
      let prodClcMth_ = this.formData.prodClcMth;
      let prodInvTyp_ = this.formData.prodInvTyp;
      let prodSerNm_ = this.formData.prodSerNm;
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
          return;
        }
      });
      if (this.formData.disclosureType!==null&&this.formData.disclosureType!==''&&this.formData.disclosureType!==undefined
        &&this.formData.disclosureType!=='5'&&this.formData.disclosureType!=='6'&&this.formData.disclosureType!=='1'
        &&this.formData.prodCode!==null&&this.formData.prodCode!==''&&this.formData.prodCode!==undefined){
        this.httpUtil.comnQuery({
          action:'DisclosureRule.getProdDisRuleForAdd',
          params: {
            disclosureType: this.formData.disclosureType,
            disclosureSonType: this.formData.disclosureSonType,
            prodCode: this.formData.prodCode,
          }
        }).then(data => {
          if (data.success && !data.flag){
            this.formData =data;
            this.$set(this.formData, 'addDocTypeDict','');
            this.findSonTypeByType();
            this.$set(this.formData, 'addDocTypeDict', this.formData.addDocTypeDict);
            if(data.t8DisclosureRuleId!==undefined&&data.t8DisclosureRuleId!==null&&data.t8DisclosureRuleId!==''){
              this.$set(this.formData, 'modList', '');
              this.$set(this.formData, 'modVList', '');
              this.findSonVersion();
              this.queryVersion();
              this.$set(this.formData, 'modList', this.formData.modList);
              this.$set(this.formData, 'modVList', this.formData.modVList);
              this.flag = data.flag;
              this.$set(this.formData,'prodForm',prodForm_);
              this.$set(this.formData,'invProDime',invProDime_);
              this.$set(this.formData,'invPrdLed',invPrdLed_);
              this.$set(this.formData,'prodObj',prodObj_);
              this.$set(this.formData,'prodClcMth',prodClcMth_);
              this.$set(this.formData,'prodInvTyp',prodInvTyp_);
              this.$set(this.formData,'prodSerNm',prodSerNm_);
            }
          }else if (data.success && data.flag){//为true则表示没有可匹配的信息
            this.$set(this.formData,'prodForm',prodForm_);
            this.$set(this.formData,'invProDime',invProDime_);
            this.$set(this.formData,'invPrdLed',invPrdLed_);
            this.$set(this.formData,'prodObj',prodObj_);
            this.$set(this.formData,'prodClcMth',prodClcMth_);
            this.$set(this.formData,'prodInvTyp',prodInvTyp_);
            this.$set(this.formData,'prodSerNm',prodSerNm_);
            return;
          }
        });
      }else if ((this.formData.disclosureType==='5'||this.formData.disclosureType==='6'||this.formData.disclosureType==='1')
        &&this.formData.prodCode!==null&&this.formData.prodCode!==''&&this.formData.prodCode!==undefined
        &&this.formData.disclosureSonType!==null&&this.formData.disclosureSonType!==''&&this.formData.disclosureSonType!==undefined){
        this.httpUtil.comnQuery({
          action:'DisclosureRule.getProdDisRuleForAdd',
          params: {
            disclosureType: this.formData.disclosureType,
            disclosureSonType: this.formData.disclosureSonType,
            prodCode: this.formData.prodCode,
          }
        }).then(data => {
          if (data.success && !data.flag) {
            this.formData =data;
            this.$set(this.formData, 'addDocTypeDict','');
            this.findSonTypeByType();
            this.$set(this.formData, 'addDocTypeDict', this.formData.addDocTypeDict);
            if (data.t8DisclosureRuleId !== undefined && data.t8DisclosureRuleId !== null && data.t8DisclosureRuleId !== '') {
              this.$set(this.formData, 'modList', '');
              this.$set(this.formData, 'modVList', '');
              this.findSonVersion();
              this.queryVersion();
              this.$set(this.formData, 'modList', this.formData.modList);
              this.$set(this.formData, 'modVList', this.formData.modVList);
              this.flag = data.flag;
              this.$set(this.formData,'prodForm',prodForm_);
              this.$set(this.formData,'invProDime',invProDime_);
              this.$set(this.formData,'invPrdLed',invPrdLed_);
              this.$set(this.formData,'prodObj',prodObj_);
              this.$set(this.formData,'prodClcMth',prodClcMth_);
              this.$set(this.formData,'prodInvTyp',prodInvTyp_);
              this.$set(this.formData,'prodSerNm',prodSerNm_);
            }else if (data.success && data.flag){//为true则表示没有可匹配的信息
              this.$set(this.formData,'prodForm',prodForm_);
              this.$set(this.formData,'invProDime',invProDime_);
              this.$set(this.formData,'invPrdLed',invPrdLed_);
              this.$set(this.formData,'prodObj',prodObj_);
              this.$set(this.formData,'prodClcMth',prodClcMth_);
              this.$set(this.formData,'prodInvTyp',prodInvTyp_);
              this.$set(this.formData,'prodSerNm',prodSerNm_);
              return;
            }
          }
        });
      }
    },
    //T+/T-日期互相校验联动----start----
    changeExpCreateDays(){
      this.$refs.expCreateDays.value=this.$refs.expCreateDays.displayValue
      if (this.showDateClear){
        if (this.formData.expSupplementRule==='2'&&this.formData.expCreateRule==='2'? (this.formData.expCreateDays > this.formData.expSupplementDays):(this.formData.expSupplementRule==='1'&&this.formData.expCreateRule==='1'? (this.formData.expCreateDays < this.formData.expSupplementDays):false)){
          this.formData.expSupplementDays=this.$refs.expCreateDays.displayValue;
        }
      }else if (this.showDateCondition){
        if (this.formData.expApprovalRule==='2'&&this.formData.expCreateRule==='2'?(this.formData.expCreateDays > this.formData.expApprovalDays):(this.formData.expApprovalRule==='1'&&this.formData.expCreateRule==='1'? (this.formData.expCreateDays < this.formData.expApprovalDays):false)){
          this.formData.expApprovalDays=this.$refs.expCreateDays.displayValue;
        }
      }else{
        if(this.formData.expPublishRule==='2'&&this.formData.expCreateRule==='2'?(this.formData.expCreateDays > this.formData.expPublishDays):(this.formData.expPublishRule==='1'&&this.formData.expCreateRule==='1'?(this.formData.expCreateDays < this.formData.expPublishDays):false)){
          this.formData.expPublishDays=this.$refs.expCreateDays.displayValue;
        }
      }
    },
    changeExpSupplementDays(){
      this.$refs.expSupplementDays.value=this.$refs.expSupplementDays.displayValue
      if (this.showDateCondition){
        if (this.formData.expApprovalRule==='2'&&this.formData.expSupplementRule==='2'?(this.formData.expSupplementDays > this.formData.expApprovalDays):(this.formData.expApprovalRule==='1'&&this.formData.expSupplementRule==='1'?(this.formData.expSupplementDays < this.formData.expApprovalDays):false)){
          this.formData.expApprovalDays=this.$refs.expSupplementDays.displayValue;
        }
      }else {
        if (this.formData.expPublishRule==='2'&&this.formData.expSupplementRule==='2'?(this.formData.expSupplementDays > this.formData.expPublishDays):(this.formData.expPublishRule==='1'&&this.formData.expSupplementRule==='1'?(this.formData.expSupplementDays < this.formData.expPublishDays):false)){
          this.formData.expPublishDays=this.$refs.expSupplementDays.displayValue;
        }
      }
    },
    changeExpApprovalDays(){
      this.$refs.expApprovalDays.value=this.$refs.expApprovalDays.displayValue
      if (this.formData.expPublishRule==='2'&&this.formData.expApprovalRule==='2'?(this.formData.expApprovalDays > this.formData.expPublishDays):(this.formData.expPublishRule==='1'&&this.formData.expApprovalRule==='1'?(this.formData.expApprovalDays < this.formData.expPublishDays):false)){
        this.formData.expPublishDays=this.$refs.expApprovalDays.displayValue;
      }
    },
    changeExpPublishDays(){
      this.$refs.expPublishDays.value=this.$refs.expPublishDays.displayValue
    },
    changeExpCreateRule(){
      if (this.showDateClear){
        this.expSupplementDisabled = false;
      }else if(this.showDateCondition){
        this.expApprovalDisabled = false;
      }else {
        this.expPublishDisabled = false;
      }
      if (this.formData.expCreateRule==='2'){
        if (this.showDateClear){
          this.$set(this.formData,'expSupplementRule','2');
          this.expSupplementDisabled = true;
        }else if (this.showDateCondition){
          this.$set(this.formData,'expApprovalRule','2');
          this.expApprovalDisabled = true;
        }else{
          this.$set(this.formData,'expPublishRule','2');
          this.expPublishDisabled = true;
        }
      }
      if (this.showDateClear){
        if (this.formData.expSupplementRule==='2'&&this.formData.expCreateRule==='2'? (this.formData.expCreateDays > this.formData.expSupplementDays):(this.formData.expSupplementRule==='1'&&this.formData.expCreateRule==='1'? (this.formData.expCreateDays < this.formData.expSupplementDays):false)){
          this.formData.expSupplementDays=this.$refs.expCreateDays.displayValue;
        }
      }else if (this.showDateCondition){
        if (this.formData.expApprovalRule==='2'&&this.formData.expCreateRule==='2'?(this.formData.expCreateDays > this.formData.expApprovalDays):(this.formData.expApprovalRule==='1'&&this.formData.expCreateRule==='1'? (this.formData.expCreateDays < this.formData.expApprovalDays):false)){
          this.formData.expApprovalDays=this.$refs.expCreateDays.displayValue;
        }
      }else{
        if(this.formData.expPublishRule==='2'&&this.formData.expCreateRule==='2'?(this.formData.expCreateDays > this.formData.expPublishDays):(this.formData.expPublishRule==='1'&&this.formData.expCreateRule==='1'?(this.formData.expCreateDays < this.formData.expPublishDays):false)){
          this.formData.expPublishDays=this.$refs.expCreateDays.displayValue;
        }
      }
    },
    changeExpSupplementRule(){
      if (this.showDateCondition){
        this.expApprovalDisabled = false;
      }else{
        this.expPublishDisabled = false;
      }
      if (this.formData.expSupplementRule==='2'){
        if (this.showDateCondition){
          this.$set(this.formData,'expApprovalRule','2');
          this.expApprovalDisabled = true;
        }else{
          this.$set(this.formData,'expPublishRule','2');
          this.expPublishDisabled = true;
        }
      }
      if (this.showDateClear) {
        if (this.formData.expSupplementRule === '2' && this.formData.expCreateRule === '2' ? (this.formData.expCreateDays > this.formData.expSupplementDays) : (this.formData.expSupplementRule === '1' && this.formData.expCreateRule === '1' ? (this.formData.expCreateDays < this.formData.expSupplementDays) : false)) {
          this.formData.expSupplementDays = this.$refs.expCreateDays.displayValue;
        }
      }
      if (this.showDateCondition){
        if (this.formData.expApprovalRule==='2'&&this.formData.expSupplementRule==='2'?(this.formData.expSupplementDays > this.formData.expApprovalDays):(this.formData.expApprovalRule==='1'&&this.formData.expSupplementRule==='1'?(this.formData.expSupplementDays < this.formData.expApprovalDays):false)){
          this.formData.expApprovalDays=this.$refs.expSupplementDays.displayValue;
        }
      }else {
        if (this.formData.expPublishRule==='2'&&this.formData.expSupplementRule==='2'?(this.formData.expSupplementDays > this.formData.expPublishDays):(this.formData.expPublishRule==='1'&&this.formData.expSupplementRule==='1'?(this.formData.expSupplementDays < this.formData.expPublishDays):false)){
          this.formData.expPublishDays=this.$refs.expSupplementDays.displayValue;
        }
      }
    },
    changeExpApprovalRule(){
      // if (this.showDateCondition) {
      this.expPublishDisabled = false;
      // }
      if (this.formData.expApprovalRule==='2'){
        // if (this.showDateCondition){
        this.$set(this.formData,'expPublishRule','2');
        this.expPublishDisabled = true;
        // }
      }
      if (this.showDateClear){
        if (this.formData.expApprovalRule==='2'&&this.formData.expSupplementRule==='2'?(this.formData.expSupplementDays > this.formData.expApprovalDays):(this.formData.expApprovalRule==='1'&&this.formData.expSupplementRule==='1'?(this.formData.expSupplementDays < this.formData.expApprovalDays):false)){
          this.formData.expApprovalDays=this.$refs.expSupplementDays.displayValue;
        }
      }else {
        if (this.formData.expApprovalRule==='2'&&this.formData.expCreateRule==='2'?(this.formData.expCreateDays > this.formData.expApprovalDays):(this.formData.expApprovalRule==='1'&&this.formData.expCreateRule==='1'? (this.formData.expCreateDays < this.formData.expApprovalDays):false)){
          this.formData.expApprovalDays=this.$refs.expCreateDays.displayValue;
        }
      }
      if (this.showDateCondition) {
        if (this.formData.expPublishRule === '2' && this.formData.expApprovalRule === '2' ? (this.formData.expApprovalDays > this.formData.expPublishDays) : (this.formData.expPublishRule === '1' && this.formData.expApprovalRule === '1' ? (this.formData.expApprovalDays < this.formData.expPublishDays) : false)) {
          this.formData.expPublishDays = this.$refs.expApprovalDays.displayValue;
        }
      }
    },
    changeExpPublishRule(){
      if (this.showDateCondition){
        if (this.formData.expPublishRule==='2'&&this.formData.expApprovalRule==='2'?(this.formData.expApprovalDays > this.formData.expPublishDays):(this.formData.expPublishRule==='1'&&this.formData.expApprovalRule==='1'?(this.formData.expApprovalDays < this.formData.expPublishDays):false)){
          this.formData.expPublishDays=this.$refs.expApprovalDays.displayValue;
        }
      }else if (this.showDateClear){
        if (this.formData.expPublishRule==='2'&&this.formData.expSupplementRule==='2'?(this.formData.expSupplementDays > this.formData.expPublishDays):(this.formData.expPublishRule==='1'&&this.formData.expSupplementRule==='1'?(this.formData.expSupplementDays < this.formData.expPublishDays):false)){
          this.formData.expPublishDays=this.$refs.expSupplementDays.displayValue;
        }
      }else {
        if(this.formData.expPublishRule==='2'&&this.formData.expCreateRule==='2'?(this.formData.expCreateDays > this.formData.expPublishDays):(this.formData.expPublishRule==='1'&&this.formData.expCreateRule==='1'?(this.formData.expCreateDays < this.formData.expPublishDays):false)){
          this.formData.expPublishDays=this.$refs.expCreateDays.displayValue;
        }
      }
    },
    //T+/T-日期互相校验联动----end---
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
