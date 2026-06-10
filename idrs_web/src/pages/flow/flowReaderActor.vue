<template>
    <div>
        <!-- <k-popup ref="detailPopup" data-title="详情" data-width="60%"> -->
            <el-button type="danger"
                       @click="ExportSavePdf(formData.displayName,nowTime)">下载审批单</el-button>
            <div id="pdfCentent">
                <h4>审批任务名称：{{formData.displayName}}</h4>
                <h4>目标处理日期：{{formData.processDeadline}}</h4>
                <div>
                    <!--   产品信息START     -->
                    <!--   自定义字段形式     -->
                    <div  v-if="formType==='3'">
                        <div v-for="(item,i) in taskDetail" :key="item.i" style="margin-bottom: 10px">
                   <span style="font-size: 16px">
                    {{ item.fieldName }}
                    </span>
                            <div style="margin-top: 5px"></div>
<!--                            <k-field-text data-disabled data-clearable="false" v-model="taskDetail[i].fieldValue" input-type="textarea"/>-->
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

                </div>
            </div>
      <div>
        <!--  评论列表      -->
        <flow-comment  v-bind:commentParams="commentParams"></flow-comment>
      </div>
      <div style="text-align: center;margin-top: 40px">
        <k-btn
          class="btn-custom-primary"
          data-functype="SUBMIT"
          @click="confirmSend"
          data-target="detailPopup"
          ref="submitBtn"
          v-if="showBtn">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确认
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE" v-if="!showBtn">
          <md-icon md-src="/static/svg/cancel.svg"></md-icon>关闭</k-btn>
      </div>
<!--        </k-popup>-->
    </div>
</template>

<script>
    import {assign} from "lodash";
    import Tools from "@/utils/tools.js";
    import KFlowShow from "../../components/k-flow/k-flow-show/k-flow-show";
    import ppp from "../../pages/flow/dividendsTaskApproval"
    import FlowComment from "@/pages/flow/flowComment";

    export default {
        name: "flowReaderActor",
        components: {KFlowShow,FlowComment},
        props: {
            paramData:{},
        },
        data() {
            return {
                formData: {},
                flowDataList: [{}],
                taskDetail: [{}],
                isShow: '',
                pageUrl: '',
                formType: '',
                nowTime: '',
                showBtn:false,
                attachmentList:[{
                }],
                commentParams:[],
            };
        },
        computed: {
            loadUrl(){
                const self = this;
                let url = self.pageUrl;
                console.log('url',url)
                return function (resolve) {
                    require(['@/pages/'+url+'.vue'], resolve)
                    //require(['@/pages/test/test-text.vue'], resolve)
                };
            }
        },
        created() {
            this.formData.id = this.paramData.processId;
            this.processId = this.paramData.processId;
            this.formData.displayName = this.paramData.displayName;
            this.formData.processDeadline = this.paramData.processDeadline;
            this.pageUrl = this.paramData.url;
            if (this.paramData.flag == '0')
                this.showBtn = true;
            this.getApprovalNodeList(this.processId);
            this.findFormInfo(this.processId);
            this.getAttachmentList(this.processId);
          //获取评论数据
          this.commentParams[0] =this.paramData.processId;
          this.commentParams[1] = this.paramData.displayName;
          console.log('代办抄送页面：commentParams',this.commentParams)
        },
        methods:{
          typeFormatter(row,column){
            return row.fileType === '1' ? "经办附件" : row.fileType === '2' ? "业务附件" :row.fileType === '3' ? "审批附件" : row.fileType;
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
              }).then(res => {
              this.$nextTick(()=>{
                this.attachmentList = res.rows;
              })
            });
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
                        if (res.status=='200'){
                            this.dealConfirm();
                        }
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
                    if (this.taskDetail.length !=0){
                        this.formType = this.taskDetail[0].formType;
                    }else {
                        this.formType = '2';
                    }
                })
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
            confirmSend(){
                let id = this.formData.id;
                this.httpUtil.ajax({
                    url: "/wf/readonlyActor/readonlyActorConfirm.json",
                    params: {processId : id},
                }).then(res => {
                    if (res.status=='200'){
                        this.$message.success('确认成功');
                        this.isShow = false;
                        this.getApprovalNodeList(this.formData.id);
                        this.$emit('submitClose', '1')
                    }
                })
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

        },


    }
</script>

<style scoped>
.text_style{
  padding: 10px;
  border-radius: 5px;
  margin-top: 10px;
  border: 1px solid #d7dae2;
  /*text-indent: 2em*/
}
</style>
