<template>
  <div class="cockpit-dialog">
    <el-dialog
      width="70%"
      :title="title"
      :visible.sync="show"
      custom-class="cockpit-el-dialog"
      @close="close"
    >
      <div slot="title" class="title-box">
        <div class="title">{{title}}</div>
        <el-select
          size="mini"
          v-model="optionValue"
          placeholder="请选择数据类型"
          clearable
          popper-class="type-select"
          v-show=""
          v-if="tableData.tableHeader && tableData.tableHeader.length && ('创设'!=title && '申报'!=title && '发行'!=title && '未来一年内销售只数预测（募集、开放、到期）'!=title)"
          @change="change"
        >
          <el-option
            :value="item.value"
            :label="item.label"
            v-for="item in options"
            :key="item.value">
          </el-option>
        </el-select>
      </div>
      <div class="cockpit-dialog-container">
        <div class="cockpit-dialog-content">
          <div
            class="loading-box"
            v-if="tableLoading"
            v-loading="true"
            element-loading-text="加载中..."
            element-loading-background="rgba(0, 0, 0, 0)"
          ></div>
          <MarketTable
            v-else
            :tableData="tableData"
          />
        </div>
        <div class="cockpit-dialog-btn" v-if="tableData.tableContent">
          <md-button
            class="md-default"
            size="mini"
            @click="handleExport"
            :disabled="loading"
          >
            <em v-show="loading" class="el-icon-loading"></em>
            <md-icon v-show="!loading">cloud_download</md-icon>
            导出
          </md-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import MarketTable from '@/pages/pms/workbench/chart/marketTable.vue'
export default {
  components:{
    MarketTable
  },
  props: {
    title: String,
    value: {
      type: Boolean,
      default: true
    },
    tableData: Object,
    options: Array,
    type: [String, Number],
    tableLoading: Boolean
  },
  data() {
    return {
      show: true,
      loading: false,
      optionValue: ''
    }
  },
  watch: {
    value: {
      handler(v) {
        this.show = v
      },
      immediate: true
    },
    type: {
      handler(v) {
        if (!v) return
        this.optionValue = ''
      },
      immediate: true
    }
  },
  methods: {
    close() {
      this.$emit('input', false)
    },
    handleExport() {
      this.loading = true
      this.$emit('handleExport', ()=>{
        this.loading = false
      })
    },
    change() {
      this.$emit('change', this.optionValue)
    }
  }
}
</script>
<style lang="scss">
.cockpit-el-dialog {
  background: rgba(26,59,106,0.96);
  max-height: 70%;
  min-height: 50%;
  display: flex;
  flex-direction: column;
  .el-dialog__title {
    color: #fff;
    font-size: 14px;
    font-weight: bold;
  }
  .el-dialog__header {
    .title-box {
      display: flex;
      align-items: center;
      .title {
        color: #fff;
        font-size: 14px;
        font-weight: bold;
      }
      .el-select{
        margin-left: 10px;
        background: transparent;
        color: #fff;
        .el-input__inner {
          background: transparent;
          color: #fff !important;
        }
        .el-input.is-focus .el-input__inner {
          border-color: #4869ea;
        }
      }
    }
  }
  .el-dialog__body {
    flex: 1;
    display: flex;
    overflow: hidden;
    padding-bottom: 20px;
  }
  .cockpit-dialog-container {
    display: flex;
    flex-direction: column;
    flex: 1;
    overflow: hidden;
    .cockpit-dialog-content {
      flex: 1;
      overflow: hidden;
      .loading-box {
        width: 100%;
        height: 100%;
      }
    }
    .cockpit-dialog-btn {
      display: flex;
      justify-content: flex-end;
      margin-top: 20px;
      .md-default {
        background: rgba(72,105,234,0.8) !important;
        box-shadow: 0 2px 2px 0 rgba(72,105,234, 0.14),
        0 3px 1px -2px rgba(72,105,234, 0.2),
        0 1px 5px 0 rgba(72,105,234, 0.12);
      }
    }
  }
}
</style>
<style lang="scss">
.type-select {
  background: linear-gradient(to bottom, rgba(72,105,234,0.96), rgba(72,105,234,0.9));
  border-color: #4869ea;
  .popper__arrow {
    border-bottom-color: #4869ea !important;
    &::after {
      border-bottom-color: #4869ea !important;
    }
  }
  .el-select-dropdown__item {
    color: #fff;
    font-size: 12px;
  }
  .el-select-dropdown__item.hover {
    background: rgba(31,27,73,0.5);
  }
  .el-select-dropdown__item.selected {
    color: #fff;
  }
}
</style>
