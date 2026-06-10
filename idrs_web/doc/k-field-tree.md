---

>	Description: 树下拉框控件

>	Author: 崔毅丰

---

-	data-allowblank：true/false，是否允许为空
-	data-validate：指定一个JS函数，用于输入验证
-	data-on-change：定义修改事件
-	data-on-focus：定义获取焦点事件
-	data-on-blur：定义失去焦点事件
-	data-disabled：true/false，输入控件是否不可用
-	data-params：指定固定的查询参数值
-	data-action：指定数据来源action
-	data-graphql：指定数据来源graphql
-	data-value-field：指定查询数据中作为值的字段名称
-	data-display-field：指定查询数据中作为文本显示的字段名称，可以指定多个字段名（以半角逗号分隔），那么显示的时候就以data-display-separator分隔符串起来
-	data-display-separator：指定显示多个字段值的时候字段值之间的分隔符，默认为“-”
-	data-on-beforeload：指定加载数据之前触发调用的JS函数
-	data-on-afterload：指定加载数据之后触发调用的JS函数
-	data-async：true/false，指定是否异步加载数据
-	data-select-branch：true/false，是否能选择分支节点
-	data-multiple：是否支持多选
