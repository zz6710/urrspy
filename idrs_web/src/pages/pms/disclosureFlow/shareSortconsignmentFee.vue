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
          <k-btn class="btn-custom-plain" data-functype="PAGE" data-target="/main/pms/disclosureFlow/consignmentFee">
            返回
          </k-btn>
        </div>
      </md-card>
    </template>
    <div>
      <k-grid ref="t8ConsignmentFeeGrid"
              data-action="T8ConsignmentFee.findT8ProdShareSort"
              :data-before-load="beforePopupLoad"
              @data-row-select="selectPrintTemp"
              data-operate-column="true"
              :data-display="false">
        <k-grid-column data-header="主键id" data-name="id" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode"></k-grid-column>
        <k-grid-column data-align="center" data-header="产品名称" data-name="prodName" data-width="280"></k-grid-column>
        <k-grid-column data-align="center" data-header="计费起始日期(含)" data-name="feeStartDate" data-type="date"></k-grid-column>
        <k-grid-column data-align="center" data-header="计费结束日期(含)" data-name="feeEndDate" data-type="date"></k-grid-column>
        <k-grid-column data-align="center" data-header="实付光大银行代销费(零售)(元)" data-name="gdFeeRetail" data-width="200"></k-grid-column>
        <k-grid-column data-align="center" data-header="实付光大银行代销费(对公)(元)" data-name="gdFeeCompany" data-width="200"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改代销费" data-functype="POPUP" data-size="mini"
                 data-target="editT8ConsignmentFeePopup" v-if="global.isShowAuthorityButton('T8ConsignmentFee.updateT8ConsignmentFee')">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT"
                 data-action="T8ConsignmentFee.deleteT8ConsignmentFee" data-size="mini"
                 data-type="danger" data-target="t8ConsignmentFeeGrid"
                 v-if="global.isShowAuthorityButton('T8ConsignmentFee.deleteT8ConsignmentFee')"
                 :data-confirm="true" data-descript="删除代销费">
            <md-icon>close</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    修改代销费实体类弹出框   -->
    <k-popup ref="editT8ConsignmentFeePopup" data-title="修改" data-width="60%">
      <k-form ref="editT8ConsignmentFeeForm" :data-col="2" dataLabelWidth="200px">
        <k-form-item label="主键id" v-show="false">
          <k-field-text v-model="formData.id"/>
        </k-form-item>
        <k-form-item label="产品代码">
          <k-field-text v-model="formData.prodCode" data-display-field="prodCode,prodName"
                        :data-disabled="true"
                        data-value-field="prodCode" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="计费起始日期(含)">
          <k-field-date v-model="formData.feeStartDate" :data-allowblank="false" data-date-format="yyyy-MM-dd"/>
        </k-form-item>
        <k-form-item label="计费结束日期(含)">
          <k-field-date v-model="formData.feeEndDate" :data-allowblank="false" data-date-format="yyyy-MM-dd"/>
        </k-form-item>
        <k-form-item label="实付光大银行代销费(零售)(元)">
          <k-field-text v-model="formData.gdFeeRetail" :data-regx="'^[0-9]+(\\.[0-9]{2})?$'"
                        data-regx-text="请输入正确的数字" :data-max-length="17"
                        data-digits="2"  data-integer-length="14"
                        data-validate-type="number" data-type="number"/>
        </k-form-item>
        <k-form-item label="实付光大银行代销费(对公)(元)">
          <k-field-text v-model="formData.gdFeeCompany" :data-regx="'^[0-9]+(\\.[0-9]{2})?$'"
                        data-regx-text="请输入正确的数字" :data-max-length="17"
                        data-digits="2"  data-integer-length="14"
                        data-validate-type="number" data-type="number"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ConsignmentFee.updateT8ConsignmentFee" data-from="editT8ConsignmentFeeForm"
                 :data-model="formData" data-target="t8ConsignmentFeeGrid" :data-handler="compareDate">
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
  name: "shareSortconsignmentFee",
  components: {KFieldExcelUpload},
  data() {
    return {
      formData: {},
      prodCode:'',
      feeEndDate:'',
      feeStartDate:''
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
      this.$refs.t8ConsignmentFeeGrid.load({prodCode: row.prodCode,feeStartDate:row.feeStartDate,feeEndDate:row.feeEndDate});
    },
    beforePopupLoad(params) {
      params.prodCode = this.$route.query.prodCode;
      params.feeStartDate = this.$route.query.feeStartDate;
      params.feeEndDate = this.$route.query.feeEndDate;
      return params;
    },
    compareDate(){
      if(this.formData.feeStartDate>this.formData.feeEndDate){
        Tools.alert("起始日期不能大于结束日期","danger");
        return false;
      }
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
    //console.log("sdsd");
    //console.log(this.$route.query.prodCode);
    this.$nextTick(() => {
      this.$refs.t8ConsignmentFeeGrid.load({'prodCode':this.$route.query.prodCode,'feeStartDate':this.$route.query.feeStartDate,'feeEndDate':this.$route.query.feeEndDate})
    });
  },
};
</script>
