  <template>
  <div>
    <div class="query-div" >
      <k-form ref="T82001Form" :data-col="3" :data-model="ProdFeeDeal">
        <k-form-item data-input-width="150px" v-show="false">
          <k-field-text v-model="ProdFeeDeal.prodCode"  data-placeholder="产品代码"/>
        </k-form-item>
      </k-form>
    </div>
    <div style="min-height:225px;">
      <div class="add-btn-div">
        <div class="add-btn"  @click="addHandler">+</div>
      </div>
        <k-grid ref="t8FeeDealGrid" @data-row-select="selectRow" >
          <k-grid-column data-header="行数" data-name="numId" :data-hidden="true"></k-grid-column>
          <k-grid-column data-header="序号" data-name="id" :data-hidden="true"></k-grid-column>
          <k-grid-column data-header="产品代码" data-name="prodCode"  :data-hidden="true"></k-grid-column>
          <k-grid-column data-header="交易费用类型" data-name="feeTypeDeal" data-dict="t8_fee_type_deal"></k-grid-column>
          <!-- <k-grid-column data-header="交易客户类型" data-name="custTypeDeal"  data-dict="t8_cust_type_deal"></k-grid-column> -->
<!--          <k-grid-column data-header="交易计费基数" data-name="chargingIndexDeal" data-dict="t8_charging_index_deal"></k-grid-column>-->
<!--          <k-grid-column data-header="交易计算方式" data-name="chargingMethod" data-dict="t8_charging_method_deal"></k-grid-column>-->
          <k-grid-column data-header="基本费率%" data-name="baseFeeRate"></k-grid-column>
          <k-grid-column data-header="最高费用" data-name="maxCost"></k-grid-column>
          <k-grid-column data-header="最低费用" data-name="minCost"></k-grid-column>
          <k-grid-column data-header="是否按金额分段" data-name="isAmtSegment" data-dict="t8_prod_isok"></k-grid-column>
          <k-grid-column data-header="是否按时间分段" data-name="isTimeSegment" data-dict="t8_prod_isok"></k-grid-column>
          <k-grid-column data-header="费用说明" data-name="costDesc"  :data-hidden="true"></k-grid-column>
          <k-grid-column data-header="审批状态" data-name="approvalStatus" :data-hidden="true"></k-grid-column>
          <k-grid-column data-header="生命周期状态" data-name="prodStatus" :data-hidden="true"></k-grid-column>
          <k-grid-column data-header="创建日期" data-name="crtDate" :data-hidden="true"></k-grid-column>
          <k-grid-column data-header="创建时间" data-name="crtTime" :data-hidden="true"></k-grid-column>
          <k-grid-column data-header="创建人" data-name="crtUser" :data-hidden="true"></k-grid-column>
          <k-grid-column data-header="更新日期" data-name="updDate" :data-hidden="true"></k-grid-column>
          <k-grid-column data-header="更新时间" data-name="updTime" :data-hidden="true"></k-grid-column>
          <k-grid-column data-header="更新人" data-name="updUser" :data-hidden="true"></k-grid-column>
          <template slot="operate" slot-scope="scope">
            <k-btn  data-functype="POPUP" data-size="mini" data-target="selctComp" class="md-info md-just-icon md-simple"
                    :data-handler="editHandler" data-descript="查看交易费用">
              <md-icon>search</md-icon>
            </k-btn>
            <k-btn data-functype="POPUP" data-size="mini" data-target="editPopup"  class="md-info md-just-icon md-simple"
                   :data-handler="editHandler"  data-descript="修改交易费用">
              <md-icon>edit</md-icon>
            </k-btn>
            <k-btn class="md-danger md-just-icon md-simple" data-functype="POPUP" :data-handler="deleteProdFee"
                   data-type="danger" data-target="t8FeeDealGrid" :data-confirm="true" data-descript="删除交易费用">
              <md-icon>close</md-icon>
            </k-btn>
          </template>
        </k-grid>
    </div>
	<!--    添加交易费用实体类弹出框   -->
    <k-popup ref="addT8FeeDealPopup" data-title="新增">
      <k-form ref="addT8FeeDealForm" :data-col="2">
        <k-form-item label="产品代码" v-show="false">
          <k-field-text v-model="ProdFeeDeal2.prodCode"  />
        </k-form-item>
        <k-form-item label="产品主表id" v-show="false">
          <k-field-text v-model="ProdFeeDeal2.t8ProdInfoId"  />
        </k-form-item>
        <k-form-item label="交易费用类型" >
          <k-field-select v-model="ProdFeeDeal2.feeTypeDeal" data-dict="t8_fee_type_deal"
                          :data-allowblank="false" @data-on-change="changeValue(ProdFeeDeal2.feeTypeDeal)"
                          :data-default-value="'1'" :data-disabled="prodMode=='1'" />
        </k-form-item>
        <!-- <k-form-item label="交易客户类型">
          <k-field-select v-model="ProdFeeDeal2.custTypeDeal" data-dict="t8_cust_type_deal" :data-allowblank="false"/>
        </k-form-item> -->
<!--        <k-form-item label="交易计费基数">-->
<!--          <k-field-select v-model="ProdFeeDeal2.chargingIndexDeal" data-dict="t8_charging_index_deal" :data-default-value="'2'" :data-allowblank="false"/>-->
<!--        </k-form-item>-->
<!--        <k-form-item label="交易计算方式">-->
<!--          <k-field-select v-model="ProdFeeDeal2.chargingMethod" data-dict="t8_charging_method_deal" :data-allowblank="false"/>-->
<!--        </k-form-item>-->
        <k-form-item label="基本费率%" v-if="!this.switchSegmentValue && !this.switchTimeValue">
          <k-field-text v-model="ProdFeeDeal2.baseFeeRate" :data-max-length="8" data-digits="4" data-type="number"  data-validate-type="number"
                        data-integer-length="2"  :data-allowblank="this.switchSegmentValue || this.switchTimeValue"/>
        </k-form-item>
        <k-form-item label="最高费用">
          <k-field-text v-model="ProdFeeDeal2.maxCost" data-type="money" :data-max-length="19" data-digits="2"
                        :data-min-value="this.ProdFeeDeal2.minCost+''"  data-validate-type="money"  data-show-gbmoney="true"
                        data-integer-length="16" />
        </k-form-item>
        <k-form-item label="最低费用">
          <k-field-text v-model="ProdFeeDeal2.minCost" data-type="money" :data-max-length="19" data-digits="2"
                        :data-max-value="this.ProdFeeDeal2.maxCost" data-validate-type="money" data-show-gbmoney="true"
                        data-integer-length="16" />
        </k-form-item>
<!--        <k-form-item label="是否按金额分段" v-show="false">-->
<!--          <k-field-select v-model="ProdFeeDeal2.isAmtSegment" data-default-value="" data-dict="t8_prod_isok"/>-->
<!--        </k-form-item>-->
<!--        <k-form-item label="是否按时间分段" v-show="false">-->
<!--          <k-field-select v-model="ProdFeeDeal2.isTimeSegment" data-dict="t8_prod_isok"/>-->
<!--        </k-form-item>-->
        <!-- <k-form-item label="费用说明" :data-col="2">
          <k-field-text v-model="ProdFeeDeal2.costDesc" :data-allowblank="false"  :data-max-length="2000" inputType="textarea" :rows="1"/>
        </k-form-item> -->
      </k-form>

        <div class="my-container">
          <div class="my-item2" >
            <div style="width: 135px !important"  v-if="ProdFeeDeal2.feeTypeDeal !='3'">
              <md-switch v-model="switchSegmentValue" :disabled="moneySwitchDisabled" class="md-info" @change="changeSegmentType">金额分段</md-switch>
            </div>
            <div class="my-item2-chips" v-show="ProdFeeDeal2.feeTypeDeal !='3'">
              <md-chips v-model="moneyList" class="md-primary" style="padding-top: 4px !important;" md-placeholder="请输入分段金额,并按回车确认" data-show-gbmoney="true"  md-input-type="number"  @md-delete="deleteMoney" @md-insert="insertMoney"  v-show="switchSegmentValue" ></md-chips>
            </div>
            <k-field-text  class="moneyTextClass"  id="moneyText" v-model="nowMoney" data-default-value="0" data-show-gbmoney="true" data-validate-type="money" data-type="money" />
          </div>
          <div class="my-item3" >
            <div style="width: 135px !important" v-show="prodMode!='1' && ProdFeeDeal2.feeTypeDeal =='3'">
              <md-switch v-model="switchTimeValue" :disabled="timeSwitchDisabled" class="md-info" @change="changeTimeType">时间分段</md-switch>
            </div>
            <div class="my-item2-chips" v-show="prodMode!='1' && ProdFeeDeal2.feeTypeDeal =='3'">
              <md-chips v-model="timeList" class="md-primary" style="padding-top: 4px !important;" md-placeholder="请输入分段天数,并按回车确认"  md-input-type="number"  @md-delete="deleteTime" @md-insert="insertTime" v-show="switchTimeValue" ></md-chips>
            </div>
          </div>
        </div>
        <div label="金额分段展示"  class="my-table" v-show="switchSegmentValue || switchTimeValue">
          <md-table>
            <md-table-row>
              <md-table-head v-show="moneyList.length > 0 " >金额段</md-table-head>
              <md-table-head v-show="timeList.length > 0" >持有时间</md-table-head>
              <md-table-head>{{feeTableHead}}</md-table-head>
            </md-table-row>

            <!--没有数据时才展示这一行 -->
            <md-table-row v-show="timeList.length == 0 && moneyList.length == 0">
              <md-table-cell :colspan="3" style="text-align:center">
                <p> 暂无数据</p>
              </md-table-cell>
            </md-table-row>

            <md-table-row   v-show="timeList.length > 0 || moneyList.length > 0" v-for="(item,index) in tailingCommisionList" :key="index">
              <md-table-cell v-show="item.showMoneyTd" :rowspan="item.moneyRowspan" >{{ item.moneyDesc }}</md-table-cell>
              <md-table-cell v-show="item.showTimeTd">{{ item.timeDesc }}</md-table-cell>
              <md-table-cell>
                <md-field>
                  <span class="md-suffix" v-show="ProdFeeDeal2.chargeType=='0'||ProdFeeDeal2.chargeType==''">￥</span>
                  <k-field-text class="md-input" style="width: 80px; text-align: right;" v-model="item.rate"  data-max-value="100"
                                @change="versionChangeFnc(item.rate)"  v-show="ProdFeeDeal2.chargeType=='1' "
                                data-min-value="0"  data-integer-length="3"
                                data-type="money" data-digits="4"/>
<!--                  <md-input class="md-input" style="width: 80px; text-align: right;" v-model="item.rate" maxlength="3" md-input-type="number" -->
<!--                            @change="versionChangeFnc(item.rate)"  v-show="ProdFeeDeal2.chargeType=='1' "></md-input>-->
                  <md-input class="md-input" style="width: 80px; text-align: right;" v-model="item.constantFee" md-input-type="number" v-show="ProdFeeDeal2.chargeType=='0'||ProdFeeDeal2.chargeType==''"></md-input>
                  <span class="md-suffix" v-show="ProdFeeDeal2.chargeType=='1'">%</span>
                </md-field>
              </md-table-cell>
            </md-table-row>
          </md-table>
        </div>
        <div>
          <k-form>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"  data-from="addT8FeeDealForm"
                 :data-handler="beforeSubmit" :data-model="ProdFeeDeal2" data-target="t8FeeDealGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
          </k-form>
        </div>
    </k-popup>
    <k-popup ref="editPopup" data-title="修改">
      <EditComp v-model="ProdFeeDeal2" :info="ProdFeeDeal2" :prodMode="prodMode" :dataParams="dataParams" @getUptDate="getRowData"/>
    </k-popup>
    <k-popup ref="selctComp" data-title="查看">
      <selctComp v-model="ProdFeeDeal2" :info="ProdFeeDeal2" :prodMode="prodMode" :dataParams="dataParams" @getUptDate="getRowData"/>
    </k-popup>
    <k-form  dataLabelWidth="170px" dataInputWidth="300px">
      <k-form-footer data-align="center" v-show="menuName == 'M81007'" >
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdAllInfo.updateFeeDeal" :data-handler="saveProdFeeDeal"
               :data-model="dataParams" data-target=""  :data-after-success="passDataSuccess">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>保存
        </k-btn>
      </k-form-footer>
    </k-form>

</div>
</template>

<script>
import {assign} from "lodash";
import Tools from "@/utils/tools";
import EditComp from "./M81001-FeeDealEdit";
import selctComp from "./M81001-FeeDealSelect";

export default {
  components: {EditComp,selctComp},
  computed: {},
  model: {
    prop: 'ProdFeeDeal',
    event: 'input'
  },
  props: {
    menuName: '',
    ProdFeeDeal: {
      t8PrjFeeLists: [],
    },
    prodCode: {
      type: String,
      default: ''
    },
    prodMode: {
      type: String,
      default: ''
    },
    t8ProdInfoId: {
      type: String,
      default: ''
    },
  },
  data() {
    return {
      selectRowData: {},
      ProdFeeDeal2: {},
      saveLoading: false,
      moneyList: [],
      timeList: [],
      tailingCommisionList: [],
      tailingCommisionMoneyList: [],
      tailingCommisionTimeList: [],
      switchSegmentValue: false,   //金额分段切换按钮的值
      switchTimeValue: false,      //金额分段切换按钮的值
      constantRatesSitch: true,     //固定费率切换按钮的值
      moneySwitchDisabled: false,    //金额分段切换按钮是否可用
      timeSwitchDisabled: false,     //时间分段切换按钮是否可用
      feeTableHead: "费率",
      constantRate: null,
      dataParams: [],
      chargeType: '1',
      rateType: '0',
      nowMoney: 0,
    };
  },
  mounted() {
  },
  methods: {
    passDataSuccess() {
      this.$emit('isShowButton', '1')
    },
    saveProdFeeDeal(val) {
      val["assemblyMenuType"] = 'feeDeal';
      val["t8FeeDeals"] = JSON.stringify(this.dataParams);
      val["prodCode"] = this.ProdFeeDeal.prodCode;
      val["t8ProdInfoId"] = this.ProdFeeDeal.t8ProdInfoId;
    },
    changeValue(feeType) {
      this.moneySwitchDisabled = false;   //金额分段切换按钮是否可用
      this.timeSwitchDisabled = false;    //时间分段切换按钮是否可用
      this.switchSegmentValue = false;   //金额分段切换按钮的值
      this.switchTimeValue = false;      //金额分段切换按钮的值
    },
    versionChangeFnc(value) {
    },

      validateData() {
        return this.$refs.feeDeal.validate();
      },

      beforeSubmit : function(value){
        if(this.ProdFeeDeal2.prodCode==null || this.ProdFeeDeal2.prodCode==''){
          Tools.alert("未取到产品代码","danger");
          return false;
        }

        let ret = this.$refs.addT8FeeDealForm.validate();
        if(!ret){
          return false;
        }

        //验证费用类型和销售对象类型
        let flags = true;
        this.dataParams.forEach(function(i,index){
          if(value.feeTypeDeal == i.feeTypeDeal && value.custTypeDeal == i.custTypeDeal){
            flags = false;
          }
        });

        if(this.switchTimeValue || this.switchSegmentValue){
          value.baseFeeRate = '';
        }

        if(!flags){
          Tools.alert("客户对象已经设置了该费用类型，不要重复设置!","danger");
          return false;
        }



        //标记校验是否通过，是否允许提交
        let commitFlag = true;
        let errorMsg = "";
        if(this.switchTimeValue){
          value.isTimeSegment = '1';
          value.isAmtSegment = '0';
          if(this.tailingCommisionList.length < 1){
            commitFlag = false;
            errorMsg = "请输入时间分段";
          }
        }else
        if(this.switchSegmentValue){
          value.isAmtSegment = '1';
          value.isTimeSegment = '0';
          if(this.tailingCommisionList.length < 1){
            commitFlag = false;
            errorMsg = "请输入金额分段";
          }
        }else{
          value.isTimeSegment = '0';
          value.isAmtSegment = '0';
        }
        this.$refs.addT8FeeDealForm.isAmtSegment = this.ProdFeeDeal2.isAmtSegment;
        this.$refs.addT8FeeDealForm.isTimeSegment = this.ProdFeeDeal2.isTimeSegment;
        if(this.switchTimeValue || this.switchSegmentValue ){
          //维度组合：（0-不分段；1-金额段；2-持有时间；3-客户类型）；多个维度组合直接将多个值串连起来，最多三个维度组合

          //分段
          this.tailingCommisionList.forEach(e => {
            //数据校验，如果输入的是费用，则费率清空，否则费用清空

              if(e.rate == null || e.rate == ""){
                errorMsg =  "存在"+ this.feeTableHead +"未录入";
                commitFlag =  false;
              }

              if(Number(e.rate)<0 || Number(e.rate) > Number(this.maxRateValue) ){
                //errorMsg =  "费率值必须在0-"+this.maxRateValue+"之间";
                errorMsg =  "费率值必须在0-999之间";
                commitFlag =  false;
              }

              if(Number(e.minFee) < 0 || Number(e.minFee) > Number(e.maxFee) ) {
                errorMsg = "最低费用必须,大于0 小于最高费用"
                commitFlag = false;
              }

              if(Number(e.maxFee) < 0 ) {
                errorMsg = "最高费用必须大于0 "
                commitFlag = false;
              }

            if(e.minFee == null || e.minFee == ''){
              e.minFee = 0.00
            }
            if(e.maxFee == null || e.maxFee == ''){
              e.maxFee = this.maxMoneyValue;
            }
          })
        }else{
          //errorMsg = "费率设置有误";
          //commitFlag =  false;
        }
        if(commitFlag){
          // this.ProdFeeDeal2.t8PrjFeeLists = JSON.stringify(this.tailingCommisionList);
          this.ProdFeeDeal2.t8PrjFeeLists = this.tailingCommisionList;
          value.t8PrjFeeLists = this.ProdFeeDeal2.t8PrjFeeLists;
          value.dataStatus = this.ProdFeeDeal2.dataStatus;
          value.feeRole = this.ProdFeeDeal2.feeRole;
          this.saveProdDealFeeAdd(value);
        }else{
          Tools.alert( errorMsg, "danger");
          return false;
        }
      },



      saveInfo(){
        this.httpUtil.comnUpdate({
          action: 'T8ProdSubsidiary.addT8ProdSubsidiary',
          params: this.ProdFeeDeal2.T8ProdInfo.prodCode,
          successAlert: true
        }).then(data => {//如果存在验证后面的信息,不存在则为新增
          this.saveLoading = false;

          document.getElementById(menuItem.id).scrollIntoView({
            block:'start',
            inline:'nearest',
            behavior:'smooth'
          })
        })},
      change(value) {
        console.info(value);
      },
      changeConstantType: function(value) {
        //选择固定费用/费率时，分段设置变为空
        this.switchSegmentValue = !value
        this.switchTimeValue = !value
        this.moneyList = []
        this.timeList = []
        this.ProdFeeDeal2.constantRate = null
        this.ProdFeeDeal2.rateType = this.ProdFeeDeal2.rateType == '0' ? '1' : '0'
        this.tailingCommisionList = []

        //当金额或时间分段不可用的时候，表示该费用类型下不能选这种方式，应该还原成不可编辑
        if(this.moneySwitchDisabled){
          this.switchSegmentValue = false;
        }
        if(this.timeSwitchDisabled){
          this.switchTimeValue = false;
        }
      },
      changeSegmentType: function(value) {
        //金额分段
        /*if(this.constantRatesSitch && value){
          this.constantRatesSitch = false;
        }else if(this.switchTimeValue==false && value==false){
          //分段都为空，则固定费率不能为空
          this.constantRatesSitch = true;
        }*/

        this.moneyList = []
        //this.ProdFeeDeal2.constantRate = null
        this.ProdFeeDeal2.rateType = this.ProdFeeDeal2.rateType == '0' ? '1' : '0'
          if(this.switchSegmentValue==true){
            this.timeSwitchDisabled=true;
            this.moneySwitchDisabled = false;
          }else{
            this.timeSwitchDisabled=false;
            this.moneySwitchDisabled = false;
          }

        //重新生成列表
        this.buildTimeTable();
      },
      changeTimeType : function(value){
        //时间分段
        /*if(this.constantRatesSitch && value){
          this.constantRatesSitch = false;
        }else if(this.switchSegmentValue==false && value==false){
          //分段都为空，则固定费率不能为空
          this.constantRatesSitch = true;
        }*/

        this.timeList = []
        //this.ProdFeeDeal2.constantRate = null
        this.ProdFeeDeal2.rateType = this.ProdFeeDeal2.rateType == '0' ? '1' : '0'
        if(this.switchTimeValue==true){
          this.timeSwitchDisabled=false;
          this.moneySwitchDisabled = true;
        }else{
          this.timeSwitchDisabled=false;
          this.moneySwitchDisabled = false;
        }
        //重新生成列表
        this.buildMoneyTable();
      },
      deleteMoney : function(text, index){
        this.nowMoney = this.moneyList[this.moneyList.length-1];
        //是否已经删完
        if(this.moneyList.length == 0){
          Tools.alert( "已经没有数据", "danger");
          //没有数据，要按时间维度重新刷新列表
          this.buildTimeTable();
          return false;
        }
        //inde-下标，从0开始
        if(index != this.moneyList.length){
          Tools.alert( "请顺序删除", "danger");
          //在指定位置添加元素,第一个参数指定位置,第二个参数指定要删除的元素,如果为0,则追加
          this.moneyList.splice(index, 0, text);
          return false;
        }
        this.buildMoneyTable();
      },
      deleteTime : function(text, index){
        //是否已经删完
        if(this.timeList.length == 0){
          Tools.alert( "已经没有数据", "danger");
          //没有数据，要按金额维度重新刷新列表
          this.buildMoneyTable();
          return false;
        }
        //inde-下标，从0开始
        if(index != this.timeList.length){
          Tools.alert( "请顺序删除", "danger");
          //在指定位置添加元素,第一个参数指定位置,第二个参数指定要删除的元素,如果为0,则追加
          this.timeList.splice(index, 0, text);
          return false;
        }
        this.buildTimeTable();
      },
      buildMoneyTable : function(){
        this.tailingCommisionList = [];
        //先按金额组合，再按持有时间
        this.tailingCommisionMoneyList = [];

        for(var i = 0 ;i < this.moneyList.length ; i++){

          //1-金额，2-时间
          this.tailingCommisionMoneyList.push(
            {
              //最小金额
              dimension1Min: i == 0 ? 0 : this.moneyList[i-1],
              //最大金额
              dimension1Max: this.moneyList[i],
              //最小天数
              dimension2Min: null,
              //最大天数
              dimension2Max: null,
              moneyDesc: (i == 0 ? 0 : this.moneyList[i-1] )+ ' <= 金额 < ' + this.moneyList[i],
              timeDesc: null,
              rate: null,
              constantFee:null,
              minFee: null,
              maxFee: null,
              dataStatus: 'E',
              moneyRowspan: 1,
              showMoneyTd: true,
              showTimeTd: false
            }
          )
        }
        var lastMoney = {
          dimension1Min: this.moneyList[this.moneyList.length-1],
          dimension1Max: '-1',
          dimension2Min: null,
          dimension2Max: null,
          moneyDesc: this.moneyList[this.moneyList.length-1] + ' <= 金额 < ∞',
          timeDesc: null,
          rate: null,
          constantFee:null,
          minFee: null,
          maxFee: null,
          dataStatus: 'E',
          moneyRowspan: 1,
          showMoneyTd: true,
          showTimeTd: false
        };
        this.tailingCommisionMoneyList.push(lastMoney);
        if(this.timeList == null || this.timeList.length == 0){
          //为空只有一个维度
          this.tailingCommisionList = this.tailingCommisionMoneyList;
        }else{
          //不为空，两个维度
          for(let i = 0 ;i < this.tailingCommisionMoneyList.length ; i++  ){
            for(var j = 0 ;j <= this.timeList.length ; j++ ){
              let temp = Object.assign({}, this.tailingCommisionMoneyList[i]);
              temp.showTimeTd = true;
              temp.showMoneyTd = j==0 ? true : false;
              temp.moneyRowspan = j==0 ? this.timeList.length+1 : 1;
              temp.timeDesc = (j == 0 ? 0 : this.timeList[j-1] )+ ' <= 天数 < ' + (j== this.timeList.length ? ' ∞ ' : this.timeList[j] );
              temp.dimension2Min = j == 0 ? 0 : this.timeList[j-1];
              temp.dimension2Max = j== this.timeList.length ? '-1' : this.timeList[j] ;
              this.tailingCommisionList.push(temp);
            }
          }
        }

      },
      buildTimeTable : function(){
        this.tailingCommisionList = [];
        //先按金额组合，再按持有时间
        this.tailingCommisionTimeList = [];
        for(var i = 0 ;i < this.timeList.length ; i++){

          //1-金额，2-时间
          this.tailingCommisionTimeList.push(
            {
              dimension1Min: null,
              dimension1Max: null,
              dimension2Min: i == 0 ? 0 : this.timeList[i-1],
              dimension2Max: this.timeList[i],
              moneyDesc: null,
              timeDesc: (i == 0 ? 0 : this.timeList[i-1] )+ ' <= 天数 < ' + this.timeList[i],
              rate: null,
              constantFee:null,
              minFee: null,
              maxFee: null,
              dataStatus: 'E',
              moneyRowspan: 1,
              showMoneyTd: false,
              showTimeTd: true
            }
          )
        }
        var lastTime = {
          dimension1Min: null,
          dimension1Max: null,
          dimension2Min: this.timeList[this.timeList.length-1],
          dimension2Max: '-1',
          moneyDesc: null,
          timeDesc: this.timeList[this.timeList.length-1] + ' <= 天数 < ∞',
          rate: null,
          constantFee:null,
          minFee: null,
          maxFee: null,
          dataStatus: 'E',
          moneyRowspan: 1,
          showMoneyTd: false,
          showTimeTd: true
        };
        this.tailingCommisionTimeList.push(lastTime);
        if(this.moneyList == null || this.moneyList.length == 0){
          //为空只有一个维度
          this.tailingCommisionList = this.tailingCommisionTimeList;
        }else{
          //不为空，两个维度
          for(let i = 0 ;i <= this.moneyList.length ; i++ ){
            for(let j = 0 ;j < this.tailingCommisionTimeList.length ; j++  ){
              let temp = Object.assign({}, this.tailingCommisionTimeList[j]);
              temp.showMoneyTd = j!=0 ? false : true;
              temp.moneyRowspan = j==0 ? this.tailingCommisionTimeList.length : 1;
              temp.moneyDesc = (i == 0 ? 0 : this.moneyList[i-1] )+ ' <= 金额 < ' + (this.moneyList.length == i ? ' ∞ ' : this.moneyList[i]);
              temp.dimension1Min = i == 0 ? 0 : this.moneyList[i-1];
              temp.dimension1Max = i == this.moneyList.length ? '-1' : this.moneyList[i];
              this.tailingCommisionList.push(temp);
            }
          }
        }
      },
      insertMoney : function(value){
        this.nowMoney = this.moneyList[this.moneyList.length-1];
        //插入后，已经插入了
        if(value.toString().length>17){
          this.moneyList.pop();
          Tools.alert( "输入数字长度不能大于16字符!", "danger");
          return false;
        }else{
          if(value.indexOf(".")!=-1){
            let arr = value.toString().split(".");
            if(arr[1].length>2){
              this.moneyList.pop();
              Tools.alert( "小数点后长度不能大于2字符!", "danger");
              return false;
            }
          }

        }
        if(value <= 0 || parseInt(this.moneyList[this.moneyList.length-2]) > parseInt(value)){
          this.moneyList.pop();
          Tools.alert( "金额不能小于等于0，不能小于上次输入金额", "danger");
          return false;
        }

        this.buildMoneyTable();
        return value;
      },
      insertTime : function(value){
        //插入后，已经插入了
        if((value.indexOf(".")!=-1)||value.indexOf("-")!=-1){
          this.moneyList.pop();
          Tools.alert( "请输入整数", "danger");
          return false;
        }
        if(value.toString().length>16){
          this.moneyList.pop();
          Tools.alert( "输入数字长度不能大于16字符!", "danger");
          return false;
        }
        if(value <= 0 || parseInt(this.timeList[this.timeList.length-2]) > parseInt(value)){
          this.timeList.pop();
          Tools.alert( "天数不能小于等于0，不能小于上次输入天数", "danger");
          return false;
        }
        this.buildTimeTable();
        return value;
      },
      handleBlur : function(){
        if(this.ProdFeeDeal2.constantRate !=null && this.ProdFeeDeal2.constantRate!=""){
          if(Number(this.ProdFeeDeal2.constantRate)>this.maxRateValue && (this.ProdFeeDeal2.chargeType=="1")){
            //固定费率限制小于100
            this.ProdFeeDeal2.constantRate = this.maxRateValue;
          }else if(Number(this.ProdFeeDeal2.constantRate)>this.maxMoneyValue && this.ProdFeeDeal2.chargeType=="0"){
            //固定费用限制小于 99999999999999.99
            this.ProdFeeDeal2.constantRate = this.maxMoneyValue;
          }
        }
      },
      addHandler(){
        if(this.ProdFeeDeal.prodCode == '' || this.ProdFeeDeal.prodCode == null){
          Tools.alert("正在获取产品信息，稍后重试 !","danger")
          return false;
        }
        this.ProdFeeDeal2={};
        this.ProdFeeDeal2.chargeType=1;
        this.timeList=[];
        this.moneyList=[];
        this.tailingCommisionList=[]
        this.ProdFeeDeal2.t8ProdInfoId = this.ProdFeeDeal.t8ProdInfoId;
        this.ProdFeeDeal2.prodCode = this.ProdFeeDeal.prodCode;
        this.switchSegmentValue = false,
        this.switchTimeValue = false,
        this.moneySwitchDisabled=false,    //金额分段切换按钮是否可用
        this.timeSwitchDisabled=false,     //时间分段切换按钮是否可用
        this.$refs.addT8FeeDealPopup.popup();
      },
      selectRow(row, column, event) {
        this.selectRowData = assign({}, row)
        this.ProdFeeDeal2 = assign({}, row)
      },
      editHandler(params){
        let numId = params.numId;
        this.ProdFeeDeal2 = this.dataParams[numId];
        if(this.ProdFeeDeal2.t8PrjFeeLists==null){
          Tools.alert("未获取到信息，稍后重试 !","danger");
          return  false;
        }
        return params;
      },

      deleteProdFee(val){
        this.$delete(this.dataParams,val.numId);
        this.uptDataParams(this.dataParams);
      },

      uptDataParams(dataParams){
        for(let i = 0;i < dataParams.length; i++){
          this.$set(dataParams[i],'numId',i)
        }
        this.dataParams = dataParams;
        //先加载页面，再查询子模块
        this.$set(this.$refs.t8FeeDealGrid,'list',this.dataParams);

      },
      getRowData(data){
        data.oldData = {};
        data.t8ProdInfoId = this.ProdFeeDeal.t8ProdInfoId;
        data.prodCode = this.ProdFeeDeal.prodCode;
        this.$delete(this.dataParams,data.numId);
        this.saveProdDealFeeAdd(data);
        this.$refs.editPopup.close();

      },
      saveProdDealFeeAdd(data){
        this.dataParams.push(data);
        for(let i = 0;i < this.dataParams.length; i++){
          this.$set(this.dataParams[i],'numId',i)
        }
        this.$set(this.$refs.t8FeeDealGrid,'list',this.dataParams);
        this.$refs.addT8FeeDealPopup.close();
      }

    },
    watch:{

      'ProdFeeDeal.dataParams' : function (value) {
        this.uptDataParams(value);
      },

      'ProdFeeDeal2.feeType'(value){
        //根据不同费用类型判断对应的切换按钮是否可用，在这里做监听是因为平台的data-on-change事件点击X时不会触发
        if(value == '2' || value == '5' || value == '6' ){
          this.moneySwitchDisabled = true;
          this.switchSegmentValue = false;
          this.changeSegmentType(this.switchSegmentValue);
        }else{
          this.moneySwitchDisabled = false;
        }

        if(value == '0' || value == '1' || value == '4' ){
          this.timeSwitchDisabled = true;
          this.switchTimeValue = false;
          this.changeTimeType(this.switchTimeValue);
        }else{
          this.timeSwitchDisabled = false;
        }
      },

    },




    created() {
      this.ProdFeeDeal2.chargeType="1";
    }




  };
</script>
<style lang="scss" scoped>
::v-deep .moneyTextClass .el-input__inner{
  border: 0px white;
  color: white !important;
}
::v-deep .el-table .cell {
  padding-right:0
}

::v-deep #moneyText {
  border-color:white !important;
  color:white !important;
}

.add-btn-div {
  position: relative;
  z-index: 1;
}
.add-btn{
  background-color: #4caf50;
  border-radius: 20px;
  box-shadow: 0 4px 5px 0 rgba(76,175,80,0.14), 0 1px 10px 0 rgba(76,175,80,0.12), 0 2px 4px -1px rgba(76,175,80,0.2);
  width: 20px;
  height: 20px;
  line-height: 20.5px;
  font-size: 23px;
  font-weight: 400;
  cursor: pointer;
  color: #FFF;
  text-align: center;
}
</style>
