<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="PractyRegistInfoErr" data-target="PractyRegistInfoErrGrid" data-label-width="80px" v-model = "searchParam">
        <k-form-item label="导入日期">
          <k-field-date v-model="BreathDay" data-type="daterange" data-date-format="yyyyMM"
                        data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="登记流水号">
          <k-field-text v-model="searchParam.registerSerno"/>
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <k-grid ref="PractyRegistInfoErrGrid" @data-row-select="selectRow" data-operate-column="false" data-action="PractyRegistInfoErr.findPractyRegistInfos" >
		<k-grid-column data-header="从业人员类型错误描述" data-name="professionDesc"></k-grid-column>
		<k-grid-column data-header="姓名错误描述" data-name="nameDesc"></k-grid-column>
		<k-grid-column data-header="性别错误描述" data-name="sexDesc"></k-grid-column>
		<k-grid-column data-header="发行机构代码错误描述" data-name="bankCodeDesc"></k-grid-column>
		<k-grid-column data-header="身份证号错误描述" data-name="idCodeDesc"></k-grid-column>
		<k-grid-column data-header="所属总行或分行错误描述" data-name="issBranchTypeDesc"></k-grid-column>
		<k-grid-column data-header="所属区域错误描述" data-name="regionDesc"></k-grid-column>
		<k-grid-column data-header="具体单位名称错误描述" data-name="firmNameDesc"></k-grid-column>
		<k-grid-column data-header="所属部门错误描述" data-name="departmentDesc"></k-grid-column>
		<k-grid-column data-header="职位错误描述" data-name="postDesc"></k-grid-column>
		<k-grid-column data-header="学历错误描述" data-name="educationDesc"></k-grid-column>
		<k-grid-column data-header="学位错误描述" data-name="degreeDesc"></k-grid-column>
		<k-grid-column data-header="首次参加工作时间错误描述" data-name="careerStartDateDesc"></k-grid-column>
		<k-grid-column data-header="首次从事理财业务时间错误描述" data-name="wealthStartDateDesc"></k-grid-column>
		<k-grid-column data-header="专业技术职称错误描述" data-name="professQualyLevelDesc"></k-grid-column>
		<k-grid-column data-header="理财专业证书错误描述" data-name="wealthCerDesc"></k-grid-column>
		<k-grid-column data-header="理财登记培训证书编号错误描述" data-name="registCerNoDesc"></k-grid-column>
		<k-grid-column data-header="所获奖励错误描述" data-name="rewardDesc"></k-grid-column>
		<k-grid-column data-header="办公电话错误描述" data-name="telphoneDesc"></k-grid-column>
		<k-grid-column data-header="移动电话错误描述" data-name="mobileDesc"></k-grid-column>
		<k-grid-column data-header="电子邮箱错误描述" data-name="emailDesc"></k-grid-column>
		<k-grid-column data-header="登记业务分类错误描述" data-name="registerClassifyDesc"></k-grid-column>
		<k-grid-column data-header="登记人员类别错误描述" data-name="registTypeDesc"></k-grid-column>
		<k-grid-column data-header="备注错误描述" data-name="detailsDesc"></k-grid-column>
		<k-grid-column data-header="登记流水号" data-name="registerSerno"></k-grid-column>
		<k-grid-column data-header="导入日期" data-name="impDate" data-type="date"></k-grid-column>
		<k-grid-column data-header="投资者登记日期" data-name="registerDate" data-type="date"></k-grid-column>
		<k-grid-column data-header="报送状态" data-dict="report_status" data-name="registerStatus" ></k-grid-column>
      </k-grid>
    </div>

	<!--    添加从业人员登记错误信息弹出框   -->
	<k-popup ref="addPractyRegistInfoErrPopup" data-title="新增">
    	<k-form ref="addPractyRegistInfoErrForm" :data-col="2">
			<k-form-item label="从业人员类型错误">
	        	<k-field-text v-model="formData.professionDesc"/>
	     	</k-form-item>
			<k-form-item label="姓名错误">
	        	<k-field-text v-model="formData.nameDesc"/>
	     	</k-form-item>
			<k-form-item label="性别错误">
	        	<k-field-text v-model="formData.sexDesc"/>
	     	</k-form-item>
			<k-form-item label="发行机构代码错误">
	        	<k-field-text v-model="formData.bankCodeDesc"/>
	     	</k-form-item>
			<k-form-item label="身份证号错误">
	        	<k-field-text v-model="formData.idCodeDesc"/>
	     	</k-form-item>
			<k-form-item label="所属总行或分行错误">
	        	<k-field-text v-model="formData.issBranchTypeDesc"/>
	     	</k-form-item>
			<k-form-item label="所属区域错误">
	        	<k-field-text v-model="formData.regionDesc"/>
	     	</k-form-item>
			<k-form-item label="具体单位名称错误">
	        	<k-field-text v-model="formData.firmNameDesc"/>
	     	</k-form-item>
			<k-form-item label="所属部门错误">
	        	<k-field-text v-model="formData.departmentDesc"/>
	     	</k-form-item>
			<k-form-item label="职位错误">
	        	<k-field-text v-model="formData.postDesc"/>
	     	</k-form-item>
			<k-form-item label="学历 错误">
	        	<k-field-text v-model="formData.educationDesc"/>
	     	</k-form-item>
			<k-form-item label="学位错误">
	        	<k-field-text v-model="formData.degreeDesc"/>
	     	</k-form-item>
			<k-form-item label="首次参加工作时间错误">
	        	<k-field-text v-model="formData.careerStartDateDesc"/>
	     	</k-form-item>
			<k-form-item label="首次从事理财业务时间错误">
	        	<k-field-text v-model="formData.wealthStartDateDesc"/>
	     	</k-form-item>
			<k-form-item label="专业技术职称错误">
	        	<k-field-text v-model="formData.professQualyLevelDesc"/>
	     	</k-form-item>
			<k-form-item label="理财专业证书错误">
	        	<k-field-text v-model="formData.wealthCerDesc"/>
	     	</k-form-item>
			<k-form-item label="理财登记培训证书编号错误">
	        	<k-field-text v-model="formData.registCerNoDesc"/>
	     	</k-form-item>
			<k-form-item label="所获奖励错误">
	        	<k-field-text v-model="formData.rewardDesc"/>
	     	</k-form-item>
			<k-form-item label="办公电话错误">
	        	<k-field-text v-model="formData.telphoneDesc"/>
	     	</k-form-item>
			<k-form-item label="移动电话错误">
	        	<k-field-text v-model="formData.mobileDesc"/>
	     	</k-form-item>
			<k-form-item label="电子邮箱错误">
	        	<k-field-text v-model="formData.emailDesc"/>
	     	</k-form-item>
			<k-form-item label="登记业务分类错误">
	        	<k-field-text v-model="formData.registerClassifyDesc"/>
	     	</k-form-item>
			<k-form-item label="登记人员类别错误">
	        	<k-field-text v-model="formData.registTypeDesc"/>
	     	</k-form-item>
			<k-form-item label="备注错误">
	        	<k-field-text v-model="formData.detailsDesc"/>
	     	</k-form-item>
			<k-form-item label="登记流水号">
	        	<k-field-text v-model="formData.registerSerno"/>
	     	</k-form-item>
			<k-form-item label="导入日期">
	        	<k-field-date v-model="formData.impDate"/>
	     	</k-form-item>
			<k-form-item label="投资者登记日期">
	        	<k-field-date v-model="formData.registerDate"/>
	     	</k-form-item>
			<k-form-item label="登记状态（0：未处理，1：已登记，2：登记失败）">
	        	<k-field-select v-model="formData.registerStatus" data-dict="tr_register_status"/>
	     	</k-form-item>

	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="PractyRegistInfoErr.addPractyRegistInfo" data-from="addPractyRegistInfoErrForm"
		               :data-model="formData" data-target="PractyRegistInfoErrGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改从业人员登记错误信息弹出框   -->
	<k-popup ref="editPractyRegistInfoErrPopup" data-title="修改">
	  <k-form ref="editPractyRegistInfoErrForm" :data-col="2">
		<k-form-item label="从业人员类型错误">
        	<k-field-text v-model="formData.professionDesc"/>
     	</k-form-item>
		<k-form-item label="姓名错误">
        	<k-field-text v-model="formData.nameDesc"/>
     	</k-form-item>
		<k-form-item label="性别错误">
        	<k-field-text v-model="formData.sexDesc"/>
     	</k-form-item>
		<k-form-item label="发行机构代码错误">
        	<k-field-text v-model="formData.bankCodeDesc"/>
     	</k-form-item>
		<k-form-item label="身份证号错误">
        	<k-field-text v-model="formData.idCodeDesc"/>
     	</k-form-item>
		<k-form-item label="所属总行或分行错误">
        	<k-field-text v-model="formData.issBranchTypeDesc"/>
     	</k-form-item>
		<k-form-item label="所属区域错误">
        	<k-field-text v-model="formData.regionDesc"/>
     	</k-form-item>
		<k-form-item label="具体单位名称错误">
        	<k-field-text v-model="formData.firmNameDesc"/>
     	</k-form-item>
		<k-form-item label="所属部门错误">
        	<k-field-text v-model="formData.departmentDesc"/>
     	</k-form-item>
		<k-form-item label="职位错误">
        	<k-field-text v-model="formData.postDesc"/>
     	</k-form-item>
		<k-form-item label="学历错误">
        	<k-field-text v-model="formData.educationDesc"/>
     	</k-form-item>
		<k-form-item label="学位错误">
        	<k-field-text v-model="formData.degreeDesc"/>
     	</k-form-item>
		<k-form-item label="首次参加工作时间错误">
        	<k-field-text v-model="formData.careerStartDateDesc"/>
     	</k-form-item>
		<k-form-item label="首次从事理财业务时间错误">
        	<k-field-text v-model="formData.wealthStartDateDesc"/>
     	</k-form-item>
		<k-form-item label="专业技术职称错误">
        	<k-field-text v-model="formData.professQualyLevelDesc"/>
     	</k-form-item>
		<k-form-item label="理财专业证书错误">
        	<k-field-text v-model="formData.wealthCerDesc"/>
     	</k-form-item>
		<k-form-item label="理财登记培训证书编号错误">
        	<k-field-text v-model="formData.registCerNoDesc"/>
     	</k-form-item>
		<k-form-item label="所获奖励错误">
        	<k-field-text v-model="formData.rewardDesc"/>
     	</k-form-item>
		<k-form-item label="办公电话错误">
        	<k-field-text v-model="formData.telphoneDesc"/>
     	</k-form-item>
		<k-form-item label="移动电话错误">
        	<k-field-text v-model="formData.mobileDesc"/>
     	</k-form-item>
		<k-form-item label="电子邮箱错误">
        	<k-field-text v-model="formData.emailDesc"/>
     	</k-form-item>
		<k-form-item label="登记业务分类错误">
        	<k-field-text v-model="formData.registerClassifyDesc"/>
     	</k-form-item>
		<k-form-item label="登记人员类别错误">
        	<k-field-text v-model="formData.registTypeDesc"/>
     	</k-form-item>
		<k-form-item label="备注错误">
        	<k-field-text v-model="formData.detailsDesc"/>
     	</k-form-item>
		<k-form-item label="登记流水号">
        	<k-field-text v-model="formData.registerSerno"/>
     	</k-form-item>
		<k-form-item label="导入日期">
        	<k-field-date v-model="formData.impDate"/>
     	</k-form-item>
		<k-form-item label="投资者登记日期">
        	<k-field-date v-model="formData.registerDate"/>
     	</k-form-item>
		<k-form-item label="登记状态（0：未处理，1：已登记，2：登记失败）">
        	<k-field-select v-model="formData.registerStatus" data-dict="tr_register_status"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="PractyRegistInfoErr.updatePractyRegistInfo" data-from="editPractyRegistInfoErrForm"
	        :data-model="formData" data-target="PractyRegistInfoErrGrid">
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
  export default {
    name: "PractyRegistInfoErr",
    data() {
      return {
        formData: {},
        selectRowData: {},
        searchParam:{}, //查询条件
        BreathDay:[],
      };
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      }
    },
    watch: {
      // 查询导入日期
      BreathDay() {
        console.log(this.BreathDay);
        this.$set(this.searchParam, 'startDate', this.BreathDay == null ? '' : this.BreathDay[0]);
        this.$set(this.searchParam, 'endDate', this.BreathDay == null ? '' : this.BreathDay[1]);
      },
    }
  };
</script>
