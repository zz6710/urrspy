<template>
  <div class="k-form-footer-content" :class="[{
      'k-form-footer-align-left': dataAlign === 'left',
      'k-form-footer-align-center': dataAlign === 'center',
      'k-form-footer-align-right': dataAlign === 'right',
      'k-form-footer-align-default': !dataAlign
    }]">
    <slot></slot>
  </div>
</template>
<script>
  export default {
    name: 'KFormFooter',

    provide() {
      return {
        kFormItem: this
      };
    },
    inject: {
      kForm: {
        default: null
      }
    },
    props: {
      dataAlign: {
        type: String
      },
    },
    created() {},

    data() {
      return {};
    }

  };
</script>

<style lang="scss">
  @import "./k-form-footer.scss";
</style>
