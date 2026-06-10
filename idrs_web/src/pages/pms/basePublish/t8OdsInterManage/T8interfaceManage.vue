<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="interfaceManagement" v-model="searchParam" data-label-width="80px" data-target="t8InterfaceList">
        <k-form-item label="接口类型">
          <k-field-select v-model="searchParam.portType" data-dict="interface_type"/>
        </k-form-item>
        <k-form-item label="接口名称">
          <k-field-text v-model="searchParam.portName" data-validate-type="text"/>
        </k-form-item>
        <k-form-item label="接口表名">
          <k-field-text v-model="searchParam.portTable" data-validate-type="text"/>
        </k-form-item>
        <k-form-item label="状态">
          <k-field-select v-model="searchParam.portState" data-dict="interface_status"/>
        </k-form-item>
        <k-form-item label="任务名称">
          <k-field-select v-model="searchParam.pid" data-action="KbatchTaskInfo.findTaskInfos"
                          data-display-field="taskId,taskName"  data-value-field="taskId" />
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.portInfoData={}" slot="button"
            data-target="addInterfacePopup" >
            <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
          <k-btn class="btn-custom-plain" data-functype="POPUP" style="width:100px" :data-handler="()=>this.portInfoData={}" slot="button"
            data-target="dealExecutePopup" >
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>批量处理</k-btn>
          <k-btn class="btn-custom-plain" data-functype="POPUP" :data-handler="()=>this.downloadInfoData={}" slot="button"
            data-target="downloadPortFilePopup" > <md-icon>cloud_download</md-icon>下载</k-btn>
        </div>
      </div>
      <k-grid ref="t8InterfaceList" @data-row-select="selectRow"  data-operate-width="300px"  data-action="T8PortInfoModel.findPortInformation"
              @init="(id)=>{this.$kgrid = id}" :data-checkbox="false" data-checkbox-id="id" >
        <k-grid-column data-align="left" data-header="接口类型" data-dict="interface_type" data-name="portType" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="id" data-name="id" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="接口代码" data-name="portCode" data-width="210" ></k-grid-column>
        <k-grid-column data-align="left" data-header="接口名称" data-name="portName" data-width="180"></k-grid-column>
        <k-grid-column data-header="字符集 " data-name="charset" data-hidden="true"></k-grid-column>
<!--        <k-grid-column data-align="center" data-header="接口exeid" data-name="portExeid"></k-grid-column>-->
        <k-grid-column data-align="left" data-header="文件名" data-name="portAddress"></k-grid-column>
<!--        <k-grid-column data-align="center" data-header="录入柜员" data-name="inputuser"></k-grid-column>-->
        <k-grid-column data-align="left" data-header="创建日期" data-name="crtDate" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="创建时间" data-name="crtTime" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="更新日期" data-name="updDate" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="更新时间" data-name="updTime" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="同步类型" data-name="synchType" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="文件类型" data-name="fileType" data-dict="port_file_type" data-width="70" ></k-grid-column>
<!--        <k-grid-column data-align="center" data-header="接口方向" data-dict="interface_dir" data-name="portDir"></k-grid-column>-->
        <k-grid-column data-align="left" data-header="接口表名" data-name="portTable" data-width="200" ></k-grid-column>
        <k-grid-column data-align="left" data-header="状态" data-name="portState" data-dict="interface_status" data-width="70"></k-grid-column>
        <k-grid-column data-header="关联任务ID " data-name="pid" data-width="70"></k-grid-column>
<!--        <k-grid-column data-align="center" data-header="同步文件跳过行数" data-name="skipRows"></k-grid-column>
        <k-grid-column data-align="center" data-header="是否带分隔符结束" data-dict="1yes0no" data-name="hasEndSeparator"></k-grid-column>-->
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改" data-functype="POPUP" data-size="mini"
                 :data-handler="selectRow" data-target="editPortInfoPopup">
            修改
          </k-btn>
          <k-btn class="btn-custom-text" data-descript="处理" data-functype="POPUP" data-size="mini"
                 :data-handler="selectRow2" data-target="dealPortPopup">
            处理
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="T8PortInfoModel.deletePortInformation"
                 :data-confirm="true" data-size="mini" data-type="danger" data-target="t8InterfaceList" data-descript="删除">
          	删除
    	    </k-btn>

          <k-btn class="btn-custom-text"  data-functype="SUBMIT" data-size="mini" data-action="T8PortInfoModel.recoverStatus"
                  data-target="t8InterfaceList" :data-confirm="true" v-if="scope.row.row.portState == '0'">
            启用
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="T8PortInfoModel.stopStatus" v-if="scope.row.row.portState == '1'"
                 :data-confirm="true" data-size="mini" data-type="danger" data-target="t8InterfaceList" >
            停用
          </k-btn>
          <k-btn class="btn-custom-text" data-descript="新增" data-functype="POPUP" data-size="small"
                 data-target="addPortFieldManageInfoPopup">
            字段新增
          </k-btn>


<!--          <k-field-bswitch data-on-value="N" data-off-value="P" v-model="scope.row.row.status" data-on-action="T8PortInfoModel.recoverStatus"-->
<!--                           data-off-action="T8PortInfoModel.stopStatus" :data-params=scope.row.row :data-confirm="true" data-on-confirm-info="启用"-->
<!--                           data-off-confirm-info="停用" data-target="t8InterfaceList"/>-->
        </template>
      </k-grid>
    </div>

    <div class="py-page-container">
      <k-grid ref="portFieldManageInfoGrid" @data-row-select="setExeidBool" data-operate-width="150px" data-action="PortFieldManageInfo.findPortFieldManageInfos" >
        <k-grid-column data-header="接口代码 " data-name="portCode"></k-grid-column>
        <k-grid-column data-header="id " data-name="id" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="字段代码 " data-name="fieldCode"></k-grid-column>
        <k-grid-column data-header="字段名称 " data-name="fieldName"></k-grid-column>
        <k-grid-column data-header="字段类型 " data-name="fieldType"></k-grid-column>
        <k-grid-column data-header="字段长度 " data-name="fieldLength"></k-grid-column>
        <k-grid-column data-header="字段小数位 " data-name="fieldDights"></k-grid-column>
        <k-grid-column data-header="字段序号 " data-name="fieldSeq"></k-grid-column>
        <k-grid-column data-header="文件字段代码 " data-name="fileFieldCode"></k-grid-column>
        <k-grid-column data-header="录入柜员 " data-name="inputuser"></k-grid-column>
        <k-grid-column data-header="创建日期 " data-name="crtDate" data-type="date"></k-grid-column>
        <k-grid-column data-header="创建时间 " data-name="crtTime" data-type="time"></k-grid-column>
        <k-grid-column data-header="更新日期 " data-name="updDate" data-type="date"></k-grid-column>
        <k-grid-column data-header="更新时间 " data-name="updTime" data-type="time"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改接口文件字段信息" data-functype="POPUP" data-size="mini"
                 :data-handler="setExeidBool" data-target="editPortFieldManageInfoPopup">
            修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="PortFieldManageInfo.deletePortFieldManageInfo"
                 :data-confirm="true" data-size="mini" data-target="portFieldManageInfoGrid" data-descript="删除接口文件字段信息">
            删除
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    添加接口文件字段信息弹出框   -->
    <k-popup ref="addPortFieldManageInfoPopup" data-title="字段新增">
      <k-form ref="addPortFieldManageInfoForm" :data-col="2">
        <k-form-item label="接口代码 ">
          <k-field-text v-model="formData.portCode" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="字段代码 ">
          <k-field-text v-model="formData.fieldCode" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="字段名称 ">
          <k-field-text v-model="formData.fieldName" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="字段类型 ">
          <k-field-text v-model="formData.fieldType" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="字段长度 ">
          <k-field-text v-model="formData.fieldLength" :data-allowblank="false" data-validate-type="int"/>
        </k-form-item>
        <k-form-item label="字段小数位 ">
          <k-field-text v-model="formData.fieldDights" data-validate-type="int"/>
        </k-form-item>
        <k-form-item label="字段序号 ">
          <k-field-text v-model="formData.fieldSeq" :data-allowblank="false" data-validate-type="int"/>
        </k-form-item>
        <k-form-item label="文件字段代码 ">
          <k-field-text v-model="formData.fileFieldCode" :data-allowblank="false"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="PortFieldManageInfo.addPortFieldManageInfo" data-from="addPortFieldManageInfoForm"
                 :data-model="formData" data-target="portFieldManageInfoGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改接口文件字段信息弹出框   -->
    <k-popup ref="editPortFieldManageInfoPopup" data-title="修改接口文件字段信息">
      <k-form ref="editPortFieldManageInfoForm" :data-col="2">
        <k-form-item label="接口代码 ">
          <k-field-text v-model="formData.portCode" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="字段代码 ">
          <k-field-text v-model="formData.fieldCode" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="字段名称 ">
          <k-field-text v-model="formData.fieldName" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="字段类型 ">
          <k-field-text v-model="formData.fieldType" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="字段长度 ">
          <k-field-text v-model="formData.fieldLength" :data-allowblank="false" data-validate-type="int"/>
        </k-form-item>
        <k-form-item label="字段小数位 ">
          <k-field-text v-model="formData.fieldDights" data-validate-type="int"/>
        </k-form-item>
        <k-form-item label="字段序号 ">
          <k-field-text v-model="formData.fieldSeq" :data-allowblank="false" data-validate-type="int"/>
        </k-form-item>
        <k-form-item label="文件字段代码 ">
          <k-field-text v-model="formData.fileFieldCode" :data-allowblank="false"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="PortFieldManageInfo.updatePortFieldManageInfo" data-from="editPortFieldManageInfoForm"
                 :data-model="formData" data-target="portFieldManageInfoGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    新增接口信息弹出框   -->
    <k-popup ref="addInterfacePopup" data-title="新增" style="max-height: 100%;">
      <k-form ref="addInterfaceForm" :data-col="2" style="max-height: 100%;">

        <k-form-item label="接口类型">
          <k-field-select v-model="portInfoData.portType" data-dict="interface_type" :data-allowblank="false" :disabled = "false"/>
        </k-form-item>

        <k-form-item label="接口代码">
          <k-field-text v-model="portInfoData.portCode" data-validate-type="text" :data-allowblank="false" :disabled = "false"/>
        </k-form-item>

        <k-form-item label="接口名称">
          <k-field-text v-model="portInfoData.portName" data-validate-type="text" :data-allowblank="false" :disabled = "false"/>
        </k-form-item>

        <k-form-item label="接口方向">
          <k-field-select v-model="portInfoData.portDir" data-dict="interface_dir" :data-allowblank="false" :disabled = "false"/>
        </k-form-item>

        <k-form-item label="数据来源">
          <k-field-select v-model="portInfoData.sourceFrom" @data-on-change="changeValue" data-dict="source_from" :data-allowblank="false" :disabled = "false" />
        </k-form-item>
        <k-form-item label="字符集" v-show="portInfoData.sourceFrom=='1'">
          <k-field-select v-model="portInfoData.charset" data-dict="charset" :data-allowblank="false" :disabled = "false"/>
        </k-form-item>
        <k-form-item label="源表数据库" v-if="portInfoData.sourceFrom=='0'">
          <k-field-text v-model="portInfoData.sliceDataSource"  :dataAllowblank="portInfoData.sourceFrom!='0'" />
        </k-form-item>
        <k-form-item label="文件类型">
          <k-field-select v-model="portInfoData.fileType" data-dict="port_file_type" :data-allowblank="false" :disabled = "false"/>
        </k-form-item>

        <k-form-item label="xml节点信息" v-if="portInfoData.fileType=='XML'">
          <k-field-text v-model="portInfoData.xmlNodeInfo" data-validate-type="text" :data-allowblank="false" :disabled = "false"/>
        </k-form-item>

        <k-form-item label="源表名" v-if="portInfoData.sourceFrom=='0'">
          <k-field-text v-model="portInfoData.sliceTableName" data-validate-type="text" :dataAllowblank="portInfoData.sourceFrom!='0'"/>
        </k-form-item>
        <k-form-item label="接口表名">
          <k-field-text v-model="portInfoData.portTable" data-validate-type="text" :dataAllowblank="false"/>
        </k-form-item>

        <k-form-item label="分隔符">
          <k-field-select v-model="portInfoData.separator" data-dict="separator" :dataAllowblank="false"/>
        </k-form-item>

        <k-form-item label="文件名">
          <k-field-text v-model="portInfoData.portAddress" data-validate-type="text" :dataAllowblank="false"/>
        </k-form-item>

        <k-form-item label="exeid">
          <k-field-text v-model="portInfoData.portExeid" data-validate-type="text" :dataAllowblank="true"/>
        </k-form-item>

        <k-form-item label="同步文件跳过行数">
          <k-field-text v-model="portInfoData.skipRows" data-validate-type="text"  :dataAllowblank="true"/>
        </k-form-item>

        <k-form-item label="是否带分隔符结束">
          <k-field-select v-model="portInfoData.hasEndSeparator" data-dict="1yes0no" :dataAllowblank="true"/>
        </k-form-item>

        <k-form-item label="文件不存在是否跳过">
          <k-field-select v-model="portInfoData.skipNoFile" data-dict="1yes0no" :dataAllowblank="true"/>
        </k-form-item>

        <k-form-item label="关联任务ID">
          <k-field-text v-model="portInfoData.pid"  :dataAllowblank="true"/>
        </k-form-item>
        <k-form-item label="是否分片处理" :key="portInfoData.sliceFlag" ref="sliceRef">
          <k-field-select v-model="portInfoData.sliceFlag" :data-disabled="portInfoData.sourceFrom=='0'" :dataAllowblank="false" data-dict="xp_if_ok"/>
        </k-form-item>
        <k-form-item label="同步类型">
          <k-field-select v-model="portInfoData.synchType" :dataAllowblank="false" data-dict="synch_type"/>
        </k-form-item>
        <k-form-item label="分片主键" v-if="portInfoData.sourceFrom=='0' && portInfoData.sliceFlag=='1'">
          <k-field-text v-model="portInfoData.splitKey"  :dataAllowblank="!(portInfoData.sourceFrom=='0' && portInfoData.sliceFlag=='1')"/>
        </k-form-item>
        <k-form-item label="源数据查询条件" v-if="portInfoData.sourceFrom=='0' && portInfoData.sliceFlag=='1'">
          <k-field-text v-model="portInfoData.splitWhere"  :dataAllowblank="!(portInfoData.sourceFrom=='0' && portInfoData.sliceFlag=='1')"/>
        </k-form-item>
        <k-form-item label="源数据查询sql" v-if="portInfoData.sourceFrom=='0' && portInfoData.sliceFlag=='1'">
          <k-field-text v-model="portInfoData.selectSql"  :dataAllowblank="!(portInfoData.sourceFrom=='0' && portInfoData.sliceFlag=='1')"/>
        </k-form-item>
        <k-form-item label="源数据分片查询sql" v-if="false">
          <k-field-text v-model="portInfoData.sliceSelectSql"  :dataAllowblank="portInfoData.sliceFlag!='1'"/>
        </k-form-item>
        <k-form-item label="每片最大条数" v-if="portInfoData.sourceFrom=='0' && portInfoData.sliceFlag=='1'">
          <k-field-text v-model="portInfoData.sliceCount"  :dataAllowblank="!(portInfoData.sourceFrom=='0' && portInfoData.sliceFlag=='1')"/>
        </k-form-item>

        <k-form-item label="分片文件存放路径" v-if="false">
          <k-field-text v-model="portInfoData.sliceFilePath"  :dataAllowblank="portInfoData.sliceFlag!='1'"/>
        </k-form-item>
        <k-form-item label="分片文件合并后存放路径" v-if="false">
          <k-field-text v-model="portInfoData.sliceMergePath"  :dataAllowblank="portInfoData.sliceFlag!='1'"/>
        </k-form-item>
        <k-form-item label="读文件插入sql" v-if="false">
          <k-field-text :data-max-length="2000" inputType="textarea" :rows="5" v-model="portInfoData.insertSql"  :dataAllowblank="portInfoData.sliceFlag!='1'"/>
        </k-form-item>
        <k-form-item label="rdf模板内容" v-if="true" :data-col="2">
          <k-field-text :data-max-length="2000" inputType="textarea" :rows="5" v-model="portInfoData.tempContent"  :dataAllowblank="!(portInfoData.sourceFrom=='1' && portInfoData.sliceFlag=='1')"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"  data-from="addInterfacePopup" data-action="T8PortInfoModel.addPortInfo"
                 :data-model="portInfoData" data-target="t8InterfaceList">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定</k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>

      </k-form>
    </k-popup>

    <!--    修改弹出框   -->
    <k-popup ref="editPortInfoPopup" data-title="修改" style="max-height: 100%;">
      <k-form ref="editPortInfoForm" :data-col="2" style="max-height: 100%;">

        <k-form-item label="id" v-show="false">
          <k-field-select v-model="portInfoData.id" data-validate-type="text" :disabled = "true"/>
        </k-form-item>

        <k-form-item label="接口类型">
          <k-field-select v-model="portInfoData.portType" data-dict="interface_type" :data-allowblank="false" :disabled = "false"/>
        </k-form-item>

        <k-form-item label="接口代码">
          <k-field-text v-model="portInfoData.portCode" data-validate-type="text" :data-allowblank="false" :disabled = "false"/>
        </k-form-item>

        <k-form-item label="接口名称">
          <k-field-text v-model="portInfoData.portName" data-validate-type="text" :data-allowblank="false" :disabled = "false"/>
        </k-form-item>

        <k-form-item label="接口方向">
          <k-field-select v-model="portInfoData.portDir" data-dict="interface_dir" :data-allowblank="false" :disabled = "false"/>
        </k-form-item>
        <k-form-item label="数据来源">
          <k-field-select v-model="portInfoData.sourceFrom" data-dict="source_from" @data-on-change="changeValue" :data-allowblank="false" :disabled = "false" />
        </k-form-item>
        <k-form-item label="字符集" v-if="portInfoData.sourceFrom=='1'">
          <k-field-select v-model="portInfoData.charset" data-dict="charset" :data-allowblank="false" :disabled = "false"/>
        </k-form-item>
        <k-form-item label="源表数据库" v-if="portInfoData.sourceFrom=='0'">
          <k-field-text v-model="portInfoData.sliceDataSource"  :dataAllowblank="portInfoData.sourceFrom!='0'" />
        </k-form-item>
        <k-form-item label="文件类型">
          <k-field-select v-model="portInfoData.fileType" data-dict="port_file_type" :data-allowblank="false" :disabled = "false"/>
        </k-form-item>

        <k-form-item label="xml节点信息" v-if="portInfoData.fileType=='XML'">
          <k-field-text v-model="portInfoData.xmlNodeInfo" data-validate-type="text" :data-allowblank="false" :disabled = "false"/>
        </k-form-item>

        <k-form-item label="源表名" v-if="portInfoData.sourceFrom=='0'">
          <k-field-text v-model="portInfoData.sliceTableName" data-validate-type="text" :dataAllowblank="portInfoData.sourceFrom!='0'"/>
        </k-form-item>
        <k-form-item label="接口表名">
          <k-field-text v-model="portInfoData.portTable" data-validate-type="text" :dataAllowblank="false"/>
        </k-form-item>

        <k-form-item label="分隔符">
          <k-field-select v-model="portInfoData.separator" data-dict="separator" :dataAllowblank="false"/>
        </k-form-item>

        <k-form-item label="文件名">
          <k-field-text v-model="portInfoData.portAddress" data-validate-type="text" :dataAllowblank="false"/>
        </k-form-item>

        <k-form-item label="exeid">
          <k-field-text v-model="portInfoData.portExeid" data-validate-type="text" :dataAllowblank="true"/>
        </k-form-item>

        <k-form-item label="同步文件跳过行数">
          <k-field-text v-model="portInfoData.skipRows" data-validate-type="text" :dataAllowblank="true"/>
        </k-form-item>

        <k-form-item label="是否带分隔符结束">
          <k-field-select v-model="portInfoData.hasEndSeparator" data-dict="1yes0no" :dataAllowblank="true"/>
        </k-form-item>

        <k-form-item label="文件不存在是否跳过">
          <k-field-select v-model="portInfoData.skipNoFile" data-dict="1yes0no" :dataAllowblank="true"/>
        </k-form-item>

        <k-form-item label="关联任务ID">
          <k-field-text v-model="portInfoData.pid"  :dataAllowblank="true"/>
        </k-form-item>
        <k-form-item label="是否分片处理" :key="portInfoData.sliceFlag" ref="sliceRef">
          <k-field-select ref="editSliceFlag" v-model="portInfoData.sliceFlag" :data-disabled="portInfoData.sourceFrom=='0'" :dataAllowblank="false" data-dict="xp_if_ok"/>
        </k-form-item>
        <k-form-item label="分片主键" v-if="portInfoData.sourceFrom=='0' && portInfoData.sliceFlag=='1'">
          <k-field-text v-model="portInfoData.splitKey"  :dataAllowblank="!(portInfoData.sourceFrom=='0' && portInfoData.sliceFlag=='1')"/>
        </k-form-item>
        <k-form-item label="源数据查询条件" v-if="portInfoData.sourceFrom=='0' && portInfoData.sliceFlag=='1'">
          <k-field-text v-model="portInfoData.splitWhere"  :dataAllowblank="!(portInfoData.sourceFrom=='0' && portInfoData.sliceFlag=='1')"/>
        </k-form-item>
        <k-form-item label="源数据查询sql" v-if="portInfoData.sourceFrom=='0' && portInfoData.sliceFlag=='1'">
          <k-field-text v-model="portInfoData.selectSql"  :dataAllowblank="!(portInfoData.sourceFrom=='0' && portInfoData.sliceFlag=='1')"/>
        </k-form-item>
        <k-form-item label="源数据分片查询sql" v-if="false">
          <k-field-text v-model="portInfoData.sliceSelectSql"  :dataAllowblank="portInfoData.sliceFlag!='1'"/>
        </k-form-item>
        <k-form-item label="每片最大条数" v-if="portInfoData.sourceFrom=='0' && portInfoData.sliceFlag=='1'">
          <k-field-text v-model="portInfoData.sliceCount"  :dataAllowblank="!(portInfoData.sourceFrom=='0' && portInfoData.sliceFlag=='1')"/>
        </k-form-item>
        <k-form-item label="同步类型">
          <k-field-select v-model="portInfoData.synchType" :dataAllowblank="false" data-dict="synch_type"/>
        </k-form-item>
        <k-form-item label="分片文件存放路径" v-if="false">
          <k-field-text v-model="portInfoData.sliceFilePath"  :dataAllowblank="portInfoData.sliceFlag!='1'"/>
        </k-form-item>
        <k-form-item label="分片文件合并后存放路径" v-if="false">
          <k-field-text v-model="portInfoData.sliceMergePath"  :dataAllowblank="portInfoData.sliceFlag!='1'"/>
        </k-form-item>
        <k-form-item label="读文件插入sql" v-if="false">
          <k-field-text :data-max-length="2000" inputType="textarea" :rows="5" v-model="portInfoData.insertSql"  :dataAllowblank="portInfoData.sliceFlag!='1'"/>
        </k-form-item>
        <k-form-item label="rdf模板内容" v-if="portInfoData.sourceFrom=='1'" :data-col="2">
          <k-field-text  inputType="textarea" :rows="5" v-model="portInfoData.tempContent"  :dataAllowblank="!(portInfoData.sourceFrom=='1' && portInfoData.sliceFlag=='1')"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"  data-from="editPortPopup" data-action="T8PortInfoModel.updPortInfo"
                 :data-model="portInfoData" data-target="t8InterfaceList">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定</k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    批量处理接口信息弹出框   -->
    <k-popup ref="dealExecutePopup" data-title="批量处理">
      <k-form ref="dealExecuteForm" :data-col="2">

        <k-form-item label="接口类型">
          <k-field-select v-model="portInfoData.portType" data-dict="interface_type" :data-allowblank="false" :disabled = "false"/>
        </k-form-item>

        <k-form-item label="接口方向">
          <k-field-select v-model="portInfoData.portDir" data-dict="interface_dir" :data-allowblank="false" :disabled = "false"/>
        </k-form-item>

        <k-form-item label="处理类型">
          <k-field-select v-model="portInfoData.dealType" data-dict="dealtypeDict" data-default-value="1" :data-allowblank="false" :disabled = "false"/>
        </k-form-item>
        <k-form-item label="数据日期" v-if="portInfoData.dealType=='1'">
            <k-field-date v-model="portInfoData.dealDate" data-type="date" data-date-format="yyyyMMdd" :dataAllowblank="false"/>
        </k-form-item>
        <k-form-item label="数据区间"  v-if="portInfoData.dealType=='2'">
          <k-field-date v-model="portInfoData.dealDates" data-type="daterange" data-date-format="yyyy-MM-dd" :dataAllowblank="false"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"  data-from="dealExecutePopup" style="width:100px" ref="batchSubmitButton"
                 :data-model="portInfoData" data-target="t8InterfaceList" @click="batchSubmitToDealExecute">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    处理接口弹出框   -->
    <k-popup ref="dealPortPopup" data-title="处理">
      <k-form ref="dealPortForm" :data-col="2">

        <k-form-item label="id" v-show="false">
          <k-field-display v-model="portInfoData.id" data-validate-type="text" />
        </k-form-item>

        <k-form-item label="接口类型">
          <k-field-display v-model="portInfoData.portType" data-dict="interface_type" />
        </k-form-item>

        <k-form-item label="接口代码">
          <k-field-display v-model="portInfoData.portCode" data-validate-type="text" />
        </k-form-item>

        <k-form-item label="接口名称">
          <k-field-display v-model="portInfoData.portName" data-validate-type="text" />
        </k-form-item>

        <k-form-item label="文件类型">
          <k-field-display v-model="portInfoData.fileType" data-dict="port_file_type" />
        </k-form-item>

        <k-form-item label="xml节点信息" v-if="portInfoData.fileType=='XML'">
          <k-field-display v-model="portInfoData.xmlNodeInfo" data-validate-type="text" />
        </k-form-item>

        <k-form-item label="分隔符">
          <k-field-display v-model="portInfoData.separator" data-dict="separator" />
        </k-form-item>

        <k-form-item label="接口方向">
          <k-field-display v-model="portInfoData.portDir" data-dict="interface_dir" />
        </k-form-item>

        <k-form-item label="地址">
          <k-field-display v-model="portInfoData.portAddress" data-validate-type="text" />
        </k-form-item>

        <k-form-item label="同步文件跳过行数" >
          <k-field-display v-model="portInfoData.skipRows" data-validate-type="text" :dataAllowblank="true"/>
        </k-form-item>

        <k-form-item label="文件不存在是否跳过" >
          <k-field-display v-model="portInfoData.skipNoFile" data-dict="1yes0no" data-validate-type="text" :dataAllowblank="true"/>
        </k-form-item>

        <k-form-item label="是否带分隔符结束" >
          <k-field-display v-model="portInfoData.hasEndSeparator" data-dict="1yes0no" :dataAllowblank="true"/>
        </k-form-item>

        <k-form-item label="同步类型">
          <k-field-display v-model="portInfoData.synchType" data-dict="synch_type"/>
        </k-form-item>

        <k-form-item label="处理类型">
          <k-field-select v-model="portInfoData.dealType" data-dict="dealtypeDict" data-default-value="1" :data-allowblank="false" :disabled = "false"/>
        </k-form-item>
        <k-form-item label="数据日期" v-if="portInfoData.dealType=='1'">
          <k-field-date v-model="portInfoData.dealDate" data-type="date" data-date-format="yyyy-MM-dd" :dataAllowblank="false"/>
        </k-form-item>
        <k-form-item label="数据区间"  v-if="portInfoData.dealType=='2'">
          <k-field-date v-model="portInfoData.dealDates" data-type="daterange" data-date-format="yyyy-MM-dd" :dataAllowblank="false"/>
        </k-form-item>

        <k-form-item label="字符集" v-if="portInfoData.sourceFrom=='1'">
          <k-field-display v-model="portInfoData.charset" data-validate-type="text" data-dict="charset"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" ref="dealBtn" data-functype="SUBMIT"  data-from="dealPortPopup"
                 :data-model="portInfoData" data-target="t8InterfaceList" @click="submitToDeal">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确认
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>


    <!-- 下载弹出框接口 -->
    <k-popup ref="downloadPortFilePopup" data-title="下载">
      <k-form ref="downloadPortFileForm" :data-col="2">

        <k-form-item label="接口类型">
          <k-field-select v-model="downloadInfoData.portType" data-dict="interface_type" :data-allowblank="false" @data-on-change="showDownloadPortAddress"/>
        </k-form-item>

        <k-form-item label="数据日期">
          <k-field-date v-model="downloadInfoData.dealDate" data-type="date" data-date-format="yyyy-MM-dd" :dataAllowblank="false" @data-on-change="showDownloadPortAddress"/>
        </k-form-item>

        <k-form-item label="文件路径">
          <k-field-text v-model="downloadInfoData.portAddress" data-validate-type="text" />
        </k-form-item>

        <k-form-item label="接口地址" v-show="false">
          <k-field-text v-model="downloadInfoData.sftpIp" data-validate-type="text" />
        </k-form-item>

        <k-form-item label="端口号" v-show="false">
          <k-field-text v-model="downloadInfoData.sftpPort" data-validate-type="text" />
        </k-form-item>

        <k-form-item label="用户名" v-show="false">
          <k-field-text v-model="downloadInfoData.username" data-validate-type="text" />
        </k-form-item>

        <k-form-item label="密码" v-show="false">
          <k-field-text v-model="downloadInfoData.password" data-validate-type="text" />
        </k-form-item>

        <k-form-item label="本地路径" v-show="false">
          <k-field-text v-model="downloadInfoData.localPath" data-validate-type="text" />
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" ref="downloadBtn" data-from="downloadPortFilePopup"
                 :data-model="downloadInfoData" data-target="t8InterfaceList" @click="submitDownloadPortFile">
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
    name:"interfaceManagement",
    data() {
      return {
        formData: {},
        selectRowData: {},
        searchParam:{},
        portInfoData:{},
        downloadInfoData:{},
      };
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row);
        this.formData = Object.assign({}, row);
        this.portInfoData = Object.assign({}, row);
        this.$refs.portFieldManageInfoGrid.load({portCode:row.portCode});
      },
      selectRow2(row, column, event) {
        if(row.portType=='4'){
          Tools.alertTime( "wind数据不支持手动执行，请到清算流程中执行！", "danger",3000);
          return false;
        }
        this.selectRowData = Object.assign({}, row);
        this.formData = Object.assign({}, row);
        this.portInfoData = Object.assign({}, row);
        this.$refs.portFieldManageInfoGrid.load({portCode:row.portCode});
      },
      setExeidBool(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      },
      batchSubmitToDealExecute(){
        if(this.portInfoData.portType!=undefined && this.portInfoData.portDir!=undefined && this.portInfoData.dealType!=undefined){
          if((this.portInfoData.dealType=='1' && this.portInfoData.dealDate!=undefined) || (this.portInfoData.dealType=='2' && this.portInfoData.dealDates!=undefined)){
            this.httpUtil.ajax({
              url:"/server/form/DpsApp/disclosure/evaluate/emp/portBatchRecv.action",
              params: this.portInfoData
            }).then(res=>{
              this.$refs.batchSubmitButton.setIconStyle(1, []);
              this.$refs.dealExecutePopup.close();
              this.$refs.t8InterfaceList.load();
              Tools.alert(res.returnmsg);
            })
          }
        }
      },
      /*submitAddPortManageInfo(){//新增接口信息
        this.httpUtil.ajax({
          url:"/server/form/DpsApp/portManage/addPortInfo.action",
          params: this.portInfoData
        }).then(res=>{
          this.$refs.addInterfacePopup.close();
          this.$refs.t8InterfaceList.load();
          Tools.alert(res.returnmsg);
        })
      },
      submitUpdPortManageInfo(){//修改接口信息
        this.httpUtil.ajax({
          url:"/server/form/DpsApp/portManage/updPortInfo.action",
          params: this.portInfoData
        }).then(res=>{
          this.$refs.editPortInfoPopup.close();
          this.$refs.t8InterfaceList.load();
          Tools.alert(res.returnmsg);
        })
      },*/
      submitToDeal(){
        this.httpUtil.ajax({
          url:"/server/form/DpsApp/disclosure/evaluate/emp/portBatchRecv.action",
          params: this.portInfoData
        }).then(res=>{
            if(res.success){
            this.$refs.dealPortPopup.close();
              Tools.alert(res.returnmsg);
            }else{
            this.$refs.dealBtn.setIconStyle(1, []);
            }
        })
      },
      submitDownloadPortFile() {//根据反显路径下载接口文件
        this.httpUtil.ajax({
          url:"download/server/DpsApp/downloadPortFiles.json",
          params: this.downloadInfoData
        }).then(res=>{
          if(!res.success){
            this.$refs.downloadBtn.setIconStyle(1, []);//可执行
            return false;
          }
          this.$refs.downloadBtn.setIconStyle(1, []);//可执行
          this.$refs.downloadPortFilePopup.close();
          this.$refs.t8InterfaceList.load();
        })
      },
      showDownloadPortAddress() {//根据所选接口及日期反显路径
        let deal_date = this.downloadInfoData.dealDate;
        let port_type = this.downloadInfoData.portType;
        if (deal_date == "" || port_type == "" || deal_date == undefined || port_type == undefined) {
          return false;
        }

        this.httpUtil.comnQuery({
          action: "T8PortInfoModel.queryPortAddressByPortType",
          params: this.downloadInfoData,
        }).then(data => {
          this.$set(this.downloadInfoData,"portAddress",data.rows[0].remotePath);
          this.$set(this.downloadInfoData,"sftpIp",data.rows[0].sftpIp);
          this.$set(this.downloadInfoData,"sftpPort",data.rows[0].sftpPort);
          this.$set(this.downloadInfoData,"username",data.rows[0].username);
          this.$set(this.downloadInfoData,"password",data.rows[0].password);
          this.$set(this.downloadInfoData,"localPath",data.rows[0].localPath);
        })
      },
      changeValue(val){
        this.$set(this.portInfoData,"sliceFlag",'1');
        this.$refs.sliceRef.reload();
        console.log("this.portInfoData.sliceFlag=:>.",this.portInfoData.sliceFlag);
      },
     }
  };
</script>
<style lang="scss" scoped>

/deep/  .k-form-body {
  max-height: 100%;
}
</style>
