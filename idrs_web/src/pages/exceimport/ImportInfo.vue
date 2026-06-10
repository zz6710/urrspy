<template>
  <div class="py-page">
    <k-form-search-customize data-target="valReadassetsMergeGrid" data-model-name="valReadassetsMerge" v-model="findFormData" data-label-width="140px">
      <k-form-item label="估值表名称">
        <k-field-select v-model="findFormData.t8ValReporttabId"  data-action="ValReportTab.findValReportTabs"
                        data-display-field="t8ValReporttabId,reporttabName" data-value-field="t8ValReporttabId" />
      </k-form-item>
       <k-form-item label="估值日期">
         <k-field-date v-model="findFormData.changeDate" data-value-format="yyyyMMdd" data-date-format="yyyy-MM-dd" />
       </k-form-item>
      <k-form-item label="资产/产品代码">
        <k-field-select v-model="findFormData.assetCode"  :data-data="assetCodeList"
                        data-display-field="assetCode" data-value-field="assetCode"  />
      </k-form-item>
      <k-form-item label="底层资产负债编码">
          <k-field-text v-model="findFormData.ftoolCode"/>
        </k-form-item>
      <k-form-item label="底层资产负债名称">
          <k-field-text v-model="findFormData.ftoolName"/>
        </k-form-item>
      <k-form-item label="关联类型">
        <k-field-select v-model="findFormData.isprodorasset" :data-data="isprodorassetList" data-display-field="isprodorasset,isprodorassetname" data-value-field="isprodorasset"  :data-disabled="isprodorassetsDisabled"  />
      </k-form-item>
<!--
      <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addT81003Popup">
        <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
     -->
       
    </k-form-search-customize>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn slot="button" class="btn-custom-plain" data-functype="POPUP" :data-handler="checkBatchDeleteData" data-target="bathDelPopup"
            v-if="global.isShowAuthorityButton('ValReadassetsMerge.batchDeleteValReadassetsMerge')">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>批量删除</k-btn>
          <k-btn slot="button" data-functype="POPUP" class="btn-custom-plain" data-target="singleUploadPopup"   v-if="global.isShowAuthorityButton('ValReadassetsMerge.dataImport')"
            :data-handler="()=>this.uploadData={}">
            <md-icon md-src="/static/svg/upload.svg"/>
            导入估值表
          </k-btn>
          <k-btn slot="button" class="btn-custom-plain" data-target="valReadassetsMergeGrid" :data-export-name="'估值表信息解析'"   v-if="global.isShowAuthorityButton('ValReadassetsMerge.dataExport')"
            data-descript="数据导出" data-functype="EXPORT" data-size="small"
            data-url="ValReadassetsMerge.findValReadassetsMerges">
            <md-icon>cloud_download</md-icon>
            导出
          </k-btn>
        </div>
      </div>
      <k-grid ref="valReadassetsMergeGrid" @data-row-select="selectRow" data-fixed="right" :data-checkbox="true" data-checkbox-id="id"   @init="(grid)=>{this.$kgrid = grid}"
              data-operate-width="90px" data-action="ValReadassetsMerge.findValReadassetsMerges" >
          <k-grid-column data-align="left"  data-width="100" data-header="估值表名称" data-name="reporttabName" />
          <k-grid-column data-align="left"  data-width="150" data-header="资产/产品代码" data-name="assetCode"/>
          <k-grid-column data-align="left"  data-width="100" data-header="估值日期" data-name="changeDate"  data-type="date"/>
          <k-grid-column data-align="left"  data-width="100" data-header="底层资产负债编码" data-name="ftoolCode"/>
          <k-grid-column data-align="left"  data-width="150" data-header="底层资产负债名称" data-name="ftoolName"/>
          <k-grid-column data-align="left"  data-width="100" data-header="品种名称" data-name="adName"/>
          <k-grid-column data-align="left"  data-width="100" data-header="品种ID" data-name="t8SysAdtypeId" :data-hidden="true"/>
          <k-grid-column data-align="left"  data-width="100" data-header="市场" data-name="market" data-dict="market"/>
          <k-grid-column data-align="right"  data-width="100" data-header="面额余额" data-name="positionbln"/>
          <k-grid-column data-align="right"  data-width="100" data-header="本金余额" data-name="principalbln"/>
          <k-grid-column data-align="left"  data-width="100" data-header="摊余成本余额，利息调整余额" data-name="interestbln" :data-hidden="true"/>
          <k-grid-column data-align="right"  data-width="100" data-header="应收利息/红利余额" data-name="accruedincomebln"/>
          <k-grid-column data-align="right"  data-width="100" data-header="净价成本余额/成本余额" data-name="npamountbln" :data-hidden="true"/>
          <k-grid-column data-align="left"  data-width="100" data-header="费用余额" data-name="feepaybln"/>
          <k-grid-column data-align="right"  data-width="100" data-header="公允价余额" data-name="fairvaluebln"/>
          <k-grid-column data-align="right"  data-width="100" data-header="应付税费余额" data-name="taxfeebln"/>
          <k-grid-column data-align="right"  data-width="100" data-header="待付税费余额" data-name="payTaxbln"/>
          <k-grid-column data-align="right"  data-width="100" data-header="应付利息余额" data-name="accruedpaybln"/>
          <k-grid-column data-align="right"  data-width="100" data-header="证券清算款余额" data-name="securitiesliquidationbln" :data-hidden="true"/>
          <k-grid-column data-align="right"  data-width="100" data-header="金融计算值" data-name="jrjsValue" />
          <k-grid-column data-align="left"  data-width="100" data-header="录入柜员" data-name="inputuser" :data-hidden="true"/>
          <k-grid-column data-align="left"  data-width="100" data-header="关联类型" data-name="isprodorasset" data-dict="base_isprodorasset"/>
          <k-grid-column data-align="left"  data-width="100" data-header="产品名称" data-name="prodName" :data-hidden="true"/>
          <k-grid-column data-align="left"  data-width="100" data-header="当日行情" data-name="balance" :data-hidden="true"/>
        <template slot="operate" slot-scope="scope">
          <!--
          <k-btn class="btn-custom-plain" data-descript="修改解析配置合并" data-functype="POPUP" data-size="mini"
                data-target="editValReadassetsMergePopup">
            修改
          </k-btn>
          -->
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="ValReadassetsMerge.deleteValReadassetsMerge" data-size="mini"
                data-type="danger" data-target="valReadassetsMergeGrid" :data-confirm="true" data-descript="删除解析配置合并">
            删除
          </k-btn>
        </template>
      </k-grid>
    </div>
    <k-popup ref="singleUploadPopup" data-title="上传excel文件" @data-opened="uploadOpened()">
      <k-form ref="singleUploadForm" :data-col="2">

        <k-form-item label="上传附件" data-ui="element">

          <k-form-item label="关联类型">
            <k-field-select v-model="upData.isprodorasset" data-dict="base_isprodorasset" @data-on-change="setAssetCode" :data-allowblank="false"  data-default-value="2" />
          </k-form-item>

          <k-form-item label="资产/产品代码">
            <k-field-select v-model="upData.asset_code"  :data-data="assetCodes"
                            data-display-field="assetCode" data-value-field="assetCode"  :data-allowblank="false"/>
          </k-form-item>

          <k-field-upload ref="fileUpload" :data-limit="10" :data-auto-upload="false" data-accept=".xls,.dbf"
                          :dataChange="uploadXml" :data-remove="uploadRemove"><!-- data-accept=".xls" -->
            <div slot="tip" class="el-upload__tip">只能上传excel文件</div>
          </k-field-upload>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn ref="upload" class="btn-custom-primary" @click.native="uploadHandler" data-target="valReadassetsMergeGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

     <k-popup ref="bathDelPopup" title="批量删除" >
              <k-form ref="bathDelForm" data-ui="element" dataLabelWidth="130px" dataInputWidth="220px" >
                <k-form-item label="确定批量删除吗？"  :data-col="2" data-input-width="80px" >
                </k-form-item>
                <k-form-footer data-align="center">
                  <k-btn class="btn-custom-primary" data-target="prodInfoGrid" ref="submitPublishBtn" data-from="bathDelForm" :data-model="formData" @click="batchDelDatas">
                    <span v-show="showSubmitBtn">确定</span>
                    <i v-show="!showSubmitBtn" class="el-icon-loading"/>
                  </k-btn>
                  <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
                </k-form-footer>

              </k-form>
            </k-popup>

  </div>
</template>

<script>
import Tools from '@/utils/tools.js';
import httpUtil from "@/frame/httpUtil";

export default {
  name: "ImportInfo",
  data() {
    return {
      queryParam: {},
      formData: {},
      uploadData: [],
      upData: {},
      findFormData :{},

      assetCodes:[],
      assetCodeList:[],
      isprodorassetList:[],
      showSubmitBtn:true,
      isprodorassetsDisabled: false,
    };
  },
  watch: {
    'findFormData.t8ValReporttabId'() {
      this.isprodorassetsDisabled =false;
      this.findassetCodeList();
      this.findisprodorassetList();
    },
  },
  methods: {

    setAssetCode(val){

      this.httpUtil.comnQuery({
        action:"OdsReadAssetsReport.findOdsReadAssetsReports",
        params:{
          isprodorasset : val,
        }
      }).then(data=>{
        this.assetCodes = data.rows;
      })
    },


    selectRow(row, column, event) {
      this.formData = Object.assign({}, row)
    },
    uploadXml(file, fileList) {
      // if (file.name.substring(file.name.lastIndexOf(".") + 1) !== "xml") {
      //   Tools.alert("文档类型不为xml！", "warning");
      //   this.$refs.fileUpload.doReset()
      // } else {
      this.uploadData = fileList;
      // }
    },
    uploadRemove(file, fileList) {
      this.uploadData = fileList;
    },
    uploadHandler($event) {
      if (this.uploadData.length === 0) {
        Tools.alert("请选择文件，然后提交！", "warning");
      }else if(this.upData.isprodorasset == null || this.upData.isprodorasset == '' || this.upData.isprodorasset == undefined){
        Tools.alert("请选择关联类型！", "warning");
      }else if(this.upData.asset_code == null || this.upData.asset_code == '' || this.upData.asset_code == undefined){
        Tools.alert("请选择资产/产品代码！", "warning");
      } else {
        let params = [];
        this.uploadData.forEach(file => {
          params.push(file.raw)
        });
        let children = $event.currentTarget.childNodes[0].childNodes[0].childNodes;
        let iconList = [];
        for (let i = 0; i < children.length; i++) {
          if (children[i].nodeName === 'I') {
            iconList.push(children[i])
          }
        }
        //this.$refs.upload.setIconStyle(0, iconList);
       // let formData = { beginDate: this.uploadBeginDate, queryDate: this.uploadQueryDate};
        httpUtil.uploadForExcel({
          url: 'valtabImpdata/valTabImport.action',
          files: params,
          successAlert: true
        },this.upData).then(res => {
          if (res.success) {
            this.$refs.upload.setIconStyle(1, iconList)
            this.$refs.singleUploadPopup.close();
            this.$refs.valReadassetsMergeGrid.load();
            this.findassetCodeList();
          }
        })
      }
    },

    findassetCodeList(){
      let t8ValReporttabId = this.findFormData.t8ValReporttabId;
      this.httpUtil.comnQuery({
        action:"ValReadassetsMerge.findAssetCode",
        params:{t8ValReporttabId:t8ValReporttabId}
      }).then(data=>{
        this.assetCodeList = data.rows;
      })
    },
    findisprodorassetList(){
      let t8ValReporttabId = this.findFormData.t8ValReporttabId;
      this.httpUtil.comnQuery({
        action:"ValReadassetsMerge.findisprodorassetList",
        params:{t8ValReporttabId:t8ValReporttabId}
      }).then(data=>{
        this.isprodorassetList = data.rows;
        if(t8ValReporttabId!=""&&t8ValReporttabId!=undefined&&data.rows.length>0){
          this.$set(this.findFormData, 'isprodorasset', data.rows[0].isprodorasset);
          this.isprodorassetsDisabled = true;
        }else{
          this.$set(this.findFormData, 'isprodorasset', "");
          this.isprodorassetsDisabled = false;
        }
      })
    },
    uploadOpened() {
        this.$set(this.upData, 'isprodorasset', '2');
        this.$set(this.upData, 'asset_code', '');
        this.setAssetCode(2);
      },
      checkBatchDeleteData () {
          let pass = true;
          const _this = this;
          const list = _this.$kgrid.getSelected();
          if(list.length === 0){
            Tools.alertTime( "请先勾选需要删除的数据!", "danger",5000);
            return false;
          }
        },
       batchDelDatas () {
           const _this = this;
           const list = _this.$kgrid.getSelected();
           this.showSubmitBtn = false;
           this.httpUtil.comnUpdate({
             action: 'ValReadassetsMerge.batchDeleteValReadassetsMerge',
             params: {list: JSON.stringify(list)},
             successAlert: true,
           }).then(data => {
             this.showSubmitBtn = true;
             this.$refs.bathDelForm.reset();
             this.$refs.bathDelPopup.close();
             this.$refs.valReadassetsMergeGrid.load(this.findFormData);
             this.$refs.valReadassetsMergeGrid.setSelected([]);//刷新复选框
           }).catch(() => {
             this.showSubmitBtn = true;
             this.$refs.bathDelPopup.close();
             this.$refs.valReadassetsMergeGrid.load(this.findFormData);
             this.$refs.valReadassetsMergeGrid.setSelected([]);//刷新复选框
           });
         },
  },


  created() {
    this.findassetCodeList();
    this.findisprodorassetList();
  },
};
</script>
