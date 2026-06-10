<template>
  <div>
    <k-form-search-customize data-target="prodGrid" v-model="printTemp">
      <k-form-item label="产品名称">
        <k-field-select v-model="printTemp.prodCode"  data-action="T8Dict.findTaProdInfos"
                        data-display-field="prodCode,prodName" data-value-field="prodCode" ></k-field-select>
      </k-form-item>
    </k-form-search-customize>
    <k-grid ref="prodGrid"   data-action='T8ProdInfo.findProdInfoByByCodeAndDate' @data-row-select="selectDistributorQuotaRow"  data-operate-width="210px">
      <k-grid-column data-header="产品名称" data-name="prodName"></k-grid-column>
      <k-grid-column data-header="产品代码" data-name="prodCode"></k-grid-column>
      <k-grid-column data-header="预期募集规模" data-name="expeScale"></k-grid-column>
      <k-grid-column data-header="产品状态" data-name="prodStatus" data-dict="t8_prod_lifecycle"></k-grid-column>
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" data-descript="新增" :data-handler="openBox" data-functype="POPUP" data-size="mini"
               data-target="addBranchBankQuotaPopup">
          <md-icon>add</md-icon>
        </k-btn>
      </template>
    </k-grid>

    <k-grid ref="branchGrid" data-action="T8BranchBank.findInfoByProdCode" @data-row-select="selectData"   data-operate-width="210px">
      <k-grid-column data-header="产品名称" data-name="prodName"></k-grid-column>
      <k-grid-column data-header="产品代码" data-name="prodCode"></k-grid-column>
      <k-grid-column data-header="分行名称" data-name="branchName"></k-grid-column>
      <k-grid-column data-header="分行额度" data-name="branchQuota"></k-grid-column>
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" data-descript="修改" data-functype="POPUP" data-size="mini"
               data-target="updateBranchBankQuotaPopup">
          <md-icon>edit</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple" data-descript="删除" data-action="T8BranchBank.deleteById"
               data-type="danger" :data-confirm="true" data-functype="SUBMIT" data-size="mini"
               data-target="branchGrid">
          <md-icon>close</md-icon>
        </k-btn>
      </template>
    </k-grid>
    <!--    添加额度弹出框   -->
    <k-popup ref="addBranchBankQuotaPopup" data-title="新增销售商额度" @data-close="()=>this.envItems=[]"
             @data-opened="()=>this.envItems=[{}]">
      <k-form ref="addBranchBankQuotaForm1" data-total-width="888px" >
        <k-form-item label="产品代码">
          <k-field-text v-model="formData.prodCode"  data-disabled />
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="formData.prodName" data-disabled  />
        </k-form-item>
      </k-form>
        <k-form ref="addBranchBankQuotaForm2" v-for="(item,index) in envItems" :key="index"
                :data-col="4" data-input-width="150px" data-label-width="115px" data-total-width="918px">
            <k-form-item label="分行名称">
              <k-field-text v-model="item.key" :data-allowblank="false"/>
            </k-form-item>
            <k-form-item label="分行金额">
              <k-field-text v-model="item.value" :data-allowblank="false" style="margin-left: 40px"/>
            </k-form-item>
            <k-btn class="md-info md-just-icon md-simple" style="top: 15px;" data-descript="新增"
                   @click="()=>envItems.push({})">
              <md-icon>add</md-icon>
            </k-btn>
            <k-btn class="md-info md-just-icon md-simple" style="top: 15px;" data-descript="删除当前行"
                   @click="deleteEvent(index)">
              <md-icon md-src="/static/svg/delete.svg"/>
            </k-btn>
        </k-form>

        <div style="margin: 0 auto;width: 255px;">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"
                 :data-handler="submitHandle" data-url="/server/form/PmsApp/t8BranchBank/save.json"
                 :data-model="formData">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
      </div>
    </k-popup>
    <k-popup ref="updateBranchBankQuotaPopup"  data-title="修改分行额度">
      <k-form ref="updateBranchBankQuotaFrom" :data-col="2">
        <k-form-item label="产品代码">
          <k-field-text v-model="formData.prodCode"  :data-allowblank="false"
                          data-disabled></k-field-text>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="formData.prodName"  :data-allowblank="false"
                          data-disabled></k-field-text>
        </k-form-item>
        <k-form-item label="分行名称">
          <k-field-text v-model="formData.branchName"  :data-allowblank="false"></k-field-text>
        </k-form-item>
        <k-form-item label="产品额度">
          <k-field-text v-model="formData.branchQuota" data-validate-type="money" data-type="money" data-min-value="0"
                        :data-max-length="18" data-digits="2" :data-allowblank="false"></k-field-text>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" :data-model="formData" data-functype="SUBMIT"
                 data-from="updateBranchBankQuotaFrom" data-action="T8BranchBank.updaeteBranchQuota"
                 data-target="branchGrid">
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
  import {
    assign
  } from "lodash";

    export default {
      data() {
        return {
          printTemp:{
            prodName: ''
          },
          formData: {},
          envItems: [],
          selectRowData: {},
          btnIndex: 0
        };
      },
      methods:{
        selectDistributorQuotaRow(row, column, event){
          const _this = this
          _this.selectRowData = assign({}, row)
          _this.formData = assign({}, row)
          let branchGrid = _this.$refs.branchGrid
          if (typeof branchGrid.gridOptions.dataParams == "undefined") {
            branchGrid.gridOptions.dataParams = {
              "prodCode": row.prodCode
            }
          } else {
            branchGrid.gridOptions.dataParams.prodCode = row.prodCode
          }
          branchGrid.load(row)
        },
        selectData(row, column, event){
          this.formData = assign({}, row)
        },
        openBox(){
          this.envItems = [{}];
        },
       /* addEvent(index){
          this.envItems.push({})
        },*/
        deleteEvent(index){
          if (this.envItems.length > 1){
            this.envItems.splice(index,1)
          }
        },
        submitHandle(value){
          let result = true;
          result = this.$refs.addBranchBankQuotaForm1.validate();
          let form2s = this.$refs.addBranchBankQuotaForm2;
          if (form2s && form2s.length > 0) {
            for (let i = 0; i < form2s.length; i++) {
              result = result && form2s[i].validate();
            }
          }
          if (result === false) {
            return false;
          }
          if (this.envItems && this.envItems.length > 0) {
            value.json = JSON.stringify({"envItemsConf": this.envItems})
          }
        }
      }
    }
</script>
<style scoped>
</style>
