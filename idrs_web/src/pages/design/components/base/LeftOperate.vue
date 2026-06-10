<template>
  <div class="leftWarp">
    <div class="leftWarp_btn" icon="el-icon-arrow-right" circle @click="setWidth">
      <i v-if="!isExtend" class="el-icon-arrow-right"></i>
      <i v-else class="el-icon-arrow-left"></i>
    </div>
  <el-aside class="left-operate" :width="asideWidth">
    <el-collapse  v-if="isExtend" v-model="activeNames">
      <el-collapse-item title="基础组件" name="1">
        <template slot="title">
          <span class="title">基础组件</span>
        </template>
        <draggable tag="ul" :list="tmpBasicComponents"
          v-bind="{group:{ name:'people', pull:'clone',put:false},sort:false}"
          @end="handleMoveEnd"
          @start="handleMoveStart"
          :clone="clone"
          :move="handleMove"
        >
          <li class="form-edit-widget-label" :class="{'no-put': item.type == 'divider'}" v-for="(item, index) in tmpBasicComponents" :key="index" :data-type="item.type">
            <a>
              <i class="icon iconfont" :class="item.icon"></i>
              <span>{{item.name}}</span>
            </a>
          </li>
        </draggable>
      </el-collapse-item>
      <el-collapse-item title="高级组件" name="2">
        <template slot="title">
          <span class="title">高级组件</span>
        </template>
        <div v-if="!tmpAdvanceComponents || tmpAdvanceComponents.length==0" class="empty-hint">
          <el-empty description="组件待补充..."></el-empty>
        </div>
        <draggable v-else tag="ul" :list="tmpAdvanceComponents"
          v-bind="{group:{ name:'people', pull:'clone',put:false},sort:false}"
          @end="handleMoveEnd"
          @start="handleMoveStart"
          :clone="clone"
          :move="handleMove"
        >
          <li class="form-edit-widget-label" :class="{'no-put': item.type == 'table'}" v-for="(item, index) in tmpAdvanceComponents" :key="index" :data-type="item.type">
            <a>
              <i class="icon iconfont" :class="item.icon"></i>
              <span>{{item.name}}</span>
            </a>
          </li>
        </draggable>
      </el-collapse-item>
      <el-collapse-item title="布局组件" name="3">
        <template slot="title">
          <span class="title">布局组件</span>
        </template>
        <draggable tag="ul" :list="tmpLayoutComponents"
          v-bind="{group:{ name:'people', pull:'clone',put:false},sort:false}"
          @end="handleMoveEnd"
          @start="handleMoveStart"
          :clone="clone"
          :move="handleMove"
        >
          <li class="form-edit-widget-label no-put" v-for="(item, index) in tmpLayoutComponents" :key="index" :data-type="item.type" draggable>
            <a>
              <i class="icon iconfont" :class="item.icon"></i>
              <span>{{item.name}}</span>
            </a>
          </li>
        </draggable>
      </el-collapse-item>
      <el-collapse-item title="图表组件" name="4">
        <template slot="title">
          <span class="title">图表组件</span>
        </template>
        <div v-if="!tmpChartComponents || tmpChartComponents.length==0" class="empty-hint">
          <el-empty description="组件待补充..."></el-empty>
        </div>
        <draggable v-else tag="ul" :list="tmpChartComponents"
          v-bind="{group:{ name:'people', pull:'clone',put:false},sort:false}"
          @end="handleMoveEnd"
          @start="handleMoveStart"
          !clone="clone"
          :move="handleMove"
        >
          <li class="form-edit-widget-label" :class="{'no-put': item.type == 'table'}" v-for="(item, index) in tmpChartComponents" :key="index" :data-type="item.type">
            <a>
              <i class="icon iconfont" :class="item.icon"></i>
              <span>{{item.name}}</span>
            </a>
          </li>
        </draggable>
      </el-collapse-item>
    </el-collapse>
    <div class="collapseMini" v-else>
      <el-tooltip effect="dark" content="基础组件" placement="right">
        <i class="el-icon-s-home collapseMini_icon" @click="setWidth('1')"></i>
      </el-tooltip>
      <el-tooltip effect="dark" content="高级组件" placement="right">
        <i class="el-icon-s-claim collapseMini_icon" @click="setWidth('2')"></i>
      </el-tooltip>
      <el-tooltip effect="dark" content="布局组件" placement="right">
        <i class="el-icon-menu collapseMini_icon" @click="setWidth('3')"></i>
      </el-tooltip>
      <el-tooltip effect="dark" content="图表组件" placement="right">
        <i class="el-icon-s-data collapseMini_icon" @click="setWidth('4')"></i>
      </el-tooltip>
    </div>
  </el-aside>
  </div>
</template>

<script>
import Draggable from 'vuedraggable'
import {basicComponents, layoutComponents, advanceComponents, chartComponents} from '../componentsConfig.js'

export default {
  components: {
    Draggable
  },
  props: {

  },
  inject: ['kFormDesign'],
  data () {
    return {
      basicComponents,
      layoutComponents: JSON.parse(JSON.stringify(layoutComponents)),
      advanceComponents,
      chartComponents,
      activeNames: ['1'],
      tmpBasicComponents:[],
      tmpAdvanceComponents:[],
      tmpChartComponents:[],
      tmpLayoutComponents:[],
      isExtend: true,
      asideWidth: '180px',
    }
  },
  created() {
  },
  mounted () {
    this.tmpBasicComponents = basicComponents.filter(item=>item.show!==false);
    this.tmpAdvanceComponents = advanceComponents.filter(item=>item.show!==false);
    this.tmpChartComponents = chartComponents.filter(item=>item.show!==false);
    this.tmpLayoutComponents = layoutComponents.filter(item=>item.show!==false);
  },
  methods: {
    setWidth(flag) {
      if(this.isExtend) {
        this.asideWidth = '50px'
        this.isExtend = false
      } else {
        this.asideWidth = '180px'
        this.isExtend = true
        if(flag) {
          this.activeNames = []
          this.activeNames.push(flag)
        } else {
          this.activeNames = ['1']
        }
      }


    },
    handleMoveEnd: function(e){
    },
    handleMoveStart: function(e){
      this.kFormDesign.draggableType = e.clone.dataset.type;
    },
    handleMove: function(e){
    },
    clone: function(origin){
      // console.log(" origin ", origin, aaa, bbb, ccc);
      // clone 从一个数组拖拽到另外一个数组时触发的事件和add不同，clone是复制了数组元素
      // 所以我们可以重新new 拷贝一个新对象
      // let data = JSON.parse(JSON.stringify(origin))
      // data.id = newGuid()
      // console.log(data,'data')
      // return data;
      return origin;
    },
  },
  watch: {

  }
}
</script>

<style lang="scss" scoped>
@import '../../styles/variable.scss';
.leftWarp {
  position: relative;
  &_btn {
    position: absolute;
    top: 50%;
    right: 0;
    background-color: #F4F4F5;
    color: #000;
    border-radius: 4px 0 0 4px;
    border-right: 0;
    padding: 5px 1px;
    cursor: pointer;
    opacity: .7;
    transition: .3s all;
    transform: translateY(-50%);
    z-index: 2;
    .el-icon-arrow-right, .el-icon-arrow-left {
      font-weight: 600;
      font-size: 15px;
    }
    &:hover {
      opacity: .9;
    }
  }
}
.collapseMini {
  &_icon {
    font-size: 24px;
    padding: 8px;
    margin: 5px;
    color: $dark-color;
    cursor: pointer;
    transition: .2s all;
    &:hover {
      background: #606266;
      border-radius: 4px;
    }
  }
}
.left-operate {
  position: relative;
  height: calc(100vh - #{$top-height});
  background: $dark-bg;
  transition: .1s width;
  &::-webkit-scrollbar {
    display: none; /* Chrome Safari */
  }

  .el-collapse {
    border-color: $dark-border;
  }

  /deep/ .el-collapse-item__header {
    background-color: $dark-bg;
    border-bottom-color: $dark-border;
    color: $dark-color;
  }
  /deep/ .el-collapse-item__wrap {
    background-color: $dark-bg;
    border-bottom-color: $dark-border;
    padding: 10px 0;
  }
  /deep/ .el-collapse-item__content  {
    padding-bottom: 0px;
  }
  /deep/ .el-empty__image {
    width: 50px;
  }
  .title {
    padding: 8px 12px;
    font-size: 13px;
    color: $dark-color;
  }

  ul{
    position: relative;
    // overflow: hidden;
    padding: 0 10px 10px;
    margin: 0;
  }

  .form-edit-widget-label{
    font-size: 12px;
    display: block;
    width: 98%;
    line-height: 26px;
    position: relative;
    float: left;
    left: 0;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    margin: 3px 0;
    color: #333;
    border: 1px dashed rgba(255,255,255,.2);
    transition: .3s all;

    &:hover{
      border: 1px dashed #FFF;
      // box-shadow: 0 2px 5px 0 rgba(255,255,255,.3);
    }

    &>a{
      display: block;
      cursor: move;
      background-color: $dark-bg;
      color: $dark-color;
      &:hover {
        background-color: #2A292F;
        color: $dark-color !important;
      }

      .icon{
        margin-right: 6px;
        margin-left: 8px;
        font-size: 14px;
        display: inline-block;
        vertical-align: middle;
      }

      span{
        display: inline-block;
        vertical-align: middle;
      }
    }
  }
}

// 拖拽样式
.ghost{
  background: #F56C6C;
  border: 2px solid #F56C6C;
  outline-width: 0;
  height: 3px;
  box-sizing: border-box;
  font-size: 0;
  content: '';
  overflow: hidden;
  padding: 0;
}

.ghost-abs {
  background: $primary-color;
  border: 0px solid $primary-color;
  outline-width: 0;
  height: 0px;
  box-sizing: border-box;
  font-size: 0;
  content: '';
  overflow: hidden;
  padding: 0;
}
</style>
