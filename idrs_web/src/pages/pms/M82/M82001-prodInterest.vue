<template>
    <div>
        <div class="query-div">
            <k-form ref="ta5002Form" :data-col="3" :data-model="gridQueryData">
                <k-form-item data-input-width="150px">
                  <k-field-text v-model="gridQueryData.prodCode"   data-value-field="prodCode" data-placeholder="产品代码" /></k-form-item>
            </k-form>
        </div>

        <div style="min-height:225px;">
              <div class="add-btn-div">
                  <div class="add-btn"  @click="addHandler"  :data-disabled="this.distributorCode==''" >+
                  </div>
              </div>
               <k-grid id = "TaProdInterestGrid"
                 ref="TaProdInterestGrid"
                 data-action='T82001ProdAuthority.findTaDistributorInfos'
                 @data-row-select="selectRow"
                 @init="(grid)=>{this.$kgrid = grid}"
                 data-operate-column-position="end"
                 data-operate-width="300px"
                 :data-autoload="false">

                 <k-grid-column data-header="产品代码" data-name="prodCode" ></k-grid-column>
                 <k-grid-column data-header="销售商代码" data-name="distributorCode"></k-grid-column>
                 <k-grid-column data-header="利率方案代码" data-name="interestCode"  ></k-grid-column>
                 <k-grid-column data-header="资金处理模式" data-name="handlerMode" data-dict="handler_mode"></k-grid-column>
                 <k-grid-column data-header="状态" data-name="status"  data-dict="prodAuthority_status" ></k-grid-column>

                 <template slot="operate">

                 <k-btn data-functype="POPUP" data-confirm data-size="mini"   class="md-info md-just-icon md-simple"
                  data-target="updateTaIntfPopup" data-descript="修改利率信息" @submit.native.prevent>
                  <md-icon>edit</md-icon>
                 </k-btn>

                 <k-btn data-functype="SUBMIT" data-size="mini" class="md-danger md-just-icon md-simple" data-descript="删除利率信息"
                 data-target="TaProdInterestGrid" data-action="T82001ProdAuthority.deleteTaDistributorInfo" data-confirm data-type="danger" >
                 <md-icon>close</md-icon>
                 </k-btn>

                 </template>
               </k-grid>
        </div>

    <!--    添加弹出框   -->
    <k-popup ref="addPopup" data-title="新增">
     <k-form ref="addTaIntfForm" :data-col="2">

       <k-form-item label="销售商代码" v-show="false">
         <k-field-text v-model="rowData.distributorCode" data-action="T8Dict.findTaDistributorInfos"  :data-multiple="false"
                       data-display-field="distributorCode,distributorName"  data-value-field="distributorCode" data-disabled/>
       </k-form-item>

       <k-form-item label="产品代码" data-input-width="600px">
           <k-field-select v-model="rowData.prodCode" data-action="T8Dict.findTaProdInfos" :dataAllowblank='false'
             data-display-field="prodCode,prodName"  data-value-field="prodCode" :data-multiple="true"/>
       </k-form-item>

       <k-form-item label="利率方案">
         <k-field-select v-model="rowData.interestCode" data-action="T82003.findTaPrjInterestsWithDistinct"  ref="addInterestField"
                 data-display-field="interestCode,interestClass"  data-value-field="interestCode" :dataAllowblank='false' />
       </k-form-item>
       <k-form-item label="资金处理模式">
         <k-field-select v-model="rowData.handlerMode" data-dict="handler_mode" :dataAllowblank="true" />
       </k-form-item>

       <k-form-item label="状态">
         <k-field-select v-model="rowData.status" data-dict="prodAuthority_status" :dataAllowblank="false"/>
       </k-form-item>

       <k-form-footer data-align="center">
           <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T82001ProdAuthority.addTaProdAuthorityRelation"
                     data-from="addTaIntfForm" :data-model="rowData" :data-handler="addConfirmHandler"
                     data-target="TaProdInterestGrid">
                  <md-icon md-src="/static/svg/confirm.svg" />确定
           </k-btn>
           <k-btn class="btn-custom-plain" data-functype="CLOSE">
                  <md-icon md-src="/static/svg/cancel.svg" />取消</k-btn>
       </k-form-footer>

     </k-form>
    </k-popup>



    <!--    修改弹出框   -->
    <k-popup ref="updateTaIntfPopup" data-title="修改">
      <k-form ref="updateTaIntfForm" :data-col="2">

        <k-form-item label="销售商代码" v-show="false">
          <k-field-select v-model="rowData.distributorCode" data-action="T8Dict.findTaDistributorInfos"  :dataAllowblank="false"
                        data-display-field="distributorCode,distributorName"  data-value-field="distributorCode" data-disabled="true"/>
        </k-form-item>

        <k-form-item label="产品代码">
          <k-field-select v-model="rowData.prodCode"  data-action="T8Dict.findTaProdInfos" :dataAllowblank="false"
                          data-display-field="prodCode,prodName"  data-value-field="prodCode" data-disabled="true"/>
        </k-form-item>

        <k-form-item label="利率方案">
          <k-field-select v-model="rowData.interestCode" data-action="T82003.findTaPrjInterestsWithDistinct"  ref="addInterestField"
                  data-display-field="interestCode,interestClass"  data-value-field="interestCode" :dataAllowblank="false" />
        </k-form-item>
        <k-form-item label="资金处理模式">
          <k-field-select v-model="rowData.handlerMode" data-dict="handler_mode" />
        </k-form-item>

       <k-form-item label="状态">
         <k-field-select v-model="rowData.status"  data-dict="prodAuthority_status" :dataAllowblank="false" />
       </k-form-item>

        <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T82001ProdAuthority.updateTaProdAuthorityRelation"
                      data-from="updateTaIntfForm" :data-model="rowData"
                      data-target="TaProdInterestGrid">
                   <md-icon md-src="/static/svg/confirm.svg" />确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
                   <md-icon md-src="/static/svg/cancel.svg" />取消</k-btn>
        </k-form-footer>

      </k-form>
    </k-popup>


    </div>
</template>

<script>
  import kayak from '@/frame/kayak.js'
  import Tools from '@/utils/tools.js'
  import { assign } from "lodash"

  export default {
    props: {
      updSuccess: Function,
      infos:{},
      distributorCode:String,
    },
    data() {
      return {
        $kgrid : null,
        rowData:{},
        gridQueryData:{},
        interestData:{},
        row: null,
      };
    },
    methods: {
      selectRow(row, column, event) {
        const _this = this
        _this.selectRowData = assign({}, row)
         _this.rowData = assign({}, row)

        console.log(row);
      },
      addHandler(){
          this.rowData={
              distributorCode : this.distributorCode
          }
          this.$refs.addPopup.popup();
      },
      addConfirmHandler(params){
        if(params.distributorCode==null || params.distributorCode==''){
          Tools.alert("未录入销售商代码:","danger");
          return false;
        }
      },

    },
    watch: {
      //父页面传递的 销售商值变化时 更新此页面
      distributorCode: function(){
        this.gridQueryData.distributorCode  = this.distributorCode;
        this.$kgrid.load(this.gridQueryData);

      },
        'gridQueryData.prodCode'(){
            this.$kgrid.load(this.gridQueryData);
        },
    },

  };
</script>


<style lang="scss" scoped>

  @import "src/styles/T82001";


::v-deep #TaProdInterestGrid > div:first-child{
   box-shadow: none;
}

::v-deep #TaProdInterestGrid > div:first-child > div:first-child{
   display: none;
}

::v-deep #TaProdInterestGrid .el-table--scrollable-x .el-table__body-wrapper{
   overflow-x: hidden;
}


</style>
