<!---预测流程图走向-->
<template>
  <div>
    <process-viewer :key="`designer-${loadIndex}`" :style="{height: '400px'}" :xml="xmlData" :finishedInfo="finishedInfo" />
  </div>
</template>
  
<script>
import ProcessViewer from "./ProcessViewer";
export default {
	name: "FlowCalculateViewer",
	components: { ProcessViewer },
	props: {
		procKey: {
			type: String,
		},
		variables: {
			type: Object,
			default: () => {},
		},
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
		Promise.all([this.getFlowViewer(), this.getModelDetail()]).then(() => {
			this.loadIndex = this.procKey;
		});
	},
	mounted() {},
	methods: {
		getModelDetail() {
			// 发送请求，获取xml
			return this.httpUtil
				.ajaxJson({
					url: "wf/calculateProcess/getFlowView.json",
					params: {
						procKey: this.procKey,
					},
				})
				.then((response) => {
					this.xmlData = response.data;
				});
		},
		getFlowViewer() {
			return this.httpUtil
				.ajaxJson({
					url: "wf/calculateProcess/getFlowViewRun.json",
					params: {
						procKey: this.procKey,
						variables: this.variables,
					},
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
  