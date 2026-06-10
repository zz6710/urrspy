<template>
  <div>
    <div>
      <k-form-search-customize v-model="empParams" data-target="disclosureEvaluateEmpGrid">
        <k-form-item label="产品代码">
          <k-field-select v-model="empParams.prodCode" data-action="T8ProdInfo.findT8ProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode"></k-field-select>
        </k-form-item>
        <k-form-item label="员工号">
          <k-field-text v-model="empParams.empNo"></k-field-text>
        </k-form-item>
        <k-form-item label="员工名称">
          <k-field-text v-model="empParams.empName"></k-field-text>
        </k-form-item>
        <k-btn slot="button" style="width: 120px" class="btn-custom-primary" :data-download-name="'估值核算信披人员导入模板'+'.xlsx'"
               data-descript="下载模板" data-functype="DOWNLOAD" data-size="small"
               data-url="/download/server/PmsApp/disclosure/evaluate/emp/downloadMould.json">
          <md-icon>cloud_download</md-icon>
          下载模板
        </k-btn>
        <k-btn slot="button" style="width: 120px" data-functype="POPUP" class="btn-custom-primary"
               data-target="addPopup" v-if="global.isShowAuthorityButton('DisclosureEvaluateEmp.batchImport')">
          <md-icon>cloud_upload</md-icon>
          导入数据
        </k-btn>
        <k-btn slot="button" style="width: 120px" class="md-rose" data-functype="EXPORT" data-target="disclosureEvaluateEmpGrid"
               :data-export-name="'估值核算信披人员'">
          <md-icon>cloud_download</md-icon>
          导出数据
        </k-btn>
      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="disclosureEvaluateEmpGrid" @data-row-select="selectRow"
              data-action="DisclosureEvaluateEmp.findEvaluateEmpsAuth">
        <k-grid-column data-header="主键" data-name="id" :data-hidden="true" data-export="false"></k-grid-column>
        <k-grid-column  data-header="员工名称" data-name="empName"></k-grid-column>
        <k-grid-column  data-header="员工号" data-name="empNo"  data-width="120"></k-grid-column>
        <k-grid-column  data-header="产品代码" data-name="prodCode"  data-width="120"></k-grid-column>
        <k-grid-column  data-header="产品名称" data-name="prodName" data-width="240"></k-grid-column>
        <k-grid-column  data-header="创建人id" data-name="createUserId" data-export="false" :data-hidden="true"></k-grid-column>
        <k-grid-column  data-header="创建人" data-name="createUserName" data-export="false"></k-grid-column>
        <k-grid-column  data-header="创建时间" data-name="createDateTime" data-export="false" data-width="180"></k-grid-column>
        <k-grid-column  data-header="修改人" data-name="updateUserName" data-export="false"></k-grid-column>
        <k-grid-column  data-header="修改时间" data-name="updateDateTime" data-export="false" data-width="180"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改信披估值核算人员信息" data-functype="POPUP"
                 data-size="mini"
                 data-target="editDisclosureEvaluateEmpPopup">
            <md-icon>edit</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </div>
    <k-popup ref="addPopup" title="上传Excels">
      <k-form ref="addForm" data-ui="element">
        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="picture" ref="uploadRef" :data-multiple="false" :data-limit=1
                                :data-error="onSubmitError" :data-success="onSubmitSuccess" data-accept=".xlsx,.xls"
                                :data-auto-upload="false" :notice-timeout="5000"
                                data-upload-url="/upload/server/PmsApp/disclosure/evaluate/emp/importEmp.json">
          </k-field-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="printTempGrid" ref="submitBtn"
                 data-from="addForm" :data-handler="submitUploadParam">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
    <!--    添加信披估值核算人员信息弹出框   -->
    <k-popup ref="addDisclosureEvaluateEmpPopup" data-title="新增">
      <k-form ref="addDisclosureEvaluateEmpForm" :data-col="2">
        <k-form-item label="产品代码">
          <k-field-text v-model="formData.prodCode"/>
        </k-form-item>
        <k-form-item label="员工号">
          <k-field-text v-model="formData.empNo"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="DisclosureEvaluateEmp.addDisclosureEvaluateEmp"
                 data-from="addDisclosureEvaluateEmpForm"
                 :data-model="formData" data-target="disclosureEvaluateEmpGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改信披估值核算人员信息弹出框   -->
    <k-popup ref="editDisclosureEvaluateEmpPopup" data-title="修改">
      <k-form ref="editDisclosureEvaluateEmpForm" :data-col="2">
        <k-form-item label="产品代码">
          <k-field-text v-model="formData.prodCode" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="员工名">
          <k-field-select v-model="formData.empNo" data-action="User.getUser" data-display-field="username"
                          data-value-field="jobno"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"
                 data-action="DisclosureEvaluateEmp.updateDisclosureEvaluateEmp"
                 data-from="editDisclosureEvaluateEmpForm"
                 :data-model="formData" data-target="disclosureEvaluateEmpGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
  </div>
</template>

<script>
import KFieldExcelUpload from '@/components/k-element/k-field-excel-upload/k-field-excel-upload.vue'
import Tools from "@/utils/tools";

export default {
  components: {KFieldExcelUpload},
  data() {
    return {
      formData: {},
      selectRowData: {},
      empParams: {
        prodCode: '',
        empNo: '',
        empName: '',
      }
    };
  },
  methods: {
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
    },
    onSubmitError() {
      this.$refs.uploadRef.doReset();
      this.$refs.submitBtn.setIconStyle(1, [])
    },

    onSubmitSuccess() {
      this.$refs.uploadRef.doReset();
      this.$refs.addForm.reset();
      this.$refs.addPopup.close();
      this.$refs.disclosureEvaluateEmpGrid.load();
    },
    submitUploadParam() {
      let validate = this.$refs.addForm.validate();
      if (validate) {
        let formData = this.formData;
        let temp = document.getElementsByClassName('upload-demo');
        let lis = temp[0].childNodes[1].childNodes.length;
        if (lis > 0) {
          this.$refs.uploadRef.upload(formData);
        } else {
          Tools.alert("上传文件不能为空!", "danger");
          this.showSubmitBtn = true;
          return false;
        }
      }
    },
  }
};
</script>
