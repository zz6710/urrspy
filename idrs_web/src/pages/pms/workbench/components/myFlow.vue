<template>
  <div class="myFlow tab-page">
    <div class="myFlow_block01">
      <div class="myFlow_block01_line1">
        <div>处理状态:</div>
        <div class="tagBlock" :class="{ 'tagBlock-active': isActive1 === item.id }" v-for="item in flowStatus"
          :key="item.id" @click="changeTab(1, item.id)" >
<!--          <div class="count" v-if="item.showSpot">
            <span>{{ item.num }}</span>
          </div>-->
          {{ item.value }}
        </div>
      </div>
      <div class="myFlow_block01_line1" >
        <span style="color: #F56C6C">*</span>
        <div>数据日期:</div>
        <div style="width: 15%;margin-left: 10px">
<!--          <k-field-date v-model="searchParam.dealDate" data-value-format="yyyyMMdd"  :data-default-value="currentDate" @data-on-change="changeTab()" ></k-field-date>-->
          <k-field-date v-model="BreathDay" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"  @data-on-change="changeTab()"></k-field-date>
        </div>
      </div>
    </div>
    <!-- <div class="myFlow_block02"> -->
      <k-grid ref="T8ProdAccountFindInfo" data-action="DeskTopModel.findPortDeskTopInformation"
        @data-row-select="selectRow" data-operate-width="200px" :data-autoload="false">
        <!-- :data-operate-column="false" -->
        <k-grid-column data-header="序号" data-name="id" :data-hidden="true" />
        <k-grid-column data-align="center" data-header="数据日期" data-name="dealDate"></k-grid-column>
        <k-grid-column data-align="center" data-header="接口类型" data-name="portType" data-dict="interface_type"></k-grid-column>
        <k-grid-column data-align="center" data-header="接口名称" data-name="portName"></k-grid-column>
        <k-grid-column data-align="center" data-header="处理状态" data-name="fileState" data-dict="port_info_status"></k-grid-column>
        <k-grid-column data-align="center" data-header="数据量"  data-name="totalNum"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-plain" data-descript="查看详情" data-functype="popup" data-size="mini"
            :data-model="scope.row.row" :data-handler="openPortLogDetail"  v-if="isbtnShow">
            查看详情
          </k-btn>
        </template>
      </k-grid>
    <!-- </div> -->
  </div>
</template>

<script>
  import {
    assign
  } from "lodash";
  import Tools from "@/utils/tools";

  export default {
    watch: {
      // 查询导入日期
      BreathDay() {
        console.log(this.BreathDay);
        this.$set(this.searchParam, 'theoryReportStartDate', this.BreathDay == null ? '' : this.BreathDay[0]);
        this.$set(this.searchParam, 'theoryReportEndDate', this.BreathDay == null ? '' : this.BreathDay[1]);
      },
    },
    data() {
      return {
        BreathDay: [],
        isActive1: null,
        isActive2: null,
        selectRowData: {},
        taskData: {},
        pageUrl:"",
        processData: {},
        taskDetail: [{}],
        attachmentList: [],
        flowDataList: [],
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
        formType: "",
        flowStatus: [
          {
            id: -1,
            value: "不限",
          },
          {
            id: 0,
            value: "待执行",
            num: 0,
            showSpot: true,
          },
          {
            id: 1,
            value: "处理中",
            num: 0,
            showSpot: false,
          },
          {
            id: 2,
            value: "成功",
            num: 0,
            showSpot: false,
          },
          {
            id: 3,
            value: "失败",
            num: 0,
            showSpot: true,
          },
        ],
        flowClassification: [{
            id: "00",
            value: "不限",
          },
          {
            id: "01",
            value: "固收产品",
          },
          {
            id: "02",
            value: "权益产品",
          },
          {
            id: "03",
            value: "商品及衍生品",
          },
          {
            id: "04",
            value: "混合类的产品",
          },
          {
            id: "05",
            value: "无关产品",
          },
        ],
        userid: localStorage.userid,
        isShow: false,
        commentParams:[],
        searchParam:{},
        currentDate:'',//定义当前日期回显使用
        isbtnShow:true,
      };
    },

    created() {
      this.isActive1 = this.flowStatus[0].id;
      //this.loadCount();
      this.httpUtil.sysDate().then(res => {
        if (res) {
          this.$set(this.searchParam, 'dealDate', res.toString());
        }
      })
      this.$nextTick(() => {
        this.changeTab(1,-1);
      });
    },
    activated() {
      this.isActive1 = this.flowStatus[0].id;
      //this.loadCount();
      this.$nextTick(() => {
        this.changeTab(1,-1);
      });
    },

    computed: {
      loadUrl(){
        const self = this;
        let url = self.pageUrl;
        console.log('url',url)
        return function (resolve) {
          require(['@/pages/'+url+'.vue'], resolve)
        };
      }
    },

    methods: {
      //跳转到接口日志详情
      openPortLogDetail(value){
        let pathUrl = '/main/pms/basePublish/t8PortLogManage';
        this.$router.push({
          path: pathUrl,
          query: value,
        });
        console.log("value=:>>>>",value);
      },
      popupClose1() {
        this.$refs.sharePopup.close();
      },
      confirmSend(){
        let id = this.selectRowData.processId;
        this.httpUtil.ajax({
          url: "/wf/readonlyActor/readonlyActorConfirm.json",
          params: {processId : id},
        }).then(res => {
          if (res.status=='200'){
            this.$message.success('确认成功')
            this.isShow = false;
          }
        })
      },
      dealConfirm(){
        console.log('this.flowDataList',this.flowDataList)
        let list = this.flowDataList;
        let x;
        if(list[1].nodeLevel=='999' || list.length == 1)
          return this.isShow = false;
        for (x in list){
          let userName = localStorage.getItem('username');
          if (list[x].nodeLevel=='999'&&list[x].result=='未确认'&&list[x].operator==userName){
            this.isShow = true;
            return this.isShow;
          }
        }
        this.isShow = false;
        return this.isShow;
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

      typeFormatter(row,column){
        console.log('row',row.fileType)
        return row.fileType === '1' ? "经办附件" : row.fileType === '2' ? "业务附件" :row.fileType === '3' ? "审批附件" : row.fileType;
      },
      download(value) {
        let fileName = value.originalFilename;
        this.httpUtil.download({
          url: "download/server/WorkflowServer/wf/attachment/downloadFile.json",
          params: value,
          callback: response => {}
        }, fileName)
        return false
      },
      loadCount() {
        // 待审批
        this.httpUtil.comnQuery({
          action: 'DirecBbusinessProcess.findDirecBbusinessProcessNum',
          params: {
            approvalStatus: "0",
            result: '2',
            operator: localStorage.userid,
            start: 0,
            limit: 10
          }
        }).then(data => {
          this.flowStatus[1].num = data.results
        });

        // 已发起
        this.httpUtil.comnQuery({
          action: 'DirecBbusinessProcess.findDirecBbusinessProcessNum',
          params: {
            // processStatus: 1,
            result: '0',
            creator: localStorage.userid,
            start: 0,
            limit: 10
          }
        }).then(data => {
          console.log("已发起",data.results);
          this.flowStatus[2].num = data.results
        });

        // 已审批
        this.httpUtil.comnQuery({
          action: 'DirecBbusinessProcess.findDirecBbusinessProcessNum',
          params: {
            result : '3,4,5,6,7',
            processStatus : '2',
            operator : localStorage.userid,
            start: 0,
            limit: 10
          }
        }).then(data => {
          this.flowStatus[3].num = data.results
        });

        // 抄送我
        this.httpUtil.comnQuery({
          action: 'DirecBbusinessProcess.findDirecBbusinessProcessNum',
          params: {
            isPush: 1,
            readonlyActor: localStorage.userid,
            start: 0,
            limit: 10
          }
        }).then(data => {
          this.flowStatus[4].num = data.results
        });

        // 分享我
        this.httpUtil.comnQuery({
          action: 'DirecBbusinessProcess.findDirecBbusinessProcessNum',
          params: {
            sharedUserId: localStorage.userid,
            start: 0,
            limit: 10
          }
        }).then(data => {
          this.flowStatus[5].num = data.results
        });
      },
      popupClose() {
        this.loadCount();
        this.$refs.editPopup.close();
        let params = {};
        params.processType = 2;
        params.result = "2";
        params.approvalStatus = "0";
        params.operator = localStorage.userid;

        if (this.isActive2 != "00") {
          params.prodClassify = this.isActive2; //我发起
        }

        this.$refs.T8ProdAccountFindInfo.load(params);
        this.refreshMyDesktop();
      },
      changeTab(flag, id) {
        if (this.searchParam.dealDate === null ){
          Tools.alert("请选择数据日期！","danger");
          return false;
        }
        flag == 1 ? (this.isActive1 = id) : (this.isActive2 = id);
        let params = {};
        let dealDate ='';
        if (this.isActive1 == 0) { // 待执行
          params.fileState = "00";
          this.isbtnShow = false;
        } else if (this.isActive1 == 1) { //处理中
          params.fileState = "01";
          this.isbtnShow = true;
        } else if (this.isActive1 == 2) { //成功
          params.fileState = '03';
          this.isbtnShow = true;
        } else if (this.isActive1 == 3) {//失败
          params.fileState = "02";
          this.isbtnShow = true;
        }else{
          this.isbtnShow = true;
        }
        // this.httpUtil.sysDate().then(res => {
        //   if (res) {
        //     if (this.searchParam.dealDate === undefined) {
        //       dealDate = res.toString();
        //     } else {
        //       dealDate = this.searchParam.dealDate;
        //     }
        //     params.dealDate = dealDate;
        //     this.$refs.T8ProdAccountFindInfo.load(params);
        //   }
        // })
        if (this.searchParam.theoryReportStartDate!==undefined && this.searchParam.theoryReportStartDate!=='' && this.searchParam.theoryReportStartDate!=null) {
          params.theoryReportStartDate = this.searchParam.theoryReportStartDate;
        }
        if (this.searchParam.theoryReportEndDate!==undefined && this.searchParam.theoryReportEndDate!=='' && this.searchParam.theoryReportEndDate!=null) {
          params.theoryReportEndDate = this.searchParam.theoryReportEndDate;
        }
        this.$refs.T8ProdAccountFindInfo.load(params);
      },
      selectRow(row, column, event) {
        this.selectRowData = assign({}, row);
      },
      openFlowPage() {
        setTimeout(() => {
          this.httpUtil.comnQuery({
            action: 'DirecBbusinessProcess.findDirecBbusinessTask',
            params: {
              operator: localStorage.userid,
              processId: this.selectRowData.processId
            }
          }).then(data => {
            this.$set(this.taskData,'applyUser',data.rows[0].applyUser);
            this.$set(this.taskData,'approvalTaskId',data.rows[0].approvalTaskId);
            this.$set(this.taskData,'processCreateTime',data.rows[0].processCreateTime);
            this.$set(this.taskData,'processDisplayName',data.rows[0].processDisplayName);
            this.$set(this.taskData,'processId',data.rows[0].processId);
            this.$set(this.taskData,'taskDisplayName',data.rows[0].taskDisplayName);
            this.$set(this.taskData,'taskFormUrl',data.rows[0].taskFormUrl);
            this.$refs.editPopup.popup();
          });
        }, 300);
        return false;
      },
      openFlowPage2() {
        setTimeout(() => {
          this.httpUtil.comnQuery({
            action: 'DirecBbusinessProcess.findDirecBbusinessFinish',
            params: {
              processId: this.selectRowData.processId
            }
          }).then(data => {
            this.processData.createTime = data.rows[0].createTime;
            this.processData.creator = data.rows[0].creator;
            this.processData.displayName = data.rows[0].displayName;
            this.processData.finishDate = data.rows[0].finishDate;
            this.processData.formId = data.rows[0].formId;
            this.processData.id = data.rows[0].id;
            this.pageUrl = data.rows[0].formUrl;
            this.processData.processStatus = data.rows[0].processStatus;
            this.processData.revocable = data.rows[0].revocable;
            this.$refs.detailPopup.popup();
            this.getApprovalNodeList(this.selectRowData.processId);
            this.findFormInfo(this.selectRowData.processId);
            this.getAttachmentList(this.selectRowData.processId);
            this.commentParams[0] = data.rows[0].id;
            this.commentParams[1] = data.rows[0].displayName;
          });
        }, 300);


        return false;
      },

      getApprovalNodeList(processId) {
        console.log('流程节点参数', processId)
        this.httpUtil
          .ajax({
            url: "/wf/approvalTask/listAllApprovalTasks.json",
            params: {
              processId: processId
            }
          }).then(res => {
            this.flowDataList = res.data;
            // this.$set(this.formData, "disabled", 'true')
          console.log('this.isActive1',this.isActive1)
          if (this.isActive1 == 3){
            this.dealConfirm();
          }
          });
      },
      //自定义表单数据
      findFormInfo(processId) {
        console.log('表单数据参数', processId)
        this.httpUtil.ajax({
          url: "/wf/formInfo/getFormInfo.json",
          params: {
            processId: processId
          },
        }).then(res => {
          console.log('表单数据', res)
          this.taskDetail = res.data;
          if (this.taskDetail.length != 0) {
            this.formType = this.taskDetail[0].formType;
          } else {
            this.formType = '2';
          }
        })
      },
      getAttachmentList(val) {
        let processId = val;
        this.httpUtil
          .ajax({
            url: "wf/wf/attachment/getAttachmentList.json",
            params: {
              processId: processId
            },
            successAlert: false,
          }).then(res => {
            this.$nextTick(() => {
              this.attachmentList = res.rows;
            })
          });
      },
      //首页待审批刷新
      refreshMyDesktop(){
        this.$emit('refreshDesktop', '1')
      },
    },
  };
</script>

<style lang="scss" scoped>
  .myFlow_block01_line1 {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    flex-wrap: wrap;
    margin: 15px 0;
  }

  .spot {
    position: absolute;
    width: 14px;
    height: 14px;
    border-radius: 50%;
    top: 1px;
    background: #ff9e00;
    font-size: 12px;
    color: #fff;
    right: 1px;
    font-size: 12px;
    line-height: 14px;
  }

  .row_drop {
    .el-icon-d-arrow-right {
      font-size: 20px;
      transform: rotate(90deg);
    }
  }

  .myFlow_block01 {
    margin-bottom: 15px;
  }

  .myFlow_block02_state {
    display: flex;
    justify-content: flex-start;
    align-items: center;
  }

  .dorp {
    // 已完成
    width: 8px;
    height: 8px;
    background: #666;
    border-radius: 50%;
    margin-right: 10px;
  }

  .dorpColor0 {
    // 我发起
    background: #00bcd4;
  }

  .dorpColor1 {
    // 代办
    background: #e53929;
  }

  .dorpColor2 {
    // 处理中
    background: #4caf50;
  }
  .count {
		position: absolute;
		top: 1px;
		right: 1px;
		background: #ff9e00;
		border-radius: 10px;
		color: #fff;
		padding: 0 4px;
		height: 14px;
		line-height: 14px;
		text-align: center;
    font-weight: normal;
		font-size: 12px;
	}
</style>
