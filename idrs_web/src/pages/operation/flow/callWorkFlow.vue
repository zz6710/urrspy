<template>
  <div>

    <k-form ref="prodGroupForm1" :data-col="3" data-input-width="560px" data-label-width="180px" data-total-width="1188px" style="width: 100%"  >

      <k-form-item label="审批任务名称" :data-col="2" data-input-width="560px">
        <k-field-text v-model="formData.displayName"   :data-allowblank="false"/>
      </k-form-item>
      <k-form-item v-if="!opFlowStatus"  v-for="(item,index) in dataList" :key="index" :label="item.FIELD_NAME" data-label-width="180px" :hidden="states" style="width: 100%">
        <k-field-text v-model="item.FIELD_VALUE"  :data-allowblank="true" :rows="1" data-disabled/>
      </k-form-item>

    </k-form>

    <k-form v-if="!opFlowStatus" ref="prodGroupForm2" v-for="(item,index) in dataList" :key="item.FIELD_NAME"  :data-col="2" data-input-width="560px" style="width: 100%"
            data-label-width="180px" data-total-width="1118px" :hidden="state" >
      <k-form-item :label="item.FIELD_NAME" :data-col="2" data-input-width="560px">
        <k-field-text v-model="item.FIELD_VALUE" :data-allowblank="true" input-type="textarea" :data-max-length="500" :rows="1"/>
      </k-form-item>
    </k-form>

    <!-- 处理产品清盘流程表单的数据 -->
    <k-form v-if="opFlowStatus" :data-col="2" data-label-width="180px" data-total-width="1118px">
      <template v-for="item in opFlowList">
        <k-form-item  :label="item.paramName">
          <k-field-date v-if="item.funcType==='date'" v-model="item.value" :data-disabled="true"/>
          <k-field-radio v-else-if="item.funcType==='radio'" v-model="item.value" :data-dict="item.dict" :data-disabled="true"/>
          <k-field-text v-else v-model="item.value" :data-disabled="true"/>
        </k-form-item>
      </template>
    </k-form>


    <!--  附件信息  -->
    <div style="width: 100%">
      <h4>附件信息</h4>
      <k-btn class="btn-custom-primary" style="width: 130px" data-functype="POPUP" data-target="uploadAttachmentPopup">
        <md-icon >cloud_upload</md-icon>上传附件
      </k-btn>
      <!--  附件信息列表START      -->
      <el-table
        ref="newAttachmentGrid"
        :data="attachmentList"
        style="width: 100%;">
        <el-table-column v-if="false" prop="id" label="id">
        </el-table-column>
        <el-table-column prop="fileType" label="附件类型" width="120" height="20px" align="center">
        </el-table-column>
        <el-table-column prop="originalFilename" label="附件名称" width="300" height="20px" align="center">
        </el-table-column>
        <el-table-column prop="username" label="上传人" width="100" height="20px" align="center">
        </el-table-column>
        <el-table-column prop="createDate" label="上传时间" width="150" height="20px" align="center">
        </el-table-column>
        <el-table-column label="操作" width="200" height="120px">
          <template slot-scope="scope">
            <k-btn class="btn-custom-text" data-descript="删除" data-functype="POPUP" data-size="mini"
                   :data-confirm="false" data-target="newAttachmentGrid"
                   v-if="isShowButton(scope.row)"
                   @click="deleteFile(scope.$index, scope.row)">
              <!--              v-if="isShowAuthorityButton(fromData,scope.row)"-->
              删除
            </k-btn>
            <!--            <el-button type="danger"  @click="deleteFile(scope.row)">删除</el-button>-->
            <k-btn class="btn-custom-text" data-descript="附件下载" data-functype="POPUP" data-size="mini"
                   :data-confirm="true" data-target="newAttachmentGrid" @click="download(scope.row)">
              下载
            </k-btn>
          </template>
        </el-table-column>
      </el-table>
      <!--  附件信息列表END      -->
    </div>

    <k-form ref="prodGroupForm3" :data-col="3" data-input-width="220px" data-label-width="180px" data-total-width="1188px" style="width: 100%;margin-top: 20px" >
      <k-form-item label="经办说明" data-input-width="220px" :data-col="2">
        <k-field-text v-model="formData.remark" :data-max-length="500" inputType="textarea" :rows="3"/>
      </k-form-item>
    </k-form>



    <k-form ref="prodGroupUserForm1" :data-col="3"  data-input-width="160px"  data-label-width="80px" data-total-width="1188px">
      <k-form-item label="业务操作" :hidden="true">
        <k-field-select v-model="formData.server"  data-action="WfBusinessConfig.findServerMethodTree" data-display-field="name" :data-allowblank="true"
                        data-value-field="id" />
      </k-form-item>

<!--      <k-form-item label="审批任务名称" data-label-width="120px">-->
<!--        <k-field-text v-model="formDat.name"   data-display-field="name" :data-allowblank="false"-->
<!--                      data-value-field="id" />-->
<!--      </k-form-item>-->

      <k-form-item label="是否在移动端显示" data-label-width="180px" :hidden="true">
        <k-field-select v-model="formData.appDisplay" data-dict="1yes0no" :data-allowblank="true"/>
      </k-form-item>

      <k-form-item label="流程模板id"  :hidden="true">
        <k-field-select v-model="formData.processId" />
      </k-form-item>

      <k-form-item label="url"  :hidden="true">
        <k-field-select v-model="formData.formUrl" />
      </k-form-item>

      <k-form-item label="流程id"  :hidden="true">
        <k-field-select v-model="formData.formId" />
      </k-form-item>

      <k-form-item label="是否允许全流程" :hidden="true">
        <k-field-text v-model="formData.isPush" />
      </k-form-item>

    </k-form>
    <div style="border:solid 1px" ></div><br>

    <k-form ref="prodGroupUserForm2" v-for="(item,index) in envItems" style="display: inline-block" :key="index"
     :data-col="4" data-label-width="80px" data-input-width="150px" data-total-width="1118px">
      <k-form-item label="节点名称">
        <k-field-text v-model="item.nodeName" :data-allowblank="false" :data-disabled="isDisable(index)"/>
      </k-form-item>
      <k-form-item label="审批类型" >
        <k-field-select v-model="item.nodeType"  data-dict="approval_type" :data-disabled="isDisable(index)" :data-allowblank="false" />
      </k-form-item>
      <k-form-item label="审批等级" :hidden="true">
        <k-field-text v-model="item.nodeLevel = index+1 " />
      </k-form-item>
      <k-form-item label="审批人员" >
        <k-field-select v-model="item.actorId" :data-data="readonlyActorList" data-display-field="label" :data-multiple="true" :data-allowblank="false"
                        data-value-field="value" />
      </k-form-item>
      <k-form-item v-if="index == envItems.length-1">
        <k-btn class="md-info md-just-icon md-simple" data-descript="新增" @click="()=>envItems.push({})" v-show="addNodeStatus" >
          <md-icon>add</md-icon>
        </k-btn >
        <k-btn class="md-info md-just-icon md-simple" data-descript="删除当前行" @click="deleteEvent(index)" v-show="addNodeStatus">
          <md-icon md-src="/static/svg/delete.svg" />
        </k-btn>
      </k-form-item>

    </k-form>

    <div style="border:solid 1px" ></div><br>

    <k-form ref="prodGroupUserForm3" :data-col="5" style="width: 100%">
      <k-form-item label="节点名称" >
        <k-field-text v-model="formData.nodeName " :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="审批类型" >
        <k-field-text v-model="formData.nodeType "  :data-disabled="true"  />
      </k-form-item>
      <k-form-item label="抄送人员" >
        <k-field-select v-model="formData.readonlyActor" :data-data="readonlyActorList" :data-default-value="this.readonlyList" :data-multiple="true" data-display-field="label" data-value-field="value" />
      </k-form-item>
    </k-form>

    <div style="margin: 0 auto; width: 255px">
      <k-btn class="btn-custom-primary" data-functype="SUBMIT"  :data-handler="submitHandle"
             data-target="prodInfoGrid" :data-model="formData" >
        <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
      </k-btn>
      <k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
    </div>

    <!--  上传附件弹框  -->
    <k-popup ref="uploadAttachmentPopup" data-title="上传附件" data-width="40%">
      <k-form ref="fileForm" data-ui="element">
        <k-field-upload data-type="file" ref="uploadFileRef" :data-multiple="false"
                        :data-error="onFileSubmitError" :data-success="onFileSubmitSuccess"
                        :data-auto-upload="false"
                        data-upload-url="wf/upload/uploadFile.json">
        </k-field-upload>
        <!--          </k-form-item>-->
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="attachmentGrid" ref="fileSubmitBtn"
                 data-from="fileForm" :data-model="formData" :data-handler="fileSubmitUploadParam">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

  </div>
</template>

<script>
  import Tools from "@/utils/tools";

  export default {
    name: "callWorkFlow",
    props: {
      fromData:{},
      disNoticeId : ''
    },

    data() {
      return {
        showGroupName:false,
        state:false,
        states:false,
        formData:{},
        formDataList:{},
        nodeList:[],
        userList:{},
        prodGroupUserItems:[],
        envItems: [],
        list:{},
        queryRoleGraphql: "{queryRole(action:\"find\",roleids:\"0\") {rows{roleid, rolename, parentroleid, descript},results}}",
        dataList:[],
        formType:{},
        readonlyActorList:{},
        readonlyList:{},
        addNodeStatus:false,
        //上传文件信息
        attachmentList:[{}],
        newList:[],
        newFileName:'',
        newFileUrl:'',
        newUsername:localStorage.getItem("username"),
        newUserId:localStorage.getItem("userid"),
        newFileType:'',
        newUploadDate:'',
        newFileSize:'',
        newAttachmentId:1,
        isNewAddOnPage:true,
        aFile:[],
        pageUrl:'',
        showUrlPage:false,
	      num:'',
        itemArr:[],
        rowsNum:'',
        defaultLength:0,
        opFlowStatus:false,
        opFlowList:[],
      }
    },
    computed: {
      loadUrl() {
        const self = this;
        let url = self.pageUrl;
        console.log('url',url)
        return function (resolve) {
          require(['@/pages/'+url+'.vue'], resolve)
        };
      }
    },
    created() {
      // this.envItems.length = 1;
      // this.queryformByProcessId();
      this.findUser();
      //this.getAttachmentList();
    },
    methods: {
      isDisable(value){
        if (value<this.defaultLength){
          return true;
        }else {
          return false;
        }

      },
      getAttachmentList(){
        let processId = this.formData.processId;
        this.httpUtil.ajax({
          url: "wf/wf/attachment/getAttachmentList.json",
          params: {
            processId: processId
          },
          successAlert: false,
        }).then(res => {
          this.$nextTick(()=>{
            this.attachmentList = res.rows;
          })
        });
      },
      //上传失败操作
      onFileSubmitError(){
        this.$refs.uploadFileRef.doReset();
        this.$refs.fileSubmitBtn.setIconStyle(1, []);
      },
      saveNewAttachment(){
        let list = this.attachmentList;
        console.log('附件值',list)
        for (let i in list){
          if (list[i].isNewAddOnPage){
            this.aFile.push(list[i])
          }
        }
        let nFileRec = this.aFile;
        this.httpUtil
          .ajax({
            url: "wf/wf/attachment/saveFile.json",
            params: {
              recordList: JSON.stringify(nFileRec)
            }
          })
          .then(res => {
            console.log(res)
          });
      },
      deleteFile(index,row){
        this.attachmentList.splice(index,1)
        if (!this.isNewAddOnPage){
          let attachmentId = row.id;
          this.httpUtil.ajax({
            url: "wf/wf/attachment/deleteFile.json",
            params: {
              id: attachmentId
            }
          }).then(res => {
          });
        }
      },
      download(value){
        console.log(value)
        let fileName = value.originalFilename;
        this.httpUtil.download({
          url:"download/server/WorkflowServer/wf/attachment/downloadFile.json",
          params: value,
          callback: response => { }
        },fileName)
        return false
      },
      dealTime(timestamp){
        let date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
        var Y = date.getFullYear() + '-';
        var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1):date.getMonth()+1) + '-';
        var D = (date.getDate()< 10 ? '0'+date.getDate():date.getDate())+ ' ';
        var h = (date.getHours() < 10 ? '0'+date.getHours():date.getHours())+ ':';
        var m = (date.getMinutes() < 10 ? '0'+date.getMinutes():date.getMinutes()) + ':';
        var s = date.getSeconds() < 10 ? '0'+date.getSeconds():date.getSeconds();
        return Y+M+D+h+m+s;
      },
      //上传成功操作
      onFileSubmitSuccess(response,file,fileList) {
        if(this.attachmentList === undefined) {
          this.attachmentList = []
        }

        this.newFileName = response.name;
        this.newFileUrl = response.response.returndata.path;
        this.newFileSize = response.size;
        this.newFileType = '经办附件';
        this.newUploadDate = this.dealTime(Date.now());
        this.newList = {
          fileType:this.newFileType,
          originalFilename: this.newFileName,
          createDate:this.newUploadDate,
          username:this.newUsername,
          url:this.newFileUrl,
          isNewAddOnPage:this.isNewAddOnPage,
          fileSize:this.newFileSize,
          processId:this.formData.processId,
          uploader:this.newUserId
        }
        this.attachmentList.push(this.newList);

        this.$refs.uploadFileRef.doReset();
        this.$refs.fileForm.reset();
        this.$refs.uploadAttachmentPopup.close();
        //this.$refs.attachmentGrid.load();
      },
      //上传附件
      fileSubmitUploadParam(){
        var validate = this.$refs.fileForm.validate();
        if (validate) {
          let _formData = this.formData;
          this.$set(_formData,'fileType','审批附件')
          let temp = document.getElementsByClassName('upload-demo');
          let lis = temp[0].childNodes[1].childNodes.length;
          if(lis>0){
            this.$refs.uploadFileRef.upload(_formData);
          }else{
            Tools.alert("上传文件不能为空!","danger");
            return false;
          }
        }
        //location.reload();
      },
      //判断用户是否为当前上传附件人
      isShowButton(row){
        if (row.fileType=='3' && row.username==this.newUsername)
          return true;
        return false;
      },
      passDataSuccess(){
        this.$emit('submitClose', '1')
      },
      findUser(){
        this.httpUtil.comnQuery({
          action: 'User.getAllUser',
        }).then(data=> {
          this.readonlyActorList = data.rows
          console.log('点击查询',data)
        })
      },
      submitHandle(value) {

        console.log('获取参数',value)
        this.$set(value,'formUrl',this.formData.formUrl);
        let result = true;
        console.log('formUrl==>',this.formData.formUrl)
        let result2 = true;
        if (this.formData.formUrl!==undefined&&this.formData.formUrl!==''){
          result2 = this.$refs.urlForm.validateData();
        }
        result = this.$refs.prodGroupUserForm1.validate();
        if (!result||!result2){
          return false;
        }
        let form2s = this.$refs.prodGroupUserForm2;
        if (form2s && form2s.length > 0) {
          for (let i = 0; i < form2s.length; i++) {
            result = result && form2s[i].validate();
          }
        }
        if (result === false) {
          return false;
        }
        if(this.envItems.length<1){
          Tools.alert("未获取到信息，请稍后 !","danger")
          return false;
        }
        if (this.envItems && this.envItems.length > 0) {
          // console.log('获取参数11111',this.envItems)
          value.nodeList = JSON.stringify(this.envItems);
          // console.log("传递参数",value)
        }
        let formInfo = null;
        if (this.dataList && this.dataList.length > 0){
          formInfo = JSON.stringify(this.dataList)
        }
        console.log("formInfo",formInfo)
        let len = this.envItems.length
        let list = JSON.parse(value.nodeList);
        if (list.length > 1){
          let k = 0;
          for (let i = k; i < list.length; i++) {
            let nodeName = list[i].nodeName;
            k++
            console.log('i==>',k)
            for (let j = k; j < list.length; j++) {
              if (list[j].nodeName == nodeName){
                Tools.alert("节点名称不能重复", "danger");
                return false;
              }
            }
          }
        }
        //保存附件信息
        this.saveNewAttachment();
        //获取新生成的模板id
        // let processId = value[1]
        this.httpUtil.ajax({
          url:"/wf/businessProcess/startAndExecute.json" ,
          params: {server:value.server , nodeList: value.nodeList , appDisplay:value.appDisplay , actorId:value.readonlyActor , processId : value.processId , disNoticeId : this.disNoticeId ,
             formUrl:value.formUrl , formId : value.formId ,highestLevel : len , formInfo: formInfo , remark : value.remark ,displayName : value.displayName,isPush:value.isPush },
        }).then(data => {
          // console.log('执行参数',data)
          if (data.data == '新增流程成功'){
            this.passDataSuccess();
          }
        });
        return false;
      },


      deleteEvent(index) {
        index = this.num;
        console.log('获取行数',index)
        console.log('回显行数',this.rowsNum)
        if (index < this.rowsNum){
          Tools.alert("已配置流程节点无法删除","danger");
          return false;
        }
        if (this.envItems.length > 1) {
          this.envItems.splice(index, 1);
        }
      },
      deleteEventPopup(index){
        if (this.dataList.length > 1) {
          this.dataList.splice(index, 1);
        }
      },

      reloadGroupData(){
        this.httpUtil.ajax({
          url: 'wf/businessProcess/getAllProcess.json',
          params: {},
        }).then(data => {
          console.log('参数类型',data)
          this.flowTemplateList=data.data
        });
      },
    },
    watch:{
      fromData : function(value) {
        console.log("value===",value);
        let valueData = null;
        let array = [];
        let label = null;
        let json = null;
        let addNode = value[0].busnessProcess.addNode;
        let jsonList = value[0].processForm.json;
        if (addNode =='1'){
          this.addNodeStatus = true;
        }else{
          this.addNodeStatus = false;
        }
        this.formData.isPush = value[0].busnessProcess.isPush;
        // console.log('addNode',addNode);
        this.formData.nodeName = '抄送';
        this.formData.nodeType = '抄送';
        //this.envItems = value[0].nodeList;
        this.readonlyList =  value[0].readonlyActor;
        // console.log('赋值',this.readonlyList)
        this.formData.readonlyActor = value[0].readonlyActor;
        this.formData.server = value[0].busnessProcess.server;
        this.formData.formId = value[0].busnessProcess.formId;
        this.formData.processId = value[1];
        let formUrl = value[0].processForm.formUrl;
        this.pageUrl = formUrl;
        this.formData.formUrl = formUrl;
        this.fromData.id=value.processId;
        let formType = value[0].processForm.formType;
        let arrayList = value[3];
        // console.log("arrayList",arrayList);
        this.getAttachmentList();
        //页面回显字段
        if (arrayList){
          //仅针对产品清盘操作流
          if (arrayList[0]==='op-flow'){
            this.opFlowStatus = true;
            let flows = arrayList[1];
            let itemArr=[]
            arrayList.forEach(n=>{
              itemArr.push({
                name:Object.keys(n)[0],
                namekey:n[Object.keys(n)[0]]
              })
            })
            let formId = itemArr[1].name;//操作流表单id
            let keys = Object.keys(itemArr[1].namekey);
            let values = Object.values(itemArr[1].namekey);
            console.log(keys,values)
            this.httpUtil.comnQuery({
              action: 'OpFormParam.findOpFormParam',
              params: {
                formId: formId
              }
            }).then(res => {
              let rows = res.rows;
              for (let i = 0; i < rows.length; i++) {
                for (let j = 0; j < keys.length; j++) {
                  if (rows[i].paramCode==keys[i]){
                    this.$set(rows[i],'value',values[i]);
                    break;
                  }
                }
              }
              this.opFlowList = rows;
            })
          }
          if ( formType == '2' ){
            this.formData.dataName = formUrl;
            this.showUrlPage = true;
            this.state = true;
            this.states = true;
          }else if (formType == '1'){
            let obj = JSON.parse(value[0].processForm.json);
            aaa:for (let i = 0; i < obj.length; i++) {
              displayName = obj[i].displayName;
              for (let j = 0; j < arrayList.length; j++) {
                if (arrayList[j].child){
                  // console.log('displayName==',displayName);
                  label = arrayList[j].child.label;
                  // console.log('label==',label);
                  if (displayName == label){
                    valueData = arrayList[j].child.$children[0].dataValue;
                    let valueData1=arrayList[j].child.$children[0].$children[0].$data.query;
                    let keyData = arrayList[j].child.$children[0].value;
                    let valueData2 = arrayList[j].child.$children[0]._data.options;
                    if(valueData1){
                      valueData=valueData1;
                    }
                    if (valueData == undefined && valueData1 == undefined){
                      for (let k = 0; k < valueData2.length; k++) {
                        if (keyData == valueData2[k].itemkey){
                          valueData = valueData2[k].itemval;
                          // console.log('valueData==',valueData);
                        }
                      }
                    }
                    json = { FIELD_NAME: displayName, FIELD_VALUE: valueData};
                    array.push(json);
                    continue aaa;
                  }
                }
              }
              json = { FIELD_NAME: displayName};
              array.push(json);
            }
            this.dataList = array;

            this.state = true;
            this.showGroupName = true;
          }else if (formType == '3'){
            let obj = JSON.parse(JSON.parse(jsonList));
            // let list = JSON.parse(obj);
            console.log('obj==',obj)
            aaa:for (let i = 0; i < obj.length; i++) {
              displayName = obj[i].displayName;
              for (let j = 0; j < arrayList.length; j++) {
                if (arrayList[j].child){
                  // console.log('displayName==',displayName);
                  label = arrayList[j].child.label;
                  // console.log('label==',label);
                  if (displayName == label){
                    valueData = arrayList[j].child.$children[0].dataValue;
                    let valueData1=arrayList[j].child.$children[0].$children[0].$data.query;
                    let keyData = arrayList[j].child.$children[0].value;
                    let valueData2 = arrayList[j].child.$children[0]._data.options;
                    if(valueData1){
                      valueData=valueData1;
                    }
                    if (valueData == undefined && valueData1 == undefined){
                      for (let k = 0; k < valueData2.length; k++) {
                        if (keyData == valueData2[k].itemkey){
                          valueData = valueData2[k].itemval;
                        }
                      }
                    }
                    // console.log('valueData==',valueData);
                    json = { FIELD_NAME: displayName, FIELD_VALUE: valueData };
                    array.push(json);
                    continue aaa;
                  }
                }
              }
              json = { FIELD_NAME: displayName};
              array.push(json);

            }
            this.dataList = array;
            // console.log('this.dataList==',this.dataList)
            this.states = true;
            this.showGroupName = true;
          };
        }else {
          if (formType == '2'){
            this.formData.dataName = formUrl;
            this.showUrlPage = true;
            this.state = true;
            this.states = true;
          }else if (formType == '1'){
            let obj = JSON.parse(value[0].processForm.json);
            for (let i = 0; i < obj.length ; i++) {
              json = { FIELD_NAME: obj[i].displayName, FIELD_VALUE: valueData };
              array.push(json);
            }
            console.log('array+++++++++1',array);
            this.dataList = array;
            this.state = true;
            this.showGroupName = true;
          }else if (formType == '3'){
            let obj = JSON.parse(JSON.parse(jsonList));
            for (let j = 0; j < obj.length ; j++) {
              json = { FIELD_NAME: obj[j].displayName, FIELD_VALUE: valueData };
              array.push(json);
            }
            console.log('array+++++++++3',array);
            this.dataList = array;
            this.state = true;
            this.showGroupName = true;
          }
        }

        let displayName = value[0].busnessProcess.displayName;

        //处理节点审批人员
        //处理每个节点的默认审批人员（跟产品有关）
        let bindProd = value[0].busnessProcess.bindProd; //是否关联产品
        let nodeList = value[0].nodeList;
        let isNeedProd = '0';  //接口是否与产品有关
        let prodCode = '';
        //判断页面传入是否包含产品相关
        //let arrayList = this.fromData[3]; //前台传入

        if (arrayList){
          if(formType != '2') {
            for (let i = 0; i <arrayList.length; i++) {
              if (arrayList[i].child && arrayList[i].child.label == '产品代码'){
                isNeedProd = '1';
                prodCode = arrayList[i].child.$children[0].dataValue;
                break;
              }
            }
          } else {
            if (arrayList.prodCode) {
              isNeedProd = '1';
              prodCode = arrayList.prodCode;
            }
          }
        }


        console.log("bindProd",bindProd);
        console.log("isNeedProd",isNeedProd);
        console.log("prodCode",prodCode);
        //循环nodeList处理角色
        for(let j=0;j<nodeList.length;j++) {
          let node = nodeList[j]; //节点
          this.$set(node,'isNeedProd',isNeedProd);
          this.$set(node,'bindProd',bindProd);
          this.$set(node,'prodCode',prodCode);
        }
        let reList = new Array();

        this.httpUtil.ajax({
          url: "/wf/businessProcess/approvalPerson.json",
          params:{'json':JSON.stringify({'items':nodeList})},
          mask: false,
          successAlert: false,
        }).then(re =>{
          console.log("re",re);
          this.$nextTick(() =>{
            for (let i = 0; i < re.data.length; i++) {
              if (re.data[i].actorId == 'null')
                re.data[i].actorId = '';
              reList.push(re.data[i])
            }
            // console.log("reList",reList);
            this.envItems = reList;
            this.defaultLength = reList.length;
            console.log('默认的长度',reList.length)
            this.rowsNum = re.data.length;
          });
        });

        let name =  displayName.indexOf("{");
        let name1 =  displayName.indexOf("}");
        let name2 = displayName.substring(name+1,name1);
        console.log('name2',name2)
        let dataValue = [];
        if (arrayList){
          for (let i = 0; i < arrayList.length; i++) {
            if (arrayList[i].componentInstance){
              // console.log('arrayList[i].componentInstance',arrayList[i].componentInstance)
              dataValue = arrayList[i].componentInstance.$children[0].dataValue;
              // console.log('dataValue',dataValue)
              if (arrayList[i].child){
                // console.log('判断是否存在',arrayList[i].child)
                if (name2 == arrayList[i].child.label){
                  let displayName2 =  displayName.indexOf("}")
                  let displayName1 = displayName.substring(displayName2+1)
                  displayName1 = dataValue+displayName1+''
                  // console.log('截取',displayName2)
                  // console.log('替换参数',displayName1)
                  this.formData.displayName = displayName1
                  return false;
                }
                // }else{
                //   let displayName2 =  displayName.indexOf("}")
                //   let displayName1 = displayName.substring(displayName2+1)
                //   this.formData.displayName = displayName1
                //   return false;
                // }
              }
            }
          };
        }


        //let displayName = value[0].busnessProcess.displayName;
        let obj = value.formLabels;
        let name3 = displayName.substring(name1+1)
        if (name != -1){
          for (let i=0;i<obj.length;i++){
            let label = obj[i].label;
            if (name2 === label){
              var value = obj[i].$data.field.value;
              this.formData.displayName = value+name3;
              break;
            }else {
              this.formData.displayName = displayName;
            }
          }
          return false;
        }

        let displayName2 =  displayName.indexOf("}")
        let displayName1 = displayName.substring(displayName2+1)
        console.log('displayName1',displayName1)
        this.formData.displayName = displayName1
        return false;
      },
      'formData.readonlyActor':function (value) {
        console.log('监听抄送',value)

      },
      'item.actorId':function (data) {
        console.log('监听审批人员',data)
      },
      envItems:function (value) {
        console.log('监听行数',value.length)
        this.num = value.length - 1;

      }
    },
  }
</script>

<style scoped>

</style>
