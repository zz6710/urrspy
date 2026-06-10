<template>
    <div>
        <k-form-search-customize data-target="dividendPlanGrid" v-model="queryParam">
<!--            <k-form-item label="分红方式">-->
<!--                <k-field-select v-model="prodSearchParam.dividendType" data-dict="t8_bonus_type"/>-->
<!--            </k-form-item>-->
            <k-form-item label="分红模式">
                <k-field-select v-model="prodSearchParam.dividendMode" data-dict="t8_dividend_mode"/>
            </k-form-item>
            <k-form-item label="分红权益登记日">
                <k-field-date v-model="queryParamDateRange" data-type="daterange"/>
            </k-form-item>
            <k-form-item label="分红方案状态">
                <k-field-select v-model="prodSearchParam.dividendStatus" data-dict="t8_dividend_status"/>
            </k-form-item>
            <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="initFormData"
                   data-target="addDividendPlanPopup" v-show="showCreate"
                   v-if="global.isShowAuthorityButton('T8ProdDividendPlan.addProdDividendPlan')">
                <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
        </k-form-search-customize>

        <k-grid ref="dividendPlanGrid" data-action="T8ProdDividendPlan.findProdDividendPlan1" @data-row-select="selectRow" :data-autoload="false">
            <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode"/>
            <k-grid-column data-align="center" data-header="产品名称" data-name="prodName"/>
            <k-grid-column data-align="center" data-header="分红模式" data-name="dividendMode" data-dict="t8_dividend_mode"/>
            <k-grid-column data-align="center" data-header="分红方式" data-name="dividendType" data-dict="t8_bonus_type"/>
            <k-grid-column data-align="center" data-header="分红频率" data-name="bonusModel" data-dict="t8_prod_share_bonus_frequency"/>
            <k-grid-column data-align="center" data-header="分红金额" data-name="amount"/>
            <k-grid-column data-align="center" data-header="分红除权日" data-name="dividendExDate"/>
            <k-grid-column data-align="center" data-header="分红权益登记日" data-name="dividendRegisterDate"/>
            <k-grid-column data-align="center" data-header="红利发放日" data-name="handOutDate"/>
            <k-grid-column data-align="center" data-header="客户到账日" data-name="custArrivalDate"/>
            <k-grid-column data-align="center" data-header="产品状态" data-name="prodStatus" data-dict="t8_prod_status"/>
            <k-grid-column data-align="center" data-header="分红方案状态" data-name="dividendStatus" data-dict="t8_dividend_status"/>
            <template slot="operate" slot-scope="scope">
                <k-btn class="md-info md-just-icon md-simple" data-descript="修改产品分红方案" data-functype="POPUP" data-size="mini"
                       data-target="updatePopup"  :data-disabled="scope.row.row.dividendStatus != '0'" v-show="showUpdate"
                       v-if="global.isShowAuthorityButton('T8ProdDividendPlan.updProdDividendPlan')">
                    <md-icon>edit</md-icon>
                </k-btn>
                <k-btn class="md-info md-just-icon md-simple" data-descript="审批" data-functype="POPUP" data-size="mini"
                       data-target="ApprovePopup"  :data-disabled="scope.row.row.dividendStatus != '0'" v-show="showApproval"
                       v-if="global.isShowAuthorityButton('T8ProdDividendPlan.approveProdDividendPlan')">
                    <md-icon>done</md-icon>
                </k-btn>
                <k-btn class="md-danger md-just-icon md-simple"  data-descript="删除产品分红方案" data-functype="SUBMIT"  data-confirm data-type="danger"
                       data-target="dividendPlanGrid" v-show="showDelete"
                       data-action="T8ProdDividendPlan.delProdDividendPlan"  :data-disabled="scope.row.row.dividendStatus != '0'"
                       v-if="global.isShowAuthorityButton('T8ProdDividendPlan.delProdDividendPlan')">
                    <md-icon>close</md-icon>
                </k-btn>
                <k-btn class="md-info md-just-icon md-simple" :data-download-name="scope.row.row.prodName+'-分红方案信息表.docx'"
                       data-descript="下载分红方案信息表" data-functype="DOWNLOAD" data-size="small"
                       data-url="/download/server/PmsApp/prodDividendPlanAttachment/DownloadPlanRecordsFile.json" v-model="scope.row.row">
                    <md-icon>cloud_download</md-icon>
                </k-btn>
                <!-- <k-btn class="md-info md-just-icon md-simple" data-descript="上传分红方案模板"  data-functype="POPUP" data-size="mini"
                      data-target="uploadPlanFile" v-show="showUpload">
                 <md-icon >cloud_upload</md-icon>
               </k-btn> -->
                <!-- <k-btn class="md-info md-just-icon md-simple" :data-download-name="scope.row.row.prodName+'-分红方案模板.docx'"
                          data-descript="下载分红方案模板" data-functype="DOWNLOAD" data-size="small"
                          data-url="/download/server/PmsApp/prodDividendPlanAttachment/DownloadPlanFile.json" v-model="scope.row.row">
                     <md-icon>cloud_download</md-icon>
                </k-btn> -->
                <k-btn class="md-info md-just-icon md-simple" data-descript="上传审批单"  data-functype="POPUP" data-size="mini"
                       data-target="updateFilePopup" v-show="showUpload"
                       v-if="global.isShowAuthorityButton('T8ProdDividendPlan.UploadApprovalForm')">
                    <md-icon >cloud_upload</md-icon>
                </k-btn>
                <k-btn class="md-info md-just-icon md-simple" v-show="showDownload"
                       data-descript="下载审批单" data-functype="POPUP" data-target="downListPOPUP" v-model="scope.row.row">
                    <md-icon>weekend</md-icon>
                </k-btn>
            </template>
        </k-grid>


        <!--    添加分红记录弹出框   -->
        <k-popup ref="addDividendPlanPopup" :dataDialogDrag="true"   data-title="添加产品分红方案">
            <k-form ref="addDividendPlanForm" :data-col="2" >

                <k-form-item label="产品代码">
                    <k-field-text v-model="formData.prodCode" :data-disabled="true"
                                    :data-allowblank="false"/>
                </k-form-item>
                <k-form-item label="产品名称">
                    <k-field-text v-model="formData.prodName" :data-allowblank="false" :data-disabled="true" :data-max-length="128"/>
                </k-form-item>
                <k-form-item label="分红方式">
                    <k-field-select v-model="formData.dividendType" data-dict="t8_bonus_type" :data-disabled="true" :data-max-length="20"/>
                </k-form-item>
                <k-form-item label="分红频率">
                    <k-field-select v-model="formData.bonusModel" data-dict="t8_prod_share_bonus_frequency" :data-disabled="true" :data-max-length="20"/>
                </k-form-item>
                <k-form-item label="分红模式">
                    <k-field-select v-model="formData.dividendMode" data-dict="t8_dividend_mode" :data-max-length="20"/>
                </k-form-item>
                <k-form-item label="分红权益登记日">
                    <k-field-date v-model="formData.dividendRegisterDate" @data-on-change="getTotalVol"/>
                </k-form-item>
                <k-form-item label="分红除权日">
                    <k-field-date v-model="formData.dividendExDate" :dataMinValue="formData.dividendBaseDate"/>
                </k-form-item>
                <k-form-item label="当前份额">
                    <k-field-text v-model="formData.share" data-validate-type="money" data-type="money" data-min-value="(0" :data-max-length="14" data-digits="2"/>
                </k-form-item>
                <k-form-item label="分红金额">
                    <k-field-text v-model="formData.amount" data-validate-type="money" data-type="money" data-min-value="(0" :data-max-length="14" data-digits="2"/>
                </k-form-item>
                <k-form-item label="分红基准日">
                    <k-field-date v-model="formData.dividendBaseDate"/>
                </k-form-item>
                <k-form-item label="红利发放/再投日">
                    <k-field-date v-model="formData.handOutDate" :dataMibValue="formData.dividendExDate"/>
                </k-form-item>
                <k-form-item label="客户到账日">
                    <k-field-date v-model="formData.custArrivalDate"/>
                </k-form-item>
                <k-form-item label="分红清算天数">
                    <k-field-text v-model="formData.liquidationDays" :data-max-length="255"/>
                </k-form-item>
                <k-form-item label="备注">
                    <k-field-text v-model="formData.remarks" :data-max-length="255"/>
                </k-form-item>
                <k-form-footer data-align="center">
                    <k-btn class="btn-custom-primary" data-functype="SUBMIT"
                           data-from="addDividendPlanForm" :data-model="formData"
                           data-action="T8ProdDividendPlan.addProdDividendPlan" data-target="dividendPlanGrid" :data-handler="validate">确定
                    </k-btn>
                    <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
                </k-form-footer>
            </k-form>
        </k-popup>

        <!--修改分红记录弹出框 -->
        <k-popup ref="updatePopup" data-title="修改产品分红方案">
            <k-form ref="updateForm" :data-col="2">
                <k-form-item label="产品代码">
                    <k-field-select v-model="formData.prodCode" data-action="T8Dict.findTaProdInfos"
                                    data-display-field="prodCode,prodName" data-value-field="prodCode"
                                    :data-allowblank="false" :data-disabled="true"  :data-max-length="20"/>
                </k-form-item>
                <k-form-item label="产品名称">
                    <k-field-text v-model="formData.prodName" :data-allowblank="false" :data-disabled="true" :data-max-length="128"/>
                </k-form-item>
                <k-form-item label="分红方式">
                    <k-field-select v-model="formData.dividendType" data-dict="t8_bonus_type" :data-disabled="true" />
                </k-form-item>
                <k-form-item label="分红频率">
                    <k-field-select v-model="formData.bonusModel" data-dict="t8_prod_share_bonus_frequency" :data-disabled="true" :data-max-length="20"/>
                </k-form-item>
                <k-form-item label="分红模式">
                    <k-field-select v-model="formData.dividendMode" data-dict="t8_dividend_mode"/>
                </k-form-item>
                <k-form-item label="分红权益登记日">
                    <k-field-date v-model="formData.dividendRegisterDate"/>
                </k-form-item>
                <k-form-item label="分红除权日">
                    <k-field-date v-model="formData.dividendExDate" :dataMinValue="formData.dividendBaseDate"/>
                </k-form-item>
                <k-form-item label="当前份额">
                    <k-field-text v-model="formData.share" data-validate-type="money" data-type="money" data-min-value="(0" :data-max-length="18" data-digits="2"/>
                </k-form-item>
                <k-form-item label="分红金额">
                    <k-field-text v-model="formData.amount" data-validate-type="money" data-type="money" data-min-value="(0" :data-max-length="18" data-digits="2"/>
                </k-form-item>
                <k-form-item label="分红基准日">
                    <k-field-date v-model="formData.dividendBaseDate"/>
                </k-form-item>
                <k-form-item label="红利发放/再投日">
                    <k-field-date v-model="formData.handOutDate" :dataMibValue="formData.dividendExDate"/>
                </k-form-item>
                <k-form-item label="客户到账日">
                    <k-field-date v-model="formData.custArrivalDate"/>
                </k-form-item>
                <k-form-item label="分红清算天数">
                    <k-field-text v-model="formData.liquidationDays" :data-max-length="255"/>
                </k-form-item>
                <k-form-item label="备注">
                    <k-field-text v-model="formData.remarks" :data-max-length="255"/>
                </k-form-item>

                <k-form-footer data-align="center">
                    <k-btn class="btn-custom-primary" data-functype="SUBMIT"
                           data-from="updateForm" :data-model="formData"
                           data-action="T8ProdDividendPlan.updProdDividendPlan" data-target="dividendPlanGrid" :data-handler="validate">确定
                    </k-btn>
                    <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
                </k-form-footer>
            </k-form>
        </k-popup>

        <!--审批弹出框 -->
        <k-popup ref="ApprovePopup" data-title="发起审批">
            <k-form ref="ApproveForm" :data-col="2">
                <k-form-item label="产品代码">
                    <k-field-select v-model="formData.prodCode" data-action="T8Dict.findTaProdInfos"
                                    data-display-field="prodCode,prodName" data-value-field="prodCode"
                                    :data-allowblank="false" :data-disabled="true" />
                </k-form-item>
                <k-form-item label="产品名称">
                    <k-field-text v-model="formData.prodName" :data-allowblank="false" :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="分红方式">
                    <k-field-select v-model="formData.dividendType" data-dict="t8_bonus_type" :data-disabled="true" />
                </k-form-item>
                <k-form-item label="当前份额">
                    <k-field-text v-model="formData.share" :data-disabled="true" data-type="money"/>
                </k-form-item>
                <k-form-item label="分红模式">
                    <k-field-select v-model="formData.dividendMode" data-dict="t8_dividend_mode" :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="分红金额">
                    <k-field-text v-model="formData.amount" :data-disabled="true" data-type="money"/>
                </k-form-item>
                <k-form-item label="分红基准日">
                    <k-field-date v-model="formData.dividendBaseDate" :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="分红除权日">
                    <k-field-date v-model="formData.dividendExDate" :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="分红权益登记日">
                    <k-field-date v-model="formData.dividendRegisterDate" :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="红利发放/再投日">
                    <k-field-date v-model="formData.handOutDate" :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="客户到账日">
                    <k-field-date v-model="formData.custArrivalDate"/>
                </k-form-item>
                <k-form-item label="分红清算天数">
                    <k-field-text v-model="formData.liquidationDays" :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="备注">
                    <k-field-text v-model="formData.remarks" :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="可分配利润">
                    <k-field-text v-model="formData.profit" data-validate-type="money" data-type="money" data-min-value="(0"
                                  :data-max-length="18" data-digits="2"/>
                </k-form-item>

                <k-form-footer data-align="center">
                    <k-btn class="btn-custom-primary" data-functype="SUBMIT"
                           data-from="updateForm" :data-model="formData"
                           data-action="T8ProdDividendPlan.approveProdDividendPlan" data-target="dividendPlanGrid">确定
                    </k-btn>
                    <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
                </k-form-footer>
            </k-form>
        </k-popup>

        <!-- 上传附件  -->
        <k-popup ref="updateFilePopup" data-title="上传">
            <k-form ref="updateFileForm" :data-col="2">
                <k-form-item style="display:none" label="id">
                    <k-field-text v-model="uploadData.id" :data-allowblank="false" :data-disabled="true"/>
                    <k-field-text v-model="uploadData.prodCode" :data-allowblank="false" :data-disabled="true"/>
                    <k-field-text v-model="uploadData.t8ProdInfoId" :data-allowblank="false" :data-disabled="true"/>
                </k-form-item>
                <k-form-item >
                    <k-field-upload label="附件信息" data-type="file" ref="uploadonAnnexRef" :data-multiple="true" :data-limit=5
                                    :data-error="onAnnexSubmitError" :dataChange="onUploadChange"
                                    :dataHttpRequest="httpRequest"
                                    :data-auto-upload="false">
                    </k-field-upload>
                </k-form-item>
                <k-form-footer data-align="center">
                    <k-btn class="btn-custom-primary" data-target="prodInfoGrid" ref="submitBtn"
                           data-from="minutesOfMeetingForm" :data-model="uploadData" @click="batchSubmit">
                        <span v-show="showSubmitBtn">确定</span>
                        <i v-show="!showSubmitBtn" class="el-icon-loading"/>
                    </k-btn>
                    <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
                </k-form-footer>
            </k-form>
        </k-popup>

        <!--下载附件弹出框-->
        <k-popup ref="downListPOPUP" title="附件信息列表" @data-opened="loadAttachmentInfo" data-width="60%" :data-dialog-drag="true">
            <k-grid ref="downloadAgencyAgreementGrid"  :data-autoload="false"
                    data-action="DocumentAttachment.getAttachmentInfo" >
                <k-grid-column data-align="center" data-header="附件名称" data-name="fileName"/>
                <k-grid-column data-align="center" data-header="创建日期" data-name="crtDate"/>
                <k-grid-column data-align="center" data-header="创建时间" data-name="crtTime"/>
                <template slot="operate" slot-scope="scope">
                    <k-btn class="md-info md-just-icon md-simple" :data-download-name="scope.row.row.fileName"
                           data-descript="下载" data-functype="DOWNLOAD" data-size="small"
                           data-url="/download/server/PmsApp/documentAttachment/downAttachment.json" v-model="scope.row.row">
                        <md-icon>cloud_download</md-icon>
                    </k-btn>
                    <k-btn class="md-danger md-just-icon md-simple"  data-descript="删除附件" data-functype="SUBMIT"  :data-confirm="true"
                           data-target="downloadAgencyAgreementGrid"
                           data-action="T8ProdDividendPlan.deleteFile"
                           v-if="global.isShowAuthorityButton('T8ProdDividendPlan.deleteFile')">
                        <md-icon>close</md-icon>
                    </k-btn>
                </template>
            </k-grid>
        </k-popup>


        <!-- 上传分红方案模板  -->
        <!-- <k-popup ref="uploadPlanFile" data-title="上传">
          <k-form ref="uploadPlanForm" :data-col="2">
            <k-form-item style="display:none" label="id">
              <k-field-text v-model="uploadData.id" :data-allowblank="false" :data-disabled="true"/>
              <k-field-text v-model="uploadData.prodCode" :data-allowblank="false" :data-disabled="true"/>
              <k-field-text v-model="uploadData.t8ProdInfoId" :data-allowblank="false" :data-disabled="true"/>
            </k-form-item>
            <k-form-item >
              <k-field-upload label="附件信息" data-type="file" ref="uploadonAnnexRef" :data-multiple="true" :data-limit=5
                              :data-error="onAnnexSubmitError" :dataChange="onUploadChange"
                              :dataHttpRequest="httpRequest"
                              :data-auto-upload="false">
              </k-field-upload>
            </k-form-item>
            <k-form-footer data-align="center">
              <k-btn class="btn-custom-primary" data-target="prodInfoGrid" ref="submitBtn"
                     data-from="uploadPlanForm" :data-model="uploadData" @click="uploadPlanFile">
                <span v-show="showSubmitBtn">确定</span>
                <i v-show="!showSubmitBtn" class="el-icon-loading"/>
              </k-btn>
              <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
            </k-form-footer>
          </k-form>
        </k-popup> -->

    </div>
</template>

<script>
    import { assign } from "lodash";
    import Tools from "@/utils/tools";

    export default {
        props:{
            prodCode: {
                type:String,
                default:'',
            },
            prodName:{
                type:String,
                default: '',
            },
            dividendType:{
                type:String,
                default:'',
            },
            bonusModel:{
                type:String,
                default:'',
            },
            dividendMode:{
                type:String,
                default:'',
            }
        },
        data() {
            return {
                prodSearchParam: {},
                queryParamDateRange:[],
                prodCode1:'',
                formData: {
                    prodCode: '',
                    prodName: '',
                    dividendType: '',
                    bonusModel:'',
                    dividendMode:'',
                },
                uploadData: {
                    id:'',
                    prodCode: '',
                    t8ProdInfoId: ''
                },
                showSubmitBtn:true,
                fileList:[],
                showCreate:true,//是否显示新增按钮
                showUpdate:true,//是否显示修改按钮
                showApproval:true,//是否显示审批按钮
                showDelete:true,//是否显示删除按钮
                showUpload:true,//是否显示上传按钮
                showDownload:true,//是否显示下载按钮
            };
        },
        created() {
            this.prodCode1 = this.$route.query.prodCode;
            this.$nextTick(()=>{
                //获取需要隐藏按钮属性  rennannan 20210322   将接收到的属性值置为false达到隐藏按钮的效果
                this.global.getHideButtons(this);
                this.$refs.dividendPlanGrid.load({prodCode: this.prodCode1})
            });
        },
        computed: {
            queryParam() {
                return {
                    'prodCode': this.prodSearchParam.prodCode,
                    'prodName': this.prodSearchParam.prodName,
                    'dividendType': this.prodSearchParam.dividendType,
                    'dividendStatus': this.prodSearchParam.dividendStatus,
                    'dividendExStartDate': this.queryParamDateRange ? this.queryParamDateRange[0] : null,
                    'dividendExEndDate': this.queryParamDateRange ? this.queryParamDateRange[1] : null,
                }
            }
        },
        methods: {
            selectRow(row) {
                const _this = this;
                _this.selectRowData = assign({}, row)
                _this.formData = assign({}, row);
            },
            httpRequest(file){
                this.fileData.append('files', file.file);
            },
            onUploadChange(file,fileList){
                this.fileList = fileList;
            },
            onAnnexSubmitError() {
                this.$refs.uploadonAnnexRef.doReset();
                this.showSubmitBtn = true;
            },
            onSubmitAnnexSuccess() {
                this.$refs.uploadonAnnexRef.doReset();
                this.$refs.updateFileForm.reset();
                this.$refs.updateFilePopup.close();

            },

            //   onSubmitSuccess() {
            //   this.$refs.uploadonAnnexRef.doReset();

            //   this.$refs.uploadPlanForm.reset();

            //    this.$refs.uploadPlanFile.close();
            // },
            batchSubmit(){
                let uploadData = this.formData;
                this.showSubmitBtn = false;
                this.fileData = new FormData();
                this.$refs.uploadonAnnexRef.upload();
                this.fileData.append('params', JSON.stringify(uploadData));
                this.httpUtil.upload({
                    url:"/upload-files/server/PmsApp/prodDividendPlanAttachment/upload.json",
                    formData: this.fileData
                }).then(res=>{
                    this.showSubmitBtn = true;
                    Tools.alert(res.data.returnmsg)
                    this.onSubmitAnnexSuccess()
                })
            },

            //  uploadPlanFile(){
            //   let uploadData = this.formData;
            //   this.showSubmitBtn = false;
            //   this.fileData = new FormData();
            //   this.$refs.uploadonAnnexRef.upload();
            //   this.fileData.append('params', JSON.stringify(uploadData));
            //   this.httpUtil.upload({
            //     url:"/upload-files/server/PmsApp/prodDividendPlanAttachment/uploadPlanFile.json",
            //     formData: this.fileData
            //   }).then(res=>{
            //     this.showSubmitBtn = true;
            //     Tools.alert(res.data.returnmsg)
            //     this.onSubmitSuccess();
            //   })
            // },
            prodCodeChange(val){
                const _this = this;
                this.httpUtil.comnQuery({
                    action: "T8ProdInfo.findProdDividendInfo",
                    params: {prodCode: val}
                }).then(data => {
                    //_this.formData.prodName = data.rows[0].prodName
                    _this.$set(_this.formData,"prodName",data.rows[0].prodName);
                    console.log(data.rows[0].prodName)
                    //_this.formData.dividendType = data.rows[0].dividendType
                    _this.$set(_this.formData,"dividendType",data.rows[0].bonusType);
                }).catch({})
            },
            loadAttachmentInfo(){
                this.$refs.downloadAgencyAgreementGrid.load({
                    attachmentType: '7',
                    prodCode: this.formData.prodCode
                })
            },

            validate(param){
                if(param.handOutDate<param.dividendExDate){

                    Tools.alert("红利发放/再投日不得小于分红除权日!","danger");
                    return false;
                }
                else{
                    return true;
                }
            },

            getTotalVol() {
                this.httpUtil.comnQuery({
                    action:'T8ProdDividendPlan.getTotalVol',
                    params:{
                        'prodCode': this.formData.prodCode,
                        'dividendRegisterDate': this.formData.dividendRegisterDate,
                    },
                }).then(data => {
                    this.$set(this.formData,"share",data.returndata.totalVol);
                    this.$set(this.formData,"dividendExDate",data.returndata.dividendExDate);
                })
            },
            initFormData() {
                this.formData = {},
                this.formData.prodCode = this.$route.query.prodCode;
                this.formData.prodName = this.$route.query.prodName;
                this.formData.dividendType = this.$route.query.dividendType;
                this.formData.bonusModel = this.$route.query.bonusModel;
                this.formData.dividendMode = this.$route.query.dividendMode;
            },

        }
    };
</script>
