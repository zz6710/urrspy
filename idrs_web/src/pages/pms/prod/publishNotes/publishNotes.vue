<template>
  <div>
    <k-form-search-customize data-target="prodInfoGrid" v-model="prodSearchParam">

      <k-form-item label="产品代码">
        <k-field-select v-model="prodSearchParam.prodCode" data-action="T8Dict.findTaProdInfos"
                        data-display-field="prodCode,prodName" data-value-field="prodCode"
                        @data-on-change="selectProd"/>
      </k-form-item>
      <k-form-item label="产品名称">
        <k-field-text v-model="prodSearchParam.prodName" data-validate-type="text"/>
      </k-form-item>
      <k-form-item label="产品状态">
        <k-field-select v-model="prodSearchParam.prodStatus" data-dict="t8_prod_status"></k-field-select>
      </k-form-item>
      <k-form-item label="是否存在用印扫描件" v-show="true" data-input-width="164px" data-label-width="180px">
        <k-field-select v-model="prodSearchParam.isHave" data-dict="t8_prod_isok"/>
      </k-form-item>
      <k-form-item label="代码回收">
        <k-field-select v-model="prodSearchParam.isRecycleCode" data-dict="1yes0no"></k-field-select>
      </k-form-item>
    </k-form-search-customize>

    <k-grid ref="prodInfoGrid" data-action="PublishNote.findProdPublishAgreement1" @data-row-select="selectRow">
      <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode"/>
      <k-grid-column data-align="center" data-header="产品名称" data-name="prodName"/>
      <k-grid-column data-align="center" data-header="文档类型" data-name="documentType" data-hidden="true"/>
      <k-grid-column data-align="center" data-header="产品状态" data-name="prodStatus" data-dict="t8_prod_status"/>

      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" data-descript="生成发行说明" :data-after-success="changeVersion"
               data-functype="SUBMIT" data-size="small"
               :data-disabled="scope.row.row.showCreateBtn==='1' || scope.row.row.t8SpareColumnOne ==='-1'"
               data-action="PublishNote.generatePublishNoteByProdCode" data-target="escrowAgreementGrid"
               v-model="scope.row.row.prodCode" :data-confirm="true"
               v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)&&
               global.isShowAuthorityButton('PublishNote.generatePublishNoteByProdCode')"
        >
          <md-icon>add_circle</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple" v-model="scope.row.row" data-descript="上传备案审批表"
               data-functype="POPUP" data-size="small" data-target="addPublish"
               :data-handler="addPublishHandler" v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)&&
                global.isShowAuthorityButton('PublishNote.uploadApprovalForm')"
               v-show="showUploadRecord">
          <md-icon>backup</md-icon>
        </k-btn>

        <k-btn class="md-info md-just-icon md-simple" :data-download-name="scope.row.row.prodName+'备案审批表'+'.pdf'"
               data-descript="下载传备案审批表" data-functype="DOWNLOAD" data-size="small"
               v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)"
               data-url="/download/server/PmsApp/publishNote/downAttachment.json" v-model="scope.row.row"
               v-show="showDownloadRecord">
          <md-icon>cloud_download</md-icon>
        </k-btn>


        <k-btn class="md-info md-just-icon md-simple" v-model="scope.row.row" data-descript="上传用印扫描件"
               :data-handler="addPublishHandler" data-functype="POPUP" data-size="small" data-target="addPopup"
               v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)&&
                global.isShowAuthorityButton('PublishNote.uploadScannedCopy')" v-show="showUploadSeal">
          <md-icon>backup</md-icon>
        </k-btn>


        <!--<k-btn class="md-info md-just-icon md-simple" :data-download-name="scope.row.row.prodName+'用印扫描件.pdf'" :data-disabled="scope.row.row.showUploadBtn==1"
              data-descript="下载用印扫描件" data-functype="DOWNLOAD" data-size="small"
              data-url="/download/server/PmsApp/publishNote/downloadSealApply.json" v-model="scope.row.row"
               v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)" v-show="showDownloadSeal">
         <md-icon>cloud_download</md-icon>
       </k-btn>-->
      </template>
    </k-grid>

    <k-grid ref="escrowAgreementGrid" @data-row-select="selectRow" :data-autoload="false"
            data-action="T8ProdDocumentVersion.findPublishNotesByProdCode">
      <k-grid-column data-align="center" data-header="版本id" data-name="id" data-hidden="true"/>
      <k-grid-column data-align="center" data-header="文件名" data-name="fileName" data-hidden="true"/>
      <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode"/>
      <k-grid-column data-align="center" data-header="产品名称" data-name="prodName"/>
      <k-grid-column data-align="center" data-header="文件名" data-name="fileName"/>
      <k-grid-column data-align="center" data-header="文档版本" data-name="version"/>
      <k-grid-column data-align="center" data-header="文件类型" data-name="documentName"/>
      <k-grid-column data-align="center" data-header="是否模板文档" data-dict="1yes0no" data-name="isTemplateFile"/>
      <k-grid-column data-align="center" data-header="用印审批状态" data-name="approveStatus" data-dict="t8_process_status"/>
      <k-grid-column data-align="center" data-header="用印状态" data-dict="t8_document_status" data-name="documentStatus"/>
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" :data-download-name="downFileName(scope.row.row)"
               data-descript="下载" data-functype="DOWNLOAD" data-size="small"
               data-url="/download/server/PmsApp/prodDocument/downloadOnlineEditT8ProdDocumentVersion.json"
               v-model="scope.row.row" v-show="downloadPublishNote">
          <md-icon>cloud_download</md-icon>
        </k-btn>

        <div style="display:inline-block;" v-show="checkVersion(scope.row.row.version)">
          <k-btn class="md-info md-just-icon md-simple" v-model="scope.row.row" data-descript="用印申请"
                 data-functype="POPUP" data-size="small"
                 @click="applicationData(scope.row.row)" v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)&&
                 global.isShowAuthorityButton('PublishNote.approvalAudiopinion')"
                 v-show="showApplySeal">
            <md-icon>create_new_folder</md-icon>
          </k-btn>
        </div>

        <!--        <div style="display:inline-block;" v-show="checkVersion(scope.row.row.version)">
                  <k-btn class="md-info md-just-icon md-simple"  v-model="scope.row.row" data-descript="上传用印扫描件"
                         :data-handler="addPublishHandler" data-functype="POPUP" data-size="small" data-target="addPopup"
                         v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)" v-show="showUploadSeal">
                    <md-icon>backup</md-icon>
                  </k-btn>
                </div>-->

        <!-- <k-btn class="md-info md-just-icon md-simple" :data-download-name="scope.row.row.prodName+'用印扫描件.pdf'"
               data-descript="下载用印扫描件" data-functype="DOWNLOAD" data-size="small"
               data-url="/download/server/PmsApp/publishNote/downloadSealApply.json" v-model="scope.row.row"
                v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)" v-show="showDownloadSeal">
          <md-icon>cloud_download</md-icon>
        </k-btn>-->
        <div style="display:inline-block;" v-show="checkVersion(scope.row.row.version)">
          <k-btn class="md-info md-just-icon md-simple" data-descript="在线预览"
                 :data-disabled="scope.row.row.isTemplateFile==='0'"
                 data-size="small" v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)"
                 @click="onlineEditHandler(scope.row.row)" data-functype="POPUP" data-target="onlineEditPopup"
                 v-model="scope.row.row" v-show="showOnline">
            <md-icon>zoom_in</md-icon>
          </k-btn>
        </div>

      </template>
    </k-grid>
    <!--在线预览-->
    <k-popup ref="onlineEditPopup" data-width="80%" :data-dialog-drag="true" style="margin-left:10%;">
      <div class="edit">
        <div class="word">
          <k-btn data-functype="SUBMIT" :data-handler="reset" class="btn-custom-primary"
                 data-form="setRoleForm">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <iframe name="onlineEdit" id="onlineEdit" :src="viewUrl"></iframe>
        </div>

      </div>
    </k-popup>
    <!--上传备案审批表-->
    <k-popup ref="addPublish" data-title="上传备案审批表">
      <k-form ref="addForm" data-ui="element">

        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit=1
                          :data-error="onSubmitError" :data-success="onSubmitSuccess"
                          :data-auto-upload="false" data-upload-url="/upload/server/PmsApp/publishNote/upload.json">
          </k-field-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" ref="submitBtn"
                 data-from="addForm" :data-model="publishData" :data-handler="submitUploadParam">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--上传用印扫描件-->
    <k-popup ref="addPopup" data-title="上传用印扫描件">
      <k-form ref="addForm" data-ui="element">

        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit=1
                          :data-error="onSubmitError" :data-success="onSubmitSuccess" data-accept=".pdf"
                          :data-auto-upload="false"
                          data-upload-url="/upload/server/PmsApp/publishNote/uploadSealApply.json">
          </k-field-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" ref="submitBtn"
                 data-from="addForm" :data-model="publishData" :data-handler="submitUploadParam">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--用印申请弹出框-->
    <k-popup ref="applicationPopup" data-title="用印申请">
      <k-form ref="applicationForm" :data-col="2" dataLabelWidth="150px" dataInputWidth="200px">
        <k-field-text v-model="application.t8ProdDocumentVersionId" v-show="false" :data-allowblank="false"/>
        <k-form-item label="用印件名称">
          <k-field-text v-model="application.printName" :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="用印发起日期">
          <k-field-date v-model="application.printDate" :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="用印部门">
          <k-field-select v-model="application.printOrg" data-action='Dept.find' data-display-field="deptname"
                          data-value-field="deptno" :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="用印人">
          <k-field-select v-model="application.printUser" data-action="User.findUsers" data-display-field="username"
                          data-value-field="userid"
                          :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="联系电话">
          <k-field-text v-model="application.phone" :data-max-length="11"
                        data-validate-type="int" data-type="int" :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="用印种类">
          <k-field-select v-model="application.printType" data-dict="t8_print_type" :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="发往单位">
          <k-field-text v-model="application.shipUnit" :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="用印性质">
          <k-field-select v-model="application.printProperties" data-dict="t8_print_properties"
                          :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="法律审查意见书编号">
          <k-field-text v-model="application.opinionNumber" :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="审批类型">
          <k-field-select v-model="application.approvalType" :data-allowblank="false" data-dict="t8_approval_type"/>
        </k-form-item>

        <k-form-item label="用印种类及个数详情" data-input-width="590px">
          <k-field-text v-model="application.typeNum" :data-allowblank="false" :data-max-length="2000" inputType="textarea" :rows="1"/>
        </k-form-item>

        <k-form-item label="用印事由" data-input-width="590px">
          <k-field-text v-model="application.printReason" :data-allowblank="false" :data-max-length="2000" inputType="textarea" :rows="1"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-handler="addAudiopinion"
                 data-from="applicationForm" :data-model="application">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

  </div>
</template>

<script>
import {assign} from "lodash";
import Tools from "@/utils/tools";

export default {
  name: "publishNotes",
  data() {
    return {
      prodSearchParam: {
        prodCode: ''
      },
      publishData: {
        prodCode: '',
        prodName: '',
        documentType: '',
        version: '',
        remark: '',
        docType: '',
        auditOpinion: '',
        opinionDate: '',
        auditor: '',
        currentWorkday: '',
        auditType: '',
        t8ProdDocumentVersionId: ''
      },
      selectRowData: {},
      onlineEditData: {},
      viewUrl: '',
      userid: '',
      queryList: [],
      opinionTitle: '',
      application: {},
      toDate: '',
      showGenerate: true,//是否显示生成申请发行说明按钮
      showUploadRecord: true,//是否显示上传备案审批表按钮
      showDownloadRecord: true,//是否显示下载备案审批表按钮
      downloadPublishNote: true,//是否显示下载发行说明按钮
      showApplySeal: true,//是否显示用印申请按钮
      showUploadSeal: true,//是否显示上传用印按钮
      showDownloadSeal: true,//是否显示下载用印扫描件按钮
      showOnline: true,//是否显示在线预览按钮
      lastVersion: '',
    }
  },

  created() {
    //用户组权限
    this.global.getProdUser('');
    this.$nextTick(() => {
      //获取需要隐藏按钮属性  rennannan 20210322   将接收到的属性值置为false达到隐藏按钮的效果
      this.global.getHideButtons(this);
      let prodCode = this.$route.query.prod_code;
      if (prodCode != '' && prodCode != undefined) {
        this.$refs.prodInfoGrid.load({prodCode: prodCode});
      }
    });
  },
  mounted() {
    //获取系统当前用户
    Tools.getLoginUser().then(res => {
      this.userid = res.userid;
    })
    //获取系统当前时间 阿新修改系统时间用sysDate获取
    this.httpUtil.sysDate().then(res => {
      if (res) {
        this.toDate = res;
        this.$set(this.publishData, 'opinionDate', res)
      }
    })
    window.addEventListener('message', (e) => {
      if (e.data.key) {
        let refName = e.data.key
        this.$refs[refName][0].focus()
      }
    })
  },
  methods: {
    selectProd(prodCode) {
      this.httpUtil.comnQuery({
        action: 'PublishNote.findProdPublishAgreement1',
        params: {
          prodCode: prodCode
        }
      }).then(data => {
        this.selectRowData.documentType=data.rows[0].documentType
        this.selectRowData.prodCode = prodCode
        this.tableLoad(this)
      });

    },
    changeVersion(row) {
     // this.$refs.prodInfoGrid.load();
      //console.log("data=>>>>>",row);
      this.httpUtil.comnQuery({
        action: 'T8ProdDocumentVersion.getNewestT8ProdDocumentVersion',
        params: {
          prodCode: row.returndata.prodCode,
          documentType: row.returndata.documentType
        }
      }).then(data => {
        this.$nextTick(() => {
          let version = data.rows[0].version;
          let array = version.split(".");
          let numbers = array[1];
          numbers = numbers - 1;
          this.lastVersion = array[0] + "." + numbers;
        })
      });
    },
    checkVersion(version) {
      if (version == this.lastVersion) {
        return true;
      } else {
        return false;
      }
    },
    //axin 反显数据
    applicationData(val) {
      this.$set(this.application, 't8ProdDocumentVersionId', val.id);
      this.$set(this.application, 'printName', val.prodName + '-' + val.prodCode + ' ' + '发行申请用印申请');
      this.$set(this.application, 'printDate', this.toDate);

      this.httpUtil.comnQuery({
        action: "User.findUsersForLogin",
        params: {},
        mask: false,
        successAlert: false
      }).then(data => {
        this.$nextTick(() => {
          this.httpUtil.comnQuery({
            action: "PublishNote.findApprovalInfo",
            params: {t8ProdDocumentVersionId: val.id},
            mask: false,
            successAlert: false
          }).then(res => {
            this.$nextTick(() => {
              if (res.rows.length > 0) {
                let result = res.rows[0];
                this.$set(this.application, 'phone', result.phone);
                this.$set(this.application, 'printType', result.printType);
                this.$set(this.application, 'typeNum', result.typeNum);
                this.$set(this.application, 'shipUnit', result.shipUnit);
                this.$set(this.application, 'printProperties', result.printProperties);
                this.$set(this.application, 'printReason', result.printReason);
                this.$set(this.application, 'opinionNumber', result.opinionNumber);
                this.$set(this.application, 'approvalType', result.approvalType);
              }
              let row = data.rows[0];
              this.$set(this.application, 'printUser', row.userid);
              this.$set(this.application, 'printOrg', row.deptno);
              //this.$set(this.application,'phone',row.mobileno);
              this.$refs.applicationPopup.popup();
            });
          });
        });
      });
    },
    /*返回下载的文件名称*/
    downFileName(rows) {
      if (rows.isTemplateFile === '0') {
        return rows.fileName
      } else {
        return rows.prodName +"产品"+ rows.documentName + rows.version + '.docx'
      }
    },
    addAudiopinion(params) {
      /*表单校验*/
      let validateResult = this.$refs.applicationForm.validate();
      if (validateResult) {
        //是否选择线下审批
        if (params.approvalType == 2) {
          this.saveAudiopinion(params);
        } else {
          // Tools.alert("提交中...");
          this.httpUtil.comnUpdate({
            action: "PublishNote.approvalAudiopinion",
            params: params,
            mask: false,
            successAlert: false
          }).then(data => {
            //Tools.alert("流程开启成功");
            //判断是否进入了审批流
            if (data.returndata != undefined && data.returndata.data != undefined && data.returndata.data.processInstanceId != undefined) {
              params.processInstanceId = data.returndata.data.processInstanceId;
              Tools.alert("流程开启成功");
            }
            this.saveAudiopinion(params);
          });
        }
        return true;
      }
    },
    saveAudiopinion(params) {
      this.httpUtil.comnUpdate({
        action: "PublishNote.addAudiopinion",
        params: params,
        mask: false,
        successAlert: false
      }).then(data => {
        // Tools.alert("提交成功");
        //关闭当前弹出窗口
        this.$refs.applicationPopup.close();
        this.tableLoad(this);
      });
    },
    selectRow(row, column, event) {
      const _this = this;
      _this.selectRowData = assign({}, row);
       var documentType='10006,20006,30006,40006,50006,60006,70006,10106';
      this.httpUtil.comnQuery({
        action: 'T8ProdDocumentVersion.getNewestT8ProdDocumentVersion',
        params: {
          prodCode: _this.selectRowData.prodCode,
          documentType: documentType
        }
      }).then(data => {
        this.$nextTick(() => {
          let version = data.rows[0].version;
          let array = version.split(".");
          let numbers = array[1];
          numbers = numbers - 1;
          //this.$set(this, "lastVersion", array[0]+"."+numbers),
          this.lastVersion = array[0] + "." + numbers;
          //this.tableLoad(_this);
          this.$refs.escrowAgreementGrid.load({
          prodCode: _this.selectRowData.prodCode,
          documentType: documentType
      });
        })
      });
    },
    tableLoad(_this) {
      this.$refs.escrowAgreementGrid.load({
        prodCode: _this.selectRowData.prodCode,
        documentType: _this.selectRowData.documentType
      });
    },
    submitUploadParam() {
      let formData = this.publishData;
      this.$refs.uploadRef.upload(formData);

    },
    onSubmitError() {
      this.$refs.uploadRef.doReset();
      this.$refs.submitBtn.setIconStyle(1, []);
    },
    onSubmitSuccess() {
      let formData = this.publishData;
      this.$refs.uploadRef.doReset();
      this.$refs.addForm.reset();
      this.$refs.addPopup.close();
      this.$refs.addPublish.close();
      //加载表格
      this.$refs.escrowAgreementGrid.load({prodCode: formData.prodCode, documentType: formData.documentType})
    },


    save() {
      this.httpUtil.ajax({
        url: '/server/form/PmsApp/onlineEdit/saveProdDocumentVersionWordValue.json',
        params: {onlineEditData: JSON.stringify(this.onlineEditData)},
        successAlert: true
      }).then(res => {
        this.$refs.onlineEditPopup.close();
        this.$refs.prodManualGrid.load()
      })
    },
    itemChange(value, key) {
      document.getElementById("onlineEdit").contentWindow.document.querySelectorAll("span[name='v_" + key + "']").forEach(item => {
        var reg = new RegExp("\n", "g");
        var reg1 = new RegExp(" ", "g");
        //换行符替换
        value = value.replace(reg, "<br/>");
        /*使用半角空格替换java的空格*/
        value = value.replace(reg1, "&ensp;");
        item.innerHTML = value
      })
    },

    addPublishHandler(data) {
      this.publishData = data;
      //console.log(this.publishData);
    },
    onlineEditHandler(value) {
      console.log(value);
      this.httpUtil.comnQuery({
        action: 'T8OnlineWordValue.preview',
        params: {
          t8ProdDocumentVersionId: value.id,
          prodCode: value.prodCode
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
                if (val != null  && val != 'null') {
                  //将java换行符替换成html换行符
                  //val = val.replaceAll("\n","<br/>");
                  /*使用半角空格替换java的空格*/
                  //val = val.replaceAll(" ", "&ensp;");
                  var reg = new RegExp("\n", "g");
                  var reg1 = new RegExp(" ", "g");
                  val = val.replace(reg, "<br/>");
                  val = val.replace(reg1, "&ensp;");
                  item.innerHTML = val;
                } 
              })
            }
          }, 3000)
        }
      });
    },
    reset() {
      this.$refs.onlineEditPopup.close();
    }

  }
}
</script>


<style lang="scss" scoped>
.edit {
  display: flex;
  flex-direction: row;
  width: 100%;
  height: 700px;

  .word {
    width: 100%;

    iframe {
      width: 100%;
      height: 100%;
    }
  }
}
</style>
