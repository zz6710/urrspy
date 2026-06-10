<template>
  <div>

    <k-form-search-customize data-target="prodInfoGrid" v-model="queryParam">

      <k-form-item label="产品代码">
        <k-field-select v-model="queryParam.prodCode"  data-action="T8Dict.findTaProdInfos"
                        data-display-field="prodCode,prodName" data-value-field="prodCode" ></k-field-select>
      </k-form-item>
      <k-form-item label="产品名称">
        <k-field-text v-model="queryParam.prodName" data-validate-type="text"/>
      </k-form-item>
      <k-form-item label="产品登记编码">
        <k-field-text v-model="queryParam.registCode" ></k-field-text>
      </k-form-item>
      <k-form-item label="是否维护登记编码" data-input-width="194px" data-label-width="150px">
        <k-field-select v-model="queryParam.isHave" data-dict="t8_prod_isok"></k-field-select>
      </k-form-item>
    </k-form-search-customize>

    <k-grid ref="prodInfoGrid" data-action="T8ProdRegist.findT8ProdRegistCodeInfos1" @data-row-select="selectRow"
            data-operate-column-position="end" data-align="center" data-operate-data-width="200px" data-operate-column="true" :dataAutoload="false">
      <template slot="operate"  slot-scope="scope">
        <k-btn data-functype="POPUP" data-size="mini" data-target="editTable" data-descript="登记申报结果"
               class="md-info md-just-icon md-simple"  v-if="global.getProdIfUser(scope.row.row.id)&&
               global.isShowAuthorityButton('T8ProdRegist.updateT8ProdRegisCodeAndStatus')"
               v-show="showRegistResult">
          <md-icon>edit</md-icon>
        </k-btn>
        <k-btn  data-functype="POPUP" data-size="mini" data-target="annexPopup"  v-if="global.getProdIfUser(scope.row.row.id)&&
                global.isShowAuthorityButton('T8ProdRegist.uploadNotice')"
                class="md-info md-just-icon md-simple" data-descript="上传报备结果通知书" v-show="showUploadDoc">
          <md-icon >cloud_upload</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple"
               data-descript="下载附件" data-functype="POPUP" data-target="downListPOPUP" v-model="scope.row.row"
               v-show="showDownloadDoc">
          <md-icon>weekend</md-icon>
        </k-btn>
      </template>
      <k-grid-column data-header="产品代码" data-name="prodCode"/>
      <k-grid-column data-header="产品名称" data-name="prodName"/>
        <k-grid-column data-header="产品状态" data-dict="t8_prod_status" data-name="prodStatus"/>
      <k-grid-column data-header="产品子状态" data-dict="t8_prod_son_status" data-name="prodSonStatus"/>
      <k-grid-column data-header="产品登记编码" data-name="registCode"/>
       <k-grid-column data-header="募集方式" data-dict="t8_raise_type" data-name="raiseType" />
    </k-grid>
    <k-popup ref="editTable" data-title="登记申报结果">
      <AddComp v-model="formData"  :updSuccess="()=>{
        this.$refs.editTable.close();
        this.$refs.prodInfoGrid.load()
    }">
      </AddComp>
    </k-popup>
    <k-popup ref="annexPopup" data-title="上传申报结果通知书">
      <k-form ref="annexForm" :data-col="2">
        <k-form-item >
          <k-field-upload label="附件信息" data-type="file" ref="uploadonAnnexRef" :data-multiple="false" :data-limit=1
                          :data-error="onAnnexSubmitError" :dataChange="onUploadChange"
                          :dataHttpRequest="httpRequest" data-accept=".pdf"
                          :data-auto-upload="false">
          </k-field-upload>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-target="prodInfoGrid" ref="submitBtn"
                 data-from="minutesOfMeetingForm"  @click="submit">
            <span v-show="showSubmitBtn">确定</span>
            <i v-show="!showSubmitBtn" class="el-icon-loading"/>
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
    <k-popup ref="downListPOPUP" title="附件信息列表" data-width="60%" :data-dialog-drag="true"  @data-opened="loadAttachmentInfo">
      <k-grid ref="downloadAgencyAgreementGrid"  :data-autoload="false"
              data-action="DocumentAttachment.findAttachments" :dataPopupAppendToBody="true">
        <k-grid-column data-align="center" data-header="附件名称" data-name="fileName"/>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" :data-download-name="scope.row.row.fileName"
                 data-descript="下载" data-functype="DOWNLOAD" data-size="small"
                 data-url="/download/server/PmsApp/documentAttachment/downAttachment.json" v-model="scope.row.row">
            <md-icon>cloud_download</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </k-popup>
    <!--  模板上传
    <k-popup ref="addPopup" title="上传文档模板">
      <k-form ref="addForm" data-ui="element">
        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="picture" ref="uploadRef" :data-multiple="false" :data-limit=1
                          :data-error="onSubmitError" :data-success="onSubmitSuccess"
                          :data-auto-upload="false" data-upload-url="/upload/server/PmsApp/prod/registcode/comn-upload.json">
          </k-field-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="printTempGrid" ref="submitBtn"
                 data-from="addForm"  :data-handler="submitUploadParam">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
    -->
  </div>



</template>
<script>
import Tools from '@/utils/tools.js';
import { assign } from "lodash";
import AddComp from "./M81014Add"
export default {
  name: "M81014",
  components: {
    AddComp
  },
  data() {
    return {
      queryParam: {},
      formData: {},
      selectRowData: {},
      expands: [],
      prodCard: [],
      prodCode: "",
      prodMode: "",
      findValue:{
        findProdCode:"",
        findProdName:"",
        findProdMode:"",
        findProdLifecycle:"",
      },
      showSubmitBtn:true,
      fileData:'',
      showRegistResult:true,//是否显示登记申报结果按钮
      showUploadDoc:true,//是否显示上传报备结果通知书按钮
      showDownloadDoc:true,//是否显示下载附件按钮
    };
  },
  methods: {
    submit(){
      this.showSubmitBtn = false;
      this.fileData = new FormData();
      this.$refs.uploadonAnnexRef.upload();
      this.fileData.append('attachmentType', '8');
      this.fileData.append('parentId', this.selectRowData.prodCode);
      this.fileData.append('prodCode', this.selectRowData.prodCode);
      this.httpUtil.upload({
        url:"/upload-files/server/PmsApp/documentAttachment/commonUpload.json",
        formData: this.fileData
      }).then(res=>{
        this.showSubmitBtn = true;
        Tools.alert(res.data.returnmsg)
        this.onSubmitAnnexSuccess()
      })
    },
    onAnnexSubmitError() {
      this.$refs.uploadonAnnexRef.doReset();
      this.showSubmitBtn = true;
    },
    onSubmitAnnexSuccess() {
      this.$refs.uploadonAnnexRef.doReset();
      this.$refs.annexForm.reset();
      this.$refs.annexPopup.close();
      //this.$refs.supplementalAgencyAgreementGrid.load();
    },
    onUploadChange(file,fileList){
      this.fileList = fileList;
    },
    httpRequest(file){
      this.fileData.append('files', file.file);
    },
    selectProdMode(item){

      let pathUrl = '';
      if (item.url == "" || item.url == null){
        pathUrl = '/main/pms/M81/M81001add';
      }else {
        pathUrl = item.url;
      }

      this.$router.push({
        path: pathUrl,
        query: {prodMode: item.prodMode,findProdCode:'', findProdName:'', findProdMode:'', findProdLifecycle:'',},
      });

    },
    onSubmitError() {
      this.$refs.uploadRef.doReset();
      this.$refs.submitBtn.setIconStyle(1, [])
    },

    onSubmitSuccess() {
      this.$refs.uploadRef.doReset();
      this.$refs.addForm.reset();
      this.$refs.addPopup.close();
      this.$refs.prodInfoGrid.load();
    },
    submitUploadParam() {
      let formData = this.formData;
      this.$refs.uploadRef.upload(formData);
    },
    // 收取页面搜索框的值
    findData(){
      let valueFind = {} ;
      this.$refs.prodInfo.formList.map(item=>{
        if(item.field === 'prodCode'|| item.field === 'prodName'|| item.field === 'prodMode'||item.field === 'prodLifecycle')
        {
          valueFind[item.field] = item;
        }
      });
      this.findValue.findProdCode = valueFind.prodCode;
      this.findValue.findProdName = valueFind.prodName;
      this.findValue.findProdMode = valueFind.prodMode;
      this.findValue.findProdLifecycle = valueFind.prodLifecycle;
    },

    // 往get请求传 搜索框存下的值
    findDataBye(params){
      params.findProdCode = this.findValue.findProdCode;
      params.findProdName = this.findValue.findProdName;
      params.findProdMode = this.findValue.findProdMode;
      params.findProdLifecycle = this.findValue.findProdLifecycle;
      return params;
    },

    selectRow(row, column, event) {
      const _this = this
      _this.selectRowData = assign({}, row)
      _this.formData = assign({}, row)

    },
    loadAttachmentInfo(){
      this.$refs.downloadAgencyAgreementGrid.load({parentId: this.formData.prodCode, attachmentType: '8'})
    }
  },
  created() {
    //this.getProdMode();
    this.global.getProdUser('');
    let prodCodeLast = '';
    let prod_code = this.$route.query.prod_code;
    let prodCode = this.$route.query.prodCode;
    if (prod_code != '' && prod_code != undefined) {
      prodCodeLast = prod_code;
    } else if (prodCode != '' && prodCode != undefined) {
      prodCodeLast = prodCode;
    }
    this.$nextTick(() => {
      if (prodCodeLast != '' && prodCodeLast != undefined) {
        this.$refs.prodInfoGrid.load({
          prodCode: prodCodeLast,
        });
      }
      //获取需要隐藏按钮属性  rennannan 20210322   将接收到的属性值置为false达到隐藏按钮的效果
      this.global.getHideButtons(this);
      if (prodCodeLast != '' && prodCodeLast != undefined) {
        this.httpUtil.comnQuery({
          action: 'T8ProdRegist.findT8ProdRegistCodeInfos',
          params: {prodCode: prodCodeLast},
        }).then(data => {
          if (data.rows.length > 0) {
            this.$nextTick(() => {
              this.formData = data.rows[0];
              this.$refs.editTable.popup();
              this.$refs.prodInfoGrid.load({prodCode: prodCodeLast});
            })
          }
        });
      }
    });
  }
}

</script>

<style>
.el-icon-color{
  color: #FF8C00;
}
</style>
<style lang="scss" scoped>
::v-deep .dropdown-menu{
  margin-top: 10px;
  right: auto;
}
::v-deep .k-card{
  z-index:0;
}

.el-table__expanded-cell {
  background-color: #F9F9F9 !important;
}
.el-table__expanded-cell:hover{
  background-color: #F9F9F9 !important;
}

.tool{
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: -15px;
  margin-bottom: -20px;
  padding-top: 60px;
}

.row-tools{
  background: #FFFFFF;
  box-shadow: 0 8px 12px 0 rgba(0,0,0,0.06);
  border-radius: 24px;
  height: 45px;
  //display: inline-block;
  align-items: center;
  //margin-top: 20px;
  text-align: center;
  //margin-left: -690px;
  position: absolute;
  left: 0;
  margin-top: -45px;
  margin-left: 30px;
}
.tools-text{
  font-family: PingFangSC-Regular;
  font-size: 10px;
  letter-spacing: 0;
  /*font-weight: 500;*/
  /*    margin-top: 15px;*/
  color: #707E8F;
}
.tool-item{
  margin-left: 25px;
  margin-right: 25px;
  margin-top: -1px;
  float: left;
}
.step-tools{
  margin-top: 60px;
  margin-bottom: 20px;
  align-items: center;
  display: inline-block;
}
.tool-item .md-icon{
  width: 15px;
  height: 15px;
  margin-top: -1px;
  margin-bottom: 6px;
}
.test{
  width: 0;
  height: 0;
  border-top: 70px solid transparent;
  border-right: 140px solid #6bbf20;
  border-bottom: 70px solid transparent;
}
.steps{
  display: flex;
  flex-direction: row;
  align-items: center;
  overflow-x: auto;
}

.step{
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-left: 2px;
  padding-right: 4px;
}
.my-line{
  background-image: linear-gradient(90deg, #7FC7FF 0%, #35A7EF 100%);
  border-radius: 0 0 0 0;
  width: 156px;
  height: 6px;
  margin-top: 12px;
}
.my-content{
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: -12px;
}
.my-number-content{
  top:0;
  left:-15px ;
  text-align: center;
  display: inline-block;
  height: 18px;
  width: 18px;
  color: #ffffff;
  background-color: #b9b9b9;
  line-height: 18px;
  border-radius: 50%;
  text-align: center;
  /*  border:1px solid;*/
  background-color: #4CA7EE;

}
.my-number{
  font-family: Arial-BoldMT;
  font-size: 12px;
  color: #ffffff;
  letter-spacing: 0;
  z-index: 2;
  /*margin-top: -2px;*/
  font-weight : 500;
}
.my-title{
  font-weight: 500;
  color: #3B4858;
  margin-top: 4px;
  font-size: 14px;
  font-family: PingFangSC-Medium;
}
.my-desc{
  height: 24px;
  color: #999999;
  margin-top: 1px;
}
.last-step-content{
  display: flex;
  flex-direction: row;
}
.my-delta{
  height: 10px;
  width: 0px;
  position: absolute;
  margin-left: 155px;
  margin-top: 8px;
  border-bottom: 10px solid #4CA7EE;
  border-right: 16px solid transparent;
}
.back-line-content{
  margin-bottom: -18px;
  margin-left: -250px;
  margin-right: -450px;
}
.my-back-line{
  background: #EDEDED;
  border-radius: 0px 0px 0px 0px;
  width: 100%;
  height: 6px;
  margin-top: -25px;
}
.popover-container{
  display: flex;
  flex-direction: column;
  margin-left: 10px;
}
.template{
  display: flex;
  flex-direction: row;
  align-items: center;
}
.template-desc{
  display: flex;
  flex-direction: row;
  align-items: center;
}
.template-btn{
  display: flex;
  flex-direction: row;
  margin-left: 10px;
}

.module{
  border: 1px solid #41A0EB;
  margin-left: 10px;
  padding: 1px 15px;
  border-radius: 2px;
  color: #41A0EB;
}

.task{
  display: flex;
  flex-direction: row;
  margin-top: 20px;
}
.task-item{

  display: inline-block;
  max-width: 420px;
  border-radius: 2px;
  border-radius: 2px;
  //width: 100%;
  height: 203px;
  //margin: 0 auto;
  margin: 0 5px 0 5px;
  text-align: left;
}
.task-box{
  margin-left: 7.5px;
  margin-right: 7.5px;
  width: 89.4px;
  height: 23px;
  position: relative;
  display: inline-block;
  // display: flex;
  // flex-direction: column;
  // height: 200px;
  //box-shadow: 0px 0px 10px 0px rgba(0,0,0,0.14);
  border-radius: 2px;
  text-align: center;
  line-height: 25px;
  margin-top: 10px;
  background-repeat: no-repeat;
}
.task-desc{
  font-family: PingFangSC-Regular;
  font-size: 12px;
  color: #FFFFFF;
}
.el-popover .el-popper{
  top: 180px;
}

.tool-item:hover{
  cursor:pointer;
}

.tool-item:hover ::v-deep.md-icon svg >path{
  fill: #41A0EB;
}
.tool-item:hover span{
  color: #41A0EB;
}

.tool-disable,
.tool-disable:hover{
  cursor: default;
}

.tool-disable ::v-deep.md-icon svg >path,
.tool-disable:hover ::v-deep.md-icon svg >path{
  fill: #cccccc;
}
.tool-disable span,
.tool-disable:hover span{
  color: #cccccc;
}

.prodModeCursor :hover{
  cursor: pointer;
}

.el-icon-circle-plus:before{
  margin-left: -15px;
  padding-right: 10px;
  //margin-top: 10px;
  padding-top: 3px;
}

</style>
