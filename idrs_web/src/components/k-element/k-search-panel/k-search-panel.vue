<template>
  <div>
    <el-header height="25px">
      <label class="head-shadow">
        <i class="el-icon-share" />
        <span class="head-shadow-label">
          {{ dataTitles }}
        </span>
      </label>
    </el-header>
      <p>
        <el-input
          v-if="dataFilterable === true || dataFilterable === 'true'"
          :value="value"
          class="api-panel-filter"
          size="small"
          :placeholder="dataPlaceholder"
          :clearable="dataClearable === true || dataClearable === 'true'"
          @mouseenter.native="inputHover = true"
          @mouseleave.native="inputHover = false"
          @input="handleInputValue"
        >
        </el-input>
      </p>
      <k-field-tree
        ref="tree"
        :data-action="dataAction"
        :data-params="dataParams"
        :data-display-field="dataDisplayField"
        :data-value-field="dataValueField"
        :data-parent-field="dataParentField"
        :data-lazy="dataLazy === true || dataLazy === 'true'"
        :data-filter-node-method="filterNode"
        @data-on-node-click="handleNodeClick"
      />
    <el-footer v-if="hasFooter">
      <slot></slot>
    </el-footer>
  </div>
</template>

<script>
export default {
  name: "KSearchPanel",
  componentName: 'KSearchPanel',
  props: {
    dataTitles: {
      type: String,
      default: '列表'
    },
    dataParams: {
      type: [String,Object],
      default: undefined
    },
    dataAction: {
      type: String,
      default: undefined
    },
    value: {
      type: [String, Number],
      default: undefined
    },
    dataPlaceholder: {
      type: String,
      default: '请输入搜索内容'
    },
    dataLazy:{
      type: [Boolean, String],
      default: undefined
    },
    dataClearable:{
      type: [Boolean, String],
      default: undefined
    },
    dataFilterable: {
      type: [Boolean, String],
      default: true
    },
    dataDisplayField:{
      type:String,
      default: undefined
    },
    //值字段
    dataValueField: {
      type:String,
      default: undefined
    },
    //父节点
    dataParentField: {
      type:String,
      default: undefined
    }
  },
  data() {
    return {
      options: [],
      inputHover: false,
    }
  },
  computed: {
    hasFooter() {
      return !!this.$slots.default
    }
  },
  methods: {
    handleInputValue(value){
      this.$refs.tree.filterData(value)
      this.$emit('input', value);
    },
    inputIcon() {
      return this.value ? this.value.length > 0 && this.inputHover
        ? 'circle-close'
        : 'search' : ''
    },
    filterNode(value, data) {
      return this.$refs.tree.filterNode(value,data)
    },
    handleNodeClick(data, node, obj) {
      this.$emit('data-on-node-click', data, node, obj)
    }
  }
}
</script>

<style scoped>
  .head-shadow .head-shadow-label {
    font-size: 14px;
    color: #303133;
    font-weight: 400
  }
  .head-shadow .head-shadow-label span {
    position: absolute;
    right: 15px;
    color: #909399;
    font-size: 12px;
    font-weight: 400
  }
  .api-panel-filter {
    text-align: center;
    margin: 15px;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    display: block;
    width: auto
  }
</style>
