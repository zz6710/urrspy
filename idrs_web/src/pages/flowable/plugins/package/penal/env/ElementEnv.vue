<template>
	<div class="panel-tab__content">
	  <div class="element-property input-property">
		<div class="element-property__value">
		  <el-select v-model="envId" size="mini" clearable placeholder="请选择 流程参数" @change="changeSelectEnv">
			<el-option v-for="item in envs" :key="item.envId" :label="item.displayName" :value="item.envId">
			</el-option>
		  </el-select>
		</div>
	  </div>
	</div>
  </template>
  
  <script>
  export default {
	  name: "ElementEnv",
	  props: {
		  id: String,
	  },
	  inject: {
		  prefix: "prefix",
		  width: "width",
	  },
	  data() {
		  return {
			  envId: "",
			  otherExtensionList: [],
			  bpmnElementEnv: [],
			  envs: [],
		  };
	  },
	  created() {
		  this.getEnvList();
	  },
	  watch: {
		  id: {
			  immediate: true,
			  handler: function (id) {
				  if (id && id.length) {
					  this.$nextTick(() => {
						  this.resetEnv();
						  if (this.bpmnElementEnv.length > 0) {
							  this.envId = this.bpmnElementEnv[0].id;
						  }
					  });
				  } else {
					  this.envId = "";
				  }
			  },
		  },
	  },
	  methods: {
		  getEnvList() {
			  this.httpUtil
				  .ajaxJson({
					  url: "wf/env/list.json",
				  })
				  .then((res) => {
					  this.envs = res.rows;
				  });
		  },
		  resetEnv() {
			  this.bpmnElement = window.bpmnInstances.bpmnElement;
			  this.otherExtensionList = []; // 其他扩展配置
			  this.bpmnElementEnv =
				  this.bpmnElement.businessObject?.extensionElements?.values?.filter((ex) => {
					  if (ex.$type !== `${this.prefix}:Env`) {
						  this.otherExtensionList.push(ex);
					  }
					  return ex.$type === `${this.prefix}:Env`;
				  }) ?? {};
		  },
		  changeSelectEnv(val) {
			  // 新建上下文
			  this.resetEnv();
			  const envObject = window.bpmnInstances.moddle.create(`${this.prefix}:Env`, { id: val });
			  this.updateElementExtensions(envObject);
		  },
		  updateElementExtensions(env) {
			  const extensions = window.bpmnInstances.moddle.create("bpmn:ExtensionElements", {
				  values: this.otherExtensionList.concat([env]),
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
  