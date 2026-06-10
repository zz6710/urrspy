<template>
  <div class="panel-tab__content">
    <div class="element-property input-property">
      <div class="element-property__value">
        <el-radio-group v-model="enableFileUpload" @change="changeEnableFileUpload">
          <el-radio label="1">开启</el-radio>
          <el-radio label="0">关闭</el-radio>
        </el-radio-group>
      </div>
    </div>
  </div>
</template>
  
<script>
export default {
	name: "elementFileUpload",
	props: {
		id: String,
		type: String,
	},
	data() {
		return {
			enableFileUpload: "",
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
			this.enableFileUpload = this.bpmnELement.businessObject.enableFileUpload ?? "0";
		},
		changeEnableFileUpload() {
			window.bpmnInstances.modeling.updateProperties(this.bpmnELement, { enableFileUpload: this.enableFileUpload });
		},
	},
};
</script>
  