<template>
  <div>
    <div>
      <table class="task-table">
        <tr>
          <td>
            <div class="btn-title">
              <span style="head-span">选择产品组件</span>
            </div>
          </td>
          <td>
            <div class="btn-title" style="padding: 0px;">
              <span style="head-span">预览效果展示</span>
              <div class="head-tips">i</div>
              <span style="font-family: PingFangSC-Regular;font-size: 12px;color: #999999;">每个版块内可拖动组件调整执行顺序</span>
            </div>
          </td>
        </tr>
        <tr>
          <td>
            <ul class="add-task-ul">
              <div class="btn-bottom1">
                <div class="color-head" style="background: #4EA6EC;"></div>
                <div class="color-head-span">募集</div>
              </div>
              <li v-for="taskInfo in taskInfoMJ" :key="taskInfo.taskId">
                <div class="btn-bottom2" >
                  <el-button round size="small" @click="btnOnClick(taskInfo)" :title="taskInfo.taskName"
                             :class="{'btn-color-MJ' : taskInfo.inTaskSet === '1'}"> {{taskInfo.taskName}} </el-button>
                </div>
              </li>
            </ul>
          </td>
          <td>
            <div class="td-div">
              <div class="cycle-div border-MJ" :style="{'height': cycleHeadHeight_MJ +'px'}">
                <div class = "cycle-title">
                  <span>募集</span>
                </div>
                <div class = "cycle-detail">
                  <ul class="task-ul">
                    <draggable ref="draggableObj" v-model="prodTaskSetMJ" @update="datadragEnd" :options="{animation:500}" >
                      <transition-group>
                        <li v-for="(taskInfo,index) in prodTaskSetMJ" :key="taskInfo.taskId">
                          <div class="task-div" :title="taskInfo.taskName">
                            <div class="box">
                              <span>{{ (Array(2).join(0) + parseInt(index+1)).slice(-2) }}</span>
                            </div>
                            <div class="task-box task-box-MJ">
                              <span>{{taskInfo.taskName}}</span>
                            </div>
                          </div>
                        </li>
                      </transition-group>
                    </draggable>
                  </ul>
                </div>
              </div>
              <div class="cycle-head-arrowhead arrowhead_mj"></div>
            </div>
          </td>
        </tr>
        <tr>
          <td>
            <ul class="add-task-ul">
              <div class="btn-bottom1">
                <div class="color-head" style="background: #5ACBB7;"></div>
                <div class="color-head-span">成立</div>
              </div>
              <li v-for="taskInfo in taskInfoCL" :key="taskInfo.taskId">
                <div class="btn-bottom2" >
                  <el-button round size="small" @click="btnOnClick(taskInfo)" :title="taskInfo.taskName"
                             :class="{'btn-color-CL' : taskInfo.inTaskSet === '1'}"> {{taskInfo.taskName}} </el-button>
                </div>
              </li>
            </ul>
          </td>
          <td>
            <div class="td-div">
              <div class="cycle-head-nock nock-cl"></div>
              <div class="cycle-div border-CL" :style="{'height': cycleHeadHeight_CL +'px'}">
                <div class = "cycle-title">
                  <span>成立</span>
                </div>
                <div class = "cycle-detail">
                  <ul class="task-ul">
                    <draggable ref="draggableObj" v-model="prodTaskSetCL" @update="datadragEnd" :options="{animation:500}" >
                      <transition-group>
                        <li v-for="(taskInfo,index) in prodTaskSetCL" :key="taskInfo.taskId">
                          <div class="task-div" :title="taskInfo.taskName">
                            <div class="box">
                              <span>{{ (Array(2).join(0) + parseInt(index+1)).slice(-2) }}</span>
                            </div>
                            <div class="task-box task-box-CL">
                              <span>{{taskInfo.taskName}}</span>
                            </div>
                          </div>
                        </li>
                      </transition-group>
                    </draggable>
                  </ul>
                </div>
              </div>
              <div class="cycle-head-arrowhead arrowhead_cl"></div>
            </div>
          </td>
        </tr>
        <tr>
          <td>
            <ul class="add-task-ul">
              <div class="btn-bottom1">
                <div class="color-head" style="background: #FF8307;"></div>
                <div class="color-head-span">存续</div>
              </div>
              <li v-for="taskInfo in taskInfoCX" :key="taskInfo.taskId">
                <div class="btn-bottom2" >
                  <el-button round size="small" @click="btnOnClick(taskInfo)" :title="taskInfo.taskName"
                             :class="{'btn-color-CX' : taskInfo.inTaskSet === '1'}">{{taskInfo.taskName}}</el-button>
                </div>
              </li>
            </ul>
          </td>
          <td>
            <div class="td-div">
              <div class="cycle-head-nock nock-cx"></div>
              <div class="cycle-div border-CX" :style="{'height': cycleHeadHeight_CX +'px'}">
                <div class = "cycle-title">
                  <span>存续</span>
                </div>
                <div class = "cycle-detail">
                  <ul class="task-ul">
                    <draggable ref="draggableObj" v-model="prodTaskSetCX" @update="datadragEnd" :options="{animation:500}" >
                      <transition-group>
                        <li v-for="(taskInfo,index) in prodTaskSetCX" :key="taskInfo.taskId">
                          <div class="task-div" :title="taskInfo.taskName">
                            <div class="box">
                              <span>{{ (Array(2).join(0) + parseInt(index+1)).slice(-2) }}</span>
                            </div>
                            <div class="task-box task-box-CX">
                              <span>{{taskInfo.taskName}}</span>
                            </div>
                          </div>
                        </li>
                      </transition-group>
                    </draggable>
                  </ul>
                </div>
              </div>
              <div class="cycle-head-arrowhead arrowhead_cx"></div>
            </div>
          </td>
        </tr>
        <tr>
          <td>
            <ul class="add-task-ul">
              <div class="btn-bottom1">
                <div class="color-head" style="background: #F55D60;"></div>
                <div class="color-head-span">到期</div>
              </div>
              <li v-for="taskInfo in taskInfoDQ" :key="taskInfo.taskId">
                <div class="btn-bottom2" >
                  <el-button round size="small" @click="btnOnClick(taskInfo)" :title="taskInfo.taskName"
                             :class="{'btn-color-DQ' : taskInfo.inTaskSet === '1'}">{{taskInfo.taskName}}</el-button>
                </div>
              </li>
            </ul>
          </td>
          <td>
            <div class="td-div">
              <div class="cycle-head-nock nock-dq"></div>
              <div class="cycle-div border-DQ" :style="{'height': cycleHeadHeight_DQ +'px'}">
                <div class = "cycle-title">
                  <span>到期</span>
                </div>
                <div class = "cycle-detail">
                  <ul class="task-ul">
                    <draggable ref="draggableObj" v-model="prodTaskSetDQ" @update="datadragEnd" :options="{animation:500}" >
                      <transition-group>
                        <li v-for="(taskInfo,index) in prodTaskSetDQ" :key="taskInfo.taskId">
                          <div class="task-div" :title="taskInfo.taskName">
                            <div class="box">
                              <span>{{ (Array(2).join(0) + parseInt(index+1)).slice(-2) }}</span>
                            </div>
                            <div class="task-box task-box-DQ">
                              <span>{{taskInfo.taskName}}</span>
                            </div>
                          </div>
                        </li>
                      </transition-group>
                    </draggable>
                  </ul>
                </div>
              </div>
              <div class="cycle-head-arrowhead arrowhead_dq"></div>
            </div>
          </td>
        </tr>

        <tr>
          <td>
            <ul class="add-task-ul">
              <div class="btn-bottom1">
                <div class="color-head" style="background: #977DDF;"></div>
                <div class="color-head-span">统计</div>
              </div>
              <li v-for="taskInfo in taskInfoTJ" :key="taskInfo.taskId">
                <div class="btn-bottom2" >
                  <el-button round size="small" @click="btnOnClick(taskInfo)" :title="taskInfo.taskName"
                             :class="{'btn-color-TJ' : taskInfo.inTaskSet === '1'}">{{taskInfo.taskName}}</el-button>
                </div>
              </li>
            </ul>
          </td>
          <td>
            <div class="td-div">
              <div class="cycle-head-nock nock-tj"></div>
              <div class="cycle-div border-TJ" :style="{'height': cycleHeadHeight_TJ +'px'}">
                <div class = "cycle-title">
                  <span>统计</span>
                </div>
                <div class = "cycle-detail">
                  <ul class="task-ul">
                    <draggable ref="draggableObj" v-model="prodTaskSetTJ" @update="datadragEnd" :options="{animation:500}" >
                      <transition-group>
                        <li v-for="(taskInfo,index) in prodTaskSetTJ" :key="taskInfo.taskId">
                          <div class="task-div" :title="taskInfo.taskName">
                            <div class="box">
                              <span>{{ (Array(2).join(0) + parseInt(index+1)).slice(-2) }}</span>
                            </div>
                            <div class="task-box task-box-TJ">
                              <span>{{taskInfo.taskName}}</span>
                            </div>
                          </div>
                        </li>
                      </transition-group>
                    </draggable>
                  </ul>
                </div>
              </div>
              <div class="cycle-head-arrowhead arrowhead_tj"></div>
            </div>
          </td>
        </tr>
      </table>
    </div>


    <div class="form-foot">
      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="TaProdTaskSetList.addTaProdTaskSets" style="margin-right: 4px;"
             :data-model="formData" :data-handler="confirmHandler">
        <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
      </k-btn>
      <k-btn class="btn-custom-plain" data-functype="CLOSE">
        <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
      </k-btn>
    </div>
  </div>
</template>

<script>

import draggable from'vuedraggable'

export default {
  components:{
    draggable,
  },
  props: {
    updSuccess: Function,
    modValue:{},
  },
  data() {
    return {
      prodClearLifecycleTypes:{typeMJ:'01',typeCL:'02',typeCX:'03',typeDQ:'04',typeTJ:'05'},
      taskInfoMJ: [],
      taskInfoCL: [],
      taskInfoCX: [],
      taskInfoDQ: [],
      taskInfoTJ: [],
      prodTaskSetMJ:[],
      prodTaskSetCL:[],
      prodTaskSetCX:[],
      prodTaskSetDQ:[],
      prodTaskSetTJ:[],
      btnSelected: [],
      btnColor:{},
      formData:{},
      indexMapping:{},
      taskInfos: [],
      cycleHeadHeight_MJ:45,
      cycleHeadHeight_CL:45,
      cycleHeadHeight_CX:45,
      cycleHeadHeight_DQ:45,
      cycleHeadHeight_TJ:45,
    };
  },
  methods: {

    confirmHandler:function(params){

      let prodTaskList =  [];
      prodTaskList.push(...this.prodTaskSetMJ);
      prodTaskList.push(...this.prodTaskSetCL);
      prodTaskList.push(...this.prodTaskSetCX);
      prodTaskList.push(...this.prodTaskSetDQ);
      prodTaskList.push(...this.prodTaskSetTJ);

      params["prodTaskList"] = JSON.stringify(prodTaskList);
      params["prodMode"] = this.modValue.prodMode;

      return params;
    },

    btnOnClick: function(taskInfo){

      let taskSet;
      if(this.prodClearLifecycleTypes["typeMJ"] === taskInfo.lifecycleType){
        taskSet = this.prodTaskSetMJ;
      }else if(this.prodClearLifecycleTypes["typeCL"] === taskInfo.lifecycleType){
        taskSet = this.prodTaskSetCL;
      }else if(this.prodClearLifecycleTypes["typeCX"] === taskInfo.lifecycleType){
        taskSet = this.prodTaskSetCX;
      }else if(this.prodClearLifecycleTypes["typeDQ"] === taskInfo.lifecycleType){
        taskSet = this.prodTaskSetDQ;
      }else if(this.prodClearLifecycleTypes["typeTJ"] === taskInfo.lifecycleType){
        taskSet = this.prodTaskSetTJ;
      }else{
        return;
      }

      //按钮点击，通过删除和添加 对应类型数组 内的对象来更新表单内容
      if(taskInfo.inTaskSet=="1"){
        this.$set(taskInfo, 'inTaskSet', '0');
        this.$delete(taskSet, this.indexMapping[taskInfo.taskId]);

        //更新 对应类型数组 任务与下标位置对应关系
        for(let i=0;i< taskSet.length;i++){
          this.indexMapping[taskSet[i].taskId] = i;
        }

      }else{

        this.$set(taskInfo, 'inTaskSet', '1');
        taskSet.push(taskInfo);

        //更新 对应类型数组 任务与下标位置对应关系
        for(let i=0;i< taskSet.length;i++){
          this.indexMapping[taskSet[i].taskId] = i;
        }

      }

    },

    datadragEnd(evt){
      evt.preventDefault();
      //更新 对应类型数组 任务与下标位置对应关系
      for(let i=0;i< this.prodTaskSetMJ.length;i++){
        this.indexMapping[this.prodTaskSetMJ[i].taskId] = i;
      }
      for(let i=0;i< this.prodTaskSetCL.length;i++){
        this.indexMapping[this.prodTaskSetCL[i].taskId] = i;
      }
      for(let i=0;i< this.prodTaskSetCX.length;i++){
        this.indexMapping[this.prodTaskSetCX[i].taskId] = i;
      }
      for(let i=0;i< this.prodTaskSetDQ.length;i++){
        this.indexMapping[this.prodTaskSetDQ[i].taskId] = i;
      }

      for(let i=0;i< this.prodTaskSetTJ.length;i++){
        this.indexMapping[this.prodTaskSetTJ[i].taskId] = i;
      }
    }

  },

  mounted() {

    //查询产品清算类型下的清算任务信息，生成按钮
    this.httpUtil.comnQuery({
      action: 'T8ClearTaskInfo.findProdClearTaskInfoWithFlag',
      params: { taskType:'2', prodMode:this.modValue.prodMode}
    }).then(data => {

      for(let i=0;i<data.rows.length;i++){
        let task = data.rows[i];
        if(this.prodClearLifecycleTypes["typeMJ"] === task.lifecycleType){
          this.taskInfoMJ.push(task);
        }else if(this.prodClearLifecycleTypes["typeCL"] === task.lifecycleType){
          this.taskInfoCL.push(task);
        }else if(this.prodClearLifecycleTypes["typeCX"] === task.lifecycleType){
          this.taskInfoCX.push(task);
        }else if(this.prodClearLifecycleTypes["typeDQ"] === task.lifecycleType){
          this.taskInfoDQ.push(task);
        }else if(this.prodClearLifecycleTypes["typeTJ"] === task.lifecycleType){
          this.taskInfoTJ.push(task);
        }
      }
    });

    //查询该组已经配置的清算任务
    this.httpUtil.comnQuery({
      action: 'T8ProdTaskSet.findTaProdTaskSets',
      params: {prodMode:this.modValue.prodMode}
    }).then(data => {

      for(let i=0;i<data.rows.length;i++){
        //以数组形式存储任务数据，indexMapping存储任务在Data 中的下标位置
        let task = data.rows[i];
        if(this.prodClearLifecycleTypes["typeMJ"] === task.lifecycleType){
          this.indexMapping[data.rows[i].taskId] = this.prodTaskSetMJ.length;
          this.prodTaskSetMJ.push(task);
        }else if(this.prodClearLifecycleTypes["typeCL"] === task.lifecycleType){
          this.indexMapping[data.rows[i].taskId] = this.prodTaskSetCL.length;
          this.prodTaskSetCL.push(task);
        }else if(this.prodClearLifecycleTypes["typeCX"] === task.lifecycleType){
          this.indexMapping[data.rows[i].taskId] = this.prodTaskSetCX.length;
          this.prodTaskSetCX.push(task);
        }else if(this.prodClearLifecycleTypes["typeDQ"] === task.lifecycleType){
          this.indexMapping[data.rows[i].taskId] = this.prodTaskSetDQ.length;
          this.prodTaskSetDQ.push(task);
        }else if(this.prodClearLifecycleTypes["typeTJ"] === task.lifecycleType){
          this.indexMapping[data.rows[i].taskId] = this.prodTaskSetTJ.length;
          this.prodTaskSetTJ.push(task);
        }
      }

    });

  },

  created() {

  },

  watch:{
    taskInfoMJ(newValue){
      //根据任务按钮数量调整右边向下箭头的高度；45是除了按钮，其他元素的总高度，按钮数量除以5是每一行5个按钮，得出行数，一行高36。
      this.cycleHeadHeight_MJ = 30;
      this.cycleHeadHeight_MJ = this.cycleHeadHeight_MJ + Math.ceil(newValue.length/5) * 36;

    },
    taskInfoCL(newValue){
      this.cycleHeadHeight_CL = 25;
      this.cycleHeadHeight_CL = this.cycleHeadHeight_CL + Math.ceil(newValue.length/5) * 36;
    },
    taskInfoCX(newValue){
      this.cycleHeadHeight_CX = 32;
      this.cycleHeadHeight_CX = this.cycleHeadHeight_CX + Math.ceil(newValue.length/5) * 36;

    },
    taskInfoDQ(newValue){
      this.cycleHeadHeight_DQ = 25;
      this.cycleHeadHeight_DQ = this.cycleHeadHeight_DQ + Math.ceil(newValue.length/5) * 36;
    },
    taskInfoTJ(newValue){
      this.cycleHeadHeight_TJ = 25;
      this.cycleHeadHeight_TJ = this.cycleHeadHeight_TJ + Math.ceil(newValue.length/5) * 36;
    },

  }

}
</script>

<style lang="scss" scoped>

li .el-button--small, .el-button--small.is-round {
  margin: 5px 5px 0px 5px;
  width: 87px;
  height: 24px;
  font-family: PingFangSC-Regular;
  font-size: 11px;
  line-height: 5px;
  border: 0.5px solid #CCCCCC;
}

li .el-button--small, .el-button--small.is-round > span{
  width: 100%;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
  display: inline-block;
}
.el-button--small, .el-button--small.is-round {
  padding: 9px 6px;
}

.head-span{
  font-family: PingFangSC-Semibold;
  font-size: 14px;
  color: #3D4859;
}

.task-ul {
  margin: 0;
  font-size: 14px;
  list-style: none;
  padding:0 0 0 0;
}

.task-ul  li {
  display:inline-block;
}


.addTaskPopup > .el-dialog > .el-dialog__body{
  padding: 5px 0 0 0 ;
}

.btn-selected{
  background-color:#0b9ce3;
}


.btn-unSelected{
  background-color:#FFF;
}

.btn-title {
  margin-bottom: 10px;
  padding : 0px 30px;
  margin-top: 10px;
}

.btn-title > span{
  font-weight: 600;
}

.add-task-ul {
  padding: 0 30px 10px 30px;
  margin: 0 0 12px 0;
}

.add-task-ul > li{
  display:inline-block;
}


.k-timeline-item__tail{
  position: absolute;
  left: 20px;
  height: 90%;
  border-left: 2px dashed #E4E7ED;
  top: 40px;
}

.k-el-timeline-item__node--large{
  left: 10px;
  width: 20px;
  height: 20px;
  top: 19px;
  color:#FFF;
}

.k-el-timeline-item__node--large-div{
  width: 150px;
  text-align: left;
  top: 100px;
  padding-left: 20px;
}

.k-el-timeline-item__node--large-div > span{
  line-height: 63px;
  color: #606266;
  width: 130px;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
  position: absolute;
}

.form-foot {
  text-align:center;
  padding: 10px 0 0 0;
}

.drag-item{
  width:200px;
  height:50px;
  line-height:50px;
  margin:auto;
  position:relative;
  background:#ddd;
  margin-top:20px;
}


.cycle-head {
  width: 7.9px;
  height: 85%;
}
.cycle-head-MJ {
  background: #4EA6EC;
}
.cycle-head-CL {
  background: #51CAB4;
}
.cycle-head-CX {
  background: #FF8307;
}
.cycle-head-DQ {
  background: #F55D60;
}
.cycle-head-nock {
  background-repeat: no-repeat;
  background-position: center;
  width: 8px;
  height: 12px;
  margin-top: -8px;
}
.nock-mj {
  background-image: url(/static/svg/clear/5001_task_tail_mj.svg);
}
.nock-cl {
  background-image: url(/static/svg/clear/5001_task_tail_cl.svg);
}
.nock-cx {
  background-image: url(/static/svg/clear/5001_task_tail_cx.svg);
}
.nock-dq {
  background-image: url(/static/svg/clear/5001_task_tail_dq.svg);
}
.nock-tj {
  background-image: url(/static/svg/clear/5001_task_tail_tj.svg);
}


.cycle-head-arrowhead {
  background-repeat: no-repeat;
  background-position: center;
  width: 8px;
  height: 16px;
}
.arrowhead_mj {
  background-image: url(/static/svg/clear/5001_task_head_mj.svg);
}
.arrowhead_cl {
  background-image: url(/static/svg/clear/5001_task_head_cl.svg);
}
.arrowhead_cx {
  background-image: url(/static/svg/clear/5001_task_head_cx.svg);
}
.arrowhead_dq {
  background-image: url(/static/svg/clear/5001_task_head_dq.svg);
}
.arrowhead_tj {
  background-image: url(/static/svg/clear/5001_task_head_tj.svg);
}

.cycle-title > span{
  font-family: PingFangSC-Medium;
  font-size: 12px;
  color: #333333;
  font-weight: 550;
  padding-left: 10px;
}
.cycle-div{
  display: inline-block;
}
.td-div:hover{
  box-shadow: 0 0 20px 0 rgba(0,0,0,0.10);
  z-index: 999;
}

.cycle-detail {
  padding: 0px 0px 10px 25px;
  width: 100%;
}

.color-head{
  border-radius: 2px;
  width:4px;
  height:11px;
  display: inline-flex;
}

.color-head-span{
  font-family: PingFangSC-Regular;
  font-size: 13px;
  color: #3D4859;
  display: inline-flex;
  font-weight: 400;
}

.btn-bottom1{
  margin-bottom: 4px;
}

.btn-bottom2{
  margin-bottom: 8px;
}

.btn-color-MJ{
  background: #E3F2FF;
  border: 0.5px solid #4EA6EC;
  color: #2A95EA;
  font-family: PingFangSC-Regular;
  font-size: 12px;
  line-height: 5px;
  border: 0.5px solid;
}
.btn-color-CL{
  background: #DDF7F2;
  border: 0.5px solid #51CAB4;
  color: #11BD9E;
  font-family: PingFangSC-Regular;
  font-size: 12px;
  line-height: 5px;
}
.btn-color-CX{
  background: #FFF1E3;
  border: 0.5px solid #FF8307;
  color: #FF8307;
  font-family: PingFangSC-Regular;
  font-size: 12px;
  line-height: 5px;
}
.btn-color-DQ{
  background: #FFE8E8;
  border: 0.5px solid #F55D60;
  color: #F55D60;
  font-family: PingFangSC-Regular;
  font-size: 12px;
  line-height: 5px;
}

.btn-color-TJ{
  background: #F3E8FF;
  border: 0.5px solid #9B84DE;
  color: #9B84DE;
  font-family: PingFangSC-Regular;
  font-size: 12px;
  line-height: 5px;
}

.task-div {
  display: inline-flex;
  width: 120px;
  margin: 0px 5px 2px 0px;
}

.head-tips{
  border-radius: 50%;
  width: 12px;
  height: 12px;
  border: 0.5px solid #999999;
  text-align: center;
  line-height: 11px;
  font-size: 8px;
  color: #999999;
  font-family: Times New Roman;
  display: inline-block;
  margin-left: 10px;
}

.task-table{
  width:100%;
}

.task-table td{
  width:50%;
  display: block;
}

.task-table tr{
  display: flex;
}


.task-box{
  width: 103px;
  height: 28px;
  background-repeat: no-repeat;
  background-position: center;
  text-align: center;
  line-height: 36px;
  margin-left: -11px;
}

.task-box > span{
  font-family: PingFangSC-Regular;
  font-size: 11px;
  color: #FFFFFF;
  bottom: 4px;
  position: relative;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
  width: 58px;
  display: inline-block;
}

.task-box-MJ{
  background-image: url('/static/images/clear/task-box-mj.png');
}
.task-box-CL{
  background-image: url('/static/images/clear/task-box-cl.png');
}
.task-box-CX{
  background-image: url('/static/images/clear/task-box-cx.png');
}
.task-box-DQ{
  background-image: url('/static/images/clear/task-box-dq.png');
}
.task-box-TJ{
  background-image: url('/static/images/clear/task-box-tj.png');
}

.border-MJ{
  border-left: 8px solid #4EA6EC;
}
.border-CL{
  border-left: 8px solid #51CAB4;
}
.border-CX{
  border-left: 8px solid #FF8307;
}
.border-DQ{
  border-left: 8px solid #F55D60;
}
.border-TJ{
  border-left: 8px solid #977DDF;
}

.head-icon{
  display: inline-flex;
  width: 8px;
  height: 6px;
  float: left;
  margin-left: -8px;
}

.box{
  width: 17.7px;
  height: 24px;
  background-color: #FFFFFF;
  position: relative;
  border-radius: 2px;
  display: inline-block;
  left: 5px;
  z-index: 2;
  text-align: center;
  box-shadow: -1px 0px 11px -1px rgba(0,0,0,0.14);
  margin-top: 2.3px;
}
.box:before{
  content: "";
  width: 0;
  height: 0;
  position: absolute;
  top: 0;
  left: -7px;
  border-top: 11.5px solid transparent;
  border-bottom: 13px solid transparent;
  border-right: 8px #FFFFFF solid;
  border-left: none;
  box-shadow: 0px 0px 0px 0px rgba(0,0,0,0.14);
}
.box:after{
  content: "";
  width: 0;
  height: 0;
  position: absolute;
  top: 0;
  left: 17px;
  border-top: 11.5px solid transparent;
  border-bottom: 13px solid transparent;
  border-right: none;
  border-left: 7px #FFFFFF solid;
  box-shadow: 0px 0px 0px 0px rgba(0,0,0,0.14);
}
.box > span{
  font-family: Arial-BoldMT;
  font-size: 10px;
  color: #4EA6EC;
  font-weight: 500;
  line-height: 26px;
}
</style>
