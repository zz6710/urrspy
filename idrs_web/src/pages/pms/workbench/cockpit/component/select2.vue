<template>

  <el-popover placement="bottom-start" trigger="click" popper-class="node-popover" >
    <div class="node-popover-container">
      <div class="no-data" v-if="options.length <= 0">
        {{processLoading ? '加载中' : '暂无数据'}}
      </div>
      <template v-else>
        <div class="node-popover-item" v-for="item in options" :key="item.value">
          <div class="item active"
            :style="{background: bgColor, border: `1px solid ${bgColor}`, color: '#392727'}"
            @click="select(item.value)">
            {{item.label}}
          </div>
          <div class="item no-active" :style="{border: `1px solid ${bgColor}`, color: bgColor}">
            {{item.label}}
          </div>
        </div>
      </template>

    </div>
    <div slot="reference" class="text delete" style="color: #b8c3d0;font-size: 6px;font-weight: bold;" >
      产品代码<i class="el-icon-caret-bottom" />
    </div>
  </el-popover>

</template>

<script>
export default {
  props:{
    processLoading: Boolean,
    options: {
      type: Array,
      default: ()=>{
        return {}
      }
    },
    currentVal: {
      type: [String, Number],
      default: ''
    }
  },
  computed: {
    currentLabel() {
      if(this.options.length > 0)
        return (this.options.filter(item=>item.value == this.currentValue)[0] || {}).label
    }
  },
  data() {
    return {
      show: false,
      currentValue: 1,
      bgColor: '#00BCD4',
    }
  },
  mounted() {
    // const item = this.options.filter(item=>item.value == this.currentVal)[0]
    this.select(this.currentVal)
  },
  methods: {
    select(val) {
      // this.currentValue = item.value
      this.$emit('select', val)
      this.show = false
    }
  }
}
</script>

<style lang="scss">
.node-popover {
  background: linear-gradient(to bottom, rgba(31,27,73,0.9), rgba(20,32,63,0.9));
  .el-popover__title{
    color: #dfdee7;
    font-size: 14px;
    font-weight: bold;
  }
  .node-popover-container {
    max-height: 150px;
    overflow: auto;
    .no-data {
      font-size: 12px;
      text-align: center;
    }
  }
  .node-popover-item {
    font-size: 8px;
    font-weight: bold;
    line-height: 15px;
    margin: 5px 0;
    cursor: pointer;
    text-align: center;
    .active {
      padding: 0 5px;
      border-radius: 4px;
      display: none;
    }
    .no-active {
      padding: 0 5px;
      border-radius: 4px;
    }

    &:hover {
      .no-active {
        display: none;
      }
      .active {
        display: block;
      }
    }

  }
}
</style>
