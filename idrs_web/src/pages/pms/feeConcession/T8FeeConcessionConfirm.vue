  <template>
  <div>
    <div>
      <k-form-search-customize data-target="t8FeeConcessionDetailsGrid" v-model="queryParam">

        <k-form-item label="产品代码">
          <k-field-select v-model="prodSearchParam.prodCode" data-action="T8Dict.findTaProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode"/>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="prodSearchParam.prodName" data-validate-type="text"/>
        </k-form-item>
        <k-form-item label="成立日">
          <k-field-date v-model="prodSearchParam.startEstablishDate" data-type="daterange" :data-max-value="prodSearchParam.endEstablishDate===''?'29991230':prodSearchParam.endEstablishDate"/>
        </k-form-item>
        <k-form-item label="优惠日期">
          <k-field-date v-model="prodSearchParam.concessionStartDate" data-type="date"/>
        </k-form-item>

        <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" data-target="addPopup" :data-handler="initFormData"
               v-if="global.isShowAuthorityButton('T8FeeConcessionConfirm.addT8FeeConcessionConfirm')" data-descript="设置费用优惠">
          <md-icon md-src="/static/svg/add.svg" />新增
        </k-btn>
      </k-form-search-customize>

      <k-grid ref="t8FeeConcessionDetailsGrid" :data-autoload="false" data-action="T8FeeConcession.find">
        <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode"/>
        <k-grid-column data-align="center" data-header="产品名称" data-name="prodName"/>
        <k-grid-column data-align="center" data-header="成立/开放日" data-name="prodDate"/>
        <k-grid-column data-align="center" data-header="费用类型" data-name="feeType" data-dict="fee_type"/>
        <k-grid-column data-align="center" data-header="是否有优惠" data-name="isFeeConcession" data-dict="1yes0no"/>
        <k-grid-column data-align="center" data-header="费用优惠到%" data-type="number" data-digits="2"
                       data-name="feeConcessionRate"/>
        <k-grid-column data-align="center" data-header="优惠生效日期" data-type="date" data-name="concessionStartDate"/>
        <k-grid-column data-align="center" data-header="优惠失效日期" data-type="date" data-name="concessionEndDate"/>
        <k-grid-column data-align="center" data-header="确认状态" data-dict="confirm_status" data-name="confirmStatus"/>
        <k-grid-column data-align="center" data-header="决策类型" data-name="decisionType" data-dict="decision_type"/>
        <k-grid-column data-align="center" data-header="决策名称" data-name="meetingName"/>
        <template slot="operate" slot-scope="scope">
          <k-btn data-functype="POPUP" data-size="mini" data-target="editPopup"
                 class="md-info md-just-icon md-simple"
                 v-if="global.isShowAuthorityButton('T8FeeConcessionConfirm.adjustT8FeeConcession')"
                 data-descript="确认费用优惠" :data-handler="editHandler" v-show="showUpdate"
                 :data-disabled="scope.row.row.confirmStatus==2">
            <md-icon>check</md-icon>
          </k-btn>
        </template>
      </k-grid>



      <k-popup ref="addPopup" data-title="新增费用优惠" :data-dialog-drag="true">
        <k-form ref="addForm" :data-col="2">
          <k-form-item label="产品代码">
            <k-field-select v-model="formData.prodCode" data-action="T8Dict.findTaProdInfos"
                            data-display-field="prodCode,prodName" data-value-field="prodCode"
                            @data-on-change="getProdNameByCode" :data-allowblank="false"/>
          </k-form-item>
          <k-form-item label="产品名称">
            <k-field-text v-model="formData.prodName" :data-disabled="true"
                          :dataAllowblank="false"/>
          </k-form-item>
          <k-form-item label="成立/开放日">
            <k-field-select
              v-model="formData.prodDate"
              :data-data="prodDateList"
              :data-allowblank="false"
              data-display-field="changeDate"
              data-value-field="changeDate"
              @data-on-change="changeProdDate"
            />
          </k-form-item>
          <hr align=center width="500" color=#987cb9 SIZE=3/>
          <k-form ref="feeForm" v-for="(item,index) in feeData" :key="index"
                  :data-col="2">
            <k-field-text v-model="item.id" v-show="false"/>
            <k-field-text v-model="item.confirmStatus" v-show="false"/>
            <k-form-item label="费用类型">
              <k-field-select v-model="item.feeType" data-dict="fee_type"
                              :data-disabled="true"
                              :dataAllowblank="false"/>
            </k-form-item>

            <k-form-item label="费率">
              <k-field-text v-model="item.rate" data-type="number" data-digits="2" :data-disabled="true"
                            :dataAllowblank="false"/>
            </k-form-item>
            <k-form-item label="付费规则">
              <k-field-select v-model="item.paymentRules" data-dict="t8_payment_rules" :data-disabled="true"
                              :dataAllowblank="true"/>
            </k-form-item>
            <k-form-item label="计提基数">
              <k-field-select v-model="item.chargingIndex" data-dict="t8_charging_index_deal" :data-disabled="true"
                              :dataAllowblank="true"/>
            </k-form-item>
            <k-form-item label="顺延规则">
              <k-field-select v-model="item.postponeRule" data-dict="t8_postpone_rule" :data-disabled="true"
                              :dataAllowblank="true"/>
            </k-form-item>
            <!-- <k-form-item label="费用说明">
              <k-field-text v-model="item.feeDesc" :data-disabled="true"
                            :dataAllowblank="true"/>
            </k-form-item> -->
            <k-form-item label="是否优惠">
              <k-field-radio :data-data="options"
                             @data-on-change="changedIsFeeConcession(item)" v-model="item.isFeeConcession"
                             :dataAllowblank="false"/>
            </k-form-item>
            <k-form-item label="费率优惠到%">
              <k-field-text v-model="item.feeConcessionRate" data-validate-type="number" data-digits="2"
                            :dataAllowblank="item.allowblank"  :data-disabled="item.disabled" data-min-value="0" data-max-value="99.99"/>
            </k-form-item>
            <k-form-item label="优惠生效日期">
              <k-field-date v-model="item.concessionStartDate"
                            data-type="date"
                            ref="startDate"
                            :dataAllowblank="item.allowblank"
                            :data-disabled="item.disabled"
                            :data-min-value="formData.prodDate==null || formData.prodDate==''?'19701220':formData.prodDate"
                            :data-max-value="item.concessionEndDate==null || item.concessionEndDate==''?'29991010':item.concessionEndDate"
                            @data-on-change="concessionStartDateChange"
              />
            </k-form-item>
            <k-form-item label="优惠失效日期">
              <k-field-date v-model="item.concessionEndDate"
                            data-type="date"
                            :dataAllowblank="item.allowblank"
                            :data-disabled="item.disabled"
                            :data-min-value="item.concessionStartDate=='' || item.concessionStartDate==null?formData.prodDate:item.concessionStartDate"

              />
            </k-form-item>

            <k-form-item label="决策类型">
              <k-field-select v-model="item.decisionType" data-dict="decision_type"
                              @data-on-change="changeDecisionType1(item.decisionType)" :data-allowblank="false"/>
            </k-form-item>
            <k-form-item label="决策名称">
              <k-field-select v-model="item.meetingId"
                              :data-data="meetingNameList"
                              data-display-field="meetingName"
                              data-value-field="id"
                              ref="meetings1" :data-allowblank="false"/>
            </k-form-item>

            <hr align=center width="500" color=#987cb9 SIZE=3/>
          </k-form>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT"
                   :data-handler="submitHandle"
                   data-from="addForm" :data-model="formData" :data-after-success="refresh"
                   data-target="t8FeeConcessionDetailsGrid" ref="addBtn">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
            </k-btn>
          </k-form-footer>
        </k-form>
      </k-popup>



      <k-popup ref="editPopup" data-title="确认费用优惠" :data-dialog-drag="true">
        <k-form ref="editForm" :data-col="2">
          <k-form-item label="成立/开放日">
            <k-field-select
              v-model="fee.prodDate"
              :data-params="{prodCode:fee.prodCode}"
              data-action="T8ProdWorkdays.findProdOpenDays"
              :data-allowblank="false"
              data-display-field="changeDate"
              data-value-field="changeDate"
              @data-on-change="changeConfirmProdDate"
            />
          </k-form-item>
          <k-form-item label="费用类型">
            <k-field-select v-model="fee.feeType" data-dict="fee_type"
                            :dataAllowblank="false" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="是否优惠">
            <k-field-radio :data-data="options"
                           @data-on-change="changedIsFeeConcession(fee)" v-model="fee.isFeeConcession"
                           :dataAllowblank="false"/>
          </k-form-item>
          <k-form-item label="费率优惠到%">
            <k-field-text v-model="fee.feeConcessionRate" data-validate-type="number" data-digits="2"
                          :dataAllowblank="fee.allowblank" :data-disabled="fee.disabled" data-min-value="(0"
                          data-max-value="99.99"/>
          </k-form-item>
          <k-form-item label="优惠生效日期">
            <k-field-date v-model="fee.concessionStartDate"
                          data-type="date"
                          ref="startDate"
                          :dataAllowblank="fee.allowblank"
                          :data-disabled="fee.disabled"
                          :data-min-value="selectedEstablishDate"
                          :data-max-value="fee.concessionEndDate===''?'20991230':fee.concessionEndDate"
            />
          </k-form-item>
          <k-form-item label="优惠失效日期">
            <k-field-date v-model="fee.concessionEndDate"
                          data-type="date"
                          :dataAllowblank="fee.allowblank"
                          :data-disabled="fee.disabled"
                          :data-min-value="fee.concessionStartDate===''?selectedEstablishDate:fee.concessionStartDate"/>
          </k-form-item>
          <k-form-item label="决策类型">
            <k-field-select v-model="fee.decisionType" data-dict="decision_type" :data-allowblank="false"
                            @data-on-change="changeDecisionType(fee.decisionType)"></k-field-select>
          </k-form-item>
          <k-form-item label="会议/审批单">
            <k-field-select data-action="QuotaMeeting.findMeetByTypeAndCode"
                            :data-params="{type:fee.decisionType,prodCode:fee.prodCode}"
                            v-model="fee.meetingId"
                            :data-allowblank="false"
                            data-display-field="meetingName"
                            data-value-field="id"
                            ref="meetings"></k-field-select>
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT"
                   data-action="T8FeeConcessionConfirm.adjustT8FeeConcession"
                   data-from="editForm" :data-model="fee" :data-after-success="refresh"
                   data-target="t8FeeConcessionDetailsGrid">
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
  name: "",
  data() {
    return {
      meetingNameList:{},
      feeData: [{}],
      prodDateList: {},
      prodSearchParam: {
        prodCode: '',
        startEstablishDate:'',
        endEstablishDate:''
      },
      formData: {
        prodCode: '',
        prodName: '',
        feeJson:'',
        decisionType:'',
        meetingId:'',
      },
      options:[
        {
          value: '1',
          label: '是',
        }, {
          value: '0',
          label: '否'
        }
      ],
      prodCode: '',
      minDate: '19701001',//最小日期
      selectedEstablishDate: '',
      showUpdate: true,
      fee: {},
    }
  },

  created() {
    this.global.getProdUser('');
    this.$nextTick(()=>{
      let prodCode = this.$route.query.prod_code;
      //.log(prodCode)
      if(prodCode !=''&&prodCode!=undefined){
        this.$refs.t8FeeConcessionDetailsGrid.load({prodCode:prodCode});
      }
    });
  },
  watch:{
    'prodSearchParam.startEstablishDate'(value){
      if(value==''){
        this.minDate ='19701001';
      }else{
        this.minDate = value;
      }
    },
    'formData.decisionType'(value){
      //this.$refs.meetings.load({type: value, prodCode: this.priceData.prodCode});
      this.$set(this.formData,'meetingId','');
    },
  },
  methods: {
    submitHandle(value) {
      //console.log(value)
      let result = true;
      let feeForm = this.$refs.feeForm;
      if (feeForm && feeForm.length > 0) {
        for (let i = 0; i < feeForm.length; i++) {
          result = feeForm[i].validate();
          if (result === false){
            break;
          }
        }
      }
      if(this.$refs.addForm.validate() ==true  & result == true){
        //走审批反显数据
        //console.log(this.feeData)
        this.$set(value,'prodStatus',this.prodStatus);
        if (this.feeData && this.feeData.length > 0) {
          for (let i = 0; i < this.feeData.length; i++) {
            //this.feeData[i].prodDate=this.prodDate;
            this.$set(this.feeData[i], 'prodDate', this.formData.prodDate);
          }
          value.feeJson = JSON.stringify(this.feeData);
        }

        this.httpUtil.comnUpdate({
          action: "T8FeeConcession.addT8FeeConcession",
          params: value,
        }).then(data => {
          this.priceData = {};
          this.$refs.addPopup.close();
          this.$refs.t8FeeConcessionDetailsGrid.load();
        });
      } else {
        return false;
      }
    },
    concessionStartDateChange(){
      for (let i = 0; i < this.feeData.length; i++) {
        if(this.formData.prodDate != ''&& this.formData.prodDate != null &&
          this.feeData[i].concessionStartDate != null && this.feeData[i].concessionStartDate != '' &&
          this.feeData[i].concessionStartDate<this.formData.prodDate){

          this.feeData[i].concessionStartDate = '';
          Tools.alert("优惠生效日期必须大于成立日！");
          return false;
        }
        if(this.feeData[i].concessionEndDate != ''&& this.feeData[i].concessionEndDate != null && this.feeData[i].concessionStartDate>this.feeData[i].concessionEndDate){
          this.feeData[i].concessionStartDate = '';
          Tools.alert("优惠生效日期必须小于失效日期！");
          return false;
        }
      }

    },
    //新增 产品成立/开放日发生变化  查询出对应的开放日的开始日期与结束日期 并赋值给生效日期与失效日期
    changeProdDate() {
      for (let i = 0; i < this.feeData.length; i++) {
        this.$set(this.feeData[i], 'concessionStartDate', this.formData.prodDate);
      }
      this.httpUtil.comnQuery({
        action: 'T8ProdWorkdays.findStartAndEndDate',
        params: {
          prodCode: this.formData.prodCode,
          changeDate: this.formData.prodDate
        },
      }).then(data => {
        if (data.rows.length > 0) {
          for (let i = 0; i < this.feeData.length; i++) {
            this.$set(this.feeData[i], 'concessionEndDate', data.rows[0].changeDate);
          }
        } else {
          for (let i = 0; i < this.feeData.length; i++) {
            this.$set(this.feeData[i], 'concessionEndDate', '');
          }
        }
      });
    },
    getProdNameByCode(){
      //用this.prodCode接收prodCode，然后重置addForm，再赋值
      this.prodCode = this.formData.prodCode;
      this.$refs.addForm.reset();
      this.formData.prodCode = this.prodCode;
      this.feeData=[{}];
      //判断产品代码为空，则全置空
      if(this.formData.prodCode == null || this.formData.prodCode == ''){
        this.$refs.addForm.reset();
      } else {
        //判断失效日期是否能为空
        this.httpUtil.comnQuery({
          action: 'T8FeeConcession.findProdFeeList',
          params: {
            prodCode : this.formData.prodCode,
          }
        }).then(data => {
          if (data.rows.length > 0) {
            this.feeData = data.rows;
            for (let feeDatum of this.feeData) {
              feeDatum.allowblank = false;
              feeDatum.disabled = false;
            }
          }
        });

        //通过产品代码查询产品及其信息
        this.httpUtil.comnQuery({
          action: "T8ProdInfo.getProdNameByProdCode",
          params: {prodCode: this.formData.prodCode}
        }).then(data => {
          if(data.rows.length>0){
            this.$set(this.formData,"prodName",data.rows[0].prodName);
          }
        }).catch({})

        //通过产品代码与类型查询产品所有开放日期信息
        this.httpUtil.comnQuery({
          action: "T8ProdWorkdays.findProdOpenDays",
          params: {prodCode: this.formData.prodCode}
        }).then(data => {
          this.prodDateList = data.rows;
        }).catch({})
      }
    },
    initFormData(){
      this.formData = {};
      this.feeData = [{}];
      // this.T8ProdPerformance = {};
    },
    //确认 产品成立/开放日发生变化  查询出对应的开放日的开始日期与结束日期 并赋值给生效日期与失效日期
    changeConfirmProdDate() {
      this.$set(this.fee, 'concessionStartDate', this.fee.prodDate);
      this.httpUtil.comnQuery({
        action: 'T8ProdWorkdays.findStartAndEndDate',
        params: {
          prodCode: this.fee.prodCode,
          changeDate: this.fee.prodDate
        },
      }).then(data => {
        if (data.rows.length > 0) {
          this.$set(this.fee, 'concessionEndDate', data.rows[0].changeDate);
        } else {
          this.$set(this.fee, 'concessionEndDate', '');
        }
      });
    },
    changeDecisionType1(value) {
      this.httpUtil.comnQuery({
        action: 'QuotaMeeting.findMeetByTypeAndCode',
        params: {
          prodCode: this.formData.prodCode,
          type: value
        },
      }).then(data => {
         this.meetingNameList = data.rows;
         //console.log(this.meetingNameList)
      });
    },
    //决策类型改变事件
    changeDecisionType(value) {
      if(value ==null || value == ''){
        this.$set(this.fee,'meetingId','');
      }
      this.$refs.meetings.load({type: this.fee.decisionType, prodCode: this.fee.prodCode});
    },
    //一级查询选中事件
    selectRow(row, column, event) {
      const _this = this;
      _this.selectRowData = assign({}, row);
      this.prodCode = _this.selectRowData.prodCode;
      this.$refs.t8FeeConcessionDetailsGrid.load({prodCode: this.prodCode});
      this.selectedEstablishDate = _this.selectRowData.establishDate;
    },
    //点击确认按钮事件
    editHandler(value) {
      this.fee = value;
      if (this.fee.isFeeConcession == 1) {//优惠
        this.fee.allowblank = false;
        this.fee.disabled = false;
      } else {//优惠
        this.fee.allowblank = true;
        this.fee.disabled = true;
      }
    },
    changedIsFeeConcession(item) {
      this.$set(item, "feeConcessionRate", '');
      this.$set(item, "concessionStartDate", '');
      this.$set(item, "concessionEndDate", '');
      if (item.isFeeConcession == '0') {
        item.allowblank = false;
        item.disabled = false;
      } else {
        item.allowblank = true;
        item.disabled =true;
      }
    },
    //操作成功后刷新二级查询
    refresh(){
      this.$refs.t8FeeConcessionDetailsGrid.load({prodCode:this.prodCode});
    }
  },
  computed: {
    queryParam() {
      return {
        'prodName': this.prodSearchParam.prodName,//产品名称
        'prodCode': this.prodSearchParam.prodCode,//产品代码
        'startEstablishDateMin': this.prodSearchParam.startEstablishDate ? this.prodSearchParam.startEstablishDate[0]:null,//成立起始日最小值
        'startEstablishDateMax': this.prodSearchParam.startEstablishDate ? this.prodSearchParam.startEstablishDate[1]:null,//成立起始日最大值
        'concessionStartDate': this.prodSearchParam.concessionStartDate //优惠日期
      }
    }
  }
}
</script>

<style scoped>

</style>
