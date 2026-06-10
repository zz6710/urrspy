
<template>
  <div class="py-page">
          <k-form-search-customize ref="searchRef" data-model-name="ZG02" data-target="ZG02Grid" v-model="queryParam" data-label-width="80px">
            <k-form-item label="报送日期">
             <k-field-date v-model="queryParamDateRange" data-type="daterange"  data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd" :data-allowblank="false"/>
           </k-form-item>
           <k-form-item label="产品代码_资管" data-label-width="120px">
             <k-field-text v-model="prodSearchParam.prodCd" data-validate-type="text"/>
           </k-form-item>
            <k-form-item label="地区">
              <k-field-select v-model="prodSearchParam.clcSourceZonCd" data-action="ZG02.addclcSourceZonCdDict" :dataRemote="true"
                :data-params="{'clcSourceCustTyp':'','clcSourceZonCd':''}"
                data-value-field="VALUE"
                data-display-field="TEXT"
                data-dict-type="1" />
            </k-form-item>
            <k-form-item label="客户类型">
              <k-field-select v-model="prodSearchParam.clcSourceCustTyp" data-dict="pbc_org_typ" data-dict-type="1"/>
            </k-form-item>
          </k-form-search-customize>
          <div class="py-page-container">
            <div class="table-top-btns">
              <div class="left">
                <k-btn ref="assetsZG02Send" slot="button" class="btn-custom-plain"
              data-descript="数据报送" data-size="small" @click="creatZipFile('ZG02')">
               <md-icon>cloud_download</md-icon>
              数据报送
            </k-btn>
            <k-btn slot="button" style="width: 120px" class="btn-custom-plain" data-target="ZG02Grid" :data-export-name="exportName('ZG02', '资管产品初始募集信息')"
              data-descript="报送数据导出" data-functype="EXPORT" data-size="small"
              data-url="ZG02.findZG02s" data-export-form="searchRef">
              <md-icon>cloud_download</md-icon>
              报送数据导出
            </k-btn>
            <k-btn slot="button" style="width: 120px" data-functype="POPUP" class="btn-custom-plain"
                data-target="addPopup">
              <md-icon>cloud_upload</md-icon>
              报送数据导入
            </k-btn>
          </div>
          </div>
            <k-grid ref="ZG02Grid" @data-row-select="selectRow" data-action="ZG02.findZG02s" data-fixed="right" data-operate-width="120px" :data-autoload="false" data-dict-type="1">
                <k-grid-column data-align="left"  data-header="报送日期" data-name="reportDate" data-width="100" data-export="false"></k-grid-column>
                <k-grid-column data-align="left"  data-header="产品代码_资管" data-name="prodCd"></k-grid-column>
                <k-grid-column data-align="left"  data-header="币种_资管02表" data-name="clcCcy"></k-grid-column>
                <k-grid-column data-align="left"  data-header="地区" data-name="clcSourceZonCd"></k-grid-column>
                <k-grid-column data-align="left"  data-header="客户类型_资管" data-name="clcSourceCustTyp" data-dict="pbc_org_typ"></k-grid-column>
                <k-grid-column data-align="left"  data-header="初始募集金额" data-name="clcAmtBegin" ></k-grid-column>
                <k-grid-column data-align="left" data-header="初始募集金额折人民币" data-name="clcAmtBeginCny" data-width="160"></k-grid-column>
                <k-grid-column data-align="left"  data-header="初始募集份额" data-name="clcLotBegin"></k-grid-column>
                <k-grid-column data-align="left"  data-header="产品初始单位净值" data-name="untNav"></k-grid-column>
                <k-grid-column data-align="left"  data-header="产品初始单位净值折人民币" data-name="untNavCny" data-width="180"></k-grid-column>

                <template slot="operate" slot-scope="scope">
                <k-btn class="btn-custom-text" data-descript="修改" data-functype="POPUP" data-size="mini"
                    data-target="editZG02Popup">
                    修改
                </k-btn>
                <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="ZG02.deleteZG02" data-size="mini"
                    data-type="danger" data-target="ZG02Grid" :data-confirm="true" data-descript="删除">
                    删除
                </k-btn>
                </template>
            </k-grid>
          </div>
          <!--    修改资管产品初始募集信息弹出框   -->
          <k-popup ref="editZG02Popup" data-title="修改">
            <k-form ref="editZG02Form" :data-col="2" isFormBodyScreen data-label-width="190px">
                <k-form-item label="数据日期" :class="[handleItemDiff('theoryReportStartDate')]">
                    <k-field-date v-model="formData.theoryReportStartDate" data-type="date"  data-value-format="yyyyMMdd" :data-allowblank="true" :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="产品代码_资管" :class="[handleItemDiff('prodCd')]">
                    <k-field-text v-model="formData.prodCd"  :data-allowblank="false" :data-disabled="true"/>
                </k-form-item>

                <k-form-item label="币种_资管02表" :class="[handleItemDiff('clcCcy')]">
                    <k-field-text v-model="formData.clcCcy" :data-allowblank="false" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="地区" :class="[handleItemDiff('clcSourceZonCdText')]">
                     <k-field-select v-model="formData.clcSourceZonCdText" data-action="ZG02.addclcSourceZonCdDict" :dataRemote="true"  :data-params="{'clcSourceCustTyp':formData.clcSourceCustTyp,'clcSourceZonCd':formData.clcSourceZonCdText}"
                                                data-value-field="VALUE" data-display-field="VALUE,TEXT" />
                </k-form-item>
                <k-form-item label="客户类型_资管" :class="[handleItemDiff('clcSourceCustTyp')]">
                    <k-field-select v-model="formData.clcSourceCustTyp" :data-disabled="false" data-dict="pbc_org_typ" data-dict-type="1"/>
                </k-form-item>
                <k-form-item label="初始募集金额" :class="[handleItemDiff('clcAmtBegin')]">
                    <k-field-text v-model="formData.clcAmtBegin" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/>
                </k-form-item>
                <k-form-item label="初始募集金额折人民币" :class="[handleItemDiff('clcAmtBeginCny')]">
                    <k-field-text v-model="formData.clcAmtBeginCny" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/>
                </k-form-item>
                <k-form-item label="初始募集份额" :class="[handleItemDiff('clcLotBegin')]">
                    <k-field-text v-model="formData.clcLotBegin" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/>
                </k-form-item>
              <k-form-item label="产品初始单位净值" :class="[handleItemDiff('untNav')]">
                <k-field-text v-model="formData.untNav"  :data-max-length="22" data-digits="2"/>
              </k-form-item>
              <k-form-item label="产品初始单位净值折人民币" :class="[handleItemDiff('untNavCny')]">
                <k-field-text v-model="formData.untNavCny"  :data-max-length="22" data-digits="2"/>
              </k-form-item>


                <k-form-footer data-align="center" slot="footer">
                <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ZG02.updateZG02" data-from="editZG02Form"
                    :data-model="formData" data-target="ZG02Grid" :handle-before="handleBefore">
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
            data-upload-url="/upload/server/RptApp/rhzg/uploadZG02.json">
        </k-field-excel-upload>
      </k-form-item>
      <k-form-footer data-align="center">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="ZG02Grid" ref="submitBtn"
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
    name: "M07RHZG02",
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
                clcSourceZonCd:'',
                clcSourceCustTyp:'',
            },
            formData: {},
            formDataCopy: {},
            queryParamDateRange: [],
            directedData:{},
            nowDate:'',
        }
    },
    created() {
          this.getNowDate();
          if(this.$route.query.directedData && this.$route.query.directedData.dataId){
               this.directedData = this.$route.query.directedData
               this.prodSearchParam.id = this.$route.query.directedData.dataId;
               this.$nextTick(()=>{
                 this.$refs.ZG02Grid.load(this.prodSearchParam);
              });
          }
        },
    activated() {
          if(this.$route.query.directedData && this.$route.query.directedData.dataId){
               this.directedData = this.$route.query.directedData
               this.prodSearchParam.id = this.$route.query.directedData.dataId;
               this.$nextTick(()=>{
                 this.$refs.ZG02Grid.load(this.prodSearchParam);
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
             'clcSourceZonCd': this.prodSearchParam.clcSourceZonCd,
             'clcSourceCustTyp': this.prodSearchParam.clcSourceCustTyp,
        }
      }
    },
    methods: {
      handleBefore() {
        if (this.formNoChangeCb()) {
          this.$refs.editZG02Popup.close();
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
              this.$refs.ZG02Grid.load(this.queryParam);
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
          this.$refs.ZG02Grid.load(this.prodSearchParam)

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
      },
  }

}
</script>
