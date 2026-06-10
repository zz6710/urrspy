---

>	Description: 步骤条控件

>	Author: 李博

---
k-steps:
属性：
-	data-direction：显示方向，vertical/horizontal， 默认horizontal
-	data-active: 设置当前激活步骤，类型number（点击"完成"按钮后，会触发所有步骤的校验，如果通过data-active跳过步骤导致最终校验不通过，会出现点击"完成"无响应）
-	data-next-show: 是否显示下一步按钮，默认true
-	data-submit-show: 是否显示提交按钮，默认true
-	data-button-align: 默认按钮对齐方式，left/center/right 默认center

表单属性：
-   data-col: 每一行展示的k-form-item数量
提交按钮属性，类型为SUBMIT,详细示例见k-btn组件示例：
-	data-action：提交请求的地址
-	data-graphql：提交请求的地址
-	data-confirm：点击提交按钮时是否弹出确认提示框，为true则以按钮的descript为提示内容弹出确认提示，为其他字符串则以这个字符串为提示内容弹出确认提示


事件：
-	data-active-change: 步骤条变动事件，参数，newVal，oldVal

函数：
pre();上一步
next();下一步


k-step:
-	data-icon：自定义图标
-	data-title: 自定义标题
-	data-description: 自定义描述性文字
