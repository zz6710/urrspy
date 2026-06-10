<template>
  <div>
    <k-grid ref="ta5014DetailGrid" data-action="Ta5014Detail.findTa5014Detail" @data-row-select="selectRow" data-fixed="right"
            @init="(grid)=>{this.$kgrid = grid}" :toogle="true" data-height="750" data-operate-width="180px"
            data-diffcondition="execGridId,parentExecGridId" :data-show-tree="true"
            data-tree-id="execGridId" :data-card="false" :handle-expand-change="handleExpandChange"
            :data-expend-row="initExpand" style="margin-bottom: 15px"
            :data-autoload="false" :data-after-load="afterDataHandle"
    >
      <!--      :handle-expand-change="handleExpandChange" :data-expend-row="initExpand" 如果需要进入展开所有列,加上这两条属性即可-->
      <!--      头部查询条件-->
      <template slot="search">
        <!--清算任务管理页面定制内容开始-->
        <k-form :data-col="4" v-if="formData != undefined" v-model="formData" class="searchForm">
<!--          <k-form-item style="width: 200px" v-if="execTaskType=='3'||execTaskType=='4'">
            <k-field-select v-model="searchData.targetCode" @data-on-change="changeData"
                            data-action="TaDict.findTaDistributorInfos"
                            data-display-field="distributorCode,distributorName"
                            data-value-field="distributorCode" data-placeholder="销售商代码"></k-field-select>
          </k-form-item>
          <k-form-item style="width: 200px" v-if="execTaskType=='2'">
            <k-field-select v-model="searchData.prodCode" @data-on-change="changeData"
                            data-action="TaDict.findTaProdInfos" data-display-field="prodCode,prodName"
                            data-value-field="prodCode" data-placeholder="产品名称"></k-field-select>
          </k-form-item>-->
          <k-form-item style="width: 200px">
            <k-field-text v-model="searchData.taskName" @data-on-change="changeData"
                          data-placeholder="任务名称"></k-field-text>
          </k-form-item>
          <k-form-item style="width: 200px">
            <k-field-select v-model="searchData.execStatus" @data-on-change="changeData" data-placeholder="执行状态"
                            data-dict="t8_batch_task_status"></k-field-select>
          </k-form-item>
          <!--          <k-btn class="md-info closeBtn" data-functype="CLOSE" :data-handler="handleClose" small-size="true">
                      <md-icon md-src="/static/svg/cancel.svg"></md-icon>关闭
                    </k-btn>-->
        </k-form>

        <!--清算任务管理页面定制内容结束-->
      </template>

      <k-grid-column data-header="任务Id" data-name="taskId" data-width="60"></k-grid-column>
      <k-grid-column data-header="任务名称" v-if="execTaskType!=6" data-name="taskName" data-width="230"></k-grid-column>
      <k-grid-column data-header="任务名称" v-if="execTaskType==6" data-name="distributorName"
                     data-width="160"></k-grid-column>
      <!-- <k-grid-column data-header="产品代码" v-if="execTaskType==2" data-name="prodCode" data-width="100"></k-grid-column> -->
      <!-- 产品代码显示为名称 -->
      <!--      <k-grid-column data-header="销售商代码" v-if="execTaskType!=1" data-name="distributorCode" data-width="120"></k-grid-column>-->
      <!-- <k-grid-column data-header="销售商代码" v-if="execTaskType==3||execTaskType==4" data-name="targetCode" -->
                     <!-- data-width="90"></k-grid-column> -->
      <k-grid-column data-header="执行id" data-name="taskExecid" data-width="140" data-hidden="true"></k-grid-column>
      <k-grid-column data-header="任务日期" data-name="taskDate" data-width="90" data-type="date"></k-grid-column>
      <k-grid-column data-header="应执行日期" data-name="shouldExecDate" data-width="85" data-type="date"></k-grid-column>
      <k-grid-column data-header="应执行时间" data-name="shouldExecTime" data-width="85" data-type="time"></k-grid-column>
      <k-grid-column data-header="执行日期" data-name="execDate" data-width="90" data-type="date"></k-grid-column>
      <k-grid-column data-header="开始时间" data-name="startTime" data-width="70" data-type="time"></k-grid-column>
      <k-grid-column data-header="结束时间" data-name="endTime" data-width="70" data-type="time"></k-grid-column>
      <k-grid-column data-header="执行状态" data-name="execStatus" data-width="100" data-dict="batch_task_status"
                     :dataRenderHtml="true" dataRender="render"></k-grid-column>
      <k-grid-column data-header="执行结果" data-name="rtnDesc" dataRender="render2" data-width="240"></k-grid-column>
      <k-grid-column data-header="任务执行UUID" data-name="threadUuid" data-width="100" data-hidden="true"></k-grid-column>
      <k-grid-column data-header="清算组id" data-name="taskGroup" data-hidden="true"></k-grid-column>
      <k-grid-column data-header="参数信息" data-name="taskParams" data-hidden="true"></k-grid-column>
      <k-grid-column data-header="前置任务" data-name="preTaskId" data-hidden="true"></k-grid-column>
      <k-grid-column data-header="服务器节点信息" data-name="serverNode" data-hidden="true"></k-grid-column>
      <k-grid-column data-header="服务器主机名" data-name="serverName" data-hidden="true"></k-grid-column>
      <k-grid-column data-header="服务器主机IP" data-name="serverIp" data-hidden="true"></k-grid-column>
      <!--<k-grid-column data-header="清算流程" data-name="simpleFlow"  data-dict="simple_flow" ></k-grid-column>-->

      <template slot="operate" slot-scope="scope">
        <!-- todo 执行清算任务 -->
        <k-btn data-functype="SUBMIT" data-size="mini" data-target="ta5003Grid" data-confirm
               class="md-info md-just-icon md-simple" @init="(grid)=>{this.$kgrid = grid}"
               data-descript="执行清算任务" :data-disable-condition="ta5003js.execDisableCondition"
               v-if="scope.row.row.execStatus != 'B' && (scope.row.row.buttonIsDisplay != '0' || ((scope.row.row.parentExecGridId==undefined || scope.row.row.parentExecGridId=='') && (scope.row.row.execStatus == '0' || scope.row.row.execStatus == '6')))"
               :data-after-success="whenAfterSuccess" :data-handler="ta5003js.invokeClear">
          <md-icon md-src='/static/svg/run1.svg'>play_arrow</md-icon>
        </k-btn>

        <k-btn data-functype="SUBMIT" data-size="mini" data-target="ta5003Grid" data-confirm
               class="md-warning md-just-icon md-simple"
               data-descript="继续" v-if="scope.row.row.execStatus == 'B'"
               :data-handler="ta5003js.continueExcute"
               :data-model="selectRowData">
          <md-icon md-src='/static/svg/continue.svg'>play_arrow</md-icon>
        </k-btn>

        <!-- todo 跳过清算任务 -->
        <k-btn data-functype="SUBMIT" data-size="mini" data-target="ta5003Grid" data-confirm
               class="md-warning md-just-icon md-simple"
               data-descript="跳过清算任务不执行"
               :data-handler="ta5003js.updateStatusSkip"
               :data-model="selectRowData">
          <md-icon>low_priority</md-icon>
        </k-btn>

        <!-- todo  回滚清算任务 -->
        <k-btn data-functype="SUBMIT" data-size="mini" data-target="ta5003Grid" data-confirm
               class="md-danger md-just-icon md-simple"
               data-descript="回滚已执行的清算任务" :data-model="selectRowData"
               :data-handler="ta5003js.rollBackClearTask">
          <md-icon>replay</md-icon>
        </k-btn>
        <!-- todo  回滚清算任务 -->
        <k-btn data-functype="SUBMIT" data-size="mini" data-target="ta5003Grid" data-confirm
               class="md-danger md-just-icon md-simple"
               data-descript="回滚已执行的清算任务" v-if="ta5003js.rollbackShowCondition2(scope.row.row)"
               :data-handler="ta5003js.rollBackClear2">
          <md-icon>replay</md-icon>
        </k-btn>
        <!-- todo  回滚清算任务 -->
        <k-btn data-functype="SUBMIT" data-size="mini" data-target="ta5003Grid" data-confirm
               class="md-danger md-just-icon md-simple"
               data-descript="回滚已执行的清算任务" v-if="ta5003js.rollbackShowCondition3(scope.row.row)"
               :data-handler="ta5003js.rollBackClear3">
          <md-icon>replay</md-icon>
        </k-btn>
        <!-- todo  回滚清算任务 -->
        <k-btn data-functype="SUBMIT" data-size="mini" data-target="ta5003Grid" data-confirm
               class="md-danger md-just-icon md-simple"
               data-descript="回滚已执行的清算任务" v-if="ta5003js.rollbackShowCondition4(scope.row.row)"
               :data-handler="ta5003js.rollBackClear">
          <md-icon>replay</md-icon>
        </k-btn>
        <k-btn data-functype="POPUP" data-size="mini" data-target="sliceGrid" class="md-info md-just-icon md-simple"
               v-if="ta5003js.checkTaskExecid(scope.row.row)"
               data-descript="分片详情">
          <md-icon md-src='/static/svg/slice1.svg'>info_outline</md-icon>
        </k-btn>
        <k-btn data-functype="POPUP" data-size="mini" data-target="stepGrid" class="md-info md-just-icon md-simple"
               v-if="ta5003js.checkTaskExecid(scope.row.row)"
               data-descript="步骤详情">
          <md-icon md-src='/static/svg/step1.svg'>search</md-icon>
        </k-btn>
        <!-- <k-btn data-functype="POPUP" data-size="mini" data-target="bbGrid" class="md-info md-just-icon md-simple"
               v-if="ta5003js.checkTaskExecid(scope.row.row)"
               data-descript="批前检查">
          <md-icon md-src='/static/svg/front2.svg'>help_outline</md-icon>
        </k-btn> -->
      </template>
<!--      //产品批次和销售商批次做分页-->
      <template slot="footer">
        <div style="position: relative">
          <div  class="paginationClass" v-if="(this.execTaskType=='2' || this.execTaskType=='3' || this.execTaskType=='4') && parseInt(total)>10">
            <span class="tipClass">当前{{ this.execTaskType == '2' ? '产品' : '销售商' }}数量大于10</span>
            <el-pagination style="display: inline-block" :current-page="currentPage" @current-change="handleSizeChange"
                           layout="total, prev, pager, next" :total="parseInt(total)"></el-pagination>
          </div>
        </div>
      </template>
    </k-grid>
    <k-popup ref="bbGrid" data-title="任务检查表">
      <BBGrid v-model="formData"></BBGrid>
    </k-popup>
    <k-popup ref="sliceGrid" data-title="分片任务表" data-width-percent="68%">
      <SliceGrid v-model="formData"></SliceGrid>
    </k-popup>
    <k-popup ref="stepGrid" data-title="清算任务表">
      <StepGrid v-model="formData"></StepGrid>
    </k-popup>
    <k-popup ref="revocationGrid" data-title="回滚任务步骤">
      <RevocationGrid v-model="formData"></RevocationGrid>
    </k-popup>

  </div>
</template>
<script>

import kayak from '@/frame/kayak.js';
import {assign} from "lodash";
import ta5003js from "./JS/Ta5003.js";
import BBGrid from "./Ta5003BusiBatch";
import SliceGrid from "./Ta5003SliceExec";
import StepGrid from "./Ta5003StepExec";
import RevocationGrid from "./Ta5003Revocation";
import Tools from "@/utils/tools";

export default {
  components: {BBGrid, SliceGrid, StepGrid, kayak, RevocationGrid},
  props: {
    clearGroup: Object,
    handleClose: {
      type: Function
    },
    initExpand: {
      type: Array
    },
    handleExpandChange: {
      type: Function
    }
  },
  data() {
    return {
      formData: {},
      selectRowData: {},
      ta5003js: {},
      $kgrid: null,
      isAuto: false,
      taskGroup: '',
      taskDate: '',
      execTaskType: '',
      taskGrouopName: '',
      searchData: {},
      initFlag: true,
      total: 100,
      currentPage: 1,
      limit: 10,
      cIntervalid: 0
    };
  },
  methods: {
    selectRow(row, column, event) {
      const _this = this;
      _this.selectRowData = assign({}, row);
      _this.formData = assign({}, row);
      if (event.srcElement.tagName == 'SPAN' && event.srcElement.className == "failed") {
        this.$refs.revocationGrid.popup();
      }
    },
    changeData() {
      var temp = {};
      if (this.searchData.prodCode != undefined && '' !== this.searchData.prodCode) {
        temp.prodCode = this.searchData.prodCode;
      }
      if (this.searchData.targetCode != undefined && '' !== this.searchData.targetCode) {
        temp.targetCode = this.searchData.targetCode;
      }
      if (this.searchData.taskName != undefined && '' !== this.searchData.taskName) {
        temp.taskName = this.searchData.taskName;
      }
      if (this.searchData.execStatus != undefined && '' !== this.searchData.execStatus) {
        temp.execStatus = this.searchData.execStatus;
      }
      temp.queryTaskDate = this.taskDate;
      temp.taskGroup = this.taskGroup;
      temp.execTaskType = this.execTaskType;
      temp.diffcondition = "execGridId,parentExecGridId";
      temp.currentPage = this.currentPage - 1;
      temp.limit = this.limit;
      this.$kgrid.load(temp);
    },

    whenAfterSuccess() {
      Tools.alert("执行成功");
    },
    render(row) {
      let execStyle = "";

      if (row.execStatus == "0") {
        //未执行
      } else if (row.execStatus == "1" || row.execStatus == "2" || row.execStatus == "3" || row.execStatus == "4") {
        //执行中
        execStyle = "style='color:#FF9901 !important'";
      } else if (row.execStatus == "5" || row.execStatus == "7" || row.execStatus == "8") {
        //执行成功
        execStyle = "style='color:#4CAF50 !important'";
      } else if (row.execStatus == "6" || row.execStatus == "9") {
        //执行失败
        execStyle = "style='color:#F54336 !important'";
      } else {
        //任务回滚
        execStyle = "style='color:#FF9901 !important;  cursor: pointer;' class ='failed'";
      }

      return "<span " + execStyle + ">#{}</span>";
    },
    render2(row) {
      let rtnDesc = row.rtnDesc;
      let arr;
      if (rtnDesc == undefined || (arr = rtnDesc.split(";")).length != 4) {
        return rtnDesc;
      }
      return "<span style='color: #F54336'>" + arr[0] + "</span>&nbsp;<span style='color: #5DC97A'>" + arr[1]
        + "</span>&nbsp;<span style='color: #000000'>" + arr[2] + "</span>&nbsp;<span style='color: #999999'>" + arr[3] + "</span>"

    },
    //设置定时器，定时查询清算数据
    timer() {
      return setInterval(() => {
        this.changeData();
        // 后端给空值时默认 查当前工作日
        // this.$kgrid.load(params);
      }, 5000);
    },
    setTimer() {
      this.cIntervalid = this.timer();
    },
    destroyedTimer() {
      clearInterval(this.cIntervalid);
    },

    afterDataHandle(data) {
      if (data != undefined && data[0] != undefined) {
        this.total = data[0].total;
      }
    },
    handleSizeChange(page) {
      this.currentPage = page;
      this.changeData();
    }

  },
  created() {
    this.ta5003js = ta5003js;

    //获取定时任务ID，关闭页面要清除定时任务
    this.cIntervalid = this.timer();

    //页面加载去查询自动执行的状态
    this.execTaskType = this.clearGroup.execTaskType;
    this.taskGroupName = this.clearGroup.taskGroupName;
    this.taskGroup = this.clearGroup.taskGroup;
    this.taskDate = this.clearGroup.taskDate;
    // this.$store.commit("system/setTopBarMenuName", this.taskGroupName);
    if (this.initExpand == undefined) {
      this.initExpand = [];
    }
  },
  beforeDestroy() {
    clearInterval(this.cIntervalid);
    let vnodeId;
    let tab;
    this.$store.state.system.tab.forEach(res => {
      if (res.path == '/main/TA/TA5/TA5014') {
        vnodeId = res.vnodeId;
        tab = res;
      }
    })
    this.$store.state.system.tab2.forEach(res => {
      if (res.path == '/main/TA/TA5/TA5014') {
        vnodeId = res.vnodeId;
        tab = res;
      }
    })
    let count = 0;
    let cacheVnodeId;
    for (cacheVnodeId in this.$store.state.system.keepAliveCache) {
      if (cacheVnodeId === vnodeId) {
        this.handleClose();
        return;
      }
      count++;
    }
    if (count == 1) {
      let vnode = this.$store.state.system.keepAliveCache[cacheVnodeId];
      let tag = vnode.tag.match(/\w{1,}$/g);
      if (tag && tag.length > 0 && tag[0] !== "desktop") {
        tab.vnodeId = cacheVnodeId;
        this.handleClose();
      }
    }
  },
  mounted() {
    this.$kgrid.load({
      "queryTaskDate": this.taskDate,
      "taskGroup": this.taskGroup,
      "execTaskType": this.execTaskType,
      "diffcondition": "execGridId,parentExecGridId",
      "currentPage": this.currentPage - 1,
      "limit": this.limit
    });
  },
}

</script>
<style scoped>
.closeBtn {
  display: inline-block;
  position: absolute;
  z-index: 5;
  right: 85px;
  top: 10px;
}

.searchForm {
  position: absolute;
  top: 6px;
  margin-left: 100px;
}

.paginationClass {
  display: inline-block;
  position: absolute;
  right: 0;
  top: 3px;
}

.tipClass {
  position: relative;
  top: 4.5px;
}

</style>

<style>
.abow_dialog > .el-dialog > .el-dialog__header > .el-dialog__headerbtn {
  z-index: 999;
}
</style>
