---

>	Description: 上传控件

>	Author: 殷王雄

---
属性：
 -	data-upload-url：上传地址，默认：base/comnUpload.json
 -	data-type: 上传格式，img/file, 默认file。img会显示图片上传样式，文件会显示文件上传样式
 -	data-multiple: 是否支持多选，默认fasle
 -	data-params: 指定默认上传参数
 -	data-drag: 是否支持拖拽上传，默认true
 -	data-accept: 接受的文件格式
 -	data-disabled: 是否禁用，默认true
 -	data-limit: 最大上传数量限制

事件：
 -	data-preview: 点击文件列表中已上传的文件时的钩子
 -	data-remove: 文件列表移除文件时的钩子
 -	data-success: 文件上传成功时的钩子
 -	data-error: 文件上传失败时的钩子
 -	data-progress: 文件上传时的钩子
 -	data-before-upload: 上传文件之前的钩子，参数为上传的文件，若返回 false 或者返回 Promise 且被 reject，则停止上传
 -	data-before-remove: 删除文件之前的钩子，参数为上传的文件和文件列表，若返回 false 或者返回 Promise 且被 reject，则停止删除
 -	data-auto-upload: 是否在选取文件后立即进行上传，默认true
 -	data-exceed: 文件超出个数限制时的钩子

函数：
upload(params);上传函数
getSuccessUpload();获取成功上传的文件列表
getErrorUpload();获取失败上传的文件列表
reset();重置上传控件，清空上传列表
abort();取消上传
