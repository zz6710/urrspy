<template>
  <div>
    <k-form ref="collectAsharedescriptionModelForm" :data-col="2" isFormBodyScreen>
     <k-form-item label="资产编号" v-show="false">
             <k-field-text v-model="formData.scrId"  id="scrId" :data-disabled="formData.scrIdDisabled"/>
           </k-form-item>
           <k-form-item label="市场">
             <k-field-select v-model="formData.trxMkt" :data-disabled="formData.trxMktDisabled" :data-allowblank="false" data-dict="market_stock"/>
           </k-form-item>
           <k-form-item label="板块类型">
             <k-field-select v-model="formData.plateType" :data-disabled="formData.plateTypeDisabled" :data-allowblank="false" data-dict="plateType"/>
           </k-form-item>
           <k-form-item label="股票代码">
             <k-field-text v-model="formData.scrCd" :data-disabled="formData.scrCdDisabled" :data-allowblank="false"  :data-max-length="40"/>
           </k-form-item>
           <k-form-item label="股票名称">
             <k-field-text v-model="formData.scrNm" :data-disabled="formData.scrNmDisabled" :data-allowblank="false"  :data-max-length="256"/>
           </k-form-item>
           <k-form-item label="公司名称">
             <k-field-text v-model="formData.companyName" :data-disabled="formData.companyNameDisabled" :data-allowblank="false"  :data-max-length="200"/>
           </k-form-item>
           <k-form-item label="币种">
             <k-field-select v-model="formData.ccy" :data-disabled="true" :data-allowblank="false" data-dict="cur_type"  data-default-value="CNY" />
           </k-form-item>
           <k-form-item label="中债一级分类">
             <k-field-select  v-model="formData.cbndFrsCtg" :data-disabled="formData.cbndFrsCtgDisabled" :data-allowblank="false" data-dict="cbndFrsCtg"/>
           </k-form-item>
           <k-form-item label="中债二级分类">
             <k-field-select v-model="formData.cbndScdCtg" data-dict ="ashareCbndScdCtg"  @data-on-change="dataOnChange"   :data-allowblank="formData.cbndScdCtgDisabled" :data-disabled="formData.cbndScdCtgDisabled"/>
           </k-form-item>
           <k-form-item label="人行一级分类">
             <k-field-select v-model="formData.pbnkFrsCtg"  data-dict="pbnkFrsCtg"  :data-disabled="formData.pbnkFrsCtgDisabled" :data-allowblank="false"/>
           </k-form-item>
           <k-form-item label="人行二级分类">
             <k-field-select v-model="formData.pbnkScdCtg"  data-dict="pbnkScdCtg"   :data-disabled="formData.pbnkScdCtgDisabled" :data-allowblank="false"/>
           </k-form-item>
           <k-form-item label="人行三级分类">
             <k-field-select v-model="formData.pbnkTrdCtg" data-dict="pbnkTrdCtg"  :data-allowblank="formData.pbnkTrdCtgDisabled" :data-disabled="formData.pbnkTrdCtgDisabled"/>
           </k-form-item>
           <k-form-item label="股票类型">
             <k-field-select v-model="formData.stockType" data-dict="stock_type" :data-allowblank="formData.stockTypeAllowblank" :data-disabled="formData.stockTypeDisabled"  @data-on-change="dataOnChange" />
           </k-form-item>
           <k-form-item label="投资阶段">
             <k-field-select v-model="formData.investmentType" data-dict="invest_stage" :data-allowblank="formData.investmentTypeAllowblank" :data-disabled="formData.investmentTypeDisabled"  @data-on-change="dataOnChange" />
           </k-form-item>
             <k-form-item label="股权退出安排">
                <k-field-date v-model="formData.sharehold" :data-allowblank="formData.shareholdAllowblank" :data-disabled="formData.shareholdDisabled" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
              </k-form-item>
           <k-form-item label="机构类型（按规模划分）">
             <k-field-select v-model="formData.isuOrgTypSiz" data-dict="instituteTypeTech" :data-allowblank="formData.isuOrgTypSizDisabled" :data-disabled="formData.isuOrgTypSizDisabled"/>
           </k-form-item>
           <k-form-item label="机构类型（按技术领域划分）">
             <k-field-select v-model="formData.lvrgTypTchno" data-dict="isuOrgTypTchno" :data-allowblank="formData.isuOrgTypTchnoDisabled" :data-disabled="formData.isuOrgTypTchnoDisabled"/>
           </k-form-item>
           <k-form-item label="机构类型（按经济类型划分）">
             <k-field-select v-model="formData.isuOrgTypEcn" data-dict="isuOrgTypEcn":data-allowblank="formData.isuOrgTypEcnDisabled" :data-disabled="formData.isuOrgTypEcnDisabled"  @data-on-change="dataOnChange" />
           </k-form-item>
           <k-form-item label="机构所属行业">
             <k-field-select v-model="formData.industryIssuer" data-dict="isuOrgBlgIdt":data-allowblank="formData.industryIssuerDisabled" :data-disabled="formData.industryIssuerDisabled"/>
           </k-form-item>
           <k-form-item label="是否为质押融资">
             <k-field-select v-model="formData.pledgedFinace" data-dict="isTrue":data-allowblank="formData.pledgedFinaceAllowblank" :data-disabled="formData.pledgedFinaceDisabled"/>
           </k-form-item>
           <k-form-item label="是否为债转股">
             <k-field-select v-model="formData.debtEquitySwap" data-dict="isTrue":data-allowblank="formData.debtEquitySwapAllowblank" :data-disabled="formData.debtEquitySwapDisabled"/>
           </k-form-item>
           <k-form-item label="备注">
             <k-field-text v-model="formData.remark"  :data-allowblank="formData.remarkAllowblank" :data-disabled="formData.remarkDisabled"  :data-max-length="256"/>
           </k-form-item>
      <k-form-footer slot="footer" data-align="center">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="AsharedescriptionModel.addAsharedescriptionInfo" data-from="collectAsharedescriptionModelForm"
               :data-model="formData" data-target="AsharedescriptionModelGrid">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">
          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
      </k-form-footer>
    </k-form>
  </div>
</template>

<script>
import inputNumberController from "@/pages/pms/basePublish/DisclosureRule/input-number-controller";
import Tools from "@/utils/tools";
export default {
  components: {inputNumberController},
  props: {
    parentFormData: {
      type: Object,
      default: () => {
        return {
        }
      }
    }

  },
  data() {
    return {
      formData: {
      },
      selectRowData: {},
    };
  },
  created() {
    this.formData = { ...this.parentFormData }
    this.checkColumn();
    this.dataOnChange();


  },
  methods:{

    checkColumn() {
      // 请求债券字段
      this.httpUtil.comnQuery({
        action: "AssetCollection.findColumns",
        params: {page: "4"}
      }).then(data => {
        console.log(data.rows)
        let formDatas = {...this.formData}
        for (let f in formDatas) {
          formDatas[f + 'Disabled'] = true;
        }

        if (data && data.rows.length > 0){
          let labels = data.rows[0].label
          let arr = labels.split(',')
          if (arr.length > 0){
            arr.forEach(a =>{
              if(a == 'rEMARK'){
                  formDatas['remarkDisabled'] = false;
               }
               if(a == 'sHAREHOLD'){
                  formDatas['shareholdDisabled'] = false;
               }

              formDatas[a + 'Disabled'] = false
            })

}
        }
        this.formData = { ...formDatas }
      }).catch({})
    },

    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
    },

  dataOnChange(){
    this.httpUtil.comnQuery({
           action: "AssetCollection.findColumns",
           params: {page: "8"}
         }).then(data => {
           if (data && data.rows.length > 0){
             let labels = data.rows[0].label
             let arr = labels.split(',')


           if (arr.length > 0){
              /*股票类型*/
               if(arr.includes('stockType')){
                 if(this.formData.cbndScdCtg == '1302' || this.formData.cbndScdCtg == '1305') {
                   //必填
                  this.formData.stockTypeAllowblank=false;
                  this.formData.stockTypeDisabled=false;
                 }else if(this.formData.cbndScdCtg == '1301'){
                  //不可填写
                 this.$set(this.formData, 'stockType', '');
                 this.formData.stockTypeAllowblank=true;
                 this.formData.stockTypeDisabled=true;
                 }else{
                   this.formData.stockTypeAllowblank=true;
                   this.formData.stockTypeDisabled=false;
                 }
               }
                /*备注*/
                if(arr.includes('rEMARK')){
                  if(this.formData.stockType == '99' || this.formData.investmentType == '99' || this.formData.isuOrgTypEcn == '99') {
                   //必填
                    this.formData.remarkAllowblank=false;
                    this.formData.remarkDisabled=false;
                  }else{
                   //不可填写
                   this.$set(this.formData, 'remark', '');
                   this.formData.remarkAllowblank=true;
                   this.formData.remarkDisabled=true;
                  }
                }
                 //投资阶段
                if(arr.includes('investmentType')){
                  if(this.formData.cbndScdCtg == '1301') {
                   //必填
                    this.formData.investmentTypeAllowblank=false;
                    this.formData.investmentTypeDisabled=false;
                  }else if(this.formData.cbndScdCtg == '1302' || this.formData.cbndScdCtg == '1305'){
                   //不可填写
                   this.$set(this.formData, 'investmentType', '');
                   this.formData.investmentTypeAllowblank=true;
                   this.formData.investmentTypeDisabled=true;
                  }
                }
                //股权退出安排
                 if(arr.includes('sHAREHOLD')){
                    if(this.formData.cbndScdCtg == '1301') {
                     //必填
                      this.formData.shareholdAllowblank=false;
                      this.formData.shareholdDisabled=false;
                    }else if(this.formData.cbndScdCtg == '1302' || this.formData.cbndScdCtg == '1305'){
                     //不可填写
                     this.$set(this.formData, 'sharehold', '');
                     this.formData.shareholdAllowblank=true;
                     this.formData.shareholdDisabled=true;
                    }else{
                     this.formData.shareholdAllowblank=true;
                     this.formData.shareholdDisabled=false;
                    }
                  }

                   if(arr.includes('pledgedFinace')){
                      if(this.formData.cbndScdCtg == '1301' || this.formData.cbndScdCtg == '1302' || this.formData.cbndScdCtg == '1305') {
                       //必填
                       //是否为质押融资
                        this.formData.pledgedFinaceAllowblank=false;
                        this.formData.pledgedFinaceDisabled=false;
                       //是否为债转股
                        this.formData.debtEquitySwapAllowblank=false;
                        this.formData.debtEquitySwapDisabled=false;
                      }else{
                       //不可填写
                        //是否为质押融资
                       this.$set(this.formData, 'pledgedFinace', '');
                       this.formData.pledgedFinaceAllowblank=true;
                       this.formData.pledgedFinaceDisabled=true;
                       //是否为债转股
                       this.$set(this.formData, 'debtEquitySwap', '');
                       this.formData.debtEquitySwapAllowblank=true;
                       this.formData.debtEquitySwapDisabled=true;
                      }
                    }

            }
          }
        }).catch({})
              /****/
    }
  },
};
</script>
