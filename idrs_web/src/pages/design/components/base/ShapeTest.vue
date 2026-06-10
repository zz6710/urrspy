<template>
  <div class="widget-view shape" >
    <template>
      <draggable 
        class="" 
        style="height:100%"
        v-bind="{group:{ name:'people', pull:true, put: ()=>{return true}}, ghostClass: 'ghost',animation: 200, handle: '.drag-widget'}"
      >
        <div class="widget-view-action">
          <i class="iconfont icon-icon_clone" @click.stop="kFormDesign.cloneComponentByKey(element.key)"></i>
          <i class="iconfont icon-trash" @click.stop="kFormDesign.deleteComponentByKey(element.key)"></i>
        </div>
        <div class="widget-view-drag">
          <i class="iconfont icon-drag drag-widget"></i>
          <!-- <i class="iconfont icon-drag " v-if="isAbsLayout" @mousedown="handleMouseDownOnShape($event, element)"></i> -->
        </div>
      </draggable>
    </template>
  </div>
</template>

<script>
import eventBus from '@/utils/eventBus'

export default {
  props: {
    element: {
      type: Object
    },
    isAbsLayout: {
      type: Boolean,
      default: false
    }
  },
  inject: ['kFormDesign'],

  data () {
    return {
      diff: 3 // 吸附区间
    }
  },
  created (){
  },
  mounted () {
  },
  methods: {
    handleSelectComponent: function($event){
      console.log(" $event ", $event);
      $event.preventDefault()
      $event.stopPropagation()
      this.kFormDesign.selectComponentByKey(this.element.key);
    },
    handleMouseDownOnShape(e, item) {
      e.preventDefault()
      e.stopPropagation()

      const pos = item.style
      const startY = e.clientY
      const startX = e.clientX
      // 如果直接修改属性，值的类型会变为字符串，所以要转为数值型
      const startTop = Number(pos.top)
      const startLeft = Number(pos.left)

      // 如果元素没有移动，则不保存快照
      // let hasMove = false
      const move = (moveEvent) => {
          if(item.key != this.kFormDesign.widgetFormSelect.key){
              console.log(" 未选中，不能拖拽 ", item.key);
              return false;
          }

          // hasMove = true

          // 计算拖动后组件位置，不能超出最上和最左
          const curX = moveEvent.clientX
          const curY = moveEvent.clientY
          let moveTop = curY - startY + startTop;
          let moveLeft = curX - startX + startLeft;
          pos.top = this.isNearly(moveTop, 0) ? 0:moveTop
          pos.left = this.isNearly(moveLeft, 0) ? 0:moveLeft

          // 等更新完当前组件的样式并绘制到屏幕后再判断是否需要吸附
          // 如果不使用 $nextTick，吸附后将无法移动
          this.$nextTick(() => {
              // 触发元素移动事件，用于显示标线、吸附功能
              // 后面两个参数代表鼠标移动方向
              // curY - startY > 0 true 表示向下移动 false 表示向上移动
              // curX - startX > 0 true 表示向右移动 false 表示向左移动
              // eventBus.$emit('move', curY - startY > 0, curX - startX > 0)
              eventBus.$emit('move', this.element.parentKey)
          })
      }

      const up = () => {
          // hasMove && this.$store.commit('recordSnapshot')
          // 触发元素停止移动事件，用于隐藏标线
          eventBus.$emit('unmove')
          document.removeEventListener('mousemove', move)
          document.removeEventListener('mouseup', up)
      }

      document.addEventListener('mousemove', move)
      document.addEventListener('mouseup', up)
    },

    isNearly(dragValue, targetValue) {
        return dragValue < 0 || Math.abs(dragValue - targetValue) <= this.diff
    },

    getStyle: function(style) {
      if(!style){
        return {};
      }
      // console.log(" style ", style);
      const result = {};
      Object.keys(style).forEach(function (prop) {
        var unit = "";
        // add unit if the value is numeric and is one of the following
        // 为如下的属性增加单位
        if (
          ["width", "height", "top", "right", "bottom", "left"].indexOf(prop) !== -1 
        ) {
          unit = "px";
        }
        result[prop] = style[prop] + unit;
      });
      return result
    }
  },
  watch: {
  },
}
</script>

<style lang="scss" scoped>
  @import '../../styles/cover.scss';
  @import '../../styles/index.scss';

  // .shape {
  //   position: absolute;
  //   top: 10px;
  //   left: 50px;
  // }

</style>
