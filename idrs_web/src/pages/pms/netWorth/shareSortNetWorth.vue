<template>
  <div>
    <template>
      <md-card class="box-card" style="overflow: visible;position: unset">
        <md-card-header class="md-card-header-text md-card-header-green" style="margin-right: 0;">
          <div class="search-header">
            <div class="card-icon" :style="iconStyle">
              <md-icon md-src="/static/svg/form.svg"></md-icon>
            </div>
            <div>
              <i class="el-icon-d-caret" @click="show"></i>
            </div>
          </div>
        </md-card-header>

        <div slot="header" class="clearfix" style="text-align:right">

        </div>
        <div class="show-form" id="show-form">
          <k-form ref="searchForm" :data-col="0">
            <slot></slot>
          </k-form>
        </div>
        <div class="k-form-search-footer">
          <k-btn class="btn-custom-plain" data-functype="PAGE" data-target="/main/pms/netWorth/netWorth">
            返回
          </k-btn>
        </div>
      </md-card>
    </template>
    <div>
      <k-grid ref="shareSortNetWorthGrid"
              data-action="NetWorth.findT8ProdShareSort"
              :data-before-load="beforePopupLoad"
              @data-row-select="selectPrintTemp"
              data-operate-column="true"
              :data-display="false">
        <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode"></k-grid-column>
        <k-grid-column data-align="center" data-header="产品名称" data-name="prodName"></k-grid-column>
        <k-grid-column data-align="center" data-header="净值日期" data-name="navDate"></k-grid-column>
        <k-grid-column data-align="center" data-header="产品总净值" data-name="totalNet"></k-grid-column>
        <k-grid-column data-align="center" data-header="产品总份额" data-name="totalVol"></k-grid-column>
        <k-grid-column data-align="center" data-header="当日收益" data-name="navProfit"></k-grid-column>
        <k-grid-column data-align="center" data-header="单位净值" data-name="nav"></k-grid-column>
        <k-grid-column data-align="center" data-header="累计净值" data-name="totalNav"></k-grid-column>
        <k-grid-column data-align="center" data-header="单位万份收益" data-name="tenThousandIncomeAmt"></k-grid-column>
        <k-grid-column data-align="center" data-header="近七日年化收益率" data-name="sevenDaysIncomeRate"></k-grid-column>
        <template slot="operate" slot-scope="scope">

          <k-btn class="md-info md-just-icon md-simple" data-descript="修改净值表描述" data-functype="POPUP" data-size="mini"
                 data-target="editnetWorthPopup" v-if="global.isShowAuthorityButton('NetWorth.updatenetWorth')">
            <md-icon>edit</md-icon>
          </k-btn>

          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="NetWorth.deletenetWorth" data-size="mini"
                 data-type="danger" data-target="shareSortNetWorthGrid" :data-confirm="true" data-descript="删除净值表描述">
            <md-icon>close</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    修改净值表描述弹出框    -->
    <k-popup ref="editnetWorthPopup" data-title="修改">
      <k-form ref="editnetWorthForm" :data-col="2">
        <k-form-item label="产品名称">
          <k-field-select v-model="formData.prodCode" data-action="T8Dict.findTaProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="净值日期">
          <k-field-date v-model="formData.navDate" :data-allowblank="false" data-date-format="yyyy-MM-dd" :data-disabled="true"/>
        </k-form-item>

        <k-form-item label="产品总净值">
          <k-field-text v-model="formData.totalNet"
                        data-regx-text="请输入正确的数字" :data-allowblank="false"
                        data-digits="2"  data-integer-length="13"
                        data-validate-type="number" data-type="number"/>
        </k-form-item>
        <k-form-item label="产品总份额">
          <k-field-text v-model="formData.totalVol"
                        data-regx-text="请输入正确的数字" :data-allowblank="false"
                        data-digits="2"  data-integer-length="13"
                        data-validate-type="number" data-type="number"/>
        </k-form-item>
        <k-form-item label="当日收益">
          <k-field-text v-model="formData.navProfit"
                        data-regx-text="请输入正确的数字"
                        data-digits="2"  data-integer-length="13"
                        data-validate-type="number" data-type="number"/>
        </k-form-item>
        <k-form-item label="单位净值">
          <k-field-text v-model="formData.nav"
                        data-regx-text="请输入正确的数字" :data-allowblank="false"
                        data-digits="2"  data-integer-length="13"
                        data-validate-type="number" data-type="number"/>
        </k-form-item>
        <k-form-item label="基金累计净值">
          <k-field-text v-model="formData.totalNav"
                        data-regx-text="请输入正确的数字"
                        data-digits="4"  data-integer-length="11"
                        data-validate-type="number" data-type="number"/>
        </k-form-item>
        <k-form-item label="单位万份收益">
          <k-field-text v-model="formData.tenThousandIncomeAmt"
                        data-regx-text="请输入正确的数字"
                        data-digits="4"  data-integer-length="4"
                        data-validate-type="number" data-type="number"/>
        </k-form-item>
        <k-form-item label="近七日年化收益率">
          <k-field-text v-model="formData.sevenDaysIncomeRate"
                        data-regx-text="请输入正确的数字" :data-max-length="10"
                        data-digits="4"  data-integer-length="4"
                        data-validate-type="number" data-type="number"/>
        </k-form-item>
        <k-form-item label="销售服务费">
          <k-field-text v-model="formData.saleServiceFee"
                        data-regx-text="请输入正确的数字"
                        data-digits="2"  data-integer-length="13"
                        data-validate-type="number" data-type="number"/>
        </k-form-item>

        <k-form-item label="净值增长率">
          <k-field-text v-model="formData.navGrowthRate"
                        data-regx-text="请输入正确的数字"
                        data-digits="6"  data-integer-length="1"
                        data-validate-type="number" data-type="number"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="NetWorth.updatenetWorth" data-from="editnetWorthForm"
                 :data-model="formData" data-target="shareSortNetWorthGrid">
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
import KFieldExcelUpload from '@/components/k-element/k-field-excel-upload/k-field-excel-upload.vue'
import Tools from "@/utils/tools";
import {assign} from "lodash";

export default {
  name: "shareSortNetWorth",
  components: {KFieldExcelUpload},
  data() {
    return {
      formData: {},
      prodCode:''
    };
  },
  computed: {
    iconStyle() {
      let iconStyle = {};
      iconStyle.background = this.$store.state.system.cardBackground
      return iconStyle;
    }
  },
  methods: {
    selectPrintTemp(row, column, event) {
      this.formData = Object.assign({}, row)
    },
    beforePopupLoad(params) {
      params.prodCode = this.$route.query.prodCode;
      params.navDate = this.$route.query.navDate;
      return params;
    },
    show() {
      let e = document.getElementById('show-form')
      if (this.extends) {
        e.style.display = "none"
      } else {
        e.style.display = ""
      }
      this.extends = !this.extends
    },
  },
  activated() {
    this.$nextTick(() => {
      this.$refs.shareSortNetWorthGrid.load({'prodCode':this.$route.query.prodCode,'navDate':this.$route.query.navDate})
    });
  },
};
</script>
