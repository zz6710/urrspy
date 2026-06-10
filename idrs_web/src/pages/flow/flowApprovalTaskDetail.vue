<template>
  <div>
    <div>
      <h4>审批任务名称：{{formData.displayName}}</h4>
      <h4>目标处理日期：{{formData.processDeadline}}</h4>
      <div >
        <!--   页面配置形式     -->
        <div  v-if="formType==='1'">
          <div v-for="(item,i) in taskDetail" :key="item.id" style="margin-bottom: 10px">
                   <span>
                    {{ item.fieldName }}
                    </span>
            <div style="margin-top: 5px"></div>
            <k-field-text data-disabled data-clearable="false" v-model="taskDetail[i].fieldValue" />
          </div>
        </div>
        <div v-if="formType==='2'">
          <component ref="urlForm" v-bind:is="loadUrl" v-bind:dataData="formData" ></component>
        </div>
        <!--  自定义字段  -->
        <div  v-else-if="formType==='3'">
          <div v-for="(item,i) in taskDetail" :key="item.id" style="margin-bottom: 10px">
                   <span>
                    {{ item.fieldName }}
                    </span>
            <div style="margin-top: 5px"></div>
            <k-field-text data-disabled data-clearable="false" v-model="taskDetail[i].fieldValue" input-type="textarea"/>
          </div>
        </div>
        <!--   产品信息END     -->
        <!--  附件信息列表START      -->
        <div>
          <h4>附件信息</h4>
          <div style="width:100%;"><span style="color: #ed3333;font-size: 13px;font-weight: bold">注释：如果需要删除附件信息请联系后台管理员</span></div>
          <k-btn class="btn-custom-primary" style="width: 130px" data-functype="POPUP" data-target="uploadAttachmentPopupOfDetail" v-show="isUploadFileBtn">
            <md-icon >cloud_upload</md-icon>上传附件
          </k-btn>
          <el-table
            ref="newAttachmentGrid"
            :data="attachmentListOfDetail"
            style="width: 100%;">
            <el-table-column v-if="false" prop="id" label="id">
            </el-table-column>
            <el-table-column prop="fileType" label="附件类型" width="120" height="20px" align="center" :formatter="typeFormatter">
            </el-table-column>
            <el-table-column prop="originalFilename" label="附件名称" height="20px" align="center">
            </el-table-column>
            <el-table-column prop="username" label="上传人" width="120" height="20px" align="center">
            </el-table-column>
            <el-table-column prop="url" label="url" width="120" height="20px" align="center" v-if="false">
            </el-table-column>
            <el-table-column prop="createDate" label="上传时间" width="160" height="20px" align="center">
            </el-table-column>
            <el-table-column label="操作" align="center" width="300">
              <template slot-scope="scope">

                <!--<k-btn class="md-danger" data-descript="删除" data-functype="POPUP" data-size="mini"
                       :data-confirm="false" data-target="newAttachmentGrid"
                       v-if="isShowButtonOfDetail(scope.row)"
                       @click="deleteFileOfDetail(scope.$index, scope.row)">
                  删除
                </k-btn>-->

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
      </div>
      <div>
        <!--  评论列表      -->
        <flow-comment  v-bind:commentParams="commentParams"></flow-comment>
      </div>
    </div>
    <div style="text-align: center;margin-top: 40px">
      <k-btn class="btn-custom-plain" data-functype="CLOSE">
        <md-icon md-src="/static/svg/cancel.svg"></md-icon>关闭</k-btn>
    </div>
    <!-- 图片预览 -->
    <k-popup ref="previewImage" data-title="附件详情" data-width="60%">
      <el-image :src="imageUrl"></el-image>
    </k-popup>
<!--    <k-popup ref="onlinePreviewPopup" data-title="附件详情">-->
<!--      <div style="height: 800px">-->
<!--        <changxie-office ref="officeEdit" :option='option' />-->
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

  </div>
</template>

<script>
import FlowComment from "@/pages/flow/flowComment";
import KFlowShow from "@/components/k-flow/k-flow-show/k-flow-show";
import ChangxieOffice from "@/pages/pms/basePublish/changxieOffice";
import VabOnlyOffice from "@/pages/flow/onlyOfficeView";
import Tools from "@/utils/tools";
export default {
  name: "flowApprovalTaskDetail",
  components: {ChangxieOffice, KFlowShow, FlowComment, VabOnlyOffice},
  data(){
      return{
        formData:[],
        //任务详情数据
        taskDetail:[{
        }],
        //自定义字段数据
        filedList:[{

        }],
        flowDataList:[{}],
        pageUrl:'',
        formType:'',
        nowTime:'',
        isUploadFileBtn:false,
        fileList:[],
        fileData:'',
        showSubmitBtn:true,
        isNewAddOnPageOfDetail:true,
        newUsername:localStorage.getItem("username"),
        newUserId:localStorage.getItem("userid"),
        taskDetail: [{}],
        fromDataOfDetail: {},
        attachmentListOfDetail: [],
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
          key:'',
        },
        processId:'',
        imageUrl:'',
      }
  },
  props:{
    processData:{
      type:Object,
      required:true
    }
  },
  computed: {
    loadUrl() {
      const self = this;
      let url = this.pageUrl;
      console.log('url',url)
      if (url==undefined||url==''){
        return false;
      }else {
        return function (resolve) {
          require(['@/pages/'+url+'.vue'], resolve)
        };
      }
    }
  },
  created() {
    console.log('获取页面传递到详情的值：',this.processData.processId);
    let processId = this.processData.processId;
    this.getProcessData(processId);
    this.commentParams[0] = processId;
  },
  methods:{
    //获取流程数据
    getProcessData(processId){
      if (processId!==null){
        this.httpUtil
          .ajax({
            url: "/wf/businessProcess/getBusinessProcess.json",
            params: {
              processId: processId
            }
          })
          .then(res => {
            console.log('流程数据',res);
            this.formData = res.data;
            this.pageUrl = res.data.formUrl;
            this.getDetailList(this.formData);
          });
      }else {
        return false;
      }
    },

    //判断用户是否为当前上传附件人
    isShowButtonOfDetail(row){
      if (row.fileType=='3' && row.username==this.newUsername)
        return true;
      return false;
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
      this.getAttachmentList(this.processData.processId);
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
      this.fileData.append('processId',this.processData.processId);
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
    deleteFileOfDetail(index,row){
      Tools.confirm(() => {
          let isStatus = row.id;
          this.attachmentListOfDetail.splice(index,1)
          if (isStatus!=undefined){
            let attachmentId = row.id;
            this.httpUtil.ajax({
              url: "wf/wf/attachment/deleteFile.json",
              params: {id: attachmentId}
            }).then(res => {
              this.getAttachmentList(this.processData.processId);
            });
          }
        },
        "是否删除文件?"
      )
    },

    //获取详情信息
    getDetailList(row){
      let processId = row.id;
      this.getApprovalNodeList(processId);
      this.findFormInfo(processId);
      this.getAttachmentList(processId);
      // this.pageUrl = row.formUrl;
      this.commentParams[0] = row.id;
      this.commentParams[1] = row.displayName;
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
        });
    },
    //自定义表单数据
    findFormInfo(processId){
      this.httpUtil.ajax({
        url: "/wf/formInfo/getFormInfo.json",
        params: {processId : processId},
      }).then(res => {
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
            this.attachmentListOfDetail = res.rows;
          })
        });
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
    typeFormatter(row,column){
      console.log('row',row.fileType)
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
    isAssetTypeAnImage(ext) {
      return ['png', 'jpg', 'jpeg', 'bmp', 'gif', 'webp', 'psd', 'svg', 'tiff'].indexOf(ext.toLowerCase()) !== -1;
    },
  }
}
</script>

<style scoped>

</style>
