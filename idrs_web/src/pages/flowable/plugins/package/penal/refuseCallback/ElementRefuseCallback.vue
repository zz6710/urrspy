<template>
  <div class="panel-tab__content">
    <div class="element-property input-property">
      <div class="element-property__value">
        <el-select v-model="refuseCallback" size="mini" clearable placeholder="请选择 流程参数" @change="changeRefuseCallback" filterable>
          <el-option v-for="item in envItems" :key="item.envItemId" :label="item.itemKey" :value="item.envItemId">
          </el-option>
        </el-select>
      </div>
    </div>
  </div>
</template>
		
<script>
import { getEnvItems } from "../../utils";
export default {
	name: "ElementRefuseCallback",
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
			refuseCallback: "",
			envItems: [],
		};
	},
	created() {},
	watch: {
		id: {
			immediate: true,
			handler(val) {
				this.bpmnELement = window.bpmnInstances.bpmnElement;
				val &&
					val.length &&
					this.$nextTick(() => {
						this.getEnvItemList();
						this.reset();
					});
			},
		},
	},
	methods: {
		reset() {
			this.refuseCallback = this.bpmnELement.businessObject.refuseCallback;
		},
		changeRefuseCallback() {
			window.bpmnInstances.modeling.updateProperties(this.bpmnELement, { refuseCallback: this.refuseCallback });
		},
		getEnvItemList() {
			getEnvItems().then((rows) => {
				this.envItems = rows;
			});
		},
	},
};
</script>
		