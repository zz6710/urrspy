<template>
  <div>
    <k-form-search-customize data-target="prodElementGrid" v-model="prodSearchParam">
      <k-form-item label="产品代码">
        <k-field-select v-model="prodSearchParam.prodCode" data-display-field="prodCode,prodName" data-value-field="prodCode" data-action="T8Dict.findTaProdInfos" :data-max-lenght="32"/>
      </k-form-item>
      <k-form-item label="产品名称">
        <k-field-text v-model="prodSearchParam.prodName" data-validate-type="text"/>
      </k-form-item>
      <k-form-item label="是否存在其他报备材料" data-input-width="164px" data-label-width="180px">
        <k-field-select v-model="prodSearchParam.isHave" data-dict="t8_prod_isok"></k-field-select>
      </k-form-item>
      <k-form-item label="代码回收">
        <k-field-select v-model="prodSearchParam.isRecycleCode" data-dict="1yes0no"></k-field-select>
      </k-form-item>
    </k-form-search-customize>
    <k-grid ref="prodElementGrid"  data-action="OtherFiling.findOhterFilingT8ProdInfos1" @data-row-select="selectRow">
      <k-grid-column data-hidden="true" data-align="center" data-header="id" data-name="id"/>
      <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode"/>
      <k-grid-column data-align="center" data-header="产品名称" data-name="prodName"/>
      <k-grid-column data-align="center" data-header="产品状态" data-name="prodStatus" data-dict="t8_prod_status"/>
      <k-grid-column data-align="center" data-header="文档类型" data-name="documentType" data-dict="t8_temp_type" data-hidden="true" />
      <k-grid-column data-align="center" data-header="其他材料确认" data-name="otherFilingStatus" data-hidden="true" />
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" data-descript="生成报告主体文件" data-functype="SUBMIT" data-size="small"
               data-action="OtherFiling.generateReportMainDoc" data-target="t8IssuanceRegisterGrid" :data-disabled="isDisabled(scope.row.row.t8SpareColumnOne)"
               v-model="scope.row.row" :data-confirm="true" v-if="global.getProdIfUser(scope.row.row.id)&&
               global.isShowAuthorityButton('OtherFiling.generateReportMainDoc')"
               v-show="showGenerateReport">
          <md-icon>add_circle</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple" data-descript="生成可行性报告文件" data-functype="SUBMIT" data-size="small"
               data-action="OtherFiling.generateFeasibilityReport" data-target="t8IssuanceRegisterGrid" :data-disabled="isDisabled(scope.row.row.t8SpareColumnTwo)"
               v-model="scope.row.row" :data-confirm="true" v-if="global.getProdIfUser(scope.row.row.id)&&
               global.isShowAuthorityButton('OtherFiling.generateFeasibilityReport')"
               v-show="showGenerateFeasi">
          <md-icon>add_circle</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple"  v-model="scope.row.row" data-descript="上传"
               data-functype="POPUP" data-size="small" @click="setFileParams(scope.row.row)" data-target="filePopup"
               v-if="global.isShowAuthorityButton('OtherFiling.upload')">
          <md-icon>backup</md-icon>
        </k-btn>
<!--        <k-btn class="md-info md-just-icon md-simple" data-descript="确认已报备" data-functype="SUBMIT" data-size="small"
               data-action="T8ProdInfo.updateT8ProdInfoStatusForOther" data-target="t8IssuanceRegisterGrid" v-if="global.getProdIfUser(scope.row.row.id)"
               :data-handler="beforeSubmit" :data-disabled="scope.row.row.otherFilingStatus!='2'" v-model="scope.row.row" :data-confirm="true">
          <md-icon>playlist_add_check</md-icon>
        </k-btn>-->

<!--        <k-btn class="md-info md-just-icon md-simple" data-descript="产品发行要素详情" data-functype="POPUP" data-size="mini"
               data-target="editT8ProdDeclaraPopup" v-if="global.getProdIfUser(scope.row.row.id)">
          <md-icon>library_books</md-icon>
        </k-btn>-->

      </template>
    </k-grid>
    <k-grid ref="t8IssuanceRegisterGrid" :data-autoload="false"  data-action="T8ProdDocumentVersion.findT8ProdDocumentVersionByProdCode2">
      <k-grid-column data-align="center" data-header="ID" data-name="id" data-hidden="true"/>
      <k-grid-column data-align="center" data-header="产品名称" data-name="prodName"/>
      <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode"/>
<!--      <k-grid-column data-align="center" data-header="文件名称" data-name="documentName"/>-->
      <k-grid-column data-align="center" data-header="文件类型" data-name="documentType" data-dict="t8_temp_type"/>
      <k-grid-column data-align="center" data-header="文档版本" data-name="version"/>
      <k-grid-column data-align="center" data-header="上传文件名" data-name="fileName"/>
      <k-grid-column data-align="center" data-header="是否模板文档" data-dict="1yes0no" data-name="isTemplateFile"/>
      <k-grid-column data-align="center" data-header="创建日期" data-name="createDate" data-render="renderDateTimeCreate"/>
      <k-grid-column data-align="center" data-header="创建人" data-name="createUserName"/>
      <k-grid-column data-align="center" data-header="确认状态" data-name="confirmStatus" data-hidden="true"/>
      <template slot="operate" slot-scope="scope">
<!--        <k-btn class="md-info md-just-icon md-simple" :data-handler="checkSeconds" data-functype="POPUP" data-size="mini"
               data-target="addT81004Popup" :data-disabled="scope.row.row.confirmStatus=='1'" v-model="scope.row.row"
               data-descript="确认文件信息">
          <md-icon>edit</md-icon>
        </k-btn>-->

          <k-btn class="md-info md-just-icon md-simple" data-descript="在线编辑"  :data-disabled="scope.row.row.isTemplateFile === '0'"
                 data-size="small" @click="onlineEditHandler(scope.row.row)"
                 data-functype="POPUP" data-target="onlineEditPopup" v-model="scope.row.row"
                 v-if="global.isShowAuthorityButton('OtherFiling.onlineEdit')">
            <md-icon>edit</md-icon>
          </k-btn>

        <k-btn class="md-info md-just-icon md-simple" data-descript="确认文件信息" data-functype="SUBMIT" data-size="small"
               data-action="OtherFiling.confirmReportDoc" data-target="t8IssuanceRegisterGrid"
               :data-disabled="scope.row.row.confirmStatus=='1'" v-model="scope.row.row"
               v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)&&
               global.isShowAuthorityButton('OtherFiling.confirmReportDoc')"
               v-show="showConfirm">
          <md-icon>playlist_add_check</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple" :data-disabled="scope.row.row.confirmStatus=='0'" :data-download-name="downFileName(scope.row.row)"
               data-descript="下载文件" data-functype="DOWNLOAD" data-size="small"
               data-url="/download/server/PmsApp/prodDocument/downloadOnlineEditT8ProdDocumentVersion.json" v-model="scope.row.row" v-show="showDownload">
          <md-icon>cloud_download</md-icon>
        </k-btn>
      </template>
    </k-grid>
    <!--上传补充代销协议-->
    <k-popup ref="filePopup" title="上传">
      <k-form ref="fileForm" data-ui="element">
        <k-form-item label="产品代码" v-show="false">
          <k-field-text v-model="filFormData.prodCode" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="产品代码" v-show="false">
          <k-field-text v-model="filFormData.prodName" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="是否模板文件" v-show="false">
          <k-field-text v-model="filFormData.isTemplateFile" data-default-value="0" />
        </k-form-item>
        <k-form-item label="文件类型" >
          <k-field-select v-model="filFormData.fileType" data-dict="t8_other_word" :data-allowblank="false" />
        </k-form-item>

        <k-form-item label="上传协议" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="fileUploadRef" :data-multiple="false" :data-limit=1
                          :data-error="onFileSubmitError" :data-success="onFileSubmitSuccess"
                          :data-auto-upload="false"  data-upload-url="/upload/server/PmsApp/otherFiling/upload.json">
          </k-field-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="printTempGrid" ref="fileSubmitBtn"
                 data-from="fileForm" :data-model="filFormData" :data-handler="fileSubmitUploadParam">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
    <!--    添加确认信息弹出框   -->
    <k-popup ref="addT81004Popup" >
      <k-form ref="addT81004Form" :data-col="2" >
        <span style="font-size: 18px;">确认完成后请</span><span style="color:red;font-size: 18px;">线下通知报备人员下载发行登记表并开始报备！！</span>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
          <input style="width: 90px;
                border-radius: 5px;
                background-color: #47A44B;
                color: white !important;
                border: 0px;" type="button" :disabled="countFlag"
                 v-model="btnMsg == null ? '确定('+countNum+'s)' : btnMsg"  @click="updateStatus">
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--在线编辑弹出框-->
    <k-popup ref="onlineEditPopup" data-width="90%"  :data-dialog-drag="true">
      <div class="edit">
        <div class="word">
          <iframe name="onlineEdit" id="onlineEdit" :src="viewUrl"></iframe>
        </div>
        <div class="form">
          <div>
            <k-btn data-functype="SUBMIT" :data-handler="save" class="btn-custom-primary"
                   data-form="setRoleForm">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn @click="closePopup" class="btn-custom-plain"
                   data-form="setRoleForm">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
            </k-btn>
          </div>
          <k-form ref="tableFormData" :data-col="1" style="width: 100%">
            <k-form-item v-for="(item,index) in onlineEditData" :key="index" :label="item.wordComment+':'">
              <k-field-text v-show="item.isDisabled=='1'"  style="width: 130%" inputType="textarea" :ref="item.wordKey" @input="itemChange($event,item.wordKey)" v-model="item.wordValue"/>
              <k-field-text v-show="item.isDisabled=='0'"  style="width: 130%" inputType="textarea" :rows="1" :data-disabled="true" :value="item.wordValue"/>
            </k-form-item>
          </k-form>
        </div>
      </div>
    </k-popup>
  </div>
</template>

<script>
import {assign} from "lodash";
import Tools from "@/utils/tools";
export default {
  name: "otherFiling",
  data() {
    return {
      rowData2:{
      },
      prodSearchParam: {
        prodName:''
      },
      filFormData:{
        prodCode:'',
        prodName:'',
        documentType:'',
        version:'',
        isTemplateFile:'',
        fileType: ''
      },
      // 倒计时周期
      countNum:10,
      // 用于倒计时标记，true-正在倒计时
      countFlag:false,
      // 定时器
      intervalBtn:{},
      //默认按钮的值
      btnMsg:"确定(",
      mobile:"",
      onlineEditData:{},
      viewUrl:'',
      showGenerateReport:true,//是否显示生成报告主文件按钮
      showGenerateFeasi:true,//是否显示生成可行性报告文件按钮
      showConfirm:true,//是否显示确认按钮
      showDownload:true,//是否显示下载按钮
    }
  },

  created() {
    this.global.getProdUser('');
    this.$nextTick(()=>{
      //获取需要隐藏按钮属性  rennannan 20210322   将接收到的属性值置为false达到隐藏按钮的效果
      this.global.getHideButtons(this);
      let prodCode = this.$route.query.prod_code;
      if(prodCode !=''&&prodCode!=undefined){
        this.$refs.prodElementGrid.load({prodCode:prodCode});
      }
    });
  },
  mounted() {
    window.addEventListener('message', (e)=>{
      if(e.data.key){
        let refName=e.data.key
        this.$refs[refName][0].focus()
      }
    })
  },
  methods: {
    renderDateTimeCreate(row) {
      return Tools.formatDateTime(row.createDate, row.createTime);
    },
    isDisabled(val) {
      /*判断是否禁用按钮*/
      if (val) {
        return false;
      }
      return true;
    },
    checkVersion(version) {
      if (version == this.lastVersion) {
        return true;
      }else{
        return false;
      }
    },
    showEditOnline(rows){
      console.log(rows)
    },
    downFileName(rows) {
      return rows.fileName
  /*    if (rows.isTemplateFile === '0') {
        return rows.fileName
      } else {
        return rows.prodName+"产品"+rows.documentName+rows.version+'.docx'
      }*/
    },
    setFileParams(rows) {
      this.filFormData.documentType = rows.documentType
      this.filFormData.prodCode = rows.prodCode
      this.filFormData.prodName = rows.prodName
      this.filFormData.distributorCode = rows.distributorCode
    },
    onFileSubmitError(){
      this.$refs.fileUploadRef.doReset();
      this.$refs.fileSubmitBtn.setIconStyle(1, []);
    },
    onFileSubmitSuccess() {
      this.$refs.fileUploadRef.doReset();
      this.$refs.fileForm.reset();
      this.$refs.filePopup.close();
    },
    fileSubmitUploadParam(){
      var validate = this.$refs.fileForm.validate();
      if (validate) {
        let formData = this.filFormData;
        let temp = document.getElementsByClassName('upload-demo');
        let lis = temp[0].childNodes[1].childNodes.length;
        if(lis>0){
          this.$refs.fileUploadRef.upload(formData);
        }else{
          Tools.alert("上传文件不能为空!","danger");
          return false;
        }
      }
    },
    itemChange(value,key){
      document.getElementById("onlineEdit").contentWindow.document.querySelectorAll("span[name='v_"+key+"']").forEach(item=>{
        var reg = new RegExp( "\n" , "g" );
        var reg1 = new RegExp( " " , "g" );
        //换行符替换
        value = value.replace(reg, "<br/>");
        /*使用半角空格替换java的空格*/
        value = value.replace(reg1, "&ensp;");
        item.innerHTML=value
      })
    },
    onlineEditHandler(value){
      this.httpUtil.comnQuery({
        action: 'T8OnlineWordValue.getT8OnlineWordValueList',
        params: {
          t8ProdDocumentVersionId: value.id,
        }
      }).then(data => {
        if (data.rows.length > 0) {
          this.viewUrl = data.rows[0].viewUrl;
          this.onlineEditData = data.rows;
          setTimeout(() => {
            for (let i = 0; i < this.onlineEditData.length; i++) {
              let data = this.onlineEditData[i];
              document.getElementById("onlineEdit").contentWindow.document.querySelectorAll("span[name='v_" + data.wordKey + "']").forEach(item => {
                var val = data.wordValue;
                if (val != null && val.trim() !='' && val != 'null'){
                  //将java换行符替换成html换行符
                  var reg = new RegExp( "\n" , "g" );
                  var reg1 = new RegExp( " " , "g" );
                  val = val.replace(reg, "<br/>");
                  val = val.replace(reg1, "&ensp;");
                  item.innerHTML = val;
                } else{
                  item.innerHTML = data.wordComment;
                }
              })
            }
          }, 3000)
        }
      });
    },
    save(){
      const _this = this
      this.httpUtil.ajax({
        url: '/server/form/PmsApp/onlineEdit/saveProdDocumentVersionWordValue.json',
        params: {onlineEditData: JSON.stringify(this.onlineEditData)},
        successAlert: true
      }).then(res=>{
        _this.$refs.onlineEditPopup.close();
        _this.$refs.t8IssuanceRegisterGrid.load({prodCode: _this.selectRowData.prodCode,documentType:_this.selectRowData.documentType});
      })
    },
    countDown(){
      this.countFlag=false;
      // 设置btn倒计时时显示的信息
      this.btnMsg = null;
      // 更改btn状态
      this.countFlag =! this.countFlag;
      //每次打开弹窗都重置定时器
      clearInterval(this.intervalBtn);
      this.countNum=10;
      // 设置倒计时
      this.intervalBtn = setInterval(() => {
        if(this.countNum <= 0) {
          // 重置btn提示信息
          this.btnMsg = "确定";
          // 清除定时器
          clearInterval(this.intervalBtn)
          // 更改btn状态
          this.countFlag =! this.countFlag;
          // 重置倒计时状态
          this.countNum = 10;
        }
        // 倒计时
        this.countNum -- ;
      }, 1000);
    },
    checkSeconds(val){
      this.rowData2 = {};
      this.rowData2 = val;
      //console.log("this.rowData2=>>>>>>>>",this.rowData2);
      //触发定时器方法
      this.countDown()
    },
    updateStatus(){
      //T8IssuanceRegister.updateStatus
      this.btnMsg="执行中...";
      this.countFlag = true;
      //console.log("this.rowData2=::::>>>>>>",this.rowData2);
      this.httpUtil.comnUpdate({
        action: 'T8ProdDocumentVersion.confirmT8ProdDocumentVersion2',
        params: this.rowData2,
        successAlert: true
      }).then(data => {
        this.countFlag = false;
        if(data.success==true){
          this.$refs.addT81004Popup.close();
          this.$refs.t8IssuanceRegisterGrid.load();
        }
      });
    },

    beforeSubmit(val){
      this.$set(val,'prodStatus','5');
      this.$set(val,'prodSonStatus','12');
      return val;
    },

    selectRow(row, column, event){
      const _this = this;
      _this.selectRowData = assign({}, row);
      this.formData = Object.assign({}, row)
      this.$refs.t8IssuanceRegisterGrid.load({prodCode: _this.selectRowData.prodCode,documentType: _this.selectRowData.documentType});
    },
    closePopup(){
      this.$refs.onlineEditPopup.close()
    }
  }
}
</script>

<style lang="scss" scoped>
  .edit{
    display: flex;
    flex-direction: row;
    width: 100%;
    height: 600px;
    .word{
      width: 70%;
      iframe{
        width: 100%;
        height: 100%;
      }
    }
    .form{
      padding-left: 20px;
      width: 40%;
      overflow-y:auto;
      .form-item{
        display: flex;
        align-items: center;
        margin-bottom: 10px;
        .form-item-span{
          margin-right: 5px;
          width: 100px;
          text-align: left;
        }
        .k-field-text{
          margin-left: 5px;
          width: 300px;
          height: 30px;
        }
      }
    }
  }
</style>
