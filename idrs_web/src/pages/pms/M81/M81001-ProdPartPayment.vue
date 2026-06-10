<template>
    <div>
        <div>
            <div class="add-btn-div-29">
                <div class="add-btn"  @click="addHandler" >+</div>
            </div>
            <k-grid ref="T81001Grid" data-action="T8ProdPartPayment.findT8ProdPartPayments"  id="T81001Grid" :data-autoload="false"
                  @init="(grid)=>{this.$kgrid = grid}"  data-operate-column-position="end" data-operate-width="300px">
                <k-grid-column data-header="产品代码" data-name="prodCode"  data-width="100" ></k-grid-column>

                <k-grid-column data-header="兑付方式" data-name="cashMode"  data-width="80" data-dict="cash_mode"></k-grid-column>

                <k-grid-column data-header="兑付净值" data-name="cashNav"  data-width="100" ></k-grid-column>

                <k-grid-column data-header="兑付比例" data-name="cashRatio"  data-width="80" ></k-grid-column>

<!--                <k-grid-column data-header="兑付总金额" data-name="cashTotalAmt"  data-width="100"></k-grid-column>-->//暂时不需要

                <k-grid-column data-header="兑付日期" data-name="cashDate"  data-width="100"></k-grid-column>

                <k-grid-column data-header="执行状态" data-name="execStatus"  data-width="80" data-dict="exec_status"></k-grid-column>

                <k-grid-column data-header="创建日期" data-name="crtTime"  data-width="120"></k-grid-column>

                <!-- <k-grid-column data-header="备注" data-name="remark"  data-width="80"></k-grid-column> -->

                <template slot="operate" slot-scope="scope">

                 <k-btn data-functype="POPUP" data-confirm data-size="mini"   class="md-info md-just-icon md-simple"
                  data-target="editTaProdPartPaymentPopup" :data-disabled="scope.row.row.execStatus != '0'" data-descript="修改兑付方案信息" :data-handler="editHandler">
                  <md-icon>edit</md-icon>
                 </k-btn>

                 <k-btn data-functype="SUBMIT" data-size="mini" class="md-danger md-just-icon md-simple" data-descript="删除兑付方案信息"
                 data-target="T81001Grid" data-action="TaProdPartPayment.deleteTaProdPartPayment"
                 :data-disabled="scope.row.row.execStatus != '0'" data-confirm data-type="danger">
                 <md-icon>close</md-icon>
                 </k-btn>

                 </template>

            </k-grid>
        </div>

        <!-- 添加弹出框 -->
        <k-popup ref="addPopup" data-title="新增" >
            <k-form ref="addForm" :data-col="2">

            <k-form-item label="产品代码" v-show="false">
              <k-field-text v-model="formData.prodCode"  data-disabled :dataAllowblank="false" data-value-field="prodCode" /></k-form-item>

            <k-form-item label="兑付方式">
              <k-field-select v-model="formData.cashMode" :dataAllowblank="false" data-dict="cash_mode" data-default-value="1"
              data-disabled/>
             </k-form-item>

            <k-form-item label="兑付净值" v-show="formData.cashMode == '1' ">
              <k-field-text v-model="formData.cashNav" :dataAllowblank="false" :data-max-length="9" :data-digits="7"
              data-min-value="0" data-validate-type="number" data-max-value="100)"/></k-form-item>

            <k-form-item label="兑付比例">
              <k-field-text v-model="formData.cashRatio"  :data-max-length="8" :data-digits="5"
              data-min-value="0" data-validate-type="number" data-max-value="100" :dataAllowblank="false"/></k-form-item>

            <k-form-item label="兑付总金额" v-show="formData.cashMode == '2' ">
              <k-field-text v-model="formData.cashTotalAmt" :data-max-length="18" :data-digits="2"
              data-min-value="0" data-validate-type="number" data-max-value="1000000000000000)"/></k-form-item>

            <k-form-item label="兑付日期">
              <k-field-date v-model="formData.cashDate" :dataAllowblank="false" :dataMinValue="this.currentWorkday"	/></k-form-item>

            <!-- <k-form-item label="备注">
              <k-field-text v-model="formData.remark" :data-max-length="80"/></k-form-item> -->

             <k-form-footer data-align="center">
                <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdPartPayment.addT8ProdPartPayment"
                          data-from="addForm" :data-model="formData" :data-handler="beforeSubmit"
                          data-target="T81001Grid">
                       <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
                </k-btn>
                <k-btn class="btn-custom-plain" data-functype="CLOSE">
                       <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
             </k-form-footer>

            </k-form>
        </k-popup>


        <!--    修改弹出框   -->
        <k-popup ref="editTaProdPartPaymentPopup" data-title="修改">
            <k-form ref="editTaProdPartPaymentForm" :data-col="2">

            <k-form-item label="产品代码" v-show="false">
              <k-field-text v-model="formData.prodCode"  data-disabled :dataAllowblank="false" data-value-field="prodCode" /></k-form-item>

            <k-form-item label="兑付方式">
              <k-field-select v-model="formData.cashMode" :dataAllowblank="false" data-dict="cash_mode" data-disabled/>
             </k-form-item>

            <k-form-item label="兑付净值" v-show="formData.cashMode == '1' ">
              <k-field-text v-model="formData.cashNav" :dataAllowblank="false" :data-max-length="8" :data-digits="5"
              data-min-value="0" data-validate-type="number" data-max-value="100"/></k-form-item>

            <k-form-item label="兑付比例">
              <k-field-text v-model="formData.cashRatio"  :data-max-length="8" :data-digits="5"
              data-min-value="0" data-validate-type="number" data-max-value="100"/></k-form-item>

            <k-form-item label="兑付总金额" v-show="formData.cashMode == '2' ">
              <k-field-text v-model="formData.cashTotalAmt" :data-max-length="18" :data-digits="2"
              data-min-value="0" data-validate-type="number" data-max-value="1000000000000000)"/></k-form-item>

            <k-form-item label="兑付日期">
              <k-field-date v-model="formData.cashDate" :dataAllowblank="false"/></k-form-item>

            <!-- cashDateEdit 用于查询判断 传参 -->
            <k-form-item label="兑付日期修改前" v-show="false">
              <k-field-date v-model="formData.cashDateEdit"/></k-form-item>

            <!-- <k-form-item label="备注">
              <k-field-text v-model="formData.remark" :data-max-length="80"/></k-form-item> -->

            <k-form-footer data-align="center">
               <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdPartPayment.updateT8ProdPartPayment"
                         data-from="editTaProdPartPaymentForm" :data-model="formData"
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
            formData:{},
            gridQueryData:{},
            currentWorkday: null,
        };
        },
      created() {
        this.httpUtil.sysDate().then(res=>{
          if (res) {
            this.currentWorkday = res;
          }
        });
      },
        methods: {

            editHandler(params){
            this.formData = params;
            this.formData.cashRatio = Number(this.formData.cashRatio) * 100;
            this.formData.cashDateEdit = this.formData.cashDate; //传入一个修改前的兑付日期，用于修改查询
            return params;
            },

            addHandler(){
                this.formData={
                    prodCode : this.prodCode
                }
                this.$refs.addPopup.popup();
            },

            beforeSubmit(formData){
                if(this.prodCode==null || this.prodCode==''){
                    Tools.alert("未录入产品代码","danger");
                    return false;
                }
                return formData;
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
