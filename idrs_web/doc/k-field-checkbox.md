---

>	Description: 复选框控件

>	Author: 崔毅丰

---

>   注：值的格式为逗号分隔的字符串
-	data-allowblank：true/false，是否允许为空
-	data-on-change：定义修改事件
-	data-disabled：true/false，输入控件是否不可用
-	data-dict：树控件的dict只用作翻译值的参考
-	data-params：指定固定的查询参数值
-	data-action：指定数据来源action
-	data-graphql：指定数据来源graphql
-	data-value-field：指定查询数据中作为值的字段名称
-	data-display-field：指定查询数据中作为文本显示的字段名称，可以指定多个字段名（以半角逗号分隔），那么显示的时候就以data-display-separator分隔符串起来
-	data-display-separator：指定显示多个字段值的时候字段值之间的分隔符，默认为“-”
-	data-on-beforeload：指定加载数据之前触发调用的JS函数
-	data-on-afterload：指定加载数据之后触发调用的JS函数
-	data-ui-type：选择样式，check或者button，默认值是check
-	data-min-num：最少选择数量
-	data-max-num：最多选择数量
-	data-text-color: 按钮形式的 Checkbox 激活时的文本颜色
-	data-fill-color：按钮形式的 Checkbox 激活时的填充色和边框色
