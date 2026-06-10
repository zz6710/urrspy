/**
 * 组件动态参数说明：
 *  组件需要提供parent字段，指定表格的className（字符串）,由k-grid动态生成
 *  依赖elementUI 中 el-table组件中的el-table__header-wrapper 的class名称，若element版本更新修改了类名，需同步修改
 */
const scrollContentClass = 'k-work-content'   // 滚动区域className
const topToContent = 141
const topToScroll = 111   // 滚动区域距离顶部高度，单位px
let locked = false;   // 滚动动画锁定参数

export default {
  created() {
    if (this.dataSticky) {
      this.parent = 'k-grid-' + new Date().getTime()
    }
  },
  mounted() {
    if (this.dataSticky) {
      this.$nextTick(() => {
        setTimeout(() => {
          this.containerDom = document.getElementsByClassName(scrollContentClass)
          this.clearListener()
          window.addEventListener('resize', this.resizeChange)
        }, 100)
      })
      // 取消初始化加载
      // let timer = setTimeout(() => {
      //   this.initFixedHeader()
      //   clearTimeout(timer)
      // }, 300)
    }
  },
  deactivated() {
    // 修复切换bug
    if (!this.containerDom) {
      return;
    }
    if (this.containerDom[0]) {
      this.containerDom[0].scrollTop = 0
    } else {
      let containerDom = document.getElementsByClassName(scrollContentClass)
      containerDom[0].scrollTop = 0
    }
    if (this.dataSticky) this.clearListener()
  },
  beforeDestroy() {
    if (this.dataSticky) {
      this.clearListener()
      //取消监听窗口大小
      window.removeEventListener('resize', this.resizeChange)
    }
  },
  activated() {
    this.$nextTick(() => {
      if (this.dataSticky) {
        this.initFixedHeader()
        this.updateFixedRight()
        window.addEventListener('resize', this.resizeChange)
        let timer
        timer = setTimeout(() => {
          let container = this.containerDom
          if (container[0].scrollTop > 0) {
            container[0].scrollTop = container[0].scrollTop + 1
          }
          clearTimeout(timer)
        }, 1000)
      }
    })
  },
  methods: {
    activatedReload() {
      window.addEventListener('resize', this.resizeChange)
      let timer = setTimeout(() => {
        this.clearFixedStyle()
        this.initFixedHeader()
      }, 300)
      this.timerList.push(timer)
    },
    reset() {
      this.clearFixedStyle()
    },
    // 窗口大小变化时，初始化
    resizeChange() {
      this.headerDragend()
      let timer = setTimeout(() => {
        this.initFixedHeader()
        clearTimeout(timer)
      }, 300)
    },
    initFixedHeader() {
      if (this.parent) {
        this.parentDom = document.getElementsByClassName(this.parent)
        if (this.parentDom && this.parentDom.length !== 0) {
          this.tableWidth = this.parentDom[0].querySelector('.el-table__header-wrapper').getBoundingClientRect().width
          this.setScrollXWidth()
          this.tableDom = this.parentDom[0].getElementsByClassName('el-table__header-wrapper')
          this.scrollDom = document.querySelector('.' + scrollContentClass)
          setTimeout(() => {
            this.scrollDom.addEventListener('scroll', this.scrollEvent)
          }, 200)
        }
      }
    },
    // 清空监听事件
    clearListener() {
      if (this.scrollDom) {
        this.scrollDom.removeEventListener('scroll', this.scrollEvent)
        window.removeEventListener('resize', this.resizeChange)
        this.clearFixedStyle()
        this.timerList.forEach(key => {
          clearTimeout(key)
        });
      }
    },
    // 更新右侧固定栏
    updateFixedRight() {
      let { fixedRightHeaderDom, dom } = this.getFixedDom()
      if (dom.classList.contains('fixed')) {
        let timer = setTimeout(() => {
          this.setFixedStyle({
            dom: fixedRightHeaderDom,
            left: this.fixedRightDom[0].getBoundingClientRect().left + 'px',
            width: getComputedStyle(this.fixedRightDom[0]).width,
            scrollLeft: fixedRightHeaderDom.scrollWidth
          })
          clearTimeout(timer)
        }, 100)
      }
    },
    async headerDragend() {
      await this.updateWidth()
      await this.updateFixedRight()
      this.setScrollXWidth()
      // await this.updateHeaderHeight()
    },
    setScrollXWidth() {
      let timer = setTimeout(() => {
        if (!this.parentDom) this.parentDom = document.getElementsByClassName(this.parent)
        if (this.parentDom.length == 0) return
        let dom = this.parentDom[0].querySelector('.el-table__header')
        this.tableWidth = this.parentDom[0].querySelector('.el-table__body-wrapper').getBoundingClientRect().width
        this.tableDom[0].style.width = this.tableWidth + 'px'
        this.updateHeaderHeight()
        this.headerWidth = dom.style.width
        clearTimeout(timer)
      }, 200)
    },
    // 更新表格宽度，（拖拽改变宽度时使用）
    updateWidth() {
      if (!this.parentDom) this.parentDom = document.getElementsByClassName(this.parent)
      const bodyWrapperDom = this.parentDom[0].getElementsByClassName('el-table__body-wrapper')[0]
      const width = getComputedStyle(bodyWrapperDom).width//表格宽度
      // 给表格设置宽度。
      const tableParent = this.tableDom
      for (let i = 0; i < tableParent.length; i++) {
        tableParent[i].style.width = width
      }
    },
    getFixedDom() {
      let fixedRightHeaderDom, fixedRightBox, fixedLeftHeaderDom, fixedLeftBox;
      let dom = this.tableDom[0]
      if (this.fixedLeftDom && this.fixedLeftDom[0]) {
        let lefarr = this.fixedLeftDom[0].children
        fixedLeftHeaderDom = lefarr[0]
        fixedLeftBox = lefarr[1]
      }
      if (this.fixedRightDom && this.fixedRightDom[0]) {
        let rightarr = this.fixedRightDom[0].children
        fixedRightHeaderDom = rightarr[0]
        fixedRightBox = rightarr[1]
      }
      return { fixedRightHeaderDom, fixedRightBox, fixedLeftHeaderDom, fixedLeftBox, dom }
    },
    // 更新表头高度，表头高度有可能改变
    updateHeaderHeight() {
      this.$nextTick(() => {
        this.tableDom = this.parentDom[0].getElementsByClassName('el-table__header-wrapper')
        let obj = this.tableDom[0].getBoundingClientRect()
        if (obj.height != this.tablexy.height) {
          this.tablexy.height = obj.height
          let { dom } = this.getFixedDom()
          if (dom.classList.contains('fixed')) {
            let timer = setTimeout(() => {
              this.parentDom[0].getElementsByClassName('el-table__fixed-body-wrapper')[0].style.top = 0
              let container = this.containerDom
              if (container && container[0]) {
                container[0].scrollTop = container[0].scrollTop + 3;
              }
              clearTimeout(timer)
            }, 100)
          }
        }
      })
    },
    // 获取表格属性
    getTableXy() {
      this.tablexy = JSON.parse(JSON.stringify(this.tableDom[0].getBoundingClientRect()))
      let top = this.containerDom[0].scrollTop;   // 滚动条当前高度
      this.tablexy.top = top + this.tablexy.top;  // 计算得出距离顶部的真实高度
      this.tablexy.height = this.tableDom[0].offsetHeight
      return this.tablexy
    },
    getDom() {
      if (!this.parentDom) {
        this.parentDom = document.getElementsByClassName(this.parent)
      }
    },
    //滚动事件
    async scrollEvent(e) {
      locked = false;
      if (locked) return;
      locked = true;
      window.requestAnimationFrame(async (_) => {
        this.getDom()
        this.tableDom = this.parentDom[0].getElementsByClassName('el-table__header-wrapper')
        if (!this.isSticky) {
          await this.getTableXy()
        }
        this.fixedRightDom = this.parentDom[0].getElementsByClassName('el-table__fixed-right')
        this.fixedLeftDom = this.parentDom[0].getElementsByClassName('el-table__fixed')
        let { height, top, left } = this.tablexy
        let scrollTop = e.target.scrollTop
        let { fixedRightHeaderDom, fixedRightBox, fixedLeftHeaderDom, fixedLeftBox, dom } = this.getFixedDom()
        let tempCalc = height / 2 + top - topToContent
        let calcHeight = tempCalc > 0 ? tempCalc : 0
        let tabH = this.parentDom[0].offsetHeight   // 表格高度
        if (scrollTop >= calcHeight && (tabH + top - topToContent - height) > scrollTop) {
          // 存在右侧固定表头
          if (fixedRightHeaderDom) {
            this.setFixedStyle({
              dom: fixedRightHeaderDom,
              left: this.fixedRightDom[0].getBoundingClientRect().left + 'px',
              width: getComputedStyle(this.fixedRightDom[0]).width,
              scrollLeft: fixedRightHeaderDom.scrollWidth
            })
            fixedRightBox.style.top = 0
          }
          // 左侧固定
          if (fixedLeftHeaderDom) {
            this.setFixedStyle({
              dom: fixedLeftHeaderDom,
              left: left + 'px',
              width: getComputedStyle(this.fixedLeftDom[0]).width,
              scrollLeft: 0
            })
            fixedLeftBox.style.top = 0
          }
          dom.classList.add('fixed')//加一个固定标识
          this.updateWidth()
          dom.style.position = 'fixed'
          dom.style.zIndex = '2000'
          dom.style.top = topToScroll + 'px'
          dom.style.overflow = 'hidden'
          this.isSticky = true
        } else {
          this.isSticky = false
          this.clearFixedStyle()
        }
        locked = false;
      });
    },
    //设置固定
    setFixedStyle(data) {
      let { dom, scrollLeft, width, left } = data
      dom.style.zIndex = '2000'
      dom.style.position = 'fixed'
      dom.style.top = topToScroll + 'px'
      dom.scrollLeft = scrollLeft
      dom.style.width = width
      dom.style.overflow = 'hidden'
      dom.style.left = left
    },
    // 清除header固定
    clearFixedStyle() {
      if (!this.tableDom) return
      let { height, left } = this.tablexy
      let { dom, fixedRightHeaderDom, fixedRightBox, fixedLeftHeaderDom, fixedLeftBox } = this.getFixedDom()
      if (dom.classList.contains('fixed')) {
        if (fixedRightHeaderDom) {
          fixedRightBox.style.top = height + 'px'
          fixedRightHeaderDom.removeAttribute("style");
        }
        if (fixedLeftHeaderDom) {
          fixedLeftHeaderDom.style.zIndex = '0'
          fixedLeftHeaderDom.style.position = 'static'
          fixedLeftHeaderDom.style.top = 0 + 'px'
          fixedLeftHeaderDom.style.left = left + 'px'
          fixedLeftBox.style.top = getComputedStyle(dom).height
        }
        dom.classList.remove('fixed')
        dom.style.position = 'static'
        dom.style.top = '0'
        dom.style.zIndex = '0'
      }
    },
  },
  computed: {
    dataStickyClass() {
      if (this.dataSticky) {
        return this.parent
      }
    }
  },
  data() {
    return {
      tablexy: {},//表格的左边宽度信息
      fixedRightDom: null,//右侧
      fixedLeftDom: null,//左侧栏固定
      scrollDom: null,//滚动的dom
      parentDom: null,//表格的父元素dom
      tableWidth: 0,
      timerList: [],
      tableDom: null,
      containerDom: null,
      isSticky: false,
    }
  },
}
