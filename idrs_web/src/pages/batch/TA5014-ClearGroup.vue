<template>
  <div id="body">
    <div class="bg-bottom"
         @click="clearGroupDetail(clearGroup.taskGroup,clearGroup.taskGroupName,clearGroup.execTaskType)">
      <div class="bg-top" :style="'background:'+bgcolor">
        <!--右上角的小A-->
        <div class="lt-A">
          <div class="AFont text">A</div>
        </div>
        <!---->
        <span class="title">
          <img class="titleIcon"  v-if="this.clearGroup.execTaskType == 1" src="/static/images/clear/settingIcon.png">
          <img class="titleIcon"  v-if="this.clearGroup.execTaskType == 2" src="/static/images/clear/prodIcon.png">
          <img class="titleIcon"  v-if="this.clearGroup.execTaskType == 3||this.clearGroup.execTaskType == 4"
               src="/static/images/clear/distributorIcon.png">
          <img class="titleIcon"  v-if="this.clearGroup.execTaskType == 5||this.clearGroup.execTaskType == 6"
               src="/static/images/clear/ZGIcon.png">
          <el-tooltip effect="dark" :content="clearGroup.taskGroupName" placement="top-start">
            <span class="text titleFont iconText">{{ clearGroup.taskGroupName | filterName(this.status) }}</span>
          </el-tooltip>
        </span>
        <div class="statusBtn" @click.stop="execteError(clearGroup.taskGroup)" v-if="this.status==1">
          <img style="width:15px;height: 15px " src="/static/svg/run1.svg"/>
        </div>
        <div class="statusBtn" v-if="this.status==2">
          <img style="width:35px;height: 5px " src="/static/images/clear/executing.png"/>
        </div>
        <div class="statusBtn" @click.stop="execting(clearGroup.taskGroup)" v-if="this.status==3">
          <img style="width:15px;height: 15px " src="/static/svg/run1.svg"/>
        </div>
        <img v-if="this.status==4" class="statusBtn" src="/static/images/create/success.png"/>
        <img  class="statusIconB" @click.stop="execteSkip(clearGroup.taskGroup)" src="/static/images/create/skip.png"/>
        <img  class="statusIconR" @click.stop="execteRollBack(clearGroup.taskGroup)"src="/static/images/create/return3.png"/>
      </div>
      <!--下面的四个小点-->
      <div class="dot" style="background-color: #5EC979;left: 23px;">
        <div style="color: #5EC979;" class="lNumber text">{{ clearGroup.success }}</div>
      </div>
      <div class="dot" style="background: #F64336;left: 57px">
        <div style="color: #F64336;" class="lNumber text">{{ clearGroup.failed }}</div>
      </div>
      <div class="dot" style="background: #0092FF;left: 91px">
        <div style="color: #0092FF;" class="lNumber text">{{ clearGroup.executing }}</div>
      </div>
      <div class="dot" style="background: #999999;left: 125px">
        <div style="color: #999999;" class="lNumber text">{{ parseInt(clearGroup.nonExecution)+parseInt(clearGroup.nonRegistry) }}</div>
      </div>
    </div>
  </div>
</template>

<script>
import moment from 'moment';
import Tools from "@/utils/tools";
import detail from './TA5014-detail.vue';

export default {
  components: {
    detail
  },
  props: {
    clearGroup: {
      type: Object
    },
    dateValue: {
      type: String
    },
    count: {
      type: Number,
      default: 0
    },
    clearGroupDetail: Function
  },
  data() {
    return {
      up: -23,
      bgcolor: '#5EC979',
      success: true,
      status: 0,  //0为未注册,1为执行失败,2为执行中,3为未执行,4为执行成功
      initExpand: []
    }
  },
  methods: {
    changeStatus: function (s) {

    },
    execting(taskGroup) {
      Tools.confirm(() => {
          this.httpUtil.ajax({
            url: "/server/json/DpsApp/C998", // 调用后台，采用格式为：/server/服务名/交易接口
            params: {"taskGroup": taskGroup, "moduleid": "p", "taskDate": this.dateValue}
          }).then(data => {
            console.log(data);
            let rows = data.rows;
          })
        },
        "确定执行该清算组任务?"
      )
    },
    init() {
      if (this.clearGroup.failed === '0' && this.clearGroup.executing === '0' && this.clearGroup.nonExecution === '0' && this.clearGroup.success === '0') {
        this.status = 0;
        this.bgcolor = '#C0C0C0';
      } else {
        if (this.clearGroup.failed > 0) {
          this.status = 1;
          this.bgcolor = '#F64336';
        } else if (this.clearGroup.executing > 0) {
          this.status = 2;
          this.bgcolor = '#0092FF';
        } else if (this.clearGroup.nonExecution > 0) {
          this.status = 3;
          this.bgcolor = '#C0C0C0';
        } else if(this.clearGroup.nonRegistry > 0){
          this.status = 0;
          this.bgcolor = '#C0C0C0';
        }else {
          this.status = 4;
          this.bgcolor = '#5EC979';
        }
      }
    },
    execteError(taskGroup) {
      Tools.confirm(() => {
          this.httpUtil.ajax({
            url: "/commQuery/Ta5014/queryErrorTaskByTaskGroup.json",
            params: {"queryTaskDate": this.dateValue, "taskGroup": taskGroup}
          }).then(res => {
            res.rows.forEach(taskId => {
              this.httpUtil.ajax({
                url: "/server/json/DpsApp/C999", // 调用后台，采用格式为：/server/服务名/交易接口
                params: {"taskGroup": taskGroup, "moduleid": "p", "taskDate": this.dateValue, "taskId": taskId}
              }).then(data => {
                console.log(data);
                let rows = data.rows;
              })
            })
          })
        },
        "存在失败任务,确定执行该清算组任务?"
      )
    },
    execteSkip(taskGroup) {
      Tools.confirm(() => {
        let i =0;
            this.httpUtil.ajax({
              url: "/commQuery/Ta5014/queryTaskByTaskGroup.json",
              params: {"queryTaskDate": this.dateValue, "taskGroup": taskGroup}
            }).then(res => {
              res.rows.forEach(taskExecid => {
                this.httpUtil.comnQuery({
                  action: "/Ta5014Detail.updateStatusSkip.json", // 调用后台，采用格式为：/server/服务名/交易接口
                  params: {"taskExecid": taskExecid}
                }).then(data => {
                  i++;
                  if(res.rows.length==i){
                    Tools.alert("跳过清算组任务成功！");
                  }
                })
              })
            })
          },
          "确定跳过清算组任务?"
      )
    },
    execteRollBack(taskGroup) {
      let j =0;
      Tools.confirm(() => {
            this.httpUtil.ajax({
              url: "/commQuery/Ta5014/queryTaskByTaskGroup.json",
              params: {"queryTaskDate": this.dateValue, "taskGroup": taskGroup}
            }).then(res => {
              res.rows.forEach(taskExecid => {
                this.httpUtil.comnQuery({
                  action: "/Ta5014Detail.rollBackClearTask.json", // 调用后台，采用格式为：/server/服务名/交易接口
                  params: {"taskExecid": taskExecid}
                }).then(data => {
                  j++;
                  if(res.rows.length==j){
                    Tools.alert("回滚清算组任务成功！");
                  }
                })
              })
            })
          },
          "确定回滚清算组任务?"
      )
    }
  },
  mounted() {
    this.init();
  },
  watch: {
    count() {
      this.init();
    }
  },
  filters: {
    filterName(name, status) {
      if(name.length>6){
        return name.substr(0, 6) + "...";
      }else {
        return name;
      }
    }
  }
}
</script>

<style scoped>
.iconText {
  vertical-align: top;
}

.bg-bottom {
  width: 157px;
  height: 86px;
  background: #FFFFFF;
  border: 1px solid #EDEDED;
  box-shadow: 0px 2px 3px 0px rgba(217, 217, 217, 0.38);
  border-radius: 3px;
  position: relative;
  top: 0;
  perspective: 700px;
}

.bg-bottom:hover {
  background-color: #F2F2F2;
  box-shadow: 1px 1px 12px #000000;
  cursor: pointer;
}

.bg-bottom:hover .bg-top {
  transform: translateZ(60px);
  left: 9px;
}

* {
  margin: 0;
  padding: 0;
}

.mouseHover:hover img {
  /*如果爆红,纯属于idea误报错误,不需要搭理*/
  content: url("/static/svg/run1.svg");
}

#body {
  position: relative;
}

.bg-top {
  width: 137px;
  height: 72px;
  box-shadow: 0px 1px 4px 0px rgba(237, 184, 198, 0.74);
  border-radius: 3px;
  top: -23px;
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
}

.dot {
  width: 8px;
  height: 8px;
  border: 0;
  border-radius: 50%;
  position: absolute;
  bottom: 22px;
}

.lNumber {
  border: 0;
  font-size: 12px;
  position: absolute;
  left: 50%;

  transform: translateX(-50%);
  top: 8px;
}

.lt-A {
  width: 19px;
  height: 15px;
  background: #FFFFFF;
  border: 1px solid #5EC979;
  border-radius: 3px;
  position: absolute;
  top: 0;
  right: 0;
}

.AFont {
  width: 10px;
  height: 9px;
  font-size: 12px;
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  font-style: italic;
  color: #00BCD4;
  line-height: 14px;
}

.titleIcon {
  transform: translateY(1px);
  width: 13px;
  height: 15px;
}

.titleFont {
  color: white;
  font-size: 15px;
  line-height: 26px;
}

.title {
  height: 16px;
  width: 135px;
  position: absolute;
  top: 12.5px;
  left: 50%;
  transform: translateX(-50%);
  text-align: center;
}

.statusIconS {
  width: 21px;
  height: 21px;
  position: absolute;
  left: 30%;
  transform: translateX(-50%);
  top: 42px;
}

.statusIconB {
  width: 21px;
  height: 21px;
  background-color: white;
  border-radius: 3px;
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  top: 42px;
  text-align: center;
  z-index: 4;
  margin: auto auto;
}

.statusIconR {
  width: 21px;
  height: 21px;
  background-color: white;
  border-radius: 3px;
  position: absolute;
  left: 70%;
  transform: translateX(-50%);
  top: 42px;
  text-align: center;
  z-index: 4;
  margin: auto auto;
}

.statusBtn {
  width: 21px;
  height: 21px;
  background-color: white;
  border-radius: 3px;
  position: absolute;
  left: 30%;
  transform: translateX(-50%);
  top: 42px;
  text-align: center;
  z-index: 4;
  margin: auto auto;
}

.text {
  moz-user-select: -moz-none;
  -moz-user-select: none;
  -o-user-select: none;
  -khtml-user-select: none;
  -webkit-user-select: none;
  -ms-user-select: none;
  user-select: none;
}
</style>
