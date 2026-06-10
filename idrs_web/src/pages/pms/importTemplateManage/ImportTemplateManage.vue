<template>
  <div class="py-page">
    <div>
      <k-form-search-customize
        data-model-name="FundNavInfoModel"
        data-target="fundNavInfoModelGrid"
        v-model="searchParam"
        data-label-width="80px"
      >
        <!--        <k-form-item label="系统表名">-->
        <!--          <k-field-select v-model="searchParam.systemTableName1" data-dict="systemTableNameDict"></k-field-select>-->
        <!--        </k-form-item>-->
        <k-form-item label="系统表名 ">
          <k-field-select
            v-model="searchParam.systemTableName"
            :data-data="tableNameDict"
            data-display-field="systemTableName"
            data-value-field="id"
          />
        </k-form-item>
        <k-form-item label="模板名称">
          <k-field-text v-model="searchParam.templateName" data-validate-type="text"></k-field-text>
        </k-form-item>
        <k-form-item label="导入类型">
          <k-field-select v-model="searchParam.importType" data-dict="importTypeDict"></k-field-select>
        </k-form-item>
        <k-form-item label="模板状态">
          <k-field-select
            v-model="searchParam.templateStatus"
            data-dict="templateStatusDict"
          ></k-field-select>
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn
            ref="uploadBtnRef"
            class="btn-custom-plain"
            style="width: 100px"
            data-functype="POPUP"
            :data-handler="() => (this.formData = {})"
            data-target="addImportTemplateManagePopup"
            slot="button"
            v-if="global.isShowAuthorityButton('ImportTemplateManage.importTemplate')"
            :load-disabled="false"
          >
            <md-icon md-src="/static/svg/add.svg" />上传模板</k-btn
          >
        </div>
      </div>
      <k-grid
        ref="fundNavInfoModelGrid"
        @data-row-select="selectRow"
        data-operate-width="400px"
        data-action="ImportTemplateManage.findTemplateList"
      >
        <k-grid-column data-header="id" data-name="id" data-hidden="true"></k-grid-column>
        <!--        <k-grid-column data-header="系统表名" data-name="systemTableName" data-dict="systemTableNameDict" data-hidden="true"></k-grid-column>-->
        <k-grid-column data-header="系统表名" data-name="systemTableName" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="系统表名" data-name="tableName"></k-grid-column>
        <k-grid-column data-header="模板名称" data-name="templateName"></k-grid-column>
        <k-grid-column data-header="文件名称" data-name="templateFileName" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="导入类型" data-name="importType" data-dict="importTypeDict"></k-grid-column>
        <k-grid-column data-header="起始行" data-name="rowStart"></k-grid-column>
        <k-grid-column data-header="起始列" data-name="columnStart"></k-grid-column>
        <k-grid-column data-header="跳过列" data-name="skipColumn"></k-grid-column>
        <k-grid-column data-header="版本号" data-name="version"></k-grid-column>
        <k-grid-column
          data-header="模板状态"
          data-name="templateStatus"
          data-dict="templateStatusDict"
        ></k-grid-column>
        <k-grid-column data-header="导入时间" data-name="impDate"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn
            class="btn-custom-text"
            data-functype="SUBMIT"
            data-action="ImportTemplateManage.updateTemplateInfoStatusOpen"
            data-size="mini"
            :data-disabled="scope.row.row.templateStatus === '1'"
            data-target="fundNavInfoModelGrid"
            :data-confirm="true"
            data-descript="启用当前模板"
          >
            启用
          </k-btn>

          <k-btn
            class="btn-custom-text"
            data-functype="SUBMIT"
            data-action="ImportTemplateManage.updateTemplateInfoStatusStop"
            data-size="mini"
            :data-disabled="scope.row.row.templateStatus === '0'"
            data-target="fundNavInfoModelGrid"
            :data-confirm="true"
            data-descript="停用当前模板"
          >
            停用
          </k-btn>

          <!--          <k-btn class="btn-custom-plain" data-functype="SUBMIT" data-action="FundNavInfoModel.deleteFundNavInfoModel" data-size="mini"-->
          <!--                 data-type="danger" data-target="fundNavInfoModelGrid" :data-confirm="true" data-descript="下载当前模板">-->
          <!--            下载-->
          <!--          </k-btn>-->
          <k-btn
            class="btn-custom-text"
            :data-download-name="scope.row.row.templateFileName"
            data-descript="下载文档模板信息"
            data-functype="DOWNLOAD"
            data-size="mini"
            data-url="/download/server/DpsApp/importTemplate/downloadFile.json"
            v-model="scope.row.row"
          >
            下载
          </k-btn>
          <k-btn
            class="btn-custom-text"
            data-descript="新增"
            data-functype="POPUP"
            data-size="small"
            style="width: 80px"
            data-target="addPortFieldManageInfoPopup"
            v-if="scope.row.row.importType === '01'"
          >
            字段配置
          </k-btn>

          <k-btn
            class="btn-custom-text"
            data-descript="新增"
            data-functype="POPUP"
            data-size="small"
            style="width: 80px"
            data-target="addPortFieldManageInfo2Popup"
            v-if="scope.row.row.importType === '02'"
          >
            字段配置
          </k-btn>
          <k-btn
            class="btn-custom-text"
            data-functype="POPUP"
            data-size="mini"
            @click="getTempId(scope.row.row)"
            v-model="scope.row.row"
            data-type="danger"
            data-target="getListPOPUP"
            data-descript="历史版本"
          >
            历史版本
          </k-btn>

          <!--          <k-btn class="btn-custom-plain" data-descript="历史版本" data-functype="POPUP" @click="getTempId(scope.row.row)" data-target="getListPOPUP" v-model="scope.row.row" >-->
          <!--            <md-icon>library_books</md-icon>-->
          <!--          </k-btn>-->
          <k-btn
            class="btn-custom-text"
            data-descript="修改"
            data-functype="POPUP"
            data-size="mini"
            v-if="scope.row.row.importType === '01'"
            data-target="updatePortFieldManageInfo1Popup"
          >
            修改
          </k-btn>
          <k-btn
            class="btn-custom-text"
            data-descript="修改"
            data-functype="POPUP"
            data-size="mini"
            v-if="scope.row.row.importType === '02'"
            data-target="updatePortFieldManageInfoPopup"
          >
            修改
          </k-btn>
        </template>
      </k-grid>
    </div>

    <div class="py-page-container">
      <k-grid
        ref="portFieldManageInfoGrid"
        v-show="portFieldManageInfoGrid"
        @data-row-select="setExeidBool"
        data-operate-width="200px"
        data-action="ImportTemplateManageField01.findTemplateFieldList"
      >
        <k-grid-column data-header="id " data-name="id" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="系统表名" data-name="systemTableName" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="系统表名" data-name="tableName"></k-grid-column>
        <k-grid-column data-header="数据库列字段 " data-name="databaseColumnCode"></k-grid-column>
        <k-grid-column data-header="数据库列名 " data-name="databaseColumnName"></k-grid-column>
        <k-grid-column
          data-header="数据类型 "
          data-name="columnType"
          data-dict="columnTypeDict"
          data-width="80"
        ></k-grid-column>
        <k-grid-column
          data-header="模板列序号 "
          data-name="templateColumnSerial"
          data-width="80"
        ></k-grid-column>
        <k-grid-column data-header="更新人员 " data-name="uptUsr" data-width="80"></k-grid-column>
        <k-grid-column data-header="更新时间 " data-name="uptDate" data-width="90"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn
            class="btn-custom-text"
            data-descript="修改接口文件字段信息"
            data-functype="POPUP"
            data-size="mini"
            :data-handler="setExeidBool"
            data-target="editPortFieldManageInfoPopup"
          >
            修改
          </k-btn>
          <k-btn
            class="btn-custom-text"
            data-functype="SUBMIT"
            data-action="ImportTemplateManageField01.delTemplateFieldInfo"
            :data-confirm="true"
            data-size="mini"
            data-type="danger"
            data-target="portFieldManageInfoGrid"
            data-descript="删除接口文件字段信息"
          >
            删除
          </k-btn>
        </template>
      </k-grid>
      <k-grid
        ref="portFieldManageInfo2Grid"
        v-show="portFieldManageInfo2Grid"
        @data-row-select="setExeidBool"
        data-operate-width="200px"
        data-action="ImportTemplateManageField02.findTemplateFieldList"
      >
        <k-grid-column data-header="id " data-name="id" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="系统表名" data-name="systemTableName" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="系统表名" data-name="tableName"></k-grid-column>
        <k-grid-column data-header="取值行 " data-name="valueRow"></k-grid-column>
        <k-grid-column data-header="取值列 " data-name="valueColumn"></k-grid-column>
        <k-grid-column
          data-header="数据类型 "
          data-name="columnType"
          data-dict="columnTypeDict"
          data-width="80"
        ></k-grid-column>
        <k-grid-column data-header="更新人员 " data-name="uptUsr" data-width="80"></k-grid-column>
        <k-grid-column data-header="更新时间 " data-name="uptDate" data-width="90"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn
            class="btn-custom-text"
            data-descript="修改接口文件字段信息"
            data-functype="POPUP"
            data-size="mini"
            :data-handler="setExeidBool"
            data-target="editPortFieldManageInfo2Popup"
          >
            修改
          </k-btn>
          <k-btn
            class="btn-custom-text"
            data-functype="SUBMIT"
            data-action="ImportTemplateManageField02.delTemplateFieldInfo"
            :data-confirm="true"
            data-size="mini"
            data-type="danger"
            data-target="portFieldManageInfo2Grid"
            data-descript="删除接口文件字段信息"
          >
            删除
          </k-btn>
        </template>
      </k-grid>
    </div>
    <!--    添加接口文件字段信息弹出框   -->
    <k-popup ref="addPortFieldManageInfoPopup" data-title="横表字段配置添加" @data-opened="editInfo">
      <k-form ref="addPortFieldManageInfoForm" :data-col="2">
        <k-form-item label="系统表名 ">
          <k-field-select
            v-model="formData.systemTableName"
            :data-allowblank="false"
            :data-data="tableNameDict"
            data-display-field="systemTableName"
            data-value-field="id"
          />
        </k-form-item>
        <k-form-item label="数据库列字段 ">
          <k-field-select
            v-model="formData.databaseColumnCode"
            :data-allowblank="false"
            :data-data="columnNameDict"
            data-display-field="columnName"
            data-value-field="columnName"
            @data-on-change="setColumnName"
          />
        </k-form-item>
        <k-form-item label="数据库列名 ">
          <k-field-text
            v-model="formData.databaseColumnName"
            :data-allowblank="false"
            :data-disabled="true"
          />
        </k-form-item>
        <k-form-item label="数据类型">
          <k-field-select v-model="formData.columnType" data-dict="columnTypeDict" :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="模板列序号 ">
          <k-field-text v-model="formData.templateColumnSerial" :data-allowblank="false" />
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn
            class="btn-custom-primary"
            data-functype="SUBMIT"
            data-action="ImportTemplateManageField01.addTemplateFieldInfo"
            data-from="addPortFieldManageInfoForm"
            :data-model="formData"
            data-target="portFieldManageInfoGrid"
          >
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>

          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn
          >
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改字段信息弹出框   -->
    <k-popup ref="editPortFieldManageInfoPopup" data-title="横表字段配置编辑" @data-opened="editInfo">
      <k-form ref="editPortFieldManageInfoForm" :data-col="2">
        <k-form-item label="id " v-show="false">
          <k-field-text
            v-model="formData.id"
            :data-allowblank="false"
            :data-disabled="true"
            data-hidden="true"
          />
        </k-form-item>
        <k-form-item label="系统表名 ">
          <k-field-select
            v-model="formData.systemTableName"
            :data-allowblank="false"
            :data-data="tableNameDict"
            data-display-field="systemTableName"
            data-value-field="id"
            :data-disabled="true"
          />
        </k-form-item>
        <k-form-item label="数据库列字段 ">
          <k-field-select
            v-model="formData.databaseColumnCode"
            :data-allowblank="false"
            :data-data="columnNameDict"
            data-display-field="columnName"
            data-value-field="columnName"
            :data-disabled="true"
            @data-on-change="setColumnName"
          />
        </k-form-item>
        <k-form-item label="数据库列名 ">
          <k-field-text
            v-model="formData.databaseColumnName"
            :data-allowblank="false"
            :data-disabled="true"
          />
        </k-form-item>
        <k-form-item label="数据类型">
          <k-field-select v-model="formData.columnType" data-dict="columnTypeDict" :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="模板列序号 ">
          <k-field-text
            v-model="formData.templateColumnSerial"
            data-regx="^[0-9]*$"
            data-regx-text="请输入整数"
            :data-allowblank="false"
          />
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn
            class="btn-custom-primary"
            data-functype="SUBMIT"
            data-action="ImportTemplateManageField01.uptTemplateFieldInfo"
            data-from="editPortFieldManageInfoForm"
            :data-model="formData"
            data-target="portFieldManageInfoGrid"
          >
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn
          >
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="updatePortFieldManageInfo1Popup" data-title="横表字段配置编辑">
      <k-form ref="updatePortFieldManageInfoForm" :data-col="2">
        <k-form-item label="id " v-show="false">
          <k-field-text v-model="formData.id" :data-allowblank="false" data-hidden="true" />
        </k-form-item>
        <k-form-item label="系统表名">
          <k-field-select v-model="formData.tableName" :data-allowblank="false" :data-disabled="true" />
        </k-form-item>
        <k-form-item label="模板名称">
          <k-field-select v-model="formData.templateName" :data-allowblank="false" :data-disabled="true" />
        </k-form-item>
        <k-form-item label="表头所在行">
          <k-field-text v-model="formData.rowStart" :data-allowblank="false" />
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn
            class="btn-custom-primary"
            data-functype="SUBMIT"
            data-action="ImportTemplateManage.updTemplateInfoRowStart"
            data-from="updatePortFieldManageInfoForm"
            :data-model="formData"
            data-target="fundNavInfoModelGrid"
          >
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn
          >
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改起始行列弹出框   -->
    <k-popup ref="updatePortFieldManageInfoPopup" data-title="纵表字段配置编辑">
      <k-form ref="updatePortFieldManageInfoForm" :data-col="2">
        <k-form-item label="id " v-show="false">
          <k-field-text v-model="formData.id" :data-allowblank="false" data-hidden="true" />
        </k-form-item>
        <k-form-item label="系统表名">
          <k-field-select v-model="formData.tableName" :data-allowblank="false" :data-disabled="true" />
        </k-form-item>
        <k-form-item label="模板名称">
          <k-field-select v-model="formData.templateName" :data-allowblank="false" :data-disabled="true" />
        </k-form-item>
        <k-form-item label="起始行">
          <k-field-text v-model="formData.rowStart" :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="起始列">
          <k-field-text v-model="formData.columnStart" :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="跳过列">
          <k-field-text v-model="formData.skipColumn" />
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn
            class="btn-custom-primary"
            data-functype="SUBMIT"
            data-action="ImportTemplateManage.updTemplateInfoRowStart"
            data-from="updatePortFieldManageInfoForm"
            :data-model="formData"
            data-target="fundNavInfoModelGrid"
          >
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn
          >
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    添加接口文件字段信息弹出框   -->
    <k-popup ref="addPortFieldManageInfo2Popup" data-title="纵表字段配置添加">
      <k-form ref="addPortFieldManageInfo2Form" :data-col="2">
        <k-form-item label="系统表名 ">
          <k-field-select
            v-model="formData.systemTableName"
            :data-allowblank="false"
            :data-data="tableNameDict"
            data-display-field="systemTableName"
            data-value-field="id"
          />
        </k-form-item>
        <k-form-item label="取值行 ">
          <k-field-text v-model="formData.valueRow" :data-allowblank="false" data-placeholder="格式：1,3-9" />
        </k-form-item>
        <k-form-item label="取值列 ">
          <k-field-text
            v-model="formData.valueColumn"
            :data-allowblank="false"
            data-placeholder="格式：1,3-9"
          />
        </k-form-item>
        <k-form-item label="数据类型">
          <k-field-select v-model="formData.columnType" data-dict="columnTypeDict" :data-allowblank="false" />
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn
            class="btn-custom-primary"
            data-functype="SUBMIT"
            data-action="ImportTemplateManageField02.addTemplateFieldInfo"
            data-from="addPortFieldManageInfo2Form"
            :data-model="formData"
            data-target="portFieldManageInfoGrid"
          >
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn
          >
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改字段信息弹出框   -->
    <k-popup ref="editPortFieldManageInfo2Popup" data-title="纵表字段配置编辑">
      <k-form ref="editPortFieldManageInfo2Form" :data-col="2">
        <k-form-item label="id" v-show="false">
          <k-field-text
            v-model="formData.id"
            :data-allowblank="false"
            :data-disabled="true"
            data-hidden="true"
          />
        </k-form-item>
        <k-form-item label="系统表名 ">
          <k-field-select
            v-model="formData.systemTableName"
            :data-allowblank="false"
            :data-data="tableNameDict"
            data-display-field="systemTableName"
            data-value-field="id"
            :data-disabled="true"
          />
        </k-form-item>
        <k-form-item label="取值行 ">
          <k-field-text v-model="formData.valueRow" :data-allowblank="false" data-placeholder="格式：1,3-9" />
        </k-form-item>
        <k-form-item label="取值列 " class="form-item-tooltip">
          <k-tooltip data-content="1,2-4会解析为1,2,3,4"></k-tooltip>
          <k-field-text
            v-model="formData.valueColumn"
            :data-allowblank="false"
            data-placeholder="格式：1,3-9"
          />
        </k-form-item>
        <k-form-item label="数据类型">
          <k-field-select v-model="formData.columnType" data-dict="columnTypeDict" :data-allowblank="false" />
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn
            class="btn-custom-primary"
            data-functype="SUBMIT"
            data-action="ImportTemplateManageField02.uptTemplateFieldInfo"
            data-from="editPortFieldManageInfo2Form"
            :data-model="formData"
            data-target="portFieldManageInfoGrid"
          >
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn
          >
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    上传模板   -->
    <k-popup ref="addImportTemplateManagePopup" data-title="上传模板">
      <k-form ref="addForm" :data-col="2">
        <k-form-item label="系统表名 ">
          <k-field-select
            v-model="formData.systemTableName"
            :data-allowblank="false"
            :data-data="tableNameDict"
            data-display-field="systemTableName"
            data-value-field="id"
          />
        </k-form-item>
        <k-form-item label="导入类型">
          <k-field-select v-model="formData.importType" data-dict="importTypeDict" :data-allowblank="false" />
        </k-form-item>

        <k-form-item label="模板名称">
          <k-field-text
            v-model="formData.templateName"
            data-validate-type="text"
            :data-allowblank="false"
          ></k-field-text>
        </k-form-item>

        <k-form-item label="表头所在行" v-show="formData.importType === '01'">
          <k-field-text
            v-model="formData.rowStart"
            data-validate-type="text"
            data-placeholder="从1开始"
            data-default-value="1"
            :data-allowblank="formData.importType === '02'"
          ></k-field-text>
        </k-form-item>

        <k-form-item label="起始行" v-show="formData.importType === '02'">
          <k-field-text
            v-model="formData.rowStart"
            data-validate-type="text"
            data-placeholder="从1开始"
            :data-allowblank="formData.importType === '01'"
          ></k-field-text>
        </k-form-item>
        <k-form-item label="起始列" v-show="formData.importType === '02'">
          <k-field-text
            v-model="formData.columnStart"
            data-validate-type="text"
            data-placeholder="从1开始"
            :data-allowblank="formData.importType === '01'"
          ></k-field-text>
        </k-form-item>
        <k-form-item label="跳过列" v-show="formData.importType === '02'">
          <k-field-text
            v-model="formData.skipColumn"
            data-validate-type="text"
            data-placeholder="从1开始"
          ></k-field-text>
        </k-form-item>
        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload
            data-type="file"
            ref="uploadRef"
            :data-multiple="false"
            :data-limit="1"
            data-accept=".xlsx,.xls"
            :data-error="onSubmitError"
            :data-success="onSubmitSuccess"
            :dataChange="onUploadChange"
            :dataHttpRequest="httpRequest"
            :data-auto-upload="false"
          >
          </k-field-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn
            class="btn-custom-primary"
            ref="submitBtn"
            data-target="fundNavInfoModelGrid"
            data-functype="SUBMIT"
            data-from="addForm"
            :data-model="formData"
            :data-handler="submitUploadParam"
          >
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn
          >
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改基金净值信息弹出框   -->
    <k-popup ref="editFildList" data-title="修改">
      <k-grid
        ref="editFildListGrid"
        data-fixed="right"
        data-operate-width="400px"
        :data-model="formData"
        data-action="ImportTemplateManageField01.findTemplateFieldList"
      >
        <k-grid-column data-header="id" data-name="id" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="系统表名" data-name="tableName"></k-grid-column>
        <k-grid-column data-header="模板名称" data-name="templateName"></k-grid-column>
        <k-grid-column data-header="导入类型" data-name="importType" data-dict="importTypeDict"></k-grid-column>
        <k-grid-column data-header="版本号" data-name="version"></k-grid-column>
        <k-grid-column
          data-header="模板状态"
          data-name="templateStatus"
          data-dict="templateStatusDict"
        ></k-grid-column>
        <k-grid-column data-header="导入人员" data-name="impUsr"></k-grid-column>
        <k-grid-column data-header="导入时间" data-name="impDateTime" data-type="date"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn
            class="btn-custom-text"
            data-functype="SUBMIT"
            data-action="ImportTemplateManage.updateTemplateInfoStatusOpen"
            data-size="mini"
            :data-disabled="scope.row.row.templateStatus === '1'"
            data-target="fundNavInfoModelGrid"
            :data-confirm="true"
            data-descript="启用当前模板"
          >
            修改
          </k-btn>

          <k-btn
            class="btn-custom-text"
            data-functype="SUBMIT"
            data-action="ImportTemplateManage.updateTemplateInfoStatusStop"
            data-size="mini"
            :data-disabled="scope.row.row.templateStatus === '0'"
            data-target="fundNavInfoModelGrid"
            :data-confirm="true"
            data-descript="停用当前模板"
          >
            删除
          </k-btn>
        </template>
      </k-grid>
    </k-popup>
    <!--历史版本   :data-operate-column="false" -->
    <k-popup ref="getListPOPUP" title="历史版本" @data-opened="loadApproveInfo">
      <k-grid
        ref="getAgencyAgreementGrid"
        :data-autoload="false"
        data-action="ImportTemplateManage.findTemplateListHis"
        :dataPopupAppendToBody="true"
      >
        <k-grid-column data-header="id" data-name="id" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="系统表名" data-name="systemTableName" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="系统表名" data-name="tableName"></k-grid-column>
        <k-grid-column data-header="模板名称" data-name="templateName"></k-grid-column>
        <k-grid-column data-header="文件名称" data-name="templateFileName" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="导入类型" data-name="importType" data-dict="importTypeDict"></k-grid-column>
        <k-grid-column data-header="版本号" data-name="version"></k-grid-column>
        <k-grid-column data-header="导入时间" data-name="impDate" data-type="date"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn
            class="btn-custom-text"
            :data-download-name="scope.row.row.templateFileName"
            data-descript="下载"
            data-functype="DOWNLOAD"
            data-size="mini"
            data-url="/download/server/DpsApp/importTemplate/downloadFile.json"
            v-model="scope.row.row"
          >
            下载
          </k-btn>
        </template>
      </k-grid>
    </k-popup>
  </div>
</template>

<script>
import Tools from "@/utils/tools";
import httpUtil from "@/frame/httpUtil";
export default {
	name: "FundNavInfoModel",
	data() {
		return {
			fileData: new FormData(),
			portFieldManageInfoGrid: true,
			portFieldManageInfo2Grid: false,
			showEditPrintTempVersionSubmitBtn1: true,
			showSubmitBtn: true,
			editFormData: {
				disclosureModId: "",
				t8PrintTempId: "",
				version: "",
				onlineUrl: "",
				remark: "",
			},
			formData: {
				trxMkt: "",
				scrNm: "",
				fundType: "",
				scrCd: "",
				scrId: "",
			},
			fieldData: {},
			selectRowData: {},
			searchParam: {},
			BreathDay: [],
			fundInfoDict: {},
			columnNameDict: [],
			tableNameDict: [],
		};
	},

	mounted: function () {
		this.initTableDict();
	},
	methods: {
		initTableDict() {
			this.httpUtil
				.comnQuery({
					action: "ImportTemplateManageField01.findTableName",
					params: { dictName: "systemTableNameDict" },
				})
				.then((data) => {
					this.tableNameDict = data.rows;
				})
				.catch({});
		},

		editInfo() {
			this.initColumnDict();
		},
		onSubmitError() {
			this.fileList = [];
			this.fileNameList = [];
			this.$refs.uploadRef.doReset();
			this.$refs.submitBtn.setIconStyle(1);
			this.showSubmitBtn = true;
		},
		onSubmitSuccess() {
			this.fileList = [];
			this.fileNameList = [];
			this.$refs.uploadRef.doReset();
			this.$refs.addForm.reset();
			this.$refs.addImportTemplateManagePopup.close();
			this.$refs.submitBtn.setIconStyle(1);
			this.$refs.fundNavInfoModelGrid.load();
		},
		onUploadChange(file, fileList) {
			this.fileData.delete("files");
			this.fileData.append("files", file.file);
			console.log("this.fileList=:>>>", fileList);
			this.fileList = fileList;
			this.fileNameList = [];
			for (let i in this.fileList) {
				this.fileNameList.push(this.fileList[i].name);
			}
			if (this.formData.modName === "" || this.formData.modName == null) {
				this.$set(
					this.formData,
					"modName",
					this.fileNameList[0].substring(0, this.fileNameList[0].lastIndexOf("."))
				);
			}
		},

		httpRequest(file) {
			const _this = this;
			_this.fileList = [];
			_this.fileData.delete("files");
			_this.fileData.append("files", file.file);
			_this.fileList.push(file.filename);
		},
		loadApproveInfo() {
			//console.log("this.tempId=:>",this.tempId);
			this.$refs.getAgencyAgreementGrid.load({
				systemTableName: this.systemTableName,
			});
		},
		getTempId(row) {
			this.systemTableName = row.systemTableName;
		},

		submitEditPrintTempVersionUploadParam(row) {
			let urlPath = window.document.location.href; //浏览器显示地址 http://10.1.20.88:8201/xxx/xxx
			let docPath = "8201"; //服务器相对地址 8201/xxx/xxx
			let index = urlPath.indexOf(docPath);
			let serverPath = urlPath.substring(0, index);
			let onlineUrl = this.httpUtil.onlineUrl;
			if (onlineUrl != "undefined" && onlineUrl != null && onlineUrl != "") {
				this.editFormData.onlineUrl = onlineUrl;
			} else {
				this.editFormData.onlineUrl = serverPath + "8201";
			}
			this.editFormData.remark = this.formData.remark;
			this.editFormData.disclosureModId = this.formData.id;
			let editFormData = this.editFormData;
			let temp = document.getElementsByClassName("upload-demo");
			let lis = temp[0].childNodes[1].childElementCount;
			if (lis > 0) {
				this.fileNameList = [];
				for (let i in this.childFileList) {
					this.fileNameList.push(this.childFileList[i]);
				}
				this.editFormData["fileNameList"] = JSON.stringify(this.fileNameList);
				let str = "";
				let uploadData = this.editFormData;
				this.fileData.delete("params");
				this.fileData.append("params", JSON.stringify(uploadData));
				this.httpUtil
					.upload({
						url: "/upload-files/server/DpsApp/importTemplate/uploadFile.json",
						formData: this.fileData,
					})
					.then((res) => {
						this.showEditPrintTempVersionSubmitBtn1 = true;
						this.fileList = [];
						if (res.data.success) {
							str = res.data.returnmsg;
							Tools.alert(str);
							this.$refs.editPrintTempVersionPopup1.close();
							this.$refs.printTempVersionGrid.load({ disclosureModId: this.formData.id });
						} else {
							str = res.data.returnmsg;
							Tools.alert(str, "danger");
							this.$refs.editPrintTempVersionSubmitBtn.setIconStyle(1, []);
						}
					});
			} else {
				this.fileList = [];
				Tools.alert("上传附件不能为空!", "danger");
				this.showEditPrintTempVersionSubmitBtn1 = true;
				this.$refs.editPrintTempVersionSubmitBtn.setIconStyle(1, []);
				return false;
			}
		},

		submitUploadParam() {
			// this.$refs.submitBtn.setIconStyle(0);
			var validate = this.$refs.addForm.validate();
			if (validate == false) {
				// this.$refs.submitBtn.setIconStyle(1);
				return false;
			}
			let urlPath = window.document.location.href; //浏览器显示地址 http://10.1.20.88:8201/xxx/xxx
			let docPath = "8201"; //服务器相对地址 8201/xxx/xxx
			let index = urlPath.indexOf(docPath);
			let serverPath = urlPath.substring(0, index);
			let onlineUrl = this.httpUtil.onlineUrl;
			if (onlineUrl != "undefined" && onlineUrl != null && onlineUrl != "") {
				this.formData.onlineUrl = onlineUrl;
			} else {
				this.formData.onlineUrl = serverPath + "8201";
			}

			this.fileNameList = [];
			if (this.fileList == null || this.fileList.length <= 0) {
				Tools.alert("上传附件不能为空!", "danger");
				this.$refs.submitBtn.setIconStyle(1);
				return false;
			}
      if (!this.fileList[0].name.endsWith(".xlsx") && !this.fileList[0].name.endsWith(".xls") && !this.fileList[0].name.endsWith(".csv")) {
        Tools.alert("只能上传xlsx、xls文件!","danger");
        this.$refs.uploadRef.doReset();
        return false;
      }
      this.$refs.uploadBtnRef.setIconStyle(0);
			let uploadData = this.formData;
			//this.fileData = new FormData();
			this.$refs.uploadRef.upload();
			this.fileData.delete("params");
			this.fileData.append("params", JSON.stringify(uploadData));
			this.httpUtil
				.upload({
					// url:"/upload-files/server/PmsApp/xpdoc/uploadTemp.json",
					url: "/upload-files/server/DpsApp/importTemplate/uploadFile.json",
					formData: this.fileData,
				})
				.then((res) => {
					if (res.data.success) {
						Tools.alertTime(res.data.returnmsg, "success", 0);
			      this.$refs.fundNavInfoModelGrid.load();
					} else {
            Tools.alertTime(res.data.returnmsg || "上传文件失败！", "danger", 0);
					}
          this.$refs.uploadBtnRef.setIconStyle(1);
				}).catch(err=>{
          console.log(err);
          Tools.alertTime("上传文件失败！", "danger", 0);
          this.$refs.uploadBtnRef.setIconStyle(1);
        });
      setTimeout(()=>{
        this.$refs.addImportTemplateManagePopup.close();
      }, 300)
      return false;
		},

		validateForm() {
			var validate = this.$refs.addForm.validate();
			if (validate == false) {
				return false;
			}
			if (validate) {
				let formData = this.formData;
				let temp = document.getElementsByClassName("upload-demo");
				let lis = temp[0].childNodes[1].childNodes.length;
				if (lis > 0) {
					this.$refs.uploadRef.upload(formData);
				} else {
					// Tools.alert("上传文件不能为空!","danger");
					this.showSubmitBtn = true;
					// this.$refs.editPrintTempVersionSubmitBtn.setIconStyle(1, []);
					return false;
				}
			}
		},

		setExeidBool(row, column, event) {
			this.selectRowData = Object.assign({}, row);
			this.formData = Object.assign({}, row);
		},
		selectRow(row, column, event) {
			this.selectRowData = Object.assign({}, row);
			this.formData = Object.assign({}, row);

			if (row.importType === "01") {
				this.portFieldManageInfoGrid = true;
				this.$refs.portFieldManageInfoGrid.load({ systemTableName: row.systemTableName });
				this.portFieldManageInfo2Grid = false;
			} else {
				this.portFieldManageInfo2Grid = true;
				this.$refs.portFieldManageInfo2Grid.load({ systemTableName: row.systemTableName });
				this.portFieldManageInfoGrid = false;
			}
		},
		fundIdAndNm() {
			this.fundInfoDict = {};
			this.httpUtil
				.comnQuery({
					action: "FundInfoModel.findFondInfoModelsCdAndNmByTrxMkt",
					params: { trxMkt: this.formData.trxMkt },
				})
				.then((data) => {
					this.fundInfoDict = data.rows;
				})
				.catch({});
		},
		initColumnDict() {
			let name = this.formData.systemTableName;
			if (!name) {
				return;
			}
			this.httpUtil
				.comnQuery({
					action: "ImportTemplateManageField01.findColumnName",
					params: { systemTableName: name },
				})
				.then((data) => {
					this.columnNameDict = data.rows;
				})
				.catch({});
		},
		setColumnName() {
			let databaseColumnCode = this.formData.databaseColumnCode;
			let name = this.formData.systemTableName;
			this.httpUtil
				.comnQuery({
					action: "ImportTemplateManageField01.findColumnName",
					params: { databaseColumnCode: databaseColumnCode, systemTableName: name },
				})
				.then((data) => {
					this.$set(this.formData, "databaseColumnName", data.rows[0].columnComment);
				})
				.catch({});
		},
	},
	watch: {
		//查询起息日
		BreathDay() {
			console.log(this.BreathDay);
			this.$set(this.searchParam, "startDate", this.BreathDay == null ? "" : this.BreathDay[0]);
			this.$set(this.searchParam, "endDate", this.BreathDay == null ? "" : this.BreathDay[1]);
		},
	},
};
</script>
