<template>
  <div  class="md-card k-card md-theme-default parent-div" @scroll="scrollEvent" >
    <div class="md-card-header md-card-header-text md-card-header-green" id="mainPanel">
        <!-- <div class="card-icon">
          <i class="md-icon md-icon-font md-theme-default">assignment</i>
        </div> -->

        <div class="display-flex">
          <div class="menu-container" style="top: 87px;">

            <div class="md-message-line">

              <div class="menu-list">
                <div class="menu" v-for="(item,index) in menuItems[prodMode]" :key="index">
                  <div class="icon-container">
                    <div class = "icon-image" :class="item.iconClass" ></div>
                  </div>
                  <div class="desc-container " @click="changeTab(index)"
                    @mouseenter="mouseOnItem(item) "
                    @mouseleave="mouseOutItem(item)"
                    :class="[item.mouseOver, item.alive ? item.activeClass: '']">

                    <span class="desc">{{item.desc}}</span>
                  </div>
                </div>
              </div>
            </div>


          </div>

          <div class="share-container">

            <div @click="saveProduct" size="mini" :loading="saveLoading" class="pd-button">
              <md-icon md-src="/static/images/create/save5.svg" class="pd-icon-20"/>
              <div class="pd-text">保存</div>
            </div>

            <div @click="back2Page" size="mini" class="pd-button" style="margin-left: 11px;">
              <md-icon md-src="/static/images/create/back4.svg" class="pd-icon-20"/>
              <div class="pd-text">返回</div>
            </div>
          </div>

          <div class="formPanel" ref="formPanel">


            <div class="form-item prod-panel" id="prodInfo" v-show="showPanels[prodMode] && showPanels[prodMode].prodInfo">
              <div class="title">
                <div class="prod-items"></div>
                <k-field-display class="title-desc" value="基本信息"></k-field-display>
              </div>

              <ProdInfo ref="prodInfo" v-model="formData.t8ProdInfo"
                      @changeEstablishDates="(value)=>this.formData.t8ProdCalendar.establishDate=value" :establishDate="formData.t8ProdCalendar.establishDate"
                      @changeEndDates="(value)=>this.formData.t8ProdCalendar.endDate=value" :endDate="formData.t8ProdCalendar.endDate"
                         :subsBeginDate="formData.t8ProdCalendar.subsBeginDate" :truteeCode="formData.t8ProdInfo.truteeCode"/>

            </div>

            <div class="form-item prod-panel" id="periodInfo" v-show="showPanels[prodMode] && showPanels[prodMode].periodInfo">
              <div class="title">
                <div class="prod-items"></div>
                <k-field-display class="title-desc" value="周期信息"></k-field-display>
              </div>
              <ProdCalendar ref="periodInfo" v-model="formData.t8ProdCalendar"
                  :prodMode="formData.t8ProdInfo.prodMode"
                  :prodCode="formData.t8ProdInfo.prodCode"
                  :pgmno="formData.t8ProdInfo.pgmno"
                  :updateProduct = "this.updateProduct"/>
            </div>

            <div class="form-item prod-panel" id="limitInfo" v-show="showPanels[prodMode] && showPanels[prodMode].limitInfo">
              <div class="title">
                <div class="prod-items"></div>
                <k-field-display class="title-desc" value="限制信息"></k-field-display>
              </div>
              <ProdLimit ref="limitInfo" v-model="formData.t8ProdLimit"
                @changePromode="(value)=>this.formData.t8ProdInfo.prodMode=value" :prodMode="formData.t8ProdInfo.prodMode"
                @changeRaiseType="(value)=>this.formData.t8ProdInfo.raiseType=value" :raiseType="formData.t8ProdInfo.raiseType" />
            </div>

            <div class="form-item prod-panel" id="openInfo" v-show="showPanels[prodMode] && showPanels[prodMode].openInfo">
              <div class="title">
                <div class="prod-items"></div>
                <k-field-display class="title-desc" value="开放信息"></k-field-display>
              </div>
              <ProdOpen ref="openInfo" v-model="formData.t8ProdInfo" :t8ProdLimit="formData.t8ProdLimit"
                @changeProOpen="(value)=>this.formData.t8ProdLimit=value"  />
            </div>

            <div class="form-item prod-panel" id="navInfo" v-show="showPanels[prodMode] && showPanels[prodMode].navInfo">
              <div class="title">
                <div class="prod-items"></div>
                <k-field-display class="title-desc" value="净值信息"></k-field-display>
              </div>
              <ProdValueInfo ref="navInfo" v-model="formData.t8ProdInfo"/>
            </div>

            <div class="form-item prod-panel" id="capitalInfo" v-show="showPanels[prodMode] && showPanels[prodMode].capitalInfo">
              <div class="title">
                <div class="prod-items"></div>
                <k-field-display class="title-desc" value="精度信息"></k-field-display>
              </div>
              <ProdPrecision ref="capitalInfo" v-model="formData.t8ProdInfo" :t8ProdPrecision="formData.t8ProdPrecision" @changeProdPrecision="(value)=>this.formData.t8ProdPrecision=value"  />
            </div>

            <div class="form-item prod-panel" id="advancePayInfo"  v-show="showPanels[prodMode] && showPanels[prodMode].advancePayInfo">
              <div class="title">
                <div class="prod-items"></div>
                <k-field-display class="title-desc" value="垫资信息"></k-field-display>
              </div>
              <PrjAdvancePay ref="advancePayInfo" v-model="formData.taAdvancePay" :prodCode="formData.t8ProdInfo.prodCode"/>

            </div>

            <div class="form-item prod-panel" id="truteeInfo" v-show="showPanels[prodMode] && showPanels[prodMode].truteeInfo">
              <div class="title">
                <div class="prod-items"></div>
                <k-field-display class="title-desc" value="托管信息"></k-field-display>
              </div>
              <ProdEscrowAcct ref="truteeInfo" v-model="formData.t8ProdEscrowAcct" :truteeCode="formData.t8ProdInfo.truteeCode"/>
            </div>

            <div class="form-item prod-panel" id="tailingInfo" v-show="showPanels[prodMode] && showPanels[prodMode].tailingInfo">
              <div class="title">
                <div class="prod-items"></div>
                <k-field-display class="title-desc" value="尾随佣金"></k-field-display>
              </div>
              <PrjTailing ref="tailingInfo" :prodCode="formData.t8ProdInfo.prodCode"/>
            </div>

            <div class="form-item prod-panel" id="saleFeePayInfo" v-show="showPanels[prodMode] && showPanels[prodMode].saleFeePayInfo">
              <div class="title">
                <div class="prod-items"></div>
                <k-field-display class="title-desc" value="销服支付计划"></k-field-display>
              </div>
              <SaleFeePay ref="SaleFeePay" v-model="formData.SaleFeePay" :prodCode="formData.t8ProdInfo.prodCode"/>
            </div>

            <div class="form-item prod-panel" id="feeDividInfo" v-show="showPanels[prodMode] && showPanels[prodMode].feeDividInfo">
              <div class="title">
                <div class="prod-items"></div>
                <k-field-display class="title-desc" value="费用分成"></k-field-display>
              </div>
              <PrjFeeDivid ref="PrjFeeDivid" v-model="formData.taPrjFeeDivide" :prodCode="formData.t8ProdInfo.prodCode"
                           :prodMode="formData.t8ProdInfo.prodMode"/>
            </div>

            <div class="form-item prod-panel" id="saleServiceInfo" v-show="showPanels[prodMode] && showPanels[prodMode].saleServiceInfo">
              <div class="title">
                <div class="prod-items"></div>
                <k-field-display class="title-desc" value="销售服务费"></k-field-display>
              </div>
              <SaleService ref="saleServiceInfo" v-model="formData.SaleService" :prodCode="formData.t8ProdInfo.prodCode"/>
            </div>

            <div class="form-item prod-panel" id="manageServiceInfo" v-show="showPanels[prodMode] && showPanels[prodMode].manageServiceInfo">
              <div class="title">
                <div class="prod-items"></div>
                <k-field-display class="title-desc" value="管理费"></k-field-display>
              </div>
              <ManageService ref="manageServiceInfo" v-model="formData.ManageService" :prodCode="formData.t8ProdInfo.prodCode"/>
            </div>

            <div class="form-item prod-panel" id="tradeFeeInfo" v-show="showPanels[prodMode] && showPanels[prodMode].tradeFeeInfo">
              <div class="title">
                <div class="prod-items"></div>
                <k-field-display class="title-desc" value="交易费用"></k-field-display>
              </div>

              <PrjTradeFee ref="tradeFeeInfo" v-model="formData.PrjTradeFee" :prodCode="formData.t8ProdInfo.prodCode"/>
            </div>

            <div class="form-item prod-panel" id="feeDiscount" v-show="showPanels[prodMode] && showPanels[prodMode].feeDiscount">
              <div class="title">
                <div class="prod-items"></div>
                <k-field-display class="title-desc" value="折扣率上限"></k-field-display>
              </div>
              <FeeDiscount ref="feeDiscount" :prodCode="formData.t8ProdInfo.prodCode"/>
            </div>

            <div class="form-item prod-panel" id="partPaymentInfo" v-show="showPanels[prodMode] && showPanels[prodMode].partPaymentInfo">
              <div class="title">
                <div class="prod-items"></div>
                <k-field-display class="title-desc" value="兑付方案"></k-field-display>
              </div>
              <PartPayment ref="partPaymentInfo" v-model="formData.PartPayment" :prodCode="formData.t8ProdInfo.prodCode"/>
            </div>

<!--            <div class="form-item prod-panel" id="otherInfo" v-show="showPanels[prodMode] && showPanels[prodMode].otherInfo">
              <div class="title">
                <div class="prod-items"></div>
                <k-field-display class="title-desc" value="附属信息"></k-field-display>
              </div>
              <ProdOtherInfo ref="otherInfo" v-model="formData.taProdAddition"/>
            </div>-->

          </div>
        </div>


    </div>

  </div>


</template>

<script>
  import ProdInfo         from "./M81001-ProdInfo"
  import ProdLimit        from "./M81001-ProdLimit"
  import ProdOtherInfo    from "./M81001-ProdOtherInfo"
  import ProdCrycleInfo   from "./M81001-ProdCrycleInfo.vue"
  import ProdEscrowAcct   from "./M81001-ProdEscrowAcct.vue"
  import ProdOpen         from "./M81001-ProdOpen.vue"
  import ProdPrecision    from "./M81001-ProdPrecision.vue"
  import ProdSarDays      from "./M81001-ProdSarDays.vue"
  import ProdValueInfo    from "./M81001-ProdValueInfo.vue"
  import ProdCalendar     from "./M81001-ProdCalendar.vue"
  import PrjTradeFee      from "./M81001-PrjTradeFee.vue"
  import PrjTailing       from "./M81001-PrjTailing.vue"
  import PrjFeeDivid      from "./M81001-PrjFeeDivid.vue"
  import SaleService      from "./M81001-SaleService.vue"
  import ManageService    from "./M81001-ManageService.vue"
  import SaleFeePay       from "./M81001-SaleFeePay.vue"
  import PrjAdvancePay    from "./M81001-PrjAdvancePay.vue"
  import FeeDiscount      from "./M81001-FeeDiscount.vue"
  import PartPayment      from "./M81001-ProdPartPayment.vue"



  export default {
    name: "M81001add",
    components: {
      ProdInfo,
      ProdCalendar,
      ProdOtherInfo,
      ProdLimit,
      ProdEscrowAcct,
      ProdPrecision,
      ProdValueInfo,
      ProdOpen,
      ProdSarDays,
      PrjTradeFee,
      PrjTailing,
      PrjFeeDivid,
      SaleService,
      ManageService,
      SaleFeePay,
      PrjAdvancePay,
      FeeDiscount,
      PartPayment,
    },
    props:{
      formClass: {
        type: String,
        default: 'height: 500px;'
      },

    },

    computed: {},
    data() {
      return {
        prodMode : "",
        prodCode : "",
        menuItems:{
          "1": [
            { desc: "基本信息",     id: 'prodInfo',               alive: true,   mouseOver: '', validate: true,   iconClass: 'item-base',                   activeClass: "selected-base",               },
            { desc: "周期信息",     id: 'periodInfo',             alive: false,  mouseOver: '', validate: true,   iconClass: 'item-period',                 activeClass: "selected-period",             },
            { desc: "限制信息",     id: 'limitInfo',              alive: false,  mouseOver: '', validate: true,   iconClass: 'item-limit',                  activeClass: "selected-limit",              },
            { desc: "净值信息",     id: 'navInfo',                alive: false,  mouseOver: '', validate: true,   iconClass: 'item-nav',                    activeClass: "selected-nav",                },
            { desc: "精度信息",     id: 'capitalInfo',            alive: false,  mouseOver: '', validate: true,   iconClass: 'item-capital',                activeClass: "selected-capital",            },
            { desc: "托管信息",     id: 'truteeInfo',             alive: false,  mouseOver: '', validate: true,   iconClass: 'item-trutee',                 activeClass: "selected-trutee",             },
            { desc: "尾随佣金",     id: 'tailingInfo',            alive: false,  mouseOver: '', validate: false,  iconClass: 'item-tailing-commision',      activeClass: "selected-tailing-commision",  },
            { desc: "销服支付计划", id: 'saleFeePayInfo',         alive: false,  mouseOver: '', validate: false,  iconClass: 'item-sale-fee-pay',           activeClass: "selected-sale-fee-pay",       },
            { desc: "费用分成",     id: 'feeDividInfo',           alive: false,  mouseOver: '', validate: false,  iconClass: 'item-fee-divid',              activeClass: "selected-fee-divid",          },
            { desc: "销售服务费",   id: 'saleServiceInfo',        alive: false,  mouseOver: '', validate: false,  iconClass: 'item-sale-service',           activeClass: "selected-sale-service",       },
            { desc: "管理费",       id: 'manageServiceInfo',      alive: false,  mouseOver: '', validate: false,  iconClass: 'item-sale-service',           activeClass: "selected-sale-service",       },
            { desc: "交易费用",     id: 'tradeFeeInfo',           alive: false,  mouseOver: '', validate: false,  iconClass: 'item-trade-fee',              activeClass: "selected-trade-fee",          },
            { desc: "折扣率上限",   id: 'feeDiscount',            alive: false,  mouseOver: '', validate: false,  iconClass: 'item-fee',                    activeClass: "selected-fee",          },
            { desc: "兑付方案",   id: 'partPaymentInfo',         alive: false,  mouseOver: '', validate: false,  iconClass: 'item-fee', activeClass: "selected-fee",       },
            // { desc: "附属信息",     id: 'otherInfo',              alive: false,  mouseOver: '', validate: false,   iconClass: 'item-other',                  activeClass: "selected-other",              },
          ],
          "2": [
            { desc: "基本信息",     id: 'prodInfo',               alive: true,   mouseOver: '', validate: true,   iconClass: 'item-base',                   activeClass: "selected-base",               },
            { desc: "周期信息",     id: 'periodInfo',             alive: false,  mouseOver: '', validate: true,   iconClass: 'item-period',                 activeClass: "selected-period",             },
            { desc: "限制信息",     id: 'limitInfo',              alive: false,  mouseOver: '', validate: true,   iconClass: 'item-limit',                  activeClass: "selected-limit",              },
            { desc: "开放信息",     id: 'openInfo',               alive: false,  mouseOver: '', validate: true,   iconClass: 'item-open',                   activeClass: "selected-open",               },
            { desc: "净值信息",     id: 'navInfo',                alive: false,  mouseOver: '', validate: true,   iconClass: 'item-nav',                    activeClass: "selected-nav",                },
            { desc: "精度信息",     id: 'capitalInfo',            alive: false,  mouseOver: '', validate: true,   iconClass: 'item-capital',                activeClass: "selected-capital",            },
            { desc: "托管信息",     id: 'truteeInfo',             alive: false,  mouseOver: '', validate: true,   iconClass: 'item-trutee',                 activeClass: "selected-trutee",             },
            { desc: "尾随佣金",     id: 'tailingInfo',            alive: false,  mouseOver: '', validate: false,  iconClass: 'item-tailing-commision',      activeClass: "selected-tailing-commision",  },
            { desc: "销服支付计划", id: 'saleFeePayInfo',         alive: false,  mouseOver: '', validate: false,  iconClass: 'item-sale-fee-pay',           activeClass: "selected-sale-fee-pay",       },
            { desc: "费用分成",     id: 'feeDividInfo',           alive: false,  mouseOver: '', validate: false,  iconClass: 'item-fee-divid',              activeClass: "selected-fee-divid",          },
            { desc: "销售服务费",   id: 'saleServiceInfo',        alive: false,  mouseOver: '', validate: false,  iconClass: 'item-sale-service',           activeClass: "selected-sale-service",       },
            { desc: "管理费",       id: 'manageServiceInfo',      alive: false,  mouseOver: '', validate: false,  iconClass: 'item-sale-service',           activeClass: "selected-sale-service",       },
            { desc: "交易费用",     id: 'tradeFeeInfo',           alive: false,  mouseOver: '', validate: false,  iconClass: 'item-trade-fee',              activeClass: "selected-trade-fee",          },
            { desc: "折扣率上限",   id: 'feeDiscount',            alive: false,  mouseOver: '', validate: false,  iconClass: 'item-fee',                    activeClass: "selected-fee",          },
            // { desc: "附属信息",     id: 'otherInfo',              alive: false,  mouseOver: '', validate: false,   iconClass: 'item-other',                  activeClass: "selected-other",              },
          ],
          "3": [
            { desc: "基本信息",     id: 'prodInfo',   	          alive: true,   mouseOver: '', validate: true,   iconClass: 'item-base',  		              activeClass: "selected-base",   	          },
            { desc: "周期信息",     id: 'periodInfo', 	          alive: false,  mouseOver: '', validate: true,   iconClass: 'item-period',		              activeClass: "selected-period",         	  },
            { desc: "限制信息",     id: 'limitInfo', 		          alive: false,  mouseOver: '', validate: true,   iconClass: 'item-limit', 		              activeClass: "selected-limit", 		          },
            { desc: "开放信息",     id: 'openInfo', 		          alive: false,  mouseOver: '', validate: true,   iconClass: 'item-open', 		              activeClass: "selected-open", 		          },
            { desc: "净值信息",     id: 'navInfo', 			          alive: false,  mouseOver: '', validate: true,   iconClass: 'item-nav', 			              activeClass: "selected-nav", 			          },
            { desc: "精度信息",     id: 'capitalInfo', 	          alive: false,  mouseOver: '', validate: true,   iconClass: 'item-capital', 	              activeClass: "selected-capital", 	          },
            { desc: "垫资信息",     id: 'advancePayInfo',         alive: false,  mouseOver: '', validate: false,  iconClass: 'item-fee', 			              activeClass: "selected-fee",                },
            { desc: "托管信息",     id: 'truteeInfo', 	          alive: false,  mouseOver: '', validate: true,   iconClass: 'item-trutee', 	              activeClass: "selected-trutee", 	          },
            { desc: "尾随佣金",     id: 'tailingInfo', 	          alive: false,  mouseOver: '', validate: false,  iconClass: 'item-tailing-commision', 			activeClass: "selected-tailing-commision",  },
            { desc: "销服支付计划", id: 'saleFeePayInfo', 			  alive: false,  mouseOver: '', validate: false,  iconClass: 'item-sale-fee-pay', 			    activeClass: "selected-sale-fee-pay",       },
            { desc: "费用分成",     id: 'feeDividInfo', 			    alive: false,  mouseOver: '', validate: false,  iconClass: 'item-fee-divid', 			        activeClass: "selected-fee-divid", 			    },
            { desc: "销售服务费",   id: 'saleServiceInfo', 			  alive: false,  mouseOver: '', validate: false,  iconClass: 'item-sale-service', 			    activeClass: "selected-sale-service", 			},
            { desc: "管理费",       id: 'manageServiceInfo',      alive: false,  mouseOver: '', validate: false,  iconClass: 'item-sale-service',           activeClass: "selected-sale-service",       },
            { desc: "交易费用",     id: 'tradeFeeInfo', 			    alive: false,  mouseOver: '', validate: false,  iconClass: 'item-trade-fee', 			        activeClass: "selected-trade-fee", 			    },
            { desc: "折扣率上限",   id: 'feeDiscount',            alive: false,  mouseOver: '', validate: false,  iconClass: 'item-fee',                    activeClass: "selected-fee",          },
            // { desc: "附属信息",     id: 'otherInfo', 		          alive: false,  mouseOver: '', validate: false,   iconClass: 'item-other', 		              activeClass: "selected-other",		          },
          ],
          "4": [
            { desc: "基本信息",     id: 'prodInfo',               alive: true,   mouseOver: '', validate: true,   iconClass: 'item-base',                   activeClass: "selected-base",               },
            { desc: "周期信息",     id: 'periodInfo',             alive: false,  mouseOver: '', validate: true,   iconClass: 'item-period',                 activeClass: "selected-period",             },
            { desc: "限制信息",     id: 'limitInfo',              alive: false,  mouseOver: '', validate: true,   iconClass: 'item-limit',                  activeClass: "selected-limit",              },
            { desc: "开放信息",     id: 'openInfo',               alive: false,  mouseOver: '', validate: true,   iconClass: 'item-open',                   activeClass: "selected-open",               },
            { desc: "净值信息",     id: 'navInfo',                alive: false,  mouseOver: '', validate: true,   iconClass: 'item-nav',                    activeClass: "selected-nav",                },
            { desc: "精度信息",     id: 'capitalInfo',            alive: false,  mouseOver: '', validate: true,   iconClass: 'item-capital',                activeClass: "selected-capital",            },
            { desc: "托管信息",     id: 'truteeInfo',             alive: false,  mouseOver: '', validate: true,   iconClass: 'item-trutee',                activeClass: "selected-trutee",             },
            { desc: "尾随佣金",     id: 'tailingInfo',            alive: false,  mouseOver: '', validate: false,  iconClass: 'item-tailing-commision',      activeClass: "selected-tailing-commision",  },
            { desc: "销服支付计划", id: 'saleFeePayInfo',         alive: false,  mouseOver: '', validate: false,  iconClass: 'item-sale-fee-pay',           activeClass: "selected-sale-fee-pay",       },
            { desc: "费用分成",     id: 'feeDividInfo',           alive: false,  mouseOver: '', validate: false,  iconClass: 'item-fee-divid',              activeClass: "selected-fee-divid",          },
            { desc: "销售服务费",   id: 'saleServiceInfo',        alive: false,  mouseOver: '', validate: false,  iconClass: 'item-sale-service',           activeClass: "selected-sale-service",       },
            { desc: "管理费",       id: 'manageServiceInfo',      alive: false,  mouseOver: '', validate: false,  iconClass: 'item-sale-service',           activeClass: "selected-sale-service",       },
            { desc: "交易费用",     id: 'tradeFeeInfo',           alive: false,  mouseOver: '', validate: false,  iconClass: 'item-trade-fee',              activeClass: "selected-trade-fee",          },
            { desc: "折扣率上限",   id: 'feeDiscount',            alive: false,  mouseOver: '', validate: false,  iconClass: 'item-fee',                    activeClass: "selected-fee",          },
            // { desc: "附属信息",     id: 'otherInfo',              alive: false,  mouseOver: '', validate: false,   iconClass: 'item-other',                  activeClass: "selected-other",              },
          ]
        },
        saveLoading: false,

        showPanels:{
          "1": {
                 prodInfo:              true,
                 periodInfo:            true,
                 limitInfo:             true,
                 openInfo:              false,
                 navInfo:               true,
                 capitalInfo:           true,
                 advancePayInfo:        false,
                 truteeInfo:            true,
                 tailingInfo:           true,
                 saleFeePayInfo:        true,
                 feeDividInfo:          true,
                 saleServiceInfo:       true,
                 manageServiceInfo:     true,
                 tradeFeeInfo:          true,
                 feeDiscount:           true,
                 otherInfo:             true,
                 partPaymentInfo:       true,
               },
          "2": {
                 prodInfo:              true,
                 periodInfo:            true,
                 limitInfo:             true,
                 openInfo:              true,
                 navInfo:               true,
                 capitalInfo:           true,
                 advancePayInfo:        false,
                 truteeInfo:            true,
                 tailingInfo:           true,
                 saleFeePayInfo:        true,
                 feeDividInfo:          true,
                 saleServiceInfo:       true,
                 manageServiceInfo:     true,
                 tradeFeeInfo:          true,
                 feeDiscount:           true,
                 otherInfo:             true,
               },
          "3": {
                 prodInfo:              true,
                 periodInfo:            true,
                 limitInfo:             true,
                 openInfo:              true,
                 navInfo:               true,
                 capitalInfo:           true,
                 advancePayInfo:        true,
                 truteeInfo:            true,
                 tailingInfo:           true,
                 saleFeePayInfo:        true,
                 feeDividInfo:          true,
                 saleServiceInfo:       true,
                 manageServiceInfo:     true,
                 tradeFeeInfo:          true,
                 feeDiscount:           true,
                 otherInfo:             true,
               },
          "4": {
                 prodInfo:              true,
                 periodInfo:            true,
                 limitInfo:             true,
                 openInfo:              true,
                 navInfo:               true,
                 capitalInfo:           true,
                 advancePayInfo:        false,
                 truteeInfo:            true,
                 tailingInfo:           true,
                 saleFeePayInfo:        true,
                 feeDividInfo:          true,
                 saleServiceInfo:       true,
                 manageServiceInfo:     true,
                 tradeFeeInfo:          true,
                 feeDiscount:           true,
                 otherInfo:             true,
               },
        },

        updateProduct : false,

        formData: {
          t8ProdInfo: {
            prodCode            : "",
            prodMode            : "",
            interestConvertType : "",
            subsCapitalType     : "",
            subsIsInterest      : "",
            updateProduct       : false,
            prodDays            : '',
            subsInterestEndDate : '',
            defDivMethod        : "",
            divChgFlag          : "",
            subsQuotaType       : "",
            taskGroup           : "",
            truteeCode          : "",
          },
          t8ProdLimit: {
            prodCode : "",
            smallVolMtc: "",
            prodSaleCustom: "",
          },
          t8ProdPrecision: {
            prodCode : ""
          },
          t8ProdEscrowAcct: {
            prodCode : ""
          },
          t8ProdAddition: {
            prodCode : ""
          },
          t8ProdOpen: {
            prodCode : ""
          },
          t8ProdPeriod : {
            prodCode: ""
          },
          t8ProdCalendar : {
            prodCode : "",
            establishDate : "",
            endDate : "",
            subsBeginDate:""
          },
          t8ClearGroupMember : {
            oldTaskGroup: "",
            taskGroup: "",
            groupMember: "",
            execTaskType: "2"
          }

        }

      }
    },
    methods: {

      changeTab: function(index) {

        this.menuItems[this.prodMode].forEach((item, i) => {

          if (i == index) {
            // 滑动到目的地址
            document.getElementById(item.id).scrollIntoView({
                block:'start',
                inline:'nearest',
                behavior:'smooth'
            })
          }
        })
      },

      mouseOnItem(item){
        item.mouseOver = item.activeClass
      },


      mouseOutItem(item){
        item.mouseOver = ''
      },


      back2Page(){

        let backPath = '/main/pms/M81/M81007';
/*        if (this.updateProduct){
          backPath = '/main/TA/TA1/T81007';
        }*/

        this.$router.push({
          path: backPath,
          query: {findProdCode:this.findProdCode, findProdName:this.findProdName, findProdMode:this.findProdMode, findProdLifecycle:this.findProdLifecycle,},
        });

      },

      saveProduct() {

        this.saveLoading = true;
        var isComplite = true; // 校验是否均通过

        // 先校验输入参数是否符合要求
        for (let menuItem of this.menuItems[this.prodMode]) {

          if (menuItem.validate){

            //console.log(menuItem.id);
            let validateResult = this.$refs[menuItem.id].validateData()

            if (!validateResult && isComplite) { // 验证所有的表格
              this.saveLoading = false;

              isComplite = false;

              // 滑动到目的地址
              document.getElementById(menuItem.id).scrollIntoView({
                  block:'start',
                  inline:'nearest',
                  behavior:'smooth'
              })

            }
          }

        }

        if (isComplite){ // 全部输入校验通过了，才保存

          if(this.updateProduct){
            //标识 true 为新增，false为修改
            //Object.assign(目标对象, 被合并的对象)
            //设置产品批次
            this.formData.t8ClearGroupMember.taskGroup = this.formData.t8ProdInfo.taskGroup;
            this.formData.t8ClearGroupMember.groupMember = this.formData.t8ProdInfo.prodCode;
            let array = [];
            for (let v in this.formData) {
              //this.formData[v] = JSON.stringify(this.formData[v]);
              array[v] = JSON.stringify(this.formData[v]);
            }

            // 设置周期信息
            let periodInfos = this.$refs.periodInfo.returnData();

            for (let v in periodInfos) {
              //this.formData[v] = JSON.stringify(periodInfos[v]);
              array[v] = JSON.stringify(periodInfos[v]);
            }
            this.httpUtil.comnUpdate({
              action: 'T8ProdAllInfo.updateTaProdInfo',
              params: array,
              successAlert: true
            }).then(data => {
              this.saveLoading = false;
              if(data.success==true){
                this.back2Page();
              }
            });
          } else {
            //设置产品批次
            this.formData.t8ClearGroupMember.taskGroup = this.formData.t8ProdInfo.taskGroup;
            this.formData.t8ClearGroupMember.groupMember = this.formData.t8ProdInfo.prodCode;
            for (let v in this.formData) {
              this.formData[v] = JSON.stringify(this.formData[v]);
            }

            // 设置周期信息
            let periodInfos = this.$refs.periodInfo.returnData();

            for (let v in periodInfos) {
              this.formData[v] = JSON.stringify(periodInfos[v]);
            }
            this.httpUtil.comnUpdate({
              action: 'T8ProdAllInfo.addTaProdInfo',
              params: this.formData,
              successAlert: true
            }).then(data => {
              this.saveLoading = false;
              if(data.success==true){
                this.back2Page();
              }
            });
          }
        }




      },
      // changeSmallVolMtc : function(){
      //
      //   this.formData.taProdLimit.redeemRatio = null
      // },
      // changeSubsIsInterest : function(value){
      //   if(value=='0'){
      //     this.formData.T8ProdInfo.subsInterestYeardays = null;
      //     this.formData.T8ProdInfo.subsInterestStartDate = null;
      //     this.formData.T8ProdInfo.subsInterestEndDate = null;
      //     this.formData.T8ProdInfo.subsInterestDealMode = null;
      //   }
      // },
      // changeDefDivMethod : function(value){
      //   if(value != '1'){
      //     this.formData.T8ProdInfo.minDivAmt = null;
      //   }else{
      //     this.formData.T8ProdInfo.minDivAmt = '0.01';
      //   }
      // },
      scrollEvent () {
          this.menuItems[this.prodMode].forEach((item, i) => {
            let heigthTop = document.getElementById(item.id).getBoundingClientRect().top
            if(heigthTop<=100){
              item.alive = true;
            }else{
              item.alive = false;
            }
            if(i!=0 && item.alive == true){
              this.menuItems[this.prodMode][i-1].alive = false;
            }
          });
      }

    },
    created() {
      //console.log("this.$route.query" + JSON.stringify(this.$route.query))
      this.prodMode = this.$route.query.prodMode;               // 产品模型
      this.prodCode = this.$route.query.prodCode;               // 产品代码
      this.prodSaleCustom = this.$route.query.prodSaleCustom;   // 客户类型
      this.subsCapitalType = this.$route.query.subsCapitalType; // 认购申请导入方式
      this.subsIsInterest = this.$route.query.subsIsInterest;   // 认购计息方式
      this.defDivMethod = this.$route.query.defDivMethod;      // 默认分红方式
      this.divChgFlag = this.$route.query.divChgFlag;          // 是否允许变更分红方式
      this.subsQuotaType = this.$route.query.subsQuotaType;    // 发型规模控制方式

      if ( this.$route.query.findProdCode != null ) {    // 查询传值 产品代码
        this.findProdCode = this.$route.query.findProdCode;
      }
      else {
        this.findProdCode = "";
      }
      if ( this.$route.query.findProdName != null ) {    // 查询传值 产品名称
        this.findProdName = this.$route.query.findProdName;
      }
      else {
        this.findProdName = "";
      }
      if ( this.$route.query.findProdMode != null ) {    // 查询传值 产品模型
        this.findProdMode = this.$route.query.findProdMode;
      }
      else {
        this.findProdMode = "";
      }
      if ( this.$route.query.findProdLifecycle != null ) {    // 查询传值 产品状态
        this.findProdLifecycle = this.$route.query.findProdLifecycle;
      }
      else {
        this.findProdLifecycle = "";
      }


      this.formData.t8ProdInfo.prodMode = this.prodMode;
      this.formData.t8ProdInfo.subsCapitalType = this.subsCapitalType;
      this.formData.t8ProdInfo.subsIsInterest = this.subsIsInterest;
      this.formData.t8ProdInfo.defDivMethod = this.defDivMethod;
      this.formData.t8ProdInfo.divChgFlag = this.divChgFlag;
      this.formData.t8ProdInfo.subsQuotaType = this.subsQuotaType;
      this.formData.t8ProdLimit.prodSaleCustom = this.prodSaleCustom;


      if(this.prodCode != null && this.prodCode != ""){
        //标识 true 为修改，false 为新增
        this.updateProduct = true;
        this.formData.t8ProdInfo.updateProduct = true;
        this.formData.t8ProdInfo.prodCode = this.prodCode;
        this.formData.t8ProdLimit.prodCode = this.prodCode;
        this.formData.t8ProdPrecision.prodCode = this.prodCode;
        this.formData.t8ProdEscrowAcct.prodCode = this.prodCode;
        this.formData.t8ProdAddition.prodCode = this.prodCode;
        this.formData.t8ProdOpen.prodCode = this.prodCode;
        this.formData.t8ProdPeriod.prodCode = this.prodCode;


        // 查询产品信息
        if (this.showPanels[this.prodMode].prodInfo){

          this.httpUtil.comnQuery({
            action: 'T8ProdInfo.findTaProdInfos',
            params: {
              prodCode : this.prodCode
            }
          }).then(data => {

            if(data.rows.length > 0 ){
              this.formData.t8ProdInfo = data.rows[0];
            }
          });
        }


        // 查询限制信息
        if (this.showPanels[this.prodMode].limitInfo){

          this.httpUtil.comnQuery({
            action: 'T8ProdLimit.findT8ProdLimits',
            params: {
              prodCode : this.prodCode
            }
          }).then(data => {

            if(data.rows.length > 0 ){
              this.formData.t8ProdLimit = data.rows[0];
            }
          });

        }

        // 查询精度信息
        if (this.showPanels[this.prodMode].capitalInfo){

          this.httpUtil.comnQuery({
            action: 'T8ProdPrecision.findTaProdPrecisions',
            params: {
              prodCode : this.prodCode
            }
          }).then(data => {

            if(data.rows.length > 0 ){
              this.formData.t8ProdPrecision = data.rows[0];
            }
          });
        }

        // 查询托管账户信息
        if (this.showPanels[this.prodMode].truteeInfo){

          this.httpUtil.comnQuery({
            action: 'T8ProdEscrowAcct.findT8ProdEscrowAccts',
            params: {
              prodCode : this.prodCode
            }
          }).then(data => {

            if(data.rows.length > 0 ){
              this.formData.t8ProdEscrowAcct = data.rows[0];
            }
          });

        }

        if (this.showPanels[this.prodMode].otherInfo){

          this.httpUtil.comnQuery({
            action: 'T8ProdAddition.findT8ProdAdditions',
            params: {
              groupMember : this.prodCode
            }
          }).then(data => {

            if(data.rows.length > 0 ){
              this.formData.t8ProdAddition = data.rows[0];
            }
          });

        }


        if (this.showPanels[this.prodMode].openInfo){

          this.httpUtil.comnQuery({
            action: 'T8ProdOpen.findT8ProdOpens',
            params: {
              prodCode : this.prodCode
            }
          }).then(data => {

            if(data.rows.length > 0 ){
              this.formData.t8ProdOpen = data.rows[0];
            }
          });
        }


        if (this.showPanels[this.prodMode].periodInfo){

          this.httpUtil.comnQuery({
            action: 'T8ProdPeriod.findT8ProdPeriods',
            params: {
              prodCode : this.prodCode
            }
          }).then(data => {

            if(data.rows.length > 0 ){
              this.formData.t8ProdPeriod = data.rows[0];
            }
          });

        }

        //产品批次
        if (this.showPanels[this.prodMode].periodInfo){

                  this.httpUtil.comnQuery({
                    action: 'T8ClearGroupMember.queryTaClearGroupMember',
                    params: {
                      groupMember : this.prodCode
                    }
                  }).then(data => {

                    if(data.rows.length > 0 ){
                      this.formData.t8ProdInfo.taskGroup = data.rows[0].taskGroup;
                      this.formData.t8ClearGroupMember.oldTaskGroup = data.rows[0].taskGroup;
                    }
                  });

                }


      } else {
        this.formData.t8ProdInfo.updateProduct = false;
      }

    },
    mounted() {
      this.$refs.formPanel.style['height'] = (document.body.clientHeight - 112)+ 'px';
    },
    model:{
      prop: 'prodMode',
      event: 'input'
    },
    watch: {
      prodCode: {
        handler: function(val) {
          this.$emit('pordDetail', val);
        }
      },
    },

  }
</script>

<style lang="scss" scoped>

@import "../../../styles/T81001.scss";

</style>
