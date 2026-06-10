<template>
  <div>
    <div>
      <k-form-search-customize data-model-name="DisclosureNotice" data-target="disclosureNoticeGrid"
                               v-model="prodSearchParam">
        <k-form-item label="产品代码">
          <k-field-select v-model="prodSearchParam.prodCode" data-action="T8Dict.findTaProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode"></k-field-select>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="prodSearchParam.prodName" data-validate-type="text"/>
        </k-form-item>
        <k-form-item label="公告发起人">
          <k-field-select v-model="prodSearchParam.crtUserId"
                          data-action="User.getAllUser" data-display-field="username"
                          data-value-field="username"/>
        </k-form-item>

        <k-form-item label="信披类型">
          <k-field-select v-model="prodSearchParam.disclosureType" data-dict="xp_doc_other_type"
                          @data-on-change="changeXpType"></k-field-select>
        </k-form-item>
        <k-form-item label="信披子类型">
          <k-field-select v-model="prodSearchParam.disclosureSonType" :data-data="xpSonTypeList"
                          data-value-field="value" data-display-field="text"></k-field-select>
        </k-form-item>
        <k-form-item label="基准日期">
          <k-field-date v-model="prodSearchParam.prodBaseDate" data-type="date" data-date-format="yyyy-MM-dd"/>
        </k-form-item>
        <k-form-item label="公告来源">
          <k-field-select v-model="prodSearchParam.sourceType" data-dict="source_type"/>
        </k-form-item>
        <k-form-item label="数据状态">
          <k-field-select v-model="prodSearchParam.dataStatus" data-dict="data_status" :data-multiple="true"></k-field-select>
        </k-form-item>
        <k-form-item label="审批状态">
          <k-field-select v-model="prodSearchParam.approvalStatus" data-dict="t8_approval_status"></k-field-select>
        </k-form-item>
        <k-form-item label="发布状态">
          <k-field-select v-model="prodSearchParam.publishStatus" data-dict="t8_release_status"></k-field-select>
        </k-form-item>

        <k-form-item label="计划发布日期">
          <k-field-date v-model="prodSearchParam.planFbDate" data-type="date" data-date-format="yyyy-MM-dd"/>
        </k-form-item>

        <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP"  data-target="noticePublishPopup" :data-handler="checkBatchPublishData"
               style="width: 90px;" v-if="global.isShowAuthorityButton('DisclosureNotice.batchPublishChannel')">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>批量发布
        </k-btn>
<!--        <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" data-target="noticePopup"-->
<!--               style="width: 120px;" v-if="global.isShowAuthorityButton('M8DisclosureManual.batchSendNotice')"-->
<!--               @click="planFbDate">-->
<!--          <md-icon md-src="/static/svg/add.svg" />-->
<!--          公告发布-->
<!--        </k-btn>-->
        <k-btn slot="button" class="btn-custom-primary" data-functype="SUBMIT" ref="batchCancleButton"
               :data-handler="batchCancle" style="width: 120px;"
               v-if="global.isShowAuthorityButton('M8DisclosureManual.batchNoticeCancle')">
          <md-icon md-src="/static/svg/add.svg" />公告取消
        </k-btn>

        <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" @click="openManualNotice" style="width: 120px"
               v-if="global.isShowAuthorityButton('M8DisclosureManual.insertManualInfo')">
          <md-icon md-src="/static/svg/add.svg" />新增手工公告
        </k-btn>
      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="disclosureNoticeGrid" @data-row-select="selectRow"
              data-action="DisclosureNotice.findDisclosureManualNoticesAuth"
              @init="(grid)=>{this.$kgrid = grid}" :data-checkbox="true" data-checkbox-id="id"
              data-tree-id="id" :data-reserve-selection="true"
              data-fixed="right"
              data-operate-width="160px">
        <k-grid-column data-header="id" data-name="id" :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="产品id" data-name="t8ProdInfoId" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode" data-width="100"></k-grid-column>
        <k-grid-column data-align="center" data-header="产品名称" data-name="prodName" data-width="280"></k-grid-column>
        <k-grid-column data-align="center" data-header="公告标题" data-name="noticeTitle" data-width="350"></k-grid-column>
        <k-grid-column data-align="center" data-header="信披类型" data-name="disclosureType" data-dict="xp_doc_type"
                       data-width="100"></k-grid-column>
        <k-grid-column data-align="center" data-header="信披子类型" data-name="disclosureSonType" data-dict="son_type"
                       data-width="100"></k-grid-column>
        <k-grid-column data-align="center" data-header="审批状态" data-name="reviewStatus"
                       data-dict="t8_approval_status"></k-grid-column>
        <k-grid-column data-align="center" data-header="计划发布日期" data-name="planFbDate" data-type="date"
                       data-width="100"></k-grid-column>
        <k-grid-column data-align="center" data-header="发布状态" data-name="disclosureStatus" data-dict="xp_disclosure_notice_status"
                       data-width="130px"></k-grid-column>
        <k-grid-column data-header="信披规则id" data-name="t8DisclosureRuleId" :data-hidden="true"></k-grid-column>

        <!-- <k-grid-column data-align="center" data-header="是否发送托管行" data-name="isSendEmail" data-dict="is_send_email" ></k-grid-column> -->


        <k-grid-column data-align="center" data-header="基准日期" data-name="prodBaseDate" data-type="date" :data-hidden="true"
                       data-width="120"></k-grid-column>

        <k-grid-column data-align="center" data-header="报告发起人" data-name="crtUserName" data-width="100"></k-grid-column>
        <k-grid-column data-header="计划审批日期" data-name="planSpDate" data-type="date" :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="计划补录日期" data-name="planBlDate" data-type="date" :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="实际补录日期" data-name="realBlDate" data-type="date" :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="实际审批日期" data-name="realSpDate" data-type="date" :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="计划发布日期" data-name="planSendDate" data-type="date" data-width="100"
                       :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="当前阶段" data-name="stage" data-dict="t8_current_stage"
                       :data-hidden="true"></k-grid-column>

        <k-grid-column data-header="是否自动发布" data-width="100" data-name="isAutoSend" data-dict="1yes0no"
                       :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="是否立即发布" data-name="isReleaseSend" data-width="100" data-dict="1yes0no"
                       :data-hidden="true"></k-grid-column>

        <k-grid-column data-align="center" data-header="创建日期" data-name="crtDate" data-render="renderDateTimeCreate"
                       data-width="150" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="运营机构" data-name="operatingAgency" data-width="150"
                       :data-hidden="true"></k-grid-column>

        <k-grid-column data-align="center" data-header="复核状态" data-name="reviewStatus"
                       data-dict="t8_approval_check_status"
                       :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="创建时间" data-name="crtTime" data-type="time" :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="创建人" data-name="crtUserId" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="更新日期" data-name="updDate" data-render="renderDateTimeUpd"
                       data-width="150" :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="更新时间" data-name="updTime" :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="更新人" data-name="updUserId" :data-hidden="true"></k-grid-column>


        <k-grid-column data-align="center" data-header="更新人名称" data-name="updUserName"
                       :data-hidden="true"></k-grid-column>
        <template slot="operate" slot-scope="props">

          <k-btn class="btn-custom-text" :data-model="props.row.row" @click="popupEdit(props.row.row)"
                 data-descript="公告查看" data-functype="POPUP" data-size="mini"
                 data-target="editDisclosureNoticePopup">
            <!--            <md-icon>library_books</md-icon>-->
            公告查看
          </k-btn>
          <k-btn class="btn-custom-text" :data-model="props.row.row" @click="deleteNotice(props.row.row)"
                 data-descript="公告删除" data-functype="submit" data-size="mini" :data-disabled="props.row.row.disclosureStatus=='8'"
                 data-target="disclosureNoticeGrid">
            <!--            <md-icon>library_books</md-icon>-->
            公告删除
          </k-btn>
        </template>
      </k-grid>
    </div>

    <k-popup ref="noticePopup" title="发布公告">
      <k-form ref="noticeForm" data-ui="element" dataLabelWidth="130px" dataInputWidth="220px">


        <k-form-item label="期望发布日期" :data-col="2" data-input-width="80px">
          <k-field-date v-model="disclosureNotice.date" data-type="date"
                        data-date-format="yyyy-MM-dd" :dataMinValue="this.currentTimes"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-from="noticeForm" @click="batchPublishChannel">
            <i v-show="!showBtn" class="el-icon-loading"/>
            <span>确定</span>
          </k-btn>

          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>


    <k-popup ref="filePopup" title="上传托管机构附件">
      <k-form ref="fileForm" data-ui="element">
        <k-form-item label="托管机构附件" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="fileUploadRef" :data-multiple="false" :data-limit=1
                          :data-error="onFileSubmitError" :data-change="onCompareFileChange"
                          :data-success="onFileSubmitSuccess"
                          :data-auto-upload="false"
                          data-upload-url="/upload/server/PmsApp/disclosure/uploadApproval.json">
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
    <!-- 批量发布公告   -->
    <k-popup ref="noticePublishPopup" title="批量发布公告" >
      <k-form ref="noticePublishForm" data-ui="element" dataLabelWidth="130px" dataInputWidth="220px" >

        <!--        <k-form-item label="期望发布日期"  :data-col="2" data-input-width="80px" >-->
        <!--          <k-field-date v-model="PublishformData.pub_date" data-type="date" data-date-format="yyyy-MM-dd"/>-->
        <!--        </k-form-item>-->

        <k-form-item label="确定发布吗？"  :data-col="2" data-input-width="80px" >
          <k-field-date v-model="PublishformData.pub_date" data-type="date" data-date-format="yyyy-MM-dd" v-show="false"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-target="prodInfoGrid" ref="submitPublishBtn" data-from="noticePublishForm" :data-model="PublishformData" @click="batchPublishChannel">
            <span v-show="showSubmitBtn">发布</span>
            <i v-show="!showSubmitBtn" class="el-icon-loading"/>
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>

      </k-form>
    </k-popup>
  </div>
</template>

<script>
import Tools from "@/utils/tools";
import moment from "moment"
import httpUtil from "../../../frame/httpUtil";

export default {
  name: "M8DisclosureManualNotice",
  data() {
    return {
      $kgrid: null,
      formData: {},
      showBtn:true,
      PublishformData: {},
      prodSearchParam: {
        prodCode: '',
        prodName: '',
        updUserName: '',
        updDate: '',
        publishStatus: '',
        crtUserId:'',
        dataStatus: '',
        ebaStatus: '',
        approvalStatus: '',
        stage: '',
        disclosureType: '',
        currentStageStatus: '',
        investManageId: '',
        valuationAccountingId: '',
        isSendEmail: '',
        operatingAgency: '',
        planFbDate: '',
        xpSonTypeList: [],
        noticeStatus: '',
        prodBaseDate: '',
        sourceType: '',
        isManualNotice: '',
      },
      spyj: [
        {label: '同意', value: '1'},
        {label: '不同意', value: '0'},
      ],
      selectRowData: {},
      currentUserRoles: '',
      userId: '',
      inGroup: '',
      showSubmitBtn: true,
      t8ProdInfoId: '',
      roleList: [],
      userList: [],
      disclosureNotice: {},
      truteeApproval: {},
      disclosureNoticeChannel: [],
      DisclosureNoticeProcess: {
        t8DisclosureNoticeId: '',
        $RatGrid: null,
      },
      xpSonTypeList: [],
      currentTimes:''
    };
  },
  computed: {
    queryParam() {
      return {
        'prodName': this.prodSearchParam.prodName,
        'prodCode': this.prodSearchParam.prodCode,
        'disclosureType': this.prodSearchParam.disclosureType,
        'disclosureSonType': this.prodSearchParam.disclosureSonType,
        'prodBaseDate': this.prodSearchParam.prodBaseDate,
        'sourceType': this.prodSearchParam.sourceType,
        'dataStatus': this.prodSearchParam.dataStatus,
        'approvalStatus': this.prodSearchParam.approvalStatus,
        'publishStatus': this.prodSearchParam.publishStatus,
        'planFbDate': this.prodSearchParam.planFbDate,
      }
    }
  },
  watch: {
    '$route'(val, from) {
      if (val.query.url == "publishPage" || val.query.url == "otherNoticeDetail") {
        //location.reload();
        this.$refs.disclosureNoticeGrid.load();
      }
    }
  },
  created() {
    //接收路由中的参数
    this.$nextTick(() => {
      if(this.$route.query.dealId){
        this.$refs.disclosureNoticeGrid.load({id: this.$route.query.dealId})
      }
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
    this.currentTimes = new Date().Format("yyyyMMdd");
  },
  methods: {

    deleteNotice(row){

      Tools.confirm(() => {
        this.httpUtil.comnUpdate({
          action: 'DisclosureNotice.updateDisclosureNoticeForDelete',
          params:row,
          successAlert: true,
        }).then(data => {
          this.$refs.disclosureNoticeGrid.load();
        });
      }, '确定删除吗', null, 'danger')
    },

    planFbDate() {
      this.$set(this.disclosureNotice, 'date', '');
      this.$refs.noticePopup.popup();
    },

    renderDateTimeCreate(row) {
      return Tools.formatDateTime(row.crtDate, row.crtTime);
    },
    renderDateTimeUpd(row) {
      return Tools.formatDateTime(row.updDate, row.updTime);
    },

    changeXpType(disclosureType) {
      this.$set(this.prodSearchParam, 'disclosureSonType', '');
      this.httpUtil.comnQuery({
        action: "DisclosureMod.getXPTypeByDocType",
        params: {disclosureType: disclosureType}
      }).then(data => {
        this.xpSonTypeList = data.rows;
      }).catch({})
    },
    batchCancle() {
      Tools.confirm(() => {
        const _this = this
        const list = _this.$kgrid.getSelected();
        let check = this.checkParams();
        if (check && list.length < 1) {
          Tools.alertTime("请输入筛选条件或直接勾选需要处理的数据，再触发批量处理!", "danger", 5000);
          return false;
        }
        this.httpUtil.comnUpdate({
          action: 'DisclosureNotice.otherBatchCancle',
          params: {
            list: JSON.stringify(list),
            'prodName': this.prodSearchParam.prodName,
            'prodCode': this.prodSearchParam.prodCode,
            'disclosureType': this.prodSearchParam.disclosureType,
            'disclosureSonType': this.prodSearchParam.disclosureSonType,
            'prodBaseDate': this.prodSearchParam.prodBaseDate,
            'sourceType': this.prodSearchParam.sourceType,
            'dataStatus': this.prodSearchParam.dataStatus,
            'approvalStatus': this.prodSearchParam.approvalStatus,
            'publishStatus': this.prodSearchParam.publishStatus,
            'planFbDate': this.prodSearchParam.planFbDate,
          },
          successAlert: true,
        }).then(data => {
          this.$refs.disclosureNoticeGrid.load();
          this.$refs.batchCancleButton.setIconStyle(1, [])
        });
      }, '确定取消吗', null, 'danger')

    },
    checkParams: function () {
      let flag = false;
      if ((this.prodSearchParam.prodName == "" || this.prodSearchParam.prodName == null || this.prodSearchParam.prodName == undefined) &&
        (this.prodSearchParam.prodCode == "" || this.prodSearchParam.prodCode == null || this.prodSearchParam.prodCode == undefined) &&
        (this.prodSearchParam.disclosureType == "" || this.prodSearchParam.disclosureType == null || this.prodSearchParam.disclosureType == undefined) &&
        (this.prodSearchParam.prodBaseDate == "" || this.prodSearchParam.prodBaseDate == null || this.prodSearchParam.prodBaseDate == undefined) &&
        (this.prodSearchParam.sourceType == "" || this.prodSearchParam.sourceType == null || this.prodSearchParam.sourceType == undefined) &&
        (this.prodSearchParam.publishStatus == "" || this.prodSearchParam.publishStatus == null || this.prodSearchParam.publishStatus == undefined) &&
        (this.prodSearchParam.approvalStatus == "" || this.prodSearchParam.approvalStatus == null || this.prodSearchParam.approvalStatus == undefined) &&
        (this.prodSearchParam.planFbDate == "" || this.prodSearchParam.planFbDate == null || this.prodSearchParam.planFbDate == undefined)
      ) {
        flag = true;
      }

      return flag
    },

    // batchPublishChannel() {
    //   const _this = this
    //   const list = _this.$kgrid.getSelected();
    //   let check = this.checkParams();
    //   if (check && list.length < 1) {
    //     Tools.alertTime("请输入筛选条件或直接勾选需要处理的数据，再触发批量处理!", "danger", 5000);
    //     this.showBtn = true;
    //     return false;
    //   } else{
    //     this.showBtn = false;
    //   }
    //   this.httpUtil.comnUpdate({
    //     action: 'DisclosureNotice.otherBatchPublishChannel',
    //     params: {
    //       list: JSON.stringify(list),
    //       'prodName': this.prodSearchParam.prodName,
    //       'prodCode': this.prodSearchParam.prodCode,
    //       'disclosureType': this.prodSearchParam.disclosureType,
    //       'disclosureSonType': this.prodSearchParam.disclosureSonType,
    //       'prodBaseDate': this.prodSearchParam.prodBaseDate,
    //       'sourceType': this.prodSearchParam.sourceType,
    //       'dataStatus': this.prodSearchParam.dataStatus,
    //       'approvalStatus': this.prodSearchParam.approvalStatus,
    //       'publishStatus': this.prodSearchParam.publishStatus,
    //       'planFbDate': this.prodSearchParam.planFbDate,
    //       'date': this.disclosureNotice.date,
    //     },
    //     successAlert: true,
    //   }).then(data => {
    //     this.showBtn = true;
    //     this.$refs.noticeForm.reset();
    //     this.$refs.noticePopup.close();
    //     this.$refs.disclosureNoticeGrid.load();
    //
    //   });
    //
    // },

    openManualNotice() {
      this.$router.push({
        //path: '/main/pms/disclosureMsg/M8DisclosureManual01',
        path: '/main/pms/disclosureMsg/M8DisclosureManual',
      });
    },

    findRoleUser(row) {
      this.httpUtil.comnQuery({
        action: "User.getRoleUser",
        params: {roleId: row.roleId},
      }).then(data => {
        if (data.rows.length > 0) {
          this.roleList = data.rows;
          return this.roleList;
          //console.log(this.roleList);
        }
      }).catch({})
    },
    dataEcho() {
      //this.DisclosureNoticeProcess.$RatGrid.load({t8DisclosureNoticeId: this.DisclosureNoticeProcess.t8DisclosureNoticeId});
    },
    versionChangeFnc(val) {
      this.DisclosureNoticeProcess.t8DisclosureNoticeId = val.id;
    },
    popupEdit(row) {

        let pathUrl = '/main/pms/disclosureMsg/M8DisclosureManualDisplay';
        console.log(pathUrl);
        Tools.getLoginUser().then(res => {
          this.currentUserRoles = res.roleids;
          this.userId = res.userid;
          this.$nextTick(() => {
            this.httpUtil.comnQuery({
              action: 'DisclosureNotice.findUserInfo',
              params: {
                t8ProdInfoId: row.t8ProdInfoId,
                userId: this.userId,
                id: row.id,
                disclosureType: row.disclosureType
              },
            }).then(data => {
              if (data.rows != null && data.rows != undefined && data.rows != "") {
                if (data.rows[0].count > 0) {
                  this.inGroup = '1'
                } else {
                  this.inGroup = '0'
                }
              }
              this.$nextTick(() => {
                this.$router.push({
                  path: pathUrl,
                  query: {
                    refreshpage:"refreshpage",
                    isAutoSend: row.isAutoSend,
                    planFbDate: row.planFbDate,
                    id: row.id,
                    stage: row.stage,
                    dataStatus: row.dataStatus,
                    approvalStatus: row.approvalStatus,
                    publishStatus: row.publishStatus,
                    ebaStatus: row.ebaStatus,
                    disclosureNoticeData: JSON.stringify(row),
                    t8ProdInfoId: row.t8ProdInfoId,
                    prodCode: row.prodCode,
                    inGroup: this.inGroup,
                    disclosureType: row.disclosureType,
                    sourceType: row.sourceType
                  },
                });
              });
            });
          });
        })




    },

    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
    },
    onSubmitDocSuccess() {
      this.$refs.uploadRef.doReset();
      this.$refs.addForm.reset();
      this.$refs.addPopup.close();
      this.$refs.disclosureNoticeGrid.load();
    },
    onSubmitError() {
      this.$refs.uploadRef.doReset();
      this.$refs.submitBtn.setIconStyle(1, []);
      this.showSubmitBtn = true;
    },
    changeFlag(val) {
      if (val == 0) {
        this.truteeApproval.truteeApprovalResultDesc = '同意申请,材料齐全。';
      } else {
        this.truteeApproval.truteeApprovalResultDesc = '材料有误,请核对。';
      }
    },
    onCompareFileChange(file) {
      let fileName = file.name

      this.uploadFileName = fileName;

    },
    onFileSubmitError() {
      this.truteeApproval.uploadFileName = '';
      this.$refs.fileUploadRef.doReset();
      this.$refs.fileSubmitBtn.setIconStyle(1, []);
    },
    onFileSubmitSuccess(res) {

      this.$set(this.truteeApproval, "uploadFileName", this.uploadFileName);
      this.$set(this.truteeApproval, "attachmentUrl", res.response.returnmsg);
      this.$refs.fileUploadRef.doReset();
      this.$refs.fileForm.reset();
      this.$refs.filePopup.close();
    },
    fileSubmitUploadParam() {
      let formData = this.truteeApproval;

      this.$refs.fileUploadRef.upload(formData);
    },
    //检查选中数据是否满足可发布状态
    checkBatchPublishData () {
      let pass = true;
      const _this = this;
      const list = _this.$kgrid.getSelected();

      let check = this.checkConditionParams();
      if(list.length === 0){
        Tools.alertTime( "请先勾选信披公告复选框!", "danger",5000);
        return false;
      }

      for(let i=0; i<list.length; i++){//当没有选中时不会进入
        if(list[i].disclosureStatus !== '2'){//仅生成待发布状态可进行发布操作
          pass = false;
        }
      }

      if(!pass){
        Tools.alert("执行批量的公告必须为[生成待发布]或[发布失败]状态!");
        this.$refs.disclosureNoticeGrid.setSelected([]);
        return false;
      }
    },
    // 批量发布信披公告
    batchPublishChannel () {
      const _this = this;
      const list = _this.$kgrid.getSelected();
      for(let i=0; i<list.length; i++){
        // if(list[i].disclosureType == '1' || list[i].disclosureType == '11'){
        //   Tools.alert("售前信息登记和手工报告无需发布,请勿勾选!");
        //   return false;
        // }
        console.log(list[i]);
        list[i].title = list[i].fileName;
        list[i].filePath = list[i].crtPath;
        list[i].disclosureType = "11";
        list[i].t8DisclosureNoticeId = list[i].id;
      }
      console.log("list=:>>",list);
      this.showSubmitBtn = false;
      this.httpUtil.comnUpdate({
        action: 'DisclosureNotice.batchPublishChannel',
        params: {list: JSON.stringify(list),
          'prodName': this.prodSearchParam.prodName,//查询条件
          'prodCode': this.prodSearchParam.prodCode,
          'disclosureType': this.prodSearchParam.disclosureType,
          'disclosureSonType': this.prodSearchParam.disclosureSonType,
          'prodBaseDate': this.prodSearchParam.prodBaseDate,
          'disclosureStatus': this.prodSearchParam.disclosureStatus,
        },
        successAlert: true,
      }).then(data => {
        this.showSubmitBtn = true;
        this.$refs.noticeForm.reset();
        this.$refs.noticePopup.close();
        this.$refs.disclosureNoticeGrid.load(this.queryParam);
        this.$refs.disclosureNoticeGrid.setSelected([]);//刷新复选框
      }).catch(() => {
        this.showSubmitBtn = true;
        this.$refs.noticePublishPopup.close();
        this.$refs.disclosureNoticeGrid.load(this.queryParam);
        this.$refs.disclosureNoticeGrid.setSelected([]);//刷新复选框
      });
    },
    //检查查询条件框是否为空
    checkConditionParams: function(){
      let flag = false;
      if(this.prodSearchParam.prodName!=null && this.prodSearchParam.prodName!=''&&this.prodSearchParam.prodName!=undefined){
        flag=true;
      }
      if(this.prodSearchParam.disclosureType!=null && this.prodSearchParam.disclosureType!=''&&this.prodSearchParam.disclosureType!=undefined){
        flag=true;
      }
      if(this.prodSearchParam.disclosureSonType!=null && this.prodSearchParam.disclosureSonType!=''&&this.prodSearchParam.disclosureSonType!=undefined){
        flag=true;
      }
      if(this.prodSearchParam.prodBaseDate!=null && this.prodSearchParam.prodBaseDate!=''&&this.prodSearchParam.prodBaseDate!=undefined){
        flag=true;
      }
      if(this.prodSearchParam.prodCode!=null && this.prodSearchParam.prodCode!=''&&this.prodSearchParam.prodCode!=undefined){
        flag=true;
      }
      if(this.prodSearchParam.disclosureStatus!=null && this.prodSearchParam.disclosureStatus!=''&&this.prodSearchParam.disclosureStatus!=undefined){
        flag=true;
      }
      return flag;
    },
  }
};
</script>
