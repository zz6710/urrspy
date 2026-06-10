<template>
    <div>
        <div>
            <div class="add-btn-div-29">
                <div class="add-btn"  @click="addHandler" :disabled="this.prodCode==''?'disabled':''" >+</div>
            </div>
            <k-grid ref="T81001Grid" data-action="T83002.findT83002s"  id="T81001Grid" :data-autoload="false"
                    data-operate-column-position="end"  @init="(grid)=>{this.$kgrid = grid}" >
                <k-grid-column data-name="prodCode" data-header="产品代码" ></k-grid-column>
                <k-grid-column data-header="费用类型" data-name="feeType" data-dict = "sale_manager_fee_type"></k-grid-column>
                <k-grid-column data-name="startDate" data-header="计提开始日" data-type="date"></k-grid-column>
                <k-grid-column data-name="endDate" data-header="计提截止日"  data-type="date"></k-grid-column>
                <k-grid-column data-name="theoryPayDate" data-header="计划支付日"  data-type="date"></k-grid-column>
                <k-grid-column data-name="factPayDate" data-header="实际支付日"  data-type="date"></k-grid-column>
                <k-grid-column data-name="crtTime" data-header="创建日期" ></k-grid-column>
                <k-grid-column data-name="updTime" data-header="更新日期" ></k-grid-column>
                <k-grid-column data-name="crtUser" data-header="创建人"></k-grid-column>
                <k-grid-column data-name="updUser" data-header="更新人"></k-grid-column>
                <k-grid-column data-name="remark" data-header="备注"></k-grid-column>

                <template slot="operate">
                    <k-btn data-functype="POPUP" data-size="mini" data-target="editPopup"  class="md-info md-just-icon md-simple"
                        :data-handler="editHandler"  data-descript="修改销服支付计划">
                        <md-icon>edit</md-icon>
                    </k-btn>
                    <k-btn data-functype="SUBMIT" data-size="mini" data-type="danger"  class="md-danger md-just-icon md-simple"
                        data-action="T83002.deleteTa3002" data-target="T81001Grid"
                        data-confirm data-descript="删除销服支付计划">
                        <md-icon>close</md-icon>
                    </k-btn>
                </template>
            </k-grid>
        </div>

        <!-- <div class="add-btn-div">
            <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addPopup">
                <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
        </div> -->
    <!-- 新增弹出 -->
      <k-popup ref="addPopup" data-title="新增">
          <AddComp v-model="formData" :info="formData"
          :updSuccess="()=> {
           this.$refs.addPopup.close();
           this.$refs.T81001Grid.load()
           }"/>
      </k-popup>
      <!-- 修改弹出 -->
      <k-popup ref="editPopup" data-title="详情">
          <EditComp v-model="formData"
          :updSuccess="()=> {
           this.$refs.editPopup.close();
           this.$refs.T81001Grid.load()
          }"/>
      </k-popup>
    </div>
</template>

<script>

    import kayak from '@/frame/kayak.js'
    import AddComp from "./M81001-SaleFeePay-add"
    import EditComp from "./M81001-SaleFeePay-edit"

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
            ProdTradeFeeData:{},
            formData:{},
            gridQueryData:{},
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

            'gridQueryData.prodCode'(){
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
            if(this.prodCode!=""){
                this.gridQueryData.prodCode = this.prodCode;
                this.$kgrid.load(this.gridQueryData);
            }
        }
    };
</script>


<style  lang="scss" scoped>
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
