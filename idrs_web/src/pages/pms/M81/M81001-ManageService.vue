<template>
    <div>
        <div>
            <div class="add-btn-div-29">
                <div class="add-btn"  @click="addHandler" >+</div>
            </div>
            <k-grid ref="T81001Grid" data-action="T83007.findT83007s"  id="T81001Grid" :data-autoload="false"
                    data-operate-column-position="end"  @init="(grid)=>{this.$kgrid = grid}" >
                <k-grid-column data-header="产品代码" data-name="prodCode"></k-grid-column>
                <k-grid-column data-header="启用日期" data-name="enableDate"></k-grid-column>
                <k-grid-column data-dict="calSalesFee" data-header="计算方式" data-name="calcType"></k-grid-column>
                <k-grid-column data-dict="yeardays" data-header="年天数" data-name="managefeeYeardays"></k-grid-column>
                <k-grid-column data-dict="nav_rule" data-header="净值取值规则" data-name="navRule"></k-grid-column>
                <k-grid-column data-header="管理费率" data-name="rate"></k-grid-column>
                <k-grid-column data-header="创建日期" data-name="crtTime"></k-grid-column>
                <k-grid-column data-header="创建人" data-name="crtUser"></k-grid-column>
                <k-grid-column data-header="更新日期" data-name="updTime"></k-grid-column>
                <k-grid-column data-header="更新人" data-name="updUser"></k-grid-column>
                <k-grid-column data-header="备注" data-name="remark"></k-grid-column>

                <template slot="operate">
                    <k-btn data-functype="POPUP" data-size="mini" data-target="editPopup"  class="md-info md-just-icon md-simple"
                        :data-handler="editHandler"  data-descript="修改管理费">
                        <md-icon>edit</md-icon>
                    </k-btn>
                    <k-btn data-functype="SUBMIT" data-size="mini" data-type="danger"  class="md-danger md-just-icon md-simple"
                        data-action="T83007.deleteTa3007" data-target="T81001Grid" :data-handler="deleteHandler"
                        data-confirm data-descript="删除管理费">
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
    import AddComp from "./M81001-ManageService-add"
    import EditComp from "./M81001-ManageService-edit"

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
