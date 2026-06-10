<template>
  <div>
    <k-form ref="blInfoModelForm" :data-col="2" isFormBodyScreen>

      <div class ="tableLine2" ><span id="BLXX" class="leftText2">补录信息</span></div>

      <k-form-item label="证券编码" v-show="false">
        <k-field-text v-model="formData.scrId"  id="scrId" :data-disabled="formData.scrIdDisabled"/>
      </k-form-item>


      <k-form-item label="发行价（元）">
        <k-field-text v-model="formData.issuePrice" id="issuePrice" :data-allowblank="false" :data-disabled=" formData.issuePriceDisabled" data-validate-type="number" :data-integer-length="4" :data-digits="5" />
      </k-form-item>
      <k-form-item label="发行量（亿元）">
        <k-field-text v-model="formData.issueVolume" id="issueVolume" :data-allowblank="true" :data-disabled=" formData.issueVolumeDisabled" data-validate-type="number" :data-digits="2" :data-integer-length="16"  />
      </k-form-item>
      <k-form-item label="担保方式">
        <k-field-select v-model="formData.guarType" id="guarType" data-dict="grntWay"  :data-disabled=" formData.guarTypeDisabled"/>
      </k-form-item>
      <k-form-item label="是否含权">
        <k-field-select v-model="formData.isExercise" id="isExercise" :data-allowblank="true" data-dict="1yes2no" :data-disabled=" formData.isExerciseDisabled"/>
      </k-form-item>
      <k-form-item label="是否提前还本">
        <k-field-select v-model="formData.isRepaid" id="isRepaid" :data-allowblank="true" data-dict="1yes2no" :data-disabled=" formData.isRepaidDisabled"/>
      </k-form-item>

      <div class ="tableLine1" ><span class="leftText">计息信息</span><div class="itemsCorn"></div></div>

      <k-form-item label="起息日">
        <k-field-date v-model="formData.beginDate" id="beginDate" :data-allowblank="false"  :data-disabled=" formData.beginDateDisabled" @data-on-change="changedt"/>
      </k-form-item>
      <k-form-item label="到期日">
        <k-field-date v-model="formData.endDate" id="endDate" :data-allowblank="false" :dataMinValue="'('+this.formData.beginDate" :data-disabled=" formData.endDateDisabled"/>
      </k-form-item>
      <k-form-item label="付息频率">
        <k-field-select v-model="formData.payFreq" id="payFreq" :data-allowblank="false" data-dict="payIntrFrq"  :data-disabled=" formData.payFreqDisabled"/>
      </k-form-item>
      <k-form-item label="计息基础">
        <k-field-select v-model="formData.interestBase" id="interestBase" :data-allowblank="false" data-dict="intrBas" :data-disabled=" formData.interestBaseDisabled"/>
      </k-form-item>
      <k-form-item label="计息方式">
        <k-field-select v-model="formData.interestMode" id="interestMode" :data-allowblank="false" data-dict="intrMth" :data-disabled=" formData.interestModeDisabled"/>
      </k-form-item>
      <k-form-item label="息票品种">
        <k-field-select v-model="formData.interestType" id="interestType" :data-allowblank="true" data-dict="couponType" :data-disabled=" formData.interestTypeDisabled"/>
      </k-form-item>
      <k-form-item label="票面利率(%)">
        <k-field-text v-model="formData.couponRate" id="couponRate" :data-allowblank="false" :data-disabled=" formData.couponRateDisabled" data-validate-type="number" :data-digits="5" :data-integer-length="4" />
      </k-form-item>
      <k-form-item label="利差(%)">
        <k-field-text v-model="formData.bondSpread" id="bondSpread" :data-allowblank="this.formData.interestType !== '2'" :data-disabled=" formData.bondSpreadDisabled" data-validate-type="number" :data-digits="5" :data-integer-length="4"  />
      </k-form-item>

      <template v-if="formData.isExercise==='01'">
        <div class ="tableLine" ><span class="leftText">含权信息</span><div class="itemsCorn"></div></div>
        <div class="continue-select" @click="embOptFGridData.rows.push({})"  v-if="isDetailShow?false:disabledVal?!opFuFlagDisabled:true">
          <svg-icon icon-class="add"></svg-icon>添加含权信息
        </div>
        <k-grid data-fixed="right" ref="embOptFGrid" :data-data="embOptFGridData" id="embOptFGridData" :dataPageSize="0" :data-display="false" data-operate-width="57px" class="continue-ele">
          <k-grid-column data-header="行权日期" data-name="exerciseDate" data-width="247px" >
            <template slot-scope="scope">
              <k-field-date v-model="scope.row.row.exerciseDate" :data-disabled=" formData.exerciseDateDisabled" :data-allowblank="false" :data-clearable="true"></k-field-date>
            </template>
          </k-grid-column>
          <k-grid-column data-width="118" ></k-grid-column>
          <k-grid-column data-header="利率补偿(%)" data-name="exCouponRate"  data-width="247px">
            <template slot-scope="scope">
              <k-field-text v-model="scope.row.row.exCouponRate" :data-disabled=" formData.exCouponRateDisabled" :data-allowblank="false" :data-clearable="true" data-validate-type="number" :data-digits="5" :data-integer-length="4" ></k-field-text>
            </template>
          </k-grid-column>
          <template slot="operate" slot-scope="scope">
            <k-btn class="md-danger md-just-icon md-simple"
                   :data-handler="()=>embOptFGridData.rows.splice(scope.row.row.row_index-1,1)"
                   data-size="mini" data-type="danger"
                   v-if="isDetailShow?false:disabledVal?!opFuFlagDisabled:true"
                   data-descript="删除">
              <md-icon>close</md-icon>
            </k-btn>
          </template>
        </k-grid>
      </template>

      <template v-if="formData.isRepaid==='01'">
        <div class ="tableLine" ><span class="leftText">还本信息</span><div class="itemsCorn"></div></div>
        <div class="continue-select" @click="isRepaidGridData.rows.push({})" v-if="isDetailShow?false:disabledVal?!opAdFlagDisabled:true">
          <svg-icon icon-class="add"></svg-icon>添加还本信息
        </div>
        <k-grid data-fixed="right" ref="isRepaidGrid" :data-data="isRepaidGridData" id="isRepaidGridData" :dataPageSize="0" :data-display="false" data-operate-width="57px" class="continue-ele">
          <k-grid-column data-header="提前还本日期" data-name="repayDate"  data-width="247px">
            <template slot-scope="scope">
              <k-field-date v-model="scope.row.row.repayDate" :data-disabled=" formData.repayDateDisabled" :data-allowblank="false" :data-clearable="true"></k-field-date>
            </template>
          </k-grid-column>
          <k-grid-column data-width="118" ></k-grid-column>
          <k-grid-column data-header="单位还本金额(元)" data-name="unitPrincipal"  data-width="247px">
            <template slot-scope="scope">
              <k-field-text v-model="scope.row.row.unitPrincipal" :data-disabled=" formData.unitPrincipalDisabled" :data-allowblank="false" :data-clearable="true" data-validate-type="money" :data-digits="2" :data-integer-length="7" data-show-gbmoney="true"></k-field-text>
            </template>
          </k-grid-column>
          <template slot="operate" slot-scope="scope">
            <k-btn class="md-danger md-just-icon md-simple"
                   :data-handler="()=>isRepaidGridData.rows.splice(scope.row.row.row_index-1,1)"
                   v-if="isDetailShow?false:disabledVal?!opAdFlagDisabled:true"
                   data-size="mini" data-type="danger"  data-descript="删除">
              <md-icon>close</md-icon>
            </k-btn>
          </template>
        </k-grid>
      </template>

      <template v-if="formData.interestType==='2'">
        <div class ="tableLine" ><span class="leftText">浮息信息</span><div class="itemsCorn"></div></div>
        <div class="continue-select" @click="couponTypeGridData.rows.push({})" v-if="isDetailShow?false:disabledVal?!opFlFlagDisabled:true">
          <svg-icon icon-class="add"></svg-icon>添加浮息信息
        </div>
        <k-grid data-fixed="right" ref="couponTypeGrid" :data-data="couponTypeGridData" id="couponTypeGridData" :dataPageSize="0" :data-display="false" data-operate-width="57px" class="continue-ele">
          <k-grid-column data-header="浮息起息日" data-name="flBeginDate"  data-width="204px">
            <template slot-scope="scope">
              <k-field-date v-model="scope.row.row.flBeginDate" :data-disabled=" formData.flBeginDateDisabled" :data-allowblank="false" :data-clearable="true"></k-field-date>
            </template>
          </k-grid-column>
          <k-grid-column data-header="浮息结束日" data-name="flEndDate"  data-width="204px">
            <template slot-scope="scope">
              <k-field-date v-model="scope.row.row.flEndDate" :data-disabled=" formData.flEndDateDisabled" :data-allowblank="false" :data-clearable="true"></k-field-date>
            </template>
          </k-grid-column>
          <k-grid-column data-header="基础利率(%)" data-name="baseRate"  data-width="204px">
            <template slot-scope="scope">
              <k-field-text v-model="scope.row.row.baseRate" :data-disabled=" formData.baseRateDisabled" :data-allowblank="false" data-validate-type="number" :data-digits="5" :data-integer-length="4"></k-field-text>
            </template>
          </k-grid-column>
          <template slot="operate" slot-scope="scope">
            <k-btn class="md-danger md-just-icon md-simple"
                   :data-handler="()=>couponTypeGridData.rows.splice(scope.row.row.row_index-1,1)"
                   v-if="isDetailShow?false:disabledVal?!opFlFlagDisabled:true"
                   data-size="mini" data-type="danger"  data-descript="删除">
              <md-icon>close</md-icon>
            </k-btn>
          </template>
        </k-grid>
      </template>



      <div class ="tableLine1" ><span class="leftText">评级信息</span><div class="itemsCorn"></div></div>
      <k-form-item label="担保机构">
        <k-field-select v-model="formData.guaranteer" id="guaranteer" :data-allowblank="true" :data-disabled="formData.guaranteerDisabled" :data-multiple="true" data-action="T8OrgSheet.findOrgNmAll" :data-params="{orgFullName:this.formData.guaranteer}" :dataRemote="true" data-value-field="orgNbrExt" data-display-field="orgFullName" />
      </k-form-item>
      <k-form-item label="债项发行评级">
        <k-field-select v-model="formData.isuBndRat" id="isuBndRat" :data-allowblank="true" :data-disabled="formData.isuBndRatDisabled" data-dict ="mainRating" />
      </k-form-item>
      <k-form-item label="债项当前评级（外部）">
        <k-field-select v-model="formData.bondCredit" id="bondCredit" :data-allowblank="true" :data-disabled="formData.bondCreditDisabled" data-dict ="mainRating" />
      </k-form-item>
      <k-form-item label="担保人评级（外部）">
        <k-field-select v-model="formData.grntRat" id="grntRat" :data-allowblank="false" :data-disabled="formData.grntRatDisabled" data-dict ="mainRating" />
      </k-form-item>




      <div class ="tableLine1" ><span class="leftText">中债分类</span><div class="itemsCorn"></div></div>
      <k-form-item label="中债一级分类">
        <k-field-select v-model="formData.cbndFrsCtg" id="cbndFrsCtg"
                        :data-allowblank="false"
                        :data-disabled="formData.cbndFrsCtgDisabled"
                        :data-data="cbndFrsCtgDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE" />
      </k-form-item>
      <k-form-item label="中债二级分类">
        <k-field-select v-model="formData.cbndScdCtg" id="cbndScdCtg"
                        :data-allowblank="false"
                        :data-disabled="formData.cbndScdCtgDisabled"
                        :data-data="cbndScdCtgDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE"  />
      </k-form-item>
      <k-form-item label="具体类别">
        <k-field-select v-model="formData.spcType"
                        :data-allowblank="true" id="spcType"
                        :data-disabled="formData.spcTypeDisabled"
                        data-dict ="spcType" />
      </k-form-item>

      <div class ="tableLine1" ><span class="leftText">G06分类</span><div class="itemsCorn"></div></div>
      <k-form-item label="G06一级分类">
        <k-field-select v-model="formData.ggCbcType" id="ggCbcType"
                        :data-allowblank="false"
                        :data-disabled="formData.ggCbcTypeDisabled"
                        :data-data="ggCbcTypeDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE" />
      </k-form-item>
      <k-form-item label="G06二级分类">
        <k-field-select v-model="formData.ggCbcSubType" id="ggCbcSubType"
                        :data-allowblank="false"
                        :data-disabled="formData.ggCbcSubTypeDisabled"
                        :data-data="ggCbcSubTypeDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE"/>
      </k-form-item>

      <div class ="tableLine1" ><span class="leftText">人行分类</span><div class="itemsCorn"></div></div>
      <k-form-item label="人行一级分类">
        <k-field-select v-model="formData.pbnkFrsCtg" id="pbnkFrsCtg"
                        :data-allowblank="false"
                        :data-disabled="formData.pbnkFrsCtgDisabled"
                        :data-data="pbnkFrsCtgDict"
                        data-value-field="VALUE"
                        data-display-field="TEXT"/>
      </k-form-item>
      <k-form-item label="人行二级分类">
        <k-field-select v-model="formData.pbnkScdCtg"  id="pbnkScdCtg"
                        :data-allowblank="false"
                        :data-disabled="formData.pbnkScdCtgDisabled"
                        @data-on-change="changePbnkScdCtg"
                        :data-data="pbnkScdCtgDict"
                        data-value-field="VALUE"
                        data-display-field="TEXT" />
      </k-form-item>
      <k-form-item label="人行三级分类" v-if="this.formData.pbnkScdCtg==='e3'">
        <k-field-select v-model="formData.pbnkTrdCtg" id="pbnkTrdCtg"
                        :data-allowblank="false"
                        :data-disabled="formData.pbnkTrdCtgDisabled"
                        :data-data="pbnkTrdCtgDict"
                        data-value-field="VALUE"
                        data-display-field="TEXT" />
<!--      <k-form-item label="人行四级分类">-->
<!--        <k-field-select v-model="formData.pbnkFurCtg" id="pbnkFurCtg" :data-allowblank="false"-->
<!--                        :data-disabled="formData.pbnkFurCtgDisabled"-->
<!--                        :data-data="pbnkFurCtgDict"-->
<!--                        data-value-field="VALUE"-->
<!--                        data-display-field="TEXT" />-->
<!--      </k-form-item>-->
      </k-form-item>
      <k-form-item label="人行发行机构所属行业" >
        <k-field-select v-model="formData.publisherTradePb" id="publisherTradePb" :data-allowblank="false" :data-disabled="formData.publisherTradePbDisabled" data-dict="isuOrgBlgIdt"/>
      </k-form-item>
      <k-form-item label="人行发行机构企业规模">
        <k-field-select v-model="formData.publisherScalePb" id="publisherScalePb" :data-allowblank="false" :data-disabled="formData.publisherScalePbDisabled" data-dict ="debtor_type" />
      </k-form-item>
      <k-form-item label="版本" >
        <k-field-text v-model="formData.version" id="version" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="备注" :data-col="2">
        <k-field-text v-model="formData.cmt" id="cmt" inputType="textarea" :rows="3" :data-disabled="formData.cmtDisabled" :data-allowblank="!(this.formData.isuOrgTypEcn === '99'||this.formData.isuOrgTypTchno === '99'||this.formData.isuOrgTypScaleSiz === '99')" :data-max-length="256"/>
      </k-form-item>

      <k-form-footer slot="footer" data-align="center" v-if="!isDetailShow">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-handler="submitHandle"  data-from="blInfoModelForm"
               :data-model="formData">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">
          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
      </k-form-footer>
    </k-form>
  </div>
</template>
<script>
import Tools from "@/utils/tools";
import AssetCommon from "@/pages/pms/asset/AssetComFunction";

export default {
  name:"MidDirectFusionCollection",
  props: {
    embOptFGrid: {
      type:Object,
    },
    isRepaidGrid: {
      type:Object,
    },
    couponTypeGrid: {
      type:Object,
    },
    info : {
      type:Object,
      default: ()=>{
        return {}
      }
    },
    //是否是不可修改
    disabledVal: {
      type:Boolean,
    },
    //是否只作为详情展示
    isDetailShow: {
      type:Boolean,
      default:false
    },
  },
  data() {
    return {
      embOptFGridData: {},
      isRepaidGridData: {},
      couponTypeGridData: {},
      newLabel:[],
      formData: {},
      //三个列表可操作增删标识
      opFuFlagDisabled: true,
      opAdFlagDisabled: true,
      opFlFlagDisabled: true,
      //中债
      cbndFrsCtgDict:{},
      cbndScdCtgDict:{},
      //g06
      ggCbcTypeDict:{},
      ggCbcSubTypeDict:{},
      //人行
      pbnkFrsCtgDict: {},
      pbnkScdCtgDict:{},
      pbnkTrdCtgDict:{},
      pbnkFurCtgDict:{},
    };
  },
  created() {
    this.embOptFGridData = this.embOptFGrid;
    this.isRepaidGridData = this.isRepaidGrid;
    this.couponTypeGridData = this.couponTypeGrid;
    this.formData = this.info;
    console.log( this.formData);
    //如果已经补录过（即存在版本号），则补录字段不需要默认值
    if (!this.formData.version){
      this.defaultParamDeal(this.formData);
    }
    if (this.disabledVal) {
      let formDatas = {...this.formData}
      for (let f in formDatas) {
        formDatas[f + 'Disabled'] = true
      }
      this.formData = { ...formDatas }
      if (!this.isDetailShow) {
        //直融基本信息修改
        this.checkColumn('16','01');
        //还本信息
        this.checkColumn('24','01');
        //浮息信息
        this.checkColumn('25','01');
        //行权信息
        this.checkColumn('26','01');
      }
    }
    //中债字典处理
    AssetCommon.areaDict(this,'cbndFrsCtgDict','cbndFrsCtg','5',false);
    AssetCommon.areaDict(this,'cbndScdCtgDict','cbndScdCtg','2101',false);
    //g06字典处理
    AssetCommon.areaDict(this,'ggCbcTypeDict','g06_first_type',"'1.5'",false);
    AssetCommon.areaDict(this, 'ggCbcSubTypeDict', 'g06_scd_type', '1.5',true);
    //人行字典处理
    AssetCommon.areaDict(this,'pbnkFrsCtgDict','asseFrsCtg','01',false);
    AssetCommon.areaDict(this,'pbnkScdCtgDict','pbnkFrsCtg',"'e'",false);
    AssetCommon.areaDict(this,'pbnkTrdCtgDict','pbnkScdCtg',"'e1'",false);
    // AssetCommon.areaDict(this,'pbnkTrdCtgDict','pbnkScdCtg',this.formData.pbnkScdCtg?this.formData.pbnkScdCtg:'e1',true);
    // AssetCommon.areaDict(this,'pbnkFurCtgDict','pbnkTrdCtg','e',true);
  },
  methods: {

    changePbnkScdCtg(){
      this.$set(this.formData,"pbnkTrdCtg","");
      AssetCommon.areaDict(this,'pbnkTrdCtgDict','pbnkScdCtg',"'e1'",false);
      // AssetCommon.areaDict(this,'pbnkTrdCtgDict','pbnkTrdCtg',this.formData.pbnkScdCtg?this.formData.pbnkScdCtg:'e1',true);
    },
    //处理补录页面默认值
    defaultParamDeal(val){
      val.cbndFrsCtg=AssetCommon.dealDefaultVal(val.cbndFrsCtg,'5');
      val.cbndScdCtg=AssetCommon.dealDefaultVal(val.cbndScdCtg,'2101');
      val.ggCbcType=AssetCommon.dealDefaultVal(val.ggCbcType,'1.5');
      val.ggCbcSubType=AssetCommon.dealDefaultVal(val.ggCbcSubType,'1.5.14');
      val.pbnkFrsCtg=AssetCommon.dealDefaultVal(val.pbnkFrsCtg,'01');
      val.pbnkScdCtg=AssetCommon.dealDefaultVal(val.pbnkScdCtg,'e');
    },
    MenuSelect(index) {
      this.activeMenu = index;
      this.scrollToTarget('BLXX');
    },
    scrollToTarget(id) {
      const target = document.getElementById(id);
      if (target) { target.scrollIntoView({ behavior: 'smooth' }); }
    },

    //垃圾v-if判断不显示label后，不清除文本框内容;
    //该方法处理这不合理的问题，选框、文本框添加id属性与v-model相同,如果需要不显示label但框内保持不清空，用v-show;
    removeVifRubbish(val) {
      let aaa = Object.keys(val);
      this.newLabel = this.$refs.blInfoModelForm.formItems;
      let newLabelEmbOptFGrid="";
      let newLabelIsRepaidGridData="";
      let newLabelCouponTypeGridData="";
      if (this.$refs.embOptFGrid) {
        newLabelEmbOptFGrid = this.$refs.embOptFGrid.id;
      }
      if (this.$refs.isRepaidGrid){
        newLabelIsRepaidGridData = this.$refs.isRepaidGrid.id;
      }
      if (this.$refs.couponTypeGrid) {
        newLabelCouponTypeGridData = this.$refs.couponTypeGrid.id;
      }
      a: for(let j = 0; j < aaa.length; j++){
        let oldLabel = aaa[j].toString();
        for (let i = 0; i < this.newLabel.length; i++) {
          let newLabel = this.newLabel[i].field.id;
          if (oldLabel===newLabel||oldLabel===newLabelEmbOptFGrid ||oldLabel===newLabelIsRepaidGridData || oldLabel===newLabelCouponTypeGridData){
            continue a;
          }
        }
        this.$set(val,oldLabel, '');
      }
    },


    checkColumn(page,fieldType) {
      this.httpUtil.comnQuery({
        action: "AssetCollection.findColumns",
        params: {page: page,fieldType:fieldType}
      }).then(data => {
        let formDatas = {...this.formData}
        if (data && data.rows.length > 0){
          let labels = data.rows[0].label
          let arr = labels.split(',')
          if (arr.length > 0){
            arr.forEach(a =>{
              formDatas[a + 'Disabled'] = false
              if (a + 'Disabled'==='opFuFlagDisabled'){
                this.opFuFlagDisabled=false;
              }
              if (a + 'Disabled'==='opFlFlagDisabled'){
                this.opFlFlagDisabled=false;
              }
              if (a + 'Disabled'==='opAdFlagDisabled'){
                this.opAdFlagDisabled=false;
              }
            })
          }
        }
        this.formData = { ...formDatas }
      }).catch({})
    },


    submitHandle(value) {
      let result = this.$refs.blInfoModelForm.validate();
      if(value.couponRate>100){
        Tools.alert("票面利率不可大于100！", "danger");
        result = false;
        return;
      }
      if(value.bondSpread>100){
        Tools.alert("利差不可大于100！", "danger");
        result = false;
        return;
      }
      //校验添加的行权数据是否有空
      if (value.isExercise == "01" && this.embOptFGridData.rows && this.embOptFGridData.rows.length > 0) {
        this.embOptFGridData.rows.forEach((t) => {
          if (!t.exerciseDate) {Tools.alert("请输入行权日期！", "danger");result = false; return;}
          if (!t.exCouponRate) {Tools.alert("请输入利率补偿！", "danger");result = false; return;}
        });
        value.embOptFGridData = JSON.stringify({ embOptFGridData: this.embOptFGridData.rows });
      }

      //校验添加的还款数据是否有空
      if (value.isRepaid == "01" && this.isRepaidGridData.rows && this.isRepaidGridData.rows.length > 0) {
        this.isRepaidGridData.rows.forEach((t) => {
          if (!t.repayDate) {Tools.alert("请输入还款日期！", "danger");result = false; return;}
          if (!t.unitPrincipal) {Tools.alert("请输入单位还本金额！", "danger");result = false; return;}
        });
        value.isRepaidGridData = JSON.stringify({ isRepaidGridData: this.isRepaidGridData.rows });
      }

      //校验添加的浮息数据是否有空
      if (value.interestType == "2" && this.couponTypeGridData.rows && this.couponTypeGridData.rows.length > 0) {
        this.couponTypeGridData.rows.forEach((t) => {
          if (!t.flBeginDate) {Tools.alert("请输入浮息起息日！", "danger");result = false; return;}
          if (!t.flEndDate) {Tools.alert("请输入浮息结束日！", "danger");result = false; return;}
          if (!t.baseRate) {Tools.alert("请输入基础利率！", "danger");result = false; return;}
        });
        value.couponTypeGridData = JSON.stringify({ couponTypeGridData: this.couponTypeGridData.rows });
      }

      if (result) {
        this.removeVifRubbish(value);
        this.httpUtil.ajax({
          url: "server/form/DpsApp/DrectFunsion/editDrectFunsion.json",
          params: value,
          successAlert: true,
        }).then(data => {
          if (data.success === true) {
            this.$emit('loadGriding',this.formData);
          }
        });
      } else {
        return false;
      }
    },
  },
};
</script>
<style scoped lang="scss">
@import "../AssetComFunction.css";
</style>
