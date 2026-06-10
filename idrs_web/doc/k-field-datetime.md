---

>	Description: 日期时间选择框控件

>	Author: 殷万雄

---

-	data-validate：指定一个JS函数，用于输入验证
-	data-type: 显示类型 year/month/date/week/datetime  默认datetime
-	data-date-format：指定date控件显示的日期格式，默认为yyyy-dd-mm
-	data-value-format：指定date控件显示的日期格式，默认为yyyyMMddhhmmss, 如果为long，则为时间戳，单位毫秒
-	data-on-change：定义修改事件
-	data-on-focus：定义获取焦点事件
-	data-on-blur：定义失去焦点事件
-	data-max-value：指定字段最大值，默认为小于等于，如果需要设置成小于，在设置值的右边加上半角右括号“)”，例：data-max-value="20140101)"表示小于2014-01-01的日期
-	data-min-value：指定字段最大值，默认为大于等于，如果需要设置成大于，在设置值的左边加上半角左括号“(”，例：data-min-value="(20140101"表示大于2014-01-01的日期
-	data-disabled：true/false，输入控件是否不可用
-	data-workday：true则表示只能选择系统工作日日期，其他值则可直接指定工作日方案代码
