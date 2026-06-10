<template>
  <div class="py-page">
    <div>


      <k-form-search-customize  v-model="queryParam" data-target="trPractyRegistInfoGrid" data-model-name="TrPractyRegistInfo" data-label-width="80px">

        <k-form-item label="姓名">
          <k-field-text v-model="searchParam.name" data-validate-type="text"/>
        </k-form-item>


        <k-form-item label="身份证号">
          <k-field-text v-model="searchParam.idCode" data-validate-type="text"/>
        </k-form-item>


        <k-form-item label="报送状态">
          <k-field-select v-model="searchParam.registerStatus" data-dict="subm_report_status"></k-field-select>
        </k-form-item>

        <k-form-item label="理论报送起始日期" data-label-width="150px">
          <k-field-date v-model="queryParamDateRange" data-type="daterange"  data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>


      </k-form-search-customize>

    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addTrPractyRegistInfoPopup">
            <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
          <k-btn slot="button"  data-functype="POPUP" class="btn-custom-plain"
                data-target="addPopup">
            <md-icon>cloud_upload</md-icon>
            导入
          </k-btn>
          <k-btn slot="button"  class="btn-custom-plain"  data-functype="EXPORT" data-target="trPractyRegistInfoGrid"
                :data-export-name="'从业人员信息登记数据'">
            <md-icon>cloud_download</md-icon>
            导出
          </k-btn>
<!--          <k-btn slot="button" class="btn-custom-plain" data-functype="POPUP"-->
<!--                :data-handler="auditPopup"-->
<!--                v-if="global.isShowAuthorityButton('DisclosureNotice.batchPublishChannel')">-->
<!--            <md-icon md-src="/static/svg/confirm.svg"></md-icon>复核-->
<!--          </k-btn>-->
        </div>
      </div>
      <k-grid ref="trPractyRegistInfoGrid" @data-row-select="selectRow"
              data-fixed="right" data-operate-width="150px" data-action="TrPractyRegistInfo.findTrPractyRegistInfosAndIsError" >
<!--        <k-grid-column data-align="left" data-header="复核状态" data-name="auditStatus" data-export="false" data-dict="xp_disclosure_check_status"></k-grid-column>-->
        <k-grid-column data-header="报送状态" data-name="registerStatus" data-dict="subm_report_status"  data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="从业人员类型" data-name="profession"      data-dict="subm_tr_profession"  data-width="120"  data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="姓名" data-name="name"  data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="工号" data-name="jobnumber"  data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="性别" data-name="sex"    data-dict="subm_tr_sex" data-width="50"></k-grid-column>
         <k-grid-column data-align="left" data-header="身份证号" data-name="idCode" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="发行机构代码" data-name="bankCode" data-width="100" data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="所属总行或分支行" data-name="issBranchType"  data-dict="subm_sys_orglevel" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="所属区域" data-name="region"  data-dict="subm_prod_sale_area"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="具体单位名称" data-name="firmName" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="所属部门" data-name="department"  data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="职位" data-name="post"   data-width="80"></k-grid-column>
        <k-grid-column data-align="left" data-header="学历" data-name="education"  data-dict="subm_t8_education"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="学位" data-name="degree" data-dict="subm_t8_degree" data-width="80"></k-grid-column>
        <k-grid-column data-align="left" data-header="首次参加工作时间" data-name="careerStartDate" data-width="80"></k-grid-column>
        <k-grid-column data-align="left" data-header="首次从事理财业务时间" data-name="wealthStartDate" data-width="80"></k-grid-column>
        <k-grid-column data-align="left" data-header="专业技术职称" data-name="professQualyLevel"  data-dict="subm_t8_professional_titles"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="理财专业证书" data-name="wealthCer"  data-width="350"  :data-multiple="true" data-dict="subm_t8_financial_certificate"></k-grid-column>
        <k-grid-column data-align="left" data-header="理财登记培训证书编号" data-name="registCerNo" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="所获奖励" data-name="reward" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="办公电话" data-name="telphone" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="移动电话" data-name="mobile" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="电子邮箱" data-name="email" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="登记业务分类" data-name="registerClassify" data-dict="subm_t8_register_type" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="登记人员类别" data-name="registType"  data-dict="subm_t8_register_person"  data-width="200"></k-grid-column>
        <k-grid-column data-align="left" data-header="备注" data-name="details" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="登记流水号" data-name="registerSerno" data-width="250" data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="登记日期" data-name="registerDate" data-width="120" data-export="false"  ></k-grid-column>
        <k-grid-column data-align="left" data-header="理论报送起始日期" data-name="theoryReportStartDate" data-width="100" data-export="false"  ></k-grid-column>
        <k-grid-column data-align="left" data-header="理论报送截止日期" data-name="theoryReportEndDate" data-width="100" data-export="false"  ></k-grid-column>
        <k-grid-column data-align="left" data-header="新增日期" data-name="createDate" data-width="120" data-export="false"  ></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text"   data-functype="POPUP" data-size="mini" :data-handler="dataOnChange"
                 data-target="editTrPractyRegistInfoPopup">
            修改
          </k-btn>
          <!--<k-btn data-functype="PAGE" data-size="mini" class="btn-custom-plain" :data-model="scope.row.row.id"
                 @click="popupEdit(scope.row.row)"  data-descript="从业人员登记信息错误详情">
            错误详情
          </k-btn>-->
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="TrPractyRegistInfo.deleteTrPractyRegistInfo" data-size="mini"
                 data-type="danger" data-target="trPractyRegistInfoGrid" :data-confirm="true"  >
            删除
          </k-btn>
        </template>

     </k-grid>
    </div>

	<!--    添加从业人员登记信息弹出框   -->
	<k-popup ref="addTrPractyRegistInfoPopup" data-title="新增">
    	<k-form ref="addTrPractyRegistInfoForm" :data-col="2" isFormBodyScreen>
			<k-form-item label="从业人员类型">
	        	<k-field-select v-model="formData.profession" :data-allowblank="false" :data-auto-validate="true"   data-dict="subm_tr_profession" @data-on-change="dataOnChange"/>
	     	</k-form-item>
			<k-form-item label="姓名">
	        	<k-field-text v-model="formData.name" :data-allowblank="false" :data-auto-validate="true" :data-max-length="200"/>
	     	</k-form-item>
      <k-form-item label="工号">
            <k-field-text v-model="formData.jobnumber" :data-allowblank="false" :data-auto-validate="true" :data-max-length="10"/>
        </k-form-item>
			<k-form-item label="性别">
	        	<k-field-select v-model="formData.sex" :data-allowblank="false" :data-auto-validate="true" data-dict="subm_tr_sex" />
	     	</k-form-item>
			<k-form-item label="发行机构代码">
	        	<k-field-text v-model="formData.bankCode" :data-allowblank="false" :data-auto-validate="true" :data-max-length="6"/>
	     	</k-form-item>
			<k-form-item label="身份证号">
	        	<k-field-text v-model="formData.idCode" :data-allowblank="false" :data-auto-validate="true" :data-max-length="30"/>
	     	</k-form-item>
			<k-form-item label="所属总行或分行">
	        	<k-field-select  v-model="formData.issBranchType" :data-allowblank="false" :data-auto-validate="true" data-dict="subm_sys_orglevel"  @data-on-change="dataOnChange" />
	     	</k-form-item>
			<k-form-item label="所属区域">
	        	<k-field-select v-model="formData.region" :data-allowblank="false" :data-auto-validate="true" data-dict="subm_prod_sale_area"  />
	     	</k-form-item>
			<k-form-item label="具体单位名称">
	        	<k-field-text v-model="formData.firmName" :data-allowblank="formData.firmNameAllowblank" :data-max-length="40"/>
	     	</k-form-item>
			<k-form-item label="所属部门">
	        	<k-field-text v-model="formData.department" :data-allowblank="false" :data-auto-validate="true" :data-max-length="40"/>
	     	</k-form-item>
			<k-form-item label="职位">
	        	<k-field-text v-model="formData.post" :data-allowblank="false" :data-auto-validate="true" :data-max-length="40"/>
	     	</k-form-item>
			<k-form-item label="学历 ">
	        	<k-field-select  v-model="formData.education" data-dict="subm_t8_education" :data-allowblank="false" :data-auto-validate="true"/>
	     	</k-form-item>
			<k-form-item label="学位">
	        	<k-field-select  v-model="formData.degree" data-dict="subm_t8_degree" :data-allowblank="false" :data-auto-validate="true"/>
	     	</k-form-item>
			<k-form-item label="首次参加工作时间">
	        	<k-field-date  v-model="formData.careerStartDate"  data-date-format="yyyyMM" data-value-format="yyyyMM"  :data-allowblank="false" :data-auto-validate="true"/>
	     	</k-form-item>
			<k-form-item label="首次从事理财业务时间">
	        	<k-field-date  v-model="formData.wealthStartDate"  data-date-format="yyyyMM"  data-value-format="yyyyMM" :data-allowblank="false" :data-auto-validate="true" />
	     	</k-form-item>
			<k-form-item label="专业技术职称">
	        	<k-field-select v-model="formData.professQualyLevel" data-dict="subm_t8_professional_titles" :data-allowblank="false" :data-auto-validate="true"/>
	     	</k-form-item>
			<k-form-item label="理财专业证书">
	        	<k-field-select v-model="formData.wealthCer"  data-dict="subm_t8_financial_certificate" @data-on-change="dataOnChange"  :data-allowblank="false" :data-auto-validate="true" :data-multiple="true" />
	     	</k-form-item>
			<k-form-item label="理财登记培训证书编号">
	        	<k-field-text v-model="formData.registCerNo" :data-max-length="32"/>
	     	</k-form-item>
			<k-form-item label="所获奖励">
	        	<k-field-text v-model="formData.reward" :data-allowblank="false" :data-auto-validate="true" :data-max-length="400"/>
	     	</k-form-item>
        <k-form-item label="办公电话">
          <k-field-text v-model="formData.telphone" data-validate-type="text" :data-allowblank="false" :data-auto-validate="true"
                        :data-max-length="30"/>
        </k-form-item>
        <k-form-item label="移动电话">
          <k-field-text v-model="formData.mobile" data-validate-type="telephone" :data-allowblank="false" :data-auto-validate="true"
                        :data-max-length="11"/>
        </k-form-item>
        <k-form-item label="电子邮箱">
          <k-field-text v-model="formData.email" data-validate-type="email" :data-allowblank="false" :data-auto-validate="true"
                        :data-max-length="50"/>
        </k-form-item>
        <k-form-item label="登记业务分类">
          <k-field-select v-model="formData.registerClassify" :data-allowblank="formData.registerClassifyAllowblank"
                          :data-disabled="formData.registerClassifyDisabled" :data-multiple="true"
                          data-dict="subm_t8_register_type"/>
        </k-form-item>
        <k-form-item label="登记人员类别">
          <k-field-select v-model="formData.registType" data-dict="subm_t8_register_person"
                          :data-allowblank="formData.registTypeAllowblank" :data-disabled="formData.registTypeDisabled"
                          :data-multiple="true"/>
        </k-form-item>
        <k-form-item label="备注">
          <k-field-text v-model="formData.details" :data-max-length="256"
                        :data-allowblank="formData.detailsAllowblank"/>
        </k-form-item>
        <!--<k-form-item label="理论报送起始日期">
          <k-field-date v-model="formData.theoryReportStartDate" data-type="date"  data-date-format="yyyy-MM-dd"  data-value-format="yyyyMMdd"  :data-allowblank="false"/>
        </k-form-item>-->


        <k-form-footer slot="footer" data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="TrPractyRegistInfo.addTrPractyRegistInfo"
                 data-from="addTrPractyRegistInfoForm"
                 :data-model="formData" data-target="trPractyRegistInfoGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改从业人员登记信息弹出框   -->
    <k-popup ref="editTrPractyRegistInfoPopup" data-title="修改">
      <k-form ref="editTrPractyRegistInfoForm" :data-col="2" isFormBodyScreen>
        <k-form-item label="从业人员类型">
          <k-field-select v-model="formData.profession" :data-allowblank="false" data-dict="subm_tr_profession"
                          @data-on-change="dataOnChange"/>
        </k-form-item>
        <k-form-item label="姓名">
          <k-field-text v-model="formData.name" :data-allowblank="false" :data-auto-validate="true" :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="工号">
          <k-field-text v-model="formData.jobnumber" :data-allowblank="false" :data-auto-validate="true" :data-max-length="10"/>
        </k-form-item>
        <k-form-item label="性别">
          <k-field-select v-model="formData.sex" :data-allowblank="false" :data-auto-validate="true" data-dict="subm_tr_sex"/>
        </k-form-item>
        <k-form-item label="发行机构代码">
          <k-field-text v-model="formData.bankCode" :data-allowblank="false" :data-auto-validate="true" :data-max-length="6"/>
        </k-form-item>
        <k-form-item label="身份证号">
          <k-field-text v-model="formData.idCode" :data-allowblank="false" :data-auto-validate="true" :data-max-length="30"/>
        </k-form-item>
        <k-form-item label="所属总行或分行">
          <k-field-select v-model="formData.issBranchType" :data-allowblank="false" :data-auto-validate="true" data-dict="subm_sys_orglevel"
                          @data-on-change="dataOnChange"/>
        </k-form-item>
        <k-form-item label="所属区域">
          <k-field-select v-model="formData.region" :data-allowblank="false" :data-auto-validate="true" data-dict="subm_prod_sale_area"/>
        </k-form-item>
        <k-form-item label="具体单位名称">
          <k-field-text v-model="formData.firmName" :data-allowblank="formData.firmNameAllowblank"
                        :data-max-length="40"/>
        </k-form-item>
        <k-form-item label="所属部门">
          <k-field-text v-model="formData.department" :data-allowblank="false" :data-auto-validate="true" :data-max-length="40"/>
        </k-form-item>
        <k-form-item label="职位">
          <k-field-text v-model="formData.post" :data-allowblank="false" :data-auto-validate="true" :data-max-length="40"/>
        </k-form-item>
        <k-form-item label="学历 ">
          <k-field-select v-model="formData.education" data-dict="subm_t8_education" :data-allowblank="false" :data-auto-validate="true"/>
        </k-form-item>
        <k-form-item label="学位">
          <k-field-select v-model="formData.degree" data-dict="subm_t8_degree" :data-allowblank="false" :data-auto-validate="true"/>
        </k-form-item>
        <k-form-item label="首次参加工作时间">
          <k-field-date v-model="formData.careerStartDate" data-date-format="yyyyMM" data-value-format="yyyyMM"
                        :data-allowblank="false" :data-auto-validate="true"/>
        </k-form-item>
        <k-form-item label="首次从事理财业务时间">
          <k-field-date v-model="formData.wealthStartDate" data-date-format="yyyyMM" data-value-format="yyyyMM"
                        :data-allowblank="false" :data-auto-validate="true"/>
        </k-form-item>
        <k-form-item label="专业技术职称">
          <k-field-select v-model="formData.professQualyLevel" data-dict="subm_t8_professional_titles"
                          :data-allowblank="false" :data-auto-validate="true"/>
        </k-form-item>
        <k-form-item label="理财专业证书">
          <k-field-select v-model="formData.wealthCer" data-dict="subm_t8_financial_certificate"
                          :data-allowblank="false" :data-auto-validate="true" :data-multiple="true" @data-on-change="dataOnChange"/>
        </k-form-item>
        <k-form-item label="理财登记培训证书编号">
          <k-field-text v-model="formData.registCerNo" :data-max-length="32" data-regx="^[0-9a-zA-Z]{1,}$" data-regx-text="该项由字母、数字组成"/>
        </k-form-item>
        <k-form-item label="所获奖励">
          <k-field-text v-model="formData.reward" :data-allowblank="false" :data-auto-validate="true" :data-max-length="400"/>
        </k-form-item>
        <k-form-item label="办公电话">
          <k-field-text v-model="formData.telphone" data-validate-type="text" :data-allowblank="false" :data-auto-validate="true"
                        :data-max-length="30"/>
        </k-form-item>
        <k-form-item label="移动电话">
          <k-field-text v-model="formData.mobile" data-validate-type="telephone" :data-allowblank="false" :data-auto-validate="true"
                        :data-max-length="11"/>
        </k-form-item>
        <k-form-item label="电子邮箱">
          <k-field-text v-model="formData.email" data-validate-type="email" :data-allowblank="false" :data-auto-validate="true"
                        :data-max-length="50"/>
        </k-form-item>
        <k-form-item label="登记业务分类">
          <k-field-select v-model="formData.registerClassify" :data-allowblank="formData.registerClassifyAllowblank"
                          :data-disabled="formData.registerClassifyDisabled" :data-multiple="true"
                          data-dict="subm_t8_register_type"/>
        </k-form-item>
        <k-form-item label="登记人员类别">
          <k-field-select v-model="formData.registType" data-dict="subm_t8_register_person"
                          :data-allowblank="formData.registTypeAllowblank" :data-disabled="formData.registTypeDisabled"
                          :data-multiple="true"/>
        </k-form-item>
        <k-form-item label="备注">
          <k-field-text v-model="formData.details" :data-max-length="256"
                        :data-allowblank="formData.detailsAllowblank"/>
        </k-form-item>


        <k-form-footer slot="footer" data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="TrPractyRegistInfo.updateTrPractyRegistInfo"
                 data-from="editTrPractyRegistInfoForm"
                 :data-model="formData" data-target="trPractyRegistInfoGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>

    </k-popup>


    <!-- 模板上传 -->
    <k-popup ref="addPopup" title="报送数据导入" @data-opened="uploadOpened()">
      <k-form ref="addForm" data-ui="element">
        <k-form-item label="理论报送起始日期">
          <k-field-date v-model="uploadBeginDate" data-type="date" data-date-format="yyyy-MM-dd"
                        data-value-format="yyyyMMdd" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="至">
          <k-field-date v-model="uploadQueryDate" data-type="date" data-date-format="yyyy-MM-dd"
                        data-value-format="yyyyMMdd" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="报送数据导入" data-ui="element" data-input-width="500px">
          <k-field-excel-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit='1'
                                data-accept=".xlsx,.xls"
                                :data-error="onSubmitError" :data-success="onSubmitSuccess"
                                :data-auto-upload="false"
                                data-upload-url="upload/server/RptApp/reportManage/TrPractyRegistInfoImport.json">
          </k-field-excel-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="trPractyRegistInfoGrid" ref="submitBtn"
                 :data-auto-upload="false" data-from="addForm" :data-handler="submitUploadParam">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
    <k-popup ref="auditInfoPopup">
      <KAudit v-model="infoPop" :infoPop="infoPop" @auditFunc="closeAuditFunc">
      </KAudit>
    </k-popup>
  </div>
</template>

<script>
import Tools from "@/utils/tools";
import KAudit from "@/pages/zz/manage/Audit.vue";

export default {
  name: "TrPractyRegistInfo",
  components: {KAudit},
  data() {
    return {
      formData: {
        firmNameAllowblank: true,
        detailsAllowblank: true,
        registerClassifyAllowblank: true,
        registerClassifyDisabled: true,
        registTypeAllowblank: true,
        registTypeDisabled: true
      },
      selectRowData: {},
      searchParam: {},
      queryParamDateRange: [],
      uploadBeginDate: '',
      uploadQueryDate: '',

      infoPop: {},
      auditInfoPopupData: {
        tableId: 'app_practy_regist_info',
        tableName: '从业人员登记信息管理'
      }
    };
  },
  computed: {
    queryParam() {
      return {
        'queryStartDate': this.queryParamDateRange ? this.queryParamDateRange[0] : null,
        'queryEndDate': this.queryParamDateRange ? this.queryParamDateRange[1] : null,
        'name': this.searchParam.name,
        'idCode': this.searchParam.idCode,
        'registerStatus': this.searchParam.registerStatus,
      }
    }
  },
  methods: {
    popupEdit(row) {
      let pathUrl = '/main/zz/errorInfo/PractyRegistInfoErr';
      this.$router.push({
        path: pathUrl,
        query: {
          registerSerno: row.registerSerno,
        },
      });
    },
    onSubmitSuccess() {
      this.$refs.uploadRef.doReset();
      this.$refs.addForm.reset();
      this.$refs.addPopup.close();
      this.$refs.trPractyRegistInfoGrid.load(this.queryParam);

    },
    onSubmitError() {
      this.$refs.uploadRef.doReset();
      this.$refs.submitBtn.setIconStyle(1, [])
    },
    submitUploadParam() {
      //文件上传校验
      var validate = this.$refs.addForm.validate();
      if (validate) {
        let formData = {beginDate: this.uploadBeginDate, queryDate: this.uploadQueryDate}
        let temp = document.getElementsByClassName('upload-demo');
        let lis = temp[0].childNodes[1].childNodes.length;
        if (lis > 0) {
          this.$refs.uploadRef.upload(formData);
          this.showSubmitBtn = true;
        } else {
          Tools.alert("上传文件不能为空!", "danger");
          this.showSubmitBtn = true;
          return false;
        }
      }
    },


    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
    },
    dataOnChange() {
      //具体单位名称
      if (this.formData.issBranchType == '02' || this.formData.issBranchType == '03' || this.formData.issBranchType == '04') {
        this.formData.firmNameAllowblank = false;
      } else {
        this.formData.firmNameAllowblank = true;
      }

      //理财专业证书 选择“其他”时备注必填
      this.formData.detailsAllowblank = true;
      if (this.formData.wealthCer != null && this.formData.wealthCer != "" && this.formData.wealthCer != undefined) {
        let wealthCers = this.formData.wealthCer.split(",");
        for (let i = 0; i < wealthCers.length; i++) {
          if (wealthCers[i] == '99') {
            this.formData.detailsAllowblank = false;
          }
        }
      }

      /*登记业务分类 , 登记人员类别：
               1.选择性必填，可多选；
               2.当“从业人员类型”选择“05 理 财登记人”时，该要素必填；当“从业人员类 型”选择其他选项时，该要素不可填。
               */
      if (this.formData.profession == '05') {
        //登记业务分类
         this.$set(this.formData, 'registerClassifyAllowblank', false);
         this.$set(this.formData, 'registerClassifyDisabled', false);
        //登记人员类别
        this.$set(this.formData, 'registTypeAllowblank', false);
        this.$set(this.formData, 'registTypeDisabled', false);
     }else{
          //登记业务分类
         this.$set(this.formData, 'registerClassifyAllowblank', true);
         this.$set(this.formData, 'registerClassifyDisabled', true);
         this.$set(this.formData,'registerClassify',null);
         //登记人员类别
         this.$set(this.formData, 'registTypeAllowblank', true);
         this.$set(this.formData, 'registTypeDisabled', true);
         this.$set(this.formData,'registType',null);
      }
      },
      uploadOpened() {
        this.uploadBeginDate = ''
        this.uploadQueryDate = ''
      },
      editOpened(){
        this.$set(this.formData, 'initIdCode', this.formData.idCode);
        this.dataOnChange();
      },
      // 复核弹窗
      auditPopup() {
        this.infoPop = this.auditInfoPopupData;
        this.$refs.auditInfoPopup.popup();
      },
      // 复核弹窗关闭
    closeAuditFunc(val) {
      console.log(val);
      this.$nextTick(() => {
        this.$refs.trPractyRegistInfoGrid.load();
      })
      this.$refs.auditInfoPopup.close();
    },

    }
  };



</script>
