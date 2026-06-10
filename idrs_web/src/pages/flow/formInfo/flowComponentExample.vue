<template>
  <div>
    <div>
      {{taskInfo}}
    </div>
    <div>
      {{submitData}}
    </div>
  </div>

</template>

<script>
  export default {
    name: 'FlowComponentExample',
    props: {
      taskInfo: {},
      submitData: {}
    },
    data() {
      return {
      };
    },
  };
</script>
