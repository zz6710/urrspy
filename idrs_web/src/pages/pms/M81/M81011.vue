<template>
  <div>

    <k-form-search-customize data-target="customerInfoGrid" v-model="queryParam">
      <k-form-item label="姓名">
        <k-field-select v-model="queryParam.custName"  data-action="T8ProdCustomerInfo.findDict"
                        data-display-field="custName" data-value-field="custName" ></k-field-select>
      </k-form-item>
      <k-form-item label="人物类型">
        <k-field-select v-model="queryParam.custType" data-dict="t8_cust_type"></k-field-select>
      </k-form-item>
      <k-form-item label="证件类型">
        <k-field-select v-model="queryParam.idType" data-dict="t8_id_type" ></k-field-select>
      </k-form-item>

      <k-btn  slot="button"  class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addCustomerInfoPopup"
              v-if="global.isShowAuthorityButton('T8ProdCustomerInfo.addT8ProdInfo')">
      <md-icon md-src="/static/svg/add.svg" />新增
    </k-btn>
    </k-form-search-customize>

    <k-grid ref="customerInfoGrid" data-action='T8ProdCustomerInfo.find1'
            :data-params="{'pgmtype':'2,3'}"
            @data-row-select="selectRow" data-operate-width="100px" >
      <k-grid-column data-header="人员编号" data-width="100" data-name="custNo" data-hidden="true"></k-grid-column>
      <k-grid-column data-header="姓名" data-width="100" data-name="custName"></k-grid-column>
      <k-grid-column data-header="人物类型" data-width="100" data-name="custType" data-dict="t8_cust_type"></k-grid-column>
      <k-grid-column data-header="证件类型" data-width="100" data-name="idType" data-dict="t8_id_type"></k-grid-column>
      <k-grid-column data-header="证件号码" data-name="idCode" data-width="150px"></k-grid-column>
      <k-grid-column data-header="工号" data-width="100" data-name="jobno"></k-grid-column>
      <k-grid-column data-header="手机" data-name="mobile"></k-grid-column>
      <k-grid-column data-header="座机" data-name="homeTel"></k-grid-column>
      <k-grid-column data-header="人员简介" data-name="brief"></k-grid-column>
      <k-grid-column data-header="电子邮箱" data-name="email"></k-grid-column>
<!--      <k-grid-column data-header="创建日期" data-name="crtDate" data-type="date"></k-grid-column>-->
      <k-grid-column data-header="创建时间" data-name="crtTime" data-align="center" data-render="renderDateTimeCreate"></k-grid-column>
<!--      <k-grid-column data-header="更新日期" data-name="updDate" data-type="date"></k-grid-column>-->
      <k-grid-column data-header="更新时间" data-name="updTime" data-align="center" data-render="renderDateTimeUpdate"></k-grid-column>
      <template slot="operate">
        <!-- <k-btn class="md-info md-just-icon md-simple" data-descript="工作日" :data-handler="()=> this.showListWorkDay = true"
          data-functype="POPUP" data-size="mini" data-target="workdayPopup">
          <md-icon md-src="/static/svg/workday.svg" />
        </k-btn> -->
<!--        <k-btn class="md-info md-just-icon md-simple"
               data-functype="PAGE" data-size="mini" data-target="/main/system/workday/Workday"
               :data-handler="()=> this.showListWorkDay = true"
                data-descript="修改工作日">
          <md-icon md-src="/static/svg/workday.svg" />
        </k-btn>-->
        <k-btn class="md-info md-just-icon md-simple" data-descript="修改产品人员信息" data-functype="POPUP" data-size="mini"
               data-target="editCustomerInfoPopup" v-if="global.isShowAuthorityButton('T8ProdCustomerInfo.updateT8ProdInfo')">
          <md-icon>edit</md-icon>
        </k-btn>
        <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="T8ProdCustomerInfo.deleteT8ProdCustomerInfo"
               data-size="mini" data-type="danger" data-target="customerInfoGrid" :data-confirm="true" data-descript="删除产品人员信息"
               v-if="global.isShowAuthorityButton('T8ProdCustomerInfo.deleteT8ProdCustomerInfo')">
          <md-icon>close</md-icon>
        </k-btn>
      </template>
    </k-grid>


    <k-popup ref="editCustomerInfoPopup" data-title="修改">
      <EditCustomerInfo2 v-model="formData" @closeUpdatePopup="closeUpdatePopup" :updSuccess="()=> {
                            this.$refs.editCustomerInfoPopup.close();
                            this.$refs.customerInfoGrid.load()
                          } " />
    </k-popup>


    <k-popup ref="addCustomerInfoPopup" data-title="新增">
      <AddCustomerInfo2 v-model="formData" @closeAddPopup="closeAddPopup" :updSuccess="()=> {
                           this.$refs.addCustomerInfoPopup.close();
                           this.$refs.customerInfoGrid.load()
                         }" />
    </k-popup>

  </div>
</template>

<script>
import kayak from '@/frame/kayak.js'
//import AddCustomerInfo2 from "../../test/test-m81-1011add"
//import EditCustomerInfo2 from "../../test/test-m81-1011edit"
import EditCustomerInfo2 from './M81011edit'
import AddCustomerInfo2 from './M81011add'
import {
  assign
} from "lodash";
import Tools from "@/utils/tools";

export default {
  name:"M81011",
  components: {
     EditCustomerInfo2,
     AddCustomerInfo2,
  },
  data() {
    return {
      queryParam:{},
      formData: {},
      selectRowData: {},
      selectPgmno: '',
      showListWorkDay: false
    };
  },
  methods: {
    renderDateTimeUpdate(row) {
      return Tools.formatDateTime(row.updDate, row.updTime);
    },
    renderDateTimeCreate(row) {
      return Tools.formatDateTime(row.crtDate, row.crtTime);
    },
    selectRow(row, column, event) {
      const _this = this
      _this.selectRowData = assign({}, row)
      _this.formData = assign({}, row)
    },
    closeAddPopup(data) {
      this.$refs.addCustomerInfoPopup.close();
    },
    closeUpdatePopup(val) {
      this.$refs.editCustomerInfoPopup.close();
    }
  }
};
</script>
<style lang="scss" scoped>
::v-deep .el-dialog {
  padding-top: 35px;
}
</style>

