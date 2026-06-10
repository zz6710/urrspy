<template>
	<div>
		<k-form-search-customize data-target="seminarGrid" v-model="queryForm">
			<k-form-item label="会议名称">
				<k-field-text v-model="queryForm.seminarName" />
			</k-form-item>
			<k-form-item label="会议日期">
				<k-field-date v-model="queryForm.seminarDate" />
			</k-form-item>
      <k-form-item label="创意名称">
        <k-field-text v-model="queryForm.originalityName" />
      </k-form-item>
			<k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="openBox" data-target="addTable"
             v-if="global.isShowAuthorityButton('CreativitySeminar.addMeetAndProdOriginality')">
				<md-icon md-src="/static/svg/add.svg" />
				新增
			</k-btn>
		</k-form-search-customize>

		<k-grid ref="seminarGrid" data-action="CreativitySeminar.find1" @data-row-select="selectStaticTemp"
            data-operate-width="200px">
      <k-grid-column data-header="序号" data-name="id" :data-hidden="true"/>
      <k-grid-column data-header="会议名称" data-name="seminarName"/>
      <k-grid-column data-header="会议日期" data-name="seminarDate" data-type="date"/>
      <k-grid-column data-header="会议时间" data-name="seminarTime" data-type="time"/>
      <k-grid-column data-header="会议地点" data-name="seminarAddr"/>
      <k-grid-column data-header="创建时间" data-name="crtDate" data-render="renderDateTimeCreate"
      />
      <k-grid-column data-header="创建人" data-name="inputuser"/>
      <k-grid-column data-header="与会人" data-name="participant"/>
      <template slot="operate" slot-scope="scope">
        <!--    :data-disabled="scope.row.row.seminarStatus != '1'"      -->
        <k-btn class="md-info md-just-icon md-simple" data-descript="修改" data-functype="POPUP"
               data-size="mini" data-target="editTable"
               v-if="global.isShowAuthorityButton('CreativitySeminar.updateSeminarInfo')">
          <md-icon>edit</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple" data-descript="添加创意" data-functype="POPUP"
               data-size="mini" data-target="addOriginality" :data-handler="openBox">
					<md-icon>add</md-icon>
				</k-btn>
				<k-btn class="md-info md-just-icon md-simple" data-descript="上传会议纪要" data-functype="POPUP"
					data-size="mini" data-target="minutesOfMeetingPopup"
               v-if="global.isShowAuthorityButton('CreativitySeminar.addDocumentAttachments')">
					<md-icon>cloud_upload</md-icon>
				</k-btn>
			 <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="CreativitySeminar.delSeminarInfo" data-size="mini"
              data-type="danger" data-target="seminarGrid" :data-confirm="true" data-descript="删除"
              v-if="global.isShowAuthorityButton('CreativitySeminar.delSeminarInfo')">
        <md-icon>close</md-icon>
      </k-btn>
				<!-- <k-btn class="md-info md-just-icon md-simple" data-descript="会议通知" data-functype="POPUP" data-size="mini"
             data-target="">
        <md-icon>email</md-icon>
      </k-btn> -->

				<k-btn data-functype="POPUP" data-confirm data-size="mini"   class="md-info md-just-icon md-simple"
               data-target="editAttachmentTable" :data-handler="toParams" data-descript="管理会议附件信息">
          <md-icon>weekend</md-icon>
        </k-btn>
			</template>
		</k-grid>
    <k-grid ref="t8ProdCreativeProjectGrid" @data-row-select="selectRow"
            data-action="T8ProdCreativeProject.findT8ProdCreativeProjects" :data-autoload="false">
      <k-grid-column data-header="序号" data-name="id"></k-grid-column>
      <k-grid-column data-header="研讨会名称" data-name="seminarName"></k-grid-column>
      <k-grid-column data-header="创意名称" data-name="originalityName"></k-grid-column>
      <k-grid-column data-header="创意类型" data-dict="t8_originality_type" data-name="originalityType"></k-grid-column>
      <k-grid-column data-header="创意发明人" data-name="inventor"></k-grid-column>
      <k-grid-column data-header="创意状态" data-dict="t8_originality_status" data-name="status"></k-grid-column>
      <k-grid-column data-header="更新时间" data-name="updateDate" data-render="renderDateTimeUpdate"></k-grid-column>
      <k-grid-column data-header="录入人" data-name="inputuser"></k-grid-column>
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" data-descript="修改创意信息"
               data-functype="POPUP" data-size="mini" data-target="editT8ProdCreativeProjectPopup"
               v-if="global.isShowAuthorityButton('T8ProdCreativeProject.updateT8ProdCreativeProject')">
          <md-icon>edit</md-icon>
        </k-btn>
        <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT"
               data-action="T8ProdCreativeProject.deleteT8ProdCreativeProject"
               data-size="mini" data-type="danger" data-target="t8ProdCreativeProjectGrid" :data-confirm="true"
               data-descript="删除创意信息"
          v-if="global.isShowAuthorityButton('T8ProdCreativeProject.deleteT8ProdCreativeProject')">
					<md-icon>close</md-icon>
				</k-btn>

        <k-btn class="md-info md-just-icon md-simple" data-descript="上传议题表"
					data-functype="POPUP" data-size="mini" data-target="minutesOfSeminarPopup"
               v-if="global.isShowAuthorityButton('T8ProdCreativeProject.addDocumentAttachments')">
					<md-icon>cloud_upload</md-icon>
				</k-btn>
<!--        <k-btn data-functype="DOWNLOAD" :data-download-name="scope.row.row.originalityName +'创意附件资料.zip'"
						data-confirm data-size="mini" class="md-info md-just-icon md-simple" data-target="seminarGrid"
						data-url="/download/server/PmsApp/prod/creativeProject/download.json" data-descript="下载创意附件" >
						<md-icon>cloud_download</md-icon>
					</k-btn>-->
        <k-btn data-functype="POPUP" data-confirm data-size="mini"   class="md-info md-just-icon md-simple"
               data-target="editAttachmentTable" :data-handler="toOriginalityParams" data-descript="管理创意附件信息">
          <md-icon>weekend</md-icon>
        </k-btn>
			</template>
		</k-grid>
		<k-popup ref="editT8ProdCreativeProjectPopup" data-title="修改">
			<k-form ref="editT8ProdCreativeProjectForm" :data-col="2">
				<k-form-item label="" v-show="false">
					<k-field-text v-model="formData.id" />
				</k-form-item>
				<k-form-item label="研讨会id" v-show="false">
					<k-field-text v-model="formData.seminarId" :data-disabled="true"/>
				</k-form-item>
        <k-form-item label="会议名称">
        	<k-field-text v-model="formData.seminarName" :data-disabled="true"/>
        </k-form-item>
				<k-form-item label="创意名称">
					<k-field-text v-model="formData.originalityName" :data-max-length="100"/>
				</k-form-item>
				<k-form-item label="创意类型">
					<k-field-select data-dict="t8_originality_type" v-model="formData.originalityType" />
				</k-form-item>
				<k-form-item label="创意发明人">
					<k-field-select v-model="formData.inventor"  data-multiple="true"  data-action="User.findUsers" data-value-field="username"
						data-display-field="username" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="创意状态">
					<k-field-select data-dict="t8_originality_status"  v-model="formData.status" />
				</k-form-item>

				<k-form-footer data-align="center">
					<k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdCreativeProject.updateT8ProdCreativeProject"
						data-from="editT8ProdCreativeProjectForm" :data-model="formData" data-target="t8ProdCreativeProjectGrid" >
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>
		 <k-popup ref="editAttachmentTable" title="管理附件列表">
      <k-grid ref="editAttachmentGrid"
              data-action="DocumentAttachment.findAttachments"
              @data-row-select="selectRow"
              :data-before-load="beforePopupLoad"
              data-operate-column-position="end"
              data-align="center" data-operate-data-width="300px"
              data-operate-column="true"
              :data-display="false">
        <k-grid-column data-align="center" data-header="id" data-name="id"  data-hidden="true" />
        <k-grid-column data-align="center" data-header="父级id" data-name="parentId" data-hidden="true"/>
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
                 data-action="CreativitySeminar.deleteFile"
                 v-if="global.isShowAuthorityButton('CreativitySeminar.deleteFile')">
            <md-icon>close</md-icon>
          </k-btn>

        </template>
      </k-grid>
    </k-popup>

    <k-popup ref="editOriginalityTable" title="管理附件列表">
      <k-grid ref="editOriginalityGrid"
              data-action="DocumentAttachment.findAttachments"
              @data-row-select="selectRow"
              :data-before-load="beforePopupLoad"
              data-operate-column-position="end"
              data-align="center" data-operate-data-width="300px"
              data-operate-column="true"
              :data-display="false">
        <k-grid-column data-align="center" data-header="id" data-name="id"  data-hidden="true" />
        <k-grid-column data-align="center" data-header="父级id" data-name="parentId" data-hidden="true"/>
        <k-grid-column data-align="center" data-header="附件名称" data-name="fileName"/>
        <k-grid-column data-align="center" data-header="附件类型" data-name="attachment_type" data-hidden="true"/>
        <k-grid-column data-align="center" data-header="创建日期" data-name="crtDate"/>
        <k-grid-column data-align="center" data-header="创建时间" data-name="crtTime"/>
        <template slot="operate" slot-scope="scope">
          <k-btn data-functype="DOWNLOAD" :data-download-name="scope.row.row.fileName" data-confirm data-size="mini"   class="md-info md-just-icon md-simple"
                 data-target="prodInfoGrid" data-url="/download/server/PmsApp/documentCreatMeetingAttachment/downAttachment.json" data-descript="下载会议附件资料">
            <md-icon>cloud_download</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple"  data-descript="删除附件" data-functype="SUBMIT"  data-confirm data-type="danger"
                 data-target="editAttachmentGrid"
                 data-action="CreativitySeminar.deleteFile"
                 v-if="global.isShowAuthorityButton('CreativitySeminar.deleteFile')">
            <md-icon>close</md-icon>
          </k-btn>

        </template>
      </k-grid>
    </k-popup>

		<k-popup ref="addTable" data-title="新增" data-total-width="1100px">
      <k-form ref="addForm1" :data-col="3" data-input-width="180px" data-label-width="100px" data-total-width="988px">
        <k-form-item label="会议名称">
          <k-field-text v-model="formData.seminarName" :data-allowblank="false" :data-max-length="128"></k-field-text>
        </k-form-item>
        <k-form-item label="会议地点">
          <k-field-text v-model="formData.seminarAddr" :data-allowblank="false" :data-max-length="64"></k-field-text>
        </k-form-item>
        <k-form-item label="会议日期">
          <k-field-date v-model="formData.seminarDate" :data-allowblank="false"></k-field-date>
        </k-form-item>
        <k-form-item label="会议时间">
					<k-field-time v-model="formData.seminarTime" :data-allowblank="false"></k-field-time>
				</k-form-item>
				<k-form-item label="与会人">
					<k-field-select v-model="formData.participant" data-multiple="true" data-action="User.findUsers"
						data-value-field="username" data-display-field="username" :data-allowblank="false"/>
				</k-form-item>
			</k-form>

      <k-form ref="addForm2" v-for="(item, index) in envItems" :key="index" :data-col="6"
              data-input-width="110px" data-label-width="97px" data-total-width="1118px">
        <k-form-item label="创意名称">
          <k-field-text v-model="item.originalityName" :data-allowblank="false" :data-max-length="100"/>
        </k-form-item>
        <k-form-item label="创意类型">
          <k-field-select v-model="item.originalityType" data-dict="t8_originality_type" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="创意发明人">
          <k-field-select v-model="item.inventor" data-multiple="true" data-action="User.findUsers" data-value-field="username"
                          data-display-field="username"
                          :data-allowblank="false"/>
        </k-form-item>
				<k-form-item label="创意状态">
					<k-field-select v-model="item.status" data-dict="t8_originality_status" :data-allowblank="false" />
				</k-form-item>
				<k-btn class="md-info md-just-icon md-simple" style="top: 15px" data-descript="新增" @click="() => envItems.push({})">
					<md-icon>add</md-icon>
				</k-btn>
				<k-btn class="md-info md-just-icon md-simple" style="top: 15px" data-descript="删除当前行" @click="deleteEvent(index)">
					<md-icon md-src="/static/svg/delete.svg" />
				</k-btn>
			</k-form>
			<div style="margin: 0 auto; width: 255px">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-handler="submitHandle"
               data-action="CreativitySeminar.addMeetAndProdOriginality"
               data-target="seminarGrid" :data-model="formData">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
        </k-btn>
				<k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
			</div>
		</k-popup>



	<k-popup ref="addOriginality" data-title="添加创意">

	  <k-form ref="addForm3" :data-col="3" data-input-width="180px" v-show="false" data-label-width="100px" data-total-width="988px">
        <k-form-item label="会议id">
          <k-field-text v-model="formData.id" :data-allowblank="false" :data-max-length="128"></k-field-text>
        </k-form-item>
	  </k-form>

      <k-form ref="addForm4" v-for="(item, index) in envItems" :key="index" :data-col="6"
              data-input-width="110px" data-label-width="110px" data-total-width="1118px">
        <k-form-item label="创意名称">
          <k-field-text v-model="item.originalityName" :data-allowblank="false" :data-max-length="100"/>
        </k-form-item>
        <k-form-item label="创意类型">
          <k-field-select v-model="item.originalityType" data-dict="t8_originality_type" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="创意发明人">
          <k-field-select v-model="item.inventor" data-multiple="true" data-action="User.findUsers" data-value-field="username"
                          data-display-field="username"
                          :data-allowblank="false"/>
        </k-form-item>
				<k-form-item label="创意状态">
					<k-field-select v-model="item.status" data-dict="t8_originality_status" :data-allowblank="false" />
				</k-form-item>
				<k-btn class="md-info md-just-icon md-simple" style="top: 15px" data-descript="新增" @click="() => envItems.push({})">
					<md-icon>add</md-icon>
				</k-btn>
				<k-btn class="md-info md-just-icon md-simple" style="top: 15px" data-descript="删除当前行" @click="deleteEvent(index)">
					<md-icon md-src="/static/svg/delete.svg" />
				</k-btn>
			</k-form>
			<div style="margin: 0 auto; width: 255px">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-handler="addHandle"
               data-action="CreativitySeminar.addOriginality"
               data-target="seminarGrid" :data-model="formData">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
        </k-btn>
				<k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
			</div>
	</k-popup>
    <k-popup ref="editTable" data-title="修改">
      <k-form ref="editForm1" :data-col="2" dataLabelWidth="150px" data-input-width="220px" data-label-width="62px" data-total-width="800px">
        <k-form-item label="会议名称">
          <k-field-text v-model="formData.seminarName" :data-allowblank="false" :data-max-length="128"></k-field-text>
        </k-form-item>
        <k-form-item label="会议地点">
          <k-field-text v-model="formData.seminarAddr" :data-allowblank="false" :data-max-length="64"></k-field-text>
        </k-form-item>
        <k-form-item label="会议日期">
          <k-field-date v-model="formData.seminarDate" :data-allowblank="false"></k-field-date>
        </k-form-item>
        <k-form-item label="会议时间">
          <k-field-time v-model="formData.seminarTime" :data-allowblank="false"></k-field-time>
        </k-form-item>
        <k-form-item label="与会人">
          <k-field-select v-model="formData.participant" data-multiple="true"  data-action="User.findUsers"
            data-value-field="username"  data-display-field="username" :data-allowblank="false"/>
        </k-form-item>
      </k-form>
      <div style="margin: 0 auto; width: 255px">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-handler="editSubmitHandle"
               data-action="CreativitySeminar.updateSeminarInfo" data-target="seminarGrid" :data-model="formData">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
      </div>
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


		<!--    创意  -->
		<k-popup ref="minutesOfSeminarPopup" data-title="上传创意附件">
			<k-form ref="minutesOfSeminarForm" :data-col="2">
				<k-form-item style="display: none" label="id">
					<k-field-text v-model="uploadData.id" :data-allowblank="false" :data-disabled="true" />
				</k-form-item>
				<k-form-item>
					<k-field-upload
						label="创意附件"
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
						data-from="minutesOfSeminarForm"
						:data-model="uploadData"
						@click="submitSeminaFile"
					>
						<span v-show="showSubmitBtn">确定</span>
						<i v-show="!showSubmitBtn" class="el-icon-loading" />
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>
	</div>
</template>

<script>
import KFormItem from "@/components/k-element/k-from/k-form-item";
import { assign } from "lodash";
import KFormSearchCustomize from "@/components/k-element/k-form-search/k-form-search-customize";
import Tools from "@/utils/tools";
import KFieldUpload from "../../../../components/k-element/k-field-upload/k-field-upload"
export default {
	components: {
		KFormItem,
		KFormSearchCustomize,
    KFieldUpload,
	},
	data() {
		return {
			queryForm: {
				seminarName: "",
				seminarDate: "",
				seminarStatus: "",
				seminarInitiator: "",
        originalityName:'',
			},
			formData: {},
			extraData: {},
			fileList: [],
			envItems: [],
			fileData: "",
			uploadData: {
				id: "",
			},
      showSubmitBtn: true,
       queryParentId:'',
      attachmentType:'',
		};
	},
	methods: {
    renderDateTimeCreate(row) {
      return Tools.formatDateTime(row.crtDate, row.crtTime);
    },
    renderDateTimeUpdate(row) {
      return Tools.formatDateTime(row.updateDate, row.updateTime);
    },
    initAddForm() {
      this.formData = {};
    },
    //一级查询被选中
    selectRow(row, column, event) {
      const _this = this;
      _this.selectRowData = assign({}, row);
      _this.formData = assign({}, row);
    },
    selectStaticTemp(row, column, event) {
			const _this = this;
			_this.selectRowData = assign({}, row);
			_this.formData = assign({}, row);
			this.$refs.t8ProdCreativeProjectGrid.load({ seminarId: _this.selectRowData.id });
    },
   beforePopupLoad(params){
      params.parentId = this.queryParentId;
      params.attachmentType=this.attachmentType;
      return params;
    },
    toParams : function(row){
      //console.log("row=:",row)
     // this.attachments.parentId=row.id;
     // console.log("this.attachments.parentId=:",this.attachments.parentId);
      this.attachmentType='10003';
      this.queryParentId = row.id;
    },

    toOriginalityParams : function(row){
      //console.log("row=:",row)
      // this.attachments.parentId=row.id;
      // console.log("this.attachments.parentId=:",this.attachments.parentId);
      this.attachmentType='10004';
      this.queryParentId = row.id;
    },

		onSubmitError() {
			this.$refs.uploadRef.doReset();
			this.showSubmitBtn = true;
		},
		onUploadChange(file, fileList) {
			this.fileList = fileList;
		},
		dataChange(file, fileList) {
			this.fileList = fileList;
		},
		httpRequest(file) {
			this.fileData.append("files", file.file);
		},
		onSubmitSuccess() {
			this.$refs.uploadRef.doReset();
			this.$refs.minutesOfMeetingForm.reset();
			this.$refs.minutesOfMeetingPopup.close();
			this.$refs.minutesOfSeminarForm.reset();
			this.$refs.minutesOfSeminarPopup.close();

			this.$refs.seminarGrid.load();
		},
		submitUploadParam() {
			this.fileData = new FormData();
			this.$refs.upload.upload(this.extraData);
			this.httpUtil
				.upload({
					url: "/upload-files/server/BaseServer/excel/upload.json",
					formData: this.fileData,
				})
				.then((res) => {
					this.$refs.upload.doReset();
				});
		},
    /*上传会议附件*/
		submitMettingFile() {
			this.uploadData.id = this.formData.id;
			let uploadData = this.uploadData;
			this.showSubmitBtn = false;
			this.fileData = new FormData();
			this.$refs.uploadRef.upload();
			this.fileData.append("params", JSON.stringify(uploadData));
			this.httpUtil
				.upload({
					url: "/upload-files/server/PmsApp/seminarAttachment/upload.json",
					formData: this.fileData,
				})
				.then((res) => {
				  Tools.alert(res.data.returnmsg);
          this.showSubmitBtn = true;
					this.onSubmitSuccess();
				}).catch(res => {
        this.showSubmitBtn = true;
      });
    },
    /*上传创意附件*/
    submitSeminaFile() {
			this.uploadData.id = this.formData.id;
			let uploadData = this.uploadData;
			this.showSubmitBtn = false;
			this.fileData = new FormData();
			this.$refs.uploadRef.upload();
			this.fileData.append("params", JSON.stringify(uploadData));
			this.httpUtil
				.upload({
					url: "/upload-files/server/PmsApp/creativeProjectAttachment/upload.json",
					formData: this.fileData,
				}).then((res) => {
          Tools.alert(res.data.returnmsg);
          this.$refs.minutesOfSeminarPopup.close();
          this.showSubmitBtn = true;
					this.onSubmitSuccess();
				}).catch(res => {
        this.showSubmitBtn = true;
      });
		},
		deleteEvent(index) {
			if (this.envItems.length > 1) {
				this.envItems.splice(index, 1);
			}
		},
		openBox() {
      this.formData = {};
			this.envItems = [{}];
		},
		submitHandle(value) {

			let result = true;
			result = this.$refs.addForm1.validate();
			let form2s = this.$refs.addForm1;
			if (form2s && form2s.length > 0) {
				for (let i = 0; i < form2s.length; i++) {
					result = result && form2s[i].validate();
				}
			}
			if (result === false) {
				return false;
			}
			if (this.envItems && this.envItems.length > 0) {
        for (let i = 0; i < this.$refs.addForm2.length; i++) {
          result = this.$refs.addForm2[i].validate();
        }
        if (result === false) {
          return false;
        }
				value.json = JSON.stringify({ envItemsConf: this.envItems });
			}
		},

		addHandle(value) {

			let result = true;
			result = this.$refs.addForm3.validate();
			let form3s = this.$refs.addForm3;
			if (form3s && form3s.length > 0) {
				for (let i = 0; i < form3s.length; i++) {
					result = result && form3s[i].validate();
				}
			}
			if (result === false) {
				return false;
			}
			if (this.envItems && this.envItems.length > 0) {
        for (let i = 0; i < this.$refs.addForm4.length; i++) {
          result = this.$refs.addForm4[i].validate();
        }
        if (result === false) {
          return false;
        }
				value.json = JSON.stringify({ envItemsConf: this.envItems });
			}
		},
    editSubmitHandle(value) {

      let result = true;
      result = this.$refs.editForm1.validate();
      let form2s = this.$refs.editForm1;
      if (form2s && form2s.length > 0) {
        for (let i = 0; i < form2s.length; i++) {
          result = result && form2s[i].validate();
        }
      }
      if (result === false) {
        return false;
      }
      /*if (this.envItems && this.envItems.length > 0) {
        for (let i = 0; i < this.$refs.addForm2.length; i++) {
          result = this.$refs.addForm2[i].validate();
        }
        if (result === false) {
          return false;
        }
        value.json = JSON.stringify({ envItemsConf: this.envItems });
      }*/
    },
	},

  created() {
   // this.global.getProdUser('');
  },

};
</script>

<style scoped>
</style>
