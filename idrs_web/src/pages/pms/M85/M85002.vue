<template>
  <div  class="md-layout">
    <md-card class="k-card public-width">
      <div class="head-div">
        <md-card-header class="md-card-header-text md-card-header-green">
          <div class="card-icon">
            <md-icon md-src="/static/svg/flow.svg"></md-icon>
          </div>
        </md-card-header>

        <div class="head-date">
            <span class="date-span">{{displayWorkdate}}</span>
        </div>
        <div class="circle-div">
            <div class="circle color-noExecute"></div>
            <div><span>未执行</span></div>
        </div>
        <div class="circle-div">
            <div class="circle color-executing"></div>
            <div><span>执行中</span></div>
        </div>
        <div class="circle-div">
            <div class="circle color-fail"></div>
            <div><span>执行失败</span></div>
        </div>
        <div class="circle-div">
            <div class="circle color-success"></div>
            <div><span>执行成功</span></div>
        </div>
      </div>
      <div class="progress-parents">
        <div class="progress-div">
          <div v-for="(clearInfo,index) in clearInfos" :key="clearInfo.simpleFlow" >
              <div>
                <div @click="progressOnClick(clearInfo)" class="progressbar-parents">
                    <el-progress type="circle" :percentage="clearInfo.percentage" :color="progressColorArry[clearInfo.simpleFlow]"
                          class="progress-position"  :show-text="false" :width="90" :stroke-width="3"></el-progress>
                    <div :class="clearInfo.percentage===100? finishTaskColorArry[clearInfo.simpleFlow]:taskColorArry[clearInfo.simpleFlow]"></div>
                </div>
                <div class="task-type-div"><span> {{flowNameArry[clearInfo.simpleFlow]}} </span></div>
                <div class="btn-div">
                  <div class="progress-detail-circle color-success" v-show="clearInfo.successNum>0"><span> {{clearInfo.successNum}} </span></div>
                  <div class="progress-detail-circle color-fail" v-if="clearInfo.failNum>0"><span>{{clearInfo.failNum}}</span></div>
                  <div class="progress-detail-circle color-executing" v-if="clearInfo.executingNum>0"><span>{{clearInfo.executingNum}}</span></div>
                  <div class="progress-detail-circle color-noExecute" v-if="clearInfo.noExecuteNum>0"><span>{{clearInfo.noExecuteNum}}</span></div>
                </div>
              </div>
            <div class="connect-line" v-if="index!=clearInfos.length-1"></div>
          </div>
        </div>
      </div>
    </md-card>

    <div style="margin: 20px 0 0 100px;position: relative;z-index: 1;">
      <k-form ref="ta5002Form" :data-col="3" :data-model="gridQueryData">
        <div class="showTaskNameSpan">
          <span>{{table}}</span>
        </div>
        <k-form-item data-input-width="150px" style="margin-top: 20px;">
          <k-field-text v-model="gridQueryData.prodCode"  data-placeholder="产品代码"/>
        </k-form-item>
        <k-form-item data-input-width="150px" style="margin-top: 20px;">
          <k-field-text v-model="gridQueryData.targetCode"  data-placeholder="目标代码"/>
        </k-form-item>
        <k-form-item data-input-width="150px" style="margin-top: 20px;">
          <k-field-select v-model="gridQueryData.execStatus" data-placeholder="执行状态"  data-dict="exec_status"/>
        </k-form-item>
      </k-form>
    </div>

    <div class="grid-div public-width" id="ta5002Grid">
      <k-grid ref="ta5002Grid"  data-action="Ta5003.findTa5003s" data-diffcondition="execGridId,parentExecGridId" data-tree-id="execGridId"
                :data-autoload="false" @init="(grid)=>{this.$kgrid = grid}" :data-display="false">
        <k-grid-column data-header="" data-name="headDesc"  data-width="150" ></k-grid-column>
        <k-grid-column data-header="任务名称" data-name="taskName" data-width="150" ></k-grid-column>
        <k-grid-column data-header="产品代码" data-name="prodCode" data-width="120" ></k-grid-column>
        <k-grid-column data-header="目标代码" data-name="targetCode" ></k-grid-column>
        <k-grid-column data-header="业务日期" data-name="taskDate" data-width="90" data-type="date"></k-grid-column>
        <k-grid-column data-header="应执行日期" data-name="shouldExecDate" data-width="90" data-type="date"></k-grid-column>
        <k-grid-column data-header="应执行时间" data-name="shouldExecTime" data-width="90" data-type="time"></k-grid-column>
        <k-grid-column data-header="开始时间" data-name="startTime" data-width="90" data-type="time"></k-grid-column>
        <k-grid-column data-header="结束时间" data-name="endTime" data-width="90" data-type="time"></k-grid-column>
        <k-grid-column data-header="执行状态" data-name="execStatus" width="80" data-dict="exec_status" ></k-grid-column>
        <k-grid-column data-header="执行结果" data-name="rtnDesc" width="200"></k-grid-column>
        <k-grid-column data-header="清算流程" data-name="simpleFlow"  data-dict="simple_flow" ></k-grid-column>

        <template slot="operate" slot-scope="scope" >
          <!-- todo 执行清算任务 -->
          <k-btn data-functype="SUBMIT" data-size="mini"  data-target="ta5002Grid" data-confirm  class="md-info md-just-icon md-simple"
              data-descript="执行清算任务" :data-disable-condition ="ta5003js.execDisableCondition"  v-if="scope.row.row.buttonIsDisplay != '0'"
              :data-after-success="whenAfterSuccess"  :data-handler="ta5003js.invokeClear">
            <md-icon>play_arrow</md-icon>
          </k-btn>

          <!-- todo 跳过清算任务 -->
          <k-btn data-functype="SUBMIT" data-size="mini"  data-target="ta5002Grid" data-confirm  class="md-warning md-just-icon md-simple"
              data-descript="跳过清算任务不执行" v-if="ta5003js.skipShowCondition(scope.row.row)"  data-action="Ta5003.updateStatusSkip"
            >
            <md-icon>low_priority</md-icon>
          </k-btn>

          <!-- todo  回滚清算任务 -->
          <k-btn data-functype="SUBMIT" data-size="mini"  data-target="ta5002Grid" data-confirm  class="md-danger md-just-icon md-simple"
              data-descript="回滚已执行的清算任务" v-if="ta5003js.rollbackShowCondition(scope.row.row)"  :data-handler="ta5003js.rollBackClear">
            <md-icon>replay</md-icon>
          </k-btn>
          <!-- todo  回滚清算任务 -->
          <k-btn data-functype="SUBMIT" data-size="mini"  data-target="ta5002Grid" data-confirm  class="md-danger md-just-icon md-simple"
              data-descript="回滚已执行的清算任务" v-if="ta5003js.rollbackShowCondition2(scope.row.row)"  :data-handler="ta5003js.rollBackClear2">
            <md-icon>replay</md-icon>
          </k-btn>
          <!-- todo  回滚清算任务 -->
          <k-btn data-functype="SUBMIT" data-size="mini"  data-target="ta5002Grid" data-confirm  class="md-danger md-just-icon md-simple"
              data-descript="回滚已执行的清算任务" v-if="ta5003js.rollbackShowCondition3(scope.row.row)"  :data-handler="ta5003js.rollBackClear3">
            <md-icon>replay</md-icon>
          </k-btn>

        </template>
      </k-grid>
    </div>
  </div>
</template>


<script>
  import Tools from "@/utils/tools.js";
  import ta5003js from "../JS/M85003.js";

export default {
  name:"M85002",
  props: {
  },
  data() {
    return {
      workdate:"",
      displayWorkdate:"",
      $kgrid:null,
      clearInfos : [],
      selectFlowInfo:null,
      gridQueryData: {},
      table:"",
      flowList:["001","002","003","004","005","006","007","008","009"],
      //进度条的颜色
      progressColorArry :{
          '001' :'#1DA9EE',
          '002' :'#9C26B0',
          '003' :'#FF9901',
          '004' :'#F54336',
          '005' :'#3CD0E2',
          '006' :'#4CAF50',
          '007' :'#F56236',
          '008' :'#2958B3',
          '009' :'#F4C336',
      },
      //进度条中间图案数组， '001' 这个key 对应 任务信息的 simpleFlow
      taskColorArry:{ '001' :'center-circle task-color-day-start',
                      '002' :'center-circle task-color-prod-imp',
                      '003' :'center-circle task-color-prod-check',
                      '004' :'center-circle task-color-nav-exp',
                      '005' :'center-circle task-color-apply-imp',
                      '006' :'center-circle task-color-nav-imp',
                      '007' :'center-circle task-color-prod-clear',
                      '008' :'center-circle task-color-confirm-exp',
                      '009' :'center-circle task-color-day-end',
      },
      //进度条中间图案数组(任务执行百分百时)， '001' 这个key 对应 任务信息的 simpleFlow
      finishTaskColorArry:{ '001' :'center-circle-finish task-color-day-start',
                      '002' :'center-circle-finish task-color-prod-imp',
                      '003' :'center-circle-finish task-color-prod-check',
                      '004' :'center-circle-finish task-color-nav-exp',
                      '005' :'center-circle-finish task-color-apply-imp',
                      '006' :'center-circle-finish task-color-nav-imp',
                      '007' :'center-circle-finish task-color-prod-clear',
                      '008' :'center-circle-finish task-color-confirm-exp',
                      '009' :'center-circle-finish task-color-day-end',
      },
      //清算流程类型名称
      flowNameArry:{'001' :'日初','002' :'产品导入','003' :'产品复核','004' :'行情导出','005' :'申请导入','006' :'行情导入','007' :'产品清算',
                    '008' :'确认导出','009' :'日终',
      },
      //单独给日切、日终span调位置
      specialStyleArry:{'001' :'margin-left: 13px;',
                      '002' :'','003' :'','004' :'','005' :'','006' :'','007' :'','008' :'',
                      '009' :'margin-left: 13px;',
      }
    };
  },

  created(){

    this.getData();
    //获取定时任务ID，关闭页面要清除定时任务
    this.intervalid  = this.timer();
    this.ta5003js = ta5003js;
  },

  methods: {
    getData(){
        //查询清算流程数据
        this.httpUtil.comnQuery({
          action: 'T85002FlowInfo.findTa5002s',
          params: {},
        }).then(data => {
          this.clearInfos = [];

          //后台构造的返回结果，只有一条结果
          let result =   data.rows[0];

          //给系统工作日变量赋值
          this.workdate = result.workdate;
          this.displayWorkdate = Tools.formatDate(result.workdate);

          let tempClearFlow = {};
          for(let i=0;i< result.flowInfos.length;i++){
            let clearFlow = result.flowInfos[i];
            //计算百分比
            clearFlow.percentage = (clearFlow.successNum/clearFlow.clearCount) * 100;
            tempClearFlow[clearFlow.simpleFlow] =  clearFlow;
          }

          //没有数据的清算流程，赋初始对象
          for(let i=0;i< this.flowList.length;i++){
            if(tempClearFlow[this.flowList[i]]==undefined){
              this.clearInfos.push({
                simpleFlow:this.flowList[i],
                percentage:0,
                successNum:0,
                failNum:0,
                executingNum:0,
                noExecuteNum:0,
                taskDate: result.workdate,
              });
            }else{
              this.clearInfos.push(tempClearFlow[this.flowList[i]]);
            }
          }

        });
    },
    //点击图标，加载grid清算执行信息
    progressOnClick(clearInfo){
        this.$kgrid.load({'simpleFlow':clearInfo.simpleFlow,"queryTaskDate":this.workdate,});
        //标记选中的清算流程
        this.selectFlowInfo = clearInfo;
        //重置参数对象
        this.gridQueryData = {};

        this.table = this.flowNameArry[clearInfo.simpleFlow];

        //将界面定位到表格
        document.getElementById('ta5002Grid').scrollIntoView({
            block: 'start',
            inline: 'nearest',
            behavior: 'smooth'
        })

    },
    //设置定时器，定时查询清算数据
    timer(){
      return setInterval(()=>{
        this.getData();
      },5000);
    },
    whenAfterSuccess(){
      Tools.alert("执行成功");
    },
    queryChange(){
      if(this.selectFlowInfo != null){
        this.gridQueryData.taskDate = this.selectFlowInfo.taskDate;
        this.gridQueryData.simpleFlow = this.selectFlowInfo.simpleFlow;
        console.log(this.gridQueryData);
        this.$kgrid.load(this.gridQueryData);
      }
    }
  },
  watch:{
      'gridQueryData.prodCode'(value){
        this.queryChange();
      },
      'gridQueryData.targetCode'(value){
        this.queryChange();
      },
      'gridQueryData.execStatus'(value){
        this.queryChange();
      }
  },
  beforeDestroy () {
    clearInterval(this.intervalid);
  }

};

</script>

<style lang="scss" scoped>
.progress-position{
  z-index: 1;
  position: relative;
}

.center-circle{
  border-radius: 50%;
  width: 75px;
  height: 75px;
  margin: -87.8px 0 0 7.9px;
}

.center-circle-finish{
  border-radius: 50%;
  width: 89px;
  height: 89px;
  margin: -95.8px -1px 0px 0px;
  z-index: 999;
  position: relative;
}

.progress-div{
  width:100%;
  height: 246px;
}
.progress-div > div {
  display:inline-flex;
}

.el-progress--circle, .el-progress--dashboard {
  display: inline-block;
  // margin-left: -3.6px;
  border-radius: 90px;
}

.el-progress-day-start{
  box-shadow: 4px 6px 29px -5px #1DA9EE;
}
.el-progress-prod-imp{
  box-shadow: 4px 6px 29px -5px #9C26B0;
}
.el-progress-prod-check{
  box-shadow: 4px 6px 29px -5px #FF9901;
}
.el-progress-nav-exp{
  box-shadow: 4px 6px 29px -5px #F54336;
}
.el-progress-apply-imp{
  box-shadow: 4px 6px 29px -5px #3CD0E2;
}
.el-progress-nav-imp{
  box-shadow: 4px 6px 29px -5px #4CAF50;
}
.el-progress-prod-clear{
  box-shadow: 4px 6px 29px -5px #F56236;
}
.el-progress-confirm-exp{
  box-shadow: 4px 6px 29px -5px #2958B3;
}
.el-progress-day-end{
  box-shadow: 4px 6px 29px -5px #F4C336;
}

.connect-line{
    background-image: url(/static/images/clear/clear-line.png);
    background-repeat: no-repeat;
    background-position: center;
    width: 50px;
    height: 32px;
    display: inline-block;
    margin-top: 30px;
    margin-left: -2px;
}

.color-success{
  background:#5DC97A;
}
.color-fail{
  background:#F54336;
}
.color-executing{
  background:#0091FF;
}
.color-noExecute{
  background:#999999;
}
.circle{
  width:14px;
  height:14px;
  border-radius: 50%;
}
.circle + div{
  font-family: PingFangSC-Regular;
  font-size: 12px;
  color: #A1A1A1;
  line-height: 12px;
  margin-left: 10px;
}
.circle-div{
  margin: 40px 40px 0 0;
  float: right;
  display: inline-flex;
}

.task-color-day-start{
  background-color: #1DA9EE;
  background-image: url(/static/images/clear/day-start.png);
  background-repeat: no-repeat;
  background-position: center;
}
.task-color-prod-imp{
  background-color: #9C26B0;
  background-image: url(/static/images/clear/prod-imp.png);
  background-repeat: no-repeat;
  background-position: center;
}
.task-color-prod-check{
  background-color: #FF9901;
  background-image: url(/static/images/clear/prod-check.png);
  background-repeat: no-repeat;
  background-position: center;
}
.task-color-nav-exp{
  background-color: #F54336;
  background-image: url(/static/images/clear/nav-exp.png);
  background-repeat: no-repeat;
  background-position: center;
}
.task-color-apply-imp{
  background-color: #3CD0E2;
  background-image: url(/static/images/clear/apply-imp.png);
  background-repeat: no-repeat;
  background-position: center;
}
.task-color-nav-imp{
  background-color: #4CAF50;
  background-image: url(/static/images/clear/nav-imp.png);
  background-repeat: no-repeat;
  background-position: center;
}
.task-color-prod-clear{
  background-color: #F56236;
  background-image: url(/static/images/clear/prod-clear.png);
  background-repeat: no-repeat;
  background-position: center;
}
.task-color-confirm-exp{
  background-color: #2958B3;
  background-image: url(/static/images/clear/confirm-exp.png);
  background-repeat: no-repeat;
  background-position: center;
}
.task-color-day-end{
  background-color: #F4C336;
  background-image: url(/static/images/clear/day-end.png);
  background-repeat: no-repeat;
  background-position: center;
}

.progress-detail-circle{
  width:16.6px;
  height:16.6px;
  border-radius: 50%;
  display:inline-flex;
  margin-left: 2px;
}
.progress-detail-circle > span{
  font-family: PingFangSC-Medium;
  font-size: 10px;
  color: #FFFFFF;
  text-align: center;
  line-height: 8.45px;
  margin: 0 auto;
  display: flex;
  align-items: center;
}

.task-type-div{
  margin-top: 25px;
  text-align: center;
}
.task-type-div > span{
  font-family: PingFangSC-Medium;
  font-size: 14px;
  color: #3B4858;
  margin-top: 54px;
  font-weight: 600;
}

.head-date{
  display: inline-flex;
  margin: 30px 0 0 10px;
  font-family: PingFangSC-Regular;
  font-size: 18px;
  font-weight: 400;
}
.progress-parents{
  width: 100%;
  text-align: center;
  margin-top: 25px;
}

.grid-div{
  height: 350px;
  width: 100%;
  position: relative;
  z-index: 0;
  margin-top: -85px;
}
.btn-div{
  text-align: center;
  height:20px;
  margin-top: 10px;
}
.progressbar-parents{
  cursor: pointer;
  margin-left: -3px;
  height: 89px;
}
.progressbar-parents > div:first-child{
  box-shadow: 0 12px 20px -10px rgba(154, 154, 157, 0.28), 0 4px 20px 0px rgba(0,0,0,0.12), 0 7px 8px -5px rgba(182, 182, 182, 0.2);
}

.head-div{
  width: 100%;
    height: 100px;
}

.public-width{
  width:100%;
  min-width: 1272px;
}

.query-title-div{
  margin: 40px 0px 0px 78px;
  position: relative;
  z-index: 1;
  height: 41px;
}

::v-deep .el-table--scrollable-x .el-table__body-wrapper{
  overflow-x: hidden;
}

.showTaskNameSpan{
  width: 80px;
  margin-top: 38px;
  font-family: PingFangSC-Medium;
  font-size: 18px;
  color: #3B4858;
  font-weight: 400;
}

</style>
