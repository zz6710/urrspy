<template>
  <div class="md-card k-card md-theme-default parent-div" :style="{height:scrollerHeight}">
    <div class="form-item prod-panel" style="margin-top:50px;margin-left:0px;display: -webkit-box;">
    <k-form ref="addForm" :data-col="3" data-input-width="150px"
            :style="{height:scrollerHeight}">
      <k-form-item label="id" v-show="false">
        <k-field-text v-model="formData.id" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="公告标题" :data-col="3">
        <k-field-text v-model="formData.noticeTitle" :data-max-length="100" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="信披类型" >
        <k-field-select v-model="formData.disclosureType" data-dict="xp_doc_other_type" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="信披子类型"
                   v-if="formData.disclosureType=='5'||formData.disclosureType=='6'||formData.disclosureType=='7'">
        <k-field-select v-model="formData.disclosureSonType" :data-dict="dictSonType"
                          :data-disabled="true"/>
      </k-form-item>

      <k-form-item label="计划发布日期" >
        <k-field-date v-model="formData.planFbDate" data-type="date" style="width: 100%;"
                      :data-disabled="true"/>
      </k-form-item>

      <!--<k-form-item label="是否需要审批" >
        <k-field-select v-model="formData.isNeedExamine" data-dict="1yes0no" :data-disabled="true" />
      </k-form-item>

      <k-form-item label="关联产品">
        <k-field-select v-model="formData.isConnectProd" data-dict="isConnectProd" :data-disabled="true"/>
      </k-form-item>-->


      <!--<k-form-item label="产品名称"v-if="this.showProd" :data-col="2">

        <k-field-text v-model="formData.prodName" :data-disabled="true"/>

      </k-form-item>-->
       <k-form-item label="产品代码" :data-col="2" v-if="this.showProd">

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


      <k-form-item label="是否自动发布" >
        <k-field-select v-model="formData.isAutoSend" data-dict="1yes0no" :data-disabled="true"/>
      </k-form-item>

      <k-form-item label="信披公告说明" :data-col="3">
        <div style="min-height:30px;border: solid 1px #f7f7f9;height: auto;background-color: #f7f7f7;white-space:pre-wrap;" v-html="formData.note"></div>
        <!--<k-field-text v-model="formData.note" input-type="textarea" :data-max-length="500" :data-disabled="true"/>-->
      </k-form-item>

      <k-form-item label="创建人">
        <k-field-text v-model="formData.crtUser" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="创建时间">
        <k-field-text v-model="formData.crtDate" :data-disabled="true"/>
      </k-form-item>


      <k-form-item label="信披渠道" :data-col="3">

      </k-form-item>

      <!--<div style="margin-left: 100px">
        <k-form-item :data-col="3" style="display: inline-block">
          <k-field-checkbox v-model="formData.channelCode" data-action="T8DisChannelInfo.findT8DisChannelInfoAll"
                            data-display-field="channelName" data-value-field="channelCode" :data-disabled="true"></k-field-checkbox>
        </k-form-item>
      </div>-->
    </k-form>

  </div>
    <div>
      <div class="table-box">
        <el-table :data="channelInfoList" ref="channelGrid">
          <el-table-column align="center" prop="name" label="渠道名称">
            <template slot-scope="scope">
              <div class="flex">
                <el-checkbox :disabled="true" v-model="scope.row.channelName.checked" >
                  {{scope.row.channelName.name}}
                </el-checkbox>
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="发布方式">
            <template slot-scope="scope">
              <div class="flex publish-list">
                <div class="publish-item" v-for="(item, index) in scope.row.publishStyle" :key="index">
                  <el-checkbox :disabled="true" v-model="item.checked" @change="handleClickChange(scope.row)">{{item.name}}</el-checkbox>
                </div>
              </div>
            </template>
          </el-table-column>
          <!--          <el-table-column align="center" label="发布">-->
          <!--            <template slot-scope="scope">-->
          <!--              <div class="flex">-->
          <!--                <k-btn class="btn-custom-plain" @click="publishRow(scope.row)">发布</k-btn>-->
          <!--              </div>-->
          <!--            </template>-->
          <!--          </el-table-column>-->
        </el-table>
      </div>
    </div>
  </div>


</template>

<script>
import {assign} from "lodash";
import Tools from "@/utils/tools";
import ManualChannelInfo from '@/pages/pms/disclosureMsg/components/ManualChannelInfo.vue'
export default {
  name: "",
  components: {ManualChannelInfo},
   props:{
      dataData:{
        type:Array,
        required:true
      }
    },
  data() {
    return {
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

      disclosureNoticeData: {
        id:'',
        prodCode:'',
        disclosureType:'',
        disclosureSonType:'',
      },

      sonType: {},//子类型
      formData: {
        prodCode: '',
        prodName: '',
        feeJson: '',
        crtUser: '',
          crtDate:'',
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
      dictSonType:'',
      noticeChannel: [],
      updateChannel: false,
      exclusiveList: [{}], //专户数组
      fileData: {},
      id: '',
      showProd: true,
      channelInfoList:[],
    }
  },
  watch: {
    //监听信披类型
    'formData.disclosureType'(value) {
      this.disclosureTypeChange(value);
    }
  },
  activated() {
    this.refreshPageParam();
  },
  created() {
    console.log('dataData------------>',this.dataData)
    console.log('id------------>',this.dataData[4])
    this.id = this.dataData[4];//公告id
    Tools.getLoginUser().then(res => {
      this.formData.crtUser = res.username;


    })
    //this.refreshPageParam();
    this.getUrlFormData();
  },
  methods: {
    // 滚动区高度
    scrollerHeight: function() {
      return (this.clientHeight + 60) + 'px'; //自定义高度需求
    },
    getUrlFormData(){
      console.log('this.dataData',this.dataData)
      console.log('url形式:dividendsTaskApproval',this.dataData.id)
      let processId = this.dataData.id;
      if (processId===undefined){
        processId = this.dataData[1];
      }

      this.httpUtil.ajax({
        url: 'wf/businessProcess/querySubmitParams.json',
        params: {'processInstanceId':processId},
      }).then(data => {
        this.formData = JSON.parse(data.data);
        if(this.formData.repeatApprovalFlag != undefined && this.formData.repeatApprovalFlag=='1'){
          //如果是手工公告重新发起
          this.httpUtil.ajax({
            url: 'wf/businessProcess/querySubmitParams.json',
            params: {'processInstanceId':this.formData.lastProcessId},
          }).then(data1 => {
            this.formData = JSON.parse(data1.data);
          });
        }
        this.channelInfoList = JSON.parse(this.formData.channelList);
        console.log('解析后的值',this.formData);
        console.log("this.channelInfoList",this.channelInfoList);
        // this.$refs.channelInfo.findChannelInfo();
      });
    },
    refreshPageParam() {

      this.$nextTick(() => {

         this.t8DisclosureNoticeId = this.id;
         console.log('id===>',this.id)

        if (this.id != '' && this.id != undefined) {
          this.httpUtil.comnQuery({
            action: 'DisclosureNotice.findDisclosureOtherNoticesAuth',
            params: {
              id: this.id,
            }
          }).then(data => {
            if (data.rows.length > 0) {

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



          });

        }
        ;
      });

    },
    sendNotice(row) {

      let disclosureNotice = this.formData;

      this.httpUtil.comnUpdate({
        action: "DisclosureNotice.sendOtherNotice",
        params: {"id": disclosureNotice.id, "noticeTitle": disclosureNotice.noticeTitle},
        mask: false,
        successAlert: true,
      }).then(data => {
        this.showSubmitBtn = true;
      })


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

      this.httpUtil.comnUpdate({
        action: "DisclosureNotice.cancleOtherNotice",
        params: {"id": disclosureNotice.id, "prodCode": disclosureNotice.prodCode},
        mask: false,
        successAlert: true,
      }).then(data => {
        this.showSubmitBtn = true;
      })

    },
    disclosureTypeChange(value) {
      if (value == '5') {
        this.noticeTitles = {};
        this.dictSonType = 'xp_son_dtype';
      }else if(value == '6') {
        this.noticeTitles = {};
        this.dictSonType = 'xp_son_ztype';
      }else if(value == '7') {
        this.noticeTitles = {};
        this.dictSonType = 'xp_son_ftype';
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

    //校验字段是否为空
    checkIsNull(val) {
      if (val == '' || val == null || val == undefined)
        return true;
      return false;
    },
    validateData(){
      return true;
    },
    saveRule(params) {
      console.log(this.envItems);
      console.log(this.exclusiveList);
      var flag = this.$refs.addForm.validate();
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

<style scoped lang="scss">
.k-channel {
.k-channel-btns {
  margin-bottom: 30px;
}
.k-channel-container {
.table-box {
  padding: 15px 20px;
}
}
.publish-list {
.publish-item {
  margin: 0 15px 0 0;
&:last-child {
   margin-right: 0;
 }
}
}
.el-table {
&::before {
   height: 0;
 }
/deep/ td.el-table__cell {
  border: 0;
}
/deep/ th.el-table__cell.is-leaf {
  border: 0;
}
}
}
.flex {
  display: flex;
  justify-content: center;
}

.el-checkbox {
  font-weight: normal;
/deep/ .el-checkbox__input.is-checked+.el-checkbox__label {
  color: #606266 !important;
}
}

/deep/ .el-table::before {
  height: 0;
}
/deep/ .el-table td.el-table__cell {
  border: 0;
}
/deep/ .el-table th.el-table__cell.is-leaf {
  border: 0;
}
</style>
