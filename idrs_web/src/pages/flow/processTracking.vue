<template>
  <!-- 流程追踪 -->
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
      <k-form-item label="状态">
        <k-field-select v-model="queryParamStatus" data-dict="business_status" />
      </k-form-item>
      <k-form-item label="发起时间">
        <k-field-date v-model="queryParamDateRange" data-type="daterange"
                      data-value-format="yyyy-MM-dd HH:mm:ss"/>
      </k-form-item>
    </k-form-search-customize>
    <!--  任务列表START  -->
    <k-grid
            ref="grid"
            @data-row-select="selectRow"
            :data-display="false"
            data-url='/wf/businessProcess/queryProcessTrack.json' :data-params="{'userid':userid}">
      <k-grid-column data-align="center"
                     data-header="id"
                     data-name="id"
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
                     data-header="发起时间"
                     data-name="createTime"
      ></k-grid-column>
      <template slot="operate" slot-scope="{row}">
        <k-btn class="btn-custom-text" data-descript="详情" data-functype="POPUP" data-size="mini"
               data-target="detailPopup" @click="getDetailList(row.row)">
          详情
        </k-btn>
        <k-btn class="btn-custom-text" data-descript="撤销" data-functype="SUBMIT"
               data-size="mini" data-target="grid" :disabled="isDisable(row.row)"
               @click="revokeBusinessProcess(row.row)">
          撤销
        </k-btn>
        <k-btn class="btn-custom-text" data-descript="审批单下载" data-functype="POPUP" data-size="mini"
               data-target="detailPopup2" @click="getDetailList(row.row)" v-show="false">
          审批单下载
          <!-- 审批单下载 -->
        </k-btn>
      </template>
    </k-grid>
    <!--  任务列表END  -->
    <!--  任务详情弹框START  -->
    <k-popup ref="detailPopup" data-title="详情" data-width="60%">
      <h4>审批任务名称：{{formData.displayName}}</h4>
      <h4>目标处理日期：{{formData.processDeadline}}</h4>
      <div>
        <!--   产品信息START     -->
        <!--   自定义字段形式     -->
        <div>
          <div  v-if="formType==='3'">
            <div v-for="(item,i) in taskDetail" :key="item.id" style="margin-bottom: 10px">
                 <span>
                  {{ item.fieldName }}
                  </span>
              <div style="margin-top: 5px"></div>
              <div style="min-height:30px;border: solid 1px #f7f7f9;height: auto;background-color: #f7f7f7;white-space:pre-wrap;" v-html="taskDetail[i].fieldValue"></div>
              <!--<k-field-text data-disabled data-clearable="false" v-model="taskDetail[i].fieldValue" input-type="textarea"/>-->
            </div>
          </div>
          <!--        页面字段-->
          <div  v-else-if="formType==='1'">
            <div v-for="(item,i) in taskDetail" :key="item.id" style="margin-bottom: 10px">
                 <span>
                  {{ item.fieldName }}
                  </span>
              <div style="margin-top: 5px"></div>
              <k-field-text data-disabled data-clearable="false" v-model="taskDetail[i].fieldValue" input-type="textarea"/>
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
      </div>
      <div style="text-align: center;margin-top: 40px">
        <k-btn class="btn-custom-plain" data-functype="CLOSE">
          <md-icon md-src="/static/svg/cancel.svg"></md-icon>关闭</k-btn>
      </div>
    </k-popup>
    <!--  任务详情弹框END  -->

    <!--  审批单下载  -->
    <k-popup ref="detailPopup2" data-title="审批单详情" data-width="60%" v-show="false">
      <h4>审批任务名称：{{formData.displayName}}</h4>
      <h4>目标处理日期：{{formData.processDeadline}}</h4>
      <div>
        <!--   产品信息START     -->
        <!--   自定义字段形式     -->
        <div  v-if="formType==='3'">
          <div v-for="(item,i) in taskDetail" :key="item.id" style="margin-bottom: 10px">
                 <span>
                  {{ item.fieldName }}
                  </span>
            <div style="margin-top: 5px"></div>
            <div style="min-height:30px;border: solid 1px #f7f7f9;height: auto;background-color: #f7f7f7;white-space:pre-wrap;" v-html="taskDetail[i].fieldValue"></div>
            <!--  <k-field-text data-disabled data-clearable="false" v-model="taskDetail[i].fieldValue" input-type="textarea"/> -->
          </div>
        </div>
        <!--        页面字段-->
        <div  v-else-if="formType==='1'">
          <div v-for="(item,i) in taskDetail" :key="item.id" style="margin-bottom: 10px">
                 <span>
                  {{ item.fieldName }}
                  </span>
            <div style="margin-top: 5px"></div>
            <k-field-text data-disabled data-clearable="false" v-model="taskDetail[i].fieldValue" input-type="textarea"/>
          </div>
        </div>
        <div v-else>
          <component v-bind:is="loadUrl" v-bind:dataData="formData"></component>
        </div>
        <!--  附件信息列表START      -->
        <div>
          <h4>附件信息</h4>
          <k-grid
                  ref="grid"
                  :data-display="false"
                  data-url='wf/wf/attachment/getAttachmentList.json'
                  :data-params="{'processId':this.formData.id}">
            <k-grid-column data-align="center"
                           data-header="id"
                           data-name="id"
                           data-hidden="true"
            ></k-grid-column>
            <k-grid-column data-align="center"
                           data-header="附件类型"
                           data-name="fileType"
                           data-dict="attachment_type"
            ></k-grid-column>
            <k-grid-column data-align="center"
                           data-header="附件名称"
                           data-name="originalFilename"
            ></k-grid-column>
            <k-grid-column data-align="center"
                           data-header="上传人"
                           data-name="username"
            ></k-grid-column>
            <k-grid-column data-align="center"
                           data-header="上传时间"
                           data-name="createDate"
            ></k-grid-column>
          </k-grid>
        </div>
        <!--  附件信息END      -->
        <!--   审批流程START     -->
        <k-flow-show v-bind:flowDataList="flowDataList"/>
        <!--   审批流程END     -->
      </div>
      <div style="text-align: center">
        <k-btn class="btn-custom-plain" data-functype="CLOSE">
          <md-icon md-src="/static/svg/cancel.svg"></md-icon>关闭</k-btn>
      </div>
    </k-popup>

    <!--  在线浏览    -->
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
  import OpFlowDetail from "@/pages/operation/flow/OpFlowDetail";
  import ChangxieOffice from "@/pages/pms/basePublish/changxieOffice";
  import VabOnlyOffice from "@/pages/flow/onlyOfficeView";

  export default {
    name: "processTracking",
    components: {
      ChangxieOffice,
      OpFlowDetail, FlowProcessInstanceListDialog,KFlowShow,VabOnlyOffice},
    data() {
      return {
        formData: {},
        selectRowData: {},
        queryParamApplyUser: '',
        queryParamTaskName: '',
        queryParamStatus:'',
        flowDataList:[{}],
        taskDetail:[{}],
        queryParamDateRange:[],
        userid:localStorage.getItem("userid"),
        pageUrl:'',
        formType:'',
        attachmentList:[{}],
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
        imageUrl:'',
        opFlowStatus:false,
        opProcessInstance:'',
      };
    },
    computed: {
      queryParam() {
        return {
          'processNameLike': this.queryParamTaskName,
          'processStatus': this.queryParamStatus,
          'applyUser': this.queryParamApplyUser,
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
    created(){

    },
    methods: {
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
      //获取详情数据
      getDetailList(row){
        let processId = row.id;
        console.log('po===>',processId)
        this.getApprovalNodeList(processId);
        this.findFormInfo(processId);
        this.getAttachmentList(processId)
        this.pageUrl = row.formUrl;
        this.$set(this.formData,"processId",this.formData.id)
        console.log('pos===>',this.formData.processId)
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
        console.log('流程节点参数',processId)
        this.httpUtil
                .ajax({
                  url: "/wf/approvalTask/listAllApprovalTasks.json",
                  params: {
                    processId: processId
                  }
                })
                .then(res => {
                  this.flowDataList = res.data;
                  this.$set(this.formData,"disabled",'true')
                });
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
      processFinishButBusiError(row) {
        return (row.processStatus === this.global.wf_process_status.finish &&
                row.busStatus === this.global.wf_business_status.error) || (row.processStatus === this.global.wf_process_status.finish &&
                row.busStatus === this.global.wf_business_status.processing);
      },
      renderCreateDateTime(row) {
        return Tools.formatDateTime(row.startDate, row.startTime);
      },
      renderUpdateDateTime(row) {
        let date = Tools.formatDateTime(row.updateDate, row.updateTime);
        if (date == "") {
          return '-'
        }
        return date;
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
      //判断用户是否具有撤销
      isShowButton(){
        if (this.userid=='admin'){
          return true;
        }
        return false;
      },
      isDisable(row){
        if (row.processStatus=='审批中'){
          return false;
        }else {
          return true;
        }
      },
      revokeBusinessProcess(val){
        val.isCancle="1";//流程追踪默认可以撤销流程
        Tools.confirm(()=>{
         this.httpUtil.ajax({
            url: "/wf/businessProcess/revokeBusinessProcess.json",
            params: val
          }).then(res => {
            if (res.status == '200') {
              Tools.alert(res.data);
              this.$refs.grid.load();
              return true;
            } else return false;
          });
        },"请确认是否撤销该审批流？")
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
</style>
