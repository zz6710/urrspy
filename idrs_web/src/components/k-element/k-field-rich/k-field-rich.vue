<template lang="html">
       <div
       :value="value"
       ref="editor"
       ></div>
</template>

<script>
  import E from "wangeditor";
  import props from "@/components/k-element/common/k-field-props.js";
  import event from "@/components/k-element/common/k-field-event.js";
  import emitter from "@/components/k-element/common/k-emitter.js";
  export default {
    name: 'KFieldRich',
    mixins: [props(), event(), emitter()],
    data() {
      return {
        editor: null,
      }
    },
    props: {
      dataImgCheck: {
        type: Function,
        default: function() {
          return true
        }
      },
      dataMenus: {
        type: Array,
        default: () => [
          "head", // 标题
          "bold", // 粗体
          "fontSize", // 字号
          "fontName", // 字体
          "italic", // 斜体
          "underline", // 下划线
          "strikeThrough", // 删除线
          "foreColor", // 文字颜色
          "backColor", // 背景颜色
          "link", // 插入链接
          "list", // 列表
          "justify", // 对齐方式
          "quote", // 引用
          "emoticon", // 表情
          "image", // 插入图片
          "table", // 表格
          "video", // 插入视频
          "code", // 插入代码
          "undo", // 撤销
          "redo", // 重复
          "fullscreen" // 全屏
        ]
      }
    },
    watch: {
      value: function(value) {
        if (value !== this.editor.txt.html()) {
          this.editor.txt.html(this.value);
        }
      }
    },
    mounted() {
      this.seteditor(this.dataImgCheck);
      this.editor.txt.html(this.value);
    },
    methods: {
        seteditor(dataImgCheck) {
            this.editor = new E(this.$refs.editor);
            this.editor.customConfig.uploadImgShowBase64 = true; // base 64 存储图片
            // this.editor.customConfig.uploadImgServer = ""; // 配置服务器端地址
            // this.editor.customConfig.uploadImgHeaders = {}; // 自定义 header
            // this.editor.customConfig.uploadFileName = "file"; // 后端接受上传文件的参数名
            this.editor.customConfig.uploadImgMaxSize = 2 * 1024 * 1024; // 将图片大小限制为 2M
            this.editor.customConfig.uploadImgMaxLength = 6; // 限制一次最多上传 6 张图片
            this.editor.customConfig.uploadImgTimeout = 3 * 60 * 1000; // 设置超时时间

            // 配置菜单
            this.editor.customConfig.menus = this.dataMenus;

            this.editor.customConfig.uploadImgHooks = {
                fail: (xhr, editor, result) => {
                    // 插入图片失败回调
                },
                success: (xhr, editor, result) => {
                    // 图片上传成功回调
                },
                timeout: (xhr, editor) => {
                    // 网络超时的回调
                },
                error: (xhr, editor) => {
                // 图片上传错误的回调
                },
                customInsert: (insertImg, result, editor) => {
                    // 图片上传成功，插入图片的回调
                }
            };
            this.editor.customConfig.onchange = (html) => {
            //   this.value = html // 绑定当前逐渐地值
              this.$emit('data-on-change', html) // 将内容同步到父组件中
              this.$emit('input', html)
            }
            this.editor.customConfig.onfocus = () => {
                this.$emit('data-on-focus')
            }
            this.editor.customConfig.onblur = () => {
                this.$emit('data-on-blur')
            }
            this.editor.customConfig.linkCheck = function (text, link) {//插入文字和链接的校验
                  return dataImgCheck(link)
            }
            this.editor.customConfig.linkImgCheck = function (src) {//插入网络图片的校验
                return true // 返回 true 表示校验成功
                // return '验证失败' // 返回字符串，即校验失败的提示信息
            }
            this.editor.create()
        }
    }
}
</script>
