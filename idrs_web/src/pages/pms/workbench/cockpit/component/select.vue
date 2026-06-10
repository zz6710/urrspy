<template>
  <div class="cockpit-select">
    <el-popover
      placement="right"
      title=""
      trigger="click"
      popper-class="cockpit-popover"
      :visible-arrow="false"
      v-model="show"
    >
      <div class="cockpit-select-list">
        <div
          class="cockpit-select-item"
          :class="item.value == currentValue ? 'active' : ''"
          v-for="item in options"
          :key="item.value"
          @click="select(item)">
          <span>{{item.label}}</span>
        </div>
      </div>
      <div class="btn" slot="reference">{{currentLabel}}
        <i class="icon el-icon-caret-top" :class="show ? 'is-reverse' : ''"></i>
      </div>
    </el-popover>
  </div>
</template>
<script>
export default {
  props:{
    options: {
      type: Array,
      default: ()=>{
        return {}
      }
    },
    currentVal: {
      type: [String, Number],
      default: '1'
    }
  },
  computed: {
    currentLabel() {
      return (this.options.filter(item=>item.value == this.currentValue)[0] || {}).label
    }
  },
  data() {
    return {
      show: false,
      currentValue: 1
    }
  },
  mounted() {
    const item = this.options.filter(item=>item.value == this.currentVal)[0]
    this.select(item)
  },
  methods: {
    select(item) {
      this.currentValue = item.value
      this.$emit('select', item)
      this.show = false
    }
  }
}
</script>
<style lang="scss" scoped>
.cockpit-select {
  .btn {
    color: #fff;
    font-size: 12px;
    display: inline-block;
    font-weight: bold;
    cursor: pointer;
    opacity: 0.6;
    .icon {
      transform: rotate(180deg);
      transition: transform 0.3s;
      &.is-reverse {
        transform: rotate(0deg);
      }
    }
  }
}
</style>
<style lang="scss">
.cockpit-popover {
  background: linear-gradient(to bottom, rgba(31,27,73,0.9), rgba(20,32,63,0.9));
  color: #fff;
  border-color: #4869ea;
  padding: 6px;
  min-width: auto;
  .cockpit-select-list {
    .cockpit-select-item {
      color: #fff;
      font-size: 12px;
      font-weight: bold;
      cursor: pointer;
      line-height: 24px;
      padding: 0 5px;
      user-select: none;
      &.active {
        background: rgba(72,105,234,0.8);
      }
      &:hover {
        background: rgba(72,105,234,0.4);
      }
      span {
        display: inline-block;
        transform: scale(0.9);
      }
    }
  }
}
</style>
