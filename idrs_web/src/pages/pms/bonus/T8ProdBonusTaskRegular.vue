<template>
    <div class="md-card k-card md-theme-default parent-div">
        <div class="form-item prod-panel" style="margin-top:50px;display: -webkit-box;" id="baseInfo">
            <div class="display-flex">

                <div :class="state ? 'share-container' : 'share-containerAdd'" v-show="showButton1 && showButton">
                    <div @click="stagingTask" size="mini" class="pd-button"
                         v-show="true" :data-disabled="saveLoading1">
                        <i v-show="saveLoading1" class="el-icon-loading"/>
                        <md-icon md-src="/static/images/create/save5.svg" v-show="!saveLoading1" class="pd-icon-20"/>
                        <div v-show="!saveLoading1" style="margin-top: 4px;font-size: 8px;padding-left: 13px;">暂存</div>
                    </div>

                    <div @click="submitTask" size="mini" class="pd-button"
                         v-show="true" :data-disabled="saveLoading">
                        <i v-show="saveLoading" class="el-icon-loading"/>
                        <md-icon md-src="/static/images/create/save5.svg" v-show="!saveLoading" class="pd-icon-20"/>
                        <div v-show="!saveLoading" style="margin-top: 4px;font-size: 8px;padding-left: 13px;">保存</div>
                    </div>
                </div>

            </div>

            <div class="title">
                <div class="prod-items"></div>
                <k-field-display class="title-desc" value="基本信息"></k-field-display>
            </div>
            <k-form ref="addT8ProdBonusRuleForm" :data-col="2">
                <k-form-item label="产品代码">
                    <k-field-select v-model="t8ProdBonusRule.prodCode" :data-disabled="true" :data-allowblank="false"/>
                </k-form-item>
                <k-form-item label="产品名称">
                    <k-field-text v-model="t8ProdBonusRule.prodName" :data-disabled="true" :data-allowblank="false"/>
                </k-form-item>
                <k-form-item label="分红方式">
                    <k-field-select v-model="t8ProdBonusRule.dividendType"
                                    data-dict="div_method"
                                    :data-allowblank="isEdit('dividend_type')"
                                    :data-disabled="isEdit('dividend_type')"/>
                </k-form-item>
                <k-form-item label="分红模式">
                    <k-field-select v-model="t8ProdBonusRule.dividendMode"
                                    data-dict="t8_dividend_mode"
                                    :data-allowblank="isEdit('dividend_mode')"
                                    :data-disabled="isEdit('dividend_mode')"/>
                </k-form-item>
                <k-form-item label="权益登记日">
                    <k-field-date v-model="t8ProdBonusRule.dividendRegisterDate"
                                  :data-allowblank="isEdit('dividend_register_date')"
                                  :data-disabled="isEdit('dividend_register_date')"/>
                </k-form-item>
                <k-form-item label="分红基准日">
                    <k-field-date v-model="t8ProdBonusRule.dividendBaseDate"
                                  :data-allowblank="isEdit('dividend_base_date')"
                                  :data-disabled="isEdit('dividend_base_date')"/>
                </k-form-item>
                <k-form-item label="分红除权日">
                    <k-field-date v-model="t8ProdBonusRule.dividendExDate" :data-allowblank="isEdit('dividend_ex_date')"
                                  :data-disabled="isEdit('dividend_ex_date')"/>
                </k-form-item>
                <k-form-item label="当前份额">
                    <k-field-text v-model="t8ProdBonusRule.share" :data-allowblank="isEdit('share')"
                                  data-regx-text="请输入正确的数字"
                                  data-digits="2" data-integer-length="13"
                                  data-validate-type="number" data-type="number"
                                  :data-disabled="isEdit('share')"/>
                </k-form-item>
                <k-form-item label="份额日期">
                    <k-field-date v-model="t8ProdBonusRule.shareDate" :data-allowblank="isEdit('share_date')"
                                  :data-disabled="isEdit('share_date')"/>
                </k-form-item>
                <k-form-item label="分红总金额">
                    <k-field-text v-model="t8ProdBonusRule.totalAmount"
                                  :data-allowblank="!(t8ProdBonusRule.dividendMode != '1' && !isEdit('total_amount'))"
                                  :data-disabled="!(t8ProdBonusRule.dividendMode != '1' && !isEdit('total_amount'))"
                                  data-regx-text="请输入正确的数字"
                                  data-digits="2" data-integer-length="13"
                                  data-validate-type="number" data-type="number"
                                  @data-on-blur="calcUnitBonus"/>
                </k-form-item>
                <k-form-item label="单位分红">
                    <k-field-text v-model="t8ProdBonusRule.dividendUnit"
                                  :data-allowblank="!(t8ProdBonusRule.dividendMode != '2' && !isEdit('dividend_unit'))"
                                  :data-disabled="!(t8ProdBonusRule.dividendMode != '2' && !isEdit('dividend_unit'))"
                                  data-regx-text="请输入正确的数字"
                                  data-digits="4" data-integer-length="13"
                                  data-validate-type="number" data-type="number"
                                  @data-on-blur="calcTotalAmount"/>
                </k-form-item>
                <k-form-item label="红利发放日">
                    <k-field-date v-model="t8ProdBonusRule.dividendIssueDate"
                                  :data-allowblank="!(t8ProdBonusRule.dividendType != '0' && !isEdit('dividend_issue_date'))"
                                  :data-disabled="!(t8ProdBonusRule.dividendType != '0' && !isEdit('dividend_issue_date'))"/>
                </k-form-item>
                <k-form-item label="红利再投日">
                    <k-field-date v-model="t8ProdBonusRule.handOutDate"
                                  :data-allowblank="!(t8ProdBonusRule.dividendType != '1' && !isEdit('hand_out_date'))"
                                  :data-disabled="!(t8ProdBonusRule.dividendType != '1' && !isEdit('hand_out_date'))"/>
                </k-form-item>
                <k-form-item label="现金红利到账日">
                    <k-field-date v-model="t8ProdBonusRule.dividendArrivalDate"
                                  :data-allowblank="isEdit('dividend_arrival_date')"
                                  :data-disabled="isEdit('dividend_arrival_date')"/>
                </k-form-item>
                <k-form-item label="产品经理">
                    <k-field-text v-model="t8ProdBonusRule.prodManager" :data-disabled="true" :data-allowblank="false"/>
                </k-form-item>
                <k-form-item label="投资经理">
                    <k-field-text v-model="t8ProdBonusRule.investManager" :data-disabled="true"
                                  :data-allowblank="false"/>
                </k-form-item>
                <k-form-item label="估值经理">
                    <k-field-text v-model="t8ProdBonusRule.valuationManager" :data-disabled="true"
                                  :data-allowblank="false"/>
                </k-form-item>
                <k-form-footer data-align="center">

                    <k-btn style="width:100px;" class="btn-custom-primary"
                           :data-model="t8ProdBonusRule"
                           @click="popupEdit(t8ProdBonusRule)" data-functype="POPUP"
                           :data-disabled="t8ProdBonusRule.processStatus == '4'"
                           data-target="xpModWordUpdate">
                        <md-icon md-src="/static/svg/confirm.svg"></md-icon>补录分发
                    </k-btn>

                    <k-btn style="width:100px;" class="btn-custom-primary" data-size="small"
                           @click="previewBonusPlan(t8ProdBonusRule)" :data-model="this.t8ProdBonusRule"
                           :data-disabled="t8ProdBonusRule.processStatus == '1' || t8ProdBonusRule.processStatus == '5'"
                           v-if="global.isShowAuthorityButton('T8ProdBonusRule.previewBonusPlan')">
                        <md-icon md-src="/static/svg/confirm.svg"></md-icon>方案预览
                    </k-btn>
                    <k-btn style="width:100px;" class="btn-custom-plain" data-download-name="分红方案"
                           data-size="small"
                           @click="downloadBonusPlan(t8ProdBonusRule)"
                           :data-model="this.t8ProdBonusRule"
                           :data-disabled="t8ProdBonusRule.processStatus == '1' || t8ProdBonusRule.processStatus == '5'"
                           v-if="global.isShowAuthorityButton('T8ProdBonusRule.downloadPlan')"
                    >
                        <md-icon md-src="/static/svg/add.svg" />方案下载
                    </k-btn>
                    <k-btn style="width:100px;" class="btn-custom-primary" data-size="small"
                           @click="previewBonusNoitce(t8ProdBonusRule)" :data-model="this.t8ProdBonusRule"
                           :data-disabled="t8ProdBonusRule.processStatus == '1' || t8ProdBonusRule.processStatus == '5'"
                           v-if="global.isShowAuthorityButton('T8ProdBonusRule.previewBonus')">
                        <md-icon md-src="/static/svg/confirm.svg"></md-icon>公告预览
                    </k-btn>
                    <k-btn style="width:100px;" class="btn-custom-plain" data-download-name="分红公告"
                           data-size="small"
                           @click="downloadBonusNoitce(t8ProdBonusRule)"
                           :data-model="this.t8ProdBonusRule"
                           :data-disabled="t8ProdBonusRule.processStatus == '1' || t8ProdBonusRule.processStatus == '5'"
                           v-if="global.isShowAuthorityButton('T8ProdBonusRule.download')">
                        <md-icon md-src="/static/svg/add.svg" />公告下载
                    </k-btn>
                    <k-btn style="width:100px;" class="btn-custom-primary" data-size="small"
                           :data-model="t8ProdBonusRule"
                           @click="approvalTask(t8ProdBonusRule)"
                           :data-disabled="!(t8ProdBonusRule.processStatus == '2' && this.isSubmit)">
                        <md-icon md-src="/static/svg/confirm.svg"></md-icon>发起审批
                    </k-btn>
                    <k-btn style="width:100px;" class="btn-custom-primary" data-size="small"
                           :data-model="t8ProdBonusRule"
                           @click="approvalFinished(t8ProdBonusRule)"
                           v-if="global.isShowAuthorityButton('T8ProdBonusRule.download')">
                        <md-icon md-src="/static/svg/confirm.svg"></md-icon>审批完成
                    </k-btn>
                </k-form-footer>
            </k-form>
        </div>

        <k-popup ref="xpModWordUpdate" title="补录分发" data-width="60%" :data-dialog-drag="true">
            <k-grid :data-checkbox="true" data-checkbox-id="id" ref="updateGrid"
                    :data-params="{noticeId:this.t8ProdBonusRule.id}"
                    data-action="DisclosureNoticeEntryProcess.findDisclosureNoticeEntryProcesss" :data-page-size="0"
                    data-height="500px"
                    data-operate-column="false"
                    @init="(grid)=>{this.DisclosureNoticeEntryProcess.$RatGrid = grid}"
                    style="height: 600px; overflow: auto;">
                <k-grid-column data-header="id" data-name="id" data-width="20" data-hidden="true"></k-grid-column>
                <k-grid-column data-header="noticeId" data-name="noticeId" data-width="20"
                               data-hidden="true"></k-grid-column>
                <k-grid-column data-header="录入角色" data-name="roleId" data-width="250">
                    <template slot-scope="scope">
                        <k-field-select v-model="scope.row.row.roleId" :data-data="roleList" data-display-field="label"
                                        data-value-field="value" :data-multiple="false" :data-disabled="true"
                                        :data-allowblank="false"/>
                    </template>
                </k-grid-column>
                <k-grid-column data-header="补录用户" data-name="userId" data-width="180">
                    <template slot-scope="scope">
                        <k-field-select v-model="scope.row.row.userId" :data-params="{'roleId':scope.row.row.roleId}"
                                        data-action='User.getRoleUser' data-display-field="label"
                                        data-value-field="value"
                                        :data-multiple="false" :data-allowblank="false" :data-disabled="true"/>
                    </template>
                </k-grid-column>
                <k-grid-column data-header="转交用户" data-name="toUserId" data-width="200">
                    <template slot-scope="scope">
                        <k-field-select v-model="scope.row.row.toUserId" :data-params="{'roleId':scope.row.row.roleId}"
                                        data-action='User.getRoleUser' data-display-field="label"
                                        data-value-field="value"
                                        :data-allowblank="false"/>
                    </template>
                </k-grid-column>

            </k-grid>
            <div style="text-align: right;">
                <k-btn class="btn-custom-primary" :data-handler="saveModColumns"
                       :disabled="(showSubmitBtn === false)||this.isDistriSaveBtnDisabled">
                    <span v-show="showSubmitBtn">保存</span>
                    <i v-show="!showSubmitBtn" class="el-icon-loading"/>
                </k-btn>
                <k-btn class="btn-custom-plain" data-functype="CLOSE">
                    <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
                </k-btn>
            </div>
        </k-popup>
        <k-popup ref="onlineEditPopup" data-width="60%">
            <div class="edit">
                <div class="word">
                    <iframe name="onlineEdit" id="onlineEdit" :src="viewUrl"></iframe>
                </div>
            </div>
        </k-popup>

    </div>

</template>

<script>
    import Tools from "@/utils/tools";

    export default {
        name: "T8ProdBonusTaskRegular",
        data() {
            return {
                id: '',
                prodCode: '',
                t8ProdBonusRule: {},
                inGroup: '',
                isDisRole: '',
                showButton: true,
                showButton1: true,
                saveLoading: false,//保存按钮是否可点击
                saveLoading1: false,
                state: false,
                DisclosureNoticeEntryProcess: {
                    noticeId: '',
                    $RatGrid: null,
                },
                roleList: [],
                showSubmitBtn: true,
                isDistriSaveBtnDisabled: false,//补录分发保存按钮是否不可点击  默认可以点击
                isUploadBtnEnable: true,//上传按钮是否可以点击
                isDownloadBtnEnable: true,//是否下载按钮可以点击
                fieldList: [],//属性List
                isClick: false,
                viewUrl: '',
                isSubmit: false, //是否可以发起审批
                t8ProdInfoId: '',
                isPreview:true,
            }
        },
        // activated() {
        //     this.refreshPageParam();
        // },
        created() {
            this.refreshPageParam();
            //查询产品id
            this.httpUtil.comnQuery({
                action: 'T8ProdBonusRule.findProdInfoByProdCode',
                params: {prodCode: this.prodCode},
            }).then(data => {
                this.t8ProdInfoId = data.rows[0].id;
            });
        },
        methods: {
            //判断字段是否有编辑权限
            isEdit(val) {
                if (this.showButton1 && (this.t8ProdBonusRule.processStatus == '1' || this.t8ProdBonusRule.processStatus == '5')) {
                    let index = this.fieldList.indexOf(val);
                    if (index >= 0) {
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    return true;
                }

            },
            refreshPageParam() {
                this.id = this.$route.query.id;
                this.prodCode = this.$route.query.prodCode;

                Tools.getLoginUser().then(res => {
                    let userid = res.userid;
                    this.$nextTick(() => {
                        this.httpUtil.comnQuery({
                            action: 'T8ProdBonusRule.findProcessByUserId',
                            params: {id: this.id, userId: userid}
                        }).then(data => {
                            if (data.rows.length > 0)
                                this.showButton1 = true;
                            else
                                this.showButton1 = false;
                        })
                        //let roleids = res.roleids;

                        this.httpUtil.comnQuery({
                            action: 'DisclosureModColumn.isEditByRoleId',
                            params: {userId: userid, prodCode: this.prodCode, id: this.id},
                        }).then(data => {
                            this.fieldList = [];
                            if (data.rows != null) {
                                for (let i = 0; i < data.rows.length; i++) {
                                    this.fieldList.push(data.rows[i].columnKey);
                                }
                            }
                        });

                        this.httpUtil.comnQuery({
                            action: 'T8ProdBonusRule.findRoleIdByUserId',
                            params: {prodCode: this.prodCode, userId: userid,id:this.id}
                        }).then(data => {
                            if (data.rows.length > 0) {
                                this.isSubmit = true;
                            }
                        });
                    })
                });
                this.httpUtil.comnQuery({
                    action: 'T8ProdBonusRule.findT8ProdBonusRules1',
                    params: {id: this.id}
                }).then(data => {
                    this.t8ProdBonusRule = data.rows[0];
                    if (data.rows[0].processStatus == '1' || data.rows[0].processStatus == '5' || data.rows[0].processStatus == '6') {
                        this.showButton = true;
                    } else if (data.rows[0].processStatus == '2') {
                        this.showButton = false;
                    } else {
                        this.showButton = false;
                        this.isDistriSaveBtnDisabled = true;
                    }
                    this.t8ProdBonusRule = data.rows[0];
                });

                this.httpUtil.comnQuery({
                    action: "Role.findAll",
                    params: null
                }).then(data => {
                    if (data.rows.length > 0) {
                        this.roleList = data.rows;
                    }
                }).catch({});
            },
            selectRow(row, column, event) {
                this.selectRowData = Object.assign({}, row)
                this.formData = Object.assign({}, row)
            },
            popupEdit(row) {
                this.httpUtil.comnUpdate({
                    action: "T8ProdBonusRule.findOperatorForDivided",
                    params: {"id": row.id},
                    mask: false,
                    successAlert: false,
                }).then(data => {
                    if ("failed" === data.returndata.result) {
                        this.isClick = false;
                    } else {
                        this.isClick = true;
                    }
                })
            },
            previewXP(val) {
            },
            //计算单位分红
            calcUnitBonus() {
                if (this.t8ProdBonusRule.dividendMode == '2' &&
                    this.t8ProdBonusRule.dividendMode != "" &&
                    this.t8ProdBonusRule.dividendMode != null &&
                    this.t8ProdBonusRule.dividendMode != undefined) {
                    if (this.t8ProdBonusRule.share != null &&
                        this.t8ProdBonusRule.share != "" &&
                        this.t8ProdBonusRule.share != undefined) {
                        let totalAmount = this.t8ProdBonusRule.totalAmount;
                        let share = this.t8ProdBonusRule.share;
                        let dividendUnit = (parseFloat(totalAmount) / parseFloat(share)).toFixed(4);
                        this.$set(this.t8ProdBonusRule, "dividendUnit", dividendUnit);
                    } else {
                        this.$set(this.t8ProdBonusRule, "dividendUnit", "0.0000");
                    }

                }

            },
            //计算总额分红
            calcTotalAmount() {
                if (this.t8ProdBonusRule.dividendMode == '1' &&
                    this.t8ProdBonusRule.dividendMode != "" &&
                    this.t8ProdBonusRule.dividendMode != null &&
                    this.t8ProdBonusRule.dividendMode != undefined) {

                    let share = this.t8ProdBonusRule.share;
                    let dividendUnit = this.t8ProdBonusRule.dividendUnit;
                    let totalAmount = (parseFloat(share) * parseFloat(dividendUnit)).toFixed(2);
                    this.$set(this.t8ProdBonusRule, "totalAmount", totalAmount);
                }
            },
            //暂存
            stagingTask() {
                let result = this.$refs.addT8ProdBonusRuleForm.validate();
                if (!result)
                    return false;
                this.saveLoading1 = true;
                this.httpUtil.comnQuery({
                    action: 'T8ProdBonusRule.saveBonusTask',
                    params: this.t8ProdBonusRule,
                }).then(data => {
                    Tools.alert(data.returnmsg || "操作成功");
                    this.saveLoading1 = false;
                })

                this.$router.push({
                    path: "/main/pms/bonus/prodBonusRuleRegular",
                    query: {"prodCode": this.t8ProdBonusRule.prodCode, "id": this.t8ProdBonusRule.id},
                });

            },
            //提交
            submitTask() {
                let result = this.$refs.addT8ProdBonusRuleForm.validate();
                if (!result)
                    return false;
                this.saveLoading = true;
                this.httpUtil.comnQuery({
                    action: 'T8ProdBonusRule.saveBonusTask1',
                    params: this.t8ProdBonusRule,
                }).then(data => {
                    Tools.alert(data.returnmsg || "操作成功");
                    this.saveLoading = false;
                    window.location.reload()
                })


            },
            //预览分红方案
            previewBonusPlan(rule) {
                let onlineUrl = this.httpUtil.onlineUrl;
                this.$refs.onlineEditPopup.popup();

                this.httpUtil.comnQuery({
                    action: 'T8ProdBonusRule.previewBonusPlan',
                    params: {
                        "id": rule.id,
                        "onlineUrl": onlineUrl
                    }
                }).then(data => {
                    if (data.rows.length > 0) {
                        this.viewUrl = data.rows[0].viewUrl;
                    }
                });
            },
            //下载分红方案
            downloadBonusPlan(rule) {
                let filieName = rule.prodName + rule.dividendRegisterDate + "分红方案.docx";
                this.httpUtil.download({
                    url: "/download/server/PmsApp/bonus/downloadBonusPlan.json",
                    params: rule,
                    callback: response => {
                        Tools.alert("下载完成");
                    }
                }, filieName);
            },
            //分红公告预览
            previewBonusNoitce(rule) {
                let onlineUrl = this.httpUtil.onlineUrl;
                this.$refs.onlineEditPopup.popup();

                this.httpUtil.comnQuery({
                    action: 'T8ProdBonusRule.previewBonus',
                    params: {
                        "id": rule.id,
                        "onlineUrl": onlineUrl
                    }
                }).then(data => {
                    if (data.rows.length > 0) {
                        this.viewUrl = data.rows[0].viewUrl;
                    }
                });

            },
            //分红公告下载
            downloadBonusNoitce(rule) {
                let filieName = rule.prodName + rule.dividendRegisterDate + "分红公告.docx";
                this.httpUtil.download({
                    url: "/download/server/PmsApp/bonus/downloadBonusNotice.json",
                    params: rule,
                    callback: response => {
                        Tools.alert("下载完成");
                    }
                }, filieName);
            },
            //发起审批
            approvalTask(row) {
                //1.通过公告id先查询当前公告状态是否可以发起审批
                //findDisclosureNoticeStatus
                this.httpUtil.comnQuery({
                    action: 'T8ProdBonusRule.findT8ProdBonusRules1',
                    params: row
                }).then(data => {
                    /*进行判断*/
                    if (data && data.rows.length > 0 && data.rows[0].processStatus == '2') {
                        this.$set(row, "t8ProdInfoId", this.t8ProdInfoId);
                        /*2.发起审批*/
                        this.httpUtil.comnUpdate({
                            action: "T8ProdBonusRule.submitFlowNotice",
                            params: row,
                            mask: true,
                            successAlert: true,
                        }).then(data => {

                        })
                    } else {
                        Tools.alert("当前公告不能发起审批!!","danger");
                    }
                });
            },
            //审批完成
            approvalFinished(row) {
                //1.通过公告id先查询当前公告状态是否可以发起审批
                //findDisclosureNoticeStatus
                this.httpUtil.comnQuery({
                    action: 'T8ProdBonusRule.findT8ProdBonusRules1',
                    params: row
                }).then(data => {
                    /*进行判断*/
                    if (data && data.rows.length > 0 && data.rows[0].processStatus == '2') {
                        this.$set(row, "t8ProdInfoId", this.t8ProdInfoId);
                        /*2.发起审批*/
                        this.httpUtil.comnUpdate({
                            action: "T8ProdBonusRule.approvalFinished",
                            params: row,
                            mask: true,
                            successAlert: true,
                        }).then(data => {

                        })
                    } else {
                        Tools.alert("当前公告不能发起审批!!","danger");
                    }
                });
            },
            saveModColumns() {
                //获取表格中被选中的数据，如果没有选则的数据，提示，否则验证是否需要转交的用户有填写，没有填写进行提示
                let list = this.$refs.updateGrid.getSelected();
                if (list.length == 0) {
                    Tools.alert("未选择任何需要补录分发的数据！", "danger")
                } else {
                    let result = true;
                    for (let i = 0; i < list.length; i++) {
                        if (list[i].toUserId == '') {
                            result = false;
                            Tools.alert("转交用户为必选项！", "danger");
                            break;
                        }
                    }
                    if (result) {
                        this.httpUtil.comnUpdate({
                            action: "DisclosureNoticeEntryProcess.updateDisNoticeProcess",
                            params: {
                                t8ProdInfoId: this.t8ProdInfoId,
                                jsonData: JSON.stringify(list)
                            },
                            mask: true
                        }).then(data => {
                            this.showSubmitBtn = true;
                            if (data.success) {
                                this.$refs.xpModWordUpdate.close();
                            }
                        });
                    }
                }
                window.location.reload()
            },


        }
    }
</script>

<style lang="scss" scoped>

    @import "../../../styles/T81001.scss";

    .add-btn-div {
        position: relative;
        z-index: 1;
    }

    .add-btn {
        background-color: #4caf50;
        border-radius: 20px;
        box-shadow: 0 4px 5px 0 rgba(76, 175, 80, 0.14), 0 1px 10px 0 rgba(76, 175, 80, 0.12), 0 2px 4px -1px rgba(76, 175, 80, 0.2);
        width: 20px;
        height: 20px;
        line-height: 20.5px;
        font-size: 23px;
        font-weight: 400;
        cursor: pointer;
        color: #FFF;
        text-align: center;
    }

    .edit {
        display: flex;
        flex-direction: row;
        width: 100%;
        height: 700px;

        .word {
            width: 97%;

            iframe {
                width: 100%;
                height: 100%;
            }
        }

    }
</style>
