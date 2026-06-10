<template>
  <div class="md-card k-card md-theme-default parent-div" style="height: 1200px;">
    <div class="form-item prod-panel" style="margin-top:50px;margin-left:80px;display: -webkit-box;">
      <k-form ref="addForm" :data-col="3" data-input-width="150px">
        <k-form-item label="公告Id" v-if="false">
          <k-field-text v-model="formData.t8DisclosureNoticeId" :data-max-length="100" :dataAllowblank="false"/>
        </k-form-item>
        <k-form-item label="公告标题" :data-col="3">
          <k-field-text v-model="formData.title" :data-max-length="100" :dataAllowblank="false"/>
        </k-form-item>
<!--        <k-form-item label="信披类型">
          <k-field-select v-model="formData.disclosureType" @data-on-change="changeDisclosureType" data-dict="xp_doc_type" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="信披子类型">
          <k-field-select v-model="formData.disclosureSonType" data-dict="xp_son_type" @data-on-change="changeDisclosureSonType"/>
        </k-form-item>-->
        <k-form-item label="计划发布日期">
          <k-field-date v-model="formData.planFbDate" data-type="date" style="width: 100%;"
                        :dataAllowblank="false"/>
        </k-form-item>
<!--        <k-form-item label="是否需要审批">
          <k-field-select v-model="formData.isNeedExamine" data-dict="1yes0no" :data-allowblank="false"/>
        </k-form-item>-->

<!--        <k-form-item label="关联产品">

          <k-field-select v-model="formData.isConnectProd" data-dict="1yes0no"
                          :dataAllowblank="true" data-default-value="1" @data-on-change="connectProd"/>
        </k-form-item>-->
        <k-form-item label="产品代码"  :data-col="2">

          <k-field-select v-model="formData.prodCode" data-action="T8Dict.findDpbProdInfos" style="width: 100%;"
                          data-display-field="prodCode,prodName" data-value-field="prodCode" :dataAllowblank="true" :data-multiple = "false"
                          @data-on-change="changeProd"/>
        </k-form-item>
        <k-form-item label="系列代码" :data-col="3" v-show="false">
          <k-field-text v-model="formData.prodSerCd"/>
        </k-form-item>
        <!--该字段仅针对审批流使用-->
        <k-form-item label="产品名称" v-show="false" :data-col="1">

          <k-field-text v-model="formData.prodName1"  />
        </k-form-item>

<!--        <k-form-item label="信披公告说明" :data-col="3">
          <k-field-text v-model="formData.note" input-type="textarea" :data-max-length="500"/>
        </k-form-item>-->
        <k-form-item label="公告文件" :data-col="3">
          <!--          <div slot="label" style="font-size:20px">-->
          <!--            公告文件-->
          <!--          </div>-->
          <div style="width:100%;"><span style="color: #ed3333;font-size: 13px;font-weight: bold">注释：上传的公告文件作为公告主文件进行披露</span></div>
          <div>
            <k-btn style="width: 120px" data-functype="POPUP" class="btn-custom-primary"
                   data-target="addNoticePopup" :data-disabled="noticeFiles.rows.length > 0">
              <md-icon>cloud_upload</md-icon>
              上传文件
            </k-btn>
            <k-grid ref="fileGrid1" @data-row-select="selectRow" :dataData="noticeFiles" data-operate-width="120px">
              <k-grid-column data-header="附件名称" data-name="fileName" data-width="450px"></k-grid-column>
              <k-grid-column data-header="文件类型" data-name="fileType" data-dict="manual_type"></k-grid-column>
              <k-grid-column data-header="路径" data-name="filePath" :data-hidden="true"></k-grid-column>
              <template slot="operate" slot-scope="props">
                <k-btn class="md-info md-just-icon md-simple" :data-download-name="props.row.row.fileName"
                       data-functype="DOWNLOAD" data-size="small"
                       data-url="/download/server/PmsApp/M8DisclosureManual/download.json" v-model="props.row.row">
                  <font color="#00bcd4" style="font-size: 12px;">下载</font>
                </k-btn>
                <k-btn class="md-info md-just-icon md-simple" data-size="small"
                       @click="previewXP(props.row.row)" data-functype="POPUP">
                  <font color="#00bcd4" style="font-size: 12px;">预览</font>
                </k-btn>
                <k-btn class="md-info md-just-icon md-simple"  data-size="small" @click="delNoticeFile(props.row.row)">
                  <font color="#00bcd4" style="font-size: 12px;">删除</font>
                </k-btn>
              </template>
            </k-grid>
          </div>
        </k-form-item>
<!--        <k-form-item label="信披附件" :data-col="3">
          &lt;!&ndash;          <div slot="label" style="font-size:20px">&ndash;&gt;
          &lt;!&ndash;            信披附件&ndash;&gt;
          &lt;!&ndash;          </div>&ndash;&gt;
          <div style="width:100%;"><span style="color: #ed3333;font-size: 13px;font-weight: bold">注释：上传的信披附件作为公告主文件的附件说明文件，随公告主文件披露；其他无需披露的无关事项或过程文件请勿在此处上传，若需要可在审批流中上传！！！</span></div>
          <div>
            <k-btn slot="button" style="width: 120px" data-functype="POPUP" class="btn-custom-primary"
                   data-target="addOtherPopup">
              <md-icon>cloud_upload</md-icon>
              上传文件
            </k-btn>
            <k-grid ref="fileGrid2" @data-row-select="selectRow" :dataData="otherFiles" data-operate-width="120px">
              <k-grid-column data-header="附件名称" data-name="fileName" data-width="450px"></k-grid-column>
              <k-grid-column data-header="文件类型" data-name="fileType" data-dict="manual_type"></k-grid-column>
              <k-grid-column data-header="路径" data-name="filePath" :data-hidden="true"></k-grid-column>
              <template slot="operate" slot-scope="props">

                <k-btn class="md-info md-just-icon md-simple" :data-download-name="props.row.row.fileName"
                       data-functype="DOWNLOAD" data-size="small"
                       data-url="/download/server/PmsApp/M8DisclosureManual/download.json" v-model="props.row.row">
                  <font color="#00bcd4" style="font-size: 12px;">下载</font>
                </k-btn>
                <k-btn class="md-info md-just-icon md-simple" data-size="small"
                       @click="previewXP(props.row.row)" data-functype="POPUP">
                  <font color="#00bcd4" style="font-size: 12px;">预览</font>
                </k-btn>
                <k-btn class="md-info md-just-icon md-simple" data-size="small" @click="delOherFile(props.row.row)">
                  <font color="#00bcd4" style="font-size: 12px;">删除</font>
                </k-btn>
              </template>
            </k-grid>
          </div>
        </k-form-item>-->

        <k-form-item label="发布渠道" :data-col="3">
          <!--          <div slot="label" style="font-size:20px">-->
          <!--            信披渠道-->
          <!--          </div>-->
          <!--          <k-field-checkbox v-model="formData.channelCode" data-action="T8DisChannelInfo.findT8DisChannelInfoAll" :data-allowblank="false"-->
          <!--                            data-display-field="channelName" data-value-field="channelCode"></k-field-checkbox>-->
<!--          <div class="form-item" id="publicFile">
            <ManualChannelInfo :disclosureNoticeData="this.disclosureNoticeData" ref="channelInfo"/>
          </div>-->
<!--          <div class="form-item prod-panel" style="display: -webkit-box;" id="channelInfo">
            <div class="title">
              <div class="prod-items"></div>
              <k-field-display class="title-desc" value="渠道信息"></k-field-display>
            </div>-->

            <!--      <div class="add-btn-div">-->
            <!--        <div class="add-btn" @click="addHandler">+</div>-->
            <!--      </div>-->
<!--            <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" @click="addHandler">新增</k-btn>-->
          <div style="width:100%;"><span style="color: #ed3333;font-size: 13px;font-weight: bold">注释：添加公告需要发布的渠道</span></div>
          <k-btn style="width: 120px" data-functype="POPUP" class="btn-custom-primary"
                 @click="addHandler">
            <md-icon>cloud_upload</md-icon>
            添加渠道
          </k-btn>
          <div id="channelInfo">
            <k-grid ref="disclosureRuleGrid" @data-row-select="selectRow" :dataData="noticeChannelList" data-operate-width="200px" >
              <k-grid-column data-header="公告渠道信息表id" data-name="id" :data-hidden="true" ></k-grid-column>
              <k-grid-column data-header="公告表id" data-name="disclosureNoticeId" :data-hidden="true" ></k-grid-column>
              <k-grid-column data-header="渠道表id" data-name="disclosureNoticeChannelId" :data-hidden="true" ></k-grid-column>
              <k-grid-column data-header="渠道名称" data-name="channelName" ></k-grid-column>
              <k-grid-column data-header="主机" data-name="hostIp" :data-hidden="true"></k-grid-column>
              <k-grid-column data-header="端口号" data-name="portCode" :data-hidden="true" ></k-grid-column>
              <k-grid-column data-header="文件路径" data-name="filePath" :data-hidden="true"></k-grid-column>
              <k-grid-column data-header="计划发布日期" data-name="channelPublicDate" data-type="date" data-date-format="yyyy-MM-dd" ></k-grid-column>
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
              <template slot="operate" slot-scope="props">
<!--                <k-btn class="btn-custom-plain" data-functype="SUBMIT" v-show="true"
                       data-action="DisclosureNoticeChannel.sendChannelsEmails" data-size="mini"
                       :data-model="props.row.row" data-target="disclosureRuleGrid" :data-confirm="true"
                       data-descript="手动发布公告" :data-disabled="!(props.row.row.noticeChannelPublicStatus==='2' | props.row.row.noticeChannelPublicStatus==='-1')">发布
                </k-btn>-->
                <k-btn class="md-danger" data-functype="SUBMIT" data-type="danger"
                       data-size="mini" @click="deleteChannelInfo(props.row.row)"
                       :data-model="props.row.row" data-target="disclosureRuleGrid"
                       data-descript="删除渠道">删除
                </k-btn>
              </template>
            </k-grid>
<!--          <k-grid ref="disclosureRuleGrid" @data-row-select="selectRow" @init="(grid)=>{this.disclosureRule.$AssetInfoGrid = grid}"
                  data-action="DisclosureNoticeChannel.findDisclosureNoticeChannelsInfo" :data-autoload="false" data-operate-width="200px">
            <k-grid-column data-header="公告渠道信息表id" data-name="id" :data-hidden="true" ></k-grid-column>
            <k-grid-column data-header="公告表id" data-name="disclosureNoticeId" :data-hidden="true" ></k-grid-column>
            <k-grid-column data-header="渠道表id" data-name="disclosureNoticeChannelId" :data-hidden="true" ></k-grid-column>
            <k-grid-column data-header="渠道名称" data-name="channelName" ></k-grid-column>
            <k-grid-column data-header="主机" data-name="hostIp" :data-hidden="true"></k-grid-column>
            <k-grid-column data-header="端口号" data-name="portCode" :data-hidden="true" ></k-grid-column>
            <k-grid-column data-header="文件路径" data-name="filePath" :data-hidden="true"></k-grid-column>
            <k-grid-column data-header="计划发布日期" data-name="channelPublicDate" data-type="date" data-date-format="yyyy-MM-dd" ></k-grid-column>
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
              <k-btn class="btn-custom-plain" data-functype="SUBMIT" v-show="true"
                     data-action="DisclosureNoticeChannel.sendChannelsEmails" data-size="mini"
                     :data-model="scope.row.row" data-target="disclosureRuleGrid" :data-confirm="true"
                     data-descript="手动发布公告" :data-disabled="!(scope.row.row.noticeChannelPublicStatus==='2' | scope.row.row.noticeChannelPublicStatus==='-1')">发布
              </k-btn>
              <k-btn class="md-danger" data-functype="SUBMIT" data-type="danger"
                     data-action="DisclosureNoticeChannel.deleteDisclosureNoticeChannel" data-size="mini"
                     :data-model="scope.row.row" data-target="disclosureRuleGrid" :data-confirm="true"
                     data-descript="删除渠道">删除
              </k-btn>
            </template>
          </k-grid>-->
<!--          </div>-->
          </div>
        </k-form-item>

        <k-form-footer data-align="center">
          <!--          <k-btn class="btn-custom-primary"-->
          <!--                 :data-after-success="autoFlowProcess"-->
          <!--                 :data-handler="saveRule"-->
          <!--                 data-from="addForm1" :data-model="formData"-->
          <!--                 data-target="t8ObjectGrid">-->
          <!--            <md-icon md-src="/static/svg/confirm.svg"></md-icon>提交-->
          <!--          </k-btn>-->
          <k-btn
            class="btn-custom-primary"
            @click="saveSubmit"
          >
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>提交
          </k-btn>

          <k-btn class="btn-custom-plain" data-functype="CLOSE" @click="closePage">取消</k-btn>
        </k-form-footer>
      </k-form>
      <k-popup ref="onlineEditPopup" data-width="60%"  >
        <div class="edit">
          <div class="word">
            <iframe name="onlineEdit" id="onlineEdit" :src="viewUrl"></iframe>
          </div>
        </div>
      </k-popup>
      <!--公告文件上传弹框-->
      <k-popup ref="addNoticePopup" title="上传附件">
        <k-form ref="addForm2" data-ui="element">
          <k-form-item label="文件类型" :data-col="2">

            <k-field-select v-model="formData.fileType" data-dict="manual_type" style="width: 60%;"
                            :data-disabled="true" data-default-value="0"/>
          </k-form-item>

          <k-form-item label="附件" data-ui="element" data-input-width="500px">
            <k-field-upload data-type="file" ref="uploadRef1" :data-multiple="false" :data-limit=1
                            :data-error="onSubmitError1" :data-success="onSubmitSuccess1"  data-accept=".pdf,.doc,.docx"
                            :data-exceed="onSubmitExceed" :data-auto-upload="false"
                            data-upload-url="/upload/server/PmsApp/M8DisclosureManual/fileUpload.json">

            </k-field-upload>
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary"  data-from="addForm" @click="submitNotice1">
              <i v-show="!showSubmitBtn1" class="el-icon-loading"/>
              <i v-show="showSubmitBtn1" class="icon-confirm"/>确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
          </k-form-footer>
        </k-form>
      </k-popup>

      <k-popup ref="addDisclosureRulePopup" data-title="添加渠道">
        <k-form ref="addDisclosureRuleForm" :data-col="2">
          <k-form-item label="id" v-show="false" :data-col="2">
            <k-field-text v-model="addDisclosureRuleForm.disclosureNoticeId" :data-default-value="formData.t8DisclosureNoticeId"/>
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
              <!--        data-action="DisclosureNoticeChannel.addManualNoticeChannel"      -->
              <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-from="addDisclosureRuleForm"
                     :data-model="addDisclosureRuleForm"
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

      <!--信披文件上传弹框-->
      <k-popup ref="addOtherPopup" title="上传附件">
        <k-form ref="addForm3" data-ui="element">
          <k-form-item label="文件类型" :data-col="2">

            <k-field-select v-model="formData.fileType" data-dict="manual_type" style="width: 60%;"
                            :dataAllowblank="false" data-default-value="1" :data-disabled="true"/>
          </k-form-item>

          <k-form-item label="附件" data-ui="element" data-input-width="500px">
            <k-field-upload data-type="file" ref="uploadRef2" :data-multiple="true" :data-limit=10
                            :data-error="onSubmitError2" :data-success="onSubmitSuccess2"
                            :data-auto-upload="false"
                            data-upload-url="/upload/server/PmsApp/M8DisclosureManual/otherFileUpload.json">

            </k-field-upload>
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary"  data-from="addForm" @click="submitNotice2">
              <i v-show="!showSubmitBtn1" class="el-icon-loading"/>
              <i v-show="showSubmitBtn1" class="icon-confirm"/>确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
          </k-form-footer>
        </k-form>
      </k-popup>
      <!--      <k-popup ref="addPopup" data-title="提交审批任务" data-width="60%" >-->
      <!--        <flowProcessConfigurationJump ref="flowProcessConfigurationJump" :fromData="formList" @submitClose="popupClose" />-->
      <!--      </k-popup>-->
    </div>
  </div>
</template>

<script>
import {assign} from "lodash";
import Tools from "@/utils/tools";
// import flowProcessConfigurationJump from "@/pages/flow/flowProcessConfigurationJump";
import ManualChannelInfo from './components/ManualChannelInfo.vue'

export default {
  name: "M8DisclosureManual",
  components: {ManualChannelInfo},
  data() {
    return {
      showSubmitBtn1:true,
      viewUrl:'',
      formList:{},
      dataList:{},
      channelInfo:{},
      noticeChannelList:{rows: [],},
      options: [
      ],
      prodSearchParam: {
        prodCode: '',
      },
      sonType: {},//子类型
      formData: {
        prodCode: '',
        prodName: '',
        prodName1: '',
        feeJson: '',
        crtUser: '',
        startEstablishDate: '',
        sendEmail: '',
        note: '',
        fileList:'',
        t8DisclosureNoticeId:''
      },
      disclosureRule: {
        $AssetInfoGrid: null,
      },
      addDisclosureRuleForm: {},
      editDisclosureRuleForm: {},
      modelData: [
        {text: '1', value: "待提供数据"},
      ],
      fileData: {},
      noticeFiles: {
        rows: [],
      },
      otherFiles: {
        rows: [],
      },
      submitFiles:[],
      disclosureNoticeData: {
        id:'',
        prodCode:'',
        disclosureType:'',
        disclosureSonType:'',
      },
      dataList2: {
        type: Array,
      },
      filePublic: null,
      fileOther: null,
      hasConfirmDialog: false
    }
  },
  watch: {
    //监听信披类型
    'formData.disclosureType'(value) {
      this.disclosureTypeChange(value);
    }
  },
  created() {

    Tools.getLoginUser().then(res => {
      this.formData.crtUser = res.username;
      let date = Tools.getCurrentTime();
      console.log("date>>>",date);
      this.$set(this.formData,'crtDate',date)
    })

    this.$set(this.formData,'isAutoSend','0');//暂不支持自动发布
  },
  methods: {

    validateData(){
      return true;
    },

    closePage() {
      Tools.closeCurrentWindow(this);
      this.$router.push({
        path: '/main/pms/disclosureNotice/M8DisclosureManualNotice',
        query: {},
      });
    },
    delNoticeFile(val) {

      for (var i = 0; i < this.noticeFiles.rows.length; i++) {
        if (this.noticeFiles.rows[i].fileName == val.fileName) {
          this.noticeFiles.rows.splice(i, 1);
        }

      }

    },


    delOherFile(val) {

      for (var i = 0; i < this.otherFiles.rows.length; i++) {
        if (this.otherFiles.rows[i].fileName == val.fileName) {
          this.otherFiles.rows.splice(i, 1);
        }

      }

    },


    disclosureTypeChange(value) {
      if (value == '5' || value == '6' || value == '7') {
        this.$set(this.formData, 'disclosureSonType', '');
        this.noticeTitles = {};
        this.httpUtil.comnQuery({
          action: "DisclosureMod.getXPTypeByDocType",
          params: {disclosureType: this.formData.disclosureType}
        }).then(data => {
          this.sonType = data.rows;
        }).catch({})
      } else{
        this.$set(this.formData, 'disclosureSonType', '');
      }
    },
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.fileData = Object.assign({}, row)
    },
    submitNotice1() {
      this.showSubmitBtn1 = false;
      let uploadDatas = this.uploadData;
      this.showSubmitBtn = false;
      this.fileData = new FormData();
      console.log(this.formData, 'dd')
      this.$refs.uploadRef1.upload(this.formData);
      this.showSubmitBtn1 = true;
    },
    submitNotice2() {
      this.showSubmitBtn1 = false;
      let uploadDatas = this.uploadData;
      this.showSubmitBtn = false;
      this.fileData = new FormData();
      console.log(this.formData, 'dd')
      this.$refs.uploadRef2.upload(this.formData);
      this.showSubmitBtn1 = true;
    },
    beforeUpload() {
      return false;
    },
    onSubmitError1(err) {
      this.$refs.uploadRef1.doReset();
      this.$refs.submitBtn.setIconStyle(1, [])
    },
    onSubmitError2(err) {
      this.$refs.uploadRef2.doReset();
      this.$refs.submitBtn.setIconStyle(1, [])
    },
    onSubmitExceed() {
      Tools.alert("最多只能上传1份公告文件", "danger");
    },
    onSubmitSuccess1(value) {
      this.$refs.uploadRef1.doReset();

      this.$refs.addNoticePopup.close();
      this.$refs.addForm2.reset();
      this.noticeFiles.rows.push(value.response.returndata);

      this.$refs.fileGrid1.load();

    },

    onSubmitSuccess2(value) {
      this.$refs.uploadRef2.doReset();

      this.$refs.addOtherPopup.close();
      this.$refs.addForm3.reset();
      this.otherFiles.rows.push(value.response.returndata);

      this.$refs.fileGrid2.load();

    },
    deleteEvent(index) {
      if (this.exclusiveList.length > 1) {
        this.exclusiveList.splice(index, 1)
      }
    },
    //校验字段是否为空
    checkIsNull(val) {
      if (val == '' || val == null || val == undefined)
        return true;
      return false;
    },
    saveSubmit() {
      if(this.noticeFiles.rows.length == 0){
        this.noticeFiles.rows.splice(0, 1);
        this.$refs.fileGrid1.load();
        Tools.alert("公告文件列表数据为空，请重新上传文件 ", "danger");
        return;
      }else if(this.noticeFiles.rows[0].filePath == ""){
        this.noticeFiles.rows.splice(0, 1);
        this.$refs.fileGrid1.load();
        Tools.alert("公告文件路径为空，请重新上传文件", "danger");
        return;
      }

      var flag = this.$refs.addForm.validate();
      if (flag == false) {
        return false;
      }
      let noticefilename=this.noticeFiles.rows[0].fileName;

      this.channelInfo = this.$refs.disclosureRuleGrid.list;
      this.$set(this.formData,'channelList',JSON.stringify(this.channelInfo));
      this.$set(this.formData, 'fileList', JSON.stringify(this.noticeFiles.rows.concat(this.otherFiles.rows)));
      this.submitFiles = this.noticeFiles.rows.concat(this.otherFiles.rows);

      this.$set(this.formData, 'crtUserId',localStorage.getItem('userid'));

      /*if(this.formData.isNeedExamine!='0'){
        //走审批
        let disclosureType=this.formData.disclosureType;
        let disclosureSonType=this.formData.disclosureSonType;
        let updateaction="M8DisclosureManual.addSaleNotice";//按钮操作逻辑，默认销售文件
        let isApprovalction="M8DisclosureManualService-addSaleNotice";//判断是否进入审批逻辑，默认销售文件
        if(disclosureType=='2'){//发行公告
          updateaction="M8DisclosureManual.addIssueNotice";
          isApprovalction="M8DisclosureManualService-addIssueNotice";
        }else if(disclosureType=='3'){//到期公告
          updateaction="M8DisclosureManual.addDueNotice";
          isApprovalction="M8DisclosureManualService-addDueNotice";
        }else if(disclosureType=='4'){//运作公告
          updateaction="M8DisclosureManual.addOperationNotice";
          isApprovalction="M8DisclosureManualService-addOperationNotice";
        }else if(disclosureType=='5'){//定期公告
          updateaction="M8DisclosureManual.addRegularNotice";
          isApprovalction="M8DisclosureManualService-addRegularNotice";
        }else if(disclosureType=='6'){//整体公告
          updateaction="M8DisclosureManual.addWholeNotice";
          isApprovalction="M8DisclosureManualService-addWholeNotice";
        }else if(disclosureType=='7'){//重大事项报告
          if(disclosureSonType=="8"){//分红
            updateaction="M8DisclosureManual.addShareOutBonus";
            isApprovalction="M8DisclosureManualService-addShareOutBonus";
          }else if(disclosureSonType == "15"){//其他重大事项
            updateaction="M8DisclosureManual.addOthenBigIssues";
            isApprovalction="M8DisclosureManualService-addOthenBigIssues";
          }
        }else if(disclosureType=='8'){//临时公告
          updateaction="M8DisclosureManual.addTemporaryNotice";
          isApprovalction="M8DisclosureManualService-addTemporaryNotice";
        }else if(disclosureType=='9'){//净值报告
          updateaction="M8DisclosureManual.addNetNotice";
          isApprovalction="M8DisclosureManualService-addNetNotice";
        }else if(disclosureType=='11'){//其他公告
          updateaction="M8DisclosureManual.addOtherNotice";
          isApprovalction="M8DisclosureManualService-addOtherNotice";
        }
        this.httpUtil.comnQuery({
          action: 'M8DisclosureManual.queryIsHaveApproval',
          params: {"isApprovalction":isApprovalction},
          successAlert: false,
        }).then(data => {
          if(data.success){
            let res=data.returndata.res;
            if (res == 0){
              Tools.alert("未找到审批流配置信息,请核实！","danger");
              return false;
            }else
              this.submitVerify(this.formData, updateaction, {failAlert: false});
          }
        });
      }else{
        //不走审批
        this.httpUtil.comnUpdate({
          action: 'M8DisclosureManual.insertManualInfo',
          params: this.formData,
          successAlert: true,
        }).then(data => {
          Tools.closeCurrentWindow(this);
          this.$router.push({
            path: '/main/pms/basePublish/M8DisclosureOtherNotice/M8DisclosureOtherNotice',
            query: {},
          });
        });
      }*/
      this.httpUtil.comnUpdate({
        action: 'M8DisclosureManual.insertManualInfo',
        params: this.formData,
        successAlert: true,
      }).then(data => {
        this.refreshPage()
      });
    },
    beforeHandleUpdate() {
      return true;
    },
    afterHandleUpdate() {
      this.dataList= this.$refs.addForm.$slots.default;
      let prodCode1=this.formData.prodCode;
      let prodCode2="";
      if(prodCode1 !=""){
        prodCode2=prodCode1.split(",")[0];
      }
      this.dataList.prodCode=prodCode2;
    },
    handleFile(updateData) {
      for (var i = 0; i < this.submitFiles.length; i++) {
        this.httpUtil.ajax({
          url: "/wf/wf/attachment/add.json",
          params: {
            "upload_name": this.submitFiles[i].fileName,
            "upload_code": '1',
            "processId": updateData.returndata.processId,
            "fileType":'2',
            "uploader":localStorage.getItem('userid'),
            "upload_path": this.submitFiles[i].filePath
          }
        }).then(res => {
        });
      }
    },
    refreshPage() {
      Tools.closeCurrentWindow(this);
      this.$router.push({
        path: '/main/pms/disclosureNotice/M8DisclosureManualNotice',
        query: {url:"otherNoticeDetail"},
      });
    },
    // saveRule(params) {
    //   var flag = this.$refs.addForm.validate();
    //   if (flag == false) {
    //     return false;
    //   }
    //   const { filePublic, fileOther } = this;
    //   if (filePublic != undefined && fileOther != undefined) {
    //     if (filePublic.raw.name == fileOther.raw.name) {
    //       Tools.alert("公告文件与信披附件不允许同名，提交失败 ", "danger");
    //       return
    //     }
    //   }
    //
    //   this.dataList2 = this.$refs.channelInfo.dataList;
    //   console.log("this.dataList2",this.dataList2);
    //
    //   this.$set(this.formData,'channelList',JSON.stringify(this.dataList2));
    //   this.$set(this.formData, 'fileList', JSON.stringify(this.noticeFiles.rows.concat(this.otherFiles.rows)));
    //   this.submitFiles = this.noticeFiles.rows.concat(this.otherFiles.rows);
    //   //this.$set(this.formData, 'otherFiles', JSON.stringify(this.otherFiles.rows));
    //
    //
    //
    //
    //   if(this.formData.isNeedExamine!='0'){
    //     //走审批
    //     let disclosureType=this.formData.disclosureType;
    //     let updateaction="M8DisclosureManual.addSaleNotice";//销售文件
    //     if(disclosureType=='2'){//发行公告
    //       updateaction="M8DisclosureManual.addIssueNotice";
    //     }else if(disclosureType=='3'){//到期公告
    //       updateaction="M8DisclosureManual.addDueNotice";
    //     }else if(disclosureType=='4'){//运作公告
    //       updateaction="M8DisclosureManual.addOperationNotice";
    //     }else if(disclosureType=='5'){//定期公告
    //       updateaction="M8DisclosureManual.addRegularNotice";
    //     }else if(disclosureType=='6'){//整体公告
    //       updateaction="M8DisclosureManual.addWholeNotice";
    //     }else if(disclosureType=='7'){//重大事项报告
    //       updateaction="M8DisclosureManual.addBigIssuesNotice";
    //     }else if(disclosureType=='8'){//临时公告
    //       updateaction="M8DisclosureManual.addTemporaryNotice";
    //     }else if(disclosureType=='9'){//净值报告
    //       updateaction="M8DisclosureManual.addNetNotice";
    //     }else if(disclosureType=='11'){//其他公告
    //       updateaction="M8DisclosureManual.addOtherNotice";
    //     }
    //     this.httpUtil.comnUpdate({
    //       action: updateaction,
    //       params: this.formData,
    //       successAlert: false,
    //     }).then(res => {
    //       console.log('11111',res)
    //       this.autoFlowProcess(res);
    //
    //     });
    //
    // }else{
    //    //不走审批
    //       this.httpUtil.comnUpdate({
    //       action: 'M8DisclosureManual.insertManualInfo',
    //       params: this.formData,
    //       successAlert: true,
    //     }).then(data => {
    //         Tools.closeCurrentWindow(this);
    //         this.$router.push({
    //           path: '/main/pms/basePublish/M8DisclosureOtherNotice/M8DisclosureOtherNotice',
    //           query: {},
    //         });
    //
    //     });
    //
    //  }
    //
    //
    //
    // },
    // autoFlowProcess(row){
    //   this.dataList= this.$refs.addForm.$slots.default;
    //   let List =  []
    //   let processId = row.returndata.processId;
    //   let server = row.returndata.server;
    //   if(processId){
    //     console.log("submitFiles 长度",this.submitFiles.length)
    //     for (var i = 0; i < this.submitFiles.length; i++) {
    //
    //       this.httpUtil.ajax({
    //         url: "/wf/wf/attachment/add.json",
    //         params: {
    //           "upload_name": this.submitFiles[i].fileName,
    //           "upload_code": '1',
    //           "processId": processId,
    //           "fileType":'2',
    //           "upload_path": this.submitFiles[i].filePath
    //         }
    //       }).then(res => {
    //
    //
    //       });
    //     }
    //     this.httpUtil.ajax({
    //       url: "wf/businessProcess/getTemplate.json",
    //       params: {server: server}
    //     }).then(data => {
    //       List.push(data.data)
    //       List.push(processId)
    //       List.push(server)
    //       List.push(this.dataList)
    //      //List.push(noticeId)
    //       this.formList = List;
    //       this.$set(this.formList, 'processId', processId)
    //       console.log('this.formList',this.formList)
    //       console.log('this.List',List)
    //     })
    //
    //     this.$refs.addPopup.popup();
    //
    //
    //
    //   }else {
    //     Tools.alert("未找到审批流配置信息,请核实！","danger");
    //     return false;
    //   }
    //
    // },

    // popupClose() {
    //   this.$refs.addPopup.close();
    //
    //   Tools.closeCurrentWindow(this);
    //   this.$router.push({
    //     path: '/main/pms/basePublish/M8DisclosureOtherNotice/M8DisclosureOtherNotice',
    //     query: {},
    //   });
    // },

    connectProd(){
      if(this.formData.isConnectProd=='0'){
        this.formData.prodCode="";
        this.formData.prodName1="";
        this.$refs.channelInfo.findChannelInfo();
        return;
      }
    },
    changeDisclosureType(){
      //this.changeProd();
    },
    changeDisclosureSonType(){
      //this.changeProd();
    },
    changeProd() {

      if(this.formData.prodCode==''||this.formData.prodCode==null){
        this.formData.prodName1="";
        this.$set(this.disclosureNoticeData,'prodCode','');
        this.$set(this.disclosureNoticeData,'disclosureType','');
        this.$set(this.disclosureNoticeData,'disclosureSonType','');

        //this.$refs.channelInfo.findChannelInfo();
        return;
      }

      let prodCodes = this.formData.prodCode;
      var arr = prodCodes.split(",");
      if(arr.length==1 || arr.length==2){
        this.httpUtil.comnQuery({
          action:'T8ProdInfo.getProdInfoByCode',
          params:{prodCode:arr[0]}
        }).then(re => {
          if (re.rows.length > 0 && arr.length==1){
            this.$set(this.formData,'prodName1',re.rows[0].prodName);}
          else if (re.rows.length > 0 && arr.length==2){
            this.$set(this.formData,'prodName1',re.rows[0].prodName+"等");}
        });
      }
      if(arr.length>1) return;
      //查询渠道
      this.$set(this.disclosureNoticeData,'prodCode',arr[0]);
      this.$set(this.disclosureNoticeData,'disclosureType',this.formData.disclosureType);
      this.$set(this.disclosureNoticeData,'disclosureSonType',this.formData.disclosureSonType);

      //this.$refs.channelInfo.findChannelInfo();

      // this.$set(this.formData,'channelCode','');
      // this.httpUtil.comnQuery({
      //   action:'T8DisChannelInfo.findProdNoticeChannel',
      //   params:{disclosureType: this.formData.disclosureType,disclosureSonType:this.formData.disclosureSonType,
      //     prodCode:this.formData.prodCode}
      // }).then(data =>{
      //   if (data.rows.length >0)
      //     this.$set(this.formData,'channelCode',data.rows[0].channelCode);
      //
      // });
      /*this.httpUtil.comnQuery({
        action:'M8DisclosureManual.findUserInfo',
        params:{prodCode:arr[0]}
      }).then(data =>{
        this.$set(this.formData,'prodManager',data.rows[0].prodManager);
        this.$set(this.formData,'valuateManager',data.rows[0].valuateManager);
        this.$set(this.formData,'disclosureManager',data.rows[0].disclosureManager);

      });*/
      //查询产品系列代码
      this.httpUtil.comnQuery({
        action:'T8Dict.findDpbProdSerInfos',
        params:{prodCode:arr[0]}
      }).then(data =>{
        this.$set(this.formData,'prodSerCd',data.rows[0].prodCode);
      });
    },
    previewXP(row){

      //this.$refs.onlineEditPopup.popup();
      let fileName = this.formData.title + '.pdf';
      let index = row.filePath.lastIndexOf('\\');
      let filePath = row.filePath.substring(0, index);

      if (this.formData.title == null || this.formData.title==undefined || this.formData.title=='') {
        Tools.alert("请先输入公告标题!", "danger");
        return
      }else{
        this.httpUtil.preview({
          url: "/download/server/PmsApp/print/previewXPGGByNoticeVersion.json",
          params: {"filePath":filePath,
            "fileName":row.fileName,"disclosureType":"11","noticeTitle":this.formData.title}
        }, fileName);
      }


      /*this.httpUtil.comnQuery({
        action: 'M8DisclosureManual.previewXP',
        params: {"filePath":row.filePath,
          "fileName":row.fileName,"disclosureType":"11","noticeTitle":this.formData.title}
      }).then(data => {
        this.viewUrl = data.returndata.viewUrl;
      });*/



    },
    addHandler() {
      this.addDisclosureRuleForm = {};
      this.$refs.addDisclosureRulePopup.popup();
    },
    addDisclosureNoticeChannel() {
      if (this.addDisclosureRuleForm == null) {
        Tools.alert("请先选择渠道!", "danger");
        return
      }
      this.httpUtil.comnQuery({
        action: 'DisclosureChannel.findDisclosureChannelById',
        params: {"id":this.addDisclosureRuleForm.disclosureNoticeChannelId}
      }).then(data => {
        this.$nextTick(()=>{
          this.noticeChannelList.rows.push(data.rows[0]);
        })
      });
      //在这里关闭弹窗
      this.$refs.addDisclosureRulePopup.close();
    },
    deleteChannelInfo(val){
      for (let i = 0; i < this.noticeChannelList.rows.length; i++) {
        if (this.noticeChannelList.rows[i].fileName == val.fileName) {
          this.noticeChannelList.rows.splice(i, 1);
        }

      }
    }

  }
}
</script>

<style lang="scss" scoped>
@import "../../../styles/T81001.scss";


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
