<template>
  <div>
    <div class="query-div" >
      <k-form ref="T82001Form" :data-col="3" :data-model="ProdFee">
        <k-form-item data-input-width="150px"  v-show="false">
          <k-field-text v-model="ProdFee.prodCode"  data-placeholder="产品代码"/>
        </k-form-item>
        <k-form-item data-input-width="150px"  v-show="false">
          <k-field-text v-model="ProdFee.t8ProdInfoId"  data-placeholder="产品id"/>
        </k-form-item>

      </k-form>
    </div>
    <div style="min-height:225px;">
      <div class="add-btn-div">
        <div class="add-btn"  @click="addHandler">+</div>
      </div>
        <k-grid ref="t8FeeProdGrid" @data-row-select="selectRow">
          <k-grid-column data-header="行数" data-name="numId" :data-hidden="true"></k-grid-column>
          <k-grid-column data-header="产品代码" data-name="prodCode" :data-hidden="true"></k-grid-column>
          <k-grid-column data-header="费用类型" data-name="feeType" data-dict="fee_type"></k-grid-column>
          <k-grid-column data-header="境内托管行费率%" data-name="domesticRate"></k-grid-column>
          <k-grid-column data-header="费率%" data-name="rate"></k-grid-column>
<!--          <k-grid-column data-header="首次计提日期" data-name="firstProvisionDate"></k-grid-column>-->
          <k-grid-column data-header="付费规则" data-name="paymentRules" data-dict="t8_payment_rules"></k-grid-column>
<!--          <k-grid-column data-header="计提结束日期" data-name="provisionEndDate"></k-grid-column>-->
          <k-grid-column data-header="计费基数" data-name="chargingIndex" data-dict="t8_charging_index"></k-grid-column>
          <k-grid-column data-header="顺延规则" data-name="postponeRule" data-dict="t8_postpone_rule"></k-grid-column>
          <k-grid-column data-header="创建日期" data-name="crtDate" :data-hidden="true"></k-grid-column>
          <k-grid-column data-header="创建人" data-name="crtUser" :data-hidden="true"></k-grid-column>
          <template slot="operate" slot-scope="scope">
            <k-btn class="md-info md-just-icon md-simple" data-descript="修改产品费用" data-functype="POPUP" data-size="mini"
                   data-target="editT8FeeProdPopup" >
              <md-icon>edit</md-icon>
            </k-btn>
            <k-btn class="md-danger md-just-icon md-simple" data-functype="POPUP" :data-handler="deleteProdFee"
                     data-type="danger" data-target="t8FeeProdGrid" :data-confirm="true" data-descript="删除产品费用">
              <md-icon>close</md-icon>
            </k-btn>
          </template>
        </k-grid>
    </div>


    <k-popup ref="addT8FeeProdPopup" data-title="新增">
      <k-form ref="addT8FeeProdForm" :data-col="2" dataLabelWidth="150px" dataInputWidth="200px">
        <k-form-item label="产品代码" v-show="false">
          <k-field-text v-model="ProdFeeAdd.prodCode" />
        </k-form-item>
        <k-form-item label="产品主表id" v-show="false">
          <k-field-text v-model="ProdFeeAdd.t8ProdInfoId" />
        </k-form-item>
        <k-form-item label="费用类型" v-if="this.isShareSort==='0'">
          <k-field-select v-model="ProdFeeAdd.feeType" data-dict="fee_type" :data-allowblank="false"  @data-on-change="clearFrom"/>
        </k-form-item>
        <k-form-item label="费用类型" v-if="this.isShareSort==='1'">
          <k-field-select v-model="ProdFeeAdd.feeType" data-dict="fee_type_share" :data-allowblank="false"  @data-on-change="clearFrom"/>
        </k-form-item>
        <k-form-item label="境内托管行费率%"  v-if="ProdFeeAdd.feeType=='1'">
          <k-field-text v-model="ProdFeeAdd.domesticRate" :data-max-length="8" @data-on-change="changeRate"
                        data-min-value="0" data-integer-length="3" data-validate-type="money" data-max-value="100"
                        data-type="money" data-digits="4" :data-allowblank="ProdFeeAdd.feeType!='1'"/>
        </k-form-item>
        <k-form-item label="境外托管行费率%"  v-if="ProdFeeAdd.feeType=='1'">
          <k-field-text v-model="ProdFeeAdd.abroadRate" :data-max-length="8"
                        data-min-value="0"  data-integer-length="3" data-validate-type="money" data-max-value="100"
                        data-type="money" data-digits="4"/>
        </k-form-item>


        <k-form-item label="费率%" v-if="ProdFeeAdd.feeType!='1'">
          <k-field-text v-model="ProdFeeAdd.rate" :data-max-length="8" @data-on-change="changeRate"
                        data-min-value="0" data-integer-length="3" data-validate-type="money" data-max-value="100"
                        data-type="money" data-digits="4" :data-allowblank="ProdFeeAdd.feeType=='1'"/>
        </k-form-item>
        <k-form-item label="首次计提日期" v-if="false">
          <k-field-date v-model="ProdFeeAdd.firstProvisionDate"
                        :data-min-value="ProdFee.establishDate" :data-default-value="ProdFee.establishDate"
                        :data-max-value="ProdFee.provisionEndDate?ProdFee.provisionEndDate:ProdFee.endDate"
                        data-date-format="yyyy-MM-dd" :dataAllowblank="ProdFeeAdd.rate == 0||ProdFeeAdd.domesticRate==0"/>
        </k-form-item>
        <k-form-item label="付费规则">
          <k-field-select v-model="ProdFeeAdd.paymentRules" data-dict="t8_payment_rules"
                          :data-allowblank="allowFeeInfoBlank"/>
        </k-form-item>
        <k-form-item label="计提结束日期" v-if="false">
          <k-field-date v-model="ProdFeeAdd.provisionEndDate" :data-default-value="ProdFee.endDate"
                        :data-min-value="ProdFeeAdd.firstProvisionDate?ProdFeeAdd.firstProvisionDate:ProdFee.establishDate"
                        :data-max-value="ProdFee.endDate"
                        data-date-format="yyyy-MM-dd" :dataAllowblank="ProdFeeAdd.rate == 0||ProdFeeAdd.domesticRate==0"/>
        </k-form-item>
        <k-form-item label="计费基数">
          <k-field-select v-model="ProdFeeAdd.chargingIndex" data-dict="t8_charging_index"
                          :data-allowblank="allowFeeInfoBlank"/>
        </k-form-item>
        <k-form-item label="顺延规则">
          <k-field-select v-model="ProdFeeAdd.postponeRule" data-dict="t8_postpone_rule" :data-allowblank="true"/>
        </k-form-item>
        <!-- <k-form-item label="费用说明" :data-col="2">
          <k-field-text v-model="ProdFeeAdd.feeDesc" :data-allowblank="ProdFeeAdd.rate == 0||ProdFeeAdd.domesticRate==0"  :data-max-length="2000" inputType="textarea" :rows="1"/>
        </k-form-item> -->

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-from="addT8FeeProdForm" :data-handler="saveProdFeeAdd"
                 :data-model="ProdFeeAdd" data-target="t8FeeProdGrid" >
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

	<!--    修改产品费用实体类弹出框   -->
	<k-popup ref="editT8FeeProdPopup" data-title="修改">
	  <k-form ref="editT8FeeProdForm" :data-col="2" dataLabelWidth="150px" dataInputWidth="200px">
      <k-form-item label="下标"  v-show="false">
        <k-field-text v-model="ProdFeeUpt.numId" />
      </k-form-item>
      <k-form-item label="产品代码"  v-show="false">
        <k-field-text v-model="ProdFeeUpt.prodCode" />
      </k-form-item>
      <k-form-item label="产品主表id" v-show="false">
        <k-field-text v-model="ProdFeeUpt.t8ProdInfoId" />
      </k-form-item>
      <k-form-item label="费用类型" v-if="this.isShareSort==='0'">
        <k-field-select v-model="ProdFeeUpt.feeType" data-dict="fee_type" :data-allowblank="false"  @data-on-change="clearFrom"/>
      </k-form-item>
      <k-form-item label="费用类型" v-if="this.isShareSort==='1'">
        <k-field-select v-model="ProdFeeUpt.feeType" data-dict="fee_type_share" :data-allowblank="false"  @data-on-change="clearFrom"/>
      </k-form-item>
      <k-form-item label="境内托管行费率%"  v-if="ProdFeeUpt.feeType=='1'">
        <k-field-text v-model="ProdFeeUpt.domesticRate" :data-max-length="8"
                      data-min-value="0" data-integer-length="3" data-validate-type="money" data-max-value="100"
                      data-type="money" data-digits="4" :data-allowblank="ProdFeeUpt.feeType!='1'"
                      @data-on-change="changeEditRate"/>
      </k-form-item>
      <k-form-item label="境外托管行费率%"  v-if="ProdFeeUpt.feeType=='1'">
        <k-field-text v-model="ProdFeeUpt.abroadRate" :data-max-length="8"
                      data-min-value="0"  data-integer-length="3" data-validate-type="money" data-max-value="100"
                      data-type="money" data-digits="4"/>
      </k-form-item>


      <k-form-item label="费率%" v-if="ProdFeeUpt.feeType!='1'">
        <k-field-text v-model="ProdFeeUpt.rate" :data-max-length="8" @data-on-change="changeEditRate"
                      data-min-value="0" data-integer-length="3" data-validate-type="money" data-max-value="100"
                      data-type="money" data-digits="4" :data-allowblank="ProdFeeUpt.feeType=='1'"/>
      </k-form-item>

      <k-form-item label="首次计提日期" v-if="false">
        <k-field-date v-model="ProdFeeUpt.firstProvisionDate" :data-min-value="ProdFee.establishDate"
                      :data-max-value="ProdFeeUpt.provisionEndDate" data-type="date" :dataAllowblank="ProdFeeUpt.rate == 0||ProdFeeUpt.domesticRate==0" />
      </k-form-item>
      <k-form-item label="付费规则">
        <k-field-select v-model="ProdFeeUpt.paymentRules" data-dict="t8_payment_rules"
                        :data-allowblank="allowBlankEdit"/>
      </k-form-item>
      <k-form-item label="计提结束日期" v-if="false">
        <k-field-date v-model="ProdFeeUpt.provisionEndDate" :data-min-value="ProdFeeUpt.firstProvisionDate" :data-max-value="ProdFee.endDate"
                      data-type="date" :dataAllowblank="ProdFeeUpt.rate == 0||ProdFeeUpt.domesticRate==0"/>
      </k-form-item>
      <k-form-item label="计费基数">
        <k-field-select v-model="ProdFeeUpt.chargingIndex" data-dict="t8_charging_index"
                        :data-allowblank="allowBlankEdit"/>
      </k-form-item>
      <k-form-item label="顺延规则">
        <k-field-select v-model="ProdFeeUpt.postponeRule" data-dict="t8_postpone_rule" :data-allowblank="true"/>
      </k-form-item>
      <!-- <k-form-item label="费用说明" :data-col="2">
        <k-field-text v-model="ProdFeeUpt.feeDesc" :data-allowblank="ProdFeeUpt.rate == 0"  :data-max-length="2000" inputType="textarea" :rows="1"/>
      </k-form-item> -->
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT"  data-from="editT8FeeProdForm" :data-handler = "saveProdFeeUpt"
	        :data-model="ProdFeeUpt" data-target="t8FeeProdGrid">
	        <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
	      </k-btn>
	      <k-btn class="btn-custom-plain" data-functype="CLOSE">
	        <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	    </k-form-footer>
	  </k-form>
	</k-popup>

    <k-form  dataLabelWidth="170px" dataInputWidth="300px">
      <k-form-footer data-align="center"  v-show="menuName == 'M81007'">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdAllInfo.updateFeeProd" :data-handler = "saveProdFee"
               :data-model="dataParams" data-target=""  :data-after-success="passDataSuccess">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>保存
        </k-btn>
      </k-form-footer>
    </k-form>



  </div>
</template>

<script>
import Tools from "@/utils/tools";
import eventBus from "@/utils/eventBus";
  export default {
    computed: {},
    model: {
      prop: 'ProdFee',
      event: 'input'
    },
    props:{
      T8ProdCalendar:{},
      menuName:'',
      ProdFee: {},
      prodCode: {
        type: String,
        default: ''
      },
      t8ProdInfoId: {
        type: String,
        default: ''
      },
      prodIsShareSort: {
        type: Boolean,
        default: true
      },
    },
    data() {
      return {
        ProdFeeUpt: {},
        ProdFeeAdd: {},
        selectRowData: {},
        dataParams: [],
        isShareSort: '0',
        allowFeeInfoBlank: false,//是否允许费用相关信息为空 默认为否
        allowBlankEdit: false,//修改框中是否允许费用相关信息为空 默认为否
      };
    },



    mounted(){

    },
    methods: {
      //新增框中费率发生变化
      changeRate(value) {
        if (value == 0) {
          this.allowFeeInfoBlank = true;
        } else {
          this.allowFeeInfoBlank = false;
        }
      },
      //修改框中费率发生变化
      changeEditRate(value) {
        if (value == 0) {
          this.allowBlankEdit = true;
        } else {
          this.allowBlankEdit = false;
        }
      },
      passDataSuccess() {
        this.$emit('isShowButton', '1')
      },
      saveProdFee(val) {
        val["assemblyMenuType"] = 'prodFee';
        val["t8ProdFees"] = JSON.stringify(this.dataParams);
        val["prodCode"] = this.ProdFee.prodCode;
        val["t8ProdInfoId"] = this.ProdFee.t8ProdInfoId;
      },

      saveProdFeeAdd(val){
        let validateResult = this.$refs.addT8FeeProdForm.validate();
        if(!validateResult){
          return false;
        }
        let flags = true;
        this.dataParams.forEach(function(i,index){
          if(val.feeType==i.feeType){
            flags = false;
          }
        })
        if(!flags){
          Tools.alert("相同费用类型只能保存一个!","danger");
          return false;
        }
        this.dataParams.push(val);
        console.log(val)
        this.uptDataParams(this.dataParams);
        this.$refs.addT8FeeProdPopup.close();
      },

      saveProdFeeUpt(val){
        let validateResult = this.$refs.editT8FeeProdForm.validate();
        if(!validateResult){
          return false;
        }
        let flags = true;


        val.t8ProdInfoId = this.ProdFee.t8ProdInfoId;
        val.prodCode = this.ProdFee.prodCode;
        let temp = this.dataParams[val.numId];
        this.$delete(this.dataParams,val.numId);
        this.dataParams.forEach(function(i,index){
          if(val.feeType==i.feeType){
            flags = false;
          }
        })
        if(!flags){
          this.dataParams.push(temp);
          Tools.alert("费用类型已存在!","danger");
          return false;
        }
        this.dataParams.push(val);
        this.uptDataParams(this.dataParams);
        this.$refs.editT8FeeProdPopup.close();

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
        this.$set(this.$refs.t8FeeProdGrid,'list',this.dataParams);
      },





      clearFrom(val){
        if(val == '1'){
          this.ProdFeeUpt.rate = '';
          this.ProdFeeAdd.rate = '';
        }else{
          this.ProdFeeUpt.domesticRate = '';
          this.ProdFeeUpt.abroadRate = '';
          this.ProdFeeAdd.domesticRate = '';
          this.ProdFeeAdd.abroadRate = '';
        }

      },



      addHandler(){
        if(this.ProdFee.prodCode == '' || this.ProdFee.prodCode == null){
          Tools.alert("正在获取产品信息，请稍后。。。。。。。","danger");
          return false;
        }
        if(this.ProdFee.t8ProdInfoId == '' || this.ProdFee.t8ProdInfoId == null){
          Tools.alert("正在获取产品信息，请稍后。。。。。。。","danger");
          return false;
        }
        if(this.ProdFee.endDate == '' || this.ProdFee.endDate == null){
          Tools.alert("未获取到周期信息，请先确认是否录入产品到期日","danger");
          return false;
        }
        if(this.ProdFee.establishDate == '' || this.ProdFee.establishDate == null){
          Tools.alert("未获取到周期信息，请先确认是否录入产品成立日","danger");
          return false;
        }
        this.ProdFeeAdd = {};
        this.ProdFeeAdd.t8ProdInfoId = this.ProdFee.t8ProdInfoId;
        this.ProdFeeAdd.prodCode = this.ProdFee.prodCode;
        this.allowFeeInfoBlank = false;
        this.$refs.addT8FeeProdPopup.popup();
      },

      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.ProdFeeUpt = Object.assign({}, row)
        if (this.ProdFeeUpt.rate > 0 || this.ProdFeeUpt.domesticRate > 0) {
          this.allowBlankEdit = false;
        } else {
          this.allowBlankEdit = true;
        }
      }
    },
    created() {
      console.log("prodFee--this.prodIsShareSort=:>>",this.prodIsShareSort);
      eventBus.$on('shareSortChange', item => {
        if (item.shareSort === '1') {
          this.isShareSort = '1'
        } else {
          this.isShareSort = '0'
        }
        console.log("this.isShareSort=:>",this.isShareSort);
      })
    },
    watch: {
      'ProdFee.dataParams' : function (value) {
        this.uptDataParams(value);
      },
      'T8ProdCalendar.endDate' : function (value) {
        if(value){
          this.ProdFee.endDate = value;
        }
      },
      'T8ProdCalendar.establishDate' : function (value) {
        if(value) {
          this.ProdFee.establishDate = value;
        }
      },


    },
  };
</script>
<style lang="scss" scoped>
.add-btn-div{
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
