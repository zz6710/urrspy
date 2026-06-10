<template>
  <div>
    <process-viewer :key="`designer-${loadIndex}`" :style="{height: '400px'}" :xml="xmlData" :finishedInfo="finishedInfo" />
  </div>
</template>
  
<script>
import ProcessViewer from "./ProcessViewer";
export default {
	name: "flowViewer",
	components: { ProcessViewer },
	props: {
		taskInfo: {},
	},
	data() {
		return {
			loadIndex: "",
			xmlData: "",
			finishedInfo: {
				finishedSequenceFlowSet: [],
				finishedTaskSet: [],
				unfinishedTaskSet: [],
				rejectedTaskSet: [],
			},
		};
	},
	created() {
		Promise.all([this.getFlowViewer(this.taskInfo.procInsId), this.getModelDetail(this.taskInfo.procDefId)]).then(() => {
			this.loadIndex = this.taskInfo.procInsId;
		});
	},
	mounted() {},
	methods: {
		getModelDetail(definitionId) {
			// 发送请求，获取xml
			return this.httpUtil
				.ajaxJson({
					url: "wf/deploy/bpmnXml/get/" + definitionId + ".json",
				})
				.then((response) => {
					this.xmlData = response.data;
				});
		},
		getFlowViewer(procInsId) {
			return this.httpUtil
				.ajaxJson({
					url: "wf/task/flowViewer/" + procInsId + ".json",
				})
				.then((res) => {
					let data = res.data;
					if (data) {
						this.finishedInfo.finishedTaskSet = data.finishedTaskSet;
						this.finishedInfo.unfinishedTaskSet = data.unfinishedTaskSet;
						this.finishedInfo.rejectedTaskSet = data.rejectedTaskSet;
						this.finishedInfo.finishedSequenceFlowSet = data.finishedSequenceFlowSet;
					}
				});
		},
	},
};
</script>
  