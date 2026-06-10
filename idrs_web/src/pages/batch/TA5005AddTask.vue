<template>
  <div>
    <div class="btn-title">
      <span calss="el-dialog__title">选择任务组件</span>
    </div>
    <div>
      <ul class="add-task-ul">
        <li v-for="taskInfo in taskInfos" :key="taskInfo.taskId">
          <el-button round size="small" style='width: 20%' @click="btnOnClick(taskInfo)" :disabled="!isShow" :title="taskInfo.taskName"
                     :class="{'el-button--primary' : taskInfo.inTaskSet === '1'}">  {{ taskInfo.taskId }}-{{ taskInfo.taskName }}
          </el-button>
        </li>
      </ul>
    </div>

    <div class="btn-title" style="background-color: #F5F5F5;">
      <div class="head-tips">i</div>
      <span style="font-size: 12px;color: #999999;">可拖动组件调整组件执行顺序</span>
    </div>
    <div class="form-class">
      <k-form ref="addTaskForm" style="width:auto" dataItemMargin="0">
        <div class="task-pannel">
          <ul class="task-ul">
            <draggable :disabled="!isShow" ref="draggableObj"  v-model="formData" @update="datadragEnd" :options="{animation:500}">
              <transition-group>
                <li class="el-timeline-item" v-for="(item,index) of formData" :key="item.taskId">
                  <div class="k-timeline-item__tail"></div>
                  <div class="el-timeline-item__node k-el-timeline-item__node--large el-timeline-item__node--primary">
                    <span >{{ index + 1 }}</span></div>

                  <div class="el-timeline-item__wrapper">
                    <div class="talkbubble">
                      <div class="k-el-timeline-item__node--large-div"><span>{{ item.taskId }}-{{ item.taskName }}</span></div>

                      <!--<k-form-item label="自动执行时间" data-input-width="150px" dataLabelWidth="100px">-->
                      <!--    <k-field-time v-model="item.execTime"  data-value-format="HHmm"/>-->
                      <!--</k-form-item>-->
                      <!--<k-form-item label="未完成报警时间" data-input-width="150px" dataLabelWidth="115px">-->
                      <!--    <k-field-time v-model="item.alarmTime"  data-value-format="HHmm"/>-->
                      <!--</k-form-item>-->

                    </div>
                  </div>
                </li>
              </transition-group>
            </draggable>
          </ul>
        </div>
      </k-form>
    </div>

    <div class="form-foot">
      <!--            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="TaClearTaskSetList.addTaClearTaskSets"  data-from="addTaskForm"  :data-handler="confirmHandler">-->
      <k-btn v-if="isShow" class="btn-custom-primary" data-from="addTaskForm" :data-handler="confirmHandler">
        <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
      </k-btn>
      <k-btn v-if="isShow" class="btn-custom-plain" data-functype="CLOSE">
        <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
      </k-btn>
      <k-btn v-if="!isShow" class="btn-custom-primary" data-functype="CLOSE">
        <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
      </k-btn>
    </div>
  </div>
</template>

<script>

import draggable from 'vuedraggable'

export default {
  components: {
    draggable,
  },
  props: {
    updSuccess: Function,
    groupValue: {
      type: Object
    },
    available: {
      type: Boolean,
      default: false
    },
    closePopupMethod: {
      type: Function
    },
    editActive4: Function
  },
  data() {
    return {
      taskInfos: [],
      groupInfo: {},
      formData: [],
      indexMapping: {},
      isShow: false
    };
  },
  methods: {

    confirmHandler: function (params) {
      //在这里构建后台入参对象
      /*params["taskGroup"] = this.groupValue.taskGroup;

      let taskSetList = [];
      //将选中的数据存入List
      taskSetList.push(...this.formData);

      params["taskSetList"] = JSON.stringify(taskSetList);

      this.groupValue.submitTaskParams=params;*/
      this.groupValue.existTaskInfos = [];
      this.groupValue.existTaskInfos.push(...this.formData);
      this.closePopupMethod("addTaskPopup");
      this.editActive4();
    },

    btnOnClick: function (taskInfo) {
      if (!this.isShow){
        return;
      }
      //按钮点击，通过删除和添加 formData 内的对象来更新表单内容
      if (taskInfo.inTaskSet == "1") {
        this.$set(taskInfo, 'inTaskSet', '0');
        this.$delete(this.formData, this.indexMapping[taskInfo.taskId]);

        //更新 formData 任务与下标位置对应关系
        for (let i = 0; i < this.formData.length; i++) {
          this.indexMapping[this.formData[i].taskId] = i;
        }

      } else {
        this.$set(taskInfo, 'inTaskSet', '1');
        this.formData.push(taskInfo);

        //更新 formData 任务与下标位置对应关系
        for (let i = 0; i < this.formData.length; i++) {
          this.indexMapping[this.formData[i].taskId] = i;
        }
      }
    },

    datadragEnd(evt) {
      evt.preventDefault();
      //更新 formData 任务与下标位置对应关系
      for (let i = 0; i < this.formData.length; i++) {
        this.indexMapping[this.formData[i].taskId] = i;
      }
    }

  },

  mounted() {

    this.groupInfo = this.groupValue;
    this.isShow = this.available || (this.groupValue.execTaskType == '6') || (this.groupValue.execTaskType == '5');

    this.taskInfos = this.groupValue.taskInfos;
    for (let i = 0; i < this.groupValue.existTaskInfos.length; i++) {
      //formData以数组形式存储任务数据，indexMapping存储任务在formData 中的下标位置
      this.formData.push(this.groupValue.existTaskInfos[i]);
      this.indexMapping[this.groupValue.existTaskInfos[i].taskId] = i;
    }
  },

  created() {

  },

  computed: {}

}
</script>

<style lang="scss" scoped>

.task-ul {
  margin: 0;
  font-size: 14px;
  list-style: none;
  padding: 0 0 0 0;
}


.task-pannel {
  height: 300px;
}

.addTaskPopup > .el-dialog > .el-dialog__body {
  padding: 8px 0 0 0;
}

.btn-title {
  padding: 10px 30px;
}

.add-task-ul {
  padding: 0 0 10px 20px;
  margin: 0 0 12px 0;
}

.add-task-ul > li {
  display: inline;
}

li .el-button--small, .el-button--small.is-round {
  margin: 5px 5px 7px 5px;
  width: 120px;
}

li .el-button--small, .el-button--small.is-round > span {
  width: 120px;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
}

.talkbubble {
  position: relative;
  width: 855px;
  height: 60px;
  background: #FFF;
  border-radius: 5px;
  display: inline-flex;
  margin-left: 20px;
  box-shadow: 0 1px 4px 0 rgba(0, 0, 0, .14);
}

.talkbubble:before {
  content: "";
  position: absolute;
  right: 100%;
  top: 27px;
  width: 0;
  height: 0;
  border-top: 4px solid transparent;
  border-right: 7px solid #FFF;
  border-bottom: 4px solid transparent;
}

.k-timeline-item__tail {
  position: absolute;
  left: 20px;
  height: 90%;
  border-left: 2px dashed #E4E7ED;
  top: 40px;
}

.k-el-timeline-item__node--large {
  left: 10px;
  width: 20px;
  height: 20px;
  top: 19px;
  color: #FFF;
}

.k-el-timeline-item__node--large-div {
  width: 120px;
  text-align: left;
  top: 100px;
  padding-left: 20px;
}

.k-el-timeline-item__node--large-div > span {
  line-height: 63px;
  color: #606266;
  width: 130px;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
  position: absolute;
}

.form-class {
  background-color: #F5F5F5;
  padding-left: 20px;
  // padding-top: 25px;
  overflow-y: auto;
  overflow-x: hidden;
}

.form-foot {
  text-align: center;
  padding: 10px 0px;
}

.drag-item {
  width: 200px;
  height: 50px;
  line-height: 50px;
  margin: auto;
  position: relative;
  background: #ddd;
  margin-top: 20px;
}

.ghostClass {
  opacity: 1;
}

.bottom {
  width: 200px;
  height: 50px;
  position: relative;
  background: blue;
  top: 2px;
  left: 2px;
  transition: all .5 slinear;
}

.talkbubble .md-field label {
  font-size: 13px !important;
}

.head-tips {
  border-radius: 50%;
  width: 12px;
  height: 12px;
  border: 0.5px solid #999999;
  text-align: center;
  line-height: 11px;
  font-size: 8px;
  color: #999999;
  display: inline-block;
  margin-left: 2px;
}

::v-deep .md-layout-item {
  padding-left: 4px;
}
</style>
