
<template>
  <div class="py-page">

          <k-form-search-customize ref="searchRef" data-model-name="ZG03" data-target="ZG03Grid" v-model="queryParam">
            <k-form-item label="报送日期">
              <k-field-date v-model="queryParamDateRange" data-type="daterange"  data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd" :data-allowblank="false"/>
            </k-form-item>
            <k-form-item label="产品代码_资管">
              <k-field-text v-model="prodSearchParam.prodCd" data-validate-type="text"/>
            </k-form-item>
          </k-form-search-customize>
          <div class="py-page-container">
            <div class="table-top-btns">
              <div class="left">
                <k-btn ref="assetsZG03Send" slot="button" class="btn-custom-plain"
              data-descript="数据报送" data-size="small" @click="creatZipFile('ZG03')">
               <md-icon>cloud_download</md-icon>
              数据报送
            </k-btn>
                <k-btn slot="button" class="btn-custom-plain" data-target="ZG03Grid" :data-export-name="exportName('ZG03', '资管产品终止信息')"
              data-descript="报送数据导出" data-functype="EXPORT" data-size="small"
              data-url="ZG03.findZG03s" data-export-form="searchRef">
              <md-icon>cloud_download</md-icon>
              报送数据导出
            </k-btn>
                <k-btn slot="button" data-functype="POPUP" class="btn-custom-plain"
                data-target="addPopup">
              <md-icon>cloud_upload</md-icon>
              报送数据导入
            </k-btn>
          </div>
          </div>
            <k-grid ref="ZG03Grid" @data-row-select="selectRow" data-action="ZG03.findZG03s" data-fixed="right" data-operate-width="120px" :data-autoload="false">
                <k-grid-column data-align="left" data-header="报送日期" data-name="reportDate" data-width="100"data-export="false"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品代码_资管" data-name="prodCd"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品实际终止日期" data-name="prodRelEndDt"  data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="币种_资管0304表" data-name="transCcy"></k-grid-column>
                <k-grid-column data-align="left" data-header="发行机构实现收入" data-name="isuOrgErn" data-type="money"></k-grid-column>
                <k-grid-column data-align="left" data-header="发行机构实现收入折人民币" data-name="isuOrgErnCny" data-type="money"></k-grid-column>
                <k-grid-column data-align="left" data-header="兑付客户收益" data-name="custCallErn" data-type="money"></k-grid-column>
                <k-grid-column data-align="left" data-header="兑付客户收益折人民币" data-name="custCallErnCny" data-type="money"></k-grid-column>
                <k-grid-column data-align="left" data-header="兑付客户收益率" data-name="custCallErnRate"></k-grid-column>

                <template slot="operate" slot-scope="scope">
                <k-btn class="btn-custom-text" data-descript="修改" data-functype="POPUP" data-size="mini"
                    data-target="editZG03Popup">
                    修改
                </k-btn>
                <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="ZG03.deleteZG03" data-size="mini"
                    data-type="danger" data-target="ZG03Grid" :data-confirm="true" data-descript="删除">
                    删除
                </k-btn>
                </template>
            </k-grid>
          </div>
          <!--    修改资管产品终止信息弹出框   -->
          <k-popup ref="editZG03Popup" data-title="修改">
            <k-form ref="editZG03Form" :data-col="2" isFormBodyScreen>
                <k-form-item label="数据日期" :class="[handleItemDiff('theoryReportStartDate')]">
                    <k-field-date v-model="formData.theoryReportStartDate" :data-allowblank="true" data-type="date" data-value-format="yyyyMMdd"   :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="产品代码_资管" :class="[handleItemDiff('prodCd')]">
                    <k-field-text v-model="formData.prodCd"  :data-allowblank="false" :data-disabled="true"/>
                </k-form-item>

                <k-form-item label="产品实际终止日期" :class="[handleItemDiff('prodRelEndDt')]">
                    <k-field-date v-model="formData.prodRelEndDt" :data-allowblank="false" data-type="date" data-value-format="yyyy-MM-dd" data-date-format="yyyy-MM-dd" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="币种_资管0304表" :class="[handleItemDiff('transCcy')]">
                    <k-field-text v-model="formData.transCcy" :data-allowblank="true" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="发行机构实现收入" :class="[handleItemDiff('isuOrgErn')]">
                    <k-field-text v-model="formData.isuOrgErn" :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/>
                </k-form-item>
                <k-form-item label="发行机构实现收入折人民币" :class="[handleItemDiff('isuOrgErnCny')]">
                    <k-field-text v-model="formData.isuOrgErnCny" :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/>
                </k-form-item>
                <k-form-item label="兑付客户收益" :class="[handleItemDiff('custCallErn')]">
                    <k-field-text v-model="formData.custCallErn" :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/>
                </k-form-item>
                <k-form-item label="兑付客户收益折人民币" :class="[handleItemDiff('custCallErnCny')]">
                    <k-field-text v-model="formData.custCallErnCny" :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/>
                </k-form-item>
                <k-form-item label="兑付客户收益率" :class="[handleItemDiff('custCallErnRate')]">
                    <k-field-text v-model="formData.custCallErnRate" :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="15" data-digits="5"/>
                </k-form-item>



                <k-form-footer data-align="center" slot="footer">
                <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ZG03.updateZG03" data-from="editZG03Form"
                    :data-model="formData" data-target="ZG03Grid" :handle-before="handleBefore">
                    <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
                </k-btn>
                <k-btn class="btn-custom-plain" data-functype="CLOSE">
                    <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
                </k-form-footer>
            </k-form>
          </k-popup>



    <k-popup ref="addPopup" title="报送数据导入" @data-opened="uploadOpened()">
    <k-form ref="addForm" data-ui="element">
      <k-form-item label="报送日期">
        <k-field-date v-model="reportDate" data-type="date"  data-date-format="yyyy-MM-dd"  data-value-format="yyyyMMdd" :data-allowblank="false"/>
      </k-form-item>
      <!--<k-form-item label="截止日期">
        <k-field-date v-model="uploadQueryDate" data-type="date"  data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd" :data-allowblank="false"/>
      </k-form-item>-->
      <k-form-item label="报送数据导入" data-ui="element" data-input-width="500px">
        <k-field-excel-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit='1'
            data-accept=".xlsx,.xls"
            :data-error="onSubmitError" :data-success="onSubmitSuccess"
            :data-auto-upload="false"
            data-upload-url="/upload/server/RptApp/rhzg/uploadZG03.json">
        </k-field-excel-upload>
      </k-form-item>
      <k-form-footer data-align="center">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="ZG03Grid" ref="submitBtn"
              :data-auto-upload="false" data-from="addForm" :data-handler="submitUploadParam">确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
      </k-form-footer>
    </k-form>
    </k-popup>

  </div>
</template>
<script>
import ZgMixin from "@/pages/report/rhzg/zgMixin.js";

export default {
    name: "M07RHZG03",
    mixins: [ZgMixin],
    data () {
        return {
            uploadBeginDate: '',
            uploadQueryDate: '',
            reportDate:'',
            activeName: '1',
            prodSearchParam: {
                prodCd: '',
                id: '',
            },
            queryParamDateRange: [],
            formData: {},
            formDataCopy: {},
            directedData:{},
            nowDate:'',
        }
    },
    created() {
      this.getNowDate();
      if(this.$route.query.directedData && this.$route.query.directedData.dataId){
           this.$set(this.prodSearchParam, 'theoryReportStartDate', '');
           this.directedData = this.$route.query.directedData
           this.prodSearchParam.id = this.$route.query.directedData.dataId;
           this.$nextTick(()=>{
              this.$refs.ZG03Grid.load(this.prodSearchParam);
           });
      }
    },
    activated() {
          if(this.$route.query.directedData && this.$route.query.directedData.dataId){
               this.$set(this.prodSearchParam, 'theoryReportStartDate', '');
               this.directedData = this.$route.query.directedData
               this.prodSearchParam.id = this.$route.query.directedData.dataId;
               this.$nextTick(()=>{
                  this.$refs.ZG03Grid.load(this.prodSearchParam);
               });
          }
     },
    mounted() {
    },
    computed: {
      queryParam () {
        return {
        'beginDate': this.queryParamDateRange ? this.queryParamDateRange[0] : null,
        'queryDate': this.queryParamDateRange ? this.queryParamDateRange[1] : null,
        'prodCd': this.prodSearchParam.prodCd,
          'id': this.prodSearchParam.id,
        }
      }
    },
    methods: {
        handleBefore() {
          if (this.formNoChangeCb()) {
            this.$refs.editZG03Popup.close();
            return false
          }
          return true
        },
        submitUploadParam() {
        //文件上传校验
        let validate = this.$refs.addForm.validate();
        if (validate) {
          let formData = { reportDate: this.reportDate}
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
      onSubmitSuccess() {
        this.$refs.uploadRef.doReset();
        this.$refs.addForm.reset();
        this.$refs.addPopup.close();
        if (this.queryParamDateRange) {
          this.$refs.ZG03Grid.load(this.queryParam);
        }
      },
      onSubmitError() {
        this.$refs.uploadRef.doReset();
        this.$refs.submitBtn.setIconStyle(1, [])
      },
      uploadOpened() {
        this.uploadBeginDate = ''
        this.uploadQueryDate = ''
        this.reportDate =''
      },


      tabClick(tab, event) {
          this.$refs.ZG03Grid.load(this.prodSearchParam)

      },
      selectRow(row, column, event) {
        this.formData = Object.assign({}, row)
        this.formDataCopy = Object.assign({}, row)
      },
      getNowDate() {
        const timeOne = new Date();
        const year = timeOne.getFullYear();
        let month = timeOne.getMonth() + 1;
        let day = timeOne.getDate();
        month = month < 10 ? '0' + month : month;
        day = day < 10 ? '0' + day : day;
        this.nowDate=year+''+month+''+day;
        this.queryParamDateRange[0] = this.nowDate;
        this.queryParamDateRange[1] = this.nowDate;
        this.$set(this.queryParam, "queryParamDateRange", this.nowDate);
      }
  }

}
</script>
