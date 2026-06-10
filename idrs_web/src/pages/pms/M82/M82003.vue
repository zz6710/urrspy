<template>
  <div>
    <div>

      <k-form-search-customize data-target="quotaMeetingGrid" v-model="queryParam">
        <k-form-item label="会议/审批单名称">
          <k-field-select v-model="queryParam.id"  data-action="QuotaMeeting.findQuotaMeetings"
                          data-display-field="id,meetingName" data-value-field="id" ></k-field-select>
        </k-form-item>
        <k-form-item label="会议日期">
          <k-field-date v-model="queryParam.meetingDate" ></k-field-date>
        </k-form-item>
        <k-form-item label="决策日期">
          <k-field-date v-model="queryParam.decisionDate" ></k-field-date>
        </k-form-item>
        <k-form-item label="决策类型">
          <k-field-select v-model="queryParam.type" data-dict="decision_type"></k-field-select>
        </k-form-item>

        <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}"
               data-target="addQuotaMeetingPopup" v-show="showCreate"
               v-if="global.isShowAuthorityButton('QuotaMeeting.addQuotaMeeting')">
          <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
      </k-form-search-customize>

    </div>
    <div>
      <k-grid ref="quotaMeetingGrid" @data-row-select="selectRow" :data-autoload="true" data-action="QuotaMeeting.findQuotaMeetings1" >
<!--		<k-grid-column data-header="ID" data-name="id"></k-grid-column>-->
		<k-grid-column data-header="会议/审批单名称" data-name="meetingName"></k-grid-column>
		<k-grid-column data-header="会议日期" data-name="meetingDate" data-type="date"></k-grid-column>
		<k-grid-column data-header="会议时间" data-name="meetingTime" data-type="time"></k-grid-column>
		<k-grid-column data-header="会议地址" data-name="meetingAddr"></k-grid-column>
		<k-grid-column data-header="决策日期" data-name="decisionDate" data-type="date"></k-grid-column>
<!--		<k-grid-column data-header="决策人/与会人" data-name="decisionMaker" data-action="User.findUsers" data-display-field="username"  data-value-field="userid"></k-grid-column>-->
		<!-- <k-grid-column data-header="与会人" data-name="participant"></k-grid-column> -->
		 <k-grid-column data-header="决策人/与会人" data-name="tempUsers"></k-grid-column>
<!--		<k-grid-column data-header="会议状态" data-name="meetingState"></k-grid-column>-->
		<k-grid-column data-header="决策类型" data-name="type" data-dict="decision_type"></k-grid-column>
		<k-grid-column data-header="产品代码" data-name="prodCode"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改额度决策会" data-functype="POPUP" data-size="mini"
            data-target="editQuotaMeetingPopup" v-show="showUpdate"
                 v-if="global.isShowAuthorityButton('QuotaMeeting.updateQuotaMeeting')">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="QuotaMeeting.deleteQuotaMeeting"
               data-type="danger" data-target="quotaMeetingGrid" :data-confirm="true" data-descript="删除额度决策会" v-show="showDelete"
                 v-if="global.isShowAuthorityButton('QuotaMeeting.deleteQuotaMeeting')">
          	<md-icon>close</md-icon>
    	  </k-btn>
        <k-btn class="md-info md-just-icon md-simple" v-show="showUpload"
					data-descript="上传会议纪要"data-functype="POPUP"data-size="mini" data-target="minutesOfMeetingPopup"
          v-if="global.isShowAuthorityButton('QuotaMeeting.uploadMeetingMinutes')">
					<md-icon>cloud_upload</md-icon>
				</k-btn>

        <k-btn data-functype="POPUP" data-confirm data-size="mini"   class="md-info md-just-icon md-simple"
               data-target="editAttachmentTable" :data-handler="toParams" data-descript="管理会议附件信息">
           <md-icon>weekend</md-icon>
         </k-btn>
        </template>
      </k-grid>
    </div>
    	 <k-popup ref="editAttachmentTable" title="管理附件列表" data-width="60%" :data-dialog-drag="true">
      <k-grid ref="editAttachmentGrid"
              data-action="DocumentAttachment.findAttachments"
              @data-row-select="selectRow"
              :data-before-load="beforePopupLoad"
              data-operate-column-position="end" :dataPopupAppendToBody="true">
              data-align="center" data-operate-data-width="300px"
              data-operate-column="true" >
        <k-grid-column data-align="center" data-header="id" data-name="id" :data-hidden="true"/>
        <k-grid-column data-align="center" data-header="父级id" data-name="parentId" data-hidden="true" :data-hidden="true"/>
        <k-grid-column data-align="center" data-header="附件名称" data-name="fileName"/>
        <k-grid-column data-align="center" data-header="附件类型" data-name="attachment_type" data-hidden="true"/>
        <k-grid-column data-align="center" data-header="创建日期" data-name="crtDate" data-type="date"/>
        <k-grid-column data-align="center" data-header="创建时间" data-name="crtTime" data-type="time"/>
        <template slot="operate" slot-scope="scope">
          <k-btn data-functype="DOWNLOAD" :data-download-name="scope.row.row.fileName" data-confirm data-size="mini"   class="md-info md-just-icon md-simple"
                 data-target="prodInfoGrid" data-url="/download/server/PmsApp/documentCreatMeetingAttachment/downAttachment.json" data-descript="下载会议附件资料">
            <md-icon>cloud_download</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple"  data-descript="删除附件" data-functype="SUBMIT"  data-confirm data-type="danger"
                 data-target="editAttachmentGrid"
                 data-action="QuotaMeeting.deleteFile"
                 v-if="global.isShowAuthorityButton('QuotaMeeting.deleteFile')">
            <md-icon>close</md-icon>
          </k-btn>

        </template>
      </k-grid>
    </k-popup>
    <!--    会议附件上传  -->
		<k-popup ref="minutesOfMeetingPopup" data-title="上传会议纪要">
			<k-form ref="minutesOfMeetingForm" :data-col="2">
				<k-form-item style="display: none" label="id">
					<k-field-text v-model="uploadData.id" :data-allowblank="false" :data-disabled="true" />
				</k-form-item>
				<k-form-item>
					<k-field-upload
						label="会议纪要"
						data-type="file"
						ref="uploadRef"
						:data-multiple="true"
						:data-limit="10"
						:data-error="onSubmitError"
						:dataChange="onUploadChange"
						:dataHttpRequest="httpRequest"
						:data-auto-upload="false"
					>
					</k-field-upload>
				</k-form-item>

				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-target="prodInfoGrid"
						ref="submitBtn"
						data-from="minutesOfMeetingForm"
						:data-model="uploadData"
						@click="submitMettingFile"
					>
						<span v-show="showSubmitBtn">确定</span>
						<i v-show="!showSubmitBtn" class="el-icon-loading" />
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>

	<!--    添加额度决策会弹出框   -->
	<k-popup ref="addQuotaMeetingPopup" data-title="新增">
    	<k-form ref="addQuotaMeetingForm" :data-col="2">
			<k-form-item label="决策类型">
	        	<k-field-select v-model="formData.type" :data-allowblank="false" data-dict="decision_type"/>
	     	</k-form-item>
			<k-form-item label="会议名称" v-show="formData.type=='1'">
	        	<k-field-text v-model="formData.meetingName" :data-allowblank="formData.type!='1'"/>
	     	</k-form-item>
        <k-form-item label="审批单名称" v-show="formData.type=='2'">
          <k-field-text v-model="formData.meetingName" :data-allowblank="formData.type!='2'"/>
        </k-form-item>
			<k-form-item label="会议日期"  v-show="formData.type=='1'">
	        	<k-field-date v-model="formData.meetingDate" :data-allowblank="formData.type!='1'"/>
	     	</k-form-item>
			<k-form-item label="会议时间"  v-show="formData.type=='1'">
	        	<k-field-time v-model="formData.meetingTime" :data-allowblank="formData.type!='1'"/>
	     	</k-form-item>
			<k-form-item label="会议地址"  v-show="formData.type=='1'">
	        	<k-field-text v-model="formData.meetingAddr" :data-allowblank="formData.type!='1'"/>
	     	</k-form-item>
			<k-form-item label="决策日期" v-show="formData.type=='2'">
	        	<k-field-date v-model="formData.decisionDate" :data-allowblank="formData.type!='2'"/>
	     	</k-form-item>
			<k-form-item label="决策人" v-show="formData.type=='2'">
          <k-field-select
						v-model="formData.decisionMaker"
            @data-on-change="setUsers"
						data-multiple="true"
						data-action="User.findUsers"
						data-value-field="userid"
						data-display-field="username"
            :data-allowblank="formData.type!='2'"
					></k-field-select>
	     	</k-form-item>
			<k-form-item label="与会人" v-show="formData.type=='1'">
          <k-field-select
						v-model="formData.participant"
            @data-on-change="setUsers"
						data-multiple="true"
						data-action="User.findUsers"
						data-value-field="userid"
						data-display-field="username"
            :data-allowblank="formData.type!='1'"
					></k-field-select>
	     	</k-form-item>

        <k-form-item label="临时人员"  v-show="false">
          <k-field-text v-model="formData.tempUser"/>
        </k-form-item>
			<k-form-item label="产品"  v-show="formData.type=='1'||formData.type=='2'">
	        	<k-field-text v-model="formData.t8ProdInfoId" v-show="false"/>
            <k-field-select
              v-model="formData.prodCode"
              data-multiple="true"
              data-action="T8ProdInfo.getProdInfos"
              data-value-field="prodCode"
              data-display-field="prodCode,prodName"
              :data-allowblank="false"
            ></k-field-select>
	     	</k-form-item>


	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="QuotaMeeting.addQuotaMeeting" data-from="addQuotaMeetingForm"
		               :data-model="formData" data-target="quotaMeetingGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改额度决策会弹出框   -->
	<k-popup ref="editQuotaMeetingPopup" data-title="修改">
	  <k-form ref="editQuotaMeetingForm" :data-col="2">
		<k-form-item label="ID" v-show="false">
        	<k-field-text v-model="formData.id" data-disabled="true"/>
     	</k-form-item>
			<k-form-item label="决策类型">
	        	<k-field-select v-model="formData.type" data-disabled="true" data-dict="decision_type"/>
	     	</k-form-item>
			<k-form-item label="会议名称" v-show="formData.type=='1'" >
	        	<k-field-text v-model="formData.meetingName" :data-allowblank="formData.type!='1'"/>
	     	</k-form-item>
      <k-form-item label="审批单名称" v-show="formData.type=='2'">
        <k-field-text v-model="formData.meetingName" :data-allowblank="formData.type!='2'"/>
      </k-form-item>
			<k-form-item label="会议日期"  v-show="formData.type=='1'">
	        	<k-field-date v-model="formData.meetingDate" :data-allowblank="formData.type!='1'"/>
	     	</k-form-item>
			<k-form-item label="会议时间"  v-show="formData.type=='1'">
	        	<k-field-time v-model="formData.meetingTime" :data-allowblank="formData.type!='1'"/>
	     	</k-form-item>
			<k-form-item label="会议地址"  v-show="formData.type=='1'">
	        	<k-field-text v-model="formData.meetingAddr" :data-allowblank="formData.type!='1'"/>
	     	</k-form-item>
			<k-form-item label="决策日期" v-show="formData.type=='2'">
	        	<k-field-date v-model="formData.decisionDate" :data-allowblank="formData.type!='2'"/>
	     	</k-form-item>
			<k-form-item label="决策人" v-show="formData.type=='2'">
        <k-field-select
          v-model="formData.decisionMaker"
          @data-on-change="setUsers"
          data-multiple="true"
          data-action="User.findUsers"
          data-value-field="userid"
          data-display-field="username" :data-allowblank="formData.type!='2'"></k-field-select>
	     	</k-form-item>
			<k-form-item label="与会人" v-show="formData.type=='1'">
          <k-field-select
						v-model="formData.participant"
            @data-on-change="setUsers"
						data-multiple="true"
						data-action="User.findUsers"
						data-value-field="userid"
						data-display-field="username"
            :data-allowblank="formData.type!='1'"
					></k-field-select>
	     	</k-form-item>

			<k-form-item label="产品"  v-show="formData.type=='1'||formData.type=='2'">
	        	<k-field-text v-model="formData.t8ProdInfoId" v-show="false"/>

        <k-field-select
          v-model="formData.prodCode"
          data-multiple="true"
          data-action="T8ProdInfo.getProdInfos"
          data-value-field="prodCode"
          data-display-field="prodCode,prodName"
          :data-allowblank="false"
        ></k-field-select>
	     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="QuotaMeeting.updateQuotaMeeting" data-from="editQuotaMeetingForm"
	        :data-model="formData" data-target="quotaMeetingGrid">
	        <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
	      </k-btn>
	      <k-btn class="btn-custom-plain" data-functype="CLOSE">
	        <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	    </k-form-footer>
	  </k-form>
	</k-popup>
  </div>
</template>

<script>
  import Tools from "@/utils/tools";
  export default {
    data() {
      return {
        queryParam:{},
        formData: {},
        fileList: [],
        selectRowData: {},
        uploadData: {
				id: "",
      },
         showSubmitBtn: true,
         queryParentId:'',
        showCreate:true,//是否显示新增按钮
        showUpdate:true,//是否显示修改按钮
        showDelete:true,//是否显示删除按钮
        showUpload:true,//是否显示上传附件按钮
      };
    },
    created() {
      this.global.getProdUser('');
      this.$nextTick(()=>{
        //获取需要隐藏按钮属性  rennannan 20210322   将接收到的属性值置为false达到隐藏按钮的效果
        this.global.getHideButtons(this);
      });
    },
    methods: {
      setUsers(val){
        console.log("val = >>>>>>>>",val)
        this.formData.tempUsers = val;
      },
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      },
    beforePopupLoad(params){
      params.attachmentType = '10';
      params.parentId = this.queryParentId;
      return params;
    },
    submitMettingFile() {
			this.uploadData.id = this.formData.id;
			let uploadData = this.uploadData;
			this.showSubmitBtn = false;
			this.fileData = new FormData();
			this.$refs.uploadRef.upload();
			this.fileData.append("params", JSON.stringify(uploadData));
			this.httpUtil
				.upload({
					url: "/upload-files/server/PmsApp/quotaMeetAttachment/upload.json",
					formData: this.fileData,
				})
				.then((res) => {
          this.showSubmitBtn = true;
          Tools.alert(res.data.returnmsg)
					this.onSubmitSuccess();
				});
    },
    onSubmitError() {
			this.$refs.uploadRef.doReset();
			this.showSubmitBtn = true;
    },
    onUploadChange(file, fileList) {
			this.fileList = fileList;
    },
    httpRequest(file) {
			this.fileData.append("files", file.file);
    },
    onSubmitSuccess() {
			this.$refs.uploadRef.doReset();
			this.$refs.minutesOfMeetingForm.reset();
			this.$refs.minutesOfMeetingPopup.close();
			this.$refs.quotaMeetingGrid.load();
    },
     toParams : function(row){
      console.log("row=:",row)
     // this.attachments.parentId=row.id;
     // console.log("this.attachments.parentId=:",this.attachments.parentId);
      this.queryParentId = row.id;
    },
    }
  };
</script>
