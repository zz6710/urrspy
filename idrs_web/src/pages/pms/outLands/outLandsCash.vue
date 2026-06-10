<template>
  <div>
    <div>
      <k-form-search-customize v-model="queryParam"  data-target="outLandsCashGrid">
        <k-form-item label="产品代码">
          <k-field-text v-model="searchParam.prodCd"></k-field-text>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="searchParam.prodNm" ></k-field-text>
        </k-form-item>
        <k-form-item label="数据日期">
          <k-field-date v-model="searchParam.dataDete" data-type="daterange" ></k-field-date>
        </k-form-item>
        <k-btn slot="button" class="md-danger"  data-functype="POPUP" data-target="initPopup" :data-handler="clean"
               v-if="global.isShowAuthorityButton('OutLandsCash.importOutLandsCash')">
          <md-icon>backup</md-icon>
          导入
        </k-btn>

      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="outLandsCashGrid" @data-row-select="selectRow" data-action="OutLandsCash.findOutLandsCash"  :data-autoload="true" :data-operate-column="false">
		<k-grid-column data-header="ID" data-name="id" data-hidden="true"></k-grid-column>
            			<k-grid-column data-header="产品代码" data-name="prodCd"></k-grid-column>
                  <k-grid-column data-header="产品名称" data-name="prodNm"></k-grid-column>
                  <k-grid-column data-header="境外客户当期募集金额(万元)" data-name="clcAmt"  data-type="money"></k-grid-column>
                   <k-grid-column data-header="境外客户当期兑付金额(万元)" data-name="callAmt"  data-type="money"></k-grid-column>
                  <k-grid-column data-header="数据日期" data-name="dataDate"  data-type="date" ></k-grid-column>>
      </k-grid>
    </div>

   <k-popup ref="initPopup" title="报送数据导入" @data-opened="uploadOpened()">

           <k-form ref="addForm" data-ui="element">

           <k-form-item label="数据日期">
                  <k-field-date v-model="formData.dataDt"  data-date-format="yyyy-MM-dd"  data-value-format="yyyyMMdd" :data-allowblank="false"/>
                </k-form-item>
             <k-form-item label="导入sheet页">
                 <k-field-text v-model="formData.sheetNumber" data-validate-type="number" data-min-value="0" :data-max-length="1" :data-allowblank="false"  data-default-value="2" />
             </k-form-item>
             <k-form-item label="导入起始行">
                 <k-field-text v-model="formData.rowStart" data-validate-type="number" data-min-value="(0" :data-max-length="6" :data-allowblank="false"  data-default-value="3" />
             </k-form-item>
             <k-form-item label="报送数据导入" data-ui="element" data-input-width="500px">
               <k-field-excel-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit='1'
                   data-accept=".xlsx,.xls"
                   :data-error="onSubmitError" :data-success="onSubmitDocSuccessLegal"
                   :data-auto-upload="false"
                   data-upload-url= "/upload/server/DpsApp/outLandsCaseUpload.json">
               </k-field-excel-upload>
             </k-form-item>
             <k-form-footer data-align="center">
               <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="outLandsCashGrid" ref="submitBtn"
                     :data-auto-upload="false" data-from="addForm" :data-handler="submitUploadParamLegal">确定
               </k-btn>
               <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
             </k-form-footer>
           </k-form>
           </k-popup>
  </div>
</template>

<script>
  export default {
    name: "outLandsCash",
    data() {
      return {
        formData: {
          dataDete:''
        },
        selectRowData: {},
        searchParam:{
          prodCd:'',
          prodNm:'',
          dataDete:'',
        },
         dataDete:[],
        nowdate:'',//传递值使用
        showSubmitBtn : true,
        fileName : {fileName : '资管产品境外募集余额统计表导入模板.xlsx'},
        dataDesc:{},//导入模板查询数据库生效日期最大日期
      };
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      },
      submitUploadParamLegal(){
             // this.$refs.uploadRef.upload(this.formData);
             // this.$refs.uploadRef.close();
              //文件上传校验
                     let validate = this.$refs.addForm.validate();
                     if (validate) {
                       let formData = { dataDt : this.formData.dataDt, sheetNumber: this.formData.sheetNumber, rowStart: this.formData.rowStart }
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
      this.formData.dataDt='';
      this.$refs.uploadRef.doReset();
      this.$refs.submitBtn.setIconStyle(1, [])
     },
      uploadOpened() {
      },

      onSubmitDocSuccessLegal() {
      this.formData.dataDt='';
          this.$refs.uploadRef.doReset();
          this.$refs.initPopup.reset();
          this.$refs.initPopup.close();
          this.$refs.outLandsCashGrid.load();
      },
    },
    computed:{
      queryParam(){
        return {
          'prodCd' :  this.searchParam.prodCd ,
          'prodNm' :  this.searchParam.prodNm ,
          'dataDateStart' : this.searchParam.dataDete ? this.searchParam.dataDete[0] : null,//日期开始
          'dataDateEnd' : this.searchParam.dataDete ? this.searchParam.dataDete[1] : null,//日期结束
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
