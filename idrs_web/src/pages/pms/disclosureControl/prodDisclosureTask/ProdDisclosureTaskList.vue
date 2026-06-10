<template>
  <div>
    <div>
      <k-form-search-customize v-model="searchParam" data-target="disclosureProdTaskGrid">
        <k-form-item label="信披类型">
          <k-field-select v-model="searchParam.disclosureType" :data-data="DocTypeDict"   data-value-field="value" data-display-field="text" @data-on-change="changeXpType"></k-field-select>
        </k-form-item>
        <k-form-item label="信披子类型" v-if="searchParam.disclosureType=='5'||searchParam.disclosureType=='6'||searchParam.disclosureType=='1'||searchParam.disclosureType=='9'">
          <k-field-select v-model="searchParam.disclosureSonType" :data-data="addDocTypeDict"
                          data-display-field="text" data-value-field="value"
          ></k-field-select>
        </k-form-item>
        <k-form-item label="产品代码">
          <k-field-select v-model="searchParam.prodCode" data-action="T8ProdInfo.getProdInfosZG" data-value-field="prodCode"
                          data-display-field="prodCode,prodName"/>
        </k-form-item>

        <k-form-item label="产品名称">
          <k-field-text v-model="searchParam.prodName" :data-max-length="100"></k-field-text>
        </k-form-item>

        <k-form-item label="公告生成状态">
          <k-field-select v-model="searchParam.status" data-dict="xp_disclosure_task_status"></k-field-select>
        </k-form-item>

        <k-form-item label="任务所属月份" >
          <k-field-date v-model="taskMonthRange" data-type="daterange" data-date-format="yyyyMM"
                        data-value-format="yyyyMM"/>
        </k-form-item>
        <k-form-item label="基准日期">
          <k-field-date v-model="searchParam.prodBaseDate"/>
        </k-form-item>
        <k-form-item label="任务来源">
          <k-field-select v-model="searchParam.dataSource" data-dict="xp_disclosure_task_source"></k-field-select>
        </k-form-item>
        <k-btn class="btn-custom-primary" slot="button" :data-handler="generateTask" style="width: 90px;" v-if="global.isShowAuthorityButton('DisclosureProdTask.addTaskRightControl')">
          <md-icon md-src="/static/svg/add.svg" />
          生成任务
        </k-btn>
        <k-btn class="btn-custom-primary" slot="button" :data-handler="generateReportData" style="width: 110px;" v-if="global.isShowAuthorityButton('DisclosureProdTask.batchGenerateData')">
          <md-icon md-src="/static/svg/add.svg" />
          <span v-show="showSubmitBtn">
            生成报告数据</span>
          <i v-show="!showSubmitBtn" class="el-icon-loading"/>
        </k-btn>
        <k-btn class="md-success" slot="button" :data-handler="updateReportData" style="width: 110px;" v-if="global.isShowAuthorityButton('DisclosureProdTask.batchGenerateData')">
          <i class="icon-reset" />
          <span v-show="showSubmitBtn1">
          报告数据更新</span>
          <i v-show="!showSubmitBtn1" class="el-icon-loading"/>
        </k-btn>
        <k-btn slot="button" class="md-rose" data-functype="EXPORT" data-target="disclosureProdTaskGrid"
               :data-export-name="'信披任务'">
          <md-icon>cloud_download</md-icon>
          导出
        </k-btn>
      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="disclosureProdTaskGrid" @data-row-select="selectRow" :data-checkbox="true" data-checkbox-id="id" :data-reserve-selection="resetOffset" data-tree-id="id"
              data-action="DisclosureProdTask.findT8DisclosureProdTasksAuth" @init="(grid)=>{this.$kgrid = grid}" :data-operate-column="false">
        <k-grid-column data-align="left" data-header="任务id" data-name="id" :data-hidden="true" ></k-grid-column>
        <k-grid-column data-align="left" data-header="信披类型" data-name="disclosureType" data-width="100" data-dict="xp_doc_type"></k-grid-column>
        <k-grid-column data-align="left" data-header="信披子类型" data-name="disclosureSonType" data-width="120" data-dict="xp_son_type"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品代码" data-name="prodCode" data-width="110"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品名称" data-name="prodName" data-width="140"></k-grid-column>
        <k-grid-column data-align="left" data-header="公告标题" data-name="noticeTitle" data-width="340"></k-grid-column>
        <k-grid-column data-align="left" data-header="基准日期" data-name="prodBaseDate" data-type="date" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="预计生成日期" data-name="sysCrtDate" data-type="date" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="任务所属月份" data-name="taskMonth" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="公告生成状态" data-name="status" data-width="100" data-dict="xp_disclosure_task_status"></k-grid-column>
        <k-grid-column data-align="left" data-header="任务来源" data-name="dataSource" data-dict="xp_disclosure_task_source" data-width="80"></k-grid-column>
        <k-grid-column data-align="left" data-header="任务创建日期" data-name="crtDate" data-type="date" data-render="renderDateTimeCreate" data-width="150"></k-grid-column>
<!--        <k-grid-column data-align="center" data-header="信披状态" data-name="disclosureStatus" data-dict="xp_disclosure_notice_status" data-width="150" :data-hidden="true"></k-grid-column>-->


<!--        <k-grid-column data-align="center" data-header="产品形态" data-name="prodForm" data-dict="xp_prod_form" data-hidden="true" data-export="true"></k-grid-column>-->
<!--        <k-grid-column data-align="center" data-header="销售对象" data-name="prodObj"  data-dict="xp_target_customer" data-hidden="true" data-export="true"></k-grid-column>-->
<!--        <k-grid-column data-align="center" data-header="募集方式" data-name="prodClcMth" data-dict="xp_raise_type" data-hidden="true" data-export="true"></k-grid-column>-->
<!--        <k-grid-column data-align="center" data-header="产品投资性质" data-name="prodInvTyp" data-dict="xp_prod_invest_nature" data-hidden="true" data-export="true"></k-grid-column>-->
<!--        <k-grid-column data-align="center" data-header="产品系列" data-name="prodSerNm" data-hidden="true" data-export="true"></k-grid-column>-->
<!--        <k-grid-column data-align="center" data-header="投资周期长度" data-name="invPrdLen" data-hidden="true" data-export="true"></k-grid-column>-->
<!--        <k-grid-column data-align="center" data-header="投资周期维度" data-name="invPrdDime" data-dict="xp_cycle_dimension" data-hidden="true" data-export="true"></k-grid-column>-->

        <!--        <k-grid-column data-header="任务创建日期" data-name="crtDate" data-type="date"></k-grid-column>
                <k-grid-column data-header="任务创建时间" data-name="crtTime" data-type="time"></k-grid-column>-->
        <k-grid-column data-align="left" data-header="创建人" data-name="crtUserName" data-width="100"></k-grid-column>
      </k-grid>
    </div>
    <k-popup ref="generateTaskPopup" :data-dialog-drag="true">
      <k-form ref="generateForm" :data-col="2">
        <k-form-item label="信披类型">
          <k-field-select v-model="generateData.disclosureType" :data-data="DocTypeDict"   data-value-field="value" data-display-field="text"
                          :data-allowblank="false"></k-field-select>
        </k-form-item>
        <k-form-item label="信披子类型" v-if="generateData.disclosureType==='5'||generateData.disclosureType==='6'||generateData.disclosureType==='1'||generateData.disclosureType==='9'">
          <k-field-select v-model="generateData.disclosureSonType" :data-allowblank="false"
                          :data-data="generateData.addDocTypeDict"
                          data-display-field="text" data-value-field="value" key="disclosureSonType"/>
        </k-form-item>
        <k-form-item label="计划生成月区间" v-show="generateData.disclosureType==='5'">
          <k-field-date v-model="generateData.queryParamDateRange" data-type="daterange" data-date-format="yyyyMM"
                        data-value-format="yyyyMM" :data-allowblank="generateData.disclosureType!=='5'"/>
        </k-form-item>
        <k-form-item label="计划生成日区间" v-show="generateData.disclosureType==='13'">
          <k-field-date v-model="generateData.queryParamDateRanger" data-type="daterange" data-date-format="yyyyMMdd"
                        data-value-format="yyyyMMdd" :data-allowblank="generateData.disclosureType!=='13'"/>
        </k-form-item>
        <k-form-item label="计划生成年份" v-show="generateData.disclosureType==='6'">
          <k-field-date v-model="generateData.prodBaseDate"  data-date-format="yyyy"
                        data-value-format="yyyy" :data-allowblank="generateData.disclosureType!=='6'"/>
        </k-form-item>
        <k-form-item label="基准日期" v-show="generateData.disclosureType==='2'||generateData.disclosureType==='3'||generateData.disclosureType==='1'||generateData.disclosureType==='9'||generateData.disclosureType==='10'||generateData.disclosureType==='12'">
          <k-field-date v-model="generateData.prodBaseDate"
                        :data-allowblank="!(generateData.disclosureType==='2'||generateData.disclosureType==='3'||generateData.disclosureType==='1'||generateData.disclosureType==='9'||generateData.disclosureType==='10'||generateData.disclosureType==='12')"></k-field-date>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="DisclosureProdTask.generateTaskManual"
                 data-from="generateForm"
                 :data-model="generateData" data-target="disclosureProdTaskGrid">
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

import Tools from "@/utils/tools";

export default {
  name:"ProdDisclosureTaskList",
  data() {
    return {
      $kgrid: null,
      addDocTypeDict:{},
      formData: {
        addDocTypeDict:'',
      },
      selectRowData: {},
      searchParam: {},
      queryParamDateRange: [],
      queryParamDateRanger: [],
      saveLoading: false,//是否显示加载图标
      taskMonthRange: [],
      DocTypeDict: {},
      generateData: {
        queryParamDateRange: [],
        queryParamDateRanger: [],
        startMonth: '',
        endMonth: '',
        startDate: '',
        endDate: '',
        disclosureType: '',
        disclosureSonType: '',
        addDocTypeDict: '',
      },
      showSubmitBtn: true,
      showSubmitBtn1: true,
      resetOffset: false,
    };
  },
  watch: {
    'generateData.disclosureType'(value){
      this.$set(this.generateData, 'addDocTypeDict', '');
      this.$set(this.generateData, 'disclosureSonType', '');
      this.$set(this.generateData, 'prodBaseDate', '');
      this.$set(this.generateData, 'queryParamDateRange', '');
      this.$set(this.generateData, 'queryParamDateRanger', '');
      this.disclosureChange(value);
    },
    //查询条件任务所属月份
    taskMonthRange() {
      console.log(this.taskMonthRange);
      this.$set(this.searchParam, 'startMonth', this.taskMonthRange == null ? '' : this.taskMonthRange[0]);
      this.$set(this.searchParam, 'endMonth', this.taskMonthRange == null ? '' : this.taskMonthRange[1]);
    },
    //生成任务窗口中计划生成月份
    queryParamDateRange() {
      this.generateData.startMonth = this.queryParamDateRange[0];
      this.generateData.endMonth = this.queryParamDateRange[1];
    },
    //生成任务窗口中计划生成日期
    queryParamDateRanger() {
      this.generateData.startDate = this.queryParamDateRanger[0];
      this.generateData.endDate = this.queryParamDateRanger[1];
    },
    /** 获取计划生成月份区间 */
    'generateData.queryParamDateRange'() {
      this.generateData.startMonth = this.generateData.queryParamDateRange[0];
      this.generateData.endMonth = this.generateData.queryParamDateRange[1];
    },
    /** 获取计划生成日期区间 */
    'generateData.queryParamDateRanger'() {
      this.generateData.startDate = this.generateData.queryParamDateRanger[0];
      this.generateData.endDate = this.generateData.queryParamDateRanger[1];
    },
    'searchParam.disclosureType'(value) {
      this.$set(this.formData, 'addDocTypeDict', '');
      this.$set(this.searchParam, 'disclosureSonType', '');
      this.disclosureTypeChange(value);
    },
  },
  created() {
    this.xpType();
  },
  methods: {
    changeXpType(disclosureType) {
      this.$set(this.generateData, 'disclosureSonType', '');
      this.httpUtil.comnQuery({
        action: "DisclosureMod.getXPTypeByDocType",
        params: {disclosureType: disclosureType}
      }).then(data => {
        if (disclosureType==='9'){
          delete data.rows[2];//排除净值产品多合并报告
        }
        this.addDocTypeDict = data.rows;
      }).catch({})
    },
    xpType() {
      this.httpUtil.comnQuery({
        action: "DisclosureMod.getXPType",
        params: null
      }).then(data => {
        this.DocTypeDict = data.rows;
      }).catch({})
    },
    renderDateTimeCreate(row) {
      return Tools.formatDateTime(row.crtDate, row.crtTime);
    },
    //行被选中事件
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
    },
    //点击生成任务按钮
    generateTask() {
      this.$set(this.generateData, 'startMonth', '');
      this.$set(this.generateData, 'endMonth', '');
      this.$set(this.generateData, 'startDate', '');
      this.$set(this.generateData, 'endDate', '');
      this.$set(this.generateData, 'prodBaseDate', '');
      this.$set(this.generateData, 'disclosureSonType', '');
      this.$set(this.generateData, 'disclosureType', '');
      this.$set(this.generateData, 'disclosureType', '');
      this.xpType();
      this.$refs.generateTaskPopup.popup();
    },
    //生成报告数据
    generateReportData() {
      //表格被选中的记录
      const _this = this
      const selectedRec = _this.$kgrid.getSelected();
      if (selectedRec.length === 0) {
        Tools.alert('请勾选需要生成报告数据的任务！', 'danger');
        return;
      }
      this.showSubmitBtn = false;
      for(let i=0; i<selectedRec.length; i++){//当没有选中时不会进入
        let pass = true;
        let prodInfo = "";
        let disclosureType = "";
        let disclosureSonType = "";
        let prodBaseDate = "";
        let noticeTitle = "";
        if(selectedRec[i].status === '2'){//已生成公告的任务
          pass = false;
          prodInfo = selectedRec[i].prodName//产品名称
          disclosureType = selectedRec[i].disclosureType//信披类型
          disclosureSonType = selectedRec[i].disclosureSonType//信披类型
          prodBaseDate = selectedRec[i].prodBaseDate//基准日期
          noticeTitle = selectedRec[i].noticeTitle//标题
        }
        if(!pass){
          Tools.alert("公告 ["+noticeTitle+"] 已生成，请勿重复操作",'danger');
          _this.$kgrid.setSelected([]);
          this.showSubmitBtn = true;
          return false;
        }
      }
      //提交到后台
      //如果复选项不为空，生成勾选的报告数据
      if (selectedRec.length > 0) {
        this.httpUtil.comnUpdate({
          action: "DisclosureProdTask.batchGenerateData",
          params: {list: JSON.stringify(selectedRec),update: false}
        }).then(data => {
          this.showSubmitBtn = true;
          _this.$kgrid.setSelected([]);
        })
      }
    },
    //报告数据更新
    updateReportData() {
      //表格被选中的记录
      const _this = this
      const selectedRec = _this.$kgrid.getSelected();
      if (selectedRec.length === 0) {
        Tools.alert('请勾选需要更新报告数据的任务！', 'danger');
        return;
      }
      this.showSubmitBtn1 = false;
      for(let i=0; i<selectedRec.length; i++){//当没有选中时不会进入
        let pass = true;
        let prodInfo = "";
        let disclosureType = "";
        let disclosureSonType = "";
        let prodBaseDate = "";
        let noticeTitle = "";
        if(selectedRec[i].status === '1' ||selectedRec[i].status === '-1'){//未生成公告或生成失败的任务
          pass = false;
          prodInfo = selectedRec[i].prodName//产品名称
          disclosureType = selectedRec[i].disclosureType//信披类型
          disclosureSonType = selectedRec[i].disclosureSonType//信披类型
          prodBaseDate = selectedRec[i].prodBaseDate//基准日期
          noticeTitle = selectedRec[i].noticeTitle//标题
        }
        if(!pass){
          Tools.alert("公告 ["+noticeTitle+"] 还未生成，公告生成后可支持数据更新",'danger');
          _this.$kgrid.setSelected([]);
          this.showSubmitBtn1 = true;
          return false;
        }
        if(selectedRec[i].disclosureStatus==='8'){//已成功发布的公告
          pass = false;
          prodInfo = selectedRec[i].prodName//产品名称
          disclosureType = selectedRec[i].disclosureType//信披类型
          disclosureSonType = selectedRec[i].disclosureSonType//信披类型
          prodBaseDate = selectedRec[i].prodBaseDate//基准日期
          noticeTitle = selectedRec[i].noticeTitle//标题
        }
        if(!pass){
          Tools.alert("公告 ["+noticeTitle+"] 已成功发布，不支持数据更新，公告状态变更后可支持数据更新",'danger');
          _this.$kgrid.setSelected([]);
          this.showSubmitBtn1 = true;
          return false;
        }
      }
      //提交到后台
      //如果复选项不为空，更新勾选的报告数据
      if (selectedRec.length > 0) {
        this.httpUtil.comnUpdate({
          action: "DisclosureProdTask.batchGenerateData",
          params: {list: JSON.stringify(selectedRec),update: true}
        }).then(data => {
          this.showSubmitBtn1 = true;
          _this.$kgrid.setSelected([]);
        })
      }
    },
    //信披类型发生改变
    disclosureTypeChange(value) {
      this.httpUtil.comnQuery({
        action: "DisclosureMod.getXPTypeByDocType",
        params: {disclosureType: value}
      }).then(data => {
        if (value==='9'){
          delete data.rows[2];//排除净值产品多合并报告
        }
        this.formData.addDocTypeDict = data.rows;
      }).catch({})
    },
    disclosureChange(value) {
      this.httpUtil.comnQuery({
        action: "DisclosureMod.getXPTypeByDocType",
        params: {disclosureType: value}
      }).then(data => {
        if (value==='9'){
          delete data.rows[2];//排除净值产品多合并报告
        }
        this.generateData.addDocTypeDict = data.rows;
      }).catch({})
    },
  },
  computed: {},
};
</script>
