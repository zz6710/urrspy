<template>
  <div>
    <k-form-search-customize data-target="queryTable" v-model="printTemp">

      <k-form-item label="产品代码">
        <k-field-select v-model="printTemp.prodCode"  data-action="T8Dict.findNotEstablishProdInfos"
                        data-display-field="prodCode,prodName" data-value-field="prodCode"></k-field-select>
      </k-form-item>
      <k-form-item label="产品名称">
        <k-field-text v-model="printTemp.prodName" data-validate-type="text"/>
      </k-form-item>
      <k-form-item label="报备材料确认状态" v-show="true">
        <k-field-select v-model="printTemp.filingMaterialsStatus" data-dict="t8_filing_materials_status"/>
      </k-form-item>
      <k-form-item label="报备状态">
        <k-field-select v-model="printTemp.filingStatus" data-dict="t8_filing_materials_status"></k-field-select>
      </k-form-item>
    </k-form-search-customize>

    <k-grid ref="queryTable" data-action="OneTimesFiling.findFilingT8ProdInfos1" @data-row-select="selectRow" data-operate-width="200px" >
      <k-grid-column data-header="产品Id" data-name="id" :data-hidden="true" />
      <k-grid-column data-header="产品代码" data-name="prodCode"  />
      <k-grid-column data-header="产品名称" data-name="prodName" />
      <k-grid-column data-header="文档类型" data-name="documentType" data-hidden="true"/>
      <k-grid-column data-header="产品状态" data-name="prodStatus" data-dict="t8_prod_status" />
      <k-grid-column data-header="产品子状态" data-name="prodSonStatus" data-dict="t8_prod_son_status" />
      <k-grid-column data-header="报备材料确认状态" data-name="filingMaterialsStatus" data-dict="t8_filing_materials_status"/>
      <k-grid-column data-header="报备状态" data-name="filingStatus" data-dict="t8_filing_status"/>
      <template slot="operate"  slot-scope="scope">
        <!-- <k-btn class="md-info md-just-icon md-simple" v-if="global.getProdIfUser(scope.row.row.id)"
               data-descript="附件详情" data-functype="POPUP" data-size="small"
               data-target="detailsPopup" v-model="scope.row.row">
          <md-icon>library_books</md-icon>
        </k-btn> -->
        <k-btn class="md-info md-just-icon md-simple" v-if="global.getProdIfUser(scope.row.row.id)&&
               global.isShowAuthorityButton('OneTimesFiling.updateFilingMaterialsStatus')"
               data-descript="报备材料确认" data-functype="SUBMIT" data-size="small"
               data-action="OneTimesFiling.updateFilingMaterialsStatus" data-target="queryTable" v-model="scope.row.row"
               v-show="showConfirmDoc">
          <md-icon>done</md-icon>
        </k-btn>
<!--        <k-btn class="md-info md-just-icon md-simple" v-if="global.getProdIfUser(scope.row.row.id)&&
               global.isShowAuthorityButton('OneTimesFiling.updateFilingMaterialsStatus')"
               data-descript="报备材料法审" data-functype="POPUP" data-size="small"
               @click="setFileParams(scope.row.row)" data-target="filePopup" v-model="scope.row.row" v-show="showConfirmDoc">
          <md-icon>done</md-icon>
        </k-btn>-->
        <k-btn class="md-info md-just-icon md-simple" :data-disabled="(scope.row.row.prodSonStatus != '6' && scope.row.row.prodSonStatus != '7')"
               data-descript="报备确认" data-functype="POPUP" data-size="small" v-if="global.getProdIfUser(scope.row.row.id)&&
               global.isShowAuthorityButton('OneTimesFiling.updateFilingStatus')"
               data-target="confirmPopup" :data-handler="confirmData" v-show="showConfirmRegist">
          <md-icon>done</md-icon>
        </k-btn>

         <k-btn data-functype="DOWNLOAD" :data-download-name="scope.row.row.prodName+'申报登记材料.zip'" data-confirm data-size="mini"
                 class="md-info md-just-icon md-simple"
               data-target="prodInfoGrid" data-url="/download/server/PmsApp/oneTimesFiing/downloaAll.json" data-descript="一键下载" v-show="showDownloadAll">
          <md-icon>cloud_download</md-icon>
        </k-btn>
      </template>
    </k-grid>

    <k-popup ref="filePopup" data-title="报备材料法审">
      <k-form ref="fileForm" data-ui="element">
        <k-form-item label="产品代码" v-show="true">
          <k-field-text v-model="filFormData.prodCode" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="产品名称" v-show="true">
          <k-field-text v-model="filFormData.prodName" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="文件类型" >
          <k-field-select v-model="filFormData.fileType" data-dict="t8_material_review" :data-disabled="true" :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="文件类型" v-show="false">
          <k-field-select v-model="filFormData.documentType"/>
        </k-form-item>
        <k-form-item label="法律合规部意见" v-show="true" :data-col="2">
          <k-field-text v-model="filFormData.advice" inputType="textarea" :rows="3"/>
        </k-form-item>
        <k-form-item label="意见附件" data-ui="element" data-input-width="500px" :data-allowblank="false">
          <k-field-upload data-type="file" ref="fileUploadRef" :data-multiple="false" :data-limit=1
                          :data-error="onFileSubmitError" :data-success="onFileSubmitSuccess"
                          :data-auto-upload="false"  data-upload-url="/upload/server/PmsApp/oneTimesFiling/upload.json">
          </k-field-upload>
        </k-form-item>
        <k-form-item label="附件说明" v-show="true" :data-col="2">
          <k-field-display v-model="filFormData.desc" inputType="textarea" :rows="3"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn data-functype="DOWNLOAD" :data-download-name="filFormData.prodName+'申报登记材料.zip'" data-confirm data-size="mini"
                 class="btn-custom-primary" data-from="fileForm" :data-model="filFormData" data-url="/download/server/PmsApp/oneTimesFiing/downloaAll.json" data-descript="一键下载" v-show="showDownloadAll">
            下载文件
          </k-btn>
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="printTempGrid" ref="fileSubmitBtn"
                 data-from="fileForm" :data-model="filFormData" :data-handler="fileSubmitUploadParam">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="detailsPopup" title="附件详情" @data-opened="loadDownFileInfo">
      <k-grid ref="queryTable" data-url="/server/form/PmsApp/oneTimesFiing/attachList.json" :data-autoload="false"  data-operate-width="150px" >
        <k-grid-column data-header="产品代码" data-name="prodCode" />
        <k-grid-column data-header="产品名称" data-name="prodName" />
        <k-grid-column data-header="文档类型"  data-name="documentType"/>
        <k-grid-column data-header="文件名称"  data-name="fileName" />
       <k-grid-column data-header="文件路径"  data-name="path" />
        <template slot="operate"  slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple"
                 data-descript="下载" data-functype="DOWNLOAD" data-size="small"
                 :data-download-name="scope.row.row.prodName+scope.row.row.fileName"
                 data-url="/download/server/PmsApp/oneTimesFiing/downAttachment.json" v-model="scope.row.row">
            <md-icon>cloud_download</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </k-popup>
    <k-popup ref="confirmPopup" title="报备确认" >
      <k-form ref="confirmForm" :data-col="2">
        <k-form-item label="报备日期" >
          <k-field-date :dataAllowblank="false" v-model="formData.applyRegistDate">

          </k-field-date>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-from="confirmForm"
                 :data-model="formData" data-target="queryTable" data-action="T8ProdInfo.updateFilingStatus" >
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
  import { assign } from "lodash";
  import Tools from '@/utils/tools.js';
    export default {
      name: "oneTimesFiling",
      data(){
        return {
          printTemp:{
            prodCode: ''
          },
          filFormData:{
            id:'',
            prodCode:'',
            prodName:'',
            documentType:'',
            version:'',
            advice:'',
            desc:'',
            isTemplateFile:'',
            fileType: ''
          },
          selectRowData:{},
          formData:{},
          showConfirmDoc:true,//是否显示报备材料确认按钮
          showConfirmRegist:true,//是否显示报备确认按钮
          showDownloadAll:true,//是否显示一键下载按钮
        }
      },
      created() {
        this.global.getProdUser('');
        this.$nextTick(()=>{
          //获取需要隐藏按钮属性  rennannan 20210322   将接收到的属性值置为false达到隐藏按钮的效果
          this.global.getHideButtons(this);
          let prodCode = this.$route.query.prod_code;
          if(prodCode !=''&&prodCode!=undefined){
            this.$refs.queryTable.load({prodCode:prodCode});
          }
        });
      },
      methods:{
        fileSubmitUploadParam(){
          var validate = this.$refs.fileForm.validate();
          if (validate) {
            let formData = this.filFormData;
            let temp = document.getElementsByClassName('upload-demo');
            let lis = temp[0].childNodes[1].childNodes.length;
            if(lis>0){
              this.$refs.fileUploadRef.upload(formData);
            }else{
              Tools.alert("上传文件不能为空!","danger");
              return false;
            }
          }
        },
        onFileSubmitError(){
          this.$refs.fileUploadRef.doReset();
          this.$refs.fileSubmitBtn.setIconStyle(1, []);
        },
        onFileSubmitSuccess() {
          this.$refs.fileUploadRef.doReset();
          this.$refs.fileForm.reset();
          console.log("进入aftersuccess方法!");
          this.$refs.filePopup.close();
          console.log("关闭poup弹窗成功!");
          this.$refs.queryTable.load();
        },
        setFileParams(rows) {
          this.filFormData.id = rows.id;
          this.filFormData.documentType = '20';
          this.filFormData.fileType = '1';
          this.filFormData.prodCode = rows.prodCode;
          this.filFormData.prodName = rows.prodName;
          this.filFormData.distributorCode = rows.distributorCode;
          this.filFormData.advice = rows.approvalAdvice;
          this.filFormData.desc = '(仅支持小于100M的docx或pdf文件上传)';
        },
        selectRow(row) {
          const _this = this;
          _this.selectRowData = assign({}, row);
        },
        loadDownFileInfo(){
          this.$refs.queryTable.load({prodCode: this.selectRowData.prodCode,
          id: this.selectRowData.id,
          prodName: this.selectRowData.prodName});
        },
        //点击报备确认按钮
        confirmData(value){
          this.formData=value;
        }
      }
    }
</script>

<style scoped>

</style>
