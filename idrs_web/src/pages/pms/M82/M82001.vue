<template>
  <div>

    <k-form-search-customize data-target="T82001Grid" v-model="queryParam">
      <k-form-item label="销售商名称">
        <k-field-select v-model="queryParam.distributorCode" data-action="T82001.findTaDistributorInfos"
                        data-display-field="distributorCode,distributorName"
                        data-value-field="distributorCode"></k-field-select>
      </k-form-item>
      <k-form-item label="销售商类型">
        <k-field-select v-model="queryParam.distributorType" data-dict="t8_distributor_type"></k-field-select>
      </k-form-item>

      <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.distributorInfo={}"
             data-target="addDistributorInfoPopup"
             v-if="global.isShowAuthorityButton('T82001.addTaDistributorInfo2')">
        <md-icon md-src="/static/svg/add.svg" />新增
      </k-btn>
    </k-form-search-customize>


    <k-grid ref="T82001Grid" data-action="T82001.findTaDistributorInfos1" @data-row-select="selectRow"
            data-operate-column-position="end" @init="(grid)=>{this.$kgrid = grid}"
            data-operate-width="100px">
      <k-grid-column data-header="销售商id" data-name="id" data-hidden="true"></k-grid-column>
      <k-grid-column data-header="销售商代码" data-name="distributorCode"></k-grid-column>
      <k-grid-column data-header="销售商名称" data-name="distributorName"></k-grid-column>
      <k-grid-column data-header="销售商类型" data-name="distributorType" data-display-name="itemval"
                     data-dict="t8_distributor_type"></k-grid-column>
      <k-grid-column data-header="个人渠道所属部门" data-name="managerDept" data-dict="manager_dept"></k-grid-column>
      <k-grid-column data-header="机构渠道所属部门" data-name="orgManageDept" data-dict="manager_dept"></k-grid-column>
      <k-grid-column data-header="同业渠道所属部门" data-name="interManageDept" data-dict="manager_dept"></k-grid-column>
      <k-grid-column data-header="信披邮箱地址" data-name="email"></k-grid-column>
      <!--<k-grid-column data-header="流程状态" data-name="processStatus"  data-dict="process_status" ></k-grid-column>-->

      <!--:data-disabled="scope.row.row.processStatus != '9' && scope.row.row.processStatus != '3'" -->
      <template slot="operate" slot-scope="scope">
        <k-btn data-functype="POPUP" data-confirm data-size="mini" class="md-info md-just-icon md-simple"
               data-target="editDistributorInfoPopup" data-descript="修改销售商信息"
               v-if="global.isShowAuthorityButton('T82001.updateTaDistributorInfo2')">
          <md-icon>edit</md-icon>
        </k-btn>

        <k-btn data-functype="SUBMIT" data-size="mini" class="md-danger md-just-icon md-simple" data-descript="删除销售商信息"
               data-target="T82001Grid" data-action="T82001.deleteTaDistributorInfo" data-confirm data-type="danger"
               v-if="global.isShowAuthorityButton('T82001.deleteTaDistributorInfo')">
          <md-icon>close</md-icon>
        </k-btn>
      </template>
    </k-grid>

    <k-popup ref="addDistributorInfoPopup" data-title="添加销售商信息" :dataDialogDrag="true">
      <k-form ref="distributorInfoForm" :data-col="2" dataLabelWidth="170px" dataInputWidth="250px">
        <k-form-item label="销售商代码">
          <k-field-text v-model="distributorInfo.distributorCode" :data-max-length="32" :dataAllowblank="false"
                        data-validate-type="codeLetter"/>
        </k-form-item>
        <k-form-item label="销售商名称">
          <k-field-text v-model="distributorInfo.distributorName" :data-max-length="64" :dataAllowblank="false"/>
        </k-form-item>
        <k-form-item label="销售商类型">
          <k-field-select v-model="distributorInfo.distributorType" data-dict="t8_distributor_type"
                          :dataAllowblank="false"/>
        </k-form-item>
        <k-form-item label="个人渠道所属部门">
          <k-field-select v-model="distributorInfo.managerDept" data-dict="manager_dept"/>
        </k-form-item>
        <k-form-item label="机构渠道所属部门">
          <k-field-select v-model="distributorInfo.orgManageDept" data-dict="manager_dept"/>
        </k-form-item>
        <k-form-item label="同业渠道所属部门">
          <k-field-select v-model="distributorInfo.interManageDept" data-dict="manager_dept"/>
        </k-form-item>
        <k-form-item label="统一社会信用代码">
          <k-field-text v-model="distributorInfo.nLegalCode" data-validate-type="codeNumber" :data-max-length="18"
                        :data-min-length="18"/>
        </k-form-item>
        <k-form-item label="法人代表证件类型">
          <k-field-select v-model="distributorInfo.nLegalType" data-dict="id_type_p"/>
        </k-form-item>
        <k-form-item label="法人代表证件号码">
          <k-field-text v-model="distributorInfo.nLegalIdCode" :data-max-length="32" data-validate-type="codeLetter"/>
        </k-form-item>

        <k-form-item label="业务联系人姓名">
          <k-field-text v-model="distributorInfo.busiConnector" :data-max-length="64"/>
        </k-form-item>
        <k-form-item label="业务联系人电话">
          <k-field-text v-model="distributorInfo.busiConnectorMobile" :data-max-length="11"
                        data-validate-type="telephone"/>
        </k-form-item>
        <!--<k-form-item label="邮件地址">
          <k-field-text v-model="distributorInfo.email" data-validate-type="email" :data-max-length="64"/>
        </k-form-item>-->
        <k-form-item label="邮编">
          <k-field-text v-model="distributorInfo.postcode" :data-max-length="6" data-validate-type="postcode"/>
        </k-form-item>
        <k-form-item label="传真号码">
          <k-field-text v-model="distributorInfo.fax" :data-max-length="20" data-validate-type="code"/>
        </k-form-item>
        <k-form-item label="销售商官网">
          <k-field-text v-model="distributorInfo.officialWebsite" :data-max-length="128"/>
        </k-form-item>
        <k-form-item label="客服热线">
          <k-field-text v-model="distributorInfo.customerServiceHotline" :data-max-length="20"/>
        </k-form-item>
        <k-form-item label="销售商地址" :data-col="2">
          <k-field-text v-model="distributorInfo.address" inputType="textarea" :data-max-length="128"/>
        </k-form-item>
        <k-form-item label="主要职责" :data-col="2">
          <k-field-text v-model="distributorInfo.mainDuty" inputType="textarea" :data-max-length="2000"/>
        </k-form-item>
        <k-form-item label="备注" :data-col="2">
          <k-field-text v-model="distributorInfo.remark" inputType="textarea" :data-max-length="128"/>
        </k-form-item>

        <k-form-item :data-col="2">
          <H5 style="margin-left: 50px">销售商信披邮箱</H5>
        </k-form-item>
        <k-form-item label="信披邮箱地址">
          <k-field-text v-model="distributorInfo.email" data-regx="^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$"
                        data-regx-text="请输入正确的邮箱" :data-max-length="64"
                        :dataAllowblank="true"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" ref="distributorSaveButton" data-functype="SUBMIT"
                 @click="saveDistributorInfo(distributorInfo)" data-from="distributorInfoForm"
                 :data-after-success="afterSuccess" :data-model="distributorInfo" data-target="T82001Grid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
    <k-popup ref="editDistributorInfoPopup" data-title="修改销售商信息" :dataDialogDrag="true">
      <k-form ref="editDistributorInfoForm" :data-col="2" dataLabelWidth="170px" dataInputWidth="250px">
        <k-form-item label="销售商代码">
          <k-field-text v-model="distributorInfo.distributorCode" :data-max-length="32" :dataAllowblank="false"
                        data-validate-type="codeLetter"/>
        </k-form-item>
        <k-form-item label="销售商名称">
          <k-field-text v-model="distributorInfo.distributorName" :data-max-length="64" :dataAllowblank="false"/>
        </k-form-item>
        <k-form-item label="销售商类型">
          <k-field-select v-model="distributorInfo.distributorType" data-dict="t8_distributor_type"
                          :dataAllowblank="false"/>
        </k-form-item>
        <k-form-item label="个人渠道所属部门">
          <k-field-select v-model="distributorInfo.managerDept" data-dict="manager_dept"/>
        </k-form-item>
        <k-form-item label="机构渠道所属部门">
          <k-field-select v-model="distributorInfo.orgManageDept" data-dict="manager_dept"/>
        </k-form-item>
        <k-form-item label="同业渠道所属部门">
          <k-field-select v-model="distributorInfo.interManageDept" data-dict="manager_dept"/>
        </k-form-item>
        <k-form-item label="统一社会信用代码">
          <k-field-text v-model="distributorInfo.nLegalCode" data-validate-type="codeNumber" :data-max-length="18"
                        :data-min-length="18"/>
        </k-form-item>
        <k-form-item label="法人代表证件类型">
          <k-field-select v-model="distributorInfo.nLegalType" data-dict="id_type_p"/>
        </k-form-item>
        <k-form-item label="法人代表证件号码">
          <k-field-text v-model="distributorInfo.nLegalIdCode" :data-max-length="32" data-validate-type="codeLetter"/>
        </k-form-item>

        <k-form-item label="业务联系人姓名">
          <k-field-text v-model="distributorInfo.busiConnector" :data-max-length="64"/>
        </k-form-item>
        <k-form-item label="业务联系人电话">
          <k-field-text v-model="distributorInfo.busiConnectorMobile" :data-max-length="11"
                        data-validate-type="telephone"/>
        </k-form-item>
        <!--<k-form-item label="邮件地址">
          <k-field-text v-model="distributorInfo.email" data-validate-type="email" :data-max-length="64"/>
        </k-form-item>-->
        <k-form-item label="邮编">
          <k-field-text v-model="distributorInfo.postcode" :data-max-length="6" data-validate-type="postcode"/>
        </k-form-item>
        <k-form-item label="传真号码">
          <k-field-text v-model="distributorInfo.fax" :data-max-length="20" data-validate-type="code"/>
        </k-form-item>
        <k-form-item label="销售商官网">
          <k-field-text v-model="distributorInfo.officialWebsite" :data-max-length="128"/>
        </k-form-item>
        <k-form-item label="客服热线">
          <k-field-text v-model="distributorInfo.customerServiceHotline" :data-max-length="20"/>
        </k-form-item>
        <k-form-item label="销售商地址" :data-col="2">
          <k-field-text v-model="distributorInfo.address" inputType="textarea" :data-max-length="128"/>
        </k-form-item>
        <k-form-item label="主要职责" :data-col="2">
          <k-field-text v-model="distributorInfo.mainDuty" inputType="textarea" :data-max-length="2000"/>
        </k-form-item>
        <k-form-item label="备注" :data-col="2">
          <k-field-text v-model="distributorInfo.remark" inputType="textarea" :data-max-length="128"/>
        </k-form-item>

        <k-form-item :data-col="2">
          <H5 style="margin-left: 50px">销售商信披邮箱</H5>
        </k-form-item>
        <k-form-item label="信披邮箱地址">
          <k-field-text v-model="distributorInfo.email" data-regx="^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$"
                        data-regx-text="请输入正确的邮箱" :data-max-length="256"
                        :dataAllowblank="true"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" ref="distributorUpdateButton"
                 @click="updateDistributorInfo(distributorInfo)" data-from="editDistributorInfoForm"
                 :data-after-success="afterSuccess" :data-model="distributorInfo" data-target="T82001Grid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

  </div>
</template>
<script>
import {assign} from "lodash";
import Tools from "@/utils/tools";

export default {
  name: "M82001",
  data() {
    return {
      queryParam: {},
      formData: {},
      distributorInfo: {},
      cascaderValue: [],
      selectRowData: {},
      $kgrid: null
    };
  },
  //
  watch: {
    'distributorInfo.distributorType'(value) {
      if (value != '') {
        if (value == '1' || value == '2') {//传统代销、互联网代销  全部默认开放平台部2
          this.$set(this.distributorInfo, 'managerDept', '2');
          this.$set(this.distributorInfo, 'orgManageDept', '2');
          this.$set(this.distributorInfo, 'interManageDept', '2');
        } else if (value == '3') {//母行代销  个人默认产品市场部3  同业和机构默认解决方案部1
          this.$set(this.distributorInfo, 'managerDept', '3');
          this.$set(this.distributorInfo, 'orgManageDept', '1');
          this.$set(this.distributorInfo, 'interManageDept', '1');
        }
      }
    }
  },
  methods: {
    afterSuccess() {
      this.prodCreateInfo = {};
    },
    selectRow(row, column, event) {
      const _this = this
      _this.selectRowData = assign({}, row)
      _this.distributorInfo = assign({}, row)
    },

    dataBeforeLoad() {
      return {"excOrgno": "ROOT"}
    },
    statusRender(row) {
      // console.log("statusRender=======>", row)
    },
    updSuccess(pop) {
      this.$refs.T82001Grid.load()
      pop.close()
    },
    //停用销售商按钮事件 根据销售商状态判断是否停用
    stopDistributor(params) {
      if (params.status == "1") {
        return false;
      } else {
        return true;
      }
    },

    //启用销售商按钮事件 根据销售商状态判断是否启用
    recDistributor(params) {
      if (params.status == "1") {
        return true;
      } else {
        return false;
      }
    },
    saveDistributorInfo(rows) {
      let result = this.$refs.distributorInfoForm.validate();
      if(result===true) {
        var _this = this;
        //代码唯一性校验
        this.httpUtil.comnQuery({
          action: "T82001.checkAddTaDistributorInfo",
          params: {distributorCode:rows.distributorCode},
        }).then(data => {
          console.log(data.rows.length == 0);
          if(data.rows.length == 0){
            //进行新增操作
            this.httpUtil.comnUpdate({
              action: 'T82001.addTaDistributorInfo2',
              params: rows,
              successAlert: true
            }).then(data => {
              //this.saveLoading = false;
              //销售商信息保存成功后再发起一个更新流程
              if (data.success == true) {
                /* this.httpUtil.comnUpdate({
                 action: 'T82001.approveaddTaDistributorInfo',
                 params: {distributor_code: rows.distributorCode,distributor_name: rows.distributorName},
               }).then(data=>{
                 if(data.success == true||data.status == 200){
                   _this.$refs.addDistributorInfoPopup.close();
                   _this.$refs.T82001Grid.load();
                 }
               });*/
                _this.$refs.addDistributorInfoPopup.close();
                _this.$refs.T82001Grid.load();
              } else {
                _this.$refs.distributorSaveButton.setIconStyle(1, [])
              }
            })
          } else {
            this.$refs.distributorSaveButton.setIconStyle(1, [])
            Tools.alert("销售机构代码已存在","danger")
          }
        }).catch({})
      };
    },
    updateDistributorInfo(rows) {
      let result = this.$refs.editDistributorInfoForm.validate();
      if(result===true){
      var _this = this;
      //代码唯一性校验
        this.httpUtil.comnQuery({
          action: "T82001.checkAddTaDistributorInfo",
          params: {distributorCode:rows.distributorCode,id:rows.id},
        }).then(data => {
          console.log(data.rows.length == 0);
          if(data.rows.length == 0){
            //进行更新操作
            this.httpUtil.comnUpdate({
              action: 'T82001.updateTaDistributorInfo2',
              params: rows,
              successAlert: true
            }).then(data => {
              //this.saveLoading = false;
              //销售商信息保存成功后再发起一个更新流程
              if (data.success == true) {
                /*this.httpUtil.comnUpdate({
                  action: 'T82001.approveUpdateTaDistributorInfo',
                  params: {distributor_code: rows.distributorCode,distributor_name: rows.distributorName},
                }).then(data=>{
                  if(data.success == true||data.status == 200){
                    _this.$refs.editDistributorInfoPopup.close();
                    _this.$refs.T82001Grid.load();
                  }
                });*/
                _this.$refs.editDistributorInfoPopup.close();
                _this.$refs.T82001Grid.load();

              } else {
                _this.$refs.distributorUpdateButton.setIconStyle(1, [])
              }
            })
          } else {
            this.$refs.distributorUpdateButton.setIconStyle(1, [])
            Tools.alert("销售机构代码已存在","danger")
          }
        }).catch({})
      };
      /*this.httpUtil.comnUpdate({
        action: 'T82001.updateTaDistributorInfo2',
        params: {rows},
      }).then(data=>{
        if(data.success == true||data.status == 200){
          _this.$refs.editDistributorInfoPopup.close();
          _this.$refs.T82001Grid.load();
        }
      });*/
    },
    changeDistributorStatus: function (formData) {
      console.log(formData)
      if (formData.status == '2') {
        this.$confirm('确认要启用销售商?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.httpUtil.comnUpdate({
            action: 'T82001.startTaDistributorInfo',
            params: formData,
            successAlert: true,
          }).then(data => {
            this.$kgrid.load();
          });
        }).catch(error => {
          this.$kgrid.load();
        });
      } else {
        this.$confirm('确认要停用销售商?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.httpUtil.comnUpdate({
            action: 'T82001.stopTaDistributorInfo',
            params: formData,
            successAlert: true,
          }).then(data => {
            this.$kgrid.load();
          });
        }).catch(error => {
          this.$kgrid.load();
        });
      }
    },

  }
}

</script>

<style scoped>
/* .md-switch{
    position: relative;
    display: inline-block;
    margin: auto;
 }*/

</style>
