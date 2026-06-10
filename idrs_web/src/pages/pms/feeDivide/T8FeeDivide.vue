<template>
  <div>
    <k-form-search-customize data-target="t8FeeDivideGrid" v-model="prodSearchParam">
<!--      <k-form-item label="产品代码">
        <k-field-select v-model="prodSearchParam.prodCode" data-action="T8Dict.findNotEstablishProdInfos"
                        data-display-field="prodCode,prodName" data-value-field="prodCode"/>
      </k-form-item>-->
      <k-form-item label="销售商代码">
        <k-field-select v-model="prodSearchParam.distributorCode" data-action="T8Dict.findTaDistributorInfos"
                        data-display-field="distributorName"  data-value-field="distributorCode"/>
      </k-form-item>
      <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.initFormData()"
             data-target="addPopup"
             v-if="global.isShowAuthorityButton('T8FeeDivide.addT8FeeDivide')">
        <md-icon md-src="/static/svg/add.svg" />新增
      </k-btn>
    </k-form-search-customize>
    <k-grid ref="t8FeeDivideGrid"
            @data-row-select="selectRow" data-action="T8FeeDivide.find1">
      <k-grid-column data-align="center" data-hidden="true" data-header="销售商代码" data-name="distributorCode"/>
      <k-grid-column data-align="center" data-header="销售商名称" data-name="distributorName"/>
      <k-grid-column data-align="center" data-header="费用类型" data-name="feeType" data-dict="t8_fee_divide_fee_type"/>
      <k-grid-column data-align="center" data-header="启用日期" data-type="date" data-name="enableDate"/>
      <k-grid-column data-align="center" data-header="分成方式" data-dict="divide_type" data-name="divideType"/>
      <k-grid-column data-align="center" data-header="归销售商比例（%）" data-type="number" data-name="distributorDivideRate"/>
      <k-grid-column data-align="center" data-header="归管理人比例（%）" data-type="number" data-name="managerDivideRate"/>
      <k-grid-column data-align="center" data-header="归销售商固定BP" data-type="number" data-name="distributorDivideBp"/>
      <k-grid-column data-align="center" data-header="创建人" data-name="createUserName"/>
      <k-grid-column data-align="center" data-header="创建日期" data-type="date" data-name="createDate"/>
      <k-grid-column data-align="center" data-header="创建时间" data-type="time" data-name="createTime"/>
      <k-grid-column data-align="center" data-header="数据状态" data-dict="t8_fee_divide_status" data-name="status"/>
      <k-grid-column data-align="center" data-header="产品名称"
                     data-name="prodName" data-value-field="prodName" />
      <template slot="operate" slot-scope="scope">
        <k-btn data-functype="POPUP" data-size="mini"
               class="md-info md-just-icon md-simple" data-target="detailPopup"
               :data-handler="detailHandler" data-descript="查看费用分成详情">
          <md-icon>library_books</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple"
               data-descript="修改费用分成信息" data-functype="POPUP" data-size="small"
               :data-handler="editHandler" data-target="editPopup"
               v-if="global.isShowAuthorityButton('T8FeeDivide.updateT8FeeDivide')">
          <md-icon>edit</md-icon>
        </k-btn>

        <k-btn data-functype="POPUP" data-size="mini" data-target="detailStatus"
               class="md-info md-just-icon md-simple" :data-disabled="scope.row.row.status == 1"
               :data-handler="detailHandler" data-descript="确认费用分成信息"
               v-if="global.isShowAuthorityButton('T8FeeDivide.updateFeeDivideStatus')">
          <md-icon>done</md-icon>
        </k-btn>


        <k-btn class="md-info md-just-icon md-simple"
               data-descript="关联产品" data-functype="POPUP" data-size="small"
               :data-handler="associaProdHandler" data-target="editProdPopup"
               v-if="global.isShowAuthorityButton('T8FeeDivide.updateT8FeeDivideProd')">
          <md-icon>add</md-icon>
        </k-btn>
      </template>
    </k-grid>

    <k-popup ref="addPopup" data-title="添加费用分成信息">
      <k-form ref="addForm" :data-col="2">
        <k-form-item label="销售商代码" >
          <k-field-select v-model="formData.distributorCode" data-action="T8Dict.findTaDistributorInfos"
                          :dataAllowblank='false'
                          data-display-field="distributorName" @data-on-change="selectProdInfo(formData)" data-value-field="distributorCode"  />
        </k-form-item>
        <k-form-item label="费用类型">
          <k-field-select v-model="formData.feeType" data-dict="t8_fee_divide_fee_type" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="分成方式">
          <k-field-select v-model="formData.divideType" data-dict="divide_type" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="启用日期">
          <k-field-date v-model="formData.enableDate" :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="归销售商比例(%)" v-show="!switchSegmentValue && formData.divideType == '1'">
          <k-field-text data-validate-type="number" v-model="formData.distributorDivideRate" data-min-value="0" data-max-value="100" data-digits="5"
                        :data-disabled="false" :data-allowblank="!(!switchSegmentValue && formData.divideType == '1')" />
        </k-form-item>
        <k-form-item label="归管理人比例(%)" v-show="!switchSegmentValue && formData.divideType == '1'">
          <k-field-text data-validate-type="number" v-model="formData.managerDivideRate" data-max-value="100" data-digits="5"
                        data-min-value="0" :data-disabled="true" :data-allowblank="!(!switchSegmentValue && formData.divideType == '1')"/>
        </k-form-item>

        <k-form-item label="归销售商固定BP" v-show="!switchSegmentValue && formData.divideType == '2'">
          <k-field-text data-validate-type="number" data-min-value="0" data-max-value="10000" v-model="formData.distributorDivideBp" data-digits="5"
                        :data-allowblank="!(!switchSegmentValue && formData.divideType == '2')" />
        </k-form-item>
        <!--    data-action="T8Dict.findNotEstablishProdInfos"    -->
        <k-form-item label="产品代码">
          <k-field-select v-model="formData.prodCode" :data-auto-load="false"
                          :data-data="prodInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode"
                          :dataAllowblank="true" data-multiple="true" />
        </k-form-item>
      </k-form>
      <div class="my-container">
        <div class="my-item2">
          <div style="width: 135px !important">
            <md-switch v-model="switchSegmentValue" class="md-info" @change="changeSegmentType">份额分段</md-switch>
          </div>
          <div class="my-item2-chips">
            <md-chips v-model="moneyList" class="md-primary" style="padding-top: 4px !important;" md-placeholder="请输入分段份额,并按回车确认"  md-input-type="number"  @md-delete="deleteMoney" @md-insert="insertMoney"  v-show="switchSegmentValue" ></md-chips>
          </div>
        </div>
      </div>

      <div label="份额分段展示"  class="my-table" v-show="switchSegmentValue">
        <md-table>
          <md-table-row>
            <md-table-head v-show="moneyList.length > 0 " >份额段</md-table-head>
            <md-table-head v-show="formData.divideType=='1'">归管理人比例(%)</md-table-head>
            <md-table-head v-show="formData.divideType=='1'">归销售商比例(%)</md-table-head>
            <md-table-head v-show="formData.divideType=='2'">归销售商固定BP</md-table-head>
          </md-table-row>

          <!--没有数据时才展示这一行 -->
          <md-table-row v-show="moneyList.length == 0">
            <md-table-cell :colspan="4" style="text-align:center">
              <p> 暂无数据</p>
            </md-table-cell>
          </md-table-row>
          <md-table-row   v-show=" moneyList.length > 0" v-for="(item,index) in tailingCommisionMoneyList" :key="index">
            <md-table-cell v-show="item.showMoneyTd" :rowspan="item.moneyRowspan" >{{ item.moneyDesc }}</md-table-cell>
            <md-table-cell v-show="formData.divideType=='1' ">
              <md-field>
                <md-input class="md-input" style="width: 40px;text-align: left;" @blur="changeListRate(item)" v-model="item.managerDivideRate"
                          md-input-type="number"/>
                <span class="md-suffix">%</span>
              </md-field>
            </md-table-cell>
            <md-table-cell v-show="formData.divideType=='1' ">
              <md-field>
                <md-input class="md-input" style="width: 40px; text-align: left;" :disabled="true" v-model="item.distributorDivideRate"
                          md-input-type="number" />
                <span class="md-suffix">%</span>
              </md-field>
            </md-table-cell>
            <md-table-cell v-show="formData.divideType=='2'">
              <md-field>
                <md-input class="md-input" style="text-align: left;" v-model="item.distributorDivideBp" @blur="changeListRate2(item)"
                          md-input-type="number"/>
              </md-field>
            </md-table-cell>
          </md-table-row>
        </md-table>
      </div>
      <div>
        <k-form>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8FeeDivide.addT8FeeDivide" data-from="addForm"
                   :data-model="formData" data-target="t8FeeDivideGrid" :data-handler="beforeSubmit">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
          </k-form-footer>
        </k-form>
      </div>
    </k-popup>


    <k-popup ref="editPopup" data-title="修改费用分成信息">
      <k-form ref="addForm" :data-col="2">
        <k-form-item label="销售商代码" >
          <k-field-select v-model="formData.distributorCode" data-action="T8Dict.findTaDistributorInfos"
                          :dataAllowblank='false' :data-disabled="true"
                          data-display-field="distributorName" @data-on-change="selectProdInfo(formData)" data-value-field="distributorCode"  />
        </k-form-item>
        <k-form-item label="费用类型">
          <k-field-select v-model="formData.feeType" data-dict="t8_fee_divide_fee_type"
                          :data-disabled="true"
                          :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="分成方式">
          <k-field-select v-model="formData.divideType" data-dict="divide_type" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="启用日期">
          <k-field-date v-model="formData.enableDate" :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="归销售商比例(%)" v-show="!switchSegmentValue && formData.divideType == '1'">
          <k-field-text data-validate-type="number" data-min-value="0" v-model="formData.distributorDivideRate" data-max-value="100" data-digits="5"
                        :data-allowblank="!(!switchSegmentValue && formData.divideType == '1')" />
        </k-form-item>
        <k-form-item label="归管理人比例(%)" v-show="!switchSegmentValue && formData.divideType == '1'">
          <k-field-text data-validate-type="number" v-model="formData.managerDivideRate" data-max-value="100" data-digits="5"
                        :data-disabled="true" data-min-value="0"  :data-allowblank="!(!switchSegmentValue && formData.divideType == '1')"/>
        </k-form-item>
        <k-form-item label="归销售商固定BP" v-show="!switchSegmentValue && formData.divideType == '2'">
          <k-field-text data-validate-type="number" data-min-value="0" data-max-value="10000" v-model="formData.distributorDivideBp" data-digits="5"
                        :data-allowblank="!(!switchSegmentValue && formData.divideType == '2')" />
        </k-form-item>
        <k-form-item label="产品代码">
          <k-field-select v-model="formData.prodCode"
                          data-display-field="prodCode,prodName"
                          :data-params="{'distributorCode':this.formData.distributorCode}"
                          data-action="T8Dict.findProdInfosByCustNo"
                          data-value-field="prodCode"
                          :data-disabled="false" data-multiple="true"
                          :dataAllowblank="true"/>
        </k-form-item>
      </k-form>
      <div class="my-container">
        <div class="my-item2">
          <div style="width: 135px !important">
            <md-switch v-model="switchSegmentValue" class="md-info" @change="changeSegmentType">份额分段</md-switch>
          </div>
          <div class="my-item2-chips">
            <md-chips v-model="moneyList" class="md-primary" style="padding-top: 4px !important;" md-placeholder="请输入分段份额,并按回车确认"  md-input-type="number"  @md-delete="deleteMoney" @md-insert="insertMoney"  v-show="switchSegmentValue" ></md-chips>
          </div>
        </div>
      </div>
      <div label="份额分段展示"  class="my-table" v-show="switchSegmentValue">
        <md-table>
          <md-table-row>
            <md-table-head v-show="moneyList.length > 0 " >份额段</md-table-head>
            <md-table-head v-show="formData.divideType=='1'">归管理人比例(%)</md-table-head>
            <md-table-head v-show="formData.divideType=='1'">归销售商比例(%)</md-table-head>
            <md-table-head v-show="formData.divideType=='2'">归销售商固定BP</md-table-head>
          </md-table-row>

          <!--没有数据时才展示这一行 -->
          <md-table-row v-show="moneyList.length == 0">
            <md-table-cell :colspan="4" style="text-align:center">
              <p> 暂无数据</p>
            </md-table-cell>
          </md-table-row>

          <md-table-row   v-show=" moneyList.length > 0" v-for="(item,index) in tailingCommisionMoneyList" :key="index">
            <md-table-cell v-show="item.showMoneyTd" :rowspan="item.moneyRowspan" >{{ item.moneyDesc }}</md-table-cell>
            <md-table-cell v-show="formData.divideType=='1' ">
              <md-field>
                <md-input class="md-input" style="width: 40px;text-align: left;" v-model="item.managerDivideRate" @blur="changeListRate(item)"
                          md-input-type="number"/>
                <span class="md-suffix">%</span>
              </md-field>
            </md-table-cell>
            <md-table-cell v-show="formData.divideType=='1' ">
              <md-field>
                <md-input class="md-input" style="width: 40px; text-align: left;" :disabled="true" v-model="item.distributorDivideRate"
                          md-input-type="number" />
                <span class="md-suffix">%</span>
              </md-field>
            </md-table-cell>
            <md-table-cell v-show="formData.divideType=='2'">
              <md-field>
                <md-input class="md-input" style="text-align: left;" v-model="item.distributorDivideBp"
                          md-input-type="number" @blur="changeListRate2(item)"/>
              </md-field>
            </md-table-cell>
          </md-table-row>
        </md-table>
      </div>
      <div>
        <k-form>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8FeeDivide.updateT8FeeDivide" data-from="addForm"
                   :data-model="formData" data-target="t8FeeDivideGrid" :data-handler="beforeSubmit">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
          </k-form-footer>
        </k-form>
      </div>
    </k-popup>
    <k-popup ref="editProdPopup" data-title="关联产品">
      <k-form ref="editProdForm" :data-col="2">
        <k-form-item label="销售商代码" >
          <k-field-select v-model="formData.distributorCode" data-action="T8Dict.findTaDistributorInfos"
                          :dataAllowblank='false' :data-disabled="true"
                          data-display-field="distributorName"  data-value-field="distributorCode"  />
        </k-form-item>
        <k-form-item label="费用类型">

          <k-field-select v-model="formData.feeType" data-dict="t8_fee_divide_fee_type"
                          :data-disabled="true"
                          :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="分成方式">
          <k-field-select v-model="formData.divideType" data-dict="divide_type"
                          :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="启用日期">
          <k-field-date v-model="formData.enableDate" :data-allowblank="false"
                        :data-disabled="true"/>
        </k-form-item>
<!--        <k-form-item label="归管理人比例(%)" v-show="!switchSegmentValue && formData.divideType == '1'">-->
<!--          <k-field-text data-validate-type="number" v-model="formData.managerDivideRate"-->
<!--                        data-max-value="100" data-digits="5"-->
<!--                        data-min-value="0" :data-disabled="true"-->
<!--                        :data-allowblank="!(!switchSegmentValue && formData.divideType == '1')"/>-->
<!--        </k-form-item>-->
<!--        <k-form-item label="归销售商比例(%)" v-show="!switchSegmentValue && formData.divideType == '1'">-->
<!--          <k-field-text data-validate-type="number" v-model="formData.distributorDivideRate" data-max-value="100" data-digits="5"-->
<!--                        :data-disabled="true" :data-allowblank="!(!switchSegmentValue && formData.divideType == '1')" />-->
<!--        </k-form-item>-->
<!--        <k-form-item label="归销售商固定BP" v-show="!switchSegmentValue && formData.divideType == '2'">-->
<!--          <k-field-text data-validate-type="number" v-model="formData.distributorDivideBp" data-digits="5"-->
<!--                        :data-allowblank="!(!switchSegmentValue && formData.divideType == '2')" />-->
<!--        </k-form-item>-->
        <k-form-item label="产品代码">
          <k-field-select v-model="formData.prodCode" data-action="T8Dict.findNotEstablishProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode"
                          :data-allowblank="false" data-multiple="true" @data-on-change="change"/>
        </k-form-item>
      </k-form>
      <k-form>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8FeeDivide.updateT8FeeDivideProd" data-from="editProdForm"
                 :data-model="formData" data-target="t8FeeDivideGrid" :data-handler="validateData">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <div class="popClass">
      <k-popup class="popClass" ref="detailPopup" data-title="费用分成详情"  data-width="1100px">
        <div  dataLabelWidth="170px" dataInputWidth="300px">
          <DisplayFeeDivide ref="baseInfoForm" v-model="detailData" :detailData="detailData"/>
        </div>
      </k-popup>

      <k-popup ref="detailStatus" data-title="确认费用分成"  data-width="1100px">
        <div  dataLabelWidth="170px" dataInputWidth="300px">
          <DisplayFeeDivide ref="StatusForm" v-model="detailData" :detailData="detailData"/>
        </div>
        <k-form>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8FeeDivide.updateFeeDivideStatus"
                   data-from="StatusForm" :data-model="detailData" data-target="t8FeeDivideGrid">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
          </k-form-footer>
        </k-form>
      </k-popup>
    </div>



  </div>
</template>

<script>
  import {assign} from "lodash";
  import Tools from "@/utils/tools";
  const math = require("mathjs");
  import DisplayFeeDivide         from "../M81/prodDisplay/DisplayFeeDivide.vue"

  export default {
    name: "",
    components: {DisplayFeeDivide},
    mounted() {
      this.httpUtil.sysDate().then(res=>{
        //console.log(res);
        if (res) {

          this.currentWorkday = res;
        }
      });
    },
    watch:{
      /*'formData.managerDivideRate'(newValue) {
        if(newValue != null && newValue !=''){
          if(newValue>100){
            newValue = 100;
          }
          this.formData.distributorDivideRate = math.format(100 - newValue,7);
        }
      },*/
      'formData.distributorDivideRate'(newValue) {
        if(newValue != null && newValue !=''){
          if(newValue>100){
            newValue = 100;
          }
          this.formData.managerDivideRate = math.format(100 - newValue,7);
        }
      }
    },
    data() {
      return {
        moneyList: [],
        tailingCommisionMoneyList:[],
        switchSegmentValue : false,   //金额分段切换按钮的值
        switchDetailSegmentValue : false,   //详情金额分段切换按钮的值
        currentWorkday:null,
        prodInfos:{},
        prodSearchParam: {
          prodCode: '',
          distributorCode:'',
        },
        formData:{
          prodCode:'',
          distributorCode:'',
          feeType:'',
          enableDate:'',
          divideType:'',
          distributorDivideRate:'',
          managerDivideRate:'',
          distributorDivideBp:'',
          t8FeeDivideSectionList:''
        },
        detailData:{
          prodCode:'',
          prodName:'',
          distributorCode:'',
          distributorName:'',
          feeType:'',
          enableDate:'',
          divideType:'',
          distributorDivideRate:'',
          managerDivideRate:'',
          distributorDivideBp:'',
          t8FeeDivideSectionList:[]
        },
        selectRowData:{},
        oldProdCode: '',
        countFlag:0
      }
    },
    methods: {
      selectRow(row, column, event) {
        const _this = this;
        _this.selectRowData = assign({}, row);
      },
      initFormData(){
        this.formData = {
          prodCode: '',
          distributorCode: '',
          feeType: '',
          enableDate: '',
          divideType: '',
          distributorDivideRate: '',
          managerDivideRate: '',
          distributorDivideBp: '',
          t8FeeDivideSectionList:''
        };
        this.moneyList=[];
        this.switchSegmentValue =false;
        this.tailingCommisionMoneyList=[];
      },
      validateData(){
        let validateResult = this.$refs.editProdForm.validate();
        return validateResult;
      },
      beforeSubmit (value){
        // if(this.formData.prodCode==null || this.formData.prodCode==''){
        //   Tools.alert("未录入产品代码","danger");
        //   return false;
        // }

        let ret = this.$refs.addForm.validate();
        if(!ret){
          return false;
        }

        //标记校验是否通过，是否允许提交
        let commitFlag = true;
        let errorMsg = "";

        if(this.switchSegmentValue){
          this.tailingCommisionMoneyList.forEach(e => {
            //数据校验，如果输入的是费用，则费率清空，否则费用清空
            if (this.formData.divideType == '1'){
              if (e.managerDivideRate == null || e.managerDivideRate ==''){
                errorMsg = "存在归管理人比例未录入";
                commitFlag=false;
              }
              if (Number(e.rate)<0 || Number(e.rate) > Number(100) ){
                errorMsg =  "在归管理人比例必须在0-100之间";
                commitFlag =  false;
              }
            }else {
              if (e.distributorDivideBp == null || e.distributorDivideBp ==''){
                errorMsg = "存在归销售商固定BP未录入";
                commitFlag=false;
              }
              if (Number(e.rate)<0){
                errorMsg =  "存在归销售商固定BP不能为负";
                commitFlag =  false;
              }
            }
          })
        }
        if(commitFlag){
          this.formData.t8FeeDivideSectionList = JSON.stringify(this.tailingCommisionMoneyList);
          value.t8FeeDivideSectionList = this.formData.t8FeeDivideSectionList;
        }else{
          Tools.alert( errorMsg, "danger");
          return false;
        }
        this.$set(value,'switchDetailSegmentValue',this.switchSegmentValue);
      },
      selectProdInfo(value){
        //console.log("value",value);
        this.httpUtil.comnQuery({
          action: 'T8Dict.findProdInfosByCustNo',
          params: {
            distributorCode: value.distributorCode,
          }
        }).then(data => {
          this.$nextTick(()=>{
            if (data.rows.length>0){
              this.prodInfos = data.rows;
              //this.$set(data.rowsthis.formData.prodCode,"t8PrintTempVersionId","");
            }
          })
        });
      },
      changeSegmentType() {
        this.moneyList = [];
        this.tailingCommisionMoneyList=[];
        this.formData.managerDivideRate='';
        this.formData.distributorDivideRate='';
        this.formData.distributorDivideBp='';
      },
      buildMoneyTable(type){
        if ((this.moneyList.length == 1 && type != 'pop') ||
          this.tailingCommisionMoneyList == null || this.tailingCommisionMoneyList.length == 0) {
          this.tailingCommisionMoneyList = [];
          for(var i = 0 ;i < this.moneyList.length ; i++){
            //1-金额，2-时间
            this.tailingCommisionMoneyList.push(
              {
                //最小金额
                dimensionMin: i == 0 ? 0 : this.moneyList[i-1],
                //最大金额
                dimensionMax: this.moneyList[i],
                moneyDesc: (i == 0 ? 0 : this.moneyList[i-1] )+ ' <= 份额 < ' + this.moneyList[i],
                distributorDivideRate:'',
                managerDivideRate:'',
                distributorDivideBp:'',
                moneyRowspan: 1,
                showMoneyTd: true,
              }
            )
          }
          var lastMoney = {
            dimensionMin: this.moneyList[this.moneyList.length-1],
            dimensionMax: '-1',
            moneyDesc: this.moneyList[this.moneyList.length-1] + ' <= 份额 < ∞',
            distributorDivideRate:'',
            managerDivideRate:'',
            distributorDivideBp:'',
            moneyRowspan: 1,
            showMoneyTd: true,
          };
          this.tailingCommisionMoneyList.push(lastMoney);
        }else {
          if (type == 'push'){
            let listElement = this.tailingCommisionMoneyList[this.tailingCommisionMoneyList.length-1];
            listElement.dimensionMax=this.moneyList[this.moneyList.length-1];
            listElement.moneyDesc=listElement.dimensionMin + '<= 份额 <' +listElement.dimensionMax;
            let lastMoney = {
              dimensionMin: this.moneyList[this.moneyList.length-1],
              dimensionMax: '-1',
              moneyDesc: this.moneyList[this.moneyList.length-1] + ' <= 份额 < ∞',
              distributorDivideRate:'',
              managerDivideRate:'',
              distributorDivideBp:'',
              moneyRowspan: 1,
              showMoneyTd: true,
            };
            this.tailingCommisionMoneyList.push(lastMoney);
          }else {
            this.tailingCommisionMoneyList.pop();
            let element = this.tailingCommisionMoneyList[this.tailingCommisionMoneyList.length-1];
            element.dimensionMax='-1';
            element.moneyDesc=element.dimensionMin + ' <= 份额 < ∞'
          }
        }
      },
      insertMoney(value){
        //插入后，已经插入了
        if(value <= 0 || parseInt(this.moneyList[this.moneyList.length-2]) > parseInt(value)){
          this.moneyList.pop();
          Tools.alert( "份额不能小于等于0，不能小于上次输入份额", "danger");
          return false;
        }
        this.buildMoneyTable('push');
        return value;
      },
      deleteMoney(text, index){
        //是否已经删完
        if(this.moneyList.length == 0){
          Tools.alert( "已经没有数据", "danger");
          return false;
        }
        //inde-下标，从0开始
        if(index != this.moneyList.length){
          Tools.alert( "请顺序删除", "danger");
          //在指定位置添加元素,第一个参数指定位置,第二个参数指定要删除的元素,如果为0,则追加
          this.moneyList.splice(index, 0, text);
          return false;
        }
        this.buildMoneyTable('pop');
      },
      changeListRate(value){
        let price = '' + value.managerDivideRate;
        price = price
          .replace(/[^\d.-]/g, '') // 清除“数字”和“.”以外的字符
          .replace(/\.{2,}/g, '.') // 只保留第一个. 清除多余的
          .replace('.', '$#$')
          .replace(/\./g, '')
          .replace('$#$', '.')
          .replace(/^(\-)*(\d+)\.(\d\d\d\d).*$/, '$1$2.$3'); // 只能输入两个小数
        if (price.indexOf('.') < 0 && price != '') {
          // 以上已经过滤，此处控制的是如果没有小数点，首位不能为类似于 01、02的金额
          price = parseFloat(price);
        }
        value.managerDivideRate = price;
        //console.log("value.managerDivideRate=:>>>>>>>>>",value.managerDivideRate);
        if(price===''||price===undefined||price===null){
          value.managerDivideRate = null;
          value.distributorDivideRate = null;
        }else if(price>100){
          value.managerDivideRate = 100;
          value.distributorDivideRate = 0;
        }else if(price<0){
          value.managerDivideRate = null;
          value.distributorDivideRate = null;
          Tools.alert("归管理人比例不能为负数", "danger");
        }else {
          value.distributorDivideRate = math.format(100 - value.managerDivideRate,7);
          //console.log(value.distributorDivideRate);
        }
      },
      changeListRate2(value){
        let price = '' + value.distributorDivideBp;
        price = price
          .replace(/[^\d.-]/g, '') // 清除“数字”和“.”以外的字符
          .replace(/\.{2,}/g, '.') // 只保留第一个. 清除多余的
          .replace('.', '$#$')
          .replace(/\./g, '')
          .replace('$#$', '.')
          .replace(/^(\-)*(\d+)\.(\d\d\d\d).*$/, '$1$2.$3'); // 只能输入两个小数
        if (price.indexOf('.') < 0 && price != '') {
          // 以上已经过滤，此处控制的是如果没有小数点，首位不能为类似于 01、02的金额
          price = parseFloat(price);
        }
        value.distributorDivideBp = price;

        //console.log("value.managerDivideRate=:>>>>>>>>>",value.distributorDivideBp);
        if(value.distributorDivideBp==''||value.distributorDivideBp==undefined||value.distributorDivideBp==null){
          value.distributorDivideBp = 0;
        }else if(value.distributorDivideBp>10000){
          value.distributorDivideBp = 10000;
        }else if(value.distributorDivideBp<0){
          value.distributorDivideBp = null;
          Tools.alert("归销售商固定BP不能为负数", "danger");
        }else if(value.distributorDivideBp==0){
          value.distributorDivideBp = 0;
        }else{
        }
      },
      detailHandler(value){
        this.detailData = value;
        this.httpUtil.comnQuery({
          action: 'T8FeeDivideSection.findByT8FeeDivideId',
          params: {
            t8FeeDivideId: value.id,
          }
        }).then(data => {
          this.$nextTick(()=>{
            if (data.rows.length>0){
              let list = data.rows;
              for(let a in list) {
                this.$set(list[a],'moneyDesc',list[a].dimensionDesc);
              }
              let t8FeeDivideSectionList = JSON.stringify(list);
              this.$set(this.detailData,'t8FeeDivideSectionList',t8FeeDivideSectionList);
              this.$set(this.detailData,'tailingCommisionMoneyList',data.rows);
              if(list.length > 1){
                this.$set(this.detailData,'switchDetailSegmentValue',true);
              }else{
                this.$set(this.detailData,'switchDetailSegmentValue',false);
              }
            }
          })
 //         this.$refs.detailPopup.popup();
        });
      },
      //关联产品
      associaProdHandler(value){
        this.formData = value;
        this.oldProdCode = this.formData.prodCode;
        this.countFlag = 0;
      },
      change(value) {
        if(this.oldProdCode!=''&&this.oldProdCode!=undefined&&this.oldProdCode!=""){
          let codeArr = this.oldProdCode.split(",");
          let deleteCode = '';
          let count = 0;
          for(let i=0;i<codeArr.length;i++){
            if(value.indexOf(codeArr[i]) == -1){
              count = count+1;
              if(count==1){
                deleteCode=deleteCode+codeArr[i];
              }else{
                deleteCode=deleteCode+","+codeArr[i];
              }
            }
          }
          if(deleteCode.length>1 && deleteCode.length<this.oldProdCode.length){
            Tools.alertDiy("您删除了"+deleteCode+"产品关联信息!","danger")
          }
          if(deleteCode.length==this.oldProdCode.length && this.countFlag==0){
            this.countFlag = this.countFlag+1;
            Tools.alertDiy("您删除了"+deleteCode+"产品关联信息!","danger")
          }
        }
      },
      editHandler(value){
        this.formData = value;
        this.httpUtil.comnQuery({
          action: 'T8FeeDivideSection.findByT8FeeDivideId',
          params: {
            t8FeeDivideId: value.id,
          }
        }).then(data => {
          this.$nextTick(()=>{
            if (data.rows.length>0){
              this.moneyList=[];
              this.tailingCommisionMoneyList=[];
              for (let i = 0; i < data.rows.length; i++) {
                let row = data.rows[i];
                if (row.dimensionMin == '0'){
                  this.tailingCommisionMoneyList.push(
                    {
                      //最小金额
                      dimensionMin: '0',
                      //最大金额
                      dimensionMax: row.dimensionMax,
                      moneyDesc: row.dimensionDesc,
                      distributorDivideRate:row.distributorDivideRate,
                      managerDivideRate:row.managerDivideRate,
                      distributorDivideBp:row.distributorDivideBp,
                      moneyRowspan: 1,
                      showMoneyTd: true,
                    }
                  )
                }else if (row.dimensionMax == '-1') {
                  this.tailingCommisionMoneyList.push(
                    {
                      //最小金额
                      dimensionMin: row.dimensionMin,
                      //最大金额
                      dimensionMax: '-1',
                      moneyDesc: row.dimensionDesc,
                      distributorDivideRate:row.distributorDivideRate,
                      managerDivideRate:row.managerDivideRate,
                      distributorDivideBp:row.distributorDivideBp,
                      moneyRowspan: 1,
                      showMoneyTd: true,
                    }
                  )
                }else {
                  this.tailingCommisionMoneyList.push(
                    {
                      //最小金额
                      dimensionMin: row.dimensionMin,
                      //最大金额
                      dimensionMax: row.dimensionMax,
                      moneyDesc: row.dimensionDesc,
                      distributorDivideRate:row.distributorDivideRate,
                      managerDivideRate:row.managerDivideRate,
                      distributorDivideBp:row.distributorDivideBp,
                      moneyRowspan: 1,
                      showMoneyTd: true,
                    }
                  )
                }
                if (row.dimensionMin != '0'){
                  this.moneyList.push(row.dimensionMin);
                }
              }
              this.switchSegmentValue=true;
            }
          })
        });
      }
    }
  }
</script>

<style scoped>
  .popClass ::v-deep .el-dialog {margin-right: 10%;}
</style>
