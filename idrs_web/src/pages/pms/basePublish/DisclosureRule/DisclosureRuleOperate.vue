<template>
  <div>
    <k-form ref="addDisclosureRuleForm" :data-col="2" data-label-width="130px" data-input-width="180px" :isFormBodyScreen="true">

      <div class ="tableLine" >
        <span class="leftText">规则信息</span>
        <div class="itemsCorn"></div>
      </div>

      <k-form-item label="信披规则名称" :data-col="2">
        <k-field-text v-model="formData.ruleName" :data-allowblank="false" :data-max-length="255"/>
      </k-form-item>
      <k-form-item label="信披类型">
        <k-field-select v-model="formData.disclosureType" :data-data="DocTypeDict"   data-value-field="value" data-display-field="text" :data-allowblank="false" />
      </k-form-item>
      <k-form-item label="信披子类型" v-if="formData.disclosureType==='5'||formData.disclosureType==='6'||formData.disclosureType==='1'||formData.disclosureType==='9'">
        <k-field-select v-model="formData.disclosureSonType" :data-allowblank="false" :data-data="addDocTypeDict"
                        data-display-field="text" data-value-field="value"/>
      </k-form-item>
      <k-form-item label="公告标题" :data-col="2">
        <k-field-text v-model="formData.noticeTitle" :data-max-length="200"  :data-allowblank="false"
        />
      </k-form-item>
      <k-form-item label="模板名称"  :data-col="2">
        <k-field-select v-model="formData.disclosureModId" :data-allowblank="false"
                        :data-data="modList"
                        data-display-field="t8DisclosureModName"
                        data-value-field="disclosureModId" />
      </k-form-item>
      <k-form-item label="版本号">
        <k-field-select style="width:100%" v-model="formData.disclosureModVersionId"  :data-allowblank="false"
                        :data-data="formData.modVList"
                        data-display-field="versionNumber"
                        data-value-field="disclosureModVersionId"
        />
        <!--        <k-btn class="md-info md-just-icon md-simple" data-descript="预览文档模板信息" data-size="small"-->
        <!--               :data-handler="previewPrintTempVersion">-->
        <!--          <md-icon>zoom_in</md-icon>-->
        <!--        </k-btn>-->
      </k-form-item>
      <k-form-item label="是否需要补录">
        <k-field-select v-model="formData.ifClearing" data-dict="xp_if_ok" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="是否复核">
        <k-field-select v-model="formData.ifCondition" data-dict="if_ok" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="任务发起方式" >
        <k-field-select v-model="formData.startRule" data-dict="xp_disclosure_start_rule" data-default-value="1"
                        :data-disabled="true" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="公告负责角色" key="noticeRoleid">
        <k-field-select v-model="formData.noticeRoleid" :data-allowblank="false"
                        data-action="Role.findParents" data-display-field="rolename" data-value-field="roleid"/>
      </k-form-item>

      <k-form-item label="备注" :data-col="2">
        <k-field-text v-model="formData.remark" :data-allowblank="true" :data-max-length="60"
                      inputType="textarea" :rows="3"/>
      </k-form-item>

      <div class ="tableLine" >
        <span class="leftText">产品参数</span>
        <div class="itemsCorn"></div>
      </div>

      <k-form-item label="产品形态" >
        <k-field-select v-show="!changeMultiple"
                        v-model="formData.prodForm"  data-dict="xp_prod_form" :data-multiple="false" />
        <k-field-select v-show="changeMultiple"
                        v-model="formData.prodForm"  data-dict="xp_prod_form" :data-multiple="true" />
      </k-form-item>
      <k-form-item label="投资周期维度" v-if="formData.prodForm ==='06'" >
        <k-field-select v-show="!changeMultiple"
                        v-model="formData.invPrdDime"  data-dict="xp_cycle_dimension" :data-allowblank="true" :data-multiple="false"/>
        <k-field-select v-show="changeMultiple"
                        v-model="formData.invPrdDime"  data-dict="xp_cycle_dimension" :data-allowblank="true" :data-multiple="true"/>
      </k-form-item>
      <k-form-item label="投资周期长度" v-if="formData.prodForm ==='06'">
        <k-field-text v-model="formData.invPrdLen"  :data-allowblank="true" :data-multiple="false" :data-max-length="3" data-regx="^\+?[1-9][0-9]*$" data-regx-text="请输入大于0的整数"/>
      </k-form-item>
      <k-form-item label="销售对象" >
        <k-field-select  v-show="!changeMultiple"
                         v-model="formData.prodObj"  :data-allowblank="true" data-dict="xp_target_customer" :data-multiple="false"/>
        <k-field-select  v-show="changeMultiple"
                         v-model="formData.prodObj"  :data-allowblank="true" data-dict="xp_target_customer" :data-multiple="true"/>
      </k-form-item>
      <k-form-item label="募集方式" >
        <k-field-select  v-show="!changeMultiple"
                         v-model="formData.prodClcMth" data-dict="xp_raise_type" :data-multiple="false"
                         :data-allowblank="true">
        </k-field-select>
        <k-field-select  v-show="changeMultiple"
                         v-model="formData.prodClcMth" data-dict="xp_raise_type" :data-multiple="true"
                         :data-allowblank="true">
        </k-field-select>
      </k-form-item>
      <k-form-item label="产品投资性质">
        <k-field-select  v-show="!changeMultiple"
                         v-model="formData.prodInvTyp" data-dict="xp_prod_invest_nature" :data-multiple="false"
                         :data-allowblank="true">
        </k-field-select>
        <k-field-select  v-show="changeMultiple"
                         v-model="formData.prodInvTyp" data-dict="xp_prod_invest_nature" :data-multiple="true"
                         :data-allowblank="true">
        </k-field-select>
      </k-form-item>
      <k-form-item label="产品系列"  :dataCol="2" key="prodSerCd" >
        <k-field-select  v-show="!changeMultiple"
                         v-model="formData.prodSerCd" :data-multiple="false"
                         data-action="T8ProdInfo.getNewProdSeries" data-value-field="seriesCode" data-display-field="seriesName"
                         :data-allowblank="true"
        ></k-field-select>
        <k-field-select  v-show="changeMultiple"
                         v-model="formData.prodSerCd" :data-multiple="true"
                         data-action="T8ProdInfo.getNewProdSeries" data-value-field="seriesCode" data-display-field="seriesName"
                         :data-allowblank="true"
        ></k-field-select>

      </k-form-item>
      <k-form-item label="分级产品标志">
        <k-field-select  v-show="!changeMultiple"
                         v-model="formData.motherFundFlag" data-dict="rule_mother_fund_flag" :data-multiple="false"
                         :data-allowblank="true">
        </k-field-select>
        <k-field-select  v-show="changeMultiple"
                         v-model="formData.motherFundFlag" data-dict="rule_mother_fund_flag" :data-multiple="true"
                         :data-allowblank="true">
        </k-field-select>
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
      </k-form-item>
      <k-form-item label="计划时间设置" :data-col="2" v-show="showDateConfigTable">
        <div>
          <div>
            <span style="color: #F56C6C">*</span>计划生成日期 T
            <k-field-select v-model="formData.expCreateRule" data-dict="xp_disclosure_compute_date" style="width: 60px;" :data-allowblank="blankExpCreate"
                            data-placeholder="" />
            <input-number-controller v-model="formData.expCreateDays" controls-position="right" :min="0" :data-allowblank="blankExpCreate"
                                     :max="90" :precision="0" :step="1" ref="expCreateDays"></input-number-controller>
            工作日
          </div>
          <div v-show="showDateClear">
            <span style="color: #F56C6C">*</span>补录完成日期 T
            <k-field-select v-model="formData.expSupplementRule" data-dict="xp_disclosure_compute_date"
                            style="width: 60px;" :data-allowblank="blankExpSupplement" :data-disabled="expSupplementDisabled"
                            data-placeholder=""  />
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
                            data-placeholder="" />
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
                            data-placeholder=""/>
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
  </div>
</template>

<script>
import Tools from "@/utils/tools";
import inputNumberController from "@/pages/pms/basePublish/DisclosureRule/input-number-controller";

export default {
  components: {inputNumberController},
  props: {
    isEditer:false,//是否是编辑框
    formData: {},
  },
  data() {
    return {
      formData: {
        modVList:{},//模板版本下拉数据
      },
      DocTypeDict: {},//信披类型下拉数据
      addDocTypeDict:{},//子类型下拉数据
      modList:{},//模板下拉数据
      baseDateForDict:{},//基准日期下拉数据

      showDateConfigTable: true,//是否展示时间组件
      showDateCondition: true,//是否展示时间组件中的复核
      showDateClear: true,//是否展示时间组件中的补录


      showTimePlan: true,//是否显示基准时间与计划时间设置
      showTimePlanTwo: false,//是否显示基准时间与计划时间设置
      baseDateBlank: false,//基准日期是否可以为空
      baseDateBlank2: true,//基准日期是否可以为空
      baseDateDisabled: false,//基准日期是否置灰
      showNetValueBaseDate: false,//是否展示复选基准日期  默认不显示，选择净值公告时，才展示
      netBaseDateBlank: true,//净值披露基准日期与规则是否可以为空   默认可以为空，选择净值公告时，不可为空


      blankExpCreate:false,//计划生成日是否可为空
      blankExpSupplement:false,//补录完成日是否可为空
      blankExpApproval:false,//复核完成日是否可为空
      blankExpPublish:false,//计划发布日是否可为空


      expSupplementDisabled:false,//补录完成规则是否置灰
      expApprovalDisabled:false,//复核完成规则是否置灰
      expPublishDisabled:false,//计划发布规则是否置灰

      itemkey:'',//字典key变量
    };
  },
  computed: {
    //复选框变量
    changeMultiple(){
      if(this.formData.disclosureSonType==='0903'||this.formData.disclosureType==='6') {
        return true;
      }
    },
  },
  created() {
    //加载信披类型
    this.xpType();
    //是否是编辑框
    if (this.isEditer){
      /**顺序执行*/
      //滞空下拉数据
      this.$set(this.formData, 'addDocTypeDict', '');
      this.$set(this.formData, 'modList', '');
      //获取模板信息
      this.queryModel();
      //获取子类型
      this.selectDisclosureType();
      //编辑框数据初始化
      this.queryPage();
      //滞空模板版本下拉数据
      this.$set(this.formData, 'modVList', '');
      //获取版本
      this.queryVersion();
      //获取是否补录
      this.queryIsClearing();
      //补录联动
      this.changeIfClearing();
      //复核联动
      this.changeIfCondition();
      //时间控件联动
      this.changeExpApprovalRule();
      this.changeExpSupplementRule();
      this.changeExpCreateRule();
    }else{
      this.timeController();
    }
  },
  watch: {
    //监听补录
    'formData.ifClearing'() {
      this.showDateClear= true;
      this.blankExpApproval=false;
      this.$set(this.formData, 'expSupplementRule', '2');
      this.$set(this.formData, 'expSupplementDays', '0');
      this.changeIfClearing();
      if(this.showDateClear){
        this.changeExpSupplementRule();
      }else {
        this.changeExpCreateRule();
      }
    },
    //监听复核
    'formData.ifCondition'() {
      this.showDateCondition=true;
      this.blankExpSupplement=false;
      this.$set(this.formData, 'expApprovalRule', '2');
      this.$set(this.formData, 'expApprovalDays', '0');
      this.changeIfCondition();
      if (this.showDateCondition){
        this.changeExpApprovalRule();
      }else if (this.showDateClear){
        this.changeExpSupplementRule();
      }else {
        this.changeExpCreateRule();
      }

    },
    //监听产品形态
    'formData.prodForm'() {
      this.$set(this.formData, 'invPrdDime', '');
      this.$set(this.formData, 'invPrdLen', '');
    },
    //监听模板版本
    'formData.disclosureModVersionId'() {
      if (this.formData.disclosureModVersionId === ''||this.formData.disclosureModVersionId==null){
        this.$set(this.formData, 'ifClearing', '');
      }else{
        this.queryIsClearing()
      }
    },
    //监听模板
    'formData.disclosureModId'() {
      if (this.formData.disclosureModId === ''||this.formData.disclosureModId==null){
        this.disclosureModVersionId();
      }else{
        //切换下拉选择时滞空前数据
        this.disclosureModVersionId();
        this.queryVersion();
      }
    },
    //监听信披类型
    'formData.disclosureType'() {
      //change前滞空数据
      this.$set(this.formData, 'disclosureSonType', '');
      this.$set(this.formData, 'disclosureModId', '');
      this.$set(this.formData, 'modList', '');
      this.$set(this.formData, 'netValueDate', '');
      this.$set(this.formData, 'noticeTitle', '');
      this.$set(this.formData, 'baseDate', '');
      //change前还原状态
      this.baseDateDisabled = true;
      this.netBaseDateBlank = true;
      this.baseDateBlank = false;
      this.baseDateBlank2 = true;
      this.showDateConfigTable = true;
      this.showNetValueBaseDate = false;
      this.baseDateForDict=[];
      this.showTimePlan = true;
      this.showTimePlanTwo = false;
      /**顺序执行*/
      this.queryModel();
      this.selectDisclosureType();
      //信披类型联动
      this.queryPage();
      //获取公告标题
      this.queryNotice();
      this.expNotBlank();
      this.changeMul();

    },
    //监听信披子类型
    'formData.disclosureSonType'() {
      this.$set(this.formData, 'disclosureModId', '');
      this.$set(this.formData, 'modList', '');
      this.$set(this.formData, 'noticeTitle', '');
      this.baseDateDisabled = true;
      this.queryNotice();
      this.queryModel();
      this.changeMul();
      //子类型联动
      this.querySonPage();
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
  methods: {
    //子类型联动加载
    querySonPage(){
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
    //时间控件滞空
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
    //加载公告标题
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
    changeIfCondition(){
      if (this.formData.ifCondition==='0'){
        this.showDateCondition=false;
        this.blankExpSupplement=true;
      }
    },
    changeIfClearing(){
      if (this.formData.ifClearing==='0'){
        this.showDateClear= false;
        this.blankExpApproval=true;
      }
    },
    xpType() {
      this.httpUtil.comnQuery({
        action: "DisclosureMod.getXPType",
        params: null
      }).then(data => {
        this.DocTypeDict = data.rows;
      }).catch({})
    },
    queryIsClearing() {
      //是否补录
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
    //初始化编辑页加载
    queryPage() {
      //信披类型联动
      if (this.formData.disclosureType=='7'||this.formData.disclosureType=='8'||this.formData.disclosureType=='11'){
        this.baseDateBlank = true;
        this.baseDateBlank2 = true;
        this.showDateConfigTable = false;
        this.showTimePlan = false;
        this.timeController();
      } else if (this.formData.disclosureType=='1'){
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
        this.baseDateBlank2 = true;
      }else if (this.formData.disclosureType=='13'){
        this.$set(this.formData, 'baseDate', '11');
        this.baseDateBlank = false;
        this.baseDateBlank2 = true;
      }else if (this.formData.disclosureType=='5'){
        this.showTimePlan =false;
        this.showTimePlanTwo =true;
        this.baseDateDisabled = false;
        this.baseDateBlank =true;
        this.baseDateBlank2 = false;
        this.itemkey='07,08,09'
        this.httpUtil.comnQuery({
          action: "T8Dict.findBaseDate",
          params: {dict:'xp_disclosure_base_date',itemkey:this.itemkey}
        }).then(data => {
          this.baseDateForDict=data.rows
        }).catch({})
      }else if (this.formData.disclosureType=='6'){
        this.showTimePlan = false;
        this.showTimePlanTwo = true;
        this.baseDateDisabled = false;
        this.baseDateBlank =true;
        this.baseDateBlank2 = false;
        this.itemkey='08,09'
        this.httpUtil.comnQuery({
          action: "T8Dict.findBaseDate",
          params: {dict:'xp_disclosure_base_date',itemkey:this.itemkey}
        }).then(data => {
          this.baseDateForDict=data.rows
        }).catch({})
      } else if (this.formData.disclosureType=='9'){
        this.baseDateBlank = true;
        this.baseDateBlank2 = true;
        this.showDateConfigTable = true;
        this.showTimePlan = false;
        this.showNetValueBaseDate = true;
        this.netBaseDateBlank = false;
      }else if (this.formData.disclosureType=='10'){
        this.$set(this.formData, 'baseDate', '12');
        this.baseDateBlank = false;
        this.baseDateBlank2 = true;
      } else{
        this.showTimePlan = true;
        this.showTimePlanTwo = false;
        this.baseDateDisabled = false;
      }
    },


    //获取版本号
    queryVersion() {
      this.httpUtil.comnQuery({
        action: "DisclosureRule.findDisclosureModsVWithRule",
        params: {disclosureModId: this.formData.disclosureModId}
      }).then(data => {
        this.formData.modVList = data.rows;
      }).catch({})
    },


    //获取模板信息
    queryModel() {
      this.httpUtil.comnQuery({
        action: "DisclosureRule.findDisclosureModsWithRule",
        params: {disclosureType: this.formData.disclosureType,
          disclosureSonType: this.formData.disclosureSonType
        }
      }).then(data => {
        this.modList = data.rows;
      }).catch({})
    },

    //请求信披子类型字典
    selectDisclosureType() {
      this.httpUtil.comnQuery({
        action: "DisclosureMod.getXPTypeByDocType",
        params: {disclosureType: this.formData.disclosureType}
      }).then(data => {
        this.addDocTypeDict = data.rows;
      }).catch({})
    },
    //清空版本框
    disclosureModVersionId() {
      this.$set(this.formData, 'disclosureModVersionId', '');
      this.$set(this.formData, 'modVList', '');
    },


    //产品参数可复选设置
    changeMul() {
      //切换选框模式时滞空数据
      if(!(this.formData.disclosureSonType==='0903'||this.formData.disclosureType==='6')) {
        this.$set(this.formData, 'prodSerCd', '');
        this.$set(this.formData, 'prodInvTyp', '');
        this.$set(this.formData, 'prodClcMth', '');
        this.$set(this.formData, 'prodObj', '');
        this.$set(this.formData, 'invPrdLen', '');
        this.$set(this.formData, 'invPrdDime', '');
        this.$set(this.formData, 'prodForm', '');
      }
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
    //T+/T-日期互相校验联动----end----



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
