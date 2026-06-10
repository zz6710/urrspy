<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="FundNavInfoModel" data-target="fundNavInfoModelGrid" v-model="searchParam" data-label-width="80px">
        <k-form-item label="系统表名 ">
          <k-field-select v-model="searchParam.tableName"  :data-data="tableNameDict" data-display-field="systemTableName" data-value-field="id"  />
        </k-form-item>
        <k-form-item label="模板名称">
          <k-field-text v-model="searchParam.templateName" data-validate-type="text" ></k-field-text>
        </k-form-item>
        <k-form-item label="基准日期">
          <k-field-date v-model="searchParam.reportDate" data-type="date"  data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd" />
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="handleUpload" data-target="addImportTemplateManagePopup" slot="button" ref="uploadBtnRef" :load-disabled="false">
            <md-icon md-src="/static/svg/add.svg" />新增导入</k-btn>

          <k-btn class="btn-custom-plain" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="downTemplateManagePopup" slot="button" >
            <md-icon md-src="/static/svg/add.svg" />下载模板</k-btn>
        </div>
      </div>
      <k-grid ref="fundNavInfoModelGrid" data-fixed="right" data-operate-width="150px" data-action="ImportTemplateDataLog.findImportTemplateDataLogs"
              data-operate-column="false">
		    <k-grid-column data-header="id" data-name="id" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="系统表名" data-name="itemkey" data-dict="systemTableNameDict" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="系统表名" data-name="tableName"></k-grid-column>
        <k-grid-column data-header="模板名称" data-name="templateName"></k-grid-column>
        <k-grid-column data-header="文件名称" data-name="templateFileName" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="基准日期" data-name="reportDate" data-type="date"></k-grid-column>
<!--        <k-grid-column data-header="数据状态" data-name="sysDataStatus"   data-dict="sysDataStatusDict"></k-grid-column>-->
        <k-grid-column data-header="数据版本号" data-name="sysDataVersion" ></k-grid-column>
<!--        <k-grid-column data-header="导入人员" data-name="impUsr" ></k-grid-column>-->
        <k-grid-column data-header="导入时间" data-name="impDate"></k-grid-column>
        <template slot="operate" slot-scope="scope">
<!--          <k-btn class="btn-custom-plain" data-functype="SUBMIT" data-action="ImportTemplateManage.updateTemplateInfoStatusOpen" data-size="mini"-->
<!--                 :data-disabled="scope.row.row.templateStatus === '1'" data-target="fundNavInfoModelGrid" :data-confirm="true" data-descript="启用当前模板">-->
<!--            启用-->
<!--          </k-btn>-->

<!--          <k-btn class="md-danger" data-functype="SUBMIT" data-action="ImportTemplateManage.updateTemplateInfoStatusStop" data-size="mini"-->
<!--                 :data-disabled="scope.row.row.templateStatus === '0'"  data-target="fundNavInfoModelGrid" :data-confirm="true" data-descript="停用当前模板">-->
<!--            停用-->
<!--          </k-btn>-->
<!--          <k-btn class="btn-custom-plain" data-functype="SUBMIT" data-action="FundNavInfoModel.deleteFundNavInfoModel" data-size="mini"-->
<!--                 data-type="danger" data-target="fundNavInfoModelGrid" :data-confirm="true" data-descript="下载当前模板">-->
<!--            下载-->
<!--          </k-btn>-->
          <k-btn class="btn-custom-text" :data-download-name="scope.row.row.templateFileName"  data-descript="下载文档模板信息" data-functype="DOWNLOAD" data-size="mini"
                  data-url="/download/server/DpsApp/importTemplate/downloadTempData.json" v-model="scope.row.row">
            下载
          </k-btn>
<!--          <k-btn class="btn-custom-plain" data-descript="新增" data-functype="POPUP" data-size="small" style="width: 80px"-->
<!--                 data-target="addPortFieldManageInfoPopup" v-if="scope.row.row.importType === '01'">-->
<!--            字段配置-->
<!--          </k-btn>-->

<!--          <k-btn class="btn-custom-plain" data-descript="新增" data-functype="POPUP" data-size="small" style="width: 80px"-->
<!--                 data-target="addPortFieldManageInfo2Popup"  v-if="scope.row.row.importType === '02'">-->
<!--            字段配置-->
<!--          </k-btn>-->
<!--          <k-btn class="btn-custom-plain" data-functype="SUBMIT" data-action="FundNavInfoModel.deleteFundNavInfoModel" data-size="mini" v-if="global.isShowAuthorityButton('FundNavInfoModel.deleteFundNavInfoModel')"-->
<!--               data-type="danger" data-target="fundNavInfoModelGrid" :data-confirm="true" data-descript="删除基金净值信息">-->
<!--          	历史版本-->
<!--    	  </k-btn>-->
        </template>
      </k-grid>
    </div>




<!--    <div>-->
<!--      <k-grid ref="portFieldManageInfoGrid"   v-show="portFieldManageInfoGrid" @data-row-select="setExeidBool" data-fixed="right" data-operate-width="200px" data-action="ImportTemplateManageField01.findTemplateFieldList" >-->
<!--        <k-grid-column data-header="id " data-name="id" data-hidden="true"></k-grid-column>-->
<!--        <k-grid-column data-header="系统表名 " data-name="systemTableName" data-dict="systemTableNameDict"></k-grid-column>-->
<!--        <k-grid-column data-header="数据库列字段 " data-name="databaseColumnCode"></k-grid-column>-->
<!--        <k-grid-column data-header="数据库列名 " data-name="databaseColumnName"></k-grid-column>-->
<!--        <k-grid-column data-header="数据类型 " data-name="columnType" data-dict="columnTypeDict"></k-grid-column>-->
<!--        <k-grid-column data-header="模板列序号 " data-name="templateColumnSerial"></k-grid-column>-->
<!--        <k-grid-column data-header="更新人员 " data-name="uptUsr"></k-grid-column>-->
<!--        <k-grid-column data-header="更新时间 " data-name="uptDateTime"></k-grid-column>-->
<!--        <template slot="operate" slot-scope="scope">-->
<!--          <k-btn class="btn-custom-plain" data-descript="修改接口文件字段信息" data-functype="POPUP" data-size="mini"-->
<!--                 :data-handler="setExeidBool" data-target="editPortFieldManageInfoPopup">-->
<!--            修改-->
<!--          </k-btn>-->
<!--          <k-btn class="md-danger" data-functype="SUBMIT" data-action="ImportTemplateManageField01.delTemplateFieldInfo"-->
<!--                 :data-confirm="true" data-size="mini" data-type="danger" data-target="portFieldManageInfoGrid" data-descript="删除接口文件字段信息">-->
<!--            删除-->
<!--          </k-btn>-->
<!--        </template>-->
<!--      </k-grid>-->
<!--    </div>-->

    <div>
      <k-grid ref="portFieldManageInfo2Grid"  v-show="portFieldManageInfo2Grid" @data-row-select="setExeidBool" data-fixed="right" data-operate-width="200px" data-action="ImportTemplateManageField02.findTemplateFieldList" >
        <k-grid-column data-header="id " data-name="id" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="系统表名 " data-name="systemTableName" data-dict="systemTableNameDict"></k-grid-column>
        <k-grid-column data-header="取值行 " data-name="valueRow"></k-grid-column>
        <k-grid-column data-header="取值列 " data-name="valueColumn"></k-grid-column>
        <k-grid-column data-header="数据类型 " data-name="columnType" data-dict="columnTypeDict"></k-grid-column>
        <k-grid-column data-header="更新人员 " data-name="uptUsr"></k-grid-column>
        <k-grid-column data-header="更新时间 " data-name="uptDateTime"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改接口文件字段信息" data-functype="POPUP" data-size="mini"
                 :data-handler="setExeidBool" data-target="editPortFieldManageInfo2Popup">
            修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="ImportTemplateManageField02.delTemplateFieldInfo"
                 :data-confirm="true" data-size="mini" data-type="danger" data-target="portFieldManageInfo2Grid" data-descript="删除接口文件字段信息">
            删除
          </k-btn>
        </template>
      </k-grid>
    </div>
    <!--    添加接口文件字段信息弹出框   -->
    <k-popup ref="addPortFieldManageInfoPopup" data-title="横表添加">
      <k-form ref="addPortFieldManageInfoForm" :data-col="2">
        <k-form-item label="系统表名 ">
          <k-field-select v-model="formData.systemTableName" :data-allowblank="false" :data-disabled="true" data-dict="systemTableNameDict"/>
        </k-form-item>
        <k-form-item label="数据库列字段 ">
          <k-field-text v-model="formData.databaseColumnCode" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="数据库列名 ">
          <k-field-text v-model="formData.databaseColumnName" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="数据类型">
          <k-field-select v-model="formData.columnType" data-dict="columnTypeDict" :data-allowblank="false"  />
        </k-form-item>
        <k-form-item label="模板列序号 ">
          <k-field-text v-model="formData.templateColumnSerial" :data-allowblank="false"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ImportTemplateManageField01.addTemplateFieldInfo" data-from="addPortFieldManageInfoForm"
                 :data-model="formData" data-target="portFieldManageInfoGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>

          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>


    <!--    修改字段信息弹出框   -->
    <k-popup ref="editPortFieldManageInfoPopup" data-title="横表编辑">
      <k-form ref="editPortFieldManageInfoForm" :data-col="2" >
        <k-form-item label="id " data-hidden="true">
          <k-field-text v-model="formData.id" :data-allowblank="false"  :data-disabled="true"  data-hidden="true"/>
        </k-form-item>
        <k-form-item label="系统表名 ">
          <k-field-select v-model="formData.systemTableName" :data-allowblank="false" :data-disabled="true" data-dict="systemTableNameDict"/>
        </k-form-item>
        <k-form-item label="数据库列字段 ">
          <k-field-text v-model="formData.databaseColumnCode" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="数据库列名 ">
          <k-field-text v-model="formData.databaseColumnName" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="数据类型">
          <k-field-select v-model="formData.columnType" data-dict="columnTypeDict" :data-allowblank="false"  />
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ImportTemplateManageField01.uptTemplateFieldInfo" data-from="editPortFieldManageInfoForm"
                 :data-model="formData" data-target="portFieldManageInfoGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>






    <!--    添加接口文件字段信息弹出框   -->
    <k-popup ref="addPortFieldManageInfo2Popup" data-title="纵表添加">
      <k-form ref="addPortFieldManageInfo2Form" :data-col="2">
        <k-form-item label="系统表名 ">
          <k-field-select v-model="formData.systemTableName" :data-allowblank="false" :data-disabled="true" data-dict="systemTableNameDict"/>
        </k-form-item>
        <k-form-item label="取值行 ">
          <k-field-text v-model="formData.valueRow" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="取值列 ">
          <k-field-text v-model="formData.valueColumn" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="数据类型">
          <k-field-select v-model="formData.columnType" data-dict="columnTypeDict" :data-allowblank="false"  />
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ImportTemplateManageField02.addTemplateFieldInfo" data-from="addPortFieldManageInfo2Form"
                 :data-model="formData" data-target="portFieldManageInfoGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>


    <!--    修改字段信息弹出框   -->
    <k-popup ref="editPortFieldManageInfo2Popup" data-title="纵表编辑">
      <k-form ref="editPortFieldManageInfo2Form" :data-col="2" >
        <k-form-item label="id " data-hidden="true">
          <k-field-text v-model="formData.id" :data-allowblank="false"  :data-disabled="true"  data-hidden="true"/>
        </k-form-item>
        <k-form-item label="系统表名 ">
          <k-field-select v-model="formData.systemTableName" :data-allowblank="false" :data-disabled="true" data-dict="systemTableNameDict"/>
        </k-form-item>
        <k-form-item label="取值行 ">
          <k-field-text v-model="formData.valueRow" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="取值列 ">
          <k-field-text v-model="formData.valueColumn" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="数据类型">
          <k-field-select v-model="formData.columnType" data-dict="columnTypeDict" :data-allowblank="false"  />
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ImportTemplateManageField02.uptTemplateFieldInfo" data-from="editPortFieldManageInfo2Form"
                 :data-model="formData" data-target="portFieldManageInfoGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>


    <!--    添加基金净值信息弹出框   -->
	<k-popup ref="downTemplateManagePopup" data-title="下载模板" >
    	<k-form ref="addForm" :data-col="2">
<!--        <k-form-item label="id"  v-show="false">-->
<!--          <k-field-text v-model="formData.scrId" />-->
<!--        </k-form-item>-->
<!--        <k-form-item label="系统表名">-->
<!--          <k-field-select v-model="formData.systemTableName" data-dict="systemTableNameDict" :data-allowblank="false"  @data-on-change="changeData"  />-->
<!--        </k-form-item>-->

        <k-form-item label="系统表名 ">
          <k-field-select v-model="formData.systemTableName"  :data-data="tableNameDict" data-display-field="systemTableName" data-value-field="id"  @data-on-change="changeData" :data-allowblank="false" />
        </k-form-item>


	      	<k-form-footer data-align="center">


            <k-btn class="btn-custom-primary"  data-descript="下载文档模板信息" data-functype="DOWNLOAD" data-size="mini"
                   data-url="/download/server/DpsApp/importTemplate/downloadFileByName.json" v-model="formData" data-from="addForm" :data-model="formData"  :data-handler="beforeSubmit">
              <md-icon md-src="/static/svg/download.svg"></md-icon>下载
            </k-btn>


		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>




    <!--    报表数据导入弹出框   -->
    <k-popup ref="addImportTemplateManagePopup" data-title="新增导入" >
      <k-form ref="addForm" :data-col="2">

<!--        <k-form-item label="系统表名">-->
<!--          <k-field-select v-model="formData.systemTableName" data-dict="systemTableNameDict" :data-allowblank="false"  />-->
<!--        </k-form-item>-->
        <k-form-item label="系统表名 ">
          <k-field-select v-model="formData.systemTableName"  :data-data="tableNameDict" data-display-field="systemTableName" data-value-field="id" :data-allowblank="false" />
        </k-form-item>

        <k-form-item label="基准日期">
          <k-field-date v-model="formData.sysDataDate" data-type="date"  data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd" :data-allowblank="false"/>
        </k-form-item>


        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit=1 data-accept=".xlsx,.xls"
                          :data-error="onSubmitError" :data-success="onSubmitSuccess" :dataChange="onUploadChange"
                          :dataHttpRequest="httpRequest"
                          :data-auto-upload="false">
          </k-field-upload>

        </k-form-item>


        <k-form-footer data-align="center">

          <k-btn class="btn-custom-primary" ref="submitBtn" data-target="fundNavInfoModelGrid" data-functype="SUBMIT"  data-from="addForm" :data-model="formData" :data-handler="submitUploadParam">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定

          </k-btn>


          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>


    <!--    修改基金净值信息弹出框   -->
    <k-popup ref="editFildList" data-title="修改" @data-opened="editInfo">
      <k-grid ref="editFildListGrid"  data-fixed="right" data-operate-width="400px"  :data-model="formData"  data-action="ImportTemplateManageField01.findTemplateFieldList" >
        <k-grid-column data-header="id" data-name="id" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="系统表名" data-name="systemTableName" data-dict="systemTableNameDict"></k-grid-column>
        <k-grid-column data-header="模板名称" data-name="templateName"></k-grid-column>
        <k-grid-column data-header="导入类型" data-name="importType" data-dict="importTypeDict" ></k-grid-column>
        <k-grid-column data-header="版本号" data-name="version" ></k-grid-column>
        <k-grid-column data-header="模板状态" data-name="templateStatus"   data-dict="templateStatusDict"   ></k-grid-column>
        <k-grid-column data-header="导入人员" data-name="impUsr" ></k-grid-column>
        <k-grid-column data-header="导入时间" data-name="impDateTime"  data-type="date"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-plain" data-functype="SUBMIT" data-action="ImportTemplateManage.updateTemplateInfoStatusOpen" data-size="mini"
                 :data-disabled="scope.row.row.templateStatus === '1'" data-target="fundNavInfoModelGrid" :data-confirm="true" data-descript="启用当前模板">
            修改
          </k-btn>

          <k-btn class="md-danger" data-functype="SUBMIT" data-action="ImportTemplateManage.updateTemplateInfoStatusStop" data-size="mini"
                 :data-disabled="scope.row.row.templateStatus === '0'"  data-target="fundNavInfoModelGrid" :data-confirm="true" data-descript="停用当前模板">
            删除
          </k-btn>

        </template>
      </k-grid>
    </k-popup>



<!--	&lt;!&ndash;    修改基金净值信息弹出框   &ndash;&gt;-->
<!--	<k-popup ref="editFundNavInfoModelPopup" data-title="修改" @data-opened="editInfo">-->
<!--	  <k-form ref="editFundNavInfoModelForm" :data-col="2">-->
<!--      <k-form-item label="id" v-show="false">-->
<!--        <k-field-text v-model="formData.scrId"/>-->
<!--      </k-form-item>-->
<!--      <k-form-item label="市场">-->
<!--        <k-field-select v-model="formData.trxMkt" :data-allowblank="false" data-dict="marketFund" :data-disabled="true"  @data-on-change="fundIdAndNm"/>-->
<!--      </k-form-item>-->
<!--      <k-form-item label="基金代码">-->
<!--        <k-field-select v-model="formData.scrCd" :data-data="fundInfoDict" :data-disabled="true"-->
<!--                      data-display-field="scrCd,scrNm" :data-allowblank="false" data-value-field="scrCd" @data-on-change="setTypeAndNm(false)"/>-->
<!--      </k-form-item>-->
<!--      <k-form-item label="基金名称">-->
<!--        <k-field-select v-model="formData.scrNm" :data-allowblank="false" :data-disabled="true"/>-->
<!--      </k-form-item>-->
<!--      <k-form-item label="基金类型">-->
<!--        <k-field-select v-model="formData.fundType" :data-allowblank="false" :data-disabled="true" data-dict="tr_fund_frs_type"/>-->
<!--      </k-form-item>-->
<!--      <k-form-item label="估值日期">-->
<!--        <k-field-date v-model="formData.stopDt" :data-allowblank="false" :data-disabled="true"/>-->
<!--      </k-form-item>-->

<!--      <k-form-item label="公告日期" v-if="this.formData.trxMkt == 6">-->
<!--        <k-field-date v-model="formData.ntcDt" :data-allowblank="false"/>-->
<!--      </k-form-item>-->
<!--      <k-form-item label="单位净值"  v-if="this.formData.trxMkt == 6">-->
<!--        <k-field-text v-model="formData.untNav" :data-allowblank="false"/>-->
<!--      </k-form-item>-->
<!--      <k-form-item label="收盘价"  v-if="this.formData.trxMkt == 1 || this.formData.trxMkt == 2">-->
<!--        <k-field-text v-model="formData.clsPrc" :data-allowblank="false"/>-->
<!--      </k-form-item>-->


<!--      <k-form-item label="万份收益（元）" v-if="this.formData.fundType == 4">-->
<!--        <k-field-text v-model="formData.tenThsdShrErn" :data-allowblank="false"/>-->
<!--      </k-form-item>-->
<!--      <k-form-item label="七日年化收益率（%）" v-if="this.formData.fundType == 4">-->
<!--        <k-field-text v-model="formData.rct7dAnlYld" :data-allowblank="false"/>-->
<!--      </k-form-item>-->
<!--	    <k-form-footer data-align="center">-->
<!--	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="FundNavInfoModel.updateFundNavInfoModel" data-from="editFundNavInfoModelForm"-->
<!--	        :data-model="formData" data-target="fundNavInfoModelGrid">-->
<!--	        <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定-->
<!--	      </k-btn>-->
<!--	      <k-btn class="btn-custom-plain" data-functype="CLOSE">-->
<!--	        <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>-->
<!--	    </k-form-footer>-->
<!--	  </k-form>-->
<!--	</k-popup>-->
  </div>
</template>

<script>
  import Tools from "@/utils/tools";
  export default {
    name:"FundNavInfoModel",
    data() {
      return {
        fileData: new FormData(),
        portFieldManageInfoGrid:true,
        portFieldManageInfo2Grid:false,
        showEditPrintTempVersionSubmitBtn1:true,
        showSubmitBtn:true,
        editFormData:{
          disclosureModId:'',
          t8PrintTempId:'',
          version:'',
          onlineUrl:'',
          remark:'',
        },
        formData: {
          trxMkt:'',
          scrNm:'',
          fundType:'',
          scrCd:'',
          scrId:''
        },
        Template: false,
        TemplateStatus: false,
        fieldData: {},
        selectRowData: {},
        searchParam:{},
        BreathDay:[],
        fundInfoDict:{},
        tableNameDict:[],
      };
    },
    created() {
      // this.fundIdAndNm();
    },
    mounted: function(){
      this.initTableDict();
    },
    methods: {
      handleUpload() {
        this.formData = {}
      },
      initTableDict(){
        this.httpUtil.comnQuery({
          action: "ImportTemplateManageField01.findTableName",
          params: {dictName: "systemTableNameDict"}
        }).then(data => {
          this.tableNameDict = data.rows;
        }).catch({})
      },


      editInfo(){
        this.fundIdAndNm()
      },


      onSubmitError() {
        this.fileList=[];
        this.fileNameList = [];
        this.$refs.uploadRef.doReset();
        this.$refs.submitBtn.setIconStyle(1);
        this.showSubmitBtn = true;
      },
      onSubmitSuccess() {
        this.fileList=[];
        this.fileNameList = [];
        this.$refs.uploadRef.doReset();
        this.$refs.addForm.reset();
        this.$refs.addImportTemplateManagePopup.close();
        this.$refs.submitBtn.setIconStyle(1);
        this.$refs.fundNavInfoModelGrid.load();
      },
      onUploadChange(file,fileList){
        this.fileData.delete("files");
        this.fileData.append('files', file.file);
        console.log("this.fileList=:>>>",fileList);
        this.fileList = fileList;
        this.fileNameList = [];
        for(let i in this.fileList){
          this.fileNameList.push(this.fileList[i].name);
        }
        if (this.formData.modName ===''||this.formData.modName== null){
          this.$set(this.formData,'modName',this.fileNameList[0].substring(0,this.fileNameList[0].lastIndexOf(".")));
        }
      },

      httpRequest(file){
        const _this = this
        _this.fileList=[];
        _this.fileData.delete('files');
        _this.fileData.append('files', file.file);
        _this.fileList.push(file.filename);
      },


      submitEditPrintTempVersionUploadParam(row){
        let urlPath = window.document.location.href;  //浏览器显示地址 http://10.1.20.88:8201/xxx/xxx
        let docPath = "8201"; //服务器相对地址 8201/xxx/xxx
        let index = urlPath.indexOf(docPath);
        let serverPath = urlPath.substring(0, index);
        let onlineUrl = this.httpUtil.onlineUrl;
        if(onlineUrl!="undefined"&&onlineUrl!=null&&onlineUrl!=""){
          this.editFormData.onlineUrl = onlineUrl;
        }else{
          this.editFormData.onlineUrl = serverPath+"8201";
        }
        this.editFormData.remark = this.formData.remark;
        this.editFormData.disclosureModId = this.formData.id;
        let editFormData = this.editFormData;
        let temp = document.getElementsByClassName('upload-demo');
        let lis = temp[0].childNodes[1].childElementCount;
        if(lis>0){
          this.fileNameList = [];
          for(let i in this.childFileList){
            this.fileNameList.push(this.childFileList[i]);
          }
          this.editFormData['fileNameList'] = JSON.stringify(this.fileNameList);
          let str = '';
          let uploadData = this.editFormData;
          this.fileData.delete('params');
          this.fileData.append('params', JSON.stringify(uploadData));
          this.httpUtil.upload({
            url:"/upload-files/server/DpsApp/importTemplate/uploadFile.json",
            formData: this.fileData
          }).then(res=>{
            this.showEditPrintTempVersionSubmitBtn1 = true;
            this.fileList=[];
            if(res.data.success){
              str = res.data.returnmsg;
              Tools.alert(str);
              this.$refs.editPrintTempVersionPopup1.close();
              this.$refs.printTempVersionGrid.load({disclosureModId: this.formData.id});
            }else{
              str = res.data.returnmsg;
              Tools.alert(str,'danger');
              this.$refs.editPrintTempVersionSubmitBtn.setIconStyle(1, []);
            }
          })
        }else{
          this.fileList=[];
          Tools.alert("上传附件不能为空!","danger");
          this.showEditPrintTempVersionSubmitBtn1 = true;
          this.$refs.editPrintTempVersionSubmitBtn.setIconStyle(1, []);
          return false;
        }
      },


      submitUploadParam() {
        var validate = this.$refs.addForm.validate();
        if(validate==false){
          return false;
        }
        let urlPath = window.document.location.href;  //浏览器显示地址 http://10.1.20.88:8201/xxx/xxx
        let docPath = "8201"; //服务器相对地址 8201/xxx/xxx
        let index = urlPath.indexOf(docPath);
        let serverPath = urlPath.substring(0, index);
        let onlineUrl = this.httpUtil.onlineUrl;
        if(onlineUrl!="undefined"&&onlineUrl!=null&&onlineUrl!=""){
          this.formData.onlineUrl = onlineUrl;
        }else{
          this.formData.onlineUrl = serverPath+"8201";
        }

        this.fileNameList = [];
        if(this.fileList==null || this.fileList.length<=0){
          Tools.alert("上传附件不能为空!","danger");
          return false;
        }
        if (!this.fileList[0].name.endsWith(".xlsx") && !this.fileList[0].name.endsWith(".xls") && !this.fileList[0].name.endsWith(".csv") && !this.fileList[0].name.endsWith(".zip")) {
          Tools.alert("只能上传xlsx、xls文件!","danger");
          this.$refs.uploadRef.doReset();
          return false;
			  }
        let uploadData = this.formData;
        //this.fileData = new FormData();
        this.$refs.uploadRef.upload();
        this.fileData.delete('params');
        this.fileData.append('params', JSON.stringify(uploadData));
        this.$refs.uploadBtnRef.setIconStyle(0);
        this.httpUtil.upload({
          // url:"/upload-files/server/PmsApp/xpdoc/uploadTemp.json",
          url:"/upload-files/server/DpsApp/importTemplate/implData.json",
          formData: this.fileData
        }).then(res=>{
          if(res.data.success){
            Tools.alertTime(res.data.returnmsg, "success", 0);
            this.onSubmitSuccess()
          }else{
            Tools.alertTime(res.data.returnmsg || "上传文件失败！","danger", 0);
          }
          this.$refs.uploadBtnRef.setIconStyle(1);
        }).catch(err=>{
          this.$refs.uploadBtnRef.setIconStyle(1);
        })
        setTimeout(()=>{
          this.$refs.addImportTemplateManagePopup.close();
        }, 300)
        return false
      },

      validateForm(){
        var validate = this.$refs.addForm.validate();
        if(validate==false){
          return false;
        }
        if (validate) {
          let formData = this.formData;
          let temp = document.getElementsByClassName('upload-demo');
          let lis = temp[0].childNodes[1].childNodes.length;
          if(lis>0){
            this.$refs.uploadRef.upload(formData);
          }else{
            return false;
          }
        }
      },


      setExeidBool(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      },


      //校验模板状态
       beforeSubmit(){

        //
        // this.tempData =  res.data();
        //
        // alert( this.tempData);

        if(!this.Template) {
          // alert("模板不存在")
          // return false;
           Tools.confirm(()=>{
            this.$router.push({
              path: '/main/pms/importTemplateManage/ImportTemplateManage',
              query: {
              },
            });
          },'未找到对应模板,是否上传','模板不存在','warning');
          return false;
        }else if (!this.TemplateStatus){
          // alert("模板停用")
          // return false;
          // alert(this.formData.systemTableName)
            Tools.confirm(()=>{

              this.$router.push({
                path: '/main/pms/importTemplateManage/ImportTemplateManage',
                query: {
                  systemTableName: this.formData.systemTableName,
                },
              });
          },'模板未启用，是否启用','模板已停用','warning');
          return false;
        }

      },


      beforeSubmit2(value){


        return new Promise((resolve, reject) => {

          this.httpUtil.comnQuery({
            action: "ImportTemplateManage.findTemplateInfoOrderById",
            params: {systemTableName:this.formData.systemTableName,},
            successAlert: false
          }).then(data => {
            // if (this.formData.reportDate === '' || this.formData.reportDate === undefined
            //   || this.formData.balanceAssets === '' || this.formData.balanceAssets === undefined
            //   || this.formData.financialAssets === '' || this.formData.financialAssets === undefined){
            //   return false;
            // }
            if(data[0].tempData === '' ||   data[0].tempData ===  undefined){
              Tools.alert("该报送日期数据已经存在！","danger");
              this.$refs.addSubmitBtn.loading = false;
              return false;
            } else {
              Tools.alert("6！","danger");
              return false;
            }

          });

        });




      },



    async  changeData() {
        this.httpUtil.comnQuery({
          action: "ImportTemplateManage.findTemplateInfoOrderById",
          params: {systemTableName:this.formData.systemTableName,},
          successAlert: false
        }).then(data => {
          let rows = data.rows;
          // let row = rows[0];
          if(rows[0] ===undefined  ||  rows[0].templateStatus === '' ||   rows[0].templateStatus ===  undefined){
            this.Template =false;
          } else if (rows[0].templateStatus === '0')  {
            this.Template =true;
            this.TemplateStatus = false;
          }else {
            this.Template =true;
            this.TemplateStatus = true;
          }
          // alert('Template:'+this.Template)
          // alert('TemplateStatus:'+this.TemplateStatus)
        });
        return true;
      },
      // setTypeAndNm(value){
      //   this.httpUtil.comnQuery({
      //     action: "FundInfoModel.findFondInfoModelsCdAndNmByScrCd",
      //     params: {scrCd:this.formData.scrCd,}
      //   }).then(data => {
      //     if (value) {
      //       this.$set(this.formData, 'scrId', data.rows[0].scrId);
      //     }
      //     this.$set(this.formData, 'scrNm', data.rows[0].scrNm);
      //     this.$set(this.formData, 'fundType', data.rows[0].wdFrsCtg);
      //   }).catch({})
      // },
      // selectRow(row, column, event) {
      //   this.selectRowData = Object.assign({}, row)
      //   this.formData = Object.assign({}, row)
      //
      //   if(row.importType==='01'){
      //     this.portFieldManageInfoGrid = true;
      //     this.$refs.portFieldManageInfoGrid.load({systemTableName:row.systemTableName});
      //     this.portFieldManageInfo2Grid = false;
      //   }else {
      //     this.portFieldManageInfo2Grid = true;
      //     this.$refs.portFieldManageInfo2Grid.load({systemTableName:row.systemTableName});
      //     this.portFieldManageInfoGrid = false;
      //   }
      //
      // },
      fundIdAndNm(){
        this.fundInfoDict = {};
        this.httpUtil.comnQuery({
          action: "FundInfoModel.findFondInfoModelsCdAndNmByTrxMkt",
          params: {trxMkt:this.formData.trxMkt,}
        }).then(data => {
          this.fundInfoDict = data.rows;
        }).catch({})
      },
    },
    watch: {
      //查询起息日
      BreathDay() {
        console.log(this.BreathDay);
        this.$set(this.searchParam, 'startDate', this.BreathDay == null ? '' : this.BreathDay[0]);
        this.$set(this.searchParam, 'endDate', this.BreathDay == null ? '' : this.BreathDay[1]);
      },
    }
  };
</script>
