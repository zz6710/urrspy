<template>
  <div>
    <k-form-search-customize data-target="prodInfoGrid" v-model="prodSearchParam">

      <k-form-item label="产品代码">
        <k-field-select v-model="prodSearchParam.prodCode"  data-action="T8Dict.findTaProdInfos"
                        data-display-field="prodCode,prodName" data-value-field="prodCode" ></k-field-select>
      </k-form-item>
      <k-form-item label="产品名称">
        <k-field-text v-model="prodSearchParam.prodName" data-validate-type="text"/>
      </k-form-item>
      <k-form-item label="是否存在发行审批表" data-input-width="164px" data-label-width="180px">
        <k-field-select v-model="prodSearchParam.isExistApprovalForm" data-dict="is_default"></k-field-select>
      </k-form-item>
    </k-form-search-customize>

    <k-grid ref="prodInfoGrid" data-action="ProdPublishApproval.findT8ProdInfosPublish" :data-params="{'prodSonStatus':'11',}">
<!--      <k-grid-column data-align="center" data-header="ID" data-name="id"/>-->
      <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode"/>
      <k-grid-column data-align="center" data-header="产品名称" data-name="prodName"/>
      <k-grid-column data-align="center" data-header="产品形态" data-name="prodMode" data-dict="t8_prod_mode"/>
      <k-grid-column data-align="center" data-header="产品状态" data-name="prodStatus" data-dict="t8_prod_status"/>
      <k-grid-column data-header="产品子状态" data-name="prodSonStatus" data-dict="t8_prod_son_status"/>

      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" data-descript="上传发行审批表" data-functype="POPUP"
					data-size="mini" data-target="addPop" :data-handler="setId"  v-if="global.getProdIfUser(scope.row.row.id)&&
               global.isShowAuthorityButton('ProdPublishApproval.UploadForm')"
               v-show="showUploadIssue">
					<md-icon>cloud_upload</md-icon>
				</k-btn>
        <k-btn data-functype="POPUP" data-confirm data-size="mini"   class="md-info md-just-icon md-simple"
               v-if="global.getProdIfUser(scope.row.row.id)"
               data-target="editAttachmentTable" :data-handler="toParams" data-descript="查看发行审批表列表">
          <md-icon>weekend</md-icon>
        </k-btn>
      </template>
    </k-grid>

  <k-popup ref="addPop" data-title="上传发行审批表">
			<k-form ref="addPopForm" :data-col="2" >
				<k-form-item style="display: none"  label="id">
					<k-field-text v-model="uploadData.id" :data-allowblank="false" :data-disabled="true" />
				</k-form-item>
				<k-form-item>
					<k-field-upload
						label="上传发行审批表"
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
						data-from="prodInfoGrid"
						:data-model="uploadData"
						@click="submitFile"
					>
						<span v-show="showSubmitBtn">确定</span>
						<i v-show="!showSubmitBtn" class="el-icon-loading" />
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>

    <k-popup ref="editAttachmentTable" title="管理附件列表">
      <k-grid ref="editAttachmentGrid"
              data-action="DocumentAttachment.findAttachments"
			  :data-params="{'attachmentType':'10001'}"
              :data-before-load="beforePopupLoad"
              data-operate-column-position="end"
              data-align="center" data-operate-data-width="300px"
              data-operate-column="true" :dataPopupAppendToBody="true">
        <k-grid-column data-align="center" data-header="id" data-name="id"/>
        <k-grid-column data-align="center" data-header="父级id" data-name="parentId" data-hidden="true"/>
        <k-grid-column data-align="center" data-header="附件名称" data-name="fileName"/>
        <k-grid-column data-align="center" data-header="附件类型" data-name="attachment_type" data-hidden="true"/>
        <k-grid-column data-align="center" data-header="创建日期" data-name="crtDate"/>
        <k-grid-column data-align="center" data-header="创建时间" data-name="crtTime"/>
        <template slot="operate" slot-scope="scope">
           <k-btn data-functype="DOWNLOAD" :data-download-name="scope.row.row.fileName" data-confirm data-size="mini"   class="md-info md-just-icon md-simple"
                 data-target="prodInfoGrid" data-url="/download/server/PmsApp/prodPublishApprovalAttachment/downAttachment.json" data-descript="下载发行审批表">
            <md-icon>cloud_download</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple"  data-descript="删除附件" data-functype="SUBMIT"  data-confirm data-type="danger"
                 data-action="ProdPublishApproval.deleteFile"
                 v-if="global.isShowAuthorityButton('ProdPublishApproval.deleteFile')">
            <md-icon>close</md-icon>
          </k-btn>

        </template>
      </k-grid>
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
  name: "prodPublishApproval",
	components: {
		KFormItem,
		KFormSearchCustomize,
    KFieldUpload,
	},
	data() {
		return {
      prodSearchParam:{
          prodName:''
        },
			queryForm: {
				seminarName: "",
				seminarDate: "",
				seminarStatus: "",
				seminarInitiator: "",
			},
			formData: {},
			extraData: {},
			fileList: [],
			envItems: [],
			fileData: "",
			uploadData: {
				id: "",
				prodCode:"",
				prodName:""
			},
      showSubmitBtn: true,
       queryParentId:'',
      showUploadIssue:true,//是否显示上传发行审批表按钮
		};
	},
  created() {
    this.global.getProdUser('');
    this.$nextTick(()=>{
      //获取需要隐藏按钮属性  rennannan 20210322   将接收到的属性值置为false达到隐藏按钮的效果
      this.global.getHideButtons(this);
      let prodCode = this.$route.query.prod_code;
      if(prodCode !=''&&prodCode!=undefined){
        this.$refs.prodInfoGrid.load({prodCode:prodCode});
      }
    });
  },
	methods: {
    beforePopupLoad(params){
      params.parentId = this.queryParentId;
      return params;
    },
     toParams : function(row){
      this.queryParentId = row.prodCode;
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
			//this.$refs.uploadRef.doReset();
			this.$refs.addPopForm.reset();
			this.$refs.addPop.close();
			this.$refs.prodInfoGrid.load();
		},
		submitFile() {
			let uploadData = this.uploadData;
			this.showSubmitBtn = false;
			this.fileData = new FormData();
			this.$refs.uploadRef.upload();
			this.fileData.append("params", JSON.stringify(uploadData));
			this.httpUtil
				.upload({
					url: "/upload-files/server/PmsApp/prodPublishApprovalAttachment/upload.json",
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
    setId(param){
     	this.uploadData.prodCode = param.prodCode;
		 this.uploadData.prodName = param.prodName;
    }


	},
};
</script>

<style scoped>
</style>
