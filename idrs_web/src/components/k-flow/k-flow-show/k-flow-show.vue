<template>
  <div>
    <!--   审批流程START     -->
<!--    <div style="margin-bottom: 20px;margin-top: 20px">审批流程</div>-->
    <h4>审批流程</h4>
    <div class="main_div" v-for="(item,i) in flowDataList">
      <div class="flow_div">
        <img class="img_div" v-if="item.result=='已发起'||item.result=='已同意'||item.result=='自动审批'||item.result=='已转交'||item.result=='已加签'" :src="normalImg" />
        <img class="img_div" v-if="item.result=='审批中'" :src="approvalingImg" />
        <img class="img_div" v-if="item.result=='待审批'" :src="waitingImg" />
        <img class="img_div" v-if="item.result=='已确认'||item.result=='未确认'" :src="sendOtherImg" />
        <img class="img_div" v-if="item.result=='已拒绝'||item.result=='已撤销'" :src="rejectedImg" />
<!--        <img class="img_div" v-if="item.result=='5'" :src="endImg" />-->
      </div>
      <div class="info_div">
            <span>
              <span>{{item.operator}} ({{item.result}})
              </span>
              <span style="position: relative;left:40%;" v-if="item.nodeLevel!='999'">
                {{formatDateTimeStr(item.dateTime)}}
              </span>
              <span style="position: relative;left:40%;" v-else>
                {{item.dateTime}}
              </span>
              <span style="white-space: pre;display: block;width: 100px">{{item.nodeName}}</span>
              <div v-if="item.opinion && item.result=='已发起'" style="min-width:500px;border-left:2px solid gray;padding-left:40px;
                position:relative;left:-15px;top:10px;margin-bottom: 20px;">
                <span style="word-break: break-all;overflow: hidden;text-overflow: ellipsis;display: -webkit-box;
                -webkit-box-orient: vertical;-webkit-line-clamp: 13;">经办意见：{{item.opinion}}</span>
              </div>
              <div v-if="item.opinion && item.result != '已发起'" style="min-width:500px;border-left:2px solid gray;padding-left:40px;
                position:relative;left:-15px;top:10px;margin-bottom: 20px;">
                <span style="word-break: break-all;overflow: hidden;text-overflow: ellipsis;display: -webkit-box;
                -webkit-box-orient: vertical;-webkit-line-clamp: 13;">审批意见：{{item.opinion}}</span>
              </div>

            </span>
      </div>
<!--      <div class="line_div">
        <img class="img_div" v-if="i != flowDataList.length-1" :src="lineImg">
      </div>-->
      <div v-if="i != flowDataList.length-1" style="height: 30px;border-left: 2px solid gray;position: relative;left:25px;clear:both;width: 2px;">

      </div>
<!--      <div class="time_div">
            <span v-if="item.nodeLevel!='999'">
              {{formatDateTimeStr(item.dateTime)}}
            </span>
            <span v-else>
              {{item.dateTime}}
            </span>
      </div>-->
    </div>
    <!--   审批流程END     -->
    <div  style="height: 30px;position: relative;left:25px;clear:both;">

    </div>
  </div>
</template>

<script>

import approvalingImg from "@/assets/svg/flow/approvaling.svg";
import endImg from "@/assets/svg/flow/end.svg";
import lineImg from "@/assets/svg/flow/line.svg";
import normalImg from "@/assets/svg/flow/normal.svg";
import sendOtherImg from "@/assets/svg/flow/send.svg"
import waitingImg from "@/assets/svg/flow/waiting.svg"
import rejectedImg from "@/assets/svg/flow/rejected.svg"
import Tools from "@/utils/tools";

export default {
  name: "k-flow-show",
  props:{
    flowDataList:{
      type:Array,
      required:true
    }
  },
  data(){
    return{
      //线
      lineImg: lineImg,
      //审批中
      approvalingImg:approvalingImg,
      //结束
      endImg:endImg,
      //正常流程
      normalImg:normalImg,
      //超送
      sendOtherImg:sendOtherImg,
      //待审批
      waitingImg:waitingImg,
      //已拒绝
      rejectedImg:rejectedImg,
    }
  },
  methods:{
    //处理 20220124 124523 格式时间
      formatDateTimeStr (datetime) {
        if (!datetime) {
          return "";
        }
        let result = datetime.substring(0, 4) + "-" + datetime.substring(4, 6) + "-" + datetime.substring(6, 8) + " "+datetime.substring(9, 11) + ":" + datetime.substring(11, 13) + ":" + datetime.substring(13, 15);
        return result;
      }
  }
}
</script>

<style scoped>

.main_div {
  width: 80%;
  margin-left: 20%;
}
.flow_div {
  float: left;
  height: 50%;
  width: 50px;
}
.info_div {
  position: relative;
  top:10px;
  left:20px;
  margin-left: 20px
}
.line_div {
  float:left;
  width: 50px;
  height: 40px;
  position:absolute;
  margin-top: 50px;
}
.time_div {
  float: right;
  width: 20%;

  text-align: center;
  margin-right: 30%;
}
.img_div{
  width: 50px;
  height: 50px;
}
</style>
