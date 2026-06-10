<template>
  <div class="md-card k-card md-theme-default parent-div" style="height: 1500px;">
    <div class="form-item prod-panel" style="margin-top:50px;margin-left:80px;display: -webkit-box;">
    <k-form ref="editForm" :data-col="3" data-input-width="150px">

      <k-form-item label="公告标题" :data-col="3">
        <k-field-text v-model="formData.noticeTitle" :data-max-length="100" :dataAllowblank="false"/>
      </k-form-item>
      <k-form-item label="信披类型">
        <k-field-select v-model="formData.disclosureType" data-dict="xp_doc_other_type" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="信披子类型"
                   v-if="formData.disclosureType=='5'||formData.disclosureType=='6'||formData.disclosureType=='7'">
        <k-field-select v-model="formData.disclosureSonType" data-display-field="text" data-value-field="value"
                        :data-data="sonType" :data-disabled="true"/>
      </k-form-item>


      <k-form-item label="计划发布日期" :data-col="1">
        <k-field-date v-model="formData.planFbDate" data-type="date" style="width: 100%;"
                      :dataAllowblank="false" :data-disabled="true"/>
      </k-form-item>


      <k-form-item label="关联产品">

        <k-field-select v-model="formData.isConnectProd" data-dict="isConnectProd" style="width: 100%;"
                        :dataAllowblank="false" :data-disabled="true"/>
      </k-form-item>

      <k-form-item label="选择产品(单选)" :data-col="2" v-if="this.formData.isConnectProd=='1'">

        <k-field-select v-model="formData.prodCode" data-action="T8Dict.findTaProdInfos" style="width: 100%;"
                        data-display-field="prodCode,prodName" data-value-field="prodCode" :dataAllowblank="false"
                        @data-on-change="changeProd" :data-disabled="true"/>
      </k-form-item>

      <k-form-item label="产品经理"  v-if="this.formData.isConnectProd=='1'">
           <k-field-text v-model="formData.prodManager" :data-disabled="true"/>
      </k-form-item>
       <k-form-item label="投资经理"  v-if="this.formData.isConnectProd=='1'">
           <k-field-text v-model="formData.disclosureManager" :data-disabled="true"/>
      </k-form-item>
       <k-form-item label="估值经理"  v-if="this.formData.isConnectProd=='1'">
           <k-field-text v-model="formData.valuateManager" :data-disabled="true"/>
      </k-form-item>

      <k-form-item label="是否需要审批">
        <k-field-select v-model="formData.isNeedExamine" data-dict="1yes0no" :data-disabled="true"/>
      </k-form-item>

      <k-form-item label="是否自动发布">
        <k-field-select v-model="formData.isAutoSend" data-dict="1yes0no" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>

      <k-form-item label="信披公告说明" :data-col="3">
        <k-field-text v-model="formData.note" input-type="textarea" :data-max-length="500"/>
      </k-form-item>

      <k-form-item label="公告文件" :data-col="3">
<!--        <div slot="label" style="font-size:20px">-->
<!--          公告文件-->
<!--        </div>-->
        <div>
          <k-grid ref="fileGrid" @data-row-select="selectRow" :dataData="noticeFiles"
                  :data-params="{'t8DisclosureNoticeId':id}" data-operate-width="150px">
            <k-grid-column data-header="附件名称" data-name="fileName" data-width="400px"></k-grid-column>
            <k-grid-column data-header="版本号" data-name="version" ></k-grid-column>
            <k-grid-column data-header="文件类型" data-name="fileType" data-dict="manual_type"></k-grid-column>
            <k-grid-column data-header="路径" data-name="filePath" :data-hidden="true"></k-grid-column>
            <template slot="operate" slot-scope="props" >
             <k-btn class="md-info md-just-icon md-simple" :data-download-name="props.row.row.fileName"
                      data-functype="DOWNLOAD" data-size="small"
                     data-url="/download/server/PmsApp/M8DisclosureManual/download.json" v-model="props.row.row">
               <font color="#00bcd4" style="font-size: 12px;">下载</font>
              </k-btn>
              <k-btn class="md-info md-just-icon md-simple"
                       data-size="small"
                     @click="previewXP(props.row.row)" data-functype="POPUP">
                <font color="#00bcd4" style="font-size: 12px;">预览</font>
              </k-btn>
              <k-btn class="md-info md-just-icon md-simple" data-functype="POPUP"
                     data-size="small" data-target="uploadNoticePopup" @click="updateFile(props.row.row)">
                <font color="#00bcd4" style="font-size: 12px;">替换</font>
              </k-btn>

            </template>
          </k-grid>
        </div>
      </k-form-item>



      <k-form-item label="信披附件" :data-col="3">
<!--        <div slot="label" style="font-size:20px">-->
<!--          其他附件-->
<!--        </div>-->
        <div>
          <k-btn slot="button" style="width: 120px" data-functype="POPUP" class="btn-custom-primary"
                 data-target="addPopup">
            <md-icon>cloud_upload</md-icon>
            上传文件
          </k-btn>
          <k-grid ref="fileGrid" @data-row-select="selectRow" :dataData="otherFiles"
                  :data-params="{'t8DisclosureNoticeId':id}" data-operate-width="180px">
            <k-grid-column data-header="附件名称" data-name="fileName" data-width="400px"></k-grid-column>
            <k-grid-column data-header="版本号" data-name="version" ></k-grid-column>
            <k-grid-column data-header="文件类型" data-name="fileType" data-dict="manual_type"></k-grid-column>
            <k-grid-column data-header="路径" data-name="filePath" :data-hidden="true"></k-grid-column>
            <template slot="operate" slot-scope="props">
               <k-btn class="md-info md-just-icon md-simple" :data-download-name="props.row.row.fileName"
                     data-descript="" data-functype="DOWNLOAD" data-size="small"
                     data-url="/download/server/PmsApp/M8DisclosureManual/download.json" v-model="props.row.row">
                 <font color="#00bcd4" style="font-size: 12px;">下载</font>
              </k-btn>
              <k-btn class="md-info md-just-icon md-simple" data-size="small"
                     @click="previewXP(props.row.row)" data-functype="POPUP">
                <font color="#00bcd4" style="font-size: 12px;">预览</font>
              </k-btn>
              <k-btn class="md-info md-just-icon md-simple" data-functype="POPUP" data-size="small" data-target="uploadOtherPopup" @click="updateFile(props.row.row)">
                <font color="#00bcd4" style="font-size: 12px;">替换</font>
              </k-btn>
              <k-btn class="md-info md-just-icon md-simple" data-size="small" @click="deleteOtherFile(props.row.row)">
                <font color="#00bcd4" style="font-size: 12px;">删除</font>
              </k-btn>
            </template>
          </k-grid>
        </div>
      </k-form-item>


      <k-form-item label="信披渠道" :data-col="3">
<!--        <div slot="label" style="font-size:20px">-->
<!--          信披渠道-->
<!--        </div>-->
        <k-field-checkbox v-model="formData.channelCode" data-action="T8DisChannelInfo.findT8DisChannelInfoAll"
                          data-display-field="channelName" data-value-field="channelCode" :data-allowblank="false"></k-field-checkbox>
      </k-form-item>


      <k-form-footer data-align="center">
        <k-btn class="btn-custom-primary"
               :data-handler="updateManualNotice"
               data-from="editForm1" :data-model="formData"
               data-target="t8ObjectGrid">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>提交
        </k-btn>

        <k-btn class="btn-custom-plain" data-functype="CLOSE" @click="closePage">取消</k-btn>
      </k-form-footer>
    </k-form>
    <k-popup ref="addPopup" title="上传附件">
      <k-form ref="editForm2" data-ui="element">
        <k-form-item label="文件类型" :data-col="2">

          <k-field-select v-model="formData.fileType" data-dict="manual_type" style="width: 60%;"
                          :dataAllowblank="false" data-default-value="1" :data-disabled="true"/>
        </k-form-item>

        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="uploadRef" :data-multiple="true" :data-limit=10
                          :data-error="onSubmitError" :data-success="onSubmitSuccess"
                          data-upload-url="/upload/server/PmsApp/M8DisclosureManual/fileUpload.json">

          </k-field-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary"  data-from="editForm" @click="submitNotice">
                  <i v-show="!showSubmitBtn1" class="el-icon-loading"/>
                  <i v-show="showSubmitBtn1" class="icon-confirm"/>确定
          </k-btn>

          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="uploadNoticePopup" title="更新公告文件">
      <k-form ref="uploadForm2" data-ui="element">
        <k-form-item label="文件类型" :data-col="2">

          <k-field-select v-model="formData.fileType" data-dict="manual_type" style="width: 60%;"
                          :dataAllowblank="false" data-default-value="0" :data-disabled="true"/>
        </k-form-item>

        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="uploadRef1" :data-multiple="true" :data-limit=10
                          :data-error="onSubmitNoticeFileError" :data-success="onSubmitNoticeFileSuccess"
                          data-upload-url="/upload/server/PmsApp/M8DisclosureManual/fileUpload.json">

          </k-field-upload>
        </k-form-item>
        <k-form-footer data-align="center">
         <k-btn class="btn-custom-primary"  data-from="editForm" @click="submitNoticeFile">
                 <i v-show="!showSubmitBtn1" class="el-icon-loading"/>
                  <i v-show="showSubmitBtn1" class="icon-confirm"/>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
    <k-popup ref="onlineEditPopup" data-width="60%"  >
            <div class="edit">
              <div class="word">
                <iframe name="onlineEdit" id="onlineEdit" :src="viewUrl"></iframe>
              </div>
            </div>
    </k-popup>
    <k-popup ref="uploadOtherPopup" title="更新其他文件">
      <k-form ref="uploadForm2" data-ui="element">
        <k-form-item label="文件类型" :data-col="2">

          <k-field-select v-model="formData.fileType" data-dict="manual_type" style="width: 60%;"
                          :dataAllowblank="false" data-default-value="1" :data-disabled="true"/>
        </k-form-item>

        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="uploadRef2" :data-multiple="true" :data-limit=10
                          :data-error="onSubmitOtherFileError" :data-success="onSubmitOtherFileSuccess"
                          data-upload-url="/upload/server/PmsApp/M8DisclosureManual/fileUpload.json">

          </k-field-upload>
        </k-form-item>
        <k-form-footer data-align="center">
         <k-btn class="btn-custom-primary"  data-from="editForm" @click="submitOtherFile">
                  <i v-show="!showSubmitBtn1" class="el-icon-loading"/>
                  <i v-show="showSubmitBtn1" class="icon-confirm"/>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
  </div>
  </div>

</template>

<script>
import {assign} from "lodash";
import Tools from "@/utils/tools";

export default {
  name: "M8DisclosureManualEdit",
  data() {
    return {
      showSubmitBtn1:true,
       viewUrl:'',
      options: [
        /*{label:'中国光大银行官网',value:'1'},
        {label:'光大理财官网',value:'2'},
        {label:'光大银行销售渠道',value:'3'},
        {label:'光大理财直销渠道',value:'4'},
        {label:'行外代销机构',value:'5'},
        {label:'专户机构邮箱',value:'6'},*/
      ],
      prodSearchParam: {
        prodCode: '',
      },
      sonType: {},//子类型
      formData: {
        prodCode: '',
        prodName: '',
        feeJson: '',
        crtUser: '',
        startEstablishDate: '',
        sendEmail: '',
        note: '',

      },
      modelData: [
        {text: '1', value: "待提供数据"},
      ],
      fileData: {},
      fileList: {
        rows: [],
      },
       noticeFiles: {
        rows: [],
      },
      otherFiles: {
        rows: [],
      },
      submitList:[],//提交的文件集合
      updateRow:{}, //要更新的对象
    }
  },
  watch: {
    //监听信披类型
    'formData.disclosureType'(value) {
      //this.disclosureTypeChange(value);
    }
  },
  //  activated() {
  //   this.refreshPageParam();
  // },
  created() {
    this.id = this.$route.query.id;//公告id
    Tools.getLoginUser().then(res => {
      this.formData.crtUser = res.username;
      this.refreshPageParam();
    })

  },
  methods: {
     refreshPageParam() {

      this.$nextTick(() => {

        this.t8DisclosureNoticeId = this.id;

        if (this.id != '' && this.id != undefined) {
          this.httpUtil.comnQuery({
            action: 'DisclosureNotice.findDisclosureOtherNoticesAuth',
            params: {
              id: this.id,
            }
          }).then(data => {
            if (data.rows.length > 0) {
              this.disclosureTypeChange(data.rows[0].disclosureType);
              this.$set(this.formData, "id", data.rows[0].id);
              this.$set(this.formData, "t8ProdInfoId", data.rows[0].t8ProdInfoId);
              this.$set(this.formData, "t8DisclosureRuleId", data.rows[0].t8DisclosureRuleId);
              this.$set(this.formData, "prodName", data.rows[0].prodName);
              if (data.rows[0].prodName != '') {
                this.showProd = true;
              }
              this.$set(this.formData, "prodCode", data.rows[0].prodCode);
              this.$set(this.formData, "noticeTitle", data.rows[0].noticeTitle);
              this.$set(this.formData, "disclosureType", data.rows[0].disclosureType);
              this.$set(this.formData, "disclosureSonType", data.rows[0].disclosureSonType);

              this.$set(this.formData, "isConnectProd", data.rows[0].isConnectProd);
              this.$set(this.formData, "planFbDate", data.rows[0].planFbDate);
              var crtDate = Tools.formatDateTime(data.rows[0].crtDate, data.rows[0].crtTime);
              this.$set(this.formData, "crtDate", crtDate);
              this.$set(this.formData, "crtUserName", data.rows[0].crtUserName);
              this.$set(this.formData, "note", data.rows[0].note);
              this.$set(this.formData, "isAutoSend", data.rows[0].isAutoSend);
              this.$set(this.formData, "isNeedExamine", data.rows[0].isNeedExamine);
            }



            this.httpUtil.comnQuery({
              action: 'DisclosureNoticeChannel.findDisclosureNoticeChannelsByNoticeId',
              params: {

                disclosureNoticeId: this.id
              }
            }).then(data => {
              if (data.rows.length > 0)
                this.$set(this.formData, 'channelCode', data.rows[0].channelCode);
            });
             this.httpUtil.comnQuery({
                action:'M8DisclosureManual.findUserInfo',
                params:{prodCode:this.formData.prodCode}
              }).then(data =>{


                  this.$set(this.formData,'prodManager',data.rows[0].prodManager);

                  this.$set(this.formData,'valuateManager',data.rows[0].valuateManager);
                  this.$set(this.formData,'disclosureManager',data.rows[0].disclosureManager);

              });

               this.httpUtil.comnQuery({
                action:'M8DisclosureManual.qureyManualList',
                params:{t8DisclosureNoticeId:this.id}
              }).then(data =>{

                if (data.rows.length > 0) {
                  for (var i=0;i<data.rows.length;i++){
                    if(data.rows[i].fileType=='0'){
                      this.noticeFiles.rows.push(data.rows[i]);
                    }else{
                      this.otherFiles.rows.push(data.rows[i]);
                    }

                    this.submitList.push(data.rows[i]);
                  }

                }

              });

          });

        }
        ;
      });

    },
    closePage() {
      Tools.closeCurrentWindow(this);
      this.$router.push({
        path: '/main/pms/basePublish/M8DisclosureOtherNotice/M8DisclosureOtherNotice',
        query: {},
      });
    },

    disclosureTypeChange(value) {
      if (value == '5' || value == '6' || value == '7') {
        this.$set(this.formData, 'disclosureSonType', '');
        this.noticeTitles = {};
        this.httpUtil.comnQuery({
          action: "DisclosureMod.getXPTypeByDocType",
          params: {disclosureType: value}
        }).then(data => {
          this.sonType = data.rows;
        }).catch({})
      }
    },
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.fileData = Object.assign({}, row)
    },
    submitNotice() {
      this.showSubmitBtn1 = false;
      let uploadDatas = this.uploadData;
      this.showSubmitBtn = false;
      this.fileData = new FormData();
      this.$refs.uploadRef.upload(this.formData);
      this.showSubmitBtn1 = true;
    },
    submitNoticeFile() {
      this.showSubmitBtn1 = false;
      this.showSubmitBtn = false;
      this.fileData = new FormData();
      this.$refs.uploadRef1.upload(this.formData);
      this.showSubmitBtn1 = true;
    },
    submitOtherFile() {
      this.showSubmitBtn1 = false;
      this.showSubmitBtn = false;
      this.fileData = new FormData();
      this.$refs.uploadRef2.upload(this.formData);
      this.showSubmitBtn1 = true;
    },
    beforeUpload() {
      return false;
    },

     onSubmitError() {
      this.$refs.uploadRef1.doReset();
      this.$refs.submitBtn.setIconStyle(1, [])
    },
    onSubmitNoticeFileError() {
      this.$refs.uploadRef1.doReset();
      this.$refs.submitBtn.setIconStyle(1, [])
    },
    onSubmitOtherFileError() {
      this.$refs.uploadRef2.doReset();
      this.$refs.submitBtn.setIconStyle(1, [])
    },
    onSubmitSuccess(value) {

        this.$refs.uploadRef.doReset();

        this.$refs.addPopup.close();

        this.otherFiles.rows.push(value.response.returndata);
        this.submitList.push(value.response.returndata);

        this.$refs.fileGrid.load();


    },
    onSubmitNoticeFileSuccess(value) {
      this.$refs.uploadRef1.doReset();

      this.$refs.uploadNoticePopup.close();

      let result = value.response.returndata;
      var hasChangeFileName = "";
      for (var i = 0; i < this.noticeFiles.rows.length; i++) {
        if (this.noticeFiles.rows[i].id == this.updateRow.id) {
          hasChangeFileName = this.noticeFiles.rows[i].fileName;
          //更新列表List数据
          this.$set(this.noticeFiles.rows[i],'fileName',result.fileName);
          this.$set(this.noticeFiles.rows[i],'filePath',result.filePath);
          this.$set(this.noticeFiles.rows[i],'fileType',result.fileType);
          break;
        }

      }

       for (var i = 0; i < this.submitList.length; i++) {
        if (this.submitList.fileName == hasChangeFileName) {
          //更新提交List数据
          this.$set(this.submitList[i],'fileName',result.fileName);
          this.$set(this.submitList[i],'filePath',result.filePath);
          this.$set(this.submitList[i],'fileType',result.fileType);
          break;
        }

      }

      this.$refs.fileGrid.load();

    },


    onSubmitOtherFileSuccess(value) {
      console.log("updateRow",this.updateRow);
        console.log("this.otherFiles.rows",this.otherFiles.rows);
      this.$refs.uploadRef2.doReset();

      this.$refs.uploadOtherPopup.close();

      let result = value.response.returndata;
       var hasChangeFileName = "";
      for (var i = 0; i < this.otherFiles.rows.length; i++) {
        if (this.otherFiles.rows[i].fileName == this.updateRow.fileName ) {
          hasChangeFileName = this.otherFiles.rows[i].fileName;
          //更新列表List数据
          this.$set(this.otherFiles.rows[i],'fileName',result.fileName);
          this.$set(this.otherFiles.rows[i],'filePath',result.filePath);
          this.$set(this.otherFiles.rows[i],'fileType',result.fileType);
          break;
        }

      }

        console.log("更新后this.otherFiles.rows",this.otherFiles.rows);

        console.log("更新前this.submitList",this.submitList);
        for (var i = 0; i < this.submitList.length; i++) {
            let item = this.submitList[i];
            if (item.fileName == hasChangeFileName && (item.flag == null || item.flag == '' || item.flag == undefined)) {
                //更新提交List数据
                this.$set(this.submitList[i], 'fileName', result.fileName);
                this.$set(this.submitList[i], 'filePath', result.filePath);
                this.$set(this.submitList[i], 'fileType', result.fileType);
                break;
            }

        }
        console.log("更新后this.submitList",this.submitList);

      this.$refs.fileGrid.load();

    },

    updateFile(row) {
      console.log("row",row);
       this.updateRow = row;
      console.log("updateRow", this.updateRow);

    },

    updateManualNotice(params) {
      var flag = this.$refs.editForm.validate();
      if (flag == false) {
        return false;
      }

      this.$set(this.formData, 'fileList', JSON.stringify(this.submitList));

      this.httpUtil.comnUpdate({
        action: 'M8DisclosureManual.updateManualInfo',
        params: this.formData,
        successAlert: true,
      }).then(data => {
        Tools.closeCurrentWindow(this);
        this.$router.push({
          path: '/main/pms/basePublish/M8DisclosureOtherNotice/M8DisclosureOtherNotice',
          query: {},
        });
      });

    },
    changeProd() {
      this.$set(this.formData,'channelCode','');
      this.httpUtil.comnQuery({
        action:'T8DisChannelInfo.findProdNoticeChannel',
        params:{disclosureType: this.formData.disclosureType,disclosureSonType:this.formData.disclosureSonType,
                prodCode:this.formData.prodCode}
      }).then(data =>{
          if (data.rows.length >0)
            this.$set(this.formData,'channelCode',data.rows[0].channelCode);

      });
       this.httpUtil.comnQuery({
        action:'M8DisclosureManual.findUserInfo',
        params:{prodCode:this.formData.prodCode}
      }).then(data =>{


           this.$set(this.formData,'prodManager',data.rows[0].prodManager);

           this.$set(this.formData,'valuateManager',data.rows[0].valuateManager);
           this.$set(this.formData,'investManager',data.rows[0].investManager);

      });
    },



     //删除其他文件
    deleteOtherFile(row) {
       this.$confirm('确定要删除文件吗?', '提示', {}).then(confirm => {
        if (confirm) {
          for (var i = 0; i < this.otherFiles.rows.length; i++) {
            if (this.otherFiles.rows[i].fileName == row.fileName) {
              //展示数组中去除
              this.otherFiles.rows.splice(i, 1);

              break;
            }
          }


           for (var i = 0; i < this.submitList.length; i++) {
            if (this.submitList[i].fileName == row.fileName) {
                if (this.submitList[i].flag == '1')  //数据库中的数据
                    this.$set(this.submitList[i],'status','0');
                else
                    this.submitList.splice(i, 1);
              //提交数组中修改状态
              break;
            }
          }
        }
      }).catch(error => {
        console.log('取消提交')
      })


    },
    previewXP(row){

       this.$refs.onlineEditPopup.popup();


       this.httpUtil.comnQuery({
          action: 'M8DisclosureManual.previewXP',
          params: {"filePath":row.filePath,
                   "fileName":row.fileName}
        }).then(data => {
          this.viewUrl = data.returndata.viewUrl;
        });



    },
  }
}
</script>

<style lang="scss" scoped>
  @import "../../../styles/T81001.scss";

  .editNotice {
    background-color: #FFF;
    padding-top: 30px;
    padding-left: 20px;
    border-radius: 10px;
  }

  .edit {
    display: flex;
    flex-direction: row;
    width: 100%;
    height: 700px;

    .word {
      width: 97%;
      iframe {
        width: 100%;
        height: 100%;
      }
    }
  }

</style>
