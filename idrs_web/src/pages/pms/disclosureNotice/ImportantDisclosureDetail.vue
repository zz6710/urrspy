<template>
    <div class="md-card k-card md-theme-default parent-div">
        <div class="form-item prod-panel" style="margin-top:50px;display: -webkit-box;" id="baseInfo">
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
                                    :data-allowblank="false"
                                    :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="分红模式">
                    <k-field-select v-model="t8ProdBonusRule.dividendMode"
                                    data-dict="t8_dividend_mode"
                                    :data-allowblank="false"
                                    :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="权益登记日">
                    <k-field-date v-model="t8ProdBonusRule.dividendRegisterDate"
                                  :data-allowblank="false"
                                  :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="分红基准日">
                    <k-field-date v-model="t8ProdBonusRule.dividendBaseDate"
                                  :data-allowblank="false"
                                  :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="分红除权日">
                    <k-field-date v-model="t8ProdBonusRule.dividendExDate" :data-allowblank="false"
                                  :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="当前份额">
                    <k-field-text v-model="t8ProdBonusRule.share" :data-allowblank="false"
                                  :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="份额日期">
                    <k-field-date v-model="t8ProdBonusRule.shareDate" :data-allowblank="false"
                                  :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="分红总金额">
                    <k-field-text v-model="t8ProdBonusRule.totalAmount"
                                  :data-allowblank="false"
                                  :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="单位分红">
                    <k-field-text v-model="t8ProdBonusRule.dividendUnit"
                                  :data-allowblank="false"
                                  :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="红利发放日">
                    <k-field-date v-model="t8ProdBonusRule.dividendIssueDate"
                                  :data-allowblank="false" :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="红利再投日">
                    <k-field-date v-model="t8ProdBonusRule.handOutDate"
                                  :data-allowblank="false" :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="现金红利到账日">
                    <k-field-date v-model="t8ProdBonusRule.dividendArrivalDate"
                                  :data-allowblank="false" :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="产品经理">
                    <k-field-text v-model="t8ProdBonusRule.prodManager" :data-disabled="true" :data-allowblank="false"/>
                </k-form-item>
                <k-form-item label="投资经理">
                    <k-field-text v-model="t8ProdBonusRule.investManager" :data-disabled="true" :data-allowblank="false"/>
                </k-form-item>
                <k-form-item label="估值经理">
                    <k-field-text v-model="t8ProdBonusRule.valuationManager" :data-disabled="true" :data-allowblank="false"/>
                </k-form-item>
                <k-form-footer data-align="center">
                    <k-btn style="width:100px;" class="btn-custom-primary" data-size="small"
                           @click="previewBonusNoitce(t8ProdBonusRule)" :data-model="this.t8ProdBonusRule">
                        <md-icon md-src="/static/svg/confirm.svg"></md-icon>公告预览
                    </k-btn>
                    <k-btn style="width:100px;" class="btn-custom-plain" data-download-name="分红公告"
                           data-size="small"
                           @click="downloadBonusNoitce(t8ProdBonusRule)"
                           :data-model="this.t8ProdBonusRule">
                        <md-icon md-src="/static/svg/add.svg" />公告下载
                    </k-btn>
                    <k-btn style="width:100px;" class="btn-custom-primary" data-download-name="分红公告"
                           data-size="small"
                           @click="sendChannel(t8ProdBonusRule)"
                           :data-model="this.t8ProdBonusRule">
                        <md-icon md-src="/static/svg/confirm.svg"></md-icon>公告发布
                    </k-btn>
                </k-form-footer>
            </k-form>
        </div>

        <div class="form-item prod-panel" style="display: -webkit-box;" id="channelInfo">
            <div class="title">
                <div class="prod-items"></div>
                <k-field-display class="title-desc" value="渠道信息"></k-field-display>
            </div>

            <div class="add-btn-div">
                <div class="add-btn" @click="addHandler">+</div>
            </div>
            <div style="width:100%;">
                <div style="display: inline-block;position: relative;top:40px;"><label class="el-form-item__label">发件人</label>
                </div>
                <div label="发件邮箱" style="width:225px;">
                    <k-field-text style="margin-left:70px;" v-model="t8ProdBonusRule.sendEmails" :data-disabled="true"/>
                </div>
            </div>

            <k-grid ref="disclosureRuleGrid" @data-row-select="selectRow" :data-autoload="true"
                        data-action="DisclosureNoticeChannel.findDisclosureNoticeChannelsInfo"
                    :data-params="{'disclosureNoticeId':this.id}">
                <k-grid-column data-header="渠道id" data-name="id" :data-hidden="true"></k-grid-column>
                <k-grid-column data-header="发布渠道" data-name="channelName" :data-hidden="false"></k-grid-column>
                <k-grid-column data-header="地址" data-name="emails" :data-hidden="false"></k-grid-column>
                <k-grid-column data-header="发布状态" data-name="noticeChannelPublicStatus" :data-hidden="false"></k-grid-column>
                <k-grid-column data-header="发布日期" data-name="channelPublicDate" :data-hidden="false"></k-grid-column>
                <template slot="operate" slot-scope="scope">
                    <!--            <k-btn data-functype="POPUP" data-size="mini" data-target="editPopup"  class="md-info md-just-icon md-simple"
                                       :data-handler="editDisclosureRule"  data-descript="修改渠道">
                                  <md-icon>edit</md-icon>
                                </k-btn>-->
                    <k-btn class="md-info md-just-icon md-simple" data-functype="SUBMIT"
                           data-action="DisclosureNoticeChannel.sendChannelsEmails" data-size="mini"
                           data-type="danger" :data-model="scope.row.row" data-target="disclosureRuleGrid" :data-confirm="true"
                           data-descript="手动发布">
                        <md-icon>edit</md-icon>
                    </k-btn>
                    <k-btn class="md-info md-just-icon md-simple" data-functype="SUBMIT"
                           data-action="DisclosureNoticeChannel.deleteDisclosureNoticeChannel" data-size="mini"
                           data-type="danger" :data-model="scope.row.row" data-target="disclosureRuleGrid" :data-confirm="true"
                           data-descript="删除渠道">
                        <md-icon>close</md-icon>
                    </k-btn>

                </template>
            </k-grid>
        </div>

        <k-popup ref="onlineEditPopup" data-width="60%"  >
            <div class="edit">
                <div class="word">
                    <iframe name="onlineEdit" id="onlineEdit" :src="viewUrl"></iframe>
                </div>
            </div>
        </k-popup>
        <k-popup ref="addDisclosureRulePopup" data-title="添加渠道">
            <k-form ref="addDisclosureRuleForm" :data-col="2">
                <k-form-item label="id" v-show="false" :data-col="2">
                    <k-field-text v-model="addDisclosureRuleForm.disclosureNoticeId" :data-default-value="this.id"/>
                </k-form-item>
                <k-form-item label="发布渠道" v-show="true">
                    <k-field-select v-model="addDisclosureRuleForm.disclosureNoticeChannelId"
                                    data-action="T8Dict.findDisclosureChannel" :dataAllowblank='false'
                                    data-display-field="channelName" data-value-field="id"/>
                </k-form-item>
            </k-form>
            <div>
                <k-form>
                    <k-form-footer data-align="center">
                        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-from="addDisclosureRuleForm"
                               :data-model="addDisclosureRuleForm" data-action="DisclosureNoticeChannel.addDisclosureNoticeChannel"
                               data-target="disclosureRuleGrid" :data-handler="addDisclosureNoticeChannel">
                            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
                        </k-btn>
                        <k-btn class="btn-custom-plain" data-functype="CLOSE">
                            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
                        </k-btn>
                    </k-form-footer>
                </k-form>
            </div>
        </k-popup>

    </div>

</template>

<script>
    import Tools from "@/utils/tools";

    export default {
        name: "ImportantDisclosureDetail",
        data() {
            return {
                id:'',
                prodCode:'',
                t8ProdBonusRule: {},
                inGroup:'',
                isDisRole:'',
                showButton: true,
                showButton1:true,
                saveLoading: false,//保存按钮是否可点击
                saveLoading1:false,
                state: false,
                DisclosureNoticeEntryProcess:{
                    noticeId: '',
                    $RatGrid: null,
                },
                roleList: [],
                showSubmitBtn: true,
                isDistriSaveBtnDisabled: false,//补录分发保存按钮是否不可点击  默认可以点击
                isUploadBtnEnable: true,//上传按钮是否可以点击
                isDownloadBtnEnable: true,//是否下载按钮可以点击
                fieldList:[],//属性List
                isClick: false,
                viewUrl:'',
                isSubmit:false, //是否可以发起审批
                t8ProdInfoId: '',
                disclosureRule: {
                    $AssetInfoGrid: null,
                },
                addDisclosureRuleForm: {},
            }
        },
        activated() {
            this.refreshPageParam();
        },
        created() {
            this.refreshPageParam();
        },
        methods:{
            refreshPageParam() {
                this.id = this.$route.query.id;
                this.prodCode = this.$route.query.prodCode;
                //查询分红公告
                this.httpUtil.comnQuery({
                    action:'T8ProdBonusRule.findProdBonusRule',
                    params:{id:this.id,prodCode:this.prodCode}
                }).then(data => {
                    this.t8ProdBonusRule = data.rows[0];
                })

                //发件人
                this.httpUtil.comnQuery({

                })
            },
            selectRow(row, column, event) {
                this.selectRowData = Object.assign({}, row)
                this.formData = Object.assign({}, row)
            },
            addDisclosureNoticeChannel() {
                if (this.addDisclosureRuleForm == null) {
                    Tools.alert("请先选择渠道!", "danger");
                    return
                }
                //在这里关闭弹窗
                this.$refs.addDisclosureRulePopup.close();
            },
            //分红公告预览
            previewBonusNoitce(rule) {
                let onlineUrl = this.httpUtil.onlineUrl;
                this.$refs.onlineEditPopup.popup();

                this.httpUtil.comnQuery({
                    action: 'T8ProdBonusRule.previewBonusNoitce',
                    params: {"id":this.id,
                        "onlineUrl":onlineUrl}
                }).then(data => {
                    if (data.rows.length > 0) {
                        this.viewUrl = data.rows[0].viewUrl;
                        console.log( this.viewUrl);
                    }
                });

            },
            //分红公告下载
            downloadBonusNoitce(rule){
                let filieName = rule.prodName +rule.dividendRegisterDate +"分红公告.docx";
                this.httpUtil.download({
                    url: "/download/server/PmsApp/bonus/downloadBonusNoticeVersion.json",
                    params: {id:this.id},
                    callback: response => {
                        Tools.alert("下载完成");
                    }
                }, filieName);
            },
            //发送渠道
            sendChannel(val) {
                this.$set(val,"id",this.id);
                this.httpUtil.comnQuery({
                    action:'T8ProdBonusRule.batchSendEmail',
                    params:val
                }).then(data => {
                    Tools.alert("发布成功");
                })
            },
            addHandler() {
                this.addDisclosureRuleForm = {};
                this.$refs.addDisclosureRulePopup.popup();
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

    .edit{
        display: flex;
        flex-direction: row;
        width: 100%;
        height: 700px;
        .word{
            width: 97%;
            iframe{
                width: 100%;
                height: 100%;
            }
        }

    }
</style>
