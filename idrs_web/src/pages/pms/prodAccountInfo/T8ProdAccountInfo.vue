<template>
  <div>
    <k-form-search-customize data-target="t8prodAccountInfoGrid" v-model="prodSearchParam">
      <k-form-item label="账户类型">
        <k-field-select v-model="prodSearchParam.accountType" data-dict="t8_account_type"/>
      </k-form-item>
      <k-form-item label="资金账户名称">
        <k-field-select v-model="prodSearchParam.accountName" data-action="T8ProdAccountInfo.find"
                        data-display-field="accountName" data-value-field="accountName"/>
      </k-form-item>
      <k-form-item label="资金账号">
        <k-field-select v-model="prodSearchParam.accountCode" data-action="T8ProdAccountInfo.find"
                        data-display-field="accountCode" data-value-field="accountCode"/>
      </k-form-item>
      <k-form-item label="创建起始日">
        <k-field-date v-model="prodSearchParam.crtDateStart" data-type="date"/>
      </k-form-item>
      <k-form-item label="创建结束日">
        <k-field-date v-model="prodSearchParam.crtDateEnd" data-type="date"
                      :data-min-value="prodSearchParam.crtDateStart"/>
      </k-form-item>
      <k-form-item label="账户状态">
        <k-field-select v-model="prodSearchParam.accountStatus" data-dict="t8_account_status"/>
      </k-form-item>
      <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.initFormData()"
             data-target="addPopup" v-show="showCreate"
             v-if="global.isShowAuthorityButton('T8ProdAccountInfo.addAccountInfo')">
        <md-icon md-src="/static/svg/add.svg" />新增
      </k-btn>
    </k-form-search-customize>

    <k-grid ref="t8prodAccountInfoGrid"  data-action="T8ProdAccountInfo.find1" @data-row-select="selectRow" data-operate-width="120px">
      <k-grid-column data-hidden="true" data-header="id" data-name="id" />
      <k-grid-column data-header="账户类型" data-name="accountType"  data-dict="t8_account_type"/>
      <k-grid-column data-header="开户行名称" data-name="openAccountName" />
      <k-grid-column data-header="资金账户名称" data-name="accountName" />
      <k-grid-column data-header="资金账号" data-name="accountCode" />
      <k-grid-column data-header="大额行号" data-name="bankAccNum" />
      <k-grid-column data-hidden="true" data-header="币种" data-name="cur" />
      <k-grid-column data-hidden="true" data-header="托管场所" data-name="trusteeMarket" data-dict="t8_trustee_market_dvp"/>
      <k-grid-column data-hidden="true" data-header="基金交易对手" data-name="fundOside" />
      <k-grid-column data-hidden="true" data-header="交易账号" data-name="tranAccount" />
      <k-grid-column data-hidden="true" data-header="一码通账号" data-name="eMarkAccountCode" />
      <k-grid-column data-hidden="true" data-header="深交所席位号" data-name="szSeatsCode" />
      <k-grid-column data-hidden="true" data-header="上交所席位号" data-name="shSeatsCode" />
      <k-grid-column data-header="创建日期" data-name="crtDate" data-render="renderDateTimeCreate"/>
      <k-grid-column data-header="创建人名称" data-name="createUserName"/>
      <k-grid-column data-header="账户状态" data-name="accountStatus" data-dict="t8_account_status"/>
      <k-grid-column data-header="备注" data-name="remark" />
      <template slot="operate" slot-scope="scope">

                <k-btn class="md-info md-just-icon md-simple" data-descript="启用" data-functype="SUBMIT" :data-disabled="scope.row.row.accountStatus == '1'"
                       data-action="T8ProdAccountInfo.updateStatusOnEnable" :data-confirm="true" data-target="t8prodAccountInfoGrid"
                       data-size="mini" v-show="showEnable" v-if="global.isShowAuthorityButton('T8ProdAccountInfo.enableOrNot')">
                  <md-icon>lock_open</md-icon>
                </k-btn>
                <k-btn class="md-info md-just-icon md-simple" data-descript="停用" :data-disabled="scope.row.row.accountStatus=='0' || scope.row.row.accountStatus=='2' || scope.row.row.accountStatus=='3'"
                       data-action="T8ProdAccountInfo.updateStatusOnStop" :data-confirm="true"
                       data-target="t8prodAccountInfoGrid" data-functype="SUBMIT" data-size="mini"
                       v-show="showUnable" v-if="global.isShowAuthorityButton('T8ProdAccountInfo.enableOrNot')">
                  <md-icon>lock</md-icon>
                </k-btn>

        <k-btn class="md-info md-just-icon md-simple" data-descript="修改账户信息" data-functype="POPUP" data-size="mini"
               :data-handler="editHandler" data-target="editPopup"
               :data-disabled="scope.row.row.accountStatus != '0' && scope.row.row.accountStatus != '2'"
               v-show="showUpdate" v-if="global.isShowAuthorityButton('T8ProdAccountInfo.updateAccountInfo')">
          <md-icon>edit</md-icon>
        </k-btn>

        <k-btn class="md-info md-just-icon md-simple" data-descript="销户"
               data-action="T8ProdAccountInfo.updateStatusOnLogOut" :data-confirm="true" data-target="t8prodAccountInfoGrid"
               data-functype="SUBMIT"  data-size="mini" v-show="showCancel"
               v-if="global.isShowAuthorityButton('T8ProdAccountInfo.updateStatusOnLogOut')">
          <md-icon>stop</md-icon>
        </k-btn>

        <k-btn class="md-info md-just-icon md-simple" data-descript="关联产品信息" data-functype="POPUP" data-size="mini"
               :data-handler="editHandler" data-target="addProdPopup" v-show="showAssociatProd"
               v-if="global.isShowAuthorityButton('T8ProdAccountInfo.associatedProductInformation')">
          <md-icon>add</md-icon>
        </k-btn>
      </template>
    </k-grid>

    <k-grid ref="prodAccountInfoCorrelationGrid"  :data-autoload="false" data-action="T8ProdAccountInfoCorrelation.findByT8ProdAccountInfoId">
      <k-grid-column data-align="center" data-hidden="true" data-header="账户id" data-name="t8ProdAccountInfoId"/>
      <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode"/>
      <k-grid-column data-align="center" data-header="产品名称" data-name="prodName"/>
      <k-grid-column data-align="center" data-header="账户名称" data-name="accountName"/>
      <template slot="operate"  slot-scope="scope">
        <k-btn data-functype="POPUP" data-confirm data-size="mini"   class="md-info md-just-icon md-simple"
               data-target="uploadPopup" data-descript="上传开户回执"  :data-handler="uploadHandler" v-show="showUpload"
               v-if="global.isShowAuthorityButton('T8ProdAccountInfo.uploadReceipt')">
          <md-icon>cloud_upload</md-icon>
        </k-btn>
        <k-btn data-functype="DOWNLOAD" :data-download-name="scope.row.row.prodName+scope.row.row.accountName+'回执材料.zip'" data-confirm data-size="mini"
               class="md-info md-just-icon md-simple"
               data-target="t8prodAccountInfoGrid"
               data-url="/download/server/PmsApp/t8ProdAccountInfo/downloadT8AccountInfo.json"
               data-descript="下载开户回执" v-model="scope.row.row" v-show="showDownload">
          <md-icon>cloud_download</md-icon>
        </k-btn>
        <k-btn data-functype="SUBMIT" data-size="mini" class="md-danger md-just-icon md-simple" data-descript="删除关联"
               data-target="prodAccountInfoCorrelationGrid"  data-action="T8ProdAccountInfo.deleteT8ProdAccountInfo"
               data-confirm data-type="danger" v-show="showDeleteAsscociation" v-if="global.isShowAuthorityButton('T8ProdAccountInfo.deleteT8ProdAccountInfo')">
          <md-icon>close</md-icon>
        </k-btn>
      </template>
    </k-grid>


    <k-popup ref="addProdPopup" data-title="添加产品关联信息">
      <k-form  ref="addProdForm"  :data-col="2">
        <k-form-item label="账户类型">
          <k-field-select v-model="editFormData.accountType" :data-allowblank="false"
                          data-dict="t8_account_type" :data-disabled="true"/>
        </k-form-item>
          <k-form-item label="托管行名称" v-if="this.editFormData.accountType == '1' || this.editFormData.accountType == '2'">
          <k-field-select v-model="editFormData.t8TruteeInfoId" data-action="T82006.findTaCustodianBanks"
                          data-display-field="truteeName"  data-value-field="id" @data-on-change="setOpenAccountNameAdd"
                          :data-allowblank="!(this.editFormData.accountType == '1' || this.editFormData.accountType == '2')"/>
        </k-form-item>
        <k-form-item label="开户行名称" :data-col="2"
                     v-if="this.editFormData.accountType == '1' || this.editFormData.accountType == '2'|| this.editFormData.accountType == '3'|| this.editFormData.accountType == '4'|| this.editFormData.accountType == '8'|| this.editFormData.accountType == '9'">
          <k-field-text v-model="editFormData.openAccountName" :data-max-length="128" :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="交易账户名称" :data-col="2" v-if=" this.editFormData.accountType == '5'">
          <k-field-text v-model="editFormData.accountName" :data-max-length="128" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="证券账号名称" :data-col="2" v-if=" this.editFormData.accountType == '7'">
          <k-field-text v-model="editFormData.accountName" :data-max-length="128" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="资金账户名称" :data-col="2"
                     v-if="this.editFormData.accountType == '1'|| this.editFormData.accountType == '2'|| this.editFormData.accountType == '3'|| this.editFormData.accountType == '4'|| this.editFormData.accountType == '6'|| this.editFormData.accountType == '8'|| this.editFormData.accountType == '9'">
          <k-field-text v-model="editFormData.accountName" :data-max-length="128" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="券商名称" :data-col="2" v-if="this.editFormData.accountType == '6' ">
          <k-field-text v-model="editFormData.brokerName" :data-max-length="128"
                        :data-allowblank="!(this.editFormData.accountType == '6')"/>
        </k-form-item>
         <k-form-item label="基金账号"  v-if="this.editFormData.accountType == '5'">
          <k-field-text v-model="editFormData.accountCode"   data-validate-type="int" data-type="int" :data-max-lenght="32" :data-allowblank="false"/>
        </k-form-item>
         <k-form-item label="证券账号号码"  v-if="this.editFormData.accountType == '7'">
          <k-field-text v-model="editFormData.accountCode"   data-validate-type="int" data-type="int" :data-max-lenght="32" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="资金账号"  v-if="this.editFormData.accountType == '1'|| this.editFormData.accountType == '2'|| this.editFormData.accountType == '3'|| this.editFormData.accountType == '4'|| this.editFormData.accountType == '6'|| this.editFormData.accountType == '8'|| this.editFormData.accountType == '9'">
          <k-field-text v-model="editFormData.accountCode"   data-validate-type="int" data-type="int" :data-max-lenght="32" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="大额行号" v-if="this.editFormData.accountType == '1' || this.editFormData.accountType == '2'|| this.editFormData.accountType == '3'|| this.editFormData.accountType == '4'|| this.editFormData.accountType == '8'|| this.editFormData.accountType == '9'">
          <k-field-text v-model="editFormData.bankAccNum" :data-max-length="32" data-validate-type="int" data-type="int" />
        </k-form-item>
        <k-form-item label="产品代码" v-if="this.editFormData.accountType == '1' " :data-col="2">
          <k-field-select v-model="editFormData.prodCodes" data-action="T8Dict.findNotEstablishProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode" :data-multiple="true"
                          :dataAllowblank="!(this.editFormData.accountType == '1')"/>
        </k-form-item>
        <k-form-item label="产品代码" v-if="this.editFormData.accountType == '2'" :data-col="2">
          <k-field-select v-model="editFormData.prodCodes" data-action="T8Dict.findNotEstablishProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode"
                          :dataAllowblank="!(this.editFormData.accountType == '2')"/>
        </k-form-item>
        <k-form-item label="产品代码" v-if="this.editFormData.accountType != '1' && this.editFormData.accountType != '2'"
                     :data-col="2">
          <k-field-select v-model="editFormData.prodCodes" data-action="T8Dict.findNotEstablishProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode" :data-multiple="true"
                          :dataAllowblank="!(this.editFormData.accountType == '1' || this.editFormData.accountType == '2')"/>
        </k-form-item>

        <k-form-item label="币种" v-if="this.editFormData.accountType == '2'">
          <k-field-select v-model="editFormData.cur" data-dict="t8_prod_currtype" data-default-value="156"/>
        </k-form-item>
        <k-form-item label="托管场所" v-if="this.editFormData.accountType == '3' || this.editFormData.accountType == '4'">
          <k-field-select v-model="editFormData.trusteeMarket" data-dict="t8_trustee_market_dvp" :data-allowblank="!(this.editFormData.accountType == '3' || this.editFormData.accountType == '4')"/>
        </k-form-item>
        <k-form-item label="托管场所" v-if="this.editFormData.accountType == '7'">
          <k-field-select v-model="editFormData.trusteeMarket" data-dict="t8_trustee_market_exchange" :data-allowblank="!(this.editFormData.accountType == '7')"/>
        </k-form-item>
        <k-form-item label="一码通账号" v-if="this.editFormData.accountType == '7'">
          <k-field-text v-model="editFormData.oneYardPassAccountCode" data-min-value="0" data-validate-type="number" data-type="number" data-digits="0" :data-allowblank="!(this.editFormData.accountType == '7')"/>
        </k-form-item>
        <k-form-item label="基金公司名称" v-if="this.editFormData.accountType == '5'">
          <k-field-text v-model="editFormData.fundOside" :data-max-length="128" :data-allowblank="!(this.editFormData.accountType == '5')"/>
        </k-form-item>
        <k-form-item label="交易账号" v-if="this.editFormData.accountType == '5'">
          <k-field-text v-model="editFormData.tranAccount" data-validate-type="number" data-type="number" :data-max-lenght="32" data-digits="0" :data-allowblank="!(this.editFormData.accountType == '5')"/>
        </k-form-item>
        <k-form-item label="深交所席位号" v-if="this.editFormData.accountType == '6'">
          <k-field-text v-model="editFormData.szSeatsCode" data-validate-type="number" data-type="number"  data-digits="0"/>
        </k-form-item>
        <k-form-item label="上交所席位号" v-if="this.editFormData.accountType == '6'" >
          <k-field-text v-model="editFormData.shSeatsCode" data-validate-type="number" data-type="number" data-digits="0"/>
        </k-form-item>
        <k-form-item label="备注" >
          <k-field-text v-model="editFormData.remark"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn data-target="prodAccountInfoCorrelationGrid" data-functype="SUBMIT" class="btn-custom-primary" :data-model="editFormData"
                 data-action="T8ProdAccountInfo.updateAccountInfo" data-from="addProdForm"
                  >
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn  data-functype="CLOSE" class="btn-custom-plain">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>




    <k-popup ref="addPopup" data-title="添加账户信息">
      <k-form  ref="addForm"  :data-col="2">
        <k-form-item label="账户类型" key="accountType_add">
          <k-field-select v-model="formData.accountType" :data-allowblank="false"
                          @data-on-change="changeAccountType"
                          data-display-field="itemval" data-value-field="itemkey"
                          data-dict="t8_account_type"/>
        </k-form-item>
        <k-form-item label="托管行名称" v-if="this.formData.accountType == '1' || this.formData.accountType == '2'" key="t8TruteeInfoId_add">
          <k-field-select v-model="formData.t8TruteeInfoId" data-action="T82006.findTaCustodianBanks"
                          data-display-field="truteeName"  data-value-field="id" @data-on-change="setOpenAccountNameAdd"
                          :data-allowblank="!(this.formData.accountType == '1' || this.formData.accountType == '2')"/>
        </k-form-item>
        <k-form-item label="开户行名称" :data-col="2"
                     v-if="this.formData.accountType == '1' || this.formData.accountType == '2'|| this.formData.accountType == '3'|| this.formData.accountType == '4'|| this.formData.accountType == '8'|| this.formData.accountType == '9'">
          <k-field-text v-model="formData.openAccountName" :data-max-length="128" :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="交易账户名称" :data-col="2" v-if=" this.formData.accountType == '5'">
          <k-field-text v-model="formData.accountName" :data-max-length="128" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="证券账号名称" :data-col="2" v-if=" this.formData.accountType == '7'">
          <k-field-text v-model="formData.accountName" :data-max-length="128" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="资金账户名称" :data-col="2"
                     v-if="this.formData.accountType == '1'|| this.formData.accountType == '2'|| this.formData.accountType == '3'|| this.formData.accountType == '4'|| this.formData.accountType == '6'|| this.formData.accountType == '8'|| this.formData.accountType == '9'">
          <k-field-text v-model="formData.accountName" :data-max-length="128" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="券商名称" :data-col="2" v-if="this.formData.accountType == '6' ">
          <k-field-text v-model="formData.brokerName" :data-max-length="128"
                        :data-allowblank="!(this.formData.accountType == '6')"/>
        </k-form-item>
         <k-form-item label="基金账号"  v-if="this.formData.accountType == '5'">
          <k-field-text v-model="formData.accountCode"   data-validate-type="int" data-type="int" :data-max-lenght="32" :data-allowblank="false"/>
        </k-form-item>
         <k-form-item label="证券账号号码"  v-if="this.formData.accountType == '7'">
          <k-field-text v-model="formData.accountCode"   data-validate-type="int" data-type="int" :data-max-lenght="32" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="资金账号"  v-if="this.formData.accountType == '1'|| this.formData.accountType == '2'|| this.formData.accountType == '3'|| this.formData.accountType == '4'|| this.formData.accountType == '6'|| this.formData.accountType == '8'|| this.formData.accountType == '9'">
          <k-field-text v-model="formData.accountCode"   data-validate-type="int" data-type="int" :data-max-lenght="32" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="大额行号" v-if="this.formData.accountType == '1' || this.formData.accountType == '2'|| this.formData.accountType == '3'|| this.formData.accountType == '4'|| this.formData.accountType == '8'|| this.formData.accountType == '9'">
          <k-field-text v-model="formData.bankAccNum" :data-max-length="32" data-validate-type="int" data-type="int" />
        </k-form-item>
        <k-form-item label="产品代码" v-if="this.formData.accountType == '1' " :data-col="2" key="prodCode_add1">
          <k-field-select v-model="formData.prodCodes" data-action="T8Dict.findNotEstablishProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode" :data-multiple="true"
                          :dataAllowblank="!(this.formData.accountType == '1')"/>
        </k-form-item>
        <k-form-item label="产品代码" v-if="this.formData.accountType == '2'" :data-col="2" key="prodCode_add2">
          <k-field-select v-model="formData.prodCodes" data-action="T8Dict.findNotEstablishProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode"
                          :dataAllowblank="!(this.formData.accountType == '2')"/>
        </k-form-item>
        <k-form-item label="产品代码" :data-col="2"
                     v-if="this.formData.accountType != '1' && this.formData.accountType != '2'&& this.formData.accountType != ''" key="prodCode_add3">
          <k-field-select v-model="formData.prodCodes" data-action="T8Dict.findNotEstablishProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode" :data-multiple="true"
                          :dataAllowblank="!(this.formData.accountType == '1' || this.formData.accountType == '2')"/>
        </k-form-item>

        <k-form-item label="币种" v-if="this.formData.accountType == '2'" key="cur_add">
          <k-field-select v-model="formData.cur" data-dict="t8_prod_currtype" data-default-value="156"/>
        </k-form-item>
        <k-form-item label="托管场所" v-if="this.formData.accountType == '3' || this.formData.accountType == '4'" key="trusteeMarket_add">
          <k-field-select v-model="formData.trusteeMarket" data-dict="t8_trustee_market_dvp" :data-allowblank="!(this.formData.accountType == '3' || this.formData.accountType == '4')"/>
        </k-form-item>
        <k-form-item label="托管场所" v-if="this.formData.accountType == '7'" key="trusteeMarket1_add">
          <k-field-select v-model="formData.trusteeMarket" data-dict="t8_trustee_market_exchange" :data-allowblank="!(this.formData.accountType == '7')"/>
        </k-form-item>
        <k-form-item label="一码通账号" v-if="this.formData.accountType == '7'">
          <k-field-text v-model="formData.oneYardPassAccountCode" data-min-value="0" data-validate-type="number" data-type="number" data-digits="0" :data-allowblank="!(this.formData.accountType == '7')"/>
        </k-form-item>
        <k-form-item label="基金公司名称" v-if="this.formData.accountType == '5'" :data-col="2">
          <k-field-text v-model="formData.fundOside" :data-max-length="128"
                        :data-allowblank="!(this.formData.accountType == '5')"/>
        </k-form-item>
        <k-form-item label="交易账号" v-if="this.formData.accountType == '5'">
          <k-field-text v-model="formData.tranAccount" data-validate-type="number" data-type="number" :data-max-lenght="32" data-digits="0" :data-allowblank="!(this.formData.accountType == '5')"/>
        </k-form-item>
        <k-form-item label="深交所席位号" v-if="this.formData.accountType == '6'">
          <k-field-text v-model="formData.szSeatsCode" data-validate-type="number" data-type="number"  data-digits="0"/>
        </k-form-item>
        <k-form-item label="上交所席位号" v-if="this.formData.accountType == '6'" >
          <k-field-text v-model="formData.shSeatsCode" data-validate-type="number" data-type="number" data-digits="0"/>
        </k-form-item>
        <k-form-item label="备注" >
          <k-field-text v-model="formData.remark"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn data-target="t8prodAccountInfoGrid" data-functype="SUBMIT" class="btn-custom-primary" :data-model="formData"
                  data-from="addForm" :data-handler="checkValue">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn  data-functype="CLOSE" class="btn-custom-plain">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="editPopup" data-title="修改账户信息">
      <k-form  ref="editForm"  :data-col="2" key="accountType_update">
         <k-form-item label="账户类型">
          <k-field-select v-model="editFormData.accountType" :data-allowblank="false"
                          @data-on-change="changeAccountType"
                          data-display-field="itemval" data-value-field="itemkey"
                          data-dict="t8_account_type" data-disabled="true"/>
        </k-form-item>
        <k-form-item label="托管行名称" v-if="this.editFormData.accountType == '1' || this.editFormData.accountType == '2'" key="t8TruteeInfoId_add">
          <k-field-select v-model="editFormData.t8TruteeInfoId" data-action="T82006.findTaCustodianBanks"
                          data-display-field="truteeName"  data-value-field="id" @data-on-change="setOpenAccountNameAdd(editFormData.t8TruteeInfoId,'update')"
                          :data-allowblank="!(this.editFormData.accountType == '1' || this.editFormData.accountType == '2')"/>
        </k-form-item>
        <k-form-item label="开户行名称" :data-col="2"
                     v-if="this.editFormData.accountType == '1' || this.editFormData.accountType == '2'|| this.editFormData.accountType == '3'|| this.editFormData.accountType == '4'|| this.editFormData.accountType == '8'|| this.editFormData.accountType == '9'">
          <k-field-text v-model="editFormData.openAccountName" :data-max-length="128" :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="交易账户名称" :data-col="2" v-if=" this.editFormData.accountType == '5'">
          <k-field-text v-model="editFormData.accountName" :data-max-length="128" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="证券账号名称" :data-col="2" v-if=" this.editFormData.accountType == '7'">
          <k-field-text v-model="editFormData.accountName" :data-max-length="128" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="资金账户名称" :data-col="2"
                     v-if="this.editFormData.accountType == '1'|| this.editFormData.accountType == '2'|| this.editFormData.accountType == '3'|| this.editFormData.accountType == '4'|| this.editFormData.accountType == '6'|| this.editFormData.accountType == '8'|| this.editFormData.accountType == '9'">
          <k-field-text v-model="editFormData.accountName" :data-max-length="128" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="券商名称" :data-col="2" v-if="this.editFormData.accountType == '6' ">
          <k-field-text v-model="editFormData.brokerName" :data-max-length="128"
                        :data-allowblank="!(this.editFormData.accountType == '6')"/>
        </k-form-item>
         <k-form-item label="基金账号"  v-if="this.editFormData.accountType == '5'">
          <k-field-text v-model="editFormData.accountCode"   data-validate-type="int" data-type="int" :data-max-lenght="32" :data-allowblank="false"/>
        </k-form-item>
         <k-form-item label="证券账号号码"  v-if="this.editFormData.accountType == '7'">
          <k-field-text v-model="editFormData.accountCode"   data-validate-type="int" data-type="int" :data-max-lenght="32" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="资金账号"  v-if="this.editFormData.accountType == '1'|| this.editFormData.accountType == '2'|| this.editFormData.accountType == '3'|| this.editFormData.accountType == '4'|| this.editFormData.accountType == '6'|| this.editFormData.accountType == '8'|| this.editFormData.accountType == '9'">
          <k-field-text v-model="editFormData.accountCode"   data-validate-type="int" data-type="int" :data-max-lenght="32" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="大额行号" v-if="this.editFormData.accountType == '1' || this.editFormData.accountType == '2'|| this.editFormData.accountType == '3'|| this.editFormData.accountType == '4'|| this.editFormData.accountType == '8'|| this.editFormData.accountType == '9'">
          <k-field-text v-model="editFormData.bankAccNum" :data-max-length="32" data-validate-type="int" data-type="int" />
        </k-form-item>
        <k-form-item label="产品代码" v-if="this.editFormData.accountType == '1' " :data-col="2" key="prodCode_update1">
          <k-field-select v-model="editFormData.prodCodes" data-action="T8Dict.findNotEstablishProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode" :data-multiple="true"
                          :dataAllowblank="!(this.editFormData.accountType == '1')"/>
        </k-form-item>
        <k-form-item label="产品代码" v-if="this.editFormData.accountType == '2'" :data-col="2" key="prodCode_update2">
          <k-field-select v-model="editFormData.prodCodes" data-action="T8Dict.findNotEstablishProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode"
                          :dataAllowblank="!(this.editFormData.accountType == '2')"/>
        </k-form-item>
        <k-form-item label="产品代码" :data-col="2"
                     v-if="this.editFormData.accountType != '1' && this.editFormData.accountType != '2'&& this.formData.accountType != ''" key="prodCode_update3">
          <k-field-select v-model="editFormData.prodCodes" data-action="T8Dict.findNotEstablishProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode" :data-multiple="true"
                          :dataAllowblank="!(this.editFormData.accountType == '1' || this.editFormData.accountType == '2')"/>
        </k-form-item>

        <k-form-item label="币种" v-if="this.editFormData.accountType == '2'" key="cur_update">
          <k-field-select v-model="editFormData.cur" data-dict="t8_prod_currtype" />
        </k-form-item>
        <k-form-item label="托管场所" v-if="this.editFormData.accountType == '3' || this.editFormData.accountType == '4'" key="trusteeMarket_update">
          <k-field-select v-model="editFormData.trusteeMarket" data-dict="t8_trustee_market_dvp" :data-allowblank="!(this.editFormData.accountType == '3' || this.editFormData.accountType == '4')"/>
        </k-form-item>
        <k-form-item label="托管场所" v-if="this.editFormData.accountType == '7'" key="trusteeMarket1_update">
          <k-field-select v-model="editFormData.trusteeMarket" data-dict="t8_trustee_market_exchange" :data-allowblank="!(this.editFormData.accountType == '7')"/>
        </k-form-item>
        <k-form-item label="一码通账号" v-if="this.editFormData.accountType == '7'">
          <k-field-text v-model="editFormData.oneYardPassAccountCode" data-min-value="0" data-validate-type="number" data-type="number" data-digits="0" :data-allowblank="!(this.editFormData.accountType == '7')"/>
        </k-form-item>
        <k-form-item label="基金公司名称" v-if="this.editFormData.accountType == '5'" :data-col="2">
          <k-field-text v-model="editFormData.fundOside" :data-max-length="128"
                        :data-allowblank="!(this.editFormData.accountType == '5')"/>
        </k-form-item>
        <k-form-item label="交易账号" v-if="this.editFormData.accountType == '5'">
          <k-field-text v-model="editFormData.tranAccount" data-validate-type="number" data-type="number" :data-max-lenght="32" data-digits="0" :data-allowblank="!(this.editFormData.accountType == '5')"/>
        </k-form-item>
        <k-form-item label="深交所席位号" v-if="this.editFormData.accountType == '6'">
          <k-field-text v-model="editFormData.szSeatsCode" data-validate-type="number" data-type="number"  data-digits="0"/>
        </k-form-item>
        <k-form-item label="上交所席位号" v-if="this.editFormData.accountType == '6'" >
          <k-field-text v-model="editFormData.shSeatsCode" data-validate-type="number" data-type="number" data-digits="0"/>
        </k-form-item>
        <k-form-item label="备注" >
          <k-field-text v-model="editFormData.remark"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn data-target="t8prodAccountInfoGrid" data-functype="SUBMIT" class="btn-custom-primary" :data-model="editFormData"
                  data-from="editForm" :data-handler="checkValue2">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn  data-functype="CLOSE" class="btn-custom-plain">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="uploadPopup" data-title="上传开户回执">
      <k-form  ref="uploadForm" data-ui="element" >
        <k-form-item style="display:none" label="产品代码">
          <k-field-text v-model="uploadData.prodCode" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item style="display:none" label="账户id">
          <k-field-text v-model="uploadData.t8ProdAccountInfoId" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="uploadRef" :data-multiple="true" :data-limit=10
                          :data-error="onSubmitError" :dataChange="onUploadChange"
                          :dataHttpRequest="httpRequest"
                          :data-auto-upload="false">
          </k-field-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-target="t8ProdPayBackGrid" ref="submitBtn"
                 data-from="uploadForm" :data-model="uploadData" @click="submit">
            <span v-show="showSubmitBtn">确定</span>
            <i v-show="!showSubmitBtn" class="el-icon-loading"/>
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
  </div>
</template>

<script>
  import Tools from "../../../utils/tools";
  import MD5 from "@/frame/md5";
  import httpUtil from "@/frame/httpUtil";
  export default {
    name: "T8ProdAccountInfo",
    data() {
      return {
        prodSearchParam:{
          accountType:'',
          crtDateStart:'',
          crtDateEnd:'',
          accountStatus:''
        },
        formData:{
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
          remark:'',
        },
        editFormData:{
          accountType:'',
          openAccountName:'',
          accountName:'',
          brokerName:'',
          accountCode:'',
          bankAccNum:'',
          prodCodes:'',
          t8TruteeInfoId:'',
          cur:'',
          trusteeMarket:'',
          prodCode:'',
          t8ProdAccountInfoId:'',
          oneYardPassAccountCode:'',
          fundOside:'',
          tranAccount:'',
          szSeatsCode:'',
          shSeatsCode:'',
          remark:''
        },
        uploadData:{
          prodCode:'',
          t8ProdAccountInfoId:'',
        },
        fileList:[],
        fileData:'',
        showSubmitBtn:true,
        showCreate:true,//是否显示新增按钮
        showUpdate:true,//是否显示修改按钮
        showEnable:true,//是否显示启用按钮
        showUnable:true,//是否显示停用按钮
        showCancel:true,//是否显示销户按钮
        showAssociatProd:true,//是否显示关联产品按钮
        showUpload:true,//是否显示上传开户回执按钮
        showDownload:true,//是否显示下载开户回执按钮
        showDeleteAsscociation:true,//是否显示删除关联按钮
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
      checkValue(val) {
        //console.log("data=>>>>>>",this.formData.bankAccNum);
        var flag = this.$refs.addForm.validate()
        //验证表单
        if (!flag) {
          return false;
        }
        if (this.editFormData.bankAccNum != null && this.editFormData.bankAccNum != '' && this.editFormData.bankAccNum != undefined) {
          if (this.formData.bankAccNum.indexOf("-") > -1) {
            Tools.alert("大额行号不能包含特殊字符!", "danger");
            return false;
          }
        }
        if (this.editFormData.accountCode != '' && this.editFormData.accountCode != null && this.editFormData.accountCode != undefined) {
          if (this.formData.accountCode.indexOf("-") > -1) {
            Tools.alert("资金账户不能包含特殊字符!", "danger");
            return false;
          }
        }

        //新增时检查是否已经存在相同类型的账户
        this.httpUtil.comnQuery({
          action: 'T8ProdAccountInfo.findT8ProdAccountInfoType',
          params: val
        }).then(data => {
         if (data.returnmsg) {
           Tools.alert("产品 " + data.returnmsg + "已存在相同类型的账户,不能进行新增", "danger");
         } else {
           //进行新增账户信息
           this.httpUtil.comnUpdate({
             action: "T8ProdAccountInfo.addAccountInfo",
             params: val
           }).then(res => {
             if (res.success) {
               this.$refs.addPopup.close();
             }
           });
         }
        });
        return false;
      },
      checkValue2(val){
        var flag = this.$refs.editForm.validate()
        //验证表单
        if (!flag) {
          return false;
        }
        //console.log("data=>>>>>>",this.formData.bankAccNum);
        if(this.editFormData.bankAccNum!=null&&this.editFormData.bankAccNum!=''&&this.editFormData.bankAccNum!=undefined){
          if(this.editFormData.bankAccNum.indexOf("-")>-1){
            Tools.alert("大额行号不能包含特殊字符!","danger");
            return false;
          }
        }
        if(this.editFormData.accountCode!=''&&this.editFormData.accountCode!=null&&this.editFormData.accountCode!=undefined){
          if(this.editFormData.accountCode.indexOf("-")>-1){
            Tools.alert("资金账户不能包含特殊字符!","danger");
            return false;
          }
        }

        //修改时检查是否已经存在相同类型的账户
        this.httpUtil.comnQuery({
          action: 'T8ProdAccountInfo.findT8ProdAccountInfoTypeOnUpdate',
          params: val
        }).then(data => {
          if (data.returnmsg) {
            Tools.alert("产品 " + data.returnmsg + "已存在相同类型的账户,不能进行修改", "danger");
          } else {
            //进行修改账户信息
            this.httpUtil.comnUpdate({
              action: "T8ProdAccountInfo.updateAccountInfo",
              params: val
            }).then(res => {
              if (res.success) {
                this.$refs.editPopup.close();
              }
            });
          }
        });
        return false;
      },
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row);
        this.editFormData  =Object.assign({}, row)
        this.$refs.prodAccountInfoCorrelationGrid.load({t8ProdAccountInfoId:this.selectRowData.id});
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
      isOnLogOut(row){

        // this.httpUtil.comnQuery({
        //   action: 'T8ProdPrice.findProdPerformanceInfo',
        //   params: {
        //     prodCode: this.editFormData.id,
        //   }
        // }).then(data => {
        //   this.$nextTick(()=>{
        //     if (data.rows.length>0){
        //       this.editFormData.prodCodes = data.rows[0].prodCodes;
        //     }
        //   })
        // });
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

      setOpenAccountNameEdit(value){
        this.httpUtil.comnQuery({
          action: 'T82006.findTaCustodianBanks',
          params: {
            id: value,
          }
        }).then(data => {
          this.$nextTick(()=>{
            if (data.rows!=null && data.rows.length>0){
              this.editFormData.openAccountName = data.rows[0].truteeName;
            }
          })
        });
      },

      changeAccountType(value){
        this.formData.openAccountName ='';
        this.formData.accountName ='';
        this.formData.accountCode ='';
        this.formData.bankAccNum ='';
        this.formData.prodCodes ='';
        this.formData.t8TruteeInfoId ='';
        if (value=='1'||value=='2'){
          this.formData.cur = '156';
        }
        this.formData.trusteeMarket ='';
        this.formData.oneYardPassAccountCode ='';
        this.formData.fundOside ='';
        this.formData.tranAccount ='';
        this.formData.trusteeMarket ='';
        this.formData.szSeatsCode ='';
        this.formData.shSeatsCode ='';
        this.formData.remark ='';
      },
      editHandler(value){
        this.editFormData = value;
        this.httpUtil.comnQuery({
          action: 'T8ProdAccountInfoCorrelation.findProdCodesByT8ProdAccountInfoId',
          params: {
            t8ProdAccountInfoId: this.editFormData.id,
          }
        }).then(data => {
          this.$nextTick(()=>{
            if (data.rows!=null && data.rows.length>0){
              this.editFormData.prodCodes = data.rows[0].prodCodes;
            }
          })
        });
      },

      uploadHandler(value){
        this.uploadData = value;
      },
      onSubmitError() {
        this.$refs.uploadRef.doReset();
        this.showSubmitBtn = true;
      },
      onSubmitSuccess() {
        this.$refs.uploadRef.doReset();
        this.$refs.uploadForm.reset();
        this.$refs.uploadPopup.close();
      },
      onUploadChange(file,fileList){
        this.fileList = fileList;
      },
      httpRequest(file){
        this.fileData.append('files', file.file);
      },
      submit(){
        let uploadData = this.uploadData;
        this.showSubmitBtn = false;
        this.fileData = new FormData();
        this.$refs.uploadRef.upload();
        this.fileData.append('params', JSON.stringify(uploadData));
        this.httpUtil.upload({
          url:"/upload-files/server/PmsApp/t8ProdAccountInfo/uploadReceipt.json",
          formData: this.fileData
        }).then(res=>{
          if (res.status==200){
            Tools.alert(res.data.returnmsg);
            this.onSubmitSuccess();
          }else {
            Tools.alert(res.data.returnmsg,"danger");
          }
        })
      },
    }
  }
</script>

<style scoped>

</style>
