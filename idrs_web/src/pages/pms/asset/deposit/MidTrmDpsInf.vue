<template>
  <div class="py-page">
    <k-form-search-customize data-target="midTrmDpsInfGrid" v-model="searchParam">
      <k-form-item label="存款名称">
        <k-field-select v-model="searchParam.scrCd" data-action="MidTrmDpsInf.findScrCd"  ref="scrCdId" :dataRemote="true"
                        data-display-field="scrCd,scrNm" data-value-field="scrCd" />
      </k-form-item>

      <k-form-item label="存款起息日">
        <k-field-date v-model="dateRange1" data-type="daterange" data-date-format="yyyyMMdd"
                      data-value-format="yyyyMMdd"></k-field-date>
      </k-form-item>

      <k-form-item label="存款到期日">
        <k-field-date v-model="dateRange2" data-type="daterange" data-date-format="yyyyMMdd"
                      data-value-format="yyyyMMdd"></k-field-date>
      </k-form-item>

     
    </k-form-search-customize>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}"
                data-target="addMidTrmDpsInfPopup" v-if="global.isShowAuthorityButton('MidTrmDpsInf.addMidTrmDpsInf')">
            <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
          <k-btn slot="button" style="width: 120px" class="btn-custom-plain" :data-download-name="'定期存款导入模版'+'.xlsx'"
                data-descript="下载模板" data-functype="DOWNLOAD" data-size="small"
                data-url="/download/server/DpsApp/excelUploadAction/comn-download.json">
            <md-icon>cloud_download</md-icon>
            下载模板
          </k-btn>
          <k-btn slot="button"  class="btn-custom-plain"  data-functype="EXPORT" data-target="midTrmDpsInfGrid" :data-export-name="'定期存款导出'"
                v-if="global.isShowAuthorityButton('MidTrmDpsInf.trmExcelDownloadAction')">
            <md-icon>cloud_download</md-icon>
            导出
          </k-btn>
          <k-btn slot="button"  data-functype="POPUP" class="btn-custom-plain" data-target="uploadUnderRightInfoPopup"
                v-if="global.isShowAuthorityButton('MidTrmDpsInf.trmExcelUploadAction')">
            <md-icon>cloud_upload</md-icon>
            导入
          </k-btn>
        </div>
      </div>
    
      <k-grid ref="midTrmDpsInfGrid" @data-row-select="selectRow" data-action="MidTrmDpsInf.findMidTrmDpsInfs"
              data-operate-width="160px" v-if="global.isShowAuthorityButton('MidTrmDpsInf.findMidTrmDpsInfs')">
        <k-grid-column data-header="证券编码" data-name="scrId" data-hidden="true"/>
        <k-grid-column data-header="存款代码" data-name="scrCd" data-width="100"/>
        <k-grid-column data-header="存款名称" data-name="scrNm"/>
        <k-grid-column data-header="存款金额" data-name="dpsAmt" data-type="money"/>
        <k-grid-column data-header="存款年利率%" data-name="anlYld"/>
        <k-grid-column data-header="存款起息日" data-name="valDt" data-type="date"/>
        <k-grid-column data-header="存款到期日" data-name="mtuDt" data-type="date"/>
        <k-grid-column data-header="资金存入银行" data-name="dpsBnk" />
        <k-grid-column data-header="存款类型" data-name="dpsTyp" data-dict="deposit_type"/>
        <k-grid-column data-header="挂钩标的类别" data-name="lnkSbjMatTyp" data-dict="stru_deposit_type"/>
        <k-grid-column data-header="挂钩标的" data-name="lnkSbjMat"/>
        <k-grid-column data-header="版本号" data-name="version"/>

  <!--      隐藏字段-->
        <k-grid-column data-header="资产分类" data-name="assetDebtType" data-hidden="true"/>
        <k-grid-column data-header="币种" data-name="ccy" data-hidden="true" data-dict="cur_type"/>
        <k-grid-column data-header="存款账号" data-name="dpsActNbr" data-hidden="true"/>
        <k-grid-column data-header="计息基础" data-name="intrBas" data-hidden="true" data-dict="intr_base"/>
        <k-grid-column data-header="交易流通场所" data-name="trxMkt" data-hidden="true" data-dict="tacdingPlace"/>
        <k-grid-column data-header="中债一级分类" data-name="cbndFrsCtg" data-hidden="true" data-dict="cbndFrsCtg"/>
        <k-grid-column data-header="中债二级分类" data-name="cbndScdCtg" data-hidden="true" data-dict="cbndScdCtg"/>
        <k-grid-column data-header="备注" data-name="cmt" data-hidden="true"/>
        <k-grid-column data-header="G06二级分类" data-name="ggCbcSubType" data-hidden="true" data-dict="g06_scd_type"/>
        <k-grid-column data-header="G06一级分类" data-name="ggCbcType" data-hidden="true" data-dict="g06_first_type"/>
        <k-grid-column data-header="挂钩标的" data-name="lnkSbjMat" data-hidden="true"/>
        <k-grid-column data-header="付息频率" data-name="payinterestFreq" data-hidden="true" data-dict="payIntrFrq"/>
        <k-grid-column data-header="人行一级分类" data-name="pbnkFrsCtg" data-hidden="true" data-dict="asseFrsCtg"/>
        <k-grid-column data-header="人行四级分类" data-name="pbnkFurCtg" data-hidden="true" data-dict="pbnkTrdCtg"/>
        <k-grid-column data-header="人行二级分类" data-name="pbnkScdCtg" data-hidden="true" data-dict="pbnkFrsCtg"/>
        <k-grid-column data-header="人行三级分类" data-name="pbnkTrdCtg" data-hidden="true" data-dict="pbnkScdCtg"/>

        <k-grid-column data-header="创建日期" data-name="crtDate" data-hidden="true" data-export="false" />
        <k-grid-column data-header="创建时间" data-name="crtTime" data-hidden="true" data-export="false" />
        <k-grid-column data-header="创建人" data-name="crtUser" data-hidden="true" data-export="false" />
        <k-grid-column data-header="修改日期" data-name="updDate" data-hidden="true" data-export="false" />
        <k-grid-column data-header="修改时间" data-name="updTime" data-hidden="true" data-export="false" />
        <k-grid-column data-header="修改人" data-name="updUser" data-hidden="true" data-export="false" />
        <k-grid-column data-header="处理日期" data-name="dealDate" data-hidden="true" data-export="false"/>

        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text specialClass" data-descript="修改定期存款" data-functype="POPUP" data-size="mini"
                data-target="editMidTrmDpsInfPopup" v-if="global.isShowAuthorityButton('MidTrmDpsInf.updateMidTrmDpsInf')">
            修改
          </k-btn>
          <k-btn class="btn-custom-text specialClass" data-descript="补录定期存款" data-functype="POPUP" data-size="mini"
                data-target="blMidTrmDpsInfPopup" v-if="global.isShowAuthorityButton('MidTrmDpsInf.updateMidTrmSupplyDpsInf')">
            补录
          </k-btn>
          <k-btn class="btn-custom-text specialClass" data-functype="SUBMIT" data-size="mini" data-action="MidTrmDpsInf.deleteMidTrmDpsInf"
                data-type="danger" data-target="midTrmDpsInfGrid" :data-confirm="true" data-descript="删除定期存款"
                v-if="global.isShowAuthorityButton('MidTrmDpsInf.deleteMidTrmDpsInf')" >
            删除
          </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    补录弹出框   -->
	<k-popup ref="blMidTrmDpsInfPopup" data-title="补录" :dataDialogDrag="true">
    <EditComp @loadGriding="loadGriding" ref="editComp" :info="formData" :disabledVal="true" :isDetailShow="true" />
    <MidTrmDpsInfCollection @loadGriding="loadGriding" ref="midTrmDpsInfCollection" :info="formData" :disabledVal="true"/>
	</k-popup>
	<!--    添加弹出框   -->
	<k-popup ref="addMidTrmDpsInfPopup" data-title="新增" :dataDialogDrag="true">
    <EditComp @loadGriding="loadGriding" ref="addComp" :info="{}" :disabledVal="false"/>
	</k-popup>

	<!--    修改弹出框   -->
	<k-popup ref="editMidTrmDpsInfPopup" data-title="修改" :dataDialogDrag="true">
    <EditComp  @loadGriding="loadGriding" ref="editComp" :info="formData" :disabledVal="true"/>
	</k-popup>

    <k-popup ref="uploadUnderRightInfoPopup" title="定期存款信息导入">
      <k-form ref="addForm" data-ui="element">

        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-excel-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit='2'
                                data-accept=".xlsx,.xls" :data-error="onSubmitError" :data-success="onSubmitSuccess"
                                :data-auto-upload="false"
                                data-upload-url="upload/server/DpsApp/excelUploadAction/TrmExcelUploadAction.json">
          </k-field-excel-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="assetRightGrid" ref="submitBtn"
                 :data-auto-upload="false" data-from="addForm" :data-handler="submitUploadParam">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

  </div>
</template>

<script>
import EditComp from "./MidTrmDpsInfEdit";
import MidTrmDpsInfCollection from "./MidTrmDpsInfCollection";
  export default {
    name: "MidTrmDpsInf",
    components: {
      EditComp,
      MidTrmDpsInfCollection
    },

    data() {
      return {
        dateRange1:[],
        dateRange2:[],
        formData: {},
        selectRowData: {},
        searchParam: {},//查询参数
      };
    },
    methods: {


      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      },

      loadGriding(val){
        this.$refs.addMidTrmDpsInfPopup.close();
        this.$refs.editMidTrmDpsInfPopup.close();
        this.$refs.blMidTrmDpsInfPopup.close();
        this.$refs.midTrmDpsInfGrid.load(this.searchParam);
        this.$refs.scrCdId.load();
      },

    //  导入
      onSubmitError() {
        this.$refs.uploadRef.doReset();
        this.$refs.submitBtn.setIconStyle(1, [])
      },

      onSubmitSuccess() {
        this.$refs.uploadRef.doReset();
        this.$refs.addForm.reset();
        this.$refs.uploadUnderRightInfoPopup.close();
        this.$refs.midTrmDpsInfGrid.load(this.queryParam);
      },

      submitUploadParam(){
        let formData = this.formData;
        this.$refs.uploadRef.upload(formData);
      },


    },

    watch: {
      dateRange1() {
        this.$set(this.searchParam, 'valDtStart', this.dateRange1 == null ? '' : this.dateRange1[0]);
        this.$set(this.searchParam, 'valDtEnd', this.dateRange1 == null ? '' : this.dateRange1[1]);
      },
      dateRange2() {
        this.$set(this.searchParam, 'mtuDtStart', this.dateRange2 == null ? '' : this.dateRange2[0]);
        this.$set(this.searchParam, 'mtuDtEnd', this.dateRange2 == null ? '' : this.dateRange2[1]);
      },
    }
  };
</script>

<style scoped lang="scss">
@import "../AssetComFunction.css";
</style>
