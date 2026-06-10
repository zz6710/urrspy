<template>
    <div>
        <div class="query-div" >
            <k-form ref="ta5002Form" :data-col="3" :data-model="gridQueryData">
                <k-form-item data-input-width="150px">
                    <k-field-text v-model="gridQueryData.acctNo"  data-placeholder="垫资账号"/>
                </k-form-item>
            </k-form>
        </div>

        <div style="min-height:225px;">
          <div class="add-btn-div">
              <div class="add-btn"  @click="addHandler"  :data-disabled="this.distributorCode==''" >+
              </div>
          </div>
               <k-grid id = "T81001Grid"
                 ref="T81001Grid"
                 data-action='T82005.findTaProdCushionCapitalAccts'
                 @data-row-select="selectRow"
                 @init="(grid)=>{this.$kgrid = grid}"
                 data-operate-column-position="end"
                 data-operate-width="300px"
                 :data-autoload="false"
                 >
                 <k-grid-column data-header="销售商代码" data-name="distributorCode" data-width="90"></k-grid-column>
                 <k-grid-column data-header="垫资账号" data-name="acctNo" ></k-grid-column>
                 <k-grid-column data-header="垫资账户名称" data-name="acctName" data-width="110"></k-grid-column>
                 <k-grid-column data-header="垫资账户开户行" data-name="openAcctBank" data-width="130"></k-grid-column>
                 <k-grid-column data-header="理财账户" data-name="taAcctNo" ></k-grid-column>
                 <k-grid-column data-header="理财交易账户" data-name="transAcctNo" data-width="110"></k-grid-column>
                 <k-grid-column data-header="结算币种" data-name="cur" data-dict="currtype" ></k-grid-column>
                 <k-grid-column data-header="银行联行号" data-name="bankNo" data-width="90"></k-grid-column>


                 <template slot="operate">

                 <k-btn data-functype="POPUP" data-confirm data-size="mini"   class="md-info md-just-icon md-simple"
                  data-target="editTaProdCushionCapitalAcctPopup" data-descript="修改垫资户信息" @submit.native.prevent>
                  <md-icon>edit</md-icon>
                 </k-btn>

                 <k-btn data-functype="SUBMIT" data-size="mini" class="md-danger md-just-icon md-simple" data-descript="删除垫资户信息"
                 data-target="T81001Grid" data-action="T82005.deleteTaProdCushionCapitalAcct" data-confirm data-type="danger" >
                 <md-icon>close</md-icon>
                 </k-btn>

                 </template>
               </k-grid>
        </div>

    <!--    添加弹出框   -->
    <k-popup ref="addPopup" data-title="新增" >
      <k-form ref="addForm" :data-col="2">
        <k-form-item label="产品代码" v-show="false">
          <k-field-text v-model="rowData.prodCode"  data-disabled :dataAllowblank="false" :data-max-length="32"
            data-value-field="prodCode" /></k-form-item>
        <k-form-item label="销售商代码">
          <k-field-select v-model="rowData.distributorCode"  data-action="T8Dict.findTaDistributorInfos" :data-max-length="32"
         data-value-field="distributorCode" :dataAllowblank="false"  data-display-field="distributorCode,distributorName"/>
         </k-form-item>
        <k-form-item label="垫资账号">
          <k-field-text v-model="rowData.acctNo" :dataAllowblank="false" :data-max-length="32" data-regx="^[A-Za-z_0-9]*$" data-regx-text="请输入字母、数字"/></k-form-item>
        <k-form-item label="垫资账户名称">
          <k-field-text v-model="rowData.acctName" :dataAllowblank="false" :data-max-length="32"/></k-form-item>
        <k-form-item label="垫资账户开户行">
          <k-field-text v-model="rowData.openAcctBank":dataAllowblank="false" :data-max-length="32"/></k-form-item>
        <k-form-item label="理财账户">
          <k-field-text v-model="rowData.taAcctNo" :dataAllowblank="false" :data-max-length="32" data-regx="^[A-Za-z_0-9]*$" data-regx-text="请输入字母、数字"/></k-form-item>
        <k-form-item label="理财交易账户">
          <k-field-text v-model="rowData.transAcctNo" :dataAllowblank="false" :data-max-length="32" data-regx="^[A-Za-z_0-9]*$" data-regx-text="请输入字母、数字"/></k-form-item>
        <k-form-item label="结算币种">
          <k-field-select v-model="rowData.cur" data-dict="currtype" data-default-value="156"/></k-form-item>
        <k-form-item label="银行联行号">
          <k-field-text v-model="rowData.bankNo"  :data-max-length="32" data-regx="^[A-Za-z_0-9]*$" data-regx-text="请输入字母、数字"/></k-form-item>

         <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T82005.addTaProdCushionCapitalAcct"
                      data-from="addForm" :data-model="rowData" :data-handler="beforeSubmit"
                      data-target="T81001Grid">
                   <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
                   <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
         </k-form-footer>

      </k-form>
    </k-popup>


    <!--    修改弹出框   -->
    <k-popup ref="editTaProdCushionCapitalAcctPopup" data-title="修改">
      <k-form ref="editTaProdCushionCapitalAcctForm" :data-col="2">
        <k-form-item label="产品代码" v-show="false">
          <k-field-text v-model="rowData.prodCode" :data-max-length="32"
                          data-disabled   data-value-field="prodCode" :dataAllowblank="false"/></k-form-item>
        <k-form-item label="销售商代码">
          <k-field-select v-model="rowData.distributorCode"  data-action="T8Dict.findTaDistributorInfos" :data-max-length="32"
                          data-disabled    data-value-field="distributorCode":dataAllowblank="false"  data-display-field="distributorCode,distributorName"/></k-form-item>
        <k-form-item label="垫资账号">
          <k-field-text v-model="rowData.acctNo" :dataAllowblank="false"  :data-max-length="32" data-disabled/></k-form-item>
        <k-form-item label="垫资账户名称">
          <k-field-text v-model="rowData.acctName" :dataAllowblank="true" :data-max-length="32"/></k-form-item>
        <k-form-item label="垫资账户开户行">
          <k-field-text v-model="rowData.openAcctBank":dataAllowblank="false" :data-max-length="32"/></k-form-item>
        <k-form-item label="理财账户">
          <k-field-text v-model="rowData.taAcctNo" :dataAllowblank="false" :data-max-length="32" data-regx="^[A-Za-z_0-9]*$" data-regx-text="请输入字母、数字"/></k-form-item>
        <k-form-item label="理财交易账户">
          <k-field-text v-model="rowData.transAcctNo" :dataAllowblank="false" :data-max-length="32" data-regx="^[A-Za-z_0-9]*$" data-regx-text="请输入字母、数字"/></k-form-item>
        <k-form-item label="结算币种">
          <k-field-select v-model="rowData.cur" data-dict="currtype"/></k-form-item>
        <k-form-item label="银行联行号">
          <k-field-text v-model="rowData.bankNo" :data-max-length="32" data-regx="^[A-Za-z_0-9]*$" data-regx-text="请输入字母、数字"/></k-form-item>

        <k-form-footer data-align="center">
           <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T82005.updateTaProdCushionCapitalAcct"
                     data-from="editTaProdCushionCapitalAcctForm" :data-model="rowData"
                     data-target="T81001Grid">
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
  import kayak from '@/frame/kayak.js'
  import Tools from '@/utils/tools.js';
  import { assign } from "lodash";

  export default {
    props: {
    prodCode:{
        type:String,
        default:'',
    },
    },
    data() {
      return {
        $kgrid : null,
        rowData:{},
        gridQueryData:{},
        distributorCode: null,

      };
    },
    methods: {
      selectRow(row, column, event) {
        const _this = this
        _this.selectRowData = assign({}, row)
         _this.rowData = assign({}, row)
      },
      addHandler(){
        this.rowData={
            prodCode : this.prodCode
        }
        this.$refs.addPopup.popup();
      },
      beforeSubmit(params){
          if(params.prodCode==null || params.prodCode==''){
              Tools.alert("未录入产品代码","danger");
              return false;
          }
          return params;
      },
    },
// 页面加载完成后 给k-grid条件。
    mounted(){
         if(this.prodCode != ''){
             this.gridQueryData.prodCode = this.prodCode;
             this.$kgrid.load(this.gridQueryData);
          }
    },

    watch: {
      prodCode: function(){
        this.gridQueryData.prodCode = this.prodCode;
        this.$kgrid.load(this.gridQueryData);
      },
       'gridQueryData.acctNo'(){
          this.$kgrid.load(this.gridQueryData);
        },

    },

  };
</script>


<style lang="scss" scoped>
 ::v-deep #T81001Grid > div:first-child{
    box-shadow: none;
 }

 ::v-deep #T81001Grid > div:first-child > div:first-child{
    display: none;
 }

 .query-div{
     margin-bottom: -36px;
 }

 ::v-deep .query-div .k-form-body{
    margin-left: auto;
 }

 .add-btn-div{
   margin: -83px 0 63px 108px;
   position: relative;
   z-index: 1;
 }


 .add-btn{
  background-color: #4caf50;
  border-radius: 20px;
  box-shadow: 0 4px 5px 0 rgba(76,175,80,0.14), 0 1px 10px 0 rgba(76,175,80,0.12), 0 2px 4px -1px rgba(76,175,80,0.2);
  width: 20px;
  height: 20px;
  line-height: 20.5px;
  font-size: 23px;
  font-weight: 400;
  cursor: pointer;
  color: #FFF;
  text-align: center;
}

  #T81001Grid{
     ::-webkit-scrollbar {display:none}
  }
</style>
