<template>
    <div>
        <div class="query-div">
            <k-form ref="ta5002Form" :data-col="3" :data-model="gridQueryData">
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
            <k-grid ref="T82001Grid" data-action="T8PrjTailingCommision.findTaPrjTailingCommisions"  id="T82001Grid" :data-autoload="false"
                    data-operate-column-position="end"  @init="(grid)=>{this.$kgrid = grid}" >
                <k-grid-column data-header="产品简称" data-name="prodNameShort"  data-width="120" ></k-grid-column>
                <k-grid-column data-header="启用日期" data-name="enableDate"  data-width="100" data-type="date"></k-grid-column>
                <k-grid-column data-header="计提方式" data-name="tailingCalcMode"  data-width="100" data-dict="tailing_calc_mode"></k-grid-column>
                <k-grid-column data-header="年天数" data-name="tailingCommisionYeardays"  data-width="80" ></k-grid-column>
                <k-grid-column data-header="起付金额" data-name="minPayAmt"   data-type="money" data-width="120"></k-grid-column>
                <k-grid-column data-header="是否累进计算" data-name="graduatedCalc"  data-width="100" data-dict="1yes0no"></k-grid-column>
                <k-grid-column data-header="备注" data-name="remark"  data-width="120"></k-grid-column>

                <template slot="operate">
                    <k-btn data-functype="POPUP" data-size="mini" data-target="editPopup"  class="md-info md-just-icon md-simple"
                        :data-handler="editHandler"  data-descript="修改尾佣方案">
                        <md-icon>edit</md-icon>
                    </k-btn>
                    <k-btn data-functype="SUBMIT" data-size="mini" data-type="danger"  class="md-danger md-just-icon md-simple"
                        data-action="T8PrjTailingCommision.deleteTaPrjTailingCommision" data-target="T82001Grid"
                        data-confirm data-descript="删除尾佣方案">
                        <md-icon>close</md-icon>
                    </k-btn>
                </template>
            </k-grid>
        </div>

        <!-- 新增弹出框 -->
        <k-popup ref="addPopup" data-title="新增">
        <AddComp v-model="rowData"
            :updSuccess="()=> {
            this.$refs.addPopup.close();
            this.$refs.T82001Grid.load()
            }"/>
        </k-popup>

        <!-- 修改弹出框 -->
        <k-popup ref="editPopup" data-title="修改">
            <EditComp v-model="rowData"
                :updSuccess="()=> {
                this.$refs.editPopup.close();
                this.$refs.T82001Grid.load()
                }"/>
        </k-popup>
    </div>
</template>

<script>

    import kayak from '@/frame/kayak.js'
    import AddComp from "./M82001-prjTailing-add"
    import EditComp from "./M82001-prjTailing-edit"

    export default {
        components: {
            AddComp,EditComp
        },
        props: {
            updSuccess: Function,
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
                $kgrid : null,
                prjTailingData:{},
                rowData:{},
                gridQueryData:{},
            };
        },
        mounted(){
            if(this.distributorCode != ''){
                this.gridQueryData.distributorCode = this.distributorCode;
                this.$kgrid.load(this.gridQueryData);
            }
            this.prjTailingData = this.infos;
        },
        methods: {

            editHandler(params){
                this.rowData = params;
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
            'gridQueryData.prodCode'(value){
                this.$kgrid.load(this.gridQueryData);
            },
            distributorCode(value){
                if(value != ''){
                    this.gridQueryData.distributorCode = value;
                    this.$kgrid.load(this.gridQueryData);
                }
            },
        },
    };
</script>


<style lang="scss" scoped>
  @import "src/styles/T82001";

</style>
