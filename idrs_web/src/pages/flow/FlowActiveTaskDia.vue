<template>
  <div>

    <k-form ref="prodGroupForm" :data-col="3" data-input-width="220px" data-label-width="180px" data-total-width="988px" style="width: 100%">
      <k-form-item label="审批任务名称" :data-col="2" data-input-width="180px">
        <k-field-text v-model="fromData.processDisplayName" data-disabled/>
      </k-form-item>
      <k-form-item label="目标处理日期">
        <k-field-date v-model="fromData.processDeadline" data-disabled/>
      </k-form-item>
      <!--      <component ref="urlForm" v-if="showUrlPage" :is="pageUrl" :dataData="fromData" style="width: 100%" />-->

      <k-form-item  v-for="(item,index) in dataList" :key="index" :label="item.fieldName" data-label-width="180px" :hidden="states" style="width: 100%">
        <k-field-text v-model="item.fieldValue" :data-allowblank="true" inputType="textarea" :data-max-length="500" :rows="1" data-disabled/>
      </k-form-item>



    </k-form>

    <!--  url形式组件显示    -->
    <component v-bind:is="loadUrl" v-bind:dataData="fromData" v-if="showUrlPage"></component>

    <k-form ref="prodGroupFormCustom" v-for="(item,index) in dataList" :key="index"  :data-col="2" data-input-width="560px"
            data-label-width="180px" data-total-width="1118px" :hidden="state" style="width: 100%">
      <k-form-item :label="item.fieldName" >
        <k-field-text v-model="item.fieldValue" :data-allowblank="true" inputType="textarea" data-disabled :data-max-length="500" :rows="1"/>
      </k-form-item>
    </k-form>

    <!-- 处理产品清盘流程表单的数据 -->
    <!--    <div v-if="opFlowStatus">-->
    <!--      <OpFlowDetail :processInstanceId="opProcessInstance" />-->
    <!--    </div>-->

    <!--  附件信息  -->
    <div style="width: 100%">
      <h4>附件信息</h4>
      <div style="width:100%;"><span style="color: #ed3333;font-size: 13px;font-weight: bold">注释：如果需要删除附件信息请联系后台管理员</span></div>
      <k-btn class="btn-custom-primary" style="width: 130px" data-functype="POPUP" data-target="uploadAttachmentPopup">
        <md-icon >cloud_upload</md-icon>上传附件
      </k-btn>
      <!--  附件信息列表START      -->
      <el-table
        ref="newAttachmentGrid"
        :data="attachmentList"
        style="width: 100%;">
        <el-table-column v-if="false" prop="id" label="id">
        </el-table-column>
        <el-table-column prop="fileType" label="附件类型" width="120" height="20px" align="center" :formatter="typeFormatter">
        </el-table-column>
        <el-table-column prop="originalFilename" label="附件名称" height="20px" align="center">
        </el-table-column>
        <el-table-column prop="username" label="上传人" width="100" height="20px" align="center">
        </el-table-column>
        <el-table-column prop="url" label="url" width="120" height="20px" align="center"  v-if="false">
        </el-table-column>
        <el-table-column prop="createDate" label="上传时间" width="150" height="20px" align="center">
        </el-table-column>
        <el-table-column label="操作" width="300" height="120px">
          <template slot-scope="scope">
            <k-btn class="btn-custom-text" data-descript="删除" data-functype="POPUP" data-size="mini"
                   :data-confirm="false" data-target="newAttachmentGrid"
                   v-if="isShowButton(scope.row)"
                   @click="deleteFile(scope.$index, scope.row)">
              删除
            </k-btn>
            <k-btn class="btn-custom-text" data-descript="附件下载" data-functype="POPUP" data-size="mini"
                   :data-confirm="true" data-target="newAttachmentGrid" @click="download(scope.row)">
              下载
            </k-btn>
            <k-btn class="btn-custom-text" data-descript="附件查看" data-functype="POPUP" data-size="mini"
                   :data-confirm="true" @click="getFile(scope.row,'view')">
              查看
            </k-btn>
          </template>
        </el-table-column>
      </el-table>
      <!--  附件信息列表END      -->
    </div>
    <div style="width: 100%" >
      <!--   审批流程   -->
      <k-flow-show v-bind:flowDataList="flowDataList"/>
    </div>
    <div>
      <!--  评论列表      -->
      <flow-comment  v-bind:commentParams="commentParams"></flow-comment>
    </div>

    <!--  在线浏览    -->
    <!--    <k-popup ref="onlinePreviewPopup" data-title="附件详情">-->
    <!--      <div style="height: 800px">-->
    <!--        <changxie-office :option='option' />-->
    <!--      </div>-->
    <!--    </k-popup>-->
    <k-popup ref="onlinePreviewPopup" data-title="附件详情">
      <div style="height: 800px">
        <VabOnlyOffice ref="officeEdit" :option='option' />
      </div>
    </k-popup>

    <k-form ref="prodGroupFormOpinion" :data-col="3" data-input-width="220px" data-label-width="180px" data-total-width="988px" style="width: 100%;margin-top: 40px;" >
      <k-form-item label="审批意见" data-input-width="220px" :data-col="2">
        <k-field-text v-model="fromData.opinion" :data-max-length="500" inputType="textarea" :rows="3"/>
      </k-form-item>
    </k-form>

    <div style="text-align: center">
      <k-btn
        class="btn-custom-primary"
        data-functype="POPUP"
        v-if="btns.indexOf('3')>-1"
        :data-handler="passDataHandler"
        :data-after-success="passDataSuccess"
        data-target="dialogGrid"
        :data-model="fromData"
        @click="approvalConfirm(1)"
        data-from="prodGroupFormOpinion"
      >
        <md-icon md-src="/static/svg/confirm.svg"></md-icon>通过
      </k-btn>
      <k-btn
        class="md-danger"
        data-functype="POPUP"
        v-if="btns.indexOf('4')>-1"
        :data-after-success="passDataSuccess"
        :data-validate-form="false"
        data-target="dialogGrid"
        :data-model="fromData"
        @click="approvalConfirm(2)"
        data-from="prodGroupFormOpinion"
      >
        <md-icon md-src="/static/svg/cancel.svg"></md-icon>拒绝
      </k-btn>

      <k-btn
        class="md-warning"
        data-functype="POPUP"
        v-show="isShowCountersign"
        data-target="taskGridCountersignPopup"
        :data-model="fromData"
        @click="saveNewAttachment"
        data-from="taskGridFrom"
      >
        <md-icon md-src="/static/svg/block_white.svg" />加签
      </k-btn>

      <k-btn
        class="md-warning"
        data-functype="POPUP"
        v-if="btns.indexOf('5')>-1"
        data-target="taskGridPopup"
        :data-model="fromData"
        @click="saveNewAttachment"
        data-from="taskGridFrom"
      >
        <md-icon md-src="/static/svg/block_white.svg" />转交
      </k-btn>
      <k-btn
        class="md-warning"
        data-functype="POPUP"
        v-show="isShowTransfer"
        :data-model="fromData"
        @click="transferBack()"
        data-from="taskGridFrom"
      >
        <md-icon md-src="/static/svg/block_white.svg" />转回
      </k-btn>

      <k-btn class="btn-custom-primary" data-functype="POPUP" data-descript="分享" data-size="mini" data-target="sharePopup">
        分享</k-btn>

      <k-btn class="btn-custom-plain" data-functype="CLOSE">
        <md-icon md-src="/static/svg/cancel.svg"></md-icon>关闭</k-btn>
    </div>

    <k-popup ref="taskGridPopup"  data-width="60%"  >
      <k-form ref="taskGridFrom" :data-col="2">

        <k-form-item label="转交人员">
          <k-field-select v-model="fromData.applyUser" data-action="User.getOtherUser" @data-on-change=getUserInfo(fromData.applyUser)
                          data-display-field="username"  data-value-field="userid" :data-allowblank="false"  ></k-field-select>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-from="taskGridFrom" :data-handler="approvalTransfer"
                 :data-model="fromData" data-target="taskGrid"  >
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE" data-target="dialogGrid"  >
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="taskGridCountersignPopup"  data-width="60%"  >
      <k-form ref="taskGridCountersignFrom" :data-col="2">

        <k-form-item label="加签人员">
          <k-field-select v-model="fromData.countersignUser" data-action="User.getOtherUser" @data-on-change=getCountersignUserInfo(fromData.countersignUser)
                          data-display-field="username"  data-value-field="userid" :data-allowblank="false"  ></k-field-select>
        </k-form-item>

        <k-form-item label="加签方式" v-show="this.nodeType=='1'">
          <k-field-select v-model="fromData.countersignType" :data-default-value="'1'" data-dict="countersign_type"  :data-allowblank="this.nodeType!='1'"  ></k-field-select>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-from="taskGridFrom" :data-handler="approvalCountersign"
                 :data-model="fromData" data-target="taskGrid"  >
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE" data-target="dialogGrid"  >
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--  上传附件弹框  -->
    <k-popup ref="uploadAttachmentPopup" data-title="上传附件" data-width="40%">
      <k-form ref="fileForm" data-ui="element">
        <!--          <k-form-item   label="附件" data-ui="element" data-input-width="500px">-->
<!--        <k-field-upload data-type="file" ref="uploadFileRef" :data-multiple="true"-->
<!--                        :data-error="onFileSubmitError" :data-success="onFileSubmitSuccess"-->
<!--                        :data-auto-upload="false" :data-limit=10-->
<!--                        data-upload-url="wf/upload/uploadFileNew.json">-->
<!--        </k-field-upload>-->
        <k-field-upload
                        label="附件"
                        data-type="file"
                        ref="uploadRef"
                        :data-multiple="true"
                        :data-limit="10"
                        :data-error="onSubmitError"
                        :dataChange="onUploadChange"
                        :dataHttpRequest="httpRequest"
                        :data-auto-upload="false"
                        :data-success="onFileSubmitSuccess"
        >
        </k-field-upload>
        <!--          </k-form-item>-->
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="attachmentGrid" ref="fileSubmitBtn"
                 data-from="fileForm" :data-model="fromData" :data-handler="fileSubmitUploadParam">
            <span v-show="showSubmitBtn">确定</span>
            <i v-show="!showSubmitBtn" class="el-icon-loading" />
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--  分享弹框  -->
    <k-popup ref="sharePopup" data-title="任务分享" data-width="60%">
      <PersonTree v-bind:processData="fromData" @submitClose="popupClose"/>
    </k-popup>

    <!-- 图片预览 -->
    <k-popup ref="previewImage" data-title="附件详情" data-width="60%">
      <el-image :src="imageUrl"></el-image>
    </k-popup>

  </div>
</template>

<script>
import Tools from "@/utils/tools";
import {assign} from "lodash";
import KFlowShow from "@/components/k-flow/k-flow-show/k-flow-show";
import FlowComment from "@/pages/flow/flowComment";
import PersonTree from "@/pages/flow/PersonTree";
import OpFlowDetail from '@/pages/operation/flow/OpFlowDetail'
import ChangxieOffice from "@/pages/pms/basePublish/changxieOffice";
import VabOnlyOffice from "@/pages/flow/onlyOfficeView";

export default {
  name: "FlowActiveTaskDia",
  components: {
    ChangxieOffice,
    KFlowShow,FlowComment,PersonTree,OpFlowDetail, VabOnlyOffice},
  props: {
    fromData:{},
  },

  data() {
    return {
      showGroupName:false,
      state:false,
      states:false,
      formData:{},
      nodeList:[],
      dataList:[],
      formType:{},
      flowDataList:[],
      btns:'345',
      prodCreateInfo:{},
      //上传文件信息
      attachmentList:[{
      }],
      commentParams:[],
      newList:[],
      newFileName:'',
      newFileUrl:'',
      approvalTaskId:'',
      newUsername:localStorage.getItem("username"),
      newUserId:localStorage.getItem("userid"),
      newFileType:'',
      newUploadDate:'',
      nodeType:"",
      newFileSize:'',
      newAttachmentId:1,
      isNewAddOnPage:true,
      urlFlag:"",
      aFile:[],
      pageUrl:'',
      showUrlPage:false,
      transferUsername:'',
      countersignUsername:'',
      newOpinion:'',
      option: {},
      imageUrl:'',
      isShowCountersign:false,
      isShowTransfer:false,
      surrogateUser:'',
      transferBackUsername:'',
      opFlowFormList:[],
      opFlowStatus: false,
      opProcessInstance:'',
      urlFormDta:{},
      checkFlag:true,

      fileList:[],
      fileData:'',
      showSubmitBtn:true,
    }
  },
  computed: {
    loadUrl() {
      const self = this;
      let url = self.pageUrl;
      console.log('url',url)
      return function (resolve) {
        require(['@/pages/'+url+'.vue'], resolve)
      };
    }
  },
  created() {
    console.log("fromData",this.fromData);
    if(this.fromData.urlFlag){
      this.urlFlag=this.fromData.urlFlag;
    }
    this.findFormInfo();
    this.getApprovalNodeList();
    this.fromData.opinion = '同意';
    this.getAttachmentList();
    this.pageUrl = this.fromData.taskFormUrl;
    this.approvalTaskId = this.fromData.approvalTaskId;
    this.fromData.applyUser = '';
    //获取评论数据
    this.commentParams[0] = this.fromData.processId;
    this.commentParams[1] = this.fromData.processDisplayName;
    this.getSurrogateInfo();
    //根据taskID查询节点类型：只有依次审批和并行审批才能加签
    this.getNodeType();
    console.log('待审批页面：commentParams',this.commentParams)
    // this.getOpFlowInfo();
  },
  methods: {
    httpRequest(file) {
      this.fileData.append("files", file.file);
    },
    onSubmitError() {
      this.$refs.uploadRef.doReset();
      this.showSubmitBtn = true;
    },
    onUploadChange(file, fileList) {
      this.fileList = fileList;
    },
    //针对产品清盘-获取操作流信息
    getOpFlowInfo(){
      this.httpUtil.ajax({
        url: 'wf/businessProcess/querySubmitParams.json',
        params: {'processInstanceId':this.fromData.processId},
      }).then(data => {
        let flowJson = JSON.parse(data.data);
        if (flowJson.opProcessInstance){
          this.opProcessInstance = flowJson.opProcessInstance;
          this.opFlowStatus = true;
          this.httpUtil.comnQuery({
            action: 'OpFormParam.findOpFormDataInfo',
            params: {
              processInstanceId: flowJson.opProcessInstance
            }
          }).then(res => {
            this.opFlowFormList = res.rows;
          });
        }
      });
    },
    //转回
    transferBack(){
      let userid = this.surrogateUser;
      this.getUserInfo(userid);
      Tools.confirm(()=>{
        this.transferBackRequest(this.fromData);
      },'是否将审批流程转回给 '+this.transferBackUsername+'?')
    },
    //获取转交信息
    getSurrogateInfo(){
      this.httpUtil.ajax({
        url: "wf/transferInfo/getSurrogateInfo.json",
        params: {
          processId: this.fromData.processId
        }
      }).then(res => {
        if (res.data.surrogate){
          this.isShowTransfer = true;
          this.surrogateUser = res.data.creator;
          this.transferBackUsername = res.data.username;
        }
      });
    },

    //查询节点类型
    getNodeType(){
      this.httpUtil.ajax({
        url: "wf/approvalTask/queryNodeType.json",
        params: {
          approvalTaskId: this.fromData.approvalTaskId
        }
      }).then(res => {
        if (res.data.nodeType){
          this.nodeType = res.data.nodeType;
          if(this.nodeType==='1' || this.nodeType==='2'){
            this.isShowCountersign=true;
          }else this.isShowCountersign=false;
        }else this.isShowCountersign=false;
      });
    },

    //转交请求
    transferBackRequest(value){
      if (value.opinion=='同意'){
        value.opinion = this.newOpinion;
      }else {
        value.opinion = value.opinion + ' 转交给 '+this.transferUsername;
      }
      this.httpUtil.ajax({
        url: "/wf/approvalTask/forApproval.json",
        params: {approvalTaskId:value.approvalTaskId, result:'5' ,isSurrogate : 'true' ,
          opinion : value.opinion ,surrogate : this.surrogateUser , processId:value.processId},
      }).then(data => {
        console.log('data',data)
        if (data.data == '任务审批成功'){
          //this.saveNewAttachment();
          Tools.alert("审批任务已转交");
          if(this.urlFlag=="1"){
            this.$parent.$parent.$parent.$parent.refreshPage();
          }
        }
        this.$nextTick(() => {
          this.passDataSuccess();
        })
      })
    },
    isAssetTypeAnImage(ext) {
      return ['png', 'jpg', 'jpeg', 'bmp', 'gif', 'webp', 'psd', 'svg', 'tiff'].indexOf(ext.toLowerCase()) !== -1;
    },
    //子组件关闭父组件弹窗
    popupClose() {
      this.$refs.sharePopup.close();
    },
    typeFormatter(row,column){
      return row.fileType === '1' ? "经办附件" : row.fileType === '2' ? "业务附件" :row.fileType === '3' ? "审批附件" : row.fileType;
    },
    getFile(value) {
      //文件路径
      let url = value.url;
      console.log('文件路径',url);
      //文件名
      let fileName = value.originalFilename;
      console.log('文件名',fileName)
      //文件后缀
      let suffix = fileName.substring(fileName.lastIndexOf("."))
      suffix = suffix.substring(1,suffix.length);
      console.log('文件后缀',suffix)
      this.show = true;
      //能否编辑
      this.option.isEdit = false;
      this.option.lang = 'zh-CN';
      let localPath = getURL().onlineUrl;
      //服务器路径
      this.option.url = localPath+url;
      console.log('文件URL',this.option.url);
      this.option.title = fileName;
      this.option.fileType = suffix;
      //能否打印
      this.option.isPrint = false;
      this.option.user= { id:localStorage.getItem('userid'),name:localStorage.getItem('username')};
      this.$refs.onlinePreviewPopup.popup()
    },
    preview(value){
      console.log('文件信息',value)
      console.log('URL',value.url)
      let fileName = value.originalFilename;
      //文件后缀
      let suffix = fileName.substring(fileName.lastIndexOf("."))
      suffix = suffix.substring(1,suffix.length).toLowerCase();
      //文件预览
      this.getFile(value);
      this.$refs.onlinePreviewPopup.popup()
    },
    // preview(row,mode){
    //   console.log('value查看',row)
    //   console.log('URL',row.url)
    //   let fileName = row.originalFilename;
    //   //文件后缀
    //   let suffix = fileName.substring(fileName.lastIndexOf("."))
    //   suffix = suffix.substring(1,suffix.length).toLowerCase();
    //   //图片预览
    //   if (this.isAssetTypeAnImage(suffix)){
    //     this.imageUrl = getURL().onlineUrl+row.url;
    //     this.$refs.previewImage.popup();
    //   }else {//文件预览
    //     this.option = {};
    //     //能否编辑
    //     this.option.mode = mode;//view edit
    //     //服务器路径
    //     this.option.url = encodeURI(row.url);
    //     this.option.key = "flow_share_" + row.id;   //key  需要唯一
    //     console.log('文件URL',this.option.url);
    //     this.option.title = row.originalFilename;
    //     this.option.fileType = suffix;
    //     if (suffix == 'docx' || suffix == 'doc') {
    //       this.option.documentType = "word";
    //     } else if (suffix == 'xls' || suffix == 'xlsx') {
    //       this.option.documentType = 'cell';
    //     } else {
    //       this.option.documentType = '';
    //     }
    //     this.option.type = "desktop";
    //     this.option.limitEditMode = "nolimit";
    //     this.option.operateType = "";  //操作类型  模板  用户畅写第一次初始化内容域
    //     this.option.user= { userid:localStorage.getItem('userid'),username:localStorage.getItem('username')};
    //     this.option.permissions = {"com":"gdbank"};
    //     this.option.printTempVersionId = row.id;
    //     this.$refs.onlinePreviewPopup.popup()
    //   }
    // },
    approvalConfirm(type){
      if(this.fromData.taskFormUrl==="flow/prod/flowLiquidation" && type=='1'){
        this.httpUtil.ajax({
          url: 'wf/businessProcess/querySubmitParams.json',
          params: {'processInstanceId': this.fromData.processId},
        }).then(async data => {
          this.urlFormDta = JSON.parse(data.data);
          await this.httpUtil.sysparamFor('80000084011', '8').then(async res => {
            if (res) {
              let taskDisplayName1=this.fromData.taskDisplayName;
              if (taskDisplayName1.indexOf(res)!=-1) {
                let roleIds = localStorage.getItem("roleids");
                let roleIdsArr = [];
                if (roleIds != null && roleIds != undefined) {
                  roleIdsArr = roleIds.split(',');
                }
                await this.httpUtil.sysparamFor('80000084012', '8').then(res2 => {
                  if (res2) {
                    if (roleIdsArr.some(role => role == res2)) {
                      this.checkFlag = true;
                      if (this.urlFormDta.liquidationDate == null || this.urlFormDta.liquidationDate == undefined || this.urlFormDta.liquidationDate == '') {
                        Tools.alertTime("清盘日不能为空", "danger", 4000);
                        this.checkFlag = false;
                        if(this.checkFlag){
                          this.execApproval(type)
                        }
                      }else{
                        this.execApproval(type)
                      }
                    }else{
                      this.execApproval(type)
                    }
                  }
                });
              }else{
                await this.httpUtil.sysparamFor('80000084013', '8').then(async res3 => {
                  if (res3) {
                    let taskDisplayName2=this.fromData.taskDisplayName;
                    if (taskDisplayName2.indexOf(res3)!=-1) {
                      let roleIds = localStorage.getItem("roleids");
                      let roleIdsArr = [];
                      if (roleIds != null && roleIds != undefined) {
                        roleIdsArr = roleIds.split(',');
                      }
                      await this.httpUtil.sysparamFor('80000084014', '8').then(res4 => {
                        if (res4) {
                          if (roleIdsArr.some(role => role == res4)) {
                            this.checkFlag = true;
                            if (this.urlFormDta.corpus == null || this.urlFormDta.corpus == undefined || this.urlFormDta.corpus == '') {
                              Tools.alertTime("清盘总份额不能为空", "danger", 4000);
                              this.checkFlag = false;
                            } else if (this.urlFormDta.totalAmount == null || this.urlFormDta.totalAmount == undefined || this.urlFormDta.totalAmount == '') {
                              Tools.alertTime("清盘总金额不能为空", "danger", 4000);
                              this.checkFlag = false;
                            }
                            if(this.checkFlag){
                              this.execApproval(type)
                            }
                          }else{
                            this.execApproval(type)
                          }
                        }
                      });
                    }else{
                      this.execApproval(type)
                    }
                  }
                })
              }
            }
          })
        })
      }else{
        this.execApproval(type)
      }
    },
    execApproval(type){
      if (type=='1'){
        Tools.confirm(() => {
            this.passApproval();
          },
          "是否通过该审批?"
        )
      }else if (type=='2'){
        Tools.confirm(() => {
            this.rejectApproval();
          },
          "是否拒绝该审批?"
        )
      }
    },
    //通过审批
    passApproval(){
      console.log('fromData',this.fromData)
      this.httpUtil.ajax({
        url: "/wf/approvalTask/forApproval.json",
        params: {approvalTaskId:this.fromData.approvalTaskId, result:'3' ,isSurrogate : 'false' ,opinion : this.fromData.opinion
          ,processId:this.fromData.processId},
      }).then(data => {
        console.log('data',data)
        if (data.data == '任务审批成功'){
          //this.saveNewAttachment();
          Tools.alert("审批任务通过");
          if(this.urlFlag=="1"){
            this.$parent.$parent.$parent.$parent.refreshPage();
          }
        }
        this.$nextTick(() => {
          this.passDataSuccess();
        })
      })

    },
    //拒绝审批
    rejectApproval(){
      console.log('fromData',this.fromData)
      this.httpUtil.ajax({
        url: "/wf/approvalTask/forApproval.json",
        params: {approvalTaskId:this.fromData.approvalTaskId, result:'4' ,isSurrogate : 'false' ,opinion : this.fromData.opinion
          ,processId:this.fromData.processId},
      }).then(data => {
        console.log('data',data)
        if (data.data == '任务审批成功'){
          this.saveNewAttachment();
          Tools.alert("审批任务拒绝");
          if(this.urlFlag=="1"){
            this.$parent.$parent.$parent.$parent.refreshPage();
          }
        }
        this.$nextTick(() => {
          this.passDataSuccess();
        })
      })
    },
    saveNewAttachment(){
      // let list = this.attachmentList;
      // for (let i in list){
      //   if (list[i].isNewAddOnPage){
      //     this.aFile.push(list[i])
      //   }
      // }
      // let nFileRec = this.aFile;
      // this.httpUtil
      //   .ajax({
      //     url: "wf/wf/attachment/saveFile.json",
      //     params: {
      //       recordList: JSON.stringify(nFileRec)
      //     }
      //   })
      //   .then(res => {
      //     console.log(res)
      //   });
    },
    deleteFile(index,row){
      Tools.confirm(() => {
          let isStatus = row.id;
          this.attachmentList.splice(index,1)
          if (isStatus!=undefined){
            let attachmentId = row.id;
            this.httpUtil
              .ajax({
                url: "wf/wf/attachment/deleteFile.json",
                params: {
                  id: attachmentId
                }
              })
              .then(res => {
              });
          }
        },
        "是否删除文件?"
      )
    },
    download(value){
      let fileName = value.originalFilename;
      this.httpUtil.download({
        url:"download/server/WorkflowServer/wf/attachment/downloadFile.json",
        params: value,
        callback: response => {

        }
      },fileName)
      return false
    },
    getUserInfo(userid){
      this.httpUtil
        .ajax({
          url: "wf/web3api/findUser.json",
          params: {'userid':userid}
        })
        .then(res => {
          if (res.data!=null){
            this.transferUsername = res.data.text;
            this.newOpinion = '已转交给 '+this.transferUsername;
            console.log('this.newOpinion',this.newOpinion)
          }
        });
    },

    getCountersignUserInfo(userid){
      this.httpUtil
        .ajax({
          url: "wf/web3api/findUser.json",
          params: {'userid':userid}
        })
        .then(res => {
          if (res.data!=null){
            this.countersignUsername = res.data.text;
            this.newOpinion = '加签给 '+this.countersignUsername;
            console.log('this.newOpinion',this.newOpinion)
          }
        });
    },

    approvalTransfer(value){
      if(value.applyUser==null || value.applyUser==''){
        Tools.alert("未选择转交人员!","danger");
        return false;
      }
      console.log('获取参数',value)
      if (value.opinion=='同意'){
        value.opinion = this.newOpinion;
      }else {
        value.opinion = value.opinion + " 转交给 "+this.transferUsername;
      }

      this.httpUtil.ajax({
        url: "/wf/approvalTask/forApproval.json",
        params: {approvalTaskId:value.approvalTaskId, result:'5' ,isSurrogate : 'true' ,opinion : value.opinion ,surrogate : value.applyUser , displayName : value.serverName , processId:value.processId},
      }).then(data => {
        console.log('data',data)
        if (data.data == '任务审批成功'){
          Tools.alert("审批任务已转交");
          if(this.urlFlag=="1"){
            this.$parent.$parent.$parent.$parent.refreshPage();
          }
        }
        this.$nextTick(() => {
          this.passDataSuccess();
          this.$refs.taskGridPopup.close();
        })
      })
    },


    approvalCountersign(value){
      if(value.countersignUser==null || value.countersignUser==''){
        Tools.alert("未选择加签人员!","danger");
        return false;
      }
      if(this.nodeType=='1' && (value.countersignType==null || value.countersignType=='')){
        Tools.alert("未选择加签方式!","danger");
        return false;
      }

      //依次审批默认加签方式为之后
      if(this.nodeType=='2'){
        value.countersignType='2';
      }
      console.log('获取参数',value)
      if (value.opinion=='同意'){
        value.opinion = this.newOpinion;
      }else {
        value.opinion = value.opinion + ",并加签给 "+this.countersignUsername;
      }

      this.httpUtil.ajax({
        url: "/wf/approvalTask/forApproval.json",
        params: {approvalTaskId:value.approvalTaskId, result:'6' ,countersignType : value.countersignType,opinion : value.opinion ,countersignUser : value.countersignUser , displayName : value.serverName , processId:value.processId},
      }).then(data => {
        console.log('data',data)
        if (data.data == '任务审批成功'){
          Tools.alert("审批任务已加签");
          if(this.urlFlag=="1"){
            this.$parent.$parent.$parent.$parent.refreshPage();
          }
        }
        this.$nextTick(() => {
          this.passDataSuccess();
          this.$refs.taskGridCountersignPopup.close();
        })
      })
    },
    passDataSuccess(){
      this.$emit('submitClose', '1');
    },

    findFormInfo(){
      console.log('123',this.fromData)
      let list = new Array();
      this.httpUtil.ajax({
        url: "/wf/formInfo/getFormInfo.json",
        params: {processId : this.fromData.processId},
      }).then(data => {
        console.log('data',data)
        let value = data.data;
        let formType = null;
        for (let i = 0; i < value.length; i++) {
          formType = value[i].formType
          if ( formType == '2' ){
            this.fromData.dataName = formUrl;
            this.state = true;
            this.states = true;
          }else if (formType == '1'){
            this.dataList = data.data;
            this.state = true;
            this.showGroupName = true;
          }else if (formType == '3'){
            this.dataList =data.data;
            this.states = true;
            this.showGroupName = true;
          };
        }
        if (value==undefined||value==null||value==''){
          console.log('类型为空')
          this.showUrlPage = true;
        }
      })
    },
    passDataHandler(value) {
      console.log('点击获取',value)
    },
    getAttachmentList(){
      let processId = this.fromData.processId;
      console.log(processId)
      this.httpUtil.ajax({
          url: "wf/wf/attachment/getAttachmentList.json",
          params: {processId: processId}
        }).then(res => {
          this.attachmentList = res.rows;
        });
    },
    //上传失败操作
    onFileSubmitError(){
      this.$refs.uploadFileRef.doReset();
      this.$refs.fileSubmitBtn.setIconStyle(1, []);
    },
    dealTime(timestamp){
      let date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
      var Y = date.getFullYear() + '-';
      var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1):date.getMonth()+1) + '-';
      var D = (date.getDate()< 10 ? '0'+date.getDate():date.getDate())+ ' ';
      var h = (date.getHours() < 10 ? '0'+date.getHours():date.getHours())+ ':';
      var m = (date.getMinutes() < 10 ? '0'+date.getMinutes():date.getMinutes()) + ':';
      var s = date.getSeconds() < 10 ? '0'+date.getSeconds():date.getSeconds();
      return Y+M+D+h+m+s;
    },
    //上传成功操作
    onFileSubmitSuccess(response,file,fileList) {
      this.$refs.uploadRef.doReset();
      this.$refs.fileForm.reset();
      this.$refs.uploadAttachmentPopup.close();
      this.getAttachmentList();
      //this.$refs.attachmentGrid.load();
    },
    //上传附件
    fileSubmitUploadParam(){
      var validate = this.$refs.fileForm.validate();
      if (validate) {
        // let _formData = this.fromData;
        // this.$set(_formData,'fileType','3');
        // this.$set(_formData,'processId',this.fromData.processId);
        // let temp = document.getElementsByClassName('upload-demo');
        // let lis = temp[0].childNodes[1].childNodes.length;
        // if(lis>0){
        //   this.$refs.uploadFileRef.upload(_formData);
        // }else{
        //   Tools.alert("上传文件不能为空!","danger");
        //   return false;
        // }
        //
        ///////////////
        this.showSubmitBtn = false;
        let uploadId = this.uploadData
        this.fileData = new FormData();
        this.$refs.uploadRef.upload();
        // this.fileData.append("params", JSON.stringify(uploadId));
        this.fileData.append('fileType', '3');
        this.fileData.append('processId', this.fromData.processId);
        this.httpUtil.upload({
          url: "/upload-files/server/WorkflowServer/upload/uploadFileNew.json",
          formData: this.fileData,
        }).then((res) => {
          // Tools.alert(res.data.returnmsg);
          this.showSubmitBtn = true;
          this.onFileSubmitSuccess();
        }).catch(res => {
          this.showSubmitBtn = true;
        });
      }
    },
    selectRow(row, column, event) {
      const _this = this
      _this.selectRowData = assign({}, row)
      _this.fromData = assign({}, row)
    },
    getApprovalNodeList(){
      let processId = this.fromData.processId;
      this.fromData.id = processId;
      console.log(processId)
      this.httpUtil
        .ajax({
          url: "/wf/approvalTask/listAllApprovalTasks.json",
          params: {
            processId: processId
          }
        })
        .then(res => {
          this.flowDataList = res.data;
          this.$set(this.fromData,"disabled",'true')
        });
    },
    //判断用户是否为当前上传附件人
    isShowButton(row){
      if (row.fileType=='3' && row.username==this.newUsername)
        return true;
      return false;
    }
  },
  watch:{
    // fromData : function(value) {
    //   console.log('监听',value)
    // }
  },
}
</script>

<style scoped>


</style>
