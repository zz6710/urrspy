<template>
  <div class="py-page">
		<div>
			<k-form-search-customize data-model-name="DataReportManage" v-model="searchParam" data-target="tableGrid">
				<k-form-item label="清算日期">
					<k-field-date v-model="searchParam.taskDate"/>
				</k-form-item>
			</k-form-search-customize>
		</div>
    <div class="py-page-container">
        <div class="table-top-btns">
          <div class="left">
            <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" data-target="reBatchPopup">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>批量重跑
            </k-btn>
            <!-- <k-btn class="md-success" :data-handler="handleRefresh" style="z-index:3">
              <md-icon md-src="/static/svg/icon/reset.svg"></md-icon>刷新
            </k-btn> -->
          </div>
      </div>
			<k-grid ref="tableGrid" @data-row-select="selectRow" data-action="DataClearManage.findDataClearManages" data-fixed="right" data-operate-width="250px" :dataPageSize="0" :data-autoload="false">
				<k-grid-column data-header="TASK_ID" data-name="taskId" data-hidden="true"></k-grid-column>
				<k-grid-column data-header="任务名称" data-name="taskName"></k-grid-column>
				<k-grid-column data-header="清算日期" data-name="taskDate" data-type="date" data-date-format="yyyy-MM-dd"></k-grid-column>
				<k-grid-column data-header="执行日期" data-name="execDate" data-type="date" data-date-format="yyyy-MM-dd"></k-grid-column>
				<k-grid-column data-header="执行开始时间" data-name="startTime"></k-grid-column>
				<k-grid-column data-header="执行结束时间" data-name="endTime"></k-grid-column>
				<k-grid-column data-header="执行状态" data-name="execStatus" data-dict="batch_task_status"></k-grid-column>
				<k-grid-column data-header="执行结果" data-name="rtnDesc"></k-grid-column>
				<template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="DataClearManage.reBatchClearManage" data-size="mini"
                 data-type="danger" data-target="tableGrid" :data-confirm="true" data-descript="重跑报送任务">
            重跑
          </k-btn>
        </template>
			</k-grid>
		</div>
		<k-popup ref="reBatchPopup">
      <k-form ref="reBatchPopupForm" :data-col="2" isFormBodyScreen>
         <k-form-item label="清算日期" data-label-width="100px">
           <k-field-date v-model="formData.taskDate" data-type="date" data-date-format="yyyy-MM-dd"
                         data-value-format="yyyyMMdd" :data-allowblank="false"/>
         </k-form-item>
         <k-form-footer slot="footer" data-align="center">
           <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-from="reBatchPopupForm" data-target="tableGrid"
                  @click="reBatch" :data-model="formData"><md-icon md-src="/static/svg/confirm.svg"></md-icon>确定</k-btn>
           <k-btn class="btn-custom-plain" data-functype="CLOSE"><md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
         </k-form-footer>
      </k-form>
    </k-popup>
	</div>
</template>

<script>
import Tools from "@/utils/tools";
export default {
	name: "DataClearManage",
	data() {
		return {
			formData: {},
			selectRowData:{},
			searchParam: {},
		};
	},
	methods: {
		selectRow(row, column, event) {
			this.selectRowData = assign({}, row);
      this.formData = assign({}, row);
		},
		handleRefresh(){
		  this.$refs.tableGrid.load(this.searchParam);
		},
		reBatch(){
		  this.httpUtil.comnUpdate({
        action: 'DataClearManage.reBatchClearManage',
        params: this.formData,
        successAlert: true
      }).then(data => {
        this.$refs.reBatchPopup.close();
        this.$refs.tableGrid.load(this.searchParam);
      });
		}
	},
	created() {
    this.httpUtil.sysDate().then(res => {
      if (res) {
        this.$set(this.searchParam, 'taskDate', res.toString());
        this.$nextTick(()=>{this.$refs.tableGrid.load(this.searchParam)});
      }
    });
  },
  mounted () {
    setInterval(() => {
      this.$refs.tableGrid.load(this.searchParam);
    }, 5000);
  },
};
</script>

