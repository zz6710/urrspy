---

>	Description: 表单控件

>	Author: 殷王雄

---

基于wangEditor实现，(http://www.wangeditor.com/index.html)

属性：
    data-upload-img: 图片上传地址

事件：
  data-img-check: 上传图片校验，成功返回true，错误返回错误内容，如 return '验证失败'
  -	data-on-change：定义修改事件
  -	data-on-focus：定义获取焦点事件
  -	data-on-blur：定义失去焦点事件
  -	data-validate：指定一个JS函数，用于输入验证（稍后考虑如何实现）
