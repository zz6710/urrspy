<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="ProdStateRegistInfo"  data-target="ProdStateRegistInfoGrid" v-model="queryParam" ref="searchFormRef">
        <k-form-item label="产品登记编码">
          <k-field-text v-model="searchParam.prodRegEnc"/>
        </k-form-item>
        <k-form-item label="产品状态统计日">
            <k-field-date v-model="searchParam.startDate"   data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd" :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="报送状态">
          <k-field-select v-model="searchParam.registerStatus" data-dict="report_status"/>
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <!-- <k-btn class="md-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addProdStateRegistInfoPopup" slot="button">
            <i class="icon-add"/>新增</k-btn>-->
          <k-btn slot="button"  data-functype="POPUP" class="btn-custom-plain" data-target="uploadProdStateRegistInfoPopup">
            <md-icon>cloud_upload</md-icon>导入</k-btn>
          <k-btn slot="button"  class="btn-custom-plain" ref="exportRef" :handleBefore="handleBefore" @downSuccess="downSuccess" :data-handler="dataHandler"  data-functype="EXPORT" data-target="ProdStateRegistInfoGrid" data-excel-template ="产品状态登记.xlsx" data-excel-start-line ="2" data-template-name="产品状态登记" data-export-dict="true" :data-export-name="'产品状态登记'">
            <md-icon>cloud_download</md-icon>导出
          </k-btn>
          <k-btn slot="button" class="btn-custom-plain" :handleBefore="handleBefore" :data-handler="handleConfirmExport">
                                <md-icon>cloud_download</md-icon>确认并导出
          </k-btn>
          <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="auditPopup">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>复核
          </k-btn>
        </div>
      </div>
      <k-grid ref="ProdStateRegistInfoGrid" @data-row-select="selectRow" data-fixed="right" data-operate-width="300px" :data-autoload="false" data-action="ProdStateRegistInfo.findProdStateRegistInfos" >
        <k-grid-column  data-align="left" data-header="报送状态" data-name="registerStatus" data-dict="report_status" data-export="false"  data-width="100"></k-grid-column>
        <k-grid-column  data-align="left" data-header="发行机构代码" data-name="bankCode" data-width="120"></k-grid-column>
        <k-grid-column  data-align="left" data-header="产品登记编码" data-name="prodRegEnc" data-width="120"></k-grid-column>
        <k-grid-column  data-align="left" data-header="理财产品总资产金额(元)" data-name="totAssets"  data-width="160"></k-grid-column>
        <k-grid-column  data-align="left" data-header="理财产品杠杆率(%)" data-name="rate" data-width="140"></k-grid-column>
        <k-grid-column  data-align="left"   data-header="产品状态统计日" data-name="valdate"   data-width="140"></k-grid-column>
        <k-grid-column  data-align="left"   data-header="备注" data-name="details" data-width="150"></k-grid-column>
        <k-grid-column  data-align="left"   data-header="登记流水号" data-name="registerSerno" data-export="false" data-width="250"></k-grid-column>
        <k-grid-column  data-align="left"   data-header="登记日期" data-name="registerDate"  data-export="false" data-width="100"></k-grid-column>
        <k-grid-column  data-align="left"   data-header="新增日期" data-name="createDate"  data-export="false" data-width="100"></k-grid-column>
       <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改产品状态管理" data-functype="POPUP" data-size="mini"
              v-show="scope.row.row.registerStatus != '5'"    data-target="editProdStateRegistInfoPopup">
            修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="ProdStateRegistInfo.deleteProdStateRegistInfo" data-size="mini"
             v-show="scope.row.row.registerStatus != '5'"     data-type="danger" data-target="ProdStateRegistInfoGrid" :data-confirm="true" data-descript="删除产品状态管理">
            删除
          </k-btn>
          <!--<k-btn data-functype="PAGE" data-size="mini" class="md-info" :data-model="scope.row.row.id"
                 @click="popupEdit(scope.row.row)"  data-descript="资产持仓管理错误详情">
            错误详情
          </k-btn>-->
        </template>
      </k-grid>
    </div>

    <!--    添加资产持仓管理弹出框   -->
    <k-popup ref="addProdStateRegistInfoPopup" data-title="添加">
      <k-form ref="addProdStateRegistInfoForm" :data-col="2" isFormBodyScreen>
        <k-form-item label="发行机构代码">
          <k-field-text v-model="formData.bankCode" :data-allowblank="false" :data-max-length="6"/>
        </k-form-item>
        <k-form-item label="产品登记编码">
          <k-field-text v-model="formData.prodRegEnc" :data-allowblank="false" :data-max-length="15"/>
        </k-form-item>
        <k-form-item label="理财产品总资产金额(元)">
          <k-field-text v-model="formData.totAssets"  data-integer-length="13" data-digits="2" data-validate-type="money" :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="理财产品杠杆率(%)">
          <k-field-text v-model="formData.rate" data-integer-length="3" data-digits="5" data-validate-type="money" :data-allowblank="false" :data-max-length="40"/>
        </k-form-item>
        <k-form-item label="产品状态统计日">
          <k-field-date v-model="formData.valdate" :data-allowblank="false" data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd" />
        </k-form-item>
        <k-form-item label="备注">
          <k-field-text v-model="formData.details" :data-allowblank="false" :data-max-length="256"/>
        </k-form-item>
        <k-form-footer slot="footer" data-align="center">
          <k-btn class="md-primary" data-functype="SUBMIT" data-action="ProdStateRegistInfo.addProdStateRegistInfo"
                 data-from="addProdStateRegistInfoForm"
                 :data-model="formData" data-target="ProdStateRegistInfoGrid">
            <i class="icon-confirm"/>确定
          </k-btn>
          <k-btn class="md-info" data-functype="CLOSE">
            <i class="icon-cancel"/>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改资产持仓管理弹出框   -->
    <k-popup ref="editProdStateRegistInfoPopup" data-title="编辑" @data-opened="editOpened()">
      <k-form ref="editProdStateRegistInfoForm" :data-col="2" isFormBodyScreen>
        <k-form-item label="发行机构代码">
          <k-field-text v-model="formData.bankCode" :data-allowblank="false" :data-max-length="6" data-disabled="true"/>
        </k-form-item>
        <k-form-item label="产品登记编码">
          <k-field-text v-model="formData.prodRegEnc" :data-allowblank="false" :data-max-length="15" data-disabled="true"/>
        </k-form-item>
        <k-form-item label="理财产品总资产金额(元)">
          <k-field-text v-model="formData.totAssets" :data-allowblank="false" data-integer-length="13" data-digits="2" data-validate-type="money" data-regx-text="请输入大于等于0的数值"/>
        </k-form-item>
        <k-form-item label="理财产品杠杆率(%)">
          <k-field-text v-model="formData.rate" :data-allowblank="false" data-integer-length="3" data-digits="5" data-validate-type="number"/>
        </k-form-item>
        <k-form-item label="产品状态统计日">
          <k-field-date v-model="formData.valdate" :data-allowblank="false" data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd" data-disabled="true"/>
        </k-form-item>
        <k-form-item label="备注">
          <k-field-text v-model="formData.details" :data-max-length="256"/>
        </k-form-item>
        <k-form-footer slot="footer" data-align="center">
          <k-btn class="md-primary"
                 data-from="editProdStateRegistInfoForm"  :data-handler="sumbit_edit"  ref="sumbitedit"
                 :data-model="formData" data-target="ProdStateRegistInfoGrid">
            <i class="icon-confirm"/>确定
          </k-btn>
          <k-btn class="md-info" data-functype="CLOSE">
            <i class="icon-cancel"/>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="uploadProdStateRegistInfoPopup" title="数据导入">
      <k-form ref="addForm" data-ui="element">

        <k-form-item label="报送数据导入" data-ui="element" data-input-width="500px">
          <k-field-excel-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit='1'
                                data-accept=".xlsx,.xls"
                                :data-error="onSubmitError" :data-success="onSubmitSuccess"
                                :data-auto-upload="false"
                                data-upload-url="upload/server/RptApp/reportManage/prodStatusRegistImport.json">
          </k-field-excel-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="ProdStateRegistInfoGrid" ref="submitBtn"
                 :data-auto-upload="false" data-from="addForm" :data-handler="submitUploadParam">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
    <k-popup ref="auditInfoPopup">
      <k-form ref="updateAuditStatusForm" :data-col="2" isFormBodyScreen>
         <k-form-item label="报表类型">
           <k-field-text v-model="infoPop.tableName" :data-allowblank="false" data-disabled="true"/>
         </k-form-item>
         <k-form-item label="数据日期" data-label-width="100px">
           <k-field-date v-model="infoPop.auditDate" data-type="date" data-date-format="yyyy-MM-dd"
                         data-value-format="yyyyMMdd" :data-allowblank="false"/>
         </k-form-item>
         <k-form-item label="复核状态">
           <k-field-select v-model="infoPop.auditStatus" data-dict="xp_disclosure_check_status" data-default-value="1" data-disabled="true"/>
         </k-form-item>
         <k-form-footer slot="footer" data-align="center">
           <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-from="updateAuditStatusForm" data-target="ProdStateRegistInfoGrid"
                  @click="audit" :data-model="infoPop"><md-icon md-src="/static/svg/confirm.svg"></md-icon>确定</k-btn>
           <k-btn class="btn-custom-plain" data-functype="CLOSE"><md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
         </k-form-footer>
      </k-form>
    </k-popup>
  </div>
</template>

<script>
import KFieldSelect from "@/components/k-element/k-field-select/k-field-select";
import KPopup from "@/components/k-element/k-popup/k-popup.vue";
import KAudit from "@/pages/zz/manage/Audit.vue";
import Tools from "@/utils/tools";
import ProdMixin from "@/pages/zz/manage/mixins/prodMixin.js";


export default {
  name: "ProdStateRegistInfo",
  components: {KAudit, KPopup, KFieldSelect},
  mixins: [ProdMixin],
  data() {
    return {
      formData: {},
      selectRowData: {},
      searchParam:{},
      BreathDay:[],
      uploadBeginDate: '',
      uploadQueryDate: '',
      infoPop: {},
      auditInfoPopupData: {
        tableId: 'app_prod_state_regist_info',
        tableName: '产品状态管理'
      },
      showSubmitBtn: true,
      abnormalAction: "ProdStateRegistInfo.getAbnormalData",
      updateStatusAction: "ProdStateRegistInfo.updateProdStateRegistInfoStatus",
      comfirmExportParam:{}
    };
  },
   computed: {
        queryParam() {
          return {
            'startDate': this.searchParam.startDate,
            'prodRegEnc': this.searchParam.prodRegEnc,
            'registerStatus': this.searchParam.registerStatus
          }
        }
  },
  methods: {
   setConfirmExportParam() {
              this.comfirmExportParam = {
                 startDate: this.searchParam.startDate,
                 prodRegEnc: this.searchParam.prodRegEnc,
                 registerStatus: this.searchParam.registerStatus
              };
      },
    editOpened(){
              this.formData.oldData=Tools.json2str(this.formData);
    },
		dataHandler() {
			if (this.searchParam.startDate == null) {
				this.$message.error("统计日期不能为空!");
				return false;
			}
			setTimeout(()=>{
				this.$refs.exportRef.handleExport(this.queryParam);
			}, 500)
			return false
		},
    submitUploadParam() {
      //文件上传校验
      let validate = this.$refs.addForm.validate();
      if (validate) {
        let formData = { beginDate: this.uploadBeginDate, queryDate: this.uploadBeginDate};
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
     sumbit_edit(){
      this.$refs.sumbitedit.setIconStyle(0,[]);
      if(this.$refs.editProdStateRegistInfoForm.validate()){
           this.httpUtil.query({
                   url: 'server/json/RptApp/audit/checkProdStateRegistInfo.json',
                   params:  this.formData
                            }).then(res => {
                              if(res.success) {
                               this.httpUtil.comnUpdate({
                                        action: 'ProdStateRegistInfo.updateProdStateRegistInfo',
                                        params:  this.formData
                                         }).then(res => {
                                          if(res.success) {
                                          this.$refs.editProdStateRegistInfoPopup.close();
                                      }else{
                                        this.$refs.sumbitedit.setIconStyle(1,[]);
                                      }
                            })
                      }else{
                          this.$refs.sumbitedit.setIconStyle(1,[]);
                      }
           });
      }else{
        this.$refs.sumbitedit.setIconStyle(1,[]);
      }
    },
    audit() {
      let tableName = this.infoPop.tableName;
      let tableId = this.infoPop.tableId;
      let auditStatus = this.infoPop.auditStatus;
      let startDate = this.infoPop.auditDate;
      let endDate = this.infoPop.auditDate;
      this.httpUtil.ajax({
         url: 'server/json/RptApp/audit/indexstatus.json',
         params: {
           tableId: tableId,
           startDate: startDate,
           endDate: endDate,
           auditStatus: auditStatus
         }
       }).then(res => {
         if(res.success) {
           if(res.returnmsg=='存在指标校验未通过数据'){
               this.$confirm("日期区间存在未校验或校验未通过的数据,确认复核吗？", "操作提示", {
               confirmButtonText: "确定",
               cancelButtonText: "取消",
               type: "warning"
               }).then(() => {
                    this.httpUtil.ajax({
                    url: 'server/json/RptApp/audit/status.json',
                    params: {
                      tableId: tableId,
                      startDate: startDate,
                      endDate: endDate,
                      auditStatus: auditStatus
                    }
                  }).then(res => {
                    if(res.success) {
                     Tools.alert(res.returnmsg, "success");
                     this.$refs.ProdStateRegistInfoGrid.load(this.searchParam);
                     this.$refs.auditInfoPopup.close();
                    }
                  })
              }).catch(() => {});
           }else{
              this.httpUtil.ajax({
                url: 'server/json/RptApp/audit/status.json',
                params: {
                  tableId: tableId,
                  startDate: startDate,
                  endDate: endDate,
                  auditStatus: auditStatus
                }
              }).then(res => {
                if(res.success) {
                 Tools.alert(res.returnmsg, "success");
                 this.$refs.ProdStateRegistInfoGrid.load(this.searchParam);
                 this.$refs.auditInfoPopup.close();
                }
              })
           }
         }
       })
    },
    onSubmitSuccess() {
      this.$refs.uploadRef.doReset();
      this.$refs.addForm.reset();
      this.$refs.uploadProdStateRegistInfoPopup.close();
      this.$refs.ProdStateRegistInfoGrid.load(this.searchParam);
    },
    onSubmitError() {
      this.$refs.uploadRef.doReset();
      this.$refs.submitBtn.setIconStyle(1, [])
    },
    popupEdit(row){
      let pathUrl = '/main/zz/errorInfo/AssetRgInfoErr';
      this.$router.push({
        path: pathUrl,
        query: {
          registerSerno: row.registerSerno,
        },
      });
    },
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
    },
    uploadOpened() {
      this.uploadBeginDate = ''
      this.uploadQueryDate = ''
    },
    // 复核弹窗
    auditPopup() {
      this.infoPop = this.auditInfoPopupData;
      this.$set(this.infoPop, 'auditDate', this.searchParam.startDate);
      this.$refs.auditInfoPopup.popup();
    },
    // 复核弹窗关闭
    closeAuditFunc(val) {
      console.log(val);
      this.$nextTick(() => {
        this.$refs.ProdStateRegistInfoGrid.load();
      })
      this.$refs.auditInfoPopup.close();
    },
  },
  watch: {
    //查询起息日
    BreathDay() {

    },
  }
};
</script>
