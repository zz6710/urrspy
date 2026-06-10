<template>
  <!-- 抄送历史任务 -->
  <div>
    <!--  动态查询框  -->
    <k-form-search-customize data-target="grid" v-model="queryParam">
      <k-form-item label="发起人">
        <k-field-select v-model="queryParamApplyUser" ref="surrogateSelect" data-action="User.findUsersWithQY"
                        data-display-field="username"  data-value-field="userid"/>
      </k-form-item>
      <k-form-item label="审批任务名称">
        <k-field-text v-model="queryParamTaskName"/>
      </k-form-item>
      <k-form-item label="发起时间">
        <k-field-date v-model="queryParamDateRange" data-type="daterange"
                      data-value-format="yyyy-MM-dd HH:mm:ss"/>
      </k-form-item>
      <k-form-item label="确认状态">
        <k-field-select v-model="result" :data-data="approvalResult" />
      </k-form-item>
    </k-form-search-customize>
    <!--  任务列表START  -->
    <k-grid
            ref="grid"
            @data-row-select="selectRow"
            :data-display="false"
            data-url='/wf/businessProcess/queryProcessReadonly.json' :data-params="{'readonlyActor':userid}">
      <k-grid-column data-align="center"
                     data-header="processId"
                     data-name="processId"
                     data-hidden="true"
      ></k-grid-column>
      <k-grid-column data-align="center"
                     data-header="URL"
                     data-name="formUrl"
                     data-hidden="true"
      ></k-grid-column>
      <k-grid-column data-align="center"
                     data-header="审批任务名称"
                     data-name="displayName"
      ></k-grid-column>
      <k-grid-column data-align="center"
                     data-header="发起人"
                     data-name="creator"
      ></k-grid-column>
      <k-grid-column data-align="center"
                     data-header="当前审批人"
                     data-name="approvalUser"
                     data-render="renderApprovalUser"
      ></k-grid-column>
      <k-grid-column data-align="center"
                     data-header="状态"
                     data-name="processStatus"
      ></k-grid-column>
      <k-grid-column data-align="center"
                     data-header="确认状态"
                     data-name="verifyStatus"
      ></k-grid-column>
      <k-grid-column data-align="center"
                     data-header="发起时间"
                     data-name="createTime"
      ></k-grid-column>
      <k-grid-column data-align="center"
                     data-header="结束时间"
                     data-name="finishDate"
                     data-render="renderFinishDateTime"
      ></k-grid-column>
      <template slot="operate" slot-scope="{row}">
        <k-btn class="btn-custom-text" data-descript="详情" data-functype="POPUP" data-size="mini"
               data-target="detailPopup" @click="getDetailList(row.row)">
          详情
        </k-btn>
        <k-btn class="btn-custom-text" data-descript="审批单下载" data-functype="POPUP" data-size="mini"
               data-target="detailPopup2" @click="getDetailList(row.row)">
          审批单下载
          <!-- 审批单下载 -->
        </k-btn>
      </template>
    </k-grid>
    <!--  任务列表END  -->
    <!--  任务详情弹框START  -->
    <k-popup ref="detailPopup" data-title="详情" data-width="60%">
      <div>
        <h4>审批任务名称：{{formData.displayName}}</h4>
        <h4>目标处理日期：{{formData.processDeadline}}</h4>
        <div>
          <!--   产品信息START     -->
          <!--   自定义字段形式     -->
          <div>
            <div  v-if="formType==='3'">
              <div v-for="(item,i) in taskDetail" :key="item.i" style="margin-bottom: 10px">
                   <span>
                    {{ item.fieldName }}
                    </span>
                <div style="margin-top: 5px"></div>
                <div style="min-height:30px;border: solid 1px #f7f7f9;height: auto;background-color: #f7f7f7;white-space:pre-wrap;" v-html="taskDetail[i].fieldValue"></div>
                <!--<k-field-text data-disabled data-clearable="false" v-model="taskDetail[i].fieldValue" input-type="textarea"/>-->
              </div>
            </div>
            <div  v-else-if="formType==='1'">
              <div v-for="(item,i) in taskDetail" :key="item.i" style="margin-bottom: 10px">
                   <span>
                    {{ item.fieldName }}
                    </span>
                <div style="margin-top: 5px"></div>
                <k-field-text data-disabled data-clearable="false" v-model="taskDetail[i].fieldValue" />
              </div>
            </div>
            <div v-else>
              <component v-bind:is="loadUrl" v-bind:dataData="formData"></component>
            </div>
          </div>
          <!--  附件信息列表START      -->
          <div>
            <h4>附件信息</h4>
            <div style="width:100%;"><span style="color: #ed3333;font-size: 13px;font-weight: bold">注释：如果需要删除附件信息请联系后台管理员</span></div>
            <k-btn class="btn-custom-primary" style="width: 130px" data-functype="POPUP" data-target="uploadAttachmentPopupOfDetail" v-show="isUploadFileBtn">
              <md-icon >cloud_upload</md-icon>上传附件
            </k-btn>
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
              <el-table-column prop="username" label="上传人" width="120" height="20px" align="center">
              </el-table-column>
              <el-table-column prop="createDate" label="上传时间" width="160" height="20px" align="center">
              </el-table-column>
              <el-table-column label="操作" align="center" width="200">
                <template slot-scope="scope">
                  <k-btn class="btn-custom-text" data-descript="附件下载" data-functype="POPUP" data-size="mini"
                         :data-confirm="true" data-target="attachmentGrid" @click="download(scope.row)">
                    下载
                  </k-btn>
                  <k-btn class="btn-custom-text" data-descript="附件查看" data-functype="POPUP" data-size="mini"
                         :data-confirm="true" @click="preview(scope.row,'view')">
                    查看
                  </k-btn>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <!--  附件信息END      -->
          <!--   审批流程START     -->
          <k-flow-show v-bind:flowDataList="flowDataList"/>
          <!--   审批流程END     -->
          <div>
            <!--  评论列表      -->
            <flow-comment  v-bind:commentParams="commentParams"></flow-comment>
          </div>
          <div style="text-align: center;margin-top: 40px" v-show="isShow">
            <k-btn
                    class="btn-custom-primary"
                    data-functype="SUBMIT"
                    @click="confirmSend"
                    data-target="detailPopup"
            >
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确认
            </k-btn>
          </div>
          <div style="text-align: center;margin-top: 40px" v-show="!isShow">
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>关闭</k-btn>
          </div>
        </div>
      </div>
    </k-popup>
    <!--  任务详情弹框END  -->
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
    <!--  上传附件弹框  -->
    <k-popup ref="uploadAttachmentPopupOfDetail" data-title="上传附件" data-width="40%">
      <k-form ref="fileFormOfDetail" data-ui="element">
        <!--          <k-form-item   label="附件" data-ui="element" data-input-width="500px">-->
        <!-- <k-field-upload data-type="file" ref="uploadFileRefOfDetail" :data-multiple="true"
                        :data-error="onFileSubmitErrorOfDetail" :data-success="onFileSubmitSuccessOfDetail"
                        :data-auto-upload="false" :data-limit=10
                        data-upload-url="wf/upload/uploadFileNew.json">-->
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
          :data-success="onFileSubmitSuccessOfDetail">
        </k-field-upload>
        <!--          </k-form-item>-->
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="newAttachmentGrid" ref="fileSubmitBtnOfDetail"
                 data-from="fileFormOfDetail" :data-handler="fileSubmitUploadParamOfDetail">
            <span v-show="showSubmitBtn">确定</span>
            <i v-show="!showSubmitBtn" class="el-icon-loading" />
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
    <!-- 审批单下载页面-->
    <k-popup ref="detailPopup2" data-title="详情" data-width="60%">
      <el-button type="danger"
                 @click="ExportSavePdf(formData.displayName,nowTime)">下载审批单</el-button>
      <div id="pdfCentent">
        <h4>审批任务名称：{{formData.displayName}}</h4>
        <h4>目标处理日期：{{formData.processDeadline}}</h4>
        <div>
          <!--   产品信息START     -->
          <!--   自定义字段形式     -->
          <div>
            <div  v-if="formType==='3'">
              <div v-for="(item,i) in taskDetail" :key="item.i" style="margin-bottom: 10px">
                   <span style="font-size: 16px">
                    {{ item.fieldName }}
                    </span>
                <div style="margin-top: 5px"></div>
                <!--              <k-field-text data-disabled data-clearable="false" v-model="taskDetail[i].fieldValue" input-type="textarea"/>-->
                <!--//20220525 审批单下载页面textarea转换为div-->
                <div class="text_style"><span>{{taskDetail[i].fieldValue}}</span></div>
              </div>
            </div>
            <div  v-else-if="formType==='1'">
              <div v-for="(item,i) in taskDetail" :key="item.i" style="margin-bottom: 10px">
                   <span>
                    {{ item.fieldName }}
                    </span>
                <div style="margin-top: 5px"></div>
                <k-field-text data-disabled data-clearable="false" v-model="taskDetail[i].fieldValue" />
              </div>
            </div>
            <div v-else>
              <component v-bind:is="loadUrl" v-bind:dataData="formData"></component>
            </div>
          </div>
          <!--  附件信息列表START      -->
          <div>
            <h4>附件信息</h4>
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
              <el-table-column prop="username" label="上传人" width="120" height="20px" align="center">
              </el-table-column>
              <el-table-column prop="createDate" label="上传时间" width="180" height="20px" align="center">
              </el-table-column>
            </el-table>
          </div>
          <!--  附件信息END      -->
          <!--   审批流程START     -->
          <k-flow-show v-bind:flowDataList="flowDataList"/>
          <!--   审批流程END     -->
          <div>
            <!--  评论列表      -->
            <flow-comment-readonly  v-bind:commentParams="commentParams"></flow-comment-readonly>
          </div>
        </div>
      </div>
      <div style="text-align: center;margin-top: 40px">
        <k-btn class="btn-custom-plain" data-functype="CLOSE">
          <md-icon md-src="/static/svg/cancel.svg"></md-icon>关闭</k-btn>
      </div>
    </k-popup>
    <!-- 图片预览 -->
    <k-popup ref="previewImage" data-title="附件详情" data-width="60%">
      <el-image :src="imageUrl"></el-image>
    </k-popup>
  </div>

</template>

<script>

  import {assign} from "lodash";
  import Tools from "@/utils/tools.js";
  import FlowProcessInstanceListDialog from './FlowProcessInstanceListDialog'
  import KFlowShow from "@/components/k-flow/k-flow-show/k-flow-show";
  import FlowComment from "@/pages/flow/flowComment";
  import FlowCommentReadonly from "@/pages/flow/flowCommentReadonly";
  import OpFlowDetail from "@/pages/operation/flow/OpFlowDetail";
  import ChangxieOffice from "@/pages/pms/basePublish/changxieOffice";
  import VabOnlyOffice from "@/pages/flow/onlyOfficeView";

  export default {
    name: "historyTask",
    components: {
      ChangxieOffice,
      OpFlowDetail,
      FlowCommentReadonly,
      FlowComment, FlowProcessInstanceListDialog,KFlowShow,VabOnlyOffice},
    data() {
      return {
        formData: {},
        selectRowData: {},
        queryParamApplyUser: '',
        queryParamTaskName: '',
        isUploadFileBtn:false,
        fileList:[],
        fileData:'',
        showSubmitBtn:true,
        newUsername:localStorage.getItem("username"),
        newUserId:localStorage.getItem("userid"),
        taskDetail: [{}],
        fromDataOfDetail: {},
        attachmentListOfDetail: [],
        result: '',
        approvalResult:[
          {
            value:'7',
            label:'已确认'
          },
          {
            value:'8',
            label:'未确认'
          }
        ],
        queryParamDateRange:[],
        userid:localStorage.getItem("userid"),
        flowDataList:[{}],
        taskDetail:[{}],
        userName: localStorage.getItem("username"),
        isShow:'',
        pageUrl:'',
        formType:'',
        nowTime:'',
        attachmentList:[{
        }],
        commentParams:[],
        option: {
          url: '',
          isEdit: '',
          fileType: '',
          title: '',
          lang: '',
          isPrint: '',
          user: { id:null,name:''},
          editUrl:'',
        },
        processId:'',
        imageUrl:'',
        opFlowStatus:false,
        opProcessInstance:'',
      };
    },
    computed: {
      queryParam() {
        return {
          'processNameLike': this.queryParamTaskName,
          'applyUser': this.queryParamApplyUser,
          'result': this.result,
          'createStartDate': this.queryParamDateRange ? this.queryParamDateRange[0] : null,
          'createEndDate': this.queryParamDateRange ? this.queryParamDateRange[1] : null,
          'userid':localStorage.getItem("userid"),
        }
      },
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
      this.processId = this.$route.query.processId;
    },
    mounted() {
      this.processId='';
    },
    methods: {
      isAssetTypeAnImage(ext) {
        return ['png', 'jpg', 'jpeg', 'bmp', 'gif', 'webp', 'psd', 'svg', 'tiff'].indexOf(ext.toLowerCase()) !== -1;
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
      },
      preview(value){
        console.log('文件信息',value)
        console.log('URL',value.filePath)
        let fileName = value.originalFilename;
        //文件后缀
        let suffix = fileName.substring(fileName.lastIndexOf("."))
        suffix = suffix.substring(1,suffix.length).toLowerCase();
        //文件预览
        this.getFile(value);
        this.$refs.onlinePreviewPopup.popup()
      },
      onSubmitError() {
        this.$refs.uploadRef.doReset();
        this.showSubmitBtn = true;
      },
      onUploadChange(file, fileList) {
        this.fileList = fileList;
      },
      httpRequest(file) {
        this.fileData.append("files", file.file);
      },
      //上传失败操作
      onFileSubmitErrorOfDetail(){
        this.$refs.uploadFileRefOfDetail.doReset();
        this.$refs.fileSubmitBtnOfDetail.setIconStyle(1, []);
      },

      //上传成功操作
      onFileSubmitSuccessOfDetail(response,file,fileList) {
        this.$refs.uploadRef.doReset();
        this.$refs.fileFormOfDetail.reset();
        this.$refs.uploadAttachmentPopupOfDetail.close();
        this.getAttachmentList(this.formData.id);
      },

      dealTimeOfDetail(timestamp){
        let date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
        var Y = date.getFullYear() + '-';
        var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1):date.getMonth()+1) + '-';
        var D = (date.getDate()< 10 ? '0'+date.getDate():date.getDate())+ ' ';
        var h = (date.getHours() < 10 ? '0'+date.getHours():date.getHours())+ ':';
        var m = (date.getMinutes() < 10 ? '0'+date.getMinutes():date.getMinutes()) + ':';
        var s = date.getSeconds() < 10 ? '0'+date.getSeconds():date.getSeconds();
        return Y+M+D+h+m+s;
      },
      //上传附件
      fileSubmitUploadParamOfDetail(){
        var validate = this.$refs.fileFormOfDetail.validate();
        if (!validate) {
          Tools.alert("上传文件不能为空!","danger");
          return false;
        }
        this.showSubmitBtn = false;
        this.fileData = new FormData();
        this.$refs.uploadRef.upload();
        this.fileData.append('fileType', '3');
        this.fileData.append('processId',this.formData.id);
        this.httpUtil.upload({
          url: "/upload-files/server/WorkflowServer/upload/uploadFileNew.json",
          formData: this.fileData,
        }).then((res) => {
          this.showSubmitBtn = true;
          this.onFileSubmitSuccessOfDetail();
        }).catch(res => {
          this.showSubmitBtn = true;
        });
      },
      typeFormatter(row,column){
        return row.fileType === '1' ? "经办附件" : row.fileType === '2' ? "业务附件" :row.fileType === '3' ? "审批附件" : row.fileType;
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
      processFinishButBusiError(row) {
        return (row.processStatus === this.global.wf_process_status.finish &&
                row.busStatus === this.global.wf_business_status.error) || (row.processStatus === this.global.wf_process_status.finish &&
                row.busStatus === this.global.wf_business_status.processing);
      },
      renderCreateDateTime(row) {
        return Tools.formatDateTime(row.startDate, row.startTime);
      },
      renderApplyUser(row) {
        if (row.approvalUser) {
          return row.approvalUser
        } else {
          return row.applyUser
        }
      },
      selectRow(row, column, event) {
        const _this = this
        _this.selectRowData = assign({}, row)
        _this.formData = assign({}, row)
      },
      //处理字符串时间
      renderFinishDateTime(row) {
        let datetime = row.finishDate;
        if (!datetime) {
          return "";
        }
        let result = datetime.substring(0, 4) + "-" + datetime.substring(4, 6) + "-" + datetime.substring(6, 8) + " "+datetime.substring(9, 11) + ":" + datetime.substring(11, 13) + ":" + datetime.substring(13, 15);
        return result;
      },
      confirmSend(){
        let id = this.formData.id;
        this.httpUtil.ajax({
          url: "/wf/readonlyActor/readonlyActorConfirm.json",
          params: {processId : id},
        }).then(res => {
          if (res.status=='200'){
            this.$message.success('确认成功')
            this.isShow = false;
            this.getApprovalNodeList(this.formData.id);
          }
        })
      },
      //获取详情数据
      getDetailList(row){
        let processId = row.id;
        this.formData.id = processId;
        this.getApprovalNodeList(processId);
        this.findFormInfo(processId);
        this.getAttachmentList(processId);
        //获取评论数据
        this.commentParams[0] = row.id;
        this.commentParams[1] = row.displayName;
        console.log('抄送页面：commentParams',this.commentParams)
        this.pageUrl = row.formUrl;
        console.log('this.pageUrl',this.pageUrl);
        // this.getOpFlowInfo(processId);
      },
      //审批下载
      getDetailList2(row){
        let processId = row.id;
        this.formData.id = processId;
        this.getApprovalNodeList2(processId);
        this.findFormInfo(processId);
        this.getAttachmentList(processId);
        //获取评论数据
        this.commentParams[0] = row.id;
        this.commentParams[1] = row.displayName;
        console.log('抄送页面：commentParams',this.commentParams)
        this.pageUrl = row.formUrl;
        console.log('this.pageUrl',this.pageUrl);
        // this.getOpFlowInfo(processId);
      },
      getOpFlowInfo(processId){
        this.httpUtil.ajax({
          url: 'wf/businessProcess/querySubmitParams.json',
          params: {'processInstanceId':processId},
        }).then(data => {
          let flowJson = JSON.parse(data.data);
          if (flowJson.opProcessInstance){
            this.opProcessInstance = flowJson.opProcessInstance;
            this.opFlowStatus = true;
          }
        });
      },
      //节点数据
      getApprovalNodeList(processId){
        this.httpUtil.ajax({
                  url: "/wf/approvalTask/listAllApprovalTasks.json",
                  params: {processId: processId}
         }).then(res => {
          this.flowDataList = res.data;
          //根据流程ID查询当前节点是否允许可上传附件
          this.httpUtil.ajax({
            url: "/wf/approvalNode/listIsUploadFile.json",
            params: {processId: processId}
          }).then(res => {
            if(res.data.length>0){
              this.isUploadFileBtn=true;
            }else{
              this.isUploadFileBtn=false;
            }
          });
            this.$set(this.formData,"processId",this.formData.id)
            this.$set(this.formData,"disabled",'true')
            if (res.status=='200'){
               this.dealConfirm();
            }
         });
      },
      //节点数据
      getApprovalNodeList2(processId){
        this.httpUtil
          .ajax({
            url: "/wf/approvalTask/listApprovalTasksWithoutCS.json",
            params: {
              processId: processId
            }
          })
          .then(res => {
            this.flowDataList = res.data;
            this.$set(this.formData,"processId",this.formData.id)
            this.$set(this.formData,"disabled",'true')
            if (res.status=='200'){
              this.dealConfirm();
            }
          });
      },
      dealConfirm(){
        let list = this.flowDataList;
        let x;
        if(list[1].nodeLevel=='999' || list.length == 1)
          return this.isShow = false;
        for (x in list){
          if (list[x].nodeLevel=='999'&&list[x].result=='未确认'&&list[x].operator==this.userName){
            this.isShow = true;
            return this.isShow;
          }
        }
        this.isShow = false;
        return this.isShow;
      },
      //自定义表单数据
      findFormInfo(processId){
        console.log('表单数据参数',processId)
        this.httpUtil.ajax({
          url: "/wf/formInfo/getFormInfo.json",
          params: {processId : processId},
        }).then(res => {
          console.log('表单数据',res)
          this.taskDetail=res.data;
          if (this.taskDetail.length !=0 ){
            this.formType = this.taskDetail[0].formType;
          }else {
            this.formType = '2';
          }
        })
      },
      getAttachmentList(val){
        let processId = val;
        console.log("sfadfsadf"+processId)
        this.httpUtil
          .ajax({
            url: "wf/wf/attachment/getAttachmentList.json",
            params: {
              processId: processId
            },
            successAlert: false,
          })
          .then(res => {

            this.$nextTick(()=>{
              this.attachmentList = res.rows;
            })
          });
      },
      renderApprovalUser(row) {
        let user = row.approvalUser;
        if (!user) {
          return "-";
        }
        return user;
      },
    }
  };
</script>

<style scoped>
  /deep/ .el-dialog {
    padding-top: 35px;
  }
  .text_style{
    padding: 10px;
    border-radius: 5px;
    margin-top: 10px;
    border: 1px solid #d7dae2;
    /*text-indent: 2em*/
  }
</style>
