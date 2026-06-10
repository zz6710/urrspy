<template>
    <div>
        <div>
            <div class="add-btn-div-29">
                <div class="add-btn"  @click="addHandler">+</div>
            </div>
            <k-grid ref="T81001Grid" data-action="T83004.findTa3004s"  id="T81001Grid" :data-autoload="false"
                    data-operate-column-position="end"  @init="(grid)=>{this.$kgrid = grid}" >
                <k-grid-column data-header="费用类型" data-name="feeType" data-width="80"  data-dict="fee_type"></k-grid-column>
                <k-grid-column data-header="启用日期" data-name="enableDate"  data-width="90" data-type="date"></k-grid-column>
                <k-grid-column data-header="收费方式" data-name="chargeType"  data-width="80" data-dict="charge_type"></k-grid-column>
                <k-grid-column data-header="认申购收费模式" data-name="buyfeeMode"  data-width="100" data-dict="buyfee_mode"></k-grid-column>
                <k-grid-column data-header="认申购费用计算方式" data-name="buyfeeMethod"  data-width="100" data-dict="buyfee_method"></k-grid-column>
                <k-grid-column data-header="费率计算方式" data-name="rateCalculateMethod"  data-width="100" data-dict="rate_calculate_method"></k-grid-column>
                <k-grid-column data-header="费率合并方式" data-name="rateMergeMethod"  data-width="100" data-dict="rate_merge_method"></k-grid-column>
                <k-grid-column data-header="归产品资产计算方式" data-name="redemfeeAssetMethod"  data-width="100" data-dict="redemfee_asset_method"></k-grid-column>
                <k-grid-column data-header="归产品资产比例(%)" data-name="redemfeeAssetRat"  data-width="100"></k-grid-column>

                <template slot="operate">
                    <k-btn data-functype="POPUP" data-size="mini" data-target="editPopup"  class="md-info md-just-icon md-simple"
                        :data-handler="editHandler"  data-descript="修改交易费用">
                        <md-icon>edit</md-icon>
                    </k-btn>
                    <k-btn data-functype="SUBMIT" data-size="mini" data-type="danger"  class="md-danger md-just-icon md-simple"
                        data-action="T83004.deleteTa3004" data-target="T81001Grid"
                        data-confirm data-descript="删除交易费用">
                        <md-icon>close</md-icon>
                    </k-btn>
                </template>
            </k-grid>
        </div>

		<!-- 新增弹出 -->
    	<k-popup ref="addPopup" data-title="新增">
      		<AddComp v-model="formData" :info="formData"/>
    	</k-popup>
    	<!-- 修改弹出 -->
    	<k-popup ref="editPopup" data-title="修改">
      		<EditComp v-model="formData" :info="formData"/>
    	</k-popup>
    </div>
</template>

<script>

    import kayak from '@/frame/kayak.js'
    import AddComp from "./M81001-PrjTradeFee-add"
    import EditComp from "./M81001-PrjTradeFee-edit"

    export default {
        components: {
            AddComp,EditComp
        },
        props: {
        prodCode:{
                type:String,
                default:'',
            },
        },
        data() {
        return {
            $kgrid : null,
            PrjTradeFeeData:{},
            formData:{},
            gridQueryData:{}
        };
        },
        methods: {

        editHandler(params){
            this.formData = params;
            return params;
        },
        addHandler(){
            this.formData={
                prodCode : this.prodCode
            }
            this.$refs.addPopup.popup();
        },
        },
        watch: {

            'gridQueryData.prodCode'(value){
                this.$kgrid.load(this.gridQueryData);
            },

            prodCode(value){
                if(value != ''){
                    this.gridQueryData.prodCode = value;
                    this.$kgrid.load(this.gridQueryData);
                }
            },

        },
        mounted(){
            // console.log("mounted:"+this.prodCode);
            this.gridQueryData.prodCode = this.prodCode;
            this.$kgrid.load(this.gridQueryData);
        }
    };
</script>


<style lang="scss" scoped>
    @import "src/styles/T82001";

    .query-div{
        margin-bottom: 36px;
    }

    .add-btn-div{
        margin: -29px 0 0 108px;
        position: relative;
        z-index: 1;
    }

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
