<template>
  <div class="panel-tab__content">
    <div class="element-property input-property">
      <div class="element-property__value">
        <el-select v-model="validateId" size="mini" clearable placeholder="请选择 回调报文校验" @change="changeSelectvalidate">
          <el-option v-for="item in validates" :key="item.id" :label="item.name" :value="item.id">
          </el-option>
        </el-select>
      </div>
    </div>
  </div>
</template>

<script>
export default {
	name: "ElementCallbackValidate",
	props: {
		id: String,
	},
	inject: {
		prefix: "prefix",
		width: "width",
	},
	data() {
		return {
			validateId: "",
			otherExtensionList: [],
			bpmnElementvalidate: [],
			validates: [],
		};
	},
	created() {
		this.getvalidateList();
	},
	watch: {
		id: {
			immediate: true,
			handler: function (id) {
				if (id && id.length) {
					this.$nextTick(() => {
						this.resetvalidate();
						if (this.bpmnElementvalidate.length > 0) {
							this.validateId = this.bpmnElementvalidate[0].id;
						}
					});
				} else {
					this.validateId = "";
				}
			},
		},
	},
	methods: {
		getvalidateList() {
			this.httpUtil
				.ajaxJson({
					url: "wf/validateConfig/list.json",
				})
				.then((res) => {
					this.validates = res.rows;
				});
		},
		resetvalidate() {
			this.bpmnElement = window.bpmnInstances.bpmnElement;
			this.otherExtensionList = []; // 其他扩展配置
			this.bpmnElementvalidate =
				this.bpmnElement.businessObject?.extensionElements?.values?.filter((ex) => {
					if (ex.$type !== `${this.prefix}:Validate`) {
						this.otherExtensionList.push(ex);
					}
					return ex.$type === `${this.prefix}:Validate`;
				}) ?? {};
		},
		changeSelectvalidate(val) {
			this.resetvalidate();
			const validateObject = window.bpmnInstances.moddle.create(`${this.prefix}:Validate`, { id: val });
			this.updateElementExtensions(validateObject);
		},
		updateElementExtensions(validate) {
			const extensions = window.bpmnInstances.moddle.create("bpmn:ExtensionElements", {
				values: this.otherExtensionList.concat([validate]),
			});
			window.bpmnInstances.modeling.updateProperties(this.bpmnElement, {
				extensionElements: extensions,
			});
		},
	},
	beforeDestroy() {
		this.bpmnElement = null;
	},
};
</script>
