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
        <k-form-item label="公告版本id" v-show="false">
          <k-field-text v-model="disclosureNotice.noticeVersionId" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="公告标题" :dataCol="2">
          <k-field-text v-model="disclosureNotice.noticeTitle" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="信披类型" >
          <k-field-select v-model="disclosureNotice.disclosureType" data-dict="xp_doc_type" :data-disabled="true"/>
        </k-form-item>
        <k-form-item v-if="!(disclosureNotice.disclosureType=='5'||disclosureNotice.disclosureType=='6'||disclosureNotice.disclosureType=='1'||disclosureNotice.disclosureType=='9')">

        </k-form-item>
        <k-form-item label="信披子类型" v-if="disclosureNotice.disclosureType=='5'||disclosureNotice.disclosureType=='6'||disclosureNotice.disclosureType=='1'||disclosureNotice.disclosureType=='9'">
          <k-field-select v-model="disclosureNotice.disclosureSonType" data-dict="xp_son_type" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="产品代码" v-if="!(disclosureNotice.disclosureType=='6'||disclosureNotice.disclosureSonType=='0903')">
          <k-field-text v-model="disclosureNotice.prodCode" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="产品名称" v-if="!(disclosureNotice.disclosureType=='6'||disclosureNotice.disclosureSonType=='0903')">
          <k-field-text v-model="disclosureNotice.prodName" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="信披规则id" v-show="false">
          <k-field-text v-model="disclosureNotice.t8DisclosureRuleId" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="信披模板id" v-show="false">
          <k-field-text v-model="disclosureNotice.t8DisclosureModId" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="信披模板版本id" v-show="false">
          <k-field-text v-model="disclosureNotice.t8DisclosureModVersionId" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="模板名称" :dataCol="2">
          <k-field-text v-model="disclosureNotice.modName" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="模板版本号">
          <k-field-text v-model="disclosureNotice.modVersion" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="公告版本号" >
          <k-field-text v-model="disclosureNotice.disclosureVersion" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="模板版本号" v-show="false">
          <k-field-text v-model="disclosureNotice.modVersion" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="基准日期" >
          <k-field-date v-model="disclosureNotice.prodBaseDate"  data-type="date" data-date-format="yyyy-MM-dd" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="公告状态" >
          <k-field-select v-model="disclosureNotice.disclosureStatus" data-dict="xp_disclosure_notice_status" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="复核状态" >
          <k-field-select v-model="disclosureNotice.reviewStatus" data-dict="xp_disclosure_check_status" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="创建日期" >
          <k-field-date v-model="disclosureNotice.crtDate" data-type="date" data-date-format="yyyy-MM-dd" :data-disabled="true"/>
        </k-form-item>

        <k-form-footer data-align="center">

          <k-btn style="width:100px;" class="btn-custom-plain" :data-download-name="disclosureNotice.noticeTitle"
                 data-size="small" :data-disabled="disclosureNotice.disclosureStatus==='1'"
                 @click="previewXPGGTempVersion(disclosureNotice)" :data-model="this.disclosureNotice"
                 v-if="global.isShowAuthorityButton('DisclosureNotice.download')">
            <md-icon md-src="/static/svg/add.svg" />公告预览
          </k-btn>

          <k-btn style="width:100px;" class="btn-custom-primary" :data-model="disclosureNotice" @click="checkNotice(disclosureNotice)"
                 v-if="global.isShowAuthorityButton('DisclosureNotice.checkDisclosureNotice')"
                 :data-disabled="!(disclosureNotice.disclosureStatus === '2' && disclosureNotice.reviewStatus === '0')"><!-- 仅仅生成待发布可以复核 -->
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>复核通过
          </k-btn>

          <k-btn style="width:100px;" class="btn-custom-primary" :data-model="disclosureNotice" :data-disabled="!((disclosureNotice.disclosureStatus === '2' && disclosureNotice.reviewStatus === '1')||disclosureNotice.disclosureStatus === '-1')"
                 @click="sendNotice(disclosureNotice)"
                 v-if="global.isShowAuthorityButton('DisclosureNotice.sendNotice')">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>公告发布
            <i v-show="showSendNoticeBtn" class="el-icon-loading"/>
          </k-btn>

          <k-btn style="width:100px;" class="btn-custom-plain" :data-download-name="disclosureNotice.noticeTitle"
                 data-size="small" :data-disabled="disclosureNotice.disclosureStatus==='1'"
                 @click="downloadXPGGTempVersion(disclosureNotice)" :data-model="this.disclosureNotice"
                 v-if="global.isShowAuthorityButton('DisclosureNotice.download')">
            <md-icon md-src="/static/svg/add.svg" />公告下载
          </k-btn>

          <k-btn style="width:100px;" class="btn-custom-primary" :data-model="disclosureNotice" :data-disabled="!(disclosureNotice.disclosureStatus==='1'||disclosureNotice.disclosureStatus==='2')"
                 v-if="global.isShowAuthorityButton('DisclosureNotice.cancelNotice')"
                 @click="cancelNotice(disclosureNotice)" >
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>公告取消
          </k-btn>

          <k-btn style="width:100px;" class="btn-custom-primary" :data-model="disclosureNotice" :data-disabled="disclosureNotice.disclosureStatus!=='1'"
                 v-if="global.isShowAuthorityButton('DisclosureNotice.RecordInfo')"
                 @click="popupEdit(disclosureNotice)" data-functype="POPUP" data-target="disclosureNoticeGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>数据补录
          </k-btn>

<!--          以下按钮隐藏-->

          <k-btn style="width:100px;" class="btn-custom-primary" :data-model="disclosureNotice" v-show="false"
                 @click="previewXP(disclosureNotice)" data-functype="POPUP" data-target="disclosureNoticeGrid"
                 v-if="global.isShowAuthorityButton('DisclosureNotice.previewXP')">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>公告预览
          </k-btn>

          <k-btn style="width:100px;" class="btn-custom-primary" :data-model="disclosureNotice" v-show="false"
                 @click="popupEdit1(disclosureNotice)" data-functype="POPUP" data-target="xpModWordUpdate"
                 v-if="global.isShowAuthorityButton('DisclosureNotice.updateDisNoticeProcess')">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>补录分发
          </k-btn>

          <k-btn style="width:100px;"  class="btn-custom-plain" :data-download-name="disclosureNotice.ruleDocName" v-show="false"
                 data-descript="模板信息" data-functype="DOWNLOAD" data-size="small"
                 data-url="/download/server/PmsApp/print/saveXPGGTempVersion.json" :data-model="this.disclosureNotice">
            <md-icon md-src="/static/svg/add.svg" />保存文档
          </k-btn>

          <k-btn style="width:120px;" class="btn-custom-primary" :data-model="disclosureNotice" v-show="false"
                 @click="manualGenerateNotice(disclosureNotice)"
                 v-if="global.isShowAuthorityButton('DisclosureWordDate.manualGenerateVersion')">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>手动生成版本
          </k-btn>

        </k-form-footer>
      </k-form>
    </div>

    <div class="form-item prod-panel" style="display: -webkit-box;" id="ModColumnInfo">
      <div class="title">
        <div class="prod-items"></div>
        <k-field-display class="title-desc" value="模板取值信息"></k-field-display>
      </div>

      <k-grid ref="disclosureModColumnGrid" @data-row-select="selectColumnRow" @init="(grid)=>{this.disclosureModColumnCondition.$ModColumnGrid = grid}" :data-operate-column="false"
              data-action="DisclosureModColumn.findSupplementaryRecordForDisclosureInfo" :data-autoload="false" :data-page-size="0">
        <k-grid-column data-header="模板补录信息id" data-name="id" :data-hidden="true" ></k-grid-column>
        <k-grid-column data-header="公告表id" data-name="disclosureNoticeId" :data-hidden="true" ></k-grid-column>
        <k-grid-column data-header="信披模板版本id" data-name="t8DisclosureVersionId" :data-hidden="true" ></k-grid-column>
        <k-grid-column data-header="处理日期" data-name="dataDate" :data-hidden="true" ></k-grid-column>
        <k-grid-column data-header="产品代码" data-name="prodCode" :data-hidden="true" ></k-grid-column>
        <k-grid-column data-header="字段描述" data-name="columnLabel" ></k-grid-column>
        <k-grid-column data-header="字段值" data-name="columnValue" ></k-grid-column>
        <k-grid-column data-header="维护方式" data-name="isSysvalue" data-dict="xp_is_sysvalue" ></k-grid-column>
        <k-grid-column data-header="是否补录" data-name="isAddition" data-dict="xp_disclosure_addition_status" ></k-grid-column>
        <k-grid-column data-header="字段key" data-name="columnKey" :data-hidden="true" ></k-grid-column>
        <k-grid-column data-header="配置角色" data-name="roleids" :data-hidden="true" ></k-grid-column>
        <k-grid-column data-header="数据字典" data-name="dict" :data-hidden="true" ></k-grid-column>
        <k-grid-column data-header="输入类型" data-name="functype" :data-hidden="true" ></k-grid-column>
        <k-grid-column data-header="数据类型" data-name="dataType" :data-hidden="true" ></k-grid-column>
        <k-grid-column data-header="前端组件属性配置" data-name="confoption" :data-hidden="true" ></k-grid-column>
        <k-grid-column data-header="字段来源" data-name="source_type" :data-hidden="true" ></k-grid-column>
        <k-grid-column data-header="是否展示" data-name="isdisplay" data-dict="xp_if_ok" :data-hidden="true" ></k-grid-column>
      </k-grid>
    </div>
    <div class="form-item prod-panel" style="display: -webkit-box;" v-show="prodNavIsShow">
      <div class="title">
        <div class="prod-items"></div>
        <k-field-display class="title-desc" value="产品净值信息"></k-field-display>
      </div>
      <k-grid ref="ProdNavGrid" :data-operate-column="false" :data-autoload="false" :data-page-size="0"
              data-action="DisclosureNoticeChannel.queryProdNav">
        <k-grid-column data-header="产品代码" data-name="prod_code" ></k-grid-column>
        <k-grid-column data-header="产品名称" data-name="prod_name" ></k-grid-column>
        <k-grid-column data-header="理财产品登记编码" data-name="prod_reg_enc"></k-grid-column>
        <k-grid-column data-header="起息日" data-name="found_dt"></k-grid-column>
        <k-grid-column data-header="到期日" data-name="mtu_dt"></k-grid-column>
        <k-grid-column data-header="期限(天)" data-name="prod_day"></k-grid-column>
        <k-grid-column data-header="份额净值" data-name="unit_net_value"></k-grid-column>
        <k-grid-column data-header="份额累计净值" data-name="total_net_value"></k-grid-column>
        <k-grid-column data-header="产品资产净值" data-name="asset_net_value"></k-grid-column>
        <k-grid-column data-header="成立以来年化收益率" data-name="rate_year"></k-grid-column>
      </k-grid>
    </div>
    <div class="form-item prod-panel" style="display: -webkit-box;" id="channelInfo">
      <div class="title">
        <div class="prod-items"></div>
        <k-field-display class="title-desc" value="渠道信息"></k-field-display>
      </div>

<!--      <div class="add-btn-div">-->
<!--        <div class="add-btn" @click="addHandler">+</div>-->
<!--      </div>-->
      <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" @click="addHandler">新增</k-btn>
      <k-grid ref="disclosureRuleGrid" @data-row-select="selectRow" @init="(grid)=>{this.disclosureRule.$AssetInfoGrid = grid}"
              data-action="DisclosureNoticeChannel.findDisclosureNoticeChannelsInfo" :data-autoload="false" data-operate-width="200px">
        <k-grid-column data-header="公告渠道信息表id" data-name="id" :data-hidden="true" ></k-grid-column>
        <k-grid-column data-header="公告表id" data-name="disclosureNoticeId" :data-hidden="true" ></k-grid-column>
        <k-grid-column data-header="渠道表id" data-name="disclosureNoticeChannelId" :data-hidden="true" ></k-grid-column>
        <k-grid-column data-header="渠道名称" data-name="channelName" ></k-grid-column>
        <k-grid-column data-header="主机" data-name="hostIp" :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="端口号" data-name="portCode" :data-hidden="true" ></k-grid-column>
        <k-grid-column data-header="文件路径" data-name="filePath" :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="计划发布日期" data-name="channelPublicDate" data-type="date" data-date-format="yyyy-MM-dd" :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="发布状态" data-name="noticeChannelPublicStatus" data-dict="xp_disclosure_notice_status" ></k-grid-column>
        <k-grid-column data-header="备注" data-name="remark" :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="是否对接" data-name="isDocking" :data-hidden="true" ></k-grid-column>
        <k-grid-column data-header="对接方式" data-name="dockingWay" :data-hidden="true" ></k-grid-column>
        <k-grid-column data-header="协议" data-name="protocol" :data-hidden="true" ></k-grid-column>
        <k-grid-column data-header="用户名" data-name="userName" :data-hidden="true" ></k-grid-column>
        <k-grid-column data-header="密码" data-name="password" :data-hidden="true" ></k-grid-column>
        <k-grid-column data-header="创建日期" data-name="createDate" data-type="date" data-date-format="yyyy-MM-dd" :data-hidden="true" ></k-grid-column>
        <k-grid-column data-header="创建人id" data-name="createUserId" :data-hidden="true" ></k-grid-column>
        <k-grid-column data-header="信披文档版本" data-name="version" :data-hidden="true" ></k-grid-column>
        <k-grid-column data-header="实际发布日期" data-name="updateDate" data-type="date" data-date-format="yyyy-MM-dd"></k-grid-column>
        <k-grid-column data-header="发布时间" data-name="updateTime" data-type="time" data-date-format="HH:mm:ss"></k-grid-column>
        <template slot="operate" slot-scope="scope">
<!--          <k-btn class="btn-custom-text" data-functype="SUBMIT" v-show="true"
                 data-action="DisclosureNoticeChannel.sendChannelsEmails" data-size="mini"
                :data-model="scope.row.row" data-target="disclosureRuleGrid" :data-confirm="true"
                 data-descript="手动发布公告" :data-disabled="!(scope.row.row.noticeChannelPublicStatus==='2' | scope.row.row.noticeChannelPublicStatus==='-1')">发布
          </k-btn>-->
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-type="danger"
                 data-action="DisclosureNoticeChannel.deleteDisclosureNoticeChannel" data-size="mini"
                 :data-model="scope.row.row" data-target="disclosureRuleGrid" :data-confirm="true"
                 data-descript="删除渠道">删除
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
          <k-field-text v-model="addDisclosureRuleForm.disclosureNoticeId" :data-default-value="disclosureNotice.id"/>
        </k-form-item>
        <k-form-item label="发布渠道" v-show="true">
          <k-field-select v-model="addDisclosureRuleForm.disclosureNoticeChannelId"
                          data-action="DisclosureChannel.findDisChannel" :dataAllowblank='false'
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

    <k-popup ref="xpModWordUpdate" title="补录分发" data-width="60%" :data-dialog-drag="true">
      <k-grid :data-checkbox="true" data-checkbox-id="id" ref="updateGrid"
              :data-params="{t8DisclosureNoticeId:this.t8DisclosureNoticeId}"
              data-action="DisclosureNoticeProcess.findDisNoticeProcess" :data-page-size="0" data-height="500px"
              data-operate-column="false"
              @init="(grid)=>{this.DisclosureNoticeProcess.$RatGrid = grid}"
              style="height: 600px; overflow: auto;">
        <k-grid-column data-header="id" data-name="id" data-width="20" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="t8DisclosureNoticeId" data-name="t8DisclosureNoticeId" data-width="20"
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
                            data-action='User.getRoleUser' data-display-field="label" data-value-field="value"
                            :data-multiple="false" :data-allowblank="false" :data-disabled="true"/>
          </template>
        </k-grid-column>
        <k-grid-column data-header="转交用户" data-name="toUserId" data-width="200">
          <template slot-scope="scope">
            <k-field-select v-model="scope.row.row.toUserId" :data-params="{'roleId':scope.row.row.roleId}"
                            data-action='User.getRoleUser' data-display-field="label" data-value-field="value"
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
  </div>
</template>

<script>
import Tools from "@/utils/tools";

export default {
  name: "DisclosureNoticeDetail",
  data() {
    return {
      formFieldList: [],
      ModColumnList: [],
      filInvestFormData: {is_formal:'',non_standard_desc:''},
      truteeApprovalExist: true,
      viewUrl:'',
      onlineEditData:{},
      downloadFlag: true,
      spyj: [
        {label: '同意', value: '1'},
        {label: '不同意', value: '0'},
      ],
      uploadFormData: {},
      formData: {formFieldList: []},
      ModColumnGridData: {ModColumnList: []},
      roleList: [],
      userList: [],
      pubDisabled: true,//是否可发布
      showSubmitBtn: true,
      disclosureNotice: {},
      selectRowData: {},
      selectColumnRowData: {},
      isCompanyCheckFlag: '',
      isTruteeCheckFlag: '',
      DisclosureNoticeProcess: {
        t8DisclosureNoticeId: '',
        $RatGrid: null,
      },
      disclosureRule: {
        $AssetInfoGrid: null,
      },
      disclosureModColumnCondition: {
        $ModColumnGrid: null,
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
      prodCode: '',
      addDocTypeDict: {},
      t8DisclosureNoticeId: '',
      sysparamTrusteeApproval: '',
      prodBaseDate: '',
      fileNameList: [],
      isClick: false,
      isDisRole: 0,//当前登录人是否信息披露岗 0否 1是  控制是否可以点击补录按钮
      isUploadBtnEnable: true,//上传按钮是否可以点击
      isDownloadBtnEnable: true,//是否下载按钮可以点击
      isAdditionBtnEnable: true,//是否补录按钮可以点击
      isDistriSaveBtnDisabled: false,//补录分发保存按钮是否不可点击  默认可以点击
      prodNavIsShow:false,//产品净值信息是否展示
      showSendNoticeBtn:false,//发布公告加载图标是否展示
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
        //console.log("附件地址监听",attachmentUrl)
        if(attachmentUrl==''||attachmentUrl==null){
          this.downloadFlag=true
        }else {
          this.downloadFlag=false
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

  methods: {
    previewXP(notice){
      let onlineUrl = this.httpUtil.onlineUrl;
      this.$refs.onlineEditPopup.popup();
      //this.viewUrl = "http://10.1.20.88:8201/usr/share/nginx/html/files//xpTemp/136/html/temp2/%E5%AE%9A%E6%9C%9F%E6%8A%A5%E5%91%8A%E6%A8%A1%E6%9D%BF-%E5%85%AC%E5%8B%9F%E5%B0%81%E9%97%AD%E5%9B%BA%E6%94%B6.pdf"
      this.httpUtil.comnQuery({
        action: 'DisclosureNotice.previewXP',
        params: {"id":notice.id,
          "prodCode":notice.prodCode,
          "onlineUrl":onlineUrl}
      }).then(data => {
        if (data.rows.length > 0) {
          this.viewUrl = data.rows[0].viewUrl;
          console.log( this.viewUrl);
        }
      });



    },
    changeFlag(val) {
      if (val == 0) {
        this.truteeApproval.truteeApprovalResultDesc = '同意申请,材料齐全.';
      } else {
        this.truteeApproval.truteeApprovalResultDesc = '材料有误,请核对.';
      }
    },
    refreshPageParam() {
      let id = this.$route.query.id;//公告id
      let versionId = this.$route.query.noticeVersionId;//公告版本id
      // Tools.getLoginUser().then(res => {
      //   let roleids = res.roleids;
      //   let roleArr = roleids.split(',');
      //   //判断是否有信息披露岗，如果有，可以点击数据补录按钮
      //   if (roleArr.some(r => (r === '8'))) {
      //     this.isDisRole = '1';
      //   } else {
      //     this.isDisRole = '0';
      //   }
      // });
      this.$nextTick(() => {
        //给全局参数赋值
        this.t8DisclosureNoticeId = id;
        let prodCode = this.$route.query.prodCode;
        this.prodCode = prodCode;
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
                this.$set(this.disclosureNotice, "noticeVersionId", data.rows[0].noticeVersionId);
                this.$set(this.disclosureNotice, "t8DisclosureRuleId", data.rows[0].t8DisclosureRuleId);
                this.$set(this.disclosureNotice, "t8DisclosureModId", data.rows[0].t8DisclosureModId);
                this.$set(this.disclosureNotice, "t8DisclosureModVersionId", data.rows[0].t8DisclosureModVersionId);
                this.$set(this.disclosureNotice, "prodName", data.rows[0].prodName);
                this.$set(this.disclosureNotice, "prodCode", data.rows[0].prodCode);
                this.$set(this.disclosureNotice, "modName", data.rows[0].modName);
                this.$set(this.disclosureNotice, "modVersion", data.rows[0].modVersion);
                this.$set(this.disclosureNotice, "disclosureVersion", data.rows[0].disclosureVersion);
                this.$set(this.disclosureNotice, "noticeTitle", data.rows[0].noticeTitle);
                this.$set(this.disclosureNotice, "disclosureType", data.rows[0].disclosureType);
                this.$set(this.disclosureNotice, "disclosureSonType", data.rows[0].disclosureSonType);
                this.$set(this.disclosureNotice, "prodBaseDate", data.rows[0].prodBaseDate);
                this.$set(this.disclosureNotice, "disclosureStatus", data.rows[0].disclosureStatus);
                this.$set(this.disclosureNotice, "reviewStatus", data.rows[0].reviewStatus);
                this.$set(this.disclosureNotice, "crtDate", data.rows[0].crtDate);
                this.$set(this.disclosureNotice, "suffix", data.rows[0].suffix);
                this.loadData(this.t8DisclosureNoticeId, data.rows[0].noticeVersionId,this.disclosureNotice,data.rows[0].modName);//加载初始页面
                this.httpUtil.comnQuery({
                  action: "DisclosureMod.getXPTypeByDocType",
                  params: {disclosureType: this.disclosureNotice.disclosureType}
                }).then(data => {
                  this.addDocTypeDict = data.rows;
                }).catch({});
              }
            })
          });
        };
      });
      this.httpUtil.comnQuery({
        action: "User.getAllUser",
        params: null
      }).then(data => {
        if (data.rows.length > 0) {
          this.userList = data.rows;
        }
      }).catch({});
      this.httpUtil.comnQuery({
        action: "Role.findAll",
        params: null
      }).then(data => {
        if (data.rows.length > 0) {
          this.roleList = data.rows;
        }
      }).catch({})
    },
    trusteeshipSuccess() {
      //console.log("发送托管行成功。。。")
      this.sendTrusteeship = true
      this.refreshPageParam();
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
      // this.$refs.disclosureRuleGrid.load();
    },
    submitFlowParam(row) {
      //复核信披公告信息
      this.httpUtil.comnQuery({
        action: 'DisclosureNotice.checkDisclosureNotice',
        params: row
      }).then(data => {
        /*进行判断*/
        if (data && data.rows.length > 0 && data.rows[0].currentStageStatus === '11') {
          /*2.发起审批*/
          this.httpUtil.comnUpdate({
            action: "DisclosureNotice.DisclosureNoticeFlow",
            params: row,
            mask: true,
            successAlert: true,
          }).then(data => {

          })
        } else {
          Tools.alert("当前公告不能发起审批!!");
        }
      });
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

    //预览定期报告
    previewXPGGTempVersion(rows) {
      let fileName = rows.noticeTitle + '.pdf';

      this.httpUtil.comnQuery({
        action: "DisclosureNoticeProcess.findMaxVersionById",
        params: {t8DisclosureNoticeId: this.t8DisclosureNoticeId}
      }).then(data => {
        rows.fileName = data.returndata.fileName;
        rows.filePath = data.returndata.filePath;
        rows.docType = data.returndata.docType;
        rows.version = data.returndata.maxVersion;
        rows.maxVersionId = data.returndata.maxVersionId;
        this.httpUtil.preview({
          url: "/download/server/PmsApp/print/previewXPGGByNoticeVersion.json",
          params: rows
        }, fileName);
      }).catch({})
    },

    //下载定期报告
    downloadXPGGTempVersion(rows) {
      let fileName = rows.noticeTitle+rows.suffix;

      this.httpUtil.comnQuery({
        action: "DisclosureNoticeProcess.findMaxVersionById",
        params: {t8DisclosureNoticeId: this.t8DisclosureNoticeId}
      }).then(data => {
        rows.fileName = data.returndata.fileName;
        rows.filePath = data.returndata.filePath;
        rows.docType = data.returndata.docType;
        rows.version = data.returndata.maxVersion;
        rows.maxVersionId = data.returndata.maxVersionId;
        this.httpUtil.download({
          url: "/download/server/PmsApp/print/downloadXPGGByNoticeVersion.json",
          params: rows,
          callback: response => {
            Tools.alert("下载完成");
          }
        }, fileName);
      }).catch({})
    },

    /*修改公告状态,使得不用刷新页面就可以控制按钮*/
    editDisclosureStatus(stage,currentStageStatus) {
      this.$set(this.disclosureNotice, "stage", stage);
      this.$set(this.disclosureNotice, "currentStageStatus", currentStageStatus);
    },
    beforeSubmit(value) {
      value.prodCode = this.disclosureNotice.prodCode;
      value.prodName = this.disclosureNotice.prodName;
      value.disclosureType = this.disclosureNotice.disclosureType;
      value.disclosureSonType = this.disclosureNotice.disclosureSonType;
      value.id=this.truteeApproval.id;

      this.$set(this.truteeApproval, "recheck", '0')
      value.recheck = '0';
      this.httpUtil.comnUpdate({
        action: "DisclosureTruteeApproval.updateDisclosureNoticeApproval2",
        params: value,
        mask: true
      }).then(data => {
        this.$refs.trusteeBtn.setIconStyle(1, []);
        //查询公告状态,用于控制按钮是否可点击
        this.httpUtil.comnQuery({
          action: 'DisclosureNotice.findDisclosureNoticeStatus',
          params: this.disclosureNotice
        }).then(data => {
          this.disclosureNotice.currentStageStatus = data.rows[0].currentStageStatus;
          this.disclosureNotice.stage = data.rows[0].stage;
        })
      });
    },
    cancelNotice(row) {
      let disclosureNotice = this.disclosureNotice;

      this.httpUtil.comnUpdate({
        action: "DisclosureNotice.cancelNotice",
        params: {"id": disclosureNotice.id,"noticeVersionId": disclosureNotice.noticeVersionId,
          "prodCode": disclosureNotice.prodCode, "disclosureStatus":disclosureNotice.disclosureStatus},
      }).then(data => {
        this.showSubmitBtn = true;
        this.refreshPageParam();
      })

    },

    manualGenerateNotice(row) {
      this.httpUtil.comnQuery({
        action: 'DisclosureWordDate.manualGenerateVersion',
        params:{'t8DisclosureNoticeId':row.id}
      }).then(data => {
        if (data.success) {
          Tools.alert(data.returnmsg, 'success');
        } else {
          Tools.alert(data.returnmsg, 'danger');
        }
      })
    },
    sendNotice(row) {
      let disclosureNotice = this.disclosureNotice;
      this.showSendNoticeBtn = true
      this.httpUtil.comnUpdate({
        action: "DisclosureNotice.sendNotice",
        params:  {"id":disclosureNotice.id,"noticeTitle":disclosureNotice.noticeTitle},
      }).then(data => {
        this.showSubmitBtn = true;
        this.showSendNoticeBtn = false;
        this.refreshPageParam();
      })


    },
    saveModColumns() {
      //获取表格中被选中的数据，如果没有选则的数据，提示，否则验证是否需要转交的用户有填写，没有填写进行提示
      let list = this.$refs.updateGrid.getSelected();
      if (list.length == 0) {
        Tools.alert("未选择任何需要补录分发的数据！", "danger");
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
            action: "DisclosureNoticeProcess.updateDisNoticeProcess",
            params: {
              t8ProdInfoId: this.t8ProdInfoId,
              jsonData: JSON.stringify(list)
            },
            mask: true
          }).then(data => {
            this.showSubmitBtn = true
            if (data.success) {
              this.$refs.xpModWordUpdate.close();
            }
          });
        }
      }
    },
    popupEdit1(row) {
      this.httpUtil.comnUpdate({
        action: "DisclosureNotice.findOperatorForDivided",
        params:  {"id":row.id},
        mask: false,
        successAlert: false,
      }).then(data => {

        if("failed"===data.returndata.result){
          this.isClick = false;
        }else{
          this.isClick = true;
        }
      })
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
      //console.log("文件上传成功：", res.response.returnmsg)
      //console.log("success,fileName=:>>>",this.uploadFileName);
      this.$set(this.truteeApproval, "uploadFileName", this.uploadFileName);
      this.$set(this.truteeApproval, "attachmentUrl", res.response.returnmsg);
      this.truteeApproval.attachmentUrl=res.response.returnmsg
      //console.log("上传成功后",this.truteeApproval.attachmentUrl)
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
        //console.log("是")
      } else {
        return "否";
      }
    },
    /**数据补录跳转*/
    popupEdit(data) {
      let pathUrl = '/main/pms/basePublish/ProdRegular';
      this.$router.push({
        path: pathUrl,
        query: {
          id: this.$route.query.id,
          t8DisclosureNoticeId: data.id,
          t8DisclosureRuleId: data.t8DisclosureRuleId,
          t8DisclosureModId: data.t8DisclosureModId,
          t8DisclosureModVersionId: data.t8DisclosureModVersionId,
          t8DisclosureVersionId: data.noticeVersionId,
          prodName: data.prodName,
          prodCode: data.prodCode,
          modName: data.modName,
          modVersion: data.modVersion,
          disclosureVersion: data.disclosureVersion,
          noticeTitle: data.noticeTitle,
          disclosureType: data.disclosureType,
          disclosureSonType: data.disclosureSonType,
          disclosureStatus: data.disclosureStatus,
          reviewStatus: data.reviewStatus,
          prodBaseDate: data.prodBaseDate,
          crtDate: data.crtDate,
        },
      });
    },
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
    },
    selectColumnRow(row, column, event) {
      this.selectColumnRowData = Object.assign({}, row)
      this.ModColumnGridData = Object.assign({}, row)
    },
    loadData(id, notice_version_id,disclosureNotice,modName) {
      this.prodNavIsShow = false;
      this.disclosureRule.$AssetInfoGrid.load({disclosureNoticeId: id, noticeVersionId: notice_version_id});
      this.disclosureModColumnCondition.$ModColumnGrid.load({dataType: "0", t8DisclosureNoticeId: id, noticeVersionId: notice_version_id});
      if(disclosureNotice.disclosureType=='9'&&disclosureNotice.disclosureSonType=='0903'){
        this.prodNavIsShow = true;
        this.$refs.ProdNavGrid.load({noticeVersionId: notice_version_id,modName:modName});
      }
    },
    onCompareFileChange(file) {
      let fileName = file.name
      //console.log("上传文件名=:>>>>>",fileName);
      this.uploadFileName = fileName;
    },
    checkNotice(notice) {
      let disclosureNotice = this.disclosureNotice;

      this.httpUtil.comnUpdate({
        action: "DisclosureNotice.checkDisclosureNotice",
        params: {"id": disclosureNotice.id},
      }).then(data => {
        this.refreshPageParam();
      })
    }
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
    this.refreshPageParam();
  },
  created() {/**立即执行的方法*/
  this.refreshPageParam();
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


