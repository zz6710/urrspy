<template>
  <div class="py-page">
    <k-form-search data-target="ta5010Grid" data-model-name="KbatchTaskInfo" data-label-width="80px">
    </k-form-search>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addTa5010Popup" @click="setTaskId">
            <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
        </div>
      </div>
      <k-grid ref="ta5010Grid"
        data-action='KbatchTaskInfo.findTaskInfos' data-operate-column-position="end"
        @data-row-select="selectRow" data-operate-width="150px">

        <k-grid-column data-name="taskId" data-header="任务ID" data-width="80px" data-align="center"></k-grid-column>
        <k-grid-column data-name="taskModel" data-header="所属模块" data-dict="task_model" data-sortable="true" data-width="120px" data-align="center"></k-grid-column>
        <k-grid-column data-name="taskType" data-header="清算类型" data-dict="task_type" data-sortable="true" data-width="120px" data-align="center"></k-grid-column>
        <k-grid-column data-name="taskName" data-header="任务名称" data-align="center"></k-grid-column>
        <k-grid-column data-name="canAgain" data-header="可重复执行" data-dict="1yes0no" data-align="center" data-width="100px"></k-grid-column>
        <k-grid-column data-name="serviceClass" data-header="清算任务服务类" data-align="center"></k-grid-column>
        <k-grid-column data-name="reqClass" data-header="清算任务请求类" data-width="230px" data-align="center"></k-grid-column>
        <!--<k-grid-column data-name="inClass" data-header="接口暴露入口类" data-align="center"></k-grid-column>-->
        <!--<k-grid-column data-name="simpleFlow" data-header="所属清算流程块ID" data-dict="simple_flow" data-width="80px" data-align="center"></k-grid-column>-->
        <!--<k-grid-column data-name="lifecycleType" data-header="所属生命周期时段" data-dict="prod_clear_lifecycle_type" data-width="80px" data-align="center"></k-grid-column>-->

        <template slot="operate">
          <k-btn data-functype="POPUP" data-size="mini" data-target="updTa5010Popup"  class="md-info md-just-icon md-simple"
              data-descript="修改清算组件">
                <md-icon>edit</md-icon>
          </k-btn>
          <k-btn data-functype="SUBMIT" data-size="mini" data-type="danger"  class="md-danger md-just-icon md-simple"
                data-action="KbatchTaskInfo.deleteKbatchTaskInfo" :data-model="selectRowData" data-target="ta5010Grid"
                data-confirm data-descript="删除清算组件" :data-after-success="deleteBeforeOption">
              <md-icon>close</md-icon>
          </k-btn>

          <k-btn data-functype="POPUP" data-size="mini" data-target="addTa5010StepPopup" class="md-info md-just-icon md-simple"
                data-confirm data-descript="新增子步骤">
            <md-icon>add</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--  子任务栏  -->
    <div class="py-page-container">
      <k-grid ref="ta5010StepGrid" :dataShowSubscript="false"  :data-autoload="false"  @init="(grid)=>{this.$kgrid = grid}" :data-after-load="stepGridAfterLoad"
        data-action='KbatchTaskStep.findKbatchTaskSteps' @data-row-select="stepSelectRow" data-operate-width="200px">
        <k-grid-column data-name="taskId" data-header="任务ID" data-width="80px" data-align="center"></k-grid-column>
        <k-grid-column data-name="stepNo" data-header="步骤号" data-align="center"></k-grid-column>
        <k-grid-column data-name="stepName" data-header="步骤名称" data-width="220px" data-align="center"></k-grid-column>
        <k-grid-column data-name="canReplay" data-header="可重复执行" data-dict="1yes0no" data-align="center"></k-grid-column>
        <k-grid-column data-name="canSkip" data-header="是否可跳过" data-dict="1yes0no" data-align="center"></k-grid-column>
        <k-grid-column data-name="isSlice" data-header="是否分片任务" data-dict="1yes0no"  data-align="center"></k-grid-column>
        <k-grid-column data-name="sliceServiceClass" data-header="分片任务服务处理类" data-width="150px" data-align="center"></k-grid-column>
        <k-grid-column data-name="sliceReqClass" data-header="分片任务请求参数类" data-width="150px" data-align="center"></k-grid-column>

        <template slot="operate">
          <k-btn data-functype="POPUP" data-size="mini" data-target="updateTa5010StepPopup"  class="md-info md-just-icon md-simple"
              data-descript="修改子步骤">
                <md-icon>edit</md-icon>
          </k-btn>
          <k-btn data-functype="SUBMIT" data-size="mini" data-type="danger"  class="md-danger md-just-icon md-simple"
                data-action="KbatchTaskStep.deleteKbatchTaskStep" :data-model="stepSelectRowData" data-target="ta5010StepGrid"
                data-confirm data-descript="删除子步骤">
              <md-icon>close</md-icon>
          </k-btn>
        </template>
      </k-grid>
<!--      <div :class="stepGridSelected?'add-step-btn-hasData':'add-step-btn'">-->
<!--          <div class="add-btn"  @click="addHandler" >+</div>-->
<!--      </div>-->
    </div>

    <!--    新增清算子步骤   -->
    <k-popup ref="addTa5010StepPopup" data-title="添加清算组件子步骤">
      <k-form ref="addTa5010StepForm" :data-col="2">
        <k-form-item label="任务ID">
          <k-field-text v-model="stepFormData.taskId" :dataAllowblank="false" :data-max-length="32" data-disabled/></k-form-item>
        <k-form-item label="步骤号">
          <k-field-text v-model="stepFormData.stepNo"  :dataAllowblank="false" data-validate-type="code"/></k-form-item>
        <k-form-item label="步骤名称">
          <k-field-text v-model="stepFormData.stepName" :data-max-length="64" :dataAllowblank="false"/></k-form-item>
        <k-form-item label="是否可重复执行">
          <k-field-select v-model="stepFormData.canReplay"  data-dict="1yes0no" :dataAllowblank="false"/></k-form-item>
        <k-form-item label="是否可跳过">
          <k-field-select v-model="stepFormData.canSkip"  data-dict="1yes0no" :dataAllowblank="false"/></k-form-item>
        <k-form-item label="是否分片任务">
          <k-field-select v-model="stepFormData.isSlice"  data-dict="1yes0no" :dataAllowblank="false"/></k-form-item>
        <k-form-item label="分片任务服务处理类">
          <k-field-text v-model="stepFormData.sliceServiceClass" :data-max-length="512"/></k-form-item>
        <k-form-item label="分片任务请求参数类">
          <k-field-text v-model="stepFormData.sliceReqClass"  :data-max-length="512"/></k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="KbatchTaskStep.insertKbatchTaskStep"
                 data-from="addTa5010StepForm" :data-model="stepFormData" data-target="ta5010StepGrid">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改清算子步骤   -->
    <k-popup ref="updateTa5010StepPopup" data-title="修改清算组件子步骤">
      <k-form ref="edit5010StepForm" :data-col="2">
        <k-form-item label="任务ID">
          <k-field-text v-model="stepFormData.taskId" :dataAllowblank="false" :data-max-length="32" data-disabled/></k-form-item>
        <k-form-item label="步骤号">
          <k-field-text v-model="stepFormData.stepNo"  :dataAllowblank="false" data-disabled/></k-form-item>
        <k-form-item label="步骤名称">
          <k-field-text v-model="stepFormData.stepName" :data-max-length="64" :dataAllowblank="false"/></k-form-item>
        <k-form-item label="是否可重复执行">
          <k-field-select v-model="stepFormData.canReplay"  data-dict="1yes0no" :dataAllowblank="false"/></k-form-item>
        <k-form-item label="是否可跳过">
          <k-field-select v-model="stepFormData.canSkip"  data-dict="1yes0no" :dataAllowblank="false"/></k-form-item>
        <k-form-item label="是否分片任务">
          <k-field-select v-model="stepFormData.isSlice"  data-dict="1yes0no" :dataAllowblank="false"/></k-form-item>
        <k-form-item label="分片任务服务处理类">
          <k-field-text v-model="stepFormData.sliceServiceClass" :data-max-length="512"/></k-form-item>
        <k-form-item label="分片任务请求参数类">
          <k-field-text v-model="stepFormData.sliceReqClass"  :data-max-length="512"/></k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="KbatchTaskStep.updateKbatchTaskStep"
                 data-from="edit5010StepForm" :data-model="stepFormData" data-target="ta5010StepGrid">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>


    <!--    新增清算组件   -->
    <k-popup ref="addTa5010Popup" data-title="添加清算组件">
      <k-form ref="addTa5010Form" :data-col="2">
        <k-form-item label="任务ID">
          <k-field-text v-model="formData.taskId" :dataAllowblank="false" :data-max-length="32" data-regx="^[a-zA-Z\d]+$" data-regx-text="任务ID只包含字母和数字，请输入正确的ID!"/></k-form-item>
        <k-form-item label="清算类型">
          <k-field-select v-model="formData.taskType"  data-dict="task_type" :dataAllowblank="false"/></k-form-item>

        <k-form-item label="任务名称">
          <k-field-text v-model="formData.taskName" :data-max-length="64" :dataAllowblank="false"/></k-form-item>
        <k-form-item label="所属模块">
          <k-field-select v-model="formData.taskModel" data-dict="task_model"  :dataAllowblank="false"/></k-form-item>

        <k-form-item label="是否可重复执行">
          <k-field-select v-model="formData.canAgain"  data-dict="1yes0no" :dataAllowblank="false"/></k-form-item>
        <k-form-item label="清算任务服务类">
          <k-field-text v-model="formData.serviceClass" :data-max-length="512"/></k-form-item>
        <k-form-item label="清算任务请求类">
          <k-field-text v-model="formData.reqClass" :data-max-length="512"/></k-form-item>
        <k-form-item label="接口暴露入口类">
          <k-field-text v-model="formData.inClass" :data-max-length="512"/></k-form-item>
        <k-form-item label="所属清算流程块ID"  v-show="false"  >
          <k-field-text v-model="formData.simpleFlow" data-default-value="008"/></k-form-item>
<!--        <k-form-item label="产品清算生命周期时段">-->
<!--          <k-field-select v-model="formData.lifecycleType"  data-dict="prod_clear_lifecycle_type"/></k-form-item>-->

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="KbatchTaskInfo.insertKbatchTaskInfo"
                 data-from="addTa5010Form" :data-model="formData" data-target="ta5010Grid">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改清算组件   -->
    <k-popup ref="updTa5010Popup" data-title="修改清算组件">
      <k-form ref="edit5010Form" :data-col="2">

        <k-form-item label="任务ID">
          <k-field-text v-model="formData.taskId" :dataAllowblank="false" :data-max-length="32" data-disabled data-regx="^[a-zA-Z\d]+$" data-regx-text="任务ID只包含字母和数字，请输入正确的ID!"/></k-form-item>
        <k-form-item label="清算类型">
          <k-field-select v-model="formData.taskType" data-dict="task_type" :dataAllowblank="false"/></k-form-item>
        <k-form-item label="任务名称">
          <k-field-text v-model="formData.taskName" :data-max-length="64" :dataAllowblank="false"/></k-form-item>
        <k-form-item label="所属模块">
        <k-field-select v-model="formData.taskModel" data-dict="task_model" /></k-form-item>

        <k-form-item label="是否可重复执行">
          <k-field-select v-model="formData.canAgain"  data-dict="1yes0no" :dataAllowblank="false"/></k-form-item>
        <k-form-item label="清算任务服务类">
          <k-field-text v-model="formData.serviceClass" :data-max-length="512"/></k-form-item>
        <k-form-item label="清算任务请求类">
          <k-field-text v-model="formData.reqClass" :data-max-length="512"/></k-form-item>
        <k-form-item label="接口暴露入口类">
          <k-field-text v-model="formData.inClass" :data-max-length="512"/></k-form-item>
        <k-form-item label="所属清算流程块ID"  v-show="false" >
          <k-field-text v-model="formData.simpleFlow" data-default-value="008"/></k-form-item>
<!--        <k-form-item label="所属清算流程块ID" >-->
<!--          <k-field-select v-model="formData.simpleFlow"  data-dict="simple_flow" /></k-form-item>-->
        <!-- <k-form-item label="产品清算生命周期时段">
          <k-field-select v-model="formData.lifecycleType"  data-dict="prod_clear_lifecycle_type"/></k-form-item> -->

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="KbatchTaskInfo.updateKbatchTaskInfo"
                 data-from="edit5010Form" :data-model="formData"
                 data-target="ta5010Grid">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
  </div>
</template>

<script>
  import { assign } from "lodash";

  export default {
    name: "TA5010",

    data() {
      return {
        formData: {},
        selectRowData: {},
        stepFormData: {},
        stepSelectRowData: {},
        stepGridSelected: false,
        $kgrid : null,
      };
    },

    methods: {
        selectRow(row, column, event) {
          this.selectRowData = assign({}, row);
          this.formData = assign({}, row);
          this.stepFormData.taskId = row.taskId;
          this.$kgrid.load({taskId:row.taskId});

          /*document.getElementById('ta5010StepGrid').scrollIntoView({
              block: 'start',
              inline: 'nearest',
              behavior: 'smooth'
          })*/
        },
      deleteBeforeOption(row) {
          this.$refs['ta5010StepGrid'].list = [];
          this.$refs['ta5010StepGrid'].total = 0;
      },
      stepSelectRow(row, column, event) {
        this.stepSelectRowData = assign({}, row);
        this.stepFormData = assign({}, row);
      },

      stepGridAfterLoad(row){

        if(this.$kgrid.list.length>0){
          this.stepGridSelected = true;
        }else{
          this.stepGridSelected = false;
        }

        return row;
      },

      addHandler(){
        this.$refs.addTa5010StepPopup.popup();
      },

      setTaskId(){
        this.httpUtil.comnQuery({
          action: 'KbatchTaskInfo.findTaskId',
          params: {}
        }).then(data => {
          if(data.rows.length > 0){
            this.$set(this.formData,"taskId",data.rows[0].taskId);
            this.$refs.addTa5010Popup.popup();
          }
        });
      }
    }
  };
</script>

<style lang="scss" scoped>
  .add-step-btn{
    position: relative;
    z-index: 1;
    margin: -90px 0 0 23px;
  }

  .add-step-btn-hasData{
      position: relative;
      z-index: 1;
      margin: -110px 0 0 23px;
      width: 20%;
  }

  .add-btn{
    background-color: #4caf50;
    border-radius: 30px;
    box-shadow: 0 4px 5px 0 rgba(76,175,80,0.14), 0 1px 10px 0 rgba(76,175,80,0.12), 0 2px 4px -1px rgba(76,175,80,0.2);
    width: 30px;
    height: 30px;
    line-height: 31px;
    font-size: 32px;
    cursor: pointer;
    color: #FFF;
    text-align: center;
  }

</style>
