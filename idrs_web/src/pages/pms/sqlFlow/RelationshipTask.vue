<template>
	<div class="py-page">
		<div>
			<k-form-search-customize
				data-model-name="RmsFieldInfo"
				data-label-width="80px"
				v-model="searchParam"
				data-target="tableGrid"
			>
        <k-form-item label="表名称">
<!--          <k-field-select v-model="searchParam.tableName" data-action="RmsTableInfo.findRmsTableInfoDict"-->
<!--                          data-display-field="tableName,comment" data-value-field="tableName"/>-->
          <k-field-text v-model="searchParam.tableName"/>
        </k-form-item>
				<k-form-item label="任务名称">
					<k-field-select v-model="searchParam.taskId" data-action='RmsTableInfo.findTaskInfoDict'
                          data-display-field="taskId,taskName" data-value-field="taskId"/>
				</k-form-item>
        <k-form-item label="表所属层级">
          <k-field-select data-dict="table_owner" v-model="searchParam.owner"/>
        </k-form-item>
			</k-form-search-customize>
		</div>
		<div class="py-page-container">
			<div class="table-part">
				<k-grid
					ref="tableGrid"
					@data-row-select="selectRow"
					data-action="RmsTableInfo.findRelationshipTask"
					data-operate-width="320px"
				>
          <k-grid-column data-header="表id" data-name="tableInfoId" data-hidden="true"></k-grid-column>
					<k-grid-column data-header="库名" data-name="databaseName"></k-grid-column>
					<k-grid-column data-header="表名" data-name="tableName"></k-grid-column>
					<k-grid-column data-header="表注释" data-name="comment"></k-grid-column>
					<k-grid-column data-header="表所属层级" data-name="owner" data-dict="table_owner"></k-grid-column>
					<k-grid-column data-header="exeid" data-name="exeid"></k-grid-column>
					<k-grid-column data-header="任务id" data-name="taskId"></k-grid-column>
					<k-grid-column data-header="任务名称" data-name="taskName"></k-grid-column>
          <k-grid-column data-header="任务所属模块" data-name="taskModel" data-dict="task_model"></k-grid-column>
					<template slot="operate" slot-scope="scope">
						<k-btn
							class="btn-custom-text"
							data-target="rerunTaskPopup"
							data-functype="POPUP"
							data-size="mini"
							@click="handleTask('upAll', scope.row.row)"
						>
							上游任务重跑
						</k-btn>
						<k-btn
							class="btn-custom-text"
							data-target="rerunTaskPopup"
							data-functype="POPUP"
							data-size="mini"
							@click="handleTask('downAll', scope.row.row)"
						>
							下游任务重跑
						</k-btn>
					</template>
				</k-grid>
			</div>
			<div class="table-part">
				<div class="part part1">
					<div class="title">上游任务列表</div>
					<div class="part-container">
						<k-grid ref="tableGrid1" data-action="RmsTableInfo.findUpStreamTask" data-operate-width="60px">
							<k-grid-column data-header="任务id" data-name="taskId"></k-grid-column>
							<k-grid-column data-header="任务名称" data-name="taskName"></k-grid-column>
              <k-grid-column data-header="任务所属模块" data-name="taskModel" data-dict="task_model"></k-grid-column>
							<template slot="operate" slot-scope="scope">
								<k-btn
									class="btn-custom-text"
                  data-target="rerunTaskPopup"
									data-functype="POPUP"
									data-size="mini"
									@click="handleTask('up', scope.row.row)"
								>
									重跑
								</k-btn>
							</template>
						</k-grid>
					</div>
				</div>
				<div class="part part2">
					<div class="title">下游任务列表</div>
					<div class="part-container">
						<k-grid ref="tableGrid2" data-action="RmsTableInfo.findDownStreamTask" data-operate-width="60px">
							<k-grid-column data-header="任务id" data-name="taskId"></k-grid-column>
							<k-grid-column data-header="任务名称" data-name="taskName"></k-grid-column>
              <k-grid-column data-header="任务所属模块" data-name="taskModel" data-dict="task_model"></k-grid-column>
							<template slot="operate" slot-scope="scope">
								<k-btn
									class="btn-custom-text"
									data-target="rerunTaskPopup"
									data-functype="POPUP"
									data-size="mini"
									@click="handleTask('down', scope.row.row)"
								>
									重跑
								</k-btn>
							</template>
						</k-grid>
					</div>
				</div>
			</div>
		</div>
		<k-popup ref="rerunTaskPopup" data-width="25%" data-title="任务重跑">
			<div class="rerunTask-container">
				<k-form ref="runTaskForm" :data-col="0" data-label-width="100px">
          <k-form-item label="表id" v-show="false">
            <k-field-text v-model="formData.tableInfoId" :data-allowblank="true"/>
          </k-form-item>
          <k-form-item label="任务id" v-show="false">
            <k-field-text v-model="formData.taskId" :data-allowblank="true"/>
          </k-form-item>
          <k-form-item label="重跑类型" v-show="false">
            <k-field-text v-model="formData.taskType" :data-allowblank="false"/>
          </k-form-item>
					<k-form-item label="重跑日期">
						<k-field-date v-model="formData.date" :data-allowblank="false">
						</k-field-date>
					</k-form-item>
					<k-form-footer data-align="center">
						<k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="RmsTableInfo.updateTask" data-from="runTaskForm"
							:data-model="formData">
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
						</k-btn>
						<k-btn class="btn-custom-plain" data-functype="CLOSE">
						<md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
					</k-form-footer>
				</k-form>
			</div>
		</k-popup>
	</div>
</template>
<script>
import {merge} from "lodash";

export default {
	data() {
		return {
			searchParam: {},
			selectRowData: {},
			formData: {},
		};
	},
	methods: {
		selectRow(row, column, event) {
			this.selectRowData = Object.assign({}, row);
			this.formData = Object.assign({}, row);
			this.loadChildGrid(row);
		},

    async loadChildGrid(row) {
      this.$refs.tableGrid1.loadData([]);
      this.$refs.tableGrid2.loadData([]);
      let list1 = await this.reloadChildGridData("RmsTableInfo.findUpStreamTask", row);
      let list2 = await this.reloadChildGridData("RmsTableInfo.findDownStreamTask", row);
      this.$refs.tableGrid1.loadData(list1);
      this.$refs.tableGrid2.loadData(list2);
    },

    async reloadChildGridData(action, params) {
      let list = [];
      await this.httpUtil.comnQuery({
          action: action,
          params: params
        }).then(data => {
          list = data.rows;
        });
      return list;
    },

		handleTask(type, row) {
      this.$set(this.formData, 'taskType', type);
			if (type === 'upAll' || type === 'downAll') {
        this.$set(this.formData,'tableInfoId', row.tableInfoId);
			} else if (type === 'up' || type === 'down') {
        this.$set(this.formData,'taskId', row.taskId);
			}
      this.$set(this.formData,'date', '');
		}
	},
};
</script>
<style lang="scss" scoped>
.py-page-container {
	.table-part {
		flex: 1;
		display: flex;
		.part {
			flex: 1;
			display: flex;
			flex-direction: column;
			min-width: 0;
			&:first-child {
				margin-right: 10px;
			}
			.title {
				color: rgba(4, 79, 156, 0.9);
				font-weight: 500;
				margin-bottom: 3px;
				&::before {
					content: "";
					display: inline-block;
					width: 3px;
					height: 11px;
					background: rgba(4, 79, 156, 0.9);
					margin-right: 5px;
				}
			}
			.part-container {
				flex: 1;
				display: flex;
			}
		}
	}
}
.rerunTask-container {
	padding: 30px 0;
}
</style>
