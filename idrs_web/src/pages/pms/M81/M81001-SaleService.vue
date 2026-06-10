<template>
    <div>
        <div>
            <div class="add-btn-div-29">
                <div class="add-btn"  @click="addHandler" >+</div>
            </div>
            <k-grid ref="T81001Grid" data-action="T8ProdDistributor.find"  id="T81001Grid" :data-autoload="false"
                    data-operate-column-position="end"  @init="(grid)=>{this.$kgrid = grid}" >
                <k-grid-column data-header="销售商代码" data-name="distributorCode"></k-grid-column>
                <k-grid-column data-header="光大银行募集期开始时间" data-name="gdRaiseStartDate"></k-grid-column>
                <k-grid-column data-header="光大银行募集期结束时间" data-name="gdRaiseEndDate"></k-grid-column>
                <k-grid-column data-header="非母行代销商募集期开始时间（如有）" data-name="otherRaiseStartDate"></k-grid-column>
                <k-grid-column data-header="非母行代销商募集期结束时间（如有）" data-name="otherRaiseEndDate"></k-grid-column>
                <k-grid-column data-header="申购费折扣率" data-name="discountSubsScale"></k-grid-column>
                <k-grid-column data-header="赎回费折扣率" data-name="discountRedeemScale"></k-grid-column>
                <k-grid-column data-header="创建人" data-name="crtUser"></k-grid-column>
                <k-grid-column data-header="更新日期" data-name="updTime"></k-grid-column>
                <k-grid-column data-header="更新人" data-name="updUser"></k-grid-column>
                <k-grid-column data-header="备注" data-name="remark"></k-grid-column>

                <template slot="operate">
                    <k-btn data-functype="POPUP" data-size="mini" data-target="editPopup"  class="md-info md-just-icon md-simple"
                        :data-handler="editHandler"  data-descript="修改销售服务费">
                        <md-icon>edit</md-icon>
                    </k-btn>
                    <k-btn data-functype="SUBMIT" data-size="mini" data-type="danger"  class="md-danger md-just-icon md-simple"
                        data-action="Ta3003.deleteTa3003" data-target="T81001Grid" :data-handler="deleteHandler"
                        data-confirm data-descript="删除销售服务费">
                        <md-icon>close</md-icon>
                    </k-btn>
                </template>
            </k-grid>
        </div>


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
    import AddComp from "./M81001-SaleService-add"
    import EditComp from "./M81001-SaleService-edit"

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
            deleteHandler(params){
                console.log(params)
            }
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
            if(this.prodCode!=""){
                this.gridQueryData.prodCode = this.prodCode;
                this.$kgrid.load(this.gridQueryData);
            }
        }
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
