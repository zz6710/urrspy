<template>
  <div>
    <k-form-search-customize data-target="t8ProdCustodianEmailGrid" v-model="queryParam">
      <k-form-item label="产品代码" v-show="true" data-input-width="194px" data-label-width="150px">
        <k-field-select v-model="prodCode" data-action="T8Dict.findTaProdInfos"
                        data-display-field="prodCode,prodName" data-value-field="prodCode" />
      </k-form-item>
      <k-form-item label="状态" v-show="true" data-input-width="194px" data-label-width="150px">
        <k-field-select v-model="reviewStatus" data-dict="review_status" />
      </k-form-item>
      <k-form-item label="核算经办"  data-input-width="194px" data-label-width="150px">
        <k-field-text v-model="accountingManager"/>
      </k-form-item>
      <!-- <k-form-item label="运营机构"  data-input-width="194px" data-label-width="150px">
        <k-field-text v-model="operatingAgency"/> -->


      <k-form-item label="运营机构" data-input-width="194px" data-label-width="150px">
            <k-field-select v-model="operatingAgency" 
                          data-action="T8Dict.findOperatingAgency"
                          data-value-field="operatingAgency"
                          data-display-field="operatingAgency">
          </k-field-select>
        </k-form-item>
     
      <k-form-item label="经办邮箱"  data-input-width="194px" data-label-width="150px">
        <k-field-text v-model="handlingMailbox"/>
      </k-form-item>
      <k-btn slot="button" data-functype="POPUP" class="btn-custom-primary" data-target="uploadT8ProdCustodianEmailPopup"
             v-if="global.isShowAuthorityButton('T8ProdCustodianEmail.upadteDoc')" :data-handler="upload">
        <md-icon md-src="/static/svg/add.svg" />上传
      </k-btn>
      <k-btn slot="button" style="width: 120px" class="btn-custom-primary" :data-download-name="'托管行信披邮箱模板.xls'"
             data-descript="下载托管行信披邮箱模板" data-functype="DOWNLOAD" data-size="small"
             data-url="/download/server/PmsApp/custodianEmail/downLoadModel.json">
        <md-icon>cloud_download</md-icon>下载模板
      </k-btn>
      <k-btn slot="button" class="md-rose" data-functype="EXPORT" data-target="t8ProdCustodianEmailGrid"
             :data-export-name="'托管行信披邮箱数据'">
        <md-icon>cloud_download</md-icon>
        导出
      </k-btn>
    </k-form-search-customize>

    <div>
      <k-grid ref="t8ProdCustodianEmailGrid" @data-row-select="selectRow"
              data-action="T8ProdCustodianEmail.findT8ProdCustodianEmails">
        <k-grid-column data-header="id" data-name="id" :data-hidden="true" data-export="false"></k-grid-column>
        <k-grid-column data-header="产品代码" data-name="prodCode"></k-grid-column>
        <k-grid-column data-header="产品名称" data-name="prodName" data-width="300"></k-grid-column>
        <k-grid-column data-header="核算经办" data-name="accountingManager"></k-grid-column>
        <k-grid-column data-header="运营机构" data-name="operatingAgency"></k-grid-column>
        <k-grid-column data-header="经办电话" data-name="handlingPhone"></k-grid-column>
        <k-grid-column data-header="经办邮箱" data-name="handlingMailbox"></k-grid-column>
        <k-grid-column data-header="导入时间" data-name="importDate" data-export="false"></k-grid-column>
        <k-grid-column data-header="状态" data-name="reviewStatus" data-dict="review_status" data-export="false"></k-grid-column>
        <k-grid-column data-header="创建人" data-name="crtUserId" :data-hidden="true" data-export="false"></k-grid-column>
        <k-grid-column data-header="审批意见" data-name="opinion" :data-hidden="true" data-export="false"></k-grid-column>
        <k-grid-column data-header="创建人名称" data-name="crtUserName" data-export="false"></k-grid-column>
        <k-grid-column data-header="更新人" data-name="updUserId" :data-hidden="true" data-export="false"></k-grid-column>
        <k-grid-column data-header="更新人名字" data-name="updUserName" :data-hidden="true" data-export="false"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="编辑" data-functype="POPUP"
                 data-size="mini"
                 v-if="global.isShowAuthorityButton('T8ProdCustodianEmail.updateT8ProdCustodianEmail1')"
                 data-target="editT8ProdCustodianEmailPopup2" >
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-info md-just-icon md-simple" data-descript="复核" data-functype="POPUP"
                 data-size="mini"
                 v-if="global.isShowAuthorityButton('T8ProdCustodianEmail.updateT8ProdCustodianEmail1')"
                 data-target="editT8ProdCustodianEmailPopup" v-show="scope.row.row.reviewStatus==0">
            <md-icon>person_add</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT"
                 data-action="T8ProdCustodianEmail.deleteT8ProdCustodianEmail" data-size="mini"
                 v-if="global.isShowAuthorityButton('T8ProdCustodianEmail.deleteT8ProdCustodianEmail1')"
                 data-type="danger" data-target="t8ProdCustodianEmailGrid" :data-confirm="true" data-descript="删除产品托管行信披邮箱">
            <md-icon>close</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--文件上传-->
    <k-popup ref="uploadT8ProdCustodianEmailPopup" data-title="上传">
      <k-form ref="upload" data-ui="element">
        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" data-accept=".xlsx,.xls" ref="uploadRef" :data-multiple="false" :data-limit=1 :data-allowblank="true"
                          :data-success="uploadFileSuccessHandler" :data-error="onFileSubmitError" :notice-timeout="7000"
                          :data-auto-upload="false" data-upload-url="/upload/server/PmsApp/custodianEmail/uploadProdCustodian.json">
          </k-field-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="t8ProdCustodianEmailGrid"
                  :data-handler="submitUploadParam" >
            <span v-show="showSubmitBtn">确定</span>
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>


    <!--    添加产品托管行信披邮箱弹出框   -->
<!--    <k-popup ref="addT8ProdCustodianEmailPopup" data-title="新增">
      <k-form ref="addT8ProdCustodianEmailForm" :data-col="2">
        <k-form-item label="id">
          <k-field-text v-model="formData.id"/>
        </k-form-item>
        <k-form-item label="产品代码">
          <k-field-text v-model="formData.prodCode"/>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="formData.prodName"/>
        </k-form-item>
        <k-form-item label="核算经办">
          <k-field-text v-model="formData.accountingManager"/>
        </k-form-item>
        <k-form-item label="运营机构">
          <k-field-text v-model="formData.operatingAgency"/>
        </k-form-item>
        <k-form-item label="经办电话">
          <k-field-text v-model="formData.handlingPhone"/>
        </k-form-item>
        <k-form-item label="经办邮箱">
          <k-field-text v-model="formData.handlingMailbox"/>
        </k-form-item>
        <k-form-item label="导入时间">
          <k-field-text v-model="formData.importDate"/>
        </k-form-item>
        <k-form-item label="状态">
          <k-field-text v-model="formData.reviewStatus"/>
        </k-form-item>
        <k-form-item label="创建人">
          <k-field-text v-model="formData.crtUserId"/>
        </k-form-item>
        <k-form-item label="创建人名称">
          <k-field-text v-model="formData.crtUserName"/>
        </k-form-item>
        <k-form-item label="更新人">
          <k-field-text v-model="formData.updUserId"/>
        </k-form-item>
        <k-form-item label="更新人名字">
          <k-field-text v-model="formData.updUserName"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdCustodianEmail.addT8ProdCustodianEmail"
                 data-from="addT8ProdCustodianEmailForm"
                 :data-model="formData" data-target="t8ProdCustodianEmailGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>-->
    <!--    修改产品托管行信披邮箱弹出框   -->
    <k-popup ref="editT8ProdCustodianEmailPopup2" data-title="修改">
      <k-form ref="editT8ProdCustodianEmailForm2" :data-col="2">
        <k-form-item label="id" v-show="false">
          <k-field-text v-model="formData.id"  />
        </k-form-item>

        <k-form-item label="产品代码">
          <k-field-select v-model="formData.prodCode" data-action="T8Dict.findEstablishProdInfosForCustEmail"
                          data-display-field="prodCode,prodName" data-value-field="prodCode"
                          @data-on-change="getProdNameByCode1"
                          :dataAllowblank="false"/>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="formData.prodName" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="核算经办">
          <k-field-text v-model="formData.accountingManager" />
        </k-form-item>
        <k-form-item label="运营机构">
          <k-field-text v-model="formData.operatingAgency" />
        </k-form-item>
        <k-form-item label="经办电话">
          <k-field-text v-model="formData.handlingPhone" />
        </k-form-item>
        <k-form-item label="经办邮箱">
          <k-field-text v-model="formData.handlingMailbox" />
        </k-form-item>
        <k-form-item label="导入时间">
          <k-field-text v-model="formData.importDate" />
        </k-form-item>
        <!--        <k-form-item label="状态">
                  <k-field-text v-model="formData.reviewStatus" data-dict="review_status" :data-hidden="true" :data-disabled="true" />
                </k-form-item>-->
        <!--        <k-form-item label="创建人">
                  <k-field-text v-model="formData.crtUserId" :data-disabled="true" :data-hidden="true"/>
                </k-form-item>-->
        <k-form-item label="创建人名称">
          <k-field-text v-model="formData.crtUserName" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="备注" :dataCol="2">
          <k-field-text v-model="formData.opinion" inputType="textarea"/>
        </k-form-item>
        <!--        <k-form-item label="更新人">
                  <k-field-text v-model="formData.updUserId"/>
                </k-form-item>
                <k-form-item label="更新人名字">
                  <k-field-text v-model="formData.updUserName"/>
                </k-form-item>-->
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"
                 data-target="t8ProdCustodianEmailGrid"
                 :data-handler="takeEffect2">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
    <!--    修改产品托管行信披邮箱弹出框   -->
    <k-popup ref="editT8ProdCustodianEmailPopup" data-title="复核">
      <k-form ref="editT8ProdCustodianEmailForm" :data-col="2">
        <k-form-item label="id" v-show="false">
          <k-field-text v-model="formData.id"  :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="产品代码">
          <k-field-text v-model="formData.prodCode" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="formData.prodName" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="核算经办">
          <k-field-text v-model="formData.accountingManager" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="运营机构">
          <k-field-text v-model="formData.operatingAgency" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="经办电话">
          <k-field-text v-model="formData.handlingPhone" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="经办邮箱">
          <k-field-text v-model="formData.handlingMailbox" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="导入时间">
          <k-field-text v-model="formData.importDate" :data-disabled="true"/>
        </k-form-item>
<!--        <k-form-item label="状态">
          <k-field-text v-model="formData.reviewStatus" data-dict="review_status" :data-hidden="true" :data-disabled="true" />
        </k-form-item>-->
<!--        <k-form-item label="创建人">
          <k-field-text v-model="formData.crtUserId" :data-disabled="true" :data-hidden="true"/>
        </k-form-item>-->
        <k-form-item label="创建人名称">
          <k-field-text v-model="formData.crtUserName" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="审批意见" :dataCol="2">
          <k-field-text v-model="formData.opinion" inputType="textarea"/>
        </k-form-item>
<!--        <k-form-item label="更新人">
          <k-field-text v-model="formData.updUserId"/>
        </k-form-item>
        <k-form-item label="更新人名字">
          <k-field-text v-model="formData.updUserName"/>
        </k-form-item>-->
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"
                 data-target="t8ProdCustodianEmailGrid"
                 :data-handler="takeEffect">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>生效
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="SUBMIT"
                 data-target="t8ProdCustodianEmailGrid"  :data-handler="Invalidation">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>不生效
          </k-btn>
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
      reviewStatus:'',
      prodCode:'',
      accountingManager: '',
      operatingAgency: '',
      handlingMailbox: '',
      showSubmitBtn:true,
      formData: {},
      selectRowData: {}
    };
  },
  methods: {
    getProdNameByCode1(){
      this.httpUtil.comnQuery({
        action: "T8ProdInfo.getProdNameByProdCode",
        params: {prodCode: this.formData.prodCode}
      }).then(data => {
        this.$set(this.formData,"prodName",data.rows[0].prodName);
      }).catch({})
    },
    upload(){
      this.showSubmitBtn=true
    },
    Invalidation(){
      this.formData.reviewStatus=0
      //失效
      this.httpUtil.comnQuery({
        action: 'T8ProdCustodianEmail.updateT8ProdCustodianEmail',
        params: this.formData
      }).then(data => {
        if(data.success==true){
          Tools.alert(data.returnmsg)
        }
        this.$refs.t8ProdCustodianEmailGrid.load()
      });
      this.$refs.editT8ProdCustodianEmailPopup.close()
    },
    takeEffect() {
      //生效
      this.formData.reviewStatus=1
      this.httpUtil.comnQuery({
        action: 'T8ProdCustodianEmail.updateT8ProdCustodianEmail',
        params: this.formData
      }).then(data => {
        if(data.success==true){
          Tools.alert(data.returnmsg)
        }
        //重新加载表格
        this.$refs.t8ProdCustodianEmailGrid.load()
      });
      //关闭弹窗
      this.$refs.editT8ProdCustodianEmailPopup.close()
    },
    takeEffect2() {
      //生效
      this.formData.reviewStatus=1
      this.httpUtil.comnQuery({
        action: 'T8ProdCustodianEmail.updateT8ProdCustodianEmailOnly',
        params: this.formData
      }).then(data => {
        if(data.success==true){
          Tools.alert("修改成功")
        }
        //重新加载表格
        this.$refs.t8ProdCustodianEmailGrid.load()
      });
      //关闭弹窗
      this.$refs.editT8ProdCustodianEmailPopup2.close()
    },
    uploadFileSuccessHandler() {
      this.showSubmitBtn=true
      //清空上传列表
      //this.$refs.uploadT8ProdCustodianEmailPopup.doReset()
      //关闭上传弹窗
      this.$refs.uploadT8ProdCustodianEmailPopup.close()
      this.$refs.t8ProdCustodianEmailGrid.load()
    },
    submitUploadParam() {
      //上传文件列表
      let temp = document.getElementsByClassName('upload-demo');
      let lis = temp[0].childNodes[1].childNodes.length;
      if(lis>0){
        //旋转图标展示
        this.showSubmitBtn=false
        this.$refs.uploadRef.upload();
      }else {
        Tools.alert("至少上传一个文件",'danger')
        this.$refs.uploadT8ProdCustodianEmailPopup.close()
      }

    },
    onFileSubmitError(){
      this.showSubmitBtn=true
      this.$refs.uploadT8ProdCustodianEmailPopup.close()
      this.$refs.t8ProdCustodianEmailGrid.load()
    },
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)

    }
  },
  computed: {
    queryParam() {
      return {
        'reviewStatus':this.reviewStatus,
        'prodCode':this.prodCode,
        'accountingManager':this.accountingManager,
        'operatingAgency':this.operatingAgency,
        'handlingMailbox':this.handlingMailbox
      }
    }
  },

};
</script>
<style scoped>

</style>
