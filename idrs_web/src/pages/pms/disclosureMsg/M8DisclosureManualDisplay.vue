<template>
  <div class="md-card k-card md-theme-default parent-div" style="height: 1200px;">
    <div class="form-item prod-panel" style="margin-top:50px;margin-left:80px;display: -webkit-box;">
      <k-form ref="addForm" :data-col="3" data-input-width="150px">
        <k-form-item label="公告Id" v-if="false">
          <k-field-text v-model="formData.t8DisclosureNoticeId" :data-max-length="100" :dataAllowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="公告标题" :data-col="3">
          <k-field-text v-model="formData.title" :data-max-length="100" :dataAllowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="计划发布日期">
          <k-field-date v-model="formData.planFbDate" data-type="date" style="width: 100%;"
                        :dataAllowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="产品代码"  :data-col="2">

          <k-field-select v-model="formData.prodCode" data-action="T8Dict.findDpbProdInfos" style="width: 100%;"
                          data-display-field="prodCode,prodName" data-value-field="prodCode" :dataAllowblank="true" :data-multiple = "true"
                          @data-on-change="changeProd" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="系列代码" :data-col="3" v-show="false">
          <k-field-text v-model="formData.prodSerCd" :data-disabled="true"/>
        </k-form-item>
        <!--该字段仅针对审批流使用-->
        <k-form-item label="产品名称" v-show="false" :data-col="1" >

          <k-field-text v-model="formData.prodName1" :data-disabled="true" />
        </k-form-item>

        <k-form-item label="公告文件" :data-col="3">
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
                       @click="previewXPGGTempVersion(props.row.row)" data-functype="POPUP">
                  <font color="#00bcd4" style="font-size: 12px;">预览</font>
                </k-btn>
                <k-btn class="md-info md-just-icon md-simple" v-show="false"  data-size="small" @click="delNoticeFile(props.row.row)">
                  <font color="#00bcd4" style="font-size: 12px;">删除</font>
                </k-btn>
              </template>
            </k-grid>
          </div>
        </k-form-item>

        <k-form-item label="发布渠道" :data-col="3" >
          <div style="width:100%;"><span style="color: #ed3333;font-size: 13px;font-weight: bold">注释：添加公告需要发布的渠道</span></div>
          <k-btn style="width: 120px" data-functype="POPUP" class="btn-custom-primary" v-show="false" :data-disabled="true"
                 @click="addHandler">
            <md-icon>cloud_upload</md-icon>
            添加渠道
          </k-btn>
          <div id="channelInfo">
            <k-grid ref="disclosureRuleGrid" @data-row-select="selectRow" :dataData="noticeChannelList" data-operate-width="200px" data-operate-column="false">
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
                <k-btn class="btn-custom-plain" data-functype="SUBMIT" v-show="false"
                       data-action="DisclosureNoticeChannel.sendChannelsEmails" data-size="mini"
                       :data-model="props.row.row" data-target="disclosureRuleGrid" :data-confirm="true"
                       data-descript="手动发布公告" :data-disabled="!(props.row.row.noticeChannelPublicStatus==='2' | props.row.row.noticeChannelPublicStatus==='-1')">发布
                </k-btn>
                <k-btn class="md-danger" data-functype="SUBMIT" data-type="danger" v-show="false"
                       data-size="mini" @click="deleteChannelInfo(props.row.row)"
                       :data-model="props.row.row" data-target="disclosureRuleGrid"
                       data-descript="删除渠道">删除 :data-disabled="true"
                </k-btn>
              </template>
            </k-grid>
          </div>
        </k-form-item>
      </k-form>

    </div>
  </div>
</template>

<script>
import {assign} from "lodash";
import Tools from "@/utils/tools";
import ManualChannelInfo from './components/ManualChannelInfo.vue'

export default {
  name: "M8DisclosureManualDisplay",
  components: {ManualChannelInfo},
  data() {
    return {
      showSubmitBtn1:true,
      viewUrl:'',
      formList:{},
      dataList:{},
      channelInfo:{},
      noticeChannelList:{rows: [],},
      tempNoticeChannelList:{rows: [],},
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
      tempNoticeFiles: {
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
    this.id = this.$route.query.id;//公告id
    Tools.getLoginUser().then(res => {
      this.formData.crtUser = res.username;
      let date = Tools.getCurrentTime();
      console.log("date>>>",date);
      this.$set(this.formData,'crtDate',date)
    })

    this.$set(this.formData,'isAutoSend','0');//暂不支持自动发布
    this.refreshPageParam();
  },
  methods: {

    refreshPageParam() {

      this.$nextTick(() => {

        this.t8DisclosureNoticeId = this.id;

        if (this.id != '' && this.id != undefined) {
          this.httpUtil.comnQuery({
            action: 'DisclosureNotice.findDisclosureManualNoticesAuth',
            params: {id: this.id,}
          }).then(data => {
            if (data.rows.length > 0) {
              this.disclosureTypeChange(data.rows[0].disclosureType);
              this.$set(this.formData, "t8DisclosureNoticeId", data.rows[0].id);
              this.$set(this.formData, "t8ProdInfoId", data.rows[0].t8ProdInfoId);
              this.$set(this.formData, "prodName", data.rows[0].prodName);
              if (data.rows[0].prodName != '') {
                this.showProd = true;
              }
              this.$set(this.formData, "prodCode", data.rows[0].prodCode);
              this.$set(this.formData, "prodCode", data.rows[0].prodCode);
              this.$set(this.formData, "prodSerCd", data.rows[0].prodSerCd);
              this.$set(this.formData, "title", data.rows[0].noticeTitle);
              this.$set(this.formData, "planFbDate", data.rows[0].planFbDate);
            }
            this.httpUtil.comnQuery({
              action: 'DisclosureNoticeChannel.findDisclosureManualNoticeChannelsInfo',
              params: {disclosureNoticeId: this.id}
            }).then(data => {
              if (data.rows.length > 0)
                this.$nextTick(() => {
                  for(let i=0;i<data.rows.length;i++){
                    let row = data.rows[i];
                    row.channelPublicDate=this.formData.planFbDate;
                    this.tempNoticeChannelList.rows.push(row);
                  }
                  for(let j=0;j<this.tempNoticeChannelList.rows.length;j++){
                    this.noticeChannelList.rows.push(this.tempNoticeChannelList.rows[j]);
                  }
                });

            });
            this.httpUtil.comnQuery({
              action: 'DisclosureNoticeVersion.findManualVersionsInfoById',
              params: {t8DisclosureNoticeId: this.id}
            }).then(data => {
              if (data.rows.length > 0)
                this.$nextTick(() => {
                  for(let i=0;i<data.rows.length;i++){
                    let row = data.rows[i];
                    this.tempNoticeFiles.rows.push(row);
                  }
                  for(let j=0;j<this.tempNoticeFiles.rows.length;j++){
                    this.noticeFiles.rows.push(this.tempNoticeFiles.rows[j]);
                  }
                });

            });

          });

        }
        ;
      });

    },
    //预览定期报告
    previewXPGGTempVersion(rows) {
      let fileName = this.formData.title + '.pdf';
      rows.noticeTitle=this.formData.title;
      rows.id=this.formData.t8DisclosureNoticeId;
      rows.t8DisclosureNoticeId=this.formData.t8DisclosureNoticeId;
      rows.disclosureType='11';
      console.log("rows=:>>",rows);
      this.httpUtil.comnQuery({
        action: "DisclosureNoticeProcess.findMaxVersionById",
        params: {t8DisclosureNoticeId: this.formData.t8DisclosureNoticeId}
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
    validateData(){
      return true;
    },
    closePage() {
      Tools.closeCurrentWindow(this);
      this.$router.push({
        path: '/main/pms/disclosureNotice/M8DisclosureManualNotice',
        query: {url:"otherNoticeDetail"},
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
        query: {},
      });
    },

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

      //查询产品系列代码
      this.httpUtil.comnQuery({
        action:'T8Dict.findDpbProdSerInfos',
        params:{prodCode:arr[0]}
      }).then(data =>{
        this.$set(this.formData,'prodSerCd',data.rows[0].prodCode);
      });
    },
    previewXP(row){

      this.$refs.onlineEditPopup.popup();


      this.httpUtil.comnQuery({
        action: 'M8DisclosureManual.previewXP',
        params: {"filePath":row.filePath,
          "fileName":row.fileName}
      }).then(data => {
        this.viewUrl = data.returndata.viewUrl;
      });



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
