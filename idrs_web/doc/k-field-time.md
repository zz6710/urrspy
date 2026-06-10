---

>	Description: 时间选择框控件

>	Author: 李博

---

-	data-allowblank：true/false，是否允许为空
-	data-validate：指定一个JS函数，用于输入验证
-	data-on-change：定义修改事件
-	data-on-focus：定义获取焦点事件
-	data-on-blur：定义失去焦点事件
-	data-max-value：指定字段最大值，默认为小于等于，如果需要设置成小于，在设置值的右边加上半角右括号“)”，例：data-max-value="121000)"表示小于12:10:00的时间
-	data-min-value：指定字段最大值，默认为大于等于，如果需要设置成大于，在设置值的左边加上半角左括号“(”，例：data-min-value="(120000"表示大于12:00:00的时间
-	data-disabled：true/false，输入控件是否不可用
-	data-value-format: 日期值格式 默认hhmmss
