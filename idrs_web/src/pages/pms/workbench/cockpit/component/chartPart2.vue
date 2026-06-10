<template>
  <div class="chart-part">
    <div :class="btnAlign=='center' ? 'btn-center' : 'btn-between'">
      <CockpitBtn :name="btnName" :type="type" />
      <div class="icon-download"
           v-if="hasExport"
           @click="showTable"
      >
        <img :src="require('@/assets/img/cockpit/windmill.png')" alt="" />
        <!-- <md-icon class="icon">cloud_download</md-icon> -->
      </div>
      <div class="icon-download"
           v-if="global.isShowAuthorityButton('Cockpit.editCockpitSalesTarget')"
           @click="showTable2"
      >
        <img :src="require('@/assets/img/cockpit/setting_icon.png')" alt="" />
        <!-- <md-icon class="icon">cloud_download</md-icon> -->
      </div>
    </div>
    <div class="chart-part-container">
      <div class="chart-part-select">
        <slot name="select"></slot>
      </div>
      <slot></slot>
    </div>
  </div>
</template>
<script>
import CockpitBtn from '@/pages/pms/workbench/cockpit/component/btn.vue'

export default {
  components: {
    CockpitBtn
  },
  props: {
    btnName: {
      type: String,
      default: ''
    },
    type: {
      type: String,
      default: 'type1'
    },
    btnAlign: String,
    hasExport: {
      type: Boolean,
      default: true
    }
  },
  methods: {
    showTable() {
      this.$emit('showTable')
    },
    showTable2() {
      this.$emit('showTable2')
    }
  }
}
</script>
<style lang="scss" scoped>
.chart-part {
  .chart-part-container {
    margin-top: 10px;
    .chart-part-select {
      display: flex;
      justify-content: flex-end;
    }
  }
  .btn-center {
    display: flex;
    justify-content: center;
  }
  .btn-between {
    display: flex;
    justify-content: space-between;
  }
  .icon-download {
    display: flex;
    align-items: center;
    width: 15px;
    margin-left: 10px;
    cursor: pointer;
    .icon {
      color: rgba(255, 255, 255, 0.2) !important;
      font-size: 18px !important;
    }
  }
}
</style>
