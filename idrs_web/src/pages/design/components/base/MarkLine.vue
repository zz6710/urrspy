<template>
    <div class="mark-line">
        <div
            v-for="line in lines"
            v-show="lineStatus[line] || false"
            :key="line"
            :ref="line"
            class="line"
            :class="line.includes('x')? 'xline' : 'yline'"
        ></div>
    </div>
</template>

<script>
import eventBus from '@/utils/eventBus'
import { $ } from '@/pages/design/utils/utils'

export default {
    props: ['element', 'selectWidget'],
    inject: ['kFormDesign', 'canvasElement'],
    data() {
        return {
            lines: ['xt', 'xc', 'xb', 'yl', 'yc', 'yr'], // 分别对应三条横线和三条竖线
            diff: 3, // 相距 dff 像素将自动吸附
            lineStatus: {
                xt: false,
                xc: false,
                xb: false,
                yl: false,
                yc: false,
                yr: false,
            },
        }
    },
    mounted() {
        // 监听元素移动和不移动的事件
        eventBus.$on('move', (parentKey) => {
            // 判断当前拖动元素所属画布是否一致
            if(this.element.key == parentKey){
                this.showLine()
            }
        })

        eventBus.$on('unmove', () => {
            this.hideLine()
        })
    },
    methods: {
        hideLine() {
            Object.keys(this.lineStatus).forEach(line => {
                this.lineStatus[line] = false
            })
        },

        showLine() {
            // 隐藏辅助线
            this.hideLine()

            // 获取所有辅助线对象
            const lines = this.$refs

            // 获取当前画布内节点数组
            const components = this.element.list;

            // 当前选中节点样式
            const curComponentStyle = this.getNodeStyle(this.kFormDesign.widgetFormSelect);

            // 遍历当前画布其他节点，判断是否需要显示辅助线
            components.forEach(component => {
                // 自身组件跳过
                if (component.key == this.kFormDesign.widgetFormSelect.key) return

                // 获取遍历组件样式
                const componentStyle = this.getNodeStyle(component);

                const conditions = {
                    top: [
                        {
                            isNearly: this.isNearly(curComponentStyle.top, componentStyle.top),
                            lineNode: lines.xt[0], // xt
                            line: 'xt',
                            dragShift: componentStyle.top,
                            lineShift: componentStyle.top,
                        },
                        {
                            isNearly: this.isNearly(curComponentStyle.bottom, componentStyle.top),
                            lineNode: lines.xt[0], // xt
                            line: 'xt',
                            dragShift: componentStyle.top - curComponentStyle.height,
                            lineShift: componentStyle.top,
                        },
                        {
                            // 组件与拖拽节点的中间是否对齐
                            isNearly: this.isNearly(curComponentStyle.top + curComponentStyle.halfHeight, componentStyle.top + componentStyle.halfHeight),
                            lineNode: lines.xc[0], // xc
                            line: 'xc',
                            dragShift: componentStyle.top + componentStyle.halfHeight - curComponentStyle.halfHeight,
                            lineShift: componentStyle.top + componentStyle.halfHeight,
                        },
                        {
                            isNearly: this.isNearly(curComponentStyle.top, componentStyle.bottom),
                            lineNode: lines.xb[0], // xb
                            line: 'xb',
                            dragShift: componentStyle.bottom,
                            lineShift: componentStyle.bottom,
                        },
                        {
                            isNearly: this.isNearly(curComponentStyle.bottom, componentStyle.bottom),
                            lineNode: lines.xb[0], // xb
                            line: 'xb',
                            dragShift: componentStyle.bottom - curComponentStyle.height,
                            lineShift: componentStyle.bottom,
                        },
                    ],
                    left: [
                        {
                            isNearly: this.isNearly(curComponentStyle.left, componentStyle.left),
                            lineNode: lines.yl[0], // yl
                            line: 'yl',
                            dragShift: componentStyle.left,
                            lineShift: componentStyle.left,
                        },
                        {
                            isNearly: this.isNearly(curComponentStyle.right, componentStyle.left),
                            lineNode: lines.yl[0], // yl
                            line: 'yl',
                            dragShift: componentStyle.left - curComponentStyle.width,
                            lineShift: componentStyle.left,
                        },
                        {
                            // 组件与拖拽节点的中间是否对齐
                            isNearly: this.isNearly(curComponentStyle.left + curComponentStyle.halfWidth, componentStyle.left + componentStyle.halfWidth),
                            lineNode: lines.yc[0], // yc
                            line: 'yc',
                            dragShift: componentStyle.left + componentStyle.halfWidth - curComponentStyle.halfWidth,
                            lineShift: componentStyle.left + componentStyle.halfWidth,
                        },
                        {
                            isNearly: this.isNearly(curComponentStyle.left, componentStyle.right),
                            lineNode: lines.yr[0], // yr
                            line: 'yr',
                            dragShift: componentStyle.right,
                            lineShift: componentStyle.right,
                        },
                        {
                            isNearly: this.isNearly(curComponentStyle.right, componentStyle.right),
                            lineNode: lines.yr[0], // yr
                            line: 'yr',
                            dragShift: componentStyle.right - curComponentStyle.width,
                            lineShift: componentStyle.right,
                        },
                    ],
                }

                const needToShow = []
                Object.keys(conditions).forEach(key => {
                    // 遍历符合的条件并处理
                    conditions[key].forEach((condition) => {
                        if (!condition.isNearly) return

                        // console.log(" condition ", condition);
                        // console.log("[匹配到辅助线] ", component.key, component.type, component.style, curComponentStyle, condition);

                        // 修改当前组件定位：top | left
                        this.kFormDesign.widgetFormSelect.style[key] = condition.dragShift;

                        // 修改连线属性
                        condition.lineNode.style[key] = `${condition.lineShift}px`
                        needToShow.push(condition.line)
                    })
                })

                // 同一方向上同时显示三条线可能不太美观，因此才有了这个解决方案
                // 同一方向上的线只显示一条，例如多条横条只显示一条横线
                if (needToShow.length) {
                    // this.chooseTheTureLine(needToShow, isDownward, isRightward)
                    needToShow.forEach(key => {
                        this.lineStatus[key] = true
                    });
                }
            })
        },

        isNearly(dragValue, targetValue) {
            return Math.abs(dragValue - targetValue) <= this.diff
        },

        getNodeStyle: function(element){
            let dom = $("." + element.customClass);
            // getBoundingClientRect返回信息：top、bottom、left、right、width、height、x、y
            if(dom) {
                let domStyle = dom.getBoundingClientRect();
                return {
                    top: element.style.top,
                    left: element.style.left,
                    width: domStyle.width,
                    height: domStyle.height,
                    bottom: element.style.top + domStyle.height,
                    right: element.style.left + domStyle.width,
                    halfWidth: domStyle.width / 2,                   // x 中点
                    halfHeight: domStyle.height / 2                  // y 中点
                }
            } else {
                return {}
            }
        },
    },
}
</script>

<style lang="scss" scoped>
.mark-line {
    // height: 100%;
}

.line {
    background: #59c7f9;
    position: absolute;
    z-index: 1000;

    top: 0px;
    left: 0px;
}

.xline {
    width: 100%;
    height: 1px;
}

.yline {
    width: 1px;
    height: 100%;
}

.demo {
    position:fixed;
    // top:30px;
    bottom: 10px;
    right:5px;
    width: 200px;
    height: 100px;
    color: red
}
</style>
