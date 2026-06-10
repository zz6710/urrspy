<template>
  <div>
    <k-form-search data-target="t8ProdPayBackGrid" data-model-name="T8ProdPayBack">
      <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addPopup">
        <md-icon md-src="/static/svg/add.svg" />新增
      </k-btn>
    </k-form-search>
    <k-grid ref="t8ProdPayBackGrid"
            data-action='T8ProdPayBack.find'
            @data-row-select="selectRow" data-operate-width="200px"
            :data-before-load="loadProdCode" >
      <k-grid-column data-align="center" data-hidden="true" data-name="id" data-header="id"/>
      <k-grid-column data-align="center" data-name="prodCode" data-header="产品代码"/>
      <k-grid-column data-align="center" data-name="prodName" data-header="产品名称"/>
      <k-grid-column data-align="center" data-name="payBackType" data-header="还本方式" data-dict="pay_back_type"/>
      <k-grid-column data-align="center" data-name="redeemAmt" data-header="赎回金额" data-type="money"/>
      <k-grid-column data-align="center" data-name="redeemDate" data-header="赎回日期" data-type="date"/>
      <k-grid-column data-align="center" data-name="redeemNvl" data-header="赎回净值" data-type="number" data-digits="6" />
      <k-grid-column data-align="center" data-name="redeemProportion" data-header="赎回比例" data-type="number" data-digits="2"/>
      <k-grid-column data-align="center" data-header="创建人" data-name="createUserName"/>
      <k-grid-column data-align="center" data-header="创建日期" data-type="date" data-name="createDate"/>
      <k-grid-column data-align="center" data-header="创建时间" data-type="time" data-name="createTime"/>
      <k-grid-column data-align="center" data-header="确认日期" data-type="date" data-name="updateDate"/>
      <k-grid-column data-align="center" data-header="确认时间" data-type="time" data-name="updateTime"/>
      <template slot="operate" slot-scope="scope">
        <k-btn data-functype="POPUP" data-size="mini" data-target="editPopup"
               class="md-info md-just-icon md-simple"
               :data-handler="modifyProdPayBack" data-descript="修改还本记录">
          <md-icon>edit</md-icon>
        </k-btn>
        <k-btn data-functype="SUBMIT" data-size="mini" data-type="danger"  class="md-danger md-just-icon md-simple"
               data-action="T8ProdPayBack.delete" data-target="t8ProdPayBackGrid"
               :data-confirm="true"
               data-descript="删除还本方案">
          <md-icon>close</md-icon>
        </k-btn>
        <k-btn data-functype="POPUP" data-confirm data-size="mini"   class="md-info md-just-icon md-simple"
               data-target="uploadPopup" data-descript="上传还本资料"  :data-handler="uploadHandler">
          <md-icon>cloud_upload</md-icon>
        </k-btn>
        <k-btn data-functype="DOWNLOAD" :data-download-name="scope.row.row.prodName+'还本材料.zip'" data-confirm data-size="mini"
               class="md-info md-just-icon md-simple"
               data-target="t8ProdPayBackGrid"
               data-url="/download/server/PmsApp/prodInfo/downloadT8ProdPayBack.json"
               data-descript="下载还本资料" v-model="scope.row.row">
          <md-icon>cloud_download</md-icon>
        </k-btn>
      </template>
    </k-grid>

    <k-popup ref="addPopup" data-title="添加还本方案">
      <k-form ref="addForm" :data-col="2" @input="forceUpdate">
        <k-form-item label="产品代码">
          <k-field-select v-model="formData.prodCode" data-action="T8Dict.findEstablishProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode"
                          @data-on-change="getProdNameByCode"
                          :dataAllowblank="false"/>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="formData.prodName" :data-disabled="true"
                        :dataAllowblank="false"/>
        </k-form-item>
        <k-form-item label="还本方式">
          <k-field-select v-model="formData.payBackType" data-dict="pay_back_type"
                          @data-on-change="setPayBackType" data-default-value="1"
                          :dataAllowblank="false"/>
        </k-form-item>
        <k-form-item label="赎回金额" v-show="elementStyle[0].show">
          <k-field-text v-model="formData.redeemAmt" data-validate-type="money"
                        :data-digits="2" :data-max-length="15"
                        :dataAllowblank="elementStyle[0].allowBlank" @input="forceUpdate"/>
        </k-form-item>
        <k-form-item label="赎回日期" v-show="elementStyle[0].show">
          <k-field-date v-model="formData.redeemDate"
                        :data-max-length="8"
                        :dataAllowblank="elementStyle[0].allowBlank"/>
        </k-form-item>
        <k-form-item label="赎回净值" v-show="elementStyle[1].show">
          <k-field-text v-model="formData.redeemNvl" data-validate-type="number"
                        :data-digits="6" :data-max-length="15" data-min-value="(0"
                        :dataAllowblank="elementStyle[1].allowBlank" @input="forceUpdate"/>
        </k-form-item>
        <k-form-item label="赎回比例" v-show="elementStyle[1].show">
          <k-field-text v-model="formData.redeemProportion" data-validate-type="number"
                        :data-digits="2" :data-max-length="15" data-min-value="(0"
                        data-max-value="100)"
                        :dataAllowblank="elementStyle[1].allowBlank" @input="forceUpdate"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdPayBack.add"
                 data-from="addForm" :data-model="formData"
                 data-target="t8ProdPayBackGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="editPopup" data-title="修改还本方案">
      <k-form ref="editForm" :data-col="2" @input="forceUpdate">
        <k-form-item label="产品代码">
          <k-field-select v-model="editFormData.prodCode" data-action="T8Dict.findEstablishProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode"
                          @data-on-change="getProdNameByCode"
                          :data-disabled="true"
                          :dataAllowblank="false"/>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="editFormData.prodName" :data-disabled="true"
                        :dataAllowblank="false"/>
        </k-form-item>
        <k-form-item label="还本方式">
          <k-field-select v-model="editFormData.payBackType" data-dict="pay_back_type"
                          @data-on-change="setPayBackType" data-default-value="1"
                          :dataAllowblank="false"/>
        </k-form-item>
        <k-form-item label="赎回金额" v-show="elementStyle[0].show">
          <k-field-text v-model="editFormData.redeemAmt" data-validate-type="money"
                        :data-digits="2" :data-max-length="15"
                        :dataAllowblank="elementStyle[0].allowBlank" @input="forceUpdate"/>
        </k-form-item>
        <k-form-item label="赎回日期" v-show="elementStyle[0].show">
          <k-field-date v-model="editFormData.redeemDate"
                        :data-max-length="8"
                        :dataAllowblank="elementStyle[0].allowBlank"/>
        </k-form-item>
        <k-form-item label="赎回净值" v-show="elementStyle[1].show">
          <k-field-text v-model="editFormData.redeemNvl" data-validate-type="number"
                        :data-digits="6" :data-max-length="15" data-min-value="(0"
                        :dataAllowblank="elementStyle[1].allowBlank" @input="forceUpdate"/>
        </k-form-item>
        <k-form-item label="赎回比例" v-show="elementStyle[1].show">
          <k-field-text v-model="editFormData.redeemProportion" data-validate-type="number"
                        :data-digits="2" :data-max-length="15" data-min-value="(0"
                        data-max-value="100)"
                        :dataAllowblank="elementStyle[1].allowBlank" @input="forceUpdate"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdPayBack.update"
                 data-from="addForm" :data-model="editFormData"
                 data-target="t8ProdPayBackGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="uploadPopup" data-title="上传还本资料">
      <k-form ref="uploadForm" data-ui="element">
        <k-form-item style="display:none" label="id">
          <k-field-text v-model="uploadData.id" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="产品代码">
          <k-field-text v-model="uploadData.prodCode" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="uploadData.prodName" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="uploadRef" :data-multiple="true" :data-limit=10
                          :data-error="onSubmitError" :dataChange="onUploadChange"
                          :dataHttpRequest="httpRequest"
                          :data-auto-upload="false">
          </k-field-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-target="t8ProdPayBackGrid" ref="submitBtn"
                 data-from="uploadForm" :data-model="uploadData" @click="submit">
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
  import {assign} from "lodash";
  import KFieldUpload from "../../../components/k-element/k-field-upload/k-field-upload"
  export default {
    name: "",
    comments:{KFieldUpload},
    data() {
      return {
        prodCode: "",
        formData:{
          prodCode:'',
          prodName:'',
          payBackType:'',
          redeemAmt:'',
          redeemDate:'',
          redeemNvl:'',
          redeemProportion:'',
        },
        editFormData:{
          prodCode:'',
          prodName:'',
          payBackType:'',
          redeemAmt:'',
          redeemDate:'',
          redeemNvl:'',
          redeemProportion:'',
        },
        uploadData:{
          id:'',
          prodCode:'',
          prodName:''
        },
        elementStyle: [
          {
            name: 'volPayBack',  // 按总份额还
            allowBlank: false,
            show: true
          },{
            name: 'proportionPayBack', // 按赎回比列
            allowBlank: true,
            show: false
          }
        ],
        fileList:[],
        fileData:'',
        showSubmitBtn:true
      }
    },
    created () {
      this.prodCode = this.$route.query.prodCode;
    },
    methods: {
      selectRow(row, column, event) {
        const _this = this;
        _this.selectRowData = assign({}, row);
      },
      loadProdCode(params){
        if (this.prodCode != null && this.prodCode != ''){
          params.prodCode = this.prodCode;
          // 该值只用一次
          this.prodCode = "";
        }
        return params;
      },
      modifyProdPayBack(value){
        this.editFormData = value;
        if (this.editFormData.payBackType == '1'){
          // 按总份额还
          this.elementStyle[0].allowBlank = false;
          this.elementStyle[0].show       = true;
          this.elementStyle[1].allowBlank = true;
          this.elementStyle[1].show       = false;
          this.editFormData.redeemNvl = "";
          this.editFormData.redeemProportion = "";
        } else {
          // 按赎回比列
          this.elementStyle[0].allowBlank = true;
          this.elementStyle[0].show       = false;
          this.elementStyle[1].allowBlank = false;
          this.elementStyle[1].show       = true;
          this.editFormData.redeemAmt = "";
          this.editFormData.redeemDate = "";
        }
      },
      getProdNameByCode(){
        this.httpUtil.comnQuery({
          action: "T8ProdInfo.getProdNameByProdCode",
          params: {prodCode: this.formData.prodCode}
        }).then(data => {
          this.$set(this.formData,"prodName",data.rows[0].prodName);
        }).catch({})
      },
      forceUpdate(){
        this.$forceUpdate();
      },
      setPayBackType(value) {
        if (value == '1'){
          // 按总份额还
          this.elementStyle[0].allowBlank = false;
          this.elementStyle[0].show       = true;
          this.elementStyle[1].allowBlank = true;
          this.elementStyle[1].show       = false;
          this.formData.redeemAmt = "";
          this.formData.redeemDate = "";
          this.editFormData.redeemAmt = "";
          this.editFormData.redeemDate = "";
        } else {
          // 按赎回比列
          this.elementStyle[0].allowBlank = true;
          this.elementStyle[0].show       = false;
          this.elementStyle[1].allowBlank = false;
          this.elementStyle[1].show       = true;
          this.formData.redeemNvl = "";
          this.formData.redeemProportion = "";
          this.editFormData.redeemNvl = "";
          this.editFormData.redeemProportion = "";
        }
      },
      onSubmitError() {
        this.$refs.uploadRef.doReset();
        this.showSubmitBtn = true;
      },
      onSubmitSuccess() {
        this.$refs.uploadRef.doReset();
        this.$refs.uploadForm.reset();
        this.$refs.uploadPopup.close();
        this.$refs.t8ProdPayBackGrid.load();
      },
      onUploadChange(file,fileList){
        this.fileList = fileList;
      },
      httpRequest(file){
        this.fileData.append('files', file.file);
      },
      submit(){
        let uploadData = this.uploadData;
        this.showSubmitBtn = false;
        this.fileData = new FormData();
        this.$refs.uploadRef.upload();
        this.fileData.append('params', JSON.stringify(uploadData));
        this.httpUtil.upload({
          url:"/upload-files/server/PmsApp/prodInfo/uploadT8ProdPayBack.json",
          formData: this.fileData
        }).then(res=>{
          this.onSubmitSuccess()
        })
      },
      uploadHandler(value){
        this.uploadData = value;
      }
    }
  }
</script>

<style scoped>

</style>
