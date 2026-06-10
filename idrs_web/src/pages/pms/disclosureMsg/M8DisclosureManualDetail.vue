<template>
  <div class="md-card k-card md-theme-default parent-div" style="height: 1500px;">
    <div class="form-item prod-panel" style="margin-top:50px;margin-left:80px;display: -webkit-box;">
    <k-form ref="detailForm" :data-col="3" data-input-width="150px">
      <k-form-item label="id" v-show="false">
        <k-field-text v-model="formData.id" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="公告标题" :data-col="3">
        <k-field-text v-model="formData.noticeTitle" :data-max-length="100" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="信披类型">
        <k-field-select v-model="formData.disclosureType" data-dict="xp_doc_other_type" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="信披子类型"
                   v-if="formData.disclosureType=='5'||formData.disclosureType=='6'||formData.disclosureType=='7'">
        <k-field-select v-model="formData.disclosureSonType" data-display-field="label" data-value-field="value"
                        :data-data="sonType" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="计划发布日期">
        <k-field-date v-model="formData.planFbDate" data-type="date" style="width: 100%;"
                      :data-disabled="true"/>
      </k-form-item>

      <k-form-item label="是否需要审批">
        <k-field-select v-model="formData.isNeedExamine" data-dict="1yes0no" :data-disabled="true" />
      </k-form-item>

      <k-form-item label="关联产品">

        <k-field-select v-model="formData.isConnectProd" data-dict="isConnectProd" style="width: 100%;"
                        :dataAllowblank="false" :data-disabled="true"/>
      </k-form-item>


      <k-form-item label="产品代码" v-if="this.showProd" :data-col="2">
        <k-field-select v-model="formData.prodCode" data-action="T8Dict.findTaProdInfos" style="width: 100%;"
                        data-display-field="prodCode,prodName" data-value-field="prodCode" :dataAllowblank="false"
                       :data-disabled="true"/>
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

      <k-form-item label="是否自动发布">
        <k-field-select v-model="formData.isAutoSend" data-dict="1yes0no" :data-disabled="true"/>
      </k-form-item>

      <k-form-item label="信披公告说明" :data-col="3">
        <k-field-text v-model="formData.note" input-type="textarea" :data-max-length="500" :data-disabled="true"/>
      </k-form-item>

      <k-form-item label="创建人">
        <k-field-text v-model="formData.crtUserName" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="创建时间">
        <k-field-text v-model="formData.crtDate" :data-disabled="true"/>
      </k-form-item>


      <k-form-item label="公告文件" :data-col="3">
<!--        <div slot="label" style="font-size:20px">-->
<!--          公告文件-->
<!--        </div>-->

        <div>

          <k-grid ref="fileGrid" @data-row-select="selectRow" data-action="M8DisclosureManual.qureyManualList"
                  :data-params="{'t8DisclosureNoticeId':id,'fileType':'0'}"
                  data-operate-width="120px">
            <k-grid-column data-header="附件名称" data-name="fileName" data-width="450px"></k-grid-column>
            <k-grid-column data-header="版本号" data-name="version" ></k-grid-column>
            <k-grid-column data-header="文件类型" data-name="fileType" data-dict="manual_type"></k-grid-column>
            <k-grid-column data-header="路径" data-name="filePath" :data-hidden="true"></k-grid-column>
            <template slot="operate" slot-scope="props">
              <k-btn class="md-info md-just-icon md-simple" :data-download-name="props.row.row.fileName"
                     data-functype="DOWNLOAD" data-size="small"
                     data-url="/download/server/PmsApp/M8DisclosureManual/download.json" v-model="props.row.row">
                <font color="#00bcd4" style="font-size: 12px;">下载</font>
              </k-btn>
              <k-btn class="md-info md-just-icon md-simple" data-size="small"
                     @click="previewXP(props.row.row)" data-functype="POPUP">
                <font color="#00bcd4" style="font-size: 12px;">预览</font>
              </k-btn>
            </template>
          </k-grid>
        </div>
      </k-form-item>


      <k-form-item label="信披附件" :data-col="3">
<!--        <div slot="label" style="font-size:20px">-->
<!--          信披附件-->
<!--        </div>-->
        <div>

          <k-grid ref="fileGrid" @data-row-select="selectRow" data-action="M8DisclosureManual.qureyManualList"
                  :data-params="{'t8DisclosureNoticeId':id,'fileType':'1'}"
                  data-operate-width="120px">
            <k-grid-column data-header="附件名称" data-name="fileName" data-width="450px"></k-grid-column>
            <k-grid-column data-header="版本号" data-name="version" ></k-grid-column>
            <k-grid-column data-header="文件类型" data-name="fileType" data-dict="manual_type"></k-grid-column>
            <k-grid-column data-header="路径" data-name="filePath" :data-hidden="true"></k-grid-column>
            <template slot="operate" slot-scope="props">
              <k-btn class="md-info md-just-icon md-simple" :data-download-name="props.row.row.fileName"
                     data-functype="DOWNLOAD" data-size="small"
                     data-url="/download/server/PmsApp/M8DisclosureManual/download.json" v-model="props.row.row">
                <font color="#00bcd4" style="font-size: 12px;">下载</font>
              </k-btn>
              <k-btn class="md-info md-just-icon md-simple" data-size="small"
                     @click="previewXP(props.row.row)" data-functype="POPUP">
                <font color="#00bcd4" style="font-size: 12px;">预览</font>
              </k-btn>
            </template>
          </k-grid>
        </div>
      </k-form-item>



      <k-form-item label="信披渠道" :data-col="3">
<!--        <div slot="label" style="font-size:20px">-->
<!--          信披渠道-->
<!--        </div>-->
          <k-field-checkbox v-model="formData.channelCode" data-action="T8DisChannelInfo.findT8DisChannelInfoAll" :data-disabled="true"
                            data-display-field="channelName" data-value-field="channelCode" ></k-field-checkbox>
      </k-form-item>

      <k-form-footer data-align="center">

        <k-btn style="width:100px;" class="btn-custom-primary" :data-model="formData"
               @click="sendNotice(formData)" v-if="global.isShowAuthorityButton('DisclosureNotice.sendNotice')">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>公告发布
        </k-btn>
        <k-btn style="width:100px;" class="btn-custom-primary" :data-model="formData"
               @click=" cancleNotice(formData)" v-if="global.isShowAuthorityButton('DisclosureNotice.cancleNotice')">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>公告取消
        </k-btn>
         <k-btn class="btn-custom-plain" data-functype="CLOSE" @click="closePage">关闭</k-btn>
      </k-form-footer>
    </k-form>

     <k-popup ref="onlineEditPopup" data-width="60%"  >
            <div class="edit">
              <div class="word">
                <iframe name="onlineEdit" id="onlineEdit" :src="viewUrl"></iframe>
              </div>
            </div>
    </k-popup>

  </div>
  </div>


</template>

<script>
import {assign} from "lodash";
import Tools from "@/utils/tools";

export default {
  name: "M8DisclosureManualDetail",
  data() {
    return {
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
      envItems: [],//渠道展示数组
      channelList: [],
      typeList: [],
      noticeChannel: [],
      updateChannel: false,
      exclusiveList: [{}], //专户数组
      fileData: {},
      id: '',
      showProd: false
    }
  },
  activated() {
    this.refreshPageParam();
  },
  created() {
    this.id = this.$route.query.id;//公告id
    Tools.getLoginUser().then(res => {
      this.formData.crtUser = res.username;

    })
    this.refreshPageParam();
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

              //  this.httpUtil.comnQuery({
              //   action:'M8DisclosureManual.qureyManualList',
              //   params:{t8DisclosureNoticeId:this.id}
              // }).then(data =>{

              //   if (data.rows.length > 0) {
              //     for (var i=0;i<data.rows.length;i++){

              //       this.submitList.push(data.rows[i]);
              //     }

              //   }

              // });

          });

        }
        ;
      });

    },
    sendNotice(row) {
      let disclosureNotice = this.formData;
      Tools.confirm(()=>{
        this.httpUtil.comnUpdate({
           action: "DisclosureNotice.sendOtherNotice",
           params: {"id": disclosureNotice.id, "noticeTitle": disclosureNotice.noticeTitle},
           mask: false,
           successAlert: true,
         }).then(data =>{
           this.showSubmitBtn = true;
        })
      },disclosureNotice.prodCode+"产品是否需要公告发布？？")
    },
 closePage() {
      Tools.closeCurrentWindow(this);
      this.$router.push({
        path: '/main/pms/basePublish/M8DisclosureOtherNotice/M8DisclosureOtherNotice',
        query: {},
      });
    },
    cancleNotice(row) {
      let disclosureNotice = this.formData;
      Tools.confirm(()=>{
      this.httpUtil.comnUpdate({
        action: "DisclosureNotice.cancleOtherNotice",
        params: {"id": disclosureNotice.id, "prodCode": disclosureNotice.prodCode},
        mask: false,
        successAlert: true,
      }).then(data => {
        this.showSubmitBtn = true;
      })
      },'请确认公告是否取消？')
    },
    disclosureTypeChange(value) {
      if (value == '5' || value == '6' || value == '7') {
        this.$set(this.formData, 'disclosureSonType', '');
        this.noticeTitles = {};
        this.httpUtil.comnQuery({
          action: "DisclosureRule.getDisclosureSonType",
          params: {disclosureType: value}
        }).then(data => {
          this.sonType = data.rows;
          console.log("this.sonType",this.sonType);
        }).catch({})
      }
    },
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.fileData = Object.assign({}, row)
    },
    submitNotice() {
      let uploadDatas = this.uploadData;
      this.showSubmitBtn = false;
      this.fileData = new FormData();
      this.$refs.uploadRef.upload(this.formData);
    },
    beforeUpload() {
      return false;
    },
    onSubmitError() {
      this.$refs.uploadRef.doReset();
      this.$refs.submitBtn.setIconStyle(1, [])
    },

    onSubmitSuccess(value) {
      this.$refs.uploadRef.doReset();

      this.fileList.rows.push(value.response.returndata);

      this.$refs.fileGrid.load();

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
    //校验字段是否为空
    checkIsNull(val) {
      if (val == '' || val == null || val == undefined)
        return true;
      return false;
    },
    saveRule(params) {
      console.log(this.envItems);
      console.log(this.exclusiveList);
      var flag = this.$refs.detailForm.validate();
      if (flag == false) {
        return false;
      }
      //渠道校验  渠道code跟发布方式必须都有
      for (var i = 0; i < this.envItems.length; i++) {
        let item = this.envItems[i];
        if (this.checkIsNull(item.isChoose) && !this.checkIsNull(item.businessType)) {
          Tools.alert(item.channelName + "未勾选渠道", "danger");
          return false;
        } else if (!this.checkIsNull(item.isChoose) && this.checkIsNull(item.businessType)) {
          Tools.alert(item.channelName + "未勾选发布方式", "danger");
          return false;
        }
      }

      //专户校验
      for (var j = 0; j < this.exclusiveList.length; j++) {
        let exclusive = this.exclusiveList[j];
        if (this.checkIsNull(exclusive.accountName) && !this.checkIsNull(exclusive.accountEmail)) {
          Tools.alert("专户名称不能为空", "danger");
          return false;
        } else if (!this.checkIsNull(exclusive.accountName) && this.checkIsNull(exclusive.accountEmail)) {
          Tools.alert("邮箱不能为空", "danger");
          return false;
        }
        if (j > 0) {
          if (this.checkIsNull(exclusive.accountName) && this.checkIsNull(exclusive.accountEmail)) {
            Tools.alert("专户信息不能为空", "danger");
            return false;
          }
        }
      }


      this.$set(this.formData, 'fileList', JSON.stringify(this.fileList.rows));
      this.$set(this.formData, 'channelList', JSON.stringify(this.envItems));
      this.$set(this.formData, 'exclusiveList', JSON.stringify(this.exclusiveList));
      this.httpUtil.comnUpdate({
        action: 'M8DisclosureManual.insertManualInfo',
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
