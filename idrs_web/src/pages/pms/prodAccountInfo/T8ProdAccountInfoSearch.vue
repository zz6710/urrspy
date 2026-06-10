<template>
  <div class="py-page">
    <k-form-search-customize data-target="prodAccountInfoSearchGrid" v-model="prodSearchParam">
      <k-form-item label="产品代码">
        <k-field-select v-model="prodSearchParam.prodCd" data-action="ProdAccountInfo.findProdCdAndNm"
                        data-display-field="prodCd,prodNm" data-value-field="prodCd" />
      </k-form-item>
      <k-form-item label="账户类型">
        <k-field-select v-model="prodSearchParam.accountType" data-dict="prod_account_type"/>
      </k-form-item>
      <k-form-item label="账号">
        <k-field-text v-model="prodSearchParam.accountCode"
                        data-display-field="accountCode" data-value-field="accountCode"/>
      </k-form-item>
      <k-form-item label="销售商名称">
        <k-field-text v-model="prodSearchParam.sellerName"
                        data-display-field="sellerName" data-value-field="sellerName"/>
      </k-form-item>
      <k-form-item label="托管行名称">
        <k-field-text v-model="prodSearchParam.trusteeNameSub"
                        data-display-field="trusteeNameSub" data-value-field="trusteeNameSub"/>
      </k-form-item>
      <k-form-item label="开户行名称">
        <k-field-text v-model="prodSearchParam.accountAcntBank"
                        data-display-field="accountAcntBank" data-value-field="accountAcntBank"/>
      </k-form-item>
     
    </k-form-search-customize>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP"  :data-handler="()=>this.initAddFormData()"
             data-target="addPopup" v-if="global.isShowAuthorityButton('ProdAccountInfo.addAccountInfo')">
            <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
        </div>
      </div>
      <k-grid ref="prodAccountInfoSearchGrid"  data-action="ProdAccountInfo.findAccountInfo" @data-row-select="selectRow" data-fixed="right" data-operate-width="200px">
        <k-grid-column data-hidden="true" data-header="id" data-name="id" />
        <k-grid-column data-header="账户类型" data-name="accountType"  data-dict="prod_account_type"/>
        <k-grid-column data-header="账号" data-name="accountCode" />
        <k-grid-column data-header="户名" data-name="accountName" />
        <k-grid-column data-header="产品代码" data-name="prodCode" />
        <k-grid-column data-header="产品名称" data-name="prodNm" />
        <k-grid-column data-header="销售商名称" data-name="sellerName" />
        <k-grid-column data-header="托管行名称" data-name="trusteeNameSub" />
        <k-grid-column data-header="开户行号" data-name="bankAccNum" />
        <k-grid-column data-header="开户行名称" data-name="accountAcntBank" />
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text specialClass" data-descript="账户信息详情" data-functype="POPUP" data-size="mini"
                data-target="detailProdAccountPopup">
            详情
          </k-btn>
          <k-btn class="btn-custom-text specialClass" data-descript="修改账户信息" data-functype="POPUP" data-size="mini"
                data-target="editPopup"
                  v-if="global.isShowAuthorityButton('ProdAccountInfo.updateAccountInfo')">
            修改
          </k-btn>
          <k-btn class="btn-custom-text specialClass" data-functype="SUBMIT" data-action="ProdAccountInfo.deleteAccountInfo" data-size="mini"
                data-type="danger" data-target="prodAccountInfoSearchGrid" :data-confirm="true" data-descript="删除"
                v-if="global.isShowAuthorityButton('ProdAccountInfo.deleteAccountInfo')" >
            删除
          </k-btn>
        </template>
      </k-grid>
    </div>

    <k-popup ref="editPopup" data-title="修改账户信息">
      <k-form  ref="editForm">
         <k-form-item label="账户类型">
          <k-field-select v-model="editFormData.accountType"  data-display-field="itemval" data-value-field="itemkey" data-dict="prod_account_type" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="托管行名称" v-if=" this.editFormData.accountType === '5'">
          <k-field-text v-model="editFormData.trusteeNameSub" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="销售商名称" v-if=" this.editFormData.accountType === '1' ||this.editFormData.accountType === '2' ||this.editFormData.accountType === '7'">
          <k-field-text v-model="editFormData.sellerName"  :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="账号" >
          <k-field-text v-model="editFormData.accountCode" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="户名">
          <k-field-text v-model="editFormData.accountName" :data-max-length="128" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="产品代码" v-if=" this.editFormData.accountType !== '3' ||this.editFormData.accountType !== '8'">
          <k-field-select v-model="editFormData.prodCode" data-action="ProdAccountInfo.findProdCdAndNm"  data-display-field="prodCd" data-value-field="prodCd"
                           @data-on-change="changeProdCode"  :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="产品名称" v-if=" this.editFormData.accountType !== '3' ||this.editFormData.accountType !== '8'">
          <k-field-text v-model="editFormData.prodNm"  :data-allowblank="false"  :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="开户行号" >
          <k-field-text v-model="editFormData.bankAccNum"  :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="开户行名称" >
          <k-field-text v-model="editFormData.accountAcntBank"  :data-allowblank="false" />
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn data-target="prodAccountInfoSearchGrid" data-functype="SUBMIT" class="btn-custom-primary" :data-model="editFormData"
                  data-from="editForm" :data-handler="checkValue">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn  data-functype="CLOSE" class="btn-custom-plain">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="addPopup" data-title="新增账户信息">
      <k-form  ref="addForm">
        <k-form-item label="账户类型">
          <k-field-select v-model="addFormData.accountType"  data-display-field="itemval" data-value-field="itemkey" data-dict="prod_account_type" :data-disabled="false"/>
        </k-form-item>
        <k-form-item label="托管行名称" v-if=" this.addFormData.accountType === '5'">
          <k-field-text v-model="addFormData.trusteeNameSub" :data-disabled="false"/>
        </k-form-item>
        <k-form-item label="销售商名称" v-if=" this.addFormData.accountType === '1' ||this.addFormData.accountType === '2' ||this.addFormData.accountType === '7'">
          <k-field-select v-model="addFormData.distributorCode" data-action="ProdAccountInfo.findSellerCdAndNm" data-display-field="distributorCode,sellerName" data-value-field="distributorCode" :data-disabled="false"/>
        </k-form-item>
        <k-form-item label="账号" >
          <k-field-text v-model="addFormData.accountCode" :data-disabled="false"/>
        </k-form-item>
        <k-form-item label="户名">
          <k-field-text v-model="addFormData.accountName" :data-max-length="128" :data-disabled="false"/>
        </k-form-item>
        <k-form-item label="产品代码" v-if=" this.addFormData.accountType !== '3' ||this.addFormData.accountType !== '8'">
          <k-field-select v-model="addFormData.prodCode" data-action="ProdAccountInfo.findProdCdAndNm"  data-display-field="prodCd" data-value-field="prodCd"
                          @data-on-change="changeAddProdCode"    :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="产品名称" v-if=" this.addFormData.accountType !== '3' ||this.addFormData.accountType !== '8'">
          <k-field-text v-model="addFormData.prodNm"  :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="开户行号" >
          <k-field-text v-model="addFormData.bankAccNum"  :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="开户行名称" >
          <k-field-text v-model="addFormData.accountAcntBank"  :data-allowblank="false" />
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn data-target="prodAccountInfoSearchGrid" data-functype="SUBMIT" class="btn-custom-primary" :data-model="addFormData"
                 data-from="addForm" :data-handler="addCheckValue">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn  data-functype="CLOSE" class="btn-custom-plain">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
    <k-popup ref="detailProdAccountPopup" data-title="账户详情信息">
      <k-form  ref="detailProdAccountForm">
        <k-form-item label="账户类型">
          <k-field-select v-model="editFormData.accountType"  data-display-field="itemval" data-value-field="itemkey" data-dict="prod_account_type" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="托管行名称" v-if=" this.editFormData.accountType === '5'">
          <k-field-text v-model="editFormData.trusteeNameSub" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="销售商名称" v-if=" this.editFormData.accountType === '1' ||this.editFormData.accountType === '2' ||this.editFormData.accountType === '7'">
          <k-field-text v-model="editFormData.sellerName"  :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="账号" >
          <k-field-text v-model="editFormData.accountCode" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="户名">
          <k-field-text v-model="editFormData.accountName" :data-max-length="128" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="产品代码" v-if=" this.editFormData.accountType !== '3' ||this.editFormData.accountType !== '8'">
          <k-field-select v-model="editFormData.prodCode" data-action="ProdAccountInfo.findProdCdAndNm"  data-display-field="prodCd" data-value-field="prodCd"
                          @data-on-change="changeProdCode" :data-disabled="true" />
        </k-form-item>
        <k-form-item label="产品名称" v-if=" this.editFormData.accountType !== '3' ||this.editFormData.accountType !== '8'">
          <k-field-text v-model="editFormData.prodNm" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="开户行号" >
          <k-field-text v-model="editFormData.bankAccNum" :data-disabled="true" />
        </k-form-item>
        <k-form-item label="开户行名称" >
          <k-field-text v-model="editFormData.accountAcntBank" :data-disabled="true" />
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>关闭</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
  </div>
</template>

<script>
  import Tools from "../../../utils/tools";
  import ElementBtn from "@/pages/flowable/plugins/package/penal/btn/elementBtn";
  export default {
    name: "ProdAccountInfo",
    components: {ElementBtn},
    data() {
      return {
        prodSearchParam:{
          prodCd:'',
          accountType:'',
          accountName:'',
          sellerName:'',
          trusteeNameSub:'',
          accountAcntBank:'',
          prod_nm:'',
        },
        formData:{},
        editFormData:{},
        addFormData:{},
      }
    },
    created() {

      this.$nextTick(()=>{
        //获取需要隐藏按钮属性  rennannan 20210322   将接收到的属性值置为false达到隐藏按钮的效果
        this.global.getHideButtons(this);
      });
    },
    methods: {
      renderDateTimeCreate(row) {
        return Tools.formatDateTime(row.crtDate, row.crtTime);
      },
      checkValue(val){
        const flag = this.$refs.editForm.validate();
        //验证表单
        if (!flag) {
          return false;
        }
        if(this.editFormData.bankAccNum!=null&&this.editFormData.bankAccNum!=''&&this.editFormData.bankAccNum!=undefined){
          if(this.editFormData.bankAccNum.indexOf("-")>-1){
            Tools.alert("开户行不能包含特殊字符!","danger");
            return false;
          }
        }

        //修改时检查是否已经存在相同类型的账户
        this.httpUtil.comnQuery({
          action: 'ProdAccountInfo.findProdAccountInfoTypeOnUpdate',
          params: val
        }).then(data => {
          if (data.returnmsg) {
            Tools.alert("产品 " + data.returnmsg + "已存在相同类型的账户,不能进行修改", "danger");
          } else {
            //进行修改账户信息
            this.httpUtil.comnUpdate({
              action: "ProdAccountInfo.updateAccountInfo",
              params: val
            }).then(res => {
              if (res.success) {
                this.$refs.editPopup.close();
                this.$refs.prodAccountInfoSearchGrid.load();
              }
            });
          }
        });
        return false;
      },
      addCheckValue(val){
        const flag = this.$refs.addForm.validate();
        //验证表单
        if (!flag) {
          return false;
        }
        if(this.addFormData.bankAccNum!=null&&this.addFormData.bankAccNum!=''&&this.addFormData.bankAccNum!=undefined){
          if(this.addFormData.bankAccNum.indexOf("-")>-1){
            Tools.alert("开户行不能包含特殊字符!","danger");
            return false;
          }
        }

        //修改时检查是否已经存在相同类型的账户
        this.httpUtil.comnQuery({
          action: 'ProdAccountInfo.findProdAccountInfoTypeOnUpdate',
          params: val
        }).then(data => {
          if (data.returnmsg) {
            Tools.alert("产品 " + data.returnmsg + "已存在相同类型的账户,不能进行修改", "danger");
          } else {
            //进行修改账户信息
            this.httpUtil.comnUpdate({
              action: "ProdAccountInfo.addAccountInfo",
              params: val
            }).then(res => {
              if (res.success) {
                this.$refs.addPopup.close();
                this.$refs.prodAccountInfoSearchGrid.load();
              }
            });
          }
        });
        return false;
      },
      initFormData(){
        this.formData = {
          accountType:'',
          openAccountName:'',
          accountName:'',
          accountCode:'',
          bankAccNum:'',
          prodCodes:'',
          t8TruteeInfoId:'',
          cur:'',
          trusteeMarket:'',
          oneYardPassAccountCode:'',
          fundOside:'',
          tranAccount:'',
          szSeatsCode:'',
          shSeatsCode:'',
          remark:''
        };
      },
      initAddFormData(){
        this.addFormData={};
      },
      setOpenAccountNameAdd(value, oprate){
        this.httpUtil.comnQuery({
          action: 'T82006.findTaCustodianBanks',
          params: {
            id: value,
          }
        }).then(data => {
          this.$nextTick(()=>{
            if (data.rows!=null && data.rows.length>0){
              if (oprate === 'update') {
                this.editFormData.openAccountName = data.rows[0].truteeName;
              } else {
                this.formData.openAccountName = data.rows[0].truteeName;
              }

            }
          })
        });
      },
      changeProdCode(value){
        this.$set(this.editFormData, 'prodNm', null);
        if(this.editFormData.prodCode !="" &&this.editFormData.prodCode!=null &&this.editFormData.prodCode!=undefined) {
          this.httpUtil.comnQuery({
            action: 'ProdAccountInfo.findProdCdAndNm',
            params: {
              prodCd: value,
            }
          }).then(data => {
            this.$set(this.editFormData, 'prodNm', data.rows[0].prodNm);
          });
        }
      },
      changeAddProdCode(value){
        this.$set(this.addFormData, 'prodNm', null);
        if(this.addFormData.prodCode !="" &&this.addFormData.prodCode!=null &&this.addFormData.prodCode!=undefined) {
          this.httpUtil.comnQuery({
            action: 'ProdAccountInfo.findProdCdAndNm',
            params: {
              prodCd: value,
            }
          }).then(data => {
            this.$set(this.addFormData, 'prodNm', data.rows[0].prodNm);
          });
        }
      },
      selectRow(row, column, event) {
        this.editFormData  =Object.assign({}, row)
      },
    },
  }
</script>

<style scoped>
  >>> .el-table__cell {
    padding: 1px 0 !important;
  }
  .specialClass {
    min-width: 40px !important;
  }
  >>> .specialClass > .md-ripple{
    padding: 8px !important;
  }
</style>
