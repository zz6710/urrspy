<template>
  <div
    class="widget-view"
    :key="element.key"
    @click.stop="handleSelectComponent($event)"
    :class="[{active: kFormDesign.widgetFormSelect.key == element.key}, element.customClass]"
  >
    <slot></slot>
    <template v-if="kFormDesign.widgetFormSelect.key == element.key">
      <div class="widget-view-action">
        <i class="iconfont icon-icon_clone" @click.stop="kFormDesign.cloneComponentByKey(element.key)"></i>
        <i class="iconfont icon-trash" @click.stop="kFormDesign.deleteComponentByKey(element.key)"></i>
      </div>
      <div class="widget-view-drag">
        <i class="iconfont icon-drag drag-widget" v-if="!isAbsLayout"></i>
        <!-- absolute 中显示的拖拽按钮 -->
        <i class="iconfont icon-drag " v-else @mousedown="handleMouseDownOnShape($event, element)"></i>
      </div>
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

      // 拖入普通表单index记录
      let formIndex = -1;
      let tabIndex = this.$store.state.design.tabIndex
      let layoutIndex;
      let tagIndex;

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

          // 计算拖动后组件位置，不能超出最上和最左
          const curX = moveEvent.clientX
          const curY = moveEvent.clientY
          let moveTop = curY - startY + startTop;
          let moveLeft = curX - startX + startLeft;
          // pos.top = this.isNearly(moveTop, 0) ? 0:moveTop

          let _component = this.kFormDesign.findComponentByKey(this.element.parentKey);
          let parentDom = this.kFormDesign.$("." + _component.customClass);
          let currDom = this.kFormDesign.$("." + this.element.customClass);
          // 校验左右
          let moveRight =  moveLeft + currDom.offsetWidth - parentDom.offsetWidth;
          if(moveRight > 0){
            pos.left = parentDom.offsetWidth - currDom.offsetWidth;
          } else {
            pos.left = this.isNearly(moveLeft, 0) ? 0:moveLeft
          }

          // 校验上下
          let moveBottom =  moveTop + currDom.offsetHeight - parentDom.offsetHeight;
          if(moveBottom > 0){
            pos.top = parentDom.offsetHeight - currDom.offsetHeight;
          } else {
            pos.top = this.isNearly(moveTop, 0) ? 0:moveTop
          }


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

          // 判断移动坐标是否在表单布局内
          if(this.element.type == 'k-form') return
          try {   // 根据异常打断循环

            this.kFormDesign.pageList[tabIndex].list.forEach((item, index) => {
              // 只实现一层的查找，如果多层，嵌套absolute-layout，则需要改为递归查找
              if(item.type == 'absolute-layout') {
                layoutIndex = index
                item.list.forEach((itemList, indexList) => {
                  if(itemList.type == 'k-form') {
                    // console.log('k-form', itemList);
                    let dom = this.kFormDesign.$("." + itemList.customClass);
                    // console.log(dom, dom.clientHeight, dom.clientWidth );
                    const maxLeft = dom.clientWidth;
                    const maxTop = dom.clientHeight;

                    // moveLeft
                    // moveTop

                    // form坐标+form结束点坐标 > 拖拽坐标 > form坐标  ==> 判断在form表单中，进行添加
                    if((moveLeft > itemList.style.left) &&  (moveLeft < (maxLeft + itemList.style.left)) &&
                    (moveTop > itemList.style.top) &&  (moveTop < (maxTop + itemList.style.top))) {
                      formIndex = indexList
                      // console.log('in...');
                      throw new Error('over')
                    } else {
                      formIndex = -1
                    }
                  }
                  if(itemList.key == this.element.key) {
                    tagIndex = indexList
                  }
                })
              }
            });
          } catch (msg) {
            console.log(msg.message);
          }
      }

      const up = () => {
          // hasMove && this.$store.commit('recordSnapshot')
          // 触发元素停止移动事件，用于隐藏标线
          eventBus.$emit('unmove')
          document.removeEventListener('mousemove', move)
          document.removeEventListener('mouseup', up)

          if(formIndex !== -1) {
            // 新增
            let obj = JSON.parse(JSON.stringify(this.element))
            obj.inForm = true
            obj.formItem = {
              label: this.element.name,
              customAttrs: [],
            };
            this.kFormDesign.pageList[tabIndex].list[layoutIndex] &&
            this.kFormDesign.pageList[tabIndex].list[layoutIndex].list[formIndex].list.push(obj)

            // 删除
            this.kFormDesign.pageList[tabIndex].list[layoutIndex] &&
            this.kFormDesign.pageList[tabIndex].list[layoutIndex].list.splice(tagIndex, 1)

          }


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

</style>
