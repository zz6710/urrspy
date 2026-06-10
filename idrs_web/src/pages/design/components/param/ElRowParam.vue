<template>
  <div class="widget-param"
    v-if="element && element.key"
    :key="element.key"
  >
    <el-form ref="form" :model="element" label-position="top" label-width="80px">
      <el-form-item label="栅格间隔">
        <el-input type="number" v-model.number="element.options.gutter"></el-input>
      </el-form-item>
      <el-form-item label="列配置项" label-width="auto">
      </el-form-item>
      <el-form-item label="" label-width="auto">
        <div>
          <draggable tag="ul" :list="element.columns" 
            v-bind="{group:{ name:'options'}, ghostClass: 'ghost',handle: '.drag-item'}"
            handle=".drag-item"
          >
            <li v-for="(item, index) in element.columns" :key="index" >
              <i class="drag-item" style="font-size: 16px;margin: 0 5px;cursor: move;"><i class="iconfont icon-icon_bars"></i></i>
              <el-input placeholder="栅格值" size="mini" style="width: 100px;" type="number" v-model.number="item.span"></el-input>
              <el-button @click="handleOptionsRemove(index)" circle plain type="danger" size="mini" icon="el-icon-minus" style="padding: 4px;margin-left: 5px;"></el-button>
            </li>
          </draggable>
          <div style="margin-left: 22px;">
            <el-button type="text" @click="handleAddColumn">添加列</el-button>
          </div>
        </div>
      </el-form-item>
      <el-form-item label="水平排列方式">
        <el-select v-model="element.options.justify">
          <el-option value="start" label="左对齐"></el-option>
          <el-option value="end" label="右对齐"></el-option>
          <el-option value="center" label="居中"></el-option>
          <el-option value="space-around" label="两侧间隔相等"></el-option>
          <el-option value="space-between" label="两端对齐"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="垂直排列方式">
        <el-select v-model="element.options.align">
          <el-option value="top" label="顶部对齐"></el-option>
          <el-option value="middle" label="居中对齐"></el-option>
          <el-option value="bottom" label="底部对齐"></el-option>
        </el-select>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import Draggable from 'vuedraggable'

export default {
  components: {
    Draggable,
  },
  props: ['element', 'selectWidget'],
  inject: ['kFormDesign'],
  data () {
    return {
    }
  },
  created (){
  },
  mounted () {
    // document.body.ondrop = function (event) {
    //   let isFirefox = navigator.userAgent.toLowerCase().indexOf('firefox') > -1
    //   if (isFirefox) {
    //     event.preventDefault()
    //     event.stopPropagation()
    //   }
    // }
  },
  methods: {
    handleOptionsRemove (index) {
      this.element.columns.splice(index, 1)
    },
    handleAddColumn () {
      this.element.columns.push({
        span: '',
        list: []
      })
    },
  },
  watch: {
  }
}
</script>

<style lang="scss" scoped>   //必须要加上“<style lang="scss" scoped>，否则引入失败（提示保存）
  @import '../../styles/cover.scss';
  @import '../../styles/index.scss';

</style>
