<template>
  <md-card class="head-div " id="mainBody">
    <div>
      <md-card-header class="md-card-header-text md-card-header-green" style="opacity: 0">
        <div class="card-icon" :style="iconStyle">
          <md-icon md-src="/static/svg/flow.svg"></md-icon>
        </div>
      </md-card-header>
      <div class="operate">
        <div class="left">
          <div class="" style="width:100px">选择清算日期:</div>
          <k-field-date class="date" style="width:150px" v-model="dateValue" @data-on-change="change" data-type="date"
                      data-date-format="yyyy-MM-dd" data-value-format="yyyy-MM-dd"></k-field-date>

          <div class="" style="width:150px">清算执行状态: <strong>{{ isAuto ? '自动' : '手动' }}</strong></div>

          <div v-if="isAuto" class="">
            <k-btn class="md-warning" data-functype="SUBMIT" data-action="DpsSysParam.autoExec"
                  data-params="{'paravalue':'0'}"
                  :data-confirm="true" :data-after-success="()=>this.isAuto=false " style="z-index:3">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>关闭
            </k-btn>

          </div>

          <div v-else class="">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="DpsSysParam.autoExec"
                  data-params="{'paravalue':'1'}"
                  :data-confirm="true" :data-after-success="()=>this.isAuto=true " style="z-index:3">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>
              开启
            </k-btn>
          </div>
          <k-btn class="md-success" :data-handler="handleRefresh" style="z-index:3">
            <md-icon md-src="/static/svg/icon/reset.svg"></md-icon>刷新
          </k-btn>
        </div>
        <div class="">
          <div class="circleIcon text">
            <div class="circle"
                style="background-color: #5DC97A"></div>
            执行成功
          </div>
          <div class="circleIcon text">
            <div class="circle"
                style="background-color: #F54336"></div>
            执行失败
          </div>
          <div class="circleIcon text">
            <div class="circle"
                style="background-color: #0091FF"></div>
            执行中
          </div>
          <div class="circleIcon text">
            <div class="circle"
                style="background-color: #999999"></div>
            未执行
          </div>
        </div>
      </div>
    </div>
    <!--展示div-->
    <div class="mainBody">
      <!--时序框-->
      <div class="clear timeBox">
        <div class="bigTitle">时序</div>
        <div class="row1" v-for="(itemsF,indexF) in formData.firstDay" :key="itemsF.time">
          <div class="time">{{ itemsF.time }}</div>
          <div class="clearGroupList">
            <div class="middleLine"></div>
            <ClearGroup class="item1" :count="count" v-for="(clearGroupF,iF) in itemsF.rows" :dateValue="taskDate"
                        :key="clearGroupF.taskGroup" :clearGroupDetail="clearGroupDetail"
                        :clearGroup="clearGroupF"></ClearGroup>
          </div>
        </div>
        <!--零点线-->
        <div class="line">
          <div class="lineOne">零点线</div>
          <div class="lineTwo"></div>
          <hr class="lineThree">
          <div class="lineFour"></div>
          <div class="linefive"></div>
        </div>
        <div class="row2" v-for="(itemsS,indexS) in formData.secondDay" :key="itemsS.time">
          <div class="time">{{ itemsS.time }}</div>
          <div class="clearGroupList">
            <div class="middleLine"></div>
            <ClearGroup class="item2" :count="count" v-for="(clearGroupS,iS) in itemsS.rows" :dateValue="taskDate"
                        :key="clearGroupS.taskGroup" :clearGroupDetail="clearGroupDetail"
                        :clearGroup="clearGroupS"></ClearGroup>
          </div>
        </div>
      </div>
      <!--非时序框-->
      <div class="clear notTimeBox">
        <div class="bigTitle">非时序</div>
        <div class="notTimeList">
          <ClearGroup class="item3" :count="count" v-for="(item,index) in formData.notTimeGroup" :dateValue="taskDate"
                      :key="item.taskGroup" :clearGroupDetail="clearGroupDetail"
                      :clearGroup="item"></ClearGroup>
        </div>
      </div>
      <div class="G6" id="mountNode"></div>
    </div>
    <k-popup data-width-percent="70%" ref="detail" class="abow_dialog"  style="margin-left: 140px ;margin-top: 80px">
      <detail :clearGroup="clearGroup" ref="detailRef" show-close="false" :initExpand="initExpand"
              :handleExpandChange="handleExpandChange" :handleClose='handleClose'></detail>
    </k-popup>
  </md-card>
</template>

<script>
import ClearGroup from "@/pages/batch/TA5014-ClearGroup";
import {Graph, Node, Edge, EdgeView} from '@antv/x6';
import moment from 'moment';
import detail from './TA5014-detail.vue';
import Tools from "@/utils/tools";

export default {
  name: "TA5014",
  components: {
    detail,
    ClearGroup,
  },
  data() {
    return {
      selectDate: '',
      dateValue: '',
      formData: {},
      devGraph: '',
      intervalid: '',
      count: 0,
      isAuto: false,
      taskDate: '',
      clearGroup: {},
      expandArr: {},
      initExpand: [],
      map: {},
      isOpenDetailPopup: false,
    }
  },
  methods: {
    handleRefresh() {
      this.change(this.dateValue);
    },
    clearGroupDetail(taskGroup, taskGroupName, execTaskType) {
      this.clearGroup.taskGroup = taskGroup;
      this.clearGroup.taskGroupName = taskGroupName;
      this.clearGroup.execTaskType = execTaskType;
      this.clearGroup.taskDate = this.taskDate;
      if (this.expandArr[taskGroup] == undefined) {
        this.expandArr[taskGroup] = [];
      }
      this.initExpand = this.expandArr[taskGroup];
      this.pClearIntervalid();
      this.isOpenDetailPopup = true;
      this.$refs['detail'].popup();
    },
    handleClose() {
      this.isOpenDetailPopup = false;
      this.intervalid = this.timer();
    },
    pClearIntervalid() {
      clearInterval(this.intervalid);
    },
    handleExpandChange(data, isOpen) {
      var initExpand = this.expandArr[this.clearGroup.taskGroup];
      if (isOpen) {
        initExpand.push(data.execGridId);
      } else {
        var execGridId = data.execGridId;
        var index = initExpand.indexOf(execGridId);
        if (index === 0) {
          initExpand.shift();
        } else if (index === (initExpand.length - 1)) {
          initExpand.pop();
        } else {
          initExpand = initExpand.slice(0, index).concat(initExpand.slice(index + 1, initExpand.length));
        }
      }
      this.initExpand = initExpand;
    },
    change(value) {
      if (value) {
        this.taskDate = moment(value, "YYYY-MM-DD").format("YYYYMMDD");
      }
      this.httpUtil.ajax({
        url: '/commQuery/Ta5014/queryClearGroup.json',
        params: {"queryTaskDate": this.taskDate}
      }).then(result => {
        this.formData = {};
        if (this.devGraph !== '') {
          this.devGraph.clearCells();
        }
        this.formData = result;
        this.initG6();
        this.count++;
      })
    },
    findAutoExec() {
      this.httpUtil.comnQuery({
        action: "DpsSysParam.findAutoExec",
        params: {}
      }).then(data => {
        if (data.rows.length > 0) {
          this.isAuto = true;
        } else {
          this.isAuto = false;
        }
      })
    },
    initG6() {
      this.map = {};
      let graph = new Graph({
        container: document.getElementById('mountNode'),
        width: document.getElementById("mainBody").clientWidth,
        height: 200 + (this.formData.firstDay.length + this.formData.secondDay.length) * 171,
        grid: false,
        interacting: {
          nodeMovable: false
        }
      })

      //第一天节点布置
      for (var i = 0; i < this.formData.firstDay.length; i++) {
        for (var j = 0; j < this.formData.firstDay[i].rows.length; j++) {
          var temp = this.formData.firstDay[i].rows[j];
          this.map[temp.taskGroup] = this.formData.firstDay[i].time;
          graph.addNode({
            id: temp.taskGroup,
            x: 168 + j * 272,
            y: 154 + 170 * i,
            width: 155,
            height: 109,
            attrs: {
              body: {
                fill: '#ffffff',
                stroke: '#ffffff',
              },
            },
          })

          if (temp.lastTaskGroup != "") {
            var tempArr = temp.lastTaskGroup.split(",");
            for (var z = 0; z < tempArr.length; z++) {
              graph.addEdge({
                source: tempArr[z],
                target: temp.taskGroup,
                router: {
                  name: 'metro',
                  args: {
                    startDirections: [(this.map[tempArr[z]] == this.map[temp.taskGroup]) ? 'right' : 'bottom'],
                    endDirections: [(this.map[tempArr[z]] == this.map[temp.taskGroup]) ? 'left' : 'top'],
                  },
                },
                attrs: {
                  line: {
                    stroke: '#00BCD4',
                  },
                },
              })
            }
          }
        }
      }
      /*第二天节点布置*/
      for (var i = 0; i < this.formData.secondDay.length; i++) {
        for (var j = 0; j < (this.formData.secondDay[i].rows).length; j++) {
          temp = this.formData.secondDay[i].rows[j];
          this.map[temp.taskGroup] = this.formData.secondDay[i].time;
          var len = (this.formData.firstDay.length - 1) * 170 + 154 + 198;
          graph.addNode({
            id: temp.taskGroup,
            x: 168 + j * 272,
            y: len + 170 * i,
            width: 155,
            height: 109,
            attrs: {
              body: {
                fill: '#ffffff',
                stroke: '#ffffff',
              },
            },
          });
          if (temp.lastTaskGroup != "") {
            var tempArr = temp.lastTaskGroup.split(",");
            for (var z = 0; z < tempArr.length; z++) {
              graph.addEdge({
                source: tempArr[z],
                target: temp.taskGroup,
                router: {
                  name: 'metro',
                  args: {
                    startDirections: [(this.map[tempArr[z]] == this.map[temp.taskGroup]) ? 'right' : 'bottom'],
                    endDirections: [(this.map[tempArr[z]] == this.map[temp.taskGroup]) ? 'left' : 'top'],
                  },
                },
                attrs: {
                  line: {
                    stroke: '#00BCD4',
                  },
                },
              })
            }
          }
        }
      }
      this.devGraph = graph;
    }
    ,
    //设置定时器，定时查询清算数据
    timer() {
      return setInterval(() => {
        this.change();
      }, 3000);
    }
    ,
  },
  created() {

  },
  beforeRouteLeave(to,form,next) {
    if (this.isOpenDetailPopup && this.$refs.detailRef) {
      this.$refs.detailRef.destroyedTimer();
    }else {
      clearInterval(this.intervalid);
    }
    next();
  },
  beforeDestroy() {
    if (this.isOpenDetailPopup && this.$refs.detailRef) {
      this.$refs.detailRef.destroyedTimer();
    }else {
      clearInterval(this.intervalid);
    }
  },
  activated(){
    if (this.isOpenDetailPopup) {
      this.$refs.detailRef.setTimer();
    }else {
      this.handleClose();
    }
  },
  mounted() {
    if (this.taskDate == undefined || this.taskDate == '') {
      this.httpUtil.sysDate().then(res => {
        if (res) {
          this.taskDate = res;
          this.dateValue = moment(res).format("yyyy-MM-DD");
          this.taskDate = this.dateValue.slice(0,4)+this.dateValue.slice(5,7)+this.dateValue.slice(8,10)
          this.change();
        }
      })
      // this.httpUtil.sysparam('10004', '0').then(res => {
      //   if (res) {
      //     this.taskDate = res;
      //     this.dateValue = moment(res).format("yyyy-MM-DD");
      //     this.change();
      //   }
      // })
    } else {
      this.dateValue = moment(this.taskDate).format("yyyy-MM-DD");
      this.change();
    }
    //页面加载去查询自动执行的状态
    this.findAutoExec();
  },
  computed: {
    iconStyle() {
      let iconStyle = {};
      iconStyle.background = this.$store.state.system.cardBackground
      return iconStyle;
    }
  }
}
</script>

<style scoped>

.operate{
  display: flex;
  flex-direction: row;
  flex-wrap: nowrap;
  align-content: center;
  align-items: center;
  justify-content: space-between;
}

.operate .left {
  display: flex;
  flex-direction: row;
  align-items: center;
  flex-wrap: nowrap;
  align-content: center;
}

.head-div {
  width: 100%;
  color: #999999;

}

.date >>> .el-input__inner {
  border: 0;
}

.date {
  border-bottom: 1px solid #b6b6b6;
  width: 150px;
  margin-left: 10px;
  z-index: 2;
}

.dateSelect {
  left: 130px;
  display: inline-block;
  margin-top: 8px;
}

.circle {
  width: 14px;
  height: 14px;
  border-radius: 50%;
  background-color: #5DC97A;
  display: inline-block;
  transform: translateY(2px);
}

.is-auto {
  position: absolute;
  left: 470px;
  top: 5px;
  z-index: 2;
}

#mountNode {
  position: absolute !important;
  top: -68px;
  z-index: 1;
}

.text {
  /*文字不能被选中*/
  -webkit-touch-callout: none;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
  cursor: auto;
}

.iconText {
  float: right;
  display: inline-block;
  margin-top: -60px;
}

.circleIcon {
  display: inline-block;
  margin: 25px 5px;
}

.clear {
  margin-top: 25px;
  display: inline-block;
}

.notTimeBox {
  width: 12.5%;
  position: fixed;
  background: #F5F5F5;
  border-radius: 6px;
  right: 2%;
  margin-top: 20px;
  z-index: 2;
}

.timeBox {
  width: 86%;
  position: relative;
}

.bigTitle {
  padding-left: 20px;
  width: 74px;
  height: 18px;
  font-size: 18px;
  margin-bottom: 10px;
  color: #3B3B3B;
  line-height: 26px;
  margin-top: 8px;
}

.time {
  display: inline-block;
  width: 130px;
  height: 50px;
  font-size: 30px;
  color: #323232;
  line-height: 50px;
  position: relative;
  transform: translateY(-50%);
  top: 50%;
  padding-left: 10.5px;
  border-left: 2px solid #00BCD4;
}

.row1, .row2 {
  margin-top: 10px;
  margin-bottom: 10px;
  width: 100%;
  height: 160px;
  padding-left: 20px;
  position: relative;
}

.clearGroupList {
  display: inline-block;
  position: relative;
  width: 100%;
  left: 70px;
  top: -3px;
  padding-left: 20px;
}

.notTimeList {
  position: relative;
  top: 0;
}

.item1, .item2 {
  display: inline-block;
  margin: 0 58px;
  z-index: 2;
}

.item3 {
  width: 157px;
  margin: 0 auto;
  margin-top: 50px;
  margin-bottom: 20px;
  display: block;
}

#mainBody {
  position: unset!important;
}

.mainBody {
  width: 100%;
  padding-bottom: 30px;
  position: relative!important;
}

.line {
  position: relative;
  height: 18px;
}

.lineOne {
  display: inline-block;
  width: 50.5px;
  height: 18px;
  background-color: #6569FD;
  line-height: 18px;
  color: #ffffff;
  position: absolute;
  padding-left: 5px;
  border-radius: 2px 0px 0px 2px;
}

.lineTwo {
  width: 10px;
  height: 18px;
  display: inline-block;
  position: relative;
  left: 50.5px;
  border-style: solid;
  border-width: 8px 10px 10px 10px;
  border-color: transparent transparent transparent #6569FD;
}

.lineThree {
  display: inline-block;
  width: 96%;
  position: absolute;
  left: 59.5px;
  border: 1px solid #6569FD;
}

.lineFour {
  display: inline-block;
  border-style: solid;
  border-width: 8px 10px 10px 10px;
  border-color: transparent #6569FD transparent transparent;
  position: absolute;
  right: 4px;
}

.linefive {
  display: inline-block;
  width: 8px;
  height: 18px;
  background: #6569FD;
  border-radius: 2px 0px 0px 2px;
  position: absolute;
  right: -4px;
}

.autoBtn {
  position: relative;
  left: 400px;
  margin-top: -40px;
  z-index: 3;
}

.autoText {
  left: 455px;
  margin-top: 8px;
  margin-left: 10px;
}

.middleLine {
  background-color: #DBDBDB;
  height: 1px;
  width: 90%;
  position: absolute;
  left: 50px;
  top: 35px;
}
</style>

<style>

#mainBody .el-dialog__body{
  padding-top: 25px!important;
}

svg rect:hover {
  cursor: default;
}

#mainBody>.abow_dialog>.el-dialog {
  margin-top: 2% !important;
}

.abow_dialog {
  overflow-x: hidden;
}
</style>
