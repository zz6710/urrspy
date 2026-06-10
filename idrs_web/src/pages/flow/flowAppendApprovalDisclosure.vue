<template>
  <div>
    <div>
      <h4>审批任务名称：{{formData.displayName}}</h4>
      <h4>目标处理日期：{{formData.processDeadline}}</h4>
      <div >
        <div  v-if="formType==='1'">
          <div v-for="(item,i) in taskDetail" :key="item.id" style="margin-bottom: 10px">
                   <span>
                    {{ item.fieldName }}
                    </span>
            <div style="margin-top: 5px"></div>
            <k-field-text data-disabled data-clearable="false" v-model="taskDetail[i].fieldValue" />
          </div>
        </div>
        <div  v-else-if="formType==='3'">
          <div v-for="(item,i) in taskDetail" :key="item.id" style="margin-bottom: 10px">
                   <span>
                    {{ item.fieldName }}
                    </span>
            <div style="margin-top: 5px"></div>
            <k-field-text data-disabled data-clearable="false" v-model="taskDetail[i].fieldValue" input-type="textarea"/>
          </div>
        </div>
        <div>
          <h4>附件信息</h4>
          <el-table
            ref="newAttachmentGrid"
            :data="attachmentList"
            :row-class-name="rowStyle"
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
        <k-flow-show v-bind:flowDataList="flowDataList"/>
      </div>
      <div>
        <flow-comment  v-bind:commentParams="commentParams"></flow-comment>
      </div>
    </div>
    <div style="text-align: center;margin-top: 40px">
      <k-btn class="btn-custom-plain" data-functype="POPUP" @click="appendApprovalAttachment" :data-disabled="submitBtn">
        <md-icon md-src="/static/svg/cancel.svg"></md-icon>提交</k-btn>
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
  </div>
</template>

<script>
import FlowComment from "@/pages/flow/flowComment";
import KFlowShow from "@/components/k-flow/k-flow-show/k-flow-show";
import Tools from "@/utils/tools";
import ChangxieOffice from "@/pages/pms/basePublish/changxieOffice";
import VabOnlyOffice from "@/pages/flow/onlyOfficeView";
export default {
  name: "flowAppendApprovalDisclosure",
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
      attachmentList:[{}],
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
      submitBtn:true,
    }
  },
  props:{
    processData:{
      type:Array,
      required:true
    }
  },
  computed: {

  },
  created() {
    console.log('追加审批页面：',this.processData);
    let processId = this.processData.processId;
    let disclosureId = this.processData.id;
    this.getProcessData(processId,disclosureId);
    this.commentParams[0] = processId;
    this.processId=this.processData.processId;
  },
  methods:{
    //获取流程数据
    getProcessData(processId,disclosureId){
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
            this.getDetailList(this.formData,disclosureId);
          });
      }else {
        return false;
      }
    },
    //获取详情信息
    getDetailList(row,disclosureId){
      let processId = row.id;
      this.getApprovalNodeList(processId);
      this.findFormInfo(processId);
      // this.getAttachmentList(processId,disclosureId);
      this.queryNoticeFile(processId,disclosureId);
      // this.pageUrl = row.formUrl;
      this.commentParams[0] = row.id;
      this.commentParams[1] = row.displayName;
    },
    //节点数据
    getApprovalNodeList(processId){
      this.httpUtil
        .ajax({
          url: "/wf/approvalTask/listAllApprovalTasks.json",
          params: {
            processId: processId
          }
        })
        .then(res => {
          this.flowDataList = res.data;
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
    //查询定期公告文件（已生效）
    queryNoticeFile(processId,id){
      console.log('获取流程id，公告id',id)
      let initIds = [];
      this.httpUtil.ajax({
        url: "/wf/wf/attachment/getAttachmentList.json",
        params:{
          processId:processId,
        }
      }).then(res =>{
        this.attachmentList = res.rows;
        for (let i = 0; i < res.rows.length; i++) {
          if(res.rows[i].initId !=null && res.rows[i].initId !=""){
            let initIdsString =res.rows[i].initId.split("|");
            for(let j = 0; j < initIdsString.length; j++){
              initIds.push(initIdsString[j]);
            }
          }
        }
      })
      this.httpUtil.comnQuery({
        action: "T8DisclosureInitFile.qureyNoticeOtherFileList",
        params: {disclosureNoticeId: id,status:'1'}
      }).then(data => {
        console.log('文件数据-data',data);
        if (data.rows.length > 0){
          for (let i = 0; i < data.rows.length; i++) {
            let index = initIds.indexOf(data.rows[i].id);
            if (index==-1){
              this.submitBtn = false;
              let newAttachment = {};
              newAttachment.fileType="2";
              newAttachment.originalFilename=data.rows[i].fileName;
              //newAttachment.username = data.rows[i].createUser;
              let time = this.getCurrentTime(Date.now());
              console.log('time=====',time)
              newAttachment.createDate = time;
              newAttachment.url = data.rows[i].filePath;
              newAttachment.username = localStorage.getItem('username');
              newAttachment.initId = data.rows[i].id;
              newAttachment.processId = processId;
              console.log('data.rows',data.rows)
              console.log('newAttachment',newAttachment)
              this.attachmentList.push(newAttachment)
            }else{
            }
          }
        }
      })
    },
    appendApprovalAttachment(){
      console.log('提交按钮权限',this.submitBtn)
      if (this.submitBtn){
        this.$emit('submitAppendClose', '1')
        Tools.alert('未有最新版本报告','danger');
        return false;
      }
      let attachmentList = this.attachmentList;
      for (let i = 0; i < attachmentList.length; i++) {
        let id = attachmentList[i].id;
        console.log('获取原来附件及追加附件id',id)
        if (id==undefined){
          this.httpUtil.ajax({
            url: "/wf/wf/attachment/add.json",
            params: {
              "upload_name":attachmentList[i].originalFilename,
              "upload_code": '1',
              "processId": attachmentList[i].processId,
              "fileType":'2',
              "upload_path": attachmentList[i].url,
              "uploader":localStorage.getItem('userid'),
              "initId":attachmentList[i].initId
            }
          }).then(res => {
            if (res.status=='200'){
              this.$emit('submitAppendClose', '1');
            }
          });
        }
      }
      Tools.alert("最新版本报告追加到审批流成功",'success');
      this.$parent.$parent.$parent.$parent.refreshPage();
      //判断当前人员是否是该流程审批中人员,如果是，则跳转至审批页面
      this.httpUtil.ajax({
        url: "/wf/approvalTask/listPendingApprovalTasks.json",
        params: {processId:this.processId, userid:localStorage.getItem('userid')}
      }).then(data => {
        console.log('文件数据-data',data);
        if (data.rows.length > 0){
          this.$router.push({
            path:'/main/flow/flowActiveTaskNew',
            query:{'processId':this.processId}
          });
        }
      });
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
            this.attachmentList = res.rows;
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
      //this.option.key = value.id;
      if (suffix==='docx'||suffix==='doc'||suffix==='xls'||suffix==='xlsx'){
        //暂不可编辑
        //this.option.isEdit = true;
        this.option.isEdit = false;
      }else {
        this.option.isEdit = false;
      }
      this.option.lang = 'zh-CN';
      let localPath = getURL().onlineUrl;
      console.log('本地路径',localPath)
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
    getCurrentTime(timestamp){
      let date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
      var Y = date.getFullYear() + '-';
      var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1):date.getMonth()+1) + '-';
      var D = (date.getDate()< 10 ? '0'+date.getDate():date.getDate())+ ' ';
      var h = (date.getHours() < 10 ? '0'+date.getHours():date.getHours())+ ':';
      var m = (date.getMinutes() < 10 ? '0'+date.getMinutes():date.getMinutes()) + ':';
      var s = date.getSeconds() < 10 ? '0'+date.getSeconds():date.getSeconds();
      return Y+M+D+h+m+s;
    },
    //设置表格行颜色
    rowStyle({row, rowIndex}){
      if(row.id == undefined){
        return 'success_class';
      }else{
        return '';
      }
    }

  }
}
</script>

<style lang="scss" scoped>
.el-table /deep/ .success_class {
  background-color: #74df80;
}

</style>


