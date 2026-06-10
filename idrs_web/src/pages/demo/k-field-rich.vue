<template>
  <div>
    <k-field-code>
      <textarea>
  - data-img-check: 上传图片校验，成功返回true，错误返回错误内容，如 return '验证失败'
  -	data-on-change：定义修改事件
  -	data-on-focus：定义获取焦点事件
  -	data-on-blur：定义失去焦点事件
  -	data-validate：指定一个JS函数，用于输入验证（稍后考虑如何实现）
          </textarea>
    </k-field-code>

  <k-field-rich
        v-model="detail"
        :data-img-check="check"
        @data-on-change="change"
        @data-on-blur="blur"
        @data-on-focus="focus"
      ></k-field-rich>
     <k-field-code>
      <textarea>
代码：
<k-field-rich
        v-model="detail"
        :data-img-check="check"
        @data-on-change="change"
        @data-on-blur="blur"
        @data-on-focus="focus"
      ></k-field-rich>
      </textarea>
    </k-field-code>
  </div>
</template>
<script>
// import EditorBar from "../components/public/editor";
import kayak from '@/frame/kayak.js'
export default {
//    components: { EditorBar },
   data() {
    return {
       detail: ''
       };
   },
   methods: {
     change(val) {
       console.log(val);
     },
     check(link) {
       if(link.length < 8) {
         return '插入失败,链接最小长度为8'
       } else {
         return true
       }
     },
     blur() {
         console.log("失焦啦")
     },
     focus() {
         console.log("聚焦啦")
     }
   }
 };
 </script>
