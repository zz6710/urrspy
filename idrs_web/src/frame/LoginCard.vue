<template>
  <md-card class="md-card-login" :class="{ 'md-card-hidden': cardHidden }">
    <md-card-header class="md-card-header-" :style="loginChard" v-if="showHeader">
      <slot name="title"></slot>
      <div class="social-line">
        <slot name="buttons"></slot>
      </div>
    </md-card-header>

    <md-card-content>
      <slot name="description"></slot>
      <div class="login-line">
        <div class="f1"></div>
        <div class="f2"></div>
        <div class="f3"></div>
      </div>
      <slot name="sub-title"></slot>
      <slot name="inputs"></slot>
    </md-card-content>

    <md-card-actions>
      <slot name="footer"></slot>
    </md-card-actions>
  </md-card>
</template>

<script>
  export default {
    name: "login-card",
    props: {
      loginConfig: {
        type: Object,
        default: new Object()
      },
      showHeader: {
        type: Boolean,
        default: true
      }
    },
    data() {
      return {
        cardHidden: true
      };
    },
    beforeMount() {
      setTimeout(this.showCard, 400);
    },
    methods: {
      showCard: function() {
        this.cardHidden = false;
      },
    },
    computed: {
      loginChard() {
        const ret = {};
        ret.background = '#044f9c'; //this.loginConfig.loginColor;
        return ret;
      }
    }
  };
</script>

<style lang="scss" scoped>
  .social-line {
    height: 91px;
    top: 50%;
    padding-top: 33px;
  }

  .md-just-icon .md-ripple {
    width: 23px !important;
    height: 23px !important;
  }
  .md-card-login {
    background: rgba(185,225,255,.4);
    border: 2px solid rgba(255,255,255,.3);
    box-shadow: 0 0 16px #fff9;
    border-radius: 4px;
    padding: 56px 60px;
    max-width: inherit;
    width: 460px;
  }
  .md-card-content {
    padding: 0;
  }
  .login-line {
    height: 6px;
    display: flex;
    margin-bottom: 20px;
    & > div {
      flex: 1;
    }
    .f1 {
      background: #AC011D;
    }
    .f2 {
      background: #DE1627;
    }
    .f3 {
      background: #EB8790;
    }
  }
</style>
