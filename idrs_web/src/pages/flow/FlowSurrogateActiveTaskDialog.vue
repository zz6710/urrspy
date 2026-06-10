<template>
	<div>
		<k-flow-grid :task-info="taskInfo"></k-flow-grid>
		<k-flow-form :task-info="taskInfo"></k-flow-form>
		<k-flow-upload :task-info="taskInfo"></k-flow-upload>
		<k-flow-formInfo :task-info="taskInfo"></k-flow-formInfo>
    <k-flow-modify-information :task-info="taskInfo"></k-flow-modify-information>
  </div>
</template>

<script>
export default {
	name: "FlowSurrogateActiveTaskDialog",
	props: {
		taskInfo: {},
	}
};
</script>

<style>
</style>
