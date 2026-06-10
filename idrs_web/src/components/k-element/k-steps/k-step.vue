<template>
  <div :style="{'width': width,'display': 'contents'}" v-show="show">
    <slot></slot>
  </div>
</template>

<script>
  import emitter from "@/components/k-element/common/k-emitter.js";

  export default {
    name: 'KStep',
    mixins: [emitter()],

    props: {
      dataTitle: {
        type: String
      },
      dataDescription: {
        type: String
      },
      dataIcon: {
        type: String
      }
    },
    data() {
      return {
        show: false,
        width: '318px'
      };
    },
    beforeCreate() {
      this.$parent.$parent.steps.push(this);
    },
    mounted() {
      const form = this.getParent('KForm');
      this.width = form.formStyle.width
    }


  };
</script>
