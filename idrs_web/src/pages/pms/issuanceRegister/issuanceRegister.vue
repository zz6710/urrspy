<template>
  <div>
    <k-form-search-customize data-target="prodElementGrid" v-model="queryParam">

      <k-form-item label="产品代码">
        <k-field-select v-model="prodSearchParam.prodCode" data-action="T8Dict.findTaProdInfos"
                        data-display-field="prodCode,prodName" data-value-field="prodCode"/>
      </k-form-item>
      <k-form-item label="产品名称">
        <k-field-text v-model="prodSearchParam.prodName" data-validate-type="text"/>
      </k-form-item>
      <k-form-item label="募集日期区间">
        <k-field-date v-model="applyDate" data-type="daterange"/>
      </k-form-item>
      <k-form-item label="成立日期区间">
        <k-field-date v-model="establishDate" data-type="daterange"/>
      </k-form-item>
      <k-form-item label="是否完成报备">
        <k-field-select v-model="prodSearchParam.isCompleteConfirm" data-dict="is_default"/>
      </k-form-item>
    </k-form-search-customize>
    <k-grid ref="prodElementGrid"  data-action="T8ProdIssueRegister.find1" @data-row-select="selectRow">
      <k-grid-column data-align="center" data-header="产品id" data-name="t8ProdInfoId" data-width="120" data-hidden="true"/>
      <k-grid-column data-align="center" data-header="产品登记编码" data-name="registCode" data-width="120"/>
      <k-grid-column data-align="center" data-header="理财产品代码" data-name="prodCode" data-width="120"/>
      <k-grid-column data-align="center" data-header="理财产品名称" data-name="prodName" data-width="120"/>
      <k-grid-column data-align="center" data-header="募集起始日"  data-name="applyStartDate" data-width="120"/>
      <k-grid-column data-align="center" data-header="募集结束日"  data-name="applyEndDate" data-width="120"/>
      <k-grid-column data-align="center" data-header="产品成立日"  data-name="establishDate" data-width="120"/>
      <k-grid-column data-align="center" data-header="产品到期日"  data-name="endDate" data-width="120"/>
      <k-grid-column data-align="center" data-header="管理方式" data-name="manageMethod" data-dict="t8_managetype" data-width="120"/>
      <k-grid-column data-align="center" data-header="业绩比较基准上限" data-name="performBenchmarksUpper" data-width="150"/>
      <k-grid-column data-align="center" data-header="业绩比较基准下限" data-name="performBenchmarksLower" data-width="150"/>
      <k-grid-column data-align="center" data-header="业绩比较基准说明" data-name="performBenchmarksDesc" data-width="150"/>
      <k-grid-column data-align="center" data-header="报备时间"  data-name="operationDate" data-width="150"/>
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" data-descript="确认已报备" data-functype="POPUP" data-size="small"
               data-target="confirmPopup" v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId) &&
               global.isShowAuthorityButton('T8ProdIssueRegister.updateT8ProdInfoStatusForIssuancce')"
               :data-handler="confirmData" v-model="scope.row.row" :data-disabled="scope.row.row.status!='1'" v-show="showConfirm">
          <md-icon>playlist_add_check</md-icon>
        </k-btn>

        <k-btn data-functype="PAGE" data-size="mini"  class="md-info md-just-icon md-simple" :data-model="scope.row.row.prodCode"
               @click="popupEdit(scope.row.row)"  data-descript="产品发行要素详情管理">
          <md-icon>weekend</md-icon>
        </k-btn>
      </template>
    </k-grid>


    <k-popup ref="editT8ProdIssuePopup" data-title="详情">
      <ProdIssueAdjust ref="prodIssueAdjustF" v-model="formData" :T8ProdIssueRegisFields="formData"
                       :prodMode="formDataLoad.prodMode"
                         :prodCode="formData.prodCode" :assemblyMenuType="'issuanceRegister'"/>
    </k-popup>

    <k-popup ref="editT8ProdIssuePopupV" data-title="详情">
      <ProdIssueAdjust ref="prodIssueAdjustV" v-model="formDataLoad" :T8ProdIssueRegisFields="formDataLoad"
                       :prodMode="formDataLoad.prodMode"
                       :prodCode="formDataLoad.prodCode" :assemblyMenuType="'issuanceRegister'"/>
    </k-popup>
    <k-popup ref="confirmPopup" title="报备确认" >
      <k-form ref="confirmForm" :data-col="2">
        <k-form-item label="报备日期" >
          <k-field-date :dataAllowblank="false" v-model="formData.issueRegistDate">

          </k-field-date>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-from="confirmForm" :data-handler="beforeSubmit"
                 :data-model="formData" data-target="prodElementGrid" data-action="T8ProdIssueRegister.updateT8ProdInfoStatusForIssuancce" >
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

  </div>
</template>

<script>
  import {assign} from "lodash";
  import ProdIssueAdjust from "@/pages/pms/M81/prodDisplay/DisplayProdIssueAdjust.vue";
  export default {
    name: "issuanceRegister",
    components: {
      ProdIssueAdjust,
    },
    data() {
      return {
        formData:{},
        formDataLoad:{},
        rowData2:{
        },
        prodSearchParam: {
          prodCode:''
        },
        // 倒计时周期
        countNum:20,
        // 用于倒计时标记，true-正在倒计时
        countFlag:false,
        // 定时器
        intervalBtn:{},
        //默认按钮的值
        btnMsg:"确定(",
        mobile:"",
        applyDate:[],
        establishDate:[],
        showGenerate:true,//是否显示生成按钮
        showConfirm:true,//是否显示确认报备按钮
        showConfirmIssueInfo:true,//是否显示确认产品发行登记信息
        lastVersion:'',
      }
    },
    created() {
      this.global.getProdUser('');
      this.$nextTick(()=>{
        //获取需要隐藏按钮属性  rennannan 20210322   将接收到的属性值置为false达到隐藏按钮的效果
        this.global.getHideButtons(this);
        let prodCode = this.$route.query.prod_code;
        if(prodCode !=''&&prodCode!=undefined){
          this.$refs.prodElementGrid.load({prodCode:prodCode});
        }
      });
    },
    methods:{
      popupEdit(row){
        localStorage.setItem("issuanceRegisterData", JSON.stringify(row));
        let pathUrl = '/main/pms/issuanceRegister/issuanceRegisterElement';
        this.$router.push({
          path: pathUrl,
          query: {RegisterData: row},
        });
      },
      beforeSubmit(val){
        this.$set(val,'prodStatus','5');
        this.$set(val,'prodSonStatus','12');
        return val;
      },
      selectRow(row, column, event){
        const _this = this;
        _this.selectRowData = assign({}, row);
        this.formData = Object.assign({}, row)
        this.httpUtil.comnQuery({
          action: 'T8ProdIssueRegister.getMaxEdition',
          params: {
            t8ProdInfoId: _this.selectRowData.t8ProdInfoId,
          }
        }).then(data => {
          this.$nextTick(() => {
            let version = data.rows[0].version;
            this.lastVersion = version;
          })
        });

        // this.$refs.editionFrom.load({t8ProdInfoId:this.formData.t8ProdInfoId});
      },
      //点击报备确认按钮
      confirmData(value){
        this.formData=value;
      }
    },
    computed: {
      queryParam() {
        return {
          'prodName': this.prodSearchParam.prodName,//产品名称
          'isCompleteConfirm': this.prodSearchParam.isCompleteConfirm,//是否完成确认
          'prodCode': this.prodSearchParam.prodCode,//产品代码
          'applyStartDate':this.applyDate ? this.applyDate[0] : null,//募集期开始日
          'applyEndDate':this.applyDate ? this.applyDate[1] : null,//募集期结束日
          'establishDate': this.establishDate ? this.establishDate[0] : null,//成立期开始日
          'endDate': this.establishDate ? this.establishDate[1] : null,//成立日结束日
        }
      }

    }
  }
</script>

<style scoped>

</style>
