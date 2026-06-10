<template>
  <div>
    <k-form ref="addBondInfoModelForm" :data-col="2" isFormBodyScreen>

      <div class ="tableLine2" ><span id="JCXX" class="leftText2">基础信息</span></div>

      <k-form-item label="资产编码" v-show="false">
        <k-field-text v-model="formData.scrId"  id="scrId" :data-disabled="formData.scrIdDisabled"/>
      </k-form-item>
      <k-form-item label="债券代码">
        <k-field-text v-model="formData.scrCd" id="scrCd" :data-allowblank="false" :data-max-length="40" :data-disabled=" formData.scrCdDisabled"/>
      </k-form-item>
      <k-form-item label="债券名称">
        <k-field-text v-model="formData.scrShtNm" id="scrShtNm" :data-allowblank="false" :data-max-length="256" :data-disabled=" formData.scrShtNmDisabled"/>
      </k-form-item>
      <k-form-item label="债券全称">
        <k-field-text v-model="formData.scrNm" id="scrNm" :data-max-length="512" :data-disabled=" formData.scrNmDisabled"/>
      </k-form-item>
      <k-form-item label="市场">
        <k-field-select v-model="formData.trxMkt" id="trxMkt"
                        :data-data="trxMktDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE"
                        :data-allowblank="false"
                        :data-disabled="formData.trxMktDisabled"
                        @data-on-change="changeTrxMkt"/>
      </k-form-item>
      <k-form-item label="交易流通场所">
        <k-field-select v-model="formData.trxPla" id="trxPla" :data-allowblank="false"
                        :data-data="tacdingPlaceDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE"
                        :data-disabled="formData.trxPlaDisabled"/>
      </k-form-item>
      <k-form-item label="发行价(元)">
        <k-field-text v-model="formData.isuPrc" id="isuPrc" :data-allowblank="false" :data-disabled=" formData.isuPrcDisabled" data-validate-type="money" :data-digits="5" :data-integer-length="4" data-show-gbmoney="true"/>
      </k-form-item>
      <k-form-item label="发行量(元)">
        <k-field-text v-model="formData.actlIsuTotAmt" id="actlIsuTotAmt" :data-allowblank="true" :data-disabled=" formData.actlIsuTotAmtDisabled" data-validate-type="money" :data-digits="2" :data-integer-length="16" data-show-gbmoney="true"/>
      </k-form-item>
      <k-form-item label="发行方式">
        <k-field-select v-model="formData.isuMth" id="isuMth" data-dict="iss_mode_bond" :data-disabled=" formData.isuMthDisabled"/>
      </k-form-item>
      <k-form-item label="担保方式">
        <k-field-select v-model="formData.grntMth" id="grntMth" data-dict="grntMth" :data-disabled=" formData.grntMthDisabled"/>
      </k-form-item>
      <k-form-item label="是否信用债">
        <k-field-select v-model="formData.isCredit" id="isCredit" data-dict="1yes0no" :data-disabled=" formData.isCreditDisabled"/>
      </k-form-item>
      <k-form-item label="是否含权">
        <k-field-select v-model="formData.embOptF" id="embOptF" :data-allowblank="true" data-dict="1yes2no" :data-disabled=" formData.embOptFDisabled"/>
      </k-form-item>
      <k-form-item label="是否提前还本">
        <k-field-select v-model="formData.isRepaid" id="isRepaid" :data-allowblank="true" data-dict="1yes2no" :data-disabled=" formData.isRepaidDisabled"/>
      </k-form-item>
      <k-form-item label="起息日">
        <k-field-date v-model="formData.valDt"  id="valDt" :dataMaxValue="formData.mtuDt" :data-allowblank="false" :data-disabled="formData.valDtDisabled" />
      </k-form-item>
      <k-form-item label="到期日">
        <k-field-date v-model="formData.mtuDt" id="mtuDt" :dataMinValue="formData.valDt" :data-allowblank="false" :data-disabled="formData.mtuDtDisabled" />
      </k-form-item>
      <k-form-item label="付息频率">
        <k-field-select v-model="formData.payIntrFrq" id="payIntrFrq" :data-allowblank="false" data-dict="payIntrFrq"  :data-disabled=" formData.payIntrFrqDisabled"/>
      </k-form-item>
      <k-form-item label="计息基础">
        <k-field-select v-model="formData.intrBas" id="intrBas" :data-allowblank="false" data-dict="intrBas" :data-disabled=" formData.intrBasDisabled"/>
      </k-form-item>
      <k-form-item label="计息方式">
        <k-field-select v-model="formData.intrMth" id="intrMth" :data-allowblank="false" data-dict="intrMth" :data-disabled=" formData.intrMthDisabled"/>
      </k-form-item>
      <k-form-item label="息票类型">
        <k-field-select v-model="formData.couponType" id="couponType" :data-allowblank="true" data-dict="couponType" :data-disabled=" formData.couponTypeDisabled"/>
      </k-form-item>
      <k-form-item label="票面利率(%)">
        <k-field-text v-model="formData.parRat" id="parRat" :data-allowblank="false" :data-disabled=" formData.parRatDisabled" data-validate-type="number" :data-digits="5" :data-integer-length="4" />
      </k-form-item>
      <k-form-item label="利差(%)">
        <k-field-text v-model="formData.sprd" id="sprd" :data-allowblank="this.formData.couponType !== '2'" :data-disabled=" formData.sprdDisabled" data-validate-type="number" :data-digits="5" :data-integer-length="4" />
      </k-form-item>


      <template v-if="formData.embOptF==='01'">
        <div class ="tableLine" ><span class="leftText">含权信息</span><div class="itemsCorn"></div></div>
        <div class="continue-select" @click="embOptFGridData.rows.push({})"  v-if="isDetailShow?false:disabledVal?!opFuFlagDisabled:true">
          <svg-icon icon-class="add"></svg-icon>添加含权信息
        </div>
        <k-grid data-fixed="right" ref="embOptFGrid" :data-data="embOptFGridData" id="embOptFGridData" :dataPageSize="0" :data-display="false" data-operate-width="57px" class="continue-ele">
          <k-grid-column data-header="行权日期" data-name="exerciseDate" data-width="247px" >
            <template slot-scope="scope">
              <k-field-date v-model="scope.row.row.exerciseDate" :data-disabled=" formData.exerciseDateDisabled" :data-allowblank="false" :data-clearable="true" :dataMinValue="'('+formData.valDt" ></k-field-date>
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
              <k-field-date v-model="scope.row.row.repayDate" :data-disabled=" formData.repayDateDisabled" :data-allowblank="false" :data-clearable="true" :dataMinValue="'('+formData.valDt"></k-field-date>
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

      <template v-if="formData.couponType==='2'">
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

      <div class ="tableLine1" ><span class="leftText">主体信息</span><div class="itemsCorn"></div></div>
      <k-form-item label="发行主体">
        <k-field-select v-model="formData.isu" id="isu"  :data-allowblank="false"  :data-disabled="formData.isuDisabled" data-action="T8OrgSheet.findOrgNmAll" :dataRemote="true" :data-params="{orgFullName:this.formData.isu}" data-value-field="orgNbrExt" data-display-field="orgFullName" @data-on-change="findOrgInfo"/>
      </k-form-item>
      <k-form-item label="原始权益人" >
        <k-field-text v-model="formData.orignInterestObject" id="orignInterestObject" :data-disabled=" formData.orignInterestObjectDisabled" :data-allowblank="false" :data-max-length="256"/>
      </k-form-item>
      <k-form-item label="中债发行机构所属行业" >
        <k-field-select v-model="formData.ccIndustryIssuer" id="ccIndustryIssuer" :data-disabled=" formData.ccIndustryIssuerDisabled" :data-allowblank="true" data-dict="isuOrgBlgIdt"/>
      </k-form-item>
      <k-form-item label="中债发行机构类型（按规模划分）">
        <k-field-select v-model="formData.isuOrgTypScaleSiz" id="isuOrgTypScaleSiz" :data-allowblank="false" :data-disabled=" formData.isuOrgTypScaleSizDisabled" data-dict="debtor_scale_type"/>
      </k-form-item>
      <k-form-item label="中债发行机构类型（按技术领域划分）">
        <k-field-select v-model="formData.isuOrgTypTchno" id="isuOrgTypTchno" :data-allowblank="false" :data-disabled=" formData.isuOrgTypTchnoDisabled" data-dict="isuOrgTypTchno"/>
      </k-form-item>
      <k-form-item label="中债发行机构类型（按经济类型划分）">
        <k-field-select v-model="formData.isuOrgTypEcn"  id="isuOrgTypEcn" :data-allowblank="false" :data-disabled=" formData.isuOrgTypEcnDisabled" data-dict="isuOrgTypEcn"/>
      </k-form-item>
      <k-form-item label="登记托管机构">
        <k-field-select v-model="formData.regTrstOrg" id="regTrstOrg" :data-allowblank="false" :data-disabled=" formData.regTrstOrgDisabled" data-dict="regTrstOrg"/>
      </k-form-item>
      <k-form-item label="担保机构">
        <k-field-select v-model="formData.grnt" id="grnt"
                        :data-allowblank="true"
                        :data-disabled=" formData.grntDisabled"
                        :data-multiple="true"
                        data-action="T8OrgSheet.findOrgNmAll"
                        :data-params="{orgFullName:this.formData.grnt}"
                        :dataRemote="true"
                        data-value-field="orgNbrExt"
                        data-display-field="orgFullName" />
      </k-form-item>

      <div class ="tableLine1" ><span class="leftText">评级信息</span><div class="itemsCorn"></div></div>
      <k-form-item label="债项发行评级">
        <k-field-select v-model="formData.isuBndRat" id="isuBndRat" :data-allowblank="true" :data-disabled=" formData.isuBndRatDisabled" data-dict ="mainRating" />
      </k-form-item>
      <k-form-item label="债项当前评级（外部）">
        <k-field-select v-model="formData.bondFrsRat" id="bondFrsRat" :data-allowblank="true" :data-disabled=" formData.bondFrsRatDisabled" data-dict ="mainRating" />
      </k-form-item>
      <k-form-item label="主体评级（外部）">
        <k-field-select v-model="formData.mainRat" id="mainRat" :data-allowblank="false" :data-disabled=" formData.mainRatDisabled" data-dict ="mainRating" />
      </k-form-item>
      <k-form-item label="担保人评级（外部）">
        <k-field-select v-model="formData.grntRat" id="grntRat"  :data-allowblank="false" :data-disabled=" formData.grntRatDisabled" data-dict ="mainRating" />
      </k-form-item>

      <div class ="tableLine1" ><span class="leftText">资讯信息</span><div class="itemsCorn"></div></div>
      <k-form-item label="资讯分类">
        <k-field-select v-model="formData.assInfClass" id="assInfClass"  :data-allowblank="false" :data-disabled=" formData.assInfClassDisabled" data-dict ="assInfClass" />
      </k-form-item>
      <k-form-item label="债股类别">
        <k-field-select v-model="formData.debtEquityClass" id="debtEquityClass"  :data-allowblank="true" :data-disabled=" formData.debtEquityClassDisabled" data-dict ="debtEquityClass" />
      </k-form-item>
      <k-form-item label="是否永续">
        <k-field-select v-model="formData.isSustain" id="isSustain"  :data-allowblank="true" :data-disabled=" formData.isSustainDisabled" data-dict ="1yes2no" />
      </k-form-item>

      <k-form-footer slot="footer" data-align="center" v-if="!isDetailShow">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-handler="submitHandle"  data-from="addBondInfoModelForm"
               :data-model="formData" >
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
  name:"BondInfoModelEdit",
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
      //三个列表数据
      embOptFGridData: {},
      isRepaidGridData: {},
      couponTypeGridData: {},
      formData: {},
      //三个列表可操作增删标识
      opFuFlagDisabled: true,
      opAdFlagDisabled: true,
      opFlFlagDisabled: true,
      trxMktDict: {},
      tacdingPlaceDict: {},
      //校验数据
      newLabel:[],
    };
  },
  created() {
    this.formData = this.info;
    this.embOptFGridData = this.embOptFGrid;
    this.isRepaidGridData = this.isRepaidGrid;
    this.couponTypeGridData = this.couponTypeGrid;
    if (this.disabledVal) {
      let formDatas = {...this.formData}
      for (let f in formDatas) {
        formDatas[f + 'Disabled'] = true
      }
      this.formData = { ...formDatas }
      if (!this.isDetailShow) {
        //债券基本信息修改
        this.checkColumn('1', '02');
        //还本信息
        this.checkColumn('20', '02');
        //浮息信息
        this.checkColumn('21', '02');
        //行权信息
        this.checkColumn('22', '02');
      }
    }
    AssetCommon.areaDict(this,'trxMktDict','market_asset','1,2,3,4,99',false);
    AssetCommon.areaDict(this,'tacdingPlaceDict','tacdingPlace','01,03,04,99',false);
  },
  methods: {

    MenuSelect(index) {
      this.activeMenu = index;
      this.scrollToTarget('JCXX');
    },
    scrollToTarget(id) {
      const target = document.getElementById(id);
      if (target) { target.scrollIntoView({ behavior: 'smooth' }); }
    },
    changeTrxMkt(){
      this.$set(this.formData,"trxPla","");
      if (this.formData.trxMkt === '1') {
        this.formData.trxPla= '03'
      }
      if (this.formData.trxMkt === '2') {
        this.formData.trxPla= '04'
      }
      if (this.formData.trxMkt === '3') {
        this.formData.trxPla= '01'
      }
    },

    findOrgInfo() {
      this.$set(this.formData, 'isuOrgTypScaleSiz', '');
      this.$set(this.formData, 'ccIndustryIssuer', '');
      this.$set(this.formData, 'isuOrgTypEcn', '');
      this.$set(this.formData, 'isuOrgTypTchno', '');
      this.$set(this.formData, 'mainRat', '');
      this.$set(this.formData, 'grntRat', '');
      this.httpUtil.comnQuery({
        action: "T8OrgSheet.findOrgInfo",
        params: {orgNbrExt: this.formData.isu}
      }).then(data => {
        this.formData.isuOrgTypScaleSiz = data.rows[0].isuOrgTypScaleSiz;
        this.formData.ccIndustryIssuer = data.rows[0].ccIndustryIssuer;
        this.formData.isuOrgTypTchno = data.rows[0].isuOrgTypTchno;
        this.formData.isuOrgTypEcn = data.rows[0].isuOrgTypEcn;
        this.formData.mainRat = data.rows[0].orgOutRat;
        this.formData.grntRat = data.rows[0].orgOutRat;
      }).catch({})
    },
    defaultParamDeal(val){
      val.scrId=AssetCommon.dealDefaultVal(this.disabledVal?val.scrId:'',val.scrCd+"."+val.trxMkt+"."+"4");
      //因修改界面和补录界面该字段需要联动。新增或修改了【债股类别】时，需要变更补录数据中的【G06二级分类】数据。
      val.ggCbcSubType=AssetCommon.dealDefaultVal(val.debtEquityClass==='02'?'1.6.4':(this.formData.ggCbcSubType==='1.6.4'?'':this.formData.ggCbcSubType),'');
    },
    submitHandle(value) {

      let result = this.$refs.addBondInfoModelForm.validate();

      //校验添加的行权数据是否有空
      if (value.embOptF == "01" && this.embOptFGridData.rows && this.embOptFGridData.rows.length > 0) {
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
      if (value.couponType == "2" && this.couponTypeGridData.rows && this.couponTypeGridData.rows.length > 0) {
        this.couponTypeGridData.rows.forEach((t) => {
          if (!t.flBeginDate) {Tools.alert("请输入浮息起息日！", "danger"); result = false; return;}
          if (!t.flEndDate) {Tools.alert("请输入浮息结束日！", "danger");result = false; return;}
          if (!t.baseRate) {Tools.alert("请输入基础利率！", "danger");result = false; return;}
        });
        value.couponTypeGridData = JSON.stringify({ couponTypeGridData: this.couponTypeGridData.rows });
      }


      if (result) {
        /*清理联动隐藏数据*/
        this.removeVifRubbish(value);
        /*处理默认值*/
        this.defaultParamDeal(value);
        this.httpUtil.ajax({
          url: this.disabledVal?"server/form/DpsApp/bondInfo/editBondInfoModel.json":"server/form/DpsApp/bondInfo/addBondInfoModel.json",
          params: value,
          successAlert: true,
        }).then(data => {
          if (data.success === true) {
            this.$emit('loadGriding',this.formData);
          }
        });
      }else {
        return false;
      }
    },

    /**
     * 查询配置的可编辑字段
     * @param page
     * @param fieldType
     */
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

    //垃圾v-if判断不显示label后，不清除文本框内容;
    //该方法处理这不合理的问题，选框、文本框添加id属性与v-model相同,如果需要不显示label但框内保持不清空，用v-show;
    removeVifRubbish(val) {
      let aaa = Object.keys(val);
      this.newLabel = this.$refs.addBondInfoModelForm.formItems;
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
  },
};
</script>
<style scoped lang="scss">
@import "../AssetComFunction.css";
</style>
