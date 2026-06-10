---

>	Description: 输入框控件 k-field-text为输入框控件，在el-input基础上改造。需提供的属性如下

>	Author: 代超

---

-	data-validate-type：验证类型，可选值如下
	- 	email：验证电子邮箱
	-	code：限制只能输入数字文本，左对齐
	-	int：验证整型数，右对齐
	- 	number：验证包含小数点的数字，右对齐
	- 	postcode：验证邮政编码
	- 	telephone: 验证手机号
	-	money：验证金额，右对齐
	- 	text:允许输入中文，数字和英文字母，右对齐
-	data-allowblank：true/false，是否允许为空
-	data-validate：指定一个JS函数，用于输入验证（稍后考虑如何实现）
-	data-on-change：定义修改事件
-	data-on-focus：定义获取焦点事件
-	data-on-blur：定义失去焦点事件
-	data-min-length：最小长度，提示未达到长度
-	data-max-length：最大长度，限制不能输入超长的字符
	data-show-word-limit: 属性来展示字数统计
	data-show-password: 是否显示为密码
-	data-max-value：指定字段最大值，默认为小于等于，如果需要设置成小于，在设置值的右边加上半角右括号“)”，例：data-max-value="1000)"表示小于1000的值
-	data-min-value：指定字段最大值，默认为大于等于，如果需要设置成大于，在设置值的左边加上半角左括号“(”，例：data-min-value="(0"表示大于0的值
-	data-disabled：true/false，输入控件是否不可用
-	data-show-gbmoney：true/false，定义data-validate-type=”money”时，是否显示大写中文金额（可以先不实现）
-	data-regx：可指定用于验证输入的正则表达式
-	data-regx-text：可指定正则验证失败的提示信息
-	data-digits：指定有多少位小数（未实现）
-   data-placeholder: 占位文本
