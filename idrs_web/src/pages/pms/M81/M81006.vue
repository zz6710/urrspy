<template>
  <div>
    <k-form-search data-target="workdayProgramGrid" data-model-name="WorkdayProgram">
      <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addWorkdayProgramPopup">
        <md-icon md-src="/static/svg/add.svg" />新增
      </k-btn>
    </k-form-search>
    <k-grid ref="workdayProgramGrid" data-action='WorkdayProgram.find'
        :data-params="{'pgmtype':'2,3'}"
        @data-row-select="selectRow" data-operate-width="320px">
      <k-grid-column data-header="方案编号" data-name="pgmno"></k-grid-column>
      <k-grid-column data-header="方案名称" data-name="pgmname" ></k-grid-column>
      <k-grid-column data-header="方案类型" data-name="pgmtype" data-dict="pgmtype"></k-grid-column>
      <k-grid-column data-header="备注" data-name="remark"></k-grid-column>
      <template slot="operate">
        <!-- <k-btn class="md-info md-just-icon md-simple" data-descript="工作日" :data-handler="()=> this.showListWorkDay = true"
          data-functype="POPUP" data-size="mini" data-target="workdayPopup">
          <md-icon md-src="/static/svg/workday.svg" />
        </k-btn> -->
        <k-btn class="md-info md-just-icon md-simple"
            data-functype="PAGE" data-size="mini" data-target="/main/system/workday/Workday"
                    :data-handler="()=> this.showListWorkDay = true"
                                data-descript="修改工作日">
          <md-icon md-src="/static/svg/workday.svg" />
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple" data-descript="修改工作日名称" data-functype="POPUP" data-size="mini"
          data-target="editWorkdayProgramPopup">
          <md-icon>edit</md-icon>
        </k-btn>
        <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="WorkdayProgram.delete"
          data-size="mini" data-type="danger" data-target="workdayProgramGrid" :data-confirm="true" data-descript="删除工作日方案">
          <md-icon>close</md-icon>
        </k-btn>
      </template>
    </k-grid>

    <!-- 修改工作日方案弹出框 -->
    <k-popup ref="editWorkdayProgramPopup" data-title="修改">
      <EditWorkdayProgram v-model="formData" :updSuccess="()=> {
                            this.$refs.editWorkdayProgramPopup.close();
                            this.$refs.workdayProgramGrid.load()
                          } " />
    </k-popup>

    <!-- 添加工作日方案弹出框 -->
    <k-popup ref="addWorkdayProgramPopup" data-title="新增">
      <AddWorkdayProgram v-model="formData" :updSuccess="()=> {
                           this.$refs.addWorkdayProgramPopup.close();
                           this.$refs.workdayProgramGrid.load()
                         }" />
    </k-popup>

  </div>
</template>

<script>
  import kayak from '@/frame/kayak.js'
  import EditWorkdayProgram from "../../system/workday/edit-workday-program"
  import AddWorkdayProgram from "../../system/workday/add-workday-program"
  import {
    assign
  } from "lodash";

  export default {
    name:"M81006",
    components: {
      EditWorkdayProgram,
      AddWorkdayProgram,
    },
    data() {
      return {
        formData: {},
        selectRowData: {},
        selectPgmno: '',
        showListWorkDay: false
      };
    },
    methods: {
      selectRow(row, column, event) {
        const _this = this
        _this.selectRowData = assign({}, row)
        _this.formData = assign({}, row)
      }
    }
  };
</script>
<style lang="scss" scoped>
  ::v-deep .el-dialog {
    padding-top: 35px;
  }
</style>

