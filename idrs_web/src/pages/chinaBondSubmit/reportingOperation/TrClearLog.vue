<template>
  <div class="py-page">
    <div>
      <k-form-search-customize  v-model="queryParam" data-target="trClearLogGrid">

        <k-form-item label="产品到期日">
          <k-field-date v-model="queryParam.workdate"></k-field-date>
        </k-form-item>

        <k-form-item label="产品状态">
          <k-field-select v-model="queryParam.execStatus" data-dict="exec_status"></k-field-select>
        </k-form-item>

      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <k-grid ref="trClearLogGrid" @data-row-select="selectRow" data-action="TrClearLog.findTrClearLogs"  :data-operate-column="false">
        <k-grid-column data-header="交易流水号" data-name="transSerno"></k-grid-column>
        <k-grid-column data-header="流程序号" data-name="stepNo"></k-grid-column>
        <k-grid-column data-header="子流程序号" data-name="stepSubNo"></k-grid-column>
        <k-grid-column data-header="系统工作日" data-name="workdate"></k-grid-column>
        <k-grid-column data-header="业务代码" data-name="busiCode"></k-grid-column>
        <k-grid-column data-header="执行日期" data-name="execDate"></k-grid-column>
        <k-grid-column data-header="开始时间" data-name="startTime"></k-grid-column>
        <k-grid-column data-header="结束时间" data-name="endTime"></k-grid-column>
        <k-grid-column data-header="执行状态" data-name="execStatus"  data-dict="exec_status"  ></k-grid-column>
        <k-grid-column data-header="返回编码" data-name="rtnCode"></k-grid-column>
        <k-grid-column data-header="返回信息" data-name="rtnDesc"></k-grid-column>
        <k-grid-column data-header="更新日期" data-name="updDate"></k-grid-column>
        <k-grid-column data-header="更新时间" data-name="updTime"></k-grid-column>

      </k-grid>
    </div>

    <!--    添加清算流水弹出框   -->
    <k-popup ref="addTrClearLogPopup" data-title="新增">
      <k-form ref="addTrClearLogForm" :data-col="2">
        <k-form-item label="交易流水号">
          <k-field-text v-model="formData.transSerno"/>
        </k-form-item>
        <k-form-item label="流程序号,必需连续,第0号流程用于锁定,小于0的序号属于特殊处理不在流程控制范围内">
          <k-field-text v-model="formData.stepNo"/>
        </k-form-item>
        <k-form-item label="子流程序号">
          <k-field-text v-model="formData.stepSubNo"/>
        </k-form-item>
        <k-form-item label="系统工作日">
          <k-field-text v-model="formData.workdate"/>
        </k-form-item>
        <k-form-item label="业务代码">
          <k-field-text v-model="formData.busiCode"/>
        </k-form-item>
        <k-form-item label="执行日期">
          <k-field-text v-model="formData.execDate"/>
        </k-form-item>
        <k-form-item label="开始时间">
          <k-field-text v-model="formData.startTime"/>
        </k-form-item>
        <k-form-item label="结束时间">
          <k-field-text v-model="formData.endTime"/>
        </k-form-item>
        <k-form-item label="执行状态(
0">
          <k-field-text v-model="formData.execStatus"/>
        </k-form-item>
        <k-form-item label="返回编码">
          <k-field-text v-model="formData.rtnCode"/>
        </k-form-item>
        <k-form-item label="更新日期">
          <k-field-text v-model="formData.updDate"/>
        </k-form-item>
        <k-form-item label="更新时间">
          <k-field-text v-model="formData.updTime"/>
        </k-form-item>
        <k-form-item label="批量文件名">
          <k-field-text v-model="formData.fileName"/>
        </k-form-item>
        <k-form-item label="批量文件批次号">
          <k-field-text v-model="formData.fileSerno"/>
        </k-form-item>
        <k-form-item label="文件记录数">
          <k-field-text v-model="formData.recordnum"/>
        </k-form-item>
        <k-form-item label="TA代码">
          <k-field-text v-model="formData.tano"/>
        </k-form-item>
        <k-form-item label="返回信息">
          <k-field-text v-model="formData.rtnDesc"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="TrClearLog.addTrClearLog"
                 data-from="addTrClearLogForm"
                 :data-model="formData" data-target="trClearLogGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改清算流水弹出框   -->
    <k-popup ref="editTrClearLogPopup" data-title="修改">
      <k-form ref="editTrClearLogForm" :data-col="2">
        <k-form-item label="交易流水号">
          <k-field-text v-model="formData.transSerno"/>
        </k-form-item>
        <k-form-item label="流程序号,必需连续,第0号流程用于锁定,小于0的序号属于特殊处理不在流程控制范围内">
          <k-field-text v-model="formData.stepNo"/>
        </k-form-item>
        <k-form-item label="子流程序号">
          <k-field-text v-model="formData.stepSubNo"/>
        </k-form-item>
        <k-form-item label="系统工作日">
          <k-field-text v-model="formData.workdate"/>
        </k-form-item>
        <k-form-item label="业务代码">
          <k-field-text v-model="formData.busiCode"/>
        </k-form-item>
        <k-form-item label="执行日期">
          <k-field-text v-model="formData.execDate"/>
        </k-form-item>
        <k-form-item label="开始时间">
          <k-field-text v-model="formData.startTime"/>
        </k-form-item>
        <k-form-item label="结束时间">
          <k-field-text v-model="formData.endTime"/>
        </k-form-item>
        <k-form-item label="执行状态(
0">
          <k-field-text v-model="formData.execStatus"/>
        </k-form-item>
        <k-form-item label="返回编码">
          <k-field-text v-model="formData.rtnCode"/>
        </k-form-item>
        <k-form-item label="更新日期">
          <k-field-text v-model="formData.updDate"/>
        </k-form-item>
        <k-form-item label="更新时间">
          <k-field-text v-model="formData.updTime"/>
        </k-form-item>
        <k-form-item label="批量文件名">
          <k-field-text v-model="formData.fileName"/>
        </k-form-item>
        <k-form-item label="批量文件批次号">
          <k-field-text v-model="formData.fileSerno"/>
        </k-form-item>
        <k-form-item label="文件记录数">
          <k-field-text v-model="formData.recordnum"/>
        </k-form-item>
        <k-form-item label="TA代码">
          <k-field-text v-model="formData.tano"/>
        </k-form-item>
        <k-form-item label="返回信息">
          <k-field-text v-model="formData.rtnDesc"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="TrClearLog.updateTrClearLog"
                 data-from="editTrClearLogForm"
                 :data-model="formData" data-target="trClearLogGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
  </div>
</template>

<script>
export default {
  name: "TrClearLog",
  data() {
    return {
      queryParam:{},
      formData: {},
      selectRowData: {}
    };
  },
  methods: {
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
    }
  }
};
</script>
