<!--待审核任务-->
<template>
  <div class="myFlow tab-page">
    <el-tabs v-model="activeName">
      <el-tab-pane name="myTodoTask">
        <span slot="label">待审批
          <el-badge class="mark" v-if="myTodoTaskNum" :value="myTodoTaskNum" />
        </span>
        <div class="tab-page">
          <k-grid ref="taskGrid"  :data-params='{"surrogateFlag":"0"}' dataContentType="json" :data-data="{'rows':this.processTableData}">
            <k-grid-column data-align="center" data-header="流程名" data-name="procDefName"></k-grid-column>
            <k-grid-column data-align="center" data-header="关键词" data-name="values"></k-grid-column>
            <k-grid-column data-align="center" data-header="关键词名称" data-name="valuesName"></k-grid-column>
            <k-grid-column data-align="center" data-header="流程状态" data-name="status"></k-grid-column>
            <k-grid-column data-align="center" data-header="当前节点" data-name="taskName"></k-grid-column>
            <k-grid-column data-align="center" data-header="发起时间" data-name="procStartTime"></k-grid-column>
            <k-grid-column data-align="center" data-header="发起人" data-name="startUserName"></k-grid-column>
            <k-grid-column data-align="center" data-header="当前节点" data-name="taskName"></k-grid-column>
            <template slot="operate" slot-scope="scope">
              <k-btn class="md-info md-just-icon md-simple" data-descript="审核" @click.native.stop="approveBtn(scope.row.row)" data-size="mini">
                <md-icon>library_add_check</md-icon>
              </k-btn>
            </template>
          </k-grid>
        </div>
      </el-tab-pane>
      <el-tab-pane name="surrogateTask">
        <span slot="label">已发起
          <el-badge class="mark" v-if="surrogateTaskNum" :value="surrogateTaskNum" />
        </span>
        <div class="tab-page">
          <k-grid ref="surrogateTaskGrid" :data-params='{"surrogateFlag":"1"}' dataContentType="json" @data-row-select="selectRow">
            <k-grid-column data-align="center" data-header="流程名" data-name="procDefName"></k-grid-column>
            <k-grid-column data-align="center" data-header="关键词" data-name="values"></k-grid-column>
            <k-grid-column data-align="center" data-header="关键词名称" data-name="valuesName"></k-grid-column>
            <k-grid-column data-align="center" data-header="流程状态" data-name="status"></k-grid-column>
            <k-grid-column data-align="center" data-header="流程节点" data-name="taskName"></k-grid-column>
            <k-grid-column data-align="center" data-header="发起时间" data-name="procStartTime"></k-grid-column>
            <k-grid-column data-align="center" data-header="发起人" data-name="startUserName"></k-grid-column>
            <k-grid-column data-align="center" data-header="当前节点" data-name="taskName"></k-grid-column>

            <template slot="operate" slot-scope="scope">
    <!--          <k-btn class="md-info md-just-icon md-simple" data-descript="审核" @click.native.stop="detailBtn(scope.row.row)" data-size="mini">-->
    <!--            <md-icon>library_add_check</md-icon>-->
    <!--          </k-btn>-->
              <k-btn class="md-info md-just-icon md-simple" data-descript="详情" data-functype="POPUP" data-size="mini" data-target="detailPopup">
                <md-icon>library_books</md-icon>
              </k-btn>
            </template>
          </k-grid>
        </div>
      </el-tab-pane>
    </el-tabs>




    <k-popup ref="auditPopup" :data-open="getEnableFileUpload" :data-title="selectRowData.procDefName"  data-width="1200px">
      <el-collapse style="margin-left:20px;margin-right:20px" v-model="activeNames">
        <el-collapse-item title="操作历史" name="his">
          <FlowHisTaskDetail :taskInfo="selectRowData"></FlowHisTaskDetail>
        </el-collapse-item>
        <el-collapse-item title="审核表单信息" name="form">
          <FlowFormInfo :taskInfo="selectRowData"></FlowFormInfo>
        </el-collapse-item>
        <el-collapse-item v-if="enableFileUpload=='1'" title="附件列表" name="file">
          <FlowFileUpload :taskInfo="selectRowData"></FlowFileUpload>
        </el-collapse-item>
        <el-collapse-item title="流程图" name="flow">
          <FlowViewer :taskInfo="selectRowData"></FlowViewer>
        </el-collapse-item>
      </el-collapse>
      <p></p>
      <FlowApproval :taskInfo="selectRowData" @closeDetailPopup="closeAuditPopup" @loadTaskGrid="loadTaskGrid"></FlowApproval>
    </k-popup>

    <k-popup ref="detailPopup"  data-title="流程审批详情" data-width="1200px">
      <el-collapse style="margin-left:20px;margin-right:20px" v-model="activeNames">
        <el-collapse-item title="操作历史" name="his">
          <FlowHisTaskDetail :taskInfo="selectRowData"></FlowHisTaskDetail>
        </el-collapse-item>
        <el-collapse-item title="审核表单信息" name="form">
          <FlowFormInfo :taskInfo="selectRowData"></FlowFormInfo>
        </el-collapse-item>
        <el-collapse-item title="流程图" name="flow">
          <FlowViewer :taskInfo="selectRowData"></FlowViewer>
        </el-collapse-item>
      </el-collapse>
    </k-popup>
  </div>

</template>

  <script>
import FlowHisTaskDetail from "../../../flowable/components/FlowHisTaskDetail.vue";
import FlowFormInfo from "../../../flowable/components/FlowFormInfo.vue";
import FlowFileUpload from "../../../flowable/components/FlowFileUpload.vue";
import FlowViewer from "../../../flowable/components/FlowViewer.vue";
import FlowApproval from "../../../flowable/components/FlowApproval.vue";
import FlowFillBusiFormInfo from "../../../flowable/components/FlowFillBusiFormInfo.vue";
import FlowBusiFormInfo from "../../../flowable/components/FlowBusiFormInfo.vue";
import {assign} from "lodash";

export default {
	name: "myToDoTask",
	components: { FlowHisTaskDetail, FlowFormInfo, FlowViewer, FlowFileUpload, FlowApproval, FlowFillBusiFormInfo, FlowBusiFormInfo },
	data() {
		return {
			selectRowData: {},
			queryParamProcessKey: "",
			queryParamApplyUser: "",
			queryParamDateRange: [],
			activeNames: ["his", "form", "file", "busiForm", "fillBusiForm"],
			enableFileUpload: "0",
			busiFormInfo: [],
			surrogateFlagOption: [
				{
					label: "是",
					value: "1",
				},
				{
					label: "否",
					value: "0",
				},
			],
			activeName: "myTodoTask",
			myTodoTaskNum: 0,
			surrogateTaskNum: 0,
      processTableData: [],// 待审批的流程数据（流程情况一览）
      myProcessData: [],// 我发起的流程（流程情况一览）
		};
	},
	computed: {
		queryParam() {
			return {
				processKey: this.queryParamProcessKey,
				applyUser: this.queryParamApplyUser,
				createStartDate: this.queryParamDateRange ? this.queryParamDateRange[0] : null,
				createEndDate: this.queryParamDateRange ? this.queryParamDateRange[1] : null,
				surrogateFlag: this.activeName == "myTodoTask" ? "0" : "1",
			};
		},
	},
	created() {
    this.findOwnWorkFlowData();
    this.findWorkFlowData();
  },
	methods: {
    selectRow(row, column, event) {
      const _this = this;
      _this.selectRowData = assign({}, row);
    },
    change(name) {
      this.currentTabLabel = name;
      console.log("this.currentTabLabel=:>",this.currentTabLabel);
      this.$nextTick(()=>{
        if(name=="待审批"){
          console.log("待审批=:>",this.currentTabLabel);
          this.$refs.taskGrid.load();
        }else if (name=="指标校验提醒"){
          console.log("指标校验提醒=:>",this.currentTabLabel);
          this.$refs.surrogateTaskGrid.load();
        } else {

        }
      })
    },
		//审核按钮
		approveBtn(row) {
			this.selectRowData = row;
			this.getEnableFileUpload();
			//this.getBusiFormInfo();
			this.$refs.auditPopup.popup();
		},
    detailBtn(row){
      this.selectRowData = row;
      this.getEnableFileUpload();
      //this.getBusiFormInfo();
      this.$refs.detailPopup.popup();
    },
		closeAuditPopup() {
      console.log("刷新页面");
      this.$refs.auditPopup.close();
		},
		loadTaskGrid(bol = false) {
      this.findWorkFlowData();
      this.findOwnWorkFlowData();
      if (bol) {
        this.$emit("refreshNum")
      }
		},
    //查询待审批数据
    findWorkFlowData() {
      this.httpUtil.ajaxJson({
        url: 'wf/process/todoList.json',
        params: {},
      }).then(data => {
        if (data.rows.length > 0) {
          for (let i = 0; i < data.rows.length; i++) {
            data.rows[i].status = '待审批';
          }
          this.$refs.taskGrid.list=data.rows;
          this.myTodoTaskNum = data.rows.length;
        }else {
          this.$refs.taskGrid.list=data.rows;
          this.myTodoTaskNum = data.rows.length;
        }
      });
    },
    // 查询已发起的审批数据
    findOwnWorkFlowData() {
      this.httpUtil.ajaxJson({
        url: 'wf/desktop/ownList.json',
        params: {},
      }).then(data => {
        if (data.rows.length > 0) {
          // 设置流程状态（待审批）
          for (let i = 0; i < data.rows.length; i++) {
            data.rows[i].status = "待审批";
          }
          this.$refs.surrogateTaskGrid.list=data.rows;
          this.surrogateTaskNum = data.rows.length;
        }
      });
    },
		//是否开启文件上传
		getEnableFileUpload() {
			this.httpUtil
				.ajaxJson({
					url: "wf/fileUpload/enableFileUpload/" + this.selectRowData.procDefId + ".json",
				})
				.then((res) => {
					this.enableFileUpload = res.data;
				});
		},

		// getBusiFormInfo() {
		// 	// 获取审批表单数据
		// 	return this.httpUtil
		// 		.ajaxJson({
		// 			url: "wf/process/busiFormInfo/" + this.selectRowData.procDefId + "/" + this.selectRowData.procInsId + ".json",
		// 		})
		// 		.then((res) => {
		// 			this.busiFormInfo = res.rows;
		// 		});
		// },
	},
};
</script>
<style lang="scss" scoped>
/deep/ .el-dialog {
	padding-top: 35px;
}

.tabs {
  display: flex;
  margin: 10px 0 30px 0;
  .tab-item {
    position: relative;
    margin-right: 20px;
    font-size: 14px;
    font-weight: bold;
    line-height: 30px;
    margin-right: 50px;
    border-bottom: 2px solid transparent;
    text-align: center;
    cursor: pointer;
    &.active {
      color: #5475ad;
      border-color: #5475ad;
    }
  }
}
</style>
