<template>
  <div class="panel-tab__content">
    <div class="element-property input-property">
      <div class="element-property__value">
        <el-tooltip content="关闭时，审批人审批任务通过后，若后续的任务再次出现该审批人，自动审批通过。" placement="top-start" @click.stop.prevent>
          <i class="header-icon el-icon-info"></i>
        </el-tooltip>
        <el-radio-group v-model="repeatApproved" @change="changeRepeatApproved">
          <el-radio label="1">开启</el-radio>
          <el-radio label="0">关闭</el-radio>
        </el-radio-group>
      </div>
    </div>
  </div>
</template>
  
<script>
export default {
	name: "ElementRepeatApproved",
	props: {
		id: String,
		type: String,
	},
	data() {
		return {
			repeatApproved: "1",
		};
	},
	watch: {
		id: {
			immediate: true,
			handler(val) {
				val &&
					val.length &&
					this.$nextTick(() => {
						this.reset();
					});
			},
		},
	},
	methods: {
		reset() {
			this.bpmnELement = window.bpmnInstances.bpmnElement;
			this.repeatApproved = this.bpmnELement.businessObject.repeatApproved ?? "1";
		},
		changeRepeatApproved() {
			window.bpmnInstances.modeling.updateProperties(this.bpmnELement, { repeatApproved: this.repeatApproved });
		},
	},
};
</script>
  