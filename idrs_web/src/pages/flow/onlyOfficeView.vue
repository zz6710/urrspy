<template>
  <div id="vabOnlyOffice" class="myStyle">
    <el-image v-show="imageUrl" :src="imageUrl"></el-image>
  </div>
</template>

<script>
import Tools from "@/utils/tools";

export default {
  name: "VabOnlyOffice",
  props:{
    option:{
      type: Object,
      default:() =>{
        return {}
      }
    }
  },
  data() {
    return {
      doctype: '',
      docEditor: null,
      imageUrl:'',
    }
  },
  beforeDestroy() {
    if (this.docEditor !== null) {
      //
      console.log('执行了beforeDestroy（）')
      this.docEditor.destroyEditor();
      this.docEditor = null;
    }
  },
  watch: {
    option: {
      handler: function(n) {
        this.setEditor(n)
        this.doctype = this.getFileType(n.fileType)
      },
      deep: true,
    },
  },
  mounted() {
    if (this.option.url) {
      this.setEditor(this.option)
    }
  },
  created() {
  },
  methods: {
    async setEditor(option) {
      console.log('传入的参数==>',option)
      if (option.fileType=='png'||option.fileType=='jpg'||option.fileType=='jpeg'||option.fileType=='gif'||option.fileType=='bmp'||option.fileType=='webp'||option.fileType=='psd'||option.fileType=='svg'||option.fileType=='tiff'){
        this.$nextTick(()=>{
          this.imageUrl = option.url;
        })
      }else {
        if (this.docEditor !== null) {
          this.docEditor.destroyEditor();
          this.docEditor = null;
        }
        this.doctype = this.getFileType(option.fileType)
        let config = {
          document: {
            //后缀
            fileType: option.fileType,
            key: option.key ||'',
            title: option.title,
            //status:2,
            permissions: {
              edit: option.isEdit,//是否可以编辑: 只能查看，传false
              print: option.isPrint,
              download: false,
              // "fillForms": true,//是否可以填写表格，如果将mode参数设置为edit，则填写表单仅对文档编辑器可用。 默认值与edit或review参数的值一致。
              // "review": true //跟踪变化
            },
            url: option.url,
          },
          documentType: this.doctype,
          editorConfig: {
            //"编辑word后保存时回调的地址，这个api需要自己写了，将编辑后的文件通过这个api保存到自己想要的位置
            callbackUrl: option.editUrl,
            //callbackUrl: this.callBackMethod(),
            lang: option.lang,//语言设置
            //定制
            customization: {
              autosave: false,//是否自动保存
              chat: false,
              comments: false,
              help: false,
              // "hideRightMenu": false,//定义在第一次加载时是显示还是隐藏右侧菜单。 默认值为false
              //是否显示插件
              plugins: false,
              // logo: {
              //   image: "https://file.iviewui.com/icon/viewlogo.png",
              //   imageEmbedded: "https://file.iviewui.com/icon/viewlogo.png",
              // },
            },
            user:{
              id:option.user.id,
              name:option.user.name
            },
            mode:option.model?option.model:'edit',
          },
          width: '100%',
          height: '100%',
          token:option.token||''
        }
        // eslint-disable-next-line no-undef,no-unused-vars
        try {
          this.docEditor = new DocsAPI.DocEditor('vabOnlyOffice', config)
        }catch (e) {
          Tools.alert("文件服务器异常，请联系管理员","danger")
        }
      }

    },
    //请求后端保存接口
    callBackMethod(){
      console.log('callBackMethod()=>',this.option.url)
      this.httpUtil
        .ajax({
          url: "/wf/wf/attachment/uploadEditFile.json",
          params: {
           // path:this.option.url
          },
          successAlert: false,
        }).then(res => {
          console.log('请求回调函数',res)
      });
    },
    //关闭编辑器
    closeEditor(){
      console.log('子组件执行了，关闭的方法')
      this.docEditor.requestClose();
    },
    //获取文件类型
    getFileType(fileType) {
      console.log('文件类型',fileType)
      let docType = ''
      let fileTypesDoc = [
        'doc', 'docm', 'docx', 'dot', 'dotm', 'dotx', 'epub', 'fodt', 'htm', 'html', 'mht', 'odt', 'ott', 'pdf', 'rtf', 'txt', 'djvu', 'xps',
      ]
      let fileTypesCsv = [
        'csv', 'fods', 'ods', 'ots', 'xls', 'xlsm', 'xlsx', 'xlt', 'xltm', 'xltx',
      ]
      let fileTypesPPt = [
        'fodp', 'odp', 'otp', 'pot', 'potm', 'potx', 'pps', 'ppsm', 'ppsx', 'ppt', 'pptm', 'pptx',
      ]
      if (fileTypesDoc.includes(fileType)) {
        docType = 'text'
      }
      if (fileTypesCsv.includes(fileType)) {
        docType = 'spreadsheet'
      }
      if (fileTypesPPt.includes(fileType)) {
        docType = 'presentation'
      }
      return docType
    }
  },
}
</script>


<style>
/*设置高度为100%*/
iframe:not(.md-image){
  height: 100%;
}
</style>
