<template>
  <div class="py-page">
    <div>
       <k-form-search-customize data-model-name="PractyRegistInfoh" data-target="PractyRegistInfohGrid" data-label-width="80px" v-model = "searchParam">
            <k-form-item label="登记日期">
              <k-field-date v-model="BreathDay" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
            </k-form-item>
             <k-form-item label="姓名">
               <k-field-text v-model="searchParam.name"/>
             </k-form-item>
             <k-form-item label="身份证号">
               <k-field-text v-model="searchParam.idCode"/>
             </k-form-item>
             <!--<k-form-item label="登记状态">
                 <k-field-select v-model="searchParam.registerStatus" data-dict="tr_register_status"/>
              </k-form-item>-->
           </k-form-search-customize>
    </div>

    <div class="py-page-container">
      <k-grid ref="PractyRegistInfohGrid" @data-row-select="selectRow"  data-operate-column="false" data-action="PractyRegistInfoh.findPractyRegistInfos" >
	 <k-grid-column data-align="left" data-header="从业人员类型" data-name="profession"      data-dict="subm_tr_profession"  data-width="120"  data-export="false"></k-grid-column>
     <k-grid-column data-align="left" data-header="姓名" data-name="name"  data-width="100"></k-grid-column>
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
     <k-grid-column data-align="left" data-header="理财专业证书" data-name="wealthCer"  data-width="150"  :data-multiple="true"></k-grid-column>
     <k-grid-column data-align="left" data-header="理财登记培训证书编号" data-name="registCerNo" data-width="120"></k-grid-column>
     <k-grid-column data-align="left" data-header="所获奖励" data-name="reward" data-width="120"></k-grid-column>
     <k-grid-column data-align="left" data-header="办公电话" data-name="telphone" data-width="80"></k-grid-column>
     <k-grid-column data-align="left" data-header="移动电话" data-name="mobile" data-width="100"></k-grid-column>
     <k-grid-column data-align="left" data-header="电子邮箱" data-name="email" data-width="120"></k-grid-column>
     <k-grid-column data-align="left" data-header="登记业务分类" data-name="registerClassify"  data-dict="subm_t8_register_type" data-width="180"></k-grid-column>
     <k-grid-column data-align="left" data-header="登记人员类别" data-name="registType"  data-dict="subm_t8_register_person" data-width="200"></k-grid-column>
     <k-grid-column data-align="left" data-header="备注" data-name="details" data-width="150"></k-grid-column>
     <k-grid-column data-align="left" data-header="登记流水号" data-name="registerSerno" data-width="250" data-export="false"></k-grid-column>
		 <k-grid-column data-align="left" data-header="登记日期" data-name="registerDate" data-width="100"  ></k-grid-column>
		 <k-grid-column data-align="left" data-header="理论报送起始日期" data-name="theoryReportStartDate" data-width="100" data-export="false"  ></k-grid-column>
     <k-grid-column data-align="left" data-header="理论报送截止日期" data-name="theoryReportEndDate" data-width="100" data-export="false"  ></k-grid-column>
     <k-grid-column data-align="left" data-header="新增日期" data-name="createDate" data-width="100"  ></k-grid-column>
      </k-grid>
    </div>

  </div>
</template>

<script>
  export default {
    name: "PractyRegistInfoh",
    data() {
      return {
        formData: {},
        selectRowData: {},
        searchParam:{},
        RegisterDate:[],
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
      //查询起息日
      BreathDay() {
        this.$set(this.searchParam, 'startDate', this.BreathDay == null ? '' : this.BreathDay[0]);
        this.$set(this.searchParam, 'endDate', this.BreathDay == null ? '' : this.BreathDay[1]);
      },
    }
  };
</script>
