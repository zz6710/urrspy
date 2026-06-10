<template>
  <div>
    <div>
      <k-form-search-customize v-model="queryParam"  data-target="t8AffiliatedPersonGrid">
        <k-form-item label="法人名称">
          <k-field-text v-model="searchParam.nameOperson"></k-field-text>
        </k-form-item>
        <k-form-item label="生效日期">
          <k-field-date v-model="searchParam.effectiveDate" data-type="daterange" ></k-field-date>
        </k-form-item>
        <k-form-item label="失效日期">
          <k-field-date v-model="searchParam.expiryDate" data-type="daterange" ></k-field-date>
        </k-form-item>
        <k-form-item label="关联方类型">
          <k-field-select v-model="searchParam.affiliatedType"  data-dict="related_party_trans"></k-field-select>
        </k-form-item>
        <k-form-item label="所属托管行">
          <k-field-text v-model="searchParam.truteeName" ></k-field-text>
        </k-form-item>
        <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}"
               slot="button" data-target="addT8AffiliatedPersonPopup">
          <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
        <k-btn slot="button" class="md-danger"  data-functype="POPUP" data-target="initPopup" :data-handler="clean"
               v-if="global.isShowAuthorityButton('T8AffiliatedPerson.importT8ProdPrice')">
          <md-icon>backup</md-icon>
          导入
        </k-btn>

        <k-btn slot="button" style="width: 120px" class="btn-custom-primary" :data-download-name="'关联法人导入模板'"
               data-descript="下载模板" data-functype="DOWNLOAD"
               data-url="/download/server/DpsApp/downloadScheduleTemp.json">
          <md-icon>cloud_download</md-icon>
          下载模板
        </k-btn>
        <k-btn class="btn-custom-primary" style="width: 140px" data-functype="POPUP" :data-handler="()=>this.formData={}"  data-descript="定期报告关联方交易数据生成"
               slot="button" data-target="addT8AffiliatedPersonBasePopup">
          <md-icon md-src="/static/svg/add.svg" />关联方交易数据生成 </k-btn>
      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="t8AffiliatedPersonGrid" @data-row-select="selectRow" data-action="T8AffiliatedPerson.findT8AffiliatedPersons" data-fixed="right" data-operate-width="250px">
		<k-grid-column data-header="id" data-name="id" data-hidden="true"></k-grid-column>
		<k-grid-column data-header="法人名称" data-name="nameOperson"></k-grid-column>
		<k-grid-column data-header="生效日期" data-name="effectiveDate" data-type="date"></k-grid-column>
		<k-grid-column data-header="失效日期" data-name="expiryDate"  data-type="date"></k-grid-column>
		<k-grid-column data-header="关联方类型" data-name="affiliatedType" data-dict="related_party_trans"></k-grid-column>
		<k-grid-column data-header="所属托管行" data-name="truteeName" ></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-plain" data-descript="修改" data-functype="POPUP" data-size="mini"
                 data-target="editT8AffiliatedPersonPopup" >
            修改
          </k-btn>
          <k-btn class="md-danger" data-functype="SUBMIT" data-action="T8AffiliatedPerson.deleteT8AffiliatedPerson" data-size="mini"
                 data-type="danger" data-target="t8AffiliatedPersonGrid" :data-confirm="true" data-descript="删除">
            删除
          </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加T8AffiliatedPerson弹出框   -->
	<k-popup ref="addT8AffiliatedPersonPopup" data-title="新增" :data-dialog-drag="true">
    	<k-form ref="addT8AffiliatedPersonForm" :data-col="2">
			<k-form-item label="id" :hidden="true">
	        	<k-field-text v-model="formData.id"/>
	     	</k-form-item>
			<k-form-item label="法人名称">
	        	<k-field-text v-model="formData.nameOperson" :data-max-length="128" :data-allowblank="false" />
	     	</k-form-item>
			<k-form-item label="生效日期">
	        	<k-field-date v-model="formData.effectiveDate" :data-allowblank="false" :data-max-value="this.formData.expiryDate==''||this.formData.expiryDate==null?'20991231':this.formData.expiryDate+')'" />
	     	</k-form-item>
			<k-form-item label="失效日期">
	        	<k-field-date v-model="formData.expiryDate"  :data-allowblank="false"
                          :data-min-value="this.formData.effectiveDate==''||this.formData.effectiveDate==null?'('+this.nowdate:'('+this.formData.effectiveDate" />
	     	</k-form-item>
			<k-form-item label="关联方类型">
	        	<k-field-select  v-model="formData.affiliatedType"   data-dict="related_party_trans"/>
	     	</k-form-item>
			<k-form-item label="所属托管行">
	        	<k-field-text  v-model="formData.truteeName"   :data-max-length="256"/>
	     	</k-form-item>

	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8AffiliatedPerson.addT8AffiliatedPerson" data-from="addT8AffiliatedPersonForm"
		               :data-model="formData" data-target="t8AffiliatedPersonGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改T8AffiliatedPerson弹出框   -->
	<k-popup ref="editT8AffiliatedPersonPopup" data-title="修改">
	  <k-form ref="editT8AffiliatedPersonForm" :data-col="2">
		<k-form-item label="id" :hidden="true">
        	<k-field-text v-model="formData.id"/>
     	</k-form-item>
      <k-form-item label="法人名称">
        <k-field-text v-model="formData.nameOperson" :data-max-length="128" :data-allowblank="false" />
      </k-form-item>
      <k-form-item label="生效日期">
        <k-field-date v-model="formData.effectiveDate" :data-allowblank="false"
                      :data-max-value="formData.expiryDate=='' || formData.expiryDate ==null?'29991230':formData.expiryDate"/>
      </k-form-item>
		<k-form-item label="失效日期">
        	<k-field-date v-model="formData.expiryDate"  :data-allowblank="false"
                        :data-min-value="this.formData.effectiveDate==''||this.formData.effectiveDate==null?this.nowdate:this.formData.effectiveDate"/>
     	</k-form-item>
     	<k-form-item label="关联方类型">
            <k-field-select  v-model="formData.affiliatedType"   data-dict="related_party_trans"/>
        </k-form-item>
      <k-form-item label="所属托管行">
            <k-field-text  v-model="formData.truteeName"   :data-max-length="256"/>
        </k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8AffiliatedPerson.updateT8AffiliatedPerson" data-from="editT8AffiliatedPersonForm"
	        :data-model="formData" data-target="t8AffiliatedPersonGrid">
	        <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
	      </k-btn>
	      <k-btn class="btn-custom-plain" data-functype="CLOSE">
	        <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	    </k-form-footer>
	  </k-form>
	</k-popup>
    <k-popup ref="initPopup" data-title="数据导入" >
      <k-form class="my-form" ref="addT8AffiliatedPersonForm"  >
<!--          <k-form-item label="开始日期">-->
<!--            <k-field-date v-model="formData.affilate_start_date"   data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd" :data-allowblank="false"/>-->
<!--          </k-form-item>-->
<!--          <k-form-item label="结束日期">-->
<!--            <k-field-date v-model="formData.affilate_end_date"   data-date-format="yyyyMMdd" data-value-format="yyyyMMdd" :data-allowblank="false"/>-->
<!--          </k-form-item>-->
        <k-form-item label="附件" data-ui="element" data-input-width="500px">

          <k-field-upload data-type="file" ref="uploadRef" :data-multiple="true" :data-limit=1
                          data-accept=".xlsx,.xls"
                          :data-error="onSubmitErrorLegal" :data-success="onSubmitDocSuccessLegal"
                          data-upload-url= "/upload/server/DpsApp/priceTempUpload.json" :data-auto-upload="false" >
          </k-field-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="prodInfoGrid" ref="submitBtn" :data-auto-upload="false"
                 :data-handler="submitUploadParamLegal" >
            <span v-show="showSubmitBtn">确定</span>
            <i v-show="!showSubmitBtn" class="el-icon-loading"/>
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
    <!--    关联方交易数据生成    -->
    <k-popup ref="addT8AffiliatedPersonBasePopup" data-title="数据生成" :data-dialog-drag="true">
      <k-form ref="addT8AffiliatedPersonBaseForm" :data-col="2">
        <k-form-item label="开始日期">
          <k-field-date v-model="formData.effectiveDate"   data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="结束日期">
          <k-field-date v-model="formData.expiryDate"   data-date-format="yyyyMMdd" data-value-format="yyyyMMdd" :data-allowblank="false"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"  data-from="addT8AffiliatedPersonBasePopup"  ref="batchSubmitButton"
                 :data-model="formData" data-target="t8AffiliatedPersonGrid" @click="batchSubmitToDealExecute">
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
  import Tools from "@/utils/tools";
  export default {
    name: "T8AffiliatedPerson",
    data() {
      return {
        formData: {
          effectiveDate:'',
          expiryDate:'',
        },
        selectRowData: {},
        searchParam:{
          nameOperson:'',
          effectiveDate:'',
          expiryDate:'',
          affiliatedType:'',
          truteeName:'',
        },
         effectiveDate:[],//生效日期
         expiryDate:[],//失效日期
        nowdate:'',//传递值使用
        showSubmitBtn : true,
        fileName : {fileName : '关联法人导入模板.xlsx'},
        dataDesc:{},//导入模板查询数据库生效日期最大日期
      };
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      },
      submitUploadParamLegal(){
          //文件上传校验
                  let validate = this.$refs.addT8AffiliatedPersonForm.validate();
                  if (validate) {
                    let formData = { beginDate: this.beginDate};
                    let temp = document.getElementsByClassName('upload-demo');
                    let lis = temp[0].childNodes[1].childNodes.length;
                    if (lis > 0) {
                      this.$refs.uploadRef.upload(formData);
                    } else {
                      this.$message.error("上传文件不能为空!");
                      return false;
                    }
                  }
      },
      onSubmitErrorLegal() {
        this.$refs.uploadRef.doReset();
      },

      onSubmitDocSuccessLegal() {
        this.$refs.uploadRef.doReset();
        this.$refs.initPopup.close();
      this.$refs.t8AffiliatedPersonGrid.load(this.queryParam);
      },
      clean(){
        this.formData.effectiveDate='';
        this.httpUtil.comnQuery({
          action: 'T8AffiliatedPerson.findDesc',
          params: {},
        }).then(data => {
          this.dataDesc=data.rows[0].effectiveDate;
        })
      },
      batchSubmitToDealExecute(){
            this.httpUtil.ajax({
              url:"/download/server/DpsApp/handelBaseData.json",
              params: this.formData
            }).then(res=>{
              this.$refs.batchSubmitButton.setIconStyle(1, []);
              this.$refs.addT8AffiliatedPersonBasePopup.close();
              this.$refs.t8AffiliatedPersonGrid.load();
              Tools.alert(res.returnmsg);
            })
      },
    },
    computed:{
      queryParam(){
        return {
          'nameOperson' :  this.searchParam.nameOperson?this.searchParam.nameOperson:null , //法人名称
          'affiliatedType' :  this.searchParam.affiliatedType?this.searchParam.affiliatedType:null , //关联方类型
          'truteeName' :  this.searchParam.truteeName?this.searchParam.truteeName:null , //所属托管行
          'effectiveDateStart' : this.searchParam.effectiveDate ? this.searchParam.effectiveDate[0] : null,//生效日期开始
          'effectiveDateEnd' : this.searchParam.effectiveDate ? this.searchParam.effectiveDate[1] : null,//生效日期结束
          'expiryDateStart' : this.searchParam.expiryDate ? this.searchParam.expiryDate[0] : null,//失效日期开始
          'expiryDateEnd' : this.searchParam.expiryDate ? this.searchParam.expiryDate[1] : null,//失效日期结束
        }
      }
    },
    created(){
      this.global.getProdUser('');
      this.$nextTick(()=>{
        var now = new Date();
        var year=now.getFullYear();
        var month=now.getMonth()+1;
        var date=now.getDate();
        if(month<10){
          month = '0'+month
        }if(date<10){
          date = '0'+date
        }
        this.nowdate=year+""+month+""+date;

      });
    },

  };
</script>
