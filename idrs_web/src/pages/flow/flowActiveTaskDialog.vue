<template>
  <div>
    <k-flow-grid :task-info="taskInfo"></K-flow-grid>
    <k-flow-upload :task-info="taskInfo"></k-flow-upload>
    <k-flow-modify-information :task-info="taskInfo"></k-flow-modify-information>
    <k-flow-form :task-info="taskInfo"  @submitClose="popupClose"></k-flow-form>
    <k-flow-formInfo :task-info="taskInfo"></k-flow-formInfo>

  </div>
</template>

<script>
  export default {
    name: "flowActiveTaskDialog",
    props: {
      taskInfo: {},
    },
    methods: {
      popupClose(){
        this.$emit('submitClose', '1')
      }
    }
  };
</script>

<style>
</style>
