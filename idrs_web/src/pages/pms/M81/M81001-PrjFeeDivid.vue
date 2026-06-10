<template>
    <div>
        <div class="query-div" >
            <k-form ref="T82001Form" :data-col="3" :data-model="gridQueryData">
                <k-form-item data-input-width="150px">
                    <k-field-text v-model="gridQueryData.distributorCode"  data-placeholder="销售商代码"/>
                </k-form-item>
            </k-form>
        </div>
        <div style="min-height:225px;">
            <div class="add-btn-div">
                <div class="add-btn"  @click="addHandler">+</div>
            </div>
            <k-grid ref="T82001Grid" data-action="T83005.findTa3005sByProdCode"  id="T81001Grid" :data-autoload="false"
                    data-operate-column-position="end"  @init="(grid)=>{this.$kgrid = grid}" >
                <k-grid-column data-header="销售商" data-name="distributorName"  data-width="120" ></k-grid-column>
                <k-grid-column data-header="费用类型" data-name="feeType"  data-width="80"  data-dict="fee_type"></k-grid-column>
                <k-grid-column data-header="分成方式" data-name="dividType"  data-width="80" data-dict="divid_type"></k-grid-column>
                <k-grid-column data-header="启用日期" data-name="enableDate"  data-width="100" data-type="date"></k-grid-column>
                <k-grid-column data-header="归管理人比例(%)" data-name="managerDividRate"  data-width="120" ></k-grid-column>
                <k-grid-column data-header="归销售商比例(%)" data-name="distributorDividRate"  data-width="120" ></k-grid-column>
                <template slot="operate">
                    <k-btn data-functype="POPUP" data-size="mini" data-target="editPopup"  class="md-info md-just-icon md-simple"
                        :data-handler="editHandler"  data-descript="修改分成方案">
                        <md-icon>edit</md-icon>
                    </k-btn>
                    <k-btn data-functype="SUBMIT" data-size="mini" data-type="danger"  class="md-danger md-just-icon md-simple"
                        data-action="T83005.deleteTa3005" data-target="T82001Grid"
                        data-confirm data-descript="删除分成方案">
                        <md-icon>close</md-icon>
                    </k-btn>
                </template>
            </k-grid>
        </div>

        <!-- 新增弹出框 -->
        <k-popup ref="addPopup" data-title="新增">
            <k-form ref="addForm" :data-col="2">
                <k-form-item label="销售商代码" data-input-width="600px">
                    <k-field-select v-model="rowData.distributorCode" data-action="T8Dict.findTaDistributorInfos" :dataAllowblank='false'
                                    data-display-field="distributorName"  data-value-field="distributorCode"  :data-multiple="true"/>
                </k-form-item>
                <k-form-item label="费用类型" data-input-width="600px">
                    <k-field-select v-model="rowData.feeType" v-show="this.openProdFlag" data-dict="fee_type" :data-allowblank="false"  :data-multiple="true" @data-on-change="feeTypeChange"/>
                    <k-field-select v-model="rowData.feeType" v-show="this.openProdFlag ? false:true" data-dict="fee_close_type" :data-allowblank="false"  :data-multiple="true" @data-on-change="feeTypeChange"/>
                </k-form-item>
                <k-form-item label="产品代码" v-show="false">
                    <k-field-text v-model="rowData.prodCode" :dataAllowblank='false' data-disabled/>
                </k-form-item>
                <k-form-item label="分成方式">
                    <k-field-select v-model="rowData.dividType" data-dict="divid_type" :data-allowblank="false" data-disabled/>
                </k-form-item>
                <k-form-item label="启用日期">
                    <k-field-date v-model="rowData.enableDate" :data-allowblank="false" :dataMinValue="this.currentWorkday"/>
                </k-form-item>
                <k-form-item label="归管理人比例(%)">
                    <k-field-text data-validate-type="number" v-model="rowData.managerDividRate" data-max-value="100" data-digits="5"
                                            data-min-value="0"  :data-allowblank="false"/>
                </k-form-item>
                <k-form-item label="归销售商比例(%)">
                    <k-field-text data-validate-type="number" v-model="rowData.distributorDividRate" data-max-value="100" data-digits="5"
                                            :data-disabled="true" :data-allowblank="false" />
                </k-form-item>

                <k-form-footer data-align="center">
                    <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T83005.addTa3005ByProdCode" :data-handler="addConfirmHandler"
                            data-from="addForm" :data-model="rowData" data-target="T82001Grid" >
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
                <k-form-item label="销售商代码">
                    <k-field-select v-model="rowData.distributorCode" data-action="T8Dict.findTaDistributorInfos" :dataAllowblank='false'
                                    data-display-field="distributorName"  data-value-field="distributorCode"  data-disabled/>
                </k-form-item>
                <k-form-item label="费用类型">
                    <k-field-select v-model="rowData.feeType" data-dict="fee_type" :data-allowblank="false"  data-disabled/>
                </k-form-item>
                <k-form-item label="产品代码" v-show="false">
                    <k-field-text v-model="rowData.prodCode" :dataAllowblank='false' data-disabled/>
                </k-form-item>
                <k-form-item label="分成方式">
                    <k-field-select v-model="rowData.dividType" data-dict="divid_type" :data-allowblank="false" data-disabled/>
                </k-form-item>
                <k-form-item label="启用日期">
                    <k-field-date v-model="rowData.enableDate" :data-allowblank="false" data-disabled/>
                </k-form-item>
                <k-form-item label="归管理人比例(%)">
                    <k-field-text data-validate-type="number" v-model="rowData.managerDividRate" data-max-value="100" data-digits="5"
                                            data-min-value="0"  :data-allowblank="false"/>
                </k-form-item>
                <k-form-item label="归销售商比例(%)">
                    <k-field-text data-validate-type="number" v-model="rowData.distributorDividRate" data-max-value="100" data-digits="5"
                                            :data-disabled="true" data-min-value="0"  :data-allowblank="false" />
                </k-form-item>

                <k-form-footer data-align="center">
                    <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T83005.updateTa3005" :data-handler="editConfirmHandler"
                            data-from="editForm" :data-model="rowData" data-target="T82001Grid">
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
  const math = require("mathjs");

  export default {
    components: {
      updSuccess: Function,

    },
    props:{
      prodMode:'',
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
        openProdFlag:true,
        currentWorkday: null,
      };
    },
    mounted(){
      this.httpUtil.sysDate().then(res=>{
        if (res) {
          this.currentWorkday = res;
          // console.log(res);
        }
      });
        if(this.prodCode != ''){
            this.gridQueryData.prodCode = this.prodCode;
            this.$kgrid.load(this.gridQueryData);
        }
    },
    methods: {
        editHandler(params){
            this.rowData = params;
            return params;
        },

        addConfirmHandler(row){

            if(this.prodCode==null || this.prodCode==''){
                Tools.alert("未录入产品代码","danger");
                return false;
            }

            row.prodCode = this.prodCode;
            return row;
        },
        editConfirmHandler(row){
            if(this.prodCode==null || this.prodCode==''){
                Tools.alert("未录入产品代码","danger");
                return false;
            }
            row.prodCode = this.prodCode;
            return row;
        },
        addHandler(){
            this.rowData={
                prodCode : this.prodCode
            }
            this.$refs.addPopup.popup();
        },
        feeTypeChange(value){
            //如果费用类型参数选择2-赎回费，则费用分成方式只能选择2-扣除归产品资产；不是赎回费，则分成方式只能选择1-总费用比例
            if(value.indexOf("2") != -1){
                //默认资管给的就是计算扣除归资产后的数据，分成方式只能选 “扣除归产品资产后”
                this.rowData.dividType = "2";
            }else{
                // this.rowData.dividType = "1";
                this.rowData.dividType = "2";
            }
        }
    },
    created(){
      //是否是封闭型
      if(this.prodMode == '1'){
        this.openProdFlag = false;
      }
    },
    watch: {
        'rowData.managerDividRate'(newValue) {
            if(newValue != null){
                if(newValue>100){
                    newValue = 100;
                }
                this.rowData.distributorDividRate = math.format(100 - newValue,7);
            }
        },
        'gridQueryData.distributorCode'(){
            this.$kgrid.load(this.gridQueryData);
        },
        prodCode(value){
            if(value != ''){
                this.gridQueryData.prodCode = value;
                this.$kgrid.load(this.gridQueryData);
            }
        },
    },
  };
</script>


<style lang="scss" scoped>
  @import "src/styles/T82001";

    ::v-deep #T81001Grid > .md-card{
        margin: 10px 0;
    }

    ::v-deep #T81001Grid > div:first-child{
        box-shadow: none;
    }

    ::v-deep #T81001Grid > div:first-child > div:first-child{
        display: none;
    }
</style>
