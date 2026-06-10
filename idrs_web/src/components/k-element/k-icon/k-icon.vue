<template>
    <div>
        <md-icon :md-src="mdSrc"></md-icon>
    </div>
</template>

<script>
import props from "@/components/k-element/common/k-field-props.js";
  import event from "@/components/k-element/common/k-field-event.js";
  import emitter from "@/components/k-element/common/k-emitter.js";

  export default {
    name: 'KIcon',
    mixins: [props(), event(), emitter()],
    props: {
      dataSrc: {
          type: String,
          default: 'add'
      }
    },
    data() {
        return {
            src: '',
            mdSrc: '/static/svg/' + this.dataSrc + '.svg'
        }
    }
  };
</script>

<style>

</style>