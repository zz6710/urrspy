<template>
    <div :id="id" class="myStyle">
    </div>
</template>

<script>
    import Tools from "@/utils/tools";

    export default {
        name: "changxieOffice",
        props: {
            option: {
                type: Object,
                default: () => {
                    return {}
                }
            }
        },
        data() {
            return {
                doctype: '',
                docEditor: {},
                cxoConfig: {},
                id:''
            }
        },
        // watch: {
        //   option: {
        //     handler: function(n) {
        //       this.setEditor(n)
        //       this.doctype = this.getFileType(n.fileType)
        //     },
        //     deep: true,
        //   },
        // },
        mounted() {

        },
        created() {
            this.id = Tools.dateFormat('YYYYmmddHHMMSS',new Date());
            console.log("option",this.option);
            this.cxoConfigInfo(this.option);

        },
        methods: {
            destroy() {
                this.docEditor.destroyEditor();
            },
            cxoConfigInfo(option) {
                this.$nextTick(() => {
                    var _this = this;
                    /* eslint-disable no-undef */
                    this.cxoConfig = {
                        //基础参数   必传
                        "document": { //文档参数集
                            "fileType": option.fileType,  //指明要打开的文件类型，例如"xlsx"、"pptx"
                            "key": option.key,            //文档唯一ID
                            "title": option.title,        //文档标题名称
                            "usePdfjs": true,             //是否使用pdf.js插件打开pdf类型文档  默认为true
                            "url": getURL().onlineUrl + option.url,            //文档存放路径
                            "permissions": option.permissions,
                        },
                        "documentType": option.documentType,//指明文档类型，文字处理："word"、电子表格："cell"、演示文稿："slide"
                        "height": "100%",                   //设置文档编辑器在浏览器中的高度，默认"100%"
                        "type": option.type || "desktop",     //设置平台类型：PC端"desktop",手机端"mobile"
                        "width": "100%",                    //设置文档编辑器在浏览器中的宽度，默认"100%"
                        "editorConfig": {
                            "callbackUrl": getURL().wordEditServer + "/file/saveFile.json?filePath=" + option.url,   //回调接口URL
                            "mode": option.mode,                     //指定文档打开模式,默认值"edit"。只读模式"view",编辑模式"edit"
                            "user": {                           //用户信息
                                "id": option.user.userid,                       //用户key
                                "name": option.user.username//用户名
                            },
                            "limitEditMode": option.limitEditMode,  //部分编辑模式，"nolimit"无限制，"ctctrl"只能对未设置只读的内容域进行编辑
                        },
                        "events": {
                            "onDocumentReady": this.onDocumentReady,
                            "onGetDocumentContent": this.onGetDocumentContent,
                        },
                    };

                    console.log("this.this.cxoConfig",this.cxoConfig);
                    this.docEditor = new CXO_API.CXEditor(this.id, this.cxoConfig);

                })

            },

            onDocumentReady(e) {
                this.docEditor.getDocumentContent({
                    "object": "content",  //表示操作对象为内容域，必填项
                    "type": "property"       //提取类型，必填项 all全部提取 table提取表格 image提取图片
                });
                console.log("this.docEditor", this.docEditor);
            },

            onGetDocumentContent(e) {
                console.log("e", e.data);
                let role = [];
                var list = e.data.list;
                console.log("list",list)
                let printTempVersionId = this.option.printTempVersionId;
                console.log("printTempVersionId",printTempVersionId);
                console.log("this.option.operateType",this.option.operateType);
                if (this.option.operateType == 'mod' && list.length > 0) {  //模板上传初始化
                    for (let i = 0; i < list.length; i++) {
                        role.push({
                            "domainId":list[i].id,
                            "domainName":list[i].title,
                            "t8PrintTempVersionId":printTempVersionId,
                        });
                    }
                    console.log("role",role);
                    //新增模板内容域字段
                    this.httpUtil.comnUpdate({
                        action:'T8PrintTempPermission.addT8PrintTempPermission',
                        params:{'listP':JSON.stringify(role)},
                        successAlert: false,
                    }).then(re =>{});

                }
                // let role = [];
                // var list = e.data.list;//
                // for (var i = 0; i < list.length; i++) {
                //  var item = list[i];
                //  if (this.roleList.length > 0) {
                //   for (let j = 0; j < this.roleList.length; j++) {
                //    if (item.name ==  this.roleList[j].control_name) {   //有编辑权限
                //     role.push({
                //      id: item.id,         //内容域的id，时间戳，纯数字，与name互斥，优先级低于name
                //      mode: 'edit',   //必填项，edit可编辑可删除、readonly不可编辑不可删除、nodelete可编辑不可删除、noedit可删除不可编辑
                //      displayname:item.title,  //标题，编辑区显示的名字
                //      pholder:'请输入',    //预设文字，默认同界面操作，各类型不同
                //      border:"default"           //内容域边框 'none' 'default' 或 {r,g,b}
                //
                //     });
                //    } else {  //没有编辑权限
                //     role.push({
                //      id: item.id,         //内容域的id，时间戳，纯数字，与name互斥，优先级低于name
                //      mode: 'readonly',   //必填项，edit可编辑可删除、readonly不可编辑不可删除、nodelete可编辑不可删除、noedit可删除不可编辑
                //      displayname:item.title,  //标题，编辑区显示的名字
                //      pholder:'请输入',    //预设文字，默认同界面操作，各类型不同
                //      border: "default"
                //     });
                //    }
                //    break;
                //   }
                //  } else {
                //   role.push({
                //    id: item.id,         //内容域的id，时间戳，纯数字，与name互斥，优先级低于name
                //    mode: 'readonly',   //必填项，edit可编辑可删除、readonly不可编辑不可删除、nodelete可编辑不可删除、noedit可删除不可编辑
                //    displayname:item.title,  //标题，编辑区显示的名字
                //    pholder:'请输入',    //预设文字，默认同界面操作，各类型不同
                //    border: "default"
                //   });
                //  }
                // }
                // var paramObject = {
                //  object: 'content',
                //  type: 'setting',
                //  list: role
                // }
                // console.log("paramObject",paramObject);
                // this.docEditor.setDocumentContent(paramObject);
            },

        },
    }
</script>


<style>
    /*设置高度为100%*/
    iframe:not(.md-image) {
        height: 100%;
    }
</style>
