---

>	Description: 按钮控件

>	Author: 刘月贵

---
-	data-descript：按钮详细说明的提示信息
-	data-functype：按钮类型，可选值如下
 	-   RESET
 	-   SUBMIT
 	-   EXPORT
 	-   POPUP
 	-   PAGE
-	data-menuid：当data-functype=”PAGE”的时候，指定跳转到的页面的menuid
-	data-menuname：当data-functype=”PAGE”的时候，指定跳转到的页面的title
-	data-icon：当data-functype=”PAGE”的时候，指定跳转到的页面的icon
-	data-from：可配置一个CSS选择器字符串，按钮提交时将获取该选择器下所有输入字段作为参数提交
-	d：操作目标，根据按钮类型有不同的作用
 	-   RESET：重置目标下的所有表单字段值
 	-   SUBMIT：提交去刷新的grid列表
 	-   EXPORT：导出目标列表数据
 	-   POPUP： 修改弹出状态
-	data-action：提交请求的地址
-	data-graphql：提交请求的地址
-	data-confirm：点击提交按钮时是否弹出确认提示框，为true则以按钮的descript为提示内容弹出确认提示，为其他字符串则以这个字符串为提示内容弹出确认提示
-	data-params：指定提交的固定参数值
-	data-disable-condition：不可用条件JS函数
-	data-handler：参数处理函数，JS函数里返回false阻止按钮动作
-	data-after-success：提交成功之后调用
-	data-disabled：是否不可用
