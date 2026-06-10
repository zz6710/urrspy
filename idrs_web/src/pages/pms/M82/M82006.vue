<template>
  <div>
    <k-form-search-customize data-target="taCustodianBankGrid" v-model="queryParam">
      <k-form-item label="托管行">
        <k-field-select v-model="queryParam.truteeCode"  data-action="T82006.findTaCustodianBanksAll"
                        data-display-field="truteeCode,truteeName" data-value-field="truteeCode" ></k-field-select>
      </k-form-item>
      <k-form-item label="是否境外">
        <k-field-select v-model="queryParam.isOutside" data-dict="t8_prod_isok"></k-field-select>
      </k-form-item>
      <k-btn slot="button"  class="btn-custom-primary" data-functype="POPUP"
             data-target="addCustodianBankPopup" :data-handler="addFrom"
             v-if="global.isShowAuthorityButton('T82006.addTaCustodianBank')">
        <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
    </k-form-search-customize>

    <k-grid ref="taCustodianBankGrid"  data-action='T82006.findTaCustodianBanksAll1' @data-row-select="selectRow"
      data-operate-column-position="end" data-operate-width="300px">
      <k-grid-column data-hidden="true" data-header="id" data-name="id" :dataMaxLength="32"></k-grid-column>
      <k-grid-column data-header="唯一标识" data-name="rowid" :dataMaxLength="128"></k-grid-column>
      <k-grid-column data-header="托管行代码" data-name="truteeCode" :dataMaxLength="32"></k-grid-column>
      <k-grid-column data-header="托管行名称" data-name="truteeName" :dataMaxLength="128"></k-grid-column>
      <k-grid-column data-header="是否境外" data-name="isOutside" :dataMaxLength="2" data-dict="t8_prod_isok"></k-grid-column>
      <k-grid-column data-header="托管行国别" data-name="truteeNation" :dataMaxLength="255"  data-dict="t8_nation_code" data-display-field="itemkey,itemval"></k-grid-column>
      <k-grid-column data-header="联系人姓名" data-name="connector" :dataMaxLength="128"></k-grid-column>
      <k-grid-column data-header="联系人电话" data-name="mobile"  :dataMaxLength="20"></k-grid-column>
      <k-grid-column data-header="托管行地址" data-name="address" :dataMaxLength="256"></k-grid-column>
      <k-grid-column data-header="邮编" data-name="postalCode" :dataMaxLength="20"></k-grid-column>
      <k-grid-column data-header="邮箱地址" data-name="emailAddress" :dataMaxLength="255"></k-grid-column>
      <k-grid-column data-header="传真号码" data-name="fax" :dataMaxLength="20"></k-grid-column>
      <k-grid-column data-header="备注" data-name="remark" :dataMaxLength="255"></k-grid-column>
      <k-grid-column data-header="信披邮箱地址" data-name="email" :dataMaxLength="256"></k-grid-column>
      <template slot="operate">
        <k-btn data-functype="POPUP" data-confirm data-size="mini"   class="md-info md-just-icon md-simple"
               data-target="editTaCustodianBankPopup" data-descript="修改托管行信息" :data-handler="getRowid"
               v-if="global.isShowAuthorityButton('T82006.updateTaCustodianBank')">
          <md-icon>edit</md-icon>
        </k-btn>

        <k-btn data-functype="SUBMIT" data-size="mini" class="md-danger md-just-icon md-simple" data-descript="删除托管行信息"
               data-target="taCustodianBankGrid"  data-action="T82006.deleteTaCustodianBank" data-confirm data-type="danger"
               v-if="global.isShowAuthorityButton('T82006.deleteTaCustodianBank')">
          <md-icon>close</md-icon>
        </k-btn>

      </template>
    </k-grid>

    <!--    添加机构弹出框   -->
    <k-popup ref="addCustodianBankPopup" data-title="新增" :data-dialog-drag="true">
      <k-form ref="addCustodianBankForm" :data-col="3">

        <k-form-item label="是否境外">
          <k-field-select v-model="formData.isOutside" data-dict="t8_prod_isok" :dataAllowblank="false" @data-on-change="getRowid"/>
        </k-form-item>

        <k-form-item label="标识符" v-show="formData.isOutside=='0'">
          <k-field-text v-model="formData.rowid"  :dataAllowblank="formData.isOutside!='0'" :data-max-length="32"
                        data-placeholder="‘99’为其他托管行"/>
        </k-form-item>

        <k-form-item label="托管行代码">
          <k-field-text v-model="formData.truteeCode"  :dataAllowblank="false" :data-max-length="32" data-validate-type="codeLetter"/>
        </k-form-item>
        <k-form-item label="托管行名称">
          <k-field-text v-model="formData.truteeName" :dataAllowblank="false" :data-max-length="100" data-validate-type="text"/></k-form-item>
       <k-form-item label="境外托管行国别" v-show="formData.isOutside=='1'">
        <k-field-select v-model="formData.truteeNation" :dataAllowblank="formData.isOutside=='0'" data-display-field="itemkey,itemval" data-value-field="itemkey" data-dict="t8_nation_code"/></k-form-item>


        <k-form-item label="联系人姓名">
          <k-field-text v-model="formData.connector" data-validate-type="text" :data-max-length="50"/></k-form-item>
        <k-form-item label="联系人电话">
          <k-field-text v-model="formData.mobile" data-validate-type="telephone" :data-max-length="11"/></k-form-item>
          <k-form-item label="成立日期">
            <k-field-date v-model="formData.dateIncorporation" data-type="date" data-date-format="yyyy-MM-dd"/></k-form-item>
          <k-form-item label="组织形式">
            <k-field-text v-model="formData.orgForm" data-validate-type="text" :data-max-length="100"/></k-form-item>
        <k-form-item label="邮编">
          <k-field-text v-model="formData.postalCode" data-validate-type="text" :data-max-length="100"/></k-form-item>
        <k-form-item label="邮箱地址">
          <k-field-text v-model="formData.emailAddress" data-validate-type="email" :data-max-length="100"/></k-form-item>
        <k-form-item label="传真号码">
          <k-field-text v-model="formData.fax" :data-max-length="20"/></k-form-item>
        <k-form-item label="经营范围" :data-col="3">
          <k-field-text v-model="formData.natureBusiness" inputType="textarea" data-validate-type="text" :data-max-length="2000"/></k-form-item>
        <k-form-item label="托管行地址" :data-col="3">
          <k-field-text v-model="formData.address" inputType="textarea" data-validate-type="text" :data-max-length="100"/></k-form-item>
        <k-form-item label="备注" :data-col="3">
          <k-field-text v-model="formData.remark" inputType="textarea" data-validate-type="text" :data-max-length="100"/></k-form-item>

        <k-form-item :data-col="3">
          <H5>托管行信披邮箱</H5>
        </k-form-item>
        <k-form-item label="信披邮箱地址">
          <k-field-text v-model="formData.email" data-regx="^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$"
                        data-regx-text="请输入正确的邮箱" :data-max-length="256" :dataAllowblank="false"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T82006.addTaCustodianBank"
                 data-from="addCustodianBankForm" :data-model="formData"
                 data-target="taCustodianBankGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>

      </k-form>
    </k-popup>

    <!--    修改机构弹出框   -->
    <k-popup ref="editTaCustodianBankPopup" data-title="修改" :data-dialog-drag="true">
      <k-form ref="editTaCustodianBankForm" :data-col="3">

        <k-form-item label="是否境外">
          <k-field-select v-model="formData.isOutside" data-dict="t8_prod_isok" :dataAllowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="标识符"  v-show="formData.isOutside=='0'">
          <k-field-text v-model="formData.rowid"  :dataAllowblank="formData.isOutside!='0'" :data-max-length="32"
                        data-placeholder="‘99’为其他托管行" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="托管行代码">
          <k-field-text v-model="formData.truteeCode" :dataAllowblank="false" data-validate-type="codeLetter"/>
        </k-form-item>
        <k-form-item label="托管行名称">
          <k-field-text v-model="formData.truteeName" :dataAllowblank="false" :data-max-length="100" data-validate-type="text"/>
        </k-form-item>

        <k-form-item label="境外托管行国别" v-show="formData.isOutside=='1'">
          <k-field-select v-model="formData.truteeNation" data-display-field="itemkey,itemval" data-value-field="itemkey" data-dict="t8_nation_code" :dataAllowblank="formData.isOutside=='0'"/>
        </k-form-item>
        <k-form-item label="联系人姓名">
          <k-field-text v-model="formData.connector" data-validate-type="text" :data-max-length="50"/>
        </k-form-item>
        <k-form-item label="联系人电话">
          <k-field-text v-model="formData.mobile" data-validate-type="telephone" :data-max-length="11"/>
        </k-form-item>
        <k-form-item label="成立日期">
          <k-field-date v-model="formData.dateIncorporation" data-type="date" data-date-format="yyyy-MM-dd"/>
        </k-form-item>
        <k-form-item label="组织形式">
          <k-field-text v-model="formData.orgForm" data-validate-type="text" :data-max-length="100"/>
        </k-form-item>
        <k-form-item label="邮编">
          <k-field-text v-model="formData.postalCode" data-validate-type="text" :data-max-length="100"/>
        </k-form-item>
        <k-form-item label="邮箱地址">
          <k-field-text v-model="formData.emailAddress" data-validate-type="email" :data-max-length="100"/>
        </k-form-item>
        <k-form-item label="传真号码">
          <k-field-text v-model="formData.fax" :data-max-length="20" />
        </k-form-item>
        <k-form-item label="托管行地址" :data-col="3">
          <k-field-text v-model="formData.address" inputType="textarea" data-validate-type="text" :data-max-length="100"/>
        </k-form-item>
        <k-form-item label="经营范围" :data-col="3">
          <k-field-text v-model="formData.natureBusiness" data-validate-type="text" inputType="textarea" :data-max-length="2000"/>
        </k-form-item>
        <k-form-item label="备注" :data-col="3">
          <k-field-text v-model="formData.remark" inputType="textarea" data-validate-type="text" :data-max-length="100"/>
        </k-form-item>

        <k-form-item :data-col="3">
          <H5>托管行信披邮箱</H5>
        </k-form-item>
        <k-form-item label="信披邮箱地址">
          <k-field-text v-model="formData.email" data-regx="^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$"
                        data-regx-text="请输入正确的邮箱" :data-max-length="256" :dataAllowblank="false"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T82006.updateTaCustodianBank"
                 data-from="editTaCustodianBankForm" :data-model="formData" :data-handler="checkInfo"
                 data-target="taCustodianBankGrid">
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
import { assign } from "lodash";

export default {
  data() {
    return {
      queryParam:{},
      formData: {},
      cascaderValue: [],
      selectRowData: {},
      fromRowId:'',
    };
  },




  methods: {

    addFrom(){
      this.formData={};
    },

    getRowid(val){
      if(val == '0'){
        this.httpUtil.comnQuery({
          action: 'T8Dict.findDictTrutee',
          params: {},
        }).then(data => {
          if(data.rows[0]){
            let rowid = data.rows[0].itemkey;
            let a ='';
            if(rowid+0 < 10){
              a = '0' + rowid;
            }else{
              a = '' + rowid;
            }
            this.$set(this.formData,'rowid',a);
            this.fromRowId = a;
          }
        });
      }
    },


    checkInfo(){
      //alert("验证信息!!!");
    },
    selectRow(row, column, event) {
      const _this = this
      _this.selectRowData = assign({}, row)
      _this.formData = assign({}, row)
    },
    dataBeforeLoad() {
      return {"excOrgno":"ROOT"}
    },
    statusRender(row) {
      console.log("statusRender=======>", row)
    },
    updSuccess(pop) {
      this.$refs.taCustodianBankGrid.load()
      pop.close()
    }
  }
};
</script>

<style lang="scss" scoped>
  ::v-deep .el-dialog {
    margin-right: 10%;
  }
</style>
