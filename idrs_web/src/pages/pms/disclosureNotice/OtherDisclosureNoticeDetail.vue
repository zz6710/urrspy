<template>
  <div class="md-card k-card md-theme-default parent-div">
    <div class="form-item prod-panel" style="margin-top:50px;display: -webkit-box;" id="baseInfo">
      <div class="title">
        <div class="prod-items"></div>
        <k-field-display class="title-desc" value="基本信息"></k-field-display>
      </div>
      <k-form ref="addDisclosureNoticeForm" :data-col="2">
        <k-form-item label="id" v-show="false">
          <k-field-text v-model="disclosureNotice.id" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="产品id" v-show="false">
          <k-field-text v-model="disclosureNotice.t8ProdInfoId" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="公告标题" data-input-width="600px">
          <k-field-text v-model="disclosureNotice.noticeTitle" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="产品代码">
          <k-field-text v-model="disclosureNotice.prodCode" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="disclosureNotice.prodName" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="信披规则id" v-show="false">
          <k-field-text v-model="disclosureNotice.t8DisclosureRuleId" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="信披类型">
          <k-field-select v-model="disclosureNotice.disclosureType" data-dict="xp_doc_type" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="信披子类型">
          <k-field-select v-model="disclosureNotice.disclosureSonType" data-dict="xp_son_type" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="当前阶段">
          <k-field-select v-model="disclosureNotice.stage" data-dict="t8_current_stage" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="信披标题" data-input-width="600px">
          <k-field-text v-model="disclosureNotice.ruleName" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="信披模板" data-input-width="600px">
          <k-field-text v-model="disclosureNotice.ruleDocName" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="信披公告" data-input-width="600px">
          <k-field-text v-model="disclosureNotice.noticeTitle" :data-disabled="true"/>
        </k-form-item>
        <k-form-footer data-align="center">

<!--          <k-btn style="width:100px;" class="btn-custom-primary" :data-disabled="inGroup!='1'" :data-model="disclosureNotice"
                 @click="popupEdit(disclosureNotice)" data-functype="POPUP" data-target="disclosureNoticeGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>数据补录
          </k-btn>-->
          <k-btn style="width:100px;" class="btn-custom-plain" :data-download-name="disclosureNotice.noticeTitle"
                 data-descript="模板信息" data-size="small"
                 @click="downloadXPGGTempVersion(disclosureNotice)" :data-model="this.disclosureNotice">
            <md-icon md-src="/static/svg/add.svg" />下载文档
          </k-btn>
          <k-btn style="width:100px;" class="btn-custom-primary" data-functype="POPUP" data-size="small"
                 :data-model="this.disclosureNotice" data-target="addPopup"
                 :data-handler="editHandler" v-if="global.isShowAuthorityButton('DisclosureNotice.upadteDoc')">
            <md-icon md-src="/static/svg/add.svg" />上传文档
          </k-btn>
          <k-btn style="width:100px;" class="btn-custom-primary" :data-model="disclosureNotice"
                 @click="popupEdit1(disclosureNotice)" data-functype="POPUP">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>参数调整
          </k-btn>
          <k-btn style="width:100px;" class="btn-custom-primary" :data-model="disclosureNotice"
                 @click="submitFlowParam(disclosureNotice)"
                 v-if="global.isShowAuthorityButton('DisclosureNotice.issuedFlow')">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>发起审批
          </k-btn>
          <k-btn style="width:100px;" class="btn-custom-primary" :data-model="disclosureNotice"
                 @click="cancleNotice(disclosureNotice)"
                 v-if="global.isShowAuthorityButton('DisclosureNotice.cancleNotice')">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>公告取消
          </k-btn>
          <k-btn style="width:100px;" class="btn-custom-primary" :data-model="disclosureNotice"
                 @click="sendNotice(disclosureNotice)"
                 v-if="global.isShowAuthorityButton('DisclosureNotice.sendNotice')">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>公告发布
          </k-btn>

          <k-btn style="width:100px;" v-show="false" class="btn-custom-plain" :data-download-name="disclosureNotice.ruleDocName"
                 data-descript="模板信息" data-functype="DOWNLOAD" data-size="small"
                 data-url="/download/server/PmsApp/print/saveXPGGTempVersion.json" :data-model="this.disclosureNotice">
            <md-icon md-src="/static/svg/add.svg" />保存文档
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
          <k-field-text style="margin-left:70px;" v-model="disclosureNotice.sendEmails" :data-disabled="true"/>
        </div>
      </div>

      <k-grid ref="disclosureRuleGrid" @data-row-select="selectRow" :data-autoload="false"
              data-action="DisclosureNoticeChannel.findDisclosureNoticeChannelsInfo"
              @init="(grid)=>{this.disclosureRule.$AssetInfoGrid = grid}">
        <k-grid-column data-header="渠道id" data-name="id" :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="发布渠道" data-name="channelName" :data-hidden="false"></k-grid-column>
        <k-grid-column data-header="地址" data-name="emails" :data-hidden="false"></k-grid-column>
        <k-grid-column data-header="发布状态" data-name="noticeChannelPublicStatus" :data-hidden="false"></k-grid-column>
        <k-grid-column data-header="发布日期" data-type="date" data-name="channelPublicDate" :data-hidden="false"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-functype="SUBMIT"
                 data-url="/server/form/PmsApp/sendIssuedMsg.json" data-size="mini"
                 data-type="danger" :data-model="scope.row.row" data-target="disclosureRuleGrid" :data-confirm="true"
                 data-descript="手动发布" @click="senMsg(scope.row.row)">
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

    <k-popup ref="addPopup" title="上传报告文档">
      <k-form ref="addForm" data-ui="element">

        <k-form-item label="公告名称" v-show="false">
          <k-field-text v-model="uploadFormData.t8DisclosureNoticeId" :data-default-value="disclosureNotice.id"
                        :data-allowblank="true" :data-max-length="128"/>
        </k-form-item>
        <k-form-item label="报告名称">
          <k-field-text v-model="uploadFormData.modName" :data-allowblank="false" :data-max-length="128"/>
        </k-form-item>

        <k-form-item label="信披类型">
          <k-field-select v-model="uploadFormData.disclosureType" data-action="T8Dict.XPPrintDoc"
                          data-display-field="itemval" data-value-field="itemkey" :data-allowblank="false"
                          :data-disabled="true"
                          @data-on-change="onDocTypeChange"/>
        </k-form-item>
        <k-form-item label="信披子类型" v-if="uploadFormData.disclosureType=='5'||uploadFormData.disclosureType=='6'">
          <k-field-select v-model="uploadFormData.disclosureSonType" :data-data="addDocTypeDict"
                          :data-allowblank="false"
                          data-value-field="value" data-display-field="text" :data-disabled="true"/>
        </k-form-item>

        <k-form-item label="文档版本">
          <k-field-text v-model="uploadFormData.version" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="备注" :dataCol="2">
          <k-field-text v-model="uploadFormData.remark" inputType="textarea" :rows="1" :data-max-length="256"/>
        </k-form-item>
        <k-form-item label="附件" data-ui="element" data-input-width="500px">

          <k-field-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit=1
                          :data-error="onSubmitError" :dataChange="onUploadChange" data-accept=".docx"
                          :dataHttpRequest="httpRequest"
                          :data-auto-upload="false">
          </k-field-upload>

        </k-form-item>
        <k-form-footer data-align="center">


          <k-btn class="btn-custom-primary" data-target="prodInfoGrid" ref="submitBtn" data-from="addForm"
                 :data-model="uploadFormData" @click="submitUploadParams">
            <span v-show="showSubmitBtn">确定</span>
            <i v-show="!showSubmitBtn" class="el-icon-loading"/>
          </k-btn>

          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="addDisclosureRulePopup" data-title="添加渠道">
      <k-form ref="addDisclosureRuleForm" :data-col="2">
        <k-form-item label="id" v-show="false" :data-col="2">
          <k-field-text v-model="addDisclosureRuleForm.disclosureNoticeId" :data-default-value="disclosureNotice.id"/>
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
    <k-popup ref="editDisclosureRulePopup" data-title="新增">
      <k-form ref="editDisclosureRuleForm" :data-col="2">
        <k-form-item label="发布渠道" v-show="true">
          <k-field-text v-model="editDisclosureRuleForm.prodCode"/>
        </k-form-item>
        <k-form-item label="地址" v-show="true">
          <k-field-text v-model="editDisclosureRuleForm.t8ProdInfoId"/>
        </k-form-item>
      </k-form>
      <div>
        <k-form>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-from="editDisclosureRuleForm"
                   :data-model="editDisclosureRuleForm" data-action="DisclosureNoticeChannel.addDisclosureNoticeChannel"
                   data-target="disclosureRuleGrid">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
            </k-btn>
          </k-form-footer>
        </k-form>
      </div>
    </k-popup>
    <k-popup ref="filePopup" title="上传托管机构附件">
      <k-form ref="fileForm" data-ui="element">
        <k-form-item label="托管机构附件" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="fileUploadRef" :data-multiple="false" :data-limit=1
                          :data-error="onFileSubmitError" :data-change="onCompareFileChange"
                          :data-success="onFileSubmitSuccess"
                          :data-auto-upload="false"
                          data-upload-url="/upload/server/PmsApp/disclosure/uploadApprovalFile.json">
          </k-field-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="printTempGrid" ref="fileSubmitBtn"
                 data-from="fileForm" :data-model="truteeApproval" :data-handler="fileSubmitUploadParam">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
  </div>
</template>

<script>
import Tools from "@/utils/tools";

export default {
  name: "DisclosureNoticeDetail",
  data() {
    return {
      downloadFlag: true,
      spyj: [
        {label: '同意', value: '1'},
        {label: '不同意', value: '0'},
      ],
      uploadFormData: {},
      formData: {},
      roleList: [],
      userList: [],
      showSubmitBtn: true,
      disclosureNotice: {},
      selectRowData: {},
      isCompanyCheckFlag: '',
      isTruteeCheckFlag: '',
      DisclosureNoticeProcess: {
        t8DisclosureNoticeId: '',
        $RatGrid: null,
      },
      disclosureRule: {
        $AssetInfoGrid: null,
      },
      addDisclosureRuleForm: {},
      editDisclosureRuleForm: {},
      filFormData: {
        prodCode: '',
        prodName: '',
        documentType: '',
        version: '',
        isTemplateFile: '',
        t8TruteeInfoId: ''
      },
      uploadFileName: '',
      truteeApproval: {},
      inGroup: '',
      t8ProdInfoId: '',
      prodCode: '',
      addDocTypeDict: {},
      t8DisclosureNoticeId: '',
      sysparamTrusteeApproval: '',
      prodBaseDate: '',
      fileNameList: [],
      isClick: false,
    };
  },
  watch: {
    'truteeApproval.trusteeExamine': {

      handler: function (val) {
        //console.log("warch中是否托管行审批=:>>>>>",val);
        this.$set(this.truteeApproval, "trusteeExamine", val);
        this.changedTrutee(this.truteeApproval.trusteeExamine);
        //console.log("warch中是否托管行审批=:>>>>>",this.truteeApproval.trusteeExamine);
      },
      deep: true
    },
    'truteeApproval.attachmentUrl': {
      handler: function (attachmentUrl) {
        console.log("附件地址监听", attachmentUrl)
        if (attachmentUrl == '' || attachmentUrl == null) {
          this.downloadFlag = true
        } else {
          this.downloadFlag = false
        }
      }
    },
    'uploadFormData.disclosureType': {

      handler: function (val) {
        //console.log("warch中是否托管行审批=:>>>>>",val);

        this.onDocTypeChange(val);
        //console.log("warch中是否托管行审批=:>>>>>",this.truteeApproval.trusteeExamine);
      },
      deep: true
    },

  },
  computed: {
    queryParam() {
      return {
        'prodCode': this.prodSearchParam.prodCode,
        'publishStatus': this.prodSearchParam.publishStatus,
        'stage': this.prodSearchParam.stage,
        'disclosureType': this.prodSearchParam.disclosureType,
      }
    }
  },
  activated() {
    let inGroup = this.$route.query.inGroup;
    this.inGroup = inGroup;
    //console.log("this.inGroup=:>>>>",this.inGroup);
    this.$nextTick(() => {
      //接收运营导航产品代码并刷新表格
      let id = this.$route.query.id;
      this.t8DisclosureNoticeId = id;
      let t8ProdInfoId = this.$route.query.t8ProdInfoId;
      this.t8ProdInfoId = t8ProdInfoId;
      //console.log("t8ProdInfoid=:>>>>>>",t8ProdInfoId);
      let prodCode = this.$route.query.prodCode;
      this.prodCode = prodCode;
      //console.log("prodCode=:>>>>>",prodCode);
      if (id != '' && id != undefined) {
        this.httpUtil.comnQuery({
          action: 'DisclosureNotice.findDisclosureNotices',
          params: {
            id: id,
          }
        }).then(data => {
          this.$nextTick(() => {
            if (data.rows.length > 0) {
              this.$set(this.disclosureNotice, "id", data.rows[0].id);
              this.$set(this.disclosureNotice, "t8ProdInfoId", data.rows[0].t8ProdInfoId);
              this.$set(this.disclosureNotice, "t8DisclosureRuleId", data.rows[0].t8DisclosureRuleId);
              this.$set(this.disclosureNotice, "prodName", data.rows[0].prodName);
              this.$set(this.disclosureNotice, "prodCode", data.rows[0].prodCode);
              this.$set(this.disclosureNotice, "noticeTitle", data.rows[0].noticeTitle);
              this.$set(this.disclosureNotice, "disclosureType", data.rows[0].disclosureType);
              this.$set(this.disclosureNotice, "disclosureSonType", data.rows[0].disclosureSonType);
              this.$set(this.disclosureNotice, "noticeProcess", data.rows[0].noticeProcess);
              this.$set(this.disclosureNotice, "stage", data.rows[0].stage);
              this.$set(this.disclosureNotice, "ruleName", data.rows[0].ruleName);
              this.$set(this.disclosureNotice, "ruleDocName", data.rows[0].ruleDocName);
              this.$set(this.disclosureNotice, "prodBaseDate", data.rows[0].prodBaseDate);
              this.$set(this.disclosureNotice, "sendEmails", data.rows[0].sendEmails);

              this.httpUtil.comnQuery({
                action: "DisclosureMod.getXPTypeByDocType",
                params: {disclosureType: this.disclosureNotice.disclosureType}
              }).then(data => {
                this.addDocTypeDict = data.rows;
              }).catch({});
            }
          })
          this.loadData(this.t8DisclosureNoticeId);
        });
        //console.log("this.t8ProdInfoId=:>>>>>>",this.t8ProdInfoId);
        this.httpUtil.comnQuery({
          action: 'DisclosureTruteeApproval.findDisclosureNoticeApprovals',
          params: {
            disclosureNoticeId: id,
          }
        }).then(data => {
          this.$nextTick(() => {
            if (data.rows.length > 0) {
              this.sysparamTrusteeApproval = data.rows[0].sysparamTrusteeApproval;
              this.$set(this.truteeApproval, "id", data.rows[0].id);
              this.$set(this.truteeApproval, "disclosureNoticeId", data.rows[0].disclosureNoticeId);
              this.$set(this.truteeApproval, "t8ProdInfoId", this.t8ProdInfoId);
              this.$set(this.truteeApproval, "truteeApprovalResult", data.rows[0].truteeApprovalResult);
              this.$set(this.truteeApproval, "truteeApprovalResultDesc", data.rows[0].truteeApprovalResultDesc);
              this.$set(this.truteeApproval, "attachmentUrl", data.rows[0].attachmentUrl);
              this.$set(this.truteeApproval, "truteeNotice", data.rows[0].truteeNotice);
              this.$set(this.truteeApproval, "approvalStatus", data.rows[0].approvalStatus);
              this.$set(this.truteeApproval, "recheckStatus", data.rows[0].recheckStatus);
              this.$set(this.truteeApproval, "truteeName", data.rows[0].truteeName);
              this.$set(this.truteeApproval, "truteeEmail", data.rows[0].truteeEmail);
              this.$set(this.truteeApproval, "outTruteeName", data.rows[0].outTruteeName);
              this.$set(this.truteeApproval, "outTruteeEmail", data.rows[0].outTruteeEmail);
              this.$set(this.truteeApproval, "trusteeExamine", data.rows[0].trusteeExamine);
              this.$set(this.truteeApproval, "uploadFileName", data.rows[0].uploadFileName);
              if (data.rows[0].trusteeExamine == "1") {
                this.isTruteeCheckFlag = "1";
              } else {
                this.isTruteeCheckFlag = "0";
              }
            }
          })
        });
      }
      ;
    });
    this.httpUtil.comnQuery({
      action: "User.getAllUser",
      params: null
    }).then(data => {
      if (data.rows.length > 0) {
        this.userList = data.rows;
        //console.log(this.roleList);
      }
    }).catch({});
    this.httpUtil.comnQuery({
      action: "Role.findAll",
      params: null
    }).then(data => {
      if (data.rows.length > 0) {
        this.roleList = data.rows;
        //console.log(this.roleList);
      }
    }).catch({})
  },
  created() {
    let inGroup = this.$route.query.inGroup;
    this.inGroup = inGroup;
    //console.log("this.inGroup=:>>>>",this.inGroup);
    this.$nextTick(() => {
      //接收运营导航产品代码并刷新表格
      let id = this.$route.query.id;
      this.t8DisclosureNoticeId = id;
      let t8ProdInfoId = this.$route.query.t8ProdInfoId;
      this.t8ProdInfoId = t8ProdInfoId;
      //console.log("t8ProdInfoid=:>>>>>>",t8ProdInfoId);
      let prodCode = this.$route.query.prodCode;
      this.prodCode = prodCode;
      //console.log("prodCode=:>>>>>",prodCode);
      if (id != '' && id != undefined) {
        this.httpUtil.comnQuery({
          action: 'DisclosureNotice.findDisclosureNotices',
          params: {
            id: id,
          }
        }).then(data => {
          this.$nextTick(() => {
            if (data.rows.length > 0) {
              this.$set(this.disclosureNotice, "id", data.rows[0].id);
              this.$set(this.disclosureNotice, "t8ProdInfoId", data.rows[0].t8ProdInfoId);
              this.$set(this.disclosureNotice, "t8DisclosureRuleId", data.rows[0].t8DisclosureRuleId);
              this.$set(this.disclosureNotice, "prodName", data.rows[0].prodName);
              this.$set(this.disclosureNotice, "prodCode", data.rows[0].prodCode);
              this.$set(this.disclosureNotice, "noticeTitle", data.rows[0].noticeTitle);
              this.$set(this.disclosureNotice, "disclosureType", data.rows[0].disclosureType);
              this.$set(this.disclosureNotice, "disclosureSonType", data.rows[0].disclosureSonType);
              this.$set(this.disclosureNotice, "noticeProcess", data.rows[0].noticeProcess);
              this.$set(this.disclosureNotice, "stage", data.rows[0].stage);
              this.$set(this.disclosureNotice, "ruleName", data.rows[0].ruleName);
              this.$set(this.disclosureNotice, "ruleDocName", data.rows[0].ruleDocName);
              this.$set(this.disclosureNotice, "prodBaseDate", data.rows[0].prodBaseDate);
              this.$set(this.disclosureNotice, "sendEmails", data.rows[0].sendEmails);

              this.httpUtil.comnQuery({
                action: "DisclosureMod.getXPTypeByDocType",
                params: {disclosureType: this.disclosureNotice.disclosureType}
              }).then(data => {
                this.addDocTypeDict = data.rows;
              }).catch({});
            }
          })
          this.loadData(this.t8DisclosureNoticeId);
        });
        //console.log("this.t8ProdInfoId=:>>>>>>",this.t8ProdInfoId);
        this.httpUtil.comnQuery({
          action: 'DisclosureTruteeApproval.findDisclosureNoticeApprovals',
          params: {
            disclosureNoticeId: id,
          }
        }).then(data => {
          this.$nextTick(() => {
            if (data.rows.length > 0) {
              this.sysparamTrusteeApproval = data.rows[0].sysparamTrusteeApproval;
              this.$set(this.truteeApproval, "id", data.rows[0].id);
              this.$set(this.truteeApproval, "disclosureNoticeId", data.rows[0].disclosureNoticeId);
              this.$set(this.truteeApproval, "t8ProdInfoId", this.t8ProdInfoId);
              this.$set(this.truteeApproval, "truteeApprovalResult", data.rows[0].truteeApprovalResult);
              this.$set(this.truteeApproval, "truteeApprovalResultDesc", data.rows[0].truteeApprovalResultDesc);
              this.$set(this.truteeApproval, "attachmentUrl", data.rows[0].attachmentUrl);
              this.$set(this.truteeApproval, "truteeNotice", data.rows[0].truteeNotice);
              this.$set(this.truteeApproval, "approvalStatus", data.rows[0].approvalStatus);
              this.$set(this.truteeApproval, "recheckStatus", data.rows[0].recheckStatus);
              this.$set(this.truteeApproval, "truteeName", data.rows[0].truteeName);
              this.$set(this.truteeApproval, "truteeEmail", data.rows[0].truteeEmail);
              this.$set(this.truteeApproval, "outTruteeName", data.rows[0].outTruteeName);
              this.$set(this.truteeApproval, "outTruteeEmail", data.rows[0].outTruteeEmail);
              this.$set(this.truteeApproval, "trusteeExamine", data.rows[0].trusteeExamine);
              this.$set(this.truteeApproval, "uploadFileName", data.rows[0].uploadFileName);
              if (data.rows[0].trusteeExamine == "1") {
                this.isTruteeCheckFlag = "1";
              } else {
                this.isTruteeCheckFlag = "0";
              }
            }
          })
        });
      }
      ;
    });
    this.httpUtil.comnQuery({
      action: "User.getAllUser",
      params: null
    }).then(data => {
      if (data.rows.length > 0) {
        this.userList = data.rows;
        //console.log(this.roleList);
      }
    }).catch({});
    this.httpUtil.comnQuery({
      action: "Role.findAll",
      params: null
    }).then(data => {
      if (data.rows.length > 0) {
        this.roleList = data.rows;
        //console.log(this.roleList);
      }
    }).catch({})
  },
  methods: {
    senMsg(row){
      //console.log(row)
      //this.disclosureNotice.emails=row.emails
      row.disclosureNoticeId = this.disclosureNotice.id
      row.disclosureType = this.disclosureNotice.disclosureType
      row.prodName=this.disclosureNotice.prodName
      row.prodCode=this.disclosureNotice.prodCode
      row.prodBaseDate=this.disclosureNotice.prodBaseDate
      row.noticeTitle=this.disclosureNotice.noticeTitle
      row.ruleName=this.disclosureNotice.ruleName
      row.ruleDocName=this.disclosureNotice.ruleDocName
      row.t8DisclosureRuleId=this.disclosureNotice.t8DisclosureRuleId
      row.t8ProdInfoId=this.disclosureNotice.t8ProdInfoId
    },
    trusteeshipSuccess() {
      console.log("发送托管行成功。。。")
      this.sendTrusteeship = true
    },
    addDisclosureNoticeChannel() {
      if (this.addDisclosureRuleForm == null) {
        Tools.alert("请先选择渠道!", "danger");
        return
      }
      //在这里关闭弹窗
      this.$refs.addDisclosureRulePopup.close();
    },
    onDocTypeChange(disclosureType) {

      this.httpUtil.comnQuery({
        action: "DisclosureMod.getXPTypeByDocType",
        params: {disclosureType: disclosureType}
      }).then(data => {
        this.addDocTypeDict = data.rows;
      }).catch({})
    },
    validateForm() {
      var validate = this.$refs.addForm.validate();
      if (validate) {
        let formData = this.formData;
        let temp = document.getElementsByClassName('upload-demo');
        let lis = temp[0].childNodes[1].childNodes.length;
        if (lis > 0) {
          this.$refs.uploadRef.upload(formData);
        } else {
          Tools.alert("上传文件不能为空!", "danger");
          this.showSubmitBtn = true;
          return false;
        }
      }
    },
    onUploadChange(file, fileList) {
      this.fileList = {};
      this.fileList = fileList;
    },
    httpRequest(file) {
      this.fileData.append('files', file.file);
    },
    onSubmitError() {
      this.$refs.uploadRef.doReset();
      this.$refs.submitBtn.setIconStyle(1, []);
      this.showSubmitBtn = true;
    },
    onSubmitSuccess() {
      this.$refs.uploadRef.doReset();
      this.$refs.addForm.reset();
      this.$refs.addPopup.close();
      //this.$refs.disclosureRuleGrid.load();
    },
    editHandler(value) {
      this.uploadFormData = {};
      //this.onDocTypeChange(value.disclosureType);
      this.$set(this.uploadFormData, "disclosureType", value.disclosureType);
      this.$set(this.uploadFormData, "disclosureSonType", value.disclosureSonType);
      this.$set(this.uploadFormData, "prodCode", this.prodCode);
      this.$set(this.uploadFormData, "t8DisclosureNoticeId", this.t8DisclosureNoticeId);
      this.uploadFormData.remark = '';
      this.httpUtil.comnQuery({
        action: "DisclosureNoticeProcess.findMaxVersions",
        params: {t8DisclosureNoticeId: this.t8DisclosureNoticeId}
      }).then(data => {
        this.$set(this.uploadFormData, "version", data.returndata.version);

      }).catch({})
    },
    submitUploadParams() {
      let urlPath = window.document.location.href;  //浏览器显示地址 http://10.1.20.88:8201/xxx/xxx
      let docPath = "8201"; //服务器相对地址 8201/xxx/xxx
      let index = urlPath.indexOf(docPath);
      let serverPath = urlPath.substring(0, index);
      let onlineUrl = this.httpUtil.onlineUrl;
      if (onlineUrl != "undefined" && onlineUrl != null && onlineUrl != "") {
        this.uploadFormData.onlineUrl = onlineUrl;
      } else {
        this.uploadFormData.onlineUrl = serverPath + "8201";
      }
      let temp = document.getElementsByClassName('upload-demo');
      if (this.uploadFormData.modName == null || this.uploadFormData.modName == '' || this.uploadFormData.modName == undefined) {
        Tools.alert("报告名称不能为空!", "danger")
        this.$refs.submitBtn.setIconStyle(1, []);
        return false;
      }
      if (this.fileList == null || this.fileList == '' || this.fileList == undefined) {
        Tools.alert("上传文件不能为空!", "danger")
        this.$refs.submitBtn.setIconStyle(1, []);
        return false;
      }
      this.fileNameList = [];
      for (let i in this.fileList) {
        this.fileNameList.push(this.fileList[i].name);
      }
      this.uploadFormData['fileNameList'] = JSON.stringify(this.fileNameList);
      this.showSubmitBtn = false;
      let str = '';


      this.$set(this.uploadFormData, 'tempName', this.fileNameList[0].substring(0, this.fileNameList[0].lastIndexOf(".")));
      let uploadData = this.uploadFormData;

      this.fileData = new FormData();
      this.$refs.uploadRef.upload();
      this.fileData.append('params', JSON.stringify(uploadData));
      this.httpUtil.upload({
        url: "/upload-files/server/PmsApp/xpdoc/handUploadTemp.json",
        formData: this.fileData
      }).then(res => {
          this.showSubmitBtn = true;
          if (res.data.success) {//如果没有审批流获取上传后的提示信息
            Tools.alert(res.data.returnmsg);
            this.onSubmitSuccess()
          } else {
            str = res.data.returnmsg;
            Tools.alert(str, 'danger');
          }
        }
      )
    },
    //下载定期报告
    downloadXPGGTempVersion(rows) {
      //console.log('文件名称', this.disclosureNotice.noticeTitle)
/*      var filieName = rows.noticeTitle + ".docx";
      this.httpUtil.download({
        url: "/download/server/PmsApp/print/downloadIssued.json",
        params: rows,
        callback: response => {
          console.log(response)
          Tools.alert("下载完成");
        }
      }, filieName);*/



      //console.log('文件名称', this.disclosureNotice.noticeTitle)
      var filieName = rows.noticeTitle+".docx";

      this.httpUtil.comnQuery({
        action: "DisclosureNoticeProcess.findMaxVersions",
        params: {t8DisclosureNoticeId: this.t8DisclosureNoticeId}
      }).then(data => {
        let docType = data.returndata.docType;
        //console.log("docType=:>>>>",docType);
        //console.log("data=:>>>>",data);
        rows.fileName = data.returndata.fileName;
        rows.filePath = data.returndata.filePath;
        rows.docType = data.returndata.docType;
        rows.version = data.returndata.version;
        if ("1" === docType) {
          Tools.confirm(() => {
              this.httpUtil.download({
                url: "/download/server/PmsApp/print/downloadIssued.json",
                params: rows,
                callback: response => {
                  //response)
                  Tools.alert("下载完成");
                }
              }, filieName);
            },
            "该模板为业务人员手动上传,内容可能与补录界面信息有差别,是否下载?"
          )
        } else {
          this.httpUtil.download({
            url: "/download/server/PmsApp/print/downloadIssued.json",
            params: rows,
            callback: response => {
              //response)
              Tools.alert("下载完成");
            }
          }, filieName);
        }
      }).catch({})

    },
    beforeSubmit(value) {
      value.id = this.truteeApproval.id
      //console.log("this.truteeApproval.truteeApprovalResult=:>>>>",this.truteeApproval.truteeApprovalResult);
      console.log("开启审批：", value)
      if (this.truteeApproval.truteeApprovalResult === '1') {

        if (this.sysparamTrusteeApproval === this.truteeApproval.truteeApprovalResultDesc) {
          this.$set(this.truteeApproval, "recheck", '0')
          value.recheck = '0';
          this.httpUtil.comnUpdate({
            action: "DisclosureTruteeApproval.updateDisclosureNoticeApproval2",
            params: value,
            mask: true
          }).then(data => {
            this.$refs.trusteeBtn.setIconStyle(1, []);
          });
        } else {
          this.$set(this.truteeApproval, "recheck", '1')
          value.recheck = '1';
          this.$confirm('托管意见说明与默认值不一致,提交将发起复核,是否提交?', '提示', {}).then(confirm => {
            if (confirm) {
              this.httpUtil.comnUpdate({
                action: "DisclosureTruteeApproval.updateDisclosureNoticeApproval",
                params: value,
                mask: true
              }).then(data => {
                this.$refs.trusteeBtn.setIconStyle(1, []);
              });
              //this.$refs.upload.submit()
              setTimeout(() => {
                this.$emit('ok')
              }, 500)
            }
          }).catch(error => {
            console.log('取消提交')
          })
        }
      } else {
        this.$set(this.truteeApproval, "recheck", '0')
        value.recheck = '0';
        this.httpUtil.comnUpdate({
          action: "DisclosureTruteeApproval.updateDisclosureNoticeApproval2",
          params: value,
          mask: true
        }).then(data => {
          this.$refs.trusteeBtn.setIconStyle(1, []);
        });
      }
    },
    submitFlowParam(row) {
      //console.log("审批流",row)
      let arr = [];
      let truteeApproval = this.truteeApproval;
      let disclosureNotice = this.disclosureNotice;
      let disclosureNoticeChannel = this.disclosureRule.$AssetInfoGrid.list;
      arr['disclosureType']=row.disclosureType
      arr['id'] = this.disclosureNotice.id;
      arr['truteeApproval'] = JSON.stringify(truteeApproval);
      arr['disclosureNotice'] = JSON.stringify(disclosureNotice);
      arr['disclosureNoticeChannel'] = JSON.stringify(disclosureNoticeChannel);
      let str = '';
      this.httpUtil.comnUpdate({
        action: "DisclosureNotice.issuedFlow",
        params: this.disclosureNotice,
        mask: false,
        successAlert: true,
      }).then(data => {

      })

    },
    cancelNotice(row) {

      let disclosureNotice = this.disclosureNotice;

      this.httpUtil.comnUpdate({
        action: "DisclosureNotice.cancleNotice",
        params: row,
        mask: false,
        successAlert: true,
      }).then(data => {
        this.showSubmitBtn = true;
      })

    },
    sendNotice(row) {

      let disclosureNotice = this.disclosureNotice;

      this.httpUtil.comnUpdate({
        action: "DisclosureNotice.sendNotice",
        params: {"id": disclosureNotice.id},
        mask: false,
        successAlert: true,
      }).then(data => {
        this.showSubmitBtn = true;
        //刷新公告渠道表格
        this.loadData(this.t8DisclosureNoticeId);
      })


    },
    popupEdit1(row) {
      //跳转到产品信息调整
      this.$router.push({
        path: '/main/pms/M81/prodAdjust/M8ProdAdjust',
        query: {prodCode: row.prodCode}
      });
    },
    submitTrusteeApproval(row) {
      /* this.t8ProdInfoId = row.t8ProdInfoId;
       this.httpUtil.comnUpdate({
         action: "DisclosureNotice.updateDisclosureNoticeStatusTrustee",
         params: {
           t8ProdInfoId: this.t8ProdInfoId,
         },
         mask: true
       }).then(data => {
         this.showSubmitBtn = true
         if (data.success) {
           this.$refs.xpModWordUpdate.close();
           //this.$refs.printTempGrid.load();
         }
       });*/
    },
    fileSubmitUploadParam() {
      let formData = this.truteeApproval;
      //console.log("this.$refs.fileForm=:>>>>",formData);
      this.$refs.fileUploadRef.upload(formData);
    },
    onFileSubmitError() {
      this.truteeApproval.uploadFileName = '';
      this.$refs.fileUploadRef.doReset();
      this.$refs.fileSubmitBtn.setIconStyle(1, []);
    },
    onFileSubmitSuccess(res) {
      console.log("文件上传成功：", res.response.returnmsg)
      //console.log("success,fileName=:>>>",this.uploadFileName);
      this.$set(this.truteeApproval, "uploadFileName", this.uploadFileName);
      this.$set(this.truteeApproval, "attachmentUrl", res.response.returnmsg);
      this.truteeApproval.attachmentUrl = res.response.returnmsg
      console.log("上传成功后", this.truteeApproval.attachmentUrl)
      //console.log("success,this.truteeApproval.uploadFileName=:>>>",this.truteeApproval.uploadFileName);
      //this.truteeApproval.uploadFileName=this.uploadFileName;
      this.$refs.fileUploadRef.doReset();
      this.$refs.fileForm.reset();
      this.$refs.filePopup.close();
      //this.$refs.escrowAgreementGrid.load({prodCode: this.selectRowData.prodCode,documentType:this.selectRowData.documentType,t8TruteeInfoId: this.selectRowData.t8TruteeInfoId});
    },
    editDisclosureRule() {
      this.editDisclosureRuleForm = {};
      this.$refs.editDisclosureRulePopup.popup();
    },
    addHandler() {
      this.addDisclosureRuleForm = {};
      this.$refs.addDisclosureRulePopup.popup();
    },
    changedCompany(value) {
      //alert("value=:>>>>"+value);
      if (value === '1') {
        this.isCompanyCheckFlag = '1';
      } else {
        this.isCompanyCheckFlag = '0';
      }
      //console.log(this.isCompanyCheckFlag==='1');
    },
    changedTrutee(value) {
      if (value === '1') {
        return "是";
      } else {
        return "否";
      }
      //console.log(this.isTruteeCheckFlag==='1');
    },
    popupEdit(data) {
      let pathUrl = '/main/pms/basePublish/ProdRegular';
      //console.log("row=:>>>>>>>",data);
      //console.log("this.disclosureNotice.prodBaseDate=:>>>>>>>",this.disclosureNotice.prodBaseDate);
      //console.log("this.disclosureNotice.prodCdde=:>>>>>>>",this.disclosureNotice.prodCdde);
      this.$router.push({
        path: pathUrl,
        query: {
          t8ProdInfoId: data.t8ProdInfoId,
          t8DisclosureNoticeId: data.id,
          prodBaseDate: this.disclosureNotice.prodBaseDate,
          prodCode: data.prodCode
        },
      });

    },
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
    },
    loadData(id) {
      //console.log("t8ProdInfoId=:>>>>",id);
      this.disclosureRule.$AssetInfoGrid.load({disclosureNoticeId: id});
    },
    onCompareFileChange(file) {
      let fileName = file.name
      //console.log("上传文件名=:>>>>>",fileName);
      this.uploadFileName = fileName;
      //let suffix = fileName.substr(fileName.lastIndexOf('.') + 1);
      /*if ('docx' != suffix) {
        Tools.alert("只能对比docx类型的文档!","danger");
        this.$refs.comparePrintTempVersionRef.doReset();
        return false;
      }*/
    },
  }
};
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
</style>
