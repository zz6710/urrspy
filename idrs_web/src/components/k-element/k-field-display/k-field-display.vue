<template>
  <span v-if="dataType == 'html'" v-html="displayValue"/>
  <span class="ds-span" v-else>{{displayValue}}</span>
</template>

<script>
  import props from "@/components/k-element/common/k-field-props.js";
  import event from "@/components/k-element/common/k-field-event.js";
  import emitter from "@/components/k-element/common/k-emitter.js";
  import Tools from "@/utils/tools.js";

  export default {
    name: 'KFieldDisplay',
    mixins: [props(), event(), emitter()],
    props: {
      dataType: ''
    },
    computed:{
    },
    data() {
      return {
        displayValue: '',
        height:"32px"
      }
    },
    methods:{
    },
    watch:{
      value(newVal){
        if (this.dataDict) {
          this.httpUtil.dictTransfer(this.dataDict, newVal).then(data => {
            this.displayValue = data;
          });
        } else {
          if (this.dataType) {
            if (this.dataType === "date") {
              this.displayValue = Tools.formatDate(newVal);
            } else if (this.dataType === "time") {
              this.displayValue = Tools.formatTime(newVal);
            } else if (this.dataType === "money") {
              this.displayValue = Tools.formatMoney(newVal);
            } else {
              this.displayValue = newVal;
            }
          } else {
            this.displayValue = newVal;
          }
        }
      }
    },
    mounted() {
      if (this.dataDict) {
        this.httpUtil.dictTransfer(this.dataDict, this.value).then(data => {
          this.displayValue = data;
        });
      } else {
        if (this.dataType) {
          if (this.dataType === "date") {
            this.displayValue = Tools.formatDate(this.value);
          } else if (this.dataType === "time") {
            this.displayValue = Tools.formatTime(this.value);
          } else if (this.dataType === "money") {
            this.displayValue = Tools.formatMoney(this.value);
          } else {
            this.displayValue = this.value;
          }
        } else {
          this.displayValue = this.value;
        }
      }
    }
  }
</script>

<style scoped>
  .ds-span {
    min-height: 32px;
    line-height: 1.5;
  }
</style>
