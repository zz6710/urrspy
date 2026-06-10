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
    </k-form-search-customize>

      <k-grid ref="prodInfoGrid" data-action="T8ProdParamDistribution.findProdDistributionList1" :data-params="{'prodSonStatus':'12',}">
<!--      <k-grid-column data-align="center" data-header="ID" data-name="id"/>-->
      <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode"/>
      <k-grid-column data-align="center" data-header="产品名称" data-name="prodName"/>
      <k-grid-column data-align="center" data-header="产品状态" data-name="prodStatus" data-dict="t8_prod_status"/>
	 <k-grid-column data-align="center" data-header="风险等级" data-name="prodRiskLevel" data-dict="risklevel"/>
	 <k-grid-column data-align="center" data-header="募集方式" data-name="raiseType" data-dict="t8_raise_type"/>
	 <k-grid-column data-align="center" data-header="分红方式" data-name="bonusType" data-dict="t8_bonus_type" />
	 <k-grid-column data-align="center" data-header="最高募集金额" data-name="maxRaiseAmt"/>
	 <k-grid-column data-align="center" data-header="认购起始日" data-name="applyStartDate" data-type="date"/>
	 <k-grid-column data-align="center" data-header="认购结束日" data-name="applyEndDate" data-type="date"/>
      <template slot="operate" slot-scope="scope">
		   <k-btn data-functype="DOWNLOAD" :data-download-name="scope.row.row.prodCode+'参数.docx'" data-confirm data-size="mini"
              class="md-info md-just-icon md-simple" v-if="global.getProdIfUser(scope.row.row.id)"
               data-target="prodInfoGrid" data-url="/download/server/PmsApp/prod/downloadParamDistribution.json" data-descript="导出参数">
          <md-icon>cloud_download</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple" data-descript="上传终版参数表" data-functype="POPUP"
               data-size="mini" data-target="addPop" :data-handler="setParam" v-if="global.getProdIfUser(scope.row.row.id)&&
               global.isShowAuthorityButton('T8ProdParamDistribution.upload')"
               >
          <md-icon>cloud_upload</md-icon>
        </k-btn>
        <k-btn data-functype="POPUP" data-confirm data-size="mini"   class="md-info md-just-icon md-simple"
               data-target="editAttachmentTable" :data-handler="toParams" data-descript="查看终版参数表" v-if="global.getProdIfUser(scope.row.row.id)">
          <md-icon>weekend</md-icon>
        </k-btn>
      </template>
    </k-grid>

  <k-popup ref="addPop" data-title="上传终版参数表">
			<k-form ref="addPopForm" :data-col="2" >

				<k-form-item>
					<k-field-upload
						label="上传终版参数表"
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
						@click="submitFile" >
						<span v-show="showSubmitBtn">确定</span>
						<i v-show="!showSubmitBtn" class="el-icon-loading" />
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>

    <k-popup ref="editAttachmentTable" title="管理附件列表" data-width="60%" :data-dialog-drag="true">
      <k-grid ref="editAttachmentGrid"
              data-action="DocumentAttachment.findAttachments"
			  :data-params="{'attachmentType':'10002'}"
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
                 data-target="prodInfoGrid" data-url="/download/server/PmsApp/parameterDistribution/downAttachment.json" data-descript="下载发行审批表">
            <md-icon>cloud_download</md-icon>
          </k-btn>

        </template>
      </k-grid>
    </k-popup>


  </div>
</template>

<script>
import { assign } from "lodash";
import Tools from "@/utils/tools";
export default {
  name: "parameterDistribution",
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
				prodName:"",
				prodCode:""
			},
      showSubmitBtn: true,
       queryParentId:'',
      //showUploadFinal:true,//是否显示上传终版参数表
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
					url: "/upload-files/server/PmsApp/parameterDistribution/upload.json",
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
    setParam(param){
     	this.uploadData.prodCode = param.prodCode;
		 this.uploadData.prodName = param.prodName;
    }


	},
};
</script>

<style scoped>
</style>
