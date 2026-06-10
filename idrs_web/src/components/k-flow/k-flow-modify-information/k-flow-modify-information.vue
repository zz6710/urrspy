<template>
  <div>
    <h4 v-show="info && info.length > 0">更新日志</h4>
    <div class="task_detail" v-for="(item, index) in info" :key="index">
      <div>
        <span>任务：{{item.taskDisplayName}}</span>
        <span>操作人: {{item.optUserName}}</span>
        <span>时间: {{item.createDataTime}}</span>
      </div>
      <k-grid :data-data="{'rows': item.data}" data-operate-column="false" data-display="false">
        <k-grid-column data-header="字段名" data-name="label"></k-grid-column>
        <k-grid-column data-header="原值" data-name="before"></k-grid-column>
        <k-grid-column data-header="更新值" data-name="after"></k-grid-column>
      </k-grid>
      <el-divider></el-divider>
    </div>
  </div>
</template>

<script>
  import Tools from "@/utils/tools.js";

  export default {
    name: "KFlowModifyInformation",
    props: {
      taskInfo: {}
    },
    data() {
      return {
        info: []
      };
    },
    created() {
      this.httpUtil
        .ajax({
          url: "/wf/modifydata/list.json",
          params: {"processInstanceId": this.taskInfo.processInstanceId}
        })
        .then(res => {
          if (res.rows) {
            let rows = res.rows;
            for (let i = 0; i < rows.length; i++) {
              rows[i].data = JSON.parse(rows[i].data)
              rows[i].createDataTime = Tools.formatDateTime(rows[i].createDate, rows[i].createTime)
            }
            this.info = rows;
          }
        });
    }
  };
</script>
<style scoped>
  .task_detail span {
    display: inline-block;
    margin-right: 40px;
    min-width: 250px;
  }
</style>
