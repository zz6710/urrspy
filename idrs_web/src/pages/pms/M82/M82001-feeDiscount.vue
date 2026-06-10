<template>
    <div>
        <div class="query-div">
            <k-form ref="T82001Form" :data-col="3" :data-model="gridQueryData">
                <k-form-item data-input-width="150px">
                    <k-field-text v-model="gridQueryData.prodCode"  data-placeholder="产品代码"/>
                </k-form-item>
            </k-form>
        </div>
        <div style="min-height:225px;">
            <div class="add-btn-div">
                <div class="add-btn"  @click="addHandler">+
                </div>
            </div>
            <k-grid  ref="grid" data-action='T83006.findTa3006s'  data-operate-width="200px"  id="T82001Grid" :data-autoload="false"
                    @init="(grid)=>{this.$kgrid = grid}">
                <k-grid-column data-header="产品简称" data-name="prodNameShort"  data-width="120" ></k-grid-column>
                <k-grid-column data-header="费用类型" data-name="feeType" data-dict="fee_type"></k-grid-column>
                <k-grid-column data-header="最大折扣(%)" data-name="minDiscount" ></k-grid-column>
                <k-grid-column data-header="低于费率折扣上限配置" data-name="resolveWay" data-dict="resolve_way" ></k-grid-column>
                <template slot="operate">
                    <k-btn data-functype="POPUP" data-size="mini" data-target="editPopup"  class="md-info md-just-icon md-simple"
                        data-descript="修改折扣率记录" :data-handler="editHandler" >
                        <md-icon>edit</md-icon>
                    </k-btn>
                    <k-btn data-functype="SUBMIT" data-size="mini" data-type="danger"  class="md-danger md-just-icon md-simple"
                        data-action="T83006.deleteTa3006" data-target="grid"
                        data-confirm data-descript="删除记录">
                        <md-icon>close</md-icon>
                    </k-btn>
                </template>
            </k-grid>
        </div>

         <!-- 新增弹出框 -->
        <k-popup ref="addPopup" data-title="新增">
            <k-form ref="addForm" :data-col="2">
                <k-form-item label="产品代码" data-input-width="600px">
                    <k-field-select v-model="rowData.prodCode" data-action="T8Dict.findTaProdInfos" :dataAllowblank='false'
                                    data-display-field="prodCode,prodName"  data-value-field="prodCode"  :data-multiple="true" />
                </k-form-item>
                <k-form-item label="费用类型" data-input-width="600px">
                    <k-field-select v-model="rowData.feeType" data-dict="fee_type"  :data-allowblank="false" :data-multiple="true" />
                </k-form-item>
                <k-form-item label="销售商代码" v-show="false">
                    <k-field-text v-model="rowData.distributorCode" :dataAllowblank='false' data-disabled />
                </k-form-item>
                <k-form-item label="最大折扣率(%)">
                    <k-field-text data-validate-type="number" v-model="rowData.minDiscount" data-max-value="100"
                                                data-min-value="0"  :data-allowblank="false" :data-digits="5"/>
                </k-form-item>
                <k-form-item label="低于折扣率上限配置处理方式">
                    <k-field-select v-model="rowData.resolveWay"  :data-allowblank="false" data-dict="resolve_way"/>
                </k-form-item>

                <k-form-footer data-align="center">
                    <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T83006.addTa3006"
                        :data-model="rowData"  data-target="grid" :data-handler="beforeSubmit">
                        <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
                        </k-btn>
                    <k-btn class="btn-custom-plain" data-functype="CLOSE">
                        <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
                </k-form-footer>
            </k-form>
        </k-popup>


        <!-- 修改弹出框 -->
        <k-popup ref="editPopup" data-title="修改">
            <k-form ref="editForm" :data-col="2">
                <k-form-item label="产品代码" data-input-width="600px">
                    <k-field-select v-model="rowData.prodCode" data-action="T8Dict.findTaProdInfos" :dataAllowblank='false'
                                    data-display-field="prodCode,prodName"  data-value-field="prodCode" data-disabled/>
                </k-form-item>
                <k-form-item label="费用类型" data-input-width="600px">
                    <k-field-select v-model="rowData.feeType" data-dict="fee_type"  :data-allowblank="false" data-disabled/>
                </k-form-item>
                <k-form-item label="销售商代码" v-show="false">
                    <k-field-text v-model="rowData.distributorCode" :dataAllowblank='false' data-disabled />
                </k-form-item>
                <k-form-item label="最大折扣率(%)">
                    <k-field-text data-validate-type="number" v-model="rowData.minDiscount" data-max-value="100"
                                                data-min-value="0"  :data-allowblank="false" :data-digits="5"/>
                </k-form-item>
                <k-form-item label="低于折扣率上限配置处理方式">
                    <k-field-select v-model="rowData.resolveWay"  :data-allowblank="false" data-dict="resolve_way"/>
                </k-form-item>

                <k-form-footer data-align="center">
                    <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T83006.updateTa3006"
                        data-from="editForm" :data-model="rowData"
                        data-target="grid" :data-handler="beforeSubmit">
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
  import Tools from '@/utils/tools.js';

  export default {
    props: {
      infos:{
        type:Object
      },
      distributorCode:{
          type:String,
          default:'',
      }
    },
    data() {
      return {
        rowData : {},
        gridQueryData:{},
        $kgrid : null,
      };
    },
    mounted(){
        if(this.distributorCode != ''){
            this.gridQueryData.distributorCode = this.distributorCode;
            this.$kgrid.load(this.gridQueryData);
        }
    },
    methods: {

        editHandler(params){
            this.rowData = params;
            return params;
        },

        beforeSubmit(params){
            params.distributorCode = this.distributorCode;

            if(params.distributorCode==null || params.distributorCode==''){
            Tools.alert("未录入销售商代码:","danger");
            return false;
            }

            return params;
        },

        addHandler(){
            this.rowData={
                distributorCode : this.distributorCode
            }
            this.$refs.addPopup.popup();
        },
    },
    watch: {
        'gridQueryData.prodCode'(){
            this.$kgrid.load(this.gridQueryData);
        },
        distributorCode(value){
            if(value != ''){
                this.gridQueryData.distributorCode = value;
                this.$kgrid.load(this.gridQueryData);
            }else{
                this.$kgrid.clearAll();
            }
        },
    },
  };
</script>

<style lang="scss" scoped>
  @import "src/styles/T82001";
</style>
