<template>
  <div style="height: 80vh;">
    <div style="width: 45%; height: 50%; float: left; margin-left: 30px;margin-top: 30px;background-color: white;
        border-radius:5px">
      <div style="margin-top: 30px;margin-left: 20px;">
        <img src="../assets/desktop/zy15-8.png"/>
        <font size="4" style="font-weight: 500;">数据接入情况一览</font>
      </div>
      <div style="margin-left: 8%;height: 70%;width: 85%;margin-top: 10px;">

        <el-table
          border
          :data="processTableData"
          style="width: 100%;border-radius:3px;overflow: scroll;" height="90%">

          <el-table-column
            label="操作"
            width="90">
            <template slot-scope="scope">
              <el-button style="color: #fff;background-color: #409eff;border-color: #409eff;" size="mini"
                         @click="processCheckPage(scope.row)">查看
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            prop="interfaceType"
            label="接口类型"
            min-width="15%">
          </el-table-column>
          <el-table-column
            prop="interfaceName"
            label="接口名称"
            min-width="20%">
          </el-table-column>
          <el-table-column
            prop="dataDate"
            label="数据日期"
            min-width="20%">
          </el-table-column>
          <el-table-column
            prop="processingStatus"
            label="处理状态"
            min-width="20%">
          </el-table-column>
          <el-table-column
            prop="processingNum"
            label="处理条数"
            min-width="15%">
          </el-table-column>
        </el-table>



      </div>
    </div>

<!--    <div style="width: 45%; height: 50%; float: left; margin-left: 30px;margin-top: 30px;background-color: white;
        border-radius:5px">
      <div style="margin-top: 30px;margin-left: 20px;">
        <img src="../assets/desktop/zy15-8.png"/>
        <font size="4" style="font-weight: 500;">信息披露情况一览</font>
      </div>
      <div style="margin-left: 8%;height: 70%;width: 85%;margin-top: 10px;">
        <el-table
          border
          :data="tableData"
          style="width: 100%;border-radius:3px;"
          height="90%">

          <el-table-column
            label="操作"
            width="100">
            <template slot-scope="scope">
              <el-button style="color: #fff;background-color: #409eff;border-color: #409eff;" size="mini"
                         @click="workFlowCheckPage(scope.row)">查看
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            prop="disclosureTypeDesc"
            label="信披类型"
            width="90">
          </el-table-column>
          <el-table-column
            prop="disclosureSonType"
            label="信披子类型"
            width="150">
          </el-table-column>
          <el-table-column
            prop="AnnounceState"
            label="公告状态"
            width="150">
          </el-table-column>
          <el-table-column
            prop="stateNum"
            label="数量"
            width="100">
          </el-table-column>

        </el-table>

      </div>
    </div>-->

    <div style="width: 45%; height: 50%; float: left; margin-left: 30px;margin-top: 30px;background-color: white;
        border-radius:5px">
      <div style="margin-top: 30px;margin-left: 20px;">
        <img src="../assets/desktop/zy15-8.png"/>
        <font size="4" style="font-weight: 500;">监管报送情况一览</font>
      </div>
      <div style="margin-left: 8%;height: 70%;width: 85%;margin-top: 10px;">
        <el-table
          border
          :data="homeTaskTableData"
          style="width: 100%;border-radius:3px;"
          height="90%">

          <el-table-column
            label="操作"
            width="100">
            <template slot-scope="scope">
              <el-button style="color: #fff;background-color: #409eff;border-color: #409eff;" size="mini"
                         @click="homeTaskCheckPage(scope.row)">查看
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            prop="statementCategory"
            label="报表大类"
            width="100">
          </el-table-column>
          <el-table-column
            prop="statementName"
            label="报表名称"
            width="100">
          </el-table-column>
          <el-table-column
            prop="dataDate"
            label="数据日期"
            min-width="100">
          </el-table-column>
          <el-table-column
            prop="submitDeadline"
            label="理论报送截止日"
            min-width="120">
          </el-table-column>
          <el-table-column
            prop="total"
            label="总数量"
            min-width="60">
          </el-table-column>
          <el-table-column
            prop="toSubmitNum"
            label="待报送数量"
            min-width="100">
          </el-table-column>
          <el-table-column
            prop="globalState"
            label="整体状态"
            min-width="80">
          </el-table-column>

        </el-table>
      </div>
    </div>
    <div style="width: 45%; height: 50%; float: left; margin-left: 30px;margin-top: 30px;background-color: white;
        border-radius:5px">
      <div style="margin-top: 30px;margin-left: 20px;">
        <img src="../assets/desktop/zy15-8.png"/>
        <font size="4" style="font-weight: 500;">数据质量检查情况一览</font>
      </div>
      <div style="margin-left: 8%;height: 70%;width: 85%;margin-top: 10px;">
        <el-table
          border
          :data="disclosureTableData"
          style="width: 100%;border-radius:3px;"
          height="90%">

          <el-table-column
            label="操作"
            width="100">
            <template slot-scope="scope">
              <el-button style="color: #fff;background-color: #409eff;border-color: #409eff;" size="mini"
                         @click="homeTaskCheckPage(scope.row)">查看
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            prop="statementCategory"
            label="报表大类"
            width="100">
          </el-table-column>
          <el-table-column
            prop="statementName"
            label="报表名称"
            width="100">
          </el-table-column>
          <el-table-column
            prop="checkResult"
            label="校验结果"
            width="90">
          </el-table-column>
          <el-table-column
            prop="checkNum"
            label="数量"
            width="90">
          </el-table-column>
          <el-table-column
            prop="addRecordPage"
            label="补录页面"
            width="150">
          </el-table-column>
          <el-table-column
            prop="Num"
            label="数量"
            width="60">
          </el-table-column>
        </el-table>
      </div>
    </div>

    <div style="width: 45%; height: 50%; float: left; margin-left: 30px;margin-top: 30px;background-color: white;
        border-radius:5px">
      <div style="margin-top: 30px;margin-left: 20px;">
        <img src="../assets/desktop/zy15-8.png"/>
        <font size="4" style="font-weight: 500;">流程情况一览</font>
      </div>
      <div style="margin-left: 8%;height: 70%;width: 85%;margin-top: 10px;">
        <el-tabs style="width: 100%;border-radius:3px;height: 100%;">
          <el-tab-pane label="待审批" style="width: 100%;height: 100%;" >
            <el-table
              border
              :data="processTableData"
              style="width: 100%;border-radius:3px;height: 90%;">
              <el-table-column
                prop="procDefName"
                label="流程名"
                width="100">
              </el-table-column>
              <el-table-column
                prop="status"
                label="流程状态"
                width="100"
              >
              </el-table-column>
              <el-table-column
                prop="taskName"
                label="当前节点"
                width="100">
              </el-table-column>
              <el-table-column
                prop="startUserName"
                label="发起人"
                width="100">
              </el-table-column>
              <el-table-column
                prop="procStartTime"
                label="发起时间"
                width="90">
              </el-table-column>
              <el-table-column
                label="操作"
                width="100">
                <template slot-scope="scope">
                  <el-button style="color: #fff;background-color: #409eff;border-color: #409eff;" size="mini"
                             @click="taskCheckPage(scope.row)">查看
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>

          <el-tab-pane label="已发起" style="width: 100%;height: 100%;" >
            <el-table
              border
              :data="myProcessData"
              style="width: 100%;border-radius:3px;height: 90%;">
              <el-table-column
                prop="procDefName"
                label="流程名"
                width="100">
              </el-table-column>
              <el-table-column
                prop="status"
                label="流程状态"
                width="100">
              </el-table-column>
              <el-table-column
                prop="taskName"
                label="流程节点"
                width="100">
              </el-table-column>
              <el-table-column
                prop="startUserName"
                label="发起人"
                width="100">
              </el-table-column>
              <el-table-column
                prop="procStartTime"
                label="发起时间"
                width="90">
              </el-table-column>
              <el-table-column
                label="操作"
                width="100">
                <template slot-scope="scope">
                  <el-button style="color: #fff;background-color: #409eff;border-color: #409eff;" size="mini"
                             @click="myTaskCheckPage(scope.row)">查看
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>

    <k-popup ref="editPopup" class="editPopupClass" data-width="80%" :data-title="processRowData.interfaceName">
      <div style="overflow: auto">
        <FlowActiveTaskDialog :task-info="processRowData" @submitClose="popupClose"/>
      </div>
    </k-popup>

    <k-popup ref="detailPopup" class="editPopupClass" :data-title="processDetailRowData.interfaceName"
             data-width="80%">
      <div style="overflow: auto">
        <FlowProcessInstanceListDialog :task-info="processDetailRowData"/>
      </div>
    </k-popup>

    <k-popup ref="commonlyUsedPopup" data-title="增加常用功能">
      <k-form ref="commonlyUsedForm" :data-col="1">
        <k-form-item label="常用功能:">
          <k-field-select v-model="commonlyUsedForm.menuid" data-action="DesktopIndex.findCommonlyUsedMenu"
                          data-value-field="menuid" data-display-field="menuname"/>
        </k-form-item>
      </k-form>
      <div style="text-align: right;">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-handler="saveCommonlyUsed"><md-icon md-src="/static/svg/confirm.svg"></md-icon>保存
        </k-btn>
      </div>
    </k-popup>


    <k-popup ref="disclosureShowMorePopup" class="disclosureClass" data-title="信披类型更多信息" data-width="60%">
      <k-grid ref="disclosureShowMoreGrid"
              data-action="DisclosureOperation.findDesktopDisclosureOperationsDetail"
              :data-params="{operationType:disclosureParam.operationType,disclosureType:disclosureParam.disclosureType}">
        <k-grid-column data-header="产品代码" data-name="prodCode"></k-grid-column>
        <k-grid-column data-header="产品名称" data-name="prodName"></k-grid-column>
        <k-grid-column data-header="信披类型" data-name="disclosureType" data-dict="xp_doc_type"></k-grid-column>
        <k-grid-column data-header="信披子类型" data-name="disclosureSonType" data-dict="xp_son_type"></k-grid-column>
        <k-grid-column data-header="待办类型" data-name="operationType" data-dict="t8_operation_type"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="查看详情" data-functype="SUBMIT" data-size="mini"
                 :data-model="scope.row.row" :data-handler="openPage">
            <md-icon>open_in_new</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </k-popup>

    <k-popup ref="disclosureNetValShowMorePopup" class="disclosureClass" data-title="信披类型更多信息" data-width="60%">
      <k-grid ref="disclosureShowMoreGrid"
              data-action="DisclosureOperation.findDesktopDisclosureOperationsDetail"
              :data-params="{operationType:disclosureParam.operationType,disclosureType:disclosureParam.disclosureType}">
        <k-grid-column data-header="信披类型" data-name="disclosureType" data-dict="xp_doc_type"></k-grid-column>
        <k-grid-column data-header="信披日期" data-name="taskDate"></k-grid-column>
        <k-grid-column data-header="待办类型" data-name="operationType" data-dict="t8_operation_type"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="查看详情" data-functype="SUBMIT" data-size="mini"
                 :data-model="scope.row.row" :data-handler="openPage">
            <md-icon>open_in_new</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </k-popup>

    <k-popup ref="quarterReportApprovalPopup" class="disclosureClass" data-title="定期报告信披模板复核待办"
             data-width="60%">
      <k-grid ref="quarterReportApprovalGrid"
              data-action="DisclosureMod.findNeedApprovalMods">
        <k-grid-column data-header="信披类型" data-name="disclosureType" data-dict="xp_doc_type"></k-grid-column>
        <k-grid-column data-header="信披子类型" data-name="disclosureSonType" data-dict="xp_son_type"></k-grid-column>
        <k-grid-column data-header="模板名称" data-name="modName"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="查看详情" data-functype="SUBMIT" data-size="mini"
                 :data-model="scope.row.row" :data-handler="openPage">
            <md-icon>open_in_new</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </k-popup>

  </div>
</template>


<script>
import FlowProcessInstanceListDialog from './flow/FlowProcessInstanceListDialog'
import FlowActiveTaskDialog from './flow/flowActiveTaskDialog'
import Tools from "@/utils/tools";
import {assign} from "lodash";

export default {
  name: "desktop",
  components: {FlowActiveTaskDialog, FlowProcessInstanceListDialog},
  props: ['param'],
  data: function () {
    return {
      start: "",
      end: "",
      formData: {
        roleid: ""
      },
      commonlyUsedForm: {
        menuid: ''
      },
      commonlyUsedMenu: [],
      password2: '234',
      checkBoxValue: '选项1,选项2',
      mdRippleActive: null,
      dialogVisible: true,
      checkBoxValue5: "选项1,选项2",
      visible: false,
      extraData: {},
      fileList: [],
      fileData: '',
      processRowData: {},
      homeTaskTableData: [], // 监管报送情况一览
      homeTaskRowData: {},
      disclosureTableData: [], // 数据质量检查情况一览
      flowTableData: [], // 流程情况一览
      disclosureParam: {
        operationType: '',
        disclosureType: '',
      },

      processTableData: [],// 待审批的流程数据（流程情况一览）
      myProcessData: [],// 我发起的流程（流程情况一览）
      tableData: [],// 工作流数据（信息披露情况一览）
      processDetailRowData: {},
    }
  },
  methods: {
    popupClose() {
      this.$refs.editPopup.close();

    },
    //打开常用功能菜单
    openMenu(url) {
      this.$router.push({
        path: url
      });
    },
    //加载用户常用功能
    loadCommonlyUsedMenu() {
      this.httpUtil.comnQuery({
        action: "DesktopIndex.findCommonlyUsedMenuByuser"
      }).then(data => {
        this.commonlyUsedMenu = data.rows;
      });
    },
    openCommonlyUsedPopup(val) {
      this.commonlyUsedForm.menuid = "";
      this.$refs.commonlyUsedPopup.popup();
    },
    //删除用户常用功能
    delCommonlyUsedMenu(id) {
      this.httpUtil.comnUpdate({
        action: "DesktopIndex.delCommonlyUsedMenu",
        params: {id: id}
      }).then(data => {
        this.loadCommonlyUsedMenu();
      });
    },
    //保存用户常用功能
    saveCommonlyUsed(params) {
      if (this.commonlyUsedForm.menuid == "") {
        Tools.alert("请选择常用功能！", "danger");
        return false;
      } else {
        this.httpUtil.comnUpdate({
          action: "DesktopIndex.saveCommonlyUsedMenu",
          params: this.commonlyUsedForm
        }).then(data => {
          this.loadCommonlyUsedMenu();
          this.$refs.commonlyUsedPopup.close();
        });
      }
    },
    homeTaskCheckPage(data) {
      this.$router.push({
        path: data.url,
        query: {prodCode: data.prodCode},
      });
    },

    processCheckPage(data) {
      this.processRowData = data;
      this.$refs.editPopup.popup();
    },
    //发起流程点击查看按钮
    processDetailCheckPage(data) {
      this.processDetailRowData = data;
      this.$refs.detailPopup.popup();
    },
    //工作流待办中点击查看跳转到运营导航页面  rennannan 20210310
    workFlowCheckPage(data) {
      this.$router.push({
        path: 'pms/M81/M81007process',
        query: {findProdCode: data.prod_code},
      });
    },

    dataChange(file, fileList) {
      this.fileList = fileList;
    },
    httpRequest(file) {
      this.fileData.append('files', file.file);
    },
    success() {

    },
    submit() {
      this.fileData = new FormData()
      this.$refs.upload.upload(this.extraData);
      this.httpUtil.upload({
        url: "/upload-files/server/BaseServer/excel/upload.json",
        formData: this.fileData
      }).then(res => {
        this.$refs.upload.doReset()
      })
    },
    test() {
      this.visible = true;
    },
    change($event) {
      //console.info($event);
    },
    //工作流待办中点击查看跳转到运营导航页面
    taskCheckPage(data) {
      this.$router.push({
        path: 'flowable/flowUserToDoTask',
        query: {prodCode: data.prodCode},
      });
    },
    //工作流代办中点击查看跳转到流程追踪（已发起）
    myTaskCheckPage(data) {
      this.$router.push({
        path: 'flowable/flowTrack"',
        query: {prodCode: data.prodCode},
      });
    },
    //查询待审批数据
    findWorkFlowData() {
      this.httpUtil.ajaxJson({
        url: 'wf/process/todoList.json',
        params: {},
      }).then(data => {
        //console.log(data);
        if (data.rows.length > 0) {
          this.processTableData = data.rows;
          // 设置流程状态（待审批）
          for (let i = 0; i < this.processTableData.length; i++) {
            this.processTableData[i].status = "待审批";
          }
        }
      });
    },
    // 查询已发起的审批数据
    findOwnWorkFlowData() {
      this.httpUtil.ajaxJson({
        url: 'wf/desktop/ownList.json',
        params: {},
      }).then(data => {
        //console.log(data);
        if (data.rows.length > 0) {
          this.myProcessData = data.rows;
          // 设置流程状态（待审批）
          for (let i = 0; i < this.myProcessData.length; i++) {
            this.myProcessData[i].status = "待审批";
          }
        }
      });
    },
    //查询工作流数据 rnn 20210309
    // findWorkFlowData() {
    //   this.httpUtil.ajax({
    //     url: '/server/form/PmsApp/home/workflow/findUserWorkFlow.json',
    //     params: {},
    //   }).then(data => {
    //     //console.log(data);
    //     if (data.rows.length > 0) {
    //       this.tableData = data.rows;
    //       for (let i = 0; i < data.rows.length; i++) {
    //         //拼接产品代码与产品名称
    //         this.$set(this.tableData[i], 'prodName', this.tableData[i].prod_code + "-" + this.tableData[i].prod_name);
    //       }
    //     }
    //   });
    // },
    findDisclosureOperation() {
      //信息披露待办事项查询
      this.httpUtil.comnQuery({
        action: 'DisclosureOperation.findDesktopDisclosureOperations',
        params: {},
      }).then(data => {
        if (data.rows) {
          //单独处理净值披露产品首页代办描述
          if (data.rows != '' && data.rows != undefined && data.rows != null && data.rows.length > 0) {
            for (let i = 0; i < data.rows.length; i++) {
              if (data.rows[i].disclosureTypeDesc === "净值报告") {
                //console.log("data.rows[i].dealId=:>>",data.rows[i].dealId);
                let arr = data.rows[i].dealId.split(",");
                let dealId = arr[arr.length - 1];
                //console.log("dealId=:>>>",dealId);
                this.httpUtil.comnQuery({
                  action: 'T8ProdNetValueNotice.findT8ProdNetValueNotices',
                  params: {t8DisclosureTaskId: dealId},
                }).then(res => {
                  let count = res.rows.length
                  let desc = "本期净值披露共有" + count + "只产品需要发起审批";
                  data.rows[i].operationDesc = desc;
                  //modify by zhangchangsi 20211220 如果需要净值披露的产品数量不为0才进行提升
                  if (count !== 0) {
                    this.disclosureTableData.push(data.rows[i])
                  }
                });
              } else {
                this.disclosureTableData.push(data.rows[i])
              }
            }
          }
        }
      });
    },
    disclosureShowMore(data) {
      this.disclosureParam.disclosureType = data.disclosureType;
      this.disclosureParam.operationType = data.operationType;
      //console.log("data.disclosureType=:>>",data.disclosureType);
      if (data.disclosureType === '9') {
        this.$refs.disclosureNetValShowMorePopup.popup();
      } else {
        if (data.disclosureType === '5' && data.operationType === '13') {
          this.$refs.quarterReportApprovalPopup.popup();
        } else {
          this.$refs.disclosureShowMorePopup.popup();
        }
      }

    },
    openPage(row) {
      /*
      *投资经理披露确认	1
      *复核托管意见	10
      *信披经理披露确认	11
      *底层数据导入	12
      *数据补录	2
      *审批拒绝分发	3
      *审批拒绝后补录	4
      *托管拒绝后分发	5
      *托管拒绝后补录	6
      *发起审批	7
      *发送托管	8
      *填写托管意见	9
      *
      * */
      if (row.operationType === '2' || row.operationType === '3' || row.operationType === '4' || row.operationType === '5' || row.operationType === '6' || row.operationType === '7' || row.operationType === '8' || row.operationType === '9' || row.operationType === '14') { //提示需要发起申请
        var inGroup = '';
        this.httpUtil.comnQuery({
          action: 'DisclosureNotice.findUserInfo',
          params: {t8ProdInfoId: row.t8ProdInfoId, id: row.dealId},
        }).then(data => {
          if (data.rows != null && data.rows != undefined && data.rows != "") {
            if (data.rows[0].count > 0) {
              inGroup = '1'
            } else {
              inGroup = '0'
            }
          }
          this.$router.push({
            path: row.url,
            query: {id: row.dealId, t8ProdInfoId: row.t8ProdInfoId, prodCode: row.prodCode, inGroup: inGroup},
          });

        });

      } else if (row.operationType === '13') {//定期报告模板审批
        this.$router.push({
          path: row.url,
          query: {disclosureModId: row.id},
        });
      } else {
        this.$router.push({
          path: row.url,
          query: row,
        });
      }
      return false;
    }
  },
  mounted: function () {
    //加载常用功能
    this.loadCommonlyUsedMenu();
    // this.listMchts()
    // this.listSigns()
  },

  created() {
    //未来重大提醒事件
    this.httpUtil.comnQuery({
      action: 'DesktopIndex.findDesktopIndexList',
      params: {},
    }).then(data => {
      if (data.results > 0) {
        this.homeTaskTableData = data.rows;
        for (let i = 0; i < data.rows.length; i++) {
          this.$set(this.homeTaskTableData[i], 'rownum', i + 1);
          this.homeTaskRowData = assign({}, data.rows[i]);
        }
      }
    });
    //查询工作流数据
    this.findWorkFlowData();
    //查询已发起工作流数据
    this.findOwnWorkFlowData();
    //信息披露待办事项
    //this.findDisclosureOperation();
    // //未来重大提醒事件
    // this.httpUtil.comnQuery({
    //   action: 'DesktopIndex.findDesktopIndexList',
    //   params: {},
    // }).then(data => {
    //   if (data.results > 0) {
    //     this.homeTaskTableData = data.rows;
    //     for (let i = 0; i < data.rows.length; i++) {
    //       this.$set(this.homeTaskTableData[i], 'rownum', i + 1);
    //       this.homeTaskRowData = assign({}, data.rows[i]);
    //     }
    //   }
    // });
    // //查询工作流数据
    // this.findWorkFlowData();
    // //信息披露待办事项
    // this.findDisclosureOperation();
  }
}
</script>

<style>
.dialog-icon {
  width: 63px;
  height: 63px;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: -40px;
  background: linear-gradient(60deg, #66bb6a, #43a047);
  border-radius: 3px;
  float: left;
  margin-right: 8px;
}

.dialog-icon .md-icon {
  color: #FFFFFF !important;
}
</style>
<style scoped>
.editPopupClass ::v-deep .el-dialog {
  padding-top: 35px;
  margin-right: 24px;
}

.disclosureClass ::v-deep .el-dialog {
  padding-top: 35px;
  margin-right: 14%;
  margin-top: 5% !important;
}
::v-deep .el-tab-pane{
  width: 100%;
  height: 100%;
}
::v-deep .el-tabs__content{
  height: 100%;
  overflow: hidden;
  position: relative;
}
/deep/ .el-tabs__content {
  height: 100%;
  overflow: hidden;;
  position: relative;
}
/deep/ .el-tab-pane{
  width: 100%;
  height: 90%;
}
/deep/ .el-table {
  display: flex;
  flex-direction: column;
  .el-table__body-wrapper {
    flex: 1;
    overflow-y: auto;
  }
}
</style>
