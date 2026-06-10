<template>
  <div class="md-card k-card md-theme-default parent-div" @scroll="scrollEvent">
    <div class="md-card-header md-card-header-text md-card-header-green" id="mainPanel">
      <!-- <div class="card-icon">
        <i class="md-icon md-icon-font md-theme-default">assignment</i>
      </div> -->

      <div class="display-flex">
        <div class="menu-container" style="top: 87px;">
          <!--  目录渲染  -->
          <div class="md-message-line">

            <div class="menu-list">
              <div class="menu" v-for="(item,index) in menuItems" :key="index">
                <div class="icon-container">
                  <div class="icon-image" :class="item.iconClass"></div>
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

        <div :class="state ? 'share-container' : 'share-containerAdd'">

          <div @click="saveProduct('T8ProdAllInfo.addT8ProdAllInfoCopy')" size="mini" class="pd-button"
               v-show="menuName == 'M81007Copy' && state" :data-disabled="saveLoading">
            <i v-show="saveLoading" class="el-icon-loading"/>
            <md-icon md-src="/static/images/create/save5.svg" v-show="!saveLoading" class="pd-icon-20"/>
            <div v-show="!saveLoading" style="margin-top: 4px;font-size: 8px;padding-left: 13px;">暂存</div>
          </div>

          <div @click="saveProduct('T8ProdAllInfo.addT8ProdAllInfo')" size="mini" class="pd-button"
               v-show="menuName == 'M81007' && state" :data-disabled="saveLoading">
            <i v-show="saveLoading" class="el-icon-loading"/>
            <md-icon md-src="/static/images/create/save5.svg" v-show="!saveLoading" class="pd-icon-20"/>
            <div v-show="!saveLoading" style="margin-top: 4px;font-size: 8px;padding-left: 13px;">送审</div>
          </div>


          <div @click="saveProduct('T8ProdAllMeetingConfirm.addT8ProdAllInfo')" size="mini" class="pd-button"
               v-show="menuName == 'ProdMeetingList' && state" :data-disabled="saveLoading">
            <i v-show="saveLoading" class="el-icon-loading"/>
            <md-icon md-src="/static/images/create/save5.svg" v-show="!saveLoading" class="pd-icon-20"/>
            <div v-show="!saveLoading" style="margin-top: 4px;font-size: 8px;padding-left: 13px;">确认</div>
          </div>

          <!--申报参数确认-->
          <div @click="saveProduct('T8ProdAllDeclareConfirm.addT8ProdAllInfo')" size="mini" class="pd-button"
               v-show="menuName == 'ProdDeclareParamList' && state " :data-disabled="saveLoading">
            <i v-show="saveLoading" class="el-icon-loading"/>
            <md-icon md-src="/static/images/create/save5.svg" v-show="!saveLoading" class="pd-icon-20"/>
            <div v-show="!saveLoading" style="margin-top: 4px;font-size: 8px;padding-left: 13px;">确认</div>
          </div>

          <div @click="saveProduct('T8ProdAllAdjustConfirm.addT8ProdAllInfo')" size="mini" class="pd-button"
               v-show="menuName=='ProdIssueAdjustList' && state" :data-disabled="saveLoading">
            <i v-show="saveLoading" class="el-icon-loading"/>
            <md-icon md-src="/static/images/create/save5.svg" v-show="!saveLoading" class="pd-icon-20"/>
            <div v-show="!saveLoading" style="margin-top: 4px;font-size: 8px;padding-left: 13px;">确认</div>
          </div>

          <div @click="back2Page(1)" size="mini" class="pd-button" style="margin-left: 11px;">
            <md-icon md-src="/static/images/create/back4.svg" class="pd-icon-20"/>
            <div class="pd-text" style=" margin-top: 2px;">返回</div>
          </div>

        </div>


        <div class="formPanel" ref="formPanel">
          <M81001ProdInfoDisplay ref="M81001ProdInfoDisplay" :formData="formData"
                                 v-if="menuName != 'M81007' && menuName != 'ProdMeetingList' && menuName != 'M81007Copy'"/>

          <M81001ProdInfoAdd ref="M81001ProdInfoAdd" :formData="formData" @isShowButton="isProdParamsCount"
                             v-if="menuName == 'M81007' || menuName == 'ProdMeetingList' || menuName == 'M81007Copy'"/>

          <div class="form-item prod-panel" id="declarationInfo" v-if="ifDeclarationInfo">
            <div class="title">
              <div class="prod-items"></div>
              <k-field-display class="title-desc" value="申报要素信息"></k-field-display>
            </div>
            <T8DeclarationInfo ref="declarationInfo" v-model="formData.T8DeclarationInfo"
                               :T8DeclarationInfo="formData.T8DeclarationInfo" :T8ProdInfo="formData.T8ProdInfo"
                               :prodCode="formData.T8ProdInfo.prodCode" :t8ProdInfoId="formData.T8ProdInfo.id"
                               :t8TruteeInfoId="formData.T8ProdEscrowAcct.t8TruteeInfoId"
                               :menuName="formData.menuName" @isShowButton="isDeclareParamsCount"/>
          </div>

          <div class="form-item prod-panel" style="height:600px;" v-if="formData.assemblyMenuType=='2'"/>

          <div class="form-item prod-panel" id="prodIssueAdjust" v-if="ifProdIssueAdjust">
            <div class="title">
              <div class="prod-items"></div>
              <k-field-display class="title-desc" value="发行要素信息"></k-field-display>
            </div>
            <ProdIssueAdjust ref="prodIssueAdjust" :menuName="formData.menuName"
                             v-model="formData.T8ProdIssueRegisFields"
                             :prodCode="formData.T8ProdInfo.prodCode" :t8ProdInfoId="formData.T8ProdInfo.id"
                             :T8ProdIssueRegisFields="formData.T8ProdIssueRegisFields"
                             :prodMode="formData.T8ProdInfo.prodMode"
                             @isShowButton="isAdjustParamsCount"/>
          </div>
          <div class="form-item prod-panel" style="height:600px;" v-if="formData.assemblyMenuType=='3'"/>
        </div>

      </div>

      <k-popup ref="prodProcessPopup" data-title="流程设置" :dataDialogDrag="true" @data-close="close(1)">
        <k-form ref="prodProcessForm" :data-col="1">
          <k-field-text v-model="prodProcessForm.prod_code" v-show="false"/>
          <k-form-item label="是否系列过会">
            <k-field-radio v-model="prodProcessForm.isSeries" data-dict="1yes0no" data-default-value="0"
                           @data-on-change="changeDefaultValue" :dataAllowblank="false"/>
          </k-form-item>
          <k-form-item label="产品系列" v-if="prodCreateInfo.isSeries == '1'">
            <k-field-select v-model="prodProcessForm.t8ProdSeriesId" data-action="T8Dict.findSonSeriesInfos"
                            data-display-field="seriesName" data-value-field="seriesCode" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="系列说明"
                       v-if="prodCreateInfo.isSeries == '1'" data-input-width="300px">
            <k-field-text v-model="prodProcessForm.seriesExplain" inputType="textarea" :rows="2" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="流程任务:">
            <k-field-select v-model="prodProcessForm.wf_flow_template_id"
                            data-action="TaskFuncConfig.queryProcessTaskMod"
                            data-value-field="id" data-display-field="name"
                            data-default-value="ff733c5eb9c047aba933ffdd7fbe78a5" :data-allowblank="false"
                            :data-disabled="true"/>
          </k-form-item>
          <div style="height:40px;margin:0 auto;padding-top: 10px;">
            <span style="color:red;font-size: 16px;">流程开启后,产品系列不支持修改,请悉知！！！</span>
          </div>

        </k-form>
        <div style="text-align: right;">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-handler="saveProdProcess"><md-icon md-src="/static/svg/confirm.svg"></md-icon>保存
          </k-btn>
        </div>
      </k-popup>


    </div>

  </div>


</template>

<script>
    import M81001ProdInfoAdd from "./M81001ProdInfoAdd.vue";
    import M81001ProdInfoDisplay from "../prodDisplay/M81001ProdInfoDisplay.vue";
    import T8DeclarationInfo from "./M81001-DeclarationInfo.vue";
    import ProdIssueAdjust from "./M81001-ProdIssueAdjust.vue";
    import Tools from '@/utils/tools.js';
    import eventBus from "@/utils/eventBus";
    import store from "@/store/modules/system";

    export default {
        name: "prodInfoGDM81001add",
        components: {
            M81001ProdInfoAdd,
            M81001ProdInfoDisplay,
            T8DeclarationInfo,
            ProdIssueAdjust
        },
        props: {
            formClass: {
                type: String,
                default: 'height: 500px;'
            },

        },

        computed: {},
        data() {
            return {
                prodNameCopy: '',
                prodCodeCopy: '',
                prodCreateInfo: {},
                prodInfoId: "",
                prodMode: "",
                prodModeId: "",
                prodCode: "",
                tempMneu: []/*菜单组件信息,是否份额分类控制菜单显示隐藏使用*/,
                tempMneuIndex: []/*菜单组件信息对应的下标*/,
                resultChar: "",
                successCount: 0,
                menuItems: [],//菜单渲染数组
                showPanels: {},//表单渲染
                formDataShowPanels: {},
                ifDeclarationInfo: false,
                ifProdIssueAdjust: false,
                updateProduct: false,//状态，是否为不为新增
                assemblyId: '',
                prodProcessForm: {
                    wf_flow_template_id: '',
                    isSeries: '',
                    prod_code: '',
                    t8ProdSeriesId:'',
                    seriesExplain:''
                }, //流程任务

                formData: {
                    //产品基本信息
                    T8ProdInfo: {
                        id: "",
                        prodCode: "",
                        prodMode: "",
                        raiseType: "",
                        pgmno: "",
                        updateProduct: false,

                    },

                    T8ProdEscrowAcct: {
                        prodCode: "",
                        t8ProdInfoId: "",
                    },

                    T8ProdPerformance: {
                        prodCode: "",
                        t8ProdInfoId: "",
                        moneyList: [],
                        switchSegmentValue: "",
                        tailingCommisionList: [],
                        tailingCommisionMoneyList: [],
                    },

                    ProdOtherInfo: {
                        prodCode: "",
                        t8ProdInfoId: ""
                    },
                    PrjFeeDivide: {
                        prodCode: "",
                        t8ProdInfoId: ""
                    },
                    ProdFeeDeal: {
                        dataParams: [],
                        prodCode: "",
                        t8ProdInfoId: ""
                    },
                    ProdFee: {
                        dataParams: [],
                        prodCode: "",
                        t8ProdInfoId: ""
                    },
                    ProdInvest: {
                        prodCode: "",
                        t8ProdInfoId: ""
                    },
                    ProdValuation: {
                        prodCode: "",
                        t8ProdInfoId: "",
                        prodMode: ""
                    },
                    T8ProdCalendar: {
                        prodCode: "",
                        t8ProdInfoId: ""
                    },
                    T8ProdLimit: {
                        prodCode: "",
                        t8ProdInfoId: ""
                    },
                    T8ProdBonus: {
                        prodCode: "",
                        t8ProdInfoId: ""
                    },
                    T8DeclarationInfo: {
                        prodCode: "",
                        t8ProdInfoId: ""
                    },
                    T8ProdDocMods: {
                        dataParams: [],
                        prodCode: "",
                        t8ProdInfoId: ""
                    },
                    T8ProdIssueRegisFields: {
                        prodCode: "",
                        t8ProdInfoId: "",
                        manageMethod: "",
                        isStructural: "",
                        openMod: "",
                        irstOpenStartDate: ""

                    },
                    T8ProdShareSort: {
                        dataParams: [],
                        prodCode: "",
                        t8ProdInfoId: "",
                    },
                    showPanels: {},
                    assemblyMenuType: '',
                    menuName: '',
                },
                isShareSort: false,
                menuName: '',
                state: false,
                assemblyMenuType: '',
                saveLoading: false,//保存按钮是否可点击

            }
        },
        methods: {
          close(value) {
            //console.log('close========>');
            let backPath='';
            if(value===1){
              backPath = '/main/pms/M81/M81007';
              this.$router.push({
                path: backPath,
                query: {},
              });
            }
          },
            //目录点击事件
            changeTab: function (index) {

                this.menuItems.forEach((item, i) => {

                    if (i == index) {
                        this.$nextTick(() => {
                            // 滑动到目的地址
                            //通过传递参数跳转到指定位置
                            //document.getElementById("feeDeal").scrollIntoView({
                            document.getElementById(item.id).scrollIntoView({
                                block: 'start',
                                inline: 'nearest',
                                behavior: 'smooth'
                            })
                        })
                    }
                })
            },

            changeTabProp: function (assemblyId) {
                this.$nextTick(() => {
                    // 滑动到目的地址
                    //通过传递参数跳转到指定位置
                    document.getElementById(assemblyId).scrollIntoView({
                        block: 'start',
                        inline: 'nearest',
                        behavior: 'auto'
                    })
                })
            },

            // 目录渲染
            mouseOnItem(item) {
                item.mouseOver = item.activeClass
            },

            // 目录渲染
            mouseOutItem(item) {
                item.mouseOver = ''
            },

            //返回按钮点击事件
            back2Page(value) {
                //console.log("value=:>>>>>",value);
                let backPath = '';
                //如果是克隆产品，那么需要提示他配置流程
                if (this.menuName == 'M81007Copy') {
                  //console.log("this.$refs.M81001ProdInfoAdd.$refs.prodInfo.prodSeries",this.$refs.M81001ProdInfoAdd.$refs.prodInfo.$refs.T8ProdInfo);
                  //console.log("this.$refs.M81001ProdInfoAdd.$refs.prodInfo.prodSeries",this.$refs.M81001ProdInfoAdd.$refs.prodInfo);
                  //console.log("this.$refs.M81001ProdInfoAdd.$refs.prodInfo.prodSeries",this.$refs.M81001ProdInfoAdd.$refs.prodInfo.T8ProdInfo.prodSonSeries);
                    this.prodProcessForm.prod_code = this.formData.T8ProdInfo.prodCode;
                    this.prodProcessForm.wf_flow_template_id = this.$route.query.modelId;
                    this.prodProcessForm.t8ProdSeriesId = this.$refs.M81001ProdInfoAdd.$refs.prodInfo.T8ProdInfo.prodSonSeries;
                    if(value!=1){
                      this.$refs.prodProcessPopup.popup();
                    }else{
                      backPath = '/main/pms/M81/M81007';
                      this.$router.push({
                        path: backPath,
                        query: {},
                      });
                    }

                } else {
                    if (this.menuName == 'ProdMeetingList') {//会后参数确认
                        backPath = '/main/pms/M81/prodConfirm/ProdMeetingList';
                    } else if (this.menuName == 'ProdDeclareParamList') {//申报参数确认
                        backPath = '/main/pms/M81/prodConfirm/ProdDeclareParamList';
                    } else if (this.menuName == 'ProdIssueAdjustList') {//发行参数确认
                        backPath = '/main/pms/M81/prodConfirm/ProdIssueAdjustList';
                    } else if (this.menuName == 'M81007' || this.menuName == 'M81007Copy') {//创设、克隆
                        backPath = '/main/pms/M81/M81007';
                    }
                    this.$router.push({
                        path: backPath,
                        query: {},
                    });
                }
            },

            //@scroll
            scrollEvent() {
                this.menuItems.forEach((item, i) => {
                    let heigthTop = document.getElementById(item.id).getBoundingClientRect().top
                    if (heigthTop <= 100) {
                        item.alive = true;
                    } else {
                        item.alive = false;
                    }
                    if (i != 0 && item.alive == true) {
                        this.menuItems[i - 1].alive = false;
                    }
                });
            },

            //保存流程任务模板
            saveProdProcess() {
                if (!this.prodProcessForm.wf_flow_template_id) {
                    Tools.alert("请先选择流程任务！！！")
                    return false;
                }
                this.httpUtil.comnQuery({
                    action: "T8ProdFlow.saveProcessTask",
                    params: this.prodProcessForm,
                    mask: true
                }).then(data => {
                    this.$refs.prodProcessPopup.close();
                    Tools.closeCurrentWindow(this);
                    //打开编辑流程任务页面
                    this.$router.push({
                        path: "/main/flow/flowTemplateItem",
                        query: {
                            id: data.returndata.wf_flow_template_id,
                            prod_code: this.prodProcessForm.prod_code,
                            needClose:'1',
                        }
                    })
                });
                return true;
            },

//保存
            saveProduct(url) {
                this.saveLoading = true;
                let isCompLite = true; // 校验是否均通过

                for (let menuItem of this.menuItems) {
                    if (menuItem.validate) {

                        let validateResult = false;
                        if (this.menuName == 'M81007' || this.menuName == 'ProdMeetingList' || this.menuName == 'M81007Copy') {//创设和会后参数、克隆
                            validateResult = this.$refs.M81001ProdInfoAdd.$refs[menuItem.id].validateData();
                        } else if (this.menuName == 'ProdDeclareParamList') {//申报
                            if (menuItem.id == 'declarationInfo') {
                                validateResult = this.$refs[menuItem.id].validateData();
                            } else {
                                validateResult = true;
                            }
                        } else if (this.menuName == 'ProdIssueAdjustList') {//发行
                            if (menuItem.id == 'prodIssueAdjust') {
                                validateResult = this.$refs[menuItem.id].validateData();
                            } else {
                                validateResult = true;
                            }
                        }


                        if (!validateResult && isCompLite) { // 验证所有的表格
                            isCompLite = false;
                            this.saveLoading = false;
                            // 滑动到目的地址
                            document.getElementById(menuItem.id).scrollIntoView({
                                block: 'start',
                                inline: 'nearest',
                                behavior: 'smooth'
                            })
                        }
                    }
                }
                //将产品排期数据与周期信息数据做对比
                /**
                 * 产品创设及会后参数确认需要将周
                 */
                if (this.menuName === 'ProdMeetingList' || this.menuName === 'M81007') {//创设页面会后参数确认页面
                    this.httpUtil.comnQuery({
                        action: 'T8ProdCalendar.findT8ProdCalendars',
                        params: {
                            prodCode: this.prodCode,
                        },
                        successAlert: false,
                    }).then(data => {
                        var fg = true;
                        if (data.rows.length > 0) {
                            let row = data.rows[0];
                            if (row.updFlag === '1') {
                                if (row.cycleOpenTerm !== this.formData.T8ProdCalendar.cycleOpenTerm) {
                                    fg = false;
                                }
                                if (row.cycleOpenType !== this.formData.T8ProdCalendar.cycleOpenType) {
                                    fg = false;
                                }
                                if (row.orderOpenDays !== this.formData.T8ProdCalendar.orderOpenDays) {
                                    fg = false;
                                }
                                if (row.postponeRule !== this.formData.T8ProdCalendar.postponeRule) {
                                    fg = false;
                                }
                                if (row.pgmno !== this.formData.T8ProdCalendar.pgmno) {
                                    fg = false;
                                }
                                if (row.establishDate !== this.formData.T8ProdCalendar.establishDate) {
                                    fg = false;
                                }
                                if (row.openStartDate !== this.formData.T8ProdCalendar.openStartDate) {
                                    fg = false;
                                }
                                if (row.openEndDate !== this.formData.T8ProdCalendar.openEndDate) {
                                    fg = false;
                                }
                                if (row.endDate !== this.formData.T8ProdCalendar.endDate) {
                                    fg = false;
                                }
                                if (row.liquidate !== this.formData.T8ProdCalendar.liquidate) {
                                    fg = false;
                                }
                                if (row.applyStartDate !== this.formData.T8ProdCalendar.applyStartDate) {
                                    fg = false;
                                }
                                if (row.applyEndDate !== this.formData.T8ProdCalendar.applyEndDate) {
                                    fg = false;
                                }
                                if (row.redempDate !== this.formData.T8ProdCalendar.redempDate && this.formData.T8ProdInfo.prodMode !== '1') {
                                    fg = false;
                                }
                            }
                        }
                        //原逻辑
                        if (!fg) {
                            Tools.confirm(() => {
                                    this.save(isCompLite, url);
                                },
                                "市场经理已调整产品排期,是否修改?"
                            )
                        } else {
                            this.save(isCompLite, url);
                        }
                    });
                } else {
                    this.save(isCompLite, url);
                }


            },

            changeDefaultValue(value) {

                //console.log("this.prodProcessForm.prodCode=:>>>",this.prodProcessForm.prod_code);
                if ('0' !== value) {
                    this.httpUtil.comnQuery({
                        action: "T8ProdInfo.isExistsSeries",
                        params: {
                            prodCode: this.prodProcessForm.prod_code,
                            t8ProdSeriesId: this.prodProcessForm.t8ProdSeriesId
                        },
                    }).then(data => {
                        if (data.rows.length > 0) {
                            this.prodProcessForm.wf_flow_template_id = '37b8bce237b345edb35051c6511ee7e8';
                            this.prodCreateInfo.isSeries = '1';
                            this.prodProcessForm.t8ProdSeriesId = data.rows[0].prodSonSeries;
                            this.prodProcessForm.seriesExplain = data.rows[0].seriesExplain;
                        } else {
                            Tools.alert("该产品绑定子系列未上会通过!", "danger");
                            this.prodProcessForm.wf_flow_template_id = 'ff733c5eb9c047aba933ffdd7fbe78a5';
                            this.prodProcessForm.isSeries = '0';
                        }
                    });
                } else {
                    this.prodCreateInfo.isSeries = '0';
                    this.prodProcessForm.wf_flow_template_id = 'ff733c5eb9c047aba933ffdd7fbe78a5';
                }


                //console.log("wf_flow_template_id",this.prodProcessForm.wf_flow_template_id);
            },
            /*设置参数并提交*/
            save(isCompLite, url) {
                let arr = [];
                for (let v in this.formData) {
                    arr[v] = JSON.stringify(this.formData[v]);
                }
                arr['assemblyMenuType'] = this.assemblyMenuType;
                //如果是创设页面
                if (this.menuName == 'M81007' || this.menuName == 'ProdMeetingList' || this.menuName == 'M81007Copy') {//创设和会后参数、克隆

                    if (this.menuName == 'M81007') {
                        arr['assemblyMenuType'] = '0';
                    }
                    arr['prodCode'] = this.prodCode;
                    let t8ProdRiskConfig = this.$refs.M81001ProdInfoAdd.$refs.prodInvest.riskItems;
                    arr['t8ProdRiskConfig'] = JSON.stringify(t8ProdRiskConfig);

                    let dataParamsProdFee = this.$refs.M81001ProdInfoAdd.$refs.prodFee.dataParams;
                    arr['t8ProdFees'] = JSON.stringify(dataParamsProdFee);

                    let dataParamsDealFee = this.$refs.M81001ProdInfoAdd.$refs.feeDeal.dataParams;
                    arr['t8FeeDeals'] = JSON.stringify(dataParamsDealFee);

                    let dataParamsProdDocInfo = this.$refs.M81001ProdInfoAdd.$refs.prodDocInfo.dataParams;
                    arr['t8ProdDocMods'] = JSON.stringify(dataParamsProdDocInfo);

                    //设置份额分类信息
                    let prodShareSort = this.$refs.M81001ProdInfoAdd.$refs.prodShareSort.tableParams;
                    arr['prodShareSorts'] = JSON.stringify(prodShareSort);

                    let data = this.$refs.M81001ProdInfoAdd.$refs.performanceInfo.envItems;
                    let data1 = this.$refs.M81001ProdInfoAdd.$refs.performanceInfo.tailingCommisionList;
                    let value = this.formData.T8ProdPerformance;
                    if (value.baseType == '3' || value.baseType == '5') {
                        arr["t8ProdPerformanceRatios"] = JSON.stringify(data);
                    }
                    if (value.baseType == '2') {
                        arr["t8PrjTailingCommisionList"] = JSON.stringify(data1);
                        arr["t8PrjFeeLists"] = JSON.stringify(data1);
                    }
                } else {
                    let t8ProdRiskConfig = this.$refs.M81001ProdInfoDisplay.$refs.prodInvest.riskItems;
                    arr['t8ProdRiskConfig'] = JSON.stringify(t8ProdRiskConfig);

                    let dataParamsProdFee = this.$refs.M81001ProdInfoDisplay.$refs.prodFee.dataParams;
                    arr['t8ProdFees'] = JSON.stringify(dataParamsProdFee);

                    let dataParamsDealFee = this.$refs.M81001ProdInfoDisplay.$refs.feeDeal.dataParams;
                    arr['t8FeeDeals'] = JSON.stringify(dataParamsDealFee);

                    let dataParamsProdDocInfo = this.$refs.M81001ProdInfoDisplay.$refs.prodDocInfo.dataParams;
                    arr['t8ProdDocMods'] = JSON.stringify(dataParamsProdDocInfo);

                    arr['prodCode'] = this.prodCode;
                    let data = this.$refs.M81001ProdInfoDisplay.$refs.performanceInfo.envItems;
                    let data1 = this.$refs.M81001ProdInfoDisplay.$refs.performanceInfo.tailingCommisionList;
                    let value = this.formData.T8ProdPerformance;
                    if (value.baseType === '3' || value.baseType === '5') {
                        arr["t8ProdPerformanceRatios"] = JSON.stringify(data);
                    }
                    if (value.baseType === '2') {
                        arr["t8PrjTailingCommisionList"] = JSON.stringify(data1);
                        arr["t8PrjFeeLists"] = JSON.stringify(data1);
                    }
                }

                if (isCompLite) { // 全部输入校验通过了，才保存
                    if (this.menuName === 'ProdIssueAdjustList') { //如果是发行登记参数确认.先判断产品排期是否做了确认
                        //查询产品排期是否已经做了确认
                        this.httpUtil.comnQuery({
                            action: "T8ProdSchedule.findProdScheduleIsConfirm",
                            params: {prodCode: this.prodCode},
                            successAlert: false
                        }).then(data => {
                            if (data.rows.length <= 0) {//未做排期确认操作
                                this.$confirm('产品排期信息未确认，确定对发行参数进行确认操作吗?', '提示', {
                                    confirmButtonText: '确定',
                                    cancelButtonText: '取消',
                                    type: 'warning'
                                }).then(() => {
                                    //发行参数确认
                                    this.httpUtil.comnUpdate({
                                        action: url,
                                        params: arr,
                                        successAlert: true
                                    }).then(data => {
                                        this.saveLoading = false;
                                        if (data.success === true) {
                                            //关闭当前窗口
                                            Tools.closeCurrentWindow(this);
                                            //返回上一页
                                            this.back2Page();
                                        }
                                    });
                                }).catch(() => {
                                    this.saveLoading = false;
                                });

                            } else {
                                //发行参数确认
                                this.httpUtil.comnUpdate({
                                    action: url,
                                    params: arr,
                                    successAlert: true
                                }).then(data => {
                                    this.saveLoading = false;
                                    if (data.success === true) {
                                        //关闭当前窗口
                                        Tools.closeCurrentWindow(this);
                                        //返回上一页
                                        this.back2Page();
                                    }
                                });
                            }
                        });
                    } else {
                        this.httpUtil.comnUpdate({
                            action: url,
                            params: arr,
                            successAlert: true
                        }).then(data => {
                            this.saveLoading = false;
                            if (data.success === true) {
                                //关闭当前窗口
                                Tools.closeCurrentWindow(this);
                                this.back2Page();
                            }
                        });
                    }

                }
            },
            //组件渲染
            showPanelsApp(data) {
                for (const row of data.rows) {
                    const assembly = {};
                    //按assemblySort排序---
                    this.showPanels[row.assemblyId] = true;
                    this.formDataShowPanels[row.assemblyId] = true;
                    assembly.desc = row.assemblyDesc;
                    assembly.id = row.assemblyId;
                    assembly.activeClass = row.activeClass;
                    assembly.mouseOver = row.mouseOver;
                    assembly.iconClass = row.iconClass;
                    if (row.alive == 'true') {
                        assembly.alive = true
                    } else {
                        assembly.alive = false
                    }
                    if (row.validate == 'true') {
                        assembly.validate = true
                    } else {
                        assembly.validate = false
                    }
                    this.menuItems.push(assembly);
                }
                this.formData.showPanels = this.formDataShowPanels;
                if (this.menuName == 'ProdDeclareParamList') {
                    this.$set(this.formData.showPanels, 'declarationInfo', false);
                    this.$set(this.formData.showPanels, 'prodIssueAdjust', false);
                    this.ifDeclarationInfo = true;//显示申报参数组件
                } else if (this.menuName == 'ProdIssueAdjustList') {
                    this.$set(this.formData.showPanels, 'prodIssueAdjust', false);
                    this.ifProdIssueAdjust = true;//显示发行组件
                }
                this.selectProdInfo();//反显
                this.initProdModel();
            },
            //定位产品模块
            initProdModel() {
                if (!this.assemblyId) {
                    for (var i = 0; i < this.menuItems.length; i++) {
                        //匹配传入模块ID
                        if (this.menuItems[i].id == this.assemblyId) {
                            this.changeTab(i);
                            break;
                        }
                    }
                }
            },

            isProdParamsCount() {
                this.httpUtil.comnQuery({
                    action: 'T8ProdInfo.isProdParamsCount',
                    params: {
                        prodCode: this.prodCode,
                    },
                }).then(data => {
                    this.state = (data.rows[0].approvalStatus == 0);
                });
            },

            isDeclareParamsCount() {
                this.httpUtil.comnQuery({
                    action: 'T8ProdDeclara.findT8ProdDec',
                    params: {
                        t8ProdInfoId: this.prodInfoId,
                    },
                }).then(data => {
                    this.state = (data.rows.length > 0);
                });
            },

            isAdjustParamsCount() {
                this.httpUtil.comnQuery({
                    action: 'T8ProdIssueRegisFields.findProdIssueInfo',
                    params: {
                        prodCode: this.prodCode,
                    },
                }).then(data => {
                    this.state = (data.rows.length > 0);
                });
            },


            selectProdInfo() {

                //标识 true 为修改，false 为新增，新增时，输入产品代码反显所有信息
                this.updateProduct = true;
                this.formData.T8ProdInfo.updateProduct = true;
                this.formData.T8ProdInfo.prodCode = this.prodCode;
                //查询产品信息及募集信息
                if (this.showPanels.prodInfo) {
                    this.httpUtil.comnQuery({
                        action: 'T8ProdInfo.findT8ProdInfos',
                        params: {
                            prodCode: this.prodCode,
                            t8ProdInfoId: this.prodInfoId,
                        }
                    }).then(data => {

                        if (data.rows.length > 0) {
                            this.formData.T8ProdInfo = data.rows[0];
                            if (this.menuName == 'M81007Copy') {
                                this.formData.T8ProdInfo.prodCode = this.prodCodeCopy;
                                this.formData.T8ProdInfo.prodName = this.prodNameCopy;
                                this.formData.T8ProdInfo.registCode = '';
                                this.formData.T8ProdInfo.prodRiskLevel = '';
                            }


                            this.$set(this.formData.ProdFee, 'establishDate', this.formData.T8ProdInfo.establishDate);
                            this.$set(this.formData.ProdFee, 'endDate', this.formData.T8ProdInfo.endDate);

                            //投资信息
                            if (this.showPanels.prodInvest) {
                                this.httpUtil.comnQuery({
                                    action: 'T8ProdInvest.findT8ProdInvests',
                                    params: {
                                        prodCode: this.prodCode,
                                        t8ProdInfoId: this.prodInfoId,
                                    }
                                }).then(data => {
                                    if (data.rows.length > 0) {
                                        this.formData.ProdInvest = data.rows[0] ? data.rows[0] : {};

                                        this.httpUtil.comnQuery({
                                            action: 'T8ProdRiskConfig.findProdRiskOther',
                                            params: {
                                                t8ProdInfoId: this.prodInfoId,
                                            }
                                        }).then(data1 => {
                                            let prodRisk = '';
                                            for (let i in data1.rows) {
                                                prodRisk += data1.rows[i].prodRisk;
                                                prodRisk += ',';
                                            }
                                            prodRisk = prodRisk.substring(0, prodRisk.length - 1);
                                            this.$set(this.formData.ProdInvest, 'prodRisk', prodRisk);
                                            if (this.menuName != 'M81007' && this.menuName != 'ProdMeetingList' && this.menuName != 'M81007Copy') {
                                                this.$refs.M81001ProdInfoDisplay.riskItems = data1.rows;
                                            } else {
                                                this.$refs.M81001ProdInfoAdd.riskItems = data1.rows;
                                            }
                                        })

                                    }
                                    this.formData.ProdInvest.prodCode = this.formData.T8ProdInfo.prodCode;
                                    this.formData.ProdInvest.t8ProdInfoId = this.formData.T8ProdInfo.id;
                                });
                            }

                            //估值信息
                            if (this.showPanels.prodValuation) {

                                this.httpUtil.comnQuery({
                                    action: 'T8ProdInvest.findProdValuations',
                                    params: {
                                        prodCode: this.prodCode,
                                        t8ProdInfoId: this.prodInfoId,
                                    }
                                }).then(data => {
                                    if (data.rows != null && data.rows.length > 0) {
                                        this.formData.ProdValuation = data.rows[0] ? data.rows[0] : {};
                                    }
                                    this.formData.ProdValuation.prodCode = this.formData.T8ProdInfo.prodCode;
                                    this.formData.ProdValuation.t8ProdInfoId = this.formData.T8ProdInfo.id;
                                    this.formData.ProdValuation.prodMode = this.formData.T8ProdInfo.prodMode;
                                    // console.log(this.formData.ProdValuation.prodMode)
                                });
                            }

                            //业绩报酬
                            if (this.showPanels.performanceInfo) {

                                this.httpUtil.comnQuery({
                                    action: 'T8ProdPerformance.findT8ProdPerformances',
                                    params: {
                                        prodCode: this.prodCode,
                                        t8ProdInfoId: this.prodInfoId,
                                    }
                                }).then(data => {
                                    if (data.rows.length > 0) {
                                        this.formData.T8ProdPerformance = data.rows[0];
                                        let baseType = this.formData.T8ProdPerformance.baseType;
                                        if (baseType == '2') {
                                            //this.$refs.performanceInfo.switchSegmentValue=true;
                                        }
                                        if (baseType == '3' || baseType == '5') {
                                        }
                                        //查询指数信息
                                        this.httpUtil.comnQuery({
                                            action: 'T8ProdPerformanceRatio.findT8ProdPerformanceRatio',
                                            params: {
                                                t8ProdPerformanceId: this.formData.T8ProdPerformance.id,
                                            }
                                        }).then(data => {
                                            if (data.rows.length > 0) {
                                                if (this.menuName != 'M81007' && this.menuName != 'ProdMeetingList' && this.menuName != 'M81007Copy') {
                                                    this.$refs.M81001ProdInfoDisplay.envItems = data.rows;
                                                } else {
                                                    this.$refs.M81001ProdInfoAdd.envItems = data.rows;
                                                }

                                            }
                                        });
                                        //查询分段信息
                                        this.httpUtil.comnQuery({
                                            action: 'T8PrjFeeList.findPerformanceT8PrjFeeLists',
                                            params: {
                                                feeCode: this.prodInfoId,
                                            }
                                        }).then(data => {
                                            let moneyList2 = [];
                                            let array = [];
                                            if (data.rows.length > 1) {
                                                if (baseType == '2') {
                                                    if (this.menuName != 'M81007' && this.menuName != 'ProdMeetingList' && this.menuName != 'M81007Copy') {
                                                        this.$refs.M81001ProdInfoDisplay.switchSegmentValue = true;
                                                    } else {
                                                        this.$refs.M81001ProdInfoAdd.switchSegmentValue = true;
                                                    }
                                                }
                                                if (baseType == '3' || baseType == '5') {
                                                }

                                                for (let j = 0; j < data.rows.length; j++) {
                                                    array.push(data.rows[j]);
                                                    if (j < data.rows.length - 1) {
                                                        moneyList2.push(parseInt(data.rows[j].dimension2Max));
                                                    }
                                                }
                                            }
                                            if (this.menuName != 'M81007' && this.menuName != 'ProdMeetingList' && this.menuName != 'M81007Copy') {
                                                this.$refs.M81001ProdInfoDisplay.moneyList = moneyList2;
                                                this.$refs.M81001ProdInfoDisplay.tailingCommisionList = array;
                                                this.$refs.M81001ProdInfoDisplay.tailingCommisionMoneyList = array;
                                            } else {
                                                this.$refs.M81001ProdInfoAdd.moneyList = moneyList2;
                                                this.$refs.M81001ProdInfoAdd.tailingCommisionList = array;
                                                this.$refs.M81001ProdInfoAdd.tailingCommisionMoneyList = array;
                                            }

                                        });
                                    }
                                    this.formData.T8ProdPerformance.prodCode = this.formData.T8ProdInfo.prodCode;
                                    this.formData.T8ProdPerformance.t8ProdInfoId = this.formData.T8ProdInfo.id;
                                });
                            }

                            //托管信息
                            if (this.showPanels.truteeInfo) {

                                this.httpUtil.comnQuery({
                                    action: 'T8ProdTrutee.findT8ProdTrutees',
                                    params: {
                                        prodCode: this.prodCode,
                                        t8ProdInfoId: this.prodInfoId,
                                    }
                                }).then(data => {
                                    if (data.rows.length > 0) {
                                        this.formData.T8ProdEscrowAcct = data.rows[0] ? data.rows[0] : {};
                                    }
                                    this.formData.T8ProdEscrowAcct.prodCode = this.formData.T8ProdInfo.prodCode;
                                    this.formData.T8ProdEscrowAcct.t8ProdInfoId = this.formData.T8ProdInfo.id;
                                    this.$set(this.formData.T8ProdDocMods, "t8TruteeInfoId", this.formData.T8ProdEscrowAcct.t8TruteeInfoId);
                                    this.$set(this.formData.T8ProdDocMods, "t8OutTruteeInfoId", this.formData.T8ProdEscrowAcct.t8OutTruteeInfoId);
                                });
                            }

                            //产品销售信息
                            if (this.showPanels.limitInfo) {
                                this.httpUtil.comnQuery({
                                    action: 'T8ProdSale.findT8ProdSales',
                                    params: {
                                        prodCode: this.prodCode,
                                        t8ProdInfoId: this.prodInfoId,
                                    }
                                }).then(data => {
                                    if (data.rows.length > 0) {
                                        this.formData.T8ProdLimit = data.rows[0] ? data.rows[0] : {};
                                    }
                                    this.formData.T8ProdLimit.prodCode = this.formData.T8ProdInfo.prodCode;
                                    this.formData.T8ProdLimit.t8ProdInfoId = this.formData.T8ProdInfo.id;

                                });
                            }

                            //产品分红信息
                            if (this.showPanels.prodBonus) {

                                this.httpUtil.comnQuery({
                                    action: 'T8ProdBonusNew.findT8ProdBonusNews',
                                    params: {
                                        prodCode: this.prodCode,
                                        t8ProdInfoId: this.prodInfoId,
                                    }
                                }).then(data => {
                                    if (data.rows != null && data.rows.length > 0) {
                                        this.formData.T8ProdBonus = data.rows[0] ? data.rows[0] : {};
                                    }
                                    this.formData.T8ProdBonus.prodCode = this.formData.T8ProdInfo.prodCode;
                                    this.formData.T8ProdBonus.t8ProdInfoId = this.formData.T8ProdInfo.id;
                                });
                            }

                            //周期信息
                            if (this.showPanels.prodCalendar) {

                                this.httpUtil.comnQuery({
                                    action: 'T8ProdCalendar.findT8ProdCalendars',
                                    params: {
                                        prodCode: this.prodCode
                                    }
                                }).then(data => {
                                    if (data.rows.length > 0) {
                                        this.formData.T8ProdCalendar = data.rows[0] ? data.rows[0] : {};
                                        this.$set(this.formData.T8ProdBonus, "establishDate", data.rows[0].establishDate);
                                    } else {
                                        if (this.formData.T8ProdInfo.prodMode != '1') {
                                            this.$set(this.formData.ProdFee, 'endDate', '20991231');
                                        }
                                    }
                                    this.formData.T8ProdCalendar.prodCode = this.formData.T8ProdInfo.prodCode;
                                    this.formData.T8ProdCalendar.t8ProdInfoId = this.formData.T8ProdInfo.id;
                                });
                            }

                            //交易费用信息
                            if (this.showPanels.feeDeal) {

                                this.httpUtil.comnQuery({
                                    action: 'T8FeeDeal.findT8FeeDeals',
                                    params: {
                                        prodCode: this.prodCode,
                                        t8ProdInfoId: this.prodInfoId,
                                    }
                                }).then(data => {
                                    if (data.rows.length > 0) {
                                        this.formData.ProdFeeDeal.dataParams = data.rows;
                                        for (let i = 0; i < this.formData.ProdFeeDeal.dataParams.length; i++) {
                                            this.httpUtil.comnQuery({
                                                action: 'T8PrjFeeList.findT8PrjFeeLists',
                                                params: {
                                                    feeDealId: this.formData.ProdFeeDeal.dataParams[i].id,
                                                }
                                            }).then(rows => {
                                                this.$set(this.formData.ProdFeeDeal.dataParams[i], 't8PrjFeeLists', rows.rows);
                                            });
                                        }
                                    }
                                    this.formData.ProdFeeDeal.prodCode = this.formData.T8ProdInfo.prodCode;
                                    this.formData.ProdFeeDeal.t8ProdInfoId = this.formData.T8ProdInfo.id;
                                });
                            }

                            //产品费用信息
                            if (this.showPanels.prodFee) {

                                this.httpUtil.comnQuery({
                                    action: 'T8FeeProd.findT8FeeProds',
                                    params: {
                                        prodCode: this.prodCode,
                                        t8ProdInfoId: this.prodInfoId,
                                    }
                                }).then(data => {
                                    if (data.rows.length > 0) {
                                        this.formData.ProdFee.dataParams = data.rows;
                                    }
                                    this.formData.ProdFee.prodCode = this.formData.T8ProdInfo.prodCode;
                                    this.formData.ProdFee.t8ProdInfoId = this.formData.T8ProdInfo.id;
                                });
                            }


                            //产品文档模板
                            if (this.showPanels.prodDocInfo) {
                                this.httpUtil.comnQuery({
                                    action: 'T8ProdDocInfo.findT8ProdDocInfos',
                                    params: {
                                        t8ProdInfoId: this.prodInfoId,
                                    }
                                }).then(data => {
                                    if (data.rows.length > 0) {
                                        this.formData.T8ProdDocMods.dataParams = data.rows;
                                    }
                                    this.formData.T8ProdDocMods.prodCode = this.formData.T8ProdInfo.prodCode;
                                    this.formData.T8ProdDocMods.prodName = this.formData.T8ProdInfo.prodName;
                                    this.formData.T8ProdDocMods.prodMode = this.formData.T8ProdInfo.prodMode;
                                    this.formData.T8ProdDocMods.t8ProdInfoId = this.formData.T8ProdInfo.id;
                                    let prodMode_ = this.formData.T8ProdInfo.prodMode;
                                    let raiseType_ = this.formData.T8ProdInfo.raiseType;
                                    let docTypeDict = '';
                                    if (prodMode_ == '1' && raiseType_ == '01') {
                                        docTypeDict = 't8_temp_type_fb_gm';
                                    } else if (prodMode_ == '2' && raiseType_ == '01') {
                                        docTypeDict = 't8_temp_type_tt_gm';
                                    } else if (prodMode_ == '3' && raiseType_ == '01') {
                                        docTypeDict = 't8_temp_type_zq_gm';
                                    } else if (prodMode_ == '1' && raiseType_ == '02') {
                                        docTypeDict = 't8_temp_type_fb_sm';
                                    } else if (prodMode_ == '2' && raiseType_ == '02') {
                                        docTypeDict = 't8_temp_type_tt_sm';
                                    } else if (prodMode_ == '3' && raiseType_ == '02') {
                                        docTypeDict = 't8_temp_type_zq_sm';
                                    } else if (prodMode_ == '4') {
                                        docTypeDict = 't8_temp_type_hb';
                                    }
                                    this.$set(this.formData.T8ProdDocMods, 'docTypeDict', docTypeDict);

                                });
                            }


                            //申报登记要素
                            if (this.showPanels.declarationInfo || this.menuName == 'M81007Copy') {

                                this.httpUtil.comnQuery({
                                    action: 'T8ProdDeclara.findT8ProdDeclaras',
                                    params: {
                                        t8ProdInfoId: this.prodInfoId,
                                    }
                                }).then(data => {
                                    if (data.rows.length > 0) {
                                        this.formData.T8DeclarationInfo = data.rows[0] ? data.rows[0] : {};
                                    }
                                    //判断是否有销售地址,赋值默认销售地址值
                                    this.httpUtil.comnQuery({
                                        action: 'T8ProdDeclara.findT8ProdDecById',
                                        params: {
                                            id: this.formData.T8DeclarationInfo.id
                                        }
                                    }).then(data => {
                                        if (data.rows.length == 0 || data.rows[0].prodSalesArea == "" || data.rows[0].prodSalesArea == "undefined" || data.rows[0].prodSalesArea == null) {
                                            this.httpUtil.comnQuery({
                                                action: 'DictItem.fidAllArea',
                                                params: {}
                                            }).then(data => {
                                                let arry = [];
                                                for (let i = 0; i < data.rows.length; i++) {
                                                    let task = data.rows[i].itemKey;
                                                    arry.push(task)
                                                }
                                                this.$set(this.formData.T8DeclarationInfo, 'prodSalesArea', arry.toString());
                                            });
                                        } else {
                                            this.$set(this.formData.T8DeclarationInfo, 'prodSalesArea', data.rows[0].prodSalesArea);
                                        }
                                    });
                                    this.formData.T8DeclarationInfo.prodName = this.formData.T8ProdInfo.prodName;
                                    this.formData.T8DeclarationInfo.prodCode = this.formData.T8ProdInfo.prodCode;
                                    this.$set(this.formData.T8DeclarationInfo, 'internalIdentCode', this.formData.T8ProdInfo.prodCode);
                                    this.$set(this.formData.T8DeclarationInfo, 't8ProdInfoId', this.formData.T8ProdInfo.id);
                                    let prodMode = this.formData.T8ProdInfo.prodMode;
                                    if (prodMode == '1') {
                                        this.$set(this.formData.T8DeclarationInfo, 'productOperationMode', '01');
                                    } else {
                                        this.$set(this.formData.T8DeclarationInfo, 'productOperationMode', '03');
                                    }
                                });
                            }
                            //发行登记要素
                            if (this.showPanels.prodIssueAdjust || this.menuName == 'M81007Copy') {
                                //查询发行信息
                                this.httpUtil.comnQuery({
                                    action: 'T8ProdIssueRegisFields.findProdIssueInfoByProdId',
                                    params: {
                                        t8ProdInfoId: this.prodInfoId,
                                    }
                                }).then(data => {
                                    if (data.rows.length > 0) {
                                        this.formData.T8ProdIssueRegisFields = data.rows[0] ? data.rows[0] : {};
                                    }
                                    this.formData.T8ProdIssueRegisFields.prodCode = this.formData.T8ProdInfo.prodCode;
                                    this.formData.T8ProdIssueRegisFields.t8ProdInfoId = this.formData.T8ProdInfo.id;
                                    //this.$set(this.formData.T8ProdIssueRegisFields, "openMod", '01');
                                    //管理方式默认单独管理、是否结构分级默认否、开放模式默认有规律开放、开放期业务默认申赎皆可
                                    this.formData.T8ProdIssueRegisFields.manageMethod = '02';
                                    this.formData.T8ProdIssueRegisFields.isStructural = '02';
                                    this.formData.T8ProdIssueRegisFields.openMod = '01';
                                    this.formData.T8ProdIssueRegisFields.openDuringBusiness = '01';
                                    this.formData.T8ProdIssueRegisFields.firstOpenStartDate = this.formData.T8ProdCalendar.openStartDate ? this.formData.T8ProdCalendar.openStartDate : '';
                                });
                            }
                            //查询份额分类信息
                            this.httpUtil.comnQuery({
                                action: 'ProdShareSort.findProdShareSortsNoAuth',
                                params: {
                                    t8ProdInfoId: this.formData.T8ProdInfo.id,
                                }
                            }).then(data => {
                                if (data.rows.length > 0) {
                                    this.formData.T8ProdShareSort.dataParams = data.rows;
                                }
                            });
                        }
                    });
                }
            },
        },


        created() {
            this.assemblyId = this.$route.query.assembly_id;
            this.menuName = this.$route.query.menuName;
            this.assemblyMenuType = this.$route.query.assemblyMenuType;
            this.prodInfoId = this.$route.query.prodInfoId;
            this.prodMode = this.$route.query.prodMode;               // 产品形态
            this.prodModeId = this.$route.query.prodModeId;               // 产品形态
            this.prodCode = this.$route.query.prodCode;               // 产品代码
            this.formData.T8ProdInfo.prodMode = this.prodMode;
            this.formData.T8ProdInfo.prodCode = this.prodCode;
            this.formData.T8ProdInfo.id = this.$route.query.prodInfoId;
            this.formData.menuName = this.menuName;
            this.formData.assemblyMenuType = this.assemblyMenuType;
            if (this.menuName == 'M81007') {
                this.isProdParamsCount();
            } else if (this.menuName == 'ProdMeetingList') {
                this.state = true;
            } else if (this.menuName == 'ProdDeclareParamList') {
                this.isDeclareParamsCount();
            } else if (this.menuName == 'ProdIssueAdjustList') {
                this.isAdjustParamsCount();
            } else if (this.menuName == 'M81007Copy') {
                this.prodCodeCopy = this.$route.query.prodCodeCopy;
                this.prodNameCopy = this.$route.query.prodNameCopy;
                this.state = true;
            }

            //查询渲染组件
            this.httpUtil.comnQuery({
                action: 'T8ProdAssembly.findT8ProdAssemblyConfirm',
                params: {
                    assemblyType: this.assemblyMenuType,
                    prodCode: this.prodCode,
                },
            }).then(data => {
                if (data.rows.length > 0) {
                    //用来单独判断是否渲染份额分类组件，由isShareSort控制
                    if (this.$route.query.isShareSort == '0') {
                        for (let i = 0; i < data.rows.length; i++) {
                            if (data.rows[i].assemblyId == 'ProdShareSort') {
                                this.$delete(data.rows, i);
                            }
                        }
                    }
                    this.showPanelsApp(data);
                    //传递参数跳转指定位置
                    this.$nextTick(() => {
                        this.changeTabProp(this.assemblyId);
                    });
                } else {
                    Tools.alert("没有取到组件参数，请配置产品信息组件参数或者产品模型组件参数");
                }
            });
            // let sortMenu = {
            //   'activeClass': 'selected-trutee',
            //   'alive': false,
            //   'desc': '份额分类',
            //   'iconClass': 'item-trutee',
            //   'id': 'ProdShareSort',
            //   'mouseOver': null,
            //   'validate': false
            // };
            //bus通信控制份额分类组件的显示与隐藏
            // eventBus.$on('shareSortChange', item => {
            //   if (item.shareSort === '1') {
            //     //获取申报参数下标
            //     let index = this.menuItems.findIndex(item => item.id === 'declarationInfo');
            //     if (index > 0) {
            //       this.menuItems.splice(index, 0, sortMenu);
            //     } else {
            //       //新增份额分类标签
            //       this.menuItems.push(sortMenu);
            //     }
            //     this.isShareSort = true;
            //   } else {
            //     for (var i = 0; i < this.menuItems.length; i++) {
            //       //删除份额分类标签
            //       if (this.menuItems[i].id === 'ProdShareSort') {
            //         this.isShareSort = true;
            //         this.menuItems.splice(i, 1);
            //         break;
            //       }
            //     }
            //     this.isShareSort = false;
            //   }
            // });
        },

        mounted() {
            this.$refs.formPanel.style['height'] = (document.body.clientHeight - 112) + 'px';


        },
        model: {
            prop: 'prodMode',
            event: 'input'
        },


        watch: {
            prodCode: {
                handler: function (val) {
                    this.$emit('pordDetail', val);
                }
            },
        },

    }
</script>

<style lang="scss" scoped>

  @import "../../../../styles/T81001.scss";

</style>
