<template>
  <div>
    <k-form-search-customize data-target="meetCreateGrid" v-model="queryParam">
      <k-form-item label="会议名称">
        <k-field-text v-model="queryParamMeetName" :data-max-length="64"/>
      </k-form-item>
      <k-form-item label="会议日期">
        <k-field-date v-model="queryParamDateRange" data-type="daterange" />
      </k-form-item>
      <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.upDataFrom={}" data-target="addMeetCreatePopup"
             v-if="global.isShowAuthorityButton('MeetCreate.addMeetCreate')">
<!--        <md-icon md-src="/static/svg/add.svg" />新增-->
        <md-icon md-src="/static/svg/add.svg"/>新增
      </k-btn>
    </k-form-search-customize>
    <!--  grid模板列表  -->
    <k-grid ref="meetCreateGrid" @data-row-select="selectRow" data-action="MeetCreate.findMeetCreate21"
            @init="(grid)=>{this.$kgrid = grid}">

      <k-grid-column data-align="center" data-header="会议名称" data-name="meetName"/>
      <k-grid-column data-align="center" data-header="会议日期" data-name="meetDate" data-type="date" data-date-format="yyyy-MM-dd"/>
      <k-grid-column data-align="center" data-header="会议时间" data-type="time" data-name="meetTime" data-value-format="HHmmss"/>
      <k-grid-column data-align="center" data-header="会议地点" data-name="meetSite"/>
      <k-grid-column data-align="center" data-header="与会人" data-name="username" />
      <k-grid-column data-align="center" data-header="创建人" data-name="crtUser"/>
      <k-grid-column data-align="center" data-header="创建日期" data-name="crtDate" data-type="date"/>
      <k-grid-column data-align="center" data-header="更新人" data-name="updUser" />
      <k-grid-column data-align="center" data-header="更新日期" data-name="updDate" data-type="date"/>


      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" data-descript="修改" data-functype="POPUP" data-size="mini"
               data-target="editMeetCreatePopup"
               v-if="global.isShowAuthorityButton('MeetCreate.updateMeetCreate')">
          <md-icon>edit</md-icon>
        </k-btn>
<!--        <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="MeetCreate.deleteMeetCreate" data-size="mini"
               data-type="danger" data-target="meetCreateGrid" :data-confirm="true" data-descript="删除" >
          <md-icon>close</md-icon>
        </k-btn>-->
        <k-btn class="md-info md-just-icon md-simple" data-functype="POPUP"  data-target="uploadPopup"
               :data-download-name="scope.row.row.meetName"
               v-model="scope.row.row" data-descript="上传会议纪要"
               v-if="global.isShowAuthorityButton('MeetCreate.uploadMeetingMinutes')">
          <md-icon>cloud_upload</md-icon>
        </k-btn>
<!--        <k-btn data-functype="DOWNLOAD" :data-download-name="scope.row.row.meetName" data-confirm data-size="mini"   class="md-info md-just-icon md-simple"
               data-target="prodInfoGrid" data-url="/download/server/PmsApp/documentCreatMeetingAttachment/downAttachment.json" data-descript="下载会议附件资料">
          <md-icon>cloud_download</md-icon>
        </k-btn>-->
<!--        <k-btn data-functype="DOWNLOAD" :data-download-name="scope.row.row.meetName+'会议材料.zip'" data-confirm data-size="mini"   class="md-info md-just-icon md-simple"
               data-target="prodInfoGrid" data-url="/download/server/PmsApp/documentAttachment/downAttachment.json" data-descript="下载会议附件资料">
          <md-icon>cloud_download</md-icon>
        </k-btn>-->
        <k-btn class="md-info md-just-icon md-simple" :data-download-name="scope.row.row.meetName+'.zip'"
               data-descript="下载会议纪要" data-functype="DOWNLOAD" data-size="small"
               data-url="/download/server/PmsApp/prod/meeting/downloadprodCreateMeeting.json" v-model="scope.row.row">
          <md-icon>cloud_download</md-icon>
        </k-btn>


      </template>
    </k-grid>





    <!--    添加  -->
    <k-popup ref="addMeetCreatePopup" data-title="新增">
      <k-form ref="addMeetCreateForm" :data-col="2">

        <k-form-item label="会议名称">
          <k-field-text v-model="addDataFrom.meetName"  :data-max-length="255"  :dataAllowblank="false"  />
        </k-form-item>

        <k-form-item label="会议地点">
          <k-field-text v-model="addDataFrom.meetSite" :data-max-length="255" />
        </k-form-item>

        <k-form-item label="会议日期">
          <k-field-date v-model="addDataFrom.meetDate" :data-allowblank="false" data-type="date" data-date-format="yyyy-MM-dd"/>
        </k-form-item>

        <k-form-item label="会议时间">
          <k-field-time v-model="addDataFrom.meetTime" :data-allowblank="false" data-value-format="HHmm"/>
        </k-form-item>

        <k-form-item label="与会人">
          <k-field-select v-model="addDataFrom.username" data-multiple="true"
                          data-action="User.getUser" data-display-field="username"  data-value-field="username"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="meetCreateGrid" ref="submitBtn"
                 data-from="addMeetCreateForm" :data-model="addDataFrom" data-action="MeetCreate.addMeetCreate">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>



    <!--    修改  -->
    <k-popup ref="editMeetCreatePopup" data-title="修改">
      <k-form ref="editMeetCreateForm" :data-col="2">

        <k-form-item label="会议名称">
          <k-field-text v-model="upDataFrom.meetName"  :data-max-length="255"  :dataAllowblank="false" :data-disabled="false" />
        </k-form-item>

        <k-form-item label="会议地点">
          <k-field-text v-model="upDataFrom.meetSite" :data-max-length="255" />
        </k-form-item>

        <k-form-item label="会议日期">
          <k-field-date v-model="upDataFrom.meetDate" :data-allowblank="false" data-type="date" data-date-format="yyyy-MM-dd"/>
        </k-form-item>
        <k-form-item label="会议时间">
          <k-field-time v-model="upDataFrom.meetTime" :data-allowblank="false" data-value-format="HHmm"/>
        </k-form-item>
        <k-form-item label="与会人">
          <k-field-select v-model="upDataFrom.username" data-multiple="true"
                          data-action="User.getUser" data-display-field="username"  data-value-field="username"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="meetCreateGrid" ref="updateBtn" :data-confirm="false"
                 data-from="editMeetCreateForm" :data-model="upDataFrom" data-action="MeetCreate.updateMeetCreate">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>




    <!--文件-->
    <k-popup ref="uploadPopup" title="会议纪要上传">
      <k-form ref="uploadForm" data-ui="element">
        <k-form-item label="会议名称" v-show="false">
          <k-field-text v-model="uploadData.id"  :data-max-length="100"  :dataAllowblank="false"  data-disabled/>
        </k-form-item>
        <k-form-item label="会议名称">
          <k-field-text v-model="uploadData.meetName"  :data-max-length="100"  :dataAllowblank="false"  data-disabled/>
        </k-form-item>
        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="uploadRef" :data-multiple="true" :data-limit=10
                          :data-error="onSubmitError" :dataChange="onUploadChange"
                          :dataHttpRequest="httpRequest"
                          :data-auto-upload="false">
          </k-field-upload>
        </k-form-item>
        <span style="margin-left: 130px;color:red;">同名文件上传会覆盖原文件,请悉知!</span>
<!--        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="uploadRef" :data-multiple="true"
                          :data-error="onSubmitError" :data-success="onSubmitSuccess" :dataChange="onUploadChange"
                          :data-limit=20 :data-auto-upload="false" data-upload-url="/upload/server/PmsApp/meetingAttachment/upload.json">
          </k-field-upload>
        </k-form-item>-->

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-target="t8ProdPayBackGrid" ref="submitBtn"
                 data-from="uploadForm" :data-model="upDataFrom" @click="submit">
            <span v-show="showSubmitBtn">确定</span>
            <i v-show="!showSubmitBtn" class="el-icon-loading"/>
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>


  </div>

</template>

<script>
  import KFormItem from "@/components/k-element/k-from/k-form-item";
  import {assign} from "lodash";
  import MD5 from "@/frame/md5";
  import Tools from "@/utils/tools";
  import KFieldUpload from "../../../../components/k-element/k-field-upload/k-field-upload"

  export default {
    components: {
      KFormItem,KFieldUpload
    },
    data() {
      return {
        selectRowData: {},
        upDataFrom:{},
        addDataFrom:{},
        uploadData:{
          id:'',
          meetName:'',
        },
        fileList:[],
        fileData:'',
        showSubmitBtn:true,
        queryParamMeetName: '',
        queryParamDateRange: [],
      };
    },

    computed: {
      queryParam() {
        return {
          'meetName': this.queryParamMeetName,
          'createStartDate': this.queryParamDateRange ? this.queryParamDateRange[0] : null,
          'createEndDate': this.queryParamDateRange ? this.queryParamDateRange[1] : null,
        }
      }

    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = assign({}, row)
        this.upDataFrom = assign({}, row)
        this.uploadData = assign({}, row)
      },

      onSubmitError() {
        this.$refs.uploadRef.doReset();
        this.showSubmitBtn = true;
      },
      onSubmitSuccess() {
        this.$refs.uploadRef.doReset();
        this.$refs.uploadForm.reset();
        this.$refs.uploadPopup.close();
        this.$refs.meetCreateGrid.load();
        //Tools.alert("上传附件成功!");
      },
      onUploadChange(file,fileList){
        this.fileList = fileList;
      },
      httpRequest(file){
        this.fileData.append('files', file.file);
      },
      submit(){

        //console.log("this.uploadData=:>>>>>",this.uploadData.id)
        let uploadDatas = this.uploadData;
        this.showSubmitBtn = false;
        this.fileData = new FormData();
        this.$refs.uploadRef.upload();
        this.fileData.append('params', JSON.stringify(uploadDatas));
        this.httpUtil.upload({
          //url:"/upload-files/server/PmsApp/prodInfo/uploadT8ProdPayBack.json",
          url:"/upload-files/server/PmsApp/meetingAttachment/upload.json",
          formData: this.fileData
        }).then(res=>{
          this.showSubmitBtn = true;
          Tools.alert(res.data.returnmsg)
          this.onSubmitSuccess()
        })
      },
      uploadHandler(value){
        this.uploadData = value;
      }

    },



  };
</script>

<style scoped>

</style>
