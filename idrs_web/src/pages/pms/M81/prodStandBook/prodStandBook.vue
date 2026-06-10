<template>
  <div>
    <k-form-search-customize data-target="taCustodianBankGrid" v-model="queryParam">
      <k-form-item label="产品代码">
        <k-field-select v-model="prodSearchParam.prodCode" data-action="T8Dict.findTaProdInfos"
                        data-display-field="prodCode,prodName" data-value-field="prodCode"/>
      </k-form-item>

      <k-form-item label="产品名称">
        <k-field-text v-model="prodSearchParam.prodName" data-validate-type="text"/>
      </k-form-item>
      <k-form-item label="产品登记编码">
        <k-field-text v-model="prodSearchParam.registCode" data-validate-type="text"/>
      </k-form-item>
      <k-form-item label="产品经理">
        <k-field-select v-model="prodSearchParam.prodManageId" :data-params="{roleId:'3'}"
                        data-action="User.getUserByRoleId2" data-display-field="username"
                        data-value-field="userid"/>
      </k-form-item>
      <k-form-item label="投资经理">
        <k-field-select v-model="prodSearchParam.investManageId" :data-params="{roleId:'14'}"
                        data-action="User.getUserByRoleId2" data-display-field="username"
                        data-value-field="userid"/>
      </k-form-item>
      <k-form-item label="成立日">
        <k-field-date v-model="establishDateRange" data-type="daterange" :data-allowblank="true"/>
      </k-form-item>
      <k-form-item label="开放日">
        <k-field-date v-model="openDateRange" data-type="daterange" :data-allowblank="true"/>
      </k-form-item>
      <k-form-item label="到期日">
        <k-field-date v-model="expireDateRange" data-type="daterange" :data-allowblank="true"/>
      </k-form-item>
      <k-form-item label="产品系列">
        <k-field-select v-model="prodSearchParam.seriesCode" data-action="T8ProdInfo.getProdSeries"
                        data-display-field="seriesName" data-value-field="seriesCode"
        />
      </k-form-item>
      <k-form-item label="开放类型">
        <k-field-select v-model="prodSearchParam.prodMode"
                        data-dict="t8_prod_mode"/>
      </k-form-item>
      <k-form-item label="产品类型">
        <k-field-select v-model="prodSearchParam.prodClassify"
                        data-dict="t8_prod_classify"/>
      </k-form-item>
      <k-form-item label="销售对象">
        <k-field-select v-model="prodSearchParam.prodSaleCustom" data-dict="t8_prod_sale_custom"/>
      </k-form-item>
      <k-form-item label="募集方式">
        <k-field-select v-model="prodSearchParam.raiseType" data-dict="t8_raise_type"/>
      </k-form-item>
      <k-form-item label="分红方式">
        <k-field-select v-model="prodSearchParam.bonusType" data-dict="t8_bonus_type"/>
      </k-form-item>
      <k-form-item label="风险等级">
        <k-field-select v-model="prodSearchParam.prodRiskLevel" data-dict="prod_risk_level"/>
      </k-form-item>
      <k-form-item label="开放频率单位">
        <k-field-select v-model="prodSearchParam.cycleOpenType" :data-data="openRuleOptions"/>
      </k-form-item>
      <k-form-item label="开放频率">
        <k-field-text v-model="prodSearchParam.cycleOpenTerm" data-validate-type="int"/>
      </k-form-item>
      <k-form-item label="代码回收">
        <k-field-select v-model="prodSearchParam.isRecycleCode" data-dict="1yes0no"></k-field-select>
      </k-form-item>
      <k-btn slot="button" class="md-rose" data-functype="EXPORT" data-target="taCustodianBankGrid"
             :data-export-name="'产品台账'">
        <md-icon>cloud_download</md-icon>
        导出
      </k-btn>
    </k-form-search-customize>

    <k-grid
      ref="taCustodianBankGrid"
      data-action='T8ProdStandBook.findStandBook1'
      @data-row-select="selectRow"
      data-operate-column-position="end"
      data-operate-width="200px">
      <k-grid-column data-header="id" data-name="id" data-hidden="true"></k-grid-column>
      <k-grid-column data-header="产品经理" data-name="prodManageName"></k-grid-column>
      <k-grid-column data-header="产品系列" data-name="seriesName"></k-grid-column>
      <k-grid-column data-header="产品代码" data-name="prodCode"></k-grid-column>
      <k-grid-column data-header="产品名称" data-name="prodName" data-width="140px"></k-grid-column>
      <k-grid-column data-header="产品登记编码  " data-name="registCode" data-width="130px"></k-grid-column>
      <k-grid-column data-header="成立日" data-name="establishDate" data-type="date" data-width="90px"></k-grid-column>
      <k-grid-column data-header="产品到期日" data-name="endDate" data-type="date" data-width="90px"></k-grid-column>
      <k-grid-column data-header="开放类型" data-name="prodMode" data-dict="t8_prod_mode"></k-grid-column>
      <k-grid-column data-header="产品期限" data-name="productTerm"></k-grid-column>
      <k-grid-column data-header="开放频率" data-name="cycleOpenTerm"></k-grid-column>
      <k-grid-column data-header="最近申购起始日" data-name="subStartDate" data-type="date" data-width="110px"></k-grid-column>
      <k-grid-column data-header="最近赎回结束日" data-name="subEndDate" data-type="date" data-width="110px"></k-grid-column>
      <k-grid-column data-header="开放日" data-name="openDate" data-type="date" data-width="90px"></k-grid-column>
      <k-grid-column data-header="产品类型" data-name="prodClassify" data-dict="t8_prod_classify"></k-grid-column>
      <k-grid-column data-header="风险等级" data-name="prodRiskLevel" data-dict="prod_risk_level"></k-grid-column>
      <k-grid-column data-header="募集方式" data-name="raiseType" data-dict="t8_raise_type"></k-grid-column>
      <k-grid-column data-header="销售对象" data-name="prodSaleCustom" data-width="100px"></k-grid-column>
      <k-grid-column data-header="个人起点金额" data-name="minSubsPerson" data-width="90px"></k-grid-column>
      <k-grid-column data-header="机构起点金额" data-name="minSubsMechanism" data-width="90px"></k-grid-column>
      <k-grid-column data-header="业绩比较基准" data-name="baseRate"></k-grid-column>
      <k-grid-column data-header="产品市值" data-name="marketValue" data-width="90px"></k-grid-column>
      <k-grid-column data-header="主要投向" data-name="investDirection"></k-grid-column>
      <k-grid-column data-header="产品特点" data-name="prodTrait"></k-grid-column>
      <!-- <k-grid-column data-header="估值方式" data-name="liquidateType" data-dict="t8_prod_tn"></k-grid-column> -->
      <k-grid-column data-header="清算日期" data-name="liquidate" data-type="date"></k-grid-column>
      <k-grid-column data-header="估值方法" data-name="valuationMethod" data-dict="valuation_method"></k-grid-column>
      <k-grid-column data-header="分红方式" data-name="bonusType" data-dict="t8_bonus_type"></k-grid-column>
      <k-grid-column data-header="分红规律" data-name="bonusFrequency"></k-grid-column>
      <k-grid-column data-header="投资经理" data-name="investManageName"></k-grid-column>
      <template slot="operate" slot-scope="scope">

      <k-btn data-functype="POPUP" data-confirm data-size="mini" class="md-info md-just-icon md-simple" data-descript="修改"
               data-target="editForm" v-if="global.isShowAuthorityButton('T8ProdStandBook.updateStandBook')">
          <md-icon>edit</md-icon>
      </k-btn>

      <k-btn data-functype="PAGE" data-size="mini"  class="md-info md-just-icon md-simple"
               :data-model="scope.row.row" @click="toEditProdInfo(scope.row.row)"
               :prodInfoId="scope.row.row.id" data-descript="产品信息详情">
          <md-icon>library_books</md-icon>

      </k-btn>

      </template>

    </k-grid>
 <k-popup ref="editForm" data-title="修改">
      <k-form ref="taCustodianBankFormInfo" :data-col="2">
         <!-- <k-form-item label="产品系列">
          <k-field-display v-model="formData.seriesName"   />
        </k-form-item> -->
        <k-form-item label="产品代码">
          <k-field-display v-model="formData.prodCode"   />
        </k-form-item>
         <k-form-item label="产品名称">
          <k-field-display v-model="formData.prodName"   />
        </k-form-item>

        <k-form-item label="产品市值">
          <k-field-text v-model="formData.marketValue" data-validate-type="number" data-integer-length="13"
                        data-digits="2"/>
        </k-form-item>
          <k-form-item label="主要投向">
          <k-field-text v-model="formData.investDirection"   />
        </k-form-item>
          <k-form-item label="产品特点">
          <k-field-text v-model="formData.prodTrait"   />
        </k-form-item>
         <!-- <k-form-item label="估值方式">
          <k-field-display v-model="formData.liquidateType"   />
        </k-form-item> -->

        <k-form-footer data-align="center">
           <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdStandBook.updateStandBook"
                 data-from="editForm" :data-model="formData"
                 data-target="taCustodianBankGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>关闭</k-btn>
        </k-form-footer>

      </k-form>
    </k-popup>


  </div>
</template>

<script>
import { assign } from "lodash";
import Tools from "@/utils/tools";

export default {
  data() {
    return {
      prodSearchParam: {
        prodCode: '',//产品代码
        expireStartDate: '', //到期日（从）
        expireEndDate: '',//到期日（到）
        establishStartDate: '',//成立日（从）
        establishEndDate: '',//成立日（到）
        seriesCode: '',//产品系列
        prodMode: '',//开放类型
        prodClassify: '',//产品类型
        prodSaleCustom: '',//销售对象
        isRecycleCode:'0',//是否代码回收
      },
      formData: {},
      dateFormData: {
        liquidate: ''
      },
      delyFormData: {},
      cascaderValue: [],
      selectRowData: {},
      prodEndDate: '',
      currentWorkday: null,
      establishDateRange: [],//成立日区间
      expireDateRange: [],//到期日区间
      openDateRange: [],//开放日区间
      openRuleOptions: [{
        value: '1',
        label: '天'
      }, {
        value: '2',
        label: '周'
      }, {
        value: '3',
        label: '月'
      }],

    };
  },
  methods: {
    toEditProdInfo(row){
      let pathUrl = '/main/pms/M81/prodDisplay/M81001display';
      this.$router.push({
        path: pathUrl,
        query: {prodMode: row.prodMode,prodInfoId:row.id,prodCode: row.prodCode,assemblyMenuType:'3',menuName:'prodStandBook'},
      });

    },
    selectRow(row, column, event) {
      this.selectRowData = assign({}, row)
      this.formData = assign({}, row)
    },
    dataBeforeLoad() {
      return {"excOrgno":"ROOT"}
    },
    updSuccess(pop) {
      this.$refs.taCustodianBankGrid.load()
      pop.close()
    }
  },
   created() {

     this.httpUtil.sysDate().then(res=>{
        if (res) {
          //console.log(res);
         this.prodSearchParam.limitDate = res;
        }
      });
    },
  computed: {
    value() {
      return this.$attrs.value
    },
    queryParam() {
      return {
        'prodName': this.prodSearchParam.prodName,//产品名称
        'prodCode': this.prodSearchParam.prodCode,//产品代码
        'expireStartDate': this.expireDateRange ? this.expireDateRange[0] : null, //到期日（从）
        'expireEndDate': this.expireDateRange ? this.expireDateRange[1] : null,//到期日（到）
        'establishStartDate': this.establishDateRange ? this.establishDateRange[0] : null,//成立日（从）
        'establishEndDate': this.establishDateRange ? this.establishDateRange[1] : null,//成立日（到）
        'seriesCode': this.prodSearchParam.seriesCode,//产品系列
        'prodMode': this.prodSearchParam.prodMode,//开放类型
        'prodClassify': this.prodSearchParam.prodClassify,//产品类型
        'prodSaleCustom': this.prodSearchParam.prodSaleCustom,//销售对象
        'prodManageId': this.prodSearchParam.prodManageId,//产品经理id
        'investManageId': this.prodSearchParam.investManageId,//投资经理id
        'raiseType': this.prodSearchParam.raiseType,//募集方式
        'bonusType': this.prodSearchParam.bonusType,//分红方式
        'prodRiskLevel': this.prodSearchParam.prodRiskLevel,//风险等级
        'cycleOpenType': this.prodSearchParam.cycleOpenType,//开放频率单位
        'cycleOpenTerm': this.prodSearchParam.cycleOpenTerm,//开放频率
        'openStartDate': this.openDateRange ? this.openDateRange[0] : null,//开放开始日期
        'openEndDate': this.openDateRange ? this.openDateRange[1] : null,//开放结束日期
        'isRecycleCode': this.prodSearchParam.isRecycleCode,//是否代码回收
        'registCode': this.prodSearchParam.registCode,//产品登记编码
      }
    }

  }
};
</script>
