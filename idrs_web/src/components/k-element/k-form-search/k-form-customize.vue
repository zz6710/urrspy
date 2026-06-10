<template>
  <md-card class="box-card" style="overflow: visible;">
    <md-card-header class="md-card-header-text md-card-header-green" style="margin-right: 0;">
      <div class="search-header">
        <div class="card-icon" :style="iconStyle">
          <md-icon md-src="/static/svg/form.svg"></md-icon>
        </div>
        <div>
          <i class="el-icon-d-caret" @click="show"></i>
        </div>
      </div>
    </md-card-header>

    <div slot="header" class="clearfix" style="text-align:right">

    </div>
    <div class="show-form" id="show-form" style="padding:20px;">
      <slot></slot>
    </div>

    <div class="k-form-search-footer">
      <slot name="button"></slot>

    </div>

  </md-card>
</template>

<script>
  import emitter from "@/components/k-element/common/k-emitter.js";
  import props from "@/components/k-element/common/k-field-props.js";

  export default {
    name: 'kFormCustomize',
    mixins: [props(), emitter()],
    props: {
      dataTarget: {
        type: String
      }
    },
    data() {
      return {
        extends: true,
      }
    },
    computed: {
      iconStyle() {
        let iconStyle = {};
        iconStyle.background = this.$store.state.system.cardBackground
        return iconStyle;
      }
    },
    methods: {

      show() {
        let e = document.getElementById('show-form')
        if (this.extends) {
          e.style.display = "none"
        } else {
          e.style.display = ""
        }
        this.extends = !this.extends
      },

    }

  }
</script>

<style lang="scss" scoped>
  @import './k-form-search.scss';

  ::v-deep .k-form-footer-align-default{
    margin-left: 0px;
  }

  .k-form-search-footer {
    margin-top: 0px;
  }


</style>
