<template>
    <div class="md-card k-card md-theme-default parent-div">
        <div class="form-item prod-panel" style="margin-top:50px;display: -webkit-box;" id="baseInfo">
            <div class="title">
                <div class="prod-items"></div>
                <k-field-display class="title-desc" value="基本信息"></k-field-display>
            </div>
            <k-form ref="addformDataForm" :data-col="2">
                <k-form-item label="产品代码">
                    <k-field-select v-model="formData.prodCode" :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="产品名称">
                    <k-field-text v-model="formData.prodName" :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="分红方式">
                    <k-field-select v-model="formData.dividendType"
                                    data-dict="div_method"
                                    :data-disabled="true"
                                    />
                </k-form-item>
                <k-form-item label="分红模式">
                    <k-field-select v-model="formData.dividendMode"
                                    data-dict="t8_dividend_mode"
                                    :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="权益登记日">
                    <k-field-date v-model="formData.dividendRegisterDate"
                                  :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="分红基准日">
                    <k-field-date v-model="formData.dividendBaseDate"
                                  :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="分红除权日">
                    <k-field-date v-model="formData.dividendExDate"
                                  :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="当前份额">
                    <k-field-text v-model="formData.share"
                                  :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="份额日期">
                    <k-field-date v-model="formData.shareDate"
                                  :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="分红总金额">
                    <k-field-text v-model="formData.totalAmount"
                                  :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="单位分红">
                    <k-field-text v-model="formData.dividendUnit"
                                  :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="红利发放日">
                    <k-field-date v-model="formData.dividendIssueDate"
                                  :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="红利再投日">
                    <k-field-date v-model="formData.handOutDate"
                                  :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="现金红利到账日">
                    <k-field-date v-model="formData.dividendArrivalDate"
                                  :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="产品经理">
                    <k-field-text v-model="formData.prodManager" :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="投资经理">
                    <k-field-text v-model="formData.investManager" :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="估值经理">
                    <k-field-text v-model="formData.valuationManager" :data-disabled="true"/>
                </k-form-item>
                <k-form-footer data-align="center">
                    <k-btn style="width:100px;" class="btn-custom-primary" data-size="small"
                           @click="previewBonusPlan(formData)" :data-model="this.formData">
                        <md-icon md-src="/static/svg/confirm.svg"></md-icon>方案预览
                    </k-btn>
                    <k-btn style="width:100px;" class="btn-custom-plain" data-download-name="分红方案"
                           data-size="small"
                           @click="downloadBonusPlan(formData)"
                           :data-model="this.formData">
                        <md-icon md-src="/static/svg/add.svg" />方案下载
                    </k-btn>
                    <k-btn style="width:100px;" class="btn-custom-primary" data-size="small"
                           @click="previewBonusNoitce(formData)" :data-model="this.formData">
                        <md-icon md-src="/static/svg/confirm.svg"></md-icon>公告预览
                    </k-btn>
                    <k-btn style="width:100px;" class="btn-custom-plain" data-download-name="分红公告"
                           data-size="small"
                           @click="downloadBonusNoitce(formData)"
                           :data-model="this.formData">
                        <md-icon md-src="/static/svg/add.svg" />公告下载
                    </k-btn>
                </k-form-footer>
            </k-form>
        </div>
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
        props: {
            formData: {},
        },
        data() {
            return {
                id:'',
                prodCode:'',
                formData: {},
                inGroup:'',
                isDisRole:'',
                showButton: true,
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
            }
        },
        // activated() {
        //     this.refreshPageParam();
        // },
        created() {
            this.refreshPageParam();
        },
        methods:{
            selectRow(row, column, event) {
                this.selectRowData = Object.assign({}, row)
                this.formData = Object.assign({}, row)
            },
            //预览分红方案
            previewBonusPlan(rule){
                let onlineUrl = this.httpUtil.onlineUrl;
                this.$refs.onlineEditPopup.popup();

                this.httpUtil.comnQuery({
                    action: 'T8ProdBonusRule.previewBonusPlan',
                    params: {"id":rule.id,
                        "onlineUrl":onlineUrl}
                }).then(data => {
                    if (data.rows.length > 0) {
                        this.viewUrl = data.rows[0].viewUrl;
                        console.log( this.viewUrl);
                    }
                });
            },
            //下载分红方案
            downloadBonusPlan(rule){
                let filieName = rule.prodName +rule.dividendRegisterDate +"分红方案.docx";
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
                    params: {"id":rule.id,
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
                    url: "/download/server/PmsApp/bonus/downloadBonusNotice.json",
                    params: rule,
                    callback: response => {
                        Tools.alert("下载完成");
                    }
                }, filieName);
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