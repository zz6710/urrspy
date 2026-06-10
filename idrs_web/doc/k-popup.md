---

>	Description: 弹窗控件

>	Author: 李博

---

属性：
  data-mask：是否显示遮罩，默认true
  data-title: 表单标题
  data-title-align: dialog弹出框标题对齐方式：left/center 默认center，仅对data-type=dialog
  data-type: 弹窗类型。dialog/drawer，默认dialog
  data-direction: 抽屉打开的方向，top/bottom/left/right 默认right，仅对data-type=drawer有效
  data-confirm-close: 弹窗关闭提醒，默认false
  data-confirm-describe: 弹窗关闭提醒内容，默认：是否确认关闭
  data-width: 弹窗宽度,当data-type=drawer且data-direction=top或bottom时，该参数为高度
  data-width-percent：弹窗宽度比例，当data-type=drawer且data-direction=top或bottom时，该参数为高度
  data-fullscreen: 是否全屏展示



事件：
  data-before-close: 关闭前事件，如果有返回内容，则弹窗提示内容，并且不关闭
  data-open: 弹窗打开事件
  data-close: 弹窗关闭事件


函数：
    popup(): 打开弹窗
    close(): 关闭弹窗
