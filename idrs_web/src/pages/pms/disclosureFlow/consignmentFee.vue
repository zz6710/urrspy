<template>
  <div>
    <div>
      <k-form-search-customize data-target="t8ConsignmentFeeGrid" v-model="queryParam">
        <k-form-item label="产品代码">
          <k-field-select v-model="prodSearchParam.prodCode"  data-action="T8Dict.findTaProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode" ></k-field-select>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="prodSearchParam.prodName" data-validate-type="text"/>
        </k-form-item>
        <k-form-item label="是否份额分类">
          <k-field-select v-model="prodSearchParam.isShareSort" data-dict="1yes0no" />
        </k-form-item>
        <k-form-item label="计费起始日期">
          <k-field-date v-model="queryParamDateRange" data-type="daterange" />
        </k-form-item>
        <k-form-item label="计费结束日期">
          <k-field-date v-model="queryParamDateRange2" data-type="daterange" />
        </k-form-item>
        <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP"  data-target="addT8ConsignmentFeePopup"
               :data-handler="()=>this.formData={}" v-if="global.isShowAuthorityButton('T8ConsignmentFee.addT8ConsignmentFee')">
          <md-icon md-src="/static/svg/add.svg" />
          新增
        </k-btn>
        <k-btn slot="button" style="width: 120px" class="btn-custom-primary" :data-download-name="'产品代销费导入模板'+'.xlsx'"
               data-descript="下载Excel模板" data-functype="DOWNLOAD" data-size="small"
               data-url="/download/server/PmsApp/prod/consignmentFee/comn-download.json">
          <md-icon>cloud_download</md-icon>下载Excel模板
        </k-btn>
<!--        <k-btn slot="button" style="width: 120px" class="btn-custom-primary" :data-download-name="'产品代销费导入模板'+'.xlsx'"
               data-descript="下载WORD模板" data-functype="DOWNLOAD" data-size="small"
               data-url="/download/server/PmsApp/prod/consignmentFeeTest/comn-download.json">
          <md-icon>cloud_download</md-icon>下载测试word模板
        </k-btn>-->
        <k-btn slot="button" style="width: 120px" data-functype="POPUP" class="btn-custom-primary"
               data-target="addPopup" v-if="global.isShowAuthorityButton('T8ConsignmentFee.batchImport')">
          <md-icon>cloud_upload</md-icon>
          批量导入数据
        </k-btn>
      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="t8ConsignmentFeeGrid" @data-row-select="selectRow"
              data-action="T8ConsignmentFee.findT8ConsignmentFees1">
        <k-grid-column data-header="主键id" data-name="id" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode" data-width="160"></k-grid-column>
        <k-grid-column data-align="center" data-header="产品名称" data-name="prodName" data-width="240"></k-grid-column>
        <k-grid-column data-align="center" data-header="计费起始日期(含)" data-name="feeStartDate" data-type="date"></k-grid-column>
        <k-grid-column data-align="center" data-header="计费结束日期(含)" data-name="feeEndDate" data-type="date"></k-grid-column>
        <k-grid-column data-align="center" data-header="实付光大银行代销费(零售)(元)" data-name="gdFeeRetail" data-width="200"></k-grid-column>
        <k-grid-column data-align="center" data-header="实付光大银行代销费(对公)(元)" data-name="gdFeeCompany" data-width="200"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改代销费" data-functype="POPUP" data-size="mini"
            data-target="editT8ConsignmentFeePopup" v-if="global.isShowAuthorityButton('T8ConsignmentFee.updateT8ConsignmentFee')">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT"
                 data-action="T8ConsignmentFee.deleteT8ConsignmentFee" data-size="mini"
                 data-type="danger" data-target="t8ConsignmentFeeGrid"
                 v-if="global.isShowAuthorityButton('T8ConsignmentFee.deleteT8ConsignmentFee')"
                 :data-confirm="true" data-descript="删除代销费">
            <md-icon>close</md-icon>
          </k-btn>

<!--          <k-btn data-functype="PAGE" data-size="mini" class="md-info md-just-icon md-simple" v-show="scope.row.row.isShareSort == '1'"-->
<!--                 @click="popupEdit(scope.row.row)"  data-descript="子份额列表信息">-->
<!--            <md-icon>weekend</md-icon>-->
<!--          </k-btn>-->

        </template>
      </k-grid>
    </div>

    <!--    添加代销费实体类弹出框   -->
    <k-popup ref="addT8ConsignmentFeePopup" data-title="新增" data-width="60%">
      <k-form ref="addT8ConsignmentFeeForm" :data-col="2" dataLabelWidth="200px">
        <k-form-item label="主键id" v-show="false">
          <k-field-text v-model="formData.id"/>
        </k-form-item>
        <k-form-item label="产品代码">
          <k-field-select v-model="formData.prodCode" data-action="T8Dict.findTaProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="计费起始日期(含)">
          <k-field-date v-model="formData.feeStartDate" :data-allowblank="false" data-date-format="yyyy-MM-dd"/>
        </k-form-item>
			<k-form-item label="计费结束日期(含)">
	        	<k-field-date v-model="formData.feeEndDate" :data-allowblank="false" data-date-format="yyyy-MM-dd"/>
	     	</k-form-item>
			<k-form-item label="实付光大银行代销费(零售)(元)">
	        	<k-field-text v-model="formData.gdFeeRetail" :data-regx="'^[0-9]+(\\.[0-9]{2})?$'"
                          data-regx-text="请输入正确的数字" :data-max-length="17"
                          data-digits="2"  data-integer-length="14"
                          data-validate-type="number" data-type="number"/>
	     	</k-form-item>
			<k-form-item label="实付光大银行代销费(对公)(元)">
	        	<k-field-text v-model="formData.gdFeeCompany" :data-regx="'^[0-9]+(\\.[0-9]{2})?$'"
                          data-regx-text="请输入正确的数字" :data-max-length="17"
                          data-digits="2"  data-integer-length="14"
                          data-validate-type="number" data-type="number"/>
	     	</k-form-item>

	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ConsignmentFee.addT8ConsignmentFee" data-from="addT8ConsignmentFeeForm"
                   :data-model="formData" data-target="t8ConsignmentFeeGrid" :data-handler="compareDate">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
            </k-btn>
          </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改代销费实体类弹出框   -->
    <k-popup ref="editT8ConsignmentFeePopup" data-title="修改" data-width="60%">
      <k-form ref="editT8ConsignmentFeeForm" :data-col="2" dataLabelWidth="200px">
        <k-form-item label="主键id" v-show="false">
          <k-field-text v-model="formData.id"/>
        </k-form-item>
        <k-form-item label="产品代码">
          <k-field-select v-model="formData.prodCode" data-action="T8Dict.findTaProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="计费起始日期(含)">
          <k-field-date v-model="formData.feeStartDate" :data-allowblank="false" data-date-format="yyyy-MM-dd"/>
        </k-form-item>
		<k-form-item label="计费结束日期(含)">
        	<k-field-date v-model="formData.feeEndDate" :data-allowblank="false" data-date-format="yyyy-MM-dd"/>
     	</k-form-item>
		<k-form-item label="实付光大银行代销费(零售)(元)">
        	<k-field-text v-model="formData.gdFeeRetail" :data-regx="'^[0-9]+(\\.[0-9]{2})?$'"
                        data-regx-text="请输入正确的数字" :data-max-length="17"
                        data-digits="2"  data-integer-length="14"
                        data-validate-type="number" data-type="number"/>
     	</k-form-item>
		<k-form-item label="实付光大银行代销费(对公)(元)">
        	<k-field-text v-model="formData.gdFeeCompany" :data-regx="'^[0-9]+(\\.[0-9]{2})?$'"
                        data-regx-text="请输入正确的数字" :data-max-length="17"
                        data-digits="2"  data-integer-length="14"
                        data-validate-type="number" data-type="number"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ConsignmentFee.updateT8ConsignmentFee" data-from="editT8ConsignmentFeeForm"
	        :data-model="formData" data-target="t8ConsignmentFeeGrid" :data-handler="compareDate">
	        <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
	      </k-btn>
	      <k-btn class="btn-custom-plain" data-functype="CLOSE">
	        <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	    </k-form-footer>
	  </k-form>
	</k-popup>

    <!-- 模板上传 -->
    <k-popup ref="addPopup" title="上传Excels">
      <k-form ref="addForm" data-ui="element">
        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-excel-upload data-type="picture" ref="uploadRef" :data-multiple="false" :data-limit=1
                          :data-error="onSubmitError" :data-success="onSubmitSuccess" data-accept=".xlsx,.xls"
                          :data-auto-upload="false" data-upload-url="/upload/server/PmsApp/prod/consignmentFee/comn-upload.json">
          </k-field-excel-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="printTempGrid" ref="submitBtn"
                 data-from="addForm"  :data-handler="submitUploadParam" >确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

<!--      <k-popup ref="warningPopup" title="提示" data-width="800px">-->
<!--          <div>-->
<!--              <span class="mySpan">{{this.shareSortCodes}} 该份额分类产品无数据</span>-->
<!--          </div>-->
<!--          <k-form-footer data-align="center">-->
<!--              <k-btn class="btn-custom-plain" data-functype="CLOSE">确定</k-btn>-->
<!--          </k-form-footer>-->
<!--      </k-popup>-->

      <el-dialog
              title="提示"
              :visible.sync="dialogVisible"
              :close-on-click-modal="false"
              width="30%" margin-top="20%">
          <span>{{this.shareSortCodes}} 该份额分类产品无数据</span>
          <span slot="footer" class="dialog-footer">
            <!--    <el-button @click="dialogVisible = false">取 消</el-button>-->
            <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
          </span>
      </el-dialog>

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
        queryParamDateRange:[],
        queryParamDateRange2:[],
        prodSearchParam: {
          prodCode: ''
        },
        prodCode:'',
          shareSortCodes:'',
          dialogVisible: false,
      };
    },
    computed: {
      queryParam() {
        return {
          'prodName': this.prodSearchParam.prodName,
          'prodCode': this.prodSearchParam.prodCode,
          'isShareSort': this.prodSearchParam.isShareSort,
          'feeStartDateForQuery1': this.queryParamDateRange ? this.queryParamDateRange[0] : null,
          'feeStartDateForQuery2': this.queryParamDateRange ? this.queryParamDateRange[1] : null,
          'feeEndDateForQuery1': this.queryParamDateRange2 ? this.queryParamDateRange2[0] : null,
          'feeEndDateForQuery2': this.queryParamDateRange2 ? this.queryParamDateRange2[1] : null,
        }
      }
    },
    methods: {
      popupEdit(row){
        let pathUrl = '/main/pms/disclosureFlow/shareSortconsignmentFee';
        this.$router.push({
          path: pathUrl,
          query: {prodCode: row.prodCode,feeStartDate:row.feeStartDate,feeEndDate:row.feeEndDate},
        });
      },

      reSetFormData(){
        this.$nextTick(()=>{
          this.$refs.addT8ConsignmentFeeForm.reset();
        });
      },
      onSubmitError() {
        this.$refs.uploadRef.doReset();
        this.$refs.submitBtn.setIconStyle(1, [])
      },

      onSubmitSuccess(val) {
        this.$refs.uploadRef.doReset();
        this.$refs.addForm.reset();
        this.$refs.addPopup.close();
        let shareProdCode = val.response.returndata.prodCode;
        // if (shareProdCode !=null && shareProdCode !='') {
        //     this.shareSortCodes = shareProdCode;
        //     this.$refs.warningPopup.popup();
        // }
        //   if (shareProdCode !=null && shareProdCode !='') {
        //       this.$confirm(shareProdCode+" 该份额分类产品无数据", '提示',{
        //           confirmButtonText: '确定',
        //           cancelButtonText: '取消',
        //           type: 'warning'
        //       })
        //   }
          console.log(shareProdCode);
          if (shareProdCode !=null && shareProdCode !='') {
              this.shareSortCodes = shareProdCode;
              this.dialogVisible = true;
          }
        this.$refs.t8ConsignmentFeeGrid.load();
      },
      submitUploadParam() {
        var validate = this.$refs.addForm.validate();
        if (validate) {
          let formData = this.formData;
          let temp = document.getElementsByClassName('upload-demo');
          let lis = temp[0].childNodes[1].childNodes.length;
          if(lis>0){
            this.$refs.uploadRef.upload(formData);
          }else{
            Tools.alert("上传文件不能为空!","danger");
             this.showSubmitBtn=true;
            return false;
          }
        }
      },
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      },
      compareDate(){

        if(this.formData.feeStartDate>this.formData.feeEndDate){
         Tools.alert("起始日期不能大于结束日期","danger");
         return false;
        }
      }

    }
  };
</script>
<style>
    .myel {
        display: flex;
        flex-direction: column;
        margin:0 !important;
        position:absolute;
        top:50%;
        left:50%;
        transform:translate(-50%,-50%);

        max-height:calc(100% - 200px);
        max-width:calc(100% - 30px);
    }
</style>
