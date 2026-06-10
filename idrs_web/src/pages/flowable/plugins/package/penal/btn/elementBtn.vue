<template>
  <div class="panel-tab__content">
    <div class="element-property input-property">
      <div class="element-property__value">
        <el-checkbox-group v-model="btnList" @change="changeBtn">
          <el-checkbox :key="item.value" v-for="item in btnGroup" :label="item.value">{{item.label}}</el-checkbox>
        </el-checkbox-group>

      </div>
    </div>
    <div class="element-property input-property">
      <div class="element-property__label">驳回节点：</div>
      <div class="element-property__value">
        <el-select v-model="taskIds" multiple size="mini" placeholder="请选择 用户节点" @change="changeRejectTask">
          <el-option v-for="item in taskList" :key="item.taskDefKey" :label="item.taskName" :value="item.taskDefKey">
          </el-option>
        </el-select>
      </div>
    </div>
  </div>
</template>
  
<script>
import wfStatus from "@/pages/flowable/enum/enum.js";
export default {
	name: "elementBtn",
	props: {
		id: String,
		type: String,
	},
	inject: {
		prefix: "prefix",
		width: "width",
	},
	data() {
		return {
			btnList: [],
			btnGroup: Object.values(wfStatus.taskBtns),
			taskIds: [],
			taskList: [],
		};
	},
	created() {
	},
	watch: {
		id: {
			immediate: true,
			handler(val) {
				val &&
					val.length &&
					this.$nextTick(() => {
						this.gettaskList();
						this.reset();
					});
			},
		},
	},
	methods: {
		reset() {
			this.bpmnELement = window.bpmnInstances.bpmnElement;
			let btnsStr = this.bpmnELement.businessObject.btns;
			if (btnsStr) {
				this.btnList = btnsStr.split(",");
			} else {
				this.btnList = [];
			}

			let rejectTasksStr = this.bpmnELement.businessObject.rejectTasks;
			if (rejectTasksStr) {
				this.taskIds = rejectTasksStr.split(",");
			} else {
				this.taskIds = [];
			}
		},
		changeBtn() {
			window.bpmnInstances.modeling.updateProperties(this.bpmnELement, { btns: this.btnList.join(",") });
		},
		changeRejectTask(){
			window.bpmnInstances.modeling.updateProperties(this.bpmnELement, { rejectTasks: this.taskIds.join(",") });
		},
		gettaskList() {
			window.bpmnInstances.modeler.saveXML({ format: true }).then(({ xml }) => {
				this.httpUtil
					.ajaxJson({
						url: "wf/model/rejectTaskList.json",
						params: { bpmnXml: xml, taskDefKey: this.bpmnELement.businessObject.id },
					})
					.then((res) => {
						this.taskList = res.rows;
					});
			});
		},
	},
};
</script>
  