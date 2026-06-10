<template>
  <div class="switch-mark">
    <div class="switch-mark-icon" @click="switchMark">
      <em class="el-icon-menu" v-if="showType == 'chart'" />
      <em class="el-icon-s-data" v-else />
    </div>
    <md-button
      class="md-white"
      size="mini"
      v-if="showType == 'table' && showExportBtn"
      @click="handleExport"
      :disabled="loading"
    >
      <em v-show="loading" class="el-icon-loading"></em>
      <md-icon v-show="!loading">cloud_download</md-icon>
      导出
    </md-button>
  </div>
</template>
<script>
export default {
  props: {
    showExportBtn: {
      type: Boolean,
      default: true
    },
    type: {
      type: String,
      default: ''
    },
    dataAction: String,
    dataExportName: String
  },
  data() {
    return {
      showType: 'chart',
      loading: false
    }
  },
  mounted() {
    this.showType = this.type || 'chart'
    this.$parent.setType && this.$parent.setType(this.showType)
  },
  methods: {
    switchMark() {
      this.showType = this.showType == 'chart' ? 'table' : 'chart'
      if (this.$parent.setType) {
        this.$parent.setType(this.showType)
      }
    },
    handleExport() {
      if (this.$parent.handleExport) {
        this.loading = true
        this.$parent.handleExport(this.dataAction, this.dataExportName, ()=>{
          this.loading = false
        })
      }
    }
  }
}
</script>
<style scoped lang="scss">
.switch-mark {
  position: absolute;
  right: 10px;
  top: 10px;
  display: flex;
  align-items: center;
  opacity: 0.8;
  .switch-mark-icon {
    color:#fff;
    cursor: pointer;
    font-size: 20px;
  }
  .md-icon {
    color: rgba(88,158,248, 1) !important;
    font-size: 22px !important;
  }
  .md-button {
    margin: 0 0 0 10px;
    color: rgba(88,158,248, 1) !important;
    /deep/ .md-button-content {
      font-weight: bolder;
    }
    /deep/ .md-ripple {
      padding: 4px 10px !important;
    }
    &:hover {
      color: rgba(88,158,248, 1) !important;
      .md-icon {
        color: rgba(88,158,248, 1) !important;
      }
    }
  }
}
</style>
